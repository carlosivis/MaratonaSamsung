package br.com.example.maratonasamsung.modoInterativo

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Requests.JogadorRequest
import br.com.example.maratonasamsung.model.Responses.JogadorEncerra
import br.com.example.maratonasamsung.service.ErrorCases
import br.com.example.maratonasamsung.service.Service
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
                        navController!!.navigate(R.id.action_roomDiqueiroDoencaFragment_to_mainFragment)
//                        diqueiroDoencaChronometro.stop()
                        timerCronometro.cancel()
                        timerCronometro.purge()
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
//        view.findViewById<Button>(R.id.diqueiroBtnDoenca).setOnClickListener(this)

        val id_sessao = requireArguments().getInt("id_sessao")
        val jogador = requireArguments().getString("jogador_nome").toString()
        val doencas = requireArguments().getStringArrayList("doencas")

//        chronometro()
//        listarSessao(id_sessao)

        timerCronometro.schedule(5000){
            parametros.putInt("id_sessao", id_sessao)
            parametros.putInt("rodada", rodada)
            parametros.putString("jogador_nome", jogador)
            parametros.putString("doenca", doencas?.random())
            parametros.putStringArrayList("doencas", doencas)

            navController!!.navigate(R.id.action_roomDiqueiroDoencaFragment_to_roomDiqueiroDicasFragment, parametros)
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

                if (!response.isSuccessful) {
                    Log.d("Erro banco: JogadorEnc", response.message())
                    context?.let { ErrorCases().error(it)}
                }
            }
        })
    }

//    @RequiresApi(Build.VERSION_CODES.N)
//    fun chronometro(){
//        diqueiroDoencaChronometro.isCountDown= true
//        diqueiroDoencaChronometro.base = SystemClock.elapsedRealtime()+20000
//        diqueiroDoencaChronometro.start()
//    }

//    override fun onClick(v: View?) {
//        when(v!!.id){
//            R.id.diqueiroBtnDoenca -> {
//
//                val id_sessao = requireArguments().getInt("id_sessao")
//                val jogador = requireArguments().getString("jogador_nome").toString()
//                val doencas = requireArguments().getStringArrayList("doencas")
//                val doenca = diqueiroSpinnerDoenca.selectedItem.toString()
//
//                if(doenca.isEmpty()) {
//                    val texto = "Selecione uma doença"
//                    val duracao = Toast.LENGTH_SHORT
//                    val toast = Toast.makeText(context, texto, duracao)
//                    toast.show()
//                }
//                else {
//                    diqueiroDoencaChronometro.stop()
//                    timerCronometro.cancel()
//                    timerCronometro.purge()
//
//                    val parametros = Bundle()
//                    parametros.putInt("id_sessao", id_sessao)
//                    parametros.putInt("rodada", (rodada))
//                    parametros.putString("jogador_nome", jogador)
//                    parametros.putString("doenca", doenca)
//                    parametros.putStringArrayList("doencas", doencas)
//
//                    navController!!.navigate(R.id.action_roomDiqueiroDoencaFragment_to_roomDiqueiroDicasFragment, parametros)
//                }
//            }
//        }
//    }

//    fun listarSessao(id_sessao: Int) {
//        Service.retrofit.listarSessao(
//            id_sessao = id_sessao
//        ).enqueue(object : Callback<SessaoResponseListing> {
//            override fun onFailure(call: Call<SessaoResponseListing>, t: Throwable) {
//                Log.d("Deu ruim", t.toString())
//            }
//            override fun onResponse(call: Call<SessaoResponseListing>, response: Response<SessaoResponseListing>) {
//                Log.d("Nice", response.toString())
//                if (response.code()==500){
//                    Log.d("Erro do banco", response.message())
//                    context?.let { ErrorCases().error(it)}
//                }
//                else{
//                    val sessao = response.body()
//
//                    rodada = sessao?.sessao!!.rodada
//
//                    val doencas: ArrayList<String> = arrayListOf("")
//                    sessao.doencas.forEach { doencas.add((it.nome)) }
//
//                    val doencasSelecionadas: ArrayList<String> = arrayListOf("")
//                    sessao.doencasSelecionadas.forEach { doencasSelecionadas.add((it.nome)) }
//
//                    if (doencasSelecionadas.isNotEmpty()) {
//                        doencas.removeAll(doencasSelecionadas)
//                    }
//
//                    doencas.add(0, "")
//
//                    doencas.toMutableList()
//                    context?.let {
//                        spinnerAdapter =
//                            ArrayAdapter(it, android.R.layout.simple_spinner_item, doencas)
//                    }
//                    diqueiroSpinnerDoenca.adapter = spinnerAdapter
//                }
//            }
//        })
//    }
}