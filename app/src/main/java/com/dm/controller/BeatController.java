package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.Beat;

import java.util.ArrayList;

public class BeatController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_AREA_CODE,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_CREATED_DATE
			
			};
	
	public BeatController(Context context) {
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
	
	public void insertBeat(Beat beat){
		int c=0;
		String qry="select count(*) from MastBeat where webcode="+beat.getBeat_id();
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
			values.put(DatabaseConnection.COLUMN_WEB_CODE, beat.getBeat_id());
		}

		values.put(DatabaseConnection.COLUMN_NAME, beat.getBeat_name());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, beat.getArea_id());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, beat.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, beat.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, beat.getCreatedDate());
		long id=0;
		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_BEATMAST,
							values, "webcode='" +  beat.getBeat_id() + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_BEATMAST, null, values);
					System.out.println("row=" + id);
				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
				}
			}


		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}

		values=null;cursor=null;qry=null;
	}


	public void insertBeatParameter(String aId,String bId,String nm,String ms){
		int c=0;
		String qry="select count(*) from MastBeat where webcode="+bId;
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
			values.put(DatabaseConnection.COLUMN_WEB_CODE,bId);
		}

		values.put(DatabaseConnection.COLUMN_NAME, nm);
		values.put(DatabaseConnection.COLUMN_AREA_CODE, aId);
//		values.put(DatabaseConnection.COLUMN_SYNC_ID, beat.getSync_id());
//		values.put(DatabaseConnection.COLUMN_ACTIVE, beat.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, ms);
		long id=0;
		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_BEATMAST,
							values, "webcode='" +  bId + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_BEATMAST, null, values);
					System.out.println("row=" + id);
				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
				}
			}


		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}

		values=null;cursor=null;qry=null;aId=null;bId=null;nm=null;
	}

	public ArrayList<Beat> getBeatList(String areaCode){
//		Cursor cursor = database.query(DatabaseConnection.TABLE_BEATMAST,
//				allColumns, DatabaseConnection.COLUMN_AREA_CODE + "='"+areaCode+"'", null, null, null, null);
		String qry="select * from MastBeat where  area_code='"+areaCode+"' order by name";
		Cursor cursor = database.rawQuery(qry, null);
		
		ArrayList<Beat> beatArray = new ArrayList<Beat>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Beat beat = new Beat();
				beat.setBeat_id((cursor.getString(0)));
				beat.setBeat_name(cursor.getString(1));
				beat.setArea_id(cursor.getString(2));
				beatArray.add(beat);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return beatArray;
	}

	public ArrayList<Beat> getBeatListForFind(String areaCode){
//		Cursor cursor = database.query(DatabaseConnection.TABLE_BEATMAST,
//				allColumns, DatabaseConnection.COLUMN_AREA_CODE + "='"+areaCode+"'", null, null, null, null);

		String qry="select * from MastBeat where  area_code='"+areaCode+"' order by name";
		Cursor cursor = database.rawQuery(qry, null);

		ArrayList<Beat> beatArray = new ArrayList<Beat>();
		Beat beat1 = new Beat();
		beat1.setBeat_id("0");
		beat1.setBeat_name("--SELECT--");
		beat1.setArea_id("0");
		beatArray.add(beat1);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				Beat beat = new Beat();
				beat.setBeat_id((cursor.getString(0)));
				beat.setBeat_name(cursor.getString(1));
				beat.setArea_id(cursor.getString(2));
				beatArray.add(beat);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return beatArray;
	}
	public ArrayList<Beat> getBeatList(){
		Cursor cursor = database.query(DatabaseConnection.TABLE_BEATMAST,
				allColumns, null, null, null, null, null);
		ArrayList<Beat> beatArray = new ArrayList<Beat>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Beat beat = new Beat();
				beat.setBeat_id(String.valueOf(cursor.getInt(0)));
				beat.setBeat_name(cursor.getString(1));
				beat.setArea_id(String.valueOf(cursor.getInt(2)));
				beatArray.add(beat);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return beatArray;
	}
	public String getAreaCode(String name){
		String areaCode =null;
		String query = "select webcode from Areamast where name='"+name+"'";
		Cursor cursor = database.rawQuery(query,null);
		
		 if (cursor.getCount()==1)
		    {
		            cursor.moveToLast();   
		            areaCode=cursor.getString(0);
		            System.out.println("areaCode  "+areaCode);
		         
		    }
		 else{
			System.out.println("no areaCode found");
		}
		 System.out.println("no areaCode found"+areaCode);
		return areaCode;
	}
	public String getBeatCode(String name){
		String beatCode =null;
		String query = "select webcode from Beatmast where name='"+name+"'";
		Cursor cursor = database.rawQuery(query,null);
		
		 if (cursor.getCount()==1)
		    {
		            cursor.moveToLast();   
		            beatCode=cursor.getString(0);
		            System.out.println("beatCode  "+beatCode);
		         
		    }
		 else{
			System.out.println("no beatCode found");
		}
		 System.out.println("no beatCode found"+beatCode);
		 cursor.close();
		return beatCode;
	}
}