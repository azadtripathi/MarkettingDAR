package com.dm.library;

import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.crmdm_app.R;
import com.dm.model.MileStoneData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * Created by Dataman on 11/5/2017.
 */

public class DealsAttachment extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private ArrayList<MileStoneData> numberlist = null;

    protected int count;

    public DealsAttachment(Context context, ArrayList<MileStoneData> numberlist) {
        mContext = context;
        this.numberlist = numberlist;
        inflater = LayoutInflater.from(mContext);


    }

    public class ViewHolder {
        TextView milestone,rate,qty,amount;
        ImageView imageIcon;
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
            holder.imageIcon = (ImageView) view.findViewById(R.id.attachmentImageView);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextView
        String text = "<a href='"+numberlist.get(position).getFilePath()+"'>"+numberlist.get(position).getFileName()+" </a>";
        holder.milestone.setClickable(true);
        holder.imageIcon.setVisibility(View.VISIBLE);
        try {
            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_user)
                    .showImageForEmptyUri(R.drawable.ic_user)
                    .showImageOnFail(R.drawable.ic_user)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            //selectedImageBase64ImgString=allDataArray.get(i).getImageUrl();
            imageLoader.displayImage(numberlist.get(position).getImageIcon(),holder.imageIcon,options);			  }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        holder.milestone.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);

        holder.milestone.setMovementMethod(LinkMovementMethod.getInstance());
        holder.milestone.setTag(numberlist.get(position).getFilePath());
        holder.milestone.setText(Html.fromHtml(text));
        holder.rate.setVisibility(View.GONE);
        holder.amount.setVisibility(View.GONE);
        holder.qty.setVisibility(View.GONE);

        // Listen for ListView Item Click


        return view;
    }
}
