package com.dm.library;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;

import com.dm.crmdm_app.R;


public class IntentSend{
	Activity fromActivity;
	Context context;
	String sendData;
	Bundle bundle; private GestureDetector gestureDetector;
    View.OnTouchListener gestureListener;
	Class<? extends Activity> toSendActivityClass;
	
	public IntentSend(Context context,Class<? extends Activity> toSendActivityClass) {
			
		this(context,toSendActivityClass,null,null);
		}
	public IntentSend(Context context) {
		
		this(context,null,null,null);
		}
	public IntentSend(Context context,Class<? extends Activity> toSendActivityClass,String sendData) {
		this(context,toSendActivityClass,sendData,null);
	}
	public IntentSend(Context context,Class<? extends Activity> toSendActivityClass,Bundle sendDataBundle) {
		this.toSendActivityClass=toSendActivityClass;
		this.context=context;
		this.bundle=sendDataBundle;
	}
	public IntentSend(Context context,Class<? extends Activity> toSendActivityClass,String sendData,Activity fromActivity) {
		this.toSendActivityClass=toSendActivityClass;
		this.context=context;
		this.fromActivity=fromActivity;
		this.sendData=sendData;
	}

	public void toSendAcivity()
	{	

		if(sendData==null)
		{
			System.out.println(" fromActivity="+fromActivity+" context="+context+" toSendActivityClass= "+toSendActivityClass.getName());
			Intent intent = new Intent(context, toSendActivityClass);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle bndlanimation =ActivityOptions.makeCustomAnimation(context, R.anim.animation, R.anim.animation2).toBundle();
			context.startActivity(intent, bndlanimation);
			//Intent intent=new Intent(context,toSendActivityClass);
		
			System.out.println("intent="+intent);
			//context.startActivity(intent);
		}
		else {
			Intent intent=new Intent(context,toSendActivityClass);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle bundle=new Bundle();
			bundle.putString("sendData", sendData);
			intent.putExtras(bundle);
			Bundle bndlanimation =ActivityOptions.makeCustomAnimation(context, R.anim.animation, R.anim.animation2).toBundle();
			context.startActivity(intent,bndlanimation);
		}
	}
	public void toSendAcivityWithBundle()
	{	
			Intent intent=new Intent(context,toSendActivityClass);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtras(bundle);
			Bundle bndlanimation =ActivityOptions.makeCustomAnimation(context, R.anim.animation, R.anim.animation2).toBundle();
			context.startActivity(intent,bndlanimation);
		}
	}

