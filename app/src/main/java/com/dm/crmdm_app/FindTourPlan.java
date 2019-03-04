package com.dm.crmdm_app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomTourPlanArrayAdapter;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.library.DateFunction;
import com.dm.library.IntentSend;
import com.dm.model.AppData;
import com.dm.model.DashboardModel;
import com.dm.model.Sman;
import com.dm.model.TourPlan;
import com.dm.parser.JSONParser;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FindTourPlan extends AppCompatActivity implements CustomTourPlanArrayAdapter.HolderListener {
    SharedPreferences preferences;
    String conper, userId;ArrayList<String> smanArrayList;ArrayList<Sman> smanList;ArrayAdapter<String> smanAdapter;
    String server;ProgressDialog progressDialog;ArrayList<TourPlan> tourList;
    EditText fdate, tdate;Calendar c,c1;
    Button go;CustomTourPlanArrayAdapter customTourPlanArrayAdapter;
    ConnectionDetector connectionDetector;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    Spinner smanSpinner;
    SimpleDateFormat df;
    ListView listView;
    AlertOkDialog dialogWithOutView;
    ImageView filter;
    LinearLayout linearlayout;
    RelativeLayout footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_tour_plan);
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
        tv.setText("Tour Plan List");
        preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        conper = preferences.getString("CONPERID_SESSION", "0");
        userId = preferences.getString("USER_ID", "0");
        connectionDetector = new ConnectionDetector(getApplicationContext());
        appDataController1 = new AppDataController(FindTourPlan.this);
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server = appData.getCompanyUrl();
        smanSpinner = (Spinner) findViewById(R.id.smanSpinner);
        fdate = (EditText) findViewById(R.id.adfromDate);
        fdate.setInputType(InputType.TYPE_NULL);
        Calendar cal = Calendar.getInstance();
        df = new SimpleDateFormat("dd/MMM/yyyy");
        cal.add(Calendar.MONTH, -1);
        fdate.setText(df.format(cal.getTime()));
        tdate = (EditText) findViewById(R.id.adtoDateEditL);
        Calendar cal1 = Calendar.getInstance();
        tdate.setText(df.format(cal1.getTime()));
        tdate.setInputType(InputType.TYPE_NULL);
        fdate.setInputType(InputType.TYPE_NULL);
        fdate.setFocusable(false);
        tdate.setInputType(InputType.TYPE_NULL);
        tdate.setFocusable(false);
        go = (Button) findViewById(R.id.btn_search);
        /******************************* Write By Sandeep Singh 02-04-2017  ******************************/
        /********************	START	***********************/
        footer = (RelativeLayout)findViewById(R.id.footer);
        linearlayout = (LinearLayout)findViewById(R.id.linearlayout);
        filter = (ImageView)findViewById(R.id.filter);
        footer.setVisibility(View.GONE);
        filter.setVisibility(View.VISIBLE);
        /********************	END	***********************/
//        go.setClickable(false);
//        cancel.setClickable(false);
        setDateText();
        listView = (ListView) findViewById(R.id.findLeaveRequestlist);
       /* LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.tour_plan_header_row, listView, false);
        listView.addHeaderView(header, null, false);*/
        if (connectionDetector.isConnectingToInternet())

        {
            new GetSmanList().execute();

        } else {

            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

        fdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = FindTourPlan.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker(view.getId());
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub

                parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                return false;
            }
        });

        tdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View view1 = FindTourPlan.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker(view.getId());
            }
        });
        /******************************* Write By Sandeep Singh 02-04-2017  ******************************/
        /********************	START	***********************/
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        });
        /********************	END	***********************/
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionDetector.isConnectingToInternet())
                {
                    new GetTourList().execute();
                }
                else{
                    linearlayout.setVisibility(View.VISIBLE);
                    footer.setVisibility(View.GONE);
                    dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }
            }
        });

    }

    @Override
    public void holderListener(CustomTourPlanArrayAdapter.MyHolder myHolders) {

    }
void setDateText()
{
    c = Calendar.getInstance();
    c1 = Calendar.getInstance();
    tdate.setText(df.format(c.getTime()));
    c1.setTime(new Date()); // Now use today date.
    c1.add(Calendar.MONTH, -1);
    fdate.setText(df.format(c1.getTime()));
}

    class GetTourList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(FindTourPlan.this, "Loading Data", "Loading Please wait...", true);
        }
        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                tourList = getTourList();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (tourList != null) {
                customTourPlanArrayAdapter =new CustomTourPlanArrayAdapter(FindTourPlan.this,feedDahboardItemData(tourList), R.layout.find_tour_plan_list_row, R.id.tourdocid,FindTourPlan.this);
                listView.setAdapter(customTourPlanArrayAdapter);
                listView.setClickable(true);
                if(customTourPlanArrayAdapter.getCount()>0)
                {
                    linearlayout.setVisibility(View.GONE);
                    footer.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.VISIBLE);
                }
                else{
                    linearlayout.setVisibility(View.VISIBLE);
                    footer.setVisibility(View.GONE);
                    listView.setVisibility(View.GONE);
                    new Custom_Toast(getApplicationContext(), "No Record Found").showCustomAlert();
                }
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }
    private ArrayList<DashboardModel> feedDahboardItemData(ArrayList<TourPlan> tourList1)
    {
        ArrayList<DashboardModel> dashboardModels=new ArrayList<DashboardModel>();
        DashboardModel model=new DashboardModel();
        for (int i = 0; i < tourList1.size(); i++) {
            dashboardModels.add(model.FindTourPlanDateWiseModel(tourList1.get(i).getVDate(),tourList1.get(i).getDocId(),tourList1.get(i).getAppBySMId(),tourList1.get(i).getSMId(),tourList1.get(i).getAppStatus(),tourList1.get(i).getAppRemark()));
        }
        return dashboardModels;
    }
    class GetSmanList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(FindTourPlan.this, "Loading Data", "Loading Please wait...", true);
        }
        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                smanList = getSmanList();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (smanList != null) {
                smanArrayList = new ArrayList<String>();
                int select = 0;
                for (int i = 0; i < smanList.size(); i++) {
                    if (smanList.get(i).getConPerId().equals(conper)) {
                        select = i;
                    }
                    smanArrayList.add(smanList.get(i).getDisplayName());
                }

                smanAdapter = new ArrayAdapter<String>(FindTourPlan.this, R.layout.adapterdropdown, smanArrayList);
                smanAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                smanSpinner.setAdapter(smanAdapter);
                //smanSpinner.setSelection(select);
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

        }
    }
    private void showDatePicker(int id) {
        DateAndTimePicker date = new DateAndTimePicker();
        /**
         * Set Up Current Date Into dialog
         */

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        if(id== R.id.adfromDate)
        {
            date.setCallBack(ondate1);
            date.show(getSupportFragmentManager(), "Date Picker");
        }
        else if(id== R.id.adtoDateEditL)
        {
            date.setCallBack(ondate2);
            date.show(getSupportFragmentManager(), "Date Picker");
        }

    }
    DatePickerDialog.OnDateSetListener ondate1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String strDate=(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth))+"/"+(((monthOfYear+1)<10?("0"+(monthOfYear+1)):(monthOfYear+1)))+"/"+year;
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

            fdate.setText(format2.format(date));
        }
    };

    DatePickerDialog.OnDateSetListener ondate2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String strDate=(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth))+"/"+(((monthOfYear+1)<10?("0"+(monthOfYear+1)):(monthOfYear+1)))+"/"+year;
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MMM/yyyy");
//			 SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            long f;
            Date date = null;
            try {
                date = format1.parse(strDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Date filledDate = null;
            try {
                filledDate = format2.parse(format2.format(date));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            f = DateFunction.ConvertDateToTimestamp(fdate.getText().toString());
            if(filledDate.getTime()>=f)
            {
                tdate.setText(format2.format(date));
                go.setClickable(true);

            }

            else{
                dialogWithOutView= AlertOkDialog.newInstance("To Date and Time should be greater than From Date and Time");
                dialogWithOutView.show(getFragmentManager(), "Info");
                go.setClickable(false);
            }

        }
    };

    public ArrayList<TourPlan> getTourList()
    {
        String id;
        ArrayList<TourPlan> tourList=new ArrayList<TourPlan>();
        JSONParser jParser = new JSONParser();
        if(connectionDetector.isConnectingToInternet()){
            if(smanSpinner.getSelectedItem().toString().equals("--Select--")){
                id = conper;
            }
            else {
                id = smanList.get(smanSpinner.getSelectedItemPosition()).getConPerId();
            }
            try {
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/xJSFindTourPlan?smid="+id+"&fdt="+fdate.getText().toString()+"&tdt="+tdate.getText().toString()));

                for (int i = 0; i < jsonarray.length(); i++) {
                    TourPlan tourPlan=new TourPlan();
                    tourPlan.setVDate(jsonarray.getJSONObject(i).getString("dt"));
                    tourPlan.setDocId(jsonarray.getJSONObject(i).getString("dcid"));
                    tourPlan.setAppBySMId(jsonarray.getJSONObject(i).getString("slnm"));
                    tourPlan.setSMId(jsonarray.getJSONObject(i).getString("SMID"));
                    tourPlan.setAppStatus(jsonarray.getJSONObject(i).getString("st"));
                    tourPlan.setAppRemark(jsonarray.getJSONObject(i).getString("AppRemark"));
                    tourList.add(tourPlan);
                }
                jsonarray=null;

            }catch (Exception e)
            {

                System.out.println(e);

            }
        }

        return tourList;
    }

    public ArrayList<Sman> getSmanList()
    {
        ArrayList<Sman> smanList=new ArrayList<Sman>();
        String mResult = "";
        String mParent = "";
        JSONParser jParser = new JSONParser();
        if(connectionDetector.isConnectingToInternet()){
            try {
                JSONArray jsonarray = new JSONArray(jParser.getJSONArray("http://"+server+"/And_Sync.asmx/JSGetUnderUser?Smid="+conper));
                for (int i = 0; i < jsonarray.length(); i++) {
                   if(mParent.equalsIgnoreCase(mResult))
                   {
                       Sman sman=new Sman();
                       sman.setConPerId("0");
                       sman.setDisplayName("--Select--");
                       sman.setRoleId("0");
                       sman.setUnderId("0");
                       smanList.add(sman);
                       mParent = "Ashu";
                   }
                    Sman sman=new Sman();
                    sman.setConPerId(jsonarray.getJSONObject(i).getString("Id"));
                    sman.setDisplayName(jsonarray.getJSONObject(i).getString("Name"));
                    sman.setRoleId(jsonarray.getJSONObject(i).getString("RoleName"));
                    sman.setUnderId(jsonarray.getJSONObject(i).getString("UserId"));
                    smanList.add(sman);
                }
                jsonarray=null;

            }catch (Exception e)
            {
                System.out.println(e);
            }
        }
        return smanList;
    }
    @Override
    public void onBackPressed() {
        (new IntentSend(getApplicationContext(), com.dm.crmdm_app.TourPlan.class)).toSendAcivity();
        finish();
    }
}