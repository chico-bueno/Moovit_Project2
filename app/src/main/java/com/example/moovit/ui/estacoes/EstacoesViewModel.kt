package com.example.moovit.ui.estacoes

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moovit.data.local.AppDatabase
import com.example.moovit.data.local.FavoritosBanco
import com.example.moovit.data.repository.FavoritosRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EstacoesViewModel(
    application: Application,
    private val repository: FavoritosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EstacoesUiState())
    val uiState: StateFlow<EstacoesUiState> = _uiState.asStateFlow()

    init {
        carregarEstacoes()
        viewModelScope.launch {
            repository.getFavoritos().collect { listaDeFavoritos ->
                _uiState.update { it.copy(favoritos = listaDeFavoritos) }
            }
        }
    }

    private fun carregarEstacoes() {
        val estacoes = listOf(
            EstacaoCompleta(
                "Praça Rui Barbosa", "1 min a pé", "1 min",
                listOf(
                    LinhaOnibus("561", "GUILHERMINA", "2 min", "12:07"),
                    LinhaOnibus("665", "VILA REX", "2 min", "12:16"),
                    LinhaOnibus("663", "VILA CUBAS", "2 min", "19 min"),
                    LinhaOnibus("603", "PINHEIRINHO / RUI BARBOSA", "2 min", "19 min")
                )
            ),
            EstacaoCompleta(
                "Praça Rui Barbosa (360)", "2 min a pé", "2 min",
                listOf(
                    LinhaOnibus("360", "NOVENA", "3 min", "12:25")
                )
            ),
            EstacaoCompleta(
                "Terminal Guadalupe", "5 min a pé", "5 min",
                listOf(
                    LinhaOnibus("380", "DETRAN", "8 min", "12:30"),
                    LinhaOnibus("140", "VILA ESPERANÇA", "12 min", "12:35")
                )
            )
        )
        _uiState.update { it.copy(todasAsEstacoes = estacoes) }
    }

    fun onTextoPesquisaChanged(novoTexto: String) {
        _uiState.update { it.copy(textoPesquisa = novoTexto) }
    }

    fun onAbaSelecionada(novaAba: String) {
        _uiState.update { it.copy(abaSelecionada = novaAba) }
    }

    fun adicionarFavorito(nomeEstacao: String, numeroLinha: String, nomeLinha: String) {
        viewModelScope.launch {
            val novoFavorito = FavoritosBanco(
                nomeEstacao = nomeEstacao,
                numeroLinha = numeroLinha,
                nomeLinha = nomeLinha
            )
            repository.inserir(novoFavorito)
        }
    }

    fun removerFavorito(favorito: FavoritosBanco) {
        viewModelScope.launch {
            repository.deletar(favorito)
        }
    }

    fun removerFavoritoPorLinhaEEstacao(estacao: String, numero: String) {
        viewModelScope.launch {
            repository.deletarPorEstacaoELinha(estacao, numero)
        }
    }

    fun editarFavorito(favorito: FavoritosBanco) {
        viewModelScope.launch {
            repository.atualizar(favorito)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                val database = AppDatabase.getDatabase(application)
                val repository = FavoritosRepository(database.favoritosDao())
                EstacoesViewModel(application, repository)
            }
        }
    }
}