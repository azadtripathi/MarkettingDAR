package com.dm.library;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.crmdm_app.R;
import com.dm.model.Dropdown;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

/**
 * Created by Dataman on 3/27/2017.
 */
public class LoadActivity {
    Context context;
    ProgressDialog progressDialog;
    public LoadActivity(Context context)
    {
        this.context=context;
    }

    public ArrayList<String> getNameList(ArrayList<Dropdown> dropdownlist)
    {
        ArrayList<String> nameList=new ArrayList<String>();

        for(int i=0;i<dropdownlist.size();i++)
        {
            dropdownlist.get(i).getId();
            nameList.add(dropdownlist.get(i).getName());
        }

        return nameList;
    }

    public ArrayList<String> getIdList(ArrayList<Dropdown> dropdownlist)
    {
        ArrayList<String> nameList=new ArrayList<String>();

        for(int i=0;i<dropdownlist.size();i++)
        {
            dropdownlist.get(i).getId();
            nameList.add(dropdownlist.get(i).getId());
        }

        return nameList;
    }

    public ArrayAdapter<String> getAdapter(ArrayList<String> namelist)
    {
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(context, R.layout.adapterdropdown, namelist);
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    void animateText(String jk,TextView tv,LinearLayout layout)
    {
        if(!jk.equalsIgnoreCase("")){
            Animation slide_up = AnimationUtils.loadAnimation(context, R.anim.slide_up);
            tv.setVisibility(View.VISIBLE);
            tv.startAnimation(slide_up);
            tv.setTextColor(Color.parseColor("#FF4081"));
            layout.setBackgroundColor(Color.parseColor("#FF4081"));
        }
    }

    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }
    public String getRealPathFromURI(Uri contentUri, Activity activity) {

//        String[] proj = { MediaStore.Images.Media.DATA };
//        Cursor cursor = activity.managedQuery(contentUri, proj, null, null, null);
//        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//        cursor.moveToFirst();
//        return cursor.getString(column_index);
        Cursor cursor = activity.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file
            // path
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            String path = cursor.getString(idx);
            cursor.close();
            return path;
        }
    }

    public Bitmap fitImage(Bitmap bm)
    { try {
        int maxHeight = 500;
        int maxWidth = 500;
        float scale = Math.min(((float) maxHeight / bm.getWidth()), ((float) maxWidth / bm.getHeight()));
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    }
    catch(Exception e){}
      return bm;
    }


    public String getTotalAmount(double Cases1,double Unit,double Rate,double StdPack)
    {
        double Amount=0.00;
        try {
            double TotalQty = ((Cases1 * StdPack) + Unit);
            Amount = (TotalQty * Rate) + 0.00;

        }
        catch(Exception e){
            System.out.print(e);

        }

        return round(new BigDecimal(Amount),2,false).toPlainString();
    }

    public static BigDecimal round(BigDecimal d, int scale, boolean roundUp) {
        int mode = (roundUp) ? BigDecimal.ROUND_UP : BigDecimal.ROUND_DOWN;
        return d.setScale(scale, mode);
    }
   public String getSelected(String str)
   {
       String[] words=str.split("\\,");
       int l1=words.length;
       return (l1>3?l1+"selected":str);
   }
    public String getStockQty(double Cases1,double Unit,double StdPack)
    {
        double Amount=0.00;
        try {
            double TotalQty = ((Cases1 * StdPack) + Unit);
            Amount = TotalQty + 0.00;
        }
        catch(Exception e){
            System.out.print(e);

        }
        return String.valueOf(Amount);
    }

}
