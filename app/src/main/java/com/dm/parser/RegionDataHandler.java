package com.dm.parser;

import android.util.Log;

import com.dm.model.Region;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;


public class RegionDataHandler extends DefaultHandler{
    private ArrayList<Region> regionList = new ArrayList<Region>();
    private Region region;

    private boolean isInRegion = false;
    private boolean isInId = false;
    private boolean isInDescription = false;
    private boolean isInCountryId = false;
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
       if(name.equalsIgnoreCase("Region")){
           isInRegion = true;
           region = new Region();
       }
       if(name.equalsIgnoreCase("Id")){
           isInId = true;
       }
       if(name.equalsIgnoreCase("Description")){
           isInDescription = true;
       }
       if(name.equalsIgnoreCase("CountryID")){
           isInCountryId = true;
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
        if(name.equalsIgnoreCase("Region")){
            isInRegion = false;
            regionList.add(region);
        }
        if(name.equalsIgnoreCase("Id")){
            isInId = false;
        }
        if(name.equalsIgnoreCase("description")){
            isInDescription = false;
        }
        if(name.equalsIgnoreCase("CountryID")){
               isInCountryId = false;
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
            region.setRegion_id(chars.toString());
        }else if(isInDescription){
            region.setRegion_name(chars.toString());
        }else if(isInCountryId){
            region.setCountry_id(chars.toString());
        }else if(isInSyncId){
            region.setSync_id(chars.toString());
        }
        else if(isInActive){
            region.setActive(chars.toString());
        }
        else if(isInCreatedDate){
            region.setCreatedDate(chars.toString());
        }

    }

    public ArrayList<Region> getData(){
        return this.regionList;
    }


}
