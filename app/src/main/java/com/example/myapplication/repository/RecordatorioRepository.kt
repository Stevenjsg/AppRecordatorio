package com.example.myapplication.repository

import com.example.myapplication.data.room.Recordatorio
import com.example.myapplication.data.room.RecordatorioDao
import kotlinx.coroutines.flow.Flow

class RecordatorioRepository(private val dao: RecordatorioDao) {

     val recordatorios: Flow<List<Recordatorio>> = dao.getAll()


    suspend fun getById(id: Int) = dao.getById(id)

    suspend fun insert(recordatorio: Recordatorio): Long {
        return dao.insert(recordatorio)
    }


    suspend fun update(recordatorio: Recordatorio) = dao.update(recordatorio)

    suspend fun eliminar(recordatorios: List<Recordatorio>) {
        dao.deleteAll(recordatorios)
    }

}