package br.com.example.maratonasamsung.modoInterativo


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_room_acess.*

/**
 * A simple [Fragment] subclass.
 */
class RoomAcessFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_room_acess, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.btnEntrar).setOnClickListener(this)

        var nomeJogador = editNome.text
        var nomeSala = editSala.text
        var senhaSala = editSenha.text

        btnEntrar.setOnClickListener { it: View? ->

        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnEntrar -> navController!!.navigate(R.id.action_roomAcessFragment_to_roomFragment)
        }
    }

}
