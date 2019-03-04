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
import com.dm.model.Sman;

import java.util.ArrayList;

public class SmanController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_LEVEL,
			DatabaseConnection.COLUMN_ROLEID,
			DatabaseConnection.COLUMN_REPORTING_PERSON,
			DatabaseConnection.COLUMN_UNDERID,
			DatabaseConnection.COLUMN_SREP_TYPE
	};
	
	public SmanController(Context context) {
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
	
	public void insertSman(Sman sman){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, sman.getConPerId());
		values.put(DatabaseConnection.COLUMN_NAME, sman.getUser_Name());
		values.put(DatabaseConnection.COLUMN_LEVEL, sman.getLevel());
		values.put(DatabaseConnection.COLUMN_ROLEID, sman.getRoleId());
		values.put(DatabaseConnection.COLUMN_REPORTING_PERSON, sman.getDisplayName());
		values.put(DatabaseConnection.COLUMN_UNDERID, sman.getUnderId());
		values.put(DatabaseConnection.COLUMN_SREP_TYPE, sman.getSrType());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, sman.getCreatedDate());
		try{
		long id = database.insert(DatabaseConnection.TABLE_SALESMANMAST, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void insertSman(
			String Smid,
			String Lvl,
			String Uid,
			String Smnm,String timeStamp

	){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, Smid);
		values.put(DatabaseConnection.COLUMN_NAME, Smnm);
		values.put(DatabaseConnection.COLUMN_LEVEL,Lvl);
		values.put(DatabaseConnection.COLUMN_UNDERID, Uid);
		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
		/*****************		END		******************/
		try{
			long id = database.insert(DatabaseConnection.TABLE_SALESMANMAST, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	
//	public ArrayList<Sman> getSmanList(){
//		Cursor cursor = database.query(DatabaseConnection.TABLE_SALESMANMAST,
//				allColumns, null, null, null, null, null);
//		ArrayList<Sman> areaArray = new ArrayList<Sman>();
//		if(cursor.moveToFirst()){
//			while(!(cursor.isAfterLast()))
//			{				
//				Sman area = new Sman();			
//				area.setConPerId(cursor.getString(0));
//				area.setUser_Name(cursor.getString(1));
//				area.setLevel(cursor.getString(2));
//				area.setReporting_person(cursor.getString(3));
//				areaArray.add(area);
//				System.out.println(cursor.getString(1));
//				cursor.moveToNext();
//			}
//		}else{
//			System.out.println("No records found");
//		}
//		cursor.close();
//		if(areaArray.size()==3)
//		{
//			Sman area = new Sman();
//			area.setConPerId("");
//			area.setUser_Name("");
//			area.setLevel("");
//			area.setReporting_person("");
//			areaArray.add(area);
//		}
//		else
//		{
//		
//			
//		}
//		return areaArray;
//		
//	}
//	
	
	public ArrayList<Sman> getNameList(String webcode)
	{
		ArrayList<Sman> nameList=new ArrayList<Sman>();
//		nameList.add("Select city");
		String query="SELECT webcode,name FROM mastSalesPerson WHERE webcode <>'"+webcode+"' order by name";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	
				Sman sman=new Sman();
				sman.setConPerId(cursor.getString(0));
				sman.setUser_Name(cursor.getString(1));
				nameList.add(sman);
				cursor.moveToNext();
			}
		}
		else{
			
			System.out.println("No records found");
		}
		cursor.close();
		return nameList;
	}
	
	
	public ArrayList<Sman> getName(String webcode)
	{
		ArrayList<Sman> nameList=new ArrayList<Sman>();
//		nameList.add("Select city");
		String query="SELECT * FROM mastSalesPerson WHERE webcode='"+webcode+"'";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	
				Sman sman=new Sman();
				sman.setConPerId(cursor.getString(0));
				sman.setUser_Name(cursor.getString(1));
				sman.setLevel(cursor.getString(2));
				sman.setRoleId(cursor.getString(3));
				sman.setDisplayName(cursor.getString(4));
				sman.setUnderId(cursor.getString(5));
				sman.setSrType(cursor.getString(6));
				nameList.add(sman);
				cursor.moveToNext();
			}
		}
		else{
			
			System.out.println("No records found");
		}
		cursor.close();
		return nameList;
	}
	
	
}
