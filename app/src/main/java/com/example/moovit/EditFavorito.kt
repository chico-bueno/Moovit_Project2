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
fun EditFavorito(
    linha: LinhaTransporteBanco,
    onDismiss: () -> Unit,
    onSave: (LinhaTransporteBanco) -> Unit
) {
    var nome by remember { mutableStateOf(linha.nome) }
    var numeroStr by remember { mutableStateOf(linha.numero.toString()) }
    var tipo by remember { mutableStateOf(linha.tipo) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                val numero = numeroStr.toIntOrNull() ?: linha.numero
                onSave(linha.copy(nome = nome, numero = numero, tipo = tipo))
            }) { Text("Salvar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } },
        title = { Text("Editar favorito") },
        text = {
            Column {
                TextField(value = nome, onValueChange = { nome = it }, label = { Text("Nome") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = numeroStr, onValueChange = { numeroStr = it }, label = { Text("Número") })
                Spacer(modifier = Modifier.height(8.dp))
                TextField(value = tipo, onValueChange = { tipo = it }, label = { Text("Tipo") })
            }
        }
    )
}
