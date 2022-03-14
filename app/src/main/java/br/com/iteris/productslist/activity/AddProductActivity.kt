package br.com.iteris.productslist.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.os.bundleOf
import br.com.iteris.productslist.R
import br.com.iteris.productslist.databinding.ActivityAddProductBinding
import br.com.iteris.productslist.model.Product
import br.com.iteris.productslist.viewmodel.ProductsViewModel
import org.koin.android.ext.android.inject

class AddProductActivity : AppCompatActivity() {

    private val viewModel : ProductsViewModel by inject()
    private val binding : ActivityAddProductBinding by lazy { ActivityAddProductBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            addProductBtnSaveProduct.setOnClickListener(btnSaveProductListener)
        }

    }

    // Listener do botão de salvar o produto
    private val btnSaveProductListener = View.OnClickListener {
        val name = binding.addProductEtProductName.text.toString()
        val description = binding.addProductEtProductDescription.text.toString()
        val price = binding.addProductEtProductPrice.text.toString().toDouble()

        val product = Product(name, description, price)
        val intent = Intent().apply {
            putExtras(bundleOf(PRODUCT to product))
        }
        setResult(RESULT_OK, intent)
        finish()
    }


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