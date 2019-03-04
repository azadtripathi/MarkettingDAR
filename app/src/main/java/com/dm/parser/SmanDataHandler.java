/**
 * smanHandler.javacom.example.Handler
 */
package com.dm.parser;

/**
 * @author dataman
 *
 */

import android.util.Log;

import com.dm.model.Sman;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class SmanDataHandler extends DefaultHandler{
	private ArrayList<Sman> smanList = new ArrayList<Sman>();
	private Sman sman;

	private boolean isInSman = false;
	private boolean isInSmId = false;
	private boolean isInSM_Name = false;
	private boolean isInRole = false;
	private boolean isInUnderId = false;
	private boolean isInDisplayName = false;
	private boolean isInLevel = false;
	private boolean isInSrType = false;
	private boolean isInCreatedDate = false;
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("Sman")){
		   isInSman = true;		   
		   sman = new Sman();
	   }
	   if(name.equalsIgnoreCase("SMId")){
		   isInSmId = true;
	   }
	   if(name.equalsIgnoreCase("SMName")){
		   isInSM_Name = true;
	   }
	   if(name.equalsIgnoreCase("RoleId")){
		   isInRole = true;
	   }
	   if(name.equalsIgnoreCase("UnderId")){
		   isInUnderId = true;
	   }
	   if(name.equalsIgnoreCase("DisplayName")){
		   isInDisplayName = true;
	   }
	   if(name.equalsIgnoreCase("SalesRepType")){
		   isInSrType = true;
	   }
	   if(name.equalsIgnoreCase("Lvl")){
		   isInLevel = true;
	   }
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   }
	   
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("Sman")){
			   isInSman = false;		   
			   smanList.add(sman);
		   }
		if(name.equalsIgnoreCase("SMId")){
			   isInSmId = false;
		   }
		   if(name.equalsIgnoreCase("SMName")){
			   isInSM_Name = false;
		   }
		   if(name.equalsIgnoreCase("RoleId")){
			   isInRole = false;
		   }
		   if(name.equalsIgnoreCase("UnderId")){
			   isInUnderId = false;
		   }
		   if(name.equalsIgnoreCase("DisplayName")){
			   isInDisplayName = false;
		   }
		   if(name.equalsIgnoreCase("SalesRepType")){
			   isInSrType = false;
		   }
		   if(name.equalsIgnoreCase("Lvl")){
			   isInLevel = false;
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
	    if(isInSmId){
	    	sman.setConPerId(chars.toString());
	    }else if(isInSM_Name){
	    	sman.setUser_Name(chars.toString());
	    }
	    else if(isInUnderId){
	    	sman.setUnderId(chars.toString());
	    }
	    else if(isInRole){
	    	sman.setRoleId(chars.toString());
	    }
	    else if(isInDisplayName){
	    	sman.setDisplayName(chars.toString());
	    }
	    else if(isInSrType){
	    	sman.setSrType(chars.toString());
	    }
	    else if(isInLevel){
	    	sman.setLevel(chars.toString());
	    } 
	    else if(isInCreatedDate){
	    	sman.setCreatedDate(chars.toString());
	    } 
	    
	}
	
	public ArrayList<Sman> getData(){
		return this.smanList;
	}
	
	public void printData(){
		for(int i=0; i<smanList.size();i++){
			// print here
		}
	}
}
