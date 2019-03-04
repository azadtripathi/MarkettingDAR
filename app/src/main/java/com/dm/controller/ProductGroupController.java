package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.Owner;
import com.dm.model.ProductGroup;

import java.util.ArrayList;

public class ProductGroupController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;Context context;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_WEB_CODE ,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_CREATED_DATE
	};
	
	public ProductGroupController(Context context) {
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

	public void insertProductGroup(	String Nm,
									   String Id,String timeStamp,String active){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, Id);
		values.put(DatabaseConnection.COLUMN_NAME, Nm.toUpperCase());
		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
		values.put(DatabaseConnection.COLUMN_ACTIVE, active);
		/*****************		END		******************/
		try{
			Cursor c = database.query(DatabaseConnection.TABLE_PRODUCTGROUPMAST,null,"webcode=?",new String[]{Id},null,null,null);
			if(c.getCount()>0)
			{
				long id = database.update(DatabaseConnection.TABLE_PRODUCTGROUPMAST, values, "webcode=?",new String[]{Id});
			}
			else {
				long id = database.insert(DatabaseConnection.TABLE_PRODUCTGROUPMAST, null, values);
			}
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void insertProductGroup(ProductGroup productgroup){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, productgroup.getGroup_id());
		values.put(DatabaseConnection.COLUMN_NAME, productgroup.getGroup_name().toUpperCase());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, productgroup.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, productgroup.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, productgroup.getCreatedDate());
		try{
		long id = database.insert(DatabaseConnection.TABLE_PRODUCTGROUPMAST, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	public ArrayList<ProductGroup> getProductGroupList(){
		String orderby= DatabaseConnection.COLUMN_NAME;
	Cursor cursor = database.query(DatabaseConnection.TABLE_PRODUCTGROUPMAST,
				allColumns, null, null, null, null, orderby);
		ArrayList<ProductGroup> productGroups = new ArrayList<ProductGroup>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				ProductGroup group=new ProductGroup();
				group.setGroup_id(cursor.getString(0));
				group.setGroup_name(cursor.getString(1));
				productGroups.add(group);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return productGroups;
	}

	public ArrayList<Owner> getProductGroupListAddContact(){
		String orderby= DatabaseConnection.COLUMN_NAME;
		Cursor cursor = database.query(DatabaseConnection.TABLE_PRODUCTGROUPMAST,
				allColumns, null, null, null, null, orderby);
		ArrayList<Owner> productGroups = new ArrayList<Owner>();
		try {
			Owner owner=new Owner();
			owner.setId("0");
			owner.setName("--Select--");
			productGroups.add(owner);
		} catch (Exception e) {}
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				Owner owner=new Owner();
				owner.setId(cursor.getString(0));
				owner.setName(cursor.getString(1));
				productGroups.add(owner);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return productGroups;
	}


}