package com.example.moovit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

class TelaInicial : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

        }
    }
}

@Composable
fun TelaPrincipal(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Pesquisa()
        Spacer(modifier = Modifier.height(12.dp))
        Frequentes(navController = navController)
        Spacer(modifier = Modifier.height(12.dp))
        FavoritosCasa()
        Spacer(modifier = Modifier.height(12.dp))
        FavoritoTrabalho()
        Spacer(modifier = Modifier.height(12.dp))
        FavoritoDestino(navController = navController)
        Spacer(modifier = Modifier.weight(1f))
        Rodape(
            modifier = Modifier,
            navController = navController,
        )
    }
}

@Composable
fun Pesquisa() {
    Card(
        colors = CardDefaults.cardColors(Color(0xFF1C1C1C)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                "Para onde você quer ir?",
                color = Color.Gray,
                fontSize = 16.sp
            )
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
fun Frequentes(navController: NavHostController){
    Card(
        colors = CardDefaults.cardColors(Color(0xFF1C1C1C)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("Direçoes")
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                "Destinos frequentes",
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                "Para ABO - Associação Brasileira de Odontologia - Secção Paraná",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF2D2D2D), RoundedCornerShape(8.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "➡ 380 Detran ➡ 👤",
                    color = Color(0xFFFF9500),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun FavoritosCasa() {
    Card(
        colors = CardDefaults.cardColors(Color(0xFF1C1C1C)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "🏠 Casa",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Você está perto",
                color = Color(0xFFFF9500),
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun FavoritoTrabalho(){
    Card(
        colors = CardDefaults.cardColors(Color(0xFF1C1C1C)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "🌇 Trabalho",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Toque para Editar",
                color = Color(0xFFFF9500),
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun FavoritoDestino(navController: NavHostController) {
    Card(
        colors = CardDefaults.cardColors(Color(0xFF1C1C1C)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("Horarios")
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "☸ ABO-Associação Brasileira de Odontologia - Secção Paraná",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Rua Dias da Rocha Filho - Alto da XV, Curitiba - PR, Brasil",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun Rodape(modifier: Modifier, navController: NavHostController) {
    Button(
        onClick = { navController.navigate("Estaçoes") },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5722)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = "Estações",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}