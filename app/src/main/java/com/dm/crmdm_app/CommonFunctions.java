package com.dm.crmdm_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dm.controller.CityController;
import com.dm.controller.CountryController;
import com.dm.controller.ItemController;
import com.dm.controller.ProductClassController;
import com.dm.controller.ProductGroupController;
import com.dm.controller.SegmentController;
import com.dm.controller.StateController;
import com.dm.controller.UserDataController;
import com.dm.database.DatabaseConnection;
import com.dm.library.ConnectionDetector;
import com.dm.library.Constant;
import com.dm.library.Custom_Toast;
import com.dm.library.DbCon;
import com.dm.model.Country;
import com.dm.parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by dataman on 12/13/2017.
 */

public class CommonFunctions  {
    Context context;
    UserDataController userDataController;
    public static boolean mSuccess=false;
    String Solidsmid;
    String server;
    ConnectionDetector connectionDetector;
    public  CommonFunctions(Context context){
        this.context=context;
        userDataController=new UserDataController(context);
        connectionDetector=new ConnectionDetector(context);
        SharedPreferences preferences1 = context.getSharedPreferences("MyPref", MODE_PRIVATE);
        Solidsmid = preferences1.getString("CONPERID_SESSION","");
        this.server=Constant.SERVER_WEBSERVICE_URL;

    }








    public boolean insertCityTimestampWise()
    {
        int mExit=0;
        String maxTimeStamp = "0";
        CityController cityController =new CityController(context);
        long serverMaxTimeStamp=getMaxTimestampFromServer("Cities");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_CITYMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if (connectionDetector.isConnectingToInternet()) {
                    JSONParser jParser = new JSONParser();
                    try {
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + Constant.SERVER_WEBSERVICE_URL + "/And_Sync.asmx/xJSGetCities_TimeStampWise?minDate=" + maxTimeStamp));
                        if(jsonarray.length() == 0)
                        {
                            mSuccess = false;
                            break;
                        }
                        cityController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            cityController.insertCity(
                                    jsonarray.getJSONObject(i).getString("Cid"),
                                    jsonarray.getJSONObject(i).getString("NM"),
                                    jsonarray.getJSONObject(i).getString("Did"),
                                    jsonarray.getJSONObject(i).getString("MS"),
                                    jsonarray.getJSONObject(i).getString("SId"),
                                    jsonarray.getJSONObject(i).getString("active")
                            );
                        }
                        jsonarray = null;
                        cityController.close();
                    } catch (Exception e) {
                        cityController.close();
                        System.out.println(e);
                        mSuccess = true;
                    }
                }
                else {mSuccess = true;}
                if ((Long.parseLong(maxTimeStamp) >= serverMaxTimeStamp)) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mExit++;
                    if (mExit>0) break;}
            }
        }


        return mSuccess;
    }

    public boolean insertCountry()
    {
        CountryController countryController=new CountryController(context);
        JSONParser jParser = new JSONParser();
        ArrayList<Country> countries = new ArrayList<Country>();
        int mExit = 0;

        String maxTimeStamp = "";
        long serverMaxTimeStamp = getMaxTimestampFromServer("Country");
        if (serverMaxTimeStamp != 0) {
            while (true) {
                userDataController.open();
                maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_COUNTRYMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                    maxTimeStamp = "-1";
                }
                if(connectionDetector.isConnectingToInternet()){
                    try{
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+Constant.SERVER_WEBSERVICE_URL+"/And_Sync.asmx/xJSGetCountry_TimeStampWise?minDate="+maxTimeStamp));
                        if(jsonarray.length() == 0)
                        {
                            mSuccess = false;
                            return mSuccess;
                        }
                        countryController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            Country country = new Country();
                            country.setCreatedDate(obj.getString("MS"));
                            country.setSync_id(" ");
                            country.setDescription(obj.getString("NM"));
                            country.setActive(jsonarray.getJSONObject(i).getString("active"));
                            country.setId(obj.getString("Cid"));

                            countries.add(country);

                        }
                        for(int i=0;i<countries.size();i++){
                            countryController.insertCountry((countries.get(i)));
                            System.err.println("wecode imop"+countries.get(i).getId()+"\n"+"Description imo"+countries.get(i).getDescription());
                        }
                        countryController.close();
                    }catch (Exception e)
                    {
                        System.out.println(e);
                        mSuccess=true;
                    }
                }
                else {mSuccess = true;}
                if ((Long.parseLong(maxTimeStamp) >= serverMaxTimeStamp)) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //  Exportlog(e.getMessage());
                    }
                    mExit++;
                    if (mExit > 0) break;
                }
            }
        }

        return mSuccess;
    }
    public boolean insertState() {
        int mExit = 0;
        String maxTimeStamp = "0";
        StateController stateController = new StateController(context);
        JSONParser jParser = new JSONParser();
        long serverMaxTimeStamp = getMaxTimestampFromServer("States");
        if (serverMaxTimeStamp != 0) {
            while (true) {
                userDataController.open();
                maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_STATEMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                    maxTimeStamp = "-1";
                }
                if (connectionDetector.isConnectingToInternet()) {

                    try {
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + Constant.SERVER_WEBSERVICE_URL + "/And_Sync.asmx/xJSGetStates_TimeStampWise?minDate=" + maxTimeStamp));
                        if (jsonarray.length() == 0) {
                            mSuccess = false;
                            return mSuccess;
                        }
                        stateController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            stateController.insertState(jsonarray.getJSONObject(i).getString("NM"), jsonarray.getJSONObject(i).getString("Sid"), jsonarray.getJSONObject(i).getString("Rid"),jsonarray.getJSONObject(i).getString("MS"),jsonarray.getJSONObject(i).getString("active"));
                        }
                        jsonarray = null;
                        stateController.close();
                    } catch (Exception e) {
                        stateController.close();
                        System.out.println(e);
                        mSuccess = true;
                    }
                } else {
                    mSuccess = true;
                }
                if ((Long.parseLong(maxTimeStamp) >= serverMaxTimeStamp)) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mExit++;
                    if (mExit > 0) break;
                }
            }
        }
        return mSuccess;


    }
    long getMaxTimestampFromServer(String master)
    {
        JSONParser jParser=new JSONParser();long maxTime=0;
        if (connectionDetector.isConnectingToInternet()) {
            try {
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + Constant.SERVER_WEBSERVICE_URL + "/And_Sync.asmx/GetMaxTimeStamp_CRM?master=" + master.trim()));
                Log.i("ServerMaxTime","http://" + Constant.SERVER_WEBSERVICE_URL + "/And_Sync.asmx/GetMaxTimeStamp_CRM?master=" + master.trim());
                for (int i = 0; i < jsonarray.length(); i++) {
                    try {
                        maxTime = Long.parseLong((jsonarray.getJSONObject(i).getString("MaxTmStmp") == null || jsonarray.getJSONObject(i).getString("MaxTmStmp").equals("") || jsonarray.getJSONObject(i).getString("MaxTmStmp").equalsIgnoreCase("null") ? "0" : jsonarray.getJSONObject(i).getString("MaxTmStmp")));
                    } catch (Exception e) {
                        System.out.println(e);
                        mSuccess = true;
                    }
                }
                jsonarray = null;
            } catch (Exception e) {
                System.out.println(e);
                mSuccess = true;
            }
        }

        else {mSuccess = true;}
        return maxTime;
    }

    public boolean insertProductClass()
    {
        ProductClassController productClassController= new ProductClassController(context);
        JSONParser jParser = new JSONParser();
        int mExit=0;
        String maxTimeStamp = "0";
        long serverMaxTimeStamp=getMaxTimestampFromServer("ProClass");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_PRODUCTCLASSMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if(connectionDetector.isConnectingToInternet()){
                    try {
                        //publishProgress("ProductClass....Reading From Web");
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetProductClass?minDate="+maxTimeStamp));
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        productClassController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {

                            productClassController.insertProductClass(
                                    jsonarray.getJSONObject(i).getString("Id"),
                                    jsonarray.getJSONObject(i).getString("NM"),
                                    jsonarray.getJSONObject(i).getString("MS"));
                            //publishProgress("ProductClass Downloading...."+ count++);
                        }
                        jsonarray=null;
                        productClassController.close();

                    }catch (Exception e)
                    {
                        productClassController.close();
                        System.out.println(e);
                        mSuccess=true;
                    }
                }
                else {mSuccess = true;}
                if ((Long.parseLong(maxTimeStamp) >= serverMaxTimeStamp)) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mExit++;
                    if (mExit>0) break;}
            }
        }
        return mSuccess;
    }

    public boolean insertSegment()
    {
        SegmentController segmentController= new SegmentController(context);
        JSONParser jParser = new JSONParser();
        int mExit=0;
        String maxTimeStamp = "0";
        long serverMaxTimeStamp=getMaxTimestampFromServer("Prosegment");
        Log.e("AreaData",String.valueOf(serverMaxTimeStamp));
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_SEGMENTMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if(connectionDetector.isConnectingToInternet()){
                    try {
                        //publishProgress("Segment....Reading From Web");
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetSegment?minDate="+maxTimeStamp));
                        Log.e("AreaData","http://"+server+"/And_Sync.asmx/xJSGetSegment?minDate="+maxTimeStamp);

                        segmentController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            segmentController.insertSegment(
                                    jsonarray.getJSONObject(i).getString("Nm"),
                                    jsonarray.getJSONObject(i).getString("Id"),
                                    jsonarray.getJSONObject(i).getString("MS"),
                                    jsonarray.getJSONObject(i).getString("active")

                            );
                            //publishProgress("Segment Downloading...."+ count++);
                        }

                        segmentController.close();
                    }catch (Exception e)
                    {
                        System.out.println(e);
                        mSuccess=true;
                    }
                }else {mSuccess = true;}
                if ((Long.parseLong(maxTimeStamp) >= serverMaxTimeStamp)) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mExit++;
                    if (mExit>0) break;}
            }
        }
        return mSuccess;
    }
    public boolean insertProductGroup()
    {
        ProductGroupController productGroupController= new ProductGroupController(context);
        JSONParser jParser = new JSONParser();
        int mExit=0;
        String maxTimeStamp = "0";
        long serverMaxTimeStamp=getMaxTimestampFromServer("ProGroup");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_PRODUCTGROUPMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if(connectionDetector.isConnectingToInternet()){
                    try {
                        //publishProgress("ProductGroup....Reading From Web");
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetProductGroup?minDate="+maxTimeStamp));
                        Log.i("ProducGroup","http://"+server+"/And_Sync.asmx/xJSGetProductGroup?minDate="+maxTimeStamp+"-"+jsonarray.length());
                        productGroupController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            productGroupController.insertProductGroup(jsonarray.getJSONObject(i).getString("Nm"),jsonarray.getJSONObject(i).getString("Id"),jsonarray.getJSONObject(i).getString("MS"), jsonarray.getJSONObject(i).getString("active"));
                            //publishProgress("ProductGroup Downloading...."+ count++);
                        }
                        jsonarray=null;
                        productGroupController.close();

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                        mSuccess=true;
                    }
                }
                else
                {
                    mSuccess = true;
                }
                if ((Long.parseLong(maxTimeStamp) >= serverMaxTimeStamp)) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mExit++;
                    if (mExit>0) break;}
            }
        }
        return mSuccess;
    }



    public boolean insertItemTimestampWise()
    {
        String maxTimeStamp = "0";int mExit=0;
        ItemController itemController=new ItemController(context);
       // long serverMaxTimeStamp=getMaxTimestampFromServer("Products");
//        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_PRODUCTMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                    maxTimeStamp = "-1";
                }
                if (connectionDetector.isConnectingToInternet()) {
                    JSONParser jParser = new JSONParser();
                    //publishProgress("Item....Reading From Web");
                    try {
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray(Constant.SERVER_WEBSERVICE_URL + Constant.getProducts+ "?maxid=" + maxTimeStamp));
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        itemController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
//                            itemController.insertItem(
//                                    jsonarray.getJSONObject(i).getString("SyncId"),
//                                    jsonarray.getJSONObject(i).getString("Clid"),
//                                    jsonarray.getJSONObject(i).getString("Uid"),
//                                    jsonarray.getJSONObject(i).getString("DM"),
//                                    jsonarray.getJSONObject(i).getString("DP"),
//                                    jsonarray.getJSONObject(i).getString("Itid"),
//                                    jsonarray.getJSONObject(i).getString("Itnm"),
//                                    jsonarray.getJSONObject(i).getString("Itcd"),
//                                    jsonarray.getJSONObject(i).getString("MRP"),
//                                    jsonarray.getJSONObject(i).getString("Pg"),
//                                    jsonarray.getJSONObject(i).getString("RP"),
//                                    jsonarray.getJSONObject(i).getString("SegId"),
//                                    jsonarray.getJSONObject(i).getString("StdPk"),
//                                    jsonarray.getJSONObject(i).getString("Unit"),
//                                    jsonarray.getJSONObject(i).getString("MS"),
//                                    jsonarray.getJSONObject(i).getString("active")
//                            );

                            itemController.insertItem(

                                    jsonarray.getJSONObject(i).getString("Id"),
                                    jsonarray.getJSONObject(i).getString("ProductName"),


                                    jsonarray.getJSONObject(i).getString("Id")//ms

                            );
                            //publishProgress("Item Downloading...." + count++);
                        }
                        jsonarray = null;
                        itemController.close();
                    } catch (Exception e) {
                        System.out.println(e);
                        mSuccess = true;
                    }
                } else {
                    mSuccess = true;
                }

            }
//        }
        return mSuccess;
    }


    public boolean insertEmployee()
    {
        if (connectionDetector.isConnectingToInternet()) {
            JSONParser jParser = new JSONParser();
            //publishProgress("Item....Reading From Web");
            try
            {
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray(  Constant.SERVER_WEBSERVICE_URL + Constant.JSGetEmployee));
                if (jsonarray.length() == 0) {
                    mSuccess = false;

                }
                else
                {
                    SharedPreferences pref = context.getSharedPreferences("EmpData",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("emplist",jsonarray.toString());
                    editor.commit();
                }
            } catch (Exception e) {
                mSuccess = true;
                e.printStackTrace();
            }
        }
        return mSuccess;
    }


    public boolean insertStatusData() {
        if (connectionDetector.isConnectingToInternet()) {
            JSONParser jParser = new JSONParser();
            //publishProgress("Item....Reading From Web");
            try {
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray(Constant.SERVER_WEBSERVICE_URL + Constant.getStatus));
                if (jsonarray.length() == 0) {
                    mSuccess = false;

                }
                else
                {
                    SharedPreferences pref = context.getSharedPreferences("StatusData",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("statuslist",jsonarray.toString());
                    editor.commit();
                }
            } catch (Exception e) {
                mSuccess = true;
                e.printStackTrace();
            }
        }
        return mSuccess;
    }

    public boolean insertDesignation()
    {
        if (connectionDetector.isConnectingToInternet()) {
            JSONParser jParser = new JSONParser();
            //publishProgress("Item....Reading From Web");
            try {
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray(Constant.SERVER_WEBSERVICE_URL + Constant.designation));
                if (jsonarray.length() == 0) {
                    mSuccess = false;

                }
                else
                {
                    SharedPreferences pref = context.getSharedPreferences("DesignationData",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("desiglist",jsonarray.toString());
                    editor.commit();
                }
            } catch (Exception e) {
                mSuccess = true;
                e.printStackTrace();
            }
        }
        return mSuccess;
    }


    public boolean insertSubIndustry()
    {
        String maxTimeStamp = "0";int mExit=0;
        ItemController itemController=new ItemController(context);
        // long serverMaxTimeStamp=getMaxTimestampFromServer("Products");
//        if(serverMaxTimeStamp!=0) {
        while (true) {
            userDataController.open();
            maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_SUBINDUSTRYMAST));
            userDataController.close();
            if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                maxTimeStamp = "-1";
            }
            if (connectionDetector.isConnectingToInternet()) {
                JSONParser jParser = new JSONParser();
                //publishProgress("Item....Reading From Web");
                try {
                    JSONArray jsonarray = new JSONArray(jParser.getJSONArray( Constant.SERVER_WEBSERVICE_URL + Constant.get_subindustry+ "?maxid=" + maxTimeStamp));
                    if(jsonarray.length() == 0)
                    {
                        mSuccess=false;
                        break;
                    }
                    itemController.open();
                    for (int i = 0; i < jsonarray.length(); i++) {
//                            itemController.insertItem(
//                                    jsonarray.getJSONObject(i).getString("SyncId"),
//                                    jsonarray.getJSONObject(i).getString("Clid"),
//                                    jsonarray.getJSONObject(i).getString("Uid"),
//                                    jsonarray.getJSONObject(i).getString("DM"),
//                                    jsonarray.getJSONObject(i).getString("DP"),
//                                    jsonarray.getJSONObject(i).getString("Itid"),
//                                    jsonarray.getJSONObject(i).getString("Itnm"),
//                                    jsonarray.getJSONObject(i).getString("Itcd"),
//                                    jsonarray.getJSONObject(i).getString("MRP"),
//                                    jsonarray.getJSONObject(i).getString("Pg"),
//                                    jsonarray.getJSONObject(i).getString("RP"),
//                                    jsonarray.getJSONObject(i).getString("SegId"),
//                                    jsonarray.getJSONObject(i).getString("StdPk"),
//                                    jsonarray.getJSONObject(i).getString("Unit"),
//                                    jsonarray.getJSONObject(i).getString("MS"),
//                                    jsonarray.getJSONObject(i).getString("active")
//                            );

                        itemController.insertSubIndustry(

                                jsonarray.getJSONObject(i).getString("Id"),
                                jsonarray.getJSONObject(i).getString("IndustryName"),

                                jsonarray.getJSONObject(i).getString("Industrysubname"),
                                jsonarray.getJSONObject(i).getString("Id")//ms

                        );
                        //publishProgress("Item Downloading...." + count++);
                    }
                    jsonarray = null;
                    itemController.close();
                } catch (Exception e) {
                    System.out.println(e);
                    mSuccess = true;
                }
            } else {
                mSuccess = true;
            }

        }
//        }
        return mSuccess;
    }



    public boolean insertIndustry()
    {
        String maxTimeStamp = "0";int mExit=0;
        ItemController itemController=new ItemController(context);
        // long serverMaxTimeStamp=getMaxTimestampFromServer("Products");
//        if(serverMaxTimeStamp!=0) {
        while (true) {
            userDataController.open();
            maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_INDUSTRYMAST));
            userDataController.close();
            if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                maxTimeStamp = "-1";
            }
            if (connectionDetector.isConnectingToInternet()) {
                JSONParser jParser = new JSONParser();
                //publishProgress("Item....Reading From Web");
                try {
                    JSONArray jsonarray = new JSONArray(jParser.getJSONArray(Constant.SERVER_WEBSERVICE_URL + Constant.get_industry+ "?maxid=" + maxTimeStamp));
                    if(jsonarray.length() == 0)
                    {
                        mSuccess=false;
                        break;
                    }
                    itemController.open();
                    for (int i = 0; i < jsonarray.length(); i++) {
//                            itemController.insertItem(
//                                    jsonarray.getJSONObject(i).getString("SyncId"),
//                                    jsonarray.getJSONObject(i).getString("Clid"),
//                                    jsonarray.getJSONObject(i).getString("Uid"),
//                                    jsonarray.getJSONObject(i).getString("DM"),
//                                    jsonarray.getJSONObject(i).getString("DP"),
//                                    jsonarray.getJSONObject(i).getString("Itid"),
//                                    jsonarray.getJSONObject(i).getString("Itnm"),
//                                    jsonarray.getJSONObject(i).getString("Itcd"),
//                                    jsonarray.getJSONObject(i).getString("MRP"),
//                                    jsonarray.getJSONObject(i).getString("Pg"),
//                                    jsonarray.getJSONObject(i).getString("RP"),
//                                    jsonarray.getJSONObject(i).getString("SegId"),
//                                    jsonarray.getJSONObject(i).getString("StdPk"),
//                                    jsonarray.getJSONObject(i).getString("Unit"),
//                                    jsonarray.getJSONObject(i).getString("MS"),
//                                    jsonarray.getJSONObject(i).getString("active")
//                            );

                        itemController.insertIndustry(

                                jsonarray.getJSONObject(i).getString("Id"),
                                jsonarray.getJSONObject(i).getString("IndustryName"),


                                jsonarray.getJSONObject(i).getString("Id")//ms

                        );
                        //publishProgress("Item Downloading...." + count++);
                    }
                    jsonarray = null;
                    itemController.close();
                } catch (Exception e) {
                    System.out.println(e);
                    mSuccess = true;
                }
            } else {
                mSuccess = true;
            }

        }
//        }
        return mSuccess;
    }



    //////////////////////////////////DAR///////////

    public boolean insertCityTimestampWise(String logId)
    {
        int mExit=0;
        String maxTimeStamp = "0";
        CityController cityController =new CityController(context);
//        long serverMaxTimeStamp=getMaxTimestampFromServer("Cities");
//        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_CITYMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if (connectionDetector.isConnectingToInternet()) {
                    JSONParser jParser = new JSONParser();
                    try {
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray(Constant.SERVER_WEBSERVICE_URL +Constant.get_city+"?maxid=" + maxTimeStamp));
                        if(jsonarray.length() == 0)
                        {
                            mSuccess = false;
                            break;
                        }
                        cityController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            cityController.insertCity(
                                    jsonarray.getJSONObject(i).getString("Id"),
                                    jsonarray.getJSONObject(i).getString("CityName"),
                                    "",
                                    jsonarray.getJSONObject(i).getString("Id"),//ms
                                    jsonarray.getJSONObject(i).getString("stateid"),
                                    ""
                            );
                        }
                        jsonarray = null;
                        cityController.close();
                    } catch (Exception e) {
                        cityController.close();
                        System.out.println(e);
                        mSuccess = true;
                    }
                }
                else {mSuccess = true;}
//                if ((Long.parseLong(maxTimeStamp) >= serverMaxTimeStamp)) {
//                    try {
//                        Thread.sleep(2);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    mExit++;
//                    if (mExit>0) break;}
            }
//        }


        return mSuccess;
    }

}
