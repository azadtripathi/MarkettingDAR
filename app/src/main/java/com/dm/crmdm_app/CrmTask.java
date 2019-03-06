package com.dm.crmdm_app;


import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dm.controller.AppDataController;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomAdapterCRMStreamInfo;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.library.DatePickerFragmentRange;
import com.dm.library.IntentSend;
import com.dm.model.AppData;
import com.dm.model.Owner;
import com.dm.model.PersonInfo;
import com.dm.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Administrator on 6/15/2017.
 */
public class CrmTask extends AppCompatActivity {
    String currentDate;
    ConnectionDetector connectionDetector;
    EditText actionEdittext, dateEdittext, timeEdittext;
    Spinner assignSpinner, statusSpinner;
    Button save, buttonCancel;
    ArrayAdapter<String> adapter;
    ArrayList<Owner> assigTOrArrayList = new ArrayList<>();
    ArrayList<Owner> taskstatusArrayList = new ArrayList<>();
    ProgressDialog progressDialog;
    String Contact_id = "", Ref_DocID = "", TDocID = "";
    String SMID;
    SharedPreferences preferences2;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    String server;
    String Flag;
    Button takePicButton;
    String goToPage = "";
    String latitude, longitude, latlngtime;
    String intenInfo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector = new ConnectionDetector(this);
        if (connectionDetector.isConnectingToInternet()) {
            preferences2 = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            SMID = preferences2.getString("CONPERID_SESSION", null);
            appDataController1 = new AppDataController(CrmTask.this);
            AppData appData;
            appDataController1.open();
            appDataArray = appDataController1.getAppTypeFromDb();
            appDataController1.close();
//            appData = appDataArray.get(0);
//            server = appData.getCompanyUrl();

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

            int reduceheight = height * 18 / 100;
            int reducewidth = width * 15 / 100;
            System.out.println("Height@:" + height + "-" + reduceheight + "-" + width + "-" + reducewidth);
            //this.getWindow().setLayout(width-reducewidth,height-reduceheight);
            this.getWindow().setLayout(width - reducewidth, ((WindowManager.LayoutParams) params).WRAP_CONTENT);
            setContentView(R.layout.activity_crmtask);


            NewLocationService track = new NewLocationService(this);
            if (track.canGetLocation) {
                latitude = track.getLatitude();
                longitude = track.getLongitude();
                latlngtime = track.getLatLngTime();
            } else {
                latitude = track.getLatitude();
                longitude = track.getLongitude();
                latlngtime = track.getLatLngTime();
            }


            // setContentView(R.layout.listview_crmstream);
           /* getSupportActionBar().setCustomView(R.layout.actionbar_custom_view_home);
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
            tv.setText("Lead Detail List");*/

            takePicButton = (Button) findViewById(R.id.take_pic_button);
            takePicButton.setVisibility(View.GONE);
            capturedImage = (ImageView) findViewById(R.id.capturedImage);
            actionEdittext = (EditText) findViewById(R.id.textAction);
            dateEdittext = (EditText) findViewById(R.id.date);
            dateEdittext.setInputType(InputType.TYPE_NULL);
            dateEdittext.setFocusable(false);
            timeEdittext = (EditText) findViewById(R.id.time);
            timeEdittext.setInputType(InputType.TYPE_NULL);
            timeEdittext.setFocusable(false);
            assignSpinner = (Spinner) findViewById(R.id.assignSpinner);
            statusSpinner = (Spinner) findViewById(R.id.taskstatusSpinner);
            save = (Button) findViewById(R.id.asSubmit);
            buttonCancel = (Button) findViewById(R.id.asCancel);
            buttonCancel.setVisibility(View.VISIBLE);
            goToPage = "Stream";
            Intent intent = getIntent();
            if (intent != null) {
                intenInfo = intent.getStringExtra("FromWhere");
                if (intenInfo.equalsIgnoreCase("ChildScreen")) {
                    goToPage = "StreamInfo";
                    Flag = intent.getStringExtra("Flag");
                    Contact_id = intent.getStringExtra("Contact_id");
                    Ref_DocID = intent.getStringExtra("Ref_DocID");
                    TDocID = intent.getStringExtra("TDocID");
                    if (!TDocID.equalsIgnoreCase("")) {
                        save.setText("Update");
                    }
                } else if (intenInfo.equalsIgnoreCase("addContact")) {
                    buttonCancel.setVisibility(View.GONE);
                    Contact_id = intent.getStringExtra("Contact_id");
                    goToPage = "AddContact";
                    buttonCancel.setText("Assign To Me");

                }


            }
            SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
            Calendar cal = Calendar.getInstance();
            int hour = cal.get(Calendar.HOUR_OF_DAY);
            int minute = cal.get(Calendar.MINUTE);
            DecimalFormat mFormat = new DecimalFormat("00");
            currentDate = df.format(cal.getTime());
            timeEdittext.setText(mFormat.format(Double.valueOf(hour)) + ":" + mFormat.format(Double.valueOf(minute)));
            dateEdittext.setText(currentDate);
//            new statusTagLeadSourceOwnerData().execute("Owner");
            dateEdittext.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    showDatePicker(v.getId());
                }
            });
            timeEdittext.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(CrmTask.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                            DecimalFormat mFormat = new DecimalFormat("00");
                            timeEdittext.setText(mFormat.format(Double.valueOf(selectedHour)) + ":" + mFormat.format(Double.valueOf(selectedMinute)));
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();

                }
            });
            buttonCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!intenInfo.equalsIgnoreCase("") && intenInfo.equalsIgnoreCase("addContact")) {
                        Intent returnIntent = new Intent();
                        //returnIntent.putExtra("result",result);
                        setResult(AddContact.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        finish();
                    }
                }
            });
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   /* new Custom_Toast(CrmTask.this,"Data has been saved");
                    Intent intent=new Intent(CrmTask.this,CRMstream.class);
                    startActivity(intent);
                    finish();*/
                    if (!actionEdittext.getText().toString().isEmpty()) {
                        if (connectionDetector.isConnectingToInternet()) {
                            save.setEnabled(false);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    save.setEnabled(true);
                                }
                            }, 2000);
                            new saveDataOnServer().execute();
                        } else {
                            new Custom_Toast(CrmTask.this, "Try again! Check your internet connection.");
                        }
                    } else {
                        new Custom_Toast(CrmTask.this, "Please Enter All Required Fields");
                        actionEdittext.setError("Action Required");
                    }

                }
            });
            takePicButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cameraIntent();
                }
            });
        } else {
            new Custom_Toast(getApplicationContext(), "No Internet Connection! Try Again.").showCustomAlert();
        }
    }


    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            onCaptureImageResult(data);
        }
    }

    ImageView capturedImage;
    Bitmap pageBitmap;
    String selectedImageBase64ImgString = "";

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

        int maxHeight = 500;
        int maxWidth = 500;
        float scale = Math.min(((float) maxHeight / thumbnail.getWidth()), ((float) maxWidth / thumbnail.getHeight()));
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);

        int bitmapByteCount = BitmapCompat.getAllocationByteCount(thumbnail);
        if (bitmapByteCount <= 1048576) {
            capturedImage.setImageBitmap(thumbnail);
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //filePath1=data.getData().toString();
        String filePath1 = destination.getPath().toString();
        System.out.println(filePath1);
        capturedImage.setImageBitmap(thumbnail);
        pageBitmap = thumbnail;
        selectedImageBase64ImgString = encodeImage(pageBitmap);
        //filePath = fileUri.getPath();
    }


    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }


    public String getstatusAndOwnerData(String type) {
        String url = "";
        if (type.equalsIgnoreCase("Owner")) {
            url = "http://" + server + "/And_Sync.asmx/xjsowner_CRM?smid=" + SMID;
        } else if (type.equalsIgnoreCase("TaskStatus")) {
            url = "http://" + server + "/And_Sync.asmx/XjsGetTaskStatus_CRM";
        }
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }

    protected class statusTagLeadSourceOwnerData extends AsyncTask<String, String, String> {
        String type = null, result = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = new ProgressDialog(CrmTask.this);
            progressDialog.setMessage("Loading Data.Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            //     progressDialog= ProgressDialog.show(AddContactModel.this,"Loading Data", "Loading...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {

            type = arg0[0];
            // TODO Auto-generated method stub
            try {
                result = getstatusAndOwnerData(type);
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
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            JSONArray jsonArray = null;
            if (!result.isEmpty() && !(result == null)) {
                if (type.equalsIgnoreCase("Owner")) {
                    int defaultposition = 0;
                    try {
                        jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                objs = jsonArray.getJSONObject(i);
                                Owner owner = new Owner();
                                objs = jsonArray.getJSONObject(i);
                                owner.setId(objs.getString("id"));
                                owner.setName(objs.getString("Name"));
                                assigTOrArrayList.add(owner);
                                if (assigTOrArrayList.get(i).getId().equalsIgnoreCase(SMID)) {
                                    defaultposition = i;
                                }
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
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    setSpinner(assignSpinner, assigTOrArrayList);
                    assignSpinner.setSelection(defaultposition);
                    new statusTagLeadSourceOwnerData().execute("TaskStatus");
                } else if (type.equalsIgnoreCase("TaskStatus")) {
                    try {
                        jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                Owner owner = new Owner();
                                objs = jsonArray.getJSONObject(i);
                                owner.setId(objs.getString("Id"));
                                owner.setName(objs.getString("Description"));
                                taskstatusArrayList.add(owner);
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
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    setSpinner(statusSpinner, taskstatusArrayList);
                    if (!TDocID.equalsIgnoreCase("")) {
                        save.setText("Update");
                        new getTaskData().execute();
                    }
                }
            } else {
                new Custom_Toast(CrmTask.this, "No Data Found").showCustomAlert();
            }

        }
    }

    private void showDatePicker(int id) {
        //DateAndTimePicker date = new DateAndTimePicker();
        DatePickerFragmentRange date = new DatePickerFragmentRange();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        args.putInt("grace", 0);
        //args.putString("Type", "Days");
        args.putString("Type", "tour");
        date.setArguments(args);
        if (id == R.id.date) {
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
//    SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = format1.parse(strDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            currentDate = format2.format(date);
            dateEdittext.setText(format2.format(date));
        }
    };

    public class saveDataOnServer extends AsyncTask<String, Void, String> {
        String server_response;
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            params.add(new BasicNameValuePair("TDocId", TDocID));
            params.add(new BasicNameValuePair("smid", SMID));
            params.add(new BasicNameValuePair("task", actionEdittext.getText().toString()));
            params.add(new BasicNameValuePair("contactId", Contact_id));
            params.add(new BasicNameValuePair("Asgndate", dateEdittext.getText().toString()));
            if (assignSpinner.getSelectedItemPosition() > -1) {
                params.add(new BasicNameValuePair("AsgnTo", assigTOrArrayList.get(assignSpinner.getSelectedItemPosition()).getId()));
            } else {
                params.add(new BasicNameValuePair("AsgnTo", "0"));
            }

            params.add(new BasicNameValuePair("Ref_DocId", Ref_DocID));
            if (statusSpinner.getSelectedItemPosition() > -1) {
                params.add(new BasicNameValuePair("Status", taskstatusArrayList.get(statusSpinner.getSelectedItemPosition()).getId()));
            } else {
                params.add(new BasicNameValuePair("Status", "0"));
            }

            params.add(new BasicNameValuePair("time", timeEdittext.getText().toString()));
            params.add(new BasicNameValuePair("longitude", longitude));
            params.add(new BasicNameValuePair("latitude", latitude));
            params.add(new BasicNameValuePair("lat_long_dt", String.valueOf(System.currentTimeMillis())));
        }

        protected String doInBackground(String... arg0) {
            try {

                URL url = new URL("http://" + server + "/And_Sync.asmx/XjsSaveAddTask_CRM"); // here is your URL path
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

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    server_response = sb.toString();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // if(progressDialog.isShowing()){progressDialog.dismiss();}
            Log.e("Response", "" + server_response);
            if (server_response != null) {
                if (!server_response.isEmpty()) {
                    server_response = server_response.replaceAll("\"", "");
                    if (server_response.equalsIgnoreCase("Y")) {
                        if (goToPage.equalsIgnoreCase("Stream")) {
                            new Custom_Toast(CrmTask.this, "Saved Sussesfully").showCustomAlert();
                            /*Intent intent=new Intent(CrmTask.this,CRMstream.class);
                            startActivity(intent);*/
                            finish();
                        } else if (goToPage.equalsIgnoreCase("StreamInfo")) {
                            new Custom_Toast(CrmTask.this, "Saved Sussesfully").showCustomAlert();
                            /*Intent intent=new Intent(CrmTask.this,CRMStreamInfo.class);
                            intent.putExtra("Contact_id",Contact_id);
                            intent.putExtra("Flag",Flag);
                            startActivity(intent);*/
                            finish();
                        } else {
                            new Custom_Toast(CrmTask.this, "Saved Sussesfully").showCustomAlert();
                            Intent intent = new Intent(CrmTask.this, AddContact.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        new Custom_Toast(CrmTask.this, "Try Again!" + server_response).showCustomAlert();
                    }
                }
            }
        }
    }

    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
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

    public boolean setSpinner(Spinner spinner, ArrayList<Owner> arrayList) {
        CustomAdapterCRMStreamInfo adapter = new CustomAdapterCRMStreamInfo(CrmTask.this, arrayList, R.layout.spinner_adapter_view);
        spinner.setAdapter(adapter);
        return true;
    }

    protected class getTaskData extends AsyncTask<String, String, String> {
        String type = null, result = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CrmTask.this, "Loading Data", "Loading...", true);

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
                    String stringTaskDate = "", taskName = "", Status = "o", AssignedTo = "";
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objs = null;
                        try {
                            objs = jsonArray.getJSONObject(i);
                            taskName = objs.getString("Task");
                            Status = objs.getString("Status");
                            if (objs.has("time")) {
                                timeEdittext.setText(objs.getString("time"));
                            }
                            AssignedTo = objs.getString("AssignedTo");
                            SimpleDateFormat format1 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                            SimpleDateFormat format2 = new SimpleDateFormat("dd/MMM/yyyy", Locale.ENGLISH);
                            Date taskDate = new Date();
                            try {
                                String Adate = objs.getString("ADate");
                                taskDate = format1.parse(Adate);
                            } catch (ParseException e1) {
                                e1.printStackTrace();
                            }
                            stringTaskDate = format2.format(taskDate);
                        } catch (JSONException e) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            e.printStackTrace();
                        }
                    }
                    dateEdittext.setText(stringTaskDate);
                    actionEdittext.setText(taskName);
                    for (int i = 0; i < taskstatusArrayList.size(); i++) {
                        if (taskstatusArrayList.get(i).getId().equalsIgnoreCase(Status)) {
                            statusSpinner.setSelection(i);
                        }
                    }
                    for (int i = 0; i < assigTOrArrayList.size(); i++) {
                        if (assigTOrArrayList.get(i).getName().equalsIgnoreCase(AssignedTo)) {
                            assignSpinner.setSelection(i);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                new Custom_Toast(CrmTask.this, "No Data Found").showCustomAlert();
            }

        }
    }

    public String getPersonInfo() {
        String url = "http://" + server + "/And_Sync.asmx/XjsGetTaskDetailByID_CRM?DocId=" + TDocID;
        ;
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }

    @Override
    public void onBackPressed() {
        if (!intenInfo.equalsIgnoreCase("") && intenInfo.equalsIgnoreCase("addContact")) {
            Intent returnIntent = new Intent();
            //returnIntent.putExtra("actionData","");
            setResult(AddContact.RESULT_OK, returnIntent);
            finish();
        }
    }
}
