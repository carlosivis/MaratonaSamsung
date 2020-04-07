package br.com.example.maratonasamsung.modoInterativo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.example.maratonasamsung.R
import br.com.example.maratonasamsung.model.Responses.JogadorResponse
import kotlinx.android.synthetic.main.recycler_view_ranking.view.*

class RankingAdapter(private val rank:List<JogadorResponse>?)
    : RecyclerView.Adapter<RankingAdapter.RankingViewHolder>() {

    class RankingViewHolder(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recycler_view_ranking, parent, false)){
        fun bind(jogador: JogadorResponse){
            itemView.txtNomeJogador?.text = jogador.nome
            itemView.txtPontuacaoJogador?.text = jogador.pontuacao.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RankingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return RankingViewHolder(inflater, parent)
    }

    override fun getItemCount() = rank!!.size

    override fun onBindViewHolder(holder: RankingViewHolder, position: Int) {
        val jogador:JogadorResponse = rank!![position]
        holder.bind(jogador)
    }
}