package com.example.moovit

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favoritos")
data class FavoritosBanco(
    @PrimaryKey(autoGenerate = true)
    val idFav: Int = 0,

    val linhaFav: String,//fazer relacionamento com a tabela LinhaTransporte
    //para pegar todos os dados da linha pelo id
    val casaFav: String,
    val trabalhoFav: String,

    )