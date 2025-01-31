package com.ognessa.network_debugger.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.math.abs

internal object DebuggerTimeUtil {
    const val INTERNAL_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS"

    const val MILLISECONDS_PER_SECOND: Long = 1000
    const val MILLISECONDS_PER_MINUTE = 60 * MILLISECONDS_PER_SECOND
    const val MILLISECONDS_PER_HOUR = 60 * MILLISECONDS_PER_MINUTE
    const val MILLISECONDS_PER_DAY = 24 * MILLISECONDS_PER_HOUR

    fun getTime(): String {
        val format = SimpleDateFormat(INTERNAL_PATTERN, Locale.getDefault())
        val calendar = Calendar.getInstance()
        return format.format(calendar.time)
    }

    fun getHttpRequestTime(
        date: String
    ): String {
        val outputFormat = SimpleDateFormat("HH:MM", Locale.getDefault())
        val inputFormat = SimpleDateFormat(INTERNAL_PATTERN, Locale.getDefault())

        return inputFormat.parse(date)?.let { outputFormat.format(it) }.orEmpty()
    }

    fun getHttpTimeDiff(
        start: String,
        end: String
    ): String {
        val inputFormat = SimpleDateFormat(INTERNAL_PATTERN, Locale.getDefault())

        val startDate = inputFormat.parse(start)?.time ?: 0L
        val endDate = inputFormat.parse(end)?.time ?: 0L

        val diff = abs(endDate - startDate)

        val seconds = diff / MILLISECONDS_PER_SECOND
        val miliseconds = diff - (seconds * MILLISECONDS_PER_SECOND)

        return "$seconds.$miliseconds"
    }
}