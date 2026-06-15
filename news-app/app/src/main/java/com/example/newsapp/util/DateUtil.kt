package com.example.newsapp.util

import java.text.SimpleDateFormat
import java.util.*

fun formatPublishedAt(publishedAt: String?): String {
    if (publishedAt == null) return ""
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US)
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        val date = sdf.parse(publishedAt) ?: return publishedAt
        val now = Date()
        val diffMs = now.time - date.time
        val diffMin = diffMs / 60_000
        when {
            diffMin < 60 -> "${diffMin}m ago"
            diffMin < 1440 -> "${diffMin / 60}h ago"
            else -> "${diffMin / 1440}d ago"
        }
    } catch (e: Exception) {
        publishedAt.take(10)
    }
}
