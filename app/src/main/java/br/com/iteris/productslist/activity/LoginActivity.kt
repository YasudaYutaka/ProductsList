package br.com.iteris.productslist.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import br.com.iteris.productslist.MainActivity
import br.com.iteris.productslist.database.AppDatabase
import br.com.iteris.productslist.database.dao.UserDao
import br.com.iteris.productslist.databinding.ActivityLoginBinding
import br.com.iteris.productslist.preferences.dataStore
import br.com.iteris.productslist.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private val viewModel : LoginViewModel by inject()
    private val db : AppDatabase by inject()
    private val userDao : UserDao by lazy { db.userDao() }
    private val binding : ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            loginEtUsername.addTextChangedListener(textChangedListener(loginEtUsername, loginTilUsername))
            loginEtPassword.addTextChangedListener(textChangedListener(loginEtPassword, loginTilPassword))

            loginBtnLogin.setOnClickListener(btnLoginListener)
            loginTvSignUp.setOnClickListener(btnSignUpListener)
        }

        // Caso esteja válido os inputs, envia para tela de produtos
        viewModel.isValidState.observe(this, {
            with(binding) {
                val username = loginEtUsername.text.toString()
                val password = loginEtPassword.text.toString()
                autenthicateUser(username, password)
            }
        })

    }

    // Autentica usuário, caso não tenha, mostra Toast
    private fun autenthicateUser(username: String, password: String) {
        lifecycleScope.launch {
            userDao.authenticateUser(username, password)?.let { user ->
                // Guardar no dataStore
                dataStore.edit { pref ->
                    pref[stringPreferencesKey("loggedUser")] = user.id
                }
                // Startar activity de Produtos
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } ?: Toast.makeText(this@LoginActivity, "Falha na autenticação", Toast.LENGTH_SHORT).show()
        }
    }

    // Listener do botao de logar
    private val btnLoginListener = View.OnClickListener {
        with(binding) {
            viewModel.validateInputs(loginEtUsername, loginTilUsername, loginEtPassword, loginTilPassword)
        }
    }

    // Listener do botao de cadastrar novo usuário
    private val btnSignUpListener = View.OnClickListener {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    // Listener de mudan;ca no texto
    private fun textChangedListener(editText: EditText, textInputLayout: TextInputLayout) = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            viewModel.validateSingleInput(editText, textInputLayout)
        }

        override fun afterTextChanged(p0: Editable?) {}
    }


}