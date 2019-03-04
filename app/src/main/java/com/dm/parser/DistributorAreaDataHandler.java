package com.dm.parser;

import android.util.Log;

import com.dm.model.DistributorArea;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class DistributorAreaDataHandler extends DefaultHandler{
	private ArrayList<DistributorArea> distributorareaList = new ArrayList<DistributorArea>();
	private DistributorArea distributorArea;

	private boolean isInDistributorArea = false;
	private boolean isIndistributor_id = false;
	private boolean isInarea_id = false;
	
	
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("DistributorArea")){
		   isInDistributorArea = true;		   
		   distributorArea = new DistributorArea();
	   }
	   if(name.equalsIgnoreCase("distributor_id")){
		   isIndistributor_id = true;
	   }
	   if(name.equalsIgnoreCase("area_id")){
		   isInarea_id = true;
	   }
	   
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("distributorarea")){
			isInDistributorArea = false;
			distributorareaList.add(distributorArea);
		}
		if(name.equalsIgnoreCase("distributor_id")){
			isIndistributor_id = false;
		}
		if(name.equalsIgnoreCase("area_id")){
			isInarea_id = false;
		}
		
	}

	//element content
	public void characters (char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isIndistributor_id){
	    	distributorArea.setDistributor_id((chars.toString()));
	    }else if(isInarea_id){
	    	distributorArea.setArea_id(chars.toString());
	    }
	    	
	}
	
	public ArrayList<DistributorArea> getData(){
		return this.distributorareaList;
	}
	
}