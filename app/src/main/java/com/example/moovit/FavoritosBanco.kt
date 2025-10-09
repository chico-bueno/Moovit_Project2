package com.example.moovit

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favoritos")
data class FavoritosBanco(
    @PrimaryKey(autoGenerate = true)
    val idFav: Int = 0,

    val linhaFav: String,
    val trabalhoFav: String,
    )