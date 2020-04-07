package br.com.example.maratonasamsung.model.Responses

data class RankingResponse(
    val darDica: DarDica,
    val jogadores: List<Jogadores>
)