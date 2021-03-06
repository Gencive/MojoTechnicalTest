package com.example.mojotechnicaltest

import android.util.Log
import java.io.*
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

class ApiManager {

    companion object {
        private const val API_URL = "https://27.ip-51-83-69.eu/technical_test/encoder/"
    }

    private fun getApiConnection(): HttpsURLConnection {
        return (URL(API_URL).openConnection() as HttpsURLConnection).apply {
            requestMethod = "POST"
            doInput = true
            doOutput = true
        }
    }

    /**
     * Encode the text in the provided picture and return the Base64 encoded picture.
     */
    suspend fun encodePictureAndText(picture: String, text: String): String {
        val apiConnection = getApiConnection()
        val os: OutputStream = apiConnection.outputStream

        val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
        writer.write(
            getPostDataString(
                hashMapOf(
                    "picture" to picture,
                    "message" to text
                )
            )
        )

        writer.flush()
        writer.close()
        os.close()

        return handleResponse(apiConnection)
    }

    private fun handleResponse(apiConnection: HttpsURLConnection): String {
        val responseCode: Int = apiConnection.responseCode

        var response = ""
        if (responseCode == HttpsURLConnection.HTTP_OK) {
            var line: String? = ""
            val br = BufferedReader(InputStreamReader(apiConnection.inputStream))
            while (line != null) {
                line = br.readLine()

                if (line != null) {
                    response += line
                }
            }
        }

        apiConnection.disconnect()

        return response
    }

    // From stack overflow
    private fun getPostDataString(params: HashMap<String, String>): String {
        val result = StringBuilder()
        var first = true
        for ((key, value) in params.entries) {
            if (first) first = false else result.append("&")
            result.append(URLEncoder.encode(key, "UTF-8"))
            result.append("=")
            result.append(URLEncoder.encode(value, "UTF-8"))
        }
        return result.toString()
    }
}