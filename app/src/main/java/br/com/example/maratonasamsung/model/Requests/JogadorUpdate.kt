package br.com.example.maratonasamsung.model.Requests

data class JogadorUpdate(
    val id_sessao: Int,
    val nome: String,
    val rodada: Int,
    val fim: Boolean
)