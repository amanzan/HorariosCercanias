package app.amanzan.horarioscercanias.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import app.amanzan.horarioscercanias.domain.model.Station
import app.amanzan.horarioscercanias.domain.model.Stations
import app.amanzan.horarioscercanias.presentation.viewmodel.StationSelectionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StationSelectionScreen(
    onSearchClick: (origin: Station, destination: Station) -> Unit,
    viewModel: StationSelectionViewModel = hiltViewModel()
) {
    val selectedOrigin by viewModel.selectedOrigin.collectAsState()
    val selectedDestination by viewModel.selectedDestination.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()
    var showOriginDropdown by remember { mutableStateOf(false) }
    var showDestinationDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        if (!isOnline) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFFFEB3B)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color.Black
                    )
                    Text(
                        text = "Sin conexión a internet. Los datos mostrados pueden no estar actualizados.",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Text(
            text = "Selecciona tu ruta",
            style = MaterialTheme.typography.headlineMedium
        )

        // Origin Station Dropdown
        ExposedDropdownMenuBox(
            expanded = showOriginDropdown,
            onExpandedChange = { showOriginDropdown = it }
        ) {
            OutlinedTextField(
                value = selectedOrigin.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Origen") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showOriginDropdown) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = showOriginDropdown,
                onDismissRequest = { showOriginDropdown = false }
            ) {
                Stations.allStations.forEach { station ->
                    DropdownMenuItem(
                        text = { Text(station.name) },
                        onClick = {
                            viewModel.updateOrigin(station)
                            showOriginDropdown = false
                        }
                    )
                }
            }
        }

        // Swap Button
        IconButton(
            onClick = { viewModel.swapStations() },
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.SwapVert,
                contentDescription = "Intercambiar estaciones",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        // Destination Station Dropdown
        ExposedDropdownMenuBox(
            expanded = showDestinationDropdown,
            onExpandedChange = { showDestinationDropdown = it }
        ) {
            OutlinedTextField(
                value = selectedDestination.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Destino") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = showDestinationDropdown) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = showDestinationDropdown,
                onDismissRequest = { showDestinationDropdown = false }
            ) {
                Stations.allStations.forEach { station ->
                    DropdownMenuItem(
                        text = { Text(station.name) },
                        onClick = {
                            viewModel.updateDestination(station)
                            showDestinationDropdown = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Search Button
        Button(
            onClick = { onSearchClick(selectedOrigin, selectedDestination) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Buscar horarios")
        }
    }
} 