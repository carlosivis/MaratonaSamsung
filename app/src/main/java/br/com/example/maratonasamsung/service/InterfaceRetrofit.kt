package br.com.example.appacessibilidade


import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Requests.JogadorUpdate
import br.com.example.maratonasamsung.model.Requests.SalaResquest
import br.com.example.maratonasamsung.model.Responses.*
import retrofit2.Call
import retrofit2.http.*


interface InterfaceRetrofit {

    @GET("/sessao")
    fun sessao(): Call<SessaoResponse>


    @POST("/sala")
    fun criarSala(sala: SalaResquest):Call<SalaResponse>

    @GET("/sala/{sala}")
    fun listaSala(sala: SalaResquest):Call<SalaResponse>

    @GET("/doenca")
    fun doencas():Call<DoencasResponse>

    @GET("/sintoma")
    fun sintomas():Call<Sintoma>

    @GET("/prevencao")
    fun prevencao(): Call<Prevencao>

    @GET("/transmicao")
    fun transmicao(): Call<Transmicao>

    @GET("/jogador")
    fun jogador(): Call<JogadorResponse>

    @POST("/jogador")
    fun jogadorNovo(): Call<JogadorRequest>

    @PUT("/jogador")
    fun jogadorUpdate(jogadorUpdate: JogadorUpdate): Call<JogadorUpdate>
}