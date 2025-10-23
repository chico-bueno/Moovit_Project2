package com.example.moovit.ui.horarios

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.Flow

data class RotaHorario(
    val id: Int,
    val tempo: String,
    val horaChegada: String,
    val preco: String,
    val linha: String,
    val saida: String,
    val chegada: String,
    val iconeCor: Color = Color(0xFFFF5722)
)

data class HorariosUiState(
    val todasAsRotas: List<RotaHorario> = emptyList(),
    val textoPesquisa: String = ""
) {
    val rotasFiltradas: List<RotaHorario>
        get() = if (textoPesquisa.isBlank()) {
            todasAsRotas
        } else {
            todasAsRotas.filter {
                it.linha.contains(textoPesquisa, ignoreCase = true) ||
                        it.chegada.contains(textoPesquisa, ignoreCase = true)
            }
        }
}