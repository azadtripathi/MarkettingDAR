package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.library.DateFunction;
import com.dm.model.Order1;

import java.util.ArrayList;

/**
 * Created by Dataman on 4/20/2017.
 */
public class DistStockController {
    private DatabaseConnection dbHelper;
    boolean mTranse = false;
    private SQLiteDatabase database;
    Context mContext;
    public DistStockController(Context context) {
        dbHelper = new DatabaseConnection(context);
        mContext = context;
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

    public long updateDistItem(Order1 order1)
    {
        long id = 0;
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_VISIT_NO, order1.getVisId());
        values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, order1.getOrdDocId());
        values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,order1.getAndroid_Id());
        values.put(DatabaseConnection.COLUMN_USR_ID,order1.getUserId());
        values.put(DatabaseConnection.COLUMN_VISIT_DATE,order1.getVDate());
        values.put(DatabaseConnection.COLUMN_SREP_CODE,order1.getSMId());
        values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID,order1.getDistId());
        values.put(DatabaseConnection.COLUMN_AREA_CODE,order1.getAreaId());
        values.put(DatabaseConnection.COLUMN_PRODUCT_CODE,order1.getItemId());
        values.put(DatabaseConnection.COLUMN_UNIT,order1.getUnit());
        values.put(DatabaseConnection.COLUMN_CASES,order1.getCases());
        values.put(DatabaseConnection.COLUMN_QTY,order1.getQty());
        values.put(DatabaseConnection.COLUMN_LAT,order1.getLatitude());
        values.put(DatabaseConnection.COLUMN_LNG,order1.getLongitude());
        values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, order1.getLatlngTimeStamp());
        values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(order1.getVDate()));
        values.put(DatabaseConnection.COLUMN_TIMESTAMP,"0");
        try{
            id = database.update(DatabaseConnection.TABLE_TRANS_DIST_STOCK, values, DatabaseConnection.COLUMN_ANDROID_DOCID+"=? And "+DatabaseConnection.COLUMN_VISIT_DATE+"=?",new String[]{order1.getAndroid_Id(),order1.getVDate()});
            System.out.println(id);
        }
        catch(RuntimeException e){
            System.out.println("+++++++++++++++++++"+e.toString());
        }
        return id;
    }
    public long insertDistStock(Order1 order1){
        long id=0;
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_VISIT_NO, order1.getVisId());
        values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, order1.getOrdDocId());
        values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,order1.getAndroid_Id());
        values.put(DatabaseConnection.COLUMN_USR_ID,order1.getUserId());
        values.put(DatabaseConnection.COLUMN_VISIT_DATE,order1.getVDate());
        values.put(DatabaseConnection.COLUMN_SREP_CODE,order1.getSMId());
        values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID,order1.getDistId());
        values.put(DatabaseConnection.COLUMN_AREA_CODE,order1.getAreaId());
        values.put(DatabaseConnection.COLUMN_PRODUCT_CODE,order1.getItemId());
        values.put(DatabaseConnection.COLUMN_UNIT,order1.getUnit());
        values.put(DatabaseConnection.COLUMN_CASES,order1.getCases());
        values.put(DatabaseConnection.COLUMN_QTY,order1.getQty());
        values.put(DatabaseConnection.COLUMN_LAT,order1.getLatitude());
        values.put(DatabaseConnection.COLUMN_LNG,order1.getLongitude());
        values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, order1.getLatlngTimeStamp());
        values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(order1.getVDate()));


        if(order1.getNewOrder())
        {
            values.put(DatabaseConnection.COLUMN_TIMESTAMP,"0");

        }
        else{

            values.put(DatabaseConnection.COLUMN_TIMESTAMP, "0");
        }


        try{
            id = database.insert(DatabaseConnection.TABLE_TRANS_DIST_STOCK, null, values);
            System.out.println(id);
        }
        catch(RuntimeException e){
            System.out.println("+++++++++++++++++++"+e.toString());
        }
        return id;
    }

    public long insertDistStockFromWeb(
            String STKDocId,
            String AreaId,
            String VisId,
            String Android_Id,
            String UserId,
            String VDate,
            String SMID,
            String distId,
            String Item,
            String Unit,
            String Cases,
            String Qty,
            String Latitude,
            String Longitude,
            String latlongdt,
            String Millisecond,
            boolean newstock ){
        long id=0;
      //  database.beginTransaction();
        mTranse = true;
        ContentValues values = new ContentValues();
        boolean flag=false,updateflag=false;
        String qry="select timestamp from TransDistStock where Android_id='"+Android_Id+"'";
        System.out.println(qry);
        Cursor cursor = database.rawQuery(qry, null);
        if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast()))
            {
                String timestamp=cursor.getString(0);
                if(timestamp.equalsIgnoreCase("0")){
                    flag=true;
                    updateflag=true;
                }
                else
                {
                    flag=true;
                    updateflag=false;
                }
                cursor.moveToNext();
            }
        }else{
            flag=false;
            values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,Android_Id);
        }
        cursor.close();


        DsrController dc = new DsrController(mContext);
        dc.open();
        boolean isDsrLock = dc.isDsrLockForVisitDate(VDate);
        dc.close();

        if(isDsrLock)
        {
            values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,Android_Id);
            values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, STKDocId);
            values.put(DatabaseConnection.COLUMN_AREA_CODE, AreaId);
            values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
            values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
            values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
            values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
            values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, distId);
            values.put(DatabaseConnection.COLUMN_PRODUCT_CODE, Item);
            values.put(DatabaseConnection.COLUMN_UNIT, Unit);
            values.put(DatabaseConnection.COLUMN_CASES, Cases);
            values.put(DatabaseConnection.COLUMN_QTY, Qty);
            values.put(DatabaseConnection.COLUMN_LAT, Latitude);
            values.put(DatabaseConnection.COLUMN_LNG, Longitude);
            values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongdt);
            values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
            values.put(DatabaseConnection.COLUMN_TIMESTAMP, Millisecond);
            id = database.update(DatabaseConnection.TABLE_TRANS_DIST_STOCK,
                    values, "Android_id='" + Android_Id + "'", null);
            if(id>0)
            {
                // do nothing
            }
            else
            {
                id = database.insert(DatabaseConnection.TABLE_TRANS_DIST_STOCK, null, values);

            }
        }
else {
            values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, STKDocId);
            values.put(DatabaseConnection.COLUMN_AREA_CODE, AreaId);
            values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
            values.put(DatabaseConnection.COLUMN_USR_ID, UserId);
            values.put(DatabaseConnection.COLUMN_VISIT_DATE, VDate);
            values.put(DatabaseConnection.COLUMN_SREP_CODE, SMID);
            values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID, distId);
            values.put(DatabaseConnection.COLUMN_PRODUCT_CODE, Item);
            values.put(DatabaseConnection.COLUMN_UNIT, Unit);
            values.put(DatabaseConnection.COLUMN_CASES, Cases);
            values.put(DatabaseConnection.COLUMN_QTY, Qty);
            values.put(DatabaseConnection.COLUMN_LAT, Latitude);
            values.put(DatabaseConnection.COLUMN_LNG, Longitude);
            values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongdt);
            values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));
            values.put(DatabaseConnection.COLUMN_TIMESTAMP, Millisecond);
            try {
                if (flag) {
                    if (!updateflag) {
                        id = database.update(DatabaseConnection.TABLE_TRANS_DIST_STOCK,
                                values, "Android_id='" + Android_Id + "'", null);
//                    database.setTransactionSuccessful();
//                    database.endTransaction();
                        mTranse = false;
                        System.out.println("row=" + id);
                    } else {
                        // database.endTransaction();
                        CompetitorController.isUpdateFlag = true;
                        mTranse = false;
                    }

                } else {

                    id = database.insert(DatabaseConnection.TABLE_TRANS_DIST_STOCK, null, values);


                }
            } catch (RuntimeException e) {

                System.out.println("+++++++++++++++++++" + e.toString());
            }
            values = null;
            cursor = null;
        }
        return id;

     /*   long id=0;
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_WEB_DOC_ID, STKDocId);
        values.put(DatabaseConnection.COLUMN_AREA_CODE,AreaId);
        values.put(DatabaseConnection.COLUMN_VISIT_NO, VisId);
        values.put(DatabaseConnection.COLUMN_ANDROID_DOCID,Android_Id);
        values.put(DatabaseConnection.COLUMN_USR_ID,UserId);
        values.put(DatabaseConnection.COLUMN_VISIT_DATE,VDate);
        values.put(DatabaseConnection.COLUMN_SREP_CODE,SMID);
        values.put(DatabaseConnection.COLUMN_DISTRIBUTER_ID,distId);
        values.put(DatabaseConnection.COLUMN_PRODUCT_CODE,Item);
        values.put(DatabaseConnection.COLUMN_UNIT,Unit);
        values.put(DatabaseConnection.COLUMN_CASES,Cases);
        values.put(DatabaseConnection.COLUMN_QTY,Qty);
        values.put(DatabaseConnection.COLUMN_LAT,Latitude);
        values.put(DatabaseConnection.COLUMN_LNG,Longitude);
        values.put(DatabaseConnection.COLUMN_LAT_LNG_TIME, latlongdt);
        values.put(DatabaseConnection.COLUMN_CREATED_DATE, DateFunction.ConvertDateToTimestamp(VDate));

        if(newstock)
        {
            values.put(DatabaseConnection.COLUMN_TIMESTAMP, DateFunction.ConvertDateToTimestamp(VDate+" 00:00:00", "dd/MMM/yyyy 00:00:00"));

        }
        else{

            values.put(DatabaseConnection.COLUMN_TIMESTAMP, Millisecond);
        }

        try{
            id = database.insert(DatabaseConnection.TABLE_TRANS_DIST_STOCK, null, values);
            System.out.println(id);
        }
        catch(RuntimeException e){
            System.out.println("+++++++++++++++++++"+e.toString());
        }
        return id;*/
    }
    public ArrayList<Order1> getExistDocId(String date,
                                           String distId) {
        String query="";
        Cursor cursor=null;
        ArrayList<Order1> orderTransactions=null;
        try{
            if(distId!=null)
            {
                query = "select web_doc_id,Android_id,visit_date,DistId,product_code,ifnull(qty,'0'),ifnull(_case,'0'),ifnull(Unit,'0') from TransDistStock where visit_date='"
                        + date + "' and DistId='" + distId + "'";
            }
            System.out.println(query);
            cursor = database.rawQuery(query, null);
            orderTransactions = new ArrayList<Order1>();
            if (cursor.moveToFirst()) {
                while (!(cursor.isAfterLast())) {
                    Order1 orderTransaction = new Order1();
                    orderTransaction.setOrdDocId(cursor.getString(0));
                    orderTransaction.setAndroid_Id(cursor.getString(1));
                    orderTransaction.setVDate(cursor.getString(2));
                    orderTransaction.setDistId(cursor.getString(3));
                    orderTransaction.setItemId(cursor.getString(4));
                    orderTransaction.setQty(cursor.getString(5));
                    orderTransaction.setCases(cursor.getString(6));
                    orderTransaction.setUnit(cursor.getString(7));
                    orderTransactions.add(orderTransaction);
                    cursor.moveToNext();
                }
            } else {
                System.out.println("No records found");
            }

            cursor.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return orderTransactions;
    }

    public void deleteorderTransactionRowByDateDistId(String date,
                                                         String distId) {
        if(distId!=null)
        {
            int i = database.delete(DatabaseConnection.TABLE_TRANS_DIST_STOCK, "visit_date='" + date
                    + "' AND DistId='" + distId + "'", null);
            System.out
                    .println("orderTransaction deleted for date" + " result=" + i);
        }
    }


    public ArrayList<Order1> getUploadDistItemStockList(String vdate) {
        String query = "select o.Android_id,o.usr_id,o.visit_date,o.srep_code," +
                "o.DistId,vl1.visit_no,ifnull(o.product_code,'0'),ifnull(o.qty,'0')," +
                "ifnull(o._case,'0'),ifnull(o.Unit,'0'),ifnull(o.area_code,'0'),ifnull(o.latitude,'0') as  latitude,ifnull(o.longitude,'0') as longitude,ifnull(o.LatlngTime,'0') as LatlngTime"
                + " from TransDistStock o"
                + " LEFT JOIN Visitl1 vl1"
                + " ON o.visit_date = vl1.visit_date"
                + " WHERE " +
                " o.timestamp =0 AND o.visit_date = '"+vdate+"' ";
        System.out.println(query);
        Cursor cursor = database.rawQuery(query, null);
        ArrayList<Order1> orderTransactions = new ArrayList<Order1>();
        if (cursor.moveToFirst()) {
            while (!(cursor.isAfterLast())) {
                Order1 orderTransaction = new Order1();
                orderTransaction.setAndroid_Id(cursor.getString(0));
                orderTransaction.setUserId(cursor.getString(1));
                orderTransaction.setVDate(cursor.getString(2));
                orderTransaction.setSMId(cursor.getString(3));
                orderTransaction.setDistId(cursor.getString(4));
                orderTransaction.setVisId(cursor.getString(5));
                orderTransaction.setItemId(cursor.getString(6));
                orderTransaction.setQty(cursor.getString(7));
                orderTransaction.setCases(cursor.getString(8));
                orderTransaction.setUnit(cursor.getString(9));
                orderTransaction.setAreaId(cursor.getString(10));
                orderTransaction.setLatitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT)));
                orderTransaction.setLongitude(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LNG)));
                orderTransaction.setLatlngTimeStamp(cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_LAT_LNG_TIME)));
                orderTransactions.add(orderTransaction);
                cursor.moveToNext();
            }
        } else {
            System.out.println("No records found");
        }
        cursor.close();
        return orderTransactions;
    }

    public  long updateDistStockUpload(String androiddocId, String webdocId,int ono,int vId,String timeStamp){
        ContentValues values = new ContentValues();
        long id=0;
        values.put(DatabaseConnection.COLUMN_WEB_DOC_ID,webdocId);
       // values.put(DatabaseConnection.COLUMN_ID,ono);
        values.put(DatabaseConnection.COLUMN_VISIT_NO,vId);
        values.put(DatabaseConnection.COLUMN_TIMESTAMP,timeStamp);
        try{
            id = database.update(DatabaseConnection.TABLE_TRANS_DIST_STOCK, values, "Android_id='"+androiddocId+"'", null);
            System.out.println("row="+id);
        }
        catch(RuntimeException e){
            System.out.println("+++++++++++++++++++"+e.toString());
        }
        return id;
    }


    public boolean isAndroidIdExist(String android_doc_id)
    {
        Cursor c = database.query(DatabaseConnection.TABLE_TRANS_DIST_STOCK,null,"Android_id='" +android_doc_id + "'",null,null,null,null);
        if(c.getCount()>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
