package br.com.iteris.productslist.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.iteris.productslist.database.dao.ProductDao
import br.com.iteris.productslist.model.Product

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao() : ProductDao

    companion object { // Com isso, conseguimos instanciar

        @Volatile private var db : AppDatabase? = null

        // Função que da acesso a instancia do database
        fun instanceDatabase(context: Context) : AppDatabase {
            return db ?: Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "productslist.db"
            ).build()
                .also {
                    db = it
                }
        }
    }

}