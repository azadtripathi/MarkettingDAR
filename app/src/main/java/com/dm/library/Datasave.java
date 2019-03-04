package com.dm.library;

import android.content.SharedPreferences;
import android.widget.ListView;

/**
 * Created by dataman on 12/12/2016.
 */
public interface Datasave
{
    public void setFlag(SharedPreferences preferences, boolean isSave, ListView listView, SharedPreferences preferences2);
    public void setOrderTranListClear(boolean isClear, ListView listView);
}