package com.example.mojotechnicaltest

import android.util.Base64
import com.example.mojotechnicaltest.models.Pixel

class DecodingManager {

    fun fromBase64(toDecode: String): ByteArray {
        return Base64.decode(toDecode, Base64.NO_WRAP or Base64.NO_PADDING)
    }

    fun stenographyDecoding(picturePath: String): String {
        val pixels = ImageUtils.imagePathToRgbValues(picturePath) // Heaviest part of the decoding.
        return decodeMessage(pixels)
    }

    /**
     * Recursive Pixel Data chain decoding algorithm.
     * Recursive mean it can crash on some low device. or if the text is too long.
     */
    private fun decodeMessage(
        pixels: List<List<Pixel>>
    ): String {
        var stop = false
        var decodedMessage = ""
        val usedPxCoordinates = mutableListOf<Pair<Int, Int>>()
        var tableN = 0
        var updateTable = false

        var x = 0
        var y = 0

        while (!stop) {
            val pxVal = pixelToValue(pixels[x][y])

            if (usedPxCoordinates.contains(x to y)) { // Already used pixel we move to next one.
                getNextCoordinates(pixels, x, y).let { x = it.first; y = it.second }

            } else {
                when {
                    updateTable -> { // Previous value was 26
                        when (pxVal) {
                            26 -> { // two consecutive 26, that means the message is over.
                                stop = true
                            }

                            in 0..16 -> { // next Ascii sub table value
                                tableN = pxVal
                                updateTable = false
                            }

                            else -> { // That should not happen. So we stop here.
                                decodedMessage = "Invalid encrypted message."
                                stop = true
                            }
                        }
                    }

                    pxVal == 26 -> updateTable = true

                    else -> decodedMessage += pxValToChar(tableN, pxVal)
                }

                usedPxCoordinates.add(x to y)
                computeNextCoordinates(pixels, x, y).let { x = it.first; y = it.second }
            }

        }

        return decodedMessage
    }

    /**
     * Convert the pixel value to the new char. And happen it to the message.
     */
    private fun pxValToChar(tableN: Int, pxVal: Int) = (tableN * 15 + pxVal).toChar()

    /**
     * Compute the next pixel coordinates according to the Pixel Data Chain Algorithm.
     */
    private fun computeNextCoordinates(pixels: List<List<Pixel>>, x: Int, y: Int): Pair<Int, Int> {
        return (pixels[x][y] + x) % pixels.size to (pixels[x][y] + y) % pixels[x].size
    }

    /**
     * Get the coordinates of the next pixels closest to X & Y
     */
    private fun getNextCoordinates(list: List<List<Pixel>>, x: Int, y: Int): Pair<Int, Int> {
        val targetX = (x + 1) % list.size
        val targetY = if (targetX < x) {
            (y + 1) % list.size
        } else {
            y
        }

        return targetX to targetY
    }

    /**
     * Convert the pixel color the Pixel Data Chain algorithm value required.
     * (color % 3) concat and then converted to decimal from ternary.
     */
    private fun pixelToValue(pixel: Pixel): Int {
        return ((pixel.red % 3) * 3 + (pixel.green % 3)) * 3 + (pixel.blue % 3)
    }
}