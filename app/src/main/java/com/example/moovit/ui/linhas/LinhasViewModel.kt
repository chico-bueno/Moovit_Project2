package com.example.moovit.ui.linhas

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moovit.data.local.AppDatabase
import com.example.moovit.data.local.LinhaTransporteBanco
import com.example.moovit.data.repository.LinhasRepository // <-- Garanta que este import está correto
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// (As data classes LinhasUiState continuam as mesmas)
data class LinhasUiState(
    val todasAsLinhas: List<LinhaTransporteBanco> = emptyList(),
    val textoPesquisa: String = ""
) {
    val linhasFiltradas: List<LinhaTransporteBanco>
        get() = if (textoPesquisa.isBlank()) {
            todasAsLinhas
        } else {
            todasAsLinhas.filter {
                it.nome.contains(textoPesquisa, ignoreCase = true) ||
                        it.numero.contains(textoPesquisa, ignoreCase = true) ||
                        it.tipo.contains(textoPesquisa, ignoreCase = true)
            }
        }
}


class LinhasViewModel(
    private val application: Application,
    private val repository: LinhasRepository // Agora ele encontra esta classe
) : ViewModel() {

    private val _uiState = MutableStateFlow(LinhasUiState())
    val uiState: StateFlow<LinhasUiState> = _uiState.asStateFlow()

    init {
        // Coleta o fluxo de linhas do repositório
        viewModelScope.launch {
            repository.getAllLinhas().collect { linhas -> // <-- Agora encontra este método
                _uiState.update { it.copy(todasAsLinhas = linhas) }
            }
        }
    }

    fun onTextoPesquisaChanged(novoTexto: String) {
        _uiState.update { it.copy(textoPesquisa = novoTexto) }
    }

    fun adicionarLinha(nome: String, numero: String, tipo: String, cor: String) {
        viewModelScope.launch {
            val novaLinha = LinhaTransporteBanco(nome = nome, numero = numero, tipo = tipo, cor = cor)
            repository.inserir(novaLinha) // <-- Agora encontra este método
        }
    }

    fun atualizarLinha(linha: LinhaTransporteBanco) {
        viewModelScope.launch {
            repository.atualizar(linha) // <-- Agora encontra este método
        }
    }

    fun removerLinha(linha: LinhaTransporteBanco) {
        viewModelScope.launch {
            repository.deletar(linha) // <-- Agora encontra este método
        }
    }

    // Factory para criar o ViewModel com suas dependências
    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                val database = AppDatabase.getDatabase(application)
                val repository = LinhasRepository(database.moovitDao()) // <-- E agora encontra aqui
                LinhasViewModel(application, repository)
            }
        }
    }
}