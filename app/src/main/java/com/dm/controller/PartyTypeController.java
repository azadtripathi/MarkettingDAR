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
import com.dm.model.PartyType;

import java.util.ArrayList;

public class PartyTypeController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME,
			};
	
	public PartyTypeController(Context context) {
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
	
	public void insertArea(PartyType partyType){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, partyType.getId());
		values.put(DatabaseConnection.COLUMN_NAME, partyType.getName());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, partyType.getCreatedDate());
		try{

			Cursor c = database.query(DatabaseConnection.TABLE_INDUSTRYMAST,null,"webcode=?",new String[]{partyType.getId()},null,null,null);
			if(c.getCount()>0)
			{
				long id = database.update(DatabaseConnection.TABLE_PARTYTYPEMAST, values, "webcode=?",new String[]{partyType.getId()});
				System.out.println(id);
			}
			else {
				long id = database.insert(DatabaseConnection.TABLE_PARTYTYPEMAST, null, values);
				System.out.println(id);
			}

		//long id = database.insert(DatabaseConnection.TABLE_PARTYTYPEMAST, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}
	public ArrayList<PartyType> getPartyTypes(){
		String orderby="name";
		Cursor cursor = database.query(DatabaseConnection.TABLE_PARTYTYPEMAST,
				allColumns, null, null, null, null, orderby);
		ArrayList<PartyType> partyTypes = new ArrayList<PartyType>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				PartyType partyType = new PartyType();
				partyType.setId(cursor.getString(0));
				partyType.setName(cursor.getString(1));
				partyTypes.add(partyType);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return partyTypes;
	}
	
	
	
}
