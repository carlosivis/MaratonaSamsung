package br.com.example.maratonasamsung.data.repository

import br.com.example.maratonasamsung.data.service.Service
import br.com.example.maratonasamsung.domain.repository.DoencasRepository
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import retrofit2.await

class DoencasRepositoryImpl(private val service: Service):DoencasRepository {

    override suspend fun doencas() = withContext(IO) {
        service.retrofit.doencas().await().map { it }
    }
}
