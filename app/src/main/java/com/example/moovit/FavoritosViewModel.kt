package com.example.moovit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoritosViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.moovitDAO()

    private val _favoritos = MutableStateFlow<List<LinhaTransporteBanco>>(emptyList())
    val favoritos: StateFlow<List<LinhaTransporteBanco>> = _favoritos

    init {
        buscarTodos()
        // Se não houver favoritos, inserir alguns padrões para a primeira experiência
        viewModelScope.launch {
            val atuais = dao.buscarTodos()
            if (atuais.isEmpty()) {
                val padroes = listOf(
                    LinhaTransporteBanco(nome = "561 GUILHERMINA", numero = 561, tipo = "Ônibus", cor = "#FF5722"),
                    LinhaTransporteBanco(nome = "665 VILA REX", numero = 665, tipo = "Ônibus", cor = "#FF5722"),
                    LinhaTransporteBanco(nome = "360 NOVENA", numero = 360, tipo = "Ônibus", cor = "#FF5722")
                )
                padroes.forEach { dao.inserir(it) }
                buscarTodos()
            }
        }
    }

    fun buscarTodos() {
        viewModelScope.launch {
            _favoritos.value = dao.buscarTodos()
        }
    }

    fun inserir(linha: LinhaTransporteBanco, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            dao.inserir(linha)
            buscarTodos()
            onComplete?.invoke()
        }
    }

    fun atualizar(linha: LinhaTransporteBanco, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            dao.atualizar(linha)
            buscarTodos()
            onComplete?.invoke()
        }
    }

    fun deletar(linha: LinhaTransporteBanco, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            dao.deletar(linha)
            buscarTodos()
            onComplete?.invoke()
        }
    }
}