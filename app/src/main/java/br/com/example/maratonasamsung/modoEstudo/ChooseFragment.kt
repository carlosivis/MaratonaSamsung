package br.com.example.maratonasamsung.modoEstudo


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
<<<<<<< HEAD
<<<<<<< HEAD
import br.com.example.maratonasamsung.R
//import br.com.example.maratonasamsung.service.doencas
=======
=======
>>>>>>> a6d5895123c7f077be4622bddb3c4ca209a809d6
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.example.appacessibilidade.Service
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import br.com.example.maratonasamsung.model.Responses.Prevencao
import br.com.example.maratonasamsung.model.Responses.Sintoma
import kotlinx.android.synthetic.main.fragment_choose.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
<<<<<<< HEAD
>>>>>>> 298d3c798d780ee8f3321c1330aeec04d6d77e62

=======
>>>>>>> a6d5895123c7f077be4622bddb3c4ca209a809d6

class ChooseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance =true
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
<<<<<<< HEAD
        //doencas()
=======

        doencas()
>>>>>>> 298d3c798d780ee8f3321c1330aeec04d6d77e62
    }


    fun doencas() {
        Service.retrofit.doencas().enqueue(object :Callback<DoencasResponse>{
            override fun onFailure(call: Call<DoencasResponse>, t: Throwable) {
                Log.d("Deu ruim!!!",t.toString())
            }

            override fun onResponse(call: Call<DoencasResponse>, response: Response<DoencasResponse>) {
                Log.d("Sucesso", response.body().toString())
                var list: DoencasResponse = response.body()!!
                val arr: ArrayList<DoencasResponse> = arrayListOf(DoencasResponse(
                    agente = "a",
                    id = 1,
                    nome = "aa",
                    prevencao = listOf(Prevencao("null"),Prevencao("null"),Prevencao("null")),
                    sintomas = listOf(Sintoma("null")),
                    tipo = "www"
                ))
                recyclerDoencas.apply{
                    layoutManager = LinearLayoutManager(activity)
                    adapter = DoencaAdapter(list)
                }

            }
        })
    }
}


