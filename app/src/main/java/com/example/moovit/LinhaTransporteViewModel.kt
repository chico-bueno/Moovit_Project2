package com.example.moovit

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LinhaTransporteViewModel(application: Application) : AndroidViewModel(application) {

    private val moovitDAO: MoovitDAO by lazy {
        AppDatabase.getDatabase(application).moovitDao()
    }

    private val _listaLinhas = MutableStateFlow<List<LinhaTransporteBanco>>(emptyList())
    val listaLinhas: StateFlow<List<LinhaTransporteBanco>> = _listaLinhas

    init {

        carregarTodasLinhas()

        adicionarLinhasIniciais()
    }

    private fun adicionarLinhasIniciais() {
        viewModelScope.launch {
            if (moovitDAO.buscarTodos().isEmpty()) {
                Log.d("LinhaVM", "Adicionando dados iniciais...")
                inserirLinha("Centenario/Campo Comprido", "A01", "Bi-Articulado", "Vermelho", moovitDAO)
                inserirLinha("Interbairros I ", "011", "Interbairros", "Verde", moovitDAO)
                inserirLinha("Ligeirão Boqueirão", "500", "Ligeirão", "Vermelho", moovitDAO)
                carregarTodasLinhas()
            }
        }
    }


    fun carregarTodasLinhas() {
        viewModelScope.launch {
            _listaLinhas.value = buscarLinha(moovitDAO)
            Log.d("LinhaVM", "Linhas carregadas: ${_listaLinhas.value.size}")
        }
    }

    fun adicionarLinha(nome: String, numero: String, tipo: String, cor: String) {
        viewModelScope.launch {
            inserirLinha(nome, numero, tipo, cor, moovitDAO)
            carregarTodasLinhas()
        }
    }

    fun atualizarLinhaExistente(linha: LinhaTransporteBanco) {
        viewModelScope.launch {
            atualizarLinha(linha, moovitDAO)
            carregarTodasLinhas()
        }
    }

    fun removerLinha(linha: LinhaTransporteBanco) {
        viewModelScope.launch {
            deletarLinha(linha, moovitDAO)
            carregarTodasLinhas()
        }
    }
}