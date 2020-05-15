package br.com.example.maratonasamsung.modoInterativo

import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import kotlinx.android.synthetic.main.fragment_winner.*

class WinnerFragment : Fragment(), View.OnClickListener {

    var navController: NavController? = null
    var clicavel = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_winner, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            activity?.let {
                AlertDialog.Builder(it)
                    .setTitle("Você não pode voltar! Use o botão apropriado para sair da tela")
                    .setNegativeButton(android.R.string.ok) { dialog, which -> }
                    .show()
            }
        }
        callback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        view.findViewById<Button>(R.id.btnExit).setOnClickListener(this)

        winnerProgressBar.visibility = View.INVISIBLE

        nomeVencedor.text = requireArguments().getString("vencedor")
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.btnExit -> {
                if(clicavel) {
                    btnExit.setText("")
                    winnerProgressBar.visibility = View.VISIBLE
                    clicavel = false

                    navController!!.navigate(R.id.action_winnerFragment_to_mainFragment)
                }
            }
        }
    }
}
