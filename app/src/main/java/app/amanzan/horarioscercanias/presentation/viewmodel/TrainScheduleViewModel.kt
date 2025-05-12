package app.amanzan.horarioscercanias.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.amanzan.horarioscercanias.domain.model.TrainSchedule
import app.amanzan.horarioscercanias.domain.model.TrainScheduleResponse
import app.amanzan.horarioscercanias.domain.usecase.GetTrainSchedulesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrainScheduleViewModel @Inject constructor(
    private val getTrainSchedulesUseCase: GetTrainSchedulesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<TrainScheduleUiState>(TrainScheduleUiState.Loading)
    val uiState: StateFlow<TrainScheduleUiState> = _uiState.asStateFlow()

    init {
        Log.d("TrainScheduleViewModel", "ViewModel initialized")
        loadTrainSchedules()
    }

    fun loadTrainSchedules() {
        Log.d("TrainScheduleViewModel", "Loading train schedules...")
        viewModelScope.launch {
            _uiState.value = TrainScheduleUiState.Loading
            try {
                val request = TrainSchedule()
                Log.d("TrainScheduleViewModel", "Making request with: $request")
                
                getTrainSchedulesUseCase(request)
                    .onSuccess { response ->
                        Log.d("TrainScheduleViewModel", "Success response: $response")
                        _uiState.value = TrainScheduleUiState.Success(response)
                    }
                    .onFailure { error ->
                        Log.e("TrainScheduleViewModel", "Error in response: ${error.message}")
                        _uiState.value = TrainScheduleUiState.Error(error.message ?: "Error desconocido")
                    }
            } catch (e: Exception) {
                Log.e("TrainScheduleViewModel", "Exception occurred", e)
                _uiState.value = TrainScheduleUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}

sealed interface TrainScheduleUiState {
    data object Loading : TrainScheduleUiState
    data class Success(val data: TrainScheduleResponse) : TrainScheduleUiState
    data class Error(val message: String) : TrainScheduleUiState
} 