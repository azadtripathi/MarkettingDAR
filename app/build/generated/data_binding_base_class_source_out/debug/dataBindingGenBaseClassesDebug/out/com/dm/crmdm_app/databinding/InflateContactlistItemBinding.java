package com.dm.crmdm_app.databinding;

import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public abstract class InflateContactlistItemBinding extends ViewDataBinding {
  @NonNull
  public final TextView addPhNo;

  @NonNull
  public final TextView companyName;

  @NonNull
  public final TextView dealer;

  @NonNull
  public final TextView industrySubIndustry;

  @NonNull
  public final TextView name;

  @NonNull
  public final TextView onDate;

  @NonNull
  public final TextView productName;

  @NonNull
  public final TextView remark;

  @NonNull
  public final RelativeLayout rl1;

  @NonNull
  public final RelativeLayout rl2;

  @NonNull
  public final TextView validTo;

  protected InflateContactlistItemBinding(DataBindingComponent _bindingComponent, View _root,
      int _localFieldCount, TextView addPhNo, TextView companyName, TextView dealer,
      TextView industrySubIndustry, TextView name, TextView onDate, TextView productName,
      TextView remark, RelativeLayout rl1, RelativeLayout rl2, TextView validTo) {
    super(_bindingComponent, _root, _localFieldCount);
    this.addPhNo = addPhNo;
    this.companyName = companyName;
    this.dealer = dealer;
    this.industrySubIndustry = industrySubIndustry;
    this.name = name;
    this.onDate = onDate;
    this.productName = productName;
    this.remark = remark;
    this.rl1 = rl1;
    this.rl2 = rl2;
    this.validTo = validTo;
  }

  @NonNull
  public static InflateContactlistItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static InflateContactlistItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable DataBindingComponent component) {
    return DataBindingUtil.<InflateContactlistItemBinding>inflate(inflater, com.dm.crmdm_app.R.layout.inflate_contactlist_item, root, attachToRoot, component);
  }

  @NonNull
  public static InflateContactlistItemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  @NonNull
  public static InflateContactlistItemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable DataBindingComponent component) {
    return DataBindingUtil.<InflateContactlistItemBinding>inflate(inflater, com.dm.crmdm_app.R.layout.inflate_contactlist_item, null, false, component);
  }

  public static InflateContactlistItemBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  public static InflateContactlistItemBinding bind(@NonNull View view,
      @Nullable DataBindingComponent component) {
    return (InflateContactlistItemBinding)bind(component, view, com.dm.crmdm_app.R.layout.inflate_contactlist_item);
  }
}
