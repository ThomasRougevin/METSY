package com.example.trougevin.metsy;

/////////////////////////////////////////////////////////////
//      curuser.csv
/////////////////////////////////////////////////////////////
//login
//password
//mail
//alergene 1
//alergene 1
//alergene 1
//...
//
/////////////////////////////////////////////////////////////

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class LogPage extends Activity {

    TextView login;
    TextView password;

    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_page);

        login = findViewById(R.id.loginid);
        password = findViewById(R.id.passwordid);
    }

    public void inscription_clk(View view) {
        Intent intent = new Intent(LogPage.this, ProfilePage.class);

        //intent.putExtra("type", "inscription");

        startActivity(intent);
    }

    public void connexion_clk(View view) throws InterruptedException {
        Serveur serveur = new Serveur();
        String ret = "none";

        ArrayList<String> list = new ArrayList<>();
        String name="";
        String mdp="";
        String mail="";

        int i = 0;

        if (serveur.send_2(login.getText().toString(), password.getText().toString(),"get","none"))
        {
            ret = serveur.retour;

            String [] spl = ret.split("\n");

            TextView bla = findViewById(R.id.txttest);
            bla.setText(spl[2]);

            for (String element : spl){
                if (i==0)
                    name = element;
                else if (i==1)
                    mdp = element;
                else if (i==2)
                    mail = element;
                else
                    list.add(element);

                i++;
            }

            user = new User (name,mdp,mail,list);

            Intent intent = new Intent (LogPage.this, AllergeneActivity.class);
            //Intent intent = new Intent (LogPage.this, ProfilePage.class);

            Bundle b = new Bundle();
            b.putParcelable("userInfo",user);
            intent.putExtras(b);

            startActivity(intent);
        }
        else
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(LogPage.this, R.style.AlertDialogStyle);
            builder.setTitle("ERROR");
            builder.setMessage("NOP");

            AlertDialog alertDialog = builder.create();

            if(!alertDialog.isShowing()){
                alertDialog.show();
            }
        }
    }
}
