package app.amanzan.horarioscercanias.domain.repository

import app.amanzan.horarioscercanias.domain.model.TrainSchedule
import app.amanzan.horarioscercanias.domain.model.TrainScheduleResponse

interface TrainScheduleRepository {
    suspend fun getTrainSchedules(request: TrainSchedule): TrainScheduleResponse
} 