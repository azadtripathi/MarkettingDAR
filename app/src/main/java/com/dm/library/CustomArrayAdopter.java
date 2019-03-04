package com.dm.library;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TableRow;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.controller.HistoryPartyWiseController;
import com.dm.crmdm_app.ExpenseGroupEntry;
import com.dm.crmdm_app.ExpenseSummaryScreen;
import com.dm.crmdm_app.R;
import com.dm.database.DatabaseConnection;
import com.dm.model.AppData;
import com.dm.model.DashboardModel;
import com.dm.model.Distributor;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;


public  class CustomArrayAdopter extends ArrayAdapter<DashboardModel> {
	private final ArrayList<DashboardModel> dashboardModels;private String witchRow; private int rowLayout;private int textView;
	private final Context context;private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();private static String checkData;
	String dateText;private ArrayList<InfoRowdata> infodata;static String dataForCheck;AlertBoxWithView dialogWithView;final HashMap<Integer, String> savedQtyData = new HashMap<Integer, String>();
	final HashMap<Integer, String> savedRateData = new HashMap<Integer, String>();String[] postValue;
	DataTransferInterface dataTransferInterface;ArrayList<MyHolder> arrayList;
	DataTransfer dataTransfer;HolderListener holderListener;String qtyOfProduct;ExpenseTransferInterface expenseTransferInterface;ProgressDialog progressDialog;int positionImage;
	HistoryPartyWiseController historyPartyWiseController;SharedPreferences preferences2;
	  private String[] amountEdit_values;AppDataController appDataController1;ArrayList<AppData> appDataArray;
      public String[] qtyEdit_values;String path="NA",prefix="",filePath="";String AndroidId="";String table="",server="";
	String party;
	DisplayImageOptions options;
	public CustomArrayAdopter(Context context, ArrayList<DashboardModel> dashboardModels, int rowLayout, int textView, HolderListener dataTransferInterface) {
		super(context, rowLayout, textView, dashboardModels);
		this.holderListener=dataTransferInterface; 
		this.context=context;
		this.dashboardModels=dashboardModels;
		this.textView=textView;
		this.rowLayout=rowLayout;
		this.qtyEdit_values = new String[dashboardModels.size()];
		this.amountEdit_values = new String[dashboardModels.size()];
		this.postValue = new String[dashboardModels.size()];
		historyPartyWiseController=new HistoryPartyWiseController(context);
		preferences2=context.getSharedPreferences("RETAILER_SESSION_DATA",Context.MODE_PRIVATE);
		arrayList=new ArrayList<MyHolder>();
		infodata = new ArrayList<InfoRowdata>();
        for (int i = 0; i < dashboardModels.size(); i++) {
            infodata.add(new InfoRowdata(false, i));
            savedQtyData.put(i, "");
            savedRateData.put(i, "");
       }

		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(0)
				.showImageForEmptyUri(0)
				.showImageOnFail(0)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
	}

	public CustomArrayAdopter(Context context, ArrayList<DashboardModel> dashboardModels, int rowLayout, int textView, HolderListener dataTransferInterface, ExpenseTransferInterface expenseTransferInterface) {
		super(context, rowLayout, textView, dashboardModels);
		this.holderListener=dataTransferInterface;
		this.context=context;
		this.dashboardModels=dashboardModels;
		this.textView=textView;
		this.rowLayout=rowLayout;
		this.qtyEdit_values = new String[dashboardModels.size()];
		this.amountEdit_values = new String[dashboardModels.size()];
		this.postValue = new String[dashboardModels.size()];
		historyPartyWiseController=new HistoryPartyWiseController(context);
		this.expenseTransferInterface=expenseTransferInterface;
		arrayList=new ArrayList<MyHolder>();
		infodata = new ArrayList<InfoRowdata>();
		for (int i = 0; i < dashboardModels.size(); i++) {
			infodata.add(new InfoRowdata(false, i));
			savedQtyData.put(i, "");
			savedRateData.put(i, "");
		}
	}

	public int getCount() {

	    return dashboardModels.size();
	}

	public DashboardModel getItem(int position) {
	    // TODO Auto-generated method stub
	    return dashboardModels.get(position);
	}

	public long getItemId(int position) {
	    // TODO Auto-generated method stub
	    return position;
	}
public static void setdata(String data)
{
	dataForCheck=data;
	Log.d("check check", dataForCheck);
}
	public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }
    
@Override
public View getView(final int position, View convertView, ViewGroup parent) {
	View row = convertView;final MyHolder holder;
	if(rowLayout== R.layout.dashboard_row){
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.dashboard_row,parent,false);
			row.setBackgroundResource(R.drawable.input_dashborad_item_style);
			holder=new MyHolder(row);
			row.setTag(holder);
		}
		else
		{
			holder=(MyHolder)row.getTag();
			Log.d("mani","old");
		}
		System.out.println(dashboardModels.get(position).getDashboardItemName()+" "+dashboardModels.get(position).getDashboardItemdescription()+" "+dashboardModels.get(position).getImageResourceId());
		holder.dashboardItemName.setText(dashboardModels.get(position).getDashboardItemName());
		holder.dashboardItemIcon.setImageResource(dashboardModels.get(position).getImageResourceId());
	}
	
//	else if(rowLayout== R.layout.find_beat_master_list)
//	{
//		if(row==null)
//		{
//			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			row=inflater.inflate(R.layout.find_beat_master_list,parent,false);
//			holder=new MyHolder(row);
//			row.setTag(holder);
//		}
//		else
//		{
//			holder=(MyHolder)row.getTag();
//		}
//		holder.beatNameViewOnOrderMgmtRow.setText(dashboardModels.get(position).getBeatNameViewOnOrderMgmtRow());
//		holder.remark.setText(dashboardModels.get(position).getRemark());
//		holder.active.setText(dashboardModels.get(position).getActive());
//		holder.syncId.setText(dashboardModels.get(position).getSyncId());
//		holder.areaName.setText(dashboardModels.get(position).getAreaId());
//		row.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Bundle bundle=new Bundle();
//				bundle.putInt("BEAT_ID",Integer.parseInt(dashboardModels.get(position).getDoc_id()));
//				Intent intent=new Intent(context,BeatMasterEntry.class);
//				intent.putExtras(bundle);
//				context.startActivity(intent);
//				((ActionBarActivity)context).finish();
//		}
//		});
//	}
	else if(rowLayout== R.layout.header_exp_grp_list_row)
	{
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.header_exp_grp_list_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);

		}
		else
		{
			holder=(MyHolder)row.getTag();
		}
		holder.compName.setText(dashboardModels.get(position).getName());
		holder.date.setText(dashboardModels.get(position).getFromDate().trim()+" -- "+dashboardModels.get(position).getToDate().trim());
		holder.remark.setText(dashboardModels.get(position).getRemark());
		holder.compName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
		holder.compName.setTextColor(Color.parseColor("#FF445FF4"));
		holder.date.setTextColor(Color.parseColor("#757575"));
		holder.remark.setTextColor(Color.parseColor("#757575"));
		row.setBackgroundColor(Color.parseColor("#FFFFFF"));
		holder.compName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle=new Bundle();
				bundle.putInt("EXP_GRP_ID",Integer.parseInt(dashboardModels.get(position).getDoc_id()));
				bundle.putString("EXP_GRP_NAME",dashboardModels.get(position).getName().replaceAll("'","''"));
				bundle.putString("EXP_GRP_FDATE",dashboardModels.get(position).getFromDate().trim());
				bundle.putString("EXP_GRP_TDATE",dashboardModels.get(position).getToDate().trim());
				Intent intent=new Intent(context,ExpenseSummaryScreen.class);
				intent.putExtras(bundle);
				context.startActivity(intent);
				((ActionBarActivity)context).finish();
			}
		});

		row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bundle=new Bundle();
				bundle.putInt("EXP_GRP_ID",Integer.parseInt(dashboardModels.get(position).getDoc_id()));
				bundle.putString("EXP_GRP_NAME",dashboardModels.get(position).getName().replaceAll("'","''"));
				bundle.putString("EXP_GRP_FDATE",dashboardModels.get(position).getFromDate().trim());
				bundle.putString("EXP_GRP_TDATE",dashboardModels.get(position).getToDate().trim());
				bundle.putString("EXP_GRP_REM",dashboardModels.get(position).getRemark().trim());
				Intent intent=new Intent(context,ExpenseGroupEntry.class);
				intent.putExtras(bundle);
				context.startActivity(intent);
				((ActionBarActivity)context).finish();
			}
		});
	}
	else if(rowLayout== R.layout.expense_layout_row)
	{
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.expense_layout_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
		}
		else
		{
			holder=(MyHolder)row.getTag();
		}
		holder.gstValLabel.setText(dashboardModels.get(position).getGSTNo());
		holder.compName.setText(dashboardModels.get(position).getName());
		holder.city.setText(dashboardModels.get(position).getCity());
		holder.billNo.setText(dashboardModels.get(position).getBillNo());
		holder.date.setText((dashboardModels.get(position).getDate()));
		holder.amount.setText((dashboardModels.get(position).getAmount()));
		holder.claimAmount.setText(dashboardModels.get(position).getClaimAmount());
		holder.other.setText(dashboardModels.get(position).getOtherActivity());
		/*holder.isGSTREGLable.setText(dashboardModels.get(position).getIsGSTRegistered());
		holder.partyVendorNameLabel.setText(dashboardModels.get(position).getPartyVendorName());
		holder.cgstAmount.setText(dashboardModels.get(position).getCgstAmount());
		holder.sgstAmountLabel.setText(dashboardModels.get(position).getSgstAmount());
		*/
		row.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				expenseTransferInterface.getPopupForUpdate(dashboardModels.get(position).getDoc_id());
			}
		});
	}
//	else if(rowLayout== R.layout.party_list_row)
//	{Log.d("mani","gus else gaya");
//		if(row==null)
//		{
//			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			row=inflater.inflate(R.layout.party_list_row,parent,false);
//			holder=new MyHolder(row);
//			row.setTag(holder);
//			Log.d("mani","new");
//		}
//		else
//		{
//			holder=(MyHolder)row.getTag();
//			Log.d("mani","old");
//		}
//		System.out.println(dashboardModels.get(position).getPartyName()+" "+dashboardModels.get(position).getPartyAddress()+" "+dashboardModels.get(position).getPartyVisit());
//		holder.partyName.setText(dashboardModels.get(position).getPartyName());
//		holder.partyAddress.setText(dashboardModels.get(position).getPartyAddress());
//		holder.partyVisit.setText(dashboardModels.get(position).getPartyVisit());
//		row.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				SharedPreferences preferences=context.getSharedPreferences("RETAILER_SESSION_DATA", MODE_PRIVATE);
//				Editor editor=preferences.edit();
//				editor.putString("PARTY_NAME_SESSION", holder.partyName.getText().toString());
//				editor.commit();
//				new IntentSend(context, RetailerDashboard.class).toSendAcivity();
//				((ActionBarActivity)context).finish();
//			}
//		});
//
//	}
//
//
//	else if(rowLayout== R.layout.find_dsrlist_row)
//	{
//		if(row==null)
//		{
//			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			row=inflater.inflate(R.layout.find_dsrlist_row,parent,false);
//			holder=new MyHolder(row);
//			row.setTag(holder);
//
//		}
//		else
//		{
//			holder=(MyHolder)row.getTag();
//
//		}
//
//		holder.vDate.setText(dashboardModels.get(position).getVdate());
//		//holder.vDocId.setText((dashboardModels.get(position).getDoc_id()==null?"NA":dashboardModels.get(position).getDoc_id()));
//		holder.start.setText(dashboardModels.get(position).getFromDate());
//		holder.end.setText(dashboardModels.get(position).getToDate());
//		holder.status.setText((dashboardModels.get(position).getStatus().equals("0")?"Pending":"Uploaded"));
//		holder.lock.setText((dashboardModels.get(position).getLock().equals("1")?"Yes":"No"));
//		holder.remark.setText(dashboardModels.get(position).getRemark());
//		row.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Bundle bundle=new Bundle();
//				bundle.putString("VDATE",dashboardModels.get(position).getVdate());
//				bundle.putBoolean("FromUpdate",true);
//				Intent intent=new Intent(context,DailySalesReport.class);
//				intent.putExtras(bundle);
//				context.startActivity(intent);
//				((ActionBarActivity)context).finish();
//
//			}
//		});
//	}
//	else if(rowLayout== R.layout.new_party_list_row)
//	{
//		if(row==null)
//		{
//			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			row=inflater.inflate(R.layout.new_party_list_row,parent,false);
//			holder=new MyHolder(row);
//			row.setTag(holder);
//		}
//		else
//		{
//			holder=(MyHolder)row.getTag();
//		}
//
//		holder.partyName.setText(dashboardModels.get(position).getPartyName());
//		if(dashboardModels.get(position).getContactPerson()==null || dashboardModels.get(position).getContactPerson().equalsIgnoreCase("null") || dashboardModels.get(position).getContactPerson().equals(""))
//		{
//			holder.contactPersonRow.setVisibility(View.GONE);
//
//		}
//		else
//		{
//			holder.contactPersonRow.setVisibility(View.GONE);
//			holder.contactPerson.setText(Html.fromHtml("<b>Contact Person: </b>"+dashboardModels.get(position).getContactPerson()));
//		}
//		if(dashboardModels.get(position).getPartyAddress()==null || dashboardModels.get(position).getPartyAddress().equalsIgnoreCase("null") || dashboardModels.get(position).getPartyAddress().equals(""))
//		{
//			holder.addressRow.setVisibility(View.GONE);
//		}
//		else{
//			holder.addressRow.setVisibility(View.VISIBLE);
//			holder.address.setText(Html.fromHtml("<b>Address: </b>"+dashboardModels.get(position).getPartyAddress()+" "+dashboardModels.get(position).getMonbileNo()));
//		}
//		if(dashboardModels.get(position).getMonbileNo()==null || dashboardModels.get(position).getMonbileNo().equalsIgnoreCase("null") || dashboardModels.get(position).getMonbileNo().equals(""))
//		{
//			holder.monbileNoRow.setVisibility(View.GONE);
//		}
//		else{
//			holder.monbileNoRow.setVisibility(View.GONE);
////			holder.monbileNoRow.setVisibility(View.VISIBLE);
//			holder.monbileNo.setText(Html.fromHtml("<b>Mb: </b>"+dashboardModels.get(position).getMonbileNo()));
//		}
//
//		if(dashboardModels.get(position).getOrderModel().equals("0")
//				&& dashboardModels.get(position).getDemoModel().equals("0")
//				&& dashboardModels.get(position).getCompetitorModel().equals("0")
//				&& dashboardModels.get(position).getCollectionModel().equals("0")
//				&&  dashboardModels.get(position).getFailedVisitModel().equals("0")
//				)
//		{
//			holder.star.setVisibility(View.INVISIBLE);
//			row.setBackgroundResource(R.drawable.boder_nw2);
//			holder.partyName.setTextColor(Color.parseColor("#37474F"));
//			holder.contactPerson.setTextColor(Color.parseColor("#37474F"));
//			holder.monbileNo.setTextColor(Color.parseColor("#37474F"));
//			holder.address.setTextColor(Color.parseColor("#37474F"));
//
//		}
//		else if(dashboardModels.get(position).getOrderModel().equals("1")
//				|| dashboardModels.get(position).getDemoModel().equals("1")
//				|| dashboardModels.get(position).getCompetitorModel().equals("1")
//				|| dashboardModels.get(position).getCollectionModel().equals("1")
//				)
//		{
//			holder.star.setVisibility(View.VISIBLE);
//			row.setBackgroundResource(R.drawable.boder_green);
//			holder.partyName.setTextColor(Color.parseColor("#FFFFFF"));
//			holder.contactPerson.setTextColor(Color.parseColor("#FFFFFF"));
//			holder.monbileNo.setTextColor(Color.parseColor("#FFFFFF"));
//			holder.address.setTextColor(Color.parseColor("#FFFFFF"));
//
//		}
//		else if(dashboardModels.get(position).getFailedVisitModel().equals("1"))
//		{
//			holder.star.setVisibility(View.INVISIBLE);
//			row.setBackgroundResource(R.drawable.boder_green);
//			holder.partyName.setTextColor(Color.parseColor("#FFFFFF"));
//			holder.contactPerson.setTextColor(Color.parseColor("#FFFFFF"));
//			holder.monbileNo.setTextColor(Color.parseColor("#FFFFFF"));
//			holder.address.setTextColor(Color.parseColor("#FFFFFF"));
//		}
//	}
//
/*
	else if(rowLayout== R.layout.find_demolist_row)
	{
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.find_demolist_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
			
		}
		else
		{
			holder=(MyHolder)row.getTag();
		}
		holder.vDate.setText(dashboardModels.get(position).getVdate());
		if(dashboardModels.get(position).getDoc_id()==null)
		{
			holder.vDocId.setText((dashboardModels.get(position).getAndroidId()));
		}
		else{
			holder.vDocId.setText((dashboardModels.get(position).getDoc_id()));
		}
		holder.vDocId.setText((dashboardModels.get(position).getDoc_id()==null?"NA":dashboardModels.get(position).getDoc_id()));*//*

		holder.prodClass.setText(dashboardModels.get(position).getProdClass());
		holder.prodMatGrp.setText(dashboardModels.get(position).getProdGrp());
		holder.prodSegment.setText(dashboardModels.get(position).getProdSegment());
		holder.remark.setText(dashboardModels.get(position).getRemark());
		//holder.layout1.setBackground(context.getResources().getDrawable(R.drawable.boder_nw1));
		holder.partyName.setText(dashboardModels.get(position).getPartyName());
		path=dashboardModels.get(position).getPicture();
		if(path == null || path.isEmpty())
		{
			holder.imageView.setVisibility(View.GONE);
		}
	else
		{
			holder.imageView.setVisibility(View.VISIBLE);
			holder.imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					table= DatabaseConnection.TABLE_TRANSDEMO;
					AndroidId=dashboardModels.get(position).getAndroidId();
					path=dashboardModels.get(position).getPicture();
					prefix=preferences2.getString("CONPERID_SESSION","0")+"A_D_";
					positionImage=position;
					if(path.contains("http"))
					{
						final Dialog dialog = new Dialog(context);
						// Include dialog.xml file
						 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);             dialog.setContentView(R.layout.image_dialog);
						// Set dialog title
						dialog.setTitle("View Image");
						ImageView image = (ImageView) dialog.findViewById(R.id.imageDialogView);
						ImageLoader.getInstance().displayImage(path,image,options);
						dialog.show();
						Button declineButton = (Button) dialog.findViewById(R.id.declineButton);

						declineButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// Close dialog
								dialog.dismiss();
							}
						});
					}
					else
					{
						byte[] decodedString = Base64.decode(path, Base64.DEFAULT);
						Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
						final Dialog dialog = new Dialog(context);
						// Include dialog.xml file
						 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
						dialog.setContentView(R.layout.image_dialog);
						// Set dialog title
						dialog.setTitle("View Image");
						ImageView image = (ImageView) dialog.findViewById(R.id.imageDialogView);
						if(decodedByte != null)
						{
							LoadActivity loadActivity=new LoadActivity(context);
							decodedByte=loadActivity.fitImage(decodedByte);
							image.setImageBitmap(decodedByte);
						}






BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 8;
					 Bitmap bitmap = BitmapFactory.decodeFile(filePath);

//					System.out.println("demo.getFilePath()"+dashboardModels.get(position).getPicture());
					if(bitmap!=null){
					LoadActivity loadActivity=new LoadActivity(context);
					bitmap=loadActivity.fitImage(bitmap);
					//image.setImageBitmap(loadActivity.fitImage(bitmap));
					image.setImageBitmap(bitmap);
				}*//*



				dialog.show();
				Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
				// if decline button is clicked, close the custom dialog
				declineButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Close dialog
						dialog.dismiss();
					}
				});
					}

				}
			});
		}

		if(dashboardModels.get(position).getType().equalsIgnoreCase("find")) {
			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub

					Bundle bundle = new Bundle();
					bundle.putString("ANDROIDID", dashboardModels.get(position).getAndroidId());
					bundle.putString("WEBDOCID", dashboardModels.get(position).getDoc_id());
					Intent intent = new Intent(context, DemoPreInformation.class);
					intent.putExtras(bundle);
					context.startActivity(intent);
					((ActionBarActivity)context).finish();

				}
			});
		}
		else if(dashboardModels.get(position).getType().equalsIgnoreCase("pending")) {

			if (mSelection.get(position) != null) {
				holder.layout1.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
			}
		}
	}
	
	else if(rowLayout== R.layout.find_bookorder_list_row)
	{
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.find_bookorder_list_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
			
		}
		else
		{
			holder=(MyHolder)row.getTag();
			
		}
		
		
		holder.vDate.setText(dashboardModels.get(position).getVdate());
		if(dashboardModels.get(position).getDoc_id()==null)
		{
			holder.document.setText((dashboardModels.get(position).getAndroidId()));
		}
		else{
			holder.document.setText((dashboardModels.get(position).getDoc_id()));
		}
		//holder.vDocId.setText((dashboardModels.get(position).getDoc_id()==null?"NA":dashboardModels.get(position).getDoc_id()));
		holder.amount.setText(dashboardModels.get(position).getAmount());
		holder.remark.setText(dashboardModels.get(position).getRemark());
		row.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Bundle bundle=new Bundle();
				bundle.putString("ANDROIDID",dashboardModels.get(position).getAndroidId());
				bundle.putString("WEBDOCID",dashboardModels.get(position).getDoc_id());
				Intent intent=new Intent(context,BookOrder.class);
				intent.putExtras(bundle);
				context.startActivity(intent);
				((ActionBarActivity)context).finish();
				
				
			}
		});

	}

	else if(rowLayout== R.layout.find_dist_discussion_row)
	{
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.find_dist_discussion_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);

		}
		else
		{
			holder=(MyHolder)row.getTag();

		}


		holder.vDate.setText(dashboardModels.get(position).getNextVisitDate());
		holder.vDocId.setText(dashboardModels.get(position).getVdate());
		//holder.vDocId.setText((dashboardModels.get(position).getDoc_id()==null?"NA":dashboardModels.get(position).getDoc_id()));
		holder.remark.setText(dashboardModels.get(position).getRemark());
		holder.start.setText(dashboardModels.get(position).getStartTime());
		holder.end.setText(dashboardModels.get(position).getEndTime());
		holder.nextVisitTime.setText(dashboardModels.get(position).getNextVisitTime());
		holder.distributerName.setText(dashboardModels.get(position).getDitributorName());
		holder.stock.setText(dashboardModels.get(position).getItemQty());


holder.imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(context);
				// Include dialog.xml file
				 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);             dialog.setContentView(R.layout.image_dialog);
				// Set dialog title
				dialog.setTitle("View Image");
				ImageView image = (ImageView) dialog.findViewById(R.id.imageDialogView);
				System.out.println("Value "+dashboardModels.get(position).getPicture());
				SharedPreferences dpreferences_new = context.getSharedPreferences("DYNAMIC_FORM_FIELDS_INFO", MODE_PRIVATE);
				String image_data = dpreferences_new.getString("Show_UseCamera",null);
				try{
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 8;
					final Bitmap bitmap = BitmapFactory.decodeFile(dashboardModels.get(position).getPicture());
					System.out.println("demo.getFilePath()"+dashboardModels.get(position).getPicture());
					LoadActivity loadActivity=new LoadActivity(context);
					image.setImageBitmap(loadActivity.fitImage(bitmap));
				}catch(Exception e){}
				dialog.show();
				Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
				// if decline button is clicked, close the custom dialog
				declineButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Close dialog
						dialog.dismiss();
					}
				});*//*


		path=dashboardModels.get(position).getPicture();
		if(path.isEmpty())
		{
			holder.imageView.setVisibility(View.GONE);
		}
		else
		{
			holder.imageView.setVisibility(View.VISIBLE);
			holder.imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					table= DatabaseConnection.TABLE_VISITDIST;
					AndroidId=dashboardModels.get(position).getAndroidId();
					path=dashboardModels.get(position).getPicture();
					prefix=preferences2.getString("CONPERID_SESSION","")+"A_DistDisc_";
					positionImage=position;
//					new DownloadImage().execute();
					if(path.contains("http"))
					{
						final Dialog dialog = new Dialog(context);
						// Include dialog.xml file
						 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);             dialog.setContentView(R.layout.image_dialog);
						// Set dialog title
						dialog.setTitle("View Image");
						ImageView image = (ImageView) dialog.findViewById(R.id.imageDialogView);
						ImageLoader.getInstance().displayImage(path,image,options);
						dialog.show();
						Button declineButton = (Button) dialog.findViewById(R.id.declineButton);

						declineButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// Close dialog
								dialog.dismiss();
							}
						});
					}
					else
					{
						byte[] decodedString = Base64.decode(path, Base64.DEFAULT);
						Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
						final Dialog dialog = new Dialog(context);
						// Include dialog.xml file
						 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);             dialog.setContentView(R.layout.image_dialog);
						// Set dialog title
						dialog.setTitle("View Image");
						ImageView image = (ImageView) dialog.findViewById(R.id.imageDialogView);
						if(decodedByte != null)
						{
							LoadActivity loadActivity=new LoadActivity(context);
							decodedByte=loadActivity.fitImage(decodedByte);
							image.setImageBitmap(decodedByte);
						}





					*/
/*BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 8;
					 Bitmap bitmap = BitmapFactory.decodeFile(filePath);

//					System.out.println("demo.getFilePath()"+dashboardModels.get(position).getPicture());
					if(bitmap!=null){
					LoadActivity loadActivity=new LoadActivity(context);
					bitmap=loadActivity.fitImage(bitmap);
					//image.setImageBitmap(loadActivity.fitImage(bitmap));
					image.setImageBitmap(bitmap);
				}*//*



						dialog.show();
						Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
						// if decline button is clicked, close the custom dialog
						declineButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// Close dialog
								dialog.dismiss();
							}
						});
					}



				}

			});
		}

		
		
		if(dashboardModels.get(position).getType().equalsIgnoreCase("find")) {
			DistributorController distributorController = new DistributorController(context);
			distributorController.open();
			holder.distributor = distributorController.getDistributerDetail(dashboardModels.get(position).getDitributorName() + "-" + dashboardModels.get(position).getSyncId());
			distributorController.close();
			row.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putString("ANDROIDID", dashboardModels.get(position).getAndroidId());
					bundle.putString("WEBDOCID", dashboardModels.get(position).getDoc_id());
					Intent intent = new Intent(context, DistributerDiscussion.class);
					intent.putExtra("distributor", holder.distributor);
					bundle.putBoolean("FROM_DISTRIBUTER_DASHBOARD", true);
					intent.putExtras(bundle);
					context.startActivity(intent);
					((ActionBarActivity)context).finish();
				}
			});
		}
		else if(dashboardModels.get(position).getType().equalsIgnoreCase("pending")) {
			if (mSelection.get(position) != null) {
				holder.layout1.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
			}
		}

	}
	
	else if(rowLayout== R.layout.find_collection_list_row)
	{
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.find_collection_list_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
		}
		else
		{
			holder=(MyHolder)row.getTag();
			
		}

		
		holder.vDate.setText(dashboardModels.get(position).getVdate());
		holder.amount.setText(dashboardModels.get(position).getAmount());
		holder.mode.setText(dashboardModels.get(position).getMode());
//		holder.chqdate.setText((dashboardModels.get(position).getChqdate()==null?"":dashboardModels.get(position).getChqdate()));
//		holder.chqno.setText((dashboardModels.get(position).getChqno()==null?"":dashboardModels.get(position).getChqno()));


		if(dashboardModels.get(position).getMode().equalsIgnoreCase("cash"))
		{
			holder.chqdate.setVisibility(View.GONE);
			holder.chqno.setVisibility(View.GONE);
			holder.chqdatetext.setVisibility(View.GONE);
			holder.chqnotext.setVisibility(View.GONE);

		}
		else {
			holder.chqdate.setVisibility(View.VISIBLE);
			holder.chqno.setVisibility(View.VISIBLE);
			holder.chqdatetext.setVisibility(View.VISIBLE);
			holder.chqnotext.setVisibility(View.VISIBLE);
			holder.chqdate.setText((dashboardModels.get(position).getChqdate() == null ? "" : dashboardModels.get(position).getChqdate()));
			holder.chqno.setText((dashboardModels.get(position).getChqno() == null ? "" : dashboardModels.get(position).getChqno()));
		}
                if(dashboardModels.get(position).getSyncId().equals("NA"))
				{
					SharedPreferences preferences2=getContext().getSharedPreferences("RETAILER_NAME",MODE_PRIVATE);
					holder.partyName.setText(preferences2.getString("Name","")+" Name");
					//holder.partyName.setText("Party Name");
					holder.distributerName.setText(dashboardModels.get(position).getDitributorName());
					row.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Bundle bundle = new Bundle();
							bundle.putString("ANDROIDID", dashboardModels.get(position).getAndroidId());
							bundle.putString("WEBDOCID", dashboardModels.get(position).getDoc_id());
							Intent intent = new Intent(context, PartyCollection.class);
							intent.putExtras(bundle);
							context.startActivity(intent);
							((ActionBarActivity)context).finish();
				}
	});

				}
		else {
					holder.partyName.setText("Distributor Name");
					holder.distributerName.setText(dashboardModels.get(position).getDitributorName());
					final Distributor distributor = new Distributor();
					distributor.setDistributor_id(dashboardModels.get(position).getDistId());
					distributor.setDistributor_name(dashboardModels.get(position).getDitributorName());
					distributor.setSync_id(dashboardModels.get(position).getSyncId());
					row.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							Bundle bundle = new Bundle();
							bundle.putString("ANDROIDID", dashboardModels.get(position).getAndroidId());
							bundle.putString("WEBDOCID", dashboardModels.get(position).getDoc_id());
							Intent intent = new Intent(context, AddCollection.class);
							intent.putExtra("distributor", distributor);
							bundle.putBoolean("FROM_DISTRIBUTER_DASHBOARD", true);
							intent.putExtras(bundle);
							context.startActivity(intent);
							((ActionBarActivity)context).finish();
						}
					});
				}
	}
	else if(rowLayout== R.layout.find_competitor_list_row)
	{
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.find_competitor_list_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
		}
		else
		{
			holder=(MyHolder)row.getTag();
		}
		if(dashboardModels.get(position).getDoc_id()==null)
		{
			holder.vDocId.setText((dashboardModels.get(position).getAndroidId()));
		}
		else{
			holder.vDocId.setText((dashboardModels.get(position).getDoc_id()));
		}
		holder.partyName.setText(dashboardModels.get(position).getPartyName());
		holder.vDate.setText(dashboardModels.get(position).getVdate());
		holder.compName.setText(dashboardModels.get(position).getName());
		holder.productName.setText(dashboardModels.get(position).getItemName());
		holder.rateTextview.setText(dashboardModels.get(position).getRate());
		holder.stdPack.setText(dashboardModels.get(position).getQuantity());
		holder.discount.setText(dashboardModels.get(position).getDiscount());

		if(dashboardModels.get(position).getOtherActivity().equalsIgnoreCase("true"))
		{
			holder.remark.setText("Yes");
		}
		else
		{
			holder.remark.setText(dashboardModels.get(position).getOtherActivity());
		}

		holder.reason.setText(dashboardModels.get(position).getRemark());

holder.imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(context);
                // Include dialog.xml file
                 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);             dialog.setContentView(R.layout.image_dialog);
                // Set dialog title
                dialog.setTitle("View Image");
                ImageView image = (ImageView) dialog.findViewById(R.id.imageDialogView);
				System.out.println("Value "+dashboardModels.get(position).getPicture());
				SharedPreferences dpreferences_new = context.getSharedPreferences("DYNAMIC_FORM_FIELDS_INFO", MODE_PRIVATE);
				String image_data = dpreferences_new.getString("Show_UseCamera",null);
				try{
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 8;
					final Bitmap bitmap = BitmapFactory.decodeFile(dashboardModels.get(position).getPath());
					System.out.println("demo.getFilePath()"+dashboardModels.get(position).getPath());
					LoadActivity loadActivity=new LoadActivity(context);
					image.setImageBitmap(loadActivity.fitImage(bitmap));
					}catch(Exception e){}

                dialog.show();
                Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        dialog.dismiss();
                    }
                });
            }
			
		//});

		if(dashboardModels.get(position).getPicture().isEmpty())
		{
			holder.imageView.setVisibility(View.GONE);
		}
		else {
			holder.imageView.setVisibility(View.VISIBLE);
			holder.imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					table = DatabaseConnection.TABLE_COMPETITOR;
					AndroidId = dashboardModels.get(position).getAndroidId();
					path = dashboardModels.get(position).getPicture();
					prefix = preferences2.getString("CONPERID_SESSION", "0") + "A_C_";
					positionImage = position;
					if(path.contains("http"))
					{
//						new DownloadImage().execute();
						final Dialog dialog = new Dialog(context);
						// Include dialog.xml file
						 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);             dialog.setContentView(R.layout.image_dialog);
						// Set dialog title
						dialog.setTitle("View Image");
						ImageView image = (ImageView) dialog.findViewById(R.id.imageDialogView);
						ImageLoader.getInstance().displayImage(path,image,options);
						dialog.show();
						Button declineButton = (Button) dialog.findViewById(R.id.declineButton);

						declineButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// Close dialog
								dialog.dismiss();
							}
						});
					}
					else
					{
						byte[] decodedString = Base64.decode(path, Base64.DEFAULT);
						Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
						final Dialog dialog = new Dialog(context);
						// Include dialog.xml file
						 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);             dialog.setContentView(R.layout.image_dialog);
						// Set dialog title
						dialog.setTitle("View Image");
						ImageView image = (ImageView) dialog.findViewById(R.id.imageDialogView);
						if(decodedByte != null)
						{
							LoadActivity loadActivity=new LoadActivity(context);
							decodedByte=loadActivity.fitImage(decodedByte);
							image.setImageBitmap(decodedByte);
						}
						dialog.show();
						Button declineButton = (Button) dialog.findViewById(R.id.declineButton);

						declineButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// Close dialog
								dialog.dismiss();
							}
						});
					}

				}
			});


		}
		if(dashboardModels.get(position).getType().equalsIgnoreCase("find")) {
			row.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putString("ANDROIDID", dashboardModels.get(position).getAndroidId());
					bundle.putString("WEBDOCID", dashboardModels.get(position).getDoc_id());
					Intent intent = new Intent(context, CompetitorEntry.class);
					intent.putExtras(bundle);
					context.startActivity(intent);
					((ActionBarActivity)context).finish();

				}
			});
		}

		else if(dashboardModels.get(position).getType().equalsIgnoreCase("pending")) {

			if (mSelection.get(position) != null) {
				holder.layout1.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
			}
		}
	}
*/


	/*else if(rowLayout== R.layout.find_competitor_list_row_history)
	{
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.find_competitor_list_row_history,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
		}
		else
		{
			holder=(MyHolder)row.getTag();
		}
		if(dashboardModels.get(position).getDoc_id()==null)
		{
			holder.vDocId.setText((dashboardModels.get(position).getAndroidId()));
		}
		else{
			holder.vDocId.setText((dashboardModels.get(position).getDoc_id()));
		}
		holder.partyName.setText(dashboardModels.get(position).getPartyName());
		holder.vDate.setText(dashboardModels.get(position).getVdate());
		holder.compName.setText(dashboardModels.get(position).getName());
		holder.productName.setText(dashboardModels.get(position).getItemName());
		holder.rateTextview.setText(dashboardModels.get(position).getRate());
		holder.stdPack.setText(dashboardModels.get(position).getQuantity());
		holder.discount.setText(dashboardModels.get(position).getDiscount());

		if(dashboardModels.get(position).getOtherActivity().equalsIgnoreCase("true"))
		{
			holder.remark.setText("Yes");
		}
		else
		{
			holder.remark.setText(dashboardModels.get(position).getOtherActivity());
		}

		if(dashboardModels.get(position).getOtherActivity().equalsIgnoreCase("true")|| dashboardModels.get(position).getOtherActivity().equalsIgnoreCase("yes"))
		{
			holder.viewDetailLable.setVisibility(View.VISIBLE);
			holder.viewDetailLable.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
//					Intent newIntent = new Intent(context,HistoryViewDetailPopupActivity.class);
//					newIntent.putExtra("compList",dashboardModels.get(position).getbra);
//					newIntent.putExtra("position",position);
//					startActivity(newIntent);

					Intent newIntent = new Intent(context,HistoryViewDetailPopupActivity.class);
					newIntent.putExtra("brandActivity",dashboardModels.get(position).getBrandActivity());
					newIntent.putExtra("meetActivity",dashboardModels.get(position).getMeetActivity());
					newIntent.putExtra("road",dashboardModels.get(position).getRoadshowActivity());
					newIntent.putExtra("scheme",dashboardModels.get(position).getScheme());
					newIntent.putExtra("info",dashboardModels.get(position).getGenralInfo());
					context.startActivity(newIntent);
				}
			});
		}
		else
		{
			holder.viewDetailLable.setVisibility(View.GONE);
		}
		holder.reason.setText(dashboardModels.get(position).getRemark());
		*//*holder.imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final Dialog dialog = new Dialog(context);
                // Include dialog.xml file
                 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);             dialog.setContentView(R.layout.image_dialog);
                // Set dialog title
                dialog.setTitle("View Image");
                ImageView image = (ImageView) dialog.findViewById(R.id.imageDialogView);
				System.out.println("Value "+dashboardModels.get(position).getPicture());
				SharedPreferences dpreferences_new = context.getSharedPreferences("DYNAMIC_FORM_FIELDS_INFO", MODE_PRIVATE);
				String image_data = dpreferences_new.getString("Show_UseCamera",null);
				try{
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 8;
					final Bitmap bitmap = BitmapFactory.decodeFile(dashboardModels.get(position).getPath());
					System.out.println("demo.getFilePath()"+dashboardModels.get(position).getPath());
					LoadActivity loadActivity=new LoadActivity(context);
					image.setImageBitmap(loadActivity.fitImage(bitmap));
					}catch(Exception e){}

                dialog.show();
                Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
                // if decline button is clicked, close the custom dialog
                declineButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Close dialog
                        dialog.dismiss();
                    }
                });
            }*//*

		//});
		path=dashboardModels.get(position).getPicture();
		if(path == null || path.isEmpty())
		{
			holder.imageView.setVisibility(View.GONE);
		}
		else
		{
			holder.imageView.setVisibility(View.VISIBLE);
			holder.imageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					table= DatabaseConnection.TABLE_COMPETITOR;
					AndroidId=dashboardModels.get(position).getAndroidId();
					path=dashboardModels.get(position).getPicture();
					prefix=preferences2.getString("CONPERID_SESSION","0")+"A_C_";
					positionImage=position;
					if(path.contains("http"))
					{
						final Dialog dialog = new Dialog(context);
						// Include dialog.xml file
						 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);             dialog.setContentView(R.layout.image_dialog);
						// Set dialog title
						dialog.setTitle("View Image");
						ImageView image = (ImageView) dialog.findViewById(R.id.imageDialogView);
						ImageLoader.getInstance().displayImage(path,image,options);
						dialog.show();
						Button declineButton = (Button) dialog.findViewById(R.id.declineButton);

						declineButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// Close dialog
								dialog.dismiss();
							}
						});
					}
					else
					{
						byte[] decodedString = Base64.decode(path, Base64.DEFAULT);
						Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
						final Dialog dialog = new Dialog(context);
						// Include dialog.xml file
						 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);             dialog.setContentView(R.layout.image_dialog);
						// Set dialog title
						dialog.setTitle("View Image");
						ImageView image = (ImageView) dialog.findViewById(R.id.imageDialogView);
						if(decodedByte != null)
						{
							LoadActivity loadActivity=new LoadActivity(context);
							decodedByte=loadActivity.fitImage(decodedByte);
							image.setImageBitmap(decodedByte);
						}





						dialog.show();
						Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
						// if decline button is clicked, close the custom dialog
						declineButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// Close dialog
								dialog.dismiss();
							}
						});
					}

				}
			});



		}
		if(dashboardModels.get(position).getType().equalsIgnoreCase("find")) {
			row.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Bundle bundle = new Bundle();
					bundle.putString("ANDROIDID", dashboardModels.get(position).getAndroidId());
					bundle.putString("WEBDOCID", dashboardModels.get(position).getDoc_id());
					Intent intent = new Intent(context, CompetitorEntry.class);
					intent.putExtras(bundle);
					context.startActivity(intent);
					((ActionBarActivity)context).finish();

				}
			});
		}

		else if(dashboardModels.get(position).getType().equalsIgnoreCase("pending")) {

			if (mSelection.get(position) != null) {
				holder.layout1.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
			}
		}
	}*/

//	else if(rowLayout==R.layout.book_order_list_row)
//	{
//		if(row==null)
//		{
//			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			row=inflater.inflate(R.layout.book_order_list_row,parent,false);
//			holder=new MyHolder(row);
//			row.setTag(holder);
//			holder.quantityOfProduct.setTag(position);
//			holder.amount.setTag(position);
//			holder.productPrice.setTag(position);
//			holder.itemId.setTag(position);
//		}
//		else
//		{
//			holder=(MyHolder)row.getTag();
//			Log.d("mani","old");
//		}
//		holder.qtyEditRowId=position;
//		holder.rateEditRowId=position;
//		holder.amountEditRowId=position;
//		postValue[position]=String.valueOf(dashboardModels.get(position).getPriceProduct());
//		holder.quantityOfProduct.setText(qtyEdit_values[holder.qtyEditRowId]);
//		holder.productPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
//		holder.productPrice.setInputType(InputType.TYPE_CLASS_NUMBER);
//		holder.quantityOfProduct.setInputType(InputType.TYPE_CLASS_NUMBER);
//		holder.productName.setText(dashboardModels.get(position).getProductName());
//		holder.productPrice.setText(String.valueOf(dashboardModels.get(position).getPriceProduct()));
//		holder.qtyInCarton.setText(String.valueOf(dashboardModels.get(position).getQtyInCatron()));
//		holder.itemId.setText(String.valueOf(dashboardModels.get(position).getItemId()));
//		holder.amount.setText(amountEdit_values[holder.amountEditRowId]);
//		holder.productPrice.addTextChangedListener(new TextWatcher()
//        {
//            public void afterTextChanged(Editable edt)
//            {
//
//            }
//
//            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
//
//            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//                //qtyEdit_values[ref] = Attitude_Value.getText().toString();
//
//            	if(holder.productPrice.getText().toString().equals(""))
//            	{
//            		dashboardModels.get(holder.rateEditRowId).setPriceProduct(0);
//            	}
//            	else
//            	{
//         		dashboardModels.get(holder.rateEditRowId).setPriceProduct(Double.valueOf(arg0.toString()));
//            	}
//
//            	if(holder.quantityOfProduct.getText().toString().equals("")||holder.productPrice.getText().toString().equals(""))
//            	{
//            		holder.amount.setText("");
//            	}
//            	else
//            	{
//            		holder.amount.setText(String.valueOf(Double.parseDouble(holder.quantityOfProduct.getText().toString())*Double.parseDouble(holder.productPrice.getText().toString())));
//            	}
//            }
//        });
//		holder.amount.addTextChangedListener(new TextWatcher()
//        {
//            public void afterTextChanged(Editable edt)
//            {
//               //  savedQtyData.put(position,edt.toString().trim());
//
//            }
//
//            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
//
//            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//                //qtyEdit_values[ref] = Attitude_Value.getText().toString();
//
//            	amountEdit_values[holder.amountEditRowId] = arg0.toString();
//            }
//        });
//		holder.quantityOfProduct.addTextChangedListener(new TextWatcher()
//        {
//            public void afterTextChanged(Editable edt)
//            {
//               //  savedQtyData.put(position,edt.toString().trim());
//
//            }
//
//            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
//
//            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
//                //qtyEdit_values[ref] = Attitude_Value.getText().toString();
//            	qtyEdit_values[holder.qtyEditRowId] = arg0.toString();
//            	if(holder.quantityOfProduct.getText().toString().equals("")||holder.productPrice.getText().toString().equals(""))
//            	{
//            		holder.amount.setText("");
//            	}
//            	else
//            	{holderListener.holderListener(holder);
//            	holder.amount.setText(String.valueOf(Double.parseDouble(holder.quantityOfProduct.getText().toString())*Double.parseDouble(holder.productPrice.getText().toString())));
//            	}
//            }
//        });
//		System.out.println("product price"+String.valueOf(dashboardModels.get(position).getProductPrice()));
//			}
//	else if(rowLayout== R.layout.demo_order_product_list_retailer_row)
//	{
//		Log.d("mani","gus else gaya");
//
//
//		if(row==null)
//		{
//			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			row=inflater.inflate(R.layout.demo_order_product_list_retailer_row,parent,false);
//
//
//			holder=new MyHolder(row);
//			row.setTag(holder);
//			Log.d("mani","new");
//			arrayList.add(holder);
//		}
//		else
//		{
//			holder=(MyHolder)row.getTag();
//			Log.d("mani","old");
//			arrayList.add(holder);
//		}
//
//			System.out.println(dashboardModels.get(position).getPartyName()+" "+dashboardModels.get(position).getPartyAddress()+" "+dashboardModels.get(position).getPartyVisit());
//			holder.productNameOnDemoRetailer.setText(dashboardModels.get(position).getProductNameOnDemoRetailer());
//			holder.priceProductOnDemoRetailer.setText(String.valueOf(dashboardModels.get(position).getPriceProductOnDemoRetailer()));
//			holder.qtyOnDemoRetailer.setText(String.valueOf(dashboardModels.get(position).getQtyOnDemoRetailer()));
//			holder.sampleQtyOnDemoRetailer.setText(String.valueOf(dashboardModels.get(position).getSampleQtyOnDemoRetailer()));
//	}
//	else if(rowLayout== R.layout.demo_order_product_sample_list_retailer_row)
//	{Log.d("mani","gus else gaya");
//		if(row==null)
//		{
//			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			row=inflater.inflate(R.layout.demo_order_product_sample_list_retailer_row,parent,false);
//
//
//			holder=new MyHolder(row);
//			row.setTag(holder);
//			Log.d("mani","new");
//		}
//		else
//		{
//			holder=(MyHolder)row.getTag();
//			Log.d("mani","old");
//		}
//
//			System.out.println(dashboardModels.get(position).getPartyName()+" "+dashboardModels.get(position).getPartyAddress()+" "+dashboardModels.get(position).getPartyVisit());
//			holder.productNameOnDemoRetailer.setText(dashboardModels.get(position).getProductNameOnDemoRetailer());
//			holder.priceProductOnDemoRetailer.setText(String.valueOf(dashboardModels.get(position).getPriceProductOnDemoRetailer()));
//			holder.quantityOnDemo.setText(String.valueOf(dashboardModels.get(position).getMpProductOnDemoRetailer()));
//
//
//
//	}
//	else if(rowLayout== R.layout.product_list_row)
//	{Log.d("mani","gus else gaya");
//		if(row==null)
//		{
//			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			row=inflater.inflate(R.layout.product_list_row,parent,false);
//			holder=new MyHolder(row);
//			row.setTag(holder);
//			Log.d("mani","new");
//		}
//		else
//		{
//			holder=(MyHolder)row.getTag();
//			Log.d("mani","old");
//		}
//		if(dataForCheck.equals("PRODUCT_VIEW_ONLY"))
//		{
//			holder.dpProductList.setVisibility(View.VISIBLE);
//			holder.dpView.setVisibility(View.VISIBLE);
//			holder.rpProductList.setVisibility(View.VISIBLE);
//			holder.rpView.setVisibility(View.VISIBLE);
//			holder.mpProductList.setVisibility(View.VISIBLE);
//			holder.mpView.setVisibility(View.VISIBLE);
//			holder.priceView.setVisibility(View.GONE);
//			holder.priceProductList.setVisibility(View.GONE);
//			//System.out.println(dashboardModels.get(position).getPartyName()+" "+dashboardModels.get(position).getPartyAddress()+" "+dashboardModels.get(position).getPartyVisit());
//			holder.nameProductList.setText(dashboardModels.get(position).getNameProductList());
//			//holder.priceProductList.setText(String.valueOf(dashboardModels.get(position).getPriceProductList()));
//			holder.mpProductList.setText(String.valueOf(dashboardModels.get(position).getMpProductList()));
//			holder.dpProductList.setText(String.valueOf(dashboardModels.get(position).getDpProductList()));
//			holder.rpProductList.setText(String.valueOf(dashboardModels.get(position).getRpProductList()));
//			holder.cartonqtyProduct.setText(String.valueOf(dashboardModels.get(position).getCartonqtyProduct()));
//		}
//		else if(dataForCheck.equals("PRODUCT_ADD_ON_BOOK_ORDER"))
//		{
//			System.out.println(dashboardModels.get(position).getPartyName()+" "+dashboardModels.get(position).getPartyAddress()+" "+dashboardModels.get(position).getPartyVisit());
//			holder.nameProductList.setText(dashboardModels.get(position).getNameProductList());
//			holder.priceProductList.setText(String.valueOf(dashboardModels.get(position).getPriceProductList()));
//			holder.dpProductList.setVisibility(View.GONE);
//			holder.dpView.setVisibility(View.GONE);
//			holder.rpProductList.setVisibility(View.GONE);
//			holder.rpView.setVisibility(View.GONE);
//			holder.mpProductList.setVisibility(View.GONE);
//			holder.mpView.setVisibility(View.GONE);
//			holder.cartonqtyProduct.setVisibility(View.GONE);
//			holder.cartonqtyView.setVisibility(View.GONE);
//		}
//
//		row.setBackgroundColor(context.getResources().getColor(android.R.color.transparent)); //default colon
//
//        if (mSelection.get(position) != null) {
//        	row.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
//        }
//	}
//	else if(rowLayout== R.layout.history_list_row)
//	{Log.d("mani","gus else gaya");
//		if(row==null)
//		{
//			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			row=inflater.inflate(R.layout.history_list_row,parent,false);
//
//
//			holder=new MyHolder(row);
//			row.setTag(holder);
//			Log.d("mani","new");
//		}
//		else
//		{
//			holder=(MyHolder)row.getTag();
//			Log.d("mani","old");
//		}
//		System.out.println(dashboardModels.get(position).getPartyName()+" "+dashboardModels.get(position).getPartyAddress()+" "+dashboardModels.get(position).getPartyVisit());
//		if(dashboardModels.get(position).getType().equals("Order"))
//		{
//			holder.operationOnHistory.setTextColor(Color.parseColor("#1919ff"));
//			holder.dateOnHistroy.setText(dashboardModels.get(position).getDate());
//			holder.operationOnHistory.setText(dashboardModels.get(position).getType());
//			holder.docidOnHistory.setText(dashboardModels.get(position).getDoc_id());
//
//		}
//
//		else if(dashboardModels.get(position).getType().equals("Demo"))
//		{
//			holder.operationOnHistory.setTextColor(Color.parseColor("#009900"));
//			holder.dateOnHistroy.setText(dashboardModels.get(position).getDate());
//			holder.operationOnHistory.setText(dashboardModels.get(position).getType());
//			holder.docidOnHistory.setText(dashboardModels.get(position).getDoc_id());
//
//		}
//		else if(dashboardModels.get(position).getType().equals("FailedVisit"))
//		{
//			holder.operationOnHistory.setTextColor(Color.parseColor("#cc0000"));
//			holder.dateOnHistroy.setText(dashboardModels.get(position).getDate());
//			holder.operationOnHistory.setText(dashboardModels.get(position).getType());
//			holder.docidOnHistory.setText(dashboardModels.get(position).getDoc_id());
//
//		}
//		else if(dashboardModels.get(position).getType().equals("Competitor"))
//		{
//			holder.operationOnHistory.setTextColor(Color.parseColor("#cc0000"));
//			holder.dateOnHistroy.setText(dashboardModels.get(position).getDate());
//			holder.operationOnHistory.setText(dashboardModels.get(position).getType());
//			holder.docidOnHistory.setText(dashboardModels.get(position).getDoc_id());
//
//		}
//		else if(dashboardModels.get(position).getType().equals("Collection"))
//		{
//			holder.operationOnHistory.setTextColor(Color.parseColor("#cc0000"));
//			holder.dateOnHistroy.setText(dashboardModels.get(position).getDate());
//			holder.operationOnHistory.setText(dashboardModels.get(position).getType());
//			holder.docidOnHistory.setText(dashboardModels.get(position).getDoc_id());
//
//		}
//		row.setBackgroundColor(context.getResources().getColor(android.R.color.transparent)); //default colon
//
//
//	}
/*
	else if(rowLayout== R.layout.order_managemnt_list_row)
	{Log.d("mani","gus else gaya");
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.order_managemnt_list_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
			Log.d("mani","new");
		}
		else
		{
			holder=(MyHolder)row.getTag();
			Log.d("mani","old");
		}
			holder.order.setTextColor(Color.GREEN);
			holder.demo.setTextColor(Color.YELLOW);
			holder.failedVisit.setTextColor(Color.parseColor("#FF9900"));
			holder.dateOnOrderMgmt.setText(dashboardModels.get(position).getDateOnOrderMgmt());
			if(dashboardModels.get(position).getOrderModel().equals("0")){
				holder.order.setEnabled(false);
				holder.order.setTextColor(Color.parseColor("#8F8F8F"));
			}
			if(dashboardModels.get(position).getDemoModel().equals("0")){
				holder.demo.setEnabled(false);
				holder.demo.setTextColor(Color.parseColor("#8F8F8F"));
			}
			if(dashboardModels.get(position).getFailedVisitModel().equals("0")){
				holder.failedVisit.setEnabled(false);
				holder.failedVisit.setTextColor(Color.parseColor("#8F8F8F"));
			}
			holder.order.setText("Order");
			holder.demo.setText("Demo");
			holder.failedVisit.setText("Failed Visit");
			final Switch switch1=(Switch)row.findViewById(R.id.switch1);
			switch1.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (infodata.get(position).isclicked) {
                    	System.out.println("yes="+holder.dateOnOrderMgmt.getText().toString());
                            infodata.get(position).isclicked = false;
                        } else {
                        	System.out.println("no="+holder.dateOnOrderMgmt.getText().toString());
                            infodata.get(position).isclicked = true;
                        }

                    for(int i=0;i<infodata.size();i++)
                    {
                        if (infodata.get(i).isclicked)
                        {
                       	
                            //Toast.makeText(context, holder.dateOnOrderMgmt.getText().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });

            if (infodata.get(position).isclicked) {

            	switch1.setChecked(true);
            }
            else {
                switch1.setChecked(false);
            }
			holder.demo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					System.out.println("date="+holder.dateOnOrderMgmt.getText().toString());
					SharedPreferences preferences=context.getSharedPreferences("MyPref", MODE_PRIVATE);
					Editor editor=preferences.edit();
					editor.putString("DATE_SENT_FROM_ORDER_MGMT", holder.dateOnOrderMgmt.getText().toString());
					editor.putString("OPERATION_MODE_DEMO_SENT_FROM_ORDER_MGMT", holder.demo.getText().toString());
					editor.putString("OPERATION_MODE_ORDER_SENT_FROM_ORDER_MGMT", holder.order.getText().toString());
					editor.putString("OPERATION_MODE_FAILED_VISIT_SENT_FROM_ORDER_MGMT", holder.failedVisit.getText().toString());
					editor.commit();
					new IntentSend(context, DemoPartyListDateWise.class).toSendAcivity();
					//new Custom_Toast(context, holder.dateOnOrderMgmt.getText().toString()).showCustomAlert(); 
				}
			});
			holder.order.setOnClickListener(new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								System.out.println("date="+holder.dateOnOrderMgmt.getText().toString());
								SharedPreferences preferences=context.getSharedPreferences("MyPref", MODE_PRIVATE);
								Editor editor=preferences.edit();
								editor.putString("DATE_SENT_FROM_ORDER_MGMT", holder.dateOnOrderMgmt.getText().toString());
								editor.putString("OPERATION_MODE_DEMO_SENT_FROM_ORDER_MGMT", holder.demo.getText().toString());
								editor.putString("OPERATION_MODE_ORDER_SENT_FROM_ORDER_MGMT", holder.order.getText().toString());
								editor.putString("OPERATION_MODE_FAILED_VISIT_SENT_FROM_ORDER_MGMT", holder.failedVisit.getText().toString());
								editor.commit();
								new IntentSend(context, OrderPartyListDateWise.class).toSendAcivity();
								//new Custom_Toast(context, holder.dateOnOrderMgmt.getText().toString()).showCustomAlert(); 
								
							}
						});
			holder.failedVisit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					System.out.println("date="+holder.dateOnOrderMgmt.getText().toString());
					SharedPreferences preferences=context.getSharedPreferences("MyPref", MODE_PRIVATE);
					Editor editor=preferences.edit();
					editor.putString("DATE_SENT_FROM_ORDER_MGMT", holder.dateOnOrderMgmt.getText().toString());
					editor.putString("OPERATION_MODE_DEMO_SENT_FROM_ORDER_MGMT", holder.demo.getText().toString());
					editor.putString("OPERATION_MODE_ORDER_SENT_FROM_ORDER_MGMT", holder.order.getText().toString());
					editor.putString("OPERATION_MODE_FAILED_VISIT_SENT_FROM_ORDER_MGMT", holder.failedVisit.getText().toString());
					editor.commit();
					new IntentSend(context, FailedVisitPartyListDateWise.class).toSendAcivity();
					//new Custom_Toast(context, holder.dateOnOrderMgmt.getText().toString()).showCustomAlert(); 
					
				}
			});
		row.setBackgroundColor(context.getResources().getColor(android.R.color.transparent)); //default colon
        
        if (mSelection.get(position) != null) {
        	row.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
        }
		
	}

	else if(rowLayout== R.layout.partylist_ordermgmtwise_row_astral)
	{
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.partylist_ordermgmtwise_row_astral,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
		}
		else
		{
			holder=(MyHolder)row.getTag();

		}
		AppEnviroDataController appDataController=new AppEnviroDataController(context);
		appDataController.open();
		ArrayList<AppEnviroData> appDataArray = appDataController.getAppEnviroFromDb();
		appDataController.close();
		String appType="";
		for(int i=0;i<appDataArray.size();i++)
		{
			appType=appDataArray.get(i).getItemwisesale().trim();

		}

		if(appType.equalsIgnoreCase("Y")){

			holder.partyNameOnOrderMgmtRow.setText(dashboardModels.get(position).getPartyNameOnOrderMgmtRow());
			holder.beatNameViewOnOrderMgmtRow.setText(dashboardModels.get(position).getBeatNameViewOnOrderMgmtRow());
			holder.orderAmount.setText(dashboardModels.get(position).getAmount());
			holder.orderRemark.setText(dashboardModels.get(position).getRemark());
			holder.orderRemarkheading.setText(dashboardModels.get(position).getQuantity());
			holder.beatNameLabel.setText(dashboardModels.get(position).getBeatNameViewOnOrderMgmtDemoRow());
			holder.btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SharedPreferences preferences=context.getSharedPreferences("MyPref",MODE_PRIVATE);
					Editor editor=preferences.edit();
					editor.putString("PARTY_NAME_FROM_ORDER_PARTYWISE", dashboardModels.get(position).getPartyNameOnOrderMgmtRow());
					//editor.putString("ADDRESS_FROM_ORDER_PARTYWISE", orderTransactions.get(position).getBea);
					editor.putString("DOCID_FROM_ORDER_PARTYWISE", dashboardModels.get(position).getAndroidId());
					editor.putString("ORDER_DOCID_FROM_ORDER_PARTYWISE", dashboardModels.get(position).getDoc_id());
					editor.putString("DATE_FROM_ORDER_PARTYWISE", dashboardModels.get(position).getVdate());
					editor.putString("PARTYCODE_FROM_ORDER_PARTYWISE", dashboardModels.get(position).getPartyNameOnOrderMgmtRow());
					editor.commit();
					new IntentSend(context,OrderProductDateWise.class).toSendAcivity();
				}
			});
		}
		else{
			holder.btn.setVisibility(View.GONE);
			holder.orderRemarkheading.setVisibility(View.GONE);
			holder.remark.setVisibility(View.GONE);
			holder.partyNameOnOrderMgmtRow.setText(dashboardModels.get(position).getPartyNameOnOrderMgmtRow());
			holder.beatNameViewOnOrderMgmtRow.setText(dashboardModels.get(position).getBeatNameViewOnOrderMgmtRow());
			holder.orderAmount.setText(dashboardModels.get(position).getAmount());
			holder.orderRemark.setText(dashboardModels.get(position).getRemark());
		}
		row.setBackgroundColor(context.getResources().getColor(android.R.color.transparent)); //default colon

		if (mSelection.get(position) != null) {
			row.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));// this is a selected position so make it red
		}

	}
*/

	/*else if(rowLayout== R.layout.partylist_ordermgmtwise_row)
	{
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.partylist_ordermgmtwise_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
		}
		else
		{
			holder=(MyHolder)row.getTag();
			
		}
		AppEnviroDataController appDataController=new AppEnviroDataController(context);
		appDataController.open();
		ArrayList<AppEnviroData> appDataArray = appDataController.getAppEnviroFromDb();
		appDataController.close();
		String appType="";
		for(int i=0;i<appDataArray.size();i++)
		{
			appType=appDataArray.get(i).getItemwisesale().trim();

		}

		if(appType.equalsIgnoreCase("Y")){
			
			holder.partyNameOnOrderMgmtRow.setText(dashboardModels.get(position).getPartyNameOnOrderMgmtRow());
			holder.beatNameViewOnOrderMgmtRow.setText(dashboardModels.get(position).getBeatNameViewOnOrderMgmtRow());
			holder.orderAmount.setText(dashboardModels.get(position).getAmount());
			holder.orderRemark.setText(dashboardModels.get(position).getRemark());
			holder.orderRemarkheading.setText(dashboardModels.get(position).getQuantity());
			holder.beatNameLabel.setText(dashboardModels.get(position).getBeatNameViewOnOrderMgmtDemoRow());
			holder.btn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					SharedPreferences preferences=context.getSharedPreferences("MyPref",MODE_PRIVATE);
					Editor editor=preferences.edit();
					editor.putString("PARTY_NAME_FROM_ORDER_PARTYWISE", dashboardModels.get(position).getPartyNameOnOrderMgmtRow());
					//editor.putString("ADDRESS_FROM_ORDER_PARTYWISE", orderTransactions.get(position).getBea);
					editor.putString("DOCID_FROM_ORDER_PARTYWISE", dashboardModels.get(position).getAndroidId());
					editor.putString("ORDER_DOCID_FROM_ORDER_PARTYWISE", dashboardModels.get(position).getDoc_id());
					editor.putString("DATE_FROM_ORDER_PARTYWISE", dashboardModels.get(position).getVdate());
					editor.putString("PARTYCODE_FROM_ORDER_PARTYWISE", dashboardModels.get(position).getPartyNameOnOrderMgmtRow());
					editor.commit();
					new IntentSend(context,OrderProductDateWise.class).toSendAcivity();
				}
			});
		}
		else{
			holder.btn.setVisibility(View.GONE);
			holder.orderRemarkheading.setVisibility(View.GONE);
			holder.remark.setVisibility(View.GONE);
			holder.partyNameOnOrderMgmtRow.setText(dashboardModels.get(position).getPartyNameOnOrderMgmtRow());
			holder.beatNameViewOnOrderMgmtRow.setText(dashboardModels.get(position).getBeatNameViewOnOrderMgmtRow());
			holder.orderAmount.setText(dashboardModels.get(position).getAmount());
	     	holder.orderRemark.setText(dashboardModels.get(position).getRemark());
		}
			row.setBackgroundColor(context.getResources().getColor(android.R.color.transparent)); //default colon
        
        if (mSelection.get(position) != null) {
        	row.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));// this is a selected position so make it red
        }
		
	}
	else if(rowLayout== R.layout.demopartylist_ordermgmtwise_row)
	{Log.d("mani","gus else gaya");
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.demopartylist_ordermgmtwise_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
		}
		else
		{
			holder=(MyHolder)row.getTag();
		}
			holder.partyNameOnOrderMgmtDemoRow.setText(dashboardModels.get(position).getPartyNameOnOrderMgmtDemoRow());
			holder.beatNameViewOnOrderMgmtDemoRow.setText(dashboardModels.get(position).getBeatNameViewOnOrderMgmtDemoRow());
			holder.textClass.setText(dashboardModels.get(position).getProdClass());
			holder.textSegment.setText(dashboardModels.get(position).getProdSegment());
			holder.textMatGrp.setText(dashboardModels.get(position).getProdGrp());
			holder.textRemark.setText(dashboardModels.get(position).getRemark());
			row.setBackgroundColor(context.getResources().getColor(android.R.color.transparent)); //default colon
        
        if (mSelection.get(position) != null) {
        	row.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
        }
		
	}
	else if(rowLayout== R.layout.faliedvisit_list_ordermgmtdatewise_row)
	{
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.faliedvisit_list_ordermgmtdatewise_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
		}
		else
		{
			holder=(MyHolder)row.getTag();
		}
		 if(dashboardModels.get(position).getPartyNameOnOrderMgmtFailedVisitRow().equals("PA"))
		{
			holder.compName.setText("Distributor name");
			holder.partyNameOnOrderMgmtFailedVisitRow.setText(dashboardModels.get(position).getDitributorName());
		}
		 else{

			 holder.compName.setText("Party Name");
			 holder.partyNameOnOrderMgmtFailedVisitRow.setText(dashboardModels.get(position).getPartyNameOnOrderMgmtFailedVisitRow());
		 }
		if(dashboardModels.get(position).getBeatNameViewOnOrderMgmtFailedVisitRow().equals("NA"))
			{
				holder.productNameOnDemoOrderMgmt.setVisibility(View.GONE);
				holder.beatNameViewOnOrderMgmtFailedVisitRow.setVisibility(View.GONE);
			}
			else
			{
				holder.productNameOnDemoOrderMgmt.setVisibility(View.VISIBLE);
				holder.beatNameViewOnOrderMgmtFailedVisitRow.setVisibility(View.VISIBLE);
				holder.beatNameViewOnOrderMgmtFailedVisitRow.setText(dashboardModels.get(position).getBeatNameViewOnOrderMgmtFailedVisitRow());
			}
			holder.remarksTextView.setText(dashboardModels.get(position).getRemark());
			holder.nextVisitDateFailedVisit.setText(dashboardModels.get(position).getNextVisitDate());
			holder.nextVisitTimeFailedVisit.setText(dashboardModels.get(position).getNextVisitTime());
		    holder.reason.setText(dashboardModels.get(position).getReason());
		if (mSelection.get(position) != null) {
			holder.layout1.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
		}
		
	}
	else if(rowLayout== R.layout.demo_order_product_list_row)
	{
		
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.demo_order_product_list_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
			
		}
		else
		{
			holder=(MyHolder)row.getTag();
			
		}
	holder.textClass.setText(dashboardModels.get(position).getProdClass());
		holder.textSegment.setText(dashboardModels.get(position).getProdSegment());
		holder.textMatGrp.setText(dashboardModels.get(position).getProdGrp());
		holder.textRemark.setText(dashboardModels.get(position).getRemark());



	}
	else if(rowLayout== R.layout.order_product_list_row)
	{

		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.order_product_list_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
			
		}
		else
		{
			holder=(MyHolder)row.getTag();
			
		}
		if(dashboardModels.get(position).getDoc_id()==null)
		{
			holder.vDocId.setText((dashboardModels.get(position).getAndroidId()));
		}
		else{
			holder.vDocId.setText((dashboardModels.get(position).getDoc_id()));
		}
		holder.orderAmount.setText(dashboardModels.get(position).getAmount());
		String s=dashboardModels.get(position).getAmount();
		System.out.println("s="+s);
		holder.orderRemark.setText(dashboardModels.get(position).getRemark());
		s=dashboardModels.get(position).getRemark();
		System.out.println("s="+s);
	}
	else if(rowLayout== R.layout.reamrks_failed_list_row)
	{
		
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.reamrks_failed_list_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
			
		}
		else
		{
			holder=(MyHolder)row.getTag();
			
		}
		if(dashboardModels.get(position).getDoc_id()==null)
		{
			holder.vDocId.setText((dashboardModels.get(position).getAndroidId()));
		}
		else{
			holder.vDocId.setText((dashboardModels.get(position).getDoc_id()));
		}
		
		holder.remarksTextView.setText(dashboardModels.get(position).getRemark());
		holder.nextVisitDateFailedVisit.setText(dashboardModels.get(position).getNextVisitDate());
		holder.reason.setText(dashboardModels.get(position).getReason());
				
		
	}
	
	else if(rowLayout== R.layout.party_collection_list_row)
	{

		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.party_collection_list_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);

		}
		else
		{
			holder=(MyHolder)row.getTag();

		}
try {
	if ((dashboardModels.get(position).getType()==null?false:dashboardModels.get(position).getType().equals("party"))) {
		SharedPreferences preferences2=getContext().getSharedPreferences("RETAILER_NAME",MODE_PRIVATE);
		holder.partyName.setText(preferences2.getString("Name","")+" Name");
		holder.distributerName.setText(dashboardModels.get(position).getPartyName());

	} else {

		holder.partyName.setText("Distributor Name");
		holder.distributerName.setText(dashboardModels.get(position).getDitributorName());
	}

}catch(Exception e){}
		holder.vDate.setText(dashboardModels.get(position).getVdate());

		holder.amount.setText(dashboardModels.get(position).getAmount());
		holder.mode.setText(dashboardModels.get(position).getMode());
		if(dashboardModels.get(position).getMode().equalsIgnoreCase("cash"))
		{
			holder.chqdate.setVisibility(View.GONE);
			holder.chqno.setVisibility(View.GONE);
			holder.chqdatetext.setVisibility(View.GONE);
			holder.chqnotext.setVisibility(View.GONE);

		}
		else {
			holder.chqdate.setVisibility(View.VISIBLE);
			holder.chqno.setVisibility(View.VISIBLE);
			holder.chqdatetext.setVisibility(View.VISIBLE);
			holder.chqnotext.setVisibility(View.VISIBLE);
			holder.chqdate.setText((dashboardModels.get(position).getChqdate() == null ? "" : dashboardModels.get(position).getChqdate()));
			holder.chqno.setText((dashboardModels.get(position).getChqno() == null ? "" : dashboardModels.get(position).getChqno()));
		}
		if (mSelection.get(position) != null) {

			holder.layout1.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));
		}
	}
	else if(rowLayout== R.layout.distributer_discussion_row)
	{
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.distributer_discussion_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
		}
		else
		{
			holder=(MyHolder)row.getTag();
			
		}
		holder.partyName.setText((dashboardModels.get(position).getPartyname()==null?"":dashboardModels.get(position).getPartyname()));
		holder.remarksTextView.setText(dashboardModels.get(position).getRemark());

	}
	else if(rowLayout== R.layout.competitor_list_row)
	{Log.d("mani","gus else gaya");
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.competitor_list_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
			
		}
		else
		{
			holder=(MyHolder)row.getTag();
			
		}
		holder.itemTextView.setText(dashboardModels.get(position).getItemName());
		holder.qtyTextview.setText(dashboardModels.get(position).getQuantity());
		holder.rateTextview.setText(dashboardModels.get(position).getRate());
	}
	else if(rowLayout== R.layout.order_product_list_datewise_row)
	{
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.order_product_list_datewise_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
		}
		else
		{
			holder=(MyHolder)row.getTag();
			
		}
		AppEnviroDataController appDataController=new AppEnviroDataController(context);
		appDataController.open();
		ArrayList<AppEnviroData> appDataArray = appDataController.getAppEnviroFromDb();
		appDataController.close();
		String appType="";
		for(int i=0;i<appDataArray.size();i++)
		{
			appType=appDataArray.get(i).getItemwisesale().trim();

		}

		if(appType.equalsIgnoreCase("Y")){
			holder.productNameOnOrderMgmt.setText(dashboardModels.get(position).getProductNameOnOrderMgmt());
			holder.priceProductOnOrderMgmt.setText(String.valueOf(dashboardModels.get(position).getPriceProductOnOrderMgmt()));
			holder.qtyProductOnOrderMgmt.setText(String.valueOf(dashboardModels.get(position).getQtyProductOnOrderMgmt()));
			holder.totalOrderValueOnOrderProductList.setText(new DecimalFormat("00.00").format(Double.parseDouble(String.valueOf(dashboardModels.get(position).getTotalOrderValueOnOrderProductList()))));
		}
		else{

			holder.productNameOnOrderMgmt.setText(dashboardModels.get(position).getItemName());
			holder.priceProductOnOrderMgmt.setText(String.valueOf(dashboardModels.get(position).getRate()));
			holder.qtyProductOnOrderMgmt.setText(String.valueOf(dashboardModels.get(position).getQuantity()));
		}
			row.setBackgroundColor(context.getResources().getColor(android.R.color.transparent)); //default colon
       
        if (mSelection.get(position) != null)
		{

			row.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));// this is a selected position so make it red//row.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
        }
		
	}
	else if(rowLayout== R.layout.demo_order_product_list_datewise_row)
	{Log.d("mani","gus else gaya");
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.demo_order_product_list_datewise_row,parent,false);
			holder=new MyHolder(row);
			row.setTag(holder);
			Log.d("mani","new");
		}
		else
		{
			holder=(MyHolder)row.getTag();
			Log.d("mani","old");
		} 
		if(!dashboardModels.get(position).getQtyProductOnDemoOrderMgmt().equals("0"))
		{
			holder.demoTypeOnDemoOrder.setText("Demo");
			holder.demoTypeOnDemoOrder.setTextColor(Color.parseColor("#0000FF"));
		}
		else if(!dashboardModels.get(position).getSamqty().equals("0"))
		{
			holder.demoTypeOnDemoOrder.setText("Sample");
			holder.demoTypeOnDemoOrder.setTextColor(Color.parseColor("#FF4000"));
			
			}
		    holder.productNameOnDemoOrderMgmt.setText(dashboardModels.get(position).getProductNameOnDemoOrderMgmt());
			holder.priceProductOnDemoOrderMgmt.setText(dashboardModels.get(position).getPriceProductOnDemoOrderMgmt());
			holder.qtyProductOnDemoOrderMgmt.setText(dashboardModels.get(position).getQtyProductOnDemoOrderMgmt());
			holder.sampleProductOnDemoOrderMgmt.setText(dashboardModels.get(position).getSamqty());
			holder.remarksOnDemoOrderMgmt.setText(dashboardModels.get(position).getRemark());

		row.setBackgroundColor(context.getResources().getColor(android.R.color.transparent)); //default colon
        
        if (mSelection.get(position) != null) {
        	row.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
        }
		
	}
	else if(rowLayout== R.layout.purchase_order_list_row)
	{Log.d("mani","gus else gaya");
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.purchase_order_list_row,parent,false);
			
			
			holder=new MyHolder(row);
			row.setTag(holder);
			Log.d("mani","new");
		}
		else
		{
			holder=(MyHolder)row.getTag();
			Log.d("mani","old");
		}
		System.out.println("mani qty "+String.valueOf(dashboardModels.get(position).getQuantityOnPruchaseOrder()));
			holder.productNameOnPruchaseOrder.setText(dashboardModels.get(position).getProductNameOnPruchaseOrder());
			holder.unitOnPruchaseOrder.setText(String.valueOf(dashboardModels.get(position).getUnitOnPruchaseOrder()));
			holder.quantityOnPruchaseOrder.setText(String.valueOf(dashboardModels.get(position).getQuantityOnPruchaseOrder()));
			holder.deleteBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					//new Custom_Toast(context, "position at "+position).showCustomAlert(); 
				}
			});
		
	}
	else if(rowLayout== R.layout.purchase_order_product_list_row)
	{Log.d("mani","gus else gaya");
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.purchase_order_product_list_row,parent,false);
			
			
			holder=new MyHolder(row);
			row.setTag(holder);
			Log.d("mani","new");
		}
		else
		{
			holder=(MyHolder)row.getTag();
			Log.d("mani","old");
		}
		System.out.println("mani qty "+String.valueOf(dashboardModels.get(position).getQuantityOnPruchaseOrder()));
			holder.productNameOnPruchaseOrder.setText(dashboardModels.get(position).getProductNameOnPruchaseOrder());
			holder.unitOnPruchaseOrder.setText(String.valueOf(dashboardModels.get(position).getUnitOnPruchaseOrder()));
			holder.quantityOnPruchaseOrder.setText(String.valueOf(dashboardModels.get(position).getQuantityOnPruchaseOrder()));
		
	}
	else if(rowLayout== R.layout.failed_visit_datewise_list_row)
	{Log.d("mani","gus else gaya");
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.failed_visit_datewise_list_row,parent,false);
			
			
			holder=new MyHolder(row);
			row.setTag(holder);
			Log.d("mani","new");
		}
		else
		{
			holder=(MyHolder)row.getTag();
			Log.d("mani","old");
		}
		System.out.println("mani hello "+dashboardModels.get(position).getProductNameOnOrderMgmt()+" "+dashboardModels.get(position).getPriceProductOnOrderMgmt()+" "+dashboardModels.get(position).getQtyProductOnOrderMgmt());
			holder.remarkFailedVisitOnOrderMgmt.setText(dashboardModels.get(position).getRemarkFailedVisitOnOrderMgmt());
			holder.nextVisitDateFailedVisitOnOrderMgmt.setText(dashboardModels.get(position).getNextVisitDateOnOrderMgmt());
		row.setBackgroundColor(context.getResources().getColor(android.R.color.transparent)); //default colon
        
        if (mSelection.get(position) != null) {
        	row.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
        }
		
	}
	else if(rowLayout== R.layout.purchaseorderhistory_list_row)
	{Log.d("mani","gus else gaya");
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.purchaseorderhistory_list_row,parent,false);
			
			
			holder=new MyHolder(row);
			row.setTag(holder);
			Log.d("mani","new");
		}
		else
		{
			holder=(MyHolder)row.getTag();
			Log.d("mani","old");
		}
		System.out.println("mani hello "+dashboardModels.get(position).getProductNameOnOrderMgmt()+" "+dashboardModels.get(position).getPriceProductOnOrderMgmt()+" "+dashboardModels.get(position).getQtyProductOnOrderMgmt());
			holder.date.setText(dashboardModels.get(position).getDateOfDate());
			holder.distributerName.setText(dashboardModels.get(position).getDitributorName());
		row.setBackgroundColor(context.getResources().getColor(android.R.color.transparent)); //default colon
        
        if (mSelection.get(position) != null) {
        	row.setBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
        }
		
	}
	else if(rowLayout== R.layout.reports_above_value_row)
	{Log.d("mani","gus else gaya");
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.reports_above_value_row,parent,false);
			
			
			holder=new MyHolder(row);
			row.setTag(holder);
			Log.d("mani","new");
		}
		else
		{
			holder=(MyHolder)row.getTag();
			Log.d("mani","old");
		}
			holder.partyNameOnReportAbove.setText(dashboardModels.get(position).getPartyNameOnReportAbove());
			holder.address.setText(dashboardModels.get(position).getAddress());
			holder.industryType.setText(dashboardModels.get(position).getIndustryType());
			holder.monbileNo.setText(dashboardModels.get(position).getMonbileNo());
		row.setBackgroundColor(context.getResources().getColor(android.R.color.transparent)); //default colon
		
	}
	else if(rowLayout== R.layout.reports_above_values_row)
	{Log.d("mani","gus else gaya");
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.reports_above_values_row,parent,false);
			
			
			holder=new MyHolder(row);
			row.setTag(holder);
			Log.d("mani","new");
		}
		else
		{
			holder=(MyHolder)row.getTag();
			Log.d("mani","old");
		}
			holder.partyNameOnReportAbove.setText(dashboardModels.get(position).getPartyNameOnReportAbove());
			holder.address.setText(dashboardModels.get(position).getAddress());
			holder.industryType.setText(dashboardModels.get(position).getIndustryType());
			holder.monbileNo.setText(dashboardModels.get(position).getMonbileNo());
			holder.potentail.setText(dashboardModels.get(position).getPotential());
		row.setBackgroundColor(context.getResources().getColor(android.R.color.transparent)); //default colon
		
	}
	else if(rowLayout== R.layout.beat_wise_monthly_secondry_row)
	{Log.d("mani","gus else gaya");
	Calendar calendar=Calendar.getInstance();
	int currentMonth=calendar.get(Calendar.MONTH);
	calendar.add(Calendar.MONTH, -1);
	int lastMonth=calendar.get((Calendar.MONTH));
	calendar.add(Calendar.MONTH, -1);
	int lastToLastMonth=calendar.get((Calendar.MONTH));
		if(row==null)
		{
			LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row=inflater.inflate(R.layout.beat_wise_monthly_secondry_row,parent,false);
			
			
			holder=new MyHolder(row);
			row.setTag(holder);
			Log.d("mani","new");
		}
		else
		{
			holder=(MyHolder)row.getTag();
			Log.d("mani","old");
		}
			holder.partyNameOnReportAbove.setText(dashboardModels.get(position).getPartyname());
			holder.monthFirst.setText(DateFunction.getMonthName(currentMonth));
			holder.monthSecond.setText(DateFunction.getMonthName(lastMonth));
			holder.monthThird.setText(DateFunction.getMonthName(lastToLastMonth));
			holder.monthFirstValue.setText(dashboardModels.get(position).getMonthfirstvalue());
			holder.monthSecondValue.setText(dashboardModels.get(position).getMonthsecondvalue());
			holder.monthThirdValue.setText(dashboardModels.get(position).getMonththirdvalue());
			
		row.setBackgroundColor(context.getResources().getColor(android.R.color.transparent)); //default colon
		
	}*/
	return row;
	
}


public class MyHolder
{
	public TextView dashboardItemName;public ImageView dashboardItemIcon;public TextView partyName,stock,stockTxt;public TextView partyAddress;public TextView partyVisit;
	public TextView productName;public EditText productPrice;public EditText qty1;EditText qty2;TextView nameProductList;TextView priceProductList;TextView mpProductList;
	public TextView dpProductList;TextView dateOnHistroy;TextView operationOnHistory;TextView dateOnOrderMgmt;TextView operationOnOrderMgmt;
	public TextView qty;public TextView remarksTextView;public Button order;public Button demo;public Button failedVisit;
	public TextView partyNameOnOrderMgmtRow,viewDetailLable;public Button competitor;public Button collection;
	public TextView beatNameViewOnOrderMgmtRow;public TextView partyNameOnOrderMgmtDemoRow;public TextView beatNameViewOnOrderMgmtDemoRow;
	public TextView partyNameOnOrderMgmtFailedVisitRow;public TextView beatNameViewOnOrderMgmtFailedVisitRow;
	public TextView productNameOnOrderMgmt;public TextView priceProductOnOrderMgmt;public TextView qtyProductOnOrderMgmt;
	public TextView productNameOnDemoOrderMgmt;public TextView priceProductOnDemoOrderMgmt;public TextView qtyProductOnDemoOrderMgmt;
	public TextView remarkFailedVisitOnOrderMgmt;public EditText quantityOnDemo,qtyInCarton,quantityOfProduct;
	public TextView productNameOnDemoRetailer;public TextView priceProductOnDemoRetailer;public TextView mpProductOnDemoRetailer;public TextView dpProductOnDemoRetailer;
	public Switch switch1;public TextView cartonqtyView, cartonqtyProduct, dpView,mpView;public TextView qtyTypeOnDemoOrderList;public TextView remarksOnDemoOrderMgmt;
	public TextView potentail,productNameOnPruchaseOrder;public TextView priceViewOnPruchaseOrder;public TextView quantityOnPruchaseOrder;
	public TextView totalOrderValueOnOrderProductList;public LinearLayout remarksLayoutOnDemoOrderList;public TextView demoTypeOnDemoOrder;
	public TextView date,distributerName,demoOrSample,unitOnPruchaseOrder,qtyOnDemoRetailer,priceView,docidOnHistory,sampleProductOnDemoOrderMgmt;public TextView sampleQtyOnDemoRetailer;public ImageButton remarksbtn;public LinearLayout remarklayout;public  Button remarksOkbtn;
	public Button cancelbtn;public TextView nextVisitDateFailedVisitOnOrderMgmt, nextVisitDateFailedVisit,nextVisitTimeFailedVisit, rpProductList,rpView,amount,itemId,sampleQty; int qtyEditRowId,rateEditRowId,amountEditRowId;
	public ImageButton deleteBtn;	public TextView partyNameOnReportAbove,address,contactPerson,industryType,monbileNo,monthFirst,monthSecond,monthThird,monthFirstValue,monthSecondValue,monthThirdValue;
	public TextView textClass,textSegment,textMatGrp,textRemark,orderAmount,orderRemark,itemTextView,qtyTextview,rateTextview;
	public TextView colremarks,partyamount,chqdate,chqdatetext,chqno,chqnotext,bank,banktext,mode,branch,branchtext,orderRemarkheading;
	public TextView areaName,syncId,active,other,vDate,document,nextVisitTime,vDocId,start,end,status,lock,beatNameLabel,
			remark,reason,prodClass,prodSegment,prodMatGrp,compName,city,billNo,claimAmount,stdPack,discount,imageView;
	Distributor distributor;LinearLayout layout1;ImageView star;TableRow monbileNoRow,addressRow,contactPersonRow;
	Button btn;
	TextView gstValLabel, isGSTREGLable,partyVendorNameLabel,cgstAmount,sgstAmountLabel;
	MyHolder(View view)
	{	
		if(rowLayout== R.layout.dashboard_row){
			dashboardItemName=(TextView)view.findViewById(R.id.itemName);
			dashboardItemIcon=(ImageView)view.findViewById(R.id.dashboardicon);
		}
		/*else if(rowLayout== R.layout.purchaseorderhistory_list_row)
		{
			date=(TextView)view.findViewById(R.id.date);



			distributerName=(TextView)view.findViewById(R.id.distributerName);
		}*/
		else if(rowLayout== R.layout.expense_layout_row)
		{
			gstValLabel = (TextView)view.findViewById(R.id.gstValue);
			compName=(TextView)view.findViewById(R.id.Text4);
			city=(TextView)view.findViewById(R.id.Text5);
			billNo=(TextView)view.findViewById(R.id.Text11);
			date=(TextView)view.findViewById(R.id.Text6);
			amount=(TextView)view.findViewById(R.id.Text9);
			claimAmount=(TextView)view.findViewById(R.id.Text10);
			other=(TextView)view.findViewById(R.id.supportText);

//			isGSTREGLable=(TextView)view.findViewById(R.id.gstRegValue);
//
//			partyVendorNameLabel = (TextView)view.findViewById(R.id.vendorNameLabel);
//			cgstAmount = (TextView)view.findViewById(R.id.CGSTAmountLabel);
//			sgstAmountLabel = (TextView)view.findViewById(R.id.SCGSTAmountLabel);

		}

		else if(rowLayout== R.layout.header_exp_grp_list_row)
		{
			compName=(TextView)view.findViewById(R.id.Text1);
			date=(TextView)view.findViewById(R.id.Text2);
			remark=(TextView)view.findViewById(R.id.Text3);
		}
		
//		else if(rowLayout== R.layout.find_beat_master_list)
//		{
//			beatNameViewOnOrderMgmtRow=(TextView)view.findViewById(R.id.beatname);
//			areaName=(TextView)view.findViewById(R.id.areaname);
//			remark=(TextView)view.findViewById(R.id.description);
//			syncId=(TextView)view.findViewById(R.id.syncId);
//			active=(TextView)view.findViewById(R.id.active);
//		}
//
//		else if(rowLayout== R.layout.new_party_list_row)
//		{
//			partyName=(TextView)view.findViewById(R.id.partyName);
//			monbileNo=(TextView)view.findViewById(R.id.mobile);
//			address=(TextView)view.findViewById(R.id.address);
//			contactPerson=(TextView)view.findViewById(R.id.contactTo);
//			monbileNoRow=(TableRow)view.findViewById(R.id.mobilerow);
//			addressRow=(TableRow)view.findViewById(R.id.addressrow);
//			contactPersonRow=(TableRow)view.findViewById(R.id.contactrow);
//			star=(ImageView)view.findViewById(R.id.starImage);
//
//		}
//		else if(rowLayout== R.layout.find_dsrlist_row)
//		{
//			vDate=(TextView)view.findViewById(R.id.visit_date);
//			//vDocId=(TextView)view.findViewById(R.id.doc_id);
//			start=(TextView)view.findViewById(R.id.start);
//			end=(TextView)view.findViewById(R.id.end);
//			status=(TextView)view.findViewById(R.id.status);
//			lock=(TextView)view.findViewById(R.id.lock);
//			remark=(TextView)view.findViewById(R.id.remark);
//		}
//
//		else if(rowLayout== R.layout.find_demolist_row)
//		{
//			vDate=(TextView)view.findViewById(R.id.visit_date);
//			vDocId=(TextView)view.findViewById(R.id.docno);
//			prodClass=(TextView)view.findViewById(R.id.pclass);
//			prodMatGrp=(TextView)view.findViewById(R.id.pgroup);
//			prodSegment=(TextView)view.findViewById(R.id.psegment);
//			remark=(TextView)view.findViewById(R.id.remark);
//			layout1=(LinearLayout)view.findViewById(R.id.Layout);
//			imageView=(TextView)view.findViewById(R.id.viewImage);
//			partyName=(TextView)view.findViewById(R.id.name);
//
//		}
		
//		else if(rowLayout== R.layout.find_bookorder_list_row)
//		{
//			vDate=(TextView)view.findViewById(R.id.visit_date);
//			document=(TextView)view.findViewById(R.id.documentnumber);
//			//vDocId=(TextView)view.findViewById(R.id.doc_id);
//			amount=(TextView)view.findViewById(R.id.orderAmount);
//			remark=(TextView)view.findViewById(R.id.orderdiscussion);
//		}
//		else if(rowLayout== R.layout.find_dist_discussion_row)
//		{
//			/******************************************* Ashutosh worked by 31/03/17 Start ********************************/
//			TableRow tbdetails=(TableRow)view.findViewById(R.id.rowdetails);
///******************************************* Ashutosh worked by 31/03/17 End ********************************/
//			TableRow tb=(TableRow)view.findViewById(R.id.fddrtablerow);
//			start=(TextView)view.findViewById(R.id.starttime);
//			end=(TextView)view.findViewById(R.id.endtime);
//			nextVisitTime = (TextView)view.findViewById(R.id.visittime);
//			vDate=(TextView)view.findViewById(R.id.visitdate);
//			remark=(TextView)view.findViewById(R.id.remark);
//			layout1=(LinearLayout)view.findViewById(R.id.Layout);
//			vDocId=(TextView)view.findViewById(R.id.disdate);
//			distributerName=(TextView)view.findViewById(R.id.disname);
//			partyName=(TextView)view.findViewById(R.id.nameView);
//			stock=(TextView)view.findViewById(R.id.stock);
//			stockTxt=(TextView)view.findViewById(R.id.stockTxt);
//			imageView=(TextView)view.findViewById(R.id.viewImage);
//			AppEnviroDataController appDataController=new AppEnviroDataController(context);
//			appDataController.open();
//			ArrayList<AppEnviroData> appDataArray = appDataController.getAppEnviroFromDb();
//			appDataController.close();
//			String appType="";
//			for(int i=0;i<appDataArray.size();i++)
//			{
//				appType=appDataArray.get(i).getDistDiscussionStock().trim();
//
//			}
//
//			if(appType.equalsIgnoreCase("Y")){
//				stock.setVisibility(View.VISIBLE);
//				stockTxt.setVisibility(View.VISIBLE);
//			}
//
//			else {
//			stock.setVisibility(View.GONE);
//            stockTxt.setVisibility(View.GONE);
//			}
//		}

//		else if(rowLayout== R.layout.find_collection_list_row)
//		{
//			vDate=(TextView)view.findViewById(R.id.disdate);
//			//vDocId=(TextView)view.findViewById(R.id.doc_id);
//			distributerName=(TextView)view.findViewById(R.id.disname);
//			partyName=(TextView)view.findViewById(R.id.nameView);
//			amount=(TextView)view.findViewById(R.id.amount);
//			mode=(TextView)view.findViewById(R.id.mode);
//			chqno=(TextView)view.findViewById(R.id.insno);
//			chqdate=(TextView)view.findViewById(R.id.insdate);
//			chqdatetext=(TextView)view.findViewById(R.id.instDateView);
//			chqnotext=(TextView)view.findViewById(R.id.instView);
//
//
//		}
		
//		else if(rowLayout== R.layout.find_competitor_list_row)
//		{
//			partyName = (TextView)view.findViewById(R.id.party_name);
//			vDate=(TextView)view.findViewById(R.id.visit_date);
//			vDocId=(TextView)view.findViewById(R.id.fclrdocno);
//			compName=(TextView)view.findViewById(R.id.competitor);
//			productName=(TextView)view.findViewById(R.id.item);
//			rateTextview=(TextView)view.findViewById(R.id.rate);
//			stdPack=(TextView)view.findViewById(R.id.stdPacking);
//			discount=(TextView)view.findViewById(R.id.discount);
//			remark=(TextView)view.findViewById(R.id.otherActivity);
//			imageView=(TextView)view.findViewById(R.id.viewImage);
//			layout1=(LinearLayout)view.findViewById(R.id.Layout);
//			reason=(TextView)view.findViewById(R.id.reason);
//		}

//		else if(rowLayout== R.layout.find_competitor_list_row_history)
//		{
//			partyName = (TextView)view.findViewById(R.id.party_name);
//			vDate=(TextView)view.findViewById(R.id.visit_date);
//			vDocId=(TextView)view.findViewById(R.id.fclrdocno);
//			compName=(TextView)view.findViewById(R.id.competitor);
//			productName=(TextView)view.findViewById(R.id.item);
//			rateTextview=(TextView)view.findViewById(R.id.rate);
//			stdPack=(TextView)view.findViewById(R.id.stdPacking);
//			discount=(TextView)view.findViewById(R.id.discount);
//			remark=(TextView)view.findViewById(R.id.otherActivity);
//			imageView=(TextView)view.findViewById(R.id.viewImage);
//			layout1=(LinearLayout)view.findViewById(R.id.Layout);
//			reason=(TextView)view.findViewById(R.id.reason);
//			viewDetailLable = (TextView)view.findViewById(R.id.viewDetail);
//
//		}
//		else if(rowLayout==R.layout.book_order_list_row)
//		{
//			productName=(TextView)view.findViewById(R.id.productName);
//			productPrice=(EditText)view.findViewById(R.id.priceView);
//			quantityOfProduct=(EditText)view.findViewById(R.id.quantityOfProduct);
//			qtyInCarton=(EditText)view.findViewById(R.id.qtyInCartoon);
//			amount=(TextView)view.findViewById(R.id.amount);
//			itemId=(TextView)view.findViewById(R.id.itemId);
//		}
//		else if(rowLayout== R.layout.product_list_row)
//		{
//			nameProductList=(TextView)view.findViewById(R.id.productName);
//			priceProductList=(TextView)view.findViewById(R.id.priceProduct);
//			mpProductList=(TextView)view.findViewById(R.id.mpProduct);
//			dpProductList=(TextView)view.findViewById(R.id.dpProduct);
//			rpProductList=(TextView)view.findViewById(R.id.rpProduct);
//			dpView=(TextView)view.findViewById(R.id.dpView);
//			mpView=(TextView)view.findViewById(R.id.mpView);
//			rpView=(TextView)view.findViewById(R.id.RpView);
//			cartonqtyProduct=(TextView)view.findViewById(R.id.cartonqtyProduct);
//			cartonqtyView=(TextView)view.findViewById(R.id.cartonqtyView);
//
//			priceView=(TextView)view.findViewById(R.id.priceView);
//		}
//		else if(rowLayout== R.layout.history_list_row)
//		{
//			dateOnHistroy=(TextView)view.findViewById(R.id.dateOnhistory);
//			operationOnHistory=(TextView)view.findViewById(R.id.whichOperationOnhistroy);
//			docidOnHistory=(TextView)view.findViewById(R.id.docidhistory);
//		}
//		else if(rowLayout== R.layout.order_managemnt_list_row)
//		{
//			dateOnOrderMgmt=(TextView)view.findViewById(R.id.dateOnOrderMgnt);
//			order=(Button)view.findViewById(R.id.orderOnOrderMgnt);
//			demo=(Button)view.findViewById(R.id.demoOnOrderMgmt);
//			failedVisit=(Button)view.findViewById(R.id.failedVisitOnOrderMgmt);
//			switch1=(Switch)view.findViewById(R.id.switch1);
//
//		}
//		else if(rowLayout== R.layout.demo_order_product_list_row)
//		{
//			textClass=(TextView)view.findViewById(R.id.textclass);
//			textSegment=(TextView)view.findViewById(R.id.textsegment);
//			textMatGrp=(TextView)view.findViewById(R.id.textmatgrp);
//			textRemark=(TextView)view.findViewById(R.id.remarkText);
////			demoOrSample=(TextView)view.findViewById(R.id.demoOrSample);
//		}
//		else if(rowLayout== R.layout.order_product_list_row)
//		{
//			orderAmount=(TextView)view.findViewById(R.id.orderEditText);
//			orderRemark=(TextView)view.findViewById(R.id.remarkText);
//			vDocId=(TextView)view.findViewById(R.id.docId);
//
//		}
//		else if(rowLayout== R.layout.reamrks_failed_list_row)
//		{
//			remarksTextView=(TextView)view.findViewById(R.id.remarkFailedVisite);
//			nextVisitDateFailedVisit=(TextView)view.findViewById(R.id.nextVisitDateFailedVisit);
//			vDocId=(TextView)view.findViewById(R.id.docId);
//			reason=(TextView)view.findViewById(R.id.reasonFailedVisite);
//
//		}
//		else if(rowLayout== R.layout.distributer_discussion_row)
//		{
//			remarksTextView=(TextView)view.findViewById(R.id.remarkdist);
//			partyName=(TextView)view.findViewById(R.id.distName);
//
//		}
//		else if(rowLayout== R.layout.party_collection_list_row)
//		{
//			partyName=(TextView)view.findViewById(R.id.partyname01);
//			vDate=(TextView)view.findViewById(R.id.disdate);
//			distributerName=(TextView)view.findViewById(R.id.disname);
//			amount=(TextView)view.findViewById(R.id.amount);
//			mode=(TextView)view.findViewById(R.id.mode);
//			chqno=(TextView)view.findViewById(R.id.insno);
//			chqdate=(TextView)view.findViewById(R.id.insdate);
//			layout1=(LinearLayout)view.findViewById(R.id.Layout);
//			chqdatetext=(TextView)view.findViewById(R.id.instDateView);
//			chqnotext=(TextView)view.findViewById(R.id.instView);
//
//
//		}
//		else if(rowLayout== R.layout.competitor_list_row)
//		{
//			itemTextView=(TextView)view.findViewById(R.id.ItemDetails);
//			qtyTextview=(TextView)view.findViewById(R.id.quantityDetails);
//			rateTextview=(TextView)view.findViewById(R.id.rateDetails);
//		}
//
//		else if(rowLayout== R.layout.partylist_ordermgmtwise_row)
//		{
//			partyNameOnOrderMgmtRow=(TextView)view.findViewById(R.id.partyNameOnOrderMgmtRow);
//			beatNameViewOnOrderMgmtRow=(TextView)view.findViewById(R.id.beatNameViewOnOrderMgmtRow);
//		    orderAmount=(TextView)view.findViewById(R.id.orderAmount);
//		    orderRemark=(TextView)view.findViewById(R.id.remark);
//			beatNameLabel = (TextView)view.findViewById(R.id.beatName);
//		    orderRemarkheading=(TextView)view.findViewById(R.id.qty);
//			remark=(TextView)view.findViewById(R.id.textViewremark);
//			btn=(Button) view.findViewById(R.id.button);
//
//		}
//		else if(rowLayout== R.layout.partylist_ordermgmtwise_row_astral)
//		{
//			partyNameOnOrderMgmtRow=(TextView)view.findViewById(R.id.partyNameOnOrderMgmtRow);
//			beatNameViewOnOrderMgmtRow=(TextView)view.findViewById(R.id.beatNameViewOnOrderMgmtRow);
//			orderAmount=(TextView)view.findViewById(R.id.orderAmount);
//			orderRemark=(TextView)view.findViewById(R.id.remark);
//			beatNameLabel = (TextView)view.findViewById(R.id.beatName);
//			orderRemarkheading=(TextView)view.findViewById(R.id.qty);
//			remark=(TextView)view.findViewById(R.id.textViewremark);
//			btn=(Button) view.findViewById(R.id.button);
//
//		}
//		else if(rowLayout== R.layout.demopartylist_ordermgmtwise_row)
//		{
//			partyNameOnOrderMgmtDemoRow=(TextView)view.findViewById(R.id.partyNameOnOrderMgmtDemoRow);
//			beatNameViewOnOrderMgmtDemoRow=(TextView)view.findViewById(R.id.beatNameViewOnOrderMgmtDemoRow);
//			textClass=(TextView)view.findViewById(R.id.className);
//			textSegment=(TextView)view.findViewById(R.id.segmentName);
//			textMatGrp=(TextView)view.findViewById(R.id.mgrpName);
//			textRemark=(TextView)view.findViewById(R.id.remName);
//
//		}
//		else if(rowLayout== R.layout.faliedvisit_list_ordermgmtdatewise_row)
//		{
//			partyNameOnOrderMgmtFailedVisitRow=(TextView)view.findViewById(R.id.partyNameOnOrderMgmtFailedVisitRow);
//			productNameOnDemoOrderMgmt=(TextView)view.findViewById(R.id.mobileNoOnOrderMgmtRow);
//			beatNameViewOnOrderMgmtFailedVisitRow=(TextView)view.findViewById(R.id.beatNameViewOnOrderMgmtFailedVisitRow);
//			remarksTextView=(TextView)view.findViewById(R.id.remark);
//			nextVisitDateFailedVisit=(TextView)view.findViewById(R.id.nextVisitDateFailedVisitOnOrderMgmt);
//			nextVisitTimeFailedVisit=(TextView)view.findViewById(R.id.vtime);
//			compName=(TextView)view.findViewById(R.id.name1);
//			reason=(TextView)view.findViewById(R.id.reason);
//			layout1=(LinearLayout)view.findViewById(R.id.Layout);
//
//		}
//		else if(rowLayout== R.layout.order_product_list_datewise_row)
//		{
//			productNameOnOrderMgmt=(TextView)view.findViewById(R.id.productNameOnOrderMgmt);
//			priceProductOnOrderMgmt=(TextView)view.findViewById(R.id.priceProductOnOrderMgmt);
//			qtyProductOnOrderMgmt=(TextView)view.findViewById(R.id.qtyProductOnOrderMgmt);
//			totalOrderValueOnOrderProductList=(TextView)view.findViewById(R.id.totalOrderValueOnOrderProductList);
//		}
//		else if(rowLayout== R.layout.demo_order_product_list_datewise_row)
//		{
//			productNameOnDemoOrderMgmt=(TextView)view.findViewById(R.id.productNameOnDemoOrderMgmt);
//			priceProductOnDemoOrderMgmt=(TextView)view.findViewById(R.id.priceProductOnDemoOrderMgmt);
//			qtyProductOnDemoOrderMgmt=(TextView)view.findViewById(R.id.qtyProductOnDemoOrderMgmt);
//			qtyTypeOnDemoOrderList=(TextView)view.findViewById(R.id.qtyTypeOnDemoOrderList);
//			remarksLayoutOnDemoOrderList=(LinearLayout)view.findViewById(R.id.remarksLayoutOnDemoOrderList);
//			remarksOnDemoOrderMgmt=(TextView)view.findViewById(R.id.remarksOnDemoOrderMgmt);
//			demoTypeOnDemoOrder=(TextView)view.findViewById(R.id.demoTypeOnDemoOrder);
//			sampleProductOnDemoOrderMgmt=(TextView)view.findViewById(R.id.sampleProductOnDemoOrderMgmt);
//
//		}
//		else if(rowLayout== R.layout.failed_visit_datewise_list_row)
//		{
//			remarkFailedVisitOnOrderMgmt=(TextView)view.findViewById(R.id.remarkFailedVisitOnOrderMgmt);
//			nextVisitDateFailedVisitOnOrderMgmt=(TextView)view.findViewById(R.id.nextVisitDateFailedVisitOnOrderMgmt);
//		}
	}
}

public class InfoRowdata {
    public boolean isclicked=false;
    public int index;public String data=null;

    public InfoRowdata(boolean isclicked,int index/*,String fanId,String strAmount*/)
    {
        this.index=index;
        this.isclicked=isclicked;
    }
}
public interface HolderListener{
	  public void holderListener(MyHolder myHolder);
	}

	public interface ExpenseTransferInterface {
		public void getPopupForUpdate(String expType);
	}
		
	class DownloadImage extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
             progressDialog= ProgressDialog.show(context,"Proccessing", "Proccessing Please wait...", true);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
			final Dialog dialog = new Dialog(context);
				// Include dialog.xml file
				 dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);             dialog.setContentView(R.layout.image_dialog);
				// Set dialog title
				dialog.setTitle("View Image");
				ImageView image = (ImageView) dialog.findViewById(R.id.imageDialogView);
				//System.out.println("Value "+dashboardModels.get(position).getPicture());
				SharedPreferences dpreferences_new = context.getSharedPreferences("DYNAMIC_FORM_FIELDS_INFO", MODE_PRIVATE);
				String image_data = dpreferences_new.getString("Show_UseCamera",null);

			try{
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 8;
				File imgFile = new  File(filePath);

				if(imgFile.exists()){

					Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

					if(myBitmap != null)
					{
						LoadActivity loadActivity=new LoadActivity(context);
						myBitmap=loadActivity.fitImage(myBitmap);
						image.setImageBitmap(myBitmap);
					}



				}

					/*BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 8;
					 Bitmap bitmap = BitmapFactory.decodeFile(filePath);

//					System.out.println("demo.getFilePath()"+dashboardModels.get(position).getPicture());
					if(bitmap!=null){
					LoadActivity loadActivity=new LoadActivity(context);
					bitmap=loadActivity.fitImage(bitmap);
					//image.setImageBitmap(loadActivity.fitImage(bitmap));
					image.setImageBitmap(bitmap);
				}*/
			}catch(Exception e){}
				
				dialog.show();
				Button declineButton = (Button) dialog.findViewById(R.id.declineButton);
				// if decline button is clicked, close the custom dialog
				declineButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// Close dialog
						dialog.dismiss();
					}
				});
            progressDialog.dismiss();
        }
        @Override
        protected String doInBackground(String... params) {
            try {
                filePath=downloadImage(path,prefix);
            } catch (Exception e) {
                progressDialog.dismiss();
            }
            return null;
        }
		}

	 public String downloadImage(String path,String prefix)
 {
String server="";
        File file=null;
        int pos;String imgurl="";
		
        if(!path.equalsIgnoreCase("")) {
            if (!path.equalsIgnoreCase("NA")) {
            	
            	File Locfile = new File(path);
            	 if (Locfile.exists()) {
            		  progressDialog.dismiss(); 
                     return path;
                 }
            	 else{
		appDataController1=new AppDataController(this.context);
		AppData appData;
		appDataController1.open();
		appDataArray = appDataController1.getAppTypeFromDb();
		appDataController1.close();
		appData = appDataArray.get(0);
		server=appData.getCompanyUrl();
                pos = path.indexOf("/");
                imgurl = path.substring(pos + 1, path.length());
				imgurl=imgurl.replaceAll(" ", "%20");
                System.out.println(imgurl);
                try {
                    URL url = new URL("http://" + server + "/" + imgurl); //you can write here any link
                    File dir = new File(Environment.getExternalStorageDirectory(), "DmCRM");
                    if (!dir.exists())
                        dir.mkdirs();
                    Date date = new Date();
                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
                    file = new File(dir.getPath() + "/" + prefix + timeStamp + ".jpg");
                    URLConnection ucon = url.openConnection();
                    InputStream is = ucon.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is);
                    /*
                     * Read bytes to the Buffer until there is nothing more to read(-1).
                     */
                    ByteArrayBuffer baf = new ByteArrayBuffer(50);
                    int current = 0;
                    while ((current = bis.read()) != -1) {
                        baf.append((byte) current);
                    }
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(baf.toByteArray());
                    fos.close();
                 ContentValues values = new ContentValues();
            	values.put(DatabaseConnection.COLUMN_IMAGE_PATH, file.getPath());
                DbCon dbCon=new DbCon(context);
                dbCon.open();
                dbCon.update(table,values, DatabaseConnection.COLUMN_ANDROID_DOCID+"='"+AndroidId+"'");
                dbCon.close();        
				dashboardModels.get(positionImage).setPicture(file.getPath());
                } catch (IOException e) {
                    System.out.println(e);
                }
                }
            }else{
			new Custom_Toast(context, "No Image Found").showCustomAlert();
			  progressDialog.dismiss();
			  return "";
		}
        }
		else{
			new Custom_Toast(context, "No Image Found").showCustomAlert();
			  progressDialog.dismiss();
			  return "";
		}
        return (file==null?"NA":file.getPath());
    }
}
