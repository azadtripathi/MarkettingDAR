package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.Scheme;

import java.util.ArrayList;

public class SchemeController {

	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {	
			DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_CREATED_DATE,
			DatabaseConnection.COLUMN_ACTIVE
	};
	
	public SchemeController(Context context) {
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
	
	public void insertScheme(Scheme scheme){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, scheme.getSchemeid());
		values.put(DatabaseConnection.COLUMN_NAME, scheme.getSchemename());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, scheme.getSync_id());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, scheme.getCreatedDate());
		values.put(DatabaseConnection.COLUMN_ACTIVE, scheme.getActive());
		try{
		long id = database.insert(DatabaseConnection.TABLE_SCHEME, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void insertScheme(String Id,String Nm,String ms)

	{
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, Id);
		values.put(DatabaseConnection.COLUMN_NAME, Nm);
//		values.put(DatabaseConnection.COLUMN_SYNC_ID, scheme.getSync_id());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, ms);
//		values.put(DatabaseConnection.COLUMN_ACTIVE, scheme.getActive());
		try{
			long id = database.insert(DatabaseConnection.TABLE_SCHEME, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public ArrayList<Scheme> getSchemeNameList()
	{
		ArrayList<Scheme> schemenameList=new ArrayList<Scheme>();
		String query="select webcode,name from MastScheme order by name";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	Scheme scheme=new Scheme();
			scheme.setSchemeid(cursor.getString(0));
			scheme.setSchemename(cursor.getString(1));
			schemenameList.add(scheme);
				cursor.moveToNext();
			}
		}
		else{
			
			System.out.println("No records found");
		}
		cursor.close();
		return schemenameList;
	}
	
	
}
