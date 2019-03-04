package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.library.DateFunction;
import com.dm.model.Visit;

import java.util.ArrayList;

public class VisitController {

	boolean mTranse = false;
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	Context mContext;
	private String[] allColumns = { DatabaseConnection.COLUMN_ID,
			DatabaseConnection.COLUMN_VISIT_NO,
			DatabaseConnection.COLUMN_WEB_DOC_ID,
			DatabaseConnection.COLUMN_ANDROID_DOCID,
			DatabaseConnection.COLUMN_USR_ID,
			DatabaseConnection.COLUMN_VISIT_DATE,
			DatabaseConnection.COLUMN_CITY_CODE,
			DatabaseConnection.COLUMN_SREP_CODE,
			DatabaseConnection.COLUMN_DISTRIBUTER_ID,
			DatabaseConnection.COLUMN_AREA_CODE,
			DatabaseConnection.COLUMN_REMARK

	};

	public VisitController(Context context) {
		dbHelper = new DatabaseConnection(context);
		mContext = context;
	}

	public void open() {
		try {
			database = dbHelper.getWritableDatabase();
		} catch (SQLException e) {
			System.out.println("-----------------" + e.getMessage());
		}
	}

	public void close() {
		dbHelper.close();
	}

	public void insertVisit(Visit visit) {
		mTranse = true;
		database.beginTransaction();
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ID, visit.getVisId());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, visit.getVisitNo());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, visit.getVisitDocId());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,
				visit.getAndroidDocId());
		values.put(DatabaseConnection.COLUMN_USR_ID, visit.getUserId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, visit.getVdate());
		values.put(DatabaseConnection.COLUMN_CITY_CODE, visit.getCityId());
		values.put(DatabaseConnection.COLUMN_SREP_CODE, visit.getSMId());
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, visit.getDistId());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, visit.getAreaId());
		values.put(DatabaseConnection.COLUMN_REMARK, visit.getRemark());
		values.put(DatabaseConnection.COLUMN_STOCK, visit.getStock());

		values.put(DatabaseConnection.COLUMN_NEXT_VISIT_DATE, visit.getNextVisitDate());
		values.put(DatabaseConnection.COLUMN_FROM_TIME1, visit.getfrTime1());
		values.put(DatabaseConnection.COLUMN_TO_TIME1, visit.getToTime1());
		values.put(DatabaseConnection.COLUMN_NEXT_VISIT_TIME,
				visit.getToTime2());
		values.put(DatabaseConnection.COLUMN_IMAGE_PATH,
				visit.getFilePath());
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp( visit.getVdate()));
		System.out.println("row no " + visit.getAndroidDocId() + " inserted");
		if(visit.getVisitImport())
		{
			values.put(DatabaseConnection.COLUMN_TIMESTAMP,
					0);
			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
		}
		else
		{
			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
		}
		values.put(DatabaseConnection.COLUMN_LAT,visit.getLatitude());
		values.put(DatabaseConnection.COLUMN_LNG,visit.getLongitude());
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,visit.getLatlngTimeStamp());

		try {
			// long id = database.insert(DatabaseConnection.TABLE_VISITDIST,
			// null, values);

			long id = database.insertOrThrow(
					DatabaseConnection.TABLE_VISITDIST, null, values);
			System.out.println("id=" + id);
			database.setTransactionSuccessful();
			database.endTransaction();
			mTranse = false;

		} catch (RuntimeException e) {
			if(mTranse)
			{
				database.endTransaction();
				mTranse = false;
			}
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}

	public void insertVisit(
			String UserId,
			String CreatedDate,
			String VDate,
			String Android_Id,
			String Cityid,
			String DistId,
			String SpendFromTime,
			String NextVisitTime,
			String NextVisitDate,
			String Remark,
			String SMID,
			String VisDistId,
			String Stock,
			String SpendToDate,
			boolean getVisitImport,
			String path,String lat,String longi,String latlongtimestamp,String web_doc_id



	) {

		boolean flag=false,updateflag=false;
		ContentValues values = new ContentValues();
		String qry="select timestamp from TransVisitDist where Android_id='"+Android_Id+"'";
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
		//database.beginTransaction();
		mTranse = true;
//************************************************************
		DsrController dc = new DsrController(mContext);
		dc.open();
		boolean isDsrLock = dc.isDsrLockForVisitDate(VDate);
		dc.close();
		if(isDsrLock)
		{
			values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, Android_Id);
			values.put(DatabaseConnection.COLUMN_ID, VisDistId);
			values.put(DatabaseConnection.COLUMN_VISIT_NO, VisDistId);
			values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
			values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
			values.put(DatabaseConnection.COLUMN_CITY_CODE, Cityid);
			values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
			values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, DistId);
			values.put(DatabaseConnection.COLUMN_REMARK, Remark);
			values.put(DatabaseConnection.COLUMN_STOCK, Stock);
			values.put(DatabaseConnection.COLUMN_NEXT_VISIT_DATE, NextVisitDate);
			values.put(DatabaseConnection.COLUMN_FROM_TIME1, SpendFromTime);
			values.put(DatabaseConnection.COLUMN_TO_TIME1, SpendToDate);
			values.put(DatabaseConnection.COLUMN_NEXT_VISIT_TIME,
					NextVisitTime);
			values.put(DatabaseConnection.COLUMN_IMAGE_PATH,
					path);
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, web_doc_id);
			if (getVisitImport) {
				values.put(DatabaseConnection.COLUMN_TIMESTAMP,
						CreatedDate);
//			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			} else {
				values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
				values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction.ConvertDateToTimestamp(VDate + " 00:00:00", "dd/MMM/yyyy 00:00:00"));
			}
/************************		Write By Sandeep Singh 20-04-2017		******************/
			/*****************		START		******************/
			values.put(DatabaseConnection.COLUMN_LAT, lat);
			values.put(DatabaseConnection.COLUMN_LNG, longi);
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongtimestamp);
			long id = database.update(DatabaseConnection.TABLE_VISITDIST, values, "Android_id='" + Android_Id + "'", null);
			System.out.println("row=" + id);
			if(id>0)
			{
				// donothing
			}
			else
			{
				id = database.insert(DatabaseConnection.TABLE_VISITDIST, null, values);
				System.out.println("row=" + id);
			}
//

		}
		else {


			values.put(DatabaseConnection.COLUMN_ID, VisDistId);
			values.put(DatabaseConnection.COLUMN_VISIT_NO, VisDistId);
			values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
			values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
			values.put(DatabaseConnection.COLUMN_CITY_CODE, Cityid);
			values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
			values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, DistId);
			values.put(DatabaseConnection.COLUMN_REMARK, Remark);
			values.put(DatabaseConnection.COLUMN_STOCK, Stock);
			values.put(DatabaseConnection.COLUMN_NEXT_VISIT_DATE, NextVisitDate);
			values.put(DatabaseConnection.COLUMN_FROM_TIME1, SpendFromTime);
			values.put(DatabaseConnection.COLUMN_TO_TIME1, SpendToDate);
			values.put(DatabaseConnection.COLUMN_NEXT_VISIT_TIME,
					NextVisitTime);
			values.put(DatabaseConnection.COLUMN_IMAGE_PATH,
					path);
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, web_doc_id);
			if (getVisitImport) {
				values.put(DatabaseConnection.COLUMN_TIMESTAMP,
						CreatedDate);
//			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			} else {
				values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
				values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction.ConvertDateToTimestamp(VDate + " 00:00:00", "dd/MMM/yyyy 00:00:00"));
			}
/************************		Write By Sandeep Singh 20-04-2017		******************/
			/*****************		START		******************/
			values.put(DatabaseConnection.COLUMN_LAT, lat);
			values.put(DatabaseConnection.COLUMN_LNG, longi);
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongtimestamp);
			/*****************		END		******************/
//		try {
//			// long id = database.insert(DatabaseConnection.TABLE_VISITDIST,
//			// null, values);
//			long id = database.insertOrThrow(
//					DatabaseConnection.TABLE_VISITDIST, null, values);
//			System.out.println("id=" + id);
//
//		} catch (RuntimeException e) {
//			System.out.println("+++++++++++++++++++" + e.toString());
//		}

			long id = 0;
			try {
				if (flag) {
					if (!updateflag) {
						id = database.update(DatabaseConnection.TABLE_VISITDIST, values, "Android_id='" + Android_Id + "'", null);
//						database.setTransactionSuccessful();
//						database.endTransaction();
						mTranse = false;
					} else {
						CompetitorController.isUpdateFlag = true;
//						database.endTransaction();
						mTranse = false;
					}

				} else {

					id = database.insert(DatabaseConnection.TABLE_VISITDIST, null, values);
					System.out.println("row=" + id);
//					database.setTransactionSuccessful();
//					database.endTransaction();
					mTranse = false;
				}
			} catch (RuntimeException e) {
				if (mTranse) {
//					database.endTransaction();
					mTranse = false;
				}
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			values = null;
			cursor = null;
		}
		//		***************update code******************************************************
/*		database.beginTransaction();
		mTranse = true;
		int c=0;
		String qry="select count(*) from TransVisitDist where Android_id='"+Android_Id+"'";
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



		values.put(DatabaseConnection.COLUMN_ID, VisDistId);
		values.put(DatabaseConnection.COLUMN_VISIT_NO, VisDistId);
		values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
		values.put(DatabaseConnection.COLUMN_CITY_CODE,Cityid);
		values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, DistId);
		values.put(DatabaseConnection.COLUMN_REMARK, Remark);
		values.put(DatabaseConnection.COLUMN_STOCK, Stock);
		values.put(DatabaseConnection.COLUMN_NEXT_VISIT_DATE, NextVisitDate);
		values.put(DatabaseConnection.COLUMN_FROM_TIME1, SpendFromTime);
		values.put(DatabaseConnection.COLUMN_TO_TIME1, SpendToDate);
		values.put(DatabaseConnection.COLUMN_NEXT_VISIT_TIME,
				NextVisitTime);
		values.put(DatabaseConnection.COLUMN_IMAGE_PATH,
				path);
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
		if(getVisitImport)
		{
			values.put(DatabaseConnection.COLUMN_TIMESTAMP,
					CreatedDate);
//			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
		}
		else
		{
			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction.ConvertDateToTimestamp(VDate+" 00:00:00", "dd/MMM/yyyy 00:00:00"));
		}
*//************************		Write By Sandeep Singh 20-04-2017		******************//*
		*//*****************		START		******************//*
		values.put(DatabaseConnection.COLUMN_LAT, lat);
		values.put(DatabaseConnection.COLUMN_LNG, longi);
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongtimestamp);
		*//*****************		END		******************//*
//		try {
//			// long id = database.insert(DatabaseConnection.TABLE_VISITDIST,
//			// null, values);
//			long id = database.insertOrThrow(
//					DatabaseConnection.TABLE_VISITDIST, null, values);
//			System.out.println("id=" + id);
//
//		} catch (RuntimeException e) {
//			System.out.println("+++++++++++++++++++" + e.toString());
//		}

		long id=0;
		try{
			if(c>0)
			{
				try {

					id = database.update(DatabaseConnection.TABLE_VISITDIST,
							values, "Android_id='" +Android_Id + "'", null);
					System.out.println("row=" + id);
					database.setTransactionSuccessful();
					database.endTransaction();
					mTranse = false;
				} catch (RuntimeException e) {
					if(mTranse)
					{
						mTranse  = false;
						database.endTransaction();
					}
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {

					id = database.insert(DatabaseConnection.TABLE_VISITDIST, null, values);
					System.out.println("row=" + id);
					database.setTransactionSuccessful();
					database.endTransaction();
					mTranse = false;
				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
					if(mTranse)
					{
						mTranse = false;
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

		values=null;cursor=null;*/

	}


	public ArrayList<Visit> getFindDiscussionList(String distId, String vdate)
	{
		ArrayList<Visit> visits = new ArrayList<Visit>();

		String query = "select t.Android_id," +
				"t.remark," +
				"t.fromtime1," +
				"t.totime1," +
				"t.nextVisitTime," +
				"t.next_visit_date," +
				"t.stock," +
				"ifnull(t.path,'NA')," +
				"t.web_doc_id," +
				"t.visit_date," +
				"md.name," +
				"t.CreatedDate " +
				"from TransVisitDist t " +
				"left join mastDristributor md on md.webcode=t.DistId " +
				"where t.DistId='" + distId + "' and t.visit_date='"+vdate+"' order by t.CreatedDate desc";
		System.out.println("Distributor Collection List"+query);
			Cursor cursor = database.rawQuery(query, null);
			int count = cursor.getCount();
			System.out.println("count=" + count);
			if (cursor.moveToFirst()) {
				while (!(cursor.isAfterLast())) {
					Visit visit = new Visit();
					visit.setAndroidDocId(cursor.getString(0));
					visit.setRemark(cursor.getString(1));
					visit.setfrTime1(cursor.getString(2));
					visit.setToTime1(cursor.getString(3));
					visit.setToTime2(cursor.getString(4));
					visit.setNextVisitDate(cursor.getString(5));
					visit.setStock(cursor.getString(6));

					String file_path =  cursor.getString(7);
					if(file_path.equalsIgnoreCase("Na")|| file_path.equalsIgnoreCase("N/a") || file_path.isEmpty() || file_path == null)
					{
						visit.setFilePath("");
					}
					else
					{
						visit.setFilePath(cursor.getString(7));
					}

					visit.setVisitDocId(cursor.getString(8));
					visit.setVdate(cursor.getString(9));
					visit.setDistId(cursor.getString(10));
					visits.add(visit);
					cursor.moveToNext();
				}
			} else {
				System.out.println("No records found");
			}

			cursor.close();
			return visits;

	}

	public Visit getDiscussionByDate(String vdate, String distId) {
		Visit visit = new Visit();
		String query = "select Android_id,remark,fromtime1,totime1," +
				"nextVisitTime,next_visit_date,stock,path " +
				"from TransVisitDist where visit_date='"
				+ vdate + "' and DistId='" + distId + "'";
		try {
			Cursor cursor = database.rawQuery(query, null);
			int count = cursor.getCount();
			System.out.println("count=" + count);
			if (cursor.getCount() == 1) {
				cursor.moveToLast();
				visit.setAndroidDocId(cursor.getString(0));
				visit.setRemark(cursor.getString(1));
				visit.setfrTime1(cursor.getString(2));
				visit.setToTime1(cursor.getString(3));
				visit.setToTime2(cursor.getString(4));
				visit.setNextVisitDate(cursor.getString(5));
				visit.setStock(cursor.getString(6));
				visit.setFilePath(cursor.getString(7));
				cursor.moveToNext();
			} else {
				// visit.setAndroidDocId("0");
				// visit.setRemark("");
				visit = null;
				System.out.println("No records found");

			}
			cursor.close();
		} catch (Exception e) {
			System.out.println("e=" + e);
		}

		return visit;
	}


	public String getDistStock(String vdate, String distId) {
		String stock="0";
		String query = "select stock from TransVisitDist" +
				" where visit_date='" + vdate + "' and DistId='" + distId + "'";
		try {
			Cursor cursor = database.rawQuery(query, null);
			int count = cursor.getCount();
			System.out.println("count=" + count);
			if (cursor.moveToFirst()) {
				while (!(cursor.isAfterLast())) {

					stock = cursor.getString(0);
					cursor.moveToNext();
				}
			}else {
				// visit.setAndroidDocId("0");
				// visit.setRemark("");
				stock="0";
				System.out.println("No records found");

			}
			cursor.close();
		} catch (Exception e) {
			System.out.println("e=" + e);
		}

		return stock;
	}

	public Visit getDiscussionByAndroidId(String androidId) {
		Visit visit = new Visit();
		String query = "select Android_id,remark,fromtime1,totime1," +
				"nextVisitTime,next_visit_date,stock,ifnull(path,'NA'),DistId,isUpload " +
				"from TransVisitDist where Android_id='" + androidId + "'";

			Cursor cursor = database.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				visit.setAndroidDocId(cursor.getString(0));
				visit.setRemark(cursor.getString(1));
				visit.setfrTime1(cursor.getString(2));
				visit.setToTime1(cursor.getString(3));
				visit.setToTime2(cursor.getString(4));
				visit.setNextVisitDate(cursor.getString(5));
				visit.setStock(cursor.getString(6));
				visit.setFilePath(cursor.getString(7));
				visit.setDistId(cursor.getString(8));
				visit.setIsupload(cursor.getString(9));
				cursor.moveToNext();
			}
		}
			cursor.close();


		return visit;
	}

	public void updateVisit(Visit visit) {
		mTranse = true;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ID, visit.getVisId());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, visit.getVisitNo());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, visit.getVisitDocId());
		values.put(DatabaseConnection.COLUMN_USR_ID, visit.getUserId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, visit.getVdate());
		values.put(DatabaseConnection.COLUMN_CITY_CODE, visit.getCityId());
		values.put(DatabaseConnection.COLUMN_SREP_CODE, visit.getSMId());
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, visit.getDistId());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, visit.getAreaId());
		values.put(DatabaseConnection.COLUMN_REMARK, visit.getRemark());
		values.put(DatabaseConnection.COLUMN_STOCK, visit.getStock());
		values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
		values.put(DatabaseConnection.COLUMN_NEXT_VISIT_DATE,
				visit.getNextVisitDate());
		values.put(DatabaseConnection.COLUMN_FROM_TIME1, visit.getfrTime1());
		values.put(DatabaseConnection.COLUMN_TO_TIME1, visit.getToTime1());
		values.put(DatabaseConnection.COLUMN_NEXT_VISIT_TIME,
				visit.getToTime2());
		values.put(DatabaseConnection.COLUMN_IMAGE_PATH,
				visit.getFilePath());
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp( visit.getVdate()));
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
		values.put(DatabaseConnection.COLUMN_LAT,visit.getLatitude());
		values.put(DatabaseConnection.COLUMN_LNG,visit.getLongitude());
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,visit.getLatlngTimeStamp());
		try {
			database.beginTransaction();
			long id = database.update(DatabaseConnection.TABLE_VISITDIST,
					values, DatabaseConnection.COLUMN_ANDROID_DOCID + "='"
							+ visit.getAndroidDocId() + "'", null);
			System.out.println(id);
			database.setTransactionSuccessful();
			database.endTransaction();
			mTranse = false;
		} catch (RuntimeException e) {
			if(mTranse)
			{
				database.endTransaction();
				mTranse = false;
			}
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}

	public ArrayList<Visit> getUploadDistributerVisitList(String vdate) {
		/************************		Write By Sandeep Singh 13-04-2017		******************/
		/*****************		START		******************/
		String query = "select o.Android_id,o.usr_id,o.visit_date,o.srep_code," +
				"o.city_code,o.DistId,o.remark,vl1.visit_no," +
				"o.fromtime1,o.totime1,o.nextVisitTime,o.next_visit_date,o.path,o.stock, ifnull(o.latitude,0) as latitude,ifnull(o.longitude,0) as longitude,ifnull(o.LatlngTime,0) as LatlngTime"
				+ " from TransVisitDist o"
				+ " LEFT JOIN Visitl1 vl1"
				+ " ON o.visit_date = vl1.visit_date"
				+ " LEFT JOIN mastDristributor p"
				+ " ON o.DistId = p.webcode"
				+ " WHERE " +
				" o.timestamp =0 AND o.visit_date = '"+vdate+"' ";
		/*String query = "select o.Android_id,o.usr_id,o.visit_date,o.srep_code," +
				"o.city_code,o.DistId,o.remark,vl1.visit_no," +
				"o.fromtime1,o.totime1,o.nextVisitTime,o.next_visit_date,o.path,o.stock"
				+ " from TransVisitDist o"
				+ " LEFT JOIN Visitl1 vl1"
				+ " ON o.visit_date = vl1.visit_date"
				+ " LEFT JOIN mastDristributor p"
				+ " ON o.DistId = p.webcode"
				+ " WHERE  vl1.dsr_lock = '1'" +
				" AND o.isUpload = '0' AND o.visit_date = '"+vdate+"' ";*/
		/*****************		END		******************/

		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Visit> visits = new ArrayList<Visit>();
		if (cursor.moveToFirst())
		{
			while (!(cursor.isAfterLast())) {
				Visit visit = new Visit();
				visit.setAndroidDocId(cursor.getString(0));
				visit.setUserId(cursor.getString(1));
				visit.setVdate(cursor.getString(2));
				visit.setSMId(cursor.getString(3));
				visit.setCityId(cursor.getString(4));
				visit.setDistId(cursor.getString(5));
				visit.setRemark(cursor.getString(6));
				visit.setVisId(cursor.getString(7));
				visit.setfrTime1(cursor.getString(8));
				visit.setToTime1(cursor.getString(9));
				visit.setToTime2(cursor.getString(10));
				visit.setNextVisitDate(cursor.getString(11));
				visit.setFilePath(cursor.getString(12));
				visit.setStock(cursor.getString(13));
				/************************		Write By Sandeep Singh 20-04-2017		******************/
				/*****************		START		******************/
				visit.setLatitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT)));
				visit.setLongitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LNG)));
				visit.setLatlngTimeStamp(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT_LNG_TIME)));
				/*****************		END		******************/
				visits.add(visit);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return visits;
	}

	/*	public long updateDistributerDiscussionrUpload(String androiddocId, int vId) {
		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		public long updateDistributerDiscussionrUpload(String androiddocId, int vId,String docId,String timeStamp) {
			/*****************		END		******************/
			long id=0;
			ContentValues values = new ContentValues();
			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_VISIT_NO, vId);
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID,docId);

			/************************		Write By Sandeep Singh 10-04-2017		******************/
			/*****************		START		******************/
			values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
			/*****************		END		******************/
			try {
				id = database.update(DatabaseConnection.TABLE_VISITDIST,
						values, "Android_id='" + androiddocId + "'", null);
				System.out.println("row=" + id);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			return id;

		}

	public String getAndroidDocId(String distId, String vdate) {
		String Adoc_id = "0";
		String query = "select ifnull(Android_id,0) as MyColumn from TransVisitDist where DistId='"
				+ distId + "' and visit_date='" + vdate + "'";
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Adoc_id = cursor.getString(0);
			System.out.println("max id found " + Adoc_id);
				cursor.moveToNext();
			}
		}
		else {
			System.out.println("no max id found");
		}
//		if (cursor.getCount() == 1) {
//			cursor.moveToLast();
//			Adoc_id = cursor.getString(0);
//			System.out.println("max id found " + Adoc_id);
//
//		} else {
//			System.out.println("no max id found");
//		}
		System.out.println("no max id found" + Adoc_id);
		return Adoc_id;
	}



	public boolean checkDiscussionNo(String discussionNo)
	{
		Cursor cursor = database.query(DatabaseConnection.TABLE_VISITDIST,null, DatabaseConnection.COLUMN_ANDROID_DOCID+"=?",new String[]{discussionNo},null,null,null);
		if(cursor.getCount()>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
