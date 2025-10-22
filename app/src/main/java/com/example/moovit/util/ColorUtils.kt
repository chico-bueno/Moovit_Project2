package com.example.moovit.util

import androidx.compose.ui.graphics.Color

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
        else -> Color(0xFFFF9500)
    }
}