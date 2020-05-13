package br.com.example.maratonasamsung.modoEstudo


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import br.com.example.maratonasamsung.service.ErrorCases
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_choose.*
import kotlinx.android.synthetic.main.fragment_item_choose.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    lateinit var list: List<DoencasResponse>
    lateinit var doencaAdapter: DoencaAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
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
        doencas()
        navController = Navigation.findNavController(view)

        view.findViewById<ImageButton>(R.id.btn_back).setOnClickListener(this)
        }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_back -> activity?.onBackPressed()
        }
    }
  
    fun doencas() {
        Service.retrofit.doencas().enqueue(object : Callback<List<DoencasResponse>>{
            override fun onFailure(call: Call<List<DoencasResponse>>, t: Throwable) {
                Log.d("Deu ruim!!!",t.toString())
            }

            override fun onResponse(call: Call<List<DoencasResponse>>, response: Response<List<DoencasResponse>>) {
                Log.d("Sucesso", response.body().toString())
                if (response.code()==500){
                    Log.d("Erro do banco", response.message())
                    context?.let { ErrorCases().error(it) }
                }
                else{
                    list = response.body()!!
                    configureRecyclerView(list.filter { it.tipo == arguments!!.getString("agenteInfectante") })
                }
            }
        })
    }
    private fun configureRecyclerView(list: List<DoencasResponse>) {
        doencaAdapter = DoencaAdapter(list)
        recyclerDoencas.apply {
            layoutManager = LinearLayoutManager(context)
            adapter=doencaAdapter
        }
    }
}


