package com.dm.library;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.crmdm_app.R;


public class Custom_Toast{
Context context;private String Message;private int image_Id; 
	public Custom_Toast(Context context,String message,int image_Id) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.Message=message;
		this.image_Id=image_Id;
	}
	public Custom_Toast(Context context,String message) {
		// TODO Auto-generated constructor stub
		this(context,message,0);
	}
	public void showCustomAlert()
    {
         
       // Context context = getApplicationContext();
        
        // Create layout inflator object to inflate toast.xml file
		LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // Call toast.xml file for toast layout
        View toastRoot = inflater.inflate(R.layout.custom_toast, null);
         TextView messageView=(TextView)toastRoot.findViewById(R.id.messageToastText);
         ImageView imageView=(ImageView)toastRoot.findViewById(R.id.messageToastimage);
         messageView.setText(Message);
         imageView.setImageResource(image_Id);
         Toast toast = new Toast(context);
       // Set layout to toast
        toast.setView(toastRoot);
        toast.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL,
                0, 0);
        toast.setDuration(1500);
        toast.show(); 
         
    }

}
