package br.com.iteris.productslist.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import br.com.iteris.productslist.MainActivity
import br.com.iteris.productslist.databinding.ActivityLoginBinding
import br.com.iteris.productslist.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject

class LoginActivity : AppCompatActivity() {

    private val viewModel : LoginViewModel by inject()
    private val binding : ActivityLoginBinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {

            loginEtUsername.addTextChangedListener(textChangedListener(loginEtUsername, loginTilUsername))
            loginEtPassword.addTextChangedListener(textChangedListener(loginEtPassword, loginTilPassword))

            loginBtnLogin.setOnClickListener(btnLoginListener)
        }

        viewModel.isValidState.observe(this, {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        })

    }

    // Listener do botao de logar
    private val btnLoginListener = View.OnClickListener {
        with(binding) {
            viewModel.validateInputs(loginEtUsername, loginTilUsername, loginEtPassword, loginTilPassword)
        }
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