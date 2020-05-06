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
            "bacterias" -> imageDoenca.setImageResource(R.drawable.bacteria)
            "virus" -> imageDoenca.setImageResource(R.drawable.virus)
            "platelmintos ou nemaltelmintos" -> imageDoenca.setImageResource(R.drawable.platelminto)
            "protozoarios" -> imageDoenca.setImageResource(R.drawable.protozoario)
        }
        txtAgente.text = "Agente: ${param.agente}"
        txtArraySintoma.text = "\u25CF " + param.sintomas.joinToString(" \n\u25CF ") { it.nome }
        txtPrevencao.text = "\u25CF " + param.prevencao.joinToString(" \n\u25CF ") { it.nome }
        txtTransmissao.text = "\u25CF " + param.transmicao.joinToString(" \n\u25CF ") { it.nome }

    }
}

