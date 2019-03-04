package com.dm.crmdm_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.library.DbCon;
import com.dm.library.IntentSend;
import com.dm.library.MileStoneAdapter;
import com.dm.model.AppData;
import com.dm.model.MileStoneData;
import com.dm.parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewMileStoneActivity extends ActionBarActivity {


    ListView listView;
    TextView tv;
    String dealId = "";
    AppDataController appDataController;
    String server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_mile_stone);

        listView = (ListView)findViewById(R.id.listView);
        comperAmtLabel = (TextView)findViewById(R.id.discount_comm_percent) ;
        comperLabel = (TextView)findViewById(R.id.discountAmt) ;


        getSupportActionBar().setCustomView(R.layout.actionbar_custom_view_home);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageView iv = (ImageView) findViewById(R.id.image);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new IntentSend(getApplicationContext(), DashBoradActivity.class)).toSendAcivity();
                finish();
            }
        });
        tv = (TextView) findViewById(R.id.text);
        tv.setText("Payment Terms");
        dealId = getIntent().getExtras().getString("dealId");
        AppData appData;
        ArrayList<AppData> appDataArray = new ArrayList<>();
        appDataController = new AppDataController(this);
        appDataController.open();
        appDataArray = appDataController.getAppTypeFromDb();
        appDataController.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();
        new getDealsMilstone().execute();
    }



    ArrayList<MileStoneData> mileStoneDatas =new ArrayList<>();
    JSONArray mImageArray = new JSONArray();
    ProgressDialog progressDialog;
    String imgObj = "";
    String comperVal="",comperAmt="";
    TextView comperLabel,comperAmtLabel;
    class getDealsMilstone extends AsyncTask<Void, Void, Void>
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ViewMileStoneActivity.this);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try
            {
                mileStoneDatas.clear();
                String url ="http://"+server+"/And_Sync.asmx/XjsDealDetailData_CRM?TaskDealId="+dealId;
                JSONParser jParser = new JSONParser();
                String response = jParser.getJSONArray(url);
                JSONArray jsonArray = new JSONArray(response);
                JSONObject json = jsonArray.getJSONObject(0);
                JSONArray milearray = new JSONArray(json.getString("Dmilestones"));
//                JSONObject jsonObject = jsonArray.getJSONObject(1);
                imgObj = json.getString("ImageData");
                JSONArray jsonArray1 = new JSONArray(imgObj);
                int n = jsonArray1.length();
                mImageArray = jsonArray1;
                for(int i=0;i< milearray.length();i++)
                {

                    JSONObject mjson = milearray.getJSONObject(i);
                    MileStoneData mdata = new MileStoneData();
                    mdata.setAmount(mjson.getString("amount"));
                    mdata.setCommamount(json.getString("totamt"));
                    mdata.setCommper(json.getString("commper"));
                    comperAmt = json.getString("totamt");
                    comperVal=json.getString("commper");
                    mdata.setMilstone(mjson.getString("milestone"));
                    mdata.setRate(mjson.getString("rate"));
                    mdata.setQty(mjson.getString("qty"));
                    mileStoneDatas.add(mdata);


                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            listView.setAdapter(new MileStoneAdapter(ViewMileStoneActivity.this,mileStoneDatas));
            comperAmtLabel.setText("Commision(%):  "+comperVal);
            comperLabel.setText("Total Amount: "+comperAmt);

        }
    }
    private void showMilestones(String dealId)
    {

    }

    public void viewAttachments(View v)
    {
        Intent newIntent = new Intent(this, ViewAttachments.class);
        newIntent.putExtra("imgData",imgObj);
        newIntent.putExtra("dealId",dealId);
        startActivity(newIntent);
    }
}
