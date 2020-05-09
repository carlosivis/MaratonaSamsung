package br.com.example.maratonasamsung.modoInterativo

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.navigation.NavController
import androidx.navigation.Navigation

import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.RankingResponse
import br.com.example.maratonasamsung.model.Responses.SessaoResponseListing
import br.com.example.maratonasamsung.service.ErrorCases
import br.com.example.maratonasamsung.service.Service
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
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.let {
                AlertDialog.Builder(it)
                    .setTitle(R.string.sairJogo)
                    .setPositiveButton(R.string.sair) { dialog, which ->
                        navController!!.navigate(R.id.mainFragment)
                        timerRodada.cancel()
                        timerRodada.purge()
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
                Log.d("Deu ruim", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseListing>, response: Response<SessaoResponseListing>) {
                Log.d("Nice", response.toString())
                if (response.code()==500){
                    Log.d("Erro do banco", response.message())
                    context?.let { ErrorCases().error(it)}
                }
                else{
                    val response = response.body()

                    val rodada = response?.sessao!!.rodada
                    val rodadaInicial = requireArguments().getInt("rodada")

                    if(rodada != rodadaInicial){
                        timerRodada.cancel()
                        timerRodada.purge()

                        val jogador = requireArguments().getString("jogador_nome").toString()
                        val doencas = requireArguments().getStringArrayList("doencas")

                        val parametros = Bundle()
                        parametros.putInt("id_sessao", id_sessao)
                        parametros.putString("jogador_nome", jogador)
                        parametros.putStringArrayList("doencas", doencas)
                        navController!!.navigate(R.id.action_aguardandoRodadaFragment_to_placeholderRodadaFragment, parametros)
                    }
                }
            }
        })
        timerRodada.schedule(1000) {
            pegarRodada(id_sessao)
        }
    }
}
