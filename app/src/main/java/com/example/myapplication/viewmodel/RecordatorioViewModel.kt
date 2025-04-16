package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.room.AppDatabase
import com.example.myapplication.data.room.Recordatorio
import com.example.myapplication.repository.RecordatorioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecordatorioViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.Companion.getDatabase(application).recordatorioDao()
    private val repository = RecordatorioRepository(dao)

    private val _recordatorios = MutableStateFlow<List<Recordatorio>>(emptyList())
    val recordatorios: StateFlow<List<Recordatorio>> = _recordatorios

    fun cargarRecordatorios() {
        viewModelScope.launch {
            _recordatorios.value = repository.getAll()
        }
    }

    fun guardar(recordatorio: Recordatorio) {
        viewModelScope.launch {
            repository.insert(recordatorio)
            cargarRecordatorios() // refrescar
        }
    }
    fun update(recordatorio: Recordatorio) {
        viewModelScope.launch {
            repository.update(recordatorio)
            cargarRecordatorios() // refrescar
        }
    }
    fun delete(recordatorio: Recordatorio) {
        viewModelScope.launch {
            repository.delete(recordatorio)
            cargarRecordatorios() // refrescar

        }
    }
    fun obtenerPorId(id: Int, onSuccess: (Recordatorio?) -> Unit) {
            viewModelScope.launch {
                val resultado = repository.getById(id)
                onSuccess(resultado)
            }
        }
    }