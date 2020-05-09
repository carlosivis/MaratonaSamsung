package br.com.example.maratonasamsung.model.Responses

data class Dicas (
    val sintomas: List<Sintoma>,
    val prevencao: List<Prevencao>,
    val transmicao: List<Transmicao>
)