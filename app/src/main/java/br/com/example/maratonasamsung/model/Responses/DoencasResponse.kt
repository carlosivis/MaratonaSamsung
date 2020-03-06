package br.com.example.maratonasamsung.model.Responses

data class DoencasResponse(
    val agente: String,
    val id: Int,
    val nome: String,
    val prevencao: List<Prevencao>,
    val sintomas: List<Sintoma>,
    val tipo: String
)