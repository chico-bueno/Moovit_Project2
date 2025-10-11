package com.example.moovit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
@Dao
interface FavoritosDAO {
    @Insert
    suspend fun inserir(favorito: FavoritosBanco)
    @Query("SELECT * FROM favoritos ORDER BY nomeEstacao ASC")
    fun buscarTodos(): Flow<List<FavoritosBanco>>
    @Delete
    suspend fun deletar(favorito: FavoritosBanco)
    @Update
    suspend fun atualizar(favorito: FavoritosBanco)
    @Query("SELECT * FROM favoritos WHERE nomeEstacao = :estacao AND numeroLinha = :numero LIMIT 1")
    suspend fun buscarPorEstacaoELinha(estacao: String, numero: String): FavoritosBanco?
    @Query("DELETE FROM favoritos WHERE nomeEstacao = :estacao AND numeroLinha = :numero")
    suspend fun deletarPorEstacaoELinha(estacao: String, numero: String)
}
