/**
 
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
import com.dm.model.Enviro;

import java.util.ArrayList;

public class EnviroController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {
			DatabaseConnection.COLUMN_SREP_CODE,
			DatabaseConnection.COLUMN_ORDER_NO,
			DatabaseConnection.COLUMN_PORDER_NO,
			DatabaseConnection.COLUMN_PORDER1_NO,
			DatabaseConnection.COLUMN_DEMO_NO,
			DatabaseConnection.COLUMN_FAILEDVISIT_NO,
			DatabaseConnection.COLUMN_VISIT_NO,
			DatabaseConnection.COLUMN_COMPTITOR_NO,
			DatabaseConnection.COLUMN_PARTY_NO,
			DatabaseConnection.COLUMN_DISCUSSION_NO,
			DatabaseConnection.COLUMN_DIST_COLLEC_NO,
			DatabaseConnection.COLUMN_RECIEP_NO,
			DatabaseConnection.COLUMN_LEAVE_NO,
			DatabaseConnection.COLUMN_BEATPLAN_NO,
			DatabaseConnection.COLUMN_ORDER1_NO,
			DatabaseConnection.COLUMN_TIMESTAMP,
			DatabaseConnection.COLUMN_Dist_Stock_NO
	};
		
	public EnviroController(Context context) {
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
	
	
	public void deleteEnviro()
	{
		try{
		database.delete(DatabaseConnection.TABLE_ENVIRO,null,null );
		
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
	
	public void insertEnviro(Enviro enviro)
	{

		int c=0;ContentValues values = new ContentValues();
		String qry="select count(*) from Enviro";
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
		if(c==0)
		{
			//arrayListForColumns.add("ASalesReturnNo "+DATA_TYPE_INTEGER+",");
			//arrayListForColumns.add("ASales1ReturnNo "+DATA_TYPE_INTEGER);
			values.put(DatabaseConnection.COLUMN_SREP_CODE, enviro.getSrep_code());
			values.put(DatabaseConnection.COLUMN_COMPTITOR_NO,"0");
			values.put(DatabaseConnection.COLUMN_PARTY_NO, "0");
			values.put(DatabaseConnection.COLUMN_DISCUSSION_NO, "0");
			values.put(DatabaseConnection.COLUMN_DIST_COLLEC_NO, "0");
			values.put(DatabaseConnection.COLUMN_RECIEP_NO, "0");
			values.put(DatabaseConnection.COLUMN_LEAVE_NO,"0");
			values.put(DatabaseConnection.COLUMN_BEATPLAN_NO, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");
			values.put(DatabaseConnection.COLUMN_VISIT_NO, "0");
			values.put(DatabaseConnection.COLUMN_ORDER_NO, "0");
			values.put(DatabaseConnection.COLUMN_ORDER1_NO, "0");
			values.put(DatabaseConnection.COLUMN_PORDER_NO, "0");
			values.put(DatabaseConnection.COLUMN_PORDER1_NO, "0");
			values.put(DatabaseConnection.COLUMN_DEMO_NO, "0");
			values.put(DatabaseConnection.COLUMN_FAILEDVISIT_NO, "0");
			values.put(DatabaseConnection.COLUMN_Dist_Stock_NO, "0");
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, enviro.getTimeStamp());
			values.put("ASalesReturnNo", enviro.getSalesReturnNo());
			values.put("ASales1ReturnNo", enviro.getSalesReturn1No());
			try{
				long id = database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
				System.out.println("enviro inserted "+id);

			} catch(RuntimeException e){
				System.out.println("+++++++++++++++++++"+e.toString());
			}
		}

			if(getMaxVisit_no()<Integer.parseInt(enviro.getVisit_no()))
			{
				updateEnviroVisit(Integer.parseInt(enviro.getVisit_no()));
			}
			if(getMaxOrder_no()<Integer.parseInt(enviro.getOrder_no()))
			{
				updateEnviroOrder(Integer.parseInt( enviro.getOrder_no()));
			}
			if(getMaxOrder1_no()<Integer.parseInt(enviro.getOrder1_no())){
				updateEnviroOrder1(Integer.parseInt(enviro.getOrder1_no()));
			}
			if(Integer.parseInt(getMaxPorder_no())<Integer.parseInt(enviro.getPorder_no()))
			{
				updateEnviroPorder(enviro.getPorder_no());
			}
			if(getMaxDemo_no()<Integer.parseInt(enviro.getDemo_no()))
			{
				updateEnviroDemo(Integer.parseInt(enviro.getDemo_no()));
			}
			if(Integer.parseInt(getMaxPorder1_no())<Integer.parseInt(enviro.getPorder1_no()))
			{
				updateEnviroPorder1(enviro.getPorder1_no());
			}
			if(getMaxfailedVisit_no()<Integer.parseInt(enviro.getFailed_No()))
			{
				updateEnviroFailedVisit(Integer.parseInt(enviro.getFailed_No()));
			}
			if(getMaxCompt_no()<Integer.parseInt(enviro.getCompt_no()))
			{
				updateEnviroCompetitor(Integer.parseInt(enviro.getCompt_no()));
			}
			if(getMaxParty_No()<Integer.parseInt(enviro.getParty_no()))
			{
				updateEnviroPartyNo(Integer.parseInt(enviro.getParty_no()));
			}
			if(getMaxDiscussion_No()<Integer.parseInt(enviro.getDiscussionNo()))
			{
				updateEnviroDiscussionNo(Integer.parseInt(enviro.getDiscussionNo()));
			}
			if(getMaxDistCollection_No()<Integer.parseInt(enviro.getDistCollectionNo()))
			{updateEnviroDistColNo(Integer.parseInt(enviro.getDistCollectionNo()));}
			if(Integer.parseInt(getMaxReciept_no())<Integer.parseInt(enviro.getReciep_no()))	{updateEnviroReciept(enviro.getReciep_no());}
		if(getMaxDistStock_no()<Integer.parseInt((enviro.getDistStockNo().equals("")?"0":enviro.getDistStockNo())))	{updateEnviroDistStock(Integer.parseInt((enviro.getDistStockNo().equals("")?"0":enviro.getDistStockNo())));}
		updateEnviroTimeStamp(Long.parseLong(enviro.getTimeStamp()));
	}
	public ArrayList<Enviro> getEnviroList(){
		Cursor cursor = database.query(DatabaseConnection.TABLE_ENVIRO,
				null, null, null, null, null, null);
		ArrayList<Enviro> enviros = new ArrayList<Enviro>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Enviro enviro = new Enviro();
				enviro.setSrep_code(cursor.getString(0));
				enviro.setOrder_no(cursor.getString(1));
				enviro.setPorder_no(cursor.getString(2));
				enviro.setPorder1_no(cursor.getString(3));
				enviro.setDemo_no(cursor.getString(4));
				enviro.setFailed_No(cursor.getString(5));
				enviro.setVisit_no(cursor.getString(6));
				enviro.setCompt_no(cursor.getString(7));
				enviro.setParty_no(cursor.getString(8));
				enviro.setDiscussionNo(cursor.getString(9));
				enviro.setDistCollectionNo(cursor.getString(10));
				enviro.setReciep_no(cursor.getString(11));
				enviro.setLeave_no(cursor.getString(12));
				enviro.setBeatNo(cursor.getString(13));
				enviro.setOrder1_no(cursor.getString(14));
				enviro.setTimeStamp(cursor.getString(15));
				enviro.setSalesReturnNo(cursor.getString(16));
				enviro.setSalesReturn1No(cursor.getString(17));
				enviros.add(enviro);
				cursor.moveToNext();
			}
		}
		else
		{
			System.out.println("No records found");
		}
		cursor.close();
		return enviros;
	}

	public Enviro getEnviroPaddedList(){
		String query="select ifnull(max(order_no)+1,0) as maxOrderNo,ifnull(max(demo_no)+1,0) as maxDemoNo,ifnull(max(visit_no)+1,0) as maxVisitNo from enviro";
		Cursor cursor =database.rawQuery(query, null);
		Enviro enviro = null;

		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
			 enviro = new Enviro();
				enviro.setOrder_no(cursor.getString(0));
				enviro.setDemo_no(cursor.getString(1));
				enviro.setVisit_no(cursor.getString(2));
//				enviro.setReciep_no(cursor.getString(2));
				cursor.moveToNext();
			}
	
		else{
			System.out.println("No records found");
		}
		cursor.close();
		return enviro;
	}
	
	public void updateEnviro(Enviro enviro){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_SREP_CODE, enviro.getSrep_code());
		values.put(DatabaseConnection.COLUMN_ORDER_NO, enviro.getOrder_no());
		values.put(DatabaseConnection.COLUMN_PORDER_NO, enviro.getPorder_no());
		values.put(DatabaseConnection.COLUMN_PORDER1_NO, enviro.getPorder1_no());
		values.put(DatabaseConnection.COLUMN_DEMO_NO, enviro.getDemo_no());
		values.put(DatabaseConnection.COLUMN_FAILEDVISIT_NO, enviro.getFailed_No());
		values.put(DatabaseConnection.COLUMN_VISIT_NO, enviro.getVisit_no());
		values.put(DatabaseConnection.COLUMN_COMPTITOR_NO, enviro.getCompt_no());
		values.put(DatabaseConnection.COLUMN_PARTY_NO, enviro.getParty_no());
		values.put(DatabaseConnection.COLUMN_DISCUSSION_NO, enviro.getDiscussionNo());
		values.put(DatabaseConnection.COLUMN_DIST_COLLEC_NO, enviro.getDistCollectionNo());
		values.put(DatabaseConnection.COLUMN_RECIEP_NO, enviro.getReciep_no());
		values.put(DatabaseConnection.COLUMN_LEAVE_NO, enviro.getLeave_no());
		values.put(DatabaseConnection.COLUMN_BEATPLAN_NO, enviro.getBeatNo());
		values.put(DatabaseConnection.COLUMN_ORDER1_NO, enviro.getOrder1_no());


		try{
		long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
		System.out.println("enviro updated "+id);
		
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	
}
	
	public String getMaxPorder_no()
	{
		String pno="";
		try{
			String query="select ifnull(max(porder_no),0) as maxporder_no  from enviro ";

		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
		     pno=cursor.getString(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			
		}
	
			return pno;
	}
	public void updateEnviroPorder(String pno)
	{
		ContentValues values = new ContentValues();	
		values.put(DatabaseConnection.COLUMN_PORDER_NO, pno);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro porder_no insert "+id);
			
			} catch(RuntimeException e){
				
				System.out.println("+++++++++++++++++++"+e.toString());
			}
	}
	
	public String getMaxPorder1_no()
	{
		String pno="";
		try{
			String query="select ifnull(max(Porder1_no),0) as maxporder_no  from enviro ";

		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
		     pno=cursor.getString(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			System.out.println("exception"+e);
		}
	
			return pno;
	}
	public void updateEnviroPorder1(String pno)
	{
		ContentValues values = new ContentValues();	
		values.put(DatabaseConnection.COLUMN_PORDER1_NO, pno);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro porder_no insert "+id);
			
			} catch(RuntimeException e){
				
				System.out.println("+++++++++++++++++++"+e.toString());
			}
	}
	public String getMaxReciept_no()
	{
		String rno="";
		try{
			String query="select ifnull(max(reciep_no),0) as maxreciep_no  from enviro ";
//	String query="select reciep_no as reciep_no  from enviro where srep_code ";
		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
//		    rno=cursor.getString(cursor.getColumnIndex("maxreciep_no"));
		     rno=cursor.getString(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			
		}
	
			return rno;
	}
	
	public void updateEnviroReciept(String rno)
	{
		ContentValues values = new ContentValues();	
		values.put(DatabaseConnection.COLUMN_RECIEP_NO, rno);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro reciept_no updated "+id);
			
			} catch(RuntimeException e){
				
				System.out.println("+++++++++++++++++++"+e.toString());
			}
	}
	public int getMaxVisit_no()
	{
		int vno=0;
		try{
			String query="select ifnull(max(visit_no),0) as maxvisit_no  from enviro ";
//	String query="select reciep_no as reciep_no  from enviro where srep_code ";
		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
//		    rno=cursor.getString(cursor.getColumnIndex("maxreciep_no"));
		     vno=cursor.getInt(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			
		}
	
			return vno;
	}
	
	public void updateEnviroVisit(int vno)
	{
		ContentValues values = new ContentValues();	
		values.put(DatabaseConnection.COLUMN_VISIT_NO, vno);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro visit_no updated "+id);
			
			} catch(RuntimeException e){
				
				System.out.println("+++++++++++++++++++"+e.toString());
			}
	}

	public void updateEnviroTimeStamp(long timestamp)
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_TIMESTAMP,timestamp);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro visit_no updated "+id);

		} catch(RuntimeException e){

			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	public int getSales1ReturnNo()
	{
		int ono=0;
		try{
			String query="select ifnull(max(ASales1ReturnNo),0) as maxorder_no  from enviro ";
//	String query="select reciep_no as reciep_no  from enviro where srep_code ";
			Cursor cursor =database.rawQuery(query, null);

			if (cursor.getCount()==1)
			{ cursor.moveToLast();
//		    rno=cursor.getString(cursor.getColumnIndex("maxreciep_no"));
				ono=cursor.getInt(0);
				cursor.moveToNext();
			}
			else{
				System.out.println("No records found");

			}
			cursor.close();
		}catch(Exception e)
		{

		}

		return ono;
	}

	public int getSalesReturnNo()
	{
		int ono=0;
		try{
			String query="select ifnull(max(ASalesReturnNo),0) as maxorder_no  from enviro ";
//	String query="select reciep_no as reciep_no  from enviro where srep_code ";
			Cursor cursor =database.rawQuery(query, null);

			if (cursor.getCount()==1)
			{ cursor.moveToLast();
//		    rno=cursor.getString(cursor.getColumnIndex("maxreciep_no"));
				ono=cursor.getInt(0);
				cursor.moveToNext();
			}
			else{
				System.out.println("No records found");

			}
			cursor.close();
		}catch(Exception e)
		{

		}

		return ono;
	}


	public int getMaxOrder_no()
	{
		int ono=0;
		try{
			String query="select ifnull(max(order_no),0) as maxorder_no  from enviro ";
//	String query="select reciep_no as reciep_no  from enviro where srep_code ";
		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
//		    rno=cursor.getString(cursor.getColumnIndex("maxreciep_no"));
		    ono=cursor.getInt(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			
		}
	
			return ono;
	}

	public void updateEnviroSalesRetNo(int ono)
	{
		ContentValues values = new ContentValues();
		values.put("ASalesReturnNo", ono);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro order_no updated "+id);

		} catch(RuntimeException e){

			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void updateEnviroSalesRet1No(int ono)
	{
		ContentValues values = new ContentValues();
		values.put("ASales1ReturnNo", ono);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro order_no updated "+id);

		} catch(RuntimeException e){

			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	public void updateEnviroOrder(int ono)
	{
		ContentValues values = new ContentValues();	
		values.put(DatabaseConnection.COLUMN_ORDER_NO, ono);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro order_no updated "+id);
			
			} catch(RuntimeException e){
				
				System.out.println("+++++++++++++++++++"+e.toString());
			}
	}
	
	public int getMaxOrder1_no()
	{
		int ono=0;
		try{
			String query="select ifnull(max(order1_no),0) as maxorder_no  from enviro ";
//	String query="select reciep_no as reciep_no  from enviro where srep_code ";
		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
//		    rno=cursor.getString(cursor.getColumnIndex("maxreciep_no"));
		    	ono=cursor.getInt(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			
		}
	
			return ono;
	}
	
	public void updateEnviroOrder1(int ono)
	{
		ContentValues values = new ContentValues();	
		values.put(DatabaseConnection.COLUMN_ORDER1_NO, ono);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro order_no updated "+id);
			
			} catch(RuntimeException e){
				
				System.out.println("+++++++++++++++++++"+e.toString());
			}
	}

	public int getMaxDistStock_no()
	{
		int ono=0;
		try{
			String query="select ifnull(max(distStock_no),0) as maxorder_no  from enviro ";
//	String query="select reciep_no as reciep_no  from enviro where srep_code ";
			Cursor cursor =database.rawQuery(query, null);

			if (cursor.getCount()==1)
			{ cursor.moveToLast();
//		    rno=cursor.getString(cursor.getColumnIndex("maxreciep_no"));
				ono=cursor.getInt(0);
				cursor.moveToNext();
			}
			else{
				System.out.println("No records found");

			}
			cursor.close();
		}catch(Exception e)
		{

		}

		return ono;
	}

	public void updateEnviroDistStock(int ono)
	{
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_Dist_Stock_NO, ono);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro order_no updated "+id);

		} catch(RuntimeException e){

			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}



	public int getMaxLeave_no()
	{
		int lno=0;
		try{
			String query="select ifnull(max(LeaveNo),0) as maxleave_no  from enviro ";
//	String query="select reciep_no as reciep_no  from enviro where srep_code ";
		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
     	    lno=cursor.getInt(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			
		}
	
			return lno;
	}
	
	public void updateEnviroLeave(int lno)
	{
		ContentValues values = new ContentValues();	
		values.put(DatabaseConnection.COLUMN_LEAVE_NO, lno);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro leave_no updated "+id);
			
			} catch(RuntimeException e){
				
				System.out.println("+++++++++++++++++++"+e.toString());
			}
	}
	
	public int getMaxBeatPlan_No()
	{
		int bno=0;
		try{
			String query="select ifnull(max(BeatPlanNo),0) as maxbeatplanNo  from enviro ";
//	String query="select reciep_no as reciep_no  from enviro where srep_code ";
		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
     	    bno=cursor.getInt(0);
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
	
			return bno;
	}
	
	public void updateEnviroBeatPlan(int bno)
	{
		ContentValues values = new ContentValues();	
		values.put(DatabaseConnection.COLUMN_BEATPLAN_NO, bno);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro leave_no updated "+id);
			
			} catch(RuntimeException e){
				
				System.out.println("+++++++++++++++++++"+e.toString());
			}
	}	
	
	
	public int getMaxDemo_no()
	{
		int dno=0;
		try{
			String query="select ifnull(max(demo_no),0) as maxdemo_no  from enviro ";
//	String query="select reciep_no as reciep_no  from enviro where srep_code ";
		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
//		    rno=cursor.getString(cursor.getColumnIndex("maxreciep_no"));
		    dno=cursor.getInt(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			
		}
		
	
			return dno;
	}

//	public void updateEnviroTimeStamp(String dno)
//	{
//		ContentValues values = new ContentValues();
//		values.put(DatabaseConnection.COLUMN_TIMESTAMP, dno);
//		try{
//			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
////			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
//			System.out.println("enviro timeStamp updated "+id);
//
//		}
//		catch(RuntimeException e){
//
//			System.out.println("+++++++++++++++++++"+e.toString());
//		}
//	}

	public void updateEnviroDemo(int dno)
	{
		ContentValues values = new ContentValues();	
		values.put(DatabaseConnection.COLUMN_DEMO_NO, dno);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro order_no updated "+id);
			
			} catch(RuntimeException e){
				
				System.out.println("+++++++++++++++++++"+e.toString());
			}
	}
	public int getMaxfailedVisit_no()
	{
		int dno=0;
		try{
			String query="select ifnull(max(FailedVisit_no),0) as maxFailedVisit_no  from enviro ";
//	String query="select reciep_no as reciep_no  from enviro where srep_code ";
		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
//		    rno=cursor.getString(cursor.getColumnIndex("maxreciep_no"));
		    dno=cursor.getInt(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			
		}
		
	
			return dno;
	}
	
	public void updateEnviroFailedVisit(int dno)
	{
		ContentValues values = new ContentValues();	
		values.put(DatabaseConnection.COLUMN_FAILEDVISIT_NO, dno);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro failedvisit_no updated "+id);
			
			} catch(RuntimeException e){
				
				System.out.println("+++++++++++++++++++"+e.toString());
			}
	}
	public int getMaxCompt_no()
	{
		int dno=0;
		try{
			String query="select ifnull(max(Compt_no),0) as maxCompt_no  from enviro ";
//	String query="select reciep_no as reciep_no  from enviro where srep_code ";
		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
//		    rno=cursor.getString(cursor.getColumnIndex("maxreciep_no"));
		    dno=cursor.getInt(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			
		}
		
	
			return dno;
	}
	
	public void updateEnviroCompetitor(int dno)
	{

		ContentValues values = new ContentValues();	
		values.put(DatabaseConnection.COLUMN_COMPTITOR_NO, dno);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro Compt_no updated "+id);

			
			} catch(RuntimeException e){
				
				System.out.println("+++++++++++++++++++"+e.toString());
			}
	}
	
	
	public int getMaxParty_No()
	{
		int dno=0;
		try{
			String query="select ifnull(max(Party_no),0) as maxParty_no  from enviro ";
//	String query="select reciep_no as reciep_no  from enviro where srep_code ";
		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
//		    rno=cursor.getString(cursor.getColumnIndex("maxreciep_no"));
		    dno=cursor.getInt(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			
		}
		
	
			return dno;
	}
	
	public void updateEnviroPartyNo(int dno)
	{
		ContentValues values = new ContentValues();	
		values.put(DatabaseConnection.COLUMN_PARTY_NO, dno);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro Party_no updated "+id);
			
			} catch(RuntimeException e){
				
				System.out.println("+++++++++++++++++++"+e.toString());
			}
	}

	public int getMaxDiscussion_No()
	{
		int dno=0;
		try{
			String query="select ifnull(max(DiscussionNo),0) as maxDiscussionNo  from enviro ";
//	String query="select reciep_no as reciep_no  from enviro where srep_code ";
		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
//		    rno=cursor.getString(cursor.getColumnIndex("maxreciep_no"));
		    dno=cursor.getInt(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			System.out.println("e="+e);
		}
		
	
			return dno;
	}
	
	public void updateEnviroDiscussionNo(int dno)
	{
		ContentValues values = new ContentValues();	
		values.put(DatabaseConnection.COLUMN_DISCUSSION_NO, dno);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro DiscussionNo updated "+id);
			
			} catch(RuntimeException e){
				
				System.out.println("+++++++++++++++++++"+e.toString());
			}
	}
	
	public int getMaxDistCollection_No()
	{
		int dno=0;
		try{
			String query="select ifnull(max(DistColNo),0) as maxDistColNo  from enviro ";
//	String query="select reciep_no as reciep_no  from enviro where srep_code ";
		Cursor cursor =database.rawQuery(query, null);
		
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
//		    rno=cursor.getString(cursor.getColumnIndex("maxreciep_no"));
		    dno=cursor.getInt(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			System.out.println("e="+e);
		}
		
	
			return dno;
	}
	
	public void updateEnviroDistColNo(int dno)
	{
		ContentValues values = new ContentValues();	
		values.put(DatabaseConnection.COLUMN_DIST_COLLEC_NO, dno);
		try{
			long id = database.update(DatabaseConnection.TABLE_ENVIRO, values, null, null);
//			long id= database.insert(DatabaseConnection.TABLE_ENVIRO, null, values);
			System.out.println("enviro DistColNo updated "+id);
			
			} catch(RuntimeException e){
				
				System.out.println("+++++++++++++++++++"+e.toString());
			}
	}
	
	public int CheckRowExist(String srepcode)
	{
		int rcount=0;
		String query="SELECT count(srep_code) AS SmIdCount FROM Enviro WHERE srep_code='"+srepcode+"'";
		System.out.println(query);
		try{
		Cursor cursor =database.rawQuery(query, null);
		 if (cursor.getCount()==1)
		    { cursor.moveToLast(); 
		    rcount=cursor.getInt(0);
		       cursor.moveToNext();
		    }
		 else{
				System.out.println("No records found");
				
			}
			cursor.close();
		}catch(Exception e)
		{
			System.out.println("e="+e);
		}
		if(rcount>0)
		return rcount;
		else
			return rcount;
	}
	
	
}
