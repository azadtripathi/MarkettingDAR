package com.dm.crmdm_app;

import android.*;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.controller.AppDataController;
import com.dm.controller.CityController;
import com.dm.controller.CountryController;
import com.dm.controller.ItemController;
import com.dm.controller.ProductGroupController;
import com.dm.controller.StateController;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomAdapterCRMStreamInfo;
import com.dm.library.Custom_Toast;
import com.dm.library.DbCon;
import com.dm.library.IntentSend;
import com.dm.library.Validation;
import com.dm.model.AddContactModel;
import com.dm.model.AppData;
import com.dm.model.Owner;
import com.dm.parser.JSONParser;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;


public class AddContact extends Activity {
    StringBuilder DynamicFieldskey, DynamicFieldsValue, DynamicFieldsWeb;
    StringBuilder DynamicFieldsName, DynamicFieldsPhoneNo, DynamicFieldsEmail, DynamicFieldJobTitle, DynamicURLID, DynamicPhoneID;
    JSONObject sendDataonserverJsonObect;
    JSONArray sendDataonserverJsonArray;
    String ContactID = "";
    String SMID;
    SharedPreferences preferences2, preferencesAddContact;
    SimpleDateFormat df;
    String[] contactPerson;
    String[] urlPerson;
    String[] DynamicControl;
    Calendar mcurrentDate;
    ProgressDialog progressDialog;
    String server;
    static boolean flagForCompanyDetails = true;
    ArrayList<AppData> appDataArray;
    ArrayList<LinearLayout> LinearLayoutArrayListForURL = new ArrayList<>();
    ArrayList<LinearLayout> LinearLayoutArrayListForPersonInfo = new ArrayList<>();
    ArrayList<LinearLayout> LinearLayoutArrayListForDynamicControllerView = new ArrayList<>();
    Map dynamicControlData = new HashMap();
    String latitude = "", longitude = "";
    String latlngtime;
    boolean fromCheckbox = false;
    ArrayList<Owner> contaryArray = new ArrayList<>();
    ArrayList<Owner> stateArray = new ArrayList<>();
    ArrayList<Owner> jobTitleArray = new ArrayList<>();
    ArrayList<Owner> companyCityArray = new ArrayList<>();
    ArrayList<Owner> cityArray = new ArrayList<>();
    ArrayList<Owner> itemListArray = new ArrayList<>();
    ArrayList<Owner> productGroupArray = new ArrayList<>();
    ArrayList<String> spinnerArrayListStatus = new ArrayList<>();
    ArrayList<String> spinnerArrayListTag = new ArrayList<>();
    ArrayList<String> spinnerArrayListLeadSource = new ArrayList<>();
    ArrayList<String> spinnerArrayListOwner = new ArrayList<>();
    ArrayList<String> spinnerArrayListStatusID = new ArrayList<>();
    ArrayList<String> spinnerArrayListTagID = new ArrayList<>();
    ArrayList<String> spinnerArrayListLeadSourceID = new ArrayList<>();
    ArrayList<String> spinnerArrayListOwnerID = new ArrayList<>();
    ArrayList<AddContactModel> allDataArray = new ArrayList<>();
    ConnectionDetector connectionDetector;
    Custom_Toast custom_toast;
    String companyID = "0";
    String LeadOrTaskTag = "L";
    ArrayList<AddContactModel> dynamicArrayList = new ArrayList<>();
    TextView textAddCompayDetails;
    ImageView imgAddMorePersonInfo, imgAddMoreURL, capturedImage;
    LinearLayout linearLayoutAddCompanyDetails, linearLayoutdynamicAllBelowfields, linearlayoutdynamicfieldsURL, linearLayoutdynamicfieldForPersonInfo;
    ImageView save, cancel, delete, findButton, takePicture;
    EditText editTextCompany, editTextMainEmail, editTextPersonName, editTextAddCompanyDetailsDiscription, editTextAddCompanyDetailsZip, editTextAddCompanyDetailsAddress, editTextFirstName, editTextLastName, editTextJobTittle,
            editTextMainMobNo, editTextMainUrl, editTextZip, editTextAddress, editTextBackground;
    Spinner spinnerCity, spinnerCompanyCity, spinnerState, spinnerAddCompanyDetailsState, spinnerAddCompanyDetailsCountry, spinnerCountry,
            spinnerStatue, spinnerTag, spinnerLeadSource, spinnerOwner, spinnerProduct, spinnerProductGroup, spinnerIndustry, spinnerJobTitle;
    private String userChoosenTask = "";
    CheckBox checkboxActive, checkboxSameAsCompnay;
    Validation validation;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    String selectedImageBase64ImgString = "N/A";
    Intent data;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    boolean forCompanySpinner = false, forCompanyStateSpinner = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector = new ConnectionDetector(this);
        validation = new Validation(AddContact.this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        if (connectionDetector.isConnectingToInternet()) {
            preferences2 = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            preferencesAddContact = this.getSharedPreferences("CrmModule", Context.MODE_PRIVATE);
            SMID = preferences2.getString("CONPERID_SESSION", null);
            sendDataonserverJsonArray = new JSONArray();
            sendDataonserverJsonObect = new JSONObject();
            mcurrentDate = Calendar.getInstance();
            allDataArray.clear();
            spinnerArrayListStatus.clear();
            spinnerArrayListTag.clear();
            spinnerArrayListLeadSource.clear();
            spinnerArrayListOwner.clear();
            spinnerArrayListStatusID.clear();
            spinnerArrayListTagID.clear();
            spinnerArrayListLeadSourceID.clear();
            spinnerArrayListOwnerID.clear();

            setContentView(R.layout.activity_add_contact);


            ImageView iv = (ImageView) findViewById(R.id.image);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    (new IntentSend(getApplicationContext(), DashBoradActivity.class)).toSendAcivity();
                    finish();
                }
            });
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
            TextView tv = (TextView) findViewById(R.id.text);
            tv.setText("Add Contact/Lead");
            setTitle("Add Contact/Lead");
            setContentView(R.layout.activity_new_contact);
            gettingValuesFromXml();
            AppData appData;
            AppDataController appDataController1 = new AppDataController(AddContact.this);
            appDataController1.open();
            appDataArray = appDataController1.getAppTypeFromDb();
            appDataController1.close();
            appData = appDataArray.get(0);
            server = appData.getCompanyUrl();
            Intent ds = getIntent();
            if (getIntent().getExtras() != null) {
                save.setImageResource(R.drawable.update);
                ContactID = ds.getStringExtra("ContactId");
                progressDialog = ProgressDialog.show(AddContact.this, "Loading Data", "Loading...", true);
                new getAllContacyData().execute();

            } else {
                progressDialog = ProgressDialog.show(AddContact.this, "Loading Data", "Loading...", true);
                new statusTagLeadSourceOwnerData().execute("Status");
            }
            spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //spinnerState.setSelection(spinnerAddCompanyDetailsState.getSelectedItemPosition());
                    forCompanySpinner = false;

                    new getCityStateWise().execute(stateArray.get(position).getId());


                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


//            spinnerProductGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    String productId = productGroupArray.get(i).getId();
//                    new ItemAsyncTask().execute(productId);
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });
//            spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                @Override
//                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                    selectedComContryId = contaryArray.get(i).getId();
//                    forCompanyStateSpinner = false;
//                    new countryName().execute("state");
//                }
//
//                @Override
//                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                }
//            });

            spinnerAddCompanyDetailsState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //spinnerState.setSelection(spinnerAddCompanyDetailsState.getSelectedItemPosition());
                    forCompanySpinner = true;
                    new getCityStateWise().execute(stateArray.get(spinnerAddCompanyDetailsState.getSelectedItemPosition()).getId());
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            takePicture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    cameraIntent();

                }
            });
            editTextFirstName.addTextChangedListener(new TextWatcher() {
                @Override
                public void afterTextChanged(Editable s) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    editTextPersonName.setText(s);
                    //editTextMainEmail.setText(s+" "+ editTextFirstName.getText().toString());
                }
            });

            imgAddMorePersonInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    addDynamicPersonInfo(AddContact.this, "personInfo", false, null);
                }
            });
            imgAddMoreURL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    addEditText(AddContact.this, "URL", false, null);
                }
            });
            spinnerAddCompanyDetailsCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedComContryId = contaryArray.get(i).getId();
                    forCompanyStateSpinner = true;
                    new countryName().execute("state");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            textAddCompayDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flagForCompanyDetails) {
                        flagForCompanyDetails = false;
                        textAddCompayDetails.setTextColor(Color.parseColor("#FF4081"));
                        linearLayoutAddCompanyDetails.setVisibility(View.VISIBLE);
                        //setStateContarySpinner(spinnerAddCompanyDetailsCountry,contaryArray);
                    } else {
                        flagForCompanyDetails = true;
                        textAddCompayDetails.setTextColor(Color.parseColor("#3F51B5"));
                        linearLayoutAddCompanyDetails.setVisibility(View.GONE);
                        //spinnerAddCompanyDetailsCountry.setAdapter(null);
                    }
                }
            });
//            textAddCompayDetails.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v)
//                {
//                    if(flagForCompanyDetails)
//                    {
//                        flagForCompanyDetails=false;
//                        textAddCompayDetails.setTextColor(Color.parseColor("#FF4081"));
//                        linearLayoutAddCompanyDetails.setVisibility(View.VISIBLE);
//
//                        spinnerCompanyCity.setSelection(0);
//                        spinnerAddCompanyDetailsState.setSelection(0);
//                        editTextAddCompanyDetailsZip.setText("");
//
////                        setStateContarySpinner(spinnerAddCompanyDetailsCountry,contaryArray);
//                    }
//                    else
//                        {
//                        flagForCompanyDetails=true;
//                        textAddCompayDetails.setTextColor(Color.parseColor("#3F51B5"));
//                        linearLayoutAddCompanyDetails.setVisibility(View.GONE);
//                            spinnerCompanyCity.setSelection(0);
//                            spinnerAddCompanyDetailsState.setSelection(0);
//                            editTextAddCompanyDetailsZip.setText("");
//
//                        //spinnerAddCompanyDetailsCountry.setAdapter(null);
//                    }
//                }
//            });


            checkboxSameAsCompnay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                                 @Override
                                                                 public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                                                                     if (isChecked) {
                                                                         fromCheckbox = true;


                                                                         //  setStateContarySpinner(spinnerCity,cityArray);
                                                                         editTextAddress.setText(editTextAddCompanyDetailsAddress.getText().toString());
                                                                         editTextZip.setText(editTextAddCompanyDetailsZip.getText().toString());
                                                                         // spinnerCountry.setSelection(spinnerAddCompanyDetailsCountry.getSelectedItemPosition());
                                                                         spinnerState.setSelection(spinnerAddCompanyDetailsState.getSelectedItemPosition());
                                                                         cityArray = companyCityArray;
                                                                         spinnerCity.setSelection(spinnerCompanyCity.getSelectedItemPosition());
                                                                     } else {
                                                                         fromCheckbox = false;
                                                                         editTextAddress.setText("");
                                                                         //   cityArray=companyCityArray;
                                                                         // setStateContarySpinner(spinnerCity,cityArray);
//                                                                         spinnerCity.setAdapter(null);
                                                                         editTextZip.setText("");
//                                                                        // spinnerCountry.setSelection(0);
                                                                         spinnerState.setSelection(0);
                                                                     }

                                                                 }
                                                             }
            );

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // saveData();
                    save.setEnabled(false);
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            save.setEnabled(true);
                        }
                    }, 2000);
                    progressDialog = ProgressDialog.show(AddContact.this, "Validating Data", "Please Wait...", true);
                    progressDialog.setCancelable(true);
                    //String state=stateArray.get(spinnerState.getSelectedItemPosition()).getName().toString();
                    Validation validation = new Validation(AddContact.this);
                    if (editTextFirstName.getText().toString().isEmpty()) {
                        dismissDiloge();
                        new Custom_Toast(AddContact.this, "Please Enter Name", Toast.LENGTH_SHORT).showCustomAlert();
                        ///editTextFirstName.setError("First Name Required");
                    } else if (editTextCompany.getText().toString().isEmpty()) {
                        dismissDiloge();
                        new Custom_Toast(AddContact.this, "Please Enter Company Name", Toast.LENGTH_SHORT).showCustomAlert();
                        //editTextCompany.setError("Company Required");
                    } else if (stateArray.size() > 0 && contaryArray.get(spinnerCountry.getSelectedItemPosition()).getName().equalsIgnoreCase("Select")) {
                        dismissDiloge();
                        new Custom_Toast(AddContact.this, "Please Select Country", Toast.LENGTH_SHORT).showCustomAlert();
                    } else if (stateArray.size() > 0 && stateArray.get(spinnerState.getSelectedItemPosition()).getName().toString().equalsIgnoreCase("Select")) {
                        dismissDiloge();
                        new Custom_Toast(AddContact.this, "Please Select State", Toast.LENGTH_SHORT).showCustomAlert();
                    } else if (cityArray.size() > 0 && cityArray.get(spinnerCity.getSelectedItemPosition()).getName().equalsIgnoreCase("Select")) {
                        dismissDiloge();
                        new Custom_Toast(AddContact.this, "Please Select City", Toast.LENGTH_SHORT).showCustomAlert();
                    } else if (editTextMainMobNo.getText().toString().isEmpty()) {
                        dismissDiloge();
                        new Custom_Toast(AddContact.this, "Please Enter Phone", Toast.LENGTH_SHORT).showCustomAlert();
                    } else {
                        saveData();
                    }
                    //saveData();
                    /*else if(validation.vAlNUmericDynamicFileds(editTextAddCompanyDetailsState.getText().toString()).equalsIgnoreCase("0")){}
                    else if(!validation.validateName(editTextCompany.getText().toString())){}*/
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearFields();
                }
            });
            findButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AddContact.this, FindAddContact.class);
                    startActivity(intent);
                    finish();
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (!ContactID.equalsIgnoreCase("")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddContact.this);
                        builder.setMessage("Do you want to Delete the record ?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int aid) {
                                        dialog.cancel();
                                        new deleteData().execute();

                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alert = builder.create();
                        alert.show();
                    } else {
                        Toast.makeText(AddContact.this, "No Contact Found", Toast.LENGTH_SHORT).show();
                    }


                }
            });
        } else {
            new Custom_Toast(getApplicationContext(), "No Internet Connection! Try Again.").showCustomAlert();
        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
//				filePath=fileUri.getPath();
            } else if (requestCode == REQUEST_CAMERA) {
                onCaptureImageResult(data);
                // filePath = fileUri1.getPath();
            } else if (requestCode == 7275) {
                if (resultCode == Activity.RESULT_OK) {
                    String result = data.getStringExtra("result");
                    new saveTaskDataOnServer().execute();
                }
                if (resultCode == Activity.RESULT_CANCELED) {
                    new saveTaskDataOnServer().execute();
                }
            }
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        this.data = data;
        if (data != null) {
            int maxHeight = 500;
            int maxWidth = 500;
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                float scale = Math.min(((float) maxHeight / selectedImage.getWidth()), ((float) maxWidth / selectedImage.getHeight()));
                Matrix matrix = new Matrix();
                matrix.postScale(scale, scale);
                selectedImage = Bitmap.createBitmap(selectedImage, 0, 0, selectedImage.getWidth(), selectedImage.getHeight(), matrix, true);

                int bitmapByteCount = BitmapCompat.getAllocationByteCount(selectedImage);
                if (bitmapByteCount <= 1048576) {
                    //if(bitmapByteCount <=  21233664){
//			filePath1=data.getData().toString();
//			System.out.println(filePath1);
                    capturedImage.setImageBitmap(selectedImage);

                    selectedImageBase64ImgString = encodeImage(selectedImage);
                    //return false;
                } else {
                    new Custom_Toast(getApplicationContext(), "Please upload image of upto 1mb size only").showCustomAlert();
                }
//				image_view.setImageBitmap(selectedImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }


    }

    private void onCaptureImageResult(Intent data) {


        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
        int maxHeight = 500;
        int maxWidth = 500;
        float scale = Math.min(((float) maxHeight / selectedImage.getWidth()), ((float) maxWidth / selectedImage.getHeight()));
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        selectedImage = Bitmap.createBitmap(selectedImage, 0, 0, selectedImage.getWidth(), selectedImage.getHeight(), matrix, true);

        int bitmapByteCount = BitmapCompat.getAllocationByteCount(selectedImage);
        capturedImage.setImageBitmap(selectedImage);

        selectedImageBase64ImgString = encodeImage(selectedImage);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        selectedImageBase64ImgString = Base64.encodeToString(b, Base64.DEFAULT);
        /*int maxHeight = 500;
        int maxWidth = 500;
        float scale = Math.min(((float)maxHeight / thumbnail.getWidth()), ((float)maxWidth / thumbnail.getHeight()));
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);

        int bitmapByteCount= BitmapCompat.getAllocationByteCount(thumbnail);
        if(bitmapByteCount <=  1048576){
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
        filePath1= destination.getPath().toString();
        System.out.println(filePath1);
        capturedImage.setImageBitmap(thumbnail);
        selectedImageBase64ImgString =  encodeImage(thumbnail);*/
        //filePath = fileUri.getPath();
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(AddContact.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.shouldShowRequestPermissionRationale(AddContact.this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {

                } else {

                    ActivityCompat.requestPermissions(AddContact.this,
                            new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                            1);
                }
            } else {

                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_FILE);

            }
        } else {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_FILE);

        }
    }

    String Contact_id = "";

    public class saveTaskDataOnServer extends AsyncTask<String, Void, String> {
        String server_response;
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(AddContact.this, "Saving Data", "Please Wait...", true);
            progressDialog.setCancelable(false);
            params.add(new BasicNameValuePair("TDocId", ""));
            params.add(new BasicNameValuePair("smid", SMID));
            params.add(new BasicNameValuePair("task", editTextFirstName.getText().toString()));
            params.add(new BasicNameValuePair("contactId", Contact_id));
            Date d = new Date();
            CharSequence Asgndate = DateFormat.format("dd/MMM/yyyy", d.getTime());

            params.add(new BasicNameValuePair("Asgndate", Asgndate.toString()));
            params.add(new BasicNameValuePair("AsgnTo", SMID));
            params.add(new BasicNameValuePair("Ref_DocId", ""));
            params.add(new BasicNameValuePair("Status", spinnerArrayListStatusID.get(spinnerStatue.getSelectedItemPosition())));
            Calendar c = Calendar.getInstance();
            int Hr24 = c.get(Calendar.HOUR_OF_DAY);
            int Min = c.get(Calendar.MINUTE);
            params.add(new BasicNameValuePair("time", String.valueOf(Hr24) + ":" + String.valueOf(Min)));
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
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // if(progressDialog.isShowing()){progressDialog.dismiss();}
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            Log.e("Response", "" + server_response);
            if (server_response != null) {
                if (!server_response.isEmpty()) {
                    server_response = server_response.replaceAll("\"", "");
                    if (server_response.equalsIgnoreCase("Y")) {
                        new Custom_Toast(AddContact.this, "Save successfully").showCustomAlert();
                        finish();
                        Intent intent = new Intent(AddContact.this, AddContact.class);
                        startActivity(intent);
                    } else {
                        new Custom_Toast(AddContact.this, "Try Again!" + server_response).showCustomAlert();
                    }
                }
            }
        }
    }

    public class saveDataOnServer extends AsyncTask<String, Void, String> {
        String server_response;
        List<NameValuePair> params = new ArrayList<NameValuePair>();

        protected void onPreExecute() {
            params.clear();
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            // TODO Auto-generated method stub
            super.onPreExecute();
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
            if (!ContactID.equalsIgnoreCase("")) {
                params.add(new BasicNameValuePair("contactid", ContactID));
                params.add(new BasicNameValuePair("phoneid", DynamicPhoneID.substring(0, DynamicPhoneID.length() - 1).toString()));
                params.add(new BasicNameValuePair("urlid", DynamicURLID.substring(0, DynamicURLID.length() - 1).toString()));

            } else {
                params.add(new BasicNameValuePair("Flag", LeadOrTaskTag));
                params.add(new BasicNameValuePair("CId", companyID));

            }
            params.add(new BasicNameValuePair("Cname", editTextCompany.getText().toString()));
            params.add(new BasicNameValuePair("Cdesc", editTextAddCompanyDetailsDiscription.getText().toString()));
            //params.add(new BasicNameValuePair("Cphone",editTextCompanyPhone.getText().toString()));
            params.add(new BasicNameValuePair("Cphone", ""));
            params.add(new BasicNameValuePair("Cadd", editTextAddCompanyDetailsAddress.getText().toString()));
            if (companyCityArray.size() > 0) {
                params.add(new BasicNameValuePair("Ccity", companyCityArray.get(spinnerCompanyCity.getSelectedItemPosition()).getId()));
                SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("cCityId", Integer.parseInt(companyCityArray.get(spinnerCompanyCity.getSelectedItemPosition()).getId()));
                editor.apply();
                editor.commit();
            } else {
                params.add(new BasicNameValuePair("Ccity", "0"));
                SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("cCityId", 0);
                editor.apply();
                editor.commit();
            }


            if (spinnerAddCompanyDetailsState.getSelectedItemPosition() >= 0) {
                params.add(new BasicNameValuePair("CstateId", stateArray.get(spinnerAddCompanyDetailsState.getSelectedItemPosition()).getId()));
                if (stateArray.get(spinnerAddCompanyDetailsState.getSelectedItemPosition()).getId().equalsIgnoreCase("0")) {
                    params.add(new BasicNameValuePair("Cstate", ""));
                } else {
                    params.add(new BasicNameValuePair("Cstate", stateArray.get(spinnerAddCompanyDetailsState.getSelectedItemPosition()).getName()));
                }
                SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("cStateId", Integer.parseInt(stateArray.get(spinnerAddCompanyDetailsState.getSelectedItemPosition()).getId()));
                editor.apply();
                editor.commit();
            } else {

                params.add(new BasicNameValuePair("Cstate", ""));
                params.add(new BasicNameValuePair("CstateId", "0"));
                SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("cStateId", 0);
                editor.apply();
                editor.commit();

            }
            if (spinnerAddCompanyDetailsCountry.getSelectedItemPosition() >= 0) {
                params.add(new BasicNameValuePair("Ccountry", contaryArray.get(spinnerAddCompanyDetailsCountry.getSelectedItemPosition()).getId()));

            } else {
                params.add(new BasicNameValuePair("Ccountry", "0"));
            }

//            if (spinnerProductGroup.getSelectedItemPosition() >= 0) {
//                params.add(new BasicNameValuePair("Productgroupid", productGroupArray.get(spinnerProductGroup.getSelectedItemPosition()).getId()));
//
//            } else {
//                params.add(new BasicNameValuePair("Productgroupid", "0"));
//            }

            if (spinnerProduct.getSelectedItemPosition() >= 0) {
                params.add(new BasicNameValuePair("productid", itemListArray.get(spinnerProduct.getSelectedItemPosition()).getId()));

            } else {
                params.add(new BasicNameValuePair("productid", "0"));
            }


            params.add(new BasicNameValuePair("CZip", String.valueOf(validation.vNum(editTextAddCompanyDetailsZip.getText()))));
            //  params.add(new BasicNameValuePair("CZip",editTextAddCompanyDetailsZip.getText().toString()));
            try {

            } catch (IndexOutOfBoundsException e) {

            }


            params.add(new BasicNameValuePair("Fname", editTextFirstName.getText().toString()));
            params.add(new BasicNameValuePair("Lname", ""));
            //params.add(new BasicNameValuePair("Lname",editTextLastName.getText().toString()));
            //params.add(new BasicNameValuePair("JobTitle",editTextJobTittle.getText().toString()));
            params.add(new BasicNameValuePair("JobTitle", ""));

            if (spinnerCity.getSelectedItemPosition() >= 0) {
                params.add(new BasicNameValuePair("City", cityArray.get(spinnerCity.getSelectedItemPosition()).getId()));
                SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("cityId", Integer.parseInt(cityArray.get(spinnerCity.getSelectedItemPosition()).getId()));
                editor.apply();
                editor.commit();
            } else {
                params.add(new BasicNameValuePair("City", "0"));
                SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt("cityId", 0);
                editor.apply();
                editor.commit();
            }

            params.add(new BasicNameValuePair("stateid", stateArray.get(spinnerState.getSelectedItemPosition()).getId()));
            SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putInt("stateId", Integer.parseInt(stateArray.get(spinnerState.getSelectedItemPosition()).getId()));
            editor.apply();
            editor.commit();
            if (stateArray.get(spinnerState.getSelectedItemPosition()).getId().equalsIgnoreCase("0")) {
                params.add(new BasicNameValuePair("State", ""));
            } else {
                params.add(new BasicNameValuePair("State", stateArray.get(spinnerState.getSelectedItemPosition()).getName()));
            }
            params.add(new BasicNameValuePair("Country", contaryArray.get(spinnerCountry.getSelectedItemPosition()).getId()));
            params.add(new BasicNameValuePair("zip", String.valueOf(validation.vNum(editTextZip.getText()))));
            //    params.add(new BasicNameValuePair("zip",editTextZip.getText().toString()));
            params.add(new BasicNameValuePair("Add", editTextAddress.getText().toString()));
            params.add(new BasicNameValuePair("Statusid", spinnerArrayListStatusID.get(spinnerStatue.getSelectedItemPosition())));
            params.add(new BasicNameValuePair("Tagid", spinnerArrayListTagID.get(spinnerTag.getSelectedItemPosition())));
            params.add(new BasicNameValuePair("Leadid", spinnerArrayListLeadSourceID.get(spinnerLeadSource.getSelectedItemPosition())));
            params.add(new BasicNameValuePair("Owner", spinnerArrayListOwnerID.get(spinnerOwner.getSelectedItemPosition())));
            params.add(new BasicNameValuePair("Active", String.valueOf(checkboxActive.isChecked() ? "Y" : "N")));

            params.add(new BasicNameValuePair("Background", editTextBackground.getText().toString()));
            params.add(new BasicNameValuePair("Manager", spinnerArrayListOwnerID.get(spinnerOwner.getSelectedItemPosition())));
            params.add(new BasicNameValuePair("smid", SMID));
            if (DynamicFieldskey.length() > 0) {
                params.add(new BasicNameValuePair("DynamicControls", DynamicFieldskey.substring(0, DynamicFieldskey.length() - 1).toString()));
            } else {
                params.add(new BasicNameValuePair("DynamicControls", ""));
            }
            if (DynamicFieldsValue.length() > 0) {
                params.add(new BasicNameValuePair("DynamicControlsValue", DynamicFieldsValue.substring(0, DynamicFieldsValue.length() - 1).toString()));
            } else {
                params.add(new BasicNameValuePair("DynamicControlsValue", ""));

            }
            params.add(new BasicNameValuePair("phnval", DynamicFieldsPhoneNo.substring(0, DynamicFieldsPhoneNo.length() - 1).toString()));
            params.add(new BasicNameValuePair("phnddlval", DynamicFieldJobTitle.substring(0, DynamicFieldJobTitle.length() - 1).toString()));
            params.add(new BasicNameValuePair("phncontName", DynamicFieldsName.substring(0, DynamicFieldsName.length() - 1).toString()));
            params.add(new BasicNameValuePair("emailval", DynamicFieldsEmail.substring(0, DynamicFieldsEmail.length() - 1).toString()));
            params.add(new BasicNameValuePair("urlval", DynamicFieldsWeb.substring(0, DynamicFieldsWeb.length() - 1).toString()));
            params.add(new BasicNameValuePair("longitude", longitude));
            params.add(new BasicNameValuePair("latitude", latitude));
            params.add(new BasicNameValuePair("lat_long_dt", String.valueOf(System.currentTimeMillis())));
            params.add(new BasicNameValuePair("image1", selectedImageBase64ImgString));
        }

        protected String doInBackground(String... arg0) {
            URL url = null;
            try {
                if (ContactID.equalsIgnoreCase("")) {
                    url = new URL("http://" + server + "/And_Sync.asmx/XjsSaveContacts_CRM");
                } else {
                    url = new URL("http://" + server + "/And_Sync.asmx/XjsUpdateContact_CRM");
                }
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
                    if (ContactID.equalsIgnoreCase("")) {
                        if (server_response.equalsIgnoreCase("N")) {
                            new Custom_Toast(AddContact.this, "Try Again !" + server_response).showCustomAlert();

                        } else if (server_response.equalsIgnoreCase("Record Already Exist")) {
                            new Custom_Toast(AddContact.this, "Try Again !" + server_response).showCustomAlert();
                        } else {
                            Contact_id = server_response;
                            Intent intent = new Intent(AddContact.this, CrmTask.class);
                            intent.putExtra("FromWhere", "addContact");
                            intent.putExtra("Contact_id", server_response);
                            startActivityForResult(intent, 7275);


                        }
                    } else {
                        if (server_response.equalsIgnoreCase("Updated Successfully")) {
                            new Custom_Toast(AddContact.this, server_response).showCustomAlert();
                            finish();
                            Intent intent = new Intent(AddContact.this, AddContact.class);
                            startActivity(intent);
                        } else {
                            new Custom_Toast(AddContact.this, "Try Again !" + server_response).showCustomAlert();
                        }
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

    protected class deleteData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //         progressDialog= ProgressDialog.show(AddContactModel.this,"Loading Data", "Loading...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            String response1 = null;
            // TODO Auto-generated method stub
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/XjDeleteContact_CRM");
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("contactid", ContactID));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response1 = httpclient.execute(httppost, responseHandler);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return response1;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            String server_response = result;
            if (server_response != null) {
                if (!server_response.isEmpty()) {
                    server_response = server_response.replaceAll("\"", "");
                    if (server_response.equalsIgnoreCase("Deleted Successfully")) {
                        new Custom_Toast(AddContact.this, server_response).showCustomAlert();
                        finish();
                        Intent intent = new Intent(AddContact.this, AddContact.class);
                        startActivity(intent);
                    } else {
                        new Custom_Toast(AddContact.this, "Try Again !" + server_response).showCustomAlert();
                    }
                }
            } else {
                new Custom_Toast(AddContact.this, "Try Again !" + server_response).showCustomAlert();
            }

        }
    }

    protected class countryName extends AsyncTask<String, String, String> {
        String type = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if (type != null && !type.equalsIgnoreCase("country"))
                progressDialog = ProgressDialog.show(AddContact.this, "Loading Data", "Loading...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            String response = null;
            type = arg0[0];
            // TODO Auto-generated method stub
            try {
                response = getStateAndContary(type);
               /* boolean flag=false;
                if(preferencesAddContact.getString(type,"").length()<1){
                    flag=true;
                    response =getStateAndContary(type);
                }
                else{
                    flag=false;
                    response=preferencesAddContact.getString(type,"");
                }
                if(flag){
                    SharedPreferences.Editor editor = preferencesAddContact.edit();
                    editor.putString(type,response);
                    editor.commit();
                }*/

            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            JSONArray jsonArray = null;
            if (!result.isEmpty() && !(result == null)) {
                if (type.equalsIgnoreCase("contary")) {
                    try {
                        contaryArray.clear();
                        jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                objs = jsonArray.getJSONObject(i);
                                Owner owner = new Owner();
                                objs = jsonArray.getJSONObject(i);
                                owner.setId(objs.getString("Cid"));
                                owner.setName(objs.getString("NM"));
                                contaryArray.add(owner);
                            } catch (JSONException e) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                    setStateContarySpinner(spinnerCountry, contaryArray);
                    setStateContarySpinner(spinnerAddCompanyDetailsCountry, contaryArray);

                } else {
                    try {
                        if (forCompanyStateSpinner) {
                            stateArray.clear();
                            jsonArray = new JSONArray(result);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objs = null;
                                try {
                                    Owner owner = new Owner();
                                    objs = jsonArray.getJSONObject(i);
                                    owner.setId(objs.getString("id"));
                                    owner.setName(objs.getString("nm"));
                                    stateArray.add(owner);
                                } catch (JSONException e) {
                                    if (progressDialog != null) {
                                        progressDialog.dismiss();
                                    }
                                    e.printStackTrace();
                                }
                            }
                            setStateContarySpinner(spinnerAddCompanyDetailsState, stateArray);
                            if (!ContactID.equalsIgnoreCase("")) {
                                if (allDataArray != null) {
                                    for (int i = 0; i < allDataArray.size(); i++) {
                                        for (int j = 0; j < stateArray.size(); j++) {
                                            if (stateArray.get(j).getName().equalsIgnoreCase(allDataArray.get(i).getCompanyState())) {
                                                // editTextAddCompanyDetailsCity.setText(allDataArray.get(i).getCompanyCity());
                                                // editTextCity.setText(allDataArray.get(i).getCity());
                                                spinnerAddCompanyDetailsState.setSelection(j);
                                            }


                                        }
                                    }
                                }

                            } else {
                                SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
                                int cstid = pref.getInt("cStateId", 0);
                                for (int j = 0; j < stateArray.size(); j++) {
                                    if (stateArray.get(j).getId().equalsIgnoreCase(String.valueOf(cstid))) {
                                        // editTextAddCompanyDetailsCity.setText(allDataArray.get(i).getCompanyCity());
                                        // editTextCity.setText(allDataArray.get(i).getCity());
                                        spinnerAddCompanyDetailsState.setSelection(j);
                                    }


                                }

                            }
                        } else {
                            stateArray.clear();
                            jsonArray = new JSONArray(result);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject objs = null;
                                try {
                                    Owner owner = new Owner();
                                    objs = jsonArray.getJSONObject(i);
                                    owner.setId(objs.getString("id"));
                                    owner.setName(objs.getString("nm"));
                                    stateArray.add(owner);
                                } catch (JSONException e) {
                                    if (progressDialog != null) {
                                        progressDialog.dismiss();
                                    }
                                    e.printStackTrace();
                                }
                            }
                            setStateContarySpinner(spinnerState, stateArray);
                            if (!ContactID.equalsIgnoreCase("")) {
                                if (allDataArray != null) {
                                    for (int i = 0; i < allDataArray.size(); i++) {
                                        for (int j = 0; j < stateArray.size(); j++) {
                                            if (stateArray.get(j).getName().equalsIgnoreCase(allDataArray.get(i).getState())) {
                                                // editTextAddCompanyDetailsCity.setText(allDataArray.get(i).getCompanyCity());
                                                // editTextCity.setText(allDataArray.get(i).getCity());
                                                spinnerState.setSelection(j);
                                            }


                                        }
                                    }
                                }

                            } else {
                                SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
                                int stid = pref.getInt("stateId", 0);
                                for (int j = 0; j < stateArray.size(); j++) {
                                    if (stateArray.get(j).getId().equalsIgnoreCase(String.valueOf(stid))) {
                                        // editTextAddCompanyDetailsCity.setText(allDataArray.get(i).getCompanyCity());
                                        // editTextCity.setText(allDataArray.get(i).getCity());
                                        spinnerState.setSelection(j);
                                    }


                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (!isDynamicControlledCalled) {
                        if (!ContactID.equalsIgnoreCase("")) {
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    flagForCompanyDetails = false;
                                    textAddCompayDetails.setTextColor(Color.parseColor("#FF4081"));
                                    linearLayoutAddCompanyDetails.setVisibility(View.VISIBLE);
                                }
                            }, 500);
                        }
                        new DynamicController().execute();
                    }

                }

            } else {
                new Custom_Toast(AddContact.this, "No Data Found").showCustomAlert();
            }

        }
    }

    String selectedComContryId = "";

    public String getStateAndContary(String type) {
        //String url;
        String result;
        if (type.equalsIgnoreCase("contary")) {
            // url ="http://"+server+"/And_Sync.asmx/xJSGetCountry?minDate=0";
            CountryController countryController = new CountryController(this);
            countryController.open();
            result = countryController.getCountaryList();
            countryController.close();
        } else {
            // url = "http://"+server+"/And_Sync.asmx/XjsBindCRM_State?countryid="+selectedComContryId;
            StateController stateController = new StateController(this);
            stateController.open();
            result = stateController.getStateListObject(selectedComContryId);
            stateController.close();
        }

       /* JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);*/
        return result;
    }

    protected class statusTagLeadSourceOwnerData extends AsyncTask<String, String, String> {
        String type = null, result = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //     progressDialog= ProgressDialog.show(AddContactModel.this,"Loading Data", "Loading...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {

            type = arg0[0];
            // TODO Auto-generated method stub
            try {
               /* if(preferencesAddContact.getString("Status","").length()<1 || preferencesAddContact.getString("Tag","").length()<1 || preferencesAddContact.getString("LeadResource","").length()<1 || preferencesAddContact.getString("Owner","").length()<1){
                    result=getstatusTagLeadSourceOwnerData(type);
                }
                else{
                    result=preferencesAddContact.getString(type,"");
                }*/
                boolean flag = false;
                if (preferencesAddContact.getString(type, "").length() < 1) {
                    flag = true;
                    result = getstatusTagLeadSourceOwnerData(type);
                } else {
                    flag = false;
                    result = preferencesAddContact.getString(type, "");
                }
                if (flag) {
                    SharedPreferences.Editor editor = preferencesAddContact.edit();
                    editor.putString(type, result);
                   /* if(type.equalsIgnoreCase("Status")){
                        editor.putString("Status",result);
                    }
                    else if(type.equalsIgnoreCase("Tag")){
                        editor.putString("Tag",result);
                    }
                    else if(type.equalsIgnoreCase("LeadResource")){
                        editor.putString("LeadResource",result);
                    }
                    else if(type.equalsIgnoreCase("Owner")){
                        editor.putString("Owner",result);
                    }*/
                    editor.commit();
                }

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
            if (!result.isEmpty() && !(result == null)) {
                if (type.equalsIgnoreCase("Status")) {

                    try {
                        jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                objs = jsonArray.getJSONObject(i);
                                spinnerArrayListStatus.add(objs.getString("Name"));
                                spinnerArrayListStatusID.add(objs.getString("id"));
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
                    setSpinner(spinnerStatue, spinnerArrayListStatus);
                    new statusTagLeadSourceOwnerData().execute("Tag");
                } else if (type.equalsIgnoreCase("Tag")) {
                    try {
                        jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                objs = jsonArray.getJSONObject(i);
                                spinnerArrayListTag.add(objs.getString("Name"));
                                spinnerArrayListTagID.add(objs.getString("id"));
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
                    setSpinner(spinnerTag, spinnerArrayListTag);
                    new statusTagLeadSourceOwnerData().execute("LeadResource");
                } else if (type.equalsIgnoreCase("LeadResource")) {
                    try {
                        jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                objs = jsonArray.getJSONObject(i);
                                spinnerArrayListLeadSource.add(objs.getString("Name"));
                                spinnerArrayListLeadSourceID.add(objs.getString("id"));
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
                    setSpinner(spinnerLeadSource, spinnerArrayListLeadSource);
                    new statusTagLeadSourceOwnerData().execute("Owner");
                } else if (type.equalsIgnoreCase("Owner")) {
                    try {
                        jsonArray = new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                objs = jsonArray.getJSONObject(i);
                                spinnerArrayListOwner.add(objs.getString("Name"));
                                spinnerArrayListOwnerID.add(objs.getString("id"));
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
                    setSpinner(spinnerOwner, spinnerArrayListOwner);
                    try {
                        for (int i = 0; i < spinnerArrayListOwnerID.size(); i++) {
                            if (spinnerArrayListOwnerID.get(i).equalsIgnoreCase(SMID)) {
                                spinnerOwner.setSelection(i);
                            }
                        }

                    } catch (Exception e) {

                    }
                    new countryName().execute("contary");
                    //new statusTagLeadSourceOwnerData().execute("Product");
                }
                /*else if(type.equalsIgnoreCase("Product")){
                    dfdffdg;
                    try {
                        jsonArray=new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                objs = jsonArray.getJSONObject(i);
                                spinnerArrayListOwner.add(objs.getString("Name"));
                                spinnerArrayListOwnerID.add(objs.getString("id"));
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
                    setSpinner(spinnerOwner,spinnerArrayListOwner);
                    try{
                        for(int i=0;i<spinnerArrayListOwnerID.size();i++){
                            if(spinnerArrayListOwnerID.get(i).equalsIgnoreCase(SMID)){
                                spinnerOwner.setSelection(i);
                            }
                        }

                    }catch (Exception e){

                    }
                    new countryName().execute("contary");
                }*/

            } else {
                new Custom_Toast(AddContact.this, "No Data Found").showCustomAlert();
            }

        }
    }

    public String getstatusTagLeadSourceOwnerData(String serviceURL) {
        String url = null;
        //url = "http://" + server + "/And_Sync.asmx/xjsphoneEmailUrlType_CRM?key="+ serviceURL;
        if (serviceURL.equalsIgnoreCase("Status")) {
            //url = "http://" + server + "/And_Sync.asmx/xjsStatus_CRM";
            url = "http://" + server + "/And_Sync.asmx/xjsStatus_CRM";
        } else if (serviceURL.equalsIgnoreCase("Tag")) {
            // url = "http://" + server + "/And_Sync.asmx/xjsTag_CRM";
            url = "http://" + server + "/And_Sync.asmx/xjsTag_CRM";
        } else if (serviceURL.equalsIgnoreCase("LeadResource")) {
            //url = "http://" + server + "/And_Sync.asmx/xjsLeadsource_CRM";
            url = "http://" + server + "/And_Sync.asmx/xjsLeadsource_CRM";
        } else if (serviceURL.equalsIgnoreCase("Owner")) {
            //  url = "http://" + server + "/And_Sync.asmx/xjsowner_CRM?smid="+1548;
            url = "http://" + server + "/And_Sync.asmx/xjsowner_Contact_CRM?smid=" + SMID;
        }
       /* else if(serviceURL.equalsIgnoreCase("Product")){

        }*/
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }

    protected class jobTitle extends AsyncTask<String, String, String> {
        String result;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                boolean flag = false;
                if (preferencesAddContact.getString("JobTitle", "").length() < 1) {
                    flag = true;
                    result = getjobTitle();
                } else {
                    flag = false;
                    result = preferencesAddContact.getString("JobTitle", "");
                }
                if (flag) {
                    SharedPreferences.Editor editor = preferencesAddContact.edit();
                    editor.putString("JobTitle", result);
                    editor.commit();
                }

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
            if (!result.isEmpty() && !(result == null)) {

                try {
                    jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objs = null;
                        try {
                            Owner owner = new Owner();
                            objs = jsonArray.getJSONObject(i);
                            owner.setId(objs.getString("id"));
                            owner.setName(objs.getString("Name"));
                            jobTitleArray.add(owner);
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
                setStateContarySpinner(spinnerJobTitle, jobTitleArray);
                // addingDynamicControlerOnView(AddContact.this);
                new ProductGroup().execute();
            } else {
                new Custom_Toast(AddContact.this, "No Data Found").showCustomAlert();
            }

        }
    }

    protected class ProductGroup extends AsyncTask<String, String, ArrayList<Owner>> {
        ArrayList<Owner> result;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Owner> doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                ProductGroupController productGroupController = new ProductGroupController(AddContact.this);
                productGroupController.open();
                result = productGroupController.getProductGroupListAddContact();
                productGroupController.close();
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
        protected void onPostExecute(ArrayList<Owner> result) {
            // TODO Auto-generated method stub
            if (!result.isEmpty() && !(result == null)) {

                try {
                    productGroupArray = result;
                 //   setStateContarySpinner(spinnerProductGroup, productGroupArray);
                    if (!ContactID.equalsIgnoreCase("")) {
                        if (allDataArray != null) {
                            for (int i = 0; i < allDataArray.size(); i++) {
//                                for (int j = 0; j < productGroupArray.size(); j++) {
//                                    if (productGroupArray.get(j).getId().equalsIgnoreCase(allDataArray.get(i).getProducGroup())) {
//                                        // editTextAddCompanyDetailsCity.setText(allDataArray.get(i).getCompanyCity());
//                                        // editTextCity.setText(allDataArray.get(i).getCity());
//                                        spinnerProductGroup.setSelection(j);
//                                    }
//
//                                }
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //new getCompanyAndCityList().execute();
            } else {
                new Custom_Toast(AddContact.this, "No Product Found").showCustomAlert();
            }
            addingDynamicControlerOnView(AddContact.this);

        }
    }


    protected class ItemAsyncTask extends AsyncTask<String, String, ArrayList<Owner>> {
        ArrayList<Owner> result;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected ArrayList<Owner> doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                ItemController itemController = new ItemController(AddContact.this);
                itemController.open();
                result = itemController.getItemList(arg0[0]);
                itemController.close();
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
        protected void onPostExecute(ArrayList<Owner> result) {
            // TODO Auto-generated method stub
            if (!result.isEmpty() && !(result == null)) {

                try {
                    itemListArray = result;
                    setStateContarySpinner(spinnerProduct, itemListArray);
                    if (!ContactID.equalsIgnoreCase("")) {
                        if (allDataArray != null) {
                            for (int i = 0; i < allDataArray.size(); i++) {
                                for (int j = 0; j < itemListArray.size(); j++) {
                                    if (itemListArray.get(j).getId().equalsIgnoreCase(allDataArray.get(i).getItemName())) {
                                        // editTextAddCompanyDetailsCity.setText(allDataArray.get(i).getCompanyCity());
                                        // editTextCity.setText(allDataArray.get(i).getCity());
                                        spinnerProduct.setSelection(j);
                                    }

                                }
                            }
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //new getCompanyAndCityList().execute();
            } else {
                new Custom_Toast(AddContact.this, "No Product Found").showCustomAlert();
            }

        }
    }

    //    String compContaryId = "";
//class getAllContaryData extends AsyncTask<String, String, String>{
//    @Override
//    protected String doInBackground(String... strings) {
//
//        compContaryId=strings[0];
//        try {
//            result=getCityList(compContaryId);
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            if(progressDialog!=null)
//            {
//                progressDialog.dismiss();
//            }
//            e.printStackTrace();
//        }
//        return result;
//    }
//}
    protected class getCityStateWise extends AsyncTask<String, String, String> {
        String result;
        String Stateid = "";

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog.show();
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            Stateid = arg0[0];
            try {
                result = getCityList(Stateid);
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
                try {
                    jsonArray = new JSONArray(result);
                    if (forCompanySpinner) {
                        companyCityArray.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                Owner owner = new Owner();
                                objs = jsonArray.getJSONObject(i);
                                owner.setId(objs.getString("id"));
                                owner.setName(objs.getString("nm"));
                                companyCityArray.add(owner);
                                if (fromCheckbox)
                                    cityArray.add(owner);
                            } catch (JSONException e) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
                            }
                        }
                        setStateContarySpinner(spinnerCompanyCity, companyCityArray);

                        if (!ContactID.equalsIgnoreCase("")) {
                            if (allDataArray != null) {
                                for (int i = 0; i < allDataArray.size(); i++) {
                                    for (int j = 0; j < companyCityArray.size(); j++) {
                                        if (companyCityArray.get(j).getId().equalsIgnoreCase(allDataArray.get(i).getCompanyCity())) {
                                            // editTextAddCompanyDetailsCity.setText(allDataArray.get(i).getCompanyCity());
                                            // editTextCity.setText(allDataArray.get(i).getCity());
                                            spinnerCompanyCity.setSelection(j);
                                        }


                                    }
                                }
                            }

                        } else {
                            SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
                            int cCtyd = pref.getInt("cCityId", 0);
                            for (int j = 0; j < companyCityArray.size(); j++) {
                                if (companyCityArray.get(j).getId().equalsIgnoreCase(String.valueOf(cCtyd))) {
                                    // editTextAddCompanyDetailsCity.setText(allDataArray.get(i).getCompanyCity());
                                    // editTextCity.setText(allDataArray.get(i).getCity());
                                    spinnerCompanyCity.setSelection(j);
                                }


                            }
                        }
                    } else if (!forCompanySpinner) {
                        cityArray.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                Owner owner = new Owner();
                                objs = jsonArray.getJSONObject(i);
                                owner.setId(objs.getString("id"));
                                owner.setName(objs.getString("nm"));
                                cityArray.add(owner);
                            } catch (JSONException e) {
                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
                            }
                        }
                        setStateContarySpinner(spinnerCity, cityArray);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (!ContactID.equalsIgnoreCase("")) {
                                    if (allDataArray != null) {
                                        for (int i = 0; i < allDataArray.size(); i++) {
                                            for (int j = 0; j < cityArray.size(); j++) {
                                                if (cityArray.get(j).getId().equalsIgnoreCase(allDataArray.get(i).getCity())) {
                                                    // editTextAddCompanyDetailsCity.setText(allDataArray.get(i).getCompanyCity());
                                                    // editTextCity.setText(allDataArray.get(i).getCity());
                                                    spinnerCity.setSelection(j);
                                                }

                                            }
                                        }
                                    }

                                } else {
                                    SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
                                    int ctyd = pref.getInt("cityId", 0);
                                    for (int j = 0; j < cityArray.size(); j++) {
                                        if (cityArray.get(j).getId().equalsIgnoreCase(String.valueOf(ctyd))) {
                                            // editTextAddCompanyDetailsCity.setText(allDataArray.get(i).getCompanyCity());
                                            // editTextCity.setText(allDataArray.get(i).getCity());
                                            spinnerCity.setSelection(j);
                                        }


                                    }
                                }

                                if (fromCheckbox) {
                                    spinnerCity.setSelection(spinnerCompanyCity.getSelectedItemPosition());
//
                                }
                            }
                        }, 300);
                    }


                } catch (JSONException e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    e.printStackTrace();
                }
            } else {
                new Custom_Toast(AddContact.this, "No City Found").showCustomAlert();
                spinnerCompanyCity.setAdapter(null);
                spinnerCity.setAdapter(null);
            }

        }
    }

    public String getCityList(String stateId) {
        String result;
        /*String url=null;
        url = "http://"+server+"/And_Sync.asmx/XjsBindCRM_City?stateid="+stateId;
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);*/
        CityController cityController = new CityController(this);
        cityController.open();
        result = cityController.getCityObjectStateWise(stateId);
        cityController.close();
        return result;
    }


    /*protected class getCompanyAndCityList extends AsyncTask<String, String, String> {
        String result;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                result=getContaryAndCityList();
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
            if(!result.isEmpty() && !(result == null)){
                JSONArray CityListJsonArray=null;
                JSONArray companyDetailsJsonArray;
                //String cityList="";
                try {
                    jsonArray=new JSONArray(result);
                    JSONObject json = jsonArray.getJSONObject(0);
                    companyDetailsJsonArray = new JSONArray(json.getString("compdetail"));
                    CityListJsonArray = new JSONArray(json.getString("citynm"));
                    contaryHintArray=null;
                    contaryHintArray=  new String[companyDetailsJsonArray.length()];
                    for (int i = 0; i < companyDetailsJsonArray.length(); i++) {
                        JSONObject objs = null;
                        try {
                            CompanyDetails companyDetails=new CompanyDetails();
                            objs = companyDetailsJsonArray.getJSONObject(i);
                            companyDetails.setCompanyID(objs.getString("cmpid"));
                            companyDetails.setCompanyName(objs.getString("cmpnm"));
                            companyDetails.setContaryID(Integer.parseInt(objs.getString("country")));
                            companyDetails.setState(objs.getString("state"));
                            companyDetails.setCity(objs.getString("city"));
                            companyDetails.setAddress(objs.getString("add"));
                            companyDetails.setRemark(objs.getString("desc"));
                            //cityList=objs.getString("citynm");
                            if(objs.has("zip")){
                                companyDetails.setZip(objs.getString("zip"));
                            }
                            else{
                                companyDetails.setZip("0");
                            }

                            contaryHintArray[i]=objs.getString("cmpnm");
                            companyDetalisArray.add(companyDetails);
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
                try{
                    cityHintArray=null;
                    cityHintArray=  new String[CityListJsonArray.length()];
                    for (int i = 0; i < CityListJsonArray.length(); i++) {
                        JSONObject objs = null;

                        try {
                            objs = CityListJsonArray.getJSONObject(i);
                            cityHintArray[i]=objs.getString("city");
                        } catch (JSONException e) {
                            if(progressDialog!=null)
                            {
                                progressDialog.dismiss();
                            }
                            e.printStackTrace();
                        }
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                }
                // sdfsfdsdf;
                // CustomArrayAdapterAutoComplete adapter = new CustomArrayAdapterAutoComplete(AddContact.this, companyDetalisArray, R.layout.company_name_autocomplete);
                try {
                    if(contaryHintArray != null && contaryHintArray.length>0){
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddContact.this, R.layout.autocomplet_layout, contaryHintArray);
                        editTextCompany.setThreshold(1);
                        editTextCompany.setAdapter(adapter);

                    }

                    ArrayAdapter<String> CityAapter = new ArrayAdapter<String>(AddContact.this, R.layout.autocomplet_layout, cityHintArray);
                    editTextAddCompanyDetailsCity.setThreshold(1);
                    editTextAddCompanyDetailsCity.setAdapter(CityAapter);
                    editTextCity.setThreshold(1);
                    editTextCity.setAdapter(CityAapter);

                }catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    if(cityHintArray != null && cityHintArray.length>0){
                        ArrayAdapter<String> CityAapter = new ArrayAdapter<String>(AddContact.this, R.layout.autocomplet_layout, cityHintArray);
                        editTextAddCompanyDetailsCity.setThreshold(1);
                        editTextAddCompanyDetailsCity.setAdapter(CityAapter);
                        editTextCity.setThreshold(1);
                        editTextCity.setAdapter(CityAapter);

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            else{
                new Custom_Toast(AddContact.this,"No Data Found").showCustomAlert();
            }

        }
    }
    public String getContaryAndCityList(){
        String url=null;
        url = "http://"+server+"/And_Sync.asmx/Xjgetcompanydetail_CRM";
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }*/

    public String getjobTitle() {
        String url = null;
        url = "http://" + server + "/And_Sync.asmx/xjsphoneEmailUrlType_CRM?key=Phone";
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }

    void clearFields() {
        finish();
        Intent intent = new Intent(AddContact.this, AddContact.class);
        startActivity(intent);
    }

    public void saveData() {
        DynamicPhoneID = null;
        DynamicURLID = null;
        DynamicFieldsName = null;
        DynamicFieldsPhoneNo = null;
        DynamicFieldsEmail = null;
        DynamicFieldJobTitle = null;
        DynamicFieldskey = null;
        DynamicFieldsValue = null;
        DynamicFieldsWeb = null;
        DynamicPhoneID = new StringBuilder();
        DynamicURLID = new StringBuilder();
        DynamicFieldsName = new StringBuilder();
        DynamicFieldsPhoneNo = new StringBuilder();
        DynamicFieldsEmail = new StringBuilder();
        DynamicFieldJobTitle = new StringBuilder();
        DynamicFieldskey = new StringBuilder();
        DynamicFieldsValue = new StringBuilder();
        DynamicFieldsWeb = new StringBuilder();
        boolean correct = false;
        if (editTextMainMobNo.getText().toString().length() < 10) {
            dismissDiloge();
            new Custom_Toast(AddContact.this, "Please enter 10 digit phone no").showCustomAlert();
            return;
        }
        if (!editTextMainMobNo.getText().toString().isEmpty() || !editTextMainEmail.getText().toString().isEmpty()) {
            if (!editTextMainMobNo.getText().toString().isEmpty()) {
                if (editTextMainMobNo.getText().toString().length() == 10) {
                    if (!editTextMainEmail.getText().toString().isEmpty()) {
                        if (editTextMainEmail.getText().toString().toLowerCase().matches(emailPattern)) {
                            correct = true;
                        } else {
                            dismissDiloge();
                            new Custom_Toast(AddContact.this, "Please Enter Valid Email Id").showCustomAlert();
                        }
                    } else {
                        correct = true;
                    }

                } else {
                    dismissDiloge();
                    new Custom_Toast(AddContact.this, "Please enter 10 digit phone no").showCustomAlert();
                }
            } else if (!editTextMainEmail.getText().toString().isEmpty()) {
                if (editTextMainEmail.getText().toString().toLowerCase().matches(emailPattern)) {
                    correct = true;
                } else {
                    dismissDiloge();
                    new Custom_Toast(AddContact.this, "Please Enter Valid Email Id").showCustomAlert();
                }
            }

        }

//        if(!Patterns.WEB_URL.matcher(editTextMainUrl.getText().toString()).matches())
//        {
//            correct = false;
//            dismissDiloge();
//            new Custom_Toast(AddContact.this,"Please Enter Valid Url").showCustomAlert();
//        }
        if (editTextMainUrl.getText().toString().length() > 0) {
            if (Patterns.WEB_URL.matcher(editTextMainUrl.getText().toString()).matches()) {
                correct = true;
            } else {
                dismissDiloge();
                new Custom_Toast(AddContact.this, "Enter valid url").showCustomAlert();
                correct = false;
            }
        }

        if (correct) {
            //User Contact
            DynamicFieldsPhoneNo.append(editTextMainMobNo.getText().toString());
            DynamicFieldsPhoneNo.append(",");

             /*DynamicFieldsNameType.append(spinnerMainPhoneType.getSelectedItem().toString());
             DynamicFieldsNameType.append(",");*/

            DynamicFieldsName.append(editTextPersonName.getText().toString());
            DynamicFieldsName.append(",");
            //Email
            DynamicFieldsEmail.append(editTextMainEmail.getText().toString());
            DynamicFieldsEmail.append(",");

            /* DynamicFieldsEmailType.append(spinnerMainEmailType.getSelectedItem().toString());
             DynamicFieldsEmailType.append(",");
            */

            DynamicFieldJobTitle.append(jobTitleArray.get(spinnerJobTitle.getSelectedItemPosition()).getName());
            DynamicFieldJobTitle.append(",");

            //URL
            DynamicFieldsWeb.append(editTextMainUrl.getText().toString());
            DynamicFieldsWeb.append(",");
            if (editTextMainUrl.getTag() != null) {
                DynamicURLID.append(editTextMainUrl.getTag());
                DynamicURLID.append(",");
            } else {
                DynamicURLID.append("");
                DynamicURLID.append(",");
            }

            if (editTextMainMobNo.getTag() != null) {
                DynamicPhoneID.append(editTextMainMobNo.getTag());
                DynamicPhoneID.append(",");
            } else {
                DynamicPhoneID.append("");
                DynamicPhoneID.append(",");
            }

            if (DynamicFeildsData("personInfo")) {
                if (DynamicFeildsData("URL")) {
                    if (DynamicFeildsData("DynamicService")) {
                        LeadOrTaskTag = "L";
                        dismissDiloge();
                        new saveDataOnServer().execute();
                    }
                }
            }
        }

        /*else{
            LeadOrTaskTag="L";
            new   saveDataOnServer().execute();
           *//* AlertDialog.Builder builder = new AlertDialog.Builder(AddContact.this);
            builder.setMessage("Do you want to save record as a Task?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int aid) {
                            dialog.cancel();
                            LeadOrTaskTag="T";
                            new   saveDataOnServer().execute();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            LeadOrTaskTag="L";
                            new   saveDataOnServer().execute();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();*//*
        }*/


    }

    public boolean DynamicFeildsData(String type) {
        boolean flag = true;
        switch (type) {
            case "personInfo":
                if (LinearLayoutArrayListForPersonInfo.size() > 0) {
                    contactPerson = new String[LinearLayoutArrayListForPersonInfo.size()];
                    JSONObject userInfo = new JSONObject();
                    for (int i = 0; i < LinearLayoutArrayListForPersonInfo.size(); i++) {
                        if (LinearLayoutArrayListForPersonInfo.get(i).getTag() != null) {
                            DynamicPhoneID.append(LinearLayoutArrayListForPersonInfo.get(i).getTag());
                            DynamicPhoneID.append(",");
                        } else {
                            DynamicPhoneID.append("");
                            DynamicPhoneID.append(",");
                        }
                        LinearLayout parrent = (LinearLayout) LinearLayoutArrayListForPersonInfo.get(i);
                        LinearLayout linearLayoutNameAndJobTitle = (LinearLayout) parrent.getChildAt(0);
                        LinearLayout linearLayoutEmailAndPhone = (LinearLayout) parrent.getChildAt(1);

                        TextInputLayout inputLayoutMainName = (TextInputLayout) linearLayoutNameAndJobTitle.getChildAt(0);
                        LinearLayout linearLayoutJobTite = (LinearLayout) linearLayoutNameAndJobTitle.getChildAt(1);
                        Spinner spinnerValue = (Spinner) linearLayoutJobTite.getChildAt(1);

                       /* LinearLayout linearLayoutName=(LinearLayout)linearLayoutNameAndJobTitle.getChildAt(0);
                        LinearLayout linearLayoutJob=(LinearLayout)linearLayoutNameAndJobTitle.getChildAt(1);*/
                        TextInputLayout inputLayoutPhone = (TextInputLayout) linearLayoutEmailAndPhone.getChildAt(0);
                        TextInputLayout inputLayoutEmail = (TextInputLayout) linearLayoutEmailAndPhone.getChildAt(1);

                        System.out.println("Data is:" + inputLayoutMainName.getEditText().getText().toString() + "-" +
                                jobTitleArray.get(spinnerValue.getSelectedItemPosition()).getName() + "-" + inputLayoutEmail.getEditText().getText().toString() + "-" +
                                inputLayoutPhone.getEditText().getText().toString());
                        //TextInputLayout inputLayoutMainName=(TextInputLayout)linearLayoutName.getChildAt(0);

                        //Working
                       /* if (inputLayoutEmail.getEditText().getText().toString().matches(emailPattern))
                        {
                            Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                        }*/
                        boolean correct = false;

                        if (!inputLayoutMainName.getEditText().getText().toString().isEmpty() && !inputLayoutPhone.getEditText().getText().toString().isEmpty()) {
                            if (inputLayoutPhone.getEditText().getText().toString().length() < 10) {
                                dismissDiloge();
                                new Custom_Toast(AddContact.this, "Please Enter 10 digit phone number", Toast.LENGTH_SHORT).showCustomAlert();
                                flag = false;
                                return flag;
                            }
                            if (!inputLayoutEmail.getEditText().getText().toString().isEmpty()) {
                                if (!inputLayoutEmail.getEditText().getText().toString().isEmpty()) {
                                    if (inputLayoutEmail.getEditText().getText().toString().toLowerCase().matches(emailPattern)) {
                                        if (!inputLayoutPhone.getEditText().getText().toString().isEmpty()) {
                                            if (inputLayoutPhone.getEditText().getText().toString().length() == 10) {
                                                correct = true;
                                            }
                                        } else {
                                            correct = true;
                                        }
                                    } else {
                                        dismissDiloge();
                                        new Custom_Toast(AddContact.this, "Please Enter Valid Email Id").showCustomAlert();
                                        flag = false;
                                        ;
                                        return flag;
                                    }

                                }
                                if (!inputLayoutPhone.getEditText().getText().toString().isEmpty()) {
                                    if (inputLayoutPhone.getEditText().getText().toString().length() == 10) {
                                        correct = true;
                                    } else {
                                        dismissDiloge();
                                        new Custom_Toast(AddContact.this, "Please enter 10 digit phone no", Toast.LENGTH_SHORT).showCustomAlert();
                                        flag = false;
                                        return flag;
                                    }
                                } else {
                                    dismissDiloge();
                                    new Custom_Toast(AddContact.this, "Please enter 10 digit phone no").showCustomAlert();
                                }

                            } else {
                                correct = true;
                            }
                        } else {
                            dismissDiloge();
                            if (inputLayoutMainName.getEditText().getText().toString().isEmpty()) {
                                new Custom_Toast(AddContact.this, "Please Enter Name", Toast.LENGTH_SHORT).showCustomAlert();
                            } else {
                                new Custom_Toast(AddContact.this, "Please enter 10 digit phone no").showCustomAlert();
                            }

                            flag = false;
                            ;
                            return flag;
                        }

                        if (correct) {
                            try {
                                userInfo.put("Phone", validation.vAlNUmericDynamicFileds(inputLayoutMainName.getEditText().getText().toString()));
                                //userInfo.put("Type", spinner.getSelectedItem());

                                DynamicFieldsName.append(validation.vAlNUmericFileds(inputLayoutMainName.getEditText().getText().toString()));
                                DynamicFieldsName.append(",");

                                DynamicFieldsPhoneNo.append(validation.vAlNUmericFileds(inputLayoutPhone.getEditText().getText().toString()));
                                DynamicFieldsPhoneNo.append(",");

                                DynamicFieldsEmail.append(validation.vAlNUmericFileds(inputLayoutEmail.getEditText().getText().toString()));
                                DynamicFieldsEmail.append(",");

                                DynamicFieldJobTitle.append(validation.vAlNUmericFileds(jobTitleArray.get(spinnerValue.getSelectedItemPosition()).getName()));
                                DynamicFieldJobTitle.append(",");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            dismissDiloge();
                            flag = false;
                            return flag;
                        }
                    }
                    try {
                        sendDataonserverJsonObect.put("userInfo", userInfo);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "URL":
                if (LinearLayoutArrayListForURL.size() > 0) {
                    urlPerson = new String[LinearLayoutArrayListForURL.size()];
                    JSONObject url = new JSONObject();
                    for (int i = 0; i < LinearLayoutArrayListForURL.size(); i++) {
                        if (LinearLayoutArrayListForURL.get(i).getTag() != null) {
                            DynamicURLID.append(LinearLayoutArrayListForURL.get(i).getTag());
                            DynamicURLID.append(",");
                        } else {
                            DynamicURLID.append("");
                            DynamicURLID.append(",");
                        }
                        LinearLayout parrent = (LinearLayout) LinearLayoutArrayListForURL.get(i);
                        //LinearLayout linearLayoutSpinner=(LinearLayout)parrent.getChildAt(0);
                        TextInputLayout inputLayout = (TextInputLayout) parrent.getChildAt(0);
                        //Spinner  spinner=(Spinner)linearLayoutSpinner.getChildAt(1);
                        if (inputLayout.getEditText().getText().toString() != null && inputLayout.getEditText().getText().toString().length() > 0) {
                            // urlPerson[i]=inputLayout.getEditText().getText().toString()+"&"+spinner.getSelectedItem().toString();
                            // Toast.makeText(AddContact.this,"Value Is"+urlPerson[i],Toast.LENGTH_SHORT).show();
                            try {
                                if (Patterns.WEB_URL.matcher(inputLayout.getEditText().getText().toString()).matches()) {
                                    url.put("URL", validation.vAlNUmericFileds(inputLayout.getEditText().getText().toString()));

                                    // url.put("Type",spinner.getSelectedItem());

                                    DynamicFieldsWeb.append(validation.vAlNUmericFileds(inputLayout.getEditText().getText().toString()));
                                    DynamicFieldsWeb.append(",");
                                } else {
                                    dismissDiloge();
                                    new Custom_Toast(AddContact.this, "Enter valid url").showCustomAlert();
                                    flag = false;
                                    return false;
                                }
                               /* DynamicFieldsWebType.append(spinner.getSelectedItem());
                                DynamicFieldsWebType.append(",");*/
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            dismissDiloge();
                            new Custom_Toast(AddContact.this, "Enter url").showCustomAlert();
                            flag = false;
                            return false;
                        }

                    }
                    try {
                        sendDataonserverJsonObect.put("url", url);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case "DynamicService":
                if (LinearLayoutArrayListForDynamicControllerView.size() > 0) {
                    DynamicControl = new String[LinearLayoutArrayListForDynamicControllerView.size()];
                    //Set keys = mapDynamicContentValues.keySet();
                    //Iterator itr = keys.iterator();
                    JSONObject dynamicDataJson = new JSONObject();
                    for (int i = 0; i < LinearLayoutArrayListForDynamicControllerView.size(); i++) {
                        String key;
                        String value;
                        LinearLayout linearLayout;
                        TextInputLayout inputLayout;
                        Spinner spinner;
                        AppCompatCheckBox checkBox;
                        key = dynamicArrayList.get(i).getDynamicControlName();
                        value = dynamicArrayList.get(i).getDynamicControlType();

                        System.out.println(key + " - " + value);
                        if (value.equalsIgnoreCase("Single Line Text")) {
                            linearLayout = (LinearLayout) LinearLayoutArrayListForDynamicControllerView.get(i);
                            inputLayout = (TextInputLayout) linearLayout.getChildAt(0);
                            dynamicControlData.put(key, validation.vAlNUmericDynamicFileds(inputLayout.getEditText().getText().toString()));
                            try {
                                dynamicDataJson.put(key, validation.vAlNUmericDynamicFileds(inputLayout.getEditText().getText().toString()));
                                DynamicFieldskey.append("[");
                                DynamicFieldskey.append(key);
                                DynamicFieldskey.append("]");
                                DynamicFieldsValue.append("'");
                                DynamicFieldsValue.append(validation.vAlNUmericDynamicFileds(inputLayout.getEditText().getText().toString()));
                                DynamicFieldsValue.append("'");
                                if (ContactID.equalsIgnoreCase("")) {
                                    DynamicFieldskey.append(",");
                                    DynamicFieldsValue.append(",");
                                } else {
                                    DynamicFieldskey.append("^");
                                    DynamicFieldsValue.append("#");
                                }


                            } catch (JSONException e) {
                                dismissDiloge();
                                e.printStackTrace();
                            }
                        } else if (value.equalsIgnoreCase("Multiple Line Text")) {
                            linearLayout = (LinearLayout) LinearLayoutArrayListForDynamicControllerView.get(i);
                            inputLayout = (TextInputLayout) linearLayout.getChildAt(0);
                            dynamicControlData.put(key, inputLayout.getEditText().getText().toString());
                            try {
                                dynamicDataJson.put(key, validation.vAlNUmericDynamicFileds(inputLayout.getEditText().getText().toString()));
                                DynamicFieldskey.append("[");
                                DynamicFieldskey.append(key);
                                DynamicFieldskey.append("]");
                                DynamicFieldsValue.append("'");
                                DynamicFieldsValue.append(validation.vAlNUmericDynamicFileds(inputLayout.getEditText().getText().toString()));
                                DynamicFieldsValue.append("'");
                                if (ContactID.equalsIgnoreCase("")) {
                                    DynamicFieldskey.append(",");
                                    DynamicFieldsValue.append(",");
                                } else {
                                    DynamicFieldskey.append("^");
                                    DynamicFieldsValue.append("#");
                                }

                            } catch (JSONException e) {
                                dismissDiloge();
                                e.printStackTrace();
                            }

                        } else if (value.equalsIgnoreCase("Number")) {
                            linearLayout = (LinearLayout) LinearLayoutArrayListForDynamicControllerView.get(i);
                            inputLayout = (TextInputLayout) linearLayout.getChildAt(0);
                            dynamicControlData.put(key, inputLayout.getEditText().getText().toString());
                            try {
                                dynamicDataJson.put(key, validation.vNum(inputLayout.getEditText().getText()));
                                DynamicFieldskey.append("[");
                                DynamicFieldskey.append(key);
                                DynamicFieldskey.append("]");
                                DynamicFieldsValue.append(validation.vNum(inputLayout.getEditText().getText()));
                                if (ContactID.equalsIgnoreCase("")) {
                                    DynamicFieldskey.append(",");
                                    DynamicFieldsValue.append(",");
                                } else {
                                    DynamicFieldskey.append("^");
                                    DynamicFieldsValue.append("#");
                                }

                            } catch (JSONException e) {
                                dismissDiloge();
                                e.printStackTrace();
                            }
                        } else if (value.equalsIgnoreCase("Dropdown")) {
                            linearLayout = (LinearLayout) LinearLayoutArrayListForDynamicControllerView.get(i);
                            spinner = (Spinner) linearLayout.getChildAt(1);
                            dynamicControlData.put(key, spinner.getSelectedItem().toString());
                            try {
                                dynamicDataJson.put(key, spinner.getSelectedItem().toString());
                                DynamicFieldskey.append("[");
                                DynamicFieldskey.append(key);
                                DynamicFieldskey.append("]");
                                DynamicFieldsValue.append("'");
                                DynamicFieldsValue.append(spinner.getSelectedItem().toString());
                                DynamicFieldsValue.append("'");
                                if (ContactID.equalsIgnoreCase("")) {
                                    DynamicFieldskey.append(",");
                                    DynamicFieldsValue.append(",");
                                } else {
                                    DynamicFieldskey.append("^");
                                    DynamicFieldsValue.append("#");
                                }

                            } catch (JSONException e) {
                                dismissDiloge();
                                e.printStackTrace();
                            }
                        } else if (value.equalsIgnoreCase("Date")) {
                            linearLayout = (LinearLayout) LinearLayoutArrayListForDynamicControllerView.get(i);
                            inputLayout = (TextInputLayout) linearLayout.getChildAt(0);
                            dynamicControlData.put(key, inputLayout.getEditText().getText().toString());
                            try {
                                String data = "";
                                Date dateInput = null;
                                String valueDate = inputLayout.getEditText().getText().toString();
                                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a", Locale.ENGLISH);
                                try {
                                    dateInput = format.parse(valueDate);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                                String datetime;
                                SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a", Locale.ENGLISH);
                                Date date = new Date();
                                if (dateInput != null) {
                                    datetime = dateformat.format(dateInput);
                                } else {
                                    datetime = dateformat.format(date);
                                }
                                dynamicDataJson.put(key, inputLayout.getEditText().getText().toString());
                                DynamicFieldskey.append("[");
                                DynamicFieldskey.append(key);
                                DynamicFieldskey.append("]");
                                DynamicFieldsValue.append("'");
                                DynamicFieldsValue.append(datetime);
                                DynamicFieldsValue.append("'");
                                if (ContactID.equalsIgnoreCase("")) {
                                    DynamicFieldskey.append(",");
                                    DynamicFieldsValue.append(",");
                                } else {
                                    DynamicFieldskey.append("^");
                                    DynamicFieldsValue.append("#");
                                }
                            } catch (JSONException e) {
                                dismissDiloge();
                                e.printStackTrace();
                            }

                        } else if (value.equalsIgnoreCase("Checkbox")) {
                            //  CheckBox checkBox=new CheckBox(this);
                            linearLayout = (LinearLayout) LinearLayoutArrayListForDynamicControllerView.get(i);
                            LinearLayout childlinearLayout = (LinearLayout) linearLayout.getChildAt(0);
                            LinearLayout keyNamelinearLayout = (LinearLayout) childlinearLayout.getChildAt(0);
                            LinearLayout KeyValuelinearLayout = (LinearLayout) childlinearLayout.getChildAt(1);
                            KeyValuelinearLayout.getChildCount();
                            StringBuilder checkboxValue = new StringBuilder();
                            String finalValue = "";
                            for (int m = 0; m < KeyValuelinearLayout.getChildCount(); m++) {
                                checkBox = (AppCompatCheckBox) KeyValuelinearLayout.getChildAt(m);
                                if (checkBox.isChecked()) {
                                    checkboxValue.append(checkBox.getText() + "-");
                                }
                            }

                            //dynamicControlData.put(key,String.valueOf(checkBox.isChecked()?"Y":"N"));
                            try {
                                if (checkboxValue.length() > 0) {
                                    finalValue = checkboxValue.substring(0, checkboxValue.length() - 1);
                                }
                                dynamicDataJson.put(key, finalValue);
                                DynamicFieldskey.append("[");
                                DynamicFieldskey.append(key);
                                DynamicFieldskey.append("]");
                                DynamicFieldsValue.append("'");
                                //DynamicFieldsValue.append(String.valueOf(checkBox.isChecked()?"Y":"N"));
                                DynamicFieldsValue.append(finalValue);
                                DynamicFieldsValue.append("'");
                                if (ContactID.equalsIgnoreCase("")) {
                                    DynamicFieldskey.append(",");
                                    DynamicFieldsValue.append(",");
                                } else {
                                    DynamicFieldskey.append("^");
                                    DynamicFieldsValue.append("#");
                                }

                            } catch (JSONException e) {
                                dismissDiloge();
                                e.printStackTrace();
                            }

                        } else if (value.equalsIgnoreCase("Sql View")) {
                            linearLayout = (LinearLayout) LinearLayoutArrayListForDynamicControllerView.get(i);
                            spinner = (Spinner) linearLayout.getChildAt(1);
                            dynamicControlData.put(key, spinner.getSelectedItem().toString());
                            try {
                                dynamicDataJson.put(key, spinner.getSelectedItem().toString());
                                DynamicFieldskey.append("[");
                                DynamicFieldskey.append(key);
                                DynamicFieldskey.append("]");
                                DynamicFieldsValue.append("'");
                                DynamicFieldsValue.append(spinner.getSelectedItem().toString());
                                DynamicFieldsValue.append("'");
                                if (ContactID.equalsIgnoreCase("")) {
                                    DynamicFieldskey.append(",");
                                    DynamicFieldsValue.append(",");
                                } else {
                                    DynamicFieldskey.append("^");
                                    DynamicFieldsValue.append("#");
                                }

                            } catch (JSONException e) {
                                dismissDiloge();
                                e.printStackTrace();
                            }
                        } else if (value.equalsIgnoreCase("Sql Procedure")) {
                            linearLayout = (LinearLayout) LinearLayoutArrayListForDynamicControllerView.get(i);
                            spinner = (Spinner) linearLayout.getChildAt(1);
                            dynamicControlData.put(key, spinner.getSelectedItem().toString());
                            try {
                                dynamicDataJson.put(key, spinner.getSelectedItem().toString());
                                DynamicFieldskey.append("[");
                                DynamicFieldskey.append(key);
                                DynamicFieldskey.append("]");
                                DynamicFieldsValue.append("'");
                                DynamicFieldsValue.append(spinner.getSelectedItem().toString());
                                DynamicFieldsValue.append("'");
                                if (ContactID.equalsIgnoreCase("")) {
                                    DynamicFieldskey.append(",");
                                    DynamicFieldsValue.append(",");
                                } else {
                                    DynamicFieldskey.append("^");
                                    DynamicFieldsValue.append("#");
                                }
                            } catch (JSONException e) {
                                dismissDiloge();
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        sendDataonserverJsonObect.put("DynamicControlerData", dynamicDataJson);
                    } catch (JSONException e) {
                        dismissDiloge();
                        e.printStackTrace();
                    }

                    System.out.println("Save Data is:" + sendDataonserverJsonObect.toString());
                   /* Set keysdate = dynamicControlData.keySet();
                    Iterator itrkey = keysdate.iterator();

                    while(itrkey.hasNext())
                    {
                        String datakey = (String) itrkey.next();
                        String value = (String) dynamicControlData.get(datakey);
                        System.out.println("Save Data is:"+datakey+"-"+value);
                    }
                    }
                try {
                    System.out.println("JSON:"+dynamicControlData.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                }
                break;
            default:
                flag = false;
                break;
        }
        return flag;
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
                LinearLayoutArrayListForPersonInfo.remove(linearLayout);
                linearLayoutdynamicfieldForPersonInfo.removeView(linearLayout);
//                    linearLayout.removeAllViews();
                // new Custom_Toast(getApplicationContext(),Flag+" Field Remove Successfully").showCustomAlert();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDynamicPersonInfo(AddContact.this, "personInfo", false, null);
            }
        });
        linearLayoutdynamicfieldForPersonInfo.addView(parent);
        ;
        LinearLayoutArrayListForPersonInfo.add(parent);
        ;
    }

    @Override
    protected void onResume() {
        super.onResume();
        DbCon dbCon = new DbCon(getApplicationContext());
        dbCon.open();
        //save.setEnabled(dbCon.ButtonEnable("LeaveRequest","Party","Add"));
        if (ContactID.equalsIgnoreCase("")) {
            if (!dbCon.ButtonEnable("AddContact", "CRM", "Add")) {
                save.setColorFilter(Color.parseColor("#808080"));
                save.setEnabled(false);
            }
        }
        if (!ContactID.equalsIgnoreCase("")) {
            if (!dbCon.ButtonEnable("AddContact", "CRM", "Edit")) {
                save.setColorFilter(Color.parseColor("#808080"));
                save.setEnabled(false);
            }
        }

        if (!dbCon.ButtonEnable("AddContact", "CRM", "Delete")) {
            delete.setColorFilter(Color.parseColor("#808080"));
            delete.setEnabled(false);
        }
        dbCon.close();

    }

    /* void addDynamicPersonInfo(Context myContext, final String Flag,boolean isUpload,JSONObject jsonValues){
            LinearLayout  parent = new LinearLayout(myContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,10,4,5);
            parent.setBackgroundColor(Color.parseColor("#a0bbfa"));
            params.gravity= Gravity.CENTER;
            parent.setOrientation(LinearLayout.VERTICAL);
            parent.setLayoutParams(params);
            LinearLayout  child1 = new LinearLayout(myContext);
            LinearLayout.LayoutParams paramsChild1 = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsChild1.setMargins(10,10,10,10);
            paramsChild1.gravity= Gravity.CENTER_VERTICAL;
            child1.setOrientation(LinearLayout.HORIZONTAL);

            LinearLayout  child2 = new LinearLayout(myContext);
            LinearLayout.LayoutParams paramsChild2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsChild2.setMargins(10,10,10,10);
            paramsChild2.gravity= Gravity.CENTER_VERTICAL;
            child2.setOrientation(LinearLayout.HORIZONTAL);


            LinearLayout  child3 = new LinearLayout(myContext);
            LinearLayout.LayoutParams paramsChild3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsChild3.setMargins(10,10,10,10);
            paramsChild3.gravity= Gravity.CENTER;
            paramsChild3.weight=1.0f;
            child3.setLayoutParams(paramsChild3);
            child3.setOrientation(LinearLayout.HORIZONTAL);



            AppCompatEditText editTextContactName=new AppCompatEditText(myContext);
            editTextContactName.setTypeface(Typeface.SERIF);

            AppCompatEditText editTextPhone=new AppCompatEditText(myContext);
            editTextPhone.setTypeface(Typeface.SERIF);

            AppCompatEditText editTextEmail=new AppCompatEditText(myContext);
            editTextEmail.setTypeface(Typeface.SERIF);

            ImageView buttonRemove=new ImageView(myContext);
            ImageView buttonAdd=new ImageView(myContext);

            TextInputLayout contactNametitleWrapper = new TextInputLayout(this);

            TextInputLayout phonetitleWrapper = new TextInputLayout(this);

            TextInputLayout emailtitleWrapper = new TextInputLayout(this);

            TextView JobtitleTextView = new TextView(this);

            Spinner spinnerJobTitle=new Spinner(myContext,Spinner.MODE_DIALOG);
            paramsChild1.weight = 1.0f;

            editTextContactName.setLayoutParams(paramsChild1);
            editTextContactName.setSingleLine(true);
            editTextContactName.setTextSize(14);
            editTextContactName.setTextColor(Color.parseColor("#000000"));
            editTextContactName.setHintTextColor(Color.parseColor("#FF4081"));

            contactNametitleWrapper.setHint("Contact Name:");
            contactNametitleWrapper.setLayoutParams(paramsChild1);
            editTextContactName.setInputType(InputType.TYPE_CLASS_TEXT);
            contactNametitleWrapper.addView(editTextContactName);

            paramsChild1.weight = 1.0f;
            editTextPhone.setLayoutParams(paramsChild1);
            editTextPhone.setSingleLine(true);
            editTextPhone.setTextSize(14);
            editTextPhone.setTextColor(Color.parseColor("#000000"));
            editTextPhone.setHintTextColor(Color.parseColor("#FF4081"));


            phonetitleWrapper.setHint("Enter Phone No.:");
            phonetitleWrapper.setLayoutParams(paramsChild1);
            editTextPhone.setInputType(InputType.TYPE_CLASS_PHONE);
            editTextPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
            phonetitleWrapper.addView(editTextPhone);


            paramsChild2.weight = 1.0f;
            editTextEmail.setLayoutParams(paramsChild2);
            editTextEmail.setSingleLine(true);
            editTextEmail.setTextSize(14);
            editTextEmail.setTextColor(Color.parseColor("#000000"));
            editTextEmail.setHintTextColor(Color.parseColor("#FF4081"));

            emailtitleWrapper.setHint("Email:");
            emailtitleWrapper.setLayoutParams(paramsChild1);
            editTextEmail.setInputType(InputType.TYPE_CLASS_TEXT);
            emailtitleWrapper.addView(editTextEmail);

            paramsChild2.weight = 1.0f;
            LinearLayout  jobtitleLayout = new LinearLayout(myContext);
            LinearLayout.LayoutParams jobtitleParams = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.WRAP_CONTENT);
            jobtitleParams.weight = 1;
            jobtitleParams.setMargins(0,0,30,0);
            jobtitleParams.gravity= Gravity.CENTER_VERTICAL;
            jobtitleLayout.setOrientation(LinearLayout.VERTICAL);
            jobtitleLayout.setLayoutParams(paramsChild2);
            jobtitleLayout.setLayoutParams(jobtitleParams);

            JobtitleTextView.setText("Job Title:");
            JobtitleTextView.setVisibility(View.GONE);
            spinnerJobTitle.setSelection(0);
            spinnerJobTitle.setPrompt("Select Job Title");
            setStateContarySpinner(spinnerJobTitle,jobTitleArray);

            jobtitleLayout.addView(JobtitleTextView);
            jobtitleLayout.addView(spinnerJobTitle);
            View v = new View(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);
            v.setBackgroundColor(Color.BLACK);
            v.setLayoutParams(lp);
            jobtitleLayout.addView(v);

            if(isUpload){
                try {
                    parent.setTag(jsonValues.getString("PhoneId"));
                    editTextContactName.setText(jsonValues.getString("ContName"));
                    editTextPhone.setText(jsonValues.getString("Phone"));
                    editTextEmail.setText(jsonValues.getString("Email"));
                    for(int i=0;i<jobTitleArray.size();i++){
                        if(jobTitleArray.get(i).getName().equalsIgnoreCase(jsonValues.getString("PhoneType")))
                        {
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
            RelativeLayout child4= new RelativeLayout(myContext);
            LinearLayout.LayoutParams paramsChild4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsChild4.setMargins(10,10,10,10);
            paramsChild4.gravity= Gravity.CENTER;
            child4.setLayoutParams(paramsChild4);

            RelativeLayout.LayoutParams paramsChild5 = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            paramsChild5.addRule(RelativeLayout.CENTER_IN_PARENT);


            LinearLayout  child6 = new LinearLayout(myContext);
            child6.setLayoutParams(paramsChild5);
            child6.setOrientation(LinearLayout.HORIZONTAL);



            buttonAdd.setLayoutParams(paramsChild5);
            buttonAdd.setBackgroundResource(R.drawable.addfields);
            child6.addView(buttonAdd);

            buttonRemove.setBackgroundResource(R.drawable.minus);
            paramsChild5.addRule(RelativeLayout.RIGHT_OF,buttonAdd.getId());
            buttonRemove.setLayoutParams(paramsChild5);
            child6.addView(buttonRemove);
            child4.addView(child6);
            child3.addView(child4);
            parent.addView(child3);

           // new Custom_Toast(getApplicationContext(),Flag+" Field Added Successfully").showCustomAlert();
            buttonRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout linearLayout=(LinearLayout)v.getParent().getParent().getParent().getParent();
                    LinearLayoutArrayListForPersonInfo.remove(linearLayout);
                    linearLayoutdynamicfieldForPersonInfo.removeView(linearLayout);
    //                    linearLayout.removeAllViews();
                  // new Custom_Toast(getApplicationContext(),Flag+" Field Remove Successfully").showCustomAlert();
                }
            });
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addDynamicPersonInfo(AddContact.this,"personInfo",false,null);
                }
            });
            linearLayoutdynamicfieldForPersonInfo .addView(parent);;
            LinearLayoutArrayListForPersonInfo.add(parent);;
        }*/
    void addEditText(Context myContext, final String Flag, boolean isUpload, JSONObject jsonValues) {
        LinearLayout parent = new LinearLayout(myContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(4, 0, 4, 0);
        parent.setBackgroundResource(R.drawable.dk_gray_border);
        params.gravity = Gravity.CENTER_VERTICAL;
        parent.setOrientation(LinearLayout.HORIZONTAL);
        parent.setLayoutParams(params);
        params.weight = 1.3f;
        AppCompatEditText editTextURL = new AppCompatEditText(myContext);
        editTextURL.setTypeface(Typeface.SERIF);
        TextInputLayout urltitleWrapper = new TextInputLayout(this);

        editTextURL.setLayoutParams(params);
        editTextURL.setSingleLine(true);
        //    editTextURL.setTextSize(14);
        editTextURL.setTextColor(Color.parseColor("#000000"));
        editTextURL.setHintTextColor(Color.parseColor("#FF4081"));

        urltitleWrapper.setHint("URL:");
        urltitleWrapper.setLayoutParams(params);
        editTextURL.setInputType(InputType.TYPE_CLASS_TEXT);
        urltitleWrapper.addView(editTextURL);


        LinearLayout.LayoutParams paramsChild3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_VERTICAL;
        params.weight = 0.7f;
        ImageView buttonRemove = new ImageView(myContext);
        buttonRemove.setBackgroundResource(R.drawable.minus);
        paramsChild3.gravity = Gravity.CENTER_VERTICAL;
        buttonRemove.setLayoutParams(paramsChild3);
        parent.addView(urltitleWrapper);
        if (isUpload) {
            try {
                parent.setTag(jsonValues.getString("URlId"));
                editTextURL.setText(jsonValues.getString("URl"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        parent.addView(buttonRemove);
        LinearLayoutArrayListForURL.add(parent);
        linearlayoutdynamicfieldsURL.addView(parent);
       /* LinearLayout  parent = new LinearLayout(myContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,30,30,0);
        params.gravity= Gravity.CENTER_VERTICAL;
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setLayoutParams(params);
        AppCompatEditText nameEdittext=new AppCompatEditText(myContext);
        nameEdittext.setTypeface(Typeface.SERIF);
        AppCompatEditText ed=new AppCompatEditText(myContext);
        ed.setTypeface(Typeface.SERIF);
        ImageView buttonRemove=new ImageView(myContext);
        buttonRemove.setLayoutParams(params);
        TextInputLayout nametitleWrapper = new TextInputLayout(this);
        TextInputLayout titleWrapper = new TextInputLayout(this);
        LinearLayout  linearLayoutChild2 = new LinearLayout(myContext);
        LinearLayout.LayoutParams paramsChild2 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayoutChild2.setOrientation(LinearLayout.HORIZONTAL);
        paramsChild2.weight = 1.3f;
        paramsChild2.weight = 0.7f;
        ed.setLayoutParams(paramsChild2);
        ed.setSingleLine(true);
        ed.setTextSize(14);
        ed.setTextColor(Color.parseColor("#000000"));
        ed.setHintTextColor(Color.parseColor("#FF4081"));
        buttonRemove.setBackgroundResource(R.drawable.minus);
        paramsChild2.weight = 0.5f;
        if(Flag.equalsIgnoreCase("URL")) {
            titleWrapper.setHint("URL:");
            linearLayoutChild2.addView(titleWrapper);
            titleWrapper.setLayoutParams(paramsChild2);
            ed.setInputType(InputType.TYPE_CLASS_TEXT);
            titleWrapper.addView(ed);
            linearLayoutChild2.addView(buttonRemove);
            parent.addView(linearLayoutChild2);
            if(isUpload){
                try {
                    ed.setText(jsonValues.getString("URl"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            LinearLayoutArrayListForURL.add(parent);
            linearlayoutdynamicfieldsURL.addView(parent);
        }
        if(!isUpload){
            new Custom_Toast(getApplicationContext(),"New "+Flag+" Field Added Successfully").showCustomAlert();
        }*/
        //new Custom_Toast(getApplicationContext(),Flag+" Field Added Successfully").showCustomAlert();
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout linearLayout = (LinearLayout) v.getParent();
                if (Flag.equalsIgnoreCase("URL")) {
                    LinearLayoutArrayListForURL.remove(linearLayout);
                    linearlayoutdynamicfieldsURL.removeView(linearLayout);
                }
                // new Custom_Toast(getApplicationContext(),Flag+" Field Remove Successfully").showCustomAlert();
            }
        });

    }

    protected class DynamicController extends AsyncTask<String, String, String> {
        String result = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            isDynamicControlledCalled = true;
        }

        @Override
        protected String doInBackground(String... arg0) {

            // TODO Auto-generated method stub
            try {
                boolean flag = false;
                if (preferencesAddContact.getString("DymnamicControl", "").length() < 1) {
                    flag = true;
                    result = dynamicControllerData();
                } else {
                    flag = false;
                    result = preferencesAddContact.getString("DymnamicControl", "");
                }
                if (flag) {
                    SharedPreferences.Editor editor = preferencesAddContact.edit();
                    editor.putString("DymnamicControl", result);
                    editor.commit();
                }

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
            if (!result.isEmpty() && !(result == null)) {
                try {
                    dynamicArrayList.clear();
                    jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objs = null;
                        try {
                            objs = jsonArray.getJSONObject(i);
                            AddContactModel addContactModel = new AddContactModel();
                            String ControlName = objs.getString("ControlName");
                            String ControlType = objs.getString("ControlType");
                            String Attributedata = objs.getString("Attributedata");
                            addContactModel.setDynamicControlName(ControlName);
                            addContactModel.setDynamicControlType(ControlType);
                            addContactModel.setDynmicSpinerValue(Attributedata);
                            dynamicArrayList.add(addContactModel);
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

                new jobTitle().execute();


            } else {
                new Custom_Toast(AddContact.this, "No Data Found").showCustomAlert();
            }

        }
    }

    public void addingDynamicControlerOnView(Context context) {
        for (int i = 0; i < allDataArray.size(); i++) {
            editTextCompany.setText(allDataArray.get(i).getComapy());
            // editTextAddCompanyDetailsCity.setText(allDataArray.get(i).getCompanyCity());
            // editTextCity.setText(allDataArray.get(i).getCity());
            editTextAddCompanyDetailsDiscription.setText(allDataArray.get(i).getCompanyDisscussion());
            //editTextCompanyPhone.setText(allDataArray.get(i).getCompanyPhone());
            editTextAddCompanyDetailsZip.setText(allDataArray.get(i).getCompanyZip());
            editTextAddCompanyDetailsAddress.setText(allDataArray.get(i).getCompanyAddress());
            editTextFirstName.setText(allDataArray.get(i).getFirstName());
            //editTextLastName.setText(allDataArray.get(i).getLastName());
            editTextPersonName.setText(allDataArray.get(i).getFirstName());
            //editTextJobTittle.setText(allDataArray.get(i).getJobTitle());
            editTextZip.setText(allDataArray.get(i).getZip());
            try {
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.ic_user)
                        .showImageForEmptyUri(R.drawable.ic_user)
                        .showImageOnFail(R.drawable.ic_user)
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();
                ImageLoader imageLoader = ImageLoader.getInstance();
                //selectedImageBase64ImgString=allDataArray.get(i).getImageUrl();
                imageLoader.displayImage(allDataArray.get(i).getImageUrl(), capturedImage, options);
            } catch (Exception e) {
                e.printStackTrace();
            }
            JSONArray jsonArrayPhone = allDataArray.get(i).getJsonPhone();
            for (int p = i; p < jsonArrayPhone.length(); p++) {
                JSONObject jsonObjectPhone = null;
                try {
                    jsonObjectPhone = jsonArrayPhone.getJSONObject(p);
                    if (p == 0) {
                        if (jsonArrayPhone != null) {
                            editTextMainMobNo.setTag(jsonObjectPhone.getString("PhoneId"));
                            editTextPersonName.setText(jsonObjectPhone.getString("ContName"));
                            editTextMainEmail.setText(jsonObjectPhone.getString("Email"));
                            editTextMainMobNo.setText(jsonObjectPhone.getString("Phone"));
                            for (int k = 0; k < jobTitleArray.size(); k++) {
                                if (jobTitleArray.get(k).getName().equalsIgnoreCase(jsonObjectPhone.getString("PhoneType"))) {
                                    spinnerJobTitle.setSelection(k);
                                }
                            }

                        }

                    } else {
                        addDynamicPersonInfo(AddContact.this, "Mobile", true, jsonObjectPhone);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            /*JSONArray jsonArrayEmail=allDataArray.get(i).getJsonEmail();
            for(int q=i;q<jsonArrayEmail.length();q++){
                try {
                    JSONObject jsonObjectEmail=null;
                    jsonObjectEmail=jsonArrayEmail.getJSONObject(q);
                    if(q==0){
                        if(jsonObjectEmail !=null){
                            editTextMainEmail.setText(jsonObjectEmail.getString("ContName"));
                            editTextMainEmail.setText(jsonObjectEmail.getString("Email"));
                        }
                    }else{
                        addDynamicPersonInfo(AddContact.this,"Email",true,jsonObjectEmail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }*/
            JSONArray jsonArrayURL = allDataArray.get(i).getJsonURL();
            System.out.println("jsonArrayURL:" + jsonArrayURL.length());
            for (int r = i; r < jsonArrayURL.length(); r++) {
                try {
                    JSONObject jsonObjectURL = null;
                    jsonObjectURL = jsonArrayURL.getJSONObject(r);
                    if (r == 0) {
                        if (jsonObjectURL != null) {
                            editTextMainUrl.setTag(jsonObjectURL.getString("URlId"));
                            editTextMainUrl.setText(jsonObjectURL.getString("URl"));
                        }
                    } else {
                        addEditText(AddContact.this, "URL", true, jsonObjectURL);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            editTextAddress.setText(allDataArray.get(i).getAddress());

            editTextBackground.setText(allDataArray.get(i).getBackground());
            for (int j = 0; j < contaryArray.size(); j++) {
                if (contaryArray.get(j).getId().equalsIgnoreCase(allDataArray.get(i).getCompanyContry())) {
                    spinnerAddCompanyDetailsCountry.setSelection(j);
                }
                if (contaryArray.get(j).getName().equalsIgnoreCase(allDataArray.get(i).getContry())) {
                    spinnerCountry.setSelection(j);
                }

            }
            for (int j = 0; j < stateArray.size(); j++) {
                if (stateArray.get(j).getName().equalsIgnoreCase(allDataArray.get(i).getCompanyState())) {
                    spinnerAddCompanyDetailsState.setSelection(j);
                }
                if (stateArray.get(j).getName().equalsIgnoreCase(allDataArray.get(i).getState())) {
                    spinnerState.setSelection(j);
                }

            }

            for (int j = 0; j < spinnerArrayListStatusID.size(); j++) {
                if (spinnerArrayListStatusID.get(j).equalsIgnoreCase(allDataArray.get(i).getStatus())) {
                    spinnerStatue.setSelection(j);
                }
            }
            for (int j = 0; j < spinnerArrayListTagID.size(); j++) {
                if (spinnerArrayListTagID.get(j).equalsIgnoreCase(allDataArray.get(i).getTag())) {
                    spinnerTag.setSelection(j);
                }
            }
            for (int j = 0; j < spinnerArrayListLeadSourceID.size(); j++) {
                if (spinnerArrayListLeadSourceID.get(j).equalsIgnoreCase(allDataArray.get(i).getLeadStatus())) {
                    spinnerLeadSource.setSelection(j);
                }
            }
            for (int j = 0; j < spinnerArrayListOwnerID.size(); j++) {
                if (spinnerArrayListOwnerID.get(j).equalsIgnoreCase(allDataArray.get(i).getOwner())) {
                    spinnerOwner.setSelection(j);
                }
            }
            for (int j = 0; j < stateArray.size(); j++) {
                if (stateArray.get(j).getName().equalsIgnoreCase(allDataArray.get(i).getState())) {
                    spinnerState.setSelection(j);
                }
            }

            for (int j = 0; j < stateArray.size(); j++) {
                if (stateArray.get(j).getName().equalsIgnoreCase(allDataArray.get(i).getCompanyState())) {
                    spinnerAddCompanyDetailsState.setSelection(j);
                }
            }

            for (int j = 0; j < spinnerArrayListTagID.size(); j++) {
                if (spinnerArrayListTagID.get(j).equalsIgnoreCase(allDataArray.get(i).getTag())) {
                    spinnerTag.setSelection(j);
                }
            }
            for (int j = 0; j < spinnerArrayListLeadSourceID.size(); j++) {
                if (spinnerArrayListLeadSourceID.get(j).equalsIgnoreCase(allDataArray.get(i).getLead())) {
                    spinnerLeadSource.setSelection(j);
                }
            }
            for (int j = 0; j < spinnerArrayListOwnerID.size(); j++) {
                if (spinnerArrayListOwnerID.get(j).equalsIgnoreCase(allDataArray.get(i).getOwner())) {
                    spinnerOwner.setSelection(j);
                }
            }

            checkboxActive = (CheckBox) findViewById(R.id.aacActive);
            checkboxActive.setChecked(allDataArray.get(i).getActive());
        }
        String key;
        String value;
        for (int i = 0; i < dynamicArrayList.size(); i++) {
            JSONObject jsonObjectDynamivControlValue = null;
            boolean flag = true;
            try {
                flag = true;
                jsonObjectDynamivControlValue = allDataArray.get(0).getDynamicControlData();
            } catch (Exception e) {
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                flag = false;
                e.printStackTrace();
            }
            LinearLayout parent = new LinearLayout(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 20, 0, 0);
            parent.setOrientation(LinearLayout.VERTICAL);
            parent.setLayoutParams(params);
            EditText ed = new EditText(context);
            //EditText ed=new EditText(new ContextThemeWrapper(AddContact.this, R.style.DynamicEdittext));
            // ed.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            TextInputLayout titleWrapper = new TextInputLayout(this);
            //AppCompatSpinner spinner=new AppCompatSpinner(context,AppCompatSpinner.MODE_DIALOG);
            Spinner spinner = new Spinner(context, Spinner.MODE_DIALOG);
            // MaterialBetterSpinner spinner = (MaterialBetterSpinner) findViewById(R.id.spinner_Type);
            key = dynamicArrayList.get(i).getDynamicControlName();
            value = dynamicArrayList.get(i).getDynamicControlType();
            System.out.println(key + " - " + value);
            if (value.equalsIgnoreCase("Single Line Text")) {
                titleWrapper.setHint(key);
                parent.addView(titleWrapper);
                ed.setSingleLine(true);
                //   ed.setTextSize(14);
                ed.setTextColor(Color.parseColor("#000000"));
                ed.setInputType(InputType.TYPE_CLASS_TEXT);
                if (flag) {
                    try {
                        String edittextValue = jsonObjectDynamivControlValue.getString(key);
                        ed.setText(edittextValue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                titleWrapper.addView(ed);
            } else if (value.equalsIgnoreCase("Multiple Line Text")) {
                titleWrapper.setHint(key);
                parent.addView(titleWrapper);
                ed.setTextColor(Color.parseColor("#000000"));
                //  ed.setTextSize(14);
                ed.setInputType(InputType.TYPE_CLASS_TEXT);
                if (flag) {
                    try {
                        String edittextValue = jsonObjectDynamivControlValue.getString(key);
                        ed.setText(edittextValue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                titleWrapper.addView(ed);
            } else if (value.equalsIgnoreCase("Number")) {
                titleWrapper.setHint(key);
                parent.addView(titleWrapper);
                ed.setSingleLine(true);
                ed.setTextColor(Color.parseColor("#000000"));
                //    ed.setTextSize(14);
                ed.setInputType(InputType.TYPE_CLASS_NUMBER);
                int maxLengthofEditText = 9;
                ed.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLengthofEditText)});
                if (flag) {
                    try {
                        String edittextValue = jsonObjectDynamivControlValue.getString(key);
                        ed.setText(edittextValue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                titleWrapper.addView(ed);
            } else if (value.equalsIgnoreCase("Dropdown")) {
                // params.setMargins(10,20,0,0);
                // parent.setLayoutParams(params);
                String[] spValue = dynamicArrayList.get(i).getDynmicSpinerValue().split("\\*\\#");
                ArrayList<String> arrayValue = new ArrayList<String>(Arrays.asList(spValue));
                TextView tv = new TextView(context);
                tv.setTextColor(getResources().getColor(R.color.colorTextHeader));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                tv.setText(key);

//                tv.setAllCaps(true);
                tv.setPadding(0, 0, 0, 0);
                // params.setMargins(10,0,0,0);
                // parent.setLayoutParams(params);
                parent.addView(tv);
                //  params.setMargins(0,0,0,0);
                //  parent.setLayoutParams(params);
                spinner.setPrompt("Select " + key);
                setSpinner(spinner, arrayValue);
                if (flag) {
                    try {
                        for (int n = 0; n < arrayValue.size(); n++) {
                            if (arrayValue.get(n).equalsIgnoreCase(jsonObjectDynamivControlValue.getString(key))) {
                                spinner.setSelection(n);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                parent.addView(spinner);
                LinearLayout lineBelowSpinnerLayour = new LinearLayout(context);
                LinearLayout.LayoutParams lineBelowSpinnerLayourParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 2);

                lineBelowSpinnerLayour.setOrientation(LinearLayout.VERTICAL);
                lineBelowSpinnerLayourParams.setMargins(5, 0, 8, 0);
                lineBelowSpinnerLayour.setBackgroundColor(getResources().getColor(R.color.colorUnderline));
                lineBelowSpinnerLayour.setLayoutParams(lineBelowSpinnerLayourParams);

                /*TextView textView=new TextView(this);
                textView.setText("h");
                textView.setBackgroundColor(Color.parseColor("#000000"));
                textView.setLayoutParams(lineBelowSpinnerLayourParams);
                lineBelowSpinnerLayour.addView(textView);*/
                parent.addView(lineBelowSpinnerLayour);
            } else if (value.equalsIgnoreCase("Date")) {
                titleWrapper.setHint(key);
                parent.addView(titleWrapper);
                ed.setSingleLine(true);
                ed.setTextColor(Color.parseColor("#000000"));
                //    ed.setTextSize(14);
                //  ed.setLayoutParams(params);
                ed.setTextColor(Color.parseColor("#000000"));
                ed.setFocusable(false);
                ed.setKeyListener(null);
                ed.setTag("OpenDate");
                if (flag) {
                    try {
                        String edittextValue = jsonObjectDynamivControlValue.getString(key);
                        String datetime;
                        Date dateInput = null;
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss a", Locale.ENGLISH);
                        try {
                            dateInput = format.parse(edittextValue);
                        } catch (ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a", Locale.ENGLISH);
                        Date date = new Date();
                        if (dateInput != null) {
                            datetime = dateformat.format(dateInput);
                        } else {
                            datetime = dateformat.format(date);
                        }
                        ed.setText(datetime);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                titleWrapper.addView(ed);

            } else if (value.equalsIgnoreCase("Checkbox")) {
                LinearLayout child2 = new LinearLayout(context);
                LinearLayout.LayoutParams paramsChild2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsChild2.gravity = Gravity.CENTER_VERTICAL;
                child2.setOrientation(LinearLayout.VERTICAL);

                LinearLayout child3 = new LinearLayout(context);
                LinearLayout.LayoutParams paramsChild3 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsChild3.gravity = Gravity.CENTER_VERTICAL;
                child3.setOrientation(LinearLayout.HORIZONTAL);


                LinearLayout child4 = new LinearLayout(context);
                LinearLayout.LayoutParams paramsChild4 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                paramsChild4.gravity = Gravity.CENTER_VERTICAL;
                child4.setOrientation(LinearLayout.HORIZONTAL);


                TextView tv = new TextView(context);
                // tv.setTextSize(14);
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setText(key);
//                tv.setAllCaps(true);
                String[] ckValue = dynamicArrayList.get(i).getDynmicSpinerValue().split("\\*\\#");
                child3.addView(tv);
                child2.addView(child3);
               /* LinearLayout  cllayout = new LinearLayout(context);
                LinearLayout.LayoutParams cllayoutparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                //params.setMargins(left, top, right, bottom);
                cllayout.setOrientation(LinearLayout.HORIZONTAL);
                TextView tv=new TextView(context);
                tv.setTextSize(14);
                tv.setTextColor(Color.parseColor("#000000"));
                tv.setText(key);
                tv.setAllCaps(true);
                tv.setLayoutParams(cllayoutparams);
                checkBox.setLayoutParams(cllayoutparams);*/
                if (ckValue != null) {
                    for (int n = 0; n < ckValue.length; n++) {
                        AppCompatCheckBox checkBox = new AppCompatCheckBox(context);
                        checkBox.setText(ckValue[n]);

                        child4.addView(checkBox);
                        //  checkBox.setTextSize(14);

                        if (flag) {
                            boolean checkboxCorrect = false;
                            try {
                                String updateCkValue = jsonObjectDynamivControlValue.getString(key);
                                String[] updateCkArray = updateCkValue.split("\\-");
                                if (updateCkArray.length > 0) {
                                    for (int b = 0; b < updateCkArray.length; b++) {
                                        if (updateCkArray[b].equalsIgnoreCase(ckValue[n])) {
                                            checkboxCorrect = true;

                                        }
                                    }
                                    if (checkboxCorrect) {
                                        checkBox.setChecked(true);
                                    } else {
                                        checkBox.setChecked(false);
                                    }
                                } else {
                                    checkBox.setChecked(false);
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }


                }
                child2.addView(child4);

                // cllayout.addView(tv);
                // cllayout.addView(checkBox);
                parent.addView(child2);
            } else if (value.equalsIgnoreCase("Sql View")) {
                // params.setMargins(10,20,0,0);
                // parent.setLayoutParams(params);
                String[] spValue = dynamicArrayList.get(i).getDynmicSpinerValue().split("\\*\\#");
                ArrayList<String> arrayValue = new ArrayList<String>(Arrays.asList(spValue));
                TextView tv = new TextView(context);
                tv.setText(key);
                tv.setTextColor(getResources().getColor(R.color.colorTextHeader));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//                tv.setAllCaps(true);
                tv.setPadding(0, 0, 0, 0);
                parent.addView(tv);
                spinner.setPrompt("Select " + key);
                setSpinner(spinner, arrayValue);
                if (flag) {
                    try {
                        for (int n = 0; n < arrayValue.size(); n++) {
                            if (arrayValue.get(n).equalsIgnoreCase(jsonObjectDynamivControlValue.getString(key))) {
                                spinner.setSelection(n);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                parent.addView(spinner);
                LinearLayout lineBelowSpinnerLayour = new LinearLayout(context);
                LinearLayout.LayoutParams lineBelowSpinnerLayourParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 2);
                lineBelowSpinnerLayour.setOrientation(LinearLayout.VERTICAL);
                lineBelowSpinnerLayourParams.setMargins(5, 0, 8, 0);
                lineBelowSpinnerLayour.setBackgroundColor(getResources().getColor(R.color.colorUnderline));
                lineBelowSpinnerLayour.setLayoutParams(lineBelowSpinnerLayourParams);
              /*  TextView textView=new TextView(this);
                textView.setText("h");
                textView.setBackgroundColor(Color.parseColor("#000000"));
                textView.setLayoutParams(lineBelowSpinnerLayourParams);
                lineBelowSpinnerLayour.addView(textView);*/
                parent.addView(lineBelowSpinnerLayour);
            } else if (value.equalsIgnoreCase("Sql Procedure")) {
                // params.setMargins(10,20,0,0);
                //  parent.setLayoutParams(params);
                String[] spValue = dynamicArrayList.get(i).getDynmicSpinerValue().split("\\*\\#");
                ArrayList<String> arrayValue = new ArrayList<String>(Arrays.asList(spValue));
                TextView tv = new TextView(context);
                tv.setTextColor(getResources().getColor(R.color.colorTextHeader));
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                tv.setText(key);
//                tv.setAllCaps(true);
                tv.setPadding(0, 0, 0, 0);
                parent.addView(tv);
                spinner.setPrompt("Select " + key);
                setSpinner(spinner, arrayValue);
                if (flag) {
                    try {
                        for (int n = 0; n < arrayValue.size(); n++) {
                            if (arrayValue.get(n).equalsIgnoreCase(jsonObjectDynamivControlValue.getString(key))) {
                                spinner.setSelection(n);
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                parent.addView(spinner);
                LinearLayout lineBelowSpinnerLayour = new LinearLayout(context);
                LinearLayout.LayoutParams lineBelowSpinnerLayourParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 2);
                lineBelowSpinnerLayour.setOrientation(LinearLayout.VERTICAL);
                lineBelowSpinnerLayourParams.setMargins(5, 0, 8, 0);
                lineBelowSpinnerLayour.setBackgroundColor(getResources().getColor(R.color.colorUnderline));
                lineBelowSpinnerLayour.setLayoutParams(lineBelowSpinnerLayourParams);

               /* TextView textView=new TextView(this);
                textView.setText("h");
                textView.setBackgroundColor(Color.parseColor("#000000"));
                textView.setLayoutParams(lineBelowSpinnerLayourParams);
                lineBelowSpinnerLayour.addView(textView);*/
                parent.addView(lineBelowSpinnerLayour);
            }
            /*checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                    // TODO Auto-generated method stub
                    if (checkBox.isChecked()) {

                    }
                    else{

                    }
                }
            });*/
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override

                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    System.out.println("Data" + arg0 + "-" + arg1 + "-" + arg2 + "-" + arg3 + "\n" + arg1.getId());
                    //  Toast.makeText(AddContactModel.this,"Click",Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {

                }
            });
            LinearLayoutArrayListForDynamicControllerView.add(parent);
            linearLayoutdynamicAllBelowfields.addView(parent);
            ed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TextInputLayout inputLayout=(TextInputLayout)linearLayout.getChildAt(0);
                    final EditText editText = (EditText) v;
                    if (editText.getTag() != null) {
                        if (editText.getTag().equals("OpenDate")) {
                            int mYear = mcurrentDate.get(Calendar.YEAR);
                            int mMonth = mcurrentDate.get(Calendar.MONTH);
                            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

                            DatePickerDialog mDatePicker = new DatePickerDialog(AddContact.this, new DatePickerDialog.OnDateSetListener() {
                                public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                                    // TODO Auto-generated method stub
                                    mcurrentDate.set(Calendar.YEAR, selectedyear);
                                    mcurrentDate.set(Calendar.MONTH, selectedmonth);
                                    mcurrentDate.set(Calendar.DAY_OF_MONTH, selectedday);
                                    String myFormat = "dd/MMM/yyyy"; //In which you need put here
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss a", Locale.ENGLISH);
                                    editText.setText(sdf.format(mcurrentDate.getTime()));
                                }
                            }, mYear, mMonth, mDay);
                            mDatePicker.setTitle("Select date");
                            mDatePicker.show();
                        }
                    }
                }
            });
        }

    }

    boolean isDynamicControlledCalled = false;

    public String dynamicControllerData() {

        String result = "";

        String url = "http://" + server + "/And_Sync.asmx/xjsGetcustomfields";
        JSONParser jParser = new JSONParser();
        result = jParser.getJSONArray(url);
        return result;
    }

    void gettingValuesFromXml() {
        linearLayoutdynamicfieldForPersonInfo = (LinearLayout) findViewById(R.id.dynamicfieldsPersonInfo);
        textAddCompayDetails = (TextView) findViewById(R.id.aacAddCompayDetails);
        linearLayoutAddCompanyDetails = (LinearLayout) findViewById(R.id.linearLayoutAddCompanyDetails);
        linearLayoutdynamicAllBelowfields = (LinearLayout) findViewById(R.id.dynamicAllBelowfields);
        linearlayoutdynamicfieldsURL = (LinearLayout) findViewById(R.id.dynamicfieldsURL);
        //Footer Ids
        findButton = (ImageView) findViewById(R.id.findbutton1);
        save = (ImageView) findViewById(R.id.button1);
        cancel = (ImageView) findViewById(R.id.button2);
        delete = (ImageView) findViewById(R.id.button3);
        takePicture = (ImageView) findViewById(R.id.takePicture);
        userChoosenTask = "Take Photo";
//        takePicture.setBackgroundResource(R.drawable.camera);
        SharedPreferences dpreferences_new = getSharedPreferences("DYNAMIC_FORM_FIELDS_INFO", MODE_PRIVATE);
//       String image_data = dpreferences_new.getString("Show_UseCamera",null);
//       if(!image_data.equals("Y")){
//           //capturegallery();
//           userChoosenTask = "Choose from Library";
//           takePicture.setBackgroundResource(R.drawable.galaryimage);
//
//       }
//       else {
//           userChoosenTask = "Take Photo";
//           takePicture.setBackgroundResource(R.drawable.camera);
//       }
        //ImageButton
        capturedImage = (ImageView) findViewById(R.id.imageView1);
        imgAddMorePersonInfo = (ImageView) findViewById(R.id.addPhone);
        imgAddMoreURL = (ImageView) findViewById(R.id.addfielsURL);
        //main page
        editTextCompany = (EditText) findViewById(R.id.aacCompany);
        //editTextCompany=(AutoCompleteTextView)findViewById(R.id.aacCompany);
        spinnerCompanyCity = (Spinner) findViewById(R.id.aacspineerCompanyCity);
        spinnerCity = (Spinner) findViewById(R.id.aacspineerCity);
        editTextAddCompanyDetailsDiscription = (EditText) findViewById(R.id.aacDescription);
        editTextAddCompanyDetailsZip = (EditText) findViewById(R.id.aacCompanyZip);
        editTextAddCompanyDetailsAddress = (EditText) findViewById(R.id.aacCompanyAddress);
        editTextFirstName = (EditText) findViewById(R.id.aacFirstName);
        editTextLastName = (EditText) findViewById(R.id.aacLastName);
        editTextPersonName = (EditText) findViewById(R.id.aacPersonName);
        editTextMainMobNo = (EditText) findViewById(R.id.aacmbno);
        editTextMainEmail = (EditText) findViewById(R.id.aacPersonEmail);
        editTextMainUrl = (EditText) findViewById(R.id.aacURL);
        editTextZip = (EditText) findViewById(R.id.aacZip);
        editTextAddress = (EditText) findViewById(R.id.aacAddress);
        editTextBackground = (EditText) findViewById(R.id.aacBackground);

        spinnerJobTitle = (Spinner) findViewById(R.id.adcspinnerJobTitle);
        spinnerState = (Spinner) findViewById(R.id.aacspineerState);
        spinnerAddCompanyDetailsState = (Spinner) findViewById(R.id.aacspineerCompanyState);

        spinnerAddCompanyDetailsCountry = (Spinner) findViewById(R.id.aacspineerCompanyCountry);
//        spinnerCountry = (Spinner) findViewById(R.id.aacspineerCountry);
//        spinnerCountry.setEnabled(false);
        spinnerAddCompanyDetailsCountry.setFocusableInTouchMode(false);
        spinnerAddCompanyDetailsCountry.setEnabled(false);
      //  spinnerCountry.setFocusableInTouchMode(false);

        spinnerStatue = (Spinner) findViewById(R.id.aacspineerStatus);
        spinnerTag = (Spinner) findViewById(R.id.aacspineerTag);
        spinnerLeadSource = (Spinner) findViewById(R.id.aacspineerLeadSource);
        spinnerOwner = (Spinner) findViewById(R.id.aacspineerOwner);
        spinnerProduct = (Spinner) findViewById(R.id.aacspineerProduct);
        spinnerProductGroup = (Spinner) findViewById(R.id.aacspineerProductGroup);
        checkboxActive = (CheckBox) findViewById(R.id.aacActive);
        checkboxSameAsCompnay = (CheckBox) findViewById(R.id.accSameAsCompany);


    }

    public boolean setStateContarySpinner(Spinner spinner, ArrayList<Owner> arrayList) {
        CustomAdapterCRMStreamInfo adapter = new CustomAdapterCRMStreamInfo(AddContact.this, arrayList, R.layout.spinner_adapter_view);
        spinner.setAdapter(adapter);
        return true;
    }

    public void setSpinner(Spinner spinner, ArrayList arrayList) {
        //Collections.sort(arrayList);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.adapterdropdown, arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0);
    }

    class getAllContacyData extends AsyncTask<String, String, String> {
        String serverResponse = "";

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                serverResponse = getContactData();
            } catch (Exception e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                e.printStackTrace();
            }
            return serverResponse;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (serverResponse != null || !serverResponse.equalsIgnoreCase("")) {
                try {
                    JSONArray jsonarray = new JSONArray(result);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        AddContactModel cdata = new AddContactModel();
                        cdata.setCompanyCity(obj.getString("compcity"));
                        cdata.setCompanyState(obj.getString("compstate"));
                        cdata.setCompanyZip(obj.getString("compzip"));
                        cdata.setCompanyContry(obj.getString("compcountry"));
                        cdata.setCompanyDisscussion(obj.getString("compdesc"));
                        cdata.setCompanyPhone(obj.getString("compphone"));
                        cdata.setCompanyAddress(obj.getString("compadd"));
                        if (obj.has("imageURL")) {
                            cdata.setImageUrl(obj.getString("imageURL"));
                        } else {
                            cdata.setImageUrl("");
                        }
                        if (obj.has("compname"))
                            cdata.setComapy(obj.getString("compname"));
                        String phone = obj.getString("phone");
                        JSONArray jsonAryPhone = new JSONArray(phone);
                        cdata.setJsonPhone(jsonAryPhone);

                        String email = obj.getString("email");
                        JSONArray jsonAryEmail = new JSONArray(email);
                        cdata.setJsonEmail(jsonAryEmail);

                        String url = obj.getString("URl");
                        JSONArray jsonAryURL = new JSONArray(url);
                        cdata.setJsonURL(jsonAryURL);
                        boolean active = false;
                        active = obj.has("Active");
                        if (active) {
                            cdata.setActive(obj.getString("Active").equalsIgnoreCase("Y") ? true : false);
                        } else {
                            cdata.setActive(active);
                        }
                        cdata.setFirstName(obj.getString("Fname"));
                        cdata.setLastName(obj.getString("Lname"));
                        cdata.setJobTitle(obj.getString("JobTitle"));
                        cdata.setAddress(obj.getString("Address"));
                        cdata.setCity(obj.getString("City"));
                        cdata.setState(obj.getString("State"));
                        cdata.setZip(obj.getString("Zip"));
                        cdata.setContry(obj.getString("country"));
                        cdata.setStatus(obj.getString("status"));
                        cdata.setLeadStatus(obj.getString("leadsource"));
                        cdata.setTag(obj.getString("tag"));
                        cdata.setOwner(obj.getString("owner"));
                        cdata.setBackground(obj.getString("Background"));
                        String dynamicControls = obj.getString("CustomFields");
                        if (obj.has("productgrp")) {
                            cdata.setProducGroup(obj.getString("productgrp"));
                        } else {
                            cdata.setProducGroup("0");
                        }
                        if (obj.has("product")) {
                            cdata.setItemName(obj.getString("product"));
                        } else {
                            cdata.setItemName("0");
                        }
                        JSONObject jsonObjDynamicControl = new JSONObject(dynamicControls);
                        cdata.setDynamicControlData(jsonObjDynamicControl);
                        allDataArray.add(cdata);
                    }
                } catch (Exception e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    e.printStackTrace();
                }
            }
            new statusTagLeadSourceOwnerData().execute("Status");
        }
    }

    void dismissDiloge() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public String getContactData() {
        String url = "http://" + server + "/And_Sync.asmx/XjsGetContactDetail_CRM?ContactId=" + ContactID;
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        if (result == null)
            result = "";
        return result;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AddContact.this, DashBoradActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
