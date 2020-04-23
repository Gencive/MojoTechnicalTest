package com.example.mojotechnicaltest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.mojotechnicaltest.models.Pixel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object ImageUtils {
    private const val ENCODED_DIR_NAME = "encoded"

    fun imagePathToRgbValues(imagePath: String): List<List<Pixel>> {
        val imageBitmap = BitmapFactory.decodeFile(imagePath)

        val pixels = MutableList(imageBitmap.width) {
            MutableList(imageBitmap.height) { Pixel(0, 0, 0) }
        }

        for (x in 0 until imageBitmap.width) {
            for (y in 0 until imageBitmap.height) {
                val pixel = imageBitmap.getPixel(x, y)

                pixels[x][y] = Pixel(
                    red = Color.red(pixel),
                    green = Color.green(pixel),
                    blue = Color.blue(pixel)
                )
            }
        }
        return pixels
    }

    /**
     * Large image will be very long to decode.
     * We should use glide to reduce image size.
     */
    fun imageUriToBytesArray(context: Context, uri: Uri, callback: (ByteArray) -> Unit) {
        Glide.with(context)
            .asBitmap()
            .load(uri)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    val stream = ByteArrayOutputStream()
                    resource.compress(Bitmap.CompressFormat.JPEG, 30, stream)
                    val byteArray: ByteArray = stream.toByteArray()

                    callback(byteArray)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // not implemented
                }
            })
    }

    fun saveBytesAsImageOnDisk(context: Context, bytes: ByteArray) {
        val encodedDir = getEncodedDir(context)

        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())

        val imageFileName = "$timeStamp.jpg"
        val imageFile = File(encodedDir, imageFileName)
        imageFile.createNewFile()

        val fos = FileOutputStream(imageFile)
        fos.write(bytes)
        fos.close()
    }

    fun getFilesOnEncodedDir(context: Context): List<String> {
        return getEncodedDir(context).listFiles()?.map { file ->
            file.path
        } ?: listOf()
    }

    private fun getEncodedDir(context: Context): File {
        return File(context.filesDir, ENCODED_DIR_NAME).also { encodedDir ->
            if (!encodedDir.exists()) {
                encodedDir.mkdir()
            }
        }
    }

}