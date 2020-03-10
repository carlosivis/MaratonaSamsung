package br.com.example.maratonasamsung.modoEstudo


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import androidx.recyclerview.widget.RecyclerView
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import br.com.example.maratonasamsung.modoEstudo.DoencaAdapter.ViewHolder
import kotlinx.android.synthetic.main.recycler_view_doencas.view.*

class DoencaAdapter(
    private val notes:List<DoencasResponse>,
    private val context: Context): Adapter<ViewHolder>(){
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nomeDoenca = itemView.txtDoencaNome
            val sintoma = itemView.txtArraySintoma
            val prevencao = itemView.txtPrevencao

    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val doenca= notes[position]
        holder?.let {
            it.nomeDoenca.text = doenca.nome
            it.sintoma.text = doenca.sintomas.toString()
            it.prevencao.text = doenca.sintomas.toString()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recycler_view_doencas, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size
    }

}




//
//override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
//    DoencaViewHolder {
//        LayoutInflater.from(parent.context)
//            .inflate(R.layout.recycler_view_doencas, parent,false)
//    }
//
//
//
//override fun getItemCount() = doencas.size
//
//override fun onBindViewHolder(holder: DoencaViewHolder, position: Int) {
//    val doenca = doencas[position]
//    holder.bind(doenca)
//}
//
//
//class DoencaViewHolder(itemView: () -> View): RecyclerView.ViewHolder(itemView) {
//
//    fun bind(doencas: DoencasResponse) {
//        itemView.txtDoencaNome.text = doencas.nome
//        itemView.txtArraySintoma.text = doencas.sintomas.toString()
//        itemView.txtPrevencao.text = doencas.prevencao.toString()
//
//    }
//
//}