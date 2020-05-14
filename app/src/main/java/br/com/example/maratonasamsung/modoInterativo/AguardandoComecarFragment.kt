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
import br.com.example.maratonasamsung.model.Responses.JogadorResponse
import br.com.example.maratonasamsung.model.Responses.SessaoResponseListing
import br.com.example.maratonasamsung.service.ErrorCases
import br.com.example.maratonasamsung.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule

class AguardandoComecarFragment : Fragment() {

    var navController: NavController? = null
    val timerComecar = Timer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aguardando_comecar, container, false)
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
                        timerComecar.cancel()
                        timerComecar.purge()
                        jogadorEncerrar(id_sessao, jogador)
                        navController!!.navigate(R.id.action_aguardandoComecarFragment_to_mainFragment)
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

        verificarPartida(id_sessao)
    }

    fun verificarPartida(id_sessao: Int) {
        Service.retrofit.verificarPartida(
            id_sessao = id_sessao
        ).enqueue(object : Callback<StatusBoolean> {
            override fun onFailure(call: Call<StatusBoolean>, t: Throwable) {
                Log.d("Ruim: Começar Partida", t.toString())
            }
            override fun onResponse(call: Call<StatusBoolean>, response: Response<StatusBoolean>) {
                Log.d("Bom: Começar Partida", response.body().toString())

                if (response.isSuccessful) {
                    val resposta = response.body()!!

                    if(resposta.status) {
                        timerComecar.cancel()
                        timerComecar.purge()

                        val jogador = requireArguments().getString("jogador_nome").toString()
                        val doencas = requireArguments().getStringArrayList("doencas")

                        val parametros = Bundle()
                        parametros.putInt("id_sessao", id_sessao)
                        parametros.putString("jogador_nome", jogador)
                        parametros.putStringArrayList("doencas", doencas)

                        navController!!.navigate(R.id.action_aguardandoComecarFragment_to_roomAdivinhadorFragment, parametros)
                    }
                }
                else {
                    Log.d("Erro banco: ComeçarPart", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
        timerComecar.schedule(1000){
            verificarPartida(id_sessao)
        }
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
