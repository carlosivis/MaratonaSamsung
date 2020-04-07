package br.com.example.appacessibilidade


import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Requests.RankingRequest
import br.com.example.maratonasamsung.model.Requests.SalaRequest
import br.com.example.maratonasamsung.model.Responses.*
import kotlinx.android.parcel.RawValue
import retrofit2.Call
import retrofit2.http.*


interface InterfaceRetrofit {

    @GET("/sala/{nome}")
    fun acessarSala(@Path("nome") nome: String): Call<SalaResponse>

    @GET("/doenca")
    fun doencas(): Call<List<DoencasResponse>>

    @GET("/jogador{id_sessao}")
    fun ranking(@Path("id_sessao") id_sessao:RankingRequest): Call<RankingResponse>


    @POST("/sessao")
    fun sessao(@Path sala: SalaRequest): Call<SessaoResponse>


    @POST("/sala")
    fun criarSala(@Body sala: SalaRequest): Call<SalaResponse>
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

//    @PUT("/jogador")
//    fun jogadorUpdate(jogadorUpdate: JogadorUpdate): Call<JogadorResponse>
}