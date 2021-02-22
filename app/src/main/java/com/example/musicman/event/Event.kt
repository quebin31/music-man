package com.example.musicman.event


class Event<out T>(private val value: T) {
    var consumed = false
        private set

    fun consume(): T? = when (consumed) {
        true -> null
        else -> {
            consumed = true
            value
        }
    }
}


