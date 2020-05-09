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
import br.com.example.maratonasamsung.model.Requests.EditSessaoPrevencaoRequest
import br.com.example.maratonasamsung.model.Requests.EditSessaoSintomaRequest
import br.com.example.maratonasamsung.model.Requests.EditSessaoTransmicaoRequest
import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Responses.*
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_adivinhador.*
import kotlinx.android.synthetic.main.fragment_room_diqueiro_dicas.*
import kotlinx.android.synthetic.main.fragment_room_diqueiro_dicas.recyclerRanking
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class RoomDiqueiroDicasFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    lateinit var spinnerAdapter: ArrayAdapter<String>
    val timerCronometro = Timer()
    val timerRanking = Timer()
    lateinit var sintomasGlobal: ArrayList<String>
    lateinit var prevencoesGlobal: ArrayList<String>
    lateinit var transmicoesGlobal: ArrayList<String>
    val  vencedor = Bundle()
    var rodada:Int = 0
    lateinit var list: RankingResponse

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_diqueiro_dicas, container, false)
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
                        diqueirotempoCronometro.stop()
                        timerCronometro.cancel()
                        timerCronometro.purge()
                        timerRanking.cancel()
                        timerRanking.purge()
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
        view.findViewById<Button>(R.id.diqueiroBtnDicas).setOnClickListener(this)

        val id_sessao = requireArguments().getInt("id_sessao")
        val jogador = requireArguments().getString("jogador_nome").toString()
        val doenca: String = requireArguments().getString("doenca").toString()
        val doencas = requireArguments().getStringArrayList("doencas")


        ranking(id_sessao)
        sintomasGlobal = sintomas(doenca)
        prevencoesGlobal = prevencoes(doenca)
        transmicoesGlobal = transmicoes(doenca)
        chronometro()

        if (rodada == 6){
            timerRanking.cancel()
            timerRanking.purge()
            jogadorEncerrar(id_sessao, jogador)
            Navigation.findNavController(view).navigate(R.id.action_roomDiqueiroDicasFragment_to_winnerFragment, vencedor)
        }

        timerCronometro.schedule(20000) {
            val parametros = Bundle()
            parametros.putInt("id_sessao", id_sessao)
            parametros.putString("nome", jogador)
            parametros.putStringArrayList("doencas", doencas)
            parametros.putString("diqueiro", list.darDica.nome)
            parametros.putString("jogador_nome", jogador)

            timerRanking.cancel()
            timerRanking.purge()

            Navigation.findNavController(view).navigate(R.id.action_roomDiqueiroDicasFragment_to_roomAdivinhadorFragment, parametros)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun chronometro(){
        diqueirotempoCronometro.isCountDown= true
        diqueirotempoCronometro.base = SystemClock.elapsedRealtime()+20000
        diqueirotempoCronometro.start()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.diqueiroBtnDicas -> {

                val rodada = requireArguments().getInt("rodada")

                val sintoma: String =
                    if (diqueiroSpinnerSintoma.visibility == View.VISIBLE) diqueiroSpinnerSintoma.selectedItem.toString()
                    else ""

                val prevencao: String =
                    if (diqueiroSpinnerPrevencao.visibility == View.VISIBLE) diqueiroSpinnerPrevencao.selectedItem.toString()
                    else ""

                val transmicao: String =
                    if (diqueiroSpinnerTransmicao.visibility == View.VISIBLE) diqueiroSpinnerTransmicao.selectedItem.toString()
                    else ""

                if(sintoma.isEmpty() && prevencao.isEmpty() && transmicao.isEmpty()) {
                    val texto = "Selecione uma dica"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }
                else if(sintoma.isNotEmpty() && prevencao.isEmpty() && transmicao.isEmpty()) {
                    editarSessaoSintoma(DicaUnicaSintoma(sintoma), rodada)
                    diqueiroSpinnerSintoma.setSelection(0)
                }
                else if(sintoma.isEmpty() && prevencao.isNotEmpty() && transmicao.isEmpty()) {
                    editarSessaoPrevencao(DicaUnicaPrevencao(prevencao), rodada)
                    diqueiroSpinnerSintoma.setSelection(0)
                }
                else if(sintoma.isEmpty() && prevencao.isEmpty() && transmicao.isNotEmpty()) {
                    editarSessaoTransmicao(DicaUnicaTransmicao(transmicao), rodada)
                    diqueiroSpinnerSintoma.setSelection(0)
                }
                else {
                    val texto = "Você pode enviar apenas uma dica por vez"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                    if (diqueiroSpinnerSintoma.visibility == View.VISIBLE) diqueiroSpinnerSintoma.setSelection(0)
                    if (diqueiroSpinnerPrevencao.visibility == View.VISIBLE) diqueiroSpinnerPrevencao.setSelection(0)
                    if (diqueiroSpinnerTransmicao.visibility == View.VISIBLE) diqueiroSpinnerTransmicao.setSelection(0)
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
                if (response.isSuccessful) {
                    list = response.body()!!

                    vencedor.putString("vencedor", response.body()!!.jogadores.first().nome)

                    recyclerRanking.apply {
                        layoutManager = LinearLayoutManager(activity)
                        adapter = RankingAdapter(response.body()!!)
                    }
                }
                else
                    Log.d("Erro do banco", response.message())
            }
        })
        timerRanking.schedule(2000) {
            ranking(id_sessao)
        }
    }

    fun populaSpinnerSintoma(sintomas: ArrayList<String>) {
        sintomas.toMutableList()
        context?.let {
            spinnerAdapter =
                ArrayAdapter(it, android.R.layout.simple_spinner_item, sintomas)
        }
        diqueiroSpinnerSintoma.adapter = spinnerAdapter
    }

    fun sintomas(doenca: String): ArrayList<String> {
        val sintomas: ArrayList<String> = arrayListOf("")

        Service.retrofit.sintomas(
            doenca = doenca
        ).enqueue(object : Callback<Sintomas>{
            override fun onFailure(call: Call<Sintomas>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<Sintomas>, response: Response<Sintomas>) {
                Log.d("Nice", response.toString())
                if (response.isSuccessful) {

                    val listaSintomas = response.body()

                    if (listaSintomas?.sintomas!!.isNotEmpty()) {
                        listaSintomas.sintomas.forEach { sintomas.add((it.nome)) }
                        populaSpinnerSintoma(sintomas)
                    } else {
                        diqueiroSpinnerSintoma.visibility = View.INVISIBLE
                        diqueiroTxtSintomas.visibility = View.INVISIBLE
                    }
                }
                else
                    Log.d("Erro do banco", response.message())
            }
        })
        return sintomas
    }

    fun populaSpinnerPrevencoes(prevencoes: ArrayList<String>) {
        prevencoes.toMutableList()
        context?.let {
            spinnerAdapter =
                ArrayAdapter(it, android.R.layout.simple_spinner_item, prevencoes)
        }
        diqueiroSpinnerPrevencao.adapter = spinnerAdapter
    }

    fun prevencoes(doenca: String): ArrayList<String> {
        val prevencoes: ArrayList<String> = arrayListOf("")

        Service.retrofit.prevencoes(
            doenca = doenca
        ).enqueue(object : Callback<Prevencoes>{
            override fun onFailure(call: Call<Prevencoes>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<Prevencoes>, response: Response<Prevencoes>) {
                Log.d("Nice", response.toString())
                if (response.isSuccessful) {

                    val listaPrevencao = response.body()

                    if (listaPrevencao?.prevencoes!!.isNotEmpty()) {
                        listaPrevencao.prevencoes.forEach { prevencoes.add((it.nome)) }

                        populaSpinnerPrevencoes(prevencoes)
                    } else {
                        diqueiroSpinnerPrevencao.visibility = View.INVISIBLE
                        diqueiroTxtPrevencoes.visibility = View.INVISIBLE
                    }
                }
                else
                    Log.d("Erro do banco", response.message())
            }
        })
        return prevencoes
    }

    fun populaSpinnerTransmicoes(transmicoes: ArrayList<String>) {
        transmicoes.toMutableList()
        context?.let {
            spinnerAdapter =
                ArrayAdapter(it, android.R.layout.simple_spinner_item, transmicoes)
        }
        diqueiroSpinnerTransmicao.adapter = spinnerAdapter
    }

    fun transmicoes(doenca: String): ArrayList<String> {
        val transmicoes: ArrayList<String> = arrayListOf("")

        Service.retrofit.transmicoes(
            doenca = doenca
        ).enqueue(object : Callback<Transmissoes>{
            override fun onFailure(call: Call<Transmissoes>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<Transmissoes>, response: Response<Transmissoes>) {
                Log.d("Nice", response.toString())

                val listaTransmicao = response.body()

                if (listaTransmicao?.transmicao!!.isNotEmpty()) {
                    listaTransmicao.transmicao.forEach { transmicoes.add((it.nome)) }

                    populaSpinnerTransmicoes(transmicoes)
                }
                else {
                    diqueiroSpinnerTransmicao.visibility = View.INVISIBLE
                    diqueiroTxtTransmissoes.visibility = View.INVISIBLE
                }
            }
        })
        return transmicoes
    }

    fun editarSessaoSintoma(dica: DicaUnicaSintoma, rodada: Int){
        val id_sessao = requireArguments().getInt("id_sessao")
        val doenca: String = requireArguments().getString("doenca").toString()
        Service.retrofit.editarSessaoSintoma(
            sessao = EditSessaoSintomaRequest(
                id_sessao = id_sessao,
                rodada = rodada,
                doenca = doenca,
                dicas = dica
            )
        ).enqueue(object : Callback<SessaoResponseEditing>{
            override fun onFailure(call: Call<SessaoResponseEditing>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseEditing>, response: Response<SessaoResponseEditing>) {
                Log.d("Nice", response.body().toString())
                if (response.isSuccessful) {

                    val sessao = response.body()

                    val sintomasSelecionados: ArrayList<String> = arrayListOf("")
                    sessao?.dicas!!.sintomas.forEach { sintomasSelecionados.add((it.nome)) }

                    if (sintomasSelecionados.isNotEmpty()) {
                        sintomasGlobal.removeAll(sintomasSelecionados)
                    }

                    sintomasGlobal.add(0, "")
                    populaSpinnerSintoma(sintomasGlobal)
                }
                else
                    Log.d("Erro do banco", response.message())
            }
        })
    }

    fun editarSessaoPrevencao(dica: DicaUnicaPrevencao, rodada: Int){
        val id_sessao = requireArguments().getInt("id_sessao")
        val doenca: String = requireArguments().getString("doenca").toString()
        Service.retrofit.editarSessaoPrevencao(
            sessao = EditSessaoPrevencaoRequest(
                id_sessao = id_sessao,
                rodada = rodada,
                doenca = doenca,
                dicas = dica
            )
        ).enqueue(object : Callback<SessaoResponseEditing>{
            override fun onFailure(call: Call<SessaoResponseEditing>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseEditing>, response: Response<SessaoResponseEditing>) {
                Log.d("Nice", response.body().toString())
                if (response.isSuccessful) {

                    val sessao = response.body()

                    val prevecoesSelecionados: ArrayList<String> = arrayListOf("")
                    sessao?.dicas!!.sintomas.forEach { prevecoesSelecionados.add((it.nome)) }

                    if (prevecoesSelecionados.isNotEmpty()) {
                        prevencoesGlobal.removeAll(prevecoesSelecionados)
                    }

                    prevencoesGlobal.add(0, "")
                    populaSpinnerSintoma(prevencoesGlobal)
                }
                else
                    Log.d("Erro do banco", response.message())
            }
        })
    }

    fun editarSessaoTransmicao(dica: DicaUnicaTransmicao, rodada: Int){
        val id_sessao = requireArguments().getInt("id_sessao")
        val doenca: String = requireArguments().getString("doenca").toString()
        Service.retrofit.editarSessaoTransmicao(
            sessao = EditSessaoTransmicaoRequest(
                id_sessao = id_sessao,
                rodada = rodada,
                doenca = doenca,
                dicas = dica
            )
        ).enqueue(object : Callback<SessaoResponseEditing>{
            override fun onFailure(call: Call<SessaoResponseEditing>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseEditing>, response: Response<SessaoResponseEditing>) {
                Log.d("Nice", response.body().toString())
                if (response.isSuccessful) {

                    val sessao = response.body()

                    val transmicoesSelecionados: ArrayList<String> = arrayListOf("")
                    sessao?.dicas!!.sintomas.forEach { transmicoesSelecionados.add((it.nome)) }

                    if (transmicoesSelecionados.isNotEmpty()) {
                        transmicoesGlobal.removeAll(transmicoesSelecionados)
                    }

                    transmicoesGlobal.add(0, "")
                    populaSpinnerSintoma(transmicoesGlobal)
                }
                else
                    Log.d("Erro do banco", response.message())
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
}