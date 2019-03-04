package com.dm.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.crmdm_app.R;
import com.dm.crmdm_app.TourPlanPopupAproval;
import com.dm.model.City;

import java.util.ArrayList;

/**
 * Created by Dataman on 3/19/2017.
 */
public class TourCityDropDownAprovalListAdapter extends BaseAdapter implements Filterable {
    private ArrayList<City> _Contacts;
    private ArrayList<String> mListItems;
    private ArrayList<City> mListItemsId;
    private LayoutInflater mInflater;
    private TextView mSelectedItems;
    private ListView _cityListView;
    private  int selectedCount = 0;
    private static String firstSelected = "";
    private static String firstSelectedId = "";
    private ViewHolder holder;
    private static String selected = "";	//shortened selected values representation
    private static String selectedId = "";
    SharedPreferences preferences2;
    private ValueFilter valueFilter;
    Context _context;
    DistributorAccessInterface distributorAccessInterface;
    public static String getSelected() {
        return selected;
    }

    public static void setSelected(String selected) {
        TourCityDropDownAprovalListAdapter.selected = selected;
    }

    public static String getSelectedId() {
        return selectedId;
    }

    public static void setSelectedId(String selectedId) {
        TourCityDropDownAprovalListAdapter.selectedId = selectedId;
    }

    public TourCityDropDownAprovalListAdapter(Context context, ArrayList<String> items,
                                              TextView tv, ArrayList<City> ArraycityList1,
                                              DistributorAccessInterface distributorAccessInterface) {
        preferences2=context.getSharedPreferences("MyTourCity",Context.MODE_PRIVATE);
        mListItems = new ArrayList<String>();
        mListItems.addAll(items);
        mInflater = LayoutInflater.from(context);
        mSelectedItems = tv;
        mListItemsId=ArraycityList1;
        _context=context;
        this.distributorAccessInterface=distributorAccessInterface;
        for(int i = 0; i< TourPlanPopupAproval.checkSelected.length; i++)
        {
            if(TourPlanPopupAproval.checkSelected[i])
            {
                selectedCount++;
            }
        }

    }



    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mListItems.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.drop_down_list_row, null);
            holder = new ViewHolder();
            holder.tv = (TextView) convertView.findViewById(R.id.SelectOption);
            holder.chkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv.setText(mListItems.get(position));

        final int position1 = position;

        //whenever the checkbox is clicked the selected values textview is updated with new selected values
        holder.chkbox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setText(position1);
            }
        });



        try{
            if(TourPlanPopupAproval.checkSelected[position])
            {
                holder.chkbox.setChecked(true);

            }
            else
                holder.chkbox.setChecked(false);

        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return convertView;
    }


    /*
     * Function which updates the selected values display and information(checkSelected[])
     * */
    public void setText(int position1){
        if (!TourPlanPopupAproval.checkSelected[position1]) {
            TourPlanPopupAproval.checkSelected[position1] = true;
            selectedCount++;
        } else {
            TourPlanPopupAproval.checkSelected[position1] = false;
            selectedCount--;
        }

        if (selectedCount == 0) {
            mSelectedItems.setText("None selected");
        }


        else if (selectedCount == 1) {
            for (int i = 0; i < TourPlanPopupAproval.checkSelected.length; i++) {
                if (TourPlanPopupAproval.checkSelected[i] == true) {
                    firstSelected = mListItems.get(i);
                    if(mListItemsId.get(i).getCity_name().equals(mListItems.get(i)))
                    {
                        firstSelectedId = mListItemsId.get(i).getCity_id();

                    }
                    break;
                }
            }
            mSelectedItems.setText(firstSelected);
            setSelected(firstSelected);
            setSelectedId(firstSelectedId);
            SharedPreferences.Editor editor=preferences2.edit();
            editor.putString("IDS",firstSelectedId);
            editor.putString("NAMES",firstSelected);
            editor.commit();

        }
        else if (selectedCount > 1) {
            firstSelected="";
            firstSelectedId="";
            for (int i = 0; i < TourPlanPopupAproval.checkSelected.length; i++) {
                if (TourPlanPopupAproval.checkSelected[i] == true) {
//					firstSelected = mListItems.get(i);
                    firstSelected=firstSelected+mListItems.get(i)+",";
                    if(mListItemsId.get(i).getCity_name().equals(mListItems.get(i)))
                    {
                        firstSelectedId=firstSelectedId+mListItemsId.get(i).getCity_id()+",";

                    }
//										break;
                }
            }
            if (selectedCount<=3)
            {
                mSelectedItems.setText(trimEnd(firstSelected.trim(),','));
            }
            else if(selectedCount>3)
            {
                mSelectedItems.setText(selectedCount + "selected");
            }
//			setSelected(firstSelected + " & "+ (selectedCount - 1) + " more");
            setSelected(trimEnd(firstSelected.trim(),','));
            setSelectedId(trimEnd(firstSelectedId.trim(),','));
            SharedPreferences.Editor editor=preferences2.edit();
            editor.putString("IDS",trimEnd(firstSelectedId.trim(),','));
            editor.putString("NAMES",trimEnd(firstSelected.trim(),','));
            editor.commit();
        }
        distributorAccessInterface.getDistUpdate(preferences2.getString("IDS","0"));

    }

    private class ViewHolder {
        TextView tv;
        CheckBox chkbox;
    }

    public static String trimEnd(String text, char character) {
        String normalizedText;
        int index;

        if (text.equals("")||text==null) {
            return text;
        }

        normalizedText = text.trim();
        index = normalizedText.length() - 1;

        while (normalizedText.charAt(index) == character) {
            if (--index < 0) {
                return "";
            }
        }
        String ex=normalizedText.substring(0, index + 1).trim();
//		    return normalizedText.substring(0, index + 1).trim();
        return ex;
    }


    @Override
    public Filter getFilter() {
        if(valueFilter==null) {

            valueFilter=new ValueFilter();
        }

        return valueFilter;
    }
    private class ValueFilter extends Filter {

        //Invoked in a worker thread to filter the data according to the constraint.
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(constraint!=null && constraint.length()>0){
                ArrayList<City> filterList=new ArrayList<City>();
                for(int i=0;i<mListItemsId.size();i++){
                    if((mListItemsId.get(i).getCity_name().toUpperCase())
                            .contains(constraint.toString().toUpperCase())) {
                        City contacts = new City();
                        contacts.setCity_name(mListItemsId.get(i).getCity_name());
                        contacts.setCity_id(mListItemsId.get(i).getCity_id());
                        filterList.add(contacts);
                    }
                }
                results.count=filterList.size();
                results.values=filterList;
            }else{
                results.count=mListItemsId.size();
                results.values=mListItemsId;
            }
            return results;
        }




        //Invoked in the UI thread to publish the filtering results in the user interface.
        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            _Contacts=(ArrayList<City>) results.values;
//	         TourCityDropDownListAdapter  adapter = new TourCityDropDownListAdapter(_context, mListItems, mSelectedItems,_Contacts);
//	         _cityListView.setAdapter(adapter);
            notifyDataSetChanged();
        }
    }

    public interface DistributorAccessInterface {
        public void getDistUpdate(String cityId);
    }

}