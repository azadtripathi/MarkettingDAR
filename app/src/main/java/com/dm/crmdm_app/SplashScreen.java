package com.dm.crmdm_app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.SmsListener;
import com.dm.library.ConnectionDetector;
import com.dm.library.Constant;
import com.dm.library.Custom_Toast;
import com.dm.library.Validation;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by Administrator on 5/26/2017.
 */
public class SplashScreen extends Activity {
    private static final String WEBSERVICE_URL_LICENSE = "http://license.dataman.in/jsonwebservice.asmx/";
    LinearLayout linearLayout, linearLayoutOTP;
    ProgressDialog progressDialog;
    long SPLASH_TIME_OUT = 1000;
    SharedPreferences preferences;
    Button submit, buttonOTP;
    EditText editTextMobNo, editTextOTP;
    String mDeviceNo = "", PhoneNo, stringOTP;
    ConnectionDetector connectionDetector;
    TextView Resend;
    public static boolean closeActivity = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        preferences = getApplicationContext().getSharedPreferences("ENVIRO_SESSION_DATA", MODE_PRIVATE);
        connectionDetector = new ConnectionDetector(SplashScreen.this);
        if (preferences.getString("DeviceId", "N").equalsIgnoreCase("N")) {
            Intent i = new Intent(SplashScreen.this, UserPermission.class);
            startActivity(i);
            finish();
        } else {
            if (isTimeAutomatic()) {
                if (connectionDetector.isConnectingToInternet()) {
                    mDeviceNo = preferences.getString("DeviceId", "N");
                    linearLayout = (LinearLayout) findViewById(R.id.linearLayoutPhone);
                    linearLayoutOTP = (LinearLayout) findViewById(R.id.linearLayoutOTP);
                    submit = (Button) findViewById(R.id.asSubmit);
                    editTextMobNo = (EditText) findViewById(R.id.asPhoneNo);
                    buttonOTP = (Button) findViewById(R.id.asbutonOTP);
                    editTextOTP = (EditText) findViewById(R.id.asOTP);
                    Resend = (TextView) findViewById(R.id.asResend);
                    if (preferences.getBoolean("SessionFlag", false)) {
                        startActivity(new Intent(SplashScreen.this, MainActivity.class));
                        finish();
                    } else {
                        new Handler().postDelayed(new Runnable() {

                            @Override
                            public void run() {
                                if (preferences.getString("OTPSession", "N").equalsIgnoreCase("N")) {
                                    linearLayout.setVisibility(View.VISIBLE);
                                    linearLayoutOTP.setVisibility(View.GONE);
                                } else {
                                    linearLayout.setVisibility(View.GONE);
                                    linearLayoutOTP.setVisibility(View.VISIBLE);
                                }
                            }
                        }, SPLASH_TIME_OUT);
                    }
                    submit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (connectionDetector.isConnectingToInternet()) {
                                PhoneNo = null;
                                Validation validation = new Validation(SplashScreen.this);
                                PhoneNo = validation.vAlfNum(editTextMobNo.getText().toString());
                                if (PhoneNo.length() >= 0 && PhoneNo.length() < 10) {
                                    new Custom_Toast(SplashScreen.this, Constant.MOBILE_NUMBER_ERROR).showCustomAlert();
                                } else {
                                    linearLayout.setVisibility(View.GONE);
                                    new sessionData().execute();
                                }
                            } else {
                                new Custom_Toast(SplashScreen.this, Constant.INTERNET_CONNECTION_ERROR).showCustomAlert();
                            }

                        }
                    });
                    Resend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (connectionDetector.isConnectingToInternet()) {
                                // new sessionData().execute();
                                AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                                builder.setTitle(preferences.getString("PhoneNo", PhoneNo));
                                builder.setMessage("Are you sure?");
                                builder.setNegativeButton("Edit", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int aid) {
                                        SharedPreferences.Editor editor = null;
                                        SharedPreferences preferences = getSharedPreferences("ENVIRO_SESSION_DATA", Context.MODE_PRIVATE);
                                        editor = preferences.edit();
                                        editor.putString("OTPSession", "N");
                                        editor.commit();
                                        linearLayout.setVisibility(View.VISIBLE);
                                        linearLayoutOTP.setVisibility(View.GONE);
                                    }
                                });
                                builder.setPositiveButton("Resend", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //buttonOTP.setEnabled(true);
                                        new sessionData().execute();
                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                new Custom_Toast(SplashScreen.this, "Try Again! No Internet Connection").showCustomAlert();
                            }
                        }
                    });
                    buttonOTP.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (connectionDetector.isConnectingToInternet()) {
                                // if (preferences.getString("OTPSession", "N").equalsIgnoreCase("N")) {
                                stringOTP = null;
                                Validation validation = new Validation(SplashScreen.this);
                                stringOTP = validation.vAlfNum(editTextOTP.getText().toString());
                                if (stringOTP.length() == 0) {
                                    editTextOTP.setError("Invalid OTP");
                                } else {
                                    new validateUser().execute();
                                }
                            } else {
                                new Custom_Toast(SplashScreen.this, "Try Again! No Internet Connection").showCustomAlert();
                            }

                            // }
                        }
                    });
                }

            } else {
                showSettingAlert();
            }
        }
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                try {
                    if (messageText != null) {
                        try {
                            //   if (preferences.getString("OTPSession", "N").equalsIgnoreCase("N")) {
                            int parts = messageText.indexOf(".");
                            String otp = messageText.substring(parts - 7, parts - 1);
                            otp = otp.trim();
                            System.out.println("Msg:" + messageText + "-" + parts + "-" + otp);
                            if (editTextOTP != null) {
                                editTextOTP.setText(otp.trim());
                                if (connectionDetector.isConnectingToInternet()) {
                                    stringOTP = null;
                                    Validation validation = new Validation(SplashScreen.this);
                                    stringOTP = validation.vAlfNum(editTextOTP.getText().toString());
                                    if (stringOTP.length() == 0) {
                                        editTextOTP.setError("Invalid OTP");
                                    } else {
                                        new validateUser().execute();
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public boolean isTimeAutomatic() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME, 0) == 1;
        } else {
            return Settings.System.getInt(getContentResolver(), Settings.System.AUTO_TIME, 0) == 1;
        }
    }

    public void showSettingAlert() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("It seems you have disabled auto time update! Please update your setting");
        alert.setPositiveButton("Open Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_DATE_SETTINGS), 0);
            }
        });

        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alert.create().show();
    }

    class validateUser extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SplashScreen.this, "Verifying User", "Loading...Please wait", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String result = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(WEBSERVICE_URL_LICENSE + "JGetValidateOtpForGrahak");
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("Otp", stringOTP));
                nameValuePairs.add(new BasicNameValuePair("device", mDeviceNo));
                nameValuePairs.add(new BasicNameValuePair("mobile", preferences.getString("PhoneNo", PhoneNo)));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                final String response1 = httpclient.execute(httppost, responseHandler);
                try {
                    Validation validation = new Validation(SplashScreen.this);
                    result = validation.vAlfNum(response1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            System.out.println("Result OTP:" + result);
            if (result != null) {
                result = result.replaceAll("\"", "");
                if (result.equalsIgnoreCase("Y")) {
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("SessionFlag", true);
                    editor.commit();
                    startActivity(new Intent(SplashScreen.this, MainActivity.class));
                    finish();
                    //displayLocationSettingsRequest();
                } else if (result.equalsIgnoreCase("N")) {
                    new Custom_Toast(SplashScreen.this, "Enter Correct OTP").showCustomAlert();
                } else if (result.equalsIgnoreCase("Your Otp Is Expired")) {
                    new Custom_Toast(SplashScreen.this, "Resend! Your Otp Is Expired").showCustomAlert();
                    SharedPreferences.Editor editor = null;
                    SharedPreferences preferences = getSharedPreferences("ENVIRO_SESSION_DATA", Context.MODE_PRIVATE);
                    editor = preferences.edit();
                    editor.putString("OTPSession", "N");
                    editor.commit();
                } else {
                    new Custom_Toast(SplashScreen.this, "Contact Admin!" + result).showCustomAlert();
                    SharedPreferences.Editor editor = null;
                    SharedPreferences preferences = getSharedPreferences("ENVIRO_SESSION_DATA", Context.MODE_PRIVATE);
                    editor = preferences.edit();
                    editor.putString("OTPSession", "N");
                    editor.commit();
                }
            } else {
                new Custom_Toast(SplashScreen.this, "Contact Admin!" + result).showCustomAlert();
                SharedPreferences.Editor editor = null;
                SharedPreferences preferences = getSharedPreferences("ENVIRO_SESSION_DATA", Context.MODE_PRIVATE);
                editor = preferences.edit();
                editor.putString("OTPSession", "N");
                editor.commit();
            }

        }
    }

    class sessionData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SplashScreen.this, "Validating User", "Loading...Please wait", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String response1 = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(WEBSERVICE_URL_LICENSE + "JGetValidUserfrommobileForGrahak");
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("Mobile", PhoneNo));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response1 = httpclient.execute(httppost, responseHandler);
            } catch (UnknownHostException un) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                runOnUiThread("Try Again! Check Your Connection");
            } catch (ClientProtocolException e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                runOnUiThread("Try Again After Some Time! Server Under Maintanace");
                // new Custom_Toast(SplashScreen.this,"Try Again After Some Time! Server Under Maintanace").showCustomAlert();
            } catch (IOException e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                runOnUiThread("Contact Admin! Problem While Connecting To Server");
                // new Custom_Toast(SplashScreen.this,"Try Again! Check Your Connection.").showCustomAlert();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                e.printStackTrace();
            }
            return response1;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            System.out.println("Result Mobile:" + result);
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getString("Grahak").equalsIgnoreCase("True")) {
                        linearLayoutOTP.setVisibility(View.VISIBLE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("OTPSession", jsonObject.getString("Msz"));
                        editor.putString("GrahakActivation", jsonObject.getString("Grahak"));
                        editor.putString("PhoneNo", PhoneNo);
                        editor.commit();
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
                        builder.setTitle(preferences.getString("PhoneNo", PhoneNo));
                        builder.setMessage("This mobile no. is not registered. Please contact to Admin or press cancel to enter registered mobile no.");
                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int aid) {
                                finish();
                                Intent intent = new Intent(SplashScreen.this, SplashScreen.class);
                                startActivity(intent);
                            }
                        });
                        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                linearLayout.setVisibility(View.VISIBLE);
                                dialog.cancel();
                            }
                        });
                        AlertDialog alert = builder.create();
                        alert.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                linearLayout.setVisibility(View.VISIBLE);
                new Custom_Toast(SplashScreen.this, "Please Contact Admin! Server is not Responding").showCustomAlert();
            }
        }
    }

    private void runOnUiThread(final String msg) {
        new Thread() {
            public void run() {
                try {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            new Custom_Toast(SplashScreen.this, msg).showCustomAlert();
                        }
                    });
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();

    }

    public void settingValues() {
        SharedPreferences.Editor editor = null;
        SharedPreferences preferences = getSharedPreferences("ENVIRO_SESSION_DATA", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("OTPSession", "N");
        editor.commit();
        Intent intent = new Intent(SplashScreen.this, SplashScreen.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onPause() {
        super.onPause();
        closeActivity = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        closeActivity = true;
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (preferences.getString("OTPSession", "N").equalsIgnoreCase("N")) {
            finish();
            System.exit(0);
        } else {
            settingValues();
        }

    }

    @Override
    public void onDestroy() {
        closeActivity = false;
        super.onDestroy();
        SharedPreferences.Editor editor = null;
        SharedPreferences preferences = getSharedPreferences("ENVIRO_SESSION_DATA", Context.MODE_PRIVATE);
        editor = preferences.edit();
        editor.putString("OTPSession", "N");
        editor.commit();
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (!isTimeAutomatic()) {
                showSettingAlert();
            }

        }

    }
}
