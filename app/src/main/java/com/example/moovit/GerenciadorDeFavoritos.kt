package com.example.moovit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GerenciadorDeFavoritos(application: Application) : AndroidViewModel(application) {

    private val bancoDeDados = AppDatabase.getDatabase(application)
    private val dao = bancoDeDados.favoritosDao()

    private val _listaFavoritos = MutableStateFlow<List<FavoritosBanco>>(emptyList())
    val listaFavoritos: StateFlow<List<FavoritosBanco>> = _listaFavoritos

    init {
        carregarFavoritos()
    }

    private fun carregarFavoritos() {
        viewModelScope.launch {
            dao.buscarTodos().collect { favoritos ->
                _listaFavoritos.value = favoritos
            }
        }
    }
    fun adicionarFavorito(nomeEstacao: String, numeroLinha: String, nomeLinha: String) {
        viewModelScope.launch {
            val novoFavorito = FavoritosBanco(
                nomeEstacao = nomeEstacao,
                numeroLinha = numeroLinha,
                nomeLinha = nomeLinha
            )
            dao.inserir(novoFavorito)
        }
    }

    fun removerFavorito(favorito: FavoritosBanco) {
        viewModelScope.launch {
            dao.deletar(favorito)
        }
    }

    fun removerFavoritoPorLinhaEEstacao(estacao: String, numero: String) {
        viewModelScope.launch {
            dao.deletarPorEstacaoELinha(estacao, numero)
        }
    }
    fun editarFavorito(favorito: FavoritosBanco) {
        viewModelScope.launch {
            dao.atualizar(favorito)
        }
    }
    suspend fun verificarSeFavorito(estacao: String, numero: String): Boolean {
        return dao.buscarPorEstacaoELinha(estacao, numero) != null
    }

    fun buscarFavorito(estacao: String, numero: String): FavoritosBanco? {
        return _listaFavoritos.value.find {
            it.nomeEstacao == estacao && it.numeroLinha == numero
        }
    }
}
