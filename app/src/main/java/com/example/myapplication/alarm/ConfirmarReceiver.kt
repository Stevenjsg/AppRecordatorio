package com.example.myapplication.alarm

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.myapplication.data.room.AppDatabase
import com.example.myapplication.screen.horaAFechaEnMillis
import com.example.myapplication.utils.programarNotificacion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ConfirmarReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val id = intent?.getIntExtra("recordatorioId", -1) ?: return
        if (id == -1) return
        Log.d("ConfirmarReceiver", "Recordatorio ID recibido: $id")
        Toast.makeText(context, "Recordatorio $id confirmado", Toast.LENGTH_SHORT).show()

        val db = AppDatabase.getDatabase(context)
        val dao = db.recordatorioDao()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        CoroutineScope(Dispatchers.IO).launch {
            val recordatorio = dao.getById(id)
            Log.d("ConfirmarReceiver", "Recordatorio recuperado: ${recordatorio?.titulo}")
            val verificado = dao.getById(id)
            Log.d("ConfirmarReceiver", "Verificado en BD: ${verificado?.ultimaHora} -> ${verificado?.proximaHora}")

            if (recordatorio != null) {
                val nuevaUltimaHora = recordatorio.proximaHora

                val nuevaProximaHora = try {
                    val hora = LocalTime.parse(nuevaUltimaHora, formatter)
                    val siguiente = hora.plusHours(
                        if (recordatorio.tipo == "ayuno")
                            recordatorio.horasAyuno?.toLong() ?: 0
                        else
                            recordatorio.intervaloHoras.toLong()
                    )
                    siguiente.format(formatter)
                } catch (e: Exception) {
                    Log.e("ConfirmarReceiver", "Error en hora: ${e.message}")
                    "00:00"
                }

                val actualizado = recordatorio.copy(
                    ultimaHora = nuevaUltimaHora,
                    proximaHora = nuevaProximaHora
                )

                dao.update(actualizado)
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancel(id) // ✅ Cierra la notificación actual

                val triggerTime = horaAFechaEnMillis(nuevaProximaHora)
                programarNotificacion(
                    context,
                    triggerTime,
                    titulo = "Recordatorio: ${recordatorio.titulo}",
                    mensaje = "¿Ya completaste tu ${recordatorio.tipo}?",
                    requestCode = id
                )

                Log.d("ConfirmarReceiver", "✅ Recordatorio $id actualizado y reprogramado")
            }
        }
    }
}
