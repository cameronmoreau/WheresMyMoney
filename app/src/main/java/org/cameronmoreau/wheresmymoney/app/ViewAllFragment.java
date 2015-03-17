package org.cameronmoreau.wheresmymoney.app;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.cameronmoreau.wheresmymoney.R;
import org.cameronmoreau.wheresmymoney.adapters.SummaryListAdapter;
import org.cameronmoreau.wheresmymoney.adapters.SummaryListItem;
import org.cameronmoreau.wheresmymoney.model.PaymentBundle;
import org.cameronmoreau.wheresmymoney.util.DatabaseHelper;

import java.util.ArrayList;

/**
 * Created by Cameron on 12/3/2014.
 */
public class ViewAllFragment extends Fragment {

    private Context context;
    private SummaryListAdapter adapter;
    private ListView lvAll;
    private ArrayList<SummaryListItem> lvData;

    public ViewAllFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_all, null);
        context = view.getContext();

        updateSummaryList(PaymentBundle.ALL);
        adapter = new SummaryListAdapter(context, lvData);

        lvAll = (ListView) view.findViewById(R.id.lv_all);
        lvAll.setAdapter(adapter);

        return view;
    }

    private void updateSummaryList(int paymentViewType) {
        DatabaseHelper db = new DatabaseHelper(context);
        PaymentBundle paymentBundle = new PaymentBundle(context, db.getAllPayments(), db.getAllContacts());
        db.close();
        lvData = paymentBundle.toSummaryList(paymentViewType, false);
    }
}

