package com.dm.crmdm_app;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.controller.EnviroController;
import com.dm.controller.SmanController;
import com.dm.controller.TransLeaveController;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.Custom_Toast;
import com.dm.library.DbCon;
import com.dm.library.ExceptionData;
import com.dm.library.IntentSend;
import com.dm.model.AppData;
import com.dm.model.Sman;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AprovalLiveDetails extends AppCompatActivity {
    TextView appDate, appBy, reportTo, spinner_text_transportModeOnDsr, to_text_Spinner, from_text_Spinner;
    EditText noOfDays, fromDate, toDate, reason, ramark;
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
    String currentDate, Level, server, Days, FromDate, ToDate, Reason, AppDate, UserId, SmId, LeaveFlag, AppStatus, LVRQId, Mob;
    ArrayList<String> leaveArrayList, toLeaveArrayList;
    EnviroController enviroController;
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
    String USER_ID_SESSION;
    String status, remark,parent_smid;
    RadioGroup ag;
    RadioButton radioButton;
    private Button btnDisplay;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    DbCon dbCon;
    String imc_met,Spinner_id;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aproval_live_details);
        dbCon=new DbCon(getApplicationContext());

        preferences2 = this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
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
        appDataController1=new AppDataController(AprovalLiveDetails.this);
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();

        TextView tv = (TextView)findViewById(R.id.text);
        tv.setText("Leave Approval Entry");
        setTitle("Leave Approval Entry");
        appDate=(TextView)findViewById(R.id.applicationDate);
        appBy=(TextView)findViewById(R.id.appliedBy);
        reportTo=(TextView)findViewById(R.id.ReportTo);
        noOfDays = (EditText) findViewById(R.id.noOfDays);
        noOfDays.setGravity(Gravity.TOP| Gravity.RIGHT);
        fromDate = (EditText) findViewById(R.id.fromDate);
        toDate = (EditText) findViewById(R.id.toDateEdit);
        reason = (EditText) findViewById(R.id.reasonOfleave);
        fromSpinner = (Spinner) findViewById(R.id.fromSpinner);
        toSpinner = (Spinner) findViewById(R.id.toSpinner);
        salesManSpinner = (Spinner) findViewById(R.id.salesPerson);
        save = (ImageView) findViewById(R.id.save);
        cancel = (ImageView) findViewById(R.id.cancel);
        delete = (ImageView) findViewById(R.id.delete);
        delete.setVisibility(View.GONE);
        to_text_Spinner = (TextView) findViewById(R.id.to_text_Spinner);
        from_text_Spinner = (TextView) findViewById(R.id.from_text_Spinner);
        to_text_Spinner.setVisibility(View.VISIBLE);
        from_text_Spinner.setVisibility(View.VISIBLE);
        fromLinerSpinner = (LinearLayout) findViewById(R.id.fromLinerSpinner);
        toLinerSpinner = (LinearLayout) findViewById(R.id.toLinerSpinner);
        ramark = (EditText) findViewById(R.id.editText7);

        connectionDetector=new ConnectionDetector(getApplicationContext());
        ag = (RadioGroup) findViewById(R.id.approvalGroup);
        //radioButton = ((RadioButton) findViewById(ag.getCheckedRadioButtonId())).getText().toString();

        //Toast.makeText(AprovalLiveDetails.this,radioButton.getText(),Toast.LENGTH_SHORT).show();


        transLeaveController = new TransLeaveController(AprovalLiveDetails.this);
        leaveArrayList = new ArrayList<String>();
        leaveArrayList.add("Select");
        leaveArrayList.add("First Half");
        leaveArrayList.add("Second Half");
        leaveArrayList.add("Full");
        leaveAdapter = new ArrayAdapter<String>(AprovalLiveDetails.this, android.R.layout.simple_list_item_1, leaveArrayList);
        fromSpinner.setAdapter(leaveAdapter);
        toLeaveArrayList = new ArrayList<String>();
        toLeaveArrayList.add("Select");
        toLeaveArrayList.add("First Half");
        toLeaveArrayList.add("Second Half");
        toLeaveArrayList.add("Full");
        toLeaveAdapter = new ArrayAdapter<String>(AprovalLiveDetails.this, android.R.layout.simple_list_item_1, toLeaveArrayList);
        toSpinner.setAdapter(toLeaveAdapter);
//		fromSpinner.setVisibility(View.GONE);
//		toSpinner.setVisibility(View.GONE);

        fromDate.setInputType(InputType.TYPE_NULL);
        toDate.setInputType(InputType.TYPE_NULL);
        noOfDays.setInputType(InputType.TYPE_NULL);
        reason.setInputType(InputType.TYPE_NULL);
        noOfDays.setFocusable(false);
        fromDate.setFocusable(false);
        toDate.setFocusable(false);
        reason.setFocusable(false);
        //fromDate.setFocusable(false);
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

        update = (ImageView) findViewById(R.id.update);
        update.setVisibility(View.GONE);
        cancel.setVisibility(View.VISIBLE);
        //find.setVisibility(View.VISIBLE);
       // server = Constant.SERVER_WEBSERVICE_URL;
        spinner_text_transportModeOnDsr = (TextView) findViewById(R.id.spinner_text_transportModeOnDsr);
        traDSR = (LinearLayout) findViewById(R.id.traDSR);
        //getLevel();
        //SmId=preferences2.getString("CONPERID_SESSION", null);
        SmId = preferences2.getString("CONPERID_SESSION", null);
        UserId = preferences2.getString("USER_ID", null);
        preferences3 = this.getSharedPreferences("Update_Leave", Context.MODE_PRIVATE);
        Intent ds = getIntent();
        if (getIntent().getExtras() != null) {
            String Data = ds.getStringExtra("Data").toString();
            USER_ID_SESSION = ds.getStringExtra("LEAVE_USER_ID_SESSION").toString();
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
                String aproval = ds.getStringExtra("aproval");
                String App_remark = ds.getStringExtra("App_remark");
                String getStatusForNotification = ds.getStringExtra("AppStatus");
                noOfDays.setText(Days);
                try {
                    if (!App_remark.equals("")) {
                        update.setEnabled(false);
                        update.setClickable(false);
                        delete.setEnabled(false);
                        delete.setClickable(false);
                        update.setColorFilter(Color.parseColor("#888888"));
                        delete.setColorFilter(Color.parseColor("#888888"));
                        ramark.setText(App_remark);
                        ramark.setClickable(false);
                        ramark.setEnabled(false);
                        RadioButton rb = (RadioButton) findViewById(R.id.reject);
                        RadioButton rb1 = (RadioButton) findViewById(R.id.approve);
                        if (getStatusForNotification.equals("Reject")) {
                            rb.setChecked(true);
                            rb1.setEnabled(false);
                            rb1.setClickable(false);
                        } else {
                            rb1.setChecked(true);
                            rb.setEnabled(false);
                            rb.setClickable(false);
                        }
                        if (aproval.equalsIgnoreCase("true")) {
                            tv.setText("Leave Request");
                            update.setEnabled(false);
                            update.setClickable(false);
                            delete.setEnabled(false);
                            delete.setClickable(false);
                            update.setColorFilter(Color.parseColor("#888888"));
                            // 20 june 17
                            cancel.setVisibility(View.GONE);
                            //////////////
                            delete.setColorFilter(Color.parseColor("#888888"));
                        }
                    }
                }
                catch (Exception e){}

                    if (AppStatus.equalsIgnoreCase("Approve"))
                    {
                        update.setEnabled(false);
                        update.setClickable(false);
                        delete.setEnabled(false);
                        delete.setClickable(false);
                        update.setColorFilter(Color.parseColor("#888888"));
                        delete.setColorFilter(Color.parseColor("#888888"));
                    }

                /***************************** Ashutosh (08/04/17) start ***********************************/
                        if (USER_ID_SESSION.equalsIgnoreCase(SmId)) {
                            ramark.setClickable(false);
                            ramark.setEnabled(false);
                            update.setEnabled(false);
                            update.setClickable(false);
                            delete.setEnabled(false);
                            delete.setClickable(false);
                            update.setColorFilter(Color.parseColor("#888888"));
                            delete.setColorFilter(Color.parseColor("#888888"));
                        }
                /***************************** Ashutosh (08/04/17) end ***********************************/


                //String LEAVE_USER_ID_SESSION=ds.getStringExtra("LEAVE_USER_ID_SESSION");
                //String kamall=LEAVE_USER_ID_SESSION.split("'*'")[0];
                //salesManMaterial.setSelection(Integer.parseInt(kamall));
               // salesManMaterial.setSelection(Integer.parseInt(ds.getStringExtra("DropId")));

               // salesManMaterial.setClickable(false);
                  salesManMaterial.setEnabled(false);
                fromSpinner.setEnabled(false);
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
                //delete.setVisibility(View.VISIBLE);
                //cancel.setVisibility(View.GONE);
                if (noOfDays.getText().toString().equals("0.5") || noOfDays.getText().toString().equals("0.50")) {
                    if (LeaveFlag.equals("F")) {
                        fromSpinner.setSelection(2);
                        toSpinner.setVisibility(View.GONE);
                    } else {
                        fromSpinner.setSelection(2);
                        toSpinner.setVisibility(View.GONE);
                    }
                } else if (Double.parseDouble(noOfDays.getText().toString()) == 1.0) {
                    if (LeaveFlag.equals("S")) {
                        fromSpinner.setSelection(2);
                        toSpinner.setVisibility(View.VISIBLE);
                        toSpinner.setSelection(1);
                    } else if (LeaveFlag.equals("C")) {
                        fromSpinner.setSelection(3);
                        toSpinner.setVisibility(View.VISIBLE);
                        toSpinner.setSelection(3);
                    }
                } else if (Double.parseDouble(noOfDays.getText().toString()) > 1.0) {

                    if (LeaveFlag.equals("S")) {
                        fromSpinner.setSelection(2);
                        toSpinner.setVisibility(View.VISIBLE);
//			   toSpinner.setSelection(3);
                    } else if (LeaveFlag.equals("C")) {
                        fromSpinner.setSelection(3);
                        toSpinner.setVisibility(View.VISIBLE);
//			   toSpinner.setSelection(2);

                    }
                }
            }

        }
        dbCon.open();
        //save.setEnabled(dbCon.ButtonEnable("LeaveRequest","Party","Add"));
        if(!dbCon.ButtonEnable("LeaveApproval","Plan","Add"))
        {
            save.setColorFilter(Color.parseColor("#808080"));
            save.setEnabled(false);
        }
        if(!dbCon.ButtonEnable("LeaveApproval","Plan","Edit"))
        {
            update.setColorFilter(Color.parseColor("#808080"));
            update.setEnabled(false);
        }
        if(!dbCon.ButtonEnable("LeaveApproval","Plan","Delete"))
        {
            delete.setColorFilter(Color.parseColor("#808080"));
            delete.setEnabled(false);
        }
        dbCon.close();

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(AprovalLiveDetails.this)
                        //.setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String jjk = salesManNameList2.get((int) salesManMaterial.getSelectedItemId());
                                SmId = jjk;
                                //SmId=preferences2.getString("CONPERID_SESSION", null);
                                if (connectionDetector.isConnectingToInternet())
                                {
                                    new deleteLeaveRequest().execute();
                                }
                                else{
                                    dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                                    dialogWithOutView.show(getFragmentManager(), "Info");
                                }

                                //(new IntentSend(getApplicationContext(),DashBoradActivity.class)).toSendAcivity();
                                //new Custom_Toast(getApplicationContext(), "Record Successfully Delete").showCustomAlert();
                                clearFields();
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
        update.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                int selectedId = ag.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                if (noOfDays.getText().toString().equals("")) {
                    //if(!Level.equalsIgnoreCase("L1")){
                    new Custom_Toast(getApplicationContext(), "Please enter No. of days").showCustomAlert();
                    //}
                } else if (fromDate.getText().toString().equals("")) {
                    new Custom_Toast(getApplicationContext(), "Please select From Date").showCustomAlert();
                } else if (fromSpinner.getSelectedItem().equals("Select")) {
                    new Custom_Toast(getApplicationContext(), "Please select First/Second Half or Full").showCustomAlert();
                } else if (reason.getText().toString().equals("")) {
                    new Custom_Toast(getApplicationContext(), "Please enter Reason").showCustomAlert();
                } else if (ramark.getText().toString().equals("")) {
                    new Custom_Toast(getApplicationContext(), "Please enter Remark").showCustomAlert();
                } else {
                   /* String name = noOfDays.getText().toString();
                    double name1 = Double.parseDouble(name);
                    String[] arr = String.valueOf(name1).split("\\.");
                    int[] tem = new int[10];
                    tem[0] = Integer.parseInt(arr[0]); // 1
                    tem[1] = Integer.parseInt(arr[1]);
                    String par = String.valueOf(tem[0]);
                    String pa = String.valueOf(tem[1]);
                    String pa1 = ".5";
                    pa = pa1;
                    String par1 = par + pa;
                    noOfDays.setText(par1);
                    //Toast.makeText(getApplicationContext(),par1, Toast.LENGTH_SHORT).show();*/

                    String par1 = null;
                    String pa1 = null;
                    try {
                        String name = noOfDays.getText().toString();
                        double name1 = Double.parseDouble(name);
                        String[] arr = String.valueOf(name1).split("\\.");
                        int[] tem = new int[10];
                        tem[0] = Integer.parseInt(arr[0]); // 1
                        tem[1] = Integer.parseInt(arr[1]);
                        String par = String.valueOf(tem[0]);
                        String pa = String.valueOf(tem[1]);
                        if (pa.equalsIgnoreCase("0")) {
                            par1 = par;
                            //noOfDays.setText(par1);
                        } else if (!pa.equalsIgnoreCase("5")) {
                            pa1 = ".5";
                            pa = pa1;
                            par1 = par + pa;
                            noOfDays.setText(par1);
                            fromSpinner.setSelection(0);
                            //toSpinner.setSelection(0);
                        }
                    } catch (Exception e) {
                        Log.e("App", "yourDataTask", e);
                    }

                    Days = noOfDays.getText().toString();
                    FromDate = fromDate.getText().toString();
                    ToDate = toDate.getText().toString();
                    Reason = reason.getText().toString();
                    //Appdat=appDate.getText().toString();
                    UserId = preferences2.getString("USER_ID", null);

                    String jjk = salesManNameList2.get((int) salesManMaterial.getSelectedItemId());

                    //SmId=preferences2.getString("CONPERID_SESSION", null);
                    SmId = jjk;
                    if (fromSpinner.getSelectedItem().toString().equals("Full")) {
                        LeaveFlag = "C";
                    } else if (fromSpinner.getSelectedItem().toString().equals("First Half")) {
                        LeaveFlag = "F";
                    } else if (fromSpinner.getSelectedItem().toString().equals("Second Half")) {
                        LeaveFlag = "S";
                    }
                    if (connectionDetector.isConnectingToInternet())
                    {
                        new editLeaveRequest().execute();
                    }
                    else{
                        dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                        dialogWithOutView.show(getFragmentManager(), "Info");
                    }

                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                try {
                    Intent ds = getIntent();
                    if (getIntent().getExtras() != null) {
                        String Data = ds.getStringExtra("Data").toString();
                        USER_ID_SESSION = ds.getStringExtra("LEAVE_USER_ID_SESSION").toString();
                        Log.i("Data", Data);
                        if (!Data.equals(null)) {
                            String aproval = ds.getStringExtra("aproval");

                            if (aproval.equals("true")) {
                                Intent i = new Intent(AprovalLiveDetails.this, LeaveRequest.class);
                                startActivity(i);
                            }
                        }
                    }
                }
                catch (Exception e){
                    Intent i = new Intent(AprovalLiveDetails.this, LeaveApproval.class);
                    startActivity(i);
                }
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

        View parent = findViewById(R.id.parent);
        fromDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    //hideKeyboard(v);
                    //showDatePicker(v.getId());
                } else {
                    hideKeyboard(v);
                    //showDatePicker(v.getId());
                    fromSpinner.setSelection(2);
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


        salesManMaterial.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String jk = spinner_text_transportModeOnDsr.getText().toString();

                if (!jk.equalsIgnoreCase("")) {
                    Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    spinner_text_transportModeOnDsr.setVisibility(View.VISIBLE);
                    spinner_text_transportModeOnDsr.startAnimation(slide_up);
                    spinner_text_transportModeOnDsr.setTextColor(Color.parseColor("#FF4081"));
                    traDSR.setBackgroundColor(Color.parseColor("#FF4081"));
                }
                spinner_text_transportModeOnDsr.setText("Sales Person: *");
                imc_met=salesManMaterial.getSelectedItem().toString();
                //Toast.makeText(getApplicationContext(),arg2, Toast.LENGTH_SHORT).show();
                Spinner_id=salesManNameList2.get((int) salesManMaterial.getSelectedItemId());
                appBy.setText(imc_met);
                new getReport().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                spinner_text_transportModeOnDsr.setVisibility(View.GONE);
            }
        });

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String jk = to_text_Spinner.getText().toString();
                if (!jk.equalsIgnoreCase("")) {
                    Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    to_text_Spinner.setVisibility(View.VISIBLE);
                    to_text_Spinner.startAnimation(slide_up);
                    //to_text_Spinner.setTextColor(Color.parseColor("#FF4081"));
                    //toLinerSpinner.setBackgroundColor(Color.parseColor("#FF4081"));
                }
                to_text_Spinner.setText("To Leave Time: *");
                // String imc_met=spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                to_text_Spinner.setVisibility(View.GONE);
            }
        });

        //appBy.setText(salesManNameList.get(0));
//		reportTo.setText(smanList.get(0).getDisplayName());

        noOfDays.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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
                    if(pa.equalsIgnoreCase("0")){
                        par1 = par ;
                    }
                    else {
                        String pa1 = ".5";
                        pa = pa1;
                        par1 = par + pa;
                    }
                    //Toast.makeText(getApplicationContext(),pa, Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Log.e("App", "yourDataTask", e);
                }
                try{

                    days = par1;
                    String[] words=days.split("\\.");//splits the string based on string
                    if(words.length==2)
                    {
                        if(Integer.parseInt(words[1])>0)
                        {
                            fromSpinner.setVisibility(View.VISIBLE);
                            if(words[1].equals("5") && words[0].equals("0"))
                            {
                                leaveArrayList.clear();
                                leaveArrayList.add("--Select--");
                                leaveArrayList.add("First Half");
                                leaveArrayList.add("Second Half");
                                leaveAdapter = new ArrayAdapter<String>(AprovalLiveDetails.this,  R.layout.adapterdropdown, leaveArrayList);
                                leaveAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                                fromSpinner.setAdapter(leaveAdapter);
                                fromSpinner.setVisibility(View.VISIBLE);
                                toLinerSpinner.setVisibility(View.VISIBLE);
                                fromLinerSpinner.setVisibility(View.VISIBLE);
                                toSpinner.setVisibility(View.VISIBLE);
                                to_text_Spinner.setVisibility(View.VISIBLE);
                                //toSpinner.setEnabled(false);
                                toSpinner.setClickable(false);
                            }
                            else
                            {
//							 leaveArrayList.remove(1);
                                leaveArrayList.clear();
                                leaveArrayList.add("--Select--");
                                leaveArrayList.add("Second Half");
                                leaveArrayList.add("Full");
                                leaveAdapter = new ArrayAdapter<String>(AprovalLiveDetails.this, R.layout.adapterdropdown, leaveArrayList);
                                leaveAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                                fromSpinner.setAdapter(leaveAdapter);
                                fromSpinner.setVisibility(View.VISIBLE);
                                toSpinner.setVisibility(View.VISIBLE);
                                toLinerSpinner.setVisibility(View.VISIBLE);
                                fromLinerSpinner.setVisibility(View.VISIBLE);
                                to_text_Spinner.setVisibility(View.VISIBLE);
                                //toSpinner.setEnabled(false);
                                toSpinner.setClickable(false);
                            }
                        }
                        else
                        {

                            fromSpinner.setVisibility(View.VISIBLE);
                            toSpinner.setVisibility(View.VISIBLE);
                            toLinerSpinner.setVisibility(View.VISIBLE);
                            fromLinerSpinner.setVisibility(View.VISIBLE);
                            leaveArrayList.clear();
                            leaveArrayList.add("--Select--");
                            leaveArrayList.add("Second Half");
                            leaveArrayList.add("Full");
                            leaveAdapter=new ArrayAdapter<String>(AprovalLiveDetails.this, R.layout.adapterdropdown,leaveArrayList);
                            leaveAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            fromSpinner.setAdapter(leaveAdapter);
                            //toSpinner.setEnabled(false);
                            toSpinner.setClickable(false);
                        }

                    }
                    else{
                        if (Integer.parseInt(days)==1)
                        {
                            fromSpinner.setVisibility(View.VISIBLE);
                            toSpinner.setVisibility(View.VISIBLE);
                            toLinerSpinner.setVisibility(View.VISIBLE);
                            fromLinerSpinner.setVisibility(View.VISIBLE);
                            leaveArrayList.clear();
                            leaveArrayList.add("--Select--");
                            leaveArrayList.add("Second Half");
                            leaveArrayList.add("Full");
                            leaveAdapter=new ArrayAdapter<String>(AprovalLiveDetails.this, R.layout.adapterdropdown,leaveArrayList);
                            leaveAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            fromSpinner.setAdapter(leaveAdapter);
                            //toSpinner.setEnabled(false);
                            toSpinner.setClickable(false);
                        }
                        else
                        {
                            leaveArrayList.clear();
                            leaveArrayList.add("--Select--");
                            leaveArrayList.add("Second Half");
                            leaveArrayList.add("Full");
                            leaveAdapter=new ArrayAdapter<String>(AprovalLiveDetails.this, R.layout.adapterdropdown,leaveArrayList);
                            leaveAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                            fromSpinner.setAdapter(leaveAdapter);
                            fromSpinner.setVisibility(View.VISIBLE);
                            toSpinner.setVisibility(View.VISIBLE);
                            toLinerSpinner.setVisibility(View.VISIBLE);
                            fromLinerSpinner.setVisibility(View.VISIBLE);
                            //toSpinner.setEnabled(false);
                            toSpinner.setClickable(false);
                        }
                    }
                }catch(Exception e)
                {
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
        fromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // showDatePicker(v.getId());
            }
        });


        find.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                (new IntentSend(getApplicationContext(), FindLeave.class)).toSendAcivity();
            }
        });


        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3)
            {
                s1 = (String) fromSpinner.getSelectedItem();
                String jk=from_text_Spinner.getText().toString();
                if(!jk.equalsIgnoreCase("")){
                    Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
                    from_text_Spinner.setVisibility(View.VISIBLE);
                    fromSpinner.setVisibility(View.VISIBLE);
                    fromLinerSpinner.setVisibility(View.VISIBLE);
                    from_text_Spinner.startAnimation(slide_up);
                    //from_text_Spinner.setTextColor(Color.parseColor("#FF4081"));
                   // fromLinerSpinner.setBackgroundColor(Color.parseColor("#FF4081"));
                }
                from_text_Spinner.setText("From Leave Time: *");

                Intent ds=getIntent();
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
                        /*******************  Ashutosh *******************************/
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
                                if(getIntent().getExtras() != null) {
                                    String Data = ds.getStringExtra("Data").toString();
                                    Log.i("Data", Data);
                                    if (!Data.equals(null)) {
                                    }
                                }
                                else {
                                    toSpinner.setSelection(3);
//		        					new Custom_Toast(getApplicationContext(), "Please select From Date First").showCustomAlert();
                                }
                            }
                            else{
                                Date tdate1=df.parse(fromDate.getText().toString());
                                Calendar c1=Calendar.getInstance();
                                c1.setTime(tdate1);
                                toDate.setText(df.format(c1.getTime()));
                                toSpinner.setSelection(3);
                                toSpinner.setVisibility(View.GONE);
                                toLinerSpinner.setVisibility(View.GONE);
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
                    else if(noOfDays.getText().toString().equals(".000")|| noOfDays.getText().toString().equals("000.")||noOfDays.getText().toString().equals(".0")|| noOfDays.getText().toString().equals("-0.")|| noOfDays.getText().toString().equals("-0")|| noOfDays.getText().toString().equals("-.0")|| noOfDays.getText().toString().equals("-")|| noOfDays.getText().toString().equals("0.") || noOfDays.getText().toString().equals("0") || noOfDays.getText().toString().equals("0.0") || noOfDays.getText().toString().equals("0.00") ||noOfDays.getText().toString().equals("")|| noOfDays.getText().toString().equals(null)){
                        toSpinner.setVisibility(View.GONE);
                        to_text_Spinner.setVisibility(View.GONE);
                        toLinerSpinner.setVisibility(View.GONE);
                        fromSpinner.setVisibility(View.GONE);
                        from_text_Spinner.setVisibility(View.GONE);
                        fromLinerSpinner.setVisibility(View.GONE);
                    }
                    else if(Double.parseDouble(noOfDays.getText().toString())==0.5 || Double.parseDouble(noOfDays.getText().toString())==0.05)
                    {
                        if(fromSpinner.getSelectedItem().equals("--Select--"))
                        {
							/*toSpinner.setSelection(0);
							//toSpinner.setEnabled(false);
							toSpinner.setClickable(false);*/
                            toSpinner.setVisibility(View.GONE);
                            to_text_Spinner.setVisibility(View.GONE);
                            toLinerSpinner.setVisibility(View.GONE);
                        }
                        else if (fromSpinner.getSelectedItem().equals("First Half"))
                        {
							/*toSpinner.setSelection(1);
							toSpinner.setClickable(false);*/
                            toSpinner.setVisibility(View.GONE);
                            to_text_Spinner.setVisibility(View.GONE);
                            toLinerSpinner.setVisibility(View.GONE);
                        }
                        else
                        {
							/*toSpinner.setSelection(2);
							toSpinner.setClickable(false);*/
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
                                    if(getIntent().getExtras() != null) {
                                        String Data = ds.getStringExtra("Data").toString();
                                        Log.i("Data", Data);
                                        if (!Data.equals(null)) {

                                            if(str1[1].equals("00") || str1[1].equals("")) {
                                                toSpinner.setSelection(1);
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
                                                    c1.add(Calendar.DATE,1);
                                                    toDate.setText(df.format(c1.getTime()));
                                                }
                                            }
                                            else{
                                                toSpinner.setSelection(3);
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
                                                    //c1.add(Calendar.DATE,(int)num);
                                                    toDate.setText(df.format(c1.getTime()));
                                                }
                                            }
                                        }
                                    }
                                    else {
                                        toSpinner.setSelection(1);
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
                                            c1.add(Calendar.DATE,1);
                                            toDate.setText(df.format(c1.getTime()));
                                        }
                                    }

                                }
                                else if (fromSpinner.getSelectedItem().equals("Full"))
                                {
                                    if(getIntent().getExtras() != null) {
                                        String Data = ds.getStringExtra("Data").toString();
                                        Log.i("Data", Data);
                                        if (!Data.equals(null)) {
                                            if(str1[1].equals("00") || str1[1].equals("")) {
                                                toSpinner.setSelection(3);
                                                toSpinner.setClickable(false);
                                            }
                                            else {
                                                toSpinner.setSelection(1);
                                                toSpinner.setClickable(false);
                                            }
                                        }
                                    }
                                    else {
                                        toSpinner.setSelection(1);
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
                                if(getIntent().getExtras() != null) {
                                    String Data = ds.getStringExtra("Data").toString();
                                    Log.i("Data", Data);
                                    if (!Data.equals(null)) {
                                        if(Double.parseDouble(str1[1]) == 00 || str1[1].equals("")) {
                                            toSpinner.setSelection(1);
                                            toSpinner.setClickable(false);
                                        }
                                        else {
                                            toSpinner.setSelection(3);
                                            toSpinner.setClickable(false);
                                        }
                                    }
                                }
                                else {
                                    toSpinner.setSelection(3);
                                    toSpinner.setClickable(false);
                                }

                            }
                        }
                    }

                }catch(Exception e)
                {
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
    }
    protected class getReport extends AsyncTask<Void, Void, JSONArray>
    {
        ProgressDialog pdLoading = new ProgressDialog(AprovalLiveDetails.this);
        @Override
        protected void onPreExecute() {
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }

        @Override
        protected JSONArray doInBackground(Void... params)
        {
            //server= Constant.SERVER_WEBSERVICE_URL;
            String MOb = preferences2.getString("PDAID_SESSION", "");
            //String str="http://" + server + "/And_Sync.asmx/JSGetUserDetailByPDAId?PDA_Id=" + MOb + "&minDate=0";
            String str="http://" + server + "/And_Sync.asmx/xJSFindLeaveUserReportingWise?SmId="+Spinner_id;
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
                JSONArray response = new JSONArray(stringBuffer.toString());
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
                ArrayList<String> salesManNameList=new ArrayList<String>();
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
            if(pdLoading != null)
            pdLoading.dismiss();
        }
    }

    protected class getLevel extends AsyncTask<Void, Void, JSONArray> {
        @Override
        protected JSONArray doInBackground(Void... params) {
           // server = Constant.SERVER_WEBSERVICE_URL;
            String MOb = preferences2.getString("PDAID_SESSION", "");
            //String str="http://" + server + "/And_Sync.asmx/JSGetUnderUser?PDA_Id=" + MOb + "&minDate=0&Smid="+SmId;
            String str = "http://" + server + "/And_Sync.asmx/xJSGetUserForApproval?Smid=" + SmId;
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
                try {
                    ArrayList<String> salesManNameList = new ArrayList<String>();
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject objs = null;
                        objs = response.getJSONObject(i);
                        salesManNameList.add(objs.getString("snm").toString());
                        salesManNameList2.add(objs.getString("sid").toString());
                    }
                    //Level = response.getJSONObject(response.length()-1).getString("RoleName").toString();
                    smanAdapter = new ArrayAdapter<String>(AprovalLiveDetails.this, android.R.layout.simple_list_item_1, salesManNameList);
                    //salesManSpinner.setAdapter(smanAdapter);
                    salesManMaterial.setAdapter(smanAdapter);

                    Intent ds = getIntent();
                    if (getIntent().getExtras() != null) {
                        String Data = ds.getStringExtra("Data").toString();
                        String Dropid = ds.getStringExtra("DropId").toString();
                        Log.i("Data", Data);
                        if (!Data.equals(null)) {
                            //salesManMaterial.setSelection(Integer.parseInt(Dropid));
                            salesManMaterial.setSelection(salesManNameList2.indexOf(Dropid));
                        } else {
                            salesManMaterial.setSelection(0);
                        }
                    }
                    else {
                        salesManMaterial.setSelection(salesManNameList2.indexOf(SmId));
                    }
                    salesManMaterial.setClickable(false);
                    salesManMaterial.setEnabled(false);

                   /* if(response.length()==1){
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
        }
    }

    private void hideKeyboard(View view) {
		/*InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);*/

        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(fromDate.getWindowToken(), 0);
    }

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
            System.out.println(e);
        }

    }
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
        salesManMaterial.setClickable(true);
        salesManMaterial.setEnabled(true);
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
            //server = Constant.SERVER_WEBSERVICE_URL;
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

    public class editLeaveRequest extends AsyncTask<Void, Void, JSONArray> {
        ProgressDialog pdLoading = new ProgressDialog(AprovalLiveDetails.this);
        String query = null, query1 = null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            remark = ramark.getText().toString();
            status = radioButton.getText().toString();
            parent_smid = preferences2.getString("CONPERID_SESSION", null);
            UserId = preferences2.getString("USER_ID", null);
           pdLoading.setMessage("\tLoading...");
            pdLoading.show();

        }
        @Override
        protected JSONArray doInBackground(Void... params) {
            try {
                query = URLEncoder.encode(remark, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String str = "http://" + server + "/And_Sync.asmx/xJSInsertLeaveApproval?Smid=" + USER_ID_SESSION + "&LVQRid=" + LVRQId +
                    "&senior_userid=" + UserId + "&status=" + status + "&remark=" + query + "&senior_smid=" + parent_smid;
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
                String st = null;
                ArrayList<String> salesManNameList = new ArrayList<String>();
                for (int i = 0; i < response.length(); i++) {
                    JSONObject objs = null;
                    try {
                        objs = response.getJSONObject(i);
                         st = (objs.getString("st").toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(st.equalsIgnoreCase("1"))
                {
                    new Custom_Toast(getApplicationContext(), "Record Update Successfully").showCustomAlert();
                    Intent obj = new Intent(AprovalLiveDetails.this,LeaveApproval.class);
                    startActivity(obj);
                    finish();
                }
                else {
                    new Custom_Toast(getApplicationContext(), "Record Not Update ").showCustomAlert();
                }
            }
            super.onPostExecute(response);
            pdLoading.dismiss();
        }
    }
    /*private void colorReset() {
        textView1.setTextColor(Color.parseColor("#FF808080"));
        l1.setBackgroundColor(Color.parseColor("#666666"));
        textView4.setTextColor(Color.parseColor("#FF808080"));
        l4.setBackgroundColor(Color.parseColor("#666666"));
        textView2.setTextColor(Color.parseColor("#FF808080"));
        l2.setBackgroundColor(Color.parseColor("#666666"));
        textView3.setTextColor(Color.parseColor("#FF808080"));
        l3.setBackgroundColor(Color.parseColor("#666666"));
        textView5.setTextColor(Color.parseColor("#FF808080"));
        l5.setBackgroundColor(Color.parseColor("#666666"));
        textView6.setTextColor(Color.parseColor("#FF808080"));
        l6.setBackgroundColor(Color.parseColor("#666666"));
        l7.setBackgroundColor(Color.parseColor("#666666"));
        textView7.setTextColor(Color.parseColor("#FF808080"));
        textView8.setTextColor(Color.parseColor("#FF808080"));
        l8.setBackgroundColor(Color.parseColor("#666666"));
        textView9.setTextColor(Color.parseColor("#FF808080"));
        //l9.setBackgroundColor(Color.parseColor("#FF4081"));
        l9.setBackgroundColor(Color.parseColor("#666666"));
    }

    //Color Active
    public void colorRed(LinearLayout linear,TextView tv){
        tv.setTextColor(Color.parseColor("#FF4081"));
        linear.setBackgroundColor(Color.parseColor("#FF4081"));
    }
   /* protected class editLeaveRequest extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            /*Intent ds=getIntent();
            if(getIntent().getExtras() != null) {
                String Data = ds.getStringExtra("Data").toString();
                Log.i("Data", Data);
                if (!Data.equals(null)) {
                    USER_ID_SESSION =ds.getStringExtra("LEAVE_USER_ID_SESSION");
                    UserId, SmId
                }
            }*/

    /*        remark = ramark.getText().toString();
            status = radioButton.getText().toString();
        }

        @Override
        protected String doInBackground(Void... params) {
            Reason = Reason.replace(" ", "%20");
            server = Constant.SERVER_WEBSERVICE_URL;
            //String MOb = preferences2.getString("PDAID_SESSION", "");+"&SMId=" +SmId+ "&LeaveFlag="+LeaveFlag+"&LVRQId="+LVRQId
            String str = "http://" + server + "/And_Sync.asmx/xJSInsertLeaveApproval?Smid=" + USER_ID_SESSION + "&LVQRid=" + LVRQId +
                    "&senior_userid=" + UserId + "&status=" + status + "&remark=" + remark + "&senior_smid=" + SmId;
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
        protected void onPostExecute(String stringBuffer)
        {
            if(stringBuffer != null)
            {
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject objs = null;
                        objs = response.getJSONObject(i);
                        salesManNameList.add(objs.getString("snm").toString());
                    }

                } catch (JSONException ex) {
                    Log.e("App", "Failure", ex);
                }
                if(stringBuffer.equalsIgnoreCase(""))
                new Custom_Toast(getApplicationContext(), stringBuffer).showCustomAlert();

            }
        }
    }*/
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        /*preferences3 = null;
        //super.onBackPressed();
        (new IntentSend(getApplicationContext(), LeaveApproval.class)).toSendAcivity();
        finish();*/
        //System.exit(0);

        try {
            finish();
          /*  Intent ds = getIntent();
            if (getIntent().getExtras() != null) {
                String Data = ds.getStringExtra("Data").toString();
                USER_ID_SESSION = ds.getStringExtra("LEAVE_USER_ID_SESSION").toString();
                Log.i("Data", Data);
                if (!Data.equals(null)) {
                    String aproval = ds.getStringExtra("aproval");

                    if (aproval.equals("true")) {
                        Intent i = new Intent(AprovalLiveDetails.this, LeaveRequest.class);
                        startActivity(i);
                        finish();
                    }
                }
            }*/
        }
        catch (Exception e){
          /*  Intent i = new Intent(AprovalLiveDetails.this, LeaveApproval.class);
            startActivity(i);
            finish();*/
        }
    }
}
