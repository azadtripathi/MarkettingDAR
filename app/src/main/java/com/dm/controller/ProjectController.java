package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.Project;

import java.util.ArrayList;


public class ProjectController {

	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {	
			DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_ACTIVE
	};
	
	public ProjectController(Context context) {
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
	
	public void insertProject(Project project){
		ContentValues values = new ContentValues();
		values.put(DatabaseConnection.COLUMN_WEB_CODE, project.getProjectid());
		values.put(DatabaseConnection.COLUMN_NAME, project.getProjectname());
		values.put(DatabaseConnection.COLUMN_ACTIVE, project.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, project.getCreatedDate());
				try{
		long id = database.insert(DatabaseConnection.TABLE_PROJECT, null, values);
		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}	

	public ArrayList<Project> getProjectNameList()
	{
		ArrayList<Project> projectnameList=new ArrayList<Project>();
		String query="select webcode,name from mastProject";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				Project project=new Project();
			project.setProjectid(cursor.getString(0));
			project.setProjectname(cursor.getString(1));
			projectnameList.add(project);
				cursor.moveToNext();
			}
		}
		else{
			
			System.out.println("No records found");
		}
		cursor.close();
		return projectnameList;
	}
	
}
