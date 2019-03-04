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

import java.util.ArrayList;

/**
 * Created by Administrator on 5/2/2017.
 */
public class NotificationPannelAdaptor extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private ArrayList<NotificationData> numberlist = null;

    protected int count;

    public NotificationPannelAdaptor(Context context, ArrayList<NotificationData> numberlist) {
        mContext = context;
        this.numberlist = numberlist;
        inflater = LayoutInflater.from(mContext);


    }

    public class ViewHolder {
        TextView msgText;
        LinearLayout linearLayout;

    }

    @Override
    public int getCount() {
        return numberlist.size();
    }

    @Override
    public NotificationData getItem(int position) {
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
            view = inflater.inflate(R.layout.notification_pannel_list_row, null);
            // Locate the TextView in listview_item.xml
            holder.msgText = (TextView) view.findViewById(R.id.nplrmsg);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextView
        holder.msgText.setText(numberlist.get(position).getMsg());
        if(numberlist.get(position).getStatus().equalsIgnoreCase("true"))
        {
            view.setBackgroundResource(R.drawable.dk_gray_border);
        }
        else{
            view.setBackgroundResource(R.drawable.dk_lightblue_border);
        }

        // Listen for ListView Item Click


        return view;
    }
}
