package com.example.myapplication.repository

import com.example.myapplication.data.room.Recordatorio
import com.example.myapplication.data.room.RecordatorioDao

class RecordatorioRepository(private val dao: RecordatorioDao) {

    suspend fun getAll() = dao.getAll()

    suspend fun getById(id: Int) = dao.getById(id)

    suspend fun insert(recordatorio: Recordatorio) = dao.insert(recordatorio)

    suspend fun update(recordatorio: Recordatorio) = dao.update(recordatorio)

    suspend fun delete(recordatorio: Recordatorio) = dao.delete(recordatorio)
}