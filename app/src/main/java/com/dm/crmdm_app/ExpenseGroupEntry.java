package com.dm.crmdm_app;

import android.app.DatePickerDialog;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.library.AlertMessage;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.library.DateFunction;
import com.dm.library.DatePickerFragmentRange;
import com.dm.library.DbCon;
import com.dm.library.IntentSend;
import com.dm.model.AppData;
import com.dm.model.Expense;

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

public class ExpenseGroupEntry extends AppCompatActivity implements AlertMessage.NoticeDialogListenerWithoutView {
    EditText grpName,fromDate,toDate,remark;ImageView take_picture,findButton,save,cancel,delete;
    long f,t;ProgressDialog progressDialog;Expense expense;AppDataController appDataController1;ArrayList<AppData> appDataArray;
    String server;SharedPreferences preferences1;AlertOkDialog alertOkDialog;Intent intent;Bundle bundle;int grpId=0;
    AlertOkDialog dialogWithOutView;ConnectionDetector connectionDetector;String delResp="";
    AlertMessage alertMessage;
    DbCon dbCon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_group_entry);
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
        tv.setText("Add Expense Group");
        grpName=(EditText)findViewById(R.id.grpName);
        fromDate=(EditText)findViewById(R.id.fromDate);
        toDate=(EditText)findViewById(R.id.toDate);
        remark=(EditText)findViewById(R.id.remark);
        take_picture = (ImageView)findViewById(R.id.takePicture);
        findButton=(ImageView)findViewById(R.id.findbutton1);
        save=(ImageView) findViewById(R.id.button1);
        cancel=(ImageView) findViewById(R.id.button2);
        delete=(ImageView) findViewById(R.id.button3);
        take_picture.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        fromDate.setInputType(InputType.TYPE_NULL);
        fromDate.setFocusable(false);
        toDate.setInputType(InputType.TYPE_NULL);
        toDate.setFocusable(false);
        dbCon=new DbCon(getApplicationContext());
        appDataController1=new AppDataController(ExpenseGroupEntry.this);
         preferences1 = getSharedPreferences("MyPref", MODE_PRIVATE);
        connectionDetector=new ConnectionDetector(getApplicationContext());

        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();
        intent=getIntent();
        bundle=intent.getExtras();
        if(bundle!=null)
        {
            grpId=bundle.getInt("EXP_GRP_ID",0);
        }

        if(grpId==0)
        {

            grpName.setText("");
            fromDate.setText("");
            toDate.setText("");
            remark.setText("");
            save.setImageResource(R.drawable.save1);
            delete.setVisibility(View.GONE);
            dbCon.open();
            save.setEnabled(dbCon.ButtonEnable("AddExpense","Expense","Add"));
            if(!dbCon.ButtonEnable("AddExpense","Expense","Add"))
            {
                save.setColorFilter(Color.parseColor("#808080"));
            }

            dbCon.close();


        }
        else{
            if(bundle!=null) {
                grpName.setText(bundle.getString("EXP_GRP_NAME",""));
                fromDate.setText(bundle.getString("EXP_GRP_FDATE",""));
                toDate.setText(bundle.getString("EXP_GRP_TDATE",""));
                remark.setText(bundle.getString("EXP_GRP_REM",""));
                save.setImageResource(R.drawable.update);
                delete.setVisibility(View.VISIBLE);
                dbCon.open();
                save.setEnabled(dbCon.ButtonEnable("AddExpense","Expense","Edit"));
                if(!dbCon.ButtonEnable("AddExpense","Expense","Edit"))
                {
                    save.setColorFilter(Color.parseColor("#808080"));
                }
                dbCon.close();
                dbCon.open();
                delete.setEnabled(dbCon.ButtonEnable("AddExpense","Expense","Delete"));
                if(!dbCon.ButtonEnable("AddExpense","Expense","Delete"))
                {
                    delete.setColorFilter(Color.parseColor("#808080"));
                }
                dbCon.close();
            }
        }


        fromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = ExpenseGroupEntry.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker2(v.getId());
            }
        });

        toDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = ExpenseGroupEntry.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker(v.getId());
            }
        });
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                saveData();

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clearData();

            }
        });
        findButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                (new IntentSend(getApplicationContext(),AddExpense.class)).toSendAcivity();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub




                if (connectionDetector.isConnectingToInternet())

                {
                    alertMessage = AlertMessage.newInstance(
                            "Do you want to delete ?", "delete", "cancel");
                    alertMessage.show(getFragmentManager(), "delete");

                }
                else{

                    dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }

            }
        });

    }
    private void showDatePicker2(int id) {
        DatePickerFragmentRange date = new DatePickerFragmentRange();
        /**
         * Set Up Current Date Into dialog
         */

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        args.putInt("grace", 1);
        args.putString("Type", "month");
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        if(id== R.id.fromDate)
        {
            date.setCallBack(ondate);
            date.show(getSupportFragmentManager(), "Date Picker");
        }
    }
    private void showDatePicker(int id) {
        DateAndTimePicker date = new DateAndTimePicker();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
            date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
//        if(id==R.id.fromDate)
//        {
//            date.setCallBack(ondate);
//            date.show(getSupportFragmentManager(), "Date Picker");
//        }
//        else
        if(id== R.id.toDate)
        {
            date.setCallBack(ondate1);
            date.show(getSupportFragmentManager(), "Date Picker");
        }


    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
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

            Date filledDate = null;
            try {
                filledDate = format2.parse(format2.format(date));

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(filledDate.getTime()<(System.currentTimeMillis()))
            {
                fromDate.setText(format2.format(date));
            }

            else{
                alertOkDialog= AlertOkDialog.newInstance("Can't fill future date");
                alertOkDialog.show(getFragmentManager(), "Info");
            }

        }
    };
    DatePickerDialog.OnDateSetListener ondate1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String strDate = (dayOfMonth < 10 ? ("0" + dayOfMonth) : (dayOfMonth)) + "/" + (((monthOfYear + 1) < 10 ? ("0" + (monthOfYear + 1)) : (monthOfYear + 1))) + "/" + year;
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MMM/yyyy");
//				 SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = format1.parse(strDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(format2.format(date));
            toDate.setText(format2.format(date));
        }
    };


    public void saveData()
    {
        if(grpName.getText().toString().isEmpty())
        {
            new Custom_Toast(getApplicationContext(), "Please Enter Expense Group Name").showCustomAlert();
        }
        else if(fromDate.getText().toString().isEmpty())
        {
            new Custom_Toast(getApplicationContext(), "Please Select Effective Date From").showCustomAlert();
        }
        else if(toDate.getText().toString().isEmpty())
        {
            new Custom_Toast(getApplicationContext(), "Please Select Effective Date To").showCustomAlert();
        }
        else {
            f = DateFunction.ConvertDateToTimestamp(fromDate.getText().toString());
            t = DateFunction.ConvertDateToTimestamp(toDate.getText().toString());
            if (f > t) {
                new Custom_Toast(getApplicationContext(), "From Date should be less than To Date").showCustomAlert();

            } else {
                long i = t - f;
                 String thirty="2592000000";
                if (i > Long.parseLong(thirty)) {
                    new Custom_Toast(getApplicationContext(), "From Date and To Date difference should be less than or equal to 30 days").showCustomAlert();
                } else {

                    expense=new Expense();
                    expense.setId(String.valueOf(grpId));
                    expense.setName(grpName.getText().toString());
                    expense.setFromDate(fromDate.getText().toString());
                    expense.setToDate(toDate.getText().toString());
                    expense.setRemark((remark.getText().toString().isEmpty()?"":remark.getText().toString()));

                    if (connectionDetector.isConnectingToInternet())

                    {
                        new SaveExpenseGrp().execute();

                    }
                    else{

                        dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                        dialogWithOutView.show(getFragmentManager(), "Info");
                    }



                }

            }
        }
        }


    class DeleteExpenseGrp extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(ExpenseGroupEntry.this,"Delete Expense", "Deleting Please wait...", true);

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
                new Custom_Toast(getApplicationContext(), delResp).showCustomAlert();
                (new IntentSend(getApplicationContext(),AddExpense.class)).toSendAcivity();
                finish();

            }

        }
    }

    class SaveExpenseGrp extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(ExpenseGroupEntry.this,"saving Data", "Saving Data Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
insertExpenseGroup(expense);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

        }
    }


   boolean insertExpenseGroup(Expense expense)
   {
      boolean mSuccess=false;
       try{
           HttpClient httpclient = new DefaultHttpClient();
           HttpPost httppost = new HttpPost("http://"+server+"/And_Sync.asmx/xJSInsertExpenseGroup");
           ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
           nameValuePairs.add(new BasicNameValuePair("id",expense.getId()));
           nameValuePairs.add(new BasicNameValuePair("Name",expense.getName().replaceAll("'","''")));
           nameValuePairs.add(new BasicNameValuePair("Remarks",expense.getRemark().replaceAll("'","''")));
           nameValuePairs.add(new BasicNameValuePair("EffDateFrom",expense.getFromDate()));
           nameValuePairs.add(new BasicNameValuePair("EffDateTo",expense.getToDate()));
           nameValuePairs.add(new BasicNameValuePair("UserID",preferences1.getString("USER_ID","0")));
           nameValuePairs.add(new BasicNameValuePair("SMID",preferences1.getString("CONPERID_SESSION","0")));
           httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
           ResponseHandler<String> responseHandler = new BasicResponseHandler();
           final String response1 = httpclient.execute(httppost, responseHandler);
           System.out.println("Response : " + response1);
//					JSONObject json = new JSONObject(response1);
           JSONArray jsonarray = new JSONArray(response1);
           JSONObject json=null;
           for (int k = 0; k < jsonarray.length(); k++) {
               json = jsonarray.getJSONObject(k);
               System.out.println(json);
           }
           int st;
           try{
               st = Integer.parseInt(json.getString("St"));
               System.out.println(st);
               if(st>0)
               {
                   progressDialog.dismiss();
//                   new Custom_Toast(getApplicationContext(), "Expense Group saved successfully").showCustomAlert();
//                   alertOkDialog = AlertOkDialog.newInstance("Expense Group saved successfully");
//                   alertOkDialog.show(getFragmentManager(), "Msg");
                   Bundle bundle=new Bundle();
                   bundle.putInt("EXP_GRP_ID",st);
                   bundle.putString("EXP_GRP_NAME",expense.getName().replaceAll("'","''"));
                   bundle.putString("EXP_GRP_FDATE",expense.getFromDate());
                   bundle.putString("EXP_GRP_TDATE",expense.getToDate());
//                   bundle.putBoolean("MODE",false);
                   Intent intent=new Intent(ExpenseGroupEntry.this,ExpenseSummaryScreen.class);
                   intent.putExtras(bundle);
                   startActivity(intent);
//                   (new IntentSend(getApplicationContext(),ExpenseSummaryScreen.class)).toSendAcivity();

               }
               else if(st==0)
               {
                   progressDialog.dismiss();
//                   new Custom_Toast(getApplicationContext(), "Expense Group not saved").showCustomAlert();
                   alertOkDialog = AlertOkDialog.newInstance("Expense Group not saved");
                   alertOkDialog.show(getFragmentManager(), "error");
               }
               else if(st== -1)
               {
                   progressDialog.dismiss();
                   alertOkDialog = AlertOkDialog.newInstance("Duplicate Group Name Exists");
                   alertOkDialog.show(getFragmentManager(), "error");

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


    String deletetExpenseGroup()
    {
        boolean mSuccess=false;String response1="";
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://"+server+"/And_Sync.asmx/xJSExpenseGroupDelete");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("ExpGrpId",String.valueOf(grpId)));
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

   void clearData()
    {
        grpName.setText("");
        fromDate.setText("");
        toDate.setText("");
        remark.setText("");
        save.setImageResource(R.drawable.save1);
        delete.setVisibility(View.GONE);
        dbCon.open();
        save.setEnabled(dbCon.ButtonEnable("AddExpense","Expense","Add"));
        if(!dbCon.ButtonEnable("AddExpense","Expense","Add"))
        {
            save.setColorFilter(Color.parseColor("#808080"));
        }
        dbCon.close();
    }



    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        (new IntentSend(getApplicationContext(),AddExpense.class)).toSendAcivity();

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
    }

