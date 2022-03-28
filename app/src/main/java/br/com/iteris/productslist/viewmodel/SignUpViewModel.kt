package br.com.iteris.productslist.viewmodel

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.textfield.TextInputLayout

class SignUpViewModel : ViewModel() {

    private val mutableIsValidState = MutableLiveData<Boolean>()
    val isValidState : LiveData<Boolean> get() = mutableIsValidState

    // Valida apenas um input
    fun validateSingleInput(
        input: EditText,
        inputLayout: TextInputLayout
    ) {
        if(input.text.isNotBlank() || input.text.isNotEmpty()) inputLayout.error = ""
    }

    // valida os inputs
    fun validateInputs(
        username: EditText,
        tilUsername: TextInputLayout,
        name : EditText,
        tilName: TextInputLayout,
        password: EditText,
        tilPassword: TextInputLayout,
    ) {
        var isValid = true
        if(username.text.isEmpty() || username.text.isBlank()) {
            tilUsername.error = "Usuário inválido"
            isValid = false
        } else tilUsername.error = ""
        if(name.text.isEmpty() || name.text.isBlank()) {
            tilName.error = "Usuário inválido"
            isValid = false
        } else tilName.error = ""
        if(password.text.isEmpty() || password.text.isBlank()) {
            tilPassword.error = "Senha inválida"
            isValid = false
        } else tilPassword.error = ""

        if(isValid) {
            mutableIsValidState.postValue(true)
        }
    }

}