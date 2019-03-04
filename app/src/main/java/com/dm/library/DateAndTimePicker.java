package com.dm.library;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

public class DateAndTimePicker extends DialogFragment {
 OnDateSetListener ondateSet;

 public DateAndTimePicker() {
 }

 public void setCallBack(OnDateSetListener ondate) {
  ondateSet = ondate;
 }
// public void setCallBack(OnDateSetListener ondate,String fromDate) {
//  ondateSet = ondate;
// }

 private int year, month, day,key;

 @Override
 public void setArguments(Bundle args)
 {
  super.setArguments(args);
  year = args.getInt("year");
  month = args.getInt("month");
  day = args.getInt("day");
  key = args.getInt("key");


 }

 @Override
 public Dialog onCreateDialog(Bundle savedInstanceState) {
  Calendar c = Calendar.getInstance();
  DatePickerDialog dt = new DatePickerDialog(getActivity(), ondateSet, year, month, day);


  if(key == 11)
  {
   dt.getDatePicker().setMaxDate(System.currentTimeMillis()-1);
  }
  else if (key == 12){
   dt.getDatePicker().setMinDate(System.currentTimeMillis());
  }

  if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
   dt.getDatePicker().setCalendarViewShown(true);
  }


  return dt;
 }


}