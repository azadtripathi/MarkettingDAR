package com.dm.library;

import android.content.Context;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class ConnectionFromSqlServer {
	 static ArrayList<String> valuesArrayList;
	 static ExceptionData exceptionData;
	 private static String server= Constant.SERVER_DATABASE_URL;
	 private static String db= Constant.DATABASE_NAME;
	 private static String username= Constant.DATABASE_USER_NAME;
	 private static String pass= Constant.DATABASE_PASSWORD;
	 private static String	connectionUrl = "jdbc:jtds:sqlserver://"+server+":"+ Constant.DATABASE_PORT+"/"+db+";encrypt=fasle;user="+username+";password="+pass+";";
	      // Declare the JDBC objects.
	     static  Connection con = null;
	     /* (non-Javadoc)
		 * @see com.example.rcl_app.ReadValuesFromFile.ValueReadFromFile#setValue(java.util.ArrayList)
		 */
	
		static  Statement stmt = null;
	     static  ResultSet rs = null;
	     Context context;
    public  ConnectionFromSqlServer( Context context,ExceptionData activity)
     {
    	ConnectionFromSqlServer.exceptionData=activity;
    	System.out.println(connectionUrl);
     }
	public static Statement connectionOpen()
	{
		  try {
			  String username = Constant.DATABASE_USER_NAME;
				String password = Constant.DATABASE_PASSWORD;
		         // Establish the connection.
		         Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
		         con = DriverManager.getConnection(connectionUrl,username,password);
		         stmt = con.createStatement();
		  }
		  catch (Exception e) {
			  
				  exceptionData.setExceptionData(e.getMessage(),"connectionOpen");
			 
			 e.printStackTrace();
		      }
		  return stmt;
	}
	public static Connection connectionOpenWithTransaction()
	{
		  try {
		         // Establish the connection.
		         Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
		         con = DriverManager.getConnection(connectionUrl);
		  }
		  catch (Exception e) {
			  
				  exceptionData.setExceptionData(e.getMessage(),"connectionOpen");
			 
			 e.printStackTrace();
		      }
		  return con;
	}
	public static Connection connectionOpenForConnectionObject()
	{
		  try {
		         // Establish the connection.
		         Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
		         con = DriverManager.getConnection(connectionUrl);
		  }
		  catch (Exception e) {
		         e.printStackTrace();
		      }
		  return con;
	}
	public static void connectionClosed()
	{
		try {
			//Close the connection.

	         if (rs != null) try { rs.close(); } catch(Exception e) {exceptionData.setExceptionData(e.getMessage(),"connectionClosed");}
	         if (stmt != null) try { stmt.close(); } catch(Exception e) {exceptionData.setExceptionData(e.getMessage(),"connectionClosed");}
	         if (con != null) try { con.close(); } catch(Exception e) {exceptionData.setExceptionData(e.getMessage(),"connectionClosed");}
	      }
		catch (Exception e) {
	         e.printStackTrace();
	      }
	}

	
}
