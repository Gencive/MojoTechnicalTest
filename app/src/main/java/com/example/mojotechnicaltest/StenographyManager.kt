package com.example.mojotechnicaltest

import android.content.Context
import com.example.mojotechnicaltest.models.EncodedItem
import java.lang.Exception


class StenographyManager {

    private val encodingManager: EncodingManager by lazy { EncodingManager() }
    private val decodingManager: DecodingManager by lazy { DecodingManager() }

    fun encodeTextAndPicture(
        context: Context,
        byteArray: ByteArray,
        textToEncode: String,
        onError: (Exception) -> Unit,
        onSuccess: () -> Unit
    ) {
        encodingManager.stenographyEncoding(byteArray, textToEncode, onError = onError) { encodedRes ->
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