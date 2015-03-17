package org.cameronmoreau.wheresmymoney.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.cameronmoreau.wheresmymoney.R;

/**
 * Created by Cameron on 11/21/2014.
 */
public class SummaryListHeader implements SummaryListItem {

    private Context context;
    private int total;
    //They, I
    private float[] loans;

    public SummaryListHeader(Context context, float[] loans, int total) {
        this.context = context;
        this.loans = loans;
        this.total = total;
    }

    private static class ViewHolder {
        TextView tvTotal, tvTheyOwe, tvIOwe;
    }

    @Override
    public int getViewType() {
        return RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.summary_list_header, null);

            holder.tvTotal = (TextView) convertView.findViewById(R.id.tv_summary_total);
            holder.tvIOwe = (TextView) convertView.findViewById(R.id.tv_summary_iowe);
            holder.tvTheyOwe = (TextView) convertView.findViewById(R.id.tv_summary_they_owe);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvTotal.setText(String.valueOf(total));
        if (loans[1] < 0) holder.tvIOwe.setText(String.format("-$%.2f", loans[1] * -1));
        else holder.tvIOwe.setText(String.format("$%.2f", loans[1]));
        holder.tvTheyOwe.setText(String.format("$%.2f", loans[0]));


        return convertView;
    }
}
