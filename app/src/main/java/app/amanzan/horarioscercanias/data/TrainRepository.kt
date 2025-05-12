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

package app.amanzan.horarioscercanias.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import app.amanzan.horarioscercanias.data.local.database.Train
import app.amanzan.horarioscercanias.data.local.database.TrainDao
import javax.inject.Inject

interface TrainRepository {
    val trains: Flow<List<String>>

    suspend fun add(name: String)
}

class DefaultTrainRepository @Inject constructor(
    private val trainDao: TrainDao
) : TrainRepository {

    override val trains: Flow<List<String>> =
        trainDao.getTrains().map { items -> items.map { it.name } }

    override suspend fun add(name: String) {
        trainDao.insertTrain(Train(name = name))
    }
}
