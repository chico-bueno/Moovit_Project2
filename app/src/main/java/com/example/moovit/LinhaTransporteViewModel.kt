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
                inserirLinha("Expresso", "A01", "Ônibus", "#FF0000", moovitDAO)
                inserirLinha("Semi-Direto", "B02", "Ônibus", "#0000FF", moovitDAO)
                inserirLinha("Ligeirão", "C03", "Ônibus", "#00FF00", moovitDAO)
                carregarTodasLinhas() // Recarrega para exibir os novos dados
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