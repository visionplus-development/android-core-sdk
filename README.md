# Android Core SDK <img src="https://img.shields.io/github/v/release/visionplus-development/android-core-sdk.svg?label=latest"/>

## Setup
### Step 1. Add the JitPack repository to your build file
Add it in your build.gradle (root) at the end of repositories:
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
### Step 2. Add the dependency
Add it in your build.gradle (app):
```groovy
dependencies {
	implementation 'com.github.visionplus-development:android-core-sdk:$latest_version'
}
```

### Step 3. Usage
#### Config
```kotlin
        // init sdk on .Application
        VisionPlusCore.init(this) // required

        // enable debug
        if (BuildConfig.DEBUG) {
            VisionPlusCore.enableDebugMode()
        }

        // Global config
        VisionPlusCore.setGlobalConfig(
            GlobalConfig(
                deviceId = "DEVICE ID", // required
                token = "USER TOKEN", // we can define token later
            )
        )

        // Module config: Device Limit
        VisionPlusCore.setCoreModuleConfig(
            CoreModuleConfig.Device(
                heartbeatIntervalMs = 5000, // in milliss
                url = "URL" // full url
            )
        )

        // Update token
        VisionPlusCore.updateToken("USER TOKEN") // we can update token like this
```

#### Preparation
```kotlin
val coreDeviceManager = VisionPlusCore.getDeviceManager()

coreDeviceManager.setOnFirstHeartbeatReceived { state ->
    when (state) {
        is DeviceLimitState.Ok -> {
            // Device Limit Ok, user can proceed or play the video
        }
        is DeviceLimitState.Exceeded -> {
            // Device limit exceeded, may prompt user about that
        }
        is DeviceLimitState.Exception -> {
            if (state.exception is SocketException) {
                // socket exception happen, please do something or leave it empty to do nothing
            }

            // another exception checking, or just leave it empty to do nothing
        }
    }
}

coreDeviceManager.setOnContinuousHeartbeatReceived { state ->
    when (state) {
        is DeviceLimitState.Ok -> {
            // Device Limit Ok, user can proceed or continue playing the video, or leave it empty to do nothing
        }
        is DeviceLimitState.Exceeded -> {
            // Device limit exceeded, may prompt user about that
        }
        is DeviceLimitState.Exception -> {
            if (state.exception is SocketException) {
                // socket exception happen, please do something or leave it empty to do nothing
            }

            // another exception checking, or just leave it empty to do nothing
        }
    }
}

```

#### Start/Stop
```kotlin
// start the heartbeat
coreDeviceManager.start()

// stop the heartbeat
coreDeviceManager.stop()

```
