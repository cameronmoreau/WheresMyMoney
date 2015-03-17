package org.cameronmoreau.wheresmymoney.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import org.cameronmoreau.wheresmymoney.model.ContactUser;

/**
 * Created by Cameron on 11/18/2014.
 */
public class ContactLoader {

    private Context context;
    private ContactUser contact;
    private String lookupKey;

    private String[] projectionContact = {
            ContactsContract.Contacts.DISPLAY_NAME,
            ContactsContract.Contacts.PHOTO_URI
    };

    private String[] projectionLookup = { ContactsContract.Contacts.LOOKUP_KEY };
    private String[] projectionPhone = { ContactsContract.CommonDataKinds.Phone.NUMBER };
    private String[] projectionEmail = { ContactsContract.CommonDataKinds.Email.ADDRESS };

    public ContactLoader(Context context, String lookupKey) {
        this.context = context;
        this.lookupKey = lookupKey;

        fetchData();
    }

    public ContactLoader(Context context, Uri contactUri) {
        this.context = context;

        this.lookupKey = fetchLookupKey(contactUri);
        fetchData();
    }

    public ContactUser getContact() { return contact; }

    private String fetchLookupKey(Uri contactUri) {
        Cursor cursor = context.getContentResolver()
                .query(contactUri, projectionLookup, null, null, null);

        if(cursor.moveToFirst()) {
            String s = cursor.getString(cursor.getColumnIndex(projectionLookup[0]));
            Log.e("CONTACT", "LOOKUP: " + s);
            return s;
        }
        return null;
    }

    private void fetchData() {
        //Check if user exists in database
        DatabaseHelper db = new DatabaseHelper(context);
        contact = db.getContact(lookupKey);

        if(contact == null) {
            contact = new ContactUser();
            contact.setLookupKey(lookupKey);
            contact.setColor(contact.randomColor(context.getResources()));
            long tempContactId = db.createContact(contact);
            contact = db.getContact(tempContactId);
        }
        db.close();

        //Basic contact data
        Cursor cursor = context.getContentResolver()
                .query(ContactsContract.Contacts.CONTENT_URI, projectionContact,
                        ContactsContract.Contacts.LOOKUP_KEY + " = ?",
                        new String[] { lookupKey }, null);

        if(cursor.moveToFirst()) {
            contact.setName(cursor.getString(cursor.getColumnIndex(projectionContact[0])));
            contact.setPhotoUri(cursor.getString(cursor.getColumnIndex(projectionContact[1])));
        }

        //Phone data
        cursor = context.getContentResolver()
                .query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projectionPhone,
                        ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY + " = ?",
                        new String[] { lookupKey }, null);

        if(cursor.moveToFirst()) {
            contact.setPhone(cursor.getString(cursor.getColumnIndex(projectionPhone[0])));
        }

        //Email data
        cursor = context.getContentResolver()
                .query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, projectionEmail,
                        ContactsContract.CommonDataKinds.Email.LOOKUP_KEY + " = ?",
                        new String[] { lookupKey }, null);

        if(cursor.moveToFirst()) {
            contact.setEmail(cursor.getString(cursor.getColumnIndex(projectionEmail[0])));
        }

        cursor.close();
    }
}
