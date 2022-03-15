package br.com.iteris.productslist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.iteris.productslist.model.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun searchAll() : List<Product>

    @Insert
    fun saveProduct(vararg product : Product) // n precisa varargs

}