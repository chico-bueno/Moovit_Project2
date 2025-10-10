package com.example.moovit

import android.util.Log

suspend fun buscarLinha(moovitDAO: MoovitDAO): List<LinhaTransporteBanco> {
    return try {
        moovitDAO.buscarTodos()
    } catch (e: Exception) {
        Log.e("Erro ao buscar", "${e.message}")
        emptyList()
    }
}

suspend fun inserirLinha(nome: String, numero: String, tipo: String, cor: String, moovitDAO: MoovitDAO) {
    try {
        moovitDAO.inserir(LinhaTransporteBanco(nome = nome, numero = numero, tipo = tipo, cor = cor))
    } catch (e: Exception) {
        Log.e("Erro ao adicionar", "Msg: ${e.message}")
    }
}

suspend fun deletarLinha(linhaTransporte: LinhaTransporteBanco, moovitDAO: MoovitDAO) {
    try {
        moovitDAO.deletar(linhaTransporte)
    } catch (e: Exception) {
        Log.e("Erro ao deletar", "Msg: ${e.message}")
    }
}

suspend fun atualizarLinha(linhaTransporte: LinhaTransporteBanco, moovitDAO: MoovitDAO) {
    try {
        moovitDAO.atualizar(linhaTransporte)
    } catch (e: Exception) {
        Log.e("Erro ao atualizar", "Msg: ${e.message}")
    }
}
