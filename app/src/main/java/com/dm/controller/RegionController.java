package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.Region;

public class RegionController {
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_WEB_CODE, 
			DatabaseConnection.COLUMN_NAME, 
			DatabaseConnection.COLUMN_COUNTRY_CODE,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_CREATED_DATE
			
			};
	
	public RegionController(Context context) {
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
	
	public void insertRegion(Region region){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, region.getRegion_id());
		values.put(DatabaseConnection.COLUMN_NAME, region.getRegion_name());
		values.put(DatabaseConnection.COLUMN_COUNTRY_CODE, region.getCountry_id());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, region.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, region.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, region.getCreatedDate());
		try{
			Cursor c = database.query(DatabaseConnection.TABLE_REGIONMAST,null,"webcode=?",new String[]{region.getRegion_id()},null,null,null,null);
			if(c.getCount()>0)
			{
				long id = database.update(DatabaseConnection.TABLE_REGIONMAST,values,"webcode=?",new String[]{region.getRegion_id()});
				System.out.println("+++++++++++++++++++"+id);
			}
			else {
				long id = database.insert(DatabaseConnection.TABLE_REGIONMAST, null, values);
				System.out.println("+++++++++++++++++++"+id);
			}
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
}
