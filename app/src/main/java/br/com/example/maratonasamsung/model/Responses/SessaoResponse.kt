package br.com.example.maratonasamsung.model.Responses

import java.util.*

data class SessaoResponse(
    val status: Boolean,
    val id_sessao: Int,
    val nome_sala: String,
    val senha_sala: String,
    val doencas: List<Objects>
)