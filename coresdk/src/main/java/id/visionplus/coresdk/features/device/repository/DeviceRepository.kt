package id.visionplus.coresdk.features.device.repository

import id.visionplus.coresdk.features.config.ConfigManager
import id.visionplus.coresdk.features.device.model.DeviceLimitState
import id.visionplus.coresdk.features.device.model.response.DeviceLimitResponse
import id.visionplus.coresdk.utils.logger.Logger
import id.visionplus.coresdk.utils.network.model.ApiResponse
import id.visionplus.coresdk.utils.orGeneraError
import id.visionplus.coresdk.utils.orUnknownError
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

internal class DeviceRepository(
    private val configManager: ConfigManager
) {

    companion object {
        private const val TAG = "DeviceRepo"
    }

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun checkDeviceLimit(): DeviceLimitState {
        try {
            Logger.debug(TAG, "Making Limited Device Report")

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
                val body = response.body<ApiResponse<DeviceLimitResponse>>()
                Logger.debug(TAG, "reportLimitedDevice: $body")
                return when (body.statusCode) {
                    403 -> DeviceLimitState.Exceeded(body.message.orUnknownError())
                    else -> DeviceLimitState.Ok
                }
            }

            val errorBody: String = response.bodyAsText()
            val errorResponse = Json.decodeFromString(ApiResponse.serializer(DeviceLimitResponse.serializer()), errorBody)

            return when (response.status) {
                HttpStatusCode.Forbidden -> {
                    DeviceLimitState.Exceeded(errorResponse.message.orGeneraError())
                }
                else -> {
                    DeviceLimitState.Ok
                }
            }
        } catch (e: Exception) {
            Logger.error(TAG, e.message.orEmpty())
            return DeviceLimitState.Exception(e)
        }
    }
}