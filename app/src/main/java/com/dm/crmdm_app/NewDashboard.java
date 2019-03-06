package com.dm.crmdm_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class NewDashboard extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_dashboard);
        setHeader();
// configure the SlidingMenu
        findViewById(R.id.img_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  SlidingMenu menu = new SlidingMenu(NewDashboard.this);
                menu.setMode(SlidingMenu.LEFT);
                menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                menu.setShadowWidthRes(R.dimen.shadow_width);
                menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
                menu.setFadeDegree(0.35f);
                menu.attachToActivity(NewDashboard.this, SlidingMenu.SLIDING_CONTENT);
                menu.setMenu(R.layout.menu);*/
            }
        });

    }

    private void setHeader() {
        findViewById(R.id.img_home).setVisibility(View.GONE);
       TextView headerText= findViewById(R.id.header_text);
       headerText.setText("Dashboard");
    }

    public void onAddContact(View v)
    {
        startActivity(new Intent(this,AddNewContactPage.class));

    }
}
