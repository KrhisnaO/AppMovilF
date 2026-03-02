package com.example.appmovil.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.appmovil.ui.screens.HomeScreen
import com.example.appmovil.ui.screens.LoginScreen
import com.example.appmovil.ui.screens.RecoverScreen
import com.example.appmovil.ui.screens.RegisterScreen

@Composable
fun AppNavigation (){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NavRouter.HomeScreen.route) {
        composable (route = NavRouter.HomeScreen.route){
            HomeScreen(navController)
        }
        composable(route = NavRouter.LoginScreen.route){
            LoginScreen(navController)
        }
        composable(route = NavRouter.RegisterScreen.route){
            RegisterScreen(navController)
        }
        composable(route = NavRouter.RecoverScreen.route){
            RecoverScreen(navController)
        }

    }
}
