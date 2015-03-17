package org.cameronmoreau.wheresmymoney.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.cameronmoreau.wheresmymoney.R;
import org.cameronmoreau.wheresmymoney.adapters.SummaryListAdapter;
import org.cameronmoreau.wheresmymoney.adapters.SummaryListItem;
import org.cameronmoreau.wheresmymoney.adapters.SummaryListRow;
import org.cameronmoreau.wheresmymoney.model.ContactUser;
import org.cameronmoreau.wheresmymoney.model.Payment;
import org.cameronmoreau.wheresmymoney.model.PaymentBundle;
import org.cameronmoreau.wheresmymoney.util.ContactLoader;
import org.cameronmoreau.wheresmymoney.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cameron on 11/19/2014.
 */
public class SummaryListFragment extends Fragment implements View.OnClickListener {

    static final int CONTACTS_PERMISSION_REQUEST = 101;
    static final int PAYMENT_ADD_PERMISSION_REQUEST = 103;

    private Context context;
    private SummaryListAdapter arrayAdapter;

    private FloatingActionsMenu fabMenu;
    private FloatingActionButton fabAddContact, fabAddNew;
    private ListView lvOwed;

    private ArrayList<SummaryListItem> lvData;

    public SummaryListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        context = view.getContext();

        //Get data for list
        updateSummaryList(PaymentBundle.DEFAULT);
        arrayAdapter = new SummaryListAdapter(context, lvData);

        //Init widgets
        fabMenu = (FloatingActionsMenu) view.findViewById(R.id.fab_menu);
        fabAddContact = (FloatingActionButton) view.findViewById(R.id.fab_add_contact);
        fabAddNew = (FloatingActionButton) view.findViewById(R.id.fab_add_new_user);
        lvOwed = (ListView) view.findViewById(R.id.lv_owed);

        fabAddContact.setOnClickListener(this);
        fabAddNew.setOnClickListener(this);

        lvOwed.setAdapter(arrayAdapter);

        //View lvHeader = arrayAdapter.getView(0, view, null);


        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fab_add_contact:
                Intent contacts = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(contacts, CONTACTS_PERMISSION_REQUEST);
                break;
            case R.id.fab_add_new_user:
                Toast.makeText(getActivity().getBaseContext(), "TODO: New user", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void updateSummaryList(int paymentViewType) {
        DatabaseHelper db = new DatabaseHelper(context);
        PaymentBundle paymentBundle = new PaymentBundle(context, db.getAllPayments(), db.getAllContacts());
        db.close();
        lvData = paymentBundle.toSummaryList(paymentViewType, true);
    }

    private void resetSummaryList() {
        arrayAdapter.clear();
        for(SummaryListItem si : lvData) {
            arrayAdapter.add(si);
        }
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CONTACTS_PERMISSION_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contactUri = data.getData();
                ContactLoader contactLoader = new ContactLoader(context, contactUri);
                ContactUser contact = contactLoader.getContact();

                fabMenu.collapse();

                Intent i = new Intent(context, AddPaymentActivity.class);
                i.putExtra("ContactUser", contact);
                getActivity().startActivityForResult(i, PAYMENT_ADD_PERMISSION_REQUEST);
            }
        } else if (requestCode == PAYMENT_ADD_PERMISSION_REQUEST) {
            if(resultCode == Activity.RESULT_OK) {
                long paymentId = data.getExtras().getLong("PaymentId");
                updateSummaryList(PaymentBundle.DEFAULT);
                resetSummaryList();
            }
        }
    }
}
