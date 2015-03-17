package org.cameronmoreau.wheresmymoney.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.cameronmoreau.wheresmymoney.R;
import org.cameronmoreau.wheresmymoney.model.ContactUser;
import org.cameronmoreau.wheresmymoney.model.Payment;
import org.cameronmoreau.wheresmymoney.util.ContactIconGenerator;
import org.cameronmoreau.wheresmymoney.util.ContactLoader;
import org.cameronmoreau.wheresmymoney.util.DatabaseHelper;

/**
 * Created by Cameron on 11/19/2014.
 */
public class AddPaymentFragment extends Fragment implements View.OnClickListener {

    //Bug: Doesn't store bPrice amount when phone rotates
    private Context context;
    private ContactUser contactUser;

    private PriceInputDialog priceInputDialog;

    private ImageView ivContact;
    private EditText etDesc;
    private Button bPrice, bReminder, bIOwe, bTheyOwe;
    private TextView tvName, tvPhone, tvEmail;

    private boolean theyOwe = true;

    public AddPaymentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_payment, container, false);

        //Setup and sent user
        context = view.getContext();
        contactUser = getActivity().getIntent().getParcelableExtra("ContactUser");

        priceInputDialog = new PriceInputDialog();
        getActivity().getActionBar().getCustomView().findViewById(R.id.actionbar_done)
                .setOnClickListener(this);

        //Init widgets
        ivContact = (ImageView) view.findViewById(R.id.imageview_contact);
        tvName = (TextView) view.findViewById(R.id.tv_contact_name);
        tvPhone = (TextView) view.findViewById(R.id.tv_contact_phone);
        tvEmail = (TextView) view.findViewById(R.id.tv_contact_email);
        etDesc = (EditText) view.findViewById(R.id.et_desc);
        bPrice = (Button) view.findViewById(R.id.button_price);
        bReminder = (Button) view.findViewById(R.id.button_reminder);
        bIOwe = (Button) view.findViewById(R.id.button_i_owe);
        bTheyOwe = (Button) view.findViewById(R.id.button_they_owe);

        //Set widget attrs
        bPrice.setOnClickListener(this);
        bReminder.setOnClickListener(this);
        bIOwe.setOnClickListener(this);
        bTheyOwe.setOnClickListener(this);
        resetOweButtons(theyOwe);

        priceInputDialog.setLinkedButton(bPrice);

        //Stuff about the selected contact
        tvName.setText(contactUser.getName());
        if(contactUser.getPhotoUri() != null) {
            Uri photo = Uri.parse(contactUser.getPhotoUri());
            ivContact.setImageURI(photo);
            ivContact.setImageDrawable(new ContactIconGenerator(context, photo).generateIcon());
        } else {
            String letter = Character.toString(contactUser.getName().charAt(0));
            ContactIconGenerator contactIcon = new ContactIconGenerator(context, letter, contactUser.getColor());
            ivContact.setImageDrawable(contactIcon.generateIcon());
        }

        if(contactUser.getPhone() != null)
            tvPhone.setText(contactUser.getPhone());
        else
            tvPhone.setText("No phone number found");

        if(contactUser.getEmail() != null)
            tvEmail.setText(contactUser.getEmail());
        else
            tvEmail.setText("No email found");

        //Start price input dialog on start
        priceInputDialog.show(getFragmentManager(), "priceInputDialog");

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_i_owe:
                resetOweButtons(false);
                break;
            case R.id.button_they_owe:
                resetOweButtons(true);
                break;
            case R.id.button_price:
                priceInputDialog.show(getFragmentManager(), "priceInputDialog");
                break;
            case R.id.actionbar_done:
                String amount = bPrice.getText().subSequence(1, bPrice.getText().length()).toString();
                if(!amount.equals("0.00")) {
                    float price = Float.parseFloat(amount);
                    if(!theyOwe) price = price * -1;

                    Payment payment = new Payment(
                            contactUser,
                            price,
                            etDesc.getText().toString()
                    );

                    //Save into database
                    DatabaseHelper db = new DatabaseHelper(context);
                    long paymentId = db.createPayment(payment);
                    db.close();

                    //Transfer back to payment id
                    Intent i = new Intent();
                    i.putExtra("PaymentId", paymentId);

                    getActivity().setResult(Activity.RESULT_OK, i);
                    getActivity().finish();
                } else {
                    Toast.makeText(context, "The price cannot be $0.00", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void resetOweButtons(boolean theyOwe) {
        if(theyOwe) {
            bTheyOwe.setBackgroundColor(getResources().getColor(R.color.main_accent_light));
            bTheyOwe.setTextColor(Color.WHITE);
            bIOwe.setBackgroundColor(getResources().getColor(R.color.owe_button_bg));
            bIOwe.setTextColor(Color.BLACK);
        } else {
            bIOwe.setBackgroundColor(getResources().getColor(R.color.primary_red));
            bIOwe.setTextColor(Color.WHITE);
            bTheyOwe.setBackgroundColor(getResources().getColor(R.color.owe_button_bg));
            bTheyOwe.setTextColor(Color.BLACK);
        }

        this.theyOwe = theyOwe;
    }
}
