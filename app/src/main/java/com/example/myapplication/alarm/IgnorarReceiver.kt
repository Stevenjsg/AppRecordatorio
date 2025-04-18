package com.example.myapplication.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import android.util.Log
import com.example.myapplication.utils.cancelarNotificacionProgramada

class IgnorarReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val id = intent?.getIntExtra("recordatorioId", -1) ?: return
        if (id == -1) return

        cancelarNotificacionProgramada(context, id)

        Toast.makeText(context, "⏸️ Recordatorio ignorado", Toast.LENGTH_SHORT).show()
        Log.d("IgnorarReceiver", "Recordatorio $id ignorado y cancelado")
    }
}
