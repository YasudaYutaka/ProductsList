package br.com.iteris.productslist

import android.view.View
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.iteris.productslist.activity.AddProductActivity
import br.com.iteris.productslist.adapter.ProductListAdapter
import br.com.iteris.productslist.databinding.ActivityMainBinding
import br.com.iteris.productslist.viewmodel.ProductsViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel : ProductsViewModel by inject()
    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    // Contrato
    private val getContent = registerForActivityResult(AddProductActivity.ActivityContract()) {
        product ->
            product?.let {
                val position = viewModel.updateProduct(product)
                adapter.notifyItemInserted(position)
            }
    }

    private val adapter : ProductListAdapter by lazy { ProductListAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

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

        viewModel.getProductsList()
    }

    override fun onResume() {
        super.onResume()
        println(viewModel.productsList.value)
    }

    // Floating Action Button Listener -> Envia para tela de cadastro de produtos
    private val fabClickListener = View.OnClickListener {
        getContent.launch("a")
    }

}