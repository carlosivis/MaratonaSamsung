package br.com.example.maratonasamsung.modoInterativo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.Jogadores
import br.com.example.maratonasamsung.model.Responses.RankingResponse
import kotlinx.android.synthetic.main.recycler_view_ranking.view.*

class JogadoresAdapter(private val rank: RankingResponse)
    : RecyclerView.Adapter<JogadoresAdapter.JogadoresViewHolder>() {

    class JogadoresViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recycler_view_jogadores, parent, false)){
        fun bind(jogador: Jogadores){
            itemView.txtNomeJogador?.text = jogador.nome
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JogadoresViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return JogadoresViewHolder(inflater, parent)
    }

    override fun getItemCount() = rank.jogadores.size

    override fun onBindViewHolder(holder: JogadoresViewHolder, position: Int) {
        val jogador: Jogadores = rank.jogadores[position]
        holder.bind(jogador)
    }
}