package com.example.mojotechnicaltest

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
       private const val REQUEST_CODE_SELECT_PICTURE = 2
    }

    private val stenographyManager: StenographyManager by lazy {
        StenographyManager()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // todo handle negative response
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 4)
        }

        btnAdd.setOnClickListener { startFilePicker() }
    }

    private fun startFilePicker() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Picture"
            ), REQUEST_CODE_SELECT_PICTURE
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_SELECT_PICTURE) {
                handleFilePickerResult(data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun handleFilePickerResult(data: Intent?) {
        data?.data?.let {
            uriToBytesArray(this, it) { byteArray ->
                stenographyManager.encodeTextAndPicture(byteArray, "Bonjour")
            }
        }
    }
}
