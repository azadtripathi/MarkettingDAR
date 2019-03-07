package com.dm.crmdm_app.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public abstract class ActivityNewDashboardBinding extends ViewDataBinding {
  @NonNull
  public final FloatingActionButton fab;

  @NonNull
  public final ImageView icon;

  @NonNull
  public final LinearLayout layoutchild;

  @NonNull
  public final RecyclerView rvContacts;

  @NonNull
  public final TextView txtchild;

  protected ActivityNewDashboardBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, FloatingActionButton fab, ImageView icon, LinearLayout layoutchild,
      RecyclerView rvContacts, TextView txtchild) {
    super(_bindingComponent, _root, _localFieldCount);
    this.fab = fab;
    this.icon = icon;
    this.layoutchild = layoutchild;
    this.rvContacts = rvContacts;
    this.txtchild = txtchild;
  }

  @NonNull
  public static ActivityNewDashboardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityNewDashboardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityNewDashboardBinding>inflate(inflater, com.dm.crmdm_app.R.layout.activity_new_dashboard, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityNewDashboardBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static ActivityNewDashboardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<ActivityNewDashboardBinding>inflate(inflater, com.dm.crmdm_app.R.layout.activity_new_dashboard, null, false, component);
  }

  public static ActivityNewDashboardBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static ActivityNewDashboardBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (ActivityNewDashboardBinding)bind(component, view, com.dm.crmdm_app.R.layout.activity_new_dashboard);
  }
}
