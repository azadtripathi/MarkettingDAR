package com.dm.library;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;

import com.dm.crmdm_app.R;

public class AlertOkFinishDialog extends DialogFragment {
	private String message;

	/* The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks.
	 * Each method passes the DialogFragment in case the host needs to query it. */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
			builder.setTitle("Message");
			builder.setCancelable(false);
			builder.setIcon(R.drawable.information).setCancelable(false);
			builder.setMessage(getArguments().getString("message"));
			builder.setPositiveButton("ok",new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					arg0.dismiss();
					System.exit(0);
					Intent homeIntent = new Intent(Intent.ACTION_MAIN);
					//Intent homeIntent = new Intent(getActivity(), MainActivity.class);
				    homeIntent.addCategory( Intent.CATEGORY_HOME );
				    homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				    startActivity(homeIntent);

				}
			});
			Dialog dialog=builder.create();
			dialog.setCanceledOnTouchOutside(false);
			return dialog;
		}
		public static AlertOkFinishDialog newInstance(String message) {
			AlertOkFinishDialog f = new AlertOkFinishDialog();
		    Bundle args = new Bundle();
		    args.putString("message", message);
		    f.setArguments(args);
		    return f;
		}

	}
