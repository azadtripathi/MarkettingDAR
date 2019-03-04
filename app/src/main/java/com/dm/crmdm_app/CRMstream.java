package com.dm.crmdm_app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.controller.AppDataController;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomFindContactAdapter;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.library.DatePickerFragmentRange;
import com.dm.library.IntentSend;
import com.dm.model.AddContactModel;
import com.dm.model.AppData;
import com.dm.model.Owner;
import com.dm.parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 6/15/2017.
 */
public class CRMstream extends ActionBarActivity {
    String timestamp = "0";
    SharedPreferences preferences2;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    String server;
    CustomFindContactAdapter adaptor;
    AlertOkDialog dialogWithOutView;
    ConnectionDetector connectionDetector;
    ListView listview;
    EditText editTextSearch;
    RelativeLayout progressBarLayout;
    boolean isMoreData = false, lodingProgressImage = true;
    ProgressDialog progressDialog;
    String currentDate;
    ArrayList<AddContactModel> arrayListData = new ArrayList<>();
    Spinner spinner;
    FloatingActionButton fab;
    public Dialog dialog;
    ArrayList<String> ownerArrayList = new ArrayList<String>();
    ArrayList<String> ownerIDArrayList = new ArrayList<String>();
    String SMID;
    EditText editTextDate;
    String UserName = "";
    boolean isSelected = false;
    ArrayList<Owner> taskstatusArrayList = new ArrayList<>();
    public static String Flag = "T";
    String rowId = "0";
    public static int spinner_selected_index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionDetector = new ConnectionDetector(CRMstream.this);
        if (connectionDetector.isConnectingToInternet()) {
            setContentView(R.layout.activity_crmstream);
            // setContentView(R.layout.listview_crmstream);
            getSupportActionBar().setCustomView(R.layout.actionbar_custom_view_home);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayUseLogoEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            ImageView iv = (ImageView) findViewById(R.id.image);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    (new IntentSend(getApplicationContext(), DashBoradActivity.class)).toSendAcivity();
                    finish();
                }
            });
            TextView tv = (TextView) findViewById(R.id.text);
            tv.setText("Stream");
            arrayListData.clear();
            ownerArrayList.clear();
            ownerIDArrayList.clear();
            taskstatusArrayList.clear();
            listview = (ListView) findViewById(R.id.acslv);
            /*Button loadMoreButton=new Button(this);
            loadMoreButton.setText("Load More");
            loadMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    zvzvxzcv;
                }
            });
            listview.addFooterView(loadMoreButton);*/
            // editTextSearch=(EditText)findViewById(R.id.acsSearch);
            adaptor = new CustomFindContactAdapter(CRMstream.this, arrayListData, R.layout.listview_crmstream);
            listview.setAdapter(adaptor);
            preferences2 = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            SMID = preferences2.getString("CONPERID_SESSION", null);
            appDataController1 = new AppDataController(CRMstream.this);
            AppData appData;
            appDataController1.open();
            appDataArray = appDataController1.getAppTypeFromDb();
            appDataController1.close();
            appData = appDataArray.get(0);
            server = appData.getCompanyUrl();
            editTextDate = (EditText) findViewById(R.id.acrmstreamDate);
            editTextDate.setInputType(InputType.TYPE_NULL);
            editTextDate.setFocusable(false);
            SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
            Calendar cal = Calendar.getInstance();
            currentDate = df.format(cal.getTime());
            editTextDate.setText(currentDate);
            spinner = (Spinner) findViewById(R.id.acsspineerMain);
            fab = (FloatingActionButton) findViewById(R.id.fab);
            progressBarLayout = (RelativeLayout) findViewById(R.id.progressbarrelativeLayout);
            connectionDetector = new ConnectionDetector(this);
            editTextDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showDatePicker(view.getId());
                }
            });
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //Toast.makeText(CRMstream.this,"Click:"+arrayListData.get(i).getID()+"-"+arrayListData.get(i).getLead(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(CRMstream.this, CRMStreamInfo.class);
                    intent.putExtra("Contact_id", arrayListData.get(i).getID());
                    intent.putExtra("Lead", arrayListData.get(i).getLead());
                    intent.putExtra("Flag", arrayListData.get(i).getFlag());
                    Flag = arrayListData.get(i).getFlag();
                    startActivity(intent);
                    //finish();
                    /*if(arrayListData.get(i).getFlag().equalsIgnoreCase("L"))
                    {
                        Intent intent=new Intent(CRMstream.this,CRMStreamInfo.class);
                        intent.putExtra("Contact_id",arrayListData.get(i).getID());
                        intent.putExtra("Lead",arrayListData.get(i).getLead());
                        intent.putExtra("Flag",arrayListData.get(i).getFlag());
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent=new Intent(CRMstream.this,CRMTaskInfo.class);
                        intent.putExtra("Contact_id",arrayListData.get(i).getID());
                        intent.putExtra("Lead",arrayListData.get(i).getLead());
                        intent.putExtra("Flag",arrayListData.get(i).getFlag());
                        startActivity(intent);
                        finish();
                    }*/
                    System.out.println("Data Is:" + arrayListData.get(i).getLead() + "-" + arrayListData.get(i).getStatus());
                }
            });
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                    if(isSelected){
                    isSelected = true;
                    spinner_selected_index = position;
                    SMID = ownerIDArrayList.get(position);
                    UserName = ownerArrayList.get(position);
                    UserName = UserName.replaceAll(" ", "");
                    arrayListData.clear();
                    progressDialog = ProgressDialog.show(CRMstream.this, "Loading Data", "Loading...", true);
                    lodingProgressImage = true;
                    rowId = "0";
                    listview.setAdapter(null);
                    adaptor = new CustomFindContactAdapter(CRMstream.this, arrayListData, R.layout.listview_crmstream);
                    listview.setAdapter(adaptor);
                    new GetListAllData().execute();
                    //   }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });
            listview.setOnScrollListener(new AbsListView.OnScrollListener() {

                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    if (isMoreData) {
                        if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                            //  getNotificationData(SMID, notificationController.getTimeStamp());
                            if (connectionDetector.isConnectingToInternet()) {
                                lodingProgressImage = true;
                                isMoreData = false;
                                new GetListAllData().execute();
                            } else {
                                dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                                dialogWithOutView.show(getFragmentManager(), "Info");
                            }
                        }
                    }
                }
            });


            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fab.setVisibility(View.GONE);
                    // creating custom Floating Action button
                    CustomDialog();
                }
            });

        } else {
            new Custom_Toast(getApplicationContext(), "No Internet Connection! Try Again.").showCustomAlert();
        }

        if (connectionDetector.isConnectingToInternet()) {
            fab.setVisibility(View.VISIBLE);
            //new GetListAllContact().execute("ALL");
            new getOwner().execute();
        } else {

            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

    }


    private int getIndexByProperty(String yourString) {
        for (int i = 0; i < DashBoradActivity.crmPermissionslist.size(); i++) {
            if (DashBoradActivity.crmPermissionslist != null && DashBoradActivity.crmPermissionslist.get(i).getActivityName().equals(yourString)) {
                return i;
            }
        }
        return -1;// not there is list
    }

    private void showDatePicker(int id) {
        //DateAndTimePicker date = new DateAndTimePicker();
        DatePickerFragmentRange date = new DatePickerFragmentRange();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        args.putInt("grace", 0);
        //args.putString("Type", "Days");
        args.putString("Type", "tour");
        date.setArguments(args);
        if (id == R.id.acrmstreamDate) {
            date.setCallBack(ondate1);
            date.show(getSupportFragmentManager(), "Date Picker");
        }
    }

    DatePickerDialog.OnDateSetListener ondate1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String strDate = (dayOfMonth < 10 ? ("0" + dayOfMonth) : (dayOfMonth)) + "/" + (((monthOfYear + 1) < 10 ? ("0" + (monthOfYear + 1)) : (monthOfYear + 1))) + "/" + year;

            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MMM/yyyy");
//			 SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = format1.parse(strDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            currentDate = format2.format(date);
            editTextDate.setText(format2.format(date));
            if (spinner.getSelectedItemPosition() >= 0) {
                SMID = ownerIDArrayList.get(spinner.getSelectedItemPosition());
                UserName = ownerArrayList.get(spinner.getSelectedItemPosition());
                UserName = UserName.replaceAll(" ", "");
                arrayListData.clear();
                rowId = "0";
                listview.setAdapter(null);
                adaptor = new CustomFindContactAdapter(CRMstream.this, arrayListData, R.layout.listview_crmstream);
                listview.setAdapter(adaptor);
                progressDialog = ProgressDialog.show(CRMstream.this, "Loading Data", "Loading...", true);
                new GetListAllData().execute();
            } else {
                new Custom_Toast(CRMstream.this, "Selct Owner First").showCustomAlert();
            }

        }
    };

    @Override
    public void onResume() {
        super.onResume();
        rowId = "0";
//     isSelected=false;
        arrayListData.clear();
        listview.setAdapter(null);
        adaptor = new CustomFindContactAdapter(CRMstream.this, arrayListData, R.layout.listview_crmstream);
        listview.setAdapter(adaptor);
        lodingProgressImage = true;
        try {
            if (isSelected) {
                new GetListAllData().execute();
            }
        } catch (Exception e) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            e.printStackTrace();
        }

    }

    protected class getOwner extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(CRMstream.this, "Loading Data", "Loading...", true);
            progressDialog.setCancelable(true);


        }

        @Override
        protected String doInBackground(String... arg0) {
            String response = null;
            // TODO Auto-generated method stub
            try {
                // String url = "http://sfmsprim.dataman.in/And_Sync.asmx/xjsphoneEmailUrlType_CRM?key=";
                String url = "http://" + server + "/And_Sync.asmx/xjsGetAllOwnerForFilter_CRM?smid=" + SMID;
                JSONParser jParser = new JSONParser();
                response = jParser.getJSONArray(url);
                url = null;
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            JSONArray jsonArray = null;
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            int defaulSpinnerSelection = 0;
            if (!result.isEmpty() && !(result == null)) {
                try {
                    jsonArray = new JSONArray(result);
                    ownerArrayList.clear();
                    ownerIDArrayList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objs = null;
                        try {
                            objs = jsonArray.getJSONObject(i);
                            ownerArrayList.add(objs.getString("Name"));
                            ownerIDArrayList.add(objs.getString("id"));
                            if (objs.getString("id").equalsIgnoreCase(SMID) && objs.getString("Name").trim().equalsIgnoreCase("Me")) {
                                UserName = objs.getString("Name");
                                UserName = UserName.replaceAll(" ", "");
                                defaulSpinnerSelection = i;
                            }
                        } catch (JSONException e) {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    e.printStackTrace();
                }
                setSpinner(spinner, ownerArrayList);
                try {
                    spinner.setSelection(defaulSpinnerSelection);
                } catch (Exception e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    e.printStackTrace();
                }
                arrayListData.clear();
                new GetListAllData().execute();


            } else {
                new Custom_Toast(CRMstream.this, "No Data Found").showCustomAlert();
            }

        }
    }

    public void CustomDialog() {

        dialog = new Dialog(CRMstream.this);
        // it remove the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set the laytout in the dialog
        dialog.setContentView(R.layout.floating_dialog);
        // set the background partial transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        // set the layout at right bottom
        param.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        // it dismiss the dialog when click outside the dialog frame
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCancelable(false);
        // initialize the item of the dialog box, whose id is demo1
        View cross = (View) dialog.findViewById(R.id.cross);
        View addTask = (View) dialog.findViewById(R.id.floatingAddTask);
        View addLead = (View) dialog.findViewById(R.id.floatingAddLead);

        int i = getIndexByProperty("Add Task");
        int k = getIndexByProperty("Add Lead");
        try {
            if (DashBoradActivity.crmPermissionslist.get(i).getPermission().equalsIgnoreCase("false")) {
                addTask.setVisibility(View.INVISIBLE);
            }
            if (DashBoradActivity.crmPermissionslist.get(k).getPermission().equalsIgnoreCase("false")) {
                addLead.setVisibility(View.INVISIBLE);
            }
            if (!addLead.isShown() && !addTask.isShown()) {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // it call when click on the item whose id is demo1.
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // diss miss the dialog
                fab.setVisibility(View.VISIBLE);
                dialog.dismiss();
            }
        });
        // it call when click on the item whose id is demo1.
        addLead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // diss miss the dialog
                dialog.dismiss();
                Intent intent = new Intent(CRMstream.this, AddContact.class);
                startActivity(intent);
                finish();
            }
        });

        // it call when click on the item whose id is demo1.
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(CRMstream.this, CrmTask.class);
                intent.putExtra("FromWhere", "MainScreen");
                startActivity(intent);

            }
        });
        dialog.show();
    }

    class GetListAllData extends AsyncTask<String, String, String> {
        String type = null;
        String result = null;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if (lodingProgressImage) {
                lodingProgressImage = false;
            } else {
                progressBarLayout.setVisibility(View.VISIBLE);
            }
            //arrayListData.clear();
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                result = null;
                result = getContactData();
            } catch (Exception e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            isSelected = true;
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            progressBarLayout.setVisibility(View.GONE);
            try {
                JSONArray jsonarray = new JSONArray(result);
                if (jsonarray.length() > 0) {
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = jsonarray.getJSONObject(i);
                        AddContactModel cdata = new AddContactModel();
                        rowId = obj.getString("rowId");
                        cdata.setID(obj.getString("Id"));
                        cdata.setLead(obj.getString("Lead"));
                        cdata.setComapy(obj.getString("compname"));
                        cdata.setStatus(obj.getString("TaskStatus"));
                        cdata.setLeadStatus(obj.getString("LeadStatus"));
                        cdata.setTag(obj.getString("Tag"));
                        cdata.setTaskDate(obj.getString("AssignDate") + " " + obj.getString("time"));
                        cdata.setTaskDiscription(obj.getString("Taskdesc"));
                        cdata.setOwner(obj.getString("Owner"));
                        cdata.setTime(obj.getString("time"));
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MMM/yyyy");
                        String date = sdf.format(new Date());
                        if (obj.has("AssignDate")) {
                            String assignDate = obj.getString("AssignDate");
                            String dt[] = assignDate.split(" ");
                            SimpleDateFormat f = new SimpleDateFormat("dd/MMM/yyyy");
                            try {
                                Date d = f.parse(assignDate);
                                long assignedmilliseconds = d.getTime();
                                Date cd = f.parse(date);
                                long clong = cd.getTime();
                                long diff = clong - assignedmilliseconds;
                                if (diff == 0) {
                                    cdata.setIstoday(1);
                                }
                                if (diff < 0) {
                                    cdata.setIstoday(2);
                                }
                                if (diff > 0) {
                                    cdata.setIstoday(3);
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        cdata.setFlag(obj.getString("Flag"));
                        arrayListData.add(cdata);
                        isMoreData = true;
                    }
                    adaptor.notifyDataSetChanged();

                } else {
                    isMoreData = false;
                }
            } catch (Exception e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                System.out.println(e);
            }
        }
    }

    public String getContactData() {
        String url = "http://" + server + "/And_Sync.asmx/XjsgetCRM_ActionStreamByRowId?Smid=" + ownerIDArrayList.get(spinner_selected_index) + "&username=" + UserName + "&Adate=" + currentDate + "&rowid=" + rowId + "";
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }

    public void setSpinner(Spinner spinner, ArrayList arrayList) {
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.adapterdropdown, arrayList);
        arrayAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
