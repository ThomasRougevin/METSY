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
import android.view.View;
import android.widget.TextView;

public class LogPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_page);
    }

    public void inscription_clk(View view) {
        Intent intent = new Intent(LogPage.this, AllergeneActivity.class);
        startActivity(intent);
    }

    public void connexion_clk(View view) {
    }
}
