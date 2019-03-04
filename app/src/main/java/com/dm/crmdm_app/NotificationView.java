package com.dm.crmdm_app;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

public class NotificationView extends Activity {
	String title;
	String text;
	TextView txttitle;
	TextView txttext;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notificationview);
		NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notificationmanager.cancel(0);
		Intent i = getIntent();
		title = i.getStringExtra("title");
		text = i.getStringExtra("Particulars");
		txttitle = (TextView) findViewById(R.id.title);
		txttext = (TextView) findViewById(R.id.text);
		txttext.setTextColor(Color.parseColor("#2a2a2a"));
		txttitle.setTextColor(Color.parseColor("#2a2a2a"));
		System.err.println(title+text);
		txttitle.setText(title);
		txttext.setText(text);
	}
}