package com.example.myapplication.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.myapplication.utils.NotificationHelper

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val titulo = intent?.getStringExtra("titulo") ?: "Recordatorio"
        val mensaje = intent?.getStringExtra("mensaje") ?: "¡Es hora de tu actividad!"

        NotificationHelper.mostrarNotificacion(context, titulo, mensaje)
    }
}