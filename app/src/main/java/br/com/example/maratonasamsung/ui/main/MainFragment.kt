package br.com.example.maratonasamsung.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R

class MainFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.btnModoEstudo).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnModoInterativo).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnMais).setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnModoInterativo -> navController!!.navigate(R.id.action_mainFragment_to_roomTypekFragment)
            R.id.btnModoEstudo -> navController!!.navigate(R.id.action_mainFragment_to_agentsFragment)
            R.id.btnMais -> {
                navController!!.navigate(R.id.action_mainFragment_to_moreFragment)
            }
        }
    }
}

