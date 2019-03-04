package com.dm.crmdm_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomTourPlanAprovalArrayAdapter;
import com.dm.library.Custom_Toast;
import com.dm.library.ExceptionData;
import com.dm.library.IntentSend;
import com.dm.library.StateDropDownListAdapter;
import com.dm.model.AppData;
import com.dm.model.DashboardModel;
import com.dm.model.TourPlan;
import com.dm.parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class TourApproval extends AppCompatActivity implements CustomTourPlanAprovalArrayAdapter.HolderListener{
    SharedPreferences preferences2;
    ArrayList<TourPlan> tourList;
    String  server,MOb,SmId, UserId, SolidArea, Smid ;
    ProgressDialog progressDialog;
    ArrayList<String> salesManNameList;
    ArrayList<String> salesManNameList2 = new ArrayList<String>();
    ArrayList<String> salesManNameList3 = new ArrayList<String>();
    Spinner salesManSpinner;
    Button gobtn;
    ListView listView;
    DashboardModel model;
    ArrayList<DashboardModel> dashboardModels;
    CustomTourPlanAprovalArrayAdapter customTourPlanAprovalArrayAdapter;
    TextView timespendtextview;
    LinearLayout traDSR;
    String salespersonidinString="";
    ArrayAdapter<String> areaadapter,beatadapter, beatadapter1, beatadapter2, beatadapter3, beatadapter4, beatadapter5, beatadapter6, beatadapter7, weekOffadapter, smanAdapter;
    ConnectionDetector connectionDetector;ExceptionData exceptionData;AlertOkDialog dialogWithOutView;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    String getdatafrom_notification = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour_approval);

        getSupportActionBar().setCustomView(R.layout.actionbar_custom_view_home);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageView iv = (ImageView)findViewById(R.id.image);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new IntentSend(getApplicationContext(),DashBoradActivity.class)).toSendAcivity();
                finish();
            }
        });
        TextView tv = (TextView)findViewById(R.id.text);
        tv.setText("Tour Approval");
        preferences2 = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        connectionDetector=new ConnectionDetector(getApplicationContext());
        SmId = preferences2.getString("CONPERID_SESSION", null);
        UserId = preferences2.getString("USER_ID", null);
        salesManNameList = new ArrayList<String>();
        salesManSpinner = (Spinner) findViewById(R.id.salesPerson);
        gobtn = (Button) findViewById(R.id.fillBtn);
        listView = (ListView)findViewById(R.id.Leaveaprovallist);
        timespendtextview = (TextView)findViewById(R.id.Salestextview);
        timespendtextview.setVisibility(View.VISIBLE);
        traDSR = (LinearLayout)findViewById(R.id.salesL);

        appDataController1=new AppDataController(TourApproval.this);
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();

        salesManNameList.add("--Select--");
        salesManNameList2.add("--Select--");

        if (connectionDetector.isConnectingToInternet())
        {
            new getLevel().execute();
        }
        else{
            dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }
        salesManSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SolidArea = salesManNameList2.get((int) salesManSpinner.getSelectedItemId());
                //new Custom_Toast(getApplicationContext(), SolidArea).showCustomAlert();

                String jk=timespendtextview.getText().toString();
                if(!jk.equalsIgnoreCase("")){
                    Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    timespendtextview.setVisibility(View.VISIBLE);
                    timespendtextview.startAnimation(slide_up);
                    timespendtextview.setTextColor(Color.parseColor("#FF4081"));
                    traDSR.setBackgroundColor(Color.parseColor("#FF4081"));
                    // listView.setVisibility(View.GONE);
                }
                timespendtextview.setText("Sales Person:");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                timespendtextview.setVisibility(View.GONE);
            }
        });
        /*LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_leave_list, listView, false);
        listView.addHeaderView(header, null, false);*/
        gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet())
                {
                    try {
                        new GetTourAprovalList().execute();
                    }
                    catch (Exception e){}
                }
                else{
                    dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }
            }
        });
    }

    @Override
    public void holderListener(CustomTourPlanAprovalArrayAdapter.MyHolder myHolders) {

    }


    class GetTourAprovalList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TourApproval.this, "Loading Data", "Loading Please wait...", true);

            if(salesManSpinner.getSelectedItem().equals("--Select--")){
                salespersonidinString = "";
                for (int i=1; i<salesManNameList2.size();i++) {
                    String salespersonid = (salesManNameList2.get(i));
                    salespersonidinString = salespersonidinString + salespersonid+",";
                }
                salespersonidinString = (StateDropDownListAdapter.trimEnd(salespersonidinString.toString().trim(), ','));
            }
            else {
                salespersonidinString = SolidArea;
            }
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                tourList = getTourAprovalList();

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

            if (tourList != null) {
                customTourPlanAprovalArrayAdapter =new CustomTourPlanAprovalArrayAdapter(TourApproval.this,feedDahboardItemData(tourList), R.layout.find_tour_plan_list_row, R.id.tourdocid,TourApproval.this, getdatafrom_notification);
                listView.setAdapter(customTourPlanAprovalArrayAdapter);
                listView.setClickable(true);
                if(customTourPlanAprovalArrayAdapter.getCount()>0)
                {
                    listView.setVisibility(View.VISIBLE);

                }
                else{
                    listView.setVisibility(View.GONE);
                    new Custom_Toast(getApplicationContext(), "Data Not Found").showCustomAlert();
                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        }
    }
    private ArrayList<DashboardModel> feedDahboardItemData(ArrayList<TourPlan> tourList1)
    {
        ArrayList<DashboardModel> dashboardModels=new ArrayList<DashboardModel>();
        DashboardModel model=new DashboardModel();
        for (int i = 0; i < tourList1.size(); i++) {
            dashboardModels.add(model.FindTourPlanDateWiseModel(tourList1.get(i).getVDate(),tourList1.get(i).getDocId(),tourList1.get(i).getAppBySMId(),tourList1.get(i).getSMId(),tourList1.get(i).getAppStatus(),tourList1.get(i).getAppRemark()));
        }

        return dashboardModels;

    }
    public ArrayList<TourPlan> getTourAprovalList()
    {
        ArrayList<TourPlan> tourList=new ArrayList<TourPlan>();
        JSONParser jParser = new JSONParser();
        if(connectionDetector.isConnectingToInternet()){
            try {
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSFindTourPlanApproval?smid="+ salespersonidinString));
                for (int i = 0; i < jsonarray.length(); i++) {
                    TourPlan tourPlan=new TourPlan();
                    tourPlan.setVDate(jsonarray.getJSONObject(i).getString("dt"));
                    tourPlan.setDocId(jsonarray.getJSONObject(i).getString("dcid"));
                    tourPlan.setAppBySMId(jsonarray.getJSONObject(i).getString("slnm"));
                    tourPlan.setSMId(jsonarray.getJSONObject(i).getString("SMID"));
                    tourPlan.setAppStatus(jsonarray.getJSONObject(i).getString("st"));
                    tourPlan.setAppRemark(jsonarray.getJSONObject(i).getString("AppRemark"));
                    tourList.add(tourPlan);
                }
                jsonarray=null;

            }catch (Exception e)
            {

                System.out.println(e);

            }
        }

        return tourList;
    }

    protected class getLevel extends AsyncTask<Void, Void, JSONArray> {
        ProgressDialog pdLoading = new ProgressDialog(TourApproval.this);
        @Override
        protected void onPreExecute() {
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected JSONArray doInBackground(Void... params) {
            // server = Constant.SERVER_WEBSERVICE_URL;
            MOb = preferences2.getString("PDAID_SESSION", "");
            //String str = "http://" + server + "/And_Sync.asmx/xJSGetUserForApproval?PDA_Id=" + MOb + "&minDate=0&Smid=" + SmId;
            String str = "http://" + server + "/And_Sync.asmx/xJSGetUserForApproval?Smid=" + SmId;
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
                //JSONObject response = new JSONObject(stringBuffer.toString());
                JSONArray response = new JSONArray(stringBuffer.toString());
                //JSONObject obj = response.getJSONObject(0);
                return new JSONArray(response.toString());
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

        @Override
        protected void onPostExecute(JSONArray response) {
            if (response != null) {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject objs = null;
                        objs = response.getJSONObject(i);
                        salesManNameList.add(objs.getString("snm").toString());
                        salesManNameList2.add(objs.getString("sid").toString());
                        // salesManNameList3.add(objs.getString("UserId").toString());
                    }
                    try {
                        int id = (salesManNameList2.indexOf(SmId));
                        salesManNameList.remove(id);
                        salesManNameList2.remove(id);
                    }
                    catch (Exception e){
                        System.out.println(e);
                    }

                    //Level = response.getJSONObject(response.length() - 1).getString("RoleName").toString();
                    smanAdapter = new ArrayAdapter<String>(TourApproval.this, R.layout.adapterdropdown, salesManNameList);
                    smanAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    smanAdapter.notifyDataSetChanged();
                    salesManSpinner.setAdapter(smanAdapter);

                   /* salesManSpinner.setSelection(salesManNameList2.indexOf(SmId));
                    if(response.length()==1){
                        salesManSpinner.setClickable(false);
                        salesManSpinner.setEnabled(false);
                        //linear_Sales.setVisibility(View.GONE);
                    }
                    else {
                        salesManSpinner.setClickable(true);
                        salesManSpinner.setEnabled(true);
                    }*/
                    //Log.e("App", "Success: " + response.getString("yourJsonElement") );
                } catch (JSONException ex) {
                    Log.e("App", "Failure", ex);
                }
				/*if(IsSearch==true){}
				else {
				new getAreaList().execute();
				}*/
            }
            super.onPostExecute(response);
            pdLoading.dismiss();
        }
    }


    @Override
    public void onBackPressed() {
        (new IntentSend(getApplicationContext(), DashBoradActivity.class)).toSendAcivity();
        finish();
    }
}
