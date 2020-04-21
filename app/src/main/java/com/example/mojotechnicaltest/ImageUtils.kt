package com.example.mojotechnicaltest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.mojotechnicaltest.models.Rgb
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


fun imagePathToRgbValues(context: Context, imagePath: String): List<List<Rgb>> {
    return emptyList()
}

fun uriToBytesArray(context: Context, uri: Uri, callback: (ByteArray) -> Unit) {
    Glide.with(context)
        .asBitmap()
        .load(uri)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(
                resource: Bitmap,
                transition: Transition<in Bitmap>?
            ) {
                val stream = ByteArrayOutputStream()
                resource.compress(Bitmap.CompressFormat.PNG, 100, stream)
                val byteArray: ByteArray = stream.toByteArray()

                callback(byteArray)
            }

            override fun onLoadCleared(placeholder: Drawable?) {
                // not implemented
            }
        })
}

fun saveBytesAsImageOnDisk(context: Context, bytes: ByteArray) {
    val internalDir = context.filesDir
    val encodedDir = File(internalDir, "encoded")

    if (!encodedDir.exists()) {
        encodedDir.mkdir()
    }
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "$timeStamp.jpg"
    val imageFile = File(encodedDir, imageFileName)
    imageFile.createNewFile()

    val fos = FileOutputStream(imageFile)
    fos.write(bytes)
    fos.close()
}