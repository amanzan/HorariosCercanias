package app.amanzan.horarioscercanias.presentation.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.ViewModel
import app.amanzan.horarioscercanias.domain.model.Station
import app.amanzan.horarioscercanias.domain.model.Stations
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class StationSelectionViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _selectedOrigin = MutableStateFlow(Stations.defaultOrigin)
    val selectedOrigin: StateFlow<Station> = _selectedOrigin.asStateFlow()

    private val _selectedDestination = MutableStateFlow(Stations.defaultDestination)
    val selectedDestination: StateFlow<Station> = _selectedDestination.asStateFlow()

    private val _isOnline = MutableStateFlow(true)
    val isOnline: StateFlow<Boolean> = _isOnline.asStateFlow()

    init {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        
        // Check initial state
        _isOnline.value = isNetworkAvailable(connectivityManager)

        // Register network callback
        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                _isOnline.value = true
            }

            override fun onLost(network: Network) {
                _isOnline.value = false
            }
        }

        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun isNetworkAvailable(connectivityManager: ConnectivityManager): Boolean {
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

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