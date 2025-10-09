package com.example.moovit

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HorariosViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val dao = db.horariosDao()

    private val _horarios = MutableStateFlow<List<HorarioBanco>>(emptyList())
    val horarios: StateFlow<List<HorarioBanco>> = _horarios

    init {
        buscarTodos()
    }

    fun buscarTodos() {
        viewModelScope.launch {
            _horarios.value = dao.buscarTodos()
        }
    }

    fun inserir(horario: HorarioBanco, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            dao.inserir(horario)
            buscarTodos()
            onComplete?.invoke()
        }
    }

    fun atualizar(horario: HorarioBanco, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            dao.atualizar(horario)
            buscarTodos()
            onComplete?.invoke()
        }
    }

    fun deletar(horario: HorarioBanco, onComplete: (() -> Unit)? = null) {
        viewModelScope.launch {
            dao.deletar(horario)
            buscarTodos()
            onComplete?.invoke()
        }
    }
}
