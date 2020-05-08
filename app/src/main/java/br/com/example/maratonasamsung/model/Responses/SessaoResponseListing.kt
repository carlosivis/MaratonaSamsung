package br.com.example.maratonasamsung.model.Responses

data class SessaoResponseListing (
    val status: Boolean,
    val sessao: Sessao,
    val dicas: Dicas,
    val doencasSelecionadas: List<Doenca>,
    val doencas: List<Doenca>
)