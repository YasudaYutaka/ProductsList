package br.com.iteris.productslist.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.iteris.productslist.database.dao.ProductDao
import br.com.iteris.productslist.model.Product

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao() : ProductDao
}