package org.cameronmoreau.wheresmymoney.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import org.cameronmoreau.wheresmymoney.R;
import org.cameronmoreau.wheresmymoney.app.ViewUserActivity;
import org.cameronmoreau.wheresmymoney.model.ContactUser;
import org.cameronmoreau.wheresmymoney.model.Payment;
import org.cameronmoreau.wheresmymoney.util.ContactIconGenerator;
import org.cameronmoreau.wheresmymoney.util.ContactLoader;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by Cameron on 11/21/2014.
 */
public class SummaryListRow implements SummaryListItem {
    private Context context;
    private Payment payment;
    private ContactUser contact;

    public SummaryListRow(Context context, Payment payment) {
        this.context = context;
        this.payment = payment;

        contact = new ContactLoader(context, payment.getContact().getLookupKey()).getContact();
    }

    private static class ViewHolder {
        LinearLayout container;
        ImageView image;
        TextView firstName, lastName, date, owed;
        ImageButton overflow;
    }

    @Override
    public int getViewType() {
        return RowType.LIST_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.summary_list_row, null);

            holder.container = (LinearLayout) convertView.findViewById(R.id.layout_summary_row);
            holder.image = (ImageView) convertView.findViewById(R.id.imageview_summary_contact);
            holder.firstName = (TextView) convertView.findViewById(R.id.tv_summary_firstname);
            holder.lastName = (TextView) convertView.findViewById(R.id.tv_summary_lastname);
            holder.date = (TextView) convertView.findViewById(R.id.tv_summary_date);
            holder.owed = (TextView) convertView.findViewById(R.id.tv_summary_owed);
            holder.overflow = (ImageButton) convertView.findViewById(R.id.imagebutton_summary_overflow);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final PopupMenu contextPopup = new PopupMenu(context, convertView.findViewById(R.id.imagebutton_summary_overflow));
        contextPopup.inflate(R.menu.menu_popup);

        if (contact.getPhotoUri() != null) {
            holder.image.setImageURI(Uri.parse(contact.getPhotoUri()));
            holder.image.setImageDrawable(new ContactIconGenerator(context,
                    Uri.parse(contact.getPhotoUri())).generateIcon());
        } else {
            String letter = Character.toString(contact.getName().charAt(0));
            ContactIconGenerator contactIcon = new ContactIconGenerator(context, letter, contact.getColor());
            holder.image.setImageDrawable(contactIcon.generateIcon());
        }

        //Set name - Make sure the user had more than one name stored
        if (contact.getName().length() > 1) {
            String[] names = contact.getName().split(" ");
            holder.firstName.setText(names[0]);
            holder.lastName.setText(names[names.length - 1]);
        } else {
            holder.firstName.setText(contact.getName());
        }

        float amount = payment.getAmount();
        if (amount < 0) {
            holder.owed.setTextColor(context.getResources().getColor(R.color.primary_red));
            holder.owed.setText(String.format("-$%.2f", amount * -1));
        } else {
            holder.owed.setTextColor(context.getResources().getColor(R.color.price_green));
            holder.owed.setText(String.format("$%.2f", amount));
        }

        //Setup date
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatOut = new SimpleDateFormat("MM/dd/yyyy");
            holder.date.setText(formatOut.format(format.parse(payment.getCreatedAt())));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        contextPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_payed_off_full:
                        Toast.makeText(context, "Payed off", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_payed_off_part:
                        Toast.makeText(context, "Payed off", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contextPopup.show();
            }
        });

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ViewUserActivity.class);
                i.putExtra("Type", "user");
                i.putExtra("ContactUser", contact);
                i.putExtra("PaymentAmount", payment.getAmount());
                context.startActivity(i);
            }
        });

        holder.container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "Cash in", Toast.LENGTH_SHORT).show();

                //Intent i = new Intent(context, ViewUserActivity.class);
                //PendingIntent pi = PendingIntent.getActivity(context, 0, i, 0);

                /*Notification n = new Notification.Builder(context)
                        .setContentTitle("You are owed money")
                        .setContentText(firstName + " " + lastName + " still owes you " + owed)
                        .setSmallIcon(imageId)
                        .setContentIntent(pi)
                        .setAutoCancel(false)
                        .addAction(R.drawable.ic_launcher, "Call", pi).build();*/

                /*NotificationManager notificationManager = (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);

                notificationManager.notify(0, n);*/

                return true;
            }
        });

        return convertView;
    }
}
