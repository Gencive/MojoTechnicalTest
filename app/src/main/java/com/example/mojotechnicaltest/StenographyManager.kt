package com.example.mojotechnicaltest

import android.util.Log
import java.net.URL

class StenographyManager {

    private val encodingManager: EncodingManager by lazy { EncodingManager() }
    private val decodingManager: DecodingManager by lazy { DecodingManager() }

    fun encodeTextAndPicture(byteArray: ByteArray, textToEncode: String) {
        encodingManager.stenographyEncoding(byteArray, textToEncode) {
            // todo save image on disk
        }
    }
}