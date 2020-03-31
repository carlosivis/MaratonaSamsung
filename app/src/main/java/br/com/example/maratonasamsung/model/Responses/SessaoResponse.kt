package br.com.example.maratonasamsung.model.Responses

data class SessaoResponse(
    val id_sessao: Int,
    val nome: String,
    val senha: String,
    val doencas: List<DoencasResponse>
)