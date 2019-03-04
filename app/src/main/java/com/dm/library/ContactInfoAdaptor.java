package com.dm.library;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.crmdm_app.ContactInfoModel;
import com.dm.crmdm_app.R;
import com.dm.model.MileStoneData;

import java.util.ArrayList;

/**
 * Created by Dataman on 11/8/2017.
 */

public class ContactInfoAdaptor extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private ArrayList<ContactInfoModel> numberlist = null;

    protected int count;

    public ContactInfoAdaptor(Context context, ArrayList<ContactInfoModel> numberlist) {
        mContext = context;
        this.numberlist = numberlist;
        inflater = LayoutInflater.from(mContext);


    }

    public class ViewHolder {
        TextView name,phone,email,nm,em,mb;//,rate,qty,amount;
        LinearLayout linearLayout;

    }

    @Override
    public int getCount() {
        return numberlist.size();
    }

    @Override
    public ContactInfoModel getItem(int position) {
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
            view = inflater.inflate(R.layout.new_person_info, null);
            // Locate the TextView in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.pName);
            holder.phone = (TextView) view.findViewById(R.id.pPhone);
            holder.email = (TextView) view.findViewById(R.id.pEmail);
            holder.mb = (TextView)view.findViewById(R.id.mbLabel);
            holder.nm = (TextView)view.findViewById(R.id.nmLable);
            holder.em = (TextView)view.findViewById(R.id.emLabel);
            holder.nm.setTextColor(Color.BLACK);
            holder.em.setTextColor(Color.BLACK);
            holder.mb.setTextColor(Color.BLACK);

            holder.name.setTextColor(Color.BLACK);
            holder.email.setTextColor(Color.BLACK);
            holder.phone.setTextColor(Color.BLACK);


            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextView


        holder.name.setText(numberlist.get(position).getName());
        holder.email.setText(numberlist.get(position).getEmail());
        holder.phone.setText(numberlist.get(position).getMobile());


        // Listen for ListView Item Click


        return view;
    }
}
