package com.dm.library;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.controller.TourPlanController;
import com.dm.crmdm_app.R;
import com.dm.crmdm_app.TourPlanAproval;
import com.dm.crmdm_app.TourPlanPopupAproval;
import com.dm.model.Competitor;
import com.dm.model.DashboardModel;
import com.dm.model.OrderTransaction;
import com.dm.model.TourPlan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by Dataman on 3/17/2017.
 */
public class CustomTourPlanAprovalArrayAdapter extends ArrayAdapter<DashboardModel> {

    private final ArrayList<DashboardModel> dashboardModels;private String witchRow; private int rowLayout;private int textView;
    private final Context context;private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();private static String checkData;
    String dateText;private ArrayList<InfoRowdata> infodata;static String dataForCheck;AlertBoxWithView dialogWithView;final HashMap<Integer, String> savedQtyData = new HashMap<Integer, String>();
    final HashMap<Integer, String> savedRateData = new HashMap<Integer, String>();String[] postValue;
    DataTransferInterface dataTransferInterface;ArrayList<MyHolder> arrayList;DataTransfer dataTransfer;HolderListener holderListener;String qtyOfProduct;
    static ArrayList<Competitor> competitors;CompetitorTransactionListener competitorTransactionListener;ArrayList<MyHolder> myHolders;View view;Competitor competitor;
    ListView listView;SharedPreferences preferences;
    String getdatafrom_notification;
//    ArrayList<String> tourDateList;
    ArrayList<TourPlan> tourPlans2;TourPlan tourPlan1;
    TourPlanController tourPlanController;TourPlan tourPlans;
    int pos=0;

    private String[] amountEdit_values;
    public String[] qtyEdit_values;
    public CustomTourPlanAprovalArrayAdapter(Context context, ArrayList<DashboardModel> dashboardModels, int rowLayout, int textView, HolderListener dataTransferInterface, String getdatafrom_notification) {
        super(context, rowLayout, textView, dashboardModels);
        this.holderListener=dataTransferInterface;
        this.context=context;
        this.dashboardModels=dashboardModels;
        this.textView=textView;
//		this.totalValueOrder=totalValueOrder;
        this.rowLayout=rowLayout;
        this.getdatafrom_notification = getdatafrom_notification;
        this.qtyEdit_values = new String[dashboardModels.size()];
        this.amountEdit_values = new String[dashboardModels.size()];
        this.postValue = new String[dashboardModels.size()];
        this.competitorTransactionListener=competitorTransactionListener;
        competitors=new ArrayList<Competitor>();
        myHolders=new ArrayList<MyHolder>();

        tourPlanController=new TourPlanController(context);
        arrayList=new ArrayList<MyHolder>();
        tourPlans2=new ArrayList<TourPlan>();
        infodata = new ArrayList<InfoRowdata>();
//        tourDateList=new ArrayList<String>();
//        for (int i = 0; i < dashboardModels.size(); i++) {
////            tourPlan1=new TourPlan();
////            tourPlan1.setRate(dashboardModels.get(i).getAmount());
////            tourPlan1.setItem(dashboardModels.get(i).getItemId());
////            tourPlan1.setQty(dashboardModels.get(i).getQuantity());
////            tourPlans2.add(i, tourPlan1);
////            infodata.add(new InfoRowdata(false, i));
////            savedQtyData.put(i, "");
////            savedRateData.put(i, "");
////            myHolders.add(i, null);
////            qtyEdit_values[i]=dashboardModels.get(i).getQuantity();
////            amountEdit_values[i]=dashboardModels.get(i).getAmount();
//            tourDateList.add(dashboardModels.get(i).getTtdate());
//        }
    }
    public int getCount() {

        return dashboardModels.size();
    }

    public DashboardModel getItem(int position) {
        // TODO Auto-generated method stub
        return dashboardModels.get(position);
    }

    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public static void setdata(String data)
    {
        dataForCheck=data;

    }
    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public Set<Integer> getCurrentCheckedPosition() {
        return mSelection.keySet();
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        //View v = super.getView(position, convertView, parent);
        view=parent;
        View row = convertView;final MyHolder holder;
        if(rowLayout== R.layout.tourplan_row) {
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.tourplan_row, parent, false);
                holder = new MyHolder(row);
                row.setTag(holder);
            } else {
                holder = (MyHolder) row.getTag();
            }
            holder.tdate.setText(String.valueOf(dashboardModels.get(position).getTtdate()));
            holder.city.setText(String.valueOf(dashboardModels.get(position).getCity()));
            holder.dist.setText(String.valueOf(dashboardModels.get(position).getDitributorName()));
            holder.remark.setText(String.valueOf(dashboardModels.get(position).getRemark()));
            holder.purpose.setText(String.valueOf(dashboardModels.get(position).getType()));
            if (dashboardModels.get(position).getTransId().equals("0")) {
                holder.add.setText("Add");

            } else {
                holder.add.setText("Update");
            }
            if(getdatafrom_notification.equals("disable")){
                holder.add.setVisibility(View.GONE);
            }
            else {
                holder.add.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
//                new IntentSend(context,TourPlanEntry.class).toSendAcivity();
                        Bundle bundle = new Bundle();
                        bundle.putInt("tourAndroidCode", Integer.parseInt(dashboardModels.get(position).getID() == null ? "0" : dashboardModels.get(position).getID()));
                        bundle.putString("smId", dashboardModels.get(position).getEmpname() == null ? "" : dashboardModels.get(position).getEmpname());
                        bundle.putString("currentDate", dashboardModels.get(position).getVdate() == null ? "" : dashboardModels.get(position).getVdate());
                        bundle.putString("tourDate", dashboardModels.get(position).getTtdate() == null ? "" : dashboardModels.get(position).getTtdate());
                        bundle.putString("transTourId", dashboardModels.get(position).getTransId());
                        if (dashboardModels.get(position).getActive().equals("1")) {
                            bundle.putBoolean("ForUpdate", true);
                        } else {
                            bundle.putBoolean("ForUpdate", false);
                        }

                        bundle.putBoolean("FromFind", false);
                        Intent intent = new Intent(context, TourPlanPopupAproval.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);

                    }
                });
            }
        }

        else  if(rowLayout== R.layout.advance_leave_request_list_row) {
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.advance_leave_request_list_row, parent, false);
                holder = new MyHolder(row);
                row.setTag(holder);


            } else {

                holder = (MyHolder) row.getTag();


            }


            System.out.println("Set in List"+dashboardModels.get(position).getAdvamount()+"-"+dashboardModels.get(position).getAstatus()
                    +"-"+dashboardModels.get(position).getApamount());

            holder.tvdocid.setText(dashboardModels.get(position).getVdate());
            holder.empname.setText(dashboardModels.get(position).getDoc_id());
            holder.tfdate.setText(dashboardModels.get(position).getName());
            holder.ttdate.setText(dashboardModels.get(position).getStatus());
//            holder.tvstaus.setText(dashboardModels.get(position).getAstatus());
//            holder.tvammount.setText(dashboardModels.get(position).getAdvamount());
//            holder.tvapammout.setText(dashboardModels.get(position).getApamount());

            holder.tvstaus.setVisibility(View.GONE);
            holder.tvammount.setVisibility(View.GONE);
            holder.tvapammout.setVisibility(View.GONE);

            row.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent i = new Intent(context, com.dm.crmdm_app.TourPlan.class);
                    i.putExtra("DocId",dashboardModels.get(position).getDoc_id());
                    i.putExtra("setID",dashboardModels.get(position).getID());
                    i.putExtra("FromFind",true);
                    i.putExtra("TourAproval" ,"TourAproval");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });

        }
        else  if(rowLayout== R.layout.find_tour_plan_list_row) {
            if (row == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                row = inflater.inflate(R.layout.find_tour_plan_list_row, parent, false);
                holder = new MyHolder(row);
                row.setTag(holder);


            } else {

                holder = (MyHolder) row.getTag();


            }

            System.out.println("Set in List"+dashboardModels.get(position).getAdvamount()+"-"+dashboardModels.get(position).getAstatus()
                    +"-"+dashboardModels.get(position).getApamount());

            holder.tfdate.setText(dashboardModels.get(position).getVdate());
            holder.tvdocid.setText(dashboardModels.get(position).getDoc_id());
            holder.empname.setText(dashboardModels.get(position).getName());
            holder.tvstaus.setText(dashboardModels.get(position).getStatus());
//            holder.tvstaus.setText(dashboardModels.get(position).getAstatus());
//            holder.tvammount.setText(dashboardModels.get(position).getAdvamount());
//            holder.tvapammout.setText(dashboardModels.get(position).getApamount());

            row.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Intent i = new Intent(context, TourPlanAproval.class);
                    i.putExtra("DocId",dashboardModels.get(position).getDoc_id());
                    i.putExtra("FromFind",true);
                    i.putExtra("setID",dashboardModels.get(position).getID());
                    i.putExtra("TourAproval" ,"TourAproval");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            });

        }

        return row;

    }

    public class MyHolder
    {  public TextView tdate,city,dist,purpose,remark,add;
        public TextView tvdocid,empname,tfdate,ttdate,tvstaus,tvammount,tvapammout;

        MyHolder(View view)
        {



            if(rowLayout== R.layout.tourplan_row) {
                tdate = (TextView) view.findViewById(R.id.tourDate);
                city = (TextView) view.findViewById(R.id.cityValue);
                dist = (TextView) view.findViewById(R.id.distValue);
                purpose = (TextView) view.findViewById(R.id.purpose);
                remark = (TextView) view.findViewById(R.id.remark);
                add = (TextView) view.findViewById(R.id.add);
                //dateRow = (TableRow) view.findViewById(R.id.addRow);
            }
            else  if(rowLayout== R.layout.advance_leave_request_list_row) {

                tvdocid=(TextView) view.findViewById(R.id.docid);
                tvdocid.setTextColor(Color.parseColor("#757575"));
                empname = (TextView) view.findViewById(R.id.empname);
                empname.setTextColor(Color.parseColor("#757575"));
                tfdate = (TextView) view.findViewById(R.id.travelfromdate);
                tfdate.setTextColor(Color.parseColor("#757575"));
                ttdate = (TextView) view.findViewById(R.id.traveltodate);
                ttdate.setTextColor(Color.parseColor("#757575"));
                tvstaus = (TextView) view.findViewById(R.id.status);
                tvstaus.setTextColor(Color.parseColor("#757575"));
                tvammount = (TextView) view.findViewById(R.id.amount);
                tvammount.setTextColor(Color.parseColor("#757575"));
                tvapammout = (TextView) view.findViewById(R.id.appamt);
                tvapammout.setTextColor(Color.parseColor("#757575"));


            }
            else  if(rowLayout== R.layout.find_tour_plan_list_row) {


                tfdate = (TextView) view.findViewById(R.id.tourdate);
                tfdate.setTextColor(Color.parseColor("#757575"));
                tvdocid=(TextView) view.findViewById(R.id.tourdocid);
                tvdocid.setTextColor(Color.parseColor("#757575"));
                empname = (TextView) view.findViewById(R.id.toursalesperson);
                empname.setTextColor(Color.parseColor("#757575"));
                tvstaus = (TextView) view.findViewById(R.id.tourstaus);
                tvstaus.setTextColor(Color.parseColor("#757575"));
            }
        }
    }

    public class InfoRowdata {

        public boolean isclicked=false;
        public int index;public String data=null;
    /*public String fanId;
    public String strAmount;*/

        public InfoRowdata(boolean isclicked,int index/*,String fanId,String strAmount*/)
        {
            this.index=index;
            this.isclicked=isclicked;
        /*this.fanId=fanId;
        this.strAmount=strAmount;*/
        }
    }
    public interface CompetitorTransactionListener{
        public void holderListener(ArrayList<TourPlan> competitors);
    }
    public interface HolderListener{
        public void holderListener(MyHolder myHolders);
    }


    public interface UpdateTotalValueOrder
    {
        public void setTotalOrderValue(ArrayList<OrderTransaction> orderTransactions);
    }
}
