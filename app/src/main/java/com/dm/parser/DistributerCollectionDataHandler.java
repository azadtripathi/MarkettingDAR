package com.dm.parser;

import android.util.Log;

import com.dm.model.Collection;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;

public class DistributerCollectionDataHandler extends DefaultHandler{
	private ArrayList<Collection> collectionList = new ArrayList<Collection>();
	private Collection collection;

	private boolean isInDistributorCollection = false;
	private boolean isInCollId = false;
	private boolean isInVisId = false;
	private boolean isInCollDocId = false;
	private boolean isInVDate = false;
	private boolean isInUserId = false;
	private boolean isInSMID = false;
	private boolean isInDistId = false;
	private boolean isInMode = false;
	private boolean isInAmount = false;
	private boolean isInpaymentDate = false;
	private boolean isInAndroid_Id = false;
	private boolean isInRemarks = false;
	private boolean isInCheque_DDNo = false;
	private boolean isInCheque_DD_Date = false;
	private boolean isInBank = false;
	private boolean isInBranch = false;
	private boolean isInCreatedDate = false;
	//start of the XML document
	public void startDocument () { Log.i("DataHandler", "Start of XML document"); }

	//end of the XML document
	public void endDocument () { Log.i("DataHandler", "End of XML document"); }

	//opening element tag
	public void startElement (String uri, String name, String qName, Attributes atts)
	{
	   if(name.equalsIgnoreCase("DistributorCollection")){
		   isInDistributorCollection = true;		   
		   collection = new Collection();
	   }
	   if(name.equalsIgnoreCase("CollId")){
		   isInCollId = true;
	   }
	   if(name.equalsIgnoreCase("VisId")){
		   isInVisId = true;
	   }
	   if(name.equalsIgnoreCase("CollDocId")){
		   isInCollDocId = true;
	   }
	   if(name.equalsIgnoreCase("Android_Id")){
		   isInAndroid_Id = true;
	   }
	   if(name.equalsIgnoreCase("VDate")){
		   isInVDate = true;
	   }
	   if(name.equalsIgnoreCase("UserId")){
		   isInUserId = true;
	   }
	   if(name.equalsIgnoreCase("SMID")){
		   isInSMID = true;
	   }
	   if(name.equalsIgnoreCase("DistId")){
		   isInDistId = true;
	   } 
	   
	   if(name.equalsIgnoreCase("Mode")){
		   isInMode = true;
	   
	   } 
	   if(name.equalsIgnoreCase("Amount")){
		   isInAmount = true;
	   
	   }  
	   if(name.equalsIgnoreCase("paymentDate")){
		   isInpaymentDate = true;
	   
	   } 
	   if(name.equalsIgnoreCase("Cheque_DDNo")){
		   isInCheque_DDNo = true;
	   
	   } 
	   if(name.equalsIgnoreCase("Cheque_DD_Date")){
		   isInCheque_DD_Date = true;
	   
	   } 
	   if(name.equalsIgnoreCase("Bank")){
		   isInBank = true;
	   
	   } 
	   if(name.equalsIgnoreCase("Branch")){
		   isInBranch = true;
	   
	   }     
	   if(name.equalsIgnoreCase("createddate")){
		   isInCreatedDate = true;
	   }
	   
	}

	//closing element tag
	public void endElement (String uri, String name, String qName)
	{
		if(name.equalsIgnoreCase("DistributorCollection")){
			isInDistributorCollection = false;
			collectionList.add(collection);
		}
		 if(name.equalsIgnoreCase("CollId")){
			   isInCollId = false;
		   }
		   if(name.equalsIgnoreCase("VisId")){
			   isInVisId = false;
		   }
		   if(name.equalsIgnoreCase("CollDocId")){
			   isInCollDocId = false;
		   }
		   if(name.equalsIgnoreCase("Android_Id")){
			   isInAndroid_Id = false;
		   }
		   if(name.equalsIgnoreCase("VDate")){
			   isInVDate = false;
		   }
		   if(name.equalsIgnoreCase("UserId")){
			   isInUserId = false;
		   }
		   if(name.equalsIgnoreCase("SMID")){
			   isInSMID = false;
		   }
		   if(name.equalsIgnoreCase("DistId")){
			   isInDistId = false;
		   } 
		   	   
		   if(name.equalsIgnoreCase("Mode")){
			   isInMode = false;
		   
		   } 
		   if(name.equalsIgnoreCase("Amount")){
			   isInAmount = false;
		   
		   }  
		   if(name.equalsIgnoreCase("paymentDate")){
			   isInpaymentDate = false;
		   
		   } 
		   if(name.equalsIgnoreCase("Cheque_DDNo")){
			   isInCheque_DDNo = false;
		   
		   } 
		   if(name.equalsIgnoreCase("Cheque_DD_Date")){
			   isInCheque_DD_Date = false;
		   
		   } 
		   if(name.equalsIgnoreCase("Bank")){
			   isInBank = false;
		   
		   } 
		   if(name.equalsIgnoreCase("Branch")){
			   isInBranch = false;
		   
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
	    if(isInCollId){
	    	collection.setCollId(chars.toString());
	    }
	    else if(isInVisId){
	    	collection.setVisitId(chars.toString());
	    }
	    else if(isInCollDocId){
	    	collection.setCollDocId(chars.toString());
	    }
	    else if(isInAndroid_Id){
	    	collection.setAndroid_id(chars.toString());
	    }
	    else if(isInVDate){
	    	collection.setVdate(chars.toString());
	    }
	    else if(isInUserId){
	    	collection.setUserId(chars.toString());
	    } 
	    else if(isInSMID){
	    	collection.setSMId(chars.toString());
	    } 
	    else if(isInDistId){
	    	collection.setDistId(chars.toString());
	    } 
	    else if(isInMode){
	    	collection.setMode(chars.toString());
	    }   
	    else if(isInAmount){
	    	collection.setAmount(chars.toString());
	    }
	    else if(isInpaymentDate){
	    	collection.setPaymentDate(chars.toString());
	    }
	    else if(isInCheque_DDNo){
	    	collection.setCheque_DDNo(chars.toString());
	    }
	    else if(isInCheque_DD_Date){
	    	collection.setCheque_DD_Date(chars.toString());
	    }
	    else if(isInBank){
	    	collection.setBank(chars.toString());
	    }
	    else if(isInBranch){
	    	collection.setBranch(chars.toString());
	    }
	    else if(isInCreatedDate){
	    	collection.setCreatedDate(chars.toString());
	    }  
	}
	
	public ArrayList<Collection> getData(){
		return this.collectionList;
	}
	
 
}
