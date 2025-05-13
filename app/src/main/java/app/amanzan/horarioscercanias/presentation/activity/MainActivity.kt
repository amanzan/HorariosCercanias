package app.amanzan.horarioscercanias.presentation.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import app.amanzan.horarioscercanias.domain.model.Station
import app.amanzan.horarioscercanias.presentation.home.StationSelectionScreen
import app.amanzan.horarioscercanias.presentation.home.TrainScheduleScreen
import app.amanzan.horarioscercanias.presentation.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var selectedOrigin by remember { mutableStateOf<Station?>(null) }
                    var selectedDestination by remember { mutableStateOf<Station?>(null) }

                    if (selectedOrigin != null && selectedDestination != null) {
                        TrainScheduleScreen(
                            origin = selectedOrigin!!,
                            destination = selectedDestination!!,
                            onBackClick = {
                                selectedOrigin = null
                                selectedDestination = null
                            }
                        )
                    } else {
                        StationSelectionScreen(
                            onSearchClick = { origin, destination ->
                                selectedOrigin = origin
                                selectedDestination = destination
                            }
                        )
                    }
                }
            }
        }
    }
} 