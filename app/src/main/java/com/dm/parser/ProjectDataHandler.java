package com.dm.parser;

import android.util.Log;

import com.dm.model.Project;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;


public class ProjectDataHandler extends DefaultHandler{
	private ArrayList<Project> projectList = new ArrayList<Project>();
	private Project project;

	private boolean isInProject = false;
	private boolean isInName = false;
	private boolean isInId = false;
	private boolean isInActive = false;
	private boolean isInCreatedDate = false;
	
	
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("Project")){
		   isInProject = true;		   
		   project = new Project();
	   }
	   if(name.equalsIgnoreCase("Id")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("Name")){
		   isInName = true;
	   }
	   if(name.equalsIgnoreCase("Active")){
		   isInActive = true;
	   }
	   if(name.equalsIgnoreCase("Createddate")){
		   isInCreatedDate = true;
	   }
	  
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("Project")){
			isInProject = false;
			projectList.add(project);
		}
		if(name.equalsIgnoreCase("Id")){
			isInId = false;
		}
		if(name.equalsIgnoreCase("Name")){
			isInName = false;
		   }
		   if(name.equalsIgnoreCase("Active")){
			   isInActive = false;
		   }
		   if(name.equalsIgnoreCase("Createddate")){
			   isInCreatedDate = false;
		   } 
		   
	}

	//element content
	public void characters (char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInId){
	    	project.setProjectid(chars.toString());
	    }else if(isInName){
	    	project.setProjectname(chars.toString());
	    }
	    else if(isInActive){
	    	project.setActive(chars.toString());
	    }
	    else if(isInCreatedDate){
	    	project.setCreatedDate(chars.toString());
	    }
	   	 	 	
	}
	
	public ArrayList<Project> getData(){
		return this.projectList;
	}
	


	
	
}
