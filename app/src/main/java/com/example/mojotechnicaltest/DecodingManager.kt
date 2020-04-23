package com.example.mojotechnicaltest

import android.graphics.Point
import android.util.Base64
import android.util.Log
import com.example.mojotechnicaltest.models.Pixel

class DecodingManager {

    fun fromBase64(toDecode: String): ByteArray {
        return Base64.decode(toDecode, Base64.NO_WRAP or Base64.NO_PADDING)
    }

    fun stenographyDecoding(picturePath: String): String {
        val pixels = ImageUtils.imagePathToRgbValues(picturePath) // Heaviest part of the decoding.
        return checkValue(pixels, mutableListOf(), 0, 0, 0, false, "")
    }

    /**
     * Recursive Pixel Data chain decoding algorithm.
     * Recursive mean it can crash on some low device. or if the text is too long.
     */
    private fun checkValue(
        pixels: List<List<Pixel>>,
        usedPxCoor: MutableList<Pair<Int, Int>>,
        x: Int,
        y: Int,
        tableN: Int,
        updateTable: Boolean,
        message: String
    ): String {
        val pxVal = pixelToValue(pixels[x][y])

        when {
            usedPxCoor.contains(x to y) -> { // If pixel is already used we move to the next one.
                val (nextX, nextY) = getNextCoordinates(pixels, x, y)
                return checkValue(pixels, usedPxCoor, nextX, nextY, tableN, updateTable, message)
            }

            updateTable -> {
                return handleTableUpdating(pxVal, message, pixels, x, y, usedPxCoor)
            }

            pxVal == 26 -> { // Must update the subtable next
                val (nextX, nextY) = computeNextCoordinates(pixels, x, y)
                usedPxCoor.add(x to y)
                return checkValue(pixels, usedPxCoor, nextX, nextY, tableN, true, message)
            }

            else -> { // Add the converted pixel to char to the message.
                return handleNewChar(pixels, x, y, tableN, pxVal, usedPxCoor, message)
            }
        }
    }

    /**
     * Handle the update table logic (the previous value was 26).
     */
    private fun handleTableUpdating(
        pxVal: Int,
        message: String,
        pixels: List<List<Pixel>>,
        x: Int,
        y: Int,
        usedPxCoor: MutableList<Pair<Int, Int>>
    ): String {
        return when (pxVal) {
            26 -> { // two consecutive 26, that means the message is over.
                message
            }

            in 0..16 -> { // next Ascii sub table value
                val (nextX, nextY) = computeNextCoordinates(pixels, x, y)
                usedPxCoor.add(x to y)
                checkValue(pixels, usedPxCoor, nextX, nextY, pxVal, false, message)
            }

            else -> { // That should not happen.
                "Invalid encrypted message."
            }
        }
    }

    /**
     * Convert the pixel value to the new char. And happen it to the message.
     */
    private fun handleNewChar(
        pixels: List<List<Pixel>>,
        x: Int,
        y: Int,
        tableN: Int,
        pxVal: Int,
        usedPxCoor: MutableList<Pair<Int, Int>>,
        message: String
    ): String {
        val (nextX, nextY) = computeNextCoordinates(pixels, x, y)
        val char = (tableN * 15 + pxVal).toChar()
        usedPxCoor.add(x to y)

        return checkValue(pixels, usedPxCoor, nextX, nextY, tableN, false, message + char)
    }

    /**
     * Compute the next pixel coordinates according to the Pixel Data Chain Algorithm.
     */
    private fun computeNextCoordinates(pixels: List<List<Pixel>>, x: Int, y: Int): Pair<Int, Int> {
        return (pixels[x][y] + x) % pixels.size  to (pixels[x][y] + y) % pixels[x].size
    }

    /**
     * Get the coordinates of the next pixels closest to X & Y
     */
    private fun getNextCoordinates(list: List<List<Pixel>>, x: Int, y: Int): Pair<Int, Int> {
        val targetY = (y + 1) % list[x].size
        val targetX = if (targetY < y) {
            (x + 1) % list.size
        } else {
            x
        }

        return targetX to targetY
    }

    /**
     * Convert the pixel color the Pixel Data Chain algorithm value required.
     * (color % 3) happen and then converted to decimal from ternary.
     */
    private fun pixelToValue(pixel: Pixel): Int {
        return ((pixel.red % 3) * 3 + (pixel.green % 3)) * 3 + (pixel.blue % 3)
    }
}