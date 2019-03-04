package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.Porder;

import java.util.ArrayList;


public class PorderController {

	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_PURCHASE_ORDERID,
			DatabaseConnection.COLUMN_PURCHASEORDER_DOCID,
			DatabaseConnection.COLUMN_ANDROID_DOCID,
			DatabaseConnection.COLUMN_VISIT_DATE,
			DatabaseConnection.COLUMN_USR_ID,
			DatabaseConnection.COLUMN_SREP_CODE,
			DatabaseConnection.COLUMN_DISTRIBUTER_ID,
			DatabaseConnection.COLUMN_REMARK,
			DatabaseConnection.COLUMN_TRANSPORTER,
			DatabaseConnection.COLUMN_PROJECT_TYPE,
			DatabaseConnection.COLUMN_PROJECTID,
			DatabaseConnection.COLUMN_SCHEMEID,
			DatabaseConnection.COLUMN_DISPATCH_NAME,
			DatabaseConnection.COLUMN_ADDRESS1,
			DatabaseConnection.COLUMN_ADDRESS2,
			DatabaseConnection.COLUMN_DISPATCH_CITY,
			DatabaseConnection.COLUMN_PIN,
			DatabaseConnection.COLUMN_DISPATCH_STATE,
			DatabaseConnection.COLUMN_DISPATCH_COUNTRY,
			DatabaseConnection.COLUMN_PHONE,
			DatabaseConnection.COLUMN_MOBILE,
			DatabaseConnection.COLUMN_AMOUNT,
			DatabaseConnection.COLUMN_EMAIL
			
			
		};
	
	public PorderController(Context context) {
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
	
	public long insertPOrder(Porder pOrder){
		ContentValues values = new ContentValues();
		long id=0;
		values.put(DatabaseConnection.COLUMN_PURCHASE_ORDERID, pOrder.getPOrdId());
		values.put(DatabaseConnection.COLUMN_PURCHASEORDER_DOCID, pOrder.getPODocId());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, pOrder.getAndroidcode());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, pOrder.getVdate());
		values.put(DatabaseConnection.COLUMN_USR_ID,pOrder.getUserId());
		values.put(DatabaseConnection.COLUMN_SREP_CODE,pOrder.getSMId());
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID,pOrder.getDistId());
		values.put(DatabaseConnection.COLUMN_REMARK,pOrder.getRemarks());
		values.put(DatabaseConnection.COLUMN_TRANSPORTER,pOrder.getTransporter());
		values.put(DatabaseConnection.COLUMN_PROJECT_TYPE,pOrder.getProjectType());
		values.put(DatabaseConnection.COLUMN_PROJECTID,pOrder.getProjectId());
		values.put(DatabaseConnection.COLUMN_SCHEMEID,pOrder.getSchemeId());
		values.put(DatabaseConnection.COLUMN_DISPATCH_NAME,pOrder.getDispName());
		values.put(DatabaseConnection.COLUMN_ADDRESS1,pOrder.getDispAdd1());
		values.put(DatabaseConnection.COLUMN_ADDRESS2,pOrder.getDispAdd2());
		values.put(DatabaseConnection.COLUMN_DISPATCH_CITY,pOrder.getDispCity());
		values.put(DatabaseConnection.COLUMN_PIN,pOrder.getDispPin());
		values.put(DatabaseConnection.COLUMN_DISPATCH_STATE,pOrder.getDispState());
		values.put(DatabaseConnection.COLUMN_DISPATCH_COUNTRY,pOrder.getDispCountry());
		values.put(DatabaseConnection.COLUMN_PHONE,pOrder.getDispPhone());
		values.put(DatabaseConnection.COLUMN_MOBILE,pOrder.getDispMobile());
		values.put(DatabaseConnection.COLUMN_EMAIL,pOrder.getDispEmail());
		values.put(DatabaseConnection.COLUMN_AMOUNT,pOrder.getOrderValue());
		values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
		System.out.println("row no "+ pOrder.getPODocId()+" inserted");
		try{
		 id = database.insert(DatabaseConnection.TABLE_PORDER, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		return id;
	}
	
	
	public ArrayList<Porder> getUploadPorderList(){
		String query="select * from Porder where isUpload='0'";
		System.out.println(query);
		Cursor cursor=database.rawQuery(query, null);
		ArrayList<Porder> porders = new ArrayList<Porder>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{						
				Porder porder=new Porder();
				porder.setAndroidcode(cursor.getString(2));
				porder.setVdate(cursor.getString(3));
				porder.setUserId(cursor.getString(4));
				porder.setSMId(cursor.getString(5));
				porder.setDistId(cursor.getString(6));
				porder.setRemarks(cursor.getString(7));
				porder.setTransporter(cursor.getString(8));
				porder.setProjectType(cursor.getString(9));
				porder.setProjectId(cursor.getString(10));
				porder.setSchemeId(cursor.getString(11));
				porder.setDispName(cursor.getString(12));
				porder.setDispAdd1(cursor.getString(13));
				porder.setDispAdd2(cursor.getString(14));
				porder.setDispCity(cursor.getString(15));
				porder.setDispPin(cursor.getString(16));
				porder.setDispState(cursor.getString(17));
				porder.setDispCountry(cursor.getString(18));
				porder.setDispPhone(cursor.getString(19));
				porder.setDispMobile(cursor.getString(20));
				porder.setDispEmail(cursor.getString(21));
				porder.setOrderValue(cursor.getString(22));
				porders.add(porder);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return porders;
	}
	

	public  void updatePOrderUpload(String androiddocId, String webdocId,int ono,String timeStamp){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_UPLOAD,"1");
		values.put(DatabaseConnection.COLUMN_PURCHASEORDER_DOCID,webdocId);
		values.put(DatabaseConnection.COLUMN_PURCHASE_ORDERID,ono);
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
//		values.put(DatabaseConnection.COLUMN_VISIT_NO,vId);
		try{
		long id = database.update(DatabaseConnection.TABLE_PORDER, values, "Android_id='"+androiddocId+"'", null);
		System.out.println("row="+id);
		if(id>0)
		{
			
			ContentValues values1 = new ContentValues();	
			values1.put(DatabaseConnection.COLUMN_PURCHASEORDER_DOCID,webdocId);
			values1.put(DatabaseConnection.COLUMN_PURCHASE_ORDERID,ono);
		long id1 = database.update(DatabaseConnection.TABLE_PORDER1, values1, "Android_id='"+androiddocId+"'", null);
		System.out.println("row="+id1);	
		}
		
		
		} 
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	

	public float getTotalDayPrimary(String date)
	{
		float sum=0;
		ArrayList<Float> dayPrimary=new ArrayList<Float>();
		
		String query1="select ifNull(sum(amount),0)  as pamount1 from Porder where visit_date='"+date+"'";
		Cursor cursor1=database.rawQuery(query1, null);
	if(cursor1.moveToFirst()){
			while(!(cursor1.isAfterLast()))
			{						
				
				dayPrimary.add(0,cursor1.getFloat(0));
				cursor1.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
	cursor1.close();
	
	sum=dayPrimary.get(0);
		
		return sum;
	}





	public boolean isAndroidIdExist(String android_doc_id)
	{
		Cursor c = database.query(DatabaseConnection.TABLE_PORDER,null,"Android_id='" +android_doc_id + "'",null,null,null,null);
		if(c.getCount()>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}


public String totalFailedVisit(String vDate)
{
	String sum = "0";
	String query = "Select * from "+DatabaseConnection.TABLE_TRANSFAILED_VISIT+" where visit_date='"+vDate+"' and DistId is null ";
	Cursor c = database.rawQuery(query,null);
	sum  = String.valueOf(c.getCount());
	return sum;
}
public String numberOfRowsInOrder(String vDate)
{
	String sum = "0";
	String query = "Select * from "+DatabaseConnection.TABLE_ORDER+" where visit_date='"+vDate+"'";
	Cursor c = database.rawQuery(query,null);
	sum  = String.valueOf(c.getCount());
	return sum;
}

	public String sumOfAmount(String vDate)
	{
		String sum = "0";
		String query = "Select sum(amount) as Total_Amt from "+DatabaseConnection.TABLE_PORDER+" where visit_date='"+vDate+"'";
		Cursor c = database.rawQuery(query,null);
		if(c.getCount()>0)
		{
			if(c.moveToLast()) {
				sum = c.getString(0);
			}
		}

		if(sum==null)
		{
			sum = "0";
		}
		return sum;
	}

	public String sumOfQty(String vDate)
	{
		String sum = "0";
		String query = "Select sum(qty) as Total_Qty from "+DatabaseConnection.TABLE_PORDER1+" where visit_date='"+vDate+"'";
		Cursor c = database.rawQuery(query,null);
		if(c.moveToLast()) {
			sum = c.getString(0);
		}

		if(sum==null)
		{
			sum = "0";
		}
		return sum;
	}

	public String sumOfSAmount(String vDate)
	{
		String sum = "0";
		String query = "Select sum(amount) as Total_Amt from "+DatabaseConnection.TABLE_ORDER+" where visit_date='"+vDate+"'";
		Cursor c = database.rawQuery(query,null);
		if(c.getCount()>0)
		{
			if(c.moveToLast()) {
				sum = c.getString(0);
			}

			if(sum==null)
			{
				sum = "0";
			}
		}
		return sum;
	}

	public String sumOfSQty(String vDate)
	{
		String sum = "0";
		String query = "Select sum(qty) as Total_Qty from "+DatabaseConnection.TABLE_ORDER1+" where visit_date='"+vDate+"'";
		Cursor c = database.rawQuery(query,null);
		if(c.moveToLast()) {
			sum = c.getString(0);
		}

		if(sum==null)
		{
			sum = "0";
		}
		return sum;
	}


	public String totalPartyCreated(String vDate,String creator)
	{
		String sum = "0";
		String query = "Select * from "+DatabaseConnection.TABLE_PARTYMAST+" where CreatedDate='"+vDate+"' and CREATED_BY='"+creator+"'";
		Cursor c = database.rawQuery(query,null);
		sum = String.valueOf(c.getCount());
		return sum;
	}

}
