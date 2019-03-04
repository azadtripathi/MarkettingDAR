package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.DistributorArea;

public class DistributorAreaController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_DIST_CODE, DatabaseConnection.COLUMN_AREA_CODE};
	
	public DistributorAreaController(Context context) {
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
	
	public void insertDistributorArea(DistributorArea distributorarea){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_DIST_CODE, distributorarea.getDistributor_id());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, distributorarea.getArea_id());
		try{
		long id = database.insert(DatabaseConnection.TABLE_DISTAREAMAST, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	
	
}