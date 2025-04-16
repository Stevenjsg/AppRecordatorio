package com.example.myapplication.data

import android.content.Context
import com.example.myapplication.R


fun obtenerMapaTipos(context: Context): Map<String, Pair<String, String>> {
    return mapOf(
        "agua" to (context.getString(R.string.tipo_agua) to "🥤"),
        "ayuno" to (context.getString(R.string.tipo_ayuno) to "⏳"),
        "suplemento" to (context.getString(R.string.tipo_suplemento) to "💊"),
        "otro" to (context.getString(R.string.tipo_otro) to "🔔")
    )
}
