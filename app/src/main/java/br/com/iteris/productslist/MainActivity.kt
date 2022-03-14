package br.com.iteris.productslist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.iteris.productslist.adapter.ProductListAdapter
import br.com.iteris.productslist.databinding.ActivityMainBinding
import br.com.iteris.productslist.model.repository.ProductsRepository
import br.com.iteris.productslist.viewmodel.ProductsViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private val viewModel : ProductsViewModel by inject()
    private val binding : ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val adapter : ProductListAdapter by lazy { ProductListAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
            productListRecyclerView.adapter = adapter
            productListRecyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
        }

        viewModel.productsList.observe(this, {
            adapter.notifyDataSetChanged()
        })

        viewModel.getProductsList()
    }

}