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
import com.dm.model.MileStoneData;
import com.dm.model.NotificationData;

import java.util.ArrayList;

/**
 * Created by Dataman on 11/5/2017.
 */

public class MileStoneAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private ArrayList<MileStoneData> numberlist = null;

    protected int count;

    public MileStoneAdapter(Context context, ArrayList<MileStoneData> numberlist) {
        mContext = context;
        this.numberlist = numberlist;
        inflater = LayoutInflater.from(mContext);


    }

    public class ViewHolder {
        TextView milestone,rate,qty,amount;
        LinearLayout linearLayout;

    }

    @Override
    public int getCount() {
        return numberlist.size();
    }

    @Override
    public MileStoneData getItem(int position) {
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
            view = inflater.inflate(R.layout.milestone_item_layout, null);
            // Locate the TextView in listview_item.xml
            holder.rate = (TextView) view.findViewById(R.id.rate);
            holder.amount = (TextView)view.findViewById(R.id.amtLabel);
            holder.qty = (TextView)view.findViewById(R.id.quantity);
            holder.milestone = (TextView)view.findViewById(R.id.milesStoneLabel);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextView
        holder.milestone.setText(numberlist.get(position).getMilstone());
        holder.rate.setText(numberlist.get(position).getRate());
        holder.amount.setText(numberlist.get(position).getAmount());
        holder.qty.setText(numberlist.get(position).getQty());

        // Listen for ListView Item Click


        return view;
    }
}
