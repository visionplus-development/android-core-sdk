package id.visionplus.coresdk

import android.util.Log
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
    }
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        runHeartbeatEveryN()
    }

    private fun runHeartbeatEveryN() {
        heartbeatJob = coroutineScope.launch {
            while (true) {
                reportLimitedDevice()
                delay(intervalInSecond.toLong() * 1000)
            }
        }
    }

    private suspend fun reportLimitedDevice() {
        try {
            if (isDebug) {
                Log.d("CoreVideo", "Making Limited Device Report")
            }

            val url = limitedDeviceBaseUrl ?: "https://web-api.visionplus.id/api/v1/visitor"
            val response: ApiResponse = client.get(url) {
                headers {
                    append("device-id", deviceID)
                    append("Authorization", token)
                    contentType(ContentType.Application.Json)
                }
            }.body()

            if(response.statusCode != 200){
                listener?.onLimitedDeviceError(response.statusCode, response.message)
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
        coroutineScope.cancel() // Cancel the CoroutineScope when releasing the CoreVideo instance
    }
}

@Serializable
data class UserData(
    @SerialName("user_type")
    val userType: String,
    @SerialName("max_play")
    val maxPlay: Int
)

@Serializable
data class ApiResponse(
    @SerialName("data")
    val data: UserData,
    @SerialName("status_code")
    val statusCode: Int,
    @SerialName("message")
    val message: String
)