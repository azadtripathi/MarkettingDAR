package com.dm.library;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.dm.crmdm_app.NotificationView;
import com.dm.crmdm_app.R;

public class GetNotification {
	Context context;private static final int myNotificationId = 1;
	CharSequence msg;
	String subject,title;
	public GetNotification(Context context,String msg,String subject,String title) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.msg=msg;
		this.subject=subject;
		this.title=title;
	}
	public void ShowNotification() {
		RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
				R.layout.custom_notification);
		Intent intent = new Intent(context, NotificationView.class);
		intent.putExtra("title", subject);
		intent.putExtra("Particulars", msg);
		PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
				.setSmallIcon(R.drawable.crmdm_action)
				.setTicker(title)
				.setAutoCancel(true)
				.setContentIntent(pIntent)
				.setContent(remoteViews);
		remoteViews.setImageViewResource(R.id.image, R.drawable.crmdm_action);
		//remoteViews.setImageViewResource(R.id.imagenotiright,R.drawable.logo_noti_ic);
		
		// Locate and set the Text into customnotificationtext.xml TextViews
		remoteViews.setTextViewText(R.id.title,subject);
		remoteViews.setTextViewText(R.id.text,"Click here to view more details !");
		
		// Create Notification Manager
		NotificationManager notificationmanager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// Build Notification with Notification Manager

		notificationmanager.notify(0, builder.build());

	}
}
