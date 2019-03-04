package com.dm.crmdm_app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.dm.SmsListener;
import com.dm.crmdm_app.SplashScreen;

/**
 * Created by Administrator on 6/7/2017.
 */
public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();
        System.out.println("SMS"+data);
        Object[] pdus = (Object[]) data.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //You must check here if the sender is your provider and not another one with same text.
            SharedPreferences preferences = context.getSharedPreferences("ENVIRO_SESSION_DATA", Context.MODE_PRIVATE);
            try{
            if (SplashScreen.closeActivity) {
                String messageBody = smsMessage.getMessageBody();
                String phoneNumber = smsMessage.getDisplayOriginatingAddress();
                if (phoneNumber.contains("DATMAN")) {
                    try {
                        mListener.messageReceived(messageBody);
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
