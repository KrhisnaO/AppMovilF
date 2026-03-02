package com.example.appmovil.navigation

sealed class NavRouter (val route: String ){
    object HomeScreen : NavRouter ("home")
    object LoginScreen : NavRouter ("login")
    object RegisterScreen : NavRouter ("register")
    object RecoverScreen : NavRouter ("recover")
}