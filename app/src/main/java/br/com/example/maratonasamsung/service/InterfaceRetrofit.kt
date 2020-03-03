package br.com.example.appacessibilidade


import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface InterfaceRetrofit {

//    @GET("/sessao/{id}")
//    fun sessao(): Call<SessaoResponse>

    @GET("/doenca")
    fun doencas():Call<DoencasResponse>
}