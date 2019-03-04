package com.dm.library;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.dm.database.DatabaseConnection;
import com.dm.model.Dropdown;
import com.dm.model.Party;
import com.dm.model.WriteLogData;

import java.util.ArrayList;

/**
 * Created by Dataman on 3/24/2017.
 */
public class DbCon {

    private DatabaseConnection dbHelper;
    private SQLiteDatabase database;
    Context context;
    public DbCon(Context context) {
        dbHelper = new DatabaseConnection(context);
        this.context=context;

    }

//    public void open() {
//        try{
//            database = dbHelper.getWritableDatabase();
//        }catch(SQLException e){
//            System.out.println("-----------------"+e.getMessage());
//        }
//
//
//    }



    public SQLiteDatabase open() {
        try{
            database = dbHelper.getWritableDatabase();
        }catch(SQLException e){
            System.out.println("-----------------"+e.getMessage());
        }
        return database;
    }

    public void close() {
        if(database != null && database.isOpen())
        {
            database.close();
//            dbHelper.close();
        }

    }

    public void insert(String tableName,ContentValues values){

        try
        {
          //  database.delete(tableName,null,null);
            long id = database.insert(tableName, null, values);
            System.out.println(id);
        }
        catch(RuntimeException e){
            System.out.println("+++++++++++++++++++"+e.toString());
        }
    }


    public void update(String tableName,ContentValues values,String whereClause){

        try {
            long id = database.update(tableName,
                    values, whereClause, null);
            System.out.println("row=" + id);
        } catch (RuntimeException e) {
            System.out.println("+++++++++++++++++++" + e.toString());
        }
    }

    public void delete(String tableName,String where)
    {
        try{
            database.delete(tableName,where,null );

        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

    public long deleteLogTable(String tableName,String where)
    {
        long rc=0;
        try{
           rc = database.delete(tableName,"EntityId=?",new String[]{where} );

        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return rc;
    }

    public String selectDataFromDb(String qry)
    {
       String data="";
        Cursor cursor = database.rawQuery(qry, null);
        if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast()))
            {
                data=cursor.getString(0);
                cursor.moveToNext();
            }
        }else{
            System.out.println("No records found");
        }
        cursor.close();
        return data;
    }

    public ArrayList<Dropdown> getDropdownList(String qry)
    {
        ArrayList<Dropdown> dropdownList=new ArrayList<Dropdown>();
        Dropdown dropdown1 =new Dropdown();
        dropdown1.setId("0");
        dropdown1.setName("--Select--");
        dropdownList.add(dropdown1);
        Cursor cursor = database.rawQuery(qry, null);
        if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast()))
            {
                Dropdown dropdown =new Dropdown();
                dropdown.setId(cursor.getString(0));
                dropdown.setName(cursor.getString(1));
                dropdownList.add(dropdown);
                cursor.moveToNext();
            }
        }else{
            System.out.println("No records found");
        }
        cursor.close();
        return dropdownList;
    }

    public Dropdown getDropdownObject(String qry)
    {

        Dropdown dropdown =new Dropdown();
       Cursor cursor = database.rawQuery(qry, null);
        if(cursor.moveToFirst()){
            while(!(cursor.isAfterLast()))
            {

                dropdown.setId(cursor.getString(0));
                dropdown.setName(cursor.getString(1));
                cursor.moveToNext();
            }
        }else{
            System.out.println("No records found");
        }
        cursor.close();
        return dropdown;
    }

    public int validateMobileNo(String query) {
        int flag = 0;
        System.out.println(query);
        Cursor cursor = database.rawQuery(query, null);
        /*if (cursor.moveToFirst()) {
            while (!(cursor.isAfterLast())) {
                 flag = cursor.getInt(0);
                cursor.moveToNext();

            }

        } else {
            flag = 0;
            System.out.println("No records found");
        }*/
        flag = cursor.getCount();
        cursor.close();
        return flag;

    }

    public Party getPartyList(String webcode, String androidId) {
        String query = "";


        Party party = new Party();
        if (webcode != null && androidId != null)

            query="select mp.webcode,  mp.Android_id,  mp.name as partyname , mp.address1 , mp.address2 , " +
                    "mc.name ,mp.pin , mp.contact_person , mp.mobile , mp.phone , mp.email , mp.blocked_reason  , " +
                    " ma.name as areaname , mb.name as beatname , im.name as industryname , mp.potential , ptm.name as ptname , " +
                    "mp.cst_no , mp.vattin_no , mp.Servicetaxreg_No , mp.PANNo , mp.remark , mp.DistId , mp.sync_id , " +
                    "mp.Active , mp.CreatedDate , mp.user_code , mp.dob , mp.doa from mastParty mp " +
                    "left join mastArea ma on ma.webcode=mp.area_code " +
                    "left join mastBeat mb on mb.webcode=mp.beat_code " +
                    "left join mastcity mc on mc.webcode=mp.city_code " +
                    "left join Industrymast im on im.webcode=mp.IndId " +
                    "left join  Partytypemast ptm on ptm.webcode=mp.party_type_code " +
                    "where mp.webcode='"+webcode+"' and mp.Android_id='" + androidId + "' "	;

        else if (webcode == null && androidId != null)
            query="select mp.webcode,  mp.Android_id,  mp.name as partyname , mp.address1 , mp.address2 , " +
                    "mc.name ,mp.pin , mp.contact_person , mp.mobile , mp.phone , mp.email , mp.blocked_reason  , " +
                    " ma.name as areaname , mb.name as beatname , im.name as industryname , mp.potential , ptm.name as ptname , " +
                    "mp.cst_no , mp.vattin_no , mp.Servicetaxreg_No , mp.PANNo , mp.remark ,mp.DistId , mp.sync_id , " +
                    "mp.Active , mp.CreatedDate , mp.user_code , mp.dob , mp.doa from mastParty mp " +
                    "left join mastArea ma on ma.webcode=mp.area_code " +
                    "left join mastBeat mb on mb.webcode=mp.beat_code " +
                    "left join mastcity mc on mc.webcode=mp.city_code " +
                    "left join Industrymast im on im.webcode=mp.IndId " +
                    "left join  Partytypemast ptm on ptm.webcode=mp.party_type_code " +
                    "where mp.Android_id='" + androidId + "' "	;
        else if (webcode != null && androidId == null)
            query="select mp.webcode,  mp.Android_id,  mp.name as partyname , mp.address1 , mp.address2 , " +
                    "mc.name ,mp.pin , mp.contact_person , mp.mobile , mp.phone , mp.email , mp.blocked_reason  , " +
                    " ma.name as areaname , mb.name as beatname , im.name as industryname , mp.potential , ptm.name as ptname , " +
                    "mp.cst_no , mp.vattin_no , mp.Servicetaxreg_No , mp.PANNo , mp.remark , mp.DistId , mp.sync_id , " +
                    "mp.Active , mp.CreatedDate , mp.user_code , mp.dob , mp.doa from mastParty mp " +
                    "left join mastArea ma on ma.webcode=mp.area_code " +
                    "left join mastBeat mb on mb.webcode=mp.beat_code " +
                    "left join mastcity mc on mc.webcode=mp.city_code " +
                    "left join Industrymast im on im.webcode=mp.IndId " +
                    "left join  Partytypemast ptm on ptm.webcode=mp.party_type_code " +
                    "where mp.webcode='"+webcode+"'"	;

        System.out.println(query);
        Cursor cursor = database.rawQuery(query, null);
        if (cursor.getCount() == 1) {
            cursor.moveToLast();

            party.setParty_id(cursor.getString(0));
            party.setAndroidId(cursor.getString(1));
            party.setParty_name(cursor.getString(2));
            party.setAddress1(cursor.getString(3));
            party.setAddress2(cursor.getString(4));
            party.setCity_id(cursor.getString(5));
            party.setPin(cursor.getString(6));
            party.setContact_person(cursor.getString(7));
            party.setMobile(cursor.getString(8));
            party.setPhone((cursor.getString(9)==null?"":cursor.getString(9)));
            party.setEmail((cursor.getString(10)==null?"":cursor.getString(10)));
            party.setBlocked_Reason(cursor.getString(11));
            party.setAreaId(cursor.getString(12));
            party.setBeatId(cursor.getString(13));
            party.setIndId(cursor.getString(14));
            party.setPotential(cursor.getString(15));
            party.setParty_type_code(cursor.getString(16));
            party.setCst_no((cursor.getString(17)==null?"":cursor.getString(17)));
            party.setVattin_no(cursor.getString(18));
            party.setServicetaxreg_No((cursor.getString(19)==null?"":cursor.getString(19)));
            party.setPANNo((cursor.getString(20)==null?"":cursor.getString(20)));
            party.setRemark(cursor.getString(21));
            party.setDistId(cursor.getString(22));
            party.setSync_id(cursor.getString(23));
            party.setActive(cursor.getString(24));
            party.setCreatedDate(cursor.getString(25));
            party.setUserId(cursor.getString(26));
            party.setDob((cursor.getString(27)==null?"":cursor.getString(27)));
            party.setDoa((cursor.getString(28)==null?"":cursor.getString(28)));
            cursor.moveToNext();
        } else {
            System.out.println("No records found");

        }
        cursor.close();
        return party;
    }
    public void truncate(String tableName)
    {
        try{
            database.execSQL("DELETE FROM "+tableName);

        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }

public boolean ButtonEnable(String MenuName,String formFilter,String Type)
{

    try {
        String qry = "select ifnull(add_p,'false'),ifnull(edit_p,'false'),ifnull(delete_p,'false') from dynamicmenu where page_name='" + MenuName + "' and form_filter='" + formFilter + "'";
        System.out.println(qry);
        Cursor cursor = database.rawQuery(qry, null);
        if (cursor.getCount() == 1) {
            cursor.moveToLast();
            if (Type.equals("Add")) {
                return Boolean.parseBoolean(cursor.getString(0));
            } else if (Type.equals("Edit")) {
                return Boolean.parseBoolean(cursor.getString(1));
            } else if (Type.equals("Delete")) {
                return Boolean.parseBoolean(cursor.getString(2));
            }
            cursor.moveToNext();
        }
        cursor.close();
    }
    catch (Exception e)
    {
        e.printStackTrace();
    }
    return false;
}


    public boolean isOpen()
    {
        if(database != null && database.isOpen())
        {
            return true;
        }
        else
        {

            return false;
        }
    }
public void updateImagePath(String query) {

    //	String query="update TransOrder set amount='"+order.getOrderAmount()+"' , remark='"+order.getRemarks()+"' where Android_id='"+order.getAndroid_id()+"'";
//		System.out.println(query);
	try
    {
		database.execSQL(query);
		System.out.println("updated");
	}
    catch (RuntimeException e) {
		System.out.println("+++++++++++++++++++" + e.toString());
	}



}





    public void insertExportImportLogData(String smId,String params,String method,String response)
    {
       /* open();
        ContentValues cv = new ContentValues();
        cv.put("smid",smId);
        cv.put("params",params);
        cv.put("method",method);
        cv.put("response",response);
        cv.put("timeStamp",System.currentTimeMillis());
        insert("TableWriteLog",cv);
        close();*/
    }


    public ArrayList<WriteLogData> getExportLogData()
    {
        ArrayList<WriteLogData> list = new ArrayList<>();
        Cursor cursor = database.query(DatabaseConnection.TABLE_WRITE_LOG,null,null,null,null,null,null);
        while (cursor.moveToNext())
        {
            WriteLogData data = new WriteLogData();
            data.setMethod(cursor.getString(2));
            data.setCreatedDate(cursor.getString(4));
            data.setSmId(cursor.getString(0));
            data.setParams(cursor.getString(1));
            data.setResponse(cursor.getString(3));
            list.add(data);
        }

        return list;
    }

}
