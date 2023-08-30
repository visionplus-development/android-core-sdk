package id.visionplus.coresdk.features.device

import androidx.core.util.Consumer
import id.visionplus.coresdk.features.config.ConfigManager
import id.visionplus.coresdk.features.device.model.ConcurrentPlayState
import id.visionplus.coresdk.features.device.repository.DeviceRepository
import kotlinx.coroutines.*

internal class DeviceManagerImpl(
    private val configManager: ConfigManager,
    private val repository: DeviceRepository
): DeviceManager {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var heartbeatJob: Job? = null

    private var onFirstHeartbeatReceived: Consumer<ConcurrentPlayState>? = null
    private var onContinuousHeartbeatReceived: Consumer<ConcurrentPlayState>? = null

    private fun startHeartBeat() {
        if (heartbeatJob != null) {
            return
        }

        val interval = configManager.getDeviceConfig().heartbeatIntervalMs

        heartbeatJob = coroutineScope.launch {
            /* First Heartbeat */
            var state = repository.checkConcurrentPlay()
            onFirstHeartbeatReceived?.accept(state)
            delay(interval)

            /* Continuous Heartbeat */
            while (true) {
                state = repository.checkConcurrentPlay()
                onContinuousHeartbeatReceived?.accept(state)
                delay(interval)
            }
        }
    }

    override fun setOnFirstHeartbeatReceived(callback: Consumer<ConcurrentPlayState>) {
        onFirstHeartbeatReceived = callback
    }

    override fun setOnContinuousHeartbeatReceived(callback: Consumer<ConcurrentPlayState>) {
        onContinuousHeartbeatReceived = callback
    }

    override fun start() {
        startHeartBeat()
    }

    override fun stop() {
        coroutineScope.cancel()
        heartbeatJob?.cancel()
        heartbeatJob = null
    }
}