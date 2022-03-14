package br.com.iteris.productslist.model

import java.io.Serializable

data class Product(
    var name : String,
    var description : String,
    var price : Double,
    var image : String? =  null
) : Serializable
