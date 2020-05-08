package br.com.example.maratonasamsung.model.Responses

data class SalaResponse(
    val status: Boolean,
    val nome: String,
    val senha: String,
    val id_sessao: Int,
    val doencas: List<Doenca>
)