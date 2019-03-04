package com.dm.library;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;

public class AlertMessageConfirmation extends DialogFragment {
private String message; static private String okString,cancelString;

/* The activity that creates an instance of this dialog fragment must
 * implement this interface in order to receive event callbacks.
 * Each method passes the DialogFragment in case the host needs to query it. */
public interface NoticeDialogListenerConfirmation {
    public void onDialogPositiveConfirmation(DialogFragment dialog);
    public void onDialogNegativeConfirmation(DialogFragment dialog);
	//public void onDialogPositiveConfirmation(DialogFragment dialog,String vdate);

}

// Use this instance of the interface to deliver action events
NoticeDialogListenerConfirmation mListener;

// Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
@Override
public void onAttach(Activity activity) {
    super.onAttach(activity);
    // Verify that the host activity implements the callback interface
    try {
        // Instantiate the NoticeDialogListener so we can send events to the host
        mListener = (NoticeDialogListenerConfirmation) activity;
    } catch (ClassCastException e) {
        // The activity doesn't implement the interface, throw exception
        throw new ClassCastException(activity.toString()
                + " must implement NoticeDialogListener");
    }
}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
		builder.setTitle("message");
		builder.setMessage(getArguments().getString("message"));
		builder.setPositiveButton(okString,new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				mListener.onDialogPositiveConfirmation(AlertMessageConfirmation.this);
				
			}
		});
		builder.setNegativeButton(cancelString,new OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						 mListener.onDialogNegativeConfirmation(AlertMessageConfirmation.this);
						
					}
				});
		Dialog dialog=builder.create();
		return dialog;
	}
	public static AlertMessageConfirmation newInstance(String message, String ok, String Cancel) {
		okString=ok;
		cancelString=Cancel;
		AlertMessageConfirmation f = new AlertMessageConfirmation();
	    Bundle args = new Bundle();
	    args.putString("message", message);
	    f.setArguments(args);
	    return f;
	}

}
