package id.visionplus.coresdk.features.device

import androidx.core.util.Consumer
import id.visionplus.coresdk.features.config.ConfigManager
import id.visionplus.coresdk.features.device.model.ConcurrentPlayState
import id.visionplus.coresdk.features.device.repository.DeviceRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.Runnable

internal class DeviceManagerImpl(
    private val configManager: ConfigManager,
    private val repository: DeviceRepository
): DeviceManager {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var heartbeatJob: Job? = null

    private var onFirstHeartbeatCallback: Consumer<ConcurrentPlayState>? = null
    private var onContinuousHeartbeatCallback: Consumer<ConcurrentPlayState>? = null
    private var onStopHeartbeatCallback: Runnable? = null

    override fun setOnFirstHeartbeatCallback(callback: Consumer<ConcurrentPlayState>) {
        onFirstHeartbeatCallback = callback
    }

    override fun setOnContinuousHeartbeatCallback(callback: Consumer<ConcurrentPlayState>) {
        onContinuousHeartbeatCallback = callback
    }

    override fun setOnStopHeartbeatCallback(callback: Runnable) {
        onStopHeartbeatCallback = callback
    }

    override fun start() {
        if (heartbeatJob?.isActive == true) {
            return
        }

        val interval = configManager.getDeviceConfig().heartbeatIntervalMs

        heartbeatJob = coroutineScope.launch {
            /* First Heartbeat */
            var state = repository.checkConcurrentPlay()
            onFirstHeartbeatCallback?.accept(state)
            delay(interval)

            /* Continuous Heartbeat */
            while (true) {
                state = repository.checkConcurrentPlay()
                onContinuousHeartbeatCallback?.accept(state)
                delay(interval)
            }
        }
    }

    override fun stop() {
        heartbeatJob?.cancel()
        onStopHeartbeatCallback?.run()
    }
}