package ru.kholodkova.address_book.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Date;

public class Contact implements Comparable<Contact> {

    private int id;

    @NotEmpty(message = "Enter a name")
    @Size(max = 30, message = "This name is too long")
    private String name;

    @NotEmpty(message = "Enter a phone number")
    private String phone;

    private Date creationTime = new Date();

    private int usage;

    private boolean isFav;

    public Contact() {}

    public Contact(int id, String name, String phone, boolean isFav) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.creationTime = new Date();
        this.isFav = isFav;
        System.out.println("hello from contact constructor!");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public long getCreationTime() { return creationTime.getTime(); }

    public int getUsage() {
        return usage;
    }

    public boolean getIsFav() {
        return isFav;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }


    @Override
    public int compareTo(Contact o) {
        return this.getName().compareTo(o.getName());
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", isFav='" + isFav + '\'' +
                '}';
    }
}
