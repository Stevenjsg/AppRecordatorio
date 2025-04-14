package com.example.myapplication.navigation

open class AppScreen (var route: String, var title: String){
    object ListScreen : AppScreen("list", "Lista")
    object FormScreen : AppScreen("form", "Formulario"){
        fun createRoute(id: Int?): String {
            return if (id != null){
                "form?id=$id"
            } else {
                "form"
            }
        }
    }
}