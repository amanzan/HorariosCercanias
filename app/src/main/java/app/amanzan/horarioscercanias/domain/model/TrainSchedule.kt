package app.amanzan.horarioscercanias.domain.model

import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class TrainSchedule(
    val nucleo: String = "10",
    val origen: String = Stations.defaultOrigin.id,
    val destino: String = Stations.defaultDestination.id,
    val fchaViaje: String = getTodayDate(),
    val horaViajeOrigen: String = "00",
    val horaViajeLlegada: String = "26",
    val validaReglaNegocio: Boolean = true,
    val tiempoReal: Boolean = true,
    val servicioHorarios: String = "VTI",
    val accesibilidadTrenes: Boolean = true,
    val cp: String = "NO",
    val i: String = "s"
) {
    companion object {
        private fun getTodayDate(): String {
            return LocalDate.now(ZoneId.of("Europe/Madrid"))
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"))
        }

        fun fromStations(origin: Station, destination: Station) = TrainSchedule(
            origen = origin.id,
            destino = destination.id
        )
    }
} 