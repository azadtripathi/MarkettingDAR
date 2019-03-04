package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.ProductSubGroup;

import java.util.ArrayList;

public class ProductSubGroupsController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_WEB_CODE, DatabaseConnection.COLUMN_NAME, DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE};
	
	public ProductSubGroupsController(Context context) {
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
	
	public void insertProductSubGroup(ProductSubGroup productsubgroup){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, productsubgroup.getSubgroup_id());
		values.put(DatabaseConnection.COLUMN_NAME , productsubgroup.getSubgroup_name());
		values.put(DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE, productsubgroup.getGroup_id());
		try{
//		long id = database.insert(DatabaseConnection.TABLE_PRODUCTSUBGROUPMAST, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
public ArrayList<ProductSubGroup> getProductSubGroupList(String productGropuCode){
		
		Cursor cursor = database.query(DatabaseConnection.TABLE_PRODUCTGROUPMAST,
				allColumns, DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE+"='"+productGropuCode+"'", null, null, null, null);
		ArrayList<ProductSubGroup> productSubGroups = new ArrayList<ProductSubGroup>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				ProductSubGroup Subgroup=new ProductSubGroup();
				Subgroup.setSubgroup_id(cursor.getString(0));
				Subgroup.setSubgroup_name(cursor.getString(1));
				Subgroup.setGroup_id(cursor.getString(2));
				productSubGroups.add(Subgroup);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return productSubGroups;
	}
	}