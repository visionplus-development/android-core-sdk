package id.visionplus.coresdk

import android.content.Context
import id.visionplus.coresdk.features.config.ConfigManager
import id.visionplus.coresdk.features.config.model.CoreModuleConfig
import id.visionplus.coresdk.features.config.model.GlobalConfig
import id.visionplus.coresdk.features.device.DeviceManager
import id.visionplus.coresdk.features.device.DeviceManagerImpl
import id.visionplus.coresdk.features.device.repository.DeviceRepository

internal var DEBUG = false


object VisionPlusCore {

    fun setGlobalConfig(context: Context, config: GlobalConfig) {
        ConfigManager(context).saveGlobalConfig(config)
    }

    fun setCoreModuleConfigs(context: Context, configs: List<CoreModuleConfig>) {
        ConfigManager(context).saveCoreModuleConfigs(configs)
    }

    fun setCoreModuleConfig(context: Context, config: CoreModuleConfig) {
        ConfigManager(context).saveCoreModuleConfig(config)
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