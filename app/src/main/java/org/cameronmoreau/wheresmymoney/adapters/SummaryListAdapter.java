package org.cameronmoreau.wheresmymoney.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Cameron on 11/21/2014.
 */
public class SummaryListAdapter extends ArrayAdapter<SummaryListItem> {

    private Context context;
    private LayoutInflater mInflater;
    private List<SummaryListItem> items;

    public SummaryListAdapter(Context context, List<SummaryListItem> items) {
        super(context, 0, items);
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.items = items;
    }

    @Override
    public int getViewTypeCount() {
        return SummaryListItem.RowType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(mInflater, convertView);
    }
}
