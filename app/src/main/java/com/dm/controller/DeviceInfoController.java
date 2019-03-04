package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DeviceInfoController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = { DatabaseConnection.COLUMN_ID,
			DatabaseConnection.COLUMN_WEB_CODE, DatabaseConnection.COLUMN_NAME};
	
	public DeviceInfoController(Context context) {
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
	
	public void insertDeviceInfo(String pdaid){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_PDAIDBYUSER, pdaid);
		try
		{
			long id = database.insert(DatabaseConnection.TABLE_DEVICE_INFO, null, values);

		
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	public String getpdaId(){
		String pdaid="0" ;
		String query="select ifnull(pdaidbyuser,'0') from "+ DatabaseConnection.TABLE_DEVICE_INFO;
//		String query="select pdaidbyuser from "+DatabaseConnection.TABLE_DEVICE_INFO;
		Cursor cursor = database.rawQuery(query,null);
		System.out.println(query);
		
		 if (cursor.getCount()==1)
		    {
		            cursor.moveToLast();   
		            pdaid=cursor.getString(0);
		            System.out.println("max id found  "+pdaid);
		           
		    }
		 else{
			System.out.println("no max id found");
			
		}
		 System.out.println("no max id found"+pdaid);
		 
		 cursor.close();
		return pdaid;
	}
	public void getUpdatePdaId(String pdaid,String pdaidbyuser){
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
		String query="update deviceinfo set "
				+"pdaidbyuser='"+pdaidbyuser+"' where pdaidbyuser='"+pdaid+"'";

		System.out.println(query);
		
		try{
			//String qurey="UPDATE order_sub SET remark="+""+" WHERE ExecutionDate = '2011-10-12 00:00:00.000'";
					
					
		database.execSQL(query);
		System.out.println(" Dsr updated");
		
		
		
	
		
		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		
	}
	public void deletedeviceRow(){
		System.out.println("CDB Data deleted");
		database.delete(DatabaseConnection.TABLE_DEVICE_INFO,null,null );
		System.out.println("dsr deleted");
	}
	 
}