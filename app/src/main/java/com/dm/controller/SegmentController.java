package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.Segment;

import java.util.ArrayList;

public class SegmentController {

	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;Context context;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_WEB_CODE ,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_CREATED_DATE
	};
	
	public SegmentController(Context context) {
		dbHelper = new DatabaseConnection(context);
		this.context=context;
	}
	public void open() {
		try{
		database = dbHelper.getWritableDatabase();
		}catch(SQLException e){
			System.out.println("-----------------"+e.getMessage());
		}
	}


	public void delete()
	{
		database.delete(DatabaseConnection.TABLE_SEGMENTMAST,null,null);
	}

	public void close() {
		dbHelper.close();
	}
	
	public void insertSegment(Segment segment){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, segment.getSegment_id());
		values.put(DatabaseConnection.COLUMN_NAME, segment.getSegment_name().toUpperCase());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, segment.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, segment.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, segment.getCreatedDate());
		try{
		long id = database.insert(DatabaseConnection.TABLE_SEGMENTMAST, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	public void insertSegment(
			String Nm,
			String Id,
			String timeStamp,
			String active

	){
		try{
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, Id);
		values.put(DatabaseConnection.COLUMN_NAME, Nm.toUpperCase());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
			values.put(DatabaseConnection.COLUMN_ACTIVE, active);
		String query = "Select * from mastSegment where webcode="+Id;
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.getCount() == 0)
		{
			long id = database.insert(DatabaseConnection.TABLE_SEGMENTMAST, null, values);
			id = id;
		}
		else
		{
			long uid = database.update(DatabaseConnection.TABLE_SEGMENTMAST, values, "webcode=?",new String[]{Id});
			uid = uid;
		}


		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}

	}
	public ArrayList<Segment> getSegmentList(){
		String orderby= DatabaseConnection.COLUMN_NAME;
		Cursor cursor = database.query(DatabaseConnection.TABLE_SEGMENTMAST,
					allColumns, null, null, null, null, orderby);
			ArrayList<Segment> producSegment = new ArrayList<Segment>();
			if(cursor.moveToFirst()){
				while(!(cursor.isAfterLast()))
				{		
					Segment segment=new Segment();
					segment.setSegment_id(cursor.getString(0));
					segment.setSegment_name(cursor.getString(1));
					producSegment.add(segment);
					cursor.moveToNext();
				}
			}else{
				System.out.println("No records found");
			}
			cursor.close();
			return producSegment;
		}
	
}
