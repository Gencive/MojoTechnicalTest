package com.example.mojotechnicaltest

import android.util.Base64
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EncodingManager {

    private val apiManager = ApiManager()

    private fun toBase64(toEncrypt: ByteArray): String = Base64.encodeToString(toEncrypt, Base64.DEFAULT)

    fun stenographyEncoding(
        picturePath: ByteArray,
        messageToEncode: String,
        callback: (String) -> Unit
    ) {
        GlobalScope.launch {
            val encodedPicture =
                apiManager.encodePictureAndText(toBase64(picturePath), messageToEncode)

            callback.invoke(encodedPicture)
        }
    }

}