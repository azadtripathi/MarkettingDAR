package com.dm.parser;

import android.util.Log;

import com.dm.model.Enviro;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class EnviroDataHandler extends DefaultHandler {

	private ArrayList<Enviro> enviroList = new ArrayList<Enviro>();
	private Enviro enviro;

	private boolean isInAndroidEnviro = false;	
	private boolean isInId = false;	
	private boolean isInSmId = false;	
	private boolean isInOrderNo = false;	
	private boolean isInDemoNo = false;	
	private boolean isInFailedNo = false;	
	private boolean isInCompId = false;	
	private boolean isInPorderNo = false;	
	private boolean isInPorder1No = false;	
	private boolean isInDistCollNo = false;	
	private boolean isInVisitNo = false;	
	private boolean isInDisNo = false;
	private boolean isInPartyNo = false;
	private boolean isInPartCollNo = false;
	private boolean isInLeaveNo = false;
	private boolean isInBeatNo = false;
	private boolean isInOrder1No = false;
	
	//start of the XML document
		public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

		//end of the XML document
		public void endDocument () { Log.i("DataHandler", "End of XML document"); }

		//opening element tag
		public void startElement (String uri, String name, String qName, Attributes atts)
		{
		   if(name.equalsIgnoreCase("AndroidEnviro")){
			   isInAndroidEnviro = true;		   
			   enviro = new Enviro();
		   }
		   if(name.equalsIgnoreCase("Id")){
			   isInId = true;
		   }
		   if(name.equalsIgnoreCase("SMID")){
			   isInSmId = true;
		   }
		   if(name.equalsIgnoreCase("A_OrderNo")){
			   isInOrderNo = true;
		   }
		   if(name.equalsIgnoreCase("A_DemoNo")){
			   isInDemoNo = true;
		   }
		   if(name.equalsIgnoreCase("A_FailedVisitNo")){
			   isInFailedNo = true;
		   }
		   if(name.equalsIgnoreCase("A_CompetitorNo")){
			   isInCompId = true;
		   }
		   if(name.equalsIgnoreCase("A_POrderNo")){
			   isInPorderNo = true;
		   }
		   if(name.equalsIgnoreCase("A_DistributorCollectionNo")){
			   isInDistCollNo = true;
		   }
		   if(name.equalsIgnoreCase("A_VisitNo")){
			   isInVisitNo = true;
		   }
		   if(name.equalsIgnoreCase("A_DiscussionNo")){
			   isInDisNo = true;
		   }
		   if(name.equalsIgnoreCase("A_PartyNo")){
			   isInPartyNo = true;
		   }
		   if(name.equalsIgnoreCase("A_PartyCollectionNo")){
			   isInPartCollNo = true;
		   }
		   if(name.equalsIgnoreCase("A_POrder1No")){
			   isInPorder1No = true;
		   }
		   if(name.equalsIgnoreCase("A_LeaveNo")){
			   isInLeaveNo = true;
		   }
		   if(name.equalsIgnoreCase("A_BeatPlanNo")){
			   isInBeatNo = true;
		   }
		   if(name.equalsIgnoreCase("A_Order1No")){
			   isInOrder1No = true;
		   }
	
		}
		
		//closing element tag
		public void endElement (String uri, String name, String qName)
		{
			   if(name.equalsIgnoreCase("AndroidEnviro")){
				   isInAndroidEnviro = false;		   
				   enviroList.add(enviro);
			   }
			   
			   if(name.equalsIgnoreCase("Id")){
				   isInId = false;
			   }
			   if(name.equalsIgnoreCase("SMID")){
				   isInSmId = false;
			   }
			   if(name.equalsIgnoreCase("A_OrderNo")){
				   isInOrderNo = false;
			   }
			   if(name.equalsIgnoreCase("A_DemoNo")){
				   isInDemoNo = false;
			   }
			   if(name.equalsIgnoreCase("A_FailedVisitNo")){
				   isInFailedNo = false;
			   }
			   if(name.equalsIgnoreCase("A_CompetitorNo")){
				   isInCompId = false;
			   }
			   if(name.equalsIgnoreCase("A_POrderNo")){
				   isInPorderNo = false;
			   }
			   if(name.equalsIgnoreCase("A_DistributorCollectionNo")){
				   isInDistCollNo = false;
			   }
			   if(name.equalsIgnoreCase("A_VisitNo")){
				   isInVisitNo = false;
			   }
			   if(name.equalsIgnoreCase("A_DiscussionNo")){
				   isInDisNo = false;
			   }
			   if(name.equalsIgnoreCase("A_PartyNo")){
				   isInPartyNo = false;
			   }
			   if(name.equalsIgnoreCase("A_PartyCollectionNo")){
				   isInPartCollNo = false;
			   }	   
			   if(name.equalsIgnoreCase("A_POrder1No")){
				   isInPorder1No = false;
			   } 
			   if(name.equalsIgnoreCase("A_LeaveNo")){
				   isInLeaveNo = false;
			   }
			   if(name.equalsIgnoreCase("A_BeatPlanNo")){
				   isInBeatNo = false;
			   }
			   if(name.equalsIgnoreCase("A_Order1No")){
				   isInOrder1No = false;
			   }
			   
}

		//element content
		public void characters (char ch[], int start, int length)
		{
			String chars = new String(ch, start, length); 
		    chars = chars.trim();
		    if(isInOrderNo){
		    	enviro.setOrder_no(chars.toString());
		    }
		    else if(isInDemoNo){
		    	enviro.setDemo_no(chars.toString());
		    }
		    else if(isInDisNo){
		    	enviro.setDiscussionNo(chars.toString());
		    }
		    else if(isInDistCollNo){
		    	enviro.setDistCollectionNo(chars.toString());
		    }
		    else if(isInFailedNo){
		    	enviro.setFailed_No(chars.toString());
		    }
		    else if(isInCompId){
		    	enviro.setCompt_no(chars.toString());
		    }
		    else if(isInPartCollNo){
		    	enviro.setReciep_no(chars.toString());
		    } 
		    else if(isInPartyNo){
		    	enviro.setParty_no(chars.toString());
		    }
		    else if(isInPorderNo){
		    	enviro.setPorder_no(chars.toString());
		    }
		    else if(isInSmId){
		    	enviro.setSrep_code(chars.toString());
		    }
		    else if(isInVisitNo){
		    	enviro.setVisit_no(chars.toString());
		    }
		    else if(isInPorder1No){
		    	enviro.setPorder1_no(chars.toString());
		    }
		    else if(isInLeaveNo){
		    	enviro.setLeave_no(chars.toString());
		    }
		    else if(isInBeatNo){
		    	enviro.setBeatNo(chars.toString());
		    } 
		    else if(isInOrder1No){
		    	enviro.setOrder1_no(chars.toString());
		    } 
		    
		}
		
		public ArrayList<Enviro> getData(){
			return this.enviroList;
		}
		
}
