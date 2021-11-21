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
import android.view.animation.Animation.AnimationListener
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.handheld.upsizeuhf.R
import com.handheld.upsizeuhf.UHFActivity
import com.handheld.upsizeuhf.model.Box
import com.handheld.upsizeuhf.model.Costume
import com.handheld.upsizeuhf.model.QueryService
import com.handheld.upsizeuhf.ui.BoxRVAdapter
import com.handheld.upsizeuhf.ui.ViewItemCodeRVAdapter
import com.handheld.upsizeuhf.util.AnimationUtils
import com.handheld.upsizeuhf.util.Constants
import com.handheld.upsizeuhf.util.Constants.Companion.getCheckedHistoryProcedure
import com.handheld.upsizeuhf.util.HttpConnectionService
import kotlinx.android.synthetic.main.activity_uhf.*
import kotlinx.android.synthetic.main.dialog_view_bulk_scan.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*

//import androidx.fragment.app.DialogFragment


class ViewBulkScanDialogFragment : DialogFragment() {
    private var TAG : String = this.javaClass.simpleName
    private var mActivity: UHFActivity? = null

    var dialog_title: TextView? = null
    private lateinit var mProgressBar: ProgressBar

    private lateinit var back_view_bulk_scan_button: Button

    private lateinit var view_bulk_scan_layout: LinearLayout

    private var view_bulk_scan_result_rvlist: RecyclerView? = null
    private var viewBulkScanRVAdapter: ViewItemCodeRVAdapter? = null
    private lateinit var found_number_textview: TextView
    private lateinit var total_number_textview: TextView

    private var mBoxArrayList = ArrayList<Box>()

    interface ReloadCostumeListener {
        fun onFinishCheckedBoxDialog(msgBody: String)
    }

    lateinit var reloadCostumeListener: ReloadCostumeListener

    lateinit var mScannedFoundList: String
    lateinit var mBoxType: String
    lateinit var mByUser: String
    lateinit var mCheckedBox: Box


    companion object {
        private var costumeCheckedList: MutableList<Costume> = mutableListOf<Costume>()
        lateinit var mTotal: String

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters

        fun newInstance(param1: String, param2: String, costumeCheckedListParam: MutableList<Costume>) : ViewBulkScanDialogFragment   {
            val fragment = ViewBulkScanDialogFragment()
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

            mTotal = param1
            costumeCheckedList = costumeCheckedListParam
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
        val view: View = inflater!!.inflate(R.layout.dialog_view_bulk_scan, null)

        initInstance(view)

        builder.setView(view)

        // set background transparent
        val dialog = builder.create()
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

//        builder.create()
        return dialog
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
        found_number_textview = view.findViewById<TextView>(R.id.view_found_number_textview)
        total_number_textview = view.findViewById<TextView>(R.id.total_number_textview)
        mProgressBar = view.findViewById(R.id.progressBar)

        back_view_bulk_scan_button = view.findViewById(R.id.back_view_bulk_scan_button)

        view_bulk_scan_layout = view.findViewById(R.id.view_bulk_scan_layout)

        view_bulk_scan_result_rvlist = view.findViewById(R.id.view_bulk_scan_result_rvlist) as RecyclerView
        val viewBulkScanLinearLayoutManager = LinearLayoutManager(mActivity!!.applicationContext, RecyclerView.VERTICAL, false)
        view_bulk_scan_result_rvlist!!.setLayoutManager(viewBulkScanLinearLayoutManager)

        viewBulkScanRVAdapter =
            ViewItemCodeRVAdapter(mActivity!!.applicationContext, mActivity,
                costumeCheckedList as ArrayList<Costume>?, Constants.ITEM_SET_MODE)
        view_bulk_scan_result_rvlist!!.adapter = viewBulkScanRVAdapter
        viewBulkScanRVAdapter!!.notifyDataSetChanged()

        found_number_textview.setText(costumeCheckedList.size.toString())
        total_number_textview.setText(mTotal)

        setEvents()
    }

    private fun setEvents() {

        back_view_bulk_scan_button.setOnClickListener(ClickBackBoxButton(back_view_bulk_scan_button))

    }

    inner class ClickBackBoxButton(back_button: Button) : View.OnClickListener{
        lateinit var back_button: Button
        init {
            this.back_button = back_button
        }

        override fun onClick(p0: View?) {

            val animate = AnimationUtils.getBounceAnimation(mActivity!!.applicationContext)

            if(back_button === back_view_bulk_scan_button) {
                animate.setAnimationListener(BackBoxButtonAnimationListener(back_view_bulk_scan_button))
                back_view_bulk_scan_button.startAnimation(animate)
            }
        }
    }

    inner class BackBoxButtonAnimationListener(back_button: Button) : AnimationListener {
        var back_button: Button
        init {
            this.back_button = back_button
        }

        override fun onAnimationStart(animation: Animation) {
//            setVisible(check_type_layout)
            dismiss()
//            this@ViewBulkScanDialogFragment.dismiss()
        }

        override fun onAnimationEnd(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    }

    inner class ClickCheckBoxButton(check_button: Button) : View.OnClickListener{
        lateinit var check_button: Button
        init {
            this.check_button = check_button
        }
        override fun onClick(p0: View?) {
            val animate = AnimationUtils.getBounceAnimation(mActivity!!.applicationContext)
            animate.setAnimationListener(CheckBoxButtonAnimationListener())

            check_button.startAnimation(animate)

        }
    }

    inner class CheckBoxButtonAnimationListener : AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            val uhfActivity = mActivity as UHFActivity
            val box = uhfActivity.getSelectedCheckedBox()
            val scannedFoundList = getCostumeUidString(uhfActivity.getScannedFoundItemArrayList())
            val boxType = uhfActivity.selectedCheckedBoxType
            val byUser = uhfActivity.currentUserName

            mCheckedBox = box
            mScannedFoundList = scannedFoundList
            mBoxType = boxType
            mByUser = byUser

            Log.d(TAG, "Scanned found Costume Uid list=" + scannedFoundList)
            Log.d(TAG, "selected box type=" + boxType)
            Log.d(TAG, "selected box=" + box.toString())
            Log.d(TAG, "byUser=" + byUser)

//            Example path:
//            http://192.168.1.101/costume/costume/checked/?costume_uid_list=341,342,343&actionType=storagebox&actionValue=1&byUser=cray
            var procedure = getCheckedHistoryProcedure()
            procedure.path = "/costume/costume/checked/" + "?costume_uid_list="+scannedFoundList + "&actionType="+boxType+"&actionValue="+box.uid+"&byUser=" + byUser

            ServiceQueryAsyncTask(mActivity!!.applicationContext, mActivity!!, procedure).execute()

//            dismiss()
        }

        override fun onAnimationEnd(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    }
    private fun getCostumeUidString(srcList: MutableList<Costume>): String {
        var result = ""
        srcList.forEach { costume ->
            if(costume.uid != null) {
                result += costume.uid.toString() + ","
            }
        }
        if(result.length > 1) {
            result = result.substring(0, result.length-1)
        }
        return result
    }

    public fun clearBoxList() {
        mBoxArrayList.forEach { box ->
            box.selected = false
        }
        val uhfActivity = mActivity as UHFActivity


//                viewBulkScanRVAdapter = BoxRVAdapter(mActivity!!.applicationContext, mActivity, mBoxArrayList, this@ViewBulkScanDialogFragment)
        view_bulk_scan_result_rvlist!!.setAdapter(viewBulkScanRVAdapter)
        viewBulkScanRVAdapter!!.notifyDataSetChanged()
    }

    inner class ReloadCostumesThread : Thread() {
        override fun run() {
            println("thread is running...")

            try {
                val uhfActivity = mActivity as UHFActivity
                uhfActivity.runOnUiThread(Runnable {
                    uhfActivity.loadData()

                    ReloadItemCodesList().start()
                })
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    inner class ReloadItemCodesList : Thread() {
        override fun run() {
            println("thread is running...")

            try {
                val uhfActivity = mActivity as UHFActivity
                uhfActivity.runOnUiThread(Runnable {
                    uhfActivity.queryItemCodesBySelectedActorActScene()

                })
//                dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

//    fun reloadCostumes() = GlobalScope.async {
//        val uhfActivity = mActivity as UHFActivity
//        uhfActivity.loadData()
//    }

//    fun reloadItemCodesList() = GlobalScope.async {
//        val uhfActivity = mActivity as UHFActivity
//        uhfActivity.queryItemCodesBySelectedActorActScene()
//    }

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
                        Constants.SHIPBOX_All,
                        Constants.STORAGEBOX_All,
                        Constants.PLAYBOX_All -> {
                            mBoxArrayList = ArrayList<Box>()
                            var i = 0
                            while (i < restfulJsonArray!!.length()) {
                                try {
                                    val jsonObject: JSONObject = restfulJsonArray!!.getJSONObject(i)
                                    val box = Box()
                                    box.uid = jsonObject.getInt("uid")
                                    box.name = jsonObject.getString("name")
                                    box.epc = jsonObject.getString("epc")
                                    box.epcHeader = jsonObject.getString("epcHeader")
                                    box.epcRun = jsonObject.getString("epcRun")

//                            if(existedBox.uid == box.uid) {
//                                box.selected = true
//                            } else {
//                                box.selected = false
//                            }

                                    mBoxArrayList.add(box)
                                } catch (e: JSONException) {
                                    e.printStackTrace()
                                }
                                i++
                            }
//                                    viewBulkScanRVAdapter = BoxRVAdapter(mContext, mActivity, mBoxArrayList, this@ViewBulkScanDialogFragment)
                            view_bulk_scan_result_rvlist!!.setAdapter(viewBulkScanRVAdapter)
                            viewBulkScanRVAdapter!!.notifyDataSetChanged()

                        }

                        Constants.CHECKED_HISTORY -> {
//                            ReloadCostumesThread().start()
                            sendBackResult()

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
            }
            return null
        }
    } //end of async task

    private fun sendBackResult() {
//        val listener = this as ReloadCostumeListener?
        Log.d(TAG, "Scanned found Costume Uid list=" + mScannedFoundList)
        Log.d(TAG, "selected box type=" + mBoxType)
        Log.d(TAG, "selected box=" + mCheckedBox.toString())
        Log.d(TAG, "byUser=" + mByUser)

        var msgBody = "Checked item id: " + mScannedFoundList + "\n" +
                "in " + mBoxType + ": " + mCheckedBox.name + "\n" +
                "by " + mByUser

        reloadCostumeListener = activity as ReloadCostumeListener
        reloadCostumeListener.onFinishCheckedBoxDialog(msgBody)

//        listener!!.onFinishCheckedBoxDialog()
        dismiss()
    }

}
