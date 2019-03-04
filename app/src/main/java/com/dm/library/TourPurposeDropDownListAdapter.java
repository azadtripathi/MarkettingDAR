package com.dm.library;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.dm.crmdm_app.R;
import com.dm.crmdm_app.TourPlanEntry;
import com.dm.model.Purpose;

import java.util.ArrayList;

/**
 * Created by Dataman on 3/19/2017.
 */
public class TourPurposeDropDownListAdapter extends BaseAdapter {
  
    private ArrayList<String> mListItems;
    private ArrayList<Purpose> mListItemsId;
    private LayoutInflater mInflater;
    private TextView mSelectedItems;
    private ListView _cityListView;
    private int selectedCount = 0;
    private static String firstSelected = "";
    private static String firstSelectedId = "";
    private ViewHolder holder;
    private static String selected = "";    //shortened selected values representation
    private static String selectedId = "";
    SharedPreferences preferences2;
    Context _context;

    public static String getSelected() {
        return selected;
    }

    public static void setSelected(String selected) {
        TourPurposeDropDownListAdapter.selected = selected;
    }

    public static String getSelectedId() {
        return selectedId;
    }

    public static void setSelectedId(String selectedId) {
        TourPurposeDropDownListAdapter.selectedId = selectedId;
    }

    public TourPurposeDropDownListAdapter(Context context, ArrayList<String> items,
                                              TextView tv, ArrayList<Purpose> ArraycityList1) {
        preferences2 = context.getSharedPreferences("MyTourPurpose", Context.MODE_PRIVATE);
        mListItems = new ArrayList<String>();
        mListItems.addAll(items);
        mInflater = LayoutInflater.from(context);
        mSelectedItems = tv;
        mListItemsId = ArraycityList1;
        _context = context;

        for (int i = 0; i < TourPlanEntry.checkSelectedPurpose.length; i++) {
            if (TourPlanEntry.checkSelectedPurpose[i]) {
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


        try {
            if (TourPlanEntry.checkSelectedPurpose[position]) {
                holder.chkbox.setChecked(true);

            } else
                holder.chkbox.setChecked(false);

        } catch (Exception e) {
            System.out.println(e);
        }
        return convertView;
    }


    /*
     * Function which updates the selected values display and information(checkSelectedPurpose[])
     * */
    public void setText(int position1) {
        if (!TourPlanEntry.checkSelectedPurpose[position1]) {
            TourPlanEntry.checkSelectedPurpose[position1] = true;
            selectedCount++;
        } else {
            TourPlanEntry.checkSelectedPurpose[position1] = false;
            selectedCount--;
        }

        if (selectedCount == 0) {
            mSelectedItems.setText("None selected");
        } else if (selectedCount == 1) {
            for (int i = 0; i < TourPlanEntry.checkSelectedPurpose.length; i++) {
                if (TourPlanEntry.checkSelectedPurpose[i] == true) {
                    firstSelected = mListItems.get(i);
                    if (mListItemsId.get(i).getName().equals(mListItems.get(i))) {
                        firstSelectedId = mListItemsId.get(i).getId();

                    }
                    break;
                }
            }
            mSelectedItems.setText(firstSelected);
            setSelected(firstSelected);
            setSelectedId(firstSelectedId);
            SharedPreferences.Editor editor = preferences2.edit();
            editor.putString("IDS", firstSelectedId);
            editor.putString("NAMES", firstSelected);
            editor.commit();

        } else if (selectedCount > 1) {
            firstSelected = "";
            firstSelectedId = "";
            for (int i = 0; i < TourPlanEntry.checkSelectedPurpose.length; i++) {
                if (TourPlanEntry.checkSelectedPurpose[i] == true) {
//					firstSelected = mListItems.get(i);
                    firstSelected = firstSelected + mListItems.get(i) + ",";
                    if (mListItemsId.get(i).getName().equals(mListItems.get(i))) {
                        firstSelectedId = firstSelectedId + mListItemsId.get(i).getId() + ",";

                    }
//										break;
                }
            }
            if (selectedCount <= 3) {
                mSelectedItems.setText(trimEnd(firstSelected.trim(), ','));
            } else if (selectedCount > 3) {
                mSelectedItems.setText(selectedCount + "selected");
            }
//			setSelected(firstSelected + " & "+ (selectedCount - 1) + " more");
            setSelected(trimEnd(firstSelected.trim(), ','));
            setSelectedId(trimEnd(firstSelectedId.trim(), ','));
            SharedPreferences.Editor editor = preferences2.edit();
            editor.putString("IDS", trimEnd(firstSelectedId.trim(), ','));
            editor.putString("NAMES", trimEnd(firstSelected.trim(), ','));
            editor.commit();
        }

    }

    private class ViewHolder {
        TextView tv;
        CheckBox chkbox;
    }

    public static String trimEnd(String text, char character) {
        String normalizedText;
        int index;

        if (text.equals("") || text == null) {
            return text;
        }

        normalizedText = text.trim();
        index = normalizedText.length() - 1;

        while (normalizedText.charAt(index) == character) {
            if (--index < 0) {
                return "";
            }
        }
        String ex = normalizedText.substring(0, index + 1).trim();
//		    return normalizedText.substring(0, index + 1).trim();
        return ex;
    }
}
