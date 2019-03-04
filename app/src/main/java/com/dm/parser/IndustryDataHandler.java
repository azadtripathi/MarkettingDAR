package com.dm.parser;

import android.util.Log;

import com.dm.model.Industry;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class IndustryDataHandler extends DefaultHandler{
	private ArrayList<Industry> industryList = new ArrayList<Industry>();
	private Industry industry;

	private boolean isInIndustry = false;
	private boolean isInId = false;
	private boolean isInDescription = false;
	private boolean isInIndustryType = false;
	private boolean isInSyncId = false;
	private boolean isInActive = false;
	private boolean isInCreatedDate = false;
	StringBuilder textContent = new StringBuilder();
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
		textContent.setLength(0);
	   if(name.equalsIgnoreCase("Industry")){
		   isInIndustry = true;		   
		   industry = new Industry();
	   }
	   if(name.equalsIgnoreCase("Id")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("Description")){
		   isInDescription = true;
	   }
	   if(name.equalsIgnoreCase("IndustryType")){
		   isInIndustryType = true;
	   }
	   if(name.equalsIgnoreCase("SyncId")){
		   isInSyncId = true;
	   }
	   if(name.equalsIgnoreCase("Active")){
		   isInActive = true;
	   }
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   }
	   
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("industry")){
			isInIndustry = false;
			industryList.add(industry);
			//writeLog(industry.toString());
		}
		if(name.equalsIgnoreCase("Id")){
			isInId = false;
		}
		if(name.equalsIgnoreCase("Description")){
			isInDescription = false;
		}
		if(name.equalsIgnoreCase("IndustryType")){
			   isInIndustryType = false;
		   }
		if(name.equalsIgnoreCase("SyncId")){
			   isInSyncId = false;
		   }
		   if(name.equalsIgnoreCase("Active")){
			   isInActive = false;
		   }
		   if(name.equalsIgnoreCase("createddate")){
			   isInCreatedDate = false;
		   }
	}

	//element content
	public void characters (char ch[], int start, int length)
	{
//		writeLog("ch111="+ch.toString());
//		writeLog("start="+start);
//		writeLog("length="+length);
//		String chars = new String(ch, start, length); 
//	    chars = chars.trim();
//	    
//	   writeLog("ch="+chars.toString());
		//writeLog("ch="+ch);
		textContent.append(ch, start, length);
//		String chars = new String(ch, start, length); 
		String chars=textContent.toString();
		//writeLog("start="+start);
		//writeLog("length="+length);
		//writeLog("chars="+chars);
		chars = chars.trim();
	    if(isInId){
	    	industry.setIndustry_id((chars.toString()));
	    }else if(isInDescription){
	    	industry.setIndustry_name(chars.toString());
	    }
	    else if(isInSyncId){
	    	industry.setSync_id(chars.toString());
	    }	
	    else if(isInActive){
	    	industry.setActive(chars.toString());
	    } 
	    else if(isInCreatedDate){
	    	industry.setCreatedDate(chars.toString());
	    } 	
	    	
	}
	
	public ArrayList<Industry> getData(){
		return this.industryList;
	}
	
//	public void writeLog(String msg) {
//	 Calendar calendar1=Calendar.getInstance();
//	  try{
//		  File myFile = new File("/sdcard/myDmCRMfile.txt");
//	       //myFile = new File(Environment.getDataDirectory()+"/fftlog/mysdfile.txt");
//	       if(!myFile.exists())
//	       {
//	   	   myFile.createNewFile();
//	       }
//	   	   FileOutputStream fOut = new FileOutputStream(myFile,true);
//	   	   OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
////	   	   myOutWriter.append(msg+"  "+dateFormat.format(calendar1.getTime())+" \n");
//	   	 myOutWriter.append(msg+" \n");
//	   	   myOutWriter.close();
//	   	   fOut.close();
//			} 
//	     catch (IOException e) {
//	    	// writeLog("IO exception");
//					e.printStackTrace();
//					}
//}
	
}