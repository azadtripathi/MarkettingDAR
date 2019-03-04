package com.dm.parser;

import android.util.Log;

import com.dm.model.DemoTransaction;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;


public class DemoDataHandler extends DefaultHandler{
	private ArrayList<DemoTransaction> demoList = new ArrayList<DemoTransaction>();
	private DemoTransaction demo;

	private boolean isInDemo = false;
	private boolean isInDemoId = false;
	private boolean isInVisId = false;
	private boolean isInDemoDocId = false;
	private boolean isInVDate = false;
	private boolean isInUserId = false;
	private boolean isInSMID = false;
	private boolean isInPartyid = false;
	private boolean isInAreaid = false;
	private boolean isInRemarks = false;
	private boolean isInproductClassid = false;
	private boolean isInproductSegmentId = false;
	private boolean isInProductMatgrp = false;
	private boolean isInAndroid_Id = false;
	private boolean isInCreatedDate = false;
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("Demo")){
		   isInDemo = true;		   
		   demo = new DemoTransaction();
	   }
	   if(name.equalsIgnoreCase("DemoId")){
		   isInDemoId = true;
	   }
	   if(name.equalsIgnoreCase("VisId")){
		   isInVisId = true;
	   }
	   if(name.equalsIgnoreCase("DemoDocId")){
		   isInDemoDocId = true;
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
	   
	   if(name.equalsIgnoreCase("Areaid")){
		   isInAreaid = true;
	   } 
	   if(name.equalsIgnoreCase("Remarks")){
		   isInRemarks = true;
	   
	   } 
	   if(name.equalsIgnoreCase("productClassid")){
		   isInproductClassid = true;
	   
	   }  
	   if(name.equalsIgnoreCase("productSegmentId")){
		   isInproductSegmentId = true;
	   
	   } 
	   if(name.equalsIgnoreCase("ProductMatgrp")){
		   isInProductMatgrp = true;
	   
	   }  
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   }
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("Demo")){
			isInDemo = false;
			demoList.add(demo);
		}
		 if(name.equalsIgnoreCase("DemoId")){
			   isInDemoId = false;
		   }
		   if(name.equalsIgnoreCase("VisId")){
			   isInVisId = false;
		   }
		   if(name.equalsIgnoreCase("DemoDocId")){
			   isInDemoDocId = false;
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
		   
		   if(name.equalsIgnoreCase("Areaid")){
			   isInAreaid = false;
		   } 
		   if(name.equalsIgnoreCase("Remarks")){
			   isInRemarks = false;
		   
		   } 
		   if(name.equalsIgnoreCase("productClassid")){
			   isInproductClassid = false;
		   
		   }  
		   if(name.equalsIgnoreCase("productSegmentId")){
			   isInproductSegmentId = false;
		   
		   } 
		   if(name.equalsIgnoreCase("ProductMatgrp")){
			   isInProductMatgrp = false;
		   
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
	    if(isInDemoId){
	    	demo.setDemoId(chars.toString());
	    }
	    else if(isInVisId){
	    	demo.setVisId(chars.toString());
	    }
	    else if(isInDemoDocId){
	    	demo.setDemoDocId(chars.toString());
	    }
	    else if(isInAndroid_Id){
	    	demo.setAndroid_id(chars.toString());
	    }
	    else if(isInVDate){
	    	demo.setVDate(chars.toString());
	    }
	    else if(isInUserId){
	    	demo.setUserId(chars.toString());
	    } 
	    else if(isInSMID){
	    	demo.setSMId(chars.toString());
	    } 
	    else if(isInPartyid){
	    	demo.setPartyId(chars.toString());
	    } 
	    else if(isInAreaid){
	    	demo.setAreaId(chars.toString());
	    } 
	    else if(isInRemarks){
	    	demo.setRemarks(chars.toString());
	    } 
	    else if(isInproductClassid){
	    	demo.setProductClassId(chars.toString());
	    }   
	    else if(isInproductSegmentId){
	    	demo.setProductSegmentId(chars.toString());
	    }
	    else if(isInProductMatgrp){
	    	demo.setProductMatGrp(chars.toString());
	    }
	    else if(isInCreatedDate){
	    	demo.setCreatedDate(chars.toString());
	    } 
	}
	
	public ArrayList<DemoTransaction> getData(){
		return this.demoList;
	}
	
	
	
	

	
	
	
}
