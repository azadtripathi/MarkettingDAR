package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.Item;
import com.dm.model.Owner;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ItemController {
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	File myFile = null;
	FileOutputStream fOut = null;
	OutputStreamWriter myOutWriter = null;
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private String[] allColumns = { 
		    DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME, DatabaseConnection.COLUMN_UNIT,
			DatabaseConnection.COLUMN_STDPACK, DatabaseConnection.COLUMN_MRP,
			DatabaseConnection.COLUMN_DP, DatabaseConnection.COLUMN_RP,
			DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE,
			DatabaseConnection.COLUMN_ITEM_CODE,
			DatabaseConnection.COLUMN_SEGMENT,
			DatabaseConnection.COLUMN_CLASS_ID,
			DatabaseConnection.COLUMN_PRICE_GROUP,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_CREATED_DATE

	};

	public ItemController(Context context) {
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

	public void insertItem(Item item) {

		int c=0;
		String qry="select count(*) from MastProduct where webcode="+item.getItem_id();
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
			values.put(DatabaseConnection.COLUMN_WEB_CODE, item.getItem_id());
		}
		values.put(DatabaseConnection.COLUMN_NAME, item.getItem_name().replaceAll("'",""));
		values.put(DatabaseConnection.COLUMN_UNIT, item.getUnit());
		values.put(DatabaseConnection.COLUMN_STDPACK, item.getStdpack());
		values.put(DatabaseConnection.COLUMN_MRP, item.getMRP());
		values.put(DatabaseConnection.COLUMN_DP, item.getDP());
		values.put(DatabaseConnection.COLUMN_RP, item.getRP());
		values.put(DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE,item.getProductGroup_Id());
		values.put(DatabaseConnection.COLUMN_ITEM_CODE, item.getItemcode());
		values.put(DatabaseConnection.COLUMN_SEGMENT, item.getSegmentid());
		values.put(DatabaseConnection.COLUMN_CLASS_ID, item.getClassid());
		values.put(DatabaseConnection.COLUMN_PRICE_GROUP, item.getPricegroup());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, item.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, item.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,
				item.getCreatedDate());
		long id=0;
		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_PRODUCTMAST,
							values, "webcode='" + item.getItem_id() + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_PRODUCTMAST, null, values);
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
//			writeLog("error in id="+party.getParty_id());
//			writeLog("\nerror in id="+constraintException);

		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
//			writeLog("1 error in id="+party.getParty_id());
//			writeLog("\n2.error in id="+e);
		}
item=null;cursor=null;values=null;
	}




	public void insertSubIndustry(String Itid,
							   String Itnm,String SubItnm,String MS
	)
	{

		int c=0;
		String qry="select count(*) from SubIndustrymast where webcode="+Itid;
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
			values.put(DatabaseConnection.COLUMN_WEB_CODE, Itid);
		}
		values.put(DatabaseConnection.COLUMN_NAME, SubItnm.replaceAll("'",""));
		values.put(DatabaseConnection.COLUMN_INDUSTRY_NAME, Itnm.replaceAll("'",""));

		values.put(DatabaseConnection.COLUMN_TIMESTAMP,
				MS);

		long id=0;
		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_SUBINDUSTRYMAST,
							values, "webcode='" + Itid + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_SUBINDUSTRYMAST, null, values);
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
//			writeLog("error in id="+party.getParty_id());
//			writeLog("\nerror in id="+constraintException);

		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
//			writeLog("1 error in id="+party.getParty_id());
//			writeLog("\n2.error in id="+e);
		}
		cursor=null;values=null;
	}






	public void insertIndustry(String Itid,
							   String Itnm,String MS
	)
	{

		int c=0;
		String qry="select count(*) from Industrymast where webcode="+Itid;
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
			values.put(DatabaseConnection.COLUMN_WEB_CODE, Itid);
		}
		values.put(DatabaseConnection.COLUMN_NAME, Itnm.replaceAll("'",""));


		values.put(DatabaseConnection.COLUMN_TIMESTAMP,
				MS);

		long id=0;
		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_INDUSTRYMAST,
							values, "webcode='" + Itid + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_INDUSTRYMAST, null, values);
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
//			writeLog("error in id="+party.getParty_id());
//			writeLog("\nerror in id="+constraintException);

		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
//			writeLog("1 error in id="+party.getParty_id());
//			writeLog("\n2.error in id="+e);
		}
		cursor=null;values=null;
	}


	public void insertItem(
			String Itid,
			String Itnm,String MS
	)
	{

		int c=0;
		String qry="select count(*) from MastProduct where webcode="+Itid;
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
			values.put(DatabaseConnection.COLUMN_WEB_CODE, Itid);
		}
		values.put(DatabaseConnection.COLUMN_NAME, Itnm.replaceAll("'",""));


		values.put(DatabaseConnection.COLUMN_TIMESTAMP,
				MS);

		long id=0;
		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_PRODUCTMAST,
							values, "webcode='" + Itid + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_PRODUCTMAST, null, values);
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
//			writeLog("error in id="+party.getParty_id());
//			writeLog("\nerror in id="+constraintException);

		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
//			writeLog("1 error in id="+party.getParty_id());
//			writeLog("\n2.error in id="+e);
		}
		cursor=null;values=null;
	}




	public void insertItem(
			String SyncId,String Clid,String Uid,String DM,String DP,String Itid,
			String Itnm,String Itcd,String MRP,String Pg,String RP,String SegId,
			String StdPk,String Unit,String MS,String active
	                       )
	{

		int c=0;
		String qry="select count(*) from MastProduct where webcode="+Itid;
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
			values.put(DatabaseConnection.COLUMN_WEB_CODE, Itid);
		}
		values.put(DatabaseConnection.COLUMN_NAME, Itnm.replaceAll("'",""));
		values.put(DatabaseConnection.COLUMN_UNIT, Unit);
		values.put(DatabaseConnection.COLUMN_STDPACK, StdPk);
		values.put(DatabaseConnection.COLUMN_MRP, MRP);
		values.put(DatabaseConnection.COLUMN_DP, DP);
		values.put(DatabaseConnection.COLUMN_RP, RP);
		values.put(DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE,Uid);
		values.put(DatabaseConnection.COLUMN_ITEM_CODE, Itcd);
		values.put(DatabaseConnection.COLUMN_SEGMENT, SegId);
		values.put(DatabaseConnection.COLUMN_CLASS_ID, Clid);
		values.put(DatabaseConnection.COLUMN_PRICE_GROUP, Pg);
		values.put(DatabaseConnection.COLUMN_SYNC_ID, SyncId);
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,
				MS);
		values.put(DatabaseConnection.COLUMN_ACTIVE, active);
		long id=0;
		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_PRODUCTMAST,
							values, "webcode='" + Itid + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_PRODUCTMAST, null, values);
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
//			writeLog("error in id="+party.getParty_id());
//			writeLog("\nerror in id="+constraintException);

		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
//			writeLog("1 error in id="+party.getParty_id());
//			writeLog("\n2.error in id="+e);
		}
		cursor=null;values=null;
	}


	public ArrayList<Item> getProductList(String groupId, String subGroupId) {
		// String
		// query="select name from partymast where beat_code='"+beatId+"' and route_code='"+routeId+"'";
		Cursor cursor = database.query(DatabaseConnection.TABLE_PRODUCTMAST,allColumns,
				DatabaseConnection.COLUMN_PRODUCT_GROUP_CODE + "='"+ groupId + "' AND "
						+ DatabaseConnection.COLUMN_CLASS_ID + "='"
						+ subGroupId + "'", null, null, null,
				DatabaseConnection.COLUMN_NAME.toUpperCase());
		ArrayList<Item> items = new ArrayList<Item>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Item item = new Item();
				item.setItem_id(cursor.getString(0));
				item.setItem_name(cursor.getString(1));
				item.setSegmentid(cursor.getString(2));
				item.setProductGroup_Id(cursor.getString(3));
				item.setPricegroup(cursor.getString(5));
				item.setUnit(cursor.getString(7));
				item.setDP(cursor.getString(8));
				item.setMRP(cursor.getString(9));
				item.setRP(cursor.getString(10));
				items.add(item);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return items;
	}

	public ArrayList<Item> getProductList(String groupId, String subGroupId,
										  String whereInString) {
		// String
		// q="select * from productmast where  (product_group_code='ZIRMEK' and product_subgroup_code='ZIRMEK') "
		// +
		// "  INTERSECT select * from productmast where webcode not in  ('IF19185','IF00004','IF00170','IF00180','IF00190','IF00200','IF00230','IF00240')"
		String query = "select * from productmast where product_group_code='"
				+ groupId + "' and product_subgroup_code='" + subGroupId
				+ "'  INTERSECT"
				+ " select * from productmast where webcode not in ("
				+ whereInString + ")";
		System.out.println("query=" + query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Item> items = new ArrayList<Item>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Item item = new Item();
				item.setItem_id(cursor.getString(0));
				item.setItem_name(cursor.getString(1));
				item.setSegmentid(cursor.getString(2));
				item.setProductGroup_Id(cursor.getString(3));
				item.setPricegroup(cursor.getString(5));
				item.setUnit(cursor.getString(7));
				item.setDP(cursor.getString(8));
				item.setMRP(cursor.getString(9));
				item.setRP(cursor.getString(10));
				items.add(item);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return items;
	}


	public ArrayList<Item> getItemStockProductListById(String whereInString) {
		String query = "SELECT  * FROM MastDistItemTemplate MD LEFT JOIN MASTPRODUCT MP ON MD.ITEM_ID=MP.WEBCODE WHERE MD.WEBCODE="+whereInString;
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Item> items = new ArrayList<Item>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Item item = new Item();
				item.setItem_id(cursor.getString(cursor.getColumnIndex("item_id")));
				item.setItem_name(cursor.getString(cursor.getColumnIndex("name")));
				item.setUnit(cursor.getString(cursor.getColumnIndex("Unit")));
				item.setStdpack(cursor.getString(cursor.getColumnIndex("stdpack")));
				item.setMRP(cursor.getString(cursor.getColumnIndex("MRP")));
				item.setDP(cursor.getString(cursor.getColumnIndex("DP")));
				item.setRP(cursor.getString(cursor.getColumnIndex("Rp")));
				item.setProductGroup_Id(cursor.getString(cursor.getColumnIndex("product_group_code")));
				item.setSegmentid(cursor.getString(cursor.getColumnIndex("segmentId")));
				item.setPricegroup(cursor.getString(cursor.getColumnIndex("pricegroup")));
				String syncId = cursor.getString(cursor.getColumnIndex("sync_id"));
				if(syncId != null)
				{
					item.setSync_id(syncId);
				}
				else
				{
					item.setSync_id("");
				}


				item.setQty("");
				item.setAmount("");
				items.add(item);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return items;
	}


	public ArrayList<Item> getProductListByIdNew(String whereInString) {
		String query = "select * from MastProduct where webcode in"+ whereInString+"order by name";
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Item> items = new ArrayList<Item>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Item item = new Item();
				item.setItem_id(cursor.getString(0));
				item.setItem_name(cursor.getString(1));
				item.setUnit(cursor.getString(2));
//				item.setCases(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_CASES)));
				item.setStdpack(cursor.getString(3));
				item.setMRP(cursor.getString(4));
				item.setDP(cursor.getString(5));
				item.setRP(cursor.getString(6));
				item.setProductGroup_Id(cursor.getString(7));
				item.setSegmentid(cursor.getString(9));
				item.setPricegroup(cursor.getString(11));
				item.setOrd1AndroidId("");
				item.setQty("");
				item.setAmount("");
				item.setBatchNo("");
				item.setMfd("");
				items.add(item);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return items;
	}


	public ArrayList<Item> getAllOrderList(String date, String partyCode)
	{
		String query="";
		Cursor cursor=null;


		ArrayList<Item> items = new ArrayList<Item>();
		query = "Select * From TransOrder1 om where om.party_code='"+partyCode+"' and om.visit_date='"+date+"'";
		try {
			cursor = database.rawQuery(query, null);
			if (cursor.getCount() > 0) {
				query = "select " +
						"distinct( a.product_code)," +
						"b.name," +
						"a.qty as unit," +
						"b.stdpack," +
						"a.rate," +
						"a.amount as amt," +
						"a._case as cases," +
						"a.Android_id, " +
						"a.ord1Android_id," +
						"c.remark" +
						" from  TransOrder" +
						" c left join TransOrder1 a on a.android_id=c.android_id left join MastProduct b on  a.product_code=b.webcode where a.party_code='"+partyCode+"' and a.visit_date='"+date+"'";

				cursor = database.rawQuery(query, null);

				if (cursor.moveToFirst()) {
					String prev_item_id = "";
					while (!(cursor.isAfterLast())) {
						Item orderTransaction = new Item();
						orderTransaction.setItem_id(cursor.getString(0));
						orderTransaction.setItem_name(cursor.getString(1));
						orderTransaction.setUnit(cursor.getString(2));
						orderTransaction.setStdpack(cursor.getString(3));
						orderTransaction.setMRP(cursor.getString(4));
						orderTransaction.setAmount(cursor.getString(5));
						orderTransaction.setCases(cursor.getString(6));
						orderTransaction.setOrd1AndroidId(cursor.getString(8));
						orderTransaction.setAndroidId(cursor.getString(7));

						orderTransaction.setVisitDate(date);
						orderTransaction.setPartyId(partyCode);

					/*orderTransaction.setQty(cursor.getString(5));
					orderTransaction.setRate(cursor.getString(6));*/

						//orderTransaction.setAndPartyId(cursor.getString(8));


						boolean isAdded = false;
						if(items.size()>0)
						{

							for(int i = 0; i < items.size();i++)
							{
								if(items.get(i).getItem_id().equalsIgnoreCase(prev_item_id))
								{
									isAdded = true;
								}
							}


						}
						if(!isAdded)
						{
							items.add(orderTransaction);

						}
						else
						{

						}
						/*if(!prev_item_id.equalsIgnoreCase(cursor.getString(0)))
						{

						}*/
						cursor.moveToNext();
					}
				}
			} else {
				query = "select a.item_id," +
						"b.name," +
						"0.00 as unit," +
						"b.stdpack," +
						"b.mrp," +
						"0.00 as amt," +
						"0.0 as cases," +
						"0 as ord1id,\n" +
						"' 'as remarks  from History a,MastProduct b where a.item_id=b.webcode and a.code='"+partyCode+"'";

				cursor = database.rawQuery(query, null);
				ArrayList<Item> items1 = new ArrayList<Item>();
				if (cursor.moveToFirst()) {
					while (!(cursor.isAfterLast())) {
						Item orderTransaction = new Item();

						orderTransaction.setItem_id(cursor.getString(0));
						orderTransaction.setItem_name(cursor.getString(1));
						orderTransaction.setUnit(cursor.getString(2));
						orderTransaction.setStdpack(cursor.getString(3));
						orderTransaction.setMRP(cursor.getString(4));
						orderTransaction.setAmount(cursor.getString(5));
						orderTransaction.setCases(cursor.getString(6));
					/*	orderTransaction.setOrd1AndroidId(cursor.getString(8));
						orderTransaction.setAndroidId(cursor.getString(7));*/

						orderTransaction.setVisitDate(date);
						orderTransaction.setPartyId(partyCode);
						items.add(orderTransaction);
						cursor.moveToNext();
					}
				}
			}

			cursor.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return items;
	}

	public ArrayList<Item> getBookOrderedList(String whereInString) {
		String query = "select * from MastProduct where webcode in "+ whereInString+" order by name";
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Item> items = new ArrayList<Item>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Item item = new Item();
				item.setItem_id(cursor.getString(0));
				item.setItem_name(cursor.getString(1));
				item.setUnit(cursor.getString(2));
				item.setStdpack(cursor.getString(3));
				item.setMRP(cursor.getString(4));
				item.setDP(cursor.getString(5));
				item.setRP(cursor.getString(6));
				item.setProductGroup_Id(cursor.getString(7));
				item.setSegmentid(cursor.getString(9));
				item.setPricegroup(cursor.getString(11));
				String syncId = cursor.getString(12);
				if(syncId != null)
				{
					item.setSync_id(syncId);
				}
				else
				{
					item.setSync_id("");
				}


				item.setQty("");
				item.setAmount("");
				items.add(item);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return items;
	}


	public ArrayList<Item> getProductListById(String whereInString, String vDate)
	{
		ArrayList<Item> items = new ArrayList<Item>();
//		String query = "select * from MastProduct where webcode in "+ whereInString+" order by name";
		String query = "Select Count(*) From TransDistStock mt where  mt.Distid="+whereInString;
		Cursor cursor = database.rawQuery(query, null);
		if(cursor.getCount() == 0)
		{
			query = "select mi.webcode,mi.name,mi.MRP,mi.dp,mi.rp,mi.sync_id,mi.pricegroup,mi.product_group_code,mi.segmentId,mi.ItemCode,mi.StdPack as UnitFactor,mi.unit as uom,.0 as StockQty," +
					"0 as Cases,0 as  unit,'' as Android_id from MastDistItemTemplate dit inner join MastProduct mi on dit.Item_Id=mi.webcode Where dit.code="+whereInString+" order by mi.name";

			cursor = database.rawQuery(query, null);

			if (cursor.moveToFirst()) {
				while (!(cursor.isAfterLast())) {
					Item item = new Item();
					item.setItem_id(cursor.getString(cursor.getColumnIndex("webcode")));
					item.setItem_name(cursor.getString(cursor.getColumnIndex("name")));
					item.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
					item.setStdpack(cursor.getString(cursor.getColumnIndex("UnitFactor")));
					item.setMRP(cursor.getString(cursor.getColumnIndex("MRP")));
					item.setDP(cursor.getString(cursor.getColumnIndex("dp")));
					item.setRP(cursor.getString(cursor.getColumnIndex("rp")));
					item.setProductGroup_Id(cursor.getString(cursor.getColumnIndex("product_group_code")));
					item.setSegmentid(cursor.getString(cursor.getColumnIndex("segmentId")));
					item.setPricegroup(cursor.getString(cursor.getColumnIndex("pricegroup")));
					String syncId = cursor.getString(cursor.getColumnIndex("sync_id"));
					item.setCases(cursor.getString(13));
					item.setUnit(cursor.getString(14));
					item.setUnit(cursor.getString(15));
					if (syncId != null) {
						item.setSync_id(syncId);
					} else {
						item.setSync_id("");
					}
					item.setQty("");
					item.setAmount("");
					items.add(item);
					cursor.moveToNext();
				}
			}
		}
		else {
			query = String.format("select distinct webcode,name,MRP,dp,rp,sync_id,pricegroup,product_group_code,segmentId," +
					"ItemCode, UnitFactor,uom,StockQty,_case, unit,Android_id" +
					" FROM (select distinct mi.webcode,mi.name,mi.MRP,mi.dp,mi.rp,mi.sync_id,mi.pricegroup,mi.product_group_code,mi.segmentId,mi.ItemCode,mi.StdPack as UnitFactor,mi.unit as uom," +
					"ifnull(mt.Qty,0) as StockQty,mt._case,mt.unit,mt.Android_id from TransDistStock mt left join MastDistItemTemplate " +
					"dit on mt.product_code=dit.Item_Id inner join MastProduct  mi on dit.Item_Id=mi.webcode where mt." +
					"Visit_Date='%s'" +
					" and	" +
					"mt.Distid=%s " +
					"union	select mi.webcode,mi.name,mi.MRP,mi.dp,mi.rp,mi.sync_id,mi.pricegroup,mi.product_group_code,mi.segmentId,mi.ItemCode,mi.StdPack as UnitFactor,mi.unit as uom,0 as StockQty," +
					"	0 as Cases,0 as  unit,'' as Android_id from MastDistItemTemplate dit inner join MastProduct mi on dit.Item_Id=mi.webcode" +
					" Where dit.code=%s " +
					" and mi.webcode not in(select mi.webcode from TransDistStock mt left join	MastDistItemTemplate" +
					" dit on mt.product_code=dit.Item_Id inner join MastProduct mi on dit.Item_Id=mi.webcode" +
					"	where " +
					"mt.Visit_Date='%s' " +
					"and  mt.Distid=%s) order by mi.name ) a",
					vDate,
					whereInString,
					whereInString,
					vDate,
					whereInString
					);
			cursor = database.rawQuery(query, null);

			if (cursor.moveToFirst()) {
				while (!(cursor.isAfterLast())) {
					Item item = new Item();
					item.setItem_id(cursor.getString(cursor.getColumnIndex("webcode")));
					item.setItem_name(cursor.getString(cursor.getColumnIndex("name")));
					item.setUnit(cursor.getString(cursor.getColumnIndex("unit")));
					item.setStdpack(cursor.getString(cursor.getColumnIndex("UnitFactor")));
					item.setMRP(cursor.getString(cursor.getColumnIndex("MRP")));
					item.setDP(cursor.getString(cursor.getColumnIndex("dp")));
					item.setRP(cursor.getString(cursor.getColumnIndex("rp")));
					item.setProductGroup_Id(cursor.getString(cursor.getColumnIndex("product_group_code")));
					item.setSegmentid(cursor.getString(cursor.getColumnIndex("segmentId")));
					item.setPricegroup(cursor.getString(cursor.getColumnIndex("pricegroup")));
					String syncId = cursor.getString(cursor.getColumnIndex("sync_id"));
					if (syncId != null) {
						item.setSync_id(syncId);
					} else {
						item.setSync_id("");
					}
					item.setQty(cursor.getString(cursor.getColumnIndex("StockQty")));
					item.setCases(cursor.getString(13));
					item.setUnit(cursor.getString(14));
					item.setAndroidId(cursor.getString(15));
					item.setAmount("");
					items.add(item);
					cursor.moveToNext();
				}
			}

		}
		/*} else {
			System.out.println("No records found");
		}*/
		cursor.close();
		return items;
	}
	public ArrayList<Owner> getItemList(String productGroupCode) {
//		str=str.replaceAll("'","\'");
		ArrayList<Owner> ItemList = new ArrayList<Owner>();
		try {
			Owner owner=new Owner();
			owner.setId("0");
			owner.setName("--Select--");
			ItemList.add(owner);
		} catch (Exception e) {}
		String qry="select webcode,name from MastProduct where product_group_code='"+productGroupCode+"' order by name desc";
		System.out.println(qry);
		Cursor cursor = database.rawQuery(qry, null);
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Owner owner=new Owner();
				owner.setId(cursor.getString(0));
				owner.setName(cursor.getString(1));
				ItemList.add(owner);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return ItemList;

	}
	public ArrayList<String> getInputWorldOrder(String str) {
//		str=str.replaceAll("'","\'");
		ArrayList<String> itemArray = new ArrayList<String>();
		//String qry = "SELECT ' ' || MastProduct.name || ' ' AS iname,webcode FROM MastProduct WHERE '(' || MastProduct.sync_id || ') ' || MastProduct.name || ' (' || MastProduct.itemcode || ')' LIKE '%"
		String qry = "SELECT MastProduct.name || ' (' || MastProduct.itemcode || ')' AS iname,webcode FROM MastProduct WHERE MastProduct.name || ' (' || MastProduct.itemcode || ')' LIKE '%"
				+ str + "%'";
		
//		String qry = "SELECT  MastProduct.name AS iname,webcode FROM MastProduct WHERE MastProduct.name LIKE '%"+ str + "%'";
		System.out.println(qry);
		Cursor cursor = database.rawQuery(qry, null);
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				itemArray.add(cursor.getString(0));
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return itemArray;

	}

	public ArrayList<String> getInputWorldOrder(String str, String whereInString) {
//		str=str.replaceAll("'","\'");
		ArrayList<String> itemArray = new ArrayList<String>();
		Cursor cursor=null;
		 String qry = "SELECT MastProduct.name || ' (' || MastProduct.itemcode || ')' AS iname,webcode FROM MastProduct WHERE MastProduct.name || ' (' || MastProduct.itemcode || ')' LIKE '%"+ str + "%'";
		//INTERSECT"
//					+ " SELECT MastProduct.name || ' (' || MastProduct.itemcode || ')' AS iname,webcode FROM MastProduct where webcode not in"
//					+ whereInString;


//		String qry = "SELECT MastProduct.name || ' (' || MastProduct.itemcode || ')' AS iname,webcode FROM MastProduct WHERE MastProduct.name LIKE '%"+ str +"%' or  MastProduct.itemcode LIKE '%"+ str + "%'  INTERSECT"
//				+ " SELECT MastProduct.name || ' (' || MastProduct.itemcode || ')' AS iname,webcode FROM MastProduct where webcode not in"
//				+ whereInString;

//		String qry = "SELECT MastProduct.name || ' (' || MastProduct.itemcode || ')' AS iname,webcode FROM MastProduct WHERE MastProduct.name LIKE '%"+ str +"%' or  MastProduct.itemcode LIKE '%"+ str + "%'";


		
		System.out.println(qry);
		try{
		 cursor = database.rawQuery(qry, null);
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				itemArray.add(cursor.getString(0));
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
		
		cursor.close();
		return itemArray;

	}
	
	public Item getItemDetail(String str) {
		str=str.replaceAll("'","\'");
		Item item = new Item();
//		String qry = "SELECT * FROM MastProduct  WHERE ' ' || MastProduct.name || ' (' || MastProduct.itemcode || ')' ='"
		String qry = "SELECT * FROM MastProduct  WHERE MastProduct.name || ' (' || MastProduct.itemcode || ')' ='"
				+ str + "'  ";
		
//		String qry = "SELECT * FROM MastProduct  WHERE MastProduct.name='"+ str + "' ";
		Cursor cursor = database.rawQuery(qry, null);
		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			item.setItem_id(cursor.getString(0));
			item.setItem_Name(cursor.getString(1));
			item.setUnit(cursor.getString(2));
			item.setStdpack(cursor.getString(3));
			item.setMRP(cursor.getString(4));
			item.setDP(cursor.getString(5));
			item.setRP(cursor.getString(6));
			item.setProductGroup_Id(cursor.getString(7));
			item.setItemcode(cursor.getString(8));
			item.setSegmentid(cursor.getString(9));
			item.setClassid(cursor.getString(10));
			item.setPricegroup(cursor.getString(11));
			//item.setSync_id(cursor.getString(12));
			item.setActive(cursor.getString(12));
			item.setCreatedDate(cursor.getString(13));

			cursor.moveToNext();
		} else {
			System.out.println("No records found");
		}
		cursor.close();

		return item;

	}
	
	
	
	

	// public void writeLog(String msg) {
	// Calendar calendar1=Calendar.getInstance();
	// try{
	// System.out.println("adsfda====sfsfsd");
	// myFile =new File("/sdcard/myastraldb.txt");
	// // myFile = new
	// File(Environment.getDataDirectory()+"/fftlog/myuploadfile.txt");
	// if(!myFile.exists())
	// {
	// myFile.createNewFile();
	// }
	// fOut = new FileOutputStream(myFile,true);
	// myOutWriter = new OutputStreamWriter(fOut);
	// myOutWriter.append(msg+"  "+dateFormat1.format(calendar1.getTime())+" \n");
	// myOutWriter.close();
	// fOut.close();
	// }
	// catch (IOException e) {
	// System.out.println("adsf%%%%%%%%%%Sdasfsfsd");
	// // writeLog("IO exception");
	// e.printStackTrace();
	// }
	// }
}
