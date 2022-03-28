package br.com.iteris.productslist.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.iteris.productslist.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM User WHERE id = :userId AND password = :password")
    suspend fun authenticateUser(userId : String, password : String) : User?

    @Insert
    suspend fun saveUser(user : User)

    @Query("SELECT * FROM User WHERE id = :userId")
    fun searchUserById(userId : String) : Flow<User>

}