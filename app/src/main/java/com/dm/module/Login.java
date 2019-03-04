package com.dm.module;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.dm.crmdm_app.NewMainActivity;
import com.dm.crmdm_app.R;
import com.dm.crmdm_app.SplashScreen;
import com.dm.library.Constant;
import com.dm.library.Custom_Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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

import java.io.Console;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static com.dm.library.Constant.WEBSERVICE_URL_LICENSE;

public class Login  extends AppCompatActivity
{

    EditText emailText,passwdText;
    String email,passwd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailText = (EditText)findViewById(R.id.emailText);
        passwdText = (EditText)findViewById(R.id.passwdText);

        SharedPreferences pref = getSharedPreferences("LoginData",MODE_PRIVATE);
        if(pref.getBoolean("isLogged",false)){
            startActivity(new Intent(this, NewMainActivity.class));
            finish();
            return;
        }

    }

    public void onLogin(View b)
    {/*startActivity(new Intent(Login.this, NewMainActivity.class));*/
        if(emailText.getText().toString().isEmpty())
        {

            return;
        }
        if(emailText.getText().toString().isEmpty())
        {

            return;
        }
        email = emailText.getText().toString();
        passwd = passwdText.getText().toString();
        new sessionData().execute();

    }



    ProgressDialog progressDialog;
    class sessionData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Login.this, "Validating User", "Loading...Please wait", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            String response1 = "";
            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(Constant.SERVER_WEBSERVICE_URL + Constant.JSGetuser);
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
                nameValuePairs.add(new BasicNameValuePair("Email", email));
                nameValuePairs.add(new BasicNameValuePair("Password", passwd));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                response1 = httpclient.execute(httppost, responseHandler);
                Log.d("res",response1);
            } catch (UnknownHostException un) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

            } catch (ClientProtocolException e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                // new Custom_Toast(SplashScreen.this,"Try Again After Some Time! Server Under Maintanace").showCustomAlert();
            } catch (IOException e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }


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
            Toast.makeText(Login.this,result,Toast.LENGTH_LONG).show();
            if (result != null) {
                try {
//                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = new JSONArray(result);
                    SharedPreferences pref = getSharedPreferences("LoginData",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    JSONObject json = jsonArray.getJSONObject(0);
                    editor.putBoolean("isLogged",true);
                    editor.putString("LoginId",json.getString("Id"));
                    editor.putString("LoginName",json.getString("EmployeeName"));
                    editor.putString("HqCode",json.getString("HQ_Code"));
                    editor.putString("HQName",json.getString("HQ_Name"));
                    editor.commit();
                    startActivity(new Intent(Login.this, NewMainActivity.class));
                    finish();


                   // if (jsonObject.getString("Grahak").equalsIgnoreCase("True")) {
//                        linearLayoutOTP.setVisibility(View.VISIBLE);
//                        SharedPreferences.Editor editor = preferences.edit();
//                        editor.putString("OTPSession", jsonObject.getString("Msz"));
//                        editor.putString("GrahakActivation", jsonObject.getString("Grahak"));
//                        editor.putString("PhoneNo", PhoneNo);
//                        editor.commit();
              //      } else {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashScreen.this);
//                        builder.setTitle(preferences.getString("PhoneNo", PhoneNo));
//                        builder.setMessage("This mobile no. is not registered. Please contact to Admin or press cancel to enter registered mobile no.");
//                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int aid) {
//                                finish();
//                                Intent intent = new Intent(SplashScreen.this, SplashScreen.class);
//                                startActivity(intent);
//                            }
//                        });
//                        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                linearLayout.setVisibility(View.VISIBLE);
//                                dialog.cancel();
//                            }
//                        });
//                        AlertDialog alert = builder.create();
//                        alert.show();
           //         }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {

                new Custom_Toast(Login.this, "Please Contact Admin! Server is not Responding").showCustomAlert();
            }
        }
    }
}
