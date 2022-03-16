package br.com.iteris.productslist.database.dao

import androidx.room.*
import br.com.iteris.productslist.model.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    fun searchAll() : MutableList<Product>

    @Insert
    fun saveProduct(product : Product) : Long

    @Delete
    fun deleteProduct(vararg product : Product)

    @Update
    fun updateProduct(vararg product: Product)

}