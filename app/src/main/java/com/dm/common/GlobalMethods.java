package com.dm.common;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.dm.crmdm_app.BuildConfig;

import org.apache.http.NameValuePair;

import java.io.UnsupportedEncodingException;
import java.util.List;


public class GlobalMethods {
    private static final String APP_PREF = "APP_PREF";

    /**
     * Method to print on console
     *
     * @param message message to print
     */
    public static void printLog(String message) {
        if (BuildConfig.DEBUG) {
            System.out.println(message);
        }
    }

    /**
     * Method to check is network connected
     *
     * @param activity Activity
     * @return true if connected else false
     */
    public static boolean isConnected(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    /**
     * Method to hide soft keyboard
     *
     * @param activity activity
     */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Method to show toast in center
     *
     * @param activity activity
     * @param msg      message to show
     */
    public static void showCustomToastInCenter(Activity activity, String msg) {
        Toast toast = Toast.makeText(activity, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * Method to validate email
     *
     * @param target email address
     * @return true if valid email else false
     */
    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isEmptySetError(Activity activity, EditText editText, String errorMsg) {
        GlobalMethods.hideKeyboard(activity);
        if (TextUtils.isEmpty(editText.getText())) {
            editText.setError(errorMsg);
            editText.requestFocus();
            return false;
        } else
            return true;

    }



    public static String getQuery(List<NameValuePair> params)
            throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            result.append(pair.getName());
            result.append("=");
            result.append(pair.getValue());
        }

        return result.toString();
    }

    private static SharedPreferences getPrefrences(Activity activity) {
        return activity.getSharedPreferences(APP_PREF, Context.MODE_PRIVATE);
    }

    public static void savePreference(Activity activity, String key, String value) {
        getPrefrences(activity).edit().putString(key, value).commit();
    }

    public static String getPreference(Activity activity, String key, String value) {
        return getPrefrences(activity).getString(key, value);
    }

    public static void clearPref(Activity activity){
        getPrefrences(activity).edit().clear().commit();
    }
}
