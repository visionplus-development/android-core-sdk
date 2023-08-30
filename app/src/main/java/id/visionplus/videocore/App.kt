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

        VisionPlusCore.init(this)

        /*
        * it is not required to define this config in .App
        * we can also define it anywhere
        * */
        VisionPlusCore.setCoreModuleConfig(
            CoreModuleConfig.Device(
                heartbeatIntervalMs = 5000, // 5 sec
                url = "https://stag2-device.visionplus.id/api/v1/device-management/concurrent-play" // full url
            )
        )

        /*
        * it is not required to define this config in .App
        * we can also define it anywhere
        * */
        VisionPlusCore.setGlobalConfig(
            config = GlobalConfig(
                deviceId = "38c309dbb175333f8f47a78fc2317dc5", // required
                token = "", // we can define token later
            )
        )
    }
}