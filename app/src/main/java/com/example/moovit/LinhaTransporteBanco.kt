package com.example.moovit

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "linhaTransporte")
data class LinhaTransporteBanco(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val nome: String,
    val numero: Int,
    val tipo: String,
    val cor: String,
)