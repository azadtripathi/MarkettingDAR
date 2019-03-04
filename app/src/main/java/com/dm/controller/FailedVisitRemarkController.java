package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.FailedVisitRemark;

import java.util.ArrayList;

public class FailedVisitRemarkController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_CREATED_DATE
			
	};
	
	public FailedVisitRemarkController(Context context) {
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
	
	public void insertFailedVisitRemark(FailedVisitRemark failedVisitRemark){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, failedVisitRemark.getWebcode());
		values.put(DatabaseConnection.COLUMN_NAME, failedVisitRemark.getName());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, failedVisitRemark.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, failedVisitRemark.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, failedVisitRemark.getCreatedDate());
		try{
		long id = database.insert(DatabaseConnection.TABLE_FAILED_VISIT_MAST, null, values);
		System.out.println(id);
		} 
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void insertFailedVisitRemark(
			String ID,
			String FvName,String MS)

	{
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, ID);
		values.put(DatabaseConnection.COLUMN_NAME, FvName);
//		values.put(DatabaseConnection.COLUMN_SYNC_ID, failedVisitRemark.getSync_id());
//		values.put(DatabaseConnection.COLUMN_ACTIVE, failedVisitRemark.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, MS);
		try{
			Cursor c = database.query(DatabaseConnection.TABLE_FAILED_VISIT_MAST,null,"webcode=?",new String[]{ID},null,null,null);
			if(c.getCount()>0)
			{
				long id = database.update(DatabaseConnection.TABLE_FAILED_VISIT_MAST, values,"webcode=?",new String[]{ID});
				System.out.println(id);
			}
			else {
				long id = database.insert(DatabaseConnection.TABLE_FAILED_VISIT_MAST, null, values);
				System.out.println(id);
			}

		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	public ArrayList<FailedVisitRemark> getFailedVisitRemarkList(){
//		Cursor cursor = database.query(DatabaseConnection.TABLE_FAILED_VISIT_MAST,
//				allColumns, null, null, null, null, null);

		String query="select webcode, name from mastFailedVisit order by name";
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<FailedVisitRemark> failedVisitRemarks = new ArrayList<FailedVisitRemark>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				FailedVisitRemark failedVisitRemark = new FailedVisitRemark();
				failedVisitRemark.setWebcode(cursor.getString(0));
				failedVisitRemark.setName(cursor.getString(1));
				failedVisitRemarks.add(failedVisitRemark);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return failedVisitRemarks;
	}
	public String getFailedVisitReason(String rid){
		String r="0";
		String query="select name from mastFailedVisit where webcode='"+rid+"'";
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<FailedVisitRemark> failedVisitRemarks = new ArrayList<FailedVisitRemark>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{			r=cursor.getString(0);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return r;
	}
	
}