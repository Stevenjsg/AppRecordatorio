@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.R
import com.example.myapplication.data.room.Recordatorio
import com.example.myapplication.navigation.AppScreen
import com.example.myapplication.ui.theme.BadgeType
import com.example.myapplication.ui.theme.RecordatorioCard
import com.example.myapplication.utils.cancelarNotificacionProgramada
import com.example.myapplication.viewmodel.RecordatorioViewModel

@Composable
fun ListScreen(
    navController: NavController,
    viewModel: RecordatorioViewModel = viewModel()
) {

    var modoSeleccion by remember { mutableStateOf(false) }
    val seleccionados = remember { mutableStateListOf<Int>() }


    val context = LocalContext.current
    val recordatorios by viewModel.recordatorios.collectAsState()
    var filtroSeleccionado by remember { mutableStateOf<String?>(null) }

    val listaFiltrada = if (filtroSeleccionado != null) {
        Log.d("con Filtro ", filtroSeleccionado.toString())

        recordatorios.filter { it.tipo == filtroSeleccionado }
    } else {
        Log.d("sin Filtro ", filtroSeleccionado.toString())
         recordatorios
    }

//    LaunchedEffect(Unit) {
//        viewModel.cargarRecordatorios()
//    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text("${context.getString(R.string.app_name)} ⏰")
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(route = AppScreen.ListScreen.route) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = "Home"
                        )
                    }

                },
                actions = {
                    IconButton(onClick = {
                        if (modoSeleccion && seleccionados.isNotEmpty()) {
                            val aEliminar = recordatorios.filter { seleccionados.contains(it.id) }
                            viewModel.eliminar(aEliminar)
                            aEliminar.forEach {
                                cancelarNotificacionProgramada(context, it.id)
                            }
                            seleccionados.clear()
                            modoSeleccion = false
                        } else {
                            modoSeleccion = !modoSeleccion
                            seleccionados.clear()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = if (seleccionados.isNotEmpty()) Red else Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(route = AppScreen.FormScreen.route) })
            {
                Icon(imageVector = Icons.Filled.DateRange, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

            BadgeType(
                recordatorios,
                onFiltroSeleccionado = { filtroSeleccionado = it },
                tipoSeleccionado = filtroSeleccionado,
                context
            )

            FirstLayout(
                recordatorios = listaFiltrada,
                navController = navController,
                modoSeleccion = modoSeleccion,
                seleccionados = seleccionados,
                viewModel = viewModel

            )
        }


    }

}

@Composable
fun FirstLayout(
    navController: NavController,
    recordatorios: List<Recordatorio>,
    modoSeleccion: Boolean,
    seleccionados: SnapshotStateList<Int>,
    viewModel: RecordatorioViewModel
) {

    LazyColumn(modifier = Modifier.padding(horizontal = 16.dp)) {
        items(recordatorios) { recordatorio ->
            Log.d("Recordatorio", recordatorio.toString())
            val estaSeleccionado = seleccionados.contains(recordatorio.id)
            RecordatorioCard(
                recordatorio = recordatorio,
                modoSeleccion = modoSeleccion,
                seleccionado = estaSeleccionado,
                onSeleccionar = { seleccionado ->
                    if (seleccionado) seleccionados.add(recordatorio.id)
                    else seleccionados.remove(recordatorio.id)
                },
                onClick = {
                    if (modoSeleccion) {
                        if (estaSeleccionado) seleccionados.remove(recordatorio.id)
                        else seleccionados.add(recordatorio.id)
                    } else {
                        navController.navigate(AppScreen.FormScreen.createRoute(recordatorio.id))
                    }
                },
                updateActivo = {
                    viewModel.update(recordatorio.copy(activo = it))
                }
            )
        }

    }

}

