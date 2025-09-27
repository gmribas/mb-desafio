package com.gmribas.mb.core.extensions

import java.text.NumberFormat
import java.util.Locale

/**
 * Formats a volume value into a readable string with appropriate suffixes (B, M, K)
 * @return formatted volume string with suffix
 */
fun Double.formatVolume(): String {
    return when {
        this >= 1_000_000_000 -> String.format("%.1fB", this / 1_000_000_000)
        this >= 1_000_000 -> String.format("%.1fM", this / 1_000_000)
        this >= 1_000 -> String.format("%.1fK", this / 1_000)
        else -> String.format("%.2f", this)
    }
}

/**
 * Formats a price value as USD currency
 * @return formatted price string with currency symbol
 */
fun Double.formatAsUSD(): String {
    val numberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    return numberFormat.format(this)
}

/**
 * Formats a percentage value with specified decimal places
 * @param decimals number of decimal places (default: 2)
 * @return formatted percentage string with % symbol
 */
fun Double.formatAsPercentage(decimals: Int = 2): String {
    return String.format("%.${decimals}f%%", this)
}