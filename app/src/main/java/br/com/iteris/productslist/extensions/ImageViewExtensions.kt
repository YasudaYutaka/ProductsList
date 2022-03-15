package br.com.iteris.productslist.extensions

import android.widget.ImageView
import br.com.iteris.productslist.R
import coil.load

fun ImageView.loadImage(url : String? = null) {
    load(url) {
        error(R.drawable.erro)
        fallback(R.drawable.erro)
    }
}

