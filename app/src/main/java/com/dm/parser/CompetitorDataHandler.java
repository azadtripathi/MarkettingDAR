package com.dm.parser;

import android.util.Log;

import com.dm.model.Competitor;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;


public class CompetitorDataHandler extends DefaultHandler{
	private ArrayList<Competitor> competitorList = new ArrayList<Competitor>();
	private Competitor competitor;

	private boolean isInCompetitor = false;
	private boolean isInComptId = false;
	private boolean isInVisId = false;
	private boolean isInDocId = false;
	private boolean isInVDate = false;
	private boolean isInUserId = false;
	private boolean isInSMID = false;
	private boolean isInPartyid = false;
	private boolean isInItem = false;
	private boolean isInQty = false;
	private boolean isInRate = false;
	private boolean isInAndroid_Id = false;
	private boolean isInCreatedDate = false;
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("Competitor")){
		   isInCompetitor = true;		   
		   competitor = new Competitor();
	   }
	   if(name.equalsIgnoreCase("ComptId")){
		   isInComptId = true;
	   }
	   if(name.equalsIgnoreCase("VisId")){
		   isInVisId = true;
	   }
	   if(name.equalsIgnoreCase("DocId")){
		   isInDocId = true;
	   }
	   if(name.equalsIgnoreCase("Android_Id")){
		   isInAndroid_Id = true;
	   }
	   if(name.equalsIgnoreCase("VDate")){
		   isInVDate = true;
	   }
	   if(name.equalsIgnoreCase("UserId")){
		   isInUserId = true;
	   }
	   if(name.equalsIgnoreCase("SMID")){
		   isInSMID = true;
	   }
	   if(name.equalsIgnoreCase("Partyid")){
		   isInPartyid = true;
	   } 
	   
	  
	   if(name.equalsIgnoreCase("Item")){
		   isInItem = true;
	   
	   } 
	   if(name.equalsIgnoreCase("Qty")){
		   isInQty = true;
	   
	   }  
	   if(name.equalsIgnoreCase("Rate")){
		   isInRate = true;
	   
	   }
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   }
	   
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("Competitor")){
			isInCompetitor = false;
			competitorList.add(competitor);
		}
		if(name.equalsIgnoreCase("ComptId")){
			   isInComptId = false;
		   }
		   if(name.equalsIgnoreCase("VisId")){
			   isInVisId = false;
		   }
		   if(name.equalsIgnoreCase("DocId")){
			   isInDocId = false;
		   }
		   if(name.equalsIgnoreCase("Android_Id")){
			   isInAndroid_Id = false;
		   }
		   if(name.equalsIgnoreCase("VDate")){
			   isInVDate = false;
		   }
		   if(name.equalsIgnoreCase("UserId")){
			   isInUserId = false;
		   }
		   if(name.equalsIgnoreCase("SMID")){
			   isInSMID = false;
		   }
		   if(name.equalsIgnoreCase("Partyid")){
			   isInPartyid = false;
		   } 
		   
		   
		   if(name.equalsIgnoreCase("Item")){
			   isInItem = false;
		   
		   } 
		   if(name.equalsIgnoreCase("Qty")){
			   isInQty = false;
		   
		   }  
		   if(name.equalsIgnoreCase("Rate")){
			   isInRate = false;
		   
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
	    if(isInComptId){
	    	competitor.setComptId(chars.toString());
	    }
	    else if(isInVisId){
	    	competitor.setVisId(chars.toString());
	    }
	    else if(isInDocId){
	    	competitor.setDocId(chars.toString());
	    }
	    else if(isInAndroid_Id){
	    	competitor.setAndroid_id(chars.toString());
	    }
	    else if(isInVDate){
	    	competitor.setVDate(chars.toString());
	    }
	    else if(isInUserId){
	    	competitor.setUserId(chars.toString());
	    } 
	    else if(isInSMID){
	    	competitor.setSMID(chars.toString());
	    } 
	    else if(isInPartyid){
	    	competitor.setPartyId(chars.toString());
	    } 
	   
	    else if(isInItem){
	    	competitor.setItem(chars.toString());
	    } 
	    else if(isInQty){
	    	competitor.setQty(chars.toString());
	    }   
	    else if(isInRate){
	    	competitor.setRate(chars.toString());
	    }
	    else if(isInCreatedDate){
	    	competitor.setCreatedDate(chars.toString());
	    } 
	    
	}
	
	public ArrayList<Competitor> getData(){
		return this.competitorList;
	}
	
	


}
