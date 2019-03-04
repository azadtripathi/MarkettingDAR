package com.dm.parser;

import android.util.Log;

import com.dm.model.AreaType;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class AreaTypeDataHandler extends DefaultHandler{
	private ArrayList<AreaType> areatypeList = new ArrayList<AreaType>();
	private AreaType areatype;
	private boolean isInAreaType = false;
	private boolean isInId = false;
	private boolean isInDescription = false;
	
	
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("Areatype")){
		   isInAreaType = true;		   
		   areatype = new AreaType();
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
		if(name.equalsIgnoreCase("areatype")){
			isInAreaType = false;
			areatypeList.add(areatype);
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
	    	areatype.setArea_type_id(chars.toString());
	    }else if(isInDescription){
	    	areatype.setArea_type_name(chars.toString());
	    }
	    	
	}
	
	public ArrayList<AreaType> getData(){
		return this.areatypeList;
	}
	
}