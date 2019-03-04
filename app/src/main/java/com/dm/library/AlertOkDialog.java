package com.dm.library;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

import com.dm.crmdm_app.R;


public class AlertOkDialog  extends DialogFragment {
	private String message;

	/* The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks.
	 * Each method passes the DialogFragment in case the host needs to query it. */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
			builder.setTitle("Message");
//			builder.setIcon(R.drawable.information);
			builder.setMessage(getArguments().getString("message"));
			builder.setPositiveButton("ok",new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					arg0.dismiss();
					
				}
			});
			Dialog dialog=builder.create();
			return dialog;
		}
		public static AlertOkDialog newInstance(String message) {
			AlertOkDialog f = new AlertOkDialog();
		    Bundle args = new Bundle();
		    args.putString("message", message);
		    f.setArguments(args);
		    return f;
		}

	}
