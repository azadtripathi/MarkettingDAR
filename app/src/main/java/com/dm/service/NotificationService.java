package com.dm.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.dm.controller.AppDataController;
import com.dm.crmdm_app.NotificationPannel;
import com.dm.crmdm_app.R;
import com.dm.database.DatabaseConnection;
import com.dm.library.ConnectionDetector;
import com.dm.library.DateFunction;
import com.dm.library.DbCon;
import com.dm.library.Validation;
import com.dm.model.AppData;
import com.dm.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    String server;AppData appData;
    SharedPreferences preferences1,preferences;
    SharedPreferences dpreferences;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        preferences1 = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        dpreferences=getApplicationContext().getSharedPreferences("DYNAMIC_FORM_FIELDS_INFO",getApplicationContext().MODE_PRIVATE);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try{
            AppDataController appDataController1 = new AppDataController(getApplicationContext());

            appDataController1.open();
            ArrayList<AppData> appDataArray = appDataController1.getAppTypeFromDb();
            appDataController1.close();
            if(appDataArray.size() > 0)
            {
                try
                {
                    appData = appDataArray.get(0);
                    server = appData.getCompanyUrl();
                    preferences = getSharedPreferences("ENVIRO_SESSION_DATA", Context.MODE_PRIVATE);
                    mTimer = new Timer();
                    mTimer.schedule(timerTask, 0, 60 * Long.parseLong(dpreferences.getString("NotificationInterval", "60000")));
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){e.printStackTrace();}

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Start:","Service is Started");
        return START_STICKY;
    }
    @Override
    public void onTaskRemoved(Intent rootIntent)
    {
        final long timeschdule=60*Long.parseLong(dpreferences.getString("NotificationInterval", "60000"));

        Log.i("onTaskRemoved","Service is Removed:"+timeschdule);
        System.out.println(true);
        Intent serviceIntent = new Intent(getApplicationContext(), NotificationService.class);
        getApplicationContext().startService(serviceIntent);

        super.onTaskRemoved(rootIntent);
    }
    private Timer mTimer;

    TimerTask timerTask = new TimerTask() {

        @Override
        public void run() {

            if (new DateFunction().isTimeFrameExist()) {
                Log.e("Log", "Running");
                ConnectionDetector connectionDetector = new ConnectionDetector(getApplicationContext());

                if (connectionDetector.isConnectingToInternet()) {
                  //  sendMobileNotification("AutoSync is running");
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                new getNotification().execute();
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                }
            }
        }

    };


    protected class getNotification extends AsyncTask<String, String, String> {
        String result;

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                String response1 = null;
                String server = appData.getCompanyUrl();
                String url = "http://" + server + "/And_Sync.asmx/xJSGetNotification";
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
                nameValuePairs.add(new BasicNameValuePair("SMID", preferences1.getString("CONPERID_SESSION", "0")));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response1 = httpclient.execute(httppost, responseHandler);
                result = response1.replace("\"", "");

            }  catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (result != null && (!result.equalsIgnoreCase("0"))) {
                Validation validation = new Validation(getApplicationContext());
                sendNotification(validation.vAlfNum(result));

            }
        }
    }
    public boolean getDynamicMenuData() {
        DbCon dbCon=new DbCon(this);
        String urlJsonObj = "http://" + server + "/And_Sync.asmx/xJSGetDynamicMenu?Smid=" + preferences1.getString("CONPERID_SESSION", "0") ;
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(urlJsonObj);
        if (result != null)
        {
            try {
                JSONArray jsonarray = new JSONArray(result);
                dbCon.open();
                dbCon.truncate(DatabaseConnection.TABLE_DYNAMIC_MENU);
                dbCon.close();
                for (int i = 0; i < jsonarray.length(); i++)
                {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    String FormFilter     = obj.getString("Form_Filter");
                    String pageid         = obj.getString("pageid");
                    String viewp          = obj.getString("viewp");
                    String addp           = obj.getString("addp");
                    String editp          = obj.getString("editp");
                    String deletep        = obj.getString("deletep");
                    String pagename       = obj.getString("pagename");
                    String displayname    = obj.getString("displayname");
                    String parentid       = obj.getString("parentid");
                    String level_idx      = obj.getString("level_idx");
                    String idx            = obj.getString("idx");
                    String icon           = obj.getString("Icon");


                    ContentValues values = new ContentValues();
                    values.put(DatabaseConnection.COLUMN_Form_Filter,FormFilter);
                    values.put(DatabaseConnection.COLUMN_PAGE_ID,pageid);
                    values.put(DatabaseConnection.COLUMN_VIEW_P, viewp);
                    values.put(DatabaseConnection.COLUMN_ADD_P, addp);
                    values.put(DatabaseConnection.COLUMN_EDIT_P, editp);
                    values.put(DatabaseConnection.COLUMN_DELETE_P, deletep);
                    values.put(DatabaseConnection.COLUMN_PAGE_NAME, pagename);
                    values.put(DatabaseConnection.COLUMN_DISPLAY_NAME, displayname);
                    values.put(DatabaseConnection.COLUMN_PARENT_ID, parentid);
                    values.put(DatabaseConnection.COLUMN_LEVEL_IDX, level_idx);
                    values.put(DatabaseConnection.COLUMN_IDX, idx);
                    values.put(DatabaseConnection.COLUMN_Icon,icon);

                    dbCon.open();
                    dbCon.insert(DatabaseConnection.TABLE_DYNAMIC_MENU,values);
                    dbCon.close();


                }

            } catch (Exception e) {
                System.out.println(e);


            }


        }
        return true;
    }
    public void sendNotification(String messageBody) {
        Long tsLong = System.currentTimeMillis()/1000;
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_NOTIFICATION,messageBody);
        values.put(DatabaseConnection.COLUMN_DATE,tsLong.toString());
        DbCon dbCon=new DbCon(this);
        dbCon.open();
        dbCon.truncate(DatabaseConnection.NOTIFIVATION_TABLE);
        dbCon.insert(DatabaseConnection.NOTIFIVATION_TABLE,values);
        dbCon.close();
        //End

        Intent intent = new Intent(this, NotificationPannel.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.crm_dm1);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.crm_dm_small)
                .setLargeIcon(largeIcon)

                .setContentText("You have "+messageBody+" Unread notification")
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }
}

