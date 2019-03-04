package com.dm.crmdm_app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dm.database.DatabaseConnection;
import com.dm.library.Constant;
import com.dm.library.CustomAdapterCRMStreamInfo;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.model.Owner;

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

public class AddNewContactPage extends AppCompatActivity
{
    LinearLayout linearLayoutAddCompanyDetails, linearLayoutdynamicAllBelowfields, linearlayoutdynamicfieldsURL, linearLayoutdynamicfieldForPersonInfo;

    ArrayList<Owner> statusList = new ArrayList<>();
    ArrayList<Owner> employeeList = new ArrayList<>();
    ArrayList<Owner> cityArray = new ArrayList<>();
    ArrayList<Owner> industryArray = new ArrayList<>();
    ArrayList<Owner> subindustryArray = new ArrayList<>();
    ArrayList<Owner> productArray = new ArrayList<>();
    Spinner spinnerIdustry,spinnerSubIndustry,spinnerCity, spinnerCompanyCity, spinnerState, spinnerAddCompanyDetailsState, spinnerAddCompanyDetailsCountry, spinnerCountry,
            spinnerStatue, spinnerTag, spinnerLeadSource, spinnerOwner, spinnerProduct, spinnerProductGroup, spinnerIndustry,
            spinnerJobTitle,aacspineerStatus,employeeSpinner;

    EditText computerNoText,siteNoText,empNoText,valueText,advDetailText,editTextCompany, editTextMainEmail, editTextPersonName, editTextAddCompanyDetailsDiscription, editTextAddCompanyDetailsZip, editTextAddCompanyDetailsAddress, editTextFirstName, editTextLastName, editTextJobTittle,
            editTextMainMobNo, editTextMainUrl, editTextZip, editTextAddress, editTextBackground,companyNameText,hqText,phoneText,onDateText,validUptoText;
    ArrayList<Owner>jobTitleArray = new ArrayList<>();
    CheckBox isDealerCheckBox,aacActive,isBlockTeleCaller;
    ImageView imgAddMorePersonInfo;
    String hqcode,hqname;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);


        computerNoText = (EditText)findViewById(R.id.computerNoText);
        siteNoText = (EditText)findViewById(R.id.siteNoText);
        empNoText = (EditText)findViewById(R.id.empNoText);
        advDetailText = (EditText)findViewById(R.id.advDetailText);
        valueText = (EditText)findViewById(R.id.valueText);
        hqText = (EditText)findViewById(R.id.hqText);
        SharedPreferences pref = getSharedPreferences("LoginData",MODE_PRIVATE);
        hqcode = pref.getString("HqCode","");
        hqname = pref.getString("HqName","");
        hqText.setText(hqname);

        employeeSpinner = (Spinner)findViewById(R.id.aacspineerLeadSource);
        isDealerCheckBox = (CheckBox)findViewById(R.id.isDealerCheckBox);
        aacActive = (CheckBox)findViewById(R.id.aacActive);
        isBlockTeleCaller = (CheckBox)findViewById(R.id.aacblockTeleActive);

        NewLocationService track = new NewLocationService(this);
        if (track.canGetLocation)
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


        linearLayoutdynamicfieldForPersonInfo = (LinearLayout) findViewById(R.id.dynamicfieldsPersonInfo);
        onDateText = (EditText)findViewById(R.id.onDateText);
        validUptoText = (EditText)findViewById(R.id.validuptoText);

        onDateText.setFocusable(false);
        onDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(1);
            }
        });
        validUptoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(2);
            }
        });
        imgAddMorePersonInfo = (ImageView) findViewById(R.id.addPhone);
        spinnerProduct = (Spinner) findViewById(R.id.aacspineerProduct);
        spinnerIndustry = (Spinner)findViewById(R.id.industrySpinner);
        spinnerSubIndustry = (Spinner)findViewById(R.id.subindustrySpinner);
        aacspineerStatus = (Spinner)findViewById(R.id.aacspineerStatus);
        spinnerCity = (Spinner)findViewById(R.id.aacspineerCity);
        spinnerJobTitle = (Spinner)findViewById(R.id.adcspinnerJobTitle);
        getCityFromDb();
        getIndustry();
        getproduct();
        getDesignation();
        getStatus();
        getEmployee();

        spinnerIndustry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getSubIndustryByIndustry(industryArray.get(i).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        editTextFirstName = (EditText) findViewById(R.id.aacFirstName);
        companyNameText = (EditText) findViewById(R.id.aacCompany);
        hqText = (EditText)findViewById(R.id.hqText);
        phoneText = (EditText)findViewById(R.id.aacPhone);
        //contact section ...
        editTextPersonName = (EditText) findViewById(R.id.aacPersonName);
        spinnerJobTitle = (Spinner) findViewById(R.id.adcspinnerJobTitle);
        editTextMainMobNo = (EditText) findViewById(R.id.aacmbno);
        editTextMainEmail = (EditText) findViewById(R.id.aacPersonEmail);
        /////////
        editTextMainUrl = (EditText) findViewById(R.id.aacURL);
        editTextPersonName = (EditText) findViewById(R.id.aacPersonName);
        editTextMainMobNo = (EditText) findViewById(R.id.aacmbno);
        editTextMainEmail = (EditText) findViewById(R.id.aacPersonEmail);
        editTextAddress = (EditText)findViewById(R.id.aacAddress);


        imgAddMorePersonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addDynamicPersonInfo(AddNewContactPage.this, "personInfo", false, null);
            }
        });

    }

    void addDynamicPersonInfo(Context myContext, final String Flag, boolean isUpload, JSONObject jsonValues) {
        LinearLayout parent = new LinearLayout(myContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(4, 16, 4, 5);
        parent.setBackgroundResource(R.drawable.dk_gray_border);
        params.gravity = Gravity.CENTER;
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setLayoutParams(params);
        LinearLayout child1 = new LinearLayout(myContext);
        LinearLayout.LayoutParams paramsChild1 = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsChild1.setMargins(10, 10, 10, 10);
        paramsChild1.weight = 1.0f;
        paramsChild1.gravity = Gravity.CENTER_VERTICAL;
        child1.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout child2 = new LinearLayout(myContext);
        LinearLayout.LayoutParams paramsChild2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsChild2.setMargins(10, 10, 10, 10);
        paramsChild2.gravity = Gravity.CENTER_VERTICAL;
        child2.setOrientation(LinearLayout.HORIZONTAL);


        LinearLayout child3 = new LinearLayout(myContext);
        LinearLayout.LayoutParams paramsChild3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsChild3.setMargins(10, 10, 10, 10);
        paramsChild3.gravity = Gravity.CENTER;
        paramsChild3.weight = 1.0f;
        child3.setLayoutParams(paramsChild3);
        child3.setOrientation(LinearLayout.HORIZONTAL);


        AppCompatEditText editTextContactName = new AppCompatEditText(myContext);
        editTextContactName.setTypeface(Typeface.SERIF);
        editTextContactName.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);

        AppCompatEditText editTextPhone = new AppCompatEditText(myContext);
        editTextPhone.setTypeface(Typeface.SERIF);

        AppCompatEditText editTextEmail = new AppCompatEditText(myContext);
        editTextEmail.setTypeface(Typeface.SERIF);

        ImageView buttonRemove = new ImageView(myContext);
        ImageView buttonAdd = new ImageView(myContext);

        TextInputLayout contactNametitleWrapper = new TextInputLayout(this);

        TextInputLayout phonetitleWrapper = new TextInputLayout(this);

        TextInputLayout emailtitleWrapper = new TextInputLayout(this);

        TextView JobtitleTextView = new TextView(this);

        Spinner spinnerJobTitle = new Spinner(myContext, Spinner.MODE_DIALOG);
        paramsChild1.weight = 1.0f;

        contactNametitleWrapper.setLayoutParams(paramsChild1);
        editTextContactName.setSingleLine(true);
        editTextContactName.setTextSize(14);
        editTextContactName.setHint("Contact Name");
        editTextContactName.setTextColor(Color.parseColor("#000000"));
        editTextContactName.setHintTextColor(Color.parseColor("#FF4081"));
        LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpp.setMargins(0, 28, 0, 3);
        editTextContactName.setLayoutParams(lpp);
        paramsChild1.weight = 1.0f;
//        contactNametitleWrapper.setHint("Contact Name:");
        // contactNametitleWrapper.setLayoutParams(paramsChild1);
        editTextContactName.setInputType(InputType.TYPE_CLASS_TEXT);
        contactNametitleWrapper.addView(editTextContactName);


        editTextPhone.setLayoutParams(lpp);

        editTextPhone.setSingleLine(true);
        // editTextPhone.setTextSize(14);
        editTextPhone.setTextColor(Color.parseColor("#000000"));
        editTextPhone.setHintTextColor(Color.parseColor("#FF4081"));


        phonetitleWrapper.setHint("Enter Phone No.:");
        phonetitleWrapper.setLayoutParams(paramsChild1);
        editTextPhone.setInputType(InputType.TYPE_CLASS_PHONE);
        editTextPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        phonetitleWrapper.addView(editTextPhone);


        paramsChild2.weight = 1.0f;
        editTextEmail.setLayoutParams(lpp);
        editTextEmail.setSingleLine(true);
        //  editTextEmail.setTextSize(14);
        editTextEmail.setTextColor(Color.parseColor("#000000"));
        editTextEmail.setHintTextColor(Color.parseColor("#FF4081"));

        emailtitleWrapper.setHint("Email:");
        emailtitleWrapper.setLayoutParams(paramsChild1);
        editTextEmail.setInputType(InputType.TYPE_CLASS_TEXT);
        emailtitleWrapper.addView(editTextEmail);

        paramsChild2.weight = 1.0f;
        LinearLayout jobtitleLayout = new LinearLayout(myContext);
        LinearLayout.LayoutParams jobtitleParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
        jobtitleParams.setMargins(0, 0, 20, 0);
        jobtitleParams.weight = 1.0f;
        jobtitleParams.gravity = Gravity.BOTTOM;
        jobtitleLayout.setOrientation(LinearLayout.VERTICAL);
        jobtitleLayout.setLayoutParams(paramsChild2);
        jobtitleLayout.setLayoutParams(jobtitleParams);

        JobtitleTextView.setText("Job Title:");
        JobtitleTextView.setVisibility(View.GONE);
        spinnerJobTitle.setSelection(0);
        LinearLayout.LayoutParams ltt = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        ltt.setMargins(0, 0, 0, 0);

        spinnerJobTitle.setPrompt("Select Job Title");
        setStateContarySpinner(spinnerJobTitle, jobTitleArray);
        spinnerJobTitle.setLayoutParams(ltt);
        jobtitleLayout.setPadding(0, 0, 0, 26);
        jobtitleLayout.addView(JobtitleTextView);
        jobtitleLayout.addView(spinnerJobTitle);
        View v = new View(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 2);
        v.setBackgroundColor(Color.BLACK);
        v.setLayoutParams(lp);

        jobtitleLayout.addView(v);

        if (isUpload) {
            try {
                parent.setTag(jsonValues.getString("PhoneId"));
                editTextContactName.setText(jsonValues.getString("ContName"));
                editTextPhone.setText(jsonValues.getString("Phone"));
                editTextEmail.setText(jsonValues.getString("Email"));
                for (int i = 0; i < jobTitleArray.size(); i++) {
                    if (jobTitleArray.get(i).getName().equalsIgnoreCase(jsonValues.getString("PhoneType"))) {
                        spinnerJobTitle.setSelection(i);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        child1.addView(contactNametitleWrapper);
        child1.addView(jobtitleLayout);

        parent.addView(child1);

        child2.addView(phonetitleWrapper);
        child2.addView(emailtitleWrapper);

        parent.addView(child2);
        RelativeLayout child4 = new RelativeLayout(myContext);
        LinearLayout.LayoutParams paramsChild4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsChild4.setMargins(10, 10, 10, 10);
        paramsChild4.gravity = Gravity.CENTER;
        child4.setLayoutParams(paramsChild4);

        RelativeLayout.LayoutParams paramsChild5 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsChild5.addRule(RelativeLayout.CENTER_IN_PARENT);


        LinearLayout child6 = new LinearLayout(myContext);
        child6.setLayoutParams(paramsChild5);
        child6.setOrientation(LinearLayout.HORIZONTAL);


        buttonAdd.setLayoutParams(paramsChild5);
        buttonAdd.setBackgroundResource(R.drawable.addfields);
        child6.addView(buttonAdd);

        buttonRemove.setBackgroundResource(R.drawable.minus);
        paramsChild5.addRule(RelativeLayout.RIGHT_OF, buttonAdd.getId());
        buttonRemove.setLayoutParams(paramsChild5);
        child6.addView(buttonRemove);
        child4.addView(child6);
        child3.addView(child4);
        parent.addView(child3);

        // new Custom_Toast(getApplicationContext(),Flag+" Field Added Successfully").showCustomAlert();
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = (LinearLayout) v.getParent().getParent().getParent().getParent();
             //   LinearLayoutArrayListForPersonInfo.remove(linearLayout);
                linearLayoutdynamicfieldForPersonInfo.removeView(linearLayout);
//                    linearLayout.removeAllViews();
                // new Custom_Toast(getApplicationContext(),Flag+" Field Remove Successfully").showCustomAlert();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDynamicPersonInfo(AddNewContactPage.this, "personInfo", false, null);
            }
        });
        linearLayoutdynamicfieldForPersonInfo.addView(parent);
       // LinearLayoutArrayListForPersonInfo.add(parent);
    }


public void getEmployee()
{
    SharedPreferences pref = getSharedPreferences("StatusData",MODE_PRIVATE);
    String js = pref.getString("statuslist","[]");
    try {
        JSONArray jsonArray = new JSONArray(js);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("Id");
            String name = jsonObject.getString("EmployeeName");
            Owner data = new Owner();
            data.setName(name);
//                data.setId(id);
            statusList.add(data);
        }

        setStateContarySpinner(employeeSpinner, employeeList);
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    }



    public void getStatus()
    {
        SharedPreferences pref = getSharedPreferences("StatusData",MODE_PRIVATE);
        String js = pref.getString("statuslist","[]");
        try {
            JSONArray jsonArray = new JSONArray(js);
            for(int i=0;i< jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String id = jsonObject.getString("Id");
                String name = jsonObject.getString("party_status");
                Owner data = new Owner();
                data.setName(name);
//                data.setId(id);
                statusList.add(data);
            }

            setStateContarySpinner(aacspineerStatus, statusList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



    }



    public void getDesignation()
    {
        SharedPreferences pref = getSharedPreferences("DesignationData",MODE_PRIVATE);
        String js = pref.getString("desiglist","[]");
        try {
            JSONArray jsonArray = new JSONArray(js);
            for(int i=0;i< jsonArray.length();i++)
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("Id");
                String name = jsonObject.getString("Designation");
                Owner data = new Owner();
                data.setName(name);
                data.setId(id);
                jobTitleArray.add(data);
            }

            setStateContarySpinner(spinnerJobTitle, jobTitleArray);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



    }


    // DB FUnctions to read data ....

    public void getCityFromDb()
    {

        DatabaseConnection db = new DatabaseConnection(this);
        SQLiteDatabase dbx = db.getReadableDatabase();
        String query = "select webcode,name from MastCity order by name";
        Cursor cursor = dbx.rawQuery(query,null);
        while(cursor.moveToNext())
        {
            Owner data = new Owner();
            data.setId(cursor.getString(0));
            data.setName(cursor.getString(1));
            cityArray.add(data);
//            cityIdList.add(cursor.getString(0));
//            cityNameList.add(cursor.getString(1));
        }
        setStateContarySpinner(spinnerCity,cityArray);
        cursor.close();
        dbx.close();

    }





    public void getproduct()
    {

        DatabaseConnection db = new DatabaseConnection(this);
        SQLiteDatabase dbx = db.getReadableDatabase();
        String query = "select webcode,name from MastProduct order by name";
        Cursor cursor = dbx.rawQuery(query,null);
        while(cursor.moveToNext())
        {
            Owner data = new Owner();
            data.setId(cursor.getString(0));
            data.setName(cursor.getString(1));
            productArray.add(data);

        }
        setStateContarySpinner(spinnerProduct,productArray);
        cursor.close();
        dbx.close();

    }



    public void getIndustry()
    {

        DatabaseConnection db = new DatabaseConnection(this);
        SQLiteDatabase dbx = db.getReadableDatabase();
        String query = "select webcode,name from Industrymast order by name";
        Cursor cursor = dbx.rawQuery(query,null);
        while(cursor.moveToNext())
        {
            Owner data = new Owner();
            data.setId(cursor.getString(0));
            data.setName(cursor.getString(1));
            industryArray.add(data);

        }
        setStateContarySpinner(spinnerIndustry,industryArray);
        cursor.close();
        dbx.close();

    }

    public void getSubIndustryByIndustry(String indName)
    {

        subindustryArray.clear();
        DatabaseConnection db = new DatabaseConnection(this);
        SQLiteDatabase dbx = db.getReadableDatabase();
        String query = "select webcode,name from SubIndustrymast where indname='"+indName+"' order by name";
        Cursor cursor = dbx.rawQuery(query,null);
        while(cursor.moveToNext())
        {
            Owner data = new Owner();
            data.setId(cursor.getString(0));
            data.setName(cursor.getString(1));
            subindustryArray.add(data);
//            cityIdList.add(cursor.getString(0));
//            cityNameList.add(cursor.getString(1));
        }
        setStateContarySpinner(spinnerSubIndustry,subindustryArray);
        cursor.close();
        dbx.close();

    }

    public boolean setStateContarySpinner(Spinner spinner, ArrayList<Owner> arrayList) {
        CustomAdapterCRMStreamInfo adapter = new CustomAdapterCRMStreamInfo(AddNewContactPage.this, arrayList, R.layout.spinner_adapter_view);
        spinner.setAdapter(adapter);
        return true;
    }



    private void showDatePicker(final int n) {
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
                if(n == 1)
                {
                    onDateText.setText(format2.format(date));
                }
                else
                {
                    validUptoText.setText(format2.format(date));
                }
              //  dateTextOnDsr.setText(format2.format(date));

//			 dateTextOnDsr.setText((dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth))+"/"+(((monthOfYear+1)<10?("0"+(monthOfYear+1)):(monthOfYear+1)))+"/"+year);

            }
        });
        date.show(getSupportFragmentManager(), "Date Picker");
    }




    String latitude = "", longitude = "";
    String latlngtime;
    ProgressDialog progressDialog;
    public class saveDataOnServer extends AsyncTask<String, Void, String> {
        String server_response;
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        protected void onPreExecute() {


            params.clear();
            progressDialog = new ProgressDialog(AddNewContactPage.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            params.add(new BasicNameValuePair("Cname", companyNameText.getText().toString()));
            params.add(new BasicNameValuePair("Fname", editTextFirstName.getText().toString()));
            params.add(new BasicNameValuePair("JobTitle", editTextCompany.getText().toString()));
            params.add(new BasicNameValuePair("Add", editTextAddress.getText().toString()));

            params.add(new BasicNameValuePair("Cityid", cityArray.get(spinnerCity.getSelectedItemPosition()).getId()));
            params.add(new BasicNameValuePair("CityName", cityArray.get(spinnerCity.getSelectedItemPosition()).getName()));

            params.add(new BasicNameValuePair("Status", statusList.get(aacspineerStatus.getSelectedItemPosition()).getName()));
            params.add(new BasicNameValuePair("Tagid", editTextCompany.getText().toString())); // status id

            params.add(new BasicNameValuePair("PartyRefby", employeeList.get(employeeSpinner.getSelectedItemPosition()).getName()));
            params.add(new BasicNameValuePair("Owner", editTextFirstName.getText().toString()));

            params.add(new BasicNameValuePair("Active", String.valueOf(aacActive.isChecked())));
            params.add(new BasicNameValuePair("phnval", editTextCompany.getText().toString()));

            params.add(new BasicNameValuePair("Active", editTextCompany.getText().toString()));
            params.add(new BasicNameValuePair("phnddlval", editTextCompany.getText().toString()));

            params.add(new BasicNameValuePair("phncontName", editTextCompany.getText().toString()));
            params.add(new BasicNameValuePair("url", editTextMainUrl.getText().toString()));
            params.add(new BasicNameValuePair("emailval", editTextCompany.getText().toString()));
            params.add(new BasicNameValuePair("Flag", editTextCompany.getText().toString()));
            params.add(new BasicNameValuePair("longitude", longitude));
            params.add(new BasicNameValuePair("latitude", latitude));
            params.add(new BasicNameValuePair("lat_long_dt", String.valueOf(System.currentTimeMillis())));

            params.add(new BasicNameValuePair("productid", productArray.get(spinnerProduct.getSelectedItemPosition()).getId()));
            params.add(new BasicNameValuePair("IndustrySub", subindustryArray.get(spinnerSubIndustry.getSelectedItemPosition()).getName()));
            params.add(new BasicNameValuePair("Phone", String.valueOf(System.currentTimeMillis())));
            params.add(new BasicNameValuePair("PartyRefName", String.valueOf(System.currentTimeMillis())));
            params.add(new BasicNameValuePair("OnDate", onDateText.getText().toString()));
            params.add(new BasicNameValuePair("PartyAddedby", String.valueOf(System.currentTimeMillis())));

            params.add(new BasicNameValuePair("Ad_Detail", advDetailText.getText().toString()));
            params.add(new BasicNameValuePair("noemp", String.valueOf(System.currentTimeMillis())));
            params.add(new BasicNameValuePair("nocom", String.valueOf(System.currentTimeMillis())));
            params.add(new BasicNameValuePair("nosite", String.valueOf(System.currentTimeMillis())));
            params.add(new BasicNameValuePair("Softwareusing", String.valueOf(System.currentTimeMillis())));
            params.add(new BasicNameValuePair("businessremark", String.valueOf(System.currentTimeMillis())));
            params.add(new BasicNameValuePair("Dealerid", String.valueOf(System.currentTimeMillis())));
            params.add(new BasicNameValuePair("Valid_Upto", validUptoText.getText().toString()));
            params.add(new BasicNameValuePair("Influence_Any", String.valueOf(System.currentTimeMillis())));
            params.add(new BasicNameValuePair("Block_Telecaller", String.valueOf(isBlockTeleCaller.isChecked())));
            params.add(new BasicNameValuePair("Reason", String.valueOf(System.currentTimeMillis())));

            params.add(new BasicNameValuePair("Value", valueText.getText().toString()));
            params.add(new BasicNameValuePair("Userid", String.valueOf(System.currentTimeMillis())));
            params.add(new BasicNameValuePair("industrycode", industryArray.get(spinnerIndustry.getSelectedItemPosition()).getId()));
            params.add(new BasicNameValuePair("HQCode", hqcode));

            params.add(new BasicNameValuePair("HQName", hqname));
            params.add(new BasicNameValuePair("isdealer", String.valueOf(isDealerCheckBox.isChecked())));


           /* //User Contact
             DynamicFieldsPhoneNo.append(editTextMainMobNo.getText().toString());
             DynamicFieldsPhoneNo.append(",");

             *//*DynamicFieldsNameType.append(spinnerMainPhoneType.getSelectedItem().toString());
             DynamicFieldsNameType.append(",");*//*

             DynamicFieldsName.append(editTextPersonName.getText().toString());
             DynamicFieldsName.append(",");
             //Email
             DynamicFieldsEmail.append(editTextMainEmail.getText().toString());
             DynamicFieldsEmail.append(",");

            *//* DynamicFieldsEmailType.append(spinnerMainEmailType.getSelectedItem().toString());
             DynamicFieldsEmailType.append(",");
            *//*

             DynamicFieldJobTitle.append(jobTitleArray.get(spinnerJobTitle.getSelectedItemPosition()).getName());
             DynamicFieldJobTitle.append(",");

             //URL
             DynamicFieldsWeb.append(editTextMainUrl.getText().toString());
             DynamicFieldsWeb.append(",");
             if(editTextMainUrl.getTag() != null){
                 DynamicURLID.append(editTextMainUrl.getTag());
                 DynamicURLID.append(",");
             }
             else{
                 DynamicURLID.append("");
                 DynamicURLID.append(",");
             }

             if(editTextMainMobNo.getTag() != null){
                 DynamicPhoneID.append(editTextMainMobNo.getTag());
                 DynamicPhoneID.append(",");
             }
             else{
                 DynamicPhoneID.append("");
                 DynamicPhoneID.append(",");
             }
*/

            /* DynamicFieldsWebType.append(spinnerMainUrlType.getSelectedItem().toString());
             DynamicFieldsWebType.append(",");*/
//            if (!ContactID.equalsIgnoreCase("")) {
//                params.add(new BasicNameValuePair("contactid", ContactID));
//                params.add(new BasicNameValuePair("phoneid", DynamicPhoneID.substring(0, DynamicPhoneID.length() - 1).toString()));
//                params.add(new BasicNameValuePair("urlid", DynamicURLID.substring(0, DynamicURLID.length() - 1).toString()));
//
//            } else {
//                params.add(new BasicNameValuePair("Flag", LeadOrTaskTag));
//                params.add(new BasicNameValuePair("CId", companyID));
//
//            }
//            params.add(new BasicNameValuePair("Cname", editTextCompany.getText().toString()));
//            params.add(new BasicNameValuePair("Cdesc", editTextAddCompanyDetailsDiscription.getText().toString()));
//            //params.add(new BasicNameValuePair("Cphone",editTextCompanyPhone.getText().toString()));
//            params.add(new BasicNameValuePair("Cphone", ""));
//            params.add(new BasicNameValuePair("Cadd", editTextAddCompanyDetailsAddress.getText().toString()));
//            if (companyCityArray.size() > 0) {
//                params.add(new BasicNameValuePair("Ccity", companyCityArray.get(spinnerCompanyCity.getSelectedItemPosition()).getId()));
//                SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putInt("cCityId", Integer.parseInt(companyCityArray.get(spinnerCompanyCity.getSelectedItemPosition()).getId()));
//                editor.apply();
//                editor.commit();
//            } else {
//                params.add(new BasicNameValuePair("Ccity", "0"));
//                SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putInt("cCityId", 0);
//                editor.apply();
//                editor.commit();
//            }
//
//
//            if (spinnerAddCompanyDetailsState.getSelectedItemPosition() >= 0) {
//                params.add(new BasicNameValuePair("CstateId", stateArray.get(spinnerAddCompanyDetailsState.getSelectedItemPosition()).getId()));
//                if (stateArray.get(spinnerAddCompanyDetailsState.getSelectedItemPosition()).getId().equalsIgnoreCase("0")) {
//                    params.add(new BasicNameValuePair("Cstate", ""));
//                } else {
//                    params.add(new BasicNameValuePair("Cstate", stateArray.get(spinnerAddCompanyDetailsState.getSelectedItemPosition()).getName()));
//                }
//                SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putInt("cStateId", Integer.parseInt(stateArray.get(spinnerAddCompanyDetailsState.getSelectedItemPosition()).getId()));
//                editor.apply();
//                editor.commit();
//            } else {
//
//                params.add(new BasicNameValuePair("Cstate", ""));
//                params.add(new BasicNameValuePair("CstateId", "0"));
//                SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putInt("cStateId", 0);
//                editor.apply();
//                editor.commit();
//
//            }
//            if (spinnerAddCompanyDetailsCountry.getSelectedItemPosition() >= 0) {
//                params.add(new BasicNameValuePair("Ccountry", contaryArray.get(spinnerAddCompanyDetailsCountry.getSelectedItemPosition()).getId()));
//
//            } else {
//                params.add(new BasicNameValuePair("Ccountry", "0"));
//            }
//
////            if (spinnerProductGroup.getSelectedItemPosition() >= 0) {
////                params.add(new BasicNameValuePair("Productgroupid", productGroupArray.get(spinnerProductGroup.getSelectedItemPosition()).getId()));
////
////            } else {
////                params.add(new BasicNameValuePair("Productgroupid", "0"));
////            }
//
//            if (spinnerProduct.getSelectedItemPosition() >= 0) {
//                params.add(new BasicNameValuePair("productid", itemListArray.get(spinnerProduct.getSelectedItemPosition()).getId()));
//
//            } else {
//                params.add(new BasicNameValuePair("productid", "0"));
//            }
//
//
//            params.add(new BasicNameValuePair("CZip", String.valueOf(validation.vNum(editTextAddCompanyDetailsZip.getText()))));
//            //  params.add(new BasicNameValuePair("CZip",editTextAddCompanyDetailsZip.getText().toString()));
//            try {
//
//            } catch (IndexOutOfBoundsException e) {
//
//            }
//
//
//            params.add(new BasicNameValuePair("Fname", editTextFirstName.getText().toString()));
//            params.add(new BasicNameValuePair("Lname", ""));
//            //params.add(new BasicNameValuePair("Lname",editTextLastName.getText().toString()));
//            //params.add(new BasicNameValuePair("JobTitle",editTextJobTittle.getText().toString()));
//            params.add(new BasicNameValuePair("JobTitle", ""));
//
//            if (spinnerCity.getSelectedItemPosition() >= 0) {
//                params.add(new BasicNameValuePair("City", cityArray.get(spinnerCity.getSelectedItemPosition()).getId()));
//                SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putInt("cityId", Integer.parseInt(cityArray.get(spinnerCity.getSelectedItemPosition()).getId()));
//                editor.apply();
//                editor.commit();
//            } else {
//                params.add(new BasicNameValuePair("City", "0"));
//                SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putInt("cityId", 0);
//                editor.apply();
//                editor.commit();
//            }
//
//            params.add(new BasicNameValuePair("stateid", stateArray.get(spinnerState.getSelectedItemPosition()).getId()));
//            SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
//            SharedPreferences.Editor editor = pref.edit();
//            editor.putInt("stateId", Integer.parseInt(stateArray.get(spinnerState.getSelectedItemPosition()).getId()));
//            editor.apply();
//            editor.commit();
//            if (stateArray.get(spinnerState.getSelectedItemPosition()).getId().equalsIgnoreCase("0")) {
//                params.add(new BasicNameValuePair("State", ""));
//            } else {
//                params.add(new BasicNameValuePair("State", stateArray.get(spinnerState.getSelectedItemPosition()).getName()));
//            }
//            params.add(new BasicNameValuePair("Country", contaryArray.get(spinnerCountry.getSelectedItemPosition()).getId()));
//            params.add(new BasicNameValuePair("zip", String.valueOf(validation.vNum(editTextZip.getText()))));
//            //    params.add(new BasicNameValuePair("zip",editTextZip.getText().toString()));
//            params.add(new BasicNameValuePair("Add", editTextAddress.getText().toString()));
//            params.add(new BasicNameValuePair("Statusid", spinnerArrayListStatusID.get(spinnerStatue.getSelectedItemPosition())));
//            params.add(new BasicNameValuePair("Tagid", spinnerArrayListTagID.get(spinnerTag.getSelectedItemPosition())));
//            params.add(new BasicNameValuePair("Leadid", spinnerArrayListLeadSourceID.get(spinnerLeadSource.getSelectedItemPosition())));
//            params.add(new BasicNameValuePair("Owner", spinnerArrayListOwnerID.get(spinnerOwner.getSelectedItemPosition())));
//            params.add(new BasicNameValuePair("Active", String.valueOf(checkboxActive.isChecked() ? "Y" : "N")));
//
//            params.add(new BasicNameValuePair("Background", editTextBackground.getText().toString()));
//            params.add(new BasicNameValuePair("Manager", spinnerArrayListOwnerID.get(spinnerOwner.getSelectedItemPosition())));
//            params.add(new BasicNameValuePair("smid", SMID));
//            if (DynamicFieldskey.length() > 0) {
//                params.add(new BasicNameValuePair("DynamicControls", DynamicFieldskey.substring(0, DynamicFieldskey.length() - 1).toString()));
//            } else {
//                params.add(new BasicNameValuePair("DynamicControls", ""));
//            }
//            if (DynamicFieldsValue.length() > 0) {
//                params.add(new BasicNameValuePair("DynamicControlsValue", DynamicFieldsValue.substring(0, DynamicFieldsValue.length() - 1).toString()));
//            } else {
//                params.add(new BasicNameValuePair("DynamicControlsValue", ""));
//
//            }
//            params.add(new BasicNameValuePair("phnval", DynamicFieldsPhoneNo.substring(0, DynamicFieldsPhoneNo.length() - 1).toString()));
//            params.add(new BasicNameValuePair("phnddlval", DynamicFieldJobTitle.substring(0, DynamicFieldJobTitle.length() - 1).toString()));
//            params.add(new BasicNameValuePair("phncontName", DynamicFieldsName.substring(0, DynamicFieldsName.length() - 1).toString()));
//            params.add(new BasicNameValuePair("emailval", DynamicFieldsEmail.substring(0, DynamicFieldsEmail.length() - 1).toString()));
//            params.add(new BasicNameValuePair("urlval", DynamicFieldsWeb.substring(0, DynamicFieldsWeb.length() - 1).toString()));
//            params.add(new BasicNameValuePair("longitude", longitude));
//            params.add(new BasicNameValuePair("latitude", latitude));
//            params.add(new BasicNameValuePair("lat_long_dt", String.valueOf(System.currentTimeMillis())));
//            params.add(new BasicNameValuePair("image1", selectedImageBase64ImgString));
        }

        protected String doInBackground(String... arg0) {
            URL url = null;
            try {
//                if (ContactID.equalsIgnoreCase("")) {
                    url = new URL(Constant.SERVER_WEBSERVICE_URL+" /XjsSaveContacts_CRM");
//                } else {
//                    url = new URL("http://" + server + "/And_Sync.asmx/XjsUpdateContact_CRM");
//                }
                // here is your URL path
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
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            // if(progressDialog.isShowing()){progressDialog.dismiss();}
            Log.e("Response", "" + server_response);
            if (server_response != null) {
                if (!server_response.isEmpty()) {
                    server_response = server_response.replaceAll("\"", "");
                  //  if (ContactID.equalsIgnoreCase("")) {
                        if (server_response.equalsIgnoreCase("N")) {
                            new Custom_Toast(AddNewContactPage.this, "Try Again !" + server_response).showCustomAlert();

                        } else if (server_response.equalsIgnoreCase("Record Already Exist")) {
                            new Custom_Toast(AddNewContactPage.this, "Try Again !" + server_response).showCustomAlert();
                        } else {
                      //      Contact_id = server_response;
                            Intent intent = new Intent(AddNewContactPage.this, CrmTask.class);
                            intent.putExtra("FromWhere", "addContact");
                            intent.putExtra("Contact_id", server_response);
                            startActivityForResult(intent, 7275);


                        }
//                    } else {
//                        if (server_response.equalsIgnoreCase("Updated Successfully")) {
//                            new Custom_Toast(AddNewContactPage.this, server_response).showCustomAlert();
//                            finish();
//                            Intent intent = new Intent(AddNewContactPage.this, AddContact.class);
//                            startActivity(intent);
//                        } else {
//                            new Custom_Toast(AddNewContactPage.this, "Try Again !" + server_response).showCustomAlert();
//                        }
//                    }
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
}

