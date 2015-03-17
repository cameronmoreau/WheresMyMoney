package org.cameronmoreau.wheresmymoney.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.cameronmoreau.wheresmymoney.R;
import org.cameronmoreau.wheresmymoney.model.Payment;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Cameron on 11/25/2014.
 */
public class SummaryListPaymentItem implements SummaryListItem {

    private Context context;
    private Payment payment;

    public SummaryListPaymentItem(Context context, Payment payment) {
        this.context = context;
        this.payment = payment;
    }

    private static class ViewHolder {
        TextView tvDesc, tvPrice, tvDate;
        ImageView ivStrip;
    }

    @Override
    public int getViewType() {
        return SummaryListItem.RowType.PAYMENT_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.summary_list_spec_row, null);

            holder.tvPrice = (TextView) convertView.findViewById(R.id.tv_summary_owed);
            holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_summary_desc);
            holder.tvDate = (TextView) convertView.findViewById(R.id.tv_summary_date);
            holder.ivStrip = (ImageView) convertView.findViewById(R.id.iv_summary_strip);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        float amount = payment.getAmount();
        if (amount < 0) {
            holder.tvPrice.setTextColor(context.getResources().getColor(R.color.primary_red));
            holder.tvPrice.setText(String.format("-$%.2f", amount * -1));
            holder.ivStrip.setImageDrawable(context.getResources().getDrawable(R.drawable.strip_red));
            holder.tvDesc.setText("Borrowed money");
        } else {
            holder.tvPrice.setTextColor(context.getResources().getColor(R.color.price_green));
            holder.tvPrice.setText(String.format("$%.2f", amount));
            holder.ivStrip.setImageDrawable(context.getResources().getDrawable(R.drawable.strip_green));
            holder.tvDesc.setText("Lent money");
        }

        if (!payment.getDesc().equals("")) {
            holder.tvDesc.setText(payment.getDesc());
        }

        //Setup date
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatOut = new SimpleDateFormat("MM/dd/yyyy");
            holder.tvDate.setText(formatOut.format(format.parse(payment.getCreatedAt())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return convertView;
    }
}
