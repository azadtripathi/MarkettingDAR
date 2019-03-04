package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.Transporter;
import com.dm.model.Vehicle;

import java.util.ArrayList;

public class TransportController {

	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {	
			DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_CREATED_DATE
	};
	
	public TransportController(Context context) {
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
	
	public void insertTransport(Transporter transport){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, transport.getTransport_id());
		values.put(DatabaseConnection.COLUMN_NAME, transport.getTransport_name());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, transport.getSync_id());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, transport.getCreatedDate());
		try{
		long id = database.insert(DatabaseConnection.TABLE_TRANSPORTER, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void insertTransport(String Id,
			String Nm,String MS){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, Id);
		values.put(DatabaseConnection.COLUMN_NAME, Nm);
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,MS);

		try{
			Cursor c = database.query(DatabaseConnection.TABLE_TRANSPORTER,null,"webcode=?",new String[]{Id},null,null,null,null);
			if(c.getCount()>0)
			{
				database.update(DatabaseConnection.TABLE_TRANSPORTER,values,"webcode=?",new String[]{Id});
			}
			else {
				long id = database.insert(DatabaseConnection.TABLE_TRANSPORTER, null, values);
			}
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void insertTransportData(String Trpt,String MS)
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_NAME, Trpt);
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,MS);
		try
		{
			long id = database.insert(DatabaseConnection.TABLE_TRANSPORT, null, values);

		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void insertVehicle(Vehicle transport){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_NAME, transport.getName());
		try{
			long id = database.insert(DatabaseConnection.TABLE_VEHICLE, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	public void insertVehicle(String Trpt,String ms){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_NAME, Trpt);
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,ms);
		try{
			long id = database.insert(DatabaseConnection.TABLE_VEHICLE, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}


	public ArrayList<Transporter> getTransporterNameList()
	{
		ArrayList<Transporter> transportnameList=new ArrayList<Transporter>();
		String query="select webcode,name from mastTransport order by name";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	Transporter transporter=new Transporter();
			    transporter.setTransport_id(cursor.getString(0));
			    transporter.setTransport_name(cursor.getString(1));
				transportnameList.add(transporter);
				cursor.moveToNext();
			}
		}
		else{
			
			System.out.println("No records found");
		}
		cursor.close();
		return transportnameList;
	}

	public ArrayList<Transporter> getTransportList()
	{
		ArrayList<Transporter> transportnameList=new ArrayList<Transporter>();
		String query="select name from Transport order by name";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	Transporter transporter=new Transporter();
				transporter.setTransport_name(cursor.getString(0));
				transportnameList.add(transporter);
				cursor.moveToNext();
			}
		}
		else{

			System.out.println("No records found");
		}
		cursor.close();
		return transportnameList;
	}

	public ArrayList<Vehicle> getVehicleList()
	{
		ArrayList<Vehicle> vehiclenameList=new ArrayList<Vehicle>();
		String query="select name from Vehicle order by name";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	Vehicle vehicle=new Vehicle();
				vehicle.setName(cursor.getString(0));
				vehiclenameList.add(vehicle);
				cursor.moveToNext();
			}
		}
		else{

			System.out.println("No records found");
		}
		cursor.close();
		return vehiclenameList;
	}
	
	
	
}
