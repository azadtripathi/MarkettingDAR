package com.dm.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.dm.common.GlobalMethods;
import com.dm.crmdm_app.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallWebserviceRetrofit {
    private ProgressDialog dialog;
    private Activity mActivity;
    private RetrofitCallbackListener callbackListener;
    private boolean showDialog;
    private String action;
    private Call<String> call;

    public CallWebserviceRetrofit(Call<String> call, Activity mActivity, RetrofitCallbackListener callbackListener,
                                  boolean showDialog,String action) {
        this.mActivity = mActivity;
        this.callbackListener = callbackListener;
        this.showDialog = showDialog;
        this.call = call;
        this.action = action;
        if (GlobalMethods.isConnected(mActivity))
            callRetrofit();
        else
            GlobalMethods.showCustomToastInCenter(mActivity, mActivity.getResources().getString(R.string.network_not_available));
    }

    private void callRetrofit() {
        dialog = new ProgressDialog(mActivity);
        dialog.setMessage("Please wait...");
        dialog.setCancelable(false);
        if (dialog != null && !dialog.isShowing() && showDialog)
            dialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                if (callbackListener != null)
                    callbackListener.callBackListener(response.body(),action);

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
                Toast.makeText(mActivity, "Something went wrong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
