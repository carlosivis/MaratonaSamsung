package br.com.example.maratonasamsung.modoEstudo


import android.icu.lang.UCharacter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import br.com.example.appacessibilidade.Service
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import kotlinx.android.synthetic.main.fragment_choose.*
import kotlinx.android.synthetic.main.recycler_view_doencas.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ChooseFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose, container, false)

    val recyclerView = recyclerDoencas
        recyclerView.adapter = doencas()

    val layoutManager: GridLayoutManager = GridLayoutManager(
        context,
        StaggeredGridLayoutManager.VERTICAL
    )
        recyclerView.layoutManager = layoutManager

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doencas()
    }


    fun doencas() {
        return Service.retrofit.doencas().enqueue(object : Callback<DoencasResponse>{
            override fun onFailure(call: Call<DoencasResponse>, t: Throwable) {
                Log.d("Deu ruim!!!",t.toString())
            }

             override fun onResponse(
                call: Call<DoencasResponse>,
                response: Response<DoencasResponse>
            ) {
                Log.d("Sucesso",response.body().toString())
                 return txtDoencaNome.text = response.body().nome.

            }
        })

}


