package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.library.DateFunction;
import com.dm.model.Competitor;

import java.util.ArrayList;


public class CompetitorController {
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
			DatabaseConnection.COLUMN_ITEM_NAME,
			DatabaseConnection.COLUMN_QTY,
			DatabaseConnection.COLUMN_RATE
			
	};

	public static boolean isUpdateFlag = false;
	public CompetitorController(Context context) {
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
	
	public void insertCompetitor(Competitor competitor){
		mTranse = true;
		//database.beginTransaction();
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ID, competitor.getComptId());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, competitor.getVisId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, competitor.getDocId());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, competitor.getAndroid_id());
		values.put(DatabaseConnection.COLUMN_USR_ID, competitor.getUserId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, competitor.getVDate());
		values.put(DatabaseConnection.COLUMN_PARTY_CODE, competitor.getPartyId());
		values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE, competitor.getAndPartyId());
		values.put(DatabaseConnection.COLUMN_SREP_CODE, competitor.getSMID());
		values.put(DatabaseConnection.COLUMN_ITEM_NAME, competitor.getItem());
		values.put(DatabaseConnection.COLUMN_QTY, competitor.getQty());
		values.put(DatabaseConnection.COLUMN_RATE, competitor.getRate());
		values.put(DatabaseConnection.COLUMN_REMARK, competitor.getRemark());
		values.put(DatabaseConnection.COLUMN_IMAGE_PATH, competitor.getFilePath());
		values.put(DatabaseConnection.COLUMN_BRAND_ACTIVITY, competitor.getBrandActivity());
		values.put(DatabaseConnection.COLUMN_MEET_ACTIVITY, competitor.getMeetActivity());
		values.put(DatabaseConnection.COLUMN_ROADSHOW, competitor.getRoadShow());
		values.put(DatabaseConnection.COLUMN_SCHEME, competitor.getScheme());
		values.put(DatabaseConnection.COLUMN_GENERAL_INFO, competitor.getGeneralInfo());
		values.put(DatabaseConnection.COLUMN_NAME, competitor.getCompetitorName());
		values.put(DatabaseConnection.COLUMN_DISCOUNT, competitor.getDiscount());
		values.put(DatabaseConnection.COLUMN_OTHER_ACTIVITY, competitor.getOtherActivity());
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(competitor.getVDate()));
		values.put(DatabaseConnection.COLUMN_LAT,competitor.getLatitude());
		values.put(DatabaseConnection.COLUMN_LNG,competitor.getLongitude());
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,competitor.getLatlngTimeStamp());
		if(competitor.getCompetitorImport())
		{
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
			values.put(DatabaseConnection.COLUMN_UPLOAD,"1");
		}
		else
		{
			values.put(DatabaseConnection.COLUMN_UPLOAD,"0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
		}
		try{

		long id = database.insert(DatabaseConnection.TABLE_COMPETITOR, null, values);
//			database.setTransactionSuccessful();
//			database.endTransaction();
			mTranse = false;
		System.out.println("+++++++++++++++++++"+id);
		}
		catch(RuntimeException e){
			if(mTranse)
			{
				mTranse = false;
				database.endTransaction();
			}
			System.out.println("+++++++++++++++++++"+e.toString());
		} 
}

	public void insertCompetitor(

			String VDate,
			String Createddate,
			String VisId,
			String UserId,
			String Android_Id,
			String ComptId,
			String DocId,
			String Item,
			String Partyid,
			String Qty,
			String Rate,
			String SMID,
			String CompName,
			String Discount,
			String BrandActivity,
			String MeetCtivity,
			String RoadShow,
			String Scheme,
			String OthergernralInfo,
			String OtherActivity,
			String Remarks,
			boolean getCompetitorImport,
			String path,String lat,String longi,String latlongtimestamp

	){

		String dsrLockValue="";
		mTranse = true;

		boolean flag=false,updateflag=false;
		ContentValues values = new ContentValues();
		//database.beginTransaction();
		String qry="select timestamp from TransCompetitor where Android_id='"+Android_Id+"'";

		System.out.println(qry);
		Cursor cursor = database.rawQuery(qry, null);

		DsrController dc = new DsrController(mContext);

		String dsrLockQry = "select dsr_lock from Visitl1 where visit_date='"+VDate+"'";
		//Cursor dsrLockCursor  = database.rawQuery(dsrLockQry,null);
		/*if(dc.isDsrLockForVisitDate(VDate))
		{
			dsrLockValue = dsrLockCursor.getString(0);
		}*/
		dc.open();
		boolean isDsrLock = dc.isDsrLockForVisitDate(VDate);
		dc.close();
		if(isDsrLock)
		{

			values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, Android_Id);
			values.put(DatabaseConnection.COLUMN_ID, ComptId);
			values.put(DatabaseConnection.COLUMN_VISIT_NO,VisId);
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, DocId);
			values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
			values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
			values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
			values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
			values.put(DatabaseConnection.COLUMN_ITEM_NAME, Item);
			values.put(DatabaseConnection.COLUMN_QTY, Qty);
			values.put(DatabaseConnection.COLUMN_RATE, Rate);
			values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
			values.put(DatabaseConnection.COLUMN_BRAND_ACTIVITY, BrandActivity);
			values.put(DatabaseConnection.COLUMN_MEET_ACTIVITY, MeetCtivity);
			values.put(DatabaseConnection.COLUMN_ROADSHOW, RoadShow);
			values.put(DatabaseConnection.COLUMN_SCHEME, Scheme);
			values.put(DatabaseConnection.COLUMN_GENERAL_INFO,OthergernralInfo);
			values.put(DatabaseConnection.COLUMN_NAME, CompName);
			values.put(DatabaseConnection.COLUMN_DISCOUNT, Discount);
			values.put(DatabaseConnection.COLUMN_OTHER_ACTIVITY, OtherActivity);
			values.put(DatabaseConnection.COLUMN_IMAGE_PATH, path);
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
/************************		Write By Sandeep Singh 20-04-2017		******************/
			/*****************		START		******************/
			values.put(DatabaseConnection.COLUMN_LAT, lat);
			values.put(DatabaseConnection.COLUMN_LNG, longi);
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongtimestamp);
			values.put(DatabaseConnection.COLUMN_TIMESTAMP,Createddate);

			long id = database.update(DatabaseConnection.TABLE_COMPETITOR,
					values, "Android_id='" +Android_Id + "'", null);
			System.out.print(""+id);
			if(id>0)
			{
				//do nothing
			}
			else
			{
				long id1 = database.insert(DatabaseConnection.TABLE_COMPETITOR,
						null,values);
				System.out.print(""+id1);
			}

		}
		else{
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
			values.put(DatabaseConnection.COLUMN_ID, ComptId);
			values.put(DatabaseConnection.COLUMN_VISIT_NO,VisId);
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, DocId);
			values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
			values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
			values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
			values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
			values.put(DatabaseConnection.COLUMN_ITEM_NAME, Item);
			values.put(DatabaseConnection.COLUMN_QTY, Qty);
			values.put(DatabaseConnection.COLUMN_RATE, Rate);
			values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
			values.put(DatabaseConnection.COLUMN_BRAND_ACTIVITY, BrandActivity);
			values.put(DatabaseConnection.COLUMN_MEET_ACTIVITY, MeetCtivity);
			values.put(DatabaseConnection.COLUMN_ROADSHOW, RoadShow);
			values.put(DatabaseConnection.COLUMN_SCHEME, Scheme);
			values.put(DatabaseConnection.COLUMN_GENERAL_INFO,OthergernralInfo);
			values.put(DatabaseConnection.COLUMN_NAME, CompName);
			values.put(DatabaseConnection.COLUMN_DISCOUNT, Discount);
			values.put(DatabaseConnection.COLUMN_OTHER_ACTIVITY, OtherActivity);
			values.put(DatabaseConnection.COLUMN_IMAGE_PATH, path);
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
/************************		Write By Sandeep Singh 20-04-2017		******************/
			/*****************		START		******************/
			values.put(DatabaseConnection.COLUMN_LAT, lat);
			values.put(DatabaseConnection.COLUMN_LNG, longi);
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongtimestamp);
			values.put(DatabaseConnection.COLUMN_TIMESTAMP,Createddate);
			/*****************		END		******************/
			long id=0;
			try{
				if(flag)
				{
					if(!updateflag){
						id = database.update(DatabaseConnection.TABLE_COMPETITOR,
								values, "Android_id='" +Android_Id + "'", null);
//						database.setTransactionSuccessful();
//						database.endTransaction();
						mTranse = false;
						System.out.println("row=" + id);
					}
					else
					{
						isUpdateFlag = true;
						//database.endTransaction();
						mTranse = false;
					}
				}
				else
				{

					id = database.insert(DatabaseConnection.TABLE_COMPETITOR, null, values);
					System.out.println("row=" + id);
					/*database.setTransactionSuccessful();
					database.endTransaction();*/
					mTranse=false;
				}
			}
			catch(RuntimeException e){
				/*if(mTranse)
				{
					database.endTransaction();
					mTranse = false;
				}*/
				System.out.println("+++++++++++++++++++"+e.toString());
			}
		}



		values=null;cursor=null;
		//		***************update code******************************************************

	/*	mTranse = true;
		int c=0;
		database.beginTransaction();
		String qry="select count(*) from TransCompetitor where Android_id='"+Android_Id+"'";
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



		values.put(DatabaseConnection.COLUMN_ID, ComptId);
		values.put(DatabaseConnection.COLUMN_VISIT_NO,VisId);
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, DocId);
		values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
		values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
		values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
		values.put(DatabaseConnection.COLUMN_ITEM_NAME, Item);
		values.put(DatabaseConnection.COLUMN_QTY, Qty);
		values.put(DatabaseConnection.COLUMN_RATE, Rate);
		values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
		values.put(DatabaseConnection.COLUMN_BRAND_ACTIVITY, BrandActivity);
		values.put(DatabaseConnection.COLUMN_MEET_ACTIVITY, MeetCtivity);
		values.put(DatabaseConnection.COLUMN_ROADSHOW, RoadShow);
		values.put(DatabaseConnection.COLUMN_SCHEME, Scheme);
		values.put(DatabaseConnection.COLUMN_GENERAL_INFO,OthergernralInfo);
		values.put(DatabaseConnection.COLUMN_NAME, CompName);
		values.put(DatabaseConnection.COLUMN_DISCOUNT, Discount);
		values.put(DatabaseConnection.COLUMN_OTHER_ACTIVITY, OtherActivity);
		values.put(DatabaseConnection.COLUMN_IMAGE_PATH, path);
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
*//************************		Write By Sandeep Singh 20-04-2017		******************//*
		*//*****************		START		******************//*
		values.put(DatabaseConnection.COLUMN_LAT, lat);
		values.put(DatabaseConnection.COLUMN_LNG, longi);
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongtimestamp);
		*//*****************		END		******************//*
		if(getCompetitorImport)
		{
			values.put(DatabaseConnection.COLUMN_TIMESTAMP,Createddate);
//			values.put(DatabaseConnection.COLUMN_UPLOAD,"1");
		}
		else
		{
			values.put(DatabaseConnection.COLUMN_UPLOAD,"0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction.ConvertDateToTimestamp(VDate+" 00:00:00", "dd/MMM/yyyy 00:00:00"));
		}
//		try{
//			long id = database.insert(DatabaseConnection.TABLE_COMPETITOR, null, values);
//			System.out.println("+++++++++++++++++++"+id);
//		}
//		catch(RuntimeException e){
//			System.out.println("+++++++++++++++++++"+e.toString());
//		}


		long id=0;
		try{
			if(c>0)
			{
				try {

					id = database.update(DatabaseConnection.TABLE_COMPETITOR,
							values, "Android_id='" +Android_Id + "'", null);
					System.out.println("row=" + id);
					database.setTransactionSuccessful();
					database.endTransaction();
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
					if(mTranse) {
						database.endTransaction();
						mTranse = false;
					}
				}


			}
			else{
				try {

					id = database.insert(DatabaseConnection.TABLE_COMPETITOR, null, values);
					System.out.println("row=" + id);
					database.setTransactionSuccessful();
					database.endTransaction();
					mTranse = false;
				}
				catch(RuntimeException e){
					if(mTranse)
					{
						mTranse = false;
						database.endTransaction();
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

	
	public String getAndroidDocId(String partyId,String vdate){
		String Adoc_id = "0";
		String query = "select ifnull(Android_id,0) as MyColumn from TransCompetitor where party_code='"+partyId+"' and visit_date='"+vdate+"'";
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
	
//	public void updateComptitor(Competitor comptitor)
//	{
//	String query="update TransCompetitor " +
//			"set qty='"+comptitor.getQty()+"' , " +
//			"rate='"+comptitor.getRate()+"'," +
//			"remark='"+comptitor.getRemark()+"'," +
//			"path='"+comptitor.getFilePath()+"'," +
//			"name='"+comptitor.getCompetitorName()+"'," +
//			"brandActivity='"+comptitor.getBrandActivity()+"'," +
//			"meetActivity='"+comptitor.getMeetActivity()+"'," +
//			"roadShow='"+comptitor.getRoadShow()+"'," +
//			"scheme='"+comptitor.getScheme()+"'," +
//			"generalInfo='"+comptitor.getGeneralInfo()+"'," +
//			"discount='"+comptitor.getDiscount()+"'," +
//			"otherActivity='"+comptitor.getOtherActivity()+"'," +
//			"ItemName='"+comptitor.getItem()+"' " +
//			"where Android_id='"+comptitor.getAndroid_id()+"'";
//	System.out.println(query);
//	try {
//		// String
//		// qurey="UPDATE order_sub SET remark="+""+" WHERE ExecutionDate = '2011-10-12 00:00:00.000'";
//
//	database.execSQL(query);
//		System.out.println("Competitor updated");
//	}
//	catch (RuntimeException e) {
//		System.out.println("+++++++++++++++++++" + e.toString());
//	}
//
//	}


	public void updateComptitor(Competitor competitor){
		mTranse = true;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ITEM_NAME, competitor.getItem());
		values.put(DatabaseConnection.COLUMN_QTY, competitor.getQty());
		values.put(DatabaseConnection.COLUMN_RATE, competitor.getRate());
		values.put(DatabaseConnection.COLUMN_REMARK, competitor.getRemark());
		values.put(DatabaseConnection.COLUMN_IMAGE_PATH, competitor.getFilePath());
		values.put(DatabaseConnection.COLUMN_BRAND_ACTIVITY, competitor.getBrandActivity());
		values.put(DatabaseConnection.COLUMN_MEET_ACTIVITY, competitor.getMeetActivity());
		values.put(DatabaseConnection.COLUMN_ROADSHOW, competitor.getRoadShow());
		values.put(DatabaseConnection.COLUMN_SCHEME, competitor.getScheme());
		values.put(DatabaseConnection.COLUMN_GENERAL_INFO, competitor.getGeneralInfo());
		values.put(DatabaseConnection.COLUMN_NAME, competitor.getCompetitorName());
		values.put(DatabaseConnection.COLUMN_DISCOUNT, competitor.getDiscount());
		values.put(DatabaseConnection.COLUMN_OTHER_ACTIVITY, competitor.getOtherActivity());
		values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");

		try{
			database.beginTransaction();
			long id = database.update(DatabaseConnection.TABLE_COMPETITOR, values, "Android_id='"+competitor.getAndroid_id()+"'", null);
			System.out.println("row="+id);
			database.setTransactionSuccessful();
			database.endTransaction();
			mTranse = false;
		}
		catch(RuntimeException e){
			if(mTranse)
			{
				database.endTransaction();
				mTranse = false;
			}
			System.out.println("+++++++++++++++++++"+e.toString());
		}

	}
	public ArrayList<Competitor> getUploadCompetitorList(String vdate){
		/************************		Write By Sandeep Singh 20-04-2017		******************/
		/*****************		START		******************/
		String query="select o.Android_id,o.usr_id,o.visit_date,o.srep_code," +
				"o.party_code,o.ItemName,o.qty,o.rate,vl1.visit_no,o.path," +
				"o.remark,o.name,o.discount,o.otherActivity,o.brandActivity," +
				"o.meetActivity,o.roadShow,o.generalInfo,o.scheme ,ifnull(o.latitude,0) as latitude,ifnull(o.longitude,0) as longitude,ifnull(o.LatlngTime,0) as LatlngTime"+
				" from TransCompetitor o"+
				" LEFT JOIN Visitl1 vl1"+
				" ON o.visit_date = vl1.visit_date"+
				" LEFT JOIN mastParty p"+
				" ON o.party_code = p.webcode"+
				" WHERE "+
				" o.timestamp = '0' AND o.visit_date = '"+vdate+"' ";
		/*String query="select o.Android_id,o.usr_id,o.visit_date,o.srep_code," +
				"o.party_code,o.ItemName,o.qty,o.rate,vl1.visit_no,o.path," +
				"o.remark,o.name,o.discount,o.otherActivity,o.brandActivity," +
				"o.meetActivity,o.roadShow,o.generalInfo,o.scheme "+
				" from TransCompetitor o"+
				" LEFT JOIN Visitl1 vl1"+
				" ON o.visit_date = vl1.visit_date"+
				" LEFT JOIN mastParty p"+
				" ON o.party_code = p.webcode"+
				" WHERE  vl1.dsr_lock = '1'"+
				" AND o.isUpload = '0' AND o.visit_date = '"+vdate+"' ";*/
		/*****************		END		******************/
		System.out.println(query);
		Cursor cursor=database.rawQuery(query, null);
		ArrayList<Competitor> competitors = new ArrayList<Competitor>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{						
				Competitor competitor=new Competitor();
				competitor.setAndroid_id(cursor.getString(0));
				competitor.setUserId(cursor.getString(1));
				competitor.setVDate(cursor.getString(2));
				competitor.setSMID(cursor.getString(3));
				competitor.setPartyId(cursor.getString(4));
				competitor.setItem(cursor.getString(5));
				competitor.setQty(cursor.getString(6));
				competitor.setRate(cursor.getString(7));
				competitor.setVisId(cursor.getString(8));
				competitor.setFilePath(cursor.getString(9));
				competitor.setRemark(cursor.getString(10));
				competitor.setCompetitorName(cursor.getString(11));
				competitor.setDiscount(cursor.getString(12));
				competitor.setOtherActivity(cursor.getString(13));
				competitor.setBrandActivity(cursor.getString(14));
				competitor.setMeetActivity(cursor.getString(15));
				competitor.setRoadShow(cursor.getString(16));
				competitor.setGeneralInfo(cursor.getString(17));
				competitor.setScheme(cursor.getString(18));
				/************************		Write By Sandeep Singh 20-04-2017		******************/
				/*****************		START		******************/
				competitor.setLatitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT)));
				competitor.setLongitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LNG)));
				competitor.setLatlngTimeStamp(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT_LNG_TIME)));
				/*****************		END		******************/
				competitors.add(competitor);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return competitors;
	}

	//public  long updateCompetitorUpload(String androiddocId, String webdocId,int ono,int vId){
	/************************		Write By Sandeep Singh 10-04-2017		******************/
	/*****************		START		******************/
	public  long updateCompetitorUpload(String androiddocId, String webdocId,int ono,int vId,String timeStamp){
		/*****************		END		******************/
		ContentValues values = new ContentValues();
		long id=0;
		values.put(DatabaseConnection.COLUMN_UPLOAD,"1");
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID,webdocId);
		values.put(DatabaseConnection.COLUMN_ID,ono);
		values.put(DatabaseConnection.COLUMN_VISIT_NO,vId);

		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
		/*****************		END		******************/
		try{
			id = database.update(DatabaseConnection.TABLE_COMPETITOR, values, "Android_id='"+androiddocId+"'", null);
			System.out.println("row="+id);
		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		return id;
	}
	public ArrayList<Competitor> getCompetitorByAndroidId(String androidId)
	{
		String query="select ItemName,qty,rate,remark,ifnull(path,'NA'),name,brandActivity," +
				"meetActivity,scheme,roadShow,generalInfo,discount,otherActivity,isUpload " +
				"from TransCompetitor where Android_id='"+androidId+"'";
		System.out.println(query);
		Cursor cursor=database.rawQuery(query, null);
		ArrayList<Competitor> competitors = new ArrayList<Competitor>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{						
				Competitor competitor=new Competitor();
				competitor.setItem(cursor.getString(0));
				competitor.setQty(cursor.getString(1));
				competitor.setRate(cursor.getString(2));
				competitor.setRemark(cursor.getString(3));
				competitor.setFilePath(cursor.getString(4));
				competitor.setCompetitorName(cursor.getString(5));
				competitor.setBrandActivity(cursor.getString(6));
				competitor.setMeetActivity(cursor.getString(7));
				competitor.setScheme(cursor.getString(8));
				competitor.setRoadShow(cursor.getString(9));
				competitor.setGeneralInfo(cursor.getString(10));
				competitor.setDiscount(cursor.getString(11));
				competitor.setOtherActivity(cursor.getString(12));
				competitor.setIsUpload(cursor.getString(13));
				competitors.add(competitor);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return competitors;
	}
	
	
	public ArrayList<Competitor> getExistDocId(String date , String partyCode){
		String query = "select Android_id,ItemName,qty,rate,remark " +
				"from TransCompetitor where visit_date='"+date+"' and party_code='"+partyCode+"'";
		
		System.out.println(query);
		Cursor cursor=database.rawQuery(query, null);
		ArrayList<Competitor> competitors = new ArrayList<Competitor>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{						
				Competitor competitor=new Competitor();
				competitor.setAndroid_id(cursor.getString(0));
				competitor.setItem(cursor.getString(1));
				competitor.setQty(cursor.getString(2));
				competitor.setRate(cursor.getString(3));
				competitor.setRemark(cursor.getString(4));
				competitors.add(competitor);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return competitors;
	}
	
	
	public ArrayList<Competitor> getCompetitorPartyWiseList(String party_code, String aparty_code){
		String query="";
		if(party_code!=null && aparty_code!=null)	
		{
			 query="select ItemName,qty,rate,remark,path,name,brandActivity," +
				"meetActivity,scheme,roadShow,generalInfo,discount,otherActivity,isUpload,Android_id,web_doc_id,visit_date " +
				"from TransCompetitor where party_code='"+party_code+"'";

		}
		
		else if(party_code!=null && aparty_code==null)
	{
		
			query="select ItemName,qty,rate,remark,path,name,brandActivity," +
					"meetActivity,scheme,roadShow,generalInfo,discount,otherActivity,isUpload,Android_id,web_doc_id,visit_date " +
					"from TransCompetitor where party_code='"+party_code+"'";

		
	}
	
	else if(party_code==null && aparty_code!=null)
	{
	
		query="select ItemName,qty,rate,remark,path,name,brandActivity," +
				"meetActivity,scheme,roadShow,generalInfo,discount,otherActivity,isUpload,Android_id,web_doc_id,visit_date " +
				"from TransCompetitor where and_party_code='"+aparty_code+"'";

	}
	
		System.out.println(query);
		Cursor cursor=database.rawQuery(query, null);
		ArrayList<Competitor> competitors = new ArrayList<Competitor>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{						
				Competitor competitor=new Competitor();
				competitor.setItem(cursor.getString(0));
				competitor.setQty(cursor.getString(1));
				competitor.setRate(cursor.getString(2));
				competitor.setRemark(cursor.getString(3));
				competitor.setFilePath(cursor.getString(4));
				competitor.setCompetitorName(cursor.getString(5));
				competitor.setBrandActivity(cursor.getString(6));
				competitor.setMeetActivity(cursor.getString(7));
				competitor.setScheme(cursor.getString(8));
				competitor.setRoadShow(cursor.getString(9));
				competitor.setGeneralInfo(cursor.getString(10));
				competitor.setDiscount(cursor.getString(11));
				competitor.setOtherActivity(cursor.getString(12));
				competitor.setIsUpload(cursor.getString(13));
				competitor.setAndroid_id(cursor.getString(14));
				competitor.setDocId(cursor.getString(15));
				competitor.setVDate(cursor.getString(16));
				competitors.add(competitor);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return competitors;
	}

	public ArrayList<Competitor> getCompetitorPartyWiseList(String party_code, String aparty_code, String vdate){
		ArrayList<Competitor> competitors = new ArrayList<Competitor>();
			try {
			String query = "";
			if (party_code != null && aparty_code != null) {
				query = "select ItemName,qty,rate,remark,path,name,brandActivity," +
						"meetActivity,scheme,roadShow,generalInfo,discount,otherActivity,isUpload,Android_id,web_doc_id,visit_date " +
						"from TransCompetitor where party_code='" + party_code + "' and visit_date='" + vdate + "'";

			} else if (party_code != null && aparty_code == null) {

				query = "select ItemName,qty,rate,remark,path,name,brandActivity," +
						"meetActivity,scheme,roadShow,generalInfo,discount,otherActivity,isUpload,Android_id,web_doc_id,visit_date " +
						"from TransCompetitor where party_code='" + party_code + "' and visit_date='" + vdate + "'";


			} else if (party_code == null && aparty_code != null) {

				query = "select ItemName,qty,rate,remark,path,name,brandActivity," +
						"meetActivity,scheme,roadShow,generalInfo,discount,otherActivity,isUpload,Android_id,web_doc_id,visit_date " +
						"from TransCompetitor where and_party_code='" + aparty_code + "' and visit_date='" + vdate + "'";

			}

			System.out.println(query);
			Cursor cursor = database.rawQuery(query, null);
			competitors = new ArrayList<Competitor>();
			if (cursor.moveToFirst()) {
				while (!(cursor.isAfterLast())) {
					Competitor competitor = new Competitor();
					competitor.setItem(cursor.getString(0));
					competitor.setQty(cursor.getString(1));
					competitor.setRate(cursor.getString(2));
					competitor.setRemark(cursor.getString(3));
					String filePath = cursor.getString(4);
					if(filePath.isEmpty() || filePath == null || filePath.equalsIgnoreCase("NA")||filePath.equalsIgnoreCase("N/A"))
					{
						competitor.setFilePath("");
					}
					else {
						competitor.setFilePath(filePath);
					}
					competitor.setCompetitorName(cursor.getString(5));
					competitor.setBrandActivity(cursor.getString(6));
					competitor.setMeetActivity(cursor.getString(7));
					competitor.setScheme(cursor.getString(8));
					competitor.setRoadShow(cursor.getString(9));
					competitor.setGeneralInfo(cursor.getString(10));
					competitor.setDiscount(cursor.getString(11));
					competitor.setOtherActivity(cursor.getString(12));
					competitor.setIsUpload(cursor.getString(13));
					competitor.setAndroid_id(cursor.getString(14));
					competitor.setDocId(cursor.getString(15));
					competitor.setVDate(cursor.getString(16));
					competitors.add(competitor);
					cursor.moveToNext();
				}
			} else {
				System.out.println("No records found");
			}
			cursor.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return competitors;
	}



	public boolean isAndroidIdExist(String android_doc_id)
	{
		Cursor c = database.query(DatabaseConnection.TABLE_COMPETITOR,null,"Android_id='" +android_doc_id + "'",null,null,null,null);
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
