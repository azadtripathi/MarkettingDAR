package com.dm.library;

/*
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dm.crmdm_app.R;
import com.dm.model.CompanyDetails;
import com.dm.model.Owner;

import java.util.ArrayList;
*/
/**
 * Created by Administrator on 5/2/2017.
 *//*

public class CustomArrayAdapterAutoComplete extends BaseAdapter {

    // Declare Variables
    Context mContext;
    int rowlayout;
    LayoutInflater inflater;
    private ArrayList<CompanyDetails> data = null;

    protected int count;

    public CustomArrayAdapterAutoComplete(Context context, ArrayList<CompanyDetails> data, int rowlayout) {
        this.rowlayout=rowlayout;
        mContext = context;
        this.data = data;
        inflater = LayoutInflater.from(mContext);


    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CompanyDetails getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class ViewHolder {
        TextView companyName;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if(rowlayout== R.layout.company_name_autocomplete){
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.company_name_autocomplete, null);
                // Locate the TextView in listview_item.xml
                holder.companyName = (TextView) view.findViewById(R.id.companyName);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            holder.companyName.setText(data.get(position).getCompanyName());
        }
        return view;
    }

}
*/

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dm.crmdm_app.R;
import com.dm.model.CompanyDetails;

import java.util.ArrayList;
import java.util.List;


public class CustomArrayAdapterAutoComplete extends ArrayAdapter<CompanyDetails> {
    ArrayList<CompanyDetails> customers, tempCustomer, suggestions;

    public CustomArrayAdapterAutoComplete(Context context, ArrayList<CompanyDetails> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.customers = objects;
        this.tempCustomer = new ArrayList<CompanyDetails>(objects);
        this.suggestions = new ArrayList<CompanyDetails>(objects);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CompanyDetails customer = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.company_name_autocomplete, parent, false);
        }
        TextView companyName = (TextView) convertView.findViewById(R.id.companyName);
        TextView cityID = (TextView) convertView.findViewById(R.id.companyID);
        companyName.setText(customer.getCity());
        cityID.setText(customer.getId());
        return convertView;
    }


    @Override
    public Filter getFilter() {
        return myFilter;
    }

    Filter myFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            CompanyDetails customer = (CompanyDetails) resultValue;
            return customer.getCity();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (CompanyDetails city : tempCustomer) {
                    if (city.getCity().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(city);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<CompanyDetails> c = (ArrayList<CompanyDetails>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (CompanyDetails cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            }
        }
    };
}