package br.com.example.maratonasamsung.model.Responses

data class SessaoResponseEditing (
    val status: Boolean,
    val id_sessao: Int,
    val dicas: Dicas,
    val rodada: Int,
    val doencasSelecionadas: List<Doenca>
)