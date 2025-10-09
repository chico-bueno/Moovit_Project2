package com.example.moovit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel(application: Application) : AndroidViewModel(application) {
    private val banco = AppDatabase.getDatabase(application)
    private val daoFavoritos = banco.moovitDAO()

    private val _listaFavoritos = MutableStateFlow<List<LinhaTransporteBanco>>(emptyList())
    val listaFavoritos: StateFlow<List<LinhaTransporteBanco>> = _listaFavoritos

    init {
        carregarFavoritos()
        viewModelScope.launch {
            val favoritosAtuais = daoFavoritos.buscarTodos()
            if (favoritosAtuais.isEmpty()) {
                val exemplos = listOf(
                    LinhaTransporteBanco(nome = "561 GUILHERMINA", numero = 561, tipo = "Ônibus", cor = "#FF5722"),
                    LinhaTransporteBanco(nome = "665 VILA REX", numero = 665, tipo = "Ônibus", cor = "#FF5722"),
                    LinhaTransporteBanco(nome = "360 NOVENA", numero = 360, tipo = "Ônibus", cor = "#FF5722")
                )
                exemplos.forEach { daoFavoritos.inserir(it) }
                carregarFavoritos()
            }
        }
    }

    fun carregarFavoritos() {
        viewModelScope.launch {
            _listaFavoritos.value = daoFavoritos.buscarTodos()
        }
    }

    fun adicionarFavorito(linha: LinhaTransporteBanco, aoFinalizar: (() -> Unit)? = null) {
        viewModelScope.launch {
            daoFavoritos.inserir(linha)
            carregarFavoritos()
            aoFinalizar?.invoke()
        }
    }

    fun editarFavorito(linha: LinhaTransporteBanco, aoFinalizar: (() -> Unit)? = null) {
        viewModelScope.launch {
            daoFavoritos.atualizar(linha)
            carregarFavoritos()
            aoFinalizar?.invoke()
        }
    }

    fun removerFavorito(linha: LinhaTransporteBanco, aoFinalizar: (() -> Unit)? = null) {
        viewModelScope.launch {
            daoFavoritos.deletar(linha)
            carregarFavoritos()
            aoFinalizar?.invoke()
        }
    }
}