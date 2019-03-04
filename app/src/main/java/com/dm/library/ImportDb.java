package com.dm.library;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class ImportDb {
	Context context;private ProgressDialog progressDialog;AlertOkDialog alertOkDialog;
	public ImportDb(Context context) {
		this.context=context;
}
	public void importDatabaseFromPhoneStorage() {
//		new ImportDbTo().execute();
		
		try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();
                if (sd.canWrite()) {
                String currentDBPath = "//data//com.dm.crmdm_app//databases//datamancrm_mobile_db.db";
                String backupDBPath = "datamancrm_mobile_db.db";
                File backupDB = new File(data, currentDBPath);
                File currentDB = new File(sd, backupDBPath);

            FileChannel src = new FileInputStream(currentDB).getChannel();
            FileChannel dst = new FileOutputStream(backupDB).getChannel();
            dst.transferFrom(src, 0, src.size());
            src.close();
            dst.close();
            Toast.makeText(context, "Import Successful!",
                    Toast.LENGTH_SHORT).show();

        }
    } catch (Exception e) {

    	System.out.println("exception in importing"+ e);
        Toast.makeText(context, "Import Failed!"+e, Toast.LENGTH_SHORT)
                .show();

    }
	}
	
}