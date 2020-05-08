package br.com.example.maratonasamsung.model.Responses

import java.io.Serializable

data class DoencasResponse(
    var agente: String,
    val id: Int,
    var nome: String,
    val prevencao: List<Prevencao>,
    val sintomas: List<Sintoma>,
    val tipo: String,
    val transmicao: List<Transmicao>
):Serializable