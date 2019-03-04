package com.dm.library;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.controller.AppEnviroDataController;
import com.dm.controller.AreaController;
import com.dm.controller.AreaTypeController;
import com.dm.controller.BeatController;
import com.dm.controller.BeatPlanController;
import com.dm.controller.CityController;
import com.dm.controller.CollectionController;
import com.dm.controller.CompetitorController;
import com.dm.controller.CountryController;
import com.dm.controller.DemoTransactionController;
import com.dm.controller.DeviceInfoController;
import com.dm.controller.DistStockController;
import com.dm.controller.DistributorAreaController;
import com.dm.controller.DistributorController;
import com.dm.controller.DistrictController;
import com.dm.controller.DsrController;
import com.dm.controller.EnviroController;
import com.dm.controller.FailedVisitController;
import com.dm.controller.FailedVisitRemarkController;
import com.dm.controller.HistoryController;
import com.dm.controller.IndustryController;
import com.dm.controller.ItemController;
import com.dm.controller.Order1Controller;
import com.dm.controller.OrderController;
import com.dm.controller.OrderTransactionController;
import com.dm.controller.PartyController;
import com.dm.controller.PartyTypeController;
import com.dm.controller.PriceListController;
import com.dm.controller.ProductClassController;
import com.dm.controller.ProductGroupController;
import com.dm.controller.ProductSubGroupsController;
import com.dm.controller.ProjectController;
import com.dm.controller.RegionController;
import com.dm.controller.SchemeController;
import com.dm.controller.SegmentController;
import com.dm.controller.SmanController;
import com.dm.controller.StateController;
import com.dm.controller.TransCollectionController;
import com.dm.controller.TransportController;
import com.dm.controller.UserAreaController;
import com.dm.controller.UserDataController;
import com.dm.controller.VisitController;
import com.dm.controller.VisitNameController;
import com.dm.crmdm_app.DashBoradActivity;
import com.dm.crmdm_app.R;
import com.dm.database.DatabaseConnection;
import com.dm.model.AppData;
import com.dm.model.AppEnviroData;
import com.dm.model.AreaType;
import com.dm.model.Collection;
import com.dm.model.Country;
import com.dm.model.Entity;
import com.dm.model.Enviro;
import com.dm.model.History;
import com.dm.model.PartyType;
import com.dm.model.PriceList;
import com.dm.model.Project;
import com.dm.model.Region;
import com.dm.parser.JSONParser;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by dataman on 13-Apr-17.
 */
public class ImportSync {
    Context context;private PartyController partycontroller;private AreaController areaController;private ProgressBar progressBarImport;
    Activity activity;AreaTypeController areaTypeController;BeatController beatController;CityController cityController;
    private int progressStatus = 0;private TextView importprogress;private LinearLayout importLayout;DistributorController distributorController;
    DistributorAreaController distributorAreaController;private static  String conper;IndustryController industryController;
    ItemController itemController;PriceListController priceListController;ProductSubGroupsController productSubGroupsController;
    ProductGroupController productGroupController;ProductClassController productClassController;
    StateController stateController;UserAreaController userAreaController; AlertOkDialog dialogWithOutView;boolean isImport=false;
    private static String server1,server,username,db,pass;CountryController countryController;RegionController regionController;PartyTypeController partyTypeController;
    UserDataController userDataController;SmanController smanController;DsrController dsrController;SharedPreferences preferences;
    OrderTransactionController orderTransactionController;DemoTransactionController demoTransactionController;FailedVisitController failedVisitController;
    ArrayList<History> itemFromHistoryArrayList;HistoryController historyController;EnviroController enviroController;AlertOkDialog alertOkDialog;
    TextView importTextView;ExceptionData exceptionData; private static String  connectionUrl = "";ArrayList<String> valuesArrayList;
    FailedVisitRemarkController failedVisitRemarkController;String time="0";boolean mSuccess=false;int exeCount=0;
    SegmentController segmentController;TransportController transportController;File myFile=null;boolean flag=false;
    FileOutputStream fOut=null;int execount=0;int count;
    OutputStreamWriter myOutWriter = null;BeatPlanController beatPlanController;
    DistrictController districtController;ProjectController projectController;SchemeController schemeController;
    OrderController orderController;ConnectionDetector connectionDetector;ArrayList<AppData> appDataArray;
    AppEnviroDataController appEnviroDataController;boolean areaMatch=false;AppDataController appDataController1;
    public ImportSync(Context context){
        this.context=context;
        preferences=context.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        conper=preferences.getString("CONPERID_SESSION", null);
        userDataController=new UserDataController(context);
        connectionDetector=new ConnectionDetector(context);
        appDataController1=new AppDataController(context);
        appEnviroDataController=new AppEnviroDataController(context);
        enviroController=new EnviroController(context);
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();

    }
    public boolean insertSman()
    {
        count=1;
        smanController=new SmanController(context);
        JSONParser jParser = new JSONParser();
        int mExit=0;
        String maxTimeStamp = "0";
        long serverMaxTimeStamp=getMaxTimestampFromServer("SalesPerson");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_SALESMANMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}

                if(connectionDetector.isConnectingToInternet()){
                    try {
                        //publishProgress("Salesman....Reading From Web");
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetSrep?userId="+conper+"&minDate="+maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetSrep","Y");
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        smanController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            smanController.insertSman(
                                    jsonarray.getJSONObject(i).getString("Smid"),
                                    jsonarray.getJSONObject(i).getString("Lvl"),
                                    jsonarray.getJSONObject(i).getString("Uid"),
                                    jsonarray.getJSONObject(i).getString("Smnm"),
                                    jsonarray.getJSONObject(i).getString("MS")
                            );
                            //publishProgress("Salesman Downloading...."+ count++);
                        }
                        jsonarray=null;
                        smanController.close();
                    }catch (Exception e)
                    {
                        smanController.close();
                        System.out.println(e);
                        mSuccess=true;
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetSrep",e.getMessage());
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
	/*public boolean insertSman()
	{
		count=1;
		smanController=new SmanController(context);
		JSONParser jParser = new JSONParser();
		if(connectionDetector.isConnectingToInternet()){
			try {
				////publishProgress("Salesman....Reading From Web");
				JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetSrep?userId="+conper+"&minDate=0"));
				smanController.open();
				for (int i = 0; i < jsonarray.length(); i++) {
					smanController.insertSman(
							jsonarray.getJSONObject(i).getString("Smid"),
							jsonarray.getJSONObject(i).getString("Lvl"),
							jsonarray.getJSONObject(i).getString("Uid"),
							jsonarray.getJSONObject(i).getString("Smnm")

					);
					////publishProgress("Salesman Downloading...."+ count++);
				}
				jsonarray=null;
				smanController.close();
			}catch (Exception e)
			{
				smanController.close();
				System.out.println(e);
				mSuccess=true;
			}
		}

		return mSuccess;
	}
*/
    /*****************		END		******************/

  /*  public boolean insertPartyType()
    {
        count=1;
        JSONParser jParser = new JSONParser();
        partyTypeController=new PartyTypeController(context);
        ArrayList<PartyType> types = new ArrayList<PartyType>();
        if(connectionDetector.isConnectingToInternet()){
            try {
                //publishProgress("PartyType.... Reading From Web");
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetPartyType?minDate=0"));

                partyTypeController.open();
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    PartyType type = new PartyType();
                    type.setCreatedDate(" ");
                    type.setId(obj.getString("Id"));
                    type.setName(obj.getString("Nm"));
                    type.setCreatedDate(obj.getString("MS"));
                    types.add(type);

                }
                for(int i=0;i<types.size();i++){
                    partyTypeController.insertArea(types.get(i));
                    //publishProgress("PartyType Downloading...."+ count++);
                }
                partyTypeController.close();

            }catch (Exception e)
            {
                System.out.println(e);
                mSuccess=true;
            }
        }else {mSuccess = true;}
        return mSuccess;
    }*/



    public boolean insertScheme()
    {
        count=1;
        JSONParser jParser = new JSONParser();
        schemeController=new SchemeController(context);

        if(connectionDetector.isConnectingToInternet()){
            try {
                //publishProgress("Scheme....Reading From Web");
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetScheme?minDate=0"));
                schemeController.open();
                for (int i = 0; i < jsonarray.length(); i++) {

                    schemeController.insertScheme(jsonarray.getJSONObject(i).getString("Id"),jsonarray.getJSONObject(i).getString("Nm"),jsonarray.getJSONObject(i).getString("ms"));

                    //publishProgress("Scheme Downloading...."+ count++);
                }
                schemeController.close();
            }catch (Exception e)
            {
                schemeController.close();
                System.out.println(e);
                mSuccess=true;
            }
        }else {mSuccess = true;}
        return mSuccess;
    }



    public boolean insertProject()
    {
        count=1;
        JSONParser jParser = new JSONParser();
        projectController=new ProjectController(context);
        ArrayList<Project> projects = new ArrayList<Project>();
        int mExit = 0;

        String maxTimeStamp = "0";
        long serverMaxTimeStamp = getMaxTimestampFromServer("Project");

        if(serverMaxTimeStamp !=0) {

            if (connectionDetector.isConnectingToInternet()) {
                try {
                    //publishProgress("Project....  Reading from web");
                    while (true) {
                        userDataController.open();
                        maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_PROJECT));
                        userDataController.close();
                        if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                            maxTimeStamp = "-1";
                        }
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetProjects_TimeStampWise?minDate="+maxTimeStamp));

                        if(jsonarray.length()==0)
                        {
                            break;
                        }
                        projectController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            Project project = new Project();
                            project.setCreatedDate(" ");
                            //project.setActive(obj.getString("SyncId"));
                            project.setProjectid(obj.getString("Id"));
                            project.setProjectname(obj.getString("Nm"));
                            project.setCreatedDate(obj.getString("MS"));
                            projects.add(project);
                        }
                        for (int i = 0; i < projects.size(); i++) {
                            projectController.insertProject((projects.get(i)));
                            //publishProgress("Project Downloading...."+ count++);
                        }
                        projectController.close();
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    mSuccess = true;
                }
            } else {
                mSuccess = true;
            }
        }
        return mSuccess;
    }

    public boolean insertUser()
    {
        String pda_id="0";
        int dsrAllowDays=0;
        DeviceInfoController deviceInfoController =new DeviceInfoController(this.context);
        UserDataController userDataController =new UserDataController(this.context);
        deviceInfoController.open();
        pda_id=deviceInfoController.getpdaId();
        deviceInfoController.close();
        JSONParser jParser = new JSONParser();
        if(connectionDetector.isConnectingToInternet()){
            try {

                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetUserDetailByPDAId?PDA_Id=" + pda_id + "&minDate=0"));


                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    dsrAllowDays=Integer.parseInt((obj.getString("DSRAllowDays").equals("")?"0":obj.getString("DSRAllowDays")));
                }
                jsonarray=null;
//                if(dsrAllowDays!=0)
                    userDataController.open();
                userDataController.updateDsrAllowDaysValue(dsrAllowDays);
                userDataController.close();

            } catch (Exception e) {
                userDataController.close();
                System.out.println(e);
                mSuccess=true;
            }
        }else {mSuccess = true;}
        return mSuccess;
    }


    public boolean insertTransporter()
    {
        count=1;
        TransportController transportController = new TransportController(context);
        int mExit = 0;

        String maxTimeStamp = "";
        long serverMaxTimeStamp = getMaxTimestampFromServer("Transporter");
        if(serverMaxTimeStamp != 0)
        {
            while (true) {
                if (connectionDetector.isConnectingToInternet()) {
                    JSONParser jParser = new JSONParser();
                    //  ////publishProgress("Transporter....Reading from web");
                    userDataController.open();
                    maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_TRANSPORTER));
                    userDataController.close();
                    if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                        maxTimeStamp = "-1";
                    }
                    try {
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetTransporter_TimeStampWise?minDate="+maxTimeStamp));

                        if(jsonarray.length()==0)
                        {
                            break;
                        }
                        transportController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {

                            transportController.insertTransport(
                                    jsonarray.getJSONObject(i).getString("Id"),
                                    jsonarray.getJSONObject(i).getString("Nm")
                                    , jsonarray.getJSONObject(i).getString("MS")

                            );
                            //publishProgress("Transporter Downloading...."+ count++);
                        }
                        jsonarray = null;
                        transportController.close();
                    } catch (Exception e) {
                        transportController.close();
                        System.out.println(e);
                        mSuccess = true;
                    }

                } else {
                    mSuccess = true;
                }
            }
        }
        return mSuccess;
    }
   /* public boolean insertCountry()
    {
        count=1;
        countryController=new CountryController(context);
        JSONParser jParser = new JSONParser();
        ArrayList<Country> countries = new ArrayList<Country>();
        if(connectionDetector.isConnectingToInternet()){
            try{
                //publishProgress("Country....Reading from web");
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetCountry?minDate=0"));
                countryController.open();
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    Country country = new Country();
                    country.setCreatedDate(" ");
                    country.setSync_id(" ");
                    country.setActive(" ");
                    country.setDescription(obj.getString("NM"));
                    country.setId(obj.getString("Cid"));
                    countries.add(country);

                }
                for(int i=0;i<countries.size();i++){
                    countryController.insertCountry((countries.get(i)));
                    //publishProgress("Country Downloading...."+ count++);
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
        return mSuccess;
    }*/

    public boolean insertCountry()
    {
        count=1;
        countryController=new CountryController(context);
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

                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetCountry_TimeStampWise?minDate="+maxTimeStamp));
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
                            country.setActive(" ");
                            country.setDescription(obj.getString("NM"));
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


    public boolean insertRegion()
    {
        count=1;
        int mExit = 0;
        JSONParser jParser = new JSONParser();
        regionController=new RegionController(context);

        String maxTimeStamp = "";
        long serverMaxTimeStamp = getMaxTimestampFromServer("Region");
        ArrayList<Region> regions = new ArrayList<Region>();
        if (serverMaxTimeStamp != 0) {
            while (true) {
                userDataController.open();
                maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_REGIONMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                    maxTimeStamp = "-1";
                }
                if (connectionDetector.isConnectingToInternet()) {
                    try {
                        //publishProgress("Region....Reading From Web");
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetRegions_TimeStampWise?ConPer_Id=" + conper + "&minDate="+maxTimeStamp));

                        if(jsonarray.length()==0)
                        {
                            mSuccess = false;
                            break;
                        }
                        regionController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            Region region = new Region();
                            region.setCreatedDate(" ");
                            region.setSync_id(" ");
                            region.setActive(" ");
                            region.setCountry_id(obj.getString("Cid"));
                            region.setRegion_id(obj.getString("Rid"));
                            region.setRegion_name(obj.getString("NM"));
                            region.setCreatedDate(obj.getString("MS"));
                            regions.add(region);

                        }
                        for (int i = 0; i < regions.size(); i++) {
                            regionController.insertRegion((regions.get(i)));
                            //publishProgress("Region Downloading...."+ count++);
                        }
                        regionController.close();
                    } catch (Exception e) {
                        System.out.println(e);
                        mSuccess = true;
                    }
                }
            }
        }	else {mSuccess = true;}
        if ((Long.parseLong(maxTimeStamp) >= serverMaxTimeStamp)) {
            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
                // Exportlog(e.getMessage());
            }
            mExit++;

        }
        return mSuccess;
    }

    public boolean insertUserArea()
    {
//		String maxTimeStamp="0";
//		userDataController.open();
//		maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_USERAREAMAST));
//		userDataController.close();
//		if(maxTimeStamp.equals("0.0"))
//		{
//			maxTimeStamp="0";
//		}
//		try{
//			if(!maxTimeStamp.equals("0"))
//			{
//				userDataController.open();
//				userDataController.deleteTimeStampData(DatabaseConnection.TABLE_USERAREAMAST,maxTimeStamp);
//				userDataController.close();
//			}
//		}
//		catch(Exception e)
//		{
//			System.out.println(e);
//		}
//		String url="http://"+server+"/And_Sync.asmx/JSGetUserArea?ConPer_Id="+conper+"&minDate="+maxTimeStamp;
//		JSONParser jParser = new JSONParser();
//		ArrayList<UserArea> userareaLists = new ArrayList<UserArea>();
//		String result = jParser.getJSONArray(url);
//		if (result != null) {
//			try {
//				userAreaController=new UserAreaController(context);
//				userAreaController.open();
//				JSONArray jsonarray = new JSONArray(result);
//				for (int i = 0; i < jsonarray.length(); i++) {
//					JSONObject obj = jsonarray.getJSONObject(i);
//					UserArea userareaList = new UserArea();
//					//userareaList.setCreatedDate(obj.getString("CreatedDate"));
//					userareaList.setArea_id(obj.getString("Area_Id"));
//					userareaList.setUser_id(obj.getString("Conper_Id"));
//					userareaLists.add(userareaList);
//				}
//				for(int i=0;i<userareaLists.size();i++){
//					userAreaController.insertUserArea(userareaLists.get(i));
//				}
//				userAreaController.close();
//			}catch (Exception e)
//			{
//				System.out.println(e);
////				exceptionData.setExceptionData(e.toString(), "Exception insertFailedVisitRemark");
////				connectionDetector=new ConnectionDetector(context);
////				if(!connectionDetector.isConnectingToInternet())
////				{
////					exceptionData.setInternetExceptionData("You don't have internet connection.", "Information");
////					//exceptionData.setExceptionData(e.toString(), "ClientProtocolException InsertEnviro");
////				}
//				mSuccess=true;
//			}
//		}
        return mSuccess;
    }



    public boolean insertState() {
		/*count=1;
		stateController= new StateController(context);
		JSONParser jParser = new JSONParser();
		if(connectionDetector.isConnectingToInternet()){
			try {
				publishProgress("State....Reading From Web");
				JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetStates?ConPer_Id="+conper+"&minDate=0"));
				if(jsonarray.length() == 0)
				{
					mSuccess = false;
					return mSuccess;
				}
				stateController.open();
				for (int i = 0; i < jsonarray.length(); i++) {
					stateController.insertState(jsonarray.getJSONObject(i).getString("NM"),jsonarray.getJSONObject(i).getString("Sid"),jsonarray.getJSONObject(i).getString("Rid"));
					publishProgress("State Downloading...."+ count++);
				}
				jsonarray=null;
				stateController.close();
			}catch (Exception e)
			{
				System.out.println(e);
				mSuccess=true;
			}
		}		else {mSuccess = true;}
		return mSuccess;*/
        int mExit = 0;
        String maxTimeStamp = "0";
        stateController = new StateController(context);
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

//                    publishProgress("State....Reading From Web");
                    try {
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetStates_TimeStampWise?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper, "", "xJSGetStates", "Y");
                        if (jsonarray.length() == 0) {
                            mSuccess = false;
                            return mSuccess;
                        }
                        stateController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            stateController.insertState(jsonarray.getJSONObject(i).getString("NM"), jsonarray.getJSONObject(i).getString("Sid"), jsonarray.getJSONObject(i).getString("Rid"),jsonarray.getJSONObject(i).getString("MS"),jsonarray.getJSONObject(i).getString("active"));
//                            publishProgress("State Downloading...." + count++);
                        }
                        jsonarray = null;
                        stateController.close();
                    } catch (Exception e) {
                        stateController.close();
                        System.out.println(e);
                        mSuccess = true;
                        new DbCon(context).insertExportImportLogData(conper, "", "xJSGetDistTemplate", e.getMessage());
                    }
                } else {
                    mSuccess = true;
                }
                if ((Long.parseLong(maxTimeStamp) >= serverMaxTimeStamp)) {
                    try {
                        Thread.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                       // Exportlog(e.getMessage());
                    }
                    mExit++;
                    if (mExit > 0) break;
                }
            }
        }
        return mSuccess;


    }
   /* public boolean insertState()
    {
        count=1;
        stateController= new StateController(context);
        JSONParser jParser = new JSONParser();
        if(connectionDetector.isConnectingToInternet()){
            try {
                //publishProgress("State....Reading From Web");
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetStates?ConPer_Id="+conper+"&minDate=0"));

                stateController.open();
                for (int i = 0; i < jsonarray.length(); i++) {
                    stateController.insertState(jsonarray.getJSONObject(i).getString("NM"),jsonarray.getJSONObject(i).getString("Sid"),jsonarray.getJSONObject(i).getString("Rid"));
                    //publishProgress("State Downloading...."+ count++);
                }
                jsonarray=null;
                stateController.close();
            }catch (Exception e)
            {
                System.out.println(e);
                mSuccess=true;
            }
        }		else {mSuccess = true;}
        return mSuccess;
    }*/

/************************		Write By Sandeep Singh 11-04-2017		******************/
    /*****************		START		******************/
	/*public boolean insertProductGroup()
	{
		count=1;
		productGroupController= new ProductGroupController(context);
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
						////publishProgress("ProductGroup....Reading From Web");
						JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetProductGroup?minDate="+maxTimeStamp));
						Log.i("ProducGroup","http://"+server+"/And_Sync.asmx/xJSGetProductGroup?minDate="+maxTimeStamp+"-"+jsonarray.length());
						productGroupController.open();
						if(!jsonarray.equals("[]")){
							for (int i = 0; i < jsonarray.length(); i++) {
								productGroupController.insertProductGroup(jsonarray.getJSONObject(i).getString("Nm"),jsonarray.getJSONObject(i).getString("Id"),jsonarray.getJSONObject(i).getString("MS"));
								////publishProgress("ProductGroup Downloading...."+ count++);
							}
						}
						else{mSuccess=true;}

						jsonarray=null;
						productGroupController.close();

					}catch (Exception e)
					{
						System.out.println("ProductGrop Error"+e);
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
	}*/
    public boolean insertProductGroup()
    {
        count=1;
        productGroupController= new ProductGroupController(context);
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
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetProductGroup","Y");
                        Log.i("ProducGroup","http://"+server+"/And_Sync.asmx/xJSGetProductGroup?minDate="+maxTimeStamp+"-"+jsonarray.length());
                        productGroupController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            productGroupController.insertProductGroup(jsonarray.getJSONObject(i).getString("Nm"),jsonarray.getJSONObject(i).getString("Id"),jsonarray.getJSONObject(i).getString("MS"),jsonarray.getJSONObject(i).getString("active"));
                            //publishProgress("ProductGroup Downloading...."+ count++);
                        }
                        jsonarray=null;
                        productGroupController.close();

                    }catch (Exception e)
                    {
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetProductGroup",e.getMessage());
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
/*	public boolean insertProductGroup()
	{
		count=1;
		productGroupController= new ProductGroupController(context);
		JSONParser jParser = new JSONParser();
		if(connectionDetector.isConnectingToInternet()){
			try {
				////publishProgress("ProductGroup....Reading From Web");
				JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetProductGroup?minDate=0"));

				productGroupController.open();
				for (int i = 0; i < jsonarray.length(); i++) {
					productGroupController.insertProductGroup(
							jsonarray.getJSONObject(i).getString("Nm"),
							jsonarray.getJSONObject(i).getString("Id"));
					////publishProgress("ProductGroup Downloading...."+ count++);
				}
				jsonarray=null;
				productGroupController.close();

			}catch (Exception e)
			{
				System.out.println(e);
				mSuccess=true;
			}
		}else {mSuccess = true;}
		return mSuccess;
	}*/
    /*****************		END		******************/

/************************		Write By Sandeep Singh 11-04-2017		******************/
    /*****************		START		******************/
	public boolean insertPriceList()
	{
		count=1;
		priceListController = new PriceListController(context);
		JSONParser jParser = new JSONParser();
		int mExit=0;
		String maxTimeStamp = "0";
		long serverMaxTimeStamp=getMaxTimestampFromServer("PriceList");
		if(serverMaxTimeStamp!=0) {
			while (true) {
				userDataController.open();
				maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_PRICELIST));
				userDataController.close();
				if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
				if(connectionDetector.isConnectingToInternet()){
					try {
						////publishProgress("PriceList....Reading From Web");
						JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetPriceList?minDate="+ maxTimeStamp));
						if(jsonarray.length()==0)
                        {
                            break;
                        }
                        priceListController.open();
						for (int i = 0; i < jsonarray.length(); i++) {
                            priceListController.insertPriceList(
                                    jsonarray.getJSONObject(i).getString("MS"),
                                    jsonarray.getJSONObject(i).getString("ProdGrpId"),
                                    jsonarray.getJSONObject(i).getString("DP"),
                                    jsonarray.getJSONObject(i).getString("ProdId"),
                                    jsonarray.getJSONObject(i).getString("WefDate"),
                                    jsonarray.getJSONObject(i).getString("MRP"),
                                    Integer.parseInt( jsonarray.getJSONObject(i).getString("Id")),
                                    jsonarray.getJSONObject(i).getString("RP")

                            );
							////publishProgress("PriceList Downloading...."+ count++);
						}
						priceListController.close();

					}catch (Exception e)
					{
						System.out.println(e);
						mSuccess=true;
					}
				} {mSuccess = true;}
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
   /* public boolean insertPriceList()
    {
        count=1;
        priceListController = new PriceListController(context);
        JSONParser jParser = new JSONParser();
        ArrayList<PriceList> pricelistLists = new ArrayList<PriceList>();
        if(connectionDetector.isConnectingToInternet()){
            try {
                //publishProgress("PriceList....Reading From Web");
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetPriceList?minDate=0"));

                priceListController.open();
                for (int i = 0; i < jsonarray.length(); i++) {
                    priceListController.insertPriceList(
                            jsonarray.getJSONObject(i).getString("MS"),
                            jsonarray.getJSONObject(i).getString("ProdGrpId"),
                            jsonarray.getJSONObject(i).getString("DP"),
                            jsonarray.getJSONObject(i).getString("ProdId"),
                            jsonarray.getJSONObject(i).getString("WefDate"),
                            jsonarray.getJSONObject(i).getString("MRP"),
                            Integer.parseInt( jsonarray.getJSONObject(i).getString("Id")),
                            jsonarray.getJSONObject(i).getString("RP")

                    );
                    //publishProgress("PriceList Downloading...."+ count++);
                }
                for (int i = 0; i < pricelistLists.size(); i++) {
                    priceListController.insertPriceList(pricelistLists.get(i));
                }
                priceListController.close();

            }catch (Exception e)
            {
                System.out.println(e);
                mSuccess=true;
            }
        }	else {mSuccess = true;}
        return mSuccess;
    }*/
    /*****************		END		******************/

    public boolean insertItemTimestampWise()
    {
        count=1;
        String maxTimeStamp = "0";int mExit=0;
        itemController=new ItemController(context);
        long serverMaxTimeStamp=getMaxTimestampFromServer("Products");
        if(serverMaxTimeStamp!=0) {
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
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetProducts_TimeStampWise?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetProducts_TimeStampWise","Y");
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        itemController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            itemController.insertItem(
                                    jsonarray.getJSONObject(i).getString("SyncId"),
                                    jsonarray.getJSONObject(i).getString("Clid"),
                                    jsonarray.getJSONObject(i).getString("Uid"),
                                    jsonarray.getJSONObject(i).getString("DM"),
                                    jsonarray.getJSONObject(i).getString("DP"),
                                    jsonarray.getJSONObject(i).getString("Itid"),
                                    jsonarray.getJSONObject(i).getString("Itnm"),
                                    jsonarray.getJSONObject(i).getString("Itcd"),
                                    jsonarray.getJSONObject(i).getString("MRP"),
                                    jsonarray.getJSONObject(i).getString("Pg"),
                                    jsonarray.getJSONObject(i).getString("RP"),
                                    jsonarray.getJSONObject(i).getString("SegId"),
                                    jsonarray.getJSONObject(i).getString("StdPk"),
                                    jsonarray.getJSONObject(i).getString("Unit"),
                                    jsonarray.getJSONObject(i).getString("MS"),
                                    jsonarray.getJSONObject(i).getString("active")
                            );
                            //publishProgress("Item Downloading...." + count++);
                        }
                        jsonarray = null;
                        itemController.close();
                    } catch (Exception e) {
                        System.out.println(e);
                        mSuccess = true;
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetProducts_TimeStampWise",e.getMessage());
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

    public boolean insertIndustry()
    {
        count=1;
        JSONParser jParser = new JSONParser();
        industryController=new IndustryController(context);
        if(connectionDetector.isConnectingToInternet()){
            try {
                //publishProgress("Industry....Reading From Web");
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetIndustry?minDate=0"));

                industryController.open();
                for (int i = 0; i < jsonarray.length(); i++) {
                    industryController.insertIndustry(
                            jsonarray.getJSONObject(i).getString("Id"),
                            jsonarray.getJSONObject(i).getString("Nm"),
                            jsonarray.getJSONObject(i).getString("MS")
                    );
                    //publishProgress("Industry Downloading...."+ count++);
                }
                jsonarray=null;
                industryController.close();
            }catch (Exception e)
            {
                industryController.close();
                System.out.println(e);
                mSuccess=true;
            }
        }	else {mSuccess = true;}
        return mSuccess;
    }

    public boolean insertTransport()
    {
        count=1;
        transportController=new TransportController(context);
        JSONParser jParser=new JSONParser();

        String maxTimeStamp = "0";int mExit=0;

        long serverMaxTimeStamp=getMaxTimestampFromServer("TransportVehicle");
        if(connectionDetector.isConnectingToInternet()){

            if(serverMaxTimeStamp!=0) {
                while (true) {
                    userDataController.open();
                    maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_TRANSPORT));
                    userDataController.close();
                    if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                        maxTimeStamp = "-1";
                    }
                    try {

                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetTransportVehicle_TimeStampWise?type=Transport&minDate="+maxTimeStamp));
                        if (jsonarray.length() == 0) {
                            mSuccess = false;
                            return mSuccess;
                        }

                        transportController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            transportController.insertTransportData(
                                    jsonarray.getJSONObject(i).getString("Trpt"),
                                    jsonarray.getJSONObject(i).getString("MS")
                            );


                        }
                        jsonarray = null;
                        transportController.close();
                    } catch (Exception e) {
                        transportController.close();
                        System.out.println(e);
                        mSuccess = true;
                    }
                }
            }

        }else {mSuccess = true;}
        return mSuccess;
    }


    public boolean insertVehicle()
    {
        count=1;
        transportController=new TransportController(context);
        JSONParser jParser=new JSONParser();

        String maxTimeStamp = "0";int mExit=0;

        long serverMaxTimeStamp=getMaxTimestampFromServer("TransportVehicle");
        if(connectionDetector.isConnectingToInternet()){

            if(serverMaxTimeStamp!=0) {
                while (true) {
                    userDataController.open();
                    maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_VEHICLE));
                    userDataController.close();
                    if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                        maxTimeStamp = "-1";
                    }
                    try {

                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetTransportVehicle_TimeStampWise?type=vehicle&minDate="+maxTimeStamp));
                        if (jsonarray.length() == 0) {
                            mSuccess = false;
                            return mSuccess;
                        }

                        transportController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            transportController.insertVehicle(
                                    jsonarray.getJSONObject(i).getString("Trpt"),
                                    jsonarray.getJSONObject(i).getString("MS")
                            );


                        }
                        jsonarray = null;
                        transportController.close();
                    } catch (Exception e) {
                        transportController.close();
                        System.out.println(e);
                        mSuccess = true;
                    }
                }
            }

        }else {mSuccess = true;}
        return mSuccess;
    }

   /* public boolean insertVehicle()
    {	count=1;
        transportController=new TransportController(context);
        JSONParser jParser=new JSONParser();
        if(connectionDetector.isConnectingToInternet()){
            try {
                //publishProgress("Vehicle....Reading from web");
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetTransportVehicle?type=vehicle"));

                transportController.open();
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    transportController.insertVehicle(
                            jsonarray.getJSONObject(i).getString("Trpt")
                    );
                    //publishProgress("Vehicle Downloading...."+count++);

                }
                jsonarray=null;
                transportController.close();
            }catch (Exception e)
            {
                transportController.close();
                System.out.println(e);
                mSuccess=true;
            }

        }else {mSuccess = true;}
        return mSuccess;
    }*/


    public boolean insertVisitName()
    {
        count=1;
        VisitNameController visitController=new VisitNameController(context);
        String url="http://"+server+"/And_Sync.asmx/xJSGetVisitcode";
        JSONParser jParser=new JSONParser();
        if(connectionDetector.isConnectingToInternet()){
            try {
                //publishProgress("Visit....Reading from web");
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetVisitcode"));

                visitController.open();
                for (int i = 0; i < jsonarray.length(); i++) {
                    visitController.insertVisit(jsonarray.getJSONObject(i).getString("C"));
                    //publishProgress("Visit Downloading...."+count++);
                }
                jsonarray=null;
                visitController.close();
            }catch (Exception e)
            {
                visitController.close();
                System.out.println(e);
                mSuccess=true;
            }

        }else {mSuccess = true;}
        return mSuccess;
    }


    public boolean insertDistributorTimeStampWise()
    {
        count=1;
        distributorController=new DistributorController(context);
        String maxTimeStamp="0";
        int mExit=0;
        long serverMaxTimeStamp=getMaxTimestampFromServer("Distributors");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_DISTRIBUTERMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if (connectionDetector.isConnectingToInternet()) {
                    JSONParser jParser = new JSONParser();
                    //publishProgress("Distributor....Reading From Web");
                    try {
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetDistributors_TimeStampWise?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetDistributors_TimeStampWise","Y");
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        distributorController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            distributorController.insertDistributor(jsonarray.getJSONObject(i).getString("Pid"),jsonarray.getJSONObject(i).getString("Pnm"),jsonarray.getJSONObject(i).getString("Add1"),jsonarray.getJSONObject(i).getString("Add2"),jsonarray.getJSONObject(i).getString("Pin"),jsonarray.getJSONObject(i).getString("Aid"),jsonarray.getJSONObject(i).getString("Em"),jsonarray.getJSONObject(i).getString("Mo"),jsonarray.getJSONObject(i).getString("Indid"),jsonarray.getJSONObject(i).getString("Rmk"),jsonarray.getJSONObject(i).getString("SyncId"),jsonarray.getJSONObject(i).getString("Ctp"),jsonarray.getJSONObject(i).getString("CSTNo"),jsonarray.getJSONObject(i).getString("Vattin"),jsonarray.getJSONObject(i).getString("SrTx"),jsonarray.getJSONObject(i).getString("Panno"),jsonarray.getJSONObject(i).getString("Cid"),jsonarray.getJSONObject(i).getString("Crlmt"),jsonarray.getJSONObject(i).getString("OS"),jsonarray.getJSONObject(i).getString("Ph"),jsonarray.getJSONObject(i).getString("Opor"),jsonarray.getJSONObject(i).getString("Crd"),jsonarray.getJSONObject(i).getString("MS")								);
                            //publishProgress("Distributor Downloading...." + count++);
                        }
                        jsonarray = null;
                        distributorController.close();
                    } catch (Exception e) {
                        distributorController.close();
                        System.out.println(e);
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetDistributors_TimeStampWise",e.getMessage());
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

    public boolean insertDistrictTimestampWise()
    {
        count=1;int mExit=0;
        String maxTimeStamp = "0";
        districtController=new DistrictController(context);
        long serverMaxTimeStamp=getMaxTimestampFromServer("Districts");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_DISTRICTMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if (connectionDetector.isConnectingToInternet()) {
                    JSONParser jParser = new JSONParser();
                    //publishProgress("District....Reading From Web");
                    try {
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetDistricts_TimeStampWise?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetDistricts_TimeStampWise","Y");
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }

                        districtController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            districtController.insertDistrict(
                                    jsonarray.getJSONObject(i).getString("Did"),
                                    jsonarray.getJSONObject(i).getString("NM"),
                                    jsonarray.getJSONObject(i).getString("Sid"),
                                    jsonarray.getJSONObject(i).getString("MS")

                            );
                            //publishProgress("District Downloading...."+ count++);
                        }
                        jsonarray = null;
                        districtController.close();
                    } catch (Exception e) {
                        districtController.close();
                        System.out.println(e);
                        mSuccess = true;
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetDistricts_TimeStampWise","Y");
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



    public boolean insertCityTimestampWise()
    {
        count=1;int mExit=0;
        String maxTimeStamp = "0";
        cityController =new CityController(context);
        long serverMaxTimeStamp=getMaxTimestampFromServer("Cities");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_CITYMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if (connectionDetector.isConnectingToInternet()) {
                    JSONParser jParser = new JSONParser();
                    //publishProgress("City....Reading From Web");
                    try {
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetCities_TimeStampWise?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetCities_TimeStampWise","Y");
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
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
                            ) ;
                            //publishProgress("City Downloading...."+ count++);
                        }
                        jsonarray = null;
                        cityController.close();
                    } catch (Exception e) {
                        cityController.close();
                        System.out.println(e);
                        mSuccess = true;
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetCities_TimeStampWise",e.getMessage());
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

    public boolean insertBeatTimestampWise()
    {
        count=1;int mExit=0;
        beatController = new BeatController(context);
        String maxTimeStamp = "0";
        long serverMaxTimeStamp=getMaxTimestampFromServer("Beats");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_BEATMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if (connectionDetector.isConnectingToInternet()) {
                    JSONParser jParser = new JSONParser();
                    //publishProgress("Beat....Reading From Web");
                    try {
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetBeats_TimeStampWise?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetBeats_TimeStampWise","Y");
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        beatController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            beatController.insertBeatParameter(jsonarray.getJSONObject(i).getString("AId"),
                                    jsonarray.getJSONObject(i).getString("BID"),
                                    jsonarray.getJSONObject(i).getString("NM"),
                                    jsonarray.getJSONObject(i).getString("MS")
                            );

                            //publishProgress("Beat Downloading...."+ count++);
                        }
                        jsonarray = null;
                        beatController.close();
                    } catch (Exception e) {
                        beatController.close();
                        System.out.println(e);
                        mSuccess = true;
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetBeats_TimeStampWise",e.getMessage());

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

/************************		Write By Sandeep Singh 11-04-2017		******************/
    /*****************		START		******************/
    public boolean insertOrderTran()
    {
        count=1;
        orderController = new OrderController(context);
        String maxTimeStamp="0";
        userDataController.open();
        maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_ORDER));
        userDataController.close();
        if(maxTimeStamp.equals("0.0"))
        {
            maxTimeStamp="0";
        }
        try{
            if(!maxTimeStamp.equals("0"))
            {
                userDataController.open();
                userDataController.deleteTimeStampData(DatabaseConnection.TABLE_ORDER,maxTimeStamp);
                userDataController.close();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }


        JSONParser jParser = new JSONParser();
        int mExit=0;
        long serverMaxTimeStamp=getMaxTimestampFromServer("RetOr");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_ORDER));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if(connectionDetector.isConnectingToInternet()){
                    try {
                        //publishProgress("Order....Reading From Web");
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetRetailerOrder?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetRetailerOrder","Y");
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        orderController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            String lat = "0",lng = "0",lat_lng_timeStmp = "0";
                            if(jsonarray.getJSONObject(i).has("Latitude"))
                            {
                                lat = jsonarray.getJSONObject(i).getString("Latitude");
                            }
                            if(jsonarray.getJSONObject(i).has("Longitude"))
                            {
                                lng = jsonarray.getJSONObject(i).getString("Longitude");
                            }
                            if(jsonarray.getJSONObject(i).has("latlongdt"))
                            {
                                lat_lng_timeStmp = jsonarray.getJSONObject(i).getString("latlongdt");
                            }
                            orderController.insertOrder(									jsonarray.getJSONObject(i).getString("AreaId"),
                                    jsonarray.getJSONObject(i).getString("Millisecond"),
                                    jsonarray.getJSONObject(i).getString("Partyid"),
                                    jsonarray.getJSONObject(i).getString("Remarks"),
                                    jsonarray.getJSONObject(i).getString("SMID"),
                                    jsonarray.getJSONObject(i).getString("VisId"),
                                    jsonarray.getJSONObject(i).getString("VDate"),
                                    jsonarray.getJSONObject(i).getString("Android_Id"),
                                    jsonarray.getJSONObject(i).getString("OrdDocId"),
                                    jsonarray.getJSONObject(i).getString("OrderAmount"),
                                    jsonarray.getJSONObject(i).getString("UserId"),
                                    jsonarray.getJSONObject(i).getString("OrdId"),true,  /************************		Write By Sandeep Singh 20-04-2017		******************/
                                    /*****************		START		******************/
                                   /* jsonarray.getJSONObject(i).getString("case"),
                                    jsonarray.getJSONObject(i).getString("unit"),*/


                                    lat,lng,lat_lng_timeStmp

                                    /*****************		END		******************/
                            );

                            //publishProgress("Order Downloading...."+ count++);
                        }
                        jsonarray=null;
                        orderController.close();
                    }catch (Exception e)
                    {
                        System.out.println(e);
                        orderController.close();
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetRetailerOrder",e.getMessage());
                        mSuccess=true;
                    }
                }		else {mSuccess = true;}
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
	/*public boolean insertOrderTran()
	{
		count=1;
		orderController = new OrderController(context);
		String maxTimeStamp="0";
		userDataController.open();
		maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_ORDER));
		userDataController.close();
		if(maxTimeStamp.equals("0.0"))
		{
			maxTimeStamp="0";
		}
		try{
			if(!maxTimeStamp.equals("0"))
			{
				userDataController.open();
				userDataController.deleteTimeStampData(DatabaseConnection.TABLE_ORDER,maxTimeStamp);
				userDataController.close();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}


		JSONParser jParser = new JSONParser();


		if(connectionDetector.isConnectingToInternet()){
			try {
				////publishProgress("Order....Reading From Web");
				JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetRetailerOrder?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
				orderController.open();
				for (int i = 0; i < jsonarray.length(); i++) {
					orderController.insertOrder(
							jsonarray.getJSONObject(i).getString("AreaId"),
							jsonarray.getJSONObject(i).getString("Millisecond"),
							jsonarray.getJSONObject(i).getString("Partyid"),
							jsonarray.getJSONObject(i).getString("Remarks"),
							jsonarray.getJSONObject(i).getString("SMID"),
							jsonarray.getJSONObject(i).getString("VisId"),
							jsonarray.getJSONObject(i).getString("VDate"),
							jsonarray.getJSONObject(i).getString("Android_Id"),
							jsonarray.getJSONObject(i).getString("OrdDocId"),
							jsonarray.getJSONObject(i).getString("OrderAmount"),
							jsonarray.getJSONObject(i).getString("UserId"),
							jsonarray.getJSONObject(i).getString("OrdId"),true
					);

					////publishProgress("Order Downloading...."+ count++);
				}
				jsonarray=null;
				orderController.close();
			}catch (Exception e)
			{
				System.out.println(e);
				orderController.close();
				mSuccess=true;
			}
		}		else {mSuccess = true;}
		return mSuccess;
	}*/
    /*****************		END		******************/

/************************		Write By Sandeep Singh 11-04-2017		******************/
    /*****************		START		******************/
    public boolean insertOrder1Tran()
    {
        count=1;
        Order1Controller order1Controller=new Order1Controller(context);
        String maxTimeStamp="0";
        userDataController.open();
        maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_ORDER1));
        userDataController.close();
        if(maxTimeStamp.equals("0.0"))
        {
            maxTimeStamp="0";
        }
        try{
            if(!maxTimeStamp.equals("0"))
            {
                userDataController.open();
                userDataController.deleteTimeStampData(DatabaseConnection.TABLE_ORDER1,maxTimeStamp);
                userDataController.close();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        JSONParser jParser = new JSONParser();
        int mExit=0;
        long serverMaxTimeStamp=getMaxTimestampFromServer("RetOr1");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_ORDER1));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if(connectionDetector.isConnectingToInternet()){
                    try {
                        //publishProgress("Order1....Reading From Web");
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetRetailerOrder1?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetRetailerOrder1","Y");
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        order1Controller.open();
                        for(int i=0; i<jsonarray.length(); i++)
                        {
                            String lat = "0",lng = "0",lat_lng_timeStmp = "0";
                            if(jsonarray.getJSONObject(i).has("Latitude"))
                            {
                                lat = jsonarray.getJSONObject(i).getString("Latitude");
                            }
                            if(jsonarray.getJSONObject(i).has("Longitude"))
                            {
                                lng = jsonarray.getJSONObject(i).getString("Longitude");
                            }
                            if(jsonarray.getJSONObject(i).has("latlongdt"))
                            {
                                lat_lng_timeStmp = jsonarray.getJSONObject(i).getString("latlongdt");
                            }
                            order1Controller.insertOrder1(
                                    jsonarray.getJSONObject(i).getString("Ord1Id"),
                                    jsonarray.getJSONObject(i).getString("OrdId"),
                                    jsonarray.getJSONObject(i).getString("VisId"),
                                    jsonarray.getJSONObject(i).getString("OrdDocId"),
                                    jsonarray.getJSONObject(i).getString("Sno"),
                                    jsonarray.getJSONObject(i).getString("UserId"),
                                    jsonarray.getJSONObject(i).getString("VDate"),
                                    jsonarray.getJSONObject(i).getString("SMID"),
                                    jsonarray.getJSONObject(i).getString("Partyid"),
                                    jsonarray.getJSONObject(i).getString("AreaId"),
                                    jsonarray.getJSONObject(i).getString("Item"),
                                    jsonarray.getJSONObject(i).getString("Qty"),
                                    jsonarray.getJSONObject(i).getString("Rate"),
                                    jsonarray.getJSONObject(i).getString("Remarks"),
                                    jsonarray.getJSONObject(i).getString("amount"),
                                    jsonarray.getJSONObject(i).getString("Android_Id"),
                                    jsonarray.getJSONObject(i).getString("Android_Id1"),
                                    jsonarray.getJSONObject(i).getString("Millisecond"),
                                    jsonarray.getJSONObject(i).getString("Cases"),
                                    jsonarray.getJSONObject(i).getString("Unit"),
                                    false, /************************		Write By Sandeep Singh 20-04-2017		******************/
                                    /*****************		START		******************/
                                    lat,lng,lat_lng_timeStmp
                                    /*****************		END		******************/
                            );
                            //publishProgress("Order1 Downloading...."+ count++);
                        }
                        jsonarray=null;
                        order1Controller.close();
                    }
                    catch (Exception e)
                    {
                        order1Controller.close();
                        System.out.println(e);
                        mSuccess=true;
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetRetailerOrder1",e.getMessage());
                    }
                }	else {mSuccess = true;}
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

	/*public boolean insertOrder1Tran()
	{
		count=1;
		Order1Controller  order1Controller=new Order1Controller(context);
		String maxTimeStamp="0";
		userDataController.open();
		maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_ORDER1));
		userDataController.close();
		if(maxTimeStamp.equals("0.0"))
		{
			maxTimeStamp="0";
		}
		try{
			if(!maxTimeStamp.equals("0"))
			{
				userDataController.open();
				userDataController.deleteTimeStampData(DatabaseConnection.TABLE_ORDER1,maxTimeStamp);
				userDataController.close();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		JSONParser jParser = new JSONParser();
		if(connectionDetector.isConnectingToInternet()){
			try {
				////publishProgress("Order1....Reading From Web");
				JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetRetailerOrder1?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
				order1Controller.open();
				for(int i=0; i<jsonarray.length(); i++)
				{
					order1Controller.insertOrder1(
							jsonarray.getJSONObject(i).getString("Ord1Id"),
							jsonarray.getJSONObject(i).getString("OrdId"),
							jsonarray.getJSONObject(i).getString("VisId"),
							jsonarray.getJSONObject(i).getString("OrdDocId"),
							jsonarray.getJSONObject(i).getString("Sno"),
							jsonarray.getJSONObject(i).getString("UserId"),
							jsonarray.getJSONObject(i).getString("VDate"),
							jsonarray.getJSONObject(i).getString("SMID"),
							jsonarray.getJSONObject(i).getString("Partyid"),
							jsonarray.getJSONObject(i).getString("AreaId"),
							jsonarray.getJSONObject(i).getString("Item"),
							jsonarray.getJSONObject(i).getString("Qty"),
							jsonarray.getJSONObject(i).getString("Rate"),
							jsonarray.getJSONObject(i).getString("Remarks"),
							jsonarray.getJSONObject(i).getString("amount"),
							jsonarray.getJSONObject(i).getString("Android_Id"),
							jsonarray.getJSONObject(i).getString("Android_Id1"),
							jsonarray.getJSONObject(i).getString("Millisecond"),
							false
					);
					////publishProgress("Order1 Downloading...."+ count++);
				}
				jsonarray=null;
				order1Controller.close();
			}
			catch (Exception e)
			{
				order1Controller.close();
				System.out.println(e);
				mSuccess=true;
			}
		}	else {mSuccess = true;}
		return mSuccess;
	}*/
    /*****************		END		******************/


/************************		Write By Sandeep Singh 11-04-2017		******************/
    /*****************		START		******************/
    public boolean insertDemoTran()
    {
        count=1;
        demoTransactionController = new DemoTransactionController(context);
        String maxTimeStamp="0";
        userDataController.open();
        maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_TRANSDEMO));
        userDataController.close();
        if(maxTimeStamp.equals("0.0"))
        {
            maxTimeStamp="0";
        }
        try{
            if(!maxTimeStamp.equals("0"))
            {
                userDataController.open();
                userDataController.deleteTimeStampData(DatabaseConnection.TABLE_TRANSDEMO,maxTimeStamp);
                userDataController.close();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        JSONParser jParser = new JSONParser();
        int mExit=0;
        long serverMaxTimeStamp=getMaxTimestampFromServer("RetDemo");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_TRANSDEMO));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if(connectionDetector.isConnectingToInternet()){
                    try {
                        //publishProgress("Demo....Reading From Web");
//                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetRetailerDemo?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetRetailerDemoWithImage?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetRetailerDemoWithImage","Y");
                        if(jsonarray.length()==0)
                        {
                            mSuccess = false;
                            break;
                        }
                        demoTransactionController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            String lat = "0",lng = "0",lat_lng_timeStmp = "0";
                            if(jsonarray.getJSONObject(i).has("Latitude"))
                            {
                                lat = jsonarray.getJSONObject(i).getString("Latitude");
                            }
                            if(jsonarray.getJSONObject(i).has("Longitude"))
                            {
                                lng = jsonarray.getJSONObject(i).getString("Longitude");
                            }
                            if(jsonarray.getJSONObject(i).has("latlongdt"))
                            {
                                lat_lng_timeStmp = jsonarray.getJSONObject(i).getString("latlongdt");
                            }
                            demoTransactionController.insertdemoTransaction(
                                    jsonarray.getJSONObject(i).getString("AreaId"),
                                    jsonarray.getJSONObject(i).getString("Millisecond"),
                                    jsonarray.getJSONObject(i).getString("Partyid"),
                                    jsonarray.getJSONObject(i).getString("Remarks"),
                                    jsonarray.getJSONObject(i).getString("SMID"),
                                    jsonarray.getJSONObject(i).getString("VisId"),
                                    jsonarray.getJSONObject(i).getString("VDate"),
                                    jsonarray.getJSONObject(i).getString("Android_Id"),
                                    jsonarray.getJSONObject(i).getString("DemoDocId"),
                                    jsonarray.getJSONObject(i).getString("DemoId"),
                                    jsonarray.getJSONObject(i).getString("ProductClassId"),
                                    jsonarray.getJSONObject(i).getString("ProductMatgrp"),
                                    jsonarray.getJSONObject(i).getString("ProductSegmentId"),
                                    jsonarray.getJSONObject(i).getString("UserId"),true,
//					downloadImage(jsonarray.getJSONObject(i).getString("ImgUrl"),"A_D_")
                                    jsonarray.getJSONObject(i).getString("ImgUrl"),
                                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                                    /*****************		START		******************/
                                    lat,lng,lat_lng_timeStmp
                                    /*****************		END		******************/
                            );
                            //publishProgress("Demo Downloading...."+ count++);
                        }
                        jsonarray=null;
                        demoTransactionController.close();
                    }catch (Exception e)
                    {
                        System.out.println(e);
                        demoTransactionController.close();
                        mSuccess=true;
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetRetailerDemoWithImage",e.getMessage());
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
/*	public boolean insertDemoTran()
	{
		count=1;
		demoTransactionController = new DemoTransactionController(context);
		String maxTimeStamp="0";
		userDataController.open();
		maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_TRANSDEMO));
		userDataController.close();
		if(maxTimeStamp.equals("0.0"))
		{
			maxTimeStamp="0";
		}
		try{
			if(!maxTimeStamp.equals("0"))
			{
				userDataController.open();
				userDataController.deleteTimeStampData(DatabaseConnection.TABLE_TRANSDEMO,maxTimeStamp);
				userDataController.close();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		JSONParser jParser = new JSONParser();

		if(connectionDetector.isConnectingToInternet()){
			try {
				////publishProgress("Demo....Reading From Web");
				JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetRetailerDemo?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
				demoTransactionController.open();
				for (int i = 0; i < jsonarray.length(); i++) {
					demoTransactionController.insertdemoTransaction(
							jsonarray.getJSONObject(i).getString("AreaId"),
							jsonarray.getJSONObject(i).getString("Millisecond"),
							jsonarray.getJSONObject(i).getString("Partyid"),
							jsonarray.getJSONObject(i).getString("Remarks"),
							jsonarray.getJSONObject(i).getString("SMID"),
							jsonarray.getJSONObject(i).getString("VisId"),
							jsonarray.getJSONObject(i).getString("VDate"),
							jsonarray.getJSONObject(i).getString("Android_Id"),
							jsonarray.getJSONObject(i).getString("DemoDocId"),
							jsonarray.getJSONObject(i).getString("DemoId"),
							jsonarray.getJSONObject(i).getString("ProductClassId"),
							jsonarray.getJSONObject(i).getString("ProductMatgrp"),
							jsonarray.getJSONObject(i).getString("ProductSegmentId"),
							jsonarray.getJSONObject(i).getString("UserId"),true,
//					downloadImage(jsonarray.getJSONObject(i).getString("ImgUrl"),"A_D_")
							jsonarray.getJSONObject(i).getString("ImgUrl")
					);
					////publishProgress("Demo Downloading...."+ count++);
				}
				jsonarray=null;
				demoTransactionController.close();
			}catch (Exception e)
			{
				System.out.println(e);
				demoTransactionController.close();
				mSuccess=true;
			}
		}else {mSuccess = true;}
		return mSuccess;
	}*/
    /*****************		END		******************/

/************************		Write By Sandeep Singh 11-04-2017		******************/
    /*****************		START		******************/
    public boolean insertFailedVisit()
    {
        count=1;
        failedVisitController = new FailedVisitController(context);
        String maxTimeStamp="0";
        userDataController.open();
        maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_TRANSFAILED_VISIT));
        userDataController.close();
        if(maxTimeStamp.equals("0.0"))
        {
            maxTimeStamp="0";
        }

        try{
            if(!maxTimeStamp.equals("0"))
            {
                userDataController.open();
                userDataController.deleteTimeStampData(DatabaseConnection.TABLE_TRANSFAILED_VISIT,maxTimeStamp);
                userDataController.close();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        JSONParser jParser = new JSONParser();
        int mExit=0;
        long serverMaxTimeStamp=getMaxTimestampFromServer("FaiVisit");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_TRANSFAILED_VISIT));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if(connectionDetector.isConnectingToInternet()){
                    try {
                        //publishProgress("FailedVisit....Reading From Web");
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetFailedVisit?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetFailedVisit","Y");
                        if(jsonarray.length() == 0)
                        {
                            mSuccess = false;
                            break;
                        }
                        failedVisitController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {

                            String lat = "0",lng = "0",lat_lng_timeStmp = "0";
                            if(jsonarray.getJSONObject(i).has("Latitude"))
                            {
                                lat = jsonarray.getJSONObject(i).getString("Latitude");
                            }
                            if(jsonarray.getJSONObject(i).has("Longitude"))
                            {
                                lng = jsonarray.getJSONObject(i).getString("Longitude");
                            }
                            if(jsonarray.getJSONObject(i).has("latlongdt"))
                            {
                                lat_lng_timeStmp = jsonarray.getJSONObject(i).getString("latlongdt");
                            }
                            failedVisitController.insertFailedVisit(
                                    jsonarray.getJSONObject(i).getString("AreaId"),
                                    jsonarray.getJSONObject(i).getString("Android_Id"),
                                    jsonarray.getJSONObject(i).getString("VDate"),
                                    jsonarray.getJSONObject(i).getString("MS"),
                                    jsonarray.getJSONObject(i).getString("FVDocId"),
                                    jsonarray.getJSONObject(i).getString("FVId"),
                                    (jsonarray.getJSONObject(i).getString("PartyID").equals("")?null:jsonarray.getJSONObject(i).getString("PartyID")),
                                    jsonarray.getJSONObject(i).getString("NextVisitDate"),
                                    jsonarray.getJSONObject(i).getString("reasonId"),
                                    jsonarray.getJSONObject(i).getString("Remark"),
                                    jsonarray.getJSONObject(i).getString("SMID"),
                                    jsonarray.getJSONObject(i).getString("UserId"),
                                    jsonarray.getJSONObject(i).getString("VisId"),
                                    jsonarray.getJSONObject(i).getString("VisitTime"),
                                    (jsonarray.getJSONObject(i).getString("DistId").equals("")?null:jsonarray.getJSONObject(i).getString("DistId")),
                                    true,/************************		Write By Sandeep Singh 20-04-2017		******************/
                                    /*****************		START		******************/
                                    lat,lng,lat_lng_timeStmp
                                    /*****************		END		******************/ );
                            //publishProgress("FailedVisit Downloading...."+ count++);
                        }
                        jsonarray=null;
                        failedVisitController.close();
                    }catch (Exception e)
                    {
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetFailedVisit",e.getMessage());
                        failedVisitController.close();
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
	/*public boolean insertFailedVisit()
	{
		count=1;
		failedVisitController = new FailedVisitController(context);
		String maxTimeStamp="0";
		userDataController.open();
		maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_TRANSFAILED_VISIT));
		userDataController.close();
		if(maxTimeStamp.equals("0.0"))
		{
			maxTimeStamp="0";
		}

		try{
			if(!maxTimeStamp.equals("0"))
			{
				userDataController.open();
				userDataController.deleteTimeStampData(DatabaseConnection.TABLE_TRANSFAILED_VISIT,maxTimeStamp);
				userDataController.close();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		JSONParser jParser = new JSONParser();
		if(connectionDetector.isConnectingToInternet()){
			try {
				////publishProgress("FailedVisit....Reading From Web");
				JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetFailedVisit?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
				failedVisitController.open();
				for (int i = 0; i < jsonarray.length(); i++) {
					failedVisitController.insertFailedVisit(
							jsonarray.getJSONObject(i).getString("AreaId"),
							jsonarray.getJSONObject(i).getString("Android_Id"),
							jsonarray.getJSONObject(i).getString("VDate"),
							jsonarray.getJSONObject(i).getString("FVDocId"),
							jsonarray.getJSONObject(i).getString("FVId"),
							(jsonarray.getJSONObject(i).getString("PartyID").equals("")?null:jsonarray.getJSONObject(i).getString("PartyID")),
							jsonarray.getJSONObject(i).getString("NextVisitDate"),
							jsonarray.getJSONObject(i).getString("reasonId"),
							jsonarray.getJSONObject(i).getString("Remark"),
							jsonarray.getJSONObject(i).getString("SMID"),
							jsonarray.getJSONObject(i).getString("UserId"),
							jsonarray.getJSONObject(i).getString("VisId"),
							jsonarray.getJSONObject(i).getString("VisitTime"),
							(jsonarray.getJSONObject(i).getString("DistId").equals("")?null:jsonarray.getJSONObject(i).getString("DistId")),
							true,
							jsonarray.getJSONObject(i).getString("MS")


					);
					////publishProgress("FailedVisit Downloading...."+ count++);
				}
				jsonarray=null;
				failedVisitController.close();
			}catch (Exception e)
			{

				failedVisitController.close();
				System.out.println(e);
				mSuccess=true;
			}
		}else {mSuccess = true;}
		return mSuccess;
	}*/
    /*****************		END		******************/


//    public boolean insertDistributerFailedVisit()
//    {
//        count=1;
//        failedVisitController = new FailedVisitController(context);
//        String maxTimeStamp="0";
//        userDataController.open();
//        maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_TRANSFAILED_VISIT));
//        userDataController.close();
//        if(maxTimeStamp.equals("0.0"))
//        {
//            maxTimeStamp="0";
//        }
//        try{
//            if(!maxTimeStamp.equals("0"))
//            {
//                userDataController.open();
//                userDataController.deleteTimeStampData(DatabaseConnection.TABLE_TRANSFAILED_VISIT,maxTimeStamp);
//                userDataController.close();
//            }
//        }
//        catch(Exception e)
//        {
//            System.out.println(e);
//        }
//        JSONParser jParser = new JSONParser();
//        if(connectionDetector.isConnectingToInternet()){
//            try {
//                ////publishProgress("Distributor Failed Visit....Reading From Web");
//                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetDistributorFailedVisit?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
//                failedVisitController.open();
//                for (int i = 0; i < jsonarray.length(); i++) {
//                    failedVisitController.insertFailedVisit(
//                            jsonarray.getJSONObject(i).getString("AreaId"),
//                            jsonarray.getJSONObject(i).getString("Android_Id"),
//                            jsonarray.getJSONObject(i).getString("VDate"),
//                            jsonarray.getJSONObject(i).getString("FVDocId"),
//                            jsonarray.getJSONObject(i).getString("FVId"),
//                            null,
//                            jsonarray.getJSONObject(i).getString("NextVisitDate"),
//                            jsonarray.getJSONObject(i).getString("reasonId"),
//                            jsonarray.getJSONObject(i).getString("Remark"),
//                            jsonarray.getJSONObject(i).getString("SMID"),
//                            jsonarray.getJSONObject(i).getString("UserId"),
//                            jsonarray.getJSONObject(i).getString("VisId"),
//                            jsonarray.getJSONObject(i).getString("VisitTime"),
//                            jsonarray.getJSONObject(i).getString("DistId"),
//                            true,
//                            jsonarray.getJSONObject(i).getString("MS")
//                    );
//                    ////publishProgress("Distributor Failed Visit Downloading...."+count++);
//                }
//                jsonarray=null;
//                failedVisitController.close();
//            }catch (Exception e)
//            {
//                failedVisitController.close();
//                System.out.println(e);
//                mSuccess=true;
//            }
//        }else {mSuccess = true;}
//        return mSuccess;
//    }
/************************		Write By Sandeep Singh 11-04-2017		******************/
    /*****************		START		******************/
    public boolean insertCompetitor()
    {
        count=1;
        CompetitorController competitorController = new CompetitorController(context);
        String maxTimeStamp="0";
        userDataController.open();
        maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_COMPETITOR));
        userDataController.close();
        if(maxTimeStamp.equals("0.0"))
        {
            maxTimeStamp="0";
        }
        try{
            if(!maxTimeStamp.equals("0"))
            {
                userDataController.open();
                userDataController.deleteTimeStampData(DatabaseConnection.TABLE_COMPETITOR,maxTimeStamp);
                userDataController.close();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        JSONParser jParser = new JSONParser();
        int mExit=0;
        long serverMaxTimeStamp=getMaxTimestampFromServer("ReComp");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_COMPETITOR));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if(connectionDetector.isConnectingToInternet()){
                    try {
                        //publishProgress("Competitor....Reading From Web");
//                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetRetailerCompetitor?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetRetailerCompetitorWithImage?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetRetailerCompetitorWithImage","Y");
                        if(jsonarray.length() == 0)
                        {
                            mSuccess = false;
                            break;
                        }
                        Log.i("Competitor","http://" + server + "/And_Sync.asmx/JSGetRetailerCompetitor?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp);
                        competitorController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            String lat = "0",lng = "0",lat_lng_timeStmp = "0";
                            if(jsonarray.getJSONObject(i).has("Latitude"))
                            {
                                lat = jsonarray.getJSONObject(i).getString("Latitude");
                            }
                            if(jsonarray.getJSONObject(i).has("Longitude"))
                            {
                                lng = jsonarray.getJSONObject(i).getString("Longitude");
                            }
                            if(jsonarray.getJSONObject(i).has("latlongdt"))
                            {
                                lat_lng_timeStmp = jsonarray.getJSONObject(i).getString("latlongdt");
                            }

                            competitorController.insertCompetitor(
                                    jsonarray.getJSONObject(i).getString("VDate"),
                                    jsonarray.getJSONObject(i).getString("MS"),
                                    jsonarray.getJSONObject(i).getString("VisId"),
                                    jsonarray.getJSONObject(i).getString("UserId"),
                                    jsonarray.getJSONObject(i).getString("Android_Id"),
                                    jsonarray.getJSONObject(i).getString("ComptId"),
                                    jsonarray.getJSONObject(i).getString("DocId"),
                                    jsonarray.getJSONObject(i).getString("Item"),
                                    jsonarray.getJSONObject(i).getString("Partyid"),
                                    jsonarray.getJSONObject(i).getString("Qty"),
                                    jsonarray.getJSONObject(i).getString("Rate"),
                                    jsonarray.getJSONObject(i).getString("SMID"),
                                    jsonarray.getJSONObject(i).getString("CompName"),
                                    jsonarray.getJSONObject(i).getString("Discount"),
                                    jsonarray.getJSONObject(i).getString("BrandActivity"),
                                    jsonarray.getJSONObject(i).getString("MeetCtivity"),
                                    jsonarray.getJSONObject(i).getString("RoadShow"),
                                    jsonarray.getJSONObject(i).getString("Scheme"),
                                    jsonarray.getJSONObject(i).getString("OthergernralInfo"),
                                    jsonarray.getJSONObject(i).getString("OtherActivity"),
                                    jsonarray.getJSONObject(i).getString("Remarks"),true,
//					downloadImage(jsonarray.getJSONObject(i).getString("ImgUrl"),"A_C_")
                                    jsonarray.getJSONObject(i).getString("ImgUrl"),
                                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                                    /*****************		START		******************/
                                    lat,lng,lat_lng_timeStmp
                                    /*****************		END		******************/

                            );
                            Log.i("Competitor=",jsonarray.getJSONObject(i).getString("VDate")+"-"+jsonarray.getJSONObject(i).getString("VisId")+"-"+jsonarray.getJSONObject(i).getString("UserId")+"-"+jsonarray.getJSONObject(i).getString("Android_Id")+"-"+jsonarray.getJSONObject(i).getString("ComptId")+"-"+jsonarray.getJSONObject(i).getString("DocId")+"-"+jsonarray.getJSONObject(i).getString("Item")+"-"+jsonarray.getJSONObject(i).getString("Partyid")+"-"+jsonarray.getJSONObject(i).getString("Qty")+"-"+jsonarray.getJSONObject(i).getString("Rate")+"-"+jsonarray.getJSONObject(i).getString("SMID")+"-"+jsonarray.getJSONObject(i).getString("CompName")+"-"+jsonarray.getJSONObject(i).getString("Discount")+"-"+jsonarray.getJSONObject(i).getString("BrandActivity")+"-"+jsonarray.getJSONObject(i).getString("MeetCtivity")+"-"+jsonarray.getJSONObject(i).getString("RoadShow")+"-"+jsonarray.getJSONObject(i).getString("Scheme")+"-"+jsonarray.getJSONObject(i).getString("OthergernralInfo")+"-"+jsonarray.getJSONObject(i).getString("OtherActivity")+"-"+jsonarray.getJSONObject(i).getString("Remarks")
                                    +"-"+jsonarray.getJSONObject(i).getString("MS"));
                            //publishProgress("Competitor Downloading...."+ count++);
                        }
                        jsonarray=null;
                        competitorController.close();
                    }catch (Exception e)
                    {
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetRetailerCompetitorWithImage",e.getMessage());
                        System.out.println(e);
                        competitorController.close();
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
	/*public boolean insertCompetitor()
	{
		count=1;
		CompetitorController competitorController = new CompetitorController(context);
		String maxTimeStamp="0";
		userDataController.open();
		maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_COMPETITOR));
		userDataController.close();
		if(maxTimeStamp.equals("0.0"))
		{
			maxTimeStamp="0";
		}
		try{
			if(!maxTimeStamp.equals("0"))
			{
				userDataController.open();
				userDataController.deleteTimeStampData(DatabaseConnection.TABLE_COMPETITOR,maxTimeStamp);
				userDataController.close();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		JSONParser jParser = new JSONParser();
		if(connectionDetector.isConnectingToInternet()){
			try {
				////publishProgress("Competitor....Reading From Web");
				JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetRetailerCompetitor?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
				Log.i("Competitor","http://" + server + "/And_Sync.asmx/JSGetRetailerCompetitor?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp);
				competitorController.open();
				for (int i = 0; i < jsonarray.length(); i++) {
					competitorController.insertCompetitor(
							jsonarray.getJSONObject(i).getString("VDate"),
							jsonarray.getJSONObject(i).getString("VisId"),
							jsonarray.getJSONObject(i).getString("UserId"),
							jsonarray.getJSONObject(i).getString("Android_Id"),
							jsonarray.getJSONObject(i).getString("ComptId"),
							jsonarray.getJSONObject(i).getString("DocId"),
							jsonarray.getJSONObject(i).getString("Item"),
							jsonarray.getJSONObject(i).getString("Partyid"),
							jsonarray.getJSONObject(i).getString("Qty"),
							jsonarray.getJSONObject(i).getString("Rate"),
							jsonarray.getJSONObject(i).getString("SMID"),
							jsonarray.getJSONObject(i).getString("CompName"),
							jsonarray.getJSONObject(i).getString("Discount"),
							jsonarray.getJSONObject(i).getString("BrandActivity"),
							jsonarray.getJSONObject(i).getString("MeetCtivity"),
							jsonarray.getJSONObject(i).getString("RoadShow"),
							jsonarray.getJSONObject(i).getString("Scheme"),
							jsonarray.getJSONObject(i).getString("OthergernralInfo"),
							jsonarray.getJSONObject(i).getString("OtherActivity"),
							jsonarray.getJSONObject(i).getString("Remarks"),true,
//					downloadImage(jsonarray.getJSONObject(i).getString("ImgUrl"),"A_C_")
							jsonarray.getJSONObject(i).getString("ImgUrl"),
							jsonarray.getJSONObject(i).getString("MS")
					);
					Log.i("Competitor=",jsonarray.getJSONObject(i).getString("VDate")+"-"+jsonarray.getJSONObject(i).getString("VisId")+"-"+jsonarray.getJSONObject(i).getString("UserId")+"-"+jsonarray.getJSONObject(i).getString("Android_Id")+"-"+jsonarray.getJSONObject(i).getString("ComptId")+"-"+jsonarray.getJSONObject(i).getString("DocId")+"-"+jsonarray.getJSONObject(i).getString("Item")+"-"+jsonarray.getJSONObject(i).getString("Partyid")+"-"+jsonarray.getJSONObject(i).getString("Qty")+"-"+jsonarray.getJSONObject(i).getString("Rate")+"-"+jsonarray.getJSONObject(i).getString("SMID")+"-"+jsonarray.getJSONObject(i).getString("CompName")+"-"+jsonarray.getJSONObject(i).getString("Discount")+"-"+jsonarray.getJSONObject(i).getString("BrandActivity")+"-"+jsonarray.getJSONObject(i).getString("MeetCtivity")+"-"+jsonarray.getJSONObject(i).getString("RoadShow")+"-"+jsonarray.getJSONObject(i).getString("Scheme")+"-"+jsonarray.getJSONObject(i).getString("OthergernralInfo")+"-"+jsonarray.getJSONObject(i).getString("OtherActivity")+"-"+jsonarray.getJSONObject(i).getString("Remarks")
							+"-"+jsonarray.getJSONObject(i).getString("MS"));
					////publishProgress("Competitor Downloading...."+ count++);
				}
				jsonarray=null;
				competitorController.close();
			}catch (Exception e)
			{
				System.out.println(e);
				competitorController.close();
				mSuccess=true;
			}
		}else {mSuccess = true;}
		return mSuccess;
	}*/
    /*****************		END		******************/

/************************		Write By Sandeep Singh 11-04-2017		******************/
    /*****************		START		******************/
    public boolean insertPartyCollection()
    {
        count=1;
        TransCollectionController transCollectionController = new TransCollectionController(context);
        String maxTimeStamp="0";
        userDataController.open();
        maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_TRANSCOLLECTION));
        userDataController.close();
        if(maxTimeStamp.equals("0.0"))
        {
            maxTimeStamp="0";
        }
        try{
            if(!maxTimeStamp.equals("0"))
            {
                userDataController.open();
                userDataController.deleteTimeStampData(DatabaseConnection.TABLE_TRANSCOLLECTION,maxTimeStamp);
                userDataController.close();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        JSONParser jParser = new JSONParser();
        int mExit=0;
        long serverMaxTimeStamp=getMaxTimestampFromServer("PaColl");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_TRANSCOLLECTION));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if(connectionDetector.isConnectingToInternet()){
                    try {
                        //publishProgress("Party Collection....Reading From Web");
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetPartyCollection?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetPartyCollection","Y");
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        transCollectionController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {

                            String lat = "0",lng = "0",lat_lng_timeStmp = "0";
                            if(jsonarray.getJSONObject(i).has("Latitude"))
                            {
                                lat = jsonarray.getJSONObject(i).getString("Latitude");
                            }
                            if(jsonarray.getJSONObject(i).has("Longitude"))
                            {
                                lng = jsonarray.getJSONObject(i).getString("Longitude");
                            }
                            if(jsonarray.getJSONObject(i).has("latlongdt"))
                            {
                                lat_lng_timeStmp = jsonarray.getJSONObject(i).getString("latlongdt");
                            }

                            transCollectionController.insertTransCollection(
                                    jsonarray.getJSONObject(i).getString("CollId"),
                                    jsonarray.getJSONObject(i).getString("AreaId"),
                                    jsonarray.getJSONObject(i).getString("CreatedDate"),
                                    jsonarray.getJSONObject(i).getString("VisId"),
                                    jsonarray.getJSONObject(i).getString("UserId"),
                                    jsonarray.getJSONObject(i).getString("Amount"),
                                    //transCollectionList.setAndPartyId(obj.getString("Beat_Id"));
                                    jsonarray.getJSONObject(i).getString("Android_Id"),
                                    jsonarray.getJSONObject(i).getString("Bank"),
                                    jsonarray.getJSONObject(i).getString("Branch"),
                                    jsonarray.getJSONObject(i).getString("Cheque_DD_Date"),
                                    jsonarray.getJSONObject(i).getString("Cheque_DD_No"),
                                    jsonarray.getJSONObject(i).getString("CollDocId"),
                                    //transCollectionList.setItemId(obj.getString("CST_No"));
                                    jsonarray.getJSONObject(i).getString("Mode"),
                                    jsonarray.getJSONObject(i).getString("Partyid"),
                                    //transCollectionList.setPartyName(obj.getString("DOB"));
                                    jsonarray.getJSONObject(i).getString("PaymentDate"),
                                    jsonarray.getJSONObject(i).getString("Remarks"),
                                    jsonarray.getJSONObject(i).getString("SMID"),
                                    jsonarray.getJSONObject(i).getString("VDate"),
                                    true,
                                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                                    /*****************		START		******************/
                                    lat,lng,lat_lng_timeStmp
                                    /*****************		END		******************/
                            );
                            //publishProgress("Party Collection Downloading...."+ count++);
                        }
                        jsonarray=null;
                        transCollectionController.close();
                    }catch (Exception e)
                    {
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetPartyCollection",e.getMessage());
                        System.out.println(e);
                        transCollectionController.close();
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
	/*public boolean insertPartyCollection()
	{
		count=1;
		TransCollectionController transCollectionController = new TransCollectionController(context);
		String maxTimeStamp="0";
		userDataController.open();
		maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_TRANSCOLLECTION));
		userDataController.close();
		if(maxTimeStamp.equals("0.0"))
		{
			maxTimeStamp="0";
		}
		try{
			if(!maxTimeStamp.equals("0"))
			{
				userDataController.open();
				userDataController.deleteTimeStampData(DatabaseConnection.TABLE_TRANSCOLLECTION,maxTimeStamp);
				userDataController.close();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		JSONParser jParser = new JSONParser();
		if(connectionDetector.isConnectingToInternet()){
			try {
				////publishProgress("Party Collection....Reading From Web");
				JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetPartyCollection?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
				transCollectionController.open();
				for (int i = 0; i < jsonarray.length(); i++) {
					transCollectionController.insertTransCollection(
							jsonarray.getJSONObject(i).getString("CollId"),
							jsonarray.getJSONObject(i).getString("AreaId"),
							jsonarray.getJSONObject(i).getString("CreatedDate"),
							jsonarray.getJSONObject(i).getString("VisId"),
							jsonarray.getJSONObject(i).getString("UserId"),
							jsonarray.getJSONObject(i).getString("Amount"),
							//transCollectionList.setAndPartyId(obj.getString("Beat_Id"));
							jsonarray.getJSONObject(i).getString("Android_Id"),
							jsonarray.getJSONObject(i).getString("Bank"),
							jsonarray.getJSONObject(i).getString("Branch"),
							jsonarray.getJSONObject(i).getString("Cheque_DD_Date"),
							jsonarray.getJSONObject(i).getString("Cheque_DD_No"),
							jsonarray.getJSONObject(i).getString("CollDocId"),
							//transCollectionList.setItemId(obj.getString("CST_No"));
							jsonarray.getJSONObject(i).getString("Mode"),
							jsonarray.getJSONObject(i).getString("Partyid"),
							//transCollectionList.setPartyName(obj.getString("DOB"));
							jsonarray.getJSONObject(i).getString("PaymentDate"),
							jsonarray.getJSONObject(i).getString("Remarks"),
							jsonarray.getJSONObject(i).getString("SMID"),
							jsonarray.getJSONObject(i).getString("VDate"),
							true


					);
					////publishProgress("Party Collection Downloading...."+ count++);
				}
				jsonarray=null;
				transCollectionController.close();
			}catch (Exception e)
			{
				System.out.println(e);
				transCollectionController.close();
				mSuccess=true;
			}
		}else {mSuccess = true;}
		return mSuccess;
	}
*/
    /*****************		END		******************/


    /************************		Write By Sandeep Singh 11-04-2017		******************/
    /*****************		START		******************/
    public boolean insertDistributerDiscussion()
    {
        count=1;
        VisitController visitController = new VisitController(context);
        String maxTimeStamp="0";
        userDataController.open();
        maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_VISITDIST));
        userDataController.close();
        if(maxTimeStamp.equals("0.0"))
        {
            maxTimeStamp="0";
        }
        try{
            if(!maxTimeStamp.equals("0"))
            {
                userDataController.open();
                userDataController.deleteTimeStampData(DatabaseConnection.TABLE_VISITDIST,maxTimeStamp);
                userDataController.close();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        JSONParser jParser = new JSONParser();
        int mExit=0;
        long serverMaxTimeStamp=getMaxTimestampFromServer("DistDisc");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_VISITDIST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if(connectionDetector.isConnectingToInternet()){
                    try {
                        //publishProgress("Distributor Discussion....Reading From Web");
//                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetDistributorDiscussion?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetDistributorDiscussionWithImage?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetDistributorDiscussionWithImage","Y");
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        visitController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            String lat = "0",lng = "0",lat_lng_timeStmp = "0";
                            if(jsonarray.getJSONObject(i).has("Latitude"))
                            {
                                lat = jsonarray.getJSONObject(i).getString("Latitude");
                            }
                            if(jsonarray.getJSONObject(i).has("Longitude"))
                            {
                                lng = jsonarray.getJSONObject(i).getString("Longitude");
                            }
                            if(jsonarray.getJSONObject(i).has("latlongdt"))
                            {
                                lat_lng_timeStmp = jsonarray.getJSONObject(i).getString("latlongdt");
                            }
                            visitController.insertVisit(
                                    jsonarray.getJSONObject(i).getString("UserId"),
                                    jsonarray.getJSONObject(i).getString("CreatedDate"),
                                    jsonarray.getJSONObject(i).getString("VDate"),
                                    jsonarray.getJSONObject(i).getString("Android_Id"),
                                    jsonarray.getJSONObject(i).getString("Cityid"),
                                    jsonarray.getJSONObject(i).getString("DistId"),
                                    jsonarray.getJSONObject(i).getString("SpendFromTime"),
                                    jsonarray.getJSONObject(i).getString("NextVisitTime"),
                                    jsonarray.getJSONObject(i).getString("NextVisitDate"),
                                    jsonarray.getJSONObject(i).getString("Remark"),
                                    jsonarray.getJSONObject(i).getString("SMID"),
                                    jsonarray.getJSONObject(i).getString("VisDistId"),
                                    jsonarray.getJSONObject(i).getString("Stock"),
                                    jsonarray.getJSONObject(i).getString("SpendToDate")	,true,
//					downloadImage(jsonarray.getJSONObject(i).getString("ImgUrl"),"A_DistDisc_")
                                    jsonarray.getJSONObject(i).getString("ImgUrl"),
                                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                                    /*****************		START		******************/
                                    lat,lng,lat_lng_timeStmp,jsonarray.getJSONObject(i).getString("Docid")
                                    /*****************		END		******************/
                            );
                            //publishProgress("Distributor Discussion Downloading...."+count++);
                        }
                        jsonarray=null;
                        visitController.close();
                    }catch (Exception e)
                    {
                        visitController.close();
                        System.out.println(e);
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetDistributorDiscussionWithImage",e.getMessage());
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
	/*public boolean insertDistributerDiscussion()
	{
		count=1;
		VisitController visitController = new VisitController(context);
		String maxTimeStamp="0";
		userDataController.open();
		maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_VISITDIST));
		userDataController.close();
		if(maxTimeStamp.equals("0.0"))
		{
			maxTimeStamp="0";
		}
		try{
			if(!maxTimeStamp.equals("0"))
			{
				userDataController.open();
				userDataController.deleteTimeStampData(DatabaseConnection.TABLE_VISITDIST,maxTimeStamp);
				userDataController.close();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		JSONParser jParser = new JSONParser();
		if(connectionDetector.isConnectingToInternet()){
			try {
				////publishProgress("Distributor Discussion....Reading From Web");
				JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetDistributorDiscussion?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
				visitController.open();
				for (int i = 0; i < jsonarray.length(); i++) {
					visitController.insertVisit(
							jsonarray.getJSONObject(i).getString("UserId"),
							jsonarray.getJSONObject(i).getString("CreatedDate"),
							jsonarray.getJSONObject(i).getString("VDate"),
							jsonarray.getJSONObject(i).getString("Android_Id"),
							jsonarray.getJSONObject(i).getString("Cityid"),
							jsonarray.getJSONObject(i).getString("DistId"),
							jsonarray.getJSONObject(i).getString("SpendFromTime"),
							jsonarray.getJSONObject(i).getString("NextVisitTime"),
							jsonarray.getJSONObject(i).getString("NextVisitDate"),
							jsonarray.getJSONObject(i).getString("Remark"),
							jsonarray.getJSONObject(i).getString("SMID"),
							jsonarray.getJSONObject(i).getString("VisDistId"),
							jsonarray.getJSONObject(i).getString("Stock"),
							jsonarray.getJSONObject(i).getString("SpendToDate")	,true,
//					downloadImage(jsonarray.getJSONObject(i).getString("ImgUrl"),"A_DistDisc_")
							jsonarray.getJSONObject(i).getString("ImgUrl")
					);
					////publishProgress("Distributor Discussion Downloading...."+count++);
				}
				jsonarray=null;
				visitController.close();
			}catch (Exception e)
			{
				visitController.close();
				System.out.println(e);

				mSuccess=true;
			}
		}else {mSuccess = true;}
		return mSuccess;
	}*/
    /*****************		END		******************/

/************************		Write By Sandeep Singh 11-04-2017		******************/
    /*****************		START		******************/
    public boolean insertDsr()
    {
        count=1;
        DsrController dsrController = new DsrController(context);
        String maxTimeStamp="0";
        userDataController.open();
        maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_VISITL1));
        userDataController.close();
        if(maxTimeStamp.equals("0.0"))
        {
            maxTimeStamp="0";
        }
        try{
            if(!maxTimeStamp.equals("0"))
            {
                userDataController.open();
                userDataController.deleteTimeStampData(DatabaseConnection.TABLE_VISITL1,maxTimeStamp);
                userDataController.close();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        JSONParser jParser = new JSONParser();
        int mExit=0;
        long serverMaxTimeStamp=getMaxTimestampFromServer("Dsr");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_VISITL1));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if(connectionDetector.isConnectingToInternet()){
                    try {
                        //publishProgress("Dsr....Reading From Web");
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetDSR?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetDSR","Y");
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        Log.i("InserDsr","http://" + server + "/And_Sync.asmx/JSGetDSR?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp);
                        dsrController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {

                            String lat = "0",lng = "0",lat_lng_timeStmp = "0";
                            if(jsonarray.getJSONObject(i).has("Latitude"))
                            {
                                lat = jsonarray.getJSONObject(i).getString("Latitude");
                            }
                            if(jsonarray.getJSONObject(i).has("Longitude"))
                            {
                                lng = jsonarray.getJSONObject(i).getString("Longitude");
                            }
                            if(jsonarray.getJSONObject(i).has("latlongdt"))
                            {
                                lat_lng_timeStmp = jsonarray.getJSONObject(i).getString("latlongdt");
                            }

                            dsrController.insertDsr(
                                    jsonarray.getJSONObject(i).getString("UserId"),
                                    jsonarray.getJSONObject(i).getString("MS"),
                                    jsonarray.getJSONObject(i).getString("Vdate"),
                                    jsonarray.getJSONObject(i).getString("Android_Id"),
                                    jsonarray.getJSONObject(i).getString("CityId"),
                                    jsonarray.getJSONObject(i).getString("cityIds"),
                                    jsonarray.getJSONObject(i).getString("cityName"),
                                    jsonarray.getJSONObject(i).getString("DistId"),
                                    jsonarray.getJSONObject(i).getString("Lock"),
                                    jsonarray.getJSONObject(i).getString("FROMTime"),
                                    jsonarray.getJSONObject(i).getString("ToTime"),
                                    jsonarray.getJSONObject(i).getString("Industry"),
                                    jsonarray.getJSONObject(i).getString("ModeOfTransport"),
                                    jsonarray.getJSONObject(i).getString("nCityId"),
                                    jsonarray.getJSONObject(i).getString("NextVisitDate"),
                                    jsonarray.getJSONObject(i).getString("WithUserId"),
                                    jsonarray.getJSONObject(i).getString("Remark"),
                                    jsonarray.getJSONObject(i).getString("SMID"),
                                    jsonarray.getJSONObject(i).getString("VehicleUsed"),
                                    jsonarray.getJSONObject(i).getString("VisId"),
                                    jsonarray.getJSONObject(i).getString("visitDocId"),
                                    jsonarray.getJSONObject(i).getString("nWithUserId"),
                                    jsonarray.getJSONObject(i).getString("OrderAmountMail"),
                                    jsonarray.getJSONObject(i).getString("OrderAmountPhone"),
                                    (jsonarray.getJSONObject(i).getString("visitcode").equals("")?"NA":jsonarray.getJSONObject(i).getString("visitcode")),
                                    (jsonarray.getJSONObject(i).getString("Attendance").equals("")?"0":jsonarray.getJSONObject(i).getString("Attendance")),
                                    (jsonarray.getJSONObject(i).getString("Fromareacode").equals("")?"0":jsonarray.getJSONObject(i).getString("Fromareacode")),
                                    (jsonarray.getJSONObject(i).getString("OtherExpense").equals("")?"0":jsonarray.getJSONObject(i).getString("OtherExpense")),
                                    (jsonarray.getJSONObject(i).getString("AndroidAppRemark1").equals("")?"NA":jsonarray.getJSONObject(i).getString("AndroidAppRemark1")),
                                    (jsonarray.getJSONObject(i).getString("ToAreacode").equals("")?"0":jsonarray.getJSONObject(i).getString("ToAreacode"))
                                    ,true,
                                    /************************		Write By Sandeep Singh 20-04-2017		******************/
                                    /*****************		START		******************/
                                    lat,lng,lat_lng_timeStmp
                                    /*****************		END		******************/
                            );

                            //publishProgress("Dsr Downloading...."+count++);
                        }
                        jsonarray=null;
                        dsrController.close();
                    }catch (Exception e)
                    {
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetDSR",e.getMessage());
                        System.out.println(e);
                        dsrController.close();
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
	/*public boolean insertDsr()
	{
		count=1;
		DsrController dsrController = new DsrController(context);
		String maxTimeStamp="0";
		userDataController.open();
		maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_VISITL1));
		userDataController.close();
		if(maxTimeStamp.equals("0.0"))
		{
			maxTimeStamp="0";
		}
		try{
			if(!maxTimeStamp.equals("0"))
			{
				userDataController.open();
				userDataController.deleteTimeStampData(DatabaseConnection.TABLE_VISITL1,maxTimeStamp);
				userDataController.close();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		JSONParser jParser = new JSONParser();
		if(connectionDetector.isConnectingToInternet()){
			try {
				////publishProgress("Dsr....Reading From Web");
				JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetDSR?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
				Log.i("InserDsr","http://" + server + "/And_Sync.asmx/JSGetDSR?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp);
				dsrController.open();
				for (int i = 0; i < jsonarray.length(); i++) {
					dsrController.insertDsr(
							jsonarray.getJSONObject(i).getString("UserId"),
							jsonarray.getJSONObject(i).getString("Vdate"),
							jsonarray.getJSONObject(i).getString("Android_Id"),
							jsonarray.getJSONObject(i).getString("CityId"),
							jsonarray.getJSONObject(i).getString("cityIds"),
							jsonarray.getJSONObject(i).getString("cityName"),
							jsonarray.getJSONObject(i).getString("DistId"),
							jsonarray.getJSONObject(i).getString("Lock"),
							jsonarray.getJSONObject(i).getString("FROMTime"),
							jsonarray.getJSONObject(i).getString("ToTime"),
							jsonarray.getJSONObject(i).getString("Industry"),
							jsonarray.getJSONObject(i).getString("ModeOfTransport"),
							jsonarray.getJSONObject(i).getString("nCityId"),
							jsonarray.getJSONObject(i).getString("NextVisitDate"),
							jsonarray.getJSONObject(i).getString("WithUserId"),
							jsonarray.getJSONObject(i).getString("Remark"),
							jsonarray.getJSONObject(i).getString("SMID"),
							jsonarray.getJSONObject(i).getString("VehicleUsed"),
							jsonarray.getJSONObject(i).getString("VisId"),
							jsonarray.getJSONObject(i).getString("visitDocId"),
							jsonarray.getJSONObject(i).getString("nWithUserId"),
							jsonarray.getJSONObject(i).getString("OrderAmountMail"),
							jsonarray.getJSONObject(i).getString("OrderAmountPhone"),
							(jsonarray.getJSONObject(i).getString("visitcode").equals("")?"NA":jsonarray.getJSONObject(i).getString("visitcode")),
							(jsonarray.getJSONObject(i).getString("Attendance").equals("")?"0":jsonarray.getJSONObject(i).getString("Attendance")),
							(jsonarray.getJSONObject(i).getString("Fromareacode").equals("")?"0":jsonarray.getJSONObject(i).getString("Fromareacode")),
							(jsonarray.getJSONObject(i).getString("OtherExpense").equals("")?"0":jsonarray.getJSONObject(i).getString("OtherExpense")),
							(jsonarray.getJSONObject(i).getString("AndroidAppRemark1").equals("")?"NA":jsonarray.getJSONObject(i).getString("AndroidAppRemark1")),
							(jsonarray.getJSONObject(i).getString("ToAreacode").equals("")?"0":jsonarray.getJSONObject(i).getString("ToAreacode"))
							,true
							,jsonarray.getJSONObject(i).getString("MS")
					);
					Log.i("InserDsr=",jsonarray.getJSONObject(i).getString("UserId")+"-"+
							jsonarray.getJSONObject(i).getString("Vdate")+"-"+
							jsonarray.getJSONObject(i).getString("Android_Id")+"-"+
							jsonarray.getJSONObject(i).getString("CityId")+"-"+
							jsonarray.getJSONObject(i).getString("cityIds")+"-"+
							jsonarray.getJSONObject(i).getString("cityName")+"-"+
							jsonarray.getJSONObject(i).getString("DistId")+"-"+
							jsonarray.getJSONObject(i).getString("Lock")+"-"+
							jsonarray.getJSONObject(i).getString("FROMTime")+"-"+
							jsonarray.getJSONObject(i).getString("ToTime")+"-"+
							jsonarray.getJSONObject(i).getString("Industry")+"-"+
							jsonarray.getJSONObject(i).getString("ModeOfTransport")+"-"+
							jsonarray.getJSONObject(i).getString("nCityId")+"-"+
							jsonarray.getJSONObject(i).getString("NextVisitDate")+"-"+
							jsonarray.getJSONObject(i).getString("WithUserId")+"-"+
							jsonarray.getJSONObject(i).getString("Remark")+"-"+
							jsonarray.getJSONObject(i).getString("SMID")+"-"+
							jsonarray.getJSONObject(i).getString("VehicleUsed")+"-"+
							jsonarray.getJSONObject(i).getString("VisId")+"-"+
							jsonarray.getJSONObject(i).getString("visitDocId")+"-"+
							jsonarray.getJSONObject(i).getString("nWithUserId")+"-"+
							jsonarray.getJSONObject(i).getString("OrderAmountMail")+"-"+
							jsonarray.getJSONObject(i).getString("OrderAmountPhone")+"-"+
							(jsonarray.getJSONObject(i).getString("visitcode").equals("")?"NA":jsonarray.getJSONObject(i).getString("visitcode"))+"-"+
							(jsonarray.getJSONObject(i).getString("Attendance").equals("")?"0":jsonarray.getJSONObject(i).getString("Attendance"))+"-"+
							(jsonarray.getJSONObject(i).getString("Fromareacode").equals("")?"0":jsonarray.getJSONObject(i).getString("Fromareacode"))+"-"+
							(jsonarray.getJSONObject(i).getString("OtherExpense").equals("")?"0":jsonarray.getJSONObject(i).getString("OtherExpense"))+"-"+
							(jsonarray.getJSONObject(i).getString("AndroidAppRemark1").equals("")?"NA":jsonarray.getJSONObject(i).getString("AndroidAppRemark1"))+"-"+
							(jsonarray.getJSONObject(i).getString("ToAreacode").equals("")?"0":jsonarray.getJSONObject(i).getString("ToAreacode"))
							+"-"+
							jsonarray.getJSONObject(i).getString("MS")+"-");
					////publishProgress("Dsr Downloading...."+count++);

				}
				jsonarray=null;
				dsrController.close();
			}catch (Exception e)
			{
				System.out.println(e);
				dsrController.close();
				mSuccess=true;
			}
		}else {mSuccess = true;}
		return mSuccess;
	}*/

    /*****************		END		******************/

/************************		Write By Sandeep Singh 11-04-2017		******************/
    /*****************		START		******************/
    public boolean insertDistributerCollection()
    {
        CollectionController collectionController = new CollectionController(context);
        count=1;
        String maxTimeStamp="0";
        userDataController.open();
        maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION));
        userDataController.close();
        if(maxTimeStamp.equals("0.0"))
        {
            maxTimeStamp="0";
        }
        try
        {
            if(!maxTimeStamp.equals("0"))
            {
                userDataController.open();
                userDataController.deleteTimeStampData(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION,maxTimeStamp);
                userDataController.close();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        JSONParser jParser = new JSONParser();
        ArrayList<Collection> collectionLists = new ArrayList<Collection>();
        int mExit=0;
        areaController = new AreaController(context);
        long serverMaxTimeStamp=getMaxTimestampFromServer("DistColl");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if(connectionDetector.isConnectingToInternet()){
                    try {
                        //publishProgress("Distributor Collection....Reading From Web");
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetDistributorCollection?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetDistributorCollection","Y");
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        collectionController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            String lat = "0",lng = "0",lat_lng_timeStmp = "0";
                            if(jsonarray.getJSONObject(i).has("Latitude"))
                            {
                                lat = jsonarray.getJSONObject(i).getString("Latitude");
                            }
                            if(jsonarray.getJSONObject(i).has("Longitude"))
                            {
                                lng = jsonarray.getJSONObject(i).getString("Longitude");
                            }
                            if(jsonarray.getJSONObject(i).has("latlongdt"))
                            {
                                lat_lng_timeStmp = jsonarray.getJSONObject(i).getString("latlongdt");
                            }
                            JSONObject obj = jsonarray.getJSONObject(i);
                            Collection collectionList = new Collection();
                            collectionList.setAreaId(obj.getString("AreaId"));
                            collectionList.setUserId(obj.getString("UserId"));
                            collectionList.setCreatedDate(obj.getString("CreatedDate"));
                            collectionList.setAmount(obj.getString("Amount"));
                            //collectionList.setAndPartyId(obj.getString("Partyid"));
                            collectionList.setAndroid_id(obj.getString("Android_Id"));
                            collectionList.setBank(obj.getString("Bank"));
                            collectionList.setBranch(obj.getString("Branch"));
                            collectionList.setCheque_DD_Date(obj.getString("Cheque_DD_Date"));
                            collectionList.setCheque_DDNo(obj.getString("Cheque_DD_No"));
                            //collectionList.setColectionImport(obj.getString("Type_Id"));
                            collectionList.setCollDocId(obj.getString("CollDocId"));
                            collectionList.setCollId(obj.getString("CollId"));
                            //collectionList.setDistId(obj.getString("Type_Id"));
                            // collectionList.setDistName(obj.getString("Type_Id"));
                            //collectionList.setIsUpload(obj.getString("Type_Id"));
                            collectionList.setMode(obj.getString("Mode"));
                            collectionList.setDistId(obj.getString("Partyid"));
                            collectionList.setSMId(obj.getString("SMID"));
                            collectionList.setPaymentDate(obj.getString("PaymentDate"));
                            collectionList.setRemarks(obj.getString("Remarks"));
                            collectionList.setVdate(obj.getString("VDate"));
                            collectionList.setVisitId(obj.getString("VisId"));

                            /************************		Write By Sandeep Singh 20-04-2017		******************/
                            /*****************		START		******************/
                            collectionList.setLatitude(lat);
                            collectionList.setLongitude(lng);
                            collectionList.setLatlngTime_stamp(lat_lng_timeStmp);
                            /*****************		END		******************/
                            collectionLists.add(collectionList);
                        }
                        for(int i=0;i<collectionLists.size();i++){
                            collectionLists.get(i).setColectionImport(true);
                            collectionController.insertCollection(collectionLists.get(i));
                            //publishProgress("Distributor Collection Downloading...."+count++);
                        }
                        collectionController.close();
                    }catch (Exception e)
                    {
                        new DbCon(context).insertExportImportLogData(conper,"","JSGetDistributorCollection",e.getMessage());
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
	/*public boolean insertDistributerCollection()
	{
		CollectionController collectionController = new CollectionController(context);
		count=1;
		String maxTimeStamp="0";
		userDataController.open();
		maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION));
		userDataController.close();
		if(maxTimeStamp.equals("0.0"))
		{
			maxTimeStamp="0";
		}
		try{
			if(!maxTimeStamp.equals("0"))
			{
				userDataController.open();
				userDataController.deleteTimeStampData(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION,maxTimeStamp);
				userDataController.close();
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}

		String url="http://"+server+"/And_Sync.asmx/JSGetDistributorCollection?ConPer_Id="+conper+"&minDate="+maxTimeStamp;
		JSONParser jParser = new JSONParser();

		ArrayList<Collection> collectionLists = new ArrayList<Collection>();
		String result = jParser.getJSONArray(url);
		if(connectionDetector.isConnectingToInternet()){
			try {
				////publishProgress("Distributor Collection....Reading From Web");
				JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetDistributorCollection?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
				collectionController.open();
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					Collection collectionList = new Collection();
					collectionList.setAreaId(obj.getString("AreaId"));
					collectionList.setUserId(obj.getString("UserId"));
					collectionList.setCreatedDate(obj.getString("CreatedDate"));
					collectionList.setAmount(obj.getString("Amount"));
					//collectionList.setAndPartyId(obj.getString("Partyid"));
					collectionList.setAndroid_id(obj.getString("Android_Id"));
					collectionList.setBank(obj.getString("Bank"));
					collectionList.setBranch(obj.getString("Branch"));
					collectionList.setCheque_DD_Date(obj.getString("Cheque_DD_Date"));
					collectionList.setCheque_DDNo(obj.getString("Cheque_DD_No"));
					//collectionList.setColectionImport(obj.getString("Type_Id"));
					collectionList.setCollDocId(obj.getString("CollDocId"));
					collectionList.setCollId(obj.getString("CollId"));
					//collectionList.setDistId(obj.getString("Type_Id"));
					// collectionList.setDistName(obj.getString("Type_Id"));
					//collectionList.setIsUpload(obj.getString("Type_Id"));
					collectionList.setMode(obj.getString("Mode"));
					collectionList.setDistId(obj.getString("Partyid"));
					collectionList.setSMId(obj.getString("SMID"));
					collectionList.setPaymentDate(obj.getString("PaymentDate"));
					collectionList.setRemarks(obj.getString("Remarks"));
					collectionList.setVdate(obj.getString("VDate"));
					collectionList.setVisitId(obj.getString("VisId"));
					collectionLists.add(collectionList);
				}
				for(int i=0;i<collectionLists.size();i++){
					collectionLists.get(i).setColectionImport(true);
					collectionController.insertCollection(collectionLists.get(i));
					////publishProgress("Distributor Collection Downloading...."+count++);
				}
				collectionController.close();
			}catch (Exception e)
			{
				System.out.println(e);
				mSuccess=true;
			}
		}else {mSuccess = true;}
		return mSuccess;
	}*/
    /*****************		END		******************/


    public boolean insertFailedVisitRemark()
    {
        count=1;
        FailedVisitRemarkController failedVisitRemarkController=new FailedVisitRemarkController(context);
        JSONParser jParser = new JSONParser();
        count = 1;
        String maxTimeStamp = "0";
        long serverMaxTimeStamp = getMaxTimestampFromServer("FvRemark");

        if(serverMaxTimeStamp != 0) {
            while (true) {
                userDataController.open();
                maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_FAILED_VISIT_MAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                    maxTimeStamp = "-1";
                }

                if (connectionDetector.isConnectingToInternet()) {
                    try {
                        //publishProgress("FailedVisitRemark....Reading From Web");
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetFaildVisitRemark_TimeStampWise?minDate="+maxTimeStamp));

                        if (jsonarray.length() == 0) {
                            break;
                        }
                        failedVisitRemarkController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            failedVisitRemarkController.insertFailedVisitRemark(
                                    jsonarray.getJSONObject(i).getString("ID"),
                                    jsonarray.getJSONObject(i).getString("FvName")
                                    , jsonarray.getJSONObject(i).getString("MS")

                            );
                            //publishProgress("FailedVisitRemark Downloading...."+ count++);
                        }

                        failedVisitRemarkController.close();

                    } catch (Exception e) {
                        failedVisitRemarkController.close();
                        System.out.println(e);
                        mSuccess = true;
                    }

                } else {
                    mSuccess = true;
                }

            }
        }
        return mSuccess;
    }


    public boolean insertAreaMaxTimeStampWize() {
        count=1;int mExit=0;
        String maxTimeStamp = "0";
        areaController = new AreaController(context);
        long serverMaxTimeStamp=getMaxTimestampFromServer("Area");
        Log.e("AreaData=",String.valueOf(serverMaxTimeStamp));
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_AREAMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if (connectionDetector.isConnectingToInternet()) {
                    JSONParser jParser = new JSONParser();
                    //publishProgress("Area....Reading From Web");
                    try {
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetArea_TimeStampWise?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));

                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetArea_TimeStampWise","Y");
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        System.out.print(jsonarray);
                        areaController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            areaController.insertAreaParameter(jsonarray.getJSONObject(i).getString("Id"), jsonarray.getJSONObject(i).getString("Nm"), jsonarray.getJSONObject(i).getString("CID"), jsonarray.getJSONObject(i).getString("MS"));
                            //publishProgress("Area Downloading...." + count++);
                        }
                        jsonarray = null;
                        areaController.close();
                    } catch (Exception e) {
                        areaController.close();
                        System.out.println(e);
                        mSuccess = true;
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetArea_TimeStampWise",e.getMessage());

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


    public void writeLog(String msg) {

        try{
            myFile =new File("/sdcard/areadata.csv");
            //  myFile = new File(Environment.getDataDirectory()+"/fftlog/myuploadfile.txt");
            if(!myFile.exists())
            {
                myFile.createNewFile();
            }
            fOut = new FileOutputStream(myFile,true);
            myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(msg+" \n");
            myOutWriter.close();
            fOut.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    long getMaxTimestampFromServer(String master)
    {
        JSONParser jParser=new JSONParser();long maxTime=0;
        if (connectionDetector.isConnectingToInternet()) {
            try {
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/GetMaxTimeStamp?master=" + master.trim() + "&ConPer_Id=" + conper));
                Log.i("ServerMaxTime","http://" + server + "/And_Sync.asmx/GetMaxTimeStamp?master=" + master.trim() + "&ConPer_Id=" + conper);
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

    private void sendMobileNotification(String messageBody) {
        //Inset Into Table
        //Start
        writeLog("/*******************  Notification    **********************/");
        Long tsLong = System.currentTimeMillis()/1000;

        Intent intent = new Intent(context, DashBoradActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.crmdm_action)
                .setContentTitle(context.getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setTicker(messageBody)

                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notificationBuilder.build());
    }

    public boolean insertPartyTimestampWise() {
        count = 1;
        int mExit = 0;
        boolean isPartyExistWithMobile = false;
        partycontroller = new PartyController(context);
        String maxTimeStamp = "0";
        long serverMaxTimeStamp = getMaxTimestampFromServer("Party");
        if (serverMaxTimeStamp != 0) {
            while (true) {
                userDataController.open();
                maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_PARTYMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                    maxTimeStamp = "-1";
                }
                if (connectionDetector.isConnectingToInternet()) {
                    JSONParser jParser = new JSONParser();
                    //publishProgress("Party....Reading From Web");
                    try {
//                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetParties_TimeStampWise?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetParties_TimeStampWiseLatLongAdd?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetParties_TimeStampWise","Y");

                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }

                        partycontroller.open();
                        for (int i = 0; i < jsonarray.length(); i++) {

                             isPartyExistWithMobile  = partycontroller.isPartyExist(jsonarray.getJSONObject(i).getString("M"),jsonarray.getJSONObject(i).getString("ID"));
                            if(isPartyExistWithMobile)
                            {

                                sendMobileNotification("Mobile number exist for "+jsonarray.getJSONObject(i).getString("Nm")+" while importing from web.");
                                break;
                            }
                            else {
                                partycontroller.insertPartyFromWeb(
                                        jsonarray.getJSONObject(i).getString("At"),
                                        jsonarray.getJSONObject(i).getString("Ad1"),
                                        jsonarray.getJSONObject(i).getString("Ad2"),
                                        jsonarray.getJSONObject(i).getString("Ad"),
                                        jsonarray.getJSONObject(i).getString("Bd"),
                                        jsonarray.getJSONObject(i).getString("Bdt"),
                                        jsonarray.getJSONObject(i).getString("Bby"),
                                        jsonarray.getJSONObject(i).getString("Brzn"),
                                        jsonarray.getJSONObject(i).getString("Ct"),
                                        jsonarray.getJSONObject(i).getString("Cp"),
                                        jsonarray.getJSONObject(i).getString("CST"),
                                        jsonarray.getJSONObject(i).getString("DsId"),
                                        jsonarray.getJSONObject(i).getString("DA"),
                                        jsonarray.getJSONObject(i).getString("DB"),
                                        jsonarray.getJSONObject(i).getString("E"),
                                        jsonarray.getJSONObject(i).getString("Ind"),
                                        jsonarray.getJSONObject(i).getString("M"),
                                        jsonarray.getJSONObject(i).getString("PAN"),
                                        jsonarray.getJSONObject(i).getString("ID"),
                                        jsonarray.getJSONObject(i).getString("Nm"),
                                        jsonarray.getJSONObject(i).getString("PCd"),
                                        jsonarray.getJSONObject(i).getString("Pi"),
                                        jsonarray.getJSONObject(i).getString("Pl"),
                                        jsonarray.getJSONObject(i).getString("R"),
                                        jsonarray.getJSONObject(i).getString("STNo"),
                                        jsonarray.getJSONObject(i).getString("VTn"),
                                        jsonarray.getJSONObject(i).getString("blk"),
                                        jsonarray.getJSONObject(i).getString("MS"),
                                        jsonarray.getJSONObject(i).getString("CreatedBy"),
                                        false,
                                        jsonarray.getJSONObject(i).getString("androidid"),
                                        jsonarray.getJSONObject(i).getString("Latitude"),
                                        jsonarray.getJSONObject(i).getString("Longitude"),
                                        jsonarray.getJSONObject(i).getString("LatlngTime")
                                );
                            }
                            //publishProgress("Party Downloading...." + count++);
                        }
                        jsonarray = null;
                        partycontroller.close();
                    } catch (Exception e) {
                        partycontroller.close();
                        System.out.println(e);
                        mSuccess = true;
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetParties_TimeStampWise",e.getMessage());
                    }
                } else {
                    mSuccess = true;
                }


                if(isPartyExistWithMobile)
                {
                    break;
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


    public boolean insertBeatPlanTimestampWise()
    {
        count=1;int mExit=0;
        beatPlanController=new BeatPlanController(context);
        String maxTimeStamp = "0";
        long serverMaxTimeStamp=getMaxTimestampFromServer("BP");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_TRANSBEATPLAN));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if (connectionDetector.isConnectingToInternet()) {
                    JSONParser jParser = new JSONParser();
                    //publishProgress("Beat Plan....Reading From Web");
                    try {
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetBeatPlan?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        beatPlanController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            beatPlanController.insertBeatPlan(
                                    jsonarray.getJSONObject(i).getString("Bplnd"),
                                    jsonarray.getJSONObject(i).getString("Dcid"),
                                    jsonarray.getJSONObject(i).getString("Uid"),
                                    jsonarray.getJSONObject(i).getString("Aid"),
                                    jsonarray.getJSONObject(i).getString("AppSt"),
                                    jsonarray.getJSONObject(i).getString("AppBy"),
                                    jsonarray.getJSONObject(i).getString("Apprmk"),
                                    jsonarray.getJSONObject(i).getString("StDt"),
                                    jsonarray.getJSONObject(i).getString("Ctid"),
                                    jsonarray.getJSONObject(i).getString("Bid"),
                                    jsonarray.getJSONObject(i).getString("SMId"),
                                    jsonarray.getJSONObject(i).getString("AppBySMId"),
                                    jsonarray.getJSONObject(i).getString("MS"),
                                    jsonarray.getJSONObject(i).getString("Andid"),
                                    jsonarray.getJSONObject(i).getString("Pdt")
                            );
                            //publishProgress("Beat Plan Downloading...."+ count++);
                        }
                        jsonarray = null;
                        beatPlanController.close();
                    } catch (Exception e) {
                        beatPlanController.close();
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


    public boolean insertItemTemplate()
    {
        count=1;int mExit=0;
        historyController=new HistoryController(context);
        String maxTimeStamp = "0";
        long serverMaxTimeStamp=getMaxTimestampFromServer("IT");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_HISTORY));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if (connectionDetector.isConnectingToInternet()) {
                    JSONParser jParser = new JSONParser();
                    //publishProgress("Item Template....Reading From Web");
                    try {
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetOrderTemplate?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        historyController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            historyController.insertHistoryFromWeb(	jsonarray.getJSONObject(i).getString("PartyId"),"",	jsonarray.getJSONObject(i).getString("ItemId"),	jsonarray.getJSONObject(i).getString("Dt")							);
                            //publishProgress("Item Template...."+ count++);
                        }
                        jsonarray = null;
                        historyController.close();
                    } catch (Exception e) {
                        historyController.close();
                        e.printStackTrace();
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

    public boolean insertEnviro()
    {
        ArrayList<Enviro> enviros=new ArrayList<Enviro>();
        JSONParser jParser = new JSONParser();

        try {
            enviroController.open();
            enviros = enviroController.getEnviroList();
            enviroController.close();

        }
        catch(Exception e)
        {

            System.out.println(e);
        }


        if(connectionDetector.isConnectingToInternet()){

            try {

                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSGetAndroidEnviro?SMID="+conper));

                if(jsonarray.length()>0) {
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject json = jsonarray.getJSONObject(i);
                        Enviro enviro = new Enviro();
                        enviro.setSrep_code(conper);
                        enviro.setOrder_no(json.getString("AOrdNo"));
                        enviro.setPorder_no(json.getString("APOrdNo"));
                        enviro.setPorder1_no(json.getString("APOrd1No"));
                        enviro.setParty_no(json.getString("APtyNo"));
                        enviro.setDemo_no(json.getString("ADemoNo"));
                        enviro.setDiscussionNo(json.getString("ADiscNo"));
                        enviro.setDistCollectionNo(json.getString("ADisColNo"));
                        enviro.setFailed_No(json.getString("AFVNo"));
                        enviro.setReciep_no(json.getString("APtyColNo"));
                        enviro.setCompt_no(json.getString("ACmpNo"));
                        enviro.setVisit_no(json.getString("AVisNo"));
                        enviro.setLeave_no("0");
                        enviro.setBeatNo("0");
                        enviro.setOrder1_no(json.getString("AOrd1No"));
                        enviro.setDistStockNo(json.getString("AdistStockno"));
                        enviro.setSalesReturnNo(json.getString("A_SalesReturn_no"));
                        enviro.setSalesReturn1No(json.getString("A_SalesReturn1_no"));

                        if (enviros != null && enviros.size() > 0) {
                            enviroController.open();
                            enviro.setTimeStamp(enviros.get(0).getTimeStamp());
                            //enviroController.deleteEnviro();
                            enviroController.insertEnviro(enviro);
                            enviroController.close();
                        } else {
                            enviroController.open();
                            //enviroController.deleteEnviro();
                            enviro.setTimeStamp("0");
                            enviroController.insertEnviro(enviro);
                            enviroController.close();
                        }
                    }
                }

                else{

                    Enviro enviro = new Enviro();
                    enviro.setSrep_code(conper);
                    enviro.setOrder_no("0");
                    enviro.setPorder_no("0");
                    enviro.setPorder1_no("0");
                    enviro.setParty_no("0");
                    enviro.setDemo_no("0");
                    enviro.setDiscussionNo("0");
                    enviro.setDistCollectionNo("0");
                    enviro.setFailed_No("0");
                    enviro.setReciep_no("0");
                    enviro.setCompt_no("0");
                    enviro.setVisit_no("0");
                    enviro.setLeave_no("0");
                    enviro.setBeatNo("0");
                    enviro.setOrder1_no("0");
                    enviro.setDistStockNo("0");
                    if (enviros != null && enviros.size() > 0) {

                        enviroController.open();
                        enviro.setTimeStamp(enviros.get(0).getTimeStamp());
//						enviroController.deleteEnviro();
                        enviroController.insertEnviro(enviro);
                        enviroController.close();
                    } else {
                        enviroController.open();
//						enviroController.deleteEnviro();
                        enviro.setTimeStamp("0");
                        enviroController.insertEnviro(enviro);
                        enviroController.close();
                    }

                }
            } catch (Exception e) {
                System.out.println(e);
                mSuccess = true;
            }
        }
        else {
            mSuccess = true;}

        return mSuccess;
    }

    public boolean insertProductClass()
    {
        count=1;
        productClassController= new ProductClassController(context);
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
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetProductClass","Y");
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
                        new DbCon(context).insertExportImportLogData(conper,"","xJSGetProductClass",e.getMessage());
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
        count=1;
        segmentController= new SegmentController(context);
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



    public boolean InsertAppEnviroData() {
        String retailer=null;count=1;
        SharedPreferences dpreferences_new = context.getSharedPreferences("DYNAMIC_FORM_FIELDS_INFO", context.MODE_PRIVATE);
        boolean mSuccess = false;
        ArrayList<AppEnviroData> appEnviroDataList = new ArrayList<AppEnviroData>();
        JSONParser jParser = new JSONParser();
        if(connectionDetector.isConnectingToInternet()){
            try {
                ////publishProgress("Enviro....  Reading from web");
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetEnviroSetting"));
                String ret = null;
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    AppEnviroData appEnviroData = new AppEnviroData();
                    appEnviroData.setDistSearch(obj.getString("DistSearch"));
                    appEnviroData.setItemSearch(obj.getString("ItemSearch"));
                    appEnviroData.setItemwisesale(obj.getString("Itemwisesale"));
                    appEnviroData.setAreawiseDistributor(obj.getString("AreawiseDistributor"));
                    appEnviroData.setHost(obj.getString("Host"));
                    appEnviroData.setUserName(obj.getString("UserName"));
                    appEnviroData.setPassword(obj.getString("Password"));
                    appEnviroData.setFtpDirectory(obj.getString("FTP_DIRECTORY"));
                    appEnviroData.setImageDirectoryName(obj.getString("IMAGE_DIRECTORY_NAME"));
                    appEnviroData.setDistDiscussionStock(obj.getString("DistDiscussionStock"));
                    appEnviroData.setDistSearch(obj.getString("DsrRemarkMandatory"));
                    appEnviroData.setPartyCaption(obj.getString("PartyCaption"));
                    // Dynamic Form fields
                    appEnviroData.setDSREntry_WithWhom(obj.getString("DSREntry_WithWhom"));
                    appEnviroData.setDSREntry_NextVisitWithWhom(obj.getString("DSREntry_NextVisitWithWhom"));
                    appEnviroData.setDSREntry_NextVisitDate(obj.getString("DSREntry_NextVisitDate"));
                    appEnviroData.setDSREntry_RetailerOrderByEmail(obj.getString("DSREntry_RetailerOrderByEmail"));
                    appEnviroData.setDSREntry_RetailerOrderByPhone(obj.getString("DSREntry_RetailerOrderByPhone"));
                    appEnviroData.setDSREntry_Remarks(obj.getString("DSREntry_Remarks"));
                    appEnviroData.setDSREntry_ExpensesFromArea(obj.getString("DSREntry_ExpensesFromArea"));
                    appEnviroData.setDSREntry_VisitType(obj.getString("DSREntry_VisitType"));
                    appEnviroData.setDSREntry_Attendance(obj.getString("DSREntry_Attendance"));
                    appEnviroData.setDSREntry_OtherExpenses(obj.getString("DSREntry_OtherExpenses"));
                    appEnviroData.setDSREntry_OtherExpensesRemarks(obj.getString("DSREntry_OtherExpensesRemarks"));
                    appEnviroData.setDSREntry_WithWhom_Rq(obj.getString("DSREntry_WithWhom_Rq"));
                    appEnviroData.setDSREntry_NextVisitWithWhom_Rq(obj.getString("DSREntry_NextVisitWithWhom_Rq"));
                    appEnviroData.setDSREntry_NextVisitDate_Rq(obj.getString("DSREntry_NextVisitDate_Rq"));
                    appEnviroData.setDSREntry_RetailerOrderByEmail_Rq(obj.getString("DSREntry_RetailerOrderByEmail_Rq"));
                    appEnviroData.setDSREntry_RetailerOrderByPhone_Rq(obj.getString("DSREntry_RetailerOrderByPhone_Rq"));
                    appEnviroData.setDSREntry_Remarks_Rq(obj.getString("DSREntry_Remarks_Rq"));
                    appEnviroData.setDSREntry_ExpensesFromArea_Rq(obj.getString("DSREntry_ExpensesFromArea_Rq"));
                    appEnviroData.setDSREntry_VisitType_Rq(obj.getString("DSREntry_VisitType_Rq"));
                    appEnviroData.setDSREntry_Attendance_Rq(obj.getString("DSREntry_Attendance_Rq"));
                    appEnviroData.setDSREntry_OtherExpenses_Rq(obj.getString("DSREntry_OtherExpenses_Rq"));
                    appEnviroData.setDSREntry_OtherExpensesRemarks_Rq(obj.getString("DSREntry_OtherExpensesRemarks_Rq"));
                    appEnviroData.setDSRENTRY_ExpenseToArea(obj.getString("DSRENTRY_ExpenseToArea"));
                    appEnviroData.setDSRENTRY_ExpenseToArea_req(obj.getString("DSRENTRY_ExpenseToArea_req"));
                    appEnviroData.setDSRENTRY_Chargeable(obj.getString("DSRENTRY_Chargeable"));
                    appEnviroData.setDSRENTRY_Chargeable_req(obj.getString("DSRENTRY_Chargeable_req"));
                    appEnviroData.setShow_PartyCollection(obj.getString("Show_PartyCollection"));
                    appEnviroData.setCheckTransactionForLock(obj.getString("CheckTransactionForLock"));
                    appEnviroData.setShow_GeneratePDF(obj.getString("Show_GeneratePDF"));
                    appEnviroData.setShow_UseCamera(obj.getString("UseCamera"));
                    appEnviroData.setNextVisitTime(obj.getString("Primary_Disc_NextVTime"));
                    appEnviroData.setNextVisitTimeRequired(obj.getString("Primary_Disc_NextVTime_Req"));

                    //*********************************

                    /*************		Write By Sandeep Singh 24-03-17-		**************/
                    /**********		Start		**************/
                    appEnviroData.setNotificationInterval(obj.getString("NotificationInterval_Android"));
                    /**********		End		**************/

                    /***********************Start 4rth July***********************/
                    appEnviroData.setDSREntry_Attendancebyorder(obj.getString("DSREntry_Attendancebyorder"));
                    appEnviroData.setDSREntry_Attendancebyorder_Req(obj.getString("DSREntry_Attendancebyorder_Req"));
                    appEnviroData.setDSREntry_AttendanceByphoto(obj.getString("DSREntry_AttendanceByphoto"));
                    appEnviroData.setDSREntry_AttendanceByphoto_Req(obj.getString("DSREntry_AttendanceByphoto_Req"));
                    appEnviroData.setDSREntry_Attendancemannual(obj.getString("DSREntry_Attendancemannual"));
                    appEnviroData.setDSREntry_AttendancemannualReq(obj.getString("DSREntry_AttendancemannualReq"));
                    /*****************************End ************************************/
                    //*********************************

                    //*********************************

                    appEnviroDataList.add(appEnviroData);
                    ret = obj.getString("PartyCaption");

                    //publishProgress("Enviro Downloading...."+ count++);
                }
                if(ret!= null){
                    retailer = ret;
                }
            } catch (Exception e) {
                System.out.println(e);

                mSuccess = true;
            }
        }else {mSuccess = true;}
        if (appEnviroDataList.size() > 0) {

            appEnviroDataController.open();

            appEnviroDataController.deleteAppData();
            for (int i = 0; i < appEnviroDataList.size(); i++) {

                appEnviroDataController.insertAppEnviroData(appEnviroDataList.get(i));
                SharedPreferences.Editor deditor = dpreferences_new.edit();
                deditor.putString("DSREntry_WithWhom", appEnviroDataList.get(i).getDSREntry_WithWhom());
                deditor.putString("DSREntry_NextVisitWithWhom", appEnviroDataList.get(i).getDSREntry_NextVisitWithWhom());
                deditor.putString("DSREntry_NextVisitDate", appEnviroDataList.get(i).getDSREntry_NextVisitDate());
                deditor.putString("DSREntry_RetailerOrderByEmail", appEnviroDataList.get(i).getDSREntry_RetailerOrderByEmail());
                deditor.putString("DSREntry_RetailerOrderByPhone", appEnviroDataList.get(i).getDSREntry_RetailerOrderByPhone());
                deditor.putString("DSREntry_Remarks", appEnviroDataList.get(i).getDSREntry_Remarks());
                deditor.putString("DSREntry_ExpensesFromArea", appEnviroDataList.get(i).getDSREntry_ExpensesFromArea());
                deditor.putString("DSREntry_VisitType", appEnviroDataList.get(i).getDSREntry_VisitType());
                deditor.putString("DSREntry_Attendance", appEnviroDataList.get(i).getDSREntry_Attendance());
                deditor.putString("DSREntry_OtherExpenses", appEnviroDataList.get(i).getDSREntry_OtherExpenses());
                deditor.putString("DSREntry_OtherExpensesRemarks", appEnviroDataList.get(i).getDSREntry_OtherExpensesRemarks());
                deditor.putString("DSREntry_WithWhom_Rq", appEnviroDataList.get(i).getDSREntry_WithWhom_Rq());
                deditor.putString("DSREntry_NextVisitWithWhom_Rq", appEnviroDataList.get(i).getDSREntry_NextVisitWithWhom_Rq());
                deditor.putString("DSREntry_NextVisitDate_Rq", appEnviroDataList.get(i).getDSREntry_NextVisitDate_Rq());
                deditor.putString("DSREntry_RetailerOrderByEmail_Rq", appEnviroDataList.get(i).getDSREntry_RetailerOrderByEmail_Rq());
                deditor.putString("DSREntry_RetailerOrderByPhone_Rq", appEnviroDataList.get(i).getDSREntry_RetailerOrderByPhone_Rq());
                deditor.putString("DSREntry_Remarks_Rq", appEnviroDataList.get(i).getDSREntry_Remarks_Rq());
                deditor.putString("DSREntry_ExpensesFromArea_Rq", appEnviroDataList.get(i).getDSREntry_ExpensesFromArea_Rq());
                deditor.putString("DSREntry_VisitType_Rq", appEnviroDataList.get(i).getDSREntry_VisitType_Rq());
                deditor.putString("DSREntry_Attendance_Rq", appEnviroDataList.get(i).getDSREntry_Attendance_Rq());
                deditor.putString("DSREntry_OtherExpenses_Rq", appEnviroDataList.get(i).getDSREntry_OtherExpenses_Rq());
                deditor.putString("DSREntry_OtherExpensesRemarks_Rq", appEnviroDataList.get(i).getDSREntry_OtherExpensesRemarks_Rq());
                deditor.putString("DSRENTRY_ExpenseToArea", appEnviroDataList.get(i).getDSRENTRY_ExpenseToArea());
                deditor.putString("DSRENTRY_ExpenseToArea_req", appEnviroDataList.get(i).getDSRENTRY_ExpenseToArea_req());
                deditor.putString("DSRENTRY_Chargeable", appEnviroDataList.get(i).getDSRENTRY_Chargeable());
                deditor.putString("DSRENTRY_Chargeable_req", appEnviroDataList.get(i).getDSRENTRY_Chargeable_req());
                deditor.putString("Show_PartyCollection", appEnviroDataList.get(i).getShow_PartyCollection());
                deditor.putString("CheckTransactionForLock", appEnviroDataList.get(i).getCheckTransactionForLock());
                deditor.putString("Show_GeneratePDF", appEnviroDataList.get(i).getShow_GeneratePDF());
                deditor.putString("Show_UseCamera", appEnviroDataList.get(i).getShow_UseCamera());
                /*************		Write By Sandeep Singh 24-03-17-		**************/
                /**********		Start		**************/
                deditor.putString("NotificationInterval", appEnviroDataList.get(i).getNotificationInterval());
                deditor.putString("Primary_Disc_NextVTime",appEnviroDataList.get(i).getNextVisitTime());
                deditor.putString("Primary_Disc_NextVTime_Req", appEnviroDataList.get(i).getNextVisitTimeRequired());
                deditor.putString("Primary_Failed_Visit_TIME",appEnviroDataList.get(i).getPrime_failed_visit_next_time());
                deditor.putString("Primary_Failed_Visit_Time_Req",appEnviroDataList.get(i).getPrime_next_visit_time_req());
                /**********		End		**************/

                /**********Start 4 july attandance added *///////////
                deditor.putString("AttandanceByOrder", appEnviroDataList.get(i).getDSREntry_Attendancebyorder());
                deditor.putString("AttandanceByOrderReq", appEnviroDataList.get(i).getDSREntry_Attendancebyorder_Req());
                deditor.putString("AttendanceByphoto", appEnviroDataList.get(i).getDSREntry_AttendanceByphoto());
                deditor.putString("AttendanceByphoto_Req", appEnviroDataList.get(i).getDSREntry_AttendanceByphoto_Req());
                deditor.putString("Attendancemannual", appEnviroDataList.get(i).getDSREntry_Attendancemannual());
                deditor.putString("AttendancemannualReq", appEnviroDataList.get(i).getDSREntry_AttendancemannualReq());
                //*********************************







                //*********************************
                deditor.commit();
            }
            appEnviroDataController.close();

        }
        try {
            appEnviroDataController.open();
            ArrayList<AppEnviroData> appDataArray1 = appEnviroDataController.getAppEnviroFromDb();
            appEnviroDataController.close();
            AppEnviroData appData1 = appDataArray1.get(0);
            Constant.IMAGE_DIRECTORY_NAME = appData1.getImageDirectoryName();
            Constant.FTP_USER_NAME = appData1.getUserName();
            Constant.FTP_PASSWORD = appData1.getPassword();
            Constant.FTP_DIRECTORY = appData1.getFtpDirectory();
            Constant.FTP_HOST = appData1.getHost();
            Constant.PARTY_CAPTION = appData1.getPartyCaption();


        } catch (Exception e) {
            System.out.println(e);

            mSuccess = true;
        }
        SharedPreferences preferences_new = context.getSharedPreferences("RETAILER_NAME", context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = preferences_new.edit();
        editor2.putString("Name", retailer);
        editor2.commit();



        return mSuccess;
    }


    public boolean checkAreaChange()
    {
        AreaTypeController areaTypeController=new AreaTypeController(context);
        String areaString="0";
        areaTypeController.open();
        areaString=areaTypeController.getAreaString();
        areaTypeController.close();
        JSONParser jParser = new JSONParser();
        ArrayList<AreaType> areaTypes = new ArrayList<AreaType>();
        if(connectionDetector.isConnectingToInternet()){
            try {
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSsalespersonareastring?smid="+conper));
                areaTypeController.open();
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    AreaType areaType = new AreaType();
                    areaType.setArea_type_id("1");
                    areaType.setArea_type_name(obj.getString("aid"));
                    areaTypes.add(areaType);
                }

                if(areaString.equals(areaTypes.get(0).getArea_type_name()))
                {
                    areaMatch=true;
                }

                if(!areaMatch)
                {
                    for(int i=0;i<areaTypes.size();i++){
                        areaTypeController.deleteAreaTypeData();
                        areaTypeController.insertAreaType((areaTypes.get(i)));
                    }

                }
                areaTypeController.close();
            }catch (Exception e)
            {
                System.out.println(e);
                mSuccess=true;
            }
        }else {mSuccess = true;}
        return mSuccess;
    }

/************************		Write By Sandeep Singh 11-04-2017		******************/
    /*****************		START		******************/
    public boolean deleteData()
    {
        String maxTimeStamp="0";
        long TimeStamp=0;ArrayList<Enviro> enviros=null;
        int mExit=0;
        areaController = new AreaController(context);
        long serverMaxTimeStamp=getMaxTimestampFromServer("Deletelog");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_ENVIRO));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                    maxTimeStamp = "-1";
                }
                if (connectionDetector.isConnectingToInternet()) {

                    String url = "http://" + server + "/And_Sync.asmx/xJSDeleteLog?minDate=" + maxTimeStamp;
                    JSONParser jParser = new JSONParser();
                    ArrayList<Entity> entities = new ArrayList<Entity>();
                    String result = jParser.getJSONArray(url);
                    if (result != null) {
                        try {
                            JSONArray jsonarray = new JSONArray(result);
                            for (int i = 0; i < jsonarray.length(); i++) {
                                JSONObject obj = jsonarray.getJSONObject(i);
                                Entity entity = new Entity();
                                entity.setId(obj.getString("ENid"));
                                entity.setName(obj.getString("ENNM"));
                                entity.setMilliSecond(obj.getString("MS"));
                                entities.add(entity);
                            }
                            userDataController.open();
                            for (int i = 0; i < entities.size(); i++) {
                                if (TimeStamp < Long.parseLong(entities.get(i).getMilliSecond())) {
                                    TimeStamp = Long.parseLong(entities.get(i).getMilliSecond());
                                }
                                userDataController.deleteEntityData(entities.get(i).getName(), entities.get(i).getId());
                            }
                            userDataController.close();

                            try {
                                enviroController.open();
                                enviros = enviroController.getEnviroList();
                                enviroController.close();

                            } catch (Exception e) {
                            }
//					enviroController.open();


//					enviroController.deleteEnviro();
                            enviroController.open();
                            if (enviros != null && enviros.size() > 0) {
                                enviroController.updateEnviroTimeStamp(TimeStamp);

                            } else {
                                Enviro enviro = new Enviro();
                                enviro.setSrep_code(conper);
                                enviro.setTimeStamp(String.valueOf(TimeStamp));
                                enviro.setOrder_no("0");
                                enviro.setPorder_no("0");
                                enviro.setPorder1_no("0");
                                enviro.setParty_no("0");
                                enviro.setDemo_no("0");
                                enviro.setDiscussionNo("0");
                                enviro.setDistCollectionNo("0");
                                enviro.setFailed_No("0");
                                enviro.setReciep_no("0");
                                enviro.setCompt_no("0");
                                enviro.setVisit_no("0");
                                enviro.setLeave_no("0");
                                enviro.setBeatNo("0");
                                enviro.setOrder1_no("0");
                                enviroController.insertEnviro(enviro);
//					enviroController.updateEnviroTimeStamp(String.valueOf(TimeStamp));

                            }
                            enviroController.close();


                        } catch (Exception e) {
                            System.out.println(e);
                            enviroController.close();
                            mSuccess = true;
                        }
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
        }
        return mSuccess;
    }


    public boolean insertPartyType()
    {
        count=1;
        JSONParser jParser = new JSONParser();
        partyTypeController=new PartyTypeController(context);
        ArrayList<PartyType> types = new ArrayList<PartyType>();

        String maxTimeStamp = "";
        long maxTimeStampWise = getMaxTimestampFromServer("partytype");

        if (maxTimeStampWise != 0) {
            while (true) {
                userDataController.open();
                maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_PARTYTYPEMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                    maxTimeStamp = "-1";
                }
                if (connectionDetector.isConnectingToInternet()) {
                    try {

                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetPartyType?minDate="+maxTimeStamp));
                        if (jsonarray.length() == 0) {
                            mSuccess = false;
                            return mSuccess;
                        }
                        partyTypeController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            PartyType type = new PartyType();
                            type.setCreatedDate(" ");
                            type.setId(obj.getString("Id"));
                            type.setName(obj.getString("Nm"));
                            type.setCreatedDate(obj.getString("MS"));
                            types.add(type);


                        }
                        for (int i = 0; i < types.size(); i++) {
                            partyTypeController.insertArea(types.get(i));

                        }
                        partyTypeController.close();

                    } catch (Exception e) {
                        System.out.println(e);
                        mSuccess = true;
                    }
                } else {
                    mSuccess = true;
                }
            }
        }
        return mSuccess;
    }


	/*public boolean deleteData()
	{
		String maxTimeStamp="0";
		long TimeStamp=0;ArrayList<Enviro> enviros=null;
		userDataController.open();
		maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_ENVIRO));
		userDataController.close();
		if(maxTimeStamp.equals("0.0"))
		{
			maxTimeStamp="0";
		}
		String url="http://"+server+"/And_Sync.asmx/xJSDeleteLog?minDate="+maxTimeStamp;
		JSONParser jParser = new JSONParser();
		ArrayList<Entity> entities = new ArrayList<Entity>();
		String result = jParser.getJSONArray(url);
		if (result != null) {
			try {
				JSONArray jsonarray = new JSONArray(result);
				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					Entity entity = new Entity();
					entity.setId(obj.getString("ENid"));
					entity.setName(obj.getString("ENNM"));
					entity.setMilliSecond(obj.getString("MS"));
					entities.add(entity);
				}
				userDataController.open();
				for(int i=0;i<entities.size();i++)
				{
					if(TimeStamp<Long.parseLong(entities.get(i).getMilliSecond()))
					{
						TimeStamp=Long.parseLong(entities.get(i).getMilliSecond());
					}
					userDataController.deleteEntityData(entities.get(i).getName(),entities.get(i).getId());
				}
				userDataController.close();

				try {
					enviroController.open();
					enviros = enviroController.getEnviroList();
					enviroController.close();

				}
				catch(Exception e)
				{}
//					enviroController.open();





//					enviroController.deleteEnviro();
				enviroController.open();
				if (enviros != null && enviros.size() > 0) {
					enviroController.updateEnviroTimeStamp(TimeStamp);

				}
				else
				{
					Enviro enviro=new Enviro();
					enviro.setSrep_code(conper);
					enviro.setTimeStamp(String.valueOf(TimeStamp));
					enviro.setOrder_no("0");
					enviro.setPorder_no("0");
					enviro.setPorder1_no("0");
					enviro.setParty_no("0");
					enviro.setDemo_no("0");
					enviro.setDiscussionNo("0");
					enviro.setDistCollectionNo("0");
					enviro.setFailed_No("0");
					enviro.setReciep_no("0");
					enviro.setCompt_no("0");
					enviro.setVisit_no("0");
					enviro.setLeave_no("0");
					enviro.setBeatNo("0");
					enviro.setOrder1_no("0");
					enviroController.insertEnviro(enviro);
//					enviroController.updateEnviroTimeStamp(String.valueOf(TimeStamp));

				}
				enviroController.close();



			}catch (Exception e)
			{
				System.out.println(e);
				enviroController.close();
				mSuccess=true;
			}
		}
		return mSuccess;
	}
*/
    /*****************		END		******************/

//**************************** Swati*************************

    public boolean insertDistStockItem()
    {
        count=1;
        DistStockController distStockController=new DistStockController(context);
        String maxTimeStamp="0";
        userDataController.open();
        maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_TRANS_DIST_STOCK));
        userDataController.close();
        if(maxTimeStamp.equals("0.0"))
        {
            maxTimeStamp="0";
        }
        try{
            if(!maxTimeStamp.equals("0"))
            {
                userDataController.open();
                userDataController.deleteTimeStampData(DatabaseConnection.TABLE_TRANS_DIST_STOCK,maxTimeStamp);
                userDataController.close();
            }
        }
        catch(Exception e)
        {
            System.out.println(e);
        }

        JSONParser jParser = new JSONParser();
        int mExit=0;
        long serverMaxTimeStamp=getMaxTimestampFromServer("DistStock");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_TRANS_DIST_STOCK));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if(connectionDetector.isConnectingToInternet()){
                    try {
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/JSGetDistStock?ConPer_Id=" + conper + "&minDate=" + maxTimeStamp));
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        distStockController.open();
                        for(int i=0; i<jsonarray.length(); i++)
                        {
                            distStockController.insertDistStockFromWeb(
                                    jsonarray.getJSONObject(i).getString("STKDocId"),
                                    jsonarray.getJSONObject(i).getString("AreaId"),
                                    jsonarray.getJSONObject(i).getString("VisId"),
                                    jsonarray.getJSONObject(i).getString("Android_Id"),
                                    jsonarray.getJSONObject(i).getString("UserId"),
                                    jsonarray.getJSONObject(i).getString("VDate"),
                                    jsonarray.getJSONObject(i).getString("SMID"),
                                    jsonarray.getJSONObject(i).getString("Partyid"),
                                    jsonarray.getJSONObject(i).getString("Item"),
                                    jsonarray.getJSONObject(i).getString("Unit"),
                                    jsonarray.getJSONObject(i).getString("Cases"),
                                    jsonarray.getJSONObject(i).getString("Qty"),
                                    jsonarray.getJSONObject(i).getString("Latitude"),
                                    jsonarray.getJSONObject(i).getString("Longitude"),
                                    jsonarray.getJSONObject(i).getString("latlongdt"),
                                    jsonarray.getJSONObject(i).getString("Millisecond"),
                                    false
                            );

                        }
                        jsonarray=null;
                        distStockController.close();
                    }
                    catch (Exception e)
                    {
                        distStockController.close();
                        System.out.println(e);
                        mSuccess=true;
                    }
                }	else {mSuccess = true;}
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

    public boolean insertStockItemTemplateTimestampWise()
    {
        count=1;int mExit=0;
        historyController=new HistoryController(context);
        String maxTimeStamp = "0";
        long serverMaxTimeStamp=getMaxTimestampFromServer("DistItemTemplate");
        if(serverMaxTimeStamp!=0) {
            while (true) {
                userDataController.open();
                maxTimeStamp=String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_DIST_ITEM_TEMPLATE));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))){maxTimeStamp="-1";}
                if (connectionDetector.isConnectingToInternet()) {
                    JSONParser jParser = new JSONParser();
                    //publishProgress("Stock....Reading From Web");
                    try {
                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetDistTemplate?minDate=" + maxTimeStamp + "&smid=" + conper));
                        if(jsonarray.length() == 0)
                        {
                            mSuccess=false;
                            break;
                        }
                        historyController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            historyController.insertStockTemplateFromWeb(jsonarray.getJSONObject(i).getString("PartyId"),jsonarray.getJSONObject(i).getString("ItemId"),jsonarray.getJSONObject(i).getString("MS")							);
                            //publishProgress("Item Stock...."+ count++);
                        }
                        jsonarray = null;
                        historyController.close();
                    } catch (Exception e) {
                        historyController.close();
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

//    **************************** End ***************************

    public boolean getDynamicMenuData() {
        DbCon dbCon=new DbCon(context);
        String urlJsonObj = "http://" + server + "/And_Sync.asmx/xJSGetDynamicMenu?Smid=" + conper ;
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(urlJsonObj);
        if (result != null)
        {
            try {
                JSONArray jsonarray = new JSONArray(result);
                dbCon.open();
                dbCon.truncate(DatabaseConnection.TABLE_DYNAMIC_MENU);
                dbCon.close();
                for (int i = 0; i < jsonarray.length(); i++)
                {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    String FormFilter     = obj.getString("Form_Filter");
                    String pageid         = obj.getString("pageid");
                    String viewp          = obj.getString("viewp");
                    String addp           = obj.getString("addp");
                    String editp          = obj.getString("editp");
                    String deletep        = obj.getString("deletep");
                    String pagename       = obj.getString("pagename");
                    String displayname    = obj.getString("displayname");
                    String parentid       = obj.getString("parentid");
                    String level_idx      = obj.getString("level_idx");
                    String idx            = obj.getString("idx");
                    String icon           = obj.getString("Icon");


                    ContentValues values = new ContentValues();
                    values.put(DatabaseConnection.COLUMN_Form_Filter,FormFilter);
                    values.put(DatabaseConnection.COLUMN_PAGE_ID,pageid);
                    values.put(DatabaseConnection.COLUMN_VIEW_P, viewp);
                    values.put(DatabaseConnection.COLUMN_ADD_P, addp);
                    values.put(DatabaseConnection.COLUMN_EDIT_P, editp);
                    values.put(DatabaseConnection.COLUMN_DELETE_P, deletep);
                    values.put(DatabaseConnection.COLUMN_PAGE_NAME, pagename);
                    values.put(DatabaseConnection.COLUMN_DISPLAY_NAME, displayname);
                    values.put(DatabaseConnection.COLUMN_PARENT_ID, parentid);
                    values.put(DatabaseConnection.COLUMN_LEVEL_IDX, level_idx);
                    values.put(DatabaseConnection.COLUMN_IDX, idx);
                    values.put(DatabaseConnection.COLUMN_Icon,icon);

                    dbCon.open();
                    dbCon.insert(DatabaseConnection.TABLE_DYNAMIC_MENU,values);
                    dbCon.close();


                }

            } catch (Exception e) {
                System.out.println(e);
                mSuccess=true;

            }


        }
        return mSuccess;
    }

    public String downloadImage(String path,String prefix)
    {

        File file=null;
        int pos;String imgurl="";
        if(!path.equalsIgnoreCase("")) {
            if (!path.equalsIgnoreCase("NA")) {
                pos = path.indexOf("/");
                imgurl = path.substring(pos + 1, path.length());
                System.out.println(imgurl);
                try {
                    URL url = new URL("http://" + server + "/" + imgurl); //you can write here any link
//			File file = new File(fileName);
//			long startTime = System.currentTimeMillis();
//			tv.setText("Starting download......from " + url);

                    File dir = new File(Environment.getExternalStorageDirectory(), "DmCRM");
                    if (!dir.exists())
                        dir.mkdirs();
                    Date date = new Date();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
                    file = new File(dir.getPath() + "/" + prefix + timeStamp + ".jpg");
                    URLConnection ucon = url.openConnection();
                    InputStream is = ucon.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    /*
                     * Read bytes to the Buffer until there is nothing more to read(-1).
                     */
                    ByteArrayBuffer baf = new ByteArrayBuffer(50);
                    int current = 0;
                    while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                    }

                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(baf.toByteArray());
                    fos.close();
//			tv.setText("Download Completed in" + ((System.currentTimeMillis() - startTime) / 1000) + " sec");
                } catch (IOException e) {
//			tv.setText("Error: " + e);
                    System.out.println(e);

                }
            }
        }
        return (file==null?"NA":file.getPath());
    }




    public boolean insertIndustryTimeStampWise() {
        count = 1;
        String maxTimeStamp = "0";
        int mExit = 0;
        itemController = new ItemController(context);
        long serverMaxTimeStamp = getMaxTimestampFromServer("Industry");
        industryController = new IndustryController(context);
        if (serverMaxTimeStamp != 0) {
            while (true) {
                userDataController.open();
                maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_INDUSTRYMAST));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                    maxTimeStamp = "-1";
                }

                if (connectionDetector.isConnectingToInternet()) {

                    JSONParser jParser = new JSONParser();
                    try {

                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetIndustry_TimeStampWise?minDate=" + maxTimeStamp));
                        if (jsonarray.length() == 0) {
                            mSuccess = false;
                            return mSuccess;
                        }
                        industryController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            industryController.insertIndustry(
                                    jsonarray.getJSONObject(i).getString("Id"),
                                    jsonarray.getJSONObject(i).getString("Nm"),
                                    jsonarray.getJSONObject(i).getString("MS")
                            );

                        }
                        jsonarray = null;
                        industryController.close();


                    } catch (Exception e) {
                        industryController.close();
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



    public boolean insertScheme_timestampWise()
    {

        count = 1;
        String maxTimeStamp = "0";
        int mExit = 0;
        itemController = new ItemController(context);
        long serverMaxTimeStamp = getMaxTimestampFromServer("Scheme");

        schemeController = new SchemeController(context);
        if (serverMaxTimeStamp != 0) {
            while (true) {
                userDataController.open();
                maxTimeStamp = String.valueOf(userDataController.getMaxTimeStamp(DatabaseConnection.TABLE_SCHEME));
                userDataController.close();
                if ((maxTimeStamp.equals("0.0") || (maxTimeStamp.equals("0")))) {
                    maxTimeStamp = "-1";
                }

                if (connectionDetector.isConnectingToInternet()) {
                    // publishProgress("Industry....Reading From Web");
                    JSONParser jParser = new JSONParser();
                    try {

                        JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://" + server + "/And_Sync.asmx/xJSGetScheme_TimeStampWise?minDate=" + maxTimeStamp));
                        if (jsonarray.length() == 0) {
                            mSuccess = false;
                            return mSuccess;
                        }
                        schemeController.open();
                        for (int i = 0; i < jsonarray.length(); i++) {
                            schemeController.insertScheme(jsonarray.getJSONObject(i).getString("Id"),jsonarray.getJSONObject(i).getString("Nm"),jsonarray.getJSONObject(i).getString("MS"));

                        }
                        schemeController.close();


                    } catch (Exception e) {
                        schemeController.close();
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
                        //  Exportlog(e.getMessage());
                    }
                    mExit++;
                    if (mExit > 0) break;
                }
            }

        }
        return mSuccess;
    }

}
