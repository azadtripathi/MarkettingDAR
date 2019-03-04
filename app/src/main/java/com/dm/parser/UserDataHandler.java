package com.dm.parser;

import android.util.Log;

import com.dm.model.User;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class UserDataHandler extends DefaultHandler{
	private ArrayList<User> userList = new ArrayList<User>();
	private User user;

	private boolean isInUser = false;
	private boolean isInConPerId = false;
	private boolean isInUserId = false;
	private boolean isInLevel = false;
	private boolean isInReportingPerson = false;
	private boolean isInUser_Name=false;
	private boolean isInPassWd=false;
	private boolean isInRoleId=false;
	private boolean isInDesigId=false;
	private boolean isInDeptId=false;
	private boolean isInEmail=false;
	private boolean isInPdaId=false;
	private boolean isInDsrAllowDays=false;
	private boolean isInDisplayName=false;
	private boolean isInActiveYN=false;
	
	
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("User")){
		   isInUser = true;		   
		   user= new User();
	   }
	   if(name.equalsIgnoreCase("ConPerId")){
		   isInConPerId = true;
	   }
	   if(name.equalsIgnoreCase("UserId")){
		   isInUserId = true;
	   }
	   if(name.equalsIgnoreCase("Level")){
		   isInLevel = true;
	   }
	   if(name.equalsIgnoreCase("ReportingPerson")){
		   isInReportingPerson = true;
	   }
	   if(name.equalsIgnoreCase("User_Name")){
		   isInUser_Name = true;
	   }
	   if(name.equalsIgnoreCase("PassWd")){
		   isInPassWd = true;
	   }
	   if(name.equalsIgnoreCase("RoleId")){
		   isInRoleId = true;
	   }
	   if(name.equalsIgnoreCase("DeptId")){
		   isInDeptId = true;
	   }
	   if(name.equalsIgnoreCase("DesigId")){
		   isInDesigId = true;
	   }
	   if(name.equalsIgnoreCase("Email")){
		   isInEmail = true;
	   }
	   
	   if(name.equalsIgnoreCase("PDA_Id")){
		   isInPdaId = true;
	   }
	   if(name.equalsIgnoreCase("DSRAllowDays")){
		   isInDsrAllowDays = true;
	   }
	   if(name.equalsIgnoreCase("DisplayName")){
		   isInDisplayName = true;
	   }
	   if(name.equalsIgnoreCase("ActiveYN")){
		   isInActiveYN = true;
	   }
	   
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("user")){
			isInUser = false;
			userList.add(user);
		}
		//conper_id is salesman_id
		 if(name.equalsIgnoreCase("ConPerId")){
			   isInConPerId = false;
		   }
		   if(name.equalsIgnoreCase("Level")){
			   isInLevel = false;
		   }
		   if(name.equalsIgnoreCase("UserId")){
			   isInUserId = false;
		   }
		   if(name.equalsIgnoreCase("ReportingPerson")){
			   isInReportingPerson = false;
		   }
		   if(name.equalsIgnoreCase("User_Name")){
			   isInUser_Name = false;
		   }
		   if(name.equalsIgnoreCase("PassWd")){
			   isInPassWd = false;
		   }
		   if(name.equalsIgnoreCase("Rank")){
			   isInRoleId = false;
		   }
		   if(name.equalsIgnoreCase("DeptId")){
			   isInDeptId = false;
		   }
		   if(name.equalsIgnoreCase("DesigId")){
			   isInDesigId = false;
		   }
		   if(name.equalsIgnoreCase("Email")){
			   isInEmail = false;
		   }
		   if(name.equalsIgnoreCase("PDA_Id")){
			   isInPdaId = false;
		   }
		   if(name.equalsIgnoreCase("DSRAllowDays")){
			   isInDsrAllowDays = false;
		   }
		   if(name.equalsIgnoreCase("DisplayName")){
			   isInDisplayName = false;
		   }
		   if(name.equalsIgnoreCase("ActiveYN")){
			   isInActiveYN = false;
		   }
	}

	//element content
	public void characters (char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInConPerId){
	    	user.setConper_id(chars.toString());
	    }else if(isInLevel){
	    	user.setLevel(chars.toString());
	    }
	    else if(isInUser_Name){
	    	user.setUser_name(chars.toString());
	    }
	    else if(isInUserId){
	    	user.setUserId(chars.toString());
	    }
	    else if(isInPassWd){
	    	user.setPassword(chars.toString());
	    }
	   
	    else if(isInPdaId){
	    	user.setPDA_Id(chars.toString());
	    }
	    else if(isInDsrAllowDays){
	    	user.setDSRAllowDays(Integer.parseInt(chars.toString()));
	    }
	    else if(isInRoleId){
	    	user.setRoleId(chars.toString());
	    }
	    else if(isInDeptId){
	    	user.setDeptId(chars.toString());
	    }
	    else if(isInDesigId){
	    	user.setDesigId(chars.toString());
	    }
	    else if(isInEmail){
	    	user.setEmail(chars.toString());
	    }	
	    
	}
	
	public ArrayList<User> getData(){
		return this.userList;
	}
	
}