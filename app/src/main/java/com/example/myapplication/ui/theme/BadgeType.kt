package com.example.myapplication.ui.theme

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.obtenerMapaTipos
import com.example.myapplication.data.room.Recordatorio

@Composable
fun BadgeType(
    recordatorios: List<Recordatorio>,
    onFiltroSeleccionado: (String?) -> Unit,
    tipoSeleccionado: String?,
    context: Context = LocalContext.current
){
    val mapaTipos = remember(context) { obtenerMapaTipos(context) }
    val tiposUnicos = recordatorios.map { it.tipo }.distinct()
    val opciones = listOf<String?>(null) + tiposUnicos

    LazyRow (
        modifier = Modifier.padding(start = 8.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        items(opciones) { clave ->
            val (nombre, icono) = if (clave == null) {
                "Todos" to "🔄"
            } else {
                mapaTipos[clave] ?: return@items
            }

            val isSelected = clave == tipoSeleccionado
            val backgroundColor = if (isSelected) Color(0xFF1976D2) else Color(0, 0, 255, 50)

            Text(
                text = "$icono $nombre",
                modifier = Modifier
                    .border(1.dp, White, shape = CircleShape)
                    .background(backgroundColor, shape = CircleShape)
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .clickable {
                        onFiltroSeleccionado(clave)
                    },
                fontWeight = FontWeight.Bold,
                color = White
            )
        }


    }
}
