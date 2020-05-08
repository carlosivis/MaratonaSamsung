package br.com.example.maratonasamsung.model.Requests

import br.com.example.maratonasamsung.model.Responses.DicaUnicaTransmicao

data class EditSessaoTransmicaoRequest(
    val id_sessao: Int,
    val rodada: Int,
    val doenca: String,
    val dicas: DicaUnicaTransmicao
)