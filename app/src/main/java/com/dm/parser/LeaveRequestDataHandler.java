package com.dm.parser;

import android.util.Log;

import com.dm.model.TransLeaveRequest;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class LeaveRequestDataHandler extends DefaultHandler{
	private ArrayList<TransLeaveRequest> transLeaveRequestList = new ArrayList<TransLeaveRequest>();
	private TransLeaveRequest transLeaveRequest;

	private boolean isInLeave = false;
	private boolean isInLVRQId = false;
	private boolean isInLVRDocId = false;
	private boolean isInUserId = false;
	private boolean isInVDate = false;
	private boolean isInFromDate = false;
	private boolean isInToDate = false;
	private boolean isInReason = false;
	private boolean isInAppStatus = false;
	private boolean isInAppBy = false;
	private boolean isInAppRemark = false;
	private boolean isInSmid = false;
	private boolean isInAppBySMid = false;
	private boolean isInLeaveFlag = false;
	private boolean isInL3RejectionRemark = false;
	private boolean isInAndroid_Id = false;
	private boolean isInNoOFDays = false;
	private boolean isInCreatedDate = false;
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("Leave")){
		   isInLeave = true;		   
		   transLeaveRequest = new TransLeaveRequest();
	   }
	   if(name.equalsIgnoreCase("LVRQId")){
		   isInLVRQId= true;
	   }
	   if(name.equalsIgnoreCase("LVRDocId")){
		   isInLVRDocId = true;
	   }
	   if(name.equalsIgnoreCase("UserId")){
		   isInUserId = true;
	   }
	   if(name.equalsIgnoreCase("VDate")){
		   isInVDate = true;
	   }
	   if(name.equalsIgnoreCase("FromDate")){
		   isInFromDate = true;
	   }
	   if(name.equalsIgnoreCase("ToDate")){
		   isInToDate = true;
	   }
	   if(name.equalsIgnoreCase("Reason")){
		   isInReason = true;
	   }
	   if(name.equalsIgnoreCase("AppStatus")){
		   isInAppStatus = true;
	   }
	   if(name.equalsIgnoreCase("AppBy")){
		   isInAppBy= true;
	   }
	   
	   if(name.equalsIgnoreCase("AppRemark")){
		   isInAppRemark = true;
	   }
	   
	   if(name.equalsIgnoreCase("Smid")){
		   isInSmid = true;
	   }
	   if(name.equalsIgnoreCase("AppBySMid")){
		   isInAppBySMid = true;
	   }
	   if(name.equalsIgnoreCase("LeaveFlag")){
		   isInLeaveFlag= true;
	   }
	  
	   if(name.equalsIgnoreCase("Android_Id")){
		   isInAndroid_Id = true;
	   }
	   if(name.equalsIgnoreCase("NoOFDays")){
		   isInNoOFDays = true;
	   }
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   }
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("Leave")){
			   isInLeave = false;		   
			   transLeaveRequestList.add(transLeaveRequest);
		   }
		 if(name.equalsIgnoreCase("LVRQId")){
			   isInLVRQId= false;
		   }
		   if(name.equalsIgnoreCase("LVRDocId")){
			   isInLVRDocId = false;
		   }
		   if(name.equalsIgnoreCase("UserId")){
			   isInUserId = false;
		   }
		   if(name.equalsIgnoreCase("VDate")){
			   isInVDate = false;
		   }
		   if(name.equalsIgnoreCase("FromDate")){
			   isInFromDate = false;
		   }
		   if(name.equalsIgnoreCase("ToDate")){
			   isInToDate = false;
		   }
		   if(name.equalsIgnoreCase("Reason")){
			   isInReason = false;
		   }
		   if(name.equalsIgnoreCase("AppStatus")){
			   isInAppStatus = false;
		   }
		   if(name.equalsIgnoreCase("AppBy")){
			   isInAppBy= false;
		   }
		   
		   if(name.equalsIgnoreCase("AppRemark")){
			   isInAppRemark = false;
		   }
		   
		   if(name.equalsIgnoreCase("Smid")){
			   isInSmid = false;
		   }
		   if(name.equalsIgnoreCase("AppBySMid")){
			   isInAppBySMid = false;
		   }
		   if(name.equalsIgnoreCase("LeaveFlag")){
			   isInLeaveFlag= false;
		   }
		   if(name.equalsIgnoreCase("Android_Id")){
			   isInAndroid_Id = false;
		   }
		   if(name.equalsIgnoreCase("NoOFDays")){
			   isInNoOFDays = false;
		   }   
		   if(name.equalsIgnoreCase("createddate")){
			   isInCreatedDate = false;
		   }  
	}

	//element content
	public void characters(char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInLVRQId){
	    	transLeaveRequest.setId(chars.toString());
	    	System.out.println("id="+chars.toString());
	    }
	    else if(isInLVRDocId){
	    	transLeaveRequest.setLeaveDocId(chars.toString());
	    	System.out.println("name="+chars.toString());
	    }
	       else if(isInUserId){
	    	   transLeaveRequest.setUserId(chars.toString());
	    }	
	    else if(isInVDate){
	    	transLeaveRequest.setVdate(chars.toString());
	    }
	    else if(isInFromDate){
	    	transLeaveRequest.setFromDate(chars.toString());
	    }
	    else if(isInToDate){
	    	transLeaveRequest.setToDate(chars.toString());
	    }
	    else if(isInReason){
	    	transLeaveRequest.setReason(chars.toString());
	    }
	    else if(isInAppStatus){
	    	transLeaveRequest.setAppStatus(chars.toString());
	    }
	   
	   	    else if(isInAppBy){
	   	    	transLeaveRequest.setAppBy(chars.toString());
	    }
	    else if(isInAppRemark){
	    	transLeaveRequest.setAppRemark(chars.toString());
	    }
	    else if(isInSmid){
	    	transLeaveRequest.setSmId(chars.toString());
	    }
	   
	    else if(isInAppBySMid){
	    	transLeaveRequest.setAppBySmid(chars.toString());
	    }
	    else if(isInLeaveFlag){
	    	transLeaveRequest.setLeaveFlag(chars.toString());
	    }
	    else if(isInAndroid_Id){
	    	transLeaveRequest.setAndroid_id(chars.toString());
	    }
	    else if(isInNoOFDays){
	    	transLeaveRequest.setNoOfDay(chars.toString());
	    }
	    else if(isInCreatedDate){
	    	transLeaveRequest.setCreatedDate(chars.toString());
	    } 
	    
	}
	
	public ArrayList<TransLeaveRequest> getData(){
		return this.transLeaveRequestList;
	}
	
}