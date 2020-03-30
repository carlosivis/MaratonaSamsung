package br.com.example.maratonasamsung.modoInterativo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import br.com.example.maratonasamsung.R


/**
 * A simple [Fragment] subclass.
 */
class RoomFragment :  Fragment() {

    //lateinit var arr : ArrayList<String>
    var resposta : String = "nenhuma"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room, container, false)
    }

    fun selecionaDoenca(){
        var spinner = view?.findViewById<Spinner>(R.id.spinnerResposta)

        spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                //Toast.makeText(this@RoomFragment,"Selecione uma doença",Toast.LENGTH_LONG).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                resposta = parent!!.getItemAtPosition(position).toString()
            }
        }
    }

//    fun doencas() {
//        Service.retrofit.doencas().enqueue(Object : Callback<DoencasResponse> {
//            override fun onFailure(call: Call<DoencasResponse>, t: Throwable) {
//                Log.d("Deu ruim!!!",t.toString())
//            }
//
//            override fun onResponse(call: Call<DoencasResponse>, response: Response<DoencasResponse>) {
//                Log.d("Sucesso", response.body().toString())
//                var list : DoencasResponse = response.body()!!
//                val arr : ArrayList<String> = arrayListOf(response.body()!!.nome)
//                recyclerDoencas.apply{
//                layoutManager = LinearLayoutManager(activity)
//                adapter = DoencaAdapter(list)
//            }
//        })
//    }
}