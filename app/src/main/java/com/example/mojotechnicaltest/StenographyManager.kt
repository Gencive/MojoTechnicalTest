package com.example.mojotechnicaltest

import android.content.Context
import android.util.Log
import java.net.URL

class StenographyManager {

    private val encodingManager: EncodingManager by lazy { EncodingManager() }
    private val decodingManager: DecodingManager by lazy { DecodingManager() }

    fun encodeTextAndPicture(context: Context, byteArray: ByteArray, textToEncode: String) {
        encodingManager.stenographyEncoding(byteArray, textToEncode) { encodedRes ->
            saveBytesAsImageOnDisk(context, decodingManager.fromBase64(encodedRes))
        }
    }
}