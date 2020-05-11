package br.com.example.maratonasamsung.model.Responses

data class RankingResponse(
    val status: Boolean,
    val darDica: DarDica,
    val jogadores: List<Jogadores>
)