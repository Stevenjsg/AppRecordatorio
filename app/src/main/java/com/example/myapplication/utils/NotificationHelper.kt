package com.example.myapplication.utils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.R
import com.example.myapplication.alarm.ConfirmarReceiver
import com.example.myapplication.alarm.IgnorarReceiver

object NotificationHelper {

    fun mostrarNotificacion(context: Context, titulo: String, mensaje: String, id: Int ) {
        Log.d("NotificationHelper", "Mostrando notificación con ID: $id")
        // ✅ Verificación del permiso en Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val tienePermiso = ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED

            if (!tienePermiso) {
                return
            }
        }

        // ✅ Intent para el botón "Hecho"
        val intentConfirmar = Intent(context, ConfirmarReceiver::class.java).apply {
            putExtra("recordatorioId", id)
        }

        val pendingIntentConfirmar = PendingIntent.getBroadcast(
            context,
            id, // único por recordatorio
            intentConfirmar,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Intent para la acción "Ignorar"
        val intentIgnorar = Intent(context, IgnorarReceiver::class.java).apply {
            putExtra("recordatorioId", id)
        }

        val pendingIntentIgnorar = PendingIntent.getBroadcast(
            context,
            id + 1000, // Asegúrate de que sea un requestCode diferente
            intentIgnorar,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // ✅ Construcción de la notificación
        val builder = NotificationCompat.Builder(context, "recordatorio_channel_id")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Puedes usar un icono propio
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .addAction(
                R.drawable.ic_launcher_foreground, // Puedes usar otro icono aquí también
                context.getString(R.string.hecho), // O simplemente "Hecho"
                pendingIntentConfirmar
            )
            .addAction(
                R.drawable.ic_launcher_foreground,
                context.getString(R.string.ignorar),
                pendingIntentIgnorar
            )


        with(NotificationManagerCompat.from(context)) {
            notify(id, builder.build())
        }
    }
}
