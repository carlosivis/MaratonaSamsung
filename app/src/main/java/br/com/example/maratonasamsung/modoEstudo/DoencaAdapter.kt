package br.com.example.maratonasamsung.modoEstudo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import kotlinx.android.synthetic.main.recycler_view_listadoencas.view.txtDoencaNomeLista

class DoencaAdapter(
    private val list: List<DoencasResponse>?, val itemClickListener: OnItemClickListener
):  RecyclerView.Adapter<DoencaAdapter.DoencaViewHolder>() {

    class DoencaViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.recycler_view_listadoencas,
            parent,
            false)
    ) {

        fun bind(doencas: DoencasResponse, clickListener: OnItemClickListener) {
            itemView.txtDoencaNomeLista?.text = doencas.nome
//            itemView.txtArraySintoma?.text = doencas.sintomas.joinToString("\n") { it.nome }
//            itemView.txtPrevencao?.text = doencas.prevencao.joinToString("\n") { it.nome }
//            itemView.txtAgente?.text = "Agente: ${doencas.agente}"
//            itemView.txtTipo?.text = "Tipo: ${doencas.tipo}"
            itemView.setOnClickListener {
                clickListener.onItemClicked(doencas)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoencaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DoencaViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: DoencaViewHolder, position: Int) {

        val doenca: DoencasResponse = list?.get(position)!!
        holder.bind(doenca,itemClickListener)

    }

    override fun getItemCount(): Int = list!!.size

    interface OnItemClickListener{
        fun onItemClicked(doencas: DoencasResponse)
    }
}