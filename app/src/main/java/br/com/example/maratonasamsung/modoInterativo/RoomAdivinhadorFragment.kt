package br.com.example.maratonasamsung.modoInterativo

import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Requests.JogadorUpdate
import br.com.example.maratonasamsung.model.Responses.JogadorEncerra
import br.com.example.maratonasamsung.model.Responses.JogadorResponse
import br.com.example.maratonasamsung.model.Responses.RankingResponse
import br.com.example.maratonasamsung.model.Responses.SessaoResponseListing
import br.com.example.maratonasamsung.service.ErrorCases
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_adivinhador.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class RoomAdivinhadorFragment :  Fragment(), View.OnClickListener {

    var navController: NavController? = null
    lateinit var spinnerAdapter: ArrayAdapter<String>
    val  vencedor = Bundle()
    val timerCronometro = Timer()
    val timerRanking = Timer()
    val timerDicas = Timer()
    lateinit var dicasAdapter: DicasAdapter
    lateinit var rankingAdapter: RankingAdapter
    var listDicas: ArrayList<String> = arrayListOf("")
    lateinit var doencaRodada: String
    var rodada: Int = 0
    var clicavel = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_adivinhador, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val id_sessao = requireArguments().getInt("id_sessao")
        val jogador = requireArguments().getString("jogador_nome").toString()

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.let {
                AlertDialog.Builder(it)
                    .setTitle(R.string.sairJogo)
                    .setMessage(R.string.sairJogoPont)
                    .setPositiveButton(R.string.sair) { dialog, which ->
                        navController!!.navigate(R.id.action_roomAdivinhadorFragment_to_mainFragment)
                        tempoCronometro.stop()
                        timerCronometro.cancel()
                        timerCronometro.purge()
                        timerRanking.cancel()
                        timerRanking.purge()
                        timerDicas.cancel()
                        timerDicas.purge()
                        jogadorEncerrar(id_sessao, jogador)
                    }
                    .setNegativeButton(R.string.cancelar) { dialog, which -> }
                    .show()
            }
        }
        callback
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.adivinhadorBtnAdivinhar).setOnClickListener(this)

        val id_sessao = requireArguments().getInt("id_sessao")
        val doencas = requireArguments().getStringArrayList("doencas")
        val jogador = requireArguments().getString("jogador_nome").toString()

        doencas!!.toMutableList()
        context?.let {
            spinnerAdapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, doencas)
        }
        spinnerResposta.adapter = spinnerAdapter

        chronometro()
        //Tentativa de fazer a rodada ficar certo (Fazer teste verificando se quando eu mando a rodada pro jogadorUpdate
        // tá certo, comparar com o diqueiro dicas, que usa rodada pra pegar os sintomas, talvez lá esteja errado tbm, mas
        // que eu saiba, não)
        pegarRodadaDoenca(id_sessao)
        dicas(id_sessao)
        ranking(id_sessao)

        timerCronometro.schedule(45000) {
            val parametros = Bundle()
            parametros.putInt("id_sessao", id_sessao)
            parametros.putString("jogador_nome", jogador)
            parametros.putStringArrayList("doencas",doencas!!)

            jogadorUpdate(id_sessao,true)
            tempoCronometro.stop()
            timerRanking.cancel()
            timerRanking.purge()
            timerDicas.cancel()
            timerDicas.purge()
            listDicas.clear()

            //Porque a rodada 0 está sendo jogada (confirmar)
            if (rodada == 4){
                jogadorEncerrar(id_sessao, jogador)
                navController!!.navigate(R.id.action_roomAdivinhadorFragment_to_winnerFragment, vencedor)
            }
            else
                navController!!.navigate(R.id.action_roomAdivinhadorFragment_to_placeholderRodadaFragment, parametros)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun chronometro(){
        tempoCronometro.isCountDown= true
        tempoCronometro.base = SystemClock.elapsedRealtime()+45000
        tempoCronometro.start()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.adivinhadorBtnAdivinhar -> {
                val resposta = spinnerResposta.selectedItem.toString()

                if(clicavel) {
                    if(resposta.isEmpty()) {
                        val texto = "Selecione uma doença como resposta"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                    }
                    else {
                        val id_sessao = requireArguments().getInt("id_sessao")
                        listarSessao(id_sessao)
                    }
                }
                else {
                    spinnerResposta.setSelection(0)
                    val texto = "Você já acertou a doença!"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }
            }
        }
    }

    private fun configureRecyclerViewRanking(list: RankingResponse) {
        rankingAdapter = RankingAdapter(list)
        recyclerRanking.apply {
            layoutManager = LinearLayoutManager(context)
            isComputingLayout
            adapter= rankingAdapter
            onPause()
            onCancelPendingInputEvents()
        }
    }

    fun pegarRodadaDoenca(id_sessao: Int) {
        Service.retrofit.listarSessao(
            id_sessao = id_sessao
        ).enqueue(object : Callback<SessaoResponseListing> {
            override fun onFailure(call: Call<SessaoResponseListing>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseListing>, response: Response<SessaoResponseListing>) {
                Log.d("Nice", response.toString())

                if (response.isSuccessful) {
                    val resposta = response.body()!!
                    rodada = resposta.sessao.rodada
                    doencaRodada = resposta.ultimaDoenca
                }
                else {
                    Log.d("Erro do banco", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
    }

    fun ranking(id_sessao: Int){
        Service.retrofit.ranking(
            id_sessao = id_sessao
        ).enqueue(object : Callback<RankingResponse> {
            override fun onFailure(call: Call<RankingResponse>, t: Throwable) {
                Log.d("Ruim: Ranking", t.toString())
            }
            override fun onResponse(call: Call<RankingResponse>, response: Response<RankingResponse>) {
                Log.d("Bom: Ranking", response.body().toString())

                if (response.isSuccessful) {
                    val ranking = response.body()!!

                    if (!ranking.status) {
                        val texto = "Erro ao atualizar ranking"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                    }
                    else {
                        vencedor.putString("vencedor", ranking.jogadores.first().nome)
                        configureRecyclerViewRanking(ranking)

                        val quantidadeJogadores: ArrayList<String> = arrayListOf("")
                        ranking.jogadores.forEach { quantidadeJogadores.add((it.nome)) }

                        quantidadeJogadores.removeAt(0)

                        if (quantidadeJogadores.size < 2) {
                            val jogador = requireArguments().getString("jogador_nome").toString()

                            tempoCronometro.stop()
                            timerCronometro.cancel()
                            timerCronometro.purge()
                            timerRanking.cancel()
                            timerRanking.purge()
                            timerDicas.cancel()
                            timerDicas.purge()
                            jogadorEncerrar(id_sessao, jogador)
                            navController!!.navigate(R.id.action_roomAdivinhadorFragment_to_mainFragment)
                        }
                    }
                }
                else {
                    Log.d("Erro banco: Ranking", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
        timerRanking.schedule(2000) {
            ranking(id_sessao)
        }
    }

    private fun configureRecyclerViewDicas(list: ArrayList<String>) {
        dicasAdapter = DicasAdapter(list)
        recyclerDicas.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter= dicasAdapter
            onCancelPendingInputEvents()
            onPause()
        }
    }

    fun dicas(id_sessao: Int){
        Service.retrofit.listarSessao(
            id_sessao = id_sessao
        ).enqueue(object :Callback<SessaoResponseListing>{
            override fun onFailure(call: Call<SessaoResponseListing>, t: Throwable) {
                Log.d("Ruim: Dicas", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseListing>, response: Response<SessaoResponseListing>) {
                Log.d("Bom: Dicas", response.body().toString())

                if (response.isSuccessful) {
                    val resposta = response.body()!!
                    listDicas = arrayListOf("")

                    if(resposta.dicas.sintomas.isNotEmpty())
                        resposta.dicas.sintomas.forEach { listDicas.add(it.nome) }
                    if(resposta.dicas.prevencao.isNotEmpty())
                        resposta.dicas.prevencao.forEach { listDicas.add(it.nome) }
                    if(resposta.dicas.transmicao.isNotEmpty())
                        resposta.dicas.transmicao.forEach { listDicas.add(it.nome) }

                    configureRecyclerViewDicas(listDicas)
                }
                else {
                    Log.d("Erro banco: Dicas", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
        timerDicas.schedule(2000){
            dicas(id_sessao)
        }
    }
      
    fun listarSessao(id_sessao: Int) {
        Service.retrofit.listarSessao(
            id_sessao = id_sessao
        ).enqueue(object : Callback<SessaoResponseListing> {
            override fun onFailure(call: Call<SessaoResponseListing>, t: Throwable) {
                Log.d("Ruim: Listar Sessao", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseListing>, response: Response<SessaoResponseListing>) {
                Log.d("Bom: Listar Sessao", response.toString())

                if (response.isSuccessful) {
                    val sessao = response.body()!!

                    val doencasSelecionadas: ArrayList<String> = arrayListOf("")
                    sessao.doencasSelecionadas.forEach { doencasSelecionadas.add((it.nome)) }
                    lateinit var doenca: String
                    doenca =
                        if (doencasSelecionadas.isNotEmpty()) doencasSelecionadas[doencasSelecionadas.lastIndex]
                        else ""

                    val resposta = spinnerResposta.selectedItem.toString()

                    if (resposta == doenca) {
                        clicavel = false
                        jogadorUpdate(rodada, false)
                        spinnerResposta.setSelection(0)
                        val texto = "Parabéns, você acertou!"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                    }
                    else {
                        spinnerResposta.setSelection(0)
                        val texto = "Resposta incorreta, tente novamente"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                    }
                }
                else {
                    Log.d("Erro banco: ListSessao", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
    }

    fun jogadorUpdate(rodada: Int, fim: Boolean) {
        Service.retrofit.jogadorUpdate(
            jogadorUpdate = JogadorUpdate(
                id_sessao = requireArguments().getInt("id_sessao"),
                nome = requireArguments().getString("jogador_nome").toString(),
                rodada = rodada,
                fim = fim
            )
        ).enqueue(object : Callback<JogadorResponse> {
            override fun onFailure(call: Call<JogadorResponse>, t: Throwable) {
                Log.d("Ruim: Jogador Update", t.toString())
            }

            override fun onResponse(call: Call<JogadorResponse>, response: Response<JogadorResponse>) {
                Log.d("Bom: Jogador Update", response.toString())

                if (response.code() == 500) {
                    Log.d("Erro banco: JogUpdate", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
    }

    fun jogadorEncerrar(id_sessao: Int, jogador: String) {
        Service.retrofit.jogadorEncerrar(
            jogador = JogadorRequest(
                id_sessao = id_sessao,
                nome = jogador
            )
        ).enqueue(object : Callback<JogadorEncerra> {
            override fun onFailure(call: Call<JogadorEncerra>, t: Throwable) {
                Log.d("Ruim: Jogador Encerrar", t.toString())
            }

            override fun onResponse(call: Call<JogadorEncerra>, response: Response<JogadorEncerra>) {
                Log.d("Bom: Jogador Encerrar", response.body().toString())

                if (response.code() == 500) {
                    Log.d("Erro banco: JogUpdate", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
    }
}




