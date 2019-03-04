package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.State;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StateController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns ={DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_REGION_CODE,
			DatabaseConnection.COLUMN_REGION_CODE,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_CREATED_DATE
	};
	
	public StateController(Context context) {
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


	public void insertState(String Nm,String Id,String RId,String ms,String active)
	{

		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, Id);
		values.put(DatabaseConnection.COLUMN_NAME, Nm);
		values.put(DatabaseConnection.COLUMN_REGION_CODE, RId);
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,ms);
		values.put(DatabaseConnection.COLUMN_ACTIVE, active);

		Cursor c = database.query(DatabaseConnection.TABLE_STATEMAST,null,"webcode=?",new String[]{Id},null,null,null);
		try{
			if(c.getCount()>0)
			{
				long id = database.update(DatabaseConnection.TABLE_STATEMAST,values,"webcode=?",new String[]{Id});
			}
			else {
				long id = database.insert(DatabaseConnection.TABLE_STATEMAST, null, values);
			}
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void insertState(State state){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, state.getState_id());
		values.put(DatabaseConnection.COLUMN_NAME, state.getState_name());
		values.put(DatabaseConnection.COLUMN_REGION_CODE, state.getState_region_id());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, state.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, state.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, state.getCreatedDate());
		try{
		long id = database.insert(DatabaseConnection.TABLE_STATEMAST, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public ArrayList<State> getStateList()
	{
		ArrayList<State> statenameList=new ArrayList<State>();
		State state1=new State();
		state1.setState_name("--Select--");
		state1.setState_id("0");
		statenameList.add(state1);
//		citynameList.add("Select city");
		String query="select name,webcode from MastState order by name";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	State state=new State();
				state.setState_name(cursor.getString(0));
				state.setState_id(cursor.getString(1));
				statenameList.add(state);
				cursor.moveToNext();
			}
		}
		else{

			System.out.println("No records found");
		}
		cursor.close();
		return statenameList;
	}

	public String getStateListObject(String countaryId){
		JSONArray result = new JSONArray();
		try {
			JSONObject userResults = new JSONObject();
			userResults.put("id","0");
			userResults.put("nm", "--Select--");
			result.put(userResults);
		} catch (Exception e) {}
			String query="select name,webcode from MastState where Region_id="+countaryId+" order by name";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				try {
                    JSONObject userResults = new JSONObject();
					userResults.put("id",cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_WEB_CODE)));
					userResults.put("nm", cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_NAME)));
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