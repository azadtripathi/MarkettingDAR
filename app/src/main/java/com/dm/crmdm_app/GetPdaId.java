package com.dm.crmdm_app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dm.controller.DeviceInfoController;
import com.dm.library.AlertOkDialog;
import com.dm.library.IntentSend;


public class GetPdaId extends Activity {
AlertOkDialog alertOkDialog;DeviceInfoController deviceInfoController;TextView pd;
//File myFile;FileOutputStream fOut;OutputStreamWriter myOutWriter;
//SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_pda_id);
		  pd=(TextView)findViewById(R.id.pdaview);
		final EditText pdaid=(EditText)findViewById(R.id.pdaid);
		pd.setText("Device no");
		deviceInfoController=new DeviceInfoController(this);
		Button submitButton=(Button)findViewById(R.id.submit);
		deviceInfoController.open();
		final String pdaidbyuser=deviceInfoController.getpdaId();
		//deviceInfoController.deletedeviceRow();
		if(pdaidbyuser.equals("0"))
		{
			pdaid.setText("");
		}
		else
		{
			pdaid.setText(String.valueOf(pdaidbyuser));
		}
		deviceInfoController.close();
		submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(pdaid.getText().toString().isEmpty())
				{
					alertOkDialog= AlertOkDialog.newInstance("Please Enter Device No. !");
					alertOkDialog.show(getFragmentManager(), "pda");
					
				}
				else
				{
					deviceInfoController.open();
					final String pdaidbyuser=deviceInfoController.getpdaId();
					deviceInfoController.close();
					if(pdaidbyuser.equals("0"))
					{
						
						deviceInfoController.open();
						deviceInfoController.insertDeviceInfo(pdaid.getText().toString());
						deviceInfoController.close();
						Log.d("msg", "insert");
						SharedPreferences preferences1=getSharedPreferences("MyPref",MODE_PRIVATE);
						 Editor editor1=preferences1.edit();
						 editor1.putString("PDAID_SESSION", pdaid.getText().toString());
						 editor1.commit();
						//new IntentSend(getApplicationContext(),LoginActivity.class).toSendAcivity();
						
					}
					else
					{
						deviceInfoController.open();
						deviceInfoController.getUpdatePdaId(pdaidbyuser,pdaid.getText().toString());
						
						deviceInfoController.close();
						Log.d("msg", "update");
						SharedPreferences preferences1=getSharedPreferences("MyPref",MODE_PRIVATE);
						Editor editor1=preferences1.edit();
						editor1.putString("PDAID_SESSION", pdaid.getText().toString());
						editor1.commit();
					//	new IntentSend(getApplicationContext(),LoginActivity.class).toSendAcivity();
						
					}
					
				}
				
				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		return true;
	}
    
//	 public void writeLog(String msg) {
//		 Calendar calendar1=Calendar.getInstance();
//		  try{
//			  myFile =new File("/sdcard/myastralfile.txt");
//		       //myFile = new File(Environment.getDataDirectory()+"/fftlog/mysdfile.txt");
//		       if(!myFile.exists())
//		       {
//		   	   myFile.createNewFile();
//		       }
//		   	   fOut = new FileOutputStream(myFile,true);
//		   	   myOutWriter = new OutputStreamWriter(fOut);
//		   	   myOutWriter.append(msg+"  "+dateFormat.format(calendar1.getTime())+" \n");
//		   	   myOutWriter.close();
//		   	   fOut.close();
//				} 
//		     catch (IOException e) {
//		    	// writeLog("IO exception");
//						e.printStackTrace();
//						}
//	}
	
}
