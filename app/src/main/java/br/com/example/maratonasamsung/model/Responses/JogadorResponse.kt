package br.com.example.maratonasamsung.model.Responses

data class JogadorResponse(
    val status: Boolean,
    val nome: String,
    val id_sessao: Int,
    val id: Int,
    val pontuacao: Float,
    val adivinhador: Boolean
)