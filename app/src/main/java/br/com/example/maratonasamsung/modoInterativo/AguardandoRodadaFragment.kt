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
import br.com.example.maratonasamsung.model.Responses.JogadorResponse
import br.com.example.maratonasamsung.model.Responses.SessaoResponseListing
import br.com.example.maratonasamsung.service.ErrorCases
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_acess_name.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule

class AguardandoRodadaFragment : Fragment() {

    var navController: NavController? = null
    val timerRodada = Timer()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_aguardando_rodada, container, false)
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
                        navController!!.navigate(R.id.action_aguardandoRodadaFragment_to_mainFragment)
                        timerRodada.cancel()
                        timerRodada.purge()
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
        pegarRodada(id_sessao)
    }

    fun pegarRodada(id_sessao: Int) {
        Service.retrofit.listarSessao(
            id_sessao = id_sessao
        ).enqueue(object : Callback<SessaoResponseListing> {
            override fun onFailure(call: Call<SessaoResponseListing>, t: Throwable) {
                Log.d("Ruim: Pegar Rodada", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseListing>, response: Response<SessaoResponseListing>) {
                Log.d("Bom: Pegar Rodada", response.toString())

                if (response.isSuccessful) {
                    val resposta = response.body()!!

                    val rodada = resposta.sessao.rodada
                    val rodadaInicial = requireArguments().getInt("rodada")

                    if(rodada != rodadaInicial){
                        timerRodada.cancel()
                        timerRodada.purge()

                        jogadorNovo(id_sessao)
                    }
                }
                else {
                    Log.d("Erro banco: PegarRodada", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
        timerRodada.schedule(1000) {
            pegarRodada(id_sessao)
        }
    }

    fun jogadorNovo(id_sessao: Int){
        Service.retrofit.jogadorNovo(
            jogador = JogadorRequest(
                id_sessao = id_sessao,
                nome = requireArguments().getString("jogador_nome").toString()
            )
        ).enqueue(object : Callback<JogadorResponse> {
            override fun onFailure(call: Call<JogadorResponse>, t: Throwable) {
                Log.d("Ruim: Jogador novo", t.toString())
            }
            override fun onResponse(call: Call<JogadorResponse>, response: Response<JogadorResponse>) {
                Log.d("Bom: Jogador novo", response.toString())

                if (response.isSuccessful) {
                    val jogador = response.body()

                    if (!jogador!!.status) {
                        val doencas = requireArguments().getStringArrayList("doencas")

                        val parametrosAcessName = Bundle()
                        parametrosAcessName.putStringArrayList("doencas", doencas)

                        if (jogador.message == "Esse nome já existe") {
                            val texto = "Nome de usuário já existente nesta sala"
                            val duracao = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context, texto, duracao)
                            toast.show()

                            navController!!.navigate(R.id.action_aguardandoRodadaFragment_to_roomAcessNameFragment, parametrosAcessName)
                        } else if (jogador.message == "Sessao já foi iniciada") {
                            val texto = "Rodada já iniciada, entrada não mais permitida"
                            val duracao = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(context, texto, duracao)
                            toast.show()

                            navController!!.navigate(R.id.action_aguardandoRodadaFragment_to_roomAcessNameFragment, parametrosAcessName)
                        }
                    }
                    else {
                        val jogador = requireArguments().getString("jogador_nome").toString()
                        val doencas = requireArguments().getStringArrayList("doencas")

                        val parametros = Bundle()
                        parametros.putInt("id_sessao", id_sessao)
                        parametros.putString("jogador_nome", jogador)
                        parametros.putStringArrayList("doencas", doencas)
                        navController!!.navigate(R.id.action_aguardandoRodadaFragment_to_placeholderRodadaFragment, parametros)
                    }
                }
                else {
                    Log.d("Erro banco: JogadorNovo", response.message())
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
