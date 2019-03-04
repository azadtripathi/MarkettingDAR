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
import com.dm.model.PersonInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 5/2/2017.
 */
public class CustomPersonInfoArrayAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    int rowlayout;
    LayoutInflater inflater;
    private ArrayList<PersonInfo> data = null;

    protected int count;

    public CustomPersonInfoArrayAdapter(Context context, ArrayList<PersonInfo> data, int rowlayout) {
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
    public PersonInfo getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder {
        TextView name,phone,email;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if(rowlayout==R.layout.person_contact_information){
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.person_contact_information, null);
                // Locate the TextView in listview_item.xml
                holder.name = (TextView) view.findViewById(R.id.pName);
                holder.phone = (TextView) view.findViewById(R.id.pPhone);
                holder.email = (TextView) view.findViewById(R.id.pEmail);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.name.setText(((data.get(position).getName().length()<1) ? "Name" : data.get(position).getName()));
            holder.phone.setText(((data.get(position).getPhone().length()<1) ? "Phone" : data.get(position).getPhone()));
            holder.email.setText(((data.get(position).getEmail().length()<1)) ? "Website" : data.get(position).getEmail());
        }
        return view;
    }
}

