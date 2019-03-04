package com.dm.parser;

import android.util.Log;

import com.dm.model.ResponsbilityCentre;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class ResponsbilityCentreDataHandler extends DefaultHandler{
	private ArrayList<ResponsbilityCentre> responsbilitycentreList = new ArrayList<ResponsbilityCentre>();
	private ResponsbilityCentre responsbilitycentre;

	private boolean isInResponsbilityCentre = false;
	private boolean isInId = false;
	private boolean isInDescription = false;
	
	
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("ResponsibilityCentre")){
		   isInResponsbilityCentre = true;		   
		   responsbilitycentre = new ResponsbilityCentre();
	   }
	   if(name.equalsIgnoreCase("Id")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("Description")){
		   isInDescription = true;
	   }
	   
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("responsibilityCentre")){
			isInResponsbilityCentre = false;
			responsbilitycentreList.add(responsbilitycentre);
		}
		if(name.equalsIgnoreCase("Id")){
			isInId = false;
		}
		if(name.equalsIgnoreCase("description")){
			isInDescription = false;
		}
		
	}

	//element content
	public void characters (char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInId){
	    	responsbilitycentre.setResponsbilitycentre_id(chars.toString());
	    }else if(isInDescription){
	    	responsbilitycentre.setResponsbilitycentre_name(chars.toString());
	    }
	    	
	}
	
	public ArrayList<ResponsbilityCentre> getData(){
		return this.responsbilitycentreList;
	}
	
}