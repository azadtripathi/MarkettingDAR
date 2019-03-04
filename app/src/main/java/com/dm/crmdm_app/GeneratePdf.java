package com.dm.crmdm_app;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.controller.AreaController;
import com.dm.controller.DistributorController;
import com.dm.controller.Order1Controller;
import com.dm.controller.VisitController;
import com.dm.library.AlertOkDialog;
import com.dm.library.Custom_Toast;
import com.dm.library.DateAndTimePicker;
import com.dm.library.DateFunction;
import com.dm.library.IntentSend;
import com.dm.model.Distributor;
import com.dm.model.Order1;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.html.WebColors;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class GeneratePdf extends AppCompatActivity {
    EditText fromdateEditText;
    Button goButton;boolean mSuccess=false;
    Calendar c;String conper;AlertOkDialog dialogWithOutView ;
    SimpleDateFormat df;SharedPreferences preferences2;AutoCompleteTextView autoDist;
    ProgressDialog progressDialog;Order1Controller order1Controller;DistributorController distributorController;
    Distributor distributor;File file=null; VisitController visitController;AreaController areaController;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_pdf);

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
        tv.setText("Generate Pdf");
        preferences2=this.getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        conper=preferences2.getString("CONPERID_SESSION", null);
        visitController=new VisitController(GeneratePdf.this);
        areaController=new AreaController(GeneratePdf.this);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3F51B5")));
        actionBar.setTitle(Html.fromHtml("<font color='#FFFFFF'>PDF Report</font>"));
        fromdateEditText = (EditText) findViewById(R.id.fromdateTextOnDsr);
        autoDist=(AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        fromdateEditText.setInputType(InputType.TYPE_NULL);
        fromdateEditText.setFocusable(false);
        goButton = (Button) findViewById(R.id.gobutton);
        order1Controller=new Order1Controller(this);
        distributorController=new DistributorController(this);
        df = new SimpleDateFormat("dd/MMM/yyyy");
        c = Calendar.getInstance();
        fromdateEditText.setText(df.format(c.getTime()));
        fromdateEditText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePicker(v.getId());
            }
        });
        autoDist.setThreshold(3);
        autoDist.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

//				autoDist.setVisibility(View.VISIBLE);
//				autoDist.showDropDown();


            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {
                // TODO Auto-generated method stub
//				autoDist.showDropDown();
//				autoDist.setText(preferences1.getString("DIST_NAME_SESSION", ""));
            }

            @Override
            public void onTextChanged(CharSequence s, int arg1, int arg2,int arg3) {
                // TODO Auto-generated method stub
                String str = s.toString ();
                distributorController.open();
                ArrayList<String> temp = distributorController.getInputDistributorOrder((String)str);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(GeneratePdf.this,android.R.layout.simple_dropdown_item_1line, temp);
                autoDist.setAdapter(adapter) ;
                distributorController.close();
//                autoDist.showDropDown();
            }


        });

        autoDist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,	int arg2, long arg3) {
                distributorController.open();
                distributor=distributorController.getDistributerDetail(autoDist.getText().toString());
                distributorController.close();
                if(distributor==null)
                {
                    new Custom_Toast(getApplicationContext(), "Select valid distributor").showCustomAlert();
                    autoDist.setText("");

                }
                else
                {
//    	  preferences1=DistributerDashboard.this.getSharedPreferences("DISTRIBUTOT_SESSION_DATA",Context.MODE_PRIVATE);
//
//    	  Editor editor=preferences.edit();
//			editor.putString("DIST_NAME_SESSION",distributor.getDistributor_name()+"-"+distributor.getSync_id());
//			editor.commit();

                }
            }


        });
        goButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(autoDist.getText().toString().isEmpty())
                {
                    new Custom_Toast(getApplicationContext(), "Distributor name is required").showCustomAlert();
                }

//                else if(distributor==null)
//                {
//                    new Custom_Toast(getApplicationContext(), "Select valid distributor").showCustomAlert();
//                    autoDist.setText("");
//
//                }
                else
                {
                    if(distributor!=null)
                {
                    new GeneratePdfInBackground().execute();


                }
                    else {
                        new Custom_Toast(getApplicationContext(), "Select valid distributor").showCustomAlert();
                        autoDist.setText("");

                    }
                }




//                progressDialog = ProgressDialog.show(GeneratePdf.this, "processing Data", "Processing Please wait...", true);

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

        if (id == R.id.fromdateTextOnDsr) {
            date.setCallBack(ondate);
            date.show(getSupportFragmentManager(), "Date Picker");
        }


    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {
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
                System.out.println(date);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            fromdateEditText.setText((format2.format(date)));

        }
    };





    public boolean generatePdf(String docDate, Distributor distributor) throws FileNotFoundException {

      Font bfBold12 = new Font(FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
      Font bf12 = new Font(FontFamily.TIMES_ROMAN, 12);
        Font bfBold = new Font(FontFamily.TIMES_ROMAN, 15, Font.BOLD, new BaseColor(0, 0, 0));
        ArrayList<Order1>orders;
        String OPEN_STOCK="0";
        String CLOSE_STOCK="0";
        String AREA="";
        String Beat="";
        float sum=0;
        visitController.open();
        CLOSE_STOCK=visitController.getDistStock(docDate,distributor.getDistributor_id());
        visitController.close();
        areaController.open();
        AREA=areaController.getAreaName(distributor.getAreaId());
        areaController.close();

        visitController.open();
        OPEN_STOCK=visitController.getDistStock(DateFunction.addDaysToDate(docDate,"-1","dd/MMM/yyyy","dd/MMM/yyyy"),distributor.getDistributor_id());
        visitController.close();
        order1Controller.open();
        orders=order1Controller.getOrderDataList(docDate,distributor.getAreaId());
        order1Controller.close();
//        UserDataController userDataController=new UserDataController(this);
//        userDataController.open();
//        SMAN=userDataController.getSmanName(conper);
//        userDataController.close();
//        CityController cityController=new CityController(this);
//        cityController.open();
//        CITY=cityController.getCityName(distributor.getCity_id());
//        cityController.close();
        if(orders!=null && orders.size()>0)
        {
            Beat=orders.get(0).getBeatId();
        }
        if(Beat == null){
            Beat="";
        }
        Document doc=null;
        try {
            File dir = new File(
                    Environment.getExternalStorageDirectory(),
                    "DmCRM");

            if (!dir.exists())
                dir.mkdirs();

            Date date = new Date() ;
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
            file = new File(dir.getPath()+"/"+"DmCrm" + timeStamp + ".pdf");
            FileOutputStream fOut = new FileOutputStream(file);

            doc = new Document(PageSize.A4);
            PdfWriter.getInstance(doc, fOut);
            doc.open();
            Paragraph p1 = new Paragraph("Retailing Summary");
            p1.setAlignment(Paragraph.ALIGN_CENTER);
            p1.setFont(bfBold);
              Paragraph p2 = new Paragraph("Date:- "+docDate);
            p2.setAlignment(Paragraph.ALIGN_LEFT);
            p2.setFont(bfBold);

//            Paragraph p1 = new Paragraph(distributor.getDistributor_name()+"\n"+distributor.getAddress1()+", "+"\n"+CITY);
//            p1.setAlignment(Paragraph.ALIGN_CENTER);

            float[] columnWidths = {1.0f,3.0f,3.0f,3.0f,2.5f,1.5f};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100f);
            //insert column headings
            insertCell(table, "", Element.ALIGN_LEFT, 1, bfBold12,false,"");
            insertCell(table, "Customer Name:-", Element.ALIGN_LEFT, 1, bfBold12,false,"");
            insertCell(table, distributor.getDistributor_name(), Element.ALIGN_LEFT, 1, bfBold12,false,"");
            insertCell(table, "Area:-", Element.ALIGN_LEFT, 1, bfBold12,false,"");
            insertCell(table, AREA, Element.ALIGN_LEFT, 1, bfBold12,false,"");
            insertCell(table, "", Element.ALIGN_LEFT, 1, bfBold12,false,"");
            table.setHeaderRows(1);


            insertCell(table, "", Element.ALIGN_LEFT, 1, bfBold12,false,"");
            insertCell(table, "Open Stock:-", Element.ALIGN_LEFT, 1, bfBold12,false,"");
            insertCell(table, OPEN_STOCK, Element.ALIGN_RIGHT, 1, bfBold12,false,"");
            insertCell(table, "Closing Stock:-", Element.ALIGN_LEFT, 1, bfBold12,false,"");
            insertCell(table, CLOSE_STOCK, Element.ALIGN_RIGHT, 1, bfBold12,false,"");
            insertCell(table, "", Element.ALIGN_LEFT, 1, bfBold12,false,"");
            table.setHeaderRows(1);

            insertCell(table, "", Element.ALIGN_LEFT, 1, bfBold12,false,"");
            insertCell(table, "Beat Name:-", Element.ALIGN_LEFT, 1, bfBold12,false,"");
            insertCell(table, Beat, Element.ALIGN_LEFT, 1, bfBold12,false,"");
            insertCell(table, "", Element.ALIGN_LEFT, 1, bfBold12,false,"");
            insertCell(table, "", Element.ALIGN_LEFT, 1, bfBold12,false,"");
            insertCell(table, "", Element.ALIGN_LEFT, 1, bfBold12,false,"");
            table.setHeaderRows(1);

            //insert column headings
            insertCell(table, "S.N.", Element.ALIGN_RIGHT, 1, bfBold12,true,"");
            insertCell(table, "Retailer Name", Element.ALIGN_LEFT, 1, bfBold12,true,"");
            insertCell(table, "Address", Element.ALIGN_LEFT, 1, bfBold12,true,"");
            insertCell(table, "Item Code", Element.ALIGN_RIGHT, 1, bfBold12,true,"");
            insertCell(table, "Item Name", Element.ALIGN_LEFT, 1, bfBold12,true,"");
            insertCell(table, "Qty(Kg)", Element.ALIGN_RIGHT, 1, bfBold12,true,"");
            table.setHeaderRows(1);

            for(int i=0;i<orders.size();i++)
            {
                sum=sum+Float.parseFloat(orders.get(i).getAmount());
                System.out.println(sum);
//                Paragraph p4 = new Paragraph(orders.get(i).getPartyId()+"\t"+orders.get(i).getPartyName()+"\t"+orders.get(i).getItemId()+"\t"+orders.get(i).getItemName()+"\t"+orders.get(i).getStdPkg()+"\tNA");
//                doc.add(p4);
                insertCell(table, String.valueOf(i+1), Element.ALIGN_RIGHT, 1, bfBold12,false,"");
                insertCell(table, orders.get(i).getPartyName(), Element.ALIGN_LEFT, 1, bfBold12,false,"");
                insertCell(table, orders.get(i).getAndPartyId(), Element.ALIGN_LEFT, 1, bfBold12,false,"");
                insertCell(table, orders.get(i).getItemId(), Element.ALIGN_RIGHT, 1, bfBold12,false,"");
                insertCell(table,orders.get(i).getItemName(), Element.ALIGN_LEFT, 1, bfBold12,false,"");
                insertCell(table, orders.get(i).getQty(), Element.ALIGN_RIGHT, 1, bfBold12,false,"");


            }
            //add paragraph to document
//            Paragraph p5 = new Paragraph("Total: "+sum);
//            p5.setAlignment(Paragraph.ALIGN_RIGHT);
//            doc.add(p5);
             sum= (float) Math.round(sum * 100) / 100;
            insertCell(table, "", Element.ALIGN_RIGHT, 1, bfBold12,true,"total");
            insertCell(table, "", Element.ALIGN_RIGHT, 1, bfBold12,true,"total");
            insertCell(table, "Total:-    "+String.valueOf(sum), Element.ALIGN_LEFT, 1, bfBold12,true,"total");
            insertCell(table, "", Element.ALIGN_RIGHT, 1, bfBold12,true,"total");
            insertCell(table,"", Element.ALIGN_RIGHT, 1, bfBold12,true,"total");
            insertCell(table,"", Element.ALIGN_RIGHT, 1, bfBold12,true,"total");
            p2.add(table);
            doc.add(p1);
            doc.add(p2);


        } catch (DocumentException de) {
            mSuccess=true;
            Log.e("PDFCreator", "DocumentException:" + de);
        } catch (IOException e) {
            mSuccess=true;
            Log.e("PDFCreator", "ioException:" + e);
        }
        catch (Exception e) {
            mSuccess=true;
            Log.e("PDFCreator", "ioException:" + e);
        }
        finally {
            doc.close();
        }
        return mSuccess;
        //viewPdf("newFile.pdf", "Dir");
    }


    @Override
    public void onStart() {
        super.onStart();

        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GeneratePdf Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.dm.crmdm_app/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "GeneratePdf Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.dm.crmdm_app/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    String fromDate;
    boolean st=false;
    class GeneratePdfInBackground extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            progressDialog = ProgressDialog.show(GeneratePdf.this, "processing Data", "Processing Please wait...", true);
            fromDate = fromdateEditText.getText().toString();

        }
        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            progressDialog.dismiss();
            if(!st)
            {
                progressDialog.dismiss();

                android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(GeneratePdf.this);
                builder.setMessage("PDF Generated Successfully");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (file.exists())
                        {
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            Uri uri = Uri.fromFile(file);
                            intent.setDataAndType(uri, "application/pdf");
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                            try
                            {
                                startActivity(intent);
                            }
                            catch(ActivityNotFoundException e)
                            {
                                Toast.makeText(GeneratePdf.this, "No Application available to view pdf", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
                builder.create().show();


               /* dialogWithOutView= AlertOkDialog.newInstance("PDF Generated Successfully");
                dialogWithOutView.show(getFragmentManager(), "MyDialogBox");*/
            }
            else{
                progressDialog.dismiss();
                dialogWithOutView= AlertOkDialog.newInstance("PDF Not Generated");
                dialogWithOutView.show(getFragmentManager(), "MyDialogBox");

            }
        }
        @Override
        protected String doInBackground(String... params) {


            try {



                 st = generatePdf(fromDate,distributor);

            }
            catch(Exception e)
            {
System.out.println(e);
            }

            return null;
        }
    }
    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font, boolean color, String total ){

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        if(color)
        {
            if(total.equalsIgnoreCase("total"))
            {
                BaseColor myColor = WebColors.getRGBColor("#e6ecb9");
                cell.setBackgroundColor(myColor);
            }
            else {
                cell.setBackgroundColor(BaseColor.GREEN);
            }
        }
        //add the call to the table
        table.addCell(cell);

    }
    @Override
    public void onBackPressed()
    {
        finish();
        Intent i = new Intent(GeneratePdf.this, DashBoradActivity.class);
        startActivity(i);
        super.onBackPressed();
    }
}

