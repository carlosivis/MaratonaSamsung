package br.com.example.maratonasamsung.model.Requests

import br.com.example.maratonasamsung.model.Responses.DicaUnicaSintoma

data class EditSessaoSintomaRequest(
    val id_sessao: Int,
    val rodada: Int,
    val doenca: String,
    val dicas: DicaUnicaSintoma
)