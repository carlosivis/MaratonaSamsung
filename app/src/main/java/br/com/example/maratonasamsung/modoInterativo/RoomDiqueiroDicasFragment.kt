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
import br.com.example.maratonasamsung.model.Responses.Prevencoes
import br.com.example.maratonasamsung.model.Responses.RankingResponse
import br.com.example.maratonasamsung.model.Responses.Sintomas
import br.com.example.maratonasamsung.model.Responses.Transmicoes
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_diqueiro_dicas.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomDiqueiroDicasFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    lateinit var spinnerAdapter: ArrayAdapter<String>

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
                    .setTitle("Você deseja sair do jogo?")
                    .setMessage("Ao aceitar você sairá da sala e perderá toda a sua pontuação.")
                    .setPositiveButton(android.R.string.ok) { dialog, which ->
                        navController!!.navigate(R.id.mainFragment)
                    }
                    .setNegativeButton(android.R.string.cancel) { dialog, which -> }
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
        TODO("Not yet implemented")
    }

    fun ranking(id_sessao: Int){
        Service.retrofit.ranking(
            id_sessao = id_sessao
        ).enqueue(object :Callback<RankingResponse>{
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
    }

    fun sintomas(doenca: String) {
        Service.retrofit.sintomas(
            doenca = doenca
        ).enqueue(object : Callback<Sintomas>{
            override fun onFailure(call: Call<Sintomas>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<Sintomas>, response: Response<Sintomas>) {
                Log.d("Nice", response.toString())

                val listaSintomas = response.body()

                if (listaSintomas?.sintomas?.size != 0) {
                    val sintomas: ArrayList<String> = arrayListOf("")
                    listaSintomas?.sintomas?.forEach { sintomas.add((it.nome)) }
                    sintomas.toMutableList()
                    context?.let {
                        spinnerAdapter =
                            ArrayAdapter(it, android.R.layout.simple_spinner_item, sintomas)
                    }
                    diqueiroSpinnerSintoma.adapter = spinnerAdapter
                    var texto = "Sintomas"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }
                else {
                    var texto = "Sem sintomas"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }
            }
        })
    }

    fun transmicoes(doenca: String) {
        Service.retrofit.transmicoes(
            doenca = doenca
        ).enqueue(object : Callback<Transmicoes>{
            override fun onFailure(call: Call<Transmicoes>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<Transmicoes>, response: Response<Transmicoes>) {
                Log.d("Nice", response.toString())

                val listaTransmicao = response.body()

                if (listaTransmicao?.transmicao?.size != 0) {
                    val transmicoes: ArrayList<String> = arrayListOf("")
                    listaTransmicao?.transmicao?.forEach { transmicoes.add((it.nome)) }
                    transmicoes.toMutableList()
                    context?.let {
                        spinnerAdapter =
                            ArrayAdapter(it, android.R.layout.simple_spinner_item, transmicoes)
                    }
                    diqueiroSpinnerTransmicao.adapter = spinnerAdapter
                    var texto = "Transmição"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }
                else {
                    var texto = "Sem transmissões"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }
            }
        })
    }

    fun prevencoes(doenca: String) {
        Service.retrofit.prevencoes(
            doenca = doenca
        ).enqueue(object : Callback<Prevencoes>{
            override fun onFailure(call: Call<Prevencoes>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<Prevencoes>, response: Response<Prevencoes>) {
                Log.d("Nice", response.toString())

                val listaPrevencao = response.body()

                if (listaPrevencao?.prevencoes?.size != 0) {
                    val prevencoes: ArrayList<String> = arrayListOf("")
                    listaPrevencao?.prevencoes?.forEach { prevencoes.add((it.nome)) }
                    prevencoes.toMutableList()
                    context?.let {
                        spinnerAdapter =
                            ArrayAdapter(it, android.R.layout.simple_spinner_item, prevencoes)
                    }
                    diqueiroSpinnerPrevencao.adapter = spinnerAdapter
                    var texto = "Prevenção"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }
                else {
                    var texto = "Sem prevenção"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }
            }
        })
    }
}