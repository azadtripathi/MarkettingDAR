package com.dm.library;

import android.app.Activity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dm.controller.FindPaymentController;
import com.dm.crmdm_app.OnLoadMoreListener;
import com.dm.crmdm_app.R;

import java.util.List;

/**
 * Created by dataman on 1/5/2018.
 */

public class CustomFindPaymentTransferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private OnLoadMoreListener onLoadMoreListener;
    private boolean isLoading;
    private Activity activity;
    private List<FindPaymentController> contacts;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;

    public CustomFindPaymentTransferAdapter(RecyclerView recyclerView, List<FindPaymentController> contacts, Activity activity) {
        this.contacts = contacts;
        this.activity = activity;

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.onLoadMoreListener = mOnLoadMoreListener;
    }

    @Override
    public int getItemViewType(int position) {
        return contacts.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_find_payment_transfer_row, parent, false);
            return new CustomFindPaymentTransferAdapter.UserViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(activity).inflate(R.layout.item_loading, parent, false);
            return new CustomFindPaymentTransferAdapter.LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CustomFindPaymentTransferAdapter.UserViewHolder) {
            FindPaymentController findPaymentController = contacts.get(position);
            CustomFindPaymentTransferAdapter.UserViewHolder userViewHolder = (CustomFindPaymentTransferAdapter.UserViewHolder) holder;
            userViewHolder.fromDateTextView.setText(findPaymentController.getFromDate());
            userViewHolder.toDateTextView.setText(findPaymentController.getToDate());
            userViewHolder.personTextView.setText(findPaymentController.getParty());
        } else if (holder instanceof CustomFindPaymentTransferAdapter.LoadingViewHolder) {
            CustomFindPaymentTransferAdapter.LoadingViewHolder loadingViewHolder = (CustomFindPaymentTransferAdapter.LoadingViewHolder) holder;
            loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return contacts == null ? 0 : contacts.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View view) {
            super(view);
            progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
        }
    }

    private class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView fromDateTextView,toDateTextView,personTextView;

        public UserViewHolder(View view) {
            super(view);
            fromDateTextView = (TextView) view.findViewById(R.id.fromDate);
            toDateTextView = (TextView) view.findViewById(R.id.toDate);
            personTextView = (TextView) view.findViewById(R.id.person);
        }
    }
}