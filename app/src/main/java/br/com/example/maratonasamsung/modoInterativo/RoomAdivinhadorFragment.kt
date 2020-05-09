package br.com.example.maratonasamsung.modoInterativo

import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_adivinhador.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class RoomAdivinhadorFragment :  Fragment(), View.OnClickListener{

    var navController: NavController? = null
    lateinit var spinnerAdapter: ArrayAdapter<String>
    val timerCronometro = Timer()
    val timerRanking = Timer()
    val timerDicas = Timer()
    lateinit var list: RankingResponse
    lateinit var rankingAdapter: RankingAdapter
    lateinit var listDicas: ArrayList<String>
    lateinit var dicasAdapter: DicasAdapter

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
                        navController!!.navigate(R.id.mainFragment)
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

        val id_sessao = requireArguments().getInt("id_sessao")
        val doencas = requireArguments().getStringArrayList("doencas")
        val jogador = requireArguments().getString("jogador_nome").toString()

        doencas!!.toMutableList()
        context?.let {
            spinnerAdapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, doencas)
        }
        spinnerResposta.adapter = spinnerAdapter

        ranking(id_sessao)
        dicas(id_sessao)
        chronometro()

        timerCronometro.schedule(10000) {
            val parametro = Bundle()
            parametro.putInt("id_sessao", id_sessao)
            parametro.putString("diqueiro", list.darDica.nome)
            parametro.putString("jogador_nome", jogador)
            parametro.putStringArrayList("doencas",doencas!!)

            timerRanking.cancel()
            timerRanking.purge()
            timerDicas.cancel()
            timerDicas.purge()
            listDicas.clear()
            Navigation.findNavController(view).navigate(R.id.action_roomAdivinhadorFragment_to_placeholderRodadaFragment, parametro)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun chronometro(){
        tempoCronometro.isCountDown= true
        tempoCronometro.base = SystemClock.elapsedRealtime()+10000
        tempoCronometro.start()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.adivinhadorBtnAdivinhar -> {
                val resposta = spinnerResposta.selectedItem.toString()

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
        }
    }

    fun ranking(id_sessao: Int){
        Service.retrofit.ranking(
            id_sessao = id_sessao
        ).enqueue(object : Callback<RankingResponse> {
            override fun onFailure(call: Call<RankingResponse>, t: Throwable) {
                Log.d("Falha ao gerar ranking", t.toString())
            }

            override fun onResponse(call: Call<RankingResponse>, response: Response<RankingResponse>) {
                Log.d("Ranking com Sucesso", response.body().toString())
                list= response.body()!!
                configureRecyclerViewRanking(list)
            }
        })
        timerRanking.schedule(2000) {
            ranking(id_sessao)
        }
    }

    fun dicas(id_sessao: Int){
        Service.retrofit.listarSessao(
            id_sessao = id_sessao
        ).enqueue(object :Callback<SessaoResponseListing>{
                override fun onFailure(call: Call<SessaoResponseListing>, t: Throwable) {
                    Log.d("Falha ao pegar dicas", t.toString())
                }

                override fun onResponse(call: Call<SessaoResponseListing>, response: Response<SessaoResponseListing>) {
                    Log.d("Sucesso ao pegar dicas", response.body().toString())
                    listDicas= arrayListOf("")
                    response.body()?.dicas?.transmicoes?.forEach { listDicas.add(it.nome) }
                    response.body()?.dicas?.prevencoes?.forEach { listDicas.add(it.nome) }
                    response.body()?.dicas?.sintomas?.forEach { listDicas.add(it.nome) }
                    configureRecyclerViewDicas(listDicas)
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
                Log.d("Deu ruim", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseListing>, response: Response<SessaoResponseListing>) {
                Log.d("Nice", response.toString())

                val sessao = response.body()

                val rodada = sessao?.sessao!!.rodada

                val doencasSelecionadas: ArrayList<String> = arrayListOf("")
                sessao.doencasSelecionadas.forEach { doencasSelecionadas.add((it.nome)) }

                lateinit var doenca: String
                doenca =
                    if(doencasSelecionadas.isNotEmpty()) doencasSelecionadas.get(doencasSelecionadas.lastIndex)
                    else ""

                val resposta = spinnerResposta.selectedItem.toString()

                if (resposta == doenca) {
                    jogadorUpdate(rodada)
                }
                else {
                    val texto = "Resposta incorreta"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }
            }
        })
    }

    fun jogadorUpdate(rodada: Int) {
        Service.retrofit.jogadorUpdate(
            jogadorUpdate = JogadorUpdate(
                id_sessao = requireArguments().getInt("id_sessao"),
                nome = requireArguments().getString("jogador_nome").toString(),
                rodada = rodada
            )
        ).enqueue(object : Callback<JogadorResponse> {
            override fun onFailure(call: Call<JogadorResponse>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<JogadorResponse>, response: Response<JogadorResponse>) {
                Log.d("Nice", response.toString())
                val jogador = response.body()
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
                Log.d("Falha ao encerrar", t.toString())
            }

            override fun onResponse(call: Call<JogadorEncerra>, response: Response<JogadorEncerra>) {
                Log.d("Sucesso ao encerrar", response.body().toString())
            }
        })
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
    private fun configureRecyclerViewDicas(list: ArrayList<String>) {
        dicasAdapter = DicasAdapter(list)
        recyclerDicas.apply {
            layoutManager = LinearLayoutManager(context)
            adapter= dicasAdapter
            onCancelPendingInputEvents()
            onPause()
        }
    }

}




