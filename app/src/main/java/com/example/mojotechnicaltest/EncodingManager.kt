package com.example.mojotechnicaltest

import android.util.Base64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class EncodingManager {

    private val apiManager = ApiManager()

    private fun toBase64(toEncrypt: ByteArray): String = Base64.encodeToString(toEncrypt, Base64.DEFAULT)

    fun stenographyEncoding(
        picturePath: ByteArray,
        messageToEncode: String,
        onError: (Exception) -> Unit,
        onSuccess: (String) -> Unit
    ) {
        GlobalScope.launch {
            try {
                val encodedPicture =
                    apiManager.encodePictureAndText(toBase64(picturePath), messageToEncode)

                GlobalScope.launch(Dispatchers.Main) {
                    onSuccess(encodedPicture)
                }
            } catch (e: Exception) {
                GlobalScope.launch(Dispatchers.Main) {
                    onError(e)
                }
            }
        }
    }

}