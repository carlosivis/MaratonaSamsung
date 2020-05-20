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
    val sintomasZero = "     SINTOMAS:"
    val transmissoesZero = "     TRANSMISSÕES:"
    val prevecoesZero = "     PREVENÇÕES:"
    var sintomasGlobal: ArrayList<String> = arrayListOf(sintomasZero)
    var prevencoesGlobal: ArrayList<String> = arrayListOf(prevecoesZero)
    var transmicoesGlobal: ArrayList<String> = arrayListOf(transmissoesZero)
    var responseSintomas = false
    var responsePrevencoes = false
    var responseTransmicoes = false
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
        val doenca = requireArguments().getString("doenca").toString()
        val doencas = requireArguments().getStringArrayList("doencas")

        diqueiroProgressBar.visibility = View.INVISIBLE
        Toast.makeText(context,"Você pode enviar varias dicas!", Toast.LENGTH_SHORT).show()

        chronometro()
        sintomas(doenca)
        prevencoes(doenca)
        transmicoes(doenca)
        ranking(id_sessao)

        nomeDoenca.text = "Doença selecionada: $doenca"

        timerCronometro.schedule(60000) {
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
        diqueirotempoCronometro.base = SystemClock.elapsedRealtime()+60000
        diqueirotempoCronometro.start()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btn_back -> activity?.onBackPressed()
            R.id.diqueiroBtnDicas -> {

                if(clicavel) {
                    val dica = diqueiroSpinnerDicas.selectedItem.toString()

                    if(dica.isEmpty()) {
                        val texto = "Selecione uma dica"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                    }
                    else if(dica.equals(sintomasZero) || dica.equals(prevecoesZero) || dica.equals(transmissoesZero)) {
                        val texto = "Esta não é uma dica"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                    }
                    else {
                        diqueiroBtnDicas.setText("")
                        diqueiroProgressBar.visibility = View.VISIBLE;
                        clicavel = false

                        diqueiroSpinnerDicas.setSelection(0)

                        var encontraTipo = false

                        sintomasGlobal.forEach {
                            if(it == dica) {
                                encontraTipo = true
                                editarSessaoSintoma(DicaUnicaSintoma(dica))
                            }
                        }

                        if(!encontraTipo) {
                            prevencoesGlobal.forEach {
                                if(it == dica) {
                                    encontraTipo = true
                                    editarSessaoPrevencao(DicaUnicaPrevencao(dica))
                                }
                            }
                        }

                        if(!encontraTipo) {
                            transmicoesGlobal.forEach {
                                if(it == dica) {
                                    encontraTipo = true
                                    editarSessaoTransmicao(DicaUnicaTransmicao(dica))
                                }
                            }
                        }
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


    fun populaSpinner() {
        if(responseSintomas && responsePrevencoes && responseTransmicoes) {
            var dicas: ArrayList<String> = arrayListOf("")

            sintomasGlobal.forEach { dicas.add(it) }
            prevencoesGlobal.forEach { dicas.add(it) }
            transmicoesGlobal.forEach { dicas.add(it) }

            dicas.toMutableList()
            context?.let {
                spinnerAdapter =
                    ArrayAdapter(it, android.R.layout.simple_spinner_item, dicas)
            }
            diqueiroSpinnerDicas.adapter = spinnerAdapter
        }
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

                        responseSintomas = true

                        populaSpinner()
                    }
                }
                else {
                    Log.d("Erro banco: Sintomas", response.message())
                    context?.let { ErrorCases().error(it) }
                }
            }
        })
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

                        prevencoesGlobal.add(0, "")
                        responsePrevencoes = true

                        populaSpinner()
                    }
                }
                else {
                    Log.d("Erro banco: Prevencoes", response.message())
                    context?.let { ErrorCases().error(it) }
                }
            }
        })
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

                        transmicoesGlobal.add(0, "")
                        responseTransmicoes = true

                        populaSpinner()
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

                    if (sintomasSelecionados.isNotEmpty())
                        sintomasGlobal.removeAll(sintomasSelecionados)

                    populaSpinner()
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
                    sessao.dicas.prevencao.forEach { prevecoesSelecionados.add((it.nome)) }

                    if (prevecoesSelecionados.isNotEmpty())
                        prevencoesGlobal.removeAll(prevecoesSelecionados)

                    prevencoesGlobal.add(0, "")
                    populaSpinner()
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
                    sessao.dicas.transmicao.forEach { transmicoesSelecionados.add((it.nome)) }

                    if (transmicoesSelecionados.isNotEmpty())
                        transmicoesGlobal.removeAll(transmicoesSelecionados)

                    transmicoesGlobal.add(0, "")
                    populaSpinner()
                }
                else {
                    Log.d("Erro banco: EditSessaoT", response.message())
                    context?.let { ErrorCases().error(it) }
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