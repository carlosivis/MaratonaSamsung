package br.com.example.maratonasamsung.modoEstudo


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import br.com.example.maratonasamsung.service.Service
import kotlinx.android.synthetic.main.fragment_choose.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseFragment : Fragment() , View.OnClickListener, DoencaAdapter.OnItemClickListener{

    var navController: NavController? = null
    lateinit var doenca: List<DoencasResponse>
    lateinit var selfDoencaResponse: DoencasResponse

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
        }
  
    fun doencas(){
        Service.retrofit.doencas().enqueue(object : Callback<List<DoencasResponse>>{
            override fun onFailure(call: Call<List<DoencasResponse>>, t: Throwable) {
                Log.d("Deu ruim!!!",t.toString())
            }

            override fun onResponse(call: Call<List<DoencasResponse>>, response: Response<List<DoencasResponse>>) {
                Log.d("Sucesso", response.body().toString())
                doenca =  response!!.body()!!
                recyclerDoencas.apply{
                    layoutManager = LinearLayoutManager(activity)
                    adapter = DoencaAdapter(doenca.filter { it.tipo == arguments!!.getString("agenteInfectante") }, this@ChooseFragment)
                }
            }
        })
    }

    override fun onItemClicked(doencas: DoencasResponse) {
        val parametros = Bundle()
        selfDoencaResponse= doencas
        parametros.putSerializable("selfDoenca", selfDoencaResponse)
        onClick(view)
    }

    override fun onClick(v: View?) {
        when(v!!.id) {
            R.id.txtDoencaNomeLista ->navController!!.navigate(R.id.action_chooseFragment_to_itemChooseFragment)
        }
    }
}


