package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.library.DateFunction;
import com.dm.model.TransLeaveRequest;

import java.util.ArrayList;

public class TransLeaveController {
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = { DatabaseConnection.COLUMN_ID,
			DatabaseConnection.COLUMN_WEB_DOC_ID,
			DatabaseConnection.COLUMN_ANDROID_DOCID,
			DatabaseConnection.COLUMN_USR_ID,
			DatabaseConnection.COLUMN_VISIT_DATE,
			DatabaseConnection.COLUMN_SREP_CODE,
			DatabaseConnection.COLUMN_NOOFDAYS,
			DatabaseConnection.COLUMN_FROM_DATE,
			DatabaseConnection.COLUMN_TO_DATE,
			DatabaseConnection.COLUMN_REASON,
			DatabaseConnection.COLUMN_APPROVE_STATUS,
			DatabaseConnection.COLUMN_APP_REMARKS,
			DatabaseConnection.COLUMN_APPROVE_BY,
			DatabaseConnection.COLUMN_APPROVE_BY_SMID,
			DatabaseConnection.COLUMN_LEAVE_FLAG,

	};

	public TransLeaveController(Context context) {
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

	public void insertLeave(TransLeaveRequest transLeaveRequest) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ID, transLeaveRequest.getId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID,
				transLeaveRequest.getLeaveDocId());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,
				transLeaveRequest.getAndroid_id());
		values.put(DatabaseConnection.COLUMN_USR_ID,
				transLeaveRequest.getUserId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE,
				transLeaveRequest.getVdate());
		values.put(DatabaseConnection.COLUMN_SREP_CODE,
				transLeaveRequest.getSmid());
		values.put(DatabaseConnection.COLUMN_FROM_DATE,
				transLeaveRequest.getFromDate());
		values.put(DatabaseConnection.COLUMN_TO_DATE,
				transLeaveRequest.getToDate());
		values.put(DatabaseConnection.COLUMN_NOOFDAYS,
				transLeaveRequest.getNoOfDay());
		values.put(DatabaseConnection.COLUMN_REASON,
				transLeaveRequest.getReason());
		values.put(DatabaseConnection.COLUMN_APPROVE_BY,
				transLeaveRequest.getAppBy());
		values.put(DatabaseConnection.COLUMN_APPROVE_BY_SMID,
				transLeaveRequest.getAppBySmid());
		values.put(DatabaseConnection.COLUMN_APPROVE_REMARK,
				transLeaveRequest.getAppRemark());
		values.put(DatabaseConnection.COLUMN_APPROVE_STATUS,
				transLeaveRequest.getAppStatus());
		values.put(DatabaseConnection.COLUMN_LEAVE_FLAG,
				transLeaveRequest.getLeaveFlag());

		if (transLeaveRequest.getNewTransLeave()) {
			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP,
					transLeaveRequest.getCreatedDate());
		} else {
			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction.ConvertDateToTimestamp(transLeaveRequest.getVdate()+" 00:00:00", "dd/MMM/yyyy 00:00:00"));
		}

		try {
			// long id = database.insert(DatabaseConnection.TABLE_VISITDIST,
			// null, values);
			long id = database.insertOrThrow(
					DatabaseConnection.TABLE_TRANSLEAVE, null, values);
			System.out.println("id=" + id);

		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}

	public ArrayList<TransLeaveRequest> getUploadLeaveList() {

		String query = "select * from TransLeaveRequest where isUpload='0'";
		ArrayList<TransLeaveRequest> transLeaveRequestList = new ArrayList<TransLeaveRequest>();

		Cursor cursor = database.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				TransLeaveRequest transLeaveRequest = new TransLeaveRequest();
				transLeaveRequest.setAndroid_id(cursor.getString(2));
				transLeaveRequest.setUserId(cursor.getString(3));
				transLeaveRequest.setVdate(cursor.getString(4));
				transLeaveRequest.setNoOfDay(cursor.getString(5));
				transLeaveRequest.setFromDate(cursor.getString(6));
				transLeaveRequest.setToDate(cursor.getString(7));
				transLeaveRequest.setReason(cursor.getString(8));
				transLeaveRequest.setSmId(cursor.getString(12));
				transLeaveRequest.setLeaveFlag(cursor.getString(14));
				transLeaveRequestList.add(transLeaveRequest);
				cursor.moveToNext();

			}
		} else {

			System.out.println("No records found");
		}
		cursor.close();

		return transLeaveRequestList;

	}

	public void updateLeaveRequestUpload(String androiddocId, String webdocId,
			int cno) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, webdocId);
		values.put(DatabaseConnection.COLUMN_ID, cno);

		try {
			long id = database.update(DatabaseConnection.TABLE_TRANSLEAVE,
					values, "Android_id='" + androiddocId + "'", null);
			System.out.println("row=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}

	public ArrayList<TransLeaveRequest> getFindLeaveRequestList() {
		String query = "select * from TransLeaveRequest order by  visit_date asc";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<TransLeaveRequest> leaveRequestArray = new ArrayList<TransLeaveRequest>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				TransLeaveRequest transLeaveRequest = new TransLeaveRequest();
				transLeaveRequest.setLeaveDocId(cursor.getString(1));
				transLeaveRequest.setAndroid_id(cursor.getString(2));
				transLeaveRequest.setUserId(cursor.getString(3));
				transLeaveRequest.setVdate(cursor.getString(4));
				transLeaveRequest.setNoOfDay(cursor.getString(5));
				transLeaveRequest.setFromDate(cursor.getString(6));
				transLeaveRequest.setToDate(cursor.getString(7));
				transLeaveRequest.setReason(cursor.getString(8));
				transLeaveRequest.setAppStatus(cursor.getString(9));
				transLeaveRequest.setAppRemark(cursor.getString(11));
				transLeaveRequest.setSmId(cursor.getString(12));
				leaveRequestArray.add(transLeaveRequest);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return leaveRequestArray;

	}

	public ArrayList<TransLeaveRequest> getLeaveRequestToUpdate(String smId,
																String fdate, String tdate, String androidId, String docId) {
		String query = "";
		if (androidId != null && docId == null) {
			query = "select * from TransLeaveRequest where srep_code='" + smId
					+ "' and fromDate='" + fdate + "' and toDate='" + tdate
					+ "' and Android_id='" + androidId + "'";
		} else if (docId != null && androidId == null) {
			query = "select * from TransLeaveRequest where srep_code='" + smId
					+ "' and fromDate='" + fdate + "' and toDate='" + tdate
					+ "' and web_doc_id='" + docId + "'";
		} else {
			query = "select * from TransLeaveRequest where srep_code='" + smId
					+ "' and fromDate='" + fdate + "' and toDate='" + tdate
					+ "' and web_doc_id='" + docId + "' and Android_id='"
					+ androidId + "'";
		}
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<TransLeaveRequest> leaveRequestArray = new ArrayList<TransLeaveRequest>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				TransLeaveRequest transLeaveRequest = new TransLeaveRequest();
				transLeaveRequest.setLeaveDocId(cursor.getString(1));
				transLeaveRequest.setAndroid_id(cursor.getString(2));
				transLeaveRequest.setUserId(cursor.getString(3));
				transLeaveRequest.setVdate(cursor.getString(4));
				transLeaveRequest.setNoOfDay(cursor.getString(5));
				transLeaveRequest.setFromDate(cursor.getString(6));
				transLeaveRequest.setToDate(cursor.getString(7));
				transLeaveRequest.setReason(cursor.getString(8));
				transLeaveRequest.setAppStatus(cursor.getString(9));
				transLeaveRequest.setAppRemark(cursor.getString(11));
				transLeaveRequest.setSmId(cursor.getString(12));
				transLeaveRequest.setLeaveFlag(cursor.getString(14));
				transLeaveRequest.setUpload(cursor.getString(15));
				leaveRequestArray.add(transLeaveRequest);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return leaveRequestArray;

	}

	public void updateLeave(TransLeaveRequest transLeaveRequest) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ID, transLeaveRequest.getId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID,
				transLeaveRequest.getLeaveDocId());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,
				transLeaveRequest.getAndroid_id());
		values.put(DatabaseConnection.COLUMN_USR_ID,
				transLeaveRequest.getUserId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE,
				transLeaveRequest.getVdate());
		values.put(DatabaseConnection.COLUMN_SREP_CODE,
				transLeaveRequest.getSmid());
		values.put(DatabaseConnection.COLUMN_FROM_DATE,
				transLeaveRequest.getFromDate());
		values.put(DatabaseConnection.COLUMN_TO_DATE,
				transLeaveRequest.getToDate());
		values.put(DatabaseConnection.COLUMN_NOOFDAYS,
				transLeaveRequest.getNoOfDay());
		values.put(DatabaseConnection.COLUMN_REASON,
				transLeaveRequest.getReason());
		values.put(DatabaseConnection.COLUMN_APPROVE_BY,
				transLeaveRequest.getAppBy());
		values.put(DatabaseConnection.COLUMN_APPROVE_BY_SMID,
				transLeaveRequest.getAppBySmid());
		values.put(DatabaseConnection.COLUMN_APPROVE_REMARK,
				transLeaveRequest.getAppRemark());
		values.put(DatabaseConnection.COLUMN_APPROVE_STATUS,
				transLeaveRequest.getAppStatus());
		values.put(DatabaseConnection.COLUMN_LEAVE_FLAG,
				transLeaveRequest.getLeaveFlag());

		if (transLeaveRequest.getNewTransLeave()) {
			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
		} else {
			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");

		}

		try {
			if (transLeaveRequest.getLeaveDocId() != null) {
				long id = database
						.update(DatabaseConnection.TABLE_TRANSLEAVE, values,
								DatabaseConnection.COLUMN_WEB_CODE + "='"
										+ transLeaveRequest.getLeaveDocId()
										+ "'", null);
				System.out.println("id=" + id);
			} else if (transLeaveRequest.getAndroid_id() != null) {
				long id = database
						.update(DatabaseConnection.TABLE_TRANSLEAVE, values,
								DatabaseConnection.COLUMN_ANDROID_DOCID + "='"
										+ transLeaveRequest.getAndroid_id()
										+ "'", null);
				System.out.println("id=" + id);

			}
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}

	public void deleteLeaveRequest(String androidId) {

		database.delete(DatabaseConnection.TABLE_TRANSLEAVE, "Android_id='"
				+ androidId + "'", null);
		System.out.println("Leave no. doc_id=" + androidId + " is deleted");

	}

	public int checkLeaveExist(String fdate, String tdate, String smid) {
         int countLeave=0;
		String query = "select count(*) from TransLeaveRequest " +
				"where ((fromDate>='"+fdate+"' and toDate<='"+tdate+"') " +
				"or (toDate>='"+fdate+"' and fromDate<='"+tdate+"')) " +
				"and srep_code='"+smid+"' and appStatus<>'Reject'";
		System.out.println(query);
		Cursor cursor=database.rawQuery(query, null);
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 					
				
				countLeave=cursor.getInt(0);
				cursor.moveToNext();
			}
		else{
			System.out.println("No records found");
		}
		cursor.close();
		return countLeave;
		
	}

}
