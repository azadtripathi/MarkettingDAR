package com.dm.crmdm_app;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.controller.EnviroController;
import com.dm.controller.SessionDataController;
import com.dm.controller.SmanController;
import com.dm.controller.TourPlanController;
import com.dm.database.DatabaseConnection;
import com.dm.library.AlertMessage;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomArrayAdopter;
import com.dm.library.CustomTourPlanAprovalArrayAdapter;
import com.dm.library.Custom_Toast;
import com.dm.library.DataTransferInterface;
import com.dm.library.DbCon;
import com.dm.library.IntentSend;
import com.dm.model.AppData;
import com.dm.model.DashboardModel;
import com.dm.model.SessionData;
import com.dm.model.Sman;
import com.dm.model.TourDate;
import com.dm.parser.JSONParser;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class TourPlanAproval extends AppCompatActivity implements DataTransferInterface, CustomTourPlanAprovalArrayAdapter.HolderListener,AlertMessage.NoticeDialogListenerWithoutView {
    Spinner smanSpinner;
    ArrayList<String> smanArrayList;
    ArrayList<Sman> smanList;
    AlertOkDialog dialogWithOutView;
    ImageView save, cancel, find, delete, cam;
    SharedPreferences preferences;
    ActionBar actionBar;
    String currentDate;
    SimpleDateFormat df;
    Calendar c;
   // int hour, min;
    SmanController smanController;
    SharedPreferences preferences2;
    ConnectionDetector connectionDetector;
   // EnviroController enviroController;
    Button addTour;
    String conper, userId;
    String server, SmId, UserId;
    EditText remark , ramarkAproval;
    ListView listView1;
    AlertMessage alertMessage;
    LinearLayout statusLayout;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    ProgressDialog progressDialog;

    CustomTourPlanAprovalArrayAdapter customTourPlanAprovalArrayAdapter;
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
    String delResp = "",save_responce="" ,sendNotification="" ;
        boolean fromFind = false,ForUpdate=false;
    String DocId = "";
    LinearLayout linearSearchBox;
    TextView tv;
    RelativeLayout footer;

    RadioGroup ag;
    RadioButton radioButton;
    String status, remarkAproval, Seniour_Smid, Seniour_Userid;
    String fromdate = "", toDate = "", finalRemark = "", getdatafrom_notification = "";
    DbCon dbCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_plan_aproval);
        connectionDetector = new ConnectionDetector(getApplicationContext());
        listView1 = (ListView) findViewById(R.id.listView1);
        remark = (EditText) findViewById(R.id.TourRemark);
        remarkLayout = (LinearLayout) findViewById(R.id.remarkLayout);
        footer = (RelativeLayout)findViewById(R.id.footer);
        save = (ImageView) findViewById(R.id.button1);
        //footer.setVisibility(View.GONE);

        /************************************* Ashutosh *************************************/
        getSupportActionBar().setCustomView(R.layout.actionbar_custom_view_home);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageView iv = (ImageView) findViewById(R.id.image);
        dbCon=new DbCon(getApplicationContext());
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new IntentSend(getApplicationContext(), DashBoradActivity.class)).toSendAcivity();
                finish();
            }
        });
        tv = (TextView) findViewById(R.id.text);
        tv.setText("Tour Approval");
        /************************************************ Ashutosh ***********************************/
        preferences2 = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        Seniour_Smid = preferences2.getString("CONPERID_SESSION", null);
        Seniour_Userid = preferences2.getString("USER_ID", null);
        ramarkAproval = (EditText) findViewById(R.id.AprovalReason);
        ag = (RadioGroup) findViewById(R.id.approvalGroup);

        /************************************************ Ashutosh ***********************************/
        //linearSearchBox=(LinearLayout)findViewById(R.id.linearSearchBox);
        preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        conper = preferences.getString("CONPERID_SESSION", "0");
        userId = preferences.getString("USER_ID", "0");
        connectionDetector = new ConnectionDetector(getApplicationContext());
        tourPlanController = new TourPlanController(TourPlanAproval.this);
        sessionDataController = new SessionDataController(TourPlanAproval.this);
        c = Calendar.getInstance();
        df = new SimpleDateFormat("dd/MMM/yyyy");
        currentDate = df.format(c.getTime());
        appDataController1 = new AppDataController(TourPlanAproval.this);
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server = appData.getCompanyUrl();
        //fromDate = (EditText) findViewById(R.id.fromDate);
        //toDate = (EditText) findViewById(R.id.toDate);
        //smanSpinner = (Spinner) findViewById(R.id.salesPerson);
//        statusLayout=(LinearLayout) findViewById(R.id.statusLayout);
//        statusLayout.setVisibility(View.GONE);

        //cancel = (ImageView) findViewById(R.id.button2);
       // delete = (ImageView) findViewById(R.id.button3);
        //find = (ImageView) findViewById(R.id.findbutton1);
       // cam = (ImageView) findViewById(R.id.takePicture);
      //  cam.setVisibility(View.GONE);
       // delete.setVisibility(View.GONE);

        /************************************* Ashutosh *************************************/

        if (connectionDetector.isConnectingToInternet())

        {
            new GetSmanList().execute();

        } else {
            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();

            }
        });
    }

    void saveData() {
        int selectedId = ag.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
       /* if (fromDate.getText().toString().isEmpty()) {
            new Custom_Toast(getApplicationContext(), "Please select From Date").showCustomAlert();
        } else if (toDate.getText().toString().isEmpty()) {
            new Custom_Toast(getApplicationContext(), "Please select To Date").showCustomAlert();
        } */
        if (customTourPlanAprovalArrayAdapter == null) {

            new Custom_Toast(getApplicationContext(), "Please add tour plan").showCustomAlert();

        }
        else if (ramarkAproval.getText().toString().isEmpty())
        {
            new Custom_Toast(getApplicationContext(), "Please Add Remark").showCustomAlert();
        }
        else {
            tourPlanController.open();
            tourPlanListFromDb = tourPlanController.getTransTourPlan(String.valueOf(tourAndroidCode));
            tourPlanController.close();
            tourPlanController.open();
            tourPlanController.updatetTourHeaderRemark(remark.getText().toString(), tourAndroidCode);
            tourPlanController.close();
            if (connectionDetector.isConnectingToInternet()) {
                 new SaveTourPlanAproval().execute();
            }
            else
            {
                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
            }

           // }

        }

    }

    class SaveTourPlanAproval extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            remarkAproval = ramarkAproval.getText().toString();
            status = radioButton.getText().toString();
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TourPlanAproval.this, "Processing Data", "Processing Data Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                if (tourPlanListFromDb != null) {
                    delResp = insertTourPlan(tourPlanListFromDb,ForUpdate);
                }
                if(delResp != null){
                    save_responce = insertChildTourPlan(tourPlanListFromDb,ForUpdate,delResp);
                }
                if(save_responce != null){
                    sendNotification = insertNotificationTourPlan(tourPlanListFromDb,ForUpdate,delResp);
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
            if(sendNotification != null){
                new Custom_Toast(getApplicationContext(), sendNotification).showCustomAlert();
                Intent intent = new Intent(TourPlanAproval.this,DashBoradActivity.class);
                startActivity(intent);
            }
            else {
                new Custom_Toast(getApplicationContext(), save_responce).showCustomAlert();
            }
        }

    }

    /************************************* Ashutosh  new code for save (Start)************************************************/

                        String insertTourPlan(ArrayList<com.dm.model.TourPlan> tourPlanArrayList,boolean forUpdate) {
                            com.dm.model.TourPlan tourPlan = new com.dm.model.TourPlan();

                            if (tourPlanArrayList.size() > 0) {
                                tourPlanController.open();
                                tourPlan = tourPlanController.getTourHeader(tourPlanArrayList.get(0).getTourPlanHId());
                                tourPlanController.close();
                            }
                            boolean mSuccess = false;
                            String response1 = "";
                            String str= null;
                            String query = null,query1 = null,query2 = null,query3 = null,query4 = null,query5 = null,query6 = null,query7 = null,query8 = null;
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
                            if(!forUpdate)
                            {
                               /* str="http://" + server + "/And_Sync.asmx/xJSSaveTourHeader?smid=" + query +
                                        "&userid="+query1+
                                        "&fdt=" +query2+
                                        "&Tdt="+query3+
                                        "&rmkH=" +query4+
                                        "&dcid=" +query5;*/
                               // new Custom_Toast(getApplicationContext(), "Record Not Update").showCustomAlert();
                            }
                            else{
                                str="http://" + server + "/And_Sync.asmx/xJSSaveTourHeader?smid=" + query +
                                        "&userid="+query1+
                                        "&fdt=" +query2+
                                        "&Tdt="+query3+
                                        "&rmkH=" +query4+
                                        "&dcid=" +query5;
                            }

                            URLConnection urlConn = null;
                            BufferedReader bufferedReader = null;
                            try
                            {
                                URL url = new URL(str);
                                urlConn = url.openConnection();
                                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                                StringBuffer stringBuffer = new StringBuffer();
                                String line;
                                while ((line = bufferedReader.readLine()) != null)
                                {
                                    stringBuffer.append(line);
                                }

                                return stringBuffer.toString().replace("\"", "");
                            }
                            catch(Exception ex)
                            {
                                Log.e("App", "yourDataTask", ex);
                                return null;
                            }
                            finally
                            {
                                if(bufferedReader != null)
                                {
                                    try {
                                        bufferedReader.close();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                        String insertChildTourPlan(ArrayList<com.dm.model.TourPlan> tourPlanArrayList,boolean forUpdate,String dcid)  {
                            com.dm.model.TourPlan tourPlan = new com.dm.model.TourPlan();
                            boolean mSuccess = false;
                            String response1 = "";
                            String str = null;
                            for (int i = 0; i < tourPlanArrayList.size(); i++) {
                                String did = dcid.replaceAll(" ","%20");
                                String query = null,query1 = null,query2 = null,query3 = null,query4 = null,query5 = null,query6 = null,query7 = null,query8 = null;
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
                                if(!forUpdate)
                                {
                                   /* str="http://" + server + "/And_Sync.asmx/xJSSaveTourChild?Docid=" + query +
                                            "&rmk="+query1+
                                            "&date="+query2+
                                            "&Cid="+query3+
                                            "&Cnm="+query4+
                                            "&Dsid="+query5+
                                            "&Dsnm="+query6+
                                            "&Prid="+query7+
                                            "&prnm="+query8+
                                            "&flag="+"I";*/
                                    new Custom_Toast(getApplicationContext(), "Record Not Update").showCustomAlert();
                                }
                                else{
                                    if(dcid.equals("0")){
                                        new Custom_Toast(getApplicationContext(), "Record Not Update").showCustomAlert();
                                    }
                                    else {
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
                                try
                                {
                                    URL url = new URL(str);
                                    Log.i("str",str);
                                    urlConn = url.openConnection();
                                    bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                                    StringBuffer stringBuffer = new StringBuffer();
                                    String line;
                                    while ((line = bufferedReader.readLine()) != null)
                                    {
                                        stringBuffer.append(line);
                                    }

                                    response1 =  stringBuffer.toString().replace("\"", "");
                                }
                                catch(Exception ex)
                                {
                                    Log.e("App", "yourDataTask", ex);
                                    return null;
                                }
                                finally
                                {
                                    if(bufferedReader != null)
                                    {
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

                        String insertNotificationTourPlan(ArrayList<com.dm.model.TourPlan> tourPlanArrayList,boolean forUpdate,String dcid)  {
                            com.dm.model.TourPlan tourPlan = new com.dm.model.TourPlan();
                            boolean mSuccess = false;
                            String response1 = "";
                            String str = null;
                            if (tourPlanArrayList.size() > 0) {
                                tourPlanController.open();
                                tourPlan = tourPlanController.getTourHeader(tourPlanArrayList.get(0).getTourPlanHId());
                                tourPlanController.close();
                            }
                            String query = null, query1 = null, query2 = null, query3 = null, query4 = null, query5 = null;
                            try {

                                query = URLEncoder.encode(status, "utf-8");
                                query1 = URLEncoder.encode(remarkAproval, "utf-8");
                                query2 = URLEncoder.encode(Seniour_Smid, "utf-8");
                                query3 = URLEncoder.encode(Seniour_Userid, "utf-8");
                                query4 = URLEncoder.encode(tourPlan.getSMId(), "utf-8");
                                query5 = URLEncoder.encode(dcid, "utf-8");

                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            if(!forUpdate)
                            {
                                new Custom_Toast(getApplicationContext(), "Record Not Update").showCustomAlert();
                            }
                            else{
                                str="http://" + server + "/And_Sync.asmx/xJStourapproval?status=" + query +
                                        "&appremark=" + query1 +
                                        "&senior_smid=" + query2 +
                                        "&senior_userid=" + query3 +
                                        "&smid=" + query4 +
                                        "&docid=" + query5 +
                                        "&Type=" + "A" ;
                            }

                            URLConnection urlConn = null;
                            BufferedReader bufferedReader = null;
                            try
                            {
                                URL url = new URL(str);
                                Log.i("str",str);
                                urlConn = url.openConnection();
                                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                                StringBuffer stringBuffer = new StringBuffer();
                                String line;
                                while ((line = bufferedReader.readLine()) != null)
                                {
                                    stringBuffer.append(line);
                                }

                                response1 =  stringBuffer.toString().replace("\"", "");
                            }
                            catch(Exception ex)
                            {
                                Log.e("App", "yourDataTask", ex);
                                return null;
                            }
                            finally
                            {
                                if(bufferedReader != null)
                                {
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




   /* //String insertTourPlan(ArrayList<com.dm.model.TourPlan> tourPlanArrayList,boolean forUpdate) {
    String insertTourPlan(ArrayList<com.dm.model.TourPlan> tourPlanArrayList) {
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
        //DocId=DocId.replace(" ","%20");
        JSONParser jParser = new JSONParser();
        URLConnection urlConn = null;
        BufferedReader bufferedReader = null;

        try {
            HttpPost httppost=null;
            HttpClient httpclient = new DefaultHttpClient();
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                httppost = new HttpPost("http://" + server + "/And_Sync.asmx/xJStourapproval");

            nameValuePairs.add(new BasicNameValuePair("status", status));
            nameValuePairs.add(new BasicNameValuePair("appremark", remarkAproval));
            nameValuePairs.add(new BasicNameValuePair("senior_smid", Seniour_Smid));
            nameValuePairs.add(new BasicNameValuePair("senior_userid", Seniour_Userid));

            nameValuePairs.add(new BasicNameValuePair("smid", tourPlan.getSMId()));
            nameValuePairs.add(new BasicNameValuePair("docid", DocId));
            nameValuePairs.add(new BasicNameValuePair("userid", tourPlan.getUserID()));
            nameValuePairs.add(new BasicNameValuePair("fdt", fromdate));
            nameValuePairs.add(new BasicNameValuePair("Tdt", toDate));
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
                System.out.println("Response : " + httppost);
                response1 = httpclient.execute(httppost, responseHandler);
            }

        } catch (Exception e) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            System.out.println("Exception : " + e.getMessage());
            mSuccess = true;
        }
        return response1;
    }*/
    @Override
    public void onDialogPositiveWithoutViewClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeWithoutViewClick(DialogFragment dialog) {
        alertMessage.dismiss();
    }


    @Override
    public void setValues(ArrayList<CustomArrayAdopter.MyHolder> al) {

    }

    @Override
    public void holderListener(CustomTourPlanAprovalArrayAdapter.MyHolder myHolders) {

    }

    class GetSmanList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TourPlanAproval.this, "Loading Data", "Loading Please wait...", true);

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

              /*  smanAdapter = new ArrayAdapter<String>(TourPlanAproval.this,R.layout.adapterdropdown , smanArrayList);
                smanAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                smanSpinner.setAdapter(smanAdapter);
                smanSpinner.setSelection(select);*/

                intent = getIntent();
                if (intent != null) {
                    bundle = intent.getExtras();

                    if (bundle != null) {
                        fromFind = bundle.getBoolean("FromFind", false);
                        if (fromFind) {
                            DocId = intent.getStringExtra("DocId");
                            smId = intent.getStringExtra("setID");
                            //tourAndroidCode = bundle.getInt("tourAndroidCode", 0);
                            fromFind = intent.getBooleanExtra("FromFind", false);
                            ForUpdate = true;
/********************************* Ashutosh (12/04/17) (Start) *********************************************/
                            String aproval = intent.getStringExtra("aproval");
                            String App_remark = intent.getStringExtra("App_remark");
                            String getStatusForNotification = intent.getStringExtra("AppStatus");

                            try {
                                if (!App_remark.equals("")) {
                                    save.setEnabled(false);
                                    save.setClickable(false);
                                    save.setColorFilter(Color.parseColor("#888888"));
                                    ramarkAproval.setText(App_remark);
                                    ramarkAproval.setClickable(false);
                                    ramarkAproval.setEnabled(false);
                                    remark.setClickable(false);
                                    remark.setEnabled(false);
                                    RadioButton rb = (RadioButton) findViewById(R.id.reject);
                                    RadioButton rb1 = (RadioButton) findViewById(R.id.approve);
                                    getdatafrom_notification = "disable";

                                    if (getStatusForNotification.equals("Reject")) {
                                        rb.setChecked(true);
                                        rb1.setEnabled(false);
                                        rb1.setClickable(false);
                                    } else {
                                        rb1.setChecked(true);
                                        rb.setEnabled(false);
                                        rb.setClickable(false);
                                    }
                                    if (aproval.equalsIgnoreCase("true")) {
                                        tv.setText("Tour Plan");
                                        save.setEnabled(false);
                                        save.setClickable(false);
                                        save.setColorFilter(Color.parseColor("#888888"));
                                    }
                                }
                            }
                            catch (Exception e){}
                            if (Seniour_Smid.equalsIgnoreCase(smId)) {
                                ramarkAproval.setClickable(false);
                                ramarkAproval.setEnabled(false);
                                save.setEnabled(false);
                                save.setClickable(false);
                                save.setColorFilter(Color.parseColor("#888888"));
                                getdatafrom_notification = "disable";
                            }
/********************************* Ashutosh (12/04/17) (End) *********************************************/
                        } else {
                            tourAndroidCode = bundle.getInt("tourAndroidCode", 0);
                            smId = bundle.getString("smId", "");
                            currentDate = bundle.getString("currentDate", "");
                            ForUpdate = intent.getBooleanExtra("ForUpdate", false);

                            if (Seniour_Smid.equalsIgnoreCase(smId)) {
                                ramarkAproval.setClickable(false);
                                ramarkAproval.setEnabled(false);
                                save.setEnabled(false);
                                save.setClickable(false);
                                save.setColorFilter(Color.parseColor("#888888"));
                                getdatafrom_notification = "disable";
                            }
                        }
                    }
                            /*if (connectionDetector.isConnectingToInternet())

                            {
                                new GetTourPlanData().execute();

                            } else {

                                dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                                dialogWithOutView.show(getFragmentManager(), "Info");
                            }*/
                }

                if (bundle != null) {
                    if(fromFind)
                    {
                        if (connectionDetector.isConnectingToInternet())

                        {
                            new GetTourPlanData().execute();

                        } else {

                            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                            dialogWithOutView.show(getFragmentManager(), "Info");
                        }
                    }
                    else {
                        setFromFindData();
                    }
                    if(ForUpdate)
                    {
                        //fieldsEnable(true);
                    }

                    else
                    {
                        //fieldsEnable(false);
                    }
                }
            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            dbCon.open();
            //save.setEnabled(dbCon.ButtonEnable("LeaveRequest","Party","Add"));
            if(!dbCon.ButtonEnable("TourApproval","Plan","Add"))
            {
                save.setColorFilter(Color.parseColor("#808080"));
                save.setEnabled(false);
            }
            if(!dbCon.ButtonEnable("TourApproval","Plan","Edit"))
            {
                save.setColorFilter(Color.parseColor("#808080"));
                save.setEnabled(false);
            }
           /* if(!dbCon.ButtonEnable("TourApproval","Plan","Delete"))
            {
                delete.setColorFilter(Color.parseColor("#808080"));
                delete.setEnabled(false);
            }*/
            dbCon.close();
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

            setFromFindData();
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }


        }
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
    void setFromFindData() {

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
           // smanSpinner.setSelection(select);
           // fromDate.setText(tourPlan1.getFromDate());
           // toDate.setText(tourPlan1.getToDate());
            customTourPlanAprovalArrayAdapter = new CustomTourPlanAprovalArrayAdapter(TourPlanAproval.this, feedDahboardItemData1(), R.layout.tourplan_row, R.id.tourDate, TourPlanAproval.this, getdatafrom_notification);
            listView1.setAdapter(customTourPlanAprovalArrayAdapter);
            if (customTourPlanAprovalArrayAdapter.getCount() > 0) {
               // smanSpinner.setEnabled(false);
                remarkLayout.setVisibility(View.VISIBLE);
                if (fromFind) {
                    remark.setText(tourPlan1.getRemarks());
                    //fromDate.setText(tourPlan1.getFromDate());
                    //toDate.setText(tourPlan1.getToDate());
                    smId=tourPlan1.getSMId();
                   // fieldsEnable(false);
                }
                else if(ForUpdate)
                {
                    remark.setText(tourPlan1.getRemarks());
                    //fromDate.setText(tourPlan1.getFromDate());
                   // toDate.setText(tourPlan1.getToDate());
                    DocId=tourPlan1.getDocId();
                    smId=tourPlan1.getSMId();

                }
                else {
                    remark.setText(tourPlan1.getRemarks());
                }
            }
        }
    }
    private ArrayList<DashboardModel> feedDahboardItemData1() {
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
        System.out.println("Tour Plan 11111111ssssssssssss"+tourAndroidCode);
        System.out.println("Tour Plan"+(tourPlanListFromDb.get(0).getIsUpload()));
        tourPlanController.close();
        String upload="0";
        if (tourPlanListFromDb != null && tourPlanListFromDb.size()>0 )
        {
            try{
                if(tourPlanListFromDb.get(0).getIsUpload().equals("0"))
                {
                    upload="0";
                }
                else{
                    upload="1";
                }
            }
            catch(Exception e){}
        }


        if (tourPlanListFromDb != null) {
            for (int i = 0; i < tourPlanListFromDb.size(); i++) {
                dashboardModels.add(model.TourPlanDateWiseModel(tourPlanListFromDb.get(i).getVDate(), tourPlanListFromDb.get(i).getMCityName(), tourPlanListFromDb.get(i).getMDistName(), tourPlanListFromDb.get(i).getMPurposeName(), tourPlanListFromDb.get(i).getTourPlanHId(), tourPlanListFromDb.get(i).getSMId(), tourPlanListFromDb.get(i).getVDate(), tourPlanListFromDb.get(i).getRemarks(), tourPlanListFromDb.get(i).getCode(), upload));
            }
        }
               /* for (int j = 0; j < sessionDatasArrayList.size(); j++) {
                for (int i = 0; i < tourPlanListFromDb.size(); i++) {

                    if (sessionDatasArrayList.get(j).getItem_id().equalsIgnoreCase(tourPlanListFromDb.get(i).getVDate()))
                    {
                        dashboardModels.add(model.TourPlanDateWiseModel(sessionDatasArrayList.get(j).getItem_id(), tourPlanListFromDb.get(i).getMCityName(), tourPlanListFromDb.get(i).getMDistName(), tourPlanListFromDb.get(i).getMPurposeName(), tourPlanListFromDb.get(i).getTourPlanHId(), tourPlanListFromDb.get(i).getSMId(), tourPlanListFromDb.get(i).getVDate(), tourPlanListFromDb.get(i).getRemarks(), tourPlanListFromDb.get(i).getCode(),upload));
                        flag = true;
                    }
                }
                if (flag) {
                    flag = false;
                } else {
                    dashboardModels.add(model.TourPlanDateWiseModel(sessionDatasArrayList.get(j).getItem_id(), "None selected", "None selected", "None selected", String.valueOf(tourAndroidCode), smId, currentDate, "", "0",upload));
                }
                }*/

        return dashboardModels;
    }
    /*************************************************** Ashutosh *****************************************/
    public void getTourPlanDataList() {
        ArrayList<com.dm.model.TourPlan> tourList = new ArrayList<com.dm.model.TourPlan>();
        JSONParser jParser = new JSONParser();

//        GetTourHeader
        if (connectionDetector.isConnectingToInternet()) {
            try {
                String url="http://" + server + "/And_Sync.asmx/xJSGetDataforTourUpdateheader?docid=" + DocId;
                url=url.replaceAll(" ","%20");

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
                String url="http://" + server + "/And_Sync.asmx/xJSGetDataforTourUpdateChild?docid=" + DocId;
                url=url.replaceAll(" ","%20");
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
            tourAndroidCode=tourHeaderCode;
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
/*
    void clearData() {
        //fieldsEnable(false);
       // fromDate.setText("");
       // toDate.setText("");
        remark.setText("");
        bundle = null;
        tourHeaderCode = 0;
        tourAndroidCode = 0;
        smId = "";
        ForUpdate=false;
        fromFind=false;
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
        save.setImageResource(R.drawable.save1);
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
*/
    /*************************************************** Ashutosh *****************************************/
    @Override
    public void onBackPressed() {
        try {
            intent = getIntent();
            if (intent != null) {
                bundle = intent.getExtras();
                if (bundle != null) {
                    fromFind = bundle.getBoolean("FromFind", false);
                        String Notification = intent.getStringExtra("Notification");
                    String tour = intent.getStringExtra("TourAproval");
                    if (Notification != null) {
                        DbCon dbCon=new DbCon(getApplicationContext());
                        dbCon.open();
                        dbCon.truncate(DatabaseConnection.NOTIFIVATION_Data_TABLE);
                        dbCon.truncate(DatabaseConnection.NOTIFIVATION_TABLE);
                        dbCon.close();
                        Intent i = new Intent(TourPlanAproval.this, NotificationPannel.class);
                        startActivity(i);
                        finish();
                    }
                    else if(tour == null){
                        Intent i = new Intent(TourPlanAproval.this, FindTourPlan.class);
                        startActivity(i);
                        finish();
                    }
                    else {
                        Intent i = new Intent(TourPlanAproval.this, TourApproval.class);
                        startActivity(i);
                        finish();

                    }
                    }
                }
        }
        catch (Exception e){
            Intent i = new Intent(TourPlanAproval.this, DashBoradActivity.class);
            startActivity(i);
            finish();
        }
    }
}
