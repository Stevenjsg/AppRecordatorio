package com.example.myapplication.screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.data.Recordatorio
import com.example.myapplication.navigation.AppScreen
import com.example.myapplication.viewmodel.RecordatorioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormCrud(navController: NavController, id: Int? = null) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    if (id != null) {
                        Text("Editar Recordatorio")
                    } else {
                        Text("Nuevo Recordatorio")

                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(route = AppScreen.ListScreen.route) }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atras"
                        )
                    }

                },
                actions = {
                    Text(
                        text = "Guardar",
                        modifier = Modifier.padding(end = 8.dp)
                        //onClick = { navController.navigate(route = AppScreen.ListScreen.route) }
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        }
    ) { innerPadding ->
        BodyCrud(
            modifier = Modifier.padding(innerPadding),
            id = id,
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyCrud(
    modifier: Modifier = Modifier,
    id: Int? = null,
    navController: NavController,
    viewModel: RecordatorioViewModel = viewModel(),
) {
    var titulo by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var intervaloHoras by remember { mutableStateOf("") }
    var horasAyuno by remember { mutableStateOf("") }
    var horasComida by remember { mutableStateOf("") }
    var activo by remember { mutableStateOf(true) }

    // ✅ Si es edición, traemos los datos y los metemos en los campos
    LaunchedEffect(id) {
        if (id != null) {
            viewModel.obtenerPorId(id) { recordatorio ->
                recordatorio?.let {
                    titulo = it.titulo
                    tipo = it.tipo
                    intervaloHoras = it.intervaloHoras.toString()
                    horasAyuno = it.horasAyuno?.toString() ?: ""
                    horasComida = it.horasComida?.toString() ?: ""
                    activo = it.activo
                    id
                }
            }
        }
    }

    Column(
        modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Aquí el selector de tipo

        SelectorTipoRecordatorio(
            tipoSeleccionado = tipo,
            onTipoSeleccionado = { tipo = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (tipo == "ayuno") {
            OutlinedTextField(
                value = horasAyuno,
                onValueChange = { horasAyuno = it },
                label = { Text("Horas de ayuno") },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = horasComida,
                onValueChange = { horasComida = it },
                label = { Text("Horas de comida") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            OutlinedTextField(
                value = intervaloHoras,
                onValueChange = { intervaloHoras = it },
                label = { Text("Intervalo en horas") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Activo:")
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = activo, onCheckedChange = { activo = it })
        }

        Button(
            onClick = {
                val recordatorio = Recordatorio(
                    id = id ?: 0,
                    titulo = titulo,
                    tipo = if (tipo == "") "otro" else tipo,
                    intervaloHoras = if (tipo != "ayuno") intervaloHoras.toIntOrNull() ?: 0 else 0,
                    horasAyuno = if (tipo == "ayuno") horasAyuno.toIntOrNull() else null,
                    horasComida = if (tipo == "ayuno") horasComida.toIntOrNull() else null,
                    ultimaHora = "00:00",
                    proximaHora = "00:00",
                    activo = activo
                )

                if (id != null) {
                    viewModel.update(recordatorio)
                    Log.d("Estamos en update", recordatorio.toString())
                } else {
                    viewModel.guardar(recordatorio)
                    Log.d("Estamos en el else", recordatorio.toString())
                }

                navController.navigate(route = AppScreen.ListScreen.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar")
        }

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectorTipoRecordatorio(
    tipoSeleccionado: String,
    onTipoSeleccionado: (String) -> Unit,
) {
    val opciones = listOf("agua", "ayuno", "suplemento", "otro")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = tipoSeleccionado,
            onValueChange = {},
            readOnly = true,
            label = { Text("Tipo") },
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
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion.replaceFirstChar { it.uppercaseChar() }) },
                    onClick = {
                        onTipoSeleccionado(opcion)
                        expanded = false
                    }
                )
            }
        }
    }
}


//@Preview (showSystemUi = true)
//@Composable
//fun FormCrudPreview(){
//   FormCrud()
//}
