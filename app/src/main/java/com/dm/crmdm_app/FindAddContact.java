package com.dm.crmdm_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.dm.controller.AppDataController;
import com.dm.controller.CityController;
import com.dm.controller.CountryController;
import com.dm.controller.StateController;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomAdapterCRMStreamInfo;
import com.dm.library.CustomFindContactAdapter;
import com.dm.library.Custom_Toast;
import com.dm.library.IntentSend;
import com.dm.model.AddContactModel;
import com.dm.model.AppData;
import com.dm.model.Owner;
import com.dm.parser.JSONParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Map;

public class FindAddContact extends ActionBarActivity {
    ListView listview;AlertOkDialog dialogWithOutView;
    ConnectionDetector connectionDetector;boolean isMoreData = false,lodingProgressImage=true,msucssess=true;
    String timestamp="0";ProgressDialog progressDialog;
    ArrayList<AddContactModel> contactarrayList= new ArrayList<>();
    CustomFindContactAdapter adaptor;
    RelativeLayout progressBarLayout;
    EditText editTextSearch;String searchData="";
    ArrayList<Map<String, String>> contactValues= new ArrayList<Map<String, String>>();
    SharedPreferences preferences2;
    AppDataController appDataController1;
    ArrayList<AppData> appDataArray;
    String server,SMID;
    ArrayList<Owner> contaryArray=new ArrayList<>();
    ArrayList<Owner> cityArray=new ArrayList<>();
    ArrayList<Owner> stateArray=new ArrayList<>();
    String contary="",state="",city="";
    Spinner spinnerContary,spinnerState,spinnerCity;
    Button editTextSubmit;
    RelativeLayout footer;
    ImageView filter;
    CardView mainSerchPannel;
    @Override

    protected void onCreate(Bundle savedInstantState){
        super.onCreate(savedInstantState);
        setContentView(R.layout.find_add_contact);
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
        preferences2=this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        SMID=preferences2.getString("CONPERID_SESSION", null);
        appDataController1=new AppDataController(FindAddContact.this);
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();
        TextView tv = (TextView) findViewById(R.id.text);
        tv.setText("Contact Detail List");
        listview=(ListView)findViewById(R.id.faclv);
        editTextSearch=(EditText)findViewById(R.id.fadSearch);
        editTextSubmit=(Button) findViewById(R.id.asSubmit);
        spinnerContary=(Spinner)findViewById(R.id.fcCountary);
        spinnerState=(Spinner)findViewById(R.id.fcState);
        spinnerCity=(Spinner)findViewById(R.id.fcCity);
        new getingAllSpinnerValue().execute("Contary");
        progressBarLayout=(RelativeLayout)findViewById(R.id.progressbarrelativeLayout);
        connectionDetector=new ConnectionDetector(this);
        adaptor = new CustomFindContactAdapter(this,contactarrayList,R.layout.listview_find_add_contact);
        mainSerchPannel=(CardView)findViewById(R.id.mainSerchPannel);
        footer = (RelativeLayout)findViewById(R.id.footer);
        filter = (ImageView)findViewById(R.id.filter);
        filter.setVisibility(View.GONE);
        listview.setVisibility(View.GONE);
        mainSerchPannel.setVisibility(View.VISIBLE);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lodingProgressImage=true;
                listview.setVisibility(View.GONE);
                mainSerchPannel.setVisibility(View.VISIBLE);
                footer.setVisibility(View.GONE);
            }
        });

        listview.setAdapter(adaptor);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println("Data Is:"+contactarrayList.get(i).getContact_id());
                Intent intent=new Intent(FindAddContact.this,AddContact.class);
                intent.putExtra("ContactId",contactarrayList.get(i).getContact_id());
                startActivity(intent);
                finish();
            }
        });
        editTextSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lodingProgressImage=true;
                if (connectionDetector.isConnectingToInternet())
                {
                    mainSerchPannel.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    footer.setVisibility(View.VISIBLE);
                    new GetListAllContact().execute("ALL");

                }
                else{

                    dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                    dialogWithOutView.show(getFragmentManager(), "Info");
                }
            }
        });
//        if (connectionDetector.isConnectingToInternet())
//        {
//            new GetListAllContact().execute("ALL");
//
//        }
//        else{
//
//            dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
//            dialogWithOutView.show(getFragmentManager(), "Info");
//        }
        spinnerContary.setEnabled(false);
        spinnerContary.setFocusableInTouchMode(false);
        spinnerContary.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                selectedComContryId = contaryArray.get(position).getId();
                contary = selectedComContryId;
//                if(position != 0){
//                    contary=contaryArray.get(position).getId();
//                }
//                else if(position == 0){
//                    contary="";
//                    state="";
//                    city="";
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        spinnerState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                state=stateArray.get(position).getId();
                new getingAllSpinnerValue().execute("city");

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView)
            {

            }

        });
        spinnerCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(position != 0){
                    mainSerchPannel.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                    footer.setVisibility(View.VISIBLE);
                    city=cityArray.get(position).getId();
                    new GetListAllContact().execute("ALL");
                }
                else{
                    city="";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });
        /*editTextSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if(cs.length()>2){
                    searchData=String.valueOf(cs);
                    if(msucssess){
                        msucssess=false;
                        contactValues.clear();
                        contactarrayList.clear();
                        adaptor.notifyDataSetChanged();
                        new GetListAllContact().execute("Search");
                    }
                }
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                contactValues.clear();
                contactarrayList.clear();
                adaptor.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if(arg0.length()==0){
                    contactValues.clear();
                    contactarrayList.clear();
                    adaptor.notifyDataSetChanged();
                    new GetListAllContact().execute("ALL");
                }
            }
        });*/

        /*listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                if(isMoreData)
                {
                    if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {
                        isMoreData = false;
                        //  getNotificationData(SMID, notificationController.getTimeStamp());
                        if (connectionDetector.isConnectingToInternet()) {
                            new GetListAllContact().execute("ALL");
                        } else {
                            dialogWithOutView = AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
                            dialogWithOutView.show(getFragmentManager(), "Info");
                        }
                    }
                }
            }
        });*/

    }
    class GetListAllContact extends AsyncTask<String, String, String> {
        String type=null;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            if(lodingProgressImage){
                lodingProgressImage=false;
                progressDialog= ProgressDialog.show(FindAddContact.this,"Loading Data", "Please Wait...", true);
            }
            else
            {
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        }
        @Override
        protected String doInBackground(String... arg0) {
            type=arg0[0];
            // TODO Auto-generated method stub
            try {
                getContactData(timestamp,type);
            } catch (Exception e)
            {
                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            if(progressDialog!=null)
            {
                progressDialog.dismiss();
            }
            progressBarLayout.setVisibility(View.GONE);
            msucssess=true;
            adaptor.notifyDataSetChanged();
            filter.setVisibility(View.VISIBLE);
        }
    }

    protected class getingAllSpinnerValue extends AsyncTask<String, String, String> {
        String type=null,result=null;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
           // progressDialog= ProgressDialog.show(FindAddContact.this,"Loading Data", "Loading...", true);
           // progressDialog.setCancelable(true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            type=arg0[0];
            // TODO Auto-generated method stub
            try {
                result=getSpinnerValue(type);
            } catch (Exception e) {
                // TODO Auto-generated catch block
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
            JSONArray jsonArray=null;
            if(!(result == null)){
                if(type.equalsIgnoreCase("contary")){
                    try {
                        jsonArray=new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                Owner owner=new Owner();
                                objs = jsonArray.getJSONObject(i);
                                owner.setId(objs.getString("Cid"));
                                owner.setName(objs.getString("NM"));
                                contaryArray.add(owner);
                            } catch (JSONException e) {
                                if(progressDialog!=null)
                                {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        if(progressDialog!=null)
                        {
                            progressDialog.dismiss();
                        }
                        e.printStackTrace();
                    }
                    setSpinner(spinnerContary,contaryArray);
                    try{
                        for(int i=0;i<contaryArray.size();i++){
                            if(contaryArray.get(i).getName().equalsIgnoreCase("India")){
                                spinnerContary.setSelection(i);
                                selectedComContryId = contaryArray.get(i).getId();
                            }
                        }

                    }catch (Exception e){

                    }


                    new getingAllSpinnerValue().execute("state");
                }
                else if(type.equalsIgnoreCase("state")){
                    try {
                        jsonArray=new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                Owner owner=new Owner();
                                objs = jsonArray.getJSONObject(i);
                                owner.setId(objs.getString("id"));
                                owner.setName(objs.getString("nm"));
                                stateArray.add(owner);
                            } catch (JSONException e) {
                                if(progressDialog!=null)
                                {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        if(progressDialog!=null)
                        {
                            progressDialog.dismiss();
                        }

                        e.printStackTrace();
                    }

                    setSpinner(spinnerState,stateArray);
                }
                else{
                    try {
                        cityArray.clear();
                        jsonArray=new JSONArray(result);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject objs = null;
                            try {
                                Owner owner=new Owner();
                                objs = jsonArray.getJSONObject(i);
                                owner.setId(objs.getString("id"));
                                owner.setName(objs.getString("nm"));
                                cityArray.add(owner);
                            } catch (JSONException e) {
                                if(progressDialog!=null)
                                {
                                    progressDialog.dismiss();
                                }
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        if(progressDialog!=null)
                        {
                            progressDialog.dismiss();
                        }
                        e.printStackTrace();
                    }
                    if(progressDialog!=null)
                    {
                        progressDialog.dismiss();
                    }
                    setSpinner(spinnerCity,cityArray);
                }

            }
            else{
                new Custom_Toast(FindAddContact.this,"No Data Found").showCustomAlert();
            }

        }
    }


    public boolean setStateContarySpinner(Spinner spinner, ArrayList<Owner> arrayList){
        CustomAdapterCRMStreamInfo adapter = new CustomAdapterCRMStreamInfo(FindAddContact.this,arrayList ,R.layout.spinner_adapter_view);
        spinner.setAdapter(adapter);

        return  true;
    }
    String selectedComContryId="";
    public String getSpinnerValue(String type){
        String url;String result=null;
        if(type.equalsIgnoreCase("contary")){
           // url = "http://"+server+"/And_Sync.asmx/xJSGetCountry?minDate=0";
            CountryController countryController=new CountryController(this);
            countryController.open();
            result=countryController.getCountaryList();
            countryController.close();
        }
        else if(type.equalsIgnoreCase("state")){
           // url = "http://"+server+"/And_Sync.asmx/XjsBindCRM_State?countryid="+selectedComContryId;
            StateController stateController=new StateController(this);
            stateController.open();
            result=stateController.getStateListObject(selectedComContryId);
            stateController.close();
        }
        else{
           // url = "http://"+server+"/And_Sync.asmx/XjsBindCRM_City?stateid="+state;
            CityController cityController=new CityController(this);
            cityController.open();
            result=cityController.getCityObjectStateWise(state);
            cityController.close();
        }

      /*  JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);*/
        return result;
    }
    public boolean setSpinner(Spinner spinner, ArrayList<Owner> arrayList){
        CustomAdapterCRMStreamInfo adapter = new CustomAdapterCRMStreamInfo(FindAddContact.this,arrayList ,R.layout.spinner_adapter_view);
        spinner.setAdapter(adapter);
        return  true;
    }
    public void getContactData(String timestamp,String mode){
        String url="";
        contactarrayList.clear();
        if(mode.equalsIgnoreCase("ALL")){
            url="http://"+server+"/And_Sync.asmx/XjsFindContacts_CRM?Smid="+SMID+"&country="+contary+"&state="+state+"&city="+city+"";
        }
        else{
            //url="http://"+server+"/And_Sync.asmx/xJSGetNotificationInDetail?Smid="+SMID+"&minDate="+timestamp ;
            url="http://"+server+"/And_Sync.asmx/XjsFindContacts_CRM?Smid="+SMID+"&country="+contary+"&state="+state+"&city="+city+"";
        }
        JSONParser jParser = new JSONParser();
        String result = jParser.getJSONArray(url);
        if (result != null) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                if(jsonarray.length()>0)
                {
                    isMoreData = true;
                }
                else
                {
                    isMoreData = false;
                }
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    AddContactModel cdata = new AddContactModel();
                    cdata.setContactID(obj.getString("Contact_Id"));
                    cdata.setFirstName(obj.getString("FirstName"));
                    cdata.setLastName(obj.getString("Lastname"));
                    cdata.setJobTitle(obj.getString("Jobtitle"));
                    cdata.setComapy(obj.getString("CompName"));
                    cdata.setPhone(obj.getString("Phone"));
                    cdata.setEmail(obj.getString("Email"));
                    cdata.setUrl(obj.getString("Url"));
                    cdata.setAddress(obj.getString("address"));
                    cdata.setStatus(obj.getString("Status"));
                    cdata.setImageUrl(obj.getString("imageURL"));
                    contactarrayList.add(cdata);
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
public void onBackPressed(){
    super.onBackPressed();
    Intent intent=new Intent(FindAddContact.this,AddContact.class);
    startActivity(intent);
    finish();
}
}
