package id.visionplus.coresdk.utils.logger

import android.util.Log

internal var DEBUG = false
internal object Logger {

    fun debug(message: String) {
        if (DEBUG) {
            Log.d("", message)
        }
    }

    fun debug(tag: String, message: String) {
        if (DEBUG) {
            Log.d(tag, message)
        }
    }

    fun error(message: String) {
        if (DEBUG) {
            Log.e("", message)
        }
    }

    fun error(tag: String, message: String) {
        if (DEBUG) {
            Log.e(tag, message)
        }
    }
}