package com.handheld.upsizeuhf.ui.dialog

import android.app.*
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.handheld.upsizeuhf.R
import com.handheld.upsizeuhf.UHFActivity
import com.handheld.upsizeuhf.model.EPC
import com.handheld.upsizeuhf.util.AnimationUtils
import com.handheld.upsizeuhf.util.UhfUtils.Companion.separateEPCString


class WriteSingleTagDialogFragment : DialogFragment() {
    private var TAG : String = this.javaClass.simpleName
    private var mActivity: UHFActivity? = null

    var dialog_title: TextView? = null
    private lateinit var mProgressBar: ProgressBar

    private lateinit var check_type_layout: LinearLayout
    private lateinit var scanned_epc_textview: TextView

    private lateinit var back_write_single_tag_button: Button
    private lateinit var write_single_tag_button: Button

    interface ReloadCostumeListener {
        fun onFinishCheckedBoxDialog(msgBody: String)
    }

    public lateinit var reloadCostumeListener: ReloadCostumeListener

    lateinit var mScannedFoundList: String
    lateinit var mBoxType: String
    lateinit var mByUser: String

    companion object {
        private var mEPC: EPC = EPC()

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters

        fun newInstance(param1: String, param2: String, epcParam: EPC) : WriteSingleTagDialogFragment   {
            val fragment = WriteSingleTagDialogFragment()
            val args = Bundle()
            args.putString("text", param1)
            args.putString("hint", param2)
            args.putString("title", "Title")
            args.putBoolean("inputtype", true)
            args.putString("comment", "")
            args.putString("uid", "")
            args.putString("action", "")

            args.putString("username", "")
            args.putString("role", "user")
            args.putBoolean("active", false)
            args.putString("email", "")
            fragment.arguments = args

            mEPC = epcParam
            return fragment
        }

    }

    fun showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE)
    }

    fun hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        // Get the layout inflater
        val inflater = activity?.layoutInflater

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        val view: View = inflater!!.inflate(R.layout.dialog_write_single_tag, null)

        initInstance(view)

        builder.setView(view)

        // set background transparent
        val dialog = builder.create()
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog //builder.create()
    }

//    override fun onDismiss(dialog: DialogInterface?) {
//        super.onDismiss(dialog)
//        sendBackResult(Constants.ACTION_SAVE)
//        Log.d(TAG, "Back to home screen!!")
//        mActivity!!.supportFragmentManager.popBackStack()
//    }

    private fun initInstance(view: View) {
        mActivity = (activity as UHFActivity)

        dialog_title = view.findViewById<TextView>(R.id.dialog_title)
        mProgressBar = view.findViewById(R.id.progressBar)

        check_type_layout = view.findViewById(R.id.check_type_layout)

        scanned_epc_textview = view.findViewById(R.id.scanned_epc_textview)

        back_write_single_tag_button = view.findViewById(R.id.back_write_single_tag_button)
        write_single_tag_button = view.findViewById(R.id.write_single_tag_button)

        setEvents()
    }

    private fun setEvents() {
        val epcScanned = separateEPCString(mEPC.epcRaw, " ", 4, 24)
        scanned_epc_textview.setText(epcScanned)

        back_write_single_tag_button.setOnClickListener(ClickButton(back_write_single_tag_button))

    }

    inner class ClickButton(back_button: Button) : View.OnClickListener{
        lateinit var back_button: Button
        init {
            this.back_button = back_button
        }

        override fun onClick(p0: View?) {

            val animate = AnimationUtils.getBounceAnimation(mActivity!!.applicationContext)

            if(back_button === back_write_single_tag_button) {
                animate.setAnimationListener(BackButtonAnimationListener(back_write_single_tag_button))
                back_write_single_tag_button.startAnimation(animate)
            }
        }
    }

    inner class BackButtonAnimationListener(back_button: Button) : Animation.AnimationListener {
        lateinit var back_button: Button
        init {
            this.back_button = back_button
        }

        override fun onAnimationStart(animation: Animation) {
            dismiss()
        }

        override fun onAnimationEnd(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    }

    private fun setVisible(layout : LinearLayout) {
        check_type_layout.visibility = View.GONE

        layout.visibility = View.VISIBLE
    }

    private fun sendBackResult() {
//        val listener = this as ReloadCostumeListener?
        Log.d(TAG, "Scanned found Costume Uid list=" + mScannedFoundList)
        Log.d(TAG, "selected box type=" + mBoxType)
//        Log.d(TAG, "selected box=" + mCheckedBox.toString())
        Log.d(TAG, "byUser=" + mByUser)

        var msgBody = "Checked item id: " + mScannedFoundList + "\n"// +
//                "in " + mBoxType + ": " + mCheckedBox.name + "\n" +
//                "by " + mByUser

        reloadCostumeListener = activity as ReloadCostumeListener
        reloadCostumeListener.onFinishCheckedBoxDialog(msgBody)

//        listener!!.onFinishCheckedBoxDialog()
        dismiss()
    }

}
