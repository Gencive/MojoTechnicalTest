package com.example.mojotechnicaltest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    companion object {
        private const val REQUEST_CODE_SELECT_PICTURE = 2
    }

    private val stenographyManager: StenographyManager by lazy {
        StenographyManager()
    }

    private val encodedItemsAdapter = EncodedItemsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListener()

        setupEncodedList()
    }

    private fun setListener() {
        btnAdd.setOnClickListener { startFilePicker() }
    }

    private fun setupEncodedList() {
        rvEncoded.layoutManager = LinearLayoutManager(this)
        rvEncoded.adapter = encodedItemsAdapter

        displayEncodedList()
    }

    private fun displayEncodedList() {
        stenographyManager.getEncodedItems(this).let {
            encodedItemsAdapter.setData(it)
        }
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
                stenographyManager.encodeTextAndPicture(this, byteArray, "Bonjour") {
                    displayEncodedList()
                }
            }
        }
    }
}
