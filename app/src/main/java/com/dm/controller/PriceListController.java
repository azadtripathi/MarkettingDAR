package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.PriceList;

public class PriceListController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_WEF_DATE,
			DatabaseConnection.COLUMN_PRODUCT_CODE,
			DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE,
			DatabaseConnection.COLUMN_MRP,
			DatabaseConnection.COLUMN_DP,
			DatabaseConnection.COLUMN_RP
			};
	
	public PriceListController(Context context) {
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
	
	public void insertPriceList(PriceList pricelist){
		ContentValues values = new ContentValues();
		//values.put(DatabaseConnection.COLUMN_PRICE_LIST_ID,pricelist.getPricelist_id());
		values.put(DatabaseConnection.COLUMN_WEF_DATE, pricelist.getList_date());
		values.put(DatabaseConnection.COLUMN_PRODUCT_CODE, pricelist.getPricelist_id());
		values.put(DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE, pricelist.getProductGroup_Id());
		values.put(DatabaseConnection.COLUMN_MRP, pricelist.getMrp());
		values.put(DatabaseConnection.COLUMN_DP, pricelist.getDp());
		values.put(DatabaseConnection.COLUMN_RP, pricelist.getRp());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, pricelist.getCreatedDate());
		
		try{
		long id = database.insert(DatabaseConnection.TABLE_PRICELIST, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void insertPriceList(
			String Createddate,
			String ProdGrpId,
			String DP,
			String ProdId,
			String WefDate,
			String MRP,
			int Id,
			String RP

	){
		ContentValues values = new ContentValues();
		//values.put(DatabaseConnection.COLUMN_PRICE_LIST_ID,pricelist.getPricelist_id());
		values.put(DatabaseConnection.COLUMN_WEF_DATE, WefDate);
		values.put(DatabaseConnection.COLUMN_PRODUCT_CODE, Id);
		values.put(DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE, ProdGrpId);
		values.put(DatabaseConnection.COLUMN_MRP, MRP);
		values.put(DatabaseConnection.COLUMN_DP, DP);
		values.put(DatabaseConnection.COLUMN_RP, RP);
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, Createddate);

		try{
			long id = database.insert(DatabaseConnection.TABLE_PRICELIST, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
}