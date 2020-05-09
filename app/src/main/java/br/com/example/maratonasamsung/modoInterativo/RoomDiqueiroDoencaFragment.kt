package br.com.example.maratonasamsung.modoInterativo

import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Requests.EditSessaoSintomaRequest
import br.com.example.maratonasamsung.model.Requests.EditarRodadaRequest
import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Responses.JogadorEncerra
import br.com.example.maratonasamsung.model.Responses.SessaoResponseEditing
import br.com.example.maratonasamsung.model.Responses.SessaoResponseListing
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_adivinhador.*
import kotlinx.android.synthetic.main.fragment_room_diqueiro_dicas.*
import kotlinx.android.synthetic.main.fragment_room_diqueiro_doenca.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class RoomDiqueiroDoencaFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    lateinit var spinnerAdapter: ArrayAdapter<String>
    var rodada: Int = 0
    val parametros = Bundle()

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
                        navController!!.navigate(R.id.mainFragment)
                        jogadorEncerrar(id_sessao, jogador)
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
        view.findViewById<Button>(R.id.diqueiroBtnDoenca).setOnClickListener(this)

        val id_sessao = requireArguments().getInt("id_sessao")
        val jogador = requireArguments().getString("jogador_nome").toString()
        val doencas = requireArguments().getStringArrayList("doencas")

        chronometro()
        listarSessao(id_sessao)

        Timer().schedule(20000){
            diqueiroDoencaChronometro.stop()

            parametros.putInt("id_sessao", id_sessao)
            parametros.putInt("rodada", (rodada+1))
            parametros.putString("jogador_nome", jogador)
            parametros.putString("doenca", doencas?.random())
            parametros.putStringArrayList("doencas", doencas)

            navController!!.navigate(R.id.action_roomDiqueiroDoencaFragment_to_roomDiqueiroDicasFragment, parametros)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun chronometro(){
        diqueiroDoencaChronometro.isCountDown= true
        diqueiroDoencaChronometro.base = SystemClock.elapsedRealtime()+20000
        diqueiroDoencaChronometro.start()
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.diqueiroBtnDoenca -> {

                val id_sessao = requireArguments().getInt("id_sessao")
                val jogador = requireArguments().getString("jogador_nome").toString()
                val doencas = requireArguments().getStringArrayList("doencas")
                val doenca = diqueiroSpinnerDoenca.selectedItem.toString()

                if(doenca.isEmpty()) {
                    val texto = "Selecione uma doença"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }
                else {
                    editarRodada(id_sessao, doenca)

                    val parametros = Bundle()
                    parametros.putInt("id_sessao", id_sessao)
                    parametros.putInt("rodada", (rodada))
                    parametros.putString("jogador_nome", jogador)
                    parametros.putString("doenca", doenca)
                    parametros.putStringArrayList("doencas", doencas)

                    navController!!.navigate(R.id.action_roomDiqueiroDoencaFragment_to_roomDiqueiroDicasFragment, parametros)
                }
            }
        }
    }

    fun listarSessao(id_sessao: Int) {
        Service.retrofit.listarSessao(
            id_sessao = id_sessao
        ).enqueue(object : Callback<SessaoResponseListing> {
            override fun onFailure(call: Call<SessaoResponseListing>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseListing>, response: Response<SessaoResponseListing>) {
                Log.d("Nice", response.toString())

                val sessao = response.body()

                rodada = sessao?.sessao!!.rodada

                val doencas: ArrayList<String> = arrayListOf("")
                sessao.doencas.forEach { doencas.add((it.nome)) }

                val doencasSelecionadas: ArrayList<String> = arrayListOf("")
                sessao.doencasSelecionadas.forEach { doencasSelecionadas.add((it.nome)) }

                if(doencasSelecionadas.isNotEmpty()) {
                    doencas.removeAll(doencasSelecionadas)
                }

                doencas.add(0, "")

                doencas.toMutableList()
                context?.let {
                    spinnerAdapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, doencas)
                }
                diqueiroSpinnerDoenca.adapter = spinnerAdapter
            }
        })
    }

    fun editarRodada(id_sessao: Int, doenca: String){
        Service.retrofit.editarRodada(
            sessao = EditarRodadaRequest(
                id_sessao = id_sessao,
                rodada = rodada,
                doenca = doenca
            )
        ).enqueue(object : Callback<SessaoResponseEditing>{
            override fun onFailure(call: Call<SessaoResponseEditing>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }
            override fun onResponse(call: Call<SessaoResponseEditing>, response: Response<SessaoResponseEditing>) {
                Log.d("Nice", response.body().toString())

                val sessao = response.body()
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
                Log.d("Falha ao encerrar", t.toString())
            }

            override fun onResponse(call: Call<JogadorEncerra>, response: Response<JogadorEncerra>) {
                Log.d("Sucesso ao encerrar", response.body().toString())
            }
        })
    }
}