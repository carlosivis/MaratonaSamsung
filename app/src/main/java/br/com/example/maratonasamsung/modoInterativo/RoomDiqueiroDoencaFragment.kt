package br.com.example.maratonasamsung.modoInterativo

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Responses.RankingResponse
import br.com.example.maratonasamsung.model.Responses.StatusBoolean
import br.com.example.maratonasamsung.model.Responses.SessaoResponseListing
import br.com.example.maratonasamsung.data.service.ErrorCases
import br.com.example.maratonasamsung.data.service.Service
import br.com.example.maratonasamsung.model.Requests.EditarSessaoRequest
import br.com.example.maratonasamsung.model.Responses.SessaoResponseEditing
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.concurrent.schedule


class RoomDiqueiroDoencaFragment : Fragment() { //, View.OnClickListener

    //    lateinit var spinnerAdapter: ArrayAdapter<String>
    var navController: NavController? = null
    val timerCronometro = Timer()
    val parametros = Bundle()
    var rodada: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_diqueiro_doenca, container, false)
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
                        navController!!.navigate(R.id.action_roomDiqueiroDoencaFragment_to_mainFragment)
                    }
                    .setNegativeButton(R.string.voltar) { dialog, which -> }
                    .show()
            }
        }
        callback
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)

        val id_sessao = requireArguments().getInt("id_sessao")
        val jogador = requireArguments().getString("jogador_nome").toString()
        val doencas = requireArguments().getStringArrayList("doencas")
        val doenca = doencas!!.random()

        jogadores(id_sessao)
        pegarRodada(id_sessao, doenca)

        timerCronometro.schedule(5000){
            parametros.putInt("id_sessao", id_sessao)
            parametros.putInt("rodada", (rodada+1))
            parametros.putString("jogador_nome", jogador)
            parametros.putString("doenca", doenca)
            parametros.putStringArrayList("doencas", doencas)

            navController!!.navigate(R.id.action_roomDiqueiroDoencaFragment_to_roomDiqueiroDicasFragment, parametros)
        }
    }

    fun pegarRodada(id_sessao: Int, doenca: String) {
        Service.retrofit.listarSessao(
            id_sessao = id_sessao
        ).enqueue(object : Callback<SessaoResponseListing> {
            override fun onFailure(call: Call<SessaoResponseListing>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseListing>, response: Response<SessaoResponseListing>) {
                Log.d("Nice", response.toString())

                if (response.isSuccessful) {
                    val sessao = response.body()!!
                    rodada = sessao.sessao.rodada
//                    editarRodada(id_sessao, doenca)
                }
                else {
                    Log.d("Erro do banco", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
    }

    fun editarRodada(id_sessao: Int, doenca: String){
        val rodada = requireArguments().getInt("rodada")

        Service.retrofit.editarRodada(
            sessao = EditarSessaoRequest(
                id_sessao = id_sessao,
                rodada = (rodada+1),
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

    fun jogadores(id_sessao: Int){
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
                        val quantidadeJogadores: ArrayList<String> = arrayListOf("")
                        ranking.jogadores.forEach { quantidadeJogadores.add((it.nome)) }

                        quantidadeJogadores.removeAt(0)

                        if (quantidadeJogadores.size < 2) {
                            val jogador = requireArguments().getString("jogador_nome").toString()

                            val parametrosJogadores = Bundle()
                            parametrosJogadores.putInt("id_sessao", id_sessao)
                            parametrosJogadores.putString("jogador_nome", jogador)

                            timerCronometro.cancel()
                            timerCronometro.purge()

                            jogadorEncerrar(id_sessao, jogador)
                            navController!!.navigate(R.id.action_roomDiqueiroDoencaFragment_to_expulsoSalaFragment, parametrosJogadores)
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
}