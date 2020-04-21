package com.example.mojotechnicaltest.models

import java.lang.Exception

// todo: check if alpha channel is required but I think not.
data class Rgb(val r: Int, val g: Int, val b: Int) {

    operator fun get(index: Int) = when(index) {
        0 -> r
        1 -> g
        2 -> b
        else -> throw Exception("Unsupported index")
    }
}