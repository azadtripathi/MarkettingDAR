package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.District;

public class DistrictController {

	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_STATE_CODE,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_CREATED_DATE
			};
	
	public DistrictController(Context context) {
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
	
	public void insertDistrict(District district){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, district.getDistrict_id());
		values.put(DatabaseConnection.COLUMN_NAME, district.getDistrict_name());
		values.put(DatabaseConnection.COLUMN_STATE_CODE, district.getState_id());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, district.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, district.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, district.getCreatedDate());
		try{
		long id = database.insert(DatabaseConnection.TABLE_DISTRICTMAST, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}


	public void insertDistrict(
			String Did,
			String NM,
			String Sid,
			String MS

	){


		int c=0;
		String qry="select count(*) from MastCity where webcode="+Did;
		System.out.println(qry);
		Cursor cursor = database.rawQuery(qry, null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				c=cursor.getInt(0);
				cursor.moveToNext();
			}
		}else{
			c=0;
		}
		cursor.close();
		ContentValues values = new ContentValues();
		if(c>0)
		{

		}
		else{
			values.put(DatabaseConnection.COLUMN_WEB_CODE, Did);
		}



		values.put(DatabaseConnection.COLUMN_NAME, NM);
		values.put(DatabaseConnection.COLUMN_STATE_CODE, Sid);
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, MS);
		long id=0;

		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_DISTRICTMAST,
							values, "webcode='" + Did + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_DISTRICTMAST, null, values);
					System.out.println("row=" + id);
				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
				}
			}


		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}

	}
}
