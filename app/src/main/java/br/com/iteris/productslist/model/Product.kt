package br.com.iteris.productslist.model

import androidx.room.Entity
import java.io.Serializable

@Entity
data class Product(
    var name : String,
    var description : String,
    var price : Double,
    var image : String? =  null
) : Serializable
