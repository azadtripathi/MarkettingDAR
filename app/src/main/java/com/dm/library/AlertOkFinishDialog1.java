package com.dm.library;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.dm.crmdm_app.MainActivity;
import com.dm.crmdm_app.R;

import static android.content.Context.MODE_PRIVATE;

public class AlertOkFinishDialog1 extends DialogFragment {
	private String message;

	/* The activity that creates an instance of this dialog fragment must
	 * implement this interface in order to receive event callbacks.
	 * Each method passes the DialogFragment in case the host needs to query it. */
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
			builder.setTitle("Message");
			builder.setIcon(R.drawable.information).setCancelable(false);
			builder.setMessage(getArguments().getString("message"));
			builder.setPositiveButton("ok",new OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					SharedPreferences settings = getActivity().getSharedPreferences("RETAILER_SESSION_DATA",MODE_PRIVATE);
					settings.edit().clear().commit();
					SharedPreferences settings1 = getActivity().getSharedPreferences("PreferencesName", MODE_PRIVATE);
					settings1.edit().clear().commit();
					arg0.dismiss();
					Intent i = getActivity().getBaseContext().getPackageManager()
							.getLaunchIntentForPackage( getActivity().getBaseContext().getPackageName() );
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
					startActivity(i);

				}
			});
			Dialog dialog=builder.create();
			return dialog;
		}
		public static AlertOkFinishDialog1 newInstance(String message) {
			AlertOkFinishDialog1 f = new AlertOkFinishDialog1();
		    Bundle args = new Bundle();
		    args.putString("message", message);
		    f.setArguments(args);
		    return f;
		}

	}
