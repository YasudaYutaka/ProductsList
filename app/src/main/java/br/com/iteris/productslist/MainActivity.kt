package br.com.iteris.productslist

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.iteris.productslist.activity.AddProductActivity
import br.com.iteris.productslist.activity.ProductDetailsActivity
import br.com.iteris.productslist.adapter.ProductListAdapter
import br.com.iteris.productslist.database.AppDatabase
import br.com.iteris.productslist.database.dao.ProductDao
import br.com.iteris.productslist.databinding.ActivityMainBinding
import br.com.iteris.productslist.viewmodel.ProductsViewModel
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel : ProductsViewModel by inject()
    private val db : AppDatabase by inject()
    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var productDao : ProductDao? = null

    // Contrato
    private val getContent = registerForActivityResult(AddProductActivity.ActivityContract()) {
        product ->
            product?.let {
                lifecycleScope.launch {
                    // Salva produto e coloca o novo ID no produto (caso contrario iria como 0 para a lista, ai n dava para remover direto,só se reiniciasse o app)
                    productDao?.saveProduct(it).apply {
                        product.id = this!!
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
            // Database
            productDao = db.productDao()
            viewModel.updateProductsList(productDao!!.searchAll())
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

        //viewModel.getProductsList()
    }

    // Floating Action Button Listener -> Envia para tela de cadastro de produtos
    private val fabClickListener = View.OnClickListener {
        getContent.launch("a")
    }

}