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

@Deprecated(message = "Not being maintained anymore", replaceWith = ReplaceWith(""), level = DeprecationLevel.WARNING)
interface CoreVideoListener {
    fun onFirstHeartbeatReceived(code: Int, message: String) // success: start player, failure: show message limit
    fun onHeartbeatReceived(code: Int, message: String) // success: do nothing, failure: show message limit
}

@Deprecated(message = "Not being maintained anymore", replaceWith = ReplaceWith(""), level = DeprecationLevel.WARNING)
class CoreVideo(
    var token: String = "",
    var deviceID: String = "",
    var intervalInSecond: Int = 5,
    var isDebug: Boolean = false,
    var limitedDeviceBaseUrl: String? = null
) {
    var listener: CoreVideoListener? = null
    private var heartbeatJob: Job? = null
    private var heartbeatCount: Long = 0
    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
    }
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    private fun runHeartbeatEveryN() {
        if (heartbeatJob != null) {
            return
        }
        heartbeatJob = coroutineScope.launch {
            while (true) {
                reportLimitedDevice(heartbeatCount)
                delay(intervalInSecond.toLong() * 1000)
            }
        }
    }

    private suspend fun reportLimitedDevice(heartbeatCount: Long) {
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

            handleHeartbeat(heartbeatCount, response.statusCode ?: 0, response.message ?: "Unknown error")

            if (isDebug) {
                Log.d("CoreVideo", "reportLimitedDevice: $response")
            }
        } catch (e: Exception) {
            handleHeartbeat(heartbeatCount, 0, e.message ?: "Unknown error")
        }
    }

    private fun handleHeartbeat(heartbeatCount: Long, code: Int, message: String) {
        if (heartbeatCount < 1) {
            listener?.onFirstHeartbeatReceived(code, message)
        } else {
            listener?.onHeartbeatReceived(code, message)
        }
    }

    fun stop() {
        heartbeatJob?.cancel()
        coroutineScope.cancel() // Cancel the CoroutineScope when releasing the CoreVideo instance
        heartbeatCount = 0
    }

    fun start() {
        runHeartbeatEveryN()
    }
}
@Deprecated(message = "Not being maintained anymore", replaceWith = ReplaceWith("id.visionplus.coresdk.features.device.model.response.ConcurrentPlayResponse"), level = DeprecationLevel.WARNING)
@Serializable
data class UserData(
    @SerialName("user_type")
    val userType: String?,
    @SerialName("max_play")
    val maxPlay: Int?
)

@Deprecated(message = "Not being maintained anymore", replaceWith = ReplaceWith("id.visionplus.coresdk.utils.network.model.ApiResponse"), level = DeprecationLevel.WARNING)
@Serializable
data class ApiResponse(
    @SerialName("data")
    val data: UserData?,
    @SerialName("status_code")
    val statusCode: Int?,
    @SerialName("message")
    val message: String?
)