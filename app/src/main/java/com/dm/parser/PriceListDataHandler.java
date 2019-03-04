package com.dm.parser;

import android.util.Log;

import com.dm.model.PriceList;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class PriceListDataHandler extends DefaultHandler{
	private ArrayList<PriceList> pricelistList = new ArrayList<PriceList>();
	private PriceList pricelist;

	private boolean isInPriceList = false;
	private boolean isInId = false;
	private boolean isInWef_Date = false;
	private boolean isInItem_Id = false;
	private boolean isInMRP = false;
	private boolean isInDP = false;
	private boolean isInRP = false;
	private boolean isInProductSubGroup_Id = false;
	private boolean isInProductGroup_Id = false;
	private boolean isInCreatedDate = false;
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("PriceList")){
		   isInPriceList = true;		   
		   pricelist = new PriceList();
	   }
	   if(name.equalsIgnoreCase("id")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("WefDate")){
		   isInWef_Date = true;
	   }
	   if(name.equalsIgnoreCase("MRP")){
		   isInMRP = true;
	   }
	   if(name.equalsIgnoreCase("DP")){
		   isInDP = true;
	   }
	   if(name.equalsIgnoreCase("RP")){
		   isInRP = true;
	   }
	   if(name.equalsIgnoreCase("ProdGrpId")){
		   isInProductGroup_Id = true;
	   }
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   } 
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("PriceList")){
			isInPriceList = false;
			pricelistList.add(pricelist);
		}
		if(name.equalsIgnoreCase("id")){
			   isInId = false;
		   }
		   if(name.equalsIgnoreCase("WefDate")){
			   isInWef_Date = false;
		   }
		   if(name.equalsIgnoreCase("MRP")){
			   isInMRP = false;
		   }
		   if(name.equalsIgnoreCase("DP")){
			   isInDP = false;
		   }
		   if(name.equalsIgnoreCase("RP")){
			   isInRP = false;
		   }
		   if(name.equalsIgnoreCase("ProdGrpId")){
			   isInProductGroup_Id = false;
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
	    	pricelist.setPricelist_id(Integer.parseInt(chars.toString()));
	    }else if(isInWef_Date){
	    	pricelist.setList_date(chars.toString());
	    }
	    else if(isInItem_Id){
	    	pricelist.setItem_id(chars.toString());
	    }
	    else if(isInMRP){
	    	pricelist.setMrp(chars.toString());
	    }
	    else if(isInDP){
	    	pricelist.setDp(chars.toString());
	    }
	    else if(isInRP){
	    	pricelist.setRp(chars.toString());
	    }
	    
	    else if(isInProductGroup_Id){
	    	pricelist.setProductGroup_Id(chars.toString());
	    }  
	    else if(isInCreatedDate){
	    	pricelist.setCreatedDate(chars.toString());
	    } 	
	}
	
	public ArrayList<PriceList> getData(){
		return this.pricelistList;
	}
	
}