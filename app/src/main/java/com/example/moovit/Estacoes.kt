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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
class Estacoes : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TelaEstacoes(navController = rememberNavController())
        }
    }
}
data class LinhaOnibus(
    val numero: String,
    val nome: String,
    val tempoChegada: String,
    val proximoHorario: String,
    val cor: Color = Color(0xFFFF5722)
)
data class EstacaoCompleta(
    val nome: String,
    val distancia: String,
    val tempoAPe: String,
    val linhas: List<LinhaOnibus>
)

@Composable
fun TelaEstacoes(
    navController: NavHostController,
    gerenciador: GerenciadorDeFavoritos = viewModel()
) {
    val estacoes = remember {
        listOf(
            EstacaoCompleta(
                "Praça Rui Barbosa", "1 min a pé", "1 min",
                listOf(
                    LinhaOnibus("561", "GUILHERMINA", "2 min", "37, 12:07"),
                    LinhaOnibus("665", "VILA REX", "2 min", "42, 12:16"),
                    LinhaOnibus("663", "VILA CUBAS", "2 min", "10, 19 min"),
                    LinhaOnibus("603", "PINHEIRINHO / RUI BARBOSA", "2 min", "10, 19 min")
                )
            ),
            EstacaoCompleta(
                "Praça Rui Barbosa (360)", "2 min a pé", "2 min",
                listOf(
                    LinhaOnibus("360", "NOVENA", "3 min", "15, 12:25")
                )
            ),
            EstacaoCompleta(
                "Terminal Guadalupe", "5 min a pé", "5 min",
                listOf(
                    LinhaOnibus("380", "DETRAN", "8 min", "20, 12:30"),
                    LinhaOnibus("140", "VILA ESPERANÇA", "12 min", "25, 12:35")
                )
            )
        )
    }

    // Estados locais da tela
    var textoPesquisa by remember { mutableStateOf("") }
    var abaSelecionada by remember { mutableStateOf("ao_redor") }
    var mostrarDialogoCriar by remember { mutableStateOf(false) }

    val favoritos by gerenciador.listaFavoritos.collectAsState()

    val estacoesFiltradas = estacoes.filter {
        it.nome.contains(textoPesquisa, ignoreCase = true)
    }
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        HeaderEstacoes(textoPesquisa) { textoPesquisa = it }
        AbasEstacoes(abaSelecionada) { abaSelecionada = it }
        if (abaSelecionada == "favoritas") {
            TelaFavoritos(favoritos, gerenciador) { mostrarDialogoCriar = true }
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(estacoesFiltradas) {
                    CardEstacaoMoovit(it, gerenciador)
                }
            }
        }
    }
    if (mostrarDialogoCriar) {
        DialogoFavorito(
            titulo = "Adicionar Favorito",
            onDismiss = { mostrarDialogoCriar = false },
            onSalvar = { est, num, nom ->
                gerenciador.adicionarFavorito(est, num, nom)
                mostrarDialogoCriar = false
            }
        )
    }
}
@Composable
fun TelaFavoritos(
    favoritos: List<FavoritosBanco>,
    gerenciador: GerenciadorDeFavoritos,
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
                items(favoritos) { favorito ->
                    CardFavorito(favorito, gerenciador)
                }
            }
        }
    }
}
@Composable
fun CardFavorito(favorito: FavoritosBanco, gerenciador: GerenciadorDeFavoritos) {
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

            // Botões de editar e excluir
            IconButton(onClick = { mostrarEditar = true }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFFFF9500))
            }
            IconButton(onClick = { mostrarExcluir = true }) {
                Icon(Icons.Default.Delete, contentDescription = "Excluir", tint = Color(0xFFFF5252))
            }
        }
    }

    // edição
    if (mostrarEditar) {
        DialogoFavorito(
            titulo = "Editar Favorito",
            favoritoInicial = favorito,
            onDismiss = { mostrarEditar = false },
            onSalvar = { est, num, nom ->
                gerenciador.editarFavorito(
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

    // confirmação de exclusão
    if (mostrarExcluir) {
        AlertDialog(
            onDismissRequest = { mostrarExcluir = false },
            title = { Text("Confirmar exclusão", fontWeight = FontWeight.Bold) },
            text = { Text("Deseja realmente excluir ${favorito.numeroLinha} - ${favorito.nomeLinha}?") },
            confirmButton = {
                Button(
                    onClick = {
                        gerenciador.removerFavorito(favorito)
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

/**
 usado para criar ou editar um favorito.
 */
@Composable
fun DialogoFavorito(
    titulo: String,
    favoritoInicial: FavoritosBanco? = null,
    onDismiss: () -> Unit,
    onSalvar: (String, String, String) -> Unit
) {
    var estacao by remember { mutableStateOf(favoritoInicial?.nomeEstacao ?: "") }
    var numero by remember { mutableStateOf(favoritoInicial?.numeroLinha ?: "") }
    var nome by remember { mutableStateOf(favoritoInicial?.nomeLinha ?: "") }
    val valido = estacao.isNotBlank() && numero.isNotBlank() && nome.isNotBlank()

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(Color(0xFF2D2D2D)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(Modifier.padding(24.dp)) {
                Text(titulo, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(20.dp))

                CampoTexto("Nome da Estação", estacao) { estacao = it }
                Spacer(Modifier.height(12.dp))
                CampoTexto("Número da Linha", numero) { numero = it }
                Spacer(Modifier.height(12.dp))
                CampoTexto("Nome da Linha", nome) { nome = it }

                Spacer(Modifier.height(24.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = Color.Gray)
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = { if (valido) onSalvar(estacao, numero, nome) },
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

/** Campo de texto padrão usado nos diálogos */
@Composable
fun CampoTexto(label: String, valor: String, onChange: (String) -> Unit) {
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

/** Cabeçalho com campo de pesquisa */
@Composable
fun HeaderEstacoes(texto: String, onMudou: (String) -> Unit) {
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
/** Barra com as abas "Ao redor" e "Favoritas" */
@Composable
fun AbasEstacoes(selecionada: String, onSelecionar: (String) -> Unit) {
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
/** Aba individual */
@Composable
fun AbaItem(texto: String, id: String, selecionada: String, onSelecionar: (String) -> Unit) {
    val ativa = selecionada == id
    Column(
        Modifier
            .clickable { onSelecionar(id) }
            .padding(horizontal = 24.dp)
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
fun CardEstacaoMoovit(estacao: EstacaoCompleta, gerenciador: GerenciadorDeFavoritos) {
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
        estacao.linhas.forEach {
            CardLinhaMoovit(it, estacao.nome, gerenciador)
        }
        if (estacao.linhas.size > 2) {
            Text(
                "Visualizar todas as linhas desta estação",
                color = Color(0xFFFF5722),
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable { }
            )
        }
        Spacer(Modifier.height(8.dp))
    }
}
@Composable
fun CardLinhaMoovit(
    linha: LinhaOnibus,
    nomeEstacao: String,
    gerenciador: GerenciadorDeFavoritos
) {
    val scope = rememberCoroutineScope()
    var isFavorito by remember { mutableStateOf(false) }

    // Verifica se a linha já é favorita
    LaunchedEffect(linha.numero, nomeEstacao) {
        isFavorito = gerenciador.verificarSeFavorito(nomeEstacao, linha.numero)
    }
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
            Text("Horário agendado para Apenas embarque", color = Color.Gray, fontSize = 12.sp)
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(linha.tempoChegada, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(linha.proximoHorario, color = Color.Gray, fontSize = 12.sp)
        }
        // Botão de favoritar/desfavoritar
        IconButton(
            onClick = {
                scope.launch {
                    if (isFavorito)
                        gerenciador.removerFavoritoPorLinhaEEstacao(nomeEstacao, linha.numero)
                    else
                        gerenciador.adicionarFavorito(nomeEstacao, linha.numero, linha.nome)
                    isFavorito = !isFavorito
                }
            }
        ) {
            Icon(
                if (isFavorito) Icons.Filled.Star else Icons.Outlined.Star,
                contentDescription = if (isFavorito) "Favorito" else "Não favorito",
                tint = if (isFavorito) Color(0xFFFFD700) else Color.Gray
            )
        }
    }
}
