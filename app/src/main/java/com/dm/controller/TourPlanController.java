package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.TourPlan;

import java.util.ArrayList;

/**
 * Created by Dataman on 3/17/2017.
 */
public class TourPlanController {

    private DatabaseConnection dbHelper;
    private SQLiteDatabase database;

    public TourPlanController(Context context) {
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


    public void insertTransTourPlan(TourPlan tourPlan) {
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_ID, tourPlan.getTourPlanId());
        values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, tourPlan.getDocId());
        values.put(DatabaseConnection.COLUMN_HEADER_ID, tourPlan.getTourPlanHId());
        values.put(DatabaseConnection.COLUMN_USR_ID, tourPlan.getUserID());
        values.put(DatabaseConnection.COLUMN_VISIT_DATE, tourPlan.getVDate());
        values.put(DatabaseConnection.COLUMN_SREP_CODE, tourPlan.getSMId());
        values.put(DatabaseConnection.COLUMN_CITY_IDS, tourPlan.getMCityID());
        values.put(DatabaseConnection.COLUMN_CITY_NAMES, tourPlan.getMCityName());
        values.put(DatabaseConnection.COLUMN_DISTRIBUTER_IDS, tourPlan.getMDistId());
        values.put(DatabaseConnection.COLUMN_DISTRIBUTER_NAMES, tourPlan.getMDistName());
        values.put(DatabaseConnection.COLUMN_PURPOSE_IDS, tourPlan.getMPurposeId());
        values.put(DatabaseConnection.COLUMN_PURPOSE_NAMES, tourPlan.getMPurposeName());
        values.put(DatabaseConnection.COLUMN_REMARK, tourPlan.getRemarks());
        values.put(DatabaseConnection.COLUMN_APPROVE_BY, tourPlan.getAppBy());
        values.put(DatabaseConnection.COLUMN_APPROVE_BY_SMID, tourPlan.getAppBySMId());
        values.put(DatabaseConnection.COLUMN_APPROVE_REMARK, tourPlan.getAppRemark());
        values.put(DatabaseConnection.COLUMN_APPROVE_STATUS, tourPlan.getAppStatus());
        values.put(DatabaseConnection.COLUMN_UPLOAD, tourPlan.getIsUpload());
        values.put(DatabaseConnection.COLUMN_TIMESTAMP, tourPlan.getCreatedDate());
        try {

            long id = database.insertOrThrow(
                    DatabaseConnection.TABLE_TRANS_TOUR_PLAN, null, values);
            System.out.println("id=" + id);

        } catch (RuntimeException e) {
            System.out.println("+++++++++++++++++++" + e.toString());
        }


    }


    public void updateTransTourPlan(TourPlan tourPlan, int transId) {
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_ID, tourPlan.getTourPlanId());
        values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, tourPlan.getDocId());
        values.put(DatabaseConnection.COLUMN_HEADER_ID, tourPlan.getTourPlanHId());
        values.put(DatabaseConnection.COLUMN_USR_ID, tourPlan.getUserID());
        values.put(DatabaseConnection.COLUMN_VISIT_DATE, tourPlan.getVDate());
        values.put(DatabaseConnection.COLUMN_SREP_CODE, tourPlan.getSMId());
        values.put(DatabaseConnection.COLUMN_CITY_IDS, tourPlan.getMCityID());
        values.put(DatabaseConnection.COLUMN_CITY_NAMES, tourPlan.getMCityName());
        values.put(DatabaseConnection.COLUMN_DISTRIBUTER_IDS, tourPlan.getMDistId());
        values.put(DatabaseConnection.COLUMN_DISTRIBUTER_NAMES, tourPlan.getMDistName());
        values.put(DatabaseConnection.COLUMN_PURPOSE_IDS, tourPlan.getMPurposeId());
        values.put(DatabaseConnection.COLUMN_PURPOSE_NAMES, tourPlan.getMPurposeName());
        values.put(DatabaseConnection.COLUMN_REMARK, tourPlan.getRemarks());
        values.put(DatabaseConnection.COLUMN_APPROVE_BY, tourPlan.getAppBy());
        values.put(DatabaseConnection.COLUMN_APPROVE_BY_SMID, tourPlan.getAppBySMId());
        values.put(DatabaseConnection.COLUMN_APPROVE_REMARK, tourPlan.getAppRemark());
        values.put(DatabaseConnection.COLUMN_APPROVE_STATUS, tourPlan.getAppStatus());
        values.put(DatabaseConnection.COLUMN_UPLOAD, tourPlan.getIsUpload());
        values.put(DatabaseConnection.COLUMN_TIMESTAMP, tourPlan.getCreatedDate());
        try {

           long id = database.update(
                    DatabaseConnection.TABLE_TRANS_TOUR_PLAN,
                    values,
                    DatabaseConnection.COLUMN_PID + "="
                            + transId + "", null);
            System.out.println("id=" + id);

        } catch (RuntimeException e) {
            System.out.println("+++++++++++++++++++" + e.toString());
        }


    }


    public long updatetTourHeaderRemark(String rem,int pid) {
        long id = 0;
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_REMARK, rem);

        try {
            id = database.update(
                    DatabaseConnection.TABLE_TRANS_TOUR_PLAN_HEADER,
                    values,
                    DatabaseConnection.COLUMN_PID + "="
                            + pid + "", null);
            System.out.println("id=" + id);
        } catch (RuntimeException e) {
            System.out.println("+++++++++++++++++++" + e.toString());
        }
return id;
    }
    public void insertTransTourPlanHeader(TourPlan tourPlan) {
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_ID, tourPlan.getTourPlanId());
        values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, tourPlan.getDocId());
        values.put(DatabaseConnection.COLUMN_USR_ID, tourPlan.getUserID());
        values.put(DatabaseConnection.COLUMN_VISIT_DATE, tourPlan.getVDate());
        values.put(DatabaseConnection.COLUMN_SREP_CODE, tourPlan.getSMId());
        values.put(DatabaseConnection.COLUMN_REMARK, tourPlan.getRemarks());
        values.put(DatabaseConnection.COLUMN_APPROVE_BY, tourPlan.getAppBy());
        values.put(DatabaseConnection.COLUMN_APPROVE_BY_SMID, tourPlan.getAppBySMId());
        values.put(DatabaseConnection.COLUMN_APPROVE_REMARK, tourPlan.getAppRemark());
        values.put(DatabaseConnection.COLUMN_APPROVE_STATUS, tourPlan.getAppStatus());
        values.put(DatabaseConnection.COLUMN_FROM_DATE, tourPlan.getFromDate());
        values.put(DatabaseConnection.COLUMN_TO_DATE, tourPlan.getToDate());
        values.put(DatabaseConnection.COLUMN_UPLOAD, tourPlan.getIsUpload());
        values.put(DatabaseConnection.COLUMN_TIMESTAMP, tourPlan.getCreatedDate());
        try {

            long id = database.insertOrThrow(
                    DatabaseConnection.TABLE_TRANS_TOUR_PLAN_HEADER, null, values);
            System.out.println("id=" + id);

        } catch (RuntimeException e) {
            System.out.println("+++++++++++++++++++" + e.toString());
        }


    }

    public int getTourHeaderId(String vdate,String smId) {
int code=0;
        String query = "select ifnull(PK_id,0) from TransTourPlanHeader where visit_date='"+vdate+"' and  srep_code ='"+smId+"'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!(cursor.isAfterLast())) {

                code= cursor.getInt(0);
                cursor.moveToNext();
            }
        } else {
            System.out.println("No records found");
        }
        cursor.close();
        return code;
    }


    public TourPlan getTourHeader(String tourId) {
        TourPlan tourPlan=null;
        String query = "select  PK_id,web_doc_id , usr_id , visit_date , " +
                "srep_code , fromDate , toDate , remark , appBy , appBySmid , appRemark , " +
                "appStatus , isUpload,_id from TransTourPlanHeader where PK_id='"+tourId+"'";
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!(cursor.isAfterLast())) {
                tourPlan=new TourPlan();
                tourPlan.setCode(String.valueOf(cursor.getInt(0)));
                tourPlan.setDocId(cursor.getString(1));
                tourPlan.setUserID(cursor.getString(2));
                tourPlan.setVDate(cursor.getString(3));
                tourPlan.setSMId(cursor.getString(4));
                tourPlan.setFromDate(cursor.getString(5));
                tourPlan.setToDate(cursor.getString(6));
                tourPlan.setRemarks(cursor.getString(7));
                tourPlan.setAppBy(cursor.getString(8));
                tourPlan.setAppBySMId(cursor.getString(9));
                tourPlan.setAppRemark(cursor.getString(10));
                tourPlan.setAppStatus(cursor.getString(11));
                tourPlan.setIsUpload(cursor.getString(12));
                tourPlan.setTourPlanId(cursor.getString(13));
                cursor.moveToNext();
            }
        } else {
            System.out.println("No records found");
        }
        cursor.close();
        return tourPlan;
    }

    public ArrayList<TourPlan> getTransTourPlan(String tourHeaderId) {
        ArrayList<TourPlan> tourPlans=new ArrayList<TourPlan> ();
        String query = "select PK_id , web_doc_id , headerId , usr_id , " +
                "visit_date , srep_code , cityIds , cityNames , distIds ," +
                " distNames , purposeIds , purposeNames , remark , appBy , appBySmid , appRemark , appStatus , isUpload , " +
                "timestamp,_id  from TransTourPlan where headerId='"+tourHeaderId+"'";
        System.out.println("query"+query);
        Cursor cursor = database.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            while (!(cursor.isAfterLast())) {
                TourPlan tourPlan=new TourPlan();
                tourPlan.setCode(String.valueOf(cursor.getInt(0)));
                tourPlan.setDocId(cursor.getString(1));
                tourPlan.setTourPlanHId(cursor.getString(2));
                tourPlan.setUserID(cursor.getString(3));
                tourPlan.setVDate(cursor.getString(4));
                tourPlan.setSMId(cursor.getString(5));
                tourPlan.setMCityID(cursor.getString(6));
                tourPlan.setMCityName(cursor.getString(7));
                tourPlan.setMDistId(cursor.getString(8));
                tourPlan.setMDistNamed(cursor.getString(9));
                tourPlan.setMPurposeId(cursor.getString(10));
                tourPlan.setMPurposeName(cursor.getString(11));
                tourPlan.setRemarks(cursor.getString(12));
                tourPlan.setAppBy(cursor.getString(13));
                tourPlan.setAppBySMId(cursor.getString(14));
                tourPlan.setAppRemark(cursor.getString(15));
                tourPlan.setAppStatus(cursor.getString(16));
                tourPlan.setIsUpload(cursor.getString(17));
                tourPlan.setCreatedDate(cursor.getString(18));
                tourPlan.setTourPlanId(cursor.getString(19));
                tourPlans.add(tourPlan);
                cursor.moveToNext();
            }
        } else {
            System.out.println("No records found");
        }
        cursor.close();
        return tourPlans;
    }

    public TourPlan getTransTourPlanEntry(int transTourId) {
        TourPlan tourPlan=new TourPlan();
        String query = "select PK_id , web_doc_id , headerId , usr_id , " +
                "visit_date , srep_code , cityIds , cityNames , distIds ," +
                " distNames , purposeIds , purposeNames , remark , appBy , appBySmid , appRemark , appStatus , isUpload , " +
                "timestamp,_id  from TransTourPlan where PK_id="+transTourId+"";

        Cursor cursor = database.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            while (!(cursor.isAfterLast())) {

                tourPlan.setCode(String.valueOf(cursor.getInt(0)));
                tourPlan.setDocId(cursor.getString(1));
                tourPlan.setTourPlanHId(cursor.getString(2));
                tourPlan.setUserID(cursor.getString(3));
                tourPlan.setVDate(cursor.getString(4));
                tourPlan.setSMId(cursor.getString(5));
                tourPlan.setMCityID(cursor.getString(6));
                tourPlan.setMCityName(cursor.getString(7));
                tourPlan.setMDistId(cursor.getString(8));
                tourPlan.setMDistNamed(cursor.getString(9));
                tourPlan.setMPurposeId(cursor.getString(10));
                tourPlan.setMPurposeName(cursor.getString(11));
                tourPlan.setRemarks(cursor.getString(12));
                tourPlan.setAppBy(cursor.getString(13));
                tourPlan.setAppBySMId(cursor.getString(14));
                tourPlan.setAppRemark(cursor.getString(15));
                tourPlan.setAppStatus(cursor.getString(16));
                tourPlan.setIsUpload(cursor.getString(17));
                tourPlan.setCreatedDate(cursor.getString(18));
                tourPlan.setTourPlanId(cursor.getString(19));

                cursor.moveToNext();
            }
        } else {
            System.out.println("No records found");
        }
        cursor.close();
        return tourPlan;
    }

    public void deleteTourHeader()
    {
        try{
            database.delete(DatabaseConnection.TABLE_TRANS_TOUR_PLAN_HEADER,null,null );
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public void deleteTransTour() {
        try{
            int s=database.delete(DatabaseConnection.TABLE_TRANS_TOUR_PLAN, null, null);

           }
        catch(Exception exception)
        {
            System.out.println(exception);
        }


    }
}
