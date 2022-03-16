package br.com.iteris.productslist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.ByteArrayOutputStream

class Converter {

    companion object {
        fun fromBitmapToByteArray(bitmap: Bitmap) : ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            return stream.toByteArray()
        }

        fun fromByteArrayToBitMap(byteArray: ByteArray) : Bitmap {
            return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
        }
    }

}