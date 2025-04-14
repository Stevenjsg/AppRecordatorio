package com.example.myapplication.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Recordatorio")
data class Recordatorio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val tipo: String,
    val intervaloHoras: Int,
    val ultimaHora: String,
    val proximaHora: String,
    val activo: Boolean,
    val horasAyuno: Int? = null,
    val horasComida: Int? = null
)