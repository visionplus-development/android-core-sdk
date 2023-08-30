package id.visionplus.coresdk.utils.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ApiResponse<Data>(
    @SerialName("data")
    val data: Data?,
    @SerialName("status_code")
    val statusCode: Int?,
    @SerialName("message")
    val message: String?
)