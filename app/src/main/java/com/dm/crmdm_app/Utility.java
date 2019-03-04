package com.dm.crmdm_app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.database.DatabaseConnection;
import com.dm.library.ClearAppData;
import com.dm.library.DbCon;
import com.dm.library.ExportDBTo;
import com.dm.library.IntentSend;
import com.dm.util.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import it.sauronsoftware.ftp4j.FTPClient;

public class Utility extends AppCompatActivity {
    Button exportDb,clearData;
    String smid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utility);

        SharedPreferences preferences2=this.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        smid = preferences2.getString("CONPERID_SESSION", null);
        TextView textView = (TextView)findViewById(R.id.vLabel);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String versionName = pInfo.versionName;
            String versionCode = String.valueOf(pInfo.versionCode);
            textView.setText("Version Name: "+versionName+" Version Code: "+versionCode);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
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
        tv.setText("Utility");
        exportDb=(Button)findViewById(R.id.exportDb);
        clearData=(Button)findViewById(R.id.claerData);
        exportDb.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert = new AlertDialog.Builder(Utility.this);
                alert.setMessage("Are you sure you want to export app database?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //  new ExportDBTo(Utility.this).exportDatabaseToPhoneStorage();

                        try {
                            File sd = Environment.getExternalStorageDirectory();
                            File data = Environment.getDataDirectory();

                            if (sd.canWrite())
                            {
                                String currentDBPath = "//data//com.dm.crmdm_app//databases//"+ DatabaseConnection.DATABASE_NAME;//datamancrm_mobile_db.db";
                                String backupDBPath = ""+smid+"_"+DatabaseConnection.DATABASE_NAME;
                                File currentDB = new File(data, currentDBPath);
                                File backupDB = new File(sd, backupDBPath);

                                if (currentDB.exists())
                                {
                                    FileChannel src = new FileInputStream(currentDB).getChannel();
                                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                                    dst.transferFrom(src, 0, src.size());
                                    src.close();
                                    dst.close();
                                    insertFile(backupDB.getPath());
                                   // backupDB.delete();

                                   /* Intent emailApp = new Intent(Intent.ACTION_SEND);
                                    emailApp.putExtra(Intent.EXTRA_EMAIL, new String[]{"vinayak.dutt@dataman.in"});
                                    emailApp.putExtra(Intent.EXTRA_SUBJECT, "Backup_DB");
                                    emailApp.putExtra(Intent.EXTRA_TEXT, "");
                                    emailApp.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(backupDB));
                                    emailApp.setType("message/rfc822");
                                    startActivity(Intent.createChooser(emailApp, "Send Email Via"));*/
                                }
                            }
                        } catch (Exception e) {

                        }
                    }
                });

                alert.setNegativeButton("No",null);
                alert.create().show();
            }
        });

        clearData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(Utility.this);
                alert.setMessage("Are you sure you want to clear app data?");
                alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // new ClearAppData(Utility.this).clearAllDataStorage();

                        final ProgressDialog progressDialog = new ProgressDialog(Utility.this);
                        progressDialog.setMessage("Cleaning Data Please wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        clearApplicationData();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressDialog.dismiss();
                                SharedPreferences settings = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
                                settings.edit().clear().commit();
                                SharedPreferences settings1 = getSharedPreferences("ENVIRO_SESSION_DATA", Context.MODE_PRIVATE);
                                settings1.edit().clear().commit();
                                SharedPreferences preferences=getSharedPreferences("RETAILER_SESSION_DATA",MODE_PRIVATE);
                                preferences.edit().clear().commit();
                                SharedPreferences preferences2=getSharedPreferences("MyTourDist",Context.MODE_PRIVATE);
                                preferences2.edit().clear().commit();

                                Intent i = getBaseContext().getPackageManager()
                                        .getLaunchIntentForPackage( getBaseContext().getPackageName() );
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            }
                        },500);

                       /* Intent intent=new Intent(Utility.this,UserPermission.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();*/
                    }
                });

                alert.setNegativeButton("No",null);
                alert.create().show();


            }
        });
    }



    SQLiteDatabase database;
    DbCon dbcon;
    public void insertFile(String filePath)
    {
        dbcon = new DbCon(this);
        String Host = null,Username = null,Password = null,FtpDirectory = null;
        File file = new File(filePath);

        database = dbcon.open();
        String qry = "select * from AppEnviro";
        Cursor cursor = database.rawQuery(qry, null);
        try {
            while (cursor.moveToNext()) {
                Host          = cursor.getString(5);
                Username      = cursor.getString(6);
                Password      = cursor.getString(7);
                FtpDirectory  = "MobileDB";//cursor.getString(8);
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

                client.changeDirectory(FtpDirectory); //I want to upload picture in MyPictures directory/folder. you can use your own.
            }
            catch (Exception e) {

//                      client.createDirectory(Constant.FTP_DIRECTORY);
//                      client.changeDirectory(Constant.FTP_DIRECTORY);
                System.out.println(e);

            }
            client.upload(file); //this is actual file uploading on FtpServer in specified directory/folder
            client.disconnect(true);   //after file upload, don't forget to disconnect from FtpServer.


            AlertDialog.Builder alert = new AlertDialog.Builder(Utility.this);
            alert.setMessage("DB exported successfully");
            alert.setPositiveButton("OK",null);
            alert.create().show();

            //file.delete();
        }
        catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "Exception: "+e.getMessage(), Toast.LENGTH_LONG).show();
            System.out.println(e);
        }
    }

    public void emailDb(View v)
    {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//com.dm.crmdm_app//databases//"+ DatabaseConnection.DATABASE_NAME;//datamancrm_mobile_db.db";
                String backupDBPath = DatabaseConnection.DATABASE_NAME;
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists())
                {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();

                    Intent emailApp = new Intent(Intent.ACTION_SEND);
                    emailApp.putExtra(Intent.EXTRA_EMAIL, new String[]{"nishutripathi@dataman.in"});
                    emailApp.putExtra(Intent.EXTRA_SUBJECT, "CRM_Backup_DB");
                    emailApp.putExtra(Intent.EXTRA_TEXT, "");
                    emailApp.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(backupDB));
                    emailApp.setType("message/rfc822");
                    startActivity(Intent.createChooser(emailApp, "Send Email Via"));
                }
            }
        }
        catch (Exception e)
        {

        }
    }

    public void clearApplicationData() {
        File cache =getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                // if (!s.equals("lib")) {
                deleteDir(new File(appDir, s));

                // }
            }
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(Utility.this, DashBoradActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}
