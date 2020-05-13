package br.com.example.maratonasamsung.modoEstudo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import kotlinx.android.synthetic.main.fragment_item_choose.*


class ItemChooseFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_choose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var param = arguments?.get("self") as DoencasResponse
        Log.d("Teste nome", param.toString())
        txtDoencaNome.text = param.nome
        Log.d(param.tipo, "euaqui")
        when (param.tipo) {
            "BACTERIAS" -> imageDoenca.setImageResource(R.drawable.bacteria)
            "VIRUS" -> imageDoenca.setImageResource(R.drawable.virus)
            "PLATELMINTOS E NEMATELMINTOS" -> imageDoenca.setImageResource(R.drawable.platelminto)
            "PROTOZOARIOS" -> imageDoenca.setImageResource(R.drawable.protozoario)
        }
        txtAgente.text = "Agente: ${param.agente}"
        txtArraySintoma.text = "\u00B7 "+param.sintomas.joinToString(" \n\u00B7 ") { it.nome }
        txtPrevencao.text = "\u00B7 " + param.prevencao.joinToString(" \n\u00B7 ") { it.nome }
        txtTransmissao.text = "\u00B7 " + param.transmicao.joinToString(" \n\u00B7 ") { it.nome }

    }
}

