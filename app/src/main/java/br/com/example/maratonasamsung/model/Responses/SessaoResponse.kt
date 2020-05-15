package br.com.example.maratonasamsung.model.Responses

data class SessaoResponse(
    val status: Boolean,
    val sessao: Sessao,
    val doencas: List<Doenca>,
    val doencasSelecionadas: List<Doenca>,
    val dicas: List<DicaResponse>
)