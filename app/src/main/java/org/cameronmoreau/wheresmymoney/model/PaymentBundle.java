package org.cameronmoreau.wheresmymoney.model;

import android.content.Context;
import android.util.Log;

import org.cameronmoreau.wheresmymoney.adapters.SummaryListHeader;
import org.cameronmoreau.wheresmymoney.adapters.SummaryListItem;
import org.cameronmoreau.wheresmymoney.adapters.SummaryListRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Cameron on 11/21/2014.
 */
public class PaymentBundle {

    public static final int ALL = 0;
    public static final int I_OWE = 1;
    public static final int THEY_OWE = 2;
    public static final int DEFAULT = 3;

    Context context;
    List<Payment> payments;
    HashMap<Long, ArrayList<Payment>> contactPayments;

    public PaymentBundle(Context context, List<Payment> payments, List<ContactUser> contacts) {
        this.context = context;
        this.payments = payments;
        this.contactPayments = new HashMap<Long, ArrayList<Payment>>();

        //Start hashmap with contact IDs
        for(ContactUser c : contacts) {
            contactPayments.put(c.getId(), new ArrayList<Payment>());
        }

        //Populate hashmap with payments array
        for(int i = 0; i < payments.size(); i++) {
            Payment p = payments.get(i);
            ArrayList<Payment> tempList = contactPayments.get(p.getContact().getId());
            tempList.add(p);
            contactPayments.put(p.getContact().getId(), tempList);
        }
    }

    public ArrayList<SummaryListItem> toSummaryList(int paymentView, boolean showHeader) {
        ArrayList<SummaryListItem> list = new ArrayList<SummaryListItem>();
        float totalAmountOwed = 0;
        float totalAmountIOwe = 0;
        for(Map.Entry<Long, ArrayList<Payment>> entry : contactPayments.entrySet()) {
            float amount = 0;
            String date = null;
            String desc = null;
            ContactUser contact = new ContactUser();
            for(int i = 0; i < entry.getValue().size(); i++) {
                Payment p = entry.getValue().get(i);
                amount += p.getAmount();
                if(i == entry.getValue().size() - 1) {
                    date = p.getCreatedAt();
                    desc = p.getDesc();
                    contact = p.getContact();
                }
            }

            if(amount > 0) totalAmountOwed += amount;
            else totalAmountIOwe += amount;
            Payment p = new Payment(contact, amount, desc);
            p.setCreatedAt(date);

            if(paymentView == ALL) {
                list.add(new SummaryListRow(context, p));
            } else if(paymentView == DEFAULT && amount != 0) {
                list.add(new SummaryListRow(context, p));
            } else if(paymentView == I_OWE && amount < 0) {
                list.add(new SummaryListRow(context, p));
            } else if(paymentView == THEY_OWE && amount > 0) {
                list.add(new SummaryListRow(context, p));
            }
        }

        if(showHeader) {
            list.add(0, new SummaryListHeader(
                    context,
                    new float[] { totalAmountOwed, totalAmountIOwe },
                    list.size()
            ));
        }

        return list;
    }
}
