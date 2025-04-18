package com.example.myapplication.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.data.obtenerMapaTipos
import com.example.myapplication.data.room.Recordatorio

@Composable
fun RecordatorioCard(
    recordatorio: Recordatorio,
    onClick: () -> Unit = {},
    modoSeleccion: Boolean = false,
    seleccionado: Boolean = false,
    onSeleccionar: (Boolean) -> Unit = {},
    updateActivo: (Boolean) -> Unit = {}
) {
    val context = LocalContext.current
   val mapaTipos  = remember { obtenerMapaTipos(context) }


    val (_, icono) = mapaTipos[recordatorio.tipo] ?: mapaTipos["otro"]!!

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(){ onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "$icono  ${recordatorio.titulo}",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                if (modoSeleccion) {
                    Checkbox(
                        checked = seleccionado,
                        onCheckedChange = { onSeleccionar(it) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }else{
                    Switch(
                        checked = recordatorio.activo,
                        onCheckedChange = { nuevoEstado ->
                            updateActivo(!recordatorio.activo)
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            uncheckedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF4CAF50),
                            uncheckedTrackColor = Color(0xFFF44336)
                        ),
                        modifier = Modifier.scale(0.75f) // Reduce el tamaño
                    )
                }

            }


            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = when (recordatorio.tipo) {
                    "ayuno" -> "${recordatorio.horasAyuno}h ${context.getString(R.string.horas_ayuno)}  · ${recordatorio.horasComida}h ${context.getString(R.string.horas_comida)} "
                    else -> "${context.getString(R.string.intervalo_horas)}  ${recordatorio.intervaloHoras}h"
                },
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text("${context.getString(R.string.ultima_hora)}: ${recordatorio.ultimaHora} ➡️ ${context.getString(R.string.proxima_hora)}: ${recordatorio.proximaHora}",
                style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}