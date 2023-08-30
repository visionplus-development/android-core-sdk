package id.visionplus.videocore

import android.app.Application
import id.visionplus.coresdk.VisionPlusCore
import id.visionplus.coresdk.features.config.model.GlobalConfig
import id.visionplus.coresdk.features.config.model.CoreModuleConfig

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        setupVisionPlusCoreSdk()
    }

    private fun setupVisionPlusCoreSdk() {
        if (BuildConfig.DEBUG) {
            VisionPlusCore.enableDebugMode()
        }

        /*
        * it is not required to define this config in .App
        * we can also define it anywhere
        * */
        VisionPlusCore.setCoreModuleConfig(
            context = this,
            config = CoreModuleConfig.Device(
                heartbeatIntervalMs = 5000, // 5 sec
                url = "" // full url
            )
        )

        /*
        * it is not required to define this config in .App
        * we can also define it anywhere
        * */
        VisionPlusCore.setGlobalConfig(
            context = this,
            config = GlobalConfig(
                deviceId = "", // required
                token = "", // we can define token later
            )
        )
    }
}