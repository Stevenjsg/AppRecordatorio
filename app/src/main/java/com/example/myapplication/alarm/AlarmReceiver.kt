package com.example.myapplication.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.myapplication.utils.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val id = intent?.getIntExtra("recordatorioId", -1) ?: return
        val titulo = intent.getStringExtra("titulo") ?: "Recordatorio"
        val mensaje = intent.getStringExtra("mensaje") ?: "¡Es hora de tu actividad!"
        Log.d("AlarmReceiver", "Mostrando notificación con ID: $id")
        NotificationHelper.mostrarNotificacion(context, titulo, mensaje,id)
    }
}