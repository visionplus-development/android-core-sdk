package id.visionplus.coresdk

import android.content.Context
import id.visionplus.coresdk.features.config.ConfigManager
import id.visionplus.coresdk.features.config.model.Config
import id.visionplus.coresdk.features.device.DeviceManager
import id.visionplus.coresdk.features.device.DeviceManagerImpl
import id.visionplus.coresdk.features.device.repository.DeviceRepository

internal var DEBUG = false


object VisionPlusCore {

    fun setConfig(context: Context, config: Config) {
        ConfigManager(context).saveConfig(config)
    }

    fun updateToken(context: Context, token: String) {
        ConfigManager(context).updateToken(token)
    }

    fun enableDebugMode() {
        DEBUG = true
    }

    fun getDeviceManager(context: Context): DeviceManager {
        val configManager = ConfigManager(context)
        return DeviceManagerImpl(
            configManager = ConfigManager(context),
            repository = DeviceRepository(configManager)
        )
    }
}