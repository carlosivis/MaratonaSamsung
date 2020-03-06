package br.com.example.appacessibilidade


import br.com.example.maratonasamsung.model.Requests.SalaResquest
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import br.com.example.maratonasamsung.model.Responses.SalaResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface InterfaceRetrofit {

//    @GET("/sessao/{id}")
//    fun sessao(): Call<SessaoResponse>


    @POST("/sala/{sala}")
    fun criarSala(sala: SalaResquest):Call<SalaResponse>

    @GET("/doenca")
    fun doencas():Call<DoencasResponse>
}