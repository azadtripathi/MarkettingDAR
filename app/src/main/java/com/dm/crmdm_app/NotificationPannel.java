package com.dm.crmdm_app;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.controller.NotificationController;
import com.dm.database.DatabaseConnection;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.Custom_Toast;
import com.dm.library.DbCon;
import com.dm.library.IntentSend;
import com.dm.library.NotificationPannelAdaptor;
import com.dm.model.AppData;
import com.dm.model.DashboardModel;
import com.dm.model.Expense;
import com.dm.model.NotificationData;
import com.dm.parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

@SuppressLint("CommitPrefEdits")
public class NotificationPannel extends AppCompatActivity {
    ProgressDialog progressDialog; AlertOkDialog dialogWithOutView;
    String server;ArrayList<AppData> appDataArray;SharedPreferences preferences1;
    String SMID,MOb;ListView listview;SQLiteDatabase mydb;
    NotificationController notificationController;
    String timestamp="0"; ConnectionDetector connectionDetector;
    ArrayList<String> ArrayNotificationData;String GlobalPageName,DocIDOrLVQRID,Notid;
    SharedPreferences preferences2;
    ArrayList<DashboardModel> dashboardModels;
    boolean isMoreData = false;
    ArrayList<NotificationData> notiarrayList;
    NotificationPannelAdaptor adaptor;
    //ActionBar actionBar;
    @SuppressWarnings("deprecation")
    @SuppressLint({ "SimpleDateFormat", "WorldReadableFiles" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_pannel);
        getSupportActionBar().setCustomView(R.layout.actionbar_custom_view_home);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageView iv = (ImageView) findViewById(R.id.image);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // (new IntentSend(getApplicationContext(), DashBoradActivity.class)).toSendAcivity();
                finish();
            }
        });
        preferences2 = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        MOb = preferences2.getString("PDAID_SESSION", "");
        TextView tv = (TextView) findViewById(R.id.text);
        tv.setText("Notifications");
        notiarrayList = new ArrayList<>();
        listview=(ListView)findViewById(R.id.anplv);
        adaptor = new NotificationPannelAdaptor(this,notiarrayList);
        listview.setAdapter(adaptor);
        preferences1 = getSharedPreferences("MyPref", MODE_PRIVATE);
        SMID=preferences1.getString("CONPERID_SESSION","0");
        connectionDetector=new ConnectionDetector(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              /*  adapterView.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.cardview_light_background));

               */
//*/              view.setBackgroundColor(Color.parseColor("#f0f0f0"));
//                TextView tvDocId=(TextView)view.findViewById(R.id.nplrnotid);
                if(!notiarrayList.get(i).getStatus().equalsIgnoreCase("true"))
                {
                    reduceCount();
                }

                Notid=notiarrayList.get(i).getNotId();
                //GlobalPageName=notificationController.getPageName(DocIDOrLVQRID);Notid
                ArrayList<String> data=notificationController.getPageName(Notid);
                try {
                    GlobalPageName = data.get(0);
                    //DocIDOrLVQRID=notiarrayList.get(i).getNotId();
                    DocIDOrLVQRID = data.get(1);
                }catch (Exception e){GlobalPageName="";}
                if(GlobalPageName.length()>1) {
                    new GetFormData().execute();
                }
                //navigatetoPage(PageName);
            }
        });
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if(isMoreData)
                {
                    if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                        timestamp = notificationController.getTimeStamp();
                        isMoreData = false;
                        //  getNotificationData(SMID, notificationController.getTimeStamp());
                        if (connectionDetector.isConnectingToInternet()) {

                            new GetListNotificationData().execute();

                        } else {

                            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                            dialogWithOutView.show(getFragmentManager(), "Info");
                        }
                    }
                }
            }
        });
    }


    class GetListNotificationData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if(!isMoreData)
                progressDialog= ProgressDialog.show(NotificationPannel.this,"Loading Data", "Loading...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {

                // expenseGrpList=getExpenseGrpList();
                getNotificationData(SMID,timestamp);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if(progressDialog!=null)
            {
                progressDialog.dismiss();
            }
            setDataOnListView(timestamp);
        }
    }

    boolean isDataPopulate= false;
    private void setDataOnListView(String timestamp) {

        notiarrayList.clear();
        notificationController=new NotificationController(this);
        final ArrayList<Map<String, String>> reciveFromdatabase= notificationController.getValues(timestamp);
        for(int i=0;i< reciveFromdatabase.size();i++){
            NotificationData nDta = new NotificationData();
            nDta.setMsg(reciveFromdatabase.get(i).get("nplrmsg"));
            nDta.setNotId(reciveFromdatabase.get(i).get("nplrnotid"));
            nDta.setStatus(reciveFromdatabase.get(i).get("status"));
            notiarrayList.add(nDta);
            isDataPopulate = true;
        }
        try{
            if(isDataPopulate)
                adaptor.notifyDataSetChanged();}
        catch (Exception e){}

/*//        arrayNotificationData.clear();
       // LinearLayout linearLayout=(LinearLayout)findViewById(R.id.acpLinearLayout);
        notificationController=new NotificationController(this);
        *//*ArrayList<String> adpterData=notificationController.getValues();
         ArrayAdapter< String> adapter = new ArrayAdapter< String>(this,R.layout.notification_pannel_list_row, R.id.text1, adpterData);
         listview.setAdapter(adapter);*//*
        //arrayNotificationData= notificationController.getValues();
        final ArrayList<Map<String, String>> reciveFromdatabase= notificationController.getValues(timestamp);
       *//* for(int i=0;i< reciveFromdatabase.size();i++){
            arrayNotificationData.add(reciveFromdatabase.get(i));
        }*//*
      //  linearLayout.removeViewInLayout(listview);
        int[] to = { R.id.nplrnotid,R.id.nplrmsg};
        String[] from = {"nplrnotid","nplrmsg"};
        //CustomNotificationAdapter customNotificationAdapter=new CustomNotificationAdapter(this,reciveFromdatabase,R.layout.demo_order_product_list_retailer_row,R.id.productNameOnDemoRetailer,this,this);)
        // SimpleAdapter  simpleAdapter=new SimpleAdapter(this,reciveFromdatabase,R.layout.notification_pannel_list_row,from,to);//Create object and set the parameters for simpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, reciveFromdatabase,
                R.layout.notification_pannel_list_row, from,to){
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                System.out.println("MapValue"+position+"-"+convertView+"-"+parent);
                System.out.println("MapValue1"+reciveFromdatabase.get(position).get("nplrnotid"));
                String notid=reciveFromdatabase.get(position).get("nplrnotid");
                String status=notificationController.getStatus(notid);
                if(status.equalsIgnoreCase("True"))
                {
                    v.setBackgroundColor(Color.parseColor("#f0f0f0"));
                }
                else
                {
                    v.setBackgroundColor(Color.parseColor("#d8d8d8"));
                }
                return v;
            }

        };
      //  linearLayout.addView(listview);
        listview.setAdapter(simpleAdapter);

        if(progressDialog != null)
        {
            progressDialog.dismiss();
        }*/
    }

    /* private void navigatetoPage(String pageName) {
         GlobalPageName=pageName;
         new GetFormData().execute();

     }*/
    class GetFormData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(NotificationPannel.this,"Loading Data", "Loading...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            String result="";
            // TODO Auto-generated method stub
            try
            {
                result= getFormListData();
                // expenseGrpList=getExpenseGrpList();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if(progressDialog!=null)
            {
                progressDialog.dismiss();
            }
            if(result != null)
            {
                if(GlobalPageName.equalsIgnoreCase("LEAVEREQUEST"))
                {
                    boolean intentflag=false;
                    JSONArray jsonarray = null;
                    try {
                        jsonarray = new JSONArray(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String LVQRID="",noOfDays="",vdate="",fromdate="",todate="",reson="",remark="",status="",flag="",userid="",App_remark= "", AppStatus ="",aproval="false";

                    if(jsonarray != null){

                        for (int i = 0; i < jsonarray.length(); i++) {
                            try {
                                intentflag=true;
                                JSONObject obj = jsonarray.getJSONObject(i);
                                LVQRID =  obj.getString("LVRQId");
                                noOfDays=obj.getString("NoOfDays");
                                vdate=obj.getString("VDate");
                                fromdate=obj.getString("FromDate");
                                todate=obj.getString("ToDate");
                                reson=obj.getString("Reason");
                                status=obj.getString("AppStatus");
                                remark=obj.getString("AppRemark");
                                userid=obj.getString("SMId");
                                obj.getString("SMName");
                                flag=obj.getString("LeaveFlag");
                                App_remark=obj.getString("AppRemark");
                                AppStatus=obj.getString("AppStatus");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(intentflag){ finish();
                        Intent i=new Intent(NotificationPannel.this,AprovalLiveDetails.class);
                        i.putExtra("Data", "EditLeave");
                        i.putExtra("LEAVEDOCID_SESSION", LVQRID);
                        i.putExtra("Days", noOfDays);
                        i.putExtra("APPLICATION_DATE_SESSION",vdate);
                        i.putExtra("FROMDATE_SESSION", fromdate);
                        i.putExtra("TODATE_SESSION",todate);
                        i.putExtra("Reason", reson);
                        i.putExtra("Status",status);
                        i.putExtra("lEAVE_ANDROID_ID_SESSION",MOb);
                        i.putExtra("LEAVE_USER_ID_SESSION", userid);
                        i.putExtra("Flag",flag);
                        i.putExtra("DropId", userid);
                        i.putExtra("remark", remark);
                        i.putExtra("aproval",aproval);
                        i.putExtra("App_remark",App_remark);
                        i.putExtra("AppStatus",AppStatus);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
                        new Custom_Toast(getApplicationContext(),GlobalPageName+": NO DATA AVAILABLE , PLEASE CONTACT TO CRM DEPARTMENT", R.drawable.delete).showCustomAlert();
                    }


                }


                else  if(GlobalPageName.equalsIgnoreCase("LEAVEAPPROVED")  || GlobalPageName.equalsIgnoreCase("LEAVEREJECTED"))
                {
                    boolean intentflag=false;
                    JSONArray jsonarray = null;
                    try {
                        jsonarray = new JSONArray(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String LVQRID="",noOfDays="",vdate="",fromdate="",todate="",reson="",remark="",status="",flag="",userid="",App_remark= "", AppStatus ="",aproval="true";
                    if(jsonarray != null){

                        for (int i = 0; i < jsonarray.length(); i++) {
                            try {
                                intentflag=true;
                                JSONObject obj = jsonarray.getJSONObject(i);
                                LVQRID =  obj.getString("LVRQId");
                                noOfDays=obj.getString("NoOfDays");
                                vdate=obj.getString("VDate");
                                fromdate=obj.getString("FromDate");
                                todate=obj.getString("ToDate");
                                reson=obj.getString("Reason");
                                status=obj.getString("AppStatus");
                                remark=obj.getString("AppRemark");
                                userid=obj.getString("SMId");
                                obj.getString("SMName");
                                flag=obj.getString("LeaveFlag");
                                App_remark=obj.getString("AppRemark");
                                AppStatus=obj.getString("AppStatus");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(intentflag){
                        // finish();
                        Intent i=new Intent(NotificationPannel.this,AprovalLiveDetails.class);
                        i.putExtra("Data", "EditLeave");
                        i.putExtra("LEAVEDOCID_SESSION", LVQRID);
                        i.putExtra("Days", noOfDays);
                        i.putExtra("APPLICATION_DATE_SESSION",vdate);
                        i.putExtra("FROMDATE_SESSION", fromdate);
                        i.putExtra("TODATE_SESSION",todate);
                        i.putExtra("Reason", reson);
                        i.putExtra("Status",status);
                        i.putExtra("lEAVE_ANDROID_ID_SESSION",MOb);
                        i.putExtra("LEAVE_USER_ID_SESSION", SMID);
                        i.putExtra("Flag",flag);
                        i.putExtra("DropId", userid);
                        i.putExtra("remark", remark);
                        i.putExtra("aproval",aproval);
                        i.putExtra("App_remark",App_remark);
                        i.putExtra("AppStatus",AppStatus);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
                        new Custom_Toast(getApplicationContext(),GlobalPageName+": NO DATA AVAILABLE , PLEASE CONTACT TO CRM DEPARTMENT", R.drawable.delete).showCustomAlert();
                    }


                }
                else if(GlobalPageName.equalsIgnoreCase("LeaveRequest"))
                {
                    boolean intentflag=false;
                    JSONArray jsonarray = null;
                    try {
                        jsonarray = new JSONArray(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String LVQRID="",noOfDays="",vdate="",fromdate="",todate="",reson="",remark="",status="",flag="",userid="";
                    if(jsonarray != null){

                        for (int i = 0; i < jsonarray.length(); i++) {
                            try {
                                intentflag=true;
                                JSONObject obj = jsonarray.getJSONObject(i);
                                LVQRID =  obj.getString("LVRQId");
                                noOfDays=obj.getString("NoOfDays");
                                vdate=obj.getString("VDate");
                                fromdate=obj.getString("FromDate");
                                todate=obj.getString("ToDate");
                                reson=obj.getString("Reason");
                                status=obj.getString("AppStatus");
                                remark=obj.getString("AppRemark");
                                userid=obj.getString("SMId");
                                obj.getString("SMName");
                                flag=obj.getString("LeaveFlag");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(intentflag){
                        finish();
                        Intent i=new Intent(NotificationPannel.this,LeaveRequest.class);
                        i.putExtra("Data", "EditLeave");
                        i.putExtra("LEAVEDOCID_SESSION", LVQRID);
                        i.putExtra("Days", noOfDays);
                        i.putExtra("APPLICATION_DATE_SESSION",vdate);
                        i.putExtra("FROMDATE_SESSION", fromdate);
                        i.putExtra("TODATE_SESSION",todate);
                        i.putExtra("Reason", reson);
                        i.putExtra("Status",status);
                        i.putExtra("lEAVE_ANDROID_ID_SESSION",MOb);
                        i.putExtra("LEAVE_USER_ID_SESSION", SMID);
                        i.putExtra("Flag",flag);
                        i.putExtra("DropId", userid);
                        i.putExtra("remark", remark);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
                        new Custom_Toast(getApplicationContext(),GlobalPageName+": NO DATA AVAILABLE , PLEASE CONTACT TO CRM DEPARTMENT", R.drawable.delete).showCustomAlert();
                    }
                }
                else if(GlobalPageName.equalsIgnoreCase("BEATPLANREQUEST")){
                    JSONArray jsonarray = null;boolean intentflag=false;
                    try {
                        jsonarray = new JSONArray(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String docid = "", user = "", date = "", smid = "", status = "",aproval="false",App_remark= "", AppStatus ="";
                    if(jsonarray != null) {

                        for (int i = 0; i < jsonarray.length(); i++) {
                            try {
                                intentflag = true;
                                JSONObject obj = jsonarray.getJSONObject(i);
                                docid = obj.getString("DocId");
                                user = obj.getString("SMName");
                                date = obj.getString("StartDate");
                                smid = obj.getString("SMId");
                                status = obj.getString("AppStatus");
                                App_remark=obj.getString("AppRemark");
                                AppStatus=obj.getString("AppStatus");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(intentflag){
//                        finish();
//                        Intent i=new Intent(NotificationPannel.this,BeatAprovalEntry.class);
//                        i.putExtra("Data", "FindBeat");
//                        i.putExtra("Doc_id",docid);
//                        i.putExtra("User",user);
//                        i.putExtra("Date", date);
//                        i.putExtra("SmId",smid);
//                        i.putExtra("Status",status);
//                        i.putExtra("aproval",aproval);
//                        i.putExtra("App_remark",App_remark);
//                        i.putExtra("AppStatus",AppStatus);
//                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(i);
                    }else {
                        new Custom_Toast(getApplicationContext(),GlobalPageName+": NO DATA AVAILABLE , PLEASE CONTACT TO CRM DEPARTMENT", R.drawable.delete).showCustomAlert();
                    }

                }
                else if(GlobalPageName.equalsIgnoreCase("BEATAPPROVED") || GlobalPageName.equalsIgnoreCase("BEATREJECTED")){
                    JSONArray jsonarray = null;boolean intentflag=false;
                    try {
                        jsonarray = new JSONArray(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String docid = "", user = "", date = "", smid = "", status = "",aproval="true",App_remark= "", AppStatus ="";
                    if(jsonarray != null) {

                        for (int i = 0; i < jsonarray.length(); i++) {
                            try {
                                intentflag = true;
                                JSONObject obj = jsonarray.getJSONObject(i);
                                docid = obj.getString("DocId");
                                user = obj.getString("SMName");
                                date = obj.getString("StartDate");
                                smid = obj.getString("SMId");
                                status = obj.getString("AppStatus");
                                App_remark=obj.getString("AppRemark");
                                AppStatus=obj.getString("AppStatus");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(intentflag){
                        finish();
//                    Intent i=new Intent(NotificationPannel.this,BeatAprovalEntry.class);
//                    i.putExtra("Data", "FindBeat");
//                    i.putExtra("Doc_id",docid);
//                    i.putExtra("User",user);
//                    i.putExtra("Date", date);
//                    i.putExtra("SmId",smid);
//                    i.putExtra("Status",status);
//                    i.putExtra("aproval",aproval);
//                    i.putExtra("App_remark",App_remark);
//                    i.putExtra("AppStatus",AppStatus);
//                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(i);
                    }else {
                        new Custom_Toast(getApplicationContext(),GlobalPageName+": NO DATA AVAILABLE , PLEASE CONTACT TO CRM DEPARTMENT", R.drawable.delete).showCustomAlert();
                    }

                }
                else if(GlobalPageName.equalsIgnoreCase("TOURPLANREQUEST")) {
                    JSONArray jsonarray = null;boolean intentflag=false;
                    try {
                        jsonarray = new JSONArray(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String docid = "", user = "", date = "", smid = "", status = "",aproval="false",App_remark= "", AppStatus ="";
                    if(jsonarray != null) {

                        for (int i = 0; i < jsonarray.length(); i++) {
                            try {
                                intentflag = true;
                                JSONObject obj = jsonarray.getJSONObject(i);
                                App_remark=obj.getString("AppRemark");
                                AppStatus=obj.getString("AppStatus");
                                smid=obj.getString("SMId");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(intentflag){
                        finish();
                        Intent i=new Intent(NotificationPannel.this,TourPlanAproval.class);
                        i.putExtra("DocId", DocIDOrLVQRID);
                        i.putExtra("setID",smid);
                        i.putExtra("FromFind",true);
                        i.putExtra("aproval",aproval);
                        i.putExtra("App_remark",App_remark);
                        i.putExtra("AppStatus",AppStatus);
                        i.putExtra("Notification","Notification");
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }else {
                        new Custom_Toast(getApplicationContext(),GlobalPageName+": NO DATA AVAILABLE , PLEASE CONTACT TO CRM DEPARTMENT", R.drawable.delete).showCustomAlert();
                    }
                }
                else if(GlobalPageName.equalsIgnoreCase("TOURPLANAPPROVED") || GlobalPageName.equalsIgnoreCase("TOURPLANREJECTED")){
                    JSONArray jsonarray = null;boolean intentflag=false;
                    try {
                        jsonarray = new JSONArray(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String docid = "", user = "", date = "", smid = "", status = "",aproval="true",App_remark= "", AppStatus ="";
                    if(jsonarray != null) {

                        for (int i = 0; i < jsonarray.length(); i++) {
                            try {
                                intentflag = true;
                                JSONObject obj = jsonarray.getJSONObject(i);
                                App_remark=obj.getString("AppRemark");
                                AppStatus=obj.getString("AppStatus");
                                smid=obj.getString("SMId");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(intentflag){
                        finish();
                        Intent i=new Intent(NotificationPannel.this,TourPlanAproval.class);
                        i.putExtra("DocId", DocIDOrLVQRID);
                        i.putExtra("setID",smid);
                        i.putExtra("FromFind",true);
                        i.putExtra("aproval",aproval);
                        i.putExtra("App_remark",App_remark);
                        i.putExtra("AppStatus",AppStatus);
                        i.putExtra("Notification","Notification");
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    }
                    else {
                        new Custom_Toast(getApplicationContext(),GlobalPageName+": NO DATA AVAILABLE , PLEASE CONTACT TO CRM DEPARTMENT", R.drawable.delete).showCustomAlert();
                    }
                }
                else if(GlobalPageName.equalsIgnoreCase("CRMTASK")) {
                    JSONArray jsonarray = null;boolean intentflag=false;
                    String contactID="",Flag="",Lead="";
                    try {
                        jsonarray = new JSONArray(result);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(jsonarray != null) {

                        for (int i = 0; i < jsonarray.length(); i++) {
                            try {
                                intentflag = true;
                                JSONObject obj = jsonarray.getJSONObject(i);
                                contactID=obj.getString("contactid");
                                Flag=obj.getString("Flag");
                                Lead=obj.getString("Lead");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if(intentflag){
                        Intent intent=new Intent(NotificationPannel.this,CRMStreamInfo.class);
                        intent.putExtra("Contact_id",contactID);
                        intent.putExtra("Lead",Lead);
                        intent.putExtra("Flag",Flag);
                        CRMstream.Flag = Flag;
                        startActivity(intent);
                    }
                    else {
                        new Custom_Toast(getApplicationContext(),GlobalPageName+": NO DATA AVAILABLE , PLEASE CONTACT TO CRM DEPARTMENT", R.drawable.delete).showCustomAlert();
                    }

                }
                else {
                    new Custom_Toast(getApplicationContext(), "Contact Admin"+GlobalPageName, R.drawable.delete).showCustomAlert();
                }
            }


        }
    }
    public  void reduceCount(){
        DashBoradActivity.notificationCount=DashBoradActivity.notificationCount-1;
        Long tsLong = System.currentTimeMillis()/1000;
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_NOTIFICATION,DashBoradActivity.notificationCount);
        values.put(DatabaseConnection.COLUMN_DATE,tsLong.toString());
        DbCon dbCon=new DbCon(NotificationPannel.this);
        dbCon.open();
        dbCon.truncate(DatabaseConnection.NOTIFIVATION_TABLE);
        dbCon.insert(DatabaseConnection.NOTIFIVATION_TABLE,values);
        dbCon.close();
    }
    /*   public String getFormListData(){
           String url="";String response1="";
           if(GlobalPageName.equalsIgnoreCase("LeaveRequest") || GlobalPageName.equalsIgnoreCase("LeaveApproval"))
           {
               url="http://"+server+"/And_Sync.asmx/xJSFindLeaveDetailsForNotification";
           }
           else if(GlobalPageName.equalsIgnoreCase("BeatPlanEntry") || GlobalPageName.equalsIgnoreCase("BeatPlanApproval")){
               url="http://"+server+"/And_Sync.asmx/xJSGetBeatPlanByUserForApprovalForNotification";
           }
           else if(GlobalPageName.equalsIgnoreCase("TourPlan")||GlobalPageName.equalsIgnoreCase("TourPlanApproval")){
               url="http://"+server+"/And_Sync.asmx/xJSGetDataforTourUpdateChild";
           }
           JSONParser jParser = new JSONParser();
           String result = jParser.getJSONArray(url);
           System.out.println("NotificationPage"+url+"-"+result);
           HttpClient httpclient = new DefaultHttpClient();
           HttpPost httppost = new HttpPost(url);
           ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
           nameValuePairs.add(new BasicNameValuePair("id", DocIDOrLVQRID));
           nameValuePairs.add(new BasicNameValuePair("notiid",Notid));
           try {
               httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
           } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
           }
           ResponseHandler<String> responseHandler = new BasicResponseHandler();
           try {
               response1 = httpclient.execute(httppost, responseHandler);
           } catch (IOException e) {
               e.printStackTrace();
           }
           System.out.println("NotificationPage"+url+"-"+DocIDOrLVQRID+"-"+Notid);
           return  response1;
       }*/
    public String getFormListData(){
        String url="";String result=null;

        if(GlobalPageName.equalsIgnoreCase("LEAVEREQUEST") || GlobalPageName.equalsIgnoreCase("LEAVEAPPROVED") || GlobalPageName.equalsIgnoreCase("LEAVEREJECTED"))
        {
            url="http://"+server+"/And_Sync.asmx/xJSFindLeaveDetailsForNotification?id="+DocIDOrLVQRID+"&notiid="+Notid;
        }
        else if(GlobalPageName.equalsIgnoreCase("BEATPLANREQUEST") || GlobalPageName.equalsIgnoreCase("BEATAPPROVED") || GlobalPageName.equalsIgnoreCase("BEATREJECTED")){
            url="http://"+server+"/And_Sync.asmx/xJSGetBeatPlanByUserForApprovalForNotification?Docid="+DocIDOrLVQRID+""+"&notiid="+Notid ;
            url=url.replaceAll(" ","%20");
        }
        else if(GlobalPageName.equalsIgnoreCase("TOURPLANREQUEST")||GlobalPageName.equalsIgnoreCase("TOURPLANAPPROVED") || GlobalPageName.equalsIgnoreCase("TOURPLANREJECTED")){
            url="http://"+server+"/And_Sync.asmx/xJSGetTourPlanByUserForApprovalForNotification?Docid="+DocIDOrLVQRID+""+"&notiid="+Notid ;
            //url="http://"+server+"/And_Sync.asmx/xJSGetDataforTourUpdateChild?docid="+DocIDOrLVQRID+""+"&notiid="+Notid ;
            url=url.replaceAll(" ","%20");
        }
        else if(GlobalPageName.equalsIgnoreCase("CRMTASK")){
            url="http://"+server+"/And_Sync.asmx/XjGetActionNotification_CRM?Docid="+DocIDOrLVQRID+"&notiid="+Notid ;
            //url="http://"+server+"/And_Sync.asmx/xJSGetDataforTourUpdateChild?docid="+DocIDOrLVQRID+""+"&notiid="+Notid ;
            url=url.replaceAll(" ","%20");
        }
        Log.e("NotificationPage",url);
        JSONParser jParser = new JSONParser();
        try {
            System.out.println("");
            result = jParser.getJSONArray(url);

            System.out.println("NotificationPage"+url+"-"+result);
        }
        catch (Exception e){
            System.out.println(e);
        }
        return  result;
    }
    String id="";
    public void getNotificationData(String smid,String timestamp){
        id=smid;
        String url="http://"+server+"/And_Sync.asmx/xJSGetNotificationInDetail?Smid="+smid+"&minDate="+timestamp ;
        JSONParser jParser = new JSONParser();
        ArrayList<Expense> expenseList = new ArrayList<Expense>();
        String result = jParser.getJSONArray(url);
        System.out.println("NotificationPannel"+url+"-"+result);
        if (result != null) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length()>0)
                {
                    isMoreData = true;
                }
                else
                {
                    isMoreData = false;
                }
                for (int i = 0; i < jsonarray.length(); i++)
                {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    ContentValues values = new ContentValues();
                    values.put(DatabaseConnection.COLUMN_NOTIFICATION,obj.getString("notiid"));
                    values.put(DatabaseConnection.COLUMN_pro_id,obj.getString("pro_id"));
                    values.put(DatabaseConnection.COLUMN_userid,obj.getString("userid"));
                    values.put(DatabaseConnection.COLUMN_TIMESTAMP,obj.getString("Miliseconds"));
                    values.put(DatabaseConnection.COLUMN_url,obj.getString("url"));
                    values.put(DatabaseConnection.COLUMN_msz,obj.getString("msz"));
                    values.put(DatabaseConnection.COLUMN_STATUS,obj.getString("status"));
                    String urlData=obj.getString("url");
                    String Page,SMID,LVQR;
                    if(obj.getString("pro_id").equalsIgnoreCase("CRMTASK")){
                        int positionaspxPage =urlData.indexOf("?");
                        int positionofLQVR =urlData.indexOf("=");
                        Page=urlData.substring(0,(positionaspxPage-5));
                        SMID="";
                        LVQR=urlData.substring(positionofLQVR,urlData.length());
                        LVQR=LVQR.replaceAll("=", "");
                    }
                    else{
                        int positionaspxPage =urlData.indexOf("?");
                        int positionofLQVR =urlData.indexOf("&");
                        Page=urlData.substring(0,(positionaspxPage-5));
                        SMID=urlData.substring(positionaspxPage+6,(positionofLQVR));
                        LVQR=urlData.substring(positionofLQVR+7);
                        LVQR=LVQR.replaceAll("=", "");
                    }

                    values.put(DatabaseConnection.COLUMN_URL_Page,Page);
                    values.put(DatabaseConnection.COLUMN_Smid,SMID);
                    values.put(DatabaseConnection.COLUMN_LVQR,LVQR);
                    // System.out.println("SMID AND UNIQUE ID:" + Page+"-"+SMID+"-"+LVQR);
                    DbCon dbCon=new DbCon(this);
                    dbCon.open();
                    dbCon.insert(DatabaseConnection.NOTIFIVATION_Data_TABLE,values);
                    dbCon.close();

                }
                // System.out.println("jsonarray"+jsonarray);
            }catch (Exception e)
            {
                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }

                System.out.println(e);
//                mSuccess=true;
            }
        }
        if(!timestamp.equalsIgnoreCase("0"))
        {
            setDataOnListView(timestamp);
        }
    }
    @Override
    public void onPause()
    {
        super.onPause();
        connectionDetector=new ConnectionDetector(this);
        if(!connectionDetector.isConnectingToInternet()){
            dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

    }
    @Override
    public void onResume()
    {
        super.onResume();
        connectionDetector=new ConnectionDetector(this);
        if (connectionDetector.isConnectingToInternet())
        {
            timestamp="0";
            DbCon dbCon=new DbCon(getApplicationContext());
            dbCon.open();
            dbCon.truncate(DatabaseConnection.NOTIFIVATION_Data_TABLE);
            //dbCon.truncate(DatabaseConnection.NOTIFIVATION_TABLE);
            dbCon.close();
            AppData appData;
            AppDataController appDataController1=new AppDataController(NotificationPannel.this);
            appDataController1.open();
            appDataArray = appDataController1.getAppTypeFromDb();
            appDataController1.close();
            appData = appDataArray.get(0);
            server=appData.getCompanyUrl();
            new GetListNotificationData().execute();

        }
        else
        {

            dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

    }
    @Override
    public void onStop()
    {
        super.onStop();
        connectionDetector=new ConnectionDetector(this);
        if(!connectionDetector.isConnectingToInternet()){
            dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        DbCon dbCon=new DbCon(this);
        dbCon.open();
        //dbCon.truncate(DatabaseConnection.NOTIFIVATION_Data_TABLE);
        dbCon.close();

    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        finish();
        DbCon dbCon=new DbCon(this);
        dbCon.open();
        //  dbCon.truncate(DatabaseConnection.NOTIFIVATION_Data_TABLE);
        dbCon.close();
        Intent i = new Intent( NotificationPannel.this,DashBoradActivity.class);
        startActivity(i);
    }
}

