package com.example.myapplication.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.myapplication.data.room.AppDatabase
import com.example.myapplication.screen.horaAFechaEnMillis
import com.example.myapplication.utils.programarNotificacion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class ConfirmarReceiver : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent?) {
        val id = intent?.getIntExtra("recordatorioId", -1) ?: return
        if (id == -1) return

        val db = AppDatabase.getDatabase(context)
        val dao = db.recordatorioDao()
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        CoroutineScope(Dispatchers.IO).launch {
            val recordatorio = dao.getById(id)
            if (recordatorio != null) {
                val nuevaUltimaHora = recordatorio.proximaHora

                // Calcular nueva proximaHora
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
                    Log.e("ConfirmarReceiver", "Error al parsear hora: ${e.message}")
                    "00:00"
                }

                val actualizado = recordatorio.copy(
                    ultimaHora = nuevaUltimaHora,
                    proximaHora = nuevaProximaHora
                )

                dao.update(actualizado)

                // Reprogramar nueva notificación
                val triggerTime = horaAFechaEnMillis(nuevaProximaHora)
                programarNotificacion(
                    context = context,
                    triggerTimeMillis = triggerTime,
                    titulo = "Recordatorio: ${recordatorio.titulo}",
                    mensaje = "¿Ya completaste tu ${recordatorio.tipo}?",
                    requestCode = id
                )

                Log.d("ConfirmarReceiver", "Recordatorio $id confirmado y reprogramado")
            } else {
                Log.w("ConfirmarReceiver", "Recordatorio $id no encontrado")
            }
        }

        // Opcional: feedback visual si la app está en foreground
        Toast.makeText(context, "✅ Acción confirmada", Toast.LENGTH_SHORT).show()
    }
}
