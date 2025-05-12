package app.amanzan.horarioscercanias.data.api

import app.amanzan.horarioscercanias.domain.model.TrainSchedule
import app.amanzan.horarioscercanias.domain.model.TrainScheduleResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TrainScheduleApi {
    @POST("cer/HorariosServlet")
    suspend fun getTrainSchedules(
        @Body request: TrainSchedule
    ): Response<TrainScheduleResponse>
} 