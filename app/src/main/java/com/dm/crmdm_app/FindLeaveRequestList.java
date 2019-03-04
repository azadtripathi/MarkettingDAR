package com.dm.crmdm_app;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.widget.ListView;

import com.dm.controller.CompetitorController;
import com.dm.controller.TransLeaveController;
import com.dm.library.AlertMessage;
import com.dm.library.CustomArrayAdapterLeaveRequestList;
import com.dm.library.CustomArrayAdapterLeaveRequestList.FindLeaveRequestTransactionListener;
import com.dm.model.DashboardModel;
import com.dm.model.TransLeaveRequest;

import java.util.ArrayList;

public class FindLeaveRequestList extends Activity implements
		AlertMessage.NoticeDialogListenerWithoutView,
        FindLeaveRequestTransactionListener,
		CustomArrayAdapterLeaveRequestList.HolderListener {
	SharedPreferences preferences, preferences2;
	DashboardModel model;
	CompetitorController competitorController;
	ArrayList<DashboardModel> dashboardModels;
	ListView listView;
	ActionMode modeCustom;
	TransLeaveController transLeaveController;
	CustomArrayAdapterLeaveRequestList customArrayAdapterLeaveRequestList;
	ArrayList<TransLeaveRequest> transLeaveArrayList;
	SharedPreferences preference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_leave_request_list);
		listView = (ListView) findViewById(R.id.findLeaveRequestlist);
		setTitle("Leave List");
		preferences2 = this.getSharedPreferences("MyPref", MODE_PRIVATE);
		transLeaveController = new TransLeaveController(
				FindLeaveRequestList.this);
		preference = this.getSharedPreferences("MyfindpartyPref",
				MODE_PRIVATE);
		customArrayAdapterLeaveRequestList = new CustomArrayAdapterLeaveRequestList(
				this, feedDahboardItemData(), R.layout.find_leave_request_list_row,
				R.id.apptextdate, FindLeaveRequestList.this,
				FindLeaveRequestList.this);
		listView.setAdapter(customArrayAdapterLeaveRequestList);
		listView.setClickable(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.find_leave_request_list, menu);
		return true;
	}

	private ArrayList<DashboardModel> feedDahboardItemData() {
		String qty = null, rate = null;
		double sum = 0;
		// dashboardModels.clear();
		transLeaveController.open();
		dashboardModels = new ArrayList<DashboardModel>();
		model = new DashboardModel();
		transLeaveArrayList = transLeaveController.getFindLeaveRequestList();
		transLeaveController.close();
		for (int i = 0; i < transLeaveArrayList.size(); i++) {
			dashboardModels.add(model.findLeaveRequestListModel(
					transLeaveArrayList.get(i).getLeaveDocId(),
					transLeaveArrayList.get(i).getAndroid_id(),
					transLeaveArrayList.get(i).getSmid(), transLeaveArrayList
							.get(i).getVdate(), transLeaveArrayList.get(i)
							.getNoOfDay(), transLeaveArrayList.get(i)
							.getFromDate(), transLeaveArrayList.get(i)
							.getToDate(), transLeaveArrayList.get(i)
							.getReason(), transLeaveArrayList.get(i)
							.getAppStatus(), transLeaveArrayList.get(i)
							.getAppRemark()));
		}

		return dashboardModels;
	}




	@Override
	public void onDialogPositiveWithoutViewClick(DialogFragment dialog) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDialogNegativeWithoutViewClick(DialogFragment dialog) {
		// TODO Auto-generated method stub

	}



	@Override
	public void holderListener(ArrayList<LeaveRequest> leaveRequestList) {
		// TODO Auto-generated method stub

	}

	@Override
	public void holderListener(
			CustomArrayAdapterLeaveRequestList.MyHolder myHolders) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

	}



}
