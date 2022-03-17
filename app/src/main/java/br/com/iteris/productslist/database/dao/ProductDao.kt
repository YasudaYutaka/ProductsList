package br.com.iteris.productslist.database.dao

import androidx.room.*
import br.com.iteris.productslist.model.Product

@Dao
interface ProductDao {

    @Query("SELECT * FROM Product")
    suspend fun searchAll() : MutableList<Product>

    @Insert
    suspend fun saveProduct(product : Product) : Long

    @Delete
    suspend fun deleteProduct(vararg product : Product)

    @Update
    suspend fun updateProduct(vararg product: Product)

}