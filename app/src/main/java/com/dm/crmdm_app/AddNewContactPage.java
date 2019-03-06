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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.dm.util.Util;

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
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class AddNewContactPage extends AppCompatActivity {
    LinearLayout linearLayoutAddCompanyDetails, linearLayoutdynamicAllBelowfields, linearlayoutdynamicfieldsURL, linearLayoutdynamicfieldForPersonInfo;

    ArrayList<Owner> statusList = new ArrayList<>();
    ArrayList<Owner> employeeList = new ArrayList<>();
    ArrayList<Owner> ownerList = new ArrayList<>();
    ArrayList<Owner> cityArray = new ArrayList<>();
    ArrayList<Owner> industryArray = new ArrayList<>();
    ArrayList<Owner> subindustryArray = new ArrayList<>();
    ArrayList<Owner> productArray = new ArrayList<>();
    ArrayList<Owner> dealerArray = new ArrayList<>();
    Spinner spinnerIdustry, spinnerSubIndustry, spinnerCity, spinnerCompanyCity, spinnerState, spinnerAddCompanyDetailsState, spinnerAddCompanyDetailsCountry, spinnerCountry,
            spinnerStatue, spinnerTag, aacspineerOwner, dealerSpiner, spinnerProduct, spinnerProductGroup, spinnerIndustry,
            spinnerJobTitle, aacspineerStatus, employeeSpinner;

    EditText computerNoText, siteNoText, empNoText, valueText, advDetailText, softwareUsingText, editTextMainEmail, editTextPersonName, editTextAddCompanyDetailsDiscription, editTextAddCompanyDetailsZip, editTextAddCompanyDetailsAddress, editTextFirstName, editTextLastName, editTextJobTittle,
            editTextMainMobNo, editTextMainUrl, party_addbyText, editTextAddress, aacBackground, companyNameText, hqText, phoneText, onDateText, validUptoText;
    ArrayList<Owner> jobTitleArray = new ArrayList<>();
    CheckBox isDealerCheckBox, aacActive, isBlockTeleCaller;
    ImageView imgAddMorePersonInfo;
    String hqcode, hqname, LoginId;
    //Azad
    Button btnAddContact;
    LinearLayout parentLL;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        setHeader();
        computerNoText = findViewById(R.id.computerNoText);
        parentLL = findViewById(R.id.parentLL);
        siteNoText = findViewById(R.id.siteNoText);
        empNoText = findViewById(R.id.empNoText);
        advDetailText = findViewById(R.id.advDetailText);
        softwareUsingText = findViewById(R.id.softwareUsingText);
        aacBackground = findViewById(R.id.aacBackground);
        valueText = findViewById(R.id.valueText);
        hqText = findViewById(R.id.hqText);
        SharedPreferences pref = getSharedPreferences("LoginData", MODE_PRIVATE);
        hqcode = pref.getString("HqCode", "");
        hqname = pref.getString("HqName", "");
        LoginId = pref.getString("LoginId", "");
        hqText.setText(hqname);

        employeeSpinner = findViewById(R.id.aacspineerLeadSource);
        isDealerCheckBox = findViewById(R.id.isDealerCheckBox);
        aacActive = findViewById(R.id.aacActive);
        isBlockTeleCaller = findViewById(R.id.aacblockTeleActive);
        btnAddContact = findViewById(R.id.btnAddContact);

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


        linearLayoutdynamicfieldForPersonInfo = findViewById(R.id.dynamicfieldsPersonInfo);
        onDateText = findViewById(R.id.onDateText);
        validUptoText = findViewById(R.id.validuptoText);

        onDateText.setFocusable(false);
        onDateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDateText.setError(null);
                Util.hideKeyboard(AddNewContactPage.this);
                showDatePicker(1);
            }
        });
        validUptoText.setFocusable(false);
        validUptoText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validUptoText.setError(null);
                Util.hideKeyboard(AddNewContactPage.this);
                showDatePicker(2);
            }
        });
        imgAddMorePersonInfo = findViewById(R.id.addPhone);
        spinnerProduct = findViewById(R.id.aacspineerProduct);
        spinnerIndustry = findViewById(R.id.industrySpinner);
        spinnerSubIndustry = findViewById(R.id.subindustrySpinner);
        aacspineerStatus = findViewById(R.id.aacspineerStatus);
        aacspineerOwner = findViewById(R.id.aacspineerOwner);
        spinnerCity = findViewById(R.id.aacspineerCity);
        spinnerJobTitle = findViewById(R.id.adcspinnerJobTitle);
        getCityFromDb();
        getIndustry();
        getproduct();
        getDesignation();
        getStatus();
        getEmployee();
        getDealers();

        spinnerIndustry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getSubIndustryByIndustry(industryArray.get(i).getName());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        editTextFirstName = findViewById(R.id.aacFirstName);
        companyNameText = findViewById(R.id.aacCompany);
        hqText = findViewById(R.id.hqText);
        phoneText = findViewById(R.id.aacPhone);
        //contact section ...
        editTextPersonName = findViewById(R.id.aacPersonName);
        spinnerJobTitle = findViewById(R.id.adcspinnerJobTitle);
        editTextMainMobNo = findViewById(R.id.aacmbno);
        editTextMainEmail = findViewById(R.id.aacPersonEmail);
        /////////
        editTextMainUrl = findViewById(R.id.aacURL);
        party_addbyText = findViewById(R.id.party_addbyText);
        editTextAddress = findViewById(R.id.aacAddress);


        imgAddMorePersonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addDynamicPersonInfo(AddNewContactPage.this, "personInfo", false, null);
            }
        });

        btnAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validated())
                    new saveDataOnServer().execute();
            }
        });
        parentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.hideKeyboard(AddNewContactPage.this);
            }
        });

    }

    private boolean validated() {
        if (TextUtils.isEmpty(editTextFirstName.getText().toString().trim())) {
            editTextFirstName.setError("Please enter Name.");
            editTextFirstName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(companyNameText.getText().toString().trim())) {
            companyNameText.setError("Please enter Company Name.");
            companyNameText.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(editTextAddress.getText().toString().trim())) {
            editTextAddress.setError("Please enter Address.");
            editTextAddress.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(phoneText.getText().toString().trim())) {
            phoneText.setError("Please enter Phone Number.");
            phoneText.requestFocus();
            return false;

        } else if (TextUtils.isEmpty(editTextPersonName.getText().toString().trim())) {
            editTextPersonName.setError("Please enter Contact Person Name.");
            editTextPersonName.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(editTextMainMobNo.getText().toString().trim())) {
            editTextMainMobNo.setError("Please enter Contact Person's Mobile Number.");
            editTextMainMobNo.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(editTextMainEmail.getText().toString().trim())) {
            editTextMainEmail.setError("Please enter Contact Person Email.");
            editTextMainEmail.requestFocus();

            return false;
        } else if (TextUtils.isEmpty(editTextMainUrl.getText().toString().trim())) {
            editTextMainUrl.setError("Please enter Company URL.");
            editTextMainUrl.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(party_addbyText.getText().toString().trim())) {
            party_addbyText.setError("Please enter Influence by Name.");
            party_addbyText.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(valueText.getText().toString().trim())) {
            valueText.setError("Please enter Value.");
            valueText.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(advDetailText.getText().toString().trim())) {
            advDetailText.setError("Please enter Advertisement Detail.");
            advDetailText.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(empNoText.getText().toString().trim())) {
            empNoText.setError("Please enter Number Of Employees.");
            empNoText.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(siteNoText.getText().toString().trim())) {
            siteNoText.setError("Please enter Site Number.");
            siteNoText.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(computerNoText.getText().toString().trim())) {
            computerNoText.setError("Please enter Number of Computer System used.");
            computerNoText.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(onDateText.getText().toString().trim())) {
            onDateText.setError("Please enter on Date.");
            onDateText.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(validUptoText.getText().toString().trim())) {
            validUptoText.setError("Please enter valid up-to.");
            validUptoText.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(softwareUsingText.getText().toString().trim())) {
            softwareUsingText.setError("Please enter software using.");
            softwareUsingText.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(aacBackground.getText().toString().trim())) {
            aacBackground.setError("Please enter remark.");
            aacBackground.requestFocus();
            return false;
        }
        return true;
    }

    private void setHeader() {
        findViewById(R.id.img_home).setVisibility(View.VISIBLE);
        findViewById(R.id.img_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView headerText = findViewById(R.id.header_text);
        headerText.setText("Add Contact");
    }

    private void getDealers() {
        SharedPreferences pref = getSharedPreferences("EmpData", MODE_PRIVATE);
        String js = pref.getString("emplist", "[]");
        try {
            JSONArray jsonArray = new JSONArray(js);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if (jsonObject.getString("Designation").equalsIgnoreCase("3")) {
                    String id = jsonObject.getString("Id");
                    String name = jsonObject.getString("EmployeeName");
                    Owner data = new Owner();
                    data.setName(name);
                    data.setId(id);
                    dealerArray.add(data);
                }
            }
            if (dealerArray.size() > 0)
                setStateContarySpinner(dealerSpiner, dealerArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    public void getEmployee() {
        SharedPreferences pref = getSharedPreferences("EmpData", MODE_PRIVATE);
        String js = pref.getString("emplist", "[]");
        try {
            JSONArray jsonArray = new JSONArray(js);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("Id");
                String name = jsonObject.getString("EmployeeName");
                Owner data = new Owner();
                data.setName(name);
                data.setId(id);
                employeeList.add(data);
                ownerList.add(data);
            }

            setStateContarySpinner(employeeSpinner, employeeList);
            setStateContarySpinner(aacspineerOwner, ownerList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getStatus() {
        SharedPreferences pref = getSharedPreferences("StatusData", MODE_PRIVATE);
        String js = pref.getString("statuslist", "[]");
        try {
            JSONArray jsonArray = new JSONArray(js);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                String id = jsonObject.getString("Id");
                String name = jsonObject.getString("party_status");
                Owner data = new Owner();
                data.setName(name);
//                data.setId(id);
                statusList.add(data);
            }

            setStateContarySpinner(aacspineerStatus, statusList);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void getDesignation() {
        SharedPreferences pref = getSharedPreferences("DesignationData", MODE_PRIVATE);
        String js = pref.getString("desiglist", "[]");
        try {
            JSONArray jsonArray = new JSONArray(js);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String id = jsonObject.getString("Id");
                String name = jsonObject.getString("Designation");
                Owner data = new Owner();
                data.setName(name);
                data.setId(id);
                jobTitleArray.add(data);
            }

            setStateContarySpinner(spinnerJobTitle, jobTitleArray);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    // DB FUnctions to read data ....

    public void getCityFromDb() {

        DatabaseConnection db = new DatabaseConnection(this);
        SQLiteDatabase dbx = db.getReadableDatabase();
        String query = "select webcode,name from MastCity order by name";
        Cursor cursor = dbx.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Owner data = new Owner();
            data.setId(cursor.getString(0));
            data.setName(cursor.getString(1));
            cityArray.add(data);
//            cityIdList.add(cursor.getString(0));
//            cityNameList.add(cursor.getString(1));
        }
        setStateContarySpinner(spinnerCity, cityArray);
        cursor.close();
        dbx.close();

    }


    public void getproduct() {

        DatabaseConnection db = new DatabaseConnection(this);
        SQLiteDatabase dbx = db.getReadableDatabase();
        String query = "select webcode,name from MastProduct order by name";
        Cursor cursor = dbx.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Owner data = new Owner();
            data.setId(cursor.getString(0));
            data.setName(cursor.getString(1));
            productArray.add(data);

        }
        setStateContarySpinner(spinnerProduct, productArray);
        cursor.close();
        dbx.close();

    }


    public void getIndustry() {

        DatabaseConnection db = new DatabaseConnection(this);
        SQLiteDatabase dbx = db.getReadableDatabase();
        String query = "select webcode,name from Industrymast order by name";
        Cursor cursor = dbx.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Owner data = new Owner();
            data.setId(cursor.getString(0));
            data.setName(cursor.getString(1));
            industryArray.add(data);

        }
        setStateContarySpinner(spinnerIndustry, industryArray);
        cursor.close();
        dbx.close();

    }

    public void getSubIndustryByIndustry(String indName) {

        subindustryArray.clear();
        DatabaseConnection db = new DatabaseConnection(this);
        SQLiteDatabase dbx = db.getReadableDatabase();
        String query = "select webcode,name from SubIndustrymast where indname='" + indName + "' order by name";
        Cursor cursor = dbx.rawQuery(query, null);
        while (cursor.moveToNext()) {
            Owner data = new Owner();
            data.setId(cursor.getString(0));
            data.setName(cursor.getString(1));
            subindustryArray.add(data);
//            cityIdList.add(cursor.getString(0));
//            cityNameList.add(cursor.getString(1));
        }
        setStateContarySpinner(spinnerSubIndustry, subindustryArray);
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

                String strDate = (dayOfMonth < 10 ? ("0" + dayOfMonth) : (dayOfMonth)) + "/" + (((monthOfYear + 1) < 10 ? ("0" + (monthOfYear + 1)) : (monthOfYear + 1))) + "/" + year;
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
                if (n == 1) {
                    onDateText.setText(format2.format(date));
                } else {
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

        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(String... arg0) {
            URL url = null;
            try {
//                if (ContactID.equalsIgnoreCase("")) {
                url = new URL(Constant.SERVER_WEBSERVICE_URL + "XjsSaveContacts_CRM");
//                } else {
//                    url = new URL("http://" + server + "/And_Sync.asmx/XjsUpdateContact_CRM");
//                }
                // here is your URL path
                params.add(new BasicNameValuePair("Cname", companyNameText.getText().toString()));
                params.add(new BasicNameValuePair("Fname", editTextFirstName.getText().toString()));
                params.add(new BasicNameValuePair("JobTitle", industryArray.get(spinnerIndustry.getSelectedItemPosition()).getName()));
                params.add(new BasicNameValuePair("Add", editTextAddress.getText().toString()));

                params.add(new BasicNameValuePair("Cityid", cityArray.get(spinnerCity.getSelectedItemPosition()).getId()));
                params.add(new BasicNameValuePair("CityName", cityArray.get(spinnerCity.getSelectedItemPosition()).getName()));

                params.add(new BasicNameValuePair("Status", statusList.get(aacspineerStatus.getSelectedItemPosition()).getName()));
//                params.add(new BasicNameValuePair("Tagid", statusList.get(aacspineerStatus.getSelectedItemPosition()).getId())); // status id

                params.add(new BasicNameValuePair("PartyRefby", employeeList.get(employeeSpinner.getSelectedItemPosition()).getName()));
                params.add(new BasicNameValuePair("Owner", ownerList.get(aacspineerOwner.getSelectedItemPosition()).getName()));

                params.add(new BasicNameValuePair("Active", String.valueOf(aacActive.isChecked())));
                params.add(new BasicNameValuePair("phnval", editTextMainMobNo.getText().toString()));

                params.add(new BasicNameValuePair("phncontName", editTextPersonName.getText().toString()));
                params.add(new BasicNameValuePair("phnddlval", jobTitleArray.get(spinnerJobTitle.getSelectedItemPosition()).getName()));

                params.add(new BasicNameValuePair("url", editTextMainUrl.getText().toString()));
                params.add(new BasicNameValuePair("emailval", editTextMainEmail.getText().toString()));
//            params.add(new BasicNameValuePair("Flag", editTextCompany.getText().toString()));
                params.add(new BasicNameValuePair("longitude", longitude.substring(0, 6)));
                params.add(new BasicNameValuePair("latitude", latitude.substring(0, 6)));
//            params.add(new BasicNameValuePair("lat_long_dt", String.valueOf(System.currentTimeMillis())));

                params.add(new BasicNameValuePair("productid", productArray.get(spinnerProduct.getSelectedItemPosition()).getId()));
                params.add(new BasicNameValuePair("IndustrySub", subindustryArray.get(spinnerSubIndustry.getSelectedItemPosition()).getName()));
                params.add(new BasicNameValuePair("Phone", phoneText.getText().toString().trim()));
                params.add(new BasicNameValuePair("PartyRefName", employeeList.get(employeeSpinner.getSelectedItemPosition()).getName()));
                params.add(new BasicNameValuePair("OnDate", onDateText.getText().toString()));
//                params.add(new BasicNameValuePair("PartyAddedby", party_addbyText.getText().toString()));

                params.add(new BasicNameValuePair("Ad_Detail", advDetailText.getText().toString()));
                params.add(new BasicNameValuePair("noemp", empNoText.getText().toString()));
                params.add(new BasicNameValuePair("nocom", computerNoText.getText().toString().trim()));
                params.add(new BasicNameValuePair("nosite", siteNoText.getText().toString()));
                params.add(new BasicNameValuePair("Softwareusing", softwareUsingText.getText().toString()));
                params.add(new BasicNameValuePair("businessremark", aacBackground.getText().toString()));
                if (dealerArray.size() > 0)
                    params.add(new BasicNameValuePair("Dealerid", dealerArray.get(dealerSpiner.getSelectedItemPosition()).getId()));
                else
                    params.add(new BasicNameValuePair("Dealerid", "1"));
                params.add(new BasicNameValuePair("Valid_Upto", validUptoText.getText().toString()));
                params.add(new BasicNameValuePair("Influence_Any", party_addbyText.getText().toString().trim()));
                params.add(new BasicNameValuePair("Block_Telecaller", String.valueOf(isBlockTeleCaller.isChecked())));
                params.add(new BasicNameValuePair("Reason", "sada"));

                params.add(new BasicNameValuePair("Value", valueText.getText().toString()));
                params.add(new BasicNameValuePair("Userid", LoginId));
                params.add(new BasicNameValuePair("industrycode", industryArray.get(spinnerIndustry.getSelectedItemPosition()).getId()));
                params.add(new BasicNameValuePair("HQCode", hqcode));

                params.add(new BasicNameValuePair("HQName", "UNDEFINED"/*hqname*/));
                params.add(new BasicNameValuePair("isdealer", String.valueOf(isDealerCheckBox.isChecked())));
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

                    StringBuffer sb = new StringBuffer();
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
                        new Custom_Toast(AddNewContactPage.this, "Record Inserted Successfully").showCustomAlert();
//                        Intent intent = new Intent(AddNewContactPage.this, CrmTask.class);
//                        intent.putExtra("FromWhere", "addContact");
//                        intent.putExtra("Contact_id", server_response);
//                        startActivity(intent);
                        finish();


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

