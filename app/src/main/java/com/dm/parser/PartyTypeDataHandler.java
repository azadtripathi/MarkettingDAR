/**
 * AreaHandler.javacom.example.Handler
 */
package com.dm.parser;

/**
 * @author dataman
 *
 */

import android.util.Log;

import com.dm.model.PartyType;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class PartyTypeDataHandler extends DefaultHandler{
	private ArrayList<PartyType> partyTypes = new ArrayList<PartyType>();
	private PartyType partyType;

	private boolean isInPartyType = false;
	private boolean isInId = false;
	private boolean isInName = false;
	private boolean isInCreatedDate = false;
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("PartyType")){
		   isInPartyType = true;		   
		   partyType = new PartyType();
	   }
	   if(name.equalsIgnoreCase("Id")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("Name")){
		   isInName = true;
	   }
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   }
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("PartyType")){
			isInPartyType = false;
			partyTypes.add(partyType);
		}
		if(name.equalsIgnoreCase("Id")){
			isInId = false;
		}
		if(name.equalsIgnoreCase("Name")){
			isInName = false;
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
		   partyType.setId(chars.toString());
	    }else if(isInName){
	    	partyType.setName(chars.toString());
	    }
	    else if(isInCreatedDate){
	    	partyType.setCreatedDate(chars.toString());
	    }
	    	
	}
	
	public ArrayList<PartyType> getData(){
		return this.partyTypes;
	}
	
	public void printData(){
		for(int i=0; i<partyTypes.size();i++){
			// print here
		}
	}
}
