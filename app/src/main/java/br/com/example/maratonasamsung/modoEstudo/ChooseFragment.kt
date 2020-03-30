package br.com.example.maratonasamsung.modoEstudo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.example.maratonasamsung.R

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


    /*    fun doencas() {
        Service.retrofit.doencas().enqueue(object :Callback<DoencasResponse>{
            override fun onFailure(call: Call<DoencasResponse>, t: Throwable) {
                Log.d("Deu ruim!!!",t.toString())
            }

            override fun onResponse(call: Call<DoencasResponse>, response: Response<DoencasResponse>) {
                Log.d("Sucesso", response.body().toString())
                var list: DoencasResponse = response.body()!!
                val arr: ArrayList<DoencasResponse> = arrayListOf(DoencasResponse(
                    agente = "a",
                    id = 1,ic
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
 */
}


