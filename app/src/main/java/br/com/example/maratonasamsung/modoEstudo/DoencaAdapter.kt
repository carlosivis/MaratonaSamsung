package br.com.example.maratonasamsung.modoEstudo
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