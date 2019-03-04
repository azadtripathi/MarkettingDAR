package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Dataman on 8/18/2017.
 */

public class AllSyncDataController
{
    private DatabaseConnection dbHelper;
    private SQLiteDatabase database;

    public AllSyncDataController(Context context)
    {
        dbHelper = new DatabaseConnection(context);
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



    public Cursor getAllData()
    {
        Cursor c = database.query(DatabaseConnection.TABLE_SYNC_STATUS,null,null,null,null,null,null,null);
        return c;
    }

    public void updateLastSyncTime(String displayName)
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        TimeZone utc = TimeZone.getTimeZone("etc/UTC");
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy hh:mm aa", Locale.ENGLISH);
//        df.setTimeZone(utc);//
        String formattedDate = df.format(c.getTime());
        ContentValues cv = new ContentValues();
        String sync_date_time = "";
        cv.put("LastSyncTime",formattedDate);;
        long id = database.update(DatabaseConnection.TABLE_SYNC_STATUS,cv,"DisplayName=?",new String[]{displayName});

    }
}
