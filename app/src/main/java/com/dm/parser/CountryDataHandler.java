/**
 * AreaHandler.javacom.example.Handler
 */
package com.dm.parser;

/**
 * @author dataman
 *
 */

import android.util.Log;

import com.dm.model.Country;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class CountryDataHandler extends DefaultHandler{
	private ArrayList<Country> countries=new ArrayList<Country>();
	private Country country;
	private boolean isInCountry = false;
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
	   if(name.equalsIgnoreCase("Country")){
		   isInCountry = true;		   
		   country = new Country();
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
		if(name.equalsIgnoreCase("Country")){
			   isInCountry = false;		   
			   countries.add(country);
		   }
		   if(name.equalsIgnoreCase("Id")){
			   isInId = false;
		   }
		   if(name.equalsIgnoreCase("Description")){
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
		   country.setId(chars.toString());
	    }else if(isInDescription){
	    	country.setDescription(chars.toString());
	    } else if(isInSyncId){
	    	country.setSync_id(chars.toString());
	    }
	    else if(isInActive){
	    	country.setActive(chars.toString());
	    } 
	    else if(isInCreatedDate){
	    	country.setCreatedDate(chars.toString());
	    } 		  	
	}
	
	public ArrayList<Country> getData(){
		return this.countries;
	}
	
	public void printData(){
		for(int i=0; i<countries.size();i++){
			// print here
		}
	}
}
