package com.dm.library;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class SpinnerAdaptor {
ArrayAdapter<String> adapter;Spinner spinner;ArrayList<String> arrayList;
	public static void setDataAdaptor(ArrayAdapter<String> adapter,ArrayList<String> arrayList,Spinner spinner,Context context)
	{
		adapter=new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, arrayList);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
	}
}
