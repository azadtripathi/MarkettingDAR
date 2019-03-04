package com.dm.library;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;

import com.dm.database.DatabaseConnection;
import com.dm.util.DataConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class ExportDBTo {
Context context;private ProgressDialog progressDialog;AlertOkDialog alertOkDialog;
	public ExportDBTo(Context context) {
		this.context=context;
}
public void exportDatabaseToPhoneStorage() {
	new ExportDb().execute();
}
class ExportDb extends AsyncTask<String, String, String>
{
@Override
protected void onPreExecute() {
	// TODO Auto-generated method stub
	super.onPreExecute();
	progressDialog= ProgressDialog.show(context,"Location", "Getting Location Please wait...", true);
}
@Override
	protected void onPostExecute(String result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		progressDialog.dismiss();
		alertOkDialog= AlertOkDialog.newInstance("Database saved on Internal Storage");
		alertOkDialog.show(((Activity)context).getFragmentManager(), "d");
	}
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
	        File sd = Environment.getExternalStorageDirectory();
	        File data = Environment.getDataDirectory();

	        if (sd.canWrite()) {
	            String currentDBPath = "//data//com.dm.crmdm_app//databases//"+ DatabaseConnection.DATABASE_NAME;//datamancrm_mobile_db.db";
	            String backupDBPath = DatabaseConnection.DATABASE_NAME;
	            File currentDB = new File(data, currentDBPath);
	            File backupDB = new File(sd, backupDBPath);

	            if (currentDB.exists()) {
	                FileChannel src = new FileInputStream(currentDB).getChannel();
	                FileChannel dst = new FileOutputStream(backupDB).getChannel();
	                dst.transferFrom(src, 0, src.size());
	                src.close();
	                dst.close();
	            }
	        }
	    } catch (Exception e) {
	    	progressDialog.dismiss();
	    }
		return null;
	}}
}
