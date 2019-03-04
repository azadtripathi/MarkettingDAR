package com.dm.crmdm_app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.controller.AppDataController;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomAdapterCRMStreamInfo;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.library.IntentSend;
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

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dataman on 1/4/2018.
 */

public class PaymentTransfer extends ActionBarActivity {
    String server;
    ProgressDialog progressDialog;
    Spinner spinnerOwner;
    SharedPreferences preferences2;
    String SMID,userId;
    ArrayList<Owner> ownerArray=new ArrayList<>();
    RadioGroup radioGrpMode;
    LinearLayout NotCashLinearLayout;
    Validation validation;
    EditText amtChq,dateTextOnDsr,bankName,remark,bankAddressEditText,editTextinstumentNumber;
    //ImageView save,cancel,delete,findButton;
    String amount="",instumentDate="",bankNameString="",bankAddress="",remarkString="",instumentNumber="",TransactionMode="";
    ImageView imageSigmature;
    Button buttonSave,buttonSignature;
    String latitude,longitude,latlngtime;
    String stringSignature="";
    ConnectionDetector connectionDetector;
    AlertOkDialog dialogWithOutView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector=new ConnectionDetector(this);
        if(connectionDetector.isConnectingToInternet()){
        setContentView(R.layout.payment_transfer);
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
        tv.setText("Payment Transfer");
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
        AppData appData;
        ArrayList<AppData> appDataArray = new ArrayList<>();
        AppDataController appDataController = new AppDataController(this);
        appDataController.open();
        appDataArray = appDataController.getAppTypeFromDb();
        appDataController.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();
        preferences2=this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SMID=preferences2.getString("CONPERID_SESSION", null);
        SharedPreferences preferences = getSharedPreferences("RETAILER_SESSION_DATA", MODE_PRIVATE);
        userId=preferences.getString("USER_ID", "");
        spinnerOwner=(Spinner)findViewById(R.id.acsspineerMain) ;
        radioGrpMode=(RadioGroup)findViewById(R.id.radioGrpMode);
        NotCashLinearLayout=(LinearLayout)findViewById(R.id.NotCashLinearLayout);
        amtChq=(EditText)findViewById(R.id.amtChq);
        editTextinstumentNumber=(EditText)findViewById(R.id.chqdraNo);
        bankAddressEditText=(EditText)findViewById(R.id.branchAddressName);
        bankName=(EditText)findViewById(R.id.bankName);
        remark=(EditText)findViewById(R.id.narrationChq);
        dateTextOnDsr=(EditText)findViewById(R.id.dateTextOnDsr);
        dateTextOnDsr.setInputType(InputType.TYPE_NULL);
        dateTextOnDsr.setFocusable(false);
        validation=new Validation(PaymentTransfer.this);
        TransactionMode="Cash";
        disabledFields();
        buttonSave=(Button)findViewById(R.id.prSave);
        buttonSignature=(Button)findViewById(R.id.prSignature);
        imageSigmature=(ImageView)findViewById(R.id.imageSigmature);
        TransactionMode="Cash";
        disabledFields();
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(connectionDetector.isConnectingToInternet()) {
                    String mode1 = ((RadioButton) findViewById(radioGrpMode.getCheckedRadioButtonId())).getText().toString();
                    TransactionMode = mode1;
                    amount = validation.vAlNUmericFileds(amtChq.getText().toString());
                    remarkString = remark.getText().toString();
                    if (mode1.equals("Cash")) {
                        instumentNumber = "";
                        instumentDate = "";
                        bankNameString = "";
                        bankAddress = "";
                        if (ownerArray.size() > 0) {
                            if (!amount.isEmpty() && amount != null && !amount.equalsIgnoreCase("")) {
                                if (validation.parseStringToDouble(amount) != 0 || validation.parseStringToDouble(amount) != 0.00) {
                                    //if(!remarkString.isEmpty() && remarkString != null && !remarkString.equalsIgnoreCase("")){
                                    if (stringSignature.length() > 0 && !stringSignature.equalsIgnoreCase("")) {
                                        buttonSave.setEnabled(false);
                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                buttonSave.setEnabled(true);
                                            }
                                        }, 2000);

                                        new saveDataOnServer().execute();
                                    } else {
                                        new Custom_Toast(PaymentTransfer.this, "Please Add Signature").showCustomAlert();
                                    }
                          /*  }
                            else{
                                new Custom_Toast(PaymentTransfer.this,"Please Enter Remark").showCustomAlert();
                            }*/
                                } else {
                                    amount = "";
                                    amtChq.setText("");
                                    new Custom_Toast(PaymentTransfer.this, "Amount Should Not Be Zero").showCustomAlert();
                                }
                            } else {
                                amount = "";
                                amtChq.setText("");
                                new Custom_Toast(PaymentTransfer.this, "Please Enter  Amount").showCustomAlert();
                            }
                        } else {
                            new Custom_Toast(PaymentTransfer.this, "Person To Required").showCustomAlert();
                        }
                    } else {
                        boolean isCorrect = true;
                        String errorMsg = "";
                        instumentNumber = validation.vAlfNum(editTextinstumentNumber.getText().toString());
                        instumentDate = validation.vAlfNum(dateTextOnDsr.getText().toString());
                        bankNameString = validation.vAlfNum(bankName.getText().toString());
                        bankAddress = validation.vAlfNum(bankAddressEditText.getText().toString());
                        if (ownerArray.size() < 0) {
                            isCorrect = false;
                            errorMsg = "Person To Required";
                        } else if (amount.isEmpty() || amount == null || amount.equalsIgnoreCase("")) {
                            isCorrect = false;
                            errorMsg = "Please Enter Amount";
                        } else if (validation.parseStringToDouble(amount) == 0 || validation.parseStringToDouble(amount) == 0.00) {
                            isCorrect = false;
                            errorMsg = "Amount Should Not Be Zero";
                        } else if (instumentNumber.isEmpty() || instumentNumber == null || instumentNumber.equalsIgnoreCase("")) {
                            isCorrect = false;
                            errorMsg = "Please Enter Instrument Number";
                        } else if (instumentDate.isEmpty() || instumentDate == null || instumentDate.equalsIgnoreCase("")) {
                            isCorrect = false;
                            errorMsg = "Please Enter Instrument Date";

                        } else if (bankNameString.isEmpty() || bankNameString == null || bankNameString.equalsIgnoreCase("")) {
                            isCorrect = false;
                            errorMsg = "Please Enter Bank Name";
                        } else if (bankAddress.isEmpty() || bankAddress == null || bankAddress.equalsIgnoreCase("")) {
                            isCorrect = false;
                            errorMsg = "Please Enter Bank Address";
                        }
                  /*  else if(remarkString.isEmpty() || remarkString == null || remarkString.equalsIgnoreCase("")){
                        isCorrect=false;
                        errorMsg="Please Enter Remark";
                    }*/
                        else if (stringSignature.length() < 0 || stringSignature.equalsIgnoreCase("")) {
                            isCorrect = false;
                            errorMsg = "Please Add Signature";
                        }

                        if (isCorrect) {
                            buttonSave.setEnabled(false);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    buttonSave.setEnabled(true);
                                }
                            }, 2000);
                            new saveDataOnServer().execute();
                        } else {
                            new Custom_Toast(PaymentTransfer.this, errorMsg).showCustomAlert();
                        }

                    }
                }
                else{
                    new Custom_Toast(PaymentTransfer.this,"There is no Internet Connection available").showCustomAlert();
                }

            }
        });
        buttonSignature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent intent=new Intent(PaymentTransfer.this,DigitalSignature.class);
                    intent.putExtra("fromWhere","Transfer");
                    intent.putExtra("partyName",ownerArray.get(spinnerOwner.getSelectedItemPosition()).getName());
                    startActivityForResult(intent,2);

            }
        });
        amtChq.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try{
                    System.out.println("Value:"+String.valueOf(s));
                    if(validation.parseStringToDouble(String.valueOf(s))<0){
                        RadioButton rb = (RadioButton)findViewById(R.id.cashRadio);
                        rb.setChecked(true);
                    }
                }catch (Exception e){e.printStackTrace();}

            }
        });
        radioGrpMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.cashRadio)
                {

                    disabledFields();

                }
                else  {
                    try {
                        if (validation.parseStringToDouble(amtChq.getText().toString()) < 0) {
                            RadioButton rb = (RadioButton)findViewById(R.id.cashRadio);
                            rb.setChecked(true);
                            disabledFields();

                        } else {
                            enabledFields();
                        }
                    }catch (Exception e){
                        enabledFields();
                    }


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
        new getOwner().execute();
        }else{
            dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }
    }
    void clearFields(){
        finish();
        Intent intent=new Intent(PaymentTransfer.this,PaymentTransfer.class);
        startActivity(intent);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 2) {
                if(resultCode == Activity.RESULT_OK){
                    String result=data.getStringExtra("result").replaceAll("\\n","");
                    if (!result.isEmpty()) {
                        stringSignature = result;
                    }
                    imageSigmature.setVisibility(View.VISIBLE);
                    imageSigmature.setImageBitmap(StringToBitMap(result));
                    Log.e("Image",result);

                }
                else {
                    stringSignature ="";
                }
            }
        }
    }
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    public void disabledFields()
    {
        dateTextOnDsr.setText(null);
        bankName.setText(null);
        bankAddressEditText.setText(null);
        editTextinstumentNumber.setText(null);
        NotCashLinearLayout.setVisibility(View.GONE);
    }

    protected class getOwner extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(PaymentTransfer.this,"Loading Data", "Loading...", true);
            progressDialog.setCancelable(true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            String response=null;
            // TODO Auto-generated method stub
            try {
                // String url = "http://sfmsprim.dataman.in/And_Sync.asmx/xjsphoneEmailUrlType_CRM?key=";
                String url ="http://"+server+"/And_Sync.asmx/XjGetUser_CRM?smid="+SMID;
                JSONParser jParser = new JSONParser();
                response = jParser.getJSONArray(url);
                url=null;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            JSONArray jsonArray=null;
            if(progressDialog!=null)
            {
                progressDialog.dismiss();
            }
            int defaulSpinnerSelection=0;
            if(!result.isEmpty() && !(result == null)){
                try {
                    jsonArray=new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objs = null;
                        try {
                            objs = jsonArray.getJSONObject(i);
                            Owner owner=new Owner();
                            owner.setId(objs.getString("smid"));
                            owner.setName(objs.getString("smname"));
                            ownerArray.add(owner);
                        } catch (JSONException e) {
                            if(progressDialog!=null)
                            {
                                progressDialog.dismiss();
                            }
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    if(progressDialog!=null)
                    {
                        progressDialog.dismiss();
                    }
                    e.printStackTrace();
                }
                setSpinner(spinnerOwner,ownerArray);
                try{
                    spinnerOwner.setSelection(0);

                }catch (Exception e){

                }
                /*try{
                    for(int i=0;i<ownerArray.size();i++){
                        if(ownerArray.get(i).getId().equalsIgnoreCase(SMID)){
                            spinnerOwner.setSelection(i);
                        }
                    }

                }catch (Exception e){

                }*/
            }
            else{
                new Custom_Toast(PaymentTransfer.this,"No Data Found").showCustomAlert();
            }

        }
    }
    public class saveDataOnServer extends AsyncTask<String , Void ,String> {

        String server_response;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(PaymentTransfer.this,"Saving Data", "Please Wait...", true);
            SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
            Calendar cal=Calendar.getInstance();
            String currentDate=df.format(cal.getTime());
            params.add(new BasicNameValuePair("DrUserId",userId));
            if(spinnerOwner.getSelectedItemPosition()>=0){
            params.add(new BasicNameValuePair("CrUserId",ownerArray.get(spinnerOwner.getSelectedItemPosition()).getId()));
            }
            else{
                params.add(new BasicNameValuePair("CrUserId","0"));
            }
            params.add(new BasicNameValuePair("VDate",currentDate));
            params.add(new BasicNameValuePair("SMId",SMID));
            params.add(new BasicNameValuePair("PaymentMode",TransactionMode));
            params.add(new BasicNameValuePair("Amount",amount));
            params.add(new BasicNameValuePair("PaymentDate",currentDate));
            params.add(new BasicNameValuePair("Cheque_DDNo",instumentNumber));
            params.add(new BasicNameValuePair("Cheque_DD_Date",instumentDate));
            params.add(new BasicNameValuePair("Bank",bankNameString));
            params.add(new BasicNameValuePair("Branch",bankAddress));
            params.add(new BasicNameValuePair("Remarks",remarkString));
            params.add(new BasicNameValuePair("SignImage",stringSignature));
            params.add(new BasicNameValuePair("longitude",longitude));
            params.add(new BasicNameValuePair("latitude",latitude));
            params.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
        }
        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://"+server+"/And_Sync.asmx/xJSSaveCRM_PaymentTransfer_CRM"); // here is your URL path
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
                        new Custom_Toast(PaymentTransfer.this,"Saved Sussesfully").showCustomAlert();
                        clearFields();
                    }else
                    {
                        new Custom_Toast(PaymentTransfer.this,"Try Again!"+server_response).showCustomAlert();
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
    public boolean setSpinner(Spinner spinner, ArrayList<Owner> arrayList){
        CustomAdapterCRMStreamInfo adapter = new CustomAdapterCRMStreamInfo(PaymentTransfer.this,arrayList ,R.layout.spinner_adapter_view);
        spinner.setAdapter(adapter);
        return  true;
    }
}
