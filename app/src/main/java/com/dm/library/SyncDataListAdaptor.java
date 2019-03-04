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
import com.dm.model.NotificationData;
import com.dm.model.SyncStateData;

import java.util.ArrayList;

/**
 * Created by Dataman on 8/18/2017.
 */

public class SyncDataListAdaptor extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private ArrayList<SyncStateData> numberlist = null;

    protected int count;

    public SyncDataListAdaptor(Context context, ArrayList<SyncStateData> numberlist) {
        mContext = context;
        this.numberlist = numberlist;
        inflater = LayoutInflater.from(mContext);


    }

    public class ViewHolder {
        TextView msgText,sync_date_time_label;
        LinearLayout linearLayout;

    }

    @Override
    public int getCount() {
        return numberlist.size();
    }

    @Override
    public SyncStateData getItem(int position) {
        return numberlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.sync_data_custom_item, null);
            // Locate the TextView in listview_item.xml
            holder.msgText = (TextView) view.findViewById(R.id.displayNameLabel);
            holder.sync_date_time_label = (TextView)view.findViewById(R.id.sync_date_time_label);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextView
        holder.msgText.setText(numberlist.get(position).getDisplayName());
        holder.sync_date_time_label.setText(" "+numberlist.get(position).getLastUpdateDate());
       /* if(numberlist.get(position).getStatus().equalsIgnoreCase("true"))
        {
            view.setBackgroundColor(Color.parseColor("#f0f0f0"));
        }
        else
        {
            view.setBackgroundColor(Color.parseColor("#d8d8d8"));
        }*/

        // Listen for ListView Item Click


        return view;
    }
}
