package br.com.example.maratonasamsung

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_placeholder_rodada.*
import java.util.*
import kotlin.concurrent.schedule

/**
 * A simple [Fragment] subclass.
 */
class PlaceholderRodadaFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_placeholder_rodada, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val jogador = requireArguments().getString("jogador_nome")
        val diqueiro = requireArguments().getString("diqueiro")
        Log.d("Testezinho","$jogador - $diqueiro")
        val parametros = Bundle()
        parametros.getString("id_sessao")
        parametros.putString("jogador_nome",jogador)
        parametros.putInt("id_sessao",requireArguments().getInt("id_sessao"))
        parametros.putStringArrayList("doencas",requireArguments().getStringArrayList("doencas"))
        if (jogador==diqueiro)
            txtTipoJogador.text = "Agora você será o Diqueiro"
        else
            txtTipoJogador.text = "Agora você será o Adivinhador"

        Timer().schedule(5000) {
            if (jogador == diqueiro)
                Navigation.findNavController(view).navigate(R.id.action_placeholderRodadaFragment_to_roomDiqueiroDoencaFragment,parametros)

            else
                Navigation.findNavController(view).navigate(R.id.action_placeholderRodadaFragment_to_roomAdivinhadorFragment,parametros)
        }

    }
}
