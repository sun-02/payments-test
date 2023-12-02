package com.example.payments_test.ui.util

import java.util.TimeZone
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DateTimeConverter {
    private val timePattern = "HH:mm:ss"
    private val datePattern = "dd.MM.yyyy"
    private val timeFormatter = DateTimeFormatter.ofPattern(timePattern)
    private val dateFormatter = DateTimeFormatter.ofPattern(datePattern)

    fun getDateTimeString(epochTimeSeconds: Long): String {
        val instant = Instant.ofEpochSecond(epochTimeSeconds)
        val zoneId = TimeZone.getDefault().toZoneId()
        val dateTime = LocalDateTime.ofInstant(instant, zoneId)
        val date = dateFormatter.format(dateTime.toLocalDate())
        val time = timeFormatter.format(dateTime.toLocalTime())
        return "$time $date"
    }
}