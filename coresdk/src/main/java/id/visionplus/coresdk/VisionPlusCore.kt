package id.visionplus.coresdk

import android.content.Context
import id.visionplus.coresdk.features.config.ConfigManager
import id.visionplus.coresdk.features.config.model.CoreModuleConfig
import id.visionplus.coresdk.features.config.model.GlobalConfig
import id.visionplus.coresdk.features.device.DeviceManager
import id.visionplus.coresdk.features.device.DeviceManagerImpl
import id.visionplus.coresdk.features.device.repository.DeviceRepository
import id.visionplus.coresdk.utils.logger.DEBUG

object VisionPlusCore {

    private var configManager: ConfigManager? = null

    fun init(context: Context) {
        if (configManager == null) {
            configManager = ConfigManager(context)
        }
    }

    fun enableDebugMode() {
        DEBUG = true
    }

    fun setGlobalConfig(config: GlobalConfig) {
        configManager?.saveGlobalConfig(config) ?: throw IllegalStateException("Call VisionPlusCore.init() first!")
    }

    fun setCoreModuleConfigs(configs: List<CoreModuleConfig>) {
        configManager?.saveCoreModuleConfigs(configs) ?: throw IllegalStateException("Call VisionPlusCore.init() first!")
    }

    fun setCoreModuleConfig(config: CoreModuleConfig) {
        configManager?.saveCoreModuleConfig(config) ?: throw IllegalStateException("Call VisionPlusCore.init() first!")
    }

    fun updateToken(token: String) {
        configManager?.updateToken(token) ?: throw IllegalStateException("Call VisionPlusCore.init() first!")
    }


    fun getDeviceManager(): DeviceManager {
        configManager?.let {
            return DeviceManagerImpl(
                configManager = it,
                repository = DeviceRepository(it)
            )
        } ?: throw IllegalStateException("Call VisionPlusCore.init() first!")
    }
}