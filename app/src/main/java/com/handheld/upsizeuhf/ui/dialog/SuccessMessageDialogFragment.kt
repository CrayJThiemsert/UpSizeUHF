package com.handheld.upsizeuhf.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.widget.Button
import android.widget.TextView
import com.handheld.upsizeuhf.R
import com.handheld.upsizeuhf.UHFActivity
import com.handheld.upsizeuhf.util.AnimationUtils
import com.handheld.upsizeuhf.util.UpsizeUhfUtils


class SuccessMessageDialogFragment : DialogFragment() {
    private var TAG : String = this.javaClass.simpleName
    private var mActivity: UHFActivity? = null

    private lateinit var dialog_title: TextView
    private lateinit var message_top_textview: TextView
    private lateinit var message_body_textview: TextView

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters

        fun newInstance(param1: String, param2: String, param3: String) : SuccessMessageDialogFragment   {
            val fragment = SuccessMessageDialogFragment()
            val args = Bundle()
            args.putString("title", param1)
            args.putString("messageTop", param2)
            args.putString("messageBody", param3)

            fragment.arguments = args
            return fragment
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        // Get the layout inflater
        val inflater = activity.layoutInflater

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        val view: View = inflater.inflate(R.layout.dialog_success_message, null)

        initInstance(view)

        builder.setView(view)

        // set background transparent
        val dialog = builder.create()
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog //builder.create()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
    }

    private fun initInstance(view: View) {
        mActivity = (activity as UHFActivity)

        dialog_title = view.findViewById<TextView>(R.id.dialog_title)
        message_top_textview = view.findViewById<TextView>(R.id.message_top_textview)
        message_body_textview = view.findViewById<TextView>(R.id.message_body_textview)

        if(!arguments!!["title"].toString().equals("")) {
            dialog_title.text = arguments!!["title"].toString()
        }
        if(!arguments!!["messageTop"].toString().equals("")) {
            message_top_textview.text = arguments!!["messageTop"].toString()
        } else {
            message_top_textview.visibility = View.GONE
        }

        if(!arguments!!["messageBody"].toString().equals("")) {
            message_body_textview.text = arguments!!["messageBody"].toString()
        } else {
            message_body_textview.visibility = View.GONE
        }
//        message_body_textview.typeface = UpsizeUhfUtils.getFontKanitItalic()

        setEvents()
    }

    private fun setEvents() {
    }


}