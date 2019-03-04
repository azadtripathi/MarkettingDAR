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
import com.dm.model.DashboardMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class DistributordynamicMenuBaseAdapter extends BaseAdapter {
	Context context;
	private static ArrayList<DashboardMenu> dashboardMenuArrayList;
	private LayoutInflater mInflater;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	public DistributordynamicMenuBaseAdapter(Context context, ArrayList<DashboardMenu> results) {
			 dashboardMenuArrayList = results;
	  mInflater = LayoutInflater.from(context);
		this.context=context;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.leave_request)
				.showImageForEmptyUri(R.drawable.leave_request)
				.showImageOnFail(R.drawable.leave_request)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();

		imageLoader = ImageLoader.getInstance();
	 }

	 public int getCount() {
	  return dashboardMenuArrayList.size();
	 }

	 public Object getItem(int position) {
		 return dashboardMenuArrayList.get(position);
	 }

	 public long getItemId(int position) {
	  return position;
	 }

	 static class ViewHolder {
	  	LinearLayout layoutparent,layoutchild;
		TextView txtparent,txtchild;
		 ImageView imageView;
	 }

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		  ViewHolder holder;
		  if (convertView == null) {
		   		convertView = mInflater.inflate(R.layout.custom_row_view, null);
		   		holder = new ViewHolder();

			  holder.imageView = (ImageView)convertView.findViewById(R.id.icon);
		   		holder.layoutparent = (LinearLayout) convertView.findViewById(R.id.layoutparent);
		  		holder.layoutchild = (LinearLayout)convertView.findViewById(R.id.layoutchild);

			  	holder.txtparent = (TextView)convertView.findViewById(R.id.txtparent);
			  	holder.txtchild = (TextView)convertView.findViewById(R.id.txtchild);
		   		convertView.setTag(holder);
		  }
		  else
		  {
		   		holder = (ViewHolder) convertView.getTag();
		  }
		try {

			if (dashboardMenuArrayList.get(position).getParentMenu().equals("1")) {
				holder.txtparent.setText(dashboardMenuArrayList.get(position).getparent_name());
				//holder.txtchild.setText(dashboardMenuArrayList.get(position).getchild_name());
				holder.layoutparent.setBackgroundColor(Color.parseColor("#3F51B5"));
				holder.layoutparent.setEnabled(false);
				holder.layoutparent.setVisibility(View.GONE);
				holder.layoutchild.setVisibility(View.GONE);
			} else {
				holder.txtchild.setText(dashboardMenuArrayList.get(position).getchild_name());
				holder.layoutparent.setVisibility(View.GONE);
				holder.layoutchild.setVisibility(View.VISIBLE);
				try {
					imageLoader.displayImage(dashboardMenuArrayList.get(position).getIcon().trim().replace(" ", "%20"), holder.imageView, options);			  }
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		catch (Exception e){}
		return convertView;
//		String page_name = (dashboardMenuArrayList.get(position).getPage_NAme().trim());
//		String child_name = holder.txtchild.getText().toString();

		/*convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Class c2=null;
				try {
					c2 = Class.forName("com.dm.crmdm_app." + dashboardMenuArrayList.get(position).getPage_NAme().trim());

				}
				catch(ClassNotFoundException e)
				{
					System.out.println(e);
				}
				if(c2!=null)
				{
					new IntentSend(context, c2).toSendAcivity();
				}
			}
		});*/
	}
	}