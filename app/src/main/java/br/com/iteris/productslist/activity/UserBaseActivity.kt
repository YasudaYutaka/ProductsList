package br.com.iteris.productslist.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.lifecycleScope
import br.com.iteris.productslist.database.AppDatabase
import br.com.iteris.productslist.database.dao.UserDao
import br.com.iteris.productslist.model.User
import br.com.iteris.productslist.preferences.dataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

abstract class UserBaseActivity : AppCompatActivity() {

    private val db : AppDatabase by inject()
    private val userDao : UserDao by lazy { db.userDao() }

    private var _user : MutableStateFlow<User?> = MutableStateFlow(null)
    protected var user : StateFlow<User?> = _user

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verifyLoggedUser()
    }

    private fun verifyLoggedUser() {
        lifecycleScope.launch {
            // Flow - Pega id do user logado, caso n tenha, volta para login
            dataStore.data.collect { pref ->
                pref[stringPreferencesKey("loggedUser")]?.let { userId ->
                    searchUser(userId)
                } ?: returnToLogin() // Caso n esteja logado, vai para tela de login
            }
        }
    }

    private fun searchUser(userId: String) {
        lifecycleScope.launch {
            _user.value = userDao.searchUserById(userId).firstOrNull()
        }
    }

    protected suspend fun logout() {
        dataStore.edit { pref ->
            pref.remove(stringPreferencesKey("loggedUser"))
        }
    }

    // Função que retorna para LoginActivity
    private fun returnToLogin() {
        val intent = Intent(this, LoginActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        finish()
    }

}