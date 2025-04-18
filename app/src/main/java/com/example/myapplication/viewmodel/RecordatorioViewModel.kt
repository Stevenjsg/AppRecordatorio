package com.example.myapplication.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.room.AppDatabase
import com.example.myapplication.data.room.Recordatorio
import com.example.myapplication.repository.RecordatorioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecordatorioViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getDatabase(application).recordatorioDao()
    private val repository = RecordatorioRepository(dao)

    // ✅ Exposición directa de los recordatorios como StateFlow
    val recordatorios: StateFlow<List<Recordatorio>> =
        repository.recordatorios.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun guardar(recordatorio: Recordatorio, onIdGenerado: (Int) -> Unit = {}) {
        viewModelScope.launch {
            val idGenerado = repository.insert(recordatorio)
            onIdGenerado(idGenerado.toInt())
        }
    }

    fun update(recordatorio: Recordatorio) {
        viewModelScope.launch {
            repository.update(recordatorio)
        }
    }

    fun eliminar(recordatorios: List<Recordatorio>) {
        viewModelScope.launch {
            repository.eliminar(recordatorios)
        }
    }

    fun obtenerPorId(id: Int, onSuccess: (Recordatorio?) -> Unit) {
        viewModelScope.launch {
            val resultado = repository.getById(id)
            onSuccess(resultado)
        }
    }
}
