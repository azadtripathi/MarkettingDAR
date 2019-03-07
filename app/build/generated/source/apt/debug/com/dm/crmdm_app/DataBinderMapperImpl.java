package com.dm.crmdm_app;

import android.databinding.DataBinderMapper;
import android.databinding.DataBindingComponent;
import android.databinding.ViewDataBinding;
import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import com.dm.crmdm_app.databinding.ActivityNewDashboardBindingImpl;
import com.dm.crmdm_app.databinding.InflateContactlistItemBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYNEWDASHBOARD = 1;

  private static final int LAYOUT_INFLATECONTACTLISTITEM = 2;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(2);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.dm.crmdm_app.R.layout.activity_new_dashboard, LAYOUT_ACTIVITYNEWDASHBOARD);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.dm.crmdm_app.R.layout.inflate_contactlist_item, LAYOUT_INFLATECONTACTLISTITEM);
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
        case  LAYOUT_ACTIVITYNEWDASHBOARD: {
          if ("layout/activity_new_dashboard_0".equals(tag)) {
            return new ActivityNewDashboardBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for activity_new_dashboard is invalid. Received: " + tag);
        }
        case  LAYOUT_INFLATECONTACTLISTITEM: {
          if ("layout/inflate_contactlist_item_0".equals(tag)) {
            return new InflateContactlistItemBindingImpl(component, view);
          }
          throw new IllegalArgumentException("The tag for inflate_contactlist_item is invalid. Received: " + tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new com.android.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(2);

    static {
      sKeys.put(0, "_all");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(2);

    static {
      sKeys.put("layout/activity_new_dashboard_0", com.dm.crmdm_app.R.layout.activity_new_dashboard);
      sKeys.put("layout/inflate_contactlist_item_0", com.dm.crmdm_app.R.layout.inflate_contactlist_item);
    }
  }
}
