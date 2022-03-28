package br.com.iteris.productslist.application

import android.app.Application
import androidx.room.Room
import br.com.iteris.productslist.database.AppDatabase
import br.com.iteris.productslist.database.MIGRATION_1_2
import br.com.iteris.productslist.model.repository.ProductsRepository
import br.com.iteris.productslist.viewmodel.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val myModules = module {
            factory { ProductsRepository() }
            viewModel { ProductsViewModel() }
            viewModel { AddProductViewModel() }
            viewModel { ProductDetailsViewModel() }
            viewModel { LoginViewModel() }
            viewModel { SignUpViewModel() }
            single<AppDatabase> { Room.databaseBuilder(
                get(),
                AppDatabase::class.java,
                "productslist.db"
            ).addMigrations(MIGRATION_1_2).build() }
        }

        startKoin {
            androidContext(this@AppApplication)
            modules(myModules)
        }

    }

}