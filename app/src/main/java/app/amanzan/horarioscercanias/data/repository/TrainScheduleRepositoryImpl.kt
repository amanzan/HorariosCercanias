package app.amanzan.horarioscercanias.data.repository

import android.util.Log
import app.amanzan.horarioscercanias.data.api.TrainScheduleApi
import app.amanzan.horarioscercanias.domain.model.TrainSchedule
import app.amanzan.horarioscercanias.domain.model.TrainScheduleResponse
import app.amanzan.horarioscercanias.domain.repository.TrainScheduleRepository
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TrainScheduleRepositoryImpl @Inject constructor(
    private val api: TrainScheduleApi,
    private val gson: Gson
) : TrainScheduleRepository {
    override suspend fun getTrainSchedules(request: TrainSchedule): TrainScheduleResponse {
        try {
            Log.d("TrainScheduleRepo", "Making API request with: ${gson.toJson(request)}")
            val response = api.getTrainSchedules(request)
            Log.d("TrainScheduleRepo", "Response code: ${response.code()}")
            Log.d("TrainScheduleRepo", "Response headers: ${response.headers()}")
            
            if (!response.isSuccessful) {
                Log.e("TrainScheduleRepo", "Unsuccessful response: ${response.code()} - ${response.message()}")
                return TrainScheduleResponse(error = "HTTP Error: ${response.code()} - ${response.message()}")
            }

            val responseBody = response.body()
            if (responseBody == null) {
                Log.e("TrainScheduleRepo", "Response body is null")
                return TrainScheduleResponse(error = "Empty response from server")
            }

            Log.d("TrainScheduleRepo", "Response body: $responseBody")
            Log.d("TrainScheduleRepo", "Response horario size: ${responseBody.horario.size}")
            Log.d("TrainScheduleRepo", "Response peticion: ${responseBody.peticion}")
            Log.d("TrainScheduleRepo", "Response actTiempoReal: ${responseBody.actTiempoReal}")
            
            return responseBody
        } catch (e: Exception) {
            Log.e("TrainScheduleRepo", "Network error", e)
            return TrainScheduleResponse(error = "Network error: ${e.message}")
        }
    }
} 