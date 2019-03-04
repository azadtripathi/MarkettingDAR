package com.dm.crmdm_app;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import com.dm.controller.SmanController;
import com.dm.controller.UserDataController;
import com.dm.library.AlertMessage;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomArrayAdapterLeaveRequestList;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.library.DateFunction;
import com.dm.library.ExceptionData;
import com.dm.library.IntentSend;
import com.dm.model.AppData;
import com.dm.model.DashboardModel;
import com.dm.model.Sman;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class FindLeave extends AppCompatActivity implements
        AlertMessage.NoticeDialogListenerWithoutView,
        CustomArrayAdapterLeaveRequestList.FindLeaveRequestTransactionListener,
        CustomArrayAdapterLeaveRequestList.HolderListener{
    long f,t;
    Spinner salesManMaterial;
    ArrayAdapter smanAdapter;
    AlertOkDialog alertOkDialog;
    int ListCount;
    SharedPreferences preferences, preferences2;
    ArrayList<Sman> smanList;
    SmanController smanController;
    UserDataController userDataController;
    String currentDate,checkdate;
    String SmId,dropdownId,SolidArea,newSmid;
    String server;
    String FromDate;
    String ToDate;
    String MOb;
    String Level;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    long Rmns;
    TextView timespendtextview;
    LinearLayout traDSR,linear_Sales;
    Calendar cal;SimpleDateFormat df;
    ListView listView;
    DashboardModel model;
    ArrayList<DashboardModel> dashboardModels;
    CustomArrayAdapterLeaveRequestList customArrayAdapterLeaveRequestList;
    ArrayList<String> salesManNameList=new ArrayList<String>();
    ArrayList<String> salesManNameList2=new ArrayList<String>();
    ConnectionDetector connectionDetector;ExceptionData exceptionData;AlertOkDialog dialogWithOutView;
    Button btn_search;
    EditText fromDate,toDateEditL;
    ImageView filter;
    LinearLayout linearlayout;
    RelativeLayout footer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_leave);
        getSupportActionBar().setCustomView(R.layout.actionbar_custom_view_home);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        ImageView iv = (ImageView)findViewById(R.id.image);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new IntentSend(getApplicationContext(),DashBoradActivity.class)).toSendAcivity();
                finish();
            }
        });

        TextView tv = (TextView)findViewById(R.id.text);
        tv.setText("Leave List");
        /******************************* Write By Sandeep Singh 02-04-2017  ******************************/
        /********************	START	***********************/
        footer = (RelativeLayout)findViewById(R.id.footer);
        linearlayout = (LinearLayout)findViewById(R.id.linearlayout);
        filter = (ImageView)findViewById(R.id.filter);
        footer.setVisibility(View.GONE);
        filter.setVisibility(View.VISIBLE);
        /********************	END	***********************/
        connectionDetector=new ConnectionDetector(getApplicationContext());
        timespendtextview = (TextView)findViewById(R.id.timespendtextview);
        timespendtextview.setVisibility(View.VISIBLE);
        traDSR = (LinearLayout)findViewById(R.id.traDSR);
        salesManMaterial = (Spinner)findViewById(R.id.MaterialBetterSpinner);
        preferences2=this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        listView = (ListView) findViewById(R.id.findLeaveRequestlist);
        fromDate=(EditText) findViewById(R.id.fromDate);
        linear_Sales=(LinearLayout) findViewById(R.id.linear_Sales);
        fromDate.setInputType(InputType.TYPE_NULL);
        fromDate.setFocusable(false);
        smanController=new SmanController(this);
        smanController.open();
        smanList=smanController.getName(preferences2.getString("CONPERID_SESSION", null));
       // server= Constant.SERVER_WEBSERVICE_URL;
        SmId=preferences2.getString("CONPERID_SESSION", null);


        /*userDataController = new UserDataController(this);
        userDataController.open();
        SmId = userDataController.getSmanId();
        userDataController.close();*/

        appDataController1=new AppDataController(FindLeave.this);
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();

        toDateEditL=(EditText) findViewById(R.id.toDateEditL);
        toDateEditL.setInputType(InputType.TYPE_NULL);
        toDateEditL.setFocusable(false);
        btn_search= (Button)findViewById(R.id.btn_search);

        salesManNameList.add("--Select--");
        salesManNameList2.add("--Select--");
        df = new SimpleDateFormat("dd/MMM/yyyy");
        Calendar cal=Calendar.getInstance();
        cal.add(Calendar.MONTH, -1);
        fromDate.setText(df.format(cal.getTime()));
        Calendar cal1 = Calendar.getInstance();
        toDateEditL.setText(df.format(cal1.getTime()));
        currentDate=df.format(cal.getTime());

        MOb = preferences2.getString("PDAID_SESSION", "");

       /* LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_leave_list, listView, false);
        listView.addHeaderView(header, null, false);*/
        ArrayList<String> salesManNameList=new ArrayList<String>();
        /*try{
            for(int i=0;i<smanList.size();i++)
            {
                salesManNameList.add(smanList.get(i).getUser_Name());
            }
            smanAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,salesManNameList);
            //salesManSpinner.setAdapter(smanAdapter);
            salesManMaterial.setAdapter(smanAdapter);
            //salesManMaterial.setSelection(0);
        }catch(Exception e)
        {
            System.out.println(e);
        }*/
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

        salesManMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    String jk = timespendtextview.getText().toString();
                    SolidArea = salesManNameList2.get((int) salesManMaterial.getSelectedItemId());
                    if (!jk.equalsIgnoreCase("")) {
                        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                        timespendtextview.setVisibility(View.VISIBLE);
                        timespendtextview.startAnimation(slide_up);
                        timespendtextview.setTextColor(Color.parseColor("#FF4081"));
                        traDSR.setBackgroundColor(Color.parseColor("#FF4081"));
                    }
                    timespendtextview.setText("Sales Person:");
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                timespendtextview.setVisibility(View.GONE);
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
        btn_search.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FromDate=fromDate.getText().toString();
                ToDate= toDateEditL.getText().toString();
                /******************************************** Ashutosh Start ********************************/
                long b = DateFunction.ConvertDateToTimestamp(fromDate.getText().toString());
                long t = DateFunction.ConvertDateToTimestamp(toDateEditL.getText().toString());

                if (b > t) {
                    new Custom_Toast(getApplicationContext(), "To Date Cannot be less than from date.").showCustomAlert();

                }
                /******************************************** Ashutosh End ********************************/
                else {
                    if (!FromDate.equalsIgnoreCase("")) {
                        if (!ToDate.toString().equalsIgnoreCase("")) {
                        /*if(salesManMaterial.getSelectedItem()==null) {
                            new Custom_Toast(getApplicationContext(), "Please Select Person Name").showCustomAlert();
                        }
                        else {*/
                            if (connectionDetector.isConnectingToInternet()) {
                                new findLeaveRequest().execute();
                            } else {
                                /******************************* Write By Sandeep Singh 02-04-2017  ******************************/
                                /********************	START	***********************/
                                linearlayout.setVisibility(View.VISIBLE);
                                footer.setVisibility(View.GONE);
                                /********************	END	***********************/
                                dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                                dialogWithOutView.show(getFragmentManager(), "Info");
                            }
                            //}
                        } else {
                            /******************************* Write By Sandeep Singh 02-04-2017  ******************************/
                            /********************	START	***********************/
                            linearlayout.setVisibility(View.VISIBLE);
                            footer.setVisibility(View.GONE);
                            /********************	END	***********************/
                            new Custom_Toast(getApplicationContext(), "Please Select Your To Date").showCustomAlert();
                        }
                    } else {
                        /******************************* Write By Sandeep Singh 02-04-2017  ******************************/
                        /********************	START	***********************/
                        linearlayout.setVisibility(View.VISIBLE);
                        footer.setVisibility(View.GONE);
                        /********************	END	***********************/
                        new Custom_Toast(getApplicationContext(), "Please Select Your From Date").showCustomAlert();
                    }
                }
            }
        });

        fromDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                //new Custom_Toast(getApplicationContext(), "onTextChanged").showCustomAlert();
                //  onDateDisplay(fromDate.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                //new Custom_Toast(getApplicationContext(), "beforeTextChanged").showCustomAlert();

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                //new Custom_Toast(getApplicationContext(), "afterTextChanged").showCustomAlert();
            }
        });
        toDateEditL.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                //new Custom_Toast(getApplicationContext(), "onTextChanged").showCustomAlert();
                // onDateDisplay(toDateEditL.getText().toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
                //new Custom_Toast(getApplicationContext(), "beforeTextChanged").showCustomAlert();

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                //new Custom_Toast(getApplicationContext(), "afterTextChanged").showCustomAlert();
            }
        });
        SmId=preferences2.getString("CONPERID_SESSION", null);
        fromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePicker(v.getId());
            }
        });

        toDateEditL.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePicker1(v.getId());
            }
        });
        if (connectionDetector.isConnectingToInternet())
        {
            new getLevel().execute();
        }
        else{
            dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }
    }


    @Override
    public void holderListener(ArrayList<LeaveRequest> leaveRequestList) {

    }

    @Override
    public void holderListener(CustomArrayAdapterLeaveRequestList.MyHolder myHolders) {

    }

    @Override
    public void onDialogPositiveWithoutViewClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeWithoutViewClick(DialogFragment dialog) {

    }

    protected class findLeaveRequest extends AsyncTask<Void, Void, ArrayList<DashboardModel>> {
        ProgressDialog pdLoading = new ProgressDialog(FindLeave.this);
        String select="Single";
        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPreExecute() {
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
            try {
                if(SolidArea.equals("--Select--")){
                    SmId=preferences2.getString("CONPERID_SESSION", null);
                    select = "select";
                }
                else {
                    SmId = SolidArea;
                /*String jjk = salesManNameList2.get((int) salesManMaterial.getSelectedItemId());
                Rmns = salesManMaterial.getSelectedItemId();
                //new Custom_Toast(getApplicationContext(), jjk).showCustomAlert();
                SmId = jjk;
                dropdownId = String.valueOf(salesManMaterial.getSelectedItemId());*/
                }
            }
            catch (Exception e){}
        }
        @Override
        protected ArrayList<DashboardModel> doInBackground(Void... params) {
            //server= Constant.SERVER_WEBSERVICE_URL;
            model = new DashboardModel();
            //SmId = preferences2.getString("CONPERID_SESSION", null);
            /*runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    FromDate=fromDate.getText().toString();
                    ToDate= toDateEditL.getText().toString();
                }
            });*/

            String str = "http://" + server + "/And_Sync.asmx/JSFindLeaveDetails?SmId=" + SmId + "&selectiontype="+select+"&FromDate="+FromDate+"&ToDate="+ToDate;
            Log.e("findLeaveRequest", str);
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try {
                dashboardModels = new ArrayList<DashboardModel>();
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                JSONArray response = new JSONArray(stringBuffer.toString());

                for (int i = 0; i < response.length(); i++) {
                    JSONObject objs = null;
                    try {
                        objs = response.getJSONObject(i);
                        String dfg=(objs.getString("FromDate").toString());
                        String tfg=(objs.getString("ToDate").toString());
                        String tfh=(objs.getString("VDate").toString());
                        dashboardModels.add(model.findLeaveRequestListModel(
                                objs.getString("LeaveFlag").toString(),
                                MOb,
                                SmId,
                                tfh,
                                objs.getString("NoOfDays").toString(),
                                dfg,
                                tfg,
                                objs.getString("SMName").toString(),
                                objs.getString("Reason").toString(),
                                objs.getString("AppRemark").toString(),
                                objs.getString("AppStatus").toString(),
                                objs.getString("LVRQId").toString(),
                                objs.getString("LVRDocId").toString(),
                                objs.getString("SMId").toString(),
                                dropdownId));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                return dashboardModels;
            } catch (Exception ex) {
                Log.e("App", "yourDataTask", ex);
                return null;
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(ArrayList<DashboardModel> response) {
            if (response != null) {
                if(response.size()>0) {
                    /******************************* Write By Sandeep Singh 02-04-2017  ******************************/
                    /********************	START	***********************/
                    linearlayout.setVisibility(View.GONE);
                    footer.setVisibility(View.VISIBLE);
                    /********************	END	***********************/
                    listView.setVisibility(View.GONE);
                    customArrayAdapterLeaveRequestList = new CustomArrayAdapterLeaveRequestList(
                            getApplicationContext(), response, R.layout.find_leave_request_list_row,
                            R.id.apptextdate, FindLeave.this, FindLeave.this);
                    listView.setAdapter(customArrayAdapterLeaveRequestList);
                    listView.setClickable(true);
                    listView.setVisibility(View.VISIBLE);
                    Log.e("App", response.toString());
                }else{
                    /******************************* Write By Sandeep Singh 02-04-2017  ******************************/
                    /********************	START	***********************/
                    linearlayout.setVisibility(View.VISIBLE);
                    footer.setVisibility(View.GONE);
                    /********************	END	***********************/
                    listView.setVisibility(View.GONE);
                    new Custom_Toast(getApplicationContext(), "No Leave Available.").showCustomAlert();
                }
            }else{
                /******************************* Write By Sandeep Singh 02-04-2017  ******************************/
                /********************	START	***********************/
                linearlayout.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
                /********************	END	***********************/
                listView.setVisibility(View.GONE);
                new Custom_Toast(getApplicationContext(), "No Leave Available.").showCustomAlert();
            }
            super.onPostExecute(response);
            pdLoading.dismiss();
        }
    }

    private String convertDate(String Date)
    {
        String[] parts = Date.split(" ")[0].split("/");
        int year = Integer.parseInt(parts[2]);
        String mon="Jan";
        if(parts[1].equalsIgnoreCase("1")){
            mon="Jan";
        }else if(parts[1].equalsIgnoreCase("2")){
            mon="Fab";
        }else if(parts[1].equalsIgnoreCase("3")){
            mon="Mar";
        }else if(parts[1].equalsIgnoreCase("4")){
            mon="Apr";
        }else if(parts[1].equalsIgnoreCase("5")){
            mon="May";
        }else if(parts[1].equalsIgnoreCase("6")){
            mon="Jun";
        }else if(parts[1].equalsIgnoreCase("7")){
            mon="Jul";
        }else if(parts[1].equalsIgnoreCase("8")){
            mon="Aug";
        }else if(parts[1].equalsIgnoreCase("9")){
            mon="Sep";
        }else if(parts[1].equalsIgnoreCase("10")){
            mon="Oct";
        }else if(parts[1].equalsIgnoreCase("11")){
            mon="Nov";
        }else{
            mon="Dec";
        }
        String date=parts[0]+"/"+mon+"/"+year;
        return date;
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
        if(id== R.id.fromDate)
        {
            date.setCallBack(ondate);
            date.show(getSupportFragmentManager(), "Date Picker");
        }
    }
    private void showDatePicker1(int id) {
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
        if(id== R.id.toDateEditL)
        {
            date.setCallBack(ondate1);
            date.show(getSupportFragmentManager(), "Date Picker");
        }
    }
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
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
            System.out.println(format2.format(date));
            Date filledDate = null,currenttime = null;
            try {
                filledDate = format2.parse(format2.format(date));
                currenttime=format2.parse(currentDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            System.out.println(c.getTime()); // Tue Jun 18 17:07:45 IST 2013
            c.set(Calendar.DATE, c.get(Calendar.DATE)-30);
            System.out.println("time before="+c.getTime()+" "+c.getTimeInMillis());
            Date dateBefore = new Date(currenttime.getTime() - (30 * 24 * 3600 * 1000) );
//			 long i=filledDate.getTime()-dateBefore.getTime();
            long i=filledDate.getTime()-c.getTimeInMillis();
            System.out.println("30 days="+(30 * 24 * 3600 * 1000) );
            System.out.println("curent="+currenttime.getTime());
            System.out.println("before="+c.getTimeInMillis());
            System.out.println("i="+i);

            fromDate.setText(format2.format(date));
            // toDateEditL.setText(format2.format(date));
        }
    };
    DatePickerDialog.OnDateSetListener ondate1 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
           /* String strDate=(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth))+"/"+(((monthOfYear+1)<10?("0"+(monthOfYear+1)):(monthOfYear+1)))+"/"+year;
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
            System.out.println(format2.format(date));
            Date filledDate = null,currenttime = null;
            try {
                filledDate = format2.parse(format2.format(date));
                currenttime = format2.parse(currentDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            System.out.println(c.getTime()); // Tue Jun 18 17:07:45 IST 2013
            c.set(Calendar.DATE, c.get(Calendar.DATE)-30);
            System.out.println("time before="+c.getTime()+" "+c.getTimeInMillis());
            Date dateBefore = new Date(currenttime.getTime() - (30 * 24 * 3600 * 1000) );
//			 long i=filledDate.getTime()-dateBefore.getTime();
            long i=filledDate.getTime()-c.getTimeInMillis();
            System.out.println("30 days="+(30 * 24 * 3600 * 1000) );
            System.out.println("curent="+currenttime.getTime());
            System.out.println("before="+c.getTimeInMillis());
            System.out.println("i="+i);

           // Date filldate = (Date) fromDate.getText();
            i =(int)((filledDate.getTime()-currenttime.getTime())/(1000 * 60 * 60 * 24));
            if(i==0||i>0)
            {
                alertOkDialog=AlertOkDialog.newInstance("To date should be greater than From date");
                alertOkDialog.show(getFragmentManager(), "my alert");
            }
            else {

                toDateEditL.setText(format2.format(date));
            }*/

            /*f = DateFunction.ConvertDateToTimestamp(fromDate.getText().toString());
            t = DateFunction.ConvertDateToTimestamp(toDateEditL.getText().toString());
            if (f > t) {
                new Custom_Toast(getApplicationContext(), "From Date should be less than To Date").showCustomAlert();

            } else {
                toDateEditL.setText(format2.format(date));
            }*/

            String strDate=(dayOfMonth<10?("0"+dayOfMonth):(dayOfMonth))+"/"+(((monthOfYear+1)<10?("0"+(monthOfYear+1)):(monthOfYear+1)))+"/"+year;
            SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
            SimpleDateFormat format2 = new SimpleDateFormat("dd/MMM/yyyy");
//				 SimpleDateFormat format2 = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            Date date1 = null;
            String vdate=fromDate.getText().toString();
            System.out.println(vdate);
            try {
                date1 = format1.parse(DateFunction.ToConvertDateFormat(vdate,"dd/MMM/yyyy","dd/MM/yyyy"));
                System.out.println(date1);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                date = format1.parse(strDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            Date filleddate=null,todaysDate=null;
            try {


                filleddate=df.parse(format2.format(date));
//				todaysDate=df.parse(format2.format(c.getTime()));
                todaysDate=df.parse(format2.format(date1));
                //System.out.println(todaysDate);

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


            int i=(int)((filleddate.getTime()-todaysDate.getTime())/(1000 * 60 * 60 * 24));
            if(i<0)
            {
                alertOkDialog= AlertOkDialog.newInstance("To date cannot be less than from date");
                alertOkDialog.show(getFragmentManager(), "my alert");

            }
            else {

                toDateEditL.setText(format2.format(date));
            }

        }
    };

    protected class getLevel extends AsyncTask<Void, Void, JSONArray>
    {
        ProgressDialog pdLoading = new ProgressDialog(FindLeave.this);
        @Override
        protected void onPreExecute() {
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }
        @Override
        protected JSONArray doInBackground(Void... params)
        {
           // server= Constant.SERVER_WEBSERVICE_URL;
            String MOb = preferences2.getString("PDAID_SESSION", "");
            //String str="http://" + server + "/And_Sync.asmx/JSGetUserDetailByPDAId?PDA_Id=" + MOb + "&minDate=0";
            String str="http://" + server + "/And_Sync.asmx/JSGetUnderUser?PDA_Id=" + MOb + "&minDate=0&Smid="+SmId;
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try
            {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null)
                {
                    stringBuffer.append(line);
                }
                //JSONObject response = new JSONObject(stringBuffer.toString());
                JSONArray response = new JSONArray(stringBuffer.toString());
                //JSONObject obj = response.getJSONObject(0);
                return new JSONArray(response.toString());
            }
            catch(Exception ex)
            {
                Log.e("App", "yourDataTask", ex);
                return null;
            }
            finally
            {
                if(bufferedReader != null)
                {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(JSONArray response)
        {
            if(response != null)
            {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject objs = null;
                        try {
                            objs = response.getJSONObject(i);
                            salesManNameList.add(objs.getString("Name").toString());
                            salesManNameList2.add(objs.getString("Id").toString());

                            //spinnerMap.put(objs.getString("Name"),objs.getString("Id").toString());
                            //spinnerArray[i] = objs.getString("Name");

                            //String id = objs.getString("Id").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    Level = response.getJSONObject(response.length()-1).getString("RoleName").toString();
                    smanAdapter=new ArrayAdapter<String>(FindLeave.this, R.layout.adapterdropdown,salesManNameList);
                    smanAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    //salesManSpinner.setAdapter(smanAdapter);
                    salesManMaterial.setAdapter(smanAdapter);
                    salesManMaterial.setSelection(0);

                    /*salesManMaterial.setSelection(salesManNameList2.indexOf(SmId));
                    if(response.length()==1){
                        salesManMaterial.setClickable(false);
                        salesManMaterial.setEnabled(false);
                        //linear_Sales.setVisibility(View.GONE);
                    }
                    else {
                        salesManMaterial.setClickable(true);
                        salesManMaterial.setEnabled(true);
                    }*/
                    //Log.e("App", "Success: " + response.getString("yourJsonElement") );
                } catch (JSONException ex) {
                    Log.e("App", "Failure", ex);
                }
            }
            else {
                new Custom_Toast(getApplicationContext(), "Please Select Sales Person").showCustomAlert();
            }
            super.onPostExecute(response);
            pdLoading.dismiss();
        }
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
//        super.onBackPressed();
//        finish();
        Intent i = new Intent(FindLeave.this, LeaveRequest.class);
        startActivity(i);
        super.onBackPressed();
        finish();
    }


}
