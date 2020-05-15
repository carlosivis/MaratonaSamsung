package br.com.example.maratonasamsung.domain.repository

import br.com.example.maratonasamsung.domain.model.DoencasResponse

interface DoencasRepository {
    suspend fun doencas(): List<br.com.example.maratonasamsung.model.Responses.DoencasResponse>
}