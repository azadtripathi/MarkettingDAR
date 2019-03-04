package com.dm.parser;

import android.util.Log;

import com.dm.model.FailedVisit;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class FailedVisitDataHandler extends DefaultHandler{
	private ArrayList<FailedVisit> failedVisitList = new ArrayList<FailedVisit>();
	private FailedVisit failedVisit;

	private boolean isInFailedVisit = false;
	private boolean isInFVId = false;
	private boolean isInVisId = false;
	private boolean isInFVDocId = false;
	private boolean isInVDate = false;
	private boolean isInUserId = false;
	private boolean isInSMID = false;
	private boolean isInPartyId = false;
	private boolean isInDistId = false;
	private boolean isInremarks = false;
	private boolean isInNextVisitDate = false;
	private boolean isInNextVisit = false;
	private boolean isInVisitTime = false;
	private boolean isInAndroidId = false;
	private boolean isInreasonId = false;
	private boolean isInCreatedDate = false;
	
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("FailedVisit")){
		   isInFailedVisit = true;		   
		   failedVisit = new FailedVisit();
	   }
	   if(name.equalsIgnoreCase("FVId")){
		   isInFVId = true;
	   }
	   if(name.equalsIgnoreCase("VisId")){
		   isInVisId = true;
	   }
	   if(name.equalsIgnoreCase("FVDocId")){
		   isInFVDocId = true;
	   }
	   if(name.equalsIgnoreCase("VDate")){
		   isInVDate = true;
	   }if(name.equalsIgnoreCase("UserId")){
		   isInUserId = true;
	   }
	   if(name.equalsIgnoreCase("PartyId")){
		   isInPartyId = true;
	   }
	  
	   if(name.equalsIgnoreCase("DistId")){
		   isInDistId = true;
	   }
	   if(name.equalsIgnoreCase("remarks")){
		   isInremarks = true;
	   }
	   if(name.equalsIgnoreCase("NextVisitDate")){
		   isInNextVisitDate = true;
	   }
	   if(name.equalsIgnoreCase("NextVisit")){
		   isInNextVisit = true;
	   }
	   if(name.equalsIgnoreCase("VisitTime")){
		   isInVisitTime = true;
	   } 
	   if(name.equalsIgnoreCase("reasonId")){
		   isInreasonId = true;
	   } 
	   if(name.equalsIgnoreCase("Android_Id")){
		   isInAndroidId = true;
	   }  
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   }
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("FailedVisit")){
			isInFailedVisit = false;
			failedVisitList.add(failedVisit);
		}
		if(name.equalsIgnoreCase("FVId")){
			   isInFVId = false;
		   }
		   if(name.equalsIgnoreCase("VisId")){
			   isInVisId = false;
		   }
		   if(name.equalsIgnoreCase("FVDocId")){
			   isInFVDocId = false;
		   }
		   if(name.equalsIgnoreCase("VDate")){
			   isInVDate = false;
		   }if(name.equalsIgnoreCase("UserId")){
			   isInUserId = false;
		   }
		   if(name.equalsIgnoreCase("PartyId")){
			   isInPartyId = false;
		   }
		  
		   if(name.equalsIgnoreCase("DistId")){
			   isInDistId = false;
		   }
		   if(name.equalsIgnoreCase("remarks")){
			   isInremarks = false;
		   }
		   if(name.equalsIgnoreCase("NextVisitDate")){
			   isInNextVisitDate = false;
		   }
		   if(name.equalsIgnoreCase("NextVisit")){
			   isInNextVisit = false;
		   }
		   if(name.equalsIgnoreCase("VisitTime")){
			   isInVisitTime = false;
		   } 
		   if(name.equalsIgnoreCase("reasonId")){
			   isInreasonId = false;
		   } 
		   if(name.equalsIgnoreCase("Android_Id")){
			   isInAndroidId = false;
		   }  
		   if(name.equalsIgnoreCase("createddate")){
			   isInCreatedDate = false;
		   }
		   
	}

	//element content
	public void characters (char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInFVId){
	    	failedVisit.setFVId(chars.toString());
	    }
	    else if(isInVisId){
	    	failedVisit.setVisId(chars.toString());
	    }
	    else if(isInVDate){
	    	failedVisit.setVDate(chars.toString());
	    }
	    else if(isInUserId){
	    	failedVisit.setUserID(chars.toString());
	    }
	    else if(isInPartyId){
	    	failedVisit.setPartyId(chars.toString());
	    } 
	    else if(isInSMID){
	    	failedVisit.setSMId(chars.toString());
	    } 		
	    else if(isInDistId){
	    	failedVisit.setDistId(chars.toString());
	    } 
	    else if(isInremarks){
	    	failedVisit.setRemarks(chars.toString());
	    } 
	    else if(isInNextVisitDate){
	    	failedVisit.setNextvisit(chars.toString());
	    } 
	    else if(isInVisitTime){
	    	failedVisit.setVtime(chars.toString());
	    } 
	    else if(isInreasonId){
	    	failedVisit.setReasonID(chars.toString());
	    } 
	    else if(isInFVDocId){
	    	failedVisit.setFVDocId(chars.toString());
	    }
	    else if(isInAndroidId){
	    	failedVisit.setAndroidId(chars.toString());
	    } 
	    else if(isInCreatedDate){
	    	failedVisit.setCreatedDate(chars.toString());
	    }  
	}
	
	public ArrayList<FailedVisit> getData(){
		return this.failedVisitList;
	}


	
	
}
