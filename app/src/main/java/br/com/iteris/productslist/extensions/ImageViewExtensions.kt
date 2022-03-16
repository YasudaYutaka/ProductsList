package br.com.iteris.productslist.extensions

import android.graphics.Bitmap
import android.widget.ImageView
import br.com.iteris.productslist.R
import coil.load

fun ImageView.loadImageWithString(url : String? = null) {
    load(url) {
        error(R.drawable.erro)
        fallback(R.drawable.erro)
    }
}

fun ImageView.loadImage(url : Bitmap? = null) {
    load(url) {
        error(R.drawable.erro)
        fallback(R.drawable.erro)
    }
}

