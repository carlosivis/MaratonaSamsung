package br.com.example.maratonasamsung.model.Requests

import br.com.example.maratonasamsung.model.Responses.Doenca

data class SessaoRequest(
    val status: Boolean,
    val id_sessao: Int,
    val nome_sala: String,
    val senha_sala: String,
    val doencas: List<Doenca>
)