package com.dm.crmdm_app;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.controller.OrderController;
import com.dm.controller.PartyController;
import com.dm.controller.Porder1Controller;
import com.dm.controller.PorderController;
import com.dm.controller.UserDataController;
import com.dm.library.IntentSend;

public class UserDashboard extends ActionBarActivity {


    TextView pQtyLabel,pAmtLabel,sQtyLabel,sAmtLabel,productiveCall,failVisitLabel, totalNewPartyLabel;
    OrderController orderController;
    PartyController partyController;
    PorderController porderController;
    Porder1Controller porder1Controller;
    SimpleDateFormat dateFormat;
    Calendar calendar;

    String p_amt,p_qty,s_amt,s_qty,total_party,total_productive_call, totalFailedVisit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        dateFormat=new java.text.SimpleDateFormat("dd/MMM/yyyy");
        calendar = Calendar.getInstance();
        porderController = new PorderController(this);
        UserDataController userDataController = new UserDataController(this);
        userDataController.open();
        SharedPreferences preferences2=getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String name=userDataController.getUsername(preferences2.getString("CONPERID_SESSION", ""));
        userDataController.close();
        porderController.open();

        p_amt = porderController.sumOfAmount(dateFormat.format(calendar.getTime()));
        p_qty = porderController.sumOfQty(dateFormat.format(calendar.getTime()));
        s_amt = porderController.sumOfSAmount(dateFormat.format(calendar.getTime()));
        s_qty = porderController.sumOfSQty(dateFormat.format(calendar.getTime()));
        total_productive_call = porderController.numberOfRowsInOrder(dateFormat.format(calendar.getTime()));
        totalFailedVisit = porderController.totalFailedVisit(dateFormat.format(calendar.getTime()));

        total_party = porderController.totalPartyCreated(dateFormat.format(calendar.getTime()),name);
        porderController.close();
        orderController = new OrderController(this);
        partyController = new PartyController(this);

        totalNewPartyLabel = (TextView)findViewById(R.id.totalNewPartyLabel);

        pQtyLabel = (TextView)findViewById(R.id.pQTYLabel);
        pAmtLabel = (TextView)findViewById(R.id.pAmtLabel);
        sQtyLabel = (TextView)findViewById(R.id.sQtyLabel);
        sAmtLabel = (TextView)findViewById(R.id.sAmtLabel);
        productiveCall = (TextView)findViewById(R.id.pCallLabel) ;
        failVisitLabel = (TextView)findViewById(R.id.failedVisitCallLabel);

        productiveCall.setText("Productive Calls: "+ total_productive_call);
        failVisitLabel.setText("Failed Visit: "+totalFailedVisit);

        pQtyLabel.setText("Primary Qty: "+p_qty);
        pAmtLabel.setText("Primary Amt: "+p_amt);

        sQtyLabel.setText("Secondary Qty: "+s_qty);
        sAmtLabel.setText("Secondary Amt: "+s_amt);
        totalNewPartyLabel.setText("Total Parties: "+total_party);


        productiveCall = (TextView)findViewById(R.id.pCallLabel) ;
        failVisitLabel = (TextView)findViewById(R.id.failedVisitCallLabel);

        totalNewPartyLabel = (TextView)findViewById(R.id.newPartyLabel);

        getSupportActionBar().setCustomView(R.layout.actionbar_custom_view_home);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageView iv = (ImageView) findViewById(R.id.image);
        iv.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                (new IntentSend(getApplicationContext(), DashBoradActivity.class)).toSendAcivity();
                finish();
            }
        });
        final TextView tv2 = (TextView) findViewById(R.id.text);
        tv2.setText("Dashboard");










    }
}
