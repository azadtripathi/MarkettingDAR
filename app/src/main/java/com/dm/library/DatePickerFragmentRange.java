package com.dm.library;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Dataman on 1/28/2017.
 */
public class DatePickerFragmentRange extends DialogFragment {
    DatePickerDialog.OnDateSetListener ondateSet;
    int key;
    public DatePickerFragmentRange() {
    }

    public void setCallBack(DatePickerDialog.OnDateSetListener ondate) {
        ondateSet = ondate;
    }

    private int year, month, day,grace;long maxDate,minDate;String type;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        year = args.getInt("year");
        month = args.getInt("month");
        day = args.getInt("day");
        grace = args.getInt("grace");
        type=args.getString("Type","");
        key = args.getInt("key");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DatePickerDialog pickerDialog=null;
        Date today = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(today);
         maxDate = c.getTime().getTime();
//        maxDate = c.getTimeInMillis();
        if(type.equalsIgnoreCase("month"))
        { c.add( Calendar.MONTH, -grace );
            minDate = c.getTime().getTime();
            pickerDialog = new DatePickerDialog(getActivity(), ondateSet, year, month, day);
            pickerDialog.getDatePicker().setMinDate(minDate);
            pickerDialog.getDatePicker().setMaxDate(maxDate);

        }
        else if(type.equalsIgnoreCase("days"))
        {c.add( Calendar.DATE, -grace );
            minDate = c.getTime().getTime();
//            minDate = c.getTimeInMillis();
            pickerDialog = new DatePickerDialog(getActivity(), ondateSet, year, month, day);
            pickerDialog.getDatePicker().setMinDate(minDate);
            pickerDialog.getDatePicker().setMaxDate(maxDate);
            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                pickerDialog.getDatePicker().setCalendarViewShown(true);
            }
            else
            {
                pickerDialog.getDatePicker().setCalendarViewShown(false);
            }

        }
        else if(type.equalsIgnoreCase("tour"))
        {
            minDate = c.getTime().getTime();
            pickerDialog = new DatePickerDialog(getActivity(), ondateSet, year, month, day);
            pickerDialog.getDatePicker().setMinDate(minDate);
            //pickerDialog.getDatePicker().setMaxDate(maxDate);
            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                pickerDialog.getDatePicker().setCalendarViewShown(true);
            }
            else
            {
                pickerDialog.getDatePicker().setCalendarViewShown(false);
            }

        }
        else if(type.equalsIgnoreCase("complain"))
        {
            minDate = c.getTime().getTime();
            pickerDialog = new DatePickerDialog(getActivity(), ondateSet, year, month, day);
            //pickerDialog.getDatePicker().setMinDate(minDate);
            pickerDialog.getDatePicker().setMaxDate(maxDate);
            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                pickerDialog.getDatePicker().setCalendarViewShown(true);
            }
            else
            {
                pickerDialog.getDatePicker().setCalendarViewShown(false);
            }

        }

        return pickerDialog;
//        return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
    }

}