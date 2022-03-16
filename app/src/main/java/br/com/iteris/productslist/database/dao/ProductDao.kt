package br.com.iteris.productslist.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import br.com.iteris.productslist.model.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun searchAll() : MutableList<Product>

    @Insert
    fun saveProduct(vararg product : Product) // n precisa varargs

    @Delete
    fun deleteProduct(vararg product : Product)

}