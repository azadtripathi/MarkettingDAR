package com.dm.crmdm_app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dm.controller.CollectionController;
import com.dm.controller.DemoTransactionController;
import com.dm.controller.FailedVisitController;
import com.dm.controller.OrderController;
import com.dm.controller.OrderTransactionController;
import com.dm.controller.PorderController;
import com.dm.controller.TransCollectionController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReportSummary extends Activity {

    TextView DaySecondary,NoofRetailerVisit,NoofRetailerVisitView,NoofProductiveVisit,
            CurrentMonthSecondary,CurrentMonthSecondaryView,CurrentMonthPrimary,CurrentMonthPrimaryView,
            DayCollection,DayCollectionView,NoOfPartyFailedVisit,NoOfDistFailedVisit,NoOfDemo,DayPrimary,DayPrimaryView,PrimaryCollection;
    ArrayList<String> reportArrayList;String rdate="";
    OrderTransactionController orderTransactionController;
    OrderController orderController; CollectionController collectionController;
    TransCollectionController transCollectionController;PorderController porderController;
    int sum=0;DemoTransactionController demoTransactionController;FailedVisitController failedVisitController;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog_box);
        setTitle("Summary");
        DaySecondary=(TextView)findViewById(R.id.DaySecondary);
        NoofRetailerVisit=(TextView)findViewById(R.id.NoofRetailerVisit);
        NoofProductiveVisit=(TextView)findViewById(R.id.NoofProductiveVisit);
        CurrentMonthSecondary=(TextView)findViewById(R.id.CurrentMonthSecondary);
        CurrentMonthSecondaryView=(TextView)findViewById(R.id.CurrentMonthSecondaryView);
        CurrentMonthPrimary=(TextView)findViewById(R.id.CurrentMonthPrimary);
        CurrentMonthPrimaryView=(TextView)findViewById(R.id.CurrentMonthPrimaryView);
        DayCollection=(TextView)findViewById(R.id.DayCollection);
        DayCollectionView=(TextView)findViewById(R.id.DayCollectionView);
        DayCollectionView.setText("Day Secondary Collection");
        NoOfPartyFailedVisit=(TextView)findViewById(R.id.NoofpartyFailedVisit);
        NoOfDistFailedVisit=(TextView)findViewById(R.id.NoofdistFailedVisit);
        NoOfDemo=(TextView)findViewById(R.id.NoofDemo);
        DayPrimary=(TextView)findViewById(R.id.dayPrimary);
        DayPrimaryView=(TextView)findViewById(R.id.dayPrimaryView);
        PrimaryCollection=(TextView)findViewById(R.id.primaryCollection);
        NoofRetailerVisitView=(TextView)findViewById(R.id.NoofRetailerVisitView);
        NoofRetailerVisit.setVisibility(View.GONE);
        NoofRetailerVisitView.setVisibility(View.GONE);
        CurrentMonthSecondary.setVisibility(View.GONE);
        CurrentMonthSecondaryView.setVisibility(View.GONE);
        CurrentMonthPrimary.setVisibility(View.GONE);
        CurrentMonthPrimaryView.setVisibility(View.GONE);
        DayPrimary.setVisibility(View.GONE);
        DayPrimaryView.setVisibility(View.GONE);
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MMM/yyyy");
        rdate=dateFormat.format(calendar.getTime());
        failedVisitController=new FailedVisitController(this);
        orderTransactionController=new OrderTransactionController(this);
        orderController=new OrderController(this);
        collectionController=new CollectionController(this);
        transCollectionController = new TransCollectionController(this);
        porderController=new PorderController(this);
        orderTransactionController.open();
        reportArrayList=orderTransactionController.getReportData(rdate);
        orderTransactionController.close();
        orderController.open();
        NoofProductiveVisit.setText(String.valueOf(orderController.getNoOfProductiveVisit(rdate)));
        orderController.close();
        demoTransactionController=new DemoTransactionController(this);
        demoTransactionController.open();
        NoOfDemo.setText(String.valueOf(demoTransactionController.getNoOfDemo(rdate)));
        demoTransactionController.close();
        failedVisitController.open();
        NoOfPartyFailedVisit.setText(String.valueOf(failedVisitController.getNoOfPartyFailedVisit(rdate)));
        failedVisitController.close();
        failedVisitController.open();
        NoOfDistFailedVisit.setText(String.valueOf(failedVisitController.getNoOfDistFailedVisit(rdate)));
        failedVisitController.close();
        orderController.open();
        DaySecondary.setText(orderController.getDaySecondary(rdate));
        orderController.close();
        CurrentMonthSecondary.setText(reportArrayList.get(0));
//        NoofProductiveVisit.setText(reportArrayList.get(1));
//        NoofRetailerVisit.setText(reportArrayList.get(2));
//        NoOfFailedVisit.setText(reportArrayList.get(3));
//        NoOfDemo.setText(reportArrayList.get(4));
//        DaySecondary.setText(String.valueOf((int)Double.parseDouble(reportArrayList.get(6))));
        CurrentMonthPrimary.setText(reportArrayList.get(7));
        collectionController.open();
        PrimaryCollection.setText(String.valueOf(collectionController.getTotalPrimaryCollection(rdate)));
        collectionController.close();
        transCollectionController.open();
        DayCollection.setText(String.valueOf(transCollectionController.getTotalSecondaryCollection(rdate)));
        transCollectionController.close();
        porderController.open();
        DayPrimary.setText(String.valueOf(porderController.getTotalDayPrimary(rdate)));
        porderController.close();




    }
}