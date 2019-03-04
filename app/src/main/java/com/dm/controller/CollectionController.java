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
import com.dm.model.Collection;

import java.util.ArrayList;

public class CollectionController{
	boolean mTrans = false;
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	Context mContext;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_ID,
			DatabaseConnection.COLUMN_WEB_DOC_ID,
			DatabaseConnection.COLUMN_ANDROID_DOCID,
			DatabaseConnection.COLUMN_USR_ID,
			DatabaseConnection.COLUMN_DISTRIBUTER_ID,
			DatabaseConnection.COLUMN_SREP_CODE,
			DatabaseConnection.COLUMN_MODE,
			DatabaseConnection.COLUMN_AMOUNT,
			DatabaseConnection.COLUMN_PAYMENT_DATE,
			DatabaseConnection.COLUMN_CHEQUE_DDNO,
			DatabaseConnection.COLUMN_CHQ_DATE,
			DatabaseConnection.COLUMN_BANK,
			DatabaseConnection.COLUMN_BRANCH,
			DatabaseConnection.COLUMN_REMARK,
			DatabaseConnection.COLUMN_UPLOAD};
	
	public CollectionController(Context context) {
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

	/*
	 Vinayak started work on 20 April 2017
	 */
	public long insertCollection(Collection collection){
		long id=-1;
		ContentValues values = new ContentValues();
		//database.beginTransaction();
		boolean flag=false,updateflag=false;
		String qry="select timestamp from DistributerCollection where Android_id='"+collection.getAndroid_id()+"'";
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
			values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, collection.getAndroid_id());
		}
		cursor.close();
//************************************************************
		/*

		updated transaction on 21 April 2017 vinayak
		 */
		mTrans = true;

		DsrController dc = new DsrController(mContext);
		dc.open();
		boolean isDsrLock = dc.isDsrLockForVisitDate(collection.getVdate());
		dc.close();

		if(isDsrLock)
		{
			values.put(DatabaseConnection.COLUMN_ID, collection.getCollId());
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, collection.getCollDocId());
			values.put(DatabaseConnection.COLUMN_USR_ID, collection.getUserId());
			values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, collection.getDistId());
			values.put(DatabaseConnection.COLUMN_SREP_CODE, collection.getSMId());
			values.put(DatabaseConnection.COLUMN_MODE, collection.getMode());
			values.put(DatabaseConnection.COLUMN_AMOUNT, collection.getAmount());
			values.put(DatabaseConnection.COLUMN_PAYMENT_DATE, collection.getPaymentDate());
			values.put(DatabaseConnection.COLUMN_CHEQUE_DDNO, collection.getCheque_DDNo());
			values.put(DatabaseConnection.COLUMN_CHQ_DATE, collection.getCheque_DD_Date());
			values.put(DatabaseConnection.COLUMN_BANK, collection.getBank());
			values.put(DatabaseConnection.COLUMN_BRANCH, collection.getBranch());
			values.put(DatabaseConnection.COLUMN_REMARK, collection.getRemarks());
			values.put(DatabaseConnection.COLUMN_VISIT_NO, collection.getVisitId());
			values.put(DatabaseConnection.COLUMN_VISIT_DATE, collection.getVdate());
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(collection.getVdate()));
			values.put(DatabaseConnection.COLUMN_LAT,collection.getLatitude());
			values.put(DatabaseConnection.COLUMN_LNG,collection.getLongitude());
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,collection.getLatlngTime_stamp());
			values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,collection.getAndroid_id());
			if(collection.getColectionImport())
			{
				values.put(DatabaseConnection.COLUMN_TIMESTAMP, collection.getCreatedDate());
			}
			else
			{
				values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
				values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
			}
			id = database.update(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION,
					values, "Android_id='" + collection.getAndroid_id() + "'", null);
			if(id>0)
			{
				// do nothing
			}
			else
			{
				database.insert(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION, null, values);
			}
		}
		else {


			values.put(DatabaseConnection.COLUMN_ID, collection.getCollId());
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, collection.getCollDocId());
			values.put(DatabaseConnection.COLUMN_USR_ID, collection.getUserId());
			values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, collection.getDistId());
			values.put(DatabaseConnection.COLUMN_SREP_CODE, collection.getSMId());
			values.put(DatabaseConnection.COLUMN_MODE, collection.getMode());
			values.put(DatabaseConnection.COLUMN_AMOUNT, collection.getAmount());
			values.put(DatabaseConnection.COLUMN_PAYMENT_DATE, collection.getPaymentDate());
			values.put(DatabaseConnection.COLUMN_CHEQUE_DDNO, collection.getCheque_DDNo());
			values.put(DatabaseConnection.COLUMN_CHQ_DATE, collection.getCheque_DD_Date());
			values.put(DatabaseConnection.COLUMN_BANK, collection.getBank());
			values.put(DatabaseConnection.COLUMN_BRANCH, collection.getBranch());
			values.put(DatabaseConnection.COLUMN_REMARK, collection.getRemarks());
			values.put(DatabaseConnection.COLUMN_VISIT_NO, collection.getVisitId());
			values.put(DatabaseConnection.COLUMN_VISIT_DATE, collection.getVdate());
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(collection.getVdate()));
			values.put(DatabaseConnection.COLUMN_LAT, collection.getLatitude());
			values.put(DatabaseConnection.COLUMN_LNG, collection.getLongitude());
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, collection.getLatlngTime_stamp());
			values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, collection.getAndroid_id());
			if (collection.getColectionImport()) {
				values.put(DatabaseConnection.COLUMN_TIMESTAMP, collection.getCreatedDate());
			} else {
				values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
				values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
			}
			try {
				if (flag) {
					if (!updateflag) {
						id = database.update(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION,
								values, "Android_id='" + collection.getAndroid_id() + "'", null);
//					database.setTransactionSuccessful();
//					database.endTransaction();
//					mTrans = false;
						System.out.println("row=" + id);
					} else {
						CompetitorController.isUpdateFlag = true;
//					database.endTransaction();
//					mTrans = false;
					}

				} else {

					id = database.insert(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION, null, values);
					System.out.println("row=" + id);
//				database.setTransactionSuccessful();
//				database.endTransaction();
					mTrans = false;
				}
			} catch (RuntimeException e) {
				if (mTrans) {
//				database.endTransaction();
					mTrans = false;
				}
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			values = null;
			cursor = null;
		}
		//		***************update code******************************************************
/*		database.beginTransaction();
		int c=0;
		String qry="select count(*) from DistributerCollection where Android_id='"+collection.getAndroid_id()+"'";
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
			values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, collection.getAndroid_id());
		}


/*//************************************************************
		*//*

		updated transaction on 21 April 2017 vinayak
		 *//*
		mTrans = true;
		values.put(DatabaseConnection.COLUMN_ID, collection.getCollId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, collection.getCollDocId());
		values.put(DatabaseConnection.COLUMN_USR_ID, collection.getUserId());
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, collection.getDistId());
		values.put(DatabaseConnection.COLUMN_SREP_CODE, collection.getSMId());
		values.put(DatabaseConnection.COLUMN_MODE, collection.getMode());
		values.put(DatabaseConnection.COLUMN_AMOUNT, collection.getAmount());
		values.put(DatabaseConnection.COLUMN_PAYMENT_DATE, collection.getPaymentDate());
		values.put(DatabaseConnection.COLUMN_CHEQUE_DDNO, collection.getCheque_DDNo());
		values.put(DatabaseConnection.COLUMN_CHQ_DATE, collection.getCheque_DD_Date());
		values.put(DatabaseConnection.COLUMN_BANK, collection.getBank());
		values.put(DatabaseConnection.COLUMN_BRANCH, collection.getBranch());
		values.put(DatabaseConnection.COLUMN_REMARK, collection.getRemarks());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, collection.getVisitId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, collection.getVdate());
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(collection.getVdate()));
		values.put(DatabaseConnection.COLUMN_LAT,collection.getLatitude());
		values.put(DatabaseConnection.COLUMN_LNG,collection.getLongitude());
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,collection.getLatlngTime_stamp());
		if(collection.getColectionImport())
		{
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, collection.getCreatedDate());
//			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
		}
		else
		{
			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
			
			
		}
//
//		try{
//		 id = database.insert(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION, null, values);
//		System.out.println(id);
//		} catch(RuntimeException e){
//			System.out.println("+++++++++++++++++++"+e.toString());
//		}

		try{
			if(c>0)
			{
				try {
					mTrans = true;


					id = database.update(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION,
							values, "Android_id='" +collection.getAndroid_id() + "'", null);
					database.setTransactionSuccessful();
					database.endTransaction();
					mTrans = false;
					System.out.println("row=" + id);
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


					id = database.insert(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION, null, values);
					System.out.println("row=" + id);
					database.setTransactionSuccessful();
					database.endTransaction();
					mTrans = false;
				}
				catch(RuntimeException e){
					if(mTrans)
					{
						database.endTransaction();
						mTrans = false;
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

		return id;
	}

	public long updateCollection(Collection collection){
		mTrans = true;
		database.beginTransaction();
		long id=-1;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ID, collection.getCollId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, collection.getCollDocId());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, collection.getAndroid_id());
		values.put(DatabaseConnection.COLUMN_USR_ID, collection.getUserId());
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, collection.getDistId());
		values.put(DatabaseConnection.COLUMN_SREP_CODE, collection.getSMId());
		values.put(DatabaseConnection.COLUMN_MODE, collection.getMode());
		values.put(DatabaseConnection.COLUMN_AMOUNT, collection.getAmount());
		values.put(DatabaseConnection.COLUMN_PAYMENT_DATE, collection.getPaymentDate());
		values.put(DatabaseConnection.COLUMN_CHEQUE_DDNO, collection.getCheque_DDNo());
		values.put(DatabaseConnection.COLUMN_CHQ_DATE, collection.getCheque_DD_Date());
		values.put(DatabaseConnection.COLUMN_BANK, collection.getBank());
		values.put(DatabaseConnection.COLUMN_BRANCH, collection.getBranch());
		values.put(DatabaseConnection.COLUMN_REMARK, collection.getRemarks());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, collection.getVisitId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE, collection.getVdate());
		values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,collection.getLatlngTime_stamp());
		values.put(DatabaseConnection.COLUMN_LAT,collection.getLatitude());

		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(collection.getVdate()));
		try{

			 id = database.update(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION, values, "Android_id='"+collection.getAndroid_id()+"'", null);
			database.setTransactionSuccessful();
			database.endTransaction();
			mTrans = false;
			System.out.println("row="+id);
		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
			if(mTrans)
			{
				mTrans = false;
				database.endTransaction();
			}
		}
		return id;
	}



	public Collection getCollectionByAndroidId(String androidId)
	{
		String query="select * from DistributerCollection where Android_id='"+androidId+"'";
		System.out.println(query);
		Collection collection = new Collection();
		Cursor cursor=database.rawQuery(query, null);

		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				collection.setAndroid_id(cursor.getString(2));
				collection.setUserId(cursor.getString(3));
				collection.setDistId(cursor.getString(4));
				collection.setSMId(cursor.getString(5));
				collection.setMode(cursor.getString(6));
				collection.setAmount(cursor.getString(7));
				collection.setPaymentDate(cursor.getString(8));
				collection.setCheque_DDNo(cursor.getString(9));
				collection.setCheque_DD_Date(cursor.getString(10));
				collection.setBank(cursor.getString(11));
				collection.setBranch(cursor.getString(12));
				collection.setRemarks(cursor.getString(13));
				collection.setIsUpload(cursor.getString(14));

				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return collection;
	}


	public ArrayList<Collection> getCollectionList(){
		Cursor cursor = database.query(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION,
				allColumns, null, null, null, null, null);
		ArrayList<Collection> collections = new ArrayList<Collection>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Collection collection = new Collection();
//				collection.setId(cursor.getInt(0));
//				collection.setUsr_id(cursor.getString(1));
//				collection.setDistributor_id(cursor.getString(2));
//				collection.setParty_code(cursor.getString(3));
//				collection.setMode(cursor.getString(4));
//				collection.setAmount(cursor.getString(5));
//				collection.setPayment_date(cursor.getString(6));
//				collection.setCheque_ddno(cursor.getString(7));
//				collection.setChq_date(cursor.getString(8));
//				collection.setBank(cursor.getString(9));
//				collection.setBranch(cursor.getString(10));
//				collection.setRemark(cursor.getString(11));
//				collection.setCreated_date(cursor.getString(12));
//				collection.setUsr_recstatus(cursor.getString(13));
//				collection.setArea_code(cursor.getString(14));
//				collection.setBeat_code(cursor.getString(15));
//				collection.setRoute_code(cursor.getString(16));
//				collection.setTime(cursor.getString(17));
				collections.add(collection);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return collections;
	}
	
	public ArrayList<Collection> getCollectionUplaodList(){
//		String sql="select cl.*,pm.webcode,pm.name from collection cl "
//					+" left join partymast pm on pm.code=cl.party_code " +
//					" where cl.isupload='0' " ;

		String sql="select cl.*,pm.webcode,pm.name from DistributerCollection cl "
				+" left join mastDristributor pm on pm.webcode=cl.DistId " +
					" where cl.isupload='0' " ;
		Cursor cursor=database.rawQuery(sql, null);
		ArrayList<Collection> collections = new ArrayList<Collection>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Collection collection = new Collection();
				collection.setAndroid_id(cursor.getString(2));
				collection.setUserId(cursor.getString(3));
				collection.setDistId(cursor.getString(4));
				collection.setSMId(cursor.getString(5));
				collection.setMode(cursor.getString(6));
				collection.setAmount(cursor.getString(7));
				collection.setPaymentDate(cursor.getString(8));
				collection.setCheque_DDNo(cursor.getString(9));
				collection.setCheque_DD_Date(cursor.getString(10));
				collection.setBank(cursor.getString(11));
				collection.setBranch(cursor.getString(12));
				collection.setRemarks(cursor.getString(13));
				collection.setDistName(cursor.getString(16));
				collections.add(collection);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return collections;
	}
	public ArrayList<Collection> getCollectionUplaodList(String vdate){
//		String sql="select cl.*,pm.webcode,pm.name from collection cl "
//					+" left join partymast pm on pm.code=cl.party_code " +
//					" where cl.isupload='0' " ;


		/************************		Write By Sandeep Singh 13-04-2017		******************/
		/*****************		START		******************/
		/************************		Write By Sandeep Singh 20-04-2017		******************/
		/*****************		START		******************/
		String sql="select cl.Android_id,cl.usr_id,cl.DistId,cl.srep_code,cl.mode," +
				"cl.amount,cl.payment_date,cl.cheque_ddno," +
				"cl.chq_date,cl.bank,cl.branch,cl.remark,cl.visit_date,pm.webcode,pm.name,vl1.visit_no,ifnull(cl.latitude,0) as latitude,ifnull(cl.longitude,0) as longitude,ifnull(cl.LatlngTime,0) as LatlngTime from DistributerCollection cl "
				+" left join mastDristributor pm on pm.webcode=cl.DistId " +
				" LEFT JOIN Visitl1 vl1"+
				" ON cl.visit_date = vl1.visit_date"+
				" where "+
				" cl.timestamp=0 and cl.visit_date='"+vdate+"' " ;
		/*String sql="select cl.Android_id,cl.usr_id,cl.DistId,cl.srep_code,cl.mode," +
				"cl.amount,cl.payment_date,cl.cheque_ddno," +
				"cl.chq_date,cl.bank,cl.branch,cl.remark,cl.visit_date,pm.webcode,pm.name,vl1.visit_no from DistributerCollection cl "
				+" left join mastDristributor pm on pm.webcode=cl.DistId " +
				" LEFT JOIN Visitl1 vl1"+
				" ON cl.visit_date = vl1.visit_date"+
				" where vl1.dsr_lock = '1'"+
				" and cl.timestamp=0 and cl.visit_date='"+vdate+"' " ;*/
		/*****************		END		******************/
		/*String sql="select cl.Android_id,cl.usr_id,cl.DistId,cl.srep_code,cl.mode," +
				"cl.amount,cl.payment_date,cl.cheque_ddno," +
				"cl.chq_date,cl.bank,cl.branch,cl.remark,cl.visit_date,pm.webcode,pm.name,vl1.visit_no from DistributerCollection cl "
				+" left join mastDristributor pm on pm.webcode=cl.DistId " +
				" LEFT JOIN Visitl1 vl1"+
				" ON cl.visit_date = vl1.visit_date"+
				" where vl1.dsr_lock = '1'"+
				" and cl.isupload='0' and cl.visit_date='"+vdate+"' " ;*/
		/*****************		END		******************/
		Cursor cursor=database.rawQuery(sql, null);
		ArrayList<Collection> collections = new ArrayList<Collection>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Collection collection = new Collection();
				collection.setAndroid_id(cursor.getString(0));
				collection.setUserId(cursor.getString(1));
				collection.setDistId(cursor.getString(2));
				collection.setSMId(cursor.getString(3));
				collection.setMode(cursor.getString(4));
				collection.setAmount(cursor.getString(5));
				collection.setPaymentDate(cursor.getString(6));
				collection.setCheque_DDNo(cursor.getString(7));
				collection.setCheque_DD_Date(cursor.getString(8));
				collection.setBank(cursor.getString(9));
				collection.setBranch(cursor.getString(10));
				collection.setRemarks(cursor.getString(11));
				collection.setVdate(cursor.getString(12));
				collection.setDistId(cursor.getString(13));
				collection.setDistName(cursor.getString(14));
				collection.setVisitId(cursor.getString(15));
	/************************		Write By Sandeep Singh 20-04-2017		******************/
				/*****************		START		******************/
				collection.setLatitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT)));
				collection.setLongitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LNG)));
				collection.setLatlngTime_stamp(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT_LNG_TIME)));
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
	public ArrayList<Collection> getCollectionRawList(){
		String sql="select cl.*,pm.webcode,pm.name from collection cl "
					+" left join partymast pm on pm.code=cl.party_code ";
					
		Cursor cursor=database.rawQuery(sql, null);
		ArrayList<Collection> collections = new ArrayList<Collection>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Collection collection = new Collection();
//				collection.setId(cursor.getInt(0));
//				collection.setUsr_id(cursor.getString(1));
//				collection.setDistributor_id(cursor.getString(2));
//				collection.setParty_code(cursor.getString(3));
//				collection.setMode(cursor.getString(4));
//				collection.setAmount(cursor.getString(5));
//				collection.setPayment_date(cursor.getString(6));
//				collection.setCheque_ddno(cursor.getString(7));
//				collection.setChq_date(cursor.getString(8));
//				collection.setBank(cursor.getString(9));
//				collection.setBranch(cursor.getString(10));
//				collection.setRemark(cursor.getString(11));
//				collection.setCreated_date(cursor.getString(12));
//				collection.setUsr_recstatus(cursor.getString(13));
//				collection.setArea_code(cursor.getString(14));
//				collection.setBeat_code(cursor.getString(15));
//				collection.setRoute_code(cursor.getString(16));
//				collection.setTime(cursor.getString(17));
//				collection.setReciep_no(cursor.getString(18));
//				collection.setWebcode(cursor.getString(20));
//				collection.setParty_name(cursor.getString(21));
				collections.add(collection);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return collections;
	}
	//	public  long updateCollectionUpload(String androiddocId, String webdocId,int cno){
	/************************		Write By Sandeep Singh 10-04-2017		******************/
	/*****************		START		******************/
	public  long updateCollectionUpload(String androiddocId, String webdocId,int cno,String timeStamp){
		/*****************		END		******************/
		long id=0;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_UPLOAD,"1");
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID,webdocId);
		values.put(DatabaseConnection.COLUMN_ID,cno);



		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
		/*****************		END		******************/

		try{
			id = database.update(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION, values, "Android_id='"+androiddocId+"'", null);
			System.out.println("row="+id);
		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		return id;
	}


	public String getAndroidDocId(String distId,String vdate){
		String Adoc_id = "0";
		String query = "select ifnull(Android_id,0) as MyColumn from DistributerCollection where DistId='"+distId+"' and payment_date='"+vdate+"'";
		Cursor cursor = database.rawQuery(query,null);


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

//		 if (cursor.getCount()==1)
//		    {
//		            cursor.moveToLast();
//		            Adoc_id=cursor.getString(0);
//		            System.out.println("max id found "+Adoc_id);
//
//		    }
//		 else{
//			System.out.println("no max id found");
//		}
		 System.out.println("no max id found"+Adoc_id);
		return Adoc_id;
	}
	

	public ArrayList<Collection> getCollectionListFromAndToDate(String fromDate, String toDate, String distId) {
		String query="";
		
		 query = "select dc.web_doc_id,dc.Android_id,md.name," +
		 		"dc.mode,dc.amount,dc.payment_date,dc.cheque_ddno," +
		 		"dc.chq_date,dc.isUpload,dc.DistId,md.sync_id "
				+ "from DistributerCollection dc " +
				"left join mastDristributor md on md.webcode=dc.DistId " +
				"where dc.DistId='"+distId+"'  " +
				"and dc.visit_date " +
				"='"+fromDate+"' " +
				"" +

				"order by dc.timestamp desc";
		 
		 System.out.println(query);
		Cursor cursor=database.rawQuery(query, null);
		ArrayList<Collection> collections = new ArrayList<Collection>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Collection collection = new Collection();
				collection.setCollDocId(cursor.getString(0));
				collection.setAndroid_id(cursor.getString(1));
				collection.setDistName(cursor.getString(2));
				collection.setMode(cursor.getString(3));
				collection.setAmount(cursor.getString(4));
				collection.setPaymentDate(cursor.getString(5));
				collection.setCheque_DDNo(cursor.getString(6));
				collection.setCheque_DD_Date(cursor.getString(7));
				collection.setIsUpload(cursor.getString(8));
				collection.setDistId(cursor.getString(9));
				collection.setSyncId(cursor.getString(10));
				collections.add(collection);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return collections;
		
	}
	
	public float getTotalPrimaryCollection(String date)
	{
		float sum=0;
		ArrayList<Float> primaryCollection=new ArrayList<Float>();
		
		String query1="select ifNull(sum(amount),0)  as pamount1 from distributercollection where payment_date='"+date+"'";
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
	
	sum=primaryCollection.get(0);
		
		return sum;
	}



	public boolean checkRno(String rno)
	{
		Cursor c = database.query(DatabaseConnection.TABLE_TRANSCOLLECTION,null, DatabaseConnection.COLUMN_ANDROID_DOCID+"=?",new String[]{rno},null,null,null);
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
	
	
	
	

