package com.dm.parser;

import android.util.Log;

import com.dm.model.Visit;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class DistributerDiscussionDataHandler extends DefaultHandler{
	private ArrayList<Visit> visitList = new ArrayList<Visit>();
	private Visit visit;

	private boolean isInDistributorDiscussion = false;
	private boolean isInVisDistId = false;
	private boolean isInVisId = false;
	private boolean isInVDate = false;
	private boolean isInUserId = false;
	private boolean isInSMID = false;
	private boolean isInCityId = false;
	private boolean isInDistId = false;
	private boolean isInRemark = false;
	private boolean isInNextVisitDate = false;
	private boolean isInnextVisitTime = false;
	private boolean isInSpendfromTime = false;
	private boolean isInSpendToTime = false;
	private boolean isInAndroidId = false;
	private boolean isInCreatedDate = false;
	private boolean isInStock = false;
	
	
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("DistributorDiscussion")){
		   isInDistributorDiscussion = true;		   
		   visit = new Visit();
	   }
	   if(name.equalsIgnoreCase("VisDistId")){
		   isInVisDistId = true;
	   }
	   if(name.equalsIgnoreCase("VisId")){
		   isInVisId = true;
	   }
	   if(name.equalsIgnoreCase("VDate")){
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
	   if(name.equalsIgnoreCase("DistId")){
		   isInDistId = true;
	   }
	   if(name.equalsIgnoreCase("Remark")){
		   isInRemark = true;
	   }
	   if(name.equalsIgnoreCase("NextVisitDate")){
		   isInNextVisitDate = true;
	   }
	   if(name.equalsIgnoreCase("nextVisitTime")){
		   isInnextVisitTime = true;
	   }
	   if(name.equalsIgnoreCase("SpendfromTime")){
		   isInSpendfromTime = true;
	   } 
	   if(name.equalsIgnoreCase("SpendToTime")){
		   isInSpendToTime = true;
	   } 
	   if(name.equalsIgnoreCase("Android_Id")){
		   isInAndroidId = true;
	   } 
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   }
	   if(name.equalsIgnoreCase("stock")){
		   isInStock = true;
	   }
	   
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("DistributorDiscussion")){
			isInDistributorDiscussion = false;
			visitList.add(visit);
		}
		if(name.equalsIgnoreCase("VisDistId")){
			   isInVisDistId = false;
		   }
		   if(name.equalsIgnoreCase("VisId")){
			   isInVisId = false;
		   }
		   if(name.equalsIgnoreCase("VDate")){
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
		   if(name.equalsIgnoreCase("DistId")){
			   isInDistId = false;
		   }
		   if(name.equalsIgnoreCase("Remark")){
			   isInRemark = false;
		   }
		   if(name.equalsIgnoreCase("NextVisitDate")){
			   isInNextVisitDate = false;
		   }
		   if(name.equalsIgnoreCase("nextVisitTime")){
			   isInnextVisitTime = false;
		   }
		   if(name.equalsIgnoreCase("SpendfromTime")){
			   isInSpendfromTime = false;
		   } 
		   if(name.equalsIgnoreCase("SpendToTime")){
			   isInSpendToTime = false;
		   } 
		   if(name.equalsIgnoreCase("Android_Id")){
			   isInAndroidId = false;
		   } 
		   if(name.equalsIgnoreCase("createddate")){
			   isInCreatedDate = false;
		   }
		   if(name.equalsIgnoreCase("stock")){
			   isInStock = false;
		   }
		   
	}

	//element content
	public void characters (char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInVisDistId){
	    	visit.setVisId((chars.toString()));
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
	    else if(isInnextVisitTime){
	    	visit.setToTime2(chars.toString());
	    } 
	    else if(isInSpendfromTime){
	    	visit.setfrTime1(chars.toString());
	    } 
	    else if(isInSpendToTime){
	    	visit.setToTime1(chars.toString());
	    }
	    else if(isInAndroidId){
	    	visit.setAndroidDocId(chars.toString());
	    } 
	    else if(isInCreatedDate){
	    	visit.setCreatedDate(chars.toString());
	    } 
	    else if(isInStock){
	    	visit.setStock(chars.toString());
	    }
	}
	
	public ArrayList<Visit> getData(){
		return this.visitList;
	}

	
	
	
}
