package com.dm.crmdm_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.library.Custom_Toast;
import com.dm.library.FilePath;
import com.dm.library.IntentSend;
import com.dm.model.AppData;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by dataman on 12/11/2017.
 */

public class Attendence extends ActionBarActivity {
    String latitude="",longitude="";
    String latlngtime;
    ImageView imageCapture;
    Button buttonCheckIn,buttonCheckOut;
    private int REQUEST_CAMERA = 0;
    SharedPreferences preferences;
    String SMID;
    SharedPreferences preferences2;
    String selectedImageBase64ImgString="N/A";
    int RequetCode;
    ArrayList<AppData> appDataArray;
    String server;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendene);
        getSupportActionBar().setCustomView(R.layout.actionbar_custom_view_home);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        preferences = getSharedPreferences("UserAttendance", MODE_PRIVATE);
        preferences2=this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SMID=preferences2.getString("CONPERID_SESSION", null);
        ImageView iv = (ImageView)findViewById(R.id.image);
        iv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getApplicationContext().getSharedPreferences("UserAttendance", getApplicationContext().MODE_PRIVATE);
              settings.edit().clear().commit();
                (new IntentSend(getApplicationContext(),DashBoradActivity.class)).toSendAcivity();
                finish();
            }
        });
        TextView tv = (TextView) findViewById(R.id.text);
        tv.setText("Attendence");
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
        imageCapture=(ImageView)findViewById(R.id.imageView);
        buttonCheckIn=(Button)findViewById(R.id.buttonCheckIn);
        buttonCheckOut=(Button)findViewById(R.id.buttonCheckOut);
        AppData appData;
        AppDataController appDataController1=new AppDataController(Attendence.this);
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();
        buttonCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequetCode=0;
                if(preferences.getBoolean("Attendance",false) && preferences.getBoolean("isCheckedIn",false)){
                    new svaeDataOnServer().execute("CheckIn");
                }
                else{
                    cameraIntent();
                }
            }
        });
        buttonCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequetCode=1;
                if(preferences.getBoolean("Attendance",false) && preferences.getBoolean("isCheckedOut",false)){
                    new svaeDataOnServer().execute("CheckOut");
                }
                else{
                    cameraIntent();
                }
            }
        });
        imageCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(preferences.getInt("ResponseCode",2) ==0){
                    RequetCode=0;
                    cameraIntent();
                }
                else {
                    RequetCode=1;
                    cameraIntent();
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == REQUEST_CAMERA) {
                    if (RequetCode == 0) {
                        onCaptureImageResult(data);
                    } else {
                        onCaptureImageResult(data);
                    }
                /*                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageCapture.setImageBitmap(photo);
                Uri tempUri = getImageUri(getApplicationContext(), photo);

                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File finalFile = new File(getRealPathFromURI(tempUri));
                String imageUrl= FilePath.getPath(this,tempUri);
                loadImageFromLocalStore(imageUrl);*/

                }

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
   /* public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }*/
    private void onCaptureImageResult(Intent data) {


        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
        int maxHeight = 500;
        int maxWidth = 500;
        float scale = Math.min(((float)maxHeight / selectedImage.getWidth()), ((float)maxWidth / selectedImage.getHeight()));
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        selectedImage = Bitmap.createBitmap(selectedImage, 0, 0, selectedImage.getWidth(), selectedImage.getHeight(), matrix, true);

        int bitmapByteCount= BitmapCompat.getAllocationByteCount(selectedImage);
        imageCapture.setImageBitmap(selectedImage);

        selectedImageBase64ImgString = encodeImage(selectedImage);


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        selectedImageBase64ImgString= Base64.encodeToString(b, Base64.DEFAULT);
        if(RequetCode ==0) {
            if (!selectedImageBase64ImgString.isEmpty() || selectedImageBase64ImgString != null || selectedImageBase64ImgString.length() > 5) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("ResponseCode", 0);
                editor.putBoolean("Attendance", true);
                editor.putBoolean("isCheckedIn", true);
                editor.putBoolean("isCheckedOut", false);
                editor.putString("userImage", selectedImageBase64ImgString);
                editor.commit();
            }
            else{
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("ResponseCode", 2);
                editor.putBoolean("Attendance", false);
                editor.putBoolean("isCheckedIn", false);
                editor.putBoolean("isCheckedOut", false);
                editor.putString("userImage", "N/A");
                editor.commit();
                new Custom_Toast(Attendence.this,"Please Capture Image").showCustomAlert();
            }
        }
        else{
            if (!selectedImageBase64ImgString.isEmpty() || selectedImageBase64ImgString != null || selectedImageBase64ImgString.length() > 5) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("ResponseCode", 1);
                editor.putBoolean("Attendance", true);
                editor.putBoolean("isCheckedIn", true);
                editor.putBoolean("isCheckedOut", true);
                editor.putString("userImage", selectedImageBase64ImgString);
                editor.commit();
            }
            else{
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("ResponseCode", 2);
                editor.putBoolean("Attendance", false);
                editor.putBoolean("isCheckedIn", false);
                editor.putBoolean("isCheckedOut", false);
                editor.putString("userImage", "N/A");
                editor.commit();
                new Custom_Toast(Attendence.this,"Please Capture Image").showCustomAlert();
            }
        }
    }

 /*   private void saveIamgeToLocalStore(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root +"/"+R.string.app_name);
        myDir.mkdirs();
        String fname = "Profile_Image.png";
        File file = new File (myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

  /*  private void loadImageFromLocalStore(String imageURI) {
        try {
            Uri uri = Uri.parse(Environment.getExternalStorageDirectory().toString() + imageURI);
            Bitmap bitmap = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri));
            imageCapture.setImageBitmap(bitmap);
            imageCapture.setTag("Other");
            //select_image_button.setText(R.string.button_remove_profile_picture);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/
  private Bitmap decodeImage(String image){
      Bitmap decodedByte = null;
      try{
          byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
          decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
      }
      catch (Exception e){
          e.printStackTrace();
      }

      return  decodedByte;
  }
    private String encodeImage(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }
    public Camera getCameraInstance() {
        Camera c = null;
        try {
            c = openFrontFacingCamera();
        }
        catch (Exception e){
        }
        return c;
    }
    private Camera openFrontFacingCamera()
    {
        int cameraCount = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras();
        for ( int camIdx = 0; camIdx < cameraCount; camIdx++ ) {
            Camera.getCameraInfo( camIdx, cameraInfo );
            if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT  ) {
                try {
                    cam = Camera.open( camIdx );
                } catch (RuntimeException e) {
                    Log.e("", "Camera failed to open: " + e.getLocalizedMessage());
                }
            }
        }

        return cam;
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        startActivityForResult(intent, REQUEST_CAMERA);

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(preferences.getInt("ResponseCode",2) ==0){
            buttonCheckIn.setVisibility(View.VISIBLE);
            buttonCheckOut.setVisibility(View.GONE);
            if(preferences.getBoolean("Attendance",false) && preferences.getBoolean("isCheckedIn",false)){
                try{
                    imageCapture.setImageBitmap(decodeImage(preferences.getString("userImage","")));}
                catch (Exception e){
                    e.printStackTrace();
                }
                buttonCheckIn.setText("Save");
            }
            else{
                buttonCheckIn.setText("Check In");
            }
        }
        else if(preferences.getInt("ResponseCode",2) ==1){
            buttonCheckIn.setVisibility(View.GONE);
            buttonCheckOut.setVisibility(View.VISIBLE);
            if(preferences.getBoolean("Attendance",false) && preferences.getBoolean("isCheckedOut",false)){
                try{
                    imageCapture.setImageBitmap(decodeImage(preferences.getString("userImage","")));}
                catch (Exception e){
                    e.printStackTrace();
                }
                buttonCheckOut.setText("Save");
            }
            else{
                buttonCheckOut.setText("Check Out");
            }
        }
        else{
            buttonCheckIn.setVisibility(View.VISIBLE);
            buttonCheckOut.setVisibility(View.GONE);
            buttonCheckIn.setText("Check In");
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public class svaeDataOnServer extends AsyncTask<String , Void ,String> {
        String server_response;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(Attendence.this,"Saving Data", "Please Wait...", true);
            progressDialog.setCancelable(false);
            params.add(new BasicNameValuePair("smid",SMID));
            params.add(new BasicNameValuePair("image1",selectedImageBase64ImgString));
            Calendar c = Calendar.getInstance();
            int Hr24=c.get(Calendar.HOUR_OF_DAY);
            int Min=c.get(Calendar.MINUTE);
            //params.add(new BasicNameValuePair("time",String.valueOf(Hr24)+":"+String.valueOf(Min)));
            params.add(new BasicNameValuePair("longitude",longitude));
            params.add(new BasicNameValuePair("latitude",latitude));
            params.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));

        }
        protected String doInBackground(String... arg0) {
            try {
                params.add(new BasicNameValuePair("type",arg0[0]));
                URL url = new URL("http://"+server+"/And_Sync.asmx/XjsSaveAttendence_CRM"); // here is your URL path
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
                if(progressDialog != null){
                    progressDialog.dismiss();
                }
                return new String("Exception: " + e.getMessage());
            }

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // if(progressDialog.isShowing()){progressDialog.dismiss();}
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            Log.e("Response", "" + server_response);
            if(preferences.getInt("ResponseCode",2)==0){
                if(server_response != null){
                    if(!server_response.isEmpty()){
                        server_response=server_response.replaceAll("\"","");
                        if(server_response.equalsIgnoreCase("Y")){
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt("ResponseCode", 1);
                            editor.putBoolean("Attendance", true);
                            editor.putBoolean("isCheckedIn", false);
                            editor.putBoolean("isCheckedOut", false);
                            editor.putString("userImage", "N/A");
                            editor.commit();

                            buttonCheckIn.setVisibility(View.GONE);
                            buttonCheckOut.setVisibility(View.VISIBLE);
                            if(preferences.getBoolean("Attendance",false) && preferences.getBoolean("isCheckedOut",false)){
                                try{
                                    imageCapture.setImageBitmap(decodeImage(preferences.getString("userImage","")));}
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                buttonCheckOut.setText("Save");
                            }
                            else{
                                buttonCheckOut.setText("Check Out");
                            }
                            new Custom_Toast(Attendence.this,"Save successfully").showCustomAlert();
                            finish();
                            Intent intent=new Intent(Attendence.this,Attendence.class);
                            startActivity(intent);
                        }else
                        {
                            new Custom_Toast(Attendence.this,"Try Again!"+server_response).showCustomAlert();
                        }
                    }
                }
                else{
                    SharedPreferences.Editor editor = preferences.edit();
                    buttonCheckIn.setVisibility(View.GONE);
                    buttonCheckOut.setVisibility(View.VISIBLE);
                    imageCapture.setImageResource(R.drawable.ic_user);
                    buttonCheckOut.setText("Check Out");
                    editor.putInt("ResponseCode", 1);
                    editor.putBoolean("Attendance", true);
                    editor.putBoolean("isCheckedIn", false);
                    editor.putBoolean("isCheckedOut", false);
                    editor.putString("userImage", "N/A");
                    editor.commit();
                }
            }else{
                if(server_response != null){
                    if(!server_response.isEmpty()){
                        server_response=server_response.replaceAll("\"","");
                        if(server_response.equalsIgnoreCase("Y")){
                            SharedPreferences settings = getApplicationContext().getSharedPreferences("UserAttendance", getApplicationContext().MODE_PRIVATE);
                            settings.edit().clear().commit();
                            new Custom_Toast(Attendence.this,"Save successfully").showCustomAlert();
                            finish();
                            Intent intent=new Intent(Attendence.this,Attendence.class);
                            startActivity(intent);
                        }else
                        {
                            new Custom_Toast(Attendence.this,"Try Again!"+server_response).showCustomAlert();
                        }
                    }
                }
                else{
                    /*SharedPreferences.Editor editor = preferences.edit();
                    buttonCheckIn.setVisibility(View.GONE);
                    buttonCheckOut.setVisibility(View.VISIBLE);
                    imageCapture.setImageResource(R.drawable.ic_user);
                    buttonCheckOut.setText("Check Out");
                    editor.putInt("ResponseCode", 2);
                    editor.putBoolean("Attendance", true);
                    editor.putBoolean("isCheckedIn", false);
                    editor.putBoolean("isCheckedOut", false);
                    editor.putString("userImage", "N/A");
                    editor.commit();*/
                    SharedPreferences settings = getApplicationContext().getSharedPreferences("UserAttendance", getApplicationContext().MODE_PRIVATE);
                    settings.edit().clear().commit();
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
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
}
