package com.dm.library;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.controller.SmanController;
import com.dm.controller.TransLeaveController;
import com.dm.crmdm_app.AprovalLiveDetails;
import com.dm.crmdm_app.LeaveRequest;
import com.dm.crmdm_app.R;
import com.dm.model.DashboardModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class CustomArrayAdapterLeaveRequestList extends
		ArrayAdapter<DashboardModel> {

	private final ArrayList<DashboardModel> dashboardModels;
	private String witchRow;
	private int rowLayout;
	private int textView;
	private final Context context;
	private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
	private static String checkData;
	String dateText;
	private ArrayList<InfoRowdata> infodata;
	static String dataForCheck;
	AlertBoxWithView dialogWithView;
	final HashMap<Integer, String> savedQtyData = new HashMap<Integer, String>();
	final HashMap<Integer, String> savedRateData = new HashMap<Integer, String>();
	String[] postValue;
	DataTransferInterface dataTransferInterface;
	ArrayList<MyHolder> arrayList;
	DataTransfer dataTransfer;
	HolderListener holderListener;
	String qtyOfProduct;
	static ArrayList<LeaveRequest> leaveRequestList;
	ArrayList<MyHolder> myHolders;
	View view;
	LeaveRequest leaveRequest;
	ListView listView;
	SharedPreferences preferences;
	FindLeaveRequestTransactionListener findLeaveRequestTransactionListener;
	// UpdateTotalValueOrder totalValueOrder;
	ArrayList<LeaveRequest> leaveRequestList2;
	TransLeaveController transLeaveController;
	LeaveRequest leaveRequest1;
	int pos = 0;
	SmanController smanController;
	private String[] amountEdit_values;
	public String[] qtyEdit_values;

	public CustomArrayAdapterLeaveRequestList(
			Context context,
			ArrayList<DashboardModel> dashboardModels,
			int rowLayout,
			int textView,
			HolderListener dataTransferInterface,
			FindLeaveRequestTransactionListener findLeaveRequestTransactionListener) {
		super(context, rowLayout, textView, dashboardModels);
		this.holderListener = dataTransferInterface;
		this.context = context;
		this.dashboardModels = dashboardModels;
		this.textView = textView;
		this.rowLayout = rowLayout;
		this.qtyEdit_values = new String[dashboardModels.size()];
		this.amountEdit_values = new String[dashboardModels.size()];
		this.postValue = new String[dashboardModels.size()];
		this.findLeaveRequestTransactionListener = findLeaveRequestTransactionListener;
		smanController=new SmanController(context);
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

	public static void setdata(String data) {
		dataForCheck = data;

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
		// TODO Auto-generated method stub
		// View v = super.getView(position, convertView, parent);
		view = parent;
		View row = convertView;
		final MyHolder holder;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.find_leave_request_list_row, parent, false);
			holder = new MyHolder(row);
			row.setTag(holder);

		} else {

			holder = (MyHolder) row.getTag();

		}
		try{
			holder.applicationDate.setText(dashboardModels.get(position).getVdate());

		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		holder.docId.setText(dashboardModels.get(position).getDocid());
		//holder.androidId.setText(dashboardModels.get(position).getAndroidId());
		//holder.fromDate.setText(DateFunction.ToConvertDateFormat(dashboardModels.get(position).getFromDate(),"yyyy-MM-dd HH:mm:ss","dd/MMM/yyyy"));
		//holder.toDAte.setText(DateFunction.ToConvertDateFormat(dashboardModels.get(position).getToDate(),"yyyy-MM-dd HH:mm:ss","dd/MMM/yyyy"));

		holder.fromDate.setText(dashboardModels.get(position).getFromDate());
		holder.toDAte.setText(dashboardModels.get(position).getToDate());
		holder.days.setText(dashboardModels.get(position).getNoOfDays());
		holder.reason.setText(dashboardModels.get(position).getReason());
		holder.remark.setText(dashboardModels.get(position).getRemark());
		holder.status.setText(dashboardModels.get(position).getStatus());
		//smanController.open();
		//ArrayList<Sman>smanList=smanController.getName(dashboardModels.get(position).getUserId());
		//smanController.close();
		holder.sales.setText(dashboardModels.get(position).getSamqty());

		if(dashboardModels.get(position).getStatus().toString().equalsIgnoreCase("Reject")){
			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String aproval="true";
					Intent i=new Intent(context,AprovalLiveDetails.class);
					i.putExtra("Data", "EditLeave");
					i.putExtra("LEAVEDOCID_SESSION", dashboardModels.get(position).getLock());
					i.putExtra("Days", dashboardModels.get(position).getNoOfDays());
					i.putExtra("APPLICATION_DATE_SESSION",dashboardModels.get(position).getVdate());
					i.putExtra("FROMDATE_SESSION", dashboardModels.get(position).getFromDate());
					i.putExtra("TODATE_SESSION",dashboardModels.get(position).getToDate());
					i.putExtra("Reason", dashboardModels.get(position).getReason());
					i.putExtra("Status",dashboardModels.get(position).getStatus());
					i.putExtra("lEAVE_ANDROID_ID_SESSION",dashboardModels.get(position).getAndroidId());
					i.putExtra("LEAVE_USER_ID_SESSION", dashboardModels.get(position).getUserId());
					i.putExtra("Flag",dashboardModels.get(position).getDoc_id());
					i.putExtra("DropId", dashboardModels.get(position).getSyncId());
					i.putExtra("remark", dashboardModels.get(position).getRemark());
					i.putExtra("aproval",aproval);
					i.putExtra("App_remark",dashboardModels.get(position).getRemark());
					i.putExtra("AppStatus","Reject");
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(i);
				}
			});

		}
		else if(dashboardModels.get(position).getStatus().toString().equalsIgnoreCase("Approve")){
			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String aproval="true";
					Intent i=new Intent(context,AprovalLiveDetails.class);
					i.putExtra("Data", "EditLeave");
					i.putExtra("LEAVEDOCID_SESSION", dashboardModels.get(position).getLock());
					i.putExtra("Days", dashboardModels.get(position).getNoOfDays());
					i.putExtra("APPLICATION_DATE_SESSION",dashboardModels.get(position).getVdate());
					i.putExtra("FROMDATE_SESSION", dashboardModels.get(position).getFromDate());
					i.putExtra("TODATE_SESSION",dashboardModels.get(position).getToDate());
					i.putExtra("Reason", dashboardModels.get(position).getReason());
					i.putExtra("Status",dashboardModels.get(position).getStatus());
					i.putExtra("lEAVE_ANDROID_ID_SESSION",dashboardModels.get(position).getAndroidId());
					i.putExtra("LEAVE_USER_ID_SESSION", dashboardModels.get(position).getUserId());
					i.putExtra("Flag",dashboardModels.get(position).getDoc_id());
					i.putExtra("DropId", dashboardModels.get(position).getSyncId());
					i.putExtra("remark", dashboardModels.get(position).getRemark());
					i.putExtra("aproval",aproval);
					i.putExtra("App_remark",dashboardModels.get(position).getRemark());
					i.putExtra("AppStatus","Approve");
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(i);
				}
			});

		}
      else {
			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent i = new Intent(context, LeaveRequest.class);
					i.putExtra("Data", "EditLeave");
					i.putExtra("LEAVEDOCID_SESSION", dashboardModels.get(position).getLock());
					i.putExtra("Days", dashboardModels.get(position).getNoOfDays());
					i.putExtra("APPLICATION_DATE_SESSION", dashboardModels.get(position).getVdate());
					i.putExtra("FROMDATE_SESSION", dashboardModels.get(position).getFromDate());
					i.putExtra("TODATE_SESSION", dashboardModels.get(position).getToDate());
					i.putExtra("Reason", dashboardModels.get(position).getReason());
					i.putExtra("Status", dashboardModels.get(position).getStatus());
					i.putExtra("lEAVE_ANDROID_ID_SESSION", dashboardModels.get(position).getAndroidId());
					i.putExtra("LEAVE_USER_ID_SESSION", dashboardModels.get(position).getUserId());
					i.putExtra("Flag", dashboardModels.get(position).getDoc_id());
					i.putExtra("DropId", dashboardModels.get(position).getSyncId());
					i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(i);
				}
			});
		}
		return row;
	}

	public class MyHolder {
		public TextView applicationDate, docId, androidId, fromDate, toDAte, sales, days,
				reason,remark,status;
		//public Button updateRecord,deleteRecord;

		MyHolder(View view) {
			applicationDate=(TextView) view.findViewById(R.id.appDate);
			applicationDate.setTextColor(Color.parseColor("#757575"));
			docId = (TextView) view.findViewById(R.id.document);
			docId.setTextColor(Color.parseColor("#757575"));
			//androidId = (TextView) view.findViewById(R.id.androidId);
			fromDate = (TextView) view.findViewById(R.id.fromDate);
			fromDate.setTextColor(Color.parseColor("#757575"));
			toDAte = (TextView) view.findViewById(R.id.toDate);
			toDAte.setTextColor(Color.parseColor("#757575"));
			sales = (TextView) view.findViewById(R.id.sales);
			sales.setTextColor(Color.parseColor("#757575"));
			days = (TextView) view.findViewById(R.id.days);
			days.setTextColor(Color.parseColor("#757575"));
			reason = (TextView) view.findViewById(R.id.reason);
			reason.setTextColor(Color.parseColor("#757575"));
			remark = (TextView) view.findViewById(R.id.remark);
			remark.setTextColor(Color.parseColor("#757575"));
			status = (TextView) view.findViewById(R.id.status);
			status.setTextColor(Color.parseColor("#757575"));
			//updateRecord = (Button) view.findViewById(R.id.button1);
		}
	}

	public class InfoRowdata {

		public boolean isclicked = false;
		public int index;
		public String data = null;

		/*
		 * public String fanId; public String strAmount;
		 */

		public InfoRowdata(boolean isclicked, int index/*
														 * ,String fanId,String
														 * strAmount
														 */) {
			this.index = index;
			this.isclicked = isclicked;
			/*
			 * this.fanId=fanId; this.strAmount=strAmount;
			 */
		}
	}

	public interface FindLeaveRequestTransactionListener {
		public void holderListener(ArrayList<LeaveRequest> leaveRequestList);
	}

	public interface HolderListener {
		public void holderListener(MyHolder myHolders);
	}



}
