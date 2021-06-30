package com.handheld.upsizeuhf.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.handheld.upsizeuhf.R
import com.handheld.upsizeuhf.UHFActivity
import com.handheld.upsizeuhf.util.AnimationUtils
import com.handheld.upsizeuhf.util.UpsizeUhfUtils


class CheckTypeDialogFragment : DialogFragment() {
    private var TAG : String = this.javaClass.simpleName
    private var mActivity: UHFActivity? = null

    var dialog_title: TextView? = null

    private lateinit var shipbox_type_button: Button
    private lateinit var storagebox_type_button: Button
    private lateinit var playbox_type_button: Button

    private lateinit var back_shipbox_button: Button
    private lateinit var check_shipbox_button: Button

    private lateinit var check_type_layout: LinearLayout
    private lateinit var shipbox_select_filter_layout: LinearLayout

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

        fun newInstance(param1: String, param2: String) : CheckTypeDialogFragment   {
            val fragment = CheckTypeDialogFragment()
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
        val view: View = inflater.inflate(R.layout.dialog_check_type, null)

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


        shipbox_type_button = view.findViewById(R.id.shipbox_type_button)
        storagebox_type_button = view.findViewById(R.id.storagebox_type_button)
        playbox_type_button = view.findViewById(R.id.playbox_type_button)

        back_shipbox_button = view.findViewById(R.id.back_shipbox_button)
        check_shipbox_button = view.findViewById(R.id.check_shipbox_button)

        check_type_layout = view.findViewById(R.id.check_type_layout)
        shipbox_select_filter_layout = view.findViewById(R.id.shipbox_select_filter_layout)

        setEvents()
    }

    private fun setEvents() {
        shipbox_type_button.setOnClickListener(ClickShipBoxButton())
        storagebox_type_button.setOnClickListener(ClickStorageBoxButton())
        playbox_type_button.setOnClickListener(ClickPlayBoxButton())

        back_shipbox_button.setOnClickListener(ClickBackShipBoxButton())
        check_shipbox_button.setOnClickListener(ClickCheckShipBoxButton())
    }

    private fun setVisible(layout : LinearLayout) {
        check_type_layout.visibility = View.GONE
        shipbox_select_filter_layout.visibility = View.GONE

        layout.visibility = View.VISIBLE
    }

    inner class ClickShipBoxButton() : View.OnClickListener{
        override fun onClick(p0: View?) {
//            MediaUtils.playOneBeepSoundNoMediaPlayer()
            val animate = AnimationUtils.getBounceAnimation(mActivity!!.applicationContext)
            animate.setAnimationListener(ShipBoxButtonAnimationListener())
            shipbox_type_button.startAnimation(animate)

        }
    }

    inner class ClickStorageBoxButton() : View.OnClickListener{
        override fun onClick(p0: View?) {
//            MediaUtils.playOneBeepSoundNoMediaPlayer()
            val animate = AnimationUtils.getBounceAnimation(mActivity!!.applicationContext)
            animate.setAnimationListener(StorageBoxButtonAnimationListener())
            storagebox_type_button.startAnimation(animate)

        }
    }

    inner class ClickPlayBoxButton() : View.OnClickListener{
        override fun onClick(p0: View?) {
//            MediaUtils.playOneBeepSoundNoMediaPlayer()
            val animate = AnimationUtils.getBounceAnimation(mActivity!!.applicationContext)
            animate.setAnimationListener(PlayBoxButtonAnimationListener())
            playbox_type_button.startAnimation(animate)

        }
    }

    inner class ShipBoxButtonAnimationListener : AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            setVisible(shipbox_select_filter_layout)
        }

        override fun onAnimationEnd(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    }

    inner class StorageBoxButtonAnimationListener : AnimationListener {
        override fun onAnimationStart(animation: Animation) {


        }

        override fun onAnimationEnd(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    }

    inner class PlayBoxButtonAnimationListener : AnimationListener {
        override fun onAnimationStart(animation: Animation) {


        }

        override fun onAnimationEnd(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    }

    inner class ClickBackShipBoxButton() : View.OnClickListener{
        override fun onClick(p0: View?) {
            val animate = AnimationUtils.getBounceAnimation(mActivity!!.applicationContext)
            animate.setAnimationListener(BackShipBoxButtonAnimationListener())
            back_shipbox_button.startAnimation(animate)

        }
    }

    inner class BackShipBoxButtonAnimationListener : AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            setVisible(check_type_layout)

        }

        override fun onAnimationEnd(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    }

    inner class ClickCheckShipBoxButton() : View.OnClickListener{
        override fun onClick(p0: View?) {
            val animate = AnimationUtils.getBounceAnimation(mActivity!!.applicationContext)
            animate.setAnimationListener(CheckShipBoxButtonAnimationListener())
            check_shipbox_button.startAnimation(animate)

        }
    }

    inner class CheckShipBoxButtonAnimationListener : AnimationListener {
        override fun onAnimationStart(animation: Animation) {


        }

        override fun onAnimationEnd(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    }
}
