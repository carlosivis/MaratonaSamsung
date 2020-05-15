package br.com.example.maratonasamsung.model.Responses

import br.com.example.maratonasamsung.model.Responses.Prevencao
import br.com.example.maratonasamsung.model.Responses.Sintoma
import br.com.example.maratonasamsung.model.Responses.Transmicao

data class DicaResponse(
    val prevencoes: List<Prevencao>,
    val sintomas: List<Sintoma>,
    val transmicoes: List<Transmicao>
)