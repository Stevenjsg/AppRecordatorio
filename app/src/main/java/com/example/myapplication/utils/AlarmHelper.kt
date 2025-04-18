package com.example.myapplication.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import com.example.myapplication.alarm.AlarmReceiver
import androidx.core.net.toUri

fun programarNotificacion(
    context: Context,
    triggerTimeMillis: Long,
    titulo: String,
    mensaje: String,
    requestCode: Int ,
) {
    val intent = Intent(context, AlarmReceiver::class.java).apply {
        putExtra("recordatorioId", requestCode) // 👈 ESTE ES EL CLAVE
        putExtra("titulo", titulo)
        putExtra("mensaje", mensaje)
    }


    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    try {
        // Solo para Android 12+ debemos verificar el permiso
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                val intentAjustes = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = "package:${context.packageName}".toUri()
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intentAjustes)
                return
            }
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTimeMillis,
            pendingIntent
        )

    } catch (e: SecurityException) {
        e.printStackTrace()
        Toast.makeText(
            context,
            "No se pudo programar la alarma. Verifica los permisos de notificación.",
            Toast.LENGTH_LONG
        ).show()
    }
}

fun cancelarNotificacionProgramada(context: Context, requestCode: Int) {
    val intent = Intent(context, AlarmReceiver::class.java)

    val pendingIntent = PendingIntent.getBroadcast(
        context,
        requestCode,
        intent,
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )

    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    alarmManager.cancel(pendingIntent)
}
