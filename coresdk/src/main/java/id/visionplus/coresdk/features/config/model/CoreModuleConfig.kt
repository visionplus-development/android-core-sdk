package id.visionplus.coresdk.features.config.model

sealed class CoreModuleConfig {
    data class Device(
        val heartbeatIntervalMs: Long = 60000,
        val url: String = ""
    ): CoreModuleConfig()
}