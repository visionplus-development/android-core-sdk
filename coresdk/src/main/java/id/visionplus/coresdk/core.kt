package id.visionplus.coresdk

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.coroutines.GlobalScope

interface CoreVideoListener {
    fun onLimitedDeviceError(code: Int, message: String)
}


class CoreVideo(
    var token: String = "",
    var deviceID: String = "",
    var intervalInSecond: Int = 5,
    var isDebug: Boolean = false,
    var limitedDeviceBaseUrl: String? = null
) {
    var listener: CoreVideoListener? = null
    private var heartbeatJob: Job? = null
    private val client = HttpClient()

    init {
        runHeartbeatEveryN()
    }

    private fun runHeartbeatEveryN() {
        heartbeatJob = GlobalScope.launch(Dispatchers.IO) {
            while (true) { // isActive is a property of CoroutineScope.
                reportLimitedDevice()
                delay(intervalInSecond.toLong() * 1000) //
            }
        }

    }

    private suspend fun reportLimitedDevice() {
        try {
            if (isDebug) {
                Log.d("CoreVideo", "Making Limited Device Report")
            }

            // TODO: Change to real url and value
            // Make a http call
            val url = limitedDeviceBaseUrl ?: "https://web-api.visionplus.id/api/v1/visitor"
            val payload = mapOf("token" to token, "deviceID" to deviceID)
            val response = client.get(url) {
                contentType(ContentType.Application.Json)
            }

            if (isDebug) {
                Log.d("CoreVideo", "reportLimitedDevice: $response")
            }
        } catch (e: Exception) {
            listener?.onLimitedDeviceError(0, e.message ?: "Unknown error")
        }
    }

    fun release() {
        heartbeatJob?.cancel()
    }


}