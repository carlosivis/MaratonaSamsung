package br.com.example.maratonasamsung.model.Responses

data class Sala(
    val id: Int,
    val nome: String,
    val publica: Boolean,
    val senha: String
)