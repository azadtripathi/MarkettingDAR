package com.dm.library;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dm.controller.DsrController;
import com.dm.crmdm_app.R;
import com.dm.model.DashboardMenu;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

public class DynamicMenuBaseAdapter extends BaseAdapter {
	Context context; int row;
	Activity activity;
	DsrController dsrController;AlertOkDialog alertOkDialog;
	private static ArrayList<DashboardMenu> dashboardMenuArrayList;
	private LayoutInflater mInflater;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	public DynamicMenuBaseAdapter(Context context, ArrayList<DashboardMenu> results, int row, Activity activity) {
			 dashboardMenuArrayList = results;
	  mInflater = LayoutInflater.from(context);
		this.context=context;
		this.row=row;
		this.activity=activity;
		dsrController=new DsrController(context);
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
		  if (convertView == null)
		  {
		   		convertView = mInflater.inflate(R.layout.custom_row_view, null);
		   		holder = new ViewHolder();
		   		holder.layoutparent = (LinearLayout) convertView.findViewById(R.id.layoutparent);
		  		holder.layoutchild = (LinearLayout)convertView.findViewById(R.id.layoutchild);
			  	holder.txtparent = (TextView)convertView.findViewById(R.id.txtparent);
			  	holder.txtchild = (TextView)convertView.findViewById(R.id.txtchild);
				holder.imageView = (ImageView)convertView.findViewById(R.id.icon) ;
		   		convertView.setTag(holder);
		  }
		  else
		  {
		   		holder = (ViewHolder) convertView.getTag();
		  }

		  if (dashboardMenuArrayList.get(position).getParentMenu().equals("1"))
		  {

			  String str = dashboardMenuArrayList.get(position).getparent_name();


			  holder.txtparent.setText(dashboardMenuArrayList.get(position).getparent_name());
			  holder.txtparent.setAllCaps(false);
			  //holder.txtchild.setText(dashboardMenuArrayList.get(position).getchild_name());
			  holder.layoutparent.setBackgroundColor(Color.parseColor("#3F51B5"));

			  holder.layoutparent.setEnabled(false);
			  holder.layoutparent.setVisibility(View.VISIBLE);
			  holder.layoutchild.setVisibility(View.GONE);
		  }

		  else {
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
		//String page_name = (dashboardMenuArrayList.get(position).getPage_NAme());

		/*convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (dashboardMenuArrayList.get(position).getPage_NAme().trim().equals("DailySalesReport")) {
					dsrController.open();
					String isDataImported = dsrController.getIsDataImported();
					//	 dsrController.deleteOrderRow();
					dsrController.close();
					if (isDataImported.equals("true")) {
						(new IntentSend(context, DailySalesReport.class)).toSendAcivity();

//						 Class c=null;
//						 try {
//							 c = Class.forName("com.dm.crmdm_app.DailySalesReport");
//						 }
//						 catch(ClassNotFoundException e)
//						 {
//							 System.out.println(e);
//						 }
//						 Intent i=new Intent(getApplicationContext(),c);
//						 startActivity(i);
					} else {
						alertOkDialog = AlertOkDialog.newInstance("There is no data available  !\n\nPlease run Import tab from Synchronization");
						alertOkDialog.show(activity.getFragmentManager(), "syn");
					}


				} else {
					if(dashboardMenuArrayList.get(position).getPage_NAme().trim().equals("")){

					}
					try {
						Class c2 = null;
						try {
							c2 = Class.forName("com.dm.crmdm_app." + dashboardMenuArrayList.get(position).getPage_NAme().trim());

						} catch (ClassNotFoundException e) {
							System.out.println(e);
						}
						if (c2 != null) {
							new IntentSend(context, c2).toSendAcivity();
						}
					} catch (Exception e) {
						System.out.println(e);
					}
				}
			}
		});*/
		return convertView;
	}
	}