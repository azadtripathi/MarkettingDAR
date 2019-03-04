package com.dm.parser;

import android.content.Context;
import android.util.Log;

import com.dm.model.GetResponse;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class ResponseDataHandler extends DefaultHandler{
	private ArrayList<GetResponse> getResponses = new ArrayList<GetResponse>();
	private GetResponse getResponse;
	private boolean isInGetStatus = false;
	private boolean isInResponse = false;	
	Context context;
	public ResponseDataHandler(Context context)
	{
		this.context=context;
	}
	
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("GetStatus")){
		   isInGetStatus = true;		   
		   getResponse = new GetResponse();
	   }
	   if(name.equalsIgnoreCase("Response")){
		   isInResponse = true;
	   }	   
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("GetStatus")){
			isInGetStatus = false;
			getResponses.add(getResponse);
		}
		if(name.equalsIgnoreCase("Response")){
			isInResponse = false;
		}
	}

	//element content
	public void characters (char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInResponse){
	    	getResponse.setStatus(chars.toString());
	    	System.out.println("collection status="+chars.toString());
	    	
	        }
	}
	
	public ArrayList<GetResponse> getData(){
		return this.getResponses;
	}
	
}