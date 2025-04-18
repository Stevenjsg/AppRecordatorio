package com.example.myapplication.utils


import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.net.toUri

fun verificarPermisoAlarmasExactas(context: Context): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val tienePermiso = alarmManager.canScheduleExactAlarms()
        if (!tienePermiso) {
            // Abre ajustes para que el usuario otorgue el permiso
            val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                data = "package:${context.packageName}".toUri()
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
        return tienePermiso
    }
    return true // No es necesario en versiones menores a Android 12
}
