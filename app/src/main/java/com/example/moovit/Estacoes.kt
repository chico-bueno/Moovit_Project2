package com.example.moovit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

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
    val numeroLinha: String,
    val nomeLinha: String,
    val tempoChegada: String,
    val proximoHorario: String,
    val corLinha: Color = Color(0xFFFF5722)
)

data class EstacaoCompleta(
    val nomeEstacao: String,
    val distanciaEstacao: String,
    val tempoAPe: String,
    val linhasEstacao: List<LinhaOnibus>
)
@Composable
fun TelaEstacoes(navController: NavHostController) {
    val estacoes = listOf(
        EstacaoCompleta(
            "Praça Rui Barbosa", "1 min a pé", "1 min",
            listOf(
                LinhaOnibus("561", "GUILHERMINA", "Já", "37, 12:07"),
                LinhaOnibus("665", "VILA REX", "2 min", "42, 12:16"),
                LinhaOnibus("663", "VILA CUBAS", "2 min", "10, 19 min"),
                LinhaOnibus("603", "PINHEIRINHO / RUI BARBOSA", "2 min", "10, 19 min")
            )
        ),
        EstacaoCompleta(
            "Praça Rui Barbosa (360)", "2 min a pé", "2 min",
            listOf(LinhaOnibus("360", "NOVENA", "3 min", "15, 12:25"))
        ),
        EstacaoCompleta(
            "Terminal Guadalupe", "5 min a pé", "5 min",
            listOf(
                LinhaOnibus("380", "DETRAN", "8 min", "20, 12:30"),
                LinhaOnibus("140", "VILA ESPERANÇA", "12 min", "25, 12:35")
            )
        )
    )

    var textoPesquisa by remember { mutableStateOf("") }
    val estacoesFiltradas = estacoes.filter {
        it.nomeEstacao.contains(textoPesquisa, ignoreCase = true)
    }

    val favoritosViewModel: FavoritosViewModel = viewModel()
    val favoritosState by favoritosViewModel.listaFavoritos.collectAsState()

    var mostrarFavoritas by remember { mutableStateOf(false) }
    var editarLinha by remember { mutableStateOf<LinhaTransporteBanco?>(null) }
    var mostrarDialogEditar by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().background(Color.Black)) {
        HeaderEstacoes(textoPesquisa) { textoPesquisa = it }
        AbasEstacoes(onFavoritasClick = { mostrarFavoritas = !mostrarFavoritas })

        if (mostrarFavoritas) {
            LazyColumn(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(favoritosState) { linha ->
                    Card(
                        colors = CardDefaults.cardColors(Color(0xFF1C1C1C)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(linha.nome, color = Color.White, fontWeight = FontWeight.Bold)
                                Text("Nº ${linha.numero} • ${linha.tipo}", color = Color.Gray)
                            }

                            IconButton(onClick = { editarLinha = linha; mostrarDialogEditar = true }) {
                                Icon(imageVector = Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFFFF9500))
                            }
                            IconButton(onClick = { favoritosViewModel.removerFavorito(linha) }) {
                                Icon(imageVector = Icons.Default.Delete, contentDescription = "Deletar", tint = Color.Red)
                            }
                        }
                    }
                }
            }
        } else {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                items(estacoesFiltradas) { estacao ->
                    CardEstacaoMoovit(estacao, onFavoritar = { linha ->
                        val nova = LinhaTransporteBanco(nome = "${linha.numeroLinha} ${linha.nomeLinha}", numero = linha.numeroLinha.toIntOrNull() ?: 0, tipo = "Ônibus", cor = "#FF5722")
                        val existe = favoritosState.any { it.numero == nova.numero }
                        if (!existe) favoritosViewModel.adicionarFavorito(nova)
                    })
                }
            }
        }
    }

    if (mostrarDialogEditar && editarLinha != null) {
        EditFavoritoDialog(linha = editarLinha!!, aoFechar = { mostrarDialogEditar = false; editarLinha = null }) { atualizado ->
            favoritosViewModel.editarFavorito(atualizado)
            mostrarDialogEditar = false
            editarLinha = null
        }
    }
}

@Composable
fun HeaderEstacoes(textoPesquisa: String, onMudarTexto: (String) -> Unit) {
    Surface(color = Color(0xFF2D2D2D), modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("☰", color = Color.White, fontSize = 18.sp)

            Spacer(Modifier.width(16.dp))

            Card(
                colors = CardDefaults.cardColors(Color(0xFF424242)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(modifier = Modifier.padding(12.dp)) {
                    BasicTextField(
                        value = textoPesquisa,
                        onValueChange = onMudarTexto,
                        singleLine = true,
                        textStyle = LocalTextStyle.current.copy(color = Color.White, fontSize = 16.sp),
                        modifier = Modifier.fillMaxWidth(),
                        decorationBox = { innerTextField ->
                            if (textoPesquisa.isEmpty()) {
                                Text("Para onde você quer ir?", color = Color.Gray, fontSize = 16.sp)
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
fun AbasEstacoes(onFavoritasClick: () -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text("Ao redor", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        Box(Modifier.width(60.dp).height(2.dp).background(Color(0xFFFF9500)))
        Spacer(Modifier.weight(1f))
        Text(
            "Favoritas",
            color = Color.Gray,
            fontSize = 16.sp,
            modifier = Modifier
                .clickable { onFavoritasClick() }
                .padding(4.dp)
        )
    }
}

@Composable
fun CardLinhaMoovit(linha: LinhaOnibus, onFavoritar: (LinhaOnibus) -> Unit = {}) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(32.dp).background(linha.corLinha, RoundedCornerShape(4.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Search, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
        }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text("${linha.numeroLinha} ${linha.nomeLinha}", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text("Horário agendado • Apenas embarque", color = Color.Gray, fontSize = 12.sp)
        }

        Column(horizontalAlignment = Alignment.End) {
            Text("⏰ ${linha.tempoChegada}", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(linha.proximoHorario, color = Color.Gray, fontSize = 12.sp)
        }
        IconButton(onClick = { onFavoritar(linha) }) {
            Icon(imageVector = Icons.Default.Star, contentDescription = "Favoritar", tint = Color(0xFFFFD700))
        }
    }
}

@Composable
fun CardEstacaoMoovit(estacao: EstacaoCompleta, onFavoritar: (LinhaOnibus) -> Unit = {}) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                Modifier.size(40.dp).background(Color(0xFFFF5722), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Search, contentDescription = null, tint = Color.White)
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Text(estacao.nomeEstacao, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Text("${estacao.distanciaEstacao} • ${estacao.tempoAPe}", color = Color.Gray, fontSize = 14.sp)
            }
        }

        estacao.linhasEstacao.forEach { linha ->
            CardLinhaMoovit(linha, onFavoritar)
        }

        if (estacao.linhasEstacao.size > 2) {
            Text(
                "Visualizar todas as linhas desta estação",
                color = Color(0xFFFF5722),
                fontSize = 14.sp,
                modifier = Modifier.padding(8.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
    }
}
