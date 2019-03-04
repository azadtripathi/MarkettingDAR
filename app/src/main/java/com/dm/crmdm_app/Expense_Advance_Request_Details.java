package com.dm.crmdm_app;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dm.library.AlertMessage;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.Constant;
import com.dm.library.CustomArrayAdapterAdvanceLeaveRequestList;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.library.DateFunction;
import com.dm.library.IntentSend;
import com.dm.model.DashboardModel;

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

public class Expense_Advance_Request_Details extends AppCompatActivity implements
        AlertMessage.NoticeDialogListenerWithoutView,
        CustomArrayAdapterAdvanceLeaveRequestList.FindLeaveRequestTransactionListener,
        CustomArrayAdapterAdvanceLeaveRequestList.HolderListener{

    // write by sandeep 06-03-17
    // start

    CustomArrayAdapterAdvanceLeaveRequestList customArrayAdapterLeaveRequestList;
    ConnectionDetector connectionDetector;
    ArrayList<String> StatusList=new ArrayList<String>();
    EditText fdate,tdate;String sttype;Spinner statusspinner;
    String userId;SimpleDateFormat df;
    ArrayAdapter smanAdapter;AlertOkDialog dialogWithOutView;
    Button serach;ListView listView;
    SharedPreferences preferences2;
    DashboardModel model;ImageView filter;
    ArrayList<DashboardModel> dashboardModels;
    String server;
    boolean flag=false;String stringFromdate,stringTodate;
    LinearLayout linearlayout;
    RelativeLayout footer;
    //End
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_request);

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
        StatusList.add("Pending");
        StatusList.add("Approved");
        StatusList.add("All");
        preferences2=this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        userId=preferences2.getString("USER_ID", "0");
        TextView tv = (TextView)findViewById(R.id.text);
        tv.setText("Expense Advance Request Details");
        connectionDetector=new ConnectionDetector(getApplicationContext());
        statusspinner=(Spinner)findViewById(R.id.earsp);
        fdate=(EditText)findViewById(R.id.adfromDate);
        fdate.setInputType(InputType.TYPE_NULL);
        Calendar cal=Calendar.getInstance();
        df = new SimpleDateFormat("dd/MMM/yyyy");
        cal.add(Calendar.MONTH, -1);
        fdate.setText(df.format(cal.getTime()));
        tdate=(EditText)findViewById(R.id.adtoDateEditL);
        Calendar cal1 = Calendar.getInstance();
        tdate.setText(df.format(cal1.getTime()));
        tdate.setInputType(InputType.TYPE_NULL);
        linearlayout = (LinearLayout)findViewById(R.id.linear);
        footer = (RelativeLayout)findViewById(R.id.footer);
        filter = (ImageView)findViewById(R.id.filter);
        footer.setVisibility(View.GONE);
        serach=(Button)findViewById(R.id.btn_search);
        serach.setClickable(false);
        listView = (ListView) findViewById(R.id.findLeaveRequestlist);
        smanAdapter=new ArrayAdapter<String>(this, R.layout.adapterdropdown,StatusList);
        smanAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        statusspinner.setAdapter(smanAdapter);
        statusspinner.setSelection(0);
//        LayoutInflater inflater = getLayoutInflater();
//        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.advance_header_leave_list, listView, false);
//        listView.addHeaderView(header, null, false);
        System.out.println("ADAPTER LENG"+StatusList.size()+"-"+userId);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });
        serach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                long b = DateFunction.ConvertDateToTimestamp(fdate.getText().toString());
                long t = DateFunction.ConvertDateToTimestamp(tdate.getText().toString());

                if (b > t) {
                    new Custom_Toast(getApplicationContext(), "To Date Cannot be less than from date.").showCustomAlert();

                } else {

                if (connectionDetector.isConnectingToInternet())
                {
                    stringFromdate=fdate.getText().toString();
                    stringTodate=tdate.getText().toString();
                    flag=true;
                        new getLevel().execute();

                }
                else{
                    dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }
            }
            }
        });
        statusspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                sttype=statusspinner.getSelectedItem().toString();
                serach.setClickable(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        fdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = Expense_Advance_Request_Details.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker(view.getId());
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub

                parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                return false;
            }
        });

        tdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = Expense_Advance_Request_Details.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker(view.getId());
            }
        });
//        if (connectionDetector.isConnectingToInternet())
//        {
//
//            new getLevel().execute();
//        }
//        else{
//            dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
//            dialogWithOutView.show(getFragmentManager(), "Info");
//        }
    }

    @Override
    public void holderListener(CustomArrayAdapterAdvanceLeaveRequestList.MyHolder myHolders) {

    }



    protected class getLevel extends AsyncTask<Void, Void, ArrayList<DashboardModel>> {
        ProgressDialog pdLoading = new ProgressDialog(Expense_Advance_Request_Details.this);
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPreExecute() {
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
          /*  String jjk=salesManNameList2.get((int) salesManMaterial.getSelectedItemId());
            Rmns= salesManMaterial.getSelectedItemId();
            //new Custom_Toast(getApplicationContext(), jjk).showCustomAlert();
            SmId=jjk;

            // new Custom_Toast(getApplicationContext(), SmId).showCustomAlert();
            dropdownId= String.valueOf(salesManMaterial.getSelectedItemId());
            //new Custom_Toast(getApplicationContext(), dropdownId).showCustomAlert();*/
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
            String str;
            if(!flag)
            {
               str="http://" + server + "/And_Sync.asmx/xJSFindallExpAdvance?userId="+userId;
                Log.e("findLeaveRequest", str);
            }
            else
            {
                str="http://"+server+"/And_Sync.asmx/xJSFindGoExpAdvance?Fromdate="+stringFromdate+"&Todate="+stringTodate+"&Status="+sttype+"&userId="+userId;
                Log.e("findLeaveRequest", str);
            }


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
                      /*  ExpenseAdvanceData expenseAdvanceData=new ExpenseAdvanceData();
                        expenseAdvanceData.setID(objs.getString("Id").toString());
                        expenseAdvanceData.setDocid(objs.getString("Dcid").toString());
                        expenseAdvanceData.setEmpname(objs.getString("Dcid").toString());
                        expenseAdvanceData.setTfdate(objs.getString("Fdt").toString());
                        expenseAdvanceData.setTtdate(objs.getString("Tdt").toString());
                        expenseAdvanceData.setStatus(objs.getString("RescStatus").toString());
                        expenseAdvanceData.setAmount(objs.getString("Dcid").toString());
                        *//*expenseAdvanceData.setAmount(objs.getString("Amt").toString());*//*
                       *//* expenseAdvanceData.setApamount(objs.getString("aamt").toString());*/
                        dashboardModels.add(model.advanceLeaveRequestListModel(
                                objs.getString("Id").toString(),
                                objs.getString("Dcid").toString(),
                                objs.getString("Empnm").toString(),
                                objs.getString("Fdt").toString(),
                                objs.getString("Tdt").toString(),
                                objs.getString("RescStatus").toString(),
                                objs.getString("Amt").toString(),
                                objs.getString("ApprAmt").toString(),
                                objs.getString("ftime").toString(),
                                objs.getString("ttime").toString()));
                        System.out.println("Data Store in Array"+ objs.getString("Id").toString()+"-"+
                                objs.getString("Dcid").toString()+"-"+
                                objs.getString("Fdt").toString()+"-"+
                                objs.getString("Tdt").toString()+"-"+
                                objs.getString("RescStatus").toString()+"-"+
                                objs.getString("Amt").toString());
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
                    listView.setVisibility(View.GONE);
                    customArrayAdapterLeaveRequestList = new CustomArrayAdapterAdvanceLeaveRequestList(
                            getApplicationContext(), response, R.layout.advance_leave_request_list_row,
                            R.id.apptextdate, Expense_Advance_Request_Details.this, Expense_Advance_Request_Details.this);

                    if (customArrayAdapterLeaveRequestList.getCount() == 0) {
                        new Custom_Toast(getApplicationContext(), "No Record found").showCustomAlert();
                        listView.setAdapter(null);

                    } else {
                        linearlayout.setVisibility(View.GONE);
                        footer.setVisibility(View.VISIBLE);
                        filter.setVisibility(View.VISIBLE);
                        listView.setVisibility(View.VISIBLE);
                        listView.setAdapter(customArrayAdapterLeaveRequestList);

                    }
//                    listView.setAdapter(customArrayAdapterLeaveRequestList);
//                    listView.setClickable(true);
//                    listView.setVisibility(View.VISIBLE);
//                    Log.e("App", response.toString());
                }
                else{
                    listView.setVisibility(View.GONE);
                    new Custom_Toast(getApplicationContext(), "No Record found.").showCustomAlert();
                }
            }
            else{
                listView.setVisibility(View.GONE);
                new Custom_Toast(getApplicationContext(), "No Record found.").showCustomAlert();
            }
            super.onPostExecute(response);
            pdLoading.dismiss();
        }
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
        if(id== R.id.adfromDate)
        {
            date.setCallBack(ondate1);
            date.show(getSupportFragmentManager(), "Date Picker");
        }
        else if(id== R.id.adtoDateEditL)
        {
            date.setCallBack(ondate2);
            date.show(getSupportFragmentManager(), "Date Picker");
        }

    }
    DatePickerDialog.OnDateSetListener ondate1 = new DatePickerDialog.OnDateSetListener() {
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

            fdate.setText(format2.format(date));
        }
    };

    DatePickerDialog.OnDateSetListener ondate2 = new DatePickerDialog.OnDateSetListener() {
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


            f = DateFunction.ConvertDateToTimestamp(fdate.getText().toString());
            if(filledDate.getTime()>=f)
            {
                tdate.setText(format2.format(date));
                serach.setClickable(true);

            }

            else{
                dialogWithOutView= AlertOkDialog.newInstance("To Date and Time should be greater than From Date and Time");
                dialogWithOutView.show(getFragmentManager(), "Info");
                serach.setClickable(false);
            }

        }
    };
    @Override
    public void onPause()
    {
        super.onPause();
    }
    @Override
    public void onResume()
    {
        super.onResume();
    }
    @Override
    public void onStop()
    {
        super.onStop();
    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
//        super.onBackPressed();
//        finish();
        Intent i = new Intent(Expense_Advance_Request_Details.this, AdvanceExpenseRequest.class);

        i.putExtra("ID","0");
        i.putExtra("FromFind",false);
        startActivity(i);
        super.onBackPressed();
        finish();
    }


    @Override
    public void holderListener(ArrayList<LeaveRequest> leaveRequestList) {

    }

    @Override
    public void onDialogPositiveWithoutViewClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeWithoutViewClick(DialogFragment dialog) {

    }
}
