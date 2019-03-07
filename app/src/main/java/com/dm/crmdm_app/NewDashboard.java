package com.dm.crmdm_app;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.TextView;

import com.dm.common.GlobalMethods;
import com.dm.crmdm_app.databinding.ActivityNewDashboardBinding;
import com.dm.network.ApiConfig;
import com.dm.network.CallWebserviceRetrofit;
import com.dm.network.RetrofitCallbackListener;
import com.dm.swipe_controller.SwipeController;
import com.dm.swipe_controller.SwipeControllerActions;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static com.dm.network.ApiClient.getApiClientRetrofit;

public class NewDashboard extends AppCompatActivity implements RetrofitCallbackListener {
    AdapterContactList contactAdapter;
    List<JSONObject> contactList = new ArrayList<>();
    SwipeController swipeController;
    ActivityNewDashboardBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_dashboard);
        setHeader();
        setRecyclerView();
// configure the SlidingMenu
        findViewById(R.id.img_home).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  SlidingMenu menu = new SlidingMenu(NewDashboard.this);
                menu.setMode(SlidingMenu.LEFT);
                menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                menu.setShadowWidthRes(R.dimen.shadow_width);
                menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
                menu.setFadeDegree(0.35f);
                menu.attachToActivity(NewDashboard.this, SlidingMenu.SLIDING_CONTENT);
                menu.setMenu(R.layout.menu);*/
            }
        });
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NewDashboard.this, AddNewContactPage.class));
            }
        });
        getDataContactList();
    }

    private void getDataContactList() {
        ApiConfig config = getApiClientRetrofit().create(ApiConfig.class);
        JSONObject jsonObject = new JSONObject();
        Call<String> call = config.sendOtp(jsonObject.toString());
        new CallWebserviceRetrofit(call, NewDashboard.this, NewDashboard.this, true,
                "getContactList");
    }


    private void setRecyclerView() {
        contactAdapter = new AdapterContactList(NewDashboard.this, contactList);
        binding.rvContacts.setLayoutManager(new LinearLayoutManager(NewDashboard.this, LinearLayoutManager.VERTICAL, false));
        binding.rvContacts.setAdapter(contactAdapter);

        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
//                contactList.remove(position);
                contactAdapter.notifyItemRemoved(position);
                contactAdapter.notifyItemRangeChanged(position, contactAdapter.getItemCount());
            }

            @Override
            public void onLeftClicked(int position) {
                startActivity(new Intent(NewDashboard.this, AddNewContactPage.class)
                        /* .putExtra("itemInfo", contactList.get(position).toString())*/);
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(binding.rvContacts);

        binding.rvContacts.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });

    }

    private void setHeader() {
        findViewById(R.id.img_home).setVisibility(View.GONE);
        TextView headerText = findViewById(R.id.header_text);
        headerText.setText("Dashboard");
    }

    public void onAddContact(View v) {
        startActivity(new Intent(this, AddNewContactPage.class));
    }

    @Override
    public void callBackListener(String result, String action) {
        GlobalMethods.printLog("result dashboard contact list==" + result);
    }
}
