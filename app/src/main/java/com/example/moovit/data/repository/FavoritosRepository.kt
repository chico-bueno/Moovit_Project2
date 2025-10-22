package com.example.moovit.data.repository

import com.example.moovit.data.local.FavoritosBanco
import com.example.moovit.data.local.FavoritosDAO
import kotlinx.coroutines.flow.Flow

class FavoritosRepository(private val favoritosDAO: FavoritosDAO) {

    // A função que estava faltando ou incorreta.
    // Ela busca os dados do DAO e os expõe como um Flow.
    fun getFavoritos(): Flow<List<FavoritosBanco>> = favoritosDAO.buscarTodos()

    suspend fun inserir(favorito: FavoritosBanco) {
        favoritosDAO.inserir(favorito)
    }

    suspend fun deletar(favorito: FavoritosBanco) {
        favoritosDAO.deletar(favorito)
    }

    suspend fun deletarPorEstacaoELinha(estacao: String, numero: String) {
        favoritosDAO.deletarPorEstacaoELinha(estacao, numero)
    }

    suspend fun atualizar(favorito: FavoritosBanco) {
        favoritosDAO.atualizar(favorito)
    }

    suspend fun buscarPorEstacaoELinha(estacao: String, numero: String): FavoritosBanco? {
        return favoritosDAO.buscarPorEstacaoELinha(estacao, numero)
    }
}