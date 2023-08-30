package id.visionplus.coresdk.features.device.repository

import android.util.Log
import id.visionplus.coresdk.features.config.ConfigManager
import id.visionplus.coresdk.features.device.model.ConcurrentPlayState
import id.visionplus.coresdk.features.device.model.response.ConcurrentPlayResponse
import id.visionplus.coresdk.utils.logger.DEBUG
import id.visionplus.coresdk.utils.logger.Logger
import id.visionplus.coresdk.utils.network.model.ApiResponse
import id.visionplus.coresdk.utils.orGeneraError
import id.visionplus.coresdk.utils.orUnknownError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.Logger as KtorLogger

internal class DeviceRepository(
    private val configManager: ConfigManager
) {

    companion object {
        private const val TAG = "VisionPlusCore"
    }

    private val client = HttpClient(Android) {
        if (DEBUG) {
            install(Logging) {
                logger = object : KtorLogger {
                    override fun log(message: String) {
                        Logger.debug(TAG, message)
                    }
                }
                level = LogLevel.ALL
            }
        }

        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun checkConcurrentPlay(): ConcurrentPlayState {
        try {
            val deviceConfig = configManager.getDeviceConfig()
            val url = deviceConfig.url

            val response = client.get(url) {
                headers {
                    append("device-id", configManager.deviceId)
                    append("Authorization", configManager.token)
                    contentType(ContentType.Application.Json)
                }
            }

            if (response.status.isSuccess()) {
                val body = response.body<ApiResponse<ConcurrentPlayResponse>>()
                return when (body.statusCode) {
                    403 -> ConcurrentPlayState.DeviceLimitExceeded(body.message.orUnknownError())
                    else -> ConcurrentPlayState.Ok
                }
            }

            val errorBody: String = response.bodyAsText()
            val errorResponse = Json.decodeFromString(ApiResponse.serializer(ConcurrentPlayResponse.serializer()), errorBody)

            return when (response.status) {
                HttpStatusCode.Forbidden -> {
                    ConcurrentPlayState.DeviceLimitExceeded(errorResponse.message.orGeneraError())
                }
                else -> {
                    ConcurrentPlayState.Ok
                }
            }
        } catch (e: Exception) {
            Logger.error(TAG, e.message.orEmpty())
            return ConcurrentPlayState.Exception(e)
        }
    }
}