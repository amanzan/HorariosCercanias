package app.amanzan.horarioscercanias.domain.model

data class TrainScheduleResponse(
    val actTiempoReal: Boolean = false,
    val peticion: Peticion? = null,
    val horario: List<Horario> = emptyList(),
    val error: String? = null
)

data class Peticion(
    val cdgoEstOrigen: String = "",
    val cdgoEstDestino: String = "",
    val fchaViaje: String = "",
    val horaDesde: String = "",
    val horaHasta: String = "",
    val descEstOrigen: String = "",
    val descEstDestino: String = ""
)

data class Horario(
    val linea: String = "",
    val lineaEstOrigen: String = "",
    val lineaEstDestino: String = "",
    val cdgoTren: String = "",
    val horaSalida: String = "",
    val horaLlegada: String = "",
    val duracion: String = "",
    val accesible: Boolean = false
) 