package com.example.moovit

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "horarios")
data class HorarioBanco(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val linha: String,
    val hora: String,
    val destino: String,
    val observacao: String? = null
)
