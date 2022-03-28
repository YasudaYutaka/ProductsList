package br.com.iteris.productslist

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.iteris.productslist.activity.*
import br.com.iteris.productslist.adapter.ProductListAdapter
import br.com.iteris.productslist.database.AppDatabase
import br.com.iteris.productslist.database.dao.ProductDao
import br.com.iteris.productslist.database.dao.UserDao
import br.com.iteris.productslist.databinding.ActivityMainBinding
import br.com.iteris.productslist.preferences.dataStore
import br.com.iteris.productslist.viewmodel.ProductsViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import org.koin.android.ext.android.inject

class MainActivity : UserBaseActivity() {

    private val viewModel : ProductsViewModel by inject()
    private val db : AppDatabase by inject()
    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val productDao : ProductDao by lazy { db.productDao() }

    // Contrato
    private val getContent = registerForActivityResult(AddProductActivity.ActivityContract()) {
        product ->
            product?.let {
                lifecycleScope.launch {
                    // Salva produto e coloca o novo ID no produto (caso contrario iria como 0 para a lista, ai n dava para remover direto,só se reiniciasse o app)
                    productDao.saveProduct(it).apply {
                        product.id = this
                    }
                    val position = viewModel.addProductToList(product)
                    withContext(Dispatchers.Main) {
                        adapter.notifyItemInserted(position)
                    }
                }
            }
    }

    // Contrato para detalhes do produto
    private val getProductDetailsContent = registerForActivityResult(ProductDetailsActivity.ActivityContract()) {
        pair ->
            pair?.let {
                when(it.second) {
                    "remove" -> {
                        val position = viewModel.removeProductFromList(it.first)
                        adapter.notifyItemRemoved(position)
                    }
                    "edit" -> {
                        println(it.first)
                        val position = viewModel.updateProductInformation(it.first)
                        adapter.notifyItemChanged(position)
                    }
                }
            }
    }

    private val adapter : ProductListAdapter by lazy { ProductListAdapter(viewModel, getProductDetailsContent) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            launch {
                user.filterNotNull().collect {
                    searchUserProducts()
                }
            }
        }


        with(binding) {
            // Inicializa o recycler view
            productListRecyclerView.adapter = adapter
            productListRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)

            productListFabAddProduct.setOnClickListener(fabClickListener) // Seta clickListener para o adapter

        }

        // ProductsList Observer
        viewModel.productsList.observe(this, {
            adapter.notifyDataSetChanged()
        })

        // Profile observer
        viewModel.profileState.observe(this, {
            val intent = Intent(this@MainActivity, UserProfileActivity::class.java)
            startActivity(intent)
        })

    }

    private fun searchUserProducts() {
        lifecycleScope.launch {
            viewModel.updateProductsList(productDao.searchAll())
        }
    }

    // Cria menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_product_list, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Caso item menu selecionado
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.onSelectMenuItem(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    // Floating Action Button Listener -> Envia para tela de cadastro de produtos
    private val fabClickListener = View.OnClickListener {
        getContent.launch("a")
    }

}