package com.dm.library;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.util.ArrayList;

public class InputBoxSstyle{
	RelativeLayout relativeLayout;EditText inputBox;Context context;
	ArrayList<EditText> editTexts;

	public InputBoxSstyle(ArrayList<EditText> editTexts ,Context context) {
		this.editTexts=editTexts;
		this.context=context;
		// TODO Auto-generated constructor stub
	}
	public void inputBoxchangeBackground()
	{
		for (int i = 0; i < editTexts.size(); i++) {
			editTexts.get(i).setOnFocusChangeListener(new OnFocusChangeListener() {
				
				@Override
				public void onFocusChange(View arg0, boolean arg1) {
					// TODO Auto-generated method stub
					if(arg1)
					{
						EditText editText=(EditText)arg0;
						editText.setBackgroundColor(Color.WHITE);
					}
				}
			});
				
		}
			
	}
	public void setFontStyle()
	{	
	
		Typeface typeface=Typeface.SANS_SERIF;
		for (int i = 0; i < editTexts.size(); i++) {
			//Toast.makeText(context, "font set", Toast.LENGTH_LONG).show();
			editTexts.get(i).setTypeface(typeface);
				
		}
	}

	
}
