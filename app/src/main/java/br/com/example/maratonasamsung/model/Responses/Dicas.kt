package br.com.example.maratonasamsung.model.Responses

data class Dicas (
    val sintomas: List<Sintoma>,
    val prevencoes: List<Prevencao>,
    val transmicoes: List<Transmicao>
)