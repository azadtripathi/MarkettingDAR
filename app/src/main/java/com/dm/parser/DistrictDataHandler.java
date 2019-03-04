package com.dm.parser;

import android.util.Log;

import com.dm.model.District;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class DistrictDataHandler extends DefaultHandler{
    private ArrayList<District> districtList = new ArrayList<District>();
    private District district;

    private boolean isInDistrict = false;
    private boolean isInId = false;
    private boolean isInDescription = false;
    private boolean isInStateId = false;
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
       if(name.equalsIgnoreCase("District")){
           isInDistrict = true;
           district = new District();
       }
       if(name.equalsIgnoreCase("Id")){
           isInId = true;
       }
       if(name.equalsIgnoreCase("Description")){
           isInDescription = true;
       }
       if(name.equalsIgnoreCase("StateID")){
           isInStateId = true;
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
        if(name.equalsIgnoreCase("District")){
            isInDistrict = false;
            districtList.add(district);
        }
        if(name.equalsIgnoreCase("Id")){
            isInId = false;
        }
        if(name.equalsIgnoreCase("description")){
            isInDescription = false;
        }
        if(name.equalsIgnoreCase("StateID")){
               isInStateId = false;
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
            district.setDistrict_id(chars.toString());
        }else if(isInDescription){
            district.setDistrict_name(chars.toString());
        }else if(isInStateId){
            district.setState_id(chars.toString());
        }else if(isInSyncId){
            district.setSync_id(chars.toString());
        }
        else if(isInActive){
            district.setActive(chars.toString());
        }
        else if(isInCreatedDate){
            district.setCreatedDate(chars.toString());
        }
    }

    public ArrayList<District> getData(){
        return this.districtList;
    }

}
	

