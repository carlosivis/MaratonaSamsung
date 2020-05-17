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
import br.com.example.maratonasamsung.model.Responses.StatusBoolean
import br.com.example.maratonasamsung.model.Responses.RankingResponse
import br.com.example.maratonasamsung.data.service.ErrorCases
import br.com.example.maratonasamsung.data.service.Service
import kotlinx.android.synthetic.main.fragment_placeholder_rodada.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule

class PlaceholderRodadaFragment : Fragment() {

    var navController: NavController? = null
    val timerCronometro = Timer()
    lateinit var diqueiro: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_placeholder_rodada, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val id_sessao = requireArguments().getInt("id_sessao")
        val jogador = requireArguments().getString("jogador_nome").toString()

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.let {
                AlertDialog.Builder(it)
                    .setTitle(R.string.sairJogo)
                    .setPositiveButton(R.string.sair) { dialog, which ->
                        timerCronometro.cancel()
                        timerCronometro.purge()
                        jogadorEncerrar(id_sessao, jogador)
                        navController!!.navigate(R.id.action_placeholderRodadaFragment_to_mainFragment)
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
        val jogador = requireArguments().getString("jogador_nome").toString()
        val doencas = requireArguments().getStringArrayList("doencas")

        ranking(id_sessao)

        val parametros = Bundle()
        parametros.putInt("id_sessao", id_sessao)
        parametros.putString("jogador_nome",jogador)
        parametros.putStringArrayList("doencas", doencas)

        timerCronometro.schedule(5000) {
            if (jogador == diqueiro)
                navController!!.navigate(R.id.action_placeholderRodadaFragment_to_roomDiqueiroDoencaFragment,parametros)
            else
                navController!!.navigate(R.id.action_placeholderRodadaFragment_to_roomAdivinhadorFragment,parametros)
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
                    val resposta = response.body()!!

                    if (!resposta.status) {
                        val texto = "Erro ao atualizar ranking"
                        val duracao = Toast.LENGTH_SHORT
                        val toast = Toast.makeText(context, texto, duracao)
                        toast.show()
                    }
                    else {
                        val quantidadeJogadores: ArrayList<String> = arrayListOf("")
                        resposta.jogadores.forEach { quantidadeJogadores.add((it.nome)) }

                        quantidadeJogadores.removeAt(0)

                        if (quantidadeJogadores.size < 2) {
                            val jogador = requireArguments().getString("jogador_nome").toString()

                            val parametros = Bundle()
                            parametros.putInt("id_sessao", id_sessao)
                            parametros.putString("jogador_nome", jogador)

                            timerCronometro.cancel()
                            timerCronometro.purge()

                            jogadorEncerrar(id_sessao, jogador)
                            navController!!.navigate(R.id.action_placeholderRodadaFragment_to_expulsoSalaFragment, parametros)
                        }

                        diqueiro = resposta.darDica.nome
                        val jogador = requireArguments().getString("jogador_nome").toString()

                        if (jogador == diqueiro)
                            txtTipoJogador.text = "Agora você será o Diqueiro"
                        else {
                            val ultimaDoenca = requireArguments().getString("ultimaDoenca")

                            txtTipoJogador.text = "Agora você será o Adivinhador"
                            txtDoencaRodada.text = "A Doença era: $ultimaDoenca"
                        }
                    }
                }
                else {
                    Log.d("Erro banco: Ranking", response.message())
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
