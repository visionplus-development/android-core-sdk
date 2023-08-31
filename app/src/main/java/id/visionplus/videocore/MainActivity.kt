package id.visionplus.videocore

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import id.visionplus.coresdk.VisionPlusCore
import id.visionplus.coresdk.features.device.DeviceManager
import id.visionplus.coresdk.features.device.model.ConcurrentPlayState
import id.visionplus.videocore.ui.theme.VideoCoreTheme
import java.net.SocketException

class MainActivity : ComponentActivity() {

    companion object {
        private const val TAG = "VisionPlusCore"
    }

    private var coreDeviceManager: DeviceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        VisionPlusCore.updateToken("USER_TOKEN")

        coreDeviceManager = VisionPlusCore.getDeviceManager()

        coreDeviceManager?.setOnFirstHeartbeatCallback { state ->
            when (state) {
                is ConcurrentPlayState.Ok -> {
                    // Concurrent Play Ok, user can proceed or play the video
                    Log.d(TAG, "onCreate: first heartbeat received - ok")
                }
                is ConcurrentPlayState.DeviceLimitExceeded -> {
                    // Concurrent Play exceeded, may prompt user about that
                    // and call coreDeviceManager?.stop() if needed
                    Log.d(TAG, "onCreate: first heartbeat received - device limit")
                    coreDeviceManager?.stop()
                }
                is ConcurrentPlayState.Exception -> {
                    if (state.exception is SocketException) {
                        // socket exception happen, please do something or leave it empty to do nothing
                    }

                    // another exception checking, or just leave it empty to do nothing
                    Log.d(TAG, "onCreate: first heartbeat received - exception ${state.exception}")
                }
            }
        }

        coreDeviceManager?.setOnContinuousHeartbeatCallback { state ->
            when (state) {
                is ConcurrentPlayState.Ok -> {
                    // Concurrent Play Ok, user can proceed or continue playing the video, or leave it empty to do nothing
                    Log.d(TAG, "onCreate: continuous heartbeat received - ok")
                }
                is ConcurrentPlayState.DeviceLimitExceeded -> {
                    // Concurrent Play exceeded, may prompt user about that
                    // and call coreDeviceManager?.stop() if needed
                    Log.d(TAG, "onCreate: continuous heartbeat received - device limit")
                    coreDeviceManager?.stop()
                }
                is ConcurrentPlayState.Exception -> {
                    if (state.exception is SocketException) {
                        // socket exception happen, please do something or leave it empty to do nothing
                    }

                    // another exception checking, or just leave it empty to do nothing
                    Log.d(TAG, "onCreate: continuous heartbeat received - exception ${state.exception}")
                }
            }
        }

        coreDeviceManager?.setOnStopHeartbeatCallback {
            // stop player here
            Log.d(TAG, "onCreate: heartbeat stop")
        }


        setContent {
            VideoCoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        Row {
                            DefaultButton("start", onClick = {
                                coreDeviceManager?.start()
                                Log.d(TAG, "onCreate: heartbeat start")
                            })
                        }

                        Row {
                            DefaultButton("stop", onClick = {
                                coreDeviceManager?.stop()
                                Log.d(TAG, "onCreate: heartbeat stop")
                            })
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coreDeviceManager?.stop()
    }
}

@Composable
fun DefaultButton(label: String, modifier: Modifier = Modifier, onClick: () -> Unit = {}) {
    Button(onClick = onClick) {
        Text(
            text = label,
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VideoCoreTheme {
        DefaultButton("Android")
    }
}