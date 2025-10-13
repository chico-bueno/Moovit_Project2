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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

class Direcoes : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TelaLinhas(navController = rememberNavController())
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
    var textoPesquisa by remember { mutableStateOf("") }

    val linhasFiltradas = listaLinhas.filter {
        it.nome.contains(textoPesquisa, ignoreCase = true) ||
                it.numero.contains(textoPesquisa, ignoreCase = true) ||
                it.tipo.contains(textoPesquisa, ignoreCase = true)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        HeaderDirecoes(textoPesquisa) { textoPesquisa = it }

        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Linhas de Transporte",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 20.sp
                )
                FloatingActionButton(
                    onClick = { mostrarDialogoCriar = true },
                    containerColor = Color(0xFFFF9500),
                    contentColor = Color.White,
                    modifier = Modifier.size(48.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Adicionar Linha")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (linhasFiltradas.isEmpty()) {
                Text("Nenhuma linha encontrada.", color = Color.Gray)
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(linhasFiltradas) { linha ->
                        CardLinhaTransporte(linha, linhaVM)
                    }
                }
            }
        }
    }

    if (mostrarDialogoCriar) {
        DialogoLinhaTransporte(
            titulo = "Adicionar Nova Linha",
            onDismiss = { mostrarDialogoCriar = false },
            onSalvar = { nome, numero, tipo, corNome ->
                val corConvertida = corPorNome(corNome)
                linhaVM.adicionarLinha(nome, numero, tipo, corNome) // mantém nome salvo
                mostrarDialogoCriar = false
            }
        )
    }
}

// Cabeçalho
@Composable
fun HeaderDirecoes(texto: String, onMudou: (String) -> Unit) {
    Surface(color = Color(0xFF2D2D2D), modifier = Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(Modifier.width(8.dp))
            Card(
                colors = CardDefaults.cardColors(Color(0xFF424242)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Search, contentDescription = "Pesquisar", tint = Color.Gray)
                    Spacer(Modifier.width(8.dp))
                    BasicTextField(
                        value = texto,
                        onValueChange = onMudou,
                        singleLine = true,
                        textStyle = TextStyle(color = Color.White, fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (texto.isEmpty()) {
                                Text("Pesquise uma linha, número ou tipo...", color = Color.Gray, fontSize = 16.sp)
                            }
                            innerTextField()
                        }
                    )
                }
            }
            Spacer(Modifier.width(8.dp))
        }
    }
}

@Composable
fun CardLinhaTransporte(linha: LinhaTransporteBanco, linhaVM: LinhaTransporteViewModel) {
    var mostrarEditar by remember { mutableStateOf(false) }
    var mostrarExcluir by remember { mutableStateOf(false) }

    val corDaLinha = corPorNome(linha.cor)

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
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .background(corDaLinha, RoundedCornerShape(4.dp))
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${linha.numero} - ${linha.nome}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.White
                )
                Text(
                    text = "Tipo: ${linha.tipo} | Cor: ${linha.cor}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "ID: ${linha.id}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            IconButton(onClick = { mostrarEditar = true }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = Color(0xFFFF9500)
                )
            }

            IconButton(onClick = { mostrarExcluir = true }) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "Deletar",
                    tint = Color(0xFFFF5252)
                )
            }
        }
    }

    if (mostrarEditar) {
        DialogoLinhaTransporte(
            titulo = "Editar Linha (ID: ${linha.id})",
            linhaInicial = linha,
            onDismiss = { mostrarEditar = false },
            onSalvar = { nome, numero, tipo, corNome ->
                val linhaAtualizada = linha.copy(
                    nome = nome,
                    numero = numero,
                    tipo = tipo,
                    cor = corNome
                )
                linhaVM.atualizarLinhaExistente(linhaAtualizada)
                mostrarEditar = false
            }
        )
    }

    if (mostrarExcluir) {
        AlertDialog(
            onDismissRequest = { mostrarExcluir = false },
            title = { Text("Confirmar Exclusão", fontWeight = FontWeight.Bold, color = Color.White) },
            text = { Text("Deseja realmente excluir a linha ${linha.numero} - ${linha.nome}?", color = Color.White) },
            confirmButton = {
                Button(
                    onClick = {
                        linhaVM.removerLinha(linha)
                        mostrarExcluir = false
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFFFF5252))
                ) {
                    Text("Excluir", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarExcluir = false }) {
                    Text("Cancelar", color = Color.Gray)
                }
            },
            containerColor = Color(0xFF2D2D2D),
            titleContentColor = Color.White,
            textContentColor = Color.White
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
            colors = CardDefaults.cardColors(Color(0xFF2D2D2D)),
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
                CampoTextoLinha("Cor (ex: vermelho, azul, verde...)", cor) { cor = it }

                Spacer(Modifier.height(24.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = Color.Gray)
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { if (valido) onSalvar(nome, numero, tipo, cor) },
                        colors = ButtonDefaults.buttonColors(Color(0xFFFF9500)),
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
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = Color(0xFFFF9500),
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color(0xFFFF9500),
            unfocusedLabelColor = Color.Gray
        ),
        modifier = Modifier.fillMaxWidth()
    )
}

// Conversão hex para nome de cor
fun corPorNome(nome: String): Color {
    return when (nome.lowercase()) {
        "vermelho" -> Color(0xFFFF3B30)
        "azul" -> Color(0xFF007AFF)
        "verde" -> Color(0xFF34C759)
        "amarelo" -> Color(0xFFFFD60A)
        "laranja" -> Color(0xFFFF9500)
        "roxo" -> Color(0xFFAF52DE)
        "rosa" -> Color(0xFFFF2D55)
        "cinza" -> Color(0xFF8E8E93)
        "preto" -> Color.Black
        "branco" -> Color.White
        else -> Color(0xFFFF9500) // cor padrão
    }
}
