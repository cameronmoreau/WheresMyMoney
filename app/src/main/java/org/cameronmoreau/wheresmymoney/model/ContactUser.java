package org.cameronmoreau.wheresmymoney.model;

import android.content.res.Resources;
import android.os.Parcel;
import android.os.Parcelable;

import org.cameronmoreau.wheresmymoney.R;

import java.util.Random;

/**
 * Created by Cameron on 11/18/2014.
 */
public class ContactUser implements Parcelable {

    private long id;
    private String lookupKey;
    private String name, email, phone;
    private String photoUri, color;

    public ContactUser() {}

    public ContactUser(String lookupKey, String name, String email, String phone, String photoUri, String color) {
        this.lookupKey = lookupKey;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.photoUri = photoUri;
        this.color = color;
    }

    public ContactUser(Parcel in) {
        this.id = in.readLong();
        String[] data = new String[6];
        in.readStringArray(data);

        this.lookupKey = data[0];
        this.name = data[1];
        this.email = data[2];
        this.phone = data[3];
        this.photoUri = data[4];
        this.color = data[5];
    }

    public String randomColor(Resources res) {
        String[] colors = res.getStringArray(R.array.saved_colors);
        return colors[new Random().nextInt(colors.length)];
    }

    @Override
    public String toString() {
        return "ContactUser{" +
                "id=" + id +
                ", lookupKey='" + lookupKey + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", photoUri='" + photoUri + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLookupKey() {
        return lookupKey;
    }

    public void setLookupKey(String lookupKey) {
        this.lookupKey = lookupKey;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeStringArray(new String[] {
            lookupKey, name, email, phone, photoUri, color
        });
    }

    public static final Parcelable.Creator<ContactUser> CREATOR = new Parcelable.Creator<ContactUser>() {
        public ContactUser createFromParcel(Parcel in) {
            return new ContactUser(in);
        }

        public ContactUser[] newArray(int size) {
            return new ContactUser[size];
        }
    };
}
