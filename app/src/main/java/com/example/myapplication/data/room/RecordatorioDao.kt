package com.example.myapplication.data.room


import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordatorioDao  {
    @Query("SELECT * FROM recordatorio WHERE id = :id")
    suspend fun getById(id: Int): Recordatorio?

    @Query("SELECT * FROM recordatorio")
     fun getAll(): Flow<List<Recordatorio>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recordatorio: Recordatorio): Long


    @Update
    suspend fun update(recordatorio: Recordatorio)

    @Delete
    suspend fun delete(recordatorio: Recordatorio)

    @Delete
    suspend fun deleteAll(recordatorios: List<Recordatorio>)

}
