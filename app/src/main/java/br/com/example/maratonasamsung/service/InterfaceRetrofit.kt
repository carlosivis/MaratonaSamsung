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

    @GET("/doenca")
    fun doencas(): Call<List<DoencasResponse>>

    @GET("/sintomas/{doenca}")
    fun sintomas(@Path("doenca") doenca: String): Call<Sintomas>

    @GET("/prevencaos/{doenca}")
    fun prevencaos(@Path("doenca") doenca: String): Call<Prevencoes>

    @GET("/transmicaos/{doenca}")
    fun transmicaos(@Path("doenca") doenca: String): Call<Transmicoes>

//    @GET("/jogador")
//    fun jogador(): Call<JogadorResponse>

    @POST("/jogador")
    fun jogadorNovo(@Body jogador: JogadorRequest): Call<JogadorResponse>
  
    @GET("/jogador")
    fun ranking(id_sessao: Int): Call<List<JogadorResponse>>

//    @PUT("/jogador")
//    fun jogadorUpdate(jogadorUpdate: JogadorUpdate): Call<JogadorResponse>
}