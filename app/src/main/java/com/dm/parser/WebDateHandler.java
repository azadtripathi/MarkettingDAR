package com.dm.parser;

import com.dm.model.WebDate;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class WebDateHandler extends DefaultHandler {

	Boolean currentElement = false;
    String currentValue = "";
    WebDate item = null;
    private ArrayList<WebDate> itemsList = new ArrayList<WebDate>();
    public ArrayList<WebDate> getItems() {
        return itemsList;
    }
    // Called when tag starts 
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
 
        currentElement = true;
        currentValue = "";
        if (localName.equals("GetValue")) {
            item = new WebDate();
        } 
 
    }
    
    // Called when tag closing 
    @Override
    public void endElement(String uri, String localName, String qName)
    throws SAXException {
 
        currentElement = false;
 
        /** set value */
        if (localName.equalsIgnoreCase("Value"))
            item.setWdate(currentValue);
        else if (localName.equalsIgnoreCase("GetValue"))
            itemsList.add(item);
    }
 
    // Called to get tag characters 
    @Override
    public void characters(char[] ch, int start, int length)
    throws SAXException {
 
        if (currentElement) {
            currentValue = currentValue +  new String(ch, start, length);
        }
 
    }
}
