package com.gmribas.mb.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import com.gmribas.mb.core.extensions.formatDateAdded
import com.gmribas.mb.core.extensions.formatDateShort
import com.gmribas.mb.core.extensions.formatDateLong

/**
 * Provides locale-aware date formatting that updates when the system locale changes
 */
@Composable
fun rememberFormattedDate(
    dateString: String?,
    format: DateFormat = DateFormat.MEDIUM
): String? {
    val configuration = LocalConfiguration.current
    
    return remember(dateString, configuration.locales) {
        dateString?.let {
            when (format) {
                DateFormat.SHORT -> it.formatDateShort()
                DateFormat.MEDIUM -> it.formatDateAdded()
                DateFormat.LONG -> it.formatDateLong()
            }
        }
    }
}

enum class DateFormat {
    SHORT,
    MEDIUM,
    LONG
}