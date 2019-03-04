/**
 * AreaHandler.javacom.example.Handler
 */
package com.dm.parser;

/**
 * @author dataman
 *
 */

import android.util.Log;

import com.dm.model.Area;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class AreaDataHandler extends DefaultHandler{
	private ArrayList<Area> areaList = new ArrayList<Area>();
	private Area area;

	private boolean isInArea = false;
	private boolean isInId = false;
	private boolean isInCity = false;
	private boolean isInName = false;
	private boolean isInType = false;
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
	   if(name.equalsIgnoreCase("Area")){
		   isInArea = true;		   
		   area = new Area();
	   }
	   if(name.equalsIgnoreCase("Id")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("Name")){
		   isInName = true;
	   }
	   if(name.equalsIgnoreCase("Type_Id")){
		   isInType = true;
	   }
	   if(name.equalsIgnoreCase("CityID")){
		   isInCity = true;
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
		if(name.equalsIgnoreCase("Area")){
			isInArea = false;
			areaList.add(area);
		}
		if(name.equalsIgnoreCase("Id")){
			isInId = false;
		}
		if(name.equalsIgnoreCase("Name")){
			isInName = false;
		}
		if(name.equalsIgnoreCase("Type_Id")){
			isInType = false;
		}
		if(name.equalsIgnoreCase("CityID")){
			isInCity = false;
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
	    if(isInCity){
	    	area.setCity_id(chars.toString());
	    }
	    else if(isInId){
	    	area.setArea_id(chars.toString());
	    }
	    else if(isInName){
	    	area.setArea_name(chars.toString());
	    }
	    else if(isInType){
	    	area.setType(chars.toString());
	    }
	    else if(isInSyncId){
	    	area.setSync_id(chars.toString());
	    }
	    else if(isInActive){
	    	area.setActive(chars.toString());
	    } 
	    else if(isInCreatedDate){
	    	area.setCreatedDate(chars.toString());
	    } 	
	}
	
	public ArrayList<Area> getData(){
		return this.areaList;
	}
	
	public void printData(){
		for(int i=0; i<areaList.size();i++){
			// print here
		}
	}
}
