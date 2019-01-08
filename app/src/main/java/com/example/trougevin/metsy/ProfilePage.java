/****************************************************
 Ce fichier permet la mmodification du profil de l'utilisateur
 ***************************************************/

package com.example.trougevin.metsy;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

public class ProfilePage extends Activity {


    TextView login;
    TextView password_1;
    TextView password_2;
    TextView mail;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            user = bundle.getParcelable("userInfo");
        }

        login = findViewById(R.id.loginid);
        password_1 = findViewById(R.id.passwordid);
        password_2 = findViewById(R.id.confirmid);
        mail = findViewById(R.id.mailid);

        mail.setText(user.getMail());
        login.setText(user.getName());
        password_1.setText(user.getPassword());
        password_2.setText(user.getPassword());

    }

    public void confirm_clk(View view) throws InterruptedException {

        Serveur serveur = new Serveur ();

        if (password_1.getText().toString().compareTo(password_2.getText().toString()) == 0){
            if (user.getName().compareTo(login.getText().toString())!=0){
                if (serveur.send_2(user.getName(), user.getPassword(),"user",login.getText().toString())){
                    user.setName(login.getText().toString());
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfilePage.this, R.style.AlertDialogStyle);
                    builder.setTitle("ERREUR");
                    builder.setMessage("login failled...");

                    AlertDialog alertDialog = builder.create();

                    if(!alertDialog.isShowing()){
                        alertDialog.show();
                    }
                }
            }
            if (user.getMail().compareTo(mail.getText().toString())!=0){
                if (serveur.send_2(user.getName(), user.getPassword(),"mail",mail.getText().toString())){
                    user.setMail(mail.getText().toString());
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfilePage.this, R.style.AlertDialogStyle);
                    builder.setTitle("ERREUR");
                    builder.setMessage("mail failled...");

                    AlertDialog alertDialog = builder.create();

                    if(!alertDialog.isShowing()){
                        alertDialog.show();
                    }
                }
            }
            if (user.getPassword().compareTo(password_1.getText().toString())!=0){
                if (serveur.send_2(user.getName(), user.getPassword(),"password",password_1.getText().toString())){
                    user.setPassword(password_1.getText().toString());
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ProfilePage.this, R.style.AlertDialogStyle);
                    builder.setTitle("ERREUR");
                    builder.setMessage("password failled...");

                    AlertDialog alertDialog = builder.create();

                    if(!alertDialog.isShowing()){
                        alertDialog.show();
                    }
                }
            }
        }
    }

}
