package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.library.DateFunction;
import com.dm.model.Order;
import com.dm.model.SalesReturn;
import com.dm.util.Util;

import java.util.ArrayList;

/**
 * Created by Dataman on 8/29/2017.
 */

public class SalesReturnController
{
    Context mContext;
    private DatabaseConnection dbHelper;
    private SQLiteDatabase database;
    boolean mTrans = false;
    private String[] allColumns = {
            DatabaseConnection.COLUMN_ORDER_NO,
            DatabaseConnection.COLUMN_VISIT_NO,
            DatabaseConnection.COLUMN_WEB_DOC_ID,
            DatabaseConnection.COLUMN_ANDROID_DOCID,
            DatabaseConnection.COLUMN_USR_ID,
            DatabaseConnection.COLUMN_VISIT_DATE,
            DatabaseConnection.COLUMN_SREP_CODE,
            DatabaseConnection.COLUMN_PARTY_CODE,
            DatabaseConnection.COLUMN_AREA_CODE,
            DatabaseConnection.COLUMN_REMARK,
            DatabaseConnection.COLUMN_AMOUNT,
            DatabaseConnection.COLUMN_STATUS,
            DatabaseConnection.COLUMN_MEET_FLAG,
            DatabaseConnection.COLUMN_MEET_DOCID,
            DatabaseConnection.COLUMN_ORDER_TYPE

    };




    public ArrayList<Order> getUploadMainSalesList(String vdate){
        /************************		Write By Sandeep Singh 13-04-2017		******************/
        /*****************		START		******************/
        /************************		Write By Sandeep Singh 20-04-2017		******************/
        /*****************		START		******************/
        String query="select o.Android_id,o.usr_id,o.visit_date,o.srep_code,o.party_code," +
                "o.area_code,o.remark,o.amount,vl1.visit_no, ifnull(o.latitude,0) as latitude,ifnull(o.longitude,0) as longitude,ifnull(o.LatlngTime,0) as LatlngTime"+
                " from "+DatabaseConnection.TABLE_SALES_RETURN+" o"+
                " LEFT JOIN Visitl1 vl1"+
                " ON o.visit_date = vl1.visit_date"+
                " LEFT JOIN mastParty p"+
                " ON o.party_code = p.webcode"+
                " WHERE "+
                " o.timestamp =0 AND o.visit_date = '"+vdate+"'";
	/*	String query="select o.Android_id,o.usr_id,o.visit_date,o.srep_code,o.party_code," +
				"o.area_code,o.remark,o.amount,vl1.visit_no"+
				" from TransOrder o"+
				" LEFT JOIN Visitl1 vl1"+
				" ON o.visit_date = vl1.visit_date"+
				" LEFT JOIN mastParty p"+
				" ON o.party_code = p.webcode"+
				" WHERE  vl1.dsr_lock = '1'"+
				" AND o.timestamp =0 AND o.visit_date = '"+vdate+"'";*/
        /*****************		END		******************/

	/*	String query="select o.Android_id,o.usr_id,o.visit_date,o.srep_code,o.party_code," +
				"o.area_code,o.remark,o.amount,vl1.visit_no"+
				" from TransOrder o"+
				" LEFT JOIN Visitl1 vl1"+
				" ON o.visit_date = vl1.visit_date"+
				" LEFT JOIN mastParty p"+
				" ON o.party_code = p.webcode"+
				" WHERE  vl1.dsr_lock = '1'"+
				" AND o.isSubUpload = '0' AND o.visit_date = '"+vdate+"'";*/
        /*****************		END		******************/
	/*	String query="select o.Android_id,o.usr_id,o.visit_date,o.srep_code,o.party_code," +
				"o.area_code,o.remark,o.amount,vl1.visit_no"+
				" from TransOrder o"+
				" LEFT JOIN Visitl1 vl1"+
				" ON o.visit_date = vl1.visit_date"+
				" LEFT JOIN mastParty p"+
				" ON o.party_code = p.webcode"+
				" WHERE  vl1.dsr_lock = '1'"+
				" AND o.isSubUpload = '0' AND o.visit_date = '"+vdate+"'";*/
        /*****************		END		******************/
        System.out.println(query);
        Cursor cursor=database.rawQuery(query, null);
        ArrayList<Order> orders = new ArrayList<Order>();
        if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast()))
            {
                Order order=new Order();
                order.setAndroid_id(cursor.getString(0));
                order.setUserId(cursor.getString(1));
                order.setVDate(cursor.getString(2));
                order.setSMId(cursor.getString(3));
                order.setPartyId(cursor.getString(4));
                order.setAreaId(cursor.getString(5));
                order.setRemarks(cursor.getString(6));
                order.setOrderAmount(cursor.getString(7));
                order.setVisId(cursor.getString(8));
                /************************		Write By Sandeep Singh 20-04-2017		******************/
                /*****************		START		******************/
                order.setLatitude(Util.validateNumric(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT))));
                order.setLongitude(Util.validateNumric(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LNG))));
                order.setLatlngTime(Util.validateNumric(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT_LNG_TIME))));
                /*****************		END		******************/
                orders.add(order);
                cursor.moveToNext();
            }
        }else{
            System.out.println("No records found");
        }
        cursor.close();
        return orders;
    }


    public SalesReturnController(Context context) {
        mContext = context;
        dbHelper = new DatabaseConnection(context);
    }
    public void open() {
        try{
            database = dbHelper.getWritableDatabase();
        }catch(SQLException e){
            System.out.println("-----------------"+e.getMessage());
        }
    }

    public void close()
    {
        dbHelper.close();
    }

    public long insertSalesRturn(SalesReturn sales)
    {
        long id = 0;
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_SALES_RETURN_ID, sales.getSalesRetNo());
        values.put(DatabaseConnection.COLUMN_VISIT_NO, sales.getVisit_no());
        values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, sales.getSalesDocId());
        values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, sales.getAndroidId());
        values.put(DatabaseConnection.COLUMN_USR_ID,sales.getUserId());
        values.put(DatabaseConnection.COLUMN_VISIT_DATE,sales.getvDate());
        values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE,sales.getAndPartyCode());
        values.put(DatabaseConnection.COLUMN_PARTY_CODE,sales.getPartyId());
        values.put(DatabaseConnection.COLUMN_Smid,sales.getSmid());
        values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(sales.getvDate()));
        values.put(DatabaseConnection.COLUMN_TIMESTAMP,"0");
        values.put(DatabaseConnection.COLUMN_LAT,sales.getLatitude());
        values.put(DatabaseConnection.COLUMN_LNG,sales.getLongitude());
        values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,sales.getLat_long_dt());
        values.put(DatabaseConnection.COLUMN_DSRLOCK,"0");
        values.put(DatabaseConnection.COLUMN_AREA_CODE,sales.getAreaId());
        try
        {
            id = database.insert(DatabaseConnection.TABLE_SALES_RETURN, null, values);
        }
        catch(RuntimeException e)
        {
            System.out.println("+++++++++++++++++++"+e.toString());
        }

        return id;


    }



    public long insertSalesRturn(Order order){

        mTrans = true;
        database.beginTransaction();
        long id=0;
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_ORDER_NO, order.getOrdId());
        values.put(DatabaseConnection.COLUMN_VISIT_NO, order.getVisId());
        values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, order.getOrdDocId());
        values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, order.getAndroid_id());
        values.put(DatabaseConnection.COLUMN_USR_ID,order.getUserId());
        values.put(DatabaseConnection.COLUMN_VISIT_DATE,order.getVDate());
        values.put(DatabaseConnection.COLUMN_SREP_CODE,order.getSMId());
        values.put(DatabaseConnection.COLUMN_PARTY_CODE,order.getPartyId());
        values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE,order.getAndPartyId());
        values.put(DatabaseConnection.COLUMN_AREA_CODE,order.getAreaId());
        values.put(DatabaseConnection.COLUMN_REMARK,order.getRemarks());
        values.put(DatabaseConnection.COLUMN_AMOUNT,order.getOrderAmount());
        values.put(DatabaseConnection.COLUMN_STATUS,order.getOrderStatus());
        values.put(DatabaseConnection.COLUMN_MEET_FLAG,order.getMeetFlag());
        values.put(DatabaseConnection.COLUMN_MEET_DOCID,order.getMeetDocId());
        values.put(DatabaseConnection.COLUMN_ORDER_TYPE,order.getOrderType());
        values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(order.getVDate()));
        if(order.getIsOrderImport())
        {
            values.put(DatabaseConnection.COLUMN_isSubUPLOAD, "1");
            values.put(DatabaseConnection.COLUMN_TIMESTAMP,0);
        }
        else{
            values.put(DatabaseConnection.COLUMN_isSubUPLOAD, "0");
            values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
        }

        values.put(DatabaseConnection.COLUMN_LAT,order.getLatitude());
        values.put(DatabaseConnection.COLUMN_LNG,order.getLongitude());
        values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,order.getLatlngTime());

        System.out.println("row no "+ order.getOrdDocId()+" inserted");
        try{


            id = database.insert(DatabaseConnection.TABLE_SALES_RETURN, null, values);
            database.setTransactionSuccessful();
            database.endTransaction();
            mTrans = false;
        } catch(RuntimeException e)
        {
            if(mTrans)
            {
                database.endTransaction();
                mTrans = false;
            }
            System.out.println("+++++++++++++++++++"+e.toString());
        }
        return id;
    }



    public long updateOrder(String headerId,Order order)
    {
        long id = 0;
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_ORDER_NO, order.getOrdId());
        values.put(DatabaseConnection.COLUMN_VISIT_NO, order.getVisId());
        values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, order.getOrdDocId());
        values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, order.getAndroid_id());
        values.put(DatabaseConnection.COLUMN_USR_ID,order.getUserId());
        values.put(DatabaseConnection.COLUMN_VISIT_DATE,order.getVDate());
        values.put(DatabaseConnection.COLUMN_SREP_CODE,order.getSMId());
        values.put(DatabaseConnection.COLUMN_PARTY_CODE,order.getPartyId());
        values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE,order.getAndPartyId());
        values.put(DatabaseConnection.COLUMN_AREA_CODE,order.getAreaId());
        values.put(DatabaseConnection.COLUMN_REMARK,order.getRemarks());
        values.put(DatabaseConnection.COLUMN_AMOUNT,order.getOrderAmount());
        values.put(DatabaseConnection.COLUMN_STATUS,order.getOrderStatus());
        values.put(DatabaseConnection.COLUMN_MEET_FLAG,order.getMeetFlag());
        values.put(DatabaseConnection.COLUMN_MEET_DOCID,order.getMeetDocId());
        values.put(DatabaseConnection.COLUMN_ORDER_TYPE,order.getOrderType());
        values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(order.getVDate()));
        if(order.getIsOrderImport())
        {
            values.put(DatabaseConnection.COLUMN_isSubUPLOAD, "1");
            values.put(DatabaseConnection.COLUMN_TIMESTAMP,0);

        }
        else{
            values.put(DatabaseConnection.COLUMN_isSubUPLOAD, "0");
            values.put(DatabaseConnection.COLUMN_TIMESTAMP, 0);
        }

        values.put(DatabaseConnection.COLUMN_LAT,order.getLatitude());
        values.put(DatabaseConnection.COLUMN_LNG,order.getLongitude());
        values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,order.getLatlngTime());

        try{
            id = database.update(DatabaseConnection.TABLE_SALES_RETURN, values, DatabaseConnection.COLUMN_ANDROID_DOCID+"='"+headerId+"'", null);
            System.out.println(id);
        }
        catch(RuntimeException e){
            System.out.println("+++++++++++++++++++"+e.toString());
        }
        return id;
    }
    public void updateOrder(Order order)
    {
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_REMARK,order.getRemarks());
        values.put(DatabaseConnection.COLUMN_AMOUNT,order.getOrderAmount());
        values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");

        try{
            long id = database.update(DatabaseConnection.TABLE_ORDER, values, DatabaseConnection.COLUMN_ANDROID_DOCID+"='"+order.getAndroid_id()+"'", null);
            System.out.println(id);
        }
        catch(RuntimeException e){
            System.out.println("+++++++++++++++++++"+e.toString());
        }
//
//	String query="update TransOrder set amount='"+order.getOrderAmount()+"' , remark='"+order.getRemarks()+"' where Android_id='"+order.getAndroid_id()+"'";
//		System.out.println(query);
//	try {
//		// String
//		// qurey="UPDATE order_sub SET remark="+""+" WHERE ExecutionDate = '2011-10-12 00:00:00.000'";
//
//		database.execSQL(query);
//		System.out.println(" Order updated");
//	} catch (RuntimeException e) {
//		System.out.println("+++++++++++++++++++" + e.toString());
//	}

    }


    public boolean isAndroidIdExist(String android_doc_id)
    {
        Cursor c = database.query(DatabaseConnection.TABLE_SALES_RETURN,null,"Android_id='" +android_doc_id + "'",null,null,null,null);
        if(c.getCount()>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }




    public ArrayList<SalesReturn> getUploadMainList(String vDate)
    {


        String query="select o.Android_id,o.usr_id,o.visit_date,o.party_code," +
                "o.area_code,vl1.visit_no,o.Smid, ifnull(o.latitude,0) as latitude,ifnull(o.longitude,0) as longitude,ifnull(o.LatlngTime,0) as LatlngTime"+
                " from "+DatabaseConnection.TABLE_SALES_RETURN+" o"+
                " LEFT JOIN Visitl1 vl1"+
                " ON o.visit_date = vl1.visit_date"+
                " LEFT JOIN mastParty p"+
                " ON o.party_code = p.webcode"+
                " WHERE "+
                " o.timestamp =0 AND o.visit_date = '"+vDate+"'";

        Cursor cursor= database.rawQuery(query,null);
                //database.query(DatabaseConnection.TABLE_SALES_RETURN,null,"visit_date=?",new String[]{vDate},null,null,null,null);
        ArrayList<SalesReturn> orders = new ArrayList<SalesReturn>();
        while(cursor.moveToNext())
        {

            SalesReturn sales = new SalesReturn();

            sales.setAndroidId(cursor.getString(0));
            sales.setUserId(cursor.getString(1));
            sales.setvDate(cursor.getString(2));

            sales.setPartyId(cursor.getString(3));
            sales.setAreaId(cursor.getString(4));
            sales.setVisit_no(cursor.getString(5));

            sales.setSmid(cursor.getString(6));


            sales.setLatitude(cursor.getString(7));
            sales.setLongitude(cursor.getString(8));
            sales.setLat_long_dt(cursor.getString(9));



            orders.add(sales);

        }


        return orders;
    }

    public  boolean updateMultipleOrderUpload(String androiddocId, String webdocId,int ono,int vId,String timeStamp){
        /*****************		END		******************/
        ContentValues values = new ContentValues();
        boolean status=false;
        long id=0,id1=0;
        values.put(DatabaseConnection.COLUMN_isSubUPLOAD,"1");
        values.put(DatabaseConnection.COLUMN_WEB_DOC_ID,webdocId);
        values.put(DatabaseConnection.COLUMN_ORDER_NO,ono);
        values.put(DatabaseConnection.COLUMN_VISIT_NO,vId);

        /************************		Write By Sandeep Singh 10-04-2017		******************/
        /*****************		START		******************/
        values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
        /*****************		END		******************/
        try{
            id = database.update(DatabaseConnection.TABLE_SALES_RETURN, values, "Android_id='"+androiddocId+"'", null);
            System.out.println("row="+id);
            if(id>0)
            {

                ContentValues values1 = new ContentValues();
                values1.put(DatabaseConnection.COLUMN_WEB_DOC_ID,webdocId);
                values1.put(DatabaseConnection.COLUMN_ORDER1_NO,ono);
                id1 = database.update(DatabaseConnection.TABLE_SALES_RETURN1, values1, "Android_id='"+androiddocId+"'", null);
                System.out.println("row="+id1);
            }


        }
        catch(RuntimeException e){
            System.out.println("+++++++++++++++++++"+e.toString());
        }
        if(id>0 && id1>0)
        {status=false;}else{status=true;}
        return status;
    }


    /*public  boolean updateMultipleOrderUpload(String androiddocId, String webdocId,int ono,int vId,String timeStamp){
        *//*****************		END		******************//*
        ContentValues values = new ContentValues();
        boolean status=false;
        long id=0,id1=0;

        values.put(DatabaseConnection.COLUMN_WEB_DOC_ID,webdocId);
        values.put(DatabaseConnection.COLUMN_SALES_RETURN_ID,ono);
        values.put(DatabaseConnection.COLUMN_VISIT_NO,vId);

        *//************************		Write By Sandeep Singh 10-04-2017		******************//*
        *//*****************		START		******************//*
        values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
        *//*****************		END		******************//*
        try{
            id = database.update(DatabaseConnection.TABLE_SALES_RETURN, values, "Android_id='"+androiddocId+"'", null);
            System.out.println("row="+id);
            if(id>0)
            {

                ContentValues values1 = new ContentValues();
                values1.put(DatabaseConnection.COLUMN_WEB_DOC_ID,webdocId);
                values1.put(DatabaseConnection.COLUMN_SALES_RETURN_ID,ono);
                id1 = database.update(DatabaseConnection.TABLE_SALES_RETURN1, values1, "Android_id='"+androiddocId+"'", null);
                System.out.println("row="+id1);
            }


        }
        catch(RuntimeException e){
            System.out.println("+++++++++++++++++++"+e.toString());
        }
        if(id>0 && id1>0)
        {status=false;}else{status=true;}
        return status;
    }*/











}
