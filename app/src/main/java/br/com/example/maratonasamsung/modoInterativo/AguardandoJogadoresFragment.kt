package br.com.example.maratonasamsung.modoInterativo

import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Responses.JogadorEncerra
import br.com.example.maratonasamsung.model.Responses.RankingResponse
import br.com.example.maratonasamsung.service.ErrorCases
import br.com.example.maratonasamsung.service.Service
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val jogador = requireArguments().getString("jogador_nome").toString()
        val id_sessao = requireArguments().getInt("id_sessao")

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.let {
                AlertDialog.Builder(it)
                    .setTitle(R.string.sairJogo)
                    .setPositiveButton(R.string.sair) { dialog, which ->
                        navController!!.navigate(R.id.action_aguardandoJogadoresFragment_to_mainFragment)
                        timerJogadores.cancel()
                        timerJogadores.purge()
                        jogadorEncerrar(id_sessao, jogador)
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

        val id_sessao = requireArguments().getInt("id_sessao")
        jogadores(id_sessao)
    }

    fun jogadores(id_sessao: Int){
        Service.retrofit.ranking(
            id_sessao = id_sessao
        ).enqueue(object : Callback<RankingResponse> {
            override fun onFailure(call: Call<RankingResponse>, t: Throwable) {
                Log.d("Ruim: jogadores", t.toString())
            }

            override fun onResponse(call: Call<RankingResponse>, response: Response<RankingResponse>) {
                Log.d("Bom: jogadores", response.body().toString())

                if (response.isSuccessful) {
                    val jogadores = response.body()!!

                    if (!jogadores.status) {
                        val texto = "Erro ao atualizar ranking"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                    }
                    else {
                        val quantidadeJogadores: ArrayList<String> = arrayListOf("")
                        jogadores.jogadores.forEach { quantidadeJogadores.add((it.nome)) }

                        quantidadeJogadores.removeAt(0)

                        if (quantidadeJogadores.size > 1) {
                            timerJogadores.cancel()
                            timerJogadores.purge()

                            val jogador = requireArguments().getString("jogador_nome").toString()
                            val doencas = requireArguments().getStringArrayList("doencas")

                            val parametros = Bundle()
                            parametros.putInt("id_sessao", id_sessao)
                            parametros.putString("jogador_nome", jogador)
                            parametros.putStringArrayList("doencas", doencas)

                            navController!!.navigate(R.id.action_aguardandoJogadoresFragment_to_roomDiqueiroDoencaFragment, parametros)
                        }
                    }
                }
                else {
                    Log.d("Erro banco: jogadores", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
        timerJogadores.schedule(1000) {
            jogadores(id_sessao)
        }
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
