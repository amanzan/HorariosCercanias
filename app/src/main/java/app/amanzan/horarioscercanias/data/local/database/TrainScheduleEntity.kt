package app.amanzan.horarioscercanias.data.local.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import app.amanzan.horarioscercanias.domain.model.Horario
import app.amanzan.horarioscercanias.domain.model.Peticion
import java.util.Date

@Entity(tableName = "train_schedules")
data class TrainScheduleEntity(
    @PrimaryKey
    val id: String, // Will be a combination of origin, destination, and date
    val originCode: String,
    val destinationCode: String,
    val date: String,
    val lastUpdated: Date,
    val horarios: List<Horario>,
    val peticion: Peticion?
)

@androidx.room.Dao
interface TrainScheduleDao {
    @androidx.room.Query("SELECT * FROM train_schedules WHERE originCode = :originCode AND destinationCode = :destinationCode AND date = :date")
    suspend fun getTrainSchedule(originCode: String, destinationCode: String, date: String): TrainScheduleEntity?

    @androidx.room.Insert(onConflict = androidx.room.OnConflictStrategy.REPLACE)
    suspend fun insertTrainSchedule(trainSchedule: TrainScheduleEntity)

    @androidx.room.Query("DELETE FROM train_schedules WHERE lastUpdated < :date")
    suspend fun deleteOldSchedules(date: Date)
} 