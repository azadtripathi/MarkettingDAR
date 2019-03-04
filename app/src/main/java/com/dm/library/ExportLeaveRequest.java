package com.dm.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;

import com.dm.controller.PartyController;
import com.dm.controller.TransLeaveController;
import com.dm.model.Collection;
import com.dm.model.Party;
import com.dm.model.TransLeaveRequest;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ExportLeaveRequest {

	Context context;ExceptionData exceptionData;ArrayList<Collection> collections;int maxPartySerailNo;String conper;
	SharedPreferences preferences;PartyController partyController;boolean isPartyImported=false;String activeYn="0";
	java.sql.Date data ,fdata,tdata;SimpleDateFormat sdf1;boolean exception=false;
	Handler postLeaveRequestDataHandler = new Handler();
	public ExportLeaveRequest(Context context) {
		this.context=context;
		preferences=context.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
		conper=preferences.getString("CONPERID_SESSION", null);
		sdf1 = new SimpleDateFormat("dd/MMM/yyyy");
//		sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		postLeaveRequestDataHandler.postDelayed(postLeaveRequestThread, 0);
		
	}
	private Runnable postLeaveRequestThread=new Runnable() {
		@Override
		public void run() {
			new PostLeaveRequest().execute();
			postLeaveRequestDataHandler.postDelayed(postLeaveRequestThread, 60*60*1000);
			
		}	
	};

	class PostLeaveRequest extends AsyncTask<String, String, String>
	{

		@Override
		protected String doInBackground(String... params) {
			if(new ConnectionDetector(context).isConnectingToInternet())
			{
				if(checkUserIsActive().equals("1"))
				{
					if(insertNewParty())
					{
						postLeaveRequest();
					}
				}
				else
				{
					postLeaveRequestDataHandler.removeCallbacks(postLeaveRequestThread);
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
	public void postLeaveRequest() {
//		String xmlStr=null;String status="";
		StringBuilder stringBuilder=new StringBuilder("");
		 ResultSet rs = null;Statement stmt = null;Connection con = null;
		 TransLeaveController transLeaveController=new TransLeaveController(context);
		 transLeaveController.open();
		 ArrayList<TransLeaveRequest> transLeaveRequestList;
		 transLeaveRequestList=transLeaveController.getUploadLeaveList();
		 transLeaveController.close();
		if(transLeaveRequestList.size()<=0)
		{
			System.err.println("Callbacks Removed");
			postLeaveRequestDataHandler.removeCallbacks(postLeaveRequestThread);
		}

			if(isPartyImported)
			{
				 try{
					 int vId=0;
			         String docId="";
					 for (int j = 0; j < transLeaveRequestList.size(); j++) 
					 {
						 Date date=null;
				    	  Date fdate=null;
				    	  Date tdate=null;
			      	 try {
							date=sdf1.parse(transLeaveRequestList.get(j).getVdate());
							fdate=sdf1.parse(transLeaveRequestList.get(j).getFromDate());
							tdate=sdf1.parse(transLeaveRequestList.get(j).getToDate());
						} catch (java.text.ParseException e) {
							exceptionData.setExceptionData(e.toString(), "insertLeaveRequest");
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			         	data=null;
			         	fdata=null;
			         	tdata=null;
			      	    data = new java.sql.Date(date.getTime());
			      	    fdata = new java.sql.Date(fdate.getTime());
			      	    tdata = new java.sql.Date(tdate.getTime());
			      	  docId=getDocID("LVR", data);
			          	String Sql="insert into TransLeaveRequest (LVRDocId,VDate,UserId,NoOfDays," +
			          			"FromDate,ToDate,Reason,SMId,LeaveFlag,AppStatus,AppBy,AppBySMId,Android_Id)"+
									" values(" +
									"'"+docId+"'," +
									"'"+data+"'," +
									""+Integer.parseInt(transLeaveRequestList.get(j).getUserId())+"," +
									""+Double.parseDouble(transLeaveRequestList.get(j).getNoOfDay())+"," +
									"'"+fdata+"'," +
									"'"+tdata+"'," +
									"'"+transLeaveRequestList.get(j).getReason()+"'," +
									""+Integer.parseInt(transLeaveRequestList.get(j).getSmid())+"," +
									"'"+transLeaveRequestList.get(j).getLeaveFlag()+"'," +
									"'Pending'," +
									"0," +
									"0," +
									"'"+transLeaveRequestList.get(j).getAndroid_id()+"' )";
								
			        	 System.out.println(Sql);
			        	 int resultForMainInsert=0;
			        	 con= ConnectionFromSqlServer.connectionOpenWithTransaction();
			        	 con.setAutoCommit(false);
				         stmt = con.createStatement();
			        	 try {
							resultForMainInsert=stmt.executeUpdate(Sql);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							con.commit();
					         stmt.close();
					         con.close();
							exceptionData.setExceptionData(e.toString(), "insertLeaveRequest");
							e.printStackTrace();
						}
			        	 con.commit();
			             stmt.close();
			             con.close();
			        	 if(resultForMainInsert>0)
					 {
			        		
			        		 int vno=0;
			        	      
			              	try{
			             		  con= ConnectionFromSqlServer.connectionOpenWithTransaction();
			         	    	 con.setAutoCommit(false);
			         	         stmt = con.createStatement();
			         	         String sql="select LVRQId as vno  from TransLeaveRequest where LVRDocId='"+docId+"'";
			         	         System.out.println(sql);
			         	         rs=stmt.executeQuery(sql);
			         			 while (rs.next()) {
			         				 vno=rs.getInt("vno");
			         				System.out.println("max visitNo value="+vno);	
			         			}
			              	} 
			              	catch(Exception e){
			         				 
			              		System.out.println(e);
			         			 }
			              	finally{
			              		
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
			         	          transLeaveController.open();
			         	         transLeaveController.updateLeaveRequestUpload(transLeaveRequestList.get(j).getAndroid_id(),docId,vno);
			         	        transLeaveController.close();	 
						 
					 }
			        	 System.out.println("Commiting data here....");
			    		 
			             try{
			            	 setDocID("LVR", data);
			            	 }
			            	 catch(Exception e)
			            	 {
			            		 System.out.println(e);
			            	 } 
					 }
					 }
			     	 catch(SQLException se){
				          //Handle errors for JDBC
				          se.printStackTrace();
				          // If there is an error then rollback the changes.
				          System.out.println("Rolling back data here....");
				    	  try{
				    		 if(con!=null)
				                con.rollback();
				          }catch(SQLException se2){
				             se2.printStackTrace();
				          }//end try

				       }catch(Exception e){
				          //Handle errors for Class.forName
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
//					ndate=sdf1.parse(demoTransactions.get(j).getNextVisitDate());
				} catch (java.text.ParseException e) {
					exceptionData.setExceptionData(e.toString(), "insertDsr");
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	       	data=null;
	      	   data = new java.sql.Date(date.getTime());
							 
//				 String party_code="A"+String.format("%05d",Integer.valueOf(conper))+String.format("%05d",Integer.valueOf(parties.get(j).getArea_id()))+"S"+String.format("%07d",maxPartySerailNo++);
//	      	String query="";
//	      	   try{
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
//	      	   }
//				catch(Exception e){
//					System.out.println(e);	
////					System.out.println("query="+query);	
////					Toast.makeText(context, "e="+e, Toast.LENGTH_LONG);
////					Toast.makeText(context, "query="+query, Toast.LENGTH_LONG);
//				}
				 
				 int resultId=0;
				 	String pno="";
				 	System.out.println(query);
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
//		      proc.registerOutParameter(3, Types.VARCHAR); 
		      proc.execute();
//		      r=proc.getInt(3);
		      
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
