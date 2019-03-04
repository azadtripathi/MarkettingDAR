package com.dm.crmdm_app;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.controller.EnviroController;
import com.dm.controller.SmanController;
import com.dm.controller.TransLeaveController;
import com.dm.library.AlertMessage;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.library.DbCon;
import com.dm.library.ExceptionData;
import com.dm.library.IntentSend;
import com.dm.model.AppData;
import com.dm.model.Sman;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;


public class LeaveRequest extends FragmentActivity implements OnClickListener {
    TextView appDate, appBy, reportTo, spinner_text_transportModeOnDsr, to_text_Spinner, from_text_Spinner;
    EditText noOfDays, fromDate, toDate, reason;
    AlertOkDialog alertOkDialog;
    Spinner fromSpinner, toSpinner, salesManSpinner, salesManMaterial;
    Calendar cal;
    SimpleDateFormat df;
    SharedPreferences preferences2, preferences3;
    SmanController smanController;
    ArrayList<Sman> smanList;
    ArrayAdapter smanAdapter, leaveAdapter, leaveAdapter1, toLeaveAdapter;
    TransLeaveController transLeaveController;
    ImageButton fromDateButton;
    AlertOkDialog dialogWithOutView;
    ImageView save, cancel, find, update, delete;
    String currentDate, Level, server, Days = "", FromDate, ToDate, Reason, AppDate, UserId, SmId, LeaveFlag, AppStatus, LVRQId, Mob;
    ArrayList<String> leaveArrayList, toLeaveArrayList;
    EnviroController enviroController;
    ArrayList<String> leaveArrayList1;
    //MaterialBetterSpinner salesManMaterial;
    ConnectionDetector connectionDetector;
    ExceptionData exceptionData;
    LinearLayout linear_Sales, traDSR, fromLinerSpinner, toLinerSpinner;
    int pos, ListCount;
    ArrayList<String> salesManNameList2 = new ArrayList<String>();
    ArrayList<String> salesManNameList3 = new ArrayList<String>();
    int svalue;
    String[] temp;
    String s1;
    AlertMessage alertMessage;
    boolean search;
    String imc_met, Spinner_id;
    TextInputLayout toleave, fromleave;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    DbCon dbCon;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_request);
        preferences2 = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        ImageView iv = (ImageView) findViewById(R.id.image);
        dbCon = new DbCon(getApplicationContext());

        iv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                (new IntentSend(getApplicationContext(), DashBoradActivity.class)).toSendAcivity();
                finish();
            }
        });
        TextView tv = (TextView) findViewById(R.id.text);
        tv.setText("Leave Request Entry");
        setTitle("Leave Request Entry");

        toleave = (TextInputLayout) findViewById(R.id.both_toleave);
        fromleave = (TextInputLayout) findViewById(R.id.both_fromleave);
        //toleave.setVisibility(View.INVISIBLE);
        //fromleave.setVisibility(View.INVISIBLE);

        connectionDetector = new ConnectionDetector(getApplicationContext());
        appDate = (TextView) findViewById(R.id.applicationDate);
        appBy = (TextView) findViewById(R.id.appliedBy);
        reportTo = (TextView) findViewById(R.id.ReportTo);
        noOfDays = (EditText) findViewById(R.id.noOfDays);
        noOfDays.setGravity(Gravity.TOP | Gravity.RIGHT);
        noOfDays.setKeyListener(DigitsKeyListener.getInstance(true, true));

        fromDate = (EditText) findViewById(R.id.fromDate);
        toDate = (EditText) findViewById(R.id.toDateEdit);
        reason = (EditText) findViewById(R.id.reasonOfleave);
        fromSpinner = (Spinner) findViewById(R.id.fromSpinner);
        toSpinner = (Spinner) findViewById(R.id.toSpinner);
        salesManSpinner = (Spinner) findViewById(R.id.salesPerson);
        save = (ImageView) findViewById(R.id.save);
        cancel = (ImageView) findViewById(R.id.cancel);
        delete = (ImageView) findViewById(R.id.delete);
        to_text_Spinner = (TextView) findViewById(R.id.to_text_Spinner);
        from_text_Spinner = (TextView) findViewById(R.id.from_text_Spinner);
        to_text_Spinner.setVisibility(View.INVISIBLE);
        from_text_Spinner.setVisibility(View.INVISIBLE);
        fromLinerSpinner = (LinearLayout) findViewById(R.id.fromLinerSpinner);
        toLinerSpinner = (LinearLayout) findViewById(R.id.toLinerSpinner);

        /*if(noOfDays.getText().toString().equals("-")|| noOfDays.getText().toString().equals("0") || noOfDays.getText().toString().equals("0.0") || noOfDays.getText().toString().equals("0.00") ||noOfDays.getText().toString().equals("")|| noOfDays.getText().toString().equals(null)){*/

        to_text_Spinner.setVisibility(View.INVISIBLE);
        toSpinner.setVisibility(View.INVISIBLE);
        toLinerSpinner.setVisibility(View.INVISIBLE);

        from_text_Spinner.setVisibility(View.INVISIBLE);
        fromSpinner.setVisibility(View.INVISIBLE);
        fromLinerSpinner.setVisibility(View.INVISIBLE);

        transLeaveController = new TransLeaveController(LeaveRequest.this);
        leaveArrayList1 = new ArrayList<String>();
        leaveArrayList = new ArrayList<String>();
        leaveArrayList.add("--Select--");
        leaveArrayList.add("First Half");
        leaveArrayList.add("Second Half");
        leaveArrayList.add("Full");
        leaveAdapter = new ArrayAdapter<String>(LeaveRequest.this, R.layout.adapterdropdown, leaveArrayList);
        leaveAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(leaveAdapter);

        toLeaveArrayList = new ArrayList<String>();
        toLeaveArrayList.add("--Select--");
        toLeaveArrayList.add("First Half");
        toLeaveArrayList.add("Second Half");
        toLeaveArrayList.add("Full");
        toLeaveAdapter = new ArrayAdapter<String>(LeaveRequest.this, R.layout.adapterdropdown, toLeaveArrayList);
        toLeaveAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toLeaveAdapter);
//		fromSpinner.setVisibility(View.GONE);
//		toSpinner.setVisibility(View.GONE);
        fromDate.setInputType(InputType.TYPE_NULL);
        fromDate.setFocusable(false);
//		fromLinerSpinner.setVisibility(View.GONE);
//		toLinerSpinner.setVisibility(View.GONE);
        cal = Calendar.getInstance();
        df = new SimpleDateFormat("dd/MMM/yyyy");
        appDate.setText(df.format(cal.getTime()));
        currentDate = df.format(cal.getTime());
        smanController = new SmanController(this);
        smanController.open();
        smanList = smanController.getName(preferences2.getString("CONPERID_SESSION", null));
        smanController.close();
        salesManMaterial = (Spinner) findViewById(R.id.MaterialBetterSpinner);
        find = (ImageView) findViewById(R.id.findbutton1);
        linear_Sales = (LinearLayout) findViewById(R.id.linear_Sales);
        save.setVisibility(View.VISIBLE);
        cancel.setVisibility(View.VISIBLE);
        update = (ImageView) findViewById(R.id.update);
        update.setVisibility(View.GONE);
        find.setVisibility(View.VISIBLE);
        //server= Constant.SERVER_WEBSERVICE_URL;
        spinner_text_transportModeOnDsr = (TextView) findViewById(R.id.spinner_text_transportModeOnDsr);
        traDSR = (LinearLayout) findViewById(R.id.traDSR);
        //getLevel();
        SmId = preferences2.getString("CONPERID_SESSION", null);
        preferences3 = this.getSharedPreferences("Update_Leave", Context.MODE_PRIVATE);

        appDataController1 = new AppDataController(LeaveRequest.this);
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server = appData.getCompanyUrl();
        fromSpinner.setVisibility(View.GONE);


        final Intent ds = getIntent();
        if (getIntent().getExtras() != null) {
            String Data = ds.getStringExtra("Data").toString();
            Log.i("Data", Data);
            if (!Data.equals(null)) {
                LVRQId = ds.getStringExtra("LEAVEDOCID_SESSION");
                Days = ds.getStringExtra("Days");
                AppDate = ds.getStringExtra("APPLICATION_DATE_SESSION");
                FromDate = ds.getStringExtra("FROMDATE_SESSION");
                ToDate = ds.getStringExtra("TODATE_SESSION");
                Reason = ds.getStringExtra("Reason");
                AppStatus = ds.getStringExtra("Status");
                LeaveFlag = ds.getStringExtra("Flag");

                noOfDays.setText(Days.toString());

                from_text_Spinner.setVisibility(View.VISIBLE);
                fromSpinner.setVisibility(View.VISIBLE);
                fromLinerSpinner.setVisibility(View.VISIBLE);
                to_text_Spinner.setVisibility(View.VISIBLE);
                toSpinner.setVisibility(View.VISIBLE);
                toLinerSpinner.setVisibility(View.VISIBLE);

                reason.requestFocus();

                if (Days.equals("0.50")) {
                    leaveArrayList.clear();
                    leaveArrayList.add("--Select--");
                    leaveArrayList.add("First Half");
                    leaveArrayList.add("Second Half");
                    leaveAdapter = new ArrayAdapter<String>(LeaveRequest.this, R.layout.adapterdropdown, leaveArrayList);
                    leaveAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    fromSpinner.setAdapter(leaveAdapter);
                } else {
                    leaveArrayList.clear();
                    leaveArrayList.add("");
                    leaveArrayList.add("--Select--");
                    leaveArrayList.add("Second Half");
                    leaveArrayList.add("Full");
                    leaveAdapter = new ArrayAdapter<String>(LeaveRequest.this, R.layout.adapterdropdown, leaveArrayList);
                    leaveAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                    fromSpinner.setAdapter(leaveAdapter);
                }
                if (AppStatus.equalsIgnoreCase("Approve")) {
                    update.setEnabled(false);
                    update.setClickable(false);
                    delete.setEnabled(false);
                    delete.setClickable(false);
                    update.setColorFilter(Color.parseColor("#888888"));
                    delete.setColorFilter(Color.parseColor("#888888"));
                } else if (AppStatus.equalsIgnoreCase("Reject")) {
                    update.setEnabled(false);
                    update.setClickable(false);
                    delete.setEnabled(false);
                    delete.setClickable(false);
                    update.setColorFilter(Color.parseColor("#888888"));
                    delete.setColorFilter(Color.parseColor("#888888"));
                }
                //String LEAVE_USER_ID_SESSION=ds.getStringExtra("LEAVE_USER_ID_SESSION");
                //String kamall=LEAVE_USER_ID_SESSION.split("'*'")[0];
                //salesManMaterial.setSelection(Integer.parseInt(kamall));
                //salesManMaterial.setSelection(Integer.parseInt(ds.getStringExtra("DropId")));

                //salesManMaterial.setVisibility(View.GONE);
                //appDate.setVisibility(View.GONE);
                reason.setText(Reason);
                String date1 = (FromDate);
                String date2 = (ToDate);
                String date3 = (AppDate);
                appDate.setText(date3);
                //System.out.println(format.format(FromDate));
                fromDate.setText(date1);
                toDate.setText(date2);
                //fromDate.setText(DateFunction.ToConvertDateFormat(FromDate,"yyyy-MM-dd HH:mm:ss","dd/MMM/yyyy"));
                //toDate.setText(DateFunction.ToConvertDateFormat(ToDate,"yyyy-MM-dd HH:mm:ss","dd/MMM/yyyy"));
                save.setVisibility(View.GONE);
                update.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
                //cancel.setVisibility(View.GONE);
                if (noOfDays.getText().toString().equalsIgnoreCase("0.5") || noOfDays.getText().toString().equalsIgnoreCase("0.50")) {
                    if (LeaveFlag.equals("F")) {
                        fromSpinner.setSelection(1);
                        toSpinner.setVisibility(View.GONE);
                    } else {
                        fromSpinner.setSelection(2);
                        toSpinner.setVisibility(View.GONE);
                    }
                } else if (Double.parseDouble(noOfDays.getText().toString()) == 1.0) {
                    if (LeaveFlag.equals("S")) {
                        fromSpinner.setSelection(2);
                        toSpinner.setVisibility(View.VISIBLE);
                        toLinerSpinner.setVisibility(View.VISIBLE);
                        toSpinner.setSelection(1);
                    } else if (LeaveFlag.equals("C")) {
                        fromSpinner.setSelection(3);
                        toSpinner.setVisibility(View.VISIBLE);
                        toLinerSpinner.setVisibility(View.VISIBLE);
                        toSpinner.setSelection(3);
                    }
                } else if (Double.parseDouble(noOfDays.getText().toString()) > 1.0) {

                    if (LeaveFlag.equals("S")) {
                        fromSpinner.setSelection(2);
                        toSpinner.setVisibility(View.VISIBLE);
                        toLinerSpinner.setVisibility(View.VISIBLE);
                        toSpinner.setSelection(3);
                    } else if (LeaveFlag.equals("C")) {
                        fromSpinner.setSelection(3);
                        toSpinner.setVisibility(View.VISIBLE);
                        toLinerSpinner.setVisibility(View.VISIBLE);
                        toSpinner.setSelection(1);

                    }
                }
            }
        }

        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(LeaveRequest.this)
                        //.setTitle("Delete entry")
                        .setMessage("Are you sure to delete?")
                        .setCancelable(false)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String jjk = salesManNameList2.get((int) salesManMaterial.getSelectedItemId());
                                SmId = jjk;
                                //SmId=preferences2.getString("CONPERID_SESSION", null);
                                if (connectionDetector.isConnectingToInternet()) {
                                    new deleteLeaveRequest().execute();
                                } else {
                                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                                    dialogWithOutView.show(getFragmentManager(), "Info");
                                }
                                //(new IntentSend(getApplicationContext(),DashBoradActivity.class)).toSendAcivity();
                                //new Custom_Toast(getApplicationContext(), "Record Successfully Delete").showCustomAlert();
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        update.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                checkValidation();

                if (noOfDays.getText().toString().equals("")) {
                    //if(!Level.equalsIgnoreCase("L1")){
                    new Custom_Toast(getApplicationContext(), "Please enter No. of days").showCustomAlert();
                    //}
                } else if (fromDate.getText().toString().equals("")) {
                    new Custom_Toast(getApplicationContext(), "Please select From Date").showCustomAlert();
                } else if (noOfDays.getText().toString().equalsIgnoreCase("-")) {
                    noOfDays.setText("0");
                    new Custom_Toast(getApplicationContext(), "Please enter valid No. of days").showCustomAlert();
                } else if (fromSpinner.getSelectedItem().equals("--Select--")) {
                    new Custom_Toast(getApplicationContext(), "Please select First/Second Half or Full").showCustomAlert();
                } else if (reason.getText().toString().equals("")) {
                    new Custom_Toast(getApplicationContext(), "Please enter Reason").showCustomAlert();
                } else {
                    Days = noOfDays.getText().toString();
                    FromDate = fromDate.getText().toString();
                    ToDate = toDate.getText().toString();
                    Reason = reason.getText().toString().trim();
                    //Appdat=appDate.getText().toString();
                    UserId = preferences2.getString("USER_ID", null);

                    String jjk = salesManNameList2.get((int) salesManMaterial.getSelectedItemId());

                    //SmId=preferences2.getString("CONPERID_SESSION", null);
                    SmId = jjk;
                    String s2 = s1;
                    //fromSpinner.setSelection(leaveAdapter.indexOf(spinner_value));
                    if (s2.equalsIgnoreCase("Full")) {
                        LeaveFlag = "C";
                    } else if (s2.equalsIgnoreCase("First Half")) {
                        LeaveFlag = "F";
                    } else if (s2.equalsIgnoreCase("Second Half")) {
                        LeaveFlag = "S";
                    }
                    if (connectionDetector.isConnectingToInternet())

                    {
                        new editLeaveRequest().execute();
                        clearFields();
                    } else {

                        dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                        dialogWithOutView.show(getFragmentManager(), "Info");
                    }

                }
            }
        });

        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //				clearFields();
                (new IntentSend(getApplicationContext(), LeaveRequest.class)).toSendAcivity();
            }
        });

        if (connectionDetector.isConnectingToInternet())

        {
            new getLevel().execute();
        } else {

            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }


        View parent = findViewById(R.id.parent);
        fromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    colorReset();
					/*hideKeyboard(v);
					showDatePicker(v.getId());
					checkValidation();*/
                } else {
                    hideKeyboard(v);
                    showDatePicker(v.getId());
                    checkValidation();
                    colorReset();
                    //fromSpinner.setSelection(2);
                }
            }
        });
		/*ArrayList<String> salesManNameList=new ArrayList<String>();
		try{
			for(int i=0;i<smanList.size();i++)
			{
				salesManNameList.add(smanList.get(i).getUser_Name());
			}
			//salesManNameList.add("Abc");
			smanAdapter=new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,salesManNameList);
			//salesManSpinner.setAdapter(smanAdapter);
			salesManMaterial.setAdapter(smanAdapter);
			salesManMaterial.setSelection(0);
		}catch(Exception e)
		{
			System.out.println(e);
		}*/
        //salesManMaterial.setSelection(1);
		/*salesManMaterial.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> a,
									View v, int position, long id) {
				int o = position;
				ListCount = o;
				String dv=salesManMaterial.getText().toString();
				//if(dv.)
				//Toast.makeText(getApplicationContext(), dv+"  ", Toast.LENGTH_SHORT).show();
			}


			public void onNothingSelected(AdapterView<?> a,
										  View v, int position, long id) {
				//hsalesManMaterial.setText("g");
				Toast.makeText(getApplicationContext(), "fghfg"+"  ", Toast.LENGTH_SHORT).show();
			}
		});*/


        salesManMaterial.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String jk = spinner_text_transportModeOnDsr.getText().toString();

                if (!jk.equalsIgnoreCase("")) {
                    Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    spinner_text_transportModeOnDsr.setVisibility(View.VISIBLE);
                    spinner_text_transportModeOnDsr.startAnimation(slide_up);
                    //spinner_text_transportModeOnDsr.setTextColor(Color.parseColor("#FF4081"));
                    //traDSR.setBackgroundColor(Color.parseColor("#FF4081"));
                    colorReset();
                    colorRed(traDSR, spinner_text_transportModeOnDsr);
                }
                spinner_text_transportModeOnDsr.setText("Sales Person: *");
                imc_met = salesManMaterial.getSelectedItem().toString();
                //Toast.makeText(getApplicationContext(),arg2, Toast.LENGTH_SHORT).show();
                Spinner_id = salesManNameList2.get((int) salesManMaterial.getSelectedItemId());
                appBy.setText(imc_met);
                new getReport().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                spinner_text_transportModeOnDsr.setVisibility(View.GONE);
            }
        });

        toSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String jk = to_text_Spinner.getText().toString();
                if (!jk.equalsIgnoreCase("")) {
                    Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    to_text_Spinner.setVisibility(View.VISIBLE);
                    toSpinner.setVisibility(View.VISIBLE);
                    toLinerSpinner.setVisibility(View.VISIBLE);
                    to_text_Spinner.startAnimation(slide_up);
                    //to_text_Spinner.setTextColor(Color.parseColor("#FF4081"));
                    //toLinerSpinner.setBackgroundColor(Color.parseColor("#FF4081"));
                    colorReset();
                    //colorRed(toLinerSpinner,to_text_Spinner);
                    if (getIntent().getExtras() != null) {
                        String Data = ds.getStringExtra("Data").toString();
                        Log.i("Data", Data);
                        if (!Data.equals(null)) {
                            colorReset();
                        }
                    }
                }
                to_text_Spinner.setText("To Leave Time: *");
                // String imc_met=spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                to_text_Spinner.setVisibility(View.GONE);
                toSpinner.setVisibility(View.GONE);
                toLinerSpinner.setVisibility(View.GONE);
            }
        });

        //appBy.setText(salesManNameList.get(0));
//		reportTo.setText(smanList.get(0).getDisplayName());

        noOfDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                colorReset();
                String par1 = null;
                String days = null;
                try {
                    String name = s.toString();
                    double name1 = Double.parseDouble(name);
                    String[] arr = String.valueOf(name1).split("\\.");
                    int[] tem = new int[10];
                    tem[0] = Integer.parseInt(arr[0]); // 1
                    tem[1] = Integer.parseInt(arr[1]);
                    String par = String.valueOf(tem[0]);
                    String pa = String.valueOf(tem[1]);
                    if (pa.equalsIgnoreCase("0")) {
                        par1 = par;
                    } else {
                        String pa1 = ".5";
                        pa = pa1;
                        par1 = par + pa;
                    }
                    //Toast.makeText(getApplicationContext(),pa, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("App", "yourDataTask", e);
                }
                try {

                    days = par1;
                    String[] words = days.split("\\.");//splits the string based on string
                    if (words.length == 2) {
                        if (Integer.parseInt(words[1]) > 0) {
                            fromSpinner.setVisibility(View.VISIBLE);
                            if (words[1].equals("5") && words[0].equals("0")) {
                                leaveArrayList.clear();
                                leaveArrayList.add("--Select--");
                                leaveArrayList.add("First Half");
                                leaveArrayList.add("Second Half");
                                leaveAdapter = new ArrayAdapter<String>(LeaveRequest.this, R.layout.adapterdropdown, leaveArrayList);
                                leaveAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                                fromSpinner.setAdapter(leaveAdapter);
                                fromSpinner.setVisibility(View.VISIBLE);
                                toLinerSpinner.setVisibility(View.VISIBLE);
                                fromLinerSpinner.setVisibility(View.VISIBLE);
                                toSpinner.setVisibility(View.VISIBLE);
                                to_text_Spinner.setVisibility(View.VISIBLE);
                                //toSpinner.setEnabled(false);
                                toSpinner.setClickable(false);
                                colorReset();
                            } else {
//							 leaveArrayList.remove(1);
                                leaveArrayList.clear();
                                leaveArrayList.add("--Select--");
                                leaveArrayList.add("Second Half");
                                leaveArrayList.add("Full");
                                leaveAdapter = new ArrayAdapter<String>(LeaveRequest.this, R.layout.adapterdropdown, leaveArrayList);
                                leaveAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                                fromSpinner.setAdapter(leaveAdapter);
                                fromSpinner.setVisibility(View.VISIBLE);
                                toSpinner.setVisibility(View.VISIBLE);
                                toLinerSpinner.setVisibility(View.VISIBLE);
                                fromLinerSpinner.setVisibility(View.VISIBLE);
                                to_text_Spinner.setVisibility(View.VISIBLE);
                                //toSpinner.setEnabled(false);
                                toSpinner.setClickable(false);
                                colorReset();
                            }
                        } else {

                            fromSpinner.setVisibility(View.VISIBLE);
                            toSpinner.setVisibility(View.VISIBLE);
                            toLinerSpinner.setVisibility(View.VISIBLE);
                            fromLinerSpinner.setVisibility(View.VISIBLE);
                            leaveArrayList.clear();
                            leaveArrayList.add("--Select--");
                            leaveArrayList.add("Second Half");
                            leaveArrayList.add("Full");
                            leaveAdapter = new ArrayAdapter<String>(LeaveRequest.this, R.layout.adapterdropdown, leaveArrayList);
                            leaveAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            fromSpinner.setAdapter(leaveAdapter);
                            //toSpinner.setEnabled(false);
                            toSpinner.setClickable(false);
                            colorReset();
                        }

                    } else {
                        if (Integer.parseInt(days) == 1) {
                            fromSpinner.setVisibility(View.VISIBLE);
                            toSpinner.setVisibility(View.VISIBLE);
                            toLinerSpinner.setVisibility(View.VISIBLE);
                            fromLinerSpinner.setVisibility(View.VISIBLE);
                            leaveArrayList.clear();
                            leaveArrayList.add("--Select--");
                            leaveArrayList.add("Second Half");
                            leaveArrayList.add("Full");
                            leaveAdapter = new ArrayAdapter<String>(LeaveRequest.this, R.layout.adapterdropdown, leaveArrayList);
                            leaveAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            fromSpinner.setAdapter(leaveAdapter);
                            //toSpinner.setEnabled(false);
                            toSpinner.setClickable(false);
                            colorReset();
                        } else {
                            leaveArrayList.clear();
                            leaveArrayList.add("--Select--");
                            leaveArrayList.add("Second Half");
                            leaveArrayList.add("Full");
                            leaveAdapter = new ArrayAdapter<String>(LeaveRequest.this, R.layout.adapterdropdown, leaveArrayList);
                            leaveAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            fromSpinner.setAdapter(leaveAdapter);
                            fromSpinner.setVisibility(View.VISIBLE);
                            toSpinner.setVisibility(View.VISIBLE);
                            toLinerSpinner.setVisibility(View.VISIBLE);
                            fromLinerSpinner.setVisibility(View.VISIBLE);
                            //toSpinner.setEnabled(false);
                            toSpinner.setClickable(false);
                            colorReset();
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }
				/*Intent ds=getIntent();
				if(getIntent().getExtras() != null){
					String Data=ds.getStringExtra("Data").toString();
					Log.i("Data",Data);
					if(!Data.equals(null)) {
						if (Integer.parseInt(days)==1.00)
						{
							fromSpinner.setVisibility(View.VISIBLE);
							toSpinner.setVisibility(View.VISIBLE);
							toLinerSpinner.setVisibility(View.VISIBLE);
							fromLinerSpinner.setVisibility(View.VISIBLE);
							leaveArrayList.clear();
							leaveArrayList.add("Select");
							leaveArrayList.add("Second Half");
							leaveArrayList.add("Full");
							leaveAdapter=new ArrayAdapter<String>(LeaveRequest.this, android.R.layout.simple_list_item_1,leaveArrayList);
							fromSpinner.setAdapter(leaveAdapter);
							toSpinner.setClickable(false);
						}
					}
					}*/
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
                //new Custom_Toast(getApplicationContext(), "beforeTextChanged").showCustomAlert();
                //changeNoofdays();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        //noOfDays.setFilters(new InputFilter[] {new DecimalDigitsInputFilter(0,1)});
        reason.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                checkValidation();
                colorReset();
                return false;
            }
        });
        fromDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                //new Custom_Toast(getApplicationContext(), "onTextChanged").showCustomAlert();
                onDateDisplay(fromDate.getText().toString());
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

        fromDate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePicker(v.getId());
            }
        });

        find.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                (new IntentSend(getApplicationContext(), FindLeave.class)).toSendAcivity();
                finish();
            }
        });


        fromSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                s1 = (String) fromSpinner.getSelectedItem();
                String jk = from_text_Spinner.getText().toString();
                if (!jk.equalsIgnoreCase("")) {
                    Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    from_text_Spinner.setVisibility(View.VISIBLE);
                    fromSpinner.setVisibility(View.VISIBLE);
                    fromLinerSpinner.setVisibility(View.VISIBLE);
                    from_text_Spinner.startAnimation(slide_up);
                    //from_text_Spinner.setTextColor(Color.parseColor("#FF4081"));
                    //fromLinerSpinner.setBackgroundColor(Color.parseColor("#FF4081"));
                    colorReset();
                    //colorRed(fromLinerSpinner, from_text_Spinner);
                    Intent ds = getIntent();
                    if (getIntent().getExtras() != null) {
                        String Data = ds.getStringExtra("Data").toString();
                        Log.i("Data", Data);
                        if (!Data.equals(null)) {
                            colorReset();
                        }
                    }
                }
                from_text_Spinner.setText("From Leave Time: *");

                Intent ds = getIntent();
				/*if(getIntent().getExtras() != null){
					String Data=ds.getStringExtra("Data").toString();
					Log.i("Data",Data);
					if(!Data.equals(null)) {
						try{
							double dt1 = Double.parseDouble(noOfDays.getText().toString().equals("")?"0":noOfDays.getText().toString());
							double fnum = Math.floor(dt1);
							double lnum = Math.ceil(dt1);
							double num2 = (fnum + lnum) / 2;
							double num = Math.round(num2);
							if (num > 1)
							{
								if(fromDate.getText().toString().equals(""))
								{
//	        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
								}
								*//*******************  Ashutosh *******************************//*
								else{
									Date tdate=df.parse(fromDate.getText().toString());
									Calendar c=Calendar.getInstance();
									c.setTime(tdate);
									c.add(Calendar.DATE, (int)num-1);
//	                    noOfDays.setText(Double.toString(num2));
									toDate.setText(df.format(c.getTime()));
								}
							}
							else
							{
								if(fromDate.getText().toString().equals(""))
								{
//	        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
								}
								else{
									toDate.setText(fromDate.getText().toString());
								}
							}

							if(Double.parseDouble(noOfDays.getText().toString())==1.0)
							{
								if (fromSpinner.getSelectedItem().equals("Second Half"))
								{
									toSpinner.setVisibility(View.VISIBLE);
									toLinerSpinner.setVisibility(View.VISIBLE);
									toSpinner.setSelection(3);
									//toSpinner.setEnabled(false);
									toSpinner.setClickable(false);
									if(fromDate.getText().toString().equals(""))
									{
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
									}else{
										Date tdate1=df.parse(fromDate.getText().toString());
										Calendar c1=Calendar.getInstance();
										c1.setTime(tdate1);
										c1.add(Calendar.DATE,1);
//	    	                    noOfDays.setText(Double.toString(num2));
										toDate.setText(df.format(c1.getTime()));
									}

								}
								if (fromSpinner.getSelectedItem().equals("Full"))
								{
									if(fromDate.getText().toString().equals(""))
									{
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
									}
									else{
										Date tdate1=df.parse(fromDate.getText().toString());
										Calendar c1=Calendar.getInstance();
										c1.setTime(tdate1);
										toDate.setText(df.format(c1.getTime()));
										toSpinner.setSelection(3);
										//toSpinner.setEnabled(false);
										toSpinner.setClickable(false);

									}
								}
								else{
									if(fromDate.getText().toString().equals(""))
									{
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
									}
									else{
										Date tdate1=df.parse(fromDate.getText().toString());
										Calendar c1=Calendar.getInstance();
										c1.setTime(tdate1);
										c1.add(Calendar.DATE,1);
										toDate.setText(df.format(c1.getTime()));
										toSpinner.setSelection(1);
										//toSpinner.setEnabled(false);
										toSpinner.setClickable(false);
									}
								}
							}
							else if(Double.parseDouble(noOfDays.getText().toString())==1.5){
								if (fromSpinner.getSelectedItem().equals("Second Half"))
								{
									toSpinner.setVisibility(View.VISIBLE);
									toLinerSpinner.setVisibility(View.VISIBLE);
									toSpinner.setSelection(3);
									//toSpinner.setEnabled(false);
									toSpinner.setClickable(false);
									if(fromDate.getText().toString().equals(""))
									{
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
									}else{
										Date tdate1=df.parse(fromDate.getText().toString());
										Calendar c1=Calendar.getInstance();
										c1.setTime(tdate1);
										c1.add(Calendar.DATE,1);
										toDate.setText(df.format(c1.getTime()));
									}
								}
								if (fromSpinner.getSelectedItem().equals("Full"))
								{
									if(fromDate.getText().toString().equals(""))
									{
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
									}
									else{
										Date tdate1=df.parse(fromDate.getText().toString());
										Calendar c1=Calendar.getInstance();
										c1.setTime(tdate1);
										c1.add(Calendar.DATE,1);
										toDate.setText(df.format(c1.getTime()));
										toSpinner.setSelection(1);
										toSpinner.setClickable(false);
									}
								}
								*//*else{
									if(fromDate.getText().toString().equals(""))
									{
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
									}
									else{
										Date tdate1=df.parse(fromDate.getText().toString());
										Calendar c1=Calendar.getInstance();
										c1.setTime(tdate1);
										c1.add(Calendar.DATE,1);
										toDate.setText(df.format(c1.getTime()));
										toSpinner.setSelection(1);
										toSpinner.setClickable(false);
									}
								}*//*
							}
							else if(Double.parseDouble(noOfDays.getText().toString())==0.5 || Double.parseDouble(noOfDays.getText().toString())==0.05)
							{
								if(fromSpinner.getSelectedItem().equals("Select"))
								{
									toSpinner.setVisibility(View.GONE);
									to_text_Spinner.setVisibility(View.GONE);
									toLinerSpinner.setVisibility(View.GONE);
								}
								else if (fromSpinner.getSelectedItem().equals("First Half"))
								{
									toSpinner.setVisibility(View.GONE);
									to_text_Spinner.setVisibility(View.GONE);
									toLinerSpinner.setVisibility(View.GONE);
								}
								else
								{
									toSpinner.setVisibility(View.GONE);
									to_text_Spinner.setVisibility(View.GONE);
									toLinerSpinner.setVisibility(View.GONE);
								}
							}
							else if(Double.parseDouble(noOfDays.getText().toString())>1.0)
							{
								String str = noOfDays.getText().toString();
								String[] str1 = str.split("\\.");
								if (str1.length == 2)
								{
									if(Double.parseDouble(str1[1]) == 5.0)
									{
										if (fromSpinner.getSelectedItem().equals("Second Half"))
										{
											toSpinner.setSelection(3);
											//toSpinner.setEnabled(false);
											toSpinner.setClickable(false);

										}
										else if (fromSpinner.getSelectedItem().equals("Full"))
										{
											toSpinner.setSelection(1);
											//toSpinner.setEnabled(false);
											toSpinner.setClickable(false);
										}
									}
									else
									{
										if (fromSpinner.getSelectedItem().equals("Second Half"))
										{
											toSpinner.setSelection(3);
											//toSpinner.setEnabled(false);
											toSpinner.setClickable(false);
											if(fromDate.getText().toString().equals(""))
											{
//	         	        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
											}
											else{
												//Date tdate1=df.parse(fromDate.getText().toString());
												Date tdate1=df.parse(toDate.getText().toString());
												Calendar c1=Calendar.getInstance();
												c1.setTime(tdate1);
												c1.add(Calendar.DATE,(int)num-1);
												toDate.setText(df.format(c1.getTime()));
											}
										}
										else if (fromSpinner.getSelectedItem().equals("Full"))
										{
											if(str1[2]=="00") {
												toSpinner.setSelection(1);
												toSpinner.setClickable(false);
											}
											else {
												toSpinner.setSelection(3);
												toSpinner.setClickable(false);
											}
										}
									}
								}
								else
								{
									if (fromSpinner.getSelectedItem().equals("Second Half"))
									{
										toSpinner.setSelection(1);
										//toSpinner.setEnabled(false);
										toSpinner.setClickable(false);
										Date tdate1=df.parse(toDate.getText().toString());
										Calendar c1=Calendar.getInstance();
										c1.setTime(tdate1);
										c1.add(Calendar.DATE,1);
										toDate.setText(df.format(c1.getTime()));

									}
									else if (fromSpinner.getSelectedItem().equals("Full"))
									{
										toSpinner.setSelection(3);
										//toSpinner.setEnabled(false);
										toSpinner.setClickable(false);
									}
								}
							}

						}catch(Exception e)
						{
						}
					}
				}*/
                //	else {
                try {
                    double dt1 = Double.parseDouble(noOfDays.getText().toString().equals("") ? "0" : noOfDays.getText().toString());
                    double fnum = Math.floor(dt1);
                    double lnum = Math.ceil(dt1);
                    double num2 = (fnum + lnum) / 2;
                    double num = Math.round(num2);
                    if (num > 1) {
                        if (fromDate.getText().toString().equals("")) {
//	        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
                        }
                        /*******************  Ashutosh *******************************/
                        else {
                            Date tdate = df.parse(fromDate.getText().toString());
                            Calendar c = Calendar.getInstance();
                            c.setTime(tdate);
                            c.add(Calendar.DATE, (int) num - 1);
//	                    noOfDays.setText(Double.toString(num2));
                            toDate.setText(df.format(c.getTime()));
                        }
                    } else {
                        if (fromDate.getText().toString().equals("")) {
//	        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
                        } else {
                            toDate.setText(fromDate.getText().toString());
                        }
                    }

                    if (Double.parseDouble(noOfDays.getText().toString()) == 1.0) {
                        if (fromSpinner.getSelectedItem().equals("Second Half")) {
                            toSpinner.setVisibility(View.VISIBLE);
                            toLinerSpinner.setVisibility(View.VISIBLE);
                            toSpinner.setSelection(3);
                            //toSpinner.setEnabled(false);
                            toSpinner.setClickable(false);
                            if (fromDate.getText().toString().equals("")) {
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
                            } else {
                                Date tdate1 = df.parse(fromDate.getText().toString());
                                Calendar c1 = Calendar.getInstance();
                                c1.setTime(tdate1);
                                c1.add(Calendar.DATE, 1);
//	    	                    noOfDays.setText(Double.toString(num2));
                                toDate.setText(df.format(c1.getTime()));
                            }

                        }
                        if (fromSpinner.getSelectedItem().equals("Full")) {
                            if (fromDate.getText().toString().equals("")) {
                                if (getIntent().getExtras() != null) {
                                    String Data = ds.getStringExtra("Data").toString();
                                    Log.i("Data", Data);
                                    if (!Data.equals(null)) {
                                    }
                                } else {
                                    toSpinner.setSelection(3);
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
                                }
                            } else {
                                Date tdate1 = df.parse(fromDate.getText().toString());
                                Calendar c1 = Calendar.getInstance();
                                c1.setTime(tdate1);
                                toDate.setText(df.format(c1.getTime()));
                                toSpinner.setSelection(3);
                                toSpinner.setVisibility(View.GONE);
                                toLinerSpinner.setVisibility(View.GONE);
                                //toSpinner.setEnabled(false);
                                toSpinner.setClickable(false);

                            }
                        } else {
                            if (fromDate.getText().toString().equals("")) {
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
                            } else {
                                Date tdate1 = df.parse(fromDate.getText().toString());
                                Calendar c1 = Calendar.getInstance();
                                c1.setTime(tdate1);
                                c1.add(Calendar.DATE, 1);
                                toDate.setText(df.format(c1.getTime()));
                                toSpinner.setSelection(1);
                                //toSpinner.setEnabled(false);
                                toSpinner.setClickable(false);
                            }
                        }
                    } else if (noOfDays.getText().toString().equals(".000") || noOfDays.getText().toString().equals("000.") || noOfDays.getText().toString().equals(".0") || noOfDays.getText().toString().equals("-0.") || noOfDays.getText().toString().equals("-0") || noOfDays.getText().toString().equals("-.0") || noOfDays.getText().toString().equals("-") || noOfDays.getText().toString().equals("0.") || noOfDays.getText().toString().equals("0") || noOfDays.getText().toString().equals("0.0") || noOfDays.getText().toString().equals("0.00") || noOfDays.getText().toString().equals("") || noOfDays.getText().toString().equals(null)) {
                        toSpinner.setVisibility(View.GONE);
                        to_text_Spinner.setVisibility(View.GONE);
                        toLinerSpinner.setVisibility(View.GONE);
                        fromSpinner.setVisibility(View.GONE);
                        from_text_Spinner.setVisibility(View.GONE);
                        fromLinerSpinner.setVisibility(View.GONE);
                    } else if (Double.parseDouble(noOfDays.getText().toString()) == 0.5 || Double.parseDouble(noOfDays.getText().toString()) == 0.05) {
                        if (fromSpinner.getSelectedItem().equals("--Select--")) {
							/*toSpinner.setSelection(0);
							//toSpinner.setEnabled(false);
							toSpinner.setClickable(false);*/
                            toSpinner.setVisibility(View.GONE);
                            to_text_Spinner.setVisibility(View.GONE);
                            toLinerSpinner.setVisibility(View.GONE);
                        } else if (fromSpinner.getSelectedItem().equals("First Half")) {
							/*toSpinner.setSelection(1);
							toSpinner.setClickable(false);*/
                            toSpinner.setVisibility(View.GONE);
                            to_text_Spinner.setVisibility(View.GONE);
                            toLinerSpinner.setVisibility(View.GONE);
                        } else {
							/*toSpinner.setSelection(2);
							toSpinner.setClickable(false);*/
                            toSpinner.setVisibility(View.GONE);
                            to_text_Spinner.setVisibility(View.GONE);
                            toLinerSpinner.setVisibility(View.GONE);
                        }
                    } else if (Double.parseDouble(noOfDays.getText().toString()) > 1.0) {
                        String str = noOfDays.getText().toString();
                        String[] str1 = str.split("\\.");
                        if (str1.length == 2) {
                            if (Double.parseDouble(str1[1]) == 5.0) {
                                if (fromSpinner.getSelectedItem().equals("Second Half")) {
                                    toSpinner.setSelection(3);
                                    //toSpinner.setEnabled(false);
                                    toSpinner.setClickable(false);

                                } else if (fromSpinner.getSelectedItem().equals("Full")) {
                                    toSpinner.setSelection(1);
                                    //toSpinner.setEnabled(false);
                                    toSpinner.setClickable(false);
                                }
                            } else {
                                if (fromSpinner.getSelectedItem().equals("Second Half")) {
                                    if (getIntent().getExtras() != null) {
                                        String Data = ds.getStringExtra("Data").toString();
                                        Log.i("Data", Data);
                                        if (!Data.equals(null)) {

                                            if (str1[1].equals("00") || str1[1].equals("") || str1[1].equalsIgnoreCase("0")) {
                                                toSpinner.setSelection(1);
                                                toSpinner.setClickable(false);
                                                if (fromDate.getText().toString().equals("")) {
//	         	        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
                                                } else {
                                                    //Date tdate1=df.parse(fromDate.getText().toString());
                                                    Date tdate1 = df.parse(toDate.getText().toString());
                                                    Calendar c1 = Calendar.getInstance();
                                                    c1.setTime(tdate1);
                                                    c1.add(Calendar.DATE, 1);
                                                    toDate.setText(df.format(c1.getTime()));
                                                }
                                            } else {
                                                toSpinner.setSelection(3);
                                                toSpinner.setClickable(false);
                                                if (fromDate.getText().toString().equals("")) {
//	         	        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
                                                } else {
                                                    //Date tdate1=df.parse(fromDate.getText().toString());
                                                    Date tdate1 = df.parse(toDate.getText().toString());
                                                    Calendar c1 = Calendar.getInstance();
                                                    c1.setTime(tdate1);
                                                    //c1.add(Calendar.DATE,(int)num);
                                                    toDate.setText(df.format(c1.getTime()));
                                                }
                                            }
                                        }
                                    } else {
                                        toSpinner.setSelection(1);
                                        toSpinner.setClickable(false);
                                        if (fromDate.getText().toString().equals("")) {
//	         	        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
                                        } else {
                                            //Date tdate1=df.parse(fromDate.getText().toString());
                                            Date tdate1 = df.parse(toDate.getText().toString());
                                            Calendar c1 = Calendar.getInstance();
                                            c1.setTime(tdate1);
                                            c1.add(Calendar.DATE, 1);
                                            toDate.setText(df.format(c1.getTime()));
                                        }
                                    }

                                } else if (fromSpinner.getSelectedItem().equals("Full")) {
                                    if (getIntent().getExtras() != null) {
                                        String Data = ds.getStringExtra("Data").toString();
                                        Log.i("Data", Data);
                                        if (!Data.equals(null)) {
                                            if (str1[1].equals("00") || str1[1].equals("")) {
                                                toSpinner.setSelection(3);
                                                toSpinner.setClickable(false);
                                            } else {
                                                toSpinner.setSelection(1);
                                                toSpinner.setClickable(false);
                                            }
                                        }
                                    } else {
                                        toSpinner.setSelection(1);
                                        toSpinner.setClickable(false);
                                    }

                                }
                            }
                        } else {
                            if (fromSpinner.getSelectedItem().equals("Second Half")) {
                                toSpinner.setSelection(1);
                                //toSpinner.setEnabled(false);
                                toSpinner.setClickable(false);
                                Date tdate1 = df.parse(toDate.getText().toString());
                                Calendar c1 = Calendar.getInstance();
                                c1.setTime(tdate1);
                                c1.add(Calendar.DATE, 1);
                                toDate.setText(df.format(c1.getTime()));

                            } else if (fromSpinner.getSelectedItem().equals("Full")) {
                                if (getIntent().getExtras() != null) {
                                    String Data = ds.getStringExtra("Data").toString();
                                    Log.i("Data", Data);
                                    if (!Data.equals(null)) {
                                        if (Double.parseDouble(str1[1]) == 00 || str1[1].equals("")) {
                                            toSpinner.setSelection(1);
                                            toSpinner.setClickable(false);
                                        } else {
                                            toSpinner.setSelection(3);
                                            toSpinner.setClickable(false);
                                        }
                                    }
                                } else {
                                    toSpinner.setSelection(3);
                                    toSpinner.setClickable(false);
                                }

                            }
                        }
                    }

                } catch (Exception e) {
                }
                //}
				/*try{
					double dt1 = Double.parseDouble(noOfDays.getText().toString().equals("")?"0":noOfDays.getText().toString());
					double fnum = Math.floor(dt1);
					double lnum = Math.ceil(dt1);
					double num2 = (fnum + lnum) / 2;
					double num3 = num2-1;
					double num = Math.round(num3);
					if (num > 1)
					{
						if(fromDate.getText().toString().equals(""))
						{
//	        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
						}
						*//*******************  Ashutosh *******************************//*
                 *//*else if(!Data.equals(null)) {
							Date tdate=df.parse(fromDate.getText().toString());
							Calendar c=Calendar.getInstance();
							c.setTime(tdate);
							c.add(Calendar.DATE, (int)num-1);
							toDate.setText(df.format(c.getTime()));
						}*//*
						else {
							Date tdate=df.parse(fromDate.getText().toString());
							Calendar c=Calendar.getInstance();
							c.setTime(tdate);
							c.add(Calendar.DATE, (int)num);
							toDate.setText(df.format(c.getTime()));
						}
					}
					else
					{
						if(fromDate.getText().toString().equals(""))
						{
//	        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
						}
						else{
							toDate.setText(fromDate.getText().toString());
						}
					}

					if(Double.parseDouble(noOfDays.getText().toString())==1.0)
					{
						if (fromSpinner.getSelectedItem().equals("Second Half"))
						{
							toSpinner.setVisibility(View.VISIBLE);
							toLinerSpinner.setVisibility(View.VISIBLE);
							toSpinner.setSelection(3);
							//toSpinner.setEnabled(false);
							toSpinner.setClickable(false);
							if(fromDate.getText().toString().equals(""))
							{
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
							}else{
								Date tdate1=df.parse(fromDate.getText().toString());
								Calendar c1=Calendar.getInstance();
								c1.setTime(tdate1);
								c1.add(Calendar.DATE, (int)num-1);
//	    	                    noOfDays.setText(Double.toString(num2));
								toDate.setText(df.format(c1.getTime()));
							}

						}
						if (fromSpinner.getSelectedItem().equals("Full"))
						{
							if(fromDate.getText().toString().equals(""))
							{
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
							}
							else{
								Date tdate1=df.parse(fromDate.getText().toString());
								Calendar c1=Calendar.getInstance();
								c1.setTime(tdate1);
								//c1.add(Calendar.DATE, (int)num-1);
								toDate.setText(df.format(c1.getTime()));
								toSpinner.setSelection(3);
								//toSpinner.setEnabled(false);
								toSpinner.setClickable(false);

							}}
						else{
							if(fromDate.getText().toString().equals(""))
							{
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
							}
							else{
								Date tdate1=df.parse(fromDate.getText().toString());
								Calendar c1=Calendar.getInstance();
								c1.setTime(tdate1);
								c1.add(Calendar.DATE,1);
								//c1.add(Calendar.DATE, (int)num-1);
								toDate.setText(df.format(c1.getTime()));
								toSpinner.setSelection(1);
								//toSpinner.setEnabled(false);
								toSpinner.setClickable(false);
							}
						}
					}
					else if(Double.parseDouble(noOfDays.getText().toString())==0.5 || Double.parseDouble(noOfDays.getText().toString())==0.05)
					{
						if(fromSpinner.getSelectedItem().equals("Select"))
						{
							*//*toSpinner.setSelection(0);
							//toSpinner.setEnabled(false);
							toSpinner.setClickable(false);*//*
							toSpinner.setVisibility(View.GONE);
							to_text_Spinner.setVisibility(View.GONE);
							toLinerSpinner.setVisibility(View.GONE);
						}
						else if (fromSpinner.getSelectedItem().equals("First Half"))
						{
							*//*toSpinner.setSelection(1);
							toSpinner.setClickable(false);*//*
							toSpinner.setVisibility(View.GONE);
							to_text_Spinner.setVisibility(View.GONE);
							toLinerSpinner.setVisibility(View.GONE);
						}
						else
						{
							*//*toSpinner.setSelection(2);
							toSpinner.setClickable(false);*//*
							toSpinner.setVisibility(View.GONE);
							to_text_Spinner.setVisibility(View.GONE);
							toLinerSpinner.setVisibility(View.GONE);
						}
					}
					else if(Double.parseDouble(noOfDays.getText().toString())>1.0)
					{
						String str = noOfDays.getText().toString();
						String[] str1 = str.split("\\.");
						if (str1.length == 2)
						{
							if(Double.parseDouble(str1[1]) == 5.0)
							{
								if (fromSpinner.getSelectedItem().equals("Second Half"))
								{
									toSpinner.setSelection(3);
									//toSpinner.setEnabled(false);
									toSpinner.setClickable(false);

								}
								else if (fromSpinner.getSelectedItem().equals("Full"))
								{
									toSpinner.setSelection(3);
									//toSpinner.setEnabled(false);
									toSpinner.setClickable(false);
								}
							}
							else
							{
								if (fromSpinner.getSelectedItem().equals("Second Half"))
								{
									toSpinner.setSelection(3);
									//toSpinner.setEnabled(false);
									toSpinner.setClickable(false);
									if(fromDate.getText().toString().equals(""))
									{
//	         	        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
									}
									else{
										//Date tdate1=df.parse(fromDate.getText().toString());
										Date tdate1=df.parse(toDate.getText().toString());
										Calendar c1=Calendar.getInstance();
										c1.setTime(tdate1);
										//c1.add(Calendar.DATE,1);
										c1.add(Calendar.DATE, (int)num-1);
										toDate.setText(df.format(c1.getTime()));
									}
								}
								else if (fromSpinner.getSelectedItem().equals("Full"))
								{
									toSpinner.setSelection(1);
									//toSpinner.setEnabled(false);
									toSpinner.setClickable(false);
								}
							}
						}
						else
						{
							if (fromSpinner.getSelectedItem().equals("Second Half"))
							{
								toSpinner.setSelection(3);
								//toSpinner.setEnabled(false);
								toSpinner.setClickable(false);
								Date tdate1=df.parse(toDate.getText().toString());
								Calendar c1=Calendar.getInstance();
								c1.setTime(tdate1);
								//c1.add(Calendar.DATE,1);
								c1.add(Calendar.DATE, (int)num);
								toDate.setText(df.format(c1.getTime()));

							}
							else if (fromSpinner.getSelectedItem().equals("Full"))
							{
								toSpinner.setSelection(1);
								//toSpinner.setEnabled(false);
								toSpinner.setClickable(false);
							}
						}
					}

				}catch(Exception e)
				{
				}*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                from_text_Spinner.setVisibility(View.GONE);
            }
        });

        noOfDays.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                colorReset();
                checkValidation();
            }
        });

        save.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                checkValidation();
                //salesManSpinner.getSelectedItem() != null
                if (salesManMaterial.getSelectedItem() == null) {
                    new Custom_Toast(getApplicationContext(), "Please select a sales person").showCustomAlert();
					/*if(!Level.equalsIgnoreCase("L1")){
						new Custom_Toast(getApplicationContext(), "Please select a sales person").showCustomAlert();
					}*/
                } else if (noOfDays.getText().toString().equals("")) {
                    new Custom_Toast(getApplicationContext(), "Please enter the no. of days.").showCustomAlert();
                } else if (noOfDays.getText().toString().equalsIgnoreCase("-")) {
                    noOfDays.setText("0");
                    new Custom_Toast(getApplicationContext(), "Please enter valid no. of days").showCustomAlert();
                } else if (fromDate.getText().toString().equals("")) {
                    new Custom_Toast(getApplicationContext(), "Please select from date first").showCustomAlert();
                } else if (fromSpinner.getSelectedItem().equals("--Select--")) {
                    new Custom_Toast(getApplicationContext(), "Please select second half/full").showCustomAlert();
                } else if (reason.getText().toString().equals("")) {
                    new Custom_Toast(getApplicationContext(), "Please Enter reason").showCustomAlert();
                } else {
                    String jjk = salesManNameList2.get((int) salesManMaterial.getSelectedItemId());

                    //SmId=preferences2.getString("CONPERID_SESSION", null);
                    SmId = jjk;
                    //SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy");
                    //java.sql.Date date1=convertDate(fromDate.getText().toString());
                    //java.sql.Date date2=convertDate(toDate.getText().toString());
                    //FromDate=sdf.format(fromDate.getText().toString());

                    Days = noOfDays.getText().toString();
                    FromDate = fromDate.getText().toString();
                    ToDate = toDate.getText().toString();
                    Reason = reason.getText().toString().trim();
                    AppDate = appDate.getText().toString();
                    UserId = preferences2.getString("USER_ID", null);
                    SmId = preferences2.getString("CONPERID_SESSION", null);

                    String s2 = s1;
                    if (s2.equalsIgnoreCase("Full")) {
                        LeaveFlag = "C";
                    } else if (s2.equalsIgnoreCase("First Half")) {
                        LeaveFlag = "F";
                    } else if (s2.equalsIgnoreCase("Second Half")) {
                        LeaveFlag = "S";
                    }

                    enviroController = new EnviroController(getApplicationContext());
                    enviroController.open();
                    int maxDoc_id = enviroController.getMaxLeave_no();
                    enviroController.close();
                    Mob = preferences2.getString("CONPERID_SESSION", null) + " " + " " + String.format("%08d", ++maxDoc_id);
                    enviroController.open();
                    enviroController.updateEnviroLeave(maxDoc_id);
                    enviroController.close();
                    //new Custom_Toast(getApplicationContext(), Mob).showCustomAlert();
                    if (connectionDetector.isConnectingToInternet())

                    {
                        new addLeaveRequest().execute();
                    } else {

                        dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                        dialogWithOutView.show(getFragmentManager(), "Info");
                    }


                    //new Custom_Toast(getApplicationContext(),"Leave Request saved successfully",R.drawable.save).showCustomAlert();
//				  new ExportLeaveRequest(LeaveRequest.this);
                }
            }
        });

        cancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                clearFields();
            }
        });

        dbCon.open();
        //save.setEnabled(dbCon.ButtonEnable("LeaveRequest","Party","Add"));
        if (!dbCon.ButtonEnable("LeaveRequest", "Plan", "Add")) {
            save.setColorFilter(Color.parseColor("#808080"));
            save.setEnabled(false);
        }
        if (!dbCon.ButtonEnable("LeaveRequest", "Plan", "Edit")) {
            update.setColorFilter(Color.parseColor("#808080"));
            update.setEnabled(false);
        }
        if (!dbCon.ButtonEnable("LeaveRequest", "Plan", "Delete")) {
            delete.setColorFilter(Color.parseColor("#808080"));
            delete.setEnabled(false);
        }
        dbCon.close();
    }

    private void checkValidation() {
        String par1 = null;
        String pa1 = null;
        try {
            String name = noOfDays.getText().toString();
            double name1 = Double.parseDouble(name);
            String[] arr = String.valueOf(name1).split("\\.");
            int[] tem = new int[10];
            tem[0] = Integer.parseInt(arr[0]); // 1
            tem[1] = Integer.parseInt(arr[1]);
            String par5 = String.valueOf(arr[0]);
            String par6 = String.valueOf(arr[1]);
            String par = String.valueOf(tem[0]);
            String pa = String.valueOf(tem[1]);

            if (pa.equalsIgnoreCase("0")) {
                par1 = par;
            } else if (!pa.equalsIgnoreCase("5")) {
                pa1 = ".5";
                pa = pa1;
                par1 = par + pa;
                noOfDays.setText(par1);
            } else if (!par6.equalsIgnoreCase("5")) {
                pa = ".5";
                par1 = par + pa;
                noOfDays.setText(par1);
            }

        } catch (Exception e) {
            Log.e("App", "yourDataTask", e);
        }

        try {
            String name = noOfDays.getText().toString();
            double name1 = Double.parseDouble(name);
            String[] arr = String.valueOf(name1).split("-");
            if (arr[0].length() == 0) {
                new Custom_Toast(getApplicationContext(), "Please enter valid no. of days").showCustomAlert();
                noOfDays.setText("0");
            }
        } catch (Exception e) {
            Log.e("App", "yourDataTask", e);
        }
    }

    private void hideKeyboard(View view) {
		/*InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);*/

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(fromDate.getWindowToken(), 0);
    }

    private String convertDate(String Date) {
        String[] parts = Date.split(" ")[0].split("/");
        int year = Integer.parseInt(parts[2]);
        String mon = "Jan";
        if (parts[1].equalsIgnoreCase("1")) {
            mon = "Jan";
        } else if (parts[1].equalsIgnoreCase("2")) {
            mon = "Fab";
        } else if (parts[1].equalsIgnoreCase("3")) {
            mon = "Mar";
        } else if (parts[1].equalsIgnoreCase("4")) {
            mon = "Apr";
        } else if (parts[1].equalsIgnoreCase("5")) {
            mon = "May";
        } else if (parts[1].equalsIgnoreCase("6")) {
            mon = "Jun";
        } else if (parts[1].equalsIgnoreCase("7")) {
            mon = "Jul";
        } else if (parts[1].equalsIgnoreCase("8")) {
            mon = "Aug";
        } else if (parts[1].equalsIgnoreCase("9")) {
            mon = "Sep";
        } else if (parts[1].equalsIgnoreCase("10")) {
            mon = "Oct";
        } else if (parts[1].equalsIgnoreCase("11")) {
            mon = "Nov";
        } else {
            mon = "Dec";
        }
        String date = parts[0] + "/" + mon + "/" + year;
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
        if (id == R.id.fromDate) {
            date.setCallBack(ondate);
            date.show(getSupportFragmentManager(), "Date Picker");
        }
		/*try {
			String par1;
				String name = noOfDays.getText().toString();
				double name1 = Double.parseDouble(name);
				String[] arr = String.valueOf(name1).split("\\.");
				int[] tem = new int[10];
				tem[0] = Integer.parseInt(arr[0]); // 1
				tem[1] = Integer.parseInt(arr[1]);
				String par = String.valueOf(tem[0]);
				String pa = String.valueOf(tem[1]);
			if(pa.equalsIgnoreCase("0")){
				 par1 = par ;
			}
			else {
				String pa1 = ".5";
				pa = pa1;
				 par1 = par + pa;
			}
			noOfDays.setText(par1);
				//Toast.makeText(getApplicationContext(),pa, Toast.LENGTH_SHORT).show();
			}
		catch (Exception e){
			Log.e("App", "yourDataTask", e);
		}
		try {
			String name = noOfDays.getText().toString();
			double name1 = Double.parseDouble(name);
			String[] arr = String.valueOf(name1).split("-");
			if(arr[0].length() == 0){
				noOfDays.setText("0");
			}
		} catch (Exception e) {
			Log.e("App", "yourDataTask", e);
		}*/
    }

    OnDateSetListener ondate = new OnDateSetListener() {
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
            System.out.println(format2.format(date));
            Date filledDate = null, currenttime = null;
            try {
                filledDate = format2.parse(format2.format(date));
                currenttime = format2.parse(currentDate);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Calendar c = Calendar.getInstance();
            System.out.println(c.getTime()); // Tue Jun 18 17:07:45 IST 2013
            c.set(Calendar.DATE, c.get(Calendar.DATE) - 30);
            System.out.println("time before=" + c.getTime() + " " + c.getTimeInMillis());
            Date dateBefore = new Date(currenttime.getTime() - (30 * 24 * 3600 * 1000));
//			 long i=filledDate.getTime()-dateBefore.getTime();
            long i = filledDate.getTime() - c.getTimeInMillis();
            System.out.println("30 days=" + (30 * 24 * 3600 * 1000));
            System.out.println("curent=" + currenttime.getTime());
            System.out.println("before=" + c.getTimeInMillis());
            System.out.println("i=" + i);

            fromDate.setText(format2.format(date));
            fromSpinner.setSelection(0);
        }
    };


    //
    public void onDateDisplay(String selectedDate) {
        try {
            double dt1 = Double.parseDouble(noOfDays.getText().toString().equals("") ? "0" : noOfDays.getText().toString());
            double fnum = Math.floor(dt1);
            double lnum = Math.ceil(dt1);
            double num2 = (fnum + lnum) / 2;
            double num3 = num2 - 1;
            double num = Math.round(num3);
            if (num > 1) {

                Date tdate = df.parse(fromDate.getText().toString());
                Calendar c = Calendar.getInstance();
                c.setTime(tdate);
                c.add(Calendar.DATE, (int) num);
//             noOfDays.setText(Double.toString(num2));
                toDate.setText(df.format(c.getTime()));
            } else {
                Date tdate = df.parse(fromDate.getText().toString());
                Calendar c = Calendar.getInstance();
                c.setTime(tdate);
                c.add(Calendar.DATE, (int) num);
//             noOfDays.setText(Double.toString(num2));
                toDate.setText(df.format(c.getTime()));
                //toDate.setText(fromDate.getText().toString());
            }
        } catch (Exception e) {
        }

    }

    //	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.leave_request, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//		int id = item.getItemId();
//		switch (item.getItemId())
//		{
//			case R.id.search:
//				(new IntentSend(getApplicationContext(),FindLeaveRequestList.class)).toSendAcivity();
//				break;
//			case android.R.id.home:
//				(new IntentSend(getApplicationContext(),DashBoradActivity.class)).toSendAcivity();
//				break;
//			default:
//				break;
//
//		}
//		// TODO Auto-generated method stub
//		return super.onOptionsItemSelected(item);
//	}


    private void clearFields() {
        noOfDays.setText("");
        fromDate.setText("");
        toDate.setText("");
        fromSpinner.setSelection(0);
        toSpinner.setSelection(0);
        //fromSpinner.setVisibility(View.GONE);
        //toSpinner.setVisibility(View.GONE);
        reason.setText("");
        noOfDays.setFocusable(true);

        save.setVisibility(View.VISIBLE);
        update.setVisibility(View.GONE);
        delete.setVisibility(View.GONE);
        cancel.setVisibility(View.VISIBLE);
        //salesManMaterial.setSelection(0);
        //salesManMaterial.setClickable(true);
        //salesManMaterial.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    }


    protected class getReport extends AsyncTask<Void, Void, JSONArray> {
        ProgressDialog pdLoading = new ProgressDialog(LeaveRequest.this);

        @Override
        protected void onPreExecute() {
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            //server= Constant.SERVER_WEBSERVICE_URL;
            String MOb = preferences2.getString("PDAID_SESSION", "");
            //String str="http://" + server + "/And_Sync.asmx/JSGetUserDetailByPDAId?PDA_Id=" + MOb + "&minDate=0";
            String str = "http://" + server + "/And_Sync.asmx/xJSFindLeaveUserReportingWise?SmId=" + Spinner_id;
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                JSONArray response = new JSONArray(stringBuffer.toString());
                return new JSONArray(response.toString());
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
        protected void onPostExecute(JSONArray response) {
            if (response != null) {
                ArrayList<String> salesManNameList = new ArrayList<String>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject objs = null;
                    try {
                        objs = response.getJSONObject(i);
                        String reporter_name = objs.getString("Reporttonm");
                        reportTo.setText(reporter_name);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            super.onPostExecute(response);
            pdLoading.dismiss();
        }
    }

    protected class getLevel extends AsyncTask<Void, Void, JSONArray> {
        ProgressDialog pdLoading = new ProgressDialog(LeaveRequest.this);

        @Override
        protected void onPreExecute() {
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }

        @Override
        protected JSONArray doInBackground(Void... params) {
            //server= Constant.SERVER_WEBSERVICE_URL;
            String MOb = preferences2.getString("PDAID_SESSION", "");
            //String str="http://" + server + "/And_Sync.asmx/JSGetUserDetailByPDAId?PDA_Id=" + MOb + "&minDate=0";
            String str = "http://" + server + "/And_Sync.asmx/JSGetUnderUser?PDA_Id=" + MOb + "&minDate=0&Smid=" + SmId;
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                //JSONObject response = new JSONObject(stringBuffer.toString());
                JSONArray response = new JSONArray(stringBuffer.toString());
                //JSONObject obj = response.getJSONObject(0);
                return new JSONArray(response.toString());
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
        protected void onPostExecute(JSONArray response) {
            if (response != null) {
                ArrayList<String> salesManNameList = new ArrayList<String>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject objs = null;
                    try {
                        objs = response.getJSONObject(i);
                        salesManNameList.add(objs.getString("Name").toString());
                        salesManNameList2.add(objs.getString("Id").toString());
                        salesManNameList3.add(objs.getString("UserId").toString());
                        Level = objs.getString("RoleName");
                        //spinnerMap.put(objs.getString("Name"),objs.getString("Id").toString());
                        //spinnerArray[i] = objs.getString("Name");

                        //String id = objs.getString("Id").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                //Level = response.getJSONObject(response.length()-1).getString("RoleName").toString();
                smanAdapter = new ArrayAdapter<String>(LeaveRequest.this, R.layout.adapterdropdown, salesManNameList);
                smanAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                //salesManSpinner.setAdapter(smanAdapter);
                salesManMaterial.setAdapter(smanAdapter);

                Intent ds = getIntent();
                if (getIntent().getExtras() != null) {
                    String Data = ds.getStringExtra("Data").toString();
                    String Dropid = ds.getStringExtra("DropId").toString();
                    Log.i("Data", Data);
                    if (!Data.equals(null)) {
                        //int spiner_id = Integer.parseInt(ds.getStringExtra("DropId"));
                        //salesManMaterial.setSelection(Integer.parseInt(ds.getStringExtra("DropId")));
                        salesManMaterial.setSelection(salesManNameList2.indexOf(Dropid));
                        salesManMaterial.setClickable(false);
                        salesManMaterial.setEnabled(false);
                    } else {
                        //salesManMaterial.setSelection(0);
                        salesManMaterial.setClickable(true);
                        salesManMaterial.setEnabled(true);
                    }
                } else {
                    salesManMaterial.setSelection(salesManNameList2.indexOf(SmId));
                }

                if (response.length() == 1) {
                    salesManMaterial.setClickable(false);
                    salesManMaterial.setEnabled(false);
                    //linear_Sales.setVisibility(View.GONE);
                }
                //Log.e("App", "Success: " + response.getString("yourJsonElement") );
            }
            super.onPostExecute(response);
            pdLoading.dismiss();
        }
    }

    public class addLeaveRequest extends AsyncTask<String, Void, String> {
        String server_response;
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        ProgressDialog pdLoading = new ProgressDialog(LeaveRequest.this);
        String query = null, query1 = null;

        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
            String jjk = salesManNameList2.get((int) salesManMaterial.getSelectedItemId());
            SmId = jjk;
            //Mob = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
			/*String jik=salesManNameList3.get((int) salesManMaterial.getSelectedItemId());
			UserId=jik;*/

            //new Custom_Toast(getApplicationContext(), UserId).showCustomAlert();
            query = Mob;
            params.add(new BasicNameValuePair("Android_Id", query));
            params.add(new BasicNameValuePair("UserId", UserId));
            params.add(new BasicNameValuePair("NoOfDays", Days));
            params.add(new BasicNameValuePair("FromDate", FromDate));
            params.add(new BasicNameValuePair("ToDate", ToDate));
            params.add(new BasicNameValuePair("Reason", reason.getText().toString()));
            params.add(new BasicNameValuePair("SmId", SmId));
            params.add(new BasicNameValuePair("LeaveFlag", LeaveFlag));

        }

        protected String doInBackground(String... arg0) {
            try {

                URL url = new URL("http://" + server + "/And_Sync.asmx/JSTransLeaveEntryInsert");// here is your URL path
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));

                writer.flush();
                writer.close();
                os.close();

                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(new
                            InputStreamReader(
                            conn.getInputStream()));

                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    server_response = sb.toString();
                    return sb.toString();

                } else {
                    return new String("false : " + responseCode);
                }
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                new Custom_Toast(getApplicationContext(), s).showCustomAlert();
                clearFields();
				/*if(stringBuffer.equalsIgnoreCase("Leave is not allowed on Week off")){
					new Custom_Toast(getApplicationContext(), "Leave is not allowed on Week off").showCustomAlert();
				}else if(stringBuffer.equalsIgnoreCase("Holiday is declared for this day.")){
					new Custom_Toast(getApplicationContext(), "Holiday is declared for this day.").showCustomAlert();
				}else if(stringBuffer.equalsIgnoreCase("Leave Already Exist")){
					new Custom_Toast(getApplicationContext(), "Leave Already Exist").showCustomAlert();
				}else if(stringBuffer.equalsIgnoreCase("Data not Inserted Please Try Again Later")){
					new Custom_Toast(getApplicationContext(), "Data not Inserted Please Try Again Later").showCustomAlert();
				}else if(stringBuffer.equalsIgnoreCase("Record Save Successfully")) {
					new Custom_Toast(getApplicationContext(), "Record Save Successfully").showCustomAlert();
					clearFields();
					//(new IntentSend(getApplicationContext(),DashBoradActivity.class)).toSendAcivity();
				}else{
					new Custom_Toast(getApplicationContext(), "Please Try Again Later").showCustomAlert();
				}*/
            }
            pdLoading.dismiss();
        }
    }


    /*protected class addLeaveRequest extends AsyncTask<Void, Void, String>
    {
        ProgressDialog pdLoading = new ProgressDialog(LeaveRequest.this);
        String query = null, query1 = null;
        @Override
        protected void onPreExecute() {
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
            String jjk=salesManNameList2.get((int) salesManMaterial.getSelectedItemId());
            SmId=jjk;
            //Mob = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            *//*String jik=salesManNameList3.get((int) salesManMaterial.getSelectedItemId());
			UserId=jik;*//*

			//new Custom_Toast(getApplicationContext(), UserId).showCustomAlert();
		}

		@Override
		protected String doInBackground(Void... params)
		{
			try {
				query = URLEncoder.encode(Mob, "utf-8");
				query1 = URLEncoder.encode(Reason, "utf-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			String str="http://" + server + "/And_Sync.asmx/JSTransLeaveEntryInsert?Android_Id=" + query + "&UserId="+UserId+"&NoOfDays="
					+Days+ "&FromDate="+FromDate+"&ToDate=" +ToDate+ "&Reason="+query1+"&SMId=" +SmId+ "&LeaveFlag="+LeaveFlag;
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

				return stringBuffer.toString().replace("\"", "");
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
		protected void onPostExecute(String stringBuffer)
		{
			if(stringBuffer != null)
			{
				new Custom_Toast(getApplicationContext(), stringBuffer).showCustomAlert();
				clearFields();
				*//*if(stringBuffer.equalsIgnoreCase("Leave is not allowed on Week off")){
					new Custom_Toast(getApplicationContext(), "Leave is not allowed on Week off").showCustomAlert();
				}else if(stringBuffer.equalsIgnoreCase("Holiday is declared for this day.")){
					new Custom_Toast(getApplicationContext(), "Holiday is declared for this day.").showCustomAlert();
				}else if(stringBuffer.equalsIgnoreCase("Leave Already Exist")){
					new Custom_Toast(getApplicationContext(), "Leave Already Exist").showCustomAlert();
				}else if(stringBuffer.equalsIgnoreCase("Data not Inserted Please Try Again Later")){
					new Custom_Toast(getApplicationContext(), "Data not Inserted Please Try Again Later").showCustomAlert();
				}else if(stringBuffer.equalsIgnoreCase("Record Save Successfully")) {
					new Custom_Toast(getApplicationContext(), "Record Save Successfully").showCustomAlert();
					clearFields();
					//(new IntentSend(getApplicationContext(),DashBoradActivity.class)).toSendAcivity();
				}else{
					new Custom_Toast(getApplicationContext(), "Please Try Again Later").showCustomAlert();
				}*//*
			}
			super.onPostExecute(stringBuffer);
			pdLoading.dismiss();
		}
	}*/
    private String getQuery(List<NameValuePair> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;

        for (NameValuePair pair : params) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(pair.getName(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    protected class editLeaveRequest extends AsyncTask<Void, Void, String> {
        ProgressDialog pdLoading = new ProgressDialog(LeaveRequest.this);
        String query = null, query1 = null;

        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
            Intent ds = getIntent();
            if (getIntent().getExtras() != null) {
                String Data = ds.getStringExtra("Data").toString();
                Log.i("Data", Data);
                if (!Data.equals(null)) {
                    SmId = ds.getStringExtra("LEAVE_USER_ID_SESSION");
                }
            }

        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                query = URLEncoder.encode(Reason, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            //server= Constant.SERVER_WEBSERVICE_URL;
            //String MOb = preferences2.getString("PDAID_SESSION", "");
            String str = "http://" + server + "/And_Sync.asmx/JSTransLeaveEntryUpdate?Android_Id=" + "" + "&UserId=" + UserId + "&NoOfDays="
                    + Days + "&FromDate=" + FromDate + "&ToDate=" + ToDate + "&Reason=" + query + "&SMId=" + SmId + "&LeaveFlag=" + LeaveFlag + "&LVRQId=" + LVRQId;
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                return stringBuffer.toString().replace("\"", "");
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
        protected void onPostExecute(String stringBuffer) {
            if (stringBuffer != null) {
                new Custom_Toast(getApplicationContext(), stringBuffer).showCustomAlert();
				/*preferences3=null;
				if(stringBuffer.equalsIgnoreCase("Holiday is declared for this day.")){
					new Custom_Toast(getApplicationContext(), "Holiday is declared for this day.").showCustomAlert();
				}else if(stringBuffer.equalsIgnoreCase("Record Updated Successfully")){
					clearFields();
					new Custom_Toast(getApplicationContext(), "Record Updated Successfully").showCustomAlert();
					//(new IntentSend(getApplicationContext(),DashBoradActivity.class)).toSendAcivity();
				}else if(stringBuffer.equalsIgnoreCase("Leave Already Exists")){
					new Custom_Toast(getApplicationContext(), "Leave Already Exists").showCustomAlert();
				}else if(stringBuffer.equalsIgnoreCase("Data not Inserted Please Try Again Later")){
					new Custom_Toast(getApplicationContext(), "Data not Inserted Please Try Again Later").showCustomAlert();
				}else if(stringBuffer.equalsIgnoreCase("Leave is either Approved or Rejected")){
					clearFields();
					new Custom_Toast(getApplicationContext(), "Leave is either Approved or Rejected").showCustomAlert();
				}else{
					new Custom_Toast(getApplicationContext(), "Please Try Again Later").showCustomAlert();
				}*/
            }
            super.onPostExecute(stringBuffer);
            pdLoading.dismiss();
        }
    }

    protected class deleteLeaveRequest extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
            Intent ds = getIntent();
            if (getIntent().getExtras() != null) {
                String Data = ds.getStringExtra("Data").toString();
                Log.i("Data", Data);
                if (!Data.equals(null)) {
                    SmId = ds.getStringExtra("LEAVE_USER_ID_SESSION");
                }
            }
			/*String jik=salesManNameList3.get((int) salesManMaterial.getSelectedItemId());
			UserId=jik;*/
            //new Custom_Toast(getApplicationContext(), SmId).showCustomAlert();
            //new Custom_Toast(getApplicationContext(), UserId).showCustomAlert();
        }

        @Override
        protected String doInBackground(Void... params) {
            //server= Constant.SERVER_WEBSERVICE_URL;
            String MOb = preferences2.getString("PDAID_SESSION", "");
            String str = "http://" + server + "/And_Sync.asmx/JSTransLeaveEntryDelete?SMId=" + SmId + "&LVRQId=" + LVRQId;
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(str);
                urlConn = url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                return stringBuffer.toString().replace("\"", "");
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
        protected void onPostExecute(String stringBuffer) {
            if (stringBuffer != null) {
                new Custom_Toast(getApplicationContext(), stringBuffer).showCustomAlert();
                clearFields();
				/*preferences3=null;
				if(stringBuffer.equalsIgnoreCase("Record Deleted Successfully")){
					clearFields();
					new Custom_Toast(getApplicationContext(), "Record Deleted Successfully").showCustomAlert();
				}else if(stringBuffer.equalsIgnoreCase("Leave is either Approved or Rejected")){
					clearFields();
					new Custom_Toast(getApplicationContext(), "Leave is either Approved or Rejected").showCustomAlert();
				}else if(stringBuffer.equalsIgnoreCase("Record Is Not Deleted")){
					new Custom_Toast(getApplicationContext(), "Record Is Not Deleted").showCustomAlert();
				}else{
					new Custom_Toast(getApplicationContext(), "Please Try Again Later").showCustomAlert();
				}*/
            }
        }
    }

    private void colorReset() {
        spinner_text_transportModeOnDsr.setTextColor(Color.parseColor("#FF808080"));
        traDSR.setBackgroundColor(Color.parseColor("#666666"));
        from_text_Spinner.setTextColor(Color.parseColor("#FF808080"));
        fromLinerSpinner.setBackgroundColor(Color.parseColor("#666666"));
        to_text_Spinner.setTextColor(Color.parseColor("#FF808080"));
        toLinerSpinner.setBackgroundColor(Color.parseColor("#666666"));
    }

    //Color Active
    public void colorRed(LinearLayout linear, TextView tv) {
        tv.setTextColor(Color.parseColor("#FF4081"));
        linear.setBackgroundColor(Color.parseColor("#FF4081"));
    }

    /*private boolean checkValidation() {
        boolean ret = true;
        //if (!Validation.isEmailAddress(email, true)) ret = false;
        if (!Validation.isPhoneNumber(noOfDays, true)) ret = false;
        //if (!Validation.hasText(name)) ret = false;
        //if (!Validation.hasText(password)) ret = false;
        // if (!Validation.hasText(address)) ret = false;

        return ret;
    }*/
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        preferences3 = null;
        //super.onBackPressed();
        finish();
        Intent i = new Intent(LeaveRequest.this, DashBoradActivity.class);
        startActivity(i);
        super.onBackPressed();
    }

}
