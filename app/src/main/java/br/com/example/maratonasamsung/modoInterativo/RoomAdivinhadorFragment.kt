package br.com.example.maratonasamsung.modoInterativo

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Requests.SalaRequest
import br.com.example.maratonasamsung.model.Responses.RankingResponse
import br.com.example.maratonasamsung.model.Responses.SessaoResponse
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_adivinhador.*
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule


class RoomAdivinhadorFragment :  Fragment() {

    var navController: NavController? = null
    lateinit var spinnerAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_adivinhador, container, false)
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

        val id_sessao = requireArguments().getInt("id")
        val doencas = requireArguments().getStringArrayList("doencas")

        doencas!!.toMutableList()
        context?.let {
            spinnerAdapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, doencas)
        }
        spinnerResposta.adapter = spinnerAdapter
        ranking(id_sessao)
        dicas(id_sessao)

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
    fun dicas(id_sessao: Int){
        Service.retrofit.dicas(id_sessao)
            .enqueue(object :Callback<SessaoResponse>{
                override fun onFailure(call: Call<SessaoResponse>, t: Throwable) {
                    Log.d("Falha ao pegar dicas", t.toString())
                }

                override fun onResponse(call: Call<SessaoResponse>, response: Response<SessaoResponse>) {
                    Log.d("Sucesso: dicas e sessao", response.body().toString())
                    var dicas: ArrayList<String> = arrayListOf("")
                    response.body()?.dicas?.forEach {
                        it.prevencoes.forEach { dicas.add(it.toString()) }
                        it.sintomas.forEach { dicas.add(it.toString()) }
                        it.transmicoes.forEach { dicas.add(it.toString()) }
                    }
                    recyclerDicas.apply {
                        layoutManager = LinearLayoutManager(activity)
                        adapter = DicasAdapter(dicas)
                    }
                }
            })
        Timer().schedule(2000){
            dicas(id_sessao)
        }
    }

}




