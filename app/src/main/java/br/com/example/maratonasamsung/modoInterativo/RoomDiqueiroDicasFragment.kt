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
import br.com.example.maratonasamsung.model.Requests.*
import br.com.example.maratonasamsung.model.Responses.*
import br.com.example.maratonasamsung.data.service.ErrorCases
import br.com.example.maratonasamsung.data.service.Service
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
    val parametros = Bundle()
    val vencedor = Bundle()
    val timerCronometro = Timer()
    val timerRanking = Timer()
    var sintomasGlobal: ArrayList<String> = arrayListOf("")
    var prevencoesGlobal: ArrayList<String> = arrayListOf("")
    var transmicoesGlobal: ArrayList<String> = arrayListOf("")
    var clicavel = true

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
                        diqueirotempoCronometro.stop()
                        timerCronometro.cancel()
                        timerCronometro.purge()
                        timerRanking.cancel()
                        timerRanking.purge()
                        jogadorEncerrar(id_sessao, jogador)
                        navController!!.navigate(R.id.action_roomDiqueiroDicasFragment_to_mainFragment)
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
        view.findViewById<ImageButton>(R.id.btn_back).setOnClickListener(this)

        val id_sessao = requireArguments().getInt("id_sessao")
        val rodada = requireArguments().getInt("rodada")
        val jogador = requireArguments().getString("jogador_nome").toString()
        val doenca: String = requireArguments().getString("doenca").toString()
        val doencas = requireArguments().getStringArrayList("doencas")

        diqueiroProgressBar.visibility = View.INVISIBLE
        Toast.makeText(context,"Você pode enviar varias dicas!", Toast.LENGTH_SHORT).show()

        chronometro()
        editarRodada(id_sessao, doenca)
        definirDoenca()
        sintomas(doenca)
        prevencoes(doenca)
        transmicoes(doenca)
        ranking(id_sessao)

        nomeDoenca.text = "DOENÇA SELECIONADA: $doenca"

        timerCronometro.schedule(40000) {
            editarRodada(id_sessao, doenca)

            parametros.putInt("id_sessao", id_sessao)
            parametros.putString("jogador_nome", jogador)
            parametros.putStringArrayList("doencas", doencas)
            parametros.putString("ultimaDoenca", doenca)

            diqueirotempoCronometro.stop()
            timerRanking.cancel()
            timerRanking.purge()

            if (rodada == 5){
                jogadorEncerrar(id_sessao, jogador)
                navController!!.navigate(R.id.action_roomDiqueiroDicasFragment_to_winnerFragment, vencedor)
            }
            else
                navController!!.navigate(R.id.action_roomDiqueiroDicasFragment_to_placeholderRodadaFragment, parametros)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun chronometro(){
        diqueirotempoCronometro.isCountDown= true
        diqueirotempoCronometro.base = SystemClock.elapsedRealtime()+40000
        diqueirotempoCronometro.start()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_back -> activity?.onBackPressed()
            R.id.diqueiroBtnDicas -> {

                if(clicavel) {
                    val sintoma: String =
                        if (diqueiroSpinnerSintoma.visibility == View.VISIBLE) diqueiroSpinnerSintoma.selectedItem.toString()
                        else ""

                    val prevencao: String =
                        if (diqueiroSpinnerPrevencao.visibility == View.VISIBLE) diqueiroSpinnerPrevencao.selectedItem.toString()
                        else ""

                    val transmicao: String =
                        if (diqueiroSpinnerTransmicao.visibility == View.VISIBLE) diqueiroSpinnerTransmicao.selectedItem.toString()
                        else ""

                    if(sintoma.equals("Sintomas disponíveis") && prevencao.isEmpty() && transmicao.isEmpty()) {
                        val texto = "Selecione uma dica"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                    }
                    else if(sintoma.isNotEmpty() && prevencao.isEmpty() && transmicao.isEmpty()) {
                        diqueiroBtnDicas.setText("")
                        diqueiroProgressBar.visibility = View.VISIBLE;
                        clicavel = false

                        editarSessaoSintoma(DicaUnicaSintoma(sintoma))
                        diqueiroSpinnerSintoma.setSelection(0)
                    }
                    else if(sintoma.isEmpty() && prevencao.isNotEmpty() && transmicao.isEmpty()) {
                        diqueiroBtnDicas.setText("")
                        diqueiroProgressBar.visibility = View.VISIBLE;
                        clicavel = false

                        editarSessaoPrevencao(DicaUnicaPrevencao(prevencao))
                        diqueiroSpinnerSintoma.setSelection(0)
                    }
                    else if(sintoma.isEmpty() && prevencao.isEmpty() && transmicao.isNotEmpty()) {
                        diqueiroBtnDicas.setText("")
                        diqueiroProgressBar.visibility = View.VISIBLE;
                        clicavel = false

                        editarSessaoTransmicao(DicaUnicaTransmicao(transmicao))
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

                        recyclerRanking.apply {
                            layoutManager = LinearLayoutManager(activity)
                            adapter = RankingAdapter(response.body()!!)
                        }

                        val quantidadeJogadores: ArrayList<String> = arrayListOf("")
                        ranking.jogadores.forEach { quantidadeJogadores.add((it.nome)) }

                        quantidadeJogadores.removeAt(0)

                        if (quantidadeJogadores.size < 2) {
                            val jogador = requireArguments().getString("jogador_nome").toString()

                            val parametros = Bundle()
                            parametros.putInt("id_sessao", id_sessao)
                            parametros.putString("jogador_nome", jogador)

                            diqueirotempoCronometro.stop()
                            timerCronometro.cancel()
                            timerCronometro.purge()
                            timerRanking.cancel()
                            timerRanking.purge()
                            jogadorEncerrar(id_sessao, jogador)
                            navController!!.navigate(R.id.action_roomDiqueiroDicasFragment_to_expulsoSalaFragment, parametros)
                        }
                    }
                }
                else {
                    Log.d("Erro banco: Ranking", response.message())
                    context?.let { ErrorCases().error(it) }
                }
            }
        })
        timerRanking.schedule(30000) {
            ranking(id_sessao)
        }
    }

    fun definirDoenca(){
        val id_sessao = requireArguments().getInt("id_sessao")
        val rodada = requireArguments().getInt("rodada")
        val doenca: String = requireArguments().getString("doenca").toString()

        Service.retrofit.editarSessao(
            sessao = EditarSessaoRequest(
                id_sessao = id_sessao,
                rodada = rodada,
                doenca = doenca
            )
        ).enqueue(object : Callback<SessaoResponseEditing>{
            override fun onFailure(call: Call<SessaoResponseEditing>, t: Throwable) {
                Log.d("Ruim: EditarSessao", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseEditing>, response: Response<SessaoResponseEditing>) {
                Log.d("Bom: EditarSessao", response.body().toString())

                if (response.code() == 500) {
                    Log.d("Erro banco: EditarSes", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
    }

    fun populaSpinnerSintoma(sintomas: ArrayList<String>) {
        sintomas.toMutableList()
        context?.let {
            spinnerAdapter =
                ArrayAdapter(it, android.R.layout.simple_spinner_item, sintomas)
        }
        diqueiroSpinnerSintoma.adapter = spinnerAdapter
    }

    fun sintomas(doenca: String) {
        Service.retrofit.sintomas(
            doenca = doenca
        ).enqueue(object : Callback<Sintomas>{
            override fun onFailure(call: Call<Sintomas>, t: Throwable) {
                Log.d("Ruim: Sintomas", t.toString())
            }

            override fun onResponse(call: Call<Sintomas>, response: Response<Sintomas>) {
                Log.d("Bom: Sintomas", response.toString())

                if (response.isSuccessful) {
                    val listaSintomas = response.body()!!

                    if (listaSintomas.sintomas.isNotEmpty()) {
                        listaSintomas.sintomas.forEach { sintomasGlobal.add((it.nome)) }
                        populaSpinnerSintoma(sintomasGlobal)
                    }
                    else {
                        diqueiroSpinnerSintoma.visibility = View.INVISIBLE
                        diqueiroTxtSintomas.visibility = View.INVISIBLE
                    }
                }
                else {
                    Log.d("Erro banco: Sintomas", response.message())
                    context?.let { ErrorCases().error(it) }
                }
            }
        })
    }

    fun populaSpinnerPrevencoes(prevencoes: ArrayList<String>) {
        prevencoes.toMutableList()
        context?.let {
            spinnerAdapter =
                ArrayAdapter(it, android.R.layout.simple_spinner_item, prevencoes)
        }
        diqueiroSpinnerPrevencao.adapter = spinnerAdapter
    }

    fun prevencoes(doenca: String){
        Service.retrofit.prevencoes(
            doenca = doenca
        ).enqueue(object : Callback<Prevencoes>{
            override fun onFailure(call: Call<Prevencoes>, t: Throwable) {
                Log.d("Ruim: Prevencoes", t.toString())
            }

            override fun onResponse(call: Call<Prevencoes>, response: Response<Prevencoes>) {
                Log.d("Bom: Prevencoes", response.toString())

                if (response.isSuccessful) {
                    val listaPrevencao = response.body()!!

                    if (listaPrevencao.prevencoes.isNotEmpty()) {
                        listaPrevencao.prevencoes.forEach { prevencoesGlobal.add((it.nome)) }

                        populaSpinnerPrevencoes(prevencoesGlobal)
                    } else {
                        diqueiroSpinnerPrevencao.visibility = View.INVISIBLE
                        diqueiroTxtPrevencoes.visibility = View.INVISIBLE
                    }
                }
                else {
                    Log.d("Erro banco: Prevencoes", response.message())
                    context?.let { ErrorCases().error(it) }
                }
            }
        })
    }

    fun populaSpinnerTransmicoes(transmicoes: ArrayList<String>) {
        transmicoes.toMutableList()
        context?.let {
            spinnerAdapter =
                ArrayAdapter(it, android.R.layout.simple_spinner_item, transmicoes)
        }
        diqueiroSpinnerTransmicao.adapter = spinnerAdapter
    }

    fun transmicoes(doenca: String) {
        Service.retrofit.transmicoes(
            doenca = doenca
        ).enqueue(object : Callback<Transmissoes>{
            override fun onFailure(call: Call<Transmissoes>, t: Throwable) {
                Log.d("Ruim: Transmicoes", t.toString())
            }

            override fun onResponse(call: Call<Transmissoes>, response: Response<Transmissoes>) {
                Log.d("Bom: Transmicoes", response.toString())

                if (response.isSuccessful) {
                    val listaTransmicao = response.body()!!

                    if (listaTransmicao.transmicao.isNotEmpty()) {
                        listaTransmicao.transmicao.forEach { transmicoesGlobal.add((it.nome)) }

                        populaSpinnerTransmicoes(transmicoesGlobal)
                    } else {
                        diqueiroSpinnerTransmicao.visibility = View.INVISIBLE
                        diqueiroTxtTransmissoes.visibility = View.INVISIBLE
                    }
                }
                else {
                    Log.d("Erro banco: Transmicoes", response.message())
                    context?.let { ErrorCases().error(it) }
                }
            }
        })
    }

    fun editarSessaoSintoma(dica: DicaUnicaSintoma){
        val id_sessao = requireArguments().getInt("id_sessao")
        val rodada = requireArguments().getInt("rodada")
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
                Log.d("Ruim: EditarSessaoSint", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseEditing>, response: Response<SessaoResponseEditing>) {
                Log.d("Bom: EditarSessaoSint", response.body().toString())

                clicavel = true
                diqueiroBtnDicas.setText(R.string.btn_enviar_dica)
                diqueiroProgressBar.visibility = View.INVISIBLE

                if (response.isSuccessful) {
                    val sessao = response.body()!!

                    val texto = "Dica enviada!"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()

                    val sintomasSelecionados: ArrayList<String> = arrayListOf("")
                    sessao.dicas.sintomas.forEach { sintomasSelecionados.add((it.nome)) }

                    if (sintomasSelecionados.isNotEmpty()) {
                        sintomasGlobal.removeAll(sintomasSelecionados)
                    }

                    sintomasGlobal.add(0, "Sintomas disponíveis")
                    populaSpinnerSintoma(sintomasGlobal)
                }
                else {
                    Log.d("Erro banco: EditSessaoS", response.message())
                    context?.let { ErrorCases().error(it) }
                }
            }
        })
    }

    fun editarSessaoPrevencao(dica: DicaUnicaPrevencao){
        val id_sessao = requireArguments().getInt("id_sessao")
        val rodada = requireArguments().getInt("rodada")
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
                Log.d("Ruim: EditarSessaoPrev", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseEditing>, response: Response<SessaoResponseEditing>) {
                Log.d("Bom: EditarSessaoPrev", response.body().toString())

                clicavel = true
                diqueiroBtnDicas.setText(R.string.btn_enviar_dica)
                diqueiroProgressBar.visibility = View.INVISIBLE

                if (response.isSuccessful) {
                    val sessao = response.body()!!

                    val texto = "Dica enviada!"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()

                    val prevecoesSelecionados: ArrayList<String> = arrayListOf("")
                    sessao.dicas.sintomas.forEach { prevecoesSelecionados.add((it.nome)) }

                    if (prevecoesSelecionados.isNotEmpty()) {
                        prevencoesGlobal.removeAll(prevecoesSelecionados)
                    }

                    prevencoesGlobal.add(0, "")
                    populaSpinnerSintoma(prevencoesGlobal)
                }
                else {
                    Log.d("Erro banco: EditSessaoP", response.message())
                    context?.let { ErrorCases().error(it) }
                }
            }
        })
    }

    fun editarSessaoTransmicao(dica: DicaUnicaTransmicao){
        val id_sessao = requireArguments().getInt("id_sessao")
        val rodada = requireArguments().getInt("rodada")
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
                Log.d("Ruim: EditarSessaoTrans", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseEditing>, response: Response<SessaoResponseEditing>) {
                Log.d("Bom: EditarSessaoTrans", response.body().toString())

                clicavel = true
                diqueiroBtnDicas.setText(R.string.btn_enviar_dica)
                diqueiroProgressBar.visibility = View.INVISIBLE

                if (response.isSuccessful) {
                    val sessao = response.body()!!

                    val texto = "Dica enviada!"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()

                    val transmicoesSelecionados: ArrayList<String> = arrayListOf("")
                    sessao.dicas.sintomas.forEach { transmicoesSelecionados.add((it.nome)) }

                    if (transmicoesSelecionados.isNotEmpty()) {
                        transmicoesGlobal.removeAll(transmicoesSelecionados)
                    }

                    transmicoesGlobal.add(0, "")
                    populaSpinnerSintoma(transmicoesGlobal)
                }
                else {
                    Log.d("Erro banco: EditSessaoT", response.message())
                    context?.let { ErrorCases().error(it) }
                }
            }
        })
    }

    fun editarRodada(id_sessao: Int, doenca: String){
        val rodada = requireArguments().getInt("rodada")

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
                    Log.d("Erro banco: JogadorEnc", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
    }
}