package com.dm.parser;

import android.util.Log;

import com.dm.model.BeatPlan;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;


public class BeatPlanDataHandler extends DefaultHandler{
	private ArrayList<BeatPlan> beatPlanList = new ArrayList<BeatPlan>();
	private BeatPlan beat;

	private boolean isInBeatPlan = false;
	private boolean isInBeatPlanId = false;
	private boolean isIndocid = false;
	private boolean isInUserId = false;
	private boolean isInPlannedDate = false;
	private boolean isInareaId = false;
	private boolean isInAppStatus = false;
	private boolean isInAppBy = false;
	private boolean isInAppRemark = false;
	private boolean isInStartDate = false;
	private boolean isInCityid = false;
	private boolean isInbeatId = false;
	private boolean isInSmid = false;
	private boolean isInAppBySMid = false;
	private boolean isInCreatedDate = false;
	private boolean isInAndroid_Id = false;
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("BeatPlan")){
		   isInBeatPlan = true;		   
		   beat = new BeatPlan();
	   }
	   if(name.equalsIgnoreCase("BeatPlanId")){
		   isInBeatPlanId = true;
	   }
	   if(name.equalsIgnoreCase("docid")){
		   isIndocid = true;
	   }
	   if(name.equalsIgnoreCase("UserId")){
		   isInUserId = true;
	   }
	   if(name.equalsIgnoreCase("PlannedDate")){
		   isInPlannedDate = true;
	   }
	   if(name.equalsIgnoreCase("areaId")){
		   isInareaId = true;
	   }
	   if(name.equalsIgnoreCase("AppStatus")){
		   isInAppStatus = true;
	   }
	   if(name.equalsIgnoreCase("AppBy")){
		   isInAppBy = true;
	   }
	   if(name.equalsIgnoreCase("AppRemark")){
		   isInAppRemark = true;
	   }
	   if(name.equalsIgnoreCase("StartDate")){
		   isInStartDate = true;
	   }
	   if(name.equalsIgnoreCase("Cityid")){
		   isInCityid = true;
	   }
	   if(name.equalsIgnoreCase("beatId")){
		   isInbeatId = true;
	   }
	   if(name.equalsIgnoreCase("Smid")){
		   isInSmid = true;
	   }
	   if(name.equalsIgnoreCase("AppBySMid")){
		   isInAppBySMid = true;
	   }
	   if(name.equalsIgnoreCase("Createddate")){
		   isInCreatedDate = true;
	   }
	   if(name.equalsIgnoreCase("Android_Id")){
		   isInAndroid_Id = true;
	   }
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("BeatPlan")){
			isInBeatPlan = false;
			beatPlanList.add(beat);
		}
		if(name.equalsIgnoreCase("BeatPlanId")){
			   isInBeatPlanId = false;
		   }
		   if(name.equalsIgnoreCase("docid")){
		   isIndocid = false;
	   }
		   if(name.equalsIgnoreCase("UserId")){
			   isInUserId = false;
		   }
		   if(name.equalsIgnoreCase("PlannedDate")){
			   isInPlannedDate = false;
		   }
		   if(name.equalsIgnoreCase("areaId")){
			   isInareaId = false;
		   }
		   if(name.equalsIgnoreCase("AppStatus")){
			   isInAppStatus = false;
		   }
		   if(name.equalsIgnoreCase("AppBy")){
			   isInAppBy = false;
		   }
		   if(name.equalsIgnoreCase("AppRemark")){
			   isInAppRemark = false;
		   }
		   if(name.equalsIgnoreCase("StartDate")){
			   isInStartDate = false;
		   }
		   if(name.equalsIgnoreCase("Cityid")){
			   isInCityid = false;
		   }
		   if(name.equalsIgnoreCase("beatId")){
			   isInbeatId = false;
		   }
		   if(name.equalsIgnoreCase("Smid")){
			   isInSmid = false;
		   }
		   if(name.equalsIgnoreCase("AppBySMid")){
			   isInAppBySMid = false;
		   }
		   if(name.equalsIgnoreCase("Createddate")){
			   isInCreatedDate = false;
		   }
		   if(name.equalsIgnoreCase("Android_Id")){
			   isInAndroid_Id = false;
		   }
	}

	//element content
	public void characters (char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInBeatPlanId){
	    	beat.setBeatPlanId(chars.toString());
	    }else if(isIndocid){
	    	beat.setDocId(chars.toString());
	    }else if(isInUserId){
	    	beat.setUserId(chars.toString());
	    }else if(isInPlannedDate){
	    	beat.setPlannedDate(chars.toString());
	    }
	    else if(isInareaId){
	    	beat.setAreaId(chars.toString());
	    } 
	    else if(isInAppStatus){
	    	beat.setAppStatus(chars.toString());
	    } 	 
	    else if(isInAppBy){
	    	beat.setAppBy(chars.toString());
	    }  
	    else if(isInAppRemark){
	    	beat.setAppRemark(chars.toString());
	    }   
	    else if(isInStartDate){
	    	beat.setStartDate(chars.toString());
	    }
	    else if(isInCityid){
	    	beat.setCityId(chars.toString());
	    }
	    else if(isInbeatId){
	    	beat.setBeatId(chars.toString());
	    }
	    else if(isInSmid){
	    	beat.setSmId(chars.toString());
	    }
	    else if(isInAppBySMid){
	    	beat.setAppBysmId(chars.toString());
	    }
	    else if(isInCreatedDate){
	    	beat.setCreatedDate(chars.toString());
	    }
	    else if(isInAndroid_Id){
	    	beat.setAndroid_id(chars.toString());
	    }
	}
	
	public ArrayList<BeatPlan> getData(){
		return this.beatPlanList;
	}

	
	
}
