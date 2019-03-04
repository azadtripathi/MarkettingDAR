package com.dm.parser;

import com.dm.model.Enviro;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;


public class RecieptData extends DefaultHandler  {

	Boolean currentElement = false;
    String currentValue = "";
    Enviro enviro = null;
    private ArrayList<Enviro> itemsList = new ArrayList<Enviro>();
    public ArrayList<Enviro> getRecieptData() {
        return itemsList;
    }
    // Called when tag starts 
    @Override
    public void startElement(String uri, String localName, String qName,
            Attributes attributes) throws SAXException {
 
        currentElement = true;
        currentValue = "";
        if (localName.equals("Sman")) {
        	enviro = new Enviro();
        } 
 
    }
    
    // Called when tag closing 
    @Override
    public void endElement(String uri, String localName, String qName)
    throws SAXException {
 
        currentElement = false;
 
        /** set value */
        if (localName.equalsIgnoreCase("ReceiptNo"))
        	enviro.setReciep_no(currentValue);
        else if (localName.equalsIgnoreCase("Sman"))
            itemsList.add(enviro);
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
