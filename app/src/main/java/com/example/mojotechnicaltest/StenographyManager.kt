package com.example.mojotechnicaltest

import android.content.Context
import android.util.Log
import com.example.mojotechnicaltest.models.EncodedItem
import java.net.URL

class StenographyManager {

    private val encodingManager: EncodingManager by lazy { EncodingManager() }
    private val decodingManager: DecodingManager by lazy { DecodingManager() }

    fun encodeTextAndPicture(
        context: Context,
        byteArray: ByteArray,
        textToEncode: String,
        onSuccess: () -> Unit
    ) {
        encodingManager.stenographyEncoding(byteArray, textToEncode) { encodedRes ->
            ImageUtils.saveBytesAsImageOnDisk(context, decodingManager.fromBase64(encodedRes))
            onSuccess()
        }
    }

    fun getEncodedItems(context: Context): List<EncodedItem> {
        return ImageUtils.getFilesOnEncodedDir(context).map {
            EncodedItem(it, decodingManager.stenographyDecoding(it))
        }
    }
}