package com.dm.controller;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.AppData;

import java.util.ArrayList;

public class AppDataController {

	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	Context context;Activity activity;	
	public AppDataController(Context context) {
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
	public void insertAppData(AppData appData){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_COMPANY_NAME, appData.getCompanyName());
		values.put(DatabaseConnection.COLUMN_COMPANY_URL, appData.getCompanyUrl());
		values.put(DatabaseConnection.COLUMN_TYPE, appData.getAppType());
		try{
		long id = database.insert(DatabaseConnection.TABLE_APPDATA, null, values);
		System.out.println(id);
		} 
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	
public ArrayList<AppData> getAppTypeFromDb(){
		String qry=" select * from MastAppData";
		Cursor cursor = database.rawQuery(qry, null);
		ArrayList<AppData> appDataArray = new ArrayList<AppData>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				AppData appData = new AppData();
				appData.setCompanyName(cursor.getString(0));
				appData.setCompanyUrl(cursor.getString(1));
				appData.setAppType(cursor.getString(2));
				appDataArray.add(appData);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return appDataArray;
	}

public void deleteAppData() {
	try{
	int s=database.delete(DatabaseConnection.TABLE_APPDATA, null, null);
	
		System.out.println("deleted="+s);
	
	
		System.out.println("problem="+s);
	
	}
	catch(Exception exception)
	{
		System.out.println("problem="+exception);
	}
	
	
}

public int getAppDataExist()
{
	int dataCount=0;
	try{
		String query="select count(*) as datacount  from MastAppData";

	Cursor cursor =database.rawQuery(query, null);
	
	 if (cursor.getCount()==1)
	    { cursor.moveToLast(); 
	    dataCount=cursor.getInt(0);
	       cursor.moveToNext();
	    }
	 else{
			System.out.println("No records found");
			
		}
		cursor.close();
	}catch(Exception e)
	{
		System.out.println(e);
	}

		return dataCount;
}

	
}
