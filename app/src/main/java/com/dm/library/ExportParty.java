package com.dm.library;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;

import com.dm.controller.AppDataController;
import com.dm.controller.PartyController;
import com.dm.model.AppData;
import com.dm.model.Party;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dataman on 3/10/2017.
 */
public class ExportParty {
    Context context;
     String conper;
    SharedPreferences preferences;ConnectionDetector connectionDetector;
    java.sql.Date data;AppDataController appDataController1;AppData appData;
    SimpleDateFormat sdf1;String server; ArrayList<AppData>appDataArray;
    Handler postPartyDataHandler = new Handler();boolean sync;

    public ExportParty(Context context,boolean sync) {
        this.context = context;
        this.sync=sync;
        preferences = context.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        conper = preferences.getString("CONPERID_SESSION", null);
        sdf1 = new SimpleDateFormat("dd/MMM/yyyy");
        appDataController1=new AppDataController(context);
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();
        connectionDetector = new ConnectionDetector(context);
        if(!sync) {postPartyDataHandler.postDelayed(postPartyThread, 0);}
    }

    private Runnable postPartyThread = new Runnable() {
        @Override
        public void run() {
            new PostParty().execute();
            postPartyDataHandler.postDelayed(postPartyThread, 60 * 60 * 1000);

        }
    };

    class PostParty extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            if (connectionDetector.isConnectingToInternet()) {
                    insertJSNewParty();
                }
              return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    public String insertJSNewParty()
    {
        String timeStamp="0";
        sdf1 = new SimpleDateFormat("dd/MMM/yyyy");
        java.sql.Date data;int vno=0;
        String status="";
        int pno=0;
        PartyController partycontroller=new PartyController(context);
        partycontroller.open();
        ArrayList<Party> parties=new ArrayList<Party>();
        parties=partycontroller.getPartyListUpload();
        partycontroller.close();

        if(parties.size()<=0)
        {
            if(!sync) {
                postPartyDataHandler.removeCallbacks(postPartyThread);
            }

        }
        try {

            for (int j = 0; j < parties.size(); j++)
            {
                if(parties.get(j).getParty_id().isEmpty())
                {
                    Date date=null;
                    Date ndate=null;
                    try {
                        date=sdf1.parse(parties.get(j).getCreatedDate());
//					ndate=sdf1.parse(demoTransactions.get(j).getNextVisitDate());
                    }
                    catch (java.text.ParseException e) {
//						exception=true;
//						exceptionData.setExceptionData(e.toString(), "insertDsr");
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    data=null;
                    data = new java.sql.Date(date.getTime());
                    System.out.println(data);
                    try {
                        HttpClient httpclient = new DefaultHttpClient();
//                        HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/JSInsertParty");
                        HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/JSInsertPartyLatLong");
                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                        nameValuePairs.add(new BasicNameValuePair("PartyId", "0"));
                        nameValuePairs.add(new BasicNameValuePair("PartyName", parties.get(j).getParty_name().replaceAll("'","''")));
                        nameValuePairs.add(new BasicNameValuePair("Address1", (parties.get(j).getAddress1()==null?"0":parties.get(j).getAddress1().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("Address2", (parties.get(j).getAddress2()==null?"0":parties.get(j).getAddress2().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("CityId", parties.get(j).getCity_id().trim()));
                        nameValuePairs.add(new BasicNameValuePair("AreaId", parties.get(j).getAreaId().trim()));
                        nameValuePairs.add(new BasicNameValuePair("BeatId", parties.get(j).getBeatId().trim()));
                        nameValuePairs.add(new BasicNameValuePair("UnderId", parties.get(j).getDistId()));
                        nameValuePairs.add(new BasicNameValuePair("Pin", parties.get(j).getPin()));
                        nameValuePairs.add(new BasicNameValuePair("Mobile", parties.get(j).getMobile()));
                        nameValuePairs.add(new BasicNameValuePair("Phone", (parties.get(j).getPhone()==null?"0":parties.get(j).getPhone())));
                        nameValuePairs.add(new BasicNameValuePair("Remark", (parties.get(j).getRemark()==null?"0":parties.get(j).getRemark().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("SyncId", (parties.get(j).getSync_id()==null?"0":parties.get(j).getSync_id().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("IndId", parties.get(j).getIndId().trim()));
                        nameValuePairs.add(new BasicNameValuePair("Potential", (parties.get(j).getPotential().equals("")?"0":parties.get(j).getPotential())));
                        nameValuePairs.add(new BasicNameValuePair("Active", String.valueOf(parties.get(j).getActive())));
                        nameValuePairs.add(new BasicNameValuePair("BlockReason", parties.get(j).getBlocked_Reason()));
                        nameValuePairs.add(new BasicNameValuePair("PartyType", parties.get(j).getParty_type_code()));
                        nameValuePairs.add(new BasicNameValuePair("ContactPerson", parties.get(j).getContact_person().replaceAll("'","''")));
                        nameValuePairs.add(new BasicNameValuePair("CSTNo", (parties.get(j).getCst_no()==null?"0":parties.get(j).getCst_no().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("VatTin", (parties.get(j).getVattin_no()==null?"0":parties.get(j).getVattin_no().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("ServiceTax", (parties.get(j).getServicetaxreg_No()==null?"0":parties.get(j).getServicetaxreg_No().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("PanNo", (parties.get(j).getPANNo()==null?"0":parties.get(j).getPANNo().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("UserId", parties.get(j).getUserId().trim()));
                        nameValuePairs.add(new BasicNameValuePair("DOA", parties.get(j).getDoa()));
                        nameValuePairs.add(new BasicNameValuePair("DOB", parties.get(j).getDob()));
                        nameValuePairs.add(new BasicNameValuePair("Email", (parties.get(j).getEmail()==null?"0":parties.get(j).getEmail().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("androidid", (parties.get(j).getAndroidId()==null?"0":parties.get(j).getAndroidId())));
                        nameValuePairs.add(new BasicNameValuePair("latitude", parties.get(j).getPatryLat()));
                        nameValuePairs.add(new BasicNameValuePair("longitude", parties.get(j).getPartyLng()));
                        nameValuePairs.add(new BasicNameValuePair("LatlngTime",parties.get(j).getPartyLatLngTimeStamp()));

                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        final String response1 = httpclient.execute(httppost, responseHandler);
                        System.out.println("Response : " + response1);
                        JSONArray jsonarray = new JSONArray(response1);
                        JSONObject json=null;
                        for (int k = 0; k < jsonarray.length(); k++) {
                            json = jsonarray.getJSONObject(k);
                        }

                        try {
                            pno = Integer.parseInt(json.getString("Id"));
                            status = json.getString("Status");
                            /************************		Write By Sandeep Singh 10-04-2017		******************/
                            /*****************		START		******************/
                                timeStamp=json.getString("MS");
                            /*****************		END		******************/
                            System.out.println(vno);
                        } catch (Exception e) {
                            System.out.println(e);

                        }

                    }
                    catch (Exception e) {
                        if(!sync) { postPartyDataHandler.removeCallbacks(postPartyThread);}
                        System.out.println("Exception : " + e.getMessage());

                    }



                    if(pno>0 && status.equalsIgnoreCase("Record Inserted Successfully"))
                    {


                        parties.get(j).setParty_id(String.valueOf(pno));
                        partycontroller.open();
//                        partycontroller.updatetPartyWebcode(parties.get(j).getParty_id(), parties.get(j).getAndroidId(),timeStamp);
//                        partycontroller.updatetPartyWebcodeInTransactions(parties.get(j).getParty_id(), parties.get(j).getAndroidId(),timeStamp);

                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        partycontroller.updatetPartyWebcode(parties.get(j).getParty_id(), parties.get(j).getAndroidId(),timeStamp);
                        partycontroller.updatetPartyWebcodeInTransactions(parties.get(j).getParty_id(), parties.get(j).getAndroidId(),timeStamp);
                        /*****************		END		******************/
                        partycontroller.close();
                       // return status;

                    }

                    if(pno < 0 && status.equalsIgnoreCase("Duplicate Mobile Exists"))
                    {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setCancelable(false);
                        alert.setMessage(status);
                        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        alert.create().show();
                        return status;
                    }


                }
                else{
                    Date date=null;
                    Date ndate=null;
                    try {
                        date=sdf1.parse(parties.get(j).getCreatedDate());
//					ndate=sdf1.parse(demoTransactions.get(j).getNextVisitDate());
                    }
                    catch (java.text.ParseException e) {
//						exception=true;
//						exceptionData.setExceptionData(e.toString(), "insertDsr");
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    data=null;
                    data = new java.sql.Date(date.getTime());
                    System.out.println(data);
                    try {
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/JSInsertParty");
                        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
//                        if(parties.get(j).getParty_id().equalsIgnoreCase(parties.get(j).getAndroidId()))
//                        {
//                            nameValuePairs.add(new BasicNameValuePair("PartyId", "0"));
//                        }
//                        else
//                        {
                            nameValuePairs.add(new BasicNameValuePair("PartyId", parties.get(j).getParty_id()));
                       // }
                        nameValuePairs.add(new BasicNameValuePair("PartyName", parties.get(j).getParty_name().replaceAll("'","''")));
                        nameValuePairs.add(new BasicNameValuePair("Address1", (parties.get(j).getAddress1()==null?"0":parties.get(j).getAddress1().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("Address2", (parties.get(j).getAddress2()==null?"0":parties.get(j).getAddress2().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("CityId", parties.get(j).getCity_id().trim()));
                        nameValuePairs.add(new BasicNameValuePair("AreaId", parties.get(j).getAreaId().trim()));
                        nameValuePairs.add(new BasicNameValuePair("BeatId", parties.get(j).getBeatId().trim()));
                        nameValuePairs.add(new BasicNameValuePair("UnderId", parties.get(j).getDistId()));
                        nameValuePairs.add(new BasicNameValuePair("Pin", parties.get(j).getPin()));
                        nameValuePairs.add(new BasicNameValuePair("Mobile", parties.get(j).getMobile()));
                        nameValuePairs.add(new BasicNameValuePair("Phone", (parties.get(j).getPhone()==null?"0":parties.get(j).getPhone())));
                        nameValuePairs.add(new BasicNameValuePair("Remark", (parties.get(j).getRemark()==null?"0":parties.get(j).getRemark().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("SyncId", (parties.get(j).getSync_id()==null?"0":parties.get(j).getSync_id().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("IndId", parties.get(j).getIndId().trim()));
                        nameValuePairs.add(new BasicNameValuePair("Potential", (parties.get(j).getPotential().equals("")?"0":parties.get(j).getPotential())));
                        nameValuePairs.add(new BasicNameValuePair("Active", String.valueOf(parties.get(j).getActive())));
                        nameValuePairs.add(new BasicNameValuePair("BlockReason", parties.get(j).getBlocked_Reason()));
                        nameValuePairs.add(new BasicNameValuePair("PartyType", parties.get(j).getParty_type_code()));
                        nameValuePairs.add(new BasicNameValuePair("ContactPerson", parties.get(j).getContact_person().replaceAll("'","''")));
                        nameValuePairs.add(new BasicNameValuePair("CSTNo", (parties.get(j).getCst_no()==null?"0":parties.get(j).getCst_no().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("VatTin", (parties.get(j).getVattin_no()==null?"0":parties.get(j).getVattin_no().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("ServiceTax", (parties.get(j).getServicetaxreg_No()==null?"0":parties.get(j).getServicetaxreg_No().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("PanNo", (parties.get(j).getPANNo()==null || parties.get(j).getPANNo().equalsIgnoreCase("null")?"0":parties.get(j).getPANNo().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("UserId", parties.get(j).getUserId().trim()));
                        nameValuePairs.add(new BasicNameValuePair("DOA", parties.get(j).getDoa()));
                        nameValuePairs.add(new BasicNameValuePair("DOB", parties.get(j).getDob()));
                        nameValuePairs.add(new BasicNameValuePair("Email", (parties.get(j).getEmail()==null?"0":parties.get(j).getEmail().replaceAll("'","''"))));
                        nameValuePairs.add(new BasicNameValuePair("androidid", (parties.get(j).getAndroidId()==null || parties.get(j).getAndroidId().equalsIgnoreCase("web") ?"0":parties.get(j).getAndroidId())));
                        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                        ResponseHandler<String> responseHandler = new BasicResponseHandler();
                        final String response1 = httpclient.execute(httppost, responseHandler);
                        System.out.println("Response : " + response1);
                        JSONArray jsonarray = new JSONArray(response1);
                        JSONObject json=null;
                        for (int k = 0; k < jsonarray.length(); k++) {
                            json = jsonarray.getJSONObject(k);

                        }

                        try {
                            pno = Integer.parseInt(json.getString("Id"));
                            status = json.getString("Status");
                            /************************		Write By Sandeep Singh 10-04-2017		******************/
                            /*****************		START		******************/
                            timeStamp=json.getString("MS");
                            /*****************		END		******************/
                            System.out.println(vno);
                        } catch (Exception e) {
                            System.out.println(e);

                        }

                        if(pno < 0 && status.equalsIgnoreCase("Duplicate Mobile Exists for"+ parties.get(j).getParty_name()))
                        {
                            AlertDialog.Builder alert = new AlertDialog.Builder(context);
                            alert.setCancelable(false);
                            alert.setMessage(status);
                            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                            alert.create().show();
                            //return status;
                        }
                    }
                    catch (Exception e) {
                        if(!sync) { postPartyDataHandler.removeCallbacks(postPartyThread);}
                        System.out.println("Exception : " + e.getMessage());
                    }

                    if(pno>0)
                    {
                        partycontroller.open();
                        //partycontroller.updatetPartyUpload(parties.get(j).getParty_id(),timeStamp);
                        /************************		Write By Sandeep Singh 10-04-2017		******************/
                        /*****************		START		******************/
                        partycontroller.updatetPartyUpload(String.valueOf(pno),timeStamp,parties.get(j).getAndroidId());
                        /*****************		END		******************/

//						partycontroller.updatetPartyWebcodeInTransactions(parties.get(j).getParty_id(), parties.get(j).getAndroidId());
                        partycontroller.close();
                        //return status;
                    }

                }
                System.out.println("Commiting data here....");
            }

        }
        catch(Exception e){
            //Handle errors for Class.forName
            if(!sync) { postPartyDataHandler.removeCallbacks(postPartyThread);}
            e.printStackTrace();
        }
        return "";
    }

}