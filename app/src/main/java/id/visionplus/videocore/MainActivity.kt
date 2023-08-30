package id.visionplus.videocore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import id.visionplus.coresdk.VisionPlusCore
import id.visionplus.coresdk.features.device.DeviceManager
import id.visionplus.coresdk.features.device.model.DeviceLimitState
import id.visionplus.videocore.ui.theme.VideoCoreTheme
import java.net.SocketException

class MainActivity : ComponentActivity() {

    private var coreDeviceManager: DeviceManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        VisionPlusCore.updateToken(this, "")

        coreDeviceManager = VisionPlusCore.getDeviceManager(this)

        coreDeviceManager?.setOnFirstHeartbeatReceived { state ->
            when (state) {
                is DeviceLimitState.Ok -> {
                    // Device Limit Ok, user can proceed or play the video
                }
                is DeviceLimitState.Exceeded -> {
                    // Device limit exceeded, may prompt user about that
                }
                is DeviceLimitState.Exception -> {
                    if (state.exception is SocketException) {
                        // socket exception happen, please do something or leave it empty to do nothing
                    }

                    // another exception checking, or just leave it empty to do nothing
                }
            }
        }

        coreDeviceManager?.setOnContinuousHeartbeatReceived { state ->
            when (state) {
                is DeviceLimitState.Ok -> {
                    // Device Limit Ok, user can proceed or continue playing the video, or leave it empty to do nothing
                }
                is DeviceLimitState.Exceeded -> {
                    // Device limit exceeded, may prompt user about that
                }
                is DeviceLimitState.Exception -> {
                    if (state.exception is SocketException) {
                        // socket exception happen, please do something or leave it empty to do nothing
                    }

                    // another exception checking, or just leave it empty to do nothing
                }
            }
        }

        coreDeviceManager?.start()


        setContent {
            VideoCoreTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
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
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    VideoCoreTheme {
        Greeting("Android")
    }
}