package com.dm.library;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ReadValuesFromFile {
ArrayList<String> valuesArrayList,readValuesArrayList;ValueReadFromFile valueReadFromFile;ExceptionData exceptionData;
ExceptionData dataExceptionData;
public ReadValuesFromFile(ValueReadFromFile valueReadFromFile)
{
	this.valueReadFromFile=valueReadFromFile;
	//this.exceptionData=exceptionData;
	readDatabaseCredential();
}
	public void readDatabaseCredential() {
		valuesArrayList=new ArrayList<String>();
		readValuesArrayList=new ArrayList<String>();
		 File dir = Environment.getExternalStorageDirectory();
        File file = new File(dir,"connection/constr.txt");
         if(file.exists())   // check if file exist
         {
         	  //Read text from file
             StringBuilder text = new StringBuilder();
           
             try {
                 BufferedReader br = new BufferedReader(new FileReader(file));
                 String line;
                 while ((line = br.readLine()) != null){ 
                    text.append(line);
                    valuesArrayList.add(line.toString());
                    text.append('\n');
                 }
              
             }
             catch (IOException e) {
                 //You'll need to add proper error handling here
           	  System.out.println(e.getMessage());
             }
             for (int i = 0; i < valuesArrayList.size(); i++) {
            	 System.out.println(valuesArrayList.size());
				Log.v("dd", DecriptText.DecryptData(valuesArrayList.get(i)));
				readValuesArrayList.add(DecriptText.DecryptData(valuesArrayList.get(i)));
			}
             if(readValuesArrayList.size()>0)
             {
             valueReadFromFile.setValue(readValuesArrayList);
             }
             else
             {valueReadFromFile.setValue(readValuesArrayList);
            	// exceptionData.setExceptionData("Connection File does not exits", "Not Found");
             }
         }
         else
         {
        	 valueReadFromFile.setValue(readValuesArrayList);
         	System.out.println("Server file does not exist");
         	//exceptionData.setExceptionData("Connection File does not exits", "Not Found");
         	
         }
	
}
public interface ValueReadFromFile
{
	public void setValue(ArrayList<String> values);
}
}
