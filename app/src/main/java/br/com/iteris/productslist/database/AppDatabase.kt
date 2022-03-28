package br.com.iteris.productslist.database

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.iteris.productslist.database.dao.ProductDao
import br.com.iteris.productslist.database.dao.UserDao
import br.com.iteris.productslist.model.Product
import br.com.iteris.productslist.model.User

@Database(
    entities = [Product::class, User::class],
    version = 2,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    abstract fun userDao(): UserDao

    // com o koin n precisa dessa parte
    companion object { // Com isso, conseguimos instanciar

        @Volatile
        private var db: AppDatabase? = null

        // Função que da acesso a instancia do database
        fun instanceDatabase(context: Context): AppDatabase {
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