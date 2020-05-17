package br.com.example.maratonasamsung.modoEstudo


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.data.repository.DoencasRepositoryImpl
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import br.com.example.maratonasamsung.data.service.ErrorCases
import br.com.example.maratonasamsung.data.service.Service
import kotlinx.android.synthetic.main.fragment_choose.*

class ChooseFragment : Fragment(), View.OnClickListener {
    private lateinit var viewModel: ChooseViewModel

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

        viewModel = ChooseViewModel(DoencasRepositoryImpl(Service))
        navController = Navigation.findNavController(view)

        observerError()
        observerLoading()
        observerResponse()
        viewModel.doencas()
        view.findViewById<ImageButton>(R.id.btn_back).setOnClickListener(this)
        }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btn_back -> activity?.onBackPressed()
        }
    }

    private fun configureRecyclerView(list: List<DoencasResponse>) {
        doencaAdapter = DoencaAdapter(list)
        recyclerDoencas.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = doencaAdapter
        }
    }

    private fun observerResponse() {
        viewModel.response.observe(viewLifecycleOwner,
            Observer {
                Log.d("Teste", it.toString())
                list = it
                configureRecyclerView(list.filter { it.tipo == requireArguments().getString("agenteInfectante") }.sortedBy { it.nome })
            })

    }

    private fun observerError() {
        viewModel.error.observe(viewLifecycleOwner,
            Observer {
                Log.d("Error message", it.toString())
                context?.let { ErrorCases().error(it) }
            })
    }

    private fun observerLoading() {
        viewModel.loading.observe(viewLifecycleOwner,
            Observer {
                Loading.visibility = if (it == true) View.VISIBLE else View.GONE
            })
    }
}


