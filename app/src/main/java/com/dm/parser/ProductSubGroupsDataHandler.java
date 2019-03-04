package com.dm.parser;

import android.util.Log;

import com.dm.model.ProductSubGroup;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class ProductSubGroupsDataHandler extends DefaultHandler{
	private ArrayList<ProductSubGroup> productsubgroupList = new ArrayList<ProductSubGroup>();
	private ProductSubGroup productsubgroup;

	private boolean isInProductSubGroup = false;
	private boolean isInId = false;
	private boolean isInDescription = false;
	private boolean isInProductGroup_Id = false;
	
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("ProductSubGroup")){
		   isInProductSubGroup = true;		   
		   productsubgroup = new ProductSubGroup();
	   }
	   if(name.equalsIgnoreCase("Id")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("Description")){
		   isInDescription = true;
	   }
	   if(name.equalsIgnoreCase("ProductGroup_Id")){
		   isInProductGroup_Id = true;
	   }
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("productsubgroup")){
			isInProductSubGroup = false;
			productsubgroupList.add(productsubgroup);
		}
		if(name.equalsIgnoreCase("Id")){
			isInId = false;
		}
		if(name.equalsIgnoreCase("description")){
			isInDescription = false;
		}
		if(name.equalsIgnoreCase("ProductGroup_Id")){
			isInProductGroup_Id = false;
		}
	}

	//element content
	public void characters (char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInId){
	    	productsubgroup.setSubgroup_id((chars.toString()));
	    }else if(isInDescription){
	    	productsubgroup.setSubgroup_name(chars.toString());
	    }else if(isInProductGroup_Id){
	    	productsubgroup.setGroup_id(chars.toString());
	    }
	    	
	}
	
	public ArrayList<ProductSubGroup> getData(){
		return this.productsubgroupList;
	}
	
}