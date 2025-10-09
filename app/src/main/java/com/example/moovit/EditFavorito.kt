package com.example.moovit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EditFavoritoDialog(
    linha: LinhaTransporteBanco,
    aoFechar: () -> Unit,
    aoSalvar: (LinhaTransporteBanco) -> Unit
) {
    var nomeLinha by remember { mutableStateOf(linha.nome) }
    var numeroLinha by remember { mutableStateOf(linha.numero.toString()) }
    var tipoLinha by remember { mutableStateOf(linha.tipo) }

    AlertDialog(
        onDismissRequest = aoFechar,
        confirmButton = {
            TextButton(onClick = {
                val numeroConvertido = numeroLinha.toIntOrNull() ?: linha.numero
                aoSalvar(linha.copy(nome = nomeLinha, numero = numeroConvertido, tipo = tipoLinha))
            }) { Text("Salvar") }
        },
        dismissButton = { TextButton(onClick = aoFechar) { Text("Cancelar") } },
        title = { Text("Editar favorito") },
        text = {
            Column {
                TextField(value = nomeLinha, onValueChange = { nomeLinha = it }, label = { Text("Nome da linha") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = numeroLinha, onValueChange = { numeroLinha = it }, label = { Text("Número da linha") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = tipoLinha, onValueChange = { tipoLinha = it }, label = { Text("Tipo de transporte") })
            }
        }
    )
}
