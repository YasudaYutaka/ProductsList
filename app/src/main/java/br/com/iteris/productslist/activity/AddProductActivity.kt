package br.com.iteris.productslist.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import br.com.iteris.productslist.databinding.ActivityAddProductBinding
import br.com.iteris.productslist.dialog.AddImageDialog
import br.com.iteris.productslist.extensions.loadImage
import br.com.iteris.productslist.model.Product
import br.com.iteris.productslist.viewmodel.AddProductViewModel
import com.google.android.material.textfield.TextInputLayout
import org.koin.android.ext.android.inject

open class AddProductActivity : AppCompatActivity() {

    protected val viewModel : AddProductViewModel by inject()
    protected val binding : ActivityAddProductBinding by lazy { ActivityAddProductBinding.inflate(layoutInflater) }
    protected var url : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            addProductBtnSaveProduct.setOnClickListener(btnSaveProductListener)

            // Input validation
            addProductEtProductName.addTextChangedListener(editTextListener(addProductEtProductName, addProductTilProductName))
            addProductEtProductDescription.addTextChangedListener(editTextListener(addProductEtProductDescription, addProductTilProductDescription))
            addProductEtProductPrice.addTextChangedListener(editTextListener(addProductEtProductPrice, addProductTilProductPrice))

            // Listener para abrir dialog de imagem
            addProductIvProduct.setOnClickListener(ivLoadImageListener)
        }

        // Caso todos editText sejam válidos
        viewModel.isValid.observe(this, {
            onSuccess(it)
        })

    }

    // Listener do botão de salvar o produto
    protected open val btnSaveProductListener = View.OnClickListener {
        with(binding) {
            addProductBtnSaveProduct.isClickable = false
            // Valida os inputs
            viewModel.validateInputs(
                addProductEtProductName,
                addProductTilProductName,
                addProductEtProductDescription,
                addProductTilProductDescription,
                addProductEtProductPrice,
                addProductTilProductPrice,
                url
            )
        }
    }

    // Caso de sucesso validação dos campos
    private fun onSuccess(product : Product) {
        val intent = Intent().apply {
            putExtras(bundleOf(PRODUCT to product))
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    // Edit Text Listener
    private fun editTextListener(input : EditText, inputLayout : TextInputLayout) = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            viewModel.validateSingleInput(input, inputLayout)
            binding.addProductBtnSaveProduct.isClickable = true
        }

        override fun afterTextChanged(p0: Editable?) {}

    }

    // Listener para abrir dialog de cadastrar imagem
    private val ivLoadImageListener = View.OnClickListener {
        // Abre o dialog com um lambda quando da certo
        AddImageDialog(this).showDialog(url) { image ->
            url = image
            binding.addProductIvProduct.loadImage(image)
        }
    }

    // Contrato da activity
    class ActivityContract : ActivityResultContract<String, Product>() {
        override fun createIntent(context: Context, input: String) =
            Intent(context, AddProductActivity::class.java)

        override fun parseResult(resultCode: Int, result: Intent?): Product? {
            if(resultCode != Activity.RESULT_OK) return null
            return result?.getSerializableExtra(PRODUCT) as Product?
        }
    }

    companion object {
        private const val PRODUCT = "PRODUCT"
    }

}