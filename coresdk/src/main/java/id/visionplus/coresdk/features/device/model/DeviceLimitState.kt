package id.visionplus.coresdk.features.device.model


sealed class DeviceLimitState {
    object Ok: DeviceLimitState()
    data class Exceeded(val message: String): DeviceLimitState()
    data class Exception(val exception: kotlin.Exception): DeviceLimitState()
}
