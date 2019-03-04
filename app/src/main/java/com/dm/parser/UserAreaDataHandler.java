package com.dm.parser;

import android.util.Log;

import com.dm.model.UserArea;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class UserAreaDataHandler extends DefaultHandler{
	private ArrayList<UserArea> userareaList = new ArrayList<UserArea>();
	private UserArea userarea;

	private boolean isInUserArea = false;
	private boolean isInConper_Id = false;
	private boolean isInArea_Id = false;
	private boolean isInCreatedDate = false;
	
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("UserArea")){
		   isInUserArea = true;		   
		   userarea = new UserArea();
	   }
	   if(name.equalsIgnoreCase("Conper_Id")){
		   isInConper_Id = true;
	   }
	   if(name.equalsIgnoreCase("Area_Id")){
		   isInArea_Id = true;
	   }
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   }
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("userarea")){
			isInUserArea = false;
			userareaList.add(userarea);
		}
		if(name.equalsIgnoreCase("Conper_Id")){
			isInConper_Id = false;
		}
		if(name.equalsIgnoreCase("Area_Id")){
			isInArea_Id = false;
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
	    if(isInConper_Id){
	    	userarea.setUser_id(chars.toString());
	    }else if(isInArea_Id){
	    	userarea.setArea_id(chars.toString());
	    }
	    else if(isInCreatedDate){
	    	userarea.setCreatedDate(chars.toString());
	    } 
	}
	
	public ArrayList<UserArea> getData(){
		return this.userareaList;
	}
	
}