package org.cameronmoreau.wheresmymoney.app;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.cameronmoreau.wheresmymoney.R;
import org.cameronmoreau.wheresmymoney.adapters.SummaryListAdapter;
import org.cameronmoreau.wheresmymoney.adapters.SummaryListItem;
import org.cameronmoreau.wheresmymoney.adapters.SummaryListPaymentItem;
import org.cameronmoreau.wheresmymoney.model.ContactUser;
import org.cameronmoreau.wheresmymoney.model.Payment;
import org.cameronmoreau.wheresmymoney.util.ContactIconGenerator;
import org.cameronmoreau.wheresmymoney.util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Cameron on 11/25/2014.
 */
public class ViewUserFragment extends Fragment implements View.OnClickListener {

    private Context context;
    private List<SummaryListItem> summaryItems;
    private ContactUser contact;
    private float totalOwed;

    private ListView lvContact;
    private TextView tvContactName;
    private ImageView ivContactImage;
    private Button bText, bCall, bEmail;
    private View vPhone, vEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_user, null);
        context = view.getContext();

        contact = getActivity().getIntent().getParcelableExtra("ContactUser");
        totalOwed = getActivity().getIntent().getFloatExtra("PaymentAmount", 0);

        //Get Payments
        DatabaseHelper db = new DatabaseHelper(context);
        List<Payment> pList = db.getAllPaymentsForUser(contact.getId());

        summaryItems = new ArrayList<SummaryListItem>();
        //summaryItems.add(new SummaryListContact(context, contact, totalOwed));
        for (Payment p : pList) {
            summaryItems.add(new SummaryListPaymentItem(context, p));
        }

        //Init widgets
        lvContact = (ListView) view.findViewById(R.id.lv_contact);
        tvContactName = (TextView) view.findViewById(R.id.tv_user_name);
        ivContactImage = (ImageView) view.findViewById(R.id.iv_user_image);
        bText = (Button) view.findViewById(R.id.button_text);
        bCall = (Button) view.findViewById(R.id.button_call);
        bEmail = (Button) view.findViewById(R.id.button_email);
        vPhone = view.findViewById(R.id.v_phone);
        vEmail = view.findViewById(R.id.v_email);

        //Name and Image
        tvContactName.setText(contact.getName());

        //Link buttons to listener
        bText.setOnClickListener(this);
        bCall.setOnClickListener(this);
        bEmail.setOnClickListener(this);


        //Make sure user had phone and email
        if (contact.getPhone() == null) {
            bText.setVisibility(View.GONE);
            bCall.setVisibility(View.GONE);
            vPhone.setVisibility(View.GONE);
            vEmail.setVisibility(View.GONE);
        } else if (contact.getEmail() == null) {
            bEmail.setVisibility(View.GONE);
            vEmail.setVisibility(View.GONE);
        }

        //Contact Image
        if (contact.getPhotoUri() != null) {
            ivContactImage.setImageURI(Uri.parse(contact.getPhotoUri()));
            ivContactImage.setImageDrawable(new ContactIconGenerator(context,
                    Uri.parse(contact.getPhotoUri())).generateIcon(ContactIconGenerator.LARGE));
        } else {
            String letter = Character.toString(contact.getName().charAt(0));
            ContactIconGenerator contactIcon = new ContactIconGenerator(context, letter, contact.getColor());
            ivContactImage.setImageDrawable(contactIcon.generateIcon(ContactIconGenerator.LARGE));
        }

        SummaryListAdapter adapter = new SummaryListAdapter(context, summaryItems);
        lvContact.setAdapter(adapter);

        return view;
    }

    private String contactMessage() {
        String message = "";
        String price = String.format("$%.2f", Math.abs(totalOwed));
        boolean owed = (totalOwed > 0) ? true : false;

        if (owed) {
            message = "Hey, it looks like you still owe me " + price
                    + ". If I could get that back soon, that would be great. Thanks!";
        } else {
            message = "Hey, it looks like I still owe you " + price
                    + ". Don't worry, I didn't forget! Ill make sure to pay you back soon.";
        }

        return message;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_text:
                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(Uri.parse("smsto:" + Uri.encode(contact.getPhone())));
                sendIntent.putExtra("sms_body", contactMessage());
                context.startActivity(sendIntent);
                break;
            case R.id.button_call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact.getPhone()));
                context.startActivity(intent);
                break;
            case R.id.button_email:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + contact.getEmail()));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Money Loan");
                emailIntent.putExtra(Intent.EXTRA_TEXT, contactMessage());
                context.startActivity(Intent.createChooser(emailIntent, "Chooser Title"));
                break;
        }
    }
}
