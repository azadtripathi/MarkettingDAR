package com.dm.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;

import com.dm.controller.CollectionController;
import com.dm.controller.PartyController;
import com.dm.model.Collection;
import com.dm.model.Party;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExportCollection {
Context context;ExceptionData exceptionData;ArrayList<Collection> collections;int maxPartySerailNo;String conper;
SharedPreferences preferences;PartyController partyController;boolean isPartyImported=false;String activeYn="0";
java.sql.Date data ;SimpleDateFormat sdf1;boolean exception=false;
Handler postCollectionDataHandler = new Handler();
public ExportCollection(Context context) {
	this.context=context;
	preferences=context.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
	conper=preferences.getString("CONPERID_SESSION", null);
	sdf1 = new SimpleDateFormat("dd/MMM/yyyy");
//	sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	postCollectionDataHandler.postDelayed(postCollectionThread, 0);
	
}
private Runnable postCollectionThread=new Runnable() {
	@Override
	public void run() {
		new PostCollection().execute();
		postCollectionDataHandler.postDelayed(postCollectionThread, 60*60*1000);
		
	}	
};

class PostCollection extends AsyncTask<String, String, String>
{

	@Override
	protected String doInBackground(String... params) {
		if(new ConnectionDetector(context).isConnectingToInternet())
		{
			if(checkUserIsActive().equals("1"))
			{
				if(insertNewParty())
				{
					postCollection();
				}
			}
			else
			{
				postCollectionDataHandler.removeCallbacks(postCollectionThread);
				System.out.println("user not active");
			}
		}
		else
		{
			System.out.println("No intercon");
		}
		
		return null;
	}
	@Override
	protected void onPostExecute(String result) {
		
	}
}
public void postCollection() {
//	String xmlStr=null;String status="";
	StringBuilder stringBuilder=new StringBuilder("");
	 ResultSet rs = null;Statement stmt = null;Connection con = null;
	CollectionController collectionController=new CollectionController(context);
	collectionController.open();
	collections=collectionController.getCollectionUplaodList();
	collectionController.close();
	if(collections.size()<=0)
	{
		System.err.println("Callbacks Removed");
		postCollectionDataHandler.removeCallbacks(postCollectionThread);
	}

		if(isPartyImported)
		{
//		xmlStr = sh.getXmlResponseString("http://"+Constant.SERVER_WEBSERVICE_URL+"/And_Sync.asmx/PostCollection", WebServiceHandler.POST,nameValuePairs);
		
			 try{
//				 int vId=0;
		         String docId="";
				 for (int j = 0; j < collections.size(); j++) 
				 {
					 Date date=null;
			    	  Date ndate=null;
		      	 try {
						date=sdf1.parse(collections.get(j).getPaymentDate());
						if(collections.get(j).getMode().equals("Cheque") ||collections.get(j).getMode().equals("Draft") ||collections.get(j).getMode().equals("RTGS"))
						{
							ndate=sdf1.parse(collections.get(j).getCheque_DD_Date());
						}else{
							ndate=null;
						}
						
					} catch (java.text.ParseException e) {
						exceptionData.setExceptionData(e.toString(), "insert dist collection");
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		         	data=null;
		      	    data = new java.sql.Date(date.getTime());
		      	  java.sql.Date ndata=null;
		      	  if(collections.get(j).getMode().equals("Cheque") ||collections.get(j).getMode().equals("Draft") ||collections.get(j).getMode().equals("RTGS"))
					{   
		      	  ndata=new java.sql.Date(ndate.getTime());
					}
		      	  else{
		      		 ndata=null;
		      	  }
//		      	    vId=Integer.parseInt(collections.get(j).getPaymentDate());
		      	  docId=getDocID("DISTP", data);
		      	
		      	String Sql="";
		      	if(collections.get(j).getMode().equals("Cheque") ||collections.get(j).getMode().equals("Draft") ||collections.get(j).getMode().equals("RTGS"))
				{   
		      		Sql="insert into DistributerCollection (CollDocId,UserId,DistId,SMId,Mode,Amount,PaymentDate,Cheque_DDNo,Cheque_DD_Date,Bank,Branch,Remarks,Android_Id)"+
							" values(" +
							"'"+docId+"'," +
							""+Integer.parseInt(collections.get(j).getUserId().trim())+"," +
							""+Integer.parseInt(collections.get(j).getDistId().trim())+"," +
							""+Integer.parseInt(collections.get(j).getSMId().trim())+"," +
							"'"+collections.get(j).getMode()+"',"+
							""+Float.parseFloat(collections.get(j).getAmount().trim())+"," +
							"'"+(data==null?"":data)+"'," +
							"'"+collections.get(j).getCheque_DDNo()+"',"+
							"'"+(ndata==null?"":ndata)+"'," +
							"'"+collections.get(j).getBank().replaceAll("'","''")+"'," +
							"'"+collections.get(j).getBranch().replaceAll("'","''")+"'," +
							"'"+collections.get(j).getRemarks().replaceAll("'","''")+"'," +
							"'"+collections.get(j).getAndroid_id()+"' )";
				
				}
		      	else{
		      		Sql="insert into DistributerCollection (CollDocId,UserId,DistId,SMId,Mode,Amount,PaymentDate,Remarks,Android_Id)"+
							" values(" +
							"'"+docId+"'," +
							""+Integer.parseInt(collections.get(j).getUserId().trim())+"," +
							""+Integer.parseInt(collections.get(j).getDistId().trim())+"," +
							""+Integer.parseInt(collections.get(j).getSMId().trim())+"," +
							"'"+collections.get(j).getMode()+"',"+
							""+Float.parseFloat(collections.get(j).getAmount().trim())+"," +
							"'"+(data==null?"":data)+"'," +
							"'"+collections.get(j).getRemarks().replaceAll("'","''")+"'," +
							"'"+collections.get(j).getAndroid_id()+"' )";
		      		
		      	}
		          	 
		        	 System.out.println(Sql);
		        	 int resultForMainInsert=0;
		        	 con= ConnectionFromSqlServer.connectionOpenWithTransaction();
		        	 con.setAutoCommit(false);
			         stmt = con.createStatement();
		        	 try {
						resultForMainInsert=stmt.executeUpdate(Sql);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						exceptionData.setExceptionData(e.toString(), "insertDistributerCollection");
						e.printStackTrace();
					}
		        	if(resultForMainInsert>0)
		        	{
		        		collectionController.open();
		        		stringBuilder.append(collections.get(j).getDistName()+"\nAmount -> "+collections.get(j).getAmount()+"\nMode => "+collections.get(j).getMode()+"\n"); 
	        			 collectionController.updateCollectionUpload(collections.get(j).getAndroid_id(),docId,1,"0");
	        			 collectionController.close();
	        			 new GetNotification(context, stringBuilder.toString(), "Payment posted","Message from Astral").ShowNotification();
		        		
	        			
		        	}
		        	 try{
    		        	 setDocID("DISTP", data);
    		        	 }
    		        	 catch(Exception e)
    		        	 {
    		        		 System.out.println(e);
    		        	 } 
		        	 
				 }
				 System.out.println("Commiting data here....");
				 con.commit();
		         stmt.close();
		         con.close();
//		         new GetNotification(context, stringBuilder.toString(), "Payment posted","Message from Astral").ShowNotification();
//		         try{
//		        	 setDocID("DISTP", data);
//		        	 }
//		        	 catch(Exception e)
//		        	 {
//		        		 System.out.println(e);
//		        	 }
//		     	int vno=0;
//		      
//		         	try{
//		        		 //getMaxValues();
//		         		 con=ConnectionFromSqlServer.connectionOpenWithTransaction();
//		    	    	 con.setAutoCommit(false);
//		    	         stmt = con.createStatement();
//		    	         String sql="select CollId as vno  from DistributerCollection where CollDocId='"+docId+"'";
//		    	         System.out.println(sql);
//		    	         rs=stmt.executeQuery(sql);
//		    			 while (rs.next()) {
//		    				 vno=rs.getInt("vno");
//		    				System.out.println("max visitNo value="+vno);	
//		    			}
//		         	} catch(Exception e){
//		    				 
//		         		System.out.println(e);
//		    			 }
//		         	finally{
//		         		
//		         		 try{
//		    	             if(stmt!=null)
//		    	                stmt.close();
//		    	          }catch(SQLException se2){
//		    	          }// nothing we can do
//		    	          try{
//		    	             if(con!=null)
//		    	                con.close();
//		    	          }catch(SQLException se){
//		    	             se.printStackTrace();
//		    	          }//end finally try
//		    	          collectionController.open();
//		        		 for (int i = 0; i < collections.size(); i++) {
//		        			 stringBuilder.append(collections.get(i).getDistName()+"\nAmount -> "+collections.get(i).getAmount()+"\nMode => "+collections.get(i).getMode()+"\n"); 
//		        			 collectionController.updateCollectionUpload(collections.get(i).getAndroid_id(),docId,vno);
//		        			 new GetNotification(context, stringBuilder.toString(), "Payment posted","Message from Astral").ShowNotification();
//						}
//		        		 collectionController.close();	 
//		        		 
//		         	}
			 
		}
				 catch(SQLException se){
			          //Handle errors for JDBC
			          se.printStackTrace();
			          // If there is an error then rollback the changes.
			          System.out.println("Rolling back data here....");
			          postCollectionDataHandler.removeCallbacks(postCollectionThread);
			    	  try{
			    		 if(con!=null)
			                con.rollback();
			    		 postCollectionDataHandler.removeCallbacks(postCollectionThread);
			          }catch(SQLException se2){
			             se2.printStackTrace();
			             postCollectionDataHandler.removeCallbacks(postCollectionThread);
			          }//end try

			       }catch(Exception e){
			          //Handle errors for Class.forName
			    	   postCollectionDataHandler.removeCallbacks(postCollectionThread);
			          e.printStackTrace();
			       }finally{
			          //finally block used to close resources
			          try{
			             if(stmt!=null)
			                stmt.close();
			          }catch(SQLException se2){
			          }// nothing we can do
			          try{
			             if(con!=null)
			                con.close();
			          }catch(SQLException se){
			             se.printStackTrace();
			          }//end finally try
		       }//end try
		  	
		
		}
	
	}
	




public boolean insertNewParty()
{
	ResultSet rs = null;Statement stmt = null; Connection con=null;
	 con= ConnectionFromSqlServer.connectionOpenWithTransaction();
	 partyController=new PartyController(context);
	 partyController.open();
	 ArrayList<Party> parties=new ArrayList<Party>();
	 parties=partyController.getPartyListUpload();
	 partyController.close();
	 try {
		 con.setAutoCommit(false);
        stmt = con.createStatement();
	 for (int j = 0; j < parties.size(); j++) {
		 if(parties.get(j).getParty_id().isEmpty())
		 {
			 Date date=null;
	    	  Date ndate=null;
      	 try {
				date=sdf1.parse(parties.get(j).getCreatedDate());
//				ndate=sdf1.parse(demoTransactions.get(j).getNextVisitDate());
			} catch (java.text.ParseException e) {
				exceptionData.setExceptionData(e.toString(), "insertDsr");
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       	data=null;
      	   data = new java.sql.Date(date.getTime());
						 
//			 String party_code="A"+String.format("%05d",Integer.valueOf(conper))+String.format("%05d",Integer.valueOf(parties.get(j).getArea_id()))+"S"+String.format("%07d",maxPartySerailNo++);
//      	String query="";
//      	   try{
      		String query ="INSERT INTO MastParty(PartyName,Address1,Address2,Pin,AreaId,Email,Mobile,IndId,Potential,Active,Remark,PartyDist,"
							+" UserId,SyncId,Created_Date,BeatId,PartyType,ContactPerson,CSTNo,VATTIN,ServiceTax,PANNo,CityId, "
							+"Phone,Android_Id) "
							+" values( "+
							"'"+parties.get(j).getParty_name().replaceAll("'","''")+"'," +
							"'"+(parties.get(j).getAddress1()==null?'0':parties.get(j).getAddress1().replaceAll("'","''"))+"'," +
							"'"+(parties.get(j).getAddress2()==null?'0':parties.get(j).getAddress2().replaceAll("'","''"))+"'," +
							"'"+parties.get(j).getPin()+"'," +
							""+Integer.parseInt(parties.get(j).getAreaId().trim())+"," +
							"'"+(parties.get(j).getEmail()==null?'0':parties.get(j).getEmail().replaceAll("'","''"))+"'," +
							"'"+parties.get(j).getMobile()+"'," +
							""+Integer.parseInt(parties.get(j).getIndId().trim())+"," +
							""+Float.parseFloat(parties.get(j).getPotential().trim())+"," +
							""+parties.get(j).getActive()+"," +
							"'"+parties.get(j).getRemark().replaceAll("'","''")+"'," +
							" 0,"+
							""+Integer.parseInt(parties.get(j).getUserId().trim())+"," +
							"'"+parties.get(j).getSync_id().replaceAll("'","''")+"'," +
							"'"+data+"',"+
							""+Integer.parseInt(parties.get(j).getBeatId().trim())+"," +
							""+Integer.parseInt(parties.get(j).getParty_type_code().trim())+"," +
							"'"+parties.get(j).getContact_person().replaceAll("'","''")+"'," +
							"'"+parties.get(j).getCst_no().replaceAll("'","''")+"'," +
							"'"+parties.get(j).getVattin_no().replaceAll("'","''")+"'," +
							"'"+parties.get(j).getServicetaxreg_No().replaceAll("'","''")+"'," +
							"'"+parties.get(j).getPANNo().replaceAll("'","''")+"'," +
							""+Integer.parseInt(parties.get(j).getCity_id().trim())+"," +
							"'"+parties.get(j).getPhone()+"'," +
							"'"+parties.get(j).getAndroidId()+"')";
      		System.out.println("query="+query);	
//      	   }
//			catch(Exception e){
//				System.out.println(e);	
////				System.out.println("query="+query);	
////				Toast.makeText(context, "e="+e, Toast.LENGTH_LONG);
////				Toast.makeText(context, "query="+query, Toast.LENGTH_LONG);
//			}
			 
			 int resultId=0;
			 	String pno="";
				resultId=stmt.executeUpdate(query);
				System.out.println("Inserted="+resultId);
				if(!(resultId==0))
				{
					String sql="select PartyId as PartyId  from MastParty where Android_Id='"+parties.get(j).getAndroidId()+"'";
			         System.out.println(sql);
			         rs=stmt.executeQuery(sql);
					 while (rs.next()) {
						 pno=rs.getString("PartyId");
						System.out.println("pno value="+pno);	
				}
					 parties.get(j).setParty_id(pno);
				}	
		 }
		 else{

			 String query="Update MastParty " +
						"set " +
						" PartyName='"+parties.get(j).getParty_name().replaceAll("'","''")+"'," +
						" Address_1='"+(parties.get(j).getAddress1()==null?'0':parties.get(j).getAddress1().replaceAll("'","''"))+"'," +
						" Address_2='"+(parties.get(j).getAddress2()==null?'0':parties.get(j).getAddress2().replaceAll("'","''"))+"'," +
						" Pin='"+parties.get(j).getPin()+"'," +
						" CityId="+Integer.parseInt(parties.get(j).getCity_id().trim())+"," +
						" AreaId="+Integer.parseInt(parties.get(j).getAreaId().trim())+"," +
						" Email='"+(parties.get(j).getEmail()==null?'0':parties.get(j).getEmail().replaceAll("'","''"))+"'," +
						" Mobile='"+parties.get(j).getMobile().replaceAll("'","''")+"'," +
						" IndId="+Integer.parseInt(parties.get(j).getIndId().trim())+"," +
						" Potential="+Float.parseFloat(parties.get(j).getPotential().trim())+","+
						" Active="+parties.get(j).getActive()+"," +
						" Remark="+parties.get(j).getRemark().replaceAll("'","''")+"," +
						" UserId="+Integer.parseInt(parties.get(j).getUserId().trim())+"" +
						" SyncId='"+parties.get(j).getSync_id()+"',"+
						" Created_Date='"+data+"',"+
						" BeatId="+Integer.parseInt(parties.get(j).getBeatId().trim())+"," +
						" PartyType="+Integer.parseInt(parties.get(j).getParty_type_code().trim())+"," +
						" ContactPerson='"+parties.get(j).getContact_person().replaceAll("'","''")+"'," +
						" CSTNo='"+parties.get(j).getCst_no().replaceAll("'","''")+"'," +
						" VATTIN='"+parties.get(j).getVattin_no().replaceAll("'","''")+"'," +
						" ServiceTax='"+parties.get(j).getServicetaxreg_No().replaceAll("'","''")+"'," +
						" PANNo='"+parties.get(j).getPANNo().replaceAll("'","''")+"'," +
						" Phone='"+parties.get(j).getPhone().replaceAll("'","''")+"'," +
						" where PartyId='"+parties.get(j).getParty_id()+"'";
			 System.out.println(query);
			 int resultId=0;
			
				resultId=stmt.executeUpdate(query);
				 System.out.println("Updated="+resultId);
				 
			}
	 
	 }
	 System.out.println("Commiting data here....");
	 con.commit();
    stmt.close();
    con.close();
    isPartyImported=true;
    partyController.open();
	for (int i = 0; i < parties.size(); i++) {
		partyController.updatetPartyWebcode(parties.get(i).getParty_id(), parties.get(i).getAndroidId(),"0");
		isPartyImported=true;
	}
	partyController.close();
	
	 }
	 catch(SQLException se){
		 	exception=true;
		 	isPartyImported=false;
		          se.printStackTrace();
		      	
		          // If there is an error then rollback the changes.
		          System.out.println("Rolling back data here....");
		    	  try{
		    		 if(con!=null)
		    			 isPartyImported=false;
		                con.rollback();
		          }catch(SQLException se2){
		        	  isPartyImported=false;
		             se2.printStackTrace();
		          }//end try

		       }catch(Exception e){
		          //Handle errors for Class.forName
		    	   isPartyImported=false;
		          e.printStackTrace();
		       }finally{
		          //finally block used to close resources
		          try{
		             if(stmt!=null)
		                stmt.close();
		          }catch(SQLException se2){
		          }// nothing we can do
		          try{
		             if(con!=null)
		                con.close();
		          }catch(SQLException se){
		             se.printStackTrace();
		          }//end finally try
		       }
	 return isPartyImported;
		 }
private String checkUserIsActive() {
	 ResultSet rs = null;Statement stmt = null;
	 stmt= ConnectionFromSqlServer.connectionOpen();
	 String sql="select Active from MastSalesRep where DeviceNo='"+preferences.getString("PDAID_SESSION", "")+"'";
	 System.out.println(sql);
	 try {
		rs=stmt.executeQuery(sql);
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 try {
		while(rs.next())
		 {
			activeYn=rs.getString("Active");
			 System.out.println("activeYn="+activeYn);
		 }
	} catch (SQLException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	 ConnectionFromSqlServer.connectionClosed();
	 return activeYn;
}


public static String getDocID(String prefix,java.sql.Date date)
	    throws SQLException
	{
 String docId="";
	
 ResultSet rs = null;Connection con = null;
 con= ConnectionFromSqlServer.connectionOpenWithTransaction();
	   CallableStatement proc = null;

	   try
	   { 
	      proc = con.prepareCall("{ call sp_Getdocid(?,?,?) }");
	      proc.setString(1, prefix);
	      proc.setDate(2, date);
	      proc.registerOutParameter(3, Types.VARCHAR); 
	      proc.execute();
	      
	       docId=proc.getString(3);
	     
	   }
	   catch(Exception e)
	   {
		   System.out.println(e);
	   }
	   finally
	   {
	      try
	      {
	         proc.close();
	      }
	      catch (SQLException e) {}
	      con.close();
	   }
	   return docId;
	   
	}


public static void setDocID(String prefix, java.sql.Date date)
	    throws SQLException
	{
 ResultSet rs = null;Connection con = null;
 con= ConnectionFromSqlServer.connectionOpenWithTransaction();
	   CallableStatement proc = null;int r=0;

	   try
	   {
	      proc = con.prepareCall("{ call sp_Setdocid(?,?) }");
	      proc.setString(1, prefix);
	      proc.setDate(2, date);
//	      proc.registerOutParameter(3, Types.VARCHAR); 
	      proc.execute();
//	      r=proc.getInt(3);
	      
	   }
	   catch(Exception e)
	   {
		   System.out.println(e);
	   }
	   finally
	   {
	      try
	      {
	         proc.close();
	      }
	      catch (SQLException e) {}
	      con.close();
	   }
	}
}
