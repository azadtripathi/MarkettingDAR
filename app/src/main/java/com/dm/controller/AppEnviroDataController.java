package com.dm.controller;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.AppEnviroData;

import java.util.ArrayList;

/**
 * Created by Dataman on 12/27/2016.
 */
public class AppEnviroDataController {

    private DatabaseConnection dbHelper;
    private SQLiteDatabase database;
    Context context;Activity activity;
    public AppEnviroDataController(Context context) {
        dbHelper = new DatabaseConnection(context);
        this.context=context;

    }

    public void open() {
        try{
            database = dbHelper.getWritableDatabase();
        }catch(SQLException e){
            System.out.println("-----------------"+e.getMessage());
        }
    }

    public void close() {
        dbHelper.close();
    }
    public void insertAppEnviroData(AppEnviroData appData){

        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_ITEM_WISE_SECONDARY_SALES, appData.getItemwisesale());
        values.put(DatabaseConnection.COLUMN_DIST_SEARCH_BY_NAME, appData.getDistSearch());
        values.put(DatabaseConnection.COLUMN_ITEM_SEARCH_BY_NAME, appData.getItemSearch());
        values.put(DatabaseConnection.COLUMN_AREA_WISE_DISTRIBUTOR, appData.getAreawiseDistributor());
        values.put(DatabaseConnection.COLUMN_IMAGE_DIRECTORY_NAME, appData.getImageDirectoryName());
        values.put(DatabaseConnection.COLUMN_FTP_HOST, appData.getHost());
        values.put(DatabaseConnection.COLUMN_FTP_USER_NAME, appData.getUserName());
        values.put(DatabaseConnection.COLUMN_FTP_PASSWORD, appData.getPassword());
        values.put(DatabaseConnection.COLUMN_FTP_DIRECTORY, appData.getFtpDirectory());
        values.put(DatabaseConnection.COLUMN_SHOW_DISCUSSION_STOCK, appData.getDistDiscussionStock());
        values.put(DatabaseConnection.COLUMN_MANDATORY_DSR_REMARK, appData.getDsrRemarkMandatory());
        values.put(DatabaseConnection.COLUMN_PARTY_CAPTION, appData.getPartyCaption());

        values.put(DatabaseConnection.Primary_Disc_NextVTime,appData.getNextVisitTime());
        values.put(DatabaseConnection.NEXT_VISIT_TIME_REQUIRED, appData.getNextVisitTimeRequired());
        values.put(DatabaseConnection.COLUMN_PRIM_FAILED_VISIT_NEXT_TIME,appData.getPrime_failed_visit_next_time());
        values.put(DatabaseConnection.COLUMN_PRIM_FAILED_VISIT_NEXT_TIME_REQ,appData.getPrime_next_visit_time_req());
        try{
            long id = database.insert(DatabaseConnection.TABLE_APPENVIRO, null, values);
            System.out.println(id);
        }
        catch(RuntimeException e){
            System.out.println("+++++++++++++++++++"+e.toString());
        }

    }
    public ArrayList<AppEnviroData> getAppEnviroFromDb(){
        String qry=" select * from AppEnviro";
        Cursor cursor = database.rawQuery(qry, null);
        ArrayList<AppEnviroData> appDataArray = new ArrayList<AppEnviroData>();
        if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast()))
            {
                AppEnviroData appData = new AppEnviroData();
                appData.setItemwisesale(cursor.getString(0));
                appData.setDistSearch(cursor.getString(1));
                appData.setItemSearch(cursor.getString(2));
                appData.setAreawiseDistributor(cursor.getString(3));
                appData.setImageDirectoryName(cursor.getString(4));
                appData.setHost(cursor.getString(5));
                appData.setUserName(cursor.getString(6));
                appData.setPassword(cursor.getString(7));
                appData.setFtpDirectory(cursor.getString(8));
                appData.setDistDiscussionStock(cursor.getString(9));
                appData.setDsrRemarkMandatory(cursor.getString(10));
                appData.setPartyCaption(cursor.getString(11));
                appData.setNextVisitTime(cursor.getString(cursor.getColumnIndex(DatabaseConnection.Primary_Disc_NextVTime)));
                appData.setNextVisitTimeRequired(cursor.getString(cursor.getColumnIndex(DatabaseConnection.NEXT_VISIT_TIME_REQUIRED)));
                appData.setPrime_failed_visit_next_time(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_PRIM_FAILED_VISIT_NEXT_TIME)));
                appData.setPrime_next_visit_time_req(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_PRIM_FAILED_VISIT_NEXT_TIME_REQ)));
                appDataArray.add(appData);
                cursor.moveToNext();
            }
        }else{
            System.out.println("No records found");
        }
        cursor.close();
        return appDataArray;
    }

    public String getPartyCaptionName(){
        String c="";
        String qry=" select PartyCaptio from AppEnviro";
        Cursor cursor = database.rawQuery(qry, null);

        if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast()))
            {

               c=cursor.getString(0);

                cursor.moveToNext();
            }
        }else{
            System.out.println("No records found");
        }
        cursor.close();
        return c;
    }

    public void deleteAppData() {
        try{
            int s=database.delete(DatabaseConnection.TABLE_APPENVIRO, null, null);

            System.out.println("deleted="+s);
        }
        catch(Exception exception)
        {
            System.out.println("problem="+exception);
        }
    }
}
