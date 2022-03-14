package br.com.iteris.productslist.model.repository

import br.com.iteris.productslist.model.Product

class ProductsRepository {

    fun getProductList() : MutableList<Product> {
        return mutableListOf(
            Product("Abacaxi", "Fruta", 7.54, "https://superprix.vteximg.com.br/arquivos/ids/175201-292-292/Abacaxi--unidade-.png?v=636294199507870000"),
            Product("Abacaxi", "Fruta", 7.54)
        )
    }

}