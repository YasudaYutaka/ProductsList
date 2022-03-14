package br.com.iteris.productslist.model

data class Product(
    var name : String,
    var category : String,
    var price : Double,
    var image : String? =  null
)
