package com.dm.parser;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.dm.library.Constant;
import com.dm.model.Enviro;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class RecieptDataParser {
	BufferedReader in;
	StringBuffer sb;
	String page;
	String conper,server,min_date;
	String parsedData="";
	 File myFile;
	 FileOutputStream fOut;
 	 OutputStreamWriter myOutWriter;
 	SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
		SharedPreferences preferences2;
		public RecieptDataParser(Context context)
		{
	preferences2=context.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
	conper=preferences2.getString("CONPERID_SESSION", null);
	server= Constant.SERVER_WEBSERVICE_URL;
		}
	public String gettingReciep_No()
	{
	 try {
   	     
	        HttpClient client = new DefaultHttpClient();
	        String url="http://"+server+"/And_Sync.asmx/GetReceiptNo?userId="+conper+"&minDate=0";
	        HttpGet request = new HttpGet(url);
	        HttpResponse response = client.execute(request);
	          in = new BufferedReader
	        (new InputStreamReader(response.getEntity().getContent()));
	          sb = new StringBuffer("");
	        String line = "";
	        String NL = System.getProperty("line.separator");
	        while ((line = in.readLine()) != null) {
	            sb.append(line + NL);
	        }
	        in.close();
	         page = sb.toString();
	        //Toast.makeText(context, page, Toast.LENGTH_LONG).show();
	        System.out.println(page);
	        
	        } catch (ClientProtocolException e) {
	            // TODO Auto-generated catch block
	      e.printStackTrace();
	    } catch (IOException e1) {
	      // TODO Auto-generated catch bloc
	      e1.printStackTrace();
	    } finally {
	          if (in != null) {
	              try {
	                  in.close();
	                  } catch (IOException e) {
	                  e.printStackTrace();
	              }
	          }
	    }
	
	 
     try{
         SAXParserFactory spf = SAXParserFactory.newInstance();
         SAXParser sp = spf.newSAXParser();
         XMLReader xr = sp.getXMLReader();
         RecieptData mastDataHandler = new RecieptData();
         xr.setContentHandler(mastDataHandler);
         xr.parse(new InputSource(new ByteArrayInputStream(page.getBytes("utf-8"))));

         ArrayList<Enviro> itemsList = mastDataHandler.getRecieptData();
         for(int i=0;i<itemsList.size();i++){
        	 Enviro item = itemsList.get(i);
            // parsedData = parsedData + "----->\n";
             parsedData=parsedData + item.getReciep_no();
             writeLog("parsedData"+ parsedData);
         }

     }catch(ParserConfigurationException ex1) {
         Log.e("SAX XML", "sax parse error", ex1);
     } catch(SAXException ex11) {
     } catch(IOException ex) {
         Log.e("SAX XML", "sax parse io error", ex);
     }
	 

return parsedData;
}
	public void writeLog(String msg) {
		 Calendar calendar1=Calendar.getInstance();
		  try{
			  File myFile = new File("/sdcard/myjetparserfile.txt");
		     
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
		    	// writeLog("IO exception");
						e.printStackTrace();
						}
	}
	
}
