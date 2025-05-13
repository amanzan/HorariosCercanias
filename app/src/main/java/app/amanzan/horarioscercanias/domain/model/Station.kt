package app.amanzan.horarioscercanias.domain.model

data class Station(
    val id: String,
    val name: String
)

object Stations {
    val allStations = listOf(
        Station("17000", "Chamartín"),
        Station("70103", "Alcalá de Henares"),
        Station("18000", "Atocha"),
        Station("18002", "Nuevos Ministerios"),
        Station("18001", "Recoletos")
    )

    val defaultOrigin = Station("70103", "Alcalá de Henares")
    val defaultDestination = Station("17000", "Chamartín")
} 