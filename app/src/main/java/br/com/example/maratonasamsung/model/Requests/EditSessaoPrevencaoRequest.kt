package br.com.example.maratonasamsung.model.Requests

import br.com.example.maratonasamsung.model.Responses.DicaUnicaPrevencao

data class EditSessaoPrevencaoRequest(
    val id_sessao: Int,
    val rodada: Int,
    val doenca: String,
    val dicas: DicaUnicaPrevencao
)