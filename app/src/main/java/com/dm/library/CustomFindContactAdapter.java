package com.dm.library;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.crmdm_app.R;
import com.dm.model.AddContactModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Administrator on 5/2/2017.
 */
public class CustomFindContactAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    int rowlayout;
    LayoutInflater inflater;
    private ArrayList<AddContactModel> data = null;
DisplayImageOptions options;
    ImageLoader imageLoader;
    protected int count;

    public CustomFindContactAdapter(Context context, ArrayList<AddContactModel> data,int rowlayout) {
        this.rowlayout=rowlayout;
        mContext = context;
        this.data = data;
        inflater = LayoutInflater.from(mContext);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.ic_user)
                .showImageForEmptyUri(R.drawable.ic_user)
                .showImageOnFail(R.drawable.ic_user)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public AddContactModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder {
        TextView textViewfname,textViewlname,textViewtitle,textViewcompany,textViewphone,textViewemail,textViewurl,textViewaddress,textViewstatus;
        TextView textViewLead,textViewTaskDiscription,textViewOwner,textViewLeadStatus,textViewTag,textViewTaskStatus,textViewDate,textViewCompany;
        LinearLayout linearLayout,listItemViewlinearLAyout;
        ImageView dtView,userimageview;

    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if(rowlayout==R.layout.listview_find_add_contact){
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.listview_find_add_contact, null);
                // Locate the TextView in listview_item.xml
                holder.userimageview = (ImageView)view.findViewById(R.id.userimageview);
                holder.textViewfname = (TextView) view.findViewById(R.id.lfadFirstName);
                holder.textViewlname = (TextView) view.findViewById(R.id.lfadLastName);
                holder.textViewtitle = (TextView) view.findViewById(R.id.lfadJobTitle);
                holder.textViewcompany = (TextView) view.findViewById(R.id.lfacCompany);
                holder.textViewphone = (TextView) view.findViewById(R.id.lfadPhone);
                holder.textViewemail = (TextView) view.findViewById(R.id.lfacEmail);
                holder.textViewurl = (TextView) view.findViewById(R.id.lfadURL);
                holder.textViewaddress = (TextView) view.findViewById(R.id.lfacAddress);
                holder.textViewstatus = (TextView) view.findViewById(R.id.lfadStatus);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.textViewfname.setText(data.get(position).getFirstName());
            holder.textViewlname.setText(data.get(position).getLastName());
            holder.textViewtitle.setText(data.get(position).getJobTitle());
            holder.textViewcompany.setText(data.get(position).getComapy());
            holder.textViewphone.setText(data.get(position).getPhone());
            holder.textViewemail.setText(data.get(position).getEmail());
            holder.textViewurl.setText(data.get(position).getUrl());
            holder.textViewaddress.setText(data.get(position).getAddress());
            holder.textViewstatus.setText(data.get(position).getStatus());
            imageLoader.displayImage(data.get(position).getImageUrl(),holder.userimageview,options);


        }else if(rowlayout==R.layout.listview_crmstream){
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.listview_crmstream, null);
                // Locate the TextView in listview_item.xml
                holder.textViewLead = (TextView) view.findViewById(R.id.tvlead);
                holder.textViewTaskDiscription = (TextView) view.findViewById(R.id.tvtaskDescription);
                holder.textViewOwner = (TextView) view.findViewById(R.id.tvOwner);
                holder.textViewLeadStatus = (TextView) view.findViewById(R.id.tvLeadStatus);
                holder.textViewTag = (TextView) view.findViewById(R.id.tcTag);
                holder.textViewTaskStatus = (TextView) view.findViewById(R.id.tvTaskStatus);
                holder.textViewDate=(TextView)view.findViewById(R.id.taskdate);
                holder.textViewCompany=(TextView)view.findViewById(R.id.tvtaskCompany);
                holder.dtView = (ImageView)view.findViewById(R.id.dtView);
                holder.listItemViewlinearLAyout=(LinearLayout)view.findViewById(R.id.listItemView);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            if(data.get(position).getFlag().equalsIgnoreCase("L")){
                holder.listItemViewlinearLAyout.setBackgroundResource(R.drawable.dk_gray_border);
            }
            else{
                holder.listItemViewlinearLAyout.setBackgroundResource(R.drawable.dk_lightblue_border);
            }
            holder.textViewLead.setText(data.get(position).getLead());
            holder.textViewTaskDiscription.setText(data.get(position).getTaskDiscription());
            holder.textViewOwner.setText(data.get(position).getOwner());
            holder.textViewLeadStatus.setText(data.get(position).getLeadStatus());
            holder.textViewTag.setText(data.get(position).getTag());
            holder.textViewDate.setText(data.get(position).getTaskDate());
            if(data.get(position).getIstoday()==2)
            {
                holder.dtView.setColorFilter(Color.parseColor("#1b75bb"));

            }
            if(data.get(position).getIstoday()==1)
            {
                holder.dtView.setColorFilter(Color.parseColor("#EF851C"));
            }
            if(data.get(position).getIstoday()==3)
            {
                holder.dtView.setColorFilter(Color.parseColor("#c11b17"));
            }

            if(data.get(position).getComapy().equalsIgnoreCase("")){
                holder.textViewCompany.setVisibility(View.GONE);
            }

            else{
                holder.textViewCompany.setVisibility(View.VISIBLE);
                holder.textViewCompany.setText(data.get(position).getComapy());
            }
            //holder.textViewCompany.setText(data.get(position).getComapy().equalsIgnoreCase("")?"None":data.get(position).getComapy());
            //holder.textViewLeadStatus.setText(data.get(position).getStatus());
           //1 holder.textViewTag.setText(data.get(position).getTag());
           // holder.textViewTaskStatus.setText(data.get(position).getEmail());
            if(data.get(position).getStatus().equalsIgnoreCase("Open")){
                holder.textViewTaskStatus.setText(data.get(position).getStatus());
                holder.textViewTaskStatus.setTextColor(Color.parseColor("#79d867"));
            }
            else if(data.get(position).getStatus().equalsIgnoreCase("Close"))
            {
                holder.textViewTaskStatus.setText(data.get(position).getStatus());
                holder.textViewTaskStatus.setTextColor(Color.parseColor("#e41b17"));
            }
            else {
                holder.textViewTaskStatus.setText(data.get(position).getStatus());
            }
        }
        return view;
    }
}

