package com.dm.library;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.crmdm_app.R;
import com.dm.model.AddContactModel;
import com.dm.model.Owner;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 5/2/2017.
 */
public class CustomAdapterCRMStreamInfo extends BaseAdapter {

    // Declare Variables
    Context mContext;
    int rowlayout;
    LayoutInflater inflater;
    private ArrayList<Owner> data = null;

    protected int count;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    public CustomAdapterCRMStreamInfo(Context context, ArrayList<Owner> data,int rowlayout) {
        this.rowlayout=rowlayout;
        mContext = context;
        this.data = data;
        inflater = LayoutInflater.from(mContext);


    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Owner getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder {
        TextView spinnerAdapterText;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if(rowlayout==R.layout.spinner_adapter_view){
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.spinner_adapter_view, null);
                // Locate the TextView in listview_item.xml
                holder.spinnerAdapterText = (TextView) view.findViewById(R.id.spinnerAdapterText);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            try {
                holder.spinnerAdapterText.setText(data.get(position).getName());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return view;
    }
}

