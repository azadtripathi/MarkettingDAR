package com.dm.crmdm_app;

/**
 * Created by Administrator on 6/14/2017.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dm.library.IntentSend;

public class FragmentTab extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_layout, container, false);
      //  ((AppCompatActivity)getActivity()).getSupportActionBar().setSubtitle(R.string.subtitle);
        ImageView iv = (ImageView)v.findViewById(R.id.adcocimage);

        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new IntentSend(getActivity(),DashBoradActivity.class)).toSendAcivity();
                getActivity().finish();
            }
        });

        return v;
    }
}