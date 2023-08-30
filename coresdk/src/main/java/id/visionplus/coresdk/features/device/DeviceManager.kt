package id.visionplus.coresdk.features.device

import androidx.core.util.Consumer
import id.visionplus.coresdk.features.device.model.ConcurrentPlayState
import kotlinx.coroutines.Runnable

interface DeviceManager {
    /**
     * setOnFirstHeartbeatCallback triggered only for once after calling start()
     * and give you first response of heartbeat chain
     * */
    fun setOnFirstHeartbeatCallback(callback: Consumer<ConcurrentPlayState>)

    /**
     * setOnContinuousHeartbeatCallback triggered after the first response,
     * and continues to provide responses of the heartbeat chain at regular intervals,
     * as long as you don't call stop() or the instance gets killed.
     * The interval is determined by the CoreModuleConfig.Device.
     * */
    fun setOnContinuousHeartbeatCallback(callback: Consumer<ConcurrentPlayState>)

    /**
     * setOnStopHeartbeatCallback triggered only for once after calling stop()
     * */
    fun setOnStopHeartbeatCallback(callback: Runnable)

    /**
     * Start the heartbeat
     * */
    fun start()

    /**
     * Stop the heartbeat
     * */
    fun stop()
}