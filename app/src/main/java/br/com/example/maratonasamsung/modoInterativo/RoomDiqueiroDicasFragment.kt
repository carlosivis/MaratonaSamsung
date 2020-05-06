package br.com.example.maratonasamsung.modoInterativo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Requests.EditSessaoRequest
import br.com.example.maratonasamsung.model.Responses.*
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_diqueiro_dicas.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule

class RoomDiqueiroDicasFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    lateinit var spinnerAdapter: ArrayAdapter<String>
    var rodada: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_diqueiro_dicas, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.let {
                AlertDialog.Builder(it)
                    .setTitle(R.string.sairJogo)
                    .setMessage(R.string.sairJogoPont)
                    .setPositiveButton(R.string.sair) { dialog, which ->
                        navController!!.navigate(R.id.mainFragment)
                    }
                    .setNegativeButton(R.string.cancelar) { dialog, which -> }
                    .show()
            }
        }
        callback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.diqueiroBtnDicas).setOnClickListener(this)

        val id_sessao = requireArguments().getInt("id")
        val doenca: String = requireArguments().getString("doenca").toString()

        ranking(id_sessao)
        sintomas(doenca)
        prevencoes(doenca)
        transmicoes(doenca)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.diqueiroBtnDicas -> {
                lateinit var sintoma: String
                lateinit var prevencao: String
                lateinit var transmicao: String

                sintoma =
                    if (diqueiroSpinnerSintoma.visibility == View.VISIBLE) diqueiroSpinnerSintoma.selectedItem.toString()
                    else ""

                prevencao =
                    if (diqueiroSpinnerPrevencao.visibility == View.VISIBLE) diqueiroSpinnerPrevencao.selectedItem.toString()
                    else ""

                transmicao =
                    if (diqueiroSpinnerTransmicao.visibility == View.VISIBLE) diqueiroSpinnerTransmicao.selectedItem.toString()
                    else ""

                if(sintoma.isEmpty() && prevencao.isEmpty() && transmicao.isEmpty()) {
                    val texto = "Selecione uma dica"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }
                if(!sintoma.isEmpty() && prevencao.isEmpty() && transmicao.isEmpty()) {
                    lateinit var dica: DicaUnica
                    dica.nome = sintoma
                    editarSessao(dica)
                }
                else if(sintoma.isEmpty() && !prevencao.isEmpty() && transmicao.isEmpty()) {
                    var dica: DicaUnica = ""
                    dica.nome = prevencao
                    editarSessao(dica)
                }
                else if(sintoma.isEmpty() && prevencao.isEmpty() && !transmicao.isEmpty()) {
                    lateinit var dica: DicaUnica
                    dica.nome = transmicao
                    editarSessao(dica)
                }
                else {
                    val texto = "Você pode enviar apenas uma dica por vez"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }



//                if( (!sintoma.isEmpty() && !transmicao.isEmpty() && !prevencao.isEmpty()) || (!sintoma.isEmpty() && !transmicao.isEmpty())
//                    || (!sintoma.isEmpty() && !prevencao.isEmpty()) || (!transmicao.isEmpty() && !prevencao.isEmpty()) ) {
//                    val texto = "Você pode enviar somente uma dica por vez"
//                    val duracao = Toast.LENGTH_SHORT
//                    val toast = Toast.makeText(context, texto, duracao)
//                    toast.show()
//                }
//                else if(!sintoma.isEmpty())
//                    editarSessao(sintoma)
//                else if(!prevencao.isEmpty())
//                    editarSessao(prevencao)
//                else if(!transmicao.isEmpty())
//                    editarSessao(transmicao)
            }
        }
    }

    fun editarSessao(dica: DicaUnica){
        val id_sessao = requireArguments().getInt("id")
        val doenca: String = requireArguments().getString("doenca").toString()
        Service.retrofit.editarSessao(
            sessao = EditSessaoRequest(
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

                val sessao = response.body()

                //AQUI EU RECEBO, ENTRE OUTRAS COISAS, TODAS AS DICAS QUE JÁ FORAM E EU PRECISO RETIRÁ-LAS DOS SPINNERS
            }
        })
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
                recyclerRanking.apply {
                    layoutManager = LinearLayoutManager(activity)
                    adapter = RankingAdapter(response.body()!!)
                }
            }
        })
        Timer().schedule(2000) {
            ranking(id_sessao)
        }
    }
    fun sintomas(doenca: String) {
        Service.retrofit.sintomas(
            doenca = doenca
        ).enqueue(object : Callback<List<Sintoma>>{
            override fun onFailure(call: Call<List<Sintoma>>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<List<Sintoma>>, response: Response<List<Sintoma>>) {
                Log.d("Nice", response.toString())

                val listaSintomas = response.body()

                if (listaSintomas?.size != 0) {
                    val sintomas: ArrayList<String> = arrayListOf("")
                    listaSintomas?.forEach { sintomas.add((it.nome)) }
                    sintomas.toMutableList()
                    context?.let {
                        spinnerAdapter =
                            ArrayAdapter(it, android.R.layout.simple_spinner_item, sintomas)
                    }
                    diqueiroSpinnerSintoma.adapter = spinnerAdapter
                }
                else {
                    diqueiroSpinnerSintoma.visibility = View.INVISIBLE
                    diqueiroTxtSintomas.visibility = View.INVISIBLE
                }
            }
        })
    }

    fun prevencoes(doenca: String) {
        Service.retrofit.prevencoes(
            doenca = doenca
        ).enqueue(object : Callback<List<Prevencao>>{
            override fun onFailure(call: Call<List<Prevencao>>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<List<Prevencao>>, response: Response<List<Prevencao>>) {
                Log.d("Nice", response.toString())

                val listaPrevencao = response.body()

                if (listaPrevencao?.size != 0) {
                    val prevencoes: ArrayList<String> = arrayListOf("")
                    listaPrevencao?.forEach { prevencoes.add((it.nome)) }
                    prevencoes.toMutableList()
                    context?.let {
                        spinnerAdapter =
                            ArrayAdapter(it, android.R.layout.simple_spinner_item, prevencoes)
                    }
                    diqueiroSpinnerPrevencao.adapter = spinnerAdapter
                }
                else {
                    diqueiroSpinnerPrevencao.visibility = View.INVISIBLE
                    diqueiroTxtPrevencoes.visibility = View.INVISIBLE
                }
            }
        })
    }

    fun transmicoes(doenca: String) {
        Service.retrofit.transmicoes(
            doenca = doenca
        ).enqueue(object : Callback<List<Transmicao>>{
            override fun onFailure(call: Call<List<Transmicao>>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<List<Transmicao>>, response: Response<List<Transmicao>>) {
                Log.d("Nice", response.toString())

                val listaTransmicao = response.body()

                if (listaTransmicao?.size != 0) {
                    val transmicoes: ArrayList<String> = arrayListOf("")
                    listaTransmicao?.forEach { transmicoes.add((it.nome)) }
                    transmicoes.toMutableList()
                    context?.let {
                        spinnerAdapter =
                            ArrayAdapter(it, android.R.layout.simple_spinner_item, transmicoes)
                    }
                    diqueiroSpinnerTransmicao.adapter = spinnerAdapter
                }
                else {
                    diqueiroSpinnerTransmicao.visibility = View.INVISIBLE
                    diqueiroTxtTransmissoes.visibility = View.INVISIBLE
                }
            }
        })
    }
}