package br.com.example.maratonasamsung.modoEstudo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import br.com.example.maratonasamsung.model.Responses.Sintoma
import kotlinx.android.synthetic.main.recycler_view_doencas.view.*

class DoencaAdapter (private val list: List<DoencasResponse>?, val agenteInfectante: String): RecyclerView.Adapter<DoencaAdapter.DoencaViewHolder>() {

    class DoencaViewHolder(inflater: LayoutInflater, parent: ViewGroup): RecyclerView.ViewHolder(inflater.inflate(R.layout.recycler_view_listadoencas, parent, false)) {

        fun bind(doencas: DoencasResponse) {
                itemView.txtDoencaNome?.text = doencas.nome
                //itemView.txtArraySintoma?.text = doencas.sintomas.joinToString("\n") { it.nome }
                //itemView.txtPrevencao?.text = doencas.prevencao.joinToString("\n") { it.nome }
                //itemView.txtAgente?.text = "Agente: ${doencas.agente}"
                //itemView.txtTipo?.text = "Tipo: $agenteInfectante"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoencaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DoencaViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: DoencaViewHolder, position: Int) {

        val doenca: DoencasResponse = list!![position]
            if (doenca.tipo.equals(agenteInfectante)) {
                holder.bind(doenca)
            }
    }

    override fun getItemCount(): Int = list!!.size
    }
