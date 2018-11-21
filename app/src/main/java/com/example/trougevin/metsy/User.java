package com.example.trougevin.metsy;

import java.util.ArrayList;

public class User {
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
}
