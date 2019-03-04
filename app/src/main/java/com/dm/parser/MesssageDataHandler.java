/**
 * AreaHandler.javacom.example.Handler
 */
package com.dm.parser;

/**
 * @author dataman
 *
 */

import android.util.Log;

import com.dm.model.Message;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class MesssageDataHandler extends DefaultHandler{
	private ArrayList<Message> messages = new ArrayList<Message>();
	private Message message;

	private boolean isInsms = false;
	private boolean isInsmsclientid = false;
	private boolean isInmessageid = false;
	
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("sms")){
		   isInsms = true;		   
		   message = new Message();
	   }
	   if(name.equalsIgnoreCase("smsclientid")){
		   isInsmsclientid = true;
	   }
	   if(name.equalsIgnoreCase("messageid")){
		   isInmessageid = true;
	   }
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("sms")){
			isInsms = false;
			messages.add(message);
		}
		if(name.equalsIgnoreCase("smsclientid")){
			isInsmsclientid = false;
		}
		if(name.equalsIgnoreCase("messageid")){
			isInmessageid = false;
		}
	}

	//element content
	public void characters (char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInsmsclientid){
	    	message.setSmsclientid(chars.toString());
	    }else if(isInmessageid){
	    	message.setMessageid(chars.toString());
	    }
	    	
	}
	
	public ArrayList<Message> getData(){
		return this.messages;
	}
	
	public void printData(){
		for(int i=0; i<messages.size();i++){
			// print here
		}
	}
}
