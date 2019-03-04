package com.dm.crmdm_app;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
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
import com.dm.library.AlertMessage;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomArrayAdapterLeaveAprovalList;
import com.dm.library.Custom_Toast;
import com.dm.library.ExceptionData;
import com.dm.library.IntentSend;
import com.dm.model.AppData;
import com.dm.model.DashboardModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class LeaveApproval extends AppCompatActivity implements
               AlertMessage.NoticeDialogListenerWithoutView,
               CustomArrayAdapterLeaveAprovalList.FindLeaveRequestTransactionListener,
               CustomArrayAdapterLeaveAprovalList.HolderListener {
    SharedPreferences preferences2;
    String  server, Level,MOb, dropdownId,SmId, UserId, SolidArea,SolidArea1,SolidArea2,SolidArea3,SolidArea4,SolidArea5,SolidArea6,
            UniqNo, UniqBeatNo,UniqDate,UniqDay, dcid, BeatIds, AreaIds, StartDate,
            FindArea, Smid, UpdateDate, IntData = "null";
    ArrayList<String> salesManNameList;
    ArrayList<String> salesManNameList2 = new ArrayList<String>();
    ArrayList<String> salesManNameList3 = new ArrayList<String>();
    Spinner salesManSpinner;
    Button gobtn;
    ListView listView;
    DashboardModel model;
    ArrayList<DashboardModel> dashboardModels;
    CustomArrayAdapterLeaveAprovalList customArrayAdapterLeaveAprovalList;
    TextView timespendtextview;
    LinearLayout traDSR;
    ArrayAdapter<String> areaadapter,beatadapter, beatadapter1, beatadapter2, beatadapter3, beatadapter4, beatadapter5, beatadapter6, beatadapter7, weekOffadapter, smanAdapter;
    ConnectionDetector connectionDetector;ExceptionData exceptionData;AlertOkDialog dialogWithOutView;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_approval);

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
        tv.setText("Leave Approval List");
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

        appDataController1=new AppDataController(LeaveApproval.this);
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

                    new findLeaveRequest().execute();
                }
                else{
                    dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }
            }
        });
    }

    @Override
    public void onDialogPositiveWithoutViewClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeWithoutViewClick(DialogFragment dialog) {

    }

    @Override
    public void holderListener(ArrayList<LeaveRequest> leaveRequestList) {

    }

    @Override
    public void holderListener(CustomArrayAdapterLeaveAprovalList.MyHolder myHolders) {

    }


    protected class getLevel extends AsyncTask<Void, Void, JSONArray> {
        ProgressDialog pdLoading = new ProgressDialog(LeaveApproval.this);
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
                    int id = (salesManNameList2.indexOf(SmId));
                   // new Custom_Toast(getApplicationContext(),String.valueOf(id)).showCustomAlert();
                    salesManNameList.remove(id);
                    //Level = response.getJSONObject(response.length() - 1).getString("RoleName").toString();
                    smanAdapter = new ArrayAdapter<String>(LeaveApproval.this, R.layout.adapterdropdown, salesManNameList);
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
    protected class findLeaveRequest extends AsyncTask<Void, Void, ArrayList<DashboardModel>> {
        ProgressDialog pdLoading = new ProgressDialog(LeaveApproval.this);
        String select="Single";
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPreExecute() {
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
            try {
                if (SolidArea.equals("--Select--")) {
                    SmId = preferences2.getString("CONPERID_SESSION", null);
                    select = "select";
                } else {
                    SmId = SolidArea;
                }
                //String jjk=salesManNameList2.get((int) salesManMaterial.getSelectedItemId());
                //Rmns= salesManMaterial.getSelectedItemId();
                //SmId=jjk;
                dropdownId = String.valueOf(salesManSpinner.getSelectedItemId());
                //new Custom_Toast(getApplicationContext(), dropdownId).showCustomAlert();
            }
            catch (Exception e){}
        }
        @Override
        protected ArrayList<DashboardModel> doInBackground(Void... params) {
          //  server= Constant.SERVER_WEBSERVICE_URL;
            model = new DashboardModel();
            String str = "http://" + server + "/And_Sync.asmx/xJSGetAllLeaveByUserForApproval?Smid=" + SmId + "&selectiontype="+select ;
            Log.e("findLeaveRequest", str);
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try {
                dashboardModels = new ArrayList<DashboardModel>();
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                JSONArray response = new JSONArray(stringBuffer.toString());

                for (int i = 0; i < response.length(); i++) {
                    JSONObject objs = null;
                    try {
                        objs = response.getJSONObject(i);
                       /* String dfg=(objs.getString("FromDate").toString());
                        String tfg=(objs.getString("ToDate").toString());
                        String tfh=(objs.getString("VDate").toString());

                        dashboardModels.add(model.aprovalLeaveRequestListModel(
                                objs.getString("leaveflag").toString() ,
                                MOb,
                                SolidArea,
                                tfh,
                                objs.getString("NoOfDays").toString(),
                                dfg,
                                tfg,
                                objs.getString("Reason").toString(),
                                objs.getString("AppStatus").toString(),
                                objs.getString("LVRQId").toString(),
                                dropdownId));
                    } */
                    String dfg=(objs.getString("FromDate").toString());
                    String tfg=(objs.getString("ToDate").toString());
                    String tfh=(objs.getString("VDate").toString());
                    dashboardModels.add(model.aprovalLeaveRequestListModel(
                            objs.getString("leaveflag").toString(),
                            MOb,
                            SmId,
                            tfh,
                            objs.getString("NoOfDays").toString(),
                            dfg,
                            tfg,
                            objs.getString("SMName").toString(),
                            objs.getString("Reason").toString(),
                            objs.getString("AppRemark").toString(),
                            objs.getString("AppStatus").toString(),
                            objs.getString("LVRQId").toString(),
                            objs.getString("LVRDocId").toString(),
                            objs.getString("SMId").toString(),
                            dropdownId));

                } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                return dashboardModels;
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
        protected void onPostExecute(ArrayList<DashboardModel> response) {

            if (response != null) {
                if(response.size()>0) {
                    listView.setVisibility(View.GONE);
                    customArrayAdapterLeaveAprovalList = new CustomArrayAdapterLeaveAprovalList(
                            getApplicationContext(), response, R.layout.find_leave_request_list_row,
                            R.id.apptextdate, LeaveApproval.this, LeaveApproval.this);
                    listView.setAdapter(customArrayAdapterLeaveAprovalList);
                    listView.setClickable(true);
                    listView.setVisibility(View.VISIBLE);
                    Log.e("App", response.toString());
                }else{
                    listView.setVisibility(View.GONE);
                    new Custom_Toast(getApplicationContext(), "No Leave Plan Available.").showCustomAlert();
                }
            }else{
                listView.setVisibility(View.GONE);
                new Custom_Toast(getApplicationContext(), "Please Select Sales Person").showCustomAlert();
            }
            super.onPostExecute(response);
            pdLoading.dismiss();
        }
    }
    @Override
    public void onRestart() {
        super.onRestart();
      //  customArrayAdapterLeaveAprovalList = new CustomArrayAdapterLeaveAprovalList(
        //         getApplicationContext(), response, R.layout.find_leave_request_list_row,
        //         R.id.apptextdate, LeaveApproval.this, LeaveApproval.this);

      customArrayAdapterLeaveAprovalList.clear();
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(LeaveApproval.this, DashBoradActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}
