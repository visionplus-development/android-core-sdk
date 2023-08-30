package id.visionplus.coresdk.features.config

import android.annotation.SuppressLint
import android.content.Context
import id.visionplus.coresdk.features.config.model.CoreModuleConfig
import id.visionplus.coresdk.features.config.model.GlobalConfig

internal class ConfigManager(context: Context) {

    companion object {
        private const val DEVICE_ID = "device_id"
        private const val TOKEN = "token"

        private const val MODULE_DEVICE_URL = "module_device_url"
        private const val MODULE_DEVICE_HEARTBEAT_INTERVAL = "module_device_heartbeat_interval"
    }

    private val prefs = context.getSharedPreferences("core_config", Context.MODE_PRIVATE)

    val token: String
        get() = prefs.getString(TOKEN, "").orEmpty()

    val deviceId: String
        get() = prefs.getString(DEVICE_ID, "").orEmpty()

    fun getDeviceConfig(): CoreModuleConfig.Device {
        return CoreModuleConfig.Device(
            heartbeatIntervalMs = prefs.getLong(MODULE_DEVICE_HEARTBEAT_INTERVAL, 60000),
            url = prefs.getString(MODULE_DEVICE_URL, "").orEmpty()
        )
    }

    fun saveCoreModuleConfigs(configs: List<CoreModuleConfig>) {
        configs.forEach { config ->
            if (config is CoreModuleConfig.Device) {
                prefs.edit()
                    .putString(MODULE_DEVICE_URL, config.url)
                    .putLong(MODULE_DEVICE_HEARTBEAT_INTERVAL, config.heartbeatIntervalMs)
                    .apply()
            }
        }
    }

    fun saveCoreModuleConfig(config: CoreModuleConfig) {
        when (config) {
            is CoreModuleConfig.Device -> {
                prefs.edit()
                    .putString(MODULE_DEVICE_URL, config.url)
                    .putLong(MODULE_DEVICE_HEARTBEAT_INTERVAL, config.heartbeatIntervalMs)
                    .apply()
            }
        }
    }

    fun saveGlobalConfig(config: GlobalConfig) {
        prefs.edit()
            .putString(TOKEN, config.token)
            .putString(DEVICE_ID, config.deviceId)
            .apply()
    }

    /**
     * Using apply() is a good practice to ensure that changes to shared preferences are applied asynchronously.
     * However, for important data like tokens, we might want to use commit() instead to make sure that
     * the data is immediately saved and available for retrieval.
     * For less critical data like configuration settings, apply() is appropriate.
     * */
    @SuppressLint("ApplySharedPref")
    fun updateToken(token: String) {
        prefs.edit().putString(TOKEN, token).commit()
    }

}