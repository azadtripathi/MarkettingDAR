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
import com.dm.library.DateFunction;
import com.dm.model.DemoTransaction;
import com.dm.util.Util;

import java.util.ArrayList;

public class DemoTransactionController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	Context mContext;
	boolean isTrans= false;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_DOC_ID,
			DatabaseConnection.COLUMN_VISIT_NO,
			DatabaseConnection.COLUMN_WEB_DOC_ID,
			DatabaseConnection.COLUMN_ANDROID_DOCID,
			DatabaseConnection.COLUMN_USR_ID,
			DatabaseConnection.COLUMN_VISIT_DATE,
			DatabaseConnection.COLUMN_SREP_CODE,
			DatabaseConnection.COLUMN_PARTY_CODE,
			DatabaseConnection.COLUMN_REMARK,
			DatabaseConnection.COLUMN_AREA_CODE,
			DatabaseConnection.COLUMN_APPLICATION_DETAILS,
			DatabaseConnection.COLUMN_AVAILABILITY_SHOP,
			DatabaseConnection.COLUMN_APP_REMARKS,
			DatabaseConnection.COLUMN_ISPARTY_CONVERTED,
			DatabaseConnection.COLUMN_NEW_APP_AREA,
			DatabaseConnection.COLUMN_TECH_ADVANTAGE,
			DatabaseConnection.COLUMN_TECH_SUGGESTION,
			DatabaseConnection.COLUMN_NEW_APP,
			DatabaseConnection.COLUMN_ORDER_TYPE,
			DatabaseConnection.COLUMN_CLASS_ID,
			DatabaseConnection.COLUMN_SEGMENT,
			DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE,
			DatabaseConnection.COLUMN_ITEM_CODE};
	
	public DemoTransactionController(Context context) {
		mContext = context;
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
		database.close();
	}
	
	public void insertdemoTransaction(DemoTransaction demoTransaction){
		isTrans = true;
		database.beginTransaction();
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_DOC_ID, demoTransaction.getDemoId());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, demoTransaction.getVisId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, demoTransaction.getDemoDocId());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, demoTransaction.getAndroid_id());
		values.put(DatabaseConnection.COLUMN_USR_ID, demoTransaction.getUserId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, demoTransaction.getVDate());
		values.put(DatabaseConnection.COLUMN_SREP_CODE, demoTransaction.getSMId());
		values.put(DatabaseConnection.COLUMN_PARTY_CODE, demoTransaction.getPartyId());
		values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE, demoTransaction.getAndPartyId());
		values.put(DatabaseConnection.COLUMN_REMARK, demoTransaction.getRemarks());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, demoTransaction.getAreaId());
		values.put(DatabaseConnection.COLUMN_APPLICATION_DETAILS, demoTransaction.getCompleteAppDetail());
		values.put(DatabaseConnection.COLUMN_AVAILABILITY_SHOP, demoTransaction.getAvailablityShop());
		values.put(DatabaseConnection.COLUMN_ISPARTY_CONVERTED, demoTransaction.getIsPartyConverted());
		values.put(DatabaseConnection.COLUMN_NEW_APP_AREA, demoTransaction.getNewAppArea());
		values.put(DatabaseConnection.COLUMN_TECH_ADVANTAGE, demoTransaction.getTechAdvantage());
		values.put(DatabaseConnection.COLUMN_TECH_SUGGESTION, demoTransaction.getTechSuggestion());
		values.put(DatabaseConnection.COLUMN_NEW_APP,demoTransaction.getNewApp());
		values.put(DatabaseConnection.COLUMN_ORDER_TYPE, demoTransaction.getOrderType());
		values.put(DatabaseConnection.COLUMN_CLASS_ID, demoTransaction.getProductClassId());
		values.put(DatabaseConnection.COLUMN_SEGMENT, demoTransaction.getProductSegmentId());
		values.put(DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE, demoTransaction.getProductMatGrp());
		values.put(DatabaseConnection.COLUMN_ITEM_CODE, demoTransaction.getItemId());
		values.put(DatabaseConnection.COLUMN_IMAGE_PATH, demoTransaction.getFilePath());
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp( demoTransaction.getVDate()));
		values.put(DatabaseConnection.COLUMN_LAT,demoTransaction.getLatitude());
		values.put(DatabaseConnection.COLUMN_LNG, demoTransaction.getLongitude());
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, demoTransaction.getLatlngTimeStamp());
		
		if(demoTransaction.isNewDemo())
		{
			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
		}
		else
		{
			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
		}
		
	
		try{

			isTrans = true;
			long id = database.insert(DatabaseConnection.TABLE_TRANSDEMO, null, values);
			database.setTransactionSuccessful();
			database.endTransaction();
			isTrans = false;
			System.out.println(id);
		}
		catch(RuntimeException e){
			if(isTrans)
			{
				database.endTransaction();
				isTrans = false;
			}
			System.out.println("+++++++++++++++++++"+e.toString());
		}

	}

	public void insertdemoTransaction(
			String AreaId,
			String Millisecond,
			String Partyid,
			String Remarks,
			String SMID,
			String VisId,
			String VDate,
			String Android_Id,
			String DemoDocId,
			String DemoId,
			String ProductClassId,
			String ProductMatgrp,
			String ProductSegmentId,
			String UserId,boolean isNewDemo,String path,String lat,String longi,String latlongtimestamp

	){

		ContentValues values = new ContentValues();

		boolean flag=false,updateflag=false;
		String qry="select timestamp from TransDemo where Android_id='"+Android_Id+"'";
		System.out.println(qry);
		Cursor cursor = database.rawQuery(qry, null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				String timestamp=cursor.getString(0);
				if(timestamp.equalsIgnoreCase("0")){
					flag=true;
					updateflag=true;
				}
				else
				{
					flag=true;
					updateflag=false;
				}
				cursor.moveToNext();
			}
		}else{
			flag=false;
			values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, Android_Id);
		}
		cursor.close();
//************************************************************

		DsrController dc = new DsrController(mContext);
		dc.open();
		boolean isDsrLock = dc.isDsrLockForVisitDate(VDate);
		dc.close();
		isTrans = true;

		if(isDsrLock)
		{
			values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, Android_Id);
			values.put(DatabaseConnection.COLUMN_DOC_ID, DemoId);
			values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, DemoDocId);
			values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
			values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
			values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
			values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
			values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
			values.put(DatabaseConnection.COLUMN_AREA_CODE, AreaId);
			values.put(DatabaseConnection.COLUMN_CLASS_ID, ProductClassId);
			values.put(DatabaseConnection.COLUMN_SEGMENT, ProductSegmentId);
			values.put(DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE, ProductMatgrp);
			values.put(DatabaseConnection.COLUMN_IMAGE_PATH, path);
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
			/************************		Write By Sandeep Singh 20-04-2017		******************/
			/*****************		START		******************/
			values.put(DatabaseConnection.COLUMN_LAT, lat);
			values.put(DatabaseConnection.COLUMN_LNG, longi);
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongtimestamp);
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, Millisecond);
			long id = database.update(DatabaseConnection.TABLE_TRANSDEMO,
					values, "Android_id='" + Android_Id + "'", null);
			if(id>0)
			{
				//do nothing ...
			}
			else
			{
				id = database.insert(DatabaseConnection.TABLE_TRANSDEMO, null, values);
				System.out.println("row=" + id);
			}
		}
		else {

			values.put(DatabaseConnection.COLUMN_DOC_ID, DemoId);
			values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, DemoDocId);
			values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
			values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
			values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
			values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
			values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
			values.put(DatabaseConnection.COLUMN_AREA_CODE, AreaId);
			values.put(DatabaseConnection.COLUMN_CLASS_ID, ProductClassId);
			values.put(DatabaseConnection.COLUMN_SEGMENT, ProductSegmentId);
			values.put(DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE, ProductMatgrp);
			values.put(DatabaseConnection.COLUMN_IMAGE_PATH, path);
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
			/************************		Write By Sandeep Singh 20-04-2017		******************/
			/*****************		START		******************/
			values.put(DatabaseConnection.COLUMN_LAT, lat);
			values.put(DatabaseConnection.COLUMN_LNG, longi);
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongtimestamp);
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, Millisecond);
			/*****************		END		******************/
			long id = 0;

			try {
				if (flag) {
					if (!updateflag) {
						id = database.update(DatabaseConnection.TABLE_TRANSDEMO,
								values, "Android_id='" + Android_Id + "'", null);

						isTrans = false;
						System.out.println("row=" + id);
					} else {
						CompetitorController.isUpdateFlag = true;
					}

				} else {

					id = database.insert(DatabaseConnection.TABLE_TRANSDEMO, null, values);
					System.out.println("row=" + id);
//							database.setTransactionSuccessful();
//							database.endTransaction();
				}
			} catch (RuntimeException e) {
				if (isTrans) {
					database.endTransaction();
					isTrans = false;
				}
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			values = null;
			cursor = null;
		}
//		***************update code******************************************************
	/*	database.beginTransaction();
		int c=0;
		String qry="select count(*) from TransDemo where Android_id='"+Android_Id+"'";
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
			values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, Android_Id);
		}


/*//************************************************************
		isTrans = true;
		values.put(DatabaseConnection.COLUMN_DOC_ID, DemoId);
		values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, DemoDocId);
		values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
		values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
		values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
		values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
		values.put(DatabaseConnection.COLUMN_AREA_CODE, AreaId);
		values.put(DatabaseConnection.COLUMN_CLASS_ID, ProductClassId);
		values.put(DatabaseConnection.COLUMN_SEGMENT, ProductSegmentId);
		values.put(DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE, ProductMatgrp);
		values.put(DatabaseConnection.COLUMN_IMAGE_PATH, path);
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
		*//************************		Write By Sandeep Singh 20-04-2017		******************//*
		*//*****************		START		******************//*
		values.put(DatabaseConnection.COLUMN_LAT, lat);
		values.put(DatabaseConnection.COLUMN_LNG, longi);
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongtimestamp);
		*//*****************		END		******************//*
		if(isNewDemo)
		{
//			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, Millisecond);
		}
		else
		{
			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction.ConvertDateToTimestamp(VDate+" 00:00:00", "dd/MMM/yyyy 00:00:00"));
		}
		long id=0;
		try{
			if(c>0)
			{
				try {
					/*//**********************	Sandeep Singh 26-04-2017	***************************//*/
					*//*****************	START		*******************//*
					int count=0;
					String qry1="select timestamp from TransDemo where Android_id='"+Android_Id+"'";
					System.out.println(qry1);
					Cursor cursor1 = database.rawQuery(qry1, null);
					if(cursor.moveToFirst()){
						while(!(cursor1.isAfterLast()))
						{
							String timestamp=cursor1.getString(0);
							if(timestamp.equalsIgnoreCase("0")){
								count=+1;
							}
							cursor1.moveToNext();
						}
					}else{
						count+=1;
					}
					cursor1.close();
					if(count>0)
					{
						id = database.update(DatabaseConnection.TABLE_TRANSDEMO,
								values, "Android_id='" +Android_Id + "'", null);
						database.setTransactionSuccessful();
						database.endTransaction();
						isTrans = false;
						System.out.println("row=" + id);
					}
					*//*****************	END		*******************//*
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
					if(isTrans)
					{
						database.endTransaction();
						isTrans = false;
					}
				}


			}
			else{
				try {

					id = database.insert(DatabaseConnection.TABLE_TRANSDEMO, null, values);
					System.out.println("row=" + id);
					database.setTransactionSuccessful();
					database.endTransaction();
				}
				catch(RuntimeException e){
					if(isTrans)
					{
						database.endTransaction();
						isTrans = false;
					}
					System.out.println("+++++++++++++++++++"+e.toString());
				}
			}

		} catch (SQLiteConstraintException constraintException) {
			System.out.println("constraintException="
					+ constraintException.getMessage());


		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}

		values=null;cursor=null;*/
	}



	
	public void updateDemoTransaction(DemoTransaction demoTransaction)
	{

		isTrans = true;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_CLASS_ID, demoTransaction.getProductClassId());
		values.put(DatabaseConnection.COLUMN_SEGMENT, demoTransaction.getProductSegmentId());
		values.put(DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE, demoTransaction.getProductMatGrp());
		values.put(DatabaseConnection.COLUMN_ITEM_CODE, demoTransaction.getItemId());
		values.put(DatabaseConnection.COLUMN_IMAGE_PATH, demoTransaction.getFilePath());
		values.put(DatabaseConnection.COLUMN_REMARK, demoTransaction.getRemarks());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");

		try{
			isTrans = true;
			database.beginTransaction();
			long id = database.update(DatabaseConnection.TABLE_TRANSDEMO, values, DatabaseConnection.COLUMN_ANDROID_DOCID+"='"+demoTransaction.getAndroid_id()+"' and visit_date='"+demoTransaction.getVDate()+"'", null);
			System.out.println(id);
			database.setTransactionSuccessful();
			database.endTransaction();
			isTrans = false;
		}
		catch(RuntimeException e){
			if(isTrans)
			{
				isTrans = false;
				database.endTransaction();
			}
			System.out.println("+++++++++++++++++++"+e.toString());
		}

	}
	
	
	public void deleteDemoTransactionRow(){
		System.out.println("CDB Data deleted");
		database.delete(DatabaseConnection.TABLE_TRANSDEMO,null,null );
		System.out.println("demoTransaction deleted");
	}
	public long getDocId(String date,String partyCode){
		long doc_id = 0;
		String query = "select distinct ifnull(doc_id,0) from Demo where date='"+date+"' and "+ DatabaseConnection.COLUMN_PARTY_CODE+"='"+partyCode+"'";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query,null);
		
		 if (cursor.getCount()==1)
		    {
		            cursor.moveToLast();   
		            doc_id=Long.valueOf(cursor.getString(0));
		            System.out.println("max id found  "+doc_id);
		         
		    }
		 else{
			System.out.println("no max id found");
		}
		 System.out.println("no max id found"+doc_id);
		 cursor.close();
		return doc_id;
	}
	public int getMaxDocIdSNO(String id){
		int doc_id = 0;
		String query = "select ifnull(max(cast(doc_id_sno as integer)),0) as MyColumn from Demo where doc_id='"+id+"'";
		Cursor cursor = database.rawQuery(query,null);
		
		 if (cursor.getCount()==1)
		    {
		            cursor.moveToLast();   
		            doc_id=cursor.getInt(0);
		            System.out.println("max id found "+doc_id);
		         
		    }
		 else{
			System.out.println("no max id found");
		}
		 System.out.println("no max id found"+doc_id);
		 cursor.close();
		return doc_id;
	}
	public String getAndroidDocId(String partyId,String vdate){
		String Adoc_id = "0";
		String query = "select ifnull(Android_id,0) as MyColumn from TransDemo where party_code='"+partyId+"' and visit_date='"+vdate+"'";
		Cursor cursor = database.rawQuery(query,null);
		
		 if (cursor.getCount()==1)
		    {
		            cursor.moveToLast();   
		            Adoc_id=cursor.getString(0);
		            System.out.println("max id found "+Adoc_id);
		         
		    }
		 else{
			System.out.println("no max id found");
		}
		 System.out.println("no max id found"+Adoc_id);
		return Adoc_id;
	}
	
	public String getAndroidDocId(String partyId,String vdate,String apartyId)
	{
		String Adoc_id = "0";
		
		
		String query="";
		if(partyId!=null && apartyId!=null)
		{
			 query = "select ifnull(Android_id,0) as MyColumn from TransDemo where party_code='"+partyId+"' and visit_date='"+vdate+"'";
		}
		else if(partyId!=null && apartyId==null)
		{
			 query = "select ifnull(Android_id,0) as MyColumn from TransDemo where party_code='"+partyId+"' and visit_date='"+vdate+"'";
		}
		else if(partyId==null && apartyId!=null)
		{
			 query = "select ifnull(Android_id,0) as MyColumn from TransDemo where and_party_code='"+apartyId+"' and visit_date='"+vdate+"'";
		}
		
		
		Cursor cursor = database.rawQuery(query,null);
		
		
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	
		            cursor.moveToLast();   
		            Adoc_id=cursor.getString(0);
		            System.out.println("max id found "+Adoc_id);
		            cursor.moveToNext();
		    }
			}
		 else{
			System.out.println("no max id found");
		}
		 System.out.println("no max id found"+Adoc_id);
		return Adoc_id;
	}
	
	
	
	public String getPartyCode(String webCode){
		String doc_id = null;
		String query = "select ifnull(code,'') from partymast where webcode='"+webCode+"'";
		Cursor cursor = database.rawQuery(query,null);
		
		 if (cursor.getCount()==1)
		    {
		            cursor.moveToLast();   
		            doc_id=cursor.getString(0);
		            System.out.println("max id found "+doc_id);
		         
		    }
		 else{
			System.out.println("no max id found");
		}
		 System.out.println("no max id found"+doc_id);
		return doc_id;
	}
//public ArrayList<DemoTransaction> getUploadDemoList(){
//		
////		String query="select d.doc_id,d.doc_id_sno,d.date,p.webcode,d.srep_code,d.area_code,d.beat_code,d.route_code,d.product_code," +
////				"d.qty,d.tech_suggestion,d.availability_shop,d.tech_advantage,d.new_app_area,d.remark,d.application_details from demo d "
////						+" left join visitl1 vl1 on d.date=vl1.date "
////						+" left join partymast p on d.party_code=p.code "
////						+" where vl1.dsr_lock='1' and isMainUpload='0'  and qty>0 ";
//		
//	
//	String query="select d.Android_id,d.usr_id,d.visit_date,d.srep_code,d.party_code,d.remark,d.area_code,d.classid,d.segmentId,d.product_group_code,vl1.visit_no from TransDemo d"+
//                  " LEFT JOIN Visitl1 vl1"+
//              " ON d.visit_date = vl1.visit_date"+
//              " LEFT JOIN mastParty p"+
//              " ON d.party_code = p.webcode"+
//              " WHERE  vl1.dsr_lock = '1' "+
//              " AND d.isUpload = '0'";
//	    System.out.println(query);
//		Cursor cursor=database.rawQuery(query, null);
//		ArrayList<DemoTransaction> demoTransactions = new ArrayList<DemoTransaction>();
//		if(cursor.moveToFirst()){
//			while(!(cursor.isAfterLast()))
//			{						
//				DemoTransaction demoTransaction=new  DemoTransaction();
//				demoTransaction.setAndroid_id(cursor.getString(0));
//				demoTransaction.setUserId(cursor.getString(1));
//				demoTransaction.setVDate(cursor.getString(2));
//				demoTransaction.setSMId(cursor.getString(3));
//				demoTransaction.setPartyId(cursor.getString(4));
//				demoTransaction.setRemarks(cursor.getString(5));
//				demoTransaction.setAreaId(cursor.getString(6));
//				demoTransaction.setProductClassId(cursor.getString(7));
//				demoTransaction.setProductSegmentId(cursor.getString(8));
//				demoTransaction.setProductMatGrp(cursor.getString(9));
//				demoTransaction.setVisId(cursor.getString(10));
//				demoTransactions.add(demoTransaction);
//				cursor.moveToNext();
//			}
//		}else{
//			System.out.println("No records found");
//		}
//		cursor.close();
//		return demoTransactions;
//	} 
public ArrayList<DemoTransaction> getUploadDemoList(String vdate)
{
	
//	String query="select d.doc_id,d.doc_id_sno,d.date,p.webcode,d.srep_code,d.area_code,d.beat_code,d.route_code,d.product_code," +
//			"d.qty,d.tech_suggestion,d.availability_shop,d.tech_advantage,d.new_app_area,d.remark,d.application_details from demo d "
//					+" left join visitl1 vl1 on d.date=vl1.date "
//					+" left join partymast p on d.party_code=p.code "
//					+" where vl1.dsr_lock='1' and isMainUpload='0'  and qty>0 ";


/************************		Write By Sandeep Singh 13-04-2017		******************/
	/*****************		START		******************/
	/************************		Write By Sandeep Singh 20-04-2017		******************/
	/*****************		START		******************/

	String query="select d.Android_id,d.usr_id,d.visit_date,d.srep_code,d.party_code," +
			"d.remark,d.area_code,d.classid,d.segmentId,d.product_group_code," +
			"vl1.visit_no,d.path , ifnull(d.latitude,0) as latitude,ifnull(d.longitude,0) as longitude,ifnull(d.LatlngTime,0) as LatlngTime from TransDemo d"+
			" LEFT JOIN Visitl1 vl1"+
			" ON d.visit_date = vl1.visit_date"+
			" LEFT JOIN mastParty p"+
			" ON d.party_code = p.webcode"+
			" WHERE "+
			" d.timestamp = '0' AND d.visit_date = '"+vdate+"'";

	/*String query="select d.Android_id,d.usr_id,d.visit_date,d.srep_code,d.party_code," +
			"d.remark,d.area_code,d.classid,d.segmentId,d.product_group_code," +
			"vl1.visit_no,d.path from TransDemo d"+
			" LEFT JOIN Visitl1 vl1"+
			" ON d.visit_date = vl1.visit_date"+
			" LEFT JOIN mastParty p"+
			" ON d.party_code = p.webcode"+
			" WHERE  vl1.dsr_lock = '1' "+
			" AND d.timestamp = '0' AND d.visit_date = '"+vdate+"'";*/
	/*****************		END		******************/
	/*String query="select d.Android_id,d.usr_id,d.visit_date,d.srep_code,d.party_code," +
			"d.remark,d.area_code,d.classid,d.segmentId,d.product_group_code," +
			"vl1.visit_no,d.path from TransDemo d"+
			" LEFT JOIN Visitl1 vl1"+
			" ON d.visit_date = vl1.visit_date"+
			" LEFT JOIN mastParty p"+
			" ON d.party_code = p.webcode"+
			" WHERE  vl1.dsr_lock = '1' "+
			" AND d.isUpload = '0' AND d.visit_date = '"+vdate+"'";*/
	/*****************		END		******************/
    System.out.println(query);
	Cursor cursor=database.rawQuery(query, null);
	ArrayList<DemoTransaction> demoTransactions = new ArrayList<DemoTransaction>();
	if(cursor.moveToFirst()){
		while(!(cursor.isAfterLast()))
		{						
			DemoTransaction demoTransaction=new DemoTransaction();
			demoTransaction.setAndroid_id(cursor.getString(0));
			demoTransaction.setUserId(cursor.getString(1));
			demoTransaction.setVDate(cursor.getString(2));
			demoTransaction.setSMId(cursor.getString(3));
			demoTransaction.setPartyId(cursor.getString(4));
			demoTransaction.setRemarks(cursor.getString(5));
			demoTransaction.setAreaId(cursor.getString(6));
			demoTransaction.setProductClassId(cursor.getString(7));
			demoTransaction.setProductSegmentId(cursor.getString(8));
			demoTransaction.setProductMatGrp(cursor.getString(9));
			demoTransaction.setVisId(cursor.getString(10));

			String file_path = (cursor.getString(11));
			if(file_path.equalsIgnoreCase("Na") || file_path.isEmpty() || file_path == null)
			{
				demoTransaction.setFilePath("");
			}
			else
			{
				demoTransaction.setFilePath((cursor.getString(11)));
			}
//			demoTransaction.setFilePath(cursor.getString(11));
			/************************		Write By Sandeep Singh 20-04-2017		******************/
			/*****************		START		******************/
			demoTransaction.setLatitude(Util.validateAlfa(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT))));
			demoTransaction.setLongitude(Util.validateNumric(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LNG))));
			demoTransaction.setLatlngTimeStamp(Util.validateNumric(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT_LNG_TIME))));
			/*****************		END		******************/
			demoTransactions.add(demoTransaction);
			cursor.moveToNext();
		}
	}else{
		System.out.println("No records found");
	}
	cursor.close();
	return demoTransactions;
} 
public ArrayList<DemoTransaction> getUploadSampleList(){
	
	String query="select d.doc_id,d.doc_id_sno,d.date,p.webcode,d.srep_code,d.area_code,d.beat_code,d.route_code,d.product_code," +
			"d.samp_qty,d.tech_suggestion,d.availability_shop,d.tech_advantage,d.new_app_area,d.remark,d.application_details from demo d "
					+" left join visitl1 vl1 on d.date=vl1.date "
					+" left join partymast p on d.party_code=p.code "
					+" where vl1.dsr_lock='1' and isSubUpload='0'  and samp_qty>0 ";
	System.out.println(query);
	Cursor cursor=database.rawQuery(query, null);
	ArrayList<DemoTransaction> demoTransactions = new ArrayList<DemoTransaction>();
	if(cursor.moveToFirst()){
		while(!(cursor.isAfterLast()))
		{						
			DemoTransaction demoTransaction=new DemoTransaction();
//			demoTransaction.setDoc_id(cursor.getString(0));
//			demoTransaction.setDoc_id_sno(cursor.getInt(1));
//			demoTransaction.setDate(cursor.getString(2));
//			demoTransaction.setParty_code(cursor.getString(3));
//			demoTransaction.setSrep_code(cursor.getString(4));
//			demoTransaction.setArea_code(cursor.getString(5));
//			demoTransaction.setBeat_code(cursor.getString(6));
//			demoTransaction.setRoute_code(cursor.getString(7));
//			demoTransaction.setProduct_code(cursor.getString(8));
//			demoTransaction.setSamp_qty(cursor.getString(9));
//			demoTransaction.setTech_suggestion(cursor.getString(10));
//			demoTransaction.setAvailability_shop(cursor.getString(11));
//			demoTransaction.setTech_advantage(cursor.getString(12));
//			demoTransaction.setNew_app_area(Boolean.valueOf(cursor.getString(13)));
//			demoTransaction.setRemark(cursor.getString(14));
//			demoTransaction.setApplication_details(cursor.getString(15));
			demoTransactions.add(demoTransaction);
			cursor.moveToNext();
		}
	}else{
		System.out.println("No records found");
	}
	cursor.close();
	return demoTransactions;
}
/************************		Write By Sandeep Singh 10-04-2017		******************/
	/*****************		START		******************/
	public  long updateDemoOrder(String androiddocId, String webdocId,int dno,int vId,String timeStamp){
		/*****************		END		******************/
//public  long updateDemoOrder(String androiddocId, String webdocId,int dno,int vId){
		long id=0;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_UPLOAD,"1");
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID,webdocId);
		values.put(DatabaseConnection.COLUMN_DOC_ID,dno);
		values.put(DatabaseConnection.COLUMN_VISIT_NO,vId);

		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
		/*****************		END		******************/
		try{
			id = database.update(DatabaseConnection.TABLE_TRANSDEMO, values, "Android_id='"+androiddocId+"'", null);
			System.out.println("row="+id);
		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		return id;
	}
public  void updateSampleOrder(String doc_id, int doc_id_sno){
	ContentValues values = new ContentValues();
	values.put(DatabaseConnection.COLUMN_isSubUPLOAD,"1");
	try{
	long id = database.update(DatabaseConnection.TABLE_TRANSDEMO, values, "doc_id='"+doc_id+"' and doc_id_sno="+doc_id_sno, null);
	System.out.println("row="+id);
	} 
	catch(RuntimeException e){
		System.out.println("+++++++++++++++++++"+e.toString());
	}
}

public DemoTransaction getDemoByAndroidId(String androidId)
{
	String query="select remark,classid,segmentId,product_group_code,ifnull(path,'NA'),Android_id,isUpload,web_doc_id from TransDemo where Android_id='"+androidId+"'";
	System.out.println(query);
	
	Cursor cursor=database.rawQuery(query, null);
	DemoTransaction demo=new DemoTransaction();
	if(cursor.moveToFirst()){
		while(!(cursor.isAfterLast()))
		{						
			demo.setWebDocId(cursor.getString(7));
			demo.setRemarks(cursor.getString(0));
			demo.setProductClassId(cursor.getString(1));
			demo.setProductSegmentId(cursor.getString(2));
			demo.setProductMatGrp(cursor.getString(3));
			demo.setFilePath(cursor.getString(4));
			demo.setAndroid_id(cursor.getString(5));
			demo.setIsUpload(cursor.getString(6));

			cursor.moveToNext();
		}
	}else{
		System.out.println("No records found");
	}
	cursor.close();
	return demo;
}

public ArrayList<DemoTransaction> getDemoListByParty(String partyId, String apartyId)
{
	String query="";
	if(partyId!=null && apartyId!=null)
	{
		query="select mc.name,ms.name,mp.name,d.remark,d.party_code," +
			  "d.visit_date,d.web_doc_id,d.Android_id,d.path,d.party_code,d.and_party_code " +
			  "from transdemo d " +
			  "left join mastclass mc on d.classid=mc.webcode " +
			  "left join mastsegment ms on d.segmentid=ms.webcode  " +
			  "left join MastProdGroup mp on d.product_group_code=mp.webcode " +
			  "where d.party_code='"+partyId+"'";


	}
	else if(partyId!=null && apartyId==null)
	{
		query="select mc.name,ms.name,mp.name,d.remark,d.party_code," +
				  "d.visit_date,d.web_doc_id,d.Android_id,d.path,d.party_code,d.and_party_code " +
				  "from transdemo d " +
				  "left join mastclass mc on d.classid=mc.webcode " +
				  "left join mastsegment ms on d.segmentid=ms.webcode  " +
				  "left join MastProdGroup mp on d.product_group_code=mp.webcode " +
				  "where d.party_code='"+partyId+"'";
	}
	else if(partyId==null && apartyId!=null)
	{
		query="select mc.name,ms.name,mp.name,d.remark,d.party_code," +
				  "d.visit_date,d.web_doc_id,d.Android_id,d.path,d.party_code,d.and_party_code " +
				  "from transdemo d " +
				  "left join mastclass mc on d.classid=mc.webcode " +
				  "left join mastsegment ms on d.segmentid=ms.webcode  " +
				  "left join MastProdGroup mp on d.product_group_code=mp.webcode " +
				  "where d.and_party_code='"+apartyId+"'";
	}
	
	
	 
	System.out.println(query);
	Cursor cursor=database.rawQuery(query, null);
	ArrayList<DemoTransaction> demos=new  ArrayList<DemoTransaction>();
	if(cursor.moveToFirst()){
		while(!(cursor.isAfterLast()))
		{						
			DemoTransaction demo=new DemoTransaction();
			demo.setProductClassId(cursor.getString(0));
			demo.setProductSegmentId(cursor.getString(1));
			demo.setProductMatGrp(cursor.getString(2));
			demo.setRemarks(cursor.getString(3));
			demo.setPartyId(cursor.getString(4));
			demo.setVDate(cursor.getString(5));
			demo.setDemoDocId(cursor.getString(6));
			demo.setAndroid_id(cursor.getString(7));
			String file_path = (cursor.getString(8));
			if(file_path.equalsIgnoreCase("Na") || file_path.isEmpty() || file_path == null)
			{
				demo.setFilePath("");
			}
			else
			{
				demo.setFilePath((cursor.getString(8)));
			}
			demo.setPartyId(cursor.getString(9));
			demo.setAndPartyId(cursor.getString(10));
			demos.add(demo);
			cursor.moveToNext();
		}
	}else{
		System.out.println("No records found");
	}
	cursor.close();
	return demos;
}


	public ArrayList<DemoTransaction> getDemoListByParty(String partyId, String apartyId, String vdate)
	{
		String query="";
		if(partyId!=null && apartyId!=null)
		{

			query="select mc.name,ms.name,mp.name,d.remark,d.party_code," +
					"d.visit_date,d.web_doc_id,d.Android_id,d.path,d.party_code,d.and_party_code " +
					"from transdemo d " +
					"left join mastclass mc on d.classid=mc.webcode " +
					"left join mastsegment ms on d.segmentid=ms.webcode  " +
					"left join MastProdGroup mp on d.product_group_code=mp.webcode " +
					"where d.party_code='"+partyId+"' and d.visit_date='"+vdate+"'";


		}
		else if(partyId!=null && apartyId==null)
		{
			query="select mc.name,ms.name,mp.name,d.remark,d.party_code," +
					"d.visit_date,d.web_doc_id,d.Android_id,d.path,d.party_code,d.and_party_code " +
					"from transdemo d " +
					"left join mastclass mc on d.classid=mc.webcode " +
					"left join mastsegment ms on d.segmentid=ms.webcode  " +
					"left join MastProdGroup mp on d.product_group_code=mp.webcode " +
					"where d.party_code='"+partyId+"' and d.visit_date='"+vdate+"'";
		}
		else if(partyId==null && apartyId!=null)
		{
			query="select mc.name,ms.name,mp.name,d.remark,d.party_code," +
					"d.visit_date,d.web_doc_id,d.Android_id,d.path,d.party_code,d.and_party_code " +
					"from transdemo d " +
					"left join mastclass mc on d.classid=mc.webcode " +
					"left join mastsegment ms on d.segmentid=ms.webcode  " +
					"left join MastProdGroup mp on d.product_group_code=mp.webcode " +
					"where d.and_party_code='"+apartyId+"' and d.visit_date='"+vdate+"'";
		}



		System.out.println(query);
		Cursor cursor=database.rawQuery(query, null);
		ArrayList<DemoTransaction> demos=new  ArrayList<DemoTransaction>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				DemoTransaction demo=new DemoTransaction();
				demo.setProductClassId(cursor.getString(0));
				demo.setProductSegmentId(cursor.getString(1));
				demo.setProductMatGrp(cursor.getString(2));
				demo.setRemarks(cursor.getString(3));
				demo.setPartyId(cursor.getString(4));
				demo.setVDate(cursor.getString(5));
				demo.setDemoDocId(cursor.getString(6));
				demo.setAndroid_id(cursor.getString(7));

				String file_path = (cursor.getString(8));
				if(file_path == null || file_path.equalsIgnoreCase("Na")||file_path.equalsIgnoreCase("N/a") || file_path.isEmpty())
				{
					demo.setFilePath("");
				}
				else
				{
					demo.setFilePath((cursor.getString(8)));
				}

				demo.setPartyId(cursor.getString(9));
				demo.setAndPartyId(cursor.getString(10));
				demos.add(demo);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return demos;
	}

public int getNoOfDemo(String date)
{
	int sum=0;
	ArrayList<Integer> productiveVisit=new ArrayList<Integer>();
	String query1="select count(distinct party_code) as NoOfdemo1 from TransDemo  where visit_date='"+date+"'";
	String query2="select count(distinct and_party_code) as NoOfdemo2 from TransDemo  where visit_date='"+date+"' and party_code IS NULL";
	Cursor cursor1=database.rawQuery(query1, null);
if(cursor1.moveToFirst()){
		while(!(cursor1.isAfterLast()))
		{						
			
			productiveVisit.add(0,cursor1.getInt(0));
			cursor1.moveToNext();
		}
	}else{
		System.out.println("No records found");
	}
cursor1.close();
Cursor cursor2=database.rawQuery(query2, null);
if(cursor2.moveToFirst()){
	while(!(cursor2.isAfterLast()))
	{	
		productiveVisit.add(1,cursor2.getInt(0));
		cursor2.moveToNext();
	}
}else{
	System.out.println("No records found");
}
cursor2.close();

sum=productiveVisit.get(0)+productiveVisit.get(1);
	
	return sum;
}




	public boolean isAndroidIdExist(String android_doc_id)
	{
		Cursor c = database.query(DatabaseConnection.TABLE_TRANSDEMO,null,"Android_id='" +android_doc_id + "'",null,null,null,null);
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

