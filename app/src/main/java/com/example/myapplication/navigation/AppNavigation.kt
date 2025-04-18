package com.example.myapplication.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myapplication.screen.FormCrud
import com.example.myapplication.screen.ListScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = AppScreen.ListScreen.route) {
        composable(AppScreen.ListScreen.route) {
            ListScreen(navController)
        }

        composable(
            route = AppScreen.FormScreen.route + "?id={id}" ,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val idString = backStackEntry.arguments?.getString("id")
            val id: Int? = idString?.toIntOrNull()
            FormCrud(navController = navController, id = id)
        }
    }
}