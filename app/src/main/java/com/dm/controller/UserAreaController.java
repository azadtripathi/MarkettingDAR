package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.UserArea;

public class UserAreaController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_USER_CODE, DatabaseConnection.COLUMN_AREA_CODE};
	
	public UserAreaController(Context context) {
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
	
	public void insertUserArea(UserArea userarea){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_USER_CODE, userarea.getUser_id());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, userarea.getArea_id());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, userarea.getCreatedDate());
		try{
		long id = database.insert(DatabaseConnection.TABLE_USERAREAMAST, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

}