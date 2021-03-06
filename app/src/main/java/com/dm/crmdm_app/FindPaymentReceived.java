package com.dm.crmdm_app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.controller.AppDataController;
import com.dm.controller.FindPaymentController;
import com.dm.library.CustomFindPaymentReceivedAdapter;
import com.dm.library.Custom_Toast;
import com.dm.library.DatePickerFragmentRange;
import com.dm.library.IntentSend;
import com.dm.library.Validation;
import com.dm.model.AppData;
import com.dm.parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by dataman on 1/5/2018.
 */

public class FindPaymentReceived extends AppCompatActivity {
    CustomFindPaymentReceivedAdapter contactAdapter;
    private List<FindPaymentController> contacts;
    private Random random;
    RelativeLayout footer;
    ImageView filter;
    Button findButton;
    CardView mainSerchPannel;
    RecyclerView recyclerView;
    String currentDate;
    EditText editTextFromDate,editTextToDate;
    Date compareFromDate,compareToDate;
    SharedPreferences preferences2;
    String SMID;
    String server;
    String rowId="0";
    ProgressDialog progressDialog;
    boolean isLoadMoreData=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_payment_recived);
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
        tv.setText("Find Payment Received");
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        findButton=(Button)findViewById(R.id.asSubmit);
        //Filter
        contacts = new ArrayList<>();
        AppData appData;
        ArrayList<AppData> appDataArray = new ArrayList<>();
        AppDataController appDataController = new AppDataController(this);
        appDataController.open();
        appDataArray = appDataController.getAppTypeFromDb();
        appDataController.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();
        preferences2=this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SMID=preferences2.getString("CONPERID_SESSION", null);
        mainSerchPannel=(CardView)findViewById(R.id.mainSerchPannel);
        footer = (RelativeLayout)findViewById(R.id.footer);
        filter = (ImageView)findViewById(R.id.filter);
        footer.setVisibility(View.GONE);
        filter.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
        mainSerchPannel.setVisibility(View.VISIBLE);
        editTextFromDate=(EditText)findViewById(R.id.fromDate);
        editTextFromDate.setInputType(InputType.TYPE_NULL);
        editTextFromDate.setFocusable(false);
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        Calendar cal=Calendar.getInstance();
        compareFromDate=cal.getTime();
        compareToDate=cal.getTime();
        currentDate=df.format(cal.getTime());
        editTextFromDate.setText(currentDate);
        editTextFromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDatePickerForFromDate(view.getId());
            }
        });

        editTextToDate=(EditText)findViewById(R.id.toDate);
        editTextToDate.setInputType(InputType.TYPE_NULL);
        editTextToDate.setFocusable(false);
        editTextToDate.setText(currentDate);
        editTextToDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDatePickerForToDate(view.getId());
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new CustomFindPaymentReceivedAdapter(recyclerView, contacts, this);
        recyclerView.setAdapter(contactAdapter);
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoadMoreData=true;
                rowId="0";
                contacts.clear();
                recyclerView.setVisibility(View.GONE);
                mainSerchPannel.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
                contactAdapter.notifyDataSetChanged();
                contactAdapter.setLoaded();
            }
        });
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isLoadMoreData=true;
                contacts.clear();
                rowId="0";
                new GetListAllData().execute();
                recyclerView.setVisibility(View.VISIBLE);
                mainSerchPannel.setVisibility(View.GONE);
                footer.setVisibility(View.VISIBLE);
                filter.setVisibility(View.VISIBLE);
                contactAdapter.notifyDataSetChanged();
                contactAdapter.setLoaded();

            }
        });
        contactAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(isLoadMoreData)
                    new GetListAllData().execute();
            }
        });

    }
    private void showDatePickerForFromDate(int id) {
        //DateAndTimePicker date = new DateAndTimePicker();
        DatePickerFragmentRange date = new DatePickerFragmentRange();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        args.putInt("grace",0);
        //args.putString("Type", "Days");
        args.putString("Type", "tour");
        date.setArguments(args);
        if (id == R.id.fromDate) {
            date.setCallBack(fromDate);
            date.show(getSupportFragmentManager(), "Date Picker");
        }
    }
    DatePickerDialog.OnDateSetListener fromDate = new DatePickerDialog.OnDateSetListener() {
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
            currentDate=format2.format(date);
            if ( compareToDay( date,compareToDate ) <= 0 )
            {
                compareFromDate=date;
                editTextFromDate.setText(format2.format(date));
            }
            else
            {
                new Custom_Toast(FindPaymentReceived.this,"From date must be smaller or equal then from date").showCustomAlert();
            }


        }
    };

    private void showDatePickerForToDate(int id) {
        //DateAndTimePicker date = new DateAndTimePicker();
        DatePickerFragmentRange date = new DatePickerFragmentRange();
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        args.putInt("grace",0);
        //args.putString("Type", "Days");
        args.putString("Type", "tour");
        date.setArguments(args);
        if (id == R.id.toDate) {
            date.setCallBack(toDate);
            date.show(getSupportFragmentManager(), "Date Picker");
        }
    }
    DatePickerDialog.OnDateSetListener toDate = new DatePickerDialog.OnDateSetListener() {
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
            currentDate=format2.format(date);
            if ( compareToDay( compareFromDate, date ) <= 0 )
            {
                compareToDate=date;
                editTextToDate.setText(format2.format(date));
            }
            else
            {
                new Custom_Toast(FindPaymentReceived.this,"To date must be grater or equal then from date").showCustomAlert();
            }


        }
    };
    public static int compareToDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date1).compareTo(sdf.format(date2));
    }
    private String phoneNumberGenerating() {
        int low = 100000000;
        int high = 999999999;
        int randomNumber = random.nextInt(high - low) + low;

        return "0" + randomNumber;
    }

    class GetListAllData extends AsyncTask<String, String, String> {
        String type=null; String result=null;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //arrayListData.clear();
        }
        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                result=null;
                result=getContactData();
            } catch (Exception e)
            {
                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if(progressDialog!=null)
            {
                progressDialog.dismiss();
            }
            try {
                final JSONArray jsonarray = new JSONArray(result);
                if (jsonarray.length()>0) {
                    contacts.add(null);
                    contactAdapter.notifyItemInserted(contacts.size() - 1);
                  /*  new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {*/
                    contacts.remove(contacts.size() - 1);
                    contactAdapter.notifyItemRemoved(contacts.size());

                    //Generating more data
                    int index = contacts.size();
                    int end = index + 10;
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject obj = null;
                        try {
                            obj = jsonarray.getJSONObject(i);
                            rowId=obj.getString("rowId");
                            FindPaymentController findPaymentController = new FindPaymentController();
                            findPaymentController.setId(obj.getString("Id"));
                            findPaymentController.setFromDate(obj.getString("TDate"));
                            findPaymentController.setToDate(obj.getString("Taskdesc"));
                            findPaymentController.setParty(obj.getString("compname")+"("+obj.getString("Owner")+")");
                            contacts.add(findPaymentController);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    contactAdapter.notifyDataSetChanged();
                    contactAdapter.setLoaded();
                        /* }
                   }, 5000);*/
                } else {
                    isLoadMoreData=false;
                    Toast.makeText(FindPaymentReceived.this, "Loading data completed", Toast.LENGTH_SHORT).show();
                }
            }catch (Exception e)
            {
                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }

                System.out.println(e);
            }
        }
    }
    public String getContactData(){
        //String url="http://"+server+"/And_Sync.asmx/XjsgetCRM_ActionStreamByRowId?Smid="+SMID+"&username="+UserName+"&Adate="+currentDate+"&rowid="+rowId+"";
         String url="http://"+server+"/And_Sync.asmx/XjsgetCRM_ActionStreamByRowId?Smid="+SMID+"&username=Me&Adate="+currentDate+"&rowid="+rowId+"";
        //String url="http://"+server+"/And_Sync.asmx/XjsgetCRM_ActionStreamByRowId?Smid=13&username=Me&Adate=06/Jan/2018&rowid="+rowId+"";
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        return result;
    }
}
