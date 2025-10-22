package com.example.moovit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moovit.ui.estacoes.TelaEstacoes
import com.example.moovit.ui.horarios.TelaHorarios
import com.example.moovit.ui.linhas.TelaLinhas
import com.example.moovit.ui.telainicial.TelaPrincipal

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "TelaInicial"){

        composable("TelaInicial") { TelaPrincipal(navController) }
        composable("Estacoes") { TelaEstacoes(navController) }
        composable("Horarios") { TelaHorarios(navController) }
        composable("Linhas"){ TelaLinhas(navController) }

    }
}