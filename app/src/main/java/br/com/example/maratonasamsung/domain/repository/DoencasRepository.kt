package br.com.example.maratonasamsung.domain.repository
import br.com.example.maratonasamsung.model.Responses.*


interface DoencasRepository {
    suspend fun doencas(): List<DoencasResponse>
}