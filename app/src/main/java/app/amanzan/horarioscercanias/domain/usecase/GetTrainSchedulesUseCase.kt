package app.amanzan.horarioscercanias.domain.usecase

import app.amanzan.horarioscercanias.domain.model.TrainSchedule
import app.amanzan.horarioscercanias.domain.model.TrainScheduleResponse
import app.amanzan.horarioscercanias.domain.repository.TrainScheduleRepository
import javax.inject.Inject

class GetTrainSchedulesUseCase @Inject constructor(
    private val repository: TrainScheduleRepository
) {
    suspend operator fun invoke(request: TrainSchedule): Result<TrainScheduleResponse> {
        return try {
            // Validate request
            if (request.origen.isBlank() || request.destino.isBlank()) {
                return Result.failure(IllegalArgumentException("Origen y destino son requeridos"))
            }

            if (request.fchaViaje.isBlank()) {
                return Result.failure(IllegalArgumentException("Fecha de viaje es requerida"))
            }

            // Business rule: Origin and destination cannot be the same
            if (request.origen == request.destino) {
                return Result.failure(IllegalArgumentException("Origen y destino no pueden ser iguales"))
            }

            // Business rule: Validate date format (YYYYMMDD)
            if (!request.fchaViaje.matches(Regex("^\\d{8}$"))) {
                return Result.failure(IllegalArgumentException("Formato de fecha inválido. Use YYYYMMDD"))
            }

            // Business rule: Validate time range
            val horaOrigen = request.horaViajeOrigen.toIntOrNull()
            val horaLlegada = request.horaViajeLlegada.toIntOrNull()
            if (horaOrigen == null || horaLlegada == null) {
                return Result.failure(IllegalArgumentException("Horas inválidas"))
            }
            if (horaOrigen < 0 || horaLlegada > 26) {
                return Result.failure(IllegalArgumentException("Rango de horas inválido (0-26)"))
            }
            if (horaOrigen >= horaLlegada) {
                return Result.failure(IllegalArgumentException("Hora de origen debe ser menor que hora de llegada"))
            }

            // If all validations pass, fetch the data
            val response = repository.getTrainSchedules(request)
            
            // Business rule: Check if we got any schedules
            if (response.horario.isEmpty()) {
                return Result.failure(NoSuchElementException("No se encontraron horarios para la ruta especificada"))
            }

            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 