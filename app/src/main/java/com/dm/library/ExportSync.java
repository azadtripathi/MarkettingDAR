package com.dm.library;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.dm.controller.AppDataController;
import com.dm.controller.AppEnviroDataController;
import com.dm.controller.CollectionController;
import com.dm.controller.CompetitorController;
import com.dm.controller.DemoTransactionController;
import com.dm.controller.DistStockController;
import com.dm.controller.DsrController;
import com.dm.controller.EnviroController;
import com.dm.controller.FailedVisitController;
import com.dm.controller.HistoryController;
import com.dm.controller.Order1Controller;
import com.dm.controller.OrderController;
import com.dm.controller.SalesReturnController;
import com.dm.controller.SalesReturnController1;
import com.dm.controller.TransCollectionController;
import com.dm.controller.VisitController;
import com.dm.crmdm_app.DashBoradActivity;
import com.dm.crmdm_app.R;
import com.dm.database.DatabaseConnection;
import com.dm.model.AppData;
import com.dm.model.AppEnviroData;
import com.dm.model.Collection;
import com.dm.model.Competitor;
import com.dm.model.DeleteLogData;
import com.dm.model.DemoTransaction;
import com.dm.model.Enviro;
import com.dm.model.FailedVisit;
import com.dm.model.History;
import com.dm.model.Order;
import com.dm.model.Order1;
import com.dm.model.SalesReturn;
import com.dm.model.SalesReturn1;
import com.dm.model.TransCollection;
import com.dm.model.Visit;
import com.dm.model.WriteLogData;
import com.dm.util.AppParameters;
import com.dm.util.Util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import it.sauronsoftware.ftp4j.FTPClient;

/**
 * Created by dataman on 13-Apr-17.
 */
public class ExportSync  {
    final Context context;
    SharedPreferences preferences,preferences2;
    EnviroController enviroController;
    String conper;
    boolean exception=false;
    String server;
    ArrayList<AppData> appDataArray;
    AppDataController appDataController1;
    SimpleDateFormat sdf1,sdf2;
    java.sql.Date data,vdata;
    boolean mSuccess=false;
    DistStockController distStockController;
    DsrController dsrController;
    DemoTransactionController demoTransactionController;
    FailedVisitController failedVisitController;
    OrderController orderController;
    Order1Controller order1Controller;

    SalesReturnController salesController;
    SalesReturnController1 sales1Controller;


    AppEnviroDataController appDataController;int vno;
    CompetitorController competitorController;
    VisitController visitController;
    ArrayList<TransCollection> collections;
    String LockDataMsg="none";

    public ExportSync(Context context){
        this.context=context;
        enviroController =new EnviroController(context);
        preferences=context.getSharedPreferences("RETAILER_SESSION_DATA",Context.MODE_PRIVATE);
        preferences2=context.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        conper=preferences2.getString("CONPERID_SESSION", null);
        appDataController1=new AppDataController(this.context);
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();
        sdf1 = new SimpleDateFormat("dd/MMM/yyyy");
        sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dsrController=new DsrController(context);
    }




    public boolean xJsGetDeletedTranscFromApp()
    {
        String status="";
        try
        {
            ArrayList<DeleteLogData> historyList;
            HistoryController historyController = new HistoryController(context);
            historyController.open();
            historyList = historyController.getDeleteLogData();
            historyController.close();

            if (historyList != null) {
                for (int i = 0; i < historyList.size(); i++) {


                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/xJsGetDeletedTranscFromApp");
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("entity", historyList.get(i).getEntityName()));
                    nameValuePairs.add(new BasicNameValuePair("key", historyList.get(i).getEntityId()));

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);
                    JSONArray jsonArray = new JSONArray(response1);
                    String message = jsonArray.getJSONObject(0).getString("message");
                    System.out.println("Response : " + response1);
                    JSONObject json=null;
                    if(message.equals("Y"))
                    {
                        DbCon dbCon = new DbCon(context);
                        dbCon.open();
                        long rx = dbCon.deleteLogTable(DatabaseConnection.TABLE_DELETE_LOG,historyList.get(i).getEntityId());
                        dbCon.close();
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return true;
    }






    public  boolean JSvalidateDsr(String vdate,String smId,String androidid,int key)
    {
        String status="";
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://"+server+"/And_Sync.asmx/JSGetDSRValidate");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("Smid",smId));
            nameValuePairs.add(new BasicNameValuePair("Visitdate",vdate));
            nameValuePairs.add(new BasicNameValuePair("androidid",androidid));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response1 = httpclient.execute(httppost, responseHandler);
            new DbCon(context).insertExportImportLogData(smId,nameValuePairs.toString(),"JSGetDSRValidate",response1);
            System.out.println("Response : " + response1);
            JSONObject json=null;
            JSONArray jsonarray = new JSONArray(response1);
            for (int i = 0; i < jsonarray.length(); i++) {
                json = jsonarray.getJSONObject(i);
            }
            try{
                status = json.getString("msg");
                System.out.println(status);
            }
            catch(Exception e)
            {
                System.out.println(e);
                mSuccess=true;
                writeLog("4 Exception: "+e.getMessage());

            }

            if(status.equalsIgnoreCase("no"))
                mSuccess=false;
            else
            {
                                mSuccess=true;
                if(key == 2) {
                    Intent intent = new Intent(context, DashBoradActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                            PendingIntent.FLAG_ONE_SHOT);

                    Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.crmdm_action)
                            .setContentTitle(context.getString(R.string.app_name))
                            .setContentText("DSR Already Exists On Web " + vdate + ",Please Delete This DSR Entry From Pending Sync History or Contact CRM Administrator")
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);

                    NotificationManager notificationManager =
                            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                    notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
                }
                else
                {
                   /* AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setMessage("DSR Already Exists On Web " + vdate + ",Please Delete This DSR Entry From Pending Sync History or Contact CRM Administrator");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    alert.create().show();*/
                }
            }
        }
        catch(Exception e){
          mSuccess=true;
            writeLog("5 Exception: "+e.getMessage());
            new DbCon(context).insertExportImportLogData(smId,"","JSGetDSRValidate",e.getMessage());
        }

        return mSuccess;

    }



    public boolean InsertItemStockTemplate() {
        ArrayList<History> historyList;
        HistoryController historyController = new HistoryController(context);
        historyController.open();
        historyList = historyController.getStockItemTempListForUpload();
        historyController.close();
        if (historyList != null) {
            for (int i = 0; i < historyList.size(); i++) {

                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/xJSInsertDistItemTemplate");
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("Distid", historyList.get(i).getCode()));
                    nameValuePairs.add(new BasicNameValuePair("itemid", historyList.get(i).getItem_id()));
                    nameValuePairs.add(new BasicNameValuePair("minDate", historyList.get(i).getCreatedDate()));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);

                    //System.out.println("Response : " + response1);
                    JSONArray jsonarray = new JSONArray(response1);
                    JSONObject json = null;
                    for (int k = 0; k < jsonarray.length(); k++) {
                        json = jsonarray.getJSONObject(k);
                        //System.out.println(json);
                    }
                    int st;
                    String timeStamp;
                    try {
                        st = Integer.parseInt(json.getString("St"));
                        timeStamp=json.getString("MS");
                        if (st > 0) {

                            historyController.open();
                            historyController.updatetStockItemTempUpload(historyList.get(i).getCode(), historyList.get(i).getItem_id(),timeStamp);
                            historyController.close();

                        }
                    } catch (Exception e) {
                        mSuccess = true;
                        e.printStackTrace();
                        writeLog("6 Exception: "+e.getMessage());

                    }

                } catch (Exception e) {
                    mSuccess = true;
                    e.printStackTrace();
                    writeLog("7 Exception: "+e.getMessage());
                }

            }
        }
        return mSuccess;
    }

    public void insertEnviro()
    {
        int r=0;
        Connection con=null;
        Statement stmt1 = null;
        ResultSet rs = null;Statement stmt = null;
        enviroController.open();
        Enviro enviro=null;
        ArrayList<Enviro> enviros=enviroController.getEnviroList();
        enviroController.close();
//		 stmt=ConnectionFromSqlServer.connectionOpen();
//		 String query="SELECT count(SMID) AS SmIdCount FROM AndroidEnviro WHERE SMID="+Integer.parseInt(conper);
//		 System.out.println(query);
//	     try {
//	    	 rs = stmt.executeQuery(query);
//	    	 while (rs.next()) {
//	    	 r=rs.getInt("SmIdCount");
//	    	 }
//	    	 stmt.close();
//	    	 ConnectionFromSqlServer.connectionClosed();
//	     }catch(Exception e)
//	     {
//	    	System.out.println(e);
//	     }
        try{
            String Sql="";
            String deletQuery="delete from AndroidEnviro where SMID="+Integer.parseInt(conper)+"";
            System.out.println(deletQuery);
            Sql="insert into AndroidEnviro (A_OrderNo,A_Order1No,A_POrderNo,A_DemoNo,A_FailedVisitNo,A_VisitNo,A_CompetitorNo," +
                    "A_PartyNo,A_DiscussionNo,A_DistributorCollectionNo,A_POrder1No," +
                    "A_LeaveNo,A_BeatPlanNo,A_PartyCollectionNo,SMID)" +
                    " values(" +
                    ""+(enviros.get(0).getOrder_no()==null?0:enviros.get(0).getOrder_no())+"," +
                    ""+(enviros.get(0).getOrder1_no()==null?0:enviros.get(0).getOrder1_no())+"," +
                    ""+(enviros.get(0).getPorder_no()==null?0:Integer.parseInt(enviros.get(0).getPorder_no()))+"," +
                    ""+(enviros.get(0).getDemo_no()==null?0:enviros.get(0).getDemo_no())+"," +
                    ""+(enviros.get(0).getFailed_No()==null?0:enviros.get(0).getFailed_No())+"," +
                    ""+(enviros.get(0).getVisit_no()==null?0:enviros.get(0).getVisit_no())+"," +
                    ""+(enviros.get(0).getCompt_no()==null?0:enviros.get(0).getCompt_no())+"," +
                    ""+(enviros.get(0).getParty_no()==null?0:enviros.get(0).getParty_no())+"," +
                    ""+(enviros.get(0).getDiscussionNo()==null?0:enviros.get(0).getParty_no())+"," +
                    ""+(enviros.get(0).getDistCollectionNo()==null?0:enviros.get(0).getDistCollectionNo())+"," +
                    ""+(enviros.get(0).getPorder1_no()==null?0:Integer.parseInt(enviros.get(0).getPorder1_no()))+"," +
                    ""+(enviros.get(0).getLeave_no()==null?0:enviros.get(0).getLeave_no())+"," +
                    ""+(enviros.get(0).getBeatNo()==null?0:enviros.get(0).getBeatNo())+"," +
                    ""+(enviros.get(0).getReciep_no()==null?0:Integer.parseInt(enviros.get(0).getReciep_no()))+"," +
                    ""+Integer.parseInt(conper)+" )";
            System.out.println(Sql);
            int resultForMainInsert=0,resultForMaindelete=0;
            con= ConnectionFromSqlServer.connectionOpenWithTransaction();
            con.setAutoCommit(false);
            stmt = con.createStatement();
            try {

                resultForMaindelete=stmt.executeUpdate(deletQuery);
                resultForMainInsert=stmt.executeUpdate(Sql);
                System.out.println(resultForMaindelete);
                System.out.println(resultForMainInsert);

            }
            catch (SQLException e) {
                exception=true;
                // exceptionData.setExceptionData(e.toString(), "insertEnviro");
                e.printStackTrace();
                con.commit();
                stmt.close();
                con.close();
                writeLog("8 Exception: "+e.getMessage());
            }

            catch(Exception e)
            {
                exception=true;
                System.out.println(e);
                con.commit();
                stmt.close();
                con.close();
                writeLog("9 Exception: "+e.getMessage());
            }
            finally{
                //finally block used to close resources
                try{
                    if(stmt!=null)
                        stmt.close();
                }
                catch(SQLException se2){
                    writeLog("10 Exception: "+se2.getMessage());
                }// nothing we can do

                try{
                    if(con!=null)
                        con.commit();
                    con.close();
                }catch(SQLException se){
                    se.printStackTrace();
                    writeLog("11 Exception: "+se.getMessage());
                }
            }



        }
        catch(SQLException se){
            exception=true;
            se.printStackTrace();
            // If there is an error then rollback the changes.
            writeLog("12 Exception: "+se.getMessage());
            System.out.println("Rolling back data here....");
            try{
                if(con!=null)
                    con.rollback();
            }catch(SQLException se2){
                se2.printStackTrace();
            }//end try

        }catch(Exception e){
            //Handle errors for Class.forName
            e.printStackTrace();
        }finally{
            //finally block used to close resources
            try{
                if(stmt1!=null)
                    stmt1.close();
            }catch(SQLException se2){
                writeLog("13 Exception: "+se2.getMessage());
            }// nothing we can do

            try{
                if(con!=null)
                    con.close();
            }catch(SQLException se){
                se.printStackTrace();
            }//end finally try
        }
        ConnectionFromSqlServer.connectionClosed();

    }



    public void insertJSEnviro()
    {		enviroController.open();
        Enviro enviro = null;
        ArrayList<Enviro> enviros = enviroController.getEnviroList();
        enviroController.close();
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/JSInsertAndroidEnviro");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("SMID", conper));
            nameValuePairs.add(new BasicNameValuePair("A_OrderNo", String.valueOf(enviros.get(0).getOrder_no() == null ? 0 : enviros.get(0).getOrder_no())));

//            nameValuePairs.add(new BasicNameValuePair("A_OrderNo", Util.validateNumric(enviros.get(0).getOrder_no())));



            nameValuePairs.add(new BasicNameValuePair("A_POrderNo", String.valueOf(enviros.get(0).getPorder_no() == null ? 0 : Integer.parseInt(enviros.get(0).getPorder_no()))));
            nameValuePairs.add(new BasicNameValuePair("A_DemoNo", String.valueOf(enviros.get(0).getDemo_no() == null ? 0 : enviros.get(0).getDemo_no())));
            nameValuePairs.add(new BasicNameValuePair("A_FailedVisitNo", String.valueOf(enviros.get(0).getFailed_No() == null ? 0 : enviros.get(0).getFailed_No())));
            nameValuePairs.add(new BasicNameValuePair("A_VisitNo", String.valueOf(enviros.get(0).getVisit_no() == null ? 0 : enviros.get(0).getVisit_no())));
            nameValuePairs.add(new BasicNameValuePair("A_CompetitorNo", String.valueOf(enviros.get(0).getCompt_no() == null ? 0 : enviros.get(0).getCompt_no())));
            nameValuePairs.add(new BasicNameValuePair("A_PartyNo", String.valueOf(enviros.get(0).getParty_no() == null ? 0 : enviros.get(0).getParty_no())));
            nameValuePairs.add(new BasicNameValuePair("A_DiscussionNo", String.valueOf(enviros.get(0).getDiscussionNo() == null ? 0 : enviros.get(0).getParty_no())));
            nameValuePairs.add(new BasicNameValuePair("A_DistributorCollectionNo", String.valueOf(enviros.get(0).getDistCollectionNo() == null ? 0 : enviros.get(0).getDistCollectionNo())));
            nameValuePairs.add(new BasicNameValuePair("A_PartyCollectionNo", String.valueOf(enviros.get(0).getReciep_no() == null ? 0 : Integer.parseInt(enviros.get(0).getReciep_no()))));
            nameValuePairs.add(new BasicNameValuePair("A_POrder1No", String.valueOf(enviros.get(0).getPorder1_no() == null ? 0 : Integer.parseInt(enviros.get(0).getPorder1_no()))));
            nameValuePairs.add(new BasicNameValuePair("A_LeaveNo", String.valueOf(enviros.get(0).getLeave_no() == null ? 0 : enviros.get(0).getLeave_no())));
            nameValuePairs.add(new BasicNameValuePair("A_Order1No", String.valueOf(enviros.get(0).getOrder1_no() == null ? 0 : enviros.get(0).getOrder1_no())));
            nameValuePairs.add(new BasicNameValuePair("A_BeatPlanNo", String.valueOf(enviros.get(0).getBeatNo() == null ? 0 : enviros.get(0).getBeatNo())));
            nameValuePairs.add(new BasicNameValuePair("A_distStock_no", String.valueOf(enviros.get(0).getDistStockNo() == null ? 0 : enviros.get(0).getDistStockNo())));
            nameValuePairs.add(new BasicNameValuePair("A_SalesReturn_no",String.valueOf(enviros.get(0).getSalesReturnNo())));
            nameValuePairs.add(new BasicNameValuePair("A_SalesReturn1_no",String.valueOf(enviros.get(0).getSalesReturn1No())));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response1 = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response1);
            System.out.println("Response : " + response1);
        } catch (Exception e) {
            writeLog("14 Exception: "+e.getMessage());
            System.out.println("Exception : " + e.getMessage());
        }
        enviros=null;

    }
    public boolean insertJSDsrOneByOne(Visit dsrs)
    {
        String docId="0";String timeStamp="0";String lati="0";String longi="0";String latlongtimestamp="0";
        int vno=0;
        Date date = null;
        Date ndate = null;
        try {
            date = sdf1.parse(dsrs.getVdate());
            ndate = dsrs.getNextVisitDate().equals("") ? null : sdf1.parse(dsrs.getNextVisitDate());

        } catch (java.text.ParseException e) {
            exception = true;
            writeLog("15 Exception: "+e.getMessage());
            e.printStackTrace();
        }
        data = null;
        data = new java.sql.Date(date.getTime());
        java.sql.Date ndata = null;
        if (ndate != null) {ndata = new java.sql.Date(ndate.getTime());}
        String distId = dsrs.getDistId() == null ? "0" : dsrs.getDistId().replaceAll("'", "''");

        System.out.println(distId);
        if (dsrs.getUserId() == null) {dsrs.setUserId(preferences2.getString("USER_ID", "0"));}
        if (dsrs.getUserId().equals("null")) {dsrs.setUserId(preferences2.getString("USER_ID", "0"));}
        if (dsrs.getSMId() == null) {dsrs.setSMId(preferences2.getString("CONPERID_SESSION", "0"));}
        if (dsrs.getSMId().equals("null")) {dsrs.setSMId(preferences2.getString("CONPERID_SESSION", "0"));}

        ArrayList<NameValuePair> nameValuePairs = null;
        try {
            AppParameters.vNo = -1;
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/JSInsertTransVisit");
            nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("Userid", dsrs.getUserId()));
            nameValuePairs.add(new BasicNameValuePair("VDate", data.toString()));
            nameValuePairs.add(new BasicNameValuePair("NextVisitDate", String.valueOf(ndata == null ? "" : ndata)));
            nameValuePairs.add(new BasicNameValuePair("Remarks", dsrs.getRemark().replaceAll("'", "''")));
            nameValuePairs.add(new BasicNameValuePair("smid", dsrs.getSMId()));
            nameValuePairs.add(new BasicNameValuePair("frmtime1", dsrs.getfrTime1()));
            nameValuePairs.add(new BasicNameValuePair("toTime1", dsrs.getToTime1()));
            nameValuePairs.add(new BasicNameValuePair("withuserid", (dsrs.getWithUserId().equals("") ? "0" : dsrs.getWithUserId())));
            nameValuePairs.add(new BasicNameValuePair("modeoftransport", (dsrs.getModeOfTransport() == null ? "" : dsrs.getModeOfTransport()).replaceAll("'", "''")));
            nameValuePairs.add(new BasicNameValuePair("vehicleused", (dsrs.getVehicleUsed() == null ? "" : dsrs.getVehicleUsed()).replaceAll("'", "''")));
            nameValuePairs.add(new BasicNameValuePair("Lock", dsrs.getDsrLock()));
            nameValuePairs.add(new BasicNameValuePair("nwithuserid", (dsrs.getNextwithUserId().equals("") ? "0" : dsrs.getNextwithUserId())));
            nameValuePairs.add(new BasicNameValuePair("android_id", dsrs.getAndroidDocId()));
            nameValuePairs.add(new BasicNameValuePair("cityids", (dsrs.getCityIds().equals("-1") ? "0" : dsrs.getCityIds())));
            nameValuePairs.add(new BasicNameValuePair("cityname", (dsrs.getCityName().equals("OTHER CITY") ? "" : dsrs.getCityName())));
            nameValuePairs.add(new BasicNameValuePair("orderamountmail", dsrs.getOrderByEmail()));
            nameValuePairs.add(new BasicNameValuePair("orderamountphone", dsrs.getOrderByPhone()));
            nameValuePairs.add(new BasicNameValuePair("visitcode", (dsrs.getVisitName().equals("NA")?"":dsrs.getVisitName())));
            nameValuePairs.add(new BasicNameValuePair("Fromareacode", (dsrs.getAreaId().equals("0")?"0":dsrs.getAreaId())));
            nameValuePairs.add(new BasicNameValuePair("Attendance", (dsrs.getAttandanse().equals("0")?"0":dsrs.getAttandanse())));
            nameValuePairs.add(new BasicNameValuePair("OtherExpense", (dsrs.getOtherExp().equals("0")?"0":dsrs.getOtherExp())));
            nameValuePairs.add(new BasicNameValuePair("AndroidAppRemark", (dsrs.getOtherExpRem().equals("NA")?"":dsrs.getOtherExpRem())));
            nameValuePairs.add(new BasicNameValuePair("toareacode", (dsrs.getToAreaId().equals("0")?"0":dsrs.getToAreaId())));
            nameValuePairs.add(new BasicNameValuePair("ncityid", (dsrs.getNCityId()==null?"0":dsrs.getNCityId())));

            /************************		Write By Sandeep Singh 20-04-2017		******************/
            /*****************		START		******************/
           /* if(dsrs.getLongitude() == null || dsrs.getLongitude().isEmpty())
            {
                nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
            }
            else
            {
                nameValuePairs.add(new BasicNameValuePair("longitude", dsrs.getLongitude()));
            }
            if(dsrs.getLatitude() == null || dsrs.getLatitude().isEmpty())
            {
                nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
            }
            else
            {
                nameValuePairs.add(new BasicNameValuePair("latitude",dsrs.getLatitude()));
            }
            if(dsrs.getLatlngTimeStamp()== null || dsrs.getLatlngTimeStamp().isEmpty())
            {
                nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
            }
            else
            {
                nameValuePairs.add(new BasicNameValuePair("lat_long_dt",dsrs.getLatlngTimeStamp()));
            }*/

            /*****************		END		******************/
            nameValuePairs.add(new BasicNameValuePair("latitude", dsrs.getLatitude()));
            nameValuePairs.add(new BasicNameValuePair("longitude",dsrs.getLongitude()));
            nameValuePairs.add(new BasicNameValuePair("lat_long_dt",dsrs.getLatlngTimeStamp()));

            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response1 = httpclient.execute(httppost, responseHandler);
            new DbCon(context).insertExportImportLogData(dsrs.getSMId(),nameValuePairs.toString(),"JSInsertTransVisit",response1);
            System.out.println("Response : " + response1);
            JSONArray jsonarray = new JSONArray(response1);
            JSONObject json=null;

            for (int k = 0; k < jsonarray.length(); k++) {
                json = jsonarray.getJSONObject(k);
            }

            try {
                vno = Integer.parseInt(json.getString("Id"));
                AppParameters.vNo = vno;
                docId = json.getString("DocumentId");
                /************************		Write By Sandeep Singh 10-04-2017		******************/
                /*****************		START		******************/
                timeStamp=json.getString("MS");
                /*****************		END		******************/

                System.out.println(vno);
            } catch (Exception e) {
                mSuccess=true;
                e.printStackTrace();
                writeLog("16 Exception: "+e.getMessage());
            }


        } catch (Exception e) {
            System.out.println("Exception : ");
            e.printStackTrace();
            writeLog("17 Exception: "+e.getMessage()+"\nParams="+nameValuePairs.toString());
            new DbCon(context).insertExportImportLogData(dsrs.getSMId(),nameValuePairs.toString(),"JSInsertTransVisit",e.getMessage());
            mSuccess=true;
        }
        long id=0;

        if (!mSuccess) {

            if(vno>0 && !docId.equals("0"))
            {
                dsrController.open();
                AppParameters.vNo = vno;
                //id=dsrController.updateVisitl1(dsrs.getAndroidDocId(), docId, vno);
                /************************		Write By Sandeep Singh 10-04-2017		******************/
                /*****************		START		******************/
                id=dsrController.updateVisitl1(dsrs.getAndroidDocId(), docId, vno,timeStamp);
                /*****************		END		******************/
                dsrController.close();
                if(!(id>0))
                {
                    mSuccess=true;

                }
            }
        }

        return mSuccess;
    }

    public boolean insertJSDemo(String vdate)
    {
        demoTransactionController=new DemoTransactionController(context);
        demoTransactionController.open();
        ArrayList<DemoTransaction> demoTransactions=new ArrayList<DemoTransaction>();
        demoTransactions=demoTransactionController.getUploadDemoList(vdate);
        demoTransactionController.close();
        try{
            int vId=0;
            String docId="0";String timeStamp="0";
            String lati="0";String longi="0";String latlongtimestamp="0";
            int vno=0;
            for (int j = 0; j < demoTransactions.size(); j++)
            {
                Date date=null;
                Date ndate=null;
                try {
                    date=sdf1.parse(demoTransactions.get(j).getVDate());
//				ndate=sdf1.parse(demoTransactions.get(j).getNextVisitDate());
                } catch (java.text.ParseException e) {
//					exceptionData.setExceptionData(e.toString(), "insertDemo");
                    writeLog("18 Exception: "+e.getMessage());
                    e.printStackTrace();
                }
                data=null;
                data = new java.sql.Date(date.getTime());
                vId=Integer.parseInt(demoTransactions.get(j).getVisId());

                if(demoTransactions.get(j).getUserId()==null){	demoTransactions.get(j).setUserId(preferences2.getString("USER_ID", "0"));}
                if(demoTransactions.get(j).getUserId().equals("null"))	{demoTransactions.get(j).setUserId(preferences2.getString("USER_ID", "0"));	}
                if(demoTransactions.get(j).getSMId()==null)	{demoTransactions.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));}
                if(demoTransactions.get(j).getSMId().equals("null")){demoTransactions.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));}
                int pos=0;String x="";
                String  imgurl=demoTransactions.get(j).getFilePath();
             /*   String path = demoTransactions.get(j).getFilePath();
                if(!path.equalsIgnoreCase("NA"))
                {
                    pos = path.lastIndexOf("/");
                    x =path.substring(pos+1 , path.length());
                    System.out.println(x);
                    imgurl = "~/DSRImages/" + x;
                }*/
                ArrayList<NameValuePair> nameValuePairs = null;
                try{
                    HttpClient httpclient = new DefaultHttpClient();
//                    HttpPost httppost = new HttpPost("http://"+server+"/And_Sync.asmx/JSInsertTransDemo");
                    HttpPost httppost = new HttpPost("http://"+server+"/And_Sync.asmx/JSInsertTransDemoWithImage");
                    nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("VisId",Integer.toString(vId)));
                    nameValuePairs.add(new BasicNameValuePair("UserId",demoTransactions.get(j).getUserId()));
                    nameValuePairs.add(new BasicNameValuePair("VDate",data.toString()));
                    nameValuePairs.add(new BasicNameValuePair("SMId",demoTransactions.get(j).getSMId()));
                    nameValuePairs.add(new BasicNameValuePair("PartyId",demoTransactions.get(j).getPartyId()));
                    nameValuePairs.add(new BasicNameValuePair("AreaId",demoTransactions.get(j).getAreaId()));
                    nameValuePairs.add(new BasicNameValuePair("Remarks",demoTransactions.get(j).getRemarks().replaceAll("'","''")));
                    nameValuePairs.add(new BasicNameValuePair("ProductClassId",demoTransactions.get(j).getProductClassId()));
                    nameValuePairs.add(new BasicNameValuePair("ProductSegmentId",demoTransactions.get(j).getProductSegmentId()));
                    nameValuePairs.add(new BasicNameValuePair("ProductMatGrp",demoTransactions.get(j).getProductMatGrp()));
                    nameValuePairs.add(new BasicNameValuePair("Android_Id",demoTransactions.get(j).getAndroid_id()));
                    nameValuePairs.add(new BasicNameValuePair("ImgUrl",""));
                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                    /*****************		START		******************/
                    if(demoTransactions.get(j).getLongitude() == null || demoTransactions.get(j).getLongitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", demoTransactions.get(j).getLongitude()));
                    }
                    if(demoTransactions.get(j).getLatitude() == null || demoTransactions.get(j).getLatitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude",demoTransactions.get(j).getLatitude()));
                    }
                    if(demoTransactions.get(j).getLatlngTimeStamp()== null || demoTransactions.get(j).getLatlngTimeStamp().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",demoTransactions.get(j).getLatlngTimeStamp()));
                    }
                    if(imgurl == null || imgurl.isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("image1", "N/A"));
                    }
                    else {
                        nameValuePairs.add(new BasicNameValuePair("image1", imgurl));
                    }
                   // nameValuePairs.add(new BasicNameValuePair("latitude",demoTransactions.get(j).getLatitude()));
                   // nameValuePairs.add(new BasicNameValuePair("longitude",demoTransactions.get(j).getLongitude()));
                  //  nameValuePairs.add(new BasicNameValuePair("lat_long_dt",demoTransactions.get(j).getLatlngTimeStamp()));
                    /*****************		END		******************/
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);
                    new DbCon(context).insertExportImportLogData(demoTransactions.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransDemoWithImage",response1);
                    System.out.println("Response : " + response1);
//					JSONObject json = new JSONObject(response1);
                    JSONArray jsonarray = new JSONArray(response1);
                    JSONObject json=null;
                    for (int k = 0; k < jsonarray.length(); k++) {
                        json = jsonarray.getJSONObject(k);

                    }
                    jsonarray=null;
                    try{
                        vno = Integer.parseInt(json.getString("Id"));
                        docId=json.getString("DocumentId");
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        timeStamp=json.getString("MS");
                        /*****************		END		******************/

                        System.out.println(vno);
                    }
                    catch(Exception e)
                    {
                        mSuccess=true;
                        System.out.println(e);
                        writeLog("19 Exception: "+e.getMessage());
                    }

                }
                catch(Exception e){
                    System.out.println("Exception : " + e.getMessage());
                    writeLog("20 Exception: "+e.getMessage()+"\nParams="+nameValuePairs.toString());
                    new DbCon(context).insertExportImportLogData(demoTransactions.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransDemoWithImage",e.getMessage());
                    mSuccess=true;
                }

                if(demoTransactions.get(j).getFilePath()!=null && !mSuccess)
                {
                    String s=demoTransactions.get(j).getFilePath();
                    System.out.println(s);
                    try{
//                        insertImages(demoTransactions.get(j).getFilePath());
                    }
                    catch(Exception e)
                    {
                        System.out.println(e);
                        mSuccess=true;
                        writeLog("21 Exception: "+e.getMessage());
                    }
                }
                long id=0;
                if(!mSuccess)
                {
                    if (vno > 0 && !docId.equals("0"))
                    {
                        demoTransactionController.open();
                        //id=demoTransactionController.updateDemoOrder(demoTransactions.get(j).getAndroid_id(), docId, vno, vId);
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        id=demoTransactionController.updateDemoOrder(demoTransactions.get(j).getAndroid_id(), docId, vno, vId,timeStamp);
                        /*****************		END		******************/
                        demoTransactionController.close();
                        if(!(id>0))
                        {
                            mSuccess=true;

                        }
                    }
                }
            }
        }

        catch(Exception e){
            mSuccess=true;
            e.printStackTrace();
            writeLog("22 Exception: "+e.getMessage());
        }
        demoTransactions=null;
        return mSuccess;
    }
    public boolean insertJSFailedVisit(String vdate)
    {
        failedVisitController=new FailedVisitController(context);
        failedVisitController.open();
        ArrayList<FailedVisit> failedVisits=new ArrayList<FailedVisit>();
        failedVisits=failedVisitController.getUploadFailedVisitList(vdate);
        try{
            int vId=0;
            String docId="0";String timeStamp="0";String lati="0";String longi="0";String latlongtimestamp="0";
            int vno=0;
            for (int j = 0; j < failedVisits.size(); j++) {
                Date date = null;
                Date ndate = null;
                try {
                    date = sdf1.parse(failedVisits.get(j).getVDate());
                    ndate = sdf1.parse(failedVisits.get(j).getNextvisit());
                } catch (java.text.ParseException e) {
//					exceptionData.setExceptionData(e.toString(), "insertDsr");
                    writeLog("23 Exception: "+e.getMessage());
                    // TODO Auto-generated catch block
                    mSuccess = true;
                    e.printStackTrace();
                }
                data = null;
                data = new java.sql.Date(date.getTime());
                java.sql.Date ndata = new java.sql.Date(ndate.getTime());
                vId = Integer.parseInt(failedVisits.get(j).getVisId());
                if (failedVisits.get(j).getUserID() == null) {
                    failedVisits.get(j).setUserID(preferences2.getString("USER_ID", "0"));


                }
                if (failedVisits.get(j).getUserID().equals("null")) {
                    failedVisits.get(j).setUserID(preferences2.getString("USER_ID", "0"));


                }

                if (failedVisits.get(j).getSMId() == null) {
                    failedVisits.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                }
                if (failedVisits.get(j).getSMId().equals("null")) {
                    failedVisits.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                }

                ArrayList<NameValuePair> nameValuePairs=null;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/JSInsertTransFailedVisit");
                    nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("visid", Integer.toString(vId)));
                    nameValuePairs.add(new BasicNameValuePair("UserId", failedVisits.get(j).getUserID()));
                    nameValuePairs.add(new BasicNameValuePair("VDate", data.toString()));
                    nameValuePairs.add(new BasicNameValuePair("SMId", failedVisits.get(j).getSMId()));
                    nameValuePairs.add(new BasicNameValuePair("PartyId", failedVisits.get(j).getPartyId()));
                    nameValuePairs.add(new BasicNameValuePair("AreaId", failedVisits.get(j).getAreaId()));
                    nameValuePairs.add(new BasicNameValuePair("Remarks", failedVisits.get(j).getRemarks().replaceAll("'", "''")));
                    nameValuePairs.add(new BasicNameValuePair("Nextvisit", ndata.toString()));
                    nameValuePairs.add(new BasicNameValuePair("ReasonID", failedVisits.get(j).getReasonID()));
                    nameValuePairs.add(new BasicNameValuePair("VisitTime", failedVisits.get(j).getVtime()));
                    nameValuePairs.add(new BasicNameValuePair("Android_Id", failedVisits.get(j).getAndroidId()));

/************************		Write By Sandeep Singh 20-04-2017		******************/
                    /*****************		START		******************/
                    if(failedVisits.get(j).getLongitude() == null || failedVisits.get(j).getLongitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", failedVisits.get(j).getLongitude()));
                    }
                    if(failedVisits.get(j).getLatitude() == null || failedVisits.get(j).getLatitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude",failedVisits.get(j).getLatitude()));
                    }
                    if(failedVisits.get(j).getLatlng_timestamp()== null || failedVisits.get(j).getLatlng_timestamp().isEmpty() )
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",failedVisits.get(j).getLatlng_timestamp()));
                    }
                   // nameValuePairs.add(new BasicNameValuePair("latitude",failedVisits.get(j).getLatitude()));
                   // nameValuePairs.add(new BasicNameValuePair("longitude",failedVisits.get(j).getLongitude()));
                   // nameValuePairs.add(new BasicNameValuePair("lat_long_dt",failedVisits.get(j).getLatlng_timestamp()));
                    /*****************		END		******************/

                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);
                    new DbCon(context).insertExportImportLogData(failedVisits.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransFailedVisit",response1);
                    System.out.println("Response : " + response1);
//					JSONObject json = new JSONObject(response1);
                    JSONArray jsonarray = new JSONArray(response1);
                    JSONObject json = null;
                    for (int k = 0; k < jsonarray.length(); k++) {
                        json = jsonarray.getJSONObject(k);

                    }
                    jsonarray=null;
                    try {
                        vno = Integer.parseInt(json.getString("Id"));
                        docId = json.getString("DocumentId");
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        timeStamp=json.getString("MS");
                        /*****************		END		******************/

                        System.out.println(vno);
                    } catch (Exception e) {
                        mSuccess = true;
                        System.out.println(e);
                        writeLog("24 Exception: "+e.getMessage());

                    }

                } catch (Exception e) {
//					connectionDetector=new ConnectionDetector(context);
//					if(!connectionDetector.isConnectingToInternet())
//					{
//						exceptionData.setInternetExceptionData("You don't have internet connection.", "Information");
//						//exceptionData.setExceptionData(e.toString(), "ClientProtocolException InsertEnviro");
//					}
                    writeLog("25 Exception: "+e.getMessage()+"\nParams="+nameValuePairs.toString());
                    mSuccess = true;
                    System.out.println("Exception : " + e.getMessage());
                    new DbCon(context).insertExportImportLogData(failedVisits.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransFailedVisit",e.getMessage());
                }
                long id = 0;
                if (!mSuccess) {
                    if (vno > 0 && !docId.equals("0")) {
                        failedVisitController.open();
                        //id=failedVisitController.updateFailedVisitUpload(failedVisits.get(j).getAndroidId(), docId, vno, vId);
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        id=failedVisitController.updateFailedVisitUpload(failedVisits.get(j).getAndroidId(), docId, vno, vId,timeStamp);
                        /*****************		END		******************/
                        failedVisitController.close();
                        if(!(id>0))
                        {mSuccess = true;}
                    }
                }
            }
            failedVisits=null;
        }

        catch(Exception e){
            //Handle errors for Class.forName
            mSuccess = true;
            e.printStackTrace();
            writeLog("26 Exception: "+e.getMessage());
        }
        return mSuccess;
    }

    public boolean insertJSDistributerFailedVisit(String vdate)
    {
        failedVisitController=new FailedVisitController(context);
        failedVisitController.open();
        ArrayList<FailedVisit> failedVisits=new ArrayList<FailedVisit>();
        failedVisits=failedVisitController.getUploadDistFailedVisitList(vdate);
        try{
            int vId=0;
            String docId="0";String timeStamp="0";String lati="0";String longi="0";String latlongtimestamp="0";
            int vno=0;
            for (int j = 0; j < failedVisits.size(); j++)
            {
                Date date=null;
                Date ndate=null;
                try {
                    date=sdf1.parse(failedVisits.get(j).getVDate());
                    ndate=sdf1.parse(failedVisits.get(j).getNextvisit());
                }
                catch (java.text.ParseException e) {
//					exceptionData.setExceptionData(e.toString(), "insertDsr");
                    // TODO Auto-generated catch block
                    mSuccess=true;
                    e.printStackTrace();
                    writeLog("27 Exception: "+e.getMessage());
                }
                data=null;
                data = new java.sql.Date(date.getTime());
                java.sql.Date ndata=new java.sql.Date(ndate.getTime());
                vId=Integer.parseInt(failedVisits.get(j).getVisId());

                if(failedVisits.get(j).getUserID()==null)
                {
                    failedVisits.get(j).setUserID(preferences2.getString("USER_ID", "0"));


                }
                if(failedVisits.get(j).getUserID().equals("null"))
                {
                    failedVisits.get(j).setUserID(preferences2.getString("USER_ID", "0"));


                }

                if(failedVisits.get(j).getSMId()==null)
                {
                    failedVisits.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                }
                if(failedVisits.get(j).getSMId().equals("null"))
                {
                    failedVisits.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                }
                ArrayList<NameValuePair> nameValuePairs=null;
                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://"+server+"/And_Sync.asmx/JSInsertTransFailedVisit");
                    nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("visid",Integer.toString(vId)));
                    nameValuePairs.add(new BasicNameValuePair("UserId",failedVisits.get(j).getUserID()));
                    nameValuePairs.add(new BasicNameValuePair("VDate",data.toString()));
                    nameValuePairs.add(new BasicNameValuePair("SMId",failedVisits.get(j).getSMId()));
                    nameValuePairs.add(new BasicNameValuePair("PartyId",failedVisits.get(j).getDistId()));
                    nameValuePairs.add(new BasicNameValuePair("AreaId","0"));
                    nameValuePairs.add(new BasicNameValuePair("Remarks", Util.validateAlfa(failedVisits.get(j).getRemarks())));
                    nameValuePairs.add(new BasicNameValuePair("Nextvisit",ndata.toString()));
                    nameValuePairs.add(new BasicNameValuePair("ReasonID",failedVisits.get(j).getReasonID()));
                    nameValuePairs.add(new BasicNameValuePair("VisitTime",failedVisits.get(j).getVtime()));
                    nameValuePairs.add(new BasicNameValuePair("Android_Id",failedVisits.get(j).getAndroidId()));
                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                    /*****************		START		******************/
                    if(failedVisits.get(j).getLongitude() == null || failedVisits.get(j).getLongitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", failedVisits.get(j).getLongitude()));
                    }
                    if(failedVisits.get(j).getLatitude() == null || failedVisits.get(j).getLatitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude",failedVisits.get(j).getLatitude()));
                    }
                    if(failedVisits.get(j).getLatlng_timestamp()== null || failedVisits.get(j).getLatlng_timestamp().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",failedVisits.get(j).getLatlng_timestamp()));
                    }
                    /*
                    nameValuePairs.add(new BasicNameValuePair("latitude",failedVisits.get(j).getLongitude()));
                    nameValuePairs.add(new BasicNameValuePair("longitude",failedVisits.get(j).getLongitude()));
                    nameValuePairs.add(new BasicNameValuePair("lat_long_dt",failedVisits.get(j).getLatlng_timestamp()));*/
                    /*****************		END		******************/
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);
                    System.out.println("Response : " + response1);
//					JSONObject json = new JSONObject(response1);
                    JSONArray jsonarray = new JSONArray(response1);
                    JSONObject json=null;
                    for (int k = 0; k < jsonarray.length(); k++) {
                        json = jsonarray.getJSONObject(k);

                    }
                    jsonarray=null;
                    try{
                        vno = Integer.parseInt(json.getString("Id"));
                        docId=json.getString("DocumentId");
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        timeStamp=json.getString("MS");
                        /*****************		END		******************/
                        System.out.println(vno);
                    }
                    catch(Exception e)
                    {
                        mSuccess=true;
                        System.out.println(e);
                        writeLog("28 Exception: "+e.getMessage());
                    }

                }
                catch(Exception e){
                    mSuccess=true;
                    System.out.println("Exception : " + e.getMessage()+"\nParams="+nameValuePairs.toString());
                    writeLog("29 Exception: "+e.getMessage()+"\nParams="+nameValuePairs.toString());
                }

                long id = 0;
                if (!mSuccess) {
                    if (vno > 0 && !docId.equals("0")) {
                        failedVisitController.open();
                        //id=failedVisitController.updateFailedVisitUpload(failedVisits.get(j).getAndroidId(), docId, vno, vId);
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        id=failedVisitController.updateFailedVisitUpload(failedVisits.get(j).getAndroidId(), docId, vno, vId,timeStamp);
                        /*****************		END		******************/

                        failedVisitController.close();
                        if(!(id>0))
                        {mSuccess = true;}
                    }
                }

            }
            failedVisits=null;
        }

        catch(Exception e){

            mSuccess=true;
            e.printStackTrace();
            writeLog("30 Exception: "+e.getMessage());
        }
        return mSuccess;
    }
    public boolean insertJSOrder(String vdate)
    {
        String timeStamp="0";String lati="0";String longi="0";String latlongtimestamp="0";
        ArrayList<Order1> orders1;
        orderController=new OrderController(context);
        order1Controller=new Order1Controller(context);
        appDataController=new AppEnviroDataController(context);
        appDataController.open();
        ArrayList<AppEnviroData> appDataArray = appDataController.getAppEnviroFromDb();
        appDataController.close();
        String appType="";
        for(int i=0;i<appDataArray.size();i++)
        {
            appType=appDataArray.get(i).getItemwisesale().trim();

        }

        if(appType.equalsIgnoreCase("Y"))
        {

            orderController.open();
            ArrayList<Order> orders=new ArrayList<Order>();
            orders=orderController.getUploadMainList(vdate);
            try{
                int vId=0;
                int vno=0;
                String docId="0";

                for (int j = 0; j < orders.size(); j++) {

                    Date date = null;
                    Date ndate = null;
                    try {
                        date = sdf1.parse(orders.get(j).getVDate());
//						ndate=sdf1.parse(demoTransactions.get(j).getNextVisitDate());
                    } catch (java.text.ParseException e) {
//						exceptionData.setExceptionData(e.toString(), "insertOrder");
                        // TODO Auto-generated catch block
                        mSuccess = true;
                        e.printStackTrace();
                        writeLog("32 Exception: "+e.getMessage());
                    }
                    try
                    {
                        data = null;
                        data = new java.sql.Date(date.getTime());
                    }
                    catch (Exception e)
                    {
                        System.out.println(e);
                        mSuccess = true;
                        writeLog("33 Exception: "+e.getMessage());
                    }
                    vId = Integer.parseInt(orders.get(j).getVisId());
                    if (orders.get(j).getUserId() == null)
                    {
                        orders.get(j).setUserId(preferences2.getString("USER_ID", "0"));
                    }
                    if (orders.get(j).getUserId().equals("null")) {
                        orders.get(j).setUserId(preferences2.getString("USER_ID", "0"));
                    }

                    if (orders.get(j).getSMId() == null) {
                        orders.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                    }
                    if (orders.get(j).getSMId().equals("null")) {
                        orders.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                    }

                    ArrayList<NameValuePair> nameValuePairs = null;
                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/JSInsertTransOrder");
                         nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair("VisId", Integer.toString(vId)));
                        nameValuePairs.add(new BasicNameValuePair("Android_Id", orders.get(j).getAndroid_id()));



                        nameValuePairs.add(new BasicNameValuePair("UserId", orders.get(j).getUserId()));

                        nameValuePairs.add(new BasicNameValuePair("VDate", data.toString()));
                        nameValuePairs.add(new BasicNameValuePair("SMId", orders.get(j).getSMId()));
                        nameValuePairs.add(new BasicNameValuePair("PartyId", orders.get(j).getPartyId()));
                        nameValuePairs.add(new BasicNameValuePair("AreaId", orders.get(j).getAreaId()));
//                        nameValuePairs.add(new BasicNameValuePair("Remarks", (orders.get(j).getRemarks()==null?"":orders.get(j).getRemarks().replaceAll("'", "''"))));
                        nameValuePairs.add(new BasicNameValuePair("Remarks", Util.validateAlfa(orders.get(j).getRemarks())));
                        nameValuePairs.add(new BasicNameValuePair("OrderAmount", orders.get(j).getOrderAmount()));
                        /************************		Write By Sandeep Singh 20-04-2017		******************/
                        /*****************		START		******************/
                        if(orders.get(j).getLongitude() == null || orders.get(j).getLongitude().isEmpty())
                        {
                            nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("longitude", orders.get(j).getLongitude()));
                        }
                        if(orders.get(j).getLatitude() == null || orders.get(j).getLatitude().isEmpty())
                        {
                            nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("latitude",orders.get(j).getLatitude()));
                        }
                        if(orders.get(j).getLatlngTime()== null || orders.get(j).getLatlngTime().isEmpty() )
                        {
                            nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("lat_long_dt",orders.get(j).getLatlngTime()));
                        }
                       /* nameValuePairs.add(new BasicNameValuePair("latitude",orders.get(j).getLatitude()));
                        nameValuePairs.add(new BasicNameValuePair("longitude",orders.get(j).getLongitude()));
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",orders.get(j).getLatlngTime()));*/
                        /*****************		END		******************/
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        final String response1 = httpclient.execute(httppost, responseHandler);
                        new DbCon(context).insertExportImportLogData(orders.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransOrder",response1);
                        System.out.println("Response : " + response1);
//						JSONObject json = new JSONObject(response1);
                        JSONArray jsonarray = new JSONArray(response1);
                        JSONObject json = null;
                        for (int k = 0; k < jsonarray.length(); k++) {
                            json = jsonarray.getJSONObject(k);

                        }
                        jsonarray=null;
                        try {
                            vno = Integer.parseInt(json.getString("Id"));
                            docId = json.getString("DocumentId");
                            /************************		Write By Sandeep Singh 10-04-2017		******************/
                            /*****************		START		******************/
                            timeStamp=json.getString("MS");
                            /*****************		END		******************/

                            System.out.println(vno);
                        } catch (Exception e) {
                            mSuccess = true;
                            System.out.println(e);
                            writeLog("34 Exception: "+e.getMessage());
                        }

                    } catch (Exception e) {
                        mSuccess = true;
                        System.out.println("Exception : " + e.getMessage());
                        writeLog("35 Exception: "+e.getMessage()+"\nParams="+nameValuePairs.toString());
                        new DbCon(context).insertExportImportLogData(orders.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransOrder",e.getMessage());
                    }
                    boolean status = false;

                    if (!mSuccess) {
                        if(vno>0 && !docId.equals("0")){
                            orderController.open();
                            //status = orderController.updateMultipleOrderUpload(orders.get(j).getAndroid_id(), docId, vno, vId);
                            /************************		Write By Sandeep Singh 10-04-2017		******************/
                            /*****************		START		******************/
                            status = orderController.updateMultipleOrderUpload(orders.get(j).getAndroid_id(), docId, vno, vId,timeStamp);
                            /*****************		END		******************/

                            orderController.close();
                            System.out.println("Commiting data here....");
                            System.out.println("j=" + j);
                            if (!status) {
//                                mSuccess = true;
                            }
                        }
                    }
                }
                orders=null;
            }

            catch(Exception e){
                mSuccess=true;
                e.printStackTrace();
            }

            order1Controller.open();
            orders1 = order1Controller.getUploadOrder1List(vdate);
            order1Controller.close();
            try {

                for (int j = 0; j < orders1.size(); j++) {
                    Date date = null;

                    try {
                        date = sdf1.parse(orders1.get(j).getVDate());
//											ndate=sdf1.parse(demoTransactions.get(j).getNextVisitDate());
                    } catch (java.text.ParseException e) {
                        mSuccess=true;
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    try {
                        data = null;
                        data = new java.sql.Date(date.getTime());
                    } catch (Exception e) {
                        mSuccess=true;
                        System.out.println(e);
                    }
                    if (orders1.get(j).getUserId() == null ) {
                        orders1.get(j).setUserId(preferences2.getString("USER_ID", "0"));
                    }
                    if (orders1.get(j).getUserId().equals("null")) {
                        orders1.get(j).setUserId(preferences2.getString("USER_ID", "0"));


                    }

                    if (orders1.get(j).getSMId() == null)
                    {
                        orders1.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));
                    }
                    if (orders1.get(j).getSMId().equals("null")) {
                        orders1.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                    }

                    ArrayList<NameValuePair> nameValuePairs = null;
                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/JSInsertTransOrder1");
                         nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair("OrdId", orders1.get(j).getOrdId()));
                        nameValuePairs.add(new BasicNameValuePair("VisId", orders1.get(j).getVisId().trim()));
                        nameValuePairs.add(new BasicNameValuePair("Sno", orders1.get(j).getSno()));
                        nameValuePairs.add(new BasicNameValuePair("UserId", orders1.get(j).getUserId().trim()));
                        nameValuePairs.add(new BasicNameValuePair("VDate", data.toString()));
                        nameValuePairs.add(new BasicNameValuePair("SMId", orders1.get(j).getSMId().trim()));
                        nameValuePairs.add(new BasicNameValuePair("PartyId", orders1.get(j).getPartyId().trim()));
                        nameValuePairs.add(new BasicNameValuePair("AreaId", orders1.get(j).getAreaId().trim()));
                        nameValuePairs.add(new BasicNameValuePair("ItemId", orders1.get(j).getItemId().trim()));
                        nameValuePairs.add(new BasicNameValuePair("Qty", orders1.get(j).getQty().trim()));
                        nameValuePairs.add(new BasicNameValuePair("Rate", orders1.get(j).getRate().trim()));
//                        nameValuePairs.add(new BasicNameValuePair("Remarks", (orders1.get(j).getRemarks()==null?"":orders1.get(j).getRemarks().replaceAll("'", "''"))));
                       Util util=new Util();
                        String remark="NA";
                        try{remark=util.validateAlfa(orders1.get(j).getRemarks());}
                        catch(Exception e){
                            e.printStackTrace();
                            remark="NA";
                            writeLog("35 Exception: "+e.getMessage());
                        }
                        nameValuePairs.add(new BasicNameValuePair("Remarks",remark ));
                        nameValuePairs.add(new BasicNameValuePair("amount", orders1.get(j).getAmount().replaceAll("'", "''")));
                        nameValuePairs.add(new BasicNameValuePair("Android_Id", orders1.get(j).getOrderAndroid_Id()));
                        nameValuePairs.add(new BasicNameValuePair("Android_Id1", orders1.get(j).getAndroid_Id()));

                        if(orders1.get(j).getCases()==null || orders1.get(j).getCases().isEmpty()){
                            nameValuePairs.add(new BasicNameValuePair("cases","0"));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("cases",orders1.get(j).getCases()));
                        }
                        if(orders1.get(j).getUnit() == null || orders1.get(j).getUnit().isEmpty())
                        {
                            nameValuePairs.add(new BasicNameValuePair("unit","0"));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("unit",orders1.get(j).getUnit()));
                        }

                        /************************		Write By Sandeep Singh 20-04-2017		******************/
                        /*****************		START		******************/
                        /************************		Write By Sandeep Singh 20-04-2017		******************/
                        /*****************		START		******************/
                        if(orders1.get(j).getLongitude() == null || orders1.get(j).getLongitude().isEmpty())
                        {
                            nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("longitude", orders1.get(j).getLongitude()));
                        }
                        if(orders1.get(j).getLatitude() == null || orders1.get(j).getLatitude().isEmpty())
                        {
                            nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("latitude",orders1.get(j).getLatitude()));
                        }
                        if(orders1.get(j).getLatlngTimeStamp()== null || orders1.get(j).getLatlngTimeStamp().isEmpty() )
                        {
                            nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("lat_long_dt",orders1.get(j).getLatlngTimeStamp()));
                        }

                        /*****************		END		******************/
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        final String response1 = httpclient.execute(httppost, responseHandler);
                        new DbCon(context).insertExportImportLogData(orders1.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransOrder1",response1);
                        System.out.println("Response : " + response1);
                        JSONArray jsonarray = new JSONArray(response1);
                        JSONObject json=null;
                        for (int k = 0; k < jsonarray.length(); k++) {
                            json = jsonarray.getJSONObject(k);

                        }
                        jsonarray=null;
                        try {
                            vno = Integer.parseInt(json.getString("Id"));
//							docId=json.getString("DocumentId");
                            /************************		Write By Sandeep Singh 10-04-2017		******************/
                            /*****************		START		******************/
                            timeStamp=json.getString("MS");
                            /*****************		END		******************/

                            System.out.println(vno);
                        } catch (Exception e) {
                            mSuccess=true;
                            System.out.println(e);
                            writeLog("38 Exception: "+e.getMessage());
                        }

                    } catch (Exception e) {
                        mSuccess=true;
                        System.out.println("Exception : " + e.getMessage());
                        writeLog("39 Exception: "+e.getMessage());
                        new DbCon(context).insertExportImportLogData(orders.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransOrder1",e.getMessage());
                    }
                    long id=0;

                    if(!mSuccess) {
                        if(vno>0) {
                            order1Controller.open();
                            //id = order1Controller.updateOrder1Upload(orders1.get(j).getAndroid_Id());
                            /************************		Write By Sandeep Singh 10-04-2017		******************/
                            /*****************		START		******************/
                            id = order1Controller.updateOrder1Upload(orders1.get(j).getAndroid_Id(),timeStamp);
                            /*****************		END		******************/
                            order1Controller.close();
                            System.out.println("Commiting data here....");

                        }
                    }
                }
            }

            catch(Exception e){
                mSuccess=true;
                e.printStackTrace();
                writeLog("40 Exception: "+e.getMessage());
            }

        }
        else{
            orderController.open();
            ArrayList<Order> orders=new ArrayList<Order>();
            orders=orderController.getUploadMainList(vdate);
            try{
                int vId=0;
                String docId="0";
                int vno=0;
                for (int j = 0; j < orders.size(); j++)
                {
                    Date date=null;
                    Date ndate=null;
                    try {
                        date=sdf1.parse(orders.get(j).getVDate());
//				ndate=sdf1.parse(demoTransactions.get(j).getNextVisitDate());
                    } catch (java.text.ParseException e) {
                        mSuccess=true;
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    data=null;
                    data = new java.sql.Date(date.getTime());
                    vId=Integer.parseInt(orders.get(j).getVisId());
                    if(orders.get(j).getUserId()==null)
                    {
                        orders.get(j).setUserId(preferences2.getString("USER_ID", "0"));


                    }
                    if(orders.get(j).getUserId().equals("null"))
                    {
                        orders.get(j).setUserId(preferences2.getString("USER_ID", "0"));
                    }

                    if(orders.get(j).getSMId()==null)
                    {
                        orders.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                    }
                    if(orders.get(j).getSMId().equals("null"))
                    {
                        orders.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                    }

                    ArrayList<NameValuePair> nameValuePairs=null;
                    try{
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost("http://"+server+"/And_Sync.asmx/JSInsertTransOrder");
                         nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair("VisId",Integer.toString(vId)));
                        nameValuePairs.add(new BasicNameValuePair("Android_Id",orders.get(j).getAndroid_id()));
                        nameValuePairs.add(new BasicNameValuePair("UserId",orders.get(j).getUserId()));
                        nameValuePairs.add(new BasicNameValuePair("VDate",data.toString()));
                        nameValuePairs.add(new BasicNameValuePair("SMId",orders.get(j).getSMId()));
                        nameValuePairs.add(new BasicNameValuePair("PartyId",orders.get(j).getPartyId()));
                        nameValuePairs.add(new BasicNameValuePair("AreaId",orders.get(j).getAreaId()));
                        nameValuePairs.add(new BasicNameValuePair("Remarks", Util.validateAlfa(orders.get(j).getRemarks())));
                        nameValuePairs.add(new BasicNameValuePair("OrderAmount",orders.get(j).getOrderAmount()));
                        /************************		Write By Sandeep Singh 20-04-2017		******************/
                        /*****************		START		******************/
                        if(orders.get(j).getLongitude() == null || orders.get(j).getLongitude().isEmpty())
                        {
                            nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("longitude", orders.get(j).getLongitude()));
                        }
                        if(orders.get(j).getLatitude() == null || orders.get(j).getLatitude().isEmpty())
                        {
                            nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("latitude",orders.get(j).getLatitude()));
                        }
                        if(orders.get(j).getLatlngTime()== null || orders.get(j).getLatlngTime().isEmpty() )
                        {
                            nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("lat_long_dt",orders.get(j).getLatlngTime()));
                        }
                       /* nameValuePairs.add(new BasicNameValuePair("latitude",orders.get(j).getLatitude()));
                        nameValuePairs.add(new BasicNameValuePair("longitude",orders.get(j).getLongitude()));
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",orders.get(j).getLatlngTime()));*/
                        /*****************		END		******************/
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        final String response1 = httpclient.execute(httppost, responseHandler);
                        new DbCon(context).insertExportImportLogData(orders.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransOrder",response1);
                        System.out.println("Response : " + response1);
//						JSONObject json = new JSONObject(response1);
                        JSONArray jsonarray = new JSONArray(response1);
                        JSONObject json=null;
                        for (int k = 0; k < jsonarray.length(); k++) {
                            json = jsonarray.getJSONObject(k);

                        }
                        jsonarray=null;
                        try{
                            vno = Integer.parseInt(json.getString("Id"));
                            docId=json.getString("DocumentId");
                            /************************		Write By Sandeep Singh 10-04-2017		******************/
                            /*****************		START		******************/
                            timeStamp=json.getString("MS");
                            /*****************		END		******************/

                            System.out.println(vno);
                        }
                        catch(Exception e)
                        {
                            mSuccess=true;
                            System.out.println(e);
                            writeLog("41 Exception: "+e.getMessage());
                        }

                    }
                    catch(Exception e){
                        mSuccess=true;
                        System.out.println("Exception : " + e.getMessage());
                        writeLog("42 Exception: "+e.getMessage());
                        new DbCon(context).insertExportImportLogData(orders.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransOrder",e.getMessage());
                    }
                    long id=0;
                    if(!mSuccess) {
                        if(vno>0 && !docId.equals("0")) {
                            orderController.open();
                            //id=	orderController.updateOrderUpload(orders.get(j).getAndroid_id(), docId, vno, vId);
                            /************************		Write By Sandeep Singh 10-04-2017		******************/
                            /*****************		START		******************/
                            id=	orderController.updateOrderUpload(orders.get(j).getAndroid_id(), docId, vno, vId,timeStamp);
                            /*****************		END		******************/
                            orderController.close();
                            System.out.println("Commiting data here....");
                            System.out.println("j=" + j);
                            if((id<=0))
                            {mSuccess=true;}
                        }
                    }
                }

            }

            catch(Exception e){
                //Handle errors for Class.forName
                mSuccess=true;
                e.printStackTrace();
                writeLog("43 Exception: "+e.getMessage());
            }
        }
        orders1=null;
        return mSuccess;
    }


    public boolean insertJSCompetitorTest(String vdate) {
        competitorController = new CompetitorController(context);
        competitorController.open();
        ArrayList<Competitor> competitors = new ArrayList<Competitor>();
        competitors = competitorController.getUploadCompetitorList(vdate);
        try {
            int vId = 0;
            String docId = "0";String timeStamp = "0";
            String lati="0";String longi="0";String latlongtimestamp="0";
            int vno = 0;
            for (int j = 0; j < competitors.size(); j++) {
                Date date = null;
                Date ndate = null;
                try {
                    date = sdf1.parse(competitors.get(j).getVDate());
//				ndate=sdf1.parse(demoTransactions.get(j).getNextVisitDate());
                } catch (java.text.ParseException e) {
//					exceptionData.setExceptionData(e.toString(), "insertDsr");
                    mSuccess = true;
                    e.printStackTrace();
                }
                data = null;
                data = new java.sql.Date(date.getTime());
                vId = Integer.parseInt(competitors.get(j).getVisId());
                if (competitors.get(j).getUserId() == null) {
                    competitors.get(j).setUserId(preferences2.getString("USER_ID", "0"));


                }
                if (competitors.get(j).getUserId().equals("null")) {
                    competitors.get(j).setUserId(preferences2.getString("USER_ID", "0"));


                }

                if (competitors.get(j).getSMID() == null) {
                    competitors.get(j).setSMID(preferences2.getString("CONPERID_SESSION", "0"));

                }
                if (competitors.get(j).getSMID().equals("null")) {
                    competitors.get(j).setSMID(preferences2.getString("CONPERID_SESSION", "0"));

                }

                int pos = 0;
                String x = "";
                String imgurl = "";
                String path = competitors.get(j).getFilePath();
                imgurl = path;
//                if (!path.equalsIgnoreCase("NA")) {
//                    pos = path.lastIndexOf("/");
//                    x = path.substring(pos + 1, path.length());
//                    System.out.println(x);
//                    imgurl = "~/DSRImages/" + x;
//                }

                ArrayList<NameValuePair> nameValuePairs = null;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/JSInsertTransCompetitorWithimage");
                    nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("VisId", Integer.toString(vId)));
                    nameValuePairs.add(new BasicNameValuePair("UserId", competitors.get(j).getUserId()));
                    nameValuePairs.add(new BasicNameValuePair("VDate", data.toString()));
                    nameValuePairs.add(new BasicNameValuePair("SMId", competitors.get(j).getSMID()));
                    nameValuePairs.add(new BasicNameValuePair("PartyId", competitors.get(j).getPartyId()));
                    nameValuePairs.add(new BasicNameValuePair("Remarks", competitors.get(j).getRemark().replaceAll("'", "''")));
                    nameValuePairs.add(new BasicNameValuePair("Item", competitors.get(j).getItem()));
                    nameValuePairs.add(new BasicNameValuePair("Qty", competitors.get(j).getQty()));
                    nameValuePairs.add(new BasicNameValuePair("Rate", competitors.get(j).getRate()));
                    nameValuePairs.add(new BasicNameValuePair("Android_Id", competitors.get(j).getAndroid_id()));
                    nameValuePairs.add(new BasicNameValuePair("ImgUrl", ""));
                    nameValuePairs.add(new BasicNameValuePair("CompName", competitors.get(j).getCompetitorName()));
                    nameValuePairs.add(new BasicNameValuePair("Discount", competitors.get(j).getDiscount()));
                    nameValuePairs.add(new BasicNameValuePair("BrandActivity", competitors.get(j).getBrandActivity()));
                    nameValuePairs.add(new BasicNameValuePair("MeetActivity", competitors.get(j).getMeetActivity()));
                    nameValuePairs.add(new BasicNameValuePair("RoadShow", competitors.get(j).getRoadShow()));
                    nameValuePairs.add(new BasicNameValuePair("Scheme", competitors.get(j).getScheme()));
                    nameValuePairs.add(new BasicNameValuePair("OtherGeneralInfo", competitors.get(j).getGeneralInfo()));
                    nameValuePairs.add(new BasicNameValuePair("OtherActivity", Integer.toString((competitors.get(j).getOtherActivity().equals("Yes") ? 1 : 0))));
                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                    /*****************		START		******************/
                    if(competitors.get(j).getLongitude() == null || competitors.get(j).getLongitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", competitors.get(j).getLongitude()));
                    }
                    if(competitors.get(j).getLatitude() == null || competitors.get(j).getLatitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude",competitors.get(j).getLatitude()));
                    }
                    if(competitors.get(j).getLatlngTimeStamp()== null || competitors.get(j).getLatlngTimeStamp().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",competitors.get(j).getLatlngTimeStamp()));
                    }
                    if(imgurl == null || imgurl.isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("image1", "N/A"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("image1", imgurl));
                    }
                  /*  nameValuePairs.add(new BasicNameValuePair("latitude",competitors.get(j).getLatitude()));
                    nameValuePairs.add(new BasicNameValuePair("longitude",competitors.get(j).getLongitude()));
                    nameValuePairs.add(new BasicNameValuePair("lat_long_dt",competitors.get(j).getLatlngTimeStamp()));*/
                    /*****************		END		******************/
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);
                    new DbCon(context).insertExportImportLogData(competitors.get(j).getSMID(),nameValuePairs.toString(),"JSInsertTransCompetitorWithimage",response1);
                    System.out.println("Response : " + response1);
//					JSONObject json = new JSONObject(response1);
                    JSONArray jsonarray = new JSONArray(response1);
                    JSONObject json = null;
                    for (int k = 0; k < jsonarray.length(); k++) {
                        json = jsonarray.getJSONObject(k);

                    }
                    jsonarray = null;
                    try {
                        vno = Integer.parseInt(json.getString("Id"));
                        docId = json.getString("DocumentId");
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        timeStamp=json.getString("MS");
                        /*****************		END		******************/

                        System.out.println(vno);
                    } catch (Exception e) {
                        mSuccess = true;
                        System.out.println(e);
                        writeLog("44 Exception: "+e.getMessage());
                    }

                } catch (Exception e) {
                    mSuccess = true;
                    System.out.println("Exception : " + e.getMessage());
                    writeLog("45 Exception: "+e.getMessage()+"\nParams="+nameValuePairs.toString());
                    new DbCon(context).insertExportImportLogData(competitors.get(j).getSMID(),nameValuePairs.toString(),"JSInsertTransCompetitorWithimage",e.getMessage());
                }

                if (competitors.get(j).getFilePath() != null) {
                    try {
//                        insertImages(competitors.get(j).getFilePath());
                    } catch (Exception e) {
                        mSuccess = true;
                        writeLog("46 Exception: "+e.getMessage());
                    }
                }


                long id = 0;
                if (!mSuccess) {
                    if (vno > 0 && !docId.equals("0")) {
                        competitorController.open();
                        //id = competitorController.updateCompetitorUpload(competitors.get(j).getAndroid_id(), docId, vno, vId);
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        id = competitorController.updateCompetitorUpload(competitors.get(j).getAndroid_id(), docId, vno, vId,timeStamp);
                        /*****************		END		******************/
                        competitorController.close();
                        if (!(id > 0)) {
                            mSuccess = true;
                        }
                    }
                }
            }
            competitors = null;
        } catch (Exception e) {
            mSuccess = true;
            e.printStackTrace();
        }
        return mSuccess;
    }

    public boolean insertJSCompetitor(String vdate) {
        competitorController = new CompetitorController(context);
        competitorController.open();
        ArrayList<Competitor> competitors = new ArrayList<Competitor>();
        competitors = competitorController.getUploadCompetitorList(vdate);
        try {
            int vId = 0;
            String docId = "0";String timeStamp = "0";
            String lati="0";String longi="0";String latlongtimestamp="0";
            int vno = 0;
            for (int j = 0; j < competitors.size(); j++) {
                Date date = null;
                Date ndate = null;
                try {
                    date = sdf1.parse(competitors.get(j).getVDate());
//				ndate=sdf1.parse(demoTransactions.get(j).getNextVisitDate());
                } catch (java.text.ParseException e) {
//					exceptionData.setExceptionData(e.toString(), "insertDsr");
                    mSuccess = true;
                    e.printStackTrace();
                }
                data = null;
                data = new java.sql.Date(date.getTime());
                vId = Integer.parseInt(competitors.get(j).getVisId());
                if (competitors.get(j).getUserId() == null) {
                    competitors.get(j).setUserId(preferences2.getString("USER_ID", "0"));


                }
                if (competitors.get(j).getUserId().equals("null")) {
                    competitors.get(j).setUserId(preferences2.getString("USER_ID", "0"));


                }

                if (competitors.get(j).getSMID() == null) {
                    competitors.get(j).setSMID(preferences2.getString("CONPERID_SESSION", "0"));

                }
                if (competitors.get(j).getSMID().equals("null")) {
                    competitors.get(j).setSMID(preferences2.getString("CONPERID_SESSION", "0"));

                }

                int pos = 0;
                String x = "";
                String imgurl = "";
                String path = competitors.get(j).getFilePath();
                if (!path.equalsIgnoreCase("NA")) {
                    pos = path.lastIndexOf("/");
                    x = path.substring(pos + 1, path.length());
                    System.out.println(x);
                    imgurl = "~/DSRImages/" + x;
                }

                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/JSInsertTransCompetitor");
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("VisId", Integer.toString(vId)));
                    nameValuePairs.add(new BasicNameValuePair("UserId", competitors.get(j).getUserId()));
                    nameValuePairs.add(new BasicNameValuePair("VDate", data.toString()));
                    nameValuePairs.add(new BasicNameValuePair("SMId", competitors.get(j).getSMID()));
                    nameValuePairs.add(new BasicNameValuePair("PartyId", competitors.get(j).getPartyId()));
                    nameValuePairs.add(new BasicNameValuePair("Remarks", competitors.get(j).getRemark().replaceAll("'", "''")));
                    nameValuePairs.add(new BasicNameValuePair("Item", competitors.get(j).getItem()));
                    nameValuePairs.add(new BasicNameValuePair("Qty", competitors.get(j).getQty()));
                    nameValuePairs.add(new BasicNameValuePair("Rate", competitors.get(j).getRate()));
                    nameValuePairs.add(new BasicNameValuePair("Android_Id", competitors.get(j).getAndroid_id()));
                    nameValuePairs.add(new BasicNameValuePair("ImgUrl", imgurl));
                    nameValuePairs.add(new BasicNameValuePair("CompName", competitors.get(j).getCompetitorName()));
                    nameValuePairs.add(new BasicNameValuePair("Discount", competitors.get(j).getDiscount()));
                    nameValuePairs.add(new BasicNameValuePair("BrandActivity", competitors.get(j).getBrandActivity()));
                    nameValuePairs.add(new BasicNameValuePair("MeetActivity", competitors.get(j).getMeetActivity()));
                    nameValuePairs.add(new BasicNameValuePair("RoadShow", competitors.get(j).getRoadShow()));
                    nameValuePairs.add(new BasicNameValuePair("Scheme", competitors.get(j).getScheme()));
                    nameValuePairs.add(new BasicNameValuePair("OtherGeneralInfo", competitors.get(j).getGeneralInfo()));
                    nameValuePairs.add(new BasicNameValuePair("OtherActivity", Integer.toString((competitors.get(j).getOtherActivity().equals("Yes") ? 1 : 0))));
                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                    /*****************		START		******************/
                    if(competitors.get(j).getLongitude() == null || competitors.get(j).getLongitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", competitors.get(j).getLongitude()));
                    }
                    if(competitors.get(j).getLatitude() == null || competitors.get(j).getLatitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude",competitors.get(j).getLatitude()));
                    }
                    if(competitors.get(j).getLatlngTimeStamp()== null || competitors.get(j).getLatlngTimeStamp().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",competitors.get(j).getLatlngTimeStamp()));
                    }
                  /*  nameValuePairs.add(new BasicNameValuePair("latitude",competitors.get(j).getLatitude()));
                    nameValuePairs.add(new BasicNameValuePair("longitude",competitors.get(j).getLongitude()));
                    nameValuePairs.add(new BasicNameValuePair("lat_long_dt",competitors.get(j).getLatlngTimeStamp()));*/
                    /*****************		END		******************/
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);
                    System.out.println("Response : " + response1);
//					JSONObject json = new JSONObject(response1);
                    JSONArray jsonarray = new JSONArray(response1);
                    JSONObject json = null;
                    for (int k = 0; k < jsonarray.length(); k++) {
                        json = jsonarray.getJSONObject(k);

                    }
                    jsonarray = null;
                    try {
                        vno = Integer.parseInt(json.getString("Id"));
                        docId = json.getString("DocumentId");
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        timeStamp=json.getString("MS");
                        /*****************		END		******************/

                        System.out.println(vno);
                    } catch (Exception e) {
                        mSuccess = true;
                        System.out.println(e);

                    }

                } catch (Exception e) {
                    mSuccess = true;
                    System.out.println("Exception : " + e.getMessage());
                }

                if (competitors.get(j).getFilePath() != null) {
                    try {
                        insertImages(competitors.get(j).getFilePath());
                    } catch (Exception e) {
                        mSuccess = true;
                    }
                }


                long id = 0;
                if (!mSuccess) {
                    if (vno > 0 && !docId.equals("0")) {
                        competitorController.open();
                        //id = competitorController.updateCompetitorUpload(competitors.get(j).getAndroid_id(), docId, vno, vId);
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        id = competitorController.updateCompetitorUpload(competitors.get(j).getAndroid_id(), docId, vno, vId,timeStamp);
                        /*****************		END		******************/
                        competitorController.close();
                        if (!(id > 0)) {
                            mSuccess = true;
                        }
                    }
                }
            }
            competitors = null;
        } catch (Exception e) {
            mSuccess = true;
            e.printStackTrace();
        }
        return mSuccess;
    }


    // RollBack Api
    public void rollbackSync(int vNo)
    {
        try
        {
            String urlJsonObj = "http://" + server + "/And_Sync.asmx/xjsDsrRollBack?visid=" + vNo;
            @SuppressWarnings("deprecation")
            HttpParams httpParams = new BasicHttpParams();

//50 secs connection time out
//            HttpConnectionParams.setConnectionTimeout(httpParams,180 * 1000);
////50 secs session time out
//            HttpConnectionParams.setSoTimeout(httpParams, 180 * 1000);

            HttpClient httpClient = new DefaultHttpClient(httpParams);

//			DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(urlJsonObj);

            @SuppressWarnings("deprecation")
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response1 = httpClient.execute(httpGet, responseHandler);
            System.out.println("Response : " + response1);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }



    public boolean insertJSItemStock(String vdate) {
        distStockController = new DistStockController(context);
        distStockController.open();
        ArrayList<Order1> itemStockList = new ArrayList<Order1>();
        itemStockList = distStockController.getUploadDistItemStockList(vdate);
        try {
            int vId = 0;
            String docId = "0";String timeStamp = "0";
            int vno = 0;
            for (int j = 0; j < itemStockList.size(); j++) {
                Date date = null;
                try {
                    date = sdf1.parse(itemStockList.get(j).getVDate());
                } catch (java.text.ParseException e) {
                    // TODO Auto-generated catch block
                    mSuccess = true;
                    e.printStackTrace();
                }
                data = null;
                data = new java.sql.Date(date.getTime());
                vId = Integer.parseInt(itemStockList.get(j).getVisId());
                if (itemStockList.get(j).getUserId() == null) {
                    itemStockList.get(j).setUserId(preferences2.getString("USER_ID", "0"));
                }
                if (itemStockList.get(j).getUserId().equals("null")) {
                    itemStockList.get(j).setUserId(preferences2.getString("USER_ID", "0"));
                }

                if (itemStockList.get(j).getSMId() == null) {
                    itemStockList.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                }
                if (itemStockList.get(j).getSMId().equals("null")) {
                    itemStockList.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                }
                ArrayList<NameValuePair> nameValuePairs=null;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/JSInsertDistStock");
                    nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("VisId", Integer.toString(vId)));
                    nameValuePairs.add(new BasicNameValuePair("UserId", itemStockList.get(j).getUserId()));
                    nameValuePairs.add(new BasicNameValuePair("VDate", data.toString()));
                    nameValuePairs.add(new BasicNameValuePair("SMId", itemStockList.get(j).getSMId()));
                    nameValuePairs.add(new BasicNameValuePair("DistId", itemStockList.get(j).getDistId()));
                    nameValuePairs.add(new BasicNameValuePair("AreaId", itemStockList.get(j).getAreaId()));
                    nameValuePairs.add(new BasicNameValuePair("ItemId", itemStockList.get(j).getItemId()));
                    nameValuePairs.add(new BasicNameValuePair("Qty", itemStockList.get(j).getQty()));
                    nameValuePairs.add(new BasicNameValuePair("Android_Id", itemStockList.get(j).getAndroid_Id()));
                    if(itemStockList.get(j).getCases()==null || itemStockList.get(j).getCases().isEmpty()){
                        nameValuePairs.add(new BasicNameValuePair("cases","0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("cases", itemStockList.get(j).getCases()));
                    }
                    if(itemStockList.get(j).getUnit() == null || itemStockList.get(j).getUnit().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("unit","0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("unit", itemStockList.get(j).getUnit()));
                    }
                    if(itemStockList.get(j).getLongitude() == null || itemStockList.get(j).getLongitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", itemStockList.get(j).getLongitude()));
                    }
                    if(itemStockList.get(j).getLatitude() == null || itemStockList.get(j).getLatitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude",itemStockList.get(j).getLatitude()));
                    }
                    if(itemStockList.get(j).getLatlngTimeStamp()== null || itemStockList.get(j).getLatlngTimeStamp().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",itemStockList.get(j).getLatlngTimeStamp()));
                    }
                  /*  nameValuePairs.add(new BasicNameValuePair("longitude", itemStockList.get(j).getLongitude()));
                    nameValuePairs.add(new BasicNameValuePair("latitude", itemStockList.get(j).getLatitude()));
                    nameValuePairs.add(new BasicNameValuePair("lat_long_dt", String.valueOf(itemStockList.get(j).getLatlngTimeStamp())));
*/
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);
                    new DbCon(context).insertExportImportLogData(itemStockList.get(j).getSMId(),nameValuePairs.toString(),"JSInsertDistStock",response1);
                    System.out.println("Response : " + response1);
                    JSONArray jsonarray = new JSONArray(response1);
                    JSONObject json = null;
                    for (int k = 0; k < jsonarray.length(); k++) {
                        json = jsonarray.getJSONObject(k);

                    }
                    jsonarray = null;
                    try {
                        vno = Integer.parseInt(json.getString("Id"));
                        docId = json.getString("DocumentId");
                        timeStamp=json.getString("MS");
                        System.out.println(vno);
                    } catch (Exception e) {
                        mSuccess = true;
                        System.out.println(e);

                    }

                } catch (Exception e) {
                    mSuccess = true;
                    writeLog("JSInsertDistStock:Exception : " + e.getMessage()+"\nParams="+nameValuePairs.toString());
                    new DbCon(context).insertExportImportLogData(itemStockList.get(j).getSMId(),nameValuePairs.toString(),"JSInsertDistStock",e.getMessage());
                }
                long id = 0;
                if (!mSuccess) {
                    if (vno > 0 && !docId.equals("0")) {
                        distStockController.open();
                        id = distStockController.updateDistStockUpload(itemStockList.get(j).getAndroid_Id(), docId, vno, vId,timeStamp);
                        distStockController.close();
                        if (!(id > 0)) {
                            mSuccess = true;
                        }
                    }
                }
            }
            itemStockList = null;
        } catch (Exception e) {
            mSuccess = true;
            e.printStackTrace();
        }
        return mSuccess;
    }




    public boolean insertJSDistributerDiscussionTest(String vdate)
    {
        java.sql.Date ndata ;
        visitController=new VisitController(context);
        visitController.open();
        ArrayList<Visit> visits=new ArrayList<Visit>();
        visits=visitController.getUploadDistributerVisitList(vdate);
        try{
            int vId=0;
            int vno=0;
            String docId="";
            String timeStamp="0";String lati="0";String longi="0";String latlongtimestamp="0";
            for (int j = 0; j < visits.size(); j++)
            {
                Date date=null;
                Date ndate=null;
                try {
                    date=sdf1.parse(visits.get(j).getVdate());
                    ndate=sdf1.parse(visits.get(j).getNextVisitDate());
                } catch (java.text.ParseException e) {
//					exceptionData.setExceptionData(e.toString(), "insertDistributerDiscussion");
                    // TODO Auto-generated catch block
                    mSuccess=true;
                    e.printStackTrace();
                }
                data=null;
                ndata = null;
                data = new java.sql.Date(date.getTime());
                ndata = new java.sql.Date(ndate.getTime());
                vId=Integer.parseInt(visits.get(j).getVisId());
                if(visits.get(j).getUserId()==null)
                {
                    visits.get(j).setUserId(preferences2.getString("USER_ID", "0"));


                }
                if(visits.get(j).getUserId().equals("null"))
                {
                    visits.get(j).setUserId(preferences2.getString("USER_ID", "0"));


                }

                if(visits.get(j).getSMId()==null)
                {
                    visits.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                }
                if(visits.get(j).getSMId().equals("null"))
                {
                    visits.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                }
                int pos=0;String x="";
                String  imgurl="";
                String path = visits.get(j).getFilePath();
                /*if(!path.equalsIgnoreCase("NA"))
                {
                    pos = path.lastIndexOf("/");
                    x =path.substring(pos+1 , path.length());
                    System.out.println(x);
                    imgurl = "~/DSRImages/" + x;
                }*/

                ArrayList<NameValuePair> nameValuePairs=null;
                try{
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://"+server+"/And_Sync.asmx/JSInsertTransVisitDistWithImage");
                     nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("VisId",Integer.toString(vId)));
                    nameValuePairs.add(new BasicNameValuePair("Userid",visits.get(j).getUserId()));
                    nameValuePairs.add(new BasicNameValuePair("VDate",data.toString()));
                    nameValuePairs.add(new BasicNameValuePair("SMId",visits.get(j).getSMId()));
                    nameValuePairs.add(new BasicNameValuePair("DistId",visits.get(j).getDistId()));
                    nameValuePairs.add(new BasicNameValuePair("Sno","1"));
                    nameValuePairs.add(new BasicNameValuePair("cityid",visits.get(j).getCityId()));
                    nameValuePairs.add(new BasicNameValuePair("remarkdist",visits.get(j).getRemark().replaceAll("'","''")));
                    nameValuePairs.add(new BasicNameValuePair("NextVisitDate",ndata.toString()));
                    nameValuePairs.add(new BasicNameValuePair("Android_Id",visits.get(j).getAndroidDocId()));
                    nameValuePairs.add(new BasicNameValuePair("ImgUrl",""));
                    nameValuePairs.add(new BasicNameValuePair("NextVisitTime",visits.get(j).getToTime2()));
                    nameValuePairs.add(new BasicNameValuePair("SpentfrTime",visits.get(j).getfrTime1()));
                    nameValuePairs.add(new BasicNameValuePair("SpentToTime",visits.get(j).getToTime1()));
                    if(visits.get(j).getStock() == null || visits.get(j).getStock().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("stock","0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("stock",visits.get(j).getStock()==null?Float.toString(0):Float.toString(Float.parseFloat(visits.get(j).getStock()))));
                    }/************************		Write By Sandeep Singh 20-04-2017		******************/
                    /*****************		START		******************/
                    if(visits.get(j).getLongitude() == null || visits.get(j).getLongitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", visits.get(j).getLongitude()));
                    }
                    if(visits.get(j).getLatitude() == null || visits.get(j).getLatitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude",visits.get(j).getLatitude()));
                    }
                    if(visits.get(j).getLatlngTimeStamp()== null || visits.get(j).getLatlngTimeStamp().isEmpty() )
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",visits.get(j).getLatlngTimeStamp()));
                    }
                    if(path == null || path.isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("image1", "N/A"));
                    }
                    else {
                        nameValuePairs.add(new BasicNameValuePair("image1", path));
                    }
                   /* nameValuePairs.add(new BasicNameValuePair("latitude",visits.get(j).getLatitude()));
                    nameValuePairs.add(new BasicNameValuePair("longitude",visits.get(j).getLongitude()));
                    nameValuePairs.add(new BasicNameValuePair("lat_long_dt",visits.get(j).getLatlngTimeStamp()));*/
                    /*****************		END		******************/
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);
                    new DbCon(context).insertExportImportLogData(visits.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransVisitDistWithImage",response1);
                    System.out.println("Response : " + response1);
//					JSONObject json = new JSONObject(response1);
                    JSONArray jsonarray = new JSONArray(response1);
                    JSONObject json=null;
                    for (int k = 0; k < jsonarray.length(); k++) {
                        json = jsonarray.getJSONObject(k);

                    }
                    jsonarray=null;
                    try{
                        vno = Integer.parseInt(json.getString("Id"));
						docId=json.getString("DocumentId");
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        timeStamp=json.getString("MS");
                        /*****************		END		******************/

                        System.out.println(vno);
                    }
                    catch(Exception e)
                    {
                        mSuccess=true;
                        System.out.println(e);

                    }

                }
                catch(Exception e){
                    mSuccess=true;
                    System.out.println("Exception : " + e.getMessage()+"\nParams="+nameValuePairs.toString());
                    new DbCon(context).insertExportImportLogData(visits.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransVisitDistWithImage",e.getMessage());
                }

                if(visits.get(j).getFilePath()!=null)
                {
                    try{
                        //insertImages(visits.get(j).getFilePath());
                    }
                    catch(Exception e)
                    {
                        mSuccess=true;
                        System.out.println(e);
                    }
                }


                long id = 0;
                if (!mSuccess) {
                    if (vno > 0) {
                        visitController.open();
                        //id=visitController.updateDistributerDiscussionrUpload(visits.get(j).getAndroidDocId(),vId);
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        id=visitController.updateDistributerDiscussionrUpload(visits.get(j).getAndroidDocId(),vno,docId,timeStamp);
                        /*****************		END		******************/
                        visitController.close();
                        if(!(id>0))
                        {mSuccess = true;}
                    }
                }

            }
            visits=null;

        }
        catch(Exception e){
            mSuccess=true;
            e.printStackTrace();
        }
        return mSuccess;
    }




    public boolean insertJSDistributerDiscussion(String vdate)
    {
        java.sql.Date ndata ;
        visitController=new VisitController(context);
        visitController.open();
        ArrayList<Visit> visits=new ArrayList<Visit>();
        visits=visitController.getUploadDistributerVisitList(vdate);
        try{
            int vId=0;
            int vno=0;
//         String docId="";
            String timeStamp="0";String lati="0";String longi="0";String latlongtimestamp="0";
            for (int j = 0; j < visits.size(); j++)
            {
                Date date=null;
                Date ndate=null;
                try {
                    date=sdf1.parse(visits.get(j).getVdate());
                    ndate=sdf1.parse(visits.get(j).getNextVisitDate());
                } catch (java.text.ParseException e) {
//					exceptionData.setExceptionData(e.toString(), "insertDistributerDiscussion");
                    // TODO Auto-generated catch block
                    mSuccess=true;
                    e.printStackTrace();
                }
                data=null;
                ndata = null;
                data = new java.sql.Date(date.getTime());
                ndata = new java.sql.Date(ndate.getTime());
                vId=Integer.parseInt(visits.get(j).getVisId());
                if(visits.get(j).getUserId()==null)
                {
                    visits.get(j).setUserId(preferences2.getString("USER_ID", "0"));
                }
                if(visits.get(j).getUserId().equals("null"))
                {
                    visits.get(j).setUserId(preferences2.getString("USER_ID", "0"));
                }

                if(visits.get(j).getSMId()==null)
                {
                    visits.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));
                }
                if(visits.get(j).getSMId().equals("null"))
                {
                    visits.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));
                }
                int pos=0;String x="";
                String  imgurl="";
                String path = visits.get(j).getFilePath();
                if(!path.equalsIgnoreCase("NA"))
                {
                    pos = path.lastIndexOf("/");
                    x =path.substring(pos+1 , path.length());
                    System.out.println(x);
                    imgurl = "~/DSRImages/" + x;
                }
                try
                {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://"+server+"/And_Sync.asmx/JSInsertTransVisitDist");
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("VisId",Integer.toString(vId)));
                    nameValuePairs.add(new BasicNameValuePair("Userid",visits.get(j).getUserId()));
                    nameValuePairs.add(new BasicNameValuePair("VDate",data.toString()));
                    nameValuePairs.add(new BasicNameValuePair("SMId",visits.get(j).getSMId()));
                    nameValuePairs.add(new BasicNameValuePair("DistId",visits.get(j).getDistId()));
                    nameValuePairs.add(new BasicNameValuePair("Sno","1"));
                    nameValuePairs.add(new BasicNameValuePair("cityid",visits.get(j).getCityId()));
                    nameValuePairs.add(new BasicNameValuePair("remarkdist",visits.get(j).getRemark().replaceAll("'","''")));
                    nameValuePairs.add(new BasicNameValuePair("NextVisitDate",ndata.toString()));
                    nameValuePairs.add(new BasicNameValuePair("Android_Id",visits.get(j).getAndroidDocId()));
                    nameValuePairs.add(new BasicNameValuePair("ImgUrl",imgurl));
                    nameValuePairs.add(new BasicNameValuePair("NextVisitTime",visits.get(j).getToTime2()));
                    nameValuePairs.add(new BasicNameValuePair("SpentfrTime",visits.get(j).getfrTime1()));
                    nameValuePairs.add(new BasicNameValuePair("SpentToTime",visits.get(j).getToTime1()));
                    if(visits.get(j).getStock() == null || visits.get(j).getStock().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("stock","0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("stock",visits.get(j).getStock()==null?Float.toString(0):Float.toString(Float.parseFloat(visits.get(j).getStock()))));
                    }


                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                    /*****************		START		******************/
                    if(visits.get(j).getLongitude() == null || visits.get(j).getLongitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", visits.get(j).getLongitude()));
                    }
                    if(visits.get(j).getLatitude() == null || visits.get(j).getLatitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude",visits.get(j).getLatitude()));
                    }
                    if(visits.get(j).getLatlngTimeStamp()== null || visits.get(j).getLatlngTimeStamp().isEmpty() )
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",visits.get(j).getLatlngTimeStamp()));
                    }
                   /*
                   nameValuePairs.add(new BasicNameValuePair("latitude",visits.get(j).getLatitude()));
                    nameValuePairs.add(new BasicNameValuePair("longitude",visits.get(j).getLongitude()));
                    nameValuePairs.add(new BasicNameValuePair("lat_long_dt",visits.get(j).getLatlngTimeStamp()));
                    */
                    /*****************		END		******************/
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);
                    System.out.println("Response : " + response1);
//					JSONObject json = new JSONObject(response1);
                    JSONArray jsonarray = new JSONArray(response1);
                    JSONObject json=null;
                    for (int k = 0; k < jsonarray.length(); k++)
                    {
                        json = jsonarray.getJSONObject(k);
                    }
                    jsonarray=null;
                    try{
                        vno = Integer.parseInt(json.getString("Id"));
//						docId=json.getString("DocumentId");
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        timeStamp=json.getString("MS");
                        /*****************		END		******************/

                        System.out.println(vno);
                    }
                    catch(Exception e)
                    {
                        mSuccess=true;
                        System.out.println(e);
                        writeLog("3 Exception: "+e.getMessage());
                    }

                }
                catch(Exception e){
                    mSuccess=true;
                    System.out.println("Exception : " + e.getMessage());
                    writeLog("1 Exception: "+e.getMessage());
                }



                long id = 0;
                if (!mSuccess) {
                    if (vno > 0) {
                        visitController.open();
                        //id=visitController.updateDistributerDiscussionrUpload(visits.get(j).getAndroidDocId(),vId);
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                       // id=visitController.updateDistributerDiscussionrUpload(visits.get(j).getAndroidDocId(),vId,timeStamp);
                        /*****************		END		******************/
                        visitController.close();
                        if(!(id>0))
                        {mSuccess = true;}
                    }
                }

            }
            visits=null;

        }
        catch(Exception e){
            mSuccess=true;
            e.printStackTrace();
            writeLog("2 Exception: "+e.getMessage());
        }
        return mSuccess;
    }
    public static String getDocID(String prefix,java.sql.Date date)
            throws SQLException
    {
        String docId="";
        ResultSet rs = null;Connection con = null;
        con= ConnectionFromSqlServer.connectionOpenWithTransaction();
        CallableStatement proc = null;

        try
        {
            proc = con.prepareCall("{ call sp_Getdocid(?,?,?) }");
            proc.setString(1, prefix);
            proc.setDate(2, date);
            proc.registerOutParameter(3, Types.VARCHAR);
            proc.execute();

            docId=proc.getString(3);

        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            try
            {
                proc.close();
            }
            catch (SQLException e) {}
            con.close();
        }
        return docId;

    }


    public static void setDocID(String prefix, java.sql.Date date)
            throws SQLException
    {
        ResultSet rs = null;Connection con = null;
        con= ConnectionFromSqlServer.connectionOpenWithTransaction();
        CallableStatement proc = null;int r=0;

        try
        {
            proc = con.prepareCall("{ call sp_Setdocid(?,?) }");
            proc.setString(1, prefix);
            proc.setDate(2, date);
//			      proc.registerOutParameter(3, Types.VARCHAR);
            proc.execute();
//			      r=proc.getInt(3);

        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        finally
        {
            try
            {
                proc.close();
            }
            catch (SQLException e) {}
            con.close();
        }
    }

    public boolean insertJSDistributerCollection(String vdate) {
//			String xmlStr=null;String status="";
//		StringBuilder stringBuilder=new StringBuilder("");
        CollectionController collectionController=new CollectionController(context);
        collectionController.open();
        ArrayList<Collection> collections=collectionController.getCollectionUplaodList(vdate);
        collectionController.close();
        try{
            int vId=0;
            String docId="0";String timeStamp="0";String lati="0";String longi="0";String latlongtimestamp="0";
            int vno=0;
            for (int j = 0; j < collections.size(); j++)
            {
                Date date=null;
                Date ndate=null;
                Date visitdate=null;
                try {
                    visitdate=sdf1.parse(collections.get(j).getVdate());
                    date=sdf1.parse(collections.get(j).getPaymentDate());
                    if(collections.get(j).getMode().equals("Cheque") ||collections.get(j).getMode().equals("Draft") ||collections.get(j).getMode().equals("RTGS"))
                    {
                        ndate=sdf1.parse(collections.get(j).getCheque_DD_Date());
                    }else{
                        ndate=null;
                    }

                }
                catch (java.text.ParseException e) {
//					exceptionData.setExceptionData(e.toString(), "insert dist collection");
                    // TODO Auto-generated catch block
                    mSuccess=true;
                    e.printStackTrace();
                }
                catch(Exception e)
                {
                    mSuccess=true;
                    e.printStackTrace();
                }
                data=null;
                data = new java.sql.Date(date.getTime());
                vdata=null;
                vdata = new java.sql.Date(visitdate.getTime());
                java.sql.Date ndata=null;
                if(collections.get(j).getMode().equals("Cheque") ||collections.get(j).getMode().equals("Draft") ||collections.get(j).getMode().equals("RTGS"))
                {
                    ndata=new java.sql.Date(ndate.getTime());
                }
                else{
                    ndata=null;
                }
                vId=Integer.parseInt(collections.get(j).getVisitId());

                if(collections.get(j).getUserId()==null)
                {
                    collections.get(j).setUserId(preferences2.getString("USER_ID", "0"));


                }
                if(collections.get(j).getUserId().equals("null"))
                {
                    collections.get(j).setUserId(preferences2.getString("USER_ID", "0"));


                }

                if(collections.get(j).getSMId()==null)
                {
                    collections.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                }
                if(collections.get(j).getSMId().equals("null"))
                {
                    collections.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                }


                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                if(collections.get(j).getMode().equals("Cheque") ||collections.get(j).getMode().equals("Draft") ||collections.get(j).getMode().equals("RTGS"))
                {
                    nameValuePairs.add(new BasicNameValuePair("VisId",Integer.toString(vId)));
                    nameValuePairs.add(new BasicNameValuePair("UserId",collections.get(j).getUserId()));
                    nameValuePairs.add(new BasicNameValuePair("VDate",String.valueOf(vdata==null?"":vdata)));
                    nameValuePairs.add(new BasicNameValuePair("SMId",collections.get(j).getSMId()));
                    nameValuePairs.add(new BasicNameValuePair("DistId",collections.get(j).getDistId()));
                    nameValuePairs.add(new BasicNameValuePair("Mode",collections.get(j).getMode()));
                    nameValuePairs.add(new BasicNameValuePair("Remarks",collections.get(j).getRemarks().replaceAll("'","''")));
                    nameValuePairs.add(new BasicNameValuePair("Amount",collections.get(j).getAmount()));
                    nameValuePairs.add(new BasicNameValuePair("PaymentDate",String.valueOf(data==null?"":data)));
                    nameValuePairs.add(new BasicNameValuePair("Cheque_DDNo",collections.get(j).getCheque_DDNo()));
                    nameValuePairs.add(new BasicNameValuePair("Android_Id",collections.get(j).getAndroid_id()));
                    nameValuePairs.add(new BasicNameValuePair("Cheque_DD_Date",String.valueOf(ndata==null?"":ndata)));
                    nameValuePairs.add(new BasicNameValuePair("Bank",collections.get(j).getBank().replaceAll("'","''")));
                    nameValuePairs.add(new BasicNameValuePair("Branch",collections.get(j).getBranch().replaceAll("'","''")));
                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                    /*****************		START		******************/
                    if(collections.get(j).getLongitude() == null || collections.get(j).getLongitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", collections.get(j).getLongitude()));
                    }
                    if(collections.get(j).getLatitude() == null || collections.get(j).getLatitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude",collections.get(j).getLatitude()));
                    }
                    if(collections.get(j).getLatlngTime_stamp()== null || collections.get(j).getLatlngTime_stamp().isEmpty() )
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",collections.get(j).getLatlngTime_stamp()));
                    }
                  /*  nameValuePairs.add(new BasicNameValuePair("latitude",collections.get(j).getLatitude()));
                    nameValuePairs.add(new BasicNameValuePair("longitude",collections.get(j).getLongitude()));
                    nameValuePairs.add(new BasicNameValuePair("lat_long_dt",collections.get(j).getLatlngTime_stamp()));*/
                    /*****************		END		******************/
                }
                else {
                    nameValuePairs.add(new BasicNameValuePair("VisId", Integer.toString(vId)));
                    nameValuePairs.add(new BasicNameValuePair("UserId", collections.get(j).getUserId()));
                    nameValuePairs.add(new BasicNameValuePair("VDate", String.valueOf(vdata == null ? "" : vdata)));
                    nameValuePairs.add(new BasicNameValuePair("SMId", collections.get(j).getSMId()));
                    nameValuePairs.add(new BasicNameValuePair("DistId", collections.get(j).getDistId()));
                    nameValuePairs.add(new BasicNameValuePair("Mode", collections.get(j).getMode()));
                    nameValuePairs.add(new BasicNameValuePair("Remarks", collections.get(j).getRemarks().replaceAll("'", "''")));
                    nameValuePairs.add(new BasicNameValuePair("Amount", collections.get(j).getAmount()));
                    nameValuePairs.add(new BasicNameValuePair("PaymentDate", String.valueOf(data == null ? "" : data)));
                    nameValuePairs.add(new BasicNameValuePair("Android_Id", collections.get(j).getAndroid_id()));
                    nameValuePairs.add(new BasicNameValuePair("Cheque_DDNo",""));
                    nameValuePairs.add(new BasicNameValuePair("Cheque_DD_Date",""));
                    nameValuePairs.add(new BasicNameValuePair("Bank",""));
                    nameValuePairs.add(new BasicNameValuePair("Branch",""));

                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                    /*****************		START		******************/
                    if(collections.get(j).getLongitude() == null || collections.get(j).getLongitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", collections.get(j).getLongitude()));
                    }
                    if(collections.get(j).getLatitude() == null || collections.get(j).getLatitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude",collections.get(j).getLatitude()));
                    }
                    if(collections.get(j).getLatlngTime_stamp()== null || collections.get(j).getLatlngTime_stamp().isEmpty() )
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",collections.get(j).getLatlngTime_stamp()));
                    }
                   /* nameValuePairs.add(new BasicNameValuePair("latitude",collections.get(j).getLatitude()));
                    nameValuePairs.add(new BasicNameValuePair("longitude",collections.get(j).getLongitude()));
                    nameValuePairs.add(new BasicNameValuePair("lat_long_dt",collections.get(j).getLatlngTime_stamp()));*/
                    /*****************		END		******************/

                }
                try
                {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/JSInsertDistributerCollection");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);
                    new DbCon(context).insertExportImportLogData(collections.get(j).getSMId(),nameValuePairs.toString(),"JSInsertDistributerCollection",response1);
                    System.out.println("Response : " + response1);
//						JSONObject json = new JSONObject(response1);
                    JSONArray jsonarray = new JSONArray(response1);
                    JSONObject json=null;
                    for (int k = 0; k < jsonarray.length(); k++) {
                        json = jsonarray.getJSONObject(k);

                    }
                    jsonarray=null;
                    try {
                        vno = Integer.parseInt(json.getString("Id"));
                        docId = json.getString("DocumentId");
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        timeStamp=json.getString("MS");
                        /*****************		END		******************/

                        System.out.println(vno);
                    } catch (Exception e) {
                        mSuccess=true;
                        System.out.println(e);

                    }

                }
                catch (Exception e) {
                    mSuccess=true;
                    System.out.println("Exception : " + e.getMessage()+"\nParams="+nameValuePairs.toString());
                    new DbCon(context).insertExportImportLogData(collections.get(j).getSMId(),nameValuePairs.toString(),"JSInsertDistributerCollection",e.getMessage());
                }

                long id = 0;
                if (!mSuccess) {
                    if (vno > 0 && !docId.equals("0")) {
                        collectionController.open();
//					stringBuilder.append(collections.get(j).getDistName()+"\nAmount -> "+collections.get(j).getAmount()+"\nMode => "+collections.get(j).getMode()+"\n");
                        //id=collectionController.updateCollectionUpload(collections.get(j).getAndroid_id(),docId,vno);
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        id=collectionController.updateCollectionUpload(collections.get(j).getAndroid_id(),docId,vno,timeStamp);
                        /*****************		END		******************/
                        collectionController.close();
//					new GetNotification(context, stringBuilder.toString(), "Payment posted","").ShowNotification();
                        if(!(id>0))
                        {mSuccess = true;}
                    }
                }

            }
            collections=null;
        }

        catch(Exception e){
            //Handle errors for Class.forName
            mSuccess=true;
            e.printStackTrace();
        }
        return mSuccess;
    }


    public boolean insertJSPartyCollection(String vdate)
    {

        TransCollectionController collectionController=new TransCollectionController(context);
        collectionController.open();
        collections=collectionController.getPartyCollectionUplaodList(vdate);
        collectionController.close();
        ArrayList<NameValuePair> nameValuePairs = null;
        try{
            int vId=0;
            String docId="0";String timeStamp="0";
            String lati="0";String longi="0";String latlongtimestamp="0";
            int vno=0;
            for (int j = 0; j < collections.size(); j++) {
                Date date = null;
                Date ndate = null;
                try {
                    date = sdf1.parse(collections.get(j).getPaymentDate());
                    if (collections.get(j).getMode().equals("Cheque") || collections.get(j).getMode().equals("Draft") || collections.get(j).getMode().equals("RTGS")) {
                        ndate = sdf1.parse(collections.get(j).getCheque_DD_Date());
                    } else {
                        ndate = null;
                    }

                } catch (java.text.ParseException e) {
//					exceptionData.setExceptionData(e.toString(), "insert party collection");
                    // TODO Auto-generated catch block
                    mSuccess=true;
                    e.printStackTrace();
                }
                data = null;
                data = new java.sql.Date(date.getTime());
                java.sql.Date ndata = null;

                try {
                    vId = Integer.parseInt(collections.get(j).getVisId());
                }
                catch(Exception e)
                {
                    System.out.println(e);
                }

                if (collections.get(j).getUserId() == null) {
                    collections.get(j).setUserId(preferences2.getString("USER_ID", "0"));


                }
                if (collections.get(j).getUserId().equals("null")) {
                    collections.get(j).setUserId(preferences2.getString("USER_ID", "0"));


                }

                if (collections.get(j).getSMId() == null) {
                    collections.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                }
                if (collections.get(j).getSMId().equals("null")) {
                    collections.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                }

                if (collections.get(j).getMode().equals("Cheque") || collections.get(j).getMode().equals("Draft") || collections.get(j).getMode().equals("RTGS")) {
                    ndata = new java.sql.Date(ndate.getTime());
                } else {
                    ndata = null;
                }
//			      	    vId=Integer.parseInt(collections.get(j).getPaymentDate());

//				String Sql = "";
                 nameValuePairs = new ArrayList<NameValuePair>(1);
                if (collections.get(j).getMode().equals("Cheque") || collections.get(j).getMode().equals("Draft") || collections.get(j).getMode().equals("RTGS")) {
                    nameValuePairs.add(new BasicNameValuePair("VisId",Integer.toString(vId)));
                    nameValuePairs.add(new BasicNameValuePair("UserId",collections.get(j).getUserId()));
                    nameValuePairs.add(new BasicNameValuePair("VDate",String.valueOf(data==null?"":data)));
                    nameValuePairs.add(new BasicNameValuePair("SMId",collections.get(j).getSMId()));
                    nameValuePairs.add(new BasicNameValuePair("DistId",collections.get(j).getPartyId()));
                    nameValuePairs.add(new BasicNameValuePair("AreaId",collections.get(j).getAreaId()));
                    nameValuePairs.add(new BasicNameValuePair("Mode",collections.get(j).getMode()));
                    nameValuePairs.add(new BasicNameValuePair("Remarks",collections.get(j).getRemarks().replaceAll("'","''")));
                    nameValuePairs.add(new BasicNameValuePair("Amount",collections.get(j).getAmount()));
                    nameValuePairs.add(new BasicNameValuePair("PaymentDate",String.valueOf(data==null?"":data)));
                    nameValuePairs.add(new BasicNameValuePair("Cheque_DDNo",collections.get(j).getCheque_DDNo()));
                    nameValuePairs.add(new BasicNameValuePair("androidid",collections.get(j).getAndroid_id()));
                    nameValuePairs.add(new BasicNameValuePair("Cheque_DD_Date",String.valueOf(ndata==null?"":ndata)));
                    nameValuePairs.add(new BasicNameValuePair("Bank",collections.get(j).getBank().replaceAll("'","''")));
                    nameValuePairs.add(new BasicNameValuePair("Branch",collections.get(j).getBranch().replaceAll("'","''")));
                    nameValuePairs.add(new BasicNameValuePair("status",""));
                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                    /*****************		START		******************/
                    if(collections.get(j).getLongitude() == null || collections.get(j).getLongitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", collections.get(j).getLongitude()));
                    }
                    if(collections.get(j).getLatitude() == null || collections.get(j).getLatitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude",collections.get(j).getLatitude()));
                    }
                    if(collections.get(j).getLatlng_timestamp()== null || collections.get(j).getLatlng_timestamp().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",collections.get(j).getLatlng_timestamp()));
                    }
                  /*  nameValuePairs.add(new BasicNameValuePair("latitude",collections.get(j).getLatitude()));
                    nameValuePairs.add(new BasicNameValuePair("longitude",collections.get(j).getLongitude()));
                    nameValuePairs.add(new BasicNameValuePair("lat_long_dt",collections.get(j).getLatlng_timestamp()));*/
                    /*****************		END		******************/

                } else {
                    nameValuePairs.add(new BasicNameValuePair("VisId", Integer.toString(vId)));
                    nameValuePairs.add(new BasicNameValuePair("UserId", collections.get(j).getUserId()));
                    nameValuePairs.add(new BasicNameValuePair("VDate", String.valueOf(data == null ? "" : data)));
                    nameValuePairs.add(new BasicNameValuePair("SMId", collections.get(j).getSMId()));
                    nameValuePairs.add(new BasicNameValuePair("DistId", collections.get(j).getPartyId()));
                    nameValuePairs.add(new BasicNameValuePair("AreaId",collections.get(j).getAreaId()));
                    nameValuePairs.add(new BasicNameValuePair("Mode", collections.get(j).getMode()));
                    nameValuePairs.add(new BasicNameValuePair("Remarks", collections.get(j).getRemarks().replaceAll("'", "''")));
                    nameValuePairs.add(new BasicNameValuePair("Amount", collections.get(j).getAmount()));
                    nameValuePairs.add(new BasicNameValuePair("PaymentDate", String.valueOf(data == null ? "" : data)));
                    nameValuePairs.add(new BasicNameValuePair("androidid", collections.get(j).getAndroid_id()));
                    nameValuePairs.add(new BasicNameValuePair("status",""));
                    nameValuePairs.add(new BasicNameValuePair("Cheque_DDNo",""));
                    nameValuePairs.add(new BasicNameValuePair("Cheque_DD_Date",""));
                    nameValuePairs.add(new BasicNameValuePair("Bank",""));
                    nameValuePairs.add(new BasicNameValuePair("Branch",""));
                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                    /*****************		START		******************/
                    if(collections.get(j).getLongitude() == null || collections.get(j).getLongitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", collections.get(j).getLongitude()));
                    }
                    if(collections.get(j).getLatitude() == null || collections.get(j).getLatitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude",collections.get(j).getLatitude()));
                    }
                    if(collections.get(j).getLatlng_timestamp()== null || collections.get(j).getLatlng_timestamp().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",collections.get(j).getLatlng_timestamp()));
                    }
                  /*  nameValuePairs.add(new BasicNameValuePair("latitude",collections.get(j).getLatitude()));
                    nameValuePairs.add(new BasicNameValuePair("longitude",collections.get(j).getLongitude()));
                    nameValuePairs.add(new BasicNameValuePair("lat_long_dt",collections.get(j).getLatlng_timestamp()));*/
                    /*****************		END		******************/
                }
                try {

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/JSInsertTransCollection");
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);
                    new DbCon(context).insertExportImportLogData(collections.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransCollection",response1);
                    System.out.println("Response : " + response1);
//						JSONObject json = new JSONObject(response1);
                    JSONArray jsonarray = new JSONArray(response1);
                    JSONObject json=null;
                    for (int k = 0; k < jsonarray.length(); k++) {
                        json = jsonarray.getJSONObject(k);

                    }
                    jsonarray=null;
                    try {
                        vno = Integer.parseInt(json.getString("Id"));
                        docId = json.getString("DocumentId");
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        timeStamp=json.getString("MS");

                        System.out.println(vno);
                    } catch (Exception e) {
                        mSuccess=true;
                        System.out.println(e);

                    }

                }
                catch (Exception e) {
                    mSuccess=true;
                    writeLog("Exception313 "+e.getMessage()+"\nParams="+nameValuePairs.toString());
                    new DbCon(context).insertExportImportLogData(collections.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransCollection",e.getMessage());
                    System.out.println("Exception : " + e.getMessage());
                }


                long id = 0;
                if (!mSuccess) {
                    if (vno > 0 && !docId.equals("0")) {
                        collectionController.open();
//				stringBuilder.append(collections.get(j).getPartyName() + "\nAmount -> " + collections.get(j).getAmount() + "\nMode => " + collections.get(j).getMode() + "\n");
                        //id=collectionController.updatePartyCollectionUpload(collections.get(j).getAndroid_id(), docId, vno, vId);
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        id=collectionController.updatePartyCollectionUpload(collections.get(j).getAndroid_id(), docId, vno, vId,timeStamp);
                        /*****************		END		******************/
//				new GetNotification(context, stringBuilder.toString(), "Payment posted", "").ShowNotification();
                        collectionController.close();
                        if(!(id>0))
                        {mSuccess = true;}
                    }
                }

            }
            collections=null;

        }

        catch(Exception e){
            //Handle errors for Class.forName
            mSuccess = true;
            e.printStackTrace();
        }
        return mSuccess;
    }

    public void insertImages(String filePath)
    {
        File file = new File(filePath);

        try {

            FTPClient client = new FTPClient();
            client.connect(Constant.FTP_HOST);
            client.login(Constant.FTP_USER_NAME, Constant.FTP_PASSWORD); //this is the login credentials of your ftpserver. Ensure to use valid username and password otherwise it throws exception
            client.setType(FTPClient.TYPE_BINARY);
            try {

                client.changeDirectory(Constant.FTP_DIRECTORY); //I want to upload picture in MyPictures directory/folder. you can use your own.
            }
            catch (Exception e) {

//                      client.createDirectory(Constant.FTP_DIRECTORY);
//                      client.changeDirectory(Constant.FTP_DIRECTORY);
                System.out.println(e);

            }
            client.upload(file); //this is actual file uploading on FtpServer in specified directory/folder
            client.disconnect(true);   //after file upload, don't forget to disconnect from FtpServer.
            file.delete();
        }
        catch (Exception e) {
//                Toast.makeText(getApplicationContext(), "Exception: "+e.getMessage(), Toast.LENGTH_LONG).show();
            System.out.println(e);
        }
    }
    File myFile=null;
    FileOutputStream fOut=null;
    OutputStreamWriter myOutWriter = null;
    SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
    public void writeLog(String msg) {
        Calendar calendar1=Calendar.getInstance();
        try{
            System.out.println("adsfda====sfsfsd");
            myFile =new File("/sdcard/DmCrmExportSync.txt");
            //  myFile = new File(Environment.getDataDirectory()+"/fftlog/myuploadfile.txt");
            if(!myFile.exists())
            {
                myFile.createNewFile();
            }
            fOut = new FileOutputStream(myFile,true);
            myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(msg+"  "+dateFormat.format(calendar1.getTime())+" \n");
            myOutWriter.close();
            fOut.close();
        }
        catch (IOException e) {
            System.out.println("adsf%%%%%%%%%%Sdasfsfsd");
            e.printStackTrace();
        }
    }

    public boolean xjsinsertLogToServer()
    {
        try
        {
            DbCon dbCon = new DbCon(context);
            dbCon.open();
            ArrayList<WriteLogData> list = dbCon.getExportLogData();
            dbCon.close();


            for(int i = 0; i < list.size();i++)
            {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("smid", list.get(i).getSmId()));
                nameValuePairs.add(new BasicNameValuePair("webservice", list.get(i).getMethod()));
                nameValuePairs.add(new BasicNameValuePair("createddate", list.get(i).getCreatedDate()));
                nameValuePairs.add(new BasicNameValuePair("response", list.get(i).getResponse()));
                nameValuePairs.add(new BasicNameValuePair("parameters",list.get(i).getParams()));
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/xjsinsertLogToServer");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                final String response1 = httpclient.execute(httppost, responseHandler);

                System.out.println("Response : " + response1);
            }
        }
        catch (Exception e)
        {
            mSuccess = true;
            e.printStackTrace();
        }

        return mSuccess;
    }




    public boolean insertSalesReturn(String vDate) {
        String timeStamp = "0";
        String lati = "0";
        String longi = "0";
        String latlongtimestamp = "0";
        ArrayList<SalesReturn1> sales1;
        salesController = new SalesReturnController(context);
        sales1Controller = new SalesReturnController1(context);
        appDataController = new AppEnviroDataController(context);
        appDataController.open();
        ArrayList<AppEnviroData> appDataArray = appDataController.getAppEnviroFromDb();
        appDataController.close();
        String appType = "";
        for (int i = 0; i < appDataArray.size(); i++) {
            appType = appDataArray.get(i).getItemwisesale().trim();

        }

        if (appType.equalsIgnoreCase("Y")) {

            salesController.open();
            ArrayList<SalesReturn> sales = new ArrayList<SalesReturn>();
            sales = salesController.getUploadMainList(vDate);
            salesController.close();
            int vId = 0;
            int vno = 0;
            String docId = "0";
            for (int j = 0; j < sales.size(); j++) {
                vId = Integer.parseInt(sales.get(j).getVisit_no());
                if (sales.get(j).getUserId() == null) {
                    sales.get(j).setUserId(preferences2.getString("USER_ID", "0"));
                }
                if (sales.get(j).getUserId().equals("null")) {
                    sales.get(j).setUserId(preferences2.getString("USER_ID", "0"));


                }

                if (sales.get(j).getSmid() == null) {
                    sales.get(j).setSmid(preferences2.getString("CONPERID_SESSION", "0"));

                }
                if (sales.get(j).getSmid().equals("null")) {
                    sales.get(j).setSmid(preferences2.getString("CONPERID_SESSION", "0"));

                }

                ArrayList<NameValuePair> nameValuePairs = null;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/xJSSaveSaleReturns");
                    nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("VisId", Integer.toString(vId)));
                    nameValuePairs.add(new BasicNameValuePair("Android_Id", sales.get(j).getAndroidId()));


                    nameValuePairs.add(new BasicNameValuePair("UserId", sales.get(j).getUserId()));

                    nameValuePairs.add(new BasicNameValuePair("VDate", data.toString()));
                    nameValuePairs.add(new BasicNameValuePair("SMId", sales.get(j).getSmid()));
                    nameValuePairs.add(new BasicNameValuePair("PartyId", sales.get(j).getPartyId()));
                    if(sales.get(j).getAreaId() == null || sales.get(j).getAreaId().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("AreaId", "0"));
                    }
                    else {
                        nameValuePairs.add(new BasicNameValuePair("AreaId", sales.get(j).getAreaId()));
                    }

                    if (sales.get(j).getLongitude() == null || sales.get(j).getLongitude().isEmpty()) {
                        nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair("longitude", sales.get(j).getLongitude()));
                    }
                    if (sales.get(j).getLatitude() == null || sales.get(j).getLatitude().isEmpty()) {
                        nameValuePairs.add(new BasicNameValuePair("latitude", "0.0"));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair("latitude", sales.get(j).getLatitude()));
                    }
                    if (sales.get(j).getLat_long_dt() == null || sales.get(j).getLat_long_dt().isEmpty()) {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt", String.valueOf(System.currentTimeMillis())));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt", sales.get(j).getLat_long_dt()));
                    }
                       /* nameValuePairs.add(new BasicNameValuePair("latitude",orders.get(j).getLatitude()));
                        nameValuePairs.add(new BasicNameValuePair("longitude",orders.get(j).getLongitude()));
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",orders.get(j).getLatlngTime()));*/
                    /*****************		END		******************/
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);

                    System.out.println("Response : " + response1);

                    JSONArray jsonarray = new JSONArray(response1);
                    JSONObject json = null;
                    for (int k = 0; k < jsonarray.length(); k++) {
                        json = jsonarray.getJSONObject(k);

                    }
                    jsonarray = null;
                    try {
                        vno = Integer.parseInt(json.getString("Id"));
                        docId = json.getString("DocumentId");
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        timeStamp = json.getString("MS");
                        /*****************		END		******************/

                        System.out.println(vno);
                    } catch (Exception e) {
                        mSuccess = true;
                        System.out.println(e);
                        writeLog("34 Exception: " + e.getMessage());
                    }

                } catch (Exception e) {
                    mSuccess = true;
                    System.out.println("Exception : " + e.getMessage());
                    writeLog("35 Exception: " + e.getMessage() + "\nParams=" + nameValuePairs.toString());

                }
                boolean status = false;

                if (!mSuccess) {
                    if (vno > 0 && !docId.equals("0")) {
                        salesController.open();
                        //status = orderController.updateMultipleOrderUpload(orders.get(j).getAndroid_id(), docId, vno, vId);
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        status = salesController.updateMultipleOrderUpload(sales.get(j).getAndroidId(), docId, vno, vId, timeStamp);
                        /*****************		END		******************/

                        salesController.close();
                        System.out.println("Commiting data here....");
                        System.out.println("j=" + j);
                        if (!status) {
//                                mSuccess = true;
                        }
                    }

                }
            }

            sales = null;

            sales1Controller.open();
            sales1 = sales1Controller.getUploadSales1List(vDate);
            sales1Controller.close();
            try {

                for (int j = 0; j < sales1.size(); j++)
                {
                    Date date = null;

                    try {
                        date = sdf1.parse(sales1.get(j).getVDate());
//											ndate=sdf1.parse(demoTransactions.get(j).getNextVisitDate());
                    } catch (java.text.ParseException e) {
                        mSuccess=true;
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    try {
                        data = null;
                        data = new java.sql.Date(date.getTime());
                    } catch (Exception e) {
                        mSuccess=true;
                        System.out.println(e);
                    }
                    if (sales1.get(j).getUserId() == null ) {
                        sales1.get(j).setUserId(preferences2.getString("USER_ID", "0"));


                    }
                    if (sales1.get(j).getUserId().equals("null")) {
                        sales1.get(j).setUserId(preferences2.getString("USER_ID", "0"));
                    }

                    if (sales1.get(j).getSMId() == null) {
                        sales1.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                    }
                    if (sales1.get(j).getSMId().equals("null")) {
                        sales1.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                    }

                    ArrayList<NameValuePair> nameValuePairs = null;
                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/xJSSaveSaleReturns1");
                        nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair("SRetId", sales1.get(j).getSRetId()));
                        nameValuePairs.add(new BasicNameValuePair("VisId", sales1.get(j).getVisId()));
                        nameValuePairs.add(new BasicNameValuePair("Sno", String.valueOf(j+1)));
                        nameValuePairs.add(new BasicNameValuePair("UserId", sales1.get(j).getUserId()));
                        nameValuePairs.add(new BasicNameValuePair("VDate", data.toString()));
                        nameValuePairs.add(new BasicNameValuePair("SMId", sales1.get(j).getSMId()));
                        nameValuePairs.add(new BasicNameValuePair("PartyId", sales1.get(j).getPartyId()));
                        if(sales1.get(j).getAreaId() == null || sales1.get(j).getAreaId().isEmpty())
                        {
                            nameValuePairs.add(new BasicNameValuePair("AreaId", "0"));
                        }
                        else {
                            nameValuePairs.add(new BasicNameValuePair("AreaId", sales1.get(j).getAreaId()));
                        }
                        nameValuePairs.add(new BasicNameValuePair("ItemId", sales1.get(j).getItemId()));


//                        nameValuePairs.add(new BasicNameValuePair("Remarks", (orders1.get(j).getRemarks()==null?"":orders1.get(j).getRemarks().replaceAll("'", "''"))));
                        Util util=new Util();
                        String remark="NA";
                        nameValuePairs.add(new BasicNameValuePair("Android_Id", sales1.get(j).getAndroidId()));
                        nameValuePairs.add(new BasicNameValuePair("Android_Id1", sales1.get(j).getAndroidId1()));

                        if(sales1.get(j).getCases()==null || sales1.get(j).getCases().isEmpty()){
                            nameValuePairs.add(new BasicNameValuePair("cases","0"));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("cases",sales1.get(j).getCases()));
                        }
                        if(sales1.get(j).getUnit() == null || sales1.get(j).getUnit().isEmpty())
                        {
                            nameValuePairs.add(new BasicNameValuePair("unit","0"));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("unit",sales1.get(j).getUnit()));
                        }

                        /************************		Write By Sandeep Singh 20-04-2017		******************/
                        /*****************		START		******************/
                        /************************		Write By Sandeep Singh 20-04-2017		******************/
                        /*****************		START		******************/
                        if(sales1.get(j).getLongitude() == null || sales1.get(j).getLongitude().isEmpty())
                        {
                            nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("longitude", sales1.get(j).getLongitude()));
                        }
                        if(sales1.get(j).getLatitude() == null || sales1.get(j).getLatitude().isEmpty())
                        {
                            nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("latitude",sales1.get(j).getLatitude()));
                        }
                        if(sales1.get(j).getLatlngdt()== null || sales1.get(j).getLatlngdt().isEmpty() )
                        {
                            nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                        }
                        else
                        {
                            nameValuePairs.add(new BasicNameValuePair("lat_long_dt",sales1.get(j).getLatlngdt()));
                        }

                        nameValuePairs.add(new BasicNameValuePair("BatchNo", sales1.get(j).getBatchNo()));
                        nameValuePairs.add(new BasicNameValuePair("MFD", sales1.get(j).getMFD()));


                        /*****************		END		******************/
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        final String response1 = httpclient.execute(httppost, responseHandler);

                        System.out.println("Response : " + response1);
                        JSONArray jsonarray = new JSONArray(response1);
                        JSONObject json=null;
                        for (int k = 0; k < jsonarray.length(); k++)
                        {
                            json = jsonarray.getJSONObject(k);
                        }
                        jsonarray=null;
                        try {
                            vno = Integer.parseInt(json.getString("Id"));
//							docId=json.getString("DocumentId");
                            /************************		Write By Sandeep Singh 10-04-2017		******************/
                            /*****************		START		******************/
                            timeStamp=json.getString("MS");
                            /*****************		END		******************/

                            System.out.println(vno);
                        } catch (Exception e) {
                            mSuccess=true;
                            System.out.println(e);
                            writeLog("38 Exception: "+e.getMessage());
                        }

                    } catch (Exception e) {
                        mSuccess=true;
                        System.out.println("Exception : " + e.getMessage());
                        writeLog("39 Exception: "+e.getMessage());

                    }
                    long id=0;

                    if(!mSuccess) {
                        if(vno>0) {
                            sales1Controller.open();
                            //id = order1Controller.updateOrder1Upload(orders1.get(j).getAndroid_Id());
                            /************************		Write By Sandeep Singh 10-04-2017		******************/
                            /*****************		START		******************/
                            id = sales1Controller.updateSalesReturn1(sales1.get(j).getAndroidId(),timeStamp);
                            /*****************		END		******************/
                            sales1Controller.close();
                            System.out.println("Commiting data here....");

                        }
                    }
                }
            }

            catch(Exception e)
            {
                mSuccess=true;
                e.printStackTrace();
                writeLog("40 Exception: "+e.getMessage());
           }

        }

        return mSuccess;
    }




    public boolean insertJSSalesReturn(String vdate)
    {
        String timeStamp="0";String lati="0";String longi="0";String latlongtimestamp="0";
        ArrayList<Order1> orders1;
        salesController=new SalesReturnController(context);
        sales1Controller=new SalesReturnController1(context);
        appDataController=new AppEnviroDataController(context);
        appDataController.open();
        ArrayList<AppEnviroData> appDataArray = appDataController.getAppEnviroFromDb();
        appDataController.close();
        String appType="";
        for(int i=0;i<appDataArray.size();i++)
        {
            appType=appDataArray.get(i).getItemwisesale().trim();

        }



        salesController.open();
        ArrayList<Order> orders=new ArrayList<Order>();
        orders=salesController.getUploadMainSalesList(vdate);
        salesController.close();
        try{
            int vId=0;
            int vno=0;
            String docId="0";

            for (int j = 0; j < orders.size(); j++) {

                Date date = null;
                Date ndate = null;
                try {
                    date = sdf1.parse(orders.get(j).getVDate());
//						ndate=sdf1.parse(demoTransactions.get(j).getNextVisitDate());
                } catch (java.text.ParseException e) {
//						exceptionData.setExceptionData(e.toString(), "insertOrder");
                    // TODO Auto-generated catch block
                    mSuccess = true;
                    e.printStackTrace();
                    writeLog("32 Exception: "+e.getMessage());
                }
                try
                {
                    data = null;
                    data = new java.sql.Date(date.getTime());
                }
                catch (Exception e)
                {
                    System.out.println(e);
                    mSuccess = true;
                    writeLog("33 Exception: "+e.getMessage());
                }
                vId = Integer.parseInt(orders.get(j).getVisId());
                if (orders.get(j).getUserId() == null)
                {
                    orders.get(j).setUserId(preferences2.getString("USER_ID", "0"));
                }
                if (orders.get(j).getUserId().equals("null")) {
                    orders.get(j).setUserId(preferences2.getString("USER_ID", "0"));
                }

                if (orders.get(j).getSMId() == null) {
                    orders.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                }
                if (orders.get(j).getSMId().equals("null")) {
                    orders.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                }

                ArrayList<NameValuePair> nameValuePairs = null;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/JSInsertTransSaleReturns");
                    nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("VisId", Integer.toString(vId)));
                    nameValuePairs.add(new BasicNameValuePair("Android_Id", orders.get(j).getAndroid_id()));



                    nameValuePairs.add(new BasicNameValuePair("UserId", orders.get(j).getUserId()));

                    nameValuePairs.add(new BasicNameValuePair("VDate", data.toString()));
                    nameValuePairs.add(new BasicNameValuePair("SMId", orders.get(j).getSMId()));
                    nameValuePairs.add(new BasicNameValuePair("PartyId", orders.get(j).getPartyId()));
                    nameValuePairs.add(new BasicNameValuePair("AreaId", orders.get(j).getAreaId()));
//                        nameValuePairs.add(new BasicNameValuePair("Remarks", (orders.get(j).getRemarks()==null?"":orders.get(j).getRemarks().replaceAll("'", "''"))));
                    nameValuePairs.add(new BasicNameValuePair("Remarks", ""));
                    nameValuePairs.add(new BasicNameValuePair("OrderAmount", orders.get(j).getOrderAmount()));
                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                    /*****************		START		******************/
                    if(orders.get(j).getLongitude() == null || orders.get(j).getLongitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", orders.get(j).getLongitude()));
                    }
                    if(orders.get(j).getLatitude() == null || orders.get(j).getLatitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude",orders.get(j).getLatitude()));
                    }
                    if(orders.get(j).getLatlngTime()== null || orders.get(j).getLatlngTime().isEmpty() )
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",orders.get(j).getLatlngTime()));
                    }
                       /* nameValuePairs.add(new BasicNameValuePair("latitude",orders.get(j).getLatitude()));
                        nameValuePairs.add(new BasicNameValuePair("longitude",orders.get(j).getLongitude()));
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",orders.get(j).getLatlngTime()));*/
                    /*****************		END		******************/
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);
                    new DbCon(context).insertExportImportLogData(orders.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransOrder",response1);
                    System.out.println("Response : " + response1);
//						JSONObject json = new JSONObject(response1);
                    JSONArray jsonarray = new JSONArray(response1);
                    JSONObject json = null;
                    for (int k = 0; k < jsonarray.length(); k++) {
                        json = jsonarray.getJSONObject(k);

                    }
                    jsonarray=null;
                    try {
                        vno = Integer.parseInt(json.getString("Id"));
                        docId = json.getString("DocumentId");
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        timeStamp=json.getString("MS");
                        /*****************		END		******************/

                        System.out.println(vno);
                    } catch (Exception e) {
                        mSuccess = true;
                        System.out.println(e);
                        writeLog("34 Exception: "+e.getMessage());
                    }

                } catch (Exception e) {
                    mSuccess = true;
                    System.out.println("Exception : " + e.getMessage());
                    writeLog("35 Exception: "+e.getMessage()+"\nParams="+nameValuePairs.toString());
                    new DbCon(context).insertExportImportLogData(orders.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransOrder",e.getMessage());
                }
                boolean status = false;

                if (!mSuccess) {
                    if(vno>0 && !docId.equals("0")){
                        salesController.open();
                        //status = orderController.updateMultipleOrderUpload(orders.get(j).getAndroid_id(), docId, vno, vId);
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        status = salesController.updateMultipleOrderUpload(orders.get(j).getAndroid_id(), docId, vno, vId,timeStamp);
                        /*****************		END		******************/

                        salesController.close();
                        System.out.println("Commiting data here....");
                        System.out.println("j=" + j);
                        if (!status) {
//                                mSuccess = true;
                        }
                    }
                }
            }
            orders=null;
        }

        catch(Exception e){
            mSuccess=true;
            e.printStackTrace();
        }

        sales1Controller.open();
        orders1 = sales1Controller.getUploadOrder1List(vdate);
        sales1Controller.close();
        try {

            for (int j = 0; j < orders1.size(); j++) {
                Date date = null;

                try {
                    date = sdf1.parse(orders1.get(j).getVDate());
//											ndate=sdf1.parse(demoTransactions.get(j).getNextVisitDate());
                } catch (java.text.ParseException e) {
                    mSuccess=true;
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    data = null;
                    data = new java.sql.Date(date.getTime());
                } catch (Exception e) {
                    mSuccess=true;
                    System.out.println(e);
                }
                if (orders1.get(j).getUserId() == null ) {
                    orders1.get(j).setUserId(preferences2.getString("USER_ID", "0"));
                }
                if (orders1.get(j).getUserId().equals("null")) {
                    orders1.get(j).setUserId(preferences2.getString("USER_ID", "0"));


                }

                if (orders1.get(j).getSMId() == null)
                {
                    orders1.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));
                }
                if (orders1.get(j).getSMId().equals("null")) {
                    orders1.get(j).setSMId(preferences2.getString("CONPERID_SESSION", "0"));

                }

                ArrayList<NameValuePair> nameValuePairs = null;
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/JSInsertTransSaleReturns1");
                    nameValuePairs = new ArrayList<NameValuePair>(1);
                    nameValuePairs.add(new BasicNameValuePair("SRetId", orders1.get(j).getOrdId()));
                    nameValuePairs.add(new BasicNameValuePair("VisId", orders1.get(j).getVisId().trim()));
                    nameValuePairs.add(new BasicNameValuePair("Sno", orders1.get(j).getSno()));
                    nameValuePairs.add(new BasicNameValuePair("UserId", orders1.get(j).getUserId().trim()));
                    nameValuePairs.add(new BasicNameValuePair("VDate", data.toString()));
                    nameValuePairs.add(new BasicNameValuePair("SMId", orders1.get(j).getSMId().trim()));
                    nameValuePairs.add(new BasicNameValuePair("PartyId", orders1.get(j).getPartyId().trim()));
                    nameValuePairs.add(new BasicNameValuePair("AreaId", orders1.get(j).getAreaId().trim()));
                    nameValuePairs.add(new BasicNameValuePair("ItemId", orders1.get(j).getItemId().trim()));
                    nameValuePairs.add(new BasicNameValuePair("Qty", orders1.get(j).getQty().trim()));
                    nameValuePairs.add(new BasicNameValuePair("Rate", orders1.get(j).getRate().trim()));
//                  Util util=new Util();
                    String remark="NA";
                  /*  try{remark=util.validateAlfa(orders1.get(j).getRemarks());}
                    catch(Exception e){
                        e.printStackTrace();
                        remark="NA";
                        writeLog("35 Exception: "+e.getMessage());
                    }*/
                    nameValuePairs.add(new BasicNameValuePair("Remarks","" ));
                    nameValuePairs.add(new BasicNameValuePair("amount", orders1.get(j).getAmount().replaceAll("'", "''")));
                    nameValuePairs.add(new BasicNameValuePair("Android_Id", orders1.get(j).getOrderAndroid_Id()));
                    nameValuePairs.add(new BasicNameValuePair("Android_Id1", orders1.get(j).getAndroid_Id()));

                    if(orders1.get(j).getCases()==null || orders1.get(j).getCases().isEmpty()){
                        nameValuePairs.add(new BasicNameValuePair("cases","0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("cases",orders1.get(j).getCases()));
                    }
                    if(orders1.get(j).getUnit() == null || orders1.get(j).getUnit().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("unit","0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("unit",orders1.get(j).getUnit()));
                    }

                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                    /*****************		START		******************/
                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                    /*****************		START		******************/
                    if(orders1.get(j).getLongitude() == null || orders1.get(j).getLongitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", "0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("longitude", orders1.get(j).getLongitude()));
                    }
                    if(orders1.get(j).getLatitude() == null || orders1.get(j).getLatitude().isEmpty())
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude","0.0"));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("latitude",orders1.get(j).getLatitude()));
                    }
                    if(orders1.get(j).getLatlngTimeStamp()== null || orders1.get(j).getLatlngTimeStamp().isEmpty() )
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",String.valueOf(System.currentTimeMillis())));
                    }
                    else
                    {
                        nameValuePairs.add(new BasicNameValuePair("lat_long_dt",orders1.get(j).getLatlngTimeStamp()));
                    }

                    nameValuePairs.add(new BasicNameValuePair("BatchNo", orders1.get(j).getBatchNo()));
                    nameValuePairs.add(new BasicNameValuePair("MFD", orders1.get(j).getMfd()));

                    /*****************		END		******************/
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    ResponseHandler<String> responseHandler = new BasicResponseHandler();
                    final String response1 = httpclient.execute(httppost, responseHandler);
                    new DbCon(context).insertExportImportLogData(orders1.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransOrder1",response1);
                    System.out.println("Response : " + response1);
                    JSONArray jsonarray = new JSONArray(response1);
                    JSONObject json=null;
                    for (int k = 0; k < jsonarray.length(); k++) {
                        json = jsonarray.getJSONObject(k);

                    }
                    jsonarray=null;
                    try {
                        vno = Integer.parseInt(json.getString("Id"));
//							docId=json.getString("DocumentId");
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        timeStamp=json.getString("MS");
                        /*****************		END		******************/

                        System.out.println(vno);
                    } catch (Exception e) {
                        mSuccess=true;
                        System.out.println(e);
                        writeLog("38 Exception: "+e.getMessage());
                    }

                } catch (Exception e) {
                    mSuccess=true;
                    System.out.println("Exception : " + e.getMessage());
                    writeLog("39 Exception: "+e.getMessage());
                    new DbCon(context).insertExportImportLogData(orders.get(j).getSMId(),nameValuePairs.toString(),"JSInsertTransOrder1",e.getMessage());
                }
                long id=0;

                if(!mSuccess) {
                    if(vno>0) {
                        sales1Controller.open();
                        //id = order1Controller.updateOrder1Upload(orders1.get(j).getAndroid_Id());
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        id = sales1Controller.updateOrder1Upload(orders1.get(j).getAndroid_Id(),timeStamp);
                        /*****************		END		******************/
                        sales1Controller.close();
                        System.out.println("Commiting data here....");

                    }
                }
            }
        }

        catch(Exception e){
            mSuccess=true;
            e.printStackTrace();
            writeLog("40 Exception: "+e.getMessage());
        }

        return mSuccess;
    }

}
