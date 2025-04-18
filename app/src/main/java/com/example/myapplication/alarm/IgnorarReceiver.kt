package com.example.myapplication.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.util.Log

class IgnorarReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val id = intent?.getIntExtra("recordatorioId", -1) ?: return
        if (id == -1) return

        Toast.makeText(context, "⏸️ Recordatorio ignorado", Toast.LENGTH_SHORT).show()
        Log.d("IgnorarReceiver", "Recordatorio $id ignorado por el usuario")

        // Aquí podrías cancelar la notificación si es necesario
        // val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // manager.cancel(id)
    }
}
