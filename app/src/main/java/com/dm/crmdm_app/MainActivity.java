package com.dm.crmdm_app;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.controller.AppEnviroDataController;
import com.dm.controller.CityController;
import com.dm.controller.CountryController;
import com.dm.controller.DeviceInfoController;
import com.dm.controller.DsrController;
import com.dm.controller.StateController;
import com.dm.controller.UserDataController;
import com.dm.database.DatabaseConnection;
import com.dm.library.AlertOkFinishDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.Constant;
import com.dm.library.Custom_Toast;
import com.dm.library.DbCon;
import com.dm.library.IntentSend;
import com.dm.model.AppData;
import com.dm.model.AppEnviroData;
import com.dm.model.Country;
import com.dm.model.User;
import com.dm.parser.JSONParser;
import com.dm.service.NotificationService;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.games.video.Video;

public class MainActivity extends Activity {

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 500;
	AppDataController appDataController;
	AppEnviroDataController appEnviroDataController;
	int dataExist = 0;
	int userExist = 0;
	AlertOkFinishDialog alertOkDialog;
	UserDataController userDataController;
	ConnectionDetector connectionDetector;DbCon dbcon ;
	ArrayList<User> users;DeviceInfoController deviceInfoController;
	private static int REQUEST_READ_PHONE_STATE = 1;String t="";
	String retailer=null;
	String RollType,Solidsmid;
	public  static String mDeviceNo="N";
	TextView loadingIndicatorTextVew;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		loadingIndicatorTextVew=(TextView) findViewById(R.id.loading_indicator);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				getDynamicMenuData();
			}
		},50);
		SharedPreferences preferences = getSharedPreferences("ENVIRO_SESSION_DATA", MODE_PRIVATE);
		mDeviceNo=preferences.getString("DeviceId", "N");
		if(mDeviceNo.equalsIgnoreCase("N")){
			SharedPreferences settings = getApplicationContext().getSharedPreferences("ENVIRO_SESSION_DATA", getApplicationContext().MODE_PRIVATE);
			settings.edit().clear().commit();
			Intent intent=new Intent(MainActivity.this,UserPermission.class);
			startActivity(intent);
			finish();
		}
		startService(new Intent(this, NewLocationService.class));
		startService(new Intent(this, NotificationService.class));
		appDataController = new AppDataController(getApplicationContext());
		userDataController = new UserDataController(getApplicationContext());
		appEnviroDataController = new AppEnviroDataController(getApplicationContext());
		deviceInfoController=new DeviceInfoController(getApplicationContext());
		connectionDetector = new ConnectionDetector(this);
		dbcon=new DbCon(this);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				try {
					writeLogInWeb("23. main start from local");
					appDataController.open();
					dataExist = appDataController.getAppDataExist();
					appDataController.close();
					userDataController.open();
					userExist = userDataController.getUserExist();
					userDataController.close();
					writeLogInWeb("24. dataExist=" + dataExist);
					writeLogInWeb("25. userExist=" + userExist);

				} catch (Exception e) {
					System.out.println(e);
					writeLogInWeb("26. Exception=" + e);
				}
				writeLogInWeb("27. before if ");
				if (dataExist > 0 && userExist > 0) {
					writeLogInWeb("28. after if ");
					ArrayList<AppEnviroData> appDataArray1;
					AppEnviroData appData1 = null;
					ArrayList<AppData> appDataArray;
					AppData appData;
					appDataController.open();
					appDataArray = appDataController.getAppTypeFromDb();
					appDataController.close();
					appData = appDataArray.get(0);
					appEnviroDataController.open();
					appDataArray1 = appEnviroDataController.getAppEnviroFromDb();
					appEnviroDataController.close();

					Constant.SERVER_WEBSERVICE_URL = appData.getCompanyUrl();
					if(appDataArray1.size() != 0)
					{
						appData1 = appDataArray1.get(0);
						Constant.IMAGE_DIRECTORY_NAME = appData1.getImageDirectoryName();
						Constant.FTP_USER_NAME = appData1.getUserName();
						Constant.FTP_PASSWORD = appData1.getPassword();
						Constant.FTP_DIRECTORY = appData1.getFtpDirectory();
						Constant.FTP_HOST = appData1.getHost();
						Constant.PARTY_CAPTION = appData1.getPartyCaption();
					}

					userDataController.open();
					users = userDataController.getUserList();
					userDataController.close();
					User user = new User();
					for (int i = 0; i < users.size(); i++) {
						user = users.get(i);
					}

					SharedPreferences preferences = getSharedPreferences("RETAILER_SESSION_DATA", MODE_PRIVATE);
					Editor editor = preferences.edit();
					editor.putString("CONPERID_SESSION", user.getConper_id());
					editor.putString("PDAID_SESSION", user.getPDA_Id());
					editor.putString("USER_ID", user.getUserId());
					editor.commit();
					SharedPreferences preferences1 = getSharedPreferences("MyPref", MODE_PRIVATE);
					Editor editor1 = preferences1.edit();
					editor1.putString("CONPERID_SESSION", user.getConper_id());
					editor1.putString("PDAID_SESSION", user.getPDA_Id());
					editor1.putString("USER_ID", user.getUserId());
					editor1.commit();
					writeLogInWeb("30. Constant.SERVER_WEBSERVICE_URL= " + Constant.SERVER_WEBSERVICE_URL + " Constant.IMAGE_DIRECTORY_NAME= " + Constant.IMAGE_DIRECTORY_NAME + " Constant.FTP_USER_NAME= " + Constant.FTP_USER_NAME + " Constant.PARTY_CAPTION=" + Constant.PARTY_CAPTION);
					Constant.SERVER_WEBSERVICE_URL = appData.getCompanyUrl();
					if(appData1!=null) {
						Constant.IMAGE_DIRECTORY_NAME = appData1.getImageDirectoryName();
						Constant.FTP_USER_NAME = appData1.getUserName();
						Constant.FTP_PASSWORD = appData1.getPassword();
						Constant.FTP_DIRECTORY = appData1.getFtpDirectory();
						Constant.FTP_HOST = appData1.getHost();
						Constant.PARTY_CAPTION = appData1.getPartyCaption();
					}
					writeLogInWeb("31. Navigating to DashBoradActivity");

//					MainActivity.this.startService(new Intent(MainActivity.this,SyncData.class));


					DsrController dsrController = new DsrController(MainActivity.this);
					dsrController.open();
					String isDataImported=dsrController.getIsDataImported();
					//	 dsrController.deleteOrderRow();
					dsrController.close();
					if (isDataImported.equals("true")) {
						//new IntentSend(getApplicationContext(), DashBoradActivity.class).toSendAcivity();
						SharedPreferences dpreferences=getApplicationContext().getSharedPreferences("DYNAMIC_FORM_FIELDS_INFO",getApplicationContext().MODE_PRIVATE);
						Context ctx = getApplicationContext();
						Calendar cal = Calendar.getInstance();
						AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
						//long interval = 1000  60  6;
						long interval =6*Long.parseLong(dpreferences.getString("NotificationInterval", "0"));
						Intent serviceIntent = new Intent(ctx, NotificationService.class);
						PendingIntent servicePendingIntent =PendingIntent.getService(ctx,0,serviceIntent,PendingIntent.FLAG_CANCEL_CURRENT);
						am.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),interval,servicePendingIntent);
						getDynamicMenuData();
					}
					else
					{
						blink();
						//new IntentSend(getApplicationContext(),Syncronization.class).toSendAcivity();
						getDynamicMenuData();
						/*Intent newIntent = new Intent(MainActivity.this, DashBoradActivity.class);
						newIntent.putExtra("key",1);
						startActivity(newIntent);
						finish();*/
					}


				}
				else {
					blink();
					new CheckFromLicense().execute();
				}

			}
		}, SPLASH_TIME_OUT);
		client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
	}
	private void blink(){
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				int timeToBlink = 1000;    //in milissegunds
				try{Thread.sleep(timeToBlink);}catch (Exception e) {}
				handler.post(new Runnable() {
					@Override
					public void run() {
						TextView txt = (TextView) findViewById(R.id.loading_indicator);
						if(txt.getVisibility() == View.VISIBLE){
							txt.setVisibility(View.INVISIBLE);
						}else{
							txt.setVisibility(View.VISIBLE);
						}
						blink();
					}
				});
			}
		}).start();
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client.connect();
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Main Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.dm.dmgrahak/http/host/path")
		);
		AppIndex.AppIndexApi.start(client, viewAction);
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		Action viewAction = Action.newAction(
				Action.TYPE_VIEW, // TODO: choose an action type.
				"Main Page", // TODO: Define a title for the content shown.
				// TODO: If you have web page content that matches this app activity's content,
				// make sure this auto-generated web page URL is correct.
				// Otherwise, set the URL to null.
				Uri.parse("http://host/path"),
				// TODO: Make sure this auto-generated app URL is correct.
				Uri.parse("android-app://com.dm.dmgrahak/http/host/path")
		);
		AppIndex.AppIndexApi.end(client, viewAction);
		client.disconnect();
	}


	class CheckFromLicense extends AsyncTask<String, String, String> {
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			//progressDialog= ProgressDialog.show(LoginActivity.this,"processing Data", "Checking Credentials Please wait...", true);

		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			try {

                deviceInfoController.open();
				downloadAppData(deviceInfoController.getpdaId());
				deviceInfoController.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
//			    alertOkDialog=AlertOkDialog.newInstance("doInBackground\n\nMessage:-"+e.toString());
//		        alertOkDialog.show(getFragmentManager(), "ioe");
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
//			new IntentSend(getApplicationContext(), LoginActivity.class).toSendAcivity();
		}
	}


	void getImeiNo()
	{

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
		{
			int permissionCheck = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE);
			int s=PackageManager.PERMISSION_GRANTED;
			System.out.print(s);
			if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
//				ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, REQUEST_READ_PHONE_STATE);
				ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CLEAR_APP_CACHE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.INTERNET}, REQUEST_READ_PHONE_STATE);

			}
			else{
				System.out.print(s);
				TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
				String r=telephonyManager.getDeviceId();
				System.out.println(r);
				try {
					deviceInfoController.open();
					deviceInfoController.deletedeviceRow();
					deviceInfoController.insertDeviceInfo(r);
					deviceInfoController.close();
				}
				catch(Exception e)
				{
					deviceInfoController.close();
					System.out.print(e);
				}
				if (connectionDetector.isConnectingToInternet()) {
					new CheckFromLicense().execute();

				} else {

					writeLogInWeb("32. You don't have internet connection");
					alertOkDialog = AlertOkFinishDialog.newInstance("You don't have internet connection.");
					alertOkDialog.show(getFragmentManager(), "Information");
				}




			}
		}

		else {
			//TODO

			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String r=telephonyManager.getDeviceId();
			System.out.println(r);
			try {
				deviceInfoController.open();
				deviceInfoController.deletedeviceRow();
				deviceInfoController.insertDeviceInfo(r);
				deviceInfoController.close();
			}
			catch(Exception e)
			{
				deviceInfoController.close();
				System.out.print(e);
			}
			if (connectionDetector.isConnectingToInternet()) {
				new CheckFromLicense().execute();

			} else {

				writeLogInWeb("32. You don't have internet connection");
				alertOkDialog = AlertOkFinishDialog.newInstance("You don't have internet connection.");
				alertOkDialog.show(getFragmentManager(), "Information");
			}
		}

	}

	void downloadAppData(String t) {
		String urlJsonObj = "";
//		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		String c= telephonyManager.getDeviceId();
//		System.out.println(c);
		try {
			urlJsonObj = "http://" + Constant.SERVER_LICENSE_WEBSERVICE_URL + "/jsonwebservice.asmx/JGetLicenseDetailForGrahak?DeviceNo=" +t;
			System.out.println(urlJsonObj);
		} catch (Exception e) {
			System.out.println(e);

		}
		try {
			JSONParser jParser = new JSONParser();
			JSONObject json = jParser.getJSONFromUrl(urlJsonObj);
			System.out.println(json);
			AppData appData = new AppData();
			appData.setCompanyName(json.getString("CompName"));
			appData.setCompanyUrl(json.getString("CompUrl"));
			appData.setAppType(json.getString("AppType"));
			System.out.println(appData);

			//-------------------------------------------------------------
			if (!appData.getCompanyUrl().equalsIgnoreCase("no")) {
				AppDataController appDataController = new AppDataController(getApplicationContext());
				appDataController.open();
				try {

					appDataController.deleteAppData();
				} catch (Exception e) {
					System.out.println(e);

				}
				try {
					appDataController.insertAppData(appData);
				} catch (Exception e) {
					System.out.println(e);

				}
				appDataController.close();
				Constant.SERVER_WEBSERVICE_URL = appData.getCompanyUrl();
				writeLogInWeb("1. Get company url");
				insertuserdata(t);
			} else {

//				new ClearData().execute();
				alertOkDialog = AlertOkFinishDialog.newInstance("Oops! This device no. " + t + " not registered.\nPlease contact to administrator .");
				alertOkDialog.show(getFragmentManager(), "error");
			}

		} catch (Exception e) {
			System.out.println(e);

		}

	}

	class ClearData extends AsyncTask<String, String, String>
	{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			progressDialog= ProgressDialog.show(context,"Clean", "Cleaning Data Please wait...", true);
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);


		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				clearApplicationData();

			} catch (Exception e) {
//				progressDialog.dismiss();
			}
			return null;
		}}

	public void clearApplicationData() {
		File cache = MainActivity.this.getCacheDir();
		File appDir = new File(cache.getParent());
		if (appDir.exists()) {
			String[] children = appDir.list();
			for (String s : children) {
				if (!s.equals("lib")) {
					deleteDir(new File(appDir, s));

				}
			}
		}
	}
	public static boolean deleteDir(File dir) {
		if (dir != null && dir.isDirectory()) {
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}

		return dir.delete();
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		if (requestCode == REQUEST_READ_PHONE_STATE) {
			if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
				//TODO



				try
				{

					TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					t = telephonyManager.getDeviceId();
					System.out.println(t);
					deviceInfoController.open();
					deviceInfoController.deletedeviceRow();
					deviceInfoController.insertDeviceInfo(t);
					deviceInfoController.close();
					if (connectionDetector.isConnectingToInternet()) {
						new CheckFromLicense().execute();
					}
					else {

						writeLogInWeb("32. You don't have internet connection");
						alertOkDialog = AlertOkFinishDialog.newInstance("You don't have internet connection.");
						alertOkDialog.show(getFragmentManager(), "Information");
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

				}



		} else {


			alertOkDialog = AlertOkFinishDialog.newInstance("Unable to get Device Information\nPlease grant permission");
			alertOkDialog.show(getFragmentManager(), "error");

		}
	}

	public void insertuserdata(String pda_id) {
		writeLogInWeb("2. insertuserdata");
		ArrayList<User> userList = new ArrayList<User>();
		String urlJsonObj = "http://" + Constant.SERVER_WEBSERVICE_URL + "/And_Sync.asmx/JSGetUserDetailByPDAId?PDA_Id=" + pda_id + "&minDate=0";
		System.out.println(urlJsonObj);
		writeLogInWeb("3. urlJsonObj=" + urlJsonObj);
		JSONParser jParser = new JSONParser();
		String result = jParser.getJSONArray(urlJsonObj);
		if (result != null) {
			//InsertAppEnviroData();
			try {
				JSONArray jsonarray = new JSONArray(result);

				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					User user = new User();
					user.setConper_id(obj.getString("ConPerId"));
					user.setLevel(obj.getString("Level"));
					user.setUser_name(obj.getString("User_Name"));
					user.setUserId(obj.getString("UserId"));
					user.setPassword(obj.getString("PassWd"));
					user.setPDA_Id(obj.getString("PDA_Id"));
					user.setDSRAllowDays(Integer.parseInt(obj.getString("DSRAllowDays")));
					user.setRoleId(obj.getString("RoleId"));
					user.setDeptId(obj.getString("DeptId"));
					user.setDesigId(obj.getString("DesigId"));
					user.setEmail(obj.getString("Email"));
					user.setActive(obj.getString("ActiveYN"));
					//user.setRollType(obj.getString("RoleType"));
					RollType = obj.getString("RoleType");
					userList.add(user);
				}

			} catch (Exception e) {
				System.out.println(e);
				writeLogInWeb("4. Exception: " + e);
			}

		}

		String activeCheck = "";

		if (userList.size() > 0) {
			writeLogInWeb("5. In if ");

			for (int i = 0; i < userList.size(); i++) {
				activeCheck = userList.get(i).getActive();
				writeLogInWeb("6. activeCheck=" + activeCheck);
			}
			if (activeCheck.equalsIgnoreCase("false")) {
				writeLogInWeb("7. In if  activeCheck false");

				alertOkDialog = AlertOkFinishDialog.newInstance("You are not an active user.\nPlease contact to administrator.");
				alertOkDialog.show(getFragmentManager(), "error");

			} else {
				writeLogInWeb("8. In else  activeCheck true");
				userDataController.open();
				for (int i = 0; i < userList.size(); i++) {
					activeCheck = userList.get(i).getActive();
					userDataController.insertUser(userList.get(i));
					System.out.println("inserted");
					writeLogInWeb("9. user Inserted in local db");

				}
				userDataController.close();

				userDataController.open();
				users = userDataController.getUserList();
				userDataController.close();
				User user = new User();
				for (int i = 0; i < users.size(); i++) {
					user = users.get(i);

				}
				writeLogInWeb("10. before app enviro call");

				if (!InsertAppEnviroData()) {
					writeLogInWeb("19. After app enviro call");

					appEnviroDataController.open();
					ArrayList<AppEnviroData> appDataArray1 = appEnviroDataController.getAppEnviroFromDb();
					appEnviroDataController.close();
					AppEnviroData appData1 = appDataArray1.get(0);
					SharedPreferences preferences = getSharedPreferences("RETAILER_SESSION_DATA", MODE_PRIVATE);
					Editor editor = preferences.edit();
					editor.putString("CONPERID_SESSION", user.getConper_id());
					editor.putString("PDAID_SESSION", user.getPDA_Id());
					editor.putString("USER_ID", user.getUserId());
					editor.commit();
					SharedPreferences preferences1 = getSharedPreferences("MyPref", MODE_PRIVATE);
					Editor editor1 = preferences1.edit();
					editor1.putString("CONPERID_SESSION", user.getConper_id());
					editor1.putString("PDAID_SESSION", user.getPDA_Id());
					editor1.putString("USER_ID", user.getUserId());
					editor1.putString("RollType", RollType.toString());
					editor1.commit();
					writeLogInWeb("20. Preferences set and navigate to dashboard");
//					MainActivity.this.startService(new Intent(MainActivity.this,SyncData.class));
					/******************************** Ashutosh will work  *********************************************/
					/*new IntentSend(getApplicationContext(), DashBoradActivity.class).toSendAcivity();
					finish();*/
					getDynamicMenuData();
					/******************************** Ashutosh will work*********************************************/
				} else {
					writeLogInWeb("21. Oops! there is some problem in downloading app settings");
					alertOkDialog = AlertOkFinishDialog.newInstance("Oops! there is some problem in downloading app. settings\nPlease contact to administrator.");

					alertOkDialog.show(getFragmentManager(), "error");

					return;
				}


			}

		} else {
			writeLogInWeb("22. Oops! there is no user registered with this device no. " + pda_id);
			alertOkDialog = AlertOkFinishDialog.newInstance("Oops! there is no user registered with this device no. " + pda_id + "\nPlease contact to administrator.");

			alertOkDialog.show(getFragmentManager(), "error");

			return;

		}
	}

//	@Override
//	public void onBackPressed() {
//		Intent homeIntent = new Intent(Intent.ACTION_MAIN);
//		homeIntent.addCategory(Intent.CATEGORY_HOME);
//		homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(homeIntent);
//		//MainActivity.this.finish();
//	}

	public boolean InsertAppEnviroData() {
		writeLogInWeb("11. in InsertAppEnviroData method ");
		SharedPreferences dpreferences_new = getSharedPreferences("DYNAMIC_FORM_FIELDS_INFO", MODE_PRIVATE);
					Editor deditor = dpreferences_new.edit();
//					editor2.putString("Name", appData1.getPartyCaption());
//					editor2.commit();

		boolean mSuccess = false;
		ArrayList<AppEnviroData> appEnviroDataList = new ArrayList<AppEnviroData>();
		String urlJsonObj = "http://" + Constant.SERVER_WEBSERVICE_URL + "/And_Sync.asmx/JSGetEnviroSetting";
		System.out.println(urlJsonObj);
		writeLogInWeb("12. urlJsonObj= " + urlJsonObj);
		JSONParser jParser = new JSONParser();
		String result = jParser.getJSONArray(urlJsonObj);
		if (result != null) {
			try {
				JSONArray jsonarray = new JSONArray(result);
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
					//*********************************
					/************  Write By Sandeep Singh 24-03-17-  *************/
					/*********  Start  *************/
					appEnviroData.setNotificationInterval(obj.getString("NotificationInterval_Android"));
					/*********  End  *************/

					//*********************************
					appEnviroDataList.add(appEnviroData);
					ret = obj.getString("PartyCaption");
				}
				if(ret!= null){
					retailer = ret;
				}
			} catch (Exception e) {
				System.out.println(e);
				writeLogInWeb("13. Exception= " + e);
				mSuccess = true;
			}
		}
		if (appEnviroDataList.size() > 0) {

			appEnviroDataController.open();
			writeLogInWeb("14. deleteAppData call ");
			appEnviroDataController.deleteAppData();
			for (int i = 0; i < appEnviroDataList.size(); i++) {
				writeLogInWeb("15. insertAppEnviroData calling ");
				appEnviroDataController.insertAppEnviroData(appEnviroDataList.get(i));
				writeLogInWeb("16. insertAppEnviroData called ");
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
				deditor.putString("NotificationInterval", appEnviroDataList.get(i).getNotificationInterval());
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

			writeLogInWeb("17. setting global values= ");
		} catch (Exception e) {
			System.out.println(e);
			writeLogInWeb("18. Exception= " + e);
			mSuccess = true;
		}

		SharedPreferences preferences_new = getSharedPreferences("RETAILER_NAME", MODE_PRIVATE);
		Editor editor2 = preferences_new.edit();
		editor2.putString("Name", retailer);
		editor2.commit();
		return mSuccess;
	}

	public void getDynamicMenuData() {
		SharedPreferences preferences1 = getSharedPreferences("MyPref", MODE_PRIVATE);
		Solidsmid = preferences1.getString("CONPERID_SESSION","");
		//writeLogInWeb("2. insertuserdata");
		ArrayList<User> userList = new ArrayList<User>();
		String urlJsonObj = "http://" + Constant.SERVER_WEBSERVICE_URL + "/And_Sync.asmx/xJSGetDynamicMenu?Smid=" + Solidsmid ;
		System.out.println(urlJsonObj);
		//writeLogInWeb("3. urlJsonObj=" + urlJsonObj);
		JSONParser jParser = new JSONParser();
		String result = jParser.getJSONArray(urlJsonObj);
		if (result != null) {

			//InsertAppEnviroData();
			try {
				dbcon.open();
				dbcon.truncate(DatabaseConnection.TABLE_DYNAMIC_MENU);
				dbcon.close();
				JSONArray jsonarray = new JSONArray(result);

				for (int i = 0; i < jsonarray.length(); i++) {
					JSONObject obj = jsonarray.getJSONObject(i);
					/*User user = new User();
					user.setConper_id(obj.getString("ConPerId"));
					user.setDSRAllowDays(Integer.parseInt(obj.getString("DSRAllowDays")));
					RollType = obj.getString("RoleType");
					userList.add(user);*/

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

					//Blob blob = new javax.sql.rowset.serial.SerialBlob(icon);
					/*try
					{
						String value = (icon);
						byte[] buff = value.getBytes();
						Blob blob = new SerialBlob(buff);
					}
					catch (Exception e){}*/


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

					dbcon.open();
					dbcon.insert(DatabaseConnection.TABLE_DYNAMIC_MENU,values);
					dbcon.close();


				}
				new ImportData().execute("Country");
			} catch (Exception e) {
				System.out.println(e);
				writeLogInWeb("4. Exception: " + e);
			}

			/*Intent newIntent = new Intent(MainActivity.this, DashBoradActivity.class);
			newIntent.putExtra("key",1);
			startActivity(newIntent);
			finish();*/


			/*SharedPreferences dpreferences=getApplicationContext().getSharedPreferences("DYNAMIC_FORM_FIELDS_INFO",getApplicationContext().MODE_PRIVATE);
			Context ctx = getApplicationContext();
			Calendar cal = Calendar.getInstance();
			AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
			//long interval = 1000  60  6;
			long interval =6*Long.parseLong(dpreferences.getString("NotificationInterval", "0"));
			Intent serviceIntent = new Intent(ctx, NotificationService.class);
			PendingIntent servicePendingIntent =PendingIntent.getService(ctx,0,serviceIntent,PendingIntent.FLAG_CANCEL_CURRENT);
			am.setRepeating(AlarmManager.RTC_WAKEUP,cal.getTimeInMillis(),interval,servicePendingIntent);
			//startService(new Intent(this, NotificationService.class));
			// ******  END  *****
			//new IntentSend(getApplicationContext(), Dynamic_Menu.class).toSendAcivity();
			new IntentSend(getApplicationContext(), DashBoradActivity.class).toSendAcivity();
			finish();*/

		}

	}
	protected class  ImportData extends AsyncTask<String, Void, Boolean> {
		String type;
		boolean flag;
		CommonFunctions commonFunctions=new CommonFunctions(MainActivity.this);
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected Boolean doInBackground(String... arg0) {

			// TODO Auto-generated method stub
			type=arg0[0];
			try {
				if(type.equalsIgnoreCase("Country")){
					flag=commonFunctions.insertCountry();
				}
				else if(type.equalsIgnoreCase("State")){
					flag=commonFunctions.insertState();
				}
				else if(type.equalsIgnoreCase("City")){
					flag=commonFunctions.insertCityTimestampWise();
				}
				else if(type.equalsIgnoreCase("Product")){
					flag=commonFunctions.insertProductGroup();
				}
				else if(type.equalsIgnoreCase("Item")){
					flag=commonFunctions.insertItemTimestampWise();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return flag;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
//			if(type.equalsIgnoreCase("Country")){
//				new ImportData().execute("State");
//				if(result){
//					new Custom_Toast(MainActivity.this,"Try Again!Country is not saved").showCustomAlert();
//				}
//			}
//			else if(type.equalsIgnoreCase("State")){
//				new ImportData().execute("City");
//				if(result){
//					new Custom_Toast(MainActivity.this,"Try Again!State is not saved").showCustomAlert();
//				}
//			}
//			else if(type.equalsIgnoreCase("City")){
//				new ImportData().execute("Product");
//				if(result){
//					new Custom_Toast(MainActivity.this,"Try Again!City is not saved").showCustomAlert();
//				}
//			}
//			else if(type.equalsIgnoreCase("Product")){
//				new ImportData().execute("Item");
//				if(result){
//					new Custom_Toast(MainActivity.this,"Try Again!Product Group is not saved").showCustomAlert();
//				}
//			}
//			else{
				Intent newIntent = new Intent(MainActivity.this, DashBoradActivity.class);
				newIntent.putExtra("key",1);
				startActivity(newIntent);
				finish();
//			}
		}
	}

	public void writeLogInWeb(String log) {
		String status = "";
		TelephonyManager telephonyManager = null;
		try {
			telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		} catch (Exception e) {
			System.out.println(e);

		}

		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://" + Constant.SERVER_WEBSERVICE_URL + "/And_Sync.asmx/xJSInsertAppInstallationLog");
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
			nameValuePairs.add(new BasicNameValuePair("device",mDeviceNo));
			nameValuePairs.add(new BasicNameValuePair("remark", log));
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
			try {

				status = json.getString("St");
				System.out.println(status);
			} catch (Exception e) {
//				mSuccess=true;
				System.out.println(e);

			}

		} catch (Exception e) {
//					connectionDetector=new ConnectionDetector(context);
//					if(!connectionDetector.isConnectingToInternet())
//					{
//						exceptionData.setInternetExceptionData("You don't have internet connection.", "Information");
//						//exceptionData.setExceptionData(e.toString(), "ClientProtocolException InsertEnviro");
//					}
			System.out.println("Exception : " + e.getMessage());
//			mSuccess=true;
		}

	}


}

 
