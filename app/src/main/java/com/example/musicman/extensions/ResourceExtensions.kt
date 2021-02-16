package com.example.musicman.extensions

import android.content.Context
import android.net.Uri

fun Int.getAndroidUri(context: Context): Uri =
    Uri.parse("android.resource://${context.packageName}/${this}")