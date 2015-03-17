package org.cameronmoreau.wheresmymoney.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.cameronmoreau.wheresmymoney.model.ContactUser;
import org.cameronmoreau.wheresmymoney.model.Payment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Cameron on 11/18/2014.
 */
public class DatabaseHelper extends SQLiteOpenHelper {
    //Logcat
    private static final String LOG = "DatabaseHelper";

    //Database Version
    private static final int DATABASE_VERSION = 2;

    //Database Name
    private static final String DATABASE_NAME = "moneyDatabase";

    //Table Names
    private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_REMINDERS = "reminders";
    private static final String TABLE_PAYMENTS = "payments";

    //Common Columns
    private static final String KEY_ID = "_id";
    private static final String KEY_CREATED_AT = "created_at";
    private static final String KEY_CONTACT_ID = "contact_id";

    //Contact Columns
    private static final String KEY_LOOKUP_KEY = "lookup_key";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_PHOTO_URI = "photo_uri";
    private static final String KEY_COLOR = "color";

    //Reminders Columns
    private static final String KEY_REMINDER_ID = "reminder_id";

    //Payments Columns
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DESC = "desc";

    //Create Tables
    private static final String CREATE_TABLE_CONTACTS = "CREATE TABLE " + TABLE_CONTACTS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_LOOKUP_KEY + " TEXT,"
            + KEY_NAME + " TEXT,"
            + KEY_EMAIL + " TEXT,"
            + KEY_PHONE + " TEXT,"
            + KEY_PHOTO_URI + " TEXT,"
            + KEY_COLOR + " TEXT);";

    private static final String CREATE_TABLE_REMINDERS = "CREATE TABLE " + TABLE_REMINDERS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_CONTACT_ID + " INTEGER,"
            + KEY_REMINDER_ID + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ");";

    private static final String CREATE_TABLE_PAYMENTS = "CREATE TABLE " + TABLE_PAYMENTS
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + KEY_CONTACT_ID + " INTEGER,"
            + KEY_AMOUNT + " FLOAT,"
            + KEY_DESC + " TEXT,"
            + KEY_CREATED_AT + " DATETIME" + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACTS);
        db.execSQL(CREATE_TABLE_REMINDERS);
        db.execSQL(CREATE_TABLE_PAYMENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REMINDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAYMENTS);

        onCreate(db);
    }

    //User functions
    public long createContact(ContactUser user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_LOOKUP_KEY, user.getLookupKey());
        values.put(KEY_NAME, user.getName());
        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_EMAIL, user.getEmail());
        values.put(KEY_PHOTO_URI, user.getPhotoUri());
        values.put(KEY_COLOR, user.getColor());

        long userId = db.insert(TABLE_CONTACTS, null, values);

        db.close();
        return userId;
    }

    public ContactUser getContact(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContactUser user = new ContactUser();

        String query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE "
                + KEY_ID + " = " + id;

        Cursor c = db.rawQuery(query, null);
        if(c != null && c.getCount() > 0)
            c.moveToFirst();
        else
            return null;

        user.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        user.setLookupKey(c.getString(c.getColumnIndex(KEY_LOOKUP_KEY)));
        user.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        user.setPhone(c.getString(c.getColumnIndex(KEY_PHONE)));
        user.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
        user.setPhotoUri(c.getString(c.getColumnIndex(KEY_PHOTO_URI)));
        user.setColor(c.getString(c.getColumnIndex(KEY_COLOR)));

        db.close();
        return user;
    }

    public ContactUser getContact(String lookupKey) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContactUser user = new ContactUser();

        String query = "SELECT * FROM " + TABLE_CONTACTS + " WHERE "
                + KEY_LOOKUP_KEY + " = ?";

        Cursor c = db.rawQuery(query, new String[] { String.valueOf(lookupKey) });
        if(c != null && c.getCount() > 0)
            c.moveToFirst();
        else
            return null;

        user.setId(c.getLong(c.getColumnIndex(KEY_ID)));
        user.setLookupKey(c.getString(c.getColumnIndex(KEY_LOOKUP_KEY)));
        user.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        user.setPhone(c.getString(c.getColumnIndex(KEY_PHONE)));
        user.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
        user.setPhotoUri(c.getString(c.getColumnIndex(KEY_PHOTO_URI)));
        user.setColor(c.getString(c.getColumnIndex(KEY_COLOR)));

        db.close();
        return user;
    }

    public ArrayList<ContactUser> getAllContacts() {
        ArrayList<ContactUser> users = new ArrayList<ContactUser>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_CONTACTS;

        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()) {
            do {
                ContactUser user = new ContactUser();
                user.setId(c.getLong(c.getColumnIndex(KEY_ID)));
                user.setLookupKey(c.getString(c.getColumnIndex(KEY_LOOKUP_KEY)));
                user.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                user.setPhone(c.getString(c.getColumnIndex(KEY_PHONE)));
                user.setEmail(c.getString(c.getColumnIndex(KEY_EMAIL)));
                user.setPhotoUri(c.getString(c.getColumnIndex(KEY_PHOTO_URI)));
                user.setColor(c.getString(c.getColumnIndex(KEY_COLOR)));

                users.add(user);
            } while(c.moveToNext());
        }

        db.close();
        return users;
    }

    //TODO: Update user

    /*public void deleteUser(long user_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USERS, KEY_ID + " = ?", new String[] { String.valueOf(user_id) });
        db.close();
    }*/

    //Reminder functions
   /* public long createReminder(PaymentReminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_ID, reminder.getContactId());
        values.put(KEY_REMINDER_ID, reminder.getReminderId());
        values.put(KEY_CREATED_AT, getDateTime());

        long reminder_id = db.insert(TABLE_REMINDERS, null, values);

        db.close();
        return reminder_id;
    }

    public int updateReminder(PaymentReminder reminder) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_ID, reminder.getContactId());
        values.put(KEY_REMINDER_ID, reminder.getReminderId());
        values.put(KEY_CREATED_AT, getDateTime());

        int result = db.update(TABLE_REMINDERS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(reminder.getId()) });

        db.close();
        return result;
    }

    public void deleteReminder(long reminder_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_REMINDERS, KEY_ID + " = ?", new String[] { String.valueOf(reminder_id) });
        db.close();
    }*/

    //Payments
    public long createPayment(Payment payment) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_CONTACT_ID, payment.getContact().getId());
        values.put(KEY_AMOUNT, payment.getAmount());
        values.put(KEY_DESC, payment.getDesc());
        values.put(KEY_CREATED_AT, getDateTime());

        long payment_id = db.insert(TABLE_PAYMENTS, null, values);

        db.close();
        return payment_id;
    }

    public List<Payment> getAllPayments() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Payment> payments = new ArrayList<Payment>();
        String query = "SELECT * FROM " + TABLE_PAYMENTS;

        Cursor c = db.rawQuery(query, null);
        if(c.moveToFirst()) {
            do {
                Payment userPayment = new Payment();
                userPayment.setId(c.getLong(c.getColumnIndex(KEY_ID)));
                userPayment.setContact(getContact(c.getLong(c.getColumnIndex(KEY_CONTACT_ID))));
                userPayment.setAmount(c.getFloat(c.getColumnIndex(KEY_AMOUNT)));
                userPayment.setDesc(c.getString(c.getColumnIndex(KEY_DESC)));
                userPayment.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                payments.add(userPayment);
            } while(c.moveToNext());
        }

        db.close();
        return payments;
    }

    public List<Payment> getAllPaymentsForUser(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        List<Payment> payments = new ArrayList<Payment>();
        String query = "SELECT * FROM " + TABLE_PAYMENTS + " WHERE "
                + KEY_CONTACT_ID + " = ? ORDER BY " + KEY_CREATED_AT + " DESC";

        Cursor c = db.rawQuery(query, new String[] { String.valueOf(id) });
        if(c.moveToFirst()) {
            do {
                Payment userPayment = new Payment();
                userPayment.setId(c.getLong(c.getColumnIndex(KEY_ID)));
                userPayment.setContact(getContact(c.getLong(c.getColumnIndex(KEY_CONTACT_ID))));
                userPayment.setAmount(c.getFloat(c.getColumnIndex(KEY_AMOUNT)));
                userPayment.setDesc(c.getString(c.getColumnIndex(KEY_DESC)));
                userPayment.setCreatedAt(c.getString(c.getColumnIndex(KEY_CREATED_AT)));

                payments.add(userPayment);
            } while(c.moveToNext());
        }

        db.close();
        return payments;
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
