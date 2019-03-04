package com.dm.library;

import com.dm.library.CustomArrayAdopter.MyHolder;

public abstract class DataTransfer {
	public DataTransfer()
	{}
	public MyHolder sendHolder(MyHolder holder)
	{return holder;}
}
