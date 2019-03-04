package com.dm.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.City;
import com.dm.model.CompanyDetails;
import com.dm.model.Country;
import com.dm.model.State;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CityController{
	private DatabaseConnection dbHelper;
	private SQLiteDatabase database;
	private String[] allColumns = {	
			DatabaseConnection.COLUMN_WEB_CODE,
			DatabaseConnection.COLUMN_NAME,
			DatabaseConnection.COLUMN_DISTRICT_CODE,
			DatabaseConnection.COLUMN_SYNC_ID,
			DatabaseConnection.COLUMN_ACTIVE,
			DatabaseConnection.COLUMN_CREATED_DATE
	};
	
	public CityController(Context context) {
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
	
	public void insertCity(City city){

		int c=0;
		String qry="select count(*) from MastCity where webcode="+city.getCity_id();
		System.out.println(qry);
		Cursor cursor = database.rawQuery(qry, null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				c=cursor.getInt(0);
				cursor.moveToNext();
			}
		}else{
			c=0;
		}
		cursor.close();
		ContentValues values = new ContentValues();
		if(c>0)
		{

		}
		else{
			values.put(DatabaseConnection.COLUMN_WEB_CODE, city.getCity_id());
		}



		values.put(DatabaseConnection.COLUMN_NAME, city.getCity_name());
		values.put(DatabaseConnection.COLUMN_DISTRICT_CODE, city.getDistrict_id());
		values.put(DatabaseConnection.COLUMN_STATE_CODE, city.getSync_id());
		values.put(DatabaseConnection.COLUMN_ACTIVE, city.getActive());
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, city.getCreatedDate());
		long id=0;
		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_CITYMAST,
							values, "webcode='" + city.getCity_id() + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_CITYMAST, null, values);
					System.out.println("row=" + id);
				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
				}
			}


		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

	public void insertCity(String Cid,String NM,String Did, String MS,String SId,String active)

	{

		int c=0;
		String qry="select count(*) from MastCity where webcode="+Cid;
		System.out.println(qry);
		Cursor cursor = database.rawQuery(qry, null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{
				c=cursor.getInt(0);
				cursor.moveToNext();
			}
		}else{
			c=0;
		}
		cursor.close();
		ContentValues values = new ContentValues();
		if(c>0)
		{

		}
		else{
			values.put(DatabaseConnection.COLUMN_WEB_CODE, Cid);
		}
		values.put(DatabaseConnection.COLUMN_NAME, NM);
		values.put(DatabaseConnection.COLUMN_DISTRICT_CODE, Did);
		values.put(DatabaseConnection.COLUMN_STATE_CODE, SId);
		values.put(DatabaseConnection.COLUMN_TIMESTAMP, MS);
		values.put(DatabaseConnection.COLUMN_ACTIVE, active);


		long id=0;
		try{
			if(c>0)
			{
				try {
					id = database.update(DatabaseConnection.TABLE_CITYMAST,
							values, "webcode='" + Cid + "'", null);
					System.out.println("row=" + id);
				} catch (RuntimeException e) {
					System.out.println("+++++++++++++++++++" + e.toString());
				}


			}
			else{
				try {
					id = database.insert(DatabaseConnection.TABLE_CITYMAST, null, values);
					System.out.println("row=" + id);
				}
				catch(RuntimeException e){
					System.out.println("+++++++++++++++++++"+e.toString());
				}
			}


		} catch(RuntimeException e){
			System.out.println("+++++++++++++++++++"+e.toString());
		}
	}

//	public String getCityCode(String areaCode){
//		String cityName ="";
//		String query = "select city_code from Areamast where webcode='"+areaCode+"'";
//		Cursor cursor = database.rawQuery(query,null);
//		
//		 if (cursor.getCount()==1)
//		    {
//		            cursor.moveToLast();   
//		            cityName=cursor.getString(0);
//		            System.out.println("areaCode  "+cityName);
//		         
//		    }
//		 else{
//			System.out.println("no areaCode found");
//		}
//		 System.out.println("no areaCode found"+cityName);
//		return cityName;
//	}
	
	public ArrayList<City> getCityList()
	{
		ArrayList<City> citynameList=new ArrayList<City>();
//		citynameList.add("Select city");
		String query="select name,webcode from MastCity order by name";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	City city=new City();
			    city.setCity_name(cursor.getString(0));
				city.setCity_id(cursor.getString(1));
				citynameList.add(city);
				cursor.moveToNext();
			}
		}
		else{
			
			System.out.println("No records found");
		}
		cursor.close();
		return citynameList;
	}
	public ArrayList<City> getCityListStateWise(String stId)
	{
		ArrayList<City> citynameList=new ArrayList<City>();
		City city1=new City();
		city1.setCity_name("--Select--");
		city1.setCity_id("0");
		citynameList.add(city1);
//		citynameList.add("Select city");
		String query="select name,webcode from MastCity where state_code='"+stId+"' order by name";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	City city=new City();
				city.setCity_name(cursor.getString(0));
				city.setCity_id(cursor.getString(1));
				citynameList.add(city);
				cursor.moveToNext();
			}
		}
		else{

			System.out.println("No records found");
		}
		cursor.close();
		return citynameList;
	}
	public String getCityObjectStateWise(String stId)
	{
		 JSONArray result = new JSONArray();
		try {
			JSONObject userResults = new JSONObject();
			userResults.put("id","0");
			userResults.put("nm", "--Select--");
			result.put(userResults);
		} catch (Exception e) {
			e.printStackTrace();
		}
		String query="select name,webcode from MastCity where state_code='"+stId+"' order by name";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{try {
				JSONObject userResults = new JSONObject();
				userResults.put("id",cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_WEB_CODE)));
				userResults.put("nm", cursor.getString(cursor.getColumnIndex(DatabaseConnection.COLUMN_NAME)));
				result.put(userResults);
			} catch (Exception e) {
				e.printStackTrace();
			}
				cursor.moveToNext();
			}
		}
		else{

			System.out.println("No records found");
		}
		cursor.close();
		return result.toString();
	}
	public ArrayList<City> getCityList(int arg)
	{
		ArrayList<City> citynameList=new ArrayList<City>();
		City city1=new City();
		city1.setCity_name("Select all");
		city1.setCity_id("-2");
		citynameList.add(city1);
//		citynameList.add("Select city");
		String query="select name,webcode from MastCity order by name";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	City city=new City();
				city.setCity_name(cursor.getString(0));
				city.setCity_id(cursor.getString(1));
				citynameList.add(city);
				cursor.moveToNext();
			}
		}
		else{

			System.out.println("No records found");
		}
		cursor.close();
		return citynameList;
	}

	public ArrayList<City> getCityList(String search)
	{
		ArrayList<City> citynameList=new ArrayList<City>();
//		citynameList.add("Select city");
		String query="select name,webcode from MastCity where name like '%"
				+ search + "%' order by name";
		
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	City city=new City();
			    city.setCity_name(cursor.getString(0));
				city.setCity_id(cursor.getString(1));
				citynameList.add(city);
				cursor.moveToNext();
			}
		}
		else{
			
			System.out.println("No records found");
		}
		
		cursor.close();
//		String stringToTest =search.toUpperCase();
//
//		if(stringToTest.contains("O")||stringToTest.contains("T")||stringToTest.contains("E")||stringToTest.contains("R")||stringToTest.contains("C")||stringToTest.contains("I")||stringToTest.contains("Y")){
//			
//			City city=new City();
//		    city.setCity_name("OTHER CITY");
//			city.setCity_id("-1");
//			citynameList.add(city);
//		   // return true;
//		}
		
		return citynameList;
	}
	
	public ArrayList<City> getCityListFillDropdown(String cityIds)
	{
		ArrayList<City> citynameList=new ArrayList<City>();
//		citynameList.add("Select city");
		String query="select name,webcode from MastCity where webcode in("+cityIds+") order by name";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	City city=new City();
			    city.setCity_name(cursor.getString(0));
				city.setCity_id(cursor.getString(1));
				citynameList.add(city);
				cursor.moveToNext();
			}
		}
		else{
			
			System.out.println("No records found");
		}
		cursor.close();
		return citynameList;
	}
	
	
	public ArrayList<Country> getCountryList(String cityid)
	{
		ArrayList<Country> countrynameList=new ArrayList<Country>();
//		citynameList.add("Select city");
		String query="Select a.CountryId,a.Countryname from ("+
						"select "+ 
						"mc.webcode as cityid,mc.name as cityname,"+
						"md.webcode as districtid,md.name as districtname,"+
						"ms.webcode as stateid,ms.name as statename,"+
						"mr.webcode as regionid,mr.name as statename,"+
						"mco.webcode as countryid,mco.name as countryname "+
						"from mastcity mc "+
						"left join mastdistrict md on md.webcode=mc.district_id "+
						"left join maststate ms on ms.webcode=md. state_code "+
						"left join mastregion mr on mr.webcode=ms. Region_id "+
						"left join mastcountry mco on mco.webcode=mr.country_code "+
						")a "+
						"where a.cityid='"+cityid+"'";
						
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	Country country=new Country();
			country.setId(cursor.getString(0));
			country.setDescription(cursor.getString(1));
			countrynameList.add(country);
				cursor.moveToNext();
			}
		}
		else{
			
			System.out.println("No records found");
		}
		cursor.close();
		return countrynameList;
	}
	
	public ArrayList<State> getStateList(String cityid)
	{
		ArrayList<State> statenameList=new ArrayList<State>();
//		citynameList.add("Select city");
		String query="Select a.stateid,a.statename from ("+
						"select "+ 
						"mc.webcode as cityid,mc.name as cityname,"+
						"md.webcode as districtid,md.name as districtname,"+
						"ms.webcode as stateid,ms.name as statename,"+
						"mr.webcode as regionid,mr.name as statename,"+
						"mco.webcode as countryid,mco.name as countryname "+
						"from mastcity mc "+
						"left join mastdistrict md on md.webcode=mc.district_id "+
						"left join maststate ms on ms.webcode=md. state_code "+
						"left join mastregion mr on mr.webcode=ms. Region_id "+
						"left join mastcountry mco on mco.webcode=mr.country_code "+
						")a "+
						"where a.cityid='"+cityid+"'";
						
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	State state=new State();
			state.setState_id(cursor.getString(0));
			state.setState_name(cursor.getString(1));
			statenameList.add(state);
				cursor.moveToNext();
			}
		}
		else{
			
			System.out.println("No records found");
		}
		cursor.close();
		return statenameList;
	}
	
	
	public String getCityName(String cityId)
	{
		String cityName="";
		String query="select name from mastcity where webcode='"+cityId+"'";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{

				cityName=cursor.getString(0);
				cursor.moveToNext();
			}
		}
		else{
			System.out.println("No records found");
		}
		cursor.close();
		return cityName;

	}

	public ArrayList<String> getMultipleCityName(String cityId)
	{ArrayList<String> dsrArray = new ArrayList<String>();
		String query="select name from mastcity where webcode in("+cityId+") order by name";
		System.out.println(query);
		Cursor cursor = database.rawQuery(query, null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{

				dsrArray.add(cursor.getString(0));
				cursor.moveToNext();
			}
		}
		else{
			System.out.println("No records found");
		}
		cursor.close();
		return dsrArray;

	}
	public ArrayList<CompanyDetails> getAllCityList()
	{
		ArrayList<CompanyDetails> citynameList=new ArrayList<CompanyDetails>();
		//ArrayList<String> citynameList=new ArrayList<String>();
//		citynameList.add("Select city");
		//String query="select name,webcode,Active from MastCity order by name";
		String query="select name,webcode,Active from MastCity  where Active='True' order by name";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.moveToFirst()){
			while(!(cursor.isAfterLast()))
			{	CompanyDetails companyDetails=new CompanyDetails();
				companyDetails.setCity(cursor.getString(0));
				companyDetails.setId(cursor.getString(1));
				citynameList.add(companyDetails);
				System.out.println("Active Value:"+cursor.getString(2)+":"+cursor.getString(0));
				//citynameList.add(cursor.getString(0));
				cursor.moveToNext();
			}
		}
		else{

			System.out.println("No records found");
		}
		cursor.close();
		return citynameList;
	}


	public String checkCity(String cityName)
	{
		String cityId="0";
		String query="select webcode from MastCity where name='"+cityName+"'"+" AND Active='True'";
		Cursor cursor = database.rawQuery(query,null);
		if(cursor.getCount()>0){
			if(cursor.moveToFirst()){
				while(!(cursor.isAfterLast()))
				{	cityId=(cursor.getString(0));
					cursor.moveToNext();
				}
			}
		}
		cursor.close();
		return cityId;
	}

}

