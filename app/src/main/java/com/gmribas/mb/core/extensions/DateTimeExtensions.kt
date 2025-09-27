package com.gmribas.mb.core.extensions

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import java.text.SimpleDateFormat
import java.util.Date
import com.gmribas.mb.R
import java.text.DateFormat

fun String.toFormattedDateLabel(context: Context): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        try {
            val matchDateTime = LocalDateTime.parse(this, DateTimeFormatter.ISO_DATE_TIME)
            val today = LocalDate.now()
            val matchDate = matchDateTime.toLocalDate()
            
            when {
                matchDate.isEqual(today) -> {
                    val timeFormatter = DateTimeFormatter.ofPattern("h a", Locale.getDefault())
                    "${context.getString(R.string.date_today)}, ${matchDateTime.format(timeFormatter)}"
                }
                matchDate.isEqual(today.plusDays(1)) -> {
                    val timeFormatter = DateTimeFormatter.ofPattern("h a", Locale.getDefault())
                    "${context.getString(R.string.date_tomorrow)}, ${matchDateTime.format(timeFormatter)}"
                }
                else -> {
                    val dayTimeFormatter = DateTimeFormatter.ofPattern("EEE, h a", Locale.getDefault())
                    matchDateTime.format(dayTimeFormatter)
                }
            }
        } catch (e: Exception) {
            context.getString(R.string.date_tbd)
        }
    } else {
        // Fallback for API < 26
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
            val outputFormat = SimpleDateFormat("EEE, h a", Locale.getDefault())
            val date = inputFormat.parse(this)
            date?.let { outputFormat.format(it) } ?: context.getString(R.string.date_tbd)
        } catch (e: Exception) {
            context.getString(R.string.date_tbd)
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.toIsoDateString(): String {
    return this.format(DateTimeFormatter.ISO_LOCAL_DATE)
}

fun getTodayAsIsoString(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDate.now().toIsoDateString()
    } else {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.format(Date())
    }
}

/**
 * Formats a date string from ISO format to a readable format using device's locale
 * @return formatted date string or original string if parsing fails
 */
fun String.formatDateAdded(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        try {
            // For API 26+, use java.time with locale-aware formatting
            val dateTime = ZonedDateTime.parse(this.replace(".000Z", "Z"))
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
                .withLocale(Locale.getDefault())
            dateTime.format(formatter)
        } catch (e: Exception) {
            // Try alternative format
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
                val date = inputFormat.parse(this)
                date?.let {
                    DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(it)
                } ?: this
            } catch (e2: Exception) {
                this
            }
        }
    } else {
        // For older APIs, use SimpleDateFormat with locale
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(this)
            date?.let {
                // Use DateFormat for locale-aware formatting
                DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.getDefault()).format(it)
            } ?: this
        } catch (e: Exception) {
            this
        }
    }
}

/**
 * Formats a date string from ISO format to a short readable format using device's locale
 * @return formatted date string or original string if parsing fails
 */
fun String.formatDateShort(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        try {
            val dateTime = ZonedDateTime.parse(this.replace(".000Z", "Z"))
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
                .withLocale(Locale.getDefault())
            dateTime.format(formatter)
        } catch (e: Exception) {
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
                val date = inputFormat.parse(this)
                date?.let {
                    DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(it)
                } ?: this
            } catch (e2: Exception) {
                this
            }
        }
    } else {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(this)
            date?.let {
                DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault()).format(it)
            } ?: this
        } catch (e: Exception) {
            this
        }
    }
}

/**
 * Formats a date string from ISO format to a long readable format using device's locale
 * @return formatted date string or original string if parsing fails
 */
fun String.formatDateLong(): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        try {
            val dateTime = ZonedDateTime.parse(this.replace(".000Z", "Z"))
            val formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
                .withLocale(Locale.getDefault())
            dateTime.format(formatter)
        } catch (e: Exception) {
            try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
                inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
                val date = inputFormat.parse(this)
                date?.let {
                    DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault()).format(it)
                } ?: this
            } catch (e2: Exception) {
                this
            }
        }
    } else {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
            inputFormat.timeZone = java.util.TimeZone.getTimeZone("UTC")
            val date = inputFormat.parse(this)
            date?.let {
                DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault()).format(it)
            } ?: this
        } catch (e: Exception) {
            this
        }
    }
}
