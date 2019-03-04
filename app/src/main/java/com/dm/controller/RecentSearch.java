package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.City;
import com.dm.model.CompanyDetails;
import com.dm.model.Country;
import com.dm.model.State;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class RecentSearch{
    private DatabaseConnection dbHelper;
    private SQLiteDatabase database;
    public RecentSearch(Context context) {
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

    public ArrayList<CompanyDetails> getRecentData()
    {
        ArrayList<CompanyDetails> citynameList=new ArrayList<CompanyDetails>();
        //ArrayList<String> citynameList=new ArrayList<String>();
//		citynameList.add("Select city");
        //String query="select * from Chat order by Pk_id asc  Limit 3";
        String query="select * from RecentSearch  order by TimeStamp Asc Limit 20";
        Cursor cursor = database.rawQuery(query,null);
        if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast()))
            {	CompanyDetails companyDetails=new CompanyDetails();
                companyDetails.setId(cursor.getString(0));
                companyDetails.setCity(cursor.getString(1));
                companyDetails.setPersonName(cursor.getString(2));
                companyDetails.setMobileNumber(cursor.getString(3));
                citynameList.add(companyDetails);
                System.out.println("Active Value:"+cursor.getString(2)+":"+cursor.getString(0));
                //citynameList.add(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        else{

            System.out.println("No records found");
        }
        cursor.close();
        return citynameList;
    }

    public void storeRecentData(ContentValues contentValues,String recentSearchId)
    {

        if(recentSearchId == null  || recentSearchId.isEmpty()){
            try{
                long id = database.insert(DatabaseConnection.Table_Recent_Search, null, contentValues);
                System.out.println("+++++++++++++++++++"+id);
            } catch(RuntimeException e){
                System.out.println("+++++++++++++++++++"+e.toString());
            }
        }
        else{
            try{
                long  id = database.update(DatabaseConnection.Table_Recent_Search, contentValues, "id="+recentSearchId+"", null);
                System.out.println("row="+id);
            }
            catch(RuntimeException e){
                System.out.println("+++++++++++++++++++"+e.toString());
            }
        }

    }
}

