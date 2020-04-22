package com.example.mojotechnicaltest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import kotlinx.android.synthetic.main.dialog_fragment_set_text_to_encode.*

class SetTextToEncodeDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(): SetTextToEncodeDialogFragment {
            return SetTextToEncodeDialogFragment()
        }
    }

    private val listener: Listener by lazy {
        context as Listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_fragment_set_text_to_encode, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setListeners()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NORMAL, R.style.AppTheme_Dialog)

        super.onCreate(savedInstanceState)
    }

    private fun setListeners() {
        btnEncode.setOnClickListener {
            listener.onEncodeAction(edTextToEncode.text.toString())
            dismiss()
        }
    }

    override fun onDestroyView() {
        // see https://code.google.com/p/android/issues/detail?id=17423
        if (dialog != null && retainInstance)
            dialog?.setDismissMessage(null)
        super.onDestroyView()
    }

    override fun show(manager: FragmentManager, tag: String?) {
        val ft = manager.beginTransaction()
        ft.add(this, tag)
        ft.commitAllowingStateLoss()
    }

    interface Listener {
        fun onEncodeAction(textToEncode: String)
    }
}