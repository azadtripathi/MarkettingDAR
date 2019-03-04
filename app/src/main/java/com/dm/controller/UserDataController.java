package com.dm.controller;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.User;

import java.util.ArrayList;

public class UserDataController{
	
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	Context context;Activity activity;
	
	private String[] allColumns = {
			DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_USR_ID,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_PASSWORD,
			DatabaseConnection.COLUMN_LEVEL,
			DatabaseConnection.COLUMN_PDA_ID,
			DatabaseConnection.COLUMN_DSR_ALLOW_DAYS,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_EMAIL,
			DatabaseConnection.COLUMN_ROLEID,
			DatabaseConnection.COLUMN_DEPTID,
			DatabaseConnection.COLUMN_DESIGID
			
	};
	
	public UserDataController(Context context) {
		dbHelper = new DatabaseConnection(context);
		this.context=context;

		
	}

	public void open() {
		try{
		database = dbHelper.getWritableDatabase();
		}catch(SQLException e){
			System.out.println("-----------------"+e.getMessage());
		}
	}

	public void close() {
		if(database.isOpen())
		{
			database.close();
		}
	}
	public void insertUser(User user){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, user.getConper_id());
		values.put(DatabaseConnection.COLUMN_USR_ID, user.getUserId());
		values.put(DatabaseConnection.COLUMN_NAME, user.getUser_name());
		values.put(DatabaseConnection.COLUMN_PASSWORD, user.getPassword());
		values.put(DatabaseConnection.COLUMN_LEVEL, user.getLevel());
		values.put(DatabaseConnection.COLUMN_PDA_ID, user.getPDA_Id());
		values.put(DatabaseConnection.COLUMN_DSR_ALLOW_DAYS, user.getDSRAllowDays());
		values.put(DatabaseConnection.COLUMN_ACTIVE, user.getActive());
		values.put(DatabaseConnection.COLUMN_ROLEID, user.getRoleId());
		values.put(DatabaseConnection.COLUMN_DEPTID, user.getDeptId());
		values.put(DatabaseConnection.COLUMN_DESIGID, user.getDesigId());
		
		try{
		long id = database.insert(DatabaseConnection.TABLE_USERMAST, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	public ArrayList<User> getUserList(){
		Cursor cursor = database.query(DatabaseConnection.TABLE_USERMAST,
				allColumns, null, null, null, null, null);
		ArrayList<User> users = new ArrayList<User>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				User user = new User();
				user.setConper_id(cursor.getString(0));
				user.setUserId(cursor.getString(1));
				user.setUser_name(cursor.getString(2));
				user.setPassword(cursor.getString(3));
				user.setLevel(cursor.getString(4));
				user.setPDA_Id(cursor.getString(5));
				user.setDSRAllowDays(cursor.getInt(6));
				user.setActive(cursor.getString(7));
				user.setEmail(cursor.getString(8));
				user.setRoleId(cursor.getString(9));
				user.setDeptId(cursor.getString(10));
				user.setDesigId(cursor.getString(11));
				users.add(user);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return users;
	}
	public String getWebcode(String pdaId){
		String pda_Id ="";
		String query = "select ifnull(webcode,'') from mastUser where pda_id='"+pdaId+"'";
		Cursor cursor = database.rawQuery(query,null);
		
		 if (cursor.getCount()==1)
		    {
		            cursor.moveToLast();   
		            pda_Id=cursor.getString(0);
		            System.out.println("max id found  "+pda_Id);
		         
		    }
		 else{
			System.out.println("no max id found");
		}
		 System.out.println("no max id found"+pda_Id);
		 cursor.close();
		return pda_Id;
	}
	public String getUsername(String pdaId){
		String pda_Id ="";
		String query = "select name from mastUser where webcode='"+pdaId+"'";
		System.out.println("UserNameData"+pda_Id+"-"+query);
		Cursor cursor = database.rawQuery(query,null);

		if (cursor.getCount()==1)
		{
			cursor.moveToLast();
			pda_Id=cursor.getString(0);
			System.out.println("max id found  "+pda_Id);

		}
		else{
			System.out.println("no max id found");
		}
		System.out.println("no max id found"+pda_Id);
		cursor.close();
		return pda_Id;
	}
public void updateDsrAllowDaysValue(int days)
{
	ContentValues values = new ContentValues();
	values.put(DatabaseConnection.COLUMN_DSR_ALLOW_DAYS,days);
	try{
	long id = database.update(DatabaseConnection.TABLE_USERMAST, values, null, null);
	System.out.println("row="+id);
	} 
	catch(RuntimeException e){
		System.out.println("+++++++++++++++++++"+e.toString());
	}
}

public String getMaxTimeStamp(String tableName)
{
	double maxTimeStamp=0;
	String maxTimeStamp1="0";
	try{

		String query="select ifnull(max(timestamp),0) as maxtimestamp  from "+ tableName;
System.out.println(query);
	Cursor cursor =database.rawQuery(query, null);
	
	 if (cursor.getCount()==1)
	    { cursor.moveToLast(); 
	    maxTimeStamp1=cursor.getString(0);
			if(maxTimeStamp1.equals(" ") || maxTimeStamp1.equals("") )
			{
				maxTimeStamp1="0";
			}
	       cursor.moveToNext();
	    }
	 else{
		 maxTimeStamp1="0";
			System.out.println("No records found");
			
		}
		cursor.close();

	}catch(Exception e)
	{
		System.out.println(e);
	}


		return maxTimeStamp1;
}

	public void deleteTimeStampData(String tableName,String timeStamp)
	{
		try{
		database.delete(tableName,"timestamp="+timeStamp,null );
		System.out.println("timeStamp="+timeStamp+" is deleted");
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

	public void deleteEntityData(String tableName,String id)
	{
		String tn="0";
		System.out.print("del-entity"+tableName+":"+id);
		if(tableName.equals("MastParty"))
		{
			tn= DatabaseConnection.TABLE_PARTYMAST;
		}
		else if(tableName.equals("MastItem"))
		{
			tn= DatabaseConnection.TABLE_PRODUCTMAST;
		}
		else if(tableName.equals("MastArea"))
		{
			tn= DatabaseConnection.TABLE_AREAMAST;
		}

		else if(tableName.equals("MastCity"))
		{
			tn= DatabaseConnection.TABLE_CITYMAST;
		}
		else if(tableName.equals("MastDist"))
		{
			tn= DatabaseConnection.TABLE_DISTRIBUTERMAST;
		}
		else if(tableName.equals("MastBeat"))
		{
			tn= DatabaseConnection.TABLE_BEATMAST;
		}
		else if(tableName.equals("BeatPlan"))
		{
			tn= DatabaseConnection.TABLE_TRANSBEATPLAN;
		}

		try{
			if(!tn.equals("0")) {
				if(tableName.equalsIgnoreCase("BeatPlan"))
				{
					database.delete(tn, "web_doc_id='"+id+"'", null);
					System.out.println("id=" + id + " is deleted");
				}
				else{
				database.delete(tn, "webcode= '"+id+"'", null);
				System.out.println("id=" + id + " is deleted");
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}


	public int getUserExist()
	{
		int dataCount=0;
		try{
			String query="select count(*) as datacount  from mastUser";

		Cursor cursor =database.rawQuery(query, null);

		 if (cursor.getCount()==1)
			{ cursor.moveToLast();
			dataCount=cursor.getInt(0);
			   cursor.moveToNext();
			}
		 else{
				System.out.println("No records found");

			}
			cursor.close();
		}catch(Exception e)
		{
			System.out.println(e);
		}

			return dataCount;
	}

}
