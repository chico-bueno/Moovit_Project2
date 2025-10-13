package com.example.moovit


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation(){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "TelaInicial"){

        composable("TelaInicial") { TelaPrincipal(navController) }
        composable("Estaçoes") { TelaEstacoes(navController) }
        composable("Horarios") { TelaHorarios(navController) }
        composable("Direçoes"){ TelaLinhas(navController) }

    }
}

