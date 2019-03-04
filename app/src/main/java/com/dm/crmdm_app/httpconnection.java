package com.dm.crmdm_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class httpconnection {
	
	StringBuilder sb=null;
	InputStream is=null;
	String result="";
	
	public String get_httpvalue(String url, List<BasicNameValuePair> nvps, Context cx)
	{
		
		SharedPreferences mPreferences = cx.getSharedPreferences("AdnawabUser", Context.MODE_PRIVATE);
	    String sid=mPreferences.getString("sid","FAIL");
		
		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(url);
        
        
       try
        { 
              UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(nvps, HTTP.UTF_8);
              httppost.setEntity(p_entity);
              httppost.setHeader("Cookie",sid);
              HttpResponse response = client.execute(httppost);
              //Log.v(TAG, response.getStatusLine().toString());
              HttpEntity responseEntity = response.getEntity();
              //Log.v(TAG, "Set response to responseEntity");
              //Toast.makeText(getApplicationContext(),"recieve", Toast.LENGTH_LONG).show(); 
              is = responseEntity.getContent();
              
              
        }
        
        catch (Exception e) 
        { 
              
        } 
        
       
		try
        {
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String line="0";
            while ((line = reader.readLine()) != null)
            {

                sb.append(line + "\n");

            }
            is.close();

            result=sb.toString();
            }
        
        catch(Exception e)
        {

            Log.e("log_tag", "Error converting result "+e.toString());

        }
		
		return result;
	}

}
