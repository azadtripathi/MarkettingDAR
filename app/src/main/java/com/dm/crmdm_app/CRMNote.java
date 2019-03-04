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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dm.controller.AppDataController;
import com.dm.library.Custom_Toast;
import com.dm.model.AppData;
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
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class CRMNote extends AppCompatActivity {
    EditText editTextNote;
    Button  buttonSave,buttonCancel;
    String Contact_id="",Ref_DocID="",TDocID="";
    ProgressDialog progressDialog;
    SharedPreferences preferences2;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    String server,SMID;String Flag;
    String latitude,longitude,latlngtime;

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
            setContentView(R.layout.activity_crmnote);


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


            preferences2=this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            SMID=preferences2.getString("CONPERID_SESSION", null);
            appDataController1=new AppDataController(CRMNote.this);
            AppData appData;
            appDataController1.open();
            appDataArray = appDataController1.getAppTypeFromDb();
            appDataController1.close();
            appData = appDataArray.get(0);
            server=appData.getCompanyUrl();
            editTextNote=(EditText)findViewById(R.id.textNote);
            buttonSave=(Button)findViewById(R.id.asSubmit);
            buttonCancel=(Button)findViewById(R.id.asCancel);
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
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            buttonSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String savedata=editTextNote.getText().toString();

                    if(!savedata.isEmpty()){
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
                    else
                    {
                        new Custom_Toast(CRMNote.this,"PLEASE ENTER NOTE").showCustomAlert();
                    }
                }
            });
        if(!TDocID.equalsIgnoreCase("")){
          new getNoteData().execute();
        }
    }
    public class saveDataOnServer extends AsyncTask<String , Void ,String> {
        String server_response;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            params.add(new BasicNameValuePair("TaskDocID",Ref_DocID));
            params.add(new BasicNameValuePair("Note",editTextNote.getText().toString()));
            params.add(new BasicNameValuePair("smid",SMID));
            params.add(new BasicNameValuePair("NoteId",TDocID));
            params.add(new BasicNameValuePair("longitude",longitude));
            params.add(new BasicNameValuePair("latitude",latitude));
            params.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
        }
        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://"+server+"/And_Sync.asmx/XjsInsertUpdTaskNote_CRM"); // here is your URL path
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
                return new String("Exception: " + e.getMessage());
            }

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // if(progressDialog.isShowing()){progressDialog.dismiss();}
            Log.e("Response", "" + server_response);
            if(server_response != null){
                if(!server_response.isEmpty()){
                    server_response=server_response.replaceAll("\"","");
                    if(server_response.equalsIgnoreCase("Y")){
                        new Custom_Toast(CRMNote.this,"Saved Sussesfully").showCustomAlert();
                      /*  Intent intent=new Intent(CRMNote.this,CRMStreamInfo.class);
                        intent.putExtra("Contact_id",Contact_id);
                        intent.putExtra("Flag",Flag);
                        startActivity(intent);*/
                        finish();
                    }else
                    {
                        new Custom_Toast(CRMNote.this,"Try Again!"+server_response).showCustomAlert();
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
    protected class getNoteData extends AsyncTask<String, String, String> {
        String type = null, result = null;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CRMNote.this, "Loading Data", "Loading...", true);
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
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objs = null;
                        try {
                            objs = jsonArray.getJSONObject(i);
                            editTextNote.setText(objs.getString("Notes"));
                        } catch (JSONException e) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            e.printStackTrace();
                        }
                    }

                } catch (JSONException e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    e.printStackTrace();
                }

            } else {
                new Custom_Toast(CRMNote.this, "No Data Found").showCustomAlert();
            }

        }
    }
    public String getPersonInfo() {
        String url = "http://"+server+"/And_Sync.asmx/XjsGetTaskNote_CRM?TaskNoteId="+TDocID;;
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }
}
