package br.com.example.maratonasamsung.modoEstudo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import kotlinx.android.synthetic.main.fragment_item_choose.*


class ItemChooseFragment : Fragment() {
    var param =  arguments?.getString("selfDoenca")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_choose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        txtDoencaNome.text = param
//        txtAgente.text = "Agente: ${param.agente}"
//        txtArraySintoma.text = param.sintomas.joinToString("\n"){ it.nome }
//        txtPrevencao.text = param.prevencao.joinToString("\n") { it.nome }
//        txtTipo.text = "Tipo: ${param.tipo}"
    }
}
