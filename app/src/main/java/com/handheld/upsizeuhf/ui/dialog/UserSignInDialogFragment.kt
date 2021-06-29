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
import android.widget.EditText
import android.widget.TextView
import com.handheld.upsizeuhf.R
import com.handheld.upsizeuhf.UHFActivity
import com.handheld.upsizeuhf.model.User
import com.handheld.upsizeuhf.util.AnimationUtils
import com.handheld.upsizeuhf.util.UpsizeUhfUtils


class UserSignInDialogFragment : DialogFragment() {
    private var TAG : String = this.javaClass.simpleName
    private var mActivity: UHFActivity? = null

    var text_title_textview: TextView? = null
    var username_edittext: EditText? = null

    private lateinit var signin_button: Button

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

        fun newInstance(param1: String, param2: String) : UserSignInDialogFragment   {
            val fragment = UserSignInDialogFragment()
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
        val view: View = inflater.inflate(R.layout.dialog_user_signin, null)

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

        text_title_textview = view.findViewById<TextView>(R.id.trap_group_title)
        username_edittext = view.findViewById<EditText>(R.id.username_edittext)

        signin_button = view.findViewById(R.id.signin_button)

        text_title_textview?.typeface = UpsizeUhfUtils.getFontKanitMedium()

        signin_button?.typeface = UpsizeUhfUtils.getFontKanitMedium()

//        text_title_textview?.text = arguments!!.getString("title")


        setEvents()
    }

    private fun setEvents() {
        signin_button.setOnClickListener(ClickSaveMemberButton())
    }

    inner class ClickSaveMemberButton() : View.OnClickListener{
        override fun onClick(p0: View?) {
//            MediaUtils.playOneBeepSoundNoMediaPlayer()
            val animate = AnimationUtils.getBounceAnimation(mActivity!!.applicationContext)
            animate.setAnimationListener(SignInButtonAnimationListener())
            signin_button.startAnimation(animate)

        }
    }

    inner class SignInButtonAnimationListener : AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            val uhfActivity = mActivity as UHFActivity
            var user = User()
            user.username = username_edittext!!.text.toString()
            uhfActivity.upDateCurrentUserOperation(user)

        }

        override fun onAnimationEnd(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    }
}