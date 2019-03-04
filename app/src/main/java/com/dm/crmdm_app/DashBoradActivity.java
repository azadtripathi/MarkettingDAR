package com.dm.crmdm_app;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.controller.DsrController;
import com.dm.controller.NotificationController;
import com.dm.controller.UserDataController;
import com.dm.database.DatabaseConnection;
import com.dm.library.AlertMessage;
import com.dm.library.AlertMessage.NoticeDialogListenerWithoutView;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.Constant;
import com.dm.library.CustomArrayAdopter;
import com.dm.library.CustomArrayAdopter.HolderListener;
import com.dm.library.CustomArrayAdopter.MyHolder;
import com.dm.library.Custom_Toast;
import com.dm.library.DataTransferInterface;
import com.dm.library.DbCon;
import com.dm.library.DynamicMenuBaseAdapter;
import com.dm.library.IntentSend;
import com.dm.library.Validation;
import com.dm.model.AppData;
import com.dm.model.CRMPermission;
import com.dm.model.DashboardMenu;
import com.dm.model.Owner;
import com.dm.parser.JSONParser;
import com.dm.service.NotificationService;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

public class DashBoradActivity extends Activity implements DataTransferInterface, NoticeDialogListenerWithoutView, HolderListener {
    CustomArrayAdopter customArrayAdopter;
    ListView listView;
    View lastView = null;
    TextView itemName = null;
    AlertMessage alertMessage;
    DsrController dsrController;
    AlertOkDialog alertOkDialog;
    ConnectionDetector connectionDetector;
    SharedPreferences preferences_new, usernameSharedPref, dpreferences;
    String smid;
    TextView NoOFNotification;
    private DatabaseConnection dbHelper;
    private SQLiteDatabase database;
    DbCon dbcon;
    Activity activity;
    public static boolean tradeORpendingTittle = false;
    final ArrayList<DashboardMenu> resultss = new ArrayList<DashboardMenu>();
    Toolbar toolbar;
    AppDataController appDataController;
    public static String latitude, longitude, latlngtimestamp;
    AppData appData;
    String server;
    public static int notificationCount = 0;

    class ClearData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            SharedPreferences settings = getApplicationContext().getSharedPreferences("RETAILER_SESSION_DATA", getApplicationContext().MODE_PRIVATE);
            settings.edit().clear().commit();
            SharedPreferences settings1 = getApplicationContext().getSharedPreferences("PreferencesName", getApplicationContext().MODE_PRIVATE);
            settings1.edit().clear().commit();

            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
            finish();

            System.exit(0);
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                SharedPreferences prefAddContact = getSharedPreferences("CrmModule", MODE_PRIVATE);
                SharedPreferences.Editor editorAddContact = prefAddContact.edit();
                editorAddContact.clear().commit();
                SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear().commit();
            } catch (Exception e) {

            }
            return null;
        }
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_borad);
        displayLocationSettingsRequest();

//		TextView tv = (TextView)findViewById(R.id.text);
//		tv.setText("Dashboard");
        setTitle("Dashboard");
        activity = DashBoradActivity.this;


	/*	dsrController = new DsrController(this);
	 		dsrController.open();
			String isDataImported=dsrController.getIsDataImported();
			//	 dsrController.deleteOrderRow();
			dsrController.close();
			if (isDataImported.equals("true")) {

			}
			else {
				*//*alertOkDialog=AlertOkDialog.newInstance("There is no data available  !\n\nPlease run Import tab from Synchronization");
				alertOkDialog.show(getFragmentManager(), "syn");*//*
				(new IntentSend(getApplicationContext(),Syncronization.class)).toSendAcivity();
				return;
			}*/


        ImageView lg = (ImageView) findViewById(R.id.logout);
        TextView username = (TextView) findViewById(R.id.acvdun);
//		*********************** start Notification***********************************
		/*NotificationManager notifManager= (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
		notifManager.cancelAll();*/
        NoOFNotification = (TextView) findViewById(R.id.acvdNotification);
		/*NotificationController notificationController=new NotificationController(getApplicationContext());
		String NoOfNotification=(notificationController.getNotification());
		notificationCircleView(NoOfNotification);
		System.out.println("Notification Data"+NoOfNotification);*/
        ImageView NotificationImage = (ImageView) findViewById(R.id.nacvdNotificationImage);
        NotificationImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoOFNotification.setVisibility(View.INVISIBLE);
                DbCon dbCon = new DbCon(getApplicationContext());
                dbCon.open();
                dbCon.truncate(DatabaseConnection.NOTIFIVATION_Data_TABLE);
                //dbCon.truncate(DatabaseConnection.NOTIFIVATION_TABLE);
                dbCon.close();
                Intent i = new Intent(DashBoradActivity.this, NotificationPannel.class);
                startActivity(i);

            }
        });
//		*********************** END Notification***********************************
        lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(DashBoradActivity.this);
                builder.setMessage("Do you want to exit the application?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int aid) {


                                new ClearData().execute();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();


            }
        });


        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.dashboard);

        preferences_new = getSharedPreferences("RETAILER_NAME", MODE_PRIVATE);
        dpreferences = this.getSharedPreferences("DYNAMIC_FORM_FIELDS_INFO", Context.MODE_PRIVATE);

        usernameSharedPref = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        smid = usernameSharedPref.getString("CONPERID_SESSION", "0");
        System.out.println("SMID" + smid);
        UserDataController userDataController = new UserDataController(DashBoradActivity.this);
        userDataController.open();
        String name = userDataController.getUsername(smid);
        userDataController.close();
        username.setText("Hi," + "\n" + name);
        appDataController = new AppDataController(getApplicationContext());
        //customArrayAdopter =new CustomArrayAdopter(this,feedDahboardItemData(),R.layout.dashboard_row,R.id.itemName,DashBoradActivity.this);
        listView = (ListView) findViewById(R.id.dashboardlistview);
//		listView.setBackgroundResource(R.drawable.input_style);
        appDataController = new AppDataController(getApplicationContext());
        dbcon = new DbCon(getApplicationContext());


        database = dbcon.open();
        getMenuItem(database);
        // getDsrData(database);
        dbcon.close();
        dsrController = new DsrController(getApplicationContext());
        //listView.setAdapter(customArrayAdopter);
//		dsrController.open();
//		dsrController.deleteDsrRow();
//		dsrController.close();
//		listView.setFooterDividersEnabled(false);

        AppData appData;
        ArrayList<AppData> appDataArray = new ArrayList<>();
        appDataController = new AppDataController(this);
        appDataController.open();
        appDataArray = appDataController.getAppTypeFromDb();
        appDataController.close();
        appData = appDataArray.get(0);
        server = appData.getCompanyUrl();
        new GetCRMPermission().execute();

    }

    String SMID;

    public static ArrayList<CRMPermission> crmPermissionslist = new ArrayList<>();

    class GetCRMPermission extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SharedPreferences preferences2 = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            SMID = preferences2.getString("CONPERID_SESSION", null);
            crmPermissionslist.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                String url = "http://" + server + "/And_Sync.asmx/JSGetMobilePermission_CRM?smid=" + SMID;
                JSONParser jParser = new JSONParser();
                String response = jParser.getJSONArray(url);
                JSONArray jsonArray = new JSONArray(response);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject json = jsonArray.getJSONObject(i);
                    String actionName = json.getString("Activityname");
                    String canView = json.getString("view");
                    CRMPermission crmPermission = new CRMPermission();
                    crmPermission.setActivityName(actionName);
                    crmPermission.setPermission(canView);
                    crmPermissionslist.add(crmPermission);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }

    public void onDashboardClick(View v) {
        startActivity(new Intent(this, UserDashboard.class));
    }
//	private ArrayList<DashboardModel> feedDahboardItemData() {
//		ArrayList<AppData> appDataArray;
//		String appType="";
////	appDataController.open();
////	appDataArray=appDataController.getAppTypeFromDb();
////	 appDataController.close();
////	 for(int i=0;i<appDataArray.size();i++)
////	 {
////		 appType=appDataArray.get(i).getAppType();
////
////	 }
//		ArrayList<DashboardModel> dashboardModels=new ArrayList<DashboardModel>();
////	if(appType.equals("1")){
//
//		dashboardModels.add(new DashboardModel("DSR Entry",R.drawable.dsr_entry));
//		dashboardModels.add(new DashboardModel("Leave Request",R.drawable.leave_request));
//		dashboardModels.add(new DashboardModel("Leave Approval",R.drawable.leave_request));
//		dashboardModels.add(new DashboardModel("Beat Plan",R.drawable.beatplan));
//		dashboardModels.add(new DashboardModel("Beat Plan Approval",R.drawable.beatplan));
//		dashboardModels.add(new DashboardModel("Beat Master",R.drawable.report));
//		dashboardModels.add(new DashboardModel("Add Expense",R.drawable.report));
//		dashboardModels.add(new DashboardModel("Expense Advance Request",R.drawable.report));
//	dashboardModels.add(new DashboardModel("Tour Plan",R.drawable.report));
//		dashboardModels.add(new DashboardModel("Trade History",R.drawable.party_history));
//		dashboardModels.add(new DashboardModel("New "+preferences_new.getString("Name",""),R.drawable.add_party));
//	dashboardModels.add(new DashboardModel("Reports",R.drawable.party_history));
////	dashboardModels.add(new DashboardModel("Price List",R.drawable.product));
//		dashboardModels.add(new DashboardModel("Synchronization",R.drawable.synchronization));
////	dashboardModels.add(new DashboardModel("Sales Order",R.drawable.order));
////	dashboardModels.add(new DashboardModel("Distributor Collection",R.drawable.collection));
//		dashboardModels.add(new DashboardModel("Pending Sync",R.drawable.party_history));
//		dashboardModels.add(new DashboardModel("Utility",R.drawable.exportdb));
//		if(dpreferences.getString("Show_PartyCollection","N").equalsIgnoreCase("y")) {
//			dashboardModels.add(new DashboardModel("Generate PDF", R.drawable.generate_pdf));
//		}
////	}
////	else{
////
////		dashboardModels.add(new DashboardModel("DSR Entry",R.drawable.beat));
////		dashboardModels.add(new DashboardModel("Leave Request",R.drawable.report));
////		dashboardModels.add(new DashboardModel("Beat Plan",R.drawable.report));
////		dashboardModels.add(new DashboardModel("Trade History",R.drawable.order));
////		dashboardModels.add(new DashboardModel("New Party",R.drawable.party_new));
//////		dashboardModels.add(new DashboardModel("Reports",R.drawable.report));
//////		dashboardModels.add(new DashboardModel("Price List",R.drawable.product));
////		dashboardModels.add(new DashboardModel("Synchronization",R.drawable.sync_center));
////		//dashboardModels.add(new DashboardModel("Sales Order",R.drawable.order));
////		//dashboardModels.add(new DashboardModel("Distributor Collection",R.drawable.collection));
////		dashboardModels.add(new DashboardModel("Pending Trade History",R.drawable.order));
////		dashboardModels.add(new DashboardModel("Export DB",R.drawable.order));
////
////
////	}
//		return dashboardModels;
//	}

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_borad, menu);
        return true;
    }*/
	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		 case android.R.id.home:

		     break;
		 case R.id.sum:
			 (new IntentSend(getApplicationContext(),ReportSummary.class)).toSendAcivity();
		     break;
		default:
			break;
		}
		return super.onOptionsItemSelected(item);
		}*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button  
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(DashBoradActivity.this);
            builder.setMessage("Do you want to exit the application?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int aid) {

							/*SharedPreferences settings = getApplicationContext().getSharedPreferences("RETAILER_SESSION_DATA", getApplicationContext().MODE_PRIVATE);
							settings.edit().clear().commit();
							SharedPreferences settings1 = getApplicationContext().getSharedPreferences("PreferencesName", getApplicationContext().MODE_PRIVATE);
							settings1.edit().clear().commit();*/
                            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                            homeIntent.addCategory(Intent.CATEGORY_HOME);
                            homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(homeIntent);
                            System.exit(0);

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void setValues(ArrayList<MyHolder> al) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onDialogPositiveWithoutViewClick(DialogFragment dialog) {
        // TODO Auto-generated method stub
		/*SharedPreferences settings = getApplicationContext().getSharedPreferences("RETAILER_SESSION_DATA", getApplicationContext().MODE_PRIVATE);
		settings.edit().clear().commit();*/
        SharedPreferences settings1 = getApplicationContext().getSharedPreferences("PreferencesName", getApplicationContext().MODE_PRIVATE);
        settings1.edit().clear().commit();


        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);

    }

    @Override
    public void onDialogNegativeWithoutViewClick(DialogFragment dialog) {
        // TODO Auto-generated method stub
        alertMessage.dismiss();
    }

    @Override
    public void holderListener(MyHolder myHolder) {
        // TODO Auto-generated method stub

    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            finish();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        connectionDetector = new ConnectionDetector(DashBoradActivity.this);
        if (connectionDetector.isConnectingToInternet()) {

            new getNotification().execute();
            IntentFilter filter = new IntentFilter("Your filter");
            LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
        } else {
            AlertOkDialog dialogWithOutView;

            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }
    }

    protected class getNotification extends AsyncTask<String, String, String> {
        String result;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                AppDataController appDataController1 = new AppDataController(getApplicationContext());

                appDataController1.open();
                ArrayList<AppData> appDataArray = appDataController1.getAppTypeFromDb();
                appDataController1.close();
                appData = appDataArray.get(0);
                String response1 = null;
                String server = appData.getCompanyUrl();
                String url = "http://" + server + "/And_Sync.asmx/xJSGetNotification";
                //String url = "https://spireapiaz.pramier.com:10880/api/v1/companies/";
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(url);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(0);
                nameValuePairs.add(new BasicNameValuePair("SMID", smid));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response1 = httpclient.execute(httppost, responseHandler);
                result = response1.replace("\"", "");

				/*String url = "https://spireapiaz.pramier.com:10880/api/v1/companies/";
				String response=getDataFromUrl(url);
				System.out.println("Server Response:"+response);*/
            } catch (Exception e) {
            }

            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            JSONArray jsonArray = null;
            if (result != null) {
                if (!result.isEmpty() && (!result.equalsIgnoreCase("0"))) {
                    if (result != null) {
                        NoOFNotification.setVisibility(View.VISIBLE);
                        Validation validation = new Validation(getApplicationContext());
                        sendNotification(validation.vAlfNum(result));
                        NotificationController notificationController = new NotificationController(getApplicationContext());
                        String NoOfNotification = (notificationController.getNotification());
                        try {
                            notificationCount = Integer.parseInt(NoOfNotification);
                            if (notificationCount < 1) {
                                NoOfNotification = "0";
                            }
                        } catch (Exception e) {
                        }
                        notificationCircleView(NoOfNotification);
                    } else {
                        notificationCircleView("0");
                    }
                } else {
                    notificationCircleView("0");
                }
            } else {
                notificationCircleView("0");
            }

        }
    }

    /*String error = ""; // string field
    private String getDataFromUrl(String demoIdUrl) {
        String result = null;
        *//*String result = null;
		int resCode;
		InputStream in;
		try {
			URL url = new URL(demoIdUrl);
			URLConnection urlConn = url.openConnection();

			HttpsURLConnection httpsConn = (HttpsURLConnection) urlConn;
			httpsConn.setAllowUserInteraction(false);
			httpsConn.setInstanceFollowRedirects(true);
			httpsConn.setRequestMethod("GET");
			httpsConn.connect();
			resCode = httpsConn.getResponseCode();

			if (resCode == HttpURLConnection.HTTP_OK) {
				in = httpsConn.getInputStream();

				BufferedReader reader = new BufferedReader(new InputStreamReader(
						in, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line).append("\n");
				}
				in.close();
				result = sb.toString();
			} else {
				error += resCode;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		*//*
		// Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
		CertificateFactory cf = null;
		try {
			cf = CertificateFactory.getInstance("X.509");
		} catch (CertificateException e) {
			e.printStackTrace();
		}
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
		InputStream caInput = null;
		try {
			caInput = new BufferedInputStream(new FileInputStream("load-der.crt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Certificate ca = null;
		try {
			ca = cf.generateCertificate(caInput);
			System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
		} catch (CertificateException e) {
			e.printStackTrace();
		} finally {
			try {
				caInput.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

// Create a KeyStore containing our trusted CAs
		String keyStoreType = KeyStore.getDefaultType();
		KeyStore keyStore = null;
		try {
			keyStore = KeyStore.getInstance(keyStoreType);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		try {
			keyStore.load(null, null);
			keyStore.setCertificateEntry("ca", ca);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}


// Create a TrustManager that trusts the CAs in our KeyStore
		String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory tmf = null;
		try {
			tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			tmf.init(keyStore);
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}

// Create an SSLContext that uses our TrustManager
		SSLContext context = null;
		try {
			context = SSLContext.getInstance("TLS");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			context.init(null, tmf.getTrustManagers(), null);
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}

// Tell the URLConnection to use a SocketFactory from our SSLContext
		URL url = null;
		try {
			url = new URL(demoIdUrl);
			HttpsURLConnection urlConnection =
					(HttpsURLConnection)url.openConnection();
			urlConnection.setSSLSocketFactory(context.getSocketFactory());
			InputStream in = urlConnection.getInputStream();
			BufferedReader r = new BufferedReader(new InputStreamReader(in));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				total.append(line).append('\n');
			}
			result=total.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		return result;
	}*/
    public void sendNotification(String messageBody) {
        Long tsLong = System.currentTimeMillis() / 1000;
        ContentValues values = new ContentValues();
        values.put(DatabaseConnection.COLUMN_NOTIFICATION, messageBody);
        values.put(DatabaseConnection.COLUMN_DATE, tsLong.toString());
        DbCon dbCon = new DbCon(this);
        dbCon.open();
        dbCon.truncate(DatabaseConnection.NOTIFIVATION_TABLE);
        dbCon.insert(DatabaseConnection.NOTIFIVATION_TABLE, values);
        dbCon.close();
        //End

		/*Intent intent = new Intent(this, NotificationPannel.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
				PendingIntent.FLAG_ONE_SHOT);

		Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.crm_dm1);
		NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
				.setSmallIcon(R.drawable.crm_dm_small)
				.setLargeIcon(largeIcon)

				.setContentText("You have "+messageBody+" Unread notification")
				.setAutoCancel(true)
				.setSound(defaultSoundUri)
				.setContentIntent(pendingIntent);

		NotificationManager notificationManager =
				(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(0, notificationBuilder.build());*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void notificationCircleView(String NoOfNotification) {

        if (NoOfNotification.length() < 1 && (!NoOfNotification.equalsIgnoreCase("0"))) {
            NoOFNotification.setVisibility(View.VISIBLE);
            NoOFNotification.setWidth(5);
            NoOFNotification.setHeight(5);
            NoOFNotification.setText(NoOfNotification);

        } else if (NoOfNotification.length() < 2 && (!NoOfNotification.equalsIgnoreCase("0"))) {
            NoOFNotification.setVisibility(View.VISIBLE);
            NoOFNotification.setWidth(5);
            NoOFNotification.setHeight(5);
            NoOFNotification.setText(NoOfNotification);

        } else if (NoOfNotification.length() < 3 && (!NoOfNotification.equalsIgnoreCase("0"))) {
            NoOFNotification.setVisibility(View.VISIBLE);
            NoOFNotification.setWidth(10);
            NoOFNotification.setHeight(10);
            NoOFNotification.setText(NoOfNotification);
        } else if (NoOfNotification.length() < 4 && (!NoOfNotification.equalsIgnoreCase("0"))) {
            NoOFNotification.setVisibility(View.VISIBLE);
            NoOFNotification.setWidth(20);
            NoOFNotification.setHeight(20);
            NoOFNotification.setText(NoOfNotification);
        } else if (NoOfNotification.equalsIgnoreCase("0")) {
            NoOFNotification.setVisibility(View.INVISIBLE);
        }
    }

    private void getMenuItem(SQLiteDatabase database) {
        String mResult = "";
        String mParent = "";

        /*String qry = "select parent.display_name as parent_name," +
                "child.display_name as child_name," +
                "child.Page_NAme,child.View_p,child.Add_p,child.Edit_p,child.Delete_p from dynamicmenu parent left join dynamicmenu child " +
                "on parent.page_id= child.parent_id where child.level_idx=2 and parent.form_filter='Main' and parent.display_name<>'DSR' order by parent.idx,child.idx";*/
        String qry = "select parent.display_name as parent_name," +
                "child.display_name as child_name," +
                "child.Page_NAme,child.View_p,child.Add_p,child.Edit_p,child.Delete_p,child.icon from dynamicmenu parent left join dynamicmenu child " +
                "on parent.page_id= child.parent_id where child.level_idx=2 and parent.form_filter='Main' order by parent.idx,child.idx";
        Cursor cursor = database.rawQuery(qry, null);
        try {
            if (cursor.moveToFirst()) {
                while (!(cursor.isAfterLast())) {

                    mResult = cursor.getString(0);
                    if (!mParent.equalsIgnoreCase(mResult)) {
                        DashboardMenu mDashboardMenu = new DashboardMenu();
                        mDashboardMenu.setparent_name(cursor.getString(0));
                        mDashboardMenu.setParentMenu("1");
                        resultss.add(mDashboardMenu);
                        mParent = cursor.getString(0);
                    }
                    DashboardMenu mDashboardMenu = new DashboardMenu();
                    //mDashboardMenu.setPage_id(cursor.getString(0));
                    mDashboardMenu.setparent_name(cursor.getString(0));
                    mDashboardMenu.setchild_name(cursor.getString(1));
                    mDashboardMenu.setPage_NAme(cursor.getString(2));
                    mDashboardMenu.setIcon(cursor.getString(7));
                    mDashboardMenu.setView_p(cursor.getString(3));
                    mDashboardMenu.setAdd_p(cursor.getString(4));
                    mDashboardMenu.setEdit_p(cursor.getString(5));
                    mDashboardMenu.setDelete_p(cursor.getString(6));
                    mDashboardMenu.setParentMenu("0");
                    resultss.add(mDashboardMenu);
                    cursor.moveToNext();
                }
            } else {
                System.out.println("No records found");
            }
            cursor.close();
        } catch (Exception E) {
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                listView.setAdapter(new DynamicMenuBaseAdapter(getApplicationContext(), resultss, R.layout.custom_row_view, activity));
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String itemNameText = (resultss.get(position).getPage_NAme() == null ? "" : resultss.get(position).getPage_NAme().trim());
                if (itemNameText.equals("DailySalesReport")) {
                    dsrController.open();
                    String isDataImported = dsrController.getIsDataImported();
                    //	 dsrController.deleteOrderRow();
                    dsrController.close();
//					if (isDataImported.equals("true")) {
//						(new IntentSend(getApplicationContext(),DailySalesReport.class)).toSendAcivity();
////						finish();
//					}
//					else
//					{
//						alertOkDialog= AlertOkDialog.newInstance("There is no data available  !\n\nPlease run Import tab from Synchronization");
//						alertOkDialog.show(getFragmentManager(), "syn");
//					}
                } else if (itemNameText.equals("Order Fullfillment")) {
                    //	(new IntentSend(getApplicationContext(),OrderMarkingActivity.class)).toSendAcivity();
//					(new IntentSend(getApplicationContext(),TargetActivity.class)).toSendAcivity();
//						finish();

                }
//				 else if(itemNameText.equals("New Party"))
                else if (itemNameText.equals("NewParty")) {
                    dsrController.open();
                    String isDataImported = dsrController.getIsDataImported();
                    dsrController.close();

//					if (isDataImported.equals("true"))
//					{
//						connectionDetector=new ConnectionDetector(DashBoradActivity.this);
//
//							Bundle bundle=new Bundle();
//							bundle.putString("mode","Add");
//							bundle.putString("comeFrom","DashBoradActivity");
//							Intent intent=new Intent(getApplicationContext(),NewParty.class);
//							intent.putExtras(bundle);
//							startActivity(intent);
//
//					}
//					else {
//						alertOkDialog= AlertOkDialog.newInstance("There is no data available  !\n\nPlease run Import tab from Synchronization");
//						alertOkDialog.show(getFragmentManager(), "syn");
//					}
                } else if (itemNameText.equals("LeaveRequest")) {
                    (new IntentSend(getApplicationContext(), LeaveRequest.class)).toSendAcivity();
                } else if (itemNameText.equals("LeaveApproval")) {
                    (new IntentSend(getApplicationContext(), LeaveApproval.class)).toSendAcivity();
                } else if (itemNameText.equals("BeatPlanEntry")) {
                    //(new IntentSend(getApplicationContext(),BeatPlanEntry.class)).toSendAcivity();
                } else if (itemNameText.equals("BeatAproval")) {
                    //(new IntentSend(getApplicationContext(),BeatAproval.class)).toSendAcivity();
                } else if (itemNameText.equals("TourApproval")) {
                    (new IntentSend(getApplicationContext(), TourApproval.class)).toSendAcivity();
                } else if (itemNameText.equals("BeatMasterEntry")) {
                    //	(new IntentSend(getApplicationContext(),BeatMasterEntry.class)).toSendAcivity();
                } else if (itemNameText.equals("AddExpense")) {
                    (new IntentSend(getApplicationContext(), AddExpense.class)).toSendAcivity();
                } else if (itemNameText.equals("AdvanceExpenseRequest")) {
                    Intent i = new Intent(DashBoradActivity.this, AdvanceExpenseRequest.class);
                    i.putExtra("ID", "0");
                    i.putExtra("FromFind", false);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else if (itemNameText.equals("OrderManagement")) {
                    tradeORpendingTittle = false;
                    //(new IntentSend(getApplicationContext(),OrderManagement.class)).toSendAcivity();
                } else if (itemNameText.equals("TourPlan")) {
                    (new IntentSend(getApplicationContext(), TourPlan.class)).toSendAcivity();
                } else if (itemNameText.equals("OrderManagementPending")) {
                    tradeORpendingTittle = true;
                    //(new IntentSend(getApplicationContext(),OrderManagementPending.class)).toSendAcivity();
                } else if (itemNameText.equals("ReportSummary")) {
                    (new IntentSend(getApplicationContext(), ReportSummary.class)).toSendAcivity();
                } else if (itemNameText.equals("AutoPurchaseOrder")) {
                    dsrController.open();
                    String isDataImported = dsrController.getIsDataImported();
                    //	 dsrController.deleteOrderRow();
                    dsrController.close();
                    if (isDataImported.equals("true")) {

                        //	(new IntentSend(getApplicationContext(),AutoPurchaseOrder.class)).toSendAcivity();
                    } else {
                        alertOkDialog = AlertOkDialog.newInstance("There is no data available  !\n\nPlease run Import tab from Synchronization");
                        alertOkDialog.show(getFragmentManager(), "syn");

                    }
                } else if (itemNameText.equals("Syncronization")) {
//					Intent newIntent = new Intent(DashBoradActivity.this, SynchronisationActivity.class);
//					newIntent.putExtra("key",2);
//					startActivity(newIntent);
                } else if (itemNameText.equals("AddCollection")) {
                    dsrController.open();
                    String isDataImported = dsrController.getIsDataImported();
                    //	 dsrController.deleteOrderRow();
                    dsrController.close();
                    if (isDataImported.equals("true")) {
                        //(new IntentSend(getApplicationContext(),AddCollection.class)).toSendAcivity();
                    } else {
                        alertOkDialog = AlertOkDialog.newInstance("There is no data available  !\n\nPlease run Import tab from Synchronization");
                        alertOkDialog.show(getFragmentManager(), "syn");

                    }

                } else if (itemNameText.equals("Utility")) {
//					 new ExportDBTo(DashBoradActivity.this).exportDatabaseToPhoneStorage();
                    (new IntentSend(getApplicationContext(), Utility.class)).toSendAcivity();

                } else if (itemNameText.equals("GeneratePdf")) {
                    (new IntentSend(getApplicationContext(), GeneratePdf.class)).toSendAcivity();
                } else if (itemNameText.equals("ComplaintEntry")) {
                    //(new IntentSend(getApplicationContext(),ComplaintEntry.class)).toSendAcivity();
                } else if (itemNameText.equals("SuggestionEntry")) {
                    //(new IntentSend(getApplicationContext(),SuggestionEntry.class)).toSendAcivity();
                } else if (itemNameText.equals("Collateral")) {
                    (new IntentSend(getApplicationContext(), GridViewActivity.class)).toSendAcivity();
                } else if (itemNameText.equals("AddContact")) {
//                    (new IntentSend(getApplicationContext(), AddContact.class)).toSendAcivity();
                    (new IntentSend(getApplicationContext(), AddNewContactPage.class)).toSendAcivity();
                } else if (itemNameText.equals("CRMstream")) {
                    (new IntentSend(getApplicationContext(), CRMstream.class)).toSendAcivity();
                } else if (itemNameText.equals("ChatUserActivity")) {
                    //(new IntentSend(getApplicationContext(),ChatUserActivity.class)).toSendAcivity();
//					(new IntentSend(getApplicationContext(),UserDashboard.class)).toSendAcivity();
                } else if (itemNameText.equals("Attendence")) {
                    (new IntentSend(getApplicationContext(), Attendence.class)).toSendAcivity();
                } else if (itemNameText.equals("PaymentReceived")) {
                    (new IntentSend(getApplicationContext(), PaymentReceived.class)).toSendAcivity();
                } else if (itemNameText.equals("PaymentTransfer")) {
                    (new IntentSend(getApplicationContext(), PaymentTransfer.class)).toSendAcivity();
                } else if (itemNameText.equals("PaymentLedger")) {
                    (new IntentSend(getApplicationContext(), PaymentLedger.class)).toSendAcivity();
                } else {
                }

            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
//                        startLocationUpdates();
                    try {


                        NewLocationService track = new NewLocationService(this);
                        if (track.canGetLocation) {
                            latitude = track.getLatitude();
                            longitude = track.getLongitude();
                            latlngtimestamp = track.getLatLngTime();
                        } else {
                            latlngtimestamp = track.getLatLngTime();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    break;
                case Activity.RESULT_CANCELED:
                    try {
                        displayLocationSettingsRequest();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                    break;
            }
        }

    }

    int REQUEST_CHECK_SETTINGS = 2;

    GoogleApiClient googleApiClient;

    private void displayLocationSettingsRequest() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API).build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        //**************************
        builder.setAlwaysShow(true); //this is the key ingredient
        //**************************

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:

                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                            //Toast.makeText(GetLocationActivity.this,""+e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                        Log.i("TAG", "All location settings are satisfied.");

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(DashBoradActivity.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                        Log.i(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                        break;
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }


    public void setupPreveiousAddressData() {
        SharedPreferences pref = getSharedPreferences("AddressData", MODE_PRIVATE);
        if (pref.contains("isAdded")) {
            // do nothing
        } else {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isAdded", true);
            editor.putInt("cStateId", 0);
            editor.putInt("cCityId", 0);
            editor.putInt("stateId", 0);
            editor.putInt("cityId", 0);
            editor.commit();
        }
    }
}
