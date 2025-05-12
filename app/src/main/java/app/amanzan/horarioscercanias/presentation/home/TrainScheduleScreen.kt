package app.amanzan.horarioscercanias.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.amanzan.horarioscercanias.presentation.viewmodel.TrainScheduleUiState
import app.amanzan.horarioscercanias.presentation.viewmodel.TrainScheduleViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun TrainScheduleScreen(
    viewModel: TrainScheduleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing = uiState is TrainScheduleUiState.Loading
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    SwipeRefresh(
        state = swipeRefreshState,
        onRefresh = { viewModel.loadTrainSchedules() }
    ) {
        when (uiState) {
            is TrainScheduleUiState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is TrainScheduleUiState.Success -> {
                val data = (uiState as TrainScheduleUiState.Success).data
                if (data.horario.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se encontraron horarios",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(data.horario) { horario ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                            ) {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Línea: ${horario.linea}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Tren: ${horario.cdgoTren}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Salida: ${horario.horaSalida}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Llegada: ${horario.horaLlegada}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "Duración: ${horario.duracion}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    if (horario.accesible) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Tren accesible",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
            is TrainScheduleUiState.Error -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (uiState as TrainScheduleUiState.Error).message,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
} 