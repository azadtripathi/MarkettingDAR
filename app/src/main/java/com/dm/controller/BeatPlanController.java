package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.library.DateFunction;
import com.dm.model.BeatPlan;

import java.util.ArrayList;


public class BeatPlanController {
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = { 
			DatabaseConnection.COLUMN_ID,
			DatabaseConnection.COLUMN_WEB_DOC_ID,
			DatabaseConnection.COLUMN_ANDROID_DOCID,
			DatabaseConnection.COLUMN_USR_ID,
			DatabaseConnection.COLUMN_SREP_CODE,
			DatabaseConnection.COLUMN_AREA_CODE,
			DatabaseConnection.COLUMN_FROM_DATE,
			DatabaseConnection.COLUMN_CITY_CODE,
			DatabaseConnection.COLUMN_BEAT_CODE,
			DatabaseConnection.COLUMN_APPROVE_STATUS,
			DatabaseConnection.COLUMN_APP_REMARKS,
			DatabaseConnection.COLUMN_APPROVE_BY,
			DatabaseConnection.COLUMN_APPROVE_BY_SMID,
			
			

	};

	public BeatPlanController(Context context) {
		dbHelper = new DatabaseConnection(context);
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

	public void insertBeatPlan(
	        String Bplnd,
			String Dcid,
			String Uid,
			String Aid,
			String AppSt,
			String AppBy,
			String Apprmk,
			String StDt,
			String Ctid,
			String Bid,
			String SMId,
			String AppBySMId,
			String MS,
			String Andid,
			String Pdt

	) {
		int c=0;
		String qry="select count(*) from TransBeatPlan where _id="+Bplnd;
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
			values.put(DatabaseConnection.COLUMN_ID, Bplnd);
		}

		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, Dcid);
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,
				Andid);
		values.put(DatabaseConnection.COLUMN_PLANNED_DATE,Pdt);
		values.put(DatabaseConnection.COLUMN_USR_ID, Uid);
		values.put(DatabaseConnection.COLUMN_AREA_CODE, Aid);
		values.put(DatabaseConnection.COLUMN_SREP_CODE, SMId);
		values.put(DatabaseConnection.COLUMN_FROM_DATE, StDt);
		values.put(DatabaseConnection.COLUMN_CITY_CODE, Ctid);
		values.put(DatabaseConnection.COLUMN_BEAT_CODE, Bid);
		values.put(DatabaseConnection.COLUMN_APPROVE_BY, AppBy);
		values.put(DatabaseConnection.COLUMN_APPROVE_BY_SMID, AppBySMId);
		values.put(DatabaseConnection.COLUMN_APPROVE_REMARK, Apprmk);
		values.put(DatabaseConnection.COLUMN_APPROVE_STATUS, AppSt);
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, MS);
		long id=0;
		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_TRANSBEATPLAN,
							values, "_id='" +Bplnd + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}
			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_TRANSBEATPLAN, null, values);
					System.out.println("row=" + id);
				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
				}
			}

		} catch (SQLiteConstraintException constraintException) {
			System.err.println("Duplicate Party");
			System.out.println("constraintException="
					+ constraintException.getMessage());


		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
		values=null;cursor=null;
	}

	public void insertBeatPlan(BeatPlan beatPlan) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ID, beatPlan.getBeatPlanId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, beatPlan.getDocId());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,
				beatPlan.getAndroid_id());
		values.put(DatabaseConnection.COLUMN_PLANNED_DATE, beatPlan.getPlannedDate());
		values.put(DatabaseConnection.COLUMN_USR_ID, beatPlan.getUserId());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, beatPlan.getAreaId());
		values.put(DatabaseConnection.COLUMN_SREP_CODE, beatPlan.getSmId());
		values.put(DatabaseConnection.COLUMN_FROM_DATE, beatPlan.getStartDate());
		values.put(DatabaseConnection.COLUMN_CITY_CODE, beatPlan.getCityId());
		values.put(DatabaseConnection.COLUMN_BEAT_CODE, beatPlan.getBeatId());
		values.put(DatabaseConnection.COLUMN_APPROVE_BY, beatPlan.getAppBy());
		values.put(DatabaseConnection.COLUMN_APPROVE_BY_SMID, beatPlan.getAppBysmId());
		values.put(DatabaseConnection.COLUMN_APPROVE_REMARK, beatPlan.getAppRemark());
		values.put(DatabaseConnection.COLUMN_APPROVE_STATUS, beatPlan.getAppStatus());
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, beatPlan.getCreatedDate());
		
		if(beatPlan.getBeatPlanImport())
		{
			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, beatPlan.getCreatedDate());
		}
		else
		{
			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction.ConvertDateToTimestamp(beatPlan.getStartDate()+" 00:00:00", "dd/MMM/yyyy 00:00:00"));
		}
		
		try {
			// long id = database.insert(DatabaseConnection.TABLE_VISITDIST,
			// null, values);
			long id = database.insertOrThrow(
					DatabaseConnection.TABLE_TRANSBEATPLAN, null, values);
			System.out.println("id=" + id);

		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}
	
	public void updateBeatPlan(BeatPlan beatPlan) {
		
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_AREA_CODE, beatPlan.getAreaId());
		values.put(DatabaseConnection.COLUMN_BEAT_CODE, beatPlan.getBeatId());
		values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
		try {
			long id = database.update(DatabaseConnection.TABLE_TRANSBEATPLAN,
					values, DatabaseConnection.COLUMN_ANDROID_DOCID + "='"
							+ beatPlan.getAndroid_id() + "' and "+ DatabaseConnection.COLUMN_PLANNED_DATE +"='"+ beatPlan.getPlannedDate()+"'and "+ DatabaseConnection.COLUMN_FROM_DATE +"='"+ beatPlan.getStartDate()+"'", null);
		System.out.println(id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
		
		
	}
	
	public String getSelectedDateBeatPlan(String date)
	{
	String query="select ifnull(b.name,'') from TransBeatPlan tb" +
                  " left join MastBeat b on b.webcode=tb.beat_code where PlannedDate='"+date+"'";
	System.out.println("bREAT pLAN"+query);
	Cursor cursor = database.rawQuery(query, null);
	String beatName="";
	if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				beatName=cursor.getString(0);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
	return beatName;
	}
	public int getBeatPlanDocId(String date)	
	{	
		int countBeatPlan=0;
		String query = "select count(*)from TransBeatPlan where fromDate='"
				+ date + "'";

		Cursor cursor = database.rawQuery(query, null);

		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			countBeatPlan=cursor.getInt(0);
			System.out.println("max id found  " + countBeatPlan);
		} 
		else {
				System.out.println("no max id found");
		}
//		System.out.println("no max id found" + doc_id);
		cursor.close();
		return countBeatPlan;
		
	}
	
	public ArrayList<BeatPlan> getBeatPlanListByDate(String date)
	{
		String query="select * from TransBeatPlan where fromDate='"+date+"'";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<BeatPlan> beatPlanArray = new ArrayList<BeatPlan>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				BeatPlan beatPlan = new BeatPlan();
				beatPlan.setBeatPlanId(cursor.getString(0));
				beatPlan.setDocId(cursor.getString(1));
				beatPlan.setAndroid_id(cursor.getString(2));
				beatPlan.setUserId(cursor.getString(3));
				beatPlan.setPlannedDate(cursor.getString(4));
				beatPlan.setAreaId(cursor.getString(5));
				beatPlan.setAppStatus(cursor.getString(6));
				beatPlan.setAppBy(cursor.getString(7));
				beatPlan.setAppRemark(cursor.getString(8));
				beatPlan.setStartDate(cursor.getString(9));
				beatPlan.setCityId(cursor.getString(10));
				beatPlan.setBeatId(cursor.getString(11));
				beatPlan.setSmId(cursor.getString(12));
				beatPlan.setAppBysmId(cursor.getString(13));
				beatPlan.setUpload(cursor.getString(14));
				beatPlan.setCreatedDate(cursor.getString(15));
				beatPlanArray.add(beatPlan);
				cursor.moveToNext();
			}
		} else {

			System.out.println("No records found");
		}
		cursor.close();
		return beatPlanArray;
		
	}
	
	public ArrayList<BeatPlan> getUploadBeatPlanList()
	{
		String query="select * from TransBeatPlan where isUpload='0'";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<BeatPlan> beatPlanArray = new ArrayList<BeatPlan>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				BeatPlan beatPlan = new BeatPlan();
				beatPlan.setBeatPlanId(cursor.getString(0));
				beatPlan.setDocId(cursor.getString(1));
				beatPlan.setAndroid_id(cursor.getString(2));
				beatPlan.setUserId(cursor.getString(3));
				beatPlan.setPlannedDate(cursor.getString(4));
				beatPlan.setAreaId(cursor.getString(5));
				beatPlan.setAppStatus(cursor.getString(6));
				beatPlan.setAppBy(cursor.getString(7));
				beatPlan.setAppRemark(cursor.getString(8));
				beatPlan.setStartDate(cursor.getString(9));
				beatPlan.setCityId(cursor.getString(10));
				beatPlan.setBeatId(cursor.getString(11));
				beatPlan.setSmId(cursor.getString(12));
				beatPlan.setAppBysmId(cursor.getString(13));
				beatPlan.setUpload(cursor.getString(14));
				beatPlanArray.add(beatPlan);
				cursor.moveToNext();
			}
		} else {

			System.out.println("No records found");
		}
		cursor.close();
		return beatPlanArray;
		
	}
	public ArrayList<BeatPlan> getFindBeatPlanList()
	{
		String query="select distinct web_doc_id,Android_id,appStatus,fromDate,srep_code from TransBeatPlan order by CreatedDate desc";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<BeatPlan> beatPlanArray = new ArrayList<BeatPlan>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				BeatPlan beatPlan = new BeatPlan();
				beatPlan.setDocId(cursor.getString(0));
				beatPlan.setAndroid_id(cursor.getString(1));
				beatPlan.setAppStatus(cursor.getString(2));
				beatPlan.setStartDate(cursor.getString(3));
				beatPlan.setSmId(cursor.getString(4));
				beatPlanArray.add(beatPlan);
				cursor.moveToNext();
			}
		} else {

			System.out.println("No records found");
		}
		cursor.close();
		return beatPlanArray;
		
	}	

	
	public void deleteBeatPlan(String androidId) {

		database.delete(DatabaseConnection.TABLE_TRANSBEATPLAN, "Android_id='"
				+ androidId + "'", null);
		System.out.println("beatPlan no. doc_id=" + androidId + " is deleted");

	}
	
	public void updateBeatPlanUpload(String androiddocId, String webdocId,
			int cno) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, webdocId);
		values.put(DatabaseConnection.COLUMN_ID, cno);

		try {
			long id = database.update(DatabaseConnection.TABLE_TRANSBEATPLAN,
					values, "Android_id='" + androiddocId + "'", null);
			System.out.println("row=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}
	
	
	
}
