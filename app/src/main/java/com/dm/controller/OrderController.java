package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.library.DateFunction;
import com.dm.model.Order;
import com.dm.util.Util;

import java.util.ArrayList;


public class OrderController {

	Context mContext;
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	boolean mTrans = false;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_ORDER_NO,
			DatabaseConnection.COLUMN_VISIT_NO,
			DatabaseConnection.COLUMN_WEB_DOC_ID,
			DatabaseConnection.COLUMN_ANDROID_DOCID,
			DatabaseConnection.COLUMN_USR_ID,
			DatabaseConnection.COLUMN_VISIT_DATE,
			DatabaseConnection.COLUMN_SREP_CODE,
			DatabaseConnection.COLUMN_PARTY_CODE,
			DatabaseConnection.COLUMN_AREA_CODE,
			DatabaseConnection.COLUMN_REMARK,
			DatabaseConnection.COLUMN_AMOUNT,
			DatabaseConnection.COLUMN_STATUS,
			DatabaseConnection.COLUMN_MEET_FLAG,
			DatabaseConnection.COLUMN_MEET_DOCID,
			DatabaseConnection.COLUMN_ORDER_TYPE
						
		};
	
	public OrderController(Context context) {
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
		dbHelper.close();
	}
	
	public long insertOrder(Order order){

		mTrans = true;
		database.beginTransaction();
		long id=0;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ORDER_NO, order.getOrdId());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, order.getVisId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, order.getOrdDocId());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, order.getAndroid_id());
		values.put(DatabaseConnection.COLUMN_USR_ID,order.getUserId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE,order.getVDate());
		values.put(DatabaseConnection.COLUMN_SREP_CODE,order.getSMId());
		values.put(DatabaseConnection.COLUMN_PARTY_CODE,order.getPartyId());
		values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE,order.getAndPartyId());
		values.put(DatabaseConnection.COLUMN_AREA_CODE,order.getAreaId());
		values.put(DatabaseConnection.COLUMN_REMARK,order.getRemarks());
		values.put(DatabaseConnection.COLUMN_AMOUNT,order.getOrderAmount());
		values.put(DatabaseConnection.COLUMN_STATUS,order.getOrderStatus());
		values.put(DatabaseConnection.COLUMN_MEET_FLAG,order.getMeetFlag());
		values.put(DatabaseConnection.COLUMN_MEET_DOCID,order.getMeetDocId());
		values.put(DatabaseConnection.COLUMN_ORDER_TYPE,order.getOrderType());
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(order.getVDate()));
		if(order.getIsOrderImport())
		{
			values.put(DatabaseConnection.COLUMN_isSubUPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP,0);
		}
		else{
			values.put(DatabaseConnection.COLUMN_isSubUPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
		}

		values.put(DatabaseConnection.COLUMN_LAT,order.getLatitude());
		values.put(DatabaseConnection.COLUMN_LNG,order.getLongitude());
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,order.getLatlngTime());

		System.out.println("row no "+ order.getOrdDocId()+" inserted");
		try{


		 id = database.insert(DatabaseConnection.TABLE_ORDER, null, values);
			database.setTransactionSuccessful();
			database.endTransaction();
			mTrans = false;
		} catch(RuntimeException e){
			if(mTrans)
			{
				database.endTransaction();
				mTrans = false;
			}
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		return id;
	}

	public void insertOrder(
			String AreaId,
			String Millisecond,
			String Partyid,
			String Remarks,
			String SMID,
			String VisId,
			String VDate,
			String Android_Id,
			String OrdDocId,
			String OrderAmount,
			String UserId,
			String OrdId,
			boolean getIsOrderImport,

			String latitude,
			String longitude,
			String latlngTimestamp

	){


		mTrans = true;boolean flag=false,updateflag=false;
		ContentValues values = new ContentValues();
		String qry="select timestamp from TransOrder where Android_id='"+Android_Id+"'";
		System.out.println(qry);

		DsrController dc = new DsrController(mContext);
		dc.open();
		boolean isDsrLock = dc.isDsrLockForVisitDate(VDate);
		dc.close();
		if(isDsrLock)
		{
			values.put(DatabaseConnection.COLUMN_ORDER_NO, OrdId);
			values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, OrdDocId);
			values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
			values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
			values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
			values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
			values.put(DatabaseConnection.COLUMN_AREA_CODE, AreaId);
			values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
			values.put(DatabaseConnection.COLUMN_AMOUNT, OrderAmount);
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
			values.put(DatabaseConnection.COLUMN_LAT, latitude);
			values.put(DatabaseConnection.COLUMN_LNG, longitude);
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlngTimestamp);
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, Millisecond);
			values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, Android_Id);
			long id = database.update(DatabaseConnection.TABLE_ORDER,
					values, "Android_id='" + Android_Id + "'", null);
			System.out.print(""+id);
			if(id>0)
			{
				//do nothing
			}
			else
			{
				long id1 = database.insert(DatabaseConnection.TABLE_ORDER,
						null,values);
				System.out.print(""+id1);
			}
		}
		else {
			Cursor cursor = database.rawQuery(qry, null);
			if (cursor.moveToFirst()) {
				while (!(cursor.isAfterLast())) {
					String timestamp = cursor.getString(0);
					if (timestamp.equalsIgnoreCase("0")) {
						flag = true;
						updateflag = true;
					} else {
						flag = true;
						updateflag = false;
					}
					cursor.moveToNext();
				}
			} else {
				flag = false;
				values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, Android_Id);
			}
			cursor.close();
//**************************************************************************************
			mTrans = true;
			long id = 0;
			values.put(DatabaseConnection.COLUMN_ORDER_NO, OrdId);
			values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, OrdDocId);
			values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
			values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
			values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
			values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
			values.put(DatabaseConnection.COLUMN_AREA_CODE, AreaId);
			values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
			values.put(DatabaseConnection.COLUMN_AMOUNT, OrderAmount);
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
			values.put(DatabaseConnection.COLUMN_LAT, latitude);
			values.put(DatabaseConnection.COLUMN_LNG, longitude);
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlngTimestamp);
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, Millisecond);
			try {
				if (flag) {
					if (!updateflag) {
						id = database.update(DatabaseConnection.TABLE_ORDER,
								values, "Android_id='" + Android_Id + "'", null);

						mTrans = false;
						System.out.println("row=" + id);
					} else {

						CompetitorController.isUpdateFlag = true;
						mTrans = false;
					}

				} else {

					id = database.insert(DatabaseConnection.TABLE_ORDER, null, values);
					System.out.println("row=" + id);

				}
			} catch (RuntimeException e) {
				if (mTrans) {

					mTrans = false;
				}
				System.out.println("+++++++++++++++++++" + e.toString());
			}

			values = null;
			cursor = null;
		}
//		***************update code******************************************************
/*		database.beginTransaction();
		mTrans = true;
		int c=0;
		String qry="select count(*) from TransOrder where Android_id='"+Android_Id+"'";
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
		{}else{values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, Android_Id);	}

/*//**************************************************************************************

		long id=0;
		values.put(DatabaseConnection.COLUMN_ORDER_NO, OrdId);
		values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, OrdDocId);
		values.put(DatabaseConnection.COLUMN_USR_ID,UserId);
		values.put(DatabaseConnection.COLUMN_VISIT_DATE,VDate);
		values.put(DatabaseConnection.COLUMN_SREP_CODE,SMID);
		values.put(DatabaseConnection.COLUMN_PARTY_CODE,Partyid);
		values.put(DatabaseConnection.COLUMN_AREA_CODE,AreaId);
		values.put(DatabaseConnection.COLUMN_REMARK,Remarks);
		values.put(DatabaseConnection.COLUMN_AMOUNT,OrderAmount);
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
		values.put(DatabaseConnection.COLUMN_LAT,latitude);
		values.put(DatabaseConnection.COLUMN_LNG,longitude);
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,latlngTimestamp);

		if(getIsOrderImport)
		{
//			values.put(DatabaseConnection.COLUMN_isSubUPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP,Millisecond);

		}
		else{
			values.put(DatabaseConnection.COLUMN_isSubUPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction.ConvertDateToTimestamp(VDate+" 00:00:00", "dd/MMM/yyyy 00:00:00"));
		}

//		try{
//			id = database.insert(DatabaseConnection.TABLE_ORDER, null, values);
//		} catch(RuntimeException e){
//			System.out.println("+++++++++++++++++++"+e.toString());
//		}


		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_ORDER,
							values, "Android_id='" +Android_Id + "'", null);
					System.out.println("row=" + id);

					database.setTransactionSuccessful();
					database.endTransaction();
					mTrans = false;
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
					if(mTrans)
					{
						database.endTransaction();
						mTrans = false;
					}
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_ORDER, null, values);
					database.setTransactionSuccessful();
					database.endTransaction();
					mTrans = false;
					System.out.println("row=" + id);

				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
					if(mTrans)
					{
						database.endTransaction();
						mTrans = false;
					}
				}
			}

		} catch (SQLiteConstraintException constraintException) {
			System.out.println("constraintException="
					+ constraintException.getMessage());


		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}

		values=null;cursor=null;*/

		//return id;
	}

	public String getAndroidDocId(String partyId,String vdate){
		String Adoc_id = "0";
		String query="";
		
		
		
		
		
		 query = "select ifnull(Android_id,0) as MyColumn from TransOrder where party_code='"+partyId+"' and visit_date='"+vdate+"'";
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
			 query = "select ifnull(Android_id,0) as MyColumn from TransOrder where party_code='"+partyId+"' and visit_date='"+vdate+"'";
		}
		else if(partyId!=null && apartyId==null)
		{
			 query = "select ifnull(Android_id,0) as MyColumn from TransOrder where party_code='"+partyId+"' and visit_date='"+vdate+"'";
		}
		else if(partyId==null && apartyId!=null)
		{
			 query = "select ifnull(Android_id,0) as MyColumn from TransOrder where and_party_code='"+apartyId+"' and visit_date='"+vdate+"'";
		}
		
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


	public long updateOrder(String headerId,Order order)
	{
		long id = 0;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ORDER_NO, order.getOrdId());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, order.getVisId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, order.getOrdDocId());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, order.getAndroid_id());
		values.put(DatabaseConnection.COLUMN_USR_ID,order.getUserId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE,order.getVDate());
		values.put(DatabaseConnection.COLUMN_SREP_CODE,order.getSMId());
		values.put(DatabaseConnection.COLUMN_PARTY_CODE,order.getPartyId());
		values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE,order.getAndPartyId());
		values.put(DatabaseConnection.COLUMN_AREA_CODE,order.getAreaId());
		values.put(DatabaseConnection.COLUMN_REMARK,order.getRemarks());
		values.put(DatabaseConnection.COLUMN_AMOUNT,order.getOrderAmount());
		values.put(DatabaseConnection.COLUMN_STATUS,order.getOrderStatus());
		values.put(DatabaseConnection.COLUMN_MEET_FLAG,order.getMeetFlag());
		values.put(DatabaseConnection.COLUMN_MEET_DOCID,order.getMeetDocId());
		values.put(DatabaseConnection.COLUMN_ORDER_TYPE,order.getOrderType());
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(order.getVDate()));
		if(order.getIsOrderImport())
		{
			values.put(DatabaseConnection.COLUMN_isSubUPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP,0);

		}
		else{
			values.put(DatabaseConnection.COLUMN_isSubUPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
		}

		values.put(DatabaseConnection.COLUMN_LAT,order.getLatitude());
		values.put(DatabaseConnection.COLUMN_LNG,order.getLongitude());
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,order.getLatlngTime());

		try{
			 id = database.update(DatabaseConnection.TABLE_ORDER, values, DatabaseConnection.COLUMN_ANDROID_DOCID+"='"+headerId+"'", null);
			System.out.println(id);
		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		return id;
	}
	public void updateOrder(Order order)
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_REMARK,order.getRemarks());
		values.put(DatabaseConnection.COLUMN_AMOUNT,order.getOrderAmount());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");

		try{
			long id = database.update(DatabaseConnection.TABLE_ORDER, values, DatabaseConnection.COLUMN_ANDROID_DOCID+"='"+order.getAndroid_id()+"'", null);
			System.out.println(id);
		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
//
//	String query="update TransOrder set amount='"+order.getOrderAmount()+"' , remark='"+order.getRemarks()+"' where Android_id='"+order.getAndroid_id()+"'";
//		System.out.println(query);
//	try {
//		// String
//		// qurey="UPDATE order_sub SET remark="+""+" WHERE ExecutionDate = '2011-10-12 00:00:00.000'";
//
//		database.execSQL(query);
//		System.out.println(" Order updated");
//	} catch (RuntimeException e) {
//		System.out.println("+++++++++++++++++++" + e.toString());
//	}
	
	}
	
//	public ArrayList<Order> getUploadMainList(){
//		String query="select o.Android_id,o.usr_id,o.visit_date,o.srep_code,o.party_code,o.area_code,o.remark,o.amount,vl1.visit_no"+
//					 " from TransOrder o"+
//					 " LEFT JOIN Visitl1 vl1"+
//					 " ON o.visit_date = vl1.visit_date"+
//					 " LEFT JOIN mastParty p"+
//					 " ON o.party_code = p.webcode"+
//					 " WHERE  vl1.dsr_lock = '1'"+
//                     " AND o.isSubUpload = '0'";
//		System.out.println(query);
//		Cursor cursor=database.rawQuery(query, null);
//		ArrayList<Order> orders = new ArrayList<Order>();
//		if(cursor.moveToFirst()){
//			while(!(cursor.isAfterLast()))
//			{						
//				Order order=new  Order();
//				order.setAndroid_id(cursor.getString(0));
//				order.setUserId(cursor.getString(1));
//				order.setVDate(cursor.getString(2));
//				order.setSMId(cursor.getString(3));
//				order.setPartyId(cursor.getString(4));
//				order.setAreaId(cursor.getString(5));
//				order.setRemarks(cursor.getString(6));
//				order.setOrderAmount(cursor.getString(7));
//				order.setVisId(cursor.getString(8));
//				orders.add(order);
//				cursor.moveToNext();
//			}
//		}else{
//			System.out.println("No records found");
//		}
//		cursor.close();
//		return orders;
//	}
	
	public ArrayList<Order> getUploadMainList(String vdate){
		/************************		Write By Sandeep Singh 13-04-2017		******************/
		/*****************		START		******************/
		/************************		Write By Sandeep Singh 20-04-2017		******************/
		/*****************		START		******************/
		String query="select o.Android_id,o.usr_id,o.visit_date,o.srep_code,o.party_code," +
				"o.area_code,o.remark,o.amount,vl1.visit_no, ifnull(o.latitude,0) as latitude,ifnull(o.longitude,0) as longitude,ifnull(o.LatlngTime,0) as LatlngTime"+
				" from TransOrder o"+
				" LEFT JOIN Visitl1 vl1"+
				" ON o.visit_date = vl1.visit_date"+
				" LEFT JOIN mastParty p"+
				" ON o.party_code = p.webcode"+
				" WHERE "+
				" o.timestamp =0 AND o.visit_date = '"+vdate+"'";
	/*	String query="select o.Android_id,o.usr_id,o.visit_date,o.srep_code,o.party_code," +
				"o.area_code,o.remark,o.amount,vl1.visit_no"+
				" from TransOrder o"+
				" LEFT JOIN Visitl1 vl1"+
				" ON o.visit_date = vl1.visit_date"+
				" LEFT JOIN mastParty p"+
				" ON o.party_code = p.webcode"+
				" WHERE  vl1.dsr_lock = '1'"+
				" AND o.timestamp =0 AND o.visit_date = '"+vdate+"'";*/
		/*****************		END		******************/

	/*	String query="select o.Android_id,o.usr_id,o.visit_date,o.srep_code,o.party_code," +
				"o.area_code,o.remark,o.amount,vl1.visit_no"+
				" from TransOrder o"+
				" LEFT JOIN Visitl1 vl1"+
				" ON o.visit_date = vl1.visit_date"+
				" LEFT JOIN mastParty p"+
				" ON o.party_code = p.webcode"+
				" WHERE  vl1.dsr_lock = '1'"+
				" AND o.isSubUpload = '0' AND o.visit_date = '"+vdate+"'";*/
		/*****************		END		******************/
	/*	String query="select o.Android_id,o.usr_id,o.visit_date,o.srep_code,o.party_code," +
				"o.area_code,o.remark,o.amount,vl1.visit_no"+
				" from TransOrder o"+
				" LEFT JOIN Visitl1 vl1"+
				" ON o.visit_date = vl1.visit_date"+
				" LEFT JOIN mastParty p"+
				" ON o.party_code = p.webcode"+
				" WHERE  vl1.dsr_lock = '1'"+
				" AND o.isSubUpload = '0' AND o.visit_date = '"+vdate+"'";*/
		/*****************		END		******************/
		System.out.println(query);
		Cursor cursor=database.rawQuery(query, null);
		ArrayList<Order> orders = new ArrayList<Order>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{						
				Order order=new Order();
				order.setAndroid_id(cursor.getString(0));
				order.setUserId(cursor.getString(1));
				order.setVDate(cursor.getString(2));
				order.setSMId(cursor.getString(3));
				order.setPartyId(cursor.getString(4));
				order.setAreaId(cursor.getString(5));
				order.setRemarks(cursor.getString(6));
				order.setOrderAmount(cursor.getString(7));
				order.setVisId(cursor.getString(8));
				/************************		Write By Sandeep Singh 20-04-2017		******************/
				/*****************		START		******************/
				order.setLatitude(Util.validateNumric(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT))));
				order.setLongitude(Util.validateNumric(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LNG))));
				order.setLatlngTime(Util.validateNumric(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT_LNG_TIME))));
				/*****************		END		******************/
				orders.add(order);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return orders;
	}



	//public  long updateOrderUpload(String androiddocId, String webdocId,int ono,int vId){
	/************************		Write By Sandeep Singh 10-04-2017		******************/
	/*****************		START		******************/
	public  long updateOrderUpload(String androiddocId, String webdocId,int ono,int vId,String timeStamp){
		/*****************		END		******************/
		long id=0;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_isSubUPLOAD,"1");
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID,webdocId);
		values.put(DatabaseConnection.COLUMN_ORDER_NO,ono);
		values.put(DatabaseConnection.COLUMN_VISIT_NO,vId);


		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
		/*****************		END		******************/
		try{
			id = database.update(DatabaseConnection.TABLE_ORDER, values, "Android_id='"+androiddocId+"'", null);
			System.out.println("row="+id);
		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		return id;
	}

	//public  boolean updateMultipleOrderUpload(String androiddocId, String webdocId,int ono,int vId){
	/************************		Write By Sandeep Singh 10-04-2017		******************/
	/*****************		START		******************/
	public  boolean updateMultipleOrderUpload(String androiddocId, String webdocId,int ono,int vId,String timeStamp){
		/*****************		END		******************/
		ContentValues values = new ContentValues();
		boolean status=false;
		long id=0,id1=0;
		values.put(DatabaseConnection.COLUMN_isSubUPLOAD,"1");
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID,webdocId);
		values.put(DatabaseConnection.COLUMN_ORDER_NO,ono);
		values.put(DatabaseConnection.COLUMN_VISIT_NO,vId);

		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
		/*****************		END		******************/
		try{
			id = database.update(DatabaseConnection.TABLE_ORDER, values, "Android_id='"+androiddocId+"'", null);
			System.out.println("row="+id);
			if(id>0)
			{

				ContentValues values1 = new ContentValues();
				values1.put(DatabaseConnection.COLUMN_WEB_DOC_ID,webdocId);
				values1.put(DatabaseConnection.COLUMN_ORDER1_NO,ono);
				id1 = database.update(DatabaseConnection.TABLE_ORDER1, values1, "Android_id='"+androiddocId+"'", null);
				System.out.println("row="+id1);
			}


		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		if(id>0 && id1>0)
		{status=false;}else{status=true;}
		return status;
	}


	public ArrayList<Order> getOrderByAndroidId(String androidId)
	{
		String query="select amount,remark,isSubUpload from TransOrder where Android_id='"+androidId+"' OR web_doc_id='"+androidId+"'";
		System.out.println("getOrderByAndroidId:"+query);
		Cursor cursor=database.rawQuery(query, null);
		ArrayList<Order> orders = new ArrayList<Order>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{						
				Order order=new Order();
				order.setOrderAmount(cursor.getString(0));
				order.setRemarks(cursor.getString(1));
				order.setIsUpload(cursor.getString(2));
				orders.add(order);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return orders;
	}
	
	public ArrayList<Order> getOrderPartyWiseList(String party_code, String aparty_code, String vdate){
		String query="";
		if(party_code!=null && aparty_code!=null)	
		{
			 query="select td.visit_date as visitdate," +
						"td.remark as remark,td.amount as amount,"+
						"ma.name as areaname,td.Android_id as androidId,td.web_doc_id as orderDocId "+
						"from TransOrder td "+
						"left join MastArea ma on ma.webcode=td.area_code "+
						"where td.party_code='"+party_code+"' and td.visit_date='"+vdate+"'";
			System.out.println("Query Order List1"+query);
		}
		else if(party_code!=null && aparty_code==null)
	{
		
			 query="select td.visit_date as visitdate," +
						"td.remark as remark,td.amount as amount,"+
						"ma.name as areaname,td.Android_id as androidId,td.web_doc_id as orderDocId "+
						"from TransOrder td "+
						"left join MastArea ma on ma.webcode=td.area_code "+
						"where td.party_code='"+party_code+"' and td.visit_date='"+vdate+"'";

		System.out.println("Query Order List2"+query);
	}
	
	else if(party_code==null && aparty_code!=null)
	{
	
		 query="select td.visit_date as visitdate," +
					"td.remark as remark,td.amount as amount,"+
					"ma.name as areaname,td.Android_id as androidId,td.web_doc_id as orderDocId "+
					"from TransOrder td "+
					"left join MastArea ma on ma.webcode=td.area_code "+
					"where td.and_party_code='"+aparty_code+"' and td.visit_date='"+vdate+"'";
		System.out.println("Query Order List3"+query);
	}
	
		
				

		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Order> orderTransactions = new ArrayList<Order>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Order orderTransaction = new Order();
				orderTransaction.setVDate(cursor.getString(0));
				orderTransaction.setRemarks(cursor.getString(1));
				orderTransaction.setOrderAmount(cursor.getString(2));
				orderTransaction.setAreaId(cursor.getString(3));
				orderTransaction.setAndroid_id(cursor.getString(4));
				orderTransaction.setOrdDocId(cursor.getString(5));
				orderTransactions.add(orderTransaction);
			    cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}
	
	
	
	public int getNoOfProductiveVisit(String date)
	{
		int sum=0;
		ArrayList<Integer> productiveVisit=new ArrayList<Integer>();
		String query1="select count(distinct party_code) as NoOfProductiveVisit1 from TransOrder  where visit_date='"+date+"'";
		String query2="select count(distinct and_party_code) as NoOfProductiveVisit2 from TransOrder  where visit_date='"+date+"' and party_code IS NULL";
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


	public String getDaySecondary(String date)
	{
		String sum="";
		String query1="select sum(amount) as samount from transorder where visit_date='"+date+"'";
		Cursor cursor1=database.rawQuery(query1, null);
	if(cursor1.moveToFirst()){
			while(!(cursor1.isAfterLast()))
			{						
				
				sum=cursor1.getString(0);
				cursor1.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
	cursor1.close();
		return sum;
	}


	public boolean isAndroidIdExist(String android_doc_id)
	{
		Cursor c = database.query(DatabaseConnection.TABLE_ORDER,null,"Android_id='" +android_doc_id + "'",null,null,null,null);
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
