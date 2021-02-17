package com.example.musicman.extensions

import android.content.Context
import android.widget.Toast

fun String.showShortToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}