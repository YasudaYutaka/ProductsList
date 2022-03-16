package br.com.iteris.productslist.viewmodel

import android.widget.EditText
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.iteris.productslist.model.Product
import com.google.android.material.textfield.TextInputLayout

class AddProductViewModel : ViewModel() {

    private val mutableIsValid = MutableLiveData<Product>()
    val isValid : LiveData<Product> get() = mutableIsValid

    // Valida apenas um input
    fun validateSingleInput(
        input: EditText,
        inputLayout: TextInputLayout
    ) {
        if(input.text.isNotBlank() || input.text.isNotEmpty()) inputLayout.error = ""
    }

    // valida os inputs
    fun validateInputs(
        name: EditText,
        tilName: TextInputLayout,
        description: EditText,
        tilDescription: TextInputLayout,
        price: EditText,
        tilPrice: TextInputLayout,
        image : String? = null,
        idProduct : Long = 0L
    ) {
        var isValid = true
        if(name.text.isEmpty() || name.text.isBlank()) {
            tilName.error = "Nome inválido"
            isValid = false
        } else tilName.error = ""
        if(description.text.isEmpty() || description.text.isBlank()) {
            tilDescription.error = "Descriçao inválida"
            isValid = false
        } else tilDescription.error = ""
        if(price.text.isEmpty() || price.text.isBlank()) {
            tilPrice.error = "Valor inválido"
            isValid = false
        } else tilPrice.error = ""

        if(isValid) {
            val product = Product(
                id = idProduct, // precisa disso por conta que estou utilizando no editProduct tmb
                name = name.text.toString(),
                description = description.text.toString(),
                price = price.text.toString().toDouble(),
                image = image
            )
            mutableIsValid.postValue(product)
        }
    }

}