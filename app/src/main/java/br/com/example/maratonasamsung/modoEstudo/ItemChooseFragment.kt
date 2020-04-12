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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_item_choose, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var param: DoencasResponse = arguments?.get("selfDoenca") as DoencasResponse
        this.txtDoencaNome.text = param.nome
        this.txtAgente.text = param.agente
        this.txtArraySintoma.text = param.sintomas.joinToString("\n"){ it.nome }
        this.txtPrevencao.text = param.prevencao.joinToString("\n") { it.nome }
        this.txtTipo.text = param.tipo
    }
}
