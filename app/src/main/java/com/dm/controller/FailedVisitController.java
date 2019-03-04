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
import com.dm.model.Dsr;
import com.dm.model.FailedVisit;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class FailedVisitController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	boolean mTranse = false;
	Context mContext;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_DOC_ID,
			DatabaseConnection.COLUMN_VISIT_NO,
			DatabaseConnection.COLUMN_WEB_DOC_ID,
			DatabaseConnection.COLUMN_ANDROID_DOCID,
			DatabaseConnection.COLUMN_VISIT_DATE,
			DatabaseConnection.COLUMN_USR_ID,
			DatabaseConnection.COLUMN_SREP_CODE,
			DatabaseConnection.COLUMN_PARTY_CODE,
			DatabaseConnection.COLUMN_DISTRIBUTER_ID,
			DatabaseConnection.COLUMN_REMARK,
			DatabaseConnection.COLUMN_AREA_CODE,
			DatabaseConnection.COLUMN_NEXT_VISIT_DATE,
			DatabaseConnection.COLUMN_TIME,
			DatabaseConnection.COLUMN_REASONID,
			DatabaseConnection.COLUMN_LAT,
			DatabaseConnection.COLUMN_LNG
			
	};
	
	public FailedVisitController(Context context) {
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
	
	public void insertFailedVisit(FailedVisit failedVisit) {
		mTranse = true;
		database.beginTransaction();
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_DOC_ID, failedVisit.getFVId());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, failedVisit.getVisId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, failedVisit.getFVDocId());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, failedVisit.getAndroidId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, failedVisit.getVDate());
		values.put(DatabaseConnection.COLUMN_USR_ID, failedVisit.getUserID());
		values.put(DatabaseConnection.COLUMN_SREP_CODE, failedVisit.getSMId());
		values.put(DatabaseConnection.COLUMN_PARTY_CODE, failedVisit.getPartyId());
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, failedVisit.getDistId());
		values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE, failedVisit.getAndPartyId());
		values.put(DatabaseConnection.COLUMN_REMARK, failedVisit.getRemarks());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, failedVisit.getAreaId());
		values.put(DatabaseConnection.COLUMN_NEXT_VISIT_DATE, failedVisit.getNextvisit());
		values.put(DatabaseConnection.COLUMN_TIME, failedVisit.getVtime());
		values.put(DatabaseConnection.COLUMN_REASONID, failedVisit.getReasonID());
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(failedVisit.getVDate()));
		values.put(DatabaseConnection.COLUMN_LAT,failedVisit.getLatitude());
		values.put(DatabaseConnection.COLUMN_LNG, failedVisit.getLongitude());
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,failedVisit.getLatlng_timestamp());
		if(failedVisit.isNewfailedvisit())
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

		long id = database.insert(DatabaseConnection.TABLE_TRANSFAILED_VISIT, null, values);
		System.out.println("id"+id);
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


	/*
	Update on 20April/17
	 */
	public void insertFailedVisit(
	        String AreaId,
			String Android_Id,
			String VDate,
			String Createddate,
			String FVDocId,
			String FVId,
			String Partyid,
			String NextVisit,
			String reasonId,
			String Remarks,
			String SMID,
			String UserId,
			String VisId,
			String VisitTime,
			String DistId,
			boolean isNewfailedvisit,
			String latitude,
			String longitude,
			String latlngTimeStamp

	) {
		//		***************update code******************************************************
		ContentValues values = new ContentValues();
//		mTranse = true;
//		database.beginTransaction();
		boolean flag=false,updateflag=false;
		String qry="select timestamp from Trans_Failed_visit where Android_id='"+Android_Id+"'";
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


		if(isDsrLock)
		{
			values.put(DatabaseConnection.COLUMN_DOC_ID, FVId);
			values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, FVDocId);
			values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
			values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
			values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
			values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
			values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
			values.put(DatabaseConnection.COLUMN_AREA_CODE, AreaId);
			values.put(DatabaseConnection.COLUMN_NEXT_VISIT_DATE, NextVisit);
			values.put(DatabaseConnection.COLUMN_TIME, VisitTime);
			values.put(DatabaseConnection.COLUMN_REASONID, reasonId);
			values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, DistId);
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
			values.put(DatabaseConnection.COLUMN_LAT, latitude);
			values.put(DatabaseConnection.COLUMN_LNG, longitude);
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlngTimeStamp);
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, Createddate);
			values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, Android_Id);

			long id = database.update(DatabaseConnection.TABLE_TRANSFAILED_VISIT,
					values, "Android_id='" + Android_Id + "'", null);
			if(id>0)
			{
				//do nothing ...
			}
			else
			{
				id = database.insert(DatabaseConnection.TABLE_TRANSFAILED_VISIT, null, values);
				System.out.println("row=" + id);
			}

		}
		else {
			values.put(DatabaseConnection.COLUMN_DOC_ID, FVId);
			values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, FVDocId);
			values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
			values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
			values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
			values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
			values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
			values.put(DatabaseConnection.COLUMN_AREA_CODE, AreaId);
			values.put(DatabaseConnection.COLUMN_NEXT_VISIT_DATE, NextVisit);
			values.put(DatabaseConnection.COLUMN_TIME, VisitTime);
			values.put(DatabaseConnection.COLUMN_REASONID, reasonId);
			values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, DistId);
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
			values.put(DatabaseConnection.COLUMN_LAT, latitude);
			values.put(DatabaseConnection.COLUMN_LNG, longitude);
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlngTimeStamp);
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, Createddate);
			long id = 0;
			try {
				if (flag) {
					if (!updateflag) {
						id = database.update(DatabaseConnection.TABLE_TRANSFAILED_VISIT,
								values, "Android_id='" + Android_Id + "'", null);
						/*database.setTransactionSuccessful();
						database.endTransaction();
						mTranse = false;*/
						System.out.println("row=" + id);
					} else {
						CompetitorController.isUpdateFlag = true;
						/*database.endTransaction();*/
						mTranse = false;
					}

				} else {

					id = database.insert(DatabaseConnection.TABLE_TRANSFAILED_VISIT, null, values);
					System.out.println("row=" + id);
					/*database.setTransactionSuccessful();
					database.endTransaction();*/
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
		/*
		//		***************update code******************************************************

		mTranse = true;
		database.beginTransaction();
		int c=0;
		String qry="select count(*) from Trans_Failed_visit where Android_id='"+Android_Id+"'";
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
		values.put(DatabaseConnection.COLUMN_DOC_ID, FVId);
		values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID,FVDocId);
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
		values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
		values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
		values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
		values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
		values.put(DatabaseConnection.COLUMN_AREA_CODE, AreaId);
		values.put(DatabaseConnection.COLUMN_NEXT_VISIT_DATE, NextVisit);
		values.put(DatabaseConnection.COLUMN_TIME, VisitTime);
		values.put(DatabaseConnection.COLUMN_REASONID, reasonId);
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID,DistId);
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
		values.put(DatabaseConnection.COLUMN_LAT, latitude);
		values.put(DatabaseConnection.COLUMN_LNG,longitude);
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,latlngTimeStamp);
		if(isNewfailedvisit)
		{
//			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, Createddate);
		}
		else
		{
			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction.ConvertDateToTimestamp(VDate+" 00:00:00", "dd/MMM/yyyy 00:00:00"));

		}
//		try{
//			long id = database.insert(DatabaseConnection.TABLE_TRANSFAILED_VISIT, null, values);
//			System.out.println("id"+id);
//		}
//		catch(RuntimeException e){
//			System.out.println("+++++++++++++++++++"+e.toString());
//		}


		long id=0;
		try{
			if(c>0)
			{
				try {

					id = database.update(DatabaseConnection.TABLE_TRANSFAILED_VISIT,
							values, "Android_id='" +Android_Id + "'", null);
					System.out.println("row=" + id);
					database.setTransactionSuccessful();
					database.endTransaction();
					mTranse = false;
				} catch (RuntimeException e) {
					if(mTranse)
					{
						mTranse = false;
						database.endTransaction();
					}
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {

					id = database.insert(DatabaseConnection.TABLE_TRANSFAILED_VISIT, null, values);
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

		values=null;cursor=null;
*/

	}

	
	public void updateFailedVisit(FailedVisit failedVisit)
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_DOC_ID, failedVisit.getFVId());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, failedVisit.getVisId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, failedVisit.getFVDocId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, failedVisit.getVDate());
		values.put(DatabaseConnection.COLUMN_USR_ID, failedVisit.getUserID());
		values.put(DatabaseConnection.COLUMN_SREP_CODE, failedVisit.getSMId());
		values.put(DatabaseConnection.COLUMN_PARTY_CODE, failedVisit.getPartyId());
		values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE, failedVisit.getAndPartyId());
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, failedVisit.getDistId());
		values.put(DatabaseConnection.COLUMN_REMARK, failedVisit.getRemarks());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, failedVisit.getAreaId());
		values.put(DatabaseConnection.COLUMN_NEXT_VISIT_DATE, failedVisit.getNextvisit());
		values.put(DatabaseConnection.COLUMN_TIME, failedVisit.getVtime());
		values.put(DatabaseConnection.COLUMN_REASONID, failedVisit.getReasonID());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");
		values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(failedVisit.getVDate()));
		
		try{
			database.beginTransaction();
			long id = database.update(DatabaseConnection.TABLE_TRANSFAILED_VISIT, values, DatabaseConnection.COLUMN_ANDROID_DOCID+"='"+failedVisit.getAndroidId()+"'", null);
		System.out.println(id);
			database.setTransactionSuccessful();
			database.endTransaction();
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
	
	
	public void getUpdateDsr(Dsr dsr){
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
		String query="update visitl1 set "
				+"doc_id='"+dsr.getDoc_id()+"',"
				+"date='"+dsr.getDate()+"',"
				+"srep_code='"+dsr.getSrep_code()+"',"
				+"area_code='"+dsr.getArea_code()+"',"
				+"remark='"+dsr.getRemark()+"',"
				+"srep_codel2='"+dsr.getSrep_codel2()+"',"
				+"srep_codel3='"+dsr.getSrep_codel3()+"',"
				+"srep_codebdp='"+dsr.getSrep_codebdp()+"',"
				+"mode_of_transport='"+dsr.getMode_of_transport()+"',"
				+"vehicle_used='"+dsr.getVehicle()+"',"
				+"time='"+dsr.getTime()+"'"
				+" where doc_id="+"'"+dsr.getDoc_id()+"'";
		System.out.println(query);
		try{
			//String qurey="UPDATE order_sub SET remark="+""+" WHERE ExecutionDate = '2011-10-12 00:00:00.000'";
					
					
		database.execSQL(query);
		System.out.println(" Dsr updated");
		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		
	}
	public void deleteFailedvisitTransactionRow(){
		System.out.println("CDB Data deleted");
		database.delete(DatabaseConnection.TABLE_TRANSFAILED_VISIT,null,null );
		System.out.println("orderTransaction deleted");
	}
//	public ArrayList<FailedVisit> getFailedVisitUploadList(){
//		String query="select fv.doc_id,fv.date,fv.srep_code,fv.area_code,next_visit_date,fv.remark,p.webcode from failed_visit fv "
//					+" left join visitl1 vl1 on fv.date=vl1.date " 
//					+" left join partymast p on fv.party_code=p.code "
//					+" where vl1.dsr_lock='1'  and fv.isUpload='0'";
//		Cursor cursor=database.rawQuery(query, null);
//		ArrayList<FailedVisit> failedVisits = new ArrayList<FailedVisit>();
//		if(cursor.moveToFirst()){
//			while(!(cursor.isAfterLast()))
//			{				
//				FailedVisit failedVisit=new FailedVisit();			
//				failedVisit.setDoc_id(cursor.getString(0));
//				failedVisit.setDate(cursor.getString(1));
//				failedVisit.setSrep_code(cursor.getString(2));
//				failedVisit.setArea_code(cursor.getString(3));
//				failedVisit.setNext_visit_date(cursor.getString(4));
//				failedVisit.setRemark(cursor.getString(5));
//				failedVisit.setParty_code(cursor.getString(6));
//				failedVisits.add(failedVisit);
//				cursor.moveToNext();
//			}
//		}else{
//			System.out.println("No records found");
//		}
//		cursor.close();
//		return failedVisits;
//	}
	public void deleteDsrRow(){
		System.out.println("CDB Data deleted");
		database.delete(DatabaseConnection.TABLE_VISITL1,null,null );
		System.out.println("dsr deleted");
	}
	public long getDocId(String date){
		long doc_id = 0;
		String query = "select ifnull(doc_id,0) from Failed_visit where date='"+date+"'";
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
	public long getDocId(String date,String partyCode){
		long doc_id = 0;
		String query = "select ifnull(doc_id,0) from Failed_visit where date='"+date+"' and  party_code='"+partyCode+"'";
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
	public long getMaxDocId(){
		long doc_id = 0;
		String query = "select ifnull(max(cast(doc_id as integer)),0)+1 as MyColumn from Failed_visit";
		Cursor cursor = database.rawQuery(query,null);
		
		 if (cursor.getCount()==1)
		    {
		            cursor.moveToLast();   
		            doc_id=cursor.getLong(0);
		            System.out.println("max id found "+doc_id);
		         
		    }
		 else{
			System.out.println("no max id found");
		}
		 System.out.println("no max id found"+doc_id);
		 cursor.close();
		return doc_id;
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
		 cursor.close();
		return doc_id;
	}
	public  void updateFailed(String doc_id){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_UPLOAD,"1");
		try{
		long id = database.update(DatabaseConnection.TABLE_TRANSFAILED_VISIT, values, "doc_id='"+doc_id+"'", null);
		System.out.println("row="+id);
		} 
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	public String getDistributorAndroidDocId(String distId,String vdate){
		String Adoc_id = "0";
		String query = "select ifnull(Android_id,0) as MyColumn from Trans_Failed_visit where DistId='"+distId+"' and visit_date='"+vdate+"'";
		Cursor cursor = database.rawQuery(query,null);
		
		 if (cursor.getCount()>=1)
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
	public String getAndroidDocId(String partyId,String vdate){
		String Adoc_id = "0";
		String query = "select ifnull(Android_id,0) as MyColumn from Trans_Failed_visit where party_code='"+partyId+"' and visit_date='"+vdate+"'";
		Cursor cursor = database.rawQuery(query,null);
		
		 if (cursor.getCount()>=1)
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
	
	public String getAndroidDocId(String partyId,String vdate,String apartyId){
		String Adoc_id = "0";
		String query="";
		if(partyId!=null && apartyId!=null)
		{
			query = "select ifnull(Android_id,0) as MyColumn from Trans_Failed_visit where party_code='"+partyId+"' and visit_date='"+vdate+"'";
		}
		else if(partyId!=null && apartyId==null)
		{
			query = "select ifnull(Android_id,0) as MyColumn from Trans_Failed_visit where party_code='"+partyId+"' and visit_date='"+vdate+"'";
		}
		else if(partyId==null && apartyId!=null)
		{
			query = "select ifnull(Android_id,0) as MyColumn from Trans_Failed_visit where and_party_code='"+apartyId+"' and visit_date='"+vdate+"'";
		}
		 
		Cursor cursor = database.rawQuery(query,null);
		
		 if (cursor.getCount()>=1)
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
	
	public String getAndroidDocIdDistributer(String distId,String vdate){
		String Adoc_id = "0";
		String query = "select ifnull(Android_id,0) as MyColumn from Trans_Failed_visit where DistId='"+distId+"' and visit_date='"+vdate+"'";
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
	public FailedVisit getFailedVisitByVisitDate(String vdate, String distId, String partyId, String apartyId)
	{
		String query="";
		FailedVisit failedVisit=null;
		if(vdate!=null && distId!=null && partyId==null && apartyId==null)
		{
			query="select * from Trans_Failed_visit tf left join visitl1 tv on tf.visit_date=tv.visit_date  where tf.visit_date='"+vdate+"' and tf.DistId='"+distId+"' and tv.dsr_lock='0'";
			
		}
		if(vdate!=null && distId!=null && partyId==null && apartyId!=null)
		{
			query="select * from Trans_Failed_visit tf left join visitl1 tv on tf.visit_date=tv.visit_date  where tf.visit_date='"+vdate+"' and tf.DistId='"+distId+"' and tv.dsr_lock='0'";
			
		}
		
		else if(vdate!=null && distId==null && partyId!=null && apartyId!=null)
		{
			query="select * from Trans_Failed_visit tf left join visitl1 tv on tf.visit_date=tv.visit_date  where tf.visit_date='"+vdate+"' and tf.party_code='"+partyId+"' and tv.dsr_lock='0'";
		}
		else if(vdate!=null && distId==null && partyId!=null && apartyId==null)
		{
			query="select * from Trans_Failed_visit tf left join visitl1 tv on tf.visit_date=tv.visit_date  where tf.visit_date='"+vdate+"' and tf.party_code='"+partyId+"' and tv.dsr_lock='0'";
			
		}
		else if(vdate!=null && distId==null && partyId==null && apartyId!=null)
		{
			query="select * from Trans_Failed_visit tf left join visitl1 tv on tf.visit_date=tv.visit_date  where tf.visit_date='"+vdate+"' and tf.and_party_code='"+apartyId+"' and tv.dsr_lock='0'";
		}
		
		//-------------------------------------------
//		if(vdate!=null && distId!=null)
//			query="select * from Trans_Failed_visit where visit_date='"+vdate+"' and DistId='"+distId+"' and isUpload='0'";	
//		else if(vdate!=null && partyId!=null)
//			query="select * from Trans_Failed_visit where visit_date='"+vdate+"' and party_code='"+partyId+"' and isUpload='0'";	
		Cursor cursor = database.rawQuery(query,null);	
		if(cursor.moveToFirst())
		{
			while(!(cursor.isAfterLast()))
			{						
	     failedVisit=new FailedVisit();
	    failedVisit.setFVDocId(cursor.getString(0));
	    failedVisit.setVisId(cursor.getString(1));
	    failedVisit.setFVId(cursor.getString(2));
	    failedVisit.setAndroidId(cursor.getString(3));
	    failedVisit.setVDate(cursor.getString(4));
	    failedVisit.setUserID(cursor.getString(5));
	    failedVisit.setSMId(cursor.getString(6));
	    failedVisit.setPartyId(cursor.getString(7));
	    failedVisit.setDistId(cursor.getString(8));
	    failedVisit.setRemarks(cursor.getString(9));
	    failedVisit.setAreaId(cursor.getString(10));
	    failedVisit.setNextvisit(cursor.getString(11));
	    failedVisit.setVtime(cursor.getString(12));
		failedVisit.setReasonID(cursor.getString(13));

	    cursor.moveToNext();
		}
	}else{
		System.out.println("No records found");
	}
		cursor.close();
	    return failedVisit;
		
	}
	
	
//public ArrayList<FailedVisit> getUploadFailedVisitList(){
//String query="select d.Android_id,d.usr_id,d.visit_date,d.srep_code,d.party_code,d.remark,d.area_code,d.next_visit_date,d.time,d.reasonId,vl1.visit_no from Trans_Failed_visit d"+
//                  " LEFT JOIN Visitl1 vl1"+
//              " ON d.visit_date = vl1.visit_date"+
//              " LEFT JOIN mastParty p"+
//              " ON d.party_code = p.webcode"+
//              " WHERE  vl1.dsr_lock = '1' "+
//              " AND d.isUpload = '0'";
//	    System.out.println(query);
//		Cursor cursor=database.rawQuery(query, null);
//		ArrayList<FailedVisit> failedVisits = new ArrayList<FailedVisit>();
//		if(cursor.moveToFirst()){
//			while(!(cursor.isAfterLast()))
//			{						
//				FailedVisit failedVisit=new  FailedVisit();
//				failedVisit.setAndroidId(cursor.getString(0));
//				failedVisit.setUserID(cursor.getString(1));
//				failedVisit.setVDate(cursor.getString(2));
//				failedVisit.setSMId(cursor.getString(3));
//				failedVisit.setPartyId(cursor.getString(4));
//				failedVisit.setRemarks(cursor.getString(5));
//				failedVisit.setAreaId(cursor.getString(6));
//				failedVisit.setNextvisit(cursor.getString(7));
//				failedVisit.setVtime(cursor.getString(8));
//				failedVisit.setReasonID(cursor.getString(9));
//				failedVisit.setVisId(cursor.getString(10));
//				failedVisits.add(failedVisit);
//				cursor.moveToNext();
//			}
//		}else{
//			System.out.println("No records found");
//		}
//		cursor.close();
//		return failedVisits;
//	} 
	
	public ArrayList<FailedVisit> getUploadFailedVisitList(String vdate){
		/************************		Write By Sandeep Singh 13-04-2017		******************/
		/*****************		START		******************/
		String query="select d.Android_id,d.usr_id,d.visit_date,d.srep_code,d.party_code," +
				"d.remark,d.area_code,d.next_visit_date,d.time,d.reasonId," +
				"vl1.visit_no,ifnull(d.latitude,0) as latitude,ifnull(d.longitude,0) as longitude,ifnull(d.LatlngTime,0) as LatlngTime from Trans_Failed_visit d"+
				" LEFT JOIN Visitl1 vl1"+
				" ON d.visit_date = vl1.visit_date"+
				" LEFT JOIN mastParty p"+
				" ON d.party_code = p.webcode"+
				" WHERE  "+
				" d.timestamp = 0 AND d.visit_date = '"+vdate+"' AND d.party_code  IS NOT NULL";
		/*String query="select d.Android_id,d.usr_id,d.visit_date,d.srep_code,d.party_code," +
				"d.remark,d.area_code,d.next_visit_date,d.time,d.reasonId," +
				"vl1.visit_no from Trans_Failed_visit d"+
				" LEFT JOIN Visitl1 vl1"+
				" ON d.visit_date = vl1.visit_date"+
				" LEFT JOIN mastParty p"+
				" ON d.party_code = p.webcode"+
				" WHERE  vl1.dsr_lock = '1' "+
				" AND d.isUpload = '0' AND d.visit_date = '"+vdate+"' AND d.party_code  IS NOT NULL";*/
		/*****************		END		******************/
				Cursor cursor=database.rawQuery(query, null);
				ArrayList<FailedVisit> failedVisits = new ArrayList<FailedVisit>();
				if(cursor.moveToFirst()){
					while(!(cursor.isAfterLast()))
					{						
						FailedVisit failedVisit=new FailedVisit();
						failedVisit.setAndroidId(cursor.getString(0));
						failedVisit.setUserID(cursor.getString(1));
						failedVisit.setVDate(cursor.getString(2));
						failedVisit.setSMId(cursor.getString(3));
						failedVisit.setPartyId(cursor.getString(4));
						failedVisit.setRemarks(cursor.getString(5));
						failedVisit.setAreaId(cursor.getString(6));
						failedVisit.setNextvisit(cursor.getString(7));
						failedVisit.setVtime(cursor.getString(8));
						failedVisit.setReasonID(cursor.getString(9));
						failedVisit.setVisId(cursor.getString(10));
						/************************		Write By Sandeep Singh 20-04-2017		******************/
						/*****************		START		******************/
						failedVisit.setLatitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT)));
						failedVisit.setLongitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LNG)));
						failedVisit.setLatlng_timestamp(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT_LNG_TIME)));
						/*****************		END		******************/
						failedVisits.add(failedVisit);
						cursor.moveToNext();
					}
				}else{
					System.out.println("No records found");
				}
				cursor.close();
				return failedVisits;
			} 
//public ArrayList<FailedVisit> getUploadDistFailedVisitList(){
//String query="select d.Android_id,d.usr_id,d.visit_date,d.srep_code,d.DistId,d.remark,d.area_code,d.next_visit_date,d.time,d.reasonId,vl1.visit_no from Trans_Failed_visit d"+
//                  " LEFT JOIN Visitl1 vl1"+
//              " ON d.visit_date = vl1.visit_date"+
//              " LEFT JOIN mastDristributor p"+
//              " ON d.DistId = p.webcode"+
//              " WHERE  vl1.dsr_lock = '1' "+
//              " AND d.isUpload = '0'";
//	    System.out.println(query);
//		Cursor cursor=database.rawQuery(query, null);
//		ArrayList<FailedVisit> failedVisits = new ArrayList<FailedVisit>();
//		if(cursor.moveToFirst()){
//			while(!(cursor.isAfterLast()))
//			{						
//				FailedVisit failedVisit=new  FailedVisit();
//				failedVisit.setAndroidId(cursor.getString(0));
//				failedVisit.setUserID(cursor.getString(1));
//				failedVisit.setVDate(cursor.getString(2));
//				failedVisit.setSMId(cursor.getString(3));
//				failedVisit.setDistId(cursor.getString(4));
//				failedVisit.setRemarks(cursor.getString(5));
//				failedVisit.setAreaId(cursor.getString(6));
//				failedVisit.setNextvisit(cursor.getString(7));
//				failedVisit.setVtime(cursor.getString(8));
//				failedVisit.setReasonID(cursor.getString(9));
//				failedVisit.setVisId(cursor.getString(10));
//				failedVisits.add(failedVisit);
//				cursor.moveToNext();
//			}
//		}else{
//			System.out.println("No records found");
//		}
//		cursor.close();
//		return failedVisits;
//	} 

	public ArrayList<FailedVisit> getUploadDistFailedVisitList(String vdate){
		/************************		Write By Sandeep Singh 13-04-2017		******************/
		/*****************		START		******************/
		String query="select d.Android_id,d.usr_id,d.visit_date,d.srep_code,d.DistId,d.remark,d.area_code,d.next_visit_date,d.time,d.reasonId,vl1.visit_no, ifnull(d.latitude,0) as latitude,ifnull(d.longitude,0) as longitude,ifnull(d.LatlngTime,0) as LatlngTime from Trans_Failed_visit d"+
				" LEFT JOIN Visitl1 vl1"+
				" ON d.visit_date = vl1.visit_date"+
				" LEFT JOIN mastDristributor p"+
				" ON d.DistId = p.webcode"+
				" WHERE "+
				" d.timestamp = 0 AND d.visit_date = '"+vdate+"' AND d.DistId IS NOT NULL";
	/*	String query="select d.Android_id,d.usr_id,d.visit_date,d.srep_code,d.DistId,d.remark,d.area_code,d.next_visit_date,d.time,d.reasonId,vl1.visit_no from Trans_Failed_visit d"+
				" LEFT JOIN Visitl1 vl1"+
				" ON d.visit_date = vl1.visit_date"+
				" LEFT JOIN mastDristributor p"+
				" ON d.DistId = p.webcode"+
				" WHERE  vl1.dsr_lock = '1' "+
				" AND d.isUpload = '0' AND d.visit_date = '"+vdate+"' AND d.DistId IS NOT NULL";*/
		/*****************		END		******************/
				Cursor cursor=database.rawQuery(query, null);
				ArrayList<FailedVisit> failedVisits = new ArrayList<FailedVisit>();
				if(cursor.moveToFirst()){
					while(!(cursor.isAfterLast()))
					{						
						FailedVisit failedVisit=new FailedVisit();
						failedVisit.setAndroidId(cursor.getString(0));
						failedVisit.setUserID(cursor.getString(1));
						failedVisit.setVDate(cursor.getString(2));
						failedVisit.setSMId(cursor.getString(3));
						failedVisit.setDistId(cursor.getString(4));
						failedVisit.setRemarks(cursor.getString(5));
						failedVisit.setAreaId(cursor.getString(6));
						failedVisit.setNextvisit(cursor.getString(7));
						failedVisit.setVtime(cursor.getString(8));
						failedVisit.setReasonID(cursor.getString(9));
						failedVisit.setVisId(cursor.getString(10));
						/************************		Write By Sandeep Singh 20-04-2017		******************/
						/*****************		START		******************/
						failedVisit.setLatitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT)));
						failedVisit.setLongitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LNG)));
						failedVisit.setLatlng_timestamp(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT_LNG_TIME)));
						/*****************		END		******************/
						failedVisits.add(failedVisit);
						cursor.moveToNext();
					}
				}else{
					System.out.println("No records found");
				}
				cursor.close();
				return failedVisits;
			}

//public  long updateFailedVisitUpload(String androiddocId, String webdocId,int dno,int vId){
	/************************		Write By Sandeep Singh 10-04-2017		******************/
	/*****************		START		******************/
	public  long updateFailedVisitUpload(String androiddocId, String webdocId,int dno,int vId,String timeStamp){
		/*****************		END		******************/
		ContentValues values = new ContentValues();
		long id =0;
		values.put(DatabaseConnection.COLUMN_UPLOAD,"1");
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID,webdocId);
		values.put(DatabaseConnection.COLUMN_DOC_ID,dno);
		values.put(DatabaseConnection.COLUMN_VISIT_NO,vId);

		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
		/*****************		END		******************/
		try{
			id = database.update(DatabaseConnection.TABLE_TRANSFAILED_VISIT, values, "Android_id='"+androiddocId+"'", null);
			System.out.println("row="+id);
		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		return id;
	}

public int getNoOfPartyFailedVisit(String date)
{
	int sum=0;
	ArrayList<Integer> productiveVisit=new ArrayList<Integer>();
	String query1="select count(distinct party_code) as NoOfFailedVisit from Trans_Failed_visit  where visit_date='"+date+"'";
	String query2="select count(distinct and_party_code) as NoOfFailedVisit1 from Trans_Failed_visit  where visit_date='"+date+"' and party_code IS NULL";
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



public int getNoOfDistFailedVisit(String date)
{
	int sum=0;
	String query1="select count(distinct DistId) as NoOfFailedVisit from Trans_Failed_visit  where visit_date='"+date+"'";
	Cursor cursor1=database.rawQuery(query1, null);
if(cursor1.moveToFirst()){
		while(!(cursor1.isAfterLast()))
		{						
			
			sum=cursor1.getInt(0);
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
		Cursor c = database.query(DatabaseConnection.TABLE_TRANSFAILED_VISIT,null,"Android_id='" +android_doc_id + "'",null,null,null,null);
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
