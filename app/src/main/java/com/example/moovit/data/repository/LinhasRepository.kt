package com.example.moovit.data.repository

import com.example.moovit.data.local.MoovitDAO
import com.example.moovit.data.local.LinhaTransporteBanco
import kotlinx.coroutines.flow.Flow

class LinhasRepository(private val moovitDAO: MoovitDAO) {

    fun getAllLinhas(): Flow<List<LinhaTransporteBanco>> = moovitDAO.buscarTodos()

    suspend fun inserir(linha: LinhaTransporteBanco) {
        moovitDAO.inserir(linha)
    }

    suspend fun deletar(linha: LinhaTransporteBanco) {
        moovitDAO.deletar(linha)
    }

    suspend fun atualizar(linha: LinhaTransporteBanco) {
        moovitDAO.atualizar(linha)
    }
}