package com.example.moovit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class Direcoes : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TelaEstacoes(navController = rememberNavController())
        }
    }
}
@Composable
fun TelaLinhas(
    navController: NavHostController,
    linhaVM: LinhaTransporteViewModel = viewModel()
) {
    val listaLinhas by linhaVM.listaLinhas.collectAsState()

    var mostrarDialogoCriar by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Linhas de Transporte", fontWeight = FontWeight.Bold)//se quiser coloca cor
            FloatingActionButton(
                onClick = { mostrarDialogoCriar = true },
                //Se quiser coloca cor
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar Linha")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (listaLinhas.isEmpty()) {
            Text("Nenhuma linha cadastrada.", color = Color.Gray)
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(listaLinhas) { linha ->
                    CardLinhaTransporte(linha, linhaVM)
                }
            }
        }
    }

    if (mostrarDialogoCriar) {
        DialogoLinhaTransporte(
            titulo = "Adicionar Nova Linha",
            onDismiss = { mostrarDialogoCriar = false },
            onSalvar = { nome, numero, tipo, cor ->
                linhaVM.adicionarLinha(nome, numero, tipo, cor)
                mostrarDialogoCriar = false
            }
        )
    }
}

@Composable
fun CardLinhaTransporte(linha: LinhaTransporteBanco, linhaVM: LinhaTransporteViewModel) {
    var mostrarEditar by remember { mutableStateOf(false) }
    var mostrarExcluir by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(//se quiser coloca cor
                    text = "${linha.numero} - ${linha.nome}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Text(//se quiser coloca cor
                    text = "Tipo: ${linha.tipo} | Cor: ${linha.cor}",
                    fontSize = 14.sp
                )
                Text(//se quiser coloca cor
                    text = "ID: ${linha.id}",
                    fontSize = 12.sp
                )
            }

            IconButton(onClick = { mostrarEditar = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar")//se quiser coloca cor
            }

            IconButton(onClick = { mostrarExcluir = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Deletar")//se quiser coloca cor
            }
        }
    }

    if (mostrarEditar) {
        DialogoLinhaTransporte(
            titulo = "Editar Linha (ID: ${linha.id})",
            linhaInicial = linha,
            onDismiss = { mostrarEditar = false },
            onSalvar = { nome, numero, tipo, cor ->
                val linhaAtualizada = linha.copy(
                    nome = nome,
                    numero = numero,
                    tipo = tipo,
                    cor = cor
                )
                linhaVM.atualizarLinhaExistente(linhaAtualizada)
                mostrarEditar = false
            }
        )
    }

    if (mostrarExcluir) {
        AlertDialog(
            onDismissRequest = { mostrarExcluir = false },
            title = { Text("Confirmar Exclusão", fontWeight = FontWeight.Bold) },
            text = { Text("Deseja realmente excluir a linha ${linha.numero} - ${linha.nome}?") },
            confirmButton = {
                Button(
                    onClick = {
                        linhaVM.removerLinha(linha)
                        mostrarExcluir = false
                    },
                    //se quiser coloca cor
                ) {
                    Text("Excluir", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarExcluir = false }) {
                    Text("Cancelar")
                }
            },
            //se quiser coloca cor
        )
    }
}

@Composable
fun DialogoLinhaTransporte(
    titulo: String,
    linhaInicial: LinhaTransporteBanco? = null,
    onDismiss: () -> Unit,
    onSalvar: (String, String, String, String) -> Unit
) {
    var nome by remember { mutableStateOf(linhaInicial?.nome ?: "") }
    var numero by remember { mutableStateOf(linhaInicial?.numero ?: "") }
    var tipo by remember { mutableStateOf(linhaInicial?.tipo ?: "") }
    var cor by remember { mutableStateOf(linhaInicial?.cor ?: "") }

    val valido = nome.isNotBlank() && numero.isNotBlank() && tipo.isNotBlank() && cor.isNotBlank()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            //se quiser coloca cor,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(Modifier.padding(24.dp)) {
                Text(titulo, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(20.dp))

                CampoTextoLinha("Nome da Linha", nome) { nome = it }
                Spacer(Modifier.height(12.dp))
                CampoTextoLinha("Número", numero) { numero = it }
                Spacer(Modifier.height(12.dp))
                CampoTextoLinha("Tipo (Ex: Ligeirinho/Interbairros)", tipo) { tipo = it }
                Spacer(Modifier.height(12.dp))
                CampoTextoLinha("Cor", cor) { cor = it }

                Spacer(Modifier.height(24.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = Color.Gray)
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { if (valido) onSalvar(nome, numero, tipo, cor) },
                        //se quiser coloca cor
                        enabled = valido
                    ) {
                        Text("Salvar", color = Color.White)
                    }
                }
            }
        }
    }
}


@Composable
fun CampoTextoLinha(label: String, valor: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = valor,
        onValueChange = onChange,
        label = { Text(label) },
        //se quiser coloca cor,
        modifier = Modifier.fillMaxWidth()
    )
}