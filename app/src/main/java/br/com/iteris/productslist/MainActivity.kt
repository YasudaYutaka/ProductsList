package br.com.iteris.productslist

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import br.com.iteris.productslist.activity.AddProductActivity
import br.com.iteris.productslist.activity.ProductDetailsActivity
import br.com.iteris.productslist.adapter.ProductListAdapter
import br.com.iteris.productslist.database.AppDatabase
import br.com.iteris.productslist.database.dao.ProductDao
import br.com.iteris.productslist.databinding.ActivityMainBinding
import br.com.iteris.productslist.model.Product
import br.com.iteris.productslist.viewmodel.ProductsViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel : ProductsViewModel by inject()
    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var productDao : ProductDao? = null

    // Contrato
    private val getContent = registerForActivityResult(AddProductActivity.ActivityContract()) {
        product ->
            product?.let {
                GlobalScope.launch {
                    productDao?.saveProduct(it) // salva no database
                    val position = viewModel.addProductToList(product)
                    adapter.notifyItemInserted(position)
                }
            }
    }

    // Contrato para detalhes do produto
    private val getProductDetailsContent = registerForActivityResult(ProductDetailsActivity.ActivityContract()) {
        product ->
            product?.let {

            }
    }

    private val adapter : ProductListAdapter by lazy { ProductListAdapter(viewModel, getProductDetailsContent) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        GlobalScope.launch {
            // Database
            val db = AppDatabase.instanceDatabase(this@MainActivity)
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
            println("asdasdasdas"+ viewModel.productsList.value)
        })

        //viewModel.getProductsList()
    }

    // Floating Action Button Listener -> Envia para tela de cadastro de produtos
    private val fabClickListener = View.OnClickListener {
        getContent.launch("a")
    }

}