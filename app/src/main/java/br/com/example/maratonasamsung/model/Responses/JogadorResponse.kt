package br.com.example.maratonasamsung.model.Responses

data class JogadorResponse(
    val status: String,
    val id_sessao: Int,
    val nome: String,
    val id: Int,
    val pontuacao: Float,
    val adivinhador: Boolean
)