package com.dm.crmdm_app;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.dm.controller.AppDataController;
import com.dm.controller.DeviceInfoController;
import com.dm.library.AlertMessage;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomArrayAdopter;
import com.dm.library.Custom_Toast;
import com.dm.library.DataTransferInterface;
import com.dm.library.DateAndTimePicker;
import com.dm.library.DateFunction;
import com.dm.library.DatePickerFragmentRange;
import com.dm.library.DbCon;
import com.dm.library.IntentSend;
import com.dm.model.AppData;
import com.dm.model.City;
import com.dm.model.DashboardModel;
import com.dm.model.Expense;
import com.dm.model.ExpenseEntry;
import com.dm.model.State;
import com.dm.model.Vehicle;
import com.dm.parser.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExpenseSummaryScreen extends AppCompatActivity implements DataTransferInterface, CustomArrayAdopter.HolderListener, AlertMessage.NoticeDialogListenerWithoutView, CustomArrayAdopter.ExpenseTransferInterface {
    ListView listView;
    ImageView submit, cancel, clear, goExpGrp;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    String server, smid, userId;
    boolean mSuccess = false;
    ArrayList<Expense> expenseTypeList;
    Spinner expTypeSpinner;
    ArrayAdapter<String> expTypeAdapter;
    boolean allowToChangeCity = false;
    EditText billDate, billNo, amt, camt, gstNoText, remark, fromDate, toDate;
    Spinner tostateSpinner, stateSpinner, tocitySpinner, citySpinner, conveyanseSpinner, fromTimeSpinner, toTimeSpinner;
    AlertOkDialog alertOkDialog;
    ArrayList<State> stateList, tostateList;
    ArrayList<City> cityList, toCityList;
    ArrayList<String> cityArrayList, tocityArrayList, stateArrayList, tostateArrayList, conveyanceArrayList;
    String stId = "0", tostId = "0";
    ArrayAdapter<String> stateAdapter, tostateAdapter, cityAdapter, tocityAdapter, conveyanceAdapter, timeAdapter;
    Intent intent, intent1;
    Bundle bundle, bundle1;
    TextView expGrpName;
    String fDate, tDate, grpName;
    int grpId = 0, expDetailId = 0;
    ExpenseEntry expenseEntry;
    ArrayList<String> expenseTypeArrayList;
    String expTypeCode = "0", expTypeId = "0", expTypeName = "";
    SharedPreferences preferences1;
    CheckBox supportAttach, supportGSTINCheckBox, zeroAmount, requiredGstInCheckbox, stayWithRelative;
    int EmpGracePeriod = 0;
    boolean mode = false;
    ArrayList<ExpenseEntry> expenseEntryList;
    CustomArrayAdopter customArrayAdopter;
    Button addExp;
    ExpenseEntry expenseEntryObj = null;
    LinearLayout conveyanseLayout, lodgingLayout;
    ArrayList<Vehicle> conveyanceList1;
    ArrayList<String> timeArrayList;
    SharedPreferences preferences;
    String comeFrom = "", delResp = "";
    boolean FromUpdate = false;
    AlertOkDialog dialogWithOutView;
    ConnectionDetector connectionDetector;
    ArrayList<Integer> stateCityId;
    Dialog dialog;
    ProgressDialog progressDialog;
    AlertMessage alertMessage;
    TextView supportText, zeroAmountText, stayWithRelativeText;
    String retainAmtVal = "", retainCAmtVal = "";
    String getWebAmount;
    boolean getWebAccount = true;
    DbCon dbCon;

    //Editedt by sandeep Singh 06-03-2017
    //start
    EditText rateperkm, kmvisited;
    String ratecard;
    TextView gstNotesLable;

    //End
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_summary_screen);
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
        tv.setText("Expense Summary");

        //android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        //actionBar.setBackgroundDrawable(new ColorDrawable(parseColor("#3F51B5")));
        //actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Expense Summary </font>"));
        dbCon = new DbCon(getApplicationContext());
        expGrpName = (TextView) findViewById(R.id.expTypeText);
        submit = (ImageView) findViewById(R.id.addnewparty);
        cancel = (ImageView) findViewById(R.id.next);
        clear = (ImageView) findViewById(R.id.clear);
        ImageView filter = (ImageView) findViewById(R.id.beat_filter);
        filter.setVisibility(View.GONE);
        goExpGrp = (ImageView) findViewById(R.id.sum);
        submit.setImageResource(R.drawable.submit);
        cancel.setImageResource(R.drawable.cancel);
        cancel.setVisibility(View.GONE);
        clear.setVisibility(View.GONE);
        addExp = (Button) findViewById(R.id.addExp);
        goExpGrp.setImageResource(R.drawable.expensegroup);
        expTypeSpinner = (Spinner) findViewById(R.id.expspinner);
        listView = (ListView) findViewById(R.id.listView1);

        dbCon.open();
        addExp.setEnabled(dbCon.ButtonEnable("AddExpense", "Expense", "Add"));
        if (!dbCon.ButtonEnable("AddExpense", "Expense", "Add")) {
            addExp.setBackgroundColor(Color.parseColor("#808080"));
        }
        dbCon.close();
       /* LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_expence_list, listView, false);
        listView.addHeaderView(header, null, false);*/
        preferences = ExpenseSummaryScreen.this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        smid = preferences.getString("CONPERID_SESSION", "0");
        userId = preferences.getString("USER_ID", "0");
        connectionDetector = new ConnectionDetector(getApplicationContext());
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        intent = getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {

            System.out.println("You are in Bundle");
            grpName = bundle.getString("EXP_GRP_NAME", "");
            fDate = bundle.getString("EXP_GRP_FDATE", "");
            tDate = bundle.getString("EXP_GRP_TDATE", "");
            grpId = bundle.getInt("EXP_GRP_ID", 0);
            // System.out.println(grpId);
            expGrpName.setText(grpName + " -- " + fDate + " to " + tDate);
//            refresh();
        }
       /* else
        {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            System.out.println("You are in Shared Pref");
                grpId=prefs.getInt("EXP_GRP_ID",0);
                grpName= prefs.getString("EXP_GRP_NAME","");
                fDate=prefs.getString("EXP_GRP_FDATE","");
                tDate=prefs.getString("EXP_GRP_TDATE","");


        }*/
        preferences1 = getSharedPreferences("MyPref", MODE_PRIVATE);
        appDataController1 = new AppDataController(ExpenseSummaryScreen.this);
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server = appData.getCompanyUrl();

        if (connectionDetector.isConnectingToInternet()) {
            new ExpenseType().execute();
        } else {

            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {
                    new SubmitExpense().execute();
                } else {
                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }
            }
        });
        // sandeep singh 04-03-17
        //Start
        //End
        addExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (expenseTypeList != null) {
                    if (expenseTypeList.size() > 0) {
                        if (!expTypeSpinner.getSelectedItem().equals("")) {
                            expTypeCode = expenseTypeList.get(expTypeSpinner.getSelectedItemPosition()).getExpcd();
                            expTypeId = expenseTypeList.get(expTypeSpinner.getSelectedItemPosition()).getId();
                            expTypeName = expTypeSpinner.getSelectedItem().toString();
                            expenseEntryObj = null;
                            if (connectionDetector.isConnectingToInternet()) {
                                new GetStates().execute();

                            } else {
                                dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                                dialogWithOutView.show(getFragmentManager(), "Info");
                            }
                        }
                    }
                }
            }
        });
        goExpGrp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new IntentSend(getApplicationContext(), AddExpense.class)).toSendAcivity();
                finish();
            }
        });
    }


    EditText partyVendorName, CGSTAmountLabel, SCGSTAmountLabel, IGSTAmountLabel;

    void initiatePopUpWindow(final String expType, ArrayList<String> stateArrayList,
                             final ArrayList<State> stateList, final ExpenseEntry expenseEntryObj, ArrayList<String> conveyanceList) {
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {

            }
        }

        dialog = new Dialog(ExpenseSummaryScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.expense_detail_form_popup);
        supportText = (TextView) dialog.findViewById(R.id.supportText);
        zeroAmountText = (TextView) dialog.findViewById(R.id.zeroAmountText);
        stayWithRelativeText = (TextView) dialog.findViewById(R.id.stayWithRelativeText);
//        supportAttach.setBackgroundColor(Color.parseColor("#dedede"));
        zeroAmount = (CheckBox) dialog.findViewById(R.id.zeroAmount);
        zeroAmount.setVisibility(View.GONE);
        zeroAmountText.setVisibility(View.GONE);
        stayWithRelative = (CheckBox) dialog.findViewById(R.id.stayWithRelative);
        stateSpinner = (Spinner) dialog.findViewById(R.id.statespinner);
        citySpinner = (Spinner) dialog.findViewById(R.id.cityspinner);
        conveyanseSpinner = (Spinner) dialog.findViewById(R.id.conveyancespinner);
        conveyanseLayout = (LinearLayout) dialog.findViewById(R.id.conveyanceLayout);
        lodgingLayout = (LinearLayout) dialog.findViewById(R.id.lodgingLayout);
        remark = (EditText) dialog.findViewById(R.id.remark);
        amt = (EditText) dialog.findViewById(R.id.amount);
        amt.setGravity(Gravity.TOP | Gravity.RIGHT);
        camt = (EditText) dialog.findViewById(R.id.claimamount);
        camt.setGravity(Gravity.TOP | Gravity.RIGHT);
        billNo = (EditText) dialog.findViewById(R.id.billNo);
        billDate = (EditText) dialog.findViewById(R.id.billDate);
        fromDate = (EditText) dialog.findViewById(R.id.fromDate);
        toDate = (EditText) dialog.findViewById(R.id.toDate);

        // Write by Sandeep Singh 06-03-2017
        //start
        rateperkm = (EditText) dialog.findViewById(R.id.cttarveperkm);
        rateperkm.setVisibility(View.GONE);
        kmvisited = (EditText) dialog.findViewById(R.id.ctkmvisited);
        kmvisited.setVisibility(View.GONE);
        //End
        fromTimeSpinner = (Spinner) dialog.findViewById(R.id.fromTimeSpinner);
        toTimeSpinner = (Spinner) dialog.findViewById(R.id.toTimeSpinner);
        supportGSTINCheckBox = (CheckBox) dialog.findViewById(R.id.requiredGstInCheckbox);
        supportGSTINCheckBox.setChecked(false);
        supportAttach = (CheckBox) dialog.findViewById(R.id.supportattach);
        supportAttach.setChecked(false);
        gstNoText = (EditText) dialog.findViewById(R.id.gstNo);
        partyVendorName = (EditText) dialog.findViewById(R.id.vendorNoText);
        CGSTAmountLabel = (EditText) dialog.findViewById(R.id.CGSTAmountLabel);
        SCGSTAmountLabel = (EditText) dialog.findViewById(R.id.SCGSTAmountLabel);
        IGSTAmountLabel = (EditText) dialog.findViewById(R.id.IGSTAmountLabel);
        final LinearLayout gstininput_layout = (LinearLayout) dialog.findViewById(R.id.input_layout);

        gstNotesLable = (TextView) dialog.findViewById(R.id.gst_note);
        gstNotesLable.setVisibility(View.GONE);
        if (expType.equalsIgnoreCase("BOARDING (FOOD)")) {
            supportGSTINCheckBox.setEnabled(false);
            supportAttach.setEnabled(false);
            gstininput_layout.setVisibility(View.GONE);
            gstNotesLable.setVisibility(View.GONE);
            billNo.setVisibility(View.GONE);
            gstNoText.setVisibility(View.GONE);
        } else {
            gstininput_layout.setVisibility(View.VISIBLE);
        }

        CGSTAmountLabel.setGravity(Gravity.TOP | Gravity.RIGHT);
        SCGSTAmountLabel.setGravity(Gravity.TOP | Gravity.RIGHT);
        IGSTAmountLabel.setGravity(Gravity.TOP | Gravity.RIGHT);
        billNo.setEnabled(false);
        IGSTAmountLabel.setEnabled(false);
        partyVendorName.setEnabled(false);
        CGSTAmountLabel.setEnabled(false);
        SCGSTAmountLabel.setEnabled(false);
        gstNoText.setEnabled(false);

        TextView titleText = (TextView) dialog.findViewById(R.id.titleText);
        titleText.setText("Expense Details - " + expType);
        ImageView cancel = (ImageView) dialog.findViewById(R.id.button2);
        ImageView save = (ImageView) dialog.findViewById(R.id.button1);
        ImageView delete = (ImageView) dialog.findViewById(R.id.button3);
        ImageView cam = (ImageView) dialog.findViewById(R.id.takePicture);
        final ImageView find = (ImageView) dialog.findViewById(R.id.findbutton1);
        find.setVisibility(View.GONE);
        cam.setVisibility(View.GONE);

        // requiredGstInCheckbox=(CheckBox) dialog.findViewById(R.id.requiredGstInCheckbox);


        billDate.setInputType(InputType.TYPE_NULL);
        billDate.setFocusable(false);
        fromDate.setInputType(InputType.TYPE_NULL);
        fromDate.setFocusable(false);
        toDate.setInputType(InputType.TYPE_NULL);
        toDate.setFocusable(false);
        comeFrom = "";
        billDate.setText(getCurrentDate());


        IGSTAmountLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.length() >= 1) {
                    CGSTAmountLabel.setText("");
                    SCGSTAmountLabel.setText("");

                }
            }
        });

        supportAttach.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    stayWithRelative.setChecked(false);

                    if (!expType.equalsIgnoreCase("BOARDING (FOOD)")) {
/*

                        gstininput_layout.setVisibility(View.VISIBLE);
                        gstNotesLable.setVisibility(View.GONE);
*/

                        billNo.setEnabled(true);
                        partyVendorName.setEnabled(true);
                      /*  CGSTAmountLabel.setEnabled(true);
                        SCGSTAmountLabel.setEnabled(true);*/

                    }

                } else {
                    billNo.setEnabled(false);
                    partyVendorName.setEnabled(false);
                    gstNotesLable.setVisibility(View.GONE);

                    /*CGSTAmountLabel.setEnabled(false);
                    SCGSTAmountLabel.setEnabled(false);
                    gstNoText.setEnabled(false);
                    partyVendorName.setText("");
                    CGSTAmountLabel.setText("");
                    SCGSTAmountLabel.setText("");
                    gstNoText.setText("");*/


//                    gstininput_layout.setVisibility(View.GONE);

                }
            }
        });
        supportGSTINCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    stayWithRelative.setChecked(false);

                    gstNoText.setEnabled(true);
                    if (!expType.equalsIgnoreCase("BOARDING (FOOD)")) {

                        gstininput_layout.setVisibility(View.VISIBLE);
                        gstNotesLable.setVisibility(View.GONE);

                        /*partyVendorName.setEnabled(true);*/
                        CGSTAmountLabel.setEnabled(true);
                        SCGSTAmountLabel.setEnabled(true);
                        IGSTAmountLabel.setEnabled(true);

                    }

                } else {
                    gstNotesLable.setVisibility(View.GONE);
                    /*partyVendorName.setEnabled(false);*/
                    CGSTAmountLabel.setEnabled(false);
                    SCGSTAmountLabel.setEnabled(false);
                    IGSTAmountLabel.setEnabled(false);
                    gstNoText.setEnabled(false);

                    CGSTAmountLabel.setText("");
                    SCGSTAmountLabel.setText("");
                    gstNoText.setText("");


//                    gstininput_layout.setVisibility(View.GONE);

                }
            }
        });
        zeroAmount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {

                if (expTypeCode.equalsIgnoreCase("CONVEYANCE")) {
                    if (!zeroAmount.isChecked()) {
                        amt.setText(retainAmtVal);
                        camt.setText(retainCAmtVal);
                        amt.setEnabled(false);
                        camt.setEnabled(false);
                    } else {
                        amt.setText("0");
                        camt.setText("0");
                        amt.setEnabled(false);
                        camt.setEnabled(false);
                    }
                } else if (expTypeCode.equalsIgnoreCase("CONVEYANCETRAVEL")) {

                    if (!zeroAmount.isChecked()) {
                        amt.setText(retainAmtVal);
                        camt.setText(retainCAmtVal);
                        amt.setEnabled(true);
                        camt.setEnabled(true);
                        kmvisited.setEnabled(true);
                    } else {
                        kmvisited.setText("");
                        amt.setText("0");
                        camt.setText("0");
                        amt.setEnabled(false);
                        camt.setEnabled(false);
                        kmvisited.setEnabled(false);
                    }
                } else {
                    if (!zeroAmount.isChecked()) {
                        amt.setText(retainAmtVal);
                        camt.setText(retainCAmtVal);
                        amt.setEnabled(true);
                        camt.setEnabled(true);
                    } else {

                        amt.setText("");
                        camt.setText("");
                        amt.setEnabled(true);
                        camt.setEnabled(true);
                    }
                }
            }
        });

        if (expType.equalsIgnoreCase("CONVEYANCE")) {
            supportAttach.setEnabled(false);
            supportGSTINCheckBox.setEnabled(false);
            billNo.setEnabled(false);
        }
        if (expType.equalsIgnoreCase("CONVEYANCE") || expType.equalsIgnoreCase("CONVEYANCE - TRAVEL")) {
            zeroAmount.setVisibility(View.VISIBLE);
            zeroAmountText.setVisibility(View.VISIBLE);
        } else {
            zeroAmount.setVisibility(View.GONE);
            zeroAmountText.setVisibility(View.GONE);
        }
        if (expType.equalsIgnoreCase("CONVEYANCE - TRAVEL")) {
            conveyanseLayout.setVisibility(View.VISIBLE);

        } else {
            conveyanseLayout.setVisibility(View.GONE);
        }
        if (expType.equalsIgnoreCase("LODGING (STAY)")) {
            lodgingLayout.setVisibility(View.VISIBLE);
            stayWithRelative.setVisibility(View.VISIBLE);
            stayWithRelativeText.setVisibility(View.VISIBLE);
            zeroAmount.setVisibility(View.GONE);
            zeroAmountText.setVisibility(View.GONE);

        } else {
            lodgingLayout.setVisibility(View.GONE);
            stayWithRelative.setVisibility(View.GONE);
            stayWithRelativeText.setVisibility(View.GONE);
        }


        stayWithRelative.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    supportAttach.setChecked(false);
                    supportGSTINCheckBox.setChecked(false);
                }
            }
        });


        stateAdapter = new ArrayAdapter<String>(ExpenseSummaryScreen.this, R.layout.simple_spinner_dropdown_item, stateArrayList);
        stateAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        // System.out.println("State Selection"+stateAdapter+"$%^&*"+expTypeCode);
        stateSpinner.setAdapter(stateAdapter);
        int selectIndex1 = 0;
        if (expTypeCode.equalsIgnoreCase("CONVEYANCE") || expTypeCode.equalsIgnoreCase("CONVEYANCETRAVEL")) {
            if (stateCityId != null) {
                for (int j = 0; j < stateList.size(); j++) {
                    if (stateList.get(j).getState_id().equals(String.valueOf(stateCityId.get(0)))) {
                        selectIndex1 = j;
                    }
                }
//    stateAdapter=new ArrayAdapter<String>(ExpenseSummaryScreen.this, R.layout.simple_spinner_dropdown_item,stateArrayList);
//    stateAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
//    // System.out.println("State Selection"+stateAdapter+"$%^&*"+expTypeCode);
//    stateSpinner.setAdapter(stateAdapter);
                stateSpinner.setSelection(selectIndex1);
            }
        } else {
            comeFrom = "";
            if (connectionDetector.isConnectingToInternet()) {
                new GetCityList().execute();

            } else {

                dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                dialogWithOutView.show(getFragmentManager(), "Info");
            }
        }

        if (conveyanceList != null) {
            conveyanceAdapter = new ArrayAdapter<String>(ExpenseSummaryScreen.this, R.layout.simple_spinner_dropdown_item, conveyanceList);
            conveyanceAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            conveyanseSpinner.setAdapter(conveyanceAdapter);
            String conveysmode = conveyanseSpinner.getSelectedItem().toString();
            System.out.println("Conveynce mode:" + conveysmode);
        }
        timeArrayList = getTimeList();
        timeAdapter = new ArrayAdapter<String>(ExpenseSummaryScreen.this, R.layout.simple_spinner_dropdown_item, timeArrayList);
        timeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        fromTimeSpinner.setAdapter(timeAdapter);
        toTimeSpinner.setAdapter(timeAdapter);

        if (expenseEntryObj != null) {
            save.setImageResource(R.drawable.update);
            delete.setVisibility(View.VISIBLE);
            dbCon.open();
            save.setEnabled(dbCon.ButtonEnable("AddExpense", "Expense", "Edit"));
            if (!dbCon.ButtonEnable("AddExpense", "Expense", "Edit")) {
                save.setColorFilter(Color.parseColor("#808080"));
            }
            dbCon.close();
            dbCon.open();
            delete.setEnabled(dbCon.ButtonEnable("AddExpense", "Expense", "Delete"));
            if (!dbCon.ButtonEnable("AddExpense", "Expense", "Delete")) {
                delete.setColorFilter(Color.parseColor("#808080"));
            }
            dbCon.close();

            int selectIndex = 0;
            try {
                for (int j = 0; j < stateList.size(); j++) {
                    if (stateList.get(j).getState_id().equals(expenseEntryObj.getStateid())) {
                        selectIndex = j;
                    }
                }
                int conSelect = 0;
                try {
                    for (int j = 0; j < conveyanceList1.size(); j++) {
                        if (conveyanceList1.get(j).getId().equals(expenseEntryObj.getTravelModeId())) {
                            conSelect = j;
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e);
                }

                remark.setText(expenseEntryObj.getRemarks());
                /****************************************  Ashutosh ****************************/

                /****************************************  Ashutosh ****************************/
                if ((expenseEntryObj.getBillAmount() == null || expenseEntryObj.getBillAmount().equalsIgnoreCase("") ? 0 : Float.parseFloat(expenseEntryObj.getBillAmount())) <= 0) {
                    zeroAmount.setChecked(true);
                } else {
                    zeroAmount.setChecked(false);

                }


                billNo.setText(expenseEntryObj.getBillNumber());
                billDate.setText(expenseEntryObj.getBillDate());
                supportAttach.setChecked(expenseEntryObj.getIsSupportingAttached());
                stayWithRelative.setChecked(expenseEntryObj.getStayWithRelative());
                fromDate.setText(expenseEntryObj.getFromDate());
                toDate.setText(expenseEntryObj.getToDate());
                fromTimeSpinner.setSelection(timeArrayList.indexOf(expenseEntryObj.getTimeFrom()));
                toTimeSpinner.setSelection(timeArrayList.indexOf(expenseEntryObj.getTimeTo()));

                conveyanseSpinner.setSelection(conSelect);
                rateperkm.setText(expenseEntryObj.getRate());
                kmvisited.setText(expenseEntryObj.getKmVisited());
                amt.setText(expenseEntryObj.getBillAmount());
                camt.setText(expenseEntryObj.getClaimAmount());
                retainAmtVal = expenseEntryObj.getBillAmount();
                retainCAmtVal = expenseEntryObj.getClaimAmount();
                stateSpinner.setSelection(selectIndex);
//                amt.setText(retainAmtVal);
//                camt.setText(retainAmtVal);
                if (expTypeCode.equalsIgnoreCase("CONVEYANCE")) {
                    if (allowToChangeCity) {
                        stateSpinner.setEnabled(true);
                        citySpinner.setEnabled(true);

                    } else {
                        stateSpinner.setEnabled(false);
                        citySpinner.setEnabled(false);

                    }
                }

            } catch (Exception e) {
                System.out.println(e);
            }
            gstNoText.setText(expenseEntryObj.getGstNo());
            supportGSTINCheckBox.setChecked(expenseEntryObj.isGSTReq());
            partyVendorName.setText(expenseEntryObj.getPartyVendorName());
            CGSTAmountLabel.setText(expenseEntryObj.getcGSTAmt());
            SCGSTAmountLabel.setText(expenseEntryObj.getsGSTAmt());
            IGSTAmountLabel.setText(expenseEntryObj.getIgstAmount());
//            expDetailId=Integer.parseInt(expenseEntryObj.getExpDetailId());
        } else {
            save.setImageResource(R.drawable.save1);
            delete.setVisibility(View.GONE);
            dbCon.open();
            save.setEnabled(dbCon.ButtonEnable("AddExpense", "Expense", "Add"));
            if (!dbCon.ButtonEnable("AddExpense", "Expense", "Add")) {
                save.setColorFilter(Color.parseColor("#808080"));
            }
            dbCon.close();
//            stateSpinner.setSelection(0);
            remark.setText("");
            amt.setText("");
            camt.setText("");
            billNo.setText("");
//            billDate.setText("");
            billDate.setText(getCurrentDate());
            supportAttach.setChecked(false);
            expDetailId = 0;
            stayWithRelative.setChecked(false);
            fromDate.setText("");
            toDate.setText("");
            fromTimeSpinner.setSelection(0);
            toTimeSpinner.setSelection(0);
            conveyanseSpinner.setSelection(0);
            if (expTypeCode.equalsIgnoreCase("CONVEYANCE")) {

                if (allowToChangeCity) {
                    stateSpinner.setEnabled(true);
                    citySpinner.setEnabled(true);
//                    try {
//                        stateSpinner.setSelection(stateCityId.get(0));
//                    }
//                    catch(Exception e)
//                    {
//
//                    }

                } else {
                    /********************************* Ashutosh *********************************/
                    try {
                        stateSpinner.setSelection(1);
                    } catch (Exception e) {

                    }
                    /********************************* Ashutosh *********************************/
                    stateSpinner.setEnabled(false);
                    citySpinner.setEnabled(false);

                }
            }


        }
        dialog.show();
        // if decline button is clicked, close the custom dialog
        dialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                dialog.dismiss();
                if (connectionDetector.isConnectingToInternet()) {
                    new ExpenseEntryType().execute();
                } else {

                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                saveData(expTypeCode);

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {

                    alertMessage = AlertMessage.newInstance(
                            "Do you want to delete ?", "delete", "cancel");
                    alertMessage.show(getFragmentManager(), "delete");
                } else {

                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }
            }
        });

        billDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = ExpenseSummaryScreen.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker2(v.getId());
            }
        });

        fromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = ExpenseSummaryScreen.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker(v.getId());
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = ExpenseSummaryScreen.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker(v.getId());
            }
        });
        /************************ WRITE by Sandeep Singh 06-04-17   *************/
        /**************     Start       **********/
        conveyanseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (conveyanseSpinner.getSelectedItem().toString().equalsIgnoreCase("COMPANY'S FOUR WHEELER") || conveyanseSpinner.getSelectedItem().toString().equalsIgnoreCase("PERSONAL FOUR WHEELER") || conveyanseSpinner.getSelectedItem().toString().equalsIgnoreCase("PERSONAL TWO WHEELER")) {
                    for (int i = 0; i < conveyanceList1.size(); i++) {
                        System.out.println("VAlue Store in Array List: " + conveyanceList1.get(i).getId() + "-" + conveyanceList1.get(i).getName() + "-"
                                + conveyanceList1.get(i).getRate());
                        if (conveyanceList1.get(i).getName().equalsIgnoreCase(conveyanseSpinner.getSelectedItem().toString())) {
                            ratecard = conveyanceList1.get(i).getRate();
                        }
                    }
                    rateperkm.setVisibility(View.VISIBLE);
                    rateperkm.setFocusable(false);
                    rateperkm.setFocusableInTouchMode(false); // user touches widget on phone with touch screen
                    rateperkm.setClickable(false);
                    kmvisited.setVisibility(View.VISIBLE);
//                        if(retainAmtVal.equals("0")){
//                            kmvisited.setText("");
//                        }
//                        else {
//                            kmvisited.setText(retainAmtVal);
//                        }

                    rateperkm.setText(ratecard);
                } else {
                    rateperkm.setVisibility(View.GONE);
                    kmvisited.setVisibility(View.GONE);
                    if (zeroAmount.isChecked()) {
                        amt.setText("0");
                        camt.setText("0");
                    } else {
                        amt.setText(retainAmtVal);
                        camt.setText(retainCAmtVal);
                    }
                    retainAmtVal = "0";
                    retainCAmtVal = "0";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                rateperkm.setVisibility(View.GONE);
                kmvisited.setVisibility(View.GONE);

            }
        });
        kmvisited.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                if (s.length() != 0) {
                    float totalclaimamount = 0;
                    try {
                        totalclaimamount = Float.parseFloat(s.toString()) * Float.parseFloat(ratecard);
                    } catch (Exception e) {
                        System.out.print(e);
                    }
//                    DecimalFormat df = new DecimalFormat("0.00");
//                    df.setMaximumFractionDigits(2);

                    try {
                        camt.setText(String.valueOf(Math.round(totalclaimamount)));
                        amt.setText(String.valueOf(Math.round(totalclaimamount)));
                    } catch (Exception e) {
                        System.out.print(e);
                    }
                } else {
                    amt.setText("0");
                    camt.setText("0");
                }

            }
        });


        /********************   END     ***************/

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (stateSpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    stateSpinner.setSelection(0);
                } else {
                    comeFrom = "";
                    if (connectionDetector.isConnectingToInternet())

                    {
                        new GetCityList().execute();

                    } else {

                        dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                        dialogWithOutView.show(getFragmentManager(), "Info");
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (expTypeCode.equalsIgnoreCase("CONVEYANCE")) {

                    if (citySpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
//                    if(expenseEntryObj!=null)
//                    {
//
//                        amt.setText(ExpenseSummaryScreen.this.expenseEntryObj.getBillAmount());
//                        camt.setText(ExpenseSummaryScreen.this.expenseEntryObj.getClaimAmount());
//                        retainAmtVal=ExpenseSummaryScreen.this.expenseEntryObj.getBillAmount();
//                        amt.setEnabled(false);
//                        camt.setEnabled(false);
//                    }
//                    else{
                        retainAmtVal = "0";
                        retainCAmtVal = "0";
                        amt.setText("0");
                        camt.setText("0");
                        amt.setEnabled(false);
                        camt.setEnabled(false);

//                    }


                    } else {
//                    if(expenseEntryObj!=null)
//                    {
//
//                        amt.setText(retainAmtVal);
//                        camt.setText(retainAmtVal);
////                        retainAmtVal=ExpenseSummaryScreen.this.expenseEntryObj.getBillAmount();
//
//                        amt.setEnabled(false);
//                        camt.setEnabled(false);
//                    }
//                    else {

                 /*   Intent ds=getIntent();
                    if(getIntent().getExtras() != null){
                        String Data=ds.getStringExtra("EXP_GRP_ID").toString();
                        Log.i("Data",Data);
                        if(!Data.equals(null)) {
                        }
                        }
                    else {*/

                        amt.setText((cityList.get(citySpinner.getSelectedItemPosition()).getAmount().equals("0") ? "0" : cityList.get(citySpinner.getSelectedItemPosition()).getAmount()));
                        camt.setText((cityList.get(citySpinner.getSelectedItemPosition()).getAmount().equals("0") ? "0" : cityList.get(citySpinner.getSelectedItemPosition()).getAmount()));
                        retainAmtVal = (cityList.get(citySpinner.getSelectedItemPosition()).getAmount().equals("0") ? "0" : cityList.get(citySpinner.getSelectedItemPosition()).getAmount());
                        retainCAmtVal = (cityList.get(citySpinner.getSelectedItemPosition()).getAmount().equals("0") ? "0" : cityList.get(citySpinner.getSelectedItemPosition()).getAmount());
                        amt.setEnabled(false);
                        camt.setEnabled(false);
                        // }
                        if (zeroAmount.isChecked()) {
                            if (getWebAccount && getWebAmount.equalsIgnoreCase("0.00")) {
                                //retainAmtVal = "0";
                                amt.setText("0");
                                camt.setText("0");
                            }
                        }

                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        amt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                camt.setText(amt.getText().toString());
            }
        });


        CGSTAmountLabel.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

                SCGSTAmountLabel.setText(CGSTAmountLabel.getText().toString());
                if (arg0.toString().length() >= 1) {
                    IGSTAmountLabel.setText("");
                }
            }
        });
    }


    void initiateTravelPopUpWindow(final String expType, ArrayList<String> stateArrayList,
                                   final ArrayList<State> stateList, ArrayList<String> tostateArrayList,
                                   final ArrayList<State> tostateList, ExpenseEntry expenseEntryObj, ArrayList<String> conveyanceList) {
        if (dialog != null) {
            try {
                dialog.dismiss();
            } catch (Exception e) {

            }

        }

//        dialog = new Dialog(ExpenseSummaryScreen.this,R.style.exp_details_dialog);
        dialog = new Dialog(ExpenseSummaryScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.travel_expense_detail_form);

        final LinearLayout gstininput_layout = (LinearLayout) dialog.findViewById(R.id.input_layout);
        supportAttach = (CheckBox) dialog.findViewById(R.id.supportattach);
        supportAttach.setChecked(false);
        zeroAmount = (CheckBox) dialog.findViewById(R.id.zeroAmount);
        stateSpinner = (Spinner) dialog.findViewById(R.id.fromstatespinner);
        citySpinner = (Spinner) dialog.findViewById(R.id.fromcityspinner);
        tostateSpinner = (Spinner) dialog.findViewById(R.id.tostatespinner);
        tocitySpinner = (Spinner) dialog.findViewById(R.id.tocityspinner);
        conveyanseSpinner = (Spinner) dialog.findViewById(R.id.conveyancespinner);
        conveyanseLayout = (LinearLayout) dialog.findViewById(R.id.conveyanceLayout);
        lodgingLayout = (LinearLayout) dialog.findViewById(R.id.lodgingLayout);
        remark = (EditText) dialog.findViewById(R.id.remark);
        amt = (EditText) dialog.findViewById(R.id.amount);
        amt.setGravity(Gravity.TOP | Gravity.RIGHT);
        camt = (EditText) dialog.findViewById(R.id.claimamount);
        camt.setGravity(Gravity.TOP | Gravity.RIGHT);
        billNo = (EditText) dialog.findViewById(R.id.billNo);
        billDate = (EditText) dialog.findViewById(R.id.billDate);
        fromDate = (EditText) dialog.findViewById(R.id.fromDate);
        toDate = (EditText) dialog.findViewById(R.id.toDate);
        fromTimeSpinner = (Spinner) dialog.findViewById(R.id.fromTimeSpinner);
        toTimeSpinner = (Spinner) dialog.findViewById(R.id.toTimeSpinner);
        gstNotesLable = (TextView) dialog.findViewById(R.id.gst_note);
        gstNotesLable.setVisibility(View.GONE);
        if (expType.equalsIgnoreCase("BOARDING")) {
            gstininput_layout.setVisibility(View.GONE);
        } else {
            gstininput_layout.setVisibility(View.VISIBLE);
        }

        gstNoText = (EditText) dialog.findViewById(R.id.gstNo);
        partyVendorName = (EditText) dialog.findViewById(R.id.vendorNoText);
        CGSTAmountLabel = (EditText) dialog.findViewById(R.id.CGSTAmountLabel);
        SCGSTAmountLabel = (EditText) dialog.findViewById(R.id.SCGSTAmountLabel);
        IGSTAmountLabel = (EditText) dialog.findViewById(R.id.IGSTAmountLabel);
        supportGSTINCheckBox = (CheckBox) dialog.findViewById(R.id.requiredGstInCheckbox);
        CGSTAmountLabel.setGravity(Gravity.TOP | Gravity.RIGHT);
        SCGSTAmountLabel.setGravity(Gravity.TOP | Gravity.RIGHT);
        IGSTAmountLabel.setGravity(Gravity.TOP | Gravity.RIGHT);
        partyVendorName.setEnabled(false);
        CGSTAmountLabel.setEnabled(false);
        SCGSTAmountLabel.setEnabled(false);
        IGSTAmountLabel.setEnabled(false);
        billNo.setEnabled(false);
        gstNoText.setEnabled(false);
        supportGSTINCheckBox.setChecked(false);

        TextView titleText = (TextView) dialog.findViewById(R.id.titleText);
        titleText.setText("Expense Details - " + expType);
        ImageView cancel = (ImageView) dialog.findViewById(R.id.button2);
        ImageView save = (ImageView) dialog.findViewById(R.id.button1);
        ImageView delete = (ImageView) dialog.findViewById(R.id.button3);
        ImageView cam = (ImageView) dialog.findViewById(R.id.takePicture);
        ImageView find = (ImageView) dialog.findViewById(R.id.findbutton1);
        find.setVisibility(View.GONE);
        cam.setVisibility(View.GONE);

        billDate.setInputType(InputType.TYPE_NULL);
        billDate.setFocusable(false);
        fromDate.setInputType(InputType.TYPE_NULL);
        fromDate.setFocusable(false);
        toDate.setInputType(InputType.TYPE_NULL);
        toDate.setFocusable(false);
        billDate.setText(getCurrentDate());

        IGSTAmountLabel.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (editable.toString().length() >= 1) {
                    CGSTAmountLabel.setText("");
                    SCGSTAmountLabel.setText("");
                }
            }
        });

        supportAttach.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {

                    if (!expType.equalsIgnoreCase("BOARDING")) {
                        /*gstNotesLable.setVisibility(View.GONE);

                        gstininput_layout.setVisibility(View.VISIBLE);*/
                        partyVendorName.setEnabled(true);
                        billNo.setEnabled(true);

                    }

                } else {

                    partyVendorName.setEnabled(false);
                    billNo.setEnabled(false);

//                    gstininput_layout.setVisibility(View.GONE);

                }
            }
        });


        supportGSTINCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {

                    if (!expType.equalsIgnoreCase("BOARDING")) {
                        gstNotesLable.setVisibility(View.GONE);

                        gstininput_layout.setVisibility(View.VISIBLE);
//                        partyVendorName.setEnabled(true);
                        CGSTAmountLabel.setEnabled(true);
                        SCGSTAmountLabel.setEnabled(true);
                        IGSTAmountLabel.setEnabled(true);
                        gstNoText.setEnabled(true);
                    }

                } else {
                    gstNotesLable.setVisibility(View.GONE);

//                    partyVendorName.setEnabled(false);
                    CGSTAmountLabel.setEnabled(false);
                    SCGSTAmountLabel.setEnabled(false);
                    IGSTAmountLabel.setEnabled(false);
                    gstNoText.setEnabled(false);

                    partyVendorName.setText("");
                    CGSTAmountLabel.setText("");
                    SCGSTAmountLabel.setText("");
                    gstNoText.setText("");
//                    gstininput_layout.setVisibility(View.GONE);

                }
            }
        });


        zeroAmount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (zeroAmount.isChecked()) {
                    amt.setText("0");
                    camt.setText("0");
                    amt.setEnabled(false);
                    camt.setEnabled(false);
                } else {
                    amt.setText("");
                    camt.setText("");
                    amt.setEnabled(true);
                    camt.setEnabled(true);
                }
            }
        });

        stateAdapter = new ArrayAdapter<String>(ExpenseSummaryScreen.this, R.layout.simple_spinner_dropdown_item, stateArrayList);
        stateAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);
        tostateAdapter = new ArrayAdapter<String>(ExpenseSummaryScreen.this, R.layout.simple_spinner_dropdown_item, tostateArrayList);
        tostateAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        tostateSpinner.setAdapter(tostateAdapter);

        if (conveyanceList != null) {
            conveyanceAdapter = new ArrayAdapter<String>(ExpenseSummaryScreen.this, R.layout.simple_spinner_dropdown_item, conveyanceList);
            conveyanceAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
            conveyanseSpinner.setAdapter(conveyanceAdapter);
        }
        timeArrayList = getTimeList();
        timeAdapter = new ArrayAdapter<String>(ExpenseSummaryScreen.this, R.layout.simple_spinner_dropdown_item, timeArrayList);
        timeAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
        fromTimeSpinner.setAdapter(timeAdapter);
        toTimeSpinner.setAdapter(timeAdapter);
        comeFrom = "fromstate";
        if (connectionDetector.isConnectingToInternet()) {
            new GetCityList().execute();

        } else {

            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

        int selectIndex = 0;
        int selectToIndex = 0;
        if (expenseEntryObj != null) {
            save.setImageResource(R.drawable.update);
            delete.setVisibility(View.VISIBLE);
            dbCon.open();
            save.setEnabled(dbCon.ButtonEnable("AddExpense", "Expense", "Edit"));
            if (!dbCon.ButtonEnable("AddExpense", "Expense", "Edit")) {
                save.setColorFilter(Color.parseColor("#808080"));
            }
            dbCon.close();
            dbCon.open();
            delete.setEnabled(dbCon.ButtonEnable("AddExpense", "Expense", "Delete"));
            if (!dbCon.ButtonEnable("AddExpense", "Expense", "Delete")) {
                delete.setColorFilter(Color.parseColor("#808080"));
            }
            dbCon.close();

            try {
                for (int j = 0; j < stateList.size(); j++) {
                    if (stateList.get(j).getState_id().equals(expenseEntryObj.getFromstate())) {
                        selectIndex = j;
                    }
                }
                for (int j = 0; j < stateList.size(); j++) {
                    if (stateList.get(j).getState_id().equals(expenseEntryObj.getTostate())) {
                        selectToIndex = j;
                    }
                }

                int conSelect = 0;
                for (int j = 0; j < conveyanceList1.size(); j++) {
                    if (conveyanceList1.get(j).getId().equals(expenseEntryObj.getTravelModeId())) {
                        conSelect = j;
                    }
                }

                remark.setText(expenseEntryObj.getRemarks());
                if ((expenseEntryObj.getBillAmount() == null || expenseEntryObj.getBillAmount().equalsIgnoreCase("") ? 0 : Float.parseFloat(expenseEntryObj.getBillAmount())) <= 0) {
                    zeroAmount.setChecked(true);
                }
                conveyanseSpinner.setSelection(conSelect);
                amt.setText(expenseEntryObj.getBillAmount());
                camt.setText(expenseEntryObj.getClaimAmount());
                billNo.setText(expenseEntryObj.getBillNumber());
                billDate.setText(expenseEntryObj.getBillDate());
                gstNoText.setText(expenseEntryObj.getGstNo());
                supportAttach.setChecked(expenseEntryObj.getIsSupportingAttached());
//             stayWithRelative.setChecked(expenseEntryObj.getStayWithRelative());
                fromDate.setText(expenseEntryObj.getFromDate());
                toDate.setText(expenseEntryObj.getToDate());
                fromTimeSpinner.setSelection(timeArrayList.indexOf(expenseEntryObj.getTimeFrom()));
                toTimeSpinner.setSelection(timeArrayList.indexOf(expenseEntryObj.getTimeTo()));
                stateSpinner.setSelection(selectIndex);
                tostateSpinner.setSelection(selectToIndex);
                gstNoText.setText(expenseEntryObj.getGstNo());
                supportGSTINCheckBox.setChecked(expenseEntryObj.isGSTReq());
                partyVendorName.setText(expenseEntryObj.getPartyVendorName());
                CGSTAmountLabel.setText(expenseEntryObj.getcGSTAmt());
                SCGSTAmountLabel.setText(expenseEntryObj.getsGSTAmt());

            } catch (Exception e) {
                System.out.println(e);
            }
//            expDetailId=Integer.parseInt(expenseEntryObj.getExpDetailId());
        } else {
            save.setImageResource(R.drawable.save1);
            delete.setVisibility(View.GONE);
            dbCon.open();
            save.setEnabled(dbCon.ButtonEnable("AddExpense", "Expense", "Add"));
            if (!dbCon.ButtonEnable("AddExpense", "Expense", "Add")) {
                save.setColorFilter(Color.parseColor("#808080"));
            }

            dbCon.close();
            stateSpinner.setSelection(0);
            remark.setText("");
            amt.setText("");
            camt.setText("");
            billNo.setText("");
//            billDate.setText("");
            billDate.setText(getCurrentDate());
            supportAttach.setChecked(false);
            expDetailId = 0;
//            stayWithRelative.setChecked(false);
            fromDate.setText("");
            toDate.setText("");
            fromTimeSpinner.setSelection(0);
            toTimeSpinner.setSelection(0);
            conveyanseSpinner.setSelection(0);
        }
        dialog.show();
        // if decline button is clicked, close the custom dialog
        dialog.setCanceledOnTouchOutside(false);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                dialog.dismiss();
                if (connectionDetector.isConnectingToInternet())

                {
                    new ExpenseEntryType().execute();

                } else {

                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Close dialog
                saveData(expTypeCode);

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connectionDetector.isConnectingToInternet()) {

                    alertMessage = AlertMessage.newInstance(
                            "Do you want delete ?", "delete", "cancel");
                    alertMessage.show(getFragmentManager(), "delete");
                } else {

                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }
            }
        });
        billDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = ExpenseSummaryScreen.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker2(v.getId());
            }
        });

        fromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = ExpenseSummaryScreen.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker(v.getId());
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                View view = ExpenseSummaryScreen.this.getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                showDatePicker(v.getId());
            }
        });

        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (stateSpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    citySpinner.setSelection(0);
//                    citySpinner.setAdapter(null);

                } else {
                    comeFrom = "fromstate";
                    if (connectionDetector.isConnectingToInternet())

                    {
                        new GetCityList().execute();

                    } else {

                        dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                        dialogWithOutView.show(getFragmentManager(), "Info");
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        tostateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
                if (tostateSpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                    tocitySpinner.setSelection(0);
//                    tocitySpinner.setAdapter(null);

                } else {

                    if (connectionDetector.isConnectingToInternet())

                    {
                        new GetToCityList().execute();

                    } else {

                        dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                        dialogWithOutView.show(getFragmentManager(), "Info");
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });


        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub
//                if(expTypeCode.equalsIgnoreCase("CONVEYANCE"))
//                {
//
//                    if(citySpinner.getSelectedItem().toString().equalsIgnoreCase("Select City"))
//                    {
//                        amt.setText("0");
//                        camt.setText("0");
//                        amt.setEnabled(false);
//                        camt.setEnabled(false);
//
//                    }
//                    else{
//                        amt.setText((cityList.get(citySpinner.getSelectedItemPosition()).getAmount().equals("0")?"0":cityList.get(citySpinner.getSelectedItemPosition()).getAmount()));
//                        camt.setText((cityList.get(citySpinner.getSelectedItemPosition()).getAmount().equals("0")?"0":cityList.get(citySpinner.getSelectedItemPosition()).getAmount()));
//                        amt.setEnabled(false);
//                        camt.setEnabled(false);
//
//                    }
//
//                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        CGSTAmountLabel.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                if (arg0.length() >= 1) {
                    SCGSTAmountLabel.setText(CGSTAmountLabel.getText().toString());
                    IGSTAmountLabel.setText("");
                }
            }
        });


        amt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                camt.setText(amt.getText().toString());
            }
        });
    }


    private void showDatePicker2(int id) {
        DatePickerFragmentRange date = new DatePickerFragmentRange();
        /**
         * Set Up Current Date Into dialog
         */

        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        args.putInt("grace", EmpGracePeriod);
        args.putString("Type", "days");
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        if (id == R.id.billDate) {
            date.setCallBack(ondate);
            date.show(getSupportFragmentManager(), "Date Picker");
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
        if (id == R.id.fromDate) {
            date.setCallBack(ondate1);
            date.show(getSupportFragmentManager(), "Date Picker");
        } else if (id == R.id.toDate) {
            date.setCallBack(ondate2);
            date.show(getSupportFragmentManager(), "Date Picker");
        }

    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            long f, t;
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

            Date filledDate = null;
            try {
                filledDate = format2.parse(format2.format(date));

            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (filledDate.getTime() < (System.currentTimeMillis())) {
//                billDate.setText(format2.format(date));

                f = DateFunction.ConvertDateToTimestamp(fDate);
                t = DateFunction.ConvertDateToTimestamp(tDate);
                if (filledDate.getTime() >= f && filledDate.getTime() <= t) {
                    billDate.setText(format2.format(date));

                } else {
                    alertOkDialog = AlertOkDialog.newInstance("Bill Date should be between Expense Group Dates");
                    alertOkDialog.show(getFragmentManager(), "Info");
                }
            } else {
                alertOkDialog = AlertOkDialog.newInstance("Can't fill future date");
                alertOkDialog.show(getFragmentManager(), "Info");
            }

        }
    };

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

//            Date filledDate = null;
//            try {
//                filledDate = format2.parse(format2.format(date));
//
//            } catch (ParseException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//            if(filledDate.getTime()<(System.currentTimeMillis()))
//            {
////                billDate.setText(format2.format(date));
//
//                f = DateFunction.ConvertDateToTimestamp(fDate);
//                t = DateFunction.ConvertDateToTimestamp(tDate);
//                if(filledDate.getTime()>=f && filledDate.getTime()<=t)
//                {
            fromDate.setText(format2.format(date));

//                }
//
//                else{
//                    alertOkDialog= AlertOkDialog.newInstance("Bill Date should be between Expense Group Dates");
//                    alertOkDialog.show(getFragmentManager(), "Info");
//                }
//            }
//
//            else{
//                alertOkDialog= AlertOkDialog.newInstance("Can't fill future date");
//                alertOkDialog.show(getFragmentManager(), "Info");
//            }
//
        }
    };

    DatePickerDialog.OnDateSetListener ondate2 = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            String strDate = (dayOfMonth < 10 ? ("0" + dayOfMonth) : (dayOfMonth)) + "/" + (((monthOfYear + 1) < 10 ? ("0" + (monthOfYear + 1)) : (monthOfYear + 1))) + "/" + year;
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


//                f = DateFunction.ConvertDateToTimestamp(fromDate.getText().toString());
//                if(filledDate.getTime()>f)
//                {
            toDate.setText(format2.format(date));

//                }
//
//                else{
//                    alertOkDialog= AlertOkDialog.newInstance("To Date and Time should be greater than From Date and Time");
//                    alertOkDialog.show(getFragmentManager(), "Info");
//                }


        }
    };

    void saveData(String expTypeCode) {
        if (expTypeCode.equalsIgnoreCase("CONVEYANCE")) {
            if (stateSpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--") || stateSpinner.getSelectedItem().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select State").showCustomAlert();

            } else if (citySpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--") || citySpinner.getSelectedItem().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select City").showCustomAlert();
            } else if (billDate.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select Bill Date").showCustomAlert();
            } else if (!checkBillDate()) {
                new Custom_Toast(getApplicationContext(), "Bill Date should be between Expense Group Dates").showCustomAlert();

            }
            /*else if (billNo.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Enter Bill No.").showCustomAlert();
            }*/
            else if (amt.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Enter Amount").showCustomAlert();
            } else if (!zeroAmount.isChecked() && Float.parseFloat(amt.getText().toString()) <= Float.parseFloat("0")) {
                new Custom_Toast(getApplicationContext(), "Please enter Amount greater than 0").showCustomAlert();
            } else if (camt.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Enter Claim Amount").showCustomAlert();
            } else if (Float.parseFloat(camt.getText().toString()) > Float.parseFloat(amt.getText().toString())) {
                new Custom_Toast(getApplicationContext(), "Claim Amount should not be greater than Bill Amount").showCustomAlert();
            } else {


//            dialog.dismiss();
                if (supportGSTINCheckBox.isChecked()) {
                    if (gstNoText.getText().toString().length() < 15) {
                        new Custom_Toast(getApplicationContext(), "Please enter 15 digit GSTIN No.").showCustomAlert();
                        return;
                    }
                   /* if(partyVendorName.getText().toString().isEmpty())
                    {
                        new Custom_Toast(getApplicationContext(), "Please Enter Vendor/Party Name").showCustomAlert();
                        return;
                    }*/
                    if (IGSTAmountLabel.getText().toString().equals("0")) {
                        new Custom_Toast(getApplicationContext(), "IGST Amount can not be 0.").showCustomAlert();
                        return;
                    }
                    if (IGSTAmountLabel.getText().toString().isEmpty()) {
                        if (CGSTAmountLabel.getText().toString().isEmpty()) {
                            new Custom_Toast(getApplicationContext(), "Please Enter CGST Amount").showCustomAlert();
                            return;
                        }
                        if (CGSTAmountLabel.getText().toString().equals("0")) {
                            new Custom_Toast(getApplicationContext(), "CGST Amount can not be 0.").showCustomAlert();
                            return;
                        }
                        if (SCGSTAmountLabel.getText().toString().isEmpty()) {
                            new Custom_Toast(getApplicationContext(), "Please Enter SGST Amount").showCustomAlert();
                            return;
                        }
                        if (SCGSTAmountLabel.getText().toString().equals("0")) {
                            new Custom_Toast(getApplicationContext(), "SGST Amount can not be 0.").showCustomAlert();
                            return;
                        }
                        if (!SCGSTAmountLabel.getText().toString().equals(CGSTAmountLabel.getText().toString())) {
                            new Custom_Toast(getApplicationContext(), "CGST amount and SGST amount should be same.").showCustomAlert();
                            return;
                        }
                    }


                    if (!supportAttach.isChecked()) {
                        new Custom_Toast(getApplicationContext(), "Please Select Supporting Attached Option.").showCustomAlert();
                        return;
                    }
                }

                if (supportAttach.isChecked()) {
                    if (partyVendorName.getText().toString().isEmpty()) {
                        new Custom_Toast(getApplicationContext(), "Please Enter Vendor/Party Name").showCustomAlert();
                        return;
                    }
                    if (billNo.getText().toString().isEmpty()) {
                        new Custom_Toast(getApplicationContext(), "Please Enter Bill No").showCustomAlert();
                        return;
                    }
                }

                expenseEntry = new ExpenseEntry();
                expenseEntry.setGSTReq(supportGSTINCheckBox.isChecked());
                expenseEntry.setPartyVendorName(partyVendorName.getText().toString());
                expenseEntry.setcGSTAmt(CGSTAmountLabel.getText().toString());
                expenseEntry.setsGSTAmt(SCGSTAmountLabel.getText().toString());
                expenseEntry.setIgstAmount(IGSTAmountLabel.getText().toString());

                expenseEntry.setCityId(cityList.get(citySpinner.getSelectedItemPosition()).getCity_id());
                expenseEntry.setGstNo(gstNoText.getText().toString());
                expenseEntry.setBillDate(billDate.getText().toString());
                expenseEntry.setBillNumber(billNo.getText().toString());
                expenseEntry.setBillAmount(amt.getText().toString());
                expenseEntry.setClaimAmount(camt.getText().toString());
                expenseEntry.setRemarks((remark.getText().toString().isEmpty() ? "" : remark.getText().toString()));
                expenseEntry.setExpCode(expTypeCode);
                expenseEntry.setExpenseTypeId(expTypeId);
                expenseEntry.setExpenseGrpId(String.valueOf(grpId));
                expenseEntry.setExpDetailId(String.valueOf(expDetailId));
                expenseEntry.setIsSupportingAttached(supportAttach.isChecked());
                expenseEntry.setKmVisited(kmvisited.getText().toString().isEmpty() ? "0" : kmvisited.getText().toString());
                expenseEntry.setRate(rateperkm.getText().toString().isEmpty() ? "0" : rateperkm.getText().toString());


                if (connectionDetector.isConnectingToInternet()) {
                    new SaveExpense().execute();
                } else {
                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }


            }
        } else if (expTypeCode.equalsIgnoreCase("CONVEYANCETRAVEL")) {
            if (stateSpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--") || stateSpinner.getSelectedItem().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select State").showCustomAlert();

            } else if (citySpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--") || citySpinner.getSelectedItem().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select City").showCustomAlert();
            } else if (billDate.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select Bill Date").showCustomAlert();
            } else if (!checkBillDate()) {
                new Custom_Toast(getApplicationContext(), "Bill Date should be between Expense Group Dates").showCustomAlert();

            }
           /* else if (billNo.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Enter Bill No.").showCustomAlert();
            }*/
            else if (conveyanseSpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                new Custom_Toast(getApplicationContext(), "Please select a Conveyance Mode").showCustomAlert();
            } else if (amt.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Enter Amount").showCustomAlert();
            } else if (!zeroAmount.isChecked() && Float.parseFloat(amt.getText().toString()) <= Float.parseFloat("0")) {

                new Custom_Toast(getApplicationContext(), "Please enter Amount greater than 0").showCustomAlert();
            }


//        else if(!expTypeCode.equalsIgnoreCase("CONVEYANCE") || !expTypeCode.equalsIgnoreCase("CONVEYANCETRAVEL"))
//        {


//        }

            else if (camt.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Enter Claim Amount").showCustomAlert();
            } else if (Float.parseFloat(camt.getText().toString()) > Float.parseFloat(amt.getText().toString())) {
                new Custom_Toast(getApplicationContext(), "Claim Amount should not be greater than Bill Amount").showCustomAlert();
            } else {
//            dialog.dismiss();
                if (supportGSTINCheckBox.isChecked()) {
                    if (gstNoText.getText().toString().length() < 15) {
                        new Custom_Toast(getApplicationContext(), "Please enter 15 digit GSTIN No.").showCustomAlert();
                        return;
                    }
                   /* if(partyVendorName.getText().toString().isEmpty())
                    {
                        new Custom_Toast(getApplicationContext(), "Please Enter Vendor/Party Name").showCustomAlert();
                        return;
                    }*/
                    if (IGSTAmountLabel.getText().toString().equals("0")) {
                        new Custom_Toast(getApplicationContext(), "IGST Amount can not be 0.").showCustomAlert();
                        return;
                    }
                    if (IGSTAmountLabel.getText().toString().isEmpty()) {
                        if (CGSTAmountLabel.getText().toString().isEmpty()) {
                            new Custom_Toast(getApplicationContext(), "Please Enter CGST Amount").showCustomAlert();
                            return;
                        }
                        if (CGSTAmountLabel.getText().toString().equals("0")) {
                            new Custom_Toast(getApplicationContext(), "CGST Amount can not be 0.").showCustomAlert();
                            return;
                        }
                        if (SCGSTAmountLabel.getText().toString().isEmpty()) {
                            new Custom_Toast(getApplicationContext(), "Please Enter SGST Amount").showCustomAlert();
                            return;
                        }
                        if (SCGSTAmountLabel.getText().toString().equals("0")) {
                            new Custom_Toast(getApplicationContext(), "SGST Amount can not be 0.").showCustomAlert();
                            return;
                        }
                        if (!SCGSTAmountLabel.getText().toString().equals(CGSTAmountLabel.getText().toString())) {
                            new Custom_Toast(getApplicationContext(), "CGST amount and SGST amount should be same.").showCustomAlert();
                            return;
                        }
                    }
                    if (!supportAttach.isChecked()) {
                        new Custom_Toast(getApplicationContext(), "Please Select Supporting Attached Option.").showCustomAlert();
                        return;
                    }
                }

                if (supportAttach.isChecked()) {
                    if (partyVendorName.getText().toString().isEmpty()) {
                        new Custom_Toast(getApplicationContext(), "Please Enter Vendor/Party Name").showCustomAlert();
                        return;
                    }
                    if (billNo.getText().toString().isEmpty()) {
                        new Custom_Toast(getApplicationContext(), "Please Enter Bill No").showCustomAlert();
                        return;
                    }
                }
                expenseEntry = new ExpenseEntry();
                expenseEntry.setGSTReq(supportGSTINCheckBox.isChecked());
                expenseEntry.setPartyVendorName(partyVendorName.getText().toString());
                expenseEntry.setcGSTAmt(CGSTAmountLabel.getText().toString());
                expenseEntry.setsGSTAmt(SCGSTAmountLabel.getText().toString());
                expenseEntry.setIgstAmount(IGSTAmountLabel.getText().toString());
                expenseEntry.setGstNo(gstNoText.getText().toString());
                expenseEntry.setCityId(cityList.get(citySpinner.getSelectedItemPosition()).getCity_id());
                expenseEntry.setBillDate(billDate.getText().toString());
                expenseEntry.setBillNumber(billNo.getText().toString());
                expenseEntry.setBillAmount(amt.getText().toString());
                expenseEntry.setClaimAmount(camt.getText().toString());
                expenseEntry.setRemarks((remark.getText().toString().isEmpty() ? "" : remark.getText().toString()));
                expenseEntry.setExpCode(expTypeCode);
                expenseEntry.setExpenseTypeId(expTypeId);
                expenseEntry.setExpenseGrpId(String.valueOf(grpId));
                expenseEntry.setExpDetailId(String.valueOf(expDetailId));
                expenseEntry.setIsSupportingAttached(supportAttach.isChecked());
                expenseEntry.setKmVisited(kmvisited.getText().toString().isEmpty() ? "0" : kmvisited.getText().toString());
                expenseEntry.setRate(rateperkm.getText().toString().isEmpty() ? "0" : rateperkm.getText().toString());
                expenseEntry.setTravelModeId(conveyanceList1.get(conveyanseSpinner.getSelectedItemPosition()).getId());
                if (connectionDetector.isConnectingToInternet()) {
                    new SaveExpense().execute();
                } else {
                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }

            }


        } else if (expTypeCode.equalsIgnoreCase("LODGING")) {
            long b, t;
            if (stateSpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--") || stateSpinner.getSelectedItem().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select State").showCustomAlert();

            } else if (citySpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--") || citySpinner.getSelectedItem().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select City").showCustomAlert();
            } else if (fromDate.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select From Date").showCustomAlert();
            } else if (toDate.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select To Date").showCustomAlert();
            } else if (fromTimeSpinner.getSelectedItem().toString().equalsIgnoreCase("00:00")) {
                new Custom_Toast(getApplicationContext(), "Please Select From Time").showCustomAlert();
            } else if (toTimeSpinner.getSelectedItem().toString().equalsIgnoreCase("00:00")) {
                new Custom_Toast(getApplicationContext(), "Please Select To Time").showCustomAlert();
            } else if (!checkBillDate()) {
                new Custom_Toast(getApplicationContext(), "Bill Date should be between Expense Group Dates").showCustomAlert();

            }
            /*else if (billNo.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Enter Bill No.").showCustomAlert();
            }*/
            else if (amt.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Enter Amount").showCustomAlert();
            } else if (Float.parseFloat(amt.getText().toString()) <= 0) {

                new Custom_Toast(getApplicationContext(), "Please enter Amount greater than 0").showCustomAlert();
            } else if (camt.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Enter Claim Amount").showCustomAlert();
            } else if (Float.parseFloat(camt.getText().toString()) > Float.parseFloat(amt.getText().toString())) {
                new Custom_Toast(getApplicationContext(), "Claim Amount should not be greater than Bill Amount").showCustomAlert();
            } else {

                if (supportGSTINCheckBox.isChecked()) {
                    if (gstNoText.getText().toString().length() < 15) {
                        new Custom_Toast(getApplicationContext(), "Please enter 15 digit GSTIN No.").showCustomAlert();
                        return;
                    }
                   /* if(partyVendorName.getText().toString().isEmpty())
                    {
                        new Custom_Toast(getApplicationContext(), "Please Enter Vendor/Party Name").showCustomAlert();
                        return;
                    }*/
                    if (IGSTAmountLabel.getText().toString().equals("0")) {
                        new Custom_Toast(getApplicationContext(), "IGST Amount can not be 0.").showCustomAlert();
                        return;
                    }
                    if (IGSTAmountLabel.getText().toString().isEmpty()) {
                        if (CGSTAmountLabel.getText().toString().isEmpty()) {
                            new Custom_Toast(getApplicationContext(), "Please Enter CGST Amount").showCustomAlert();
                            return;
                        }
                        if (CGSTAmountLabel.getText().toString().equals("0")) {
                            new Custom_Toast(getApplicationContext(), "CGST Amount can not be 0.").showCustomAlert();
                            return;
                        }
                        if (SCGSTAmountLabel.getText().toString().isEmpty()) {
                            new Custom_Toast(getApplicationContext(), "Please Enter SGST Amount").showCustomAlert();
                            return;
                        }
                        if (SCGSTAmountLabel.getText().toString().equals("0")) {
                            new Custom_Toast(getApplicationContext(), "SGST Amount can not be 0.").showCustomAlert();
                            return;
                        }
                        if (!SCGSTAmountLabel.getText().toString().equals(CGSTAmountLabel.getText().toString())) {
                            new Custom_Toast(getApplicationContext(), "CGST amount and SGST amount should be same.").showCustomAlert();
                            return;
                        }
                    }
                    if (!supportAttach.isChecked()) {
                        new Custom_Toast(getApplicationContext(), "Please Select Supporting Attached Option.").showCustomAlert();
                        return;
                    }
                }

                if (supportAttach.isChecked()) {
                    if (partyVendorName.getText().toString().isEmpty()) {
                        new Custom_Toast(getApplicationContext(), "Please Enter Vendor/Party Name").showCustomAlert();
                        return;
                    }
                    if (billNo.getText().toString().isEmpty()) {
                        new Custom_Toast(getApplicationContext(), "Please Enter Bill No").showCustomAlert();
                        return;
                    }
                }
                b = DateFunction.ConvertDateToTimestamp(billDate.getText().toString());
                t = DateFunction.ConvertDateToTimestamp(toDate.getText().toString());

                if (b < t) {
                    new Custom_Toast(getApplicationContext(), "Bill Date should be greater than or equal to ToDate").showCustomAlert();

                } else {

                    b = DateFunction.ConvertDateToTimestampWithTime(fromDate.getText().toString() + " " + fromTimeSpinner.getSelectedItem().toString());
                    t = DateFunction.ConvertDateToTimestampWithTime(toDate.getText().toString() + " " + toTimeSpinner.getSelectedItem().toString());

                    if (b > t) {
                        new Custom_Toast(getApplicationContext(), "To Date and Time should be greater than From Date and Time").showCustomAlert();
                    } else {
                        expenseEntry = new ExpenseEntry();
                        expenseEntry.setGSTReq(supportGSTINCheckBox.isChecked());
                        expenseEntry.setPartyVendorName(partyVendorName.getText().toString());
                        expenseEntry.setcGSTAmt(CGSTAmountLabel.getText().toString());
                        expenseEntry.setsGSTAmt(SCGSTAmountLabel.getText().toString());
                        expenseEntry.setIgstAmount(IGSTAmountLabel.getText().toString());
                        expenseEntry.setGstNo(gstNoText.getText().toString());
                        expenseEntry.setCityId(cityList.get(citySpinner.getSelectedItemPosition()).getCity_id());
                        expenseEntry.setBillDate(billDate.getText().toString());
                        expenseEntry.setBillNumber(billNo.getText().toString());
                        expenseEntry.setBillAmount(amt.getText().toString());
                        expenseEntry.setClaimAmount(camt.getText().toString());
                        expenseEntry.setRemarks((remark.getText().toString().isEmpty() ? "" : remark.getText().toString()));
                        expenseEntry.setExpCode(expTypeCode);
                        expenseEntry.setExpenseTypeId(expTypeId);
                        expenseEntry.setExpenseGrpId(String.valueOf(grpId));
                        expenseEntry.setExpDetailId(String.valueOf(expDetailId));
                        expenseEntry.setIsSupportingAttached(supportAttach.isChecked());
                        expenseEntry.setFromDate(fromDate.getText().toString());
                        expenseEntry.setToDate(toDate.getText().toString());
                        expenseEntry.setTimeFrom(fromTimeSpinner.getSelectedItem().toString());
                        expenseEntry.setTimeTo(toTimeSpinner.getSelectedItem().toString());
                        expenseEntry.setStayWithRelative(stayWithRelative.isChecked());
                        expenseEntry.setKmVisited(kmvisited.getText().toString().isEmpty() ? "0" : kmvisited.getText().toString());
                        expenseEntry.setRate(rateperkm.getText().toString().isEmpty() ? "0" : rateperkm.getText().toString());
                        if (connectionDetector.isConnectingToInternet())

                        {
                            new SaveExpense().execute();

                        } else {

                            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                            dialogWithOutView.show(getFragmentManager(), "Info");
                        }

                    }
                }
            }
        } else if (expTypeCode.equalsIgnoreCase("TRAVEL")) {
            long b, t;
            if (billDate.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select Bill Date").showCustomAlert();
            } else if (!checkBillDate()) {
                new Custom_Toast(getApplicationContext(), "Bill Date should be between Expense Group Dates").showCustomAlert();

            }
           /* else if (billNo.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Enter Bill No.").showCustomAlert();
            }*/
            else if (amt.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Enter Amount").showCustomAlert();
            } else if (!zeroAmount.isChecked() && Float.parseFloat(amt.getText().toString()) <= Float.parseFloat("0")) {

                new Custom_Toast(getApplicationContext(), "Please enter Amount greater than 0").showCustomAlert();
            } else if (camt.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Enter Claim Amount").showCustomAlert();
            } else if (Float.parseFloat(camt.getText().toString()) > Float.parseFloat(amt.getText().toString())) {
                new Custom_Toast(getApplicationContext(), "Claim Amount should not be greater than Bill Amount").showCustomAlert();
            } else if (fromDate.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select From Date").showCustomAlert();
            } else if (toDate.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select To Date").showCustomAlert();
            } else if (fromTimeSpinner.getSelectedItem().toString().equalsIgnoreCase("00:00")) {
                new Custom_Toast(getApplicationContext(), "Please Select From Time").showCustomAlert();
            } else if (toTimeSpinner.getSelectedItem().toString().equalsIgnoreCase("00:00")) {
                new Custom_Toast(getApplicationContext(), "Please Select To Time").showCustomAlert();
            } else if (stateSpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--") || stateSpinner.getSelectedItem().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select From State").showCustomAlert();

            } else if (citySpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--") || citySpinner.getSelectedItem().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select From City").showCustomAlert();
            } else if (tostateSpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--") || stateSpinner.getSelectedItem().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select To State").showCustomAlert();

            } else if (tocitySpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--") || citySpinner.getSelectedItem().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select To City").showCustomAlert();
            } else if (conveyanseSpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--")) {
                new Custom_Toast(getApplicationContext(), "Please select a Conveyance Mode").showCustomAlert();
            } else {


                b = DateFunction.ConvertDateToTimestamp(billDate.getText().toString());
                t = DateFunction.ConvertDateToTimestamp(toDate.getText().toString());

                if (b < t) {
                    new Custom_Toast(getApplicationContext(), "Bill Date should be greater than or equal to ToDate").showCustomAlert();

                } else {

                    b = DateFunction.ConvertDateToTimestampWithTime(fromDate.getText().toString() + " " + fromTimeSpinner.getSelectedItem().toString());
                    t = DateFunction.ConvertDateToTimestampWithTime(toDate.getText().toString() + " " + toTimeSpinner.getSelectedItem().toString());

                    if (b > t) {
                        new Custom_Toast(getApplicationContext(), "To Date and Time should be greater than From Date and Time").showCustomAlert();
                    } else {

                        if (supportGSTINCheckBox.isChecked()) {
                            if (gstNoText.getText().toString().length() < 15) {
                                new Custom_Toast(getApplicationContext(), "Please enter 15 digit GSTIN No.").showCustomAlert();
                                return;
                            }
                   /* if(partyVendorName.getText().toString().isEmpty())
                    {
                        new Custom_Toast(getApplicationContext(), "Please Enter Vendor/Party Name").showCustomAlert();
                        return;
                    }*/
                            if (IGSTAmountLabel.getText().toString().equals("0")) {
                                new Custom_Toast(getApplicationContext(), "IGST Amount can not be 0.").showCustomAlert();
                                return;
                            }
                            if (IGSTAmountLabel.getText().toString().isEmpty()) {
                                if (CGSTAmountLabel.getText().toString().isEmpty()) {
                                    new Custom_Toast(getApplicationContext(), "Please Enter CGST Amount").showCustomAlert();
                                    return;
                                }
                                if (CGSTAmountLabel.getText().toString().equals("0")) {
                                    new Custom_Toast(getApplicationContext(), "CGST Amount can not be 0.").showCustomAlert();
                                    return;
                                }
                                if (SCGSTAmountLabel.getText().toString().isEmpty()) {
                                    new Custom_Toast(getApplicationContext(), "Please Enter SGST Amount").showCustomAlert();
                                    return;
                                }
                                if (SCGSTAmountLabel.getText().toString().equals("0")) {
                                    new Custom_Toast(getApplicationContext(), "SGST Amount can not be 0.").showCustomAlert();
                                    return;
                                }
                                if (!SCGSTAmountLabel.getText().toString().equals(CGSTAmountLabel.getText().toString())) {
                                    new Custom_Toast(getApplicationContext(), "CGST amount and SGST amount should be same.").showCustomAlert();
                                    return;
                                }
                            }
                            if (!supportAttach.isChecked()) {
                                new Custom_Toast(getApplicationContext(), "Please Select Supporting Attached Option.").showCustomAlert();
                                return;
                            }
                        }

                        if (supportAttach.isChecked()) {
                            if (partyVendorName.getText().toString().isEmpty()) {
                                new Custom_Toast(getApplicationContext(), "Please Enter Vendor/Party Name").showCustomAlert();
                                return;
                            }
                            if (billNo.getText().toString().isEmpty()) {
                                new Custom_Toast(getApplicationContext(), "Please Enter Bill No").showCustomAlert();
                                return;
                            }
                        }
                        expenseEntry = new ExpenseEntry();
                        expenseEntry.setCityId("0");
                        expenseEntry.setGSTReq(supportGSTINCheckBox.isChecked());
                        expenseEntry.setPartyVendorName(partyVendorName.getText().toString());
                        expenseEntry.setcGSTAmt(CGSTAmountLabel.getText().toString());
                        expenseEntry.setsGSTAmt(SCGSTAmountLabel.getText().toString());
                        expenseEntry.setIgstAmount(IGSTAmountLabel.getText().toString());
                        expenseEntry.setGstNo(gstNoText.getText().toString());
                        expenseEntry.setFromCity(cityList.get(citySpinner.getSelectedItemPosition()).getCity_id());
                        expenseEntry.setToCity(toCityList.get(tocitySpinner.getSelectedItemPosition()).getCity_id());
                        expenseEntry.setBillDate(billDate.getText().toString());
                        expenseEntry.setBillNumber(billNo.getText().toString());
                        expenseEntry.setBillAmount(amt.getText().toString());
                        expenseEntry.setClaimAmount(camt.getText().toString());
                        expenseEntry.setRemarks((remark.getText().toString().isEmpty() ? "" : remark.getText().toString()));
                        expenseEntry.setExpCode(expTypeCode);
                        expenseEntry.setExpenseTypeId(expTypeId);
                        expenseEntry.setExpenseGrpId(String.valueOf(grpId));
                        expenseEntry.setExpDetailId(String.valueOf(expDetailId));
                        expenseEntry.setIsSupportingAttached(supportAttach.isChecked());
                        expenseEntry.setFromDate(fromDate.getText().toString());
                        expenseEntry.setToDate(toDate.getText().toString());
                        expenseEntry.setTimeFrom(fromTimeSpinner.getSelectedItem().toString());
                        expenseEntry.setTimeTo(toTimeSpinner.getSelectedItem().toString());
                        expenseEntry.setTravelModeId(conveyanceList1.get(conveyanseSpinner.getSelectedItemPosition()).getId());
                        if (connectionDetector.isConnectingToInternet())

                        {
                            new SaveExpense().execute();

                        } else {

                            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                            dialogWithOutView.show(getFragmentManager(), "Info");
                        }
                    }
                }
            }
        } else {
            if (stateSpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--") || stateSpinner.getSelectedItem().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select State").showCustomAlert();

            } else if (citySpinner.getSelectedItem().toString().equalsIgnoreCase("--Select--") || citySpinner.getSelectedItem().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select City").showCustomAlert();
            } else if (billDate.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Select Bill Date").showCustomAlert();
            } else if (!checkBillDate()) {
                new Custom_Toast(getApplicationContext(), "Bill Date should be between Expense Group Dates").showCustomAlert();

            }
           /* else if (billNo.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Enter Bill No.").showCustomAlert();
            }*/
            else if (amt.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Enter Amount").showCustomAlert();
            } else if (Float.parseFloat(amt.getText().toString()) <= 0) {

                new Custom_Toast(getApplicationContext(), "Please enter Amount greater than 0").showCustomAlert();
            } else if (camt.getText().toString().isEmpty()) {
                new Custom_Toast(getApplicationContext(), "Please Enter Claim Amount").showCustomAlert();
            } else if (Float.parseFloat(camt.getText().toString()) > Float.parseFloat(amt.getText().toString())) {
                new Custom_Toast(getApplicationContext(), "Claim Amount should not be greater than Bill Amount").showCustomAlert();
            } else {
//            dialog.dismiss();

                if (supportGSTINCheckBox.isChecked()) {
                    if (gstNoText.getText().toString().length() < 15) {
                        new Custom_Toast(getApplicationContext(), "Please enter 15 digit GSTIN No.").showCustomAlert();
                        return;
                    }
                }
                if (supportGSTINCheckBox.isChecked() && (!expTypeCode.equalsIgnoreCase("BOARDING") && !expTypeCode.equalsIgnoreCase("Conveyance"))) {

                    if (gstNoText.getText().toString().length() < 15) {
                        new Custom_Toast(getApplicationContext(), "Please enter 15 digit GSTIN No.").showCustomAlert();
                        return;
                    }
                   /* if(partyVendorName.getText().toString().isEmpty())
                    {
                        new Custom_Toast(getApplicationContext(), "Please Enter Vendor/Party Name").showCustomAlert();
                        return;
                    }*/
                    if (IGSTAmountLabel.getText().toString().equals("0")) {
                        new Custom_Toast(getApplicationContext(), "IGST Amount can not be 0.").showCustomAlert();
                        return;
                    }
                    if (IGSTAmountLabel.getText().toString().isEmpty()) {
                        if (CGSTAmountLabel.getText().toString().isEmpty()) {
                            new Custom_Toast(getApplicationContext(), "Please Enter CGST Amount").showCustomAlert();
                            return;
                        }
                        if (CGSTAmountLabel.getText().toString().equals("0")) {
                            new Custom_Toast(getApplicationContext(), "CGST Amount can not be 0.").showCustomAlert();
                            return;
                        }
                        if (SCGSTAmountLabel.getText().toString().isEmpty()) {
                            new Custom_Toast(getApplicationContext(), "Please Enter SGST Amount").showCustomAlert();
                            return;
                        }
                        if (SCGSTAmountLabel.getText().toString().equals("0")) {
                            new Custom_Toast(getApplicationContext(), "SGST Amount can not be 0.").showCustomAlert();
                            return;
                        }
                        if (!SCGSTAmountLabel.getText().toString().equals(CGSTAmountLabel.getText().toString())) {
                            new Custom_Toast(getApplicationContext(), "CGST amount and SGST amount should be same.").showCustomAlert();
                            return;
                        }
                    }
                    if (!supportAttach.isChecked()) {
                        new Custom_Toast(getApplicationContext(), "Please Select Supporting Attached Option.").showCustomAlert();
                        return;
                    }
                }


                if (supportAttach.isChecked() && (!expTypeCode.equalsIgnoreCase("BOARDING") && !expTypeCode.equalsIgnoreCase("Conveyance"))) {
                    if (partyVendorName.getText().toString().isEmpty()) {
                        new Custom_Toast(getApplicationContext(), "Please Enter Vendor/Party Name").showCustomAlert();
                        return;
                    }
                    if (billNo.getText().toString().isEmpty()) {
                        new Custom_Toast(getApplicationContext(), "Please Enter Bill No").showCustomAlert();
                        return;
                    }
                }

                expenseEntry = new ExpenseEntry();
                expenseEntry.setGSTReq(supportGSTINCheckBox.isChecked());
                expenseEntry.setPartyVendorName(partyVendorName.getText().toString());
                expenseEntry.setcGSTAmt(CGSTAmountLabel.getText().toString());
                expenseEntry.setsGSTAmt(SCGSTAmountLabel.getText().toString());
                expenseEntry.setGstNo(gstNoText.getText().toString());
                expenseEntry.setIgstAmount(IGSTAmountLabel.getText().toString());
                expenseEntry.setCityId(cityList.get(citySpinner.getSelectedItemPosition()).getCity_id());
                expenseEntry.setBillDate(billDate.getText().toString());
                expenseEntry.setBillNumber(billNo.getText().toString());
                expenseEntry.setBillAmount(amt.getText().toString());
                expenseEntry.setClaimAmount(camt.getText().toString());
                expenseEntry.setRemarks((remark.getText().toString().isEmpty() ? "" : remark.getText().toString()));
                expenseEntry.setExpCode(expTypeCode);
                expenseEntry.setExpenseTypeId(expTypeId);
                expenseEntry.setExpenseGrpId(String.valueOf(grpId));
                expenseEntry.setExpDetailId(String.valueOf(expDetailId));
                expenseEntry.setIsSupportingAttached(supportAttach.isChecked());
                if (connectionDetector.isConnectingToInternet())

                {
                    new SaveExpense().execute();

                } else {

                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }

            }


        }


    }

    @Override
    public void setValues(ArrayList<CustomArrayAdopter.MyHolder> al) {

    }

    @Override
    public void holderListener(CustomArrayAdopter.MyHolder myHolder) {

    }


    @Override
    public void getPopupForUpdate(String expType) {

        expDetailId = Integer.parseInt(expType);
        if (connectionDetector.isConnectingToInternet())

        {
            new GetExpenseEntry().execute();

        } else {

            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

    }

    class GetExpenseEntry extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ExpenseSummaryScreen.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            // super.onPreExecute();
            // progressDialog= ProgressDialog.show(ExpenseSummaryScreen.this,"Loading Data", "Loading Please wait...", true);
            pdLoading.setMessage("\tLoading Please wait...");
            pdLoading.show();

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                expenseEntryObj = getExpenseEntry();
            } catch (Exception e) {
                // TODO Auto-generated catch block
               /* if(progressDialog!=null)
                {
                    //progressDialog.dismiss();
                }*/
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
           /* if(progressDialog.isShowing())
            {
               // progressDialog.dismiss();
            }*/
            if (expenseEntryObj != null) {
                int selectIndex = 0;

                for (int j = 0; j < expenseTypeList.size(); j++) {
                    if (expenseTypeList.get(j).getId().equals(expenseEntryObj.getExpenseTypeId())) {
                        expTypeCode = expenseTypeList.get(j).getExpcd();
                        expTypeName = expenseTypeList.get(j).getName();
                    }
                }

                expTypeId = expenseEntryObj.getExpenseTypeId();
                FromUpdate = true;
                if (connectionDetector.isConnectingToInternet())

                {
                    new GetStates().execute();

                } else {

                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }
//                initiatePopUpWindow(expTypeSpinner.getSelectedItem().toString(),stateArrayList,stateList,expenseEntryObj);
            }
            super.onPostExecute(result);
            pdLoading.dismiss();

        }
    }

    class GetStates extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(ExpenseSummaryScreen.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pdLoading.setMessage("\tLoading Please wait...");
            pdLoading.show();
//            progressDialog= ProgressDialog.show(ExpenseGroupEntry.this,"saving Data", "Saving Data Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {

                stateList = getStateList();
                if (expTypeName.equalsIgnoreCase("TRAVEL")) {
                    if (stateList != null) {
                        tostateList = new ArrayList<State>();
                        tostateList.addAll(stateList);
                    }
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                if (expTypeName.equalsIgnoreCase("CONVEYANCE - TRAVEL") || expTypeName.equalsIgnoreCase("TRAVEL")) {
                    conveyanceList1 = getConveyanceModeList(expTypeName);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                if (expTypeCode.equalsIgnoreCase("CONVEYANCE")) {
                    allowToChangeCity = getAllowChangeCity();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                if (expTypeCode.equalsIgnoreCase("CONVEYANCE") || expTypeCode.equalsIgnoreCase("CONVEYANCETRAVEL")) {
                    stateCityId = getConveyanceCityId();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                if (stateCityId == null) {
                    stateCityId = new ArrayList<Integer>();
                    stateCityId.add(0);
                    stateCityId.add(0);

                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (stateList != null) {
                stateArrayList = new ArrayList<String>();
                for (int i = 0; i < stateList.size(); i++) {
                    stateArrayList.add(stateList.get(i).getState_name());
                }

                if (conveyanceList1 != null) {
                    conveyanceArrayList = new ArrayList<String>();
                    for (int i = 0; i < conveyanceList1.size(); i++) {
                        conveyanceArrayList.add(conveyanceList1.get(i).getName());
                    }
                }
                if (expTypeCode.equalsIgnoreCase("BOARDING")) {
                    initiatePopUpWindow("BOARDING (FOOD)", stateArrayList, stateList, expenseEntryObj, conveyanceArrayList);
                } else if (expTypeCode.equalsIgnoreCase("CAPITALPURCHASE")) {
                    initiatePopUpWindow("CAPITAL / ASSET PURCHASE", stateArrayList, stateList, expenseEntryObj, conveyanceArrayList);
                } else if (expTypeCode.equalsIgnoreCase("COMMUNICATION")) {
                    initiatePopUpWindow("COMMUNICATION - FAX - INTERNET", stateArrayList, stateList, expenseEntryObj, conveyanceArrayList);
                } else if (expTypeCode.equalsIgnoreCase("CONVEYANCE")) {
                    initiatePopUpWindow("CONVEYANCE", stateArrayList, stateList, expenseEntryObj, conveyanceArrayList);
                } else if (expTypeCode.equalsIgnoreCase("CONVEYANCETRAVEL")) {
                    initiatePopUpWindow("CONVEYANCE - TRAVEL", stateArrayList, stateList, expenseEntryObj, conveyanceArrayList);
                } else if (expTypeCode.equalsIgnoreCase("ENTERTAINMENT")) {
                    initiatePopUpWindow("ENTERTAINMENT", stateArrayList, stateList, expenseEntryObj, conveyanceArrayList);
                } else if (expTypeCode.equalsIgnoreCase("FOODSTAFF")) {
                    initiatePopUpWindow("FOOD WITH STAFF", stateArrayList, stateList, expenseEntryObj, conveyanceArrayList);
                } else if (expTypeCode.equalsIgnoreCase("LAUNDRY")) {
                    initiatePopUpWindow("LAUNDRY", stateArrayList, stateList, expenseEntryObj, conveyanceArrayList);
                } else if (expTypeCode.equalsIgnoreCase("OTHER") && expTypeName.equalsIgnoreCase("OTHER EXPENSE")) {

                    initiatePopUpWindow("OTHER EXPENSE", stateArrayList, stateList, expenseEntryObj, conveyanceArrayList);
                } else if (expTypeCode.equalsIgnoreCase("OTHER") && expTypeName.equalsIgnoreCase("OTHERS - TRAVEL")) {

                    initiatePopUpWindow("OTHERS - TRAVEL", stateArrayList, stateList, expenseEntryObj, conveyanceArrayList);
                } else if (expTypeCode.equalsIgnoreCase("COURIER")) {
                    initiatePopUpWindow("POST & COURIER", stateArrayList, stateList, expenseEntryObj, conveyanceArrayList);
                } else if (expTypeCode.equalsIgnoreCase("STATIONARY")) {
                    initiatePopUpWindow("STATIONARY - PHOTOCOPY", stateArrayList, stateList, expenseEntryObj, conveyanceArrayList);
                } else if (expTypeCode.equalsIgnoreCase("LODGING")) {
                    initiatePopUpWindow("LODGING (STAY)", stateArrayList, stateList, expenseEntryObj, conveyanceArrayList);
                } else if (expTypeCode.equalsIgnoreCase("TRAVEL")) {
                    tostateArrayList = new ArrayList<String>();
                    for (int i = 0; i < tostateList.size(); i++) {
                        tostateArrayList.add(tostateList.get(i).getState_name());
                    }

                    initiateTravelPopUpWindow("TRAVEL", stateArrayList, stateList, tostateArrayList, tostateList, expenseEntryObj, conveyanceArrayList);
                }

            } else {
                new Custom_Toast(getApplicationContext(), "Please contact admin for state/city not alloted!").showCustomAlert();
            }
            pdLoading.dismiss();

        }
    }

    class GetCityList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            //    progressDialog= ProgressDialog.show(ExpenseSummaryScreen.this,"Loading Data!", "Loading Please wait...", true);

            stId = stateList.get(stateSpinner.getSelectedItemPosition()).getState_id();
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                cityList = getCityList(stId);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (cityList != null) {
                cityArrayList = new ArrayList<String>();

                for (int i = 0; i < cityList.size(); i++) {
                    cityArrayList.add(cityList.get(i).getCity_name());
                }

                cityAdapter = new ArrayAdapter<String>(ExpenseSummaryScreen.this, R.layout.simple_spinner_dropdown_item, cityArrayList);
                cityAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                citySpinner.setAdapter(cityAdapter);

                if (expTypeCode.equalsIgnoreCase("CONVEYANCE") || expTypeCode.equalsIgnoreCase("CONVEYANCETRAVEL")) {
                    int selectIndex = 0;

                    for (int j = 0; j < cityList.size(); j++) {
                        if (cityList.get(j).getCity_id().equals(String.valueOf(stateCityId.get(1)))) {
                            selectIndex = j;
                        }
                    }
                    citySpinner.setSelection(selectIndex);

                }
                if (expTypeCode.equalsIgnoreCase("TRAVEL")) {
                    if (expenseEntryObj != null) {

                        int selectIndex = 0;
                        for (int j = 0; j < cityList.size(); j++) {
                            if (cityList.get(j).getCity_id().equals(expenseEntryObj.getFromCity())) {
                                selectIndex = j;
                            }
                        }
                        citySpinner.setSelection(selectIndex);
                    }
                } else {
                    if (expenseEntryObj != null) {
                        int selectIndex = 0;

                        for (int j = 0; j < cityList.size(); j++) {
                            if (cityList.get(j).getCity_id().equals(expenseEntryObj.getCityId())) {
                                selectIndex = j;
                            }
                        }
                        citySpinner.setSelection(selectIndex);
                    }
                }

                if (expTypeCode.equalsIgnoreCase("CONVEYANCE")) {
                    if (allowToChangeCity) {
                        stateSpinner.setEnabled(true);
                        citySpinner.setEnabled(true);
                    } else {
                        stateSpinner.setEnabled(false);
                        citySpinner.setEnabled(false);
                    }
                }
            }
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        }
    }


    class GetToCityList extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ExpenseSummaryScreen.this, "Loading Data", "Loading Data Please wait...", true);
            tostId = stateList.get(tostateSpinner.getSelectedItemPosition()).getState_id();
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                toCityList = getCityList(tostId);
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
            if (toCityList != null) {
                tocityArrayList = new ArrayList<String>();
                for (int i = 0; i < toCityList.size(); i++) {
                    tocityArrayList.add(toCityList.get(i).getCity_name());
                }
                tocityAdapter = new ArrayAdapter<String>(ExpenseSummaryScreen.this, R.layout.simple_spinner_dropdown_item, tocityArrayList);
                tocityAdapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item);
                tocitySpinner.setAdapter(tocityAdapter);
            }

            if (expTypeCode.equalsIgnoreCase("TRAVEL")) {
                if (expenseEntryObj != null) {
                    int selectIndex = 0;
                    int selectToIndex = 0;
                    for (int j = 0; j < toCityList.size(); j++) {
                        if (toCityList.get(j).getCity_id().equals(expenseEntryObj.getToCity())) {
                            selectToIndex = j;
                        }
                    }
                    tocitySpinner.setSelection(selectToIndex);
                }
                expenseEntryObj = null;
            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    class SubmitExpense extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ExpenseSummaryScreen.this, "Submit Expense", "Submit Expense Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                DeviceInfoController deviceInfoController = new DeviceInfoController(ExpenseSummaryScreen.this);
                deviceInfoController.open();
                String pda_id = deviceInfoController.getpdaId();
                deviceInfoController.close();
                submitExpense(String.valueOf(grpId), smid, userId, pda_id);

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
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }


        }
    }


    class ExpenseType extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ExpenseSummaryScreen.this, "Loading Data", "Loading Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                expenseTypeList = getExpenseType();
                EmpGracePeriod = getGracePeriod();

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
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (expenseTypeList != null) {

                expenseTypeArrayList = new ArrayList<String>();
                for (int i = 0; i < expenseTypeList.size(); i++) {
                    expenseTypeArrayList.add(expenseTypeList.get(i).getName());
                }


                expTypeAdapter = new ArrayAdapter<String>(ExpenseSummaryScreen.this, android.R.layout.simple_spinner_item, expenseTypeArrayList);
                expTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                expTypeSpinner.setAdapter(expTypeAdapter);
                if (connectionDetector.isConnectingToInternet())

                {
                    new ExpenseEntryType().execute();

                } else {

                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }


            }

        }
    }

    class ExpenseEntryType extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ExpenseSummaryScreen.this, "Loading Data", "Loading Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {

                expenseEntryList = getExpenseEntryList();


            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
            if (expenseEntryList != null) {
                if (expenseEntryList.size() > 0) {
                    submit.setVisibility(View.VISIBLE);
                    dbCon.open();
                    submit.setEnabled(dbCon.ButtonEnable("AddExpense", "Expense", "Add"));
                    if (!dbCon.ButtonEnable("AddExpense", "Expense", "Add")) {
                        submit.setColorFilter(Color.parseColor("#808080"));
                    }
                    dbCon.close();
                } else {
                    submit.setVisibility(View.GONE);
                }

                customArrayAdopter = new CustomArrayAdopter(ExpenseSummaryScreen.this, feedDahboardItemData(expenseEntryList), R.layout.expense_layout_row, R.id.Text1, ExpenseSummaryScreen.this, ExpenseSummaryScreen.this);
                listView.setAdapter(customArrayAdopter);
            }
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    private ArrayList<DashboardModel> feedDahboardItemData(ArrayList<ExpenseEntry> expenseEntryListList) {
        ArrayList<DashboardModel> dashboardModels = new ArrayList<DashboardModel>();
        DashboardModel model = new DashboardModel();
        for (int i = 0; i < expenseEntryListList.size(); i++) {
            dashboardModels.add(model.expenseEntryListFind(
                    expenseEntryListList.get(i).getExpDetailId(),
                    expenseEntryListList.get(i).getExpCode(),
                    expenseEntryListList.get(i).getCityId(),
                    expenseEntryListList.get(i).getBillNumber(),
                    expenseEntryListList.get(i).getBillDate(),
                    expenseEntryListList.get(i).getBillAmount(),
                    expenseEntryListList.get(i).getClaimAmount(),
                    (expenseEntryListList.get(i).getIsSupportingAttached() ? "Yes" : "No"),
                    expenseEntryListList.get(i).getGstNo(),
                    expenseEntryListList.get(i).getPartyVendorName(),
                    expenseEntryListList.get(i).getsGSTAmt(),
                    expenseEntryListList.get(i).getcGSTAmt(),
                    (expenseEntryListList.get(i).isGSTReq() ? "Yes" : "No"), expenseEntryListList.get(i).getIgstAmount()));
        }

        return dashboardModels;
    }

    class SaveExpense extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ExpenseSummaryScreen.this, "saving Data", "Saving Data Please wait...", true);
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                if (expenseEntry != null) {
                    delResp = insertExpense(expenseEntry);

                }

//                expenseTypeList=getExpenseType();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

                e.printStackTrace();
            }
            return delResp;
        }

        @Override
        protected void onPostExecute(String response1) {
            // TODO Auto-generated method stub
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            try {
                JSONArray jsonarray = new JSONArray(response1);
                JSONObject json = null;
                for (int k = 0; k < jsonarray.length(); k++) {
                    json = jsonarray.getJSONObject(k);
                    // System.out.println(json);
                }
                int st;
                try {
                    st = Integer.parseInt(json.getString("St"));
                    //System.out.println(st);
                    if (st > 0) {
                        progressDialog.dismiss();
                        dialog.dismiss();
                        if (connectionDetector.isConnectingToInternet())

                        {
                            new ExpenseEntryType().execute();

                        } else {

                            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                            dialogWithOutView.show(getFragmentManager(), "Info");
                        }

                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putString("EXP_GRP_NAME", grpName);
                        bundle.putString("EXP_GRP_FDATE", fDate);
                        bundle.getString("EXP_GRP_TDATE", tDate);
                        bundle.getInt("EXP_GRP_ID", grpId);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if (st == 0) {
                        progressDialog.dismiss();
//                   new Custom_Toast(getApplicationContext(), "Expense Group not saved").showCustomAlert();
                        alertOkDialog = AlertOkDialog.newInstance("Expense not saved");
                        alertOkDialog.show(getFragmentManager(), "error");


                    }


                } catch (Exception e) {
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }

                    mSuccess = true;
                    System.out.println(e);

                }

            } catch (Exception e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }

                mSuccess = true;
                System.out.println(e);

            }
        }

    }

    String insertExpense(ExpenseEntry expense) {
        boolean mSuccess = false;
        String response1 = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/xJSInsertNewExpense");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("ExpCode", expense.getExpCode()));
            nameValuePairs.add(new BasicNameValuePair("ExpDetailId", expense.getExpDetailId()));
            nameValuePairs.add(new BasicNameValuePair("smid", preferences1.getString("CONPERID_SESSION", "0")));
            nameValuePairs.add(new BasicNameValuePair("ExpenseTypeId", expense.getExpenseTypeId()));
            nameValuePairs.add(new BasicNameValuePair("BillNumber", expense.getBillNumber()));
            nameValuePairs.add(new BasicNameValuePair("BillDate", expense.getBillDate()));
            nameValuePairs.add(new BasicNameValuePair("FromCity", (expense.getFromCity() == null ? "0" : expense.getFromCity())));
            nameValuePairs.add(new BasicNameValuePair("ToCity", (expense.getToCity() == null ? "0" : expense.getToCity())));
            nameValuePairs.add(new BasicNameValuePair("CityId", expense.getCityId()));
            nameValuePairs.add(new BasicNameValuePair("FromDate", (expense.getFromDate() == null ? "" : expense.getFromDate())));
            nameValuePairs.add(new BasicNameValuePair("ToDate", (expense.getToDate() == null ? "" : expense.getToDate())));
            nameValuePairs.add(new BasicNameValuePair("Remarks", expense.getRemarks()));
            nameValuePairs.add(new BasicNameValuePair("ClaimAmount", expense.getClaimAmount()));
            nameValuePairs.add(new BasicNameValuePair("BillAmount", expense.getBillAmount()));
            nameValuePairs.add(new BasicNameValuePair("UserId", preferences1.getString("USER_ID", "0")));
            nameValuePairs.add(new BasicNameValuePair("IsSupportingAttached", String.valueOf(expense.getIsSupportingAttached())));
            nameValuePairs.add(new BasicNameValuePair("TravelModeId", (expense.getTravelModeId() == null ? "0" : expense.getTravelModeId())));
            nameValuePairs.add(new BasicNameValuePair("ExpenseGrpId", expense.getExpenseGrpId()));
            nameValuePairs.add(new BasicNameValuePair("StayWithRelative", String.valueOf(expense.getStayWithRelative())));
            nameValuePairs.add(new BasicNameValuePair("TimeFrom", (expense.getTimeFrom() == null ? "" : expense.getTimeFrom())));
            nameValuePairs.add(new BasicNameValuePair("TimeTo", (expense.getTimeTo() == null ? "" : expense.getTimeTo())));
            nameValuePairs.add(new BasicNameValuePair("KmVisited", (expense.getKmVisited() == null ? "0" : expense.getKmVisited())));
            nameValuePairs.add(new BasicNameValuePair("PerKmRate", (expense.getRate() == null ? "0" : expense.getRate())));
            nameValuePairs.add(new BasicNameValuePair("GSTNO", expense.getGstNo()));
            nameValuePairs.add(new BasicNameValuePair("gst_required", String.valueOf(expense.isGSTReq())));
            nameValuePairs.add(new BasicNameValuePair("vendor_name", expense.getPartyVendorName()));
            nameValuePairs.add(new BasicNameValuePair("CGST_AMT", expense.getcGSTAmt()));
            nameValuePairs.add(new BasicNameValuePair("SGST_AMT", expense.getsGSTAmt()));
            nameValuePairs.add(new BasicNameValuePair("IGST_AMT", expense.getIgstAmount()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response1 = httpclient.execute(httppost, responseHandler);
            System.out.println("Response : " + response1);
        } catch (Exception e) {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            System.out.println("Exception : " + e.getMessage());
            mSuccess = true;
        }
        return response1;
    }

    boolean submitExpense(String grpId, String smid, String userId, String pda_id) {
        boolean mSuccess = false;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/xJSInsertApprovalForExpense");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("Expnsgrpid", grpId));
            nameValuePairs.add(new BasicNameValuePair("smid", smid));
            nameValuePairs.add(new BasicNameValuePair("userid", userId));
            nameValuePairs.add(new BasicNameValuePair("host", ""));
            nameValuePairs.add(new BasicNameValuePair("device", pda_id));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response1 = httpclient.execute(httppost, responseHandler);
            // System.out.println("Response : " + response1);
            JSONArray jsonarray = new JSONArray(response1);
            JSONObject json = null;
            for (int k = 0; k < jsonarray.length(); k++) {
                json = jsonarray.getJSONObject(k);
                // System.out.println(json);
            }
            int st;
            try {
                st = Integer.parseInt(json.getString("St"));
                //System.out.println(st);
                if (st == 1) {
                    progressDialog.dismiss();

                    (new IntentSend(getApplicationContext(), AddExpense.class)).toSendAcivity();

                } else if (st == 0) {
                    progressDialog.dismiss();
                    alertOkDialog = AlertOkDialog.newInstance("Expense not submitted");
                    alertOkDialog.show(getFragmentManager(), "error");
                } else if (st == -1) {
                    progressDialog.dismiss();
                    alertOkDialog = AlertOkDialog.newInstance("This expense group cannot be submitted as the expense group with the forward dates has been already submitted or approved");
                    alertOkDialog.show(getFragmentManager(), "Info");
                }


            } catch (Exception e) {
                mSuccess = true;
                System.out.println(e);

            }

        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            mSuccess = true;
        }

        return mSuccess;

    }


    ArrayList<Expense> getExpenseType() {
        String url = "http://" + server + "/And_Sync.asmx/xJSGetExpenseType";
        JSONParser jParser = new JSONParser();
        ArrayList<Expense> expenseTypeList = new ArrayList<Expense>();
        String result = jParser.getJSONArray(url);
        System.out.println("First Service Call" + url + "-" + result);
        if (result != null) {
            try {

                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    Expense exp = new Expense();
                    exp.setId(obj.getString("id"));
                    exp.setName(obj.getString("Nm"));
                    exp.setExpcd(obj.getString("Expcd"));
                    expenseTypeList.add(exp);

                }

            } catch (Exception e) {
                System.out.println(e);
            }


        }
        return expenseTypeList;

    }

    ArrayList<ExpenseEntry> getExpenseEntryList() {
        String url = "http://" + server + "/And_Sync.asmx/xJSGetExpenseList?groupid=" + grpId;
        JSONParser jParser = new JSONParser();
        ArrayList<ExpenseEntry> expenseTypeList = new ArrayList<ExpenseEntry>();
        String result = jParser.getJSONArray(url);
        System.out.println("First Service Call Thired" + url + "-" + result);
        if (result != null) {
            try {

                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    ExpenseEntry exp = new ExpenseEntry();
                    exp.setExpDetailId(obj.getString("ExpenseDetailID"));
                    exp.setExpCode(obj.getString("Expensetype"));
                    exp.setCityId(obj.getString("City"));
                    exp.setBillNumber(obj.getString("Bill"));
                    exp.setBillDate(obj.getString("Dt"));
                    exp.setBillAmount(obj.getString("Amount"));
                    exp.setClaimAmount(obj.getString("ClaimAmt"));
                    exp.setGstNo(obj.getString("GSTNO"));
                    exp.setIsSupportingAttached(obj.getBoolean("SupprAttac"));
                    exp.setGSTReq(obj.getBoolean("gst_required"));
                    exp.setPartyVendorName(obj.getString("vendor_name"));
                    exp.setsGSTAmt(obj.getString("SGST_AMT"));
                    exp.setcGSTAmt(obj.getString("CGST_AMT"));
                    exp.setIgstAmount(obj.getString("IGST_AMT"));


                    expenseTypeList.add(exp);

                }

            } catch (Exception e) {
                System.out.println(e);
            }


        }

//        if(expenseTypeList.size()>0)
//        {
//            submit.setVisibility(View.VISIBLE);
//        }
//        else{
//            submit.setVisibility(View.GONE);
//        }

        return expenseTypeList;

    }

    int getGracePeriod() {
        String url = "http://" + server + "/And_Sync.asmx/JSGetEnviroSetting";
        JSONParser jParser = new JSONParser();
        int g = 0;
        String result = jParser.getJSONArray(url);
        System.out.println("First Service Call Second" + url + "-" + result);
        if (result != null) {
            try {

                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    g = Integer.parseInt(obj.getString("EmpGraceDays"));
                    System.out.println(g);
                }

            } catch (Exception e) {
                System.out.println(e);
            }


        }
        return g;

    }


    ArrayList<Integer> getConveyanceCityId() {
        ArrayList<Integer> stateCityId = new ArrayList<Integer>();
        String url = "http://" + server + "/And_Sync.asmx/xJGetExpenseConveyanceCity?&SMID=" + smid;
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        if (result != null) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    stateCityId.add(Integer.parseInt(obj.getString("Sid")));
                    stateCityId.add(Integer.parseInt(obj.getString("id")));

                }
            } catch (Exception e) {
                System.out.println(e);
                mSuccess = true;
            }
        }

        return stateCityId;
    }

    ArrayList<City> getCityList(String stId) {
        ArrayList<City> cityLists = new ArrayList<City>();
        String url = "";
        if (expTypeCode.equalsIgnoreCase("CONVEYANCE")) {
            if (FromUpdate) {
                url = "http://" + server + "/And_Sync.asmx/xJSCityForExpense?stateid=" + stId + "&SMID=" + smid + "&Expensetype=Conveyance";
            } else {

//                url = "http://" + server + "/And_Sync.asmx/xJSCityForExpense?stateid=" + stId +"&SMID="+smid+"&Expensetype=0";
                url = "http://" + server + "/And_Sync.asmx/xJSCityForExpense?stateid=" + stId + "&SMID=" + smid + "&Expensetype=Conveyance";
            }
        } else if (expTypeCode.equalsIgnoreCase("CONVEYANCETRAVEL")) {
            url = "http://" + server + "/And_Sync.asmx/xJSCityForExpense?stateid=" + stId + "&SMID=" + smid + "&Expensetype=CONVEYANCETRAVEL";
        } else {
            url = "http://" + server + "/And_Sync.asmx/xJSCityForExpense?stateid=" + stId + "&SMID=" + smid + "&Expensetype=0";
        }
        JSONParser jParser = new JSONParser();
        City cityList1 = new City();
        cityList1.setCity_id("0");
        cityList1.setCity_name("--Select--");
        cityLists.add(cityList1);
        String result = jParser.getJSONArray(url);
        Log.e("URL", url);
        System.out.println("DataGetCity" + result);
        if (result != null) {
            try {

                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    City cityList = new City();
                    cityList.setCity_id(obj.getString("id"));
                    cityList.setCity_name(obj.getString("Nm"));
                    cityList.setAmount(obj.getString("Amt"));
                    cityLists.add(cityList);
                }

            } catch (Exception e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                System.out.println(e);
                mSuccess = true;
            }


        }

        return cityLists;
    }


    ArrayList<Vehicle> getConveyanceModeList(String expTypeName) {
        String url = "";
        if (expTypeName.equalsIgnoreCase("TRAVEL")) {
            url = "http://" + server + "/And_Sync.asmx/xJSGetConveynaceMode?type=Travel";
        } else if (expTypeName.equalsIgnoreCase("CONVEYANCE - TRAVEL")) {
            url = "http://" + server + "/And_Sync.asmx/xJSGetConveynaceMode?type=CONVEYANCETRAVEL";
        }

        JSONParser jParser = new JSONParser();
        ArrayList<Vehicle> convLists = new ArrayList<Vehicle>();
        Vehicle vehicle = new Vehicle();
        vehicle.setName("--Select--");
        vehicle.setId("0");
        convLists.add(vehicle);
        String result = jParser.getJSONArray(url);
        //System.out.println("Conveyance Travel"+url)
        // System.out.println("Conveyance Travel Data"+result);
        if (result != null) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    Vehicle vehicle1 = new Vehicle();
                    vehicle1.setName(obj.getString("Conveyance"));
                    vehicle1.setId(obj.getString("Id"));
                    //Write By Sandeep Singh 06-03-2017
                    //Start
                    vehicle1.setRate(obj.getString("PerKmrate"));

                    //END
                    convLists.add(vehicle1);
                }

            } catch (Exception e) {
                System.out.println(e);
                mSuccess = true;
            }

        }
        return convLists;
    }

    ArrayList<State> getStateList() {
        ArrayList<State> stateLists = new ArrayList<State>();
        String url = "";
        //System.out.println("Spinner Data"+expTypeCode);
        if (expTypeCode.equalsIgnoreCase("CONVEYANCE")) {
            url = "http://" + server + "/And_Sync.asmx/xJSStateForExpense?&SMID=" + smid + "&Expensetype=Conveyance";
        } else if (expTypeCode.equalsIgnoreCase("CONVEYANCETRAVEL")) {
            url = "http://" + server + "/And_Sync.asmx/xJSStateForExpense?&SMID=" + smid + "&Expensetype=CONVEYANCETRAVEL";
        } else {
            url = "http://" + server + "/And_Sync.asmx/xJSStateForExpense?&SMID=" + smid + "&Expensetype=0";
        }
        JSONParser jParser = new JSONParser();
        if (!expTypeCode.equalsIgnoreCase("CONVEYANCETRAVEL")) {
            State stateList1 = new State();
            stateList1.setState_id("0");
            stateList1.setState_name("--Select--");
            stateLists.add(stateList1);
        }


        String result = jParser.getJSONArray(url);
        //System.out.println("Conveyance Travel"+url);
        //System.out.println("Conveyance Travel Data"+result);
        if (result != null) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    State stateList = new State();
                    stateList.setState_id(obj.getString("id"));
                    stateList.setState_name(obj.getString("Nm"));
                    stateLists.add(stateList);
                }
            } catch (Exception e) {
                System.out.println(e);
                mSuccess = true;
            }
        }

        return stateLists;
    }

    boolean getAllowChangeCity() {
        boolean allow = false;
        String url = "http://" + server + "/And_Sync.asmx/xJAllowChangeCity?&SMID=" + smid;
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        if (result != null) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);

                    allow = Boolean.parseBoolean(obj.getString("allowcity"));
                }
            } catch (Exception e) {
                System.out.println(e);
                mSuccess = true;
            }
        }

        return allow;
    }

    ExpenseEntry getExpenseEntry() {
        String url = "http://" + server + "/And_Sync.asmx/xJSGetExpenseListDetails?Expnsdetailid=" + expDetailId;
        JSONParser jParser = new JSONParser();
        ExpenseEntry ExpenseEntry = null;
        String result = jParser.getJSONArray(url);
        if (result != null) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    ExpenseEntry = new ExpenseEntry();
                    ExpenseEntry.setExpenseTypeId(obj.getString("ExpenseTypeID"));
                    ExpenseEntry.setExpenseGrpId(obj.getString("ExpenseGroupID"));
                    ExpenseEntry.setTravelModeId(obj.getString("TravelModeID"));
                    ExpenseEntry.setIsSupportingAttached(Boolean.parseBoolean(obj.getString("IsSupportingAttached")));
                    ExpenseEntry.setFromCity(obj.getString("FromCity"));
                    ExpenseEntry.setToCity(obj.getString("ToCity"));
                    ExpenseEntry.setClaimAmount(obj.getString("ClaimAmount"));
                    getWebAmount = obj.getString("ClaimAmount");
                    ExpenseEntry.setFromDate(obj.getString("FromDate"));
                    ExpenseEntry.setToDate(obj.getString("ToDate"));
                    ExpenseEntry.setBillDate(obj.getString("BillDate"));
                    ExpenseEntry.setRemarks(obj.getString("Remarks"));
                    ExpenseEntry.setBillAmount(obj.getString("BillAmount"));
                    ExpenseEntry.setStayWithRelative(Boolean.parseBoolean((obj.getString("StayWithRelative").equals("1") ? "True" : "False")));
                    ExpenseEntry.setTimeFrom(obj.getString("FromTime"));
                    ExpenseEntry.setCityId(obj.getString("CityID"));
                    ExpenseEntry.setTimeTo(obj.getString("ToTime"));
                    ExpenseEntry.setBillNumber(obj.getString("BillNo"));
                    ExpenseEntry.setKmVisited(obj.getString("KmVisit"));
                    ExpenseEntry.setRate(obj.getString("PerKmRate"));
                    ExpenseEntry.setGSTReq(Boolean.parseBoolean(obj.getString("gst_required")));
                    ExpenseEntry.setPartyVendorName(obj.getString("vendor_name"));
                    ExpenseEntry.setcGSTAmt(obj.getString("CGST_AMT"));
                    ExpenseEntry.setsGSTAmt(obj.getString("SGST_AMT"));
                    ExpenseEntry.setGstNo(obj.getString("GSTINNo"));
                    ExpenseEntry.setIgstAmount(obj.getString("IGST_AMT"));

                }
            } catch (Exception e) {
                System.out.println(e);
                mSuccess = true;
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }

            if (ExpenseEntry != null) {
                url = "http://" + server + "/And_Sync.asmx/xJSStateForExpensedetails?cityid=" + (ExpenseEntry.getCityId() == null ? "-1" : ExpenseEntry.getCityId());
                jParser = new JSONParser();
                result = jParser.getJSONArray(url);
                if (result != null) {
                    try {
                        JSONArray jsonarray = new JSONArray(result);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            ExpenseEntry.setStateid(obj.getString("id"));
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        mSuccess = true;
                    }

                }
                url = "http://" + server + "/And_Sync.asmx/xJSStateForExpensedetails?cityid=" + (ExpenseEntry.getFromCity() == null ? "-1" : ExpenseEntry.getFromCity());
                jParser = new JSONParser();
                result = jParser.getJSONArray(url);
                if (result != null) {
                    try {
                        JSONArray jsonarray = new JSONArray(result);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            ExpenseEntry.setFromstate(obj.getString("id"));
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        mSuccess = true;
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }

                }
                url = "http://" + server + "/And_Sync.asmx/xJSStateForExpensedetails?cityid=" + (ExpenseEntry.getToCity() == null ? "-1" : ExpenseEntry.getToCity());
                jParser = new JSONParser();
                result = jParser.getJSONArray(url);
                if (result != null) {
                    try {
                        JSONArray jsonarray = new JSONArray(result);
                        for (int i = 0; i < jsonarray.length(); i++) {
                            JSONObject obj = jsonarray.getJSONObject(i);
                            ExpenseEntry.setTostate(obj.getString("id"));
                        }
                    } catch (Exception e) {
                        System.out.println(e);
                        mSuccess = true;
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }

                }

            }
        }
        return ExpenseEntry;
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        (new IntentSend(getApplicationContext(), AddExpense.class)).toSendAcivity();
        /* (new IntentSend(getApplicationContext(),DashBoradActivity.class)).toSendAcivity();*/
    }

    ArrayList<String> getTimeList() {
        ArrayList<String> timeList = new ArrayList<String>();
        timeList.add("00:00");
        timeList.add("00:30");
        timeList.add("01:00");
        timeList.add("01:30");
        timeList.add("02:00");
        timeList.add("02:30");
        timeList.add("03:00");
        timeList.add("03:30");
        timeList.add("04:00");
        timeList.add("04:30");
        timeList.add("05:00");
        timeList.add("05:30");
        timeList.add("06:00");
        timeList.add("06:30");
        timeList.add("07:00");
        timeList.add("07:30");
        timeList.add("08:00");
        timeList.add("08:30");
        timeList.add("09:00");
        timeList.add("09:30");
        timeList.add("10:00");
        timeList.add("10:30");
        timeList.add("11:00");
        timeList.add("11:30");
        timeList.add("12:00");
        timeList.add("12:30");
        timeList.add("13:00");
        timeList.add("13:30");
        timeList.add("14:00");
        timeList.add("14:30");
        timeList.add("15:00");
        timeList.add("15:30");
        timeList.add("16:00");
        timeList.add("16:30");
        timeList.add("17:00");
        timeList.add("17:30");
        timeList.add("18:00");
        timeList.add("18:30");
        timeList.add("19:00");
        timeList.add("19:30");
        timeList.add("20:00");
        timeList.add("20:30");
        timeList.add("21:00");
        timeList.add("21:30");
        timeList.add("22:00");
        timeList.add("22:30");
        timeList.add("23:00");
        timeList.add("23:30");
        return timeList;


    }

    String getCurrentDate() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy");
        String c1 = df.format(c.getTime());
        return c1;

    }

    boolean checkBillDate() {
        long f, t, b;

        f = DateFunction.ConvertDateToTimestamp(fDate);
        t = DateFunction.ConvertDateToTimestamp(tDate);
        b = DateFunction.ConvertDateToTimestamp(billDate.getText().toString());
        if (b >= f && b <= t) {
            return true;

        } else {

            return false;

        }

    }

    class DeleteExpense extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ExpenseSummaryScreen.this, "Delete Expense", "Deleting Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                delResp = deletetExpense();

            } catch (Exception e) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return delResp;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub

            if (delResp != null) {
                new Custom_Toast(getApplicationContext(), delResp).showCustomAlert();

                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                if (dialog != null) {
                    dialog.dismiss();
                }
                if (connectionDetector.isConnectingToInternet())

                {
                    new ExpenseEntryType().execute();

                } else {

                    dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }
            }

        }
    }

    String deletetExpense() {
        boolean mSuccess = false;
        String response1 = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://" + server + "/And_Sync.asmx/xJSExpenseDelete");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("ExpGrpId", String.valueOf(grpId)));
            nameValuePairs.add(new BasicNameValuePair("ExpDetailId", String.valueOf(expDetailId)));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            response1 = httpclient.execute(httppost, responseHandler);
            //System.out.println("Response : " + response1);

        } catch (Exception e) {
            delResp = "";
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

            mSuccess = true;
            System.out.println(e);

        }

        return response1;
    }

    void refresh() {
        onRestart();
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        if (connectionDetector.isConnectingToInternet())

        {
            new ExpenseEntryType().execute();

        } else {

            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button
        /*if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            alertMessage=AlertMessage.newInstance("Do you want to Logout ?","Ok","Cancel");
            alertMessage.show(getFragmentManager(), "logout");
            return true;
        }*/
        if (progressDialog.isShowing()) {
            System.out.println("ISshowing");
            progressDialog.dismiss();
        }
        if (progressDialog != null) {
            System.out.println("ISshowing Null");
            progressDialog.dismiss();
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onDialogPositiveWithoutViewClick(DialogFragment dialog) {
// TODO Auto-generated method stub

        if (connectionDetector.isConnectingToInternet())

        {
            new DeleteExpense().execute();
        } else {

            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

    }


    @Override
    public void onDialogNegativeWithoutViewClick(DialogFragment dialog) {
// TODO Auto-generated method stub
        alertMessage.dismiss();

    }
    /*public static DisplayMetrics getDeviceMetrics(Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        display.getMetrics(metrics);
        return metrics;
    }*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            if (connectionDetector.isConnectingToInternet())

            {
                new ExpenseEntryType().execute();

            } else {

                dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                dialogWithOutView.show(getFragmentManager(), "Info");
            }
        }
    }

   /* public void showview()
    {
        FrameLayout rv = new FrameLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(350, 350);
        layoutParams.setMargins(5,10,5,10);
        rv.setLayoutParams(layoutParams);
        final VideoView video = new VideoView(this);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        int reduce_width=width*15/100;


        FrameLayout.LayoutParams fp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT, 350);
        video.setLayoutParams(fp);
        ImageView imageview = new ImageView(this);
        fp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        fp.gravity = Gravity.CENTER;
        imageview.setImageResource(android.R.drawable.ic_media_play);

        imageview.bringToFront();
        imageview.setLayoutParams(fp);
        rv.addView(video);
        rv.addView(imageview);
        layout.addView(rv);
       *//* if (type == 1) {
            Uri uriFromPath = Uri.fromFile(new File(message));
            try {
                video.setVideoURI(uriFromPath);
                video.seekTo(100);


               *//**//* Bitmap thumb = ThumbnailUtils.createVideoThumbnail(message,
                        MediaStore.Images.Thumbnails.MINI_KIND);
                BitmapDrawable bitmapDrawable = new BitmapDrawable(thumb);
                video.setBackgroundDrawable(bitmapDrawable);*//**//*
            } catch (Exception e) {
                //e.printStackTrace();
            }
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.setMargins(reduce_width, 0, 20, 10);

            video.setLayoutParams(lp);
            lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            // video.setBackgroundResource(R.drawable.rounded_corner1);
        }*//*


    }*/
}
