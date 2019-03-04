package com.dm.crmdm_app;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.controller.CityController;
import com.dm.controller.StateController;
import com.dm.library.AlertMessage;
import com.dm.library.AlertOkDialog;
import com.dm.library.CityDropDownListAdapter;
import com.dm.library.ConnectionDetector;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.library.DateFunction;
import com.dm.library.DbCon;
import com.dm.library.IntentSend;
import com.dm.library.LoadActivity;
import com.dm.library.StateDropDownListAdapter;
import com.dm.model.AppData;
import com.dm.model.City;
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
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AdvanceExpenseRequest extends AppCompatActivity implements StateDropDownListAdapter.CityAccessInterface,AlertMessage.NoticeDialogListenerWithoutView {
    EditText city, state;
    private boolean expanded;
    ArrayList<String> timeArrayList;
    public static boolean[] checkSelected;
    ArrayAdapter<String> timeAdapter;
    public static boolean[] checkStateSelected;
    ExpenseEntry expenseEntryObj = null;
    TextView tv1, tv2;LoadActivity loadActivity;
    String server, smid, userId;
    EditText amt, remark, fromDate, toDate;
    CityController cityController;
    Spinner fromTimeSpinner, toTimeSpinner;
    ArrayList<City> ArraycityList;
    ImageView save, cancel, find, delete, cam;
    ArrayList<String> cityList;
    String stId;
    AlertMessage alertMessage;
    private PopupWindow pw, pw1;
    StateDropDownListAdapter stateDropDownListAdapter;
    CityDropDownListAdapter adapter;
    ExpenseEntry expenseEntry;
    ProgressDialog progressDialog, getStateprogressDialog;
    StateController stateController;
    SharedPreferences preferences;
    ArrayList<State> stateList;
    ArrayList<AppData> appDataArray;
    boolean fromFind = false;
    ArrayList<String> stateArrayList;
    AppDataController appDataController1;
    AlertOkDialog dialogWithOutView;
    ConnectionDetector connectionDetector;
    SharedPreferences cityPreferences, statePreferences;
    String delResp;
    String expId = "0";
    DbCon dbCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_expense_request);

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
        tv.setText("Expense Advance Request");
        appDataController1 = new AppDataController(AdvanceExpenseRequest.this);
        loadActivity=new LoadActivity(AdvanceExpenseRequest.this);
        dbCon = new DbCon(getApplicationContext());
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server = appData.getCompanyUrl();
        preferences = AdvanceExpenseRequest.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        smid = preferences.getString("CONPERID_SESSION", "0");
        userId = preferences.getString("USER_ID", "0");
        connectionDetector = new ConnectionDetector(getApplicationContext());
        //State fill
        tv2 = (TextView) findViewById(R.id.SelectStateBox);
        tv2.setInputType(InputType.TYPE_NULL);
        tv2.setFocusable(false);
        tv2.setText("None selected");
        Intent intent = getIntent();
        if (intent != null) {
            expId = intent.getStringExtra("ID");
            fromFind = intent.getBooleanExtra("FromFind", false);
            System.out.println("Send Data on Server In" + expId);
        } else {
            expId = "0";
            System.out.println("Send Data on Server Out" + expId);
        }

        if (connectionDetector.isConnectingToInternet())

        {
            new GetStates().execute();

        } else {

            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

        tv1 = (TextView) findViewById(R.id.SelectBox);
        tv1.setInputType(InputType.TYPE_NULL);
        tv1.setFocusable(false);
        tv1.setText("None selected");
        save = (ImageView) findViewById(R.id.button1);
        cancel = (ImageView) findViewById(R.id.button2);
        delete = (ImageView) findViewById(R.id.button3);
        find = (ImageView) findViewById(R.id.findbutton1);
        cam = (ImageView) findViewById(R.id.takePicture);
        cam.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        amt = (EditText) findViewById(R.id.amount);
//        amt.setInputType(InputType.TYPE_NULL);
//        amt.setFocusable(false);
        fromDate = (EditText) findViewById(R.id.fromDate);
        toDate = (EditText) findViewById(R.id.toDate);
        remark = (EditText) findViewById(R.id.remark);
//        remark.setInputType(InputType.TYPE_NULL);
//        remark.setFocusable(false);
        fromTimeSpinner = (Spinner) findViewById(R.id.fromTimeSpinner);
        toTimeSpinner = (Spinner) findViewById(R.id.toTimeSpinner);
        fromDate.setInputType(InputType.TYPE_NULL);
        fromDate.setFocusable(true);
        toDate.setInputType(InputType.TYPE_NULL);
        toDate.setFocusable(false);
        timeArrayList = getTimeList();
        timeAdapter = new ArrayAdapter<String>(AdvanceExpenseRequest.this, R.layout.simple_spinner_dropdown_item, timeArrayList);
        timeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        /*fromTimeSpinner.setSelection(getIndex(fromTimeSpinner,"10:00am"));*/
        fromTimeSpinner.setAdapter(timeAdapter);
        fromTimeSpinner.setSelection(20);
        /*toTimeSpinner.setSelection(getIndex(toTimeSpinner,"06:00pm"));*/
        toTimeSpinner.setAdapter(timeAdapter);
        toTimeSpinner.setSelection(36);

        if (fromFind) {
            save.setImageResource(R.drawable.update);
            delete.setVisibility(View.VISIBLE);
            dbCon.open();
            save.setEnabled(dbCon.ButtonEnable("AdvanceExpenseRequest", "Expense", "Edit"));
            if(!dbCon.ButtonEnable("AdvanceExpenseRequest","Expense","Edit"))
            {
                save.setColorFilter(Color.parseColor("#808080"));
            }

            dbCon.close();
            dbCon.open();
            delete.setEnabled(dbCon.ButtonEnable("AdvanceExpenseRequest", "Expense", "Delete"));
            if(!dbCon.ButtonEnable("AdvanceExpenseRequest","Expense","Delete"))
            {
                delete.setColorFilter(Color.parseColor("#808080"));
            }
            dbCon.close();
            if (connectionDetector.isConnectingToInternet()) {
                new GetExpenseEntry().execute();

            } else {

                dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                dialogWithOutView.show(getFragmentManager(), "Info");
            }

        } else {
            save.setImageResource(R.drawable.save1);
            delete.setVisibility(View.GONE);
            dbCon.open();
            save.setEnabled(dbCon.ButtonEnable("AdvanceExpenseRequest", "Expense", "Add"));
            if(!dbCon.ButtonEnable("AdvanceExpenseRequest","Expense","Add"))
            {
                save.setColorFilter(Color.parseColor("#808080"));
            }
            dbCon.close();
        }


        fromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = AdvanceExpenseRequest.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker(v.getId());
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = AdvanceExpenseRequest.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    fromDate.setFocusable(false);
                    toDate.setFocusable(true);
                }
                showDatePicker(v.getId());
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Send Data on Server1" + expId);
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
                    alertMessage = AlertMessage.newInstance(
                            "Do you want to delete ?", "delete", "cancel");
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
                // Write By Sandeep 06-03-2017
                //Start
                if (connectionDetector.isConnectingToInternet())

                {
                    (new IntentSend(getApplicationContext(), Expense_Advance_Request_Details.class)).toSendAcivity();
                   // finish();

                } else {

                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }

                //End
            }
        });
    }

    void saveData() {
        if (fromDate.getText().toString().isEmpty() || fromDate.getText().toString().length() < 1) {
            new Custom_Toast(getApplicationContext(), "Please Select From Date").showCustomAlert();
        } else if (toDate.getText().toString().isEmpty() || toDate.getText().toString().length() < 1) {
            new Custom_Toast(getApplicationContext(), "Please Select To Date").showCustomAlert();
        } else if (fromTimeSpinner.getSelectedItem().toString().equalsIgnoreCase("12:00am")) {
            new Custom_Toast(getApplicationContext(), "Please Select From Time").showCustomAlert();
        } else if (toTimeSpinner.getSelectedItem().toString().equalsIgnoreCase("12:00am")) {
            new Custom_Toast(getApplicationContext(), "Please Select To Time").showCustomAlert();
        } else if (amt.getText().toString().isEmpty()) {
            new Custom_Toast(getApplicationContext(), "Please Enter Amount").showCustomAlert();
        } else if (Float.parseFloat(amt.getText().toString()) <= 0) {

            new Custom_Toast(getApplicationContext(), "Please enter Amount greater than 0").showCustomAlert();
        } else if (remark.getText().toString().isEmpty()) {
            new Custom_Toast(getApplicationContext(), "Please Enter Remark").showCustomAlert();
        } else if (tv2.getText().toString().equals("None selected") || tv2.getText().toString().length() < 1) {
            new Custom_Toast(getApplicationContext(), "Please select city to visit").showCustomAlert();
        } else if (tv1.getText().toString().equals("None selected") || tv1.getText().toString().length() < 1) {
            new Custom_Toast(getApplicationContext(), "Please select city to visit").showCustomAlert();
        }
        else{
           long f = DateFunction.ConvertDateToTimestamp(fromDate.getText().toString());
           long t = DateFunction.ConvertDateToTimestamp(toDate.getText().toString());
            if(f>t)
            {
                dialogWithOutView= AlertOkDialog.newInstance("From Date Should be less than To Date");
                dialogWithOutView.show(getFragmentManager(), "Info");
            }

        else if (fromDate.getText().toString().equals(toDate.getText().toString()) && (DateFunction.ConvertDateToTimestampWithTimeAmPm(fromDate.getText().toString() + " " + (fromTimeSpinner.getSelectedItem().toString().substring(0, 5) + ":00 " + fromTimeSpinner.getSelectedItem().toString().substring(5))))>= DateFunction.ConvertDateToTimestampWithTimeAmPm(toDate.getText().toString() + " " + (toTimeSpinner.getSelectedItem().toString().substring(0, 5) + ":00 " + toTimeSpinner.getSelectedItem().toString().substring(5)))) {

               new Custom_Toast(getApplicationContext(), "From Time Should be less than To Time").showCustomAlert();
        }
        else {

            expenseEntry = new ExpenseEntry();
            System.out.println("Send Data on Server2" + expId);
            expenseEntry.setExpCode(expId);
            cityPreferences = getSharedPreferences("MyExpCity", Context.MODE_PRIVATE);
            statePreferences = getSharedPreferences("MyExpState", Context.MODE_PRIVATE);
            expenseEntry.setCityId(cityPreferences.getString("CITY_IDS", "0"));
            expenseEntry.setStateid(statePreferences.getString("CITY_IDS", "0"));
            expenseEntry.setBillAmount(amt.getText().toString());
            expenseEntry.setRemarks((remark.getText().toString().isEmpty() ? "" : remark.getText().toString()));
            expenseEntry.setFromDate(fromDate.getText().toString());
            expenseEntry.setToDate(toDate.getText().toString());
            expenseEntry.setTimeFrom(fromTimeSpinner.getSelectedItem().toString());
            expenseEntry.setTimeTo(toTimeSpinner.getSelectedItem().toString());

            if (connectionDetector.isConnectingToInternet())

            {
                new SaveExpense().execute();

            } else {

                dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                dialogWithOutView.show(getFragmentManager(), "Info");
            }


        }
        }
    }








    class GetExpenseEntry extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(AdvanceExpenseRequest.this,"Loading Data", "Loading Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                expenseEntryObj=getExpenseEntry();
                stId=expenseEntryObj.getStateid();
                if(expenseEntryObj!=null) {
                    try {
                        if (ArraycityList != null) {
                            ArraycityList.clear();
                        }
                        ArraycityList = getCityList(stId);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }


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
            if(progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
            if(expenseEntryObj!=null)
            {

                if(cityList!=null)
                {
                    cityList.clear();
                }


                cityList = new ArrayList<String>();
                for (
                        int i = 0;
                        i < ArraycityList.size(); i++)

                {
                    cityList.add(ArraycityList.get(i).getCity_name().toUpperCase());
                }

                fromDate.setText(expenseEntryObj.getFromDate());
                toDate.setText(expenseEntryObj.getToDate());
                 amt.setText(expenseEntryObj.getBillAmount());
                remark.setText(expenseEntryObj.getRemarks());
                int selection=20;
//                fromTimeSpinner.setSelection(20);
        /*toTimeSpinner.setSelection(getIndex(toTimeSpinner,"06:00pm"));*/
//                toTimeSpinner.setAdapter(timeAdapter);
//                toTimeSpinner.setSelection(36);
                for(int i=0;i<timeArrayList.size();i++)
                {
                    if(timeArrayList.get(i).equalsIgnoreCase(expenseEntryObj.getTimeFrom()))
                    {
                        selection=i;
                    }

                }
                fromTimeSpinner.setSelection(selection);
                selection=36;
                for(int i=0;i<timeArrayList.size();i++)
                {
                    if(timeArrayList.get(i).equalsIgnoreCase(expenseEntryObj.getTimeTo()))
                    {
                        selection=i;
                    }

                }
                toTimeSpinner.setSelection(selection);
                StringBuilder s=new StringBuilder();
//                System.out.println(s);
//                tv2.setText(expenseEntryObj.getStateid());
//					 String[] words=tv1.getText().toString().split("\\,");
                String[] words=expenseEntryObj.getStateid().split("\\,");
                int l1=words.length;
                int l2=stateList.size();
                System.out.println(l1+l2);
                // checkSelected = new boolean[words.length+1];
                checkStateSelected = new boolean[stateList.size()];
                //initialize all values of list to 'unselected' initially
                int j=0;
                int l=words.length;
                try{
                    for (int i = 0; i < stateList.size(); i++) {
                      /*  if(j<l)
                        {
                            if(stateList.get(i).getState_id().equals(words[j]))
                            {
                                checkStateSelected[i] = true;
                                s.append(stateList.get(i).getState_name()+",");
                                j++;
                            }

                            else{
                                checkStateSelected[i] = false;

                            }

                        }*/

                        for(int k = 0; k < words.length;k++)
                        {
                            if(stateList.get(i).getState_id().equals(words[k]))
                            {
                                checkStateSelected[i] = true;
                                s.append(stateList.get(i).getState_name()+",");

                                break;
                                //j++;
                            }

                            //else{
                                checkStateSelected[i] = false;

                           // }
                        }
                    }
                    int l3=checkStateSelected.length;
                    System.out.println(l3);
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }

                initializeState(stateArrayList, stateList);
//                cityPreferences=getSharedPreferences("MyExpCity",Context.MODE_PRIVATE);
                statePreferences=getSharedPreferences("MyExpState",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor=statePreferences.edit();
                editor.putString("CITY_IDS",expenseEntryObj.getStateid());
//                editor.putString("CITY_NAMES",dsr.getCityName());
                editor.commit();
//                tv2.setText(stateDropDownListAdapter.getSelected());

                state.setText(loadActivity.getSelected(StateDropDownListAdapter.trimEnd(s.toString().trim(), ',')));
//                tv1.setText(expenseEntryObj.getCityId());
//                tv2.setText(expenseEntryObj.getStateid());

//        code for city



                if(fromFind) {
                    if (expenseEntryObj != null) {

                        StringBuilder s1=new StringBuilder();
                        String[] words1 = expenseEntryObj.getCityId().split("\\,");
                        int l11 = words1.length;
                        int l21 = ArraycityList.size();
                        System.out.println(l11 + l21);
                        // checkSelected = new boolean[words.length+1];
                        checkSelected = new boolean[ArraycityList.size()];
                        //initialize all values of list to 'unselected' initially
                        int j1 = 0;
                        int l111 = words1.length;
                        try {
                            for (int i = 0; i < ArraycityList.size(); i++) {
                                if (j1 < l111) {
                                    if (ArraycityList.get(i).getCity_id().equals(words1[j1])) {
                                        checkSelected[i] = true;
                                        s1.append(ArraycityList.get(i).getCity_name()+",");
                                        j1++;
                                    } else {
                                        checkSelected[i] = false;

                                    }

                                }
                            }
                            int l3 = checkSelected.length;
                            System.out.println(l3);
                        } catch (Exception e) {
                            System.out.println(e);
                        }

                        initialize(cityList, ArraycityList);
                        cityPreferences=getSharedPreferences("MyExpCity",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor1 = cityPreferences.edit();
                        editor1.putString("CITY_IDS", expenseEntryObj.getCityId());
//                editor.putString("CITY_NAMES", dsr.getCityName());
                        editor1.commit();
                        city.setText(loadActivity.getSelected(CityDropDownListAdapter.trimEnd(s1.toString().trim(), ',')));

                    }
                }


            }

            }

        }



    ExpenseEntry getExpenseEntry() {
        String url = "http://" + server + "/And_Sync.asmx/xJSGetExpAdvance?id=" + expId;
        JSONParser jParser = new JSONParser();
        ExpenseEntry ExpenseEntry = null;
        String result = jParser.getJSONArray(url);
        if (result != null) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    ExpenseEntry = new ExpenseEntry();
                    ExpenseEntry.setFromDate(obj.getString("Fdt"));
                    ExpenseEntry.setToDate(obj.getString("Tdt"));
                    ExpenseEntry.setTimeFrom(obj.getString("ftime"));
                    ExpenseEntry.setTimeTo(obj.getString("ttime"));
                    ExpenseEntry.setRemarks(obj.getString("Rmk"));
                    ExpenseEntry.setBillAmount(obj.getString("Amt"));
                    ExpenseEntry.setCityId(obj.getString("City"));
                    ExpenseEntry.setStateid(obj.getString("State"));

                }
            } catch (Exception e) {
                System.out.println(e);
                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }
            }

        }
        return ExpenseEntry;
    }


    class SaveExpense extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(AdvanceExpenseRequest.this,"saving Data", "Saving Data Please wait...", true);
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                if(expenseEntry!=null)
                {
                    delResp=insertExpense(expenseEntry);

                }

            } catch (Exception e) {
                if(progressDialog.isShowing())
                {
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
            if(progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
            try {
                System.out.println("Result");
                JSONArray jsonarray = new JSONArray(result);
                JSONObject json = null;
                for (int k = 0; k < jsonarray.length(); k++) {
                    json = jsonarray.getJSONObject(k);
                    // System.out.println(json);
                }
                int st;
                try {
                    st = Integer.parseInt(json.getString("St"));
                    //System.out.println(st);
                    if (st > 0 && Integer.parseInt(expId)>0) {
                        progressDialog.dismiss();
                        new Custom_Toast(getApplicationContext(), "Record Updated Successfully").showCustomAlert();
                        clearData();
                    }
                    else   if (st > 0) {
                        progressDialog.dismiss();
                 new Custom_Toast(getApplicationContext(), "Record Inserted Successfully").showCustomAlert();
                        clearData();

                    }

                    else if (st == 0) {
                        progressDialog.dismiss();
                   new Custom_Toast(getApplicationContext(), "Record not Inserted").showCustomAlert();
                    }

                } catch (Exception e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }


                    System.out.println(e);

                }

            }catch (Exception e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }


                System.out.println(e);

            }
        }
    }
            void clearData()
            {
            fromDate.setText(null);
                toDate.setText(null);
                fromDate.setFocusable(true);
                fromTimeSpinner.setSelection(20);
                toTimeSpinner.setSelection(36);
                amt.setText(null);
                remark.setText(null);
                tv1.setText(null);
                tv2.setText(null);
                expId="0";
                fromFind=false;
                save.setImageResource(R.drawable.save1);
                delete.setVisibility(View.GONE);
                tv2.setText("None selected");
                initializeState(stateArrayList, stateList);
                checkStateSelected=null;
                checkStateSelected = new boolean[stateList.size()];
                for (int i = 0; i < checkStateSelected.length; i++) {
                    checkStateSelected[i] = false;
                }
                tv1.setClickable(true);

                tv1.setText("None selected");
                try {
                    cityList.clear();
                    ArraycityList.clear();
                    initialize(cityList, ArraycityList);
                    checkSelected = new boolean[cityList.size()];
                    for (int i = 0; i < checkSelected.length; i++) {
                        checkSelected[i] = false;
                    }
//                tv1.setClickable(true);
                }
                catch (Exception e)
                {}
                dbCon.open();
                save.setEnabled(dbCon.ButtonEnable("AdvanceExpenseRequest","Expense","Add"));
                if(!dbCon.ButtonEnable("AdvanceExpenseRequest","Expense","Add"))
                {
                    save.setColorFilter(Color.parseColor("#808080"));
                }
                dbCon.close();
            }

    String insertExpense(ExpenseEntry expense)
    {
        boolean mSuccess=false;String response1="";
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://"+server+"/And_Sync.asmx/xJSInsertExpenseAdvanceReq");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
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
            System.out.println("Send Data on Server"+"ExpAdvId"+expense.getExpCode()+"FromDate"+(expense.getFromDate()==null?"":expense.getFromDate())+"ToDate"+(expense.getToDate()==null?"":expense.getToDate())+"FromTime"+(expense.getTimeFrom()==null?"":expense.getTimeFrom())+"ToTime"+(expense.getTimeTo()==null?"":expense.getTimeTo())+"Amount"+expense.getBillAmount()+"Remarks"+expense.getRemarks()+"StateId"+expense.getStateid()+"CityId"+expense.getCityId()+"UserId"+userId);
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
              response1 = httpclient.execute(httppost, responseHandler);
            // System.out.println("Response : " + response1);
//					JSONObject json = new JSONObject(response1);

        }
        catch(Exception e){
            System.out.println("Exception : " + e.getMessage());
            mSuccess=true;
        }
        return response1;
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
        if(id== R.id.fromDate)
        {
            date.setCallBack(ondate1);
            date.show(getSupportFragmentManager(), "Date Picker");
        }
        else if(id== R.id.toDate)
        {
            date.setCallBack(ondate2);
            date.show(getSupportFragmentManager(), "Date Picker");
        }

    }


    DatePickerDialog.OnDateSetListener ondate1 = new DatePickerDialog.OnDateSetListener() {
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
            Date filledDate = null;
            try {
                filledDate = format2.parse(format2.format(date));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (!toDate.getText().toString().isEmpty()) {
                long f = DateFunction.ConvertDateToTimestamp(toDate.getText().toString());
                if (filledDate.getTime() <= f) {
                    fromDate.setText(format2.format(date));

                } else {
                    dialogWithOutView = AlertOkDialog.newInstance("From Date Should be less than To Date");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }

//            fromDate.setText(format2.format(date));
            }

        else{
                fromDate.setText(format2.format(date));
        }
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


            f = DateFunction.ConvertDateToTimestamp(fromDate.getText().toString());
            if(filledDate.getTime()>=f)
            {
                toDate.setText(format2.format(date));

            }

            else{
                dialogWithOutView= AlertOkDialog.newInstance("From Date Should be less than To Date");
                dialogWithOutView.show(getFragmentManager(), "Info");
            }

        }
    };

    private void initialize(ArrayList<String> cityItems, ArrayList<City> ArraycityList) {

        final ArrayList<String> items = cityItems;
        final ArrayList<City> ArraycityList1 = ArraycityList;


        city = (EditText) findViewById(R.id.SelectBox);
        city.setInputType(InputType.TYPE_NULL);
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!expanded) {
                    //display all selected values
                    String selected = "";
                    String firstSelectedId = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelected[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }
                    if (flag == 1)
                        city.setText(CityDropDownListAdapter.trimEnd(selected.trim(), ','));
                    expanded = true;
                } else {
                    city.setText(CityDropDownListAdapter.getSelected());
                    expanded = false;
                }
            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUp(items, city, ArraycityList1);


            }
        });

    }


    private void initiatePopUp(ArrayList<String> items, TextView tv, ArrayList<City> ArraycityList1) {
        LayoutInflater inflater = (LayoutInflater) AdvanceExpenseRequest.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.pop_up_window, (ViewGroup) findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        final RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.relativeLayout1);
        pw = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw.setOutsideTouchable(true);
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//    				pw.dismiss();
                    //pw.showAsDropDown(layout1);
                    return true;
                }
                pw.showAsDropDown(layout1);
                return false;
            }
        });

        //provide the source layout for drop-down
        pw.setContentView(layout);
       // System.out.println(layout);
        //anchor the drop-down to bottom-left corner of 'layout1'
        try {
            pw.showAsDropDown(layout1);
            //pw.showAtLocation(layout1,Gravity.BOTTOM, 0, layout1.getHeight());
          //  System.out.println(layout1);
        } catch (Exception e) {
            System.out.println(e);
        }
//    	populate the drop-down list
        ListView list = (ListView) layout.findViewById(R.id.dropDownList);
//    		final CityDropDownListAdapter adapter = new CityDropDownListAdapter(this, items, tv,ArraycityList1);
        adapter = new CityDropDownListAdapter(this, items, tv, ArraycityList1);

        list.setAdapter(adapter);

    }

    private void initializeState(ArrayList<String> stateItems, ArrayList<State> ArraystateList) {

        final ArrayList<String> stateArrayList = stateItems;
        final ArrayList<State> stateList = ArraystateList;
        state = (EditText) findViewById(R.id.SelectStateBox);
        state.setInputType(InputType.TYPE_NULL);

        state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!expanded) {
                    //display all selected values
                    String selected = "";
                    String firstSelectedId = "";
                    int flag = 0;
                    for (int i = 0; i < stateArrayList.size(); i++) {
                        if (checkStateSelected[i] == true) {
                            selected += stateArrayList.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }
                    if (flag == 1)
                        state.setText(StateDropDownListAdapter.trimEnd(selected.trim(), ','));
                    expanded = true;
                } else {
                    state.setText(StateDropDownListAdapter.getSelected());
                    expanded = false;
                }
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiateStatePopUp(stateArrayList, state, stateList);


            }
        });

    }


    private void initiateStatePopUp(ArrayList<String> items, TextView tv, ArrayList<State> ArraycityList1) {
        LayoutInflater inflater = (LayoutInflater) AdvanceExpenseRequest.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.pop_up_window, (ViewGroup) findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        final RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.relativeLayoutState);
        pw1 = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw1.setBackgroundDrawable(new BitmapDrawable());
        pw1.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw1.setOutsideTouchable(true);
        pw1.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw1.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//    				pw.dismiss();
                    //pw.showAsDropDown(layout1);
                    return true;
                }
                pw1.showAsDropDown(layout1);
                return false;
            }
        });

        //provide the source layout for drop-down
        pw1.setContentView(layout);
       // System.out.println(layout);
        //anchor the drop-down to bottom-left corner of 'layout1'
        try {
            pw1.showAsDropDown(layout1);
            //pw.showAtLocation(layout1,Gravity.BOTTOM, 0, layout1.getHeight());
           // System.out.println(layout1);
        } catch (Exception e) {
            System.out.println(e);
        }
//    	populate the drop-down list
        ListView list = (ListView) layout.findViewById(R.id.dropDownList);
//    		final CityDropDownListAdapter adapter = new CityDropDownListAdapter(this, items, tv,ArraycityList1);
        stateDropDownListAdapter = new StateDropDownListAdapter(this, items, tv, ArraycityList1,AdvanceExpenseRequest.this);
        list.setAdapter(stateDropDownListAdapter);

    }

    @Override
    public void getCityUpdate(String stateId) {
        tv1.setText("None selected");
        stId=stateId;

        if (connectionDetector.isConnectingToInternet())
        {
            new GetCityList().execute();

        }
        else{

            dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }
    }


    class GetCityList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(AdvanceExpenseRequest.this,"Loading Data", "Loading Data Please wait...", true);


        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                if(ArraycityList!=null)
                {
                    ArraycityList.clear();
                }
                ArraycityList=getCityList(stId);

            } catch (Exception e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if(cityList!=null)
            {
                cityList.clear();
            }


            cityList = new ArrayList<String>();
            for (
                    int i = 0;
                    i < ArraycityList.size(); i++)

            {
                cityList.add(ArraycityList.get(i).getCity_name().toUpperCase());
            }



            initialize(cityList, ArraycityList);
            checkSelected=null;
            checkSelected = new boolean[cityList.size()];
            for (
                    int i = 0;
                    i < checkSelected.length; i++)

            {
                checkSelected[i] = false;
            }



            }


    }

    class GetStates extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            getStateprogressDialog= ProgressDialog.show(AdvanceExpenseRequest.this,"Loading Data", "Loading Data Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                if(stateList!=null)
                {
                    stateList.clear();
                }

                stateList = getStateList();

            } catch (Exception e) {
                if(getStateprogressDialog!=null)
                {
                    getStateprogressDialog.dismiss();
                }
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if(getStateprogressDialog!=null)
            {
                getStateprogressDialog.dismiss();
            }
            if (stateList != null) {
                if(stateArrayList!=null)
                {
                    stateArrayList.clear();
                }
                stateArrayList = new ArrayList<String>();
                for (int i = 0; i < stateList.size(); i++) {
                    stateArrayList.add(stateList.get(i).getState_name());
                }

                initializeState(stateArrayList, stateList);
                checkStateSelected=null;
                checkStateSelected = new boolean[stateList.size()];
                for (int i = 0; i < checkStateSelected.length; i++) {
                    checkStateSelected[i] = false;
                }


            }
        }
    }

        ArrayList<City> getCityList(String stId) {
            ArrayList<City> cityLists = new ArrayList<City>();
            String url="";
            url = "http://" + server + "/And_Sync.asmx/xJSCityForExpense?stateid=" + stId +"&SMID="+smid+"&Expensetype=0";
            JSONParser jParser = new JSONParser();
           String result = jParser.getJSONArray(url);
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
                    System.out.println(e);
                }


            }

            return cityLists;
        }
        ArrayList<State> getStateList() {
            ArrayList<State> stateLists = new ArrayList<State>();
            String url = "";

                url = "http://" + server + "/And_Sync.asmx/xJSStateForExpense?&SMID=" + smid + "&Expensetype=0";

            JSONParser jParser = new JSONParser();
            //********* Commented By Sandeep Singh 04-03-16     ***********//
                    //*********** START ************//
            /*
            State stateList1 = new State();
            stateList1.setState_id("0");
            stateList1.setState_name("Select State");
             stateLists.add(stateList1);
            */
            //*********** START ************//
            //********* Commented By Sandeep Singh 04-03-16     ***********//

            String result = jParser.getJSONArray(url);
            if (result != null) {
                try {
                    JSONArray jsonarray = new JSONArray(result);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        State stateList = new State();
                        stateList.setState_id(obj.getString("id"));
                        stateList.setState_name(obj.getString("Nm"));
                        stateLists.add(stateList);
                    }
                } catch (Exception e) {
                    if(getStateprogressDialog.isShowing())
                    {
                        getStateprogressDialog.dismiss();
                    }
                    e.printStackTrace();

                }
            }

            return stateLists;
        }

    ArrayList<String> getTimeList()
    {
        ArrayList<String> timeList=new  ArrayList<String>();
        timeList.add("12:00am");
        timeList.add("12:30am");
        timeList.add("01:00am");
        timeList.add("01:30am");
        timeList.add("02:00am");
        timeList.add("02:30am");
        timeList.add("03:00am");
        timeList.add("03:30am");
        timeList.add("04:00am");
        timeList.add("04:30am");
        timeList.add("05:00am");
        timeList.add("05:30am");
        timeList.add("06:00am");
        timeList.add("06:30am");
        timeList.add("07:00am");
        timeList.add("07:30am");
        timeList.add("08:00am");
        timeList.add("08:30am");
        timeList.add("09:00am");
        timeList.add("09:30am");
        timeList.add("10:00am");
        timeList.add("10:30am");
        timeList.add("11:00am");
        timeList.add("11:30am");
        timeList.add("12:00pm");
        timeList.add("12:30pm");
        timeList.add("01:00pm");
        timeList.add("01:30pm");
        timeList.add("02:00pm");
        timeList.add("02:30pm");
        timeList.add("03:00pm");
        timeList.add("03:30pm");
        timeList.add("04:00pm");
        timeList.add("04:30pm");
        timeList.add("05:00pm");
        timeList.add("05:30pm");
        timeList.add("06:00pm");
        timeList.add("06:30pm");
        timeList.add("07:00pm");
        timeList.add("07:30pm");
        timeList.add("08:00pm");
        timeList.add("08:30pm");
        timeList.add("09:00pm");
        timeList.add("09:30pm");
        timeList.add("10:00pm");
        timeList.add("10:30pm");
        timeList.add("11:00pm");
        timeList.add("11:30pm");
        return timeList;
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(AdvanceExpenseRequest.this, DashBoradActivity.class);
        startActivity(i);
        super.onBackPressed();
    }


    @Override
    public void onDialogPositiveWithoutViewClick(DialogFragment dialog) {
// TODO Auto-generated method stub

        if (connectionDetector.isConnectingToInternet())

        {
            new DeleteExpenseGrp().execute();

        }
        else{

            dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

    }


    @Override
    public void onDialogNegativeWithoutViewClick(DialogFragment dialog) {
// TODO Auto-generated method stub
        alertMessage.dismiss();

    }
    class DeleteExpenseGrp extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(AdvanceExpenseRequest.this,"Delete Expense", "Deleting Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                delResp=deletetExpenseGroup();

            } catch (Exception e) {
                if(progressDialog!=null)
                {
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

            if(delResp!=null)
            {
                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }
                new Custom_Toast(getApplicationContext(), delResp.replaceAll("\"","")).showCustomAlert();
                clearData();
            }

        }
    }

    String deletetExpenseGroup()
    {
        boolean mSuccess=false;String response1="";
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://"+server+"/And_Sync.asmx/xJSExpenseDeleteAdvanceReq");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("Id",String.valueOf(expId)));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response1 = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response1);

        }
        catch(Exception e)
        {
            delResp="";
            if(progressDialog!=null)
            {
                progressDialog.dismiss();
            }

            mSuccess=true;
            System.out.println(e);

        }

        return response1;
    }
    }


