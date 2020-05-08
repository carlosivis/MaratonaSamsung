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
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.SessaoResponseListing
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_diqueiro_doenca.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RoomDiqueiroDoencaFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    lateinit var spinnerAdapter: ArrayAdapter<String>
    var rodada: Int = 0 //não sei se inicializar com zero dá certo, nem se a variavel global funciona

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_diqueiro_doenca, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.let {
                AlertDialog.Builder(it)
                    .setTitle(R.string.sairJogo)
                    .setPositiveButton(R.string.sair) { dialog, which ->
                        navController!!.navigate(R.id.mainFragment)
                    }
                    .setNegativeButton(R.string.voltar) { dialog, which -> }
                    .show()
            }
        }
        callback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.diqueiroBtnDoenca).setOnClickListener(this)

        val id_sessao = requireArguments().getInt("id")

        listarSessao(id_sessao)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.diqueiroBtnDoenca -> {
                val id_sessao = requireArguments().getInt("id")
                val jogador = requireArguments().getString("jogador").toString()
                val doencas = requireArguments().getStringArrayList("doencas")
                val doenca = diqueiroSpinnerDoenca.selectedItem.toString()

//                val rodada = pegarRodada(id_sessao)

                if(doenca.isEmpty()) {
                    val texto = "Selecione uma doença"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }
                else {
                    val parametros = Bundle()
                    parametros.putInt("id", id_sessao)
                    parametros.putInt("rodada", (rodada+1))
                    parametros.putString("nome", jogador)
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
                sessao?.doencas!!.forEach { doencas.add((it.nome)) }

                val doencasSelecionadas: ArrayList<String> = arrayListOf("")
                sessao?.doencasSelecionadas!!.forEach { doencasSelecionadas.add((it.nome)) }

                if(doencasSelecionadas.isNotEmpty()) {
                    doencas!!.removeAll(doencasSelecionadas)
                }

                doencas?.add(0, "")

                doencas!!.toMutableList()
                context?.let {
                    spinnerAdapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, doencas)
                }
                diqueiroSpinnerDoenca.adapter = spinnerAdapter
            }
        })
    }
}