package br.com.example.maratonasamsung.model.Responses

data class SalaResponse(
    val status: Boolean,
    val id: Int,
    val nome: String,
    val senha: String,
    val doencas: List<Doenca>
)