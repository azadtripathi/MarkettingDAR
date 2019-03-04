package com.dm.library;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectionDetector {
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    private Context _context;
     
    public ConnectionDetector(Context context){
        this._context = context;
    }
    ConnectivityManager connectivity;
    public boolean isConnectingToInternet(){

        for (int k = 0; k < 10; k++) {
                 connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null)
                    for (int i = 0; i < info.length; i++)
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
          return false;
    }
    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }
    public static String getConnectivityStatusString(Context context) {
        int conn = ConnectionDetector.getConnectivityStatus(context);
        String status = null;
        if (conn == ConnectionDetector.TYPE_WIFI) {
            status = "Wifi enabled";
        } else if (conn == ConnectionDetector.TYPE_MOBILE) {
            status = "Mobile data enabled";
        } else if (conn == ConnectionDetector.TYPE_NOT_CONNECTED) {
            status = "Not connected to Internet";
        }
        return status;
    }}