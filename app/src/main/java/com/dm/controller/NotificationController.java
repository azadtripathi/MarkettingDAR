package com.dm.controller;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.Area;
import com.dm.model.NotificationData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dataman on 24-Mar-17.
 */
public class NotificationController {
    ArrayList<Map<String, String>> notivifationValues= new ArrayList<Map<String, String>>();
    private SQLiteDatabase database;private DatabaseConnection dbHelper;
    public NotificationController(Context context) {
        dbHelper = new DatabaseConnection(context);
    }
    public String getNotification(){
        try{
            database = dbHelper.getWritableDatabase();
        }catch(SQLException e){
            System.out.println("-----------------"+e.getMessage());
        }

//		Cursor cursor = database.query(DatabaseConnection.TABLE_AREAMAST,
//				allColumns, null, null, null, null, null);
        String nm="";
        String qry="select * from notification ORDER BY date desc Limit 1";
        System.out.println(qry);
        Cursor cursor = database.rawQuery(qry, null);
        ArrayList<Area> areaArray = new ArrayList<Area>();
        if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast()))
            {

                nm=cursor.getString(0);

                cursor.moveToNext();
            }
        }else{
            nm="0";
            System.out.println("0");
        }
        cursor.close();
        return nm;
    }
    public   ArrayList<Map<String, String>>getValues(String timestamp) {

        try{
            database = dbHelper.getWritableDatabase();
        }catch(SQLException e){
            System.out.println("-----------------"+e.getMessage());
        }
        String qry;
       /* if(timestamp.equalsIgnoreCase("0"))
        {
            qry="select lvqr,msz from NotificationData order by timestamp desc";
        }
        else
        {
            qry="select lvqr,msz from NotificationData where timestamp<"+timestamp+" order by timestamp desc";

        }*/
        //qry="select lvqr,msz,status from NotificationData order by timestamp desc";
       qry="select notify,msz,status from NotificationData order by timestamp desc";
        System.out.println("Database Query"+qry);
        Cursor cursor = database.rawQuery(qry, null);
        if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast()))
            {
                HashMap<String, String> hashMap1 = new HashMap<>();
                hashMap1.put("nplrnotid", cursor.getString(0) );
                hashMap1.put("nplrmsg", cursor.getString(1) );
                hashMap1.put("status",cursor.getString(cursor.getColumnIndex("status")));
                notivifationValues.add(hashMap1);
                cursor.moveToNext();
            }
        }else{

        }
        cursor.close();
        return notivifationValues;
    }
    public ArrayList<String> getPageName(String docid){
        try{
            database = dbHelper.getWritableDatabase();
        }catch(SQLException e){
            System.out.println("-----------------"+e.getMessage());
        }

//		Cursor cursor = database.query(DatabaseConnection.TABLE_AREAMAST,
//				allColumns, null, null, null, null, null);
        ArrayList<String> nm=new ArrayList<>();//REPLACE(username, ' ', '') = REPLACE("John Bob Jones", ' ', '')
        String qry="select pro_id,lvqr from NotificationData where notify='"+docid+"'";
        System.out.println("getPageName"+qry);
        Cursor cursor = database.rawQuery(qry, null);
         if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast()))
            {
                NotificationData notification=new NotificationData();
                nm.add(cursor.getString(0));
                nm.add(cursor.getString(1));
                cursor.moveToNext();
            }
        }else{
             nm.add("0");
            System.out.println("0");
        }
        cursor.close();
        return nm;
    }
    public String getTimeStamp(){
        try{
            database = dbHelper.getWritableDatabase();
        }catch(SQLException e){
            System.out.println("-----------------"+e.getMessage());
        }

//		Cursor cursor = database.query(DatabaseConnection.TABLE_AREAMAST,
//				allColumns, null, null, null, null, null);
        String timestamp="";//REPLACE(username, ' ', '') = REPLACE("John Bob Jones", ' ', '')
        String qry="select timestamp from NotificationData order by timestamp asc Limit 1";
        System.out.println("getPageName"+qry);
        Cursor cursor = database.rawQuery(qry, null);
        if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast()))
            {

                timestamp=cursor.getString(0);

                cursor.moveToNext();
            }
        }else{
            timestamp="0";
            System.out.println("0");
        }
        cursor.close();
        return timestamp;
    }
 /*   public ArrayList<String> getValues1(String timestamp) {
        try {
            database = dbHelper.getWritableDatabase();
        } catch (SQLException e) {
            System.out.println("-----------------" + e.getMessage());
        }
        ArrayList<String> massage = new ArrayList<String>();
        String qry;
        if(timestamp.equalsIgnoreCase("0"))
        {
            qry="select lvqr,msz from NotificationData order by timestamp desc";
        }
        else
        {
            qry="select lvqr,msz from NotificationData where timestamp<"+timestamp+" order by timestamp desc";

        }
        System.out.println("Database Query" + qry);
        Cursor cursor = database.rawQuery(qry, null);
        if (cursor.moveToFirst()) {
            while (!(cursor.isAfterLast())) {
                System.out.println("Database Data" + cursor.getString(0));
                massage.add(cursor.getString(0));
                cursor.moveToNext();
            }
        } else {

        }
        cursor.close();
        return massage;
    }*/
 public String getStatus(String docid){
     try{
         database = dbHelper.getWritableDatabase();
     }catch(SQLException e){
         System.out.println("-----------------"+e.getMessage());
     }

//		Cursor cursor = database.query(DatabaseConnection.TABLE_AREAMAST,
//				allColumns, null, null, null, null, null);
     String nm="";//REPLACE(username, ' ', '') = REPLACE("John Bob Jones", ' ', '')
     String qry="select status from NotificationData where lvqr='"+docid+"'";
     System.out.println("getPageName"+qry);
     Cursor cursor = database.rawQuery(qry, null);
     if(cursor.moveToFirst()){
         while(!(cursor.isAfterLast()))
         {
             nm=(cursor.getString(0));
             cursor.moveToNext();
         }
     }else{
         nm=("0");
         System.out.println("0");
     }
     cursor.close();
     return nm;
 }
}
