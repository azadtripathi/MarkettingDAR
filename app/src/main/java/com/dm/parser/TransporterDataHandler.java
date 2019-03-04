package com.dm.parser;

import android.util.Log;

import com.dm.model.Transporter;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class TransporterDataHandler extends DefaultHandler{
	private ArrayList<Transporter> transportList = new ArrayList<Transporter>();
	private Transporter transporter;

	private boolean isInTransporter = false;
	private boolean isInName = false;
	private boolean isInId = false;
	private boolean isInSyncId = false;
	private boolean isInCreatedDate = false;
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("Transporter")){
		   isInTransporter = true;		   
		   transporter = new Transporter();
	   }
	   if(name.equalsIgnoreCase("Id")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("Name")){
		   isInName = true;
	   }
	   if(name.equalsIgnoreCase("SyncID")){
		   isInSyncId = true;
	   }
	   
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   }
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("Transporter")){
			isInTransporter = false;
			transportList.add(transporter);
		}
		if(name.equalsIgnoreCase("Id")){
			isInId = false;
		}
		if(name.equalsIgnoreCase("Name")){
			   isInName = false;
		   }
		   if(name.equalsIgnoreCase("SyncID")){
			   isInSyncId = false;
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
	    	transporter.setTransport_id(chars.toString());
	    }else if(isInName){
	    	transporter.setTransport_name(chars.toString());
	    }
	    else if(isInSyncId){
	    	transporter.setSync_id(chars.toString());
	    }
	    
	    else if(isInCreatedDate){
	    	transporter.setCreatedDate(chars.toString());
	    } 	 	 	
	}
	
	public ArrayList<Transporter> getData(){
		return this.transportList;
	}
	

	
}
