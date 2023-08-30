package id.visionplus.coresdk.features.device

import id.visionplus.coresdk.features.device.model.DeviceLimitState

interface DeviceManager {
    /**
     * setOnFirstHeartbeatReceived triggered only for once after calling start()
     * and give you first response of heartbeat chain
     * */
    fun setOnFirstHeartbeatReceived(callback: (DeviceLimitState) -> Unit)

    /**
     * setOnContinuousHeartbeatReceived triggered after the first response,
     * and continue give you response of heartbeat chain every interval time
     * as long as you dont call stop() or instance get killed
     * */
    fun setOnContinuousHeartbeatReceived(callback: (DeviceLimitState) -> Unit)

    /**
     * Start the heartbeat
     * */
    fun start()

    /**
     * Stop the heartbeat
     * */
    fun stop()
}