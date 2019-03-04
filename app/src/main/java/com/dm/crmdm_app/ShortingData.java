package com.dm.crmdm_app;

import com.dm.model.AddContactModel;

/**
 * Created by Administrator on 6/16/2017.
 */
public class ShortingData implements java.util.Comparator<AddContactModel> {
    @Override
    public int compare(AddContactModel lhs, AddContactModel rhs) {
        return  lhs.getLead().compareToIgnoreCase(rhs.getLead());
    }
}
