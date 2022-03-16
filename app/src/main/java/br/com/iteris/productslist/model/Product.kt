package br.com.iteris.productslist.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true) var id: Long = 0L, //precisa ser 0 para o Room entender q deve gerar um id
    var name : String,
    var description : String,
    var price : Double,
    var image : ByteArray? =  null
) : Serializable
