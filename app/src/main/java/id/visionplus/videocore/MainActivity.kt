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
import id.visionplus.coresdk.CoreVideo
import id.visionplus.coresdk.CoreVideoListener
import id.visionplus.videocore.ui.theme.VideoCoreTheme

class MainActivity : ComponentActivity() {
    lateinit var c: CoreVideo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        c = CoreVideo(
            token = "",
            deviceID = "",
            intervalInSecond = 60,
            isDebug = false,
            limitedDeviceBaseUrl = ""
        )


        c.listener = object : CoreVideoListener {
            override fun onFirstHeartbeatReceived(code: Int, message: String) {
                when (code) {
                    403 -> {
                        // tendang user
                    }
                    else -> {
                        // play player
                    }
                }
            }

            override fun onHeartbeatReceived(code: Int, message: String) {
                when (code) {
                    403 -> {
                        // tendang user
                    }
                    else -> {
                        // do nothing
                    }
                }
            }
        }

        c.start()
        c.stop()

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
        c.stop()
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