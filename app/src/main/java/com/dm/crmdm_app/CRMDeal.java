package com.dm.crmdm_app;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomAdapterCRMStreamInfo;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.library.DbCon;
import com.dm.library.FilePath;
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
import java.io.File;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import it.sauronsoftware.ftp4j.FTPClient;

public class CRMDeal extends AppCompatActivity {
    boolean monthflag=false;
    boolean isTotalDone = false;
    String currentDate;
    TextView textMultimonthdeal,revert,totalValueAMT,uploadfileNameTextView;
    Button  buttonSave,buttonCancel,buttonReset,buttonViewAtachment;
    Spinner dealStageSpinner;
    EditText editTextDealName,editTextdealDate,editTextcloseDate,editTextAmount,editmonth,commisionPercentAmtText,commisionpercentText,commissionAmount,
            editTextdealNote;//qtyText,rateText,amountText,milstoneText,commisionpercentText,commissionAmount;
    LinearLayout LinearLayoutNormaldeal,LinearLayoutMultimonthdeal,revertLinearLayout, milstoneContainerLayout, milesstoneDataLayout;
    ProgressDialog progressDialog;
    ArrayList<Owner> stageArrayList=new ArrayList<>();
    ConnectionDetector connectionDetector;
    Validation validation;
    String Contact_id="",Ref_DocID="",TDocID="";
    String SMID;
    SharedPreferences preferences2;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    String server;String Flag;
    int i = 0;
    String latitude,longitude,latlngtime;

    double totalAmount ;
    boolean isComeforEdit = false;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        connectionDetector=new ConnectionDetector(CRMDeal.this);

        /*this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
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
        this.getWindow().setLayout(width-reducewidth,height-reduceheight);*/
        //this.getWindow().setLayout(width-reducewidth,((WindowManager.LayoutParams) params).WRAP_CONTENT);
        setContentView(R.layout.activity_crmdeal);

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
        tv.setText("Add Deal");

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

        milesstoneDataLayout = (LinearLayout)findViewById(R.id.milesLinearLayout);


        milstoneContainerLayout = (LinearLayout)findViewById(R.id.milesStoneContainer);




        validation=new Validation(CRMDeal.this);



        commisionpercentText = (EditText)findViewById(R.id.commisionPercentText);
        commisionPercentAmtText = (EditText)findViewById(R.id.commisionPercentAmtText);
        commissionAmount = (EditText)findViewById(R.id.commisionamountText);
        commissionAmount.setGravity(Gravity.TOP| Gravity.RIGHT);
        commissionAmount.setKeyListener(DigitsKeyListener.getInstance(true,true));
        preferences2=this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SMID=preferences2.getString("CONPERID_SESSION", null);
        appDataController1=new AppDataController(CRMDeal.this);
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();
        Intent intent=getIntent();
        stageArrayList.clear();
        LinearLayoutNormaldeal =(LinearLayout)findViewById(R.id.LinearLayoutNormaldeal);
        LinearLayoutMultimonthdeal=(LinearLayout)findViewById(R.id.LinearLayoutMultimonthdeal);
        revertLinearLayout=(LinearLayout)findViewById(R.id.revertLinearLayout);
        textMultimonthdeal=(TextView)findViewById(R.id.textMultimonthdeal);
        editTextDealName=(EditText)findViewById(R.id.textDealName);
        editTextdealNote=(EditText)findViewById(R.id.textdealNote);
        editTextdealDate=(EditText)findViewById(R.id.dealDate);
        editTextdealDate.setInputType(InputType.TYPE_NULL);
        editTextdealDate.setFocusable(false);
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        Calendar cal=Calendar.getInstance();
        currentDate=df.format(cal.getTime());
        totalValueAMT=(TextView)findViewById(R.id.totalValueAMT);
        editTextdealDate.setText(currentDate);
        dealStageSpinner=(Spinner)findViewById(R.id.statusSpinner);
        revert=(TextView)findViewById(R.id.revert);
        editTextcloseDate=(EditText)findViewById(R.id.closeDate);
        editTextcloseDate.setInputType(InputType.TYPE_NULL);
        editTextcloseDate.setFocusable(false);
        cal.add(Calendar.DATE, 30);
        currentDate=df.format(cal.getTime());
        editTextcloseDate.setText(currentDate);
        uploadfileNameTextView=(TextView)findViewById(R.id.uploadfileName);
        editTextAmount=(EditText)findViewById(R.id.amountText);
        editmonth=(EditText)findViewById(R.id.editmonth);

        buttonReset=(Button)findViewById(R.id.asReset);
        buttonSave=(Button)findViewById(R.id.asSubmit);
        buttonCancel=(Button)findViewById(R.id.asCancel);
        buttonViewAtachment=(Button)findViewById(R.id.viewAtachment);
        if(intent !=null){
            Flag=intent.getStringExtra("Flag");
            Contact_id=intent.getStringExtra("Contact_id");
            Ref_DocID=intent.getStringExtra("Ref_DocID");
            TDocID=intent.getStringExtra("TDocID");
            if(!TDocID.equalsIgnoreCase("")){
                buttonSave.setText("Update");
                //buttonUpload.setVisibility(View.GONE);
                buttonViewAtachment.setVisibility(View.VISIBLE);
            }

        }
        /*editTextAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(monthflag){
                    if(!editmonth.getText().toString().isEmpty()){
                        if(editable.length()>0){
                            try{
                                Double month=Double.parseDouble(editmonth.getText().toString());
                                Double amt=Double.parseDouble(String.valueOf(editable));
                                totalValueAMT.setText("monnths(Total Value Rs."+validation.round(new BigDecimal(amt*month),2,false).toPlainString()+")");
                            }catch (NumberFormatException e){
                                totalValueAMT.setText("monnths(Total Value Rs.0.0)");
                            }
                        }
                        else
                        {
                            totalValueAMT.setText("monnths(Total Value Rs.0.0)");
                        }
                    }
                    else
                    {
                        totalValueAMT.setText("monnths(Total Value Rs.0.0)");
                    }
                }
                else
                {
                    totalValueAMT.setText("monnths(Total Value Rs.0.0)");
                }

            }
        });*/
        editmonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(monthflag){
                    if(!editTextAmount.getText().toString().isEmpty()){
                        if(editable.length()>0){
                            try{
                                Double amt=Double.parseDouble(editTextAmount.getText().toString());
                                Double month=Double.parseDouble(String.valueOf(editable));
                                totalValueAMT.setText("monnths(Total Value Rs."+validation.round(new BigDecimal(amt*month),2,false).toPlainString()+")");
                            }catch (NumberFormatException e){
                                totalValueAMT.setText("monnths(Total Value Rs.0.0)");
                            }
                        }
                        else
                        {
                            totalValueAMT.setText("monnths(Total Value Rs.0.0)");
                        }
                    }
                    else
                    {
                        totalValueAMT.setText("monnths(Total Value Rs.0.0)");
                    }
                }
                else
                {
                    totalValueAMT.setText("monnths(Total Value Rs.0.0)");
                }

            }
        });

        LinearLayoutMultimonthdeal.setVisibility(View.GONE);
        revertLinearLayout.setVisibility(View.GONE);
        if(connectionDetector.isConnectingToInternet()){
            new getingDealStage().execute();
        }
//        textMultimonthdeal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                monthflag=true;
//                LinearLayoutNormaldeal.setVisibility(View.GONE);
//                LinearLayoutMultimonthdeal.setVisibility(View.VISIBLE);
//                revertLinearLayout.setVisibility(View.VISIBLE);
//            }
//        });
//        revert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                monthflag=false;
//                LinearLayoutMultimonthdeal.setVisibility(View.GONE);
//                revertLinearLayout.setVisibility(View.GONE);
//                LinearLayoutNormaldeal.setVisibility(View.VISIBLE);
//            }
//        });
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0; i < milesstoneDataLayout.getChildCount();i++) {
                    JSONObject jsonObject = new JSONObject();
                    LinearLayout l = (LinearLayout) milesstoneDataLayout.getChildAt(i);
                    LinearLayout ll = (LinearLayout) l.getChildAt(0);
                    TextInputLayout milesStoneText = (TextInputLayout) ll.getChildAt(0);
                    TextInputLayout amounttext = (TextInputLayout) ll.getChildAt(3);
                    if(milesStoneText.getEditText().getText().toString().isEmpty())
                    {

                        new Custom_Toast(CRMDeal.this,"Please Enter Payment Terms").showCustomAlert();
                        milesStoneText.getEditText().requestFocus();
                        return;
                    }
                    if(!milesStoneText.getEditText().getText().toString().isEmpty() && amounttext.getEditText().getText().toString().isEmpty())
                    {
                        new Custom_Toast(CRMDeal.this,"Please Enter Amount").showCustomAlert();
                        amounttext.getEditText().requestFocus();
                        return;
                    }
                }
                if(!commisionpercentText.getText().toString().isEmpty() && Double.parseDouble(commisionpercentText.getText().toString())>100)
                {
                    new Custom_Toast(CRMDeal.this,"Commission can not be more than 100").showCustomAlert();
                    return;
                }

                    if(connectionDetector.isConnectingToInternet()){
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
                    else{new Custom_Toast(CRMDeal.this,"Try again! Check your internet connection.").showCustomAlert();}

            }
        });
        editTextdealDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view.getId(),"Deal");
            }
        });
        editTextcloseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker(view.getId(),"Close");
            }
        });
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
//        buttonUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("*/*");
//                if (Build.VERSION.SDK_INT < 19) {
//                    intent.setAction(Intent.ACTION_GET_CONTENT);
//                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                    intent = Intent.createChooser(intent, "Select file");
//                } else {
//                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                    String[] mimetypes = { "*/*"};
//                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
//                }
//                startActivityForResult(intent,0);
//            }
//        });

       /* buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                if (Build.VERSION.SDK_INT < 19) {
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent = Intent.createChooser(intent, "Select file");
                } else {
                    intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);


//                    String[] mimetypes = { "**//*"};
                    String[] mimeTypes =
                            {"application/msword",
                                    "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                    "application/vnd.ms-powerpoint",
                                    "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                    "application/vnd.ms-excel",
                                    "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                                    "text/plain",
                                    "application/pdf",
                                    "application/zip",
                                    "image*//*"};

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "**//*");
                        if (mimeTypes.length > 0) {
                            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                        }
                    } else {
                        String mimeTypesStr = "";
                        for (String mimeType : mimeTypes) {
                            mimeTypesStr += mimeType + "|";
                        }
                        intent.setType(mimeTypesStr.substring(0,mimeTypesStr.length() - 1));
                    }

//                    intent.setType("image*//*|application/pdf|audio*//*");
//                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                }
                startActivityForResult(intent,0);
            }
        });*/





        commisionpercentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (!isComeforEdit) {
                        totalAmount = 0;
//                    EditText amttext = null;
                        calculateCommissionAmount(editable.toString());
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
               // if(!isTotalDone) {
//                    for (int x = 0; x < milesstoneDataLayout.getChildCount(); x++) {
//                        LinearLayout child = (LinearLayout) milesstoneDataLayout.getChildAt(x);
//                        LinearLayout ll = (LinearLayout) child.getChildAt(0);
//                        amttext = (EditText) ll.getChildAt(3);
//                        if(!amttext.getText().toString().isEmpty())
//                        totalAmount = totalAmount+Double.parseDouble(amttext.getText().toString());
//
//                  //  }
//                   // isTotalDone=true;
//                }
//                if(commisionpercentText.getText().toString().length()>0)
//                    if(Double.parseDouble(commisionpercentText.getText().toString()) > 100)
//                    {
//                        Toast.makeText(CRMDeal.this, "Commission Percentage value can not be greater than 100", Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        commissionAmount.setText(""+( totalAmount- (totalAmount * (Double.parseDouble(commisionpercentText.getText().toString())) / 100)));
//                    }

            }
        });

        commisionPercentAmtText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                ifeditedCmAmt = true;
                return false;
            }
        });


        commisionPercentAmtText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
               try {
                   if (!isComeforEdit) {
                       if (ifeditedCmAmt) {
                           calculateCommissionAmount("");
                       }
                   }
               }
               catch (Exception e)
               {

               }

            }
        });
        buttonViewAtachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAtachtment=new Intent(CRMDeal.this, DealsAttachments.class);
                intentAtachtment.putExtra("TDocID",TDocID);
                startActivity(intentAtachtment);
                finish();
            }
        });
        if(TDocID.equalsIgnoreCase(""))
        addMilstone();
    }



    boolean ifeditedCmAmt;
    public void calculateCommissionAmount(String s)
    {
        double totalAmt = 0;
        for (int x = 0; x < milesstoneDataLayout.getChildCount(); x++) {
            LinearLayout child = (LinearLayout) milesstoneDataLayout.getChildAt(x);
            LinearLayout ll = (LinearLayout) child.getChildAt(0);
            TextInputLayout amttext = (TextInputLayout) ll.getChildAt(3);
//            EditText amtvaltext = (EditText)ll.getChildAt(0);
            if(!amttext.getEditText().getText().toString().isEmpty())
            {

                totalAmt = totalAmt+Double.parseDouble(amttext.getEditText().getText().toString());
            }


//            if(commisionpercentText.getText().toString().isEmpty())
//            {
//                commissionAmount.setText("");
//                commisionPercentAmtText.setText("");
//            }
            //  }
            // isTotalDone=true;
        }

        if(ifeditedCmAmt) {
            if (!commisionPercentAmtText.getText().toString().isEmpty())
                commissionAmount.setText("" + round(new BigDecimal((totalAmt -(Double.parseDouble(commisionPercentAmtText.getText().toString())) )),2,false).toPlainString());


        }
        else
        {
            if (!commisionpercentText.getText().toString().isEmpty())
            {
                commisionPercentAmtText.setText("" + (totalAmt * (Double.parseDouble(commisionpercentText.getText().toString())) / 100));
                double tot = ( totalAmt- (totalAmt * (Double.parseDouble(commisionpercentText.getText().toString())) / 100));
                commissionAmount.setText(round(new BigDecimal(tot),2,false).toPlainString());
            }
            else
            {
                commissionAmount.setText(round(new BigDecimal(totalAmt),2,false).toPlainString());
            }

        }
//        if(!commisionPercentAmtText.getText().toString().isEmpty())
//        {
//
//            commissionAmount.setText(""+( totalAmt- (totalAmt * (Double.parseDouble(commisionPercentAmtText.getText().toString())) / 100)));
//
//        }

    }

//    public void  addNewMilestone(View v)
//    {
//        View child = getLayoutInflater().inflate(R.layout.milestone_layout, null);
//        milesstoneDataLayout.addView(child);
//    }

    String fullFilePath = null;
    String filename = "";
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         fullFilePath = null;
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            if (data == null) {
                //no data present
                return;
            }
            Uri selectedFileUri = data.getData();
            fullFilePath = FilePath.getPath(this, selectedFileUri);
            Log.i("", "Selected File Path:" + fullFilePath);
            if (fullFilePath != null && !fullFilePath.equals("")) {
                 filename=fullFilePath.substring(fullFilePath.lastIndexOf("/")+1);
                uploadfileNameTextView.setText(filename);
//                new UploadData().execute(fullFilePath);
                //dialog.dismiss();
            } else {
                Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
            }
        }
    }*/
    /*************************************************************************************************************************************/

//    ArrayList<String> fullFilePathList = new ArrayList<>();
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        try {
//            fullFilePathList.clear();
//            imagesEncodedList = new ArrayList<String>();
//            String fullFilePath = null;
//            if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
//                if (data == null) {
//                    //no data present
//                    return;
//                }
//
//
//                ClipData mClipData = data.getClipData();
//                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
//                for (int i = 0; i < mClipData.getItemCount(); i++) {
//
//                    ClipData.Item item = mClipData.getItemAt(i);
//                    Uri uri = item.getUri();
//                    mArrayUri.add(uri);
//                    fullFilePath = FilePath.getPath(this, uri);
//                    if (fullFilePath != null && !fullFilePath.equals("")) {
//                        String filename = fullFilePath.substring(fullFilePath.lastIndexOf("/") + 1);
//                        uploadfileNameTextView.setText(uploadfileNameTextView.getText().toString() + filename);
//                        imagesEncodedList.add(filename);
////                        fullFilePathList.add(fullFilePath);
//                        new UploadData().execute(fullFilePath);
//                    }
//                }
//
//
////                Uri selectedFileUri = data.getData();
////                fullFilePath = FilePath.getPath(this, selectedFileUri);
////                Log.i("", "Selected File Path:" + fullFilePath);
////                if (fullFilePath != null && !fullFilePath.equals("")) {
////                    String filename=fullFilePath.substring(fullFilePath.lastIndexOf("/")+1);
////                    uploadfileNameTextView.setText(filename);
////                    new UploadData().execute(fullFilePath);
////                    //dialog.dismiss();
////                } else {
////                    Toast.makeText(this, "Cannot upload file to server", Toast.LENGTH_SHORT).show();
////                }
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }
//    }

    String imageEncoded;
    List<String> imagesEncodedList;
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            // When an Image is picked
            if (requestCode == 0 && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                imagesEncodedList = new ArrayList<String>();
                if(data.getData()!=null){

                    Uri mImageUri=data.getData();

                    // Get the cursor
                    Cursor cursor = getContentResolver().query(mImageUri,
                            filePathColumn, null, null, null);
                    // Move to first row
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);

                    imageEncoded  = cursor.getString(columnIndex);
                    cursor.close();

                } else {
                    if (data.getClipData() != null) {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            // Get the cursor
                            Cursor cursor = getContentResolver().query(uri, filePathColumn, null, null, null);
                            // Move to first row
                            cursor.moveToFirst();

                            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                            imageEncoded  = cursor.getString(columnIndex);

                            imagesEncodedList.add(imageEncoded);
                            uploadfileNameTextView.setText(imageEncoded);
                            String filename=imageEncoded.substring(imageEncoded.lastIndexOf("/")+1);
                            uploadfileNameTextView.setText(uploadfileNameTextView.getText().toString()+filename+"\n");
                            new UploadData().execute(imageEncoded);
                            cursor.close();

                        }
                        Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                    }
                }
            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG)
                    .show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }*/





    public static BigDecimal round(BigDecimal d, int scale, boolean roundUp) {
        int mode = (roundUp) ? BigDecimal.ROUND_UP : BigDecimal.ROUND_DOWN;
        return d.setScale(scale, mode);
    }


    int kt = 0;
    public class saveDataOnServer extends AsyncTask<String , Void ,String> {
        String server_response;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            progressDialog = new ProgressDialog(CRMDeal.this);
            progressDialog.setMessage("Save Deal");
            progressDialog.setCancelable(false);
            progressDialog.show();

            if(fullFilePath != null)
                insertFile(fullFilePath);

                params.add(new BasicNameValuePair("Task_DealID", TDocID));
                params.add(new BasicNameValuePair("CRM_TaskDocID", Ref_DocID));
                params.add(new BasicNameValuePair("smid", SMID));
                params.add(new BasicNameValuePair("DealName", ""));
//            params.add(new BasicNameValuePair("DealName",editTextDealName.getText().toString()));
                //  params.add(new BasicNameValuePair("Amount",editTextAmount.getText().toString()));
                params.add(new BasicNameValuePair("DealDate", editTextdealDate.getText().toString()));
                params.add(new BasicNameValuePair("ExpClsDt", editTextcloseDate.getText().toString()));
                // params.add(new BasicNameValuePair("DealStage","p"));
                //  params.add(new BasicNameValuePair("DealStagePerc",stageArrayList.get(dealStageSpinner.getSelectedItemPosition()).getName()));
                params.add(new BasicNameValuePair("DealNote", editTextdealNote.getText().toString()));
                params.add(new BasicNameValuePair("TotalDealAmt", "" + commissionAmount.getText().toString()));
                params.add(new BasicNameValuePair("longitude", longitude));
                params.add(new BasicNameValuePair("latitude", latitude));
                params.add(new BasicNameValuePair("lat_long_dt", String.valueOf(System.currentTimeMillis())));
//            if(!filePathForSend.isEmpty()) {
//                params.add(new BasicNameValuePair("FilePath","/CRM_UploadFile/"+fileName));
//                params.add(new BasicNameValuePair("Filename",""+SMID+System.currentTimeMillis()+""+ fileName));
//            }
                //params.add(new BasicNameValuePair("MonthlyDeal",(editmonth.getText().toString().isEmpty())?"1":editmonth.getText().toString()));
                // params.add(new BasicNameValuePair("TotalDealAmt",validation.round(new BigDecimal(Double.parseDouble(editTextAmount.getText().toString())*Double.parseDouble((editmonth.getText().toString().isEmpty())?"1":editmonth.getText().toString())),2,false).toPlainString()));
                //  params.add(new BasicNameValuePair("MonthlyDeal",(editmonth.getText().toString().isEmpty())?"1":editmonth.getText().toString()));

                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < milesstoneDataLayout.getChildCount(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    LinearLayout l = (LinearLayout) milesstoneDataLayout.getChildAt(i);
                    LinearLayout ll = (LinearLayout) l.getChildAt(0);
                    TextInputLayout milesStoneText = (TextInputLayout) ll.getChildAt(0);

                    TextInputLayout qtyText = (TextInputLayout) ll.getChildAt(1);
                    TextInputLayout rate = (TextInputLayout) ll.getChildAt(2);
                    TextInputLayout amtText = (TextInputLayout) ll.getChildAt(3);
                    try {

                        jsonObject.put("id", "" + milesStoneText.getEditText().getTag());

                        jsonObject.put("milestone", milesStoneText.getEditText().getText().toString());
                        if (!qtyText.getEditText().getText().toString().isEmpty()) {
                            jsonObject.put("qty", qtyText.getEditText().getText().toString());
                        } else {
                            jsonObject.put("qty", "0");
                        }
                        if (!rate.getEditText().getText().toString().isEmpty()) {
                            jsonObject.put("rate", rate.getEditText().getText().toString());
                        } else {
                            jsonObject.put("rate", "0");
                        }
                        if (!amtText.getEditText().getText().toString().isEmpty()) {
                            jsonObject.put("amount", amtText.getEditText().getText().toString());
                        } else {
                            jsonObject.put("amount", "0");
                        }
                        if(!milesStoneText.getEditText().getText().toString().isEmpty())
                        jsonArray.put(jsonObject);
                    } catch (Exception e) {

                    }
                }
                JSONObject json = new JSONObject();


                params.add(new BasicNameValuePair("milestones", "" + jsonArray));

//                JSONArray jsonImageArray = new JSONArray();
//            if (fullFilePath != null) {
////                    for (int i = 0; i < imagesEncodedList.size(); i++) {
//                try {
//                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("FilePath", "" + "/CRM_UploadFile/" + filename);
//                    jsonObject.put("FileName", "" + filename);
//                    jsonImageArray.put(jsonObject);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                // }
//
//             //   params.add(new BasicNameValuePair("imagedata", "" + jsonImageArray));
//            }
//            else {
//                //params.add(new BasicNameValuePair("imagedata", "" + ""));
//            }
                //  params.add(new BasicNameValuePair("amount",amountText.getText().toString()));
//            params.add(new BasicNameValuePair("qty",qtyText.getText().toString()));
//            params.add(new BasicNameValuePair("rate",rateText.getText().toString()));
                if (commisionpercentText.getText().toString().isEmpty()) {
                    params.add(new BasicNameValuePair("commper", "0"));
                } else {
                    params.add(new BasicNameValuePair("commper", commisionpercentText.getText().toString()));
                }
                if (commisionpercentText.getText().toString().isEmpty()) {
                    params.add(new BasicNameValuePair("commamt", "0"));
                } else {
                    params.add(new BasicNameValuePair("commamt", commisionPercentAmtText.getText().toString()));
                }

        }
        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://"+server+"/And_Sync.asmx/XjsSaveDealMultipart_CRM"); // here is your URL path
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
           new Handler().postDelayed(new Runnable() {
               @Override
               public void run() {
                   if(progressDialog != null)
                   {
                       progressDialog.dismiss();
                   }
                   // if(progressDialog.isShowing()){progressDialog.dismiss();}
                   Log.e("Response", "" + server_response);
                   if(server_response != null) {
                       if (!server_response.isEmpty()) {
                           server_response = server_response.replaceAll("\"", "");
                           if (!server_response.equalsIgnoreCase("n")) {
                               TDocID = server_response;
                               new Custom_Toast(CRMDeal.this, "Saved Sussesfully").showCustomAlert();
                               Intent intent=new Intent(CRMDeal.this, DealsAttachments.class);
                               intent.putExtra("TDocID",TDocID);
                               startActivity(intent);
                               finish();
                               /*if(fullFilePathList.size()>0)
                               {
                                   TDocID = server_response;
                                   new saveDealFiles().execute();
                               }
                               else
                               {
                                   finish();
                               }*/

                           } else {
                               new Custom_Toast(CRMDeal
                                       .this,"Try Again!").showCustomAlert();
                           }
                       }
                   }
               }
           },1500);


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
  /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            String fullFilePath = null;
            Uri uri = data.getData();

            if (uri != null) {
                // TODO: handle your case
               // dialog = ProgressDialog.show(CRMDeal.this, "", "Uploading file...", true);
                try {
                    fullFilePath= getFilePath(CRMDeal.this,uri);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
                if(fullFilePath != null){
                    insertFile(fullFilePath);
                }
            }
        }
    }*/
    public static String getFilePath(Context context, Uri uri) throws URISyntaxException {
        String selection = null;
        String[] selectionArgs = null;
        // Uri is different in versions after KITKAT (Android 4.4), we need to
        if (Build.VERSION.SDK_INT >= 19 && DocumentsContract.isDocumentUri(context.getApplicationContext(), uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                return Environment.getExternalStorageDirectory() + "/" + split[1];
            }
            else if (isDownloadsDocument(uri))
            {
                final String id = DocumentsContract.getDocumentId(uri);
                uri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            }
            else if (isMediaDocument(uri))
            {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("image".equals(type)) {
                    uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                selection = "_id=?";
                selectionArgs = new String[]{
                        split[1]
                };
            }
        }
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = {
                    MediaStore.Images.Media.DATA
            };
            Cursor cursor = null;
            try {
                cursor = context.getContentResolver()
                        .query(uri, projection, selection, selectionArgs, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    ArrayList<String> fileNameList = new ArrayList<>();
    ArrayList<String> filePathList = new ArrayList<>();
    public void insertFile(String filePath)
    {
        String Host = null,Username = null,Password = null,FtpDirectory = null;
        File file = new File(filePath);
        filePathForSend = file.getAbsolutePath();
        filePathList.add(filePathForSend);
        fileNameList.add( file.getName());
        DbCon dbcon = new DbCon(this);
        SQLiteDatabase database = dbcon.open();
        String qry = "select * from AppEnviro";
        Cursor cursor = database.rawQuery(qry, null);
        try {
            while (cursor.moveToNext()) {
                Host          = cursor.getString(5);
                Username      = cursor.getString(6);
                Password      = cursor.getString(7);
                FtpDirectory  = cursor.getString(8);
            }
        } catch (Exception E) {
            System.out.print("exception:"+E);
        }
        dbcon.close();


        try {

            FTPClient client = new FTPClient();
            client.connect(Host);
            client.login(Username, Password); //this is the login credentials of your ftpserver. Ensure to use valid username and password otherwise it throws exception
            client.setType(FTPClient.TYPE_BINARY);
            try {

                client.changeDirectory("CRM_UploadFile");  //I want to upload picture in MyPictures directory/folder. you can use your own.
            }
            catch (Exception e) {

//                      client.createDirectory(Constant.FTP_DIRECTORY);
//                      client.changeDirectory(Constant.FTP_DIRECTORY);
                System.out.println(e);

            }
            client.upload(file); //this is actual file uploading on FtpServer in specified directory/folder

            client.disconnect(true);   //after file upload, don't forget to disconnect from FtpServer.
            //file.delete();

//            if(pDialog !=null)
//            pDialog.dismiss();
        }
        catch (Exception e) {
            System.out.println(e);

        }
    }

    String filePathForSend = "",fileName = "";

    protected class UploadData extends AsyncTask<String, String, String> {
        String fullFilePath = null, result = null;
        FilePath filePath;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected String doInBackground(String... arg0) {
            fullFilePath=arg0[0];
            // TODO Auto-generated method stub
            try {
                insertFile(fullFilePath);
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
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           // if(pDialog !=null)
             //   pDialog.dismiss();
        }
    }
    protected class getingDealStage extends AsyncTask<String, String, String> {
        String type=null,result=null;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(CRMDeal.this,"Loading Data", "Loading...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                result=getSpinnerValue();
            } catch (Exception e) {
                // TODO Auto-generated catch block
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
            JSONArray jsonArray=null;
            if(progressDialog!=null)
            {
                progressDialog.dismiss();
            }
            if(!result.isEmpty() && !(result == null)){
                try {
                    jsonArray=new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objs = null;
                        try {
                            Owner owner=new Owner();
                            objs = jsonArray.getJSONObject(i);
                            owner.setId(objs.getString("Id"));
                            owner.setName(objs.getString("DStage"));
                            stageArrayList.add(owner);
                        } catch (JSONException e) {
                            if(progressDialog!=null)
                            {
                                progressDialog.dismiss();
                            }
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setSpinner(dealStageSpinner,stageArrayList);
                if(!TDocID.equalsIgnoreCase("")){
                    isComeforEdit = true;
                    new getDealData().execute();
                }
            }
            else{
                new Custom_Toast(CRMDeal.this,"No Data Found").showCustomAlert();
            }

        }
    }
    public String getSpinnerValue(){
        String url = "http://"+server+"/And_Sync.asmx/XjsGetDealStage_CRM";
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }
    private void showDatePicker(int id,String textSet) {
        DateAndTimePicker date = new DateAndTimePicker();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        args.putInt("grace",0);
        args.putInt("key",12);
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        if(id==R.id.dealDate)
        {
            date.setCallBack(onDealdate);
            date.show(getSupportFragmentManager(), textSet+" Date");
        }
        else if(id==R.id.closeDate){
            date.setCallBack(onCloseDate);
            date.show(getSupportFragmentManager(), textSet+" Date");
        }
    }
    DatePickerDialog.OnDateSetListener onDealdate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String strDate=(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth))+"/"+(((monthOfYear+1)<10?("0"+(monthOfYear+1)):(monthOfYear+1)))+"/"+year;
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
            System.out.println(format2.format(date));
            Date filledDate = null,currenttime = null;
            try {
                filledDate = format2.parse(format2.format(date));
                currenttime=format2.parse(currentDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            System.out.println(c.getTime()); // Tue Jun 18 17:07:45 IST 2013
            c.set(Calendar.DATE, c.get(Calendar.DATE)-30);
            System.out.println("time before="+c.getTime()+" "+c.getTimeInMillis());
            long i=filledDate.getTime()-c.getTimeInMillis();
            System.out.println("30 days="+(30*24*3600 * 1000) );
            System.out.println("curent="+currenttime.getTime());
            System.out.println("before="+c.getTimeInMillis());
            System.out.println("i="+i);

            editTextdealDate.setText(format2.format(date));
            // toDateEditL.setText(format2.format(date));
        }
    };
    DatePickerDialog.OnDateSetListener onCloseDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            String strDate=(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth))+"/"+(((monthOfYear+1)<10?("0"+(monthOfYear+1)):(monthOfYear+1)))+"/"+year;
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
            System.out.println(format2.format(date));
            Date filledDate = null,currenttime = null;
            try {
                filledDate = format2.parse(format2.format(date));
                currenttime=format2.parse(currentDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            System.out.println(c.getTime()); // Tue Jun 18 17:07:45 IST 2013
            c.set(Calendar.DATE, c.get(Calendar.DATE)-30);
            System.out.println("time before="+c.getTime()+" "+c.getTimeInMillis());
            long i=filledDate.getTime()-c.getTimeInMillis();
            System.out.println("30 days="+(30 * 24  *3600 * 1000) );
            System.out.println("curent="+currenttime.getTime());
            System.out.println("before="+c.getTimeInMillis());
            System.out.println("i="+i);

            editTextcloseDate.setText(format2.format(date));
            // toDateEditL.setText(format2.format(date));
        }
    };
    public boolean setSpinner(Spinner spinner, ArrayList<Owner> arrayList){
        CustomAdapterCRMStreamInfo adapter = new CustomAdapterCRMStreamInfo(CRMDeal.this,arrayList ,R.layout.spinner_adapter_view);
        spinner.setAdapter(adapter);
        return  true;
    }
    protected class getDealData extends AsyncTask<String, String, String> {
        String type = null, result = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CRMDeal.this, "Loading Data", "Loading...", true);

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
            //dfsdfsdfsfd
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (!result.isEmpty() && !(result == null)) {
                try {
                    jsonArray = new JSONArray(result);
                    String DealNm="",dealDate="",ExpClsDt="",DealStagePerc="",Amt="",
                            monthlydeal="",TotalDealAmt="",DealNote="",commPer,comAmt;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objs = null;
                        try {
                            objs = jsonArray.getJSONObject(i);
                            DealNm=objs.getString("DealNm");
                            dealDate=objs.getString("DealDt");
                            ExpClsDt=objs.getString("ExpClsDt");
                            DealStagePerc=objs.getString("DealStagePerc");
                            Amt=objs.getString("Amt");
                            monthlydeal=objs.getString("monthlydeal");
                            TotalDealAmt=objs.getString("TotalDealAmt");
                            commissionAmount.setText(Amt);
                            DealNote=objs.getString("DealNote");
                            JSONArray milestoneArray = objs.getJSONArray("Dealmilestones");
                            for(int k=0; k< milestoneArray.length(); k++)
                            {
                                JSONObject jsonObject = milestoneArray.getJSONObject(k);
//                                if(k == 0)
//                                {
//
//                                    milstoneText.setTag(jsonObject.getInt("id"));
//                                    milstoneText.setText(jsonObject.getString("milestone"));
//                                    rateText.setText(jsonObject.getString("rate"));
//                                    qtyText.setText(jsonObject.getString("qty"));
//                                    amountText.setText(jsonObject.getString("amount"));
//
//
//
//                                }
//                                else
//                                {
                                    showMilestonesLayout(jsonObject);
//                                }

                               // showMilestonesLayout(jsonObject);
                            }

                            commisionPercentAmtText.setText(objs.getString("commamt"));
                            commisionpercentText.setText(objs.getString("commper"));

                        } catch (JSONException e) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            e.printStackTrace();
                        }
                    }
                    editmonth.setText(monthlydeal.equalsIgnoreCase("0")?"1":monthlydeal);
                    if(monthlydeal.equalsIgnoreCase("0")){
                        monthflag=false;
                        LinearLayoutMultimonthdeal.setVisibility(View.GONE);
                        revertLinearLayout.setVisibility(View.GONE);
                        LinearLayoutNormaldeal.setVisibility(View.GONE);
                    }
                    else{
                        monthflag=true;
                        LinearLayoutNormaldeal.setVisibility(View.GONE);
                        LinearLayoutMultimonthdeal.setVisibility(View.GONE);
                        revertLinearLayout.setVisibility(View.GONE);
                    }
                    editTextDealName.setText(DealNm);
                    //editTextAmount.setText(Amt);
                    editTextdealDate.setText(dealDate);
                    editTextcloseDate.setText(ExpClsDt);
                    editTextdealNote.setText(DealNote);
                    totalValueAMT.setText("months(Total Value Rs."+TotalDealAmt+")");
                    for(int i=0;i<stageArrayList.size();i++){
                        if(stageArrayList.get(i).getName().equalsIgnoreCase(DealStagePerc))
                        {
                            dealStageSpinner.setSelection(i);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                new Custom_Toast(CRMDeal.this, "No Data Found").showCustomAlert();
            }

            isComeforEdit = false;

        }
    }
    public String getPersonInfo() {
        String url = "http://"+server+"/And_Sync.asmx/XjsGetTaskDealByid_CRM?TaskDealId='"+TDocID+"'";;
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }



    public void showMilestonesLayout(JSONObject jsonObject)
    {
        View child = getLayoutInflater().inflate(R.layout.milestone_layout, null);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,10,0,0);
        child.setLayoutParams(lp);
        milesstoneDataLayout.addView(child);
        Button img = (Button)child.findViewById(R.id.addNewMilestone);
        img.setOnClickListener(addMilesClick);
        i++;
        Button imj = (Button)child.findViewById(R.id.removeMilestone);

        imj.setVisibility(View.VISIBLE);

        imj.setOnClickListener(removeView);
        //Sandeep Singh
       final EditText qtyText = (EditText)child.findViewById(R.id.qtyText);
        qtyText.setGravity(Gravity.TOP| Gravity.RIGHT);
        qtyText.setKeyListener(DigitsKeyListener.getInstance(true,true));
       final EditText rateText = (EditText)child.findViewById(R.id.rateText);
        rateText.setGravity(Gravity.TOP| Gravity.RIGHT);
        rateText.setKeyListener(DigitsKeyListener.getInstance(true,true));
        final EditText milstoneText = (EditText)child.findViewById(R.id.milestonetext);
        final EditText amountText = (EditText)child.findViewById(R.id.amountText);
        amountText.setGravity(Gravity.TOP| Gravity.RIGHT);
        amountText.setKeyListener(DigitsKeyListener.getInstance(true,true));
        try {
            milstoneText.setTag(jsonObject.getInt("id"));
            qtyText.setText("" + jsonObject.getString("qty"));
            rateText.setText("" + jsonObject.getString("rate"));
            milstoneText.setText(""+jsonObject.getString("milestone"));
            amountText.setText(""+jsonObject.getString("amount"));
        }
        catch (Exception e)
        {

        }
        amountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(!isComeforEdit) {
                    //if (!commisionpercentText.getText().toString().isEmpty()) {
                    totalAmount = 0;
                    calculateCommissionAmount("");

                    //  }
                }


            }
        });


        qtyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                try {
                    if (!isComeforEdit) {
                        if (editable.toString().length() > 0) {
                            double qts = 0, rts = 0;
                            String rt = rateText.getText().toString();
                            String qt = qtyText.getText().toString();
                            if (!rt.isEmpty())
                                rts = Double.parseDouble(rt);
                            if (!qt.isEmpty())
                                qts = Double.parseDouble(qt);
                            double tot = rts * qts;
                            amountText.setText("" + tot);
                        } else if (editable.toString().length() == 0) {
                            double tot = 0;
                            amountText.setText("" + round(new BigDecimal(tot), 2, false).toPlainString());
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        rateText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {


                    if (!isComeforEdit) {
                        if (editable.toString().length() > 0) {
                            double qts = 0, rts = 0;
                            String rt = rateText.getText().toString();
                            String qt = qtyText.getText().toString();

                            if (!rt.isEmpty())
                                rts = Double.parseDouble(rt);
                            if (!qt.isEmpty())
                                qts = Double.parseDouble(qt);
                            double tot = rts * qts;
                            amountText.setText("" + tot);
                        } else if (editable.toString().length() == 0) {
                            double tot = 0;
                            amountText.setText("" + round(new BigDecimal(tot), 2, false).toPlainString());
                        }

                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }


    public void addMilstone()
    {


        View child = getLayoutInflater().inflate(R.layout.milestone_layout, null);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0,10,0,0);
        child.setLayoutParams(lp);
        milesstoneDataLayout.addView(child);

        Button addMils = (Button)child.findViewById(R.id.addNewMilestone) ;
        addMils.setOnClickListener(addMilesClick);
        Button imj = (Button)child.findViewById(R.id.removeMilestone);
        if(milesstoneDataLayout.getChildCount()>1)
        {
            imj.setVisibility(View.VISIBLE);
            imj.setOnClickListener(removeView);
        }
        final EditText qtyText = (EditText)child.findViewById(R.id.qtyText);
        final EditText rateText = (EditText)child.findViewById(R.id.rateText);
        final EditText  milstoneText = (EditText)child.findViewById(R.id.milestonetext);
        milstoneText.setTag("");
        final  EditText amountText = (EditText)child.findViewById(R.id.amountText);
        qtyText.setGravity(Gravity.TOP| Gravity.RIGHT);
        qtyText.setKeyListener(DigitsKeyListener.getInstance(true,true));
        rateText.setGravity(Gravity.TOP| Gravity.RIGHT);
        rateText.setKeyListener(DigitsKeyListener.getInstance(true,true));
        amountText.setGravity(Gravity.TOP| Gravity.RIGHT);
        amountText.setKeyListener(DigitsKeyListener.getInstance(true,true));


        amountText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
               try {
                   if (!isComeforEdit) {
                       //if (!commisionpercentText.getText().toString().isEmpty()) {
                       totalAmount = 0;
                       calculateCommissionAmount("");

                       //  }
                   }

               }
               catch (Exception e)
               {
                   e.printStackTrace();
               }
            }
        });

        qtyText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (!isComeforEdit) {
                        if (editable.toString().length() > 0) {
                            double rts = 0, qts = 0;
                            String rt = rateText.getText().toString();
                            String qt = editable.toString();
                            if (!rt.isEmpty())
                                rts = Double.parseDouble(rt);
                            if (!qt.isEmpty())
                                qts = Double.parseDouble(qt);
                            double tot = rts * qts;
                            amountText.setText("" + round(new BigDecimal(tot), 2, false).toPlainString());
                            calculateCommissionAmount("");
                        } else if (editable.toString().length() == 0) {
                            double tot = 0;
                            amountText.setText("" + round(new BigDecimal(tot), 2, false).toPlainString());
                        }
                    }
                } catch (Exception e) {

                }

            }
        });

        rateText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (!isComeforEdit) {
                        if (editable.toString().length() > 0) {
                            double rts = 0, qts = 0;
                            String rt = editable.toString();
                            String qt = qtyText.getText().toString();
                            if (!rt.isEmpty())
                                rts = Double.parseDouble(rt);
                            if (!qt.isEmpty())
                                qts = Double.parseDouble(qt);
                            double tot = rts * qts;
                            amountText.setText("" + round(new BigDecimal(tot), 2, false).toPlainString());
                            calculateCommissionAmount("");
                        } else if (editable.toString().length() == 0) {
                            double tot = 0;
                            amountText.setText("" + round(new BigDecimal(tot), 2, false).toPlainString());
                        }
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }


    ImageView prevImageView;
    View.OnClickListener addMilesClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            addMilstone();


        }
    };


    View.OnClickListener removeView = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LinearLayout linearLayout=(LinearLayout)view.getParent().getParent();
            milesstoneDataLayout.removeView(linearLayout);
            i--;
            calculateCommissionAmount("");
        }
    };

    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
