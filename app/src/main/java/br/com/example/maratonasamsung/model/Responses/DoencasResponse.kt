package br.com.example.maratonasamsung.model.Responses

import br.com.example.maratonasamsung.domain.model.DoencasResponse
import java.io.Serializable

data class DoencasResponse(
    var agente: String,
    val id: Int,
    var nome: String,
    val prevencao: List<Prevencao>,
    val sintomas: List<Sintoma>,
    var tipo: String,
    val transmicao: List<Transmicao>
) : Serializable

fun DoencasResponse.toModel() = DoencasResponse(this.agente,this.id,this.nome,this.prevencao,
        this.sintomas,this.tipo,this.transmicao)

