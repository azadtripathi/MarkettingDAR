package com.dm.crmdm_app;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.List;

public class AdapterContactList extends RecyclerView.Adapter<AdapterContactList.ContactViewHolder> {

    private List<JSONObject> contactList;
    private Activity activity;



    public AdapterContactList(Activity activity,List<JSONObject> contactList) {
        this.activity = activity;
        this.contactList = contactList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.inflate_contactlist_item, parent, false);

        return new ContactViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return 10 /*contactList.size()*/;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {

        public ContactViewHolder(View view) {
            super(view);
        }
    }
}
