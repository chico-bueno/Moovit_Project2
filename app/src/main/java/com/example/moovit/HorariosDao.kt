package com.example.moovit

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface HorariosDAO {
    @Insert
    suspend fun inserir(horario: HorarioBanco)

    @Query("SELECT * FROM horarios ORDER BY hora")
    suspend fun buscarTodos(): List<HorarioBanco>

    @Delete
    suspend fun deletar(horario: HorarioBanco)

    @Update
    suspend fun atualizar(horario: HorarioBanco)
}
