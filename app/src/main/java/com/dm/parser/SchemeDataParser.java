package com.dm.parser;

import android.util.Log;

import com.dm.model.Scheme;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;


public class SchemeDataParser extends DefaultHandler{
	private ArrayList<Scheme> schemeList = new ArrayList<Scheme>();
	private Scheme scheme;

	private boolean isInScheme = false;
	private boolean isInName = false;
	private boolean isInId = false;
	private boolean isInCreatedDate = false;
	private boolean isInActive = false;
	private boolean isInSyncId = false;
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("Scheme")){
		   isInScheme = true;		   
		   scheme = new Scheme();
	   }
	   if(name.equalsIgnoreCase("Id")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("Name")){
		   isInName = true;
	   }
	   if(name.equalsIgnoreCase("Active")){
		   isInActive = true;
	   }
	   
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   }
	   if(name.equalsIgnoreCase("SyncId")){
		   isInSyncId = true;
	   }
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("Scheme")){
			isInScheme = false;
			schemeList.add(scheme);
		}
		if(name.equalsIgnoreCase("Id")){
			isInId = false;
		}
		if(name.equalsIgnoreCase("Name")){
			   isInName = false;
		   }
		   if(name.equalsIgnoreCase("Active")){
			   isInActive = false;
		   }
		  
		   if(name.equalsIgnoreCase("createddate")){
			   isInCreatedDate = false;
		   }
		   if(name.equalsIgnoreCase("SyncId")){
			   isInSyncId = false;
		   }   
		   
		   
	}

	//element content
	public void characters (char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInId){
	    	scheme.setSchemeid(chars.toString());
	    }else if(isInName){
	    	scheme.setSchemename(chars.toString());
	    }
	    else if(isInActive){
	    	scheme.setActive(chars.toString());
	    }
	    
	    else if(isInCreatedDate){
	    	scheme.setCreatedDate(chars.toString());
	    } 	 
	    else if(isInSyncId){
	    	scheme.setSync_id(chars.toString());
	    } 
	    
	}
	
	public ArrayList<Scheme> getData(){
		return this.schemeList;
	}

	
	
}
