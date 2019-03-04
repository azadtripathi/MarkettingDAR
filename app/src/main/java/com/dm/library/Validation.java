package com.dm.library;

import android.content.Context;
import android.text.Editable;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation  {
	Context context;
	public static final  Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
//            "[a-zA-Z0-9+._%-+]{1,256}" +
//            "@" +
//            "[a-zA-Z0-9][a-zA-Z0-9-]{0,64}" +
//            "(" +
//            "." +
//            "[a-zA-Z0-9][a-zA-Z0-9-]{0,25}" +
//            ")+"

			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
					+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

	public Validation(Context context)
	{
		this.context=context;
	}


	public static boolean isEmailCorrect(String email)
	{
		
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
		}
	public int  vNum(Editable value)
	{
		int val=0;
		if(value.equals(null))
		{val=0;}
		else
		{
			try {
				val=Integer.parseInt(String.valueOf(value));
			}
			catch (NumberFormatException n)
			{
				val= 0;
			}

		}
		return val;
	}

	public boolean validateName(String str){


//			Pattern pattern = Pattern.compile("[a-zA-Z0-9]*");

		Pattern pattern = Pattern.compile("[a-zA-Z]*");
			Matcher matcher = pattern.matcher(str.substring(0, 1));

			if (!matcher.matches()) {
//				System.out.println("string '"+str + "' contains special character");
				return true;
			} else {
//				System.out.println("string '"+str + "' doesn't contains special character");
				return false;
			}

		}
	public Double vDec(String value)
	{
		Double val=0.00;
		if(value.equals(null))
		{
			DecimalFormat decim = new DecimalFormat("0.00");
			val = Double.parseDouble(decim.format(val));
		}
		else
		{
			try {
				DecimalFormat twoDForm = new DecimalFormat("#.##");
				val=Double.valueOf(twoDForm.format( Double.parseDouble(String.valueOf(value))));
			}
			catch (NumberFormatException n)
			{
				DecimalFormat decim = new DecimalFormat("0.00");
				val = Double.parseDouble(decim.format(val));
			}

		}
		return val;
	}

	public Double  vPDec(Editable value,int noplace)
	{
		Double val=0.00;
		if(value.equals(null) || value.equals("0") )
		{
			DecimalFormat decim = new DecimalFormat("0.00");
			val = Double.parseDouble(decim.format(val));
			}
		else
		{
			try {
				StringBuilder messages = new StringBuilder();
				for(int i=1;i<=noplace;i++)
				{
					messages.append("#");
				}
				String deci="#."+messages;
				System.out.println("Decimal Place "+deci);
				DecimalFormat twoDForm = new DecimalFormat(deci);
				val=Double.valueOf(twoDForm.format( Double.parseDouble(String.valueOf(value))));
				System.out.println("Before Decimal Place "+val);
				val=val.doubleValue();
				System.out.println("After Decimal Place "+val);
			}
			catch (NumberFormatException n)
			{
				DecimalFormat decim = new DecimalFormat("0.00");
				val = Double.parseDouble(decim.format(val));
			}

		}
		return val;
	}
	public String  vAlfNum(String value)
	{
		String val = "";
		if(value.isEmpty() || value.equalsIgnoreCase(null))
		{
			val="";
		}
		else
		{
			val = value;
//			System.out.println("Validate Remark0"+value);
//			val = value.replaceAll("\"", "");
//			System.out.println("Validate Remark1"+val);
			val = val.replaceAll("'","");
//			System.out.println("Validate Remark2"+val);
//			val = val.replaceAll(",","");
//			System.out.println("Validate Remark3"+val);
//			val = val.replaceAll(":","");
//			System.out.println("Validate Remark4"+val);
//			val = val.replaceAll(";","");
//			System.out.println("Validate Remark5"+val);
//			val = val.replaceAll("\\.", "");
//			System.out.println("Validate Remark6"+val);
//			val = val.replace("\\n", "");
//			System.out.println("Validate Remark7"+val);
		}
		return val;
	}
	public String  vAlNUmericDynamicFileds(String value)
	{
		String val = "";
		if(value.isEmpty() || value.equalsIgnoreCase(null))
		{
			val="";
		}
		else
		{
			val = value;
			val = value.replaceAll("\"", "");
			val = val.replaceAll("'","");
			val = val.replaceAll(",","");
			val = val.replaceAll(":","");
			val = val.replaceAll(";","");
			val = val.replaceAll("\\.", "");
			val = val.replace("\\n", "");
			val = val.replace("^", "");
			val = val.replace("&", "");
			val = val.replace("[", "");
			val = val.replace("]", "");
		}
		return val;
	}

	public String  vAlNUmericFileds(String value)
	{
		String val = "";
		if(value.isEmpty() || value.equalsIgnoreCase(null))
		{
			val="";
		}
		else
		{
			val = value;
			try{
				if(val.trim().length()==1){
					val = val.replaceAll("\\.","");
				}
				val = val.replaceAll(",","");
				val = val.replace("\\n", "");
			}
			catch (NumberFormatException e){}

		}
		return val;
	}
	public double  parseStringToDouble(String value)
	{
		double val = 0.00;
		if(value.isEmpty() || value.equalsIgnoreCase(null))
		{
			val = 0.00;
		}
		else
		{

			try{
				val = Double.parseDouble(value);
				if(val == 0 || val == 0.00 ){
					val = 0.00;
				}

			}
			catch (NumberFormatException e){
				val = 0.00;
			}

		}
		return val;
	}
	public static BigDecimal round(BigDecimal d, int scale, boolean roundUp) {
		int mode = (roundUp) ? BigDecimal.ROUND_UP : BigDecimal.ROUND_DOWN;
		return d.setScale(scale, mode);
	}

}

