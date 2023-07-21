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

```kotlin
        val c = CoreVideo("USER TOKEN","DEVICE ID",20)

        c.listener = object : CoreVideoListener {
            override fun onLimitedDeviceError(code: Int, message: String) {
                TODO("Not yet implemented")
            }
        }

        // Release when usage are done or when activity is destroyed
        c.release()
```
