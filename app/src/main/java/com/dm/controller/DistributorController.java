package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.Distributor;

import java.util.ArrayList;

public class DistributorController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_ADDRESS1,
			DatabaseConnection.COLUMN_ADDRESS2,
			DatabaseConnection.COLUMN_CITY_CODE,
			DatabaseConnection.COLUMN_PIN,
			DatabaseConnection.COLUMN_CONTACT_PERSON,
			DatabaseConnection.COLUMN_MOBILE,
			DatabaseConnection.COLUMN_PHONE,
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
			DatabaseConnection.COLUMN_PAN_NO,
			DatabaseConnection.COLUMN_REMARK,
			DatabaseConnection.COLUMN_CREDIT_LIMIT,
			DatabaseConnection.COLUMN_OUTSTANDING,
			DatabaseConnection.COLUMN_PENDING_ORDER,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_CREATED_DATE
	
	};
	
	public DistributorController(Context context) {
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
	
	public void insertDistributor(Distributor distributor){
        int c=0;
		String qry="select count(*) from mastDristributor where webcode="+distributor.getDistributor_id();
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
			values.put(DatabaseConnection.COLUMN_WEB_CODE, distributor.getDistributor_id());
		}


		values.put(DatabaseConnection.COLUMN_NAME, distributor.getDistributor_name());
		values.put(DatabaseConnection.COLUMN_ADDRESS1, distributor.getAddress1());
		values.put(DatabaseConnection.COLUMN_ADDRESS1, distributor.getAddress2());
		values.put(DatabaseConnection.COLUMN_CITY_CODE, distributor.getCity_id());
		values.put(DatabaseConnection.COLUMN_CONTACT_PERSON, distributor.getContact_person());
		values.put(DatabaseConnection.COLUMN_MOBILE, distributor.getMobile());
		values.put(DatabaseConnection.COLUMN_PHONE, distributor.getPhone());
		values.put(DatabaseConnection.COLUMN_EMAIL, distributor.getEmail());
		values.put(DatabaseConnection.COLUMN_PIN, distributor.getPin());
		values.put(DatabaseConnection.COLUMN_BLOCKED_BY, distributor.getBlocked_By());
		values.put(DatabaseConnection.COLUMN_BLOCK_DATE, distributor.getBlock_Date());
		values.put(DatabaseConnection.COLUMN_BLOCKED_REASON, distributor.getBlocked_Reason());
		values.put(DatabaseConnection.COLUMN_BEAT_CODE, distributor.getBeatId());
		values.put(DatabaseConnection.COLUMN_INDUSTRY_ID,distributor.getIndId());
		values.put(DatabaseConnection.COLUMN_POTENTIAL,distributor.getPotential());
		values.put(DatabaseConnection.COLUMN_PARTY_TYPE_CODE,distributor.getParty_type_code());
		values.put(DatabaseConnection.COLUMN_CST_NO,distributor.getCst_no());
		values.put(DatabaseConnection.COLUMN_VATTIN_NO,distributor.getVattin_no());
		values.put(DatabaseConnection.COLUMN_SERVICETAXREG_NO,distributor.getServicetaxreg_No());
		values.put(DatabaseConnection.COLUMN_PAN_NO,distributor.getPANNo());
		values.put(DatabaseConnection.COLUMN_REMARK,distributor.getRemark());
		values.put(DatabaseConnection.COLUMN_CREDIT_LIMIT,distributor.getCreditLimit());
		values.put(DatabaseConnection.COLUMN_OUTSTANDING,distributor.getOutStanding());
		values.put(DatabaseConnection.COLUMN_PENDING_ORDER,distributor.getPendingOrder());
		values.put(DatabaseConnection.COLUMN_SYNC_ID,distributor.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE,distributor.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,distributor.getCreatedDate());
		values.put(DatabaseConnection.COLUMN_OPEN_ORDER,distributor.getOpenOrder());
		values.put(DatabaseConnection.COLUMN_AREA_CODE,distributor.getAreaId());
		values.put(DatabaseConnection.COLUMN_CREDIT_DAYS,distributor.getCrediDays());
		long id=0;
		try{
			if(c>0)
			{
				try {
					 id = database.update(DatabaseConnection.TABLE_DISTRIBUTERMAST,
							values, "webcode='" + distributor.getDistributor_id() + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_DISTRIBUTERMAST, null, values);
					System.out.println("row=" + id);
				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
				}
			}


		}
		catch(RuntimeException e)
		{
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void insertDistributor(String mPid,
			String mPnm, String mAdd1,String mAdd2,	String mPin,
			String mAid,		String mEm,
			String mMo,		String mIndid,
			String mRmk,		String mSyncId,
			String mCtp,		String mCSTNo,
			String mVattin,		String mSrTx,
			String mPanno,		String mCid,
			String mCrlmt,		String mOS,
			String mPh,		String mOpor,
			String mCrd,	String mMS
)
	{
		int c=0;
		String qry="select count(*) from mastDristributor where webcode="+mPid;
		System.out.println(qry);
		Cursor cursor = database.rawQuery(qry, null);
		if(cursor.moveToFirst())
		{
			while(!(cursor.isAfterLast()))
			{
				c=cursor.getInt(0);
				cursor.moveToNext();
			}
		}
		else
		{
			c=0;
		}
		cursor.close();
		ContentValues values = new ContentValues();
		if(c>0)
		{

		}
		else{
			values.put(DatabaseConnection.COLUMN_WEB_CODE, mPid);
		}

		values.put(DatabaseConnection.COLUMN_NAME, mPnm);
		values.put(DatabaseConnection.COLUMN_ADDRESS1, mAdd1);
		values.put(DatabaseConnection.COLUMN_ADDRESS2, mAdd2);
		values.put(DatabaseConnection.COLUMN_CITY_CODE, mCid);
		values.put(DatabaseConnection.COLUMN_CONTACT_PERSON, mCtp);
		values.put(DatabaseConnection.COLUMN_MOBILE,mMo);
		values.put(DatabaseConnection.COLUMN_PHONE,mPh);
		values.put(DatabaseConnection.COLUMN_EMAIL, mEm);
		values.put(DatabaseConnection.COLUMN_PIN, mPin);
		values.put(DatabaseConnection.COLUMN_INDUSTRY_ID,mIndid);
		values.put(DatabaseConnection.COLUMN_CST_NO,mCSTNo);
		values.put(DatabaseConnection.COLUMN_VATTIN_NO,mVattin);
		values.put(DatabaseConnection.COLUMN_SERVICETAXREG_NO,mSrTx);
		values.put(DatabaseConnection.COLUMN_PAN_NO,mPanno);
		values.put(DatabaseConnection.COLUMN_REMARK,mRmk);
		values.put(DatabaseConnection.COLUMN_CREDIT_LIMIT,mCrlmt);
		values.put(DatabaseConnection.COLUMN_OUTSTANDING,mOS);
		values.put(DatabaseConnection.COLUMN_PENDING_ORDER," ");
		values.put(DatabaseConnection.COLUMN_SYNC_ID,mSyncId);
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,mMS);
		values.put(DatabaseConnection.COLUMN_OPEN_ORDER,mOpor);
		values.put(DatabaseConnection.COLUMN_AREA_CODE,mAid);
		values.put(DatabaseConnection.COLUMN_CREDIT_DAYS,mCrd);
		long id=0;
		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_DISTRIBUTERMAST,
							values, "webcode='" + mPid + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_DISTRIBUTERMAST, null, values);
					System.out.println("row=" + id);
				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
				}
			}


		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public ArrayList<Distributor> getdistibuterList(){
		Cursor cursor = database.query(DatabaseConnection.TABLE_DISTRIBUTERMAST,
				allColumns, null, null, null, null, null);
		ArrayList<Distributor> distributors = new ArrayList<Distributor>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Distributor distributor = new Distributor();
				distributor.setDistributor_id(cursor.getString(0));
				distributor.setDistributor_name(cursor.getString(1));
				distributor.setAddress1(cursor.getString(2));
				distributor.setAddress2(cursor.getString(3));
				distributor.setCity_id(cursor.getString(4));
				distributor.setPin(cursor.getString(5));
				distributor.setContact_person(cursor.getString(6));
				distributor.setMobile(cursor.getString(7));
				distributor.setPhone(cursor.getString(8));
				distributor.setEmail(cursor.getString(9));
				distributor.setBlocked_Reason(cursor.getString(11));
				distributor.setBlock_Date(cursor.getString(12));
				distributors.add(distributor);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return distributors;
	}
	
	public Distributor getDistributerDetail(String distname)
	{
	Distributor distributor = new Distributor();
	try{
//		String query="select *from mastDristributor d left join MastCity c on c.webcode=d.city_code where '('||c.name||')'||d.name||'('||d.sync_id||')'='"+distname+"'";
//		String query="select *from mastDristributor where name ||'-'||sync_id='"+distname+"'";
		/**************   Sandeep Singh 16-03-16   *****************/
			/**********		Start	*************/
		/*String query="select md.* from mastDristributor md left JOIN MastCity as c on c.webcode=md.city_code where md.name||'-'||md.sync_id||'-'||c.name='"+distname+"'"  ;
*/
		/**********		END	*************/
		String query="select md.* from mastDristributor md left JOIN MastCity as c on c.webcode=md.city_code where md.name||'-'||md.sync_id||'-'||c.name='"+distname+"' OR md.name||'-'||md.sync_id='"+distname+"'"  ;
		Cursor cursor=database.rawQuery(query, null);
		if (cursor.getCount()==1)
	    { cursor.moveToLast();
	       		
			distributor.setDistributor_id(cursor.getString(0));
			distributor.setDistributor_name(cursor.getString(1));
			distributor.setAddress1(cursor.getString(2));
			distributor.setAddress2(cursor.getString(3));
			distributor.setCity_id(cursor.getString(4));
			distributor.setPin(cursor.getString(5));
			distributor.setContact_person(cursor.getString(6));
			distributor.setMobile(cursor.getString(7));
			distributor.setPhone(cursor.getString(8));
			distributor.setEmail(cursor.getString(9));
			distributor.setBlocked_Reason(cursor.getString(10));
			distributor.setBlock_Date(cursor.getString(11));
			distributor.setBlocked_By(cursor.getString(12));
			distributor.setAreaId(cursor.getString(13));
			distributor.setBeatId(cursor.getString(14));
			distributor.setIndId(cursor.getString(15));
			distributor.setPotential(cursor.getString(16));
			distributor.setParty_type_code(cursor.getString(17));
			distributor.setCst_no(cursor.getString(18));
			distributor.setVattin_no(cursor.getString(19));
			distributor.setServicetaxreg_No(cursor.getString(20));
			distributor.setPANNo(cursor.getString(21));
			distributor.setRemark(cursor.getString(22));
			distributor.setCreditLimit(cursor.getString(23));
			distributor.setOutStanding(cursor.getString(24));
			distributor.setPendingOrder(cursor.getString(25));
			distributor.setSync_id(cursor.getString(26));
			distributor.setOpenOrder(cursor.getString(29));
			cursor.moveToNext();
	    }
	 else{
			System.out.println("No records found");
			
			distributor=null;	
	 }
		cursor.close();
	}catch(Exception e)
	{
		
	}
	    
		return distributor;
	
	
	}

	public Distributor getDistributorExist(String distname)
	{
		Distributor distributor = new Distributor();
		try{
//		String query="select *from mastDristributor d left join MastCity c on c.webcode=d.city_code where '('||c.name||')'||d.name||'('||d.sync_id||')'='"+distname+"'";
			String query="select *from mastDristributor where name ||'-'||sync_id='"+distname+"'";
			Cursor cursor=database.rawQuery(query, null);
			if (cursor.getCount()==1)
			{ cursor.moveToLast();

				distributor.setDistributor_id(cursor.getString(0));
				distributor.setDistributor_name(cursor.getString(1));
				distributor.setAddress1(cursor.getString(2));
				distributor.setAddress2(cursor.getString(3));
				distributor.setCity_id(cursor.getString(4));
				distributor.setPin(cursor.getString(5));
				distributor.setContact_person(cursor.getString(6));
				distributor.setMobile(cursor.getString(7));
				distributor.setPhone(cursor.getString(8));
				distributor.setEmail(cursor.getString(9));
				distributor.setBlocked_Reason(cursor.getString(10));
				distributor.setBlock_Date(cursor.getString(11));
				distributor.setBlocked_By(cursor.getString(12));
				distributor.setAreaId(cursor.getString(13));
				distributor.setBeatId(cursor.getString(14));
				distributor.setIndId(cursor.getString(15));
				distributor.setPotential(cursor.getString(16));
				distributor.setParty_type_code(cursor.getString(17));
				distributor.setCst_no(cursor.getString(18));
				distributor.setVattin_no(cursor.getString(19));
				distributor.setServicetaxreg_No(cursor.getString(20));
				distributor.setPANNo(cursor.getString(21));
				distributor.setRemark(cursor.getString(22));
				distributor.setCreditLimit(cursor.getString(23));
				distributor.setOutStanding(cursor.getString(24));
				distributor.setPendingOrder(cursor.getString(25));
				distributor.setSync_id(cursor.getString(26));
				distributor.setOpenOrder(cursor.getString(29));


				cursor.moveToNext();
			}
			else{
				System.out.println("No records found");

				distributor=null;
			}
			cursor.close();
		}catch(Exception e)
		{

		}

		return distributor;


	}

	public String getDistributerName(String distId)
	{
		String dname="";
		String query="select name from mastDristributor where webcode='"+distId+"'";
		try{
		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
		    dname=cursor.getString(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			
		}
	
			return dname;
		
	}

	public Distributor getDistributerNameAreaWise(String areaId)
	{
		Distributor distributor=new Distributor();
		String query="select name,webcode from mastDristributor where area_code='"+areaId+"'";
		try{
			Cursor cursor =database.rawQuery(query, null);

			if (cursor.getCount()==1)
			{ cursor.moveToLast();
				distributor.setDistributor_name(cursor.getString(0));
				distributor.setDistributor_id(cursor.getString(1));

				cursor.moveToNext();
			}
			else{
				System.out.println("No records found");
				distributor.setDistributor_name("NA");
				distributor.setDistributor_id("NA");

			}
			cursor.close();
		}catch(Exception e)
		{
System.out.println(e);
		}

		return distributor;

	}

	public String getDistributerName(String distId,int i)
	{
		String dname="";
		String query="select name||'-'||sync_id AS distname from mastDristributor where webcode='"+distId+"'";
		try{
		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
		    dname=cursor.getString(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			
		}
	
			return dname;
		
	}
//	public ArrayList<String> getInputDistributorOrder(String str,String cityId)
//	{
//		ArrayList<String> itemArray=new ArrayList<String>();
////		String qry="SELECT '('||c.name||')'||d.name||'('||d.sync_id||')' AS distname FROM mastDristributor d left join MastCity c on c.webcode=d.city_code WHERE d.city_code='"+cityId+"' and d.name LIKE '%"+str.trim()+"%'";
//		String qry="SELECT d.sync_id AS distname FROM mastDristributor d left join MastCity c on c.webcode=d.city_code WHERE d.name LIKE '%"+str.trim()+"%' and d.city_code in'"+cityId+"'";
////		c.name||'-'||d.name||'-'||
//		Cursor cursor = database.rawQuery(qry, null);
//		if(cursor.moveToFirst()){
//			while(!(cursor.isAfterLast()))
//			{
//				itemArray.add(cursor.getString(0).trim());
//				cursor.moveToNext();
//			}
//		}else{
//			System.out.println("No records found");
//		}
//		cursor.close();
//		return itemArray;
//
//	}
	public ArrayList<String> getInputDistributorOrder(String str,String cityId)
	{
		ArrayList<String> itemArray=new ArrayList<String>();
//		String search="";
//		String query="select DistSearchByName from AppEnviro";
//		Cursor cursor1 = database.rawQuery(query, null);
//		if(cursor1.moveToFirst()){
//			while(!(cursor1.isAfterLast()))
//			{
//				search=cursor1.getString(0);
//				cursor1.moveToNext();
//			}
//		}else{
//			System.out.println("No records found");
//		}
//		cursor1.close();
//
		String qry="";
//		if(search.equalsIgnoreCase("y"))
//		{
////			qry="SELECT  c.name||'-'|| d.name||'-'|| d.sync_id AS distname FROM mastDristributor d  left join MastCity c on c.webcode=d.city_code  WHERE (c.name LIKE '%" + str + "%' or d.name LIKE '%" + str + "%' or d.sync_id LIKE '%" + str + "%')  and d.city_code in(" + cityId + ")";
//
//			qry="SELECT  d.name AS distname FROM mastDristributor d  left join MastCity c on c.webcode=d.city_code  WHERE (c.name LIKE '%" + str + "%' or d.name LIKE '%" + str + "%' or d.sync_id LIKE '%" + str + "%')  and d.city_code in(" + cityId + ")";
//		}
//		else {
//			qry = "SELECT name||'-'||sync_id AS distname FROM mastDristributor WHERE name LIKE '%" + str + "%' and city_code in(" + cityId + ")";
//		}

		qry = "select md.name||'-'||md.sync_id||'-'||c.name AS distname,md.webcode as id from mastDristributor md left JOIN MastCity as c on c.webcode=md.city_code WHERE (md.sync_id LIKE  '%"+str+"%' OR  md.name LIKE '%"+str+"%' OR c.name LIKE '%"+str+"%' ) AND md.city_code in("+cityId+")";

		System.out.println(qry);
		Cursor cursor = database.rawQuery(qry, null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				itemArray.add(cursor.getString(0));
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return itemArray;

	}
//	public ArrayList<String> getInputDistributorOrder(String str)
//	{
//		ArrayList<String> itemArray=new ArrayList<String>();
//		String qry="SELECT '('||c.name||')'||d.name||'('||d.sync_id||')' AS distname FROM mastDristributor d left join MastCity c on c.webcode=d.city_code WHERE d.name LIKE '%"+str.trim()+"%'";
////		String qry="SELECT c.name||'-'||d.name||'-'||d.sync_id AS distname FROM mastDristributor d left join MastCity c on c.webcode=d.city_code WHERE d.name LIKE '%"+str.trim()+"%'";
//		Cursor cursor = database.rawQuery(qry, null);
//		if(cursor.moveToFirst()){
//			while(!(cursor.isAfterLast()))
//			{		
//				itemArray.add(cursor.getString(0));
//				cursor.moveToNext();
//			}
//		}else{
//			System.out.println("No records found");
//		}
//		cursor.close();
//		return itemArray;
//		
//	}
	public ArrayList<String> getInputDistributorOrder(String str)
	{
		ArrayList<String> itemArray=new ArrayList<String>();
		String qry="SELECT name||'-'||sync_id AS distname FROM mastDristributor WHERE name LIKE '%"+str+"%'";
		Cursor cursor = database.rawQuery(qry, null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{		
				itemArray.add(cursor.getString(0));
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return itemArray;
		
	}

	public ArrayList<Distributor> getDistributorList()
	{
//		HashMap<Integer, String> itemArray=new HashMap<Integer, String>();
		ArrayList<Distributor> itemArray=new ArrayList<Distributor>();
				String qry="";
//		String qry="SELECT name||'-'||sync_id AS distname FROM mastDristributor WHERE name LIKE '%"+str+"%'";
		qry = "select md.name||'-'||md.sync_id||'-'||c.name AS distname,md.webcode as id from mastDristributor md " +
				"left JOIN MastCity as c on c.webcode=md.city_code order by md.name";
//			int j=1;
		Cursor cursor = database.rawQuery(qry, null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{Distributor distributor =new Distributor();
				distributor.setDistributor_name(cursor.getString(0));
				distributor.setDistributor_id(cursor.getString(1));
				itemArray.add(distributor);
//				j++;
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return itemArray;

	}

	public void deleteDisrtibuterRow(){
		System.out.println("CDB Data deleted");
		database.delete(DatabaseConnection.TABLE_DISTRIBUTERMAST,null,null );
		System.out.println("Distributer deleted");
	}
	
	
	
}