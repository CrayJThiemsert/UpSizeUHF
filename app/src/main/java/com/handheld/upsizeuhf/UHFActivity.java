package com.handheld.upsizeuhf;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.AutoSizeableTextView;
import androidx.core.widget.TextViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import cn.pda.serialport.Tools;
import pl.droidsonroids.gif.GifImageView;

import com.android.hdhe.uhf.reader.UhfReader;

import com.android.hdhe.uhf.readerInterface.TagModel;
import com.handheld.upsizeuhf.database.CostumeRoomDatabase;
import com.handheld.upsizeuhf.model.Actor;
import com.handheld.upsizeuhf.model.Box;
import com.handheld.upsizeuhf.model.Costume;
import com.handheld.upsizeuhf.model.QueryService;
import com.handheld.upsizeuhf.model.User;
import com.handheld.upsizeuhf.ui.ActSceneRVAdapter;
import com.handheld.upsizeuhf.ui.ActorRVAdapter;
import com.handheld.upsizeuhf.ui.EPCTagRVAdapter;
import com.handheld.upsizeuhf.ui.ItemCodeFilterRVAdapter;
import com.handheld.upsizeuhf.ui.ItemCodeRVAdapter;
import com.handheld.upsizeuhf.ui.ItemInfoRVAdapter;
import com.handheld.upsizeuhf.ui.dialog.CheckTypeDialogFragment;
import com.handheld.upsizeuhf.ui.dialog.ErrorMessageDialogFragment;
import com.handheld.upsizeuhf.ui.dialog.SuccessMessageDialogFragment;
import com.handheld.upsizeuhf.ui.dialog.UserSignInDialogFragment;
import com.handheld.upsizeuhf.ui.dialog.WarningMessageDialogFragment;
import com.handheld.upsizeuhf.ui.dialog.WriteSingleTagDialogFragment;
import com.handheld.upsizeuhf.util.AnimationUtils;
import com.handheld.upsizeuhf.util.Constants;
import com.handheld.upsizeuhf.util.DialogUtils;
import com.handheld.upsizeuhf.util.HttpConnectionService;
import com.handheld.upsizeuhf.util.RoomUtils;
import com.handheld.upsizeuhf.util.UhfUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UHFActivity extends Activity implements OnClickListener, CheckTypeDialogFragment.ReloadCostumeListener, WriteSingleTagDialogFragment.ReloadScannedTagListener {
    private String TAG = this.getClass().getSimpleName();
    /****************** for view:**************************************/
    private LinearLayout ls1;
    private LinearLayout l1;
    private LinearLayout l2;
    private LinearLayout l3;
    private LinearLayout l4;
    private TextView textViewS1;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private View viewS1;
    private View view1;
    private View view2;
    private View view3;
    private View view4;
    // Main layout
    private LinearLayout searchandcheck_layout;
    private LinearLayout byitemset_select_filter_layout;
    private LinearLayout byitemcode_select_filter_layout;
    private LinearLayout byitemset_queryresult_layout;

    private LinearLayout byitemcode_queryresult_layout;
    private LinearLayout read_single_tag_layout;
    private LinearLayout write_single_tag_1of4_layout;
    private LinearLayout write_single_tag_2of4_layout;
    private LinearLayout write_single_tag_3of4_layout;
    private LinearLayout search_single_tag_3of3_layout;
    
    private RelativeLayout l1epc;
    private LinearLayout l2readandwrite;
    private LinearLayout l3lockandkill;
    private LinearLayout l4settings;
    private LinearLayout l5moresettings;
    private Button button_moresetting;

    private EditText editAccesslock;
    private String accessPwd;
    /******************************************/
    /****for uhf operation:**************************************/
    /******************************************/
    private Spinner spinnerEPCRead;
    private Spinner spinnerEPCLock;
    private Button buttonClear;
    private Button buttonStart;

    private TextView textVersion;
    private ListView listViewData;
    private ArrayList<EPC> listEPC;
    private ArrayList<String> listepc = new ArrayList<String>();
    private ArrayAdapter<String> arr_adapter;
    private ArrayList<Map<String, Object>> listMap;
    private boolean runFlag = true;
    private boolean startFlag = false;
    private boolean scanItemSetFlag = false;
    private boolean scanItemCodeFlag = false;
    private boolean scanSingleTagFlag = false;
    private boolean scanWriteSingleTagFlag = false;
    private boolean scanSearchSingleTagFlag = false;
    private UhfReader manager; // UHF manager,UHF Operating handle
//	private ScreenStateReceiver screenReceiver;
    /******************************************/
    private Spinner spinnerMemBank;// mem area
    private EditText editPassword;// password
    private EditText editAddr;// begin address
    private EditText editLength;// read data length
    private Button buttonRead;
    private Button buttonWrite;
    private EditText editWriteData;// write data
    private EditText editReadData;// read data
    // RESERVE EPC TID USER:0,1,2,3
    private final String[] strMemBank = {"RESERVE", "EPC", "TID", "USER"};
    /************************************/
    private ArrayAdapter<String> adatpterMemBank;
    private Spinner spinnerLockType;//
    private Button buttonLock;//
    private EditText editKillPassword;//
    private Button buttonKill;//
    private ArrayAdapter<CharSequence> adapterLockType;
    private int membank;//
    private int lockMembank;
    private int addr = 0;// begin address
    private int length = 1;// read or write  data length
    private int lockType;//
    private Button buttonBack;
    /******************************************/
    private Button by_item_set_button;  //set by item set button
    private Button by_item_code_button; //set by item code button
    private Button read_single_tag_button;  // read single tag button
    private Button write_single_tag_button;  // write single tag button
    private Button search_single_tag_button;  // search single tag button

    private Button back_byitemset_button;//set by back to item set button
    private Button next_byitemset_button;//set by next to query result button

    private Button back_byitemcode_button;//set by back to item code button
    private Button next_byitemcode_button;//set by next to query result button

    private Button back_itemset_info_result_button;//set by back to select condition button
    private Button scan_itemset_info_result_button;//set by next to search/scan button
    private Button clear_itemset_info_result_button; // Clear query result list
    private Button check_itemset_info_result_button; // Check query result list

    private Button back_itemcode_info_result_button;//set by back to select condition button
    private Button scan_itemcode_info_result_button;//set by next to search/scan button
    private Button clear_itemcode_info_result_button; // Clear query result list
    private Button check_itemcode_info_result_button; // Check query result list

    // Single Tag screen
    private Button back_read_single_tag_button;
    private Button scan_read_single_tag_button;
    private Button clear_read_single_tag_button;
    private Button write_read_single_tag_button;

    // Write Single Tag
    private Button back_write_single_tag_1of4_button;
    private Button next_write_single_tag_1of4_button;

    private Button back_write_single_tag_2of4_button;
    private Button next_write_single_tag_2of4_button;

    private Button back_write_single_tag_3of4_button;
    private Button scan_write_single_tag_3of4_button;
    private Button clear_write_single_tag_3of4_button;
    private Button write_write_single_tag_3of4_button;

    private TextView write_single_tag_1of4_title_textview;
    private TextView write_single_tag_2of4_title_textview;

    private TextView write_single_tag_2of4_selected_actor_textview;
    private TextView write_single_tag_2of4_selected_actscene_textview;

    private TextView write_single_tag_3of4_tag_detail_actor_textview;
    private TextView write_single_tag_3of4_tag_detail_actscene_textview;

    private TextView write_single_tag_3of4_tag_detail_epc_header_textview;
    private TextView write_single_tag_3of4_tag_detail_epc_run_textview;

    private TextView write_single_tag_3of4_tag_detail_code_textview;
    private TextView write_single_tag_3of4_tag_detail_type_textview;
    private TextView write_single_tag_3of4_tag_detail_size_textview;
    private TextView write_single_tag_3of4_tag_detail_number_textview;

    private TextView write_single_tag_3of4_tag_detail_current_box_textview;

    // Search Single Tag
    private Button back_search_single_tag_3of3_button;
    private Button scan_search_single_tag_3of3_button;
    private Button clear_search_single_tag_3of3_button;

    private TextView search_single_tag_3of3_tag_detail_actor_textview;
    private TextView search_single_tag_3of3_tag_detail_actscene_textview;

    private TextView search_single_tag_3of3_tag_detail_epc_header_textview;
    private TextView search_single_tag_3of3_tag_detail_epc_run_textview;

    private TextView search_single_tag_3of3_tag_detail_code_textview;
    private TextView search_single_tag_3of3_tag_detail_type_textview;
    private TextView search_single_tag_3of3_tag_detail_size_textview;
    private TextView search_single_tag_3of3_tag_detail_number_textview;

    private TextView search_single_tag_3of3_tag_detail_current_box_textview;

    private TextView search_single_tag_3of3_tag_detail_rssi_textview;
    private TextView scan_area_message_textview;
    private TextView scanned_epc_textview;
    private TextView scanned_rssi_textview;

    private GifImageView scan_progress_gifview;

    private ArrayList<Integer> listSearchPower;
    {
        listSearchPower = new ArrayList<Integer>(Arrays.asList(
                Integer.valueOf(Constants.POWER_26)
                , Integer.valueOf(Constants.POWER_24)
                , Integer.valueOf(Constants.POWER_20)
                , Integer.valueOf(Constants.POWER_18)
                , Integer.valueOf(Constants.POWER_17)
                , Integer.valueOf(Constants.POWER_16)
        ));
    }

    private int mCurrentSearchPower = Constants.POWER_26;
    private int mCurrentSearchPowerIndex = 0;
    private int mSearchFound = Constants.SEARCH_NOT_FOUND;

    private TextView scan_area_title_textview;

    private Button button1;//set button1
    private Button button2;//set button2
    private Button button3;//set button3
    private Button button4;//set button4
    private Button button5;//set button5
    private Spinner spinnerSensitive;//The sensitivity
    private Spinner spinnerPower;//RF power
    private Spinner spinnerWorkArea;//work area
    private EditText editFrequency;// frequency
    private EditText editServerIp;// server ip
    private String[] powers = {"26dbm", "24dbm", "20dbm", "18dbm", "17dbm", "16dbm"};
    //private String[] powers = {"26dbm","25dm","24dbm","23dbm","22dbm","21dbm","20dbm","19dbm","18dbm","17dbm","16dbm"};
    private String[] sensitives = null;

    private String[] lockMemArrays = {"Kill Password", "Access password", "EPC", "TID", "USER"};
    private int lockMem = 0;
    private Spinner spinnerLockMem;

    private String[] areas = null;
    private ArrayAdapter<String> adapterSensitive;
    private ArrayAdapter<String> adapterPower;
    private ArrayAdapter<String> adapterArea;
    private int sensitive = 0;
    private int power = 0;//rate of work
    private int area = 0;
    private int frequency = 0;
    private String serverIp = "192.168.71.119"; // Dev server
    private String currentUserName = ""; // The current user

    private String what = "uhf";
    private String selectEpc = "";

    private ProgressDialog processDialog;
    private JSONArray restfulJsonArray;
    private int success = 0;
    private String actorAllPath = "http://192.168.71.119/costume/costume/actors/";
    private String costumeAllPath = "http://192.168.71.119/costume/costume/list/";

    private Context mContext;
    private Activity mActivity;
    ArrayList<Costume> mCostumeArrayList = new ArrayList<Costume>();
    ArrayList<Costume> mActSceneArrayList = new ArrayList<Costume>();
    ArrayList<Costume> mItemCodeArrayList = new ArrayList<Costume>();
    ArrayList<Costume> mItemInfoArrayList = new ArrayList<Costume>();
    ArrayList<Costume> mScannedFoundItemArrayList = new ArrayList<Costume>();

    // For CheckTypeDialog
    ArrayList<Box> mShipBoxArrayList = new ArrayList<Box>();
    ArrayList<Box> mStorageBoxArrayList = new ArrayList<Box>();
    ArrayList<Box> mPlayBoxArrayList = new ArrayList<Box>();

    private boolean isOnline = false;

    ArrayList<Actor> mActorArrayList = new ArrayList<Actor>();
    ArrayList<Costume> mItemCodeFilterArrayList = new ArrayList<Costume>();

    ArrayList<Costume> mItemInfoFilterArrayList = new ArrayList<Costume>();

    private RecyclerView actor_name_rvlist;
    private ActorRVAdapter actorRVAdapter;

    private RecyclerView actscene_name_rvlist;
    private ActSceneRVAdapter actsceneRVAdapter;

    private RecyclerView write_single_tag_1of4_actor_name_rvlist;
    private ActorRVAdapter actorWriteSingleTagRVAdapter;

    private RecyclerView write_single_tag_1of4_actscene_name_rvlist;
    private ActSceneRVAdapter actsceneWriteSingleTagRVAdapter;

    private RecyclerView write_single_tag_2of4_item_code_rvlist;
    private ItemCodeRVAdapter writeSingleTag2of4_itemCodeRVAdapter;

    private RecyclerView byitemset_item_code_rvlist;
    private ItemCodeRVAdapter byitemset_itemCodeRVAdapter;

    public ArrayList<String> mSelectedActorArray = new ArrayList<String>();
    public ArrayList<String> mSelectedActSceneArray = new ArrayList<String>();

    private TextView selected_actor_textview;
    private TextView selected_actscene_textview;

    private TextView total_items_textview;
    private TextView items_scanned_textview;

    private TextView write_single_tag_2of4_total_items_textview;
    private TextView write_single_tag_2of4_items_scanned_textview;

    // By Item Codes
    private RecyclerView byitemcode_filter_rvlist;
    private ItemCodeFilterRVAdapter itemCodeFilterRVAdapter;

    private RecyclerView byitemcode_item_info_rvlist;
    private ItemInfoRVAdapter itemInfoFilterRVAdapter;

    public ArrayList<Costume> mSelectedItemCodeFilterArray = new ArrayList<Costume>();
    public ArrayList<Costume> mSelectedItemCodeInfoArray = new ArrayList<Costume>();

    public Box mSelectedCheckedBox = new Box();
    public String mSelectedCheckedBoxType = "";

    private TextView byitemcode_selected_code_textview;
    private TextView byitemcode_selected_type_textview;
    private TextView byitemcode_selected_size_textview;
    private TextView byitemcode_selected_number_textview;

    private RecyclerView item_info_result_rvlist;
    private ItemInfoRVAdapter itemInfoResultRVAdapter;

    private RecyclerView read_single_tag_rvlist;
    private EPCTagRVAdapter epcTagRVAdapter;

    private RecyclerView write_single_tag_3of4_read_single_tag_rvlist;

    private TextView total_item_info_textview;
    private TextView item_info_scanned_textview;

    Thread thread = new InventoryThread();

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    UserSignInDialogFragment userSignInDialogFragment = new UserSignInDialogFragment();
    CheckTypeDialogFragment checkTypeDialogFragment = new CheckTypeDialogFragment();
    WarningMessageDialogFragment warningMessageDialogFragment = new WarningMessageDialogFragment();
    SuccessMessageDialogFragment successMessageDialogFragment = new SuccessMessageDialogFragment();
    ErrorMessageDialogFragment errorMessageDialogFragment = new ErrorMessageDialogFragment();
    WriteSingleTagDialogFragment writeSingleTagDialogFragment = new WriteSingleTagDialogFragment();

    public static CostumeRoomDatabase costumeRoomDatabase;

    private ProgressBar mProgressBar;

    private int mCurrentSearchMode = Constants.ITEM_SET_MODE;

    private String mSelectedActorFilter = "";
    private Costume mSelectedItemCodeFilter = new Costume();

    private Costume mSelectedCostumeToWrite = new Costume();

    private Costume mSingleScannedTag = new Costume();
    private int mSearchResult = 0;

    // Single Tag Detail
    private TextView tag_detail_actor_textview;
    private TextView tag_detail_actscene_textview;
    private TextView tag_detail_code_textview;
    private TextView tag_detail_type_textview;
    private TextView tag_detail_size_textview;
    private TextView tag_detail_number_textview;
    private TextView tag_detail_current_box_textview;
    private TextView tag_detail_epc_header_textview;
    private TextView tag_detail_epc_run_textview;

    com.handheld.upsizeuhf.model.EPC mEPCScanned = new com.handheld.upsizeuhf.model.EPC();
    com.handheld.upsizeuhf.model.EPC mEPCSelected = new com.handheld.upsizeuhf.model.EPC();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setOverflowShowingAlways();
        setContentView(R.layout.activity_uhf);
        if(isNumeric("0aB")){
            Log.e("zeng-1","true");
        }else {
            Log.e("zeng-1","false");
        }
        if(isNumeric("G")){
            Log.e("zeng-2","true");
        }else {
            Log.e("zeng-2","false");
        }

        // Get the Rf power, and set
        shared = getSharedPreferences("UhfRfPower", 0);
        editor = shared.edit();
        power = shared.getInt("power", 26);
        area = shared.getInt("area", 3);
        serverIp = shared.getString("serverIp", "192.168.71.119");
        currentUserName = shared.getString("currentUserName", "admin");

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi.isConnected()) {
            // Do whatever
            isOnline = true;
        }

        showAppTitle();

        //init view
        initView();

        // load database once
        loadData();
        //start inventory thread
        thread = new InventoryThread();

        thread.start();
        // init sound pool
        Util.initSoundPool(this);

        refreshItemSet();
    }

    private void refreshItemSet() {
        new RefreshItemSetResultThread().start();
    }

    public String getCurrentUserName() {
        return currentUserName;
    }

    private void showAppTitle() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = packageInfo.versionName;
            int versionCode = packageInfo.versionCode;
            setTitle(getString(R.string.app_name) + "-v" + versionName + " build " + versionCode + "." + versionName + " by [" + currentUserName + "]" + ((isOnline) ? " - Online" : " - Offline") );
            setTitleColor(getResources().getColor(R.color.colorOrangeRed));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadUserSignInUI() {
//        if (processDialog.isShowing()) {
//            processDialog.dismiss();
//        }
        userSignInDialogFragment = UserSignInDialogFragment.Companion.newInstance("", "");
        userSignInDialogFragment.show(getFragmentManager(), "user_signin_fragment");
    }

    private void loadCheckTypeUI(int mode) {
        switch (mode) {
            case Constants.ITEM_SET_MODE: {
                Log.d(TAG, "Found mItemCodeArrayList.size()="+mItemCodeArrayList.size());
                mScannedFoundItemArrayList = filterOnlyScannedFound(mItemCodeArrayList);
                break;
            }

            case Constants.ITEM_CODE_MODE: {
                Log.d(TAG, "Found mItemInfoArrayList.size()="+mItemInfoArrayList.size());
                mScannedFoundItemArrayList = filterOnlyScannedFound(mItemInfoArrayList);
                break;
            }
        }

        Log.d(TAG, "Found mScannedFoundItemArrayList.size()="+mScannedFoundItemArrayList.size());
        if(mScannedFoundItemArrayList.size() > 0) {
            if(isOnline) {
                checkTypeDialogFragment = CheckTypeDialogFragment.Companion.newInstance("", "", mScannedFoundItemArrayList);

                checkTypeDialogFragment.show(getFragmentManager(), "check_type_fragment");
            } else {
                loadWarningDialog("", getString(R.string.database_server_offline), getString(R.string.database_server_offline_solution));
            }

        } else {
            loadWarningDialog("", getString(R.string.costume_notfound), getString(R.string.costume_notfound_solution));
        }
    }

    public ArrayList<Costume> getScannedFoundItemArrayList() {
        return mScannedFoundItemArrayList;
    }

    private ArrayList<Costume> filterOnlyScannedFound(ArrayList<Costume> costumeRawArrayList) {
        ArrayList<Costume> resultList = new ArrayList<Costume>();
        for(int i = 0; i < costumeRawArrayList.size(); i++) {
            Costume costume = costumeRawArrayList.get(i);
            if(costume.isFound) {
                resultList.add(costume);
            }
        }
        return resultList;
    }

    private void loadWarningDialog(String title, String messageTop, String messageBody) {
        warningMessageDialogFragment = WarningMessageDialogFragment.Companion.newInstance(title, messageTop, messageBody);
        warningMessageDialogFragment.show(getFragmentManager(), "warning_fragment");
    }

    private void loadErrorDialog(String title, String messageTop, String messageBody) {
        errorMessageDialogFragment = ErrorMessageDialogFragment.Companion.newInstance(title, messageTop, messageBody);
        errorMessageDialogFragment.show(getFragmentManager(), "error_fragment");
    }

    private void loadSuccessDialog(String title, String messageTop, String messageBody) {
        successMessageDialogFragment = SuccessMessageDialogFragment.Companion.newInstance(title, messageTop, messageBody);
        successMessageDialogFragment.show(getFragmentManager(), "success_fragment");
    }


    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        String powerString = "";
//		switch (UhfManager.Power) {
//			case SerialPort.Power_3v3:
//				powerString = "power_3V3";
//				break;
//			case SerialPort.Power_5v:
//				powerString = "power_5V";
//				break;
//			case SerialPort.Power_Scaner:
//				powerString = "scan_power";
//				break;
//			case SerialPort.Power_Psam:
//				powerString = "psam_power";
//				break;
//			case SerialPort.Power_Rfid:
        powerString = "rfid_power";
//				break;
//			default:
//				break;
//		}
        TextView textView_title_config;
        textView_title_config = (TextView) findViewById(R.id.textview_title_config);
        textView_title_config.setText("Port:com" + 13
                + ";Power:" + powerString);
        manager = UhfReader.getInstance();
        if (manager == null) {
            textVersion.setText(getString(R.string.serialport_init_fail_));
            setButtonClickable(buttonClear, false);
            setButtonClickable(buttonStart, false);
            return;
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        registerReceiver();

//		Log.e("", "value" + power);
        manager.setOutputPower(power);
        manager.setWorkArea(area);
//		byte[] version_bs = manager.getFirmware();
//		if (version_bs!=null){
//			textView_title_config.append("("+new String(version_bs)+")");
//		}
    }

    @Override
    protected void onPause() {
        startFlag = false;
        buttonStart.setText(R.string.inventory);

        scanItemSetFlag = false;
        scan_itemset_info_result_button.setText(R.string.scan);

        scanItemCodeFlag = false;
        scan_itemcode_info_result_button.setText(R.string.scan);

        scanSingleTagFlag = false;
        scan_read_single_tag_button.setText(R.string.scan);

        scanWriteSingleTagFlag = false;
        scan_write_single_tag_3of4_button.setText(R.string.scan);

        scanSearchSingleTagFlag = false;
        scan_search_single_tag_3of3_button.setText(R.string.scan);

        manager.close();
        super.onPause();
        unregisterReceiver();
    }

    @Override
    protected void onDestroy() {
        startFlag = false;
        scanItemSetFlag = false;
        scanItemCodeFlag = false;
        scanSingleTagFlag = false;
        scanWriteSingleTagFlag = false;
        scanSearchSingleTagFlag = false;
        runFlag = false;
        if (manager != null) {
            manager.close();
        }
        super.onDestroy();
    }

    public void loadData() {
        showProgressBar();

        // clear single scanned tag
        mSingleScannedTag = new Costume();

        new ServiceQueryAsyncTask(mContext, mActivity, Constants.Companion.getCostumeAllQuery()).execute();
    }

    private void initView() {
        mContext = this;
        mActivity = this;

        // get font
        AssetManager assetManager = getAssets();
//        UpsizeUhfUtils.loadFonts(assetManager);
        UhfUtils.Companion.loadFonts(assetManager);
        Log.d(TAG, "load fonts success.");

        buttonStart = (Button) findViewById(R.id.button_start);


        buttonClear = (Button) findViewById(R.id.button_clear);
        listViewData = (ListView) findViewById(R.id.listView_data);
        textVersion = (TextView) findViewById(R.id.textView_version);
        buttonStart.setOnClickListener(this);

        buttonClear.setOnClickListener(this);
        editAccesslock = (EditText) findViewById(R.id.edittext_access_lock);
        listEPC = new ArrayList<EPC>();
        ls1 = (LinearLayout) findViewById(R.id.linearLayoutSearchAndCheck);
        ls1.setOnTouchListener(new myOnTouch());
        l1 = (LinearLayout) findViewById(R.id.linearLayoutUhfEpc);
        l1.setOnTouchListener(new myOnTouch());
        l2 = (LinearLayout) findViewById(R.id.linearLayoutUhfRead);
        l2.setOnTouchListener(new myOnTouch());
        l3 = (LinearLayout) findViewById(R.id.linearLayoutUhfLock);
        l3.setOnTouchListener(new myOnTouch());
        l4 = (LinearLayout) findViewById(R.id.linearLayoutUhfSet);
        l4.setOnTouchListener(new myOnTouch());
        textViewS1 = (TextView) findViewById(R.id.textViewSearchAndCheck);
        textView1 = (TextView) findViewById(R.id.textViewUhfEpc);
        textView2 = (TextView) findViewById(R.id.textViewUhfMore);
        textView3 = (TextView) findViewById(R.id.textViewUhfLock);
        textView4 = (TextView) findViewById(R.id.textViewUhfSet);
        viewS1 = findViewById(R.id.viewSearchAndCheck);
        view1 = findViewById(R.id.viewUhfEpc);
        view2 = findViewById(R.id.viewUhfMore);
        view3 = findViewById(R.id.viewUhfLock);
        view4 = findViewById(R.id.viewUhfSet);
        searchandcheck_layout = (LinearLayout) findViewById(R.id.searchandcheck_layout);
        byitemset_select_filter_layout = (LinearLayout) findViewById(R.id.byitemset_select_filter_layout);
        byitemcode_select_filter_layout = (LinearLayout) findViewById(R.id.byitemcode_select_filter_layout);
        byitemset_queryresult_layout = (LinearLayout) findViewById(R.id.byitemset_queryresult_layout);

        byitemcode_queryresult_layout = (LinearLayout) findViewById(R.id.byitemcode_queryresult_layout);
        read_single_tag_layout = (LinearLayout) findViewById(R.id.read_single_tag_layout);
        write_single_tag_1of4_layout = (LinearLayout) findViewById(R.id.write_single_tag_1of4_layout);
        write_single_tag_2of4_layout = (LinearLayout) findViewById(R.id.write_single_tag_2of4_layout);
        write_single_tag_3of4_layout = (LinearLayout) findViewById(R.id.write_single_tag_3of4_layout);
        search_single_tag_3of3_layout = (LinearLayout) findViewById(R.id.search_single_tag_3of3_layout);
        
        l1epc = (RelativeLayout) findViewById(R.id.l1epc);
        l2readandwrite = (LinearLayout) findViewById(R.id.l2read);
        l3lockandkill = (LinearLayout) findViewById(R.id.l3lock);
        l4settings = (LinearLayout) findViewById(R.id.l4settings);

        arr_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listepc);
        arr_adapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEPCRead = (Spinner) findViewById(R.id.spinnerEPCread);
        spinnerEPCRead.setAdapter(arr_adapter);
        spinnerEPCLock = (Spinner) findViewById(R.id.spinnerEPClock);
        spinnerEPCLock.setAdapter(arr_adapter);
        spinnerMemBank = (Spinner) findViewById(R.id.spinner_membank);
        spinnerLockMem = (Spinner) findViewById(R.id.spinner_lock_mem);
        editAddr = (EditText) findViewById(R.id.edittext_addr);
        editLength = (EditText) findViewById(R.id.edittext_length);
        editPassword = (EditText) findViewById(R.id.editTextPassword);
        buttonRead = (Button) findViewById(R.id.button_read);
        buttonWrite = (Button) findViewById(R.id.button_write);
        buttonClear = (Button) findViewById(R.id.button_readClear);
        buttonLock = (Button) findViewById(R.id.button_lock_6c);
        buttonKill = (Button) findViewById(R.id.button_kill_6c);
        buttonBack = (Button) findViewById(R.id.button_back);
        button_moresetting = (Button) findViewById(R.id.button_uhf_more_settings);
        l5moresettings = (LinearLayout) findViewById(R.id.layout_uhf_more_settings);
        button_moresetting.setOnClickListener(this);
        editKillPassword = (EditText) findViewById(R.id.edit_kill_password);
        editWriteData = (EditText) findViewById(R.id.edittext_write);
        editReadData = (EditText) findViewById(R.id.linearLayout_readData);
        adatpterMemBank = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, strMemBank);
        adatpterMemBank
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLockType = (Spinner) findViewById(R.id.spinner_lock_type);
        adapterLockType = ArrayAdapter.createFromResource(this,
                R.array.arr_lockType, android.R.layout.simple_spinner_item);
        adapterLockType
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLockType.setAdapter(adapterLockType);
        spinnerMemBank.setAdapter(adatpterMemBank);
        buttonClear.setOnClickListener(this);
        buttonRead.setOnClickListener(this);
        buttonWrite.setOnClickListener(this);
        buttonKill.setOnClickListener(this);
        buttonLock.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        spinnerMemBank.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                switch (arg2) {
                    case 0:
//						membank = UhfManager.RESERVE;
                        membank = UhfReader.MEMBANK_RESEVER;
                        break;
                    case 1:
                        membank = UhfReader.MEMBANK_EPC;
                        break;
                    case 2:
                        membank = UhfReader.MEMBANK_TID;
                        break;
                    case 3:
                        membank = UhfReader.MEMBANK_USER;
                        break;
                    default:
                        break;
                }

                lockMembank = arg2 + 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        spinnerLockMem.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, lockMemArrays));
        spinnerLockMem.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("lockMem", "lockMem");
                switch (position) {
                    case 0://lock kill password
                        lockMem = 0;
                        break;
                    case 1://lock access password
                        lockMem = 1;
                        break;
                    case 2://lock EPC
                        lockMem = 2;
                        break;
                    case 3://lock TID
                        lockMem = 3;
                        break;
                    case 4://lock USER
                        lockMem = 4;
                        break;
                }
                Log.e("lockMem", "lockMem = " + lockMem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerLockType.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                lockType = arg2;

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        spinnerEPCRead.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (listepc == null || listepc.size() == 0) {
                    return;
                }
                manager.selectEPC(Tools.HexString2Bytes(listepc.get(position)));
                selectEpc = listepc.get(position);
                spinnerEPCRead.setSelection(position, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        spinnerEPCLock.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                if (listepc.size() == 0) {
                    return;
                }
                manager.selectEPC(Tools.HexString2Bytes(listepc.get(position)));
                selectEpc = listepc.get(position);
                spinnerEPCLock.setSelection(position, true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
        button1 = (Button) findViewById(R.id.button_min);
        button2 = (Button) findViewById(R.id.button_plus);
        button3 = (Button) findViewById(R.id.button_set);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);

        spinnerSensitive = (Spinner) findViewById(R.id.spinner1);
        spinnerPower = (Spinner) findViewById(R.id.spinner2);
        spinnerWorkArea = (Spinner) findViewById(R.id.spinner3);
        editFrequency = (EditText) findViewById(R.id.edit4);
        editServerIp = (EditText) findViewById(R.id.edit5);
        sensitives = getResources().getStringArray(R.array.arr_sensitivity);
        areas = getResources().getStringArray(R.array.arr_area);

        adapterSensitive = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, sensitives);
        adapterPower = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, powers);
        adapterArea = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, areas);
        spinnerSensitive.setAdapter(adapterSensitive);
        spinnerPower.setAdapter(adapterPower);
        spinnerWorkArea.setAdapter(adapterArea);
        int power_position = 0;
        switch (power) {
            case 26:
                power_position = 0;
                break;
            case 24:
                power_position = 1;
                break;
            case 20:
                power_position = 2;
                break;
            case 18:
                power_position = 3;
                break;
            case 17:
                power_position = 4;
                break;
            case 16:
                power_position = 5;
                break;
            default:
                break;
        }
//        switch (power) {
//            case 26:
//                power_position = 0;
//                break;
//            case 25:
//                power_position = 1;
//                break;
//            case 24:
//                power_position = 2;
//                break;
//            case 23:
//                power_position = 3;
//                break;
//            case 22:
//                power_position = 4;
//                break;
//            case 21:
//                power_position = 5;
//                break;
//            case 20:
//                power_position = 6;
//                break;
//            case 19:
//                power_position = 7;
//                break;
//            case 18:
//                power_position = 8;
//                break;
//            case 17:
//                power_position = 9;
//                break;
//            case 16:
//                power_position = 10;
//                break;
//            default:
//                break;
//        }
        spinnerPower.setSelection(power_position, true);
        int area_position;
        if (area != 6) {
            area_position = area - 1;
        } else {
            area_position = 4;
        }
        spinnerWorkArea.setSelection(area_position, true);
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        spinnerWorkArea.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View view,
                                       int position, long id) {
                switch (position) {
                    case 0:
                        area = 1;
                        break;
                    case 1:
                        area = 2;
                        break;
                    case 2:
                        area = 3;
                        break;
                    case 3:
                        area = 4;
                        break;
                    case 4:
                        area = 6;
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spinnerSensitive.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {

                Log.e("", sensitives[position]);
                switch (position) {
                    case 0:
                        sensitive = 3;
                        break;
                    case 1:
                        sensitive = 2;
                        break;
                    case 2:
                        sensitive = 1;
                        break;
                    case 3:
                        sensitive = 0;
                        break;

                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        spinnerPower.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int position, long arg3) {
                Log.e("", powers[position]);
                switch (position) {
                    case 0:
                        power = 26;
                        break;
                    case 1:
                        power = 24;
                        break;
                    case 2:
                        power = 20;
                        break;
                    case 3:
                        power = 18;
                        break;
                    case 4:
                        power = 17;
                        break;
                    case 5:
                        power = 16;
                        break;
                    default:
                        break;
//                    case 0:
//                        power = 26;
//                        break;
//                    case 1:
//                        power =25;
//                        break;
//                    case 2:
//                        power = 24;
//                        break;
//                    case 3:
//                        power = 23;
//                        break;
//                    case 4:
//                        power = 22;
//                        break;
//                    case 5:
//                        power = 21;
//                        break;
//                    case 6:
//                        power = 20;
//                        break;
//                    case 7:
//                        power =19;
//                        break;
//                    case 8:
//                        power = 18;
//                        break;
//                    case 9:
//                        power = 17;
//                        break;
//                    case 10:
//                        power = 16;
//                        break;
//                    default:
//                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        editServerIp.setText(serverIp);

        // Font setup
        buttonStart.setTypeface(UhfUtils.Companion.getFontKanitSemiBoldItalic());

        // Search & Check
        by_item_set_button = (Button) findViewById(R.id.by_item_set_button);
        by_item_set_button.setOnClickListener(this);
        by_item_set_button.setBackgroundColor(getResources().getColor(R.color.colorGrey));

        by_item_code_button = (Button) findViewById(R.id.by_item_code_button);
        by_item_code_button.setOnClickListener(this);
        by_item_code_button.setBackgroundColor(getResources().getColor(R.color.colorGrey));

        read_single_tag_button = (Button) findViewById(R.id.read_single_tag_button);
        read_single_tag_button.setOnClickListener(this);
        read_single_tag_button.setBackgroundColor(getResources().getColor(R.color.colorGrey));

        write_single_tag_button = (Button) findViewById(R.id.write_single_tag_button);
        write_single_tag_button.setOnClickListener(this);
        write_single_tag_button.setBackgroundColor(getResources().getColor(R.color.colorGrey));

        search_single_tag_button = (Button) findViewById(R.id.search_single_tag_button);
        search_single_tag_button.setOnClickListener(this);
        search_single_tag_button.setBackgroundColor(getResources().getColor(R.color.colorGrey));

        back_byitemset_button = (Button) findViewById(R.id.back_byitemset_button);
        back_byitemset_button.setOnClickListener(this);

        next_byitemset_button = (Button) findViewById(R.id.next_byitemset_button);
        next_byitemset_button.setOnClickListener(this);

        back_byitemcode_button = (Button) findViewById(R.id.back_byitemcode_button);
        back_byitemcode_button.setOnClickListener(this);

        next_byitemcode_button = (Button) findViewById(R.id.next_byitemcode_button);
        next_byitemcode_button.setOnClickListener(this);

        back_itemset_info_result_button = (Button) findViewById(R.id.back_itemset_info_result_button);
        back_itemset_info_result_button.setOnClickListener(this);

        scan_itemset_info_result_button = (Button) findViewById(R.id.scan_itemset_info_result_button);
        scan_itemset_info_result_button.setOnClickListener(this);

        clear_itemset_info_result_button = (Button) findViewById(R.id.clear_itemset_info_result_button);
        clear_itemset_info_result_button.setOnClickListener(this);

        check_itemset_info_result_button = (Button) findViewById(R.id.check_itemset_info_result_button);
        check_itemset_info_result_button.setOnClickListener(this);

        back_itemcode_info_result_button = (Button) findViewById(R.id.back_itemcode_info_result_button);
        back_itemcode_info_result_button.setOnClickListener(this);

        scan_itemcode_info_result_button = (Button) findViewById(R.id.scan_itemcode_info_result_button);
        scan_itemcode_info_result_button.setOnClickListener(this);

        clear_itemcode_info_result_button = (Button) findViewById(R.id.clear_itemcode_info_result_button);
        clear_itemcode_info_result_button.setOnClickListener(this);

        check_itemcode_info_result_button = (Button) findViewById(R.id.check_itemcode_info_result_button);
        check_itemcode_info_result_button.setOnClickListener(this);

        // Read Single Tag
        back_read_single_tag_button = (Button) findViewById(R.id.back_read_single_tag_button);
        back_read_single_tag_button.setOnClickListener(this);

        scan_read_single_tag_button = (Button) findViewById(R.id.scan_read_single_tag_button);
        scan_read_single_tag_button.setOnClickListener(this);

        clear_read_single_tag_button = (Button) findViewById(R.id.clear_read_single_tag_button);
        clear_read_single_tag_button.setOnClickListener(this);

        write_read_single_tag_button = (Button) findViewById(R.id.write_read_single_tag_button);
        write_read_single_tag_button.setOnClickListener(this);

        // Write Single Tag 1of4
        back_write_single_tag_1of4_button = (Button) findViewById(R.id.back_write_single_tag_1of4_button);
        back_write_single_tag_1of4_button.setOnClickListener(this);

        next_write_single_tag_1of4_button = (Button) findViewById(R.id.next_write_single_tag_1of4_button);
        next_write_single_tag_1of4_button.setOnClickListener(this);

        write_single_tag_1of4_actor_name_rvlist = (RecyclerView)findViewById(R.id.write_single_tag_1of4_actor_name_rvlist);
        LinearLayoutManager writeActorlinearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        write_single_tag_1of4_actor_name_rvlist.setLayoutManager(writeActorlinearLayoutManager);

        write_single_tag_1of4_actscene_name_rvlist = (RecyclerView)findViewById(R.id.write_single_tag_1of4_actscene_name_rvlist);
        LinearLayoutManager writeActscenelinearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        write_single_tag_1of4_actscene_name_rvlist.setLayoutManager(writeActscenelinearLayoutManager);

        // Write Single Tag 2of4
        back_write_single_tag_2of4_button = (Button) findViewById(R.id.back_write_single_tag_2of4_button);
        back_write_single_tag_2of4_button.setOnClickListener(this);

        next_write_single_tag_2of4_button = (Button) findViewById(R.id.next_write_single_tag_2of4_button);
        next_write_single_tag_2of4_button.setOnClickListener(this);

        actor_name_rvlist = (RecyclerView)findViewById(R.id.actor_name_rvlist);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        actor_name_rvlist.setLayoutManager(linearLayoutManager);

        actscene_name_rvlist = (RecyclerView)findViewById(R.id.actscene_name_rvlist);
        LinearLayoutManager actscenelinearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        actscene_name_rvlist.setLayoutManager(actscenelinearLayoutManager);

        // Write Single Tag 3of4
        back_write_single_tag_3of4_button = (Button) findViewById(R.id.back_write_single_tag_3of4_button);
        back_write_single_tag_3of4_button.setOnClickListener(this);

        scan_write_single_tag_3of4_button = (Button) findViewById(R.id.scan_write_single_tag_3of4_button);
        scan_write_single_tag_3of4_button.setOnClickListener(this);

        clear_write_single_tag_3of4_button = (Button) findViewById(R.id.clear_write_single_tag_3of4_button);
        clear_write_single_tag_3of4_button.setOnClickListener(this);

        write_write_single_tag_3of4_button = (Button) findViewById(R.id.write_write_single_tag_3of4_button);
        write_write_single_tag_3of4_button.setOnClickListener(this);

        // Search Single Tag 3of4
        back_search_single_tag_3of3_button = (Button) findViewById(R.id.back_search_single_tag_3of3_button);
        back_search_single_tag_3of3_button.setOnClickListener(this);

        scan_search_single_tag_3of3_button = (Button) findViewById(R.id.scan_search_single_tag_3of3_button);
        scan_search_single_tag_3of3_button.setOnClickListener(this);

        clear_search_single_tag_3of3_button = (Button) findViewById(R.id.clear_search_single_tag_3of3_button);
        clear_search_single_tag_3of3_button.setOnClickListener(this);

        // Item Set Query Result Layout or Scan
        selected_actor_textview = (TextView)findViewById(R.id.selected_actor_textview);
        selected_actscene_textview = (TextView)findViewById(R.id.selected_actscene_textview);

        selected_actor_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());
        selected_actscene_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());

        byitemset_item_code_rvlist = (RecyclerView)findViewById(R.id.byitemset_item_code_rvlist);
        LinearLayoutManager itemCodelinearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        byitemset_item_code_rvlist.setLayoutManager(itemCodelinearLayoutManager);

        total_items_textview = (TextView)findViewById(R.id.total_items_textview);
        items_scanned_textview = (TextView)findViewById(R.id.items_scanned_textview);

        // By Item Code Filter
        byitemcode_filter_rvlist = (RecyclerView)findViewById(R.id.byitemcode_filter_rvlist);
        LinearLayoutManager itemCodeFilterlinearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        byitemcode_filter_rvlist.setLayoutManager(itemCodeFilterlinearLayoutManager);

        // By Item Code Query Result or Scan
        byitemcode_selected_code_textview = (TextView)findViewById(R.id.byitemcode_selected_code_textview);
        byitemcode_selected_type_textview = (TextView)findViewById(R.id.byitemcode_selected_type_textview);
        byitemcode_selected_size_textview = (TextView)findViewById(R.id.byitemcode_selected_size_textview);
        byitemcode_selected_number_textview = (TextView)findViewById(R.id.byitemcode_selected_number_textview);

        byitemcode_item_info_rvlist = (RecyclerView)findViewById(R.id.byitemcode_item_info_rvlist);
        LinearLayoutManager itemInfoFilterlinearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        byitemcode_item_info_rvlist.setLayoutManager(itemInfoFilterlinearLayoutManager);

        item_info_result_rvlist = (RecyclerView)findViewById(R.id.item_info_result_rvlist);
        LinearLayoutManager itemInfoResultlinearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        item_info_result_rvlist.setLayoutManager(itemInfoResultlinearLayoutManager);

        // Read single tag
        read_single_tag_rvlist = (RecyclerView)findViewById(R.id.read_single_tag_rvlist);
        LinearLayoutManager readSingleTaglinearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        read_single_tag_rvlist.setLayoutManager(readSingleTaglinearLayoutManager);

        // Write Single Tag
        write_single_tag_3of4_read_single_tag_rvlist = (RecyclerView)findViewById(R.id.write_single_tag_3of4_read_single_tag_rvlist);
        LinearLayoutManager writeSingleTaglinearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        write_single_tag_3of4_read_single_tag_rvlist.setLayoutManager(writeSingleTaglinearLayoutManager);

        total_item_info_textview = (TextView)findViewById(R.id.total_item_info_textview);
        item_info_scanned_textview = (TextView)findViewById(R.id.item_info_scanned_textview);


        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);

        tag_detail_actor_textview = (TextView)findViewById(R.id.tag_detail_actor_textview);
        tag_detail_actscene_textview = (TextView)findViewById(R.id.tag_detail_actscene_textview);
        tag_detail_code_textview = (TextView)findViewById(R.id.tag_detail_code_textview);
        tag_detail_type_textview = (TextView)findViewById(R.id.tag_detail_type_textview);
        tag_detail_size_textview = (TextView)findViewById(R.id.tag_detail_size_textview);
        tag_detail_number_textview = (TextView)findViewById(R.id.tag_detail_number_textview);
        tag_detail_current_box_textview = (TextView)findViewById(R.id.tag_detail_current_box_textview);
        tag_detail_epc_header_textview = (TextView)findViewById(R.id.tag_detail_epc_header_textview);
        tag_detail_epc_run_textview = (TextView)findViewById(R.id.tag_detail_epc_run_textview);

        // Write Single Tag
        write_single_tag_1of4_title_textview = (TextView)findViewById(R.id.write_single_tag_1of4_title_textview);
        write_single_tag_2of4_title_textview = (TextView)findViewById(R.id.write_single_tag_2of4_title_textview);

        write_single_tag_1of4_title_textview.setTypeface(UhfUtils.Companion.getFontKanitMedium());
        write_single_tag_2of4_title_textview.setTypeface(UhfUtils.Companion.getFontKanitMedium());

        write_single_tag_2of4_selected_actor_textview = (TextView)findViewById(R.id.write_single_tag_2of4_selected_actor_textview);
        write_single_tag_2of4_selected_actscene_textview = (TextView)findViewById(R.id.write_single_tag_2of4_selected_actscene_textview);

        write_single_tag_2of4_selected_actor_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());
        write_single_tag_2of4_selected_actscene_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());

        write_single_tag_2of4_item_code_rvlist = (RecyclerView)findViewById(R.id.write_single_tag_2of4_item_code_rvlist);
        LinearLayoutManager writeSingleTag2of4itemCodelinearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        write_single_tag_2of4_item_code_rvlist.setLayoutManager(writeSingleTag2of4itemCodelinearLayoutManager);

        write_single_tag_2of4_total_items_textview = (TextView)findViewById(R.id.write_single_tag_2of4_total_items_textview);
        write_single_tag_2of4_items_scanned_textview = (TextView)findViewById(R.id.write_single_tag_2of4_items_scanned_textview);

        // Write Single Tag 3 of 4
        write_single_tag_3of4_tag_detail_actor_textview = (TextView)findViewById(R.id.write_single_tag_3of4_tag_detail_actor_textview);
        write_single_tag_3of4_tag_detail_actscene_textview = (TextView)findViewById(R.id.write_single_tag_3of4_tag_detail_actscene_textview);

        write_single_tag_3of4_tag_detail_actor_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());
        write_single_tag_3of4_tag_detail_actscene_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());

        write_single_tag_3of4_tag_detail_epc_header_textview = (TextView)findViewById(R.id.write_single_tag_3of4_tag_detail_epc_header_textview);
        write_single_tag_3of4_tag_detail_epc_run_textview = (TextView)findViewById(R.id.write_single_tag_3of4_tag_detail_epc_run_textview);

        write_single_tag_3of4_tag_detail_code_textview = (TextView)findViewById(R.id.write_single_tag_3of4_tag_detail_code_textview);
        write_single_tag_3of4_tag_detail_type_textview = (TextView)findViewById(R.id.write_single_tag_3of4_tag_detail_type_textview);
        write_single_tag_3of4_tag_detail_size_textview = (TextView)findViewById(R.id.write_single_tag_3of4_tag_detail_size_textview);
        write_single_tag_3of4_tag_detail_number_textview = (TextView)findViewById(R.id.write_single_tag_3of4_tag_detail_number_textview);

        write_single_tag_3of4_tag_detail_current_box_textview = (TextView)findViewById(R.id.write_single_tag_3of4_tag_detail_current_box_textview);

        write_single_tag_3of4_tag_detail_code_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());
        write_single_tag_3of4_tag_detail_type_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());
        write_single_tag_3of4_tag_detail_size_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());
        write_single_tag_3of4_tag_detail_number_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());
        
        // Search Single Tag 3 of 4
        search_single_tag_3of3_tag_detail_actor_textview = (TextView)findViewById(R.id.search_single_tag_3of3_tag_detail_actor_textview);
        search_single_tag_3of3_tag_detail_actscene_textview = (TextView)findViewById(R.id.search_single_tag_3of3_tag_detail_actscene_textview);

        search_single_tag_3of3_tag_detail_actor_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());
        search_single_tag_3of3_tag_detail_actscene_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());

        search_single_tag_3of3_tag_detail_epc_header_textview = (TextView)findViewById(R.id.search_single_tag_3of3_tag_detail_epc_header_textview);
        search_single_tag_3of3_tag_detail_epc_run_textview = (TextView)findViewById(R.id.search_single_tag_3of3_tag_detail_epc_run_textview);

        search_single_tag_3of3_tag_detail_code_textview = (TextView)findViewById(R.id.search_single_tag_3of3_tag_detail_code_textview);
        search_single_tag_3of3_tag_detail_type_textview = (TextView)findViewById(R.id.search_single_tag_3of3_tag_detail_type_textview);
        search_single_tag_3of3_tag_detail_size_textview = (TextView)findViewById(R.id.search_single_tag_3of3_tag_detail_size_textview);
        search_single_tag_3of3_tag_detail_number_textview = (TextView)findViewById(R.id.search_single_tag_3of3_tag_detail_number_textview);

        search_single_tag_3of3_tag_detail_current_box_textview = (TextView)findViewById(R.id.search_single_tag_3of3_tag_detail_current_box_textview);

        search_single_tag_3of3_tag_detail_code_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());
        search_single_tag_3of3_tag_detail_type_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());
        search_single_tag_3of3_tag_detail_size_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());
        search_single_tag_3of3_tag_detail_number_textview.setTypeface(UhfUtils.Companion.getFontKanitLightItalic());

        search_single_tag_3of3_tag_detail_rssi_textview = (TextView)findViewById(R.id.search_single_tag_3of3_tag_detail_rssi_textview);
        search_single_tag_3of3_tag_detail_rssi_textview.setTypeface(UhfUtils.Companion.getFontKanitSemiBoldItalic());

        scan_area_message_textview= (TextView)findViewById(R.id.scan_area_message_textview);
        scan_area_message_textview.setTypeface(UhfUtils.Companion.getFontKanitSemiBold());

        scanned_epc_textview = (TextView)findViewById(R.id.scanned_epc_textview);
        scanned_epc_textview.setTypeface(UhfUtils.Companion.getFontKanitBoldItalic());

        scanned_rssi_textview = (TextView)findViewById(R.id.scanned_rssi_textview);
        scanned_rssi_textview.setTypeface(UhfUtils.Companion.getFontKanitSemiBoldItalic());

        scan_progress_gifview = (GifImageView) findViewById(R.id.scan_progress_gifview);

        scan_area_title_textview = (TextView)findViewById(R.id.scan_area_title_textview);
        scan_area_title_textview.setTypeface(UhfUtils.Companion.getFontKanitMedium());

        clearTagDetail();

    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private boolean isActSceneExistingInActSceneArray(String actScene) {
        boolean result = false;

        for(int i = 0; i < mActSceneArrayList.size(); i++) {
            Costume costume = mActSceneArrayList.get(i);
            if(costume.actScence.equals(actScene)) {
                result = true;
                break;
            }
        }

        return result;
    }

    private boolean isItemCodeExistingInItemCodeInfoArray(Costume itemCode) {
        boolean result = false;

        for(int i = 0; i < mItemInfoFilterArrayList.size(); i++) {
            Costume costume = mItemInfoFilterArrayList.get(i);
            if(costume.code.trim().equals(itemCode.code.trim()) &&
                    costume.type.trim().equals(itemCode.type.trim()) &&
                    costume.size.trim().equals(itemCode.size.trim()) &&
                    costume.codeNo.trim().equals(itemCode.codeNo.trim())) {
                result = true;
                break;
            }
        }

        return result;
    }

    public void refreshActScene(String actor, int screenType) {
        Log.d(TAG, "refreshActScene actor name=" + actor + " | screenType=" + screenType);

        // clear search and check data[selected actor, selected act scene]
        clearSearchandCheckData();

        processDialog = new ProgressDialog(mContext);
        processDialog.setMessage("Please  Wait ... refreshActScene");
        processDialog.setCancelable(false);
        processDialog.show();

        mActSceneArrayList = new ArrayList<Costume>();
        for(int i = 0; i < mCostumeArrayList.size(); i++) {
            Costume costume = mCostumeArrayList.get(i);
            if(costume.actor.equals(actor) && !isActSceneExistingInActSceneArray(costume.actScence)) {
                mActSceneArrayList.add(costume);
            }
        }

        switch (screenType) {
            case Constants.SCREEN_TYPE_BY_ITEM_SET: {
                actsceneRVAdapter = new ActSceneRVAdapter(mContext, mActivity, mActSceneArrayList);
                actscene_name_rvlist.setAdapter(actsceneRVAdapter);
                actsceneRVAdapter.notifyDataSetChanged();

                if(mActSceneArrayList.size() == 1) {
                    Costume costume = (Costume) mActSceneArrayList.get(0);
                    addSelectedActScene(costume.actScence);
                    selected_actscene_textview.setText(costume.actScence);
                } else {
                    selected_actscene_textview.setText("");
                }

                processDialog.dismiss();
                break;
            }

            case Constants.WRITE_SINGLE_TAG_MODE:
            case Constants.SEARCH_SINGLE_TAG_MODE: {
                actsceneWriteSingleTagRVAdapter = new ActSceneRVAdapter(mContext, mActivity, mActSceneArrayList);
                write_single_tag_1of4_actscene_name_rvlist.setAdapter(actsceneWriteSingleTagRVAdapter);
                actsceneWriteSingleTagRVAdapter.notifyDataSetChanged();

                if(mActSceneArrayList.size() == 1) {
                    Costume costume = (Costume) mActSceneArrayList.get(0);
                    addSelectedActScene(costume.actScence);

                    switch (screenType) {
                        case Constants.WRITE_SINGLE_TAG_MODE: {
                            write_single_tag_2of4_selected_actscene_textview.setText(costume.actScence);
                            write_single_tag_3of4_tag_detail_actscene_textview.setText(costume.actScence);
                            break;
                        }

                        case Constants.SEARCH_SINGLE_TAG_MODE: {
                            write_single_tag_2of4_selected_actscene_textview.setText(costume.actScence);
                            search_single_tag_3of3_tag_detail_actscene_textview.setText(costume.actScence);
                            break;
                        }
                    }

                } else {

                    switch (screenType) {
                        case Constants.WRITE_SINGLE_TAG_MODE: {
                            write_single_tag_2of4_selected_actscene_textview.setText("");
                            write_single_tag_3of4_tag_detail_actscene_textview.setText("");
                            break;
                        }

                        case Constants.SEARCH_SINGLE_TAG_MODE: {
                            write_single_tag_2of4_selected_actscene_textview.setText("");
                            search_single_tag_3of3_tag_detail_actscene_textview.setText("");
                            break;
                        }
                    }
                }

                processDialog.dismiss();
                break;
            }
        }

    }

    public void refreshItemCodeInfo(Costume itemCode) {
        Log.d(TAG, "refreshItemCodeInfo selected itemCode=" + itemCode.toString());
        mSelectedItemCodeFilter = itemCode;

        // clear item code info data[selected item code]
        clearItemCodeData();

        processDialog = new ProgressDialog(mContext);
        processDialog.setMessage("Please  Wait ... refreshItemCodeInfo");
        processDialog.setCancelable(false);
        processDialog.show();

        mItemInfoFilterArrayList = new ArrayList<Costume>();
        for(int i = 0; i < mCostumeArrayList.size(); i++) {
            Costume costume = mCostumeArrayList.get(i);
            costume.isFound = false;
            if(costume.code.trim().equals(itemCode.code.trim()) &&
                    costume.type.trim().equals(itemCode.type.trim()) &&
                    costume.size.trim().equals(itemCode.size.trim()) &&
                    costume.codeNo.trim().equals(itemCode.codeNo.trim())) {
//                    &&
//                    !isItemCodeExistingInItemCodeInfoArray(costume)) {
                mItemInfoFilterArrayList.add(costume);
            }
        }

        Log.d(TAG, "mItemInfoFilterArrayList.size=" + mItemInfoFilterArrayList.size());

        itemInfoFilterRVAdapter = new ItemInfoRVAdapter(mContext, mActivity, mItemInfoFilterArrayList);
        byitemcode_item_info_rvlist.setAdapter(itemInfoFilterRVAdapter);
        itemInfoFilterRVAdapter.notifyDataSetChanged();

//        if(mItemInfoFilterArrayList.size() == 1) {
            Costume costume = (Costume) mItemInfoFilterArrayList.get(0);
            addSelectedActScene(costume.actScence);
            byitemcode_selected_code_textview.setText(costume.code);
            byitemcode_selected_type_textview.setText(costume.type);
            byitemcode_selected_size_textview.setText(costume.size);
            byitemcode_selected_number_textview.setText(costume.codeNo);

//        } else {
//            byitemcode_selected_code_textview.setText("");
//            byitemcode_selected_type_textview.setText("");
//            byitemcode_selected_size_textview.setText("");
//            byitemcode_selected_number_textview.setText("");
//        }

        processDialog.dismiss();
    }


    /**
     * Query Item Info list by use selected Item Code.
     */
    public void queryItemInfoBySelectedItemCode() {
        String code = byitemcode_selected_code_textview.getText().toString();
        String type = byitemcode_selected_type_textview.getText().toString();
        String size = byitemcode_selected_size_textview.getText().toString();
        String number = byitemcode_selected_number_textview.getText().toString();

        Log.d(TAG, "query Item Info by code=" + code + " : type=" + type + " : size=" + size + " : number=" + number);

        processDialog = new ProgressDialog(mContext);
        processDialog.setMessage("Please  Wait ... queryItemInfoBySelectedItemCode");
        processDialog.setCancelable(false);
        processDialog.show();

        if(mSelectedItemCodeInfoArray.size() == 0) {
            mItemInfoArrayList = new ArrayList<Costume>();
            mItemInfoArrayList.clear();
            for (int i = 0; i < mCostumeArrayList.size(); i++) {
                Costume costume = mCostumeArrayList.get(i);
                costume.isFound = false;
                if (costume.code.trim().equals(code.trim()) && costume.type.trim().equals(type.trim()) && costume.size.trim().equals(size.trim()) && costume.codeNo.trim().equals(number.trim())) {
                    mItemInfoArrayList.add(costume);
                }
            }
        } else {
            mItemInfoArrayList = mSelectedItemCodeInfoArray;
        }
        itemInfoResultRVAdapter = new ItemInfoRVAdapter(mContext, mActivity, mItemInfoArrayList);

        item_info_result_rvlist.setAdapter(itemInfoResultRVAdapter);
        itemInfoResultRVAdapter.notifyDataSetChanged();
        total_item_info_textview.setText(Integer.toString(mItemInfoArrayList.size()));
        item_info_scanned_textview.setText(Integer.toString(0));

        processDialog.dismiss();
    }

    /**
     * Query Item Codes list by use selected actor and act, scene.
     */
    public void queryItemCodesBySelectedActorActScene() {
        String actor = "";
        String actScene = "";
        switch (mCurrentSearchMode) {
            case Constants.ITEM_SET_MODE: {
                actor = selected_actor_textview.getText().toString();
                actScene = selected_actscene_textview.getText().toString();
                break;
            }
            case Constants.WRITE_SINGLE_TAG_MODE:
            case Constants.SEARCH_SINGLE_TAG_MODE:{
                actor = write_single_tag_2of4_selected_actor_textview.getText().toString();
                actScene = write_single_tag_2of4_selected_actscene_textview.getText().toString();
                break;
            }
        }
        Log.d(TAG, "query ItemCodes by actor=" + actor + " : act, scene=" + actScene);

        processDialog = new ProgressDialog(mContext);
        processDialog.setMessage("Please  Wait ... queryItemCodesBySelectedActorActScene");
        processDialog.setCancelable(false);
        processDialog.show();

        mItemCodeArrayList = new ArrayList<Costume>();
        mItemCodeArrayList.clear();
        for (int i = 0; i < mCostumeArrayList.size(); i++) {
            Costume costume = mCostumeArrayList.get(i);
            costume.isFound = false;
            if (costume.actor.equals(actor) && costume.actScence.equals(actScene)) {
                mItemCodeArrayList.add(costume);
            }
        }

        switch (mCurrentSearchMode) {
            case Constants.ITEM_SET_MODE: {
                byitemset_itemCodeRVAdapter = new ItemCodeRVAdapter(mContext, mActivity, mItemCodeArrayList, Constants.ITEM_SET_MODE);
                byitemset_item_code_rvlist.setAdapter(byitemset_itemCodeRVAdapter);
                byitemset_itemCodeRVAdapter.notifyDataSetChanged();

                items_scanned_textview.setText(Integer.toString(0));
                total_items_textview.setText(Integer.toString(mItemCodeArrayList.size()));
                break;
            }
            case Constants.WRITE_SINGLE_TAG_MODE:
            case Constants.SEARCH_SINGLE_TAG_MODE: {
                writeSingleTag2of4_itemCodeRVAdapter = new ItemCodeRVAdapter(mContext, mActivity, mItemCodeArrayList, mCurrentSearchMode);
                write_single_tag_2of4_item_code_rvlist.setAdapter(writeSingleTag2of4_itemCodeRVAdapter);
                writeSingleTag2of4_itemCodeRVAdapter.notifyDataSetChanged();

                write_single_tag_2of4_items_scanned_textview.setText(Integer.toString(0));
                write_single_tag_2of4_total_items_textview.setText(Integer.toString(mItemCodeArrayList.size()));

                break;
            }
        }


        processDialog.dismiss();
    }

    /**
     * Select EPC code from database to write to the tag
     * @param costume
     * @param screenType
     */
    public void addSelectedCostumeToWriteTag(Costume costume, int screenType) {

        for(int i = 0; i < mCostumeArrayList.size(); i++) {
            Costume ct = mCostumeArrayList.get(i);
            if(ct.uid == costume.uid) {
                mSelectedCostumeToWrite = ct;
            }
        }

        switch (screenType) {
            case Constants.WRITE_SINGLE_TAG_MODE: {
                if(mSelectedCostumeToWrite.epcHeader.trim().equals("")) {
                    mEPCSelected = new com.handheld.upsizeuhf.model.EPC();
                    write_single_tag_3of4_tag_detail_epc_header_textview.setText("");
                    write_single_tag_3of4_tag_detail_epc_run_textview.setText("");
                } else {
                    String epcRaw = mSelectedCostumeToWrite.epcHeader + mSelectedCostumeToWrite.epcRun;
                    String epcTop = UhfUtils.Companion.separateEPCTopString(epcRaw, " ", 4, 16);
                    String epcBottom = UhfUtils.Companion.separateEPCBottomString(epcRaw, " ", 4, 16);

                    mEPCSelected.setEpcRaw(epcRaw);
                    mEPCSelected.setEpcHeader(mSelectedCostumeToWrite.epcHeader);
                    mEPCSelected.setEpcRun(mSelectedCostumeToWrite.epcRun);

                    write_single_tag_3of4_tag_detail_epc_header_textview.setText(epcTop);
                    write_single_tag_3of4_tag_detail_epc_run_textview.setText(epcBottom);
                }

                write_single_tag_3of4_tag_detail_code_textview.setText(!mSelectedCostumeToWrite.code.trim().equals("") ? mSelectedCostumeToWrite.code : "");
                write_single_tag_3of4_tag_detail_type_textview.setText(!mSelectedCostumeToWrite.type.trim().equals("") ? mSelectedCostumeToWrite.type : "");
                write_single_tag_3of4_tag_detail_size_textview.setText(!mSelectedCostumeToWrite.size.trim().equals("") ? mSelectedCostumeToWrite.size : "");
                write_single_tag_3of4_tag_detail_number_textview.setText(!mSelectedCostumeToWrite.codeNo.trim().equals("") ? mSelectedCostumeToWrite.codeNo : "");

                String currentBox = getCurrentBoxString(mSelectedCostumeToWrite);
                write_single_tag_3of4_tag_detail_current_box_textview.setText(currentBox);
                break;
            }
            case Constants.SEARCH_SINGLE_TAG_MODE: {
                if(mSelectedCostumeToWrite.epcHeader.trim().equals("")) {
                    mEPCSelected = new com.handheld.upsizeuhf.model.EPC();
                    search_single_tag_3of3_tag_detail_epc_header_textview.setText("");
                    search_single_tag_3of3_tag_detail_epc_run_textview.setText("");
                } else {
                    String epcRaw = mSelectedCostumeToWrite.epcHeader + mSelectedCostumeToWrite.epcRun;
                    String epcTop = UhfUtils.Companion.separateEPCTopString(epcRaw, " ", 4, 16);
                    String epcBottom = UhfUtils.Companion.separateEPCBottomString(epcRaw, " ", 4, 16);

                    mEPCSelected.setEpcRaw(epcRaw);
                    mEPCSelected.setEpcHeader(mSelectedCostumeToWrite.epcHeader);
                    mEPCSelected.setEpcRun(mSelectedCostumeToWrite.epcRun);

                    search_single_tag_3of3_tag_detail_epc_header_textview.setText(epcTop);
                    search_single_tag_3of3_tag_detail_epc_run_textview.setText(epcBottom);
                }

                search_single_tag_3of3_tag_detail_code_textview.setText(!mSelectedCostumeToWrite.code.trim().equals("") ? mSelectedCostumeToWrite.code : "");
                search_single_tag_3of3_tag_detail_type_textview.setText(!mSelectedCostumeToWrite.type.trim().equals("") ? mSelectedCostumeToWrite.type : "");
                search_single_tag_3of3_tag_detail_size_textview.setText(!mSelectedCostumeToWrite.size.trim().equals("") ? mSelectedCostumeToWrite.size : "");
                search_single_tag_3of3_tag_detail_number_textview.setText(!mSelectedCostumeToWrite.codeNo.trim().equals("") ? mSelectedCostumeToWrite.codeNo : "");

                String currentBox = getCurrentBoxString(mSelectedCostumeToWrite);
                search_single_tag_3of3_tag_detail_current_box_textview.setText(currentBox);
                break;
            }
        }
    }

    /**
     * add only one name in this time, but open for the future more than one name
     * @param actorName
     */
    public void addSelectedActor(String actorName, int screenType) {
        mSelectedActorFilter = actorName;
        mSelectedActorArray.clear();
        mSelectedActorArray.add(actorName);

        switch (screenType) {
            case Constants.ITEM_SET_MODE: {
                selected_actor_textview.setText(mSelectedActorArray.get(0).toString());

                if (selected_actor_textview.getText().toString().length() > 76) {
                    selected_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    if (selected_actor_textview.getText().toString().length() > 55) {
                        selected_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    } else {
                        if (selected_actor_textview.getText().toString().length() > 28) {
                            selected_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        } else {
                            selected_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                        }
                    }
                }
                break;
            }
            case Constants.WRITE_SINGLE_TAG_MODE: {
                write_single_tag_2of4_selected_actor_textview.setText(mSelectedActorArray.get(0).toString());

                if (write_single_tag_2of4_selected_actor_textview.getText().toString().length() > 76) {
                    write_single_tag_2of4_selected_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    if (write_single_tag_2of4_selected_actor_textview.getText().toString().length() > 55) {
                        write_single_tag_2of4_selected_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    } else {
                        if (write_single_tag_2of4_selected_actor_textview.getText().toString().length() > 28) {
                            write_single_tag_2of4_selected_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        } else {
                            write_single_tag_2of4_selected_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                        }
                    }
                }

                write_single_tag_3of4_tag_detail_actor_textview.setText(mSelectedActorArray.get(0).toString());
                if (write_single_tag_3of4_tag_detail_actor_textview.getText().toString().length() > 76) {
                    write_single_tag_3of4_tag_detail_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    if (write_single_tag_3of4_tag_detail_actor_textview.getText().toString().length() > 55) {
                        write_single_tag_3of4_tag_detail_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    } else {
                        if (write_single_tag_3of4_tag_detail_actor_textview.getText().toString().length() > 28) {
                            write_single_tag_3of4_tag_detail_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        } else {
                            write_single_tag_3of4_tag_detail_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                        }
                    }
                }
                break;
            }

            case Constants.SEARCH_SINGLE_TAG_MODE: {
                write_single_tag_2of4_selected_actor_textview.setText(mSelectedActorArray.get(0).toString());

                if (write_single_tag_2of4_selected_actor_textview.getText().toString().length() > 76) {
                    write_single_tag_2of4_selected_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    if (write_single_tag_2of4_selected_actor_textview.getText().toString().length() > 55) {
                        write_single_tag_2of4_selected_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    } else {
                        if (write_single_tag_2of4_selected_actor_textview.getText().toString().length() > 28) {
                            write_single_tag_2of4_selected_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        } else {
                            write_single_tag_2of4_selected_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                        }
                    }
                }

                search_single_tag_3of3_tag_detail_actor_textview.setText(mSelectedActorArray.get(0).toString());
                if (search_single_tag_3of3_tag_detail_actor_textview.getText().toString().length() > 76) {
                    search_single_tag_3of3_tag_detail_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                } else {
                    if (search_single_tag_3of3_tag_detail_actor_textview.getText().toString().length() > 55) {
                        search_single_tag_3of3_tag_detail_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                    } else {
                        if (search_single_tag_3of3_tag_detail_actor_textview.getText().toString().length() > 28) {
                            search_single_tag_3of3_tag_detail_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                        } else {
                            search_single_tag_3of3_tag_detail_actor_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 22);
                        }
                    }
                }
                break;
            }
        }

        mSelectedActSceneArray.clear();
        selected_actscene_textview.setText("");

    }

    public static void setAutoSizeTextTypeUniformWithConfiguration(
            @NonNull TextView textView,
            int autoSizeMinTextSize,
            int autoSizeMaxTextSize,
            int autoSizeStepGranularity,
            int unit) throws IllegalArgumentException {
        if (Build.VERSION.SDK_INT >= 27) {
            textView.setAutoSizeTextTypeUniformWithConfiguration(
                    autoSizeMinTextSize, autoSizeMaxTextSize, autoSizeStepGranularity, unit);
        } else if (textView instanceof AutoSizeableTextView) {
            TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(textView,
                    autoSizeMinTextSize, autoSizeMaxTextSize, autoSizeStepGranularity, unit);
        }
    }

    /**
     * add only one name in this time, but open for the future more than one name
     * @param actSceneName
     */
    public void addSelectedActScene(String actSceneName) {
        mSelectedActSceneArray.clear();
        mSelectedActSceneArray.add(actSceneName);
        selected_actscene_textview.setText(mSelectedActSceneArray.get(0).toString());
    }

    public String getServerIp() {
        return serverIp;
    }

    /**
     * add only one name in this time, but open for the future more than one name
     * @param itemInfo
     */
    public void addSelectedItemInfo(Costume itemInfo) {
        mSelectedItemCodeInfoArray.clear();
        mSelectedItemCodeInfoArray.add(itemInfo);

        byitemcode_selected_code_textview.setText(itemInfo.code);
        byitemcode_selected_type_textview.setText(itemInfo.type);
        byitemcode_selected_size_textview.setText(itemInfo.size);
        byitemcode_selected_number_textview.setText(itemInfo.codeNo);
    }

    /**
     * add only one name in this time, but open for the future more than one name
     * @param box
     */
    public void setSelectedCheckedBox(Box box) {
        mSelectedCheckedBox = box;
    }

    public Box getSelectedCheckedBox() {
        return mSelectedCheckedBox;
    }

    public void setSelectedCheckedBoxType(String boxType) {
        mSelectedCheckedBoxType = boxType;
    }

    public String getSelectedCheckedBoxType() {
        return mSelectedCheckedBoxType;
    }

    public ArrayList<Box> getmShipBoxArrayList() {
        return mShipBoxArrayList;
    }

    public ArrayList<Box> getmStorageBoxArrayList() {
        return mStorageBoxArrayList;
    }

    public ArrayList<Box> getmPlayBoxArrayList() {
        return mPlayBoxArrayList;
    }

    @Override
    public void onFinishCheckedBoxDialog(String msgBody) {
        loadData();

        if(mCurrentSearchMode == Constants.ITEM_SET_MODE) {
//            new RefreshActSceneListAsyncTask().execute();
            new RefreshActSceneListThread().start();
        } else if(mCurrentSearchMode == Constants.ITEM_CODE_MODE) {
//            new RefreshItemInfoListAsyncTask().execute();
            new RefreshItemInfoListThread().start();
        }

        loadSuccessDialog("", getString(R.string.checked_completed), msgBody);
    }

    @Override
    public void onFinishWriteSingleTagDialog(@NotNull int msgBody) {
        mSelectedCostumeToWrite.uid = msgBody;
        // reload costume item data for new epc case
        new ServiceQueryAsyncTask(mContext, mActivity, Constants.Companion.getCostumeAllQuery()).execute();

        new RefreshEPCSingleTagThread().start();
    }

    private class ReloadDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            loadData();
            return null;
        }
    }

    private class RefreshActSceneListAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            if(mCurrentSearchMode == Constants.ITEM_SET_MODE) {
                refreshActScene(mSelectedActorFilter, Constants.SCREEN_TYPE_BY_ITEM_SET);
            }
            return null;
        }
    }

    private class RefreshItemInfoListAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            refreshItemCodeInfo(mSelectedItemCodeFilter);
            return null;
        }
    }

    class RefreshActSceneListThread extends Thread {
        public void run() {
            try {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if(mCurrentSearchMode == Constants.ITEM_SET_MODE) {
                            refreshActScene(mSelectedActorFilter, Constants.SCREEN_TYPE_BY_ITEM_SET);
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class RefreshItemInfoListThread extends Thread {
        public void run() {
            try {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        refreshItemCodeInfo(mSelectedItemCodeFilter);
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inventory EPC Thread
     */
    class InventoryThread extends Thread {
        private List<TagModel> tagList;
        byte[] accessPassword = Tools.HexString2Bytes("00000000");

        @Override
        public void run() {
            super.run();
            while (runFlag) {
                if (startFlag || scanItemSetFlag || scanItemCodeFlag || scanSingleTagFlag || scanWriteSingleTagFlag || scanSearchSingleTagFlag) {
                    tagList = manager.inventoryRealTime(); //实时盘存
                    if (tagList != null && !tagList.isEmpty()) {
                        //播放提示音
//                        Util.play(1, 0);
                        for (TagModel tag : tagList) {
                            if (tag == null) {
                                String epcStr = "";
//								String epcStr = new String(epc);
                                addToList(listEPC, epcStr, (byte) -1);
                            } else {
                                String epcStr = Tools.Bytes2HexString(tag.getmEpcBytes(), tag.getmEpcBytes().length);
//								String epcStr = new String(epc);
                                byte rssi = tag.getmRssi();
                                addToList(listEPC, epcStr, rssi);
                            }

                        }
                    } else {
                        try {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if(mSearchResult != Constants.READYTOSCAN_SEARCH_RESULT) {
                                        displayScanResultGif(0);
                                    }
                                }
                            });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    tagList = null;
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    // EPC add to LISTVIEW
    private void addToList(final List<EPC> list, final String epc, final byte rssi) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // If do single scan, then clear scan list every time
                if(scanSingleTagFlag || scanWriteSingleTagFlag || scanSearchSingleTagFlag) {
                    Log.d(TAG, "removeAll: scanSingleTagFlag=" + scanSingleTagFlag + " : scanWriteSingleTagFlag=" + scanWriteSingleTagFlag + " : scanSearchSingleTagFlag=" + scanSearchSingleTagFlag);
                    list.removeAll(listEPC);
                }

                // The epc for the first time
                if (list.isEmpty()) {
                    EPC epcTag = new EPC();
                    epcTag.setEpc(epc);
                    epcTag.setCount(1);
                    epcTag.setRssi(rssi);
                    list.add(epcTag);
                    listepc.add(epc);
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        EPC epcLocal = list.get(i);
                        // list contain this epc
                        if (epc.equals(epcLocal.getEpc())) {
                            epcLocal.setCount(epcLocal.getCount() + 1);
                            epcLocal.setRssi(rssi);
                            list.set(i, epcLocal);
                            break;
                        } else if (i == (list.size() - 1)) {
                            // list doesn't contain this epc
                            EPC newEPC = new EPC();
                            newEPC.setEpc(epc);
                            newEPC.setCount(1);
                            newEPC.setRssi(rssi);
                            list.add(newEPC);
                            listepc.add(epc);
                        }
                    }
                }
                // add the data to ListView
                listMap = new ArrayList<Map<String, Object>>();
                int idcount = 1;
                for (EPC epcdata : list) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("ID", idcount);
//                    map.put("EPC", epcdata.getEpc());
//                    map.put("EPC", getTwoLineEPC(epcdata.getEpc()));
                    // Type A
                    map.put("EPCRaw", epcdata.getEpc());
                    map.put("EPC", separateEPCString(epcdata.getEpc(), " ", 4, 16));

                    map.put("EPCTop", separateEPCTopString(epcdata.getEpc(), " ", 4, 16));
                    map.put("EPCBottom", separateEPCBottomString(epcdata.getEpc(), " ", 4, 16));

                    // Type B
//                    map.put("EPC", separateEPCString(epcdata.getEpc(), " ", 19, 19));

                    map.put("COUNT", epcdata.getCount());
                    map.put("RSSI", epcdata.getRssi());
                    idcount++;
                    listMap.add(map);
                }
                Log.d(TAG, "list.size()=" + list.size());
                Log.d(TAG, "listepc.size()=" + listepc.size());
                Log.d(TAG, "listMap.size()=" + listMap.size());
                if(startFlag) {
                    Log.d(TAG, "original listMap.size()=" + listMap.size());
                    listViewData.setAdapter(new SimpleAdapter(UHFActivity.this,
                            listMap, R.layout.listview_item, new String[]{"ID",
                            "EPCTop", "EPCBottom", "COUNT", "RSSI"}, new int[]{
                            R.id.textView_list_item_id,
//                        R.id.textView_list_item_barcode,
                            R.id.textView_list_item_barcode_top,
                            R.id.textView_list_item_barcode_bottom,
                            R.id.textView_list_item_count,
                            R.id.textView_list_item_rssi}));
                    spinnerEPCRead.setAdapter(arr_adapter);
                    spinnerEPCLock.setAdapter(arr_adapter);

                    // play sound
                    Util.play(1, 0);
                } else if(scanItemSetFlag) {
                    Log.d(TAG, "scanItemSetFlag listMap.size()=" + listMap.size());

                    int scannedCount = 0;
                    for(int i = 0; i < mItemCodeArrayList.size(); i++) {
                        Costume costume = mItemCodeArrayList.get(i);
                        String epcBase = costume.epcHeader + costume.epcRun;
                        Log.d(TAG, "epcBase=" + epcBase);
                        for (int m = 0; m < listMap.size(); m++) {
                            Map map = listMap.get(m);

                            for (Object entry : map.entrySet()) {
                                String key = ((Map.Entry<String, Object>) entry).getKey();
                                Object value = ((Map.Entry<String, Object>) entry).getValue();
                                // do something with key and/or tab
                                Log.d(TAG, "key=" + key + " : value=" + value);
                                if (key.equals("EPCRaw") && value.equals(epcBase)) {
                                    Log.d(TAG, "isFound=true");
                                    costume.isFound = true;
                                    mItemCodeArrayList.set(i, costume);
                                    scannedCount++;

                                }
                            }
                        }

                    }

                    byitemset_itemCodeRVAdapter = new ItemCodeRVAdapter(mContext, mActivity, mItemCodeArrayList, Constants.ITEM_SET_MODE);
                    byitemset_item_code_rvlist.setAdapter(byitemset_itemCodeRVAdapter);
                    byitemset_itemCodeRVAdapter.notifyDataSetChanged();
                    items_scanned_textview.setText(Integer.toString(scannedCount));

                    Util.play(1, 0);

                } else if(scanItemCodeFlag) {
                    Log.d(TAG, "scanItemCodeFlag listMap.size()=" + listMap.size());

                    int scannedCount = 0;
                    for(int i = 0; i < mItemInfoArrayList.size(); i++) {
                        Costume costume = mItemInfoArrayList.get(i);
                        String epcBase = costume.epcHeader + costume.epcRun;
                        Log.d(TAG, "epcBase=" + epcBase);
                        for (int m = 0; m < listMap.size(); m++) {
                            Map map = listMap.get(m);

                            for (Object entry : map.entrySet()) {
                                String key = ((Map.Entry<String, Object>) entry).getKey();
                                Object value = ((Map.Entry<String, Object>) entry).getValue();
                                // do something with key and/or tab
                                Log.d(TAG, "key=" + key + " : value=" + value);
                                if (key.equals("EPCRaw") && value.equals(epcBase)) {
                                    costume.isFound = true;
                                    mItemInfoArrayList.set(i, costume);
                                    scannedCount++;

                                }
                            }
                        }

                    }

                    itemInfoResultRVAdapter = new ItemInfoRVAdapter(mContext, mActivity, mItemInfoArrayList);
                    item_info_result_rvlist.setAdapter(itemInfoResultRVAdapter);
                    itemInfoResultRVAdapter.notifyDataSetChanged();
                    item_info_scanned_textview.setText(Integer.toString(scannedCount));

                    Util.play(1, 0);

                } else if(scanSingleTagFlag || scanWriteSingleTagFlag || scanSearchSingleTagFlag) {
                    Log.d(TAG, "scanSingleTagFlag || scanWriteSingleTagFlag || scanSearchSingleTagFlag listMap.size()=" + listMap.size());

                    mItemInfoArrayList = new ArrayList<Costume>();
                    mItemInfoArrayList.clear();
                    int scannedCount = 0;
                    for (int m = 0; m < listMap.size(); m++) {
                        Costume costume = new Costume();
                        Map map = listMap.get(m);

                        for (Object entry : map.entrySet()) {
                            String key = ((Map.Entry<String, Object>) entry).getKey();
                            Object value = ((Map.Entry<String, Object>) entry).getValue();
                            // do something with key and/or tab
                            Log.d(TAG, "key=" + key + " : value=" + value);
                            if (key.equals("RSSI")) {
                                costume.size = value.toString();
                            }

                            if (key.equals("EPCTop")) {
                                costume.epcHeader = value.toString().replace(" ", "");
                            }

                            if (key.equals("EPCBottom")) {
                                costume.epcRun = value.toString().replace(" ", "");
                            }

                        }

                        mItemInfoArrayList.add(costume);
                        scannedCount++;
                    }

                    if(scanSingleTagFlag) {
                        epcTagRVAdapter = new EPCTagRVAdapter(mContext, mActivity, mItemInfoArrayList);
                        read_single_tag_rvlist.setAdapter(epcTagRVAdapter);
                        epcTagRVAdapter.notifyDataSetChanged();

                        if(mItemInfoArrayList.size() > 0) {
                            Costume singleScannedTag = mItemInfoArrayList.get(0);
//                            if(!singleScannedTag.epcHeader.trim().equalsIgnoreCase(mSingleScannedTag.epcHeader) || !singleScannedTag.epcRun.trim().equalsIgnoreCase(mSingleScannedTag.epcRun)) {
                                mSingleScannedTag = singleScannedTag;
                                displayTagCostumeDetail();
//                            }
                            String epcRaw = singleScannedTag.epcHeader + singleScannedTag.epcRun;
                            mEPCScanned.setEpcRaw(epcRaw);
                            mEPCScanned.setEpcHeader(singleScannedTag.epcHeader);
                            mEPCScanned.setEpcRun(singleScannedTag.epcRun);

                            Util.play(1, 0);

                        }
                    } else if(scanWriteSingleTagFlag) {
                        epcTagRVAdapter = new EPCTagRVAdapter(mContext, mActivity, mItemInfoArrayList);
                        write_single_tag_3of4_read_single_tag_rvlist.setAdapter(epcTagRVAdapter);
                        epcTagRVAdapter.notifyDataSetChanged();

                        if(mItemInfoArrayList.size() > 0) {
                            Costume singleScannedTag = mItemInfoArrayList.get(0);
//                            if(!singleScannedTag.epcHeader.trim().equalsIgnoreCase(mSingleScannedTag.epcHeader) || !singleScannedTag.epcRun.trim().equalsIgnoreCase(mSingleScannedTag.epcRun)) {
                                mSingleScannedTag = singleScannedTag;
                                String epcRaw = singleScannedTag.epcHeader + singleScannedTag.epcRun;
//                                String epcTop = UhfUtils.Companion.separateEPCTopString(epcRaw, " ", 4, 16);
//                                String epcBottom = UhfUtils.Companion.separateEPCBottomString(epcRaw, " ", 4, 16);


                                mEPCScanned.setEpcRaw(epcRaw);
                                mEPCScanned.setEpcHeader(singleScannedTag.epcHeader);
                                mEPCScanned.setEpcRun(singleScannedTag.epcRun);
//                            }
                            if(mEPCSelected.getEpcRaw().equalsIgnoreCase(mEPCScanned.getEpcRaw())) {
                                Util.play(1, 0);
                            }
                        }
                    } else if(scanSearchSingleTagFlag) {

                        if(mItemInfoArrayList.size() > 0) {
                            Costume singleScannedTag = mItemInfoArrayList.get(0);
                            mSingleScannedTag = singleScannedTag;
                            String epcRaw = singleScannedTag.epcHeader + singleScannedTag.epcRun;
//                                String epcTop = UhfUtils.Companion.separateEPCTopString(epcRaw, " ", 4, 16);
//                                String epcBottom = UhfUtils.Companion.separateEPCBottomString(epcRaw, " ", 4, 16);

                            mEPCScanned.setEpcRaw(epcRaw);
                            mEPCScanned.setEpcHeader(singleScannedTag.epcHeader);
                            mEPCScanned.setEpcRun(singleScannedTag.epcRun);

                            if(mEPCSelected.getEpcRaw().equalsIgnoreCase(mEPCScanned.getEpcRaw())) {
                                search_single_tag_3of3_tag_detail_rssi_textview.setText(singleScannedTag.size);
                                scanned_epc_textview.setText(UhfUtils.Companion.separateEPCString(epcRaw, " ", 4, 16));
                                scanned_rssi_textview.setText(singleScannedTag.size);
                                displayScanResultGif(Integer.parseInt(singleScannedTag.size));
                                Util.play(1, 0);
                            }
                        } else {
                            search_single_tag_3of3_tag_detail_rssi_textview.setText(getString(R.string.dash));
                            scanned_epc_textview.setText(getString(R.string.dash));
                            scanned_rssi_textview.setText(getString(R.string.dash));
                            mSearchFound = Constants.SEARCH_NOT_FOUND;
                            mSearchResult = Constants.SCANNOTFOUND_SEARCH_RESULT;
                            displayScanResultGif(0);
                        }
                    }
                }


            }
        });
    }

    private void displayScanResultGif(int rssi) {
        Log.d(TAG, "rssi=" + rssi);
        rssi = Math.abs(rssi);
        Log.d(TAG, "abs rssi=" + rssi);

        if (rssi < Constants.RSSI_CHANGING_POINT) {
            if(rssi == 0) {
                if(mSearchResult != Constants.SCANNOTFOUND_SEARCH_RESULT) {
                    scan_progress_gifview.setImageResource(R.drawable.scannotfound);
                    mSearchResult = Constants.SCANNOTFOUND_SEARCH_RESULT;
                }
            } else {
                if(mSearchResult != Constants.SCANFOUNDCLOSERRANGE_SEARCH_RESULT) {
                    scan_progress_gifview.setImageResource(R.drawable.scanfoundcloserange);
                    mSearchResult = Constants.SCANFOUNDCLOSERRANGE_SEARCH_RESULT;
                }
            }
        } else if (rssi > Constants.RSSI_CHANGING_POINT) {
            if(mSearchResult != Constants.SCANFOUNDSOMETHING_SEARCH_RESULT) {
                scan_progress_gifview.setImageResource(R.drawable.scanfoundsomething);
                mSearchResult = Constants.SCANFOUNDSOMETHING_SEARCH_RESULT;

                if(mSearchFound == Constants.SEARCH_NOT_FOUND) {
                    mSearchFound = Constants.SEARCH_FOUND;
                    // Count down 10 seconds
                    new CountDownTimer(10000, 1000) {

                        public void onTick(long millisUntilFinished) {
//                        mTextField.setText("seconds remaining: " + millisUntilFinished / 1000);
                            scan_area_title_textview.setText("Power " + mCurrentSearchPower + "dBm - " + millisUntilFinished / 1000);
                            scan_area_message_textview.setText(getString(R.string.dash));
                        }

                        public void onFinish() {
//                        mTextField.setText("done!");
                            // Try to stop scan thread before set the new power value.
                            thread.interrupt();

                            if ((mCurrentSearchPowerIndex) == (listSearchPower.size() - 1)) {
                                scan_area_title_textview.setText("Power " + mCurrentSearchPower + "dBm");
                                scan_area_message_textview.setText("reach lowest limit power!");
                            } else {
                                scan_area_title_textview.setText("Power " + mCurrentSearchPower + "dBm");
                                scan_area_message_textview.setText("timeup!");
                            }
                            search_single_tag_3of3_tag_detail_rssi_textview.setText(getString(R.string.dash));
                            scanned_rssi_textview.setText(getString(R.string.dash));
                            scanned_epc_textview.setText(getString(R.string.dash));

                            // Reduce power one step every 10 second
                            if ((mCurrentSearchPowerIndex + 1) <= (listSearchPower.size() - 1)) {
                                mCurrentSearchPowerIndex = mCurrentSearchPowerIndex + 1;
                                mCurrentSearchPower = listSearchPower.get(mCurrentSearchPowerIndex).intValue();
                                scan_area_title_textview.setText("Power " + mCurrentSearchPower + "dBm");
                                scan_area_message_textview.setText("is setting...");
                                mSearchFound = Constants.SEARCH_NOT_FOUND;

                                if (manager.setOutputPower(mCurrentSearchPower)) {
                                    scan_area_title_textview.setText("Power " + mCurrentSearchPower + "dBm");
                                    scan_area_message_textview.setText("ready!");
                                } else {
                                    scan_area_title_textview.setText("Power " + mCurrentSearchPower + "dBm");
                                    scan_area_message_textview.setText("setup failed?!, please try to stop/scan again.");
                                }

                                thread.start();

                            } else {
                                thread.start();
                            }

                        }
                    }.start();
                }

            }
        }
    }

    /**
     * Match scanned epc to costume database and displat tag detail
     */
    private void displayTagCostumeDetail() {
        clearTagDetail();

        String scannedEPCRaw = mSingleScannedTag.epcHeader + mSingleScannedTag.epcRun;
        String scannedEPCHeader = scannedEPCRaw.substring(0, 19);
        String scannedEPCRun = scannedEPCRaw.substring(19);

        for (int i = 0; i < mCostumeArrayList.size(); i++) {
            Costume costume = mCostumeArrayList.get(i);

//            if (costume.epcHeader.trim().equalsIgnoreCase(mSingleScannedTag.epcHeader.replace(" ", "").trim()) && costume.epcRun.trim().equalsIgnoreCase(mSingleScannedTag.epcRun.replace(" ", "").trim())) {
            if (costume.epcHeader.trim().equalsIgnoreCase(scannedEPCHeader) && costume.epcRun.trim().equalsIgnoreCase(scannedEPCRun)) {
                tag_detail_actor_textview.setText(costume.actor);
                tag_detail_actscene_textview.setText(costume.actScence);
                tag_detail_code_textview.setText(costume.code);
                tag_detail_type_textview.setText(costume.type);
                tag_detail_size_textview.setText(costume.size);
                tag_detail_number_textview.setText(costume.codeNo);

                String epcRaw = costume.epcHeader + costume.epcRun;
                String epcTop = UhfUtils.Companion.separateEPCTopString(epcRaw, " ", 4, 16);
                String epcBottom = UhfUtils.Companion.separateEPCBottomString(epcRaw, " ", 4, 16);

                tag_detail_epc_header_textview.setText(epcTop);
                tag_detail_epc_run_textview.setText(epcBottom);

                mEPCScanned.setEpcRaw(epcRaw);
                mEPCScanned.setEpcHeader(costume.epcHeader);
                mEPCScanned.setEpcRun(costume.epcRun);

                String currentBox = getCurrentBoxString(costume);
                tag_detail_current_box_textview.setText(currentBox);

            }
        }
    }

    private void clearTagDetail() {
        tag_detail_actor_textview.setText("");
        tag_detail_actscene_textview.setText("");
        tag_detail_code_textview.setText("");
        tag_detail_type_textview.setText("");
        tag_detail_size_textview.setText("");
        tag_detail_number_textview.setText("");
        tag_detail_current_box_textview.setText("");
        tag_detail_epc_header_textview.setText("");
        tag_detail_epc_run_textview.setText("");
    }

    private String getCurrentBoxString(Costume costume) {
        String result = "";
        if(!costume.shipBox.equalsIgnoreCase("")) {
            result = mActivity.getString(R.string.ship_box) + ": " + costume.shipBox;
        } else if(!costume.storageBox.equalsIgnoreCase("")) {
            result = mActivity.getString(R.string.storage_box) + ": " + costume.storageBox;
        } else if(!costume.playBox.equalsIgnoreCase("")) {
            result = mActivity.getString(R.string.play_box) + ": " + costume.playBox;
        }
        return result;
    }

    private String getTwoLineEPC(String epc) {
        String result = epc;
        if(epc.length() > 2) {
            int halfPos = (epc.length() / 2);
            result = epc.substring(0, halfPos) + "\n" + epc.substring(halfPos);

        }
        Log.d(TAG, "epc=" + epc);
        Log.d(TAG, "separateEPCString="+separateEPCString(epc, " ", 4, 16));


        return result;
    }

    private String separateEPCString(String data, String separator, int separateLen, int numPerLine) {
        String resultStr = "";
        if(data != null) {
            String result = data;
            Log.d(TAG, "data=" + data);
            StringBuffer buffer = new StringBuffer();
            for(int i = 0; i < result.length(); i++) {

                Log.d(TAG, "i=" + i);
                Log.d(TAG, "result=" + result);
//                Log.d(TAG, "result.substring(i, 1)=" + result.substring(i, 1));
                Log.d(TAG, "result.charAt(i)=" + result.charAt(i));
//                buffer.append(result.substring(i, 1));
                buffer.append(result.charAt(i));
//                if(i < result.length() - 1) {
                if((i + 1) % separateLen == 0) {
                    buffer.append(separator);
                }

                if(numPerLine > 0 && ((i + 1) % numPerLine == 0) && (i < result.length() - 1)) {
                    buffer.append("\n");
                }

                Log.d(TAG, "buffer=" + buffer.toString());
            }
            resultStr = buffer.toString();
        }

        return resultStr;
    }

    private String separateEPCTopString(String data, String separator, int separateLen, int numPerLine) {
        String resultStr = "";
        if(data != null) {
            String result = data;
            Log.d(TAG, "data=" + data);
            StringBuffer buffer = new StringBuffer();
            for(int i = 0; i < result.length(); i++) {

                Log.d(TAG, "i=" + i);
                Log.d(TAG, "result=" + result);
//                Log.d(TAG, "result.substring(i, 1)=" + result.substring(i, 1));
                Log.d(TAG, "result.charAt(i)=" + result.charAt(i));
//                buffer.append(result.substring(i, 1));
                buffer.append(result.charAt(i));
//                if(i < result.length() - 1) {
                if((i + 1) % separateLen == 0) {
                    buffer.append(separator);
                }

                if(numPerLine > 0 && ((i + 1) % numPerLine == 0) && (i < result.length() - 1)) {
//                    buffer.append("\n");
                    break;
                }

                Log.d(TAG, "buffer=" + buffer.toString());
            }
            resultStr = buffer.toString();
        }

        return resultStr;
    }

    private String separateEPCBottomString(String data, String separator, int separateLen, int numPerLine) {
        String resultStr = "";
        if(data != null) {
            String result = data;
            Log.d(TAG, "data=" + data);
            StringBuffer buffer = new StringBuffer();
            for(int i = 0; i < result.length(); i++) {



                if(numPerLine > 0 && ((i + 1) > numPerLine) && (i < result.length())) {
//                    buffer.append("\n");
                    Log.d(TAG, "i=" + i);
                    Log.d(TAG, "result=" + result);
//                Log.d(TAG, "result.substring(i, 1)=" + result.substring(i, 1));
                    Log.d(TAG, "result.charAt(i)=" + result.charAt(i));
//                buffer.append(result.substring(i, 1));
                    buffer.append(result.charAt(i));
//                if(i < result.length() - 1) {
                    if((i + 1) % separateLen == 0) {
                        buffer.append(separator);
                    }
                }

                Log.d(TAG, "buffer=" + buffer.toString());
            }
            resultStr = buffer.toString();
        }

        return resultStr;
    }


    // Make the button clickable or unclickable
    private void setButtonClickable(Button button, boolean flag) {
        button.setClickable(flag);
        if (flag) {
            button.setTextColor(Color.BLACK);
        } else {
            button.setTextColor(Color.GRAY);
        }
    }


    /**
     * clear list and listview O
     */
    private void clearData() {
        listEPC.removeAll(listEPC);
        listViewData.setAdapter(null);
        listepc.removeAll(listepc);
    }

    private void clearSearchandCheckData() {
        mSelectedActorArray = new ArrayList<String>();
        mSelectedActSceneArray = new ArrayList<String>();
        mSelectedActorArray.clear();
        mSelectedActSceneArray.clear();

    }

    private void clearItemCodeData() {
        mSelectedItemCodeFilterArray = new ArrayList<Costume>();
        mSelectedItemCodeInfoArray = new ArrayList<Costume>();
        mSelectedItemCodeFilterArray.clear();
        mSelectedItemCodeInfoArray.clear();
    }

    @Override
    public void onClick(View v) {
        byte[] accessPassword = Tools.HexString2Bytes(editPassword.getText()
                .toString());
        addr = Integer.valueOf(editAddr.getText().toString());
        length = Integer.valueOf(editLength.getText().toString());
        switch (v.getId()) {
            case R.id.by_item_set_button: {
                    Util.play(1, 0);
                    Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                    animate.setAnimationListener(new ByItemSetButtonAnimationListener());
                    by_item_set_button.startAnimation(animate);
                }
                break;

            case R.id.by_item_code_button: {
                    Util.play(1, 0);
                    Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                    animate.setAnimationListener(new ByItemCodeButtonAnimationListener());
                    by_item_code_button.startAnimation(animate);
                }
                break;

            case R.id.read_single_tag_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new ReadSingleTagButtonAnimationListener());
                read_single_tag_button.startAnimation(animate);
            }
            break;

            case R.id.write_single_tag_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new WriteSingleTagButtonAnimationListener(v));
                write_single_tag_button.startAnimation(animate);
            }
            break;

            case R.id.search_single_tag_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new WriteSingleTagButtonAnimationListener(v));
                search_single_tag_button.startAnimation(animate);
            }
            break;

            // Item Set Filter =================
            case R.id.back_byitemset_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new BackByItemSetFilterButtonAnimationListener());
                back_byitemset_button.startAnimation(animate);
            }
            break;

            case R.id.next_byitemset_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new NextByItemSetFilterButtonAnimationListener());
                next_byitemset_button.startAnimation(animate);
            }
            break;

            case R.id.back_itemset_info_result_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new BackByItemSetInfoResultButtonAnimationListener());
                back_itemset_info_result_button.startAnimation(animate);
            }
            break;

            case R.id.scan_itemset_info_result_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new ScanItemSetButtonAnimationListener());
                scan_itemset_info_result_button.startAnimation(animate);
            }
            break;

            case R.id.clear_itemset_info_result_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new ClearItemSetButtonAnimationListener());
                clear_itemset_info_result_button.startAnimation(animate);
            }
            break;

            case R.id.check_itemset_info_result_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new CheckItemSetButtonAnimationListener());
                check_itemset_info_result_button.startAnimation(animate);
            }
            break;

            // Item Codes Filter =================
            case R.id.back_byitemcode_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new BackByItemCodeFilterButtonAnimationListener());
                back_byitemcode_button.startAnimation(animate);
            }
            break;

            case R.id.next_byitemcode_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new NextByItemCodeFilterButtonAnimationListener());
                next_byitemcode_button.startAnimation(animate);
            }
            break;

            case R.id.back_itemcode_info_result_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new BackByItemCodeInfoResultButtonAnimationListener());
                back_itemcode_info_result_button.startAnimation(animate);
            }
            break;

            case R.id.scan_itemcode_info_result_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new ScanItemCodeButtonAnimationListener());
                scan_itemcode_info_result_button.startAnimation(animate);
            }
            break;

            case R.id.clear_itemcode_info_result_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new ClearItemCodeButtonAnimationListener());
                clear_itemcode_info_result_button.startAnimation(animate);
            }
            break;

            case R.id.check_itemcode_info_result_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new CheckItemCodeButtonAnimationListener());
                check_itemcode_info_result_button.startAnimation(animate);
            }
            break;

            // Read Single Tag
            case R.id.back_read_single_tag_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new BackReadSingleTagButtonAnimationListener());
                back_read_single_tag_button.startAnimation(animate);
            }
            break;

            case R.id.scan_read_single_tag_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new ScanSingleTagButtonAnimationListener());
                scan_read_single_tag_button.startAnimation(animate);
            }
            break;

            case R.id.clear_read_single_tag_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new ClearSingleTagButtonAnimationListener());
                clear_read_single_tag_button.startAnimation(animate);
            }
            break;

            case R.id.write_read_single_tag_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new WriteReadSingleTagButtonAnimationListener());
                write_read_single_tag_button.startAnimation(animate);
            }
            break;

            // Write Single Tag 1of4
            case R.id.back_write_single_tag_1of4_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new BackWriteSingleTag1of4ButtonAnimationListener());
                back_write_single_tag_1of4_button.startAnimation(animate);
            }
            break;

            case R.id.next_write_single_tag_1of4_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new NextWriteSingleTag1of4ButtonAnimationListener(v));
                next_write_single_tag_1of4_button.startAnimation(animate);
            }
            break;

            // Write Single Tag 2of4
            case R.id.back_write_single_tag_2of4_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new BackWriteSingleTag2of4ButtonAnimationListener());
                back_write_single_tag_2of4_button.startAnimation(animate);
            }
            break;

            case R.id.next_write_single_tag_2of4_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new NextWriteSingleTag2of4ButtonAnimationListener());
                next_write_single_tag_2of4_button.startAnimation(animate);
                break;
            }

            // Write Single Tag 3of4
            case R.id.back_write_single_tag_3of4_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new BackWriteSingleTag3of4ButtonAnimationListener(v));
                back_write_single_tag_3of4_button.startAnimation(animate);
            }
            break;

            case R.id.scan_write_single_tag_3of4_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new ScanWriteSingleTag3of4ButtonAnimationListener());
                scan_write_single_tag_3of4_button.startAnimation(animate);
                break;
            }

            case R.id.clear_write_single_tag_3of4_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new ClearWriteSingleTag3of4ButtonAnimationListener());
                clear_write_single_tag_3of4_button.startAnimation(animate);
                break;
            }

            case R.id.write_write_single_tag_3of4_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new WriteWriteSingleTag3of4ButtonAnimationListener());
                write_write_single_tag_3of4_button.startAnimation(animate);
                break;
            }

            // Search Single Tag
            case R.id.back_search_single_tag_3of3_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new BackWriteSingleTag3of4ButtonAnimationListener(v));
                back_search_single_tag_3of3_button.startAnimation(animate);
                break;
            }

            case R.id.scan_search_single_tag_3of3_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new ScanSearchSingleTag3of3ButtonAnimationListener());
                scan_search_single_tag_3of3_button.startAnimation(animate);
                break;
            }

            case R.id.clear_search_single_tag_3of3_button: {
                Util.play(1, 0);
                Animation animate = AnimationUtils.Companion.getBounceAnimation(getApplicationContext());
                animate.setAnimationListener(new ClearSearchSingleTag3of3ButtonAnimationListener());
                clear_search_single_tag_3of3_button.startAnimation(animate);
                break;
            }

            
            // Original inventory
            case R.id.button_start:
                if (!startFlag) {
                    startFlag = true;
                    buttonStart.setText(R.string.stop_inventory);
                } else {
                    startFlag = false;
                    buttonStart.setText(R.string.inventory);
                }
                break;
            case R.id.button_clear:
                int frequency = manager.getFrequency();
                clearData();
                break;
            case R.id.button_read:
                manager.selectEPC(Tools.HexString2Bytes(selectEpc));
                if (accessPassword.length != 4) {
                    showToast(getString(R.string.password_is_4_bytes));
                    return;
                }
                // read data
                byte[] data = manager.readFrom6C(membank, addr, length, accessPassword);
                if (data != null && data.length > 1) {
                    String dataStr = Tools.Bytes2HexString(data, data.length);
                    editReadData.append(getString(R.string.read_data_) + dataStr + "\n");
                } else {
                    if (data != null) {
                        editReadData.append(getString(R.string.read_fail_error) + (data[0] & 0xff) + "\n");
                        return;
                    }
                    editReadData.append(getString(R.string.read_fail_return_null) + "\n");
                }
                break;
            // Write data to tag
            case R.id.button_write:
                manager.selectEPC(Tools.HexString2Bytes(selectEpc));
                if (accessPassword.length != 4) {
                    showToast(getString(R.string.password_is_4_bytes));
                    return;
                }


                String writeData = editWriteData.getText().toString();
                if (writeData.length() % 4 != 0 || writeData.equals("")) {
                    showToast(getString(R.string.the_unit_is_word_1word_2bytes));
                    return;
                }
                if(!isNumeric(writeData)){
                    showToast(getString(R.string.write_hex));
                    return;
                }
                byte[] dataBytes = Tools.HexString2Bytes(writeData);
                // dataLen = dataBytes/2 dataLen
                boolean writeFlag = manager.writeTo6C(accessPassword, membank, addr, dataBytes.length / 2, dataBytes);
                if (writeFlag) {
                    editReadData.append(getString(R.string.write_successful_) + "\n");
                } else {
                    editReadData.append(getString(R.string.write_failue_) + "\n");
                }
                break;
            // lock tag
            case R.id.button_lock_6c:
                manager.selectEPC(Tools.HexString2Bytes(selectEpc));
                String password = editAccesslock.getText().toString();
                if (manager.lock6C(Tools.HexString2Bytes(password), lockMem, lockType)) {
                    showToast(getString(R.string.lock_successful_));
                } else {
                    showToast(getString(R.string.lock_fail_));
                }
                break;
            // kill tag
            case R.id.button_kill_6c:
                final AlertDialog dlg = new AlertDialog.Builder(UHFActivity.this)
                        .setTitle(R.string.sure_kill)
                        .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                                manager.selectEPC(Tools.HexString2Bytes(selectEpc));
                                String killPassword = ((EditText) findViewById(R.id.edit_kill_password)).getText().toString();
                                if (manager.kill6C(Tools.HexString2Bytes(killPassword))) {
                                    showToast("Kill success!");
                                } else {
                                    showToast("Kill fail!");
                                }
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .create();
                dlg.show();
                break;
            // clear read data
            case R.id.button_readClear:
                editReadData.setText("");
                break;
            case R.id.button_min:
                manager.setSensitivity(sensitive);
                showToast(getString(R.string.setSuccess));
                break;
            case R.id.button_plus:
                editor.putInt("power", power);
                editor.commit();
                if (manager.setOutputPower(power)) {
                    showToast(getString(R.string.setSuccess));
                }
                break;
            case R.id.button_set:
                manager.setWorkArea(area);
                editor.putInt("area", area);
                editor.commit();
                showToast(getString(R.string.setSuccess));
                break;
            case R.id.button4:
                String freqStr = editFrequency.getText().toString();
                if (freqStr == null || "".equals(freqStr)) {
                    showToast(getString(R.string.freqSetting));
                    return;
                }
//			manager.setFrequency(frequency, 0, 0);
                showToast(getString(R.string.setSuccess));
                break;
            case R.id.button5:
                String serverIpStr = editServerIp.getText().toString();
                if (serverIpStr == null || "".equals(serverIpStr)) {
                    showToast(getString(R.string.server_ip));
                    return;
                }
                editor.putString("serverIp", serverIpStr);
                editor.commit();
                showToast(getString(R.string.setSuccess));
                break;
            case R.id.button_uhf_more_settings:
                l5moresettings.setVisibility(View.GONE);
                AlertDialog dlg2 = new AlertDialog.Builder(UHFActivity.this)
                        .setTitle(R.string.note_the_following_operation_may_lead_to_module_does_not_work_)

                        .setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                l5moresettings.setVisibility(View.VISIBLE);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        })
                        .create();
                dlg2.show();

                break;
            default:
                break;
        }
    }

    private class ByItemSetButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            // Clear select actor
            mSelectedActorFilter = "";

            SetVisible(byitemset_select_filter_layout, textViewS1, viewS1);
            // Load Actor List
            new LoadActorThread(byitemset_select_filter_layout).start();

        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class ByItemCodeButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            // clear select item code
            mSelectedItemCodeFilter = new Costume();

            SetVisible(byitemcode_select_filter_layout, textViewS1, viewS1);

            new LoadItemCodeThread().start();
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class ReadSingleTagButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            mCurrentSearchMode = Constants.READ_SINGLE_TAG_MODE;
            // do no clear scan single rv list
            new RefreshEPCSingleTagThread().start();

            SetVisible(read_single_tag_layout, textViewS1, viewS1);
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class WriteSingleTagButtonAnimationListener implements Animation.AnimationListener   {
        View view;
        public WriteSingleTagButtonAnimationListener(View v) {
            view = v;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            if(isOnline) {
                switch (view.getId()) {
                    case R.id.write_single_tag_button: {
                        mCurrentSearchMode = Constants.WRITE_SINGLE_TAG_MODE;
                        write_single_tag_1of4_title_textview.setText(getString(R.string.select_act_1of4));
                        break;
                    }
                    case R.id.search_single_tag_button: {
                        mCurrentSearchMode = Constants.SEARCH_SINGLE_TAG_MODE;
                        write_single_tag_1of4_title_textview.setText(getString(R.string.search_select_act_1of3));
                        break;
                    }
                }

                // Clear select actor
                mSelectedActorFilter = "";

                SetVisible(write_single_tag_1of4_layout, textViewS1, viewS1);

                // Load Actor List
                new LoadActorThread(write_single_tag_1of4_layout).start();
            } else {
                loadWarningDialog("", getString(R.string.database_server_offline), getString(R.string.database_server_offline_solution));

                write_single_tag_button.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                write_single_tag_button.setEnabled(false);

                search_single_tag_button.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                search_single_tag_button.setEnabled(false);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class BackByItemSetFilterButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            SetVisible(searchandcheck_layout, textViewS1, viewS1);
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class BackByItemCodeFilterButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            SetVisible(searchandcheck_layout, textViewS1, viewS1);
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class NextByItemSetFilterButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            mCurrentSearchMode = Constants.ITEM_SET_MODE;
            String actor = selected_actor_textview.getText().toString();
            String actScene = selected_actscene_textview.getText().toString();
            Log.d(TAG, "query ItemCodes by actor=" + actor + " : act, scene=" + actScene);

            if(actor.equalsIgnoreCase("") && actScene.equalsIgnoreCase("")) {
                loadWarningDialog("", getString(R.string.actor_actscene_notfound), getString(R.string.actor_actscene_notfound_solution));
            } else {
                SetVisible(byitemset_queryresult_layout, textViewS1, viewS1);
                queryItemCodesBySelectedActorActScene();
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class NextByItemCodeFilterButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            String code = byitemcode_selected_code_textview.getText().toString();
            String type = byitemcode_selected_type_textview.getText().toString();
            String size = byitemcode_selected_size_textview.getText().toString();
            String number = byitemcode_selected_number_textview.getText().toString();

            mCurrentSearchMode = Constants.ITEM_CODE_MODE;

            Log.d(TAG, "query Item Info by code=" + code + " : type=" + type + " : size=" + size + " : number=" + number);

            if(code.equalsIgnoreCase("") && type.equalsIgnoreCase("") && size.equalsIgnoreCase("")) {
                loadWarningDialog("", getString(R.string.itemcode_notfound), getString(R.string.itemcode_notfound_solution));
            } else {
                SetVisible(byitemcode_queryresult_layout, textViewS1, viewS1);
                queryItemInfoBySelectedItemCode();
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private void initQueryResultLayout() {
        // init actor and act scene text view
    }

    private class BackByItemSetInfoResultButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            thread.interrupt();
            scanItemSetFlag = false;
            scan_itemset_info_result_button.setText(R.string.scan);

            SetVisible(byitemset_select_filter_layout, textViewS1, viewS1);
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class BackByItemCodeInfoResultButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            thread.interrupt();
            scanItemCodeFlag = false;
            scan_itemcode_info_result_button.setText(R.string.scan);

            SetVisible(byitemcode_select_filter_layout, textViewS1, viewS1);
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class ScanItemSetButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            if (!scanItemSetFlag) {
                thread.start();
                scanItemSetFlag = true;
                scan_itemset_info_result_button.setText(R.string.stop);
            } else {
                thread.interrupt();
                scanItemSetFlag = false;
                scan_itemset_info_result_button.setText(R.string.scan);
            }
//            SetVisible(ls1searchandcheck, textViewS1, viewS1);
//            if(MySQLUtils.Companion.getConnection()) {
//                Log.d(TAG, "DB connected success!!....");
//                MySQLUtils.Companion.executeMySQLQuery("SHOW DATABASES;");
//            } else {
//                Log.d(TAG, "DB connected failed!!....");
//            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class ScanItemCodeButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            if (!scanItemCodeFlag) {
                thread.start();
                scanItemCodeFlag = true;
                scan_itemcode_info_result_button.setText(R.string.stop);
            } else {
                thread.interrupt();
                scanItemCodeFlag = false;
                scan_itemcode_info_result_button.setText(R.string.scan);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class ClearItemSetButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            thread.interrupt();
            scanItemSetFlag = false;
            scan_itemset_info_result_button.setText(R.string.scan);

            queryItemCodesBySelectedActorActScene();
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class BackReadSingleTagButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            thread.interrupt();
            scanSingleTagFlag = false;
            scan_read_single_tag_button.setText(R.string.scan);

            SetVisible(searchandcheck_layout, textViewS1, viewS1);
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class BackWriteSingleTag1of4ButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            SetVisible(searchandcheck_layout, textViewS1, viewS1);
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class NextWriteSingleTag1of4ButtonAnimationListener implements Animation.AnimationListener   {
        View view;
        public NextWriteSingleTag1of4ButtonAnimationListener(View v) {
            view = v;
        }

        @Override
        public void onAnimationStart(Animation animation) {
//            mCurrentSearchMode = Constants.WRITE_SINGLE_TAG_MODE;
            switch (mCurrentSearchMode) {
                case Constants.WRITE_SINGLE_TAG_MODE: {
                    write_single_tag_2of4_title_textview.setText(getString(R.string.select_item_2of4));
                    break;
                }
                case Constants.SEARCH_SINGLE_TAG_MODE: {
                    write_single_tag_2of4_title_textview.setText(getString(R.string.search_select_act_2of3));
                    break;
                }
            }

            mEPCSelected = new com.handheld.upsizeuhf.model.EPC();
            mSelectedCostumeToWrite = new Costume();

            String actor = write_single_tag_2of4_selected_actor_textview.getText().toString();
            String actScene = write_single_tag_2of4_selected_actscene_textview.getText().toString();
            Log.d(TAG, "query ItemCodes by actor=" + actor + " : act, scene=" + actScene);

            if(actor.equalsIgnoreCase("") && actScene.equalsIgnoreCase("")) {
                loadWarningDialog("", getString(R.string.actor_actscene_notfound), getString(R.string.actor_actscene_notfound_solution));
            } else {
                SetVisible(write_single_tag_2of4_layout, textViewS1, viewS1);
                queryItemCodesBySelectedActorActScene();

            }


        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class NextWriteSingleTag2of4ButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
//            mCurrentSearchMode = Constants.WRITE_SINGLE_TAG_MODE;

            String actor = write_single_tag_2of4_selected_actor_textview.getText().toString();
            String actScene = write_single_tag_2of4_selected_actscene_textview.getText().toString();
            Log.d(TAG, "query ItemCodes by actor=" + actor + " : act, scene=" + actScene);

            if(mSelectedCostumeToWrite.code.equals("")) {
//            if(mEPCSelected.getEpcRaw().trim().equals("")) {
                loadWarningDialog("", getString(R.string.costume_notselected), getString(R.string.costume_notselected_solution));
            } else {
                switch (mCurrentSearchMode) {
                    case Constants.WRITE_SINGLE_TAG_MODE: {
                        SetVisible(write_single_tag_3of4_layout, textViewS1, viewS1);
                        break;
                    }
                    case Constants.SEARCH_SINGLE_TAG_MODE: {
                        scan_area_title_textview.setText(getString(R.string.power_status));
                        scan_area_message_textview.setText(getString(R.string.dash));
                        SetVisible(search_single_tag_3of3_layout, textViewS1, viewS1);
                        break;
                    }
                }
                
                // do no clear scan single rv list
                new RefreshEPCSingleTagThread().start();
            }


        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class BackWriteSingleTag2of4ButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            // Clear selected costume item
            mEPCSelected = new com.handheld.upsizeuhf.model.EPC();
            mSelectedCostumeToWrite = new Costume();

            SetVisible(write_single_tag_1of4_layout, textViewS1, viewS1);
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class BackWriteSingleTag3of4ButtonAnimationListener implements Animation.AnimationListener   {
        View view;
        public BackWriteSingleTag3of4ButtonAnimationListener(View v) {
            view = v;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            thread.interrupt();

            switch (view.getId()) {
                case R.id.write_single_tag_button: {
                    scanWriteSingleTagFlag = false;
                    scan_write_single_tag_3of4_button.setText(R.string.scan);
                    break;
                }
                case R.id.search_single_tag_button: {
                    scanSearchSingleTagFlag = false;
                    scan_search_single_tag_3of3_button.setText(R.string.scan);
                    break;
                }
            }

            SetVisible(write_single_tag_2of4_layout, textViewS1, viewS1);

            queryItemCodesBySelectedActorActScene();
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class ScanWriteSingleTag3of4ButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            if (!scanWriteSingleTagFlag) {
                thread.start();
                scanWriteSingleTagFlag = true;
                scan_write_single_tag_3of4_button.setText(R.string.stop);
            } else {
                thread.interrupt();
                scanWriteSingleTagFlag = false;
                scan_write_single_tag_3of4_button.setText(R.string.scan);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class ScanSearchSingleTag3of3ButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {


            if (!scanSearchSingleTagFlag) {
                // Set full scale scanning power
                mCurrentSearchPowerIndex = 0;
                search_single_tag_3of3_tag_detail_rssi_textview.setText(getString(R.string.dash));
                scanned_epc_textview.setText(getString(R.string.dash));
                scanned_rssi_textview.setText(getString(R.string.dash));
                mCurrentSearchPower = listSearchPower.get(mCurrentSearchPowerIndex).intValue();
                if (manager.setOutputPower(mCurrentSearchPower)) {
                    scan_area_title_textview.setText("Power " + mCurrentSearchPower + "dBm");
                    scan_area_message_textview.setText("ready!");
                    // Start scanning
                    thread.start();
                    scanSearchSingleTagFlag = true;
                    scan_search_single_tag_3of3_button.setText(R.string.stop);
                    scan_progress_gifview.setImageResource(R.drawable.scannotfound);
                    mSearchResult = Constants.SCANNOTFOUND_SEARCH_RESULT;


                } else {
                    scan_area_title_textview.setText("Power " + mCurrentSearchPower + "dBm");
                    scan_area_message_textview.setText("setup failed?!, please try to stop/scan again.");
                    showToast(getString(R.string.not_success));
                }

            } else {
                // Stop scanning
                thread.interrupt();
                scanSearchSingleTagFlag = false;
                scan_search_single_tag_3of3_button.setText(R.string.scan);
                scan_progress_gifview.setImageResource(R.drawable.readytoscan);
                mSearchResult = Constants.READYTOSCAN_SEARCH_RESULT;
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class ClearSearchSingleTag3of3ButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
//            ((GifDrawable) gib.getDrawable()).stop();
            if (!scanSearchSingleTagFlag) {
                scanSearchSingleTagFlag = true;
                scan_search_single_tag_3of3_button.setText(R.string.stop);
                scan_progress_gifview.setImageResource(R.drawable.scanfoundsomething);

            } else {
                scanSearchSingleTagFlag = false;
                scan_search_single_tag_3of3_button.setText(R.string.scan);
                scan_progress_gifview.setImageResource(R.drawable.scanfoundcloserange);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class ClearWriteSingleTag3of4ButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            thread.interrupt();
            scanWriteSingleTagFlag = false;
            scan_write_single_tag_3of4_button.setText(R.string.scan);

            // do no clear scan single rv list
            new RefreshEPCSingleTagThread().start();
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class WriteWriteSingleTag3of4ButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            if(mEPCScanned.getEpcRaw().equals("")) {
                DialogUtils.Companion.showWarningDialog(UHFActivity.this, getString(R.string.warning), getString(R.string.target_scanned_tag_not_found), getString(R.string.target_scanned_tag_not_found_solution));
            } else {
                thread.interrupt();
                scanWriteSingleTagFlag = false;
                scan_write_single_tag_3of4_button.setText(R.string.scan);

                // do no clear scan single rv list
                writeSingleTagDialogFragment = WriteSingleTagDialogFragment.Companion.newInstance("", "", mSelectedCostumeToWrite, mEPCSelected, mEPCScanned, manager);

                writeSingleTagDialogFragment.show(getFragmentManager(), "write_single_tag_fragment");
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class ScanSingleTagButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            if (!scanSingleTagFlag) {
                thread.start();
                scanSingleTagFlag = true;
                scan_read_single_tag_button.setText(R.string.stop);
            } else {
                thread.interrupt();
                scanSingleTagFlag = false;
                scan_read_single_tag_button.setText(R.string.scan);
            }
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class ClearSingleTagButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            thread.interrupt();
            scanSingleTagFlag = false;
            scan_read_single_tag_button.setText(R.string.scan);

            // do no clear scan single rv list
            new RefreshEPCSingleTagThread().start();
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class WriteReadSingleTagButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            thread.interrupt();
            scanSingleTagFlag = false;
            scan_read_single_tag_button.setText(R.string.scan);

            // do no clear scan single rv list
            writeSingleTagDialogFragment = WriteSingleTagDialogFragment.Companion.newInstance("", "", mSelectedCostumeToWrite, mEPCSelected, mEPCScanned, manager);

            writeSingleTagDialogFragment.show(getFragmentManager(), "write_single_tag_fragment");
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class CheckItemSetButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            thread.interrupt();
            scanItemSetFlag = false;
            scan_itemset_info_result_button.setText(R.string.scan);

            loadCheckTypeUI(Constants.ITEM_SET_MODE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class ClearItemCodeButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            thread.interrupt();
            scanItemCodeFlag = false;
            scan_itemcode_info_result_button.setText(R.string.scan);

            queryItemInfoBySelectedItemCode();
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    private class CheckItemCodeButtonAnimationListener implements Animation.AnimationListener   {
        @Override
        public void onAnimationStart(Animation animation) {
            thread.interrupt();
            scanItemCodeFlag = false;
            scan_itemcode_info_result_button.setText(R.string.scan);

            loadCheckTypeUI(Constants.ITEM_CODE_MODE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {


        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    @Override
    public boolean onMenuItemSelected(int featureId, @NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_signout:
                loadUserSignInUI();
                return true;
            case R.id.action_settings:
                return true;
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    /**
     * on actionbar show menu button
     */
    private void setOverflowShowingAlways() {
        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            menuKeyField.setAccessible(true);
            menuKeyField.setBoolean(config, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class myOnTouch implements OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent arg1) {
            switch (view.getId()) {
                case R.id.linearLayoutSearchAndCheck:
                    SetVisible(searchandcheck_layout, textViewS1, viewS1);
                    break;
                case R.id.linearLayoutUhfEpc:
                    SetVisible(null, textView1, view1);
                    break;
                case R.id.linearLayoutUhfRead:
                    SetVisible(l2readandwrite, textView2, view2);
                    break;
                case R.id.linearLayoutUhfLock:
                    SetVisible(l3lockandkill, textView3, view3);
                    break;
                case R.id.linearLayoutUhfSet:
                    SetVisible(l4settings, textView4, view4);
                    break;
                default:
                    break;
            }
            return true;
        }
    }

    private void SetVisible(LinearLayout layout, TextView textView, View view) {
        if (listepc.size() == 0 && (layout == l2readandwrite || layout == l3lockandkill)) {
            showToast("Please inventory!");
            return;
        }
        searchandcheck_layout.setVisibility(View.GONE);
        byitemset_select_filter_layout.setVisibility(View.GONE);
        byitemset_queryresult_layout.setVisibility(View.GONE);

        byitemcode_select_filter_layout.setVisibility(View.GONE);
        byitemcode_queryresult_layout.setVisibility(View.GONE);

        read_single_tag_layout.setVisibility(View.GONE);
        write_single_tag_1of4_layout.setVisibility(View.GONE);
        write_single_tag_2of4_layout.setVisibility(View.GONE);
        write_single_tag_3of4_layout.setVisibility(View.GONE);
        
        search_single_tag_3of3_layout.setVisibility(View.GONE);

        l1epc.setVisibility(View.GONE);
        l2readandwrite.setVisibility(View.GONE);
        l3lockandkill.setVisibility(View.GONE);
        l4settings.setVisibility(View.GONE);

        textViewS1.setTextColor(getResources().getColor(R.color.black));
        viewS1.setBackgroundColor(getResources().getColor(R.color.white));

        textView1.setTextColor(getResources().getColor(R.color.black));
        view1.setBackgroundColor(getResources().getColor(R.color.white));
        textView2.setTextColor(getResources().getColor(R.color.black));
        view2.setBackgroundColor(getResources().getColor(R.color.white));
        textView3.setTextColor(getResources().getColor(R.color.black));
        view3.setBackgroundColor(getResources().getColor(R.color.white));
        textView4.setTextColor(getResources().getColor(R.color.black));
        view4.setBackgroundColor(getResources().getColor(R.color.white));
        textView.setTextColor(getResources().getColor(R.color.tabSelect));
        view.setBackgroundColor(getResources().getColor(R.color.tabSelect));
        if (layout == null) {
            // change sensitive back to normal setting
            manager.setSensitivity(sensitive);

            if (!manager.setOutputPower(power)) {
                showToast(getString(R.string.not_success));
            }


            l1epc.setVisibility(View.VISIBLE);
            startFlag = false;
            buttonStart.setText(R.string.inventory);
        } else {

            if(layout == searchandcheck_layout ||
                    layout == l2readandwrite ||
                    layout == l3lockandkill ||
                    layout == l4settings
            ) {
                // change sensitive back to normal setting
                manager.setSensitivity(sensitive);
                if (!manager.setOutputPower(power)) {
                    showToast(getString(R.string.not_success));
                }
            }

            if(layout == byitemset_queryresult_layout) {
                scanItemSetFlag = false;
                scan_itemset_info_result_button.setText(R.string.scan);
            }

            if(layout == byitemcode_queryresult_layout) {
                scanItemSetFlag = false;
                scan_itemcode_info_result_button.setText(R.string.scan);
            }

            if(layout == read_single_tag_layout ||
                    layout == write_single_tag_3of4_layout ||
                    layout == search_single_tag_3of3_layout) {
                // change sensitive to Very Low
                manager.setSensitivity(Constants.VERY_LOW);

                if(layout == search_single_tag_3of3_layout) {
                    Log.d(TAG, "Search single tag...");
                } else {
                    if (manager.setOutputPower(Constants.POWER_16)) {
                        showToast(getString(R.string.single_tag_settings_ready));
                    } else {
                        showToast(getString(R.string.not_success));
                    }
                }

                if(layout == read_single_tag_layout) {
                    scanSingleTagFlag = false;
                    scan_read_single_tag_button.setText(R.string.scan);
                }

                if(layout == write_single_tag_3of4_layout) {
                    scanWriteSingleTagFlag = false;
                    scan_write_single_tag_3of4_button.setText(R.string.scan);
                }

                if(layout == search_single_tag_3of3_layout) {
                    scanSearchSingleTagFlag = false;
                    scan_search_single_tag_3of3_button.setText(R.string.scan);
                }

            }

            layout.setVisibility(View.VISIBLE);
        }
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime < 2000) {
                finish();
            } else {
                exitTime = System.currentTimeMillis();
                showToast("Double click to exit!");
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * show Toast
     */
    private Toast mToast;

    private void showToast(String message) {
        if (mToast == null) {
            mToast = Toast.makeText(UHFActivity.this, message, Toast.LENGTH_SHORT);
            mToast.show();
        } else {
            mToast.setText(message);
            mToast.show();
        }
    }

    private Toast toast;
    private KeyReceiver keyReceiver;

    private void registerReceiver() {
        keyReceiver = new KeyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.rfid.FUN_KEY");
        filter.addAction("android.intent.action.FUN_KEY");
        registerReceiver(keyReceiver, filter);
    }

    private void unregisterReceiver() {
        unregisterReceiver(keyReceiver);
    }

    private class KeyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int keyCode = intent.getIntExtra("keyCode", 0);
            if (keyCode == 0) {
                keyCode = intent.getIntExtra("keycode", 0);
            }
            boolean keyDown = intent.getBooleanExtra("keydown", false);
            if (keyDown) {
                if (toast == null) {
                    toast = Toast.makeText(UHFActivity.this, "KeyReceiver:keyCode = down" + keyCode, Toast.LENGTH_SHORT);
                } else {
                    toast.setText("KeyReceiver:keyCode = down" + keyCode);
                }
                toast.show();
                switch (keyCode) {
                    case KeyEvent.KEYCODE_F1:
                    case KeyEvent.KEYCODE_F2:
                    case KeyEvent.KEYCODE_F3:
                    case KeyEvent.KEYCODE_F4:
                    case KeyEvent.KEYCODE_F5:
                        onClick(buttonStart);
                        break;
                }
            }


        }
    }

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9A-Fa-f]*");
        return pattern.matcher(str).matches();
    }



    private class ServiceQueryAsyncTask extends AsyncTask<Void, Void, Void> {

        private Context mContext;
        private Activity mActivity;
        String response = "";
        QueryService servicePath = new QueryService();
        HashMap<String, String> postDataParams;

        public ServiceQueryAsyncTask(Context context, Activity activity, QueryService servicePath) {
            this.mContext = context;
            this.mActivity = activity;
            this.servicePath = servicePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            processDialog = new ProgressDialog(mContext);
//            processDialog.setMessage("Please  Wait ... ServiceQueryAsyncTask");
//            processDialog.setCancelable(false);
//            processDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            postDataParams = new HashMap<String, String>();
            postDataParams.put("HTTP_ACCEPT", "application/json");

            HttpConnectionService service = new HttpConnectionService();
            String path = "http://" + serverIp + servicePath.path;
            Log.d(TAG, "path=" + path);

            response = service.sendRequest(path, postDataParams);
            try {
                success = 1;
                JSONObject resultJsonObject = new JSONObject(response);
                restfulJsonArray = resultJsonObject.getJSONArray("output");
                isOnline = true;
            } catch (JSONException e) {
                success = 0;
                e.printStackTrace();
                Log.d(TAG, "Error while loading : "+e.getMessage());
                loadErrorDialog("", getString(R.string.database_server_offline) + " " + path, getString(R.string.database_server_offline_solution));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

//            if (processDialog.isShowing()) {
//                processDialog.dismiss();
//            }

            if (success == 1) {
                if (null != restfulJsonArray) {
                    switch (servicePath.uid) {
                        case Constants.ITEM_CODE_All: {
                                ArrayList<Costume> itemCodeArrayList = new ArrayList<Costume>();

                                for (int i = 0; i < restfulJsonArray.length(); i++) {
                                    try {
                                        JSONObject jsonObject = restfulJsonArray.getJSONObject(i);
                                        Costume itemCode = new Costume();

                                        itemCode.code = jsonObject.getString("code");
                                        itemCode.type = jsonObject.getString("type");
                                        itemCode.size = jsonObject.getString("size");
                                        itemCode.codeNo = jsonObject.getString("codeNo");

                                        itemCodeArrayList.add(itemCode);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                itemCodeFilterRVAdapter = new ItemCodeFilterRVAdapter(mContext, mActivity, itemCodeArrayList);
                                byitemcode_filter_rvlist.setAdapter(itemCodeFilterRVAdapter);
                                itemCodeFilterRVAdapter.notifyDataSetChanged();

//                                processDialog.dismiss();
                            }
                            break;
                        // 12-Aug-2021 19:52:43 - Not used at the moment.
                        case Constants.ACTOR_All: {
                            ArrayList<Actor> actorArrayList = new ArrayList<Actor>();

                            for (int i = 0; i < restfulJsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = restfulJsonArray.getJSONObject(i);
                                    Actor actor = new Actor();
                                    actor.name = jsonObject.getString("actor");
                                    actorArrayList.add(actor);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            actorRVAdapter = new ActorRVAdapter(mContext, mActivity, actorArrayList, Constants.SCREEN_TYPE_BY_ITEM_SET);
                            actor_name_rvlist.setAdapter(actorRVAdapter);
                            actorRVAdapter.notifyDataSetChanged();

//                            actorWriteSingleTagRVAdapter = new ActorRVAdapter(mContext, mActivity, actorArrayList);
//                            write_single_tag_1of4_actor_name_rvlist.setAdapter(actorWriteSingleTagRVAdapter);
//                            actorWriteSingleTagRVAdapter.notifyDataSetChanged();


//                                processDialog.dismiss();
                            }
                            break;
                        case Constants.COSTUME_All: {
                                mCostumeArrayList = new ArrayList<Costume>();

                                for (int i = 0; i < restfulJsonArray.length(); i++) {
                                    try {
                                        JSONObject jsonObject = restfulJsonArray.getJSONObject(i);
                                        Costume costume = new Costume();
                                        costume.uid = jsonObject.getInt("uid");
                                        costume.runningNo = jsonObject.getString("runningNo");
                                        costume.actor = jsonObject.getString("actor");
                                        costume.actScence = jsonObject.getString("actScence");
                                        costume.code = jsonObject.getString("code");
                                        costume.type = jsonObject.getString("type");
                                        costume.size = jsonObject.getString("size");
                                        costume.codeNo = jsonObject.getString("codeNo");
                                        costume.epcHeader = jsonObject.getString("epcHeader");
                                        costume.epcRun = jsonObject.getString("epcRun");
                                        costume.shipBox = jsonObject.getString("shipBox");
                                        costume.storageBox = jsonObject.getString("storageBox");
                                        costume.playBox = jsonObject.getString("playBox");
                                        mCostumeArrayList.add(costume);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Log.d(TAG, "mCostumeArrayList.size()=" + mCostumeArrayList.size());

                                if (mCostumeArrayList.size() > 0) {
                                    Log.d(TAG, "importLocalCostumeDB...");
                                    isOnline = true;
                                    // refresh local costume database by network database
                                    new ImportLocalCostumeThread().start();
                                } else {
                                    // load costume list from local database
                                    new LoadLocalCostumeThread().start();
                                }

                                if(mSelectedCostumeToWrite.uid > -1 && (mCurrentSearchMode == Constants.WRITE_SINGLE_TAG_MODE || mCurrentSearchMode == Constants.SEARCH_SINGLE_TAG_MODE)) {
                                    addSelectedCostumeToWriteTag(mSelectedCostumeToWrite, mCurrentSearchMode);
                                }


                            }
                            break;

                        case Constants.SHIPBOX_All:
                        case Constants.STORAGEBOX_All:
                        case Constants.PLAYBOX_All: {
                                ArrayList<Box> boxArrayList = new ArrayList<Box>();

                                for (int i = 0; i < restfulJsonArray.length(); i++) {
                                    try {
                                        JSONObject jsonObject = restfulJsonArray.getJSONObject(i);
                                        Box box = new Box();
                                        box.uid = jsonObject.getInt("uid");
                                        box.name = jsonObject.getString("name");
                                        box.epc = jsonObject.getString("epc");
                                        box.epcHeader = jsonObject.getString("epcHeader");
                                        box.epcRun = jsonObject.getString("epcRun");
                                        boxArrayList.add(box);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                Log.d(TAG, "boxArrayList.size()=" + boxArrayList.size());

                                switch(servicePath.uid) {
                                    case Constants.SHIPBOX_All: {
                                            if(boxArrayList.size() > 0) {
                                                mShipBoxArrayList = boxArrayList;

                                                new ImportLocalShipBoxThread().start();
                                            } else {
                                                new LoadLocalShipBoxThread().start();
                                            }

                                            new ServiceQueryAsyncTask(mContext, mActivity, Constants.Companion.getStorageBoxAllQuery()).execute();
                                        }
                                        break;
                                    case Constants.STORAGEBOX_All: {
                                            if(boxArrayList.size() > 0) {
                                                mStorageBoxArrayList = boxArrayList;

                                                new ImportLocalStorageBoxThread().start();
                                            } else {
                                                new LoadLocalStorageBoxThread().start();
                                            }

                                            new ServiceQueryAsyncTask(mContext, mActivity, Constants.Companion.getPlayBoxAllQuery()).execute();
                                        }
                                        break;
                                    case Constants.PLAYBOX_All: {
                                            if(boxArrayList.size() > 0) {
                                                mPlayBoxArrayList = boxArrayList;

                                                new ImportLocalPlayBoxThread().start();
                                            } else {
                                                new LoadLocalPlayBoxThread().start();
                                            }

                                            // Verify current user name empty?
                                            if(currentUserName.equalsIgnoreCase("")) {
                                                loadUserSignInUI();
                                            }
                                            hideProgressBar();

                                        }
                                        break;
                                }
//                                processDialog.dismiss();
                            }
                            break;
                    }
                }
            } else {

                // load costume list from local database
//                processDialog = new ProgressDialog(mContext);
//                processDialog.setMessage("Cannot access database server. then use local database instead!! Please  Wait ...");
//                processDialog.setCancelable(false);
//                processDialog.show();
                new LoadLocalCostumeThread().start();
            }
//            processDialog.dismiss();
        }

    }//end of async task

    class ImportLocalCostumeThread extends Thread {
        public void run() {
            try {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        showProgressBar();
//                        processDialog = new ProgressDialog(mContext);
//                        processDialog.setMessage("Updating local costume list. Please  Wait ...");
//                        processDialog.setCancelable(false);
//                        processDialog.show();
                        by_item_set_button.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                        by_item_code_button.setBackgroundColor(getResources().getColor(R.color.colorGrey));
                        by_item_set_button.setEnabled(false);
                        by_item_code_button.setEnabled(false);

                        read_single_tag_button.setEnabled(false);
                        write_single_tag_button.setEnabled(false);
                        search_single_tag_button.setEnabled(false);

//                        hideProgressBar();
//                        processDialog.dismiss();
                    }
                });

                System.out.println("thread is running...");
                RoomUtils.Companion.importLocalCostumeDB(getApplicationContext(), mCostumeArrayList);

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        hideProgressBar();
//                        processDialog.dismiss();
                        by_item_set_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        by_item_code_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        by_item_set_button.setEnabled(true);
                        by_item_code_button.setEnabled(true);

                        read_single_tag_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        write_single_tag_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        search_single_tag_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                        read_single_tag_button.setEnabled(true);
                        write_single_tag_button.setEnabled(true);
                        search_single_tag_button.setEnabled(true);

                        isOnline = true;
                        showAppTitle();

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }

            new ServiceQueryAsyncTask(mContext, mActivity, Constants.Companion.getShipBoxAllQuery()).execute();


        }
    }

    class ImportLocalShipBoxThread extends Thread {
        public void run() {
            try {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        showProgressBar();
//                        processDialog = new ProgressDialog(mContext);
//                        processDialog.setMessage("Updating local shipbox list. Please  Wait ...");
//                        processDialog.setCancelable(false);
//                        processDialog.show();
//
//                        by_item_set_button.setBackgroundColor(getResources().getColor(R.color.colorGrey));
//                        by_item_code_button.setBackgroundColor(getResources().getColor(R.color.colorGrey));
//                        by_item_set_button.setEnabled(false);
//                        by_item_code_button.setEnabled(false);
//
//                    }
//                });

                System.out.println("thread is running...");
                RoomUtils.Companion.importLocalShipBoxDB(getApplicationContext(), mShipBoxArrayList);

            } catch (Exception e) {
                e.printStackTrace();
            }

//            try {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        hideProgressBar();
//                        processDialog.dismiss();
//                        by_item_set_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                        by_item_code_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                        by_item_set_button.setEnabled(true);
//                        by_item_code_button.setEnabled(true);
//                    }
//                });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    class ImportLocalStorageBoxThread extends Thread {
        public void run() {
            try {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        showProgressBar();
//                        processDialog = new ProgressDialog(mContext);
//                        processDialog.setMessage("Updating local storagebox list. Please  Wait ...");
//                        processDialog.setCancelable(false);
//                        processDialog.show();

//                        by_item_set_button.setBackgroundColor(getResources().getColor(R.color.colorGrey));
//                        by_item_code_button.setBackgroundColor(getResources().getColor(R.color.colorGrey));
//                        by_item_set_button.setEnabled(false);
//                        by_item_code_button.setEnabled(false);

//                    }
//                });

                System.out.println("thread is running...");
                RoomUtils.Companion.importLocalStorageBoxDB(getApplicationContext(), mStorageBoxArrayList);

            } catch (Exception e) {
                e.printStackTrace();
            }

//            try {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        hideProgressBar();
//                        processDialog.dismiss();
//                        by_item_set_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                        by_item_code_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                        by_item_set_button.setEnabled(true);
//                        by_item_code_button.setEnabled(true);
//                    }
//                });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    class ImportLocalPlayBoxThread extends Thread {
        public void run() {
            try {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        showProgressBar();
//                        processDialog = new ProgressDialog(mContext);
//                        processDialog.setMessage("Updating local playbox list. Please  Wait ...");
//                        processDialog.setCancelable(false);
//                        processDialog.show();
//
//                        by_item_set_button.setBackgroundColor(getResources().getColor(R.color.colorGrey));
//                        by_item_code_button.setBackgroundColor(getResources().getColor(R.color.colorGrey));
//                        by_item_set_button.setEnabled(false);
//                        by_item_code_button.setEnabled(false);
//
//                    }
//                });

                System.out.println("thread is running...");
                RoomUtils.Companion.importLocalPlayBoxDB(getApplicationContext(), mPlayBoxArrayList);

            } catch (Exception e) {
                e.printStackTrace();
            }

//            try {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        hideProgressBar();
//                        processDialog.dismiss();
//                        by_item_set_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                        by_item_code_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
//                        by_item_set_button.setEnabled(true);
//                        by_item_code_button.setEnabled(true);
//                    }
//                });
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        }
    }

    class RefreshItemSetResultThread extends Thread {
        public void run() {
            try {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if(mCurrentSearchMode == Constants.ITEM_SET_MODE) {
                            queryItemCodesBySelectedActorActScene();
                        } else if(mCurrentSearchMode == Constants.ITEM_CODE_MODE) {
                            queryItemInfoBySelectedItemCode();
                        }
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class RefreshEPCSingleTagThread extends Thread {
        public void run() {
            try {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        mItemInfoArrayList = new ArrayList<Costume>();
                        mItemInfoArrayList.clear();

                        switch (mCurrentSearchMode) {
                            case Constants.READ_SINGLE_TAG_MODE: {
                                epcTagRVAdapter = new EPCTagRVAdapter(mContext, mActivity, mItemInfoArrayList);
                                read_single_tag_rvlist.setAdapter(epcTagRVAdapter);
                                epcTagRVAdapter.notifyDataSetChanged();

                                clearTagDetail();
                                break;
                            }
                            case Constants.WRITE_SINGLE_TAG_MODE:
                            case Constants.SEARCH_SINGLE_TAG_MODE: {
                                epcTagRVAdapter = new EPCTagRVAdapter(mContext, mActivity, mItemInfoArrayList);
                                write_single_tag_3of4_read_single_tag_rvlist.setAdapter(epcTagRVAdapter);
                                epcTagRVAdapter.notifyDataSetChanged();

                                clearTagDetail();
                                break;
                            }
                        }


                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class LoadLocalCostumeThread extends Thread {
        public void run() {
            try {
                System.out.println("thread is running...");
                mCostumeArrayList = (ArrayList<Costume>) RoomUtils.Companion.loadLocalCostumeList(getApplicationContext());
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        processDialog.dismiss();
//                    }
//                });

                try {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            hideProgressBar();
//                            processDialog.dismiss();
                            by_item_set_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            by_item_code_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            by_item_set_button.setEnabled(true);
                            by_item_code_button.setEnabled(true);

                            read_single_tag_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            write_single_tag_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            search_single_tag_button.setBackgroundColor(getResources().getColor(R.color.colorWhite));
                            read_single_tag_button.setEnabled(true);
                            search_single_tag_button.setEnabled(true);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class LoadLocalShipBoxThread extends Thread {
        public void run() {
            try {
                System.out.println("thread is running...");
                mShipBoxArrayList = (ArrayList<Box>) RoomUtils.Companion.loadLocalShipBoxList(getApplicationContext());
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        processDialog.dismiss();
//                    }
//                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class LoadLocalStorageBoxThread extends Thread {
        public void run() {
            try {
                System.out.println("thread is running...");
                mStorageBoxArrayList = (ArrayList<Box>) RoomUtils.Companion.loadLocalStorageBoxList(getApplicationContext());
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        processDialog.dismiss();
//                    }
//                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class LoadLocalPlayBoxThread extends Thread {
        public void run() {
            try {
                System.out.println("thread is running...");
                mPlayBoxArrayList = (ArrayList<Box>) RoomUtils.Companion.loadLocalPlayBoxList(getApplicationContext());
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        processDialog.dismiss();
//                    }
//                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class LoadActorThread extends Thread {
        View layoutLocal;

        public LoadActorThread(View layout) {
            this.layoutLocal = layout;
        }

        public void run() {
            System.out.println("thread is running...");
            mActorArrayList = (ArrayList<Actor>) RoomUtils.Companion.loadActorList(getApplicationContext(), mCostumeArrayList);

            try {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        switch (layoutLocal.getId()) {
                            case R.id.byitemset_select_filter_layout: {
                                actorRVAdapter = new ActorRVAdapter(mContext, mActivity, mActorArrayList, Constants.SCREEN_TYPE_BY_ITEM_SET);
                                actor_name_rvlist.setAdapter(actorRVAdapter);
                                actorRVAdapter.notifyDataSetChanged();

                                // Clear act, scence list
                                mActSceneArrayList = new ArrayList<Costume>();
                                actsceneRVAdapter = new ActSceneRVAdapter(mContext, mActivity, mActSceneArrayList);
                                actscene_name_rvlist.setAdapter(actsceneRVAdapter);
                                actsceneRVAdapter.notifyDataSetChanged();
                            }
                            break;

                            case R.id.write_single_tag_1of4_layout: {
                                actorWriteSingleTagRVAdapter = new ActorRVAdapter(mContext, mActivity, mActorArrayList, mCurrentSearchMode);
                                write_single_tag_1of4_actor_name_rvlist.setAdapter(actorWriteSingleTagRVAdapter);
                                actorWriteSingleTagRVAdapter.notifyDataSetChanged();

                                // Clear act, scence list
                                mActSceneArrayList = new ArrayList<Costume>();
                                actsceneWriteSingleTagRVAdapter = new ActSceneRVAdapter(mContext, mActivity, mActSceneArrayList);
                                write_single_tag_1of4_actscene_name_rvlist.setAdapter(actsceneWriteSingleTagRVAdapter);
                                actsceneWriteSingleTagRVAdapter.notifyDataSetChanged();
                                break;
                            }

                        }


                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    class LoadItemCodeThread extends Thread {

        public void run() {
            System.out.println("thread is running...");
            mItemCodeFilterArrayList = (ArrayList<Costume>) RoomUtils.Companion.loadItemCodeFilterList(getApplicationContext(), mCostumeArrayList);

            try {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        itemCodeFilterRVAdapter = new ItemCodeFilterRVAdapter(mContext, mActivity, mItemCodeFilterArrayList);
                        byitemcode_filter_rvlist.setAdapter(itemCodeFilterRVAdapter);
                        itemCodeFilterRVAdapter.notifyDataSetChanged();

                        // Clear item info filter list
                        mItemInfoFilterArrayList = new ArrayList<Costume>();
                        itemInfoFilterRVAdapter = new ItemInfoRVAdapter(mContext, mActivity, mItemInfoFilterArrayList);
                        byitemcode_item_info_rvlist.setAdapter(itemInfoFilterRVAdapter);
                        itemInfoFilterRVAdapter.notifyDataSetChanged();

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public void upDateCurrentUserOperation(User user) {
        editor.putString("currentUserName", user.getUsername());
        editor.commit();

        currentUserName = shared.getString("currentUserName", "");
        showAppTitle();

        showToast(getString(R.string.setSuccess));
        userSignInDialogFragment.dismiss();
    }

}
