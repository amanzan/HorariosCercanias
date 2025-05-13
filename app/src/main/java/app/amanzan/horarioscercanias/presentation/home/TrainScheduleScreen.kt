package app.amanzan.horarioscercanias.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.amanzan.horarioscercanias.domain.model.Station
import app.amanzan.horarioscercanias.presentation.viewmodel.TrainScheduleUiState
import app.amanzan.horarioscercanias.presentation.viewmodel.TrainScheduleViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrainScheduleScreen(
    origin: Station,
    destination: Station,
    onBackClick: () -> Unit,
    viewModel: TrainScheduleViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing = uiState is TrainScheduleUiState.Loading
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing)

    LaunchedEffect(origin, destination) {
        viewModel.loadTrainSchedules(origin, destination)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "${origin.name} → ${destination.name}",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Horarios de trenes",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { viewModel.loadTrainSchedules(origin, destination) },
            modifier = Modifier.padding(paddingValues)
        ) {
            when (uiState) {
                is TrainScheduleUiState.Initial -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Selecciona una ruta para ver los horarios",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Table Header
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Línea",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Start
                            )
                            Text(
                                text = "Salida",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Llegada",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Duración",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }

                        // Table Content
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(data.horario) { horario ->
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (horario.civis == "CIVIS") 
                                            Color(0xFFFFEB3B) // Yellow background for CIVIS trains
                                        else 
                                            MaterialTheme.colorScheme.surface
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 16.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = horario.linea,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Start
                                        )
                                        Text(
                                            text = horario.horaSalida,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = horario.horaLlegada,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.Center
                                        )
                                        Text(
                                            text = horario.duracion,
                                            style = MaterialTheme.typography.bodyMedium,
                                            modifier = Modifier.weight(1f),
                                            textAlign = TextAlign.End
                                        )
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
} 