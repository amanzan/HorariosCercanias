package app.amanzan.horarioscercanias.domain.model

data class TrainSchedule(
    val nucleo: String = "10",
    val origen: String = "17000",
    val destino: String = "70103",
    val fchaViaje: String = "20250512",
    val horaViajeOrigen: String = "00",
    val horaViajeLlegada: String = "26",
    val validaReglaNegocio: Boolean = true,
    val tiempoReal: Boolean = true,
    val servicioHorarios: String = "VTI",
    val accesibilidadTrenes: Boolean = true,
    val cp: String = "NO",
    val i: String = "s"
) 