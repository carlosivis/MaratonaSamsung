package br.com.example.maratonasamsung.ui.main

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.RankingResponse
import br.com.example.maratonasamsung.modoInterativo.RankingAdapter
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_adivinhador.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule

class AguardandoJogadoresFragment : Fragment() {

    var navController: NavController? = null
    val timerJogadores = Timer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aguardando_jogadores, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val id_sessao = requireArguments().getInt("id_sessao")
        jogadores(id_sessao)

        Timer().schedule(5000){
        }
    }

    fun jogadores(id_sessao: Int){
        Service.retrofit.ranking(
            id_sessao = id_sessao
        ).enqueue(object : Callback<RankingResponse> {
            override fun onFailure(call: Call<RankingResponse>, t: Throwable) {
                Log.d("Falha pegar jogadores", t.toString())
            }

            override fun onResponse(call: Call<RankingResponse>, response: Response<RankingResponse>) {
                Log.d("Sucesso pegar jogadores", response.body().toString())

                val jogadores = response.body()

                val quantidadeJogadores: ArrayList<String> = arrayListOf("")
                jogadores?.jogadores!!.forEach { quantidadeJogadores.add((it.nome)) }

                quantidadeJogadores.removeAt(0)

                if(quantidadeJogadores.size > 1) {
                    timerJogadores.cancel()
                    timerJogadores.purge()

                    val jogador = requireArguments().getString("jogador_nome").toString()
                    val doencas = requireArguments().getStringArrayList("doencas")

                    val parametros = Bundle()
                    parametros.putInt("id_sessao", id_sessao)
                    parametros.putString("jogador_nome", jogador)
                    parametros.putStringArrayList("doencas", doencas)

                    navController!!.navigate(R.id.action_aguardandoJogadoresFragment_to_roomDiqueiroDoencaFragment, parametros)
//                    Navigation.findNavController(view).navigate(R.id.action_aguardandoJogadoresFragment_to_roomDiqueiroDoencaFragment)
                }
            }
        })
        timerJogadores.schedule(2000) {
            jogadores(id_sessao)
        }
    }
}
