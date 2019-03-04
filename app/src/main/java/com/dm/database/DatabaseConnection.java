package com.dm.database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseConnection extends SQLiteOpenHelper  {
	public static final String DATABASE_NAME = "grahak.db";
	public static final int DATABASE_VERSION =6;
	private static String TRANS_DIST_STOCK_TABLE_CREATE ;
	private static String DIST_ITEM_TEMPLATE_TABLE_CREATE ;
	/******************************** Ashutosh  *******************************/
	private static String DYNAMIC_MENU_TABLE_CREATE ;
	/******************************** Chat Module Start ************************/
	private static String CHAT_USER_TABLE_CREATE ;
	private static String CHAT_TABLE_CREATE ;
	/******************************** Chat Module End **************************/
	/******************************** Ashutosh  *******************************/
	private static String TRANS_TOUR_PLAN_HEADER_TABLE_CREATE ;
	private static String TRANS_TOUR_PLAN_TABLE_CREATE ;
	private static String APPDATA_ENVIRO_TABLE_CREATE ;
	private static String APPDATA_TABLE_CREATE ;
	private static String VISITDIST_TABLE_CREATE ;
	private static String USERMAST_TABLE_CREATE ;
	private static String COMPETITORMAST_TABLE_CREATE ;
	private static String SESSION_DATA_TABLE_CREATE ;
	private static String TRANSCOLLECTION_TABLE_CREATE ;
	private static String DISTRIBUTERCOLLECTION_TABLE_CREATE;
	private static String FAILED_VISIT_MAST_DATA_TABLE_CREATE ;
	private static String PURCHASE_ORDER_TABLE_CREATE ;
	private static String SYN_TABLE_CREATE ;
	private static String COUNTRYMAST_TABLE_CREATE ;
	private static String STATEMAST_TABLE_CREATE ;
	private static String CITYMAST_TABLE_CREATE ;
	private static String AREATYPEMAST_TABLE_CREATE ;
	private static String AREAMAST_TABLE_CREATE ;
	private static String BEATMAST_TABLE_CREATE ;
	private static String USERAREAMAST_TABLE_CREATE ;
	private static String DISTMAST_TABLE_CREATE ;
	private static String DISTAREAMAST_TABLE_CREATE ;
	private static String INDUSTRYMAST_TABLE_CREATE ;
	private static String SUBINDUSTRYMAST_TABLE_CREATE ;
	private static String PRODUCTCLASSMAST_TABLE_CREATE ;
	private static String SEGMENTMAST_TABLE_CREATE;
	private static String PRODUCTGROUPMAST_TABLE_CREATE ;
	private static String PRODUCTMAST_TABLE_CREATE ;
	private static String PARTYTYPEMAST_TABLE_CREATE ;
	private static String PARTYMAST_TABLE_CREATE ;
	private static String PRICELIST_TABLE_CREATE ;
	private static String USERLOCATION_TABLE_CREATE ;

    private static String SALESRETURN_TABLE_CREATE ;
    private static String SALESRETURN1_TABLE_CREATE;

    private static String ORDER_TABLE_CREATE ;
	private static String ORDER1_TABLE_CREATE;
	private static String TRANSDEMO_TABLE_CREATE ;
	private static String TRANSDEMO1_TABLE_CREATE ;
	private static String TRANSFAILED_VISIT_TABLE_CREATE ;
	private static String PORDER_TABLE_CREATE ;
	private static String PORDER1_TABLE_CREATE ;
	private static String VISITL1_TABLE_CREATE ;
	private static String ENVIRO_TABLE_CREATE ;
	private static String SMAN_TABLE_CREATE ;
	private static String HISTORY_TABLE_CREATE ;
	private static String DEVICE_INFO_TABLE_CREATE ;
	private static String REGIONMAST_TABLE_CREATE ;
	private static String DISTRICTMAST_TABLE_CREATE ;
	private static String TRANSPORTER_TABLE_CREATE ;
	private static String PROJECT_TABLE_CREATE ;
	private static String SCHEME_TABLE_CREATE ;
	private static String TRANSLEAVE_TABLE_CREATE ;
	private static String TRANSBEATPLAN_TABLE_CREATE;
	private static String TRANSPORT_TABLE_CREATE;
	private static String VEHICLE_TABLE_CREATE;
	private static String VISITCODE_TABLE_CREATE;
	private static String DELETE_LOG_TABLE_CREATE;
	private static String WriteLogTableCreate;
	private static String TableSyncCreate;
	private static String TableRecentSearch;
	/**
	 * Database Tables_Name
	 */
	/******************************** Ashutosh  *******************************/
	public static final String TABLE_DYNAMIC_MENU = "DynamicMenu";
	/******************************** Chat Module Start ************************/
	public static final String TABLE_CHAT_USER = "ChatUser";
	public static final String TABLE_CHAT = "Chat";
	/******************************** Chat Module End **************************/

	/******************************** Ashutosh  *******************************/
	public static final String TABLE_TRANS_TOUR_PLAN = "TransTourPlan";
	public static final String TABLE_TRANS_TOUR_PLAN_HEADER = "TransTourPlanHeader";
	public static final String TABLE_VISIT_CODE = "MastVisitCode";
	public static final String TABLE_APPDATA = "MastAppData";
	public static final String TABLE_VISITDIST = "TransVisitDist";
	public static final String TABLE_COMPETITOR = "TransCompetitor";
	public static final String TABLE_PROJECT = "mastProject";
	public static final String TABLE_SCHEME = "MastScheme";
	public static final String TABLE_TRANSPORTER = "mastTransport";
	public static final String TABLE_USERMAST = "mastUser";
	public static final String TABLE_SESSION_DATA = "sessiondata";
	public static final String TABLE_TRANSCOLLECTION = "TransCollection";
	public static final String TABLE_DISTRIBUTERCOLLECTION = "DistributerCollection";
	public static final String TABLE_FAILED_VISIT_MAST = "mastFailedVisit";
	public static final String TABLE_DEVICE_INFO = "deviceinfo";
	public static final String TABLE_PURCHASE_ORDER = "Purchaseorder";
	public static final String TABLE_SYN = "Syn";
	public static final String TABLE_COUNTRYMAST = "MastCountry";
	public static final String TABLE_STATEMAST = "MastState";
	public static final String TABLE_CITYMAST = "MastCity";
	public static final String TABLE_AREATYPEMAST = "Areatypemast";
	public static final String TABLE_AREAMAST = "MastArea";
	public static final String TABLE_BEATMAST = "MastBeat";
	public static final String TABLE_USERAREAMAST = "Userareamast";
	public static final String TABLE_DISTRIBUTERMAST = "mastDristributor";
	public static final String TABLE_DISTAREAMAST = "Distareamast";
	public static final String TABLE_INDUSTRYMAST = "Industrymast";
	public static final String TABLE_SUBINDUSTRYMAST = "SubIndustrymast";
	public static final String TABLE_PRODUCTCLASSMAST = "MastClass";
	public static final String TABLE_SEGMENTMAST = "MastSegment";
	public static final String TABLE_PRODUCTGROUPMAST = "MastProdGroup";
	public static final String TABLE_PRODUCTMAST = "MastProduct";
	public static final String TABLE_PARTYTYPEMAST = "Partytypemast";
	public static final String TABLE_PRICELIST = "MastPriceList";
	public static final String TABLE_USERLOCATION = "Userlocation";
	public static final String TABLE_ORDER = "TransOrder";
    public static final String TABLE_ORDER1 = "TransOrder1";
	public static final String Table_Recent_Search = "RecentSearch";

    public static final String TABLE_SALES_RETURN = "TransSalesReturn";
    public static final String TABLE_SALES_RETURN1 = "TransSalesReturn1";
	public  static String COLUMN_SALES_RETURN_ID = "SALES_RETURN_ID";
	public static String COLUMN_BATCH_No = "Batch_No";
	public static String COLUMN_MFD_DATE = "MFD_Date";

    public static final String TABLE_TRANSDEMO = "TransDemo";
	public static final String TABLE_TRANSDEMO1 = "TransDemo1";
	public static final String TABLE_TRANSFAILED_VISIT= "Trans_Failed_visit";
	public static final String TABLE_PORDER = "Porder";
	public static final String TABLE_PORDER1 = "Porder1";
	public static final String TABLE_VISITL1 = "Visitl1";
	public static final String TABLE_ENVIRO = "Enviro";
	public static final String TABLE_PARTYMAST = "mastParty";
	public static final String TABLE_PRODUCTS = "Products";
	public static final String TABLE_SALESMANMAST = "mastSalesPerson";
	public static final String TABLE_HISTORY = "History";
	public static final String TABLE_REGIONMAST = "MastRegion";
	public static final String TABLE_DISTRICTMAST = "MastDistrict";
	public static final String TABLE_TRANSLEAVE = "TransLeaveRequest";
	public static final String TABLE_TRANSBEATPLAN = "TransBeatPlan";
	public static final String TABLE_APPENVIRO = "AppEnviro";
	public static final String TABLE_TRANSPORT = "Transport";
	public static final String TABLE_VEHICLE = "Vehicle";
	public static final String NOTIFIVATION_TABLE = "Notification";
	public static final String NOTIFIVATION_Data_TABLE = "NotificationData";
	public static final String TABLE_TRANS_DIST_STOCK = "TransDistStock";
	public static final String TABLE_DIST_ITEM_TEMPLATE = "MastDistItemTemplate";
	public static final String TABLE_DELETE_LOG = "TableDeleteLog";
	public static final String TABLE_WRITE_LOG = "TableWriteLog";
	public static final String TABLE_SYNC_STATUS = "SyncStatusTable";
	/**
	 * Database Data types
	 */
	public static final String DATA_TYPE_NUMBER="number";
	public static final String DATA_TYPE_BINARY="binary";
	public static final String DATA_TYPE_NUMERIC="numeric(18, 2)";
	public static final String DATA_TYPE_DATE="varchar";
	public static final String DATA_TYPE_VARCHAR="varchar";
	public static final String DATA_TYPE_INTEGER="integer";
	public static final String DATA_TYPE_NUMBER_NOT_NULL="number not null";
	public static final String DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY="varchar not null primary key";
	public static final String DATA_TYPE_VARCHAR_NOT_NULL="varchar not null";
	public static final String DATA_TYPE_INTEGER_NOT_NULL="integer not null";
	public static final String DATA_TYPE_INTEGER_PRIMARY_KEY="integer primary key autoincrement";
	public static final String DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY="integer not null primary key autoincrement";
	public static final String DATA_TYPE_BLOB="BLOB";
	/**
	 * Database Columns Name
	 */
	/******************************** Ashutosh  *******************************/
	public static final String COLUMN_Form_Filter="Form_filter";
	public static final String COLUMN_PAGE_ID="Page_id";
	public static final String COLUMN_VIEW_P="View_p";
	public static final String COLUMN_ADD_P="Add_p";
	public static final String COLUMN_EDIT_P="Edit_p";
	public static final String COLUMN_DELETE_P="Delete_p";
	public static final String COLUMN_PAGE_NAME="Page_name";
	//public static final String COLUMN_DISPLAY_NAME="Servicetaxreg_No";
	public static final String COLUMN_PARENT_ID="Parent_id";
	public static final String COLUMN_LEVEL_IDX="Level_idx";
	public static final String COLUMN_IDX="idx";
	public static final String COLUMN_Icon="icon";

	public static final String COLUMN_SERVICETAXREG_NO="Servicetaxreg_No";
	public static final String COLUMN_ANDROID_DOCID="Android_id";
	public static final String COLUMN_PORDER1_ANDROID_DOCID="Pord1Android_id";
	public static final String COLUMN_ORDER1_ANDROID_DOCID="Ord1Android_id";
	public static final String COLUMN_ANDROID_SERIAL_NO="Android_sno";
	public static final String COLUMN_PAN_NO="PANNo";
	public static final String COLUMN_CREDIT_LIMIT="CreditLimit";
	public static final String COLUMN_OUTSTANDING="OutStanding";
	public static final String COLUMN_PENDING_ORDER="PendingOrder";
	public static final String COLUMN_OPEN_ORDER="OpenOrder";
	public static final String COLUMN_CREDIT_DAYS="CreditDays";
	public static final String COLUMN_VATTIN_NO="vattin_no";
	public static final String COLUMN_DSRLOCK="dsr_lock";
	public static final String COLUMN_CST_NO="cst_no";
	public static final String COLUMN_USR_RECTSTATUS="usr_recstatus";
	public static final String COLUMN_PROJECT_TYPE="ProjectType";
	public static final String COLUMN_LST_NO="lst_no";
	public static final String COLUMN_REGISTERED="registered";
	public static final String COLUMN_RATING="rating";
	public static final String COLUMN_POTENTIAL="potential";
	public static final String COLUMN_DISCUSSION_NO="DiscussionNo";
	public static final String COLUMN_DIST_COLLEC_NO="DistColNo";
	public static final String COLUMN_LEAVE_NO="LeaveNo";
	public static final String COLUMN_BEATPLAN_NO="BeatPlanNo";
	public static final String COLUMN_PARTY_TYPE_CODE="party_type_code";
	public static final String COLUMN_ID="_id";
	public static final String COLUMN_CHQ_DATE="chq_date";
	public static final String COLUMN_MODE="mode";
	public static final String COLUMN_BRANCH="branch";
	public static final String COLUMN_USR_ID="usr_id";
	public static final String COLUMN_WITH_USR_ID="Withusr_id";
	public static final String COLUMN_NEXTWITH_USR_ID="nextWithUserId";
	public static final String COLUMN_BANK="bank";
	public static final String COLUMN_WEB_CODE="webcode";
	public static final String COLUMN_USER_TYPE="user_type";
	public static final String COLUMN_USER_NAME="user_name";
	public static final String COLUMN_PASSWORD="password";
	public static final String COLUMN_RANK="rank";
	public static final String COLUMN_ROLEID="RoleId";
	public static final String COLUMN_DEPTID="DeptId";
	public static final String COLUMN_DESIGID="DesigId";
	public static final String COLUMN_PDA_ID="pda_id";
	public static final String COLUMN_DISPLAY_NAME="display_Name";
	public static final String COLUMN_ITEM_TYPE="itemtype";
	public static final String COLUMN_ACTIVE_YN="active_yn";
	public static final String COLUMN_DSR_ALLOW_DAYS="dsr_allow_days";
	public static final String COLUMN_CODE="code";
	public static final String COLUMN_PID="PK_id";
	public static final String COLUMN_PAYMENT_DATE="payment_date";
	public static final String COLUMN_COUNTRY_CODE="country_code";
	public static final String COLUMN_STATE_CODE="state_code";
	public static final String COLUMN_AREATYPE_CODE="areatype_code";
	public static final String COLUMN_CITY_CODE="city_code";
	public static final String COLUMN_NCITY_CODE="ncity_code";
	public static final String COLUMN_CITY_IDS="cityIds";
	public static final String COLUMN_CITY_NAMES="cityNames";
	public static final String COLUMN_DIST_CODE="dist_code";
	public static final String COLUMN_AREA_CODE="area_code";
	public static final String COLUMN_BEAT_CODE="beat_code";
	public static final String COLUMN_USER_CODE="user_code";
	public static final String COLUMN_DOB="dob";
	public static final String COLUMN_DOA="doa";
	public static final String COLUMN_PRICE_LIST="price_list";
	public static final String COLUMN_RP="Rp";
	public static final String COLUMN_MRP="MRP";
	public static final String COLUMN_DP="DP";
	public static final String COLUMN_UNIT="Unit";
	public static final String COLUMN_INDUSTRY_ID="IndId";
	public static final String COLUMN_PRODUCT_GROUP_CODE="product_group_code";
	public static final String COLUMN_RESPONSIBILITY_CENTRE="responsibility_centre";
	public static final String COLUMN_IS_BLOCKED="isBlocked";
	public static final String COLUMN_BLOCKED_BY="blocked_By";
	public static final String COLUMN_BLOCK_DATE="block_date";
	public static final String COLUMN_BLOCKED_REASON="blocked_reason";
	public static final String COLUMN_EMAIL="email";
	public static final String COLUMN_CHEQUE_DDNO="cheque_ddno";
	public static final String COLUMN_PHONE="phone";
	public static final String COLUMN_NAME="name";
	public static final String COLUMN_INDUSTRY_NAME="indname";
	public static final String COLUMN_ADDRESS2="address2";
	public static final String COLUMN_ADDRESS1="address1";
	public static final String COLUMN_PIN="pin";
	public static final String COLUMN_CONTACT_PERSON="contact_person";
	public static final String COLUMN_MOBILE="mobile";
	public static final String COLUMN_BLOCK="block";
	public static final String COLUMN_WEF_DATE="wef_date";
	public static final String COLUMN_PRODUCT_CODE="product_code";
	public static final String COLUMN_LOCATION_DATE="location_date";
	public static final String COLUMN_DOC_ID="doc_id";
	public static final String COLUMN_DEMO1DOC_ID="demo1docId";
	public static final String COLUMN_DOC_ID_SNO="doc_id_sno";
	public static final String COLUMN_WEB_DOC_ID="web_doc_id";
	public static final String COLUMN_HEADER_ID="headerId";
	public static final String COLUMN_DATE="date";
	public static final String COLUMN_TIME="time";
	public static final String COLUMN_PARTY_CODE="party_code";
	public static final String COLUMN_ANDROID_PARTY_CODE="and_party_code";
	public static final String COLUMN_SREP_CODE="srep_code";
	public static final String COLUMN_ITEM_NAME="ItemName";
	public static final String COLUMN_QTY="qty";
	public static final String COLUMN_COMPTITOR_NO="Compt_no";
	public static final String COLUMN_PARTY_NO="Party_no";
	public static final String COLUMN_RATE="rate";
	public static final String COLUMN_CASES="_case";
	public static final String COLUMN_DISCOUNT="discount";
	public static final String COLUMN_REMARK="remark";
	public static final String COLUMN_SAMP_QTY="samp_qty";
	public static final String COLUMN_TECH_SUGGESTION="tech_suggestion";
	public static final String COLUMN_APP_REMARKS="app_remarks";
	public static final String COLUMN_ISPARTY_CONVERTED="isPartyConverted";
	public static final String COLUMN_NEW_APP="newApp";
	public static final String COLUMN_AVAILABILITY_SHOP="availability_shop";
	public static final String COLUMN_TECH_ADVANTAGE="tech_advantage	";
	public static final String COLUMN_NEW_APP_AREA="new_app_area";
	public static final String COLUMN_NEXT_VISIT_DATE="next_visit_date";
	public static final String COLUMN_REASONID="reasonId";
	public static final String COLUMN_NAVISION_ID="navision_id";
	public static final String COLUMN_SREP_CODEL2="srep_codel2";
	public static final String COLUMN_SREP_CODEL3="srep_codel3";
	public static final String COLUMN_UNDERID="UnderId";
	public static final String COLUMN_SREP_TYPE="SrepType";
	public static final String COLUMN_VISIT_DATE="visit_date";
	public static final String COLUMN_SREP_CODEBDP="srep_codebdp";
	public static final String COLUMN_MODE_OF_TRANSPORT="mode_of_transport";
	public static final String COLUMN_VEHICLE_USED="vehicle_used";
	public static final String COLUMN_ORDER_NO="order_no";
	public static final String COLUMN_PORDER_NO="Porder_no";
	public static final String COLUMN_PORDER1_NO="Porder1_no";
	public static final String COLUMN_ORDER1_NO="order1_no";
	public static final String COLUMN_STATUS="status";
	public static final String COLUMN_MEET_DOCID="meetdocid";
	public static final String COLUMN_MEET_FLAG="meetFlag";
	public static final String COLUMN_ORDER_TYPE="orderType";
	public static final String COLUMN_FREE_QTY="freeqty";
	public static final String COLUMN_DEMO_NO="demo_no";
	public static final String COLUMN_FAILEDVISIT_NO="FailedVisit_no";
	public static final String COLUMN_Dist_Stock_NO="distStock_no";
	public static final String COLUMN_VISIT_NO="visit_no";
	public static final String COLUMN_WEB_DOC_ID_SNO="web_doc_id_sno";
	public static final String COLUMN_DESCRIPTION="description";
	public static final String COLUMN_SEGMENT="segmentId";
	public static final String COLUMN_PRICE="price";
	public static final String COLUMN_CATEGORY="category";
	public static final String COLUMN_REPORTING_PERSON="reporting_person";
	public static final String COLUMN_INDUSTRY_TYPE="industry_type";
	public static final String COLUMN_ITEM_ID="item_id";
	public static final String COLUMN_APPLICATION_DETAILS = "Application_details";
	public static final String COLUMN_TYPE = "Type";
	public static final String COLUMN_ISDATAIMPORTED = "isDataImported";
	public static final String COLUMN_ISDATAEXPORTED = "isDataExported";
	public static final String COLUMN_UPLOAD = "isUpload";
	public static final String COLUMN_isMainUPLOAD = "isMainUpload";
	public static final String COLUMN_isSubUPLOAD = "isSubUpload";
	public static final String COLUMN_AMOUNT = "amount";
	public static final String COLUMN_SAMPLE_NO = "sample_no";
	public static final String COLUMN_CREATED_DATE = "CreatedDate";
	public static final String COLUMN_TIMESTAMP = "timestamp";
	public static final String COLUMN_PDAIDBYUSER = "pdaidbyuser";
	public static final String COLUMN_RECIEP_NO = "reciep_no";
	public static final String COLUMN_SYNC_ID = "sync_id";
	public static final String COLUMN_DISTRICT_CODE = "district_id";
	public static final String COLUMN_REGION_CODE = "Region_id";
	public static final String COLUMN_STDPACK = "stdpack";
	public static final String COLUMN_ITEM_CODE="itemcode";
	public static final String COLUMN_CLASS_ID="classid";
	public static final String COLUMN_PRICE_GROUP="pricegroup";
	public static final String COLUMN_ACTIVE="Active";
	public static final String COLUMN_LEVEL="level";
	public static final String COLUMN_PURCHASE_ORDERID="POrdId";
	public static final String COLUMN_PURCHASE_ORDER1ID="POrd1Id";
	public static final String COLUMN_DISCRIPTION="Discription";
	public static final String COLUMN_PURCHASEORDER_DOCID="PODocId";
	public static final String COLUMN_SERIAL_NO="Sno";
	public static final String COLUMN_DISTRIBUTER_ID="DistId";
	public static final String COLUMN_DISPATCH_NAME="DispName";
	public static final String COLUMN_DISPATCH_STATE="DispState";
    public static final String COLUMN_DISPATCH_COUNTRY="DispCountry";
    public static final String COLUMN_DISPATCH_CITY="City";
    public static final String COLUMN_TRANSPORTER="transporterId";
    public static final String COLUMN_PROJECTID="projectId";
    public static final String COLUMN_SCHEMEID="schemeId";
    public static final String COLUMN_FROM_TIME1="fromtime1";
    public static final String COLUMN_FROM_TIME2="fromtime2";	
    public static final String COLUMN_TO_TIME1="totime1";
    public static final String COLUMN_TO_TIME2="totime2";
    public static final String COLUMN_NEXT_VISIT_TIME="nextVisitTime";
    public static final String COLUMN_IMAGE_PATH="path";
	// add 4 july for attandance
	public static final String COLUMN_IMAGE_PATH_LAUNCH="launch_image_path";
	public static final String COLUMN_IMAGE_PATH_LOCK="lock_image_path";
	///////
    public static final String COLUMN_NOOFDAYS="noOfDays";
    public static final String COLUMN_FROM_DATE="fromDate";
    public static final String COLUMN_TO_DATE="toDate";
    public static final String COLUMN_REASON="reason";
    public static final String COLUMN_APPROVE_STATUS="appStatus";
    public static final String COLUMN_APPROVE_BY="appBy";
    public static final String COLUMN_APPROVE_REMARK="appRemark";
    public static final String COLUMN_APPROVE_BY_SMID="appBySmid";
    public static final String COLUMN_LEAVE_FLAG="leaveFlag";
    public static final String COLUMN_PLANNED_DATE="PlannedDate";
    public static final String COLUMN_COMPANY_NAME="CompName";
    public static final String COLUMN_COMPANY_URL="CompUrl";
    public static final String COLUMN_ORDER_BY_EMAIL="orderbyemail";
    public static final String COLUMN_ORDER_BY_PHONE="orderbyphone";
    public static final String COLUMN_BRAND_ACTIVITY="brandActivity";
    public static final String COLUMN_MEET_ACTIVITY="meetActivity";
    public static final String COLUMN_ROADSHOW="roadShow";
    public static final String COLUMN_GENERAL_INFO="generalInfo";
    public static final String COLUMN_SCHEME="scheme";
    public static final String COLUMN_OTHER_ACTIVITY="otherActivity";
    public static final String COLUMN_STOCK="stock";
	public static final String COLUMN_ITEM_WISE_SECONDARY_SALES="ItemwiseSeconadarySales";
	public static final String COLUMN_DIST_SEARCH_BY_NAME="DistSearchByName";
	public static final String COLUMN_ITEM_SEARCH_BY_NAME="ItemSearchByName";
	public static final String COLUMN_AREA_WISE_DISTRIBUTOR="AreawiseDistributor";
	public static final String COLUMN_FTP_HOST="Host";
	public static final String COLUMN_IMAGE_DIRECTORY_NAME="ImageDirectory";
	public static final String COLUMN_FTP_USER_NAME="UserName";
	public static final String COLUMN_FTP_PASSWORD="Password";
	public static final String COLUMN_FTP_DIRECTORY="FtpDirectory";
	public static final String COLUMN_SHOW_DISCUSSION_STOCK="DistDiscussionStock";
	public static final String COLUMN_MANDATORY_DSR_REMARK="DsrRemarkMandatory";
	public static final String COLUMN_PARTY_CAPTION="PartyCaptio";
	public static final String COLUMN_VISIT="Visit";
	public static final String COLUMN_ATTANDANCE="Attandance";
	public static final String COLUMN_OTHER_EXPENSE="OtherExp";
	public static final String COLUMN_OTHER_EXPENSE_REMARK="OtherExpRmk";
	public static final String COLUMN_TO_AREA="toArea";
	public static final String COLUMN_CHARGEABLE="chargeable";
	public static final String COLUMN_DISTRIBUTER_IDS="distIds";
	public static final String COLUMN_DISTRIBUTER_NAMES="distNames";
	public static final String COLUMN_PURPOSE_IDS="purposeIds";
	public static final String COLUMN_PURPOSE_NAMES="purposeNames";
	public static final String COLUMN_NOTIFICATION="notify";
	public static final String COLUMN_pro_id="pro_id";
	public static final String COLUMN_userid="userid";
	public static final String COLUMN_url="url";
	public static final String COLUMN_msz="msz";
	public static final String COLUMN_URL_Page="urlPage";
	public static final String COLUMN_Smid="smid";
	public static final String COLUMN_LVQR="lvqr";
	public static final String COLUMN_LAT = "latitude";
	public static final String COLUMN_LNG = "longitude";
	public static final String COLUMN_LAT_LNG_TIME = "LatlngTime";
	public static final String COLUMN_IMPORT_TIME_STAMP = "ImportTimeStamp";
	public static final String COLUMN_CREATED_BY = "CREATED_BY";
	public static final String COLUMN_Row_Color="rowcolor";

	public static final String COLUMN_TIME_STAMP = "TimeStamp";
	public static final String COLUMN_ENTITY_NAME = "EntityName";
	public static final String COLUMN_ENTITY_ID = "EntityId";

	public static final String COLUMN_RECENT_SEARCH_ID = "id";
	public static final String COLUMN_PARTY_NAME = "partyName";
	public static final String COLUMN_PERSON_NAME = "personName";
	public static final String COLUMN_MOBILE_NO = "mobileNumber";



	//*************** Vinayak ///////////////

	public static final String NEXT_VISIT_TIME_REQUIRED = "NEXT_VISIT_TIME_REQUIRED";
	public static final String Primary_Disc_NextVTime = "Primary_Disc_NextVTime";
    /**
	 *Author Name SWATI GUPTA { Variable Description arrayListForColumns
	 * for string Table's Columns along with corresponding data types }
	 * 
	 */




	private static final String ALTER_DISTRIBUTORMAST_TABLE_ADD_CREDITDAYS =
			"ALTER TABLE mastDristributor add COLUMN CreditDays VARCHAR";

	private static final String ALTER_PARTYMAST_TABLE_ADD_ISBLOCKED =
			"ALTER TABLE mastParty add COLUMN isBlocked VARCHAR";

	private static final String ALTER_VISITL1_TABLE_ADD_VISIT =
			"ALTER TABLE visitl1 add COLUMN Visit VARCHAR";

	private static final String ALTER_VISITL1_TABLE_ADD_ATTANDANSE =
			"ALTER TABLE visitl1 add COLUMN Attandance VARCHAR";

	private static final String ALTER_VISITL1_TABLE_ADD_AREA_CODE =
			"ALTER TABLE visitl1 add COLUMN area_code VARCHAR";

	private static final String ALTER_VISITL1_TABLE_ADD_OTHER_EXPENSE =
			"ALTER TABLE visitl1 add COLUMN OtherExp VARCHAR";

	private static final String ALTER_VISITL1_TABLE_ADD_OTHER_EXPENSE_REM =
			"ALTER TABLE visitl1 add COLUMN OtherExpRmk VARCHAR";

	private static final String ALTER_VISITL1_TABLE_ADD_TO_AREA =
			"ALTER TABLE visitl1 add COLUMN toArea VARCHAR";

	private static final String ALTER_VISITL1_TABLE_ADD_CHARGEABLE =
			"ALTER TABLE visitl1 add COLUMN chargeable VARCHAR";


	ArrayList<String> arrayListForColumns;


	public String creatWriteLogTable()
	{
		arrayListForColumns = new ArrayList<String>();
		arrayListForColumns.add(COLUMN_Smid +" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add("params"+ " "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add("method"+" "+ DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add("response"+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add("timeStamp"+" "+DATA_TYPE_NUMERIC);
		WriteLogTableCreate=createTableSting(arrayListForColumns,TABLE_WRITE_LOG);
		return WriteLogTableCreate;
	}

	/******************************** Ashutosh  *******************************/
	public String createDynamicMenuTableString()
	{
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_PID             +" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_Form_Filter     +" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PAGE_ID         +" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VIEW_P          +" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ADD_P           +" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_EDIT_P          +" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DELETE_P        +" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PAGE_NAME       +" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DISPLAY_NAME    +" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PARENT_ID       +" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LEVEL_IDX       +" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_IDX             +" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_Icon            +" "+DATA_TYPE_BLOB);


		DYNAMIC_MENU_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_DYNAMIC_MENU);
		return DYNAMIC_MENU_TABLE_CREATE;

	}

	public String createTransTourPlanTableString()
	{
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_ID+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_HEADER_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CITY_IDS+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CITY_NAMES+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DISTRIBUTER_IDS+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DISTRIBUTER_NAMES+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PURPOSE_IDS+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PURPOSE_NAMES+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_APPROVE_BY+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_APPROVE_BY_SMID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_APPROVE_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_APPROVE_STATUS+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		TRANS_TOUR_PLAN_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_TRANS_TOUR_PLAN);
		return TRANS_TOUR_PLAN_TABLE_CREATE;

	}

	public String createTransTourPlanHeaderTableString()
	{
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_ID+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_FROM_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TO_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_APPROVE_BY+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_APPROVE_BY_SMID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_APPROVE_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_APPROVE_STATUS+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		TRANS_TOUR_PLAN_HEADER_TABLE_CREATE =createTableSting(arrayListForColumns,TABLE_TRANS_TOUR_PLAN_HEADER);
		return TRANS_TOUR_PLAN_HEADER_TABLE_CREATE;

	}
	public String createDistItemTemplateTableString()
	{
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_ITEM_ID+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_RATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add("primary key("+COLUMN_CODE+","+COLUMN_ITEM_ID+")");
		DIST_ITEM_TEMPLATE_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_DIST_ITEM_TEMPLATE);
		return DIST_ITEM_TEMPLATE_TABLE_CREATE;
	}


	public String createTransDistStockTableString()
	{
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DISTRIBUTER_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PRODUCT_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CASES+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UNIT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_QTY+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_NUMERIC);
		TRANS_DIST_STOCK_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_TRANS_DIST_STOCK);
		return TRANS_DIST_STOCK_TABLE_CREATE;
	}

	public static String COLUMN_PRIM_FAILED_VISIT_NEXT_TIME = "PRIM_FAILED_VISIT_NEXT_TIME";
	public static String COLUMN_PRIM_FAILED_VISIT_NEXT_TIME_REQ = "PRIM_FAILED_VISIT_NEXT_TIME_REQ";
	public String createAppEnviroString()
	{
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_ITEM_WISE_SECONDARY_SALES+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DIST_SEARCH_BY_NAME+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ITEM_SEARCH_BY_NAME+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AREA_WISE_DISTRIBUTOR+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_IMAGE_DIRECTORY_NAME+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_FTP_HOST+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_FTP_USER_NAME+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_FTP_PASSWORD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_FTP_DIRECTORY+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SHOW_DISCUSSION_STOCK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MANDATORY_DSR_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PARTY_CAPTION+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(Primary_Disc_NextVTime+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(NEXT_VISIT_TIME_REQUIRED+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PRIM_FAILED_VISIT_NEXT_TIME+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PRIM_FAILED_VISIT_NEXT_TIME_REQ+" "+DATA_TYPE_VARCHAR);

		APPDATA_ENVIRO_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_APPENVIRO);
		return APPDATA_ENVIRO_TABLE_CREATE;
	}
	public String createAppDataMastString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_COMPANY_NAME+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_COMPANY_URL+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TYPE+" "+DATA_TYPE_VARCHAR);
		APPDATA_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_APPDATA);
		Log.d("APPDATA_TABLE_CREATE",APPDATA_TABLE_CREATE);
		return APPDATA_TABLE_CREATE;	
	}

	public String createTransportMastString()
	{
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		TRANSPORT_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_TRANSPORT);
		return TRANSPORT_TABLE_CREATE;
	}

	public String createVisitCodeMastString()
	{
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR);
		VISITCODE_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_VISIT_CODE);
		return VISITCODE_TABLE_CREATE;
	}


	public String createVehicleMastString()
	{
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);

		VEHICLE_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_VEHICLE);
		return VEHICLE_TABLE_CREATE;
	}
	
	public String createTransBeatPlanTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_ID+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PLANNED_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_APPROVE_STATUS+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_APPROVE_BY+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_APPROVE_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_FROM_DATE+" "+DATA_TYPE_VARCHAR+",");
	    arrayListForColumns.add(COLUMN_CITY_CODE+" "+DATA_TYPE_VARCHAR+",");
	    arrayListForColumns.add(COLUMN_BEAT_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_APPROVE_BY_SMID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);

		TRANSBEATPLAN_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_TRANSBEATPLAN);
		return TRANSBEATPLAN_TABLE_CREATE;	
	}
public String createTransLeaveRequestTableString()
{	
	arrayListForColumns=new ArrayList<String>();
	arrayListForColumns.add(COLUMN_ID+" "+DATA_TYPE_INTEGER+",");
	arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_NOOFDAYS+" "+DATA_TYPE_NUMERIC+",");
	arrayListForColumns.add(COLUMN_FROM_DATE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_TO_DATE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_REASON+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_APPROVE_STATUS+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_APPROVE_BY+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_APPROVE_REMARK+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_APPROVE_BY_SMID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_LEAVE_FLAG+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);

	TRANSLEAVE_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_TRANSLEAVE);
	Log.d("TRANSLEAVE_TABLE_CREATE",TRANSLEAVE_TABLE_CREATE);
	return TRANSLEAVE_TABLE_CREATE;	
}
public String createTransVisitDistTableString()
{	
	arrayListForColumns=new ArrayList<String>();
	arrayListForColumns.add(COLUMN_ID+" "+DATA_TYPE_INTEGER+",");
	arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_CITY_CODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_DISTRIBUTER_ID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_FROM_TIME1+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_TO_TIME1+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_NEXT_VISIT_DATE+" "+DATA_TYPE_DATE+",");
	arrayListForColumns.add(COLUMN_NEXT_VISIT_TIME+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_IMAGE_PATH+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
	arrayListForColumns.add(COLUMN_STOCK+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
	arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_NUMERIC+",");
	arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC);
	VISITDIST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_VISITDIST);
	Log.d("VISITDIST_TABLE_CREATE",VISITDIST_TABLE_CREATE);
	return VISITDIST_TABLE_CREATE;
}
	@SuppressLint("LongLogTag")
	public String createCompetitorTableString()
{	
	arrayListForColumns=new ArrayList<String>();
	arrayListForColumns.add(COLUMN_ID+" "+DATA_TYPE_INTEGER+",");
	arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_INTEGER+",");
	arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_ITEM_NAME+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_QTY+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_RATE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_IMAGE_PATH+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
	arrayListForColumns.add(COLUMN_ANDROID_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_BRAND_ACTIVITY+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_MEET_ACTIVITY+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_ROADSHOW+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_SCHEME+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_GENERAL_INFO+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_DISCOUNT+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_OTHER_ACTIVITY+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
	arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_NUMERIC+",");
	// Added Three new column 19 April 2017
	arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC);
	COMPETITORMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_COMPETITOR);
	Log.d("COMPETITORMAST_TABLE_CREATE",COMPETITORMAST_TABLE_CREATE);
	return COMPETITORMAST_TABLE_CREATE;	
}
public String createTransCollectionTableString()
{	
	arrayListForColumns=new ArrayList<String>();
	arrayListForColumns.add(COLUMN_ID+" "+DATA_TYPE_INTEGER+",");
	arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_ITEM_CODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_MODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_AMOUNT+" "+DATA_TYPE_NUMERIC+",");
	arrayListForColumns.add(COLUMN_PAYMENT_DATE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_CHEQUE_DDNO+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_CHQ_DATE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_BANK+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_BRANCH+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
	arrayListForColumns.add(COLUMN_ANDROID_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
	arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_NUMERIC+",");
	// Added three new column 19 April 17
	arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC);
	TRANSCOLLECTION_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_TRANSCOLLECTION);
	Log.d("TRANSCOLLECTION_TABLE",TRANSCOLLECTION_TABLE_CREATE);
	return TRANSCOLLECTION_TABLE_CREATE;	
}
public String createDistributerCollectionTableString()
{	
	arrayListForColumns=new ArrayList<String>();
	arrayListForColumns.add(COLUMN_ID+" "+DATA_TYPE_INTEGER+",");
	arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_DISTRIBUTER_ID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_MODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_AMOUNT+" "+DATA_TYPE_NUMERIC+",");
	arrayListForColumns.add(COLUMN_PAYMENT_DATE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_CHEQUE_DDNO+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_CHQ_DATE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_BANK+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_BRANCH+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
	arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
	arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_NUMERIC+",");
	//  Added Three New Column 20/4/17
	arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC);
	DISTRIBUTERCOLLECTION_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_DISTRIBUTERCOLLECTION);
	return DISTRIBUTERCOLLECTION_TABLE_CREATE;	
}
public String createProjecttMastTableString()
{	
	arrayListForColumns=new ArrayList<String>();
	arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
	arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY);
	PROJECT_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_PROJECT);
	Log.d("PROJECT_TABLE_CREATE",PROJECT_TABLE_CREATE);
	return PROJECT_TABLE_CREATE;
	
}

public String createSchemeMastTableString()
{	
	arrayListForColumns=new ArrayList<String>();
	arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_INTEGER+",");
	arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
	arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY);
	SCHEME_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_SCHEME);
	Log.d("SCHEME_TABLE_CREATE",SCHEME_TABLE_CREATE);
	return SCHEME_TABLE_CREATE;
	
}


public String createTransportMastTableString()
{	
	arrayListForColumns=new ArrayList<String>();
	arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
	arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
	arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY);
	TRANSPORTER_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_TRANSPORTER);
	//Log.d("TRANSPORTER_TABLE_CREATE",TRANSPORTER_TABLE_CREATE);
	return TRANSPORTER_TABLE_CREATE;
	
}

	public String createFailedVisitRemarkMastTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		FAILED_VISIT_MAST_DATA_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_FAILED_VISIT_MAST);

		return FAILED_VISIT_MAST_DATA_TABLE_CREATE;
		
	}
	public String createPurchaseOrderTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_DOC_ID+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_DOC_ID_SNO+" "+DATA_TYPE_INTEGER_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_WEB_DOC_ID_SNO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_DIST_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_PRODUCT_GROUP_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_PRODUCT_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_QTY+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_RATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_isMainUPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_isSubUPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add("primary key("+COLUMN_DOC_ID+","+COLUMN_DOC_ID_SNO+")");
		PURCHASE_ORDER_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_PURCHASE_ORDER);
		//Log.d("PURCHASE_ORDER_TABLE_CREATE",PURCHASE_ORDER_TABLE_CREATE);
		return PURCHASE_ORDER_TABLE_CREATE;
		
	}
	public String createSessionDataTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_ITEM_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_QTY+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_RATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CASES+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UNIT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ORDER1_ANDROID_DOCID+" " +DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+"");
		SESSION_DATA_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_SESSION_DATA);
		//Log.d("SESSION_DATA_TABLE_CREATE",SESSION_DATA_TABLE_CREATE);
		return SESSION_DATA_TABLE_CREATE;
		
	}
	public String createPdaidByUserTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_PDAIDBYUSER+" "+DATA_TYPE_VARCHAR);
		DEVICE_INFO_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_DEVICE_INFO);
		//Log.d("DEVICE_INFO_TABLE_CREATE",DEVICE_INFO_TABLE_CREATE);
		return DEVICE_INFO_TABLE_CREATE;
		
	}
	public String createSynTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_ISDATAIMPORTED+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ISDATAEXPORTED+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		SYN_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_SYN);
		Log.d("SMAN_TABLE_CREATE",SYN_TABLE_CREATE);
		return SYN_TABLE_CREATE;
		
	}
	public String createSmanTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LEVEL+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ROLEID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_REPORTING_PERSON+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UNDERID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SREP_TYPE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		SMAN_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_SALESMANMAST);
		Log.d("SMAN_TABLE_CREATE",SMAN_TABLE_CREATE);
		return SMAN_TABLE_CREATE;
		
	}
	public String createHistoryTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_ITEM_ID+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_RATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add("primary key("+COLUMN_CODE+","+COLUMN_ITEM_ID+")");
		HISTORY_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_HISTORY);
		Log.d("HISTORY_TABLE_CREATE",HISTORY_TABLE_CREATE);
		return HISTORY_TABLE_CREATE;
		
	}
	public String createPricelistTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEF_DATE+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_PRODUCT_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_PRODUCT_GROUP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MRP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_DP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_RP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add("primary key("+COLUMN_WEF_DATE+","+COLUMN_PRODUCT_CODE+")");
		PRICELIST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_PRICELIST);
		Log.d("PRICELIST_TABLE_CREATE",PRICELIST_TABLE_CREATE);
		return PRICELIST_TABLE_CREATE;
		
	}

	public String createUserLocationTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_USER_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LOCATION_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		USERLOCATION_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_USERLOCATION);
		//Log.d("USERLOCATION_TABLE_CREATE",USERLOCATION_TABLE_CREATE);
		return USERLOCATION_TABLE_CREATE;
		
	}
	public String createOrderTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_ORDER_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AMOUNT+" Text,");
		arrayListForColumns.add(COLUMN_STATUS+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MEET_FLAG+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MEET_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ORDER_TYPE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_isSubUPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_ANDROID_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC);

		ORDER_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_ORDER);
		Log.d("ORDER_TABLE_CREATE",ORDER_TABLE_CREATE);
		return ORDER_TABLE_CREATE;
		
	}
	public String createOrder1TableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_ORDER1_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ORDER1_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ORDER_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SERIAL_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_BEAT_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PRODUCT_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_QTY+" "+DATA_TYPE_NUMERIC+",");		
		arrayListForColumns.add(COLUMN_FREE_QTY+" "+DATA_TYPE_NUMERIC+",");	
		arrayListForColumns.add(COLUMN_RATE+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_DISCOUNT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AMOUNT+" Text,");
		arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MEET_FLAG+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MEET_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_ANDROID_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_CASES+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UNIT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC);

		ORDER1_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_ORDER1);
		Log.d("ORDER1_TABLE_CREATE",ORDER1_TABLE_CREATE);
		return ORDER1_TABLE_CREATE;
		
	}
	public String createTransDemoTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_DOC_ID+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_APPLICATION_DETAILS+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AVAILABILITY_SHOP+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_APP_REMARKS+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ISPARTY_CONVERTED+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_NEW_APP_AREA+" "+DATA_TYPE_BINARY+",");
		arrayListForColumns.add(COLUMN_TECH_ADVANTAGE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TECH_SUGGESTION+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_NEW_APP+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ORDER_TYPE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CLASS_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SEGMENT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PRODUCT_GROUP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ITEM_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_IMAGE_PATH+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_ANDROID_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC);

		TRANSDEMO_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_TRANSDEMO);
		Log.d("TRANSDEMO_TABLE_CREATE",TRANSDEMO_TABLE_CREATE);
		return TRANSDEMO_TABLE_CREATE;
		
	}




	/*
	Sales retyurn tables
	 */

 /*   public String createSalesReturnTableString()
    {
        arrayListForColumns=new ArrayList<String>();

        arrayListForColumns.add(COLUMN_SALES_RETURN_ID+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_SMID+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_ANDROID_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
        arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_BATCH_No+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MFD_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DSRLOCK+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC);
        SALESRETURN_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_SALES_RETURN);

        return SALESRETURN_TABLE_CREATE;

    }*/
/*

    public String createSalesReturn1TableString()
    {
        arrayListForColumns=new ArrayList<String>();
        arrayListForColumns.add(COLUMN_SALES_RETURN_ID+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_ORDER1_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_SERIAL_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_SMID+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_ANDROID_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DSRLOCK+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_PRODUCT_CODE+" "+DATA_TYPE_VARCHAR+",");

      // arrayListForColumns.add(COLUMN_BEAT_CODE+" "+DATA_TYPE_VARCHAR+",");

        arrayListForColumns.add(COLUMN_QTY+" "+DATA_TYPE_NUMERIC+",");

        arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");

        arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");

        arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
        arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_NUMERIC+",");
        arrayListForColumns.add(COLUMN_CASES+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_UNIT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_BATCH_No+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MFD_DATE+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
        arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC);

        ORDER1_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_SALES_RETURN1);
        Log.d("ORDER1_TABLE_CREATE",TABLE_SALES_RETURN1);
        return ORDER1_TABLE_CREATE;

    }

*/


	public String createSalesReturnTableString()
	{
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_ORDER_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AMOUNT+" Text,");
		arrayListForColumns.add(COLUMN_STATUS+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MEET_FLAG+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MEET_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ORDER_TYPE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_isSubUPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_BATCH_No+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MFD_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_ANDROID_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC);

		SALESRETURN_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_SALES_RETURN);
		Log.d("ORDER_TABLE_CREATE",SALESRETURN_TABLE_CREATE);
		return SALESRETURN_TABLE_CREATE;

	}

	public String createSalesReturn1TableString()
	{
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_ORDER1_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ORDER1_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ORDER_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SERIAL_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_BEAT_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PRODUCT_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_QTY+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_FREE_QTY+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_RATE+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_DISCOUNT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AMOUNT+" Text,");
		arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MEET_FLAG+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MEET_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_ANDROID_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_CASES+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UNIT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_BATCH_No+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MFD_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC);


		SALESRETURN1_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_SALES_RETURN1);
		//Log.d("ORDER1_TABLE_CREATE",SALESRETURN1_TABLE_CREATE);
		return SALESRETURN1_TABLE_CREATE;

	}



	public String createTransDemo1TableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_DEMO1DOC_ID+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_DOC_ID+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_INTEGER_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_SERIAL_NO+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ITEM_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_QTY+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SAMP_QTY+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_RATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MEET_FLAG+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MEET_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		//Column added
		arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add("primary key("+COLUMN_DOC_ID+","+COLUMN_WEB_DOC_ID+")");

		TRANSDEMO1_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_TRANSDEMO1);
		//Log.d("TRANSDEMO1_TABLE_CREATE",TRANSDEMO1_TABLE_CREATE);
		return TRANSDEMO1_TABLE_CREATE;
		
	}
	public String createFailedVisitTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_WEB_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DISTRIBUTER_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_NEXT_VISIT_DATE+" "+DATA_TYPE_DATE+",");
		arrayListForColumns.add(COLUMN_TIME+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_REASONID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_ANDROID_PARTY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_NUMERIC+",");
		// Added 3 New Column 19April17
		arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC);
		TRANSFAILED_VISIT_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_TRANSFAILED_VISIT);
		//Log.d("TRANSFAILED_VISIT_TABLE_CREATE",TRANSFAILED_VISIT_TABLE_CREATE);
		return TRANSFAILED_VISIT_TABLE_CREATE;
		
	}
	public String createPorderTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_PURCHASE_ORDERID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PURCHASEORDER_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
	    arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DISTRIBUTER_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TRANSPORTER+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PROJECT_TYPE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PROJECTID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SCHEMEID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DISPATCH_NAME+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ADDRESS1+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ADDRESS2+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DISPATCH_CITY+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PIN+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DISPATCH_STATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DISPATCH_COUNTRY+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PHONE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MOBILE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_EMAIL+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AMOUNT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY);
		PORDER_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_PORDER);
		Log.d("PORDER_TABLE_CREATE",PORDER_TABLE_CREATE);
		return PORDER_TABLE_CREATE;
		
	}
	public String createPorder1TableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_PURCHASE_ORDER1ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PORDER1_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PURCHASE_ORDERID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PURCHASEORDER_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SERIAL_NO+" "+DATA_TYPE_INTEGER_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_ANDROID_SERIAL_NO+" "+DATA_TYPE_INTEGER_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DISTRIBUTER_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PRODUCT_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_QTY+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_DISCRIPTION+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_RATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY);
		PORDER1_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_PORDER1);
		Log.d("PORDER1_TABLE_CREATE",PORDER1_TABLE_CREATE);
		return PORDER1_TABLE_CREATE;
		
	}
	
	public String createVisitl1TableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_DOC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VISIT_DATE+" "+DATA_TYPE_DATE+",");
		arrayListForColumns.add(COLUMN_NEXT_VISIT_DATE+" "+DATA_TYPE_DATE+",");
		arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CITY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DISTRIBUTER_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_NCITY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_FROM_TIME1+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_FROM_TIME2+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TO_TIME1+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_TO_TIME2+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_WITH_USR_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_NEXTWITH_USR_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MODE_OF_TRANSPORT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VEHICLE_USED+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_INDUSTRY_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DSRLOCK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CITY_IDS+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CITY_NAMES+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_ORDER_BY_EMAIL+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ORDER_BY_PHONE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VISIT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ATTANDANCE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_OTHER_EXPENSE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_OTHER_EXPENSE_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TO_AREA+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CHARGEABLE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_IMPORT_TIME_STAMP+" "+DATA_TYPE_NUMERIC+",");

		arrayListForColumns.add(COLUMN_IMAGE_PATH_LAUNCH+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_IMAGE_PATH_LOCK+" "+DATA_TYPE_VARCHAR);


		VISITL1_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_VISITL1);
		Log.d("VISITL1_TABLE_CREATE",VISITL1_TABLE_CREATE);
		return VISITL1_TABLE_CREATE;
		
	}
	public String createEnviroTableString()
	{	
		arrayListForColumns=new ArrayList<String>();

		arrayListForColumns.add(COLUMN_SREP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ORDER_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_PORDER_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PORDER1_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DEMO_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_FAILEDVISIT_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_VISIT_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_COMPTITOR_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_PARTY_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_DISCUSSION_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_DIST_COLLEC_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_LEAVE_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_BEATPLAN_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_ORDER1_NO+" "+DATA_TYPE_INTEGER+",");
		//		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_RECIEP_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_Dist_Stock_NO+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add("ASalesReturnNo"+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add("ASales1ReturnNo"+" "+DATA_TYPE_INTEGER);

		ENVIRO_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_ENVIRO);
		Log.d("ENVIRO_TABLE_CREATE",ENVIRO_TABLE_CREATE);
			return ENVIRO_TABLE_CREATE;
		
	}
	public String createCountryTableString()
	{

		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_DATE+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		COUNTRYMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_COUNTRYMAST);
		//Log.d("COUNTRYMAST_TABLE_CREATE",COUNTRYMAST_TABLE_CREATE);
		return COUNTRYMAST_TABLE_CREATE;
		
	}
	
	public String createRegionTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_COUNTRY_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_DATE+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		REGIONMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_REGIONMAST);
		//Log.d("REGIONMAST_TABLE_CREATE",REGIONMAST_TABLE_CREATE);
		return REGIONMAST_TABLE_CREATE;
		
	}
	public String createStateTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_REGION_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_DATE+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		STATEMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_STATEMAST);
		//Log.d("STATEMAST_TABLE_CREATE",STATEMAST_TABLE_CREATE);
		return STATEMAST_TABLE_CREATE;
		
	}
	public String createDistrictTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_STATE_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_DATE+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		DISTRICTMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_DISTRICTMAST);
		//Log.d("DISTRICTMAST_TABLE_CREATE",DISTRICTMAST_TABLE_CREATE);
		return DISTRICTMAST_TABLE_CREATE;
		
	}
	
	public String createCityTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_DISTRICT_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_DATE+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_STATE_CODE+" "+DATA_TYPE_VARCHAR);
		CITYMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_CITYMAST);
		//Log.d("CITYMAST_TABLE_CREATE",CITYMAST_TABLE_CREATE);
		return CITYMAST_TABLE_CREATE;
		
	}
	
		
	public String createAreaTypeTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		AREATYPEMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_AREATYPEMAST);
		//Log.d("AREATYPEMAST_TABLE_CREATE",AREATYPEMAST_TABLE_CREATE);
		return AREATYPEMAST_TABLE_CREATE;
		
	}
	public String createAreaTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_AREATYPE_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CITY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_DATE+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		AREAMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_AREAMAST);
		//Log.d("AREAMAST_TABLE_CREATE",AREAMAST_TABLE_CREATE);
		return AREAMAST_TABLE_CREATE;
		
	}
	public String createBeatTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_DATE+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		BEATMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_BEATMAST);
		//Log.d("BEATMAST_TABLE_CREATE",BEATMAST_TABLE_CREATE);
		return BEATMAST_TABLE_CREATE;
		
	}
	
	public String createUserAreaTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_USER_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add("primary key("+COLUMN_USER_CODE+","+COLUMN_AREA_CODE+")");
		USERAREAMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_USERAREAMAST);
		//Log.d("USERAREAMAST_TABLE_CREATE",USERAREAMAST_TABLE_CREATE);
		return USERAREAMAST_TABLE_CREATE;
		
	}
	public String createDistributerTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_ADDRESS1+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ADDRESS2+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CITY_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_PIN+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CONTACT_PERSON+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MOBILE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PHONE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_EMAIL+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_BLOCKED_REASON+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_BLOCK_DATE+" "+DATA_TYPE_DATE+",");
		arrayListForColumns.add(COLUMN_BLOCKED_BY+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_BEAT_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_INDUSTRY_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_POTENTIAL+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PARTY_TYPE_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CST_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VATTIN_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SERVICETAXREG_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PAN_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREDIT_LIMIT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_OUTSTANDING+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PENDING_ORDER+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_DATE+",");
		arrayListForColumns.add(COLUMN_OPEN_ORDER+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_CREDIT_DAYS+" "+DATA_TYPE_VARCHAR);
		DISTMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_DISTRIBUTERMAST);
		//Log.d("DISTMAST_TABLE_CREATE",DISTMAST_TABLE_CREATE);
		return DISTMAST_TABLE_CREATE;
		
	}


	public String createDistAreaTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_DIST_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add("primary key("+COLUMN_DIST_CODE+","+COLUMN_AREA_CODE+")");
		DISTAREAMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_DISTAREAMAST);
		//Log.d("DISTAREAMAST_TABLE_CREATE",DISTAREAMAST_TABLE_CREATE);
		return DISTAREAMAST_TABLE_CREATE;
		
	}
	public String createIndustryTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		INDUSTRYMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_INDUSTRYMAST);
		//Log.d("INDUSTRYMAST_TABLE_CREATE",INDUSTRYMAST_TABLE_CREATE);
		return INDUSTRYMAST_TABLE_CREATE;
		
	}

	public String createSubIndustryTableString()
	{
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_INDUSTRY_NAME+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		SUBINDUSTRYMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_SUBINDUSTRYMAST);
		//Log.d("INDUSTRYMAST_TABLE_CREATE",INDUSTRYMAST_TABLE_CREATE);
		return SUBINDUSTRYMAST_TABLE_CREATE;

	}
	
	public String createProductClassTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		PRODUCTCLASSMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_PRODUCTCLASSMAST);
		//Log.d("PRODUCTCLASSMAST_TABLE_CREATE",PRODUCTCLASSMAST_TABLE_CREATE);
		return PRODUCTCLASSMAST_TABLE_CREATE;
		
	}
	public String createSegmentTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		SEGMENTMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_SEGMENTMAST);
		//Log.d("SEGMENTMAST_TABLE_CREATE",SEGMENTMAST_TABLE_CREATE);
		return SEGMENTMAST_TABLE_CREATE;
		
	}
	
	public String createProductGroupTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		PRODUCTGROUPMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_PRODUCTGROUPMAST);
		//Log.d("PRODUCTGROUPMAST_TABLE_CREATE",PRODUCTGROUPMAST_TABLE_CREATE);
		return PRODUCTGROUPMAST_TABLE_CREATE;
		
	}
	
	public String createProductMastTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UNIT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_STDPACK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MRP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_DP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_RP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_PRODUCT_GROUP_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ITEM_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SEGMENT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CLASS_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PRICE_GROUP+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_DATE+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		PRODUCTMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_PRODUCTMAST);
		//Log.d("PRODUCTMAST_TABLE_CREATE",PRODUCTMAST_TABLE_CREATE);
		return PRODUCTMAST_TABLE_CREATE;
		
	}
	public String createPartyTypeMastTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		PARTYTYPEMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_PARTYTYPEMAST);
		//Log.d("PARTYTYPEMAST_TABLE_CREATE",PARTYTYPEMAST_TABLE_CREATE);
		return PARTYTYPEMAST_TABLE_CREATE;
		
	}
	public String createPartyTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ANDROID_DOCID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ADDRESS1+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ADDRESS2+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CITY_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PIN+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CONTACT_PERSON+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_MOBILE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PHONE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_EMAIL+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_BLOCKED_REASON+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_BLOCK_DATE+" "+DATA_TYPE_DATE+",");
		arrayListForColumns.add(COLUMN_BLOCKED_BY+" "+DATA_TYPE_INTEGER+",");
		arrayListForColumns.add(COLUMN_AREA_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_BEAT_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_INDUSTRY_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_POTENTIAL+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PARTY_TYPE_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CST_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_VATTIN_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SERVICETAXREG_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PAN_NO+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_REMARK+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DISTRIBUTER_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREDIT_LIMIT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_OUTSTANDING+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PENDING_ORDER+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_OPEN_ORDER+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREDIT_DAYS+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_SYNC_ID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_CREATED_DATE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_USER_CODE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DOB+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DOA+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC+",");
		arrayListForColumns.add(COLUMN_IS_BLOCKED+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ID+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_CREATED_BY+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LAT+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LNG+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_LAT_LNG_TIME+" "+DATA_TYPE_NUMERIC);

		PARTYMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_PARTYMAST);
		Log.d("PARTYMAST_TABLE_CREATE",PARTYMAST_TABLE_CREATE);
		return PARTYMAST_TABLE_CREATE;
		
	}

	public String createUserTableString()
	{	
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add(COLUMN_WEB_CODE+" "+DATA_TYPE_VARCHAR_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add(COLUMN_USR_ID+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_NAME+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_PASSWORD+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_LEVEL+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_PDA_ID+" "+DATA_TYPE_VARCHAR_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_DSR_ALLOW_DAYS+" "+DATA_TYPE_INTEGER_NOT_NULL+",");
		arrayListForColumns.add(COLUMN_ACTIVE+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_EMAIL+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_ROLEID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DEPTID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_DESIGID+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_UPLOAD+" "+DATA_TYPE_VARCHAR+",");
		arrayListForColumns.add(COLUMN_TIMESTAMP+" "+DATA_TYPE_NUMERIC);
		USERMAST_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_USERMAST);
		//Log.d("USERMAST_TABLE_CREATE",USERMAST_TABLE_CREATE);
		return USERMAST_TABLE_CREATE;
	}


	public String creatDeleteLogTable()
	{
		arrayListForColumns = new ArrayList<String>();
		arrayListForColumns.add(COLUMN_TIME_STAMP + " " + DATA_TYPE_NUMERIC + ",");
		arrayListForColumns.add(COLUMN_ENTITY_NAME + " " + DATA_TYPE_VARCHAR + ",");
		arrayListForColumns.add(COLUMN_ENTITY_ID + " " + DATA_TYPE_VARCHAR + ",");
		arrayListForColumns.add(COLUMN_ID+" "+DATA_TYPE_INTEGER_PRIMARY_KEY);
		DELETE_LOG_TABLE_CREATE=createTableSting(arrayListForColumns,TABLE_DELETE_LOG);
		return DELETE_LOG_TABLE_CREATE;
	}

	public String createSyncStatusTable()
	{
		arrayListForColumns=new ArrayList<String>();
		arrayListForColumns.add("id"+" "+DATA_TYPE_INTEGER_NOT_NULL_PRIMARY_KEY+",");
		arrayListForColumns.add("DisplayName"+" Text,");
		arrayListForColumns.add("LastSyncTime"+ " Text");

		TableSyncCreate=createTableSting(arrayListForColumns,TABLE_SYNC_STATUS);

		return TableSyncCreate;

	}
	@Override
	public void onCreate(SQLiteDatabase database)
	{
		// TODO Auto-generated method stub
		//database.execSQL(creatWriteLogTable());
		database.execSQL(creatDeleteLogTable());
		database.execSQL(createUserTableString());
		database.execSQL(createPartyTableString());
		database.execSQL(createPartyTypeMastTableString());
		database.execSQL(createProductMastTableString());
		database.execSQL(createProductGroupTableString());
		database.execSQL(createDistAreaTableString());
		database.execSQL(createDistributerTableString());
		database.execSQL(createUserAreaTableString());
		database.execSQL(createIndustryTableString());
		database.execSQL(createSubIndustryTableString());
		database.execSQL(createSegmentTableString());
		database.execSQL(createProductClassTableString());
		database.execSQL(createRegionTableString());
		database.execSQL(createDistrictTableString());
		database.execSQL(createBeatTableString());
		database.execSQL(createAreaTableString());
		database.execSQL(createAreaTypeTableString());
		database.execSQL(createCityTableString());
		database.execSQL(createStateTableString());
		database.execSQL(createCountryTableString());
		database.execSQL(createEnviroTableString());
		database.execSQL(createVisitl1TableString());
		database.execSQL(createPorderTableString());
		database.execSQL(createPorder1TableString());
		database.execSQL(createFailedVisitTableString());
		database.execSQL(createTransDemoTableString());
		database.execSQL(createTransDemo1TableString());
		database.execSQL(createOrderTableString());
		database.execSQL(createOrder1TableString());
		database.execSQL(createUserLocationTableString());
		database.execSQL(createPricelistTableString());
		database.execSQL(createSmanTableString());
		database.execSQL(createHistoryTableString());
		database.execSQL(createSynTableString());
		database.execSQL(createPurchaseOrderTableString());
		database.execSQL(createSessionDataTableString());
		database.execSQL(createPdaidByUserTableString());
		database.execSQL(createTransCollectionTableString());
		database.execSQL(createDistributerCollectionTableString());
		database.execSQL(createFailedVisitRemarkMastTableString());
		database.execSQL(createProjecttMastTableString());
		database.execSQL(createTransportMastTableString());
		database.execSQL(createCompetitorTableString());
		database.execSQL(createTransVisitDistTableString());
		database.execSQL(createSchemeMastTableString());
		database.execSQL(createTransLeaveRequestTableString());
		database.execSQL(createTransBeatPlanTableString());
		database.execSQL(createAppDataMastString());
		database.execSQL(createAppEnviroString());
		database.execSQL(createTransportMastString());
		database.execSQL(createVehicleMastString());
		database.execSQL(createVisitCodeMastString());
		database.execSQL(createTransTourPlanHeaderTableString());
		database.execSQL(createTransTourPlanTableString());
		database.execSQL(createDynamicMenuTableString());
		database.execSQL(createSyncStatusTable());
		database.execSQL(createRecrentSearchTable());
		database.execSQL("CREATE INDEX pWebcodeIndex ON mastParty (webcode)");
		database.execSQL("CREATE INDEX pBeatIndex ON mastParty (beat_code)");
		database.execSQL("CREATE INDEX pAreaIndex ON mastParty (area_code)");
		database.execSQL("CREATE INDEX dCityIndex ON mastDristributor (city_code)");
		database.execSQL("CREATE INDEX distStateIndex ON MastDistrict (sync_id)");
		database.execSQL("CREATE INDEX bAreaIndex ON MastBeat (area_code)");
		database.execSQL("CREATE INDEX bTsIndex ON MastBeat (timestamp)");
		database.execSQL("CREATE INDEX aCityIndex ON MastArea (city_code)");
		database.execSQL("CREATE INDEX aTsIndex ON MastArea (timestamp)");
		database.execSQL("CREATE INDEX cdistIndex ON MastCity (district_id)");
		database.execSQL("CREATE INDEX cstateIndex ON MastCity (state_code)");
		database.execSQL("CREATE INDEX cTsIndex ON MastCity (timestamp)");
		database.execSQL("CREATE INDEX visitIndex ON Visitl1 (Android_id)");
		database.execSQL("CREATE INDEX distVisitIndex ON TransVisitDist (Android_id)");
		database.execSQL("CREATE INDEX orderIndex ON TransOrder (Android_id)");
		database.execSQL("CREATE INDEX order1Index ON TransOrder1 (Android_id)");
		database.execSQL("CREATE INDEX demoIndex ON TransDemo (Android_id)");
		database.execSQL("CREATE INDEX compIndex ON TransCompetitor (Android_id)");
		database.execSQL("CREATE INDEX pcollIndex ON TransCollection (Android_id)");
		database.execSQL("CREATE INDEX dcollIndex ON DistributerCollection (Android_id)");
		database.execSQL("CREATE INDEX fvisitIndex ON Trans_Failed_visit (Android_id)");
		database.execSQL("CREATE INDEX itemTemplateIndex ON History (code)");
		database.execSQL("CREATE TABLE IF NOT EXISTS "+ DatabaseConnection.NOTIFIVATION_Data_TABLE+"("+ DatabaseConnection.COLUMN_NOTIFICATION+" "+ DatabaseConnection.DATA_TYPE_INTEGER_NOT_NULL+","+ DatabaseConnection.COLUMN_pro_id+" "+ DatabaseConnection.DATA_TYPE_VARCHAR+","+ DatabaseConnection.COLUMN_userid+" "+ DatabaseConnection.DATA_TYPE_INTEGER_NOT_NULL+","+ DatabaseConnection.COLUMN_TIMESTAMP+" "+ DatabaseConnection.DATA_TYPE_INTEGER_NOT_NULL+","+ DatabaseConnection.COLUMN_url+" "+ DatabaseConnection.DATA_TYPE_VARCHAR+","+ DatabaseConnection.COLUMN_msz+" "+ DatabaseConnection.DATA_TYPE_VARCHAR+","+ DatabaseConnection.COLUMN_STATUS+" "+ DatabaseConnection.DATA_TYPE_VARCHAR+","+ DatabaseConnection.COLUMN_URL_Page+" "+ DatabaseConnection.DATA_TYPE_VARCHAR+","+ DatabaseConnection.COLUMN_Smid+" "+ DatabaseConnection.DATA_TYPE_INTEGER+","+ DatabaseConnection.COLUMN_LVQR+" "+ DatabaseConnection.DATA_TYPE_VARCHAR+")");
		database.execSQL("CREATE TABLE IF NOT EXISTS "+ DatabaseConnection.NOTIFIVATION_TABLE+"("+ DatabaseConnection.COLUMN_NOTIFICATION+" "+ DatabaseConnection.DATA_TYPE_INTEGER_NOT_NULL+",date TIMESTAMP  DEFAULT CURRENT_TIMESTAMP"+")");
		database.execSQL(createDistItemTemplateTableString());
		database.execSQL(createTransDistStockTableString());
		insertInToSyncStatus(database);
	}
	public DatabaseConnection(Context context) {
		// TODO Auto-generated constructor stub
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		Log.d("database", "database successfully created");
	}
	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL(createSyncStatusTable());
		insertInToSyncStatus(database);
		database.execSQL(createRecrentSearchTable());
		}
	
	public String createTableSting(ArrayList<String> arrayListForColumns,String tableName)
	{
		StringBuilder builder=new StringBuilder();
		builder.append("create table"+" "+tableName+" ( ");
		for (int i = 0; i < arrayListForColumns.size(); i++) {
			builder.append(" "+arrayListForColumns.get(i));
		}
		builder.append(" );");
		arrayListForColumns.clear();
		return builder.toString();
		
	}
	public void insertInToSyncStatus(SQLiteDatabase db)
	{
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('System Setting')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('User Master')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Country Master')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Region Master')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('City Master')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('State Master')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Area Master')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Beat Master')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Industry Master')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Prod. Class Master')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Prod.Segment Master')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Prod.Group Master')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Product Master')");

		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Sales Rep. Master')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Transporter Master')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Failed Visit Remark')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Distributor Master')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Customer Master')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Customer Item Template')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Daily Sales Report')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Sales Order')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Demo')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Failed Visit')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Customer Collection')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Distributor Collection')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Distributor Discussion')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Competitor')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Distributor Stock')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Beat Plan')");
		db.execSQL("INSERT INTO "+TABLE_SYNC_STATUS+"(DisplayName) VALUES('Stock Template')");

	}

	public String createRecrentSearchTable()
	{
		arrayListForColumns = new ArrayList<String>();
		arrayListForColumns.add(COLUMN_RECENT_SEARCH_ID+" "+DATA_TYPE_INTEGER_PRIMARY_KEY+ ",");
		arrayListForColumns.add(COLUMN_PARTY_NAME + " " + DATA_TYPE_VARCHAR + ",");
		arrayListForColumns.add(COLUMN_PERSON_NAME + " " + DATA_TYPE_VARCHAR + ",");
		arrayListForColumns.add(COLUMN_MOBILE_NO + " " + DATA_TYPE_VARCHAR + ",");
		arrayListForColumns.add(COLUMN_TIME_STAMP + " " + DATA_TYPE_NUMERIC );
		TableRecentSearch=createTableSting(arrayListForColumns,Table_Recent_Search);
		return TableRecentSearch;
	}
}
