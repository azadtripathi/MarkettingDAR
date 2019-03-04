package com.dm.library;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.crmdm_app.CRMStreamInfo;
import com.dm.crmdm_app.DashBoradActivity;
import com.dm.crmdm_app.R;
import com.dm.model.CRMHistoryInfo;
import com.dm.model.PersonInfo;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Administrator on 5/2/2017.
 */
public class CustomPersonHistoryArrayAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    int rowlayout;
    LayoutInflater inflater;
    private ArrayList<CRMHistoryInfo> data = null;
    String type;
    View.OnClickListener editClick,deleteClick;
    protected int count;
    ArrayList<LinearLayout> arrayHide=new ArrayList<>();
    ArrayList<LinearLayout> arrayShow=new ArrayList<>();
    String PreviousDate="",PreviousHeaderDate="";
    int previousPosition=0;
    View.OnClickListener viewMilestone;


    public CustomPersonHistoryArrayAdapter(Context context, ArrayList<CRMHistoryInfo> data, int rowlayout, String type, View.OnClickListener editClick, View.OnClickListener deleteClick, View.OnClickListener milestoneclick) {
        this.rowlayout=rowlayout;
        mContext = context;
        this.data = data;
        inflater = LayoutInflater.from(mContext);
        this.type=type;
        this.editClick=editClick;
        this.deleteClick=deleteClick;
        viewMilestone = milestoneclick;


    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CRMHistoryInfo getItem(int position) {
        return data.get(position);
    }



    private int getIndexByProperty(String yourString) {
        for (int i = 0; i < DashBoradActivity.crmPermissionslist.size(); i++) {
            if (DashBoradActivity.crmPermissionslist !=null && DashBoradActivity.crmPermissionslist.get(i).getActivityName().equals(yourString)) {
                return i;
            }
        }
        return -1;// not there is list
    }


    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position,
                        View convertView, ViewGroup parent) {
        View row = convertView;final MyHolder holder;
        if(row==null)
        {
            LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row=inflater.inflate(R.layout.crm_history_listview,parent,false);
            holder=new MyHolder(row);
            row.setTag(holder);
        }
        else
        {
            holder=(MyHolder)row.getTag();
        }
        try {
            int i = getIndexByProperty("Edit");
            int k = getIndexByProperty("Delete");
            int j = getIndexByProperty("Deal Detail");
            if(DashBoradActivity.crmPermissionslist.get(i).getPermission().equalsIgnoreCase("false"))
            {
                holder.textViewActionEdit.setVisibility(View.GONE);
                holder.textViewCallEdit.setVisibility(View.GONE);
                holder.textViewDealEdit.setVisibility(View.GONE);
                holder.textViewNoteEdit.setVisibility(View.GONE);
                holder.textViewPaymentEdit.setVisibility(View.GONE);
            }
            if(DashBoradActivity.crmPermissionslist.get(k).getPermission().equalsIgnoreCase("false"))
            {
                holder.textViewActionDelete.setVisibility(View.GONE);
                holder.textViewCallDelete.setVisibility(View.GONE);
                holder.textViewDealDelete.setVisibility(View.GONE);
                holder.textViewNoteDelete.setVisibility(View.GONE);
                holder.textViewPaymentDelete.setVisibility(View.GONE);
            }
            if(DashBoradActivity.crmPermissionslist.get(j).getPermission().equalsIgnoreCase("false"))
            {
                holder.textViewActionDelete.setVisibility(View.GONE);
                holder.textViewCallDelete.setVisibility(View.GONE);
                holder.textViewDealDelete.setVisibility(View.GONE);
                holder.textViewNoteDelete.setVisibility(View.GONE);
                holder.viewMilestoneLabel.setVisibility(View.GONE);
                holder.textViewPaymentDelete.setVisibility(View.GONE);
            }
        }catch (Exception e){}

        /***************************for Header Date****************************/
        //Start
        if(PreviousDate.equalsIgnoreCase(data.get(position).getHeaderDate()) && position>0){
                PreviousDate=data.get(position).getHeaderDate();
                holder.headerDateLinearLayout.setVisibility(View.GONE);
            }
            else{
                if(position>=previousPosition){
                    previousPosition=position;
                    holder.headerDateLinearLayout.setVisibility(View.VISIBLE);
                    PreviousDate=data.get(position).getHeaderDate();
                    holder.textViewHeaderDate.setText(data.get(position).getHeaderDate());
                }
                else{
                    previousPosition=position;
                    PreviousDate="";
                    try{
                        if(position==0){
                            holder.headerDateLinearLayout.setVisibility(View.VISIBLE);
                            PreviousDate=data.get(position).getHeaderDate();
                            holder.textViewHeaderDate.setText(data.get(position).getHeaderDate());
                    }
                    else{
                            if(!data.get(position-1).getHeaderDate().equalsIgnoreCase(data.get(position).getHeaderDate())){
                                holder.headerDateLinearLayout.setVisibility(View.VISIBLE);
                                PreviousDate=data.get(position).getHeaderDate();
                                holder.textViewHeaderDate.setText(data.get(position).getHeaderDate());
                            }
                        }
                    }
                    catch (IndexOutOfBoundsException e)
                    {
                        e.printStackTrace();
                    }
                   /*  holder.headerDateLinearLayout.setVisibility(View.VISIBLE);
                    PreviousDate=data.get(position).getHeaderDate();
                    holder.textViewHeaderDate.setText(data.get(position).getHeaderDate());*/
                }
            }
        //End
        if(type.equalsIgnoreCase("All")) {
            holder.textViewNote.setText(data.get(position).getNote());
            if(data.get(position).getMailto()== null || data.get(position).getMailto().isEmpty())
            {
                holder.emailphoonelabel.setText("Phone");
                holder.textViewCallPhone.setText(data.get(position).getPhone()+" "+data.get(position).getPhoneoremailValue());
            }
            else
            {
                holder.emailphoonelabel.setText("Email To:");
                holder.textViewCallPhone.setText(data.get(position).getMailto()+" "+data.get(position).getPhoneoremailValue());
            }
            holder.textViewCallResult.setText(data.get(position).getResult());
            holder.textViewCallNote.setText(data.get(position).getCallNote());

            holder.textViewActionName.setText(data.get(position).getTaskTask());
            holder.textViewActionAssignDate.setText(data.get(position).getTaskADate());
            holder.textViewActionAssignTo.setText(data.get(position).getTaskAssignTo()+" "+data.get(position).getHeaderDate()+ " "+data.get(position).getActionTime());
            holder.textViewActionAssignBy.setText(data.get(position).getTaskAssignBy());

            holder.textViewDealName.setText(data.get(position).getDealName());
            holder.textViewDealNote.setText(data.get(position).getDealNote());
            holder.textViewDealAmt.setText(data.get(position).getDealTotalAmt());
            holder.textViewDealDate.setText(data.get(position).getDealDate());
            holder.textViewDealStage.setText(data.get(position).getDealPercentage());
            holder.textViewDealCloseDate.setText(data.get(position).getDealCloseDate());
            holder.noteusername.setText(data.get(position).getUsername()+" "+data.get(position).getHeaderDate());
            holder.callNoteusername.setText(data.get(position).getUsername()+" "+data.get(position).getHeaderDate());
            arrayShow.clear();
            arrayShow.add(holder.linearLayoutContainer);
            arrayShow.add(holder.linearLayoutNote);
            arrayShow.add(holder.linearLayoutCall);
            arrayShow.add(holder.linearLayoutDeal);
            arrayShow.add(holder.linearLayoutAction);
            arrayShow.add(holder.linearLayoutPaymet);
            visibilityShow(arrayShow);
            if(data.get(position).getNoteId() == null || data.get(position).getNoteId().equalsIgnoreCase("0") || data.get(position).getNoteId().equalsIgnoreCase("")){
                holder.linearLayoutNote.setVisibility(View.GONE);
            }
            if(data.get(position).getTaskCallId() == null || data.get(position).getTaskCallId().equalsIgnoreCase("0") || data.get(position).getTaskCallId().equalsIgnoreCase("")){
                holder.linearLayoutCall.setVisibility(View.GONE);
            }

            if(data.get(position).getDealID() == null || data.get(position).getDealID().equalsIgnoreCase("0") || data.get(position).getDealID().equalsIgnoreCase("")){
                holder.linearLayoutDeal.setVisibility(View.GONE);
            }
            if(data.get(position).getTaskDocid() == null || data.get(position).getTaskDocid().equalsIgnoreCase("0") || data.get(position).getTaskDocid().equalsIgnoreCase("")){
                holder.linearLayoutAction.setVisibility(View.GONE);
            }
            if(data.get(position).getPaymetcollectionId() == null || data.get(position).getPaymetcollectionId().equalsIgnoreCase("0") || data.get(position).getPaymetcollectionId().equalsIgnoreCase("")){
                holder.linearLayoutPaymet.setVisibility(View.GONE);
            }
            holder.textViewNoteEdit.setTag("Note"+"-"+data.get(position).getNoteId());
            holder.textViewNoteDelete.setTag("Note"+"-"+data.get(position).getNoteId());
            holder.textViewNoteEdit.setOnClickListener(editClick);
            holder.textViewNoteDelete.setOnClickListener(deleteClick);

            holder.textViewCallEdit.setTag("Call"+"-"+data.get(position).getTaskCallId());
            holder.textViewCallDelete.setTag("Call"+"-"+data.get(position).getTaskCallId());
            holder.textViewCallEdit.setOnClickListener(editClick);
            holder.textViewCallDelete.setOnClickListener(deleteClick);

            holder.textViewDealEdit.setTag("Deal"+"-"+data.get(position).getDealID());
            holder.textViewDealDelete.setTag("Deal"+"-"+data.get(position).getDealID());
            holder.textViewDealEdit.setOnClickListener(editClick);
            holder.textViewDealDelete.setOnClickListener(deleteClick);

            holder.textViewActionEdit.setTag("Action"+"-"+data.get(position).getTaskDocid());
            holder.textViewActionDelete.setTag("Action"+"-"+data.get(position).getTaskDocid());
            holder.textViewActionEdit.setOnClickListener(editClick);
            holder.textViewActionDelete.setOnClickListener(deleteClick);
            holder.viewMilestoneLabel.setOnClickListener(viewMilestone);
            holder.viewMilestoneLabel.setTag(data.get(position).getDealID());

            //Payment
            holder.textViewPaymentAmount.setText(data.get(position).getDealTotalAmt());
            holder.textViewPaymentMode.setText(data.get(position).getPaymentMode());
            if(data.get(position).getPaymentModeCash()){
                /*holder.textViewPaymentChequeNo.setText(data.get(position).getPaymentChequeNo());
                holder.textViewPaymentChequeDate.setText(data.get(position).getPaymentChequeNo());
                holder.textViewPaymentBank.setText(data.get(position).getDealTotalAmt());
                holder.textViewPaymentBranch.setText(data.get(position).getDealTotalAmt());*/
                holder.linearLayoutPaymentDetails.setVisibility(View.GONE);
            }
            else{
                holder.linearLayoutPaymentDetails.setVisibility(View.VISIBLE);
                holder.textViewPaymentDetails.setText(data.get(position).getPaymentChequeDate()+" ,"+data.get(position).getPaymentChequeNo()+"/"+data.get(position).getPaymentBank()+"/"+data.get(position).getPaymentBranch());
               /* holder.textViewPaymentChequeNo.setText(data.get(position).getPaymentChequeNo());
                holder.textViewPaymentChequeDate.setText(data.get(position).getPaymentChequeDate());
                holder.textViewPaymentBank.setText(data.get(position).getPaymentBank());
                holder.textViewPaymentBranch.setText(data.get(position).getPaymentBranch());*/
            }
            holder.textViewPaymentRemark.setText(data.get(position).getPaymentRemark());
            holder.textViewPaymentBy.setText(data.get(position).getTaskAssignBy());
            //holder.textViewPaymentUserName.setText(data.get(position).getUsername());
            holder.textViewPaymentDate.setText(data.get(position).getHeaderDate());
            holder.textViewPaymentEdit.setOnClickListener(editClick);
            holder.textViewPaymentDelete.setOnClickListener(deleteClick);
            holder.textViewPaymentEdit.setTag("Payment"+"-"+data.get(position).getPaymetcollectionId());
            holder.textViewPaymentDelete.setTag("Payment"+"-"+data.get(position).getPaymetcollectionId());


        }
        else if(type.equalsIgnoreCase("NoteAndCall")){
            //OnClick
            holder.textViewCallEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"hi",Toast.LENGTH_SHORT).show();
                }
            });
            holder.textViewCallDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"hi",Toast.LENGTH_SHORT).show();
                }
            });
            holder.textViewNoteEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"hi",Toast.LENGTH_SHORT).show();
                }
            });
            holder.textViewNoteDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(mContext,"hi",Toast.LENGTH_SHORT).show();
                }
            });
            arrayHide.clear();
            if(data.get(position).getTaskCallId() == null || data.get(position).getTaskCallId().equalsIgnoreCase("0")){
                arrayHide.add(holder.linearLayoutDeal);
                arrayHide.add(holder.linearLayoutAction);
                arrayHide.add(holder.linearLayoutCall);
                arrayHide.add(holder.linearLayoutPaymet);
                visibilityHide(arrayHide);
                arrayShow.clear();
                arrayShow.add(holder.linearLayoutNote);
                visibilityShow(arrayShow);
                holder.textViewNote.setText(data.get(position).getNote());

            }
            else if(data.get(position).getNoteId() == null || data.get(position).getNoteId().equalsIgnoreCase("0")){
                arrayHide.add(holder.linearLayoutDeal);
                arrayHide.add(holder.linearLayoutAction);
                arrayHide.add(holder.linearLayoutNote);
                arrayHide.add(holder.linearLayoutPaymet);
                visibilityHide(arrayHide);
                arrayShow.clear();
                arrayShow.add(holder.linearLayoutCall);
                visibilityShow(arrayShow);
                holder.textViewCallPhone.setText(data.get(position).getPhone());
                holder.textViewCallResult.setText(data.get(position).getResult());
                holder.textViewCallNote.setText(data.get(position).getCallNote());

            }
            else{
                arrayHide.add(holder.linearLayoutDeal);
                arrayHide.add(holder.linearLayoutAction);
                arrayHide.add(holder.linearLayoutPaymet);
                visibilityHide(arrayHide);
                arrayShow.clear();
                arrayShow.add(holder.linearLayoutNote);
                arrayShow.add(holder.linearLayoutCall);
                visibilityShow(arrayShow);
                holder.textViewNote.setText(data.get(position).getNote());
                holder.textViewCallPhone.setText(data.get(position).getPhone());
                holder.textViewCallResult.setText(data.get(position).getResult());
                holder.textViewCallNote.setText(data.get(position).getCallNote());

            }
            holder.callNoteusername.setText(data.get(position).getUsername()+" "+data.get(position).getHeaderDate());
            if(data.get(position).getMailto().isEmpty())
            {
                holder.emailphoonelabel.setText("Phone");
                holder.textViewCallPhone.setText(data.get(position).getPhone()+" "+data.get(position).getPhoneoremailValue());
            }
            else
            {
                holder.emailphoonelabel.setText("Email To:");
                holder.textViewCallPhone.setText(data.get(position).getMailto()+" "+data.get(position).getPhoneoremailValue());
            }
            holder.textViewNoteEdit.setTag("Note"+"-"+data.get(position).getNoteId());
            holder.textViewNoteDelete.setTag("Note"+"-"+data.get(position).getNoteId());
            holder.textViewNoteEdit.setOnClickListener(editClick);
            holder.textViewNoteDelete.setOnClickListener(deleteClick);
            holder.noteusername.setText(data.get(position).getUsername()+" "+data.get(position).getHeaderDate());
            holder.textViewCallEdit.setTag("Call"+"-"+data.get(position).getTaskCallId());
            holder.textViewCallDelete.setTag("Call"+"-"+data.get(position).getTaskCallId());
            holder.textViewCallEdit.setOnClickListener(editClick);
            holder.textViewCallDelete.setOnClickListener(deleteClick);

        }
        else if(type.equalsIgnoreCase("Deal")){
            holder.textViewDealName.setText(data.get(position).getDealName());
            holder.textViewDealAmt.setText(data.get(position).getDealTotalAmt());
            holder.textViewDealDate.setText(data.get(position).getDealDate());
            holder.textViewDealStage.setText(data.get(position).getDealPercentage());
            holder.textViewDealCloseDate.setText(data.get(position).getDealCloseDate());
            holder.viewMilestoneLabel.setTag(data.get(position).getDealID());
            holder.viewMilestoneLabel.setOnClickListener(viewMilestone);
            arrayHide.clear();
            arrayHide.add(holder.linearLayoutNote);
            arrayHide.add(holder.linearLayoutCall);
            arrayHide.add(holder.linearLayoutAction);
            arrayHide.add(holder.linearLayoutPaymet);
            visibilityHide(arrayHide);
            arrayShow.clear();
            arrayShow.add(holder.linearLayoutDeal);
            visibilityShow(arrayShow);

            holder.textViewDealEdit.setTag("Deal"+"-"+data.get(position).getDealID());
            holder.textViewDealDelete.setTag("Deal"+"-"+data.get(position).getDealID());
            holder.textViewDealEdit.setOnClickListener(editClick);
            holder.textViewDealDelete.setOnClickListener(deleteClick);
            holder.viewMilestoneLabel.setOnClickListener(viewMilestone);
        }
        else if(type.equalsIgnoreCase("Action")){
            holder.textViewActionName.setText(data.get(position).getTaskTask());
            holder.textViewActionAssignDate.setText(data.get(position).getTaskADate());
            holder.textViewActionAssignTo.setText(data.get(position).getTaskAssignTo()+" "+data.get(position).getHeaderDate());
            holder.textViewActionAssignBy.setText(data.get(position).getTaskAssignBy());
            arrayHide.clear();
            arrayHide.add(holder.linearLayoutDeal);
            arrayHide.add(holder.linearLayoutNote);
            arrayHide.add(holder.linearLayoutCall);
            arrayHide.add(holder.linearLayoutPaymet);
            visibilityHide(arrayHide);
            arrayShow.clear();
            arrayShow.add(holder.linearLayoutAction);
            visibilityShow(arrayShow);
            holder.textViewActionEdit.setTag("Action"+"-"+data.get(position).getTaskDocid());
            holder.textViewActionDelete.setTag("Action"+"-"+data.get(position).getTaskDocid());
            holder.textViewActionEdit.setOnClickListener(editClick);
            holder.textViewActionDelete.setOnClickListener(deleteClick);
        }
        else if(type.equalsIgnoreCase("Payment")){
            arrayHide.clear();
            arrayHide.add(holder.linearLayoutDeal);
            arrayHide.add(holder.linearLayoutNote);
            arrayHide.add(holder.linearLayoutCall);
            arrayHide.add(holder.linearLayoutAction);
            visibilityHide(arrayHide);
            arrayShow.clear();
            arrayShow.add(holder.linearLayoutPaymet);
            visibilityShow(arrayShow);
            holder.textViewPaymentAmount.setText(data.get(position).getDealTotalAmt());
            holder.textViewPaymentMode.setText(data.get(position).getPaymentMode());
            if(data.get(position).getPaymentModeCash()){
                /*holder.textViewPaymentChequeNo.setText(data.get(position).getPaymentChequeNo());
                holder.textViewPaymentChequeDate.setText(data.get(position).getPaymentChequeNo());
                holder.textViewPaymentBank.setText(data.get(position).getDealTotalAmt());
                holder.textViewPaymentBranch.setText(data.get(position).getDealTotalAmt());*/
                holder.linearLayoutPaymentDetails.setVisibility(View.GONE);
            }
            else{
                holder.linearLayoutPaymentDetails.setVisibility(View.VISIBLE);
                holder.textViewPaymentDetails.setText(data.get(position).getPaymentChequeNo()+"/"+data.get(position).getPaymentBank()+"/"+data.get(position).getPaymentBranch()+"/"+"\n"+data.get(position).getPaymentChequeDate());
             /*   holder.textViewPaymentChequeNo.setText(data.get(position).getPaymentChequeNo());
                holder.textViewPaymentChequeDate.setText(data.get(position).getPaymentChequeDate());
                holder.textViewPaymentBank.setText(data.get(position).getPaymentBank());
                holder.textViewPaymentBranch.setText(data.get(position).getPaymentBranch());*/
            }
            holder.textViewPaymentRemark.setText(data.get(position).getPaymentRemark());
            holder.textViewPaymentBy.setText(data.get(position).getTaskAssignBy());
            holder.textViewPaymentUserName.setText(data.get(position).getUsername());
            holder.textViewPaymentDate.setText(data.get(position).getHeaderDate());
            holder.textViewPaymentEdit.setOnClickListener(editClick);
            holder.textViewPaymentDelete.setOnClickListener(deleteClick);
            holder.textViewPaymentEdit.setTag("Payment"+"-"+data.get(position).getPaymetcollectionId());
            holder.textViewPaymentDelete.setTag("Payment"+"-"+data.get(position).getPaymetcollectionId());

        }
        return row;
    }
    public class MyHolder
    {
        public LinearLayout linearLayoutNote,linearLayoutCall,linearLayoutDeal,linearLayoutAction,headerDateLinearLayout,linearLayoutContainer,linearLayoutPaymet,linearLayoutPaymentDetails;
        TextView textViewCallPhone,textViewCallResult,textViewCallNote,textViewCallEdit,textViewCallDelete;
        TextView textViewNote,textViewNoteEdit,textViewNoteDelete;
        TextView textViewHeaderDate,noteusername,callNoteusername,emailphoonelabel;
        TextView textViewDealName,textViewDealAmt,textViewDealDate,textViewDealStage,textViewDealCloseDate,textViewDealEdit,textViewDealDelete,textViewDealNote, viewMilestoneLabel;
        TextView textViewActionName,textViewActionAssignDate,textViewActionAssignTo,textViewActionAssignBy,textViewActionEdit,textViewActionDelete;
        TextView textViewPaymentAmount,textViewPaymentDetails,textViewPaymentMode,textViewPaymentRemark,textViewPaymentBy,textViewPaymentUserName,textViewPaymentDate,textViewPaymentEdit,textViewPaymentDelete;
        //TextView textViewPaymentChequeNo,textViewPaymentChequeDate,textViewPaymentBank,textViewPaymentBranch;
        MyHolder(View view)
        {
            emailphoonelabel = (TextView)view.findViewById(R.id.emailphoonelabel);
            callNoteusername = (TextView)view.findViewById(R.id.callNoteusername);
            noteusername = (TextView)view.findViewById(R.id.noteusername);
            headerDateLinearLayout=(LinearLayout)view.findViewById(R.id.chlDHeaderDate);
            linearLayoutNote=(LinearLayout)view.findViewById(R.id.linearLayoutNote);
            linearLayoutCall=(LinearLayout)view.findViewById(R.id.linearLayoutCall);
            linearLayoutDeal=(LinearLayout)view.findViewById(R.id.linearLayoutDeal);
            linearLayoutAction=(LinearLayout)view.findViewById(R.id.linearLayoutAction);
            linearLayoutPaymet=(LinearLayout)view.findViewById(R.id.linearLayoutPayment);
            linearLayoutContainer=(LinearLayout)view.findViewById(R.id.chlContainer);
            //header Date
            textViewHeaderDate =(TextView)view.findViewById(R.id.tagDate);
            //Call
            textViewCallPhone=(TextView)view.findViewById(R.id.callPhone);
            textViewCallResult=(TextView)view.findViewById(R.id.callResult);
            textViewCallNote=(TextView)view.findViewById(R.id.callNote);
            textViewCallEdit=(TextView)view.findViewById(R.id.editCall);
            textViewCallDelete=(TextView)view.findViewById(R.id.deleteCall);
            //Note
            textViewNote=(TextView)view.findViewById(R.id.textNote);
            textViewNoteEdit=(TextView)view.findViewById(R.id.editNote);
            textViewNoteDelete=(TextView)view.findViewById(R.id.deleteNote);
            //Deal
            textViewDealName=(TextView)view.findViewById(R.id.dealName);
            textViewDealAmt=(TextView)view.findViewById(R.id.dealAmt);
            textViewDealDate=(TextView)view.findViewById(R.id.dealDate);
            textViewDealStage=(TextView)view.findViewById(R.id.dealStage);
            textViewDealCloseDate=(TextView)view.findViewById(R.id.dealClsDate);
            textViewDealEdit=(TextView)view.findViewById(R.id.editDeal);
            textViewDealDelete=(TextView)view.findViewById(R.id.deleteDeal);
            textViewDealNote = (TextView)view.findViewById(R.id.dealNoteLabel);
            viewMilestoneLabel = (TextView)view.findViewById(R.id.milesStoneLabel) ;
            //Action
            textViewActionName=(TextView)view.findViewById(R.id.actionTask);
            textViewActionAssignDate=(TextView)view.findViewById(R.id.actionAssDt);
            textViewActionAssignTo=(TextView)view.findViewById(R.id.actionAssTo);
            textViewActionAssignBy=(TextView)view.findViewById(R.id.actionAssBy);
            textViewActionEdit=(TextView)view.findViewById(R.id.editAction);
            textViewActionDelete=(TextView)view.findViewById(R.id.deleteAction);
            //Payment
            linearLayoutPaymentDetails=(LinearLayout)view.findViewById(R.id.linearLayoutPaymentDetails);
            textViewPaymentAmount=(TextView)view.findViewById(R.id.paymentAmount);
            textViewPaymentDetails=(TextView)view.findViewById(R.id.textViewPaymentDetails);
            textViewPaymentMode=(TextView)view.findViewById(R.id.paymentMode);
           /* textViewPaymentChequeNo=(TextView)view.findViewById(R.id.paymentChequeNo);
            textViewPaymentChequeDate=(TextView)view.findViewById(R.id.paymentChequeDate);
            textViewPaymentBank=(TextView)view.findViewById(R.id.paymentBank);
            textViewPaymentBranch=(TextView)view.findViewById(R.id.paymentBranch);*/
            textViewPaymentRemark=(TextView)view.findViewById(R.id.paymentRemark);
            textViewPaymentBy=(TextView)view.findViewById(R.id.paymnetAssBy);
            textViewPaymentUserName=(TextView)view.findViewById(R.id.paymnetUserName);
            textViewPaymentDate=(TextView)view.findViewById(R.id.paymnetDate);
            textViewPaymentEdit=(TextView)view.findViewById(R.id.editPayment);
            textViewPaymentDelete=(TextView)view.findViewById(R.id.deletePayment);
        }
    }
    public void visibilityHide(ArrayList<LinearLayout> linearLayout){
        for (int i=0;i<linearLayout.size();i++){
            linearLayout.get(i).setVisibility(View.GONE);
        }
    }
    public void visibilityShow(ArrayList<LinearLayout> linearLayout){
        for (int i=0;i<linearLayout.size();i++){
            linearLayout.get(i).setVisibility(View.VISIBLE);
        }
    }
}

