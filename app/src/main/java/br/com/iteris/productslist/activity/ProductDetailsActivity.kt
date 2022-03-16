package br.com.iteris.productslist.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.os.bundleOf
import br.com.iteris.productslist.R
import br.com.iteris.productslist.database.AppDatabase
import br.com.iteris.productslist.database.dao.ProductDao
import br.com.iteris.productslist.databinding.ActivityProductDetailsBinding
import br.com.iteris.productslist.extensions.loadImage
import br.com.iteris.productslist.model.Product
import br.com.iteris.productslist.viewmodel.ProductDetailsViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import java.text.NumberFormat
import java.util.*

class ProductDetailsActivity : AppCompatActivity() {

    private val binding : ActivityProductDetailsBinding by lazy { ActivityProductDetailsBinding.inflate(layoutInflater) }
    private val product : Product by lazy { intent.extras!!.getSerializable(DETAILS) as Product }
    private val viewModel : ProductDetailsViewModel by inject()
    private val db: AppDatabase by lazy { AppDatabase.instanceDatabase(this) }
    private var productDao : ProductDao? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        productDao = db.productDao()

        with(binding) {
            productDetailIvProduct.loadImage(product.image)
            val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            productDetailChipPrice.text = formatter.format(product.price)
            productDetailTvName.text = product.name
            productDetailTvDescription.text = product.description
        }


    }

    // Menu (delete & edit)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_product_details, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Listener do menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.onSelectMenuItem(item.itemId)
        menuStateObserver()
        return super.onOptionsItemSelected(item)
    }

    // Função com os observer dos LiveData do View Model (menu)
    private fun menuStateObserver() {
        viewModel.editState.observe(this, {
            println("edit")
        })
        viewModel.removeState.observe(this, {
            GlobalScope.launch {
                productDao?.deleteProduct(product)
                val intent = Intent().apply {
                    putExtras(bundleOf(DETAILS to product))
                }
                setResult(RESULT_OK, intent)
                finish()
            }
        })
    }

    // Contrato da Activity
    class ActivityContract : ActivityResultContract<Product, Product>() {
        override fun createIntent(context: Context, input: Product?) =
            Intent(context, ProductDetailsActivity::class.java).apply {
                putExtras(bundleOf(DETAILS to input))
            }

        override fun parseResult(resultCode: Int, result: Intent?): Product? {
            if(resultCode != Activity.RESULT_OK) return null
            return result?.getSerializableExtra(DETAILS) as Product?
        }

    }

    companion object {
        private const val DETAILS = "DETAILS"
    }

}