package com.dm.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dm.library.ConnectionDetector;

/**
 * Created by Administrator on 4/22/2017.
 */
public class NetworkChangeReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
        String status = ConnectionDetector.getConnectivityStatusString(context);

//        Intent serviceIntent = new Intent(context, NotificationService.class);
//        context.startService(serviceIntent);
    }
}
