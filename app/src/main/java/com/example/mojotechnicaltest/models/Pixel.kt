package com.example.mojotechnicaltest.models

import java.lang.Exception

data class Pixel(val red: Int, val green: Int, val blue: Int) {

    operator fun get(index: Int) = when(index) {
        0 -> red
        1 -> green
        2 -> blue
        else -> throw Exception("Unsupported index")
    }

    operator fun plus(increment: Int): Int = increment + (red + green + blue)
}