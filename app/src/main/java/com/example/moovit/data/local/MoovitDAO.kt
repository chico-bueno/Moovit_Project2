package com.example.moovit.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MoovitDAO {
    @Insert
    suspend fun inserir(linhaTransporte: LinhaTransporteBanco)

    // GARANTA QUE ESTA LINHA RETORNA UM Flow<List<LinhaTransporteBanco>>
    @Query("SELECT * FROM linhaTransporte ORDER BY nome ASC")
    fun buscarTodos(): Flow<List<LinhaTransporteBanco>>

    @Delete
    suspend fun deletar(linhaTransporte: LinhaTransporteBanco)

    @Update
    suspend fun atualizar(linhaTransporte: LinhaTransporteBanco)
}