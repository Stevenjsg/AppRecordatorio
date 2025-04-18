package com.example.myapplication.ui.theme

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.R
import com.example.myapplication.data.obtenerMapaTipos

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorTipoRecordatorio(
    tipoSeleccionado: String,
    onTipoSeleccionado: (String) -> Unit,
    context: Context = LocalContext.current
) {
    val mapaTipos = obtenerMapaTipos(context)
    val opciones = mapaTipos.keys.toList()
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = mapaTipos[tipoSeleccionado]?.first ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text(text = context.getString(R.string.tipo)) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { clave ->
                val (nombre, icono) = mapaTipos[clave]!!
                DropdownMenuItem(
                    text = { Text("$icono $nombre") },
                    onClick = {
                        onTipoSeleccionado(clave) // clave = "agua", "ayuno", etc.
                        expanded = false
                    }
                )
            }
        }
    }
}