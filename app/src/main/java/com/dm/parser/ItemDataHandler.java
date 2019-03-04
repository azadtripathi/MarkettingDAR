package com.dm.parser;

import android.util.Log;

import com.dm.model.Item;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class ItemDataHandler extends DefaultHandler{
	private ArrayList<Item> itemList = new ArrayList<Item>();
	private Item item;

	private boolean isInItem = false;
	private boolean isInId = false;
	private boolean isInItemName = false;
	private boolean isInUnit = false;
	private boolean isInPrice = false;
	private boolean isInDP = false;
	private boolean isInMRP = false;
	private boolean isInRP = false;
	private boolean isInActive = false;
	private boolean isInStdPack = false;
	private boolean isInSyncId = false;
	private boolean isInUnderid = false;
	private boolean isInLvl = false;
	private boolean isInItemCode = false;
	private boolean isInDisplayName = false;
	private boolean isInItemType = false;
	private boolean isInSegmentId = false;
	private boolean isInClassId = false;
	private boolean isInPriceGroup = false;
	private boolean isInCreatedDate = false;
	
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("Product")){
		   isInItem = true;		   
		   item = new Item();
	   }
	   if(name.equalsIgnoreCase("ItemId")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("ItemName")){
		   isInItemName = true;
	   }
	   if(name.equalsIgnoreCase("Unit")){
		   isInUnit = true;
	   }
	   if(name.equalsIgnoreCase("Active")){
		   isInActive = true;
	   }
	   if(name.equalsIgnoreCase("StdPack")){
		   isInStdPack = true;
	   }
	   if(name.equalsIgnoreCase("SyncId")){
		   isInSyncId = true;
	   }
	   if(name.equalsIgnoreCase("MRP")){
		   isInMRP = true;
	   }
	   if(name.equalsIgnoreCase("DP")){
		   isInDP = true;
	   }
	   if(name.equalsIgnoreCase("RP")){
		   isInRP= true;
	   }
	   
	   if(name.equalsIgnoreCase("Underid")){
		   isInUnderid = true;
	   }
	   
	   if(name.equalsIgnoreCase("Lvl")){
		   isInLvl = true;
	   }
	   if(name.equalsIgnoreCase("ItemCode")){
		   isInItemCode = true;
	   }
	   if(name.equalsIgnoreCase("DisplayName")){
		   isInDisplayName= true;
	   }
	   if(name.equalsIgnoreCase("ItemType")){
		   isInItemType = true;
	   }
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   }
	   	   if(name.equalsIgnoreCase("SegmentId")){
		   isInSegmentId = true;
	   }
	  
	   if(name.equalsIgnoreCase("ClassId")){
		   isInClassId = true;
	   }
	   if(name.equalsIgnoreCase("PriceGroup")){
		   isInPriceGroup = true;
	   }
	  
	   
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("Product")){
			   isInItem = false;		   
			   itemList.add(item);
		   }
		   if(name.equalsIgnoreCase("ItemId")){
			   isInId = false;
		   }
		   if(name.equalsIgnoreCase("ItemName")){
			   isInItemName = false;
		   }
		   if(name.equalsIgnoreCase("Unit")){
			   isInUnit = false;
		   }
		   if(name.equalsIgnoreCase("Active")){
			   isInActive = false;
		   }
		   if(name.equalsIgnoreCase("StdPack")){
			   isInStdPack = false;
		   }
		   if(name.equalsIgnoreCase("SyncId")){
			   isInSyncId = false;
		   }
		   if(name.equalsIgnoreCase("MRP")){
			   isInMRP = false;
		   }
		   if(name.equalsIgnoreCase("DP")){
			   isInDP = false;
		   }
		   if(name.equalsIgnoreCase("RP")){
			   isInRP= false;
		   }
		   
		   if(name.equalsIgnoreCase("Underid")){
			   isInUnderid = false;
		   }
		   
		   if(name.equalsIgnoreCase("Lvl")){
			   isInLvl = false;
		   }
		   if(name.equalsIgnoreCase("ItemCode")){
			   isInItemCode = false;
		   }
		   if(name.equalsIgnoreCase("DisplayName")){
			   isInDisplayName= false;
		   }
		   if(name.equalsIgnoreCase("ItemType")){
			   isInItemType = false;
		   }if(name.equalsIgnoreCase("createddate")){
			   isInCreatedDate = false;
		   }
		   
		   	   if(name.equalsIgnoreCase("SegmentId")){
			   isInSegmentId = false;
		   }
		  
		   if(name.equalsIgnoreCase("ClassId")){
			   isInClassId = false;
		   }
		   if(name.equalsIgnoreCase("PriceGroup")){
			   isInPriceGroup = false;
		   }
		   
	}

	//element content
	public void characters(char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInId){
	    	item.setItem_id(chars.toString());
	    	System.out.println("id="+chars.toString());
	    }
	    else if(isInItemName){
	    	item.setItem_Name(chars.toString());
	    	System.out.println("name="+chars.toString());
	    }
	       else if(isInUnit){
	    	item.setUnit(chars.toString());
	    }	
	    else if(isInActive){
	    	item.setActive(chars.toString());
	    }
	    else if(isInStdPack){
	    	item.setStdpack(chars.toString());
	    }
	    else if(isInSyncId){
	    	item.setSync_id(chars.toString());
	    }
	    else if(isInDP){
	    	item.setDP(chars.toString());
	    }
	    else if(isInMRP){
	    	item.setMRP(chars.toString());
	    }
	   
	   	    else if(isInRP){
	    	item.setRP(chars.toString());
	    }
	    else if(isInItemCode){
	    	item.setItemcode(chars.toString());
	    }
	    else if(isInDisplayName){
	    	item.setDisplay_Name(chars.toString());
	    }
	   
	    else if(isInSegmentId){
	    	item.setSegmentid(chars.toString());
	    }
	    else if(isInClassId){
	    	item.setClassid(chars.toString());
	    }
	    else if(isInPriceGroup){
	    	item.setPricegroup(chars.toString());
	    }
	    else if(isInCreatedDate){
	    	item.setCreatedDate(chars.toString());
	    } 	
	}
	
	public ArrayList<Item> getData(){
		return this.itemList;
	}
	
}