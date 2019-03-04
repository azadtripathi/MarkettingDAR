package com.dm.parser;

import android.util.Log;

import com.dm.model.Party;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class PartyDataHandler extends DefaultHandler{
	private ArrayList<Party> partyList = new ArrayList<Party>();
	private Party party;

	private boolean isInPartyNew = false;
	private boolean isInId = false;
	private boolean isInBeatId = false;
	private boolean isInAreaId = false;
	private boolean isInPartyCode = false;
	private boolean isInIndustryId = false;
	private boolean isInName = false;
	private boolean isInTypeId = false;
	private boolean isInConPer = false;
	private boolean isInAddress1 = false;
	private boolean isInAddress2 = false;
	private boolean isInPin = false;
	private boolean isInCtyCode_P1 = false;
	private boolean isInStd = false;
	private boolean isInPhone = false;
	private boolean isInMobile1 = false;
	private boolean isInMobile2 = false;
	private boolean isInRating = false;
	private boolean isInFax = false;
	private boolean isInEmail1 = false;
	private boolean isInWeb = false;
	private boolean isInRegistered = false;
	private boolean isInIsBlicked = false;
	private boolean isInLstNumber = false;
	private boolean isInPotential = false;
	private boolean isInCstNumber = false;
	private boolean isInVatTinNumber = false;
	private boolean isInServiceTaxNumber = false;
	private boolean isInCreatedDate = false;
	private boolean isInUserId = false;
	private boolean isInBlockedReason = false;
	private boolean isInBlock_Date = false;
	private boolean isInBlockedBy = false;
	private boolean isInBlock = false;
	private boolean isInActive = false;
	private boolean isInRemark = false;
	private boolean isInPanNo = false;
	private boolean isInPartyTypeCode = false;
	private boolean isInCreditLimit = false;
	private boolean isInOutStanding = false;
	private boolean isInSyncId = false;
	private boolean isInPendingOrder = false;
	private boolean isInDistId = false;
	
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("PartyNew")){
		   isInPartyNew = true;		   
		   party = new Party();
	   }
	   if(name.equalsIgnoreCase("Id")){
		   isInId = true;
	   }
	   if(name.equalsIgnoreCase("Area_Id")){
		   isInAreaId = true;
	   }
	   if(name.equalsIgnoreCase("Beat_Id")){
		   isInBeatId = true;
	   }
	   if(name.equalsIgnoreCase("Party_Code")){
		   isInPartyCode = true;
	   }
	   if(name.equalsIgnoreCase("Name")){
		   isInName = true;
	   }
	   if(name.equalsIgnoreCase("Industry_Id")){
		   isInIndustryId = true;
	   }
	   if(name.equalsIgnoreCase("Type_Id")){
		   isInTypeId = true;
	   }
	   if(name.equalsIgnoreCase("ContactPerson")){
		   isInConPer = true;
	   }
	   if(name.equalsIgnoreCase("Add1")){
		   isInAddress1 = true;
	   }
	   if(name.equalsIgnoreCase("Add2")){
		   isInAddress2 = true;
	   }
	   if(name.equalsIgnoreCase("Pin")){
		   isInPin = true;
	   }
	   if(name.equalsIgnoreCase("CtyCodeP1")){
		   isInCtyCode_P1 = true;
	   }
	   if(name.equalsIgnoreCase("Mobile1")){
		   isInMobile1 = true;
	   }
	   if(name.equalsIgnoreCase("Email1")){
		   isInEmail1 = true;
	   }
	   if(name.equalsIgnoreCase("Potential")){
		   isInPotential = true;
	   }
	   if(name.equalsIgnoreCase("IsBlocked")){
		   isInBlock = true;
	   }
	   if(name.equalsIgnoreCase("CST_No")){
		   isInCstNumber = true;
	   }
	   if(name.equalsIgnoreCase("ServiceTaxReg_No")){
		   isInServiceTaxNumber = true;
	   }
	   if(name.equalsIgnoreCase("Created_Date")){
		   isInCreatedDate = true;
	   }
	   if(name.equalsIgnoreCase("BlockedReason")){
		   isInBlockedReason = true;
	   }
	   if(name.equalsIgnoreCase("Block_Date")){
		   isInBlock_Date = true;
	   }
	   if(name.equalsIgnoreCase("BlockedBy")){
		   isInBlockedBy = true;
	   }
	   if(name.equalsIgnoreCase("Active")){
		   isInActive = true;
	   }
	   if(name.equalsIgnoreCase("Remark")){
		   isInRemark = true;
	   }
	   if(name.equalsIgnoreCase("PANNo")){
		   isInPanNo = true;
	   }
	   if(name.equalsIgnoreCase("Block ")){
		   isInBlock = true;
	   }
	   if(name.equalsIgnoreCase("Created_User_Id")){
		   isInUserId = true;
	   }
	   if(name.equalsIgnoreCase("DistId")){
		   isInDistId = true;
	   }
//	   if(name.equalsIgnoreCase("ServiceTaxReg_No")){
//		   isInServiceTaxNumber = true;
//	   }
//	   if(name.equalsIgnoreCase("Created_Date")){
//		   isInCreatedDate = true;
//	   }
//	   if(name.equalsIgnoreCase("Usr_Id")){
//		   isInUserId = true;
//	   }
//	   if(name.equalsIgnoreCase("BlockedReason")){
//		   isInBlockedReason = true;
//	   }
//	   if(name.equalsIgnoreCase("Block_Date")){
//		   isInBlock_Date = true;
//	   }
//	   if(name.equalsIgnoreCase("BlockedBy")){
//		   isInBlockedBy = true;
//	   }
//	   
//	   if(name.equalsIgnoreCase("Block")){
//		   isInBlock = true;
//	   }
	   
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		   if(name.equalsIgnoreCase("PartyNew")){
			   isInPartyNew = false;		   
			   partyList.add(party);
		   }
		   if(name.equalsIgnoreCase("Id")){
			   isInId = false;
		   }
		   if(name.equalsIgnoreCase("Area_Id")){
			   isInAreaId = false;
		   }
		   if(name.equalsIgnoreCase("Beat_Id")){
			   isInBeatId = false;
		   }
		   if(name.equalsIgnoreCase("Party_Code")){
			   isInPartyCode = false;
		   }
		   if(name.equalsIgnoreCase("Name")){
			   isInName = false;
		   }
		   if(name.equalsIgnoreCase("Industry_Id")){
			   isInIndustryId = false;
		   }
		   if(name.equalsIgnoreCase("Type_Id")){
			   isInTypeId = false;
		   }
		   if(name.equalsIgnoreCase("ContactPerson")){
			   isInConPer = false;
		   }
		   if(name.equalsIgnoreCase("Add1")){
			   isInAddress1 = false;
		   }
		   if(name.equalsIgnoreCase("Add2")){
			   isInAddress2 = false;
		   }
		   if(name.equalsIgnoreCase("Pin")){
			   isInPin = false;
		   }
		   if(name.equalsIgnoreCase("CtyCodeP1")){
			   isInCtyCode_P1 = false;
		   }
		   if(name.equalsIgnoreCase("Mobile1")){
			   isInMobile1 = false;
		   }
		   if(name.equalsIgnoreCase("Email1")){
			   isInEmail1 = false;
		   }
		   if(name.equalsIgnoreCase("Potential")){
			   isInPotential = false;
		   }
		   if(name.equalsIgnoreCase("IsBlocked")){
			   isInBlock = false;
		   }
		   if(name.equalsIgnoreCase("CST_No")){
			   isInCstNumber = false;
		   }
		   if(name.equalsIgnoreCase("ServiceTaxReg_No")){
			   isInServiceTaxNumber = false;
		   }
		   if(name.equalsIgnoreCase("Created_Date")){
			   isInCreatedDate = false;
		   }
		   if(name.equalsIgnoreCase("BlockedReason")){
			   isInBlockedReason = false;
		   }
		   if(name.equalsIgnoreCase("Block_Date")){
			   isInBlock_Date = false;
		   }
		   if(name.equalsIgnoreCase("BlockedBy")){
			   isInBlockedBy = false;
		   }
		   if(name.equalsIgnoreCase("Active")){
			   isInActive = false;
		   }
		   if(name.equalsIgnoreCase("Remark")){
			   isInRemark = false;
		   }
		   if(name.equalsIgnoreCase("PANNo")){
			   isInPanNo = false;
		   }
		   if(name.equalsIgnoreCase("Block ")){
			   isInBlock = false;
		   }
		   if(name.equalsIgnoreCase("Created_User_Id")){
			   isInUserId = false;
		   }
		   if(name.equalsIgnoreCase("DistId")){
			   isInDistId = false;
		   }
		   
	}

	//element content
	public void characters (char ch[], int start, int length)
	{
		String chars = new String(ch, start, length); 
	    chars = chars.trim();
	    if(isInId){
	    	party.setParty_id(chars.toString());
	    }else if(isInName){
	    	party.setParty_name(chars.toString());
	    }else if(isInAddress1){
	    	party.setAddress1(chars.toString());
	    }else if(isInAddress2){
	    	
	    	party.setAddress2(chars.toString());
	    }else if(isInCtyCode_P1){
	    	party.setCity_id(chars.toString());
	    }else if(isInPin){
	    	party.setPin(chars.toString());
	    }
	    else if(isInConPer){
	    	party.setContact_person(chars.toString());
	    }
	    else if(isInMobile1){
	    	party.setMobile(chars.toString());
	    }
	    else if(isInPhone){
	    	party.setPhone(chars.toString());
	    }
	    else if(isInEmail1){
	    	party.setEmail(chars.toString());
	    }
	    else if(isInBlockedReason){
	    	party.setBlocked_Reason(chars.toString());
	    }
	    else if(isInBlock_Date){
	    	party.setBlock_Date(chars.toString());
	    }
	    else if(isInBlockedBy){
	    	party.setBlocked_By(chars.toString());
	    }
	    else if(isInAreaId){
	    	party.setAreaId(chars.toString());
	    }
	    else if(isInBeatId){
	    	party.setBeatId(chars.toString());
	    }
	    else if(isInIndustryId){
	    	party.setIndId(chars.toString());
	    }
	    else if(isInPotential){
	    	party.setPotential(chars.toString());
	    }
	    else if(isInPartyTypeCode){
	    	party.setParty_type_code(chars.toString());
	    }
	    else if(isInCstNumber){
	    	party.setCst_no(chars.toString());
	    }
	    else if(isInVatTinNumber){
	    	party.setVattin_no(chars.toString());
	    }
	    else if(isInServiceTaxNumber){
	    	party.setServicetaxreg_No(chars.toString());
	    }
	    else if(isInPanNo){
	    	party.setPANNo(chars.toString());
	    }
	    else if(isInRemark){
	    	party.setRemark(chars.toString());
	    }
	    else if(isInCreditLimit){
	    	party.setCreditLimit(chars.toString());
	    }
	    else if(isInOutStanding){
	    	party.setOutStanding(chars.toString());
	    }
	    else if(isInPendingOrder){
	    	party.setPendingOrder(chars.toString());
	    }
	    else if(isInSyncId){
	    	party.setSync_id(chars.toString());
	    }
	    else if(isInActive){
	    	party.setActive(chars.toString());
	    }
	    else if(isInCreatedDate){
	    	party.setCreatedDate(chars.toString());
	    }
	    else if(isInDistId){
	    	party.setDistId(chars.toString());
	    }
	    
	    } 
	    
	    
	
	
	public ArrayList<Party> getData(){
		return this.partyList;
	}
	
}