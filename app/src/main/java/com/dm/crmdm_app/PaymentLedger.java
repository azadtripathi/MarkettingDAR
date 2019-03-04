package com.dm.crmdm_app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.controller.AppDataController;
import com.dm.controller.FindPaymentController;
import com.dm.controller.PaymentLadgerController;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomAdapterPaymetLadger;
import com.dm.library.CustomFindContactAdapter;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.library.DateFunction;
import com.dm.library.DatePickerFragmentRange;
import com.dm.library.IntentSend;
import com.dm.library.Validation;
import com.dm.model.AppData;
import com.dm.parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by dataman on 1/4/2018.
 */

public class PaymentLedger extends AppCompatActivity {
    String server;
    ListView listView;
    private List<PaymentLadgerController> paymentLadgerControllers;
    private CustomAdapterPaymetLadger contactAdapter;
    private Random random;
    ProgressDialog progressDialog;
    boolean isLoadMoreData=true;
    String rowId="0";
    boolean flagIsFinish=true;
    String SMID,userId;
    SharedPreferences preferences2;
    EditText filterFromDate,filterToDate;
    String stringFilterFromDate="",stringFilterToDate="";
    TextView totalBalanceTextView;
    SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
    RecyclerView recyclerView;
    ConnectionDetector connectionDetector;
    AlertOkDialog dialogWithOutView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector=new ConnectionDetector(this);
        if(connectionDetector.isConnectingToInternet()){
        setContentView(R.layout.payment_ladger);
        getSupportActionBar().setCustomView(R.layout.payment_ladger_toolbar);
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
        tv.setText("Ledger");
        ImageView filterImageView=(ImageView)findViewById(R.id.filterImageView);
        filterImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flagIsFinish=false;
                openSettingFilter();
            }
        });
        Calendar cal1 = Calendar.getInstance();
        cal1.add(Calendar.DATE, -7);
        stringFilterFromDate = df.format(cal1.getTime());
        Calendar cal2 = Calendar.getInstance();
        stringFilterToDate = df.format(cal2.getTime());
        totalBalanceTextView=(TextView)findViewById(R.id.totalBalanceTextView);
        preferences2=this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SMID=preferences2.getString("CONPERID_SESSION", null);
        SharedPreferences preferences = getSharedPreferences("RETAILER_SESSION_DATA", MODE_PRIVATE);
        userId=preferences.getString("USER_ID", "");
        AppData appData;
        ArrayList<AppData> appDataArray = new ArrayList<>();
        AppDataController appDataController = new AppDataController(this);
        appDataController.open();
        appDataArray = appDataController.getAppTypeFromDb();
        appDataController.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();
        paymentLadgerControllers = new ArrayList<>();
        random = new Random();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new CustomAdapterPaymetLadger(recyclerView, paymentLadgerControllers, this);
        recyclerView.setAdapter(contactAdapter);
        new GetListAllData().execute();
       /* contactAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(isLoadMoreData)
                    new GetListAllData().execute();
            }
        });*/
        }else{
            dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

    }

    class GetListAllData extends AsyncTask<String, String, String> {
        String type=null; String result=null;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //arrayListData.clear();
        }
        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                result=null;
                result=getContactData();
            } catch (Exception e)
            {
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
            try {
                final JSONArray jsonarray = new JSONArray(result);
                if (jsonarray.length()>0) {
                    paymentLadgerControllers.add(null);
                    contactAdapter.notifyItemInserted(paymentLadgerControllers.size() - 1);
                  /*  new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {*/
                    paymentLadgerControllers.remove(paymentLadgerControllers.size() - 1);
                    contactAdapter.notifyItemRemoved(paymentLadgerControllers.size());

                    //Generating more data
                    int index = paymentLadgerControllers.size();
                    int end = index + 10;
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = null;
                        try {
                            obj = jsonarray.getJSONObject(i);
                            if(obj.has("rowid"))
                            rowId=obj.getString("rowid");
                            PaymentLadgerController ladgerController = new PaymentLadgerController();
                            ladgerController.setId(String.valueOf(i));
                           /* String date=null;
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            if(obj.has("vdate")){
                                try{
                                date=obj.getString("vdate");
                                Date f = sdf.parse(date);
                                date=sdf.format(f);}
                                catch (Exception e){}
                            }
                            if(date == null){
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                                date  = df.format(c.getTime());
                            }*/
                            ladgerController.setDate(obj.getString("vdate"));
                            if(obj.has("partculars"))
                            ladgerController.setParticuler(obj.getString("partculars"));
                            if(obj.has("Balance")){
                                totalBalanceTextView.setText(obj.getString("Balance"));
                              /*  String balance="0.00";
                                //ladgerController.setAmount(obj.getString("Balance"));
                                try{
                                    if(Double.parseDouble(obj.getString("Balance"))<0){
                                        //ladgerController.setAmount(obj.getString("Balance")+"Cr");
                                        balance="<html>"+String.format("%.2f",Double.parseDouble(obj.getString("Balance"))*-1)+" <b>Cr</b></html>";
                                    }
                                    else if(Double.parseDouble(obj.getString("Balance"))==0){
                                        balance=("Nil");
                                    }
                                    else if(Double.parseDouble(obj.getString("Balance"))>0){
                                        balance="<html>"+String.format("%.2f",Double.parseDouble(obj.getString("Balance")))+" <b>Dr</b></html>";
                                    }

                                }catch (NumberFormatException e){
                                    ladgerController.setAmount("Error: Balance is not in Correct Formate.");
                                }*/

                                //totalBalanceTextView.setText(Html.fromHtml(balance));
                            }
                            if(obj.has("amount")){
                                ladgerController.setAmount(obj.getString("amount"));
                            }


                            //ladgerController.setRemark("Lorem ipsum is a pseudo-Latin text used in web design, typography, layout, and printing in place of English to emphasise design elements over content. It's also called placeholder (or filler) text. It's a convenient tool for mock-ups. It helps to outline the visual elements of a document or presentation, eg typography, font, or layout. Lorem ipsum is mostly a part of a Latin text by the classical author and philosopher Cicero. Its words and letters have been changed by addition or removal, so to deliberately render its content nonsensical; it's not genuine, correct, or comprehensible Latin anymore. While lorem ipsum's still resembles classical Latin, it actually has no meaning whatsoever. As Cicero's text doesn't contain the letters K, W, or Z, alien to latin, these, and others are often inserted randomly to mimic the typographic appearence of European languages, as are digraphs not to be found in the original");
                            if(obj.has("type")){
                            ladgerController.setType(obj.getString("type"));}
                            else {
                                ladgerController.setType("");
                            }
                            paymentLadgerControllers.add(ladgerController);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    contactAdapter.notifyDataSetChanged();
                    contactAdapter.setLoaded();
                        /* }
                   }, 5000);*/
                } else {
                    isLoadMoreData=false;
                    //Toast.makeText(PaymentLedger.this, "Loading data completed", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e)
            {
                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }

                System.out.println(e);
            }
        }
    }
   /* class GetToatalBalance extends AsyncTask<String, String, String> {
        String type=null; String result=null;
        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                result=getToatalBal();
            } catch (Exception e)
            {
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
            try {
                final JSONArray jsonarray = new JSONArray(result);
                if (jsonarray.length()>0) {
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = null;
                        try {
                            obj = jsonarray.getJSONObject(i);
                            if(obj.has("Amtbalance"))
                                totalBalanceTextView.setText(obj.getString("Amtbalance"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }catch (Exception e)
            {
                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }

                System.out.println(e);
            }
            new GetListAllData().execute();
        }
    }*/
    public String getToatalBal(){
        String url="http://"+server+"/And_Sync.asmx/XjGetLedgerbalance_CRM?userid="+userId;
        //String url="http://"+server+"/And_Sync.asmx/XjGetUserLedger_CRM?userid="+userId+"";
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }
    public String getContactData(){
        String url="http://"+server+"/And_Sync.asmx/XjGetUserLedger_CRM?userid="+userId+"&rowid="+rowId+"&fromdate="+stringFilterFromDate+"&todate="+stringFilterToDate+"";
        //String url="http://"+server+"/And_Sync.asmx/XjGetUserLedger_CRM?userid="+userId+"";
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }
    /*void openSettingFilter() {
        // inflate your layout
        View myPopupView = getLayoutInflater().inflate(R.layout.filter_setting, null);

        // Create the popup window; decide on the layout parameters
        PopupWindow myPopupWindow = new PopupWindow(myPopupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT);

        // find and initialize your TextView(s), EditText(s) and Button(s); setup their behavior

        // display your popup window
        myPopupWindow.showAtLocation(myPopupView, Gravity.CENTER, 0, 0);
    }*/

    private void showFromDatePicker(int id) {
        DateAndTimePicker date = new DateAndTimePicker();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        if (id == R.id.fromDate) {
            date.setCallBack(ondate1);
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
            filterFromDate.setText(format2.format(date));

        }
    };

    private void showDatePickerToDate(int id) {
        DateAndTimePicker date = new DateAndTimePicker();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        if (id == R.id.toDate) {
            date.setCallBack(toDate);
            date.show(getSupportFragmentManager(), "Date Picker");
        }
    }
    DatePickerDialog.OnDateSetListener toDate = new DatePickerDialog.OnDateSetListener() {
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
            filterToDate.setText(format2.format(date));

        }
    };

    public void openSettingFilter() {


        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.filter_setting, null);
        dialogBuilder.setView(dialogView);

        filterFromDate = (EditText) dialogView.findViewById(R.id.fromDate);
        filterFromDate.setInputType(InputType.TYPE_NULL);
        filterFromDate.setFocusable(false);

        filterToDate = (EditText) dialogView.findViewById(R.id.toDate);
        filterToDate.setInputType(InputType.TYPE_NULL);
        filterToDate.setFocusable(false);
        Button filterSaveButton=(Button)dialogView.findViewById(R.id.asSubmit);
        if(stringFilterFromDate.equalsIgnoreCase("")) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -7);
            stringFilterFromDate = df.format(cal.getTime());
            filterFromDate.setText(stringFilterFromDate);
        }
        else{
            filterFromDate.setText(stringFilterFromDate);
        }
        if(stringFilterToDate.equalsIgnoreCase("")) {
            Calendar cal = Calendar.getInstance();
            stringFilterToDate = df.format(cal.getTime());
            filterToDate.setText(stringFilterToDate);
        }
        else{
            filterToDate.setText(stringFilterToDate);
        }
        filterFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFromDatePicker(v.getId());
            }
        });
        filterToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerToDate(v.getId());
            }
        });
        final  AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        filterSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long f = DateFunction.ConvertDateToTimestamp(filterFromDate.getText().toString());
                long t = DateFunction.ConvertDateToTimestamp(filterToDate.getText().toString());
                if (f <= t) {
                    totalBalanceTextView.setText("");
                    flagIsFinish=true;
                    rowId = "0";
                    stringFilterFromDate = filterFromDate.getText().toString().trim();
                    stringFilterToDate = filterToDate.getText().toString().trim();
                    //new Custom_Toast(PaymentLedger.this,"Save Click"+stringFilterFromDate+":"+stringFilterToDate).showCustomAlert();
                    recyclerView.setAdapter(null);
                    paymentLadgerControllers.clear();
                    contactAdapter = new CustomAdapterPaymetLadger(recyclerView, paymentLadgerControllers, PaymentLedger.this);
                    recyclerView.setAdapter(contactAdapter);
                    new GetListAllData().execute();
                    alertDialog.dismiss();
                }
                else
                {
                    new Custom_Toast(getApplicationContext(), "From Date should be less than To Date").showCustomAlert();
                }
            }
        });
    }
}
