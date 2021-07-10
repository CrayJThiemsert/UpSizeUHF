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
import com.handheld.upsizeuhf.model.Actor
import com.handheld.upsizeuhf.model.Box
import com.handheld.upsizeuhf.model.Costume
import com.handheld.upsizeuhf.model.QueryService
import com.handheld.upsizeuhf.ui.ActSceneRVAdapter
import com.handheld.upsizeuhf.ui.ActorRVAdapter
import com.handheld.upsizeuhf.ui.BoxRVAdapter
import com.handheld.upsizeuhf.util.AnimationUtils
import com.handheld.upsizeuhf.util.Constants
import com.handheld.upsizeuhf.util.Constants.Companion.getCheckedHistoryProcedure
import com.handheld.upsizeuhf.util.Constants.Companion.getPlayBoxAllQuery
import com.handheld.upsizeuhf.util.Constants.Companion.getShipBoxAllQuery
import com.handheld.upsizeuhf.util.Constants.Companion.getStorageBoxAllQuery
import com.handheld.upsizeuhf.util.HttpConnectionService
import com.handheld.upsizeuhf.util.RoomUtils.Companion.loadActorList
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*
//import androidx.fragment.app.DialogFragment


class CheckTypeDialogFragment : DialogFragment() {
    private var TAG : String = this.javaClass.simpleName
    private var mActivity: UHFActivity? = null

    var dialog_title: TextView? = null
    private lateinit var mProgressBar: ProgressBar

    private lateinit var shipbox_type_button: Button
    private lateinit var storagebox_type_button: Button
    private lateinit var playbox_type_button: Button

    private lateinit var back_shipbox_button: Button
    private lateinit var check_shipbox_button: Button
    private lateinit var back_storagebox_button: Button
    private lateinit var check_storagebox_button: Button
    private lateinit var back_playbox_button: Button
    private lateinit var check_playbox_button: Button

    private lateinit var check_type_layout: LinearLayout
    private lateinit var shipbox_select_filter_layout: LinearLayout
    private lateinit var storagebox_select_filter_layout: LinearLayout
    private lateinit var playbox_select_filter_layout: LinearLayout

    private var shipbox_rvlist: RecyclerView? = null
    private var shipboxRVAdapter: BoxRVAdapter? = null

    private var storagebox_rvlist: RecyclerView? = null
    private var storageboxRVAdapter: BoxRVAdapter? = null

    private var playbox_rvlist: RecyclerView? = null
    private var playboxRVAdapter: BoxRVAdapter? = null

    private var mBoxArrayList = ArrayList<Box>()

    interface ReloadCostumeListener {
        fun onFinishCheckedBoxDialog()
    }

    public lateinit var reloadCostumeListener: ReloadCostumeListener

    companion object {
        private var costumeCheckedList: MutableList<Costume> = mutableListOf<Costume>()

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters

        fun newInstance(param1: String, param2: String, costumeCheckedListParam: MutableList<Costume>) : CheckTypeDialogFragment   {
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
        val view: View = inflater!!.inflate(R.layout.dialog_check_type, null)

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


        shipbox_type_button = view.findViewById(R.id.shipbox_type_button)
        storagebox_type_button = view.findViewById(R.id.storagebox_type_button)
        playbox_type_button = view.findViewById(R.id.playbox_type_button)

        back_shipbox_button = view.findViewById(R.id.back_shipbox_button)
        check_shipbox_button = view.findViewById(R.id.check_shipbox_button)

        back_storagebox_button = view.findViewById(R.id.back_storagebox_button)
        check_storagebox_button = view.findViewById(R.id.check_storagebox_button)

        back_playbox_button = view.findViewById(R.id.back_playbox_button)
        check_playbox_button = view.findViewById(R.id.check_playbox_button)

        check_type_layout = view.findViewById(R.id.check_type_layout)
        shipbox_select_filter_layout = view.findViewById(R.id.shipbox_select_filter_layout)
        storagebox_select_filter_layout = view.findViewById(R.id.storagebox_select_filter_layout)
        playbox_select_filter_layout = view.findViewById(R.id.playbox_select_filter_layout)

        shipbox_rvlist = view.findViewById(R.id.shipbox_rvlist) as RecyclerView
        val shipboxlinearLayoutManager = LinearLayoutManager(mActivity!!.applicationContext, RecyclerView.VERTICAL, false)
        shipbox_rvlist!!.setLayoutManager(shipboxlinearLayoutManager)

        storagebox_rvlist = view.findViewById(R.id.storagebox_rvlist) as RecyclerView
        val storageboxlinearLayoutManager = LinearLayoutManager(mActivity!!.applicationContext, RecyclerView.VERTICAL, false)
        storagebox_rvlist!!.setLayoutManager(storageboxlinearLayoutManager)

        playbox_rvlist = view.findViewById(R.id.playbox_rvlist) as RecyclerView
        val playboxlinearLayoutManager = LinearLayoutManager(mActivity!!.applicationContext, RecyclerView.VERTICAL, false)
        playbox_rvlist!!.setLayoutManager(playboxlinearLayoutManager)

        setEvents()
    }

    private fun setEvents() {
        shipbox_type_button.setOnClickListener(ClickShipBoxButton())
        storagebox_type_button.setOnClickListener(ClickStorageBoxButton())
        playbox_type_button.setOnClickListener(ClickPlayBoxButton())

        back_shipbox_button.setOnClickListener(ClickBackBoxButton(back_shipbox_button))
        check_shipbox_button.setOnClickListener(ClickCheckBoxButton(check_shipbox_button))

        back_storagebox_button.setOnClickListener(ClickBackBoxButton(back_storagebox_button))
        check_storagebox_button.setOnClickListener(ClickCheckBoxButton(check_storagebox_button))

        back_playbox_button.setOnClickListener(ClickBackBoxButton(back_playbox_button))
        check_playbox_button.setOnClickListener(ClickCheckBoxButton(check_playbox_button))

//        loadExistedBoxType()
    }

    private fun loadExistedBoxType() {
        val uhfActivity = mActivity as UHFActivity
        when(uhfActivity.getSelectedCheckedBoxType()) {
            Constants.TYPE_SHIPBOX -> {
//                loadShipBoxPage()

                shipboxRVAdapter = BoxRVAdapter(mActivity!!.applicationContext, mActivity, uhfActivity.getmShipBoxArrayList(), this@CheckTypeDialogFragment)
                shipbox_rvlist!!.setAdapter(shipboxRVAdapter)
                shipboxRVAdapter!!.notifyDataSetChanged()
            }
            Constants.TYPE_STORAGEBOX -> {
//                loadStorageBoxPage()
                storageboxRVAdapter = BoxRVAdapter(mActivity!!.applicationContext, mActivity, uhfActivity.getmStorageBoxArrayList(), this@CheckTypeDialogFragment)
                storagebox_rvlist!!.setAdapter(storageboxRVAdapter)
                storageboxRVAdapter!!.notifyDataSetChanged()
            }
            Constants.TYPE_PLAYBOX -> {
//                loadPlayBoxPage()
                playboxRVAdapter = BoxRVAdapter(mActivity!!.applicationContext, mActivity, uhfActivity.getmPlayBoxArrayList(), this@CheckTypeDialogFragment)
                playbox_rvlist!!.setAdapter(playboxRVAdapter)
                playboxRVAdapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun setVisible(layout : LinearLayout) {
        check_type_layout.visibility = View.GONE
        shipbox_select_filter_layout.visibility = View.GONE
        storagebox_select_filter_layout.visibility = View.GONE
        playbox_select_filter_layout.visibility = View.GONE

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

    fun loadShipBoxPage() {
        val uhfActivity = mActivity as UHFActivity
        uhfActivity!!.mSelectedCheckedBoxType = Constants.TYPE_SHIPBOX

        setVisible(shipbox_select_filter_layout)
        loadExistedBoxType()

//        val uhfActivity = mActivity as UHFActivity
//        uhfActivity.setSelectedCheckedBoxType(Constants.TYPE_SHIPBOX)
//
//        ServiceQueryAsyncTask(mActivity!!.applicationContext, mActivity!!, getShipBoxAllQuery()).execute()
    }

    inner class ShipBoxButtonAnimationListener : AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            loadShipBoxPage()
        }

        override fun onAnimationEnd(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    }

    fun loadStorageBoxPage() {
        val uhfActivity = mActivity as UHFActivity
        uhfActivity!!.mSelectedCheckedBoxType = Constants.TYPE_STORAGEBOX
        setVisible(storagebox_select_filter_layout)
        loadExistedBoxType()

//        val uhfActivity = mActivity as UHFActivity
//        uhfActivity.setSelectedCheckedBoxType(Constants.TYPE_STORAGEBOX)
//
//        ServiceQueryAsyncTask(mActivity!!.applicationContext, mActivity!!, getStorageBoxAllQuery()).execute()
    }

    inner class StorageBoxButtonAnimationListener : AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            loadStorageBoxPage()
        }

        override fun onAnimationEnd(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    }

    fun loadPlayBoxPage() {
        val uhfActivity = mActivity as UHFActivity
        uhfActivity!!.mSelectedCheckedBoxType = Constants.TYPE_PLAYBOX
        setVisible(playbox_select_filter_layout)
        loadExistedBoxType()

//        val uhfActivity = mActivity as UHFActivity
//        uhfActivity.setSelectedCheckedBoxType(Constants.TYPE_PLAYBOX)
//
//        ServiceQueryAsyncTask(mActivity!!.applicationContext, mActivity!!, getPlayBoxAllQuery()).execute()
    }

    inner class PlayBoxButtonAnimationListener : AnimationListener {
        override fun onAnimationStart(animation: Animation) {
            loadPlayBoxPage()
        }

        override fun onAnimationEnd(animation: Animation) {}
        override fun onAnimationRepeat(animation: Animation) {}
    }

    inner class ClickBackBoxButton(back_button: Button) : View.OnClickListener{
        lateinit var back_button: Button
        init {
            this.back_button = back_button
        }

        override fun onClick(p0: View?) {

            val animate = AnimationUtils.getBounceAnimation(mActivity!!.applicationContext)

            if(back_button === back_shipbox_button) {
                animate.setAnimationListener(BackBoxButtonAnimationListener(back_shipbox_button))
                back_shipbox_button.startAnimation(animate)
            } else if(back_button === back_storagebox_button) {
                animate.setAnimationListener(BackBoxButtonAnimationListener(back_storagebox_button))
                back_storagebox_button.startAnimation(animate)
            } else if(back_button === back_playbox_button) {
                animate.setAnimationListener(BackBoxButtonAnimationListener(back_playbox_button))
                back_playbox_button.startAnimation(animate)
            } 



        }
    }

    inner class BackBoxButtonAnimationListener(back_button: Button) : AnimationListener {
        lateinit var back_button: Button
        init {
            this.back_button = back_button
        }

        override fun onAnimationStart(animation: Animation) {
            setVisible(check_type_layout)

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


        when (uhfActivity.getSelectedCheckedBoxType()) {
            Constants.TYPE_SHIPBOX -> {
                shipboxRVAdapter = BoxRVAdapter(mActivity!!.applicationContext, mActivity, mBoxArrayList, this@CheckTypeDialogFragment)
                shipbox_rvlist!!.setAdapter(shipboxRVAdapter)
                shipboxRVAdapter!!.notifyDataSetChanged()
            }
            Constants.TYPE_STORAGEBOX -> {
                storageboxRVAdapter = BoxRVAdapter(mActivity!!.applicationContext, mActivity, mBoxArrayList, this@CheckTypeDialogFragment)
                storagebox_rvlist!!.setAdapter(storageboxRVAdapter)
                storageboxRVAdapter!!.notifyDataSetChanged()
            }
            Constants.TYPE_PLAYBOX -> {
                playboxRVAdapter = BoxRVAdapter(mActivity!!.applicationContext, mActivity, mBoxArrayList, this@CheckTypeDialogFragment)
                playbox_rvlist!!.setAdapter(playboxRVAdapter)
                playboxRVAdapter!!.notifyDataSetChanged()
            }
        }
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
                dismiss()
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
                            when (servicePath.uid) {
                                Constants.SHIPBOX_All -> {
                                    shipboxRVAdapter = BoxRVAdapter(mContext, mActivity, mBoxArrayList, this@CheckTypeDialogFragment)
                                    shipbox_rvlist!!.setAdapter(shipboxRVAdapter)
                                    shipboxRVAdapter!!.notifyDataSetChanged()
                                }
                                Constants.STORAGEBOX_All -> {
                                    storageboxRVAdapter = BoxRVAdapter(mContext, mActivity, mBoxArrayList, this@CheckTypeDialogFragment)
                                    storagebox_rvlist!!.setAdapter(storageboxRVAdapter)
                                    storageboxRVAdapter!!.notifyDataSetChanged()
                                }
                                Constants.PLAYBOX_All -> {
                                    playboxRVAdapter = BoxRVAdapter(mContext, mActivity, mBoxArrayList, this@CheckTypeDialogFragment)
                                    playbox_rvlist!!.setAdapter(playboxRVAdapter)
                                    playboxRVAdapter!!.notifyDataSetChanged()
                                }


                            }

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
        reloadCostumeListener = activity as ReloadCostumeListener
        reloadCostumeListener.onFinishCheckedBoxDialog()

//        listener!!.onFinishCheckedBoxDialog()
        dismiss()
    }

}
