package com.dm.crmdm_app;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import com.dm.controller.AppDataController;
import com.dm.library.ConnectionDetector;
import com.dm.library.ContactInfoAdaptor;
import com.dm.library.CustomAdapterCRMStreamInfo;
import com.dm.library.CustomPersonHistoryArrayAdapter;
import com.dm.library.Custom_Toast;
import com.dm.library.IntentSend;
import com.dm.model.AppData;
import com.dm.model.CRMHistoryInfo;
import com.dm.model.Owner;
import com.dm.model.PersonInfo;
import com.dm.parser.JSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class CRMStreamInfo extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    public static String DealDocID = "";

    ListView personHistoryListView;
    CustomPersonHistoryArrayAdapter customPersonHistoryArrayAdapter;
    ArrayList<CRMHistoryInfo> arrayListPersonHistoryInfo = new ArrayList<>();
    public Dialog dialog;
    String Contact_id;
    FloatingActionButton actionFloatingButton;
    Button ownerSpinner, tagSpinner, statusSpinner;
    LinearLayout personInfoListView;
    TextView personName, personHeaderName, personCompany, companyWebsite, personWebsite, personAddress;
    ProgressDialog progressDialog;
    String SMID;
    ImageView personImage;
    ArrayList<Owner> ownerArrayList = new ArrayList<>();
    ArrayList<Owner> tagArrayList = new ArrayList<>();
    ArrayList<Owner> statusArrayList = new ArrayList<>();
    ArrayList<Owner> taskstatusArrayList = new ArrayList<>();
    ArrayList<PersonInfo> arrayListPersonInfo = new ArrayList<>();
    private static int RESULT_LOAD_IMAGE = 1;
    ConnectionDetector connectionDetector;
    String RefDocID = "";
    String latitude = "0", longitude = "0";
    TextView texthistoryAll, texthistoryNoteCall, texthistoryDeal, texthistoryAction, texthistoryPayment;
    SharedPreferences preferences;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    String server;
    String rowID = "0", PreviousRowId = "0";
    LinearLayout linearLayoutPersonInfo, linearLayoutOwnerInfo;
    String Flag = "T";
    String selectedSpinId = "";
    boolean ischanged, isLocationUpdate = false;
    private Location mylocation;
    String adapterType = "All";
    boolean isMoreData = false;
    private final static int REQUEST_CHECK_SETTINGS_GPS = 0x1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crm_info_listview);

        try {
            connectionDetector = new ConnectionDetector(CRMStreamInfo.this);
            preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
            personHistoryListView = (ListView) findViewById(R.id.personHistoryListView);
            View headerView = ((LayoutInflater) CRMStreamInfo.this.getSystemService(CRMStreamInfo.this.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.crm_streem_new, null, false);
            personHistoryListView.addHeaderView(headerView);
            personHistoryListView.setAdapter(null);
            String personImageString = preferences.getString("PersonImage", "NA");
            personImageString = personImageString.replaceAll("\\n", "");
            if (connectionDetector.isConnectingToInternet()) {
                Intent intent = getIntent();
                getSupportActionBar().setCustomView(R.layout.actionbar_custom_view_home);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                getSupportActionBar().setDisplayShowCustomEnabled(true);
                getSupportActionBar().setDisplayUseLogoEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                ImageView iv = (ImageView) findViewById(R.id.image);
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        (new IntentSend(getApplicationContext(), DashBoradActivity.class)).toSendAcivity();
                        finish();
                    }
                });
                TextView tv = (TextView) findViewById(R.id.text);
                tv.setText("Stream Info");
                Contact_id = intent.getStringExtra("Contact_id");
                Flag = CRMstream.Flag;
                LinearLayout mainOptionLinearLayout = (LinearLayout) findViewById(R.id.mainOption);
                texthistoryAll = (TextView) findViewById(R.id.csiAll);
                texthistoryNoteCall = (TextView) findViewById(R.id.csiNoteAndCall);
                texthistoryDeal = (TextView) findViewById(R.id.csiDeal);
                texthistoryAction = (TextView) findViewById(R.id.csiAction);
                texthistoryPayment = (TextView) findViewById(R.id.csiPayment);
                linearLayoutPersonInfo = (LinearLayout) findViewById(R.id.listPersonInfo);
                linearLayoutOwnerInfo = (LinearLayout) findViewById(R.id.listOwnerTagInfo);
                if (Flag != null)
                    if (Flag.equalsIgnoreCase("T")) {
                        mainOptionLinearLayout.setVisibility(View.GONE);
                        linearLayoutPersonInfo.setVisibility(View.GONE);
                        linearLayoutOwnerInfo.setVisibility(View.GONE);
                        texthistoryAll.setVisibility(View.INVISIBLE);
                        texthistoryNoteCall.setVisibility(View.INVISIBLE);
                        texthistoryDeal.setVisibility(View.INVISIBLE);
                        texthistoryPayment.setVisibility(View.GONE);
                    } else {
                        mainOptionLinearLayout.setVisibility(View.VISIBLE);
                        linearLayoutPersonInfo.setVisibility(View.VISIBLE);
                        linearLayoutOwnerInfo.setVisibility(View.VISIBLE);
                        texthistoryAll.setVisibility(View.VISIBLE);
                        texthistoryNoteCall.setVisibility(View.VISIBLE);
                        texthistoryDeal.setVisibility(View.VISIBLE);
                        texthistoryPayment.setVisibility(View.VISIBLE);
                    }
                personImage = (ImageView) findViewById(R.id.personImage);
                personName = (TextView) findViewById(R.id.personName);
                personAddress = (TextView) findViewById(R.id.personAddress);
                personCompany = (TextView) findViewById(R.id.personCompany);
                companyWebsite = (TextView) findViewById(R.id.companyWebsite);
                personWebsite = (TextView) findViewById(R.id.personWebsite);
                personHeaderName = (TextView) findViewById(R.id.personHeaderName);
                personName.setText(intent.getStringExtra("Lead"));
                personHeaderName.setText(intent.getStringExtra("Lead"));
                ownerSpinner = (Button) findViewById(R.id.ownerSpinner);
                tagSpinner = (Button) findViewById(R.id.tagSpinner);
                statusSpinner = (Button) findViewById(R.id.statusSpinner);
                personInfoListView = (LinearLayout) findViewById(R.id.personInfoListView);
                SMID = preferences.getString("CONPERID_SESSION", null);
                appDataController1 = new AppDataController(CRMStreamInfo.this);
                setUpGClient();
                AppData appData;
                appDataController1.open();
                appDataArray = appDataController1.getAppTypeFromDb();
                appDataController1.close();
                appData = appDataArray.get(0);
                server = appData.getCompanyUrl();
                texthistoryAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        texthistoryNoteCall.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryAction.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryAll.setBackgroundResource(R.drawable.button_style);
                        texthistoryPayment.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryDeal.setBackgroundResource(R.drawable.deactive_button);
                        if (!RefDocID.equalsIgnoreCase("")) {
                            arrayListPersonHistoryInfo.clear();
                            rowID = "0";
                            adapterType = "All";
                            personHistoryListView.setAdapter(null);
                            customPersonHistoryArrayAdapter = new CustomPersonHistoryArrayAdapter(CRMStreamInfo.this, arrayListPersonHistoryInfo, R.layout.crm_history_listview, adapterType, editClick, deleteClick, viewMileClick);
                            personHistoryListView.setAdapter(customPersonHistoryArrayAdapter);
                            new getHistory().execute("All");
                        } else {
                            new Custom_Toast(CRMStreamInfo.this, "No Record Found").showCustomAlert();
                        }
                    }
                });
                texthistoryNoteCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        texthistoryNoteCall.setBackgroundResource(R.drawable.button_style);
                        texthistoryAction.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryAll.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryDeal.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryPayment.setBackgroundResource(R.drawable.deactive_button);
                        if (!RefDocID.equalsIgnoreCase("")) {
                            arrayListPersonHistoryInfo.clear();
                            rowID = "0";
                            adapterType = "NoteAndCall";
                            personHistoryListView.setAdapter(null);
                            customPersonHistoryArrayAdapter = new CustomPersonHistoryArrayAdapter(CRMStreamInfo.this, arrayListPersonHistoryInfo, R.layout.crm_history_listview, adapterType, editClick, deleteClick, viewMileClick);
                            personHistoryListView.setAdapter(customPersonHistoryArrayAdapter);
                            new getHistory().execute("NoteAndCall");
                        } else {
                            new Custom_Toast(CRMStreamInfo.this, "No Record Found").showCustomAlert();
                        }
                    }
                });
                texthistoryDeal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        texthistoryNoteCall.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryAction.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryAll.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryDeal.setBackgroundResource(R.drawable.button_style);
                        texthistoryPayment.setBackgroundResource(R.drawable.deactive_button);
                        if (!RefDocID.equalsIgnoreCase("")) {
                            arrayListPersonHistoryInfo.clear();
                            rowID = "0";
                            adapterType = "Deal";
                            personHistoryListView.setAdapter(null);
                            customPersonHistoryArrayAdapter = new CustomPersonHistoryArrayAdapter(CRMStreamInfo.this, arrayListPersonHistoryInfo, R.layout.crm_history_listview, adapterType, editClick, deleteClick, viewMileClick);
                            personHistoryListView.setAdapter(customPersonHistoryArrayAdapter);
                            new getHistory().execute("Deal");
                        } else {
                            new Custom_Toast(CRMStreamInfo.this, "No Record Found").showCustomAlert();
                        }
                    }
                });
                texthistoryAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        texthistoryNoteCall.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryAction.setBackgroundResource(R.drawable.button_style);
                        texthistoryAll.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryDeal.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryPayment.setBackgroundResource(R.drawable.deactive_button);
                        if (!RefDocID.equalsIgnoreCase("")) {
                            arrayListPersonHistoryInfo.clear();
                            rowID = "0";
                            adapterType = "Action";
                            personHistoryListView.setAdapter(null);
                            customPersonHistoryArrayAdapter = new CustomPersonHistoryArrayAdapter(CRMStreamInfo.this, arrayListPersonHistoryInfo, R.layout.crm_history_listview, adapterType, editClick, deleteClick, viewMileClick);
                            personHistoryListView.setAdapter(customPersonHistoryArrayAdapter);
                            new getHistory().execute("Action");
                        } else {
                            new Custom_Toast(CRMStreamInfo.this, "No Record Found").showCustomAlert();
                        }
                    }
                });
                texthistoryPayment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        texthistoryNoteCall.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryAction.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryAll.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryDeal.setBackgroundResource(R.drawable.deactive_button);
                        texthistoryPayment.setBackgroundResource(R.drawable.button_style);
                        if (!RefDocID.equalsIgnoreCase("")) {
                            arrayListPersonHistoryInfo.clear();
                            rowID = "0";
                            adapterType = "Payment";
                            personHistoryListView.setAdapter(null);
                            customPersonHistoryArrayAdapter = new CustomPersonHistoryArrayAdapter(CRMStreamInfo.this, arrayListPersonHistoryInfo, R.layout.crm_history_listview, adapterType, editClick, deleteClick, viewMileClick);
                            personHistoryListView.setAdapter(customPersonHistoryArrayAdapter);
                            new getHistory().execute("Payment");
                        } else {
                            new Custom_Toast(CRMStreamInfo.this, "No Record Found").showCustomAlert();
                        }
                    }
                });


                actionFloatingButton = (FloatingActionButton) findViewById(R.id.addActionButton);
                personHistoryListView.setOnScrollListener(new AbsListView.OnScrollListener() {

                    public void onScrollStateChanged(AbsListView view, int scrollState) {
                    }

                    public void onScroll(AbsListView view, int firstVisibleItem,
                                         int visibleItemCount, int totalItemCount) {
                        if (isMoreData && !PreviousRowId.equalsIgnoreCase(rowID)) {
                            if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                                //  getNotificationData(SMID, notificationController.getTimeStamp());
                                if (connectionDetector.isConnectingToInternet()) {
                                    isMoreData = false;
                                    new getHistory().execute(adapterType);
                                } else {
                                    new Custom_Toast(CRMStreamInfo.this, "Try Again ! No Internet Connection").showCustomAlert();
                                }
                            }
                        }
                    }
                });
//            personImage.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    Toast.makeText(getApplication(), "Upload Your Image", Toast.LENGTH_LONG).show();
//                    Intent i = new Intent(
//                            Intent.ACTION_PICK,
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//                    startActivityForResult(i, RESULT_LOAD_IMAGE);
//                }
//            });
                actionFloatingButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        actionFloatingButton.setVisibility(View.INVISIBLE);
                        // creating custom Floating Action button
                        CustomDialog();
                    }
                });
//            if(!personImageString.equals("NA")){
//                try{
//                    byte[] decodedString = Base64.decode(personImageString, Base64.DEFAULT);
//                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//                    personImage.setImageBitmap(decodedByte);
//                }catch(Exception e){
//                    e.getMessage();
//                }
//            }
                progressDialog = ProgressDialog.show(CRMStreamInfo.this, "Loading Data", "Loading...", true);
                progressDialog.setCancelable(false);
                //new statusTagLeadSourceOwnerData().execute("Owner");

            } else {
                new Custom_Toast(CRMStreamInfo.this, "Try Again ! No Internet Connection").showCustomAlert();
            }


            ownerSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showOwnerDiaog();
                }
            });
            tagSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showTagDiaog();
                }
            });
            statusSpinner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showStatusDiaog();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        firstFuction();
    }

    public void firstFuction() {
        actionFloatingButton.setVisibility(View.VISIBLE);
        ownerArrayList = null;
        tagArrayList = null;
        statusArrayList = null;
        arrayListPersonInfo = null;
        ownerArrayList = new ArrayList<>();
        tagArrayList = new ArrayList<>();
        statusArrayList = new ArrayList<>();
        arrayListPersonInfo = new ArrayList<>();
        texthistoryNoteCall.setBackgroundResource(R.drawable.deactive_button);
        texthistoryAction.setBackgroundResource(R.drawable.deactive_button);
        texthistoryAll.setBackgroundResource(R.drawable.button_style);
        texthistoryPayment.setBackgroundResource(R.drawable.deactive_button);
        texthistoryDeal.setBackgroundResource(R.drawable.deactive_button);
        //actionFloatingButton.setVisibility(View.VISIBLE);
        if (Flag.equalsIgnoreCase("T")) {

            new gettingRefId().execute();
        } else {
            new statusTagLeadSourceOwnerData().execute("Owner");
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        if (isLocationUpdate) {
            isLocationUpdate = false;
            latitude = String.valueOf(mylocation.getLatitude());
            longitude = String.valueOf(mylocation.getLongitude());
            new updateLocationOnServer().execute();
        }


    }

    class getHistory extends AsyncTask<String, String, String> {
        String type = null, HistoryResult = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            type = arg0[0];
            // TODO Auto-generated method stub
            try {
                HistoryResult = null;
                HistoryResult = getPersonHistory(type);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return HistoryResult;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            JSONArray jsonArray = null;
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (!HistoryResult.isEmpty() && !(HistoryResult == null)) {
                if (type.equalsIgnoreCase("All")) {
                    //arrayListPersonHistoryInfo.clear();
                    try {
                        jsonArray = new JSONArray(HistoryResult);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                objs = jsonArray.getJSONObject(i);
                                rowID = objs.getString("rowId");
                                CRMHistoryInfo crmHistoryInfo = new CRMHistoryInfo();
                                crmHistoryInfo.setUsername(objs.getString("username"));
                                crmHistoryInfo.setTaskDocid(objs.getString("DocId"));
                                crmHistoryInfo.setNoteId(objs.getString("Task_NoteId"));
                                crmHistoryInfo.setDealID(objs.getString("Task_DealId"));
                                crmHistoryInfo.setTaskTDocId(objs.getString("TDocId"));
                                crmHistoryInfo.setTaskTask(objs.getString("Task"));
                                crmHistoryInfo.setTaskAssignTo(objs.getString("AssignedTo"));
                                crmHistoryInfo.setTaskAssignBy(objs.getString("Assignedby"));
                                crmHistoryInfo.setTaskADate(objs.getString("ADate"));
                                crmHistoryInfo.setTaskRefDocID(objs.getString("Ref_DocId"));
                                crmHistoryInfo.setTaskRefSno(objs.getString("Ref_Sno"));
                                crmHistoryInfo.setTaskStatus(objs.getString("Status"));
                                crmHistoryInfo.setTaskSMID(objs.getString("Smid"));
                                crmHistoryInfo.setTaskCallId(objs.getString("Task_CallId"));
                                crmHistoryInfo.setTaskDocId(objs.getString("CRMTask_DocId"));
                                crmHistoryInfo.setPhone(objs.getString("Phone"));
                                crmHistoryInfo.setPhoneoremailValue(objs.getString("contactnoemail"));
                                crmHistoryInfo.setResult(objs.getString("Result"));
                                crmHistoryInfo.setCallNote(objs.getString("CallNotes"));
                                if (objs.has("ActionTime")) {
                                    crmHistoryInfo.setActionTime(objs.getString("CallNotes"));
                                }

                                if (objs.has("mailto")) {
                                    crmHistoryInfo.setMailto(objs.getString("mailto"));
                                }
                                crmHistoryInfo.setNote(objs.getString("Notes"));
                                crmHistoryInfo.setDealDocID(objs.getString("CRM_TaskDocId"));
                                crmHistoryInfo.setDealMonthly(objs.getString("MonthlyDeal"));
                                crmHistoryInfo.setDealAmt(objs.getString("TotalDealAmt"));
                                crmHistoryInfo.setDealName(objs.getString("DealName"));
                                crmHistoryInfo.setDealTotalAmt(objs.getString("Amount"));
                                crmHistoryInfo.setDealDate(objs.getString("DealDate"));
                                crmHistoryInfo.setDealCloseDate(objs.getString("ExpClsDt"));
                                crmHistoryInfo.setDealStage(objs.getString("DealStage"));
                                crmHistoryInfo.setDealPercentage(objs.getString("DealStagePerc"));
                                crmHistoryInfo.setDealNote(objs.getString("DealNote"));
                                crmHistoryInfo.setPhoneoremailValue(objs.getString("contactnoemail"));
                                crmHistoryInfo.setDealType(objs.getString("Type"));
                                crmHistoryInfo.setCreatedDate(objs.getString("Createddate"));
                                crmHistoryInfo.setHeaderDate(objs.getString("DisplayDate"));
                                crmHistoryInfo.setPaymetcollectionId(objs.getString("PaymentCollId"));
                                crmHistoryInfo.setPaymentMode(objs.getString("mode"));
                                if (objs.getString("mode").equalsIgnoreCase("Cash")) {
                                    crmHistoryInfo.setPaymentModeCash(true);
                                } else {
                                    crmHistoryInfo.setPaymentModeCash(false);
                                }
                                crmHistoryInfo.setPaymentChequeNo(objs.getString("chequeNo"));
                                crmHistoryInfo.setPaymentChequeDate(objs.getString("ChequeDt"));
                                crmHistoryInfo.setPaymentBank(objs.getString("Bank"));
                                crmHistoryInfo.setPaymentBranch(objs.getString("Branch"));
                                crmHistoryInfo.setPaymentRemark(objs.getString("Remarks"));

                                arrayListPersonHistoryInfo.add(crmHistoryInfo);
                            } catch (JSONException e) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
                            }
                        }
                        customPersonHistoryArrayAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (type.equalsIgnoreCase("NoteAndCall")) {
                    //arrayListPersonHistoryInfo.clear();
                    try {
                        jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                objs = jsonArray.getJSONObject(i);
                                rowID = objs.getString("rowId");
                                CRMHistoryInfo crmHistoryInfo = new CRMHistoryInfo();
                                crmHistoryInfo.setTaskCallId(objs.getString("Task_CallId"));
                                crmHistoryInfo.setTaskDocId(objs.getString("CRMTask_DocId"));
                                crmHistoryInfo.setPhone(objs.getString("Phone"));
                                if (objs.has("mailto")) {
                                    crmHistoryInfo.setMailto(objs.getString("mailto"));
                                }

                                crmHistoryInfo.setResult(objs.getString("Result"));
                                crmHistoryInfo.setCallNote(objs.getString("CallNotes"));
                                crmHistoryInfo.setNoteId(objs.getString("Task_NoteId"));
                                crmHistoryInfo.setNote(objs.getString("Notes"));
                                crmHistoryInfo.setCreatedDate(objs.getString("Createddate"));
                                crmHistoryInfo.setHeaderDate(objs.getString("DisplayDate"));
                                crmHistoryInfo.setPhoneoremailValue(objs.getString("contactnoemail"));
                                arrayListPersonHistoryInfo.add(crmHistoryInfo);
                            } catch (JSONException e) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
                            }
                        }
                        customPersonHistoryArrayAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (type.equalsIgnoreCase("Deal")) {
                    //arrayListPersonHistoryInfo.clear();
                    try {
                        jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                objs = jsonArray.getJSONObject(i);
                                rowID = objs.getString("rowid");
                                CRMHistoryInfo crmHistoryInfo = new CRMHistoryInfo();
                                crmHistoryInfo.setDealID(objs.getString("Task_DealId"));
                                crmHistoryInfo.setDealDocID(objs.getString("CRM_TaskDocId"));
                                crmHistoryInfo.setDealMonthly(objs.getString("MonthlyDeal"));
                                crmHistoryInfo.setDealAmt(objs.getString("TotalDealAmt"));
                                crmHistoryInfo.setDealName(objs.getString("DealName"));
                                crmHistoryInfo.setDealTotalAmt(objs.getString("Amount"));
                                crmHistoryInfo.setDealDate(objs.getString("DealDate"));
                                crmHistoryInfo.setDealCloseDate(objs.getString("ExpClsDt"));
                                crmHistoryInfo.setDealStage(objs.getString("DealStage"));
                                crmHistoryInfo.setDealPercentage(objs.getString("DealStagePerc"));
                                crmHistoryInfo.setDealNote(objs.getString("DealNote"));
                                crmHistoryInfo.setDealType(objs.getString("Type"));
                                crmHistoryInfo.setCreatedDate(objs.getString("Createddate"));
                                crmHistoryInfo.setHeaderDate(objs.getString("DisplayDate"));
                                arrayListPersonHistoryInfo.add(crmHistoryInfo);
                            } catch (JSONException e) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
                            }
                        }
                        customPersonHistoryArrayAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (type.equalsIgnoreCase("Action")) {
                    //arrayListPersonHistoryInfo.clear();
                    try {
                        jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                objs = jsonArray.getJSONObject(i);
                                rowID = objs.getString("rowid");
                                CRMHistoryInfo crmHistoryInfo = new CRMHistoryInfo();
                                crmHistoryInfo.setTaskDocid(objs.getString("DocId"));
                                crmHistoryInfo.setTaskTDocId(objs.getString("TDocId"));
                                crmHistoryInfo.setTaskTask(objs.getString("Task"));
                                crmHistoryInfo.setTaskAssignTo(objs.getString("AssignedTo"));
                                crmHistoryInfo.setTaskAssignBy(objs.getString("Assignedby"));
                                crmHistoryInfo.setTaskADate(objs.getString("ADate"));
                                crmHistoryInfo.setTaskRefDocID(objs.getString("Ref_DocId"));
                                crmHistoryInfo.setTaskRefSno(objs.getString("Ref_Sno"));
                                crmHistoryInfo.setTaskStatus(objs.getString("Status"));
                                crmHistoryInfo.setTaskSMID(objs.getString("Smid"));
                                if (objs.has("ActionTime")) {
                                    crmHistoryInfo.setActionTime(objs.getString("CallNotes"));
                                }
                                crmHistoryInfo.setTaskCreatedDate(objs.getString("Createddate"));
                                crmHistoryInfo.setHeaderDate(objs.getString("DisplayDate"));
                                arrayListPersonHistoryInfo.add(crmHistoryInfo);
                            } catch (JSONException e) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
                            }
                        }
                        customPersonHistoryArrayAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                } else if (type.equalsIgnoreCase("Payment")) {
                    // arrayListPersonHistoryInfo.clear();
                    try {
                        jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                objs = jsonArray.getJSONObject(i);
                                CRMHistoryInfo crmHistoryInfo = new CRMHistoryInfo();
                                crmHistoryInfo.setCreatedDate(objs.getString("Createddate"));
                                crmHistoryInfo.setHeaderDate(objs.getString("DisplayDate"));
                                crmHistoryInfo.setDealTotalAmt(objs.getString("Amount"));
                                crmHistoryInfo.setPaymetcollectionId(objs.getString("PaymentCollId"));
                                crmHistoryInfo.setPaymentDocID(objs.getString("DocId"));
                                crmHistoryInfo.setPaymentMode(objs.getString("Mode"));
                                if (objs.getString("Mode").equalsIgnoreCase("Cash")) {
                                    crmHistoryInfo.setPaymentModeCash(true);
                                } else {
                                    crmHistoryInfo.setPaymentModeCash(false);
                                }
                                crmHistoryInfo.setPaymentChequeNo(objs.getString("ChequeNo"));
                                crmHistoryInfo.setPaymentChequeDate(objs.getString("ChequeDt"));
                                crmHistoryInfo.setPaymentBank(objs.getString("Bank"));
                                crmHistoryInfo.setPaymentBranch(objs.getString("Branch"));
                                crmHistoryInfo.setPaymentRemark(objs.getString("Remarks"));
                                crmHistoryInfo.setUsername(objs.getString("Smname"));
                                arrayListPersonHistoryInfo.add(crmHistoryInfo);
                            } catch (JSONException e) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
                            }
                        }
                        customPersonHistoryArrayAdapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                isMoreData = true;
            } else {
                isMoreData = false;
                new Custom_Toast(CRMStreamInfo.this, "No Data Found").showCustomAlert();
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }

    public String getPersonHistory(String type) {
        String url = null;
        String id = "";
        PreviousRowId = rowID;
        //RefDocID=RefDocID.replaceAll(" ","%20");
        if (type.equalsIgnoreCase("All")) {
            // url = "http://"+server+"/And_Sync.asmx/XjsGetCompleteHistory_CRM?Ref_DocId="+RefDocID.replaceAll(" ","%20");
            url = "http://" + server + "/And_Sync.asmx/XjsGetCompleteHistory_CRMByRowId?Ref_DocId=" + RefDocID.replaceAll(" ", "%20") + "&rowid=" + rowID;
        } else if (type.equalsIgnoreCase("NoteAndCall")) {
            //url = "http://"+server+"/And_Sync.asmx/XjsGetTaskNoteCall_CRM?Ref_DocId="+RefDocID.replaceAll(" ","%20");
            url = "http://" + server + "/And_Sync.asmx/XjsGetTaskNoteCall_CRMByRowId?Ref_DocId=" + RefDocID.replaceAll(" ", "%20") + "&rowid=" + rowID;
        } else if (type.equalsIgnoreCase("Deal")) {
            //url = "http://"+server+"/And_Sync.asmx/XjsGetTaskDeal_CRM?TaskDocId="+RefDocID.replaceAll(" ","%20")+"&TaskDealId=";
            url = "http://" + server + "/And_Sync.asmx/XjsGetTaskDeal_CRMByRowId?TaskDocId=" + RefDocID.replaceAll(" ", "%20") + "&TaskDealId=" + "&rowid=" + rowID;
            ;

        } else if (type.equalsIgnoreCase("Action")) {
            // url = "http://"+server+"/And_Sync.asmx/XjsGetTaskDetails_CRMByRowId?Ref_DocId="+RefDocID.replaceAll(" ","%20");
            url = "http://" + server + "/And_Sync.asmx/XjsGetTaskDetails_CRMByRowId?Ref_DocId=" + RefDocID.replaceAll(" ", "%20") + "&rowid=" + rowID;
        } else if (type.equalsIgnoreCase("Payment")) {
            //url = "http://"+server+"/And_Sync.asmx/XjsGetPaymentDetails_CRM?DocId="+RefDocID.replaceAll(" ","%20");
            url = "http://" + server + "/And_Sync.asmx/XjsGetPaymentDetails_CRMByRowId?DocId=" + RefDocID.replaceAll(" ", "%20") + "&rowid=" + rowID;
        }
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            byte[] b = new byte[0];
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            try {
                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                b = baos.toByteArray();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (b.length > 10) {
                String temp = Base64.encodeToString(b, Base64.DEFAULT);
                SharedPreferences.Editor editor = null;
                SharedPreferences preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("PersonImage", temp);
                editor.commit();
            }
            cursor.close();
            personImage.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }

    public String getstatusTagLeadSourceOwnerData(String serviceURL) {
        String url = null;
        if (serviceURL.equalsIgnoreCase("Status")) {
            url = "http://" + server + "/And_Sync.asmx/xjsStatus_CRM";
        } else if (serviceURL.equalsIgnoreCase("Tag")) {
            url = "http://" + server + "/And_Sync.asmx/xjsTag_CRM";
        } else if (serviceURL.equalsIgnoreCase("Owner")) {
            url = "http://" + server + "/And_Sync.asmx/xjsowner_CRM?smid=" + SMID;
        } else if (serviceURL.equalsIgnoreCase("TaskStatus")) {
            url = "http://" + server + "/And_Sync.asmx/XjsGetTaskStatus_CRM";
        }

        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }

    protected class statusTagLeadSourceOwnerData extends AsyncTask<String, String, String> {
        String type = null, result = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //     progressDialog= ProgressDialog.show(AddContactModel.this,"Loading Data", "Loading...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {

            type = arg0[0];
            // TODO Auto-generated method stub
            try {

                result = getstatusTagLeadSourceOwnerData(type);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            JSONArray jsonArray = null;
            if (!result.isEmpty() && !(result == null)) {
                if (type.equalsIgnoreCase("Owner")) {

                    try {
                        jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                objs = jsonArray.getJSONObject(i);
                                Owner owner = new Owner();
                                objs = jsonArray.getJSONObject(i);
                                owner.setId(objs.getString("id"));
                                owner.setName(objs.getString("Name"));
                                ownerArrayList.add(owner);
                            } catch (JSONException e) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        e.printStackTrace();
                    }
                    int index = getIndexBySpinner(selectedOwnerId, ownerArrayList);

                    if (index >= 0) {
                        ownerSpinner.setText(ownerArrayList.get(index).getName());
                    }
                    new statusTagLeadSourceOwnerData().execute("Tag");
                } else if (type.equalsIgnoreCase("Tag")) {
                    try {
                        jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                Owner owner = new Owner();
                                objs = jsonArray.getJSONObject(i);
                                owner.setId(objs.getString("id"));
                                owner.setName(objs.getString("Name"));
                                tagArrayList.add(owner);
                            } catch (JSONException e) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        e.printStackTrace();
                    }


                    //setSpinner(tagSpinner, tagArrayList);


                    new statusTagLeadSourceOwnerData().execute("Status");
                } else if (type.equalsIgnoreCase("Status")) {
                    try {
                        jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                Owner owner = new Owner();
                                objs = jsonArray.getJSONObject(i);
                                owner.setId(objs.getString("id"));
                                owner.setName(objs.getString("Name"));
                                statusArrayList.add(owner);
                            } catch (JSONException e) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                        e.printStackTrace();
                    }

                    int index = getIndexBySpinner(selectedOwnerId, statusArrayList);

                    if (index >= 0) {
                        statusSpinner.setText(statusArrayList.get(index).getName());
                    }
                    new gettingPersonContactInfo().execute();
                }
            } else {
                new Custom_Toast(CRMStreamInfo.this, "No Data Found").showCustomAlert();
            }

        }
    }

    private void setSpinnersEvent() {
//        ownerSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                selectedSpinId = ownerArrayList.get(i).getId();
//                new UpdatespinnerDataOnServer().execute("Owner");
//            }
//        });
//        tagSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                selectedSpinId = tagArrayList.get(i).getId();
//                new UpdatespinnerDataOnServer().execute("Tag");
//            }
//        });
//        statusSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                selectedSpinId = statusArrayList.get(i).getId();
//                new UpdatespinnerDataOnServer().execute("Status");
//            }
//        });
//        ownerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selectedSpinId = ownerArrayList.get(i).getId();
//
//                new UpdatespinnerDataOnServer().execute("Owner");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selectedSpinId = statusArrayList.get(i).getId();
//                new UpdatespinnerDataOnServer().execute("Status");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                selectedSpinId = tagArrayList.get(i).getId();
//                new UpdatespinnerDataOnServer().execute("Tag");
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });


    }

    String websiteArr;
    StringBuilder websitestr = null;
    String selectedOwnerId = "", selectedTagId = "", selectedStatusId = "";

    protected class gettingPersonContactInfo extends AsyncTask<String, String, String> {
        String type = null, result = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(CRMStreamInfo.this, "Loading Data", "Loading...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                result = getPersonInfo("Main");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            JSONArray jsonArray = null;
            String companyName = "None", Address = "None", Website = "None", imgUrl = "", companyWebsiteText = "";
            if (!result.isEmpty() && !(result == null)) {
                try {
                    jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objs = null;
                        try {
                            objs = jsonArray.getJSONObject(i);
                            PersonInfo personInfo = new PersonInfo();
                            companyName = objs.getString("CmpNm");
                            companyWebsiteText = objs.getString("CompAdd");
                            Address = objs.getString("Add");
                            websiteArr = objs.getString("Website");
                            RefDocID = objs.getString("RefDocId");
                            personInfo.setName(objs.getString("Nm"));
                            personInfo.setPhone(objs.getString("Ph"));
                            personInfo.setEmail(objs.getString("Em"));
                            imgUrl = objs.getString("imgpath");
                            selectedOwnerId = objs.getString("owner");
                            selectedTagId = objs.getString("tag");
                            selectedStatusId = objs.getString("status");
                            latitude = objs.getString("Lat");
                            longitude = objs.getString("Long");
                            try {
                                DisplayImageOptions options = new DisplayImageOptions.Builder()
                                        .showImageOnLoading(R.drawable.ic_user)
                                        .showImageForEmptyUri(R.drawable.ic_user)
                                        .showImageOnFail(R.drawable.ic_user)
                                        .cacheInMemory(true)
                                        .cacheOnDisk(true)
                                        .build();
                                ImageLoader imageLoader = ImageLoader.getInstance();
                                //selectedImageBase64ImgString=allDataArray.get(i).getImageUrl();
                                imageLoader.displayImage(imgUrl, personImage, options);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            arrayListPersonInfo.add(personInfo);
                        } catch (JSONException e) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            e.printStackTrace();
                        }
                    }
                    if (websiteArr != null) {
                        JSONArray jsonArray1 = new JSONArray(websiteArr);
                        websitestr = null;
                        websitestr = new StringBuilder();

                        for (int n = 0; n < jsonArray1.length(); n++) {
                            JSONObject json = jsonArray1.getJSONObject(n);
                            websitestr.append(json.getString("URl") + ", ");


                            //   Website = Website+Website+", ";
                        }
                    }

                    int index = getIndexBySpinner(selectedTagId, tagArrayList);
                    if (index >= 0) {
                        tagSpinner.setText(tagArrayList.get(index).getName());
                    }
                    index = getIndexBySpinner(selectedOwnerId, ownerArrayList);
                    if (index >= 0) {
                        ownerSpinner.setText(ownerArrayList.get(index).getName());
                    }
                    index = getIndexBySpinner(selectedStatusId, statusArrayList);
                    if (index >= 0) {
                        statusSpinner.setText(statusArrayList.get(index).getName());
                    }

                    personAddress.setText(((Address.length() < 1) ? "Person Address" : Address));
                    personCompany.setText(((companyName.length() < 1) ? "Person Company" : companyName));
                    companyWebsite.setText(((companyWebsiteText.length() < 13) ? "Company Address: N/A" : companyWebsiteText));
                    personWebsite.setText(((websitestr.length() < 3) ? "Website: N/A" : websitestr.toString()));
                    //  Website = Website.substring(0,Website.length()-2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (Flag.equalsIgnoreCase("L")) {
                    new gettingPersonAllContact().execute();
                } else {
                    //new getHistory().execute("Action");
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }
                /*CustomPersonInfoArrayAdapter adapter = new CustomPersonInfoArrayAdapter(CRMStreamInfo.this,arrayListPersonInfo ,R.layout.person_contact_information);
                personInfoListView.setAdapter(adapter);*/
            } else {
                new Custom_Toast(CRMStreamInfo.this, "No Data Found").showCustomAlert();
            }

        }
    }

    public void locationUpdateClick(View v) {
        isLocationUpdate = true;
        getMyLocation();
    }

    private synchronized void setUpGClient() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    GoogleApiClient googleApiClient;

    private void getMyLocation() {
        if (googleApiClient != null) {
            if (googleApiClient.isConnected()) {
                int permissionLocation = ContextCompat.checkSelfPermission(CRMStreamInfo.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);
                if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                    mylocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    LocationRequest locationRequest = new LocationRequest();
                    locationRequest.setInterval(3000);
                    locationRequest.setFastestInterval(3000);
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                            .addLocationRequest(locationRequest);
                    builder.setAlwaysShow(true);
                    LocationServices.FusedLocationApi
                            .requestLocationUpdates(googleApiClient, locationRequest, this);
                    PendingResult result =
                            LocationServices.SettingsApi
                                    .checkLocationSettings(googleApiClient, builder.build());


                    result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                        @Override
                        public void onResult(LocationSettingsResult result) {
                            final Status status = result.getStatus();
                            switch (status.getStatusCode()) {
                                case LocationSettingsStatusCodes.SUCCESS:

                                    try {

                                        int permissionLocation = ContextCompat
                                                .checkSelfPermission(CRMStreamInfo.this,
                                                        android.Manifest.permission.ACCESS_FINE_LOCATION);
                                        if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
                                            mylocation = LocationServices.FusedLocationApi
                                                    .getLastLocation(googleApiClient);
                                        }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        //Toast.makeText(GetLocationActivity.this,""+e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                    Log.i("TAG", "All location settings are satisfied.");

                                    break;
                                case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        status.startResolutionForResult(CRMStreamInfo.this, REQUEST_CHECK_SETTINGS_GPS);
                                    } catch (IntentSender.SendIntentException e) {
                                        // Ignore the error.
                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    break;
                            }
                        }
                    });
                }
            }
        }
    }

    public void directionClick(View v) {
        getMyLocation();
        if (mylocation != null) {
            if (latitude.length() > 0 && longitude.length() > 0) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=" + mylocation.getLatitude() + "," + mylocation.getLongitude() + "&daddr=" + latitude + "," + longitude));
                startActivity(intent);
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Please update Contact/Lead location");
                alert.setPositiveButton("Ok", null);
                alert.create().show();
            }
        } else {
            if (latitude.length() > 0 && longitude.length() > 0) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?&daddr=" + latitude + "," + longitude));
                startActivity(intent);
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Please update Contact/Lead location");
                alert.setPositiveButton("Ok", null);
                alert.create().show();
            }
        }

    }

    protected class gettingRefId extends AsyncTask<String, String, String> {
        String type = null, result = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            // progressDialog = ProgressDialog.show(CRMStreamInfo.this, "Loading Data", "Loading...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                result = getPersonInfo("Main");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            JSONArray jsonArray = null;
            String companyName = "None", Address = "None", Website = "None", imgUrl = "";
            if (!result.isEmpty() && !(result == null)) {
                try {
                    jsonArray = new JSONArray(result);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objs = null;
                        try {
                            objs = jsonArray.getJSONObject(i);
                            PersonInfo personInfo = new PersonInfo();
                            companyName = objs.getString("CmpNm");
                            Address = objs.getString("Add");
                            websiteArr = objs.getString("Website");
                            RefDocID = objs.getString("RefDocId");
                            personInfo.setName(objs.getString("Nm"));
                            personInfo.setPhone(objs.getString("Ph"));
                            personInfo.setEmail(objs.getString("Em"));
                            imgUrl = objs.getString("imgpath");
                            selectedOwnerId = objs.getString("owner");
                            selectedTagId = objs.getString("tag");
                            selectedStatusId = objs.getString("status");
                            arrayListPersonInfo.add(personInfo);
                        } catch (JSONException e) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            e.printStackTrace();
                        }
                    }
                    arrayListPersonHistoryInfo.clear();
                    rowID = "0";
                    adapterType = "Action";
                    personHistoryListView.setAdapter(null);
                    customPersonHistoryArrayAdapter = new CustomPersonHistoryArrayAdapter(CRMStreamInfo.this, arrayListPersonHistoryInfo, R.layout.crm_history_listview, adapterType, editClick, deleteClick, viewMileClick);
                    personHistoryListView.setAdapter(customPersonHistoryArrayAdapter);
                    new getHistory().execute("Action");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                new Custom_Toast(CRMStreamInfo.this, "No Data Found").showCustomAlert();
            }

        }
    }

    JSONArray jsonArray = null;

    protected class gettingPersonAllContact extends AsyncTask<String, String, String> {
        String type = null, result = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(CRMStreamInfo.this, "Loading Data", "Loading...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                result = getPersonInfo("AllContact");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            String companyName = "None", Address = "None", Website = "None";
            if (!result.isEmpty() && !(result == null)) {
                try {
                    jsonArray = new JSONArray(result);
                    personInfoListView.removeAllViews();
                    for (int i = 0; i < 1; i++) {
                        JSONObject objs = null;
                        try {
                            objs = jsonArray.getJSONObject(i);
                            View view = ((LayoutInflater) CRMStreamInfo.this.getSystemService(CRMStreamInfo.this.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.person_contact_information, null, false);
                            TextView name = (TextView) view.findViewById(R.id.pName);
                            TextView phone = (TextView) view.findViewById(R.id.pPhone);
                            TextView email = (TextView) view.findViewById(R.id.pEmail);

                            name.setTypeface(Typeface.MONOSPACE);
                            email.setTypeface(Typeface.MONOSPACE);
                            phone.setTypeface(Typeface.MONOSPACE);
//                            name.setText(objs.getString("nm").isEmpty()?"Name":objs.getString("nm"));
//                            phone.setText(objs.getString("phone").isEmpty()?"Phone":objs.getString("phone"));
//                            email.setText(objs.getString("email").isEmpty()?"Email":objs.getString("email"));
                            name.setText(objs.getString("nm") + " (" + objs.getString("jobtitle") + ")");
                            phone.setText(objs.getString("phone").isEmpty() ? "N/A" : "" + objs.getString("phone"));
                            email.setText(objs.getString("email").isEmpty() ? "N/A" : "" + objs.getString("email"));
                            personInfoListView.addView(view);


                        } catch (JSONException e) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            e.printStackTrace();
                        }
                    }

                    if (jsonArray.length() > 1) {
                        Button showMoreButton = new Button(CRMStreamInfo.this);
                        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        showMoreButton.setBackgroundColor(Color.TRANSPARENT);
                        showMoreButton.setText("View All Contacts Info..");
                        showMoreButton.setLayoutParams(lp);
                        showMoreButton.setPadding(10, 10, 10, 10);
                        showMoreButton.setGravity(Gravity.LEFT);
                        showMoreButton.setTextColor(Color.parseColor("#00AcEC"));
                        personInfoListView.addView(showMoreButton);

                        final ArrayList<ContactInfoModel> clist = new ArrayList<>();
                        showMoreButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                clist.clear();
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject objs = null;
                                    try {
                                        objs = jsonArray.getJSONObject(i);
//                                    view = ((LayoutInflater) CRMStreamInfo.this.getSystemService(CRMStreamInfo.this.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.person_contact_information, null, false);
//                                    TextView name = (TextView) view.findViewById(R.id.pName);
//                                    TextView phone = (TextView) view.findViewById(R.id.pPhone);
//                                    TextView email = (TextView) view.findViewById(R.id.pEmail);
//                                    name.setTextSize(14);
//                                    phone.setTextSize(14);
//                                    email.setTextSize(14);
//                                    name.setTypeface(Typeface.MONOSPACE);
//                                    email.setTypeface(Typeface.MONOSPACE);
//                                    phone.setTypeface(Typeface.MONOSPACE);
//                            name.setText(objs.getString("nm").isEmpty()?"Name":objs.getString("nm"));
//                            phone.setText(objs.getString("phone").isEmpty()?"Phone":objs.getString("phone"));
//                            email.setText(objs.getString("email").isEmpty()?"Email":objs.getString("email"));
                                        ContactInfoModel cinfo = new ContactInfoModel();
                                        cinfo.setName(objs.getString("nm") + " (" + objs.getString("jobtitle") + ")");
                                        cinfo.setMobile(objs.getString("phone").isEmpty() ? "N/A" : "" + objs.getString("phone"));
                                        cinfo.setEmail(objs.getString("email").isEmpty() ? "N/A" : "" + objs.getString("email"));
                                        clist.add(cinfo);
//                                    name.setText();
//                                    phone.setText();
//                                    email.setText();


                                        //  personInfoListView.addView(view);


                                    } catch (JSONException e) {
                                        if (progressDialog != null) {
                                            progressDialog.dismiss();
                                        }
                                        e.printStackTrace();
                                    }
                                }
                                final AlertDialog.Builder dialog = new AlertDialog.Builder(CRMStreamInfo.this);

                                View hvi = getLayoutInflater().inflate(R.layout.custom_alert_header, null);
                                hvi.setMinimumHeight(56);
                                TextView close = (TextView) hvi.findViewById(R.id.close);
                                dialog.setCustomTitle(hvi);
                                dialog.setCancelable(true);

                                View vi = getLayoutInflater().inflate(R.layout.more_person_info, null);


                                ListView list = (ListView) vi.findViewById(R.id.mpersonMainInfo);
                                ContactInfoAdaptor adapter = new ContactInfoAdaptor(CRMStreamInfo.this, clist);
                                Rect displayRectangle = new Rect();
                                Window window = getWindow();
                                window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
                                list.setAdapter(adapter);
                                vi.setMinimumWidth((int) (displayRectangle.width() * 0.8f));
                                vi.setMinimumHeight((int) (displayRectangle.height() * 0.7f));
                                dialog.setView(vi);

                                final AlertDialog alert = dialog.create();

                                alert.show();
                                close.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        alert.dismiss();
                                    }
                                });
                            }
                        });
                    }
                } catch (JSONException e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    e.printStackTrace();
                }
                arrayListPersonHistoryInfo.clear();
                rowID = "0";
                adapterType = "All";
                personHistoryListView.setAdapter(null);
                customPersonHistoryArrayAdapter = new CustomPersonHistoryArrayAdapter(CRMStreamInfo.this, arrayListPersonHistoryInfo, R.layout.crm_history_listview, adapterType, editClick, deleteClick, viewMileClick);
                personHistoryListView.setAdapter(customPersonHistoryArrayAdapter);
                new getHistory().execute("All");
            } else {
                new Custom_Toast(CRMStreamInfo.this, "No Data Found").showCustomAlert();
            }

        }
    }


    public class updateLocationOnServer extends AsyncTask<String, Void, String> {
        String server_response;
        ProgressDialog updatelocationDiloge;
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            updatelocationDiloge = ProgressDialog.show(CRMStreamInfo.this, "Updating Location", "Please wait...", true);

            params.add(new BasicNameValuePair("contactid", Contact_id));
            params.add(new BasicNameValuePair("Lat", latitude));
            params.add(new BasicNameValuePair("Long", longitude));
            params.add(new BasicNameValuePair("lat_long_dt", String.valueOf(System.currentTimeMillis())));

        }

        protected String doInBackground(String... arg0) {
            try {

                URL url = new URL("http://" + server + "/And_Sync.asmx/XjsUpdLatLong_CRM"); // here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    server_response = sb.toString();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (updatelocationDiloge != null) {
                    updatelocationDiloge.dismiss();
                }
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (updatelocationDiloge != null) {
                updatelocationDiloge.dismiss();
            }
            // if(progressDialog.isShowing()){progressDialog.dismiss();}
            Log.e("Response", "" + server_response);
            if (server_response != null) {
                if (!server_response.isEmpty()) {
                    server_response = server_response.replaceAll("\"", "");
                    new Custom_Toast(getApplicationContext(), "Location successfully updated").showCustomAlert();
                }
            }
        }
    }


    public String getPersonInfo(String type) {
        String url;
        if (type.equalsIgnoreCase("AllContact")) {
            url = "http://" + server + "/And_Sync.asmx/XjsGetContacts_CRM?contactId=" + Contact_id;
        } else {
            url = "http://" + server + "/And_Sync.asmx/XjsGetContactDataByID_ActionStream?contactId=" + Contact_id;
        }
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }


    private int getIndexByProperty(String yourString) {
        for (int i = 0; i < DashBoradActivity.crmPermissionslist.size(); i++) {
            if (DashBoradActivity.crmPermissionslist != null && DashBoradActivity.crmPermissionslist.get(i).getActivityName().equals(yourString)) {
                return i;
            }
        }
        return -1;// not there is list
    }

    private int getIndexBySpinner(String yourString, ArrayList<Owner> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList != null && arrayList.get(i).getId().equals(yourString)) {
                return i;
            }
        }
        return -1;// not there is list
    }

    public void CustomDialog() {

        dialog = new Dialog(CRMStreamInfo.this);
        // it remove the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set the laytout in the dialog
        dialog.setContentView(R.layout.crm_stream_item);
        // set the background partial transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        // set the layout at right bottom
        param.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        // it dismiss the dialog when click outside the dialog frame
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCancelable(false);
        // initialize the item of the dialog box, whose id is demo1
        View cross = (View) dialog.findViewById(R.id.cross);
        // it call when click on the item whose id is demo1.
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // diss miss the dialog
                actionFloatingButton.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });
        View addNote = (View) dialog.findViewById(R.id.floatingAddNote);
        View addDeal = (View) dialog.findViewById(R.id.floatingAddDeal);
        View addCall = (View) dialog.findViewById(R.id.floatingAddCall);
        View addAction = (View) dialog.findViewById(R.id.floatingActionCall);
        View addPayment = (View) dialog.findViewById(R.id.floatingAddPayment);
        addAction.setVisibility(View.VISIBLE);
        try {
            int i = getIndexByProperty("Add Action");
            int j = getIndexByProperty("Add Call");
            int k = getIndexByProperty("Add Note");
            int l = getIndexByProperty("Add Deal");
            int m = getIndexByProperty("Add Payment");
            if (Flag.equalsIgnoreCase("T")) {
                addNote.setVisibility(View.GONE);
                addDeal.setVisibility(View.GONE);
                addCall.setVisibility(View.GONE);
                addPayment.setVisibility(View.GONE);
            } else {
                addNote.setVisibility(View.VISIBLE);
                addDeal.setVisibility(View.VISIBLE);
                addCall.setVisibility(View.VISIBLE);
                addPayment.setVisibility(View.VISIBLE);
            }
            if (i >= 0) {
                if (DashBoradActivity.crmPermissionslist.get(i).getPermission().equalsIgnoreCase("true")) {
                    addAction.setVisibility(View.VISIBLE);
                } else {
                    addAction.setVisibility(View.INVISIBLE);
                }
            }
            if (j >= 0 && !(Flag.equalsIgnoreCase("T"))) {
                if (DashBoradActivity.crmPermissionslist.get(j).getPermission().equalsIgnoreCase("true")) {
                    addCall.setVisibility(View.VISIBLE);
                } else {
                    addCall.setVisibility(View.GONE);
                }
            }
            if (k >= 0 && !(Flag.equalsIgnoreCase("T"))) {
                if (DashBoradActivity.crmPermissionslist.get(k).getPermission().equalsIgnoreCase("true")) {
                    addNote.setVisibility(View.VISIBLE);
                } else {
                    addNote.setVisibility(View.GONE);
                }
            }
            if (l >= 0 && !(Flag.equalsIgnoreCase("T"))) {
                if (DashBoradActivity.crmPermissionslist.get(l).getPermission().equalsIgnoreCase("true")) {
                    addDeal.setVisibility(View.VISIBLE);
                } else {
                    addDeal.setVisibility(View.GONE);
                }
            }

            if (m >= 0 && !(Flag.equalsIgnoreCase("T"))) {
                if (DashBoradActivity.crmPermissionslist.get(m).getPermission().equalsIgnoreCase("true")) {
                    addPayment.setVisibility(View.VISIBLE);
                } else {
                    addPayment.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        // it call when click on the item whose id is demo1.
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // diss miss the dialog
                if (!RefDocID.equalsIgnoreCase("")) {
                    actionFloatingButton.setVisibility(View.INVISIBLE);
                    dialog.cancel();
                    Intent intent = new Intent(CRMStreamInfo.this, CRMNote.class);
                    intent.putExtra("Contact_id", Contact_id);
                    intent.putExtra("FromWhere", "ChildScreen");
                    intent.putExtra("Ref_DocID", RefDocID);
                    intent.putExtra("TDocID", "");
                    intent.putExtra("Flag", Flag);
                    startActivity(intent);
                } else {
                    new Custom_Toast(CRMStreamInfo.this, "First Step To Add Action").showCustomAlert();
                }
            }
        });
        // it call when click on the item whose id is demo1.
        addDeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // diss miss the dialog
                if (!RefDocID.equalsIgnoreCase("")) {
                    actionFloatingButton.setVisibility(View.INVISIBLE);
                    dialog.cancel();
                    Intent intent = new Intent(CRMStreamInfo.this, CRMDeal.class);
                    intent.putExtra("Contact_id", Contact_id);
                    intent.putExtra("Ref_DocID", RefDocID);
                    intent.putExtra("TDocID", "");
                    intent.putExtra("Flag", Flag);
                    startActivity(intent);


                } else {
                    new Custom_Toast(CRMStreamInfo.this, "First Step To Add Action").showCustomAlert();
                }

            }
        });
        // it call when click on the item whose id is demo1.
        addCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // diss miss the dialog
                if (!RefDocID.equalsIgnoreCase("")) {
                    actionFloatingButton.setVisibility(View.INVISIBLE);
                    dialog.cancel();
                    Intent intent = new Intent(CRMStreamInfo.this, CRMCall.class);
                    intent.putExtra("Contact_id", Contact_id);
                    intent.putExtra("FromWhere", "ChildScreen");
                    intent.putExtra("Ref_DocID", RefDocID);
                    intent.putExtra("TDocID", "");
                    intent.putExtra("Flag", Flag);
                    startActivity(intent);
                    dialog.cancel();
                } else {
                    new Custom_Toast(CRMStreamInfo.this, "First Step To Add Action").showCustomAlert();
                }

            }
        });
        // it call when click on the item whose id is demo1.
        addAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // diss miss the dialog
                actionFloatingButton.setVisibility(View.INVISIBLE);
                dialog.cancel();
                Intent intent = new Intent(CRMStreamInfo.this, CrmTask.class);
                intent.putExtra("Contact_id", Contact_id);
                intent.putExtra("FromWhere", "ChildScreen");
                intent.putExtra("Ref_DocID", RefDocID);
                intent.putExtra("TDocID", "");
                intent.putExtra("Flag", Flag);
                startActivity(intent);
            }
        });
        addPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // diss miss the dialog
                actionFloatingButton.setVisibility(View.INVISIBLE);
                dialog.cancel();
                Intent intent = new Intent(CRMStreamInfo.this, CrmPayment.class);
                intent.putExtra("Contact_id", Contact_id);
                intent.putExtra("FromWhere", "ChildScreen");
                intent.putExtra("Ref_DocID", RefDocID);
                intent.putExtra("TDocID", "");
                intent.putExtra("Flag", Flag);
                startActivity(intent);
            }
        });
        dialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //actionFloatingButton.setVisibility(View.VISIBLE);
        finish();
    }

    public boolean setSpinner(Spinner spinner, ArrayList<Owner> arrayList) {
        CustomAdapterCRMStreamInfo adapter = new CustomAdapterCRMStreamInfo(CRMStreamInfo.this, arrayList, R.layout.spinner_adapter_view);
        spinner.setAdapter(adapter);


        return true;
    }

    View.OnClickListener editClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final View innerview = v;
            final TextView tv = (TextView) innerview;
            Object data = tv.getTag();
            String[] typeID = data.toString().split("-");
            String type, id;
            type = typeID[0];
            id = typeID[1];
            if (type.equalsIgnoreCase("Note")) {
                Intent intent = new Intent(CRMStreamInfo.this, CRMNote.class);
                intent.putExtra("Contact_id", Contact_id);
                intent.putExtra("Ref_DocID", RefDocID);
                intent.putExtra("TDocID", id);
                startActivity(intent);
            } else if (type.equalsIgnoreCase("Call")) {
                Intent intent = new Intent(CRMStreamInfo.this, CRMCall.class);
                intent.putExtra("Contact_id", Contact_id);
                intent.putExtra("Ref_DocID", RefDocID);
                intent.putExtra("TDocID", id);
                startActivity(intent);
            } else if (type.equalsIgnoreCase("Deal")) {
                System.out.println("ID:" + id);
                Intent intent = new Intent(CRMStreamInfo.this, CRMDeal.class);
                intent.putExtra("Contact_id", Contact_id);
                intent.putExtra("Ref_DocID", RefDocID);
                intent.putExtra("TDocID", id);
                startActivity(intent);
            } else if (type.equalsIgnoreCase("Action")) {
                System.out.println("ID:" + id);
                Intent intent = new Intent(CRMStreamInfo.this, CrmTask.class);
                intent.putExtra("Contact_id", Contact_id);
                intent.putExtra("FromWhere", "ChildScreen");
                intent.putExtra("Ref_DocID", RefDocID);
                intent.putExtra("TDocID", id);
                startActivity(intent);
            } else if (type.equalsIgnoreCase("Payment")) {
                Intent intent = new Intent(CRMStreamInfo.this, CrmPayment.class);
                intent.putExtra("Contact_id", Contact_id);
                intent.putExtra("Ref_DocID", RefDocID);
                intent.putExtra("TDocID", id);
                startActivity(intent);
            }


        }
    };
    View.OnClickListener deleteClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final View innerview = v;
            final TextView tv = (TextView) innerview;
            Object data = tv.getTag();
            String[] typeID = data.toString().split("-");
            DeleteDataValues params = new DeleteDataValues(typeID[0], typeID[1]);
            deleteData deleteDataValuesAsyncTask = new deleteData();
            deleteDataValuesAsyncTask.execute(params);
        }
    };

    private static class DeleteDataValues {
        String type, Id;

        DeleteDataValues(String type, String Id) {
            this.type = type;
            this.Id = Id;
        }
    }

    public class deleteData extends AsyncTask<DeleteDataValues, Void, String> {
        String server_response;

        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(CRMStreamInfo.this, "Deleting Data", "Please Wait...", true);
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(DeleteDataValues... params) {
            String type = params[0].type;
            String id = params[0].Id;
            id = id.replaceAll(" ", "%20");
            try {
                URL url = null;
                List<NameValuePair> parameter = new ArrayList<NameValuePair>();
                if (type.equalsIgnoreCase("Note")) {
                    parameter.add(new BasicNameValuePair("TaskNoteId", id));
                    url = new URL("http://" + server + "/And_Sync.asmx/XjsDeleteNote_CRM");
                } else if (type.equalsIgnoreCase("Call")) {
                    parameter.add(new BasicNameValuePair("TaskCallId", id));
                    url = new URL("http://" + server + "/And_Sync.asmx/XjsDeleteCall_CRM");
                } else if (type.equalsIgnoreCase("Deal")) {
                    parameter.add(new BasicNameValuePair("TaskDealId", id));
                    url = new URL("http://" + server + "/And_Sync.asmx/XjsDeleteDeal_CRM");
                } else if (type.equalsIgnoreCase("Action")) {
                    parameter.add(new BasicNameValuePair("Tdocid", id));
                    url = new URL("http://" + server + "/And_Sync.asmx/XjsDeleteTask_CRM");
                } else if (type.equalsIgnoreCase("Payment")) {
                    parameter.add(new BasicNameValuePair("id", id));
                    url = new URL("http://" + server + "/And_Sync.asmx/XjsDeletePaymentCollection_CRM");
                }
                // here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(parameter));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    server_response = sb.toString();
                }

            } catch (Exception e) {
                if (progressDialog != null)
                    progressDialog.dismiss();
                e.printStackTrace();
            }
            return server_response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog != null)
                progressDialog.dismiss();
            // if(progressDialog.isShowing()){progressDialog.dismiss();}
            Log.e("Response", "" + server_response);
            if (server_response != null) {
                if (!server_response.isEmpty()) {
                    server_response = server_response.replaceAll("\"", "");
                    if (server_response.equalsIgnoreCase("Y")) {
                        new Custom_Toast(CRMStreamInfo.this, "Delete Sussesfully").showCustomAlert();
                        firstFuction();
                    } else {
                        new Custom_Toast(CRMStreamInfo.this, "Try Again! " + server_response).showCustomAlert();
                    }
                }
            }
        }
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    View.OnClickListener viewMileClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            TextView text = (TextView) view;
            Intent newIntent = new Intent(CRMStreamInfo.this, ViewMileStoneActivity.class);
            newIntent.putExtra("dealId", text.getTag().toString());
            startActivity(newIntent);

        }
    };


    public class UpdatespinnerDataOnServer extends AsyncTask<String, Void, String> {
        String server_response;
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        protected String doInBackground(String... arg0) {
            try {


                URL url = new URL("http://" + server + "/And_Sync.asmx/XjsUpdcontactinfo_CRM"); // here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                params.add(new BasicNameValuePair("contactid", Contact_id));
                params.add(new BasicNameValuePair("type", arg0[0]));
                params.add(new BasicNameValuePair("id", selectedSpinId));

                writer.write(getQuery(params));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    server_response = sb.toString();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }


            } catch (Exception e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                return new String("Exception: " + e.getMessage());
            }


        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // if(progressDialog.isShowing()){progressDialog.dismiss();}
            Log.e("Response", "" + server_response);
            if (server_response != null) {
                if (!server_response.isEmpty()) {
                    server_response = server_response.replaceAll("\"", "");
                    if (server_response.equalsIgnoreCase("Y")) {
                        //new Custom_Toast(CRMStreamInfo.this,"Record Updated").showCustomAlert();
                    } else {
                        //new Custom_Toast(CRMStreamInfo.this,"Try Again! Record Not Saved").showCustomAlert();
                    }
                }
            }
        }
    }


    public void showTagDiaog() {
        ArrayList<String> listing = new ArrayList<String>();
        Owner item;
        int selectedIndex = 0;
        for (int i = 0; i < tagArrayList.size(); i++) {
            if (tagArrayList != null && tagArrayList.get(i).getId().equals(selectedTagId)) {
                selectedIndex = i;
            }
            item = tagArrayList.get(i);
            listing.add(item.getName());
            //getPath is a method in the customtype class which will return value in string format

        }
        final CharSequence[] fol_list = listing.toArray(new CharSequence[listing.size()]);
        List<String> list = Arrays.asList();
        CharSequence[] cs = listing.toArray(new CharSequence[list.size()]);
        System.out.println(Arrays.toString(cs)); // [foo, bar, waa]

        new AlertDialog.Builder(this)
                .setSingleChoiceItems(cs, selectedIndex, null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        // Do something useful withe the position of the selected radio button
                        selectedSpinId = tagArrayList.get(selectedPosition).getId();
                        tagSpinner.setText(tagArrayList.get(selectedPosition).getName());
                        new UpdatespinnerDataOnServer().execute("Tag");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    public void showStatusDiaog() {
        ArrayList<String> listing = new ArrayList<String>();
        Owner item;
        int selectedIndex = 0;
        for (int i = 0; i < statusArrayList.size(); i++) {
            if (statusArrayList != null && statusArrayList.get(i).getId().equals(selectedStatusId)) {
                selectedIndex = i;
            }
            item = statusArrayList.get(i);
            listing.add(item.getName());
            //getPath is a method in the customtype class which will return value in string format

        }
        final CharSequence[] fol_list = listing.toArray(new CharSequence[listing.size()]);
        List<String> list = Arrays.asList();
        CharSequence[] cs = listing.toArray(new CharSequence[list.size()]);
        System.out.println(Arrays.toString(cs)); // [foo, bar, waa]

        new AlertDialog.Builder(this)
                .setSingleChoiceItems(cs, selectedIndex, null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        // Do something useful withe the position of the selected radio button
                        // int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                        selectedSpinId = statusArrayList.get(selectedPosition).getId();
                        statusSpinner.setText(statusArrayList.get(selectedPosition).getName());
                        new UpdatespinnerDataOnServer().execute("Status");
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    public void showOwnerDiaog() {
        ArrayList<String> listing = new ArrayList<String>();
        Owner item;
        int selectedIndex = 0;
        for (int i = 0; i < ownerArrayList.size(); i++) {

            if (ownerArrayList != null && ownerArrayList.get(i).getId().equals(selectedOwnerId)) {
                selectedIndex = i;
            }
            item = ownerArrayList.get(i);
            listing.add(item.getName());
            //getPath is a method in the customtype class which will return value in string format

        }

        final CharSequence[] fol_list = listing.toArray(new CharSequence[listing.size()]);
        List<String> list = Arrays.asList();
        CharSequence[] cs = listing.toArray(new CharSequence[list.size()]);
        System.out.println(Arrays.toString(cs)); // [foo, bar, waa]

        new AlertDialog.Builder(this)
                .setSingleChoiceItems(cs, selectedIndex, null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        int selectedPosition = ((AlertDialog) dialog).getListView().getCheckedItemPosition();
                        selectedSpinId = ownerArrayList.get(selectedPosition).getId();
                        ownerSpinner.setText(ownerArrayList.get(selectedPosition).getName());
                        new UpdatespinnerDataOnServer().execute("Owner");
                        // Do something useful withe the position of the selected radio button
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}