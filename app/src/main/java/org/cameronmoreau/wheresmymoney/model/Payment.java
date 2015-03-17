package org.cameronmoreau.wheresmymoney.model;

/**
 * Created by Cameron on 11/19/2014.
 */
public class Payment {

    private long id;
    private ContactUser contact;
    private float amount;
    private String desc, createdAt;

    public Payment() {

    }

    public Payment(ContactUser contact, float amount, String desc) {
        this.contact = contact;
        this.amount = amount;
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", contact=" + contact +
                ", amount=" + amount +
                ", desc='" + desc + '\'' +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ContactUser getContact() {
        return contact;
    }

    public void setContact(ContactUser contact) {
        this.contact = contact;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
