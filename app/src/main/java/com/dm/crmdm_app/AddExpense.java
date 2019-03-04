package com.dm.crmdm_app;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.dm.controller.AppDataController;
import com.dm.library.AlertMessage;
import com.dm.library.AlertOkDialog;
import com.dm.library.ConnectionDetector;
import com.dm.library.CustomArrayAdopter;
import com.dm.library.Custom_Toast;
import com.dm.library.DataTransferInterface;
import com.dm.library.IntentSend;
import com.dm.model.AppData;
import com.dm.model.DashboardModel;
import com.dm.model.Expense;
import com.dm.parser.JSONParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class AddExpense extends AppCompatActivity implements DataTransferInterface,CustomArrayAdopter.HolderListener,AlertMessage.NoticeDialogListenerWithoutView{
        ListView listView;
    TextView grpNameText, dateText, remText;
    ImageView add, save, sum;ArrayList<Expense> expenseGrpList;
    CustomArrayAdopter customArrayAdopter;ArrayList<AppData> appDataArray;
    SharedPreferences preferences1;String server;AppDataController appDataController1;
    AlertOkDialog dialogWithOutView;ConnectionDetector connectionDetector;ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

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
        tv.setText("Expense Active Sheet");
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>Expense Active Sheet </font>"));
        add = (ImageView) findViewById(R.id.add_new);
        add.setImageResource(R.drawable.addgroup);
        listView = (ListView) findViewById(R.id.listView1);
        preferences1 = getSharedPreferences("MyPref", MODE_PRIVATE);
        appDataController1=new AppDataController(AddExpense.this);
        connectionDetector=new ConnectionDetector(getApplicationContext());
        AppData appData;
        appDataController1.open();
        appDataArray = appDataController1.getAppTypeFromDb();
        appDataController1.close();
        appData = appDataArray.get(0);
        server=appData.getCompanyUrl();
        if (connectionDetector.isConnectingToInternet())

        {
            new GetExpenseGrp().execute();

        }
        else{

            dialogWithOutView= AlertOkDialog.newInstance("There is no INTERNET CONNECTION available..");
            dialogWithOutView.show(getFragmentManager(), "Info");
        }

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view,
                                           final int position, long id) {
                // TODO Auto-generated method stub
              //  System.out.println("ViewOnClickLisner"+position+"_"+id+"_"+view);
                parent.getChildAt(position).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                return false;
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // System.out.println("ADD GROUP");
                (new IntentSend(getApplicationContext(), ExpenseGroupEntry.class)).toSendAcivity();

//                (new IntentSend(getApplicationContext(),ExpenseSummaryScreen.class)).toSendAcivity();

            }
        });


    }

    private ArrayList<DashboardModel> feedDahboardItemData(ArrayList<Expense> expensegrpList) {
        ArrayList<DashboardModel> dashboardModels = new ArrayList<DashboardModel>();
        DashboardModel model = new DashboardModel();
        for (int i = 0; i < expensegrpList.size(); i++) {
            dashboardModels.add(model.expenseGrpListFind(expensegrpList.get(i).getId(), expensegrpList.get(i).getName(), expensegrpList.get(i).getRemark(), expensegrpList.get(i).getFromDate(),expensegrpList.get(i).getToDate()));
        }

        return dashboardModels;
    }

    @Override
    public void setValues(ArrayList<CustomArrayAdopter.MyHolder> al) {

    }

    @Override
    public void holderListener(CustomArrayAdopter.MyHolder myHolder) {

    }

    @Override
    public void onDialogPositiveWithoutViewClick(DialogFragment dialog) {

    }

    @Override
    public void onDialogNegativeWithoutViewClick(DialogFragment dialog) {

    }


    class GetExpenseGrp extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog= ProgressDialog.show(AddExpense.this,"Loading Data", "Loading...", true);

        }

        @Override
        protected String doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {

                expenseGrpList=getExpenseGrpList();
            } catch (Exception e) {
                // TODO Auto-generated catch block
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

            if(expenseGrpList!=null)
            {

                if(expenseGrpList.size()==0)
                {
                    new Custom_Toast(getApplicationContext(), "No Record Found").showCustomAlert();
                }
               /* LayoutInflater inflater = getLayoutInflater();
                ViewGroup header = (ViewGroup) inflater.inflate(R.layout.header_exp_grp_list_row, listView, false);
                listView.addHeaderView(header, null, false);*/
                customArrayAdopter = new CustomArrayAdopter(AddExpense.this, feedDahboardItemData(expenseGrpList), R.layout.header_exp_grp_list_row, R.id.Text1, AddExpense.this);
                listView.setAdapter(customArrayAdopter);
               // System.out.println("listView"+expenseGrpList);
            }

        }
    }


    ArrayList<Expense> getExpenseGrpList()
    {
        String url="http://"+server+"/And_Sync.asmx/xJSGetExpenseGroup?SMID="+preferences1.getString("CONPERID_SESSION","0");
        JSONParser jParser = new JSONParser();
        ArrayList<Expense> expenseList = new ArrayList<Expense>();
        String result = jParser.getJSONArray(url);
        if (result != null) {
            try {
                JSONArray jsonarray = new JSONArray(result);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject obj = jsonarray.getJSONObject(i);
                    Expense expense = new Expense();
                    expense.setId(obj.getString("id"));
                    expense.setName(obj.getString("Nm"));
                    expense.setRemark(obj.getString("Rmk"));
                    expense.setFromDate(obj.getString("fdt"));
                    expense.setToDate(obj.getString("tdt"));
                    expenseList.add(expense);
                }
               // System.out.println("jsonarray"+jsonarray);
            }catch (Exception e)
            {
                if(progressDialog!=null)
                {
                    progressDialog.dismiss();
                }

                System.out.println(e);
//                mSuccess=true;
            }
        }
        return expenseList;
    }

    @Override
    public void onBackPressed() {
        finish();
        Intent i = new Intent(AddExpense.this, DashBoradActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}


