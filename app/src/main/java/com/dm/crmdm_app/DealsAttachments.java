package com.dm.crmdm_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.library.Custom_Toast;
import com.dm.library.DealsAttachment;
import com.dm.library.DealsAttachmentAdapter;
import com.dm.library.FilePath;
import com.dm.library.IntentSend;
import com.dm.library.MileStoneAdapter;
import com.dm.model.AppData;
import com.dm.model.AttachmentData;
import com.dm.model.MileStoneData;
import com.dm.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class DealsAttachments extends AppCompatActivity {

    ListView listView;
    String dealId;
    String server,TDocID="";
    Button buttonUpload,buttonDelete,buttonSave;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals_attachments);
        listView = (ListView)findViewById(R.id.attachmentList);
        buttonUpload=(Button)findViewById(R.id.asUpload);
        buttonDelete=(Button)findViewById(R.id.asDelete);
        buttonSave=(Button)findViewById(R.id.asSubmit);
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
        tv.setText("Deals Attachment");
        AppData appData;
        ArrayList<AppData> appDataArray = new ArrayList<>();
        AppDataController appDataController = new AppDataController(this);
        appDataController.open();
        appDataArray = appDataController.getAppTypeFromDb();
        appDataController.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();
        Intent intent=getIntent();
        if(intent !=null){
            TDocID=intent.getStringExtra("TDocID");
            new getDealsMilstone().execute();
        }
        buttonUpload.setOnClickListener(new View.OnClickListener() {
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


//                    String[] mimetypes = { "*/*"};
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
                                    "image/*"};

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
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

//                    intent.setType("image/*|application/pdf|audio/*");
//                    intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                }
                startActivityForResult(intent,0);
            }
        });
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fullFilePathList.size()>0)
                {
                    new saveDealFiles().execute();
                }
                else
                {
                    new Custom_Toast(DealsAttachments.this,"No File To Upload").showCustomAlert();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // new Custom_Toast(DealsAttachments.this,"Delete Button Pressed").showCustomAlert();
               if(arrayListDelete.size()>0)
                {
                    //String values=arrayListDelete.toString();
                   // System.out.println("Delete Id:"+values);
                    new deleteDealFiles().execute();
                }
                else
                {
                    new Custom_Toast(DealsAttachments.this,"No File Selected To Delete").showCustomAlert();
                }
            }
        });

    }
    public class deleteDealFiles extends AsyncTask<String , Void ,String> {
        String server_response;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < arrayListDelete.size(); i++) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id",arrayListDelete.get(i));
                    jsonArray.put(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            System.out.println("Send Data:"+jsonArray.toString());
            params.add(new BasicNameValuePair("id",jsonArray.toString()));
        }
        protected String doInBackground(String... arg0) {

            try {

                URL url = new URL("http://"+server+"/And_Sync.asmx/XjsDeleteDealAttachment_CRM"); // here is your URL path
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
                        new Custom_Toast(DealsAttachments.this,"Delete Sussesfully").showCustomAlert();
                        Intent intent=new Intent(DealsAttachments.this, DealsAttachments.class);
                        intent.putExtra("TDocID",TDocID);
                        startActivity(intent);
                        finish();
                    }else
                    {
                        new Custom_Toast(DealsAttachments.this,"Try Again!"+server_response).showCustomAlert();
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
    ProgressDialog progressDialog;
    ArrayList<AttachmentData> attachmentDatas = new ArrayList<>();
    class getDealsMilstone extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(DealsAttachments.this);
            progressDialog.setMessage("Loading Data.Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try
            {

                String url ="http://"+server+"/And_Sync.asmx/XjGetDealAttachment_CRM?TaskDealId="+TDocID;
                JSONParser jParser = new JSONParser();
                String response = jParser.getJSONArray(url);
                JSONArray jsonArray = new JSONArray(response);
                JSONObject json = jsonArray.getJSONObject(0);
                JSONArray ImageData = new JSONArray(json.getString("ImageData"));
                attachmentDatas.clear();
                for(int i=0;i< ImageData.length();i++)
                {
                    JSONObject jsonObject = ImageData.getJSONObject(i);
                    String fileName = jsonObject.getString("filename");
                    String filePath = jsonObject.getString("filepath");
                    String fileId = jsonObject.getString("fileid");
                    String imageIcon = jsonObject.getString("IconUrl");
                    AttachmentData mileStoneData = new AttachmentData();
                    mileStoneData.setFileName(fileName);
                    mileStoneData.setFileUrl(filePath);
                    mileStoneData.setFileId(fileId);
                    mileStoneData.setImageIcon(imageIcon);
                    attachmentDatas.add(mileStoneData);


                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            listView.setAdapter(new DealsAttachmentAdapter(DealsAttachments.this,attachmentDatas,onClick));


        }
    }
    ArrayList<Integer>arrayListDelete =new ArrayList<>();
    View.OnClickListener onClick=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String FileID="";
            CheckBox checkBox=(CheckBox)v;
            try{
                 FileID=checkBox.getTag().toString();
            }catch (Exception e){}
            if(checkBox.isChecked()){
                boolean flag=true;
                for(int i=0;i<arrayListDelete.size();i++){
                    if(arrayListDelete.get(i)== Integer.valueOf(FileID)){
                        flag=false;
                    }
                }
                if(flag){
                    arrayListDelete.add(Integer.valueOf(FileID));}
                buttonDelete.setVisibility(View.VISIBLE);
            }
            else{
               // String FileID=checkBox.getTag().toString();
                for(int i=0;i<arrayListDelete.size();i++){
                    if(arrayListDelete.get(i)== Integer.valueOf(FileID)){
                        arrayListDelete.remove(i);
                    }
                }
                if(arrayListDelete.size()<1){
                    buttonDelete.setVisibility(View.GONE);}
            }
           /* final TextView tv=(TextView)innerview;
            Object data=tv.getTag();
            String [] typeID=data.toString().split("-");*/
        }
    };    ArrayList<String> fullFilePathList = new ArrayList<>();
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            fullFilePathList.clear();
            String fullFilePath = null;
            if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
                if (data == null) {
                    //no data present
                    return;
                }


                ClipData mClipData = data.getClipData();
                if(mClipData !=null) {
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);
                        fullFilePath = FilePath.getPath(this, uri);
                        if (fullFilePath != null && !fullFilePath.equals("")) {
                            fullFilePathList.add(fullFilePath);

                        }
                    }
                }
                else
                {
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Uri mImageUri=data.getData();

                    // Get the cursor
//                    Cursor cursor = getContentResolver().query(mImageUri,
//                            filePathColumn, null, null, null);
//                    // Move to first row
//                    cursor.moveToFirst();
//
//                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//
//
//                    imageEncoded  = cursor.getString(columnIndex);
                    fullFilePath = FilePath.getPath(this, mImageUri);
                    if (fullFilePath != null && !fullFilePath.equals("")) {
                        fullFilePathList.add(fullFilePath);

                    }
                }
                buttonUpload.setText("Upload Attachment ("+fullFilePathList.size()+" files)");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private String doFileUpload(){
        String ServerResponse="";
        MultipartBody.Builder requestBody=null;


        requestBody= new MultipartBody.Builder();
        requestBody.setType(MultipartBody.FORM);
        for(int i=0;i<fullFilePathList.size();i++) {
            requestBody.addFormDataPart("file["+i+"]", new File(fullFilePathList.get(i)).getName(),
                    RequestBody.create(MediaType.parse("*/*"),new File(fullFilePathList.get(i))));
        }
        //String urlString = "http://10.0.2.2/ShiftManger/upload.php";
        requestBody.addFormDataPart("Task_DealID", TDocID);

        String urlString = "http://"+server+"/And_Sync.asmx/XjsSaveDealMultipartUpload_CRM";
        try
        {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(urlString)
                    .post(requestBody.build())
                    .build();

//Check the response
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                ServerResponse=response.body().string();
                System.out.println(response.body().string());
            }
            catch (FileNotFoundException fnf){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        new Custom_Toast(DealsAttachments.this,"Please Select Valid File.File Not Exist").showCustomAlert();
                    }
                });

            }
            catch (IOException e) {
                if(progressDialog != null){
                    progressDialog.dismiss();
                }
                e.printStackTrace();
            }

            return ServerResponse;

        }
        catch (Exception ex){
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            Log.e("Debug", "error: " + ex.getMessage(), ex);
        }
        return ServerResponse;
    }

    public class saveDealFiles extends AsyncTask<String , Void ,String> {
        String server_response;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(DealsAttachments.this, "", "Uploading files to server.....", false);
            String phone,result;
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected String doInBackground(String... arg0)
        {

            try
            {
                server_response= doFileUpload();
            }
            catch(Exception e){
                if(progressDialog != null){
                    progressDialog.dismiss();
                }
                return new String("Exception: " + e.getMessage());
            }
            return server_response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(progressDialog != null){
                progressDialog.dismiss();
            }
            // if(progressDialog.isShowing()){progressDialog.dismiss();}
            Log.e("Response", "" + server_response);
            if(server_response != null){
                if(!server_response.isEmpty()){
                    server_response=server_response.replaceAll("\"","");
                    new Custom_Toast(DealsAttachments.this,"Saved Sussesfully").showCustomAlert();
                    finish();

                }
            }
        }
    }


}
