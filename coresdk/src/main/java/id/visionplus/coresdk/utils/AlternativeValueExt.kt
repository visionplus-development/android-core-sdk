package id.visionplus.coresdk.utils

internal fun Int?.orZero() = this ?: 0

internal fun Long?.orZero() = this ?: 0

internal fun String?.orUnknownError() = this ?: "Unknown error"