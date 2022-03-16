package br.com.iteris.productslist.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import br.com.iteris.productslist.Converter
import br.com.iteris.productslist.R
import br.com.iteris.productslist.model.Product
import br.com.iteris.productslist.viewmodel.ProductsViewModel
import java.text.NumberFormat
import java.util.*
import br.com.iteris.productslist.extensions.loadImage

class ProductListAdapter(
    private val viewModel: ProductsViewModel,
    private val getProductDetailsContent: ActivityResultLauncher<Product>
) : RecyclerView.Adapter<ProductListAdapter.ViewHolder>() {

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
        private val tvProductDescription : TextView by lazy { itemView.findViewById(R.id.product_item_tvProductDescription) }
        private val tvPrice : TextView by lazy { itemView.findViewById(R.id.product_item_tvPrice) }
        private val ivProduct : ImageView by lazy { itemView.findViewById(R.id.product_item_ivProduct) }

        fun bind(product : Product) {

            // Verifica se deve ter imagem ou não e seta visibilidade
            ivProduct.visibility = if(product.image == null)View.GONE
                                        else View.VISIBLE
            ivProduct.loadImage(product.image?.let { Converter.fromByteArrayToBitMap(it) })

            tvProductTitle.text = product.name
            tvProductDescription.text = product.description

            // Formata para padrão BRL
            val formatter = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            tvPrice.text = formatter.format(product.price)

            itemView.setOnClickListener {
                getProductDetailsContent.launch(product)
            }
        }
    }

}