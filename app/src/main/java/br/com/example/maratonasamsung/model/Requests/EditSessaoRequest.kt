package br.com.example.maratonasamsung.model.Requests

import br.com.example.maratonasamsung.model.Responses.Dicas

data class EditSessaoRequest(
    val id_sessao: Int,
    val rodada: Int,
    val doenca: String,
    val dicas: List<Dicas>
)