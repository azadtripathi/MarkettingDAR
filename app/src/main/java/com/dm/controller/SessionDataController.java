package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.SessionData;

import java.util.ArrayList;

public class SessionDataController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = { DatabaseConnection.COLUMN_ITEM_ID,
			DatabaseConnection.COLUMN_QTY, DatabaseConnection.COLUMN_RATE, DatabaseConnection.COLUMN_CASES, DatabaseConnection.COLUMN_UNIT};
	
	public SessionDataController(Context context) {
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
	
	public void insertSessionData(SessionData sessionData){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ITEM_ID, sessionData.getItem_id());
		values.put(DatabaseConnection.COLUMN_QTY, sessionData.getQty());
		values.put(DatabaseConnection.COLUMN_RATE, sessionData.getRate());
		values.put(DatabaseConnection.COLUMN_CASES, sessionData.getCases());
		values.put(DatabaseConnection.COLUMN_UNIT, sessionData.getUnit());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,sessionData.getAndroidId());
		values.put(DatabaseConnection.COLUMN_ORDER1_ANDROID_DOCID,sessionData.getAndroid1Id());
				try{
		long id = database.insert(DatabaseConnection.TABLE_SESSION_DATA, null, values);
		System.out.println("insert sess="+id);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	public ArrayList<SessionData> getSessionData(){
		Cursor cursor = database.query(DatabaseConnection.TABLE_SESSION_DATA,
				null, null, null, null, null, null);
		ArrayList<SessionData> sessionDatas = new ArrayList<SessionData>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				SessionData sessionData = new SessionData();
				sessionData.setItem_id(cursor.getString(0));
				sessionData.setQty(cursor.getString(1));
				sessionData.setRate(cursor.getString(2));
				sessionData.setCases(cursor.getString(3));
				sessionData.setUnit(cursor.getString(4));
				sessionData.setAndroid1Id(cursor.getString(5));
				sessionData.setAndroidId(cursor.getString(6));
				sessionDatas.add(sessionData);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return sessionDatas;
	}
	public void getUpdateData(String item,String partycode,String price){
		ContentValues values=new ContentValues();
		int cursor = database.update(DatabaseConnection.TABLE_SESSION_DATA, values, null, null);
	
	}
	public void deleteSessionBar() {
		try{
		int s=database.delete(DatabaseConnection.TABLE_SESSION_DATA, null, null);
		
			System.out.println("deleted="+s);
		
		
			System.out.println("problem="+s);
		
		}
		catch(Exception exception)
		{
			
		}
		
		
	}
	
	
}