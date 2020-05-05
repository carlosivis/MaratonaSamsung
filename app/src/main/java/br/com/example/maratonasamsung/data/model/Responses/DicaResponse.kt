package br.com.example.maratonasamsung.data.model.Responses

data class DicaResponse(
    val prevencoes: List<Prevencao>,
    val sintomas: List<Sintoma>,
    val transmicoes: List<Transmicao>
)