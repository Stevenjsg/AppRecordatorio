@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.myapplication.screen

import android.util.Log
import com.example.myapplication.R
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.myapplication.navigation.AppScreen
import com.example.myapplication.ui.theme.RecordatorioCard
import com.example.myapplication.viewmodel.RecordatorioViewModel

@Composable
fun ListScreen(navController: NavController) {
    val context = LocalContext.current
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
                    Text(
                        text =" ${context.getString(R.string.eliminar)}",
                        modifier = Modifier.padding(end = 8.dp)
                        //onClick = { navController.navigate(route = AppScreen.ListScreen.route) }
                    )
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
        FirstLayout(
            modifier = Modifier.padding(innerPadding),
            navController
        )
    }

}

@Composable
fun FirstLayout(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: RecordatorioViewModel = viewModel(),
) {
    val recordatorios by viewModel.recordatorios.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.cargarRecordatorios()

    }
//    val Mockedrecordatorios = listOf(
//        Recordatorio(
//            id = 1,
//            titulo = "Beber agua",
//            intervaloHoras = 8,
//            ultimaHora = "06:00",
//            proximaHora = "14:00",
//            tipo = "agua",
//            activo = true
//        ),
//        Recordatorio(
//            id = 2,
//            titulo = "Ayuno 16/8",
//            intervaloHoras = 0, // no se usa aquí
//            ultimaHora = "20:00",
//            proximaHora = "12:00",
//            tipo = "ayuno",
//            activo = true,
//            horasAyuno = 16,
//            horasComida = 8
//        ),
//        Recordatorio(
//            id = 3,
//            titulo = "Suplemento de Omega 3",
//            intervaloHoras = 12,
//            ultimaHora = "08:00",
//            proximaHora = "20:00",
//            tipo = "suplemento",
//            activo = false
//        )
//    )

    LazyColumn(modifier = modifier.padding(horizontal = 16.dp)) {
        items(recordatorios.size) { recordatorio ->
            Log.d("Recordatorio", recordatorios[recordatorio].toString())
            RecordatorioCard(
                recordatorio = recordatorios[recordatorio],
                onClick = {
                    navController.navigate(AppScreen.FormScreen.createRoute(recordatorios[recordatorio].id))
                }
            )
        }
    }

}

