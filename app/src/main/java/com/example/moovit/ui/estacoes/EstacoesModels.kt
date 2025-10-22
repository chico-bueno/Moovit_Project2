package com.example.moovit.ui.estacoes

import androidx.compose.ui.graphics.Color
import com.example.moovit.data.local.FavoritosBanco

// MODELOS DE DADOS PARA A UI DA TELA DE ESTAÇÕES

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

// CLASSE QUE REPRESENTA TODO O ESTADO DA TELA
data class EstacoesUiState(
    val textoPesquisa: String = "",
    val abaSelecionada: String = "ao_redor",
    val todasAsEstacoes: List<EstacaoCompleta> = emptyList(),
    val favoritos: List<FavoritosBanco> = emptyList()
) {
    // Lógica para filtrar as estações com base na pesquisa
    val estacoesFiltradas: List<EstacaoCompleta>
        get() = todasAsEstacoes.filter {
            it.nome.contains(textoPesquisa, ignoreCase = true)
        }
}