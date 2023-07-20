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
