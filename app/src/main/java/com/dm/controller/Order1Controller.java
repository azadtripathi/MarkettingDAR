package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.library.DateFunction;
import com.dm.model.Order1;

import java.util.ArrayList;

public class Order1Controller {

	Context mContext;
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	boolean mTrans = false;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_ORDER1_NO,
			DatabaseConnection.COLUMN_ORDER_NO,
			DatabaseConnection.COLUMN_VISIT_NO,
			DatabaseConnection.COLUMN_WEB_DOC_ID,
			DatabaseConnection.COLUMN_SERIAL_NO,
			DatabaseConnection.COLUMN_USR_ID,
			DatabaseConnection.COLUMN_VISIT_DATE,
			DatabaseConnection.COLUMN_SREP_CODE,
			DatabaseConnection.COLUMN_PARTY_CODE,
			DatabaseConnection.COLUMN_AREA_CODE,
			DatabaseConnection.COLUMN_PRODUCT_CODE,
			DatabaseConnection.COLUMN_QTY,
			DatabaseConnection.COLUMN_FREE_QTY,
			DatabaseConnection.COLUMN_RATE,
			DatabaseConnection.COLUMN_DISCOUNT,
			DatabaseConnection.COLUMN_REMARK,
			DatabaseConnection.COLUMN_MEET_FLAG,
			DatabaseConnection.COLUMN_MEET_DOCID,
						
		};
	
	public Order1Controller(Context context) {
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
	
	public long insertOrder1(Order1 order1){
		database.beginTransaction();
		long id=0;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ORDER1_NO, order1.getOrd1Id());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, order1.getVisId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, order1.getOrdDocId());

		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID       ,order1.getOrderAndroid_Id());
		values.put(DatabaseConnection.COLUMN_ORDER1_ANDROID_DOCID,order1.getAndroid_Id());



		values.put(DatabaseConnection.COLUMN_SERIAL_NO,order1.getSno());
		values.put(DatabaseConnection.COLUMN_USR_ID,order1.getUserId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE,order1.getVDate());
		values.put(DatabaseConnection.COLUMN_SREP_CODE,order1.getSMId());
		values.put(DatabaseConnection.COLUMN_PARTY_CODE,order1.getPartyId());
		values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE,order1.getAndPartyId());
		values.put(DatabaseConnection.COLUMN_AREA_CODE,order1.getAreaId());
		values.put(DatabaseConnection.COLUMN_BEAT_CODE,order1.getBeatId());
		values.put(DatabaseConnection.COLUMN_PRODUCT_CODE,order1.getItemId());
		values.put(DatabaseConnection.COLUMN_QTY,order1.getQty());
		values.put(DatabaseConnection.COLUMN_RATE,order1.getRate());
		values.put(DatabaseConnection.COLUMN_DISCOUNT,order1.getDiscount());
		values.put(DatabaseConnection.COLUMN_REMARK,order1.getRemarks());
		values.put(DatabaseConnection.COLUMN_MEET_FLAG,order1.getMeetFlag());
		values.put(DatabaseConnection.COLUMN_MEET_DOCID,order1.getMeetDocId());
		values.put(DatabaseConnection.COLUMN_AMOUNT,order1.getAmount());
		values.put(DatabaseConnection.COLUMN_CASES,order1.getCases());
		values.put(DatabaseConnection.COLUMN_UNIT,order1.getUnit());
		values.put(DatabaseConnection.COLUMN_LAT,order1.getLatitude());
		values.put(DatabaseConnection.COLUMN_LNG,order1.getLongitude());
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, order1.getLatlngTimeStamp());
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(order1.getVDate()));
		System.out.println("row no "+ order1.getOrdDocId()+" inserted");
		
		if(order1.getNewOrder())
		{
			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");
		
		}
		else{
			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");
		}
		
		
		try{

			mTrans = true;
		 id = database.insert(DatabaseConnection.TABLE_ORDER1, null, values);
			database.setTransactionSuccessful();
			database.endTransaction();
			mTrans = false;
		System.out.println(id);
		} 
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
			if(mTrans)
			{
				database.endTransaction();
				mTrans = false;
			}
		}
		return id;
	}

	public String getAlteredPrice(String item_id, String partycode) {
		String price = "";
		String where = "code='" + partycode + "' and item_id='" + item_id + "'";
		String sql = "select ifnull(rate,'') from history where " + where;
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			price = cursor.getString(0);
			System.out.println("price=" + price);
		} else {
			System.out.println("no price is found");
		}
		System.out.println("price=" + price);
		return price;

	}
	public long insertOrder1(

			String Ord1Id,
			String OrdId,
			String VisId,
			String OrdDocId,
			String Sno,
			String UserId,
			String VDate,
			String SMID,
			String Partyid,
			String AreaId,
			String Item,
			String Qty,
			String Rate,
			String Remarks,
			String amount,
			String Android_Id,
			String Android_Id1,
			String Millisecond,
			String cases,
			String unit,
			boolean getNewOrder,
			String lat,String longi,String latlongtimestamp



	){
		long id=0;
		ContentValues values = new ContentValues();
		//database.beginTransaction();
		 boolean flag=false,updateflag=false;

		DsrController dc = new DsrController(mContext);
		dc.open();
		boolean isDsrLock = dc.isDsrLockForVisitDate(VDate);
		dc.close();


		if(isDsrLock)
		{
			values.put(DatabaseConnection.COLUMN_ORDER1_NO, Ord1Id);
			values.put(DatabaseConnection.COLUMN_ORDER_NO, OrdId);
			values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, OrdDocId);
			values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, Android_Id);
			values.put(DatabaseConnection.COLUMN_ORDER1_ANDROID_DOCID, Android_Id1);

			values.put(DatabaseConnection.COLUMN_SERIAL_NO, Sno);
			values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
			values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
			values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
			values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
			values.put(DatabaseConnection.COLUMN_AREA_CODE, AreaId);
			values.put(DatabaseConnection.COLUMN_PRODUCT_CODE, Item);
			values.put(DatabaseConnection.COLUMN_QTY, Qty);
			values.put(DatabaseConnection.COLUMN_RATE, Rate);
			values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
			values.put(DatabaseConnection.COLUMN_AMOUNT, amount);
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));

			values.put(DatabaseConnection.COLUMN_LAT, lat);
			values.put(DatabaseConnection.COLUMN_LNG, longi);
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongtimestamp);
			values.put(DatabaseConnection.COLUMN_CASES, cases);
			values.put(DatabaseConnection.COLUMN_UNIT, unit);
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, Millisecond);
			values.put(DatabaseConnection.COLUMN_CASES, cases);
			values.put(DatabaseConnection.COLUMN_UNIT, unit);
			values.put(DatabaseConnection.COLUMN_ORDER1_ANDROID_DOCID, Android_Id1);

			id = database.update(DatabaseConnection.TABLE_ORDER1, values, "Android_id='" + Android_Id + "' AND Ord1Android_id='" + Android_Id1 + "'", null);
			if(id>0)
			{
				// do nothing
			}
			else
			{
				id = database.insert(DatabaseConnection.TABLE_ORDER1,null,values);
			}

			values = null;
		}
		else {
			String qry = "select timestamp from TransOrder1 where Android_id='" + Android_Id + "' AND Ord1Android_id='" + Android_Id1 + "'";
			System.out.println(qry);
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
				values.put(DatabaseConnection.COLUMN_ORDER1_ANDROID_DOCID, Android_Id1);
			}
			cursor.close();
//************************************************************
			values.put(DatabaseConnection.COLUMN_ORDER1_NO, Ord1Id);
			values.put(DatabaseConnection.COLUMN_ORDER_NO, OrdId);
			values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
			values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, OrdDocId);
			values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, Android_Id);
			values.put(DatabaseConnection.COLUMN_ORDER1_ANDROID_DOCID, Android_Id1);

			values.put(DatabaseConnection.COLUMN_SERIAL_NO, Sno);
			values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
			values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
			values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
			values.put(DatabaseConnection.COLUMN_PARTY_CODE, Partyid);
			values.put(DatabaseConnection.COLUMN_AREA_CODE, AreaId);
			values.put(DatabaseConnection.COLUMN_PRODUCT_CODE, Item);
			values.put(DatabaseConnection.COLUMN_QTY, Qty);
			values.put(DatabaseConnection.COLUMN_RATE, Rate);
			values.put(DatabaseConnection.COLUMN_REMARK, Remarks);
			values.put(DatabaseConnection.COLUMN_AMOUNT, amount);
			values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));

			values.put(DatabaseConnection.COLUMN_LAT, lat);
			values.put(DatabaseConnection.COLUMN_LNG, longi);
			values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongtimestamp);
			values.put(DatabaseConnection.COLUMN_CASES, cases);
			values.put(DatabaseConnection.COLUMN_UNIT, unit);
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, Millisecond);
			values.put(DatabaseConnection.COLUMN_CASES, cases);
			values.put(DatabaseConnection.COLUMN_UNIT, unit);

			try {
				if (flag) {
					if (!updateflag) {
						mTrans = true;

						id = database.update(DatabaseConnection.TABLE_ORDER1, values, "Android_id='" + Android_Id + "' AND Ord1Android_id='" + Android_Id1 + "'", null);

						/*database.setTransactionSuccessful();
						database.endTransaction();
						mTrans = false;*/
						System.out.println("row=" + id);
					} else {
						CompetitorController.isUpdateFlag = true;
					/*	database.endTransaction();
						mTrans = false;*/
					}
				} else {
					mTrans = true;
					id = database.insert(DatabaseConnection.TABLE_ORDER1, null, values);
					System.out.println("row=" + id);
					/*database.setTransactionSuccessful();
					database.endTransaction();*/
				}
			} catch (RuntimeException e) {
				if (mTrans) {
					//database.endTransaction();
					mTrans = false;
				}
				System.out.println("+++++++++++++++++++" + e.toString());
			}
			values = null;
			cursor = null;
		}

		/*long id=0;
		//		***************update code******************************************************

		database.beginTransaction();
		int c=0;
		String qry="select count(*) from TransOrder1 where Android_id='"+Android_Id1+"'";
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
			values.put(DatabaseConnection.COLUMN_ORDER1_ANDROID_DOCID,Android_Id1);
		}


/*//************************************************************

		values.put(DatabaseConnection.COLUMN_ORDER1_NO, Ord1Id);
		values.put(DatabaseConnection.COLUMN_ORDER_NO, OrdId);
		values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, OrdDocId);
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,Android_Id);
		values.put(DatabaseConnection.COLUMN_SERIAL_NO,Sno);
		values.put(DatabaseConnection.COLUMN_USR_ID,UserId);
		values.put(DatabaseConnection.COLUMN_VISIT_DATE,VDate);
		values.put(DatabaseConnection.COLUMN_SREP_CODE,SMID);
		values.put(DatabaseConnection.COLUMN_PARTY_CODE,Partyid);
		values.put(DatabaseConnection.COLUMN_AREA_CODE,AreaId);
		values.put(DatabaseConnection.COLUMN_PRODUCT_CODE,Item);
		values.put(DatabaseConnection.COLUMN_QTY,Qty);
		values.put(DatabaseConnection.COLUMN_RATE,Rate);
		values.put(DatabaseConnection.COLUMN_REMARK,Remarks);
		values.put(DatabaseConnection.COLUMN_AMOUNT,amount);
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
*//************************		Write By Sandeep Singh 20-04-2017		******************//*
		*//*****************		START		******************//*
		values.put(DatabaseConnection.COLUMN_LAT, lat);
		values.put(DatabaseConnection.COLUMN_LNG, longi);
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongtimestamp);
		values.put(DatabaseConnection.COLUMN_CASES,cases);
		values.put(DatabaseConnection.COLUMN_UNIT,unit);
		*//*****************		END		******************//*

		if(getNewOrder)
		{
//			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction.ConvertDateToTimestamp(VDate+" 00:00:00", "dd/MMM/yyyy 00:00:00"));

		}
		else{
			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, Millisecond);
		}
		values.put(DatabaseConnection.COLUMN_CASES, cases);
		values.put(DatabaseConnection.COLUMN_UNIT, unit);


//		try{
//			id = database.insert(DatabaseConnection.TABLE_ORDER1, null, values);
//			System.out.println(id);
//		}
//		catch(RuntimeException e){
//			System.out.println("+++++++++++++++++++"+e.toString());
//		}

		try{
			if(c>0)
			{
				try {
					*//*
					 Start work on 21 April 2017 vinayak
					 *//*

					mTrans = true;
					id = database.update(DatabaseConnection.TABLE_ORDER1,
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
					*//*
					 Start work on 21 April 2017 vinayak
					 *//*

					mTrans = true;
					id = database.insert(DatabaseConnection.TABLE_ORDER1, null, values);

					database.setTransactionSuccessful();
					database.endTransaction();
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
		return id;
	}

	public void deleteorderTransactionRowByDatePartyCode(String date,
			String partyCode, String aPartyCode) {
		


		if(partyCode!=null && aPartyCode!=null)	
		{
			//database.beginTransaction();
			int i = database.delete(DatabaseConnection.TABLE_ORDER1, "visit_date='" + date
					+ "' AND party_code='" + partyCode + "'", null);
			
			int j = database.delete(DatabaseConnection.TABLE_ORDER, "visit_date='" + date
					+ "' AND party_code='" + partyCode + "'", null);

			/*database.setTransactionSuccessful();
			database.endTransaction();*/
			System.out
					.println("orderTransaction deleted for date" + " result=" + i);
		}
		else if(partyCode!=null && aPartyCode==null)
	{

		//database.beginTransaction();
			int i = database.delete(DatabaseConnection.TABLE_ORDER1, "visit_date='" + date
					+ "' AND party_code='" + partyCode + "'", null);
			
			int j = database.delete(DatabaseConnection.TABLE_ORDER, "visit_date='" + date
					+ "' AND party_code='" + partyCode + "'", null);
			System.out
					.println("orderTransaction deleted for date" + " result=" + i);

		//database.setTransactionSuccessful();
	}
	
	else if(partyCode==null && aPartyCode!=null)
	{

		//database.beginTransaction();
		int i = database.delete(DatabaseConnection.TABLE_ORDER1, "visit_date='" + date
				+ "' AND and_party_code='" + aPartyCode + "'", null);
		
		int j = database.delete(DatabaseConnection.TABLE_ORDER, "visit_date='" + date
				+ "' AND and_party_code='" + aPartyCode + "'", null);
		System.out
				.println("orderTransaction deleted for date" + " result=" + i);

		//database.setTransactionSuccessful();
	
	}
		
		
		
	}
	
	public void UpdateData(String item_id, String partycode, String price) {
		ContentValues values = new ContentValues();
		values.put("rate", price);
		String where = "code='" + partycode + "' and item_id='" + item_id + "'";
		int i = database.update(DatabaseConnection.TABLE_HISTORY, values,
				where, null);
		System.out.println("price updated=" + i);

	}


	public ArrayList<Order1> getOrderDataList(String vdate, String areaId){
//		String query="select o.party_code,p.name,o.product_code,mp.name," +
//				"o.qty,o.rate,mp.stdpack,o.amount from transorder1 o " +
//				"LEFT JOIN mastParty p ON o.party_code = p.webcode " +
//				"LEFT JOIN MastProduct mp ON o.product_code = mp.webcode " +
//				"LEFT JOIN Visitl1 vl1 ON o.visit_date = vl1.visit_date " +
//				"where o.visit_date='"+vdate+"' " +
//				"order by p.name,mp.name";

//		String query="select o.party_code,p.name,mp.sync_id,mp.name," +
//				"o.qty,o.rate,mp.stdpack,o.amount from transorder1 o " +
//				"LEFT JOIN mastParty p ON o.party_code = p.webcode " +
//				"LEFT JOIN MastProduct mp ON o.product_code = mp.webcode " +
//				"LEFT JOIN Visitl1 vl1 ON o.visit_date = vl1.visit_date " +
//				"where o.visit_date='"+vdate+"' and o.area_code='"+areaId+"' and vl1.dsr_lock = '1'"+
//		" AND o.isUpload = '0'" +
//				"order by p.name,mp.name";

		String query="select ifnull(o.party_code,''),p.name,mp.sync_id,mp.name," +
				"o.qty,o.rate,mp.stdpack, sum(o.qty*o.rate) as amount,mb.name,p.address1,p.address2 from transorder1 o " +
				"LEFT JOIN mastParty p ON (o.party_code = p.webcode or o.and_party_code = p.Android_id) " +
				"LEFT JOIN MastProduct mp ON o.product_code = mp.webcode " +
				"LEFT JOIN Visitl1 vl1 ON o.visit_date = vl1.visit_date " +
				"LEFT JOIN mastBeat mb ON o.beat_code = mb.webcode " +
				"where o.visit_date='"+vdate+"' and o.area_code='"+areaId+"'  " +
				" group by o.party_code,p.name,mp.sync_id,mp.name,o.qty,o.rate,mp.stdpack,o.amount order by p.name,mp.name";

		System.out.println(query);
		Cursor cursor=database.rawQuery(query, null);
		ArrayList<Order1> orders = new ArrayList<Order1>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				Order1 order=new Order1();
				order.setPartyId(cursor.getString(0));
				order.setPartyName(cursor.getString(1));
				order.setItemId(cursor.getString(2));
				order.setItemName(cursor.getString(3));
				order.setQty(cursor.getString(4));
				order.setRate(cursor.getString(5));
				order.setStdPkg(cursor.getString(6));
				order.setAmount(cursor.getString(7));
				order.setBeatId(cursor.getString(8));
				order.setAndPartyId(cursor.getString(9)+"\n"+cursor.getString(10));
				orders.add(order);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return orders;
	}

	public ArrayList<Order1> getExistDocId(String date,
										   String partyCode) {
		String query = "select web_doc_id,Android_id,visit_date,party_code,product_code,qty,rate,amount from TransOrder1 where visit_date='"
				+ date + "' and party_code='" + partyCode + "'";

		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Order1> orderTransactions = new ArrayList<Order1>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Order1 orderTransaction = new Order1();
				orderTransaction.setOrdDocId(cursor.getString(0));
				orderTransaction.setAndroid_Id(cursor.getString(1));
				orderTransaction.setVDate(cursor.getString(2));
				orderTransaction.setPartyId(cursor.getString(3));
				orderTransaction.setItemId(cursor.getString(4));
				orderTransaction.setQty(cursor.getString(5));
				orderTransaction.setRate(cursor.getString(6));
				orderTransaction.setAmount(cursor.getString(7));
				orderTransactions.add(orderTransaction);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}






	public ArrayList<Order1> getExistDocIdSalesReturn(String date, String partyCode, String aPartyCode) {
		String query="";
		Cursor cursor=null;
		ArrayList<Order1> orderTransactions=null;
		try{
			if(partyCode!=null && aPartyCode!=null)
			{
				query = "select distinct web_doc_id,Android_id,visit_date,party_code,product_code," +
						"qty,rate,amount,and_party_code,ifnull(_case,'0'),ifnull(Unit,'0'),Ord1Android_id,Batch_No,MFD_Date" +
						" from "+DatabaseConnection.TABLE_SALES_RETURN1+" where visit_date='"
						+ date + "' and party_code='" + partyCode + "'";
			}
			else if(partyCode!=null && aPartyCode==null)
			{
				query = "select distinct web_doc_id,Android_id,visit_date,party_code,product_code," +
						"qty,rate,amount,and_party_code,ifnull(_case,'0'),ifnull(Unit,'0'),Ord1Android_id,Batch_No,MFD_Date from "+DatabaseConnection.TABLE_SALES_RETURN1+" where visit_date='"
						+ date + "' and party_code='" + partyCode + "'";
			}

			else if(partyCode==null && aPartyCode!=null)
			{

				query = "select distinct web_doc_id," +
						"Android_id," +
						"visit_date," +
						"party_code," +
						"product_code," +
						"qty," +
						"rate," +
						"amount," +
						"and_party_code," +
						"ifnull(_case,'0')," +
						"ifnull(Unit,'0')" +
						", Ord1Android_id, Batch_No,MFD_Date" +
						" from "+DatabaseConnection.TABLE_SALES_RETURN1+" where visit_date='"
						+ date + "' and and_party_code='" + aPartyCode + "'";
			}

			System.out.println(query);
			cursor = database.rawQuery(query, null);
			orderTransactions = new ArrayList<Order1>();
			if (cursor.moveToFirst()) {
				while (!(cursor.isAfterLast())) {
					Order1 orderTransaction = new Order1();
					orderTransaction.setOrdDocId(cursor.getString(0));
					orderTransaction.setOrderAndroid_Id(cursor.getString(1));
					orderTransaction.setVDate(cursor.getString(2));
					orderTransaction.setPartyId(cursor.getString(3));
					orderTransaction.setItemId(cursor.getString(4));
					orderTransaction.setQty(cursor.getString(5));
					orderTransaction.setRate(cursor.getString(6));
					orderTransaction.setAmount(cursor.getString(7));
					orderTransaction.setAndPartyId(cursor.getString(8));
					orderTransaction.setCases(cursor.getString(9));
					orderTransaction.setUnit(cursor.getString(5));
					orderTransaction.setAndroid_Id(cursor.getString(11));
					orderTransaction.setBatchNo(cursor.getString(12));
					orderTransaction.setMfd(cursor.getString(13));
					orderTransactions.add(orderTransaction);


					cursor.moveToNext();
				}
			} else {
				System.out.println("No records found");
			}

			cursor.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return orderTransactions;
	}



	public ArrayList<Order1> getExistDocId(String date, String partyCode, String aPartyCode)
	{
		String query="";
		Cursor cursor=null;
		ArrayList<Order1> orderTransactions=null;
		try{
			if(partyCode!=null && aPartyCode!=null)	
			{
				query = "select distinct web_doc_id,Android_id,visit_date,party_code,product_code,qty,rate,amount,and_party_code,ifnull(_case,'0'),ifnull(Unit,'0'),Ord1Android_id" +
						" from TransOrder1 where visit_date='"
						+ date + "' and party_code='" + partyCode + "'";
			}
			else if(partyCode!=null && aPartyCode==null)
		{
			query = "select distinct web_doc_id,Android_id,visit_date,party_code,product_code,qty,rate,amount,and_party_code,ifnull(_case,'0'),ifnull(Unit,'0'),Ord1Android_id from TransOrder1 where visit_date='"
					+ date + "' and party_code='" + partyCode + "'";
		}
		
		else if(partyCode==null && aPartyCode!=null)
		{
		
		 query = "select distinct web_doc_id," +
				 "Android_id," +
				 "visit_date," +
				 "party_code," +
				 "product_code," +
				 "qty," +
				 "rate," +
				 "amount," +
				 "and_party_code," +
				 "ifnull(_case,'0')," +
				 "ifnull(Unit,'0')" +
				 ", Ord1Android_id from TransOrder1 where visit_date='"
				+ date + "' and and_party_code='" + aPartyCode + "'";
		}
		
		System.out.println(query);
		 cursor = database.rawQuery(query, null);
		 orderTransactions = new ArrayList<Order1>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Order1 orderTransaction = new Order1();
				orderTransaction.setOrdDocId(cursor.getString(0));
				orderTransaction.setOrderAndroid_Id(cursor.getString(1));
				orderTransaction.setVDate(cursor.getString(2));
				orderTransaction.setPartyId(cursor.getString(3));
				orderTransaction.setItemId(cursor.getString(4));
				orderTransaction.setQty(cursor.getString(5));
				orderTransaction.setRate(cursor.getString(6));
				orderTransaction.setAmount(cursor.getString(7));
				orderTransaction.setAndPartyId(cursor.getString(8));
				orderTransaction.setCases(cursor.getString(9));
				orderTransaction.setUnit(cursor.getString(5));
				orderTransaction.setAndroid_Id(cursor.getString(11));
				orderTransactions.add(orderTransaction);

				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		
		cursor.close();
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return orderTransactions;
	}
	
	
	public String getAlteredPrice(String item_id, String partycode, String aPartyCode) {
		String price = "";
		String where = "(code='" + partycode + "' or webcode='" + aPartyCode + "') and item_id='" + item_id + "'";
		String sql = "select ifnull(rate,'') from history where " + where;
		System.out.println(sql);
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			price = cursor.getString(0);
			System.out.println("price=" + price);
		} else {
			System.out.println("no price is found");
		}
		System.out.println("price=" + price);
		return price;
	}

	public ArrayList<Order1> getUploadOrder1List(String vdate){
		/************************		Write By Sandeep Singh 20-04-2017		******************/
		/*****************		START		******************/
		String query="select o.Ord1Android_id," +
				"o.order1_no," +
				"vl1.visit_no," +
				"o.web_doc_id," +
				"o.sno,o.usr_id," +
				"o.visit_date," +
				"o.srep_code," +
				"o.party_code," +
				"o.area_code," +
				"o.product_code," +
				"o.qty," +
				"o.rate," +
				"o.amount," +
				"o.remark," +
				"o.Android_id," +
				"ifnull(o.latitude,0) as latitude," +
				"ifnull(o.longitude,0) as longitude," +
				"ifnull(o.LatlngTime,0) as LatlngTime"+
				",o._case," +
				"o.Unit " +
				"from TransOrder1 o"+
				" LEFT JOIN Visitl1 vl1"+
				" ON o.visit_date = vl1.visit_date"+
				" LEFT JOIN mastParty p"+
				" ON o.party_code = p.webcode"+
				" WHERE  "+
				" o.timestamp = '0' AND o.visit_date = '"+vdate+"'";

	/*	String query="select o.Ord1Android_id,o.order_no," +
				"vl1.visit_no,o.web_doc_id,o.sno,o.usr_id,o.visit_date,o.srep_code,o.party_code," +
				"o.area_code,o.product_code,o.qty,o.rate,o.amount,o.remark,o.Android_id"+
				" from TransOrder1 o"+
				" LEFT JOIN Visitl1 vl1"+
				" ON o.visit_date = vl1.visit_date"+
				" LEFT JOIN mastParty p"+
				" ON o.party_code = p.webcode"+
				" WHERE  vl1.dsr_lock = '1'"+
				" AND o. isUpload = '0' AND o.visit_date = '"+vdate+"'";*/
		/*****************		END		******************/
		System.out.println(query);

		System.out.println(query);
		Cursor cursor=database.rawQuery(query, null);
		ArrayList<Order1> orders1 = new ArrayList<Order1>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				Order1 order1=new Order1();
				order1.setAndroid_Id(cursor.getString(0));
				order1.setOrdId(cursor.getString(1));
				order1.setVisId(cursor.getString(2));
				order1.setOrdDocId(cursor.getString(3));
				order1.setSno(cursor.getString(4));
				order1.setUserId(cursor.getString(5));
				order1.setVDate(cursor.getString(6));
				order1.setSMId(cursor.getString(7));
				order1.setPartyId(cursor.getString(8));
				order1.setAreaId(cursor.getString(9));
				order1.setItemId(cursor.getString(10));
				order1.setQty(cursor.getString(11));
				order1.setRate(cursor.getString(12));
				order1.setAmount(cursor.getString(13));
				order1.setRemarks(cursor.getString(14));
				order1.setOrderAndroid_Id(cursor.getString(15));
				order1.setCases(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_CASES)));
				order1.setUnit(cursor.getString(cursor.getColumnIndex("qty")));
				/************************		Write By Sandeep Singh 20-04-2017		******************/
				/*****************		START		******************/
				order1.setLatitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT)));
				order1.setLongitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LNG)));
				order1.setLatlngTimeStamp(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT_LNG_TIME)));
				/*****************		END		******************/
				orders1.add(order1);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return orders1;
	}

	//public  long updateOrder1Upload(String androiddocId){
	/************************		Write By Sandeep Singh 10-04-2017		******************/
	/*****************		START		******************/
	public  long updateOrder1Upload(String androiddocId,String timeStamp){
		/*****************		END		******************/
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_UPLOAD,"1");
		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);

		/*****************		END		******************/
		long id=0;
//		values.put(DatabaseConnection.COLUMN_PURCHASE_ORDER1ID,ono);

//		values.put(DatabaseConnection.COLUMN_VISIT_NO,vId);
		try
		{
			id = database.update(DatabaseConnection.TABLE_ORDER1, values, "Ord1Android_id='"+androiddocId+"'", null);
			System.out.println("row="+id);

		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
		return id;
	}





	public boolean isAndroidIdExist(String android_doc_id)
	{
		Cursor c = database.query(DatabaseConnection.TABLE_ORDER1,null,"Ord1Android_id='" +android_doc_id + "'",null,null,null,null);
		if(c.getCount()>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}



	public long updateOrder1(String androidId,Order1 order1)
	{
		long id = 0;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_ORDER1_NO, order1.getOrd1Id());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, order1.getVisId());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, order1.getOrdDocId());

		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID       ,order1.getOrderAndroid_Id());
		values.put(DatabaseConnection.COLUMN_ORDER1_ANDROID_DOCID,order1.getAndroid_Id());



		values.put(DatabaseConnection.COLUMN_SERIAL_NO,order1.getSno());
		values.put(DatabaseConnection.COLUMN_USR_ID,order1.getUserId());
		values.put(DatabaseConnection.COLUMN_VISIT_DATE,order1.getVDate());
		values.put(DatabaseConnection.COLUMN_SREP_CODE,order1.getSMId());
		values.put(DatabaseConnection.COLUMN_PARTY_CODE,order1.getPartyId());
		values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE,order1.getAndPartyId());
		values.put(DatabaseConnection.COLUMN_AREA_CODE,order1.getAreaId());
		values.put(DatabaseConnection.COLUMN_BEAT_CODE,order1.getBeatId());
		values.put(DatabaseConnection.COLUMN_PRODUCT_CODE,order1.getItemId());
		values.put(DatabaseConnection.COLUMN_QTY,order1.getQty());
		values.put(DatabaseConnection.COLUMN_RATE,order1.getRate());
		values.put(DatabaseConnection.COLUMN_DISCOUNT,order1.getDiscount());
		values.put(DatabaseConnection.COLUMN_REMARK,order1.getRemarks());
		values.put(DatabaseConnection.COLUMN_MEET_FLAG,order1.getMeetFlag());
		values.put(DatabaseConnection.COLUMN_MEET_DOCID,order1.getMeetDocId());
		values.put(DatabaseConnection.COLUMN_AMOUNT,order1.getAmount());
		values.put(DatabaseConnection.COLUMN_CASES,order1.getCases());
		values.put(DatabaseConnection.COLUMN_UNIT,order1.getUnit());
		values.put(DatabaseConnection.COLUMN_LAT,order1.getLatitude());
		values.put(DatabaseConnection.COLUMN_LNG,order1.getLongitude());
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, order1.getLatlngTimeStamp());
		values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(order1.getVDate()));
		System.out.println("row no "+ order1.getOrdDocId()+" inserted");

		if(order1.getNewOrder())
		{
			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");

		}
		else{
			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");
		}


		try {


			id = database.update(DatabaseConnection.TABLE_ORDER1,values, "Ord1Android_id=?", new String[]{androidId});
		}
		catch (Exception e)
		{

		}
		return id;
	}

}
