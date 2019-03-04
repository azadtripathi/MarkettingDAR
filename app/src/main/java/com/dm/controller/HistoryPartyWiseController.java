/**
 * AreaController.javacom.example.controller
 */
package com.dm.controller;

/**
 * @author dataman
 *
 */

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.library.DateFunction;
import com.dm.model.Collection;
import com.dm.model.Competitor;
import com.dm.model.DemoTransaction;
import com.dm.model.Distributor;
import com.dm.model.FailedVisit;
import com.dm.model.HistoryPartyWise;
import com.dm.model.Order;
import com.dm.model.Order1;
import com.dm.model.OrderTransaction;
import com.dm.model.TransCollection;
import com.dm.model.Visit;

import java.util.ArrayList;

public class HistoryPartyWiseController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_AREATYPE_CODE,
			DatabaseConnection.COLUMN_CITY_CODE,
			DatabaseConnection.COLUMN_DIST_CODE};
	
	public HistoryPartyWiseController(Context context) {
		dbHelper = new DatabaseConnection(context);
	}
	public void open() {
		try{
		database = dbHelper.getWritableDatabase();
		}catch(SQLException e){
			System.out.println("-----------------"+e.getMessage());
		}
	}

	public void close() {
		dbHelper.close();
	}
	public ArrayList<HistoryPartyWise> getHistroyList(String partyCode){
		
		String query="select a.visit_date,a.Android_id,a.type  from" +
	            "(select Android_id,visit_date, 'Order' as Type from TransOrder " 
				+"where party_code='"+partyCode+"' union all  select Android_id,visit_date, 'Demo' as Type from TransDemo "
				+"where party_code='"+partyCode+"' union all  select Android_id,visit_date, 'FailedVisit' as Type from Trans_Failed_visit " 
				+"where party_code='"+partyCode+"' union all  select Android_id,visit_date,  'Competitor' as Type from TransCompetitor "
				+"where party_code='"+partyCode+"' union all select  Android_id,payment_date as visit_date,'Collection' as Type from TransCollection where party_code='"+partyCode+"'  )" 
				+" a group by a.visit_date,a.Android_id,a.type order by substr( a.visit_date,7,4),substr( a.visit_date,4,2),substr( a.visit_date,1,2)  ";
			
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<HistoryPartyWise> historyPartyWises = new ArrayList<HistoryPartyWise>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				HistoryPartyWise historyPartyWise = new HistoryPartyWise();
				historyPartyWise.setDoc_id(cursor.getString(1));
				historyPartyWise.setDate(cursor.getString(0));
				historyPartyWise.setType(cursor.getString(2));
				historyPartyWises.add(historyPartyWise);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return historyPartyWises;
	}
	
	
public ArrayList<HistoryPartyWise> getHistroyList(String partyCode, String aPartyCode, String mLock ){
	String query="";
//	query="select a.visit_date,a.type  from"
//			+ "(select  distinct                 ot.visit_date,       'Order' as Type from TransOrder ot          LEFT JOIN Visitl1 vl1 where  vl1.dsr_lock='"+mLock+"' and (ot.party_code='"+partyCode+"' or ot.and_party_code='"+aPartyCode+"') union all  "
//			+"  select  distinct                 td.visit_date,        'Demo' as Type from TransDemo td           LEFT JOIN Visitl1 vl1 where  vl1.dsr_lock='"+mLock+"' and (td.party_code='"+partyCode+"' or td.and_party_code='"+aPartyCode+"')union all  "
//			+"	select  distinct                 fv.visit_date, 'FailedVisit' as Type from Trans_Failed_visit fv  LEFT JOIN Visitl1 vl1 where  vl1.dsr_lock='"+mLock+"' and (fv.party_code='"+partyCode+"' or fv.and_party_code='"+aPartyCode+"')union all  "
//			+"  select  distinct                 tc.visit_date,  'Competitor' as Type from TransCompetitor tc     LEFT JOIN Visitl1 vl1 where  vl1.dsr_lock='"+mLock+"' and (tc.party_code='"+partyCode+"' or tc.and_party_code='"+aPartyCode+"')union all "
//			+"  select  distinct tl.payment_date as visit_date,  'Collection' as Type from TransCollection tl     LEFT JOIN Visitl1 vl1 where  vl1.dsr_lock='"+mLock+"' and (tl.party_code='"+partyCode+"' or tl.and_party_code='"+aPartyCode+"') )"
//			+" a group by a.visit_date,a.type order by substr( a.visit_date,7,4),substr( a.visit_date,4,2),substr( a.visit_date,1,2)  ";



//	query="select b.visit_date,b.type  from (select a.visit_date,a.type  from("
//			+ " select  distinct  ot.visit_date,       'Order' as Type from TransOrder ot          where (ot.party_code='"+partyCode+"' or ot.and_party_code='"+aPartyCode+"') union all"
//			+ " select  distinct  td.visit_date,        'Demo' as Type from TransDemo td           where (td.party_code='"+partyCode+"' or td.and_party_code='"+aPartyCode+"')union all"
//			+ " select  distinct                 fv.visit_date, 'FailedVisit' as Type from Trans_Failed_visit fv  where (fv.party_code='"+partyCode+"' or fv.and_party_code='"+aPartyCode+"')union all"
//			+ " select  distinct                 tc.visit_date,  'Competitor' as Type from TransCompetitor tc     where (tc.party_code= '"+partyCode+"' or tc.and_party_code='"+aPartyCode+"')union all"
//			+ " select  distinct tl.payment_date as visit_date,  'Collection' as Type from TransCollection tl     where (tl.party_code='"+partyCode+"' or tl.and_party_code='"+aPartyCode+"')"
//
//			+ " ) a LEFT JOIN Visitl1 vl1 on a.visit_date=vl1.visit_date where vl1.dsr_lock='"+mLock+"') b"
//
//			+ " group by b.visit_date,b.type order by substr( b.visit_date,7,4),substr(b.visit_date,4,2),substr(b.visit_date,1,2)";

	query="select b.visit_date,b.type,ifnull(b.dsr_lock,'1') as Lck from (" +
			"select a.visit_date,a.type,vl1.dsr_lock from( " +
			"select  distinct  ot.visit_date,         'Order' as Type from TransOrder ot  where (ot.party_code='"+partyCode+"' or ot.and_party_code='"+aPartyCode+"')  union all " +
			"select  distinct  td.visit_date,         'Demo' as Type from TransDemo td  where (td.party_code='"+partyCode+"' or td.and_party_code='"+aPartyCode+"') union all " +
			"select  distinct   fv.visit_date,  'FailedVisit' as Type from Trans_Failed_visit fv  where (fv.party_code='"+partyCode+"' or fv.and_party_code='"+aPartyCode+"') union all " +
			"select  distinct  tc.visit_date,  'Competitor' as Type from TransCompetitor tc where (tc.party_code= '"+partyCode+"' or tc.and_party_code='"+aPartyCode+"') union all " +
			"select  distinct tl.payment_date as visit_date,  'Collection' as Type from TransCollection tl where (tl.party_code='"+partyCode+"' or tl.and_party_code='"+aPartyCode+"')" +
			"" +
			" ) a " +
			"" +
			"LEFT JOIN Visitl1 vl1 on a.visit_date=vl1.visit_date ) b group by b.visit_date,b.type order by substr( b.visit_date,7,4),substr(b.visit_date,4,2),substr(b.visit_date,1,2)";

	System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<HistoryPartyWise> historyPartyWises = new ArrayList<HistoryPartyWise>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				HistoryPartyWise historyPartyWise = new HistoryPartyWise();
				historyPartyWise.setDoc_id("0");
				historyPartyWise.setDate(cursor.getString(0));
				historyPartyWise.setType(cursor.getString(1));
				historyPartyWise.setLock(cursor.getString(2));
				historyPartyWises.add(historyPartyWise);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return historyPartyWises;
	}

	public ArrayList<Order> getProductOrderPartyWiseList(String doc_id, String date, String party_code){
		String query="select td.visit_date as visitdate,td.remark as remark,td.amount as amount,ma.name as areaname "+
						"from TransOrder td left join MastArea ma on ma.webcode=td.area_code where td.party_code='"+party_code+"' and td.visit_date='"+date+"' ";
				
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Order> orderTransactions = new ArrayList<Order>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Order orderTransaction = new Order();
				orderTransaction.setVDate(cursor.getString(0));
				orderTransaction.setRemarks(cursor.getString(1));
				orderTransaction.setOrderAmount(cursor.getString(2));
				orderTransaction.setAreaId(cursor.getString(3));
				orderTransactions.add(orderTransaction);
			    cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}
	
	public ArrayList<Order> getProductOrderPartyWiseList(String doc_id, String date, String party_code, String aparty_code){
		String query="";
		if(party_code!=null && aparty_code!=null)	
		{
			 query="select td.visit_date as visitdate," +
						"td.remark as remark,td.amount as amount,"+
						"ma.name as areaname,td.web_doc_id,td.Android_id "+
						"from TransOrder td "+
						"left join MastArea ma on ma.webcode=td.area_code "+
						"where td.party_code='"+party_code+"' and td.visit_date='"+date+"' ";

		}
		else if(party_code!=null && aparty_code==null)
	{
		
			 query="select td.visit_date as visitdate," +
						"td.remark as remark,td.amount as amount,"+
						"ma.name as areaname,td.web_doc_id,td.Android_id "+
						"from TransOrder td "+
						"left join MastArea ma on ma.webcode=td.area_code "+
						"where td.party_code='"+party_code+"' and td.visit_date='"+date+"' ";

		
	}
	
	else if(party_code==null && aparty_code!=null)
	{
	
		 query="select td.visit_date as visitdate," +
					"td.remark as remark,td.amount as amount,"+
					"ma.name as areaname,td.web_doc_id,td.Android_id "+
					"from TransOrder td "+
					"left join MastArea ma on ma.webcode=td.area_code "+
					"where td.and_party_code='"+aparty_code+"' and td.visit_date='"+date+"' ";

	}
	
		
				
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Order> orderTransactions = new ArrayList<Order>();
		if(cursor.moveToFirst())
		{
			while(!(cursor.isAfterLast()))
			{				
				Order orderTransaction = new Order();
				orderTransaction.setVDate(cursor.getString(0));
				orderTransaction.setRemarks(cursor.getString(1));
				orderTransaction.setOrderAmount(cursor.getString(2));
				orderTransaction.setAreaId(cursor.getString(3));
				orderTransaction.setWebCode(cursor.getString(4));
				orderTransaction.setAndroid_id(cursor.getString(5));
				orderTransactions.add(orderTransaction);
			    cursor.moveToNext();
			}
		}
		else{
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}
	
	
	/*public ArrayList<Order1> getMultiProductOrderPartyWiseList(String doc_id,String date,String odoc_id){
		String query="";
		
		if(odoc_id==null && doc_id!=null)
		{
		 query="select td.visit_date," +
						"td.amount,"+
						"td.qty,td.rate,ma.name,pm.name,p.name,td.web_doc_id "+
						"from TransOrder1 td "+
						"left join MastArea ma on ma.webcode=td.area_code "+
						"left join  MastProduct pm on td.product_code=pm.webcode "+
						"left join mastParty p on (td.party_code=p.webcode or td.and_party_code=p.Android_id) "+
						"where td.Android_id='"+doc_id+"' and td.visit_date='"+date+"' ";
		}
		else if(odoc_id!=null && doc_id==null)
		{
			query="select td.visit_date," +
					"td.amount,"+
					"td.qty,td.rate,ma.name,pm.name,p.name,td.web_doc_id "+
					"from TransOrder1 td "+
					"left join MastArea ma on ma.webcode=td.area_code "+
					"left join  MastProduct pm on td.product_code=pm.webcode "+
					"left join mastParty p on (td.party_code=p.webcode or td.and_party_code=p.Android_id) "+
					"where td.web_doc_id='"+odoc_id+"' and td.visit_date='"+date+"' ";
			
		}
		else if(odoc_id!=null && doc_id!=null)
		{
			query="select td.visit_date," +
					"td.amount,"+
					"td.qty,td.rate,ma.name,pm.name,p.name,td.web_doc_id "+
					"from TransOrder1 td "+
					"left join MastArea ma on ma.webcode=td.area_code "+
					"left join  MastProduct pm on td.product_code=pm.webcode "+
					"left join mastParty p on (td.party_code=p.webcode or td.and_party_code=p.Android_id) "+
					"where td.web_doc_id='"+odoc_id+"' and td.visit_date='"+date+"' ";
			
		}
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Order1> orderTransactions = new ArrayList<Order1>();
		orderTransactions.clear();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Order1 orderTransaction = new Order1();	
				orderTransaction.setVDate(cursor.getString(0));
				orderTransaction.setAmount((cursor.getString(1)==null?"0.0":cursor.getString(1)));
				orderTransaction.setQty(cursor.getString(2));
				orderTransaction.setRate(cursor.getString(3));
				orderTransaction.setAreaId(cursor.getString(4));
				orderTransaction.setItemId(cursor.getString(5));
				orderTransaction.setPartyId(cursor.getString(6));
				orderTransaction.setOrdDocId(cursor.getString(7));
				orderTransactions.add(orderTransaction);
			    cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}*/

	public ArrayList<Order1> getMultiProductOrderPartyWiseList(String doc_id, String date, String odoc_id){
		String query="";

		if(odoc_id==null && doc_id!=null)
		{
			query="select td.visit_date," +
					"td.amount,"+
					"td.qty,td.rate,ma.name,pm.name,p.name,td.order1_no,td.Ord1Android_id,td.party_code "+
					"from TransOrder1 td "+
					"left join MastArea ma on ma.webcode=td.area_code "+
					"left join  MastProduct pm on td.product_code=pm.webcode "+
					"left join mastParty p on (td.party_code=p.webcode or td.and_party_code=p.Android_id) "+
					"where td.Android_id='"+doc_id+"' and td.visit_date='"+date+"' ";
		}
		else if(odoc_id!=null && doc_id==null)
		{
			query="select td.visit_date," +
					"td.amount,"+
					"td.qty,td.rate,ma.name,pm.name,p.name,td.order1_no,td.Ord1Android_id,td.party_code "+
					"from TransOrder1 td "+
					"left join MastArea ma on ma.webcode=td.area_code "+
					"left join  MastProduct pm on td.product_code=pm.webcode "+
					"left join mastParty p on (td.party_code=p.webcode or td.and_party_code=p.Android_id) "+
					"where td.web_doc_id='"+odoc_id+"' and td.visit_date='"+date+"' ";

		}
		else if(odoc_id!=null && doc_id!=null)
		{
			query="select td.visit_date," +
					"td.amount,"+
					"td.qty,td.rate,ma.name,pm.name,p.name,td.order1_no,td.Ord1Android_id,td.party_code "+
					"from TransOrder1 td "+
					"left join MastArea ma on ma.webcode=td.area_code "+
					"left join  MastProduct pm on td.product_code=pm.webcode "+
					"left join mastParty p on (td.party_code=p.webcode or td.and_party_code=p.Android_id) "+
					"where td.web_doc_id='"+odoc_id+"' and td.visit_date='"+date+"' ";

		}
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Order1> orderTransactions = new ArrayList<Order1>();
		orderTransactions.clear();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				Order1 orderTransaction = new Order1();
				orderTransaction.setVDate(cursor.getString(0));
				orderTransaction.setAmount((cursor.getString(1)==null?"0.0":cursor.getString(1)));
				orderTransaction.setQty(cursor.getString(2));
				orderTransaction.setRate(cursor.getString(3));
				orderTransaction.setAreaId(cursor.getString(4));
				orderTransaction.setItemId(cursor.getString(5));
				orderTransaction.setPartyId(cursor.getString(6));
				orderTransaction.setOrdDocId(cursor.getString(7));
				orderTransaction.setOrderAndroid_Id(cursor.getString(8));
				orderTransaction.setPartyCode(cursor.getString(9));
				orderTransactions.add(orderTransaction);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}
	
	public ArrayList<Order1> getMultiProductOrderPartyVisitWiseList(String doc_id, String date, String party_code, String aparty_code){
		String query="";
		
		if(party_code!=null && aparty_code!=null)	
		{
		 query="select distinct td.visit_date," +
						"td.amount,"+
						"td.qty,td.rate,ma.name,pm.name,p.name,td.web_doc_id "+
						"from TransOrder1 td "+
						"left join MastArea ma on ma.webcode=td.area_code "+
						"left join  MastProduct pm on td.product_code=pm.webcode "+
						"left join mastParty p on td.party_code=p.Android_id "+
						"where td.party_code='"+party_code+"' and td.visit_date='"+date+"' ";
		}
		else if(party_code!=null && aparty_code==null)
		{
			query="select distinct td.visit_date," +
					"td.amount,"+
					"td.qty,td.rate,ma.name,pm.name,p.name,td.web_doc_id "+
					"from TransOrder1 td "+
					"left join MastArea ma on ma.webcode=td.area_code "+
					"left join  MastProduct pm on td.product_code=pm.webcode "+
					"left join mastParty p on td.party_code=p.Android_id "+
					"where td.party_code='"+party_code+"' and td.visit_date='"+date+"' ";
			
		}
		else if(party_code==null && aparty_code!=null)
		{
			query="select distinct td.visit_date," +
					"td.amount,"+
					"td.qty,td.rate,ma.name,pm.name,p.name,td.web_doc_id "+
					"from TransOrder1 td "+
					"left join MastArea ma on ma.webcode=td.area_code "+
					"left join  MastProduct pm on td.product_code=pm.webcode "+
					"left join mastParty p on td.party_code=p.Android_id "+
					"where td.and_party_code='"+aparty_code+"' and td.visit_date='"+date+"' ";
			
		}
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Order1> orderTransactions = new ArrayList<Order1>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Order1 orderTransaction = new Order1();
				orderTransaction.setVDate(cursor.getString(0));
				orderTransaction.setAmount((cursor.getString(1)==null?"0.0":cursor.getString(1)));
				orderTransaction.setQty(cursor.getString(2));
				orderTransaction.setRate(cursor.getString(3));
				orderTransaction.setAreaId(cursor.getString(4));
				orderTransaction.setItemId(cursor.getString(5));
				orderTransaction.setPartyId(cursor.getString(6));
				orderTransaction.setOrdDocId(cursor.getString(7));
				orderTransactions.add(orderTransaction);
			    cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}
	
	
	public ArrayList<DemoTransaction> getProductDemnoPartyWiseList(String doc_id, String date, String party_code){
		String query="select td.visit_date as visitdate,td.remark as remark,"+
					  "mc.name as classname,ms.name as segmentname,mp.name as productgrpname,"+
				      "ma.name as areaname "+
					   "from TransDemo td "+
						"left join MastClass mc on mc.webcode=td.classid "+
						"left join MastSegment ms on ms.webcode=td.segmentId "+
						"left join MastProdGroup mp on mp.webcode=td.product_group_code "+
						"left join MastArea ma on ma.webcode=td.area_code "+
						"where td.party_code='"+party_code+"' and td.visit_date='"+date+"' ";
				
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<DemoTransaction> demoTransactions = new ArrayList<DemoTransaction>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				DemoTransaction demoTransaction = new DemoTransaction();
				demoTransaction.setVDate(cursor.getString(0));
				demoTransaction.setRemarks(cursor.getString(1));
				demoTransaction.setProductClassId(cursor.getString(2));
				demoTransaction.setProductSegmentId(cursor.getString(3));
				demoTransaction.setProductMatGrp(cursor.getString(4));
				demoTransaction.setAreaId(cursor.getString(5));
				demoTransactions.add(demoTransaction);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return demoTransactions;
	}
	
	public ArrayList<DemoTransaction> getProductDemnoPartyWiseList(String doc_id, String date, String party_code, String aparty_code){
		String query="";
		
		if(party_code!=null && aparty_code!=null)	
		{
			 query="select td.visit_date as visitdate,td.remark as remark,"+
					  "mc.name as classname,ms.name as segmentname,mp.name as productgrpname,"+
				      "ma.name as areaname,td.path,td.web_doc_id,td.party_code "+
					   "from TransDemo td "+
						"left join MastClass mc on mc.webcode=td.classid "+
						"left join MastSegment ms on ms.webcode=td.segmentId "+
						"left join MastProdGroup mp on mp.webcode=td.product_group_code "+
						"left join MastArea ma on ma.webcode=td.area_code "+
						"where td.party_code='"+party_code+"' and td.visit_date='"+date+"' ";

		}
		else if(party_code!=null && aparty_code==null)
	{
		
			 query="select td.visit_date as visitdate,td.remark as remark,"+
					  "mc.name as classname,ms.name as segmentname,mp.name as productgrpname,"+
				      "ma.name as areaname,td.path,td.web_doc_id,td.party_code "+
					   "from TransDemo td "+
						"left join MastClass mc on mc.webcode=td.classid "+
						"left join MastSegment ms on ms.webcode=td.segmentId "+
						"left join MastProdGroup mp on mp.webcode=td.product_group_code "+
						"left join MastArea ma on ma.webcode=td.area_code "+
						"where td.party_code='"+party_code+"' and td.visit_date='"+date+"' ";

		
	}
	
	else if(party_code==null && aparty_code!=null)
	{
	
		 /*query="select td.visit_date as visitdate,td.remark as remark,"+
				  "mc.name as classname,ms.name as segmentname,mp.name as productgrpname,"+
			      "ma.name as areaname,td.path,td.web_doc_id,td.party_code "+
				   "from TransDemo td "+
					"left join MastClass mc on mc.webcode=td.classid "+
					"left join MastSegment ms on ms.webcode=td.segmentId "+
					"left join MastProdGroup mp on mp.webcode=td.product_group_code "+
					"left join MastArea ma on ma.webcode=td.area_code "+
					"where td.and_party_code='"+aparty_code+"' and td.visit_date='"+date+"' ";
*/
		query="select mc.name,ms.name,mp.name,d.remark,d.party_code," +
				"d.visit_date,d.web_doc_id,d.Android_id,d.path,d.party_code,d.and_party_code " +
				"from transdemo d " +
				"left join mastclass mc on d.classid=mc.webcode " +
				"left join mastsegment ms on d.segmentid=ms.webcode  " +
				"left join MastProdGroup mp on d.product_group_code=mp.webcode " +
				"where d.and_party_code='"+aparty_code+"' and d.visit_date='"+aparty_code+"'";

	}
	
						
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<DemoTransaction> demoTransactions = new ArrayList<DemoTransaction>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				DemoTransaction demoTransaction = new DemoTransaction();
				demoTransaction.setVDate(cursor.getString(0));
				demoTransaction.setRemarks(cursor.getString(1));
				demoTransaction.setProductClassId(cursor.getString(2));
				demoTransaction.setProductSegmentId(cursor.getString(3));
				demoTransaction.setProductMatGrp(cursor.getString(4));
				demoTransaction.setAreaId(cursor.getString(5));
				String file_path = (cursor.getString(6));
				String docId = cursor.getString(7);
				demoTransaction.setDemoDocId(docId);
				demoTransaction.setPartyId(cursor.getString(8));
				if(file_path.equalsIgnoreCase("Na") ||file_path.equalsIgnoreCase("N/a")|| file_path.isEmpty() || file_path == null)
				{
					demoTransaction.setFilePath("");
				}
				else
				{
					demoTransaction.setFilePath((cursor.getString(6)));
				}

				demoTransactions.add(demoTransaction);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return demoTransactions;
	}
	
	public ArrayList<FailedVisit> getFailedVisitPartyWiseList(String doc_id, String date, String party_code){
		String query="select td.visit_date as visitdate,td.remark as remark,"+
						"td. next_visit_date as  next_visit_date,"+
						"td. time,ma.name as areaname "+
						"from Trans_Failed_visit td "+
						"left join MastArea ma on ma.webcode=td.area_code "+
						"where td.party_code='"+party_code+"' and td.visit_date='"+date+"' ";
				
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<FailedVisit> failedVisits = new ArrayList<FailedVisit>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				FailedVisit failedVisit = new FailedVisit();
				failedVisit.setVDate(cursor.getString(0));
				failedVisit.setRemarks(cursor.getString(1));
				failedVisit.setNextvisit(cursor.getString(2));
				failedVisit.setVtime(cursor.getString(3));
				failedVisit.setAreaId(cursor.getString(4));
				failedVisits.add(failedVisit);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return failedVisits;
	}
	
	public ArrayList<FailedVisit> getFailedVisitPartyWiseList(String doc_id, String date, String party_code, String aparty_code){
		String query="";
		
		if(party_code!=null && aparty_code!=null)	
		{
			 query="select td.visit_date as visitdate,td.remark as remark,"+
						"td. next_visit_date as  next_visit_date,"+
						"td. time,ma.name as areaname,mv.name,td.web_doc_id,td.Android_id,mv.name as reason "+
						"from Trans_Failed_visit td "+
						"left join MastArea ma on ma.webcode=td.area_code "+
					    "left join mastFailedVisit mv on mv.webcode=td.reasonId "+
						"where td.party_code='"+party_code+"' and td.visit_date='"+date+"' ";

		}
		else if(party_code!=null && aparty_code==null)
	{
		
			 query="select td.visit_date as visitdate,td.remark as remark,"+
						"td. next_visit_date as  next_visit_date,"+
					    "td. time,ma.name as areaname,mv.name,td.web_doc_id,td.Android_id,mv.name as reason  "+
						"from Trans_Failed_visit td "+
						"left join MastArea ma on ma.webcode=td.area_code "+
					    "left join mastFailedVisit mv on mv.webcode=td.reasonId "+
						"where td.party_code='"+party_code+"' and td.visit_date='"+date+"' ";

		
	}
	
	else if(party_code==null && aparty_code!=null)
	{
	
		 query="select td.visit_date as visitdate,td.remark as remark,"+
					"td. next_visit_date as  next_visit_date,"+
				    "td. time,ma.name as areaname,mv.name,td.web_doc_id,td.Android_id,mv.name as reason  "+
					"from Trans_Failed_visit td "+
					"left join MastArea ma on ma.webcode=td.area_code "+
				    "left join mastFailedVisit mv on mv.webcode=td.reasonId "+
					"where td.and_party_code='"+aparty_code+"' and td.visit_date='"+date+"' ";

	}

				
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<FailedVisit> failedVisits = new ArrayList<FailedVisit>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				FailedVisit failedVisit = new FailedVisit();
				failedVisit.setVDate(cursor.getString(0));
				failedVisit.setRemarks(cursor.getString(1));
				failedVisit.setNextvisit(cursor.getString(2));
				failedVisit.setVtime(cursor.getString(3));
				failedVisit.setAreaId(cursor.getString(4));
				failedVisit.setReasonID(cursor.getString(5));
				failedVisit.setFVDocId(cursor.getString(6));
				failedVisit.setAndroidId(cursor.getString(7));
				failedVisits.add(failedVisit);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return failedVisits;
	}
	
	
	public ArrayList<Competitor> getCompetitorPartyWiseList(String doc_id, String date, String party_code){
		String query="select td.visit_date as visitdate,td.ItemName as ItemName,"+
						"td. qty as  qty,"+
						"td. rate "+
						"from TransCompetitor td "+
						"where td.party_code='"+party_code+"' and td.visit_date='"+date+"' ";
				
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Competitor> competitors = new ArrayList<Competitor>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Competitor competitor = new Competitor();
				competitor.setVDate(cursor.getString(0));
				competitor.setItem(cursor.getString(1));
				competitor.setQty(cursor.getString(2));
				competitor.setRate(cursor.getString(3));
				competitors.add(competitor);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return competitors;
	}
	
	public ArrayList<Competitor> getCompetitorPartyWiseList(String doc_id, String date, String party_code, String aparty_code){

		String query="";
		if(party_code!=null && aparty_code!=null)
		{
			query="select ItemName,qty,rate,remark,path,name,brandActivity," +
					"meetActivity,scheme,roadShow,generalInfo,discount,otherActivity,isUpload,Android_id,web_doc_id,visit_date " +
					"from TransCompetitor where party_code='"+party_code+"' and visit_date='"+date+"' ";

		}

		else if(party_code!=null && aparty_code==null)
		{

			query="select ItemName,qty,rate,remark,path,name,brandActivity," +
					"meetActivity,scheme,roadShow,generalInfo,discount,otherActivity,isUpload,Android_id,web_doc_id,visit_date " +
					"from TransCompetitor where party_code='"+party_code+"' and visit_date='"+date+"' ";


		}

		else if(party_code==null && aparty_code!=null)
		{

			query="select ItemName,qty,rate,remark,path,name,brandActivity," +
					"meetActivity,scheme,roadShow,generalInfo,discount,otherActivity,isUpload,Android_id,web_doc_id,visit_date " +
					"from TransCompetitor where and_party_code='"+aparty_code+"' and visit_date='"+date+"' ";

		}

		System.out.println(query);
		Cursor cursor=database.rawQuery(query, null);
		ArrayList<Competitor> competitors = new ArrayList<Competitor>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				Competitor competitor=new Competitor();
				competitor.setItem(cursor.getString(0));
				competitor.setQty(cursor.getString(1));
				competitor.setRate(cursor.getString(2));
				competitor.setRemark(cursor.getString(3));
				competitor.setCompetitorName(cursor.getString(5));
				competitor.setBrandActivity(cursor.getString(6));
				competitor.setMeetActivity(cursor.getString(7));
				competitor.setScheme(cursor.getString(8));
				competitor.setRoadShow(cursor.getString(9));
				competitor.setGeneralInfo(cursor.getString(10));
				competitor.setDiscount(cursor.getString(11));
				competitor.setOtherActivity(cursor.getString(12));
				competitor.setIsUpload(cursor.getString(13));
				competitor.setAndroid_id(cursor.getString(14));
				competitor.setDocId(cursor.getString(15));
				competitor.setVDate(cursor.getString(16));
				String file_path = cursor.getString(4);
				if(file_path.equalsIgnoreCase("Na") ||file_path.equalsIgnoreCase("N/a") || file_path.isEmpty() || file_path == null)
				{
					competitor.setFilePath("");
				}
				else
				{
					competitor.setFilePath(cursor.getString(4));
				}
				competitors.add(competitor);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return competitors;
	}
	
	public ArrayList<Competitor> getCompetitorDatewiseWiseList(String date){

		String query="select td.ItemName,td.qty,td.rate,td.remark,ifnull(td.path,'NA'),td.name,td.brandActivity," +
				"td.meetActivity,td.scheme,td.roadShow,td.generalInfo,td.discount,td.otherActivity,td.isUpload," +
				"td.Android_id,td.web_doc_id,td.visit_date,msp.name as partyname "+
						"from TransCompetitor td "+
						//"left join mastParty msp on msp.webcode=td.party_code "+
				"left join mastParty msp on (td.party_code=msp.webcode or td.and_party_code=msp.Android_id) "+
						"where td.visit_date='"+date+"' ";
				
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Competitor> competitors = new ArrayList<Competitor>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Competitor competitor = new Competitor();
				competitor.setItem(cursor.getString(0));
				competitor.setQty(cursor.getString(1));
				competitor.setRate(cursor.getString(2));
				competitor.setRemark(cursor.getString(3));
				competitor.setCompetitorName(cursor.getString(5));
				competitor.setBrandActivity(cursor.getString(6));
				competitor.setMeetActivity(cursor.getString(7));
				competitor.setScheme(cursor.getString(8));
				competitor.setRoadShow(cursor.getString(9));
				competitor.setGeneralInfo(cursor.getString(10));
				competitor.setDiscount(cursor.getString(11));
				competitor.setOtherActivity(cursor.getString(12));
				competitor.setIsUpload(cursor.getString(13));
				competitor.setAndroid_id(cursor.getString(14));
				competitor.setDocId(cursor.getString(15));
				competitor.setVDate(cursor.getString(16));
				competitor.setPartyId(cursor.getString(17));
				String file_path = cursor.getString(4);
				if(file_path.equalsIgnoreCase("Na") ||file_path.equalsIgnoreCase("N/a")|| file_path.isEmpty() || file_path == null)
				{
					competitor.setFilePath("");
				}
				else
				{
					competitor.setFilePath(cursor.getString(4));
				}
				competitors.add(competitor);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return competitors;
	}
	public ArrayList<TransCollection> getCollectionDateWiseList(String date){
		String query="select td.visit_date as visitdate,td.amount as amount,"+
						"td.payment_date as  payment_date,"+
						"td.cheque_ddno as chek_no,td.chq_date as chq_date,td.mode as mode, " +
						"td.bank as bank,td.branch as branch,td.remark as remark,"+
						"msp.name as partyname,td.Android_id as androidId,td.web_doc_id "+
						"from TransCollection td "+
						//"left join mastParty msp on msp.webcode=td.party_code "+
				"left join mastParty msp on(td.party_code=msp.webcode or td.and_party_code=msp.Android_id) "+
				"where td.visit_date='"+date+"' ";
				
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<TransCollection> transCollections = new ArrayList<TransCollection>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				TransCollection transCollection = new TransCollection();
				transCollection.setVDate(cursor.getString(0));
				transCollection.setAmount(cursor.getString(1));
				transCollection.setPaymentDate(cursor.getString(2));
				transCollection.setCheque_DDNo(cursor.getString(3));
				transCollection.setCheque_DD_Date(cursor.getString(4));
				transCollection.setMode(cursor.getString(5));
				transCollection.setBank(cursor.getString(6));
				transCollection.setBranch(cursor.getString(7));
				transCollection.setRemarks(cursor.getString(8));
				transCollection.setPartyName(cursor.getString(9));
				transCollection.setAndroid_id(cursor.getString(10));
				transCollection.setCollDocId(cursor.getString(11));
				transCollections.add(transCollection);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return transCollections;
	}
	public ArrayList<Collection> getDistCollectionDateWiseList(String date){
		String query="select td.visit_date as visitdate,td.amount as amount,"+
						"td.payment_date as  payment_date,"+
						"td.cheque_ddno as chek_no,td.chq_date as chq_date,td.mode as mode, " +
						"td.bank as bank,td.branch as branch,td.remark as remark,"+
						"msp.name as distname,td.Android_id as androidId,td.web_doc_id "+
						"from DistributerCollection td "+
						"left join mastDristributor msp on msp.webcode=td.DistId "+
						"where td.payment_date='"+date+"' ";
				
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Collection> collections = new ArrayList<Collection>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				Collection collection = new Collection();
				collection.setPaymentDate(cursor.getString(0));
				collection.setAmount(cursor.getString(1));
				collection.setPaymentDate(cursor.getString(2));
				collection.setCheque_DDNo(cursor.getString(3));
				collection.setCheque_DD_Date(cursor.getString(4));
				collection.setMode(cursor.getString(5));
				collection.setBank(cursor.getString(6));
				collection.setBranch(cursor.getString(7));
				collection.setRemarks(cursor.getString(8));
				collection.setDistName(cursor.getString(9));
				collection.setAndroid_id(cursor.getString(10));
				collection.setCollDocId(cursor.getString(11));
				collections.add(collection);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return collections;
	}
	public ArrayList<Visit> getDiscussionDatewiseWiseList(String date){
		String query="select td.Android_id,td.remark,fromtime1,totime1," +
				"nextVisitTime,next_visit_date,stock,ifnull(td.path,'NA'),td.web_doc_id," +
						"msp.name as distname,td.visit_date "+
						"from TransVisitDist td "+
						"left join mastDristributor msp on msp.webcode=td.DistId "+
						"where td.visit_date='"+date+"' ";

		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Visit> visits = new ArrayList<Visit>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				Visit visit = new Visit();
				visit.setAndroidDocId(cursor.getString(0));
				visit.setRemark(cursor.getString(1));
				visit.setfrTime1(cursor.getString(2));
				visit.setToTime1(cursor.getString(3));
				visit.setToTime2(cursor.getString(4));
				visit.setNextVisitDate(cursor.getString(5));
				visit.setStock(cursor.getString(6));
				visit.setVisitDocId(cursor.getString(8));
				visit.setDistId(cursor.getString(9));
				visit.setVdate(cursor.getString(10));
				String file_path = (cursor.getString(7));
				if(file_path.equalsIgnoreCase("Na")|| file_path.equalsIgnoreCase("N/a")|| file_path.isEmpty() || file_path == null)
				{
					visit.setFilePath("");
				}
				else
				{
					visit.setFilePath(cursor.getString(7));
				}
				visits.add(visit);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return visits;
	}
	
	public ArrayList<TransCollection> getCollectionPartyWiseList(String doc_id, String date, String party_code){
		String query="select td.visit_date as visitdate,td.amount as amount,"+
						"td. payment_date as  payment_date,"+
						"td. cheque_ddno,td.chq_date as chq_date," +
						"td.bank as bank,td.branch as branch,td.remark as remark,ma.name as areaid,td.mode as mode "+
						"from TransCollection td "+
						"left join MastArea ma on ma.webcode=td.area_code "+
						"where td.party_code='"+party_code+"' and td.visit_date='"+date+"' ";
				
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<TransCollection> transCollections = new ArrayList<TransCollection>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				TransCollection transCollection = new TransCollection();
				transCollection.setVDate(cursor.getString(0));
				transCollection.setAmount(cursor.getString(1));
				transCollection.setPaymentDate(cursor.getString(2));
				transCollection.setCheque_DDNo(cursor.getString(3));
				transCollection.setCheque_DD_Date(cursor.getString(4));
				transCollection.setBank(cursor.getString(5));
				transCollection.setBranch(cursor.getString(6));
				transCollection.setRemarks(cursor.getString(7));
				transCollection.setAreaId(cursor.getString(8));
				transCollection.setMode(cursor.getString(9));
				transCollections.add(transCollection);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return transCollections;
	}
	
	public ArrayList<TransCollection> getCollectionPartyWiseList(String doc_id, String date, String party_code, String aparty_code){
		String query="";
		
		if(party_code!=null && aparty_code!=null)	
		{
			 query="select td.visit_date as visitdate,td.amount as amount,"+
						"td. payment_date as  payment_date,"+
						"td. cheque_ddno,td.chq_date as chq_date," +
						"td.bank as bank,td.branch as branch,td.remark as remark,ma.name as areaid,td.mode as mode,mp.name as pname "+
						"from TransCollection td "+
						"left join MastArea ma on ma.webcode=td.area_code "+
					    "left join mastParty mp on mp.webcode=td.party_code "+
						"where td.party_code='"+party_code+"' and td.visit_date='"+date+"' ";

		}
		else if(party_code!=null && aparty_code==null)
	{
		
			 query="select td.visit_date as visitdate,td.amount as amount,"+
						"td. payment_date as  payment_date,"+
						"td. cheque_ddno,td.chq_date as chq_date," +
						"td.bank as bank,td.branch as branch,td.remark as remark,ma.name as areaid,td.mode as mode,mp.name as pname "+
						"from TransCollection td "+
						"left join MastArea ma on ma.webcode=td.area_code "+
					 "left join mastParty mp on mp.webcode=td.party_code "+
						"where td.party_code='"+party_code+"' and td.visit_date='"+date+"' ";
		
	}
	
	else if(party_code==null && aparty_code!=null)
	{
	
		 query="select td.visit_date as visitdate,td.amount as amount,"+
					"td. payment_date as  payment_date,"+
					"td. cheque_ddno,td.chq_date as chq_date," +
					"td.bank as bank,td.branch as branch,td.remark as remark,ma.name as areaid,td.mode as mode,mp.name as pname "+
					"from TransCollection td "+
					"left join MastArea ma on ma.webcode=td.area_code "+
				    "left join mastParty mp on mp.Android_id=td.and_party_code "+
					"where td.and_party_code='"+aparty_code+"' and td.visit_date='"+date+"' ";

	}
	
		
		
				
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<TransCollection> transCollections = new ArrayList<TransCollection>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				TransCollection transCollection = new TransCollection();
				transCollection.setVDate(cursor.getString(0));
				transCollection.setAmount(cursor.getString(1));
				transCollection.setPaymentDate(cursor.getString(2));
				transCollection.setCheque_DDNo(cursor.getString(3));
				transCollection.setCheque_DD_Date(cursor.getString(4));
				transCollection.setBank(cursor.getString(5));
				transCollection.setBranch(cursor.getString(6));
				transCollection.setRemarks(cursor.getString(7));
				transCollection.setAreaId(cursor.getString(8));
				transCollection.setMode(cursor.getString(9));
				transCollection.setPartyName(cursor.getString(10));
				transCollections.add(transCollection);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return transCollections;
	}
	
	
	public ArrayList<DemoTransaction> getAllDemoPartyListByDate(String date){
		String query="select td.visit_date as visitdate,td.remark as remark,"+
				  "mc.name as classname,ms.name as segmentname,mp.name as productgrpname,"+
			      "ma.name as areaname,msp.name as partyname,td. Android_id as androidId,ifnull(td.path,'NA'),td.web_doc_id "+
				   "from TransDemo td "+
					"left join MastClass mc on mc.webcode=td.classid "+
					"left join MastSegment ms on ms.webcode=td.segmentId "+
					"left join MastProdGroup mp on mp.webcode=td.product_group_code "+
					"left join MastArea ma on ma.webcode=td.area_code "+
					"left join mastParty msp on (td.party_code=msp.webcode or td.and_party_code=msp.Android_id) "+
					"where td.visit_date='"+date+"' ";
			
	System.out.println(query);
	Cursor cursor = database.rawQuery(query, null);
	ArrayList<DemoTransaction> demoTransactions = new ArrayList<DemoTransaction>();
	if(cursor.moveToFirst()){
		while(!(cursor.isAfterLast()))
		{				
			DemoTransaction demoTransaction = new DemoTransaction();
			demoTransaction.setVDate(cursor.getString(0));
			demoTransaction.setRemarks(cursor.getString(1));
			demoTransaction.setProductClassId(cursor.getString(2));
			demoTransaction.setProductSegmentId(cursor.getString(3));
			demoTransaction.setProductMatGrp(cursor.getString(4));
			demoTransaction.setAreaId(cursor.getString(5));
			demoTransaction.setPartyId(cursor.getString(6));
			demoTransaction.setAndroid_id(cursor.getString(7));
			demoTransaction.setWebDocId(cursor.getString(9));
			String file_path = cursor.getString(8);
			if(file_path.equalsIgnoreCase("Na")||file_path.equalsIgnoreCase("N/a") || file_path.isEmpty() || file_path == null)
			{
				demoTransaction.setFilePath("");
			}
			else
			{
				demoTransaction.setFilePath(cursor.getString(8));
			}

			demoTransactions.add(demoTransaction);
			cursor.moveToNext();
		}
	}else{
		System.out.println("No records found");
	}
	cursor.close();
	return demoTransactions;
	}
	public ArrayList<Order> getAllOrderPartyListByDate(String date){
//		String query="select td.visit_date as visitdate," +
//				"td.remark as remark,td.amount as amount,"+
//				"ma.name as areaname,td. Android_id as androidId,msp.name as partyname,td.web_doc_id as docId "+
//				"from TransOrder td "+
//				"left join MastArea ma on ma.webcode=td.area_code "+
//				"left join mastParty msp on msp.webcode=td.party_code "+
//				"where td.visit_date='"+date+"' ";

		String query="select td.visit_date as ovisitdate," +
				"td.remark as ordremark,td.amount as ordamount,"+
				"ma.name as areaname,td.Android_id as oandroidId,msp.name as partyname,td.web_doc_id as docId,sum(td1.qty) as tqty, mb.name as beatName"+
				" from TransOrder td "+
				"left join MastArea ma on ma.webcode=td.area_code "+
				"left join mastParty msp on (td.party_code=msp.webcode or td.and_party_code=msp.Android_id) "+
				"left join TransOrder1 td1 on td.Android_id=td1.Android_id left join MastBeat mb on ma.webcode=mb.area_code"+
				" where td.visit_date='"+date+"' group by ovisitdate,ordremark,ordamount,areaname,oandroidId,partyname,docId";
		
			System.out.println(query);
			Cursor cursor = database.rawQuery(query, null);
			ArrayList<Order> orderTransactions = new ArrayList<Order>();
if(cursor.moveToFirst()){
	while(!(cursor.isAfterLast()))
	{				
		Order orderTransaction = new Order();
		orderTransaction.setVDate(cursor.getString(0));
		orderTransaction.setRemarks(cursor.getString(1));
		orderTransaction.setOrderAmount(cursor.getString(2));
		orderTransaction.setAreaId(cursor.getString(3));
		orderTransaction.setAndroid_id(cursor.getString(4));
		orderTransaction.setPartyId(cursor.getString(5));
		orderTransaction.setOrdDocId(cursor.getString(6));
		orderTransaction.setQty(cursor.getString(7));
		orderTransaction.setBeatName(cursor.getString(8));

		orderTransactions.add(orderTransaction);
	    cursor.moveToNext();
	}
}else{
	System.out.println("No records found");
}
cursor.close();
	return orderTransactions;
	
	}
	public ArrayList<FailedVisit> getAllFailedPartyListByDate(String date){
		String query="select td.visit_date as visitdate,td.remark as remark,"+
				"td. next_visit_date as  next_visit_date,"+
				"td. time,ma.name as areaname,td. Android_id as androidId,msp.name as partyname, "+
				" msd.name as distName,td.and_party_code,td.reasonId,td.web_doc_id from Trans_Failed_visit td "+
				"left join MastArea ma on ma.webcode=td.area_code "+
				//"left join mastParty msp on msp.webcode=td.party_code "+
				"left join mastParty msp on (td.party_code=msp.webcode or td.and_party_code=msp.Android_id) "+
				"left join mastDristributor msd on msd.webcode=td.DistId "+
				"where td.visit_date='"+date+"' and (td.party_code IS NOT NULL or td.and_party_code IS NOT NULL)";
		
System.out.println(query);
Cursor cursor = database.rawQuery(query, null);
ArrayList<FailedVisit> failedVisits = new ArrayList<FailedVisit>();
if(cursor.moveToFirst()){
	while(!(cursor.isAfterLast()))
	{				
		FailedVisit failedVisit = new FailedVisit();
		failedVisit.setVDate(cursor.getString(0));
		failedVisit.setRemarks(cursor.getString(1));
		failedVisit.setNextvisit(cursor.getString(2));
		failedVisit.setVtime(cursor.getString(3));
		failedVisit.setAreaId(cursor.getString(4));
		failedVisit.setAndroidId(cursor.getString(5));
		failedVisit.setPartyId(cursor.getString(6));
		failedVisit.setDistId(cursor.getString(7));
		failedVisit.setAndPartyId(cursor.getString(8));
		failedVisit.setReasonID(cursor.getString(9));
		failedVisit.setWeb_doc_id(cursor.getString(10));
		failedVisits.add(failedVisit);
		cursor.moveToNext();
	}
}else{
	System.out.println("No records found");
}
cursor.close();
return failedVisits;
	}

	public ArrayList<FailedVisit> getAllFailedDistListByDate(String date){
		String query="select td.visit_date as visitdate,td.remark as remark,"+
				"td. next_visit_date as  next_visit_date,"+
				"td. time,ma.name as areaname,td. Android_id as androidId,msp.name as partyname, "+
				" msd.name as distName,td.reasonId,td.web_doc_id from Trans_Failed_visit td "+
				"left join MastArea ma on ma.webcode=td.area_code "+
				//"left join mastParty msp on msp.webcode=td.party_code "+
				"left join mastParty msp on (td.party_code=msp.webcode or td.and_party_code=msp.Android_id) "+
				"left join mastDristributor msd on msd.webcode=td.DistId "+
				"where td.visit_date='"+date+"' and td.DistId IS NOT NULL";

		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<FailedVisit> failedVisits = new ArrayList<FailedVisit>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				FailedVisit failedVisit = new FailedVisit();
				failedVisit.setVDate(cursor.getString(0));
				failedVisit.setRemarks(cursor.getString(1));
				failedVisit.setNextvisit(cursor.getString(2));
				failedVisit.setVtime(cursor.getString(3));
				failedVisit.setAreaId(cursor.getString(4));
				failedVisit.setAndroidId(cursor.getString(5));
				failedVisit.setPartyId(cursor.getString(6));
				failedVisit.setDistId(cursor.getString(7));
				failedVisit.setReasonID(cursor.getString(8));
				failedVisit.setWeb_doc_id(cursor.getString(9));
				failedVisits.add(failedVisit);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return failedVisits;
	}

	public ArrayList<OrderTransaction> getAllDate(){
		String currentDate= DateFunction.getCurrentDateByFromat("yyyy-MM-dd");
		String lastDate= DateFunction.addDaysToDate(currentDate, "-30", "yyyy-MM-dd","yyyy-MM-dd HH:mm:ss");
		String query="select vdate,max(demodate) as demo,max(odate) as ordr,max(fdate) as fvisit,max(cdate) as comp  from ( "+
					  "select visit_date as vdate,1 as demodate ,0 as odate     ,0 as fdate,0 as cdate from TransDemo  union all "+
					  "select visit_date as vdate,0 as demodate ,1 as odate     ,0 as fdate,0 as cdate from TransOrder  union all "+
					 "select visit_date as vdate,0 as demodate ,0 as orderadte,1 as fdate,0 as cdate from Trans_Failed_visit  union all "+ 
						 "select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,1 as cdate from TransCompetitor  union all "+ 
						 "select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as cdate from Visitl1 "+  
						")  a "+  
						 "group by vdate order by substr(vdate,7,4),substr(vdate,4,2),substr(vdate,1,2) desc";
				
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<OrderTransaction> orderTransactions = new ArrayList<OrderTransaction>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				OrderTransaction orderTransaction = new OrderTransaction();
				orderTransaction.setDate(cursor.getString(0));
				orderTransaction.setDemo(cursor.getString(1));
				orderTransaction.setOrder(cursor.getString(2));
				orderTransaction.setFailed(cursor.getString(3));
				orderTransaction.setCompt(cursor.getString(4));
				orderTransactions.add(orderTransaction);
				//System.out.println(cursor.getString(0));
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}
	public ArrayList<OrderTransaction> getAllPendingDate()
	{
		String query="select vdate,max(demodate) as demo,max(odate) as ordr,max(fdate) as fvisit,max(dfdate) as dfvisit,max(cdate) as comp,max(coll) as collection,max(distcoll) as distcollection, max(distdiscu) as discussion,CreatedDate  from ( "
				+" select visit_date as vdate,1 as demodate ,0 as odate     ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from TransDemo Where timestamp='0' union all "
				+" select visit_date as vdate,0 as demodate ,1 as odate     ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from TransOrder Where timestamp='0' union all "
				+" select visit_date as vdate,0 as demodate ,0 as odate,1 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from Trans_Failed_visit where ((DistId IS NULL and party_code IS NOT NULL) or (DistId IS NULL and and_party_code IS NOT NULL)) and  timestamp='0' union all "
				+" select visit_date as vdate,0 as demodate ,0 as odate,0 as fdate,1 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from Trans_Failed_visit where ((DistId IS NOT NULL and party_code IS NULL) or (DistId IS NOT NULL and and_party_code IS NULL)) and  timestamp='0' union all "
				+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,1 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from TransCompetitor Where timestamp='0' union all "
				+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,1 as coll,0 as distcoll,0 as distdiscu,CreatedDate from TransCollection Where timestamp='0' union all "
				+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,1 as distcoll,0 as distdiscu,CreatedDate from DistributerCollection Where timestamp='0' union all "
				+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,1 as distdiscu,CreatedDate  from TransVisitDist Where timestamp='0' union all "
				+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from Visitl1 Where timestamp='0' "
				+" )  a "
				+" group by vdate order by CreatedDate desc";

		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<OrderTransaction> orderTransactions = new ArrayList<OrderTransaction>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				OrderTransaction orderTransaction = new OrderTransaction();
				orderTransaction.setDate(cursor.getString(0));
				orderTransaction.setDemo(cursor.getString(1));
				orderTransaction.setOrder(cursor.getString(2));
				orderTransaction.setFailed(cursor.getString(3));
				orderTransaction.setDistfailed(cursor.getString(4));
				orderTransaction.setCompt(cursor.getString(5));
				orderTransaction.setCollection(cursor.getString(6));
				orderTransaction.setDistCollection(cursor.getString(7));
				orderTransaction.setDiscussion(cursor.getString(8));
				orderTransactions.add(orderTransaction);
				System.out.println(cursor.getString(0));
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}


	public ArrayList<OrderTransaction> getFromAndToPendingDate(String fromDate, String toDate, String type)
	{
		String query="";
		if(type.equalsIgnoreCase("PendingSync")){
			query="select vdate,max(demodate) as demo,max(odate) as ordr,max(fdate) as fvisit,max(dfdate) as dfvisit,max(cdate) as comp,max(coll) as collection,max(distcoll) as distcollection, max(distdiscu) as discussion,CreatedDate  from ( "
					+" select visit_date as vdate,1 as demodate ,0 as odate     ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from TransDemo Where timestamp='0' and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"')union all "
					+" select visit_date as vdate,0 as demodate ,1 as odate     ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from TransOrder Where timestamp='0' and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 as demodate ,0 as odate,1 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from Trans_Failed_visit where ((DistId IS NULL and party_code IS NOT NULL) or (DistId IS NULL and and_party_code IS NOT NULL)) and  timestamp='0' and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 as demodate ,0 as odate,0 as fdate,1 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from Trans_Failed_visit where ((DistId IS NOT NULL and party_code IS NULL) or (DistId IS NOT NULL and and_party_code IS NULL)) and  timestamp='0' and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,1 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from TransCompetitor Where timestamp='0' and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,1 as coll,0 as distcoll,0 as distdiscu,CreatedDate from TransCollection Where timestamp='0' and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,1 as distcoll,0 as distdiscu,CreatedDate from DistributerCollection Where timestamp='0' and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,1 as distdiscu,CreatedDate  from TransVisitDist Where timestamp='0' and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from Visitl1 Where timestamp='0' and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') "
					+" )  a "
					+" group by vdate order by CreatedDate desc";
		}else if(type.equalsIgnoreCase("Unlock")){
			/*query="select vdate,demodate as demo,odate as ordr,fdate as fvisit,dfdate as dfvisit,cdate as comp,coll as collection,distcoll as distcollection, distdiscu as discussion,CreatedDate,dsr_lock from ( "
					+" select visit_date as vdate,1 as demodate,0 as odate,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate,'' as dsr_lock from TransDemo Where timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"')union all "
					+" select visit_date as vdate,0 as demodate,1 as odate,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate,'' as dsr_lock from TransOrder Where timestamp IN(-1,0)and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 as demodate,0 as odate,1 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate,'' as dsr_lock from Trans_Failed_visit where ((DistId IS NULL and party_code IS NOT NULL) or (DistId IS NULL and and_party_code IS NOT NULL)) and  timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 as demodate,0 as odate,0 as fdate,1 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate,'' as dsr_lock from Trans_Failed_visit where ((DistId IS NOT NULL and party_code IS NULL) or (DistId IS NOT NULL and and_party_code IS NULL)) and  timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 demodate,0 as odate,0 as fdate,0 as dfdate,1 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate,'' as dsr_lock from TransCompetitor Where timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 demodate,0 as odate,0 as fdate,0 as dfdate,0 as cdate,1 as coll,0 as distcoll,0 as distdiscu,CreatedDate,'' as dsr_lock from TransCollection Where timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 demodate,0 as odate,0 as fdate,0 as dfdate,0 as cdate,0 as coll,1 as distcoll,0 as distdiscu,CreatedDate,'' as dsr_lock from DistributerCollection Where timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 demodate,0 as odate,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,1 as distdiscu,CreatedDate,'' as dsr_lock  from TransVisitDist Where timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 demodate,0 as odate,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate,dsr_lock from Visitl1 Where timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') "
					+" )  a where dsr_lock=0"
					+" order by CreatedDate desc";*/
			query = "select visit_date as vdate,0 demodate,0 as odate,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate,dsr_lock from Visitl1 Where timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') "
					+"  and dsr_lock=0 order by CreatedDate desc";
			
		}
		else{
			query="select vdate,max(demodate) as demo,max(odate) as ordr,max(fdate) as fvisit,max(dfdate) as dfvisit,max(cdate) as comp,max(coll) as collection,max(distcoll) as distcollection, max(distdiscu) as discussion,CreatedDate  from ( "
					+" select visit_date as vdate,1 as demodate ,0 as odate     ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from TransDemo Where timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"')union all "
					+" select visit_date as vdate,0 as demodate ,1 as odate     ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from TransOrder Where timestamp IN(-1,0)and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 as demodate ,0 as odate,1 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from Trans_Failed_visit where ((DistId IS NULL and party_code IS NOT NULL) or (DistId IS NULL and and_party_code IS NOT NULL)) and  timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 as demodate ,0 as odate,0 as fdate,1 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from Trans_Failed_visit where ((DistId IS NOT NULL and party_code IS NULL) or (DistId IS NOT NULL and and_party_code IS NULL)) and  timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,1 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from TransCompetitor Where timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,1 as coll,0 as distcoll,0 as distdiscu,CreatedDate from TransCollection Where timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,1 as distcoll,0 as distdiscu,CreatedDate from DistributerCollection Where timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,1 as distdiscu,CreatedDate  from TransVisitDist Where timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"') union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu,CreatedDate from Visitl1 Where timestamp IN(-1,0) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' ) "
					+" )  a where dsr_lock=0 "
					+" group by vdate order by CreatedDate desc";
		}
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<OrderTransaction> orderTransactions = new ArrayList<OrderTransaction>();
		int n = cursor.getCount();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				OrderTransaction orderTransaction = new OrderTransaction();
				orderTransaction.setDate(cursor.getString(0));
				orderTransaction.setDemo(cursor.getString(1));
				orderTransaction.setOrder(cursor.getString(2));
				orderTransaction.setFailed(cursor.getString(3));
				orderTransaction.setDistfailed(cursor.getString(4));
				orderTransaction.setCompt(cursor.getString(5));
				orderTransaction.setCollection(cursor.getString(6));
				orderTransaction.setDistCollection(cursor.getString(7));
				orderTransaction.setDiscussion(cursor.getString(8));

				orderTransactions.add(orderTransaction);
				System.out.println(cursor.getString(0));
				cursor.moveToNext();
			}
		}
		else
		{
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}

	public OrderTransaction getAllPendingDateForLock(String vDate)
	{

//		String query="select vdate,max(demodate) as demo,max(odate) as ordr,max(fdate) as fvisit,max(cdate) as comp,max(coll) as collection,max(distcoll) as distcollection, max(distdiscu) as discussion  from ( "
//				+" select visit_date as vdate,1 as demodate ,0 as odate     ,0 as fdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu from TransDemo Where isUpload='0' union all "
//				+" select visit_date as vdate,0 as demodate ,1 as odate     ,0 as fdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu from TransOrder Where isSubUpload='0' union all "
//				+" select visit_date as vdate,0 as demodate ,0 as odate,1 as fdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu from Trans_Failed_visit Where isUpload='0' union all "
//				+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,1 as cdate,0 as coll,0 as distcoll,0 as distdiscu from TransCompetitor Where isUpload='0' union all "
//				+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as cdate,1 as coll,0 as distcoll,0 as distdiscu from TransCollection Where isUpload='0' union all "
//				+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as cdate,0 as coll,1 as distcoll,0 as distdiscu from DistributerCollection Where isUpload='0' union all "
//				+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as cdate,0 as coll,0 as distcoll,1 as distdiscu  from TransVisitDist Where isUpload='0' union all "
//				+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu from Visitl1 Where isUpload='0' "
//				+" )  a "
//				+" group by vdate order by substr(vdate,7,4),substr(vdate,4,2),substr(vdate,1,2) desc";

		String query="select vdate,max(demodate) as demo,max(odate) as ordr,max(fdate) as fvisit,max(dfdate) as dfvisit,max(cdate) as comp,max(coll) as collection,max(distcoll) as distcollection, max(distdiscu) as discussion  from ( "
				+" select visit_date as vdate,1 as demodate ,0 as odate     ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu from TransDemo Where timestamp IN (-1,0) and visit_date='"+vDate+"' union all "
				+" select visit_date as vdate,0 as demodate ,1 as odate     ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu from TransOrder Where timestamp IN (-1,0) and visit_date='"+vDate+"' union all "
				+" select visit_date as vdate,0 as demodate ,0 as odate,1 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu from Trans_Failed_visit where DistId IS NULL and party_code IS NOT NULL and visit_date='"+vDate+"' and timestamp IN (-1,0) union all "
				+" select visit_date as vdate,0 as demodate ,0 as odate,0 as fdate,1 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu from Trans_Failed_visit where DistId IS NOT NULL and party_code IS NULL and visit_date='"+vDate+"'  and timestamp IN (-1,0) union all "
				+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,1 as cdate,0 as coll,0 as distcoll,0 as distdiscu from TransCompetitor Where timestamp IN (-1,0) and visit_date='"+vDate+"' union all "
				+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,1 as coll,0 as distcoll,0 as distdiscu from TransCollection Where timestamp IN (-1,0) and visit_date='"+vDate+"' union all "
				+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,1 as distcoll,0 as distdiscu from DistributerCollection Where timestamp IN (-1,0) and visit_date='"+vDate+"' union all "
				+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,1 as distdiscu  from TransVisitDist Where timestamp IN (-1,0) and visit_date='"+vDate+"' union all "
				+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distcoll,0 as distdiscu from Visitl1 Where timestamp IN (-1,0) and visit_date='"+vDate+"'"
				+" )  a "
				+" group by vdate order by substr(vdate,7,4),substr(vdate,4,2),substr(vdate,1,2) desc";


		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		OrderTransaction orderTransaction = new OrderTransaction();
//		ArrayList<OrderTransaction> orderTransactions = new ArrayList<OrderTransaction>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				orderTransaction.setDate(cursor.getString(0));
				orderTransaction.setDemo(cursor.getString(1));
				orderTransaction.setOrder(cursor.getString(2));
				orderTransaction.setFailed(cursor.getString(3));
				orderTransaction.setDistfailed(cursor.getString(4));
				orderTransaction.setCompt(cursor.getString(5));
				orderTransaction.setCollection(cursor.getString(6));
				orderTransaction.setDistCollection(cursor.getString(7));
				orderTransaction.setDiscussion(cursor.getString(8));
//				orderTransactions.add(orderTransaction);
				System.out.println(cursor.getString(0));
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransaction;
	}

	public OrderTransaction getTransDataOnVdate(String vDate, String partyId, String apartyId)
	{
		String query="";

		if(partyId!=null && apartyId!=null)
		{
			query="select vdate,max(demodate) as demo,max(odate) as ordr,max(fdate) as fvisit,max(cdate) as comp,max(coll) as collection from ( "
					+" select visit_date as vdate,1 as demodate ,0 as odate     ,0 as fdate,0 as cdate,0 as coll from TransDemo Where party_code='"+partyId+"' and visit_date='"+vDate+"' union all "
					+" select visit_date as vdate,0 as demodate ,1 as odate     ,0 as fdate,0 as cdate,0 as coll from TransOrder Where party_code='"+partyId+"' and visit_date='"+vDate+"' union all "
					+" select visit_date as vdate,0 as demodate ,0 as odate,1 as fdate,0 as cdate,0 as coll from Trans_Failed_visit where DistId IS NULL and party_code IS NOT NULL and visit_date='"+vDate+"' and party_code='"+partyId+"' union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,1 as cdate,0 as coll from TransCompetitor Where party_code='"+partyId+"' and visit_date='"+vDate+"' union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as cdate,1 as coll from TransCollection Where party_code='"+partyId+"' and visit_date='"+vDate+"' union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as cdate,0 as coll from Visitl1 Where visit_date='"+vDate+"'"
					+" )  a "
					+" group by vdate";
		}
		else if(partyId!=null && apartyId==null)
		{
			query="select vdate,max(demodate) as demo,max(odate) as ordr,max(fdate) as fvisit,max(cdate) as comp,max(coll) as collection from ( "
					+" select visit_date as vdate,1 as demodate ,0 as odate     ,0 as fdate,0 as cdate,0 as coll from TransDemo Where party_code='"+partyId+"' and visit_date='"+vDate+"' union all "
					+" select visit_date as vdate,0 as demodate ,1 as odate     ,0 as fdate,0 as cdate,0 as coll from TransOrder Where party_code='"+partyId+"' and visit_date='"+vDate+"' union all "
					+" select visit_date as vdate,0 as demodate ,0 as odate,1 as fdate,0 as cdate,0 as coll from Trans_Failed_visit where DistId IS NULL and party_code IS NOT NULL and visit_date='"+vDate+"' and party_code='"+partyId+"' union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,1 as cdate,0 as coll from TransCompetitor Where party_code='"+partyId+"' and visit_date='"+vDate+"' union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as cdate,1 as coll from TransCollection Where party_code='"+partyId+"' and visit_date='"+vDate+"' union all "
					+" select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as cdate,0 as coll from Visitl1 Where visit_date='"+vDate+"'"
					+" )  a "
					+" group by vdate";

		}
		else if(partyId==null && apartyId!=null) {

			query = "select vdate,max(demodate) as demo,max(odate) as ordr,max(fdate) as fvisit,max(cdate) as comp,max(coll) as collection from ( "
					+ " select visit_date as vdate,1 as demodate ,0 as odate     ,0 as fdate,0 as cdate,0 as coll from TransDemo Where and_party_code='" + apartyId + "' and visit_date='" + vDate + "' union all "
					+ " select visit_date as vdate,0 as demodate ,1 as odate     ,0 as fdate,0 as cdate,0 as coll from TransOrder Where and_party_code='" + apartyId + "' and visit_date='" + vDate + "' union all "
					+ " select visit_date as vdate,0 as demodate ,0 as odate,1 as fdate,0 as cdate,0 as coll from Trans_Failed_visit where DistId IS NULL and and_party_code IS NOT NULL and visit_date='" + vDate + "' and and_party_code='" + apartyId + "' union all "
					+ " select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,1 as cdate,0 as coll from TransCompetitor Where and_party_code='" + apartyId + "' and visit_date='" + vDate + "' union all "
					+ " select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as cdate,1 as coll from TransCollection Where and_party_code='" + apartyId + "' and visit_date='" + vDate + "' union all "
					+ " select visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as cdate,0 as coll from Visitl1 Where visit_date='" + vDate + "'"
					+ " )  a "
					+ " group by vdate";
		}
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		OrderTransaction orderTransaction = new OrderTransaction();
//		ArrayList<OrderTransaction> orderTransactions = new ArrayList<OrderTransaction>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{

				orderTransaction.setDate(cursor.getString(0));
				orderTransaction.setDemo(cursor.getString(1));
				orderTransaction.setOrder(cursor.getString(2));
				orderTransaction.setFailed(cursor.getString(3));
				orderTransaction.setCompt(cursor.getString(4));
				orderTransaction.setCollection(cursor.getString(5));
//				orderTransactions.add(orderTransaction);
				System.out.println(cursor.getString(0));
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransaction;
	}



	public ArrayList<OrderTransaction> getFromAndToPendingDate(int arg, String fromDate, String toDate)
	{
		String query="select CreatedDate,vdate,max(demodate) as demo,max(odate) as ordr,max(fdate) as fvisit,max(dfdate) as dfvisit,max(cdate) as comp,max(coll) as collection,max(distdiscu) as discussion,max(distcoll) as distcollection  from ( "
				+" select CreatedDate,visit_date as vdate,1 as demodate ,0 as odate     ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distdiscu,0 as distcoll from TransDemo where CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"'  union all "
				+" select CreatedDate,visit_date as vdate,0 as demodate ,1 as odate     ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distdiscu,0 as distcoll from TransOrder where CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"'  union all "
				+" select CreatedDate,visit_date as vdate,0 as demodate ,0 as odate,1 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distdiscu,0 as distcoll from Trans_Failed_visit where ((DistId IS NULL and party_code IS NOT NULL) or (DistId IS NULL and and_party_code IS NOT NULL)) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"')  union all "
				+" select CreatedDate,visit_date as vdate,0 as demodate ,0 as odate,0 as fdate,1 as dfdate,0 as cdate,0 as coll,0 as distdiscu,0 as distcoll from Trans_Failed_visit where ((DistId IS NOT NULL and party_code IS NULL) or (DistId IS NOT NULL and and_party_code IS NULL)) and (CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"')  union all "
				+" select CreatedDate,visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,1 as cdate,0 as coll,0 as distdiscu,0 as distcoll from TransCompetitor where  CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"'  union all "
				+" select CreatedDate,visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,1 as coll,0 as distdiscu,0 as distcoll from TransCollection where CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"'  union all "
				+" select CreatedDate,visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distdiscu,1 as distcoll from DistributerCollection where CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"'  union all "
				+" select CreatedDate,visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,1 as distdiscu,0 as distcoll  from TransVisitDist where CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"'  union all "
				+" select CreatedDate,visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distdiscu,0 as distcoll from Visitl1 where  CreatedDate between '"+ DateFunction.ConvertDateToTimestamp(fromDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"' and '"+ DateFunction.ConvertDateToTimestamp(toDate+" 00:00:00", "dd/MMM/yyyy 00:00:00")+"'  "
				+" )  a "
				+" group by vdate order by CreatedDate desc";

		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<OrderTransaction> orderTransactions = new ArrayList<OrderTransaction>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				OrderTransaction orderTransaction = new OrderTransaction();
				orderTransaction.setDate(cursor.getString(1));
				orderTransaction.setDemo(cursor.getString(2));
				orderTransaction.setOrder(cursor.getString(3));
				orderTransaction.setFailed(cursor.getString(4));
				orderTransaction.setDistfailed(cursor.getString(5));
				orderTransaction.setCompt(cursor.getString(6));
				orderTransaction.setCollection(cursor.getString(7));
				orderTransaction.setDiscussion(cursor.getString(8));
				orderTransaction.setDistCollection(cursor.getString(9));
				orderTransactions.add(orderTransaction);
				System.out.println(cursor.getString(1));
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}

	public ArrayList<OrderTransaction> getAllPendingDate(int arg)
	{
		String query="select CreatedDate,vdate,max(demodate) as demo,max(odate) as ordr,max(fdate) as fvisit,max(dfdate) as dfvisit,max(cdate) as comp,max(coll) as collection,max(distdiscu) as discussion,max(distcoll) as distcollection  from ( "
				+" select CreatedDate,visit_date as vdate,1 as demodate ,0 as odate     ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distdiscu,0 as distcoll from TransDemo  union all "
				+" select CreatedDate,visit_date as vdate,0 as demodate ,1 as odate     ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distdiscu,0 as distcoll from TransOrder  union all "
				+" select CreatedDate,visit_date as vdate,0 as demodate ,0 as odate,1 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distdiscu,0 as distcoll from Trans_Failed_visit where (DistId IS NULL and party_code IS NOT NULL) or (DistId IS NULL and and_party_code IS NOT NULL) union all "
				+" select CreatedDate,visit_date as vdate,0 as demodate ,0 as odate,0 as fdate,1 as dfdate,0 as cdate,0 as coll,0 as distdiscu,0 as distcoll from Trans_Failed_visit where (DistId IS NOT NULL and party_code IS NULL) or (DistId IS NOT NULL and and_party_code IS NULL) union all "
				+" select CreatedDate,visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,1 as cdate,0 as coll,0 as distdiscu,0 as distcoll from TransCompetitor  union all "
				+" select CreatedDate,visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,1 as coll,0 as distdiscu,0 as distcoll from TransCollection  union all "
				+" select CreatedDate,visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distdiscu,1 as distcoll from DistributerCollection  union all "
				+" select CreatedDate,visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,1 as distdiscu,0 as distcoll  from TransVisitDist union all "
				+" select CreatedDate,visit_date as vdate,0 demodate     ,0 as odate      ,0 as fdate,0 as dfdate,0 as cdate,0 as coll,0 as distdiscu,0 as distcoll from Visitl1  "
				+" )  a "
				+" group by vdate order by CreatedDate desc";




		
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<OrderTransaction> orderTransactions = new ArrayList<OrderTransaction>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{				
				OrderTransaction orderTransaction = new OrderTransaction();
				orderTransaction.setDate(cursor.getString(1));
				orderTransaction.setDemo(cursor.getString(2));
				orderTransaction.setOrder(cursor.getString(3));
				orderTransaction.setFailed(cursor.getString(4));
				orderTransaction.setDistfailed(cursor.getString(5));
				orderTransaction.setCompt(cursor.getString(6));
				orderTransaction.setCollection(cursor.getString(7));
				orderTransaction.setDiscussion(cursor.getString(8));
				orderTransaction.setDistCollection(cursor.getString(9));
				orderTransactions.add(orderTransaction);
				System.out.println(cursor.getString(1));
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return orderTransactions;
	}

	public void deleteOrderOrderMgmt(String doc_id,String date,String party_id)
	{	long id=0;
		id=database.delete(DatabaseConnection.TABLE_ORDER1," Ord1Android_id='"+doc_id+"' and visit_date='"+date+"'",null );
		System.out.println("Order do doc_id="+doc_id+" is deleted");
		if(id>0){
			String query="update TransOrder set amount=(select sum(amount) from TransOrder1 where visit_date='"+date+"' AND party_code="+party_id+") where visit_date='"+date+"' AND party_code="+party_id+"";
			database.execSQL(query);
		}

	}

	public void deleteOrderPartyInOrderMgmt(String doc_id,String date,String partyCode)
	{
		database.delete(DatabaseConnection.TABLE_ORDER,"Android_id='"+doc_id+"' and visit_date='"+date+"'",null );
		System.out.println("Order do doc_id="+doc_id+" is deleted");
	}
	public void deleteDemoPartyInOrderMgmt(String doc_id,String date,String partyCode)
	{
		database.delete(DatabaseConnection.TABLE_TRANSDEMO," Android_id='"+doc_id+"' and visit_date='"+date+"'",null );
		System.out.println("Order do doc_id="+doc_id+" is deleted");
	}
	public void deleteFailedVisitPartyInOrderMgmt(String doc_id,String date,String partyCode)
	{
		database.delete(DatabaseConnection.TABLE_TRANSFAILED_VISIT,"Android_id='"+doc_id+"' and visit_date='"+date+"'",null );
		System.out.println("Order do doc_id="+doc_id+" is deleted");
	}
	public void deleteCompetitotPartyInOrderMgmt(String doc_id,String date,String partyCode)
	{
		database.delete(DatabaseConnection.TABLE_COMPETITOR,"Android_id='"+doc_id+"' and visit_date='"+date+"'",null );
		System.out.println("Order do doc_id="+doc_id+" is deleted");
	}
	public void deleteCollectionPartyInOrderMgmt(String doc_id,String date,String partyCode)
	{
		database.delete(DatabaseConnection.TABLE_TRANSCOLLECTION,"Android_id='"+doc_id+"' and visit_date='"+date+"'",null );
		System.out.println("Order do doc_id="+doc_id+" is deleted");
	}
	public void deleteCollectionDistInOrderMgmt(String doc_id,String date,String partyCode)
	{
		database.delete(DatabaseConnection.TABLE_DISTRIBUTERCOLLECTION,"Android_id='"+doc_id+"' and visit_date='"+date+"'",null );
		System.out.println("Order do doc_id="+doc_id+" is deleted");
	}
	public void deleteDistributerDiscussionInOrderMgmt(String doc_id,String date,String partyCode)
	{
		database.delete(DatabaseConnection.TABLE_VISITDIST,"Android_id='"+doc_id+"' and visit_date='"+date+"'",null );
		System.out.println("Order do doc_id="+doc_id+" is deleted");
	}
	public void deleteDistributorStock(String doc_id,String date)
	{
		database.delete(DatabaseConnection.TABLE_TRANS_DIST_STOCK," Android_id='"+doc_id+"' and visit_date='"+date+"'",null );
		System.out.println("Order do doc_id="+doc_id+" is deleted");
	}
	
	public int deleteTransactionRecord(String androidId,String table)
	{
			int i = database.delete(table, "Android_id='" + androidId + "'", null);
			System.out.println(" deleted for " + " result=" + i);
			return i;	
	}

	public ArrayList<Distributor> getAllDistStock(String date, String smid, String distributorID){
		String query="SELECT md.name as distributorname,mp.name as productname,td. _case, td.unit,td.Android_id from TransDistStock td left join mastDristributor md on td.distid=md.webcode left join mastproduct mp on td.product_code=mp.webcode   WHERE  SREP_CODE="+smid+" AND visit_date='"+date+"' AND distid='"+distributorID+"'";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Distributor> distributorsStock = new ArrayList<Distributor>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				Distributor distributor = new Distributor();
				distributor.setDistributor_name(cursor.getString(0));
				distributor.setProductname(cursor.getString(1));
				distributor.setCases(cursor.getString(2));
				distributor.setUnit(cursor.getString(3));
				distributor.setAndroidID(cursor.getString(4));
				distributorsStock.add(distributor);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return distributorsStock;
	}
	public ArrayList<Distributor> getDistStockList(String date, String smid){
		String query="select distinct md.name as distName,ma.name as distArea,ifnull(sum(tds.Unit),0) as totalUnit,ifnull(sum(tds._case),0) as totalCase,tds.distid  from TransDistStock tds	left join MastArea ma on ma.webcode=tds.area_code		left join mastDristributor md on (tds.DistId=md.webcode or tds.area_code=md.area_code) where tds.visit_date='"+date+"'  AND tds.srep_code="+smid+" group  by distName,distArea ";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		ArrayList<Distributor> distributorsStock = new ArrayList<Distributor>();
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				Distributor distributor = new Distributor();
				distributor.setDistributor_name(cursor.getString(0));
				distributor.setArea(cursor.getString(1));
				distributor.setUnit(cursor.getString(2));
				distributor.setCases(cursor.getString(3));
				distributor.setDistributor_id(cursor.getString(4));
				distributorsStock.add(distributor);
				cursor.moveToNext();
			}
		}else{
			System.out.println("No records found");
		}
		cursor.close();
		return distributorsStock;
	}
}
