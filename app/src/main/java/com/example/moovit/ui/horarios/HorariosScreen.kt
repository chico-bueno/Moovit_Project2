package com.example.moovit.ui.horarios

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

@Composable
fun TelaHorarios(
    navController: NavHostController,
    viewModel: HorariosViewModel = viewModel(factory = HorariosViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(Modifier.fillMaxSize().background(Color.Black)) {
        HeaderHorarios(
            textoPesquisa = uiState.textoPesquisa,
            onTextoMudou = { viewModel.onTextoPesquisaChanged(it) },
            onVoltarClick = { navController.popBackStack() }
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            items(uiState.rotasFiltradas, key = { it.id }) { rota ->
                CardHorario(
                    rota = rota,
                    onCardClick = { navController.navigate("Linhas") }
                )
            }
        }
    }
}

@Composable
private fun HeaderHorarios(
    textoPesquisa: String,
    onTextoMudou: (String) -> Unit,
    onVoltarClick: () -> Unit
) {
    Surface(color = Color(0xFF2D2D2D), modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onVoltarClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )
            }
            Spacer(Modifier.width(16.dp))
            BasicTextField(
                value = textoPesquisa,
                onValueChange = onTextoMudou,
                singleLine = true,
                textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                modifier = Modifier
                    .weight(1f)
                    .background(Color(0xFF424242), RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                decorationBox = { innerTextField ->
                    if (textoPesquisa.isEmpty()) {
                        Text("Digite a linha ou destino", color = Color.Gray, fontSize = 16.sp)
                    }
                    innerTextField()
                }
            )
            Spacer(Modifier.width(16.dp))
            Icon(Icons.Default.Search, contentDescription = "Pesquisar", tint = Color.White)
        }
    }
}

@Composable
private fun CardHorario(
    rota: RotaHorario,
    onCardClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1C1C1C)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().clickable(onClick = onCardClick)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(rota.tempo, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Chegada: ${rota.horaChegada}", color = Color.Gray, fontSize = 14.sp)
            }
            Spacer(Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(rota.iconeCor, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🚌", fontSize = 18.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(rota.linha, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("${rota.saida} → ${rota.chegada}", color = Color.Gray, fontSize = 12.sp, maxLines = 1)
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(rota.preco, color = Color(0xFFFF9500), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("🌱 CO2: 48g", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}