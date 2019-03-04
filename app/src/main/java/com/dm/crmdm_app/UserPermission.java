package com.dm.crmdm_app;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.dm.controller.DeviceInfoController;
import com.dm.library.ConnectionDetector;
import com.dm.module.Login;

public class UserPermission extends Activity {
    ConnectionDetector connectionDetector;
    SharedPreferences preferences1;
    private static int REQUEST_READ_PHONE_STATE = 1;
    String t = "";
    ProgressDialog progressDialog;
    String mDeviceNo;
	int SPLASH_TIME_OUT=1000;
    boolean flag=false;
	DeviceInfoController deviceInfoController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        connectionDetector = new ConnectionDetector(this);
        preferences1 = getSharedPreferences("ENVIRO_SESSION_DATA", Context.MODE_PRIVATE);
        mDeviceNo=preferences1.getString("DeviceId", "N");
		deviceInfoController=new DeviceInfoController(getApplicationContext());
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				if (preferences1.getString("DeviceId", "N").equalsIgnoreCase("N")) {
					getImeiNo();

				} else {
					Intent i = new Intent(UserPermission.this, Login.class);
					startActivity(i);
					finish();
				}
			}
		}, SPLASH_TIME_OUT);

    }
	void getImeiNo() {

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			int permissionCheck = ContextCompat.checkSelfPermission(UserPermission.this, Manifest.permission.READ_PHONE_STATE);
			int s = PackageManager.PERMISSION_GRANTED;
			System.out.print(s);
			if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
				SharedPreferences.Editor editor1 = preferences1.edit();
				editor1.putString("DeviceId", "N");
				editor1.commit();
				//new Custom_Toast(UserPermission.this,"Try Again! Permission Required.").showCustomAlert();
				ActivityCompat.requestPermissions(UserPermission.this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CLEAR_APP_CACHE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.INTERNET}, REQUEST_READ_PHONE_STATE);

			} else {
				try {
					System.out.print(s);
					TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					String r = telephonyManager.getDeviceId();
					callingActivity(r);

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

			}
		} else {
			//TODO

			TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String r = telephonyManager.getDeviceId();
			deviceInfoController.open();
			deviceInfoController.deletedeviceRow();
			deviceInfoController.insertDeviceInfo(r);
			deviceInfoController.close();
				callingActivity(r);
		}
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
		if (requestCode == REQUEST_READ_PHONE_STATE) {
			if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
				//TODO
				try {
					TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
					t = telephonyManager.getDeviceId();
					System.out.println(t);
					deviceInfoController.open();
					deviceInfoController.deletedeviceRow();
					deviceInfoController.insertDeviceInfo(t);
					deviceInfoController.close();
					callingActivity(t);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			else{
				//new Custom_Toast(UserPermission.this,"Try Again! Permission Required.").showCustomAlert();
				ActivityCompat.requestPermissions(UserPermission.this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CLEAR_APP_CACHE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.INTERNET}, REQUEST_READ_PHONE_STATE);

			}


		} else {
			Toast.makeText(UserPermission.this,"Unable to get Device Information\n" +
					"Please grant permission", Toast.LENGTH_SHORT).show();
		}
	}
	void callingActivity(String id){
		if (connectionDetector.isConnectingToInternet()) {
			SharedPreferences.Editor editor1 = preferences1.edit();
			editor1.putString("DeviceId", id);
			editor1.commit();
			if (!preferences1.getString("DeviceId", "N").equalsIgnoreCase("N")) {
				Intent i = new Intent(UserPermission.this, Login.class);
				startActivity(i);
				finish();
			}
			else{
				ActivityCompat.requestPermissions(UserPermission.this, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.INTERNET, Manifest.permission.CLEAR_APP_CACHE,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.RECEIVE_BOOT_COMPLETED,Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.INTERNET}, REQUEST_READ_PHONE_STATE);
			}
		} else {
			Toast.makeText(UserPermission.this,"You don't have internet connection", Toast.LENGTH_SHORT).show();
		}
	}

}

