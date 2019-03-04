package com.dm.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dm.crmdm_app.MainActivity;

import java.io.File;

/**
 * Created by Dataman on 1/14/2017.
 */
public class ClearAppData  {
    Context context;private ProgressDialog progressDialog;AlertOkFinishDialog alertOkDialog;
    public ClearAppData(Context context) {
        this.context=context;
    }

    public void clearAllDataStorage() {
        new ClearData().execute();
    }
    class ClearData extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(context,"Clean", "Cleaning Data Please wait...", true);
        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
//            alertOkDialog=AlertOkFinishDialog.newInstance("Data clear successfully");
//            alertOkDialog.show(((Activity)context).getFragmentManager(), "d");
              Toast.makeText(context,"Data clear successfully",Toast.LENGTH_LONG).show();
              (new IntentSend(context,MainActivity.class)).toSendAcivity();
        }
        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                clearApplicationData();

            } catch (Exception e) {
                progressDialog.dismiss();
            }
            return null;
        }}


    public void clearApplicationData() {
        File cache = this.context.getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    deleteDir(new File(appDir, s));

                }
            }
        }
    }
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

}