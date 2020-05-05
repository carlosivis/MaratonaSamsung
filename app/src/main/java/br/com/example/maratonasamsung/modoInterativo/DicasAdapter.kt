package br.com.example.maratonasamsung.modoInterativo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.example.maratonasamsung.R
import kotlinx.android.synthetic.main.recycler_view_dicas.view.*

class DicasAdapter(private val dicas: ArrayList<String>):
    RecyclerView.Adapter<DicasAdapter.DicasViewHolder>() {

    class DicasViewHolder(inflater: LayoutInflater, parent: ViewGroup):
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recycler_view_dicas,parent,false)) {
        fun bind(dicas: String){
            itemView.txtDicaLancada.text = dicas
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DicasViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DicasViewHolder(inflater, parent)
    }

    override fun getItemCount() = dicas.size

    override fun onBindViewHolder(holder: DicasViewHolder, position: Int) {
        val dica  = dicas[position]
        holder.bind(dica)
    }
}