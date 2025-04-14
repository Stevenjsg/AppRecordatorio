package com.example.myapplication.ui.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.Recordatorio

@Composable
fun RecordatorioCard(recordatorio: Recordatorio, onClick: () -> Unit = {}) {
    val icono = when (recordatorio.tipo) {
        "agua" -> "🥤"
        "ayuno" -> "⏳"
        "suplemento" -> "💊"
        else -> "🔔"
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(){ onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {
            Text("$icono  ${recordatorio.titulo}", style = MaterialTheme.typography.titleMedium)

            if (recordatorio.tipo == "ayuno") {
                Text("Ayuno: ${recordatorio.horasAyuno}h | Comer: ${recordatorio.horasComida}h")
            } else {
                Text("Cada ${recordatorio.intervaloHoras} horas")
            }

            Text("Última: ${recordatorio.ultimaHora} |  Próxima: ${recordatorio.proximaHora}")


            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Activo:", modifier = Modifier.padding(top = 8.dp))
                Switch(modifier = Modifier.size(16.dp).padding(start = 36.dp, top = 8.dp),checked = recordatorio.activo, onCheckedChange = { /* toggle */ })
            }
        }
    }
}