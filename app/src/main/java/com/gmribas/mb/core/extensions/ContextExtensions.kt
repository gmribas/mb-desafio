package com.gmribas.mb.core.extensions

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

fun Context.openBrowser(url: String?) {
    val intent = Intent(Intent.ACTION_VIEW, url?.toUri())
    startActivity(intent)
}
