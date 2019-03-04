package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.ProductClass;

import java.util.ArrayList;


public class ProductClassController {
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;Context context;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_WEB_CODE ,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_CREATED_DATE
	};
	
	public ProductClassController(Context context) {
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

	public void close() {
		dbHelper.close();
	}
	
	public void insertProductClass(ProductClass productclass){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, productclass.getClass_id());
		values.put(DatabaseConnection.COLUMN_NAME, productclass.getClass_name().toUpperCase());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, productclass.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, productclass.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, productclass.getCreatedDate());
		try{
		long id = database.insert(DatabaseConnection.TABLE_PRODUCTCLASSMAST, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void insertProductClass(
			String Id,
			String NM,
			String MS

	){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, Id);
		values.put(DatabaseConnection.COLUMN_NAME, NM.toUpperCase());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, MS);
		try{
			long id = database.insert(DatabaseConnection.TABLE_PRODUCTCLASSMAST, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public ArrayList<ProductClass> getProductClassList(){
		String orderby= DatabaseConnection.COLUMN_NAME;
	Cursor cursor = database.query(DatabaseConnection.TABLE_PRODUCTCLASSMAST,
				allColumns, null, null, null, null, orderby);
		ArrayList<ProductClass> productclass = new ArrayList<ProductClass>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{		
				ProductClass class1=new ProductClass();
				class1.setClass_id(cursor.getString(0));
				class1.setClass_name(cursor.getString(1));
				productclass.add(class1);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return productclass;
	}
}
