package org.cameronmoreau.wheresmymoney.adapters;

import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Cameron on 11/21/2014.
 */
public interface SummaryListItem {
    public enum RowType {
        LIST_ITEM,
        PAYMENT_ITEM,
        HEADER_ITEM
    }

    public int getViewType();

    public View getView(LayoutInflater inflater, View convertView);
}
