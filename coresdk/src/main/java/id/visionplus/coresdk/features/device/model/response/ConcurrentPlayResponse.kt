package id.visionplus.coresdk.features.device.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ConcurrentPlayResponse (
    @SerialName("user_type")
    val userType: String?,
    @SerialName("max_play")
    val maxPlay: Int?
)