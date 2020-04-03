package br.com.example.appacessibilidade


import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Requests.SalaRequest
import br.com.example.maratonasamsung.model.Responses.*
import retrofit2.Call
import retrofit2.http.*


interface InterfaceRetrofit {

    @GET("/sala/{nome}")
    fun acessarSala(@Path("nome") nome: String): Call<SalaResponse>

    @POST("/sala")
    fun criarSala(@Body sala: SalaRequest): Call<SalaResponse>

    @POST("/sessao")
    fun sessao(@Body sala: SalaRequest): Call<SessaoResponse>

//    @GET("/sessao")
//    fun listarsessao(@Body id_sessao: Int): Call<SessaoResponse>

    @GET("/doenca")
    fun doencas(): Call<DoencasResponse>
//
//    @GET("/sintoma")
//    fun sintomas(): Call<Sintoma>
//
//    @GET("/prevencao")
//    fun prevencao(): Call<Prevencao>
//
//    @GET("/transmicao")
//    fun transmicao(): Call<Transmicao>
//
//    @GET("/jogador")
//    fun jogador(): Call<JogadorResponse>

    @POST("/jogador")
    fun jogadorNovo(@Body jogador: JogadorRequest): Call<JogadorResponse>
  
    @GET("/jogador")
    fun ranking(id_sessao: Int): Call<List<JogadorResponse>>

//    @PUT("/jogador")
//    fun jogadorUpdate(jogadorUpdate: JogadorUpdate): Call<JogadorResponse>
}