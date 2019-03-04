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

import java.util.ArrayList;
import java.util.Arrays;

public class AreaController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_CITY_CODE,
			DatabaseConnection.COLUMN_AREATYPE_CODE,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_CREATED_DATE
	};
	
	public AreaController(Context context) {
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
	
	public void insertArea(Area area){

		int c=0;
		String qry="select count(*) from MastArea where webcode="+area.getArea_id();
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
			values.put(DatabaseConnection.COLUMN_WEB_CODE, area.getArea_id());
		}
		values.put(DatabaseConnection.COLUMN_NAME, area.getArea_name());
		values.put(DatabaseConnection.COLUMN_CITY_CODE, area.getCity_id());
		values.put(DatabaseConnection.COLUMN_AREATYPE_CODE, area.getType());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, area.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, area.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, area.getCreatedDate());

		long id=0;
		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_AREAMAST,
							values, "webcode='" + area.getArea_id() + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_AREAMAST, null, values);
					System.out.println("row=" + id);
				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
				}
			}


		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void insertAreaParameter(String aid,String nm,String cid,String ms){

		int c=0;
		String qry="select count(*) from MastArea where webcode="+aid;
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
			values.put(DatabaseConnection.COLUMN_WEB_CODE, aid);
		}
		values.put(DatabaseConnection.COLUMN_NAME, nm);
		values.put(DatabaseConnection.COLUMN_CITY_CODE, cid);
//		values.put(DatabaseConnection.COLUMN_AREATYPE_CODE, area.getType());
//		values.put(DatabaseConnection.COLUMN_SYNC_ID, area.getSync_id());
//		values.put(DatabaseConnection.COLUMN_ACTIVE, area.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, ms);

		long id=0;
		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_AREAMAST,
							values, "webcode='" + aid + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_AREAMAST, null, values);
					System.out.println("row=" + id);
				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
				}
			}


		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}

		values=null;aid=null;cid=null;nm=null;ms=null;

	}

	public ArrayList<Area> getAreaList(){
		
//		Cursor cursor = database.query(DatabaseConnection.TABLE_AREAMAST,
//				allColumns, null, null, null, null, null);
//	
		String qry=" select * from MastArea order by name";
		Cursor cursor = database.rawQuery(qry, null);
		ArrayList<Area> areaArray = new ArrayList<Area>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Area area = new Area();
				area.setArea_id(cursor.getString(0));
				area.setArea_name(cursor.getString(1));
				area.setCity_id(cursor.getString(3));
				area.setType(cursor.getString(2));
				areaArray.add(area);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return areaArray;
	}
public ArrayList<Area> getAreaList(String areaId, int key ){
		
//		Cursor cursor = database.query(DatabaseConnection.TABLE_AREAMAST,
//				allColumns, null, null, null, null, null);
//	
		String qry = "";
	if(key == 0)
	{
		qry  ="select * from MastArea where  city_code in(" + areaId + ") order by name";
	}
	if(key != 0 && key != 6)
	{
		qry  ="select * from MastArea where  webcode in(" + areaId + ") order by name";
	}
	if(key==6)
	{
		qry  ="select * from MastArea";
	}
		System.out.println(qry);
	ArrayList<Area> areaArray = new ArrayList<Area>();
		Cursor cursor = database.rawQuery(qry, null);

		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Area area = new Area();
				area.setArea_id(cursor.getString(0));
				area.setArea_name(cursor.getString(1));
				area.setCity_id(cursor.getString(3));
				area.setType(cursor.getString(2));
				areaArray.add(area);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return areaArray;
	}


	public int findAreaFound(String areaId ){

//		Cursor cursor = database.query(DatabaseConnection.TABLE_AREAMAST,
//				allColumns, null, null, null, null, null);
		int count = 0;
		String qry="select name from MastArea where webcode='"+areaId+"'";
		System.out.println(qry);
		Cursor cursor = database.rawQuery(qry, null);
		count = cursor.getCount();

		cursor.close();
		return count;
	}

	public int deleteAreaRight(String areaId[] ){

//		Cursor cursor = database.query(DatabaseConnection.TABLE_AREAMAST,
//				allColumns, null, null, null, null, null);
		String areaids = Arrays.toString(areaId);
		areaids = areaids.replaceAll("\\[","");
		areaids = areaids.replaceAll("\\]","");

		int count = 0;
		String qry="delete from MastArea where webcode NOT IN('"+areaids+"')";
		System.out.println(qry);
		Cursor cursor = database.rawQuery(qry, null);
		count = cursor.getCount();
		cursor.close();
		return count;
	}
	public String getAreaName(String areaId ){

//		Cursor cursor = database.query(DatabaseConnection.TABLE_AREAMAST,
//				allColumns, null, null, null, null, null);
	String nm="";
		String qry="select name from MastArea where webcode='"+areaId+"'";
		System.out.println(qry);
		Cursor cursor = database.rawQuery(qry, null);
		ArrayList<Area> areaArray = new ArrayList<Area>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{

				nm=cursor.getString(0);

				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return nm;
	}

	public ArrayList<Area> getAreaListCityWise(String cityId ){
		String qry="select * from MastArea where  city_code ='"+cityId+"' order by name";
		System.out.println(qry);
		Cursor cursor = database.rawQuery(qry, null);
		ArrayList<Area> areaArray = new ArrayList<Area>();
		Area area1 = new Area();
		area1.setArea_id("0");
		area1.setArea_name("--Select--");
		area1.setCity_id("0");
		area1.setType("");
		areaArray.add(area1);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				Area area = new Area();
				area.setArea_id(cursor.getString(0));
				area.setArea_name(cursor.getString(1));
				area.setCity_id(cursor.getString(3));
				area.setType(cursor.getString(2));
				areaArray.add(area);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return areaArray;
	}
public String getCityCode(String areaId)
{
	String cityId="";
	String query="select city_code from mastArea where webcode='"+areaId+"'";
	System.out.println(query);
	Cursor cursor = database.rawQuery(query, null);
	if(cursor.moveToFirst()){
		while(!(cursor.isAfterLast()))
		{

			cityId=cursor.getString(0);
			cursor.moveToNext();
		}
	}
	else{
		System.out.println("No records found");
	}
	cursor.close();
	return cityId;

}




}
