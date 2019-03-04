package com.dm.library;

public interface ExceptionData {
	public void setExceptionData(String message, String ExceptionType);

	public void setInternetExceptionData(String message, String ExceptionType);
	public void goToDashboardData(String message, String ExceptionType);
}