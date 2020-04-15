package br.com.example.maratonasamsung.modoInterativo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.*
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_room_diqueiro_dicas.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RoomDiqueiroDicasFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    lateinit var spinnerAdapter: ArrayAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_diqueiro_dicas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.diqueiroBtnDicas).setOnClickListener(this)

        val doenca: String = arguments!!.getString("doenca").toString()

        sintomas(doenca)
        transmicaos(doenca)
        prevencaos(doenca)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

    fun sintomas(doenca: String) {
        Service.retrofit.sintomas(
            doenca = doenca
        ).enqueue(object : Callback<Sintomas>{
            override fun onFailure(call: Call<Sintomas>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<Sintomas>, response: Response<Sintomas>) {
                Log.d("Nice", response.toString())

                val listaSintomas = response.body()

                lateinit var sintomas: ArrayList<String>
                sintomas = arrayListOf("")

                listaSintomas?.sintomas?.forEach { sintomas.add((it.nome)) }

                if (sintomas.size != 0) {
                    sintomas!!.toMutableList()
                    context?.let {
                        spinnerAdapter =
                            ArrayAdapter(it, android.R.layout.simple_spinner_item, sintomas)
                    }
                    diqueiroSpinnerSintoma.adapter = spinnerAdapter
                }
            }
        })
    }

    fun transmicaos(doenca: String) {
        Service.retrofit.transmicaos(
            doenca = doenca
        ).enqueue(object : Callback<Transmicoes>{
            override fun onFailure(call: Call<Transmicoes>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<Transmicoes>, response: Response<Transmicoes>) {
                Log.d("Nice", response.toString())

                val listaTransmicao = response.body()

                lateinit var transmicoes: ArrayList<String>
                transmicoes = arrayListOf("")

                listaTransmicao?.transmicoes?.forEach { transmicoes.add((it.nome)) }

                if (transmicoes.size != 0) {
                    transmicoes!!.toMutableList()
                    context?.let {
                        spinnerAdapter =
                            ArrayAdapter(it, android.R.layout.simple_spinner_item, transmicoes)
                    }
                    diqueiroSpinnerTransmicao.adapter = spinnerAdapter
                }
            }
        })
    }

    fun prevencaos(doenca: String) {
        Service.retrofit.prevencaos(
            doenca = doenca
        ).enqueue(object : Callback<Prevencoes>{
            override fun onFailure(call: Call<Prevencoes>, t: Throwable) {
                Log.d("Deu ruim", t.toString())
            }

            override fun onResponse(call: Call<Prevencoes>, response: Response<Prevencoes>) {
                Log.d("Nice", response.toString())

                val listaPrevencao = response.body()

                lateinit var prevencoes: ArrayList<String>
                prevencoes = arrayListOf("")

                listaPrevencao?.prevencoes?.forEach { prevencoes.add((it.nome)) }

                if (prevencoes.size != 0) {
                    prevencoes!!.toMutableList()
                    context?.let {
                        spinnerAdapter =
                            ArrayAdapter(it, android.R.layout.simple_spinner_item, prevencoes)
                    }
                    diqueiroSpinnerPrevencao.adapter = spinnerAdapter
                }
            }
        })
    }
}