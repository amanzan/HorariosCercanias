package app.amanzan.horarioscercanias.presentation.viewmodel

import androidx.lifecycle.ViewModel
import app.amanzan.horarioscercanias.domain.model.Station
import app.amanzan.horarioscercanias.domain.model.Stations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StationSelectionViewModel @Inject constructor() : ViewModel() {
    private val _selectedOrigin = MutableStateFlow(Stations.defaultOrigin)
    val selectedOrigin: StateFlow<Station> = _selectedOrigin.asStateFlow()

    private val _selectedDestination = MutableStateFlow(Stations.defaultDestination)
    val selectedDestination: StateFlow<Station> = _selectedDestination.asStateFlow()

    fun updateOrigin(station: Station) {
        _selectedOrigin.value = station
    }

    fun updateDestination(station: Station) {
        _selectedDestination.value = station
    }

    fun swapStations() {
        val temp = _selectedOrigin.value
        _selectedOrigin.value = _selectedDestination.value
        _selectedDestination.value = temp
    }
} 