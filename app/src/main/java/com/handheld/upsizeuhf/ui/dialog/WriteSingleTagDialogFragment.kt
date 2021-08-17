package com.handheld.upsizeuhf.ui.dialog

import android.app.*
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.widget.*
import cn.pda.serialport.Tools
import com.android.hdhe.uhf.reader.UhfReader
import com.handheld.upsizeuhf.R
import com.handheld.upsizeuhf.UHFActivity
import com.handheld.upsizeuhf.model.Costume
import com.handheld.upsizeuhf.model.EPC
import com.handheld.upsizeuhf.model.QueryService
import com.handheld.upsizeuhf.util.UhfUtils.Companion.fontKanitSemiBold
import com.handheld.upsizeuhf.util.UhfUtils.Companion.separateEPCString
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.HashMap

import com.handheld.upsizeuhf.util.*
import com.handheld.upsizeuhf.util.UhfUtils.Companion.fontKanitBoldItalic
import com.handheld.upsizeuhf.util.UhfUtils.Companion.fontKanitLight


class WriteSingleTagDialogFragment : DialogFragment() {
    private var TAG : String = this.javaClass.simpleName
    private var mActivity: UHFActivity? = null

    private lateinit var mProgressBar: ProgressBar

    private lateinit var check_type_layout: LinearLayout
    private lateinit var scanned_epc_textview: TextView
    private lateinit var suggested_epc_code_caption_textview: TextView
    private lateinit var suggested_epc_code_edittext: EditText
    private lateinit var dialog_title: TextView
    private lateinit var current_epc_caption_textview: TextView

    private lateinit var back_write_single_tag_button: Button
    private lateinit var write_single_tag_button: Button

    interface ReloadScannedTagListener {
        fun onFinishWriteSingleTagDialog(msgBody: Int)
    }

    public lateinit var reloadScannedTagListener: ReloadScannedTagListener

    lateinit var mScannedFoundList: String
    lateinit var mBoxType: String
    lateinit var mByUser: String




    companion object {
        private lateinit var manager: UhfReader
        private val accessPassword = byteArrayOf(0x00, 0x00, 0x00, 0x00)
        private val membank = 1 // EPC
        private val addr = 2 // begin address

        private var mEPCScanned: EPC = EPC()
        private var mEPCSelected: EPC = EPC()
        private var mSuggestionEPC: EPC = EPC()

        private var mSelectedCostumeToWrite: Costume = Costume()

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters

        fun newInstance(param1: String, param2: String, costume: Costume, epcSelectedParam: EPC, epcScannedParam: EPC, managerParam: UhfReader) : WriteSingleTagDialogFragment   {
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

            mSelectedCostumeToWrite = costume
            mEPCSelected = epcSelectedParam
            mEPCScanned = epcScannedParam
            manager = managerParam

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

//        val displayRectangle = Rect()
//        val window: Window = activity.window
//
//        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle)
//
////        dialog!!.window?.setLayout()
//        dialog!!.window?.setLayout((displayRectangle.width() * 0.8f).toInt(), (displayRectangle.height() * 0.8f).toInt());

//        // Get screen width and height in pixels
//        // Get screen width and height in pixels
//        val displayMetrics = DisplayMetrics()
////        dialog.window..getDefaultDisplay().getMetrics(displayMetrics)
//        // The absolute width of the available display size in pixels.
//        // The absolute width of the available display size in pixels.
//        val displayWidth = displayMetrics.widthPixels
//        // The absolute height of the available display size in pixels.
//        // The absolute height of the available display size in pixels.
//        val displayHeight = displayMetrics.heightPixels
//
//        // Initialize a new window manager layout parameters
//
//        // Initialize a new window manager layout parameters
//        val layoutParams = WindowManager.LayoutParams()
//
//        // Copy the alert dialog window attributes to new layout parameter instance
//
//        // Copy the alert dialog window attributes to new layout parameter instance
//        layoutParams.copyFrom(dialog.window!!.attributes)
//
//        // Set the alert dialog window width and height
//        // Set alert dialog width equal to screen width 90%
//        // int dialogWindowWidth = (int) (displayWidth * 0.9f);
//        // Set alert dialog height equal to screen height 90%
//        // int dialogWindowHeight = (int) (displayHeight * 0.9f);
//
//        // Set alert dialog width equal to screen width 70%
//
//        // Set the alert dialog window width and height
//        // Set alert dialog width equal to screen width 90%
//        // int dialogWindowWidth = (int) (displayWidth * 0.9f);
//        // Set alert dialog height equal to screen height 90%
//        // int dialogWindowHeight = (int) (displayHeight * 0.9f);
//
//        // Set alert dialog width equal to screen width 70%
//        val dialogWindowWidth = (displayWidth * 0.7f).toInt()
//        // Set alert dialog height equal to screen height 70%
//        // Set alert dialog height equal to screen height 70%
//        val dialogWindowHeight = (displayHeight * 0.7f).toInt()
//
//        // Set the width and height for the layout parameters
//        // This will bet the width and height of alert dialog
//
//        // Set the width and height for the layout parameters
//        // This will bet the width and height of alert dialog
//        layoutParams.width = dialogWindowWidth
//        layoutParams.height = dialogWindowHeight
//
//        // Apply the newly created layout parameters to the alert dialog window
//
//        // Apply the newly created layout parameters to the alert dialog window
//        dialog.window!!.attributes = layoutParams

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

        mSuggestionEPC = EPC()

        dialog_title = view.findViewById<TextView>(R.id.dialog_title)
        mProgressBar = view.findViewById(R.id.progressBar)

        check_type_layout = view.findViewById(R.id.check_type_layout)

        current_epc_caption_textview = view.findViewById(R.id.current_epc_caption_textview)

        scanned_epc_textview = view.findViewById(R.id.scanned_epc_textview)
        suggested_epc_code_caption_textview = view.findViewById(R.id.suggested_epc_code_caption_textview)
        suggested_epc_code_edittext = view.findViewById(R.id.suggested_epc_code_edittext)

        back_write_single_tag_button = view.findViewById(R.id.back_write_single_tag_button)
        write_single_tag_button = view.findViewById(R.id.write_single_tag_button)


        // Font setup
        dialog_title.setTypeface(fontKanitBoldItalic)
        current_epc_caption_textview.setTypeface(fontKanitLight)
        suggested_epc_code_caption_textview.setTypeface(fontKanitLight)

        scanned_epc_textview.setTypeface(fontKanitSemiBold)
        suggested_epc_code_edittext.setTypeface(fontKanitSemiBold)

//        setEvents()
        loadSuggestionEPCRun()
    }

    private fun loadSuggestionEPCRun() {
//        Example path:
//        http://192.168.1.101/costume/costume/suggestionepcrun/?readEPCHeader=E2801170000002
        if(!mEPCScanned.epcRaw.equals("")) {
            var procedure = Constants.getSuggestionEPCProcedure()
            val epcHeader = mEPCScanned.epcRaw?.substring(0, 19)
            procedure.path = "/costume/costume/suggestionepcrun/" + "?readEPCHeader=" + epcHeader

            ServiceQueryAsyncTask(mActivity!!.applicationContext, mActivity!!, procedure).execute()
        } else {
            setEvents()
        }
    }

    private fun updateCurrentEPCRun() {
//        Example path:
//        http://192.168.1.101/costume/costume/updatecurrentepcrun/?uid=293&epcHeader=E280689400004010056&epcRun=7DD45
        val epcRaw: String = suggested_epc_code_edittext.getText().toString().replace(" ", "")
        if(!epcRaw.equals("") && mSelectedCostumeToWrite.uid != null) {
            var procedure = Constants.getUpdatedCurrentEPCRunProcedure()
            val epcHeader = epcRaw?.substring(0, 19)
            val epcRun = epcRaw?.substring(19)
            procedure.path = "/costume/costume/updatecurrentepcrun/" + "?uid=" + mSelectedCostumeToWrite.uid + "&epcHeader=" + epcHeader + "&epcRun=" + epcRun

            ServiceQueryAsyncTask(mActivity!!.applicationContext, mActivity!!, procedure).execute()
        }
    }

    private fun setEvents() {
        if(!mEPCScanned.epcRaw.equals("")) {
            val epcScanned = separateEPCString(mEPCScanned.epcRaw, " ", 4, 24)
            scanned_epc_textview.setText(epcScanned)
        } else {
            scanned_epc_textview.setText("")
        }

        if(!mEPCSelected.epcRaw.equals("")) {
            suggested_epc_code_caption_textview.setText( getString(R.string.writing_existing_epc_code))

            val epcSelected = separateEPCString(mEPCSelected.epcRaw, " ", 4, 24)
            suggested_epc_code_edittext.setText(epcSelected)
        } else {
            suggested_epc_code_caption_textview.setText( getString(R.string.suggested_writing_new_epc_code))

            if (!mSuggestionEPC.epcRaw.equals("")) {
                val epcSuggestion = separateEPCString(mSuggestionEPC.epcRaw, " ", 4, 24)
                suggested_epc_code_edittext.setText(epcSuggestion)
            } else {
                suggested_epc_code_edittext.setText("")
            }
        }

        back_write_single_tag_button.setOnClickListener(ClickButton(back_write_single_tag_button))
        write_single_tag_button.setOnClickListener(ClickButton(write_single_tag_button))

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
            } else if(back_button === write_single_tag_button) {
                animate.setAnimationListener(WriteButtonAnimationListener(write_single_tag_button))
                write_single_tag_button.startAnimation(animate)
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

    private fun doWriteTag() {
        manager.selectEPC(Tools.HexString2Bytes(mEPCScanned.epcRaw))
        if (accessPassword.size != 4) {
//            showToast(getString(R.string.password_is_4_bytes))
            return
        }

        val writeData: String = suggested_epc_code_edittext.getText().toString().replace(" ", "")
        if (writeData.length % 4 != 0 || writeData == "") {
//            showToast(getString(R.string.the_unit_is_word_1word_2bytes))
            DialogUtils.showWarningDialog(mActivity!!, mActivity!!.getString(R.string.write_tag_failed), mActivity!!.getString(R.string.the_unit_is_word_1word_2bytes), mActivity!!.getString(R.string.the_unit_is_word_1word_2bytes_solution))
            return
        }
        if (!UHFActivity.isNumeric(writeData)) {
//            showToast(getString(R.string.write_hex))
            DialogUtils.showWarningDialog(mActivity!!, mActivity!!.getString(R.string.write_tag_failed), mActivity!!.getString(R.string.write_hex), mActivity!!.getString(R.string.write_hex_solution))
            return
        }

        val dataBytes = Tools.HexString2Bytes(writeData)
        // dataLen = dataBytes/2 dataLen
        val writeFlag = manager.writeTo6C(accessPassword, membank, addr, dataBytes.size / 2, dataBytes)
        if (writeFlag) {
//            getString(R.string.write_successful_)
            updateCurrentEPCRun()

        } else {
//            getString(R.string.write_failue_)
            DialogUtils.showErrorDialog(mActivity!!, mActivity!!.getString(R.string.write_tag_failed), mActivity!!.getString(R.string.write_failue_), mActivity!!.getString(R.string.contact_tech_support))
        }
    }

    inner class WriteButtonAnimationListener(back_button: Button) : Animation.AnimationListener {
        lateinit var back_button: Button
        init {
            this.back_button = back_button
        }

        override fun onAnimationStart(animation: Animation) {
            // do something here
            doWriteTag()
//            val uhfActivity = mActivity as UHFActivity
//            val box = uhfActivity.getSelectedCheckedBox()
//            val scannedFoundList = getCostumeUidString(uhfActivity.getScannedFoundItemArrayList())
//            val boxType = uhfActivity.selectedCheckedBoxType
//            val byUser = uhfActivity.currentUserName
//
//            mCheckedBox = box
//            mScannedFoundList = scannedFoundList
//            mBoxType = boxType
//            mByUser = byUser
//
//            Log.d(TAG, "Scanned found Costume Uid list=" + scannedFoundList)
//            Log.d(TAG, "selected box type=" + boxType)
//            Log.d(TAG, "selected box=" + box.toString())
//            Log.d(TAG, "byUser=" + byUser)
//
////            Example path:
////            http://192.168.1.101/costume/costume/checked/?costume_uid_list=341,342,343&actionType=storagebox&actionValue=1&byUser=cray
//            var procedure = Constants.getCheckedHistoryProcedure()
//            procedure.path = "/costume/costume/checked/" + "?costume_uid_list="+scannedFoundList + "&actionType="+boxType+"&actionValue="+box.uid+"&byUser=" + byUser
//
//            ServiceQueryAsyncTask(mActivity!!.applicationContext, mActivity!!, procedure).execute()
        }

        override fun onAnimationEnd(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    }

    private fun sendBackResult() {
        var msgBody = mSelectedCostumeToWrite.uid

        reloadScannedTagListener = activity as ReloadScannedTagListener
        reloadScannedTagListener.onFinishWriteSingleTagDialog(msgBody)

        dismiss()
    }

    inner class ServiceQueryAsyncTask(private val mContext: Context, private val mActivity: Activity, servicePath: QueryService) : AsyncTask<Void?, Void?, Void?>() {
        private var TAG : String = this.javaClass.simpleName
        var response = ""
        var servicePath = QueryService()
        var postDataParams: HashMap<String, String>? = null

        private var restfulJsonArray: JSONArray? = null


        private var success = 0
        override fun onPreExecute() {
            super.onPreExecute()
//            showProgressBar()
        }

        override fun onPostExecute(result: Void?) {
            super.onPostExecute(result)
//            hideProgressBar()

            if (success == 1) {
                if (null != restfulJsonArray) {
                    val uhfActivity = mActivity as UHFActivity
                    val existedBox = uhfActivity.selectedCheckedBox

                    when (servicePath.uid) {
                        Constants.SUGGESTION_EPC -> {
                            var i = 0
                            while (i < restfulJsonArray!!.length()) {
                                try {
                                    val jsonObject: JSONObject = restfulJsonArray!!.getJSONObject(i)
                                    mSuggestionEPC.epcRaw = jsonObject.getString("dummyNewEPCStr")
                                    Log.d(TAG, "mSuggestionEPC.epcRaw=" + mSuggestionEPC.epcRaw)

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                                i++
                            }

                            setEvents()
                        }

                        Constants.UPDATE_CURRENT_EPCRUN -> {
                            var i = 0
                            while (i < restfulJsonArray!!.length()) {
                                try {
                                    val jsonObject: JSONObject = restfulJsonArray!!.getJSONObject(i)
                                    var value = jsonObject.getString("value")
                                    Log.d(TAG, "current currentEPCRunNo=" + value)

                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                                i++
                            }
                            if(i > 0) {
                                DialogUtils.showSuccessDialog(mActivity!!, mActivity!!.getString(R.string.write_single_tag_label), mActivity!!.getString(R.string.write_successful_), mActivity!!.getString(R.string.verify_write_tag))
                                sendBackResult()
                            } else {
                                DialogUtils.showErrorDialog(mActivity!!, mActivity!!.getString(R.string.write_tag_failed), mActivity!!.getString(R.string.write_failue_), mActivity!!.getString(R.string.contact_tech_support))
                            }

                        }
                    }

                }
            }
        }

        init {
            this.servicePath = servicePath
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            val uhfActivity = mActivity as UHFActivity

            postDataParams = HashMap()
            postDataParams!!["HTTP_ACCEPT"] = "application/json"
            val service = HttpConnectionService()
            val path = "http://" + uhfActivity.serverIp + servicePath.path
            Log.d(TAG, "path=$path")
            response = service.sendRequest(path, postDataParams)
            try {
                success = 1
                val resultJsonObject = JSONObject(response)
                restfulJsonArray = resultJsonObject.getJSONArray("output")
            } catch (e: JSONException) {
                success = 0
                e.printStackTrace()

                DialogUtils.showErrorDialog(mActivity!!, mActivity!!.getString(R.string.write_tag_failed), mActivity!!.getString(R.string.write_failue_), mActivity!!.getString(R.string.contact_tech_support))
            }
            return null
        }
    }

}
