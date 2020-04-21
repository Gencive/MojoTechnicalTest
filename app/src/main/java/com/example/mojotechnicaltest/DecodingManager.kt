package com.example.mojotechnicaltest

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64

class DecodingManager  {

    fun fromBase64(toDecode: String): ByteArray = Base64.decode(toDecode, Base64.NO_CLOSE)

    fun stenographyDecoding(picturePath: String): String = ""
}