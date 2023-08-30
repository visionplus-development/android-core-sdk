package id.visionplus.coresdk.features.device

import androidx.core.util.Consumer
import id.visionplus.coresdk.features.device.model.ConcurrentPlayState

interface DeviceManager {
    /**
     * setOnFirstHeartbeatReceived triggered only for once after calling start()
     * and give you first response of heartbeat chain
     * */
    fun setOnFirstHeartbeatReceived(callback: Consumer<ConcurrentPlayState>)

    /**
     * setOnContinuousHeartbeatReceived triggered after the first response,
     * and continue give you response of heartbeat chain every interval time
     * as long as you dont call stop() or instance get killed
     * */
    fun setOnContinuousHeartbeatReceived(callback: Consumer<ConcurrentPlayState>)

    /**
     * Start the heartbeat
     * */
    fun start()

    /**
     * Stop the heartbeat
     * */
    fun stop()
}