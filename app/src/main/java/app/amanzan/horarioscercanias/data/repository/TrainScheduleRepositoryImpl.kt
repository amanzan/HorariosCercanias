package app.amanzan.horarioscercanias.data.repository

import android.util.Log
import app.amanzan.horarioscercanias.data.api.TrainScheduleApi
import app.amanzan.horarioscercanias.data.local.database.TrainScheduleDao
import app.amanzan.horarioscercanias.data.local.database.TrainScheduleEntity
import app.amanzan.horarioscercanias.domain.model.TrainSchedule
import app.amanzan.horarioscercanias.domain.model.TrainScheduleResponse
import app.amanzan.horarioscercanias.domain.repository.TrainScheduleRepository
import com.google.gson.Gson
import java.util.Date
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainScheduleRepositoryImpl @Inject constructor(
    private val api: TrainScheduleApi,
    private val gson: Gson,
    private val dao: TrainScheduleDao
) : TrainScheduleRepository {
    override suspend fun getTrainSchedules(request: TrainSchedule): TrainScheduleResponse {
        try {
            // Try to get from network first
            val response = api.getTrainSchedules(request)
            
            if (!response.isSuccessful) {
                Log.e("TrainScheduleRepo", "Unsuccessful response: ${response.code()} - ${response.message()}")
                return TrainScheduleResponse(error = "HTTP Error: ${response.code()} - ${response.message()}")
            }

            val responseBody = response.body()
            if (responseBody == null) {
                Log.e("TrainScheduleRepo", "Response body is null")
                return TrainScheduleResponse(error = "Empty response from server")
            }
            
            // Cache the response
            val entity = TrainScheduleEntity(
                id = "${request.origen}_${request.destino}_${request.fchaViaje}",
                originCode = request.origen,
                destinationCode = request.destino,
                date = request.fchaViaje,
                lastUpdated = Date(),
                horarios = responseBody.horario,
                peticion = responseBody.peticion
            )
            dao.insertTrainSchedule(entity)
            
            return responseBody
        } catch (e: Exception) {
            Log.e("TrainScheduleRepo", "Network error", e)
            // If network fails, try to get from cache
            val cachedSchedule = dao.getTrainSchedule(
                request.origen,
                request.destino,
                request.fchaViaje
            )
            
            return if (cachedSchedule != null) {
                TrainScheduleResponse(
                    actTiempoReal = false,
                    peticion = cachedSchedule.peticion,
                    horario = cachedSchedule.horarios
                )
            } else {
                TrainScheduleResponse(error = "No se encontraron horarios para la ruta especificada.")
            }
        }
    }

    // Clean up old schedules (older than 24 hours)
    suspend fun cleanupOldSchedules() {
        val cutoffDate = Date(System.currentTimeMillis() - TimeUnit.HOURS.toMillis(24))
        dao.deleteOldSchedules(cutoffDate)
    }
} 