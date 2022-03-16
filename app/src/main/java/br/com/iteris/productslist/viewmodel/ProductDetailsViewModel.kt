package br.com.iteris.productslist.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.com.iteris.productslist.R

class ProductDetailsViewModel : ViewModel() {

    private val mutableEditState = MutableLiveData<Boolean>()
    val editState : LiveData<Boolean> get() = mutableEditState
    private val mutableRemoveState = MutableLiveData<Boolean>()
    val removeState : LiveData<Boolean> get() = mutableRemoveState

    // Recebe o id do menu clicado e postValue no state de uma determinada opção
    fun onSelectMenuItem(id : Int) {
        when(id) {
            R.id.menu_product_details_edit -> {
                mutableEditState.postValue(true)
            }
            R.id.menu_product_details_remove -> {
                mutableRemoveState.postValue(true)
            }
        }
    }

}