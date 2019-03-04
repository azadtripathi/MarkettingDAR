package com.dm.crmdm_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.controller.AppDataController;
import com.dm.library.ConnectionDetector;
import com.dm.library.Custom_Toast;
import com.dm.library.GridViewAdapter;
import com.dm.library.IntentSend;
import com.dm.model.AppData;
import com.dm.model.GridItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class GridViewActivity extends AppCompatActivity {
    private static final String TAG = GridViewActivity.class.getSimpleName();
    private GridView mGridView;
    private ProgressBar mProgressBar;
    private GridViewAdapter mGridAdapter;
    private ArrayList<GridItem> mGridData;
    private ConnectionDetector detector;
    SharedPreferences preferences2;
    String SmId,UserId,server,image,flag,storeDir;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    Activity activity;
    mconnReciver Reciver;
    private String FEED_URL /*= "http://javatechig.com/?json=get_recent_posts&count=45" */;

    String downloadFileName = "";
    @SuppressLint("LongLogTag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);

        getSupportActionBar().setCustomView(R.layout.actionbar_custom_view_home);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageView iv = (ImageView)findViewById(R.id.image);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new IntentSend(getApplicationContext(),DashBoradActivity.class)).toSendAcivity();
                finish();
            }
        });

        TextView tv = (TextView)findViewById(R.id.text);
        if(!(DashBoradActivity.tradeORpendingTittle)){
            tv.setText("Collateral");
            setTitle("Collateral");
        }else{
            tv.setText("Collateral");
            setTitle("Collateral");
        }

        Reciver = new mconnReciver();
        detector = new ConnectionDetector(GridViewActivity.this);
        activity = GridViewActivity.this;
        preferences2 = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SmId = preferences2.getString("CONPERID_SESSION", null);
        UserId = preferences2.getString("USER_ID", null);
        appDataController1=new AppDataController(GridViewActivity.this);
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();
        storeDir= Environment.getExternalStorageDirectory().toString();
        //FEED_URL = "http://javatechig.com/?json=get_recent_posts&count=45";
        FEED_URL = "http://" + server + "/And_Sync.asmx/JSGetDocuments?smid=" + SmId;




        mGridView = (GridView) findViewById(R.id.gridView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        //Initialize with empty data
        mGridData = new ArrayList<>();
        mGridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, mGridData);
        mGridView.setAdapter(mGridAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //Get item at position
                GridItem item = (GridItem) parent.getItemAtPosition(position);

                /*Intent intent = new Intent(GridViewActivity.this, DetailsActivity.class);
                ImageView imageView = (ImageView) v.findViewById(R.id.grid_item_image);
                // Interesting data to pass across are the thumbnail size/location, the
                // resourceId of the source bitmap, the picture description, and the
                // orientation (to avoid returning back to an obsolete configuration if
                // the device rotates again in the meantime)
                int[] screenLocation = new int[2];
                imageView.getLocationOnScreen(screenLocation);

                //Pass the image title and url to DetailsActivity
                intent.putExtra("left", screenLocation[0]).
                        putExtra("top", screenLocation[1]).
                        putExtra("width", imageView.getWidth()).
                        putExtra("height", imageView.getHeight()).
                        putExtra("title", item.getTitle()).
                        putExtra("downloadurl", item.getdownloadUrl()).
                        putExtra("flag", item.getflag());
                startActivity(intent);*/

                downloadFileName = item.getTitle();
                image = item.getdownloadUrl();
                flag = item.getflag();
                new BackTask(activity,image,"file","Download").execute();
            }
        });

        //Start download
        if (detector.isConnectingToInternet())
        {
            new AsyncHttpTask().execute(FEED_URL);
            mProgressBar.setVisibility(View.VISIBLE);
            Log.d("===========================", "Internet Present");
        }
        else
        {
            Log.d("===========================", "No Internet");
            this.registerReceiver(Reciver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }

    }


/****************************************** Sperate code for providing web service for GitHub example*************************/
   /* public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            Integer result = 0;
            try {
                // Create Apache HttpClient
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
                int statusCode = httpResponse.getStatusLine().getStatusCode();
                // 200 represents HTTP OK
                if (statusCode == 200) {
                    String response = streamToString(httpResponse.getEntity().getContent());
                    parseResult(response);
                    result = 1; // Successful
                } else {
                    result = 0; //"Failed
                }
            } catch (Exception e) {
                Log.d(TAG, e.getLocalizedMessage());
            }
            return result;
        }
        @Override
        protected void onPostExecute(Integer result) {
            // Download complete. Let us update UI
            if (result == 1) {
                mGridAdapter.setGridData(mGridData);
            } else {
                Toast.makeText(GridViewActivity.this, "Failed to fetch data!", Toast.LENGTH_SHORT).show();
            }
            mProgressBar.setVisibility(View.GONE);
        }
    }
    String streamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String line;
        String result = "";
        while ((line = bufferedReader.readLine()) != null) {
            result += line;
        }
        // Close stream
        if (null != stream) {
            stream.close();
        }
        return result;
    }
    /**
     * Parsing the feed results and get the list
     * @param result
     */
    /*private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray posts = response.optJSONArray("posts");
            //JSONArray posts = response.optJSONArray(result);
            GridItem item;
            for (int i = 0; i < posts.length(); i++) {
                JSONObject post = posts.optJSONObject(i);
                String title = post.optString("title");
                item = new GridItem();
                item.setTitle(title);
                JSONArray attachments = post.getJSONArray("attachments");
                if (null != attachments && attachments.length() > 0) {
                    JSONObject attachment = attachments.getJSONObject(0);
                    if (attachment != null)
                        item.setImage(attachment.getString("url"));
                }
                mGridData.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
/************************************************************ End **********************************************************/




/****************************************** according to define web service  ***********************************************/
private static void downloadFile(String url, File outputFile) {
    try {
        URL u = new URL(url);
        URLConnection conn = u.openConnection();
        int contentLength = conn.getContentLength();

        DataInputStream stream = new DataInputStream(u.openStream());

        byte[] buffer = new byte[contentLength];
        stream.readFully(buffer);
        stream.close();

        DataOutputStream fos = new DataOutputStream(new FileOutputStream(outputFile));
        fos.write(buffer);
        fos.flush();
        fos.close();
    } catch(FileNotFoundException e) {
        return; // swallow a 404
    } catch (IOException e) {
        return; // swallow a 404
    }
}

    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {
    @Override
    protected Integer doInBackground(String... params) {
        Integer result = 0;
        try {
            // Create Apache HttpClient
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse httpResponse = httpclient.execute(new HttpGet(params[0]));
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            // 200 represents HTTP OK
            if (statusCode == 200) {
                URL url = new URL(params[0]);
                URLConnection urlConn = url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                JSONArray response = new JSONArray(stringBuffer.toString());
                for (int i = 0; i < response.length(); i++) {
                    JSONObject objs = null;
                    try {
                        objs = response.getJSONObject(i);
                        String id = objs.optString("Id");
                        String title = objs.optString("Title");
                        String downloadUrl = objs.optString("Url");
                        String flag = objs.optString("Flag");
                        String imageUrl = objs.optString("IconUrl");
                        GridItem item = new GridItem();
                        item.setid(id);
                        item.setTitle(title);
                        item.setImage(imageUrl);
                        item.setdownloadUrl(downloadUrl);
                        item.setflag(flag);
                        mGridData.add(item);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    result = 1; // Successful
                }
            } else {
                result = 0; //"Failed
            }
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage());
        }
        return result;
    }

    @Override
    protected void onPostExecute(Integer result) {
        // Download complete. Let us update UI
        if (result == 1) {
            mGridAdapter.setGridData(mGridData);
        } else {
            new Custom_Toast(GridViewActivity.this,"No data available",Toast.LENGTH_SHORT).showCustomAlert();
        }
        mProgressBar.setVisibility(View.GONE);
    }
   }
/*********************************************************** End **************************************************************/



    @SuppressLint("LongLogTag")
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.d("===========================", "onResume");
        IntentFilter intentFilter = new IntentFilter("com.dm.crmdm_app.GridViewActivity");
        registerReceiver(Reciver, intentFilter);

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onPause()
    {
        super.onPause();
        Log.d("===========================", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
      unregisterReceiver(Reciver);
    }

    public class mconnReciver extends BroadcastReceiver
   // Class BroadcastReceiver mConnReceiver = new BroadcastReceiver()
    {
        @SuppressLint("LongLogTag")
        public void onReceive(Context context, Intent intent)
        {
            boolean noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
            String reason = intent.getStringExtra(ConnectivityManager.EXTRA_REASON);
            boolean isFailover = intent.getBooleanExtra(ConnectivityManager.EXTRA_IS_FAILOVER, false);

            NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            NetworkInfo otherNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);

            if (currentNetworkInfo.isConnected())
            {
                Log.d("===========================", "Connected");
                finish();
                startActivity(getIntent());
                Toast.makeText(getApplicationContext(), "Connected Internet",Toast.LENGTH_LONG).show();
            }
            else
            {
                Log.d("===========================", "Not Connected");
                Toast.makeText(getApplicationContext(), "Not Connected Internet", Toast.LENGTH_LONG).show();
            }
        }
    };
 /**************************************************************** Download File or Image ******************************************/

 private class BackTask extends AsyncTask<String,Integer,Void> {
     private String webservice;
     private String File;
     NotificationManager mNotifyManager;

     NotificationCompat.Builder mBuilder;
     private Context mContext;
     boolean isException = false;
     ProgressDialog mProgressDialog;
     File fullFilePath;
     public BackTask(Context context,String url, String file,String dialogMessage) {
         int loopvalue = 0;
         this.webservice = url;
         this.File = file;
         final BackTask me = this;
         this.mContext = context;

     }
     protected void onPreExecute(){
         super.onPreExecute();
         mNotifyManager =(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
//         mBuilder = new NotificationCompat.Builder(mContext);
//         mBuilder.setContentTitle(downloadFileName+" Download").setContentText("Download in progress").setSmallIcon(R.mipmap.ic_action_bar);
         // Toast.makeText(mContext, "Downloading the file... The download progress is on notification bar.", Toast.LENGTH_LONG).show();

         mProgressDialog = new ProgressDialog(GridViewActivity.this);
         mProgressDialog.setMessage("Downloading file..");
         mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
         mProgressDialog.setCancelable(false);
         mProgressDialog.show();

     }

     protected Void doInBackground(String...params){
         URL url;
         int count;
         webservice = webservice.replace(" ","%20");
         try {
             url = new URL(webservice);

             String sdCard = Environment.getExternalStorageDirectory().toString();
             java.io.File myDir = new File(sdCard, getResources().getString(R.string.app_name));
             final File myDir1 = new File(sdCard + "/"+getResources().getString(R.string.app_name), "Collateral");

            /* if specified not exist create new */
             if (!myDir.exists()) {
                 myDir.mkdir();
                 Log.v("", "inside mkdir");
             }
             if (!myDir1.exists()) {
                 myDir1.mkdir();
                 Log.v("", "inside mkdir");
             }

                /* checks the file and if it already exist delete */
                /*String fname = imageName;
                //File file = new File(myDir, fname);
                Log.d("file===========path", "" + file);
                if (file.exists())
                    file.delete();*/

            /* Open a connection */
             String pathl="";
             try {
                 java.io.File f=new File(storeDir);
                 if(f.exists()){
                     HttpURLConnection con=(HttpURLConnection)url.openConnection();
                     InputStream is=con.getInputStream();
                     String pathr=url.getPath();
                     final String filename=pathr.substring(pathr.lastIndexOf('/')+1);

                     // pathl=storeDir+"/"+filename;
                    // File file = new File(myDir1,System.currentTimeMillis()+filename);
                     File file = new File(myDir1,filename);
                    if(!file.exists()){
                         FileOutputStream fos=new FileOutputStream(file);
                         int lenghtOfFile = con.getContentLength();
                         byte data[] = new byte[1024];
                         long total = 0;
                         while ((count = is.read(data)) != -1) {
                             total += count;
                             publishProgress((int)((total*100)/lenghtOfFile));
                             fos.write(data, 0, count);
                         }

                         is.close();
                         fos.flush();
                         fos.close();
                    }
                    else{
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GridViewActivity.this, "File Already Exist.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                     //FileOutputStream fos=new FileOutputStream(pathl);
                     final String wholename = myDir1+"/"+filename;
                     final File fileToRead = new File(wholename);
                     fullFilePath=fileToRead;
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {

                             AlertDialog.Builder builder = new AlertDialog.Builder(GridViewActivity.this);
                             builder.setMessage("Do you want to Open the Downloaded File?")
                                     .setCancelable(false)
                                     .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                         public void onClick(DialogInterface dialog, int aid) {
                                             dialog.cancel();
                                             try {

                                                 MimeTypeMap myMime = MimeTypeMap.getSingleton();
                                                 Intent newIntent = new Intent(Intent.ACTION_VIEW);
                                                 String mimeType = myMime.getMimeTypeFromExtension(flag.substring(1));
                                                 newIntent.setDataAndType(Uri.fromFile(fileToRead),mimeType);
                                                 newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                 startActivity(newIntent);
                                                 isException = false;
                                             } catch (ActivityNotFoundException e) {
                                                 runOnUiThread(new Runnable() {
                                                     @Override
                                                     public void run() {
                                                         Toast.makeText(GridViewActivity.this, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                                                     }
                                                 });
                                                 //
                                             }
                                         }
                                     })
                                     .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                         public void onClick(DialogInterface dialog, int id) {
                                             dialog.cancel();
                                         }
                                     });
                             AlertDialog alert = builder.create();
                             alert.show();


                         }
                     });

                 }
                 else{
                     Log.e("Error","Not found: "+storeDir);

                 }

             } catch (Exception e) {
                 e.printStackTrace();
                 isException = true;

             }

         } catch (MalformedURLException e) {
             // TODO Auto-generated catch block
             e.printStackTrace();
         }

         return null;

     }

     protected void onProgressUpdate(Integer... progress) {

        /* mBuilder.setProgress(100, progress[0], false);
         // Displays the progress bar on notification
         mNotifyManager.notify(0, mBuilder.build());*/

         mProgressDialog.setProgress((progress[0]));
     }

     protected void onPostExecute(Void result){
        if(mProgressDialog.getProgress()==100)
        {
         /*   Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.crm_dm1);

            mProgressDialog.setMessage("File download completed successfully");
            mProgressDialog.dismiss();
            mBuilder = new NotificationCompat.Builder(mContext);
            mBuilder.setContentTitle(getString(R.string.app_name)).setContentText("File download completed successfully").
                    setSmallIcon(R.drawable.crmdm_action).setLargeIcon(largeIcon);
           // PendingIntent contentIntent = PendingIntent.getActivity(this, 0,intent, PendingIntent.FLAG_CANCEL_CURRENT);
            mNotifyManager.notify(1, mBuilder.build());*/
            try{
            Intent intent =  new Intent(Intent.ACTION_VIEW);
            MimeTypeMap myMime = MimeTypeMap.getSingleton();
            String mimeType = myMime.getMimeTypeFromExtension(flag.substring(1));
            intent.setDataAndType(Uri.fromFile(fullFilePath),mimeType);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            android.support.v4.app.NotificationCompat.Builder notificationBuilder  = new android.support.v4.app.NotificationCompat.Builder(mContext);
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.crm_dm1);
            notificationBuilder.setContentTitle(getString(R.string.app_name));
            notificationBuilder.setContentText("File download completed successfully");
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                notificationBuilder.setSmallIcon(R.drawable.crmdm_action);
            } else {
                notificationBuilder.setSmallIcon(R.drawable.crmdm_action);
            }
            notificationBuilder.setLargeIcon(largeIcon);
            notificationBuilder.setContentIntent(pIntent);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setOngoing(true);
            notificationBuilder.build();
            NotificationManager notificationManager =  (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(R.string.app_name, notificationBuilder.build());
            }
            catch (Exception e){}

        }

         if(isException)
         {
             mProgressDialog.dismiss();
             mBuilder = new NotificationCompat.Builder(mContext);
             Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.crm_dm1);
             mBuilder.setContentTitle(getString(R.string.app_name)).setContentText("Error: while downloading file").setSmallIcon(R.drawable.crmdm_action).setLargeIcon(largeIcon);

             mNotifyManager.notify(1, mBuilder.build());
         }
         else {
             mProgressDialog.dismiss();

         }
         // Removes the progress bar

     }
 }
 /************************************************************** End **********************************************************/
}
