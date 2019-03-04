package com.dm.parser;

import android.util.Log;

import com.dm.model.State;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;


public class StateDataHandler extends DefaultHandler{
	private ArrayList<State> statesList = new ArrayList<State>();
	private State state;

	private boolean isInState = false;
	private boolean isInId = false;
	private boolean isInDescription = false;
	private boolean isInRegionId = false;
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
	   if(name.equalsIgnoreCase("State")){
		   isInState= true;		   
		   state = new State();
	   }
	   if(name.equalsIgnoreCase("Id")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("Description")){
		   isInDescription = true;
	   }
	   if(name.equalsIgnoreCase("region_id")){
		   isInRegionId = true;
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
		if(name.equalsIgnoreCase("state")){
			isInState = false;
			 statesList.add(state);
		}
		if(name.equalsIgnoreCase("Id")){
			isInId = false;
		}
		if(name.equalsIgnoreCase("description")){
			isInDescription = false;
		}
		if(name.equalsIgnoreCase("region_id")){
			isInRegionId = false;
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
	    	state.setState_id((chars.toString()));
	    }else if(isInDescription){
	    	state.setState_name(chars.toString());
	    }else if(isInRegionId){
	    	state.setState_region_id(chars.toString());
	    }else if(isInSyncId){
	    	state.setSync_id(chars.toString());
	    }
	    else if(isInActive){
	    	state.setActive(chars.toString());
	    } 
	    else if(isInCreatedDate){
	    	state.setCreatedDate(chars.toString());
	    } 	 		
	}
	
	public ArrayList<State> getData(){
		return this.statesList;
	}
	
}