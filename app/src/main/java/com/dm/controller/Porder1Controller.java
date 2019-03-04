package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.Porder1;

import java.util.ArrayList;

public class Porder1Controller {
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_PURCHASE_ORDER1ID,
			DatabaseConnection.COLUMN_PORDER1_ANDROID_DOCID,
			DatabaseConnection.COLUMN_PURCHASE_ORDERID,
			DatabaseConnection.COLUMN_PURCHASEORDER_DOCID,
			DatabaseConnection.COLUMN_ANDROID_DOCID,
			DatabaseConnection.COLUMN_ANDROID_SERIAL_NO,
			DatabaseConnection.COLUMN_SERIAL_NO,
			DatabaseConnection.COLUMN_VISIT_DATE,
			DatabaseConnection.COLUMN_DISTRIBUTER_ID,
			DatabaseConnection.COLUMN_USR_ID,
			DatabaseConnection.COLUMN_PRODUCT_CODE,
			DatabaseConnection.COLUMN_QTY,
			DatabaseConnection.COLUMN_DISCRIPTION,
			DatabaseConnection.COLUMN_REMARK,
			DatabaseConnection.COLUMN_RATE
			};
	
	public Porder1Controller(Context context) {
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
	
	public long insertPOrder(Porder1 pOrder1){
		ContentValues values = new ContentValues();
		long id=0;
		values.put(DatabaseConnection.COLUMN_PURCHASE_ORDER1ID, pOrder1.getPOrd1Id());
		values.put(DatabaseConnection.COLUMN_PORDER1_ANDROID_DOCID, pOrder1.getPord1Androidcode());
		values.put(DatabaseConnection.COLUMN_PURCHASE_ORDERID, pOrder1.getPOrdId());
		values.put(DatabaseConnection.COLUMN_PURCHASEORDER_DOCID, pOrder1.getPODocId());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, pOrder1.getAndroidcode());
		values.put(DatabaseConnection.COLUMN_ANDROID_SERIAL_NO, pOrder1.getSno());
		values.put(DatabaseConnection.COLUMN_SERIAL_NO,pOrder1.getSno());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, pOrder1.getVdate());
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID,pOrder1.getDistId());
		values.put(DatabaseConnection.COLUMN_USR_ID,pOrder1.getUserId());
		values.put(DatabaseConnection.COLUMN_PRODUCT_CODE,pOrder1.getItemId());
		values.put(DatabaseConnection.COLUMN_QTY,pOrder1.getQty());
		values.put(DatabaseConnection.COLUMN_DISCRIPTION,pOrder1.getDisc());
		values.put(DatabaseConnection.COLUMN_REMARK,pOrder1.getRemarks());
		values.put(DatabaseConnection.COLUMN_RATE,pOrder1.getRate());
		values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
		System.out.println("row no "+ pOrder1.getPODocId()+" inserted");
		try{
		 id = database.insert(DatabaseConnection.TABLE_PORDER1, null, values);
		
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		return id;
	}

	public ArrayList<Porder1> getUploadPorder1List(){
		String query="select * from Porder1 where isUpload='0'";
		System.out.println(query);
		Cursor cursor=database.rawQuery(query, null);
		ArrayList<Porder1> porders = new ArrayList<Porder1>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{						
				Porder1 porder=new Porder1();
				porder.setPord1Androidcode(cursor.getString(1));
				porder.setPOrdId(cursor.getString(2));
				porder.setPODocId(cursor.getString(3));
				porder.setAndroidcode(cursor.getString(4));
				porder.setSno(Integer.parseInt(cursor.getString(5)));
				porder.setVdate(cursor.getString(7));
				porder.setDistId(cursor.getString(8));
				porder.setUserId(cursor.getString(9));
				porder.setItemId(cursor.getString(10));
				porder.setQty(cursor.getString(11));
				porder.setRemarks(cursor.getString(13));
				porder.setRate(cursor.getString(14));
				porders.add(porder);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return porders;
	}
	

	public  void updatePOrder1Upload(String androiddocId){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_UPLOAD,"1");
//		values.put(DatabaseConnection.COLUMN_PURCHASE_ORDER1ID,ono);
		
//		values.put(DatabaseConnection.COLUMN_VISIT_NO,vId);
		try{
		long id = database.update(DatabaseConnection.TABLE_PORDER1, values, "Pord1Android_id='"+androiddocId+"'", null);
		System.out.println("row="+id);
				
		} 
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}


	public boolean isAndroidIdExist(String android_doc_id)
	{
		Cursor c = database.query(DatabaseConnection.TABLE_PORDER1,null,DatabaseConnection.COLUMN_PORDER1_ANDROID_DOCID+"='" +android_doc_id + "'",null,null,null,null);
		if(c.getCount()>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	
}
