package app.amanzan.horarioscercanias.data.local.database

import androidx.room.TypeConverter
import app.amanzan.horarioscercanias.domain.model.Horario
import app.amanzan.horarioscercanias.domain.model.Peticion
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromHorarioList(value: List<Horario>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toHorarioList(value: String): List<Horario> {
        val listType = object : TypeToken<List<Horario>>() {}.type
        return gson.fromJson(value, listType)
    }

    @TypeConverter
    fun fromPeticion(value: Peticion?): String? {
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun toPeticion(value: String?): Peticion? {
        return value?.let { gson.fromJson(it, Peticion::class.java) }
    }
} 