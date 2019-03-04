package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.AreaType;

public class AreaTypeController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = { DatabaseConnection.COLUMN_ID,
			DatabaseConnection.COLUMN_WEB_CODE, DatabaseConnection.COLUMN_NAME};
	
	public AreaTypeController(Context context) {
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
	
	public void insertAreaType(AreaType areatype){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, areatype.getArea_type_id());
		values.put(DatabaseConnection.COLUMN_NAME, areatype.getArea_type_name());
				try{
		long id = database.insert(DatabaseConnection.TABLE_AREATYPEMAST, null, values);
					System.out.println("+++++++++++++++++++"+id);
				} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void deleteAreaTypeData() {
		try{
			int s=database.delete(DatabaseConnection.TABLE_AREATYPEMAST, null, null);

			System.out.println("deleted="+s);
		}
		catch(Exception exception)
		{
			System.out.println("problem="+exception);
		}
	}

	public boolean getAreaStringEmpty()
	{
		String areaString="0";
		try{
			String query="select ifnull(name,0) as areaSt from Areatypemast";
			System.out.println(query);
			Cursor cursor =database.rawQuery(query, null);

		if(cursor.getCount()==0)
		{
			return true;
		}
			else
		{
			return false;
		}
		}catch(Exception e)
		{
			System.out.println(e);
		}

		return false;
	}

	public String getAreaString()
	{
		String areaString="0";
		try{
			String query="select ifnull(name,0) as areaSt from Areatypemast";
			System.out.println(query);
			Cursor cursor =database.rawQuery(query, null);

			if (cursor.getCount()==1)
			{ cursor.moveToLast();
				areaString=cursor.getString(0);
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

		return areaString;
	}


}