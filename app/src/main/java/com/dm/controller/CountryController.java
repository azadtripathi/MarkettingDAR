/**
 * AreaController.javacom.example.controller
 */
package com.dm.controller;

/**
 * @author dataman
 *
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.Area;
import com.dm.model.Competitor;
import com.dm.model.Country;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CountryController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_CREATED_DATE
				
	};
	
	public CountryController(Context context) {
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
	
	public void insertCountry(Country country){

		String qry="select * from MastCountry where webcode='"+country.getId()+"'";
		Cursor cursor = database.rawQuery(qry,null);

		ContentValues values = new ContentValues();
		System.err.println("wecode "+country.getId()+"\n"+" Description"+country.getDescription());
		values.put(DatabaseConnection.COLUMN_WEB_CODE, country.getId());
		values.put(DatabaseConnection.COLUMN_NAME, country.getDescription());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, country.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, country.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, country.getCreatedDate());
		try{
			if(cursor.getCount() >0)
			{
				long id = database.update(DatabaseConnection.TABLE_COUNTRYMAST,values,"webcode=?",new String[]{country.getId()});
			}
			else {
				long id = database.insert(DatabaseConnection.TABLE_COUNTRYMAST, null, values);
			}
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	public ArrayList<Area> getAreaList(){
		Cursor cursor = database.query(DatabaseConnection.TABLE_AREAMAST,
				allColumns, null, null, null, null, null);
		ArrayList<Area> areaArray = new ArrayList<Area>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Area area = new Area();
				area.setArea_id(cursor.getString(0));
				area.setArea_name(cursor.getString(1));
				area.setArea_id(cursor.getString(2));
				area.setCity_id(cursor.getString(3));
				
				
				areaArray.add(area);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return areaArray;
	}
	public String getCountaryList(){
		JSONArray result = new JSONArray();
		//StringBuilder result=new StringBuilder();
		String qry="Select *  from "+ DatabaseConnection.TABLE_COUNTRYMAST;
		Cursor cursor = database.rawQuery(qry,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				try {
					JSONObject userResults = new JSONObject();
					userResults.put("Cid",cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_WEB_CODE)));
					userResults.put("NM", cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_NAME)));
                    result.put(userResults);
				} catch (Exception e) {
					e.printStackTrace();
				}
				cursor.moveToNext();
			}
		}
		cursor.close();
		return result.toString();
	}
}
