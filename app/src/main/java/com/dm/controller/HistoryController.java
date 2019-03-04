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
import com.dm.model.DeleteLogData;
import com.dm.model.History;

import java.util.ArrayList;

public class HistoryController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {DatabaseConnection.COLUMN_CODE,
			DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_ITEM_ID,
			DatabaseConnection.COLUMN_TIMESTAMP,
};
	
	public HistoryController(Context context) {
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

	public void insertDistHistory(History history){
//long ts=DateFunction.ConvertDateToTimestamp(history.getCreatedDate() + " 00:00:00", "dd/MMM/yyyy 00:00:00");
		long ts=System.currentTimeMillis()+19080000 ;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_CODE, (history.getCode()==""?history.getWebcode():history.getCode()));
		values.put(DatabaseConnection.COLUMN_WEB_CODE, history.getWebcode());
		values.put(DatabaseConnection.COLUMN_ITEM_ID, history.getItem_id());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
		values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
		try{
			long id=0,id1=0;
			id = database.insert(DatabaseConnection.TABLE_DIST_ITEM_TEMPLATE, null, values);
			System.out.println(id);
			if(id>0)
			{
				ContentValues values1 = new ContentValues();
				values1.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
				try {
					id = database.update(DatabaseConnection.TABLE_DIST_ITEM_TEMPLATE,
							values1, "code='" +history.getCode() + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}

			}
		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void insertHistory(History history){
//long ts=DateFunction.ConvertDateToTimestamp(history.getCreatedDate() + " 00:00:00", "dd/MMM/yyyy 00:00:00");
		long ts=System.currentTimeMillis()+19080000 ;
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_CODE, (history.getCode()==""?history.getWebcode():history.getCode()));
		values.put(DatabaseConnection.COLUMN_WEB_CODE, history.getWebcode());
		values.put(DatabaseConnection.COLUMN_ITEM_ID, history.getItem_id());

		values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
		values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
		try{
			long id=0,id1=0;
		 id = database.insert(DatabaseConnection.TABLE_HISTORY, null, values);
		System.out.println(id);
			if(id>0)
			{
				ContentValues values1 = new ContentValues();
				values1.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
				try {
					id = database.update(DatabaseConnection.TABLE_HISTORY,
							values1, "code='" +history.getCode() + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}

			}
		}
		catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	//public void updatetItemTempUpload(String partyId, String itemId) {

	/************************		Write By Sandeep Singh 10-04-2017		******************/
	/*****************		START		******************/
	public void updatetItemTempUpload(String partyId, String itemId,String timeStamp) {
		/*****************		END		******************/

		ContentValues values1 = new ContentValues();

		/************************		Write By Sandeep Singh 10-04-2017		******************/
		/*****************		START		******************/
		values1.put(DatabaseConnection.COLUMN_TIMESTAMP, timeStamp);
		/*****************		END		******************/

		values1.put(DatabaseConnection.COLUMN_UPLOAD, "1");

		try {
			long id = database.update(DatabaseConnection.TABLE_HISTORY,
					values1, DatabaseConnection.COLUMN_CODE + "='"
							+ partyId + "' and item_id='"+itemId+"'" , null);
			System.out.println("id=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}

	public void updatetStockItemTempUpload(String distId, String itemId,String timeStamp) {
		ContentValues values1 = new ContentValues();
		values1.put(DatabaseConnection.COLUMN_TIMESTAMP, timeStamp);
		values1.put(DatabaseConnection.COLUMN_UPLOAD, "1");

		try {
			long id = database.update(DatabaseConnection.TABLE_DIST_ITEM_TEMPLATE,
					values1, DatabaseConnection.COLUMN_CODE + "='"
							+ distId + "' and item_id='"+itemId+"'" , null);
			System.out.println("id=" + id);
		} catch (RuntimeException e) {
			System.out.println("+++++++++++++++++++" + e.toString());
		}
	}
	public void insertHistoryFromWeb(String partyId,String andPartyId,String ItemId,String cdate){
String p=(partyId==""?andPartyId:partyId);
		String maxTimeStamp=getItemTimeStamp(partyId,ItemId);
		if ((maxTimeStamp.equals("0.0"))){maxTimeStamp="0";}

			if(Long.parseLong(maxTimeStamp)<Long.parseLong(cdate))
			{
				deleteItemFromHistoryWeb(partyId,ItemId);

				long id=0;
				ContentValues values = new ContentValues();
				values.put(DatabaseConnection.COLUMN_CODE, p);
				values.put(DatabaseConnection.COLUMN_WEB_CODE, andPartyId);
				values.put(DatabaseConnection.COLUMN_ITEM_ID, ItemId);
				values.put(DatabaseConnection.COLUMN_TIMESTAMP, cdate);
				values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
				try{
					 id = database.insert(DatabaseConnection.TABLE_HISTORY, null, values);
					System.out.println(id);
				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
				}

			}
		else if(Long.parseLong(maxTimeStamp)==Long.parseLong(cdate))
			{
//				deleteItemFromHistoryWeb(partyId,andPartyId);

				if(!checkItemExist(partyId,andPartyId,ItemId))
				{

				long id=0;
				ContentValues values = new ContentValues();
				values.put(DatabaseConnection.COLUMN_CODE, p);
				values.put(DatabaseConnection.COLUMN_WEB_CODE, andPartyId);
				values.put(DatabaseConnection.COLUMN_ITEM_ID, ItemId);
				values.put(DatabaseConnection.COLUMN_TIMESTAMP, cdate);
				values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
				try{
					id = database.insert(DatabaseConnection.TABLE_HISTORY, null, values);
					System.out.println(id);
				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
				}

			}
			}


	}

	public void insertStockTemplateFromWeb(String distId,String ItemId,String cdate){

		String maxTimeStamp=getStockItemTimeStamp(distId);
		if ((maxTimeStamp.equals("0.0"))){maxTimeStamp="0";}

		if(Long.parseLong(maxTimeStamp)<Long.parseLong(cdate))
		{
			deleteItemFromStockItemWeb(distId);
			long id=0;
			ContentValues values = new ContentValues();
			values.put(DatabaseConnection.COLUMN_CODE, distId);
			values.put(DatabaseConnection.COLUMN_WEB_CODE, distId);
			values.put(DatabaseConnection.COLUMN_ITEM_ID, ItemId);
			values.put(DatabaseConnection.COLUMN_TIMESTAMP, cdate);
			values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
			try{
				id = database.insert(DatabaseConnection.TABLE_DIST_ITEM_TEMPLATE, null, values);
				System.out.println(id);
			}
			catch(RuntimeException e){
				System.out.println("+++++++++++++++++++"+e.toString());
			}

		}
		else if(Long.parseLong(maxTimeStamp)==Long.parseLong(cdate))
		{
 		if(!checkStockItemExist(distId,ItemId))
			{

				long id=0;
				ContentValues values = new ContentValues();
				values.put(DatabaseConnection.COLUMN_CODE, distId);
				values.put(DatabaseConnection.COLUMN_WEB_CODE, distId);
				values.put(DatabaseConnection.COLUMN_ITEM_ID, ItemId);
				values.put(DatabaseConnection.COLUMN_TIMESTAMP, cdate);
				values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
				try{
					id = database.insert(DatabaseConnection.TABLE_DIST_ITEM_TEMPLATE, null, values);
					System.out.println(id);
				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
				}

			}
		}


	}

	public boolean checkItemExist(String p,String a,String it){
int c=0;
		if(p!=null){
	String qry="select count(*) from history where code='"+p+"' and item_id='"+it+"'";
			Cursor cursor = database.rawQuery(qry, null);
			if(cursor.moveToFirst()){
				while(!(cursor.isAfterLast()))
				{

					c=cursor.getInt(0);
					cursor.moveToNext();
				}
			}else{
				System.out.println("No records found");
			}
			cursor.close();

		}
		else if(a!=null)
		{
			String qry="select count(*) from history where code='"+a+"' and item_id='"+it+"'";
			Cursor cursor = database.rawQuery(qry, null);
			if(cursor.moveToFirst()){
				while(!(cursor.isAfterLast()))
				{					c=cursor.getInt(0);
					cursor.moveToNext();
				}
			}else{
				System.out.println("No records found");
			}
			cursor.close();

		}

if(c>0) {
	return true;

}
		else{
	return false;
}


	}

	public boolean checkStockItemExist(String p,String it){
		int c=0;
		if(p!=null){
			String qry="select count(*) from MastDistItemTemplate where code='"+p+"' and item_id='"+it+"'";
			Cursor cursor = database.rawQuery(qry, null);
			if(cursor.moveToFirst()){
				while(!(cursor.isAfterLast()))
				{	c=cursor.getInt(0);
					cursor.moveToNext();
				}
			}else{
				System.out.println("No records found");
			}
			cursor.close();

		}

		if(c>0) {
			return true;

		}
		else{
			return false;
		}

	}

	public ArrayList<History> getHistoryList(String code){
		Cursor cursor = database.query(DatabaseConnection.TABLE_HISTORY,
				allColumns, DatabaseConnection.COLUMN_CODE+"='"+code+"'", null, null, null, null);
		ArrayList<History> historyArray = new ArrayList<History>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				History history = new History();
				history.setCode(cursor.getString(0));
				history.setWebcode(cursor.getString(1));
				history.setItem_id(cursor.getString(2));
				historyArray.add(history);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return historyArray;
	}

	public ArrayList<History> getHistoryListForUpload(){
//		Cursor cursor = database.query(DatabaseConnection.TABLE_HISTORY,
//				allColumns,DatabaseConnection.COLUMN_UPLOAD+"=0", null, null, null, null);

		String query="select * from history where timestamp=0";
		System.out.println(query);
		Cursor cursor =database.rawQuery(query, null);

		ArrayList<History> historyArray = new ArrayList<History>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				History history = new History();
				history.setCode(cursor.getString(0));
				history.setWebcode(cursor.getString(1));
				history.setItem_id(cursor.getString(2));
				history.setCreatedDate(cursor.getString(5));
				historyArray.add(history);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return historyArray;
	}

	public ArrayList<History> getStockItemTempListForUpload(){
//		Cursor cursor = database.query(DatabaseConnection.TABLE_HISTORY,
//				allColumns,DatabaseConnection.COLUMN_UPLOAD+"=0", null, null, null, null);

		String query="select * from MastDistItemTemplate where timestamp=0";
		System.out.println(query);
		Cursor cursor =database.rawQuery(query, null);

		ArrayList<History> historyArray = new ArrayList<History>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				History history = new History();
				history.setCode(cursor.getString(0));
				history.setWebcode(cursor.getString(1));
				history.setItem_id(cursor.getString(2));
				history.setCreatedDate(cursor.getString(5));
				historyArray.add(history);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return historyArray;
	}

	public ArrayList<History> getStockItemListForUpload(){


		String query="select * from MastDistItemTemplate where isUpload=0";
		System.out.println(query);
		Cursor cursor =database.rawQuery(query, null);

		ArrayList<History> historyArray = new ArrayList<History>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				History history = new History();
				history.setCode(cursor.getString(0));
				history.setWebcode(cursor.getString(1));
				history.setItem_id(cursor.getString(2));
				history.setCreatedDate(cursor.getString(5));
				historyArray.add(history);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return historyArray;
	}
	public String getItemTimeStamp(String partyId, String itemId)
	{
		String maxTimeStamp1="0";
		try{
			String query="select ifnull(max(timestamp),0) as maxtimestamp  from history where code='"+partyId+"' AND item_id='"+itemId+"'";
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
	public String getStockItemTimeStamp(String distId)
	{
		String maxTimeStamp1="0";
		try{
			String query="select ifnull(max(timestamp),0) as maxtimestamp  from MastDistItemTemplate where code='"+distId+"'";
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
	
	public ArrayList<History> getHistoryList(String code, String aCode){
		Cursor cursor=null;
		ArrayList<History> historyArray = new ArrayList<History>();
		if(!code.equals("")){
		 cursor = database.query(DatabaseConnection.TABLE_HISTORY,
				allColumns, DatabaseConnection.COLUMN_CODE+"='"+code+"'", null, null, null, null);
		}
		else if(!aCode.equals(""))
		{
			
			 cursor = database.query(DatabaseConnection.TABLE_HISTORY,
						allColumns, DatabaseConnection.COLUMN_WEB_CODE+"='"+aCode+"'", null, null, null, null);
			
		}
		try{
		
		
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				History history = new History();
				history.setCode(cursor.getString(0));
				history.setWebcode(cursor.getString(1));
				history.setItem_id(cursor.getString(2));
				historyArray.add(history);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		cursor.close();
		
		
		return historyArray;
	}


	public ArrayList<DeleteLogData> getDeleteLogData()
	{
		ArrayList<DeleteLogData> deleteLogDatas = new ArrayList<>();
		Cursor cursor = database.query(DatabaseConnection.TABLE_DELETE_LOG,null,null, null, null, null, null);
		try{


			if(cursor.moveToFirst()){
				while(!(cursor.isAfterLast()))
				{
					DeleteLogData history = new DeleteLogData();
					history.setEntityName(cursor.getString(1));
					history.setEntityId(cursor.getString(2));
					deleteLogDatas.add(history);
					cursor.moveToNext();
				}
			}else{
				System.out.println("No records found");
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		return deleteLogDatas;
	}
	public ArrayList<History> getDistHistoryList(String code){
		Cursor cursor=null;
		ArrayList<History> historyArray = new ArrayList<History>();
		if(!code.equals("")){
			cursor = database.query(DatabaseConnection.TABLE_HISTORY,
					allColumns, DatabaseConnection.COLUMN_CODE+"='"+code+"'", null, null, null, null);
		}

		try{


			if(cursor.moveToFirst()){
				while(!(cursor.isAfterLast()))
				{
					History history = new History();
					history.setCode(cursor.getString(0));
					history.setWebcode(cursor.getString(1));
					history.setItem_id(cursor.getString(2));
					historyArray.add(history);
					cursor.moveToNext();
				}
			}else{
				System.out.println("No records found");
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		cursor.close();
		return historyArray;
	}

	public void deleteItemFromHistory(String partyCode,String ApartyCode,
			String itemId,String d) {
		int i=0;
//		long ts=DateFunction.ConvertDateToTimestamp(d + " 00:00:00", "dd/MMM/yyyy 00:00:00");
//		long ts=System.currentTimeMillis();
		long ts=System.currentTimeMillis()+19080000 ;
		
		if(partyCode!=null){
		
		 i = database.delete(DatabaseConnection.TABLE_HISTORY, "item_id='" + itemId
				+ "' AND code='" + partyCode + "'", null);
		System.out
				.println("history deleted " + " result=" + i);

			if(i>0)
			{
				ContentValues values1 = new ContentValues();
				values1.put(DatabaseConnection.COLUMN_TIMESTAMP, ts);
				values1.put(DatabaseConnection.COLUMN_UPLOAD, "0");

				try {
					i = database.update(DatabaseConnection.TABLE_HISTORY,
							values1, "code='" +partyCode + "'", null);
					System.out.println("row=" + i);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}

			}

		}
		else if(ApartyCode!=null)
		{
			 i = database.delete(DatabaseConnection.TABLE_HISTORY, "item_id='" + itemId
					+ "' AND webcode='" + ApartyCode + "'", null);
			System.out
					.println("history deleted " + " result=" + i);

			if(i>0)
			{
				ContentValues values1 = new ContentValues();

				values1.put(DatabaseConnection.COLUMN_TIMESTAMP, ts);
				values1.put(DatabaseConnection.COLUMN_UPLOAD, "0");
				try {
					i = database.update(DatabaseConnection.TABLE_HISTORY,
							values1, "code='" +ApartyCode + "'", null);
					System.out.println("row=" + i);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}

			}

		}



	}

	public void deleteItemFromDistHistory(String distId, String itemId) {
		int i=0;
//		long ts=DateFunction.ConvertDateToTimestamp(d + " 00:00:00", "dd/MMM/yyyy 00:00:00");
//		long ts=System.currentTimeMillis();
		long ts=System.currentTimeMillis()+19080000 ;

		if(distId!=null){

			i = database.delete(DatabaseConnection.TABLE_DIST_ITEM_TEMPLATE, "item_id='" + itemId
					+ "' AND code='" + distId + "'", null);
			System.out.println("history deleted " + " result=" + i);
			if(i>0)
			{
				ContentValues values1 = new ContentValues();
				values1.put(DatabaseConnection.COLUMN_TIMESTAMP, ts);
				values1.put(DatabaseConnection.COLUMN_UPLOAD, "0");

				try {
					i = database.update(DatabaseConnection.TABLE_DIST_ITEM_TEMPLATE,
							values1, "code='" +distId + "'", null);
					System.out.println("row=" + i);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}

			}

		}
	}

	public void deleteItemFromHistoryWeb(String partyCode,String ApartyCode) {
		int i=0;

		if(partyCode!=null){

			i = database.delete(DatabaseConnection.TABLE_HISTORY, "code='" + partyCode + "' AND item_id='"+ApartyCode+"'", null);
			System.out
					.println("history deleted " + " result=" + i);

		}
		else if(ApartyCode!=null)
		{
			i = database.delete(DatabaseConnection.TABLE_HISTORY,"webcode='" + ApartyCode + "'", null);
			System.out
					.println("history deleted " + " result=" + i);

		}



	}
	public void deleteItemFromStockItemWeb(String distId) {
		int i=0;
		if(distId!=null){

			i = database.delete(DatabaseConnection.TABLE_DIST_ITEM_TEMPLATE, "code='" + distId + "'", null);
			System.out
					.println("history deleted " + " result=" + i);
		}
	}


}
