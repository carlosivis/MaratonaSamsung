package br.com.example.appacessibilidade


import br.com.example.maratonasamsung.model.Requests.EditSessaoRequest
import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Requests.SalaRequest
import br.com.example.maratonasamsung.model.Requests.SessaoRequest
import br.com.example.maratonasamsung.model.Responses.*
import retrofit2.Call
import retrofit2.http.*


interface InterfaceRetrofit {

    @GET("/sala/{nome}")
    fun acessarSala(@Path("nome") nome: String): Call<SalaResponse>

    @GET("/ranking/{id_sessao}")
    fun ranking(@Path ("id_sessao") id_sessao: Int): Call<RankingResponse>

    @POST("/sala")
    fun criarSala(@Body sala: SalaRequest): Call<SalaResponse>

    @POST("/sessao")
    fun cadastrarSessao(@Body sala: SalaRequest): Call<SessaoRequest>

    @GET("/sessao/{id_sessao}")
    fun listarSessao(@Path ("id_sessao") id_sessao: Int): Call<SessaoResponseListing>

    @GET("/sessao/{id_sessao}")
    fun dicas(@Path ("id_sessao") id_sessao: Int): Call<SessaoResponse>
    @PUT("/sessao/{id_sessao}")
    fun editarSessao(@Body sessao: EditSessaoRequest): Call<SessaoResponseEditing>

    @GET("/doenca")
    fun doencas(): Call<List<DoencasResponse>>

    @GET("/sintomas/{doenca}")
    fun sintomas(@Path("doenca") doenca: String): Call<List<Sintoma>>

    @GET("/prevencaos/{doenca}")
    fun prevencoes(@Path("doenca") doenca: String): Call<List<Prevencao>>

    @GET("/transmicaos/{doenca}")
    fun transmicoes(@Path("doenca") doenca: String): Call<List<Transmicao>>

//    @GET("/jogador")
//    fun jogador(): Call<JogadorResponse>

    @POST("/jogador")
    fun jogadorNovo(@Body jogador: JogadorRequest): Call<JogadorResponse>

//    @PUT("/jogador")
//    fun jogadorUpdate(jogadorUpdate: JogadorUpdate): Call<JogadorResponse>
}