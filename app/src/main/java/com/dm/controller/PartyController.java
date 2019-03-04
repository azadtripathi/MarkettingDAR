package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.library.DateFunction;
import com.dm.model.Party;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class PartyController {
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	static int i = 0;
	long doc_id = 0;
	Context context;
	private String[] allColumns = {DatabaseConnection.COLUMN_CODE,
			DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_ANDROID_DOCID,
			DatabaseConnection.COLUMN_NAME, DatabaseConnection.COLUMN_CODE,
			DatabaseConnection.COLUMN_ADDRESS1,
			DatabaseConnection.COLUMN_ADDRESS2,
			DatabaseConnection.COLUMN_CITY_CODE, DatabaseConnection.COLUMN_PIN,
			DatabaseConnection.COLUMN_CONTACT_PERSON,
			DatabaseConnection.COLUMN_MOBILE, DatabaseConnection.COLUMN_PHONE,
			DatabaseConnection.COLUMN_EMAIL,
			DatabaseConnection.COLUMN_BLOCKED_REASON,
			DatabaseConnection.COLUMN_BLOCK_DATE,
			DatabaseConnection.COLUMN_BLOCKED_BY,
			DatabaseConnection.COLUMN_BEAT_CODE,
			DatabaseConnection.COLUMN_INDUSTRY_ID,
			DatabaseConnection.COLUMN_POTENTIAL,
			DatabaseConnection.COLUMN_PARTY_TYPE_CODE,
			DatabaseConnection.COLUMN_CST_NO,
			DatabaseConnection.COLUMN_VATTIN_NO,
			DatabaseConnection.COLUMN_SERVICETAXREG_NO,
			DatabaseConnection.COLUMN_PAN_NO, DatabaseConnection.COLUMN_REMARK,
			DatabaseConnection.COLUMN_DISTRIBUTER_ID,
			DatabaseConnection.COLUMN_CREDIT_LIMIT,
			DatabaseConnection.COLUMN_OUTSTANDING,
			DatabaseConnection.COLUMN_PENDING_ORDER,
			DatabaseConnection.COLUMN_OPEN_ORDER,
			DatabaseConnection.COLUMN_CREDIT_DAYS,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_CREATED_DATE,
			DatabaseConnection.COLUMN_USER_CODE

	};

	public PartyController(Context context) {
		dbHelper = new DatabaseConnection(context);
		this.context = context;
	}


	public long updatePartyLocation(String lat, String lng, String partyId)
	{
		ContentValues cv = new ContentValues();
		cv.put(DatabaseConnection.COLUMN_LAT,lat);
		cv.put(DatabaseConnection.COLUMN_LNG,lng);
		cv.put(DatabaseConnection.COLUMN_TIMESTAMP,0);

		long id  = database.update(DatabaseConnection.TABLE_PARTYMAST,cv,"Android_id=?",new String[]{partyId});
		return id;
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

	public long insertParty(Party party) {
		long id=0;
		ContentValues values = new ContentValues();

		values.put(DatabaseConnection.COLUMN_WEB_CODE, party.getParty_id());


		values.put(DatabaseConnection.COLUMN_NAME, party.getParty_name());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,
				party.getAndroidId());
		values.put(DatabaseConnection.COLUMN_ADDRESS1, party.getAddress1());
		values.put(DatabaseConnection.COLUMN_ADDRESS2, party.getAddress2());
		values.put(DatabaseConnection.COLUMN_CITY_CODE, party.getCity_id());
		values.put(DatabaseConnection.COLUMN_CONTACT_PERSON,
				party.getContact_person());
		values.put(DatabaseConnection.COLUMN_MOBILE, party.getMobile());
		values.put(DatabaseConnection.COLUMN_PHONE, party.getPhone());
		values.put(DatabaseConnection.COLUMN_EMAIL, party.getEmail());
		values.put(DatabaseConnection.COLUMN_PIN, party.getPin());
		values.put(DatabaseConnection.COLUMN_BLOCKED_BY, party.getBlocked_By());
		values.put(DatabaseConnection.COLUMN_BLOCK_DATE, party.getBlock_Date());
		values.put(DatabaseConnection.COLUMN_BLOCKED_REASON,
				party.getBlocked_Reason());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, party.getAreaId());
		values.put(DatabaseConnection.COLUMN_BEAT_CODE, party.getBeatId());
		values.put(DatabaseConnection.COLUMN_INDUSTRY_ID, party.getIndId());
		values.put(DatabaseConnection.COLUMN_POTENTIAL, party.getPotential());
		values.put(DatabaseConnection.COLUMN_PARTY_TYPE_CODE,
				party.getParty_type_code());
		values.put(DatabaseConnection.COLUMN_CST_NO, party.getCst_no());
		values.put(DatabaseConnection.COLUMN_VATTIN_NO, party.getVattin_no());
		values.put(DatabaseConnection.COLUMN_SERVICETAXREG_NO,
				party.getServicetaxreg_No());
		values.put(DatabaseConnection.COLUMN_PAN_NO, party.getPANNo());
		values.put(DatabaseConnection.COLUMN_REMARK, party.getRemark());
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, party.getDistId());
		values.put(DatabaseConnection.COLUMN_CREDIT_LIMIT,party.getCreditLimit());
		values.put(DatabaseConnection.COLUMN_OUTSTANDING,
				party.getOutStanding());
		values.put(DatabaseConnection.COLUMN_PENDING_ORDER,
				party.getPendingOrder());
		values.put(DatabaseConnection.COLUMN_OPEN_ORDER, party.getOpenOrder());
		values.put(DatabaseConnection.COLUMN_CREDIT_DAYS, party.getCreditDays());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, party.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, party.getActive());
		values.put(DatabaseConnection.COLUMN_USER_CODE, party.getUserId());
		values.put(DatabaseConnection.COLUMN_DOA, party.getDoa());
		values.put(DatabaseConnection.COLUMN_DOB, party.getDob());
		values.put(DatabaseConnection.COLUMN_IS_BLOCKED, party.getIsBlocked());
		values.put(DatabaseConnection.COLUMN_CREATED_BY,party.getPartyCreatorName());
		values.put(DatabaseConnection.COLUMN_LAT,party.getPatryLat());
		values.put(DatabaseConnection.COLUMN_LNG,party.getPartyLng());
		values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,party.getPartyLatLngTimeStamp());
		// values.put(DatabaseConnection.COLUMN_UPLOAD,"0");
		System.out.println(party.getParty_name());
		// values.put(DatabaseConnection.COLUMN_TIMESTAMP,date.getTime());
		if (party.isIsnewparty()) {
			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_CREATED_DATE,
					party.getCreatedDate());
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");

		}

		try {
			 id = database.insert(DatabaseConnection.TABLE_PARTYMAST, null, values);
			if (id > 0) {
				if (party.isIsnewparty()) {

					if(getAppTypeFromDb().equalsIgnoreCase("1")) {
						SharedPreferences preferences = this.context.getSharedPreferences("PARTY_SESSION_DATA", Context.MODE_PRIVATE);
						SharedPreferences.Editor editor = preferences.edit();
						editor.putString("LAST_ADDRESS1_USED", party.getAddress1());
						editor.putString("LAST_ADDRESS2_USED", party.getAddress2());
						editor.putString("LAST_INDUSTRY_USED", party.getIndId());
						editor.putString("LAST_PIN_USED", party.getPin());
						editor.commit();
					}
				}

//			new ExportParty(context,false);
			}
//			System.out.println(id);
//			writeLog(party.getParty_id());

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
		return id;
	}

	public String getAppTypeFromDb(){
		String nm="";
		String qry=" select ifnull(Type,'') as name from MastAppData";
		Cursor cursor = database.rawQuery(qry, null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				nm=cursor.getString(0);
				cursor.moveToNext();
			}
		}else{
			nm="";
			System.out.println("No records found");
		}
		cursor.close();
		return nm;
	}
	public void insertPartyFromWeb(Party party) {

		int c=0;
		String qry="select count(*) from mastParty where webcode="+party.getParty_id();
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
			values.put(DatabaseConnection.COLUMN_WEB_CODE, party.getParty_id());
		}
		values.put(DatabaseConnection.COLUMN_NAME, party.getParty_name());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,
				party.getAndroidId());
		values.put(DatabaseConnection.COLUMN_ADDRESS1, party.getAddress1());
		values.put(DatabaseConnection.COLUMN_ADDRESS2, party.getAddress2());
		values.put(DatabaseConnection.COLUMN_CITY_CODE, party.getCity_id());
		values.put(DatabaseConnection.COLUMN_CONTACT_PERSON,
				party.getContact_person());
		values.put(DatabaseConnection.COLUMN_MOBILE, party.getMobile());
		values.put(DatabaseConnection.COLUMN_PHONE, party.getPhone());
		values.put(DatabaseConnection.COLUMN_EMAIL, party.getEmail());
		values.put(DatabaseConnection.COLUMN_PIN, party.getPin());
		values.put(DatabaseConnection.COLUMN_BLOCKED_BY, party.getBlocked_By());
		values.put(DatabaseConnection.COLUMN_BLOCK_DATE, party.getBlock_Date());
		values.put(DatabaseConnection.COLUMN_BLOCKED_REASON,
				party.getBlocked_Reason());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, party.getAreaId());
		values.put(DatabaseConnection.COLUMN_BEAT_CODE, party.getBeatId());
		values.put(DatabaseConnection.COLUMN_INDUSTRY_ID, party.getIndId());
		values.put(DatabaseConnection.COLUMN_POTENTIAL, party.getPotential());
		values.put(DatabaseConnection.COLUMN_PARTY_TYPE_CODE,
				party.getParty_type_code());
		values.put(DatabaseConnection.COLUMN_CST_NO, party.getCst_no());
		values.put(DatabaseConnection.COLUMN_VATTIN_NO, party.getVattin_no());
		values.put(DatabaseConnection.COLUMN_SERVICETAXREG_NO,
				party.getServicetaxreg_No());
		values.put(DatabaseConnection.COLUMN_PAN_NO, party.getPANNo());
		values.put(DatabaseConnection.COLUMN_REMARK, party.getRemark());
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, party.getDistId());
		values.put(DatabaseConnection.COLUMN_CREDIT_LIMIT,
				party.getCreditLimit());
		values.put(DatabaseConnection.COLUMN_OUTSTANDING,
				party.getOutStanding());
		values.put(DatabaseConnection.COLUMN_PENDING_ORDER,
				party.getPendingOrder());
		values.put(DatabaseConnection.COLUMN_OPEN_ORDER, party.getOpenOrder());
		values.put(DatabaseConnection.COLUMN_CREDIT_DAYS, party.getCreditDays());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, party.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, party.getActive());
		values.put(DatabaseConnection.COLUMN_USER_CODE, party.getUserId());
		values.put(DatabaseConnection.COLUMN_DOA, party.getDoa());
		values.put(DatabaseConnection.COLUMN_DOB, party.getDob());
		values.put(DatabaseConnection.COLUMN_IS_BLOCKED, party.getIsBlocked());
		// values.put(DatabaseConnection.COLUMN_UPLOAD,"0");
		System.out.println(party.getParty_name());
		// values.put(DatabaseConnection.COLUMN_TIMESTAMP,date.getTime());
		if (party.isIsnewparty()) {
			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_CREATED_DATE,
					party.getCreatedDate());
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");

		} else {
			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_CREATED_DATE,
					party.getCreatedDate());
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, party.getCreatedDate());
		}

			long id=0;
			try{
				if(c>0)
				{
					try {
						id = database.update(DatabaseConnection.TABLE_PARTYMAST,
								values, "webcode='" + party.getParty_id() + "'", null);
						System.out.println("row=" + id);
					} catch (RuntimeException e) {
						System.out.println("+++++++++++++++++++" + e.toString());
					}


				}
				else{
					try {
						id = database.insert(DatabaseConnection.TABLE_PARTYMAST, null, values);
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

		}

		values=null;party=null;cursor=null;
	}






	public boolean isPartyExist(String mobile,String webcode){

		Cursor c = database.query(DatabaseConnection.TABLE_PARTYMAST,null,"mobile=? And webcode!=?",new String[]{mobile,webcode},null,null,null);
		if(c.getCount()>0)
		{
			return true;
		}
		else
		{
			return false;
		}

	}

	public void insertPartyFromWeb(
			String At,String Ad1,String Ad2,String Ad,String Bd,String Bdt,String Bby,
			String Brzn,String Ct,String Cp,String CST,String DsId,String DA,String DB,
			String E,String Ind,String M,String PAN,String ID,String Nm,String PCd,String Pi,
			String Pl,String R,String STNo,String VTn,String blk,String MS,String createdName,
			boolean isIsnewparty,String androidid,String latitude,String longitude,String LatlngTime


	)
	{

		int c=0;
		String qry="select count(*) from mastParty where webcode="+ID;
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
			values.put(DatabaseConnection.COLUMN_WEB_CODE, ID);
		}
		values.put(DatabaseConnection.COLUMN_NAME, Nm);
		values.put(DatabaseConnection.COLUMN_ADDRESS1, Ad1);
		values.put(DatabaseConnection.COLUMN_ADDRESS2, Ad2);
		values.put(DatabaseConnection.COLUMN_CITY_CODE, Ct);
		values.put(DatabaseConnection.COLUMN_CONTACT_PERSON,Cp);
		values.put(DatabaseConnection.COLUMN_DOA, DA);
		values.put(DatabaseConnection.COLUMN_BLOCKED_BY, Bby);
		values.put(DatabaseConnection.COLUMN_BLOCK_DATE, Bdt);
		values.put(DatabaseConnection.COLUMN_BLOCKED_REASON,Brzn);
		values.put(DatabaseConnection.COLUMN_AREA_CODE, Ad);
		values.put(DatabaseConnection.COLUMN_BEAT_CODE, Bd);
		values.put(DatabaseConnection.COLUMN_CST_NO, CST);
		values.put(DatabaseConnection.COLUMN_MOBILE, M);
		values.put(DatabaseConnection.COLUMN_EMAIL, E);
		values.put(DatabaseConnection.COLUMN_PIN,Pi);
		values.put(DatabaseConnection.COLUMN_INDUSTRY_ID, Ind);
		values.put(DatabaseConnection.COLUMN_POTENTIAL, Pl);
		values.put(DatabaseConnection.COLUMN_PARTY_TYPE_CODE,PCd);
		values.put(DatabaseConnection.COLUMN_VATTIN_NO, VTn);
		values.put(DatabaseConnection.COLUMN_SERVICETAXREG_NO,STNo);
		values.put(DatabaseConnection.COLUMN_PAN_NO, PAN);
		values.put(DatabaseConnection.COLUMN_REMARK, R);
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID,DsId);
		values.put(DatabaseConnection.COLUMN_ACTIVE, At);
		values.put(DatabaseConnection.COLUMN_DOB, DB);
		values.put(DatabaseConnection.COLUMN_CREATED_BY,createdName);
		values.put(DatabaseConnection.COLUMN_IS_BLOCKED, blk);
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,androidid);
		values.put(DatabaseConnection.COLUMN_LAT,latitude);
		values.put(DatabaseConnection.COLUMN_LNG,longitude);
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,LatlngTime);
		if (isIsnewparty) {
			values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
			values.put(DatabaseConnection.COLUMN_CREATED_DATE,
					MS);
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction.ConvertDateToTimestamp(MS + " 00:00:00", "dd/MMM/yyyy 00:00:00"));

		} else {
			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			values.put(DatabaseConnection.COLUMN_CREATED_DATE,
					MS);
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, MS);
		}

		long id=0;
		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_PARTYMAST,
							values, "webcode='" +ID + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_PARTYMAST, null, values);
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


	public ArrayList<Party> getbyBeatPartyList(String beatId)
	{
		String qry = "";
		if(beatId.isEmpty())
		{
			qry = "select webcode,Android_id,name,contact_person,mobile," +
					"area_code,beat_code,address1,address2,CREATED_BY,latitude,longitude from mastParty order by name";
		}
		else{
			qry = "select webcode,Android_id,name,contact_person,mobile," +
					"area_code,beat_code,address1,address2,CREATED_BY,latitude,longitude from mastParty where beat_code='"
					+ beatId + "' order by name";
		}
		ArrayList<Party> partyList = new ArrayList<Party>();
		Cursor cursor = database.rawQuery(qry, null);
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				party.setParty_id(cursor.getString(0));
				party.setAndroidId(cursor.getString(1));
				party.setParty_name(cursor.getString(2));
				party.setContact_person(cursor.getString(3));
				party.setMobile(cursor.getString(4));
				party.setAreaId(cursor.getString(5));
				party.setBeatId(cursor.getString(6));
				party.setAddress1(cursor.getString(7));
				party.setAddress2(cursor.getString(8));
				party.setPartyCreatorName(cursor.getString(9));
				party.setPartyLng(cursor.getString(11));
				party.setPatryLat(cursor.getString(10));
				partyList.add(party);
				cursor.moveToNext();

			}
		} else {

			System.out.println("No records found");
		}
		cursor.close();

		return partyList;

	}

	public String getPartyName(String androidId) {
		String n="";
		String qry = "select name from mastParty where Android_id='" + androidId + "'";
		Cursor cursor = database.rawQuery(qry, null);
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {

				n=cursor.getString(0);
				cursor.moveToNext();
			}
		} else {

			System.out.println("No records found");
		}
		cursor.close();

		return n;

	}


	public ArrayList<Party> getbyBeatPartyList(String beatId, String text) {
//		String qry = "select webcode,Android_id,name,contact_person,mobile," +
//				"area_code,beat_code from mastParty where beat_code='"
//				+ beatId + "' and name like '%" + text + "%' order by name";

		String qry = "";

		if(beatId != null && !beatId.isEmpty() ) {
			qry = "select webcode,Android_id,name," +
					"contact_person,mobile,area_code,beat_code,address1,address2,CREATED_BY,latitude,longitude " +
					"from mastParty " +
					"where beat_code='" + beatId + "' " +
					"and (name like '%" + text + "%' or contact_person like '%" + text + "%' or mobile like '%" + text + "%') " +
					"order by name";
		}
		else
		{
			qry = "select webcode,Android_id,name," +
					"contact_person,mobile,area_code,beat_code,address1,address2,CREATED_BY,latitude,longitude " +
					"from mastParty " +
					"where " +
					"name like '%" + text + "%' or contact_person like '%" + text + "%' or mobile like '%" + text + "%' " +
					"order by name";
		}
		System.out.println(qry);
		ArrayList<Party> partyList = new ArrayList<Party>();
		Cursor cursor = database.rawQuery(qry, null);
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				party.setParty_id(cursor.getString(0));
				party.setAndroidId(cursor.getString(1));
				party.setParty_name(cursor.getString(2));
				party.setContact_person(cursor.getString(3));
				party.setMobile(cursor.getString(4));
				party.setAreaId(cursor.getString(5));
				party.setBeatId(cursor.getString(6));
				party.setAddress1(cursor.getString(7));
				party.setAddress2(cursor.getString(8));
				party.setPartyCreatorName(cursor.getString(9));
				party.setPatryLat(cursor.getString(10));;
				party.setPartyLng(cursor.getString(11));
				partyList.add(party);
				cursor.moveToNext();

			}
		} else {

			System.out.println("No records found");
		}
		cursor.close();

		return partyList;

	}

	public ArrayList<Party> getPartyListBYBaetRoute(String beatId,
													String routeId) {
		// String
		// query="select name from partymast where beat_code='"+beatId+"' and route_code='"+routeId+"'";
		Cursor cursor = database.query(DatabaseConnection.TABLE_PARTYMAST,
				allColumns, DatabaseConnection.COLUMN_BEAT_CODE + "='" + beatId
						+ "'", null, null, null,
				DatabaseConnection.COLUMN_NAME.toUpperCase());
		ArrayList<Party> partyArray = new ArrayList<Party>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				// party.setCode(cursor.getString(0));
				// party.setPartycode(cursor.getString(1));
				// party.setParty_name(cursor.getString(2));
				// party.setArea_id(cursor.getString(3));
				// party.setBeat_id(cursor.getString(4));
				// party.setRoute_id(cursor.getString(5));
				// party.setIndustry_id(cursor.getString(6));
				// party.setParty_type_id(cursor.getString(7));
				// party.setAddress1(cursor.getString(8));
				// party.setAddress2(cursor.getString(9));
				// party.setPincode(cursor.getString(10));
				// party.setCitycode_p1(cursor.getString(11));
				// party.setPhone(cursor.getString(12));
				// party.setMobile1(cursor.getString(13));
				// party.setEmail(cursor.getString(14));
				// party.setRating(cursor.getString(15));
				// party.setPotential(cursor.getString(16));
				// party.setBlock(cursor.getString(17));
				// party.setContactPerson(cursor.getString(18));
				// partyArray.add(party);
				// cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return partyArray;
	}

	public ArrayList<Party> getPartyListBYBaetRoute(String beatId) {

		ArrayList<Party> partyArray = new ArrayList<Party>();

		return partyArray;
	}

	public ArrayList<Party> getPartyListBYBaetRouteIndustry(String beatId,
															String routeId, String industryId) {
		// String
		// query="select name from partymast where beat_code='"+beatId+"' and route_code='"+routeId+"'";
		Cursor cursor = database.query(DatabaseConnection.TABLE_PARTYMAST,
				allColumns, DatabaseConnection.COLUMN_BEAT_CODE + "='" + beatId
						+ "' AND " + DatabaseConnection.COLUMN_INDUSTRY_ID
						+ "='" + industryId + "'", null, null, null,
				DatabaseConnection.COLUMN_NAME.toUpperCase());
		ArrayList<Party> partyArray = new ArrayList<Party>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				// party.setCode(cursor.getString(0));
				// party.setPartycode(cursor.getString(1));
				// party.setParty_name(cursor.getString(2));
				// party.setArea_id(cursor.getString(3));
				// party.setBeat_id(cursor.getString(4));
				// party.setRoute_id(cursor.getString(5));
				// party.setIndustry_id(cursor.getString(6));
				// party.setParty_type_id(cursor.getString(7));
				// party.setAddress1(cursor.getString(8));
				// party.setAddress2(cursor.getString(9));
				// party.setPincode(cursor.getString(10));
				// party.setCitycode_p1(cursor.getString(11));
				// party.setPhone(cursor.getString(12));
				// party.setMobile1(cursor.getString(13));
				// party.setEmail(cursor.getString(14));
				// party.setRating(cursor.getString(15));
				// party.setPotential(cursor.getString(16));
				// party.setBlock(cursor.getString(17));
				// party.setContactPerson(cursor.getString(18));
				// partyArray.add(party);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return partyArray;
	}

	public ArrayList<Party> getPartyListBYBaetRouteIndustryPartyType(
			String beatId, String routeId, String industryId, String partytypeId) {
		// String
		// query="select name from partymast where beat_code='"+beatId+"' and route_code='"+routeId+"'";
		Cursor cursor = database.query(DatabaseConnection.TABLE_PARTYMAST,
				allColumns, DatabaseConnection.COLUMN_BEAT_CODE + "='" + beatId
						+ "' AND " + DatabaseConnection.COLUMN_INDUSTRY_ID
						+ "='" + industryId + "'AND "
						+ DatabaseConnection.COLUMN_PARTY_TYPE_CODE + "='"
						+ partytypeId + "'", null, null, null,
				DatabaseConnection.COLUMN_NAME.toUpperCase());
		ArrayList<Party> partyArray = new ArrayList<Party>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				// party.setCode(cursor.getString(0));
				// party.setPartycode(cursor.getString(1));
				// party.setParty_name(cursor.getString(2));
				// party.setArea_id(cursor.getString(3));
				// party.setBeat_id(cursor.getString(4));
				// party.setRoute_id(cursor.getString(5));
				// party.setIndustry_id(cursor.getString(6));
				// party.setParty_type_id(cursor.getString(7));
				// party.setAddress1(cursor.getString(8));
				// party.setAddress2(cursor.getString(9));
				// party.setPincode(cursor.getString(10));
				// party.setCitycode_p1(cursor.getString(11));
				// party.setPhone(cursor.getString(12));
				// party.setMobile1(cursor.getString(13));
				// party.setEmail(cursor.getString(14));
				// party.setRating(cursor.getString(15));
				// party.setPotential(cursor.getString(16));
				// party.setBlock(cursor.getString(17));
				// party.setContactPerson(cursor.getString(18));
				// partyArray.add(party);
				// cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return partyArray;
	}

	public ArrayList<Party> getPartyListBYBaetRoutePartyType(String beatId,
															 String routeId, String partytypeId) {
		// String
		// query="select name from partymast where beat_code='"+beatId+"' and route_code='"+routeId+"'";
		Cursor cursor = database.query(DatabaseConnection.TABLE_PARTYMAST,
				allColumns, DatabaseConnection.COLUMN_BEAT_CODE + "='" + beatId
						+ "' AND " + DatabaseConnection.COLUMN_PARTY_TYPE_CODE
						+ "='" + partytypeId + "'", null, null, null,
				DatabaseConnection.COLUMN_NAME.toUpperCase());
		ArrayList<Party> partyArray = new ArrayList<Party>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				// party.setCode(cursor.getString(0));
				// party.setPartycode(cursor.getString(1));
				// party.setParty_name(cursor.getString(2));
				// party.setArea_id(cursor.getString(3));
				// party.setBeat_id(cursor.getString(4));
				// party.setRoute_id(cursor.getString(5));
				// party.setIndustry_id(cursor.getString(6));
				// party.setParty_type_id(cursor.getString(7));
				// party.setAddress1(cursor.getString(8));
				// party.setAddress2(cursor.getString(9));
				// party.setPincode(cursor.getString(10));
				// party.setCitycode_p1(cursor.getString(11));
				// party.setPhone(cursor.getString(12));
				// party.setMobile1(cursor.getString(13));
				// party.setEmail(cursor.getString(14));
				// party.setRating(cursor.getString(15));
				// party.setPotential(cursor.getString(16));
				// party.setBlock(cursor.getString(17));
				// party.setContactPerson(cursor.getString(19));
				partyArray.add(party);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return partyArray;
	}

	public ArrayList<Party> getFindPartyList(int arg, String stId, String text) {
		String query = "select mp.name,ma.name,mb.name,pt.name,"
				+ "mc.name,mp.contact_person,mp.mobile,mp.webcode,mp.Android_id from mastParty mp "
				+ "left join MastArea ma on ma.webcode=mp.area_code "
				+ "left join MastBeat mb on mb.webcode=mp.beat_code "
				+ "left join Partytypemast pt on pt.webcode=mp.party_type_code "
				+ "left join MastCity mc on mc.webcode=mp.city_code " +
				"where mp.city_code in (select mc.webcode from MastCity mc where state_code='"+stId+"') and" +
				" (mp.name like '%" + text + "%' or mp.contact_person like '%" + text + "%' or mp.mobile like '%" + text + "%' or mp.webcode like '%" + text + "%')";

		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Party> partyArray = new ArrayList<Party>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				party.setParty_name(cursor.getString(0));
				party.setAreaId(cursor.getString(1));
				party.setBeatId(cursor.getString(2));
				party.setParty_type_code(cursor.getString(3));
				party.setCity_id(cursor.getString(4));
				party.setContact_person(cursor.getString(5));
				party.setMobile(cursor.getString(6));
				party.setParty_id(cursor.getString(7));
				party.setAndroidId(cursor.getString(8));
				partyArray.add(party);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return partyArray;

	}

	public ArrayList<Party> getFindPartyList(int arg, String stId) {
		String query = "select mp.name,ma.name,mb.name,pt.name,"
				+ "mc.name,mp.contact_person,mp.mobile,mp.webcode,mp.Android_id from mastParty mp "
				+ "left join MastArea ma on ma.webcode=mp.area_code "
				+ "left join MastBeat mb on mb.webcode=mp.beat_code "
				+ "left join Partytypemast pt on pt.webcode=mp.party_type_code "
				+ "left join MastCity mc on mc.webcode=mp.city_code where mp.city_code in (select mc.webcode from MastCity mc where state_code='"+stId+"')";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Party> partyArray = new ArrayList<Party>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				party.setParty_name(cursor.getString(0));
				party.setAreaId(cursor.getString(1));
				party.setBeatId(cursor.getString(2));
				party.setParty_type_code(cursor.getString(3));
				party.setCity_id(cursor.getString(4));
				party.setContact_person(cursor.getString(5));
				party.setMobile(cursor.getString(6));
				party.setParty_id(cursor.getString(7));
				party.setAndroidId(cursor.getString(8));
				partyArray.add(party);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return partyArray;

	}

	public ArrayList<Party> getFindPartyList(int arg, String stId, String cId, String aId, String btId) {
		String query="",straddqry = "";
		if (!aId.equals("0") && btId.equals("0"))
		{
			straddqry = " mp.area_code='" + aId + "'";
		}
		else if (!aId.equals("0") && !btId.equals("0"))
		{
			straddqry = " mp.area_code='" + aId + "' and mp.beat_code='" + btId + "'";
		}
		else if (!cId.equals("0"))
		{
			straddqry = " mp.city_code='" + cId + "'";
		}
		else if (!stId.equals("0"))
		{
			straddqry = " mp.city_code in (select mc.webcode from MastCity mc where mc.state_code='"+stId+"')";
		}

		query = "select mp.name as RetailerName,md.Name as  DistributorName,ma.name as AreaName,mb.name as BeatName,pt.name as RetailerType,"
				+ "mc.name,mp.contact_person,mp.mobile,mp.webcode,mp.Android_id,im.name,mp.CREATED_BY,Mp.Active,mp.blocked_reason as blocked_reason from mastParty mp "
				+ "left join MastArea ma on ma.webcode=mp.area_code "
				+ "left join MastBeat mb on mb.webcode=mp.beat_code "
				+ "left join Partytypemast pt on pt.webcode=mp.party_type_code "
				+ "left join MastCity mc on mc.webcode=mp.city_code "
				+ "left join Industrymast im on im.webcode=mp.IndId " +
				" left join mastDristributor md on mp.DistId=md.webcode"+
				" where "+straddqry ;


		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Party> partyArray = new ArrayList<Party>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				party.setParty_name(cursor.getString(cursor.getColumnIndex("RetailerName")));
				party.setAreaId(cursor.getString(cursor.getColumnIndex("AreaName")));
				party.setBeatId(cursor.getString(2));
				party.setParty_type_code(cursor.getString(cursor.getColumnIndex("RetailerType")));
				party.setCity_id(cursor.getString(4));
				party.setContact_person(cursor.getString(5));
				party.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
				party.setParty_id(cursor.getString(8));
				party.setAndroidId(cursor.getString(9));
				party.setIndId(cursor.getString(cursor.getColumnIndex("name")));

				party.setActive(cursor.getString(12));
				party.setDistributorName(cursor.getString(cursor.getColumnIndex("DistributorName")));
				party.setPartyCreatorName(cursor.getString(11));
				party.setBlocked_Reason(cursor.getString(13));

				partyArray.add(party);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return partyArray;

	}

	public ArrayList<Party> getFindPartyList(String arg, String stId, String cId, String aId, String btId, String text) {


		String query="",straddqry = "";
		if (!aId.equals("0") && btId.equals("0"))
		{
			straddqry = " mp.area_code='" + aId + "' and ( mp.name like '%" + text + "%' or mp.contact_person like '%" + text + "%' or mp.mobile like '%" + text + "%' or mp.webcode like '%" + text + "%')";
		}
		else if (!aId.equals("0") && !btId.equals("0"))
		{
			straddqry = " mp.area_code='" + aId + "' and mp.beat_code='" + btId + "' and ( mp.name like '%" + text + "%' or mp.contact_person like '%" + text + "%' or mp.mobile like '%" + text + "%' or mp.webcode like '%" + text + "%')";
		}
		else if (!cId.equals("0"))
		{
			straddqry = " mp.city_code='" + cId + "' and ( mp.name like '%" + text + "%' or mp.contact_person like '%" + text + "%' or mp.mobile like '%" + text + "%' or mp.webcode like '%" + text + "%')";
		}
		else if (!stId.equals("0"))
		{
			straddqry = " mp.city_code in (select mc.webcode from MastCity mc where mc.state_code='"+stId+"') and ( mp.name like '%" + text + "%' or mp.contact_person like '%" + text + "%' or mp.mobile like '%" + text + "%' or mp.webcode like '%" + text + "%')";
		}

		query = "select mp.name,ma.name,mb.name,pt.name,"
				+ "mc.name,mp.contact_person,mp.mobile,mp.webcode,mp.Android_id,im.name,mp.CREATED_BY,Mp.Active,mp.blocked_reason,md.Name as  DistributorName from mastParty mp "
				+ "left join MastArea ma on ma.webcode=mp.area_code "
				+ "left join MastBeat mb on mb.webcode=mp.beat_code "
				+ "left join Partytypemast pt on pt.webcode=mp.party_type_code "
				+ "left join MastCity mc on mc.webcode=mp.city_code "
				+ "left join Industrymast im on im.webcode=mp.IndId " +
				" left join mastDristributor md on mp.DistId=md.webcode "+
				"where " +straddqry;


//		 query = "select mp.name,ma.name,mb.name,pt.name,"
//				+ "mc.name,mp.contact_person,mp.mobile,mp.webcode,mp.Android_id from mastParty mp "
//				+ "left join MastArea ma on ma.webcode=mp.area_code "
//				+ "left join MastBeat mb on mb.webcode=mp.beat_code "
//				+ "left join Partytypemast pt on pt.webcode=mp.party_type_code "
//				+ "left join MastCity mc on mc.webcode=mp.city_code " +
//				"where " +
//				"mp.city_code in (select mc.webcode from MastCity mc where state_code='"+stId+"')" +
//				" and " +
//				"( mp.area_code in (select ma.webcode from MastArea ma where ma.webcode='"+aId+"')" +
//				"or" +
//				" mp.beat_code in (select mb.webcode from Mastbeat mb where mb.webcode='"+btId+"')" +
//				"or" +
//				" mp.city_code in (select mc.webcode from Mastcity mc where mc.webcode='"+cId+"'))" +
//				" and( mp.name like '%" + text + "%' or mp.contact_person like '%" + text + "%' " +
//				"or mp.mobile like '%" + text + "%' or mp.webcode like '%" + text + "%'" +
//
//				")" +
//				"";


		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Party> partyArray = new ArrayList<Party>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				party.setParty_name(cursor.getString(0));
				party.setAreaId(cursor.getString(1));
				party.setBeatId(cursor.getString(2));
				party.setParty_type_code(cursor.getString(3));
				party.setCity_id(cursor.getString(4));
				party.setContact_person(cursor.getString(5));
				party.setMobile(cursor.getString(6));
				party.setParty_id(cursor.getString(7));
				party.setAndroidId(cursor.getString(8));
				party.setIndId(cursor.getString(9));
				party.setPartyCreatorName(cursor.getString(10));
				party.setActive(cursor.getString(11));
				party.setBlocked_Reason(cursor.getString(12));
				party.setDistributorName(cursor.getString(13));
				partyArray.add(party);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return partyArray;

	}


	public String getPartyName(String androidId,String partyid) {
		String n="";
		String qry = "select name from mastParty where Android_id='" + androidId + "' or webcode='"+ partyid +"'";
		Cursor cursor = database.rawQuery(qry, null);
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {

				n=cursor.getString(0);
				cursor.moveToNext();
			}
		} else {

			System.out.println("No records found");
		}
		cursor.close();

		return n;

	}
	public ArrayList<Party> getFindPartyList(String text) {
		String query = "select mp.name,ma.name,mb.name,pt.name,"
				+ "mc.name,mp.contact_person,mp.mobile,mp.webcode,mp.Android_id from mastParty mp "
				+ "left join MastArea ma on ma.webcode=mp.area_code "
				+ "left join MastBeat mb on mb.webcode=mp.beat_code "
				+ "left join Partytypemast pt on pt.webcode=mp.party_type_code "
				+ "left join MastCity mc on mc.webcode=mp.city_code where mp.name like '%" + text + "%' or mp.contact_person like '%" + text + "%' or mp.mobile like '%" + text + "%' or mp.webcode like '%" + text + "%'";

		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Party> partyArray = new ArrayList<Party>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				party.setParty_name(cursor.getString(0));
				party.setAreaId(cursor.getString(1));
				party.setBeatId(cursor.getString(2));
				party.setParty_type_code(cursor.getString(3));
				party.setCity_id(cursor.getString(4));
				party.setContact_person(cursor.getString(5));
				party.setMobile(cursor.getString(6));
				party.setParty_id(cursor.getString(7));
				party.setAndroidId(cursor.getString(8));
				partyArray.add(party);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return partyArray;

	}

	public Party getPartyList(String webcode, String androidId) {
		String query = "";
		Party party = new Party();
		if (webcode != null && androidId != null)
			query = "select * from mastParty" +
					" where webcode='" + webcode
					+ "' and Android_id='" + androidId + "'";
		else if (webcode == null && androidId != null)
			query = "select * from mastParty where Android_id='" + androidId
					+ "'";
		else if (webcode != null && androidId == null)
			query = "select * from mastParty where webcode='" + webcode + "'";

		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.getCount() == 1) {
			cursor.moveToLast();

			party.setParty_id(cursor.getString(0));
			party.setAndroidId(cursor.getString(1));
			party.setParty_name(cursor.getString(2));
			party.setAddress1(cursor.getString(3));
			party.setAddress2(cursor.getString(4));
			party.setCity_id(cursor.getString(5));
			party.setPin(cursor.getString(6));
			party.setContact_person(cursor.getString(7));
			party.setMobile(cursor.getString(8));
			party.setPhone((cursor.getString(9)==null?"":cursor.getString(9)));
			party.setEmail((cursor.getString(10)==null?"":cursor.getString(10)));
			party.setBlocked_Reason(cursor.getString(11));
			party.setBlock_Date(cursor.getString(12));
			party.setBlocked_By(cursor.getString(13));
			party.setAreaId(cursor.getString(14));
			party.setBeatId(cursor.getString(15));
			party.setIndId(cursor.getString(16));
			party.setPotential(cursor.getString(17));
			party.setParty_type_code(cursor.getString(18));
			party.setCst_no((cursor.getString(19)==null?"":cursor.getString(19)));
			party.setVattin_no(cursor.getString(20));
			party.setServicetaxreg_No((cursor.getString(21)==null?"":cursor.getString(21)));
			party.setPANNo((cursor.getString(22)==null?"":cursor.getString(22)));
			party.setRemark(cursor.getString(23));
			party.setDistId(cursor.getString(24));
			party.setCreditLimit(cursor.getString(25));
			party.setOutStanding(cursor.getString(26));
			party.setPendingOrder(cursor.getString(27));
			party.setOpenOrder(cursor.getString(28));
			party.setCreditDays(cursor.getString(29));
			party.setSync_id(cursor.getString(30));
			party.setActive(cursor.getString(31));
			party.setCreatedDate(cursor.getString(32));
			party.setDob((cursor.getString(34)==null?"":cursor.getString(34)));
			party.setDoa((cursor.getString(35)==null?"":cursor.getString(35)));
			cursor.moveToNext();
		} else {
			System.out.println("No records found");

		}
		cursor.close();
		return party;
	}

	public ArrayList<Party> getPartyListSearch(String text, String beatid,
											   String routeid) {
		String where = DatabaseConnection.COLUMN_NAME + " LIKE '%" + text
				+ "%'" + " AND " + DatabaseConnection.COLUMN_BEAT_CODE + "='"
				+ beatid + "'";
		Cursor cursor = database.query(DatabaseConnection.TABLE_PARTYMAST,
				allColumns, where, null, null, null,
				DatabaseConnection.COLUMN_NAME.toUpperCase());

		ArrayList<Party> partyArray = new ArrayList<Party>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				// party.setCode(cursor.getString(0));
				// party.setPartycode(cursor.getString(1));
				// party.setParty_name(cursor.getString(2));
				// party.setArea_id(cursor.getString(3));
				// party.setBeat_id(cursor.getString(4));
				// party.setRoute_id(cursor.getString(5));
				// party.setIndustry_id(cursor.getString(6));
				// party.setParty_type_id(cursor.getString(7));
				// party.setAddress1(cursor.getString(8));
				// party.setAddress2(cursor.getString(9));
				// party.setPincode(cursor.getString(10));
				// party.setCitycode_p1(cursor.getString(11));
				// party.setPhone(cursor.getString(12));
				// party.setMobile1(cursor.getString(13));
				// party.setEmail(cursor.getString(14));
				// party.setRating(cursor.getString(15));
				// party.setPotential(cursor.getString(16));
				// party.setBlock(cursor.getString(17));
				// party.setContactPerson(cursor.getString(18));
				// partyArray.add(party);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return partyArray;
	}

	public ArrayList<Party> getFilteredPartyListBYBaetRouteIndustryPartyType(
			String beatId, String routeId, String industryId,
			String partytypeId, String text) {
		String where = DatabaseConnection.COLUMN_NAME + " LIKE '%" + text
				+ "%'";
		Cursor cursor = null;
		String query1 = "select code,webcode,name,area_code,beat_code,route_code,industry_code,party_type_code,address1,address2,pin,city_code"
				+ " ,phone,mobile,email,rating,potential,block,contact_person from partymast "
				+ "where beat_code='"
				+ beatId
				+ "' and route_code='"
				+ routeId
				+ "' and " + where + " order by name";
		String query3 = "select code,webcode,name,area_code,beat_code,route_code,industry_code,party_type_code,address1,address2,pin,city_code"
				+ " ,phone,mobile,email,rating,potential,block,contact_person from partymast "
				+ "where beat_code='"
				+ beatId
				+ "' and route_code='"
				+ routeId
				+ "' and industry_code='"
				+ industryId
				+ "' and "
				+ where
				+ " order by name";
		String query4 = "select code,webcode,name,area_code,beat_code,route_code,industry_code,party_type_code,address1,address2,pin,city_code"
				+ " ,phone,mobile,email,rating,potential,block,contact_person from partymast "
				+ "where beat_code='"
				+ beatId
				+ "' and route_code='"
				+ routeId
				+ "' and party_type_code='"
				+ partytypeId
				+ "' and industry_code='"
				+ industryId
				+ "' and "
				+ where
				+ " order by name";
		if (beatId.equals("") && routeId.equals("") && industryId.equals("")
				&& partytypeId.equals("")) {
			cursor = database.rawQuery(query4, null);
		} else if (!beatId.equals("") && !routeId.equals("")
				&& industryId.equals("") && partytypeId.equals("")) {
			cursor = database.rawQuery(query1, null);
		} else if (!beatId.equals("") && !routeId.equals("")
				&& !industryId.equals("") && partytypeId.equals("")) {
			cursor = database.rawQuery(query3, null);
		} else if (!beatId.equals("") && !routeId.equals("")
				&& !industryId.equals("") && !partytypeId.equals("")) {
			cursor = database.rawQuery(query4, null);
		}
		ArrayList<Party> partyArray = new ArrayList<Party>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				// party.setCode(cursor.getString(0));
				// party.setPartycode(cursor.getString(1));
				// party.setParty_name(cursor.getString(2));
				// party.setArea_id(cursor.getString(3));
				// party.setBeat_id(cursor.getString(4));
				// party.setRoute_id(cursor.getString(5));
				// party.setIndustry_id(cursor.getString(6));
				// party.setParty_type_id(cursor.getString(7));
				// party.setAddress1(cursor.getString(8));
				// party.setAddress2(cursor.getString(9));
				// party.setPincode(cursor.getString(10));
				// party.setCitycode_p1(cursor.getString(11));
				// party.setPhone(cursor.getString(12));
				// party.setMobile1(cursor.getString(13));
				// party.setEmail(cursor.getString(14));
				// party.setRating(cursor.getString(15));
				// party.setPotential(cursor.getString(16));
				// party.setBlock(cursor.getString(17));
				// party.setContactPerson(cursor.getString(18));
				// partyArray.add(party);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return partyArray;
	}

	// public long getMaxCode(){
	// doc_id = 0;Cursor cursor=null;
	// String query =
	// "select ifnull(max(cast(code as integer)),0)+1 from mastParty";
	//
	// try {cursor = database.rawQuery(query,null);
	//
	// if (cursor.getCount()==1)
	// {
	// cursor.moveToLast();
	// doc_id=cursor.getLong(0);
	// System.out.println("max id found "+doc_id);
	//
	// }
	// else{
	// System.out.println("no max id found");
	// }
	// }
	// finally {
	// // this gets called even if there is an exception somewhere above
	// if(cursor != null)
	// cursor.close();
	// }
	// System.out.println("no max id found"+doc_id);
	// cursor.close();
	// return doc_id;
	// }
	public long updatetParty(Party party) {
		long id = 0;
		ContentValues values = new ContentValues();
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
		values.put(DatabaseConnection.COLUMN_WEB_CODE, party.getParty_id());
		values.put(DatabaseConnection.COLUMN_NAME, party.getParty_name());
		values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,
				party.getAndroidId());
		values.put(DatabaseConnection.COLUMN_ADDRESS1, party.getAddress1());
		values.put(DatabaseConnection.COLUMN_ADDRESS2, party.getAddress2());
		values.put(DatabaseConnection.COLUMN_CITY_CODE, party.getCity_id());
		values.put(DatabaseConnection.COLUMN_CONTACT_PERSON,
				party.getContact_person());
		values.put(DatabaseConnection.COLUMN_MOBILE, party.getMobile());
		values.put(DatabaseConnection.COLUMN_PHONE, party.getPhone());
		values.put(DatabaseConnection.COLUMN_EMAIL, party.getEmail());
		values.put(DatabaseConnection.COLUMN_PIN, party.getPin());
		values.put(DatabaseConnection.COLUMN_BLOCKED_BY, party.getBlocked_By());
		values.put(DatabaseConnection.COLUMN_BLOCK_DATE, party.getBlock_Date());
		values.put(DatabaseConnection.COLUMN_BLOCKED_REASON,
				party.getBlocked_Reason());
		values.put(DatabaseConnection.COLUMN_AREA_CODE, party.getAreaId());
		values.put(DatabaseConnection.COLUMN_BEAT_CODE, party.getBeatId());
		values.put(DatabaseConnection.COLUMN_INDUSTRY_ID, party.getIndId());
		values.put(DatabaseConnection.COLUMN_POTENTIAL, party.getPotential());
		values.put(DatabaseConnection.COLUMN_PARTY_TYPE_CODE,
				party.getParty_type_code());
		values.put(DatabaseConnection.COLUMN_CST_NO, party.getCst_no());
		values.put(DatabaseConnection.COLUMN_VATTIN_NO, party.getVattin_no());
		values.put(DatabaseConnection.COLUMN_SERVICETAXREG_NO,
				party.getServicetaxreg_No());
		values.put(DatabaseConnection.COLUMN_PAN_NO, party.getPANNo());
		values.put(DatabaseConnection.COLUMN_REMARK, party.getRemark());
		values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, party.getDistId());
		values.put(DatabaseConnection.COLUMN_CREDIT_LIMIT,
				party.getCreditLimit());
		values.put(DatabaseConnection.COLUMN_OUTSTANDING,
				party.getOutStanding());
		values.put(DatabaseConnection.COLUMN_PENDING_ORDER,
				party.getPendingOrder());
		values.put(DatabaseConnection.COLUMN_OPEN_ORDER, party.getOpenOrder());
		values.put(DatabaseConnection.COLUMN_CREDIT_DAYS, party.getCreditDays());
		values.put(DatabaseConnection.COLUMN_SYNC_ID, party.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, party.getActive());
		values.put(DatabaseConnection.COLUMN_USER_CODE, party.getUserId());
		values.put(DatabaseConnection.COLUMN_DOA, party.getDoa());
		values.put(DatabaseConnection.COLUMN_DOB, party.getDob());
		values.put(DatabaseConnection.COLUMN_CREATED_DATE,
				party.getCreatedDate());
		values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,"0");
		values.put(DatabaseConnection.COLUMN_LAT,party.getPatryLat());
		values.put(DatabaseConnection.COLUMN_LNG,party.getPartyLng());

		if (party.getParty_id() != null && party.getAndroidId() != null) {

			try {
				id = database.update(
						DatabaseConnection.TABLE_PARTYMAST,
						values,
						DatabaseConnection.COLUMN_WEB_CODE + "='"
								+ party.getParty_id() + "'", null);
				System.out.println("id=" + id);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
		}
	else if (party.getParty_id() != null && party.getAndroidId() == null) {

			try {
				id = database.update(
						DatabaseConnection.TABLE_PARTYMAST,
						values,
						DatabaseConnection.COLUMN_WEB_CODE + "='"
								+ party.getParty_id() + "'", null);
				System.out.println("id=" + id);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
		} else if (party.getAndroidId() != null && party.getParty_id() == null) {
			try {
				id = database.update(
						DatabaseConnection.TABLE_PARTYMAST,
						values,
						DatabaseConnection.COLUMN_ANDROID_DOCID + "='"
								+ party.getAndroidId() + "'", null);
				System.out.println("id=" + id);
			} catch (RuntimeException e) {
				System.out.println("+++++++++++++++++++" + e.toString());
			}
		}
//		if (id > 0) {
//					if(getAppTypeFromDb().equalsIgnoreCase("1")) {
//					SharedPreferences preferences = this.context.getSharedPreferences("PARTY_SESSION_DATA", Context.MODE_PRIVATE);
//					SharedPreferences.Editor editor = preferences.edit();
//					editor.putString("LAST_ADDRESS1_USED", party.getAddress1());
//					editor.putString("LAST_ADDRESS2_USED", party.getAddress2());
//					editor.putString("LAST_INDUSTRY_USED", party.getIndId());
//					editor.putString("LAST_PIN_USED", party.getPin());
//					editor.commit();
//			}
//		}
		return id;
	}

	/*public void updatetPartyWebcode(String webcode, String androidId) {*/
	/************************		Write By Sandeep Singh 10-04-2017		******************/
	/*****************		START		******************/
	public void updatetPartyWebcode(String webcode, String androidId,String timeStamp) {
		/*****************		END		******************/
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, webcode);
		values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
		/*****************		END		******************/
		try {
			long id = database.update(DatabaseConnection.TABLE_PARTYMAST,
					values, DatabaseConnection.COLUMN_ANDROID_DOCID + "='"
							+ androidId + "'", null);
			System.out.println("id=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}
/************************		Write By Sandeep Singh 10-04-2017		******************/
	/*****************		START		******************/
	public void updatetPartyWebcodeInTransactions(String webcode, String androidId,String timeStamp) {
		/*****************		END		******************/
		//public void updatetPartyWebcodeInTransactions(String webcode, String androidId) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_PARTY_CODE, webcode);
		ContentValues values1 = new ContentValues();
		values1.put(DatabaseConnection.COLUMN_CODE, webcode);
		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,"0");
		/*****************		END		******************/

		try {
			long id = database.update(DatabaseConnection.TABLE_HISTORY,
					values1, DatabaseConnection.COLUMN_WEB_CODE + "='"
							+ androidId + "'", null);
			System.out.println("id=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
		try {
			long id = database.update(DatabaseConnection.TABLE_ORDER,
					values, DatabaseConnection.COLUMN_ANDROID_PARTY_CODE + "='"
							+ androidId + "'", null);
			System.out.println("id=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}

		try {
			long id = database.update(DatabaseConnection.TABLE_ORDER1,
					values, DatabaseConnection.COLUMN_ANDROID_PARTY_CODE + "='"
							+ androidId + "'", null);
			System.out.println("id=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}

		try {
			long id = database.update(DatabaseConnection.TABLE_TRANSDEMO,
					values, DatabaseConnection.COLUMN_ANDROID_PARTY_CODE + "='"
							+ androidId + "'", null);
			System.out.println("id=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}

		try {
			long id = database.update(DatabaseConnection.TABLE_TRANSCOLLECTION,
					values, DatabaseConnection.COLUMN_ANDROID_PARTY_CODE + "='"
							+ androidId + "'", null);
			System.out.println("id=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}

		try {
			long id = database.update(DatabaseConnection.TABLE_COMPETITOR,
					values, DatabaseConnection.COLUMN_ANDROID_PARTY_CODE + "='"
							+ androidId + "'", null);
			System.out.println("id=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}

		try {
			long id = database.update(DatabaseConnection.TABLE_TRANSFAILED_VISIT,
					values, DatabaseConnection.COLUMN_ANDROID_PARTY_CODE + "='"
							+ androidId + "'", null);
			System.out.println("id=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}

	}

/************************		Write By Sandeep Singh 10-04-2017		******************/
	/*****************		START		******************/
	public void updatetPartyUpload(String webcode,String timeStamp,String androidId) {
		/*****************		END		******************/
		//public void updatetPartyUpload(String webcode) {
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
		values.put("webcode",webcode);
		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
		/*****************		END		******************/
		try {
			long id = database.update(DatabaseConnection.TABLE_PARTYMAST,
					values, DatabaseConnection.COLUMN_ANDROID_DOCID + "='" + androidId
							+ "'", null);
			System.out.println("id=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}

	public ArrayList<Party> getPartyListBy(String beatId, String routeId) {
		String query = "select * from mastParty where code in (select party_code from failed_visit where next_visit_date='16/Nov/2013'  and beat_code='5817' and route_code='4540' )";
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Party> partyArray = new ArrayList<Party>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				// party.setCode(cursor.getString(0));
				// party.setPartycode(cursor.getString(1));
				// party.setParty_name(cursor.getString(2));
				// party.setArea_id(cursor.getString(3));
				// party.setBeat_id(cursor.getString(4));
				// party.setRoute_id(cursor.getString(5));
				// party.setIndustry_id(cursor.getString(6));
				// party.setParty_type_id(cursor.getString(7));
				// party.setAddress1(cursor.getString(8));
				// party.setAddress2(cursor.getString(9));
				// party.setPincode(cursor.getString(10));
				// party.setCitycode_p1(cursor.getString(11));
				// party.setPhone(cursor.getString(12));
				// party.setMobile1(cursor.getString(13));
				// party.setEmail(cursor.getString(14));
				// party.setRating(cursor.getString(15));
				// party.setPotential(cursor.getString(16));
				// party.setBlock(cursor.getString(17));
				// party.setContactPerson(cursor.getString(19));
				// partyArray.add(party);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return partyArray;
	}


	public ArrayList<Party> getPendingParties()
	{
		String straddqry = " mp.timestamp='" + 0 + "'";
		String query = "select mp.name as RetailerName,md.Name as  DistributorName,ma.name as AreaName,mb.name as BeatName,pt.name as RetailerType,"
				+ "mc.name,mp.contact_person,mp.mobile,mp.webcode,mp.Android_id,im.name,mp.CREATED_BY,Mp.Active,mp.blocked_reason as blocked_reason from mastParty mp "
				+ "left join MastArea ma on ma.webcode=mp.area_code "
				+ "left join MastBeat mb on mb.webcode=mp.beat_code "
				+ "left join Partytypemast pt on pt.webcode=mp.party_type_code "
				+ "left join MastCity mc on mc.webcode=mp.city_code "
				+ "left join Industrymast im on im.webcode=mp.IndId " +
				" left join mastDristributor md on mp.DistId=md.webcode"+
				" where "+straddqry ;


		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Party> partyArray = new ArrayList<Party>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				party.setParty_name(cursor.getString(cursor.getColumnIndex("RetailerName")));
				party.setAreaId(cursor.getString(cursor.getColumnIndex("AreaName")));
				party.setBeatId(cursor.getString(2));
				party.setParty_type_code(cursor.getString(cursor.getColumnIndex("RetailerType")));
				party.setCity_id(cursor.getString(4));
				party.setContact_person(cursor.getString(5));
				party.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
				party.setParty_id(cursor.getString(8));
				party.setAndroidId(cursor.getString(9));
				party.setIndId(cursor.getString(cursor.getColumnIndex("name")));

				party.setActive(cursor.getString(12));
				party.setDistributorName(cursor.getString(cursor.getColumnIndex("DistributorName")));
				party.setPartyCreatorName(cursor.getString(11));
				party.setBlocked_Reason(cursor.getString(13));
				partyArray.add(party);
				cursor.moveToNext();
			}
		} else {
			System.out.println("No records found");
		}
		return partyArray;
	}

	public ArrayList<Party> getPartyListUpload() {
		String query = "select ifnull(webcode,'') as PartyId,ifnull(Android_id,'web') as androiId,ifnull(name,'') as name," +
				"ifnull(address1,'') as add1,ifnull(address2,'') as add2,ifnull(city_code,'0') as cityId,ifnull(pin,'0') as pin,"
				+ "ifnull(contact_person,0) as contactPerson,ifnull(DistId,0) as distId,ifnull(mobile,0) as mobile,ifnull(phone,0) as phone," +
				"ifnull(email,'') as email,ifnull(area_code ,'0') as areaId,ifnull(beat_code ,'0') as beatId,ifnull(IndId ,'0') as IndId," +
				"ifnull(party_type_code ,'0') as partyTypeCode,"
				+ "ifnull(Active,'') as active,ifnull(potential,0) as potential,ifnull(vattin_no,0) as vattinNo,ifnull(Servicetaxreg_No ,0) as ServiceTaxNo," +
				"ifnull(cst_no ,0) as cstNo,ifnull(PANNo ,0) as panNo,"
				+ "ifnull(sync_id ,0) as SynId,ifnull(CreatedDate,0) as createdDate,ifnull(user_code,0) as userId,ifnull(remark,0) as remark, ifnull(dob,'') as dob," +
				"ifnull(doa,'') as doa, timestamp,blocked_reason,latitude,longitude,LatlngTime from mastParty where timestamp='0' ";

		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Party> partyArray = new ArrayList<Party>();
		if(cursor.getCount()>0) {
			if (cursor.moveToFirst()) {
				while (!(cursor.isAfterLast())) {
					Party party = new Party();
					party.setParty_id(cursor.getString(cursor
							.getColumnIndex("PartyId")));
					party.setAndroidId(cursor.getString(cursor
							.getColumnIndex("androiId")));
					party.setParty_name(cursor.getString(cursor
							.getColumnIndex("name")));
					party.setAddress1(cursor.getString(cursor
							.getColumnIndex("add1")));
					party.setAddress2(cursor.getString(cursor
							.getColumnIndex("add2")));
					party.setCity_id(cursor.getString(cursor
							.getColumnIndex("cityId")));
					party.setPin(cursor.getString(cursor.getColumnIndex("pin")));
					party.setContact_person(cursor.getString(cursor
							.getColumnIndex("contactPerson")));
					party.setDistId(cursor.getString(cursor
							.getColumnIndex("distId")));
					party.setMobile(cursor.getString(cursor
							.getColumnIndex("mobile")));
					party.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
					party.setEmail(cursor.getString(cursor.getColumnIndex("email")));
					party.setAreaId(cursor.getString(cursor
							.getColumnIndex("areaId")));
					party.setBeatId(cursor.getString(cursor
							.getColumnIndex("beatId")));
					party.setIndId(cursor.getString(cursor.getColumnIndex("IndId")));
					party.setParty_type_code(cursor.getString(cursor
							.getColumnIndex("partyTypeCode")));
					party.setActive(cursor.getString(cursor
							.getColumnIndex("active")));
					party.setPotential(cursor.getString(cursor
							.getColumnIndex("potential")));
					party.setVattin_no(cursor.getString(cursor
							.getColumnIndex("vattinNo")));
					party.setServicetaxreg_No(cursor.getString(cursor
							.getColumnIndex("ServiceTaxNo")));
					party.setCst_no(cursor.getString(cursor.getColumnIndex("cstNo")));
					party.setPANNo(cursor.getString(cursor.getColumnIndex("panNo")));
					party.setSync_id(cursor.getString(cursor
							.getColumnIndex("SynId")));
					party.setCreatedDate(cursor.getString(cursor
							.getColumnIndex("createdDate")));
					party.setUserId(cursor.getString(cursor
							.getColumnIndex("userId")));
					party.setRemark(cursor.getString(cursor
							.getColumnIndex("remark")));
					party.setDob(cursor.getString(cursor
							.getColumnIndex("dob")));
					party.setDoa(cursor.getString(cursor
							.getColumnIndex("doa")));
					/************************		Write By Sandeep Singh 10-04-2017		******************/
					/*****************		START		******************/
					party.settimeStamp(cursor.getString(cursor.getColumnIndex("timestamp")));
					party.setBlocked_Reason(cursor.getString(cursor.getColumnIndex("blocked_reason")));
					party.setPatryLat(cursor.getString(cursor.getColumnIndex("latitude")));
					party.setPartyLng(cursor.getString(cursor.getColumnIndex("longitude")));
					party.setPartyLatLngTimeStamp(cursor.getString(cursor.getColumnIndex("LatlngTime")));
					/*****************		END		******************/
					partyArray.add(party);
					cursor.moveToNext();
				}

			}
		}else {
			System.out.println("No records found");
		}
		cursor.close();
		return partyArray;
	}

	public ArrayList<String> getPartyListByFollowDate(String beat_code,
													  String route_code, String date) {
		String query = "select ifnull(code,'') from mastParty pm "
				+ " inner join failed_visit fv on fv.party_code=pm.code "
				+ " where pm.beat_code='" + beat_code + "' and pm.route_code='"
				+ route_code + "'  and fv.timestamp='"
				+ DateFunction.ToConvertDateTime(date, "00", "00", "00")
				+ "' group by code";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<String> partyArray = new ArrayList<String>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				partyArray.add(cursor.getString(0));
				System.out.println("Nos records found=" + cursor.getString(0));
				cursor.moveToNext();

			}

		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return partyArray;
	}

	public ArrayList<String> getPartyListByOrder(String beat_code,
												 String route_code, String date) {
		String query = "select ifnull(code,'') from mastParty pm "
				+ " inner join ordertran fv on fv.party_code=pm.code "
				+ " where pm.beat_code='" + beat_code + "' and pm.route_code='"
				+ route_code + "'  and fv.timestamp='"
				+ DateFunction.ToConvertDateTime(date, "00", "00", "00") + "' ";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<String> partyArray = new ArrayList<String>();
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {
				Party party = new Party();
				partyArray.add(cursor.getString(0));
				System.out.println("Nos records found=" + cursor.getString(0));
				cursor.moveToNext();

			}

		} else {
			System.out.println("No records found");
		}
		cursor.close();
		return partyArray;
	}

	public void updatetMobileNoParty(String mobile, String code) {
		ContentValues values = new ContentValues();
		Calendar c = Calendar.getInstance();
		System.out.println("Current time => " + c.getTime());
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:MM:SS");
		values.put(DatabaseConnection.COLUMN_MOBILE, mobile);
		values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
		try {
			long id = database.update(DatabaseConnection.TABLE_PARTYMAST,
					values, DatabaseConnection.COLUMN_CODE + "='" + code + "'",
					null);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}

	public int validatePartyMobileNo(String mb) {
		int flag = 0;
		String query = "select *from mastParty where mobile='" + mb + "'";
		// ArrayList<String> mobileNos=new ArrayList<String>();
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {

				// mobileNos.add(cursor.getString(0));
				// System.out.println("Nos records found="+cursor.getString(0));

				flag = 1;
				cursor.moveToNext();

			}

		} else {
			flag = 0;
			System.out.println("No records found");
		}
		cursor.close();
		return flag;

	}

	public int validatePartyMobileNo(String mb, String partyId, String androidId) {
		int flag = 0;
		String query = "";
		if (partyId != null && androidId != null)
			query = "select *from mastParty where mobile='" + mb
					+ "' and webcode<>'" + partyId + "' ";
		else if (partyId == null && androidId != null)
			query = "select *from mastParty where mobile='" + mb
					+ "' and Android_id<>'" + androidId + "' ";
		else if (partyId != null && androidId == null)
			query = "select *from mastParty where mobile='" + mb
					+ "'  and webcode<>'" + partyId + "' ";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		if (cursor.moveToFirst()) {
			while (!(cursor.isAfterLast())) {

				// mobileNos.add(cursor.getString(0));
				// System.out.println("Nos records found="+cursor.getString(0));

				flag = 1;
				cursor.moveToNext();

			}

		} else {
			flag = 0;
			System.out.println("No records found");
		}
		cursor.close();
		return flag;

	}


	public boolean isAndroidIdExist(String android_doc_id)
	{
		Cursor c = database.query(DatabaseConnection.TABLE_PARTYMAST,null,"Android_id='" +android_doc_id + "'",null,null,null,null);
		if(c.getCount()>0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

//	public void writeLog(String msg) {
////		Calendar calendar1 = Calendar.getInstance();
//		try {
//			System.out.println("adsfda====sfsfsd");
//			File myFile = new File("/sdcard/dmcrm.txt");
//			//  myFile = new File(Environment.getDataDirectory()+"/fftlog/myuploadfile.txt");
//			if (!myFile.exists()) {
//				myFile.createNewFile();
//			}
//			FileOutputStream fOut = new FileOutputStream(myFile, true);
//			OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
//			myOutWriter.append(msg + ",");
//			myOutWriter.close();
//			fOut.close();
//		} catch (IOException e) {
//			System.out.println("adsf%%%%%%%%%%Sdasfsfsd");
////	    	 writeLog(" writeLog IO exception="+e);
//			e.printStackTrace();
//		}
//
//	}
}
