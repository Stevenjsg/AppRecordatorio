package com.example.myapplication.screen

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.obtenerMapaTipos
import com.example.myapplication.data.room.Recordatorio
import com.example.myapplication.navigation.AppScreen
import com.example.myapplication.viewmodel.RecordatorioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormCrud(navController: NavController, id: Int? = null) {
    val context = LocalContext.current
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    if (id != null) {
                        Text(text = context.getString(R.string.editar_recordatorio))
                    } else {
                        Text(text = context.getString(R.string.nuevo_recordatorio))

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
                        text = context.getString(R.string.guardar),
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
            navController = navController,
            context = context
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyCrud(
    modifier: Modifier = Modifier,
    id: Int? = null,
    navController: NavController,
    context: android.content.Context = LocalContext.current ,
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
            label = { Text(text = context.getString(R.string.titulo)) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Aquí el selector de tipo

        SelectorTipoRecordatorio(
            tipoSeleccionado = tipo,
            onTipoSeleccionado = { tipo = it },
            context = context
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (tipo == "ayuno") {
            OutlinedTextField(
                value = horasAyuno,
                onValueChange = { horasAyuno = it },
                label = { Text( text = context.getString(R.string.horas_ayuno)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = horasComida,
                onValueChange = { horasComida = it },
                label = { Text(text = context.getString(R.string.horas_comida) )},
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            OutlinedTextField(
                value = intervaloHoras,
                onValueChange = { nuevoValor ->
                    // Aquí va la lógica para que solo acepte números y no más de 2 dígitos, por ejemplo
                    if (nuevoValor.length <= 2 && nuevoValor.all { it.isDigit() }) {
                        intervaloHoras = nuevoValor
                    }
                },
                label = { Text("Intervalo (horas)") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text(text = context.getString(R.string.activo))
            Spacer(modifier = Modifier.width(8.dp))
            Switch(checked = activo, onCheckedChange = { activo = it })
        }

        Button(
            onClick = {
                val resultado = validarCampos(titulo, tipo, intervaloHoras, horasAyuno, horasComida)

                if (!resultado.esValido) {
                    Toast.makeText(context, resultado.mensajeError, Toast.LENGTH_SHORT).show()
                    return@Button
                }

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
            Text(context.getString(R.string.guardar))
        }

    }
}


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

data class ValidacionResultado(
    val esValido: Boolean,
    val mensajeError: String? = null
)

fun validarCampos(
    titulo: String,
    tipo: String,
    intervaloHoras: String,
    horasAyuno: String,
    horasComida: String
): ValidacionResultado {
    if (titulo.isBlank()) return ValidacionResultado(false, "El título no puede estar vacío")

    if (tipo.isBlank() || tipo == "Tipo") return ValidacionResultado(false, "Selecciona un tipo válido")

    if (tipo != "ayuno") {
        val intervalo = intervaloHoras.toIntOrNull()
        if (intervalo == null || intervalo <= 0)
            return ValidacionResultado(false, "El intervalo debe ser un número mayor que 0")
    }

    if (tipo == "ayuno") {
        val ayuno = horasAyuno.toIntOrNull()
        val comida = horasComida.toIntOrNull()

        if (ayuno == null || ayuno <= 0)
            return ValidacionResultado(false, "Las horas de ayuno deben ser válidas y mayores que 0")
        if (comida == null || comida <= 0)
            return ValidacionResultado(false, "Las horas de comida deben ser válidas y mayores que 0")
    }

    return ValidacionResultado(true)
}


//@Preview (showSystemUi = true)
//@Composable
//fun FormCrudPreview(){
//   FormCrud()
//}
