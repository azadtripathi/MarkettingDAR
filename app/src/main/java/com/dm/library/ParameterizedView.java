package com.dm.library;

import android.content.Context;

import com.dm.controller.AppDataController;
import com.dm.model.AppData;

import java.util.ArrayList;

/**
 * Created by dataman on 19-Mar-17.
 */
public class ParameterizedView {
    Context context;
    public ParameterizedView(Context context)
    {
        this.context=context;
    }
    public boolean showView()
    {
        boolean flag=false;
        AppDataController appDataController=new AppDataController(context);
        appDataController.open();
        ArrayList<AppData> appDataArray = appDataController.getAppTypeFromDb();
        appDataController.close();
        String appType="";
        for(int i=0;i<appDataArray.size();i++)
        {
            appType=appDataArray.get(i).getAppType().trim();

        }

        if(appType.equalsIgnoreCase("1")){
            flag=true;
        }

        else {
            flag=false;

        }
        return flag;
    }

}
