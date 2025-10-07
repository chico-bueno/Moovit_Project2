package com.example.moovit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface MoovitDAO {
    @Insert
    suspend fun inserir(linhaTransporte: LinhaTransporteBanco)

    @Query("SELECT * FROM linhaTransporte")
    suspend fun buscarTodos() : List<LinhaTransporteBanco>

    @Delete
    suspend fun deletar(linhaTransporte: LinhaTransporteBanco)

    @Update
    suspend fun atualizar(linhaTransporte: LinhaTransporteBanco)

    @Insert
    suspend fun inserir()


}