package br.com.iteris.productslist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.iteris.productslist.model.Product

class ProductsViewModel : ViewModel() {

    private val mutableProductsList = MutableLiveData<MutableList<Product>>()
    val productsList : LiveData<MutableList<Product>> get() = mutableProductsList

    val productsSize : Int get() = productsList.value?.size ?: 0

    fun getProductByPosition(position : Int) : Product {
        return productsList.value!![position]
    }

    // Atualiza lista com novos produtos
    fun updateProductsList(list : MutableList<Product>) {
        mutableProductsList.postValue(list)
    }

    // Pega antiga lista e adiciona novo produto a ela
    fun addProductToList(product : Product) : Int {
        val newList = mutableProductsList.value
        newList?.add(product)
        mutableProductsList.postValue(newList)
        return newList!!.size
    }

}