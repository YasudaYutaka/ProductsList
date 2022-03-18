package br.com.iteris.productslist.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import br.com.iteris.productslist.Converter
import br.com.iteris.productslist.R
import br.com.iteris.productslist.database.AppDatabase
import br.com.iteris.productslist.database.dao.ProductDao
import br.com.iteris.productslist.databinding.ActivityProductDetailsBinding
import br.com.iteris.productslist.extensions.loadImage
import br.com.iteris.productslist.model.Product
import br.com.iteris.productslist.viewmodel.ProductDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject
import java.text.NumberFormat
import java.util.*

class ProductDetailsActivity : AppCompatActivity() {

    private val binding : ActivityProductDetailsBinding by lazy { ActivityProductDetailsBinding.inflate(layoutInflater) }
    private val product : Product by lazy { intent.extras!!.getSerializable(DETAILS) as Product }
    private val viewModel : ProductDetailsViewModel by inject()
    private val db: AppDatabase by inject()
    private val productDao : ProductDao by lazy { db.productDao() }

    private val getContent = registerForActivityResult(EditProductActivity.ActivityContract()) {
        newProduct ->
            newProduct?.let {
                product.apply { // aplica os novos valores no produto da pagina, com isso o onResume faz o resto
                    name = it.name
                    description = it.description
                    price = it.price
                    image = it.image
                }
                lifecycleScope.launch {
                    productDao.updateProduct(newProduct)
                    val intent = Intent().apply { // seta a resposta
                        putExtras(bundleOf(PAIRPRODUCT to Pair(product, "edit")))
                    }
                    setResult(RESULT_OK, intent)

                    withContext(Dispatchers.Main) {
                        showToast("Produto editado com sucesso")
                    }
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        menuStateObserver() // Observers do menu
    }

    override fun onResume() {
        super.onResume()

        with(binding) {
            productDetailIvProduct.loadImage(product.image?.let {
                Converter.fromByteArrayToBitMap(it)
            })
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
        return super.onOptionsItemSelected(item)
    }

    // Função com os observer dos LiveData do View Model (menu)
    private fun menuStateObserver() {
        // Observer de edição do produto
        viewModel.editState.observe(this, {
            getContent.launch(product)
        })
        // Observer de remoção do produto
        viewModel.removeState.observe(this, {
            GlobalScope.launch {
                productDao.deleteProduct(product)
                val intent = Intent().apply {
                    putExtras(bundleOf(PAIRPRODUCT to Pair(product, "remove")))
                }
                setResult(RESULT_OK, intent)

                withContext(Dispatchers.Main) {
                    showToast("Produto removido com sucesso!")
                }

                finish()
            }
        })
    }

    private fun showToast(msg : String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    // Contrato da Activity
    class ActivityContract : ActivityResultContract<Product, Pair<Product, String>>() {
        override fun createIntent(context: Context, input: Product?) =
            Intent(context, ProductDetailsActivity::class.java).apply {
                putExtras(bundleOf(DETAILS to input))
            }

        override fun parseResult(resultCode: Int, result: Intent?): Pair<Product, String>? {
            if(resultCode != Activity.RESULT_OK) return null
            return result?.getSerializableExtra(PAIRPRODUCT) as Pair<Product, String>?
        }

    }

    companion object {
        private const val DETAILS = "DETAILS"
        private const val PAIRPRODUCT = "PAIRPRODUCT"
    }

}