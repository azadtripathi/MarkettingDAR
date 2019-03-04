package com.dm.crmdm_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.controller.AppDataController;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomAdapterCRMStreamInfo;
import com.dm.library.Custom_Toast;
import com.dm.model.AddContactModel;
import com.dm.model.AppData;
import com.dm.model.Owner;
import com.dm.model.PhoneModel;
import com.dm.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static android.R.attr.phoneNumber;
import static android.R.attr.track;
import static android.R.attr.value;

public class CRMCall extends AppCompatActivity {
    Spinner phoneSpinner,resultSpinner;
    EditText editTextdealNote;
    Button  buttonSave,buttonCancel;
    ProgressDialog progressDialog;
    ConnectionDetector connectionDetector;
    String Contact_id="",Ref_DocID="",TDocID="";
    ArrayList<Owner> phoneArray=new ArrayList<>();
    ArrayList<Owner> emailArray=new ArrayList<>();
    ArrayList<PhoneModel> newPhoneArray=new ArrayList<>();
    //ArrayList<Owner> resultArray=new ArrayList<>();
    SharedPreferences preferences2;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    String server,SMID;
    String type = "phone";
    String Flag;
    TextView information;
    String latitude,longitude,latlngtime;
    String phoneoremail;
    RadioGroup rg;
    RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        setContentView(R.layout.activity_crmcall);

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

        Intent intent=getIntent();
        phoneSpinner=(Spinner)findViewById(R.id.phoneSpinner);
        resultSpinner=(Spinner)findViewById(R.id.resultSpinner);
        editTextdealNote=(EditText)findViewById(R.id.textdealNote);
        buttonSave=(Button)findViewById(R.id.asSubmit);
        buttonCancel=(Button)findViewById(R.id.asCancel);
        information=(TextView)findViewById(R.id.information);
         rg = (RadioGroup) findViewById(R.id.radiotype);
        preferences2=this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SMID=preferences2.getString("CONPERID_SESSION", null);
        appDataController1=new AppDataController(CRMCall.this);
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();
        if(intent !=null){
            Flag=intent.getStringExtra("Flag");
            Contact_id=intent.getStringExtra("Contact_id");
            Ref_DocID=intent.getStringExtra("Ref_DocID");
            TDocID=intent.getStringExtra("TDocID");
            if(!TDocID.equalsIgnoreCase("")){
                buttonSave.setText("Update");
            }
        }

        phoneSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(type.equalsIgnoreCase("Phone"))
                {
                    if(newPhoneArray.size()>0)
                    information.setText(newPhoneArray.get(i).getPhone());
                }
                else{
                    if(newPhoneArray.size()>0)
                    information.setText(newPhoneArray.get(i).getEmail());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int  selectedId =group.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                 type= (String) radioButton.getText();
                if(type.equalsIgnoreCase("Phone"))
                {
                    if(newPhoneArray.size()>0)
                    information.setText(newPhoneArray.get(phoneSpinner.getSelectedItemPosition()).getPhone());
                }
                else{
                    if(newPhoneArray.size()>0)
                    information.setText(newPhoneArray.get(phoneSpinner.getSelectedItemPosition()).getEmail());
                }

            }
        });


        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet()){
                    if(!editTextdealNote.getText().toString().isEmpty()){
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
                        new Custom_Toast(CRMCall.this,"PLEASE ENTER CALL NOTE").showCustomAlert();
                    }
                }else{
                    new Custom_Toast(CRMCall.this,"Try Again ! No Internet Connection");
                }
            }
        });
        connectionDetector=new ConnectionDetector(CRMCall.this);

        if(connectionDetector.isConnectingToInternet()){
            new GetPhoneAndResultList().execute("Phone");
        }else{
            new Custom_Toast(CRMCall.this,"Try Again ! No Internet Connection");
        }
    }
    public class saveDataOnServer extends AsyncTask<String , Void ,String> {
        String server_response;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            String phone,result;
            progressDialog= ProgressDialog.show(CRMCall.this,"Saving Data", "Please Wait...", true);
            if(phoneArray.isEmpty())
            {
                phone="NA";
            }
            else{
                phone=newPhoneArray.get(phoneSpinner.getSelectedItemPosition()).getName()+" ("+newPhoneArray.get(phoneSpinner.getSelectedItemPosition()).getJobTitle()+")";
            }
           /* if(resultArray.isEmpty()){
                result="NA";
            }
            else{
                result=resultArray.get(resultSpinner.getSelectedItemPosition()).getName();
            }*/
            phoneSpinner=(Spinner)findViewById(R.id.phoneSpinner);
            resultSpinner=(Spinner)findViewById(R.id.resultSpinner);
            editTextdealNote=(EditText)findViewById(R.id.textdealNote);
            params.add(new BasicNameValuePair("TaskDocID",Ref_DocID));
            params.add(new BasicNameValuePair("TCallID",TDocID));
            params.add(new BasicNameValuePair("smid",SMID));
            params.add(new BasicNameValuePair("ContactactNoEmail",information.getText().toString()));
            if(type.equalsIgnoreCase("phone"))
            {
                params.add(new BasicNameValuePair("Phone",phone));
                params.add(new BasicNameValuePair("mailto",""));
            }
            else
            {
                params.add(new BasicNameValuePair("mailto",phone));
                params.add(new BasicNameValuePair("Phone",""));
            }



            //params.add(new BasicNameValuePair("Result",result));
            params.add(new BasicNameValuePair("Result",""));
            params.add(new BasicNameValuePair("longitude",longitude));
            params.add(new BasicNameValuePair("latitude",latitude));
            params.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
            params.add(new BasicNameValuePair("CallNotes",editTextdealNote.getText().toString()));
        }
        protected String doInBackground(String... arg0)
        {

            try
            {

                URL url = new URL("http://"+server+"/And_Sync.asmx/XjsSaveCall_CRM"); // here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
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
                if(progressDialog !=null)
                    progressDialog.dismiss();
                return new String("Exception: " + e.getMessage());
            }

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // if(progressDialog.isShowing()){progressDialog.dismiss();}
            Log.e("Response", "" + server_response);
            if(progressDialog !=null)
                progressDialog.dismiss();
            if(server_response != null){
                if(!server_response.isEmpty()){
                    server_response=server_response.replaceAll("\"","");
                    if(server_response.equalsIgnoreCase("Y")){
                        new Custom_Toast(CRMCall.this,"Saved Sussesfully").showCustomAlert();
                       /* Intent intent=new Intent(CRMCall.this,CRMStreamInfo.class);
                        intent.putExtra("Contact_id",Contact_id);
                        intent.putExtra("Flag",Flag);
                        startActivity(intent);*/
                        finish();
                    }else
                    {
                        new Custom_Toast(CRMCall.this,"Try Again!"+server_response).showCustomAlert();
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
    class GetPhoneAndResultList extends AsyncTask<String, String, String> {
        String type=null;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(CRMCall.this,"Loading Data", "Loading...", true);
        }
        @Override
        protected String doInBackground(String... arg0) {
            type=arg0[0];String response=null;
            // TODO Auto-generated method stub
            try {
                response=getSpinnerData(type);
            } catch (Exception e)
            {
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
            if(progressDialog!=null)
            {
                progressDialog.dismiss();
            }
            if (result != null) {
                if(type.equalsIgnoreCase("Phone")){
                    try {
                        JSONArray jsonarray = new JSONArray(result);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                objs = jsonarray.getJSONObject(i);
                                PhoneModel phoneModel = new PhoneModel();
                                phoneModel.setContactID(objs.getString("contactid"));
                                phoneModel.setPhone(objs.getString("phone"));
                                phoneModel.setEmail(objs.getString("email"));
                                phoneModel.setJobTitle(objs.getString("jobTitle"));
                                phoneModel.setName(objs.getString("contactnm"));
                                newPhoneArray.add(phoneModel);
                                Owner owner=new Owner();
                                owner.setId(String.valueOf(i));
                                owner.setName(objs.getString("contactnm")+" ("+objs.getString("jobTitle")+")");
                                phoneArray.add(owner);
                            } catch (JSONException e) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
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
                    setSpinner(phoneSpinner, phoneArray);
                    try{
                        information.setText(newPhoneArray.get(phoneSpinner.getSelectedItemPosition()).getPhone());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    if(!TDocID.equalsIgnoreCase("")){
                        new getCallData().execute();
                    }
                    //new GetPhoneAndResultList().execute("Result");
                }
                /*else if(type.equalsIgnoreCase("Result"))
                {
                    try {
                        JSONArray jsonarray = new JSONArray(result);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                Owner owner = new Owner();
                                objs = jsonarray.getJSONObject(i);
                                owner.setId(String.valueOf(i));
                                owner.setName(objs.getString("Result"));
                                resultArray.add(owner);
                            } catch (JSONException e) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
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
                    setSpinner(resultSpinner, resultArray);
                    if(!TDocID.equalsIgnoreCase("")){
                        new getCallData().execute();
                    }
                }*/

            }
        }
    }
    public String getSpinnerData(String type){
        String url="";
        if(type.equalsIgnoreCase("Phone")){
            url="http://"+server+"/And_Sync.asmx/XjGetCRM_PhoneEmailType?contactid="+Contact_id;
        }
        else {
            url="http://"+server+"/And_Sync.asmx/XjsGetResults_CRM?ContactId="+Contact_id;
        }
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }
    public boolean setSpinner(Spinner spinner, ArrayList<Owner> arrayList) {
        CustomAdapterCRMStreamInfo adapter = new CustomAdapterCRMStreamInfo(CRMCall.this, arrayList, R.layout.spinner_adapter_view);
        spinner.setAdapter(adapter);
        return true;
    }

    protected class getCallData extends AsyncTask<String, String, String> {
        String type = null, result = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CRMCall.this, "Loading Data", "Loading...", true);

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
                    String textPhone="",textResult="",textNote="",textmailto="";
                    jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objs = null;
                        try {
                            objs = jsonArray.getJSONObject(i);
                            textPhone=objs.getString("phone");
                            textmailto = objs.getString("mailto");
                            textResult=objs.getString("result");
                            textNote=objs.getString("callnotes");
                        } catch (JSONException e) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            e.printStackTrace();
                        }
                    }
                    editTextdealNote.setText(textNote);
                    try {
                        if(textPhone != null && !textPhone.isEmpty())
                        {
                            type = "phone";
                            int i = getIndexOf(textPhone);

                            phoneSpinner.setSelection(i);
                            ((RadioButton)rg.getChildAt(0)).setChecked(true);
                        }
                        else
                        {
                            type ="mailto";
                            int i = getIndexOf(textmailto);

                            phoneSpinner.setSelection(i);

                            ((RadioButton)rg.getChildAt(1)).setChecked(true);
                        }
                    }
                    catch (Exception e)
                    {

                    }


                    /*for(int i=0;i<resultArray.size();i++){
                        if(resultArray.get(i).getName().equalsIgnoreCase(textResult))
                        {
                            resultSpinner.setSelection(i);
                        }
                    }*/
                    for(int i=0;i<phoneArray.size();i++){
                        if(phoneArray.get(i).getName().equalsIgnoreCase(textPhone))
                        {
                            phoneSpinner.setSelection(i);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                new Custom_Toast(CRMCall.this, "No Data Found").showCustomAlert();
            }

        }
    }

    public int getIndexOf(String str)
    {
        int pos = 0;
        for(int i=0;i< phoneArray.size();i++)
        {
            if(phoneArray.get(i).getName().contains(str))
            {
                pos = i;
                break;
            }
        }
        return pos;
    }
    public String getPersonInfo() {
        String url = "http://"+server+"/And_Sync.asmx/XjsGetTaskCalls_CRM?TaskCallId="+TDocID;;
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }
}
