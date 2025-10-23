package com.example.moovit.ui.estacoes

import androidx.compose.ui.graphics.Color
import com.example.moovit.data.local.FavoritosBanco


data class LinhaOnibus(
    val numero: String,
    val nome: String,
    val tempoChegada: String,
    val proximoHorario: String,
    val cor: Color = Color(0xFFFF5722)
)

data class EstacaoCompleta(
    val nome: String,
    val distancia: String,
    val tempoAPe: String,
    val linhas: List<LinhaOnibus>
)

data class EstacoesUiState(
    val textoPesquisa: String = "",
    val abaSelecionada: String = "ao_redor",
    val todasAsEstacoes: List<EstacaoCompleta> = emptyList(),
    val favoritos: List<FavoritosBanco> = emptyList()
) {
    val estacoesFiltradas: List<EstacaoCompleta>
        get() = todasAsEstacoes.filter {
            it.nome.contains(textoPesquisa, ignoreCase = true)
        }
}