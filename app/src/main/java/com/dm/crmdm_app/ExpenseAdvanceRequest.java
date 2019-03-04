package com.dm.crmdm_app;

import android.annotation.TargetApi;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dm.library.AlertMessage;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.Constant;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.library.DateFunction;
import com.dm.library.IntentSend;
import com.dm.model.City;
import com.dm.model.DashboardModel;
import com.dm.model.ExpenseEntry;
import com.dm.model.State;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ExpenseAdvanceRequest extends ActionBarActivity implements OnClickListener {
    ConnectionDetector connectionDetector;
    ArrayList<State> stateList;
    EditText fromDate,fromTime,toDate,toTime,earamount,earremark;
    ImageView save,cancel,delete;
    Spinner spstate,spcity;AlertOkDialog dialogWithOutView;String server;
    String id,smid;String stId="0"; DashboardModel model;
    ArrayList<DashboardModel> dashboardModels;
    ArrayList<String> stateArrayList,cityArrayList;
    ArrayAdapter<String> stateAdapter,cityAdapter;
    AlertMessage alertMessage;
    SharedPreferences preferences;
    ProgressDialog progressDialog;
    ArrayList<City> cityList;
    ExpenseEntry expenseEntry;
    LinearLayout l1;TextView tv1;String userId;
    boolean flag=false;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_advance_request);
        getSupportActionBar().setCustomView(R.layout.actionbar_custom_view_home);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageView iv = (ImageView)findViewById(R.id.image);

        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                (new IntentSend(getApplicationContext(),DashBoradActivity.class)).toSendAcivity();
                finish();
            }
        });
        TextView tv = (TextView)findViewById(R.id.text);
        tv.setText("Expense Advance Request");
        setTitle("Expense Advance Request");
        connectionDetector=new ConnectionDetector(getApplicationContext());
        fromDate=(EditText)findViewById(R.id.earfromdate);
        fromTime=(EditText)findViewById(R.id.earfromtime);
        toDate=(EditText)findViewById(R.id.eartodate);
        toTime=(EditText)findViewById(R.id.eartotime);
        earamount=(EditText)findViewById(R.id.earamount);
        fromDate.setInputType(InputType.TYPE_NULL);
        fromDate.setFocusable(false);
        fromTime.setInputType(InputType.TYPE_NULL);
        fromTime.setFocusable(false);
        toDate.setInputType(InputType.TYPE_NULL);
        toDate.setFocusable(false);
        toTime.setInputType(InputType.TYPE_NULL);
        toTime.setFocusable(false);
        earremark=(EditText)findViewById(R.id.earremark);
        spstate=(Spinner)findViewById(R.id.earspstate);
        spcity=(Spinner)findViewById(R.id.earspcity);
        save=(ImageView)findViewById(R.id.save);
        cancel=(ImageView)findViewById(R.id.cancel);
        delete=(ImageView)findViewById(R.id.delete);
        save.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);
        l1 = (LinearLayout)findViewById(R.id.lin);
        tv1=(TextView)findViewById(R.id.reasontext);
        tv1.setVisibility(View.INVISIBLE);
        Intent i=getIntent();
       /* String stfdate=i.getStringExtra("TravelFromdate");
        String stftime=i.getStringExtra("TravelFromTime");
        String sttdate=i.getStringExtra("TravelTodate");
        String stttime=i.getStringExtra("TravelToTime");*/
        String stdocid=i.getStringExtra("Docid");
        preferences=ExpenseAdvanceRequest.this.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        userId=preferences.getString("USER_ID", "0");
        smid=preferences.getString("CONPERID_SESSION", "0");
        id=i.getStringExtra("ID");
        System.out.println("Intent ID"+id+"-"+smid);
        if (connectionDetector.isConnectingToInternet())
        {
            System.out.println("Inside Main Index");
            new GetStates().execute();

        }
        else{
            dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }
        fromDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = ExpenseAdvanceRequest.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker(view.getId());
            }
        });
        toDate.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = ExpenseAdvanceRequest.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker(view.getId());
            }
        });
        fromTime.setOnClickListener(this);
        toTime.setOnClickListener(this);
       /* *//*fromTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ExpenseAdvanceRequest.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        fromTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });*//*
        TimePickerDialog.OnTimeSetListener onStartTimeListener = new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String AM_PM ;
                if(hourOfDay < 12) {
                    AM_PM = "AM";
                } else {
                    AM_PM = "PM";
                }

                fromTime.setText(hourOfDay + " : " + minute + " " + AM_PM );
            }
        };
        toTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ExpenseAdvanceRequest.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        toTime.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });*/
        spstate.setOnItemSelectedListener(new OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id)
            {

                if(spstate.getSelectedItem().toString().equalsIgnoreCase("Select State"))
                {
                    System.out.println("Inside First Index");
                    spstate.setSelection(0);

                }
                else{
                    if (connectionDetector.isConnectingToInternet())

                    {
                        System.out.println("Inside Index");

                        new GetCityList().execute();

                    }
                    else{

                        dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                        dialogWithOutView.show(getFragmentManager(), "Info");
                    }

                }

            }



            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }
        });

        spstate.performClick();
        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionDetector.isConnectingToInternet())
                {

                   expenseEntry = new ExpenseEntry();
                     /* expenseEntry.setExpCode(expId);
                      cityPreferences=getSharedPreferences("MyExpCity",Context.MODE_PRIVATE);
                     statePreferences=getSharedPreferences("MyExpState",Context.MODE_PRIVATE);*/
                     int stid=0,ctid=0;
                     for(int j=0;j<stateList.size();j++)
                      {
                       System.out.println("Save State"+stateList.get(j).getState_name());
                          //  System.out.println("StateListData"+stateList.get(j).getState_id()+"-"+stateList.get(j).getState_name());
                          if(stateList.get(j).getState_name().equals(spstate.getSelectedItem().toString()))
                          {
                              stid=j;
                              System.out.println("InSideselectIndex"+stid);
                          }
                      }
                    for(int j=0;j<cityList.size();j++)
                    {
                        System.out.println("Save City"+cityList.get(j).getCity_name());
                                                //  System.out.println("StateListData"+stateList.get(j).getState_id()+"-"+stateList.get(j).getState_name());
                        if(cityList.get(j).getCity_name().equals(spcity.getSelectedItem().toString()))
                        {

                            ctid=j;
                            System.out.println("InSideselectIndex"+ctid);
                        }
                    }
                    expenseEntry.setExpCode(id);
                    expenseEntry.setCityId(cityList.get(ctid).getCity_id());
                    expenseEntry.setStateid(stateList.get(stid).getState_id());
                    expenseEntry.setBillAmount(earamount.getText().toString());
                    expenseEntry.setRemarks((earremark.getText().toString().isEmpty() ? "" : earremark.getText().toString()));
                    expenseEntry.setFromDate(fromDate.getText().toString());
                    expenseEntry.setToDate(toDate.getText().toString());
                    expenseEntry.setTimeFrom(fromTime.getText().toString());
                    expenseEntry.setTimeTo(toTime.getText().toString());
                    new SaveExpense().execute();

                }

                else{
                    dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }
            }
        });
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i = new Intent(ExpenseAdvanceRequest.this, Expense_Advance_Request_Details.class);
                startActivity(i);
            }
        });
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseAdvanceRequest.this);
                builder.setMessage("Are you sure you want to Delete")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int aid) {
                                if (connectionDetector.isConnectingToInternet())
                                {

                                    DeleteData runner = new DeleteData();
                                    runner.execute(id);
                                }
                                else{
                                    dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                                    dialogWithOutView.show(getFragmentManager(), "Info");
                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });
        /*spstate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                if(spstate.getSelectedItem().toString().equalsIgnoreCase("Select State"))
                {
                    spstate.setSelection(0);

                }
                else{
                    if (connectionDetector.isConnectingToInternet())

                    {
                        new GetCityList().execute();

                    }
                    else{

                        dialogWithOutView=AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                        dialogWithOutView.show(getFragmentManager(), "Info");
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });*/

    }
    class SaveExpense extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progressDialog= ProgressDialog.show(ExpenseAdvanceRequest.this,"saving Data", "Saving Data Please wait...", true);
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                if(expenseEntry!=null)
                {
                    insertExpense(expenseEntry);

                }

//                expenseTypeList=getExpenseType();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            System.out.println("Result"+result);
            clearData();
            progressDialog.dismiss();
            callActivity();

        }
    }
    void callActivity(){
        finish();
        // Edited And Commented by sandeep 04-03-17
        finish();
        new IntentSend(getApplicationContext(), AdvanceExpenseRequest.class).toSendAcivity();
    }
    private class DeleteData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ExpenseAdvanceRequest.this,
                    "ProgressDialog",
                    "Wait for delete data");
        }


        @Override
        protected String doInBackground(String... params) {
            String response="";
            server= Constant.SERVER_WEBSERVICE_URL;
            String url="http://"+server+"/And_Sync.asmx/xJSExpenseDeleteAdvanceReq";
            System.out.println("ServerData"+url+"-"+id);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("Id",id));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response = httpclient.execute(httppost, responseHandler);

            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return response;
        }


        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Result"+result,Toast.LENGTH_SHORT).show();
            callActivity();
        }


      /*  @Override
        protected void onProgressUpdate(String... text) {
            finalResult.setText(text[0]);

        }*/
    }
    void deletetExpenseGroup()
    {
        try{
            server= Constant.SERVER_WEBSERVICE_URL;
            String url="http://"+server+"/And_Sync.asmx/xJSExpenseDeleteAdvanceReq";
            System.out.println("ServerData"+url+"-"+id);
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(url);
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("Id",id));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response);
            if(response.length()>0)
            {
                Toast.makeText(getApplicationContext(),"Sussess"+response,Toast.LENGTH_SHORT).show();
            }

        }
        catch(Exception e)
        {
            System.out.println(e);

        }
    }

    boolean insertExpense(ExpenseEntry expense)
    {
        boolean mSuccess=false;
        try{
            server= Constant.SERVER_WEBSERVICE_URL;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://"+server+"/And_Sync.asmx/xJSInsertExpenseAdvanceReq");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            System.out.println("SendDataOnServer"+expense.getExpCode()+"-"+(expense.getToDate()==null?"":expense.getToDate())+"-"+
                    (expense.getTimeFrom()==null?"":expense.getTimeFrom())+"-"+(expense.getTimeTo()==null?"":expense.getTimeTo())+"-"+
                    expense.getBillAmount()+"-"+expense.getRemarks()+"-"+expense.getStateid()+"-"+expense.getCityId()+"-"+userId);
            nameValuePairs.add(new BasicNameValuePair("ExpAdvId",expense.getExpCode()));
            nameValuePairs.add(new BasicNameValuePair("FromDate",(expense.getFromDate()==null?"":expense.getFromDate())));
            nameValuePairs.add(new BasicNameValuePair("ToDate",(expense.getToDate()==null?"":expense.getToDate())));
            nameValuePairs.add(new BasicNameValuePair("FromTime",(expense.getTimeFrom()==null?"":expense.getTimeFrom())));
            nameValuePairs.add(new BasicNameValuePair("ToTime",(expense.getTimeTo()==null?"":expense.getTimeTo())));
            nameValuePairs.add(new BasicNameValuePair("Amount",expense.getBillAmount()));
            nameValuePairs.add(new BasicNameValuePair("Remarks",expense.getRemarks()));
            nameValuePairs.add(new BasicNameValuePair("StateId",expense.getStateid()));
            nameValuePairs.add(new BasicNameValuePair("CityId",expense.getCityId()));
            nameValuePairs.add(new BasicNameValuePair("UserId",userId));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response1 = httpclient.execute(httppost, responseHandler);
            // System.out.println("Response : " + response1);
//					JSONObject json = new JSONObject(response1);
            JSONArray jsonarray = new JSONArray(response1);
            JSONObject json=null;
            for (int k = 0; k < jsonarray.length(); k++) {
                json = jsonarray.getJSONObject(k);
                // System.out.println(json);
            }
            int st;
            try{
                st = Integer.parseInt(json.getString("St"));
                // System.out.println(st);
                if(st>0)
                {
                    progressDialog.dismiss();
                    if (connectionDetector.isConnectingToInternet())

                    {

//                        dialogWithOutView = AlertOkDialog.newInstance("Expense  saved successfully");
//                        dialogWithOutView.show(getFragmentManager(), "error");
                        clearData();
//                        new ExpenseEntryType().execute();

                    }
                    else{

                        dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                        dialogWithOutView.show(getFragmentManager(), "Info");
                    }

//                    alertOkDialog = AlertOkDialog.newInstance("Expense  saved successfully");
//                    alertOkDialog.show(getFragmentManager(), "msg");

                }
                else if(st==0)
                {
                    progressDialog.dismiss();
//                   new Custom_Toast(getApplicationContext(), "Expense Group not saved").showCustomAlert();
                    dialogWithOutView = AlertOkDialog.newInstance("Expense not saved");
                    dialogWithOutView.show(getFragmentManager(), "error");
                }



            }
            catch(Exception e)
            {
                mSuccess=true;
                System.out.println(e);

            }

        }
        catch(Exception e){
//					connectionDetector=new ConnectionDetector(context);
//					if(!connectionDetector.isConnectingToInternet())
//					{
//						exceptionData.setInternetExceptionData("You don't have internet connection.", "Information");
//						//exceptionData.setExceptionData(e.toString(), "ClientProtocolException InsertEnviro");
//					}
            System.out.println("Exception : " + e.getMessage());
            mSuccess=true;
        }



        return mSuccess;

    }

    void clearData()
    {
        fromDate.setText(null);
        toDate.setText(null);
        spstate.setSelection(0);
        spcity.setSelection(0);
        earamount.setText(null);
        earremark.setText(null);
        fromTime.setText(null);
        toTime.setText(null);

    }
    @Override
    public void onClick(View v) {
        showDialog(v.getId());
    }
    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {

        if(id== R.id.earfromtime)
        {
            tv1.setTextColor(Color.parseColor("#FF808080"));
            l1.setBackgroundColor(Color.parseColor("#666666"));
            int hour=Integer.parseInt(fromTime.getText().toString().substring(0,2));

            int min=Integer.parseInt(fromTime.getText().toString().substring(3,5));
            return new TimePickerDialog(this, timePickerListener, hour, min, false);
        }
        else if(id== R.id.eartotime)
        {
            tv1.setTextColor(Color.parseColor("#FF808080"));
            l1.setBackgroundColor(Color.parseColor("#666666"));
            int hour=Integer.parseInt(toTime.getText().toString().substring(0,2));

            int min=Integer.parseInt(toTime.getText().toString().substring(3,5));
            return new TimePickerDialog(this, timePickerListener1, hour, min, false);
        }
        return null;

    }

    private TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            fromTime.setText(pad(hourOfDay) + ":" + pad(minute));
        }
    };
    private TimePickerDialog.OnTimeSetListener timePickerListener1 = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            toTime.setText(pad(hourOfDay) + ":" + pad(minute));
        }
    };
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
    protected class getData extends AsyncTask<Void, Void, ArrayList<DashboardModel>> {
        ProgressDialog pdLoading = new ProgressDialog(ExpenseAdvanceRequest.this);
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPreExecute() {
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected ArrayList<DashboardModel> doInBackground(Void... params) {
            server= Constant.SERVER_WEBSERVICE_URL;
            model = new DashboardModel();
            //SmId = preferences2.getString("CONPERID_SESSION", null);
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FromDate=fromDate.getText().toString();
                    ToDate= toDateEditL.getText().toString();
                }
            });*/
            String str="http://" + server + "/And_Sync.asmx/xJSGetExpAdvance?ID="+id;
                Log.e("findLeaveRequest", str);
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try {
                dashboardModels = new ArrayList<DashboardModel>();
                URL url = new URL(str);
                Log.e("DataURL",str);
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
                       /* ExpenseAdvanceData expenseAdvanceData=new ExpenseAdvanceData();
                        expenseAdvanceData.setDocid(objs.getString("Dcid").toString());
                        expenseAdvanceData.setEmpname("EMPNAME");
                        expenseAdvanceData.setTfdate(objs.getString("Fdt").toString());
                        expenseAdvanceData.setTtdate(objs.getString("Tdt").toString());
                        expenseAdvanceData.setStatus(objs.getString("RescStatus").toString());
                        expenseAdvanceData.setAmount("0.0");
                        *//*expenseAdvanceData.setAmount(objs.getString("Amt").toString());*//*
                       *//* expenseAdvanceData.setApamount(objs.getString("aamt").toString());*/
                        dashboardModels.add(model.advanceLeaveRequestdataModel(
                                objs.getString("Fdt").toString(),
                                objs.getString("Tdt").toString(),
                                objs.getString("Amt").toString(),
                                objs.getString("Rmk").toString(),
                                objs.getString("RescStatus").toString(),
                                objs.getString("ftime").toString(),
                                objs.getString("ttime").toString(),
                                objs.getString("City").toString(),
                                objs.getString("State").toString()));
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
                System.out.println("Response"+response.size()+"-"+response);
                if(response.size()>0) {
                    int selectIndex=0,dataindex=0;
                    String stateid=dashboardModels.get(selectIndex).getStateID();
                    System.out.println("State Size"+stateList+"-"+stateid);
                    for(int j=0;j<stateList.size();j++)
                    {
                      //  System.out.println("StateListData"+stateList.get(j).getState_id()+"-"+stateList.get(j).getState_name());
                        if(stateList.get(j).getState_id().equals(stateid))
                        {
                            selectIndex=j;
                         //   System.out.println("InSideselectIndex"+selectIndex);
                        }
                    }
                    spstate.setSelection(selectIndex);
                    if (connectionDetector.isConnectingToInternet())

                    {
                        System.out.println("GetCityCall"+dashboardModels.get(dataindex).getTfdate()+"-"+dashboardModels.get(dataindex).getTtdate());
                        fromDate.setText(dashboardModels.get(dataindex).getTfdate());
                        fromTime.setText(dashboardModels.get(dataindex).getFromtime());
                        toDate.setText(dashboardModels.get(dataindex).getTtdate());
                        toTime.setText(dashboardModels.get(dataindex).getTotime());
                        earamount.setText(dashboardModels.get(dataindex).getAdvamount());
                        earremark.setText(dashboardModels.get(dataindex).getRemark());
                        new GetCityList().execute();
                        if(progressDialog.isShowing())
                        {
                            progressDialog.dismiss();
                        }

                    }
                    else{

                        dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                        dialogWithOutView.show(getFragmentManager(), "Info");
                    }


                    Log.e("App", response.toString());
                }else{

                    new Custom_Toast(getApplicationContext(), "No Data Found in Datbase").showCustomAlert();
                }
            }else{
                new Custom_Toast(getApplicationContext(), "No Data Found").showCustomAlert();
            }
            super.onPostExecute(response);
            pdLoading.dismiss();
        }
    }

    class GetCityList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
           progressDialog= ProgressDialog.show(ExpenseAdvanceRequest.this,"Loading Data", "Loading Please wait...", true);

            stId=stateList.get(spstate.getSelectedItemPosition()).getState_id();
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub

            try {
                cityList=getCityList(stId);

            } catch (Exception e) {
                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }

                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            progressDialog.dismiss();
            if(cityList!=null)
            {
                cityArrayList=new ArrayList<String>();

                for(int i=0;i<cityList.size();i++)
                {
                    System.out.println("City List"+cityList.get(i).getCity_id()+"-"+cityList.get(i).getCity_name());
                    cityArrayList.add(cityList.get(i).getCity_name());
                }

            }

            cityAdapter=new ArrayAdapter<String>(ExpenseAdvanceRequest.this, R.layout.simple_spinner_dropdown_item,cityArrayList);
            cityAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            spcity.setAdapter(cityAdapter);
            int selectIndex=0;
            String cityname=dashboardModels.get(selectIndex).getCity();
            for(int j=0;j<cityList.size();j++)
            {
                if(cityList.get(j).getCity_id().equals(cityname))
                {
                    selectIndex=j;
                    System.out.println("InSideselectIndex"+selectIndex);
                }
            }
            spcity.setSelection(selectIndex);



        }
    }
    ArrayList<City> getCityList(String stId) {
        server= Constant.SERVER_WEBSERVICE_URL;
        ArrayList<City> cityLists = new ArrayList<City>();
        String url="";
        url = "http://" + server + "/And_Sync.asmx/xJSCityForExpense?stateid=" + stId +"&SMID="+smid+"&Expensetype=0";
        Log.e("CityURL",url);
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        System.out.println("ResultInCity"+result);
        if (result != null) {
            try {

                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    City cityList = new City();
                    cityList.setCity_id(obj.getString("id"));
                    cityList.setCity_name(obj.getString("Nm"));
                    cityList.setAmount(obj.getString("Amt"));
                    cityLists.add(cityList);
                }

            } catch (Exception e) {
                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }
                System.out.println(e);
            }


        }

        return cityLists;
    }
    class GetStates extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(ExpenseAdvanceRequest.this,"saving Data", "Saving Data Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {

                stateList=getStateList();


            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }
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
            if(stateList!=null)
            {
                stateArrayList=new ArrayList<String>();
                for(int i=0;i<stateList.size();i++)
                {
                    stateArrayList.add(stateList.get(i).getState_name());
                    System.out.println("Size of stateList"+stateList.get(i).getState_name());
                }
                System.out.println("Size of Array"+stateArrayList);
                stateAdapter=new ArrayAdapter<String>(ExpenseAdvanceRequest.this, R.layout.simple_spinner_dropdown_item,stateArrayList);
                stateAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                // System.out.println("State Selection"+stateAdapter+"$%^&*"+expTypeCode);
                spstate.setAdapter(stateAdapter);
                new getData().execute();

            }

        }
    }
    ArrayList<State> getStateList()
    {
        server= Constant.SERVER_WEBSERVICE_URL;
        ArrayList<State> stateLists = new ArrayList<State>();
        String url="";
        //System.out.println("Spinner Data"+expTypeCode);
        url = "http://" + server + "/And_Sync.asmx/xJSStateForExpense?&SMID=" + smid + "&Expensetype=0";
        Log.e("StateURL",url);

        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        //System.out.println("Conveyance Travel"+url);
        //System.out.println("Conveyance Travel Data"+result);
        if (result != null) {
            try {
                State stateList1 = new State();
                stateList1.setState_id("0");
                stateList1.setState_name("Select State");
                stateLists.add(stateList1);
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    State stateList = new State();
                    stateList.setState_id(obj.getString("id"));
                    stateList.setState_name(obj.getString("Nm"));
                    stateLists.add(stateList);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }

        return stateLists;
    }
    private void showDatePicker(int id) {
        DateAndTimePicker date = new DateAndTimePicker();
        /**
         * Set Up Current Date Into dialog
         */

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        if(id== R.id.earfromdate)
        {
            date.setCallBack(ondate1);
            date.show(getSupportFragmentManager(), "Date Picker");
        }
        else if(id== R.id.eartodate)
        {
            date.setCallBack(ondate2);
            date.show(getSupportFragmentManager(), "Date Picker");
        }

    }
    OnDateSetListener ondate1 = new OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String strDate=(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth))+"/"+(((monthOfYear+1)<10?("0"+(monthOfYear+1)):(monthOfYear+1)))+"/"+year;
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

            String strDate=(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth))+"/"+(((monthOfYear+1)<10?("0"+(monthOfYear+1)):(monthOfYear+1)))+"/"+year;
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
            if(filledDate.getTime()>=f)
            {
                toDate.setText(format2.format(date));

            }

          /*  else{
                dialogWithOutView= AlertOkDialog.newInstance("To Date and Time should be greater than From Date and Time");
                dialogWithOutView.show(getFragmentManager(), "Info");
                serach.setClickable(false);
            }
*/
        }
    };
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        //super.onBackPressed();

        fromDate.setText(null);
        toDate.setText(null);
        spstate.setSelection(0);
        spcity.setSelection(0);
        earamount.setText(null);
        earremark.setText(null);
        fromTime.setText(null);
        toTime.setText(null);
        finish();
        Intent i = new Intent(ExpenseAdvanceRequest.this, Expense_Advance_Request_Details.class);
        startActivity(i);
        super.onBackPressed();
    }

}
