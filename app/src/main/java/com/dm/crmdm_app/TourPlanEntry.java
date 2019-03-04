package com.dm.crmdm_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dm.controller.AppDataController;
import com.dm.controller.TourPlanController;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.Custom_Toast;
import com.dm.library.TourCityDropDownListAdapter;
import com.dm.library.TourDistributorDropDownListAdapter;
import com.dm.library.TourPurposeDropDownListAdapter;
import com.dm.model.AppData;
import com.dm.model.City;
import com.dm.model.Distributor;
import com.dm.model.Purpose;
import com.dm.model.TourPlan;
import com.dm.parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class TourPlanEntry extends AppCompatActivity implements TourCityDropDownListAdapter.DistributorAccessInterface {
    AppDataController appDataController1;
    SharedPreferences preferences, cityPreferences, distPreferences, purposePreferences;
    AlertOkDialog dialogWithOutView;
    Bundle bundle;
    Intent intent;
    int tourAndroidCode = 0;
    ConnectionDetector connectionDetector;
    ProgressDialog progressDialog;
    ArrayList<City> ArraycityList;
    ArrayList<String> cityList;
    String server, smid, userId, cId = "0", sessionSmid = "0", currentDate = "", tourDate = "", transTourId = "0";
    EditText city, distributor, purpose;
    ArrayList<AppData> appDataArray;
    public static boolean[] checkSelected;
    public static boolean[] checkSelectedDist;
    public static boolean[] checkSelectedPurpose;
    private boolean expanded;
    private PopupWindow pw, pw1, pw2;
    TextView tv1, tv2, tv3;
    TourCityDropDownListAdapter adapter;
    ArrayList<Distributor> ArraydistList;
    ArrayList<String> distList;
    ArrayList<Purpose> ArrayPurposeList;
    ArrayList<String> purposeList;
    TourDistributorDropDownListAdapter distadapter;
    TourPurposeDropDownListAdapter tourPurposeDropDownListAdapter;
    EditText remark;
    TourPlan tourPlan1;
    boolean ForUpdate = false;
    //    ArrayList<String> tourDateList;
    Button add, cancel;
    String DocId;
    TourPlan tourPlan;
    TourPlanController tourPlanController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_ACTION_BAR);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        WindowManager.LayoutParams params = this.getWindow().getAttributes();
        params.alpha = 1.0f;
        params.dimAmount = 0.5f;
        this.getWindow().setAttributes((WindowManager.LayoutParams) params);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        this.getWindow().setGravity(Gravity.CENTER);

        int reduceheight = height * 18 / 100;
        int reducewidth = width * 15 / 100;
        System.out.println("Height@:" + height + "-" + reduceheight + "-" + width + "-" + reducewidth);
        this.getWindow().setLayout(width - reducewidth, height - reduceheight);
        // This sets the window size, while working around the IllegalStateException thrown by ActionBarView
        // this.getWindow().setLayout(width-200,height-300);
        // setContentView(R.layout.dialog_move);
        setContentView(R.layout.activity_tour_plan_entry);

        connectionDetector = new ConnectionDetector(getApplicationContext());
        appDataController1 = new AppDataController(TourPlanEntry.this);
        preferences = getSharedPreferences("MyPref", MODE_PRIVATE);
        appDataController1 = new AppDataController(TourPlanEntry.this);
        tourPlanController = new TourPlanController(TourPlanEntry.this);
        remark = (EditText) findViewById(R.id.remark);
        cityPreferences = getSharedPreferences("MyTourCity", Context.MODE_PRIVATE);
        distPreferences = getSharedPreferences("MyTourDist", Context.MODE_PRIVATE);
        purposePreferences = getSharedPreferences("MyTourPurpose", Context.MODE_PRIVATE);
        tv1 = (TextView) findViewById(R.id.SelectCityBox);
        tv1.setInputType(InputType.TYPE_NULL);
        tv1.setFocusable(false);
        tv2 = (TextView) findViewById(R.id.SelectDistBox);
        tv2.setInputType(InputType.TYPE_NULL);
        tv2.setFocusable(false);
        tv3 = (TextView) findViewById(R.id.SelectpurposeBox);
        tv3.setInputType(InputType.TYPE_NULL);
        tv3.setFocusable(false);
        add = (Button) findViewById(R.id.atpAdd);
        cancel = (Button) findViewById(R.id.atpCancel);
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server = appData.getCompanyUrl();
//        smid=preferences.getString("CONPERID_SESSION", "0");
        userId = preferences.getString("USER_ID", "0");
        intent = getIntent();
        bundle = intent.getExtras();
        if (bundle != null) {
            tourAndroidCode = bundle.getInt("tourAndroidCode", 0);
            smid = bundle.getString("smId", "");
            currentDate = bundle.getString("currentDate", "");
            tourDate = bundle.getString("tourDate", "");
            transTourId = bundle.getString("transTourId", "0");
            ForUpdate = bundle.getBoolean("ForUpdate", false);
            DocId = bundle.getString("DocId", "0");

//            tourDateList=bundle.getStringArrayList("tourDateList");
        }
        if (!transTourId.equals("0")) {
            tourPlanController.open();
            tourPlan1 = tourPlanController.getTransTourPlanEntry(Integer.parseInt(transTourId));
            tourPlanController.close();
            cId = tourPlan1.getMCityID();
            add.setText("Update");
        }
        if (connectionDetector.isConnectingToInternet())

        {
            new GetCity().execute();

        } else {

            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveData();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });


    }

    void setData() {
//            setCityData
        if (ArraycityList != null) {
            checkSelected = new boolean[ArraycityList.size()];
            //initialize all values of list to 'unselected' initially
            String[] citywords = tourPlan1.getMCityID().split("\\,");
            int j1 = 0;
            int l111 = citywords.length;
            try {
                for (int i = 0; i < ArraycityList.size(); i++) {
                    if (j1 < l111) {
                        if (ArraycityList.get(i).getCity_id().equals(citywords[j1])) {
                            checkSelected[i] = true;
                            j1++;
                        } else {
                            checkSelected[i] = false;

                        }

                    }
                }
                int l3 = checkSelected.length;
                System.out.println(l3);
            } catch (Exception e) {
                System.out.println(e);
            }

            initialize(cityList, ArraycityList);

            SharedPreferences.Editor Cityeditor = cityPreferences.edit();
            Cityeditor.putString("IDS", tourPlan1.getMCityID());
            Cityeditor.putString("NAMES", tourPlan1.getMCityName());
            Cityeditor.commit();
        }

//    set dist data
        if (ArraydistList != null) {
            checkSelectedDist = new boolean[ArraydistList.size()];
            //initialize all values of list to 'unselected' initially
            String[] distwords = tourPlan1.getMDistId().split("\\,");
            int j1 = 0;
            int l111 = distwords.length;
            try {
                for (int i = 0; i < ArraydistList.size(); i++) {
                    if (j1 < l111) {
                        if (ArraydistList.get(i).getDistributor_id().equals(distwords[j1])) {
                            checkSelectedDist[i] = true;
                            j1++;
                        } else {
                            checkSelectedDist[i] = false;

                        }

                    }
                }
                int l3 = checkSelectedDist.length;
                System.out.println(l3);
            } catch (Exception e) {
                System.out.println(e);
            }

            initializeDist(distList, ArraydistList);

            SharedPreferences.Editor Disteditor = distPreferences.edit();
            Disteditor.putString("IDS", tourPlan1.getMDistId());
            Disteditor.putString("NAMES", tourPlan1.getMDistName());
            Disteditor.commit();
        }
//    set purpose data

        if (ArrayPurposeList != null) {
            checkSelectedPurpose = new boolean[ArrayPurposeList.size()];
            //initialize all values of list to 'unselected' initially
            String[] purposewords = tourPlan1.getMPurposeId().split("\\,");
            int j1 = 0;
            int l111 = purposewords.length;
            try {
                for (int i = 0; i < ArrayPurposeList.size(); i++) {
                    if (j1 < l111) {
                        if (ArrayPurposeList.get(i).getId().equals(purposewords[j1])) {
                            checkSelectedPurpose[i] = true;
                            j1++;
                        } else {
                            checkSelectedPurpose[i] = false;

                        }

                    }
                }
                int l3 = checkSelectedPurpose.length;
                System.out.println(l3);
            } catch (Exception e) {
                System.out.println(e);
            }

            initializePurpose(purposeList, ArrayPurposeList);
            SharedPreferences.Editor Purposeeditor = purposePreferences.edit();
            Purposeeditor.putString("IDS", tourPlan1.getMPurposeId());
            Purposeeditor.putString("NAMES", tourPlan1.getMPurposeName());
            Purposeeditor.commit();
        }

    }

    void saveData() {
        /*tourPlanController.open();
        String cityid = tourPlanController.getCityId(DocId,tourDate);
        String cityname = tourPlanController.getCityname(DocId,tourDate);
        String distributorid = tourPlanController.getdistributorId(DocId,tourDate);
        String distributorname = tourPlanController.getdistributorname(DocId,tourDate);
        String purposeid = tourPlanController.getpurposeId(DocId,tourDate);
        String purposename = tourPlanController.getpurposename(DocId,tourDate);
        tourPlanController.close();*/

        if (tv1.getText().toString().equals("None selected") || tv1.getText().toString().length() < 1) {
            new Custom_Toast(getApplicationContext(), "Please Select City").showCustomAlert();
        }
       /* else if(tv2.getText().toString().equals("None selected") || tv2.getText().toString().length()<1)
        {
            new Custom_Toast(getApplicationContext(), "Please Select Distributor Name").showCustomAlert();
        }*/
        else if (tv3.getText().toString().equals("None selected") || tv3.getText().toString().length() < 1) {
            new Custom_Toast(getApplicationContext(), "Please Select Purpose Of Visit").showCustomAlert();
        } else {

            tourPlan = new TourPlan();
            tourPlan.setTourPlanHId(String.valueOf(tourAndroidCode));
            tourPlan.setUserID(userId);
            tourPlan.setSMId(smid);
           /* cityPreferences=getSharedPreferences("MyTourCity",Context.MODE_PRIVATE);
            distPreferences=getSharedPreferences("MyTourDist",Context.MODE_PRIVATE);
            purposePreferences=getSharedPreferences("MyTourPurpose",Context.MODE_PRIVATE);*/

            tourPlan.setMCityID(cityPreferences.getString("IDS", "0"));
            tourPlan.setMCityName(cityPreferences.getString("NAMES", ""));
            tourPlan.setMDistId(distPreferences.getString("IDS", "0"));
            tourPlan.setMDistNamed(distPreferences.getString("NAMES", ""));
            tourPlan.setMPurposeId(purposePreferences.getString("IDS", "0"));
            tourPlan.setMPurposeName(purposePreferences.getString("NAMES", ""));
            tourPlan.setRemarks(remark.getText().toString());
            tourPlan.setVDate(tourDate);
            if (transTourId.equals("0")) {
                tourPlanController.open();
                tourPlan.setIsUpload("0");
                tourPlanController.insertTransTourPlan(tourPlan);
                tourPlanController.close();
            } else {

                tourPlanController.open();
                tourPlan.setIsUpload("0");
                tourPlanController.updateTransTourPlan(tourPlan, Integer.parseInt(transTourId));
                tourPlanController.close();
            }
        /*  SharedPreferences.Editor Cityeditor=cityPreferences.edit();
            Cityeditor.clear();
            Cityeditor.commit();
            SharedPreferences.Editor Disteditor=distPreferences.edit();
            Disteditor.clear();
            Disteditor.commit();
            SharedPreferences.Editor Purposeeditor=purposePreferences.edit();
            Purposeeditor.clear();
            Purposeeditor.commit();*/
            Bundle bundle = new Bundle();
            bundle.putInt("tourAndroidCode", tourAndroidCode);
            bundle.putString("smId", smid);
            bundle.putString("currentDate", currentDate);
            bundle.putBoolean("ForUpdate", ForUpdate);
            Intent intent = new Intent(TourPlanEntry.this, com.dm.crmdm_app.TourPlan.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    void clearData() {
        tv1.setText("None selected");
        tv2.setText("None selected");
        tv3.setText("None selected");
        remark.setText("");
    }

    @Override
    public void getDistUpdate(String cityId) {
        cId = cityId;
        if (connectionDetector.isConnectingToInternet())

        {
            new GetDistributorList().execute();

        } else {

            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

    }


    class GetDistributorList extends AsyncTask<String, String, String> {
        ProgressDialog pdLoading = new ProgressDialog(TourPlanEntry.this);

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pdLoading.setMessage("\tLoading...");
            pdLoading.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                if (ArraydistList != null) {
                    ArraydistList.clear();
                }
                ArraydistList = getDistList(cId);

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (distList != null) {
                tv2.setText("None selected");
                distList.clear();

            }
            distList = new ArrayList<String>();
            for (int i = 0; i < ArraydistList.size(); i++) {
                distList.add(ArraydistList.get(i).getDistributor_name().toUpperCase());
            }

            initializeDist(distList, ArraydistList);
            checkSelectedDist = null;
            checkSelectedDist = new boolean[distList.size()];
            for (int i = 0; i < checkSelectedDist.length; i++) {
                checkSelectedDist[i] = false;
            }
            super.onPostExecute(result);
            pdLoading.dismiss();
        }
    }

    class GetCity extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TourPlanEntry.this, "Loading Data", "Loading Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                if (ArraycityList != null) {
                    ArraycityList.clear();
                }
                ArraycityList = getCityList();

                if (!transTourId.equals("0")) {
                    try {
                        if (ArraydistList != null) {
                            ArraydistList.clear();
                        }
                        ArraydistList = getDistList(cId);
                        if (ArraydistList != null) {
                            if (distList != null) {
                                distList.clear();
                            }
                            distList = new ArrayList<String>();
                            for (int i = 0; i < ArraydistList.size(); i++) {
                                distList.add(ArraydistList.get(i).getDistributor_name().toUpperCase());
                            }
                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
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
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            cityList = new ArrayList<String>();
            for (int i = 0; i < ArraycityList.size(); i++) {
                cityList.add(ArraycityList.get(i).getCity_name().toUpperCase());
            }
            initialize(cityList, ArraycityList);
            checkSelected = null;
            checkSelected = new boolean[cityList.size()];
            for (int i = 0; i < checkSelected.length; i++) {
                checkSelected[i] = false;
            }
            if (connectionDetector.isConnectingToInternet()) {
                new GetPurpose().execute();
            } else {
                dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                dialogWithOutView.show(getFragmentManager(), "Info");
            }
        }
    }


    class GetPurpose extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(TourPlanEntry.this, "Loading Data", "Loading Please wait...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                if (ArrayPurposeList != null) {
                    ArrayPurposeList.clear();
                }
                ArrayPurposeList = getPurposeList();


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
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            purposeList = new ArrayList<String>();
            for (int i = 0; i < ArrayPurposeList.size(); i++) {
                purposeList.add(ArrayPurposeList.get(i).getName().toUpperCase());
            }
            initializePurpose(purposeList, ArrayPurposeList);

            checkSelectedPurpose = null;
            checkSelectedPurpose = new boolean[purposeList.size()];
            for (
                    int i = 0;
                    i < checkSelectedPurpose.length; i++) {
                checkSelectedPurpose[i] = false;
            }

            if (!transTourId.equals("0")) {
                tv1.setText(tourPlan1.getMCityName());
                tv2.setText(tourPlan1.getMDistName());
                tv3.setText(tourPlan1.getMPurposeName());
                remark.setText(tourPlan1.getRemarks());
                setData();
            } else {
                tv1.setText("None selected");
                tv2.setText("None selected");
                tv3.setText("None selected");
                remark.setText("");
            }
        }
    }

    private void initialize(ArrayList<String> cityItems, ArrayList<City> ArraycityList) {

        final ArrayList<String> items = cityItems;
        final ArrayList<City> ArraycityList1 = ArraycityList;

        city = (EditText) findViewById(R.id.SelectCityBox);
        city.setInputType(InputType.TYPE_NULL);
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!expanded) {
                    //display all selected values
                    String selected = "";
                    String firstSelectedId = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelected[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }
                    if (flag == 1)
                        city.setText(TourCityDropDownListAdapter.trimEnd(selected.trim(), ','));
                    expanded = true;
                } else {
                    city.setText(TourCityDropDownListAdapter.getSelected());
                    expanded = false;
                }
            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePopUp(items, city, ArraycityList1);
            }
        });

    }


    private void initiatePopUp(ArrayList<String> items, TextView tv, ArrayList<City> ArraycityList1) {
        LayoutInflater inflater = (LayoutInflater) TourPlanEntry.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.pop_up_window, (ViewGroup) findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        final RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.relativeLayoutCity);
        pw = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw.setOutsideTouchable(true);
        pw.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//    				pw.dismiss();
                    //pw.showAsDropDown(layout1);
                    return true;
                }
                pw.showAsDropDown(layout1);
                return false;
            }
        });

        //provide the source layout for drop-down
        pw.setContentView(layout);
        // System.out.println(layout);
        //anchor the drop-down to bottom-left corner of 'layout1'
        try {
            pw.showAsDropDown(layout1);
            //pw.showAtLocation(layout1,Gravity.BOTTOM, 0, layout1.getHeight());
            //  System.out.println(layout1);
        } catch (Exception e) {
            System.out.println(e);
        }
//    	populate the drop-down list
        ListView list = (ListView) layout.findViewById(R.id.dropDownList);
//    		final TourCityDropDownListAdapter adapter = new TourCityDropDownListAdapter(this, items, tv,ArraycityList1);
        adapter = new TourCityDropDownListAdapter(TourPlanEntry.this, items, tv, ArraycityList1, TourPlanEntry.this);

        list.setAdapter(adapter);

    }

    private void initializePurpose(ArrayList<String> cityItems, ArrayList<Purpose> ArraycityList) {

        final ArrayList<String> items = cityItems;
        final ArrayList<Purpose> ArraycityList1 = ArraycityList;

        purpose = (EditText) findViewById(R.id.SelectpurposeBox);
        purpose.setInputType(InputType.TYPE_NULL);
        purpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!expanded) {
                    //display all selected values
                    String selected = "";
                    String firstSelectedId = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelectedPurpose[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }
                    if (flag == 1)
                        purpose.setText(TourPurposeDropDownListAdapter.trimEnd(selected.trim(), ','));
                    expanded = true;
                } else {
                    purpose.setText(TourPurposeDropDownListAdapter.getSelected());
                    expanded = false;
                }
            }
        });

        tv3.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiatePurposePopUp(items, purpose, ArraycityList1);
            }
        });

    }


    private void initiatePurposePopUp(ArrayList<String> items, TextView tv, ArrayList<Purpose> ArraycityList1) {
        LayoutInflater inflater = (LayoutInflater) TourPlanEntry.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.pop_up_window, (ViewGroup) findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        final RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.relativeLayoutPurpose);
        pw2 = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw2.setBackgroundDrawable(new BitmapDrawable());
        pw2.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw2.setOutsideTouchable(true);
        pw2.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw2.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//    				pw.dismiss();
                    //pw.showAsDropDown(layout1);
                    return true;
                }
                pw2.showAsDropDown(layout1);
                return false;
            }
        });
        pw2.setContentView(layout);
        // System.out.println(layout);
        //anchor the drop-down to bottom-left corner of 'layout1'
        try {
            pw2.showAsDropDown(layout1);
            //pw.showAtLocation(layout1,Gravity.BOTTOM, 0, layout1.getHeight());
            //  System.out.println(layout1);
        } catch (Exception e) {
            System.out.println(e);
        }
        ListView list = (ListView) layout.findViewById(R.id.dropDownList);
//    		final TourCityDropDownListAdapter adapter = new TourCityDropDownListAdapter(this, items, tv,ArraycityList1);
        tourPurposeDropDownListAdapter = new TourPurposeDropDownListAdapter(TourPlanEntry.this, items, tv, ArraycityList1);
        list.setAdapter(tourPurposeDropDownListAdapter);

    }

    private void initializeDist(ArrayList<String> distItems, ArrayList<Distributor> ArraycityList) {

        final ArrayList<String> items = distItems;
        final ArrayList<Distributor> ArraycityList1 = ArraycityList;

        distributor = (EditText) findViewById(R.id.SelectDistBox);
        distributor.setInputType(InputType.TYPE_NULL);
        distributor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!expanded) {
                    //display all selected values
                    String selected = "";
                    String firstSelectedId = "";
                    int flag = 0;
                    for (int i = 0; i < items.size(); i++) {
                        if (checkSelectedDist[i] == true) {
                            selected += items.get(i);
                            selected += ", ";
                            flag = 1;
                        }
                    }
                    if (flag == 1)
                        distributor.setText(TourDistributorDropDownListAdapter.trimEnd(selected.trim(), ','));
                    expanded = true;
                } else {
                    distributor.setText(TourDistributorDropDownListAdapter.getSelected());
                    expanded = false;
                }
            }
        });

        tv2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                initiateDistPopUp(items, distributor, ArraycityList1);
            }
        });
    }


    private void initiateDistPopUp(ArrayList<String> items, TextView tv, ArrayList<Distributor> ArraycityList1) {
        LayoutInflater inflater = (LayoutInflater) TourPlanEntry.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //get the pop-up window i.e.  drop-down layout
        final LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.pop_up_window, (ViewGroup) findViewById(R.id.PopUpView));

        //get the view to which drop-down layout is to be anchored
        final RelativeLayout layout1 = (RelativeLayout) findViewById(R.id.relativeLayoutDist);
        pw1 = new PopupWindow(layout, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);

        //Pop-up window background cannot be null if we want the pop-up to listen touch events outside its window
        pw1.setBackgroundDrawable(new BitmapDrawable());
        pw1.setTouchable(true);

        //let pop-up be informed about touch events outside its window. This  should be done before setting the content of pop-up
        pw1.setOutsideTouchable(true);
        pw1.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        //dismiss the pop-up i.e. drop-down when touched anywhere outside the pop-up
        pw1.setTouchInterceptor(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//    				pw.dismiss();
                    //pw.showAsDropDown(layout1);
                    return true;
                }
                pw1.showAsDropDown(layout1);
                return false;
            }
        });

        //provide the source layout for drop-down
        pw1.setContentView(layout);
        // System.out.println(layout);
        //anchor the drop-down to bottom-left corner of 'layout1'
        try {
            pw1.showAsDropDown(layout1);
            //pw.showAtLocation(layout1,Gravity.BOTTOM, 0, layout1.getHeight());
            //  System.out.println(layout1);
        } catch (Exception e) {
            System.out.println(e);
        }
//    	populate the drop-down list
        ListView list = (ListView) layout.findViewById(R.id.dropDownList);
//    		final TourCityDropDownListAdapter adapter = new TourCityDropDownListAdapter(this, items, tv,ArraycityList1);
        distadapter = new TourDistributorDropDownListAdapter(TourPlanEntry.this, items, tv, ArraycityList1);

        list.setAdapter(distadapter);

    }


    ArrayList<City> getCityList() {
        ArrayList<City> cityLists = new ArrayList<City>();
        String url = "";
        url = "http://" + server + "/And_Sync.asmx/xJSGetCities?ConPer_Id=" + smid + "&minDate=0";
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        if (result != null) {
            try {

                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    City cityList = new City();
                    cityList.setCity_id(obj.getString("Cid"));
                    cityList.setCity_name(obj.getString("NM"));
                    cityLists.add(cityList);
                }

            } catch (Exception e) {
                System.out.println(e);
            }


        }

        return cityLists;
    }

    ArrayList<Purpose> getPurposeList() {
        ArrayList<Purpose> purposeLists = new ArrayList<Purpose>();
        String url = "";
        url = "http://" + server + "/And_Sync.asmx/xJSFindPurposeForTour?smid=" + sessionSmid;
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        if (result != null) {
            try {

                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    Purpose purpose = new Purpose();
                    purpose.setId(obj.getString("Pid"));
                    purpose.setName(obj.getString("Pnm"));
                    purposeLists.add(purpose);
                }

            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return purposeLists;
    }


    ArrayList<Distributor> getDistList(String cId) {
        ArrayList<Distributor> distLists = new ArrayList<Distributor>();
        String url = "";
        url = "http://" + server + "/And_Sync.asmx/xJSFindDistForTour?smid=" + smid + "&Aid=" + cId + "&minDate=0";
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        if (result != null) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    Distributor distributor = new Distributor();
                    distributor.setDistributor_id(obj.getString("Dsid"));
                    distributor.setDistributor_name(obj.getString("Dsnm"));
                    distLists.add(distributor);
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return distLists;
    }
}
