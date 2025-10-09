package com.example.moovit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

class Horarios(onBack: () -> Boolean) : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
        }
    }
}

data class RotaHorario(
    val tempo: String,
    val horaChegada: String,
    val preco: String,
    val linha: String,
    val saida: String,
    val chegada: String,
    val iconeCor: Color = Color(0xFFFF5722)
)


@Composable
fun TelaHorarios(navController: NavHostController) {
    val viewModel: HorariosViewModel = viewModel()
    val horariosState = viewModel.horarios.collectAsState()

    var pesquisa by remember { mutableStateOf("") }
    val rotasFiltradas = horariosState.value.filter {
        it.linha.contains(pesquisa, ignoreCase = true) || it.destino.contains(pesquisa, ignoreCase = true)
    }

    var showDialog by remember { mutableStateOf(false) }
    var editarHorario by remember { mutableStateOf<HorarioBanco?>(null) }

    Box(Modifier.fillMaxSize().background(Color.Black)) {
        Column {
            HeaderHorarios(pesquisa, { pesquisa = it }, navController = navController)
            LazyColumn(
                Modifier.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(rotasFiltradas) { horario ->
                    CardHorarioBanco(horario, onEdit = { editarHorario = it; showDialog = true }, onDelete = { viewModel.deletar(it) })
                }
            }
        }

        FloatingActionButton(
            onClick = { editarHorario = null; showDialog = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Adicionar")
        }
    }

    if (showDialog) {
        EditHorarioDialog(
            horario = editarHorario,
            onDismiss = { showDialog = false },
            onSave = { h ->
                if (h.id == 0) viewModel.inserir(h) else viewModel.atualizar(h)
                showDialog = false
            }
        )
    }
}

@Composable
fun HeaderHorarios(
    textoPesquisa: String,
    onTextoMudou: (String) -> Unit,
    navController: NavHostController,
) {
    Surface(color = Color(0xFF2D2D2D), modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                { navController.navigate("TelaInicial") }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Voltar",
                    tint = Color.White
                )

            }

            Spacer(Modifier.width(16.dp))

            Card(
                colors = CardDefaults.cardColors(Color(0xFF424242)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(modifier = Modifier.padding(12.dp)) {
                    BasicTextField(
                        value = textoPesquisa,
                        onValueChange = onTextoMudou,
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (textoPesquisa.isEmpty()) {
                                Text("Digite a linha ou destino", color = Color.Gray, fontSize = 16.sp)
                            }
                            innerTextField()
                        }
                    )
                }
            }

            Spacer(Modifier.width(16.dp))
            Icon(Icons.Default.Search, contentDescription = "Pesquisar", tint = Color.White)
        }
    }
}

@Composable
fun CardHorario(rota: RotaHorario, navController: NavHostController) {
    Card(
        colors = CardDefaults.cardColors(Color(0xFF1C1C1C)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
            .clickable {
                navController.navigate("Direçoes")
            }
    ) {
        Column(Modifier.padding(16.dp)) {
            // Linha de tempo e chegada
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(rota.tempo, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text("Hora de chegada: ${rota.horaChegada}", color = Color.Gray, fontSize = 14.sp)
            }

            Spacer(Modifier.height(8.dp))

            // Linha/ônibus
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(36.dp).background(rota.iconeCor, RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🚌", fontSize = 18.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(rota.linha, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text("${rota.saida} → ${rota.chegada}", color = Color.Gray, fontSize = 12.sp)
                }
            }

            Spacer(Modifier.height(8.dp))

            // Preço
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(rota.preco, color = Color(0xFFFF9500), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("🌱 CO2: 48g", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}


@Composable
fun CardHorarioBanco(horario: HorarioBanco, onEdit: (HorarioBanco) -> Unit, onDelete: (HorarioBanco) -> Unit) {
    Card(
        colors = CardDefaults.cardColors(Color(0xFF1C1C1C)),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
            .clickable { /* manter comportamento original: navegar */ }
    ) {
        Column(Modifier.padding(16.dp)) {
            // Linha de tempo e chegada (usamos hora como "chegada")
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(horario.hora, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Row {
                    IconButton(onClick = { onEdit(horario) }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFFFF9500))
                    }
                    IconButton(onClick = { onDelete(horario) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "Deletar", tint = Color.Red)
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            // Linha/ônibus
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(36.dp).background(Color(0xFFFF5722), RoundedCornerShape(6.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("🚌", fontSize = 18.sp)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(horario.linha, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(horario.destino, color = Color.Gray, fontSize = 12.sp)
                }
            }

            Spacer(Modifier.height(8.dp))

            // Preço / info extra — usamos destino novamente para preencher o espaço similar ao design
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("", color = Color(0xFFFF9500), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHorarioDialog(horario: HorarioBanco?, onDismiss: () -> Unit, onSave: (HorarioBanco) -> Unit) {
    var linha by remember { mutableStateOf(horario?.linha ?: "") }
    var hora by remember { mutableStateOf(horario?.hora ?: "") }
    var destino by remember { mutableStateOf(horario?.destino ?: "") }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val h = HorarioBanco(id = horario?.id ?: 0, linha = linha, hora = hora, destino = destino)
                onSave(h)
            }) { Text("Salvar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
        title = { Text(if (horario == null) "Novo Horário" else "Editar Horário") },
        text = {
            Column {
                TextField(value = linha, onValueChange = { linha = it }, label = { Text("Linha") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = hora, onValueChange = { hora = it }, label = { Text("Hora") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = destino, onValueChange = { destino = it }, label = { Text("Destino") })
            }
        }
    )
}
