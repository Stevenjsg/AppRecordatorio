package com.example.myapplication.data


import androidx.room.*

@Dao
interface RecordatorioDao  {
    @Query("SELECT * FROM recordatorio WHERE id = :id")
    suspend fun getById(id: Int): Recordatorio?

    @Query("SELECT * FROM recordatorio")
    suspend fun getAll(): List<Recordatorio>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recordatorio: Recordatorio)

    @Update
    suspend fun update(recordatorio: Recordatorio)

    @Delete
    suspend fun delete(recordatorio: Recordatorio)
}
