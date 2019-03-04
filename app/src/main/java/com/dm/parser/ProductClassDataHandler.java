package com.dm.parser;

import android.util.Log;

import com.dm.model.ProductClass;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class ProductClassDataHandler extends DefaultHandler {
	
	private ArrayList<ProductClass> productclassList = new ArrayList<ProductClass>();
	private ProductClass productclass;
	private boolean isInProductClass = false;
	private boolean isInId = false;
	private boolean isInDescription = false;
	private boolean isInSyncId = false;
	private boolean isInActive = false;
	private boolean isInCreatedDate = false;
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("ProductClass")){
		   isInProductClass = true;		   
		   productclass = new ProductClass();
	   }
	   if(name.equalsIgnoreCase("Id")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("Description")){
		   isInDescription = true;
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
		if(name.equalsIgnoreCase("ProductClass")){
			isInProductClass = false;
			productclassList.add(productclass);
		}
		if(name.equalsIgnoreCase("Id")){
			isInId = false;
		}
		if(name.equalsIgnoreCase("description")){
			isInDescription = false;
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
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInId){
	    	productclass.setClass_id((chars.toString()));
	    }else if(isInDescription){
	    	productclass.setClass_name(chars.toString());
	    }
	    else if(isInSyncId){
	    	productclass.setSync_id(chars.toString());
	    }	
	    else if(isInActive){
	    	productclass.setActive(chars.toString());
	    } 
	    else if(isInCreatedDate){
	    	productclass.setCreatedDate(chars.toString());
	    } 	
	}
	
	public ArrayList<ProductClass> getData(){
		return this.productclassList;
	}

	

}
