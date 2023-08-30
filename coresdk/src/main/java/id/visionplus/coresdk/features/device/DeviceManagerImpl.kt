package id.visionplus.coresdk.features.device

import id.visionplus.coresdk.features.config.ConfigManager
import id.visionplus.coresdk.features.device.model.DeviceLimitState
import id.visionplus.coresdk.features.device.repository.DeviceRepository
import kotlinx.coroutines.*

internal class DeviceManagerImpl(
    private val configManager: ConfigManager,
    private val repository: DeviceRepository
): DeviceManager {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)
    private var heartbeatJob: Job? = null

    private var onFirstHeartbeatReceived: ((DeviceLimitState) -> Unit)? = null
    private var onContinuousHeartbeatReceived: ((DeviceLimitState) -> Unit)? = null

    private fun startHeartBeat() {
        if (heartbeatJob != null) {
            return
        }

        val interval = configManager.getDeviceConfig().heartbeatIntervalMs

        heartbeatJob = coroutineScope.launch {
            /* First Heartbeat */
            var state = repository.checkDeviceLimit()
            onFirstHeartbeatReceived?.invoke(state)
            delay(interval)

            /* Continuous Heartbeat */
            while (true) {
                state = repository.checkDeviceLimit()
                onContinuousHeartbeatReceived?.invoke(state)
                delay(interval)
            }
        }
    }

    override fun setOnFirstHeartbeatReceived(callback: (DeviceLimitState) -> Unit) {
        onFirstHeartbeatReceived = callback
    }

    override fun setOnContinuousHeartbeatReceived(callback: (DeviceLimitState) -> Unit) {
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