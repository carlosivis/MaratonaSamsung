package br.com.example.maratonasamsung.modoEstudo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import kotlinx.android.synthetic.main.recycler_view_doencas.view.*

class DoencaAdapter(private val list: DoencasResponse?)
    : RecyclerView.Adapter<DoencaAdapter.DoencaViewHolder>() {

    class  DoencaViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recycler_view_doencas, parent, false)) {

        fun bind(doencas: DoencasResponse) {
            itemView.txtDoencaNome.text = doencas.nome
            itemView.txtArraySintoma.text = doencas.sintomas.toString()
            itemView.txtPrevencao.text = doencas.prevencao.toString()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoencaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DoencaViewHolder(inflater, parent)
    }
    override fun onBindViewHolder(holder: DoencaViewHolder, position: Int) {
       // val doenca = list[position]
        //holder.bind(doenca)
    }

    override fun getItemCount(): Int = 2//list.size

}


