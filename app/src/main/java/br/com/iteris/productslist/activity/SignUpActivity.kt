package br.com.iteris.productslist.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import br.com.iteris.productslist.R
import br.com.iteris.productslist.database.AppDatabase
import br.com.iteris.productslist.database.dao.UserDao
import br.com.iteris.productslist.databinding.ActivitySignUpBinding
import br.com.iteris.productslist.model.User
import br.com.iteris.productslist.viewmodel.SignUpViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SignUpActivity : AppCompatActivity() {

    private val viewModel : SignUpViewModel by inject()
    private val db : AppDatabase by inject()
    private val dao : UserDao by lazy { db.userDao() }
    private val binding : ActivitySignUpBinding by lazy { ActivitySignUpBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            signUpEtUsername.addTextChangedListener(textChangedListener(signUpEtUsername, signUpTilUsername))
            signUpEtName.addTextChangedListener(textChangedListener(signUpEtName, signUpTilName))
            signUpEtPassword.addTextChangedListener(textChangedListener(signUpEtPassword, signUpTilPassword))

            signUpBtnLogin.setOnClickListener(btnSignUpListener)
        }

        // Caso a validaçao esteja correta
        viewModel.isValidState.observe(this, {
            val newUser = createUser()
            lifecycleScope.launch {
                try {
                    dao.saveUser(newUser) // Cria user no db
                    finish()
                } catch (e: Exception) {
                    Log.e("erro", "error: ", e)
                    Toast.makeText(this@SignUpActivity, "Falha ao cadastrar o usuário", Toast.LENGTH_SHORT).show()
                }
            }
        })

    }

    // Cria um objeto User usando os dados dos inputs
    private fun createUser() : User {
        with(binding) {
            val username = signUpEtUsername.text.toString()
            val name = signUpEtName.text.toString()
            val password = signUpEtPassword.text.toString()
            return User(username, name, password)
        }
    }


    // Listener do botão de cadastrar
    private val btnSignUpListener = View.OnClickListener {
        with(binding) {
            viewModel.validateInputs(signUpEtUsername, signUpTilUsername, signUpEtName, signUpTilName, signUpEtPassword, signUpTilPassword)
        }
    }

    // Listener de mudança no texto
    private fun textChangedListener(editText: EditText, textInputLayout: TextInputLayout) = object :
        TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            viewModel.validateSingleInput(editText, textInputLayout)
        }

        override fun afterTextChanged(p0: Editable?) {}
    }

}