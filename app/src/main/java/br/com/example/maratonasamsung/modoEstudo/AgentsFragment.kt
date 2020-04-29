package br.com.example.maratonasamsung.modoEstudo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R

/**
 * A simple [Fragment] subclass.
 */
class AgentsFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    lateinit var agenteInfectante: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agents, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<ImageView>(R.id.imageBacteria).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageVirus).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imagePlatelminto).setOnClickListener(this)
        view.findViewById<ImageView>(R.id.imageProtozoario).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val parametros = Bundle()
        when(v!!.id){
            R.id.imageProtozoario -> {
                agenteInfectante = "protozoarios"
                parametros.putString("agenteInfectante", agenteInfectante)
                navController!!.navigate(R.id.action_agentsFragment_to_chooseFragment, parametros)
            }
            R.id.imagePlatelminto -> {
                agenteInfectante = "platelmintos ou nemaltelmintos"
                parametros.putString("agenteInfectante", agenteInfectante)
                navController!!.navigate(R.id.action_agentsFragment_to_chooseFragment, parametros)
            }
            R.id.imageVirus -> {
                agenteInfectante = "virus"
                parametros.putString("agenteInfectante", agenteInfectante)
                navController!!.navigate(R.id.action_agentsFragment_to_chooseFragment, parametros)
            }
            R.id.imageBacteria -> {
                agenteInfectante = "bacterias"
                parametros.putString("agenteInfectante", agenteInfectante)
                navController!!.navigate(R.id.action_agentsFragment_to_chooseFragment, parametros)
            }
        }
    }
}
