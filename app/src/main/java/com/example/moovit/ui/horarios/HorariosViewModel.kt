package com.example.moovit.ui.horarios

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moovit.data.repository.HorariosRepository // Importa o novo repositório
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HorariosViewModel(
    private val repository: HorariosRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HorariosUiState())
    val uiState: StateFlow<HorariosUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getRotas().collect { rotas ->
                _uiState.update { it.copy(todasAsRotas = rotas) }
            }
        }
    }

    fun onTextoPesquisaChanged(novoTexto: String) {
        _uiState.update { it.copy(textoPesquisa = novoTexto) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = this[APPLICATION_KEY] as Application
                val repository = HorariosRepository()
                HorariosViewModel(repository)
            }
        }
    }
}