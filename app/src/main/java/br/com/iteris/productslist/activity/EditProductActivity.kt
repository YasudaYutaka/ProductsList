package br.com.iteris.productslist.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.os.bundleOf
import br.com.iteris.productslist.Converter
import br.com.iteris.productslist.extensions.loadImage
import br.com.iteris.productslist.model.Product

class EditProductActivity : AddProductActivity() {

    private val product : Product by lazy { intent.extras?.getSerializable("PRODUCT") as Product }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = "Editar produto"

        byteArray = product.image
        with(binding) {
            addProductEtProductName.setText(product.name)
            addProductEtProductDescription.setText(product.description)
            addProductEtProductPrice.setText(product.price.toString())
            addProductIvProduct.loadImage(product.image?.let { Converter.fromByteArrayToBitMap(it) })
        }

    }

    override val btnSaveProductListener = View.OnClickListener {
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
                byteArray,
                product.id
            )
        }
    }

    override fun loadImageIfNotNull() {
        product.image?.let {
            bindingDialogRegisterImage.registerImageIvProduct.setImageBitmap(Converter.fromByteArrayToBitMap(it))
        }
    }

    // Contrato da Activity
    class ActivityContract : ActivityResultContract<Product, Product>() {
        override fun createIntent(context: Context, input: Product?) =
            Intent(context, EditProductActivity::class.java).apply {
                putExtras(bundleOf("PRODUCT" to input))
            }

        override fun parseResult(resultCode: Int, intent: Intent?): Product? {
            if(resultCode != Activity.RESULT_OK) return null
            return intent?.getSerializableExtra("PRODUCT") as Product?
        }

    }

}
