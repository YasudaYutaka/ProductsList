package br.com.iteris.productslist.dialog

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import br.com.iteris.productslist.databinding.DialogRegisterImageBinding
import br.com.iteris.productslist.extensions.loadImage

class AddImageDialog(private val context: Context) {

    fun showDialog(url : String? = null, onLoadingImage : (image : String) -> Unit) {
        DialogRegisterImageBinding.inflate(LayoutInflater.from(context)).apply {

            // Caso já tenha uma url
            url?.let {
                //registerImageIvProduct.loadImage(it)
                //registerImageEtUrl.setText(it)
            }

            // Ao clicar carregar imagem
            registerImageBtnLoadImage.setOnClickListener {
               // val url1 = registerImageEtUrl.text.toString()
               // registerImageIvProduct.loadImage(url1)
            }

            // Gera dialog
            AlertDialog.Builder(context)
                .setView(root)
                .setPositiveButton("Confirmar") { _, _ ->
                   // val url1 = registerImageEtUrl.text.toString()
                    //onLoadingImage(url1) // lambda
                }
                .setNegativeButton("Cancelar") { _, _ ->}
                .show()

        }
    }

}