package com.example.trougevin.metsy;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class ProfilePage extends Activity {

    TextView login;
    TextView password_1;
    TextView password_2;
    TextView mail;

    String type_txt;
    boolean type;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        //Intent intent = getIntent();

        //type_txt = intent.getStringExtra("type");

        //if (type_txt.compareTo("inscription")!=0){
        //    type = false;
        login = findViewById(R.id.loginid);
        password_1 = findViewById(R.id.passwordid);
        password_2 = findViewById(R.id.confirmid);
        mail = findViewById(R.id.mailid);

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                user = bundle.getParcelable("userInfo");
                login.setText(user.getName());
                mail.setText(user.getMail());
            }
        /*}
        else
            type = true;*/
    }

    public void confirm_clk(View view) throws InterruptedException {
        int test = 0;
        String ret = "";

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
            //serveur.send(login.getText().toString(),password_1.getText().toString(),"create", mail.getText().toString());

            if (serveur.send_2(login.getText().toString(),password_1.getText().toString(),"create", mail.getText().toString())) {
                ret = serveur.retour;
            }

            //TextView temp = findViewById(R.id.textView15);
            //temp.setText(ret);

            login.setText(ret);

            //if (ret.compareTo(""))
            //finish();
        }
    }
}
