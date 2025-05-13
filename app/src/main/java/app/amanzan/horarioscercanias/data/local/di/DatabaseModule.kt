/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.amanzan.horarioscercanias.data.local.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import app.amanzan.horarioscercanias.data.local.database.AppDatabase
import app.amanzan.horarioscercanias.data.local.database.TrainDao
import app.amanzan.horarioscercanias.data.local.database.TrainScheduleDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideTrainDao(appDatabase: AppDatabase): TrainDao {
        return appDatabase.trainDao()
    }

    @Provides
    fun provideTrainScheduleDao(appDatabase: AppDatabase): TrainScheduleDao {
        return appDatabase.trainScheduleDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "Train"
        )
        .addMigrations(MIGRATION_1_2)
        .build()
    }

    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Create the train_schedules table
            database.execSQL("""
                CREATE TABLE IF NOT EXISTS `train_schedules` (
                    `id` TEXT NOT NULL,
                    `originCode` TEXT NOT NULL,
                    `destinationCode` TEXT NOT NULL,
                    `date` TEXT NOT NULL,
                    `lastUpdated` INTEGER NOT NULL,
                    `horarios` TEXT NOT NULL,
                    `peticion` TEXT,
                    PRIMARY KEY(`id`)
                )
            """)
        }
    }
}
