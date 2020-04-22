package com.example.mojotechnicaltest

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity(),
    SetTextToEncodeDialogFragment.Listener {

    companion object {
        private const val REQUEST_CODE_SELECT_PICTURE = 2
    }

    private val stenographyManager: StenographyManager by lazy {
        StenographyManager()
    }

    private val encodedItemsAdapter = EncodedItemsAdapter()

    private var textToEncode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setListener()

        setupEncodedList()
    }

    private fun setListener() {
        btnAdd.setOnClickListener {
            launchSetTextDialog()
        }
    }

    private fun launchSetTextDialog() {
        val fm = this.supportFragmentManager

        if (fm.findFragmentByTag("set_text_to_encode") == null) {
            val dialogFragment = SetTextToEncodeDialogFragment.newInstance()
            dialogFragment.show(fm, "set_text_to_encode")
        }
    }

    private fun setupEncodedList() {
        rvEncoded.layoutManager = LinearLayoutManager(this)
        rvEncoded.adapter = encodedItemsAdapter

        displayEncodedList()
    }

    private fun displayEncodedList() {
        showLoadingScreen("Decoding your pictures")

        GlobalScope.launch {
            stenographyManager.getEncodedItems(this@MainActivity).let {

                GlobalScope.launch(Dispatchers.Main) {
                    lLoading.visibility = View.GONE
                    encodedItemsAdapter.setData(it)
                }
            }
        }
    }

    override fun onEncodeAction(textToEncode: String) {
        this.textToEncode = textToEncode
        startFilePicker()
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
            showLoadingScreen("Encoding your picture")

            ImageUtils.imageUriToBytesArray(this, it) { byteArray ->
                stenographyManager.encodeTextAndPicture(this, byteArray, textToEncode) {
                    displayEncodedList()
                }
            }
        }
    }

    private fun showLoadingScreen(text: String) {
        lLoading.visibility = View.VISIBLE
        tvStatus.text = text
    }
}
