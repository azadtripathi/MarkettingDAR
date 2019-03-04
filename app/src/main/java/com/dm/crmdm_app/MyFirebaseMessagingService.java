package com.dm.crmdm_app;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by dataman on 6/12/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "MyFirebaseMsgService";
    String Tsmid,Fsmid,FName,Tname,title,body, click_Action;
    long timestamp;
    public static final String INTENT_FILTER = "INTENT_FILTER";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d("Msg", "Message received [" + remoteMessage + "]");

        timestamp = System.currentTimeMillis();
        String getBodyLocalizationKey = remoteMessage.getNotification().getBodyLocalizationKey();
        //new whennotificationoccur().execute();

        /* Intent intent1 = new Intent(INTENT_FILTER);
        sendBroadcast(intent1);*/

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

            if (/* Check if data needs to be processed by long running job */ true) {
                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                //scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow();
            }

        }

        if (remoteMessage.getNotification() != null) {
            Log.d("FCM", "Notification Message Body: " + remoteMessage.getNotification().getBody());
            Tsmid =  remoteMessage.getNotification().getColor();
            /*try {
            JSONObject objs = new JSONObject(remoteMessage.getNotification().getBody());
            Tsmid = objs.getString("Tsmid").toString();
            Fsmid = objs.getString("Fsmid").toString();
            FName = objs.getString("FName").toString();
            Tname = objs.getString("TName").toString();
            title = objs.getString("title").toString();
            body = objs.getString("Message").toString();
            click_Action = remoteMessage.getNotification().getClickAction();

        } catch(JSONException e){
            e.printStackTrace();
        }*/

        if(UserDetails.chatWith.equals(Tsmid)) {
               if (UserDetails.chatActivity_open.equals("yes")) {
                Log.d("sender", "Broadcasting message");
                Intent intent = new Intent("chat_refresh");
                intent.putExtra("message", remoteMessage.getNotification().getBody());
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
               }
               else {
                   Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
                   Intent intent = new Intent("mainActivity_refresh");
                   intent.putExtra("message", remoteMessage.getNotification().getBody());
                   LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                   click_Action = remoteMessage.getNotification().getClickAction();
                   title = remoteMessage.getNotification().getTitle();
                   sendNotification(remoteMessage.getNotification().getBody());
               }
        }
        else {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Intent intent = new Intent("mainActivity_refresh");
            intent.putExtra("message", remoteMessage.getNotification().getBody());
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            click_Action = remoteMessage.getNotification().getClickAction();
            title = remoteMessage.getNotification().getTitle();
            sendNotification(remoteMessage.getNotification().getBody());
        }
     }
    }

    /*private void scheduleJob() {
        // [START dispatch_job]
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(this));
        Job myJob = dispatcher.newJobBuilder()
                //setService(MyJobService.class)
                .setTag("my-job-tag")
                .build();
        dispatcher.schedule(myJob);
        // [END dispatch_job]
    }*/

    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }
    private void sendNotification(String messageBody) {
        Intent intent = new Intent(click_Action);
        //Intent intent = new Intent(this,ChatActivity.class);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
       /* PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//**//* Request code *//**//*, intent,
                PendingIntent.FLAG_ONE_SHOT);*/
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        //stackBuilder.addParentStack(ChatActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(intent);
        PendingIntent pendingIntent   = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.crmdm_action)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
