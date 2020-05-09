package br.com.example.maratonasamsung.modoInterativo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import br.com.example.maratonasamsung.R
import kotlinx.android.synthetic.main.fragment_winner.*

/**
 * A simple [Fragment] subclass.
 */
class WinnerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_winner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        nomeVencedor.text = requireArguments().getString("vencedor")

        btnExit.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.mainFragment)
        }

    }


}
