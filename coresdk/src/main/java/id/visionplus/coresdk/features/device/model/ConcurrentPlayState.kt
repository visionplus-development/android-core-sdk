package id.visionplus.coresdk.features.device.model


sealed class ConcurrentPlayState {
    object Ok: ConcurrentPlayState()
    data class DeviceLimitExceeded(val message: String): ConcurrentPlayState()
    data class Exception(val exception: kotlin.Exception): ConcurrentPlayState()
}
