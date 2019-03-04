package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.Industry;

import java.util.ArrayList;

public class IndustryController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {	
			DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_CREATED_DATE
			};
	
	public IndustryController(Context context) {
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
	
	public void insertIndustry(Industry industry){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, industry.getIndustry_id());
		values.put(DatabaseConnection.COLUMN_NAME, industry.getIndustry_name());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, industry.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, industry.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, industry.getCreatedDate());
		try{
		long id = database.insert(DatabaseConnection.TABLE_INDUSTRYMAST, null, values);
		System.out.println(id);
		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		industry=null;values=null;
	}

	public void insertIndustry(
			String Id,
			String Nm,
			String MS
	){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, Id);
		values.put(DatabaseConnection.COLUMN_NAME, Nm);
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, MS);
		try
		{
			Cursor c = database.query(DatabaseConnection.TABLE_INDUSTRYMAST,null,"webcode=?",new String[]{Id},null,null,null);
			if(c.getCount()>0)
			{
				long id = database.update(DatabaseConnection.TABLE_INDUSTRYMAST, values, "webcode=?",new String[]{Id});
				System.out.println(id);
			}
			else {
				long id = database.insert(DatabaseConnection.TABLE_INDUSTRYMAST, null, values);
				System.out.println(id);
			}
		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		values=null;
	}
	public ArrayList<Industry> getIndustryTypeList(){
		Cursor cursor = database.rawQuery("select distinct industry_type from industrymast order by industry_type", null);
		ArrayList<Industry> industryArray = new ArrayList<Industry>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Industry industry = new Industry();
//				industry.setIndustryType(cursor.getString(0));
				System.out.println("indutry type records found"+cursor.getString(0));
				industryArray.add(industry);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No indutry type records found");
		}
		cursor.close();
		return industryArray;
	}
	public ArrayList<Industry> getIndustryList(String industryType){
		String query="select name,webcode from industrymast  where industry_type='"+industryType+"' order by name";
		Cursor cursor = database.rawQuery(query,null);
		ArrayList<Industry> industryArray = new ArrayList<Industry>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Industry industry = new Industry();
				industry.setIndustry_name(cursor.getString(0));
				industry.setIndustry_id(cursor.getString(1));
				industryArray.add(industry);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return industryArray;
	}
	public ArrayList<Industry> getIndustryList(){
		String query="select name,webcode from industrymast  order by name";
		Cursor cursor = database.rawQuery(query,null);
		ArrayList<Industry> industryArray = new ArrayList<Industry>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Industry industry = new Industry();
				industry.setIndustry_name(cursor.getString(0));
				industry.setIndustry_id(cursor.getString(1));
				industryArray.add(industry);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return industryArray;
	}
	public ArrayList<Industry> getIndustry(String webcode){
		String query="select * from industrymast where webcode='"+webcode+"'";
		Cursor cursor = database.rawQuery(query,null);
		ArrayList<Industry> industryArray = new ArrayList<Industry>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Industry industry = new Industry();
				industry.setIndustry_id(cursor.getString(0));
				industry.setIndustry_name(cursor.getString(1));
//				industry.setIndustryType(cursor.getString(2));
				industryArray.add(industry);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return industryArray;
	}

	public void deleteIndustryRow(){
		System.out.println("CDB Data deleted");
		database.delete(DatabaseConnection.TABLE_INDUSTRYMAST,null,null );
		System.out.println("Industry deleted");
	}
	
}