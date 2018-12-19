package com.example.trougevin.metsy;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

@SuppressLint("ParcelCreator")
public class User implements Parcelable {
    private String name;
    private String password;
    private String mail;
    private ArrayList<String> allergens;

    public User(String name, String password, String mail, ArrayList<String> allergens) {
        this.name = name;
        this.password = password;
        this.mail = mail;
        this.allergens = allergens;
    }
    public User() {
    }


    protected User(Parcel in) {
        name = in.readString();
        password = in.readString();
        mail = in.readString();
        allergens = in.createStringArrayList();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public ArrayList<String> getAllergens() {
        return allergens;
    }

    public void setAllergens(ArrayList<String> allergens) {
        this.allergens = allergens;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(password);
        dest.writeString(mail);
        dest.writeStringList(allergens);
    }
}
