package com.dm.util;

import android.content.Context;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FftApp implements Thread.UncaughtExceptionHandler {
    private final Context myContext;File myFile;
    private final String LINE_SEPARATOR = "\n";
    FileOutputStream fOut;SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    OutputStreamWriter myOutWriter;
    public FftApp(Context context) {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);
       writeLog("Uncaugth exception***="+errorReport.toString());
//        myContext.startService(new Intent(myContext,SyncData.class));
        this.uncaughtException(thread, exception);
    }
    public void writeLog(String msg) {
		 Calendar calendar1=Calendar.getInstance();
		  try{
			  myFile =new File("/sdcard/DmCRMapp.txt");
		      if(!myFile.exists())
		       {
		   	   myFile.createNewFile();
		       }
		   	   fOut = new FileOutputStream(myFile,true);
		   	   myOutWriter = new OutputStreamWriter(fOut);
		   	   myOutWriter.append(msg+"  "+dateFormat.format(calendar1.getTime())+" \n");
		   	   myOutWriter.close();
		   	   fOut.close();
				}
		     catch (IOException e) {
		    	 writeLog("IO exception");
						e.printStackTrace();
						}
	}
}