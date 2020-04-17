package br.com.example.maratonasamsung.modoEstudo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.DoencasResponse
import kotlinx.android.synthetic.main.recycler_view_listadoencas.view.txtDoencaNomeLista
import kotlinx.coroutines.joinAll

class DoencaAdapter(
    private val list: List<DoencasResponse>?):  RecyclerView.Adapter<DoencaAdapter.DoencaViewHolder>() {

    class DoencaViewHolder(inflater: LayoutInflater, parent: ViewGroup) : RecyclerView.ViewHolder(
        inflater.inflate(
            R.layout.recycler_view_listadoencas,
            parent,
            false)
    ) {

        fun bind(doencas: DoencasResponse) {
            Log.d("Self Doenca :", doencas.toString())
            itemView.txtDoencaNomeLista?.text = doencas.nome
                itemView.txtDoencaNomeLista.setOnClickListener{
                    val parametros =  Bundle()
                    parametros.putString("selfDoenca", doencas.nome)
                    Navigation.createNavigateOnClickListener(R.id.action_chooseFragment_to_itemChooseFragment
                    , parametros)
                    .onClick(itemView)}
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