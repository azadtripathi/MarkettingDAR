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
import com.dm.model.OrderTransaction;

import java.util.ArrayList;

public class OrderTransactionController {
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = { DatabaseConnection.COLUMN_DOC_ID,
			DatabaseConnection.COLUMN_DOC_ID_SNO,
			DatabaseConnection.COLUMN_WEB_DOC_ID,
			DatabaseConnection.COLUMN_WEB_DOC_ID_SNO,
			DatabaseConnection.COLUMN_DATE, DatabaseConnection.COLUMN_TIME,
			DatabaseConnection.COLUMN_PARTY_CODE,
			DatabaseConnection.COLUMN_SREP_CODE,
			DatabaseConnection.COLUMN_AREA_CODE,
			DatabaseConnection.COLUMN_BEAT_CODE,
			DatabaseConnection.COLUMN_PRODUCT_CODE,
			DatabaseConnection.COLUMN_QTY, DatabaseConnection.COLUMN_RATE,
			DatabaseConnection.COLUMN_REMARK, DatabaseConnection.COLUMN_TYPE };

	public OrderTransactionController(Context context) {
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

	public void insertorderTransaction(OrderTransaction orderTransaction) {
		System.out.println("inserted");
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_DOC_ID,
				orderTransaction.getDoc_id());
		values.put(DatabaseConnection.COLUMN_DOC_ID_SNO,
				orderTransaction.getDoc_id_sno());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID,
				orderTransaction.getWeb_doc_id());
		values.put(DatabaseConnection.COLUMN_WEB_DOC_ID_SNO,
				orderTransaction.getWeb_doc_id_sno());
		values.put(DatabaseConnection.COLUMN_DATE, orderTransaction.getDate());
		values.put(DatabaseConnection.COLUMN_TIME, orderTransaction.getTime());
		values.put(DatabaseConnection.COLUMN_PARTY_CODE,
				orderTransaction.getParty_code());
		values.put(DatabaseConnection.COLUMN_SREP_CODE,
				orderTransaction.getSrep_code());
		values.put(DatabaseConnection.COLUMN_AREA_CODE,
				orderTransaction.getArea_code());
		values.put(DatabaseConnection.COLUMN_BEAT_CODE,
				orderTransaction.getBeat_code());
		values.put(DatabaseConnection.COLUMN_REMARK,
				orderTransaction.getRemark());
		values.put(DatabaseConnection.COLUMN_PRODUCT_CODE,
				orderTransaction.getProduct_code());
		values.put(DatabaseConnection.COLUMN_QTY, orderTransaction.getQty());
		values.put(DatabaseConnection.COLUMN_RATE, orderTransaction.getRate());
		values.put(DatabaseConnection.COLUMN_AMOUNT,
				orderTransaction.getAmount());
		values.put(DatabaseConnection.COLUMN_TYPE, "Order");
		if (orderTransaction.isNewOrder()) {

			values.put(DatabaseConnection.COLUMN_isMainUPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_isSubUPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction
					.ToConvertDateTime(orderTransaction.getDate(), "00", "00",
							"00"));

		} else {
			values.put(DatabaseConnection.COLUMN_isMainUPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_isSubUPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP,
					orderTransaction.getTimestamp());
		}

		try {
			long id = database.insert(DatabaseConnection.TABLE_ORDER, null,
					values);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}

	public void deleteorderTransactionRow() {
		System.out.println("CDB Data deleted");
		database.delete(DatabaseConnection.TABLE_ORDER, null, null);
		System.out.println("orderTransaction deleted");
	}

	public void deleteorderTransactionRowByDatePartyCode(String date,
			String partyCode) {
		System.out.println("CDB Data deleted");
		int i = database.delete(DatabaseConnection.TABLE_ORDER, "date='" + date
				+ "' AND party_code='" + partyCode + "'", null);
		System.out
				.println("orderTransaction deleted for date" + " result=" + i);
	}

	public long getDocId(String date, String partyCode) {
		long doc_id = 0;
		String query = "select distinct ifnull(doc_id,0) from OrderTran where date='"
				+ date
				+ "' and "
				+ DatabaseConnection.COLUMN_PARTY_CODE
				+ "='"
				+ partyCode + "'";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);

		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			doc_id = Long.valueOf(cursor.getString(0));
			System.out.println("max id found  " + doc_id);

		} else {
			System.out.println("no max id found");
		}
		System.out.println("no max id found" + doc_id);
		cursor.close();
		return doc_id;
	}

	public long getMaxDocId() {
		long doc_id = 0;
		String query = "select ifnull(max(cast(doc_id as integer)),0)+1 as MyColumn from OrderTran";
		Cursor cursor = database.rawQuery(query, null);

		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			doc_id = cursor.getLong(0);
			System.out.println("max docid found " + doc_id);

		} else {
			System.out.println("no max id found");
		}
		cursor.close();
		return doc_id;
	}

	public String getPartyCode(String webCode) {
		String doc_id = null;
		String query = "select ifnull(code,'') from partymast where webcode='"
				+ webCode + "'";
		Cursor cursor = database.rawQuery(query, null);

		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			doc_id = cursor.getString(0);
			System.out.println("max id found " + doc_id);

		} else {
			System.out.println("no max id found");
		}
		System.out.println("no max id found" + doc_id);
		cursor.close();
		return doc_id;
	}

	public ArrayList<String> getHistoryList() {
		String query = "select distinct party_code from ordertran";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<String> partycode = new ArrayList<String>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				partycode.add(cursor.getString(0));

				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return partycode;
	}

	public ArrayList<OrderTransaction> getUploadMainList() {
		String query = "select max(ot.doc_id) as doc_id,max(ot.date) as date,max(p.webcode) as webcode,max(ot.srep_code) as srep_code,max(ot.area_code) as area_code,max(ot.beat_code) as beat_code, "
				+ " max(ot.route_code) as route_code,sum(ot.qty*ot.rate) as total from ordertran ot "
				+ " left join visitl1 vl1 on ot.date=vl1.date "
				+ " left join partymast p on ot.party_code=p.code "
				+ " where vl1.dsr_lock='1' and isSubUpload='0' "
				+ " group by ot.doc_id ";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<OrderTransaction> orderTransactions = new ArrayList<OrderTransaction>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				OrderTransaction orderTransaction = new OrderTransaction();
				orderTransaction.setDoc_id(cursor.getString(0));
				orderTransaction.setDate(cursor.getString(1));
				orderTransaction.setParty_code(cursor.getString(2));
				orderTransaction.setSrep_code(cursor.getString(3));
				orderTransaction.setArea_code(cursor.getString(4));
				orderTransaction.setBeat_code(cursor.getString(5));
				orderTransaction.setRoute_code(cursor.getString(6));
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

	public ArrayList<OrderTransaction> getUploadSubList(String doc_id) {

		String query = "select ot.doc_id,ot.date,p.webcode,ot.srep_code,ot.area_code,ot.beat_code,ot.route_code,product_code,qty,rate,doc_id_sno from ordertran ot "
				+ " left join visitl1 vl1 on ot.date=vl1.date "
				+ " left join partymast p on ot.party_code=p.code "
				+ " where vl1.dsr_lock='1' and isSubUpload='0' and ot.doc_id ='"
				+ doc_id + "'  ";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<OrderTransaction> orderTransactions = new ArrayList<OrderTransaction>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				OrderTransaction orderTransaction = new OrderTransaction();
				orderTransaction.setDoc_id(cursor.getString(0));
				orderTransaction.setDate(cursor.getString(1));
				orderTransaction.setParty_code(cursor.getString(2));
				orderTransaction.setSrep_code(cursor.getString(3));
				orderTransaction.setArea_code(cursor.getString(4));
				orderTransaction.setBeat_code(cursor.getString(5));
				orderTransaction.setRoute_code(cursor.getString(6));
				orderTransaction.setProduct_code(cursor.getString(7));
				orderTransaction.setQty(cursor.getString(8));
				orderTransaction.setRate(cursor.getString(9));
				orderTransaction.setDoc_id_sno(cursor.getInt(10));
				orderTransactions.add(orderTransaction);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}

	public void insertItems(String code) {
		String q = "insert into history select party_code,pm.webcode,product_code as item_id,1 as isUpload ,rate as rate from orderTran "
				+ " left join partymast pm on orderTran.party_code=pm.code "
				+ " left join productmast pr on orderTran.product_code=pr.webcode "
				+ " where party_code ='" + code + "' " + " group by item_id ";
		System.out.println(q);
		database.execSQL(q);

	}

	public void updateSubOrder(String doc_id, int doc_id_sno) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_isSubUPLOAD, "1");
		try {
			long id = database.update(DatabaseConnection.TABLE_ORDER, values,
					"doc_id='" + doc_id + "' and doc_id_sno=" + doc_id_sno,
					null);
			System.out.println("row=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}

	public void updateMainOrder(String doc_id) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_isMainUPLOAD, "1");
		values.put(DatabaseConnection.COLUMN_isSubUPLOAD, "1");
		try {
			long id = database.update(DatabaseConnection.TABLE_ORDER, values,
					"doc_id='" + doc_id + "'", null);
			System.out.println("row=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}

	public void resetMainOrder(String doc_id) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_isMainUPLOAD, "0");
		values.put(DatabaseConnection.COLUMN_isSubUPLOAD, "0");
		try {
			long id = database.update(DatabaseConnection.TABLE_ORDER, values,
					"doc_id='" + doc_id + "'", null);
			System.out.println("row=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}

	public ArrayList<OrderTransaction> getFilteredData(int numberValue,
													   String areaCode, String beatCode) {
		Cursor cursor = null;
		String qurey1 = "select ifnull(max(p.name),'') as partyName,ifnull(max(p.address1),'') as address1,ifnull(max(p.address2),'') as address2,ifnull(max(i.name),'') as industry_code,ifnull(max(p.mobile),'') as mobile,p.potential "
				+ " from ordertran ot "
				+ " left join partymast p on ot.party_code=p.code "
				+ " left join industrymast i on p.industry_code=i.webcode "
				+ " where  (ot.qty*ot.rate)>"
				+ numberValue
				+ ""
				+ " group by ot.party_code order by p.potential desc";
		String qurey2 = "select ifnull(max(p.name),'') as partyName,ifnull(max(p.address1),'') as address1,ifnull(max(p.address2),'') as address2,ifnull(max(i.name),'') as industry_code,ifnull(max(p.mobile),'') as mobile,p.potential "
				+ " from ordertran ot "
				+ " left join partymast p on ot.party_code=p.code "
				+ " left join industrymast i on p.industry_code=i.webcode "
				+ " where (ot.qty*ot.rate)>"
				+ numberValue
				+ "  and ot.area_code='"
				+ areaCode
				+ "'"
				+ " group by ot.party_code order by p.potential desc";
		String qurey3 = "select ifnull(max(p.name),'') as partyName,ifnull(max(p.address1),'') as address1,ifnull(max(p.address2),'') as address2,ifnull(max(i.name),'') as industry_code,ifnull(max(p.mobile),'') as mobile,p.potential "
				+ " from ordertran ot "
				+ " left join partymast p on ot.party_code=p.code "
				+ " left join industrymast i on p.industry_code=i.webcode "
				+ " where  (ot.qty*ot.rate)>"
				+ numberValue
				+ "  and ot.area_code='"
				+ areaCode
				+ "' and ot.beat_code='"
				+ beatCode
				+ "'"
				+ " group by ot.party_code order by p.potential desc";
		String qurey4 = "select ifnull(max(p.name),'') as partyName,ifnull(max(p.address1),'') as address1,ifnull(max(p.address2),'') as address2,ifnull(max(i.name),'') as industry_code,ifnull(max(p.mobile),'') as mobile,p.potential "
				+ " from ordertran ot "
				+ " left join partymast p on ot.party_code=p.code "
				+ " left join industrymast i on p.industry_code=i.webcode "
				+ " where ot.area_code='"
				+ areaCode
				+ "'"
				+ " group by ot.party_code order by p.potential desc";
		String qurey5 = "select ifnull(max(p.name),'') as partyName,ifnull(max(p.address1),'') as address1,ifnull(max(p.address2),'') as address2,ifnull(max(i.name),'') as industry_code,ifnull(max(p.mobile),'') as mobile,p.potential "
				+ " from ordertran ot "
				+ " left join partymast p on ot.party_code=p.code "
				+ " left join industrymast i on p.industry_code=i.webcode "
				+ " where ot.area_code='"
				+ areaCode
				+ "' and ot.beat_code='"
				+ beatCode
				+ "'"
				+ " group by ot.party_code order by p.potential desc";
		if (!(numberValue < 0) && areaCode.isEmpty()) {
			cursor = database.rawQuery(qurey1, null);
			System.out.println(qurey1);
		} else if (!(areaCode.isEmpty()) && !(beatCode.isEmpty())
				&& (numberValue < 0)) {
			cursor = database.rawQuery(qurey5, null);
			System.out.println(qurey5);
		} else if (!(areaCode.isEmpty()) && !(numberValue < 0)
				&& beatCode.isEmpty()) {
			cursor = database.rawQuery(qurey2, null);
			System.out.println(qurey2);
		} else if (!(areaCode.isEmpty()) && !(beatCode.isEmpty())
				&& !(numberValue < 0)) {
			cursor = database.rawQuery(qurey3, null);
			System.out.println(qurey3);
		} else if ((numberValue < 0) && !(areaCode.isEmpty())
				&& beatCode.isEmpty()) {
			cursor = database.rawQuery(qurey4, null);
			System.out.println(qurey4);
		}
		ArrayList<OrderTransaction> orderTransactions = new ArrayList<OrderTransaction>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				OrderTransaction orderTransaction = new OrderTransaction();
				orderTransaction.setParty_name(cursor.getString(0));
				orderTransaction.setAddress(cursor.getString(1) + ","
						+ cursor.getString(2));
				orderTransaction.setIndustryType(cursor.getString(3));
				orderTransaction.setMobileNo(cursor.getString(4));
				orderTransaction.setPotentail(cursor.getString(5));
				orderTransactions.add(orderTransaction);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;

	}

	public ArrayList<OrderTransaction> getFilteredDataByAreaBeatItem(
			String areaCode, String beatCode, String groupCode,
			String SubgroupCode, String itemCode) {
		Cursor cursor = null;
		String qurey1 = "select ifnull(max(p.name),'') as partyName,ifnull(max(p.address1),'') as address1,ifnull(max(p.address2),'') as address2,ifnull(max(p.industry_code),'') as industry_code,ifnull(max(p.mobile),'') as mobile "
				+ " from ordertran ot "
				+ " left join partymast p on ot.party_code=p.code "
				+ " left join productmast pm on ot.product_code=pm.webcode "
				+ " where ot.area_code='"
				+ areaCode
				+ "' and ot.beat_code='"
				+ beatCode
				+ "' and  pm.product_group_code='"
				+ groupCode
				+ "' and pm.product_subgroup_code='"
				+ SubgroupCode
				+ "' and ot.product_code='"
				+ itemCode
				+ "' "
				+ " group by ot.party_code order by p.name";
		String qurey2 = "select ifnull(max(p.name),'') as partyName,ifnull(max(p.address1),'') as address1,ifnull(max(p.address2),'') as address2,ifnull(max(p.industry_code),'') as industry_code,ifnull(max(p.mobile),'') as mobile "
				+ " from ordertran ot "
				+ " left join partymast p on ot.party_code=p.code "
				+ " left join productmast pm on ot.product_code=pm.webcode "
				+ " where ot.area_code='"
				+ areaCode
				+ "' and ot.beat_code='"
				+ beatCode
				+ "' and  pm.product_group_code='"
				+ groupCode
				+ "' and pm.product_subgroup_code='"
				+ SubgroupCode
				+ "' "
				+ " group by ot.party_code order by p.name";
		String qurey3 = "select ifnull(max(p.name),'') as partyName,ifnull(max(p.address1),'') as address1,ifnull(max(p.address2),'') as address2,ifnull(max(p.industry_code),'') as industry_code,ifnull(max(p.mobile),'') as mobile "
				+ " from ordertran ot "
				+ " left join partymast p on ot.party_code=p.code "
				+ " left join productmast pm on ot.product_code=pm.webcode "
				+ " where ot.area_code='"
				+ areaCode
				+ "' and ot.beat_code='"
				+ beatCode
				+ "' and  pm.product_group_code='"
				+ groupCode
				+ "' " + " group by ot.party_code order by p.name";
		String qurey4 = "select ifnull(max(p.name),'') as partyName,ifnull(max(p.address1),'') as address1,ifnull(max(p.address2),'') as address2,ifnull(max(p.industry_code),'') as industry_code,ifnull(max(p.mobile),'') as mobile "
				+ " from ordertran ot "
				+ " left join partymast p on ot.party_code=p.code "
				+ " left join productmast pm on ot.product_code=pm.webcode "
				+ " where ot.area_code='"
				+ areaCode
				+ "' and ot.beat_code='"
				+ beatCode + "' " + " group by ot.party_code order by p.name";
		String qurey5 = "select ifnull(max(p.name),'') as partyName,ifnull(max(p.address1),'') as address1,ifnull(max(p.address2),'') as address2,ifnull(max(p.industry_code),'') as industry_code,ifnull(max(p.mobile),'') as mobile "
				+ " from ordertran ot "
				+ " left join partymast p on ot.party_code=p.code "
				+ " left join productmast pm on ot.product_code=pm.webcode "
				+ " where ot.area_code='"
				+ areaCode
				+ "' "
				+ " group by ot.party_code order by p.name";
		if (!areaCode.isEmpty() && !beatCode.isEmpty() && !groupCode.isEmpty()
				&& !SubgroupCode.isEmpty() && !itemCode.isEmpty()) {
			cursor = database.rawQuery(qurey1, null);
			System.out.println(qurey1);
		} else if (!areaCode.isEmpty() && !beatCode.isEmpty()
				&& !groupCode.isEmpty() && !SubgroupCode.isEmpty()
				&& itemCode.isEmpty()) {
			cursor = database.rawQuery(qurey2, null);
			System.out.println(qurey2);
		} else if (!areaCode.isEmpty() && !beatCode.isEmpty()
				&& !groupCode.isEmpty() && SubgroupCode.isEmpty()
				&& itemCode.isEmpty()) {
			cursor = database.rawQuery(qurey3, null);
			System.out.println(qurey3);

		} else if (!areaCode.isEmpty() && !beatCode.isEmpty()
				&& groupCode.isEmpty() && SubgroupCode.isEmpty()
				&& itemCode.isEmpty()) {
			cursor = database.rawQuery(qurey4, null);
			System.out.println(qurey4);
		} else if (!areaCode.isEmpty() && beatCode.isEmpty()
				&& groupCode.isEmpty() && SubgroupCode.isEmpty()
				&& itemCode.isEmpty()) {
			cursor = database.rawQuery(qurey5, null);
			System.out.println(qurey5);
		}
		ArrayList<OrderTransaction> orderTransactions = new ArrayList<OrderTransaction>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				OrderTransaction orderTransaction = new OrderTransaction();
				orderTransaction.setParty_name(cursor.getString(0));
				orderTransaction.setAddress(cursor.getString(1) + ","
						+ cursor.getString(2));
				orderTransaction.setIndustryType(cursor.getString(3));
				orderTransaction.setMobileNo(cursor.getString(4));
				orderTransactions.add(orderTransaction);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;

	}

	public ArrayList<OrderTransaction> getFilteredDataByDateAreaBeat(
			String fromDate, String toDate, String areaCode, String beatCode,
			String partyTypeCode) {
		Cursor cursor = null;
		String qurey1 = "select ifnull(name,''),ifnull(address1,''),ifnull(address2,''),ifnull(industry_code,''),ifnull(mobile,'') from partymast "
				+ "where created_date between '"
				+ DateFunction.ToConvertDateTime(fromDate, "00", "00", "00")
				+ "' and '"
				+ DateFunction.ToConvertDateTime(toDate, "23", "59", "59")
				+ "' and area_code='"
				+ areaCode
				+ "' and beat_code='"
				+ beatCode + "' order by created_date desc";
		String qurey2 = "select ifnull(name,''),ifnull(address1,''),ifnull(address2,''),ifnull(industry_code,''),ifnull(mobile,'') from partymast "
				+ "where created_date between '"
				+ DateFunction.ToConvertDateTime(fromDate, "00", "00", "00")
				+ "' and '"
				+ DateFunction.ToConvertDateTime(toDate, "23", "59", "59")
				+ "' and area_code='"
				+ areaCode
				+ "' order by created_date desc";
		String qurey3 = "select ifnull(name,''),ifnull(address1,''),ifnull(address2,''),ifnull(industry_code,''),ifnull(mobile,'') from partymast "
				+ "where created_date between '"
				+ DateFunction.ToConvertDateTime(fromDate, "00", "00", "00")
				+ "' and '"
				+ DateFunction.ToConvertDateTime(toDate, "23", "59", "59")
				+ "' order by created_date desc";
		String qurey4 = "select ifnull(name,''),ifnull(address1,''),ifnull(address2,''),ifnull(industry_code,''),ifnull(mobile,'') from partymast "
				+ "where created_date between '"
				+ DateFunction.ToConvertDateTime(fromDate, "00", "00", "00")
				+ "' and '"
				+ DateFunction.ToConvertDateTime(toDate, "23", "59", "59")
				+ "' and area_code='"
				+ areaCode
				+ "' and beat_code='"
				+ beatCode
				+ "' and party_type_code='"
				+ partyTypeCode
				+ "' order by created_date desc";
		if (!fromDate.isEmpty() && !toDate.isEmpty() && !areaCode.isEmpty()
				&& !beatCode.isEmpty() && !partyTypeCode.isEmpty()) {
			cursor = database.rawQuery(qurey4, null);
			System.out.println(qurey4 + fromDate + toDate);
		}

		else if (!fromDate.isEmpty() && !toDate.isEmpty()
				&& !areaCode.isEmpty() && !beatCode.isEmpty()
				&& partyTypeCode.isEmpty()) {
			cursor = database.rawQuery(qurey1, null);
			System.out.println(qurey1 + fromDate + toDate);
		} else if (!fromDate.isEmpty() && !toDate.isEmpty()
				&& !areaCode.isEmpty() && beatCode.isEmpty()
				&& partyTypeCode.isEmpty()) {
			cursor = database.rawQuery(qurey2, null);
			System.out.println(qurey2 + fromDate + toDate);
		} else if (!fromDate.isEmpty() && !toDate.isEmpty()
				&& areaCode.isEmpty() && beatCode.isEmpty()
				&& partyTypeCode.isEmpty()) {
			cursor = database.rawQuery(qurey3, null);
			System.out.println(qurey3 + fromDate + toDate);
		}
		ArrayList<OrderTransaction> orderTransactions = new ArrayList<OrderTransaction>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				OrderTransaction orderTransaction = new OrderTransaction();
				orderTransaction.setParty_name(cursor.getString(0));
				orderTransaction.setAddress(cursor.getString(1) + ","
						+ cursor.getString(2));
				orderTransaction.setIndustryType(cursor.getString(3));
				orderTransaction.setMobileNo(cursor.getString(4));
				orderTransactions.add(orderTransaction);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;

	}

	public ArrayList<OrderTransaction> getFilteredDataByBeatMonthlySecondry(
			String beatCode) {
		int currentMonth = DateFunction.getCalculatedMonth("0").get("MONTH");
		int currentMonthYear = DateFunction.getCalculatedMonth("0").get("YEAR");
		int lastMonth = DateFunction.getCalculatedMonth("-1").get("MONTH");
		int lastMonthYear = DateFunction.getCalculatedMonth("-1").get("YEAR");
		int lastToLastMonth = DateFunction.getCalculatedMonth("-2")
				.get("MONTH");
		int lastToLastMonthYear = DateFunction.getCalculatedMonth("-2").get(
				"YEAR");
		System.out.println(currentMonth + " " + lastMonth + " "
				+ lastToLastMonth);
		String qurey = "select  sum(a.totalvaluefirstmonth) as fmv,sum(a.totalvaluesecondmonth) as smv,sum(a.totalvaluethirdmonth) as tmv,a.name from "
				+ " (select ifnull(sum(qty*rate),0)  as totalvaluefirstmonth,  0 as  totalvaluesecondmonth,0 as totalvaluethirdmonth,party_code,pm.name from ordertran ot "
				+ " left join partymast pm on ot.party_code=pm.code "
				+ " where ot.timestamp between '"
				+ currentMonthYear
				+ "-"
				+ (((currentMonth + 1) < 10 ? ("0" + (currentMonth + 1))
						: (currentMonth + 1)))
				+ "-01 00:00:00' and '"
				+ currentMonthYear
				+ "-"
				+ (((currentMonth + 1) < 10 ? ("0" + (currentMonth + 1))
						: (currentMonth + 1)))
				+ "-30 23:59:59' and ot.beat_code='"
				+ beatCode
				+ "' group by ot.party_code union all "
				+ " select 0 as totalvaluefirstmonth,ifnull(sum(qty*rate),0)  as  totalvaluesecondmonth,0 as totalvaluethirdmonth,party_code,pm.name from ordertran ot "
				+ " left join partymast pm on ot.party_code=pm.code "
				+ " where ot.timestamp between '"
				+ lastMonthYear
				+ "-"
				+ (((lastMonth + 1) < 10 ? ("0" + (lastMonth + 1))
						: (lastMonth + 1)))
				+ "-01 00:00:00' and '"
				+ lastMonthYear
				+ "-"
				+ (((lastMonth + 1) < 10 ? ("0" + (lastMonth + 1))
						: (lastMonth + 1)))
				+ "-30 23:59:59' and ot.beat_code='"
				+ beatCode
				+ "' group by ot.party_code union all "
				+ " select 0 as totalvaluefirstmonth,0  as  totalvaluesecondmonth,ifnull(sum(qty*rate),0) as totalvaluethirdmonth,party_code,pm.name from ordertran ot "
				+ " left join partymast pm on ot.party_code=pm.code "
				+ " where ot.timestamp between '"
				+ lastToLastMonthYear
				+ "-"
				+ (((lastToLastMonth + 1) < 10 ? ("0" + (lastToLastMonth + 1))
						: (lastToLastMonth + 1)))
				+ "-01 00:00:00' and '"
				+ lastToLastMonthYear
				+ "-"
				+ (((lastToLastMonth + 1) < 10 ? ("0" + (lastToLastMonth + 1))
						: (lastToLastMonth + 1)))
				+ "-30 23:59:59' and ot.beat_code='"
				+ beatCode
				+ "' group by ot.party_code)a group by a.party_code,a.name order by a.name";
		System.out.println(qurey);
		Cursor cursor = database.rawQuery(qurey, null);
		ArrayList<OrderTransaction> orderTransactions = new ArrayList<OrderTransaction>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				OrderTransaction orderTransaction = new OrderTransaction();
				orderTransaction.setFirstMonthValue(cursor.getInt(0));
				orderTransaction.setSecondMonthValue(cursor.getInt(1));
				orderTransaction.setThirdMonthValue(cursor.getInt(2));
				orderTransaction.setParty_name(cursor.getString(3));
				orderTransactions.add(orderTransaction);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}

	public ArrayList<OrderTransaction> getPartyByOrdFailedDemoSample(
			String date, String beatCode, String routeCode) {
		String qurey = "select date,party_code,max(ord) as ord,max(demo) as demo,max(failed) as failed,max(sample) as sample from "
				+ " (select date,party_code,1 as ord,0 as failed,0 as demo,0 as sample from ordertran where date='"
				+ date
				+ "' and beat_code='"
				+ beatCode
				+ "' and route_code='"
				+ routeCode
				+ "' group by date,party_code union all"
				+ " select date,party_code,0 as ord,1 as failed,0 as demo, 0 as sample  from failed_visit where date='"
				+ date
				+ "' and beat_code='"
				+ beatCode
				+ "' and route_code='"
				+ routeCode
				+ "' group by date,party_code union all"
				+ " select date,party_code,0 as ord,0 as failed,1 as demo, 0 as sample from demo where date='"
				+ date
				+ "' and beat_code='"
				+ beatCode
				+ "' and route_code='"
				+ routeCode
				+ "' and qty>0 group by date,party_code union all"
				+ " select date,party_code, 0 as ord,0 as failed,0 as demo, 1 as sample from demo where date='"
				+ date
				+ "' and beat_code='"
				+ beatCode
				+ "' and route_code='"
				+ routeCode
				+ "' and samp_qty>0 group by date,party_code) a"
				+ " group by date,party_code ";
		System.out.println(qurey);
		Cursor cursor = database.rawQuery(qurey, null);
		ArrayList<OrderTransaction> orderTransactions = new ArrayList<OrderTransaction>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				OrderTransaction orderTransaction = new OrderTransaction();
				orderTransaction.setDate(cursor.getString(0));
				orderTransaction.setParty_code(cursor.getString(1));
				orderTransaction.setOrder(cursor.getString(2));
				orderTransaction.setDemo(cursor.getString(3));
				orderTransaction.setFailed(cursor.getString(4));
				orderTransaction.setSample(cursor.getString(5));
				orderTransactions.add(orderTransaction);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}

	public ArrayList<OrderTransaction> getExistDocId(String date,
													 String partyCode) {
		String query = "select doc_id,doc_id_sno,date,party_code,product_code,qty,rate from ordertran where date='"
				+ date + "' and party_code='" + partyCode + "'";

		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<OrderTransaction> orderTransactions = new ArrayList<OrderTransaction>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				OrderTransaction orderTransaction = new OrderTransaction();
				orderTransaction.setDoc_id(cursor.getString(0));
				orderTransaction.setDoc_id_sno(cursor.getInt(1));
				orderTransaction.setDate(cursor.getString(2));
				orderTransaction.setParty_code(cursor.getString(3));
				orderTransaction.setProduct_code(cursor.getString(4));
				orderTransaction.setQty(cursor.getString(5));
				orderTransaction.setRate(cursor.getString(6));
				orderTransactions.add(orderTransaction);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}

	public ArrayList<String> getReportData(String date) {
		int currentMonth = DateFunction.getCalculatedMonth("0").get("MONTH");
		int currentMonthYear = DateFunction.getCalculatedMonth("0").get("YEAR");
		String currentDate = DateFunction.getCurrentDateByFromat("yyyy-MM-dd");
		String lastDate = DateFunction.addDaysToDate(currentDate, "-30",
				"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss");
		String query = "select sum(monthlysecondry) as monthlysecondry ,sum(productive_visit) as productive_visit,sum(retailer_visit) as retailer_visit,"
				+ "sum(failedvisit) as failedvisit,sum(demo) as demo,sum(orderval) as orderval,sum(day_summary) as day_summary,sum(monthlyprimary) as monthlyprimary from "
				+ "(select ifnull(sum(amount),0)  as monthlyprimary, 0 AS monthlysecondry ,0 as productive_visit,0 as retailer_visit,"
				+ "0 as failedvisit,0 as demo,0 as orderval,0 as day_summary from porder ot where ot.visit_date='"
				+ date
				+ "' "
				+ "UNION ALL select 0  as monthlyprimary,ifnull(sum(amount),0)  as monthlysecondry ,0 as productive_visit,0 as retailer_visit,"
				+ "0 as failedvisit,0 as demo,0 as orderval,0 as day_summary from transorder ot where ot.visit_date='"
				+ date
				+ "' "
				+ "union all select 0  as monthlyprimary,0 as monthlysecondry ,0 as productive_visit,ifnull(count(*),0) as  retailer_visit, "
				+ "0 as failedvisit,0 as demo,0 as orderval,0 as day_summary "
				+ "from (select ifnull(party_code,'') as party_code from (select ifnull(party_code,'') as party_code, 'order' as type from transorder ot left join mastParty pm on ot.party_code=pm.webcode"
				+ " WHERE ot.visit_date='"
				+ date
				+ "' "
				+ "union all select ifnull(party_code,'') as party_code, 'demo' as type  from transdemo ot left join mastParty pm on ot.party_code=pm.webcode WHERE ot.visit_date='"
				+ date
				+ "' "
				+ "union all select ifnull(party_code,'') as party_code, 'failed' as type  from trans_failed_visit ot left join mastParty pm on ot.party_code=pm.webcode WHERE ot.visit_date='"
				+ date
				+ "' "
				+ "union all select ifnull(party_code,'') as party_code, 'Collection' as type  from transcollection ot left join mastParty pm on ot.party_code=pm.webcode WHERE ot.visit_date='"
				+ date
				+ "' "
				+ "union all select ifnull(party_code,'') as party_code, 'Competitor' as type  from transcompetitor    ot left join mastParty pm on ot.party_code=pm.webcode WHERE ot.visit_date='"
				+ date
				+ "' "
				+ ") a group by party_code) "
				+ "union all select 0 as monthlyprimary, 0 as monthlysecondry ,count(*) as productive_visit,0 as  retailer_visit,0 as failedvisit,0 as demo,0 as orderval,0 as day_summary from (select party_code from transorder "
				+ "where visit_date='"
				+ date
				+ "' "
				+ "group by party_code) "
				+ "union all select 0  as monthlyprimary,0 as monthlysecondry ,0 as productive_visit,0 as  retailer_visit,sum(failed) as failedvisit,sum(demo) as demo,sum(orderval) as orderval,0 as day_summary "
				+ "from (select party_code,max(a.failed) as failed,max(a.sample) as sample,max(a.demo) as demo ,max(a.orderval) as orderval from "
				+ "(select party_code, 1 as 'orderval' , 0 as 'failed',0 as 'sample',0 as 'demo' from transorder where visit_date='"
				+ date
				+ "' "
				+ "group by party_code union all select party_code, 0 as 'orderval' , 1 as 'failed',0 as 'sample',0 as 'demo' from trans_failed_visit"
				+ " where visit_date='"
				+ date
				+ "' "
				+ "group by party_code union all "
				+ "select party_code, 0 as 'orderval' , 0 as 'failed',0 as 'sample',1 as 'demo' from transdemo where visit_date='"
				+ date
				+ "' "
				+ ") a group by party_code) union all "
				+ "select 0  as monthlyprimary,0  as monthlysecondry ,0 as productive_visit,0 as retailer_visit,0 as failedvisit,0 as demo,0 as orderval,ifnull(sum(amount),0) as day_summary "
				+ " from transorder where visit_date='" + date + "' " + ")";

		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<String> reportArrayList = new ArrayList<String>();

		if (cursor.getCount() == 1) {
			cursor.moveToLast();
			reportArrayList.add(cursor.getString(0));
			reportArrayList.add(cursor.getString(1));
			reportArrayList.add(cursor.getString(2));
			reportArrayList.add(cursor.getString(3));
			reportArrayList.add(cursor.getString(4));
			reportArrayList.add(cursor.getString(5));
			reportArrayList.add(cursor.getString(6));
			reportArrayList.add(cursor.getString(7));
			for (int i = 0; i < reportArrayList.size(); i++) {
				System.out.println(reportArrayList.get(i));
				// if(i==5)
				// {
				// System.out.println(reportArrayList.get(i));
				// }
			}
		} else {
			System.out.println("no max id found");
		}
		cursor.close();
		return reportArrayList;
	}

	public void UpdateData(String item_id, String partycode, String price) {
		ContentValues values = new ContentValues();
		values.put("rate", price);
		String where = "code='" + partycode + "' and item_id='" + item_id + "'";
		int i = database.update(DatabaseConnection.TABLE_HISTORY, values,
				where, null);
		System.out.println("price updated=" + i);

	}

	public String getAlertedPrice(String item_id, String partycode) {
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
	
	public int getNoOfRetailerVisit(String date)
	{
		int sum=0;
		ArrayList<Integer> productiveVisit=new ArrayList<Integer>();
		String query1="select sum(noofretailervisit) as tvisit from("
+"select count(distinct party_code) as noofretailervisit  from transorder where visit_date='25/Nov/2016' union all "
+"select count(distinct party_code) as noofretailervisit  from transdemo where visit_date='25/Nov/2016' union all "
+"select count(distinct party_code) as noofretailervisit  from Trans_Failed_visit where visit_date='25/Nov/2016' and distid IS NULL union all "
+"select count(distinct party_code) as noofretailervisit  from TransCollection where visit_date='25/Nov/2016' union all "
+"select count(distinct party_code) as noofretailervisit  from TransCompetitor where visit_date='25/Nov/2016')";
		
		String query2="select count(distinct and_party_code) as NoOfProductiveVisit2 from TransOrder  where visit_date='"+date+"' and party_code IS NULL";
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
	Cursor cursor2=database.rawQuery(query1, null);
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
	
	
	
}
