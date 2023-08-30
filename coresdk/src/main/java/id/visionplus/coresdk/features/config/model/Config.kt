package id.visionplus.coresdk.features.config.model

data class Config(
    val deviceId: String,
    val token: String = "",
    val modules: List<CoreModule> = mutableListOf()
)

sealed class CoreModule {
    data class Device(
        val heartbeatIntervalMs: Long = 60000,
        val url: String = ""
    ): CoreModule()
}
