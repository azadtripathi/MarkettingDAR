package com.dm.crmdm_app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.dm.controller.AppDataController;
import com.dm.library.ConnectionDetector;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.library.Validation;
import com.dm.model.AppData;
import com.dm.model.Owner;
import com.dm.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dataman on 11/21/2017.
 */

public class CrmPayment extends AppCompatActivity {
    String currentDate;
    ConnectionDetector connectionDetector;
    ArrayAdapter<String> adapter;
    ProgressDialog progressDialog;
    String SMID;
    EditText amtChq,dateTextOnDsr,bankName,remark,bankAddressEditText,editTextinstumentNumber;
    SharedPreferences preferences2;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    String server;String Flag;
    RadioGroup radioGrpMode;
    Button buttonSave,buttonCancel;
    LinearLayout NotCashLinearLayout;
    Validation validation;
    String latitude,longitude,latlngtime;
    String Contact_id="",Ref_DocID="",TDocID="";
    String amount="",instumentDate="",bankNameString="",bankAddress="",remarkString="",instumentNumber="",TransactionMode="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector = new ConnectionDetector(this);
        if (connectionDetector.isConnectingToInternet()) {
            preferences2 = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            SMID = preferences2.getString("CONPERID_SESSION", null);
            appDataController1 = new AppDataController(CrmPayment.this);
            AppData appData;
            appDataController1.open();
            appDataArray = appDataController1.getAppTypeFromDb();
            appDataController1.close();
            appData = appDataArray.get(0);
            server = appData.getCompanyUrl();

            this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                    WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            WindowManager.LayoutParams params = this.getWindow().getAttributes();
            params.alpha = 1.0f;
            params.dimAmount = 0.5f;
            this.getWindow().setAttributes((WindowManager.LayoutParams) params);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;
            this.getWindow().setGravity(Gravity.CENTER);

            int reduceheight=height*18/100;
            int reducewidth=width*15/100;
            System.out.println("Height@:"+height+"-"+reduceheight+"-"+width+"-"+reducewidth);
            //this.getWindow().setLayout(width-reducewidth,height-reduceheight);
            this.getWindow().setLayout(width-reducewidth,((WindowManager.LayoutParams) params).WRAP_CONTENT);
            setContentView(R.layout.activity_payment);
            NewLocationService track = new NewLocationService(this);
            if(track.canGetLocation)
            {
                latitude = track.getLatitude();
                longitude = track.getLongitude();
                latlngtime = track.getLatLngTime();
            }
            else
            {
                latitude = track.getLatitude();
                longitude = track.getLongitude();
                latlngtime = track.getLatLngTime();
            }
            radioGrpMode=(RadioGroup)findViewById(R.id.radioGrpMode);
            NotCashLinearLayout=(LinearLayout)findViewById(R.id.NotCashLinearLayout);
            amtChq=(EditText)findViewById(R.id.amtChq);
            editTextinstumentNumber=(EditText)findViewById(R.id.chqdraNo);
            bankAddressEditText=(EditText)findViewById(R.id.branchAddressName);
            bankName=(EditText)findViewById(R.id.bankName);
            remark=(EditText)findViewById(R.id.narrationChq);
            buttonSave=(Button)findViewById(R.id.asSubmit);
            buttonCancel=(Button)findViewById(R.id.asCancel);
            dateTextOnDsr=(EditText)findViewById(R.id.dateTextOnDsr);
            dateTextOnDsr.setInputType(InputType.TYPE_NULL);
            dateTextOnDsr.setFocusable(false);
            validation=new Validation(CrmPayment.this);
            TransactionMode="RTGS";
            Intent intent=getIntent();
            if(intent !=null){
                Flag=intent.getStringExtra("Flag");
                Contact_id=intent.getStringExtra("Contact_id");
                Ref_DocID=intent.getStringExtra("Ref_DocID");
                TDocID=intent.getStringExtra("TDocID");
                if(!TDocID.equalsIgnoreCase("")){
                    buttonSave.setText("Update");
                }
            }
            radioGrpMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(TDocID.equalsIgnoreCase("")){
//                        clearField();
                    }

                    if(checkedId == R.id.cashRadio)
                    {
                        dateTextOnDsr.setText(null);
                        bankName.setText(null);
                        bankAddressEditText.setText(null);
                        editTextinstumentNumber.setText(null);
                        disabledFields();


                    }
                    else  {

                        enabledFields();

                    }

                }


            });
            dateTextOnDsr.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    // TODO Auto-generated method stub
                    showDatePicker();
                }
            });
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mode1=((RadioButton)findViewById(radioGrpMode.getCheckedRadioButtonId())).getText().toString();
                    TransactionMode=mode1;
                    amount=validation.vAlNUmericFileds(amtChq.getText().toString());
                    remarkString=remark.getText().toString();
                    if(mode1.equals("Cash")) {
                        if(!amount.isEmpty() && amount != null && !amount.equalsIgnoreCase("")){
                            if(!remarkString.isEmpty() && remarkString != null && !remarkString.equalsIgnoreCase("")){
                                buttonSave.setEnabled(false);
                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        buttonSave.setEnabled(true);
                                    }
                                }, 2000);
                                new saveDataOnServer().execute();
                            }
                            else{
                                    new Custom_Toast(CrmPayment.this,"Please Enter Remark").showCustomAlert();
                            }
                        }
                        else{
                            new Custom_Toast(CrmPayment.this,"Please Enter Amount").showCustomAlert();
                        }

                    }
                    else {
                        boolean isCorrect=true;
                        String errorMsg="";
                        instumentNumber=validation.vAlfNum(editTextinstumentNumber.getText().toString());
                        instumentDate=validation.vAlfNum(dateTextOnDsr.getText().toString());
                        bankNameString=validation.vAlfNum(bankName.getText().toString());
                        bankAddress=validation.vAlfNum(bankAddressEditText.getText().toString());
                        if(amount.isEmpty() || amount == null || amount.equalsIgnoreCase("")){
                            isCorrect=false;
                            errorMsg="Please Enter Amount";
                        }
                        else if(instumentNumber.isEmpty() || instumentNumber == null || instumentNumber.equalsIgnoreCase("")){
                            isCorrect=false;
                            errorMsg="Please Enter Instrument Number";
                        }
                        else if(instumentDate.isEmpty() || instumentDate == null || instumentDate.equalsIgnoreCase("")){
                            isCorrect=false;
                            errorMsg="Please Enter Instrument Date";

                        }
                        else if(bankNameString.isEmpty() || bankNameString == null || bankNameString.equalsIgnoreCase("")){
                            isCorrect=false;
                            errorMsg="Please Enter Bank Name";
                        }
                        else if(bankAddress.isEmpty() || bankAddress == null || bankAddress.equalsIgnoreCase("")){
                            isCorrect=false;
                            errorMsg="Please Enter Bank Address";
                        }
                        else if(remarkString.isEmpty() || remarkString == null || remarkString.equalsIgnoreCase("")){
                            isCorrect=false;
                            errorMsg="Please Enter Remark";
                        }
                        if(isCorrect){
                            buttonSave.setEnabled(false);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    buttonSave.setEnabled(true);
                                }
                            }, 2000);
                            new saveDataOnServer().execute();
                        }
                        else {
                            new Custom_Toast(CrmPayment.this,errorMsg).showCustomAlert();
                        }

                    }
                }
            });

            if(!TDocID.equalsIgnoreCase("")){
                new getPaymentData().execute();
            }
        }
    }

    protected class getPaymentData extends AsyncTask<String, String, String> {
        String type = null, result = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CrmPayment.this, "Loading Data", "Loading...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                result = getPersonInfo();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            JSONArray jsonArray = null;
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (!result.isEmpty() && !(result == null)) {
                try {
                    jsonArray = new JSONArray(result);
                    String mode="",amount="",paymentDate="",chequeNo="",chequeDate="",bank="",branck="",remk="";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objs = null;
                        try {
                            objs = jsonArray.getJSONObject(i);
                            mode=objs.getString("Mode");
                            amount=objs.getString("Amount");
                            paymentDate=objs.getString("PaymentDt");
                            chequeNo=objs.getString("ChequeNo");
                            chequeDate=objs.getString("ChequeDt");
                            bank=objs.getString("Bank");
                            branck=objs.getString("Branch");
                            remk=objs.getString("Remarks");
                            SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                            SimpleDateFormat format2 = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);
                            Date  payment=new Date();
                            Date  cheqDate=new Date();
                            try{
                                payment =  format1.parse(paymentDate);
                                cheqDate=format1.parse(chequeDate);
                            }catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            RadioButton rbCheque = (RadioButton) findViewById(R.id.chqRadio);
                            RadioButton rbDraft = (RadioButton) findViewById(R.id.draftRadio);
                            RadioButton rbRTGS = (RadioButton) findViewById(R.id.rtgsRadio);
                            RadioButton rbCash = (RadioButton) findViewById(R.id.cashRadio);
                            if(objs.getString("Mode").equalsIgnoreCase("Cash")){
                                rbCash.setChecked(true);
                                rbRTGS.setChecked(false);
                                rbDraft.setChecked(false);
                                rbCheque.setChecked(false);
                                disabledFields();
                            }
                            else{
                               enabledFields();
                                if(objs.getString("Mode").equalsIgnoreCase("RTGS")){
                                    rbCash.setChecked(false);
                                    rbRTGS.setChecked(true);
                                    rbDraft.setChecked(false);
                                    rbCheque.setChecked(false);
                                }
                                else if(objs.getString("Mode").equalsIgnoreCase("Draft")){
                                    rbCash.setChecked(false);
                                    rbRTGS.setChecked(false);
                                    rbDraft.setChecked(true);
                                    rbCheque.setChecked(false);
                                }
                                else{
                                    rbCash.setChecked(false);
                                    rbRTGS.setChecked(false);
                                    rbDraft.setChecked(false);
                                    rbCheque.setChecked(true);
                                }
                            }
                            amtChq.setText(amount);
                            dateTextOnDsr.setText(format2.format(cheqDate));
                            bankName.setText(bank);
                            remark.setText(remk);
                            bankAddressEditText.setText(branck);
                            editTextinstumentNumber.setText(chequeNo);
                        } catch (JSONException e) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                new Custom_Toast(CrmPayment.this, "No Data Found").showCustomAlert();
            }

        }
    }
    public void clearField(){
        amtChq.setText(null);
        dateTextOnDsr.setText(null);
        bankName.setText(null);
        remark.setText(null);
        bankAddressEditText.setText(null);
        editTextinstumentNumber.setText(null);
    }
    public String getPersonInfo() {
        String url = "http://"+server+"/And_Sync.asmx/XjFindEditPaymentDetails_CRM?PaymentCollId="+TDocID;;
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }

    public class saveDataOnServer extends AsyncTask<String , Void ,String> {

        String server_response;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(CrmPayment.this,"Saving Data", "Please Wait...", true);
            SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
            Calendar cal=Calendar.getInstance();
            currentDate=df.format(cal.getTime());
            params.add(new BasicNameValuePair("collid",TDocID));
            params.add(new BasicNameValuePair("TDocId",Ref_DocID));
            params.add(new BasicNameValuePair("VDate",currentDate));
            params.add(new BasicNameValuePair("SMId",SMID));
            params.add(new BasicNameValuePair("contactid",Contact_id));
            params.add(new BasicNameValuePair("Mode",TransactionMode));
            params.add(new BasicNameValuePair("Amount",amount));
            params.add(new BasicNameValuePair("PaymentDate",currentDate));
            params.add(new BasicNameValuePair("Cheque_DDNo",instumentNumber));
            params.add(new BasicNameValuePair("Cheque_DD_Date",instumentDate));
            params.add(new BasicNameValuePair("Bank",bankNameString));
            params.add(new BasicNameValuePair("Branch",bankAddress));
            params.add(new BasicNameValuePair("Remarks",remarkString));
            params.add(new BasicNameValuePair("longitude",longitude));
            params.add(new BasicNameValuePair("latitude",latitude));
            params.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
        }
        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://"+server+"/And_Sync.asmx/xJSSavePaymentCollection_CRM"); // here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    server_response=sb.toString();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                if(progressDialog !=null){
                    progressDialog.dismiss();
                }
                return new String("Exception: " + e.getMessage());
            }

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(progressDialog !=null){
                progressDialog.dismiss();
            }
            // if(progressDialog.isShowing()){progressDialog.dismiss();}
            Log.e("Response", "" + server_response);
            if(server_response != null){
                if(!server_response.isEmpty()){
                    server_response=server_response.replaceAll("\"","");
                    if(server_response.equalsIgnoreCase("Yes")){
                        new Custom_Toast(CrmPayment.this,"Saved Sussesfully").showCustomAlert();
                      /*  Intent intent=new Intent(CRMNote.this,CRMStreamInfo.class);
                        intent.putExtra("Contact_id",Contact_id);
                        intent.putExtra("Flag",Flag);
                        startActivity(intent);*/
                        finish();
                    }else
                    {
                        new Custom_Toast(CrmPayment.this,"Try Again!"+server_response).showCustomAlert();
                    }
                }
            }
        }
    }
    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException
    {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params)
        {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }
    private void showDatePicker() {
        DateAndTimePicker date = new DateAndTimePicker();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        date.setCallBack(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                String strDate=(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth))+"/"+(((monthOfYear+1)<10?("0"+(monthOfYear+1)):(monthOfYear+1)))+"/"+year;
                SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat format2 = new SimpleDateFormat("dd/MMM/yyyy");
                Date date = null;
                try {
                    date = format1.parse(strDate);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                System.out.println(format2.format(date));
                dateTextOnDsr.setText(format2.format(date));

//			 dateTextOnDsr.setText((dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth))+"/"+(((monthOfYear+1)<10?("0"+(monthOfYear+1)):(monthOfYear+1)))+"/"+year);

            }
        });
        date.show(getSupportFragmentManager(), "Date Picker");
    }
    public void enabledFields()
    {
        NotCashLinearLayout.setVisibility(View.VISIBLE);
    }
    public void disabledFields()
    {
        NotCashLinearLayout.setVisibility(View.GONE);
    }


}