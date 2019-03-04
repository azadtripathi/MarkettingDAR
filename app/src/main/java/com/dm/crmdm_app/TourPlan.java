package com.dm.crmdm_app;

import android.app.ActionBar;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.controller.EnviroController;
import com.dm.controller.SessionDataController;
import com.dm.controller.SmanController;
import com.dm.controller.TourPlanController;
import com.dm.library.AlertMessage;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomArrayAdopter;
import com.dm.library.CustomTourPlanArrayAdapter;
import com.dm.library.Custom_Toast;
import com.dm.library.DataTransferInterface;
import com.dm.library.DateFunction;
import com.dm.library.DatePickerFragmentRange;
import com.dm.library.DbCon;
import com.dm.library.IntentSend;
import com.dm.model.AppData;
import com.dm.model.DashboardModel;
import com.dm.model.SessionData;
import com.dm.model.Sman;
import com.dm.model.TourDate;
import com.dm.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TourPlan extends AppCompatActivity implements DataTransferInterface, CustomTourPlanArrayAdapter.HolderListener, AlertMessage.NoticeDialogListenerWithoutView {
    Spinner smanSpinner;
    ArrayList<String> smanArrayList;
    ArrayList<Sman> smanList;
    AlertOkDialog dialogWithOutView;
    ImageView save, update, cancel, find, delete, cam;
    SharedPreferences preferences;
    ActionBar actionBar;
    String currentDate;
    SimpleDateFormat df;
    Calendar c, c1;
    int hour, min;
    SmanController smanController;
    SharedPreferences preferences2;
    ConnectionDetector connectionDetector;
    EnviroController enviroController;
    EditText fromDate, toDate;
    Button addTour;
    String conper, userId;
    String server, SmId, UserId;
    EditText remark;
    ListView listView1;
    AlertMessage alertMessage;
    LinearLayout statusLayout;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    ProgressDialog progressDialog;
    ArrayAdapter<String> smanAdapter, tourDateAdapter;
    ArrayList<TourDate> tourDateList;
    ArrayList<String> tourDateList1;
    CustomTourPlanArrayAdapter customTourPlanArrayAdapter;
    ArrayList<DashboardModel> dashboardModels;
    DashboardModel model;
    TourPlanController tourPlanController;
    int tourHeaderCode = 0;
    Bundle bundle;
    Intent intent;
    SessionDataController sessionDataController;
    com.dm.model.TourPlan tourPlan;
    int tourAndroidCode = 0;
    String smId = "";
    ArrayList<com.dm.model.TourPlan> tourPlanList;
    ArrayList<com.dm.model.TourPlan> tourPlanListFromDb;
    boolean flag = false;
    LinearLayout remarkLayout;
    String delResp = "", save_responce = "", sendNotification = "";
    boolean fromFind = false, ForUpdate = false;
    String DocId = "0";
    LinearLayout linearSearchBox;
    private int mYear, mMonth, mDay;
    DbCon dbCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_plan);
        getSupportActionBar().setCustomView(R.layout.actionbar_custom_view_home);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageView iv = (ImageView) findViewById(R.id.image);
        dbCon = new DbCon(getApplicationContext());
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new IntentSend(getApplicationContext(), DashBoradActivity.class)).toSendAcivity();
                finish();
            }
        });
        final TextView tv = (TextView) findViewById(R.id.text);
        tv.setText("Tour Plan");
        linearSearchBox = (LinearLayout) findViewById(R.id.linearSearchBox);
        preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        conper = preferences.getString("CONPERID_SESSION", "0");
        userId = preferences.getString("USER_ID", "0");
        connectionDetector = new ConnectionDetector(getApplicationContext());
        tourPlanController = new TourPlanController(TourPlan.this);
        sessionDataController = new SessionDataController(TourPlan.this);
        c = Calendar.getInstance();
        df = new SimpleDateFormat("dd/MMM/yyyy");
        currentDate = df.format(c.getTime());
        appDataController1 = new AppDataController(TourPlan.this);
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server = appData.getCompanyUrl();
        fromDate = (EditText) findViewById(R.id.fromDate);
        fromDate.setInputType(InputType.TYPE_NULL);
        fromDate.setFocusable(false);
        toDate = (EditText) findViewById(R.id.toDate);
        toDate.setInputType(InputType.TYPE_NULL);
        toDate.setFocusable(false);


        /*c = Calendar.getInstance();
        c1 = Calendar.getInstance();
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
        df = new SimpleDateFormat("dd/MMM/yyyy");
        currentDate =df.format(c.getTime());
        fromDate.setText(df.format(c.getTime()));
        toDate.setText(df.format(c.getTime()));*/


        smanSpinner = (Spinner) findViewById(R.id.salesPerson);
//        statusLayout=(LinearLayout) findViewById(R.id.statusLayout);
//        statusLayout.setVisibility(View.GONE);
        addTour = (Button) findViewById(R.id.addTour);
        listView1 = (ListView) findViewById(R.id.listView1);
        remark = (EditText) findViewById(R.id.TourRemark);
        remarkLayout = (LinearLayout) findViewById(R.id.remarkLayout);
        remarkLayout.setVisibility(View.GONE);
        save = (ImageView) findViewById(R.id.button1);
        update = (ImageView) findViewById(R.id.update);
        update.setVisibility(View.GONE);
        cancel = (ImageView) findViewById(R.id.button2);
        delete = (ImageView) findViewById(R.id.button3);
        find = (ImageView) findViewById(R.id.findbutton1);
        cam = (ImageView) findViewById(R.id.takePicture);
        cam.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);


        if (connectionDetector.isConnectingToInternet())

        {
            new GetSmanList().execute();

        } else {
            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }


        fromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = TourPlan.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker(v.getId());

                // Process to get Current Date
                   /* final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                    // Launch Date Picker Dialog
                    DatePickerDialog dpd = new DatePickerDialog(TourPlan.this, new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {
                                    // Display Selected date in textbox
                                    fromDate.setText(dayOfMonth + "-"
                                            + (monthOfYear + 1) + "-" + year);

                                }
                            }, mYear, mMonth, mDay);
                    dpd.show();*/
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = TourPlan.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker(v.getId());
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (connectionDetector.isConnectingToInternet())

                {
                    alertMessage = AlertMessage.newInstance("Do you want delete ?", "delete", "cancel");
                    alertMessage.show(getFragmentManager(), "delete");

                } else {

                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }

            }
        });

        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet())

                {

                    (new IntentSend(getApplicationContext(), FindTourPlan.class)).toSendAcivity();
                } else {

                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }

            }
        });

        addTour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fromDate.getText().toString().isEmpty()) {
                    new Custom_Toast(getApplicationContext(), "Please select From Date").showCustomAlert();
                } else if (toDate.getText().toString().isEmpty()) {
                    new Custom_Toast(getApplicationContext(), "Please select To Date").showCustomAlert();
                } else {

                    long f = DateFunction.ConvertDateToTimestamp(fromDate.getText().toString());
                    long t = DateFunction.ConvertDateToTimestamp(toDate.getText().toString());
                    if (f > t) {
                        new Custom_Toast(getApplicationContext(), "To Date Cannot be less than from date").showCustomAlert();

                    } else {
                        if (connectionDetector.isConnectingToInternet())

                        {
                            try {
                                new GetTourPlanDates().execute();
                            } catch (Exception e) {
                            }

                        } else {
                            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                            dialogWithOutView.show(getFragmentManager(), "Info");
                        }

                    }
                }
            }
        });

    }

    void clearData() {
        fieldsEnable(false);
        fromDate.setText("");
        toDate.setText("");
        remark.setText("");
        bundle = null;
        tourHeaderCode = 0;
        tourAndroidCode = 0;
        smId = "";
        ForUpdate = false;
        fromFind = false;
        sessionDataController.open();
        sessionDataController.deleteSessionBar();
        sessionDataController.close();
        tourPlanController.open();
        tourPlanController.deleteTourHeader();
        tourPlanController.deleteTransTour();
        tourPlanController.close();
        linearSearchBox.setVisibility(View.VISIBLE);
        listView1.setAdapter(null);
//        fromFind=false;
        //save.setImageResource(R.drawable.save1);
        update.setVisibility(View.GONE);
        save.setVisibility(View.VISIBLE);
        delete.setVisibility(View.GONE);
        remarkLayout.setVisibility(View.GONE);
        smanSpinner.setEnabled(true);
        int select = 0;
        if (smanList != null) {
            for (int i = 0; i < smanList.size(); i++) {
                if (smanList.get(i).getConPerId().equals(conper)) {
                    select = i;
                }
            }
        }
        smanSpinner.setSelection(select);
    }

    void saveData() {
        if (fromDate.getText().toString().isEmpty()) {
            new Custom_Toast(getApplicationContext(), "Please select From Date").showCustomAlert();
        } else if (toDate.getText().toString().isEmpty()) {
            new Custom_Toast(getApplicationContext(), "Please select To Date").showCustomAlert();
        } else if (customTourPlanArrayAdapter == null) {

            new Custom_Toast(getApplicationContext(), "Please add tour plan").showCustomAlert();

        } else {
            tourPlanController.open();
            tourPlanListFromDb = tourPlanController.getTransTourPlan(String.valueOf(tourAndroidCode));
            tourPlanController.close();
            if (tourPlanListFromDb.size() != customTourPlanArrayAdapter.getCount()) {

                new Custom_Toast(getApplicationContext(), "Please fill all entries in tour plan").showCustomAlert();
            } else {

                tourPlanController.open();
                tourPlanController.updatetTourHeaderRemark(remark.getText().toString(), tourAndroidCode);
                tourPlanController.close();
                if (connectionDetector.isConnectingToInternet()) {
                    new SaveTourPlan().execute();

                } else {
                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }

            }

        }

    }


    class SaveTourPlan extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TourPlan.this, "Processing Data", "Processing Data Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                if (tourPlanListFromDb != null) {
                    delResp = insertTourPlan(tourPlanListFromDb, ForUpdate);
                }
                if (delResp != null) {
                    save_responce = insertChildTourPlan(tourPlanListFromDb, ForUpdate, delResp);
                }
                if (save_responce.equals("1")) {
                    sendNotification = insertNotificationTourPlan(tourPlanListFromDb, ForUpdate, delResp);
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return sendNotification;
        }

        @Override
        protected void onPostExecute(String response1) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (save_responce.equals("1")) {
                new Custom_Toast(getApplicationContext(), sendNotification).showCustomAlert();
            } else {
                new Custom_Toast(getApplicationContext(), save_responce).showCustomAlert();
            }

            clearData();
        }

    }

    /************************************* Ashutosh  new code for save (Start)************************************************/

    String insertTourPlan(ArrayList<com.dm.model.TourPlan> tourPlanArrayList, boolean forUpdate) {
        com.dm.model.TourPlan tourPlan = new com.dm.model.TourPlan();

        if (tourPlanArrayList.size() > 0) {
            tourPlanController.open();
            tourPlan = tourPlanController.getTourHeader(tourPlanArrayList.get(0).getTourPlanHId());
            tourPlanController.close();
        }
        boolean mSuccess = false;
        String response1 = "";
        String str = null;
                /*try {
                    HttpPost httppost=null;
                    HttpClient httpclient = new DefaultHttpClient();
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    if(!forUpdate)
                    {
                        httppost = new HttpPost("http://" + server + "/And_Sync.asmx/xJSSaveTourHeader");
                    }
                    else{
                        httppost = new HttpPost("http://" + server + "/And_Sync.asmx/xJSUpdateTour");
                        nameValuePairs.add(new BasicNameValuePair("docid", tourPlan.getDocId()));
                    }

                    nameValuePairs.add(new BasicNameValuePair("smid", tourPlan.getSMId()));
                    nameValuePairs.add(new BasicNameValuePair("userid", tourPlan.getUserID()));
                    nameValuePairs.add(new BasicNameValuePair("fdt", tourPlan.getFromDate()));
                    nameValuePairs.add(new BasicNameValuePair("Tdt", tourPlan.getToDate()));
                    nameValuePairs.add(new BasicNameValuePair("rmkH", tourPlan.getRemarks()));
                    Log.e("docid", tourPlan.getDocId());
                    Log.e("smid", tourPlan.getSMId());
                    Log.e("userid", tourPlan.getUserID());
                    Log.e("fdt", tourPlan.getFromDate());
                    Log.e("Tdt", tourPlan.getToDate());
                    Log.e("rmkH", tourPlan.getRemarks());
                    if(httppost!=null) {
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        response1 = httpclient.execute(httppost, responseHandler);
                    }
                    //System.out.println("Response : " + response1);
                } catch (Exception e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    System.out.println("Exception : " + e.getMessage());
                    mSuccess = true;
                }
                return response1;*/
        String query = null, query1 = null, query2 = null, query3 = null, query4 = null, query5 = null, query6 = null, query7 = null, query8 = null;
        try {
            query = URLEncoder.encode(tourPlan.getSMId(), "utf-8");
            query1 = URLEncoder.encode(tourPlan.getUserID(), "utf-8");
            query2 = URLEncoder.encode(tourPlan.getFromDate(), "utf-8");
            query3 = URLEncoder.encode(tourPlan.getToDate(), "utf-8");
            query4 = URLEncoder.encode(tourPlan.getRemarks(), "utf-8");
            query5 = URLEncoder.encode(DocId, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!forUpdate) {
            str = "http://" + server + "/And_Sync.asmx/xJSSaveTourHeader?smid=" + query +
                    "&userid=" + query1 +
                    "&fdt=" + query2 +
                    "&Tdt=" + query3 +
                    "&rmkH=" + query4 +
                    "&dcid=" + query5;
        } else {
            str = "http://" + server + "/And_Sync.asmx/xJSSaveTourHeader?smid=" + query +
                    "&userid=" + query1 +
                    "&fdt=" + query2 +
                    "&Tdt=" + query3 +
                    "&rmkH=" + query4 +
                    "&dcid=" + query5;
        }

        URLConnection urlConn = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(str);
            urlConn = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            return stringBuffer.toString().replace("\"", "");
        } catch (Exception ex) {
            Log.e("App", "yourDataTask", ex);
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    String insertChildTourPlan(ArrayList<com.dm.model.TourPlan> tourPlanArrayList, boolean forUpdate, String dcid) {
        com.dm.model.TourPlan tourPlan = new com.dm.model.TourPlan();
        boolean mSuccess = false;
        String response1 = "";
        String str = null;
        for (int i = 0; i < tourPlanArrayList.size(); i++) {
            String query = null, query1 = null, query2 = null, query3 = null, query4 = null, query5 = null, query6 = null, query7 = null, query8 = null;
            try {
                query = URLEncoder.encode(dcid, "utf-8");
                query1 = URLEncoder.encode(tourPlanArrayList.get(i).getRemarks(), "utf-8");
                query2 = URLEncoder.encode(tourPlanArrayList.get(i).getVDate(), "utf-8");
                query3 = URLEncoder.encode(tourPlanArrayList.get(i).getMCityID(), "utf-8");
                query4 = URLEncoder.encode(tourPlanArrayList.get(i).getMCityName(), "utf-8");
                query5 = URLEncoder.encode(tourPlanArrayList.get(i).getMDistId(), "utf-8");
                query6 = URLEncoder.encode(tourPlanArrayList.get(i).getMDistName(), "utf-8");
                query7 = URLEncoder.encode(tourPlanArrayList.get(i).getMPurposeId(), "utf-8");
                query8 = URLEncoder.encode(tourPlanArrayList.get(i).getMPurposeName(), "utf-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (!forUpdate) {
                str = "http://" + server + "/And_Sync.asmx/xJSSaveTourChild?Docid=" + query +
                        "&rmk=" + query1 +
                        "&date=" + query2 +
                        "&Cid=" + query3 +
                        "&Cnm=" + query4 +
                        "&Dsid=" + query5 +
                        "&Dsnm=" + query6 +
                        "&Prid=" + query7 +
                        "&prnm=" + query8 +
                        "&flag=" + "I";
            } else {
                if (dcid.equals("0")) {
                    new Custom_Toast(getApplicationContext(), "Record Not Update").showCustomAlert();
                } else {
                    str = "http://" + server + "/And_Sync.asmx/xJSSaveTourChild?Docid=" + query +
                            "&rmk=" + query1 +
                            "&date=" + query2 +
                            "&Cid=" + query3 +
                            "&Cnm=" + query4 +
                            "&Dsid=" + query5 +
                            "&Dsnm=" + query6 +
                            "&Prid=" + query7 +
                            "&prnm=" + query8 +
                            "&flag=" + "U";
                }
            }

            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(str);
                Log.i("str", str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                response1 = stringBuffer.toString().replace("\"", "");
            } catch (Exception ex) {
                Log.e("App", "yourDataTask", ex);
                return null;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return response1;
    }

    String insertNotificationTourPlan(ArrayList<com.dm.model.TourPlan> tourPlanArrayList, boolean forUpdate, String dcid) {
        com.dm.model.TourPlan tourPlan = new com.dm.model.TourPlan();
        boolean mSuccess = false;
        String response1 = "";
        String str = null;
        String query = null;
        try {
            query = URLEncoder.encode(dcid, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (!forUpdate) {
            str = "http://" + server + "/And_Sync.asmx/xJStourapproval?status=" + "" +
                    "&appremark=" + "" +
                    "&senior_smid=" + "0" +
                    "&senior_userid=" + "0" +
                    "&smid=" + "0" +
                    "&docid=" + query +
                    "&Type=" + "E";
        } else {
            new Custom_Toast(getApplicationContext(), "Record Not Update").showCustomAlert();

        }

        URLConnection urlConn = null;
        BufferedReader bufferedReader = null;
        try {
            URL url = new URL(str);
            Log.i("str", str);
            urlConn = url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }

            response1 = stringBuffer.toString().replace("\"", "");
        } catch (Exception ex) {
            Log.e("App", "yourDataTask", ex);
            return null;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response1;
    }
/************************************* Ashutosh  new code for save (End)************************************************/
    /************************************* Ashutosh  previous code save (Start)************************************************/
    /*String insertTourPlan(ArrayList<com.dm.model.TourPlan> tourPlanArrayList,boolean forUpdate) {
        com.dm.model.TourPlan tourPlan = new com.dm.model.TourPlan();

        StringBuilder cid = new StringBuilder();
        StringBuilder cnm = new StringBuilder();
        StringBuilder did = new StringBuilder();
        StringBuilder dnm = new StringBuilder();
        StringBuilder pid = new StringBuilder();
        StringBuilder pnm = new StringBuilder();
        StringBuilder rem = new StringBuilder();
        for (int i = 0; i < tourPlanArrayList.size(); i++) {
            cid.append(tourPlanArrayList.get(i).getMCityID() + "@");
            cnm.append(tourPlanArrayList.get(i).getMCityName() + "@");
            did.append(tourPlanArrayList.get(i).getMDistId() + "@");
            dnm.append(tourPlanArrayList.get(i).getMDistName() + "@");
            pid.append(tourPlanArrayList.get(i).getMPurposeId() + "@");
            pnm.append(tourPlanArrayList.get(i).getMPurposeName() + "@");
            rem.append(tourPlanArrayList.get(i).getRemarks() + "@");
        }
        if (tourPlanArrayList.size() > 0) {
            tourPlanController.open();
            tourPlan = tourPlanController.getTourHeader(tourPlanArrayList.get(0).getTourPlanHId());
            tourPlanController.close();

        }

        boolean mSuccess = false;
        String response1 = "";
        try {
            HttpPost httppost=null;
            HttpClient httpclient = new DefaultHttpClient();
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            if(!forUpdate)
            {
                 httppost = new HttpPost("http://" + server + "/And_Sync.asmx/xJSSaveTour");
            }
            else{
                 httppost = new HttpPost("http://" + server + "/And_Sync.asmx/xJSUpdateTour");
                nameValuePairs.add(new BasicNameValuePair("docid", tourPlan.getDocId()));
            }


            nameValuePairs.add(new BasicNameValuePair("smid", tourPlan.getSMId()));
            nameValuePairs.add(new BasicNameValuePair("userid", tourPlan.getUserID()));
            nameValuePairs.add(new BasicNameValuePair("fdt", tourPlan.getFromDate()));
            nameValuePairs.add(new BasicNameValuePair("Tdt", tourPlan.getToDate()));
            nameValuePairs.add(new BasicNameValuePair("rmk", TourCityDropDownListAdapter.trimEnd((rem.toString()).trim(), '@')));
            nameValuePairs.add(new BasicNameValuePair("rmkH", tourPlan.getRemarks()));
            nameValuePairs.add(new BasicNameValuePair("Cid", TourCityDropDownListAdapter.trimEnd((cid.toString()).trim(), '@')));
            nameValuePairs.add(new BasicNameValuePair("Cnm", TourCityDropDownListAdapter.trimEnd((cnm.toString()).trim(), '@')));
            nameValuePairs.add(new BasicNameValuePair("Dsid", TourCityDropDownListAdapter.trimEnd((did.toString()).trim(), '@')));
            nameValuePairs.add(new BasicNameValuePair("Dsnm", TourCityDropDownListAdapter.trimEnd((dnm.toString()).trim(), '@')));
            nameValuePairs.add(new BasicNameValuePair("Prid", TourCityDropDownListAdapter.trimEnd((pid.toString()).trim(), '@')));
            nameValuePairs.add(new BasicNameValuePair("prnm", TourCityDropDownListAdapter.trimEnd((pnm.toString()).trim(), '@')));
            Log.e("docid", tourPlan.getDocId());
            Log.e("smid", tourPlan.getSMId());
            Log.e("userid", tourPlan.getUserID());
            Log.e("fdt", tourPlan.getFromDate());
            Log.e("Tdt", tourPlan.getToDate());
            Log.e("rmk", TourCityDropDownListAdapter.trimEnd((rem.toString()).trim(), '@'));
            Log.e("rmkH", tourPlan.getRemarks());
            Log.e("Cid", TourCityDropDownListAdapter.trimEnd((cid.toString()).trim(), '@'));
            Log.e("Cnm", TourCityDropDownListAdapter.trimEnd((cnm.toString()).trim(), '@'));
            Log.e("Dsid", TourCityDropDownListAdapter.trimEnd((did.toString()).trim(), '@'));
            Log.e("Dsnm", TourCityDropDownListAdapter.trimEnd((dnm.toString()).trim(), '@'));
            Log.e("Prid", TourCityDropDownListAdapter.trimEnd((pid.toString()).trim(), '@'));
            Log.e("prnm", TourCityDropDownListAdapter.trimEnd((pnm.toString()).trim(), '@'));
            if(httppost!=null) {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response1 = httpclient.execute(httppost, responseHandler);
            }
            //System.out.println("Response : " + response1);
        } catch (Exception e) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            System.out.println("Exception : " + e.getMessage());
            mSuccess = true;
        }
        return response1;
    }*/

    /************************************* Ashutosh  previous code save (End)************************************************/
    private void showDatePicker(int id) {
        //DateAndTimePicker date = new DateAndTimePicker();
        DatePickerFragmentRange date = new DatePickerFragmentRange();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        args.putInt("grace", 0);
        //args.putString("Type", "Days");
        args.putString("Type", "tour");
        date.setArguments(args);
        if (id == R.id.fromDate) {
            date.setCallBack(ondate1);
            date.show(getSupportFragmentManager(), "Date Picker");
        } else if (id == R.id.toDate) {
            date.setCallBack(ondate2);
            date.show(getSupportFragmentManager(), "Date Picker");
        }
    }

    OnDateSetListener ondate1 = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String strDate = (dayOfMonth < 10 ? ("0" + dayOfMonth) : (dayOfMonth)) + "/" + (((monthOfYear + 1) < 10 ? ("0" + (monthOfYear + 1)) : (monthOfYear + 1))) + "/" + year;

            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MMM/yyyy");
//			 SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = format1.parse(strDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            fromDate.setText(format2.format(date));
        }
    };

    OnDateSetListener ondate2 = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String strDate = (dayOfMonth < 10 ? ("0" + dayOfMonth) : (dayOfMonth)) + "/" + (((monthOfYear + 1) < 10 ? ("0" + (monthOfYear + 1)) : (monthOfYear + 1))) + "/" + year;
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MMM/yyyy");
//			 SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            long f;
            Date date = null;
            try {
                date = format1.parse(strDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Date filledDate = null;
            try {
                filledDate = format2.parse(format2.format(date));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            f = DateFunction.ConvertDateToTimestamp(fromDate.getText().toString());
            if (filledDate.getTime() >= f) {
                toDate.setText(format2.format(date));

            } else {
                new Custom_Toast(getApplicationContext(), "To Date Cannot be less than from date").showCustomAlert();
            }

        }
    };

    @Override
    public void setValues(ArrayList<CustomArrayAdopter.MyHolder> al) {

    }

    @Override
    public void holderListener(CustomTourPlanArrayAdapter.MyHolder myHolders) {

    }


    class GetSmanList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TourPlan.this, "Loading Data", "Loading Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                smanList = getSmanList();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (smanList != null) {
                smanArrayList = new ArrayList<String>();
                int select = 0;
                for (int i = 0; i < smanList.size(); i++) {
                    if (smanList.get(i).getConPerId().equals(conper)) {
                        select = i;
                    }
                    smanArrayList.add(smanList.get(i).getDisplayName());
                }

                smanAdapter = new ArrayAdapter<String>(TourPlan.this, R.layout.adapterdropdown, smanArrayList);
                smanAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                smanSpinner.setAdapter(smanAdapter);
                smanSpinner.setSelection(select);
                intent = getIntent();
                if (intent != null) {
                    bundle = intent.getExtras();
                    if (bundle != null) {
                        fromFind = bundle.getBoolean("FromFind", false);
                        if (fromFind) {
                            DocId = intent.getStringExtra("DocId");
                            fromFind = intent.getBooleanExtra("FromFind", false);
                            ForUpdate = true;
                            delete.setVisibility(View.VISIBLE);

                        } else {
                            tourAndroidCode = bundle.getInt("tourAndroidCode", 0);
                            smId = bundle.getString("smId", "");
                            currentDate = bundle.getString("currentDate", "");
                            ForUpdate = intent.getBooleanExtra("ForUpdate", false);
                        }
                    }
                }

                if (bundle != null) {
                    if (fromFind) {
                        if (connectionDetector.isConnectingToInternet())

                        {
                            new GetTourPlanData().execute();

                        } else {

                            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                            dialogWithOutView.show(getFragmentManager(), "Info");
                        }
                    } else {
                        setFromFindData(false);
                    }
                    if (ForUpdate) {
                        fieldsEnable(true);
                    } else {
                        fieldsEnable(false);
                    }
                }
            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            dbCon.open();
            //save.setEnabled(dbCon.ButtonEnable("LeaveRequest","Party","Add"));
            if (!dbCon.ButtonEnable("TourPlan", "Plan", "Add")) {
                save.setColorFilter(Color.parseColor("#808080"));
                save.setEnabled(false);
            }
            if (!dbCon.ButtonEnable("TourPlan", "Plan", "Edit")) {
                update.setColorFilter(Color.parseColor("#808080"));
                update.setEnabled(false);
            }
            if (!dbCon.ButtonEnable("TourPlan", "Plan", "Delete")) {
                delete.setColorFilter(Color.parseColor("#808080"));
                delete.setEnabled(false);
            }
            dbCon.close();
        }
    }

    void setFromFindData(boolean forUpdate) {

        tourPlanController.open();
        com.dm.model.TourPlan tourPlan1 = tourPlanController.getTourHeader(String.valueOf(tourAndroidCode));
        tourPlanController.close();

        if (tourPlan1 != null) {
            int select = 0;
            for (int i = 0; i < smanList.size(); i++) {
                if (smanList.get(i).getConPerId().equals(tourPlan1.getSMId())) {
                    select = i;
                }
            }
            smanSpinner.setSelection(select);
            fromDate.setText(tourPlan1.getFromDate());
            toDate.setText(tourPlan1.getToDate());
            customTourPlanArrayAdapter = new CustomTourPlanArrayAdapter(TourPlan.this, feedDahboardItemData1(forUpdate), R.layout.tourplan_row, R.id.tourDate, TourPlan.this);
            listView1.setAdapter(customTourPlanArrayAdapter);
            if (customTourPlanArrayAdapter.getCount() > 0) {
                smanSpinner.setEnabled(false);
                remarkLayout.setVisibility(View.VISIBLE);
                if (fromFind) {
                    remark.setText(tourPlan1.getRemarks());
                    fromDate.setText(tourPlan1.getFromDate());
                    toDate.setText(tourPlan1.getToDate());
                    smId = tourPlan1.getSMId();
                    fieldsEnable(false);
                } else if (ForUpdate) {
                    remark.setText(tourPlan1.getRemarks());
                    fromDate.setText(tourPlan1.getFromDate());
                    toDate.setText(tourPlan1.getToDate());
                    DocId = tourPlan1.getDocId();
                    smId = tourPlan1.getSMId();

                }
            }


        }


    }

    void fieldsEnable(boolean flag) {
        if (flag) {
            linearSearchBox.setVisibility(View.GONE);
            toDate.setClickable(false);
            fromDate.setClickable(false);
            toDate.setEnabled(false);
            fromDate.setEnabled(false);
            addTour.setEnabled(false);
            addTour.setClickable(false);
            // save.setImageResource(R.drawable.update);
            save.setVisibility(View.GONE);
            update.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
        } else {
            toDate.setClickable(true);
            fromDate.setClickable(true);
            toDate.setEnabled(true);
            fromDate.setEnabled(true);
            addTour.setEnabled(true);
            addTour.setClickable(true);
            //save.setImageResource(R.drawable.save1);
            //delete.setVisibility(View.GONE);
            linearSearchBox.setVisibility(View.GONE);
        }

    }

    class GetTourPlanDates extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TourPlan.this, "Loading Data", "Loading Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                tourDateList = getDateList();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (tourDateList != null && tourDateList.size() > 0) {
                if (tourDateList.get(0).getStatus().equalsIgnoreCase("E")) {
                    new Custom_Toast(getApplicationContext(), "Record Already Exists for Date:" + tourDateList.get(0).getTDate()).showCustomAlert();

                } else {

                    customTourPlanArrayAdapter = new CustomTourPlanArrayAdapter(TourPlan.this, feedDahboardItemData(tourDateList), R.layout.tourplan_row, R.id.tourDate, TourPlan.this);
                    listView1.setAdapter(customTourPlanArrayAdapter);
                    if (customTourPlanArrayAdapter.getCount() > 0) {
                        smanSpinner.setEnabled(false);
                        linearSearchBox.setVisibility(View.GONE);
                        remarkLayout.setVisibility(View.VISIBLE);
                    }

                }


            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    class GetTourPlanData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
//            progressDialog = ProgressDialog.show(TourPlan.this, "Loading Data", "Loading Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                getTourPlanDataList();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            setFromFindData(true);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }


        }
    }

    /*************************************************** Ashutosh *****************************************/
    public void getTourPlanDataList() {
        ArrayList<com.dm.model.TourPlan> tourList = new ArrayList<com.dm.model.TourPlan>();
        String fromdate = "", toDate = "", finalRemark = "";
        JSONParser jParser = new JSONParser();

//        GetTourHeader
        if (connectionDetector.isConnectingToInternet()) {
            try {
                String url = "http://" + server + "/And_Sync.asmx/xJSGetDataforTourUpdateheader?docid=" + DocId;
                url = url.replaceAll(" ", "%20");

                JSONArray jsonarray = new JSONArray(jParser.getJSONArray(url));
                for (int i = 0; i < jsonarray.length(); i++) {
                    fromdate = jsonarray.getJSONObject(i).getString("TourFromDt");
                    toDate = jsonarray.getJSONObject(i).getString("TourToDt");
                    finalRemark = jsonarray.getJSONObject(i).getString("FinalRemarks");

                }
                jsonarray = null;

            } catch (Exception e) {

                System.out.println(e);

            }
        }
//GetTransTour
        if (connectionDetector.isConnectingToInternet()) {
            try {
                String url = "http://" + server + "/And_Sync.asmx/xJSGetDataforTourUpdateChild?docid=" + DocId;
                url = url.replaceAll(" ", "%20");
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray(url));

                for (int i = 0; i < jsonarray.length(); i++) {
                    com.dm.model.TourPlan tourPlan = new com.dm.model.TourPlan();
                    tourPlan.setSMId(jsonarray.getJSONObject(i).getString("SMId"));
                    tourPlan.setAppStatus(jsonarray.getJSONObject(i).getString("AppStatus"));
                    tourPlan.setAppRemark(jsonarray.getJSONObject(i).getString("AppRemark"));
                    tourPlan.setTourPlanId(jsonarray.getJSONObject(i).getString("TourPlanId"));
                    tourPlan.setVDate(jsonarray.getJSONObject(i).getString("VDate"));
                    tourPlan.setMCityID(jsonarray.getJSONObject(i).getString("MCityId"));
                    tourPlan.setMDistId(jsonarray.getJSONObject(i).getString("MDistId"));
                    tourPlan.setMPurposeId(jsonarray.getJSONObject(i).getString("MPurposeId"));
                    tourPlan.setMCityName(jsonarray.getJSONObject(i).getString("MCityname"));
                    tourPlan.setMDistNamed(jsonarray.getJSONObject(i).getString("MDistname"));
                    tourPlan.setMPurposeName(jsonarray.getJSONObject(i).getString("MPurposename"));
                    tourPlan.setRemarks(jsonarray.getJSONObject(i).getString("Remarks"));
                    tourList.add(tourPlan);
                }
                jsonarray = null;

            } catch (Exception e) {

                System.out.println(e);

            }
        }
        if (tourList != null && tourList.size() > 0) {
//    for(int i=0;i<tourList.size();i++)
//    {
            tourPlan = new com.dm.model.TourPlan();
            tourPlan.setVDate(fromdate);
            tourPlan.setFromDate(fromdate);
            tourPlan.setToDate(toDate);
            tourPlan.setSMId(tourList.get(0).getSMId());
            tourPlan.setUserID(userId);
            tourPlan.setRemarks(finalRemark);
            tourPlan.setDocId(DocId);
            tourPlan.setIsUpload("1");
            tourPlanController.open();
            tourPlanController.deleteTourHeader();
            tourPlanController.insertTransTourPlanHeader(tourPlan);
            tourPlanController.close();

            tourPlanController.open();
            tourHeaderCode = tourPlanController.getTourHeaderId(fromdate, tourList.get(0).getSMId());
            tourPlanController.close();
            tourAndroidCode = tourHeaderCode;
            sessionDataController.open();
            sessionDataController.deleteSessionBar();
            sessionDataController.close();
            tourPlanController.open();
            sessionDataController.open();
            for (int i = 0; i < tourList.size(); i++) {
                SessionData sessionData = new SessionData();
                sessionData.setItem_id(tourList.get(i).getVDate());
                sessionDataController.insertSessionData(sessionData);
                com.dm.model.TourPlan tourPlan = new com.dm.model.TourPlan();
                tourPlan.setTourPlanHId(String.valueOf(tourHeaderCode));
                tourPlan.setUserID(userId);
                tourPlan.setDocId(DocId);
                tourPlan.setIsUpload("1");
                tourPlan.setSMId(tourList.get(i).getSMId());
                tourPlan.setMCityID(tourList.get(i).getMCityID());
                tourPlan.setMCityName(tourList.get(i).getMCityName());
                tourPlan.setMDistId(tourList.get(i).getMDistId());
                tourPlan.setMDistNamed(tourList.get(i).getMDistName());
                tourPlan.setMPurposeId(tourList.get(i).getMPurposeId());
                tourPlan.setMPurposeName(tourList.get(i).getMPurposeName());
                tourPlan.setRemarks(tourList.get(i).getRemarks());
                tourPlan.setVDate(tourList.get(i).getVDate());
                tourPlanController.insertTransTourPlan(tourPlan);
            }
        }
        sessionDataController.close();
        tourPlanController.close();

    }

    /*************************************************** Ashutosh *****************************************/

    private ArrayList<DashboardModel> feedDahboardItemData(ArrayList<TourDate> tourdates) {
        dashboardModels = new ArrayList<DashboardModel>();
        model = new DashboardModel();
//        ArrayList<SessionData> sessionDataList=new ArrayList<SessionData> ();
        if (tourdates.size() > 0) {
            tourPlan = new com.dm.model.TourPlan();
            tourPlan.setVDate(currentDate);
            tourPlan.setFromDate(fromDate.getText().toString());
            tourPlan.setToDate(toDate.getText().toString());
            tourPlan.setSMId(smanList.get(smanSpinner.getSelectedItemPosition()).getConPerId());
            tourPlan.setUserID(userId);
            tourPlan.setIsUpload("0");
            tourPlanController.open();
            tourPlanController.deleteTourHeader();
            tourPlanController.insertTransTourPlanHeader(tourPlan);
            tourPlanController.close();
        }

        tourPlanController.open();
        tourHeaderCode = tourPlanController.getTourHeaderId(currentDate, smanList.get(smanSpinner.getSelectedItemPosition()).getConPerId());
        tourPlanController.close();
        sessionDataController.open();
        sessionDataController.deleteSessionBar();
        sessionDataController.close();
        for (int i = 0; i < tourdates.size(); i++) {

//            TourPlanDateWiseModel(String vDate,String city,String dist,String purpose)
            SessionData sessionData = new SessionData();
            sessionData.setItem_id(tourdates.get(i).getTDate());
            sessionDataController.open();
            sessionDataController.insertSessionData(sessionData);
            sessionDataController.close();
            dashboardModels.add(model.TourPlanDateWiseModel(tourdates.get(i).getTDate(), "None selected", "None selected", "None selected", String.valueOf(tourHeaderCode), smanList.get(smanSpinner.getSelectedItemPosition()).getConPerId(), currentDate, "", "0", "0"));
        }

        return dashboardModels;
    }

    //    private ArrayList<DashboardModel> feedDahboardItemData1(ArrayList<String> tourdates) {
    private ArrayList<DashboardModel> feedDahboardItemData1(boolean forUpdate) {
        dashboardModels = new ArrayList<DashboardModel>();
        model = new DashboardModel();
//         tourPlanList=new ArrayList<com.dm.model.TourPlan>();
//    getting session dates
        sessionDataController.open();
        ArrayList<SessionData> sessionDatasArrayList = sessionDataController.getSessionData();
        System.out.println("sessionDatasArrayList=" + sessionDatasArrayList.size());
        sessionDataController.close();
        tourPlanController.open();
        tourPlanListFromDb = tourPlanController.getTransTourPlan(String.valueOf(tourAndroidCode));
        System.out.println("Tour Plan 11111111ssssssssssss" + tourAndroidCode);
        System.out.println("Tour Plan" + (tourPlanListFromDb.get(0).getIsUpload()));
        tourPlanController.close();
        String upload = "0";
        if (tourPlanListFromDb != null && tourPlanListFromDb.size() > 0) {
            try {
                if (tourPlanListFromDb.get(0).getIsUpload().equals("0")) {
                    upload = "0";
                } else {
                    upload = "1";
                }
            } catch (Exception e) {
            }
//                if(forUpdate)
//                {
//                    upload="1";
//                }

        }


        if (tourPlanListFromDb != null) {
            for (int j = 0; j < sessionDatasArrayList.size(); j++) {
                for (int i = 0; i < tourPlanListFromDb.size(); i++) {

                    if (sessionDatasArrayList.get(j).getItem_id().equalsIgnoreCase(tourPlanListFromDb.get(i).getVDate())) {

                        dashboardModels.add(model.TourPlanDateWiseModel(sessionDatasArrayList.get(j).getItem_id(), tourPlanListFromDb.get(i).getMCityName(), tourPlanListFromDb.get(i).getMDistName(), tourPlanListFromDb.get(i).getMPurposeName(), tourPlanListFromDb.get(i).getTourPlanHId(), tourPlanListFromDb.get(i).getSMId(), tourPlanListFromDb.get(i).getVDate(), tourPlanListFromDb.get(i).getRemarks(), tourPlanListFromDb.get(i).getCode(), upload));


                        flag = true;
                    }
                }

                if (flag) {
                    flag = false;
                } else {

                    dashboardModels.add(model.TourPlanDateWiseModel(sessionDatasArrayList.get(j).getItem_id(), "None selected", "None selected", "None selected", String.valueOf(tourAndroidCode), smId, currentDate, "", "0", upload));


                }
            }
        }

        return dashboardModels;
    }

    public ArrayList<TourDate> getDateList() {
        ArrayList<TourDate> tourDateList = new ArrayList<TourDate>();
        JSONParser jParser = new JSONParser();
        if (connectionDetector.isConnectingToInternet()) {
            try {

                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetTourPlan?smid=" + conper + "&fdt=" + fromDate.getText().toString() + "&tdt=" + toDate.getText().toString()));

                for (int i = 0; i < jsonarray.length(); i++) {
                    TourDate tourDate = new TourDate();
                    tourDate.setTDate(jsonarray.getJSONObject(i).getString("dt"));
                    tourDate.setStatus(jsonarray.getJSONObject(i).getString("St"));
                    tourDateList.add(tourDate);
                }
                jsonarray = null;

            } catch (Exception e) {

                System.out.println(e);

            }
        }

        return tourDateList;
    }


    public ArrayList<Sman> getSmanList() {
        ArrayList<Sman> smanList = new ArrayList<Sman>();
        JSONParser jParser = new JSONParser();
        if (connectionDetector.isConnectingToInternet()) {
            try {

                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetUnderUser?Smid=" + conper));

                for (int i = 0; i < jsonarray.length(); i++) {
                    Sman sman = new Sman();
                    sman.setConPerId(jsonarray.getJSONObject(i).getString("Id"));
                    sman.setDisplayName(jsonarray.getJSONObject(i).getString("Name"));
                    sman.setRoleId(jsonarray.getJSONObject(i).getString("RoleName"));
                    sman.setUnderId(jsonarray.getJSONObject(i).getString("UserId"));
                    smanList.add(sman);
                }
                jsonarray = null;

            } catch (Exception e) {
                smanController.close();
                System.out.println(e);

            }
        }

        return smanList;
    }

    @Override
    public void onDialogPositiveWithoutViewClick(DialogFragment dialog) {
// TODO Auto-generated method stub

        if (connectionDetector.isConnectingToInternet())

        {
            new DeleteTourPlan().execute();

        } else {

            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

    }


    @Override
    public void onDialogNegativeWithoutViewClick(DialogFragment dialog) {
// TODO Auto-generated method stub
        alertMessage.dismiss();

    }

    class DeleteTourPlan extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TourPlan.this, "Delete", "Deleting Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                delResp = deletetTourPlan();

            } catch (Exception e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return delResp;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (delResp != null) {
                new Custom_Toast(getApplicationContext(), delResp).showCustomAlert();
                clearData();
            }


        }
    }

    String deletetTourPlan() {
        boolean mSuccess = false;
        String response1 = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/xJSDeleteTourplan");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("Docid", DocId));
            nameValuePairs.add(new BasicNameValuePair("smid", smId));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response1 = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response1);

        } catch (Exception e) {
            delResp = "";
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            mSuccess = true;
            System.out.println(e);

        }

        return response1;
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(TourPlan.this, DashBoradActivity.class);
        startActivity(i);
        super.onBackPressed();
    }


}
