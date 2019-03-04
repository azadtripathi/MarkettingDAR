package com.dm.crmdm_app;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.BitmapCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.controller.AppEnviroDataController;
import com.dm.controller.DemoTransactionController;
import com.dm.controller.EnviroController;
import com.dm.controller.HistoryPartyWiseController;
import com.dm.controller.ProductClassController;
import com.dm.controller.ProductGroupController;
import com.dm.controller.SegmentController;
import com.dm.database.DatabaseConnection;
import com.dm.library.Constant;
import com.dm.library.Custom_Toast;
import com.dm.library.DbCon;
import com.dm.library.IntentSend;
import com.dm.library.LoadActivity;
import com.dm.library.Validation;
import com.dm.model.DemoTransaction;
import com.dm.model.ProductClass;
import com.dm.model.ProductGroup;
import com.dm.model.Segment;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static com.dm.crmdm_app.DemoPreInformation.Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE;

public class DemoPreInformation extends ActionBarActivity {
	SharedPreferences preferences,preferences2;EditText remarks;
DemoTransactionController demoTransactionController;
Spinner classSpinner,segmentSpinner,materialSpinner;
ArrayList<ProductClass> productClassList;ArrayList<Segment> productSegmentList;ArrayList<ProductGroup> materialGroupList;
ProductClassController productClassController;
ArrayAdapter productClassAdapter,segmentAdapter,materialAdapter;SegmentController segmentController;
ProductGroupController productGroupController;EnviroController enviroController;
static String userId;static int img ;Intent intent;Bundle bundle;
ImageView capturedImage;private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
 public static final int MEDIA_TYPE_IMAGE = 1; private Uri fileUri,fileUri1; // file url to store image
 String filePath="NA",filePath1="NA",filePath2="NA";String AndroidDoc_id="0",webDoc_id="0";TextView textdoc,textdocId;RelativeLayout updateLayout;
ImageView save,cancel,delete,findButton,takePicture;	HistoryPartyWiseController historyPartyWiseController;
	LinearLayout lineardocno;LoadActivity loadActivity;
	TextView tv1,tv2, tv3,partynameTextView,visitDate;
	LinearLayout l1,l2,l3;
	String value;
	String[] temp;
	DbCon dbCon;

	AppEnviroDataController appDataController;

	private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
	private Button btnSelect;
	private String userChoosenTask="";Intent data;


	Bitmap pageBitmap;
	String selectedImageBase64ImgString = "N/A";
	DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo_pre_information);

	 options = new DisplayImageOptions.Builder()
			 .showImageOnLoading(0)
			 .showImageForEmptyUri(0)
			 .showImageOnFail(0)
			 .cacheInMemory(true)
			 .cacheOnDisk(true)
			 .build();

		NewLocationService track = new NewLocationService(this);
	 if(track.canGetLocation)
	 {
		 latitude = track.getLatitude();
		 longitude = track.getLongitude();
		 latlngTimeStamp = track.getLatLngTime();
	 }
	 else
	 {
		 latitude = track.getLatitude();
		 longitude = track.getLongitude();
		 latlngTimeStamp = track.getLatLngTime();
	 }
		preferences=getSharedPreferences("RETAILER_SESSION_DATA", MODE_WORLD_READABLE);
		preferences2=getApplicationContext().getSharedPreferences("MyPref", MODE_WORLD_READABLE);
	 getSupportActionBar().setCustomView(R.layout.actionbar_custom_view_home);
	 getSupportActionBar().setDisplayShowTitleEnabled(true);
	 getSupportActionBar().setDisplayShowCustomEnabled(true);
	 getSupportActionBar().setDisplayUseLogoEnabled(true);
	 getSupportActionBar().setDisplayShowHomeEnabled(true);
	 ImageView iv = (ImageView)findViewById(R.id.image);

	 iv.setOnClickListener(new OnClickListener() {
		 @Override
		 public void onClick(View v) {
			 (new IntentSend(getApplicationContext(),DashBoradActivity.class)).toSendAcivity();
			 finish();
		 }
	 });

	 TextView tv = (TextView)findViewById(R.id.text);
	 tv.setText("Demo Entry");
	 setTitle("Demo Entry");
		userId=preferences2.getString("CONPERID_SESSION", "");
	 TextView vdateLabel = (TextView)findViewById(R.id.vdate);
	 vdateLabel.setText(preferences.getString("VISIT_DATE_SESSION", null));
	 lineardocno=(LinearLayout)findViewById(R.id.docnolinear);
	 lineardocno.setVisibility(View.GONE);
		remarks=(EditText)findViewById(R.id.remarkText);
		classSpinner=(Spinner)findViewById(R.id.classspinner);
		segmentSpinner=(Spinner)findViewById(R.id.segmentspinner);
		materialSpinner=(Spinner)findViewById(R.id.materialspinner);
	    updateLayout=(RelativeLayout)findViewById(R.id.updateRelativeLayout);
		capturedImage=(ImageView)findViewById(R.id.imageView1);
		findButton=(ImageView) findViewById(R.id.findbutton1);
		save=(ImageView)findViewById(R.id.button1);
		cancel=(ImageView)findViewById(R.id.button2);
		delete=(ImageView)findViewById(R.id.button3);
	     loadActivity=new LoadActivity(DemoPreInformation.this);
	 dbCon=new DbCon(getApplicationContext());
	 takePicture=(ImageView)findViewById(R.id.takePicture);
	 SharedPreferences dpreferences_new = getSharedPreferences("DYNAMIC_FORM_FIELDS_INFO", MODE_WORLD_READABLE);
	 String image_data = dpreferences_new.getString("Show_UseCamera",null);
	 if(!image_data.equals("Y")){
		 //capturegallery();
		 userChoosenTask = "Choose from Library";
		 takePicture.setBackgroundResource(R.drawable.galaryimage);

	 }
	 else {
		 userChoosenTask = "Take Photo";
		 takePicture.setBackgroundResource(R.drawable.camera);
	 }
	 appDataController=new AppEnviroDataController(getApplicationContext());

	 l1 = (LinearLayout)findViewById(R.id.layout);
	 l2 = (LinearLayout)findViewById(R.id.layout1);
	 l3 = (LinearLayout)findViewById(R.id.layout2);
	 tv1 = (TextView)findViewById(R.id.TextView01);
	 tv2 = (TextView)findViewById(R.id.textView02);
	 tv3 = (TextView)findViewById(R.id.textView1);
	 tv1.setVisibility(View.INVISIBLE);
	 tv2.setVisibility(View.INVISIBLE);
	 tv3.setVisibility(View.INVISIBLE);

	 tv1.setTextColor(Color.parseColor("#FF4081"));
	 partynameTextView=(TextView)findViewById(R.id.partyVisitOnPartyDashboard);
	 setTitle("Book Order   V.D.:"+preferences.getString("VISIT_DATE_SESSION", ""));
	 String name = preferences.getString("PARTY_NAME_SESSION","This is no value");
	 String delimiter ="\n";
	 temp = name.split(delimiter);
	 for(int i =0; i < temp.length ; i++){
		 value = temp[0];
		 temp[1] = null;
		 temp[2] = null;
		 //	Toast.makeText(getApplication().getBaseContext(),temp[i],Toast.LENGTH_SHORT).show();
	 }
	 partynameTextView.setText(value);
		intent = getIntent();
		bundle=intent.getExtras();
		if(bundle!=null)
		{
			AndroidDoc_id=(bundle.getString("ANDROIDID")==null?"0":bundle.getString("ANDROIDID"));
			webDoc_id=(bundle.getString("WEBDOCID")==null?"0":bundle.getString("WEBDOCID"));
			//capturedImage.setImageBitmap(d);
			
		}
		  findButton.setOnClickListener(new OnClickListener() {

			   @Override
			   public void onClick(View v) {
				   (new IntentSend(getApplicationContext(),DemoPreInformation.class)).toSendAcivity();
				   finish();
			   }
			  });
		
		save.setOnClickListener(new OnClickListener() {

			   @Override
			   public void onClick(View v) {
				savedata();
			   
			   }
			  });
		
		cancel.setOnClickListener(new OnClickListener() {

			   @Override
			   public void onClick(View v) {
				clearFields();
			   
			   }
			  });

/**************		Edited by Sandeep Singh 14-03-17*************/
	 /**********	Start	**************/
	 delete.setOnClickListener(new OnClickListener() {
		 @Override
		 public void onClick(View view) {
			 AlertDialog.Builder builder = new AlertDialog.Builder(DemoPreInformation.this);
			 builder.setMessage("Are you sure you want to Delete")
					 .setCancelable(false)
					 .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
						 public void onClick(DialogInterface dialog, int aid) {
							 deleteDemo();
						 }
					 })
					 .setNegativeButton("No", new DialogInterface.OnClickListener() {
						 public void onClick(DialogInterface dialog, int id) {
							 dialog.cancel();
						 }
					 });
			 AlertDialog alert = builder.create();
			 alert.show();

		 }
	 });

		historyPartyWiseController=new HistoryPartyWiseController(DemoPreInformation.this);
		productClassController=new ProductClassController(DemoPreInformation.this);
		demoTransactionController=new DemoTransactionController(DemoPreInformation.this);
		productClassController.open();
		productClassList=productClassController.getProductClassList();
		productClassController.close();
		segmentController=new SegmentController(DemoPreInformation.this);
		segmentController.open();
		productSegmentList=segmentController.getSegmentList();
		segmentController.close();
		productGroupController=new ProductGroupController(DemoPreInformation.this);
		productGroupController.open();
		materialGroupList=productGroupController.getProductGroupList();
		productGroupController.close();
//		demoTransactionController.open();
//		 AndroidDoc_id=demoTransactionController.getAndroidDocId(preferences.getString("PARTY_CODE_SESSION", null),preferences.getString("VISIT_DATE_SESSION", ""),preferences.getString("ANDROID_PARTY_CODE_SESSION", null));
//		demoTransactionController.close();
 		  if(AndroidDoc_id.equals("0"))
		  {	
//			  textdoc.setVisibility(View.GONE);
//			  textdocId.setVisibility(View.GONE);
			 //  save.setText("Save");
			  save.setImageResource(R.drawable.save1);
			  delete.setVisibility(View.GONE);
			  ArrayList<String> ArraySegmentList,ArrayProductGroupList,ArrayClassList;
				ArrayClassList=new ArrayList<String>();
			  /******************************************* Ashutosh worked by 31/03/17 Start ********************************/

			  ArrayClassList.add("--Select--");
			  for(int i=0;i<productClassList.size();i++)
			  {
				  ArrayClassList.add(productClassList.get(i).getClass_name());

			  }
			  productClassAdapter=new ArrayAdapter<String>(this, R.layout.adapterdropdown, ArrayClassList);
			  productClassAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
			  classSpinner.setAdapter(productClassAdapter);
			  classSpinner.setSelection(0);
			  tv1.setTextColor(Color.parseColor("#FF4081"));
			  l1.setBackgroundColor(Color.parseColor("#FF4081"));

			  ArraySegmentList=new ArrayList<String>();
			  ArraySegmentList.add("--Select--");
			  for(int i=0;i<productSegmentList.size();i++)
			  {
				  ArraySegmentList.add(productSegmentList.get(i).getSegment_name());

			  }
			  segmentAdapter=new ArrayAdapter<String>(this, R.layout.adapterdropdown, ArraySegmentList);
			  segmentAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
			  segmentSpinner.setAdapter(segmentAdapter);
			  segmentSpinner.setSelection(0);
			  ArrayProductGroupList=new ArrayList<String>();
			  ArrayProductGroupList.add("--Select--");
			  for(int i=0;i<materialGroupList.size();i++)
			  {
				  ArrayProductGroupList.add(materialGroupList.get(i).getGroup_name());

			  }
			  materialAdapter=new ArrayAdapter<String>(this, R.layout.adapterdropdown, ArrayProductGroupList);
			  materialAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
			  materialSpinner.setAdapter(materialAdapter);
			  materialSpinner.setSelection(0);
			  /******************************************* Ashutosh worked by 31/03/17 End ********************************/
			  dbCon.open();
			  save.setEnabled(dbCon.ButtonEnable("DemoPreInformation","Party","Add"));
			  if(!dbCon.ButtonEnable("DemoPreInformation","Party","Add"))
			  {
				  save.setColorFilter(Color.parseColor("#808080"));
			  }
			  dbCon.close();

		    }
		  else{
			  TextView docid=(TextView)findViewById(R.id.dpidocno);
			  docid.setText(AndroidDoc_id);
			  lineardocno.setVisibility(View.VISIBLE);
			  demoTransactionController.open();
			  DemoTransaction demo=demoTransactionController.getDemoByAndroidId(AndroidDoc_id);
			  demoTransactionController.close();
			  filePath=demo.getFilePath();
//			  textdoc.setVisibility(View.VISIBLE);
//			  textdocId.setVisibility(View.VISIBLE);
//			  textdocId.setText(demo.getAndroid_id());
			//  save.setText("Update");
			  save.setImageResource(R.drawable.update);
			  delete.setVisibility(View.VISIBLE);
//			  if(demo.getIsUpload().equals("1"))
//			  {
//				  save.setEnabled(false);
//				  delete.setEnabled(false);
//				 /* save.setBackgroundColor(Color.parseColor("#7fb2ff"));
//				  delete.setBackgroundColor(Color.parseColor("#7fb2ff"));*/
//				  save.setColorFilter(Color.parseColor("#88888888"));
//				  delete.setColorFilter(Color.parseColor("#88888888"));
//			  }
//			  else
//			  {
//				  save.setEnabled(true);
//				  delete.setEnabled(true);
//			  }

			  if (preferences.getString("DSR_LOCK_VALUE","").equals("1")) {
				  save.setEnabled(false);
				  delete.setEnabled(false);
				  save.setColorFilter(Color.parseColor("#808080"));
				  delete.setColorFilter(Color.parseColor("#808080"));
			  } else {
				  save.setEnabled(true);
				  delete.setEnabled(true);
			  }

			 int selectIndex=0;
			ArrayList<String> ArrayClassList=new ArrayList<String>();
			  /******************************************* Ashutosh worked by 31/03/17 Start ********************************/

			  ArrayClassList.add("--Select--");
			  for(int i=0;i<productClassList.size();i++)
			  {
				  ProductClass pclass=new ProductClass();
				  pclass=productClassList.get(i);
				  if(pclass.getClass_id().equals(demo.getProductClassId()))
				  {
					  selectIndex=i;
				  }
				  ArrayClassList.add(pclass.getClass_name());

			  }
			  productClassAdapter=new ArrayAdapter<String>(this, R.layout.simple_spinner_item, ArrayClassList);
			  productClassAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
			  classSpinner.setAdapter(productClassAdapter);
			  if(demo.getProductClassId().equals("00"))
			  {
				  classSpinner.setSelection(0);
				  tv1.setTextColor(Color.parseColor("#FF4081"));
				  l1.setBackgroundColor(Color.parseColor("#FF4081"));
			  }
			  else{
				  classSpinner.setSelection(selectIndex+1);
			  }

			  selectIndex=0;
			  ArrayList<String> ArraySegmentList=new ArrayList<String>();
			  ArraySegmentList.add("--Select--");
			  for(int i=0;i<productSegmentList.size();i++)
			  {
				  Segment segment=new Segment();
				  segment=productSegmentList.get(i);
				  if(segment.getSegment_id().equals(demo.getProductSegmentId()))
				  {
					  selectIndex=i;
				  }
				  ArraySegmentList.add(segment.getSegment_name());

			  }
			  segmentAdapter=new ArrayAdapter<String>(this, R.layout.simple_spinner_item, ArraySegmentList);
			  segmentAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
			  segmentSpinner.setAdapter(segmentAdapter);
			  if(demo.getProductSegmentId().equals("00"))
			  {
				  segmentSpinner.setSelection(0);
			  }
			  else{
				  segmentSpinner.setSelection(selectIndex+1);
			  }

			  selectIndex=0;
			  ArrayList<String> ArrayProductGroupList=new ArrayList<String>();
			  ArrayProductGroupList.add("--Select--");
			  /******************************************* Ashutosh worked by 31/03/17 End ********************************/

			  for(int i=0;i<materialGroupList.size();i++)
				{
					ProductGroup productGroup=new ProductGroup();
					productGroup=materialGroupList.get(i);
					if(productGroup.getGroup_id().equals(demo.getProductMatGrp()))
					{
						selectIndex=i;
					}
					ArrayProductGroupList.add(productGroup.getGroup_name());
					
				}
				materialAdapter=new ArrayAdapter<String>(this, R.layout.simple_spinner_item, ArrayProductGroupList);
				materialAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
				materialSpinner.setAdapter(materialAdapter);
				if(demo.getProductMatGrp().equals("00"))
				{
					materialSpinner.setSelection(0);
				}
				else{
					materialSpinner.setSelection(selectIndex+1);
				}
				
				remarks.setText(demo.getRemarks());

			/*  SharedPreferences dpreferences_new = getSharedPreferences("DYNAMIC_FORM_FIELDS_INFO", MODE_WORLD_READABLE);
			  String image_data = dpreferences_new.getString("Show_UseCamera",null);*/
			 // Toast.makeText(getApplication().getBaseContext(),image_data,Toast.LENGTH_SHORT).show();
//			  if(!image_data.equals("Y")){
//
//				  Bitmap bm = null;
//				  int maxHeight = 500;
//				  int maxWidth = 500;
//				  filePath1 = demo.getFilePath();
//				  try{
//					  bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), Uri.parse(demo.getFilePath()));
//
//				  }  catch (IOException e) {
//					  e.printStackTrace();
//				  }
//				  try {
//
//					  float scale = Math.min(((float) maxHeight / bm.getWidth()), ((float) maxWidth / bm.getHeight()));
//					  Matrix matrix = new Matrix();
//					  matrix.postScale(scale, scale);
//					  bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
//
//					  int bitmapByteCount = BitmapCompat.getAllocationByteCount(bm);
//					  if (bitmapByteCount <= 1048576) {
//						  capturedImage.setImageBitmap(bm);
//					  }
//					  else {
//						  new Custom_Toast(getApplicationContext(),"Please upload image of upto 1mb size only",R.drawable.update).showCustomAlert();
//					  }
//				  }
//				  catch(Exception e)
//				  {}
//			  }
//			  else {
				  	filePath1 = demo.getFilePath();
			  if(filePath1.contains("http"))
			  {
				  selectedImageBase64ImgString = filePath1;
				  ImageLoader.getInstance().displayImage(selectedImageBase64ImgString, capturedImage, options);
			  }
			  else
			  {
				  selectedImageBase64ImgString = filePath1;
				  byte[] decodedString = Base64.decode(filePath1, Base64.DEFAULT);
				  Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
				  BitmapFactory.Options options = new BitmapFactory.Options();
				  options.inSampleSize = 8;
				  capturedImage.setImageBitmap(loadActivity.fitImage(decodedByte));
			  }

				/*final Bitmap bitmap = BitmapFactory.decodeFile(demo.getFilePath());
                System.out.println("demo"+demo.getFilePath());*/

//			  }

			  dbCon.open();
			  save.setEnabled(dbCon.ButtonEnable("DemoPreInformation","Party","Edit"));
			  delete.setEnabled(dbCon.ButtonEnable("DemoPreInformation","Party","Delete"));
			  if(!dbCon.ButtonEnable("DemoPreInformation","Party","Edit"))
			  {
				  save.setColorFilter(Color.parseColor("#808080"));
			  }
			  if(!dbCon.ButtonEnable("DemoPreInformation","Party","Delete"))
			  {
				  delete.setColorFilter(Color.parseColor("#808080"));
			  }

			  dbCon.close();
		  }
	 classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		 @Override
		 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			 String jk=tv1.getText().toString();
//			 tv1.setTextColor(Color.parseColor("#FF4081"));
//			 l1.setBackgroundColor(Color.parseColor("#FF4081"));
			 if(!jk.equalsIgnoreCase("")){
//				 Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
				 tv1.setVisibility(View.VISIBLE);
//				 tv1.startAnimation(slide_up);


//				 tv2.setTextColor(Color.parseColor("#FF808080"));
//				 l2.setBackgroundColor(Color.parseColor("#666666"));
//				 if(position != 0)
//				 tv3.setTextColor(Color.parseColor("#FF808080"));
//				 l2.setBackgroundColor(Color.parseColor("#666666"));


			 }
			 tv1.setText("Product Class:");
		 }

		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {

		 }
	 });
	 segmentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		 @Override
		 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			 String jk=tv2.getText().toString();
			 if(!jk.equalsIgnoreCase("")){
				// Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
				 tv2.setVisibility(View.VISIBLE);
				// tv2.startAnimation(slide_up);
				// tv2.setTextColor(Color.parseColor("#FF4081"));
				// l2.setBackgroundColor(Color.parseColor("#FF4081"));

				// tv1.setTextColor(Color.parseColor("#FF808080"));
				 //l1.setBackgroundColor(Color.parseColor("#666666"));
				 /*if(position != 0) {
					 tv3.setTextColor(Color.parseColor("#FF808080"));
					 l3.setBackgroundColor(Color.parseColor("#666666"));
				 }*/


			 }
			 tv2.setText("Product Segment:");
		 }

		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {

		 }
	 });
	 materialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
		 @Override
		 public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
			 String jk=tv3.getText().toString();
			 if(!jk.equalsIgnoreCase("")){
				 Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
				 tv3.setVisibility(View.VISIBLE);
				 tv3.startAnimation(slide_up);
				/* if(position != 0) {
					 tv3.setTextColor(Color.parseColor("#FF4081"));
					 l3.setBackgroundColor(Color.parseColor("#FF4081"));
				 }*/

				 tv1.setTextColor(Color.parseColor("#FF808080"));
				 l1.setBackgroundColor(Color.parseColor("#666666"));
				 tv2.setTextColor(Color.parseColor("#FF808080"));
				 l2.setBackgroundColor(Color.parseColor("#666666"));


			 }
			 tv3.setText("Product Group:");
		 }

		 @Override
		 public void onNothingSelected(AdapterView<?> parent) {

		 }
	 });
	 remarks.setOnTouchListener(new View.OnTouchListener() {
		 @Override
		 public boolean onTouch(View v, MotionEvent event) {
			 tv1.setTextColor(Color.parseColor("#FF808080"));
			 l1.setBackgroundColor(Color.parseColor("#666666"));
			 tv2.setTextColor(Color.parseColor("#FF808080"));
			 l2.setBackgroundColor(Color.parseColor("#666666"));
			 tv3.setTextColor(Color.parseColor("#FF808080"));
			 l3.setBackgroundColor(Color.parseColor("#666666"));
			 return false;
		 }
	 });
	 takePicture.setOnClickListener(new OnClickListener() {
		 @Override
		 public void onClick(View v) {
			 SharedPreferences dpreferences_new = getSharedPreferences("DYNAMIC_FORM_FIELDS_INFO", MODE_WORLD_READABLE);
			 String image_data = dpreferences_new.getString("Show_UseCamera",null);
			 	//Toast.makeText(getApplication().getBaseContext(),image_data,Toast.LENGTH_SHORT).show();
			 if(!image_data.equals("Y")){
				 //capturegallery();
				 galleryIntent();
			 }
			else {
				 cameraIntent();
			 }
			 //captureImage();
		 }
	 });
	 if(!remarks.getText().toString().isEmpty()){
		 tv1.setText("Product Class:");
		 tv2.setText("Product Segment:");
		 tv3.setText("Product Group:");
		 tv1.setTextColor(Color.parseColor("#FF808080"));
		 l1.setBackgroundColor(Color.parseColor("#666666"));
		 tv2.setTextColor(Color.parseColor("#FF808080"));
		 l2.setBackgroundColor(Color.parseColor("#666666"));
		 tv3.setTextColor(Color.parseColor("#FF808080"));
		 l3.setBackgroundColor(Color.parseColor("#666666"));
		 return;
	 }


	 tv1.setTextColor(Color.parseColor("#FF4081"));
	 l1.setBackgroundColor(Color.parseColor("#FF4081"));
 }
	
	/*public void onCameraClick(View view)
	{
	
		captureImage();
		
	}*/
	/**
     * Launching camera app to capture image
     */
    /*private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
 
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        System.out.println("fileUri="+fileUri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE); 
    }*/

	/*private void capturegallery(){
		selectImage();
	}
	private void selectImage() {
		final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
		AlertDialog.Builder builder = new AlertDialog.Builder(DemoPreInformation.this);
		builder.setTitle("Add Photo!");

		builder.setItems(items, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int item) {
				boolean result = Utility.checkPermission(DemoPreInformation.this);
				if (items[item].equals("Take Photo")) {
					userChoosenTask = "Take Photo";
					if (result)
						cameraIntent();
				} else if (items[item].equals("Choose from Library")) {
					userChoosenTask = "Choose from Library";
					if (result)
						galleryIntent();
				} else if (items[item].equals("Cancel")) {
					dialog.dismiss();
				}
			}
		});
		builder.show();
	}*/

	public static class Utility {
		public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
		@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		public static boolean checkPermission(final Context context) {
			int currentAPIVersion = Build.VERSION.SDK_INT;
			if (currentAPIVersion >= Build.VERSION_CODES.M) {
				if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
					if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
						AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
						alertBuilder.setCancelable(true);
						alertBuilder.setTitle("Permission necessary");
						alertBuilder.setMessage("External storage permission is necessary");
						alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
							}
						});
						AlertDialog alert = alertBuilder.create();
						alert.show();
					} else {
						ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
					}
					return false;
				} else {
					return true;
				}
			} else {
				return true;
			}

		}
	}
	private void cameraIntent() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

	//	fileUri1 = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
	//	System.out.println("fileUri="+fileUri1);
	//	intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);
		startActivityForResult(intent, REQUEST_CAMERA);
	}

	private void galleryIntent() {
		if (Build.VERSION.SDK_INT >= 23){
			if (ContextCompat.checkSelfPermission(DemoPreInformation.this, Manifest.permission.READ_EXTERNAL_STORAGE)
					!= PackageManager.PERMISSION_GRANTED) {

				if (ActivityCompat.shouldShowRequestPermissionRationale(DemoPreInformation.this,
						Manifest.permission.READ_EXTERNAL_STORAGE)) {

				} else {

					ActivityCompat.requestPermissions(DemoPreInformation.this,
							new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
							MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
				}
			}else{
				/*ActivityCompat.requestPermissions(DemoPreInformation.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
						MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

*/
//				intent.setAction(Intent.ACTION_PICK);

				/*if (Build.VERSION.SDK_INT <19){
					 intent = new Intent();
					intent.setType("image*//*");
					intent.setAction(Intent.ACTION_GET_CONTENT);
				}else{
					 intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
					intent.addCategory(Intent.CATEGORY_OPENABLE);
					intent.setType("image*//*");
				}
				fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				System.out.println("fileUri="+fileUri);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
				startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
*/

				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, SELECT_FILE);

				//Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				//startActivityForResult(Intent.createChooser(i, "Select File"), SELECT_FILE);

			}
		}
		else {
//			Intent intent = new Intent();
//			intent.setType("image/*");
//			intent.setAction(Intent.ACTION_GET_CONTENT);

			/*if (Build.VERSION.SDK_INT <19){
				intent = new Intent();
				intent.setType("image*//*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
			}else{
				intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image*//*");
			}
			fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
			System.out.println("fileUri="+fileUri);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
			startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);*/

			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent, SELECT_FILE);
		}
	}
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		switch (requestCode) {
			case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if (userChoosenTask.equals("Take Photo"))
						cameraIntent();
					else if (userChoosenTask.equals("Choose from Library"))
						galleryIntent();
				} else {
				}
				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == SELECT_FILE) {
				onSelectFromGalleryResult(data);
//				filePath=fileUri.getPath();
			}
			else if (requestCode == REQUEST_CAMERA) {
				onCaptureImageResult(data);
				// filePath = fileUri1.getPath();
			}
		}
	}



	//************ Start Work Vinayak on 23 April 2017
	@SuppressWarnings("deprecation")
	private void onSelectFromGalleryResult(Intent data) {
		this.data=data;
		if(data != null)
		{
			int maxHeight = 500;
			int maxWidth = 500;
			try {
				final Uri imageUri = data.getData();
				final InputStream imageStream = getContentResolver().openInputStream(imageUri);
				Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
				float scale = Math.min(((float)maxHeight / selectedImage.getWidth()), ((float)maxWidth / selectedImage.getHeight()));
				Matrix matrix = new Matrix();
				matrix.postScale(scale, scale);
				selectedImage = Bitmap.createBitmap(selectedImage, 0, 0, selectedImage.getWidth(), selectedImage.getHeight(), matrix, true);

				int bitmapByteCount= BitmapCompat.getAllocationByteCount(selectedImage);
				if(bitmapByteCount <=  1048576){
					//if(bitmapByteCount <=  21233664){
//			filePath1=data.getData().toString();
//			System.out.println(filePath1);
					capturedImage.setImageBitmap(selectedImage);
					pageBitmap = selectedImage;
					selectedImageBase64ImgString =  encodeImage(pageBitmap);
					selectedImage = null;

					//return false;
				}
				else {
					new Custom_Toast(getApplicationContext(),"Please upload image of upto 1mb size only").showCustomAlert();
				}
//				image_view.setImageBitmap(selectedImage);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
			}

		}else {
			Toast.makeText(this, "You haven't picked Image",Toast.LENGTH_LONG).show();
		}


	}

	private String encodeImage(Bitmap bm)
	{
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
		byte[] b = baos.toByteArray();
		String encImage = Base64.encodeToString(b, Base64.DEFAULT);

		return encImage;
	}
	/*@SuppressWarnings("deprecation")
	private void onSelectFromGalleryResult(Intent data) {
		this.data=data;
		Bitmap bm = null;

		int maxHeight = 500;
		int maxWidth = 500;

		if (data != null) {
			try {
				bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		float scale = Math.min(((float)maxHeight / bm.getWidth()), ((float)maxWidth / bm.getHeight()));
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);

		int bitmapByteCount= BitmapCompat.getAllocationByteCount(bm);
		if(bitmapByteCount <=  1048576){
			//if(bitmapByteCount <=  21233664){
//			*****************copy image code********************
//			File dir = new File(
//				Environment.getExternalStorageDirectory(),
//				"DmCRM");
//
//		if (!dir.exists())
//			dir.mkdirs();
//
//		Date date = new Date() ;
//		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
//		File f = new File(dir.getPath()+"/"+"DmCrm" + timeStamp + ".jpg");
////			File f = new File(imagepath);
//			if (!f.exists())
//			{
//				try {
//					f.createNewFile();
//					loadActivity.copyFile(new File(loadActivity.getRealPathFromURI(data.getData(),DemoPreInformation.this)),f);
////					copyFile(new File(getRealPathFromURI(data.getData())),f);
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
////			***************************** End**********************
////			filePath1=data.getData().toString();
//			filePath1=f.getPath();
//			System.out.println(filePath1);
			capturedImage.setImageBitmap(bm);
			//return false;
		}
		else {
			new Custom_Toast(getApplicationContext(),"Please upload image of upto 1mb size only").showCustomAlert();
		}
	}*/
	private void onCaptureImageResult(Intent data) {
		Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

		int maxHeight = 500;
		int maxWidth = 500;
		float scale = Math.min(((float)maxHeight / thumbnail.getWidth()), ((float)maxWidth / thumbnail.getHeight()));
		Matrix matrix = new Matrix();
		matrix.postScale(scale, scale);
		thumbnail = Bitmap.createBitmap(thumbnail, 0, 0, thumbnail.getWidth(), thumbnail.getHeight(), matrix, true);

		int bitmapByteCount= BitmapCompat.getAllocationByteCount(thumbnail);
		if(bitmapByteCount <=  1048576){
			capturedImage.setImageBitmap(thumbnail);
		}
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
		File destination = new File(Environment.getExternalStorageDirectory(),
				System.currentTimeMillis() + ".jpg");
		FileOutputStream fo;
		try {
			destination.createNewFile();
			fo = new FileOutputStream(destination);
			fo.write(bytes.toByteArray());
			fo.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		//filePath1=data.getData().toString();
		filePath1= destination.getPath().toString();
		System.out.println(filePath1);
		capturedImage.setImageBitmap(thumbnail);
		pageBitmap = thumbnail;
		selectedImageBase64ImgString =  encodeImage(pageBitmap);
		//filePath = fileUri.getPath();
	}
	/**
     * ------------ Helper Methods ---------------------- 
     * */
 
    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    /**
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
				Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                        Constant.IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                System.out.println("Oops! Failed create "
                        + Constant.IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
//                Locale.getDefault()).format(new Date());
        Calendar calendar=Calendar.getInstance();
        String timeStamp = userId+calendar.get(Calendar.YEAR)+
				calendar.get(Calendar.MONTH)+calendar.get(Calendar.DATE)+
				calendar.get(Calendar.HOUR)+calendar.get(Calendar.MINUTE)+
				calendar.get(Calendar.SECOND);
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + timeStamp + ".jpg");
            System.out.println("mediaFile="+mediaFile);
        } else {
            return null;
        }

        return mediaFile;
    }
    
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
            	 filePath=fileUri.getPath();
            	BitmapFactory.Options options = new BitmapFactory.Options();
            	options.inSampleSize = 8;
            	final Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
            	capturedImage.setImageBitmap(bitmap);
            	
            	// successfully captured the image
			} else if (resultCode == RESULT_CANCELED) {
                
            	// user cancelled Image capture
                Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
		}
    }*/












	String latitude,longitude;
	String latlngTimeStamp;

	public void savedata() {

		/******************************************* Ashutosh worked by 31/03/17 Start ********************************/
		if(classSpinner.getSelectedItem().equals("--Select--") && segmentSpinner.getSelectedItem().equals("--Select--") && materialSpinner.getSelectedItem().equals("--Select--"))
/******************************************* Ashutosh worked by 31/03/17 End ********************************/
		 {
			 new Custom_Toast(getApplicationContext(), "Select at least one Segment/Product Group/Class").showCustomAlert();
		 }
		 else if(remarks.getText().toString().isEmpty())
		 {
			 new Custom_Toast(getApplicationContext(), "Please enter the Remark").showCustomAlert();
		 }
//		 else if(capturedImage.getDrawable()==null)
//		 {
//			 new Custom_Toast(getApplicationContext(), "Please Take Picture").showCustomAlert();
//		 }
		 else 
		 {
			    demoTransactionController=new DemoTransactionController(DemoPreInformation.this);
				demoTransactionController.open();
				//String AndroidDoc_id=demoTransactionController.getAndroidDocId(preferences.getString("PARTY_CODE_SESSION", null),preferences.getString("VISIT_DATE_SESSION", ""),preferences.getString("ANDROID_PARTY_CODE_SESSION", null));
				DemoTransaction demoTransaction=new DemoTransaction();
				demoTransaction.setVDate(preferences.getString("VISIT_DATE_SESSION", ""));
				demoTransaction.setUserId(preferences2.getString("USER_ID", null));
				demoTransaction.setSMId(preferences2.getString("CONPERID_SESSION", null));
				demoTransaction.setPartyId(preferences.getString("PARTY_CODE_SESSION", null));
				demoTransaction.setAndPartyId(preferences.getString("ANDROID_PARTY_CODE_SESSION", null));
				demoTransaction.setAreaId(preferences.getString("AREA_CODE_SESSION", ""));

			 	demoTransaction.setLatlngTimeStamp(latlngTimeStamp);
			 demoTransaction.setLongitude(longitude);
			 demoTransaction.setLatitude(latitude);

			 /****************** Edited By Sandeep Singh 20-03-17 ***************/
			 /************		 Start		*************/
			 Validation validation=new Validation(DemoPreInformation.this);
			 demoTransaction.setRemarks(validation.vAlfNum(remarks.getText().toString()).trim());
			 /************		 END		*************/

			 /****************** Commented By Sandeep Singh 20-03-17 ***************/
			 		/************		 Start		*************/
/*			 demoTransaction.setRemarks(remarks.getText().toString());*/
			 /************		 END		*************/
//				demoTransaction.setFilePath(filePath);

//			 ************************************Swati Image Code on 7-Apr-17***************************
if(data!=null) {
	File dir = new File(
			Environment.getExternalStorageDirectory(),
			"DmCRM");

	if (!dir.exists())
		dir.mkdirs();

	Date date = new Date();
	String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
	File f = new File(dir.getPath() + "/" + userId+"A_D_" + timeStamp + ".jpg");
//			File f = new File(imagepath);
	if (!f.exists()) {
		try {
			f.createNewFile();
			loadActivity.copyFile(new File(loadActivity.getRealPathFromURI(data.getData(), DemoPreInformation.this)), f);
//					copyFile(new File(getRealPathFromURI(data.getData())),f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	filePath1 = f.getPath();
	System.out.println(selectedImageBase64ImgString);
	demoTransaction.setFilePath(selectedImageBase64ImgString);

}
			 demoTransaction.setFilePath(selectedImageBase64ImgString);
			 /******************************************* Ashutosh worked by 31/03/17 Start ********************************/

			 if(classSpinner.getSelectedItem().toString().equals("--Select--"))
			 {
				 demoTransaction.setProductClassId("00");
			 }
			 else{
				 demoTransaction.setProductClassId(productClassList.get(classSpinner.getSelectedItemPosition()-1).getClass_id());
			 }
			 if(segmentSpinner.getSelectedItem().toString().equals("--Select--"))
			 {demoTransaction.setProductSegmentId("00");
			 }
			 else{
				 demoTransaction.setProductSegmentId(productSegmentList.get(segmentSpinner.getSelectedItemPosition()-1).getSegment_id());
			 }
			 if(materialSpinner.getSelectedItem().toString().equals("--Select--"))
			 {demoTransaction.setProductMatGrp("00");
			 }
			 /******************************************* Ashutosh worked by 31/03/17 End ********************************/
				else{
				demoTransaction.setProductMatGrp(materialGroupList.get(materialSpinner.getSelectedItemPosition()-1).getGroup_id());
				}
			 if(AndroidDoc_id.equals("0"))
		 {
			 enviroController=new EnviroController(getApplicationContext());
			 enviroController.open();
			 int maxDoc_id=enviroController.getMaxDemo_no();
			  enviroController.close();

			 String androidDemoNo=preferences2.getString("CONPERID_SESSION", null)+" "+" "+String.format("%08d",++maxDoc_id);


			 boolean isAndroidIdExist = demoTransactionController.isAndroidIdExist(androidDemoNo);
			 while (isAndroidIdExist)
			 {
				 maxDoc_id = maxDoc_id+1;
				 androidDemoNo=preferences2.getString("CONPERID_SESSION", null)+" "+" "+String.format("%08d",++maxDoc_id);
				 isAndroidIdExist = demoTransactionController.isAndroidIdExist(androidDemoNo);
			 }
		 demoTransaction.setAndroid_id(androidDemoNo);
		 demoTransactionController.insertdemoTransaction(demoTransaction);
		 demoTransactionController.close(); 
		 enviroController.open();
		 enviroController.updateEnviroDemo(maxDoc_id);
		 enviroController.close();
		 new Custom_Toast(getApplicationContext(),"Record saved successfully", R.drawable.save1).showCustomAlert();
			 dbCon.open();
			 ContentValues cv = new ContentValues();
			 cv.put(DatabaseConnection.COLUMN_TIMESTAMP,"0");
			 dbCon.update(DatabaseConnection.TABLE_VISITL1,cv,"visit_date='"+demoTransaction.getVDate()+"'");
			 dbCon.close();
			 clearFields();
		// new IntentSend(DemoPreInformation.this, RetailerDashboard.class).toSendAcivity();
		 }
				else{
				 	demoTransaction.setFilePath(selectedImageBase64ImgString);
					demoTransaction.setAndroid_id(AndroidDoc_id);
					 demoTransactionController.updateDemoTransaction(demoTransaction);
					 demoTransactionController.close(); 
					 new Custom_Toast(getApplicationContext(),"Record updated successfully", R.drawable.update).showCustomAlert();
				 dbCon.open();
				 ContentValues cv = new ContentValues();
				 cv.put(DatabaseConnection.COLUMN_TIMESTAMP,"0");
				 dbCon.update(DatabaseConnection.TABLE_VISITL1,cv,"visit_date='"+demoTransaction.getVDate()+"'");
				 dbCon.close();
				 clearFields();
					 //new IntentSend(DemoPreInformation.this, RetailerDashboard.class).toSendAcivity();
				}
				
				}
		
	}
	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.demo_pre_information, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		 case R.id.home:
			 new IntentSend(getApplicationContext(), DashBoradActivity.class).toSendAcivity();
		      break;
		
         case R.id.sum:
//			 (new IntentSend(getApplicationContext(),ReportSummary.class)).toSendAcivity();
		     break;

		default:
			break;
		}
		// TODO Auto-generated method stub
		return super.onOptionsItemSelected(item);
	}
*/
	
	public void clearFields() 
	{
		selectedImageBase64ImgString="N/A";
		classSpinner.setSelection(0);
		materialSpinner.setSelection(0);
		segmentSpinner.setSelection(0);
		remarks.setText("");
		AndroidDoc_id="0";
		capturedImage.setImageBitmap(null);
		filePath1="NA";
		//save.setText("Save");
		save.setImageResource(R.drawable.save1);
		save.setEnabled(true);
		save.setClickable(true);
		save.setColorFilter(Color.parseColor("#FFFFFF"));
		delete.setVisibility(View.GONE);
		/*tv1.setVisibility(View.INVISIBLE);
		tv2.setVisibility(View.INVISIBLE);
		tv3.setVisibility(View.INVISIBLE);*/
		lineardocno.setVisibility(View.GONE);

		data = null;
		dbCon.open();
		save.setEnabled(dbCon.ButtonEnable("DemoPreInformation","Party","Add"));
		if(!dbCon.ButtonEnable("DemoPreInformation","Party","Add"))
		{
			save.setColorFilter(Color.parseColor("#808080"));
		}
		dbCon.close();
	}
	
public void deleteDemo()
{
	int i=0;
		if(!AndroidDoc_id.equals("0"))
		{
			demoTransactionController.open();
			DemoTransaction demo=demoTransactionController.getDemoByAndroidId(AndroidDoc_id);
			demoTransactionController.close();
			if(demo.getWebDocId() != null || !demo.getWebDocId().isEmpty())
			{
				ContentValues cv = new ContentValues();
				cv.put(DatabaseConnection.COLUMN_ENTITY_ID,demo.getWebDocId());
				cv.put(DatabaseConnection.COLUMN_ENTITY_NAME,"Demo");
				cv.put(DatabaseConnection.COLUMN_TIME_STAMP,0);

				DbCon dbCon = new DbCon(this);
				dbCon.open();
				dbCon.insert(DatabaseConnection.TABLE_DELETE_LOG,cv);
				dbCon.close();
			}

			historyPartyWiseController.open();
			i=historyPartyWiseController.deleteTransactionRecord(AndroidDoc_id, DatabaseConnection.TABLE_TRANSDEMO);
			historyPartyWiseController.close();
			if(i>0)
			{
				 new Custom_Toast(getApplicationContext(),"Record deleted successfully", R.drawable.save1).showCustomAlert();
				clearFields();	
			}
			
			
		}
       }
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		finish();
	}



	private void copyFile(File sourceFile, File destFile) throws IOException {
		if (!sourceFile.exists()) {
			return;
		}

		FileChannel source = null;
		FileChannel destination = null;
		source = new FileInputStream(sourceFile).getChannel();
		destination = new FileOutputStream(destFile).getChannel();
		if (destination != null && source != null) {
			destination.transferFrom(source, 0, source.size());
		}
		if (source != null) {
			source.close();
		}
		if (destination != null) {
			destination.close();
		}
	}
	private String getRealPathFromURI(Uri contentUri) {

		String[] proj = { MediaStore.Video.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}


	}

