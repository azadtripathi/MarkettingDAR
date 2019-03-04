package com.dm.parser;

import android.util.Log;

import com.dm.model.Order;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class OrderDataHandler extends DefaultHandler{
	private ArrayList<Order> orderList = new ArrayList<Order>();
	private Order order;

	private boolean isInOrder = false;
	private boolean isInOrdId = false;
	private boolean isInVisId = false;
	private boolean isInOrdDocid = false;
	private boolean isInVDate = false;
	private boolean isInUserId = false;
	private boolean isInSMID = false;
	private boolean isInPartyid = false;
	private boolean isInAreaid = false;
	private boolean isInRemarks = false;
	private boolean isInorderAmount = false;
	private boolean isInAndroid_Id = false;
	private boolean isInCreatedDate = false;
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("order")){
		   isInOrder = true;		   
		   order = new Order();
	   }
	   if(name.equalsIgnoreCase("OrdId")){
		   isInOrdId = true;
	   }
	   if(name.equalsIgnoreCase("VisId")){
		   isInVisId = true;
	   }
	   if(name.equalsIgnoreCase("OrdDocid")){
		   isInOrdDocid = true;
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
	   if(name.equalsIgnoreCase("orderAmount")){
		   isInorderAmount = true;
	   
	   }  
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   }
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("order")){
			isInOrder = false;
			orderList.add(order);
		}
		if(name.equalsIgnoreCase("OrdId")){
			   isInOrdId = false;
		   }
		   if(name.equalsIgnoreCase("VisId")){
			   isInVisId = false;
		   }
		   if(name.equalsIgnoreCase("OrdDocid")){
			   isInOrdDocid = false;
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
		   if(name.equalsIgnoreCase("orderAmount")){
			   isInorderAmount = false;
		   
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
	    if(isInOrdId){
	    	order.setOrdId(chars.toString());
	    }
	    else if(isInVisId){
	    	order.setVisId(chars.toString());
	    }
	    else if(isInOrdDocid){
	    	order.setOrdDocId(chars.toString());
	    }
	    else if(isInAndroid_Id){
	    	order.setAndroid_id(chars.toString());
	    }
	    else if(isInVDate){
	    	order.setVDate(chars.toString());
	    }
	    else if(isInUserId){
	    	order.setUserId(chars.toString());
	    } 
	    else if(isInSMID){
	    	order.setSMId(chars.toString());
	    } 
	    else if(isInPartyid){
	    	order.setPartyId(chars.toString());
	    } 
	    else if(isInAreaid){
	    	order.setAreaId(chars.toString());
	    } 
	    else if(isInRemarks){
	    	order.setRemarks(chars.toString());
	    } 
	    else if(isInorderAmount){
	    	order.setOrderAmount(chars.toString());
	    } 
	    else if(isInCreatedDate){
	    	order.setCreatedDate(chars.toString());
	    } 
	    
	}
	
	public ArrayList<Order> getData(){
		return this.orderList;
	}
	
	
	
	
}
