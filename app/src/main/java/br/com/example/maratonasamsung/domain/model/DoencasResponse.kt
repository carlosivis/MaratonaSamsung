package br.com.example.maratonasamsung.domain.model

import br.com.example.maratonasamsung.model.Responses.Prevencao
import br.com.example.maratonasamsung.model.Responses.Sintoma
import br.com.example.maratonasamsung.model.Responses.Transmicao
import java.io.Serializable

data class DoencasResponse(
    var agente: String,
    val id: Int,
    var nome: String,
    val prevencao: List<Prevencao>,
    val sintomas: List<Sintoma>,
    var tipo: String,
    val transmicao: List<Transmicao>
)
