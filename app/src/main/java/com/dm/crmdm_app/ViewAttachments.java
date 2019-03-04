package com.dm.crmdm_app;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.library.Custom_Toast;
import com.dm.library.DealsAttachment;
import com.dm.library.IntentSend;
import com.dm.model.MileStoneData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewAttachments extends AppCompatActivity {


    ListView listView;
    TextView tv;
    String dealId = "";
    AppDataController appDataController;
    String server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attachments);
        listView = (ListView) findViewById(R.id.listView);
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
        tv.setText("Attachments");

        String imgObj = getIntent().getExtras().getString("imgData");

        showAttachments(imgObj);
    }

    ArrayList<MileStoneData>mileStoneDatas = new ArrayList<>();
    private void showAttachments(String imgObj)
    {
        try
        {
            JSONArray jsonArray = new JSONArray(imgObj);
            if(jsonArray.length()>0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String fileName = jsonObject.getString("filename");
                    String filePath = jsonObject.getString("filepath");
                    String imageIcon = jsonObject.getString("IconUrl");
                    MileStoneData mileStoneData = new MileStoneData();
                    mileStoneData.setFileName(fileName);
                    mileStoneData.setFilePath(filePath);
                    mileStoneData.setImageIcon(imageIcon);
                    mileStoneDatas.add(mileStoneData);

                }


                listView.setAdapter(new DealsAttachment(this, mileStoneDatas));
            }
            else
            {
                new Custom_Toast(this,"No attachment").showCustomAlert();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}