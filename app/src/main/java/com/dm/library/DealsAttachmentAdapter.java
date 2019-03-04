package com.dm.library;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.crmdm_app.R;
import com.dm.model.AttachmentData;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Dataman on 11/20/2017.
 */

public class DealsAttachmentAdapter extends BaseAdapter {

    Context context;
    List<AttachmentData> rowItem;
    View listView;
    boolean checkState[];
    View.OnClickListener onClickListener;
    ViewHolder holder;

    public DealsAttachmentAdapter(Context context, List<AttachmentData> rowItem,View.OnClickListener onClickListener) {

        this.context = context;
        this.rowItem = rowItem;
        this.onClickListener=onClickListener;
        checkState = new boolean[rowItem.size()];

    }

    @Override
    public int getCount() {

        return rowItem.size();
    }

    @Override
    public AttachmentData getItem(int position) {

        return rowItem.get(position);

    }

    @Override
    public long getItemId(int position) {

        return rowItem.indexOf(getItem(position));

    }

    public class ViewHolder {
        TextView tvItemName;
        CheckBox check;
        ImageView imageView;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        holder = new ViewHolder();
        final AttachmentData itm = rowItem.get(position);
        LayoutInflater layoutInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if (view == null) {

            listView = new View(context);
            listView = layoutInflater.inflate(R.layout.attachment_list_data,
                    parent, false);

            holder.tvItemName = (TextView) listView
                    .findViewById(R.id.textView1);
            holder.check = (CheckBox) listView.findViewById(R.id.checkBox1);
            holder.imageView = (ImageView) listView.findViewById(R.id.attachmentImage);
            listView.setTag(holder);

        } else {
            listView = (View) view;
            holder = (ViewHolder) listView.getTag();
        }
        holder.check.setTag(itm.getFileId());

        String text = "<a href='"+itm.getFileUrl()+"'>"+itm.getFileName()+" </a>";
        holder.tvItemName.setClickable(true);
        holder.tvItemName.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);

        holder.tvItemName.setMovementMethod(LinkMovementMethod.getInstance());
        holder.tvItemName.setText(Html.fromHtml(text));
        holder.check.setOnClickListener(onClickListener);
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
            imageLoader.displayImage(itm.getImageIcon(),holder.imageView,options);			  }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        //holder.check.setChecked(checkState[position]);

       /* holder.check.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                for(int i=0;i<checkState.length;i++)
                {
                    if(i==position)
                    {
                        checkState[i]=true;
                        holder.check.setChecked(true);
                    }
                    else
                    {
                        checkState[i]=false;
                        holder.check.setChecked(false);
                    }
                }
                notifyDataSetChanged();

            }
        });*/
        return listView;
    }}
