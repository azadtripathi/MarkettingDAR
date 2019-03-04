package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;

import java.util.ArrayList;

/**
 * Created by Dataman on 1/20/2017.
 */
public class VisitNameController {

    private DatabaseConnection dbHelper;
    private SQLiteDatabase database;


    public VisitNameController(Context context) {
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

    public void insertVisit(String c){
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_NAME, c);
        try{
            long id = database.insert(DatabaseConnection.TABLE_VISIT_CODE, null, values);
            System.out.println(id);
        } catch(RuntimeException e){
            System.out.println("+++++++++++++++++++"+e.toString());
        }
    }

    public ArrayList<String> getVisitNameList(){
        String qry=" select * from MastVisitCode order by name";
        Cursor cursor = database.rawQuery(qry, null);
        ArrayList<String> visitArray = new ArrayList<String>();
        visitArray.add("Select");
        if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast()))
            {
                visitArray.add(cursor.getString(1).toUpperCase());
                cursor.moveToNext();
            }
        }else{
            System.out.println("No records found");
        }
        cursor.close();
        return visitArray;
    }


}
