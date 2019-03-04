package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.library.DateFunction;
import com.dm.model.TransCollection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TransCollectionController {

	File myFile;
	 FileOutputStream fOut;
	 OutputStreamWriter myOutWriter;
	 SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	boolean mTranse = false;
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	Context mContext;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_ID,
			DatabaseConnection.COLUMN_VISIT_NO,
			DatabaseConnection.COLUMN_WEB_DOC_ID,
			DatabaseConnection.COLUMN_ANDROID_DOCID,
			DatabaseConnection.COLUMN_USR_ID,
			DatabaseConnection.COLUMN_VISIT_DATE,
			DatabaseConnection.COLUMN_PARTY_CODE,
			DatabaseConnection.COLUMN_SREP_CODE,
			DatabaseConnection.COLUMN_AREA_CODE,
			DatabaseConnection.COLUMN_ITEM_CODE,
			DatabaseConnection.COLUMN_MODE,
			DatabaseConnection.COLUMN_AMOUNT,
			DatabaseConnection.COLUMN_PAYMENT_DATE,
			DatabaseConnection.COLUMN_CHEQUE_DDNO,
			DatabaseConnection.COLUMN_CHQ_DATE,
			DatabaseConnection.COLUMN_BANK,
			DatabaseConnection.COLUMN_BRANCH,
			DatabaseConnection.COLUMN_REMARK,
			DatabaseConnection.COLUMN_UPLOAD};
	
	public TransCollectionController(Context context) {
		mContext =context;
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


	/*

			Vinayak Start Work on 20April2017
	 */
	public long insertTransCollection(TransCollection tcollection){
		long id=-1;
		mTranse = true;
		//database.beginTransaction();
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ID, tcollection.getCollId());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, tcollection.getVisId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, tcollection.getCollDocId());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, tcollection.getAndroid_id());
		values.put(DatabaseConnection.COLUMN_USR_ID, tcollection.getUserId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, tcollection.getVDate());
		values.put(DatabaseConnection.COLUMN_PARTY_CODE, tcollection.getPartyId());
		values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE, tcollection.getAndPartyId());
		values.put(DatabaseConnection.COLUMN_SREP_CODE, tcollection.getSMId());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, tcollection.getAreaId());
		values.put(DatabaseConnection.COLUMN_ITEM_CODE, tcollection.getItemId());
		values.put(DatabaseConnection.COLUMN_MODE, tcollection.getMode());
		values.put(DatabaseConnection.COLUMN_AMOUNT, tcollection.getAmount());
		values.put(DatabaseConnection.COLUMN_PAYMENT_DATE, tcollection.getPaymentDate());
		values.put(DatabaseConnection.COLUMN_CHEQUE_DDNO, tcollection.getCheque_DDNo());
		values.put(DatabaseConnection.COLUMN_CHQ_DATE, tcollection.getCheque_DD_Date());
		values.put(DatabaseConnection.COLUMN_BANK, tcollection.getBank());
		values.put(DatabaseConnection.COLUMN_BRANCH, tcollection.getBranch());
		values.put(DatabaseConnection.COLUMN_REMARK, tcollection.getRemarks());
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(tcollection.getVDate()));
		values.put(DatabaseConnection.COLUMN_LAT,tcollection.getLatitude());
		values.put(DatabaseConnection.COLUMN_LNG,tcollection.getLongitude());
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, tcollection.getLatlng_timestamp());
		
		if(tcollection.getNewTransCollection())
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

		 id = database.insert(DatabaseConnection.TABLE_TRANSCOLLECTION, null, values);
//			database.setTransactionSuccessful();
//			database.endTransaction();
			mTranse = false;
		System.out.println(id);
		} catch(RuntimeException e){
			if(mTranse)
			{
				mTranse = false;
			//	database.endTransaction();
			}
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		return id;
	}


	public static boolean isFlag = false;
	public long insertTransCollection(
String CollId,
	String AreaId,
	String CreatedDate,
	String VisId,
	String UserId,
	String Amount,
	String Android_Id,
	String Bank,
	String Branch,
	String Cheque_DD_Date,
	String Cheque_DD_No,
	String CollDocId,
	String Mode,
	String Partyid,
	String PaymentDate,
	String Remarks,
	String SMID,
	String VDate,
	boolean getNewTransCollection,String lat,String longi,String latlongtimestamp

	){
		ContentValues values = new ContentValues();
		long id=-1;

		boolean flag=false,updateflag=false;
		String qry="select timestamp from TransCollection where Android_id='"+Android_Id+"'";
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

		DsrController dc = new DsrController(mContext);
		dc.open();
		boolean isDsrLock = dc.isDsrLockForVisitDate(VDate);
		dc.close();


		if(isDsrLock)
		{
			values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, Android_Id);
			values.put(DatabaseConnection.COLUMN_ID,CollId);
			values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, CollDocId);
			values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
			values.put(DatabaseConnection.COLUMN_VISIT_DATE,VDate);
			values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
			values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
			values.put(DatabaseConnection.COLUMN_AREA_CODE, AreaId);
			values.put(DatabaseConnection.COLUMN_MODE, Mode);
			values.put(DatabaseConnection.COLUMN_AMOUNT, Amount);
			values.put(DatabaseConnection.COLUMN_PAYMENT_DATE, PaymentDate);
			values.put(DatabaseConnection.COLUMN_CHEQUE_DDNO, Cheque_DD_No);
			values.put(DatabaseConnection.COLUMN_CHQ_DATE, Cheque_DD_Date);
			values.put(DatabaseConnection.COLUMN_BANK, Bank);
			values.put(DatabaseConnection.COLUMN_BRANCH, Branch);
			values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
			values.put(DatabaseConnection.COLUMN_LAT, lat);
			values.put(DatabaseConnection.COLUMN_LNG, longi);
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongtimestamp);
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, CreatedDate);

			id  = database.update(DatabaseConnection.TABLE_TRANSCOLLECTION,
					values, "Android_id='" +Android_Id + "'", null);
			if(id>0)
			{
				// do nothing
			}
			else
			{
				id = database.insert(DatabaseConnection.TABLE_TRANSCOLLECTION, null, values);
			}
		}

		else
		{
			values.put(DatabaseConnection.COLUMN_ID,CollId);
			values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, CollDocId);
			values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
			values.put(DatabaseConnection.COLUMN_VISIT_DATE,VDate);
			values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
			values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
			values.put(DatabaseConnection.COLUMN_AREA_CODE, AreaId);
			values.put(DatabaseConnection.COLUMN_MODE, Mode);
			values.put(DatabaseConnection.COLUMN_AMOUNT, Amount);
			values.put(DatabaseConnection.COLUMN_PAYMENT_DATE, PaymentDate);
			values.put(DatabaseConnection.COLUMN_CHEQUE_DDNO, Cheque_DD_No);
			values.put(DatabaseConnection.COLUMN_CHQ_DATE, Cheque_DD_Date);
			values.put(DatabaseConnection.COLUMN_BANK, Bank);
			values.put(DatabaseConnection.COLUMN_BRANCH, Branch);
			values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
			values.put(DatabaseConnection.COLUMN_LAT, lat);
			values.put(DatabaseConnection.COLUMN_LNG, longi);
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongtimestamp);
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, CreatedDate);
			try{
				if(flag)
				{
					if(!updateflag){
						id = database.update(DatabaseConnection.TABLE_TRANSCOLLECTION,
								values, "Android_id='" +Android_Id + "'", null);

						System.out.println("row=" + id);
					}
					else
					{
						CompetitorController.isUpdateFlag = true;

					}

				}
				else
				{

					id = database.insert(DatabaseConnection.TABLE_TRANSCOLLECTION, null, values);
					System.out.println("row=" + id);

				}
			}
			catch(RuntimeException e){
				if(mTranse)
				{
					database.endTransaction();
					mTranse = false;
				}
				System.out.println("+++++++++++++++++++"+e.toString());
			}

			values=null;cursor=null;

		}

				return id;
	}

	public long updateTransCollection(TransCollection tcollection){
		long id=-1;
		mTranse = true;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ID, tcollection.getCollId());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, tcollection.getVisId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, tcollection.getCollDocId());
		values.put(DatabaseConnection.COLUMN_USR_ID, tcollection.getUserId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, tcollection.getVDate());
		values.put(DatabaseConnection.COLUMN_PARTY_CODE, tcollection.getPartyId());
		values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE, tcollection.getAndPartyId());
		values.put(DatabaseConnection.COLUMN_SREP_CODE, tcollection.getSMId());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, tcollection.getAreaId());
		values.put(DatabaseConnection.COLUMN_ITEM_CODE, tcollection.getItemId());
		values.put(DatabaseConnection.COLUMN_MODE, tcollection.getMode());
		values.put(DatabaseConnection.COLUMN_AMOUNT, tcollection.getAmount());
		values.put(DatabaseConnection.COLUMN_PAYMENT_DATE, tcollection.getPaymentDate());
		values.put(DatabaseConnection.COLUMN_CHEQUE_DDNO, tcollection.getCheque_DDNo());
		values.put(DatabaseConnection.COLUMN_CHQ_DATE, tcollection.getCheque_DD_Date());
		values.put(DatabaseConnection.COLUMN_BANK, tcollection.getBank());
		values.put(DatabaseConnection.COLUMN_BRANCH, tcollection.getBranch());
		values.put(DatabaseConnection.COLUMN_REMARK, tcollection.getRemarks());
		values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(tcollection.getVDate()));
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,tcollection.getLatlng_timestamp());
		values.put(DatabaseConnection.COLUMN_LAT,tcollection.getLatitude());
		values.put(DatabaseConnection.COLUMN_LNG,tcollection.getLongitude());
		try{
			id = database.update(DatabaseConnection.TABLE_TRANSCOLLECTION, values, "Android_id='"+tcollection.getAndroid_id()+"'", null);
			System.out.println("row="+id);
		}
		catch(RuntimeException e){
			if(mTranse)
			{
				database.endTransaction();
				mTranse = false;
			}
			System.out.println("+++++++++++++++++++"+e.toString());
		}





		//  **************update code*****************************************************

/*
		database.beginTransaction();
		int c=0;
		String qry="select count(*) from TransCollection where Android_id='"+Android_Id+"'";
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


/*/
/************************************************************
		values.put(DatabaseConnection.COLUMN_ID,CollId);
		values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, CollDocId);
		values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
		values.put(DatabaseConnection.COLUMN_VISIT_DATE,VDate);
		values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
		values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
		values.put(DatabaseConnection.COLUMN_AREA_CODE, AreaId);
		values.put(DatabaseConnection.COLUMN_MODE, Mode);
		values.put(DatabaseConnection.COLUMN_AMOUNT, Amount);
		values.put(DatabaseConnection.COLUMN_PAYMENT_DATE, PaymentDate);
		values.put(DatabaseConnection.COLUMN_CHEQUE_DDNO, Cheque_DD_No);
		values.put(DatabaseConnection.COLUMN_CHQ_DATE, Cheque_DD_Date);
		values.put(DatabaseConnection.COLUMN_BANK, Bank);
		values.put(DatabaseConnection.COLUMN_BRANCH, Branch);
		values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
*/
/***********************  Write By Sandeep Singh 20-04-2017  *****************//*

		*/
/****************  START  *****************//*

		values.put(DatabaseConnection.COLUMN_LAT, lat);
		values.put(DatabaseConnection.COLUMN_LNG, longi);
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongtimestamp);
		*/
/****************  END  *****************//*

		if(getNewTransCollection)
		{
//   values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, CreatedDate);
		}
		else
		{
			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction.ConvertDateToTimestamp(VDate+" 00:00:00", "dd/MMM/yyyy 00:00:00"));
		}

//  try{
//   id = database.insert(DatabaseConnection.TABLE_TRANSCOLLECTION, null, values);
//   System.out.println(id);
//  } catch(RuntimeException e){
//   System.out.println("+++++++++++++++++++"+e.toString());
//  }

		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_TRANSCOLLECTION,
							values, "Android_id='" +Android_Id + "'", null);
					System.out.println("row=" + id);
					database.setTransactionSuccessful();
					database.endTransaction();
					mTranse = false;
				} catch (RuntimeException e) {
					if(mTranse)
					{
						database.endTransaction();
					}
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_TRANSCOLLECTION, null, values);
					database.setTransactionSuccessful();
					database.endTransaction();
					mTranse = false;
					System.out.println("row=" + id);
				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
					if(mTranse)
					{
						database.endTransaction();
					}
				}
			}

		} catch (SQLiteConstraintException constraintException) {
			System.out.println("constraintException="
					+ constraintException.getMessage());


		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}

		values=null;cursor=null;
		return id;
	}

	public long updateTransCollection(TransCollection tcollection){
		long id=-1;
		mTranse = true;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ID, tcollection.getCollId());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, tcollection.getVisId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, tcollection.getCollDocId());
		values.put(DatabaseConnection.COLUMN_USR_ID, tcollection.getUserId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, tcollection.getVDate());
		values.put(DatabaseConnection.COLUMN_PARTY_CODE, tcollection.getPartyId());
		values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE, tcollection.getAndPartyId());
		values.put(DatabaseConnection.COLUMN_SREP_CODE, tcollection.getSMId());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, tcollection.getAreaId());
		values.put(DatabaseConnection.COLUMN_ITEM_CODE, tcollection.getItemId());
		values.put(DatabaseConnection.COLUMN_MODE, tcollection.getMode());
		values.put(DatabaseConnection.COLUMN_AMOUNT, tcollection.getAmount());
		values.put(DatabaseConnection.COLUMN_PAYMENT_DATE, tcollection.getPaymentDate());
		values.put(DatabaseConnection.COLUMN_CHEQUE_DDNO, tcollection.getCheque_DDNo());
		values.put(DatabaseConnection.COLUMN_CHQ_DATE, tcollection.getCheque_DD_Date());
		values.put(DatabaseConnection.COLUMN_BANK, tcollection.getBank());
		values.put(DatabaseConnection.COLUMN_BRANCH, tcollection.getBranch());
		values.put(DatabaseConnection.COLUMN_REMARK, tcollection.getRemarks());
		values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(tcollection.getVDate()));
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,tcollection.getLatlng_timestamp());
		values.put(DatabaseConnection.COLUMN_LAT,tcollection.getLatitude());
		values.put(DatabaseConnection.COLUMN_LNG,tcollection.getLongitude());
		try{
			database.beginTransaction();
			id = database.update(DatabaseConnection.TABLE_TRANSCOLLECTION, values, "Android_id='"+tcollection.getAndroid_id()+"'", null);
			System.out.println("row="+id);
			database.setTransactionSuccessful();
		}
		catch(RuntimeException e){
			if(mTranse)
			{
				database.endTransaction();
				mTranse = false;
			}
			System.out.println("+++++++++++++++++++"+e.toString());
		}
*/

		return id;
	}

	public ArrayList<TransCollection> getCollectionListFromAndToDate(String fromDate, String toDate, String partyId, String apartyId) {
		String query="";

		if(partyId!=null && apartyId!=null)
		{
			/*query = "select dc.web_doc_id,dc.Android_id,md.name," +
					"dc.mode,dc.amount,dc.payment_date,dc.cheque_ddno," +
					"dc.chq_date,dc.isUpload,dc.party_code,md.sync_id "
					+ "from TransCollection dc " +
					"left join mastParty md on md.webcode=dc.party_code " +
					"where dc.party_code='"+partyId+"'  " +
					"and dc.CreatedDate " +
					"between '"+DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' " +
					"and " +
					"'"+DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' " +
					"order by dc.timestamp desc";*/

			query = "select distinct dc.web_doc_id,dc.Android_id,md.name," +
					"dc.mode,dc.amount,dc.payment_date,dc.cheque_ddno," +
					"dc.chq_date,dc.isUpload,dc.party_code,md.sync_id "
					+ "from TransCollection dc " +
					"left join mastParty md on md.webcode=dc.party_code " +
					"where dc.party_code='"+partyId+"'  " +
					"and dc.visit_date=" +
					"'"+fromDate+"' " +

					"order by dc.timestamp desc";


		}
		else if(partyId!=null && apartyId==null)
		{
			/*query = "select dc.web_doc_id,dc.Android_id,md.name," +
					"dc.mode,dc.amount,dc.payment_date,dc.cheque_ddno," +
					"dc.chq_date,dc.isUpload,dc.party_code,md.sync_id "
					+ "from TransCollection dc " +
					"left join mastParty md on md.webcode=dc.party_code " +
					"where dc.party_code='"+partyId+"'  " +
					"and dc.timestamp " +
					"between '"+DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' " +
					"and " +
					"'"+DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' " +
					"order by dc.timestamp desc";*/

			query = "select distinct dc.web_doc_id,dc.Android_id,md.name," +
					"dc.mode,dc.amount,dc.payment_date,dc.cheque_ddno," +
					"dc.chq_date,dc.isUpload,dc.party_code,md.sync_id "
					+ "from TransCollection dc " +
					"left join mastParty md on md.webcode=dc.party_code " +
					"where dc.party_code='"+partyId+"'  " +
					"and dc.visit_date='"+fromDate+"'"					+

					"order by dc.timestamp desc";

		}
		else if(partyId==null && apartyId!=null)
		{
			query = "select distinct dc.web_doc_id,dc.Android_id,md.name," +
					"dc.mode,dc.amount,dc.payment_date,dc.cheque_ddno," +
					"dc.chq_date,dc.isUpload,dc.and_party_code,md.sync_id "
					+ "from TransCollection dc " +
					"left join mastParty md on md.Android_id=dc.and_party_code " +
					"where dc.and_party_code='"+apartyId+"'  " +
					"and dc.visit_date=" +
					"'"+fromDate+"' " +

					"order by dc.timestamp desc";
		}

		System.out.println(query);
		Cursor cursor=null;
		ArrayList<TransCollection> collections = new ArrayList<TransCollection>();
		try {
			 cursor = database.rawQuery(query, null);

			if (cursor.moveToFirst()) {
				while (!(cursor.isAfterLast())) {
					TransCollection collection = new TransCollection();
					collection.setCollDocId(cursor.getString(0));
					collection.setAndroid_id(cursor.getString(1));
					collection.setPartyName(cursor.getString(2));
					collection.setMode(cursor.getString(3));
					collection.setAmount(cursor.getString(4));
					collection.setPaymentDate(cursor.getString(5));
					collection.setCheque_DDNo(cursor.getString(6));
					collection.setCheque_DD_Date(cursor.getString(7));
					collection.setIsUpload(cursor.getString(8));
					collection.setPartyId(cursor.getString(9));
					collections.add(collection);
					cursor.moveToNext();
				}
			} else {
				System.out.println("No records found");
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
//		cursor.close();
		return collections;

	}

	public TransCollection getCollectionByAndroidId(String androidId)
	{
		String query="select * from TransCollection where Android_id='"+androidId+"'";
		System.out.println(query);
		TransCollection collection = new TransCollection();
		Cursor cursor=database.rawQuery(query, null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				collection.setAndroid_id(cursor.getString(3));
				collection.setUserId(cursor.getString(4));
				collection.setVDate(cursor.getString(5));
				collection.setPartyId(cursor.getString(6));
				collection.setSMId(cursor.getString(7));
				collection.setAreaId(cursor.getString(8));
				collection.setMode(cursor.getString(10));
				collection.setAmount(cursor.getString(11));
				collection.setPaymentDate(cursor.getString(12));
				collection.setCheque_DDNo(cursor.getString(13));
				collection.setCheque_DD_Date(cursor.getString(14));
				collection.setBank(cursor.getString(15));
				collection.setBranch(cursor.getString(16));
				collection.setRemarks(cursor.getString(17));
				collection.setIsUpload(cursor.getString(14));
				collection.setAndPartyId(cursor.getString(16));

				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return collection;
	}


	//	public ArrayList<TransCollection> getPartyCollectionUplaodList(){
//		
//		String sql="select cl.*,pm.name,vl1.visit_no from TransCollection cl "
//					+" LEFT JOIN mastParty pm ON pm.webcode=cl.party_code " +
//					" LEFT JOIN Visitl1 vl1"+
//					 " ON cl.visit_date = vl1.visit_date"+
//					" where" +
//					" vl1.dsr_lock = '1'"+
//					" and cl.isupload='0' " ;
//        System.out.println(sql);
//		Cursor cursor=database.rawQuery(sql, null);
//		ArrayList<TransCollection> collections = new ArrayList<TransCollection>();
//		if(cursor.moveToFirst()){
//			while(!(cursor.isAfterLast()))
//			{				
//				TransCollection collection = new TransCollection();			
//				collection.setAndroid_id(cursor.getString(3));
//				collection.setUserId(cursor.getString(4));
//				collection.setVDate(cursor.getString(5));
//				collection.setPartyId(cursor.getString(6));
//				collection.setSMId(cursor.getString(7));
//				collection.setAreaId(cursor.getString(8));
//				collection.setMode(cursor.getString(10));
//				collection.setAmount(cursor.getString(11));
//				collection.setPaymentDate(cursor.getString(12));
//				collection.setCheque_DDNo(cursor.getString(13));
//				collection.setCheque_DD_Date(cursor.getString(14));
//				collection.setBank(cursor.getString(15));
//				collection.setBranch(cursor.getString(16));
//				collection.setRemarks(cursor.getString(17));
//				collection.setPartyName(cursor.getString(19));
//				collection.setVisId(cursor.getString(20));
//				collections.add(collection);
//				cursor.moveToNext();
//			}
//		}else{
//			System.out.println("No records found");
//		}
//		cursor.close();
//		return collections;
//	}
public ArrayList<TransCollection> getPartyCollectionUplaodList(String vdate){
		
//		String sql="select cl.*,pm.name,vl1.visit_no from TransCollection cl "
//					+" LEFT JOIN mastParty pm ON pm.webcode=cl.party_code " +
//					" LEFT JOIN Visitl1 vl1"+
//					 " ON cl.visit_date = vl1.visit_date"+
//					" where" +
//					" vl1.dsr_lock = '1'"+
//					" and cl.isupload='0' and cl.visit_date='"+vdate+"' " ;

	/************************		Write By Sandeep Singh 20-04-2017		******************/
	/*****************		START		******************/
	String sql="select cl.Android_id,cl.usr_id,cl.visit_date," +
			"cl.party_code,cl.srep_code,cl.area_code,cl.mode,cl.amount," +
			"cl.payment_date,cl.cheque_ddno,cl.chq_date,cl.bank,cl.branch,cl.remark," +
			"pm.name,vl1.visit_no,ifnull(cl.latitude,0) as latitude,ifnull(cl.longitude,0) as longitude,ifnull(cl.LatlngTime,0) as LatlngTime from TransCollection cl "
			+" LEFT JOIN mastParty pm ON pm.webcode=cl.party_code " +
			" LEFT JOIN Visitl1 vl1"+
			" ON cl.visit_date = vl1.visit_date"+
			" where" +
			" cl.timestamp=0 and cl.visit_date='"+vdate+"' " ;
	/*String sql="select cl.Android_id,cl.usr_id,cl.visit_date," +
			"cl.party_code,cl.srep_code,cl.area_code,cl.mode,cl.amount," +
			"cl.payment_date,cl.cheque_ddno,cl.chq_date,cl.bank,cl.branch,cl.remark," +
			"pm.name,vl1.visit_no from TransCollection cl "
			+" LEFT JOIN mastParty pm ON pm.webcode=cl.party_code " +
			" LEFT JOIN Visitl1 vl1"+
			" ON cl.visit_date = vl1.visit_date"+
			" where" +
			" vl1.dsr_lock = '1'"+
			" and cl.timestamp=0 and cl.visit_date='"+vdate+"' " ;*/
	/*****************		END		******************/
        System.out.println(sql);
		Cursor cursor=database.rawQuery(sql, null);
		ArrayList<TransCollection> collections = new ArrayList<TransCollection>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				TransCollection collection = new TransCollection();
				collection.setAndroid_id(cursor.getString(0));
				collection.setUserId(cursor.getString(1));
				collection.setVDate(cursor.getString(2));
				collection.setPartyId(cursor.getString(3));
				collection.setSMId(cursor.getString(4));
				collection.setAreaId(cursor.getString(5));
				collection.setMode(cursor.getString(6));
				collection.setAmount(cursor.getString(7));
				collection.setPaymentDate(cursor.getString(8));
				collection.setCheque_DDNo(cursor.getString(9));
				collection.setCheque_DD_Date(cursor.getString(10));
				collection.setBank(cursor.getString(11));
				collection.setBranch(cursor.getString(12));
				collection.setRemarks(cursor.getString(13));
				collection.setPartyName(cursor.getString(14));
				collection.setVisId(cursor.getString(15));
				/************************		Write By Sandeep Singh 20-04-2017		******************/
				/*****************		START		******************/
				collection.setLatitude(cursor.getString(16));
				collection.setLongitude(cursor.getString(17));
				collection.setLatlng_timestamp(cursor.getString(18));
				/*****************		END		******************/
				collections.add(collection);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return collections;
	}
	//	public  long updatePartyCollectionUpload(String androiddocId, String webdocId,int cno,int vId){

	/************************		Write By Sandeep Singh 10-04-2017		******************/
	/*****************		START		******************/
	public  long updatePartyCollectionUpload(String androiddocId, String webdocId,int cno,int vId,String timeStamp){
		/*****************		END		******************/		long id=0;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_UPLOAD,"1");
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID,webdocId);
		values.put(DatabaseConnection.COLUMN_ID,cno);
		values.put(DatabaseConnection.COLUMN_VISIT_NO,vId);

		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
		/*****************		END		******************/

		try{
			id = database.update(DatabaseConnection.TABLE_TRANSCOLLECTION, values, "Android_id='"+androiddocId+"'", null);
			System.out.println("row="+id);
		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		return id;
	}
	
	public float getTotalSecondaryCollection(String date)
	{
		float sum=0;
		ArrayList<Float> primaryCollection=new ArrayList<Float>();
		
		String query1="select ifNull(sum(amount),0)  as pamount1 from TransCollection where payment_date='"+date+"'";
		String query2="select ifNull(sum(amount),0)  as pamount2 from TransCollection where payment_date='"+date+"' and party_code IS NULL";
		Cursor cursor1=database.rawQuery(query1, null);
	if(cursor1.moveToFirst()){
			while(!(cursor1.isAfterLast()))
			{						
				
				primaryCollection.add(0,cursor1.getFloat(0));
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
			primaryCollection.add(1,cursor2.getFloat(0));
			cursor2.moveToNext();
		}
	}else{
		System.out.println("No records found");
	}
cursor2.close();
	
	sum=primaryCollection.get(0)+primaryCollection.get(1);
		
		return sum;
	}




	public boolean isAndroidIdExist(String android_doc_id)
	{
		Cursor c = database.query(DatabaseConnection.TABLE_TRANSCOLLECTION,null,"Android_id='" +android_doc_id + "'",null,null,null,null);
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
