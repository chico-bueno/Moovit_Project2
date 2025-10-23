package com.example.moovit.ui.estacoes

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.moovit.data.local.FavoritosBanco
import kotlinx.coroutines.launch

@Composable
fun TelaEstacoes(
    navController: NavHostController,
    viewModel: EstacoesViewModel = viewModel(factory = EstacoesViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    var mostrarDialogoCriar by remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        HeaderEstacoes(uiState.textoPesquisa) { viewModel.onTextoPesquisaChanged(it) }
        AbasEstacoes(uiState.abaSelecionada) { viewModel.onAbaSelecionada(it) }

        if (uiState.abaSelecionada == "favoritas") {
            TelaFavoritos(
                favoritos = uiState.favoritos,
                viewModel = viewModel,
                onAdd = { mostrarDialogoCriar = true }
            )
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(uiState.estacoesFiltradas) { estacao ->
                    CardEstacaoMoovit(
                        estacao = estacao,
                        favoritos = uiState.favoritos,
                        viewModel = viewModel
                    )
                }
            }
        }
    }

    if (mostrarDialogoCriar) {
        DialogoFavorito(
            titulo = "Adicionar Favorito",
            onDismiss = { mostrarDialogoCriar = false },
            onSalvar = { est, num, nom ->
                viewModel.adicionarFavorito(est, num, nom)
                mostrarDialogoCriar = false
            }
        )
    }
}

@Composable
private fun TelaFavoritos(
    favoritos: List<FavoritosBanco>,
    viewModel: EstacoesViewModel,
    onAdd: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Favoritos", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            FloatingActionButton(
                onClick = onAdd,
                containerColor = Color(0xFFFF9500),
                contentColor = Color.White,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Adicionar")
            }
        }

        Spacer(Modifier.height(16.dp))

        if (favoritos.isEmpty()) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .background(Color(0xFF1E1E1E), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Outlined.Star,
                        contentDescription = null,
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Nenhum favorito ainda", color = Color.Gray, fontSize = 16.sp)
                    Text("Clique no + para adicionar", color = Color.Gray, fontSize = 14.sp)
                }
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(favoritos, key = { it.idFav }) { favorito ->
                    CardFavorito(favorito, viewModel)
                }
            }
        }
    }
}
@Composable
private fun CardFavorito(favorito: FavoritosBanco, viewModel: EstacoesViewModel) {
    var mostrarEditar by remember { mutableStateOf(false) }
    var mostrarExcluir by remember { mutableStateOf(false) }

    Card(
        Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(Color(0xFF1E1E1E)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(50.dp)
                    .background(Color(0xFFFF5722), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    favorito.numeroLinha,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(Modifier.width(16.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    "${favorito.numeroLinha} - ${favorito.nomeLinha}",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Spacer(Modifier.height(4.dp))
                Text(favorito.nomeEstacao, color = Color.Gray, fontSize = 14.sp)
            }

            IconButton(onClick = { mostrarEditar = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFFFF9500))
            }
            IconButton(onClick = { mostrarExcluir = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color(0xFFFF5252))
            }
        }
    }

    if (mostrarEditar) {
        DialogoFavorito(
            titulo = "Editar Favorito",
            favoritoInicial = favorito,
            onDismiss = { mostrarEditar = false },
            onSalvar = { est, num, nom ->
                viewModel.editarFavorito(
                    favorito.copy(
                        nomeEstacao = est,
                        numeroLinha = num,
                        nomeLinha = nom
                    )
                )
                mostrarEditar = false
            }
        )
    }

    if (mostrarExcluir) {
        AlertDialog(
            onDismissRequest = { mostrarExcluir = false },
            title = { Text("Confirmar exclusão", fontWeight = FontWeight.Bold) },
            text = { Text("Deseja realmente excluir ${favorito.numeroLinha} - ${favorito.nomeLinha}?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.removerFavorito(favorito)
                        mostrarExcluir = false
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFFFF5252))
                ) {
                    Text("Excluir", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarExcluir = false }) {
                    Text("Cancelar")
                }
            },
            containerColor = Color(0xFF2D2D2D),
            titleContentColor = Color.White,
            textContentColor = Color.White
        )
    }
}


@Composable
private fun DialogoFavorito(
    titulo: String,
    favoritoInicial: FavoritosBanco? = null,
    onDismiss: () -> Unit,
    onSalvar: (String, String, String) -> Unit
) {
    var estacao by remember { mutableStateOf(favoritoInicial?.nomeEstacao ?: "") }
    var numero by remember { mutableStateOf(favoritoInicial?.numeroLinha ?: "") }
    var nome by remember { mutableStateOf(favoritoInicial?.nomeLinha ?: "") }
    val valido = estacao.isNotBlank() && numero.isNotBlank() && nome.isNotBlank()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF2D2D2D),
        title = { Text(titulo, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Spacer(Modifier.height(20.dp))
                CampoTexto("Nome da Estação", estacao) { estacao = it }
                Spacer(Modifier.height(12.dp))
                CampoTexto("Número da Linha", numero) { numero = it }
                Spacer(Modifier.height(12.dp))
                CampoTexto("Nome da Linha", nome) { nome = it }
            }
        },
        confirmButton = {
            Button(
                onClick = { if (valido) onSalvar(estacao, numero, nome) },
                colors = ButtonDefaults.buttonColors(Color(0xFFFF9500)),
                enabled = valido
            ) {
                Text("Salvar", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = Color.Gray)
            }
        }
    )
}

@Composable
private fun CampoTexto(label: String, valor: String, onChange: (String) -> Unit) {
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

@Composable
private fun HeaderEstacoes(texto: String, onMudou: (String) -> Unit) {
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
                        textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (texto.isEmpty()) {
                                Text("Para onde você quer ir?", color = Color.Gray, fontSize = 16.sp)
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
private fun AbasEstacoes(selecionada: String, onSelecionar: (String) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        AbaItem("Ao redor", "ao_redor", selecionada, onSelecionar)
        Spacer(Modifier.weight(1f))
        AbaItem("Favoritas", "favoritas", selecionada, onSelecionar)
    }
}

@Composable
private fun AbaItem(texto: String, id: String, selecionada: String, onSelecionar: (String) -> Unit) {
    val ativa = selecionada == id
    Column(
        modifier = Modifier
            .clickable { onSelecionar(id) }
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            texto,
            color = if (ativa) Color.White else Color.Gray,
            fontWeight = if (ativa) FontWeight.Bold else FontWeight.Normal,
            fontSize = 16.sp
        )
        Spacer(Modifier.height(8.dp))
        Box(
            Modifier
                .width(70.dp)
                .height(3.dp)
                .background(
                    if (ativa) Color(0xFFFF9500) else Color.Transparent,
                    RoundedCornerShape(2.dp)
                )
        )
    }
}

@Composable
private fun CardEstacaoMoovit(
    estacao: EstacaoCompleta,
    favoritos: List<FavoritosBanco>,
    viewModel: EstacoesViewModel
) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier
                    .size(40.dp)
                    .background(Color(0xFFFF5722), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White)
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(estacao.nome, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("${estacao.distancia} • ${estacao.tempoAPe}", color = Color.Gray, fontSize = 14.sp)
            }
        }
        estacao.linhas.forEach { linha ->
            val isFavorito = favoritos.any { it.nomeEstacao == estacao.nome && it.numeroLinha == linha.numero }
            CardLinhaMoovit(linha, estacao.nome, isFavorito, viewModel)
        }
        Spacer(Modifier.height(8.dp))
        HorizontalDivider(color = Color(0xFF2D2D2D))
    }
}

@Composable
private fun CardLinhaMoovit(
    linha: LinhaOnibus,
    nomeEstacao: String,
    isFavorito: Boolean,
    viewModel: EstacoesViewModel
) {
    val scope = rememberCoroutineScope()
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier
                .size(32.dp)
                .background(linha.cor, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.DirectionsBus,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(
                "${linha.numero} ${linha.nome}",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text("Horário agendado", color = Color.Gray, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(linha.tempoChegada, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(linha.proximoHorario, color = Color.Gray, fontSize = 12.sp)
        }
        IconButton(
            onClick = {
                scope.launch {
                    if (isFavorito) {
                        viewModel.removerFavoritoPorLinhaEEstacao(nomeEstacao, linha.numero)
                    } else {
                        viewModel.adicionarFavorito(nomeEstacao, linha.numero, linha.nome)
                    }
                }
            }
        ) {
            Icon(
                if (isFavorito) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = if (isFavorito) "Remover Favorito" else "Adicionar Favorito",
                tint = if (isFavorito) Color(0xFFFFD700) else Color.Gray
            )
        }
    }
}