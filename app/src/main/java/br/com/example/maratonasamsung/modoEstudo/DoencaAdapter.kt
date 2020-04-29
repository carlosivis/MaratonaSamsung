package br.com.example.maratonasamsung.modoEstudo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import kotlinx.android.synthetic.main.recycler_view_listadoencas.view.*

class DoencaAdapter(
    private val list: List<DoencasResponse>?):  RecyclerView.Adapter<DoencaAdapter.DoencaViewHolder>() {


    class DoencaViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.recycler_view_listadoencas,
            parent,
            false)
    ) {
        fun bind(doencas: DoencasResponse) {
            itemView.txtDoencaNomeLista?.text = doencas.nome
            itemView.txtDoencaNomeLista.setOnClickListener{
                var bundle = Bundle()
                bundle.putSerializable("self", doencas)
                Log.d("Bundle", bundle.toString())
                itemView.findNavController().navigate(R.id.itemChooseFragment, bundle)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoencaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return DoencaViewHolder(inflater, parent)
    }

    override fun onBindViewHolder(holder: DoencaViewHolder, position: Int) {

        val doenca: DoencasResponse = list?.get(position)!!
        holder.bind(doenca)
    }

    override fun getItemCount(): Int = list!!.size


}