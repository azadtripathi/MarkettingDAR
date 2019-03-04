package com.dm.parser;

import android.util.Log;

import com.dm.model.Beat;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class BeatDataHandler extends DefaultHandler{
	private ArrayList<Beat> beatList = new ArrayList<Beat>();
	private Beat beat;

	private boolean isInBeat = false;
	private boolean isInId = false;
	private boolean isInDescription = false;
	private boolean isInAreaId = false;
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
	   if(name.equalsIgnoreCase("Beat")){
		   isInBeat = true;		   
		   beat = new Beat();
	   }
	   if(name.equalsIgnoreCase("Id")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("Description")){
		   isInDescription = true;
	   }
	   if(name.equalsIgnoreCase("AreaID")){
		   isInAreaId = true;
	   }
	   if(name.equalsIgnoreCase("SyncID")){
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
		if(name.equalsIgnoreCase("beat")){
			isInBeat = false;
			beatList.add(beat);
		}
		if(name.equalsIgnoreCase("Id")){
			isInId = false;
		}
		if(name.equalsIgnoreCase("description")){
			isInDescription = false;
		}
		if(name.equalsIgnoreCase("AreaID")){
			isInAreaId = false;
		}
		 if(name.equalsIgnoreCase("SyncID")){
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
	    	beat.setBeat_id(chars.toString());
	    }else if(isInDescription){
	    	beat.setBeat_name(chars.toString());
	    }else if(isInAreaId){
	    	beat.setArea_id(chars.toString());
	    }else if(isInSyncId){
	    	beat.setSync_id(chars.toString());
	    }
	    else if(isInActive){
	    	beat.setActive(chars.toString());
	    } 
	    else if(isInCreatedDate){
	    	beat.setCreatedDate(chars.toString());
	    } 	 	
	}
	
	public ArrayList<Beat> getData(){
		return this.beatList;
	}
	
}