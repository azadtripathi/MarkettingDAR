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
import com.dm.model.Visit;
import com.dm.util.Util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DsrController {
	boolean mTrans = false;
	private DatabaseConnection dbHelper;
	Context mcontext;
	private SQLiteDatabase database;
	private String[] allColumns = { DatabaseConnection.COLUMN_VISIT_NO,
			DatabaseConnection.COLUMN_DOC_ID,
			DatabaseConnection.COLUMN_ANDROID_DOCID,
			DatabaseConnection.COLUMN_USR_ID,
			DatabaseConnection.COLUMN_VISIT_DATE,
			DatabaseConnection.COLUMN_NEXT_VISIT_DATE,
			DatabaseConnection.COLUMN_REMARK,
			DatabaseConnection.COLUMN_SREP_CODE,
			DatabaseConnection.COLUMN_CITY_CODE,
			DatabaseConnection.COLUMN_DISTRIBUTER_ID,
			DatabaseConnection.COLUMN_NCITY_CODE,
			DatabaseConnection.COLUMN_FROM_TIME1,
			DatabaseConnection.COLUMN_FROM_TIME2,
			DatabaseConnection.COLUMN_TO_TIME1,
			DatabaseConnection.COLUMN_TO_TIME2,
			DatabaseConnection.COLUMN_WITH_USR_ID,
			DatabaseConnection.COLUMN_MODE_OF_TRANSPORT,
			DatabaseConnection.COLUMN_VEHICLE_USED,
			DatabaseConnection.COLUMN_INDUSTRY_ID,
			DatabaseConnection.COLUMN_DSRLOCK };

	public DsrController(Context context) {
		dbHelper = new DatabaseConnection(context);
		mcontext = context;
	}

	public void open() {
		try {
			database = dbHelper.getWritableDatabase();
		} catch (SQLException e) {
			System.out.println("-----------------" + e.getMessage());
		}
	}




	public void updateUnlockTablesTimestamp() {
		ArrayList<String> visitno=new ArrayList<>();
		try {
			String query = "select visit_date from Visitl1 where dsr_lock='0'";
			System.out.println(query);
			Cursor cursor = database.rawQuery(query, null);
			if (cursor.moveToFirst()) {
				while (!(cursor.isAfterLast())) {
					visitno.add(cursor.getString(0));
					cursor.moveToNext();
				}
			} else {

				System.out.println("No records found");
			}

			cursor.close();
			if(visitno.size()>0)
			{
				ContentValues values = new ContentValues();
				values.put(DatabaseConnection.COLUMN_TIMESTAMP, -1);
				for(int i=0;i<visitno.size();i++)
				{
					String vdate=visitno.get(i);
					try {
						long id1 = database.update(DatabaseConnection.TABLE_VISITL1, values,
								"visit_date='" + visitno.get(i) + "'", null);
					} catch (RuntimeException e) {
						System.out.println("+++++++++++++++++++" + e.toString());
					}
					try {
						long id1 = database.update(DatabaseConnection.TABLE_TRANSDEMO, values,
								"visit_date='" + visitno.get(i) + "'", null);
					} catch (RuntimeException e) {
						System.out.println("+++++++++++++++++++" + e.toString());
					}
					try {
						long id2 = database.update(DatabaseConnection.TABLE_ORDER, values,
								"visit_date='" + visitno.get(i) + "'", null);
					} catch (RuntimeException e) {
						System.out.println("+++++++++++++++++++" + e.toString());
					}
					try {
						long id8 = database.update(DatabaseConnection.TABLE_ORDER1, values,
								"visit_date='" + visitno.get(i) + "'", null);
					} catch (RuntimeException e) {
						System.out.println("+++++++++++++++++++" + e.toString());
					}
					try {
						long id3 = database.update(DatabaseConnection.TABLE_TRANSFAILED_VISIT, values,
								"visit_date='" + visitno.get(i) + "'", null);
					} catch (RuntimeException e) {
						System.out.println("+++++++++++++++++++" + e.toString());
					}
					try {
						long id4 = database.update(DatabaseConnection.TABLE_TRANSCOLLECTION, values,
								"visit_date='" + visitno.get(i) + "'", null);
					} catch (RuntimeException e) {
						System.out.println("+++++++++++++++++++" + e.toString());
					}
					try {
						long id5 = database.update(DatabaseConnection.TABLE_COMPETITOR, values,
								"visit_date='" + visitno.get(i) + "'", null);
					} catch (RuntimeException e) {
						System.out.println("+++++++++++++++++++" + e.toString());
					}
					try {
						long id6 = database.update(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION, values,
								"visit_date='" + visitno.get(i) + "'", null);
					} catch (RuntimeException e) {
						System.out.println("+++++++++++++++++++" + e.toString());
					}
					try {
						long id7 = database.update(DatabaseConnection.TABLE_VISITDIST, values,
								"visit_date='" + visitno.get(i) + "'", null);
					} catch (RuntimeException e) {
						System.out.println("+++++++++++++++++++" + e.toString());
					}
					try {
						long id7 = database.update(DatabaseConnection.TABLE_TRANS_DIST_STOCK, values,
								"visit_date='" + visitno.get(i) + "'", null);
					} catch (RuntimeException e) {
						System.out.println("+++++++++++++++++++" + e.toString());
					}
				}
			}
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}
	/********************************************** END  *******************************/
	public void close() {
		dbHelper.close();
	}

	public int insertDsr(Visit visit) {
		mTrans = true;
		database.beginTransaction();
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,	visit.getAndroidDocId());
		values.put(DatabaseConnection.COLUMN_USR_ID, visit.getUserId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, visit.getVdate());
		values.put(DatabaseConnection.COLUMN_NEXT_VISIT_DATE,visit.getNextVisitDate());
		values.put(DatabaseConnection.COLUMN_REMARK, visit.getRemark());
		values.put(DatabaseConnection.COLUMN_SREP_CODE, visit.getSMId());
		values.put(DatabaseConnection.COLUMN_CITY_CODE, visit.getCityId());
		values.put(DatabaseConnection.COLUMN_CITY_IDS, visit.getCityIds());
		values.put(DatabaseConnection.COLUMN_CITY_NAMES, visit.getCityName());
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, visit.getDistId());
		values.put(DatabaseConnection.COLUMN_NCITY_CODE, visit.getNCityId());
		values.put(DatabaseConnection.COLUMN_FROM_TIME1, visit.getfrTime1());
		values.put(DatabaseConnection.COLUMN_FROM_TIME2, visit.getfrTime1());
		values.put(DatabaseConnection.COLUMN_TO_TIME1, visit.getToTime1());
		values.put(DatabaseConnection.COLUMN_TO_TIME2, visit.getToTime2());
		values.put(DatabaseConnection.COLUMN_WITH_USR_ID, visit.getWithUserId());
		values.put(DatabaseConnection.COLUMN_NEXTWITH_USR_ID,visit.getNextwithUserId());
		values.put(DatabaseConnection.COLUMN_MODE_OF_TRANSPORT,	visit.getModeOfTransport());
		values.put(DatabaseConnection.COLUMN_VEHICLE_USED,	visit.getVehicleUsed());
		values.put(DatabaseConnection.COLUMN_INDUSTRY_ID, visit.getIndustry());
		values.put(DatabaseConnection.COLUMN_ORDER_BY_EMAIL, visit.getOrderByEmail());
		values.put(DatabaseConnection.COLUMN_ORDER_BY_PHONE, visit.getOrderByPhone());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, visit.getAreaId());
		values.put(DatabaseConnection.COLUMN_OTHER_EXPENSE, visit.getOtherExp());
		values.put(DatabaseConnection.COLUMN_OTHER_EXPENSE_REMARK, visit.getOtherExpRem());
		values.put(DatabaseConnection.COLUMN_VISIT, visit.getVisitName());
		values.put(DatabaseConnection.COLUMN_ATTANDANCE, visit.getAttandanse());
		values.put(DatabaseConnection.COLUMN_TO_AREA, visit.getToAreaId());
		values.put(DatabaseConnection.COLUMN_CHARGEABLE, (visit.getChargeable()==null?"NA":visit.getChargeable()));
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(visit.getVdate()));

		values.put(DatabaseConnection.COLUMN_LAT,visit.getLatitude());
		values.put(DatabaseConnection.COLUMN_LNG,visit.getLongitude());
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,visit.getLatlngTimeStamp());
		if(visit.getVisitImport())
		{
			values.put(DatabaseConnection.COLUMN_VISIT_NO, visit.getVisId()==null?0:Integer.parseInt(visit.getVisId()));
			values.put(DatabaseConnection.COLUMN_DOC_ID, visit.getVisitDocId());
			values.put(DatabaseConnection.COLUMN_DSRLOCK, "1");
			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, visit.getCreatedDate());
		}
		else
		{
			values.put(DatabaseConnection.COLUMN_VISIT_NO, visit.getVisId());
			values.put(DatabaseConnection.COLUMN_DOC_ID, visit.getVisitDocId());
			values.put(DatabaseConnection.COLUMN_DSRLOCK, "0");
			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
//			values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction.ConvertDateToTimestamp(visit.getVdate()+" 00:00:00", "dd/MMM/yyyy 00:00:00"));
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
		}
		long id=0;
		try {

			 id = database.insert(DatabaseConnection.TABLE_VISITL1, null,values);
			System.out.println(id);
			database.setTransactionSuccessful();
			database.endTransaction();
		} catch (RuntimeException e) {
			if(mTrans)
			{
				mTrans = false;
				database.endTransaction();
			}
			System.out.println("+++++++++++++++++++" + e.toString());
		}
		int v=0;
		if(id>0)
		{
			v=getLocalVisitId(visit.getAndroidDocId(),visit.getVdate());
		}
		return v;
	}

public int getLocalVisitId(String androidId,String vdate)
{ int	v=0;
	String query = "select PK_id as visitId from visitl1 where Android_id='"+androidId+"' and visit_date='"+vdate+"'";
	System.out.println(query);
	Cursor cursor = database.rawQuery(query, null);
	if (cursor.moveToFirst()) {
		while (!(cursor.isAfterLast())) {
			v=cursor.getInt(0);
			cursor.moveToNext();
		}
	}
	else {
		System.out.println("No records found");
	}
	cursor.close();
	return v;
}


	public void insertDsr(
			String UserId,
			String Createddate,
			String Vdate,
			String Android_Id,
			String CityId,
			String cityIds,
			String cityName,
			String DistId,
			String Lock,
			String FROMTime,
			String ToTime,
			String Industry,
			String ModeOfTransport,
			String nCityId,
			String NextVisitDate,
			String WithUserId,
			String Remark,
			String SMID,
			String VehicleUsed,
			String VisId,
			String visitDocId,
			String nWithUserId ,
			String OrderAmountMail,
			String OrderAmountPhone,
			String visitcode,
			String Attendance,
			String Fromareacode,
			String OtherExpense,
			String AndroidAppRemark1,
			String ToAreacode,
			boolean getVisitImport,
			String latitude,
			String longitude,
			String latlongtimestamp

	) {

		//		***************update code******************************************************

		mTrans = true;
		String dsrLock = "";
		ContentValues values = new ContentValues();
		boolean flag=false,updateflag=false;
		String qry="select timestamp,dsr_lock,Android_id from Visitl1 where Android_id='"+Android_Id+"' OR visit_date='"+Vdate+"'";
		System.out.println(qry);
		Cursor cursor = database.rawQuery(qry, null);
		String mobAndroid_Id = "";
		String mobileDsrLock = "";
		if(cursor.getCount()>0)
		{
			if(cursor.moveToLast())
			{
				mobAndroid_Id = cursor.getString(2);
				mobileDsrLock = cursor.getString(1);
			}
			UserDataController dc = new UserDataController(mcontext);
			dc.open();
			if(Lock.equals("1")) {
				//	dc.deleteAllTransactionDataVdateWise(Vdate);
			}
			dc.close();
		}
		cursor = database.rawQuery(qry, null);
		if(cursor.moveToFirst())
		{
			while(!(cursor.isAfterLast()))
			{
				String timestamp=cursor.getString(0);
				dsrLock = cursor.getString(1);
				if(timestamp.equalsIgnoreCase("0"))
				{
					flag=true;
				}
				else
				{
					flag=true;
					updateflag=false;
				}
				cursor.moveToNext();
			}
		}
		else
		{
			flag=false;
			values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, Android_Id);
		}
		cursor.close();
//*********************************************************************************************
		values.put(DatabaseConnection.COLUMN_USR_ID,UserId);
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, Vdate);
		values.put(DatabaseConnection.COLUMN_NEXT_VISIT_DATE,NextVisitDate);
		values.put(DatabaseConnection.COLUMN_REMARK, Remark);
		values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
		values.put(DatabaseConnection.COLUMN_CITY_CODE, CityId);
		values.put(DatabaseConnection.COLUMN_CITY_IDS,cityIds);
		values.put(DatabaseConnection.COLUMN_CITY_NAMES, cityName);
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, DistId);
		values.put(DatabaseConnection.COLUMN_NCITY_CODE, nCityId);
		values.put(DatabaseConnection.COLUMN_FROM_TIME1, FROMTime);
		values.put(DatabaseConnection.COLUMN_FROM_TIME2, FROMTime);
		values.put(DatabaseConnection.COLUMN_TO_TIME1, ToTime);
		values.put(DatabaseConnection.COLUMN_TO_TIME2, ToTime);
		values.put(DatabaseConnection.COLUMN_WITH_USR_ID, WithUserId);
		values.put(DatabaseConnection.COLUMN_NEXTWITH_USR_ID,
				nWithUserId);
		values.put(DatabaseConnection.COLUMN_MODE_OF_TRANSPORT,
				ModeOfTransport);
		values.put(DatabaseConnection.COLUMN_VEHICLE_USED,
				VehicleUsed);
		values.put(DatabaseConnection.COLUMN_INDUSTRY_ID,Industry);
		values.put(DatabaseConnection.COLUMN_ORDER_BY_EMAIL, OrderAmountMail);
		values.put(DatabaseConnection.COLUMN_ORDER_BY_PHONE, OrderAmountPhone);
		values.put(DatabaseConnection.COLUMN_AREA_CODE, Fromareacode);
		values.put(DatabaseConnection.COLUMN_OTHER_EXPENSE, OtherExpense);
		values.put(DatabaseConnection.COLUMN_OTHER_EXPENSE_REMARK, AndroidAppRemark1);
		values.put(DatabaseConnection.COLUMN_VISIT, visitcode);
		values.put(DatabaseConnection.COLUMN_ATTANDANCE, Attendance);
		values.put(DatabaseConnection.COLUMN_TO_AREA, ToAreacode);
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(Vdate));
		values.put(DatabaseConnection.COLUMN_LAT, latitude);
		values.put(DatabaseConnection.COLUMN_LNG, longitude);
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,latlongtimestamp);
		values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId==null?0:Integer.parseInt(VisId));
		values.put(DatabaseConnection.COLUMN_DOC_ID, visitDocId);

		values.put(DatabaseConnection.COLUMN_DSRLOCK, Lock.equalsIgnoreCase("1")?"1":"0");

		values.put(DatabaseConnection.COLUMN_UPLOAD, (Lock.equalsIgnoreCase("1")?"1":"0"));
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, Createddate);
		values.put(DatabaseConnection.COLUMN_IMPORT_TIME_STAMP,Createddate);
		long id=0;
		try{
			if(flag)
			{

				/*UserDataController dc = new UserDataController(mcontext);
				dc.open();
				if(Lock.equals("1"))
				dc.deleteAllTransactionDataVdateWise(Vdate);
				dc.close();*/

				if(!updateflag){
					if(!dsrLock.isEmpty())
					{
						if(Lock.equals("1"))
						{
							values.put(DatabaseConnection.COLUMN_DSRLOCK, Lock);
							if((!mobAndroid_Id.equals(Android_Id))  || (mobAndroid_Id.equals(Android_Id) && (mobileDsrLock.equals("0") && Lock.equals("1"))))
							{
								values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
							}
						}
						else
						{
							values.put(DatabaseConnection.COLUMN_DSRLOCK, dsrLock);
						}
					}
					id = database.update(DatabaseConnection.TABLE_VISITL1,
							values, "visit_date='" +Vdate + "'", null);


					if(id==0)
					{
						id = database.insert(DatabaseConnection.TABLE_VISITL1, null, values);
					}
					System.out.println("row=" + id);
				} else
				{
					CompetitorController.isUpdateFlag = true;
				}
			}
			else
			{
				id = database.insert(DatabaseConnection.TABLE_VISITL1, null, values);
				System.out.println("row=" + id);
				mTrans = false;
			}
		}
		catch(RuntimeException e){
			if(mTrans)
			{
				database.endTransaction();
				mTrans = false;
			}
			System.out.println("+++++++++++++++++++"+e.toString());
		}

		values=null;cursor=null;

	}



	public int getUpdateDsr(Visit visit) {
		ContentValues values = new ContentValues();

//		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,
//				visit.getAndroidDocId());
		values.put(DatabaseConnection.COLUMN_USR_ID, visit.getUserId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, visit.getVdate());
		values.put(DatabaseConnection.COLUMN_NEXT_VISIT_DATE,	visit.getNextVisitDate());
		values.put(DatabaseConnection.COLUMN_REMARK, visit.getRemark());
		values.put(DatabaseConnection.COLUMN_SREP_CODE, visit.getSMId());
		values.put(DatabaseConnection.COLUMN_CITY_CODE, visit.getCityId());
		values.put(DatabaseConnection.COLUMN_CITY_IDS, visit.getCityIds());
		values.put(DatabaseConnection.COLUMN_CITY_NAMES, visit.getCityName());
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, visit.getDistId());
		values.put(DatabaseConnection.COLUMN_NCITY_CODE, visit.getNCityId());
		values.put(DatabaseConnection.COLUMN_FROM_TIME1, visit.getfrTime1());
		values.put(DatabaseConnection.COLUMN_FROM_TIME2, visit.getfrTime1());
		values.put(DatabaseConnection.COLUMN_TO_TIME1, visit.getToTime1());
		values.put(DatabaseConnection.COLUMN_TO_TIME2, visit.getToTime2());
		values.put(DatabaseConnection.COLUMN_WITH_USR_ID, visit.getWithUserId());
		values.put(DatabaseConnection.COLUMN_NEXTWITH_USR_ID,	visit.getNextwithUserId());
		values.put(DatabaseConnection.COLUMN_MODE_OF_TRANSPORT,				visit.getModeOfTransport());
		values.put(DatabaseConnection.COLUMN_VEHICLE_USED,				visit.getVehicleUsed());
		values.put(DatabaseConnection.COLUMN_INDUSTRY_ID, visit.getIndustry());
		values.put(DatabaseConnection.COLUMN_ORDER_BY_EMAIL, visit.getOrderByEmail());
		values.put(DatabaseConnection.COLUMN_ORDER_BY_PHONE, visit.getOrderByPhone());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, visit.getAreaId());
		values.put(DatabaseConnection.COLUMN_OTHER_EXPENSE, visit.getOtherExp());
		values.put(DatabaseConnection.COLUMN_OTHER_EXPENSE_REMARK, visit.getOtherExpRem());
		values.put(DatabaseConnection.COLUMN_VISIT, visit.getVisitName());
		values.put(DatabaseConnection.COLUMN_ATTANDANCE, visit.getAttandanse());
		values.put(DatabaseConnection.COLUMN_TO_AREA, visit.getToAreaId());
		values.put(DatabaseConnection.COLUMN_CHARGEABLE, visit.getChargeable());
//		values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction.ConvertDateToTimestamp(visit.getVdate()+" 00:00:00", "dd/MMM/yyyy 00:00:00"));
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(visit.getVdate()));
		values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
		long	id=0;
		try {
				id = database.update(DatabaseConnection.TABLE_VISITL1,  values, DatabaseConnection.COLUMN_ANDROID_DOCID + "='"	+ visit.getAndroidDocId() + "'", null);
			/*long id;
			if(visit.getAndroidDocId().equalsIgnoreCase("0") ||  visit.getAndroidDocId().equalsIgnoreCase(null)){
				 id = database.update(DatabaseConnection.TABLE_VISITL1,  values, DatabaseConnection.COLUMN_ANDROID_DOCID + "='"	+ visit.getAndroidDocId() + "'", null);
			}
			else{
				 id = database.update(DatabaseConnection.TABLE_VISITL1,  values, DatabaseConnection.COLUMN_DOC_ID + "='"	+ visit.getAndroidDocId() + "'", null);
			}*/
			System.out.println("Update"+id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
		int v=0;
		if(id>0)
		{
			v=getLocalVisitId(visit.getAndroidDocId(),visit.getVdate());
		}
		return v;
	}


//	public void getUpdateDsr(Visit dsr) {
//		Calendar c = Calendar.getInstance();
//		System.out.println("Current time => " + c.getTime());
//		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
//		String query = "update visitl1 set "
//		          +"doc_id='" + dsr.getVisitDocId()+ "',"
//		           +"timestamp='" + DateFunction.ConvertDateToTimestamp(dsr.getVdate()+" 00:00:00", "dd/MMM/yyyy 00:00:00")+ "',"
//				+ "usr_id='" + dsr.getUserId() + "',"
//				 + "visit_date='"
//				+ dsr.getVdate() + "',"
//				 + "next_visit_date='"
//				+ dsr.getNextVisitDate() + "',"
//				+ "orderbyemail='"
//				+ dsr.getOrderByEmail() + "',"
//				+ "orderbyphone='"
//				+ dsr.getOrderByPhone() + "',"
//				+ "area_code='"
//				+ dsr.getAreaId() + "',"
//				+ "Attandance='"
//				+ dsr.getAttandanse() + "',"
//				+ "OtherExp='"
//				+ dsr.getOtherExp() + "',"
//				+ "OtherExpRmk='"
//				+ dsr.getOtherExpRem() + "',"
//				+ "Visit='"
//				+ dsr.getVisitName() + "',"
//				+ "toArea='"
//				+ dsr.getToAreaId() + "',"
//				+ "chargeable='"
//				+ dsr.getChargeable() + "',"
//				+ "remark='" + dsr.getRemark()
//				+ "'," + "srep_code='"
//				 + dsr.getSMId() + "',"
//				+ "city_code='"
//				+ dsr.getCityId()
//				+ "'," + "fromtime1='" + dsr.getfrTime1()
//				+ "'," + "totime1='" + dsr.getToTime1() + "'," + "Withusr_id='"
//				+ dsr.getWithUserId() + "'," + "nextWithUserId='"
//				+ dsr.getNextwithUserId() + "'," + "mode_of_transport='"
//				+ dsr.getModeOfTransport() + "'," + "cityIds='"
//				+ dsr.getCityIds() + "'," + "cityNames='"
//				+ dsr.getCityName() + "'," + "vehicle_used='"
//				+ dsr.getVehicleUsed() + "'" + " where Android_id=" + "'"
//				+ dsr.getAndroidDocId() + "'";
//		System.out.println(query);
//		try {
//			// String
//			// qurey="UPDATE order_sub SET remark="+""+" WHERE ExecutionDate = '2011-10-12 00:00:00.000'";
//
//			database.execSQL(query);
//			System.out.println(" Dsr updated");
//		}
//		catch (RuntimeException e) {
//			System.out.println("+++++++++++++++++++" + e.toString());
//		}
//
//	}


	public boolean checkDsrinTable(String vDate)
	{
		Cursor cursor = database.query(DatabaseConnection.TABLE_VISITL1,null,"visit_date=?",new String[]{vDate},null,null,null,null);
		if(cursor.getCount()>0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	public void DsrLocked(String date,String imagePathonLock,String lockDsrTime) {
		Calendar c = Calendar.getInstance();
		/************************		Write By Sandeep Singh 13-04-2017		******************/
		/*****************		START		******************/
		String query ="";
		if(lockDsrTime.isEmpty())
		{
			query = "update visitl1 set " + "dsr_lock='1',timestamp=0"+ " where visit_date=" + "'" + date + "'";
		}
		else
		{
			query = "update visitl1 set " + "dsr_lock='1',totime1='"+lockDsrTime+"', timestamp=0"+ " where visit_date=" + "'" + date + "'";
		}
		/*String query = "update visitl1 set " + "dsr_lock='1'"
				+ " where visit_date=" + "'" + date + "'";*/
		/*****************		END		******************/




		System.out.println(query);
		try {
			database.execSQL(query);
			System.out.println(" Dsr Locked");
			ContentValues values = new ContentValues();
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");
			values.put(DatabaseConnection.COLUMN_IMAGE_PATH_LOCK,imagePathonLock);
			try {
				long id1 = database.update(DatabaseConnection.TABLE_VISITL1, values,
						"visit_date='" + date + "'", null);
			}
			catch (Exception e)
			{

			}
			values = new ContentValues();
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");
			try {
				long id1 = database.update(DatabaseConnection.TABLE_TRANSDEMO, values,
						"visit_date='" +date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id2 = database.update(DatabaseConnection.TABLE_ORDER, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id8 = database.update(DatabaseConnection.TABLE_ORDER1, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id3 = database.update(DatabaseConnection.TABLE_TRANSFAILED_VISIT, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id4 = database.update(DatabaseConnection.TABLE_TRANSCOLLECTION, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id5 = database.update(DatabaseConnection.TABLE_COMPETITOR, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id6 = database.update(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id7 = database.update(DatabaseConnection.TABLE_VISITDIST, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id7 = database.update(DatabaseConnection.TABLE_TRANS_DIST_STOCK, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id7 = database.update(DatabaseConnection.TABLE_SALES_RETURN, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id7 = database.update(DatabaseConnection.TABLE_SALES_RETURN1, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}

		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}

	}

	public void DsrLocked(String date) {
		Calendar c = Calendar.getInstance();

		String query = "update visitl1 set " + "dsr_lock='1',timestamp=0"+ " where visit_date=" + "'" + date + "'";
	/*	*//**//*String query = "update visitl1 set " + "dsr_lock='1'"
				+ " where visit_date=" + "'" + date + "'";*//**//*
		*//**//*****************		END		******************//**//**/


		System.out.println(query);
		try {
			database.execSQL(query);
			System.out.println(" Dsr Locked");
			ContentValues values = new ContentValues();
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");
			try {
				long id1 = database.update(DatabaseConnection.TABLE_TRANSDEMO, values,
						"visit_date='" +date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id2 = database.update(DatabaseConnection.TABLE_ORDER, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id8 = database.update(DatabaseConnection.TABLE_ORDER1, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id3 = database.update(DatabaseConnection.TABLE_TRANSFAILED_VISIT, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id4 = database.update(DatabaseConnection.TABLE_TRANSCOLLECTION, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id5 = database.update(DatabaseConnection.TABLE_COMPETITOR, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id6 = database.update(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id7 = database.update(DatabaseConnection.TABLE_VISITDIST, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id7 = database.update(DatabaseConnection.TABLE_TRANS_DIST_STOCK, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}

			try {
				long id7 = database.update(DatabaseConnection.TABLE_SALES_RETURN, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			try {
				long id7 = database.update(DatabaseConnection.TABLE_SALES_RETURN1, values,
						"visit_date='" + date + "'", null);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}

		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}

	}

	public void DsrUnLocked(String date)
	{
		Calendar c = Calendar.getInstance();
		String query = "update visitl1 set " + "dsr_lock='0',timestamp=0"+ " where visit_date=" + "'" + date + "'";
		System.out.println(query);
		try
		{
			database.execSQL(query);
			System.out.println(" Dsr UnLock");
		}
		catch (RuntimeException e)
		{
			System.out.println("+++++++++++++++++++" + e.toString());
		}

	}

	public ArrayList<String> checkDsrListToLock()
	{
		Cursor cursor=null;
//		String sql = "select vdate,max(demodate) as demo,max(odate) as ordr,max(fdate) as fvisit,max(dfdate) as dfvisit,max(cdate) as comp  from ( "
//				+ " select d.visit_date as vdate,1 as demodate ,0 as odate     ,0 as fdate,0 as cdate from TransDemo d LEFT JOIN Visitl1 vl1  ON d.visit_date = vl1.visit_date Where d.isUpload='0' and  vl1.dsr_lock = '1' union all "
//				+ " select o.visit_date as vdate,0 as demodate ,1 as odate     ,0 as fdate,0 as cdate from TransOrder o LEFT JOIN Visitl1 vl1  ON o.visit_date = vl1.visit_date Where o.isSubUpload='0' and vl1.dsr_lock = '1' union all "
//				+ " select f.visit_date as vdate,0 as demodate ,0 as orderadte,1 as fdate,0 as cdate from Trans_Failed_visit f LEFT JOIN Visitl1 vl1  ON f.visit_date = vl1.visit_date Where f.isUpload='0' and  vl1.dsr_lock = '1' union all "
//				+ " select f.visit_date as vdate,0 as demodate ,0 as orderadte,1 as fdate,0 as cdate from Trans_Failed_visit f LEFT JOIN Visitl1 vl1  ON f.visit_date = vl1.visit_date Where f.isUpload='0' and  vl1.dsr_lock = '1' union all "
//				+ " select c.visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,1 as cdate from TransCompetitor c LEFT JOIN Visitl1 vl1  ON c.visit_date = vl1.visit_date Where c.isUpload='0' and  vl1.dsr_lock = '1' union all "
//				+ " select vl1.visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as cdate from Visitl1 vl1 Where vl1.isUpload='0' and vl1.dsr_lock = '1' "
//				+ " )  a "
//				+ " group by vdate order by substr(vdate,7,4),substr(vdate,4,2),substr(vdate,1,2) desc";



		String sql="select vdate,max(demodate) as demo,max(odate) as ordr,max(fdate) as fvisit,max(dfdate) as dfvisit,max(cdate) as comp,max(coll) as collection,max(distcoll) as distcollection, max(distdiscu) as discussion  from ( "
				+" select d.visit_date as vdate,1 as demodate ,0 as odate     ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu from TransDemo d LEFT JOIN Visitl1 vl1  ON d.visit_date = vl1.visit_date Where d.timestamp='0'  union all "
				+" select o.visit_date as vdate,0 as demodate ,1 as odate     ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu from TransOrder o LEFT JOIN Visitl1 vl1  ON o.visit_date = vl1.visit_date Where o.timestamp='0'  union all "
				+" select tf.visit_date as vdate,0 as demodate ,0 as odate,1 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu from Trans_Failed_visit tf LEFT JOIN Visitl1 vl1  ON tf.visit_date = vl1.visit_date where tf.DistId IS NULL and tf.party_code IS NOT NULL  and tf.timestamp='0'  union all "
				+" select df.visit_date as vdate,0 as demodate ,0 as odate,0 as fdate,1 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu from Trans_Failed_visit df LEFT JOIN Visitl1 vl1  ON df.visit_date = vl1.visit_date where df.DistId IS NOT NULL and df.party_code IS NULL   and df.timestamp='0' union all "
				+" select c.visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,1 as cdate,0 as coll,0 as distcoll,0 as distdiscu from TransCompetitor c LEFT JOIN Visitl1 vl1  ON c.visit_date = vl1.visit_date Where c.timestamp='0'  union all "
				+" select tc.visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,1 as coll,0 as distcoll,0 as distdiscu from TransCollection tc LEFT JOIN Visitl1 vl1  ON tc.visit_date = vl1.visit_date Where tc.timestamp='0'  union all "
				+" select dc.visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,1 as distcoll,0 as distdiscu from DistributerCollection dc LEFT JOIN Visitl1 vl1  ON dc.visit_date = vl1.visit_date Where dc.timestamp='0'  union all "
				+" select vd.visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,1 as distdiscu  from TransVisitDist vd LEFT JOIN Visitl1 vl1  ON vd.visit_date = vl1.visit_date Where vd.timestamp='0'  union all "
				+" select vl1.visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu from Visitl1 vl1 Where vl1.timestamp='0' "
				+" )  a "
				+" group by vdate order by substr(vdate,7,4),substr(vdate,4,2),substr(vdate,1,2) desc";

		System.out.println(sql);
		try{
		cursor = database.rawQuery(sql, null);
		}
		catch(Exception e)
		{
			System.out.println("e="+e);
		}
		ArrayList<String> areaArray = new ArrayList<String>();
		if (cursor.moveToFirst())
		{
			while (!(cursor.isAfterLast())) {

				areaArray.add(cursor.getString(0));
				System.out.println(cursor.getString(0));
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return areaArray;
	}

	public void notUploadVisitl1(String androiddoc_id) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");
//  values.put(DatabaseConnection.COLUMN_VISIT_NO, vno);
		try {
			long id = database.update(DatabaseConnection.TABLE_VISITL1, values,
					"Android_id='" + androiddoc_id + "'", null);
			System.out.println("row=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}


	public int checkDsrDateToLock(String date,String smId) {
		int c=0;
		String query = "select count(*) from visitl1 where CreatedDate<'"+date+"' and srep_code='"+smId+"' and dsr_lock=0";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				c=cursor.getInt(0);
				cursor.moveToNext();
			}
		}
		else {
			System.out.println("No records found");
		}
		cursor.close();
		return c;
	}




	

	public int checkPendingDsr() {
		int c=0;
		String query = "select count(*) from visitl1 where timestamp=0";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				c=cursor.getInt(0);
				cursor.moveToNext();
			}
		}
		else {
			System.out.println("No records found");
		}
		cursor.close();
		return c;
	}



	public ArrayList<Visit> getDsrListFromAndToDate(String fromDate, String toDate) {
		String query = "select Android_id,visit_date,next_visit_date,"
				+ "remark,city_code,fromtime1,totime1,Withusr_id,nextWithUserId,"
				+ "mode_of_transport,vehicle_used,isUpload,cityIds,cityNames,orderbyemail,orderbyphone,doc_id,visit_no,dsr_lock "
				+ "from Visitl1 where CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate)+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate)+"' order by CreatedDate desc";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Visit> dsrArray = new ArrayList<Visit>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Visit dsr = new Visit();
				dsr.setAndroidDocId(cursor.getString(0));
				dsr.setVdate(cursor.getString(1));
				dsr.setNextVisitDate(cursor.getString(2));
				dsr.setRemark(cursor.getString(3));
				dsr.setCityId(cursor.getString(4));
				dsr.setfrTime1(cursor.getString(5));
				dsr.setToTime1(cursor.getString(6));
				dsr.setWithUserId(cursor.getString(7));
				dsr.setNextwithUserId(cursor.getString(8));
				dsr.setModeOfTransport(cursor.getString(9));
				dsr.setVehicleUsed(cursor.getString(10));
				dsr.setIsupload(cursor.getString(11));
				dsr.setCityIds(cursor.getString(12));
				dsr.setCityName(cursor.getString(13));
				dsr.setOrderByEmail(cursor.getString(14));
				dsr.setOrderByPhone(cursor.getString(15));
				dsr.setVisitDocId(cursor.getString(16));
				dsr.setVisitNo(cursor.getString(17));
				dsr.setDsrLock(cursor.getString(18));
				dsrArray.add(dsr);
				cursor.moveToNext();
			}
		} else {

			System.out.println("No records found");
		}
		cursor.close();
		return dsrArray;
	}
	
	
	public ArrayList<Visit> getDsrList(String date) {
		String query = "select Android_id,visit_date,next_visit_date,"
				+ "remark,city_code,fromtime1,totime1,Withusr_id,nextWithUserId,"
				+ "mode_of_transport,vehicle_used,isUpload,cityIds,cityNames," +
				"orderbyemail,orderbyphone,Visit,Attandance,area_code,OtherExp,OtherExpRmk,toArea,chargeable,ncity_code "
				+ "from Visitl1 where visit_date='" + date + "'";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Visit> dsrArray = new ArrayList<Visit>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Visit dsr = new Visit();
				dsr.setAndroidDocId(cursor.getString(0));
				dsr.setVdate(cursor.getString(1));
				dsr.setNextVisitDate(cursor.getString(2));
				dsr.setRemark(cursor.getString(3));
				dsr.setCityId(cursor.getString(4));
				dsr.setfrTime1(cursor.getString(5));
				dsr.setToTime1(cursor.getString(6));
				dsr.setWithUserId(cursor.getString(7));
				dsr.setNextwithUserId(cursor.getString(8));
				dsr.setModeOfTransport(cursor.getString(9));
				dsr.setVehicleUsed(cursor.getString(10));
				dsr.setIsupload(cursor.getString(11));
				dsr.setCityIds(cursor.getString(12));
				dsr.setCityName(cursor.getString(13));
				dsr.setOrderByEmail(cursor.getString(14));
				dsr.setOrderByPhone(cursor.getString(15));
				dsr.setVisitName(cursor.getString(16));
				dsr.setAttandanse(cursor.getString(17));
				dsr.setAreaId(cursor.getString(cursor.getColumnIndex("area_code")));
				dsr.setOtherExp(cursor.getString(19));
				dsr.setOtherExpRem(cursor.getString(20));
				dsr.setToAreaId(cursor.getString(cursor.getColumnIndex("toArea")));
				dsr.setChargeable(cursor.getString(22));
				dsr.setNCityId(cursor.getString(23));

				dsrArray.add(dsr);
				cursor.moveToNext();
			}
		} else {

			System.out.println("No records found");
		}
		cursor.close();
		return dsrArray;
	}

	public ArrayList<Visit> getUploadDsrList() {
		/************************		Write By Sandeep Singh 13-04-2017		******************/
		/*****************		START		******************/
		String query = "SELECT * FROM   visitl1  WHERE  timestamp = 0 " +
				"order by visit_date";

		/*String query = "SELECT * FROM   visitl1  WHERE  isupload = '0' " +
				"AND dsr_lock = '1' order by visit_date";*/
		/*****************		END		******************/
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Visit> dsrArray = new ArrayList<Visit>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Visit dsr = new Visit();
				dsr.setAndroidDocId(cursor.getString(2));
				dsr.setUserId(cursor.getString(3));
				dsr.setVdate(cursor.getString(4));
				dsr.setNextVisitDate(cursor.getString(5));
				dsr.setRemark(cursor.getString(6));
				dsr.setSMId(cursor.getString(7));
				dsr.setCityId(cursor.getString(8));
				dsr.setDistId(cursor.getString(9));

				dsr.setfrTime1(cursor.getString(11));
				dsr.setToTime1(cursor.getString(13));
				dsr.setWithUserId(cursor.getString(15));
				dsr.setNextwithUserId(cursor.getString(16));
				dsr.setModeOfTransport(cursor.getString(17));
				dsr.setVehicleUsed(cursor.getString(18));
				dsr.setCityIds(cursor.getString(22));
				dsr.setCityName(cursor.getString(23));
				dsr.setOrderByEmail(cursor.getString(25));
				dsr.setOrderByPhone(cursor.getString(26));
				dsr.setVisitName((cursor.getString(27)==null?"":cursor.getString(27)));
				dsr.setAttandanse((cursor.getString(28)==null?"0":cursor.getString(28)));
				dsr.setAreaId((cursor.getString(29)==null?"0":cursor.getString(29)));
				dsr.setOtherExp((cursor.getString(30)==null?"0":cursor.getString(30)));
				dsr.setOtherExpRem((cursor.getString(31)==null?"":cursor.getString(31)));
				dsr.setToAreaId((cursor.getString(32)==null?"0":cursor.getString(32)));
				dsr.setChargeable((cursor.getString(33)==null?"":cursor.getString(33)));
				dsr.setNCityId(cursor.getString(10));
				/************************		Write By Sandeep Singh 20-04-2017		******************/
				/*****************		START		******************/
				dsr.setLatitude(Util.validateNumric(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT))));
				dsr.setLongitude(Util.validateNumric(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LNG))));
				dsr.setLatlngTimeStamp(Util.validateNumric(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT_LNG_TIME))));
				/*****************		END		******************/
				dsr.setDsrLock((cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_DSRLOCK))));
				dsrArray.add(dsr);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return dsrArray;
	}

	public ArrayList<String> getDsrNameList(String areaCode, String l2Code,
			String l3Code, String bdpCode) {
		String query = "select name from areamast where webcode='" + areaCode
				+ "' union all " + "select name from sman where webcode='"
				+ l2Code + "' union all "
				+ "select name from sman where webcode='" + l3Code
				+ "' union all " + "select name from sman where webcode='"
				+ bdpCode + "'";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<String> dsrArray = new ArrayList<String>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				dsrArray.add(cursor.getString(0));

				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return dsrArray;
	}

	public ArrayList<String> getDsrCityList(String vdate) {
		ArrayList<String> dsrArray = new ArrayList<String>();
		String query = "select distinct a. Cityid from ( "+
		"select td.city_code as Cityid  from TransVisitDist td left join mastDristributor md on td.distid=md.webcode where td.visit_date='"+vdate+"' "+
		"union all "+
		"select md.city_code as Cityid  from Trans_Failed_visit tf inner join mastDristributor md on tf.distid=md.webcode where tf.visit_date='"+vdate+"' "+
		"union all "+
		"select  md.city_code as Cityid  from DistributerCollection dc left join mastDristributor md on dc.distid=md.webcode where dc.visit_date='"+vdate+"' "+
		"union all "+
		"select mp.city_code as Cityid  from TransOrder om inner join mastParty mp on om.party_code=mp.webcode where om.visit_date='"+vdate+"' "+
		"union all "+
		"select mp.city_code as Cityid  from TransDemo om inner join mastParty mp on om.party_code=mp.webcode where om.visit_date='"+vdate+"' "+
		"union all "+
		"select mp.city_code as Cityid  from Trans_Failed_visit tf inner join mastParty mp on tf.party_code=mp.webcode where tf.visit_date='"+vdate+"' "+
		"union all "+
		"select mp.city_code as Cityid  from TransCompetitor om inner join mastParty mp on om.party_code=mp.webcode where om.visit_date='"+vdate+"' "+
		"union all "+
		"select mp.city_code as Cityid  from TransCollection om inner join mastParty mp on om.party_code=mp.webcode where om.visit_date='"+vdate+"' "+
		") a";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				dsrArray.add(cursor.getString(0));
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return dsrArray;
	}

	public void deleteOrderRow() {
		System.out.println("CDB Data deleted");
		database.delete(DatabaseConnection.TABLE_ORDER, "date='01/05/2014'",
				null);
		System.out.println("dsr deleted");
	}

	public void deleteSyn() {
		System.out.println("Syn Data deleted");
		database.delete(DatabaseConnection.TABLE_SYN, null, null);
		System.out.println("Syn Data deleted");
	}

	public void deleteDsrRow() {
		System.out.println("CDB Data deleted");
		database.delete(DatabaseConnection.TABLE_VISITL1, null, null);
		System.out.println("dsr deleted");
	}

	public String getDsrLockValue(String date) {
		String doc_id = "";
		String query = "select dsr_lock " + " from Visitl1 where visit_date='"
				+ date + "'";
		Cursor cursor = database.rawQuery(query, null);

		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			doc_id = cursor.getString(0);
			System.out.println("max id found  " + doc_id);

		} else {
			System.out.println("no max id found");
		}
		System.out.println("no max id found" + doc_id);
		cursor.close();
		return doc_id;
	}

	public String getDsrUploadValue(String date) {
		String doc_id = "0";
		/*String query = "select isUpload " + " from Visitl1 where visit_date='"
				+ date + "'";
		Cursor cursor = database.rawQuery(query, null);

		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			doc_id = cursor.getString(0);
			System.out.println("max id found  " + doc_id);

		} else {
			System.out.println("no max id found");
		}
		System.out.println("no max id found" + doc_id);
		cursor.close();*/
		return doc_id;
	}

	public String checkDsrLockValue(String date) {
		String doc_id = "";
		String query = "select dsr_lock " + " from Visitl1 where visit_date='"
				+ date + "'";
		Cursor cursor = database.rawQuery(query, null);

		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			doc_id = cursor.getString(0);
			System.out.println("max id found  " + doc_id);

		} else {
			System.out.println("no max id found");
		}
		System.out.println("no max id found" + doc_id);
		cursor.close();
		return doc_id;
	}

	public String getDocId(String date) {
		String doc_id = "0";
		/*String query = "select ifnull(Android_id,0) as androidID,ifnull(doc_id,0) as docID from Visitl1 where visit_date='"+ date + "'";*/
		String query = "select ifnull(Android_id,0),ifnull(doc_id,0) from Visitl1 where visit_date='"+ date + "'";
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			doc_id = cursor.getString(0);
			/*and_id = cursor.getString(0);
			doc_id = cursor.getString(0);*/
			System.out.println("max id found  " + doc_id);
		} else {
			System.out.println("no max id found");
		}
		System.out.println("no max id found" + doc_id);
		cursor.close();
		return doc_id;
	}
	public String getDateVarify (String date) {
		String doc_id;
		String query = "select *from Visitl1 where visit_date='"+ date + "'";
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			doc_id = "exist";
			System.out.println("max id found  " + doc_id);

		} else {
			doc_id = "not exist";
			System.out.println("no max id found");
		}
		System.out.println("no max id found" + doc_id);
		cursor.close();
		return doc_id;
	}
	
	public String getDocIdwithwebDocId(String date) {
		 String doc_id ="0";
		String query = "select ifnull(Android_id,0) from Visitl1 where visit_date='"+ date + "'";
		Cursor cursor = database.rawQuery(query, null);

		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			doc_id=cursor.getString(0);
			System.out.println("max id found  " + doc_id);

		} 
		else {
			doc_id ="0";
			System.out.println("no max id found");
		}
//		System.out.println("no max id found" + doc_id);
		cursor.close();
		return doc_id;
	}

	// public long getMaxDocId() {
	// long doc_id = 0;
	// String query =
	// "select ifnull(max(cast(Android_id as integer)),0)+1 as MyColumn from visitl1";
	// Cursor cursor = database.rawQuery(query, null);
	//
	// if (cursor.getCount() == 1) {
	// cursor.moveToLast();
	// doc_id = cursor.getLong(0);
	// System.out.println("max id found " + doc_id);
	//
	// } else {
	// System.out.println("no max id found");
	// }
	// System.out.println("no max id found" + doc_id);
	// cursor.close();
	// return doc_id;
	// }

	public void insertSyn(String isImported) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ISDATAIMPORTED, isImported);
		values.put(DatabaseConnection.COLUMN_UPLOAD, System.currentTimeMillis());
		System.out.println("timestapt=" + System.currentTimeMillis());
		try {
			long id = database.insert(DatabaseConnection.TABLE_SYN, null,
					values);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}

	public String getIsDataImported() {
		String flag = "false";
		String query = "select isDataImported from Syn ";
		Cursor cursor = database.rawQuery(query, null);

		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			flag = cursor.getString(0);
			System.out.println("max id found " + flag);

		} else {
			System.out.println("no max id found");
		}
		System.out.println("no max id found" + flag);
		cursor.close();
		return flag;
	}

	public String getTimestamp() {
		String flag = "0";
		String query = "select ifnull(isUpload,'0') from Syn";
		Cursor cursor = database.rawQuery(query, null);

		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			flag = cursor.getString(0);
			System.out.println("max id found " + flag);

		} else {
			System.out.println("no max id found");
		}
		System.out.println("no max id found" + flag);
		cursor.close();
		return flag;
	}

	public long updateVisitl1(String androiddoc_id, String doc_id, int vno,String timeStamp) {
		long id=0;
		ContentValues values = new ContentValues();
//		values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
		values.put(DatabaseConnection.COLUMN_DOC_ID, doc_id);
		values.put(DatabaseConnection.COLUMN_VISIT_NO, vno);

		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);

		/*****************		END		******************/
		try {
			id = database.update(DatabaseConnection.TABLE_VISITL1, values,
					"Android_id='" + androiddoc_id + "'", null);
			System.out.println("row=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
		return id;
	}
	public void updateUploadVisitl1(String androiddoc_id) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
//		values.put(DatabaseConnection.COLUMN_DOC_ID, doc_id);
//		values.put(DatabaseConnection.COLUMN_VISIT_NO, vno);
		try {
			long id = database.update(DatabaseConnection.TABLE_VISITL1, values,
					"Android_id='" + androiddoc_id + "'", null);
			System.out.println("row=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}

	public void updateVisitl1test(String doc_id) {
		String date = "12/06/2014";
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
		try {
			long id = database.update(DatabaseConnection.TABLE_VISITL1, values,
					"date='" + date + "'", null);
			System.out.println("row=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}

	public String getCityIdsForSession(String date) {
		 String cityIds="";
		String query = "select ifnull(cityIds,0) from Visitl1 where visit_date='"
				+ date + "'";

		System.out.println("Other City Query"+query);
		Cursor cursor = database.rawQuery(query, null);

		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			cityIds=cursor.getString(0);
			System.out.println("max id found  " + cityIds);

		} 
		else {
			System.out.println("no max id found");
		}
//		System.out.println("no max id found" + doc_id);
		cursor.close();
		return cityIds;
	}
	public ArrayList<Visit> getDsrDetails(String date) {
		String query = "select remark,cityNames,orderbyemail,orderbyphone from Visitl1 where visit_date='" + date + "'";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Visit> dsrArray = new ArrayList<Visit>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Visit dsr = new Visit();
				dsr.setRemark(cursor.getString(0));
				dsr.setCityName(cursor.getString(1));
				dsr.setOrderByEmail(cursor.getString(2));
				dsr.setOrderByPhone(cursor.getString(3));
				dsrArray.add(dsr);
				cursor.moveToNext();
			}
		} else {

			System.out.println("No records found");
		}
		cursor.close();
		return dsrArray;
	}



	public boolean ischeckedVisitNo(String checkedVisitNo)
	{
		Cursor cursor = database.query(DatabaseConnection.TABLE_VISITL1,null, DatabaseConnection.COLUMN_ANDROID_DOCID+"=?",new String[]{checkedVisitNo},null,null,null,null);
		if(cursor.getCount()>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}



	public boolean isDsrLockForVisitDate(String vDate)
	{
		String dsrLockValue = "";
		String dsrLockQry = "select dsr_lock from Visitl1 where visit_date='"+vDate+"'";
		Cursor dsrLockCursor  = database.rawQuery(dsrLockQry,null);
		if(dsrLockCursor.getCount()>0) {
			if (dsrLockCursor.moveToLast()) {
				dsrLockValue = dsrLockCursor.getString(0);
			}
			if(dsrLockValue.equals("1"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
}
