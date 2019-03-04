package com.dm.parser;

import android.util.Log;

import com.dm.model.Visit;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class DsrDataHandler extends DefaultHandler{
	private ArrayList<Visit> visitList = new ArrayList<Visit>();
	private Visit visit;

	private boolean isInDSR = false;
	private boolean isInvisitDocId = false;
	private boolean isInVisId = false;
	private boolean isInVDate = false;
	private boolean isInUserId = false;
	private boolean isInSMID = false;
	private boolean isInCityId = false;
	private boolean isInDistId = false;
	private boolean isInRemark = false;
	private boolean isInNextVisitDate = false;
	private boolean isInWithUserId = false;
	private boolean isInFROMTime = false;
	private boolean isInToTime = false;
	private boolean isInAndroidId = false;
	private boolean isInModeOfTransport = false;
	private boolean isInVehicleUsed = false;
	private boolean isInnWithUserId = false;
	private boolean isIncityIds = false;
	private boolean isIncityName = false;
	private boolean isInCreatedDate = false;
	private boolean isInOrderByEmail=false;
	private boolean isInOrderByPhone=false;
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("DSR")){
		   isInDSR = true;		   
		   visit = new Visit();
	   }
	   if(name.equalsIgnoreCase("visitDocId")){
		   isInvisitDocId = true;
	   }
	   if(name.equalsIgnoreCase("VisId")){
		   isInVisId = true;
	   }
	   if(name.equalsIgnoreCase("Vdate")){
		   isInVDate = true;
	   }if(name.equalsIgnoreCase("UserId")){
		   isInUserId = true;
	   }
	   if(name.equalsIgnoreCase("CityId")){
		   isInCityId = true;
	   }
	   if(name.equalsIgnoreCase("SMID")){
		   isInSMID = true;
	   }
	   
	   if(name.equalsIgnoreCase("Remark")){
		   isInRemark = true;
	   }
	   if(name.equalsIgnoreCase("NextVisitDate")){
		   isInNextVisitDate = true;
	   }
	   if(name.equalsIgnoreCase("WithUserId")){
		   isInWithUserId = true;
	   }
	   if(name.equalsIgnoreCase("nWithUserId")){
		   isInnWithUserId = true;
	   }
	   if(name.equalsIgnoreCase("ModeOfTransport")){
		   isInModeOfTransport = true;
	   }
	   if(name.equalsIgnoreCase("VehicleUsed")){
		   isInVehicleUsed = true;
	   }
	   if(name.equalsIgnoreCase("FROMTime")){
		   isInFROMTime = true;
	   } 
	   if(name.equalsIgnoreCase("ToTime")){
		   isInToTime = true;
	   } 
	   if(name.equalsIgnoreCase("Android_Id")){
		   isInAndroidId = true;
	   }  
	   if(name.equalsIgnoreCase("cityIds")){
		   isIncityIds = true;
	   }
	   if(name.equalsIgnoreCase("cityName")){
		   isIncityName = true;
	   }
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   }
	   if(name.equalsIgnoreCase("OrderAmountMail")){
		   isInOrderByEmail = true;
	   }
	   if(name.equalsIgnoreCase("OrderAmountPhone")){
		   isInOrderByPhone = true;
	   }
	   
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("DSR")){
			isInDSR = false;
			visitList.add(visit);
		}
		 if(name.equalsIgnoreCase("visitDocId")){
			   isInvisitDocId = false;
		   }
		   if(name.equalsIgnoreCase("VisId")){
			   isInVisId = false;
		   }
		   if(name.equalsIgnoreCase("Vdate")){
			   isInVDate = false;
		   }if(name.equalsIgnoreCase("UserId")){
			   isInUserId = false;
		   }
		   if(name.equalsIgnoreCase("CityId")){
			   isInCityId = false;
		   }
		   if(name.equalsIgnoreCase("SMID")){
			   isInSMID = false;
		   }
		   
		   if(name.equalsIgnoreCase("Remark")){
			   isInRemark = false;
		   }
		   if(name.equalsIgnoreCase("NextVisitDate")){
			   isInNextVisitDate = false;
		   }
		   if(name.equalsIgnoreCase("WithUserId")){
			   isInWithUserId = false;
		   }
		   if(name.equalsIgnoreCase("nWithUserId")){
			   isInnWithUserId = false;
		   }
		   if(name.equalsIgnoreCase("ModeOfTransport")){
			   isInModeOfTransport = false;
		   }
		   if(name.equalsIgnoreCase("VehicleUsed")){
			   isInVehicleUsed = false;
		   }
		   if(name.equalsIgnoreCase("FROMTime")){
			   isInFROMTime = false;
		   } 
		   if(name.equalsIgnoreCase("ToTime")){
			   isInToTime = false;
		   } 
		   if(name.equalsIgnoreCase("Android_Id")){
			   isInAndroidId = false;
		   }  
		   if(name.equalsIgnoreCase("cityIds")){
			   isIncityIds = false;
		   }
		   if(name.equalsIgnoreCase("cityName")){
			   isIncityName = false;
		   }
		   if(name.equalsIgnoreCase("createddate")){
			   isInCreatedDate = false;
		   }
		   if(name.equalsIgnoreCase("OrderAmountMail")){
			   isInOrderByEmail = false;
		   }
		   if(name.equalsIgnoreCase("OrderAmountPhone")){
			   isInOrderByPhone = false;
		   }
		   
	}

	//element content
	public void characters (char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInvisitDocId){
	    	visit.setVisitDocId((chars.toString()));
	    }
	    else if(isInVisId){
	    	visit.setVisitNo(chars.toString());
	    }
	    else if(isInVDate){
	    	visit.setVdate(chars.toString());
	    }
	    else if(isInUserId){
	    	visit.setUserId(chars.toString());
	    }
	    else if(isInCityId){
	    	visit.setCityId(chars.toString());
	    } 
	    else if(isInSMID){
	    	visit.setSMId(chars.toString());
	    } 		
	    else if(isInDistId){
	    	visit.setDistId(chars.toString());
	    } 
	    else if(isInRemark){
	    	visit.setRemark(chars.toString());
	    } 
	    else if(isInNextVisitDate){
	    	visit.setNextVisitDate(chars.toString());
	    } 
	    else if(isInFROMTime){
	    	visit.setfrTime1(chars.toString());
	    } 
	    else if(isInToTime){
	    	visit.setToTime1(chars.toString());
	    } 
	    else if(isInWithUserId){
	    	visit.setWithUserId(chars.toString());
	    }
	    else if(isInnWithUserId){
	    	visit.setNextwithUserId(chars.toString());
	    }
	       
	    else if(isInAndroidId){
	    	visit.setAndroidDocId(chars.toString());
	    } 
	    else if(isInModeOfTransport){
	    	visit.setModeOfTransport(chars.toString());
	    }
	       
	    else if(isInVehicleUsed){
	    	visit.setVehicleUsed(chars.toString());
	    } 
	    else if(isIncityIds){
	    	visit.setCityIds(chars.toString());
	    } 
	    else if(isIncityName){
	    	visit.setCityName(chars.toString());
	    } 
	    else if(isInCreatedDate){
	    	visit.setCreatedDate(chars.toString());
	    } 
	    else if(isInOrderByEmail){
	    	visit.setOrderByEmail(chars.toString());
	    }
	    
	    else if(isInOrderByPhone){
	    	visit.setOrderByPhone(chars.toString());
	    }
	    
	}
	
	public ArrayList<Visit> getData(){
		return this.visitList;
	}

}
