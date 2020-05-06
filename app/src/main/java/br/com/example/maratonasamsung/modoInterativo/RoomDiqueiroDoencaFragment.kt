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
        val doencas = requireArguments().getStringArrayList("doencas")

//        listarSessao(id_sessao)

        doencas!!.toMutableList()
        context?.let {
            spinnerAdapter = ArrayAdapter(it, android.R.layout.simple_spinner_item, doencas)
        }
        diqueiroSpinnerDoenca.adapter = spinnerAdapter
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.diqueiroBtnDoenca -> {
                val doenca = diqueiroSpinnerDoenca.selectedItem.toString()

                if(doenca.isEmpty()) {
                    val texto = "Selecione uma doença"
                    val duracao = Toast.LENGTH_SHORT
                    val toast = Toast.makeText(context, texto, duracao)
                    toast.show()
                }
                else {
                    val id_sessao = requireArguments().getInt("id")

                    val parametros = Bundle()
                    parametros.putInt("id", id_sessao)
                    parametros.putString("doenca", doenca)
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

                val doencas: ArrayList<String> = arrayListOf("")
                sessao?.doencas!!.forEach { doencas.add((it.nome)) }

                val doencasSelecionadas: ArrayList<String> = arrayListOf("")
                sessao.doencasSelecionadas.forEach { doencasSelecionadas.add((it.nome)) }

                for(doenca in doencas) {
                    doencasSelecionadas.forEach {
                        if (it.equals(doenca)) doencas.remove(doenca)
                    }
                }

                //AQUI EU JÁ TENHO TODAS AS DOENÇAS QUE AINDA JÁ FORAM USADAS NO JOGO E EU PRECISO COLOCÁ-LAS NO SPINNER, PARA FICAR ATUALIZADO
            }
        })
    }
}