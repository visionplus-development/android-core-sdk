package id.visionplus.videocore

import android.app.Application
import id.visionplus.coresdk.VisionPlusCore
import id.visionplus.coresdk.features.config.model.Config
import id.visionplus.coresdk.features.config.model.CoreModule

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        setupVisionPlusCoreSdk()
    }

    private fun setupVisionPlusCoreSdk() {
        if (BuildConfig.DEBUG) {
            VisionPlusCore.enableDebugMode()
        }

        val deviceModule = CoreModule.Device(
            heartbeatIntervalMs = 5000, // 5 sec
            url = "" // full url
        )

        /*
        * it is not required to define this config in .App
        * we can also define it anywhere
        * */
        VisionPlusCore.setConfig(
            context = this,
            config = Config(
                deviceId = "", // required
                token = "", // we can define token later
                modules = listOf(
                    deviceModule
                )
            )
        )
    }
}