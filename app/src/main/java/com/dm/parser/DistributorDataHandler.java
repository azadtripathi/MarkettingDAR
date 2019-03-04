package com.dm.parser;

import android.util.Log;

import com.dm.model.Distributor;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class DistributorDataHandler extends DefaultHandler{
	private ArrayList<Distributor> distributorList = new ArrayList<Distributor>();
	private Distributor distributor;

	private boolean isInDistributor = false;
	private boolean isInId = false;
	private boolean isInName1 = false;
	private boolean isInAddress_1 = false;
	private boolean isInAddress_2= false;
	private boolean isInCity_Id = false;
	private boolean isInContact = false;
	private boolean isInMobile = false;
	private boolean isInphone = false;
	private boolean isInOpenOrder= false;
	private boolean isInEMail = false;
	private boolean isInCreatedBy_Usr_id = false;
	private boolean isInCreated_Date = false;
	private boolean isInPin = false;
	private boolean isInBlocked_Reason = false;
	private boolean isInBlock_Date= false;
	private boolean isInAreaId= false;
	private boolean isInBeatId= false;
	private boolean isInIndustryId= false;
	private boolean isInPotential= false;
	private boolean isInPartyType= false;
	private boolean isInCst= false;
	private boolean isInVattin= false;
	private boolean isInServicetaxreg_No= false;
	private boolean isInPANNo= false;
	private boolean isInRemark= false;
	private boolean isInCreditLimit= false;
	private boolean isInOutStanding= false;
	private boolean isInPendingOrder= false;
	private boolean isInsync_id= false;
	private boolean isInActive= false;
	private boolean isInBlockedBy= false;
	private boolean isInPartyDist= false;
	private boolean isInUserId= false;
	private boolean isInLvl= false;
	private boolean isInLoginCreate= false;
	private boolean isInDisplayName= false;
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	  
		if(name.equalsIgnoreCase("Distributor")){
		   isInDistributor = true;		   
		   distributor = new Distributor();
	   }
	   if(name.equalsIgnoreCase("PartyId")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("PartyName")){
		   isInName1 = true;
	   }
	   if(name.equalsIgnoreCase("Address1")){
		   isInAddress_1 = true;
	   }
	   if(name.equalsIgnoreCase("Address2")){
		   isInAddress_2 = true;
	   }
	   if(name.equalsIgnoreCase("Pin")){
		   isInPin = true;
	   }
	   if(name.equalsIgnoreCase("AreaId")){
		   isInAreaId= true;
	   }
	   
	   if(name.equalsIgnoreCase("Email")){
		   isInEMail = true;
	   }
	   
	   if(name.equalsIgnoreCase("Mobile")){
		   isInMobile = true;
	   }
	   
	   if(name.equalsIgnoreCase("IndId")){
		   isInIndustryId = true;
	   }
	   if(name.equalsIgnoreCase("Potential")){
		   isInPotential = true;
	   }
	  
	   if(name.equalsIgnoreCase("Active")){
		   isInActive = true;
	   }
	   if(name.equalsIgnoreCase("BlockReason")){
		   isInBlocked_Reason = true;
	   }
	   if(name.equalsIgnoreCase("BlockDate")){
		   isInBlock_Date = true;
	   }
	   if(name.equalsIgnoreCase("BlockBy")){
		   isInBlockedBy = true;
	   }
	   if(name.equalsIgnoreCase("Remark")){
		   isInRemark = true;
	   }
	   if(name.equalsIgnoreCase("PartyDist")){
		   isInPartyDist = true;
	   }
	   if(name.equalsIgnoreCase("LoginCreated")){
		   isInLoginCreate = true;
	   }
	   if(name.equalsIgnoreCase("UserId")){
		   isInUserId = true;
	   }
	   if(name.equalsIgnoreCase("SyncId")){
		   isInsync_id = true;
	   }
	   if(name.equalsIgnoreCase("Lvl")){
		   isInLvl = true;
	   }
	   if(name.equalsIgnoreCase("Createddate")){
		   isInCreated_Date = true;
	   }
	   if(name.equalsIgnoreCase("Created_User_id")){
		   isInCreatedBy_Usr_id = true;
	   }
	   if(name.equalsIgnoreCase("DisplayName")){
		   isInDisplayName = true;
	   }
	   if(name.equalsIgnoreCase("BeatId")){
		   isInBeatId = true;
	   }
	   if(name.equalsIgnoreCase("PartyType")){
		   isInPartyType = true;
	   }if(name.equalsIgnoreCase("ContactPerson")){
		   isInContact = true;
	   }if(name.equalsIgnoreCase("CSTNo")){
		   isInCst = true;
	   }if(name.equalsIgnoreCase("VATTIN")){
		   isInVattin = true;
	   }if(name.equalsIgnoreCase("ServiceTax")){
		   isInServicetaxreg_No = true;
	   }if(name.equalsIgnoreCase("PANNo")){
		   isInPANNo = true;
	   }
	   if(name.equalsIgnoreCase("CityId")){
		   isInCity_Id = true;
	   }
	   if(name.equalsIgnoreCase("CreditLimit")){
		   isInCreditLimit = true;
	   }
	   if(name.equalsIgnoreCase("OutStanding")){
		   isInOutStanding = true;
	   }
	   if(name.equalsIgnoreCase("Phone")){
		   isInphone = true;
	   }
	   if(name.equalsIgnoreCase("OpenOrder")){
		   isInOpenOrder = true;
	   }
	   
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("distributor")){
			isInDistributor = false;
			distributorList.add(distributor);
		}
		if(name.equalsIgnoreCase("PartyId")){
			   isInId = false;
		   }
		   if(name.equalsIgnoreCase("PartyName")){
			   isInName1 = false;
		   }
		   if(name.equalsIgnoreCase("Address1")){
			   isInAddress_1 = false;
		   }
		   if(name.equalsIgnoreCase("Address2")){
			   isInAddress_2 = false;
		   }
		   if(name.equalsIgnoreCase("Pin")){
			   isInPin = false;
		   }
		   if(name.equalsIgnoreCase("AreaId")){
			   isInAreaId= false;
		   }
		   
		   if(name.equalsIgnoreCase("Email")){
			   isInEMail = false;
		   }
		   
		   if(name.equalsIgnoreCase("Mobile")){
			   isInMobile = false;
		   }
		   
		   if(name.equalsIgnoreCase("IndId")){
			   isInIndustryId = false;
		   }
		   if(name.equalsIgnoreCase("Potential")){
			   isInPotential = false;
		   }
		  
		   if(name.equalsIgnoreCase("Active")){
			   isInActive = false;
		   }
		   if(name.equalsIgnoreCase("BlockReason")){
			   isInBlocked_Reason = false;
		   }
		   if(name.equalsIgnoreCase("BlockDate")){
			   isInBlock_Date = false;
		   }
		   if(name.equalsIgnoreCase("BlockBy")){
			   isInBlockedBy = false;
		   }
		   if(name.equalsIgnoreCase("Remark")){
			   isInRemark = false;
		   }
		   if(name.equalsIgnoreCase("PartyDist")){
			   isInPartyDist = false;
		   }
		   if(name.equalsIgnoreCase("LoginCreated")){
			   isInLoginCreate = false;
		   }
		   if(name.equalsIgnoreCase("UserId")){
			   isInUserId = false;
		   }
		   if(name.equalsIgnoreCase("SyncId")){
			   isInsync_id = false;
		   }
		   if(name.equalsIgnoreCase("Lvl")){
			   isInLvl = false;
		   }
		   if(name.equalsIgnoreCase("Createddate")){
			   isInCreated_Date = false;
		   }
		   if(name.equalsIgnoreCase("Created_User_id")){
			   isInCreatedBy_Usr_id = false;
		   }
		   if(name.equalsIgnoreCase("DisplayName")){
			   isInDisplayName = false;
		   }
		   if(name.equalsIgnoreCase("BeatId")){
			   isInBeatId = false;
		   }
		   if(name.equalsIgnoreCase("PartyType")){
			   isInPartyType = false;
		   }if(name.equalsIgnoreCase("ContactPerson")){
			   isInContact = false;
		   }if(name.equalsIgnoreCase("CSTNo")){
			   isInCst = false;
		   }if(name.equalsIgnoreCase("VATTIN")){
			   isInVattin = false;
		   }if(name.equalsIgnoreCase("ServiceTax")){
			   isInServicetaxreg_No = false;
		   }if(name.equalsIgnoreCase("PANNo")){
			   isInPANNo = false;
		   }
		   if(name.equalsIgnoreCase("CityId")){
			   isInCity_Id = false;
		   }
		   if(name.equalsIgnoreCase("CreditLimit")){
			   isInCreditLimit = false;
		   }
		   if(name.equalsIgnoreCase("OutStanding")){
			   isInOutStanding = false;
		   }
		   if(name.equalsIgnoreCase("Phone")){
			   isInphone = false;
		   }
		   if(name.equalsIgnoreCase("OpenOrder")){
			   isInOpenOrder = false;
		   }
		   
	}

	//element content
	public void characters (char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInId){
	    	distributor.setDistributor_id((chars.toString()));
	    }if(isInName1){
	    	distributor.setDistributor_name((chars.toString()));
	    }
	    if(isInAddress_1){
	    	distributor.setAddress1((chars.toString()));
	    }
	    if(isInAddress_2){
	    	distributor.setAddress2((chars.toString()));
	    }
	    if(isInCity_Id){
	    	distributor.setCity_id((chars.toString()));
	    }
	    if(isInContact){
	    	distributor.setContact_person((chars.toString()));
	    }
	    if(isInMobile){
	    	distributor.setMobile((chars.toString()));
	    }
	    if(isInphone){
	    	distributor.setPhone((chars.toString()));
	    }
	    
	    if(isInEMail){
	    	distributor.setEmail((chars.toString()));
	    }
	    
	    if(isInPin){
	    	distributor.setPin((chars.toString()));
	    }
	    if(isInBlocked_Reason){
	    	distributor.setBlocked_Reason((chars.toString()));
	    }
	    if(isInBlock_Date){
	    	distributor.setBlock_Date((chars.toString()));
	    }
	    if(isInAreaId){
	    	distributor.setAreaId((chars.toString()));
	    } 
	    if(isInBeatId){
	    	distributor.setBeatId((chars.toString()));
	    } 
	    if(isInIndustryId){
	    	distributor.setIndId((chars.toString()));
	    } 
	    if(isInPotential){
	    	distributor.setPotential((chars.toString()));
	    } 
	    if(isInPartyType){
	    	distributor.setParty_type_code((chars.toString()));
	    } 
	    if(isInCst){
	    	distributor.setCst_no((chars.toString()));
	    } 
	    if(isInVattin){
	    	distributor.setVattin_no((chars.toString()));
	    } 
	    if(isInServicetaxreg_No){
	    	distributor.setServicetaxreg_No((chars.toString()));
	    } 
	    if(isInPANNo){
	    	distributor.setPANNo((chars.toString()));
	    } 
	    if(isInRemark){
	    	distributor.setRemark((chars.toString()));
	    } 
	    if(isInCreditLimit){
	    	distributor.setCreditLimit((chars.toString()));
	    } 
	    if(isInOutStanding){
	    	distributor.setOutStanding((chars.toString()));
	    } 
	    if(isInPendingOrder){
	    	distributor.setPendingOrder((chars.toString()));
	    } 
	    if(isInsync_id){
	    	distributor.setSync_id((chars.toString()));
	    } 
	    if(isInActive){
	    	distributor.setCity_id((chars.toString()));
	    } if(isInCreated_Date){
	    	distributor.setCreatedDate((chars.toString()));
	    } 
	    
	    if(isInOpenOrder){
	    	distributor.setOpenOrder((chars.toString()));
	    } 
	    
	}
	
	public ArrayList<Distributor> getData(){
		return this.distributorList;
	}
	
}