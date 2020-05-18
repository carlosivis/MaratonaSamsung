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
import android.widget.ImageButton
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
import br.com.example.maratonasamsung.data.service.ErrorCases
import br.com.example.maratonasamsung.data.service.Service
import br.com.example.maratonasamsung.model.Requests.EditarSessaoRequest
import br.com.example.maratonasamsung.model.Responses.*
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
    val timerAguardando = Timer()
    val timerRanking = Timer()
    val timerDicas = Timer()
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
                        tempoCronometro.stop()
                        timerCronometro.cancel()
                        timerCronometro.purge()
                        timerRanking.cancel()
                        timerRanking.purge()
                        timerDicas.cancel()
                        timerDicas.purge()
                        timerAguardando.cancel()
                        timerAguardando.purge()
                        jogadorEncerrar(id_sessao, jogador)
                        navController!!.navigate(R.id.action_roomAdivinhadorFragment_to_expulsoSalaFragment)
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
        view.findViewById<ImageButton>(R.id.btn_back).setOnClickListener(this)

        val id_sessao = requireArguments().getInt("id_sessao")
        val doencas = requireArguments().getStringArrayList("doencas")
        val jogador = requireArguments().getString("jogador_nome").toString()

        adivinhadorProgressBar.visibility = View.INVISIBLE
        adivinhadorTxtAcertou.visibility = View.INVISIBLE
//        adivinhadorBtnAdivinhar.visibility = View.INVISIBLE
//        textResposta.visibility = View.INVISIBLE
//        spinnerResposta.visibility = View.INVISIBLE

        doencas!!.toArray()
        doencas.sort()
        context?.let {
            spinnerAdapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, doencas)
        }
        spinnerResposta.adapter = spinnerAdapter

        chronometro()
        ranking(id_sessao)
//        pegarRodadaDoenca(id_sessao)
        dicas(id_sessao)

        timerCronometro.schedule(45000) {
            editarRodada(id_sessao, doencaRodada)

            val parametros = Bundle()
            parametros.putInt("id_sessao", id_sessao)
            parametros.putString("jogador_nome", jogador)
            parametros.putStringArrayList("doencas",doencas!!)
            parametros.putString("ultimaDoenca", doencaRodada)

            jogadorUpdate(id_sessao,true)

            tempoCronometro.stop()
            timerRanking.cancel()
            timerRanking.purge()
            timerDicas.cancel()
            timerDicas.purge()

            if (rodada == 5){
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
            R.id.btn_back -> activity?.onBackPressed()
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
                        adivinhadorBtnAdivinhar.setText("")
                        adivinhadorProgressBar.visibility = View.VISIBLE;
                        clicavel = false

                        val id_sessao = requireArguments().getInt("id_sessao")
                        listarSessao(id_sessao)
                    }
                }
            }
        }
    }

//    fun pegarRodadaDoenca(id_sessao: Int) {
//        Service.retrofit.listarSessao(
//            id_sessao = id_sessao
//        ).enqueue(object : Callback<SessaoResponseListing> {
//            override fun onFailure(call: Call<SessaoResponseListing>, t: Throwable) {
//                Log.d("Ruim: PegarRD", t.toString())
//            }
//            override fun onResponse(call: Call<SessaoResponseListing>, response: Response<SessaoResponseListing>) {
//                Log.d("Bom: PegarRD", response.toString())
//
//                if (response.isSuccessful) {
//                    val resposta = response.body()!!
//                    rodada = resposta.sessao.rodada + 1
//                    doencaRodada = resposta.ultimaDoenca
//
//                    Log.d("ultimaDoenca", doencaRodada)
//                }
//                else {
//                    Log.d("Erro banco: PegarRD", response.message())
//                    context?.let { ErrorCases().error(it)}
//                }
//            }
//        })
//    }

    private fun configureRecyclerViewRanking(list: RankingResponse) {
        val rankingAdapter: RankingAdapter = RankingAdapter(list)
        recyclerRanking.apply {
            layoutManager = LinearLayoutManager(context)
            isComputingLayout
            adapter = rankingAdapter
            onPause()
            onCancelPendingInputEvents()
        }
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

                            val parametros = Bundle()
                            parametros.putInt("id_sessao", id_sessao)
                            parametros.putString("jogador_nome", jogador)

                            tempoCronometro.stop()
                            timerCronometro.cancel()
                            timerCronometro.purge()
                            timerRanking.cancel()
                            timerRanking.purge()
                            timerDicas.cancel()
                            timerDicas.purge()

                            jogadorEncerrar(id_sessao, jogador)
                            navController!!.navigate(R.id.action_roomAdivinhadorFragment_to_expulsoSalaFragment, parametros)
                        }
                    }
                }
                else {
                    Log.d("Erro banco: Ranking", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
        timerRanking.schedule(30000) {
            ranking(id_sessao)
        }
    }

    private fun configureRecyclerViewDicas(list: ArrayList<String>) {
        val dicasAdapter: DicasAdapter = DicasAdapter(list)
        recyclerDicas.apply {
            layoutManager = LinearLayoutManager(activity)
            isComputingLayout
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

                    var listDicas: ArrayList<String> = arrayListOf("")

                    if(resposta.dicas.sintomas.isNotEmpty())
                        resposta.dicas.sintomas.forEach { listDicas.add(it.nome) }
                    if(resposta.dicas.prevencao.isNotEmpty())
                        resposta.dicas.prevencao.forEach { listDicas.add(it.nome) }
                    if(resposta.dicas.transmicao.isNotEmpty())
                        resposta.dicas.transmicao.forEach { listDicas.add(it.nome) }

                    listDicas.removeAt(0)
                    configureRecyclerViewDicas(listDicas)

                    rodada = resposta.sessao.rodada + 1
                    doencaRodada = resposta.ultimaDoenca
                }
                else {
                    Log.d("Erro banco: Dicas", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
        timerDicas.schedule(5000){
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
                        jogadorUpdate(rodada, false)
                        ranking(id_sessao)
                        spinnerResposta.setSelection(0)
                        val texto = "Parabéns, você acertou!"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()

                        textResposta.visibility = View.INVISIBLE
                        spinnerResposta.visibility = View.INVISIBLE
                        adivinhadorBtnAdivinhar.visibility = View.INVISIBLE
                        adivinhadorProgressBar.visibility = View.INVISIBLE
                        adivinhadorTxtAcertou.visibility = View.VISIBLE
                    }
                    else {
                        spinnerResposta.setSelection(0)
                        val texto = "Resposta incorreta, tente novamente"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()

                        clicavel = true
                        adivinhadorBtnAdivinhar.setText(R.string.btn_adivinhar)
                        adivinhadorProgressBar.visibility = View.INVISIBLE
                    }
                }
                else {
                    Log.d("Erro banco: ListSessao", response.message())
                    context?.let { ErrorCases().error(it)}

                    clicavel = true
                    adivinhadorBtnAdivinhar.setText(R.string.btn_adivinhar)
                    adivinhadorProgressBar.visibility = View.INVISIBLE
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

                }
            }
        })
    }

    fun editarRodada(id_sessao: Int, doenca: String){
        Service.retrofit.editarRodada(
            sessao = EditarSessaoRequest(
                id_sessao = id_sessao,
                rodada = rodada,
                doenca = doenca
            )
        ).enqueue(object : Callback<SessaoResponseEditing>{
            override fun onFailure(call: Call<SessaoResponseEditing>, t: Throwable) {
                Log.d("Ruim: Editar Rodada", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseEditing>, response: Response<SessaoResponseEditing>) {
                Log.d("Bom: Editar Rodada", response.body().toString())

                if (response.code() == 500) {
                    Log.d("Erro banco: EditarRodad", response.message())
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
        ).enqueue(object : Callback<StatusBoolean> {
            override fun onFailure(call: Call<StatusBoolean>, t: Throwable) {
                Log.d("Ruim: Jogador Encerrar", t.toString())
            }

            override fun onResponse(call: Call<StatusBoolean>, response: Response<StatusBoolean>) {
                Log.d("Bom: Jogador Encerrar", response.body().toString())

                if (response.code() == 500) {
                    Log.d("Erro banco: JogUpdate", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
    }
}