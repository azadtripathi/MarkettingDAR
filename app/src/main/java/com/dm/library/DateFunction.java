package com.dm.library;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.dm.service.NotificationService;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateFunction {
	static Calendar calendar;static int  mHour,mMinute,mYear,mMonth,mDay;static AlertOkDialog alertOkDialog;
	public static String ToConvertDateTime(String dateToBeConverted,String hour,String mm,String ss) {
		String dateToBeReturned=null;
		SimpleDateFormat dateFormatConvertedInTo=new SimpleDateFormat("yyyy-MM-dd "+hour+":"+mm+":"+ss);
		SimpleDateFormat formatToBeConvertedFrom=new SimpleDateFormat("dd/MMM/yyyy");
		try {
			dateToBeReturned=dateFormatConvertedInTo.format(formatToBeConvertedFrom.parse(dateToBeConverted));
			System.out.println(dateToBeReturned);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateToBeReturned;

	}
	public static String ToConvertDateString(String dateToBeConverted) {
		String dateToBeReturned="";
		SimpleDateFormat formatToBeConvertedFrom=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat dateFormatConvertedInTo=new SimpleDateFormat("dd/MM/yyyy");
		try {
			dateToBeReturned=dateFormatConvertedInTo.format(formatToBeConvertedFrom.parse(dateToBeConverted));
			System.out.println(dateToBeReturned);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateToBeReturned;

	}
	public static long ConvertDateToTimestamp(String dateToBeConverted) {
		long timeStamp=0;
		SimpleDateFormat dateFormatConvertedFrom=new SimpleDateFormat("dd/MMM/yyyy");
		try {
			timeStamp=(long)(dateFormatConvertedFrom.parse(dateToBeConverted).getTime());
			System.out.println(timeStamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeStamp;

	}
	public static long ConvertDateToTimestampWithTime(String dateToBeConverted) {
		long timeStamp=0;
		SimpleDateFormat dateFormatConvertedFrom=new SimpleDateFormat("dd/MMM/yyyy HH:mm");
		try {

			timeStamp=(long)(dateFormatConvertedFrom.parse(dateToBeConverted).getTime());
			System.out.println(timeStamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeStamp;

	}

	public static long ConvertDateToTimestampWithTimeAmPm(String dateToBeConverted) {
		long timeStamp=0;
		SimpleDateFormat dateFormatConvertedFrom=new SimpleDateFormat("dd/MMM/yyyy hh:mm:ss a");
		try {
			timeStamp=(long)(dateFormatConvertedFrom.parse(dateToBeConverted).getTime());
			System.out.println(timeStamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeStamp;

	}


	//	public static long ConvertDateToTimestampWithTime(String dateToBeConverted) {
//		long timeStamp=0;
//		SimpleDateFormat dateFormatConvertedFrom=new SimpleDateFormat("dd/MMM/yyyy HH:mm");
//		try {
//			timeStamp=(long)(dateFormatConvertedFrom.parse(dateToBeConverted).getTime());
//			System.out.println(timeStamp);
//		} catch (ParseException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return timeStamp;
//
//	}
	public static long ConvertDateToTimestamp(String dateToBeConverted,String currentFormat) {
		//String dateToBeConverted1=ToConvertDateTimeFortimeStamp(dateToBeConverted);
		long timeStamp=0;
		SimpleDateFormat dateFormatConvertedFrom=new SimpleDateFormat(currentFormat);
		Date d=new Date();
		try {
			//timeStamp=dateFormatConvertedFrom.parse(dateToBeConverted).getTime();
			d.setTime(dateFormatConvertedFrom.parse(dateToBeConverted).getTime());
//			d.setTime(dateFormatConvertedFrom.parse(dateToBeConverted).getTime());
			timeStamp=d.getTime();
			System.out.println(timeStamp);

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeStamp;

	}

	public static String ToConvertDateTimeFortimeStamp(String dateToBeConverted) {
		String dateToBeReturned=null;
		SimpleDateFormat dateFormatConvertedInTo=new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		SimpleDateFormat formatToBeConvertedFrom=new SimpleDateFormat("dd/MMM/yyyy 00:00:00");
		try {
			dateToBeReturned=dateFormatConvertedInTo.format(formatToBeConvertedFrom.parse(dateToBeConverted));
			System.out.println(dateToBeReturned);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateToBeReturned;

	}


	public static String getMonthName(int month) {
		ArrayList<String> monthsArrayList=new ArrayList<String>();
		monthsArrayList.add("January");
		monthsArrayList.add("February");
		monthsArrayList.add("March");
		monthsArrayList.add("April");
		monthsArrayList.add("May");
		monthsArrayList.add("June");
		monthsArrayList.add("July");
		monthsArrayList.add("August");
		monthsArrayList.add("September");
		monthsArrayList.add("October");
		monthsArrayList.add("November");
		monthsArrayList.add("December");
		return monthsArrayList.get(month);

	}
	public static int getMonth(String month) {
		int mon=1;
		if(month.equalsIgnoreCase("jan"))
		{
			mon= 1;
		}
		else if(month.equalsIgnoreCase("feb"))
		{
			mon= 2;
		}
		else if(month.equalsIgnoreCase("mar"))
		{
			mon= 3;
		}
		else if(month.equalsIgnoreCase("apr"))
		{
			mon= 4;
		}
		else if(month.equalsIgnoreCase("may"))
		{
			mon= 5;
		}
		else if(month.equalsIgnoreCase("jun"))
		{
			mon= 6;
		}
		else if(month.equalsIgnoreCase("jul"))
		{
			mon= 7;
		}
		else if(month.equalsIgnoreCase("aug"))
		{
			mon= 8;
		}
		else if(month.equalsIgnoreCase("sep"))
		{
			mon= 9;
		}
		else if(month.equalsIgnoreCase("oct"))
		{
			mon= 10;
		}
		else if(month.equalsIgnoreCase("nov"))
		{
			mon= 11;
		}
		else if(month.equalsIgnoreCase("dec"))
		{
			mon= 12;
		}
		return  mon;
	}


	public static String addDaysToDate(String date,String days,String format)
	{
		return DateFunction.addDaysToDate(date,days,format,format);
	}
	public static String addDaysToDate(String date,String days,String currentDateformat,String returnDateFormat)
	{
		Calendar cal  = Calendar.getInstance();
		SimpleDateFormat curdateFormat=new SimpleDateFormat(currentDateformat);
		SimpleDateFormat retdateFormat=new SimpleDateFormat(returnDateFormat);
		try {
			cal.setTime(curdateFormat.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		cal.add(Calendar.DATE,Integer.parseInt(days));
		String expDateString = retdateFormat.format(cal.getTime());
		System.out.println(expDateString);
		return expDateString;
	}
	public static String ToConvertDateFormat(String dateToBeConverted,String formatCuurent,String formatConverted) {
		calendar=Calendar.getInstance();
		String dateToBeReturned="";
		SimpleDateFormat formatToBeConvertedFrom=new SimpleDateFormat(formatCuurent);
		SimpleDateFormat dateFormatConvertedInTo=new SimpleDateFormat(formatConverted);
		try {
			dateToBeReturned=dateFormatConvertedInTo.format(formatToBeConvertedFrom.parse(dateToBeConverted));
			System.out.println(dateToBeReturned);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dateToBeReturned;

	}
	public static long noOfDays(String fromDate,String toDate,String format)
	{
		long diff=0;
		SimpleDateFormat myFormat = new SimpleDateFormat(format);
		String inputString1 = fromDate;
		String inputString2 = toDate;

		try {
			Date date1 = myFormat.parse(inputString1);
			Date date2 = myFormat.parse(inputString2);
			diff = date2.getTime() - date1.getTime();
			System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
	}
	public static String setdate(Context context,final EditText editText) {
		calendar=Calendar.getInstance();
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dpd1 = new DatePickerDialog(context,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
										  int monthOfYear, int dayOfMonth) {
						// Display Selected date in textbox
						String strDate=(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth))+"/"+(((monthOfYear+1)<10?("0"+(monthOfYear+1)):(monthOfYear+1)))+"/"+year;
						editText.setText(strDate);
					}
				}, mYear, mMonth, mDay);
		dpd1.show();
		return "";
	}
	public static String setTime(Context context,final EditText editText) {
		calendar=Calendar.getInstance();
		mHour = calendar.get(Calendar.HOUR_OF_DAY);
		mMinute = calendar.get(Calendar.MINUTE);

		// Launch Time Picker Dialog
		TimePickerDialog tpd = new TimePickerDialog(context,
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view, int hourOfDay,
										  int minute) {
						// Display Selected time in textbox
						String strTime=(hourOfDay<10?("0"+hourOfDay):hourOfDay)+":"+(minute<10?("0"+minute):minute);
						editText.setText((hourOfDay<10?("0"+hourOfDay):hourOfDay)+":"+(minute<10?("0"+minute):minute));
					}
				}, mHour, mMinute, true);
		tpd.show();

		return "";
		// TODO Auto-generated method stub

	}
	public static void  setTargetDate(Context context,final EditText editText) {
		final int i=0;
		final Activity activity=(Activity)context;
		calendar=Calendar.getInstance();
		mHour = calendar.get(Calendar.HOUR_OF_DAY);
		mMinute = calendar.get(Calendar.MINUTE);
		mYear = calendar.get(Calendar.YEAR);
		mMonth = calendar.get(Calendar.MONTH);
		mDay = calendar.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dpd = new DatePickerDialog(context,
				new DatePickerDialog.OnDateSetListener() {
					SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yy");
					@Override
					public void onDateSet(DatePicker view, int year,
										  int monthOfYear, int dayOfMonth) {
						String strDate=(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth))+"/"+(((monthOfYear+1)<10?("0"+(monthOfYear+1)):(monthOfYear+1)))+"/"+year;
						Date filledDate = null;Date currentDate=null;
						try {
							currentDate=calendar.getTime();
							filledDate = dateFormat.parse(strDate);
						} catch (ParseException e) {
							e.printStackTrace();
						}
						System.out.println(filledDate.getTime()+"   "+currentDate.getTime()+" "+(filledDate.getTime()-currentDate.getTime()));
						int i=(int)((filledDate.getTime()-currentDate.getTime()));
						if(i<0)
						{
							alertOkDialog= AlertOkDialog.newInstance("You can't fill date of today or lesser\n\nPlease fill date of tomorrow or letter");
							alertOkDialog.show(activity.getFragmentManager(), "my alert");
						}
						else {
							editText.setText(strDate);
						}

					}
				}, mYear, mMonth, mDay);
		dpd.show();
		// TODO Auto-generated method stub

	}
	public static String getCurrentTimestamp()
	{
		calendar=Calendar.getInstance();
		SimpleDateFormat formatToBeConvertedFrom=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatToBeConvertedFrom.format(calendar.getTime());
	}
	public static String getCurrentDateByFromat(String format)
	{
		calendar=Calendar.getInstance();
		SimpleDateFormat formatToBeConvertedFrom=new SimpleDateFormat(format);
		return formatToBeConvertedFrom.format(calendar.getTime());
	}
	public static String getCurrentTimestamp(String fromat)
	{
		calendar=Calendar.getInstance();
		SimpleDateFormat formatToBeConvertedFrom=new SimpleDateFormat(fromat);
		return formatToBeConvertedFrom.format(calendar.getTime());
	}
	public static Map<String,Integer> getCalculatedMonth(String noOfMonths)
	{
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH,Integer.parseInt(noOfMonths));
		Map<String,Integer> map=new HashMap<String, Integer>();
		map.put("MONTH", calendar.get((Calendar.MONTH)));
		map.put("YEAR", calendar.get((Calendar.YEAR)));
		return map;
	}




	public boolean isTimeFrameExist()
	{
		boolean isTimeFrameAvail = false;
		try {
			String string1 = "20:00";
			Date time1 = new SimpleDateFormat("HH:mm").parse(string1);
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(time1);

			String string2 = "8:00";
			Date time2 = new SimpleDateFormat("HH:mm").parse(string2);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(time2);
//            calendar2.add(Calendar.DATE, 1);


			Calendar calendar3 = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
			Date currentLocalTime = calendar3.getTime();
			DateFormat date = new SimpleDateFormat("HH:mm");
// you can get seconds by adding  "...:ss" to it
			date.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));

			String lockdsrtime = date.format(currentLocalTime);
			//Calendar calendar3 = Calendar.getInstance();
			Date d = new SimpleDateFormat("HH:mm").parse(lockdsrtime);
			calendar3.setTime(d);
			Date x = calendar3.getTime();
			if ((calendar3.get(Calendar.DATE) == calendar1.get(Calendar.DATE) && calendar3.get(Calendar.DATE) == calendar2.get(Calendar.DATE) ) &&
					(x.before(calendar1.getTime()) && x.after(calendar2.getTime()))) {
				//checkes whether the current time is between 14:49:00 and 20:11:13.

				isTimeFrameAvail = true;
			}
			else
			{
				isTimeFrameAvail = false;
			}
//            calendar3.add(Calendar.DATE, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return isTimeFrameAvail;
	}
}