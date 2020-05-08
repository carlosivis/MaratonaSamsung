package br.com.example.appacessibilidade


import br.com.example.maratonasamsung.model.Requests.*
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
    fun cadastrarSessao(@Body sala: SalaRequest): Call<SalaResponse>

    @GET("/sessao/{id_sessao}")
    fun listarSessao(@Path ("id_sessao") id_sessao: Int): Call<SessaoResponseListing>

    @PUT("/sessao")
    fun editarSessaoSintoma(@Body sessao: EditSessaoSintomaRequest): Call<SessaoResponseEditing>

    @PUT("/sessao")
    fun editarSessaoPrevencao(@Body sessao: EditSessaoPrevencaoRequest): Call<SessaoResponseEditing>

    @PUT("/sessao")
    fun editarSessaoTransmicao(@Body sessao: EditSessaoTransmicaoRequest): Call<SessaoResponseEditing>

    @GET("/doenca")
    fun doencas(): Call<List<DoencasResponse>>

    @GET("/sintomas/{doenca}")
    fun sintomas(@Path("doenca") doenca: String): Call<Sintomas>

    @GET("/prevencaos/{doenca}")
    fun prevencoes(@Path("doenca") doenca: String): Call<Prevencoes>

    @GET("/transmicaos/{doenca}")
    fun transmicoes(@Path("doenca") doenca: String): Call<Transmissoes>

    @POST("/jogador")
    fun jogadorNovo(@Body jogador: JogadorRequest): Call<JogadorResponse>

    @PUT("/jogador")
    fun jogadorUpdate(@Body jogadorUpdate: JogadorUpdate): Call<JogadorResponse>

    @DELETE("/jogador/encerra")
    fun jogadorEncerrar(@Query("jogador") jogador: JogadorRequest)
}