package com.dm.parser;

import android.util.Log;

import com.dm.model.City;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class CityDataHandler extends DefaultHandler{
	private ArrayList<City> cityList = new ArrayList<City>();
	private City city;

	private boolean isInCity = false;
	private boolean isInId = false;
	private boolean isInDescription = false;
	private boolean isInDistrict = false;
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
	   if(name.equalsIgnoreCase("City")){
		   isInCity = true;		   
		   city = new City();
	   }
	   if(name.equalsIgnoreCase("Id")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("Description")){
		   isInDescription = true;
	   }
	   if(name.equalsIgnoreCase("DistrictID")){
		   isInDistrict = true;
	   }if(name.equalsIgnoreCase("SyncId")){
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
		if(name.equalsIgnoreCase("city")){
			isInCity = false;
			cityList.add(city);
		}
		if(name.equalsIgnoreCase("Id")){
			isInId = false;
		}
		if(name.equalsIgnoreCase("description")){
			isInDescription = false;
		}
		if(name.equalsIgnoreCase("DistrictID")){
			isInDistrict = false;
		}if(name.equalsIgnoreCase("SyncId")){
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
	    	city.setCity_id((chars.toString()));
	    }else if(isInDescription){
	    	city.setCity_name(chars.toString());
	    }else if(isInDistrict){
	    	city.setDistrict_id(chars.toString());
	    }else if(isInSyncId){
	    	city.setSync_id(chars.toString());
	    }
	    else if(isInActive){
	    	city.setActive(chars.toString());
	    } 
	    else if(isInCreatedDate){
	    	city.setCreatedDate(chars.toString());
	    } 		
	}
	
	public ArrayList<City> getData(){
		return this.cityList;
	}
	
}