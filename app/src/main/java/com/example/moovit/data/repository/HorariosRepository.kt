package com.example.moovit.data.repository

import androidx.compose.ui.graphics.Color
import com.example.moovit.ui.horarios.RotaHorario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf


class HorariosRepository {

    private val rotasEstaticas = listOf(
        RotaHorario(1, "21 min", "10:42", "Uber", "Táxi/Uber", "Saída em 2 min", "Chegada às 10:42", Color.Black),
        RotaHorario(2, "28 min", "10:50", "R$ 6,00", "380 Detran", "Praça Rui Barbosa", "Terminal Centenário"),
        RotaHorario(3, "20 min", "10:50", "R$ 6,00", "366 Itupã", "Praça General Osório", "Chegada 10:50"),
        RotaHorario(4, "24 min", "10:45", "R$ 6,00", "311 Interbairros", "Praça Rui Barbosa", "Chegada 10:45")
    )

    fun getRotas(): Flow<List<RotaHorario>> {
        return flowOf(rotasEstaticas)
    }
}