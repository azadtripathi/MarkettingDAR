package com.dm.crmdm_app;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.dm.controller.EnviroController;
import com.dm.controller.SmanController;
import com.dm.controller.TransLeaveController;
import com.dm.library.AlertOkDialog;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.library.DateFunction;
import com.dm.library.IntentSend;
import com.dm.model.Sman;
import com.dm.model.TransLeaveRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class UpdateLeaveRequest extends FragmentActivity implements OnClickListener {
	TextView appDate,appBy,reportTo;EditText noOfDays,fromDate,toDate,reason;AlertOkDialog alertOkDialog;
	Spinner fromSpinner,toSpinner,salesManSpinner;Calendar cal;SimpleDateFormat df;
	SharedPreferences preferences2,preferences3;SmanController smanController;ArrayList<Sman> smanList;
	ArrayAdapter smanAdapter,leaveAdapter,leaveAdapter1,toLeaveAdapter;TransLeaveController transLeaveController;
	ImageButton fromDateButton;AlertOkDialog dialogWithOutView;Button update,cancel,delete;
	String currentDate;ArrayList<String>leaveArrayList,toLeaveArrayList;EnviroController enviroController;
	ArrayList<TransLeaveRequest> leaveRequestToUpdate;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_leave_request);
		preferences2=this.getSharedPreferences("MyPref",Context.MODE_PRIVATE);
		preferences3=this.getSharedPreferences("RETAILER_SESSION_DATA",Context.MODE_PRIVATE);
		setTitle("Leave Request Entry");
		appDate=(TextView)findViewById(R.id.applicationDate);
		appBy=(TextView)findViewById(R.id.appliedBy);
		reportTo=(TextView)findViewById(R.id.ReportTo);
		noOfDays=(EditText)findViewById(R.id.noOfDays);
		fromDate=(EditText)findViewById(R.id.fromDate);
		toDate=(EditText)findViewById(R.id.toDateEdit);
		reason=(EditText)findViewById(R.id.reasonOfleave);
		fromSpinner=(Spinner)findViewById(R.id.fromSpinner);
		toSpinner=(Spinner)findViewById(R.id.toSpinner);
		salesManSpinner=(Spinner)findViewById(R.id.salesPerson);
		fromDateButton=(ImageButton)findViewById(R.id.fromdataPickerOnLeave);
		update=(Button)findViewById(R.id.button1);
		cancel=(Button)findViewById(R.id.button2);
		delete=(Button)findViewById(R.id.button3);
		transLeaveController=new TransLeaveController(UpdateLeaveRequest.this);
		leaveArrayList=new ArrayList<String>();
		leaveArrayList.add("Select");
		leaveArrayList.add("First Half");
		leaveArrayList.add("Second Half");
		leaveArrayList.add("Full");
		leaveAdapter=new ArrayAdapter<String>(UpdateLeaveRequest.this, android.R.layout.simple_list_item_1,leaveArrayList);
		fromSpinner.setAdapter(leaveAdapter);
		toLeaveArrayList=new ArrayList<String>();
		toLeaveArrayList.add("Select");
		toLeaveArrayList.add("First Half");
		toLeaveArrayList.add("Second Half");
		toLeaveArrayList.add("Full");
		toLeaveAdapter=new ArrayAdapter<String>(UpdateLeaveRequest.this, android.R.layout.simple_list_item_1,toLeaveArrayList);
		toSpinner.setAdapter(toLeaveAdapter);
		transLeaveController.open();
		leaveRequestToUpdate=transLeaveController.getLeaveRequestToUpdate(preferences2.getString("CONPERID_SESSION", null),preferences3.getString("FROMDATE_SESSION", null),preferences3.getString("TODATE_SESSION", null),preferences3.getString("lEAVE_ANDROID_ID_SESSION", null),preferences3.getString("LEAVEDOCID_SESSION", null));
		transLeaveController.close();
		if(leaveRequestToUpdate.get(0).getUpload().equals("0"))
		{
			fieldsEnable(true);
			
			
			
		}
		else
		{
			
			fieldsEnable(false);
			
			
		}
//		cal=Calendar.getInstance();
//	    df = new SimpleDateFormat("dd/MMM/yyyy");
//	    appDate.setText(df.format(cal.getTime()));
//	    currentDate=df.format(cal.getTime());
	    smanController=new SmanController(this);
	    smanController.open();
	    smanList=smanController.getName(preferences2.getString("CONPERID_SESSION", null));
	    smanController.close();
	    ArrayList<String> salesManNameList=new ArrayList<String>();
	    try{
	    for(int i=0;i<smanList.size();i++)
	    {
	    	salesManNameList.add(smanList.get(i).getUser_Name());
	    }
	    smanAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,salesManNameList);
	    salesManSpinner.setAdapter(smanAdapter);
	    }catch(Exception e)
	    {
	    	System.out.println(e);
	    }
	    appBy.setText(salesManNameList.get(0));
	    reportTo.setText(smanList.get(0).getDisplayName());
	    appDate.setText(leaveRequestToUpdate.get(0).getVdate());
	   noOfDays.setText(leaveRequestToUpdate.get(0).getNoOfDay());
	   fromDate.setText(DateFunction.ToConvertDateFormat(leaveRequestToUpdate.get(0).getFromDate(),"yyyy-MM-dd HH:mm:ss","dd/MMM/yyyy"));
	   toDate.setText(DateFunction.ToConvertDateFormat(leaveRequestToUpdate.get(0).getToDate(),"yyyy-MM-dd HH:mm:ss","dd/MMM/yyyy"));
	   reason.setText(leaveRequestToUpdate.get(0).getReason());
	   if(noOfDays.getText().toString().equals("0.5"))
	   {
		   if(leaveRequestToUpdate.get(0).getLeaveFlag().equals("F"))
		   {
			   fromSpinner.setSelection(1);
			   toSpinner.setVisibility(View.GONE);
		    }
		   else{
			   fromSpinner.setSelection(2);
			   toSpinner.setVisibility(View.GONE);
		   }
	   }
	   else if(Double.parseDouble(noOfDays.getText().toString())==1.0)
	   {
		   if(leaveRequestToUpdate.get(0).getLeaveFlag().equals("S"))
		   {
			   fromSpinner.setSelection(2);
			   toSpinner.setVisibility(View.VISIBLE);
			   toSpinner.setSelection(1);
		    }
		   else if(leaveRequestToUpdate.get(0).getLeaveFlag().equals("C"))
		   {
			   fromSpinner.setSelection(3);
			   toSpinner.setVisibility(View.VISIBLE);
			   toSpinner.setSelection(3);
			  
		   }
	   }
	   else if(Double.parseDouble(noOfDays.getText().toString())>1.0)
	   {
		   
		   if(leaveRequestToUpdate.get(0).getLeaveFlag().equals("S"))
		   {
			   fromSpinner.setSelection(2);
			   toSpinner.setVisibility(View.VISIBLE);
//			   toSpinner.setSelection(3);
		    }
		   else if(leaveRequestToUpdate.get(0).getLeaveFlag().equals("C"))
		   {
			   fromSpinner.setSelection(3);
			   toSpinner.setVisibility(View.VISIBLE);
//			   toSpinner.setSelection(2);
			  
		   }
	   }
	    noOfDays.addTextChangedListener(new TextWatcher() {
			
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
//				
				try{
				String days=s.toString();
//				
				String[] words=days.split("\\.");//splits the string based on string 	
				if(words.length==2)
				{
					if(Integer.parseInt(words[1])>0)
					{
						fromSpinner.setVisibility(View.VISIBLE);
						 if(words[1].equals("5") && words[0].equals("0"))
	                        {
//							 leaveArrayList.remove(3);
							 leaveArrayList.clear();
							 leaveArrayList.add("Select");
							 leaveArrayList.add("First Half");
							 leaveArrayList.add("Second Half");
							 leaveAdapter=new ArrayAdapter<String>(UpdateLeaveRequest.this, android.R.layout.simple_list_item_1,leaveArrayList);
						fromSpinner.setAdapter(leaveAdapter);
						fromSpinner.setVisibility(View.VISIBLE);
						toSpinner.setVisibility(View.VISIBLE);
						toSpinner.setEnabled(false);
	                        }
						 else
	                        {
//							 leaveArrayList.remove(1);
							 leaveArrayList.clear();
							 leaveArrayList.add("Select");
							 leaveArrayList.add("Second Half");
							 leaveArrayList.add("Full");
							 leaveAdapter=new ArrayAdapter<String>(UpdateLeaveRequest.this, android.R.layout.simple_list_item_1,leaveArrayList);
							 fromSpinner.setAdapter(leaveAdapter);
							 fromSpinner.setVisibility(View.VISIBLE);
							 toSpinner.setVisibility(View.VISIBLE);
							 toSpinner.setEnabled(false);
	                        }
					}
					 else
	                    {

						  fromSpinner.setVisibility(View.VISIBLE);
						  toSpinner.setVisibility(View.VISIBLE);
     					  leaveArrayList.clear();
							 leaveArrayList.add("Select");
							 leaveArrayList.add("Second Half");
							 leaveArrayList.add("Full");
						  leaveAdapter=new ArrayAdapter<String>(UpdateLeaveRequest.this, android.R.layout.simple_list_item_1,leaveArrayList);
						   fromSpinner.setAdapter(leaveAdapter);
						   toSpinner.setEnabled(false);
	                    }
					
				}
				else{
					 if (Integer.parseInt(days)==1)
	                    {

						 fromSpinner.setVisibility(View.VISIBLE);
						  toSpinner.setVisibility(View.VISIBLE);
						  leaveArrayList.clear();
							 leaveArrayList.add("Select");
							 leaveArrayList.add("Second Half");
							 leaveArrayList.add("Full");
						  leaveAdapter=new ArrayAdapter<String>(UpdateLeaveRequest.this, android.R.layout.simple_list_item_1,leaveArrayList);
						   fromSpinner.setAdapter(leaveAdapter);
						   toSpinner.setEnabled(false);                      
	                    
	                    }
	                    else
	                    {

	                    	leaveArrayList.clear();
							 leaveArrayList.add("Select");
							 leaveArrayList.add("Second Half");
							 leaveArrayList.add("Full");
						  leaveAdapter=new ArrayAdapter<String>(UpdateLeaveRequest.this, android.R.layout.simple_list_item_1,leaveArrayList);
						   fromSpinner.setAdapter(leaveAdapter);
						   fromSpinner.setVisibility(View.VISIBLE);
							  toSpinner.setVisibility(View.VISIBLE);  
							  toSpinner.setEnabled(false);
	                    }
				}
				

				
			}catch(Exception e)
			{
				System.out.println(e);
			}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				//new Custom_Toast(getApplicationContext(), "beforeTextChanged").showCustomAlert();
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				//new Custom_Toast(getApplicationContext(), "afterTextChanged").showCustomAlert();
				
				
				
			}
		});
	    fromDate.addTextChangedListener(new TextWatcher() {
			
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				//new Custom_Toast(getApplicationContext(), "onTextChanged").showCustomAlert();
				onDateDisplay(fromDate.getText().toString());	
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				//new Custom_Toast(getApplicationContext(), "beforeTextChanged").showCustomAlert();
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				//new Custom_Toast(getApplicationContext(), "afterTextChanged").showCustomAlert();
			}
		});
	    fromDateButton.setOnClickListener(new OnClickListener() {

	 	   @Override
	 	   public void onClick(View v) {
	 	    showDatePicker(v.getId());
	 	   }
	 	  });
	        
	    fromSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) 
	    {

					try{
					 double dt1 = Double.parseDouble(noOfDays.getText().toString().equals("")?"0":noOfDays.getText().toString());
	                    double fnum = Math.floor(dt1);
	                    double lnum = Math.ceil(dt1);
	                    double num2 = (fnum + lnum) / 2;
	                    double num = Math.round(num2);
	                    if (num > 1)
	                    {
	    					if(fromDate.getText().toString().equals(""))
	        				{
//	        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
	        				}
	    					else{
	                    Date tdate=df.parse(fromDate.getText().toString());
	                    Calendar c=Calendar.getInstance();
	                    c.setTime(tdate);
	                    c.add(Calendar.DATE, (int)num-1);
//	                    noOfDays.setText(Double.toString(num2));
	                    toDate.setText(df.format(c.getTime()));
                    }
	                    }
	                    else
	                    {
	                    	if(fromDate.getText().toString().equals(""))
	        				{
//	        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
	        				}
	                    	else{ 
	                    	toDate.setText(fromDate.getText().toString());
	                    	}
	                    }
	                    
	                    if(Double.parseDouble(noOfDays.getText().toString())==1.0)
	                    {
	                    	if (fromSpinner.getSelectedItem().equals("Second Half"))
	                        {
	                            toSpinner.setVisibility(View.VISIBLE);
	                            toSpinner.setSelection(3);
	                            toSpinner.setEnabled(false);
	                            if(fromDate.getText().toString().equals(""))
		        				{
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
		        				}else{
	                            Date tdate1=df.parse(fromDate.getText().toString());
	    	                    Calendar c1=Calendar.getInstance();
	    	                    c1.setTime(tdate1);
	    	                    c1.add(Calendar.DATE,1);
//	    	                    noOfDays.setText(Double.toString(num2));
	    	                    toDate.setText(df.format(c1.getTime()));
		        				}
	                       
	                        }
	                        if (fromSpinner.getSelectedItem().equals("Full"))
	                        {
	                        	if(fromDate.getText().toString().equals(""))
		        				{
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
		        				}
	                        	else{
	                        	Date tdate1=df.parse(fromDate.getText().toString());
	    	                    Calendar c1=Calendar.getInstance();
	    	                    c1.setTime(tdate1);
	    	                    toDate.setText(df.format(c1.getTime()));
	    	                    toSpinner.setSelection(3);
	    	                    toSpinner.setEnabled(false);

	                        }}
	                        else{
	                        	if(fromDate.getText().toString().equals(""))
		        				{
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
		        				}
	                        	else{
	                        	Date tdate1=df.parse(fromDate.getText().toString());
	    	                    Calendar c1=Calendar.getInstance();
	    	                    c1.setTime(tdate1);
	    	                    c1.add(Calendar.DATE,1);
	    	                    toDate.setText(df.format(c1.getTime()));
	    	                    toSpinner.setSelection(1);
	    	                    toSpinner.setEnabled(false);
	                        	
	                        }
	                    }
	                    } 
	                    else if(Double.parseDouble(noOfDays.getText().toString())==0.5)
	                    {
	                    	if(fromSpinner.getSelectedItem().equals("Select"))
	                    	{
	                    		
	                    		toSpinner.setSelection(0);
                                toSpinner.setEnabled(false);
	                    	}
	                    	else if (fromSpinner.getSelectedItem().equals("First Half"))
	                    	{
	                    		toSpinner.setSelection(1);
                                toSpinner.setEnabled(false);
	                    	}
	                    	else
	                    	{
	                    		toSpinner.setSelection(2);
                                toSpinner.setEnabled(false);
	                    	}
	                    }
	                    else if(Double.parseDouble(noOfDays.getText().toString())>1.0)
	                    {
	                        String str = noOfDays.getText().toString();
	                        String[] str1 = str.split("\\.");
	                         if (str1.length == 2)
	                         {
	                             if(Double.parseDouble(str1[1])==5.0)
	                             {
	                                 if (fromSpinner.getSelectedItem().equals("Second Half"))
	                                 {
	                                     toSpinner.setSelection(3);
	                                     toSpinner.setEnabled(false);
	                                     
	                                 }
	                                 else if (fromSpinner.getSelectedItem().equals("Full"))
	                                 {
	                                	 toSpinner.setSelection(1);
	                                     toSpinner.setEnabled(false);
	                                 }
	                             }
	                             else
	                             {
	                                 if (fromSpinner.getSelectedItem().equals("Second Half"))
	                                 {
	                                	 toSpinner.setSelection(1);
	                                     toSpinner.setEnabled(false);
	                                     if(fromDate.getText().toString().equals(""))
	         	        				{
//	         	        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
	         	        				}
	                                     else{
	                                     Date tdate1=df.parse(fromDate.getText().toString());
	     	    	                    Calendar c1=Calendar.getInstance();
	     	    	                    c1.setTime(tdate1);
	     	    	                    c1.add(Calendar.DATE,1);
	     	    	                   toDate.setText(df.format(c1.getTime()));
	                                     }
	                                 }
	                                 else if (fromSpinner.getSelectedItem().equals("Full"))
	                                 {
	                                	 toSpinner.setSelection(1);
	                                     toSpinner.setEnabled(false);

	                                 }
	                             }
	                         }
	                         else
	                         {
	                             if (fromSpinner.getSelectedItem().equals("Second Half"))
	                             {
	                            	 toSpinner.setSelection(1);
                                     toSpinner.setEnabled(false);
                                     Date tdate1=df.parse(toDate.getText().toString());
	     	    	                    Calendar c1=Calendar.getInstance();
	     	    	                    c1.setTime(tdate1);
	     	    	                    c1.add(Calendar.DATE,1);
	     	    	                   toDate.setText(df.format(c1.getTime()));
	                                
	                             }
	                             else if (fromSpinner.getSelectedItem().equals("Full"))
	                             {
	                                 toSpinner.setSelection(1);
	                                 toSpinner.setEnabled(false);
	                                 
	                             }
	                         }
	                      
	                    
	                    
	                    }
	                   
					}catch(Exception e)
					{
						
					}
					

				
	    }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
	    });
	
	    update.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
			
				if(noOfDays.getText().toString().equals(""))
				 {
					 new Custom_Toast(getApplicationContext(), "Please enter No. of days").showCustomAlert();
				 }
				else if(fromDate.getText().toString().equals(""))
				{
					new Custom_Toast(getApplicationContext(), "Please select From Date").showCustomAlert();
				}
				else if(fromSpinner.getSelectedItem().equals("Select"))
				{
					 new Custom_Toast(getApplicationContext(), "Please select First/Second Half or Full").showCustomAlert();
				}
				else if(reason.getText().toString().equals(""))
				{
					new Custom_Toast(getApplicationContext(), "Please enter Reason").showCustomAlert();
				}
				else{
					
//					TransLeaveRequest transLeaveRequest=new TransLeaveRequest();
					leaveRequestToUpdate.get(0).setNoOfDay(noOfDays.getText().toString());
					leaveRequestToUpdate.get(0).setFromDate(fromDate.getText().toString());
					leaveRequestToUpdate.get(0).setToDate(toDate.getText().toString());
					leaveRequestToUpdate.get(0).setReason(reason.getText().toString());
					leaveRequestToUpdate.get(0).setVdate(leaveRequestToUpdate.get(0).getVdate());
					leaveRequestToUpdate.get(0).setAppStatus(leaveRequestToUpdate.get(0).getAppStatus());
					leaveRequestToUpdate.get(0).setAppRemark("");
					leaveRequestToUpdate.get(0).setSmId(leaveRequestToUpdate.get(0).getSmid());
					leaveRequestToUpdate.get(0).setUserId(leaveRequestToUpdate.get(0).getUserId());
				
					if(fromSpinner.getSelectedItem().toString().equals("Full"))
					{
						leaveRequestToUpdate.get(0).setLeaveFlag("C");
					}
					else if(fromSpinner.getSelectedItem().toString().equals("First Half"))
					{
						leaveRequestToUpdate.get(0).setLeaveFlag("F");
					}
					else if(fromSpinner.getSelectedItem().toString().equals("Second Half"))
					{
						leaveRequestToUpdate.get(0).setLeaveFlag("S");
					}
					

					leaveRequestToUpdate.get(0).setAndroid_id(leaveRequestToUpdate.get(0).getAndroid_id());
				 transLeaveController.open();
					transLeaveController.updateLeave(leaveRequestToUpdate.get(0));
					transLeaveController.close();
				  new Custom_Toast(getApplicationContext(),"Leave Request Updated successfully", R.drawable.save1).showCustomAlert();
//				  new ExportLeaveRequest(UpdateLeaveRequest.this);
				  (new IntentSend(getApplicationContext(),DashBoradActivity.class)).toSendAcivity();
				}
			
			}
			
	    
	    });
 cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
//				clearFields();
				 (new IntentSend(getApplicationContext(),LeaveRequest.class)).toSendAcivity();
				
			}
 });
 
 delete.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
//			clearFields();
			 transLeaveController.open();
			 transLeaveController.deleteLeaveRequest(leaveRequestToUpdate.get(0).getAndroid_id());
			 transLeaveController.close();
			 new Custom_Toast(getApplicationContext(), "Record deleted successfully").showCustomAlert();
			 (new IntentSend(getApplicationContext(),LeaveRequest.class)).toSendAcivity();
		}
});
	}
	
	private void showDatePicker(int id) {
		 DateAndTimePicker date = new DateAndTimePicker();
	  /**
	   * Set Up Current Date Into dialog
	   */
	  Calendar calender = Calendar.getInstance();
	  Bundle args = new Bundle();
	  args.putInt("year", calender.get(Calendar.YEAR));
	  args.putInt("month", calender.get(Calendar.MONTH));
	  args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
	  date.setArguments(args);
	  /**
	   * Set Call back to capture selected date
	   */
	  if(id== R.id.fromdataPickerOnLeave)
	  {
	  date.setCallBack(ondate);
	  date.show(getSupportFragmentManager(), "Date Picker");
	  }
	 
	  }

	 OnDateSetListener ondate = new OnDateSetListener() {
	  @Override
	  public void onDateSet(DatePicker view, int year, int monthOfYear,
	    int dayOfMonth) {
		  String strDate=(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth))+"/"+(((monthOfYear+1)<10?("0"+(monthOfYear+1)):(monthOfYear+1)))+"/"+year;
			 SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
			 SimpleDateFormat format2 = new SimpleDateFormat("dd/MMM/yyyy");
//			 SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
			 Date date = null;
			try {
				date = format1.parse(strDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 System.out.println(format2.format(date));
			 Date filledDate = null,currenttime = null;
			try {
				filledDate = format2.parse(format2.format(date));
				currenttime=format2.parse(currentDate);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Calendar c = Calendar.getInstance();
			System.out.println(c.getTime()); // Tue Jun 18 17:07:45 IST 2013
			c.set(Calendar.DATE, c.get(Calendar.DATE)-30);
			System.out.println("time before="+c.getTime()+" "+c.getTimeInMillis());
			 Date dateBefore = new Date(currenttime.getTime() - (30 * 24 * 3600 * 1000) );
//			 long i=filledDate.getTime()-dateBefore.getTime();
			 long i=filledDate.getTime()-c.getTimeInMillis();
			 System.out.println("30 days="+(30 * 24 * 3600 * 1000) );
			 System.out.println("curent="+currenttime.getTime());
			 System.out.println("before="+c.getTimeInMillis());
			 System.out.println("i="+i);

			    		fromDate.setText(format2.format(date));


	  }
	 };
	 
//
		 public void onDateDisplay(String selectedDate)
		 { 
			 try{
			 double dt1 = Double.parseDouble(noOfDays.getText().toString().equals("")?"0":noOfDays.getText().toString());
             double fnum = Math.floor(dt1);
             double lnum = Math.ceil(dt1);
             double num2 = (fnum + lnum) / 2;
             double num = Math.round(num2);
              if (num > 1)
             {

             Date tdate=df.parse(fromDate.getText().toString());
             Calendar c=Calendar.getInstance();
             c.setTime(tdate);
             c.add(Calendar.DATE, (int)num);
//             noOfDays.setText(Double.toString(num2));
             toDate.setText(df.format(c.getTime()));
             
             }
             else
             {
             	toDate.setText(fromDate.getText().toString());
             	
             }
			 }catch(Exception e)
			 {
				System.out.println(e); 
			 }
			 
		 }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return true;
	}
	
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

	}
	
	
private void fieldsEnable(boolean flag)
{
	if(flag){
noOfDays.setEnabled(flag);
reason.setEnabled(flag);
fromSpinner.setEnabled(flag);
fromDateButton.setEnabled(flag);
update.setEnabled(flag);
delete.setEnabled(flag);
}
	else{
		noOfDays.setEnabled(flag);
		reason.setEnabled(flag);
		fromSpinner.setEnabled(flag);
		fromDateButton.setEnabled(flag);
		update.setEnabled(flag);
		update.setBackgroundColor(Color.parseColor("#7fb2ff"));
		delete.setEnabled(flag);
		delete.setBackgroundColor(Color.parseColor("#7fb2ff"));
	}}
@Override
public void onClick(View v) {
	// TODO Auto-generated method stub
	
}

}
