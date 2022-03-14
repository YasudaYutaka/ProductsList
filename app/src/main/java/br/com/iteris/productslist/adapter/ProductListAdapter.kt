package br.com.iteris.productslist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.iteris.productslist.R
import br.com.iteris.productslist.model.Product
import br.com.iteris.productslist.viewmodel.ProductsViewModel
import coil.load

class ProductListAdapter(private val viewModel : ProductsViewModel) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(viewModel.getProductByPosition(position))
    }

    override fun getItemCount(): Int = viewModel.productsSize

    inner class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {

        private val tvProductTitle : TextView by lazy { itemView.findViewById(R.id.product_item_tvProductTitle) }
        private val tvProductCategory : TextView by lazy { itemView.findViewById(R.id.product_item_tvProductCategory) }
        private val tvPrice : TextView by lazy { itemView.findViewById(R.id.product_item_tvPrice) }
        private val ivProduct : ImageView by lazy { itemView.findViewById(R.id.product_item_ivProduct) }

        fun bind(product : Product) {
            tvProductTitle.text = product.name
            tvProductCategory.text = product.category
            tvPrice.text = product.price.toString()
            ivProduct.load(product.image)
        }
    }

}