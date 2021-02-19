package com.example.musicman.extensions

import android.content.Context
import android.widget.Toast

fun Context.showToast(msg: String, short: Boolean = true) {
    val length = if (short) Toast.LENGTH_SHORT else Toast.LENGTH_LONG
    Toast.makeText(this, msg, length).show()
}


