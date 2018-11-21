package com.example.trougevin.metsy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ProfilePage extends Activity {

    TextView login;
    TextView password_1;
    TextView password_2;
    TextView mail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        login = findViewById(R.id.loginid);
        password_1 = findViewById(R.id.passwordid);
        password_2 = findViewById(R.id.confirmid);
        mail = findViewById(R.id.mailid);
    }

    public void confirm_clk(View view) {
        int test = 0;

        if (login.getText().toString().compareTo("")==0)
            test = 1;
        if (password_1.getText().toString().compareTo("")==0)
            test = 2;
        if (password_2.getText().toString().compareTo(password_1.getText().toString())!=0)
            test = 3;
        if (mail.getText().toString().compareTo("")==0)
            test = 4;

        if (test == 0) {
            Serveur serveur = new Serveur();
            serveur.send(login.getText().toString(),password_1.getText().toString(),"create", mail.getText().toString());
            finish();
        }
    }
}
