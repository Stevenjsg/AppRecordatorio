package com.example.myapplication.screen

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.room.Recordatorio
import com.example.myapplication.navigation.AppScreen
import com.example.myapplication.ui.theme.SelectorTipoRecordatorio
import com.example.myapplication.utils.programarNotificacion
import com.example.myapplication.utils.validarCampos
import com.example.myapplication.viewmodel.RecordatorioViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar

@RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyCrud(
    modifier: Modifier = Modifier,
    id: Int? = null,
    navController: NavController,
    context: Context = LocalContext.current,
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
                label = { Text(text = context.getString(R.string.horas_ayuno)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = horasComida,
                onValueChange = { horasComida = it },
                label = { Text(text = context.getString(R.string.horas_comida)) },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            OutlinedTextField(
                value = intervaloHoras,
                onValueChange = { intervaloHoras = it },
                label = { Text(text = context.getString(R.string.intervalo_horas)) },
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

                // 🕓 Hora actual y cálculo de proximaHora
                val ahora = LocalTime.now()
                val formatoHora = DateTimeFormatter.ofPattern("HH:mm")

                val proximaHora = ahora.plusHours(
                    if (tipo == "ayuno") horasAyuno.toLongOrNull() ?: 0
                    else intervaloHoras.toLongOrNull() ?: 0
                ).format(formatoHora)

                val recordatorio = Recordatorio(
                    id = id ?: 0,
                    titulo = titulo,
                    tipo = if (tipo == "") "otro" else tipo,
                    intervaloHoras = if (tipo != "ayuno") intervaloHoras.toIntOrNull() ?: 0 else 0,
                    horasAyuno = if (tipo == "ayuno") horasAyuno.toIntOrNull() else null,
                    horasComida = if (tipo == "ayuno") horasComida.toIntOrNull() else null,
                    ultimaHora = ahora.format(formatoHora),
                    proximaHora = proximaHora,
                    activo = activo
                )

                if (id != null) {
                    viewModel.update(recordatorio)
                } else {
                    viewModel.guardar(recordatorio)
                }

                // ✅ Programar notificación
                val triggerTime = horaAFechaEnMillis(proximaHora)
                programarNotificacion(
                    context = context,
                    triggerTimeMillis = triggerTime,
                    titulo = "Recordatorio: $titulo",
                    mensaje = "¿Ya completaste tu $tipo?",
                    requestCode = recordatorio.id
                )

                navController.navigate(route = AppScreen.ListScreen.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(context.getString(R.string.guardar))
        }

    }
}

fun horaAFechaEnMillis(horaTexto: String): Long {
    val partes = horaTexto.split(":")
    if (partes.size != 2) return System.currentTimeMillis()

    val hora = partes[0].toIntOrNull() ?: return System.currentTimeMillis()
    val minuto = partes[1].toIntOrNull() ?: return System.currentTimeMillis()

    val calendario = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hora)
        set(Calendar.MINUTE, minuto)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)

        if (timeInMillis <= System.currentTimeMillis()) {
            add(Calendar.DAY_OF_YEAR, 1)
        }
    }

    return calendario.timeInMillis
}

