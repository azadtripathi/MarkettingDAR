package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.library.DateFunction;
import com.dm.model.Order1;
import com.dm.model.SalesReturn;
import com.dm.model.SalesReturn1;

import java.util.ArrayList;

/**
 * Created by Dataman on 8/29/2017.
 */

public class SalesReturnController1
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

    public SalesReturnController1(Context context)
    {
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




    public ArrayList<Order1> getUploadOrder1List(String vdate){
        /************************		Write By Sandeep Singh 20-04-2017		******************/
        /*****************		START		******************/
        String query="select o.Ord1Android_id," +
                "o.order1_no," +
                "vl1.visit_no," +
                "o.web_doc_id," +
                "o.sno,o.usr_id," +
                "o.visit_date," +
                "o.srep_code," +
                "o.party_code," +
                "o.area_code," +
                "o.product_code," +
                "o.qty," +
                "o.rate," +
                "o.amount," +
                "o.remark," +
                "o.Android_id," +
                "ifnull(o.latitude,0) as latitude," +
                "ifnull(o.longitude,0) as longitude," +
                "ifnull(o.LatlngTime,0) as LatlngTime"+
                ",o._case," +
                "o.Unit,o.Batch_No,o.MFD_Date " +
                "from "+DatabaseConnection.TABLE_SALES_RETURN1+" o"+
                " LEFT JOIN Visitl1 vl1"+
                " ON o.visit_date = vl1.visit_date"+
                " LEFT JOIN mastParty p"+
                " ON o.party_code = p.webcode"+
                " WHERE  "+
                " o.timestamp = '0' AND o.visit_date = '"+vdate+"'";

	/*	String query="select o.Ord1Android_id,o.order_no," +
				"vl1.visit_no,o.web_doc_id,o.sno,o.usr_id,o.visit_date,o.srep_code,o.party_code," +
				"o.area_code,o.product_code,o.qty,o.rate,o.amount,o.remark,o.Android_id"+
				" from TransOrder1 o"+
				" LEFT JOIN Visitl1 vl1"+
				" ON o.visit_date = vl1.visit_date"+
				" LEFT JOIN mastParty p"+
				" ON o.party_code = p.webcode"+
				" WHERE  vl1.dsr_lock = '1'"+
				" AND o. isUpload = '0' AND o.visit_date = '"+vdate+"'";*/
        /*****************		END		******************/
        System.out.println(query);

        System.out.println(query);
        Cursor cursor=database.rawQuery(query, null);
        ArrayList<Order1> orders1 = new ArrayList<Order1>();
        if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast()))
            {
                Order1 order1=new Order1();
                order1.setAndroid_Id(cursor.getString(0));
                order1.setOrdId(cursor.getString(1));
                order1.setVisId(cursor.getString(2));
                order1.setOrdDocId(cursor.getString(3));
                order1.setSno(cursor.getString(4));
                order1.setUserId(cursor.getString(5));
                order1.setVDate(cursor.getString(6));
                order1.setSMId(cursor.getString(7));
                order1.setPartyId(cursor.getString(8));
                order1.setAreaId(cursor.getString(9));
                order1.setItemId(cursor.getString(10));
                order1.setQty(cursor.getString(11));
                order1.setRate(cursor.getString(12));
                order1.setAmount(cursor.getString(13));
                order1.setRemarks(cursor.getString(14));
                order1.setOrderAndroid_Id(cursor.getString(15));
                order1.setCases(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_CASES)));
                order1.setUnit(cursor.getString(cursor.getColumnIndex("qty")));
                order1.setBatchNo(cursor.getString(cursor.getColumnIndex("Batch_No")));
                order1.setMfd(cursor.getString(cursor.getColumnIndex("MFD_Date")));
                /************************		Write By Sandeep Singh 20-04-2017		******************/
                /*****************		START		******************/
                order1.setLatitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT)));
                order1.setLongitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LNG)));
                order1.setLatlngTimeStamp(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT_LNG_TIME)));
                /*****************		END		******************/
                orders1.add(order1);
                cursor.moveToNext();
            }
        }else{
            System.out.println("No records found");
        }
        cursor.close();
        return orders1;
    }

    public  long updateOrder1Upload(String androiddocId,String timeStamp){
        /*****************		END		******************/
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_UPLOAD,"1");
        /************************		Write By Sandeep Singh 10-04-2017		******************/
        /*****************		START		******************/
        values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);

        /*****************		END		******************/
        long id=0;
//		values.put(DatabaseConnection.COLUMN_PURCHASE_ORDER1ID,ono);

//		values.put(DatabaseConnection.COLUMN_VISIT_NO,vId);
        try
        {
            id = database.update(DatabaseConnection.TABLE_SALES_RETURN1, values, "Ord1Android_id='"+androiddocId+"'", null);
            System.out.println("row="+id);

        }
        catch(RuntimeException e){
            System.out.println("+++++++++++++++++++"+e.toString());
        }
        return id;
    }

    public void close()
    {
        dbHelper.close();
    }

    public long insertOrder1(Order1 order1){
        database.beginTransaction();
        long id=0;
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_ORDER1_NO, order1.getOrd1Id());
        values.put(DatabaseConnection.COLUMN_VISIT_NO, order1.getVisId());
        values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, order1.getOrdDocId());

        values.put(DatabaseConnection.COLUMN_ANDROID_DOCID       ,order1.getOrderAndroid_Id());
        values.put(DatabaseConnection.COLUMN_ORDER1_ANDROID_DOCID,order1.getAndroid_Id());



        values.put(DatabaseConnection.COLUMN_SERIAL_NO,order1.getSno());
        values.put(DatabaseConnection.COLUMN_USR_ID,order1.getUserId());
        values.put(DatabaseConnection.COLUMN_VISIT_DATE,order1.getVDate());
        values.put(DatabaseConnection.COLUMN_SREP_CODE,order1.getSMId());
        values.put(DatabaseConnection.COLUMN_PARTY_CODE,order1.getPartyId());
        values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE,order1.getAndPartyId());
        values.put(DatabaseConnection.COLUMN_AREA_CODE,order1.getAreaId());
        values.put(DatabaseConnection.COLUMN_BEAT_CODE,order1.getBeatId());
        values.put(DatabaseConnection.COLUMN_PRODUCT_CODE,order1.getItemId());
        values.put(DatabaseConnection.COLUMN_QTY,order1.getQty());
        values.put(DatabaseConnection.COLUMN_RATE,order1.getRate());
        values.put(DatabaseConnection.COLUMN_DISCOUNT,order1.getDiscount());
        values.put(DatabaseConnection.COLUMN_REMARK,order1.getRemarks());
        values.put(DatabaseConnection.COLUMN_MEET_FLAG,order1.getMeetFlag());
        values.put(DatabaseConnection.COLUMN_MEET_DOCID,order1.getMeetDocId());
        values.put(DatabaseConnection.COLUMN_AMOUNT,order1.getAmount());
        values.put(DatabaseConnection.COLUMN_CASES,order1.getCases());
        values.put(DatabaseConnection.COLUMN_UNIT,order1.getUnit());
        values.put(DatabaseConnection.COLUMN_LAT,order1.getLatitude());
        values.put(DatabaseConnection.COLUMN_LNG,order1.getLongitude());
        values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, order1.getLatlngTimeStamp());
        values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(order1.getVDate()));
        values.put(DatabaseConnection.COLUMN_MFD_DATE,order1.getMfd());
        values.put(DatabaseConnection.COLUMN_BATCH_No,order1.getBatchNo());
        System.out.println("row no "+ order1.getOrdDocId()+" inserted");

        if(order1.getNewOrder())
        {
            values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
            values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");

        }
        else{
            values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
            values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");
        }


        try{

            mTrans = true;
            id = database.insert(DatabaseConnection.TABLE_SALES_RETURN1, null, values);
            database.setTransactionSuccessful();
            database.endTransaction();
            mTrans = false;
            System.out.println(id);
        }
        catch(RuntimeException e){
            System.out.println("+++++++++++++++++++"+e.toString());
            if(mTrans)
            {
                database.endTransaction();
                mTrans = false;
            }
        }
        return id;
    }


    public void UpdateData(String item_id, String partycode, String price) {
        ContentValues values = new ContentValues();
        values.put("rate", price);
        String where = "code='" + partycode + "' and item_id='" + item_id + "'";
        int i = database.update(DatabaseConnection.TABLE_HISTORY, values,
                where, null);
        System.out.println("price updated=" + i);

    }
    public long insertSalesReturn1(SalesReturn1 sales)
    {
        long id = 0;
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_ORDER1_ANDROID_DOCID,sales.getAndroidId1());
        values.put(DatabaseConnection.COLUMN_VISIT_NO, sales.getVisId());
        values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, "");
        values.put(DatabaseConnection.COLUMN_ANDROID_DOCID, sales.getAndroidId());
        values.put(DatabaseConnection.COLUMN_USR_ID,sales.getUserId());
        values.put(DatabaseConnection.COLUMN_VISIT_DATE,sales.getVDate());
        values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE,sales.getAndPartyCode());
        values.put(DatabaseConnection.COLUMN_PARTY_CODE,sales.getPartyId());
        values.put(DatabaseConnection.COLUMN_Smid,sales.getSMId());
        values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(sales.getVDate()));
        values.put(DatabaseConnection.COLUMN_TIMESTAMP,"0");
        values.put(DatabaseConnection.COLUMN_LAT,sales.getLatitude());
        values.put(DatabaseConnection.COLUMN_LNG,sales.getLongitude());
        values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME,sales.getLatlngdt());
        values.put(DatabaseConnection.COLUMN_BATCH_No,sales.getBatchNo());
        values.put(DatabaseConnection.COLUMN_DSRLOCK,"0");
        values.put(DatabaseConnection.COLUMN_MFD_DATE,sales.getMFD());
        values.put(DatabaseConnection.COLUMN_CASES,sales.getCases());
        values.put(DatabaseConnection.COLUMN_UNIT,sales.getUnit());
        values.put(DatabaseConnection.COLUMN_AREA_CODE,sales.getAreaId());
        values.put(DatabaseConnection.COLUMN_PRODUCT_CODE,sales.getItemId());
        try
        {
            id = database.insert(DatabaseConnection.TABLE_SALES_RETURN1, null, values);
        }
        catch(RuntimeException e){

            System.out.println("+++++++++++++++++++"+e.toString());
        }

        return id;


    }





    public long updateOrder1(String androidId,Order1 order1)
    {
        long id = 0;
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_ORDER1_NO, order1.getOrd1Id());
        values.put(DatabaseConnection.COLUMN_VISIT_NO, order1.getVisId());
        values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, order1.getOrdDocId());
        values.put(DatabaseConnection.COLUMN_ANDROID_DOCID       ,order1.getOrderAndroid_Id());
        values.put(DatabaseConnection.COLUMN_ORDER1_ANDROID_DOCID,order1.getAndroid_Id());
        values.put(DatabaseConnection.COLUMN_SERIAL_NO,order1.getSno());
        values.put(DatabaseConnection.COLUMN_USR_ID,order1.getUserId());
        values.put(DatabaseConnection.COLUMN_VISIT_DATE,order1.getVDate());
        values.put(DatabaseConnection.COLUMN_SREP_CODE,order1.getSMId());
        values.put(DatabaseConnection.COLUMN_PARTY_CODE,order1.getPartyId());
        values.put(DatabaseConnection.COLUMN_ANDROID_PARTY_CODE,order1.getAndPartyId());
        values.put(DatabaseConnection.COLUMN_AREA_CODE,order1.getAreaId());
        values.put(DatabaseConnection.COLUMN_BEAT_CODE,order1.getBeatId());
        values.put(DatabaseConnection.COLUMN_PRODUCT_CODE,order1.getItemId());
        values.put(DatabaseConnection.COLUMN_QTY,order1.getQty());
        values.put(DatabaseConnection.COLUMN_RATE,order1.getRate());
        values.put(DatabaseConnection.COLUMN_DISCOUNT,order1.getDiscount());
        values.put(DatabaseConnection.COLUMN_REMARK,order1.getRemarks());
        values.put(DatabaseConnection.COLUMN_MEET_FLAG,order1.getMeetFlag());
        values.put(DatabaseConnection.COLUMN_MEET_DOCID,order1.getMeetDocId());
        values.put(DatabaseConnection.COLUMN_AMOUNT,order1.getAmount());
        values.put(DatabaseConnection.COLUMN_CASES,order1.getCases());
        values.put(DatabaseConnection.COLUMN_UNIT,order1.getUnit());
        values.put(DatabaseConnection.COLUMN_LAT,order1.getLatitude());
        values.put(DatabaseConnection.COLUMN_LNG,order1.getLongitude());
        values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, order1.getLatlngTimeStamp());
        values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(order1.getVDate()));
        values.put(DatabaseConnection.COLUMN_BATCH_No,order1.getBatchNo());
        values.put(DatabaseConnection.COLUMN_MFD_DATE, order1.getMfd());
        System.out.println("row no "+ order1.getOrdDocId()+" inserted");

        if(order1.getNewOrder())
        {
            values.put(DatabaseConnection.COLUMN_UPLOAD, "0");
            values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");

        }
        else{
            values.put(DatabaseConnection.COLUMN_UPLOAD, "1");
            values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");
        }


        try {
            id = database.update(DatabaseConnection.TABLE_SALES_RETURN1,values, "Ord1Android_id=?", new String[]{androidId});
        }
        catch (Exception e)
        {

        }
        return id;
    }


    public boolean isAndroidIdExist(String android_doc_id)
    {
        Cursor c = database.query(DatabaseConnection.TABLE_SALES_RETURN1,null,"Ord1Android_id='" +android_doc_id + "'",null,null,null,null);
        if(c.getCount()>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    public  long updateSalesReturn1(String androiddocId,String timeStamp){
        /*****************		END		******************/
        ContentValues values = new ContentValues();
//        values.put(DatabaseConnection.COLUMN_UPLOAD,"1");
        /************************		Write By Sandeep Singh 10-04-2017		******************/
        /*****************		START		******************/
        values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);

        /*****************		END		******************/
        long id=0;
        try
        {
            id = database.update(DatabaseConnection.TABLE_SALES_RETURN1, values, "Ord1Android_id='"+androiddocId+"'", null);
            System.out.println("row="+id);
        }
        catch(RuntimeException e){
            System.out.println("+++++++++++++++++++"+e.toString());
        }
        return id;
    }





    public ArrayList<SalesReturn1> getUploadSales1List(String vDate)
    {
        String query="select o.Ord1Android_id," +
                "o.Android_id," +
                "vl1.visit_no," +
                "o.SALES_RETURN_ID," +
                "o.Sno," +
                "o.usr_id," +
                "o.visit_date," +
                "o.Smid," +
                "o.party_code," +
                "o.area_code," +
                "o.product_code," +

                "ifnull(o.latitude,0) as latitude," +
                "ifnull(o.longitude,0) as longitude," +
                "ifnull(o.LatlngTime,0) as LatlngTime"+
                ",o._case," +
                "o.Unit, " +
                "o.Batch_No, o.MFD_Date "+
                "from "+DatabaseConnection.TABLE_SALES_RETURN1+" o"+
                " LEFT JOIN Visitl1 vl1"+
                " ON o.visit_date = vl1.visit_date"+
                " LEFT JOIN mastParty p"+
                " ON o.party_code = p.webcode"+
                " WHERE  "+
                " o.timestamp = '0' AND o.visit_date = '"+vDate+"'";


        System.out.println(query);

        System.out.println(query);
        Cursor cursor=database.rawQuery(query, null);
        ArrayList<SalesReturn1> orders1 = new ArrayList<SalesReturn1>();
        if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast()))
            {
                SalesReturn1 order1=new SalesReturn1();
                order1.setAndroidId1(cursor.getString(0));
                order1.setAndroidId(cursor.getString(1));
                order1.setVisId(cursor.getString(2));
                order1.setSRetId(cursor.getString(3));
                order1.setSno(cursor.getString(4));
                order1.setUserId(cursor.getString(5));
                order1.setVDate(cursor.getString(6));
                order1.setSMId(cursor.getString(7));
                order1.setPartyId(cursor.getString(8));
                order1.setAreaId(cursor.getString(9));
                order1.setItemId(cursor.getString(10));
                order1.setLatitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT)));
                order1.setLongitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LNG)));
                order1.setLatlngdt(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT_LNG_TIME)));

                order1.setCases(cursor.getString(14));
                order1.setUnit(cursor.getString(15));
                order1.setBatchNo(cursor.getString(16));
                order1.setMFD(cursor.getString(17));


                orders1.add(order1);
                cursor.moveToNext();
            }
        }else{
            System.out.println("No records found");
        }
        cursor.close();
        return orders1;
    }



}
