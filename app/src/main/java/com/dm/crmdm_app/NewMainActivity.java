package com.dm.crmdm_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.dm.module.Login;

public class NewMainActivity extends AppCompatActivity
{
    String logId;
    SharedPreferences preferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


       preferences =  getSharedPreferences("LoginData",MODE_PRIVATE);
       logId = preferences.getString("LoginId","");
        new ImportData().execute("Country");
       //change
        Intent newIntent = new Intent(NewMainActivity.this, NewDashboard.class);
        newIntent.putExtra("key",1);
        startActivity(newIntent);
        finish();
        //
    }


    protected class  ImportData extends AsyncTask<String, Void, Boolean> {
        String type;
        boolean flag;
        CommonFunctions commonFunctions=new CommonFunctions(NewMainActivity.this);
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

                    flag=commonFunctions.insertCityTimestampWise(logId);

                    flag=commonFunctions.insertItemTimestampWise();
                    flag=commonFunctions.insertIndustry();
                    flag=commonFunctions.insertSubIndustry();
                    flag=commonFunctions.insertEmployee();
                    flag=commonFunctions.insertDesignation();
                    flag=commonFunctions.insertStatusData();

//                }
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
            Intent newIntent = new Intent(NewMainActivity.this, NewDashboard.class);
            newIntent.putExtra("key",1);
            startActivity(newIntent);
            finish();
//			}
        }
    }
}
